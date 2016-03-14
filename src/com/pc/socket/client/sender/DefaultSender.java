package com.pc.socket.client.sender;

import static com.pc.socket.Commons.ERROR_MESSAGE_FORMAT;
import static com.pc.socket.Commons.EXIT_STR;
import static com.pc.socket.Commons.HELP_SHOW;
import static com.pc.socket.Commons.HELP_STR;
import static com.pc.socket.Commons.println;

import java.io.IOException;

import com.pc.socket.SocketWrapper;
import com.pc.socket.client.exceptions.ExitException;
/**
 * 
*
* @ClassName: DefaultSender 
* @Description: TODO Ĭ�Ϸ��ͽӿ�ʵ����
* @author Switch
* @date 2016��3��13�� ����5:31:34 
*  
*
 */
public class DefaultSender implements Sendable {
	
	public DefaultSender(String []tokens) {
		String firstToken = tokens[0];
		if(HELP_STR.equalsIgnoreCase(firstToken)) {//����
			println(HELP_SHOW);
		}else if(EXIT_STR.equalsIgnoreCase(firstToken)) {//�˳�
			//System.exit(0);�÷���ֱ�ӹرս��̣�Ҳ����ʹ�ã��Զ����ExitException�ⲿ����socket���մ���
			throw new ExitException();
		}else {
			throw new RuntimeException(ERROR_MESSAGE_FORMAT);
		}
	}

	@Override
	public byte getSendType() {
		return 0;
	}

	@Override
	public void sendContent(SocketWrapper socketWrapper) throws IOException {
		/*�������κ���Ϣ*/
	}

}
