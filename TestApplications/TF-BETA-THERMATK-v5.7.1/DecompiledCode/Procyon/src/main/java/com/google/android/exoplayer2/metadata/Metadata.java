// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata;

import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public final class Metadata implements Parcelable
{
    public static final Parcelable$Creator<Metadata> CREATOR;
    private final Entry[] entries;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<Metadata>() {
            public Metadata createFromParcel(final Parcel parcel) {
                return new Metadata(parcel);
            }
            
            public Metadata[] newArray(final int n) {
                return new Metadata[0];
            }
        };
    }
    
    Metadata(final Parcel parcel) {
        this.entries = new Entry[parcel.readInt()];
        int n = 0;
        while (true) {
            final Entry[] entries = this.entries;
            if (n >= entries.length) {
                break;
            }
            entries[n] = (Entry)parcel.readParcelable(Entry.class.getClassLoader());
            ++n;
        }
    }
    
    public Metadata(final List<? extends Entry> list) {
        if (list != null) {
            list.toArray(this.entries = new Entry[list.size()]);
        }
        else {
            this.entries = new Entry[0];
        }
    }
    
    public Metadata(final Entry... array) {
        Entry[] entries = array;
        if (array == null) {
            entries = new Entry[0];
        }
        this.entries = entries;
    }
    
    public Metadata copyWithAppendedEntries(final Entry... array) {
        final Entry[] entries = this.entries;
        final Entry[] array2 = Arrays.copyOf(entries, entries.length + array.length);
        System.arraycopy(array, 0, array2, this.entries.length, array.length);
        Util.castNonNullTypeArray(array2);
        return new Metadata((Entry[])array2);
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && Metadata.class == o.getClass() && Arrays.equals(this.entries, ((Metadata)o).entries));
    }
    
    public Entry get(final int n) {
        return this.entries[n];
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.entries);
    }
    
    public int length() {
        return this.entries.length;
    }
    
    public void writeToParcel(final Parcel parcel, int i) {
        parcel.writeInt(this.entries.length);
        final Entry[] entries = this.entries;
        int length;
        for (length = entries.length, i = 0; i < length; ++i) {
            parcel.writeParcelable((Parcelable)entries[i], 0);
        }
    }
    
    public interface Entry extends Parcelable
    {
    }
}
