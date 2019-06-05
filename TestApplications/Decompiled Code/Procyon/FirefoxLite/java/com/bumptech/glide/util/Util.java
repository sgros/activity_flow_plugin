// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.util;

import android.os.Looper;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import android.annotation.TargetApi;
import android.os.Build$VERSION;
import android.graphics.Bitmap;
import android.graphics.Bitmap$Config;
import java.util.ArrayDeque;
import java.util.Queue;
import com.bumptech.glide.load.model.Model;

public final class Util
{
    private static final char[] HEX_CHAR_ARRAY;
    private static final char[] SHA_256_CHARS;
    
    static {
        HEX_CHAR_ARRAY = "0123456789abcdef".toCharArray();
        SHA_256_CHARS = new char[64];
    }
    
    public static void assertMainThread() {
        if (isOnMainThread()) {
            return;
        }
        throw new IllegalArgumentException("You must call this method on the main thread");
    }
    
    public static boolean bothModelsNullEquivalentOrEquals(final Object o, final Object obj) {
        if (o == null) {
            return obj == null;
        }
        if (o instanceof Model) {
            return ((Model)o).isEquivalentTo(obj);
        }
        return o.equals(obj);
    }
    
    public static boolean bothNullOrEqual(final Object o, final Object obj) {
        boolean equals;
        if (o == null) {
            equals = (obj == null);
        }
        else {
            equals = o.equals(obj);
        }
        return equals;
    }
    
    private static String bytesToHex(final byte[] array, final char[] value) {
        for (int i = 0; i < array.length; ++i) {
            final int n = array[i] & 0xFF;
            final int n2 = i * 2;
            value[n2] = Util.HEX_CHAR_ARRAY[n >>> 4];
            value[n2 + 1] = Util.HEX_CHAR_ARRAY[n & 0xF];
        }
        return new String(value);
    }
    
    public static <T> Queue<T> createQueue(final int numElements) {
        return new ArrayDeque<T>(numElements);
    }
    
    public static int getBitmapByteSize(final int n, final int n2, final Bitmap$Config bitmap$Config) {
        return n * n2 * getBytesPerPixel(bitmap$Config);
    }
    
    @TargetApi(19)
    public static int getBitmapByteSize(final Bitmap obj) {
        Label_0032: {
            if (obj.isRecycled()) {
                break Label_0032;
            }
            Label_0022: {
                if (Build$VERSION.SDK_INT < 19) {
                    break Label_0022;
                }
                try {
                    return obj.getAllocationByteCount();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Cannot obtain size for recycled Bitmap: ");
                    sb.append(obj);
                    sb.append("[");
                    sb.append(obj.getWidth());
                    sb.append("x");
                    sb.append(obj.getHeight());
                    sb.append("] ");
                    sb.append(obj.getConfig());
                    throw new IllegalStateException(sb.toString());
                    return obj.getHeight() * obj.getRowBytes();
                }
                catch (NullPointerException ex) {
                    return obj.getHeight() * obj.getRowBytes();
                }
            }
        }
    }
    
    private static int getBytesPerPixel(final Bitmap$Config bitmap$Config) {
        Bitmap$Config argb_8888 = bitmap$Config;
        if (bitmap$Config == null) {
            argb_8888 = Bitmap$Config.ARGB_8888;
        }
        int n = 0;
        switch (Util$1.$SwitchMap$android$graphics$Bitmap$Config[argb_8888.ordinal()]) {
            default: {
                n = 4;
                break;
            }
            case 2:
            case 3: {
                n = 2;
                break;
            }
            case 1: {
                n = 1;
                break;
            }
        }
        return n;
    }
    
    public static <T> List<T> getSnapshot(final Collection<T> collection) {
        final ArrayList<T> list = new ArrayList<T>(collection.size());
        final Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }
    
    public static int hashCode(final float n) {
        return hashCode(n, 17);
    }
    
    public static int hashCode(final float value, final int n) {
        return hashCode(Float.floatToIntBits(value), n);
    }
    
    public static int hashCode(final int n, final int n2) {
        return n2 * 31 + n;
    }
    
    public static int hashCode(final Object o, final int n) {
        int hashCode;
        if (o == null) {
            hashCode = 0;
        }
        else {
            hashCode = o.hashCode();
        }
        return hashCode(hashCode, n);
    }
    
    public static int hashCode(final boolean b, final int n) {
        return hashCode(b ? 1 : 0, n);
    }
    
    public static boolean isOnBackgroundThread() {
        return isOnMainThread() ^ true;
    }
    
    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
    
    private static boolean isValidDimension(final int n) {
        return n > 0 || n == Integer.MIN_VALUE;
    }
    
    public static boolean isValidDimensions(final int n, final int n2) {
        return isValidDimension(n) && isValidDimension(n2);
    }
    
    public static String sha256BytesToHex(final byte[] array) {
        synchronized (Util.SHA_256_CHARS) {
            return bytesToHex(array, Util.SHA_256_CHARS);
        }
    }
}
