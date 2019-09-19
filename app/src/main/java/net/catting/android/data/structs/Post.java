package net.catting.android.data.structs;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Post extends PostBrief  implements Parcelable {
    public String content;

    public Post[] append;
    @SerializedName("append_media")
    public Media[] appendMedia;
    public String date;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.content);
        dest.writeTypedArray(this.append, flags);
        dest.writeTypedArray(this.appendMedia, flags);
        dest.writeString(this.date);
    }

    public Post() {
    }

    protected Post(Parcel in) {
        super(in);
        this.content = in.readString();
        this.append = in.createTypedArray(Post.CREATOR);
        this.appendMedia = in.createTypedArray(Media.CREATOR);
        this.date = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
