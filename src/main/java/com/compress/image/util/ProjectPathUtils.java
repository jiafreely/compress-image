package com.compress.image.util;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 获取项目路径工具类
 * @author Yang
 * @date 2019/8/7 10:51
 */
public class ProjectPathUtils {

    public static String getServerDomain(HttpServletRequest request){
        StringBuffer url = request.getRequestURL();
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append(request.getServletContext().getContextPath()).append("/").toString();

        //另一种方式
//        String url = "";
//        url = request.getScheme() +"://" + request.getServerName()
//                + ":" +request.getServerPort()
//                + request.getContextPath();
        return  tempContextUrl;
    }

    public static String getRealPath(HttpServletRequest request){
        //获取项目路径
        String realPath = request.getSession().getServletContext().getRealPath("");
        if(!realPath.endsWith("/") && !realPath.endsWith("\\")){
            realPath+= File.separator;
        }
        return  realPath;
    }

    public static String getRelativePath(HttpServletRequest request){
        //获取项目路径
        String realPath = request.getSession().getServletContext().getContextPath();
//        if(!realPath.endsWith("/") && !realPath.endsWith("\\")){
//            realPath+= File.separator;
//        }
        return  realPath;
    }

}
