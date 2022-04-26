package com.compress.image.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * MultipartFile 转 File
 */
public class MultipartFileToFile {


    /**
     * MultipartFile 转 File
     *
     * @param multipartFile
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile multipartFile, File file) throws Exception {
        //判断是否为null
        if (multipartFile.equals("") || multipartFile.getSize() <= 0) {
            return file;
        }
        //MultipartFile转换为File
        InputStream ins = null;
        OutputStream os = null;
        try {
            ins = multipartFile.getInputStream();
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }
}
