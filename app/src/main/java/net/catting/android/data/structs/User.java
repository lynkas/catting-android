package net.catting.android.data.structs;

import com.google.gson.annotations.SerializedName;

public class User {
    public String pid;
    @SerializedName("profile_url")
    public String profileUrl;

    @SerializedName("nickname")
    public String nickName;
    public String front;
    public String behind;
    public String at;
}
