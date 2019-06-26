// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.objects;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class ParcelableContainer implements Parcelable
{
    public static final Parcelable$Creator<ParcelableContainer> CREATOR;
    private byte[] mData;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<ParcelableContainer>() {
            public ParcelableContainer createFromParcel(final Parcel parcel) {
                return new ParcelableContainer(parcel, null);
            }
            
            public ParcelableContainer[] newArray(final int n) {
                return new ParcelableContainer[n];
            }
        };
    }
    
    private ParcelableContainer(final Parcel parcel) {
        this.readFromParcel(parcel);
    }
    
    public ParcelableContainer(final byte[] mData) {
        if (mData == null) {
            throw new IllegalArgumentException("'data' cannot 'null'");
        }
        this.mData = mData;
    }
    
    private void readFromParcel(final Parcel parcel) {
        parcel.readByteArray(this.mData = new byte[parcel.readInt()]);
    }
    
    public int describeContents() {
        return 0;
    }
    
    public byte[] getData() {
        return this.mData;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.mData.length);
        parcel.writeByteArray(this.mData);
    }
}
