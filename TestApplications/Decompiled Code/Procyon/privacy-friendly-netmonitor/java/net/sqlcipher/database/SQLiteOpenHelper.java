// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher.database;

import java.io.File;
import android.util.Log;
import net.sqlcipher.DefaultDatabaseErrorHandler;
import net.sqlcipher.DatabaseErrorHandler;
import android.content.Context;

public abstract class SQLiteOpenHelper
{
    private static final String TAG = "SQLiteOpenHelper";
    private final Context mContext;
    private SQLiteDatabase mDatabase;
    private final DatabaseErrorHandler mErrorHandler;
    private final SQLiteDatabase.CursorFactory mFactory;
    private final SQLiteDatabaseHook mHook;
    private boolean mIsInitializing;
    private final String mName;
    private final int mNewVersion;
    
    public SQLiteOpenHelper(final Context context, final String s, final SQLiteDatabase.CursorFactory cursorFactory, final int n) {
        this(context, s, cursorFactory, n, null, new DefaultDatabaseErrorHandler());
    }
    
    public SQLiteOpenHelper(final Context context, final String s, final SQLiteDatabase.CursorFactory cursorFactory, final int n, final SQLiteDatabaseHook sqLiteDatabaseHook) {
        this(context, s, cursorFactory, n, sqLiteDatabaseHook, new DefaultDatabaseErrorHandler());
    }
    
