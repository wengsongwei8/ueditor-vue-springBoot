package com.weng.ueditor.upload.local.utils;

import com.weng.ueditor.common.config.AppConfig;
import com.weng.ueditor.common.enums.FileTypeEnum;
import com.weng.ueditor.common.Constants;
import com.weng.ueditor.ueditor.define.AppInfo;
import com.weng.ueditor.ueditor.define.BaseState;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;



@Slf4j
public class FileUtils {

    /**
     * 校验MultipartFile 是否图片
     *
     * @param file
     * @return
     */
    public static boolean isImage(MultipartFile file) {

        Pattern pattern = Pattern.compile(Constants.REG_IMG_FORMAT);
        Matcher matcher = pattern.matcher(file.getOriginalFilename());

        return matcher.find();
    }

    public static boolean isImage(File file) {

        Pattern pattern = Pattern.compile(Constants.REG_IMG_FORMAT);
        Matcher matcher = pattern.matcher(file.getName());

        return matcher.find();
    }

    /**
     * 校验MultipartFile 是否csv文件
     *
     * @param file
     * @return
     */
    public static boolean isCsv(MultipartFile file) {
        return file.getOriginalFilename().toLowerCase().endsWith(FileTypeEnum.CSV.getFormat());
    }





    /**
     * 上传文件
     *
     * @param file
     * @param path     上传路径
     * @param fileName 文件名（不含文件类型）
     * @return 保存路径
     * @throws IOException
     */
    public static String upload(MultipartFile file, String path, String fileName) throws IOException {

        String originalFilename = file.getOriginalFilename();
        String format = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String newFilename = fileName + "." + format;

        String returnPath = (path.endsWith("/") ? path : path + "/") + newFilename;

        String filePath = AppConfig.DEFAULT_UPLOAD_FILE_PATH + returnPath;

        File dest = new File(filePath);

        if (!dest.exists()) {
            dest.getParentFile().mkdirs();
        }

        file.transferTo(dest);

        return returnPath;
    }


    /**
     * 上传本地文件
     *
     * @param file
     * @param path     上传路径
     * @param fileName 文件名（不含文件类型）
     * @return 保存路径
     * @throws IOException
     */
    public static String upload(File file, String path, String fileName, String originalFilename) throws IOException {
        String format = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String newFilename = fileName + "." + format;

        String returnPath = (path.endsWith("/") ? path : path + "/") + newFilename;

        String filePath = AppConfig.DEFAULT_UPLOAD_FILE_PATH + returnPath;

        File dest = new File(filePath);
        try {
            org.apache.commons.io.FileUtils.moveFile(file, dest);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }

        if (!dest.exists()) {
            dest.getParentFile().mkdirs();
        }

//        file.transferTo(dest);

        return returnPath;
    }

    /**
     * 下载文件
     *
     * @param filePath
     * @param response
     */
    public static void download(String filePath, HttpServletResponse response) {
        if (!StringUtils.isEmpty(filePath)) {
            File file = null;
            if (!filePath.startsWith(AppConfig.DEFAULT_UPLOAD_FILE_PATH)) {
                file = new File(AppConfig.DEFAULT_UPLOAD_FILE_PATH + filePath);
            } else {
                file = new File(filePath);
            }
            if (file.exists()) {
                byte[] buffer = new byte[0];
                try (
                        InputStream is = new BufferedInputStream(new FileInputStream(filePath));
                        OutputStream os = new BufferedOutputStream(response.getOutputStream());
                ) {
                    buffer = new byte[is.available()];
                    is.read(buffer);
                    response.reset();
                    response.addHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes(), "UTF-8"));
                    response.addHeader("Content-Length", "" + file.length());
                    response.setContentType("application/octet-stream;charset=UTF-8");
                    os.write(buffer);
                    os.flush();
                } catch (IOException e) {
                    log.info(e.getMessage(),e);
                } finally {
                    /*try {
                        if (null != is) {
                            is.close();
                        }
                        if (null != os) {
                            os.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    remove(filePath);
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @return
     */
    public static boolean remove(String filePath) {
        if (!filePath.startsWith(AppConfig.DEFAULT_UPLOAD_FILE_PATH)) {
            filePath = AppConfig.DEFAULT_UPLOAD_FILE_PATH + filePath;
        }
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            file.delete();
            return true;
        }
        return false;
    }


    /**
     * 删除文件夹及其下文件
     *
     * @param dir
     * @return
     */
    public static void deleteDir(File dir) {
        if(null==dir || !dir.exists()){
            log.info("删除文件为空");
            return;
        }
        if (dir.isFile() || dir.list().length == 0) {
            dir.delete();
        } else {
            for (File f : dir.listFiles()) {
                deleteDir(f);
            }
            dir.delete();
        }
    }

    /**
     * 格式化文件目录
     *
     * @param filePath
     * @return
     */
    public static String formatFilePath(String filePath) {
        return filePath.replace(AppConfig.DEFAULT_UPLOAD_FILE_PATH, "").replaceAll(File.separator + "{2,}", File.separator);
    }

    /**
     * 压缩文件到zip
     *
     * @param files
     * @param targetFile
     */
    public static void zipFile(List<File> files, File targetFile) {
        byte[] bytes = new byte[1024];

        try {
            try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(targetFile))) {
                for (File file : files) {
                    try (FileInputStream in = new FileInputStream(file)) {
                        out.putNextEntry(new ZipEntry(file.getName()));
                        int length;
                        while ((length = in.read(bytes)) > 0) {
                            out.write(bytes, 0, length);
                        }
                        out.closeEntry();
                        //in.close();
                    }
                }
                //out.close();
            }
        } catch (Exception e) {
            log.info(e.getMessage(),e);
        }
    }

    public static String getFilePath(FileTypeEnum type, Long id) {
        StringBuilder sb = new StringBuilder(AppConfig.DEFAULT_UPLOAD_FILE_PATH);
        if (!sb.toString().endsWith(File.separator)) {
            sb.append(File.separator);
        }
        sb.append("download");
        sb.append(new SimpleDateFormat("yyyyMMdd").format(new Date())).append(File.separator);
        sb.append(type.getType()).append(File.separator);
        File dir = new File(sb.toString());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        sb.append(id).append("_").append(System.currentTimeMillis()).append(type.getFormat());
        return sb.toString().replaceAll(File.separator + "{2,}", File.separator);
    }

    public static boolean delete(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            file.delete();
            return true;
        }
        return false;
    }

    /*
     * 根据文件后缀，获取文件所属类型
     * @param [extension]
     * @return
     */
    public static String getFileTypeByExtension(String extension){
        extension = extension == null ? "": extension.trim();
        for(FileTypeEnum fileType : FileTypeEnum.class.getEnumConstants()){
            String[] formats = fileType.getFormat().toLowerCase().split(",");
            for(String formatTemp : formats){
                if(extension.equalsIgnoreCase(formatTemp.trim())){
                    return fileType.getType();
                }
            }
        }
        return "other";
    }

}
