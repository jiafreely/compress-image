package com.compress.image.util;

import net.coobird.thumbnailator.Thumbnails;

import java.io.IOException;

public class ImageUtil {
    /**
     * 按尺寸原比例缩放图片 *@paramsource输入源 *@paramoutput输出源 *@paramwidth256 *@paramheight256 *@throwsIOException
     */
    public static void imgThumb(String source, String output, int width, int height) throws IOException {
        Thumbnails.of(source).size(width, height).toFile(output);
    }

    /**
     * 按照比例进行缩放 *@paramsource输入源 *@paramoutput输出源 *@paramscale比例 *@throwsIOException
     */
    public static void imgScale(String source, String output, double scale) throws IOException {
        Thumbnails.of(source).scale(scale).toFile(output);
    }

    /**
     * 检查图片
     * @param uploadContentType
     * @return
     */
    public static String checkImg(String uploadContentType){
        String expandedName = "";
        if (uploadContentType.endsWith(".jpg") || uploadContentType.endsWith(".JPG")) {
            // IE6上传jpg图片的headimageContentType是image/pjpeg，而IE9以及火狐上传的jpg图片是image/jpeg
            expandedName = "jpg";
        } else if (uploadContentType.endsWith(".png") || uploadContentType.endsWith(".PNG")) {
            // IE6上传的png图片的headimageContentType是"image/x-png"
            expandedName = "png";
        }
        return expandedName;
    }

}
