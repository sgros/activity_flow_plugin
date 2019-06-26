package android.support.p003v7.content.res;

import java.lang.reflect.Array;

/* renamed from: android.support.v7.content.res.GrowingArrayUtils */
final class GrowingArrayUtils {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    public static int growSize(int i) {
        return i <= 4 ? 8 : i * 2;
    }

    public static <T> T[] append(T[] tArr, int i, T t) {
        if (i + 1 > tArr.length) {
            T[] tArr2 = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), GrowingArrayUtils.growSize(i));
            System.arraycopy(tArr, 0, tArr2, 0, i);
            tArr = tArr2;
        }
        tArr[i] = t;
        return tArr;
    }

    public static int[] append(int[] iArr, int i, int i2) {
        if (i + 1 > iArr.length) {
            int[] iArr2 = new int[GrowingArrayUtils.growSize(i)];
            System.arraycopy(iArr, 0, iArr2, 0, i);
            iArr = iArr2;
        }
        iArr[i] = i2;
        return iArr;
    }

    public static long[] append(long[] jArr, int i, long j) {
        if (i + 1 > jArr.length) {
            long[] jArr2 = new long[GrowingArrayUtils.growSize(i)];
            System.arraycopy(jArr, 0, jArr2, 0, i);
            jArr = jArr2;
        }
        jArr[i] = j;
        return jArr;
    }

    public static boolean[] append(boolean[] zArr, int i, boolean z) {
        if (i + 1 > zArr.length) {
            boolean[] zArr2 = new boolean[GrowingArrayUtils.growSize(i)];
            System.arraycopy(zArr, 0, zArr2, 0, i);
            zArr = zArr2;
        }
        zArr[i] = z;
        return zArr;
    }

    public static <T> T[] insert(T[] tArr, int i, int i2, T t) {
        if (i + 1 <= tArr.length) {
            System.arraycopy(tArr, i2, tArr, i2 + 1, i - i2);
            tArr[i2] = t;
            return tArr;
        }
        Object[] objArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), GrowingArrayUtils.growSize(i));
        System.arraycopy(tArr, 0, objArr, 0, i2);
        objArr[i2] = t;
        System.arraycopy(tArr, i2, objArr, i2 + 1, tArr.length - i2);
        return objArr;
    }

    public static int[] insert(int[] iArr, int i, int i2, int i3) {
        if (i + 1 <= iArr.length) {
            System.arraycopy(iArr, i2, iArr, i2 + 1, i - i2);
            iArr[i2] = i3;
            return iArr;
        }
        int[] iArr2 = new int[GrowingArrayUtils.growSize(i)];
        System.arraycopy(iArr, 0, iArr2, 0, i2);
        iArr2[i2] = i3;
        System.arraycopy(iArr, i2, iArr2, i2 + 1, iArr.length - i2);
        return iArr2;
    }

    public static long[] insert(long[] jArr, int i, int i2, long j) {
        if (i + 1 <= jArr.length) {
            System.arraycopy(jArr, i2, jArr, i2 + 1, i - i2);
            jArr[i2] = j;
            return jArr;
        }
        long[] jArr2 = new long[GrowingArrayUtils.growSize(i)];
        System.arraycopy(jArr, 0, jArr2, 0, i2);
        jArr2[i2] = j;
        System.arraycopy(jArr, i2, jArr2, i2 + 1, jArr.length - i2);
        return jArr2;
    }

    public static boolean[] insert(boolean[] zArr, int i, int i2, boolean z) {
        if (i + 1 <= zArr.length) {
            System.arraycopy(zArr, i2, zArr, i2 + 1, i - i2);
            zArr[i2] = z;
            return zArr;
        }
        boolean[] zArr2 = new boolean[GrowingArrayUtils.growSize(i)];
        System.arraycopy(zArr, 0, zArr2, 0, i2);
        zArr2[i2] = z;
        System.arraycopy(zArr, i2, zArr2, i2 + 1, zArr.length - i2);
        return zArr2;
    }

    private GrowingArrayUtils() {
    }
}
