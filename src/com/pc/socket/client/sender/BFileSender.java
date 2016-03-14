package com.pc.socket.client.sender;

import static com.pc.socket.Commons.DEFAULT_MESSAGE_CHARSET;
import static com.pc.socket.Commons.SEND_B_FILE;
import static com.pc.socket.Commons.println;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.pc.socket.SocketWrapper;

/**
 * 
*
* @ClassName: BFileSender 
* @Description: TODO 二进制文件发送类
* @author Switch
* @date 2016年3月13日 下午5:28:14 
*  
*
 */
public class BFileSender implements Sendable {
	
	protected File file;
	
	protected long fileLength;
	
	protected int minLength = 2;
	
	/**
	 * 准备二进制文件
	 * @param tokens 命令数组
	 * @throws IOException
	 */
	public BFileSender(String []tokens) throws IOException {
		if (tokens.length >= minLength) {
			// 通过路径获取文件对象
			file = new File(tokens[1]);
			if (!file.exists()) {
				throw new FileNotFoundException("文件：" + tokens[1]
						+ " 未找到，请发送本地存在的文件。");
			}
			// 获取文件长度
			this.fileLength = file.length();
		} else {
			throw new RuntimeException("消息格式存在问题，请使用help命令查看输入格式。");
		}
	}

	
	@Override
	public void sendContent(SocketWrapper socketWrapper) throws IOException {
		println("我此时向服务器端发送二进制文件，文件大小为：" + this.fileLength);
		// 发送编码，给子类使用
		sendCharset(socketWrapper);
		//文件名
		byte[] fileNameBytes = file.getName().getBytes(DEFAULT_MESSAGE_CHARSET);
		// 写出文件长度
		socketWrapper.write((short) fileNameBytes.length);
		// 写出文件名
		socketWrapper.write(fileNameBytes);
		// 从服务器读入状态
		int status = socketWrapper.readInt();
		// 服务器端返回是否可以继续上传，若出现问题则不用上传了
		if(status != 1) {
			processErrorStatus(status);
		}
		// 文件长度&内容
		socketWrapper.write(this.fileLength);
		socketWrapper.writeFile(file.getPath());
		// 从服务器读入状态
		status = socketWrapper.readInt();
		if(status != 1) {
			processErrorStatus(status);
		}else {
			println("文件发送成功，已经成功保存到服务器端.......");
		}
	}
	
	private void processErrorStatus(int status) throws IOException {
		if (status == -1) {
			throw new RuntimeException("服务器端保存失败，由于服务器端已经存在该文件导致..");
		} else if (status != 1) {
			throw new IOException("服务器端保存失败，不确定具体原因，程序将结束运行....");
		}
	}
	
	/**
	 * 
	 * @author Switch
	 * @function 发送字符集byte
	 * @param socketWrapper
	 * @throws IOException
	 */
	protected void sendCharset(SocketWrapper socketWrapper) throws IOException {
		/*你可以继承哦*/
	}

	@Override
	public byte getSendType() {
		return SEND_B_FILE;
	}
}
