package com.niuge.bimcat.files;

import java.io.FileInputStream;
import java.io.InputStream;

public class UploadFileStatus {

    /*文件名字*/
    private String fileName;
    /*文件类型，包括img,video,html,preview_html等*/
    private String fileType;
    /*文件路径，用于提示文件服务器将文件存储到何种路径之下*/
    private String filePath;
    /*文件的输出流*/
    private FileInputStream inputStream;
    /*文件的大小*/
    private long fileSize;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(FileInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}

