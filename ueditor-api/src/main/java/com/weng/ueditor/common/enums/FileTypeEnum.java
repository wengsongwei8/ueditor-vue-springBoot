
package com.weng.ueditor.common.enums;

public enum FileTypeEnum {
    EXCEL("excel", "xls,xlsx"),
    DOC("doc", "doc,docx"),
    PPT("ppt", "ppt,pptx"),
    PDF("pdf", "pdf"),
    IMAGE("image", "pcx,emf ,gif ,bmp ,tga ,jpg,tif,tiff,jpeg,png,rle,psd"),
    VIDEO("video", "avi,mov,rmvb,rm,flv,mp4,3gp"),
    AUDIO("audio", "wav,midi,cda,mp3,wma,mp4"),
    FLASH("flash", "swf"),
    ZIP("zip", "zip,tar.gz"),
    CSV("csv", "csv");

    private String type;
    private String format;

    public String getType() {
        return type;
    }

    public String getFormat() {
        return format;
    }

    FileTypeEnum(String type, String format) {
        this.type = type;
        this.format = format;
    }



}
