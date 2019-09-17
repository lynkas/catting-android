package net.catting.android.data.structs;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Media {
    public String status;
    @SerializedName("video_url")
    public String videoUrl;
    @SerializedName("image_url")
    public String imageUrl;
    @SerializedName("embed_media")
    public String embedMedia;
    @SerializedName("is_image")
    public boolean isImage;

    public String onDelete;
    @SerializedName("media_type")
    public String mediaType;
    public String date;
}
