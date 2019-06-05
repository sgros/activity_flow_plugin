// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.reader;

final class Deserializer
{
    private Deserializer() {
        throw new IllegalStateException();
    }
    
    static long getFiveBytesLong(final byte[] array, final int n) {
        return ((long)array[n] & 0xFFL) << 32 | ((long)array[n + 1] & 0xFFL) << 24 | ((long)array[n + 2] & 0xFFL) << 16 | ((long)array[n + 3] & 0xFFL) << 8 | ((long)array[n + 4] & 0xFFL);
    }
    
    static int getInt(final byte[] array, final int n) {
        return array[n] << 24 | (array[n + 1] & 0xFF) << 16 | (array[n + 2] & 0xFF) << 8 | (array[n + 3] & 0xFF);
    }
    
    static long getLong(final byte[] array, final int n) {
        return ((long)array[n] & 0xFFL) << 56 | ((long)array[n + 1] & 0xFFL) << 48 | ((long)array[n + 2] & 0xFFL) << 40 | ((long)array[n + 3] & 0xFFL) << 32 | ((long)array[n + 4] & 0xFFL) << 24 | ((long)array[n + 5] & 0xFFL) << 16 | ((long)array[n + 6] & 0xFFL) << 8 | ((long)array[n + 7] & 0xFFL);
    }
    
    static int getShort(final byte[] array, final int n) {
        return array[n] << 8 | (array[n + 1] & 0xFF);
    }
}
