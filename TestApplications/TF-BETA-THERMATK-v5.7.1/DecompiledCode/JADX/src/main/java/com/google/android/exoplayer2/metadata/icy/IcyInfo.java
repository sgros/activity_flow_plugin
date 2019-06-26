package com.google.android.exoplayer2.metadata.icy;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.metadata.Metadata.Entry;
import com.google.android.exoplayer2.util.Util;

public final class IcyInfo implements Entry {
    public static final Creator<IcyInfo> CREATOR = new C01751();
    public final String title;
    public final String url;

    /* renamed from: com.google.android.exoplayer2.metadata.icy.IcyInfo$1 */
    static class C01751 implements Creator<IcyInfo> {
        C01751() {
        }

        public IcyInfo createFromParcel(Parcel parcel) {
            return new IcyInfo(parcel);
        }

        public IcyInfo[] newArray(int i) {
            return new IcyInfo[i];
        }
    }

    public int describeContents() {
        return 0;
    }

    public IcyInfo(String str, String str2) {
        this.title = str;
        this.url = str2;
    }

    IcyInfo(Parcel parcel) {
        this.title = parcel.readString();
        this.url = parcel.readString();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || IcyInfo.class != obj.getClass()) {
            return false;
        }
        IcyInfo icyInfo = (IcyInfo) obj;
        if (!(Util.areEqual(this.title, icyInfo.title) && Util.areEqual(this.url, icyInfo.url))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        String str = this.title;
        int i = 0;
        int hashCode = (527 + (str != null ? str.hashCode() : 0)) * 31;
        str = this.url;
        if (str != null) {
            i = str.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ICY: title=\"");
        stringBuilder.append(this.title);
        stringBuilder.append("\", url=\"");
        stringBuilder.append(this.url);
        stringBuilder.append("\"");
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.url);
    }
}
