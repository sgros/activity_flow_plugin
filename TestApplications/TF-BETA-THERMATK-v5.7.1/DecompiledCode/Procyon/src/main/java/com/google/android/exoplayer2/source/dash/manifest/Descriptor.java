// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.dash.manifest;

import com.google.android.exoplayer2.util.Util;

public final class Descriptor
{
    public final String id;
    public final String schemeIdUri;
    public final String value;
    
    public Descriptor(final String schemeIdUri, final String value, final String id) {
        this.schemeIdUri = schemeIdUri;
        this.value = value;
        this.id = id;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && Descriptor.class == o.getClass()) {
            final Descriptor descriptor = (Descriptor)o;
            if (!Util.areEqual(this.schemeIdUri, descriptor.schemeIdUri) || !Util.areEqual(this.value, descriptor.value) || !Util.areEqual(this.id, descriptor.id)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
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
        int hashCode3;
        if (value != null) {
            hashCode3 = value.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        final String id = this.id;
        if (id != null) {
            hashCode = id.hashCode();
        }
        return (hashCode2 * 31 + hashCode3) * 31 + hashCode;
    }
}
