package net.catting.android.api;

import net.catting.android.data.structs.Post;
import net.catting.android.data.structs.PostBrief;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface IApi{



    @GET("post/")
    Call<List<Post>> posts(@Query("tree") int id);

    @GET("main/")
    Call<List<PostBrief>> mainPostsList(@Query("from") int from, @Query("to") int to);



}
