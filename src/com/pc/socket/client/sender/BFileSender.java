package com.pc.socket.client.sender;

import static com.pc.socket.Commons.DEFAULT_MESSAGE_CHARSET;
import static com.pc.socket.Commons.SEND_B_FILE;
import static com.pc.socket.Commons.println;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.pc.socket.SocketWrapper;

/**
 * 
*
* @ClassName: BFileSender 
* @Description: TODO �������ļ�������
* @author Switch
* @date 2016��3��13�� ����5:28:14 
*  
*
 */
public class BFileSender implements Sendable {
	
	protected File file;
	
	protected long fileLength;
	
	protected int minLength = 2;
	
	/**
	 * ׼���������ļ�
	 * @param tokens ��������
	 * @throws IOException
	 */
	public BFileSender(String []tokens) throws IOException {
		if (tokens.length >= minLength) {
			// ͨ��·����ȡ�ļ�����
			file = new File(tokens[1]);
			if (!file.exists()) {
				throw new FileNotFoundException("�ļ���" + tokens[1]
						+ " δ�ҵ����뷢�ͱ��ش��ڵ��ļ���");
			}
			// ��ȡ�ļ�����
			this.fileLength = file.length();
		} else {
			throw new RuntimeException("��Ϣ��ʽ�������⣬��ʹ��help����鿴�����ʽ��");
		}
	}

	
	@Override
	public void sendContent(SocketWrapper socketWrapper) throws IOException {
		println("�Ҵ�ʱ��������˷��Ͷ������ļ����ļ���СΪ��" + this.fileLength);
		// ���ͱ��룬������ʹ��
		sendCharset(socketWrapper);
		//�ļ���
		byte[] fileNameBytes = file.getName().getBytes(DEFAULT_MESSAGE_CHARSET);
		// д���ļ�����
		socketWrapper.write((short) fileNameBytes.length);
		// д���ļ���
		socketWrapper.write(fileNameBytes);
		// �ӷ���������״̬
		int status = socketWrapper.readInt();
		// �������˷����Ƿ���Լ����ϴ������������������ϴ���
		if(status != 1) {
			processErrorStatus(status);
		}
		// �ļ�����&����
		socketWrapper.write(this.fileLength);
		socketWrapper.writeFile(file.getPath());
		// �ӷ���������״̬
		status = socketWrapper.readInt();
		if(status != 1) {
			processErrorStatus(status);
		}else {
			println("�ļ����ͳɹ����Ѿ��ɹ����浽��������.......");
		}
	}
	
	private void processErrorStatus(int status) throws IOException {
		if (status == -1) {
			throw new RuntimeException("�������˱���ʧ�ܣ����ڷ��������Ѿ����ڸ��ļ�����..");
		} else if (status != 1) {
			throw new IOException("�������˱���ʧ�ܣ���ȷ������ԭ�򣬳��򽫽�������....");
		}
	}
	
	/**
	 * 
	 * @author Switch
	 * @function �����ַ���byte
	 * @param socketWrapper
	 * @throws IOException
	 */
	protected void sendCharset(SocketWrapper socketWrapper) throws IOException {
		/*����Լ̳�Ŷ*/
	}

	@Override
	public byte getSendType() {
		return SEND_B_FILE;
	}
}
