// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.id3;

import com.google.android.exoplayer2.util.Util;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public final class TextInformationFrame extends Id3Frame
{
    public static final Parcelable$Creator<TextInformationFrame> CREATOR;
    public final String description;
    public final String value;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<TextInformationFrame>() {
            public TextInformationFrame createFromParcel(final Parcel parcel) {
                return new TextInformationFrame(parcel);
            }
            
            public TextInformationFrame[] newArray(final int n) {
                return new TextInformationFrame[n];
            }
        };
    }
    
    TextInformationFrame(final Parcel parcel) {
        final String string = parcel.readString();
        Util.castNonNull(string);
        super(string);
        this.description = parcel.readString();
        final String string2 = parcel.readString();
        Util.castNonNull(string2);
        this.value = string2;
    }
    
    public TextInformationFrame(final String s, final String description, final String value) {
        super(s);
        this.description = description;
        this.value = value;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && TextInformationFrame.class == o.getClass()) {
            final TextInformationFrame textInformationFrame = (TextInformationFrame)o;
            if (!super.id.equals(textInformationFrame.id) || !Util.areEqual(this.description, textInformationFrame.description) || !Util.areEqual(this.value, textInformationFrame.value)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final int hashCode = super.id.hashCode();
        final String description = this.description;
        int hashCode2 = 0;
        int hashCode3;
        if (description != null) {
            hashCode3 = description.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        final String value = this.value;
        if (value != null) {
            hashCode2 = value.hashCode();
        }
        return ((527 + hashCode) * 31 + hashCode3) * 31 + hashCode2;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.id);
        sb.append(": value=");
        sb.append(this.value);
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(super.id);
        parcel.writeString(this.description);
        parcel.writeString(this.value);
    }
}
