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
* @Description: TODO ���������
* @author Switch
* @date 2016��3��13�� ����5:07:40 
*  
*
 */
public class LineProcessor {
	// �ָ�����������
	private String []tokens;
	// ������Ϣ�ӿ�
	private Sendable sendable;
	
	/**
	 * ���������У��ҵ���Ϣ����ʵ����
	 * @param line ������
	 * @throws Exception
	 */
	public LineProcessor(String line) throws Exception {
		// ������Ԥ����
		line = preLine(line).trim();
		// û���κβ���
		if(line.trim().length() == 0) {
			throw new NoOptionException();
		}
		// regex�������ƥ��һ�ε���οհ��ַ�[\t\n\x0B\f\r]
		tokens = line.trim().split("\\s+");
		// ��ȡ��һ������
		String firstToken = tokens[0];
		// �ҵ���Ϣ����ʵ�ֽӿ�ʵ����
		Class <?>clazz = findSendableClassByOrder(firstToken);
		try {
			// ͨ��������ƴ���һ����Ϣ���ͽӿ�ʵ����
			sendable = (Sendable)clazz.getConstructor(String[].class)
				.newInstance(new Object[] {tokens});
		}catch(InvocationTargetException e) {
			throw (Exception)e.getCause();
		}
	}
	
	/**
	 * 
	 * @author Switch
	 * @function �����������ͣ����ö�Ӧ���͵Ĵ������һ������
	 * @param socketWrapper Socket��װ�����
	 * @throws IOException
	 */
	public void sendContentBySocket(SocketWrapper socketWrapper) throws IOException {
		if(sendable != null && sendable.getSendType() > 0) {
			// ��������
			socketWrapper.write(sendable.getSendType());
			sendable.sendContent(socketWrapper);
		}
	}
	
	/**
	 * 
	 * @author Switch
	 * @function ��Ԥ����
	 * @param line ������������
	 * @return ����õ�������
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
