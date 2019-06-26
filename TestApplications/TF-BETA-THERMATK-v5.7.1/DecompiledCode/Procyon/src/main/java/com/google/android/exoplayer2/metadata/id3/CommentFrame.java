// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.id3;

import com.google.android.exoplayer2.util.Util;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public final class CommentFrame extends Id3Frame
{
    public static final Parcelable$Creator<CommentFrame> CREATOR;
    public final String description;
    public final String language;
    public final String text;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<CommentFrame>() {
            public CommentFrame createFromParcel(final Parcel parcel) {
                return new CommentFrame(parcel);
            }
            
            public CommentFrame[] newArray(final int n) {
                return new CommentFrame[n];
            }
        };
    }
    
    CommentFrame(final Parcel parcel) {
        super("COMM");
        final String string = parcel.readString();
        Util.castNonNull(string);
        this.language = string;
        final String string2 = parcel.readString();
        Util.castNonNull(string2);
        this.description = string2;
        final String string3 = parcel.readString();
        Util.castNonNull(string3);
        this.text = string3;
    }
    
    public CommentFrame(final String language, final String description, final String text) {
        super("COMM");
        this.language = language;
        this.description = description;
        this.text = text;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && CommentFrame.class == o.getClass()) {
            final CommentFrame commentFrame = (CommentFrame)o;
            if (!Util.areEqual(this.description, commentFrame.description) || !Util.areEqual(this.language, commentFrame.language) || !Util.areEqual(this.text, commentFrame.text)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final String language = this.language;
        int hashCode = 0;
        int hashCode2;
        if (language != null) {
            hashCode2 = language.hashCode();
        }
        else {
            hashCode2 = 0;
        }
        final String description = this.description;
        int hashCode3;
        if (description != null) {
            hashCode3 = description.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        final String text = this.text;
        if (text != null) {
            hashCode = text.hashCode();
        }
        return ((527 + hashCode2) * 31 + hashCode3) * 31 + hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.id);
        sb.append(": language=");
        sb.append(this.language);
        sb.append(", description=");
        sb.append(this.description);
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(super.id);
        parcel.writeString(this.language);
        parcel.writeString(this.text);
    }
}
