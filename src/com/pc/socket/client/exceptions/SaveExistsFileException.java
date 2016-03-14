package com.pc.socket.client.exceptions;
/**
 * 
*
* @ClassName: SaveExistsFileException 
* @Description: TODO 保存文件异常
* @author Switch
* @date 2016年3月13日 下午2:53:22 
*  
*
 */
public class SaveExistsFileException extends RuntimeException {

	private static final long serialVersionUID = -1026575092082314002L;
	
	public SaveExistsFileException(String path) {
		super("保存文件:" + path + "失败，因为文件已经存在了。");
	}
	
}
