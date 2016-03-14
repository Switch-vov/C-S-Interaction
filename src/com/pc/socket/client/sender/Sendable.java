package com.pc.socket.client.sender;

import java.io.IOException;

import com.pc.socket.SocketWrapper;
/**
 * 
*
* @ClassName: Sendable 
* @Description: TODO 发送消息接口
* @author Switch
* @date 2016年3月13日 下午5:02:31 
*  
*
 */
public interface Sendable {
	/**
	 * 
	 * @author Switch
	 * @function 获得发送类型
	 * @return 发送类型字节
	 */
	public byte getSendType();

	/**
	 * 
	 * @author Switch
	 * @function 发送Socket包装类对象
	 * @param socketWrapper
	 * @throws IOException
	 */
	public void sendContent(SocketWrapper socketWrapper) throws IOException;
}
