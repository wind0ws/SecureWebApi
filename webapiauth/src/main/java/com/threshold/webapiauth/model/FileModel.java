package com.threshold.webapiauth.model;

/**
 * Created by Threshold on 2016/1/12.
 */
public class FileModel {
    private String name,url, size;

    public FileModel() {
    }

    public FileModel(String name, String url, String size) {
        this.name = name;
        this.url = url;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "FileModel{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
