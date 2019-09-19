package net.catting.android.data.structs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User  implements Parcelable {
    public String pid;
    @SerializedName("profile_url")
    public String profileUrl;

    @SerializedName("nickname")
    public String nickName;
    public String front;
    public String behind;
    public String at;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pid);
        dest.writeString(this.profileUrl);
        dest.writeString(this.nickName);
        dest.writeString(this.front);
        dest.writeString(this.behind);
        dest.writeString(this.at);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.pid = in.readString();
        this.profileUrl = in.readString();
        this.nickName = in.readString();
        this.front = in.readString();
        this.behind = in.readString();
        this.at = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}