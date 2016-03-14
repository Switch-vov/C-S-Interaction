package com.pc.socket.client.sender;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.pc.socket.SocketWrapper;
import static com.pc.socket.Commons.*;
/**
 * 
*
* @ClassName: MessageSender 
* @Description: TODO ��ͨ��Ϣ���ͽӿ�
* @author Switch
* @date 2016��3��13�� ����5:25:57 
*  
*
 */
public class MessageSender implements Sendable {
	// ��ͨ��message��Ϣ
	private String message;
	// ��Ϣ����ʱʹ��
	private byte []messageBytes;
	
	private int length = 0;
	/**
	 * ׼����Ϣ
	 * @param tokens ��������
	 * @throws UnsupportedEncodingException
	 */
	public MessageSender(String []tokens) throws UnsupportedEncodingException {
		if(tokens.length >= 2) {
			// ��Ϣ
			message = tokens[1];
			// ��Ϣ�ֽ�����
			this.messageBytes = message.getBytes(DEFAULT_MESSAGE_CHARSET);
			// ��Ϣ����
			this.length = messageBytes.length;
		}else {
			throw new RuntimeException("����sendMsg����������ݡ�");
		}
	}

	/**
	 * �������ݴ���
	 * @throws IOException 
	 */
	@Override
	public void sendContent(SocketWrapper socketWrapper) throws IOException {
		println("�Ҵ�ʱ��������˷�����Ϣ��" + message);
		socketWrapper.write(length);
		socketWrapper.write(messageBytes);
		println("������Ϣ��ϡ�");
	}

	@Override
	public byte getSendType() {
		return SEND_MESSAGE;
	}

}
