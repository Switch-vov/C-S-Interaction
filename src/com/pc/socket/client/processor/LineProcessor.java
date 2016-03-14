package com.pc.socket.client.processor;

import static com.pc.socket.Commons.findSendableClassByOrder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.pc.socket.SocketWrapper;
import com.pc.socket.client.exceptions.NoOptionException;
import com.pc.socket.client.sender.Sendable;
/**
 * 
*
* @ClassName: LineProcessor 
* @Description: TODO 命令解析类
* @author Switch
* @date 2016年3月13日 下午5:07:40 
*  
*
 */
public class LineProcessor {
	// 分割后的命令数组
	private String []tokens;
	// 发送消息接口
	private Sendable sendable;
	
	/**
	 * 处理命令行，找到消息发送实现类
	 * @param line 命令行
	 * @throws Exception
	 */
	public LineProcessor(String line) throws Exception {
		// 命令行预处理
		line = preLine(line).trim();
		// 没有任何操作
		if(line.trim().length() == 0) {
			throw new NoOptionException();
		}
		// regex代表的是匹配一次到多次空白字符[\t\n\x0B\f\r]
		tokens = line.trim().split("\\s+");
		// 获取第一个命令
		String firstToken = tokens[0];
		// 找到消息发送实现接口实现类
		Class <?>clazz = findSendableClassByOrder(firstToken);
		try {
			// 通过反射机制创建一个消息发送接口实现类
			sendable = (Sendable)clazz.getConstructor(String[].class)
				.newInstance(new Object[] {tokens});
		}catch(InvocationTargetException e) {
			throw (Exception)e.getCause();
		}
	}
	
	/**
	 * 
	 * @author Switch
	 * @function 传递命令类型，调用对应类型的处理类进一步处理
	 * @param socketWrapper Socket包装类对象
	 * @throws IOException
	 */
	public void sendContentBySocket(SocketWrapper socketWrapper) throws IOException {
		if(sendable != null && sendable.getSendType() > 0) {
			// 发送类型
			socketWrapper.write(sendable.getSendType());
			sendable.sendContent(socketWrapper);
		}
	}
	
	/**
	 * 
	 * @author Switch
	 * @function 行预处理
	 * @param line 待处理命令行
	 * @return 处理好的命令行
	 */
	private String preLine(String line) {
		if(line == null) {
			return "";
		}
		if(line.startsWith(">")) {
			return line.substring(1);
		}
		return line;
	}
}
