package com.weng.ueditor.ueditor.upload;


import com.weng.ueditor.common.utils.ResourceUrlUtil;
import com.weng.ueditor.ueditor.define.AppInfo;
import com.weng.ueditor.ueditor.define.BaseState;
import com.weng.ueditor.ueditor.define.State;
import com.weng.ueditor.upload.factory.UploadServiceFactory;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

@Component
@ConfigurationProperties(prefix="nginx")
public class StorageManager {

	private static Logger logger = LoggerFactory.getLogger(StorageManager.class);

	public static final int BUFFER_SIZE = 8192;
	
	private static String fileurl;

	public static String getFileurl() {
		return fileurl;
	}

	public static void setFileurl(String fileurl) {
		StorageManager.fileurl = fileurl;
	}

	public static int getBufferSize() {
		return BUFFER_SIZE;
	}

	public StorageManager() {
	}

	public static State saveBinaryFile(byte[] data, String path) {
		File file = new File(path);

		State state = valid(file);

		if (!state.isSuccess()) {
			return state;
		}

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bos.write(data);
			bos.flush();
			bos.close();
		} catch (IOException ioe) {
			return new BaseState(false, AppInfo.IO_ERROR);
		}

		state = new BaseState(true, file.getAbsolutePath());
		state.putInfo( "size", data.length );
		state.putInfo( "title", file.getName() );
		return state;
	}

	public static  State saveFileByInputStream(HttpServletRequest request, InputStream is, String path,
                                               long maxSize, String originFileName) throws IOException {
		//String uploadPath = request.getSession().getServletContext().getRealPath("");//this.getClass().getResource("/").getPath();//Class.class.getClass().getResource("/").getPath();
		File directory = new File("");// 参数为空
		String uploadPath = directory.getCanonicalPath();
		System.out.println(uploadPath);
		//uploadPath = URLDecoder.decode(uploadPath,"utf-8");
		State state = null;
		//File uploadFile = new File(uploadPath+"/"+path);
		File tmpFile = getTmpFile();
		byte[] dataBuf = new byte[2048];
		BufferedInputStream bis = new BufferedInputStream(is, 8192);
		try
		{
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(tmpFile), 8192);
			int count = 0;
			while ((count = bis.read(dataBuf)) != -1) {
				bos.write(dataBuf, 0, count);
			}
			bos.flush();
			bos.close();

			if (tmpFile.length() > maxSize) {
				tmpFile.delete();
				return new BaseState(false, 1);
			}
			UploadServiceFactory uploadServiceFactory = new UploadServiceFactory();
			String visitUrl = uploadServiceFactory.getUploadService().uploadLocalFile(tmpFile, originFileName);
			if(visitUrl == null){
				return new BaseState(false, 1);
			}
//			state = saveTmpFile(tmpFile, uploadPath+"/static/"+path);
			state = new BaseState(true);
			state.putInfo( "size", tmpFile.length() );
			state.putInfo( "title", tmpFile.getName() );
			if (!state.isSuccess()) {
				tmpFile.delete();
			}
			visitUrl = ResourceUrlUtil.convertVisitUrl(visitUrl);
			state.putInfo( "url", visitUrl);//文件访问的url填入此处
			return state;
		}
		catch (IOException localIOException) {
			logger.error("",localIOException);
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	public static State saveFileByInputStream(InputStream is, String path, String picName) {
		State state = null;

		File tmpFile = getTmpFile();

		byte[] dataBuf = new byte[ 2048 ];
		BufferedInputStream bis = new BufferedInputStream(is, StorageManager.BUFFER_SIZE);

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(tmpFile), StorageManager.BUFFER_SIZE);

			int count = 0;
			while ((count = bis.read(dataBuf)) != -1) {
				bos.write(dataBuf, 0, count);
			}
			bos.flush();
			bos.close();

			//state = saveTmpFile(tmpFile, path);
			//重新将文件转成文件流的方式
//			InputStream in = new FileInputStream(tmpFile);
//			UploadUtils uploadUtils = new UploadUtils();
//			boolean success = uploadUtils.uploadFile(in, path, picName);
			boolean success = true;
			
			//如果上传成功
			if (success) {
				state = new BaseState(true);
				state.putInfo( "size", tmpFile.length() );
				state.putInfo( "title", tmpFile.getName() );
				tmpFile.delete();
			}else{
				state = new BaseState(false, 4);
				tmpFile.delete();
			}

			return state;
		} catch (IOException e) {
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static File getTmpFile() {
		File tmpDir = FileUtils.getTempDirectory();
		String tmpFileName = (Math.random() * 10000 + "").replace(".", "");
		return new File(tmpDir, tmpFileName);
	}

	private static State saveTmpFile(File tmpFile, String path) {
		State state = null;
		File targetFile = new File(path);

		/*if(!targetFile.exists()) {
			targetFile.mkdirs();
		}*/
		/*if (!targetFile.canWrite()) {
			return new BaseState(false, AppInfo.PERMISSION_DENIED);
		}*/
		try {
			FileUtils.moveFile(tmpFile, targetFile);
		} catch (IOException e) {
			logger.error("",e);
			return new BaseState(false, AppInfo.IO_ERROR);
		}

		state = new BaseState(true);
		state.putInfo( "size", targetFile.length() );
		state.putInfo( "title", targetFile.getName() );
		return state;
	}

	private static State valid(File file) {
		File parentPath = file.getParentFile();

		if ((!parentPath.exists()) && (!parentPath.mkdirs())) {
			return new BaseState(false, AppInfo.FAILED_CREATE_FILE);
		}

		if (!parentPath.canWrite()) {
			return new BaseState(false, AppInfo.PERMISSION_DENIED);
		}

		return new BaseState(true);
	}

	public static void writeToLocal(String destination, InputStream input) throws IOException {
		byte[] bytes = new byte[1024];
		FileOutputStream downloadFile = new FileOutputStream(destination);

		try {
			int index;
			while((index = input.read(bytes)) != -1) {
				downloadFile.write(bytes, 0, index);
				downloadFile.flush();
			}

			downloadFile.flush();
			downloadFile.close();
			input.close();
		} finally {
			downloadFile.close();
			input.close();
		}

	}

}
