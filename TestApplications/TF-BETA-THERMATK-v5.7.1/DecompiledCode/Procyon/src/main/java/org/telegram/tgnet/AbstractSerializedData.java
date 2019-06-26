// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.tgnet;

public abstract class AbstractSerializedData
{
    public abstract int getPosition();
    
    public abstract int length();
    
    public abstract boolean readBool(final boolean p0);
    
    public abstract byte[] readByteArray(final boolean p0);
    
    public abstract NativeByteBuffer readByteBuffer(final boolean p0);
    
    public abstract void readBytes(final byte[] p0, final boolean p1);
    
    public abstract byte[] readData(final int p0, final boolean p1);
    
    public abstract double readDouble(final boolean p0);
    
    public abstract int readInt32(final boolean p0);
    
    public abstract long readInt64(final boolean p0);
    
    public abstract String readString(final boolean p0);
    
    public abstract int remaining();
    
    public abstract void skip(final int p0);
    
    public abstract void writeBool(final boolean p0);
    
    public abstract void writeByte(final byte p0);
    
    public abstract void writeByte(final int p0);
    
    public abstract void writeByteArray(final byte[] p0);
    
    public abstract void writeByteArray(final byte[] p0, final int p1, final int p2);
    
    public abstract void writeByteBuffer(final NativeByteBuffer p0);
    
    public abstract void writeBytes(final byte[] p0);
    
    public abstract void writeBytes(final byte[] p0, final int p1, final int p2);
    
    public abstract void writeDouble(final double p0);
    
    public abstract void writeInt32(final int p0);
    
    public abstract void writeInt64(final long p0);
    
    public abstract void writeString(final String p0);
}
