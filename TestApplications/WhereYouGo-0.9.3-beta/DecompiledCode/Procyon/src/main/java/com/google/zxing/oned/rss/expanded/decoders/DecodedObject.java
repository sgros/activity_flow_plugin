// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.oned.rss.expanded.decoders;

abstract class DecodedObject
{
    private final int newPosition;
    
    DecodedObject(final int newPosition) {
        this.newPosition = newPosition;
    }
    
    final int getNewPosition() {
        return this.newPosition;
    }
}
