package com.pc.socket.client.exceptions;
/**
 * 
*
* @ClassName: SaveExistsFileException 
* @Description: TODO �����ļ��쳣
* @author Switch
* @date 2016��3��13�� ����2:53:22 
*  
*
 */
public class SaveExistsFileException extends RuntimeException {

	private static final long serialVersionUID = -1026575092082314002L;
	
	public SaveExistsFileException(String path) {
		super("�����ļ�:" + path + "ʧ�ܣ���Ϊ�ļ��Ѿ������ˡ�");
	}
	
}
