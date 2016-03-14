package com.pc.socket;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import static com.pc.socket.Commons.*;

/**
 * 
*
* @ClassName: SocketWrapper 
* @Description: TODO Socket��װ��
* @author Switch
* @date 2016��3��13�� ����1:47:04 
*  
*
 */
public class SocketWrapper implements Closeable {
	// ҳ��С��1M��С���ļ���С��������Ҫ�ֶδ���
	private final static int PAGE_SIZE = 1024 * 1024;
	
	// �׽���
	private Socket socket;
	// ������
	private DataInputStream inputStream;
	// �����
	private DataOutputStream outputStream;
	
	// �����׽��ֵĳ�ʼ��
	public SocketWrapper(Socket socket) throws IOException {
		this.socket = socket;
		processStreams();
	}
	
	// ���������Ͷ˿ڵĳ�ʼ��
	public SocketWrapper(String host, int port) throws IOException {
		this.socket = new Socket();
		this.socket.connect(new InetSocketAddress(host, port), 1500);
		this.socket.setKeepAlive(true);
		this.socket.setTcpNoDelay(true);
		processStreams();
	}

	// ��ʼ�����������
	private void processStreams() throws IOException {
		inputStream = new DataInputStream(socket.getInputStream());
		outputStream = new DataOutputStream(socket.getOutputStream());
	}
	
	// д��byte
	public void write(byte b) throws IOException {
		this.outputStream.write(b);
	}

	// д��short
	public void write(short s) throws IOException {
		this.outputStream.writeShort(s);
	}

	// д��int
	public void write(int i) throws IOException {
		this.outputStream.writeInt(i);
	}

	// д��long
	public void write(long l) throws IOException {
		this.outputStream.writeLong(l);
	}

	// д��byte����
	public void write(byte[] bytes) throws IOException {
		this.outputStream.write(bytes);
	}

	// �����ȷֶ�д��byte����
	public void write(byte[] bytes, int length) throws IOException {
		this.outputStream.write(bytes, 0, length);
	}

	// ���ַ�����Stringת��byte���飬Ȼ��д��
	public void write(String value, String charset) throws IOException {
		if(value != null) {
			write(value.getBytes(charset));
		}
	}

	// ����byte
	public byte readByte() throws IOException {
		return this.inputStream.readByte();
	}
	
	// ����short
	public short readShort() throws IOException {
		return this.inputStream.readShort();
	}
	
	// ����int
	public int readInt() throws IOException {
		return this.inputStream.readInt();
	}
	
	// ����long
	public long readLong() throws IOException {
		return this.inputStream.readLong();
	}
	
	// ȫ������byte���鵽����bytes��
	public void readFull(byte[] bytes) throws IOException {
		this.inputStream.readFully(bytes);
	}

	// �ֶζ���byte���鵽����bytes�У������ض��볤��
	public int read(byte []bytes) throws IOException {
		return this.inputStream.read(bytes);
	}
	
	// ���ַ�������String
	public String read(int length, String charset) throws IOException {
		byte[] bytes = new byte[length];
		read(bytes);
		return new String(bytes, charset);
	}
	
	// д���ļ�
	public void writeFile(String path) throws IOException {
		File file = new File(path);
		FileInputStream fileInputStream = new FileInputStream(file);
		try {
			long fileLenth = file.length();
			// �ļ����ȳ���һ����С��Ҳ����PAGE_SIZE�Ĵ�С�ͷֶη���
			if(fileLenth > PAGE_SIZE) {
				// ����һ��PAGE_SIZE��С��byte����
				byte []bytes = new byte[PAGE_SIZE];
				// ��ǰ������ʼλ��
				int allLength = 0;
				// �����ֽ����������ض��볤��
				int length = fileInputStream.read(bytes);
				// �����Ȳ�Ϊ-1��Ҳ���ǲ�Ϊ�ļ�β��ʱ��
				while(length > 0) {
					// ��ǰ������ʼλ��+���볤��
					allLength += length;
					// д��length�����ֽ���
					this.write(bytes , length);
					// �����ֽ����������ض��볤��
					length = fileInputStream.read(bytes);
					// ��.�ĸ�����ʾ��С
					print(".");
				}
				println("ʵ�ʷ����ļ�����Ϊ��" + allLength);
			}else {
				byte []bytes = new byte[(int)fileLenth];
				fileInputStream.read(bytes);
				this.write(bytes);
			}
		}finally {
			closeStream(fileInputStream);
		}
	}
	
	// �ر���Դ
	public void close() {
		try {
			this.socket.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void displayStatus() throws SocketException {
	}
}
