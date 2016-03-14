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
* @Description: TODO ��ͨ�ļ�������
* @author Switch
* @date 2016��3��13�� ����5:27:04 
*  
*
 */
public class FileSender extends BFileSender {

	private byte charsetByte;
	
	protected int minLength = 3;
	
	/**
	 * ׼���ļ�
	 * @param tokens ��������
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
	 * @function ȥ�������charset=ǰ׺
	 * @param token ����->�ַ���
	 * @return ȥ��ǰ׺���ַ���
	 */
	private String getCharset(String token) {
		token = token.toLowerCase();
		if (token.startsWith(CHARSET_START)) {
			return token.substring(CHARSET_START.length());
		} else {
			throw new RuntimeException("�ַ������ֲ����Ϲ淶.");
		}
	}

	/**
	 * �����ַ���byte
	 */
	protected void sendCharset(SocketWrapper socketWrapper) throws IOException {
		socketWrapper.write(charsetByte);// �ַ���
	}
	
	@Override
	public byte getSendType() {
		return SEND_FILE;
	}
}
