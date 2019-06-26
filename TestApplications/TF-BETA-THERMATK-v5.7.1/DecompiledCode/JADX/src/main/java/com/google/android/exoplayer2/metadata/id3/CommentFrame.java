package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.Util;

public final class CommentFrame extends Id3Frame {
    public static final Creator<CommentFrame> CREATOR = new C01801();
    public final String description;
    public final String language;
    public final String text;

    /* renamed from: com.google.android.exoplayer2.metadata.id3.CommentFrame$1 */
    static class C01801 implements Creator<CommentFrame> {
        C01801() {
        }

        public CommentFrame createFromParcel(Parcel parcel) {
            return new CommentFrame(parcel);
        }

        public CommentFrame[] newArray(int i) {
            return new CommentFrame[i];
        }
    }

    public CommentFrame(String str, String str2, String str3) {
        super("COMM");
        this.language = str;
        this.description = str2;
        this.text = str3;
    }

    CommentFrame(Parcel parcel) {
        super("COMM");
        String readString = parcel.readString();
        Util.castNonNull(readString);
        this.language = readString;
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
        if (obj == null || CommentFrame.class != obj.getClass()) {
            return false;
        }
        CommentFrame commentFrame = (CommentFrame) obj;
        if (!(Util.areEqual(this.description, commentFrame.description) && Util.areEqual(this.language, commentFrame.language) && Util.areEqual(this.text, commentFrame.text))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        String str = this.language;
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
        stringBuilder.append(": language=");
        stringBuilder.append(this.language);
        stringBuilder.append(", description=");
        stringBuilder.append(this.description);
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f622id);
        parcel.writeString(this.language);
        parcel.writeString(this.text);
    }
}