    public SQLiteOpenHelper(final Context mContext, final String mName, final SQLiteDatabase.CursorFactory mFactory, final int n, final SQLiteDatabaseHook mHook, final DatabaseErrorHandler mErrorHandler) {
        this.mDatabase = null;
        this.mIsInitializing = false;
        if (n < 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Version must be >= 1, was ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        if (mErrorHandler == null) {
            throw new IllegalArgumentException("DatabaseErrorHandler param value can't be null.");
        }
        this.mContext = mContext;
        this.mName = mName;
        this.mFactory = mFactory;
        this.mNewVersion = n;
        this.mHook = mHook;
        this.mErrorHandler = mErrorHandler;
    }
    
    public void close() {
        synchronized (this) {
            if (this.mIsInitializing) {
                throw new IllegalStateException("Closed during initialization");
            }
            if (this.mDatabase != null && this.mDatabase.isOpen()) {
                this.mDatabase.close();
                this.mDatabase = null;
            }
        }
    }
    
    public SQLiteDatabase getReadableDatabase(final String s) {
        // monitorenter(this)
        Label_0016: {
            if (s == null) {
                final char[] charArray = null;
                break Label_0016;
            }
            try {
                final char[] charArray = s.toCharArray();
                return this.getReadableDatabase(charArray);
            }
            finally {
            }
            // monitorexit(this)
        }
    }
    
    public SQLiteDatabase getReadableDatabase(final char[] array) {
        synchronized (this) {
            if (this.mDatabase != null && this.mDatabase.isOpen()) {
                return this.mDatabase;
            }
            if (this.mIsInitializing) {
                throw new IllegalStateException("getReadableDatabase called recursively");
            }
            try {
                return this.getWritableDatabase(array);
            }
            catch (SQLiteException ex) {
                if (this.mName == null) {
                    throw ex;
                }
                final String tag = SQLiteOpenHelper.TAG;
                final StringBuilder sb = new StringBuilder();
                sb.append("Couldn't open ");
                sb.append(this.mName);
                sb.append(" for writing (will try read-only):");
                Log.e(tag, sb.toString(), (Throwable)ex);
                final SQLiteDatabase sqLiteDatabase = null;
                Object writableDatabase = null;
                Object o = sqLiteDatabase;
                try {
                    this.mIsInitializing = true;
                    o = sqLiteDatabase;
                    final String path = this.mContext.getDatabasePath(this.mName).getPath();
                    o = sqLiteDatabase;
                    o = sqLiteDatabase;
                    final File file = new File(path);
                    o = sqLiteDatabase;
                    o = sqLiteDatabase;
                    final File file2 = new File(this.mContext.getDatabasePath(this.mName).getParent());
                    o = sqLiteDatabase;
                    if (!file2.exists()) {
                        o = sqLiteDatabase;
                        file2.mkdirs();
                    }
                    o = sqLiteDatabase;
                    if (!file.exists()) {
                        o = sqLiteDatabase;
                        this.mIsInitializing = false;
                        o = sqLiteDatabase;
                        writableDatabase = this.getWritableDatabase(array);
                        try {
                            this.mIsInitializing = true;
                            ((SQLiteDatabase)writableDatabase).close();
                        }
                        finally {
                            o = writableDatabase;
                        }
                    }
                    o = writableDatabase;
                    final SQLiteDatabase openDatabase = SQLiteDatabase.openDatabase(path, array, this.mFactory, 1);
                    try {
                        if (openDatabase.getVersion() != this.mNewVersion) {
                            o = new StringBuilder();
                            ((StringBuilder)o).append("Can't upgrade read-only database from version ");
                            ((StringBuilder)o).append(openDatabase.getVersion());
                            ((StringBuilder)o).append(" to ");
                            ((StringBuilder)o).append(this.mNewVersion);
                            ((StringBuilder)o).append(": ");
                            ((StringBuilder)o).append(path);
                            throw new SQLiteException(((StringBuilder)o).toString());
                        }
                        this.onOpen(openDatabase);
                        final String tag2 = SQLiteOpenHelper.TAG;
                        o = new StringBuilder();
                        ((StringBuilder)o).append("Opened ");
                        ((StringBuilder)o).append(this.mName);
                        ((StringBuilder)o).append(" in read-only mode");
                        Log.w(tag2, ((StringBuilder)o).toString());
                        this.mDatabase = openDatabase;
                        final SQLiteDatabase mDatabase = this.mDatabase;
                        this.mIsInitializing = false;
                        if (openDatabase != null && openDatabase != this.mDatabase) {
                            openDatabase.close();
                        }
                        return mDatabase;
                    }
                    finally {
                        o = openDatabase;
                    }
                }
                finally {}
                this.mIsInitializing = false;
                if (o != null && o != this.mDatabase) {
                    ((SQLiteDatabase)o).close();
                }
            }
        }
    }
    
    public SQLiteDatabase getWritableDatabase(final String s) {
        // monitorenter(this)
        Label_0016: {
            if (s == null) {
                final char[] charArray = null;
                break Label_0016;
            }
            try {
                final char[] charArray = s.toCharArray();
                return this.getWritableDatabase(charArray);
            }
            finally {
            }
            // monitorexit(this)
        }
    }
    
    public SQLiteDatabase getWritableDatabase(final char[] p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: monitorenter   
        //     2: aload_0        
        //     3: getfield        net/sqlcipher/database/SQLiteOpenHelper.mDatabase:Lnet/sqlcipher/database/SQLiteDatabase;
        //     6: ifnull          38
        //     9: aload_0        
        //    10: getfield        net/sqlcipher/database/SQLiteOpenHelper.mDatabase:Lnet/sqlcipher/database/SQLiteDatabase;
        //    13: invokevirtual   net/sqlcipher/database/SQLiteDatabase.isOpen:()Z
        //    16: ifeq            38
        //    19: aload_0        
        //    20: getfield        net/sqlcipher/database/SQLiteOpenHelper.mDatabase:Lnet/sqlcipher/database/SQLiteDatabase;
        //    23: invokevirtual   net/sqlcipher/database/SQLiteDatabase.isReadOnly:()Z
        //    26: ifne            38
        //    29: aload_0        
        //    30: getfield        net/sqlcipher/database/SQLiteOpenHelper.mDatabase:Lnet/sqlcipher/database/SQLiteDatabase;
        //    33: astore_1       
        //    34: aload_0        
        //    35: monitorexit    
        //    36: aload_1        
        //    37: areturn        
        //    38: aload_0        
        //    39: getfield        net/sqlcipher/database/SQLiteOpenHelper.mIsInitializing:Z
        //    42: ifeq            57
        //    45: new             Ljava/lang/IllegalStateException;
        //    48: astore_1       
        //    49: aload_1        
        //    50: ldc             "getWritableDatabase called recursively"
        //    52: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //    55: aload_1        
        //    56: athrow         
        //    57: aload_0        
        //    58: getfield        net/sqlcipher/database/SQLiteOpenHelper.mDatabase:Lnet/sqlcipher/database/SQLiteDatabase;
        //    61: ifnull          71
        //    64: aload_0        
        //    65: getfield        net/sqlcipher/database/SQLiteOpenHelper.mDatabase:Lnet/sqlcipher/database/SQLiteDatabase;
        //    68: invokevirtual   net/sqlcipher/database/SQLiteDatabase.lock:()V
        //    71: aconst_null    
        //    72: astore_2       
        //    73: aload_2        
        //    74: astore_3       
        //    75: aload_0        
        //    76: iconst_1       
        //    77: putfield        net/sqlcipher/database/SQLiteOpenHelper.mIsInitializing:Z
        //    80: aload_2        
        //    81: astore_3       
        //    82: aload_0        
        //    83: getfield        net/sqlcipher/database/SQLiteOpenHelper.mName:Ljava/lang/String;
        //    86: ifnonnull       100
        //    89: aload_2        
        //    90: astore_3       
        //    91: aconst_null    
        //    92: aload_1        
        //    93: invokestatic    net/sqlcipher/database/SQLiteDatabase.create:(Lnet/sqlcipher/database/SQLiteDatabase$CursorFactory;[C)Lnet/sqlcipher/database/SQLiteDatabase;
        //    96: astore_1       
        //    97: goto            179
        //   100: aload_2        
        //   101: astore_3       
        //   102: aload_0        
        //   103: getfield        net/sqlcipher/database/SQLiteOpenHelper.mContext:Landroid/content/Context;
        //   106: aload_0        
        //   107: getfield        net/sqlcipher/database/SQLiteOpenHelper.mName:Ljava/lang/String;
        //   110: invokevirtual   android/content/Context.getDatabasePath:(Ljava/lang/String;)Ljava/io/File;
        //   113: invokevirtual   java/io/File.getPath:()Ljava/lang/String;
        //   116: astore          4
        //   118: aload_2        
        //   119: astore_3       
        //   120: new             Ljava/io/File;
        //   123: astore          5
        //   125: aload_2        
        //   126: astore_3       
        //   127: aload           5
        //   129: aload           4
        //   131: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   134: aload_2        
        //   135: astore_3       
        //   136: aload           5
        //   138: invokevirtual   java/io/File.exists:()Z
        //   141: ifne            155
        //   144: aload_2        
        //   145: astore_3       
        //   146: aload           5
        //   148: invokevirtual   java/io/File.getParentFile:()Ljava/io/File;
        //   151: invokevirtual   java/io/File.mkdirs:()Z
        //   154: pop            
        //   155: aload_2        
        //   156: astore_3       
        //   157: aload           4
        //   159: aload_1        
        //   160: aload_0        
        //   161: getfield        net/sqlcipher/database/SQLiteOpenHelper.mFactory:Lnet/sqlcipher/database/SQLiteDatabase$CursorFactory;
        //   164: aload_0        
        //   165: getfield        net/sqlcipher/database/SQLiteOpenHelper.mHook:Lnet/sqlcipher/database/SQLiteDatabaseHook;
        //   168: aload_0        
        //   169: getfield        net/sqlcipher/database/SQLiteOpenHelper.mErrorHandler:Lnet/sqlcipher/DatabaseErrorHandler;
        //   172: invokestatic    net/sqlcipher/database/SQLiteDatabase.openOrCreateDatabase:(Ljava/lang/String;[CLnet/sqlcipher/database/SQLiteDatabase$CursorFactory;Lnet/sqlcipher/database/SQLiteDatabaseHook;Lnet/sqlcipher/DatabaseErrorHandler;)Lnet/sqlcipher/database/SQLiteDatabase;
        //   175: astore_1       
        //   176: goto            97
        //   179: aload_1        
        //   180: astore_3       
        //   181: aload_1        
        //   182: invokevirtual   net/sqlcipher/database/SQLiteDatabase.getVersion:()I
        //   185: istore          6
        //   187: aload_1        
        //   188: astore_3       
        //   189: iload           6
        //   191: aload_0        
        //   192: getfield        net/sqlcipher/database/SQLiteOpenHelper.mNewVersion:I
        //   195: if_icmpeq       263
        //   198: aload_1        
        //   199: astore_3       
        //   200: aload_1        
        //   201: invokevirtual   net/sqlcipher/database/SQLiteDatabase.beginTransaction:()V
        //   204: iload           6
        //   206: ifne            221
        //   209: aload_0        
        //   210: aload_1        
        //   211: invokevirtual   net/sqlcipher/database/SQLiteOpenHelper.onCreate:(Lnet/sqlcipher/database/SQLiteDatabase;)V
        //   214: goto            232
        //   217: astore_2       
        //   218: goto            253
        //   221: aload_0        
        //   222: aload_1        
        //   223: iload           6
        //   225: aload_0        
        //   226: getfield        net/sqlcipher/database/SQLiteOpenHelper.mNewVersion:I
        //   229: invokevirtual   net/sqlcipher/database/SQLiteOpenHelper.onUpgrade:(Lnet/sqlcipher/database/SQLiteDatabase;II)V
        //   232: aload_1        
        //   233: aload_0        
        //   234: getfield        net/sqlcipher/database/SQLiteOpenHelper.mNewVersion:I
        //   237: invokevirtual   net/sqlcipher/database/SQLiteDatabase.setVersion:(I)V
        //   240: aload_1        
        //   241: invokevirtual   net/sqlcipher/database/SQLiteDatabase.setTransactionSuccessful:()V
        //   244: aload_1        
        //   245: astore_3       
        //   246: aload_1        
        //   247: invokevirtual   net/sqlcipher/database/SQLiteDatabase.endTransaction:()V
        //   250: goto            263
        //   253: aload_1        
        //   254: astore_3       
        //   255: aload_1        
        //   256: invokevirtual   net/sqlcipher/database/SQLiteDatabase.endTransaction:()V
        //   259: aload_1        
        //   260: astore_3       
        //   261: aload_2        
        //   262: athrow         
        //   263: aload_1        
        //   264: astore_3       
        //   265: aload_0        
        //   266: aload_1        
        //   267: invokevirtual   net/sqlcipher/database/SQLiteOpenHelper.onOpen:(Lnet/sqlcipher/database/SQLiteDatabase;)V
        //   270: aload_0        
        //   271: iconst_0       
        //   272: putfield        net/sqlcipher/database/SQLiteOpenHelper.mIsInitializing:Z
        //   275: aload_0        
        //   276: getfield        net/sqlcipher/database/SQLiteOpenHelper.mDatabase:Lnet/sqlcipher/database/SQLiteDatabase;
        //   279: astore_3       
        //   280: aload_3        
        //   281: ifnull          298
        //   284: aload_0        
        //   285: getfield        net/sqlcipher/database/SQLiteOpenHelper.mDatabase:Lnet/sqlcipher/database/SQLiteDatabase;
        //   288: invokevirtual   net/sqlcipher/database/SQLiteDatabase.close:()V
        //   291: aload_0        
        //   292: getfield        net/sqlcipher/database/SQLiteOpenHelper.mDatabase:Lnet/sqlcipher/database/SQLiteDatabase;
        //   295: invokevirtual   net/sqlcipher/database/SQLiteDatabase.unlock:()V
        //   298: aload_0        
        //   299: aload_1        
        //   300: putfield        net/sqlcipher/database/SQLiteOpenHelper.mDatabase:Lnet/sqlcipher/database/SQLiteDatabase;
        //   303: aload_0        
        //   304: monitorexit    
        //   305: aload_1        
        //   306: areturn        
        //   307: astore_1       
        //   308: aload_0        
        //   309: iconst_0       
        //   310: putfield        net/sqlcipher/database/SQLiteOpenHelper.mIsInitializing:Z
        //   313: aload_0        
        //   314: getfield        net/sqlcipher/database/SQLiteOpenHelper.mDatabase:Lnet/sqlcipher/database/SQLiteDatabase;
        //   317: ifnull          327
        //   320: aload_0        
        //   321: getfield        net/sqlcipher/database/SQLiteOpenHelper.mDatabase:Lnet/sqlcipher/database/SQLiteDatabase;
        //   324: invokevirtual   net/sqlcipher/database/SQLiteDatabase.unlock:()V
        //   327: aload_3        
        //   328: ifnull          335
        //   331: aload_3        
        //   332: invokevirtual   net/sqlcipher/database/SQLiteDatabase.close:()V
        //   335: aload_1        
        //   336: athrow         
        //   337: astore_1       
        //   338: aload_0        
        //   339: monitorexit    
        //   340: aload_1        
        //   341: athrow         
        //   342: astore_3       
        //   343: goto            291
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  2      34     337    342    Any
        //  38     57     337    342    Any
        //  57     71     337    342    Any
        //  75     80     307    337    Any
        //  82     89     307    337    Any
        //  91     97     307    337    Any
        //  102    118    307    337    Any
        //  120    125    307    337    Any
        //  127    134    307    337    Any
        //  136    144    307    337    Any
        //  146    155    307    337    Any
        //  157    176    307    337    Any
        //  181    187    307    337    Any
        //  189    198    307    337    Any
        //  200    204    307    337    Any
        //  209    214    217    263    Any
        //  221    232    217    263    Any
        //  232    244    217    263    Any
        //  246    250    307    337    Any
        //  255    259    307    337    Any
        //  261    263    307    337    Any
        //  265    270    307    337    Any
        //  270    280    337    342    Any
        //  284    291    342    346    Ljava/lang/Exception;
        //  284    291    337    342    Any
        //  291    298    337    342    Any
        //  298    303    337    342    Any
        //  308    327    337    342    Any
        //  331    335    337    342    Any
        //  335    337    337    342    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0291:
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
    
    public abstract void onCreate(final SQLiteDatabase p0);
    
    public void onOpen(final SQLiteDatabase sqLiteDatabase) {
    }
    
    public abstract void onUpgrade(final SQLiteDatabase p0, final int p1, final int p2);
}
