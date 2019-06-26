// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.id3;

import com.google.android.exoplayer2.metadata.Metadata;

public abstract class Id3Frame implements Entry
{
    public final String id;
    
    public Id3Frame(final String id) {
        this.id = id;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public String toString() {
        return this.id;
    }
}
