package net.catting.android.data.structs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PostBrief implements Parcelable {
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

//    @Override
//    public int describeContents() {
//        return 0;
//    }

//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(id);
//        dest.writeInt(isPower?1:0);
//        dest.writeInt(show?1:0);
//        dest.writeParcelable(postBy);
//        dest.writeString(previewPure);
//        dest.writeString(preview);
//        dest.writeParcelable(repliedTo);
//        dest.writeString(preview);
//        dest.writeParcelable(media);
//        dest.writeInt(nsfw?1:0);
//        dest.writeInt(hide?1:0);
//    }
//    Creator<PostBrief>

    public PostBrief() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeByte(this.isPower ? (byte) 1 : (byte) 0);
        dest.writeByte(this.show ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.postBy, flags);
        dest.writeString(this.previewPure);
        dest.writeParcelable(this.repliedTo, flags);
        dest.writeString(this.preview);
        dest.writeParcelable(this.media, flags);
        dest.writeByte(this.nsfw ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hide ? (byte) 1 : (byte) 0);
    }

    protected PostBrief(Parcel in) {
        this.id = in.readInt();
        this.isPower = in.readByte() != 0;
        this.show = in.readByte() != 0;
        this.postBy = in.readParcelable(UserPublic.class.getClassLoader());
        this.previewPure = in.readString();
        this.repliedTo = in.readParcelable(Post.class.getClassLoader());
        this.preview = in.readString();
        this.media = in.readParcelable(Media.class.getClassLoader());
        this.nsfw = in.readByte() != 0;
        this.hide = in.readByte() != 0;
    }

    public static final Creator<PostBrief> CREATOR = new Creator<PostBrief>() {
        @Override
        public PostBrief createFromParcel(Parcel source) {
            return new PostBrief(source);
        }

        @Override
        public PostBrief[] newArray(int size) {
            return new PostBrief[size];
        }
    };
}
