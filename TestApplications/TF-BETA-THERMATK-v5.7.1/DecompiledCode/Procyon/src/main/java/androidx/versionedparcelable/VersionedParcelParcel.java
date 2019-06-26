// 
// Decompiled by Procyon v0.5.34
// 

package androidx.versionedparcelable;

import android.os.Parcelable;
import android.text.TextUtils;
import java.lang.reflect.Method;
import androidx.collection.ArrayMap;
import android.util.SparseIntArray;
import android.os.Parcel;

class VersionedParcelParcel extends VersionedParcel
{
    private int mCurrentField;
    private final int mEnd;
    private int mFieldId;
    private int mNextRead;
    private final int mOffset;
    private final Parcel mParcel;
    private final SparseIntArray mPositionLookup;
    private final String mPrefix;
    
    VersionedParcelParcel(final Parcel parcel) {
        this(parcel, parcel.dataPosition(), parcel.dataSize(), "", new ArrayMap<String, Method>(), new ArrayMap<String, Method>(), new ArrayMap<String, Class>());
    }
    
    private VersionedParcelParcel(final Parcel mParcel, final int mOffset, final int mEnd, final String mPrefix, final ArrayMap<String, Method> arrayMap, final ArrayMap<String, Method> arrayMap2, final ArrayMap<String, Class> arrayMap3) {
        super(arrayMap, arrayMap2, arrayMap3);
        this.mPositionLookup = new SparseIntArray();
        this.mCurrentField = -1;
        this.mNextRead = 0;
        this.mFieldId = -1;
        this.mParcel = mParcel;
        this.mOffset = mOffset;
        this.mEnd = mEnd;
        this.mNextRead = this.mOffset;
        this.mPrefix = mPrefix;
    }
    
    public void closeField() {
        final int mCurrentField = this.mCurrentField;
        if (mCurrentField >= 0) {
            final int value = this.mPositionLookup.get(mCurrentField);
            final int dataPosition = this.mParcel.dataPosition();
            this.mParcel.setDataPosition(value);
            this.mParcel.writeInt(dataPosition - value);
            this.mParcel.setDataPosition(dataPosition);
        }
    }
    
    @Override
    protected VersionedParcel createSubParcel() {
        final Parcel mParcel = this.mParcel;
        final int dataPosition = mParcel.dataPosition();
        int n;
        if ((n = this.mNextRead) == this.mOffset) {
            n = this.mEnd;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mPrefix);
        sb.append("  ");
        return new VersionedParcelParcel(mParcel, dataPosition, n, sb.toString(), super.mReadCache, super.mWriteCache, super.mParcelizerCache);
    }
    
    public boolean readBoolean() {
        return this.mParcel.readInt() != 0;
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
    
    @Override
    protected CharSequence readCharSequence() {
        return (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(this.mParcel);
    }
    
    public boolean readField(final int i) {
        while (true) {
            final int mNextRead = this.mNextRead;
            final int mEnd = this.mEnd;
            boolean b = true;
            if (mNextRead >= mEnd) {
                if (this.mFieldId != i) {
                    b = false;
                }
                return b;
            }
            final int mFieldId = this.mFieldId;
            if (mFieldId == i) {
                return true;
            }
            if (String.valueOf(mFieldId).compareTo(String.valueOf(i)) > 0) {
                return false;
            }
            this.mParcel.setDataPosition(this.mNextRead);
            final int int1 = this.mParcel.readInt();
            this.mFieldId = this.mParcel.readInt();
            this.mNextRead += int1;
        }
    }
    
    public int readInt() {
        return this.mParcel.readInt();
    }
    
    public <T extends Parcelable> T readParcelable() {
        return (T)this.mParcel.readParcelable(VersionedParcelParcel.class.getClassLoader());
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
    
    public void writeBoolean(final boolean b) {
        this.mParcel.writeInt((int)(b ? 1 : 0));
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
    
    @Override
    protected void writeCharSequence(final CharSequence charSequence) {
        TextUtils.writeToParcel(charSequence, this.mParcel, 0);
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
