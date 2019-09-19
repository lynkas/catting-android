package net.catting.android.data.structs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserMeta  implements Parcelable {
    public int id;
    public String userName;
    public String email;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.userName);
        dest.writeString(this.email);
    }

    public UserMeta() {
    }

    protected UserMeta(Parcel in) {
        this.id = in.readInt();
        this.userName = in.readString();
        this.email = in.readString();
    }

    public static final Creator<UserMeta> CREATOR = new Creator<UserMeta>() {
        @Override
        public UserMeta createFromParcel(Parcel source) {
            return new UserMeta(source);
        }

        @Override
        public UserMeta[] newArray(int size) {
            return new UserMeta[size];
        }
    };
}
