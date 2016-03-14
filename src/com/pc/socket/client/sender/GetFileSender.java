package com.pc.socket.client.sender;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.pc.socket.SocketWrapper;
import com.pc.socket.client.exceptions.DirectNotExistsException;

import static com.pc.socket.Commons.*;
/**
 * 
*
* @ClassName: GetFileSender 
* @Description: TODO �����ļ�������
* @author Switch
* @date 2016��3��13�� ����5:28:52 
*  
*
 */
public class GetFileSender implements Sendable {
	/**
	 * �ļ�·��
	 */
	private String saveFilePath;
	/**
	 * �ļ���
	 */
	private String getFileName;

	@Override
	public byte getSendType() {
		return GET_FILE;
	}
	
	/**
	 * ׼�������ļ�
	 * @param tokens �ַ�����
	 */
	public GetFileSender(String []tokens) {
		if (tokens.length >= 3) {
			// ��ȡ�ļ�·��
			saveFilePath = tokens[2];
			// �����ļ�����
			File file = new File(saveFilePath);
			if(file.exists() && file.isDirectory()) {
				// ��ȡ�ļ���
				this.getFileName = tokens[1];
				// �ļ�·��+�ָ���
				this.saveFilePath = file.getAbsolutePath() + File.separator;
			}else {
				throw new DirectNotExistsException(saveFilePath);
			}
		}else {
			throw new RuntimeException("��Ϣ��ʽ�������⣬��ʹ��help����鿴�����ʽ��");
		}
	}

	
	
	/**
	 * ��������
	 */
	@Override
	public void sendContent(SocketWrapper socketWrapper) throws IOException {
		println("׼�������ļ���" + getFileName);
		// �ļ���byte����
		byte[] fileNameBytes = getFileName.getBytes(DEFAULT_MESSAGE_CHARSET);
		// д���ļ�������
		socketWrapper.write((short) fileNameBytes.length);
		// д���ļ���
		socketWrapper.write(fileNameBytes);
		// ��ȡ������״̬
		int status = socketWrapper.readInt();
		// �������˷����Ƿ���Լ����ϴ������������������ϴ���
		if(status != 1) {
			processErrorStatus(status);
		}else {
			// �����ļ�����
			long fileLength = socketWrapper.readLong();
			int readLength = 0 , i = 0;
			// ���������
			FileOutputStream out = new FileOutputStream(saveFilePath + getFileName);
			try {
				// ����byte����
				byte []bytes = new byte[DEFAULT_BUFFER_LENGTH];
				println("��ʼ�����ļ����ļ�����Ϊ��" + fileLength);
				// ѭ������
				while(readLength < fileLength) {
					// ����
					int len = socketWrapper.read(bytes);
					readLength += len;
					out.write(bytes, 0, len);
					if(++i % 1024 == 0) {
						print(".");
					}
				}
				println("��ʼ�������.......");
			}finally {
				closeStream(out);
				println("");
			}
		}
	}
	
	/**
	 * 
	 * @author Switch
	 * @function ����״̬����
	 * @param status ����״̬
	 */
	private void processErrorStatus(int status) {
		if(status == -1) {
			println("ERROR���ļ�����ʧ�ܣ�ʧ��ԭ��Ϊ��������û���ҵ�ָ�����ļ�....");
		}else {
			println("ERROR���ļ�����ʧ�ܣ�ʧ��ԭ��ȷ��������ʧ�ܴ����Ϊ��" + status);
		}
	}

}
