package com.pc.socket.client.component;
/**
 * 
*
* @ClassName: CharsetByteRelative 
* @Description: TODO �ַ������ַ���Byte������
* @author Switch
* @date 2016��3��13�� ����3:03:55 
*  
*
 */
public class CharsetByteRelative {

	private String charset;
	
	private byte charsetByte;

	/**
	 * ͨ���ַ������ַ���byte����ʼ��
	 * @param charset �ַ���
	 * @param charsetByte �ַ���byte
	 */
	public CharsetByteRelative(String charset , byte charsetByte) {
		this.charset = charset;
		this.charsetByte = charsetByte;
	}
	
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public byte getCharsetByte() {
		return charsetByte;
	}

	public void setCharsetByte(byte charsetByte) {
		this.charsetByte = charsetByte;
	}
	
	/**
	 * @author Switch
	 * @function �ж��ַ����Ƿ����
	 * @param charset �ַ���
	 * @return ��� true ���� false
	 */
	public boolean isCharset(String charset) {
		if(charset == null) return false;
		charset = charset.toLowerCase().replace("-", "").trim();
		return charset.equals(this.charset);
	}
	
	// 
	/**
	 * @author Switch
	 * @function �ж��ַ���byte�Ƿ����
	 * @param charsetByte
	 * @return ��� true ���� false
	 */
	public boolean isCharsetCode(byte charsetByte) {
		return this.charsetByte == charsetByte;
	}
}