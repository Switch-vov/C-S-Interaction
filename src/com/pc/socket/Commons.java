package com.pc.socket;

import java.io.Closeable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.reflect.Reflection;
import com.pc.socket.client.component.CharsetByteRelative;
import com.pc.socket.client.sender.BFileSender;
import com.pc.socket.client.sender.DefaultSender;
import com.pc.socket.client.sender.FileSender;
import com.pc.socket.client.sender.GetFileSender;
import com.pc.socket.client.sender.MessageSender;
import com.pc.socket.client.sender.Sendable;
/**
 * 
*
* @ClassName: Commons 
* @Description: TODO ͨ�Ż�����
* @author Switch
* @date 2016��3��13�� ����2:31:42 
*  
*
 */
public class Commons {
	
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private final static SimpleDateFormat DATE_FORMAT_OBJECT = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
	/**
	 * ��������
	 */
	public final static String HELP_STR = "help";
	/**
	 * �˳�����
	 */
	public final static String EXIT_STR = "exit";

	// �������� start
	public final static int SEND_MESSAGE = 1;
	
	public final static String SEND_MESSAGE_STR = "sendMsg";
	
	public final static byte SEND_FILE = 2;
	
	public final static String SEND_FILE_STR = "sendFile";
	
	public final static byte SEND_B_FILE = 3;
	
	public final static String SEND_B_FILE_STR = "sendBFile";
	
	public final static byte GET_FILE = 4;
	
	public final static String GET_FILE_STR = "getFile";
	// �������� end
	
	/**
	 * �ַ�������
	 */
	public final static String DEFAULT_MESSAGE_CHARSET = "utf-8";
	
	public final static String CHARSET_START = "charset=";
	
	/**
	 * ����������λ��
	 */
	public final static String SERVER_SAVE_BASE_PATH = "G:/javaA/upload/";
	
	/**
	 * Ĭ��buffer����Ϊ8K
	 */
	public final static int DEFAULT_BUFFER_LENGTH = 8 * 1024;
	/**
	 * ����˵��
	 */
	public final static String HELP_SHOW = 
	"\n\n\t\t������ʽ��\n" +
	"\t\t1��������ͨ���ַ��������磺\"sendMsg ��ð���\" ���ɡ�\n" +
	"\t\t2�������ı��ļ������磺\"sendFile /home/xieyuooo/a.sql charset=utf-8\"\n" +
	"\t\t3�����ͷ��ı��ļ������磺\"sendBFile /home/xieyuooo/aaa.jpg\"\n" +
	"\t\t4�������ļ������磺\"getFile aaa.jpg d:/download/\" ����d:/download/Ϊ���ص����ص�Ŀ¼����δָ���������ص�user.dir����\n" +
	"\t\t5��help �����ص�ʹ�ð���\n" +
	"\t\t6��exit �˳��ͻ���";
	
	public final static String ERROR_MESSAGE_FORMAT = "�������Ϣ��ʽ����ο� demo��\n" + HELP_SHOW;
	
	/**
	 * �ַ������ַ���Byte�����༯��
	 */
	private final static List<CharsetByteRelative> CHASET_BYTE_LIST = Arrays.asList(
			new CharsetByteRelative("utf8" , (byte)1) , 
			new CharsetByteRelative("gbk" , (byte)2) ,
			new CharsetByteRelative("utf16" , (byte)3)
	);
	
	/**
	 * ���ͽӿ�ʵ������������Map����
	 */
	private final static Map<String , Class<? extends Sendable>> ORDER_CLASS_MAP 
						    = new HashMap<String , Class<? extends Sendable>>() {
		private static final long serialVersionUID = 3431099761909680054L;
		{
			put(SEND_MESSAGE_STR.toLowerCase() , MessageSender.class);
			put(SEND_FILE_STR.toLowerCase() , FileSender.class);
			put(SEND_B_FILE_STR.toLowerCase() , BFileSender.class);
			put(GET_FILE_STR.toLowerCase() , GetFileSender.class);
		}
	};
	
	/**
	 * 
	 * @author Switch
	 * @function ����̨print���
	 * @param str
	 */
	public static void print(String str) {
		System.out.print(str);
	}
	
	/**
	 * 
	 * @author Switch
	 * @function ����̨println���
	 * @param str
	 */
	public static void println(String str) {
		System.out.println(str);
	}
	
	/**
	 * 
	 * @author Switch
	 * @function ��־��¼
	 * @param message ��Ϣ
	 */
	public static void logInfo(String message) {
		Class <?>clazz = Reflection.getCallerClass();
		String date = DATE_FORMAT_OBJECT.format(Calendar.getInstance().getTime());
		println(date + " [] INFO " + clazz.getName() + " - " + message);
	}
	
	public static void closeStream(Closeable closeable) {
		try {
			if(closeable != null) {
				closeable.close();
			}
		}catch(IOException e) {
			/*�����Լ����������Ĵ���*/
		}
	}
	
	public static void closeStreams(Closeable ...closeables) {
		if(closeables != null) {
			for(Closeable closeable : closeables) {
				closeStream(closeable);
			}
		}
	}
	
	// ͨ�����ֻ�ȡ�ַ���byte
	public static byte getCharsetByteByName(String charset) {
		for(CharsetByteRelative charsetByteRelative : CHASET_BYTE_LIST) {
			if(charsetByteRelative.isCharset(charset)) {
				return charsetByteRelative.getCharsetByte();
			}
		}
		throw new RuntimeException("��֧���ַ�����" + charset);
	}
	
	// ͨ�������ȡ�ַ���
	public static String getCharsetNameByCode(byte charsetCode) {
		for(CharsetByteRelative charsetByteRelative : CHASET_BYTE_LIST) {
			if(charsetByteRelative.isCharsetCode(charsetCode)) {
				return charsetByteRelative.getCharset();
			}
		}
		throw new RuntimeException("��֧���ַ�����ţ�" + charsetCode);
	}
	
	/**
	 * 
	 * @author Switch
	 * @function ͨ�������ҵ���Ӧ�ķ��ͽӿ�ʵ����
	 * @param order
	 * @return Sendable��������
	 */
	public static Class<? extends Sendable> findSendableClassByOrder(String order) {
		// ��÷��ͽӿ�ʵ����
		Class<? extends Sendable>clazz = ORDER_CLASS_MAP.get(order.toLowerCase());
		// ����Ҳ�����Ӧʵ���࣬�򷵻�Ĭ�Ͻӿ�ʵ����
		return clazz == null ? DefaultSender.class : clazz;
	}
}