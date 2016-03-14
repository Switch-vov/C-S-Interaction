package com.pc.socket.server;

import static com.pc.socket.Commons.DEFAULT_BUFFER_LENGTH;
import static com.pc.socket.Commons.DEFAULT_MESSAGE_CHARSET;
import static com.pc.socket.Commons.GET_FILE;
import static com.pc.socket.Commons.SEND_B_FILE;
import static com.pc.socket.Commons.SEND_FILE;
import static com.pc.socket.Commons.SEND_MESSAGE;
import static com.pc.socket.Commons.SERVER_SAVE_BASE_PATH;
import static com.pc.socket.Commons.closeStream;
import static com.pc.socket.Commons.logInfo;
import static com.pc.socket.Commons.print;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;

import com.pc.socket.Commons;
import com.pc.socket.SocketWrapper;
import com.pc.socket.client.exceptions.DownloadNotExistsFileException;
import com.pc.socket.client.exceptions.SaveExistsFileException;
/**
 * 
*
* @ClassName: Worker 
* @Description: TODO 服务器端线程包装类
* @author Switch
* @date 2016年3月13日 下午2:06:36 
*  
*
 */
public class Worker extends Thread {

	// socket包装类
	private SocketWrapper socketWrapper;
	
	// 线程名
	private String name;
	
	// 初始化
	public Worker(SocketWrapper socketWrapper , String name) {
		this.socketWrapper = socketWrapper;
		this.name = name;
		logInfo("我是线程：" + name + " , 开始启动接收客户端传来数据......");
		this.start();//启动
	}
	
	// 当没有遇到严重异常时，线程一直服务
	public void run() {
		while(true) {
			try {
				// 线程中断则break
				if(this.isInterrupted()) break;
				// 读入一个字节，判断命令类型
				byte type = socketWrapper.readByte();
				switch(type) {
					// 客户端发送信息流
					case SEND_MESSAGE : processMessage(); break;
					// 客户端发送文本文件
					case SEND_FILE : processFile(); break;
					// 客户端发送二进制文件
					case SEND_B_FILE : uploadFileContent(null); break;
					// 客户端下载文件
					case GET_FILE : downloadFile(); break;
					default : 
				}
			// 服务器文件重复异常
			}catch(SaveExistsFileException e) {
				logInfo(e.getMessage());
			// 下载文件不存在异常
			}catch(DownloadNotExistsFileException e) {
				logInfo(e.getMessage());
				
			// 之下的都是严重异常，会导致线程中断
			}catch(EOFException e) {
				logInfo("客户端关闭socket，线程 :" + name + " 结束执行.");
				break;//对方socket已经断开
			}catch(SocketException e) {
				logInfo("Socket异常：" + e.getMessage() + "，线程 :" + name + " 结束执行.");
				break;//socket异常
			}catch(Exception e) {
				e.printStackTrace();
				logInfo("线程 :" + name + " 结束执行.");
				break;
			}
		}
		// 关闭资源
		this.socketWrapper.close();
	}
	
	/**
	 * 中断
	 */
	public void interrupt() {
		if(this.isAlive()) 
			super.interrupt();
	}
	
	/**
	 * 处理客户端传来的信息流
	 * @throws IOException 
	 */
	private void processMessage() throws IOException {
		int length = socketWrapper.readInt();
		byte[]message = new byte[length];
		socketWrapper.read(message);
		logInfo("线程：" + name  + " 接受到来自客户端传来message信息：" + new String(message , DEFAULT_MESSAGE_CHARSET));
	}
	
	/**
	 * 处理客户端传来的普通文件
	 * @throws IOException
	 */
	private void processFile() throws IOException {
		// 获取字符集，过程比较复杂
		String charset = Commons.getCharsetNameByCode(socketWrapper.readByte());
		logInfo("线程：" + name + "接受来源客户端发送文件，字符集为：" + charset);
		
		uploadFileContent(charset);
	}

	/**
	 * 上传文件处理明细
	 * @param charset
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	private void uploadFileContent(String charset) throws IOException,
			UnsupportedEncodingException, FileNotFoundException {
		FileOutputStream out = null;
		try {
			//获取文件名长度以及文件名
			short length = socketWrapper.readShort();
			byte[]bytes = new byte[length];
			socketWrapper.readFull(bytes);
			String fileName = new String(bytes , DEFAULT_MESSAGE_CHARSET);
			logInfo("线程：" + name + "接受来源客户端发送文件，来源文件名为：" + fileName);
			
			// 文件存储路径
			String path = SERVER_SAVE_BASE_PATH + fileName;
			File file = new File(path);
			// 文件存在
			if(file.exists()) {
				throw new SaveExistsFileException(path);
			}
			// 表示文件可以创建
			socketWrapper.write(1);
			out = new FileOutputStream(file);
			
			//获取文件内容，输出前面部分内容
			long fileLength = socketWrapper.readLong();
			logInfo("线程：" + name + "接受来源客户端发送文件，文件长度为：" + fileLength);
			// 创建缓存byte数组
			bytes = new byte[DEFAULT_BUFFER_LENGTH];
			int allLength = 0 , i = 0;
			// 循环写入文件
			while(allLength < fileLength) {
				int len = socketWrapper.read(bytes);
				allLength += len;
				out.write(bytes, 0, len);
				
				// 打印文件明细
				if(charset != null) {
					print(new String(bytes , charset));
				}
				if(i++ % 1024 == 0) {
					print(".");
				}
			}
			logInfo("\n文件接收完毕并保存，向客户端发送确认信息 , 实际接受内容长度为：" + fileLength);
			socketWrapper.write(1);
		// 文件已存在
		}catch(SaveExistsFileException e) {
			socketWrapper.write(-1);
			throw e;
		}catch(RuntimeException e) {
			logInfo("\n文件接收失败，向客户端发送错误消息。");
			socketWrapper.write(-2);
			throw e;
		}finally {
			closeStream(out);
		}
	}
	
	/**
	 * 下载文件功能
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	private void downloadFile() throws IOException,
		UnsupportedEncodingException, FileNotFoundException {
		short fileNameLength = socketWrapper.readShort();
		byte []fileNameBytes = new byte[fileNameLength];
		socketWrapper.read(fileNameBytes);
		String fileName = new String(fileNameBytes , DEFAULT_MESSAGE_CHARSET);
		logInfo("线程：" + name + "接受客户端下载文件，下载文件名为：" + fileName);
		// 服务器文件全路径
		String absolatePath = SERVER_SAVE_BASE_PATH + fileName;
		File file = new File(absolatePath);
		// 文件存在
		if(file.exists()) {
			socketWrapper.write(1);
		// 文件不存在，抛出异常
		}else {
			socketWrapper.write(-1);
			throw new DownloadNotExistsFileException(absolatePath);
		}
		// 文件长度
		socketWrapper.write(file.length());
		// 写出文件
		socketWrapper.writeFile(absolatePath);
	}
}
