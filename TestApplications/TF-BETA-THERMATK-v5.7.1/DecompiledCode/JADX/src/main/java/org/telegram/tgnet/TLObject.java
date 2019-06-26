package org.telegram.tgnet;

public class TLObject {
    private static final ThreadLocal<NativeByteBuffer> sizeCalculator = new C11571();
    public boolean disableFree = false;
    public int networkType;

    /* renamed from: org.telegram.tgnet.TLObject$1 */
    static class C11571 extends ThreadLocal<NativeByteBuffer> {
        C11571() {
        }

        /* Access modifiers changed, original: protected */
        public NativeByteBuffer initialValue() {
            return new NativeByteBuffer(true);
        }
    }

    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return null;
    }

    public void freeResources() {
    }

    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
    }

    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
    }

    public int getObjectSize() {
        NativeByteBuffer nativeByteBuffer = (NativeByteBuffer) sizeCalculator.get();
        nativeByteBuffer.rewind();
        serializeToStream((AbstractSerializedData) sizeCalculator.get());
        return nativeByteBuffer.length();
    }
}
