package com.pc.socket.client.sender;

import static com.pc.socket.Commons.CHARSET_START;
import static com.pc.socket.Commons.SEND_FILE;

import java.io.IOException;

import com.pc.socket.Commons;
import com.pc.socket.SocketWrapper;
/**
 * 
*
* @ClassName: FileSender 
* @Description: TODO 普通文件发送类
* @author Switch
* @date 2016年3月13日 下午5:27:04 
*  
*
 */
public class FileSender extends BFileSender {

	private byte charsetByte;
	
	protected int minLength = 3;
	
	/**
	 * 准备文件
	 * @param tokens 命令数组
	 * @throws IOException
	 */
	public FileSender(String[] tokens) throws IOException {
		super(tokens);
		this.charsetByte = Commons
			.getCharsetByteByName(getCharset(tokens[2]));
	}
	
	/**
	 * 
	 * @author Switch
	 * @function 去掉命令的charset=前缀
	 * @param token 命令->字符集
	 * @return 去掉前缀的字符集
	 */
	private String getCharset(String token) {
		token = token.toLowerCase();
		if (token.startsWith(CHARSET_START)) {
			return token.substring(CHARSET_START.length());
		} else {
			throw new RuntimeException("字符集部分不符合规范.");
		}
	}

	/**
	 * 发送字符集byte
	 */
	protected void sendCharset(SocketWrapper socketWrapper) throws IOException {
		socketWrapper.write(charsetByte);// 字符集
	}
	
	@Override
	public byte getSendType() {
		return SEND_FILE;
	}
}
