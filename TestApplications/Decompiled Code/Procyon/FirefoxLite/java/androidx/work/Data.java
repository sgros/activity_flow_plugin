// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work;

import java.util.Collections;
import java.util.Iterator;
import java.io.IOException;
import android.util.Log;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public final class Data
{
    public static final Data EMPTY;
    private static final String TAG;
    Map<String, Object> mValues;
    
    static {
        TAG = Logger.tagWithPrefix("Data");
        EMPTY = new Builder().build();
    }
    
    Data() {
    }
    
    public Data(final Data data) {
        this.mValues = new HashMap<String, Object>(data.mValues);
    }
    
    Data(final Map<String, ?> m) {
        this.mValues = new HashMap<String, Object>(m);
    }
    
    static Boolean[] convertPrimitiveBooleanArray(final boolean[] array) {
        final Boolean[] array2 = new Boolean[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = array[i];
        }
        return array2;
    }
    
    static Double[] convertPrimitiveDoubleArray(final double[] array) {
        final Double[] array2 = new Double[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = array[i];
        }
        return array2;
    }
    
    static Float[] convertPrimitiveFloatArray(final float[] array) {
        final Float[] array2 = new Float[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = array[i];
        }
        return array2;
    }
    
    static Integer[] convertPrimitiveIntArray(final int[] array) {
        final Integer[] array2 = new Integer[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = array[i];
        }
        return array2;
    }
    
    static Long[] convertPrimitiveLongArray(final long[] array) {
        final Long[] array2 = new Long[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = array[i];
        }
        return array2;
    }
    
    public static Data fromByteArray(final byte[] p0) throws IllegalStateException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: arraylength    
        //     2: sipush          10240
        //     5: if_icmpgt       215
        //     8: new             Ljava/util/HashMap;
        //    11: dup            
        //    12: invokespecial   java/util/HashMap.<init>:()V
        //    15: astore_1       
        //    16: new             Ljava/io/ByteArrayInputStream;
        //    19: dup            
        //    20: aload_0        
        //    21: invokespecial   java/io/ByteArrayInputStream.<init>:([B)V
        //    24: astore_2       
        //    25: new             Ljava/io/ObjectInputStream;
        //    28: astore_3       
        //    29: aload_3        
        //    30: aload_2        
        //    31: invokespecial   java/io/ObjectInputStream.<init>:(Ljava/io/InputStream;)V
        //    34: aload_3        
        //    35: astore_0       
        //    36: aload_3        
        //    37: invokevirtual   java/io/ObjectInputStream.readInt:()I
        //    40: istore          4
        //    42: iload           4
        //    44: ifle            70
        //    47: aload_3        
        //    48: astore_0       
        //    49: aload_1        
        //    50: aload_3        
        //    51: invokevirtual   java/io/ObjectInputStream.readUTF:()Ljava/lang/String;
        //    54: aload_3        
        //    55: invokevirtual   java/io/ObjectInputStream.readObject:()Ljava/lang/Object;
        //    58: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    63: pop            
        //    64: iinc            4, -1
        //    67: goto            42
        //    70: aload_3        
        //    71: invokevirtual   java/io/ObjectInputStream.close:()V
        //    74: goto            88
        //    77: astore_0       
        //    78: getstatic       androidx/work/Data.TAG:Ljava/lang/String;
        //    81: ldc             "Error in Data#fromByteArray: "
        //    83: aload_0        
        //    84: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    87: pop            
        //    88: aload_2        
        //    89: invokevirtual   java/io/ByteArrayInputStream.close:()V
        //    92: goto            163
        //    95: astore          5
        //    97: goto            110
        //   100: astore_3       
        //   101: aconst_null    
        //   102: astore_0       
        //   103: goto            173
        //   106: astore          5
        //   108: aconst_null    
        //   109: astore_3       
        //   110: aload_3        
        //   111: astore_0       
        //   112: getstatic       androidx/work/Data.TAG:Ljava/lang/String;
        //   115: ldc             "Error in Data#fromByteArray: "
        //   117: aload           5
        //   119: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   122: pop            
        //   123: aload_3        
        //   124: ifnull          145
        //   127: aload_3        
        //   128: invokevirtual   java/io/ObjectInputStream.close:()V
        //   131: goto            145
        //   134: astore_0       
        //   135: getstatic       androidx/work/Data.TAG:Ljava/lang/String;
        //   138: ldc             "Error in Data#fromByteArray: "
        //   140: aload_0        
        //   141: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   144: pop            
        //   145: aload_2        
        //   146: invokevirtual   java/io/ByteArrayInputStream.close:()V
        //   149: goto            163
        //   152: astore_0       
        //   153: getstatic       androidx/work/Data.TAG:Ljava/lang/String;
        //   156: ldc             "Error in Data#fromByteArray: "
        //   158: aload_0        
        //   159: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   162: pop            
        //   163: new             Landroidx/work/Data;
        //   166: dup            
        //   167: aload_1        
        //   168: invokespecial   androidx/work/Data.<init>:(Ljava/util/Map;)V
        //   171: areturn        
        //   172: astore_3       
        //   173: aload_0        
        //   174: ifnull          195
        //   177: aload_0        
        //   178: invokevirtual   java/io/ObjectInputStream.close:()V
        //   181: goto            195
        //   184: astore_0       
        //   185: getstatic       androidx/work/Data.TAG:Ljava/lang/String;
        //   188: ldc             "Error in Data#fromByteArray: "
        //   190: aload_0        
        //   191: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   194: pop            
        //   195: aload_2        
        //   196: invokevirtual   java/io/ByteArrayInputStream.close:()V
        //   199: goto            213
        //   202: astore_0       
        //   203: getstatic       androidx/work/Data.TAG:Ljava/lang/String;
        //   206: ldc             "Error in Data#fromByteArray: "
        //   208: aload_0        
        //   209: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   212: pop            
        //   213: aload_3        
        //   214: athrow         
        //   215: new             Ljava/lang/IllegalStateException;
        //   218: dup            
        //   219: ldc             "Data cannot occupy more than 10240 bytes when serialized"
        //   221: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //   224: athrow         
        //    Exceptions:
        //  throws java.lang.IllegalStateException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                              
        //  -----  -----  -----  -----  ----------------------------------
        //  25     34     106    110    Ljava/io/IOException;
        //  25     34     106    110    Ljava/lang/ClassNotFoundException;
        //  25     34     100    106    Any
        //  36     42     95     100    Ljava/io/IOException;
        //  36     42     95     100    Ljava/lang/ClassNotFoundException;
        //  36     42     172    173    Any
        //  49     64     95     100    Ljava/io/IOException;
        //  49     64     95     100    Ljava/lang/ClassNotFoundException;
        //  49     64     172    173    Any
        //  70     74     77     88     Ljava/io/IOException;
        //  88     92     152    163    Ljava/io/IOException;
        //  112    123    172    173    Any
        //  127    131    134    145    Ljava/io/IOException;
        //  145    149    152    163    Ljava/io/IOException;
        //  177    181    184    195    Ljava/io/IOException;
        //  195    199    202    213    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0042:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static byte[] toByteArray(final Data data) throws IllegalStateException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectOutputStream objectOutputStream = null;
        Object o2;
        final Object o = o2 = null;
        Map.Entry<String, V> entry = null;
        ObjectOutputStream objectOutputStream3;
        try {
            try {
                o2 = o;
                final ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(out);
                try {
                    objectOutputStream2.writeInt(data.size());
                    final Iterator<Map.Entry<String, Object>> iterator = data.mValues.entrySet().iterator();
                    while (iterator.hasNext()) {
                        o2 = iterator.next();
                        objectOutputStream2.writeUTF(((Map.Entry<String, V>)o2).getKey());
                        objectOutputStream2.writeObject(((Map.Entry)o2).getValue());
                    }
                    try {
                        objectOutputStream2.close();
                    }
                    catch (IOException ex) {
                        Log.e(Data.TAG, "Error in Data#toByteArray: ", (Throwable)ex);
                    }
                    try {
                        out.close();
                    }
                    catch (IOException ex2) {
                        Log.e(Data.TAG, "Error in Data#toByteArray: ", (Throwable)ex2);
                    }
                    if (out.size() <= 10240) {
                        return out.toByteArray();
                    }
                    throw new IllegalStateException("Data cannot occupy more than 10240 bytes when serialized");
                }
                catch (IOException o2) {}
            }
            finally {
                entry = (Map.Entry<String, V>)o2;
            }
        }
        catch (IOException entry) {
            objectOutputStream3 = objectOutputStream;
        }
        Log.e(Data.TAG, "Error in Data#toByteArray: ", (Throwable)entry);
        final byte[] byteArray = out.toByteArray();
        if (objectOutputStream3 != null) {
            try {
                objectOutputStream3.close();
            }
            catch (IOException ex3) {
                Log.e(Data.TAG, "Error in Data#toByteArray: ", (Throwable)ex3);
            }
        }
        try {
            out.close();
        }
        catch (IOException ex4) {
            Log.e(Data.TAG, "Error in Data#toByteArray: ", (Throwable)ex4);
        }
        return byteArray;
        if (entry != null) {
            try {
                ((ObjectOutputStream)entry).close();
            }
            catch (IOException ex5) {
                Log.e(Data.TAG, "Error in Data#toByteArray: ", (Throwable)ex5);
            }
        }
        try {
            out.close();
        }
        catch (IOException ex6) {
            Log.e(Data.TAG, "Error in Data#toByteArray: ", (Throwable)ex6);
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && this.getClass() == o.getClass() && this.mValues.equals(((Data)o).mValues));
    }
    
    public Map<String, Object> getKeyValueMap() {
        return Collections.unmodifiableMap((Map<? extends String, ?>)this.mValues);
    }
    
    public String getString(final String s) {
        final String value = this.mValues.get(s);
        if (value instanceof String) {
            return value;
        }
        return null;
    }
    
    @Override
    public int hashCode() {
        return this.mValues.hashCode() * 31;
    }
    
    public int size() {
        return this.mValues.size();
    }
    
    public static final class Builder
    {
        private Map<String, Object> mValues;
        
        public Builder() {
            this.mValues = new HashMap<String, Object>();
        }
        
        public Data build() {
            final Data data = new Data(this.mValues);
            Data.toByteArray(data);
            return data;
        }
        
        public Builder put(final String s, final Object o) {
            if (o == null) {
                this.mValues.put(s, null);
            }
            else {
                final Class<?> class1 = o.getClass();
                if (class1 != Boolean.class && class1 != Integer.class && class1 != Long.class && class1 != Float.class && class1 != Double.class && class1 != String.class && class1 != Boolean[].class && class1 != Integer[].class && class1 != Long[].class && class1 != Float[].class && class1 != Double[].class && class1 != String[].class) {
                    if (class1 == boolean[].class) {
                        this.mValues.put(s, Data.convertPrimitiveBooleanArray((boolean[])o));
                    }
                    else if (class1 == int[].class) {
                        this.mValues.put(s, Data.convertPrimitiveIntArray((int[])o));
                    }
                    else if (class1 == long[].class) {
                        this.mValues.put(s, Data.convertPrimitiveLongArray((long[])o));
                    }
                    else if (class1 == float[].class) {
                        this.mValues.put(s, Data.convertPrimitiveFloatArray((float[])o));
                    }
                    else {
                        if (class1 != double[].class) {
                            throw new IllegalArgumentException(String.format("Key %s has invalid type %s", s, class1));
                        }
                        this.mValues.put(s, Data.convertPrimitiveDoubleArray((double[])o));
                    }
                }
                else {
                    this.mValues.put(s, o);
                }
            }
            return this;
        }
        
        public Builder putAll(final Data data) {
            this.putAll(data.mValues);
            return this;
        }
        
        public Builder putAll(final Map<String, Object> map) {
            for (final Map.Entry<String, Object> entry : map.entrySet()) {
                this.put(entry.getKey(), entry.getValue());
            }
            return this;
        }
        
        public Builder putString(final String s, final String s2) {
            this.mValues.put(s, s2);
            return this;
        }
    }
}
