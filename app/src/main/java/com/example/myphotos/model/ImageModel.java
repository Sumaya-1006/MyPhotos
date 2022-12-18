package com.example.myphotos.model;

public class ImageModel extends SearchImage {
    private UrlsModel urls;

    public ImageModel(UrlsModel urls) {
        this.urls = urls;
    }

    public UrlsModel getUrls() {
        return urls;
    }

    public void setUrls(UrlsModel urls) {
        this.urls = urls;
    }
}
