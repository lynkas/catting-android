package net.catting.android;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.GsonBuilder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.DisplayMetrics;
import android.util.Log;
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
    private static boolean loading=false;
    private static int postNo=-1;
    private static int maxPostNo=-1;
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

        mAdapter = new MainAdapter();
        recyclerView.setAdapter(mAdapter);
        getPost(count);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);

                int offset = recyclerView.computeVerticalScrollOffset();
                int extent = recyclerView.computeVerticalScrollExtent();
                int range = recyclerView.computeVerticalScrollRange();
//                Log.d(TAG, "onScrolled: "+(range));
//                Log.d(TAG, "onScrolled: "+(extent));
//                Log.d(TAG, "onScrolled: "+(offset));

                if (range-extent-offset<height/2){
                    if(!loading)getPost(count);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getPost(int count){
        new Thread(()->{
            loading=true;
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
                posts = new ArrayList<>();
            }
            for (PostBrief postBrief:posts){
                if (postNo==-1){
                    postNo=postBrief.id;
                    maxPostNo=postBrief.id;
                };
                if (postBrief.id>=0){
                    postNo=Integer.min(postBrief.id,postNo);
                    Log.d(TAG, "getPost: ID"+postNo);
                }
            }
            final List<PostBrief> fPosts=posts;
            runOnUiThread(()->mAdapter.addPosts(fPosts));
            Log.d(TAG, "getPost: Loaded once");
            loading=false;
        }
        ).start();
    }

    private void getUpdate(){
        new Thread(()->{
            try {
                loading=true;
                Call<List<PostBrief>> call;
                if (maxPostNo==-1){
                    return;
                }else {
                    call = Application.api.mainPostRefresh(maxPostNo);
                }

                List<PostBrief> posts;
                try {
                    posts = call.execute().body();
                }catch (Exception e){
                    posts = new ArrayList<>();
                }
                if (posts.size()==0){
                    return;
                }
                for (PostBrief postBrief:posts){
                    if (postBrief.id>=0){
                        maxPostNo=Integer.max(postBrief.id,maxPostNo);
                        Log.d(TAG, "getPost: ID"+maxPostNo);
                    }
                }
                final List<PostBrief> fPosts=posts;
                runOnUiThread(()->mAdapter.addPosts(fPosts,true));
                Log.d(TAG, "getPost: Loaded once");
                loading=false;
            }finally {
                swipeRefreshLayout.setRefreshing(false);

            }
        }
        ).start();


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
}
