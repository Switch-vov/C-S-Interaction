package com.pc.socket.client.sender;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.pc.socket.SocketWrapper;
import com.pc.socket.client.exceptions.DirectNotExistsException;

import static com.pc.socket.Commons.*;
/**
 * 
*
* @ClassName: GetFileSender 
* @Description: TODO 下载文件请求类
* @author Switch
* @date 2016年3月13日 下午5:28:52 
*  
*
 */
public class GetFileSender implements Sendable {
	/**
	 * 文件路径
	 */
	private String saveFilePath;
	/**
	 * 文件名
	 */
	private String getFileName;

	@Override
	public byte getSendType() {
		return GET_FILE;
	}
	
	/**
	 * 准备下载文件
	 * @param tokens 字符数组
	 */
	public GetFileSender(String []tokens) {
		if (tokens.length >= 3) {
			// 获取文件路径
			saveFilePath = tokens[2];
			// 创建文件对象
			File file = new File(saveFilePath);
			if(file.exists() && file.isDirectory()) {
				// 获取文件名
				this.getFileName = tokens[1];
				// 文件路径+分隔符
				this.saveFilePath = file.getAbsolutePath() + File.separator;
			}else {
				throw new DirectNotExistsException(saveFilePath);
			}
		}else {
			throw new RuntimeException("消息格式存在问题，请使用help命令查看输入格式。");
		}
	}

	
	
	/**
	 * 下载内容
	 */
	@Override
	public void sendContent(SocketWrapper socketWrapper) throws IOException {
		println("准备下载文件：" + getFileName);
		// 文件名byte数组
		byte[] fileNameBytes = getFileName.getBytes(DEFAULT_MESSAGE_CHARSET);
		// 写出文件名长度
		socketWrapper.write((short) fileNameBytes.length);
		// 写出文件名
		socketWrapper.write(fileNameBytes);
		// 获取服务器状态
		int status = socketWrapper.readInt();
		// 服务器端返回是否可以继续上传，若出现问题则不用上传了
		if(status != 1) {
			processErrorStatus(status);
		}else {
			// 读入文件长度
			long fileLength = socketWrapper.readLong();
			int readLength = 0 , i = 0;
			// 创建输出流
			FileOutputStream out = new FileOutputStream(saveFilePath + getFileName);
			try {
				// 缓冲byte数组
				byte []bytes = new byte[DEFAULT_BUFFER_LENGTH];
				println("开始下载文件，文件长度为：" + fileLength);
				// 循环下载
				while(readLength < fileLength) {
					// 读入
					int len = socketWrapper.read(bytes);
					readLength += len;
					out.write(bytes, 0, len);
					if(++i % 1024 == 0) {
						print(".");
					}
				}
				println("开始下载完毕.......");
			}finally {
				closeStream(out);
				println("");
			}
		}
	}
	
	/**
	 * 
	 * @author Switch
	 * @function 错误状态处理
	 * @param status 错误状态
	 */
	private void processErrorStatus(int status) {
		if(status == -1) {
			println("ERROR：文件下载失败，失败原因为服务器端没有找到指定的文件....");
		}else {
			println("ERROR：文件下载失败，失败原因不确定，返回失败错误号为：" + status);
		}
	}

}
