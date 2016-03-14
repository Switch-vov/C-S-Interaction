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
* @Description: TODO Socket包装类
* @author Switch
* @date 2016年3月13日 下午1:47:04 
*  
*
 */
public class SocketWrapper implements Closeable {
	// 页大小，1M大小，文件大小超过它就要分段处理
	private final static int PAGE_SIZE = 1024 * 1024;
	
	// 套接字
	private Socket socket;
	// 输入流
	private DataInputStream inputStream;
	// 输出流
	private DataOutputStream outputStream;
	
	// 传入套接字的初始化
	public SocketWrapper(Socket socket) throws IOException {
		this.socket = socket;
		processStreams();
	}
	
	// 传入主机和端口的初始化
	public SocketWrapper(String host, int port) throws IOException {
		this.socket = new Socket();
		this.socket.connect(new InetSocketAddress(host, port), 1500);
		this.socket.setKeepAlive(true);
		this.socket.setTcpNoDelay(true);
		processStreams();
	}

	// 初始化输入输出流
	private void processStreams() throws IOException {
		inputStream = new DataInputStream(socket.getInputStream());
		outputStream = new DataOutputStream(socket.getOutputStream());
	}
	
	// 写出byte
	public void write(byte b) throws IOException {
		this.outputStream.write(b);
	}

	// 写出short
	public void write(short s) throws IOException {
		this.outputStream.writeShort(s);
	}

	// 写出int
	public void write(int i) throws IOException {
		this.outputStream.writeInt(i);
	}

	// 写出long
	public void write(long l) throws IOException {
		this.outputStream.writeLong(l);
	}

	// 写出byte数组
	public void write(byte[] bytes) throws IOException {
		this.outputStream.write(bytes);
	}

	// 按长度分段写出byte数组
	public void write(byte[] bytes, int length) throws IOException {
		this.outputStream.write(bytes, 0, length);
	}

	// 按字符集将String转成byte数组，然后写出
	public void write(String value, String charset) throws IOException {
		if(value != null) {
			write(value.getBytes(charset));
		}
	}

	// 读入byte
	public byte readByte() throws IOException {
		return this.inputStream.readByte();
	}
	
	// 读入short
	public short readShort() throws IOException {
		return this.inputStream.readShort();
	}
	
	// 读入int
	public int readInt() throws IOException {
		return this.inputStream.readInt();
	}
	
	// 读入long
	public long readLong() throws IOException {
		return this.inputStream.readLong();
	}
	
	// 全部读入byte数组到参数bytes中
	public void readFull(byte[] bytes) throws IOException {
		this.inputStream.readFully(bytes);
	}

	// 分段读入byte数组到参数bytes中，并返回读入长度
	public int read(byte []bytes) throws IOException {
		return this.inputStream.read(bytes);
	}
	
	// 按字符集读入String
	public String read(int length, String charset) throws IOException {
		byte[] bytes = new byte[length];
		read(bytes);
		return new String(bytes, charset);
	}
	
	// 写出文件
	public void writeFile(String path) throws IOException {
		File file = new File(path);
		FileInputStream fileInputStream = new FileInputStream(file);
		try {
			long fileLenth = file.length();
			// 文件长度超过一定大小，也就是PAGE_SIZE的大小就分段发送
			if(fileLenth > PAGE_SIZE) {
				// 创建一个PAGE_SIZE大小的byte数组
				byte []bytes = new byte[PAGE_SIZE];
				// 当前发送起始位置
				int allLength = 0;
				// 读入字节流，并返回读入长度
				int length = fileInputStream.read(bytes);
				// 当长度不为-1，也就是不为文件尾的时候
				while(length > 0) {
					// 当前发送起始位置+读入长度
					allLength += length;
					// 写出length长度字节流
					this.write(bytes , length);
					// 读入字节流，并返回读入长度
					length = fileInputStream.read(bytes);
					// 用.的个数表示大小
					print(".");
				}
				println("实际发送文件长度为：" + allLength);
			}else {
				byte []bytes = new byte[(int)fileLenth];
				fileInputStream.read(bytes);
				this.write(bytes);
			}
		}finally {
			closeStream(fileInputStream);
		}
	}
	
	// 关闭资源
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
