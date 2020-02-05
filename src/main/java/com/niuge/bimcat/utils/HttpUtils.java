package com.niuge.bimcat.utils;

import com.niuge.bimcat.files.UploadFileStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Description: 用于发送各种Http请求的工具类
 */
public class HttpUtils {

    /**
     * @Description: 用于向静态资源服务器推送数据流(包括图片，视频等静态资源)
     * @param targetUrl 目标URL
     * @param status 上传的文件信息
     */
    // 发送post请求,获取String
    public static String postFile(String targetUrl, UploadFileStatus status) {

        try {
            URL url = new URL(targetUrl.trim());
            // 打开连接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // 设置允许输入输出
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            // 设置不用缓存
            urlConnection.setUseCaches(false);
            // 设置传递方式
            urlConnection.setRequestMethod("POST");
            // 设置维持长连接
            urlConnection.setRequestProperty("Connection", "keep-alive");
            // 设置字符集
            urlConnection.setRequestProperty("Charset", "UTF-8");
            // 设置文件长度和类型
            urlConnection.setRequestProperty("Content-Length", String.valueOf(status.getFileSize()));
            // 设置文件类型
            urlConnection.setRequestProperty("Content-Type", "application/x-form-urlencoded");

            // 设置传递的参数
            urlConnection.setRequestProperty("fileName", status.getFileName());
            urlConnection.setRequestProperty("fileType", status.getFileType());
            urlConnection.setRequestProperty("filePath", status.getFilePath());

            // 开始连接请求
            urlConnection.connect();
            OutputStream out = urlConnection.getOutputStream();
            // 获取上传文件的输入流
            InputStream fileIS = status.getInputStream();
            byte[] tempBytes = new byte[1024];
            int byteRead = 0;
            // 写入请求的字符串
            while((byteRead = fileIS.read(tempBytes)) != -1) {
                out.write(tempBytes, 0, byteRead);
            }

            out.flush();
            fileIS.close();
            out.close();
            //TODO
            System.out.println(status.getFileName() +"." + status.getFileType() + "的上传回复的code是:" + urlConnection.getResponseCode());
            // 请求返回的状态
            if (200 == urlConnection.getResponseCode()) {
                // 关闭数据流
                status.getInputStream().close();
                // 请求返回的数据
                InputStream in = urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while (-1 != (len = in.read(buffer))) {
                    baos.write(buffer, 0, len);
                    baos.flush();
                }

                return baos.toString("utf-8");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "fail";
        }
        return "fail";
    }

}

