package com.pc.socket.client.exceptions;
/**
 * 
*
* @ClassName: DownloadNotExistsFileException 
* @Description: TODO �����ļ��쳣
* @author Switch
* @date 2016��3��13�� ����2:54:14 
*  
*
 */
public class DownloadNotExistsFileException extends RuntimeException {

	private static final long serialVersionUID = 2969567696674112542L;
	
	public DownloadNotExistsFileException(String path) {
		super("�޷������ļ���" + path + "����Ϊ�������˲���������ļ�....");
	}

}
