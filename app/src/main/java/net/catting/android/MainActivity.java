package net.catting.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.GsonBuilder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import net.catting.android.api.IApi;
import net.catting.android.api.IApi;
import net.catting.android.data.S;
import net.catting.android.data.structs.Post;
import net.catting.android.data.structs.PostBrief;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Retrofit;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    private boolean errLast=false;

    private int postNo=-1;
    private int maxPostNo=-1;
    private RecyclerView recyclerView;
    private MainAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String TAG = "catting.main";
    private SwipeRefreshLayout swipeRefreshLayout;
    private int count =20;
    int height,width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.main_recycler);
        swipeRefreshLayout = findViewById(R.id.main_pull);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MainAdapter(new iBus(){
            @Override
            public void onClickReloadButton(View v) {
                getPost();
            }
        });
        recyclerView.setAdapter(mAdapter);
        getPost(count);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);

                int offset = recyclerView.computeVerticalScrollOffset();
                int extent = recyclerView.computeVerticalScrollExtent();
                int range = recyclerView.computeVerticalScrollRange();
                if (range-extent-offset<height/2&&!mAdapter.getLastErrState()){
                    if(!mAdapter.getLoadingState())getPost(count);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this::getUpdate);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener((view) -> Snackbar
//                .make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_check_update:
                Intent intent = new Intent(this,UpdateActivity.class);
                startActivity(intent);

        }
        return true;
    }

    private void getPost(){getPost(count);}

    private void getPost(int count){
        new Thread(()->{
            runOnUiThread(()->{
                mAdapter.setLoading();
                mAdapter.unsetLastErr();
            });

            Call<List<PostBrief>> call;
            if (postNo==-1){
                call = Application.api.mainPostsList(0,count);
            }else {
                call = Application.api.mainPostsMinCount(postNo,count);
            }

            List<PostBrief> posts;
            try {

                posts = call.execute().body();
            }catch (Exception e){
                runOnUiThread(()-> mAdapter.setLastErr());

                posts = new ArrayList<>();
            }
            for (PostBrief postBrief:posts){
                if (postNo==-1){
                    postNo=postBrief.id;
                    maxPostNo=postBrief.id;
                }
                if (postBrief.id>=0){
                    postNo=Integer.min(postBrief.id,postNo);
                    Log.d(TAG, "getPost: ID"+postNo);
                }
            }
            final List<PostBrief> fPosts=posts;
            runOnUiThread(()->mAdapter.addPosts(fPosts));
            Log.d(TAG, "getPost: Loaded once");
            runOnUiThread(()-> mAdapter.unsetLoading());

        }
        ).start();
    }

    private void getUpdate(){
        new Thread(()->{
            try {
                runOnUiThread(()->{
                    mAdapter.setLoading();
                    mAdapter.unsetLastErr();
                });
                Call<List<PostBrief>> call;
                if (maxPostNo==-1){
                    return;
                }else {
                    call = Application.api.mainPostRefresh(maxPostNo);
                }

                List<PostBrief> posts;
                try {
                    runOnUiThread(()->mAdapter.unsetLastErr());
                    posts = call.execute().body();
                }catch (Exception e){
                    posts = new ArrayList<>();
                    runOnUiThread(()->mAdapter.setLastErr());
                }
                if (posts.size()==0){

                    return;
                }
                for (PostBrief postBrief:posts){
                    if (postBrief.id>=0){
                        maxPostNo=Integer.max(postBrief.id,maxPostNo);
                    }
                }
                final List<PostBrief> fPosts=posts;
                runOnUiThread(()->{
                    mAdapter.addPosts(fPosts,true);
                    mAdapter.unsetLoading();
                });

            }finally {
                swipeRefreshLayout.setRefreshing(false);

            }
        }
        ).start();


    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
//
//        outState.putParcelableArrayList("posts",MainAdapter.posts);
//    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelableArrayList("posts",mAdapter.posts);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mAdapter.setPosts(savedInstanceState.getParcelableArrayList("posts"));
    }

    //    private void getPost(){
//        new Thread(()->{
//
//                Call<List<PostBrief>> call = Application.api.mainPostsList(postNo,postNo+10);
//                List<PostBrief> posts;
//                Log.d(TAG, "run: paole2");
//                try {
//                    posts = call.execute().body();
//                    Log.d(TAG, "run: paole");
//                }catch (IOException e){
//                    posts = new ArrayList<>();
//                    Log.e(TAG, "run: ", e);
//                }
//
//                final List<PostBrief> fPosts=posts;
//                runOnUiThread(()->mAdapter.addPosts(fPosts));
//            }
//        ).start();
//    }
    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
