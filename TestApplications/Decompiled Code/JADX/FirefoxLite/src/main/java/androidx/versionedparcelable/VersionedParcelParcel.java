package androidx.versionedparcelable;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;

class VersionedParcelParcel extends VersionedParcel {
    private int mCurrentField;
    private final int mEnd;
    private int mNextRead;
    private final int mOffset;
    private final Parcel mParcel;
    private final SparseIntArray mPositionLookup;
    private final String mPrefix;

    VersionedParcelParcel(Parcel parcel) {
        this(parcel, parcel.dataPosition(), parcel.dataSize(), "");
    }

    VersionedParcelParcel(Parcel parcel, int i, int i2, String str) {
        this.mPositionLookup = new SparseIntArray();
        this.mCurrentField = -1;
        this.mNextRead = 0;
        this.mParcel = parcel;
        this.mOffset = i;
        this.mEnd = i2;
        this.mNextRead = this.mOffset;
        this.mPrefix = str;
    }

    private int readUntilField(int i) {
        while (this.mNextRead < this.mEnd) {
            this.mParcel.setDataPosition(this.mNextRead);
            int readInt = this.mParcel.readInt();
            int readInt2 = this.mParcel.readInt();
            this.mNextRead += readInt;
            if (readInt2 == i) {
                return this.mParcel.dataPosition();
            }
        }
        return -1;
    }

    public boolean readField(int i) {
        i = readUntilField(i);
        if (i == -1) {
            return false;
        }
        this.mParcel.setDataPosition(i);
        return true;
    }

    public void setOutputField(int i) {
        closeField();
        this.mCurrentField = i;
        this.mPositionLookup.put(i, this.mParcel.dataPosition());
        writeInt(0);
        writeInt(i);
    }

    public void closeField() {
        if (this.mCurrentField >= 0) {
            int i = this.mPositionLookup.get(this.mCurrentField);
            int dataPosition = this.mParcel.dataPosition();
            int i2 = dataPosition - i;
            this.mParcel.setDataPosition(i);
            this.mParcel.writeInt(i2);
            this.mParcel.setDataPosition(dataPosition);
        }
    }

    /* Access modifiers changed, original: protected */
    public VersionedParcel createSubParcel() {
        Parcel parcel = this.mParcel;
        int dataPosition = this.mParcel.dataPosition();
        int i = this.mNextRead == this.mOffset ? this.mEnd : this.mNextRead;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mPrefix);
        stringBuilder.append("  ");
        return new VersionedParcelParcel(parcel, dataPosition, i, stringBuilder.toString());
    }

    public void writeByteArray(byte[] bArr) {
        if (bArr != null) {
            this.mParcel.writeInt(bArr.length);
            this.mParcel.writeByteArray(bArr);
            return;
        }
        this.mParcel.writeInt(-1);
    }

    public void writeInt(int i) {
        this.mParcel.writeInt(i);
    }

    public void writeString(String str) {
        this.mParcel.writeString(str);
    }

    public void writeParcelable(Parcelable parcelable) {
        this.mParcel.writeParcelable(parcelable, 0);
    }

    public int readInt() {
        return this.mParcel.readInt();
    }

    public String readString() {
        return this.mParcel.readString();
    }

    public byte[] readByteArray() {
        int readInt = this.mParcel.readInt();
        if (readInt < 0) {
            return null;
        }
        byte[] bArr = new byte[readInt];
        this.mParcel.readByteArray(bArr);
        return bArr;
    }

    public <T extends Parcelable> T readParcelable() {
        return this.mParcel.readParcelable(getClass().getClassLoader());
    }
}
