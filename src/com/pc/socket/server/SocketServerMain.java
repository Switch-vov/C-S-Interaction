package com.pc.socket.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import static com.pc.socket.Commons.*;
import com.pc.socket.SocketWrapper;
/**
 * 
*
* @ClassName: SocketServerMain 
* @Description: TODO 服务器端启动类
* @author Switch
* @date 2016年3月13日 下午3:41:47 
*  
*
 */
public class SocketServerMain {
	
	private final static List<Worker> workers = new ArrayList<Worker>();

	public static void main(String []args) throws IOException {
		initPath();
		// 打开服务端端口，端口为8888
		ServerSocket serverSocket = new ServerSocket(8888);
		logInfo("端口已经打开为8888，开始准备接受数据.....");
		try {
			int index = 1;
			while(true) {
				SocketWrapper socketWrapper = new SocketWrapper(serverSocket.accept());
				workers.add(new Worker(socketWrapper , "socket_thread_" + index++));
			}
		}finally {
			serverSocket.close();
			interruptWorkers();
		}
	}
	
	/**
	 * 
	 * @author Switch
	 * @function 中断所有线程
	 */
	private static void interruptWorkers() {
		for(Worker worker : workers) {
			worker.interrupt();
		}
	}
	
	/**
	 * 
	 * @author Switch
	 * @function 初始化路径
	 */
	private static void initPath() {
		File file = new File(SERVER_SAVE_BASE_PATH);
		// 路径不存在
		if(!file.exists()) {
			// 创建路径
			boolean success = file.mkdirs();
			if(!success) 
				throw new RuntimeException("无法创建目录：" + SERVER_SAVE_BASE_PATH);
		}
	}
}
