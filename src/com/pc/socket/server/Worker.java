package com.pc.socket.server;

import static com.pc.socket.Commons.DEFAULT_BUFFER_LENGTH;
import static com.pc.socket.Commons.DEFAULT_MESSAGE_CHARSET;
import static com.pc.socket.Commons.GET_FILE;
import static com.pc.socket.Commons.SEND_B_FILE;
import static com.pc.socket.Commons.SEND_FILE;
import static com.pc.socket.Commons.SEND_MESSAGE;
import static com.pc.socket.Commons.SERVER_SAVE_BASE_PATH;
import static com.pc.socket.Commons.closeStream;
import static com.pc.socket.Commons.logInfo;
import static com.pc.socket.Commons.print;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;

import com.pc.socket.Commons;
import com.pc.socket.SocketWrapper;
import com.pc.socket.client.exceptions.DownloadNotExistsFileException;
import com.pc.socket.client.exceptions.SaveExistsFileException;
/**
 * 
*
* @ClassName: Worker 
* @Description: TODO ���������̰߳�װ��
* @author Switch
* @date 2016��3��13�� ����2:06:36 
*  
*
 */
public class Worker extends Thread {

	// socket��װ��
	private SocketWrapper socketWrapper;
	
	// �߳���
	private String name;
	
	// ��ʼ��
	public Worker(SocketWrapper socketWrapper , String name) {
		this.socketWrapper = socketWrapper;
		this.name = name;
		logInfo("�����̣߳�" + name + " , ��ʼ�������տͻ��˴�������......");
		this.start();//����
	}
	
	// ��û�����������쳣ʱ���߳�һֱ����
	public void run() {
		while(true) {
			try {
				// �߳��ж���break
				if(this.isInterrupted()) break;
				// ����һ���ֽڣ��ж���������
				byte type = socketWrapper.readByte();
				switch(type) {
					// �ͻ��˷�����Ϣ��
					case SEND_MESSAGE : processMessage(); break;
					// �ͻ��˷����ı��ļ�
					case SEND_FILE : processFile(); break;
					// �ͻ��˷��Ͷ������ļ�
					case SEND_B_FILE : uploadFileContent(null); break;
					// �ͻ��������ļ�
					case GET_FILE : downloadFile(); break;
					default : 
				}
			// �������ļ��ظ��쳣
			}catch(SaveExistsFileException e) {
				logInfo(e.getMessage());
			// �����ļ��������쳣
			}catch(DownloadNotExistsFileException e) {
				logInfo(e.getMessage());
				
			// ֮�µĶ��������쳣���ᵼ���߳��ж�
			}catch(EOFException e) {
				logInfo("�ͻ��˹ر�socket���߳� :" + name + " ����ִ��.");
				break;//�Է�socket�Ѿ��Ͽ�
			}catch(SocketException e) {
				logInfo("Socket�쳣��" + e.getMessage() + "���߳� :" + name + " ����ִ��.");
				break;//socket�쳣
			}catch(Exception e) {
				e.printStackTrace();
				logInfo("�߳� :" + name + " ����ִ��.");
				break;
			}
		}
		// �ر���Դ
		this.socketWrapper.close();
	}
	
	/**
	 * �ж�
	 */
	public void interrupt() {
		if(this.isAlive()) 
			super.interrupt();
	}
	
	/**
	 * ����ͻ��˴�������Ϣ��
	 * @throws IOException 
	 */
	private void processMessage() throws IOException {
		int length = socketWrapper.readInt();
		byte[]message = new byte[length];
		socketWrapper.read(message);
		logInfo("�̣߳�" + name  + " ���ܵ����Կͻ��˴���message��Ϣ��" + new String(message , DEFAULT_MESSAGE_CHARSET));
	}
	
	/**
	 * ����ͻ��˴�������ͨ�ļ�
	 * @throws IOException
	 */
	private void processFile() throws IOException {
		// ��ȡ�ַ��������̱Ƚϸ���
		String charset = Commons.getCharsetNameByCode(socketWrapper.readByte());
		logInfo("�̣߳�" + name + "������Դ�ͻ��˷����ļ����ַ���Ϊ��" + charset);
		
		uploadFileContent(charset);
	}

	/**
	 * �ϴ��ļ�������ϸ
	 * @param charset
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	private void uploadFileContent(String charset) throws IOException,
			UnsupportedEncodingException, FileNotFoundException {
		FileOutputStream out = null;
		try {
			//��ȡ�ļ��������Լ��ļ���
			short length = socketWrapper.readShort();
			byte[]bytes = new byte[length];
			socketWrapper.readFull(bytes);
			String fileName = new String(bytes , DEFAULT_MESSAGE_CHARSET);
			logInfo("�̣߳�" + name + "������Դ�ͻ��˷����ļ�����Դ�ļ���Ϊ��" + fileName);
			
			// �ļ��洢·��
			String path = SERVER_SAVE_BASE_PATH + fileName;
			File file = new File(path);
			// �ļ�����
			if(file.exists()) {
				throw new SaveExistsFileException(path);
			}
			// ��ʾ�ļ����Դ���
			socketWrapper.write(1);
			out = new FileOutputStream(file);
			
			//��ȡ�ļ����ݣ����ǰ�沿������
			long fileLength = socketWrapper.readLong();
			logInfo("�̣߳�" + name + "������Դ�ͻ��˷����ļ����ļ�����Ϊ��" + fileLength);
			// ��������byte����
			bytes = new byte[DEFAULT_BUFFER_LENGTH];
			int allLength = 0 , i = 0;
			// ѭ��д���ļ�
			while(allLength < fileLength) {
				int len = socketWrapper.read(bytes);
				allLength += len;
				out.write(bytes, 0, len);
				
				// ��ӡ�ļ���ϸ
				if(charset != null) {
					print(new String(bytes , charset));
				}
				if(i++ % 1024 == 0) {
					print(".");
				}
			}
			logInfo("\n�ļ�������ϲ����棬��ͻ��˷���ȷ����Ϣ , ʵ�ʽ������ݳ���Ϊ��" + fileLength);
			socketWrapper.write(1);
		// �ļ��Ѵ���
		}catch(SaveExistsFileException e) {
			socketWrapper.write(-1);
			throw e;
		}catch(RuntimeException e) {
			logInfo("\n�ļ�����ʧ�ܣ���ͻ��˷��ʹ�����Ϣ��");
			socketWrapper.write(-2);
			throw e;
		}finally {
			closeStream(out);
		}
	}
	
	/**
	 * �����ļ�����
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	private void downloadFile() throws IOException,
		UnsupportedEncodingException, FileNotFoundException {
		short fileNameLength = socketWrapper.readShort();
		byte []fileNameBytes = new byte[fileNameLength];
		socketWrapper.read(fileNameBytes);
		String fileName = new String(fileNameBytes , DEFAULT_MESSAGE_CHARSET);
		logInfo("�̣߳�" + name + "���ܿͻ��������ļ��������ļ���Ϊ��" + fileName);
		// �������ļ�ȫ·��
		String absolatePath = SERVER_SAVE_BASE_PATH + fileName;
		File file = new File(absolatePath);
		// �ļ�����
		if(file.exists()) {
			socketWrapper.write(1);
		// �ļ������ڣ��׳��쳣
		}else {
			socketWrapper.write(-1);
			throw new DownloadNotExistsFileException(absolatePath);
		}
		// �ļ�����
		socketWrapper.write(file.length());
		// д���ļ�
		socketWrapper.writeFile(absolatePath);
	}
}
