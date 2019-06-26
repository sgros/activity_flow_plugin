// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import android.os.ParcelFormatException;
import com.github.mikephil.charting.utils.Utils;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class Entry extends BaseEntry implements Parcelable
{
    public static final Parcelable$Creator<Entry> CREATOR;
    private float x;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<Entry>() {
            public Entry createFromParcel(final Parcel parcel) {
                return new Entry(parcel);
            }
            
            public Entry[] newArray(final int n) {
                return new Entry[n];
            }
        };
    }
    
    public Entry() {
        this.x = 0.0f;
    }
    
    public Entry(final float x, final float n) {
        super(n);
        this.x = 0.0f;
        this.x = x;
    }
    
    public Entry(final float x, final float n, final Drawable drawable) {
        super(n, drawable);
        this.x = 0.0f;
        this.x = x;
    }
    
    public Entry(final float x, final float n, final Drawable drawable, final Object o) {
        super(n, drawable, o);
        this.x = 0.0f;
        this.x = x;
    }
    
    public Entry(final float x, final float n, final Object o) {
        super(n, o);
        this.x = 0.0f;
        this.x = x;
    }
    
    protected Entry(final Parcel parcel) {
        this.x = 0.0f;
        this.x = parcel.readFloat();
        this.setY(parcel.readFloat());
        if (parcel.readInt() == 1) {
            this.setData(parcel.readParcelable(Object.class.getClassLoader()));
        }
    }
    
    public Entry copy() {
        return new Entry(this.x, this.getY(), this.getData());
    }
    
    public int describeContents() {
        return 0;
    }
    
    public boolean equalTo(final Entry entry) {
        return entry != null && entry.getData() == this.getData() && Math.abs(entry.x - this.x) <= Utils.FLOAT_EPSILON && Math.abs(entry.getY() - this.getY()) <= Utils.FLOAT_EPSILON;
    }
    
    public float getX() {
        return this.x;
    }
    
    public void setX(final float x) {
        this.x = x;
    }
    
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Entry, x: ");
        sb.append(this.x);
        sb.append(" y: ");
        sb.append(this.getY());
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeFloat(this.x);
        parcel.writeFloat(this.getY());
        if (this.getData() != null) {
            if (!(this.getData() instanceof Parcelable)) {
                throw new ParcelFormatException("Cannot parcel an Entry with non-parcelable data");
            }
            parcel.writeInt(1);
            parcel.writeParcelable((Parcelable)this.getData(), n);
        }
        else {
            parcel.writeInt(0);
        }
    }
}
