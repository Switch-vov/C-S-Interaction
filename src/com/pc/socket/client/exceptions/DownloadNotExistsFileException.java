package com.pc.socket.client.exceptions;
/**
 * 
*
* @ClassName: DownloadNotExistsFileException 
* @Description: TODO 下载文件异常
* @author Switch
* @date 2016年3月13日 下午2:54:14 
*  
*
 */
public class DownloadNotExistsFileException extends RuntimeException {

	private static final long serialVersionUID = 2969567696674112542L;
	
	public DownloadNotExistsFileException(String path) {
		super("无法下载文件：" + path + "，因为服务器端不存在这个文件....");
	}

}
