// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.db;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase;
import java.io.File;
import android.os.Build$VERSION;
import android.util.Log;

public interface SupportSQLiteOpenHelper
{
    SupportSQLiteDatabase getReadableDatabase();
    
    SupportSQLiteDatabase getWritableDatabase();
    
    void setWriteAheadLoggingEnabled(final boolean p0);
    
    public abstract static class Callback
    {
        public final int version;
        
        public Callback(final int version) {
            this.version = version;
        }
        
        private void deleteDatabaseFile(final String s) {
            if (!s.equalsIgnoreCase(":memory:") && s.trim().length() != 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("deleting the database file: ");
                sb.append(s);
                Log.w("SupportSQLite", sb.toString());
                try {
                    if (Build$VERSION.SDK_INT >= 16) {
                        SQLiteDatabase.deleteDatabase(new File(s));
                    }
                    else {
                        try {
                            if (!new File(s).delete()) {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("Could not delete the database file ");
                                sb2.append(s);
                                Log.e("SupportSQLite", sb2.toString());
                            }
                        }
                        catch (Exception ex) {
                            Log.e("SupportSQLite", "error while deleting corrupted database file", (Throwable)ex);
                        }
                    }
                }
                catch (Exception ex2) {
                    Log.w("SupportSQLite", "delete failed: ", (Throwable)ex2);
                }
            }
        }
        
        public void onConfigure(final SupportSQLiteDatabase supportSQLiteDatabase) {
        }
        
        public void onCorruption(final SupportSQLiteDatabase p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: dup            
            //     4: invokespecial   java/lang/StringBuilder.<init>:()V
            //     7: astore_2       
            //     8: aload_2        
            //     9: ldc             "Corruption reported by sqlite on database: "
            //    11: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    14: pop            
            //    15: aload_2        
            //    16: aload_1        
            //    17: invokeinterface android/arch/persistence/db/SupportSQLiteDatabase.getPath:()Ljava/lang/String;
            //    22: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    25: pop            
            //    26: ldc             "SupportSQLite"
            //    28: aload_2        
            //    29: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //    32: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
            //    35: pop            
            //    36: aload_1        
            //    37: invokeinterface android/arch/persistence/db/SupportSQLiteDatabase.isOpen:()Z
            //    42: ifne            56
            //    45: aload_0        
            //    46: aload_1        
            //    47: invokeinterface android/arch/persistence/db/SupportSQLiteDatabase.getPath:()Ljava/lang/String;
            //    52: invokespecial   android/arch/persistence/db/SupportSQLiteOpenHelper$Callback.deleteDatabaseFile:(Ljava/lang/String;)V
            //    55: return         
            //    56: aconst_null    
            //    57: astore_2       
            //    58: aconst_null    
            //    59: astore_3       
            //    60: aload_1        
            //    61: invokeinterface android/arch/persistence/db/SupportSQLiteDatabase.getAttachedDbs:()Ljava/util/List;
            //    66: astore          4
            //    68: aload           4
            //    70: astore_2       
            //    71: goto            78
            //    74: astore_2       
            //    75: goto            89
            //    78: aload_2        
            //    79: astore_3       
            //    80: aload_1        
            //    81: invokeinterface android/arch/persistence/db/SupportSQLiteDatabase.close:()V
            //    86: goto            143
            //    89: aload_3        
            //    90: ifnull          131
            //    93: aload_3        
            //    94: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
            //    99: astore_1       
            //   100: aload_1        
            //   101: invokeinterface java/util/Iterator.hasNext:()Z
            //   106: ifeq            141
            //   109: aload_0        
            //   110: aload_1        
            //   111: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
            //   116: checkcast       Landroid/util/Pair;
            //   119: getfield        android/util/Pair.second:Ljava/lang/Object;
            //   122: checkcast       Ljava/lang/String;
            //   125: invokespecial   android/arch/persistence/db/SupportSQLiteOpenHelper$Callback.deleteDatabaseFile:(Ljava/lang/String;)V
            //   128: goto            100
            //   131: aload_0        
            //   132: aload_1        
            //   133: invokeinterface android/arch/persistence/db/SupportSQLiteDatabase.getPath:()Ljava/lang/String;
            //   138: invokespecial   android/arch/persistence/db/SupportSQLiteOpenHelper$Callback.deleteDatabaseFile:(Ljava/lang/String;)V
            //   141: aload_2        
            //   142: athrow         
            //   143: aload_2        
            //   144: ifnull          185
            //   147: aload_2        
            //   148: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
            //   153: astore_1       
            //   154: aload_1        
            //   155: invokeinterface java/util/Iterator.hasNext:()Z
            //   160: ifeq            195
            //   163: aload_0        
            //   164: aload_1        
            //   165: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
            //   170: checkcast       Landroid/util/Pair;
            //   173: getfield        android/util/Pair.second:Ljava/lang/Object;
            //   176: checkcast       Ljava/lang/String;
            //   179: invokespecial   android/arch/persistence/db/SupportSQLiteOpenHelper$Callback.deleteDatabaseFile:(Ljava/lang/String;)V
            //   182: goto            154
            //   185: aload_0        
            //   186: aload_1        
            //   187: invokeinterface android/arch/persistence/db/SupportSQLiteDatabase.getPath:()Ljava/lang/String;
            //   192: invokespecial   android/arch/persistence/db/SupportSQLiteOpenHelper$Callback.deleteDatabaseFile:(Ljava/lang/String;)V
            //   195: return         
            //   196: astore_3       
            //   197: goto            78
            //   200: astore_3       
            //   201: goto            143
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                                     
            //  -----  -----  -----  -----  -----------------------------------------
            //  60     68     196    200    Landroid/database/sqlite/SQLiteException;
            //  60     68     74     143    Any
            //  80     86     200    204    Ljava/io/IOException;
            //  80     86     74     143    Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0089:
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
        
        public abstract void onCreate(final SupportSQLiteDatabase p0);
        
        public void onDowngrade(final SupportSQLiteDatabase supportSQLiteDatabase, final int i, final int j) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Can't downgrade database from version ");
            sb.append(i);
            sb.append(" to ");
            sb.append(j);
            throw new SQLiteException(sb.toString());
        }
        
        public void onOpen(final SupportSQLiteDatabase supportSQLiteDatabase) {
        }
        
        public abstract void onUpgrade(final SupportSQLiteDatabase p0, final int p1, final int p2);
    }
    
    public static class Configuration
    {
        public final Callback callback;
        public final Context context;
        public final String name;
        
        Configuration(final Context context, final String name, final Callback callback) {
            this.context = context;
            this.name = name;
            this.callback = callback;
        }
        
        public static Builder builder(final Context context) {
            return new Builder(context);
        }
        
        public static class Builder
        {
            Callback mCallback;
            Context mContext;
            String mName;
            
            Builder(final Context mContext) {
                this.mContext = mContext;
            }
            
            public Configuration build() {
                if (this.mCallback == null) {
                    throw new IllegalArgumentException("Must set a callback to create the configuration.");
                }
                if (this.mContext != null) {
                    return new Configuration(this.mContext, this.mName, this.mCallback);
                }
                throw new IllegalArgumentException("Must set a non-null context to create the configuration.");
            }
            
            public Builder callback(final Callback mCallback) {
                this.mCallback = mCallback;
                return this;
            }
            
            public Builder name(final String mName) {
                this.mName = mName;
                return this;
            }
        }
    }
    
    public interface Factory
    {
        SupportSQLiteOpenHelper create(final Configuration p0);
    }
}
