
package com.compress.image.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.UUID;

/**
 * 压缩图片到指定大小工具类<br>
 * 〈CompressionFileUtil〉
 *
 * @author Administrator
 * @create 2020/7/27
 * @since 1.0.0
 */
@Slf4j
public class CompressionFileUtil {
    /**
     * @param multipartFile
     * @return 临时保存的文件路径
     * @throws IOException
     */
    public static String changeImg(MultipartFile multipartFile, Integer maxByte, HttpServletRequest request) {
        try {
            //本地图片地址
            String localPath = "." + File.separator + "temp_img" + File.separator + multipartFile.getOriginalFilename();
            File local_img_file = new File(localPath);
            if (!local_img_file.getParentFile().exists()) {
                //创建文件夹路径,可创建多级目录
                local_img_file.getParentFile().mkdir();
            }
            //创建文件路径
            if (!local_img_file.exists()) {
                local_img_file.createNewFile();
            }
            //MultipartFile 转 File
            local_img_file = MultipartFileToFile.multipartFileToFile(multipartFile, local_img_file);

            //保存的文件地址
            String fileName = UUID.randomUUID().toString();
            fileName = fileName.replaceAll("-", "");
            String tempUrl = "." + File.separator + "temp_img" + File.separator + fileName + ".jpg";
            File tempFile = new File(tempUrl);
            //创建压缩的图片路径
            if (!tempFile.exists()) {
                log.info(tempUrl + "\t is null,create new file......");
                tempFile.createNewFile();
            }
            String file1Path = local_img_file.getPath();
            //将传进来的图片变为byte[]
            byte[] imgBytes = getByteByPic(file1Path);
            //压缩byte[]
            byte[] resultImg = compressUnderSize(imgBytes, maxByte * 1024);
            //将byte[]流导入图片中
            byte2image(resultImg, tempUrl);
            //删除临时文件
            local_img_file.delete();
            return tempUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    public static byte[] getByteByPic(String imageUrl) throws IOException {
        File imageFile = new File(imageUrl);
        InputStream inStream = new FileInputStream(imageFile);

        BufferedInputStream bis = new BufferedInputStream(inStream); //缓存流
        //IImage是一个抽象类，BufferedImage是其实现类，是一个带缓冲区图像类，主要作用是将一幅图片加载到内存中（BufferedImage生成的图片在内存里有一个图像缓冲区，利用这个缓冲区我们可以很方便地操作这个图片）
        // 提供获得绘图对象、图像缩放、选择图像平滑度等功能，通常用来做图片大小变换、图片变灰、设置透明不透明等。
        BufferedImage bm = ImageIO.read(bis);
        //ByteArrayOutputStream 对byte类型数据进行写入的类 相当于一个中间缓冲层，将类写入到文件等其他outputStream。它是对字节进行操作，属于内存操作流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        String type = imageUrl.substring(imageUrl.length() - 3);
        ImageIO.write(bm, type, bos);
        bos.flush();
        byte[] data = bos.toByteArray();
        return data;
    }

    /**
     * 将图片压缩到指定大小以内
     *
     * @param srcImgData 源图片数据
     * @param maxSize    目的图片大小
     * @return 压缩后的图片数据
     */
    public static byte[] compressUnderSize(byte[] srcImgData, long maxSize) {
        double scale = 0.9;
        byte[] imgData = Arrays.copyOf(srcImgData, srcImgData.length);

        //循环比较 压缩后的数据是否比指定的值大
        if (imgData.length > maxSize) {
            do {
                try {
                    imgData = compress(imgData, scale);
                    System.out.println("-----------------------压缩后的图片大小:" + imgData.length / 1024 + "k");
                } catch (IOException e) {
                    throw new IllegalStateException("压缩图片过程中出错，请及时联系管理员！", e);
                }

            } while (imgData.length > maxSize);
        }

        return imgData;
    }

    /**
     * 按照 宽高 比例压缩
     *
     * @param scale 压缩刻度
     * @return 压缩后图片数据
     * @throws IOException 压缩图片过程中出错
     */
    public static byte[] compress(byte[] srcImgData, double scale) throws IOException {
        BufferedImage bi = ImageIO.read(new ByteArrayInputStream(srcImgData));
        int width = (int) (bi.getWidth() * scale); // 源图宽度
        int height = (int) (bi.getHeight() * scale); // 源图高度

        Image image = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = tag.getGraphics();
        g.setColor(Color.RED);
        g.drawImage(image, 0, 0, null); // 绘制处理后的图
        g.dispose();

        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        ImageIO.write(tag, "JPEG", bOut);

        return bOut.toByteArray();
    }

    /**
     * byte数组到图片
     */
    public static void byte2image(byte[] data, String path) {
        if (data.length < 3 || path.equals("")) return;
        try {
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            System.out.println("Make Picture success,Please find image in " + path);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        }
    }
}