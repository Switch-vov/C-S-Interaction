package com.pc.socket.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.pc.socket.SocketWrapper;
import com.pc.socket.client.exceptions.DirectNotExistsException;
import com.pc.socket.client.exceptions.ExitException;
import com.pc.socket.client.exceptions.NoOptionException;
import com.pc.socket.client.processor.LineProcessor;

import static com.pc.socket.Commons.print;
/**
 * 
*
* @ClassName: SocketClientMain 
* @Description: TODO �ͻ���������
* @author Switch
* @date 2016��3��13�� ����7:19:29 
*  
*
 */
public class SocketClientMain {

	public static void main(String []args) throws UnknownHostException, IOException {
		Scanner scanner = new Scanner(System.in);
		SocketWrapper socketWrapper = new SocketWrapper("localhost" , 8888);
		try {
			print("�Ѿ������Ϸ������ˣ����ڿ����������ݿ�ʼͨ����.....\n>");
			String line = scanner.nextLine();
			while(!"bye".equals(line)) {
				if(line != null) {
					try {
						// ���������������
						LineProcessor processor = new LineProcessor(line);
						processor.sendContentBySocket(socketWrapper);
						socketWrapper.displayStatus();
					}catch(ExitException e) {
						break;//�˳�ϵͳ
					}catch(NoOptionException e) {
						/*û�����κβ���*/
					}catch(DirectNotExistsException e) {
						System.out.println(e.getMessage());
					}catch(RuntimeException e) {
						System.out.println(e.getMessage());
					}catch(FileNotFoundException e) {
						System.out.println(e.getMessage());
					}catch(SocketException e) {
						socketWrapper.displayStatus();
						System.out.println("Socket�쳣�� " + e.getMessage()  + "��������������Ͽ�����....");
						break;
					}catch(Exception e) {
						e.printStackTrace();
						System.out.println("�����Ϸ������Ͽ����ӣ�");
						break;
					}
				}
				print(">");
				line = scanner.nextLine();
			}
		}finally {
			socketWrapper.close();
		}
	}
}
