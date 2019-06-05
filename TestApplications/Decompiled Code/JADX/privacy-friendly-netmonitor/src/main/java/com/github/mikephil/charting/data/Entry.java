package com.github.mikephil.charting.data;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.github.mikephil.charting.utils.Utils;

public class Entry extends BaseEntry implements Parcelable {
    public static final Creator<Entry> CREATOR = new C04301();
    /* renamed from: x */
    private float f485x = 0.0f;

    /* renamed from: com.github.mikephil.charting.data.Entry$1 */
    static class C04301 implements Creator<Entry> {
        C04301() {
        }

        public Entry createFromParcel(Parcel parcel) {
            return new Entry(parcel);
        }

        public Entry[] newArray(int i) {
            return new Entry[i];
        }
    }

    public int describeContents() {
        return 0;
    }

    public Entry(float f, float f2) {
        super(f2);
        this.f485x = f;
    }

    public Entry(float f, float f2, Object obj) {
        super(f2, obj);
        this.f485x = f;
    }

    public Entry(float f, float f2, Drawable drawable) {
        super(f2, drawable);
        this.f485x = f;
    }

    public Entry(float f, float f2, Drawable drawable, Object obj) {
        super(f2, drawable, obj);
        this.f485x = f;
    }

    public float getX() {
        return this.f485x;
    }

    public void setX(float f) {
        this.f485x = f;
    }

    public Entry copy() {
        return new Entry(this.f485x, getY(), getData());
    }

    public boolean equalTo(Entry entry) {
        if (entry != null && entry.getData() == getData() && Math.abs(entry.f485x - this.f485x) <= Utils.FLOAT_EPSILON && Math.abs(entry.getY() - getY()) <= Utils.FLOAT_EPSILON) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Entry, x: ");
        stringBuilder.append(this.f485x);
        stringBuilder.append(" y: ");
        stringBuilder.append(getY());
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(this.f485x);
        parcel.writeFloat(getY());
        if (getData() == null) {
            parcel.writeInt(0);
        } else if (getData() instanceof Parcelable) {
            parcel.writeInt(1);
            parcel.writeParcelable((Parcelable) getData(), i);
        } else {
            throw new ParcelFormatException("Cannot parcel an Entry with non-parcelable data");
        }
    }

    protected Entry(Parcel parcel) {
        this.f485x = parcel.readFloat();
        setY(parcel.readFloat());
        if (parcel.readInt() == 1) {
            setData(parcel.readParcelable(Object.class.getClassLoader()));
        }
    }
}
