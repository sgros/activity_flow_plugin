package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.Util;

public final class InternalFrame extends Id3Frame {
    public static final Creator<InternalFrame> CREATOR = new C01821();
    public final String description;
    public final String domain;
    public final String text;

    /* renamed from: com.google.android.exoplayer2.metadata.id3.InternalFrame$1 */
    static class C01821 implements Creator<InternalFrame> {
        C01821() {
        }

        public InternalFrame createFromParcel(Parcel parcel) {
            return new InternalFrame(parcel);
        }

        public InternalFrame[] newArray(int i) {
            return new InternalFrame[i];
        }
    }

    public InternalFrame(String str, String str2, String str3) {
        super("----");
        this.domain = str;
        this.description = str2;
        this.text = str3;
    }

    InternalFrame(Parcel parcel) {
        super("----");
        String readString = parcel.readString();
        Util.castNonNull(readString);
        this.domain = readString;
        readString = parcel.readString();
        Util.castNonNull(readString);
        this.description = readString;
        String readString2 = parcel.readString();
        Util.castNonNull(readString2);
        this.text = readString2;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || InternalFrame.class != obj.getClass()) {
            return false;
        }
        InternalFrame internalFrame = (InternalFrame) obj;
        if (!(Util.areEqual(this.description, internalFrame.description) && Util.areEqual(this.domain, internalFrame.domain) && Util.areEqual(this.text, internalFrame.text))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        String str = this.domain;
        int i = 0;
        int hashCode = (527 + (str != null ? str.hashCode() : 0)) * 31;
        str = this.description;
        hashCode = (hashCode + (str != null ? str.hashCode() : 0)) * 31;
        str = this.text;
        if (str != null) {
            i = str.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.f622id);
        stringBuilder.append(": domain=");
        stringBuilder.append(this.domain);
        stringBuilder.append(", description=");
        stringBuilder.append(this.description);
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f622id);
        parcel.writeString(this.domain);
        parcel.writeString(this.text);
    }
}
