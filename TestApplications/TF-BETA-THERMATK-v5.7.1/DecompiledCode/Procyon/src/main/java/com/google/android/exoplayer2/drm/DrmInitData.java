// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.C;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;
import java.util.Comparator;

public final class DrmInitData implements Comparator<SchemeData>, Parcelable
{
    public static final Parcelable$Creator<DrmInitData> CREATOR;
    private int hashCode;
    public final int schemeDataCount;
    private final SchemeData[] schemeDatas;
    public final String schemeType;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<DrmInitData>() {
            public DrmInitData createFromParcel(final Parcel parcel) {
                return new DrmInitData(parcel);
            }
            
            public DrmInitData[] newArray(final int n) {
                return new DrmInitData[n];
            }
        };
    }
    
    DrmInitData(final Parcel parcel) {
        this.schemeType = parcel.readString();
        this.schemeDatas = (SchemeData[])parcel.createTypedArray((Parcelable$Creator)SchemeData.CREATOR);
        this.schemeDataCount = this.schemeDatas.length;
    }
    
    public DrmInitData(final String s, final List<SchemeData> list) {
        this(s, false, (SchemeData[])list.toArray(new SchemeData[0]));
    }
    
    private DrmInitData(final String schemeType, final boolean b, final SchemeData... array) {
        this.schemeType = schemeType;
        SchemeData[] schemeDatas = array;
        if (b) {
            schemeDatas = array.clone();
        }
        this.schemeDatas = schemeDatas;
        this.schemeDataCount = schemeDatas.length;
        Arrays.sort(this.schemeDatas, this);
    }
    
    public DrmInitData(final String s, final SchemeData... array) {
        this(s, true, array);
    }
    
    public DrmInitData(final List<SchemeData> list) {
        this(null, false, (SchemeData[])list.toArray(new SchemeData[0]));
    }
    
    public DrmInitData(final SchemeData... array) {
        this((String)null, array);
    }
    
    private static boolean containsSchemeDataWithUuid(final ArrayList<SchemeData> list, final int n, final UUID obj) {
        for (int i = 0; i < n; ++i) {
            if (list.get(i).uuid.equals(obj)) {
                return true;
            }
        }
        return false;
    }
    
    public static DrmInitData createSessionCreationData(final DrmInitData drmInitData, final DrmInitData drmInitData2) {
        final ArrayList<SchemeData> list = new ArrayList<SchemeData>();
        final int n = 0;
        final DrmInitData drmInitData3 = null;
        String s;
        if (drmInitData != null) {
            final String schemeType = drmInitData.schemeType;
            final SchemeData[] schemeDatas = drmInitData.schemeDatas;
            final int length = schemeDatas.length;
            int n2 = 0;
            while (true) {
                s = schemeType;
                if (n2 >= length) {
                    break;
                }
                final SchemeData e = schemeDatas[n2];
                if (e.hasData()) {
                    list.add(e);
                }
                ++n2;
            }
        }
        else {
            s = null;
        }
        String s2 = s;
        if (drmInitData2 != null) {
            String schemeType2;
            if ((schemeType2 = s) == null) {
                schemeType2 = drmInitData2.schemeType;
            }
            final int size = list.size();
            final SchemeData[] schemeDatas2 = drmInitData2.schemeDatas;
            final int length2 = schemeDatas2.length;
            int n3 = n;
            while (true) {
                s2 = schemeType2;
                if (n3 >= length2) {
                    break;
                }
                final SchemeData e2 = schemeDatas2[n3];
                if (e2.hasData() && !containsSchemeDataWithUuid(list, size, e2.uuid)) {
                    list.add(e2);
                }
                ++n3;
            }
        }
        DrmInitData drmInitData4;
        if (list.isEmpty()) {
            drmInitData4 = drmInitData3;
        }
        else {
            drmInitData4 = new DrmInitData(s2, list);
        }
        return drmInitData4;
    }
    
    @Override
    public int compare(final SchemeData schemeData, final SchemeData schemeData2) {
        int compareTo;
        if (C.UUID_NIL.equals(schemeData.uuid)) {
            if (C.UUID_NIL.equals(schemeData2.uuid)) {
                compareTo = 0;
            }
            else {
                compareTo = 1;
            }
        }
        else {
            compareTo = schemeData.uuid.compareTo(schemeData2.uuid);
        }
        return compareTo;
    }
    
    public DrmInitData copyWithSchemeType(final String s) {
        if (Util.areEqual(this.schemeType, s)) {
            return this;
        }
        return new DrmInitData(s, false, this.schemeDatas);
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
        if (o != null && DrmInitData.class == o.getClass()) {
            final DrmInitData drmInitData = (DrmInitData)o;
            if (!Util.areEqual(this.schemeType, drmInitData.schemeType) || !Arrays.equals(this.schemeDatas, drmInitData.schemeDatas)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    public SchemeData get(final int n) {
        return this.schemeDatas[n];
    }
    
    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            final String schemeType = this.schemeType;
            int hashCode;
            if (schemeType == null) {
                hashCode = 0;
            }
            else {
                hashCode = schemeType.hashCode();
            }
            this.hashCode = hashCode * 31 + Arrays.hashCode(this.schemeDatas);
        }
        return this.hashCode;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.schemeType);
        parcel.writeTypedArray((Parcelable[])this.schemeDatas, 0);
    }
    
    public static final class SchemeData implements Parcelable
    {
        public static final Parcelable$Creator<SchemeData> CREATOR;
        public final byte[] data;
        private int hashCode;
        public final String licenseServerUrl;
        public final String mimeType;
        public final boolean requiresSecureDecryption;
        private final UUID uuid;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<SchemeData>() {
                public SchemeData createFromParcel(final Parcel parcel) {
                    return new SchemeData(parcel);
                }
                
                public SchemeData[] newArray(final int n) {
                    return new SchemeData[n];
                }
            };
        }
        
        SchemeData(final Parcel parcel) {
            this.uuid = new UUID(parcel.readLong(), parcel.readLong());
            this.licenseServerUrl = parcel.readString();
            final String string = parcel.readString();
            Util.castNonNull(string);
            this.mimeType = string;
            this.data = parcel.createByteArray();
            this.requiresSecureDecryption = (parcel.readByte() != 0);
        }
        
        public SchemeData(final UUID uuid, final String licenseServerUrl, final String s, final byte[] data, final boolean requiresSecureDecryption) {
            Assertions.checkNotNull(uuid);
            this.uuid = uuid;
            this.licenseServerUrl = licenseServerUrl;
            Assertions.checkNotNull(s);
            this.mimeType = s;
            this.data = data;
            this.requiresSecureDecryption = requiresSecureDecryption;
        }
        
        public SchemeData(final UUID uuid, final String s, final byte[] array) {
            this(uuid, s, array, false);
        }
        
        public SchemeData(final UUID uuid, final String s, final byte[] array, final boolean b) {
            this(uuid, null, s, array, b);
        }
        
        public boolean canReplace(final SchemeData schemeData) {
            return this.hasData() && !schemeData.hasData() && this.matches(schemeData.uuid);
        }
        
        public SchemeData copyWithData(final byte[] array) {
            return new SchemeData(this.uuid, this.licenseServerUrl, this.mimeType, array, this.requiresSecureDecryption);
        }
        
        public int describeContents() {
            return 0;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof SchemeData)) {
                return false;
            }
            boolean b = true;
            if (o == this) {
                return true;
            }
            final SchemeData schemeData = (SchemeData)o;
            if (!Util.areEqual(this.licenseServerUrl, schemeData.licenseServerUrl) || !Util.areEqual(this.mimeType, schemeData.mimeType) || !Util.areEqual(this.uuid, schemeData.uuid) || !Arrays.equals(this.data, schemeData.data)) {
                b = false;
            }
            return b;
        }
        
        public boolean hasData() {
            return this.data != null;
        }
        
        @Override
        public int hashCode() {
            if (this.hashCode == 0) {
                final int hashCode = this.uuid.hashCode();
                final String licenseServerUrl = this.licenseServerUrl;
                int hashCode2;
                if (licenseServerUrl == null) {
                    hashCode2 = 0;
                }
                else {
                    hashCode2 = licenseServerUrl.hashCode();
                }
                this.hashCode = ((hashCode * 31 + hashCode2) * 31 + this.mimeType.hashCode()) * 31 + Arrays.hashCode(this.data);
            }
            return this.hashCode;
        }
        
        public boolean matches(final UUID uuid) {
            return C.UUID_NIL.equals(this.uuid) || uuid.equals(this.uuid);
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeLong(this.uuid.getMostSignificantBits());
            parcel.writeLong(this.uuid.getLeastSignificantBits());
            parcel.writeString(this.licenseServerUrl);
            parcel.writeString(this.mimeType);
            parcel.writeByteArray(this.data);
            parcel.writeByte((byte)(byte)(this.requiresSecureDecryption ? 1 : 0));
        }
    }
}
