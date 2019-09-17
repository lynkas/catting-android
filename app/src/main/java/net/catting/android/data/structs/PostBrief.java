package net.catting.android.data.structs;

import com.google.gson.annotations.SerializedName;

public class PostBrief {
    public final static int MEDIA_NONE = 0;
    public final static int MEDIA_IMAGE = 1;
    public final static int MEDIA_VIDEO = 2;
    public int id;
    @SerializedName("is_power")
    public boolean isPower;
    public boolean show;

    @SerializedName("post_by")
    public UserPublic postBy;
    @SerializedName("preview_pure")
    public String previewPure;
    @SerializedName("replied_to")
    public Post repliedTo;
    public String preview;
    public Media media;
    public boolean nsfw;
    public boolean hide;

}
