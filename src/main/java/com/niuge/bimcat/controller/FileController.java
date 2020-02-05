package com.niuge.bimcat.controller;

import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(value = "/file")
public class FileController {

    /**
     * 接收上传的文件，并且将上传的文件存储在指定路径下
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload")
    public String upload(HttpServletRequest request) throws FileNotFoundException {
        ServletInputStream sis = null;
        FileOutputStream fos = null;
        String url="";
        MultipartFile multipartFile=null;
        if (request instanceof StandardMultipartHttpServletRequest){
            multipartFile= ((StandardMultipartHttpServletRequest) request).getFile("file");
        }
        else {
            multipartFile= ((DefaultMultipartHttpServletRequest) request).getFile("file");
        }



        // 获取文件名
        String filename = multipartFile.getOriginalFilename();
        //filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + filename;
        String path = new File(ResourceUtils.getURL("classpath:").getPath())+"/static/uploadfile/" +filename;
        File dest = new File(path);
        // 文件类型，例如：jpg、png、pdf...request
        // 存储路径
        String filePath = request.getHeader("filePath");
        //        //判断文件是否已经存在
        if (dest.exists()) {
            return "文件已经存在";
        }

        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }

        try {
            //上传文件
            multipartFile.transferTo(dest); //保存文件
            /* url="http://你自己的域名/项目名/images/"+fileName;//正式项目 */
            url="http://localhost:8088/uploadfile/"+filename;
        } catch (IOException e) {
            return "上传失败";
        }

        return url;
    }

    @RequestMapping(value = "/delete/{filename}",method = RequestMethod.DELETE)
    public String delete(@PathVariable("filename") String filename) throws FileNotFoundException{
        try{
            String path = new File(ResourceUtils.getURL("classpath:").getPath())+"/static/uploadfile/" +filename;
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            else {
                return "File is not exist";
            }
            return "Delete file Success";
        }
        catch (IOException e){
            return "Delete file failed";
        }
    }
}

