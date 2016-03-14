package com.pc.socket.client.sender;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.pc.socket.SocketWrapper;
import static com.pc.socket.Commons.*;
/**
 * 
*
* @ClassName: MessageSender 
* @Description: TODO 普通消息发送接口
* @author Switch
* @date 2016年3月13日 下午5:25:57 
*  
*
 */
public class MessageSender implements Sendable {
	// 普通的message消息
	private String message;
	// 消息发送时使用
	private byte []messageBytes;
	
	private int length = 0;
	/**
	 * 准备消息
	 * @param tokens 命令数组
	 * @throws UnsupportedEncodingException
	 */
	public MessageSender(String []tokens) throws UnsupportedEncodingException {
		if(tokens.length >= 2) {
			// 消息
			message = tokens[1];
			// 消息字节数组
			this.messageBytes = message.getBytes(DEFAULT_MESSAGE_CHARSET);
			// 消息长度
			this.length = messageBytes.length;
		}else {
			throw new RuntimeException("请在sendMsg后面添加内容。");
		}
	}

	/**
	 * 发送内容处理
	 * @throws IOException 
	 */
	@Override
	public void sendContent(SocketWrapper socketWrapper) throws IOException {
		println("我此时想服务器端发送消息：" + message);
		socketWrapper.write(length);
		socketWrapper.write(messageBytes);
		println("发送消息完毕。");
	}

	@Override
	public byte getSendType() {
		return SEND_MESSAGE;
	}

}
