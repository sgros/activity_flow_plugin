// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.network;

public enum FileExtension
{
    JSON(".json"), 
    ZIP(".zip");
    
    public final String extension;
    
    private FileExtension(final String extension) {
        this.extension = extension;
    }
    
    public String tempExtension() {
        final StringBuilder sb = new StringBuilder();
        sb.append(".temp");
        sb.append(this.extension);
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return this.extension;
    }
}
