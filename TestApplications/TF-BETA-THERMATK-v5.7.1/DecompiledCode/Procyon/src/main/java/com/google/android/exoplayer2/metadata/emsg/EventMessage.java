// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.emsg;

import java.util.Arrays;
import com.google.android.exoplayer2.util.Util;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import com.google.android.exoplayer2.metadata.Metadata;

public final class EventMessage implements Entry
{
    public static final Parcelable$Creator<EventMessage> CREATOR;
    public final long durationMs;
    private int hashCode;
    public final long id;
    public final byte[] messageData;
    public final long presentationTimeUs;
    public final String schemeIdUri;
    public final String value;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<EventMessage>() {
            public EventMessage createFromParcel(final Parcel parcel) {
                return new EventMessage(parcel);
            }
            
            public EventMessage[] newArray(final int n) {
                return new EventMessage[n];
            }
        };
    }
    
    EventMessage(final Parcel parcel) {
        final String string = parcel.readString();
        Util.castNonNull(string);
        this.schemeIdUri = string;
        final String string2 = parcel.readString();
        Util.castNonNull(string2);
        this.value = string2;
        this.presentationTimeUs = parcel.readLong();
        this.durationMs = parcel.readLong();
        this.id = parcel.readLong();
        final byte[] byteArray = parcel.createByteArray();
        Util.castNonNull(byteArray);
        this.messageData = byteArray;
    }
    
    public EventMessage(final String schemeIdUri, final String value, final long durationMs, final long id, final byte[] messageData, final long presentationTimeUs) {
        this.schemeIdUri = schemeIdUri;
        this.value = value;
        this.durationMs = durationMs;
        this.id = id;
        this.messageData = messageData;
        this.presentationTimeUs = presentationTimeUs;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && EventMessage.class == o.getClass()) {
            final EventMessage eventMessage = (EventMessage)o;
            if (this.presentationTimeUs != eventMessage.presentationTimeUs || this.durationMs != eventMessage.durationMs || this.id != eventMessage.id || !Util.areEqual(this.schemeIdUri, eventMessage.schemeIdUri) || !Util.areEqual(this.value, eventMessage.value) || !Arrays.equals(this.messageData, eventMessage.messageData)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            final String schemeIdUri = this.schemeIdUri;
            int hashCode = 0;
            int hashCode2;
            if (schemeIdUri != null) {
                hashCode2 = schemeIdUri.hashCode();
            }
            else {
                hashCode2 = 0;
            }
            final String value = this.value;
            if (value != null) {
                hashCode = value.hashCode();
            }
            final long presentationTimeUs = this.presentationTimeUs;
            final int n = (int)(presentationTimeUs ^ presentationTimeUs >>> 32);
            final long durationMs = this.durationMs;
            final int n2 = (int)(durationMs ^ durationMs >>> 32);
            final long id = this.id;
            this.hashCode = (((((527 + hashCode2) * 31 + hashCode) * 31 + n) * 31 + n2) * 31 + (int)(id ^ id >>> 32)) * 31 + Arrays.hashCode(this.messageData);
        }
        return this.hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("EMSG: scheme=");
        sb.append(this.schemeIdUri);
        sb.append(", id=");
        sb.append(this.id);
        sb.append(", value=");
        sb.append(this.value);
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.schemeIdUri);
        parcel.writeString(this.value);
        parcel.writeLong(this.presentationTimeUs);
        parcel.writeLong(this.durationMs);
        parcel.writeLong(this.id);
        parcel.writeByteArray(this.messageData);
    }
}
