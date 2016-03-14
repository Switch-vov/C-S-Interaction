package com.pc.socket.client.component;
/**
 * 
*
* @ClassName: CharsetByteRelative 
* @Description: TODO 字符集和字符集Byte关联类
* @author Switch
* @date 2016年3月13日 下午3:03:55 
*  
*
 */
public class CharsetByteRelative {

	private String charset;
	
	private byte charsetByte;

	/**
	 * 通过字符集和字符集byte数初始化
	 * @param charset 字符集
	 * @param charsetByte 字符集byte
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
	 * @function 判断字符集是否相等
	 * @param charset 字符集
	 * @return 相等 true 不等 false
	 */
	public boolean isCharset(String charset) {
		if(charset == null) return false;
		charset = charset.toLowerCase().replace("-", "").trim();
		return charset.equals(this.charset);
	}
	
	// 
	/**
	 * @author Switch
	 * @function 判断字符集byte是否相等
	 * @param charsetByte
	 * @return 相等 true 不等 false
	 */
	public boolean isCharsetCode(byte charsetByte) {
		return this.charsetByte == charsetByte;
	}
}