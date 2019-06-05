package locus.api.android.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ParcelableContainer implements Parcelable {
    public static final Creator<ParcelableContainer> CREATOR = new C02441();
    private byte[] mData;

    /* renamed from: locus.api.android.objects.ParcelableContainer$1 */
    static class C02441 implements Creator<ParcelableContainer> {
        C02441() {
        }

        public ParcelableContainer createFromParcel(Parcel in) {
            return new ParcelableContainer(in, null);
        }

        public ParcelableContainer[] newArray(int size) {
            return new ParcelableContainer[size];
        }
    }

    /* synthetic */ ParcelableContainer(Parcel x0, C02441 x1) {
        this(x0);
    }

    public ParcelableContainer(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException("'data' cannot 'null'");
        }
        this.mData = data;
    }

    private ParcelableContainer(Parcel in) {
        readFromParcel(in);
    }

    public byte[] getData() {
        return this.mData;
    }

    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {
        this.mData = new byte[in.readInt()];
        in.readByteArray(this.mData);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mData.length);
        dest.writeByteArray(this.mData);
    }
}
