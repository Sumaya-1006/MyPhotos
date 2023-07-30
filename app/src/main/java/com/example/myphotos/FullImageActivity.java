package com.example.myphotos;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.myphotos.model.ImageModel;
import com.example.myphotos.model.SearchImage;
import com.example.myphotos.model.UrlsModel;

public class FullImageActivity extends AppCompatActivity {
    ImageView imageView;
    ViewFlipper viewFlipper;
    ImageButton wallpaperBtn,downloadBtn;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        imageView = findViewById(R.id.myZoomImgView);
        viewFlipper = findViewById(R.id.view_flipper);
        wallpaperBtn = findViewById(R.id.wallpaperBtn);
        downloadBtn = findViewById(R.id.downloadBtn);

        String url = getIntent().getStringExtra("image");
       // Glide.with(this).load(getIntent().getStringExtra("image")).into(imageView);
       Glide.with(getApplicationContext()).load(url).into(imageView);

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager manager = null;
                manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
               try {
                   Uri uri = Uri.parse(url);

                   DownloadManager.Request request = new DownloadManager.Request(uri);
                   request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                           .setAllowedOverRoaming(false)
                           .setTitle("Wallpaper")
                           .setMimeType("image/jpg")
                           .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                           .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "Wallpaper" + imageView+ ".jpg");

                   manager.enqueue(request);

                   Toast.makeText(FullImageActivity.this, "Download completed", Toast.LENGTH_SHORT).show();
               }catch (Exception e){
                   Toast.makeText(FullImageActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
               }
            }
        });

        wallpaperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    WallpaperManager manager = WallpaperManager.getInstance(FullImageActivity.this);
                    bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    manager.setBitmap(bitmap);

                    Toast.makeText(FullImageActivity.this, "Bitmap applied", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(FullImageActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}