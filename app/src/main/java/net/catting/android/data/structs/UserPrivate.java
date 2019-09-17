package net.catting.android.data.structs;

import com.google.gson.annotations.SerializedName;

public class UserPrivate extends User {
    public UserMeta meta;
    @SerializedName("post_count")
    public int postCount;
    @SerializedName("note_count")
    public int noteCount;
}
