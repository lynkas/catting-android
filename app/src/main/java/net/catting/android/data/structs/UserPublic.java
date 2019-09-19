package net.catting.android.data.structs;

import android.os.Parcel;
import android.os.Parcelable;

public class UserPublic  implements Parcelable {
    public User user;
    public Badge badge;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.badge, flags);
    }

    public UserPublic() {
    }

    protected UserPublic(Parcel in) {
        this.user = in.readParcelable(User.class.getClassLoader());
        this.badge = in.readParcelable(Badge.class.getClassLoader());
    }

    public static final Creator<UserPublic> CREATOR = new Creator<UserPublic>() {
        @Override
        public UserPublic createFromParcel(Parcel source) {
            return new UserPublic(source);
        }

        @Override
        public UserPublic[] newArray(int size) {
            return new UserPublic[size];
        }
    };
}
