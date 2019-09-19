package net.catting.android.data.structs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Badge  implements Parcelable {
    public UserPublic creator;
    public String introduction;

    @SerializedName("img_url")
    public String ImgUrl;
    public boolean official;

    @SerializedName("badge_name")
    public String badgeName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.creator, flags);
        dest.writeString(this.introduction);
        dest.writeString(this.ImgUrl);
        dest.writeByte(this.official ? (byte) 1 : (byte) 0);
        dest.writeString(this.badgeName);
    }

    public Badge() {
    }

    protected Badge(Parcel in) {
        this.creator = in.readParcelable(UserPublic.class.getClassLoader());
        this.introduction = in.readString();
        this.ImgUrl = in.readString();
        this.official = in.readByte() != 0;
        this.badgeName = in.readString();
    }

    public static final Creator<Badge> CREATOR = new Creator<Badge>() {
        @Override
        public Badge createFromParcel(Parcel source) {
            return new Badge(source);
        }

        @Override
        public Badge[] newArray(int size) {
            return new Badge[size];
        }
    };
}
