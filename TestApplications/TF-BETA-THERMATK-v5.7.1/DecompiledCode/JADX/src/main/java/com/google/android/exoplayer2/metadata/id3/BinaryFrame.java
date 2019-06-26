package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

public final class BinaryFrame extends Id3Frame {
    public static final Creator<BinaryFrame> CREATOR = new C01771();
    public final byte[] data;

    /* renamed from: com.google.android.exoplayer2.metadata.id3.BinaryFrame$1 */
    static class C01771 implements Creator<BinaryFrame> {
        C01771() {
        }

        public BinaryFrame createFromParcel(Parcel parcel) {
            return new BinaryFrame(parcel);
        }

        public BinaryFrame[] newArray(int i) {
            return new BinaryFrame[i];
        }
    }

    public BinaryFrame(String str, byte[] bArr) {
        super(str);
        this.data = bArr;
    }

    BinaryFrame(Parcel parcel) {
        String readString = parcel.readString();
        Util.castNonNull(readString);
        super(readString);
        byte[] createByteArray = parcel.createByteArray();
        Util.castNonNull(createByteArray);
        this.data = createByteArray;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || BinaryFrame.class != obj.getClass()) {
            return false;
        }
        BinaryFrame binaryFrame = (BinaryFrame) obj;
        if (!(this.f622id.equals(binaryFrame.f622id) && Arrays.equals(this.data, binaryFrame.data))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return ((527 + this.f622id.hashCode()) * 31) + Arrays.hashCode(this.data);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f622id);
        parcel.writeByteArray(this.data);
    }
}
