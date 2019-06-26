// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.tgnet;

public class TLObject
{
    private static final ThreadLocal<NativeByteBuffer> sizeCalculator;
    public boolean disableFree;
    public int networkType;
    
    static {
        sizeCalculator = new ThreadLocal<NativeByteBuffer>() {
            @Override
            protected NativeByteBuffer initialValue() {
                return new NativeByteBuffer(true);
            }
        };
    }
    
    public TLObject() {
        this.disableFree = false;
    }
    
    public TLObject deserializeResponse(final AbstractSerializedData abstractSerializedData, final int n, final boolean b) {
        return null;
    }
    
    public void freeResources() {
    }
    
    public int getObjectSize() {
        final NativeByteBuffer nativeByteBuffer = TLObject.sizeCalculator.get();
        nativeByteBuffer.rewind();
        this.serializeToStream(TLObject.sizeCalculator.get());
        return nativeByteBuffer.length();
    }
    
    public void readParams(final AbstractSerializedData abstractSerializedData, final boolean b) {
    }
    
    public void serializeToStream(final AbstractSerializedData abstractSerializedData) {
    }
}
