package androidx.work;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class Data {
    public static final Data EMPTY = new Builder().build();
    private static final String TAG = Logger.tagWithPrefix("Data");
    Map<String, Object> mValues;

    public static final class Builder {
        private Map<String, Object> mValues = new HashMap();

        public Builder putString(String str, String str2) {
            this.mValues.put(str, str2);
            return this;
        }

        public Builder putAll(Data data) {
            putAll(data.mValues);
            return this;
        }

        public Builder putAll(Map<String, Object> map) {
            for (Entry entry : map.entrySet()) {
                put((String) entry.getKey(), entry.getValue());
            }
            return this;
        }

        public Builder put(String str, Object obj) {
            if (obj == null) {
                this.mValues.put(str, null);
            } else {
                Class cls = obj.getClass();
                if (cls == Boolean.class || cls == Integer.class || cls == Long.class || cls == Float.class || cls == Double.class || cls == String.class || cls == Boolean[].class || cls == Integer[].class || cls == Long[].class || cls == Float[].class || cls == Double[].class || cls == String[].class) {
                    this.mValues.put(str, obj);
                } else if (cls == boolean[].class) {
                    this.mValues.put(str, Data.convertPrimitiveBooleanArray((boolean[]) obj));
                } else if (cls == int[].class) {
                    this.mValues.put(str, Data.convertPrimitiveIntArray((int[]) obj));
                } else if (cls == long[].class) {
                    this.mValues.put(str, Data.convertPrimitiveLongArray((long[]) obj));
                } else if (cls == float[].class) {
                    this.mValues.put(str, Data.convertPrimitiveFloatArray((float[]) obj));
                } else if (cls == double[].class) {
                    this.mValues.put(str, Data.convertPrimitiveDoubleArray((double[]) obj));
                } else {
                    throw new IllegalArgumentException(String.format("Key %s has invalid type %s", new Object[]{str, cls}));
                }
            }
            return this;
        }

        public Data build() {
            Data data = new Data(this.mValues);
            Data.toByteArray(data);
            return data;
        }
    }

    Data() {
    }

    public Data(Data data) {
        this.mValues = new HashMap(data.mValues);
    }

    Data(Map<String, ?> map) {
        this.mValues = new HashMap(map);
    }

    public String getString(String str) {
        Object obj = this.mValues.get(str);
        return obj instanceof String ? (String) obj : null;
    }

    public Map<String, Object> getKeyValueMap() {
        return Collections.unmodifiableMap(this.mValues);
    }

    public int size() {
        return this.mValues.size();
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x0097 A:{SYNTHETIC, Splitter:B:42:0x0097} */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x007c A:{SYNTHETIC, Splitter:B:32:0x007c} */
    public static byte[] toByteArray(androidx.work.Data r4) throws java.lang.IllegalStateException {
        /*
        r0 = new java.io.ByteArrayOutputStream;
        r0.<init>();
        r1 = 0;
        r2 = new java.io.ObjectOutputStream;	 Catch:{ IOException -> 0x006e }
        r2.<init>(r0);	 Catch:{ IOException -> 0x006e }
        r1 = r4.size();	 Catch:{ IOException -> 0x0068, all -> 0x0066 }
        r2.writeInt(r1);	 Catch:{ IOException -> 0x0068, all -> 0x0066 }
        r4 = r4.mValues;	 Catch:{ IOException -> 0x0068, all -> 0x0066 }
        r4 = r4.entrySet();	 Catch:{ IOException -> 0x0068, all -> 0x0066 }
        r4 = r4.iterator();	 Catch:{ IOException -> 0x0068, all -> 0x0066 }
    L_0x001c:
        r1 = r4.hasNext();	 Catch:{ IOException -> 0x0068, all -> 0x0066 }
        if (r1 == 0) goto L_0x0039;
    L_0x0022:
        r1 = r4.next();	 Catch:{ IOException -> 0x0068, all -> 0x0066 }
        r1 = (java.util.Map.Entry) r1;	 Catch:{ IOException -> 0x0068, all -> 0x0066 }
        r3 = r1.getKey();	 Catch:{ IOException -> 0x0068, all -> 0x0066 }
        r3 = (java.lang.String) r3;	 Catch:{ IOException -> 0x0068, all -> 0x0066 }
        r2.writeUTF(r3);	 Catch:{ IOException -> 0x0068, all -> 0x0066 }
        r1 = r1.getValue();	 Catch:{ IOException -> 0x0068, all -> 0x0066 }
        r2.writeObject(r1);	 Catch:{ IOException -> 0x0068, all -> 0x0066 }
        goto L_0x001c;
    L_0x0039:
        r2.close();	 Catch:{ IOException -> 0x003d }
        goto L_0x0045;
    L_0x003d:
        r4 = move-exception;
        r1 = TAG;
        r2 = "Error in Data#toByteArray: ";
        android.util.Log.e(r1, r2, r4);
    L_0x0045:
        r0.close();	 Catch:{ IOException -> 0x0049 }
        goto L_0x0051;
    L_0x0049:
        r4 = move-exception;
        r1 = TAG;
        r2 = "Error in Data#toByteArray: ";
        android.util.Log.e(r1, r2, r4);
    L_0x0051:
        r4 = r0.size();
        r1 = 10240; // 0x2800 float:1.4349E-41 double:5.059E-320;
        if (r4 > r1) goto L_0x005e;
    L_0x0059:
        r4 = r0.toByteArray();
        return r4;
    L_0x005e:
        r4 = new java.lang.IllegalStateException;
        r0 = "Data cannot occupy more than 10240 bytes when serialized";
        r4.<init>(r0);
        throw r4;
    L_0x0066:
        r4 = move-exception;
        goto L_0x0095;
    L_0x0068:
        r4 = move-exception;
        r1 = r2;
        goto L_0x006f;
    L_0x006b:
        r4 = move-exception;
        r2 = r1;
        goto L_0x0095;
    L_0x006e:
        r4 = move-exception;
    L_0x006f:
        r2 = TAG;	 Catch:{ all -> 0x006b }
        r3 = "Error in Data#toByteArray: ";
        android.util.Log.e(r2, r3, r4);	 Catch:{ all -> 0x006b }
        r4 = r0.toByteArray();	 Catch:{ all -> 0x006b }
        if (r1 == 0) goto L_0x0088;
    L_0x007c:
        r1.close();	 Catch:{ IOException -> 0x0080 }
        goto L_0x0088;
    L_0x0080:
        r1 = move-exception;
        r2 = TAG;
        r3 = "Error in Data#toByteArray: ";
        android.util.Log.e(r2, r3, r1);
    L_0x0088:
        r0.close();	 Catch:{ IOException -> 0x008c }
        goto L_0x0094;
    L_0x008c:
        r0 = move-exception;
        r1 = TAG;
        r2 = "Error in Data#toByteArray: ";
        android.util.Log.e(r1, r2, r0);
    L_0x0094:
        return r4;
    L_0x0095:
        if (r2 == 0) goto L_0x00a3;
    L_0x0097:
        r2.close();	 Catch:{ IOException -> 0x009b }
        goto L_0x00a3;
    L_0x009b:
        r1 = move-exception;
        r2 = TAG;
        r3 = "Error in Data#toByteArray: ";
        android.util.Log.e(r2, r3, r1);
    L_0x00a3:
        r0.close();	 Catch:{ IOException -> 0x00a7 }
        goto L_0x00af;
    L_0x00a7:
        r0 = move-exception;
        r1 = TAG;
        r2 = "Error in Data#toByteArray: ";
        android.util.Log.e(r1, r2, r0);
    L_0x00af:
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.work.Data.toByteArray(androidx.work.Data):byte[]");
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:14:0x0035=Splitter:B:14:0x0035, B:28:0x0058=Splitter:B:28:0x0058} */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x006d A:{SYNTHETIC, Splitter:B:36:0x006d} */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x004c A:{SYNTHETIC, Splitter:B:24:0x004c} */
    public static androidx.work.Data fromByteArray(byte[] r6) throws java.lang.IllegalStateException {
        /*
        r0 = r6.length;
        r1 = 10240; // 0x2800 float:1.4349E-41 double:5.059E-320;
        if (r0 > r1) goto L_0x0086;
    L_0x0005:
        r0 = new java.util.HashMap;
        r0.<init>();
        r1 = new java.io.ByteArrayInputStream;
        r1.<init>(r6);
        r6 = 0;
        r2 = new java.io.ObjectInputStream;	 Catch:{ IOException | ClassNotFoundException -> 0x003f, IOException | ClassNotFoundException -> 0x003f, all -> 0x003b }
        r2.<init>(r1);	 Catch:{ IOException | ClassNotFoundException -> 0x003f, IOException | ClassNotFoundException -> 0x003f, all -> 0x003b }
        r6 = r2.readInt();	 Catch:{ IOException | ClassNotFoundException -> 0x0039, IOException | ClassNotFoundException -> 0x0039 }
    L_0x0019:
        if (r6 <= 0) goto L_0x0029;
    L_0x001b:
        r3 = r2.readUTF();	 Catch:{ IOException | ClassNotFoundException -> 0x0039, IOException | ClassNotFoundException -> 0x0039 }
        r4 = r2.readObject();	 Catch:{ IOException | ClassNotFoundException -> 0x0039, IOException | ClassNotFoundException -> 0x0039 }
        r0.put(r3, r4);	 Catch:{ IOException | ClassNotFoundException -> 0x0039, IOException | ClassNotFoundException -> 0x0039 }
        r6 = r6 + -1;
        goto L_0x0019;
    L_0x0029:
        r2.close();	 Catch:{ IOException -> 0x002d }
        goto L_0x0035;
    L_0x002d:
        r6 = move-exception;
        r2 = TAG;
        r3 = "Error in Data#fromByteArray: ";
        android.util.Log.e(r2, r3, r6);
    L_0x0035:
        r1.close();	 Catch:{ IOException -> 0x005c }
        goto L_0x0064;
    L_0x0039:
        r6 = move-exception;
        goto L_0x0043;
    L_0x003b:
        r0 = move-exception;
        r2 = r6;
        r6 = r0;
        goto L_0x006b;
    L_0x003f:
        r2 = move-exception;
        r5 = r2;
        r2 = r6;
        r6 = r5;
    L_0x0043:
        r3 = TAG;	 Catch:{ all -> 0x006a }
        r4 = "Error in Data#fromByteArray: ";
        android.util.Log.e(r3, r4, r6);	 Catch:{ all -> 0x006a }
        if (r2 == 0) goto L_0x0058;
    L_0x004c:
        r2.close();	 Catch:{ IOException -> 0x0050 }
        goto L_0x0058;
    L_0x0050:
        r6 = move-exception;
        r2 = TAG;
        r3 = "Error in Data#fromByteArray: ";
        android.util.Log.e(r2, r3, r6);
    L_0x0058:
        r1.close();	 Catch:{ IOException -> 0x005c }
        goto L_0x0064;
    L_0x005c:
        r6 = move-exception;
        r1 = TAG;
        r2 = "Error in Data#fromByteArray: ";
        android.util.Log.e(r1, r2, r6);
    L_0x0064:
        r6 = new androidx.work.Data;
        r6.<init>(r0);
        return r6;
    L_0x006a:
        r6 = move-exception;
    L_0x006b:
        if (r2 == 0) goto L_0x0079;
    L_0x006d:
        r2.close();	 Catch:{ IOException -> 0x0071 }
        goto L_0x0079;
    L_0x0071:
        r0 = move-exception;
        r2 = TAG;
        r3 = "Error in Data#fromByteArray: ";
        android.util.Log.e(r2, r3, r0);
    L_0x0079:
        r1.close();	 Catch:{ IOException -> 0x007d }
        goto L_0x0085;
    L_0x007d:
        r0 = move-exception;
        r1 = TAG;
        r2 = "Error in Data#fromByteArray: ";
        android.util.Log.e(r1, r2, r0);
    L_0x0085:
        throw r6;
    L_0x0086:
        r6 = new java.lang.IllegalStateException;
        r0 = "Data cannot occupy more than 10240 bytes when serialized";
        r6.<init>(r0);
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.work.Data.fromByteArray(byte[]):androidx.work.Data");
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.mValues.equals(((Data) obj).mValues);
    }

    public int hashCode() {
        return this.mValues.hashCode() * 31;
    }

    static Boolean[] convertPrimitiveBooleanArray(boolean[] zArr) {
        Boolean[] boolArr = new Boolean[zArr.length];
        for (int i = 0; i < zArr.length; i++) {
            boolArr[i] = Boolean.valueOf(zArr[i]);
        }
        return boolArr;
    }

    static Integer[] convertPrimitiveIntArray(int[] iArr) {
        Integer[] numArr = new Integer[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            numArr[i] = Integer.valueOf(iArr[i]);
        }
        return numArr;
    }

    static Long[] convertPrimitiveLongArray(long[] jArr) {
        Long[] lArr = new Long[jArr.length];
        for (int i = 0; i < jArr.length; i++) {
            lArr[i] = Long.valueOf(jArr[i]);
        }
        return lArr;
    }

    static Float[] convertPrimitiveFloatArray(float[] fArr) {
        Float[] fArr2 = new Float[fArr.length];
        for (int i = 0; i < fArr.length; i++) {
            fArr2[i] = Float.valueOf(fArr[i]);
        }
        return fArr2;
    }

    static Double[] convertPrimitiveDoubleArray(double[] dArr) {
        Double[] dArr2 = new Double[dArr.length];
        for (int i = 0; i < dArr.length; i++) {
            dArr2[i] = Double.valueOf(dArr[i]);
        }
        return dArr2;
    }
}
