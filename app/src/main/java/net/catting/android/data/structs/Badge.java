package net.catting.android.data.structs;

import com.google.gson.annotations.SerializedName;

public class Badge {
    public UserPublic creator;
    public String introduction;

    @SerializedName("img_url")
    public String ImgUrl;
    public boolean official;

    @SerializedName("badge_name")
    public String badgeName;
}
