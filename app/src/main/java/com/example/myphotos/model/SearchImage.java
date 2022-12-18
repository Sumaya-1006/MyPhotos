package com.example.myphotos.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchImage {
    @SerializedName("total")
    int total;
    @SerializedName("total_pages")
    int totalPages;
    @SerializedName("results")
    ArrayList<ImageModel> results;

    public ArrayList<ImageModel> getResults() {
        return results;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setResults(ArrayList<ImageModel> results) {

        this.results = results;
    }
}


