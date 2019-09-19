package net.catting.android.data.structs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Media  implements Parcelable {
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
    @SerializedName("image_width")
    public int imageWidth;
    @SerializedName("image_height")
    public int imageHeight;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeString(this.videoUrl);
        dest.writeString(this.imageUrl);
        dest.writeString(this.embedMedia);
        dest.writeByte(this.isImage ? (byte) 1 : (byte) 0);
        dest.writeString(this.onDelete);
        dest.writeString(this.mediaType);
        dest.writeString(this.date);
        dest.writeInt(this.imageWidth);
        dest.writeInt(this.imageHeight);
    }

    public Media() {
    }

    protected Media(Parcel in) {
        this.status = in.readString();
        this.videoUrl = in.readString();
        this.imageUrl = in.readString();
        this.embedMedia = in.readString();
        this.isImage = in.readByte() != 0;
        this.onDelete = in.readString();
        this.mediaType = in.readString();
        this.date = in.readString();
        this.imageWidth = in.readInt();
        this.imageHeight = in.readInt();
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel source) {
            return new Media(source);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };
}
