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

import com.example.myphotos.adapter.ImageAdapter;
import com.example.myphotos.model.ImageModel;
import com.example.myphotos.model.SearchImage;
import com.example.myphotos.utils.ApiUtilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ImageModel> list;
    private int page = 10;
    private ProgressDialog dialog;
    GridLayoutManager manager;

    private int pageSize = 30;
    private boolean isLoading;
    private boolean lastPage;
    ImageAdapter adapter;

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
                int visibleItem = manager.getChildCount();
                int totalItem = manager.getItemCount();
                int firstVisibleItemPo = manager.findFirstVisibleItemPosition();

                if (!isLoading && !lastPage) {
                    if ((visibleItem + firstVisibleItemPo >= totalItem) && firstVisibleItemPo >= 0
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
        ApiUtilities.getApiInterface().searchPhotos("Image",10,30,"portrait")
                .enqueue(new Callback<SearchImage>() {
                    @Override
                    public void onResponse(Call<SearchImage> call, Response<SearchImage> response) {
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
                    public void onFailure(Call<SearchImage> call, Throwable t) {
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

}