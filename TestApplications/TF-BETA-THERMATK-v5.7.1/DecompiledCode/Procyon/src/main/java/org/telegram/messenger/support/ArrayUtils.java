// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.support;

import java.lang.reflect.Array;

public class ArrayUtils
{
    private static final int CACHE_SIZE = 73;
    private static Object[] EMPTY;
    private static Object[] sCache;
    
    static {
        ArrayUtils.EMPTY = new Object[0];
        ArrayUtils.sCache = new Object[73];
    }
    
    private ArrayUtils() {
    }
    
    public static <T> T[] appendElement(final Class<T> clazz, final T[] array, final T t) {
        int length = 0;
        Object[] array2;
        if (array != null) {
            length = array.length;
            array2 = (Object[])Array.newInstance(clazz, length + 1);
            System.arraycopy(array, 0, array2, 0, length);
        }
        else {
            array2 = (Object[])Array.newInstance(clazz, 1);
        }
        array2[length] = t;
        return (T[])array2;
    }
    
    public static int[] appendInt(final int[] array, final int n) {
        if (array == null) {
            return new int[] { n };
        }
        final int length = array.length;
        for (int i = 0; i < length; ++i) {
            if (array[i] == n) {
                return array;
            }
        }
        final int[] array2 = new int[length + 1];
        System.arraycopy(array, 0, array2, 0, length);
        array2[length] = n;
        return array2;
    }
    
    public static boolean contains(final int[] array, final int n) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (array[i] == n) {
                return true;
            }
        }
        return false;
    }
    
    public static <T> boolean contains(final T[] array, final T obj) {
        for (final T t : array) {
            if (t == null) {
                if (obj == null) {
                    return true;
                }
            }
            else if (obj != null && t.equals(obj)) {
                return true;
            }
        }
        return false;
    }
    
    public static <T> T[] emptyArray(final Class<T> componentType) {
        if (componentType == Object.class) {
            return (T[])ArrayUtils.EMPTY;
        }
        final int n = (System.identityHashCode(componentType) / 8 & Integer.MAX_VALUE) % 73;
        final Object o = ArrayUtils.sCache[n];
        if (o != null) {
            final Object instance = o;
            if (((T[])o).getClass().getComponentType() == componentType) {
                return (T[])instance;
            }
        }
        final Object instance = Array.newInstance(componentType, 0);
        ArrayUtils.sCache[n] = instance;
        return (T[])instance;
    }
    
    public static boolean equals(final byte[] array, final byte[] array2, final int n) {
        if (array == array2) {
            return true;
        }
        if (array != null && array2 != null && array.length >= n && array2.length >= n) {
            for (int i = 0; i < n; ++i) {
                if (array[i] != array2[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public static int idealBooleanArraySize(final int n) {
        return idealByteArraySize(n);
    }
    
    public static int idealByteArraySize(final int n) {
        for (int i = 4; i < 32; ++i) {
            final int n2 = (1 << i) - 12;
            if (n <= n2) {
                return n2;
            }
        }
        return n;
    }
    
    public static int idealCharArraySize(final int n) {
        return idealByteArraySize(n * 2) / 2;
    }
    
    public static int idealFloatArraySize(final int n) {
        return idealByteArraySize(n * 4) / 4;
    }
    
    public static int idealIntArraySize(final int n) {
        return idealByteArraySize(n * 4) / 4;
    }
    
    public static int idealLongArraySize(final int n) {
        return idealByteArraySize(n * 8) / 8;
    }
    
    public static int idealObjectArraySize(final int n) {
        return idealByteArraySize(n * 4) / 4;
    }
    
    public static int idealShortArraySize(final int n) {
        return idealByteArraySize(n * 2) / 2;
    }
    
    public static <T> T[] removeElement(final Class<T> componentType, final T[] array, final T t) {
        if (array != null) {
            final int length = array.length;
            int i = 0;
            while (i < length) {
                if (array[i] == t) {
                    if (length == 1) {
                        return null;
                    }
                    final Object[] array2 = (Object[])Array.newInstance(componentType, length - 1);
                    System.arraycopy(array, 0, array2, 0, i);
                    System.arraycopy(array, i + 1, array2, i, length - i - 1);
                    return (T[])array2;
                }
                else {
                    ++i;
                }
            }
        }
        return array;
    }
    
    public static int[] removeInt(final int[] array, int n) {
        if (array == null) {
            return null;
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            if (array[i] == n) {
                n = length - 1;
                final int[] array2 = new int[n];
                if (i > 0) {
                    System.arraycopy(array, 0, array2, 0, i);
                }
                if (i < n) {
                    System.arraycopy(array, i + 1, array2, i, length - i - 1);
                }
                return array2;
            }
        }
        return array;
    }
    
    public static long total(final long[] array) {
        final int length = array.length;
        long n = 0L;
        for (int i = 0; i < length; ++i) {
            n += array[i];
        }
        return n;
    }
}
