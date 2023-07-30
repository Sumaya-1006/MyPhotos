package com.example.myphotos.Interface;

import com.example.myphotos.model.ImageModel;
import com.example.myphotos.model.SearchImage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("search/photos")
    Call<ImageModel> getPhotos(
            @Query("query")String query,
            @Query("page")int page,
            @Query("per_page")int per_page,
            @Query("orientation")String orientation
    );

    @GET("/search/photos")
    Call<SearchImage> searchImages(
            @Query("query")String query
    );


}
