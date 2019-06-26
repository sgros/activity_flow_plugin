package com.google.android.exoplayer2.metadata.id3;

import com.google.android.exoplayer2.metadata.Metadata.Entry;

public abstract class Id3Frame implements Entry {
    /* renamed from: id */
    public final String f622id;

    public int describeContents() {
        return 0;
    }

    public Id3Frame(String str) {
        this.f622id = str;
    }

    public String toString() {
        return this.f622id;
    }
}
