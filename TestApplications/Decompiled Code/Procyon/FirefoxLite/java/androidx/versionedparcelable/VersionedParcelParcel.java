// 
// Decompiled by Procyon v0.5.34
// 

package androidx.versionedparcelable;

import android.os.Parcelable;
import android.util.SparseIntArray;
import android.os.Parcel;

class VersionedParcelParcel extends VersionedParcel
{
    private int mCurrentField;
    private final int mEnd;
    private int mNextRead;
    private final int mOffset;
    private final Parcel mParcel;
    private final SparseIntArray mPositionLookup;
    private final String mPrefix;
    
    VersionedParcelParcel(final Parcel parcel) {
        this(parcel, parcel.dataPosition(), parcel.dataSize(), "");
    }
    
    VersionedParcelParcel(final Parcel mParcel, final int mOffset, final int mEnd, final String mPrefix) {
        this.mPositionLookup = new SparseIntArray();
        this.mCurrentField = -1;
        this.mNextRead = 0;
        this.mParcel = mParcel;
        this.mOffset = mOffset;
        this.mEnd = mEnd;
        this.mNextRead = this.mOffset;
        this.mPrefix = mPrefix;
    }
    
    private int readUntilField(final int n) {
        while (this.mNextRead < this.mEnd) {
            this.mParcel.setDataPosition(this.mNextRead);
            final int int1 = this.mParcel.readInt();
            final int int2 = this.mParcel.readInt();
            this.mNextRead += int1;
            if (int2 == n) {
                return this.mParcel.dataPosition();
            }
        }
        return -1;
    }
    
    public void closeField() {
        if (this.mCurrentField >= 0) {
            final int value = this.mPositionLookup.get(this.mCurrentField);
            final int dataPosition = this.mParcel.dataPosition();
            this.mParcel.setDataPosition(value);
            this.mParcel.writeInt(dataPosition - value);
            this.mParcel.setDataPosition(dataPosition);
        }
    }
    
    @Override
    protected VersionedParcel createSubParcel() {
        final Parcel mParcel = this.mParcel;
        final int dataPosition = this.mParcel.dataPosition();
        int n;
        if (this.mNextRead == this.mOffset) {
            n = this.mEnd;
        }
        else {
            n = this.mNextRead;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mPrefix);
        sb.append("  ");
        return new VersionedParcelParcel(mParcel, dataPosition, n, sb.toString());
    }
    
    public byte[] readByteArray() {
        final int int1 = this.mParcel.readInt();
        if (int1 < 0) {
            return null;
        }
        final byte[] array = new byte[int1];
        this.mParcel.readByteArray(array);
        return array;
    }
    
    public boolean readField(int untilField) {
        untilField = this.readUntilField(untilField);
        if (untilField == -1) {
            return false;
        }
        this.mParcel.setDataPosition(untilField);
        return true;
    }
    
    public int readInt() {
        return this.mParcel.readInt();
    }
    
    public <T extends Parcelable> T readParcelable() {
        return (T)this.mParcel.readParcelable(this.getClass().getClassLoader());
    }
    
    public String readString() {
        return this.mParcel.readString();
    }
    
    public void setOutputField(final int mCurrentField) {
        this.closeField();
        this.mCurrentField = mCurrentField;
        this.mPositionLookup.put(mCurrentField, this.mParcel.dataPosition());
        this.writeInt(0);
        this.writeInt(mCurrentField);
    }
    
    public void writeByteArray(final byte[] array) {
        if (array != null) {
            this.mParcel.writeInt(array.length);
            this.mParcel.writeByteArray(array);
        }
        else {
            this.mParcel.writeInt(-1);
        }
    }
    
    public void writeInt(final int n) {
        this.mParcel.writeInt(n);
    }
    
    public void writeParcelable(final Parcelable parcelable) {
        this.mParcel.writeParcelable(parcelable, 0);
    }
    
    public void writeString(final String s) {
        this.mParcel.writeString(s);
    }
}
