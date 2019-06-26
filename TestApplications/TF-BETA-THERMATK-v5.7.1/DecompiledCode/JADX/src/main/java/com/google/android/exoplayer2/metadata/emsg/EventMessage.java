package com.google.android.exoplayer2.metadata.emsg;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.metadata.Metadata.Entry;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

public final class EventMessage implements Entry {
    public static final Creator<EventMessage> CREATOR = new C01731();
    public final long durationMs;
    private int hashCode;
    /* renamed from: id */
    public final long f621id;
    public final byte[] messageData;
    public final long presentationTimeUs;
    public final String schemeIdUri;
    public final String value;

    /* renamed from: com.google.android.exoplayer2.metadata.emsg.EventMessage$1 */
    static class C01731 implements Creator<EventMessage> {
        C01731() {
        }

        public EventMessage createFromParcel(Parcel parcel) {
            return new EventMessage(parcel);
        }

        public EventMessage[] newArray(int i) {
            return new EventMessage[i];
        }
    }

    public int describeContents() {
        return 0;
    }

    public EventMessage(String str, String str2, long j, long j2, byte[] bArr, long j3) {
        this.schemeIdUri = str;
        this.value = str2;
        this.durationMs = j;
        this.f621id = j2;
        this.messageData = bArr;
        this.presentationTimeUs = j3;
    }

    EventMessage(Parcel parcel) {
        String readString = parcel.readString();
        Util.castNonNull(readString);
        this.schemeIdUri = readString;
        readString = parcel.readString();
        Util.castNonNull(readString);
        this.value = readString;
        this.presentationTimeUs = parcel.readLong();
        this.durationMs = parcel.readLong();
        this.f621id = parcel.readLong();
        byte[] createByteArray = parcel.createByteArray();
        Util.castNonNull(createByteArray);
        this.messageData = createByteArray;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            String str = this.schemeIdUri;
            int i = 0;
            int hashCode = (527 + (str != null ? str.hashCode() : 0)) * 31;
            str = this.value;
            if (str != null) {
                i = str.hashCode();
            }
            hashCode = (hashCode + i) * 31;
            long j = this.presentationTimeUs;
            hashCode = (hashCode + ((int) (j ^ (j >>> 32)))) * 31;
            j = this.durationMs;
            hashCode = (hashCode + ((int) (j ^ (j >>> 32)))) * 31;
            j = this.f621id;
            this.hashCode = ((hashCode + ((int) (j ^ (j >>> 32)))) * 31) + Arrays.hashCode(this.messageData);
        }
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || EventMessage.class != obj.getClass()) {
            return false;
        }
        EventMessage eventMessage = (EventMessage) obj;
        if (!(this.presentationTimeUs == eventMessage.presentationTimeUs && this.durationMs == eventMessage.durationMs && this.f621id == eventMessage.f621id && Util.areEqual(this.schemeIdUri, eventMessage.schemeIdUri) && Util.areEqual(this.value, eventMessage.value) && Arrays.equals(this.messageData, eventMessage.messageData))) {
            z = false;
        }
        return z;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("EMSG: scheme=");
        stringBuilder.append(this.schemeIdUri);
        stringBuilder.append(", id=");
        stringBuilder.append(this.f621id);
        stringBuilder.append(", value=");
        stringBuilder.append(this.value);
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.schemeIdUri);
        parcel.writeString(this.value);
        parcel.writeLong(this.presentationTimeUs);
        parcel.writeLong(this.durationMs);
        parcel.writeLong(this.f621id);
        parcel.writeByteArray(this.messageData);
    }
}
