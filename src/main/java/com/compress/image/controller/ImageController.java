package com.compress.image.controller;

import cn.hutool.core.util.StrUtil;
import com.compress.image.util.CompressionFileUtil;
import com.compress.image.util.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Controller
public class ImageController {

    @Value("${file.maxByte}")
    public Integer maxByte;

    /**
     * 进入页面
     *
     * @param number   多个文件
     * @param modelMap
     * @return
     */
    @RequestMapping("/showMultiFile")
    public String showMultiFile(Integer number, ModelMap modelMap) {
        modelMap.addAttribute("number", number);
        return "showMultiFile";
    }

    /**
     * 压缩图片
     * https://www.jianshu.com/p/cdd3dfa207f0
     *
     * @param addFile
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/upload")
    public String upload(MultipartFile[] addFile, HttpServletRequest request) {
        String uploadPath = "." + File.separator + "temp_img" + File.separator;
        try {
            if (addFile == null || addFile.length <= 0) {
                return "请上传图片";
            }
            for (MultipartFile file : addFile) {
                String checkImg = ImageUtil.checkImg(file.getOriginalFilename());
                if (StrUtil.isEmpty(checkImg)) {
                    return "请上传图片";
                }
                File f = new File(uploadPath);
                if (!f.exists()) {
                    f.mkdir();
                }
                String random = UUID.randomUUID().toString();
                Thumbnails.of(file.getInputStream()).scale(0.8f).outputFormat("jpg").outputQuality(0.5).toFile(uploadPath + random);
                log.info("上传图片成功,图片路径:{}", uploadPath);
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 压缩图片到指定大小
     *
     * @param addFile
     * @param request
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/compressImage")
    public String compressImage(MultipartFile[] addFile, HttpServletRequest request) throws Exception {
        if (addFile == null || addFile.length <= 0) {
            return "请上传文件";
        }
        StringJoiner stringJoiner = new StringJoiner("\n");
        for (MultipartFile file : addFile) {
            String imgUrl = CompressionFileUtil.changeImg(file, maxByte, request);
            log.info("image url:{}", imgUrl);
            stringJoiner.add(imgUrl);
        }
        return stringJoiner.toString();
    }
}
