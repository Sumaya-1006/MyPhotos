package com.example.myphotos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.example.myphotos.adapter.ImageAdapter;
import com.example.myphotos.model.ImageModel;
import com.example.myphotos.model.SearchImage;
import com.example.myphotos.utils.ApiUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ImageModel> list;
    public static  int page = 0;
    private ProgressDialog dialog;
    GridLayoutManager manager;
    private int pageSize = 30;
    private boolean isLoading;
    private boolean lastPage;
    ImageAdapter adapter;
    ImageSlider imageSlider;
    int regular = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerId);
        recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new ImageAdapter(this, list);
        recyclerView.setAdapter(adapter);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading.....");
        dialog.setCancelable(false);
        manager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(manager);
        dialog.show();

        getData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int item = manager.getChildCount();
                int totalItem = manager.getItemCount();
                int firstVisibleItemPo = manager.findFirstVisibleItemPosition();

                if (!isLoading && !lastPage) {
                    if ((item + firstVisibleItemPo >= totalItem) && firstVisibleItemPo >= 0
                            && totalItem >= pageSize) {
                        page++;
                        getData();
                    }

                }

            }
        });

    }

    private void getData() {
        isLoading = true;
        ApiUtilities.getApiInterface().getPhotos("Images",page,40,"portrait")
                .enqueue(new Callback<ImageModel>() {
                    @Override
                    public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {
                        if (response.body() != null) {
                            SearchImage apiResponse = response.body();
                            list.addAll(apiResponse.getResults());
                            adapter.updateDataSet(list);

                        }
                        isLoading = false;
                        dialog.dismiss();

                        Log.d("ApiResponse", String.valueOf(list.size()));
                    }

                    @Override
                    public void onFailure(Call<ImageModel> call, Throwable t) {
                        dialog.dismiss();
                        Log.d("ApiResponse", "Error" + t.getMessage());
                        Toast.makeText(MainActivity.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        MenuItem search = menu.findItem(R.id.searchId);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchImages(query);
                dialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });
        return true;
    }

    private void searchImages(String query) {
        ApiUtilities.getApiInterface().searchImages(query).enqueue(new Callback<SearchImage>() {
            @Override
            public void onResponse(Call<SearchImage> call, Response<SearchImage> response) {
                if (response.body() != null) {
                    list.clear();
                    list.addAll(response.body().getResults());
                    adapter.updateDataSet(list);
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<SearchImage> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}