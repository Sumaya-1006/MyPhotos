package com.example.myphotos;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class FullImageActivity extends AppCompatActivity {
    ImageView imageView;
    ViewFlipper viewFlipper;
    ImageButton wallpaperBtn,downloadBtn,shareBtn;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        imageView = findViewById(R.id.myZoomImgView);
        viewFlipper = findViewById(R.id.view_flipper);
        wallpaperBtn = findViewById(R.id.wallpaperBtn);
        downloadBtn = findViewById(R.id.downloadBtn);
        shareBtn = findViewById(R.id.shareBtn);

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
                           .setTitle("pic")
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

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Drawable mDrawable = imageView.getDrawable();
                Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

                String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "Image Description", null);
                Uri uri = Uri.parse(path);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "Share Image"));

            }
        });

    }
}