// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.signature;

import java.security.MessageDigest;
import com.bumptech.glide.load.Key;

public final class EmptySignature implements Key
{
    private static final EmptySignature EMPTY_KEY;
    
    static {
        EMPTY_KEY = new EmptySignature();
    }
    
    private EmptySignature() {
    }
    
    public static EmptySignature obtain() {
        return EmptySignature.EMPTY_KEY;
    }
    
    @Override
    public String toString() {
        return "EmptySignature";
    }
    
    @Override
    public void updateDiskCacheKey(final MessageDigest messageDigest) {
    }
}
