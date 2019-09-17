package net.catting.android.data.structs;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Post extends PostBrief{
    public String content;

    public Post[] append;
    @SerializedName("append_media")
    public Media[] appendMedia;
    public String date;

}
