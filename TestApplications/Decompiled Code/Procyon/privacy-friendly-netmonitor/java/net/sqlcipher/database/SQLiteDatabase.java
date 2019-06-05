// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher.database;

import net.sqlcipher.CursorWrapper;
import net.sqlcipher.CrossProcessCursorWrapper;
import java.util.Set;
import android.content.ContentValues;
import net.sqlcipher.DatabaseUtils;
import net.sqlcipher.DefaultDatabaseErrorHandler;
import java.io.File;
import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import net.sqlcipher.Cursor;
import android.util.Pair;
import java.util.Collection;
import java.util.ArrayList;
import android.text.TextUtils;
import java.util.Iterator;
import android.os.Debug;
import android.util.Log;
import android.os.SystemClock;
import net.sqlcipher.SQLException;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import net.sqlcipher.DatabaseErrorHandler;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

public class SQLiteDatabase extends SQLiteClosable
{
    private static final String COMMIT_SQL = "COMMIT;";
    public static final int CONFLICT_ABORT = 2;
    public static final int CONFLICT_FAIL = 3;
    public static final int CONFLICT_IGNORE = 4;
    public static final int CONFLICT_NONE = 0;
    public static final int CONFLICT_REPLACE = 5;
    public static final int CONFLICT_ROLLBACK = 1;
    private static final String[] CONFLICT_VALUES;
    public static final int CREATE_IF_NECESSARY = 268435456;
    private static final Pattern EMAIL_IN_DB_PATTERN;
    private static final int EVENT_DB_CORRUPT = 75004;
    private static final int EVENT_DB_OPERATION = 52000;
    static final String GET_LOCK_LOG_PREFIX = "GETLOCK:";
    private static final String KEY_ENCODING = "UTF-8";
    private static final int LOCK_ACQUIRED_WARNING_THREAD_TIME_IN_MS = 100;
    private static final int LOCK_ACQUIRED_WARNING_TIME_IN_MS = 300;
    private static final int LOCK_ACQUIRED_WARNING_TIME_IN_MS_ALWAYS_PRINT = 2000;
    private static final int LOCK_WARNING_WINDOW_IN_MS = 20000;
    private static final String LOG_SLOW_QUERIES_PROPERTY = "db.log.slow_query_threshold";
    public static final int MAX_SQL_CACHE_SIZE = 250;
    private static final int MAX_WARNINGS_ON_CACHESIZE_CONDITION = 1;
    public static final String MEMORY = ":memory:";
    public static final int NO_LOCALIZED_COLLATORS = 16;
    public static final int OPEN_READONLY = 1;
    public static final int OPEN_READWRITE = 0;
    private static final int OPEN_READ_MASK = 1;
    private static final int QUERY_LOG_SQL_LENGTH = 64;
    private static final int SLEEP_AFTER_YIELD_QUANTUM = 1000;
    public static final String SQLCIPHER_ANDROID_VERSION = "3.5.7";
    public static final int SQLITE_MAX_LIKE_PATTERN_LENGTH = 50000;
    private static final String TAG = "Database";
    private static WeakHashMap<SQLiteDatabase, Object> sActiveDatabases;
    private static int sQueryLogTimeInMillis;
    private int mCacheFullWarnings;
    Map<String, SQLiteCompiledSql> mCompiledQueries;
    private final DatabaseErrorHandler mErrorHandler;
    private CursorFactory mFactory;
    private int mFlags;
    private boolean mInnerTransactionIsSuccessful;
    private long mLastLockMessageTime;
    private String mLastSqlStatement;
    private final ReentrantLock mLock;
    private long mLockAcquiredThreadTime;
    private long mLockAcquiredWallTime;
    private boolean mLockingEnabled;
    private int mMaxSqlCacheSize;
    long mNativeHandle;
    private int mNumCacheHits;
    private int mNumCacheMisses;
    private String mPath;
    private String mPathForLogs;
    private WeakHashMap<SQLiteClosable, Object> mPrograms;
    private final int mSlowQueryThreshold;
    private Throwable mStackTrace;
    private final Map<String, SyncUpdateInfo> mSyncUpdateInfo;
    int mTempTableSequence;
    private String mTimeClosed;
    private String mTimeOpened;
    private boolean mTransactionIsSuccessful;
    private SQLiteTransactionListener mTransactionListener;
    
    static {
        SQLiteDatabase.sActiveDatabases = new WeakHashMap<SQLiteDatabase, Object>();
        CONFLICT_VALUES = new String[] { "", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE " };
        EMAIL_IN_DB_PATTERN = Pattern.compile("[\\w\\.\\-]+@[\\w\\.\\-]+");
    }
    
    private SQLiteDatabase(final String mPath, final CursorFactory mFactory, final int mFlags, final DatabaseErrorHandler mErrorHandler) {
        this.mLock = new ReentrantLock(true);
        this.mLockAcquiredWallTime = 0L;
        this.mLockAcquiredThreadTime = 0L;
        this.mLastLockMessageTime = 0L;
        this.mLastSqlStatement = null;
        this.mNativeHandle = 0L;
        this.mTempTableSequence = 0;
        this.mPathForLogs = null;
        this.mCompiledQueries = new HashMap<String, SQLiteCompiledSql>();
        this.mMaxSqlCacheSize = 250;
        this.mTimeOpened = null;
        this.mTimeClosed = null;
        this.mStackTrace = null;
        this.mLockingEnabled = true;
        this.mSyncUpdateInfo = new HashMap<String, SyncUpdateInfo>();
        if (mPath == null) {
            throw new IllegalArgumentException("path should not be null");
        }
        this.mFlags = mFlags;
        this.mPath = mPath;
        this.mSlowQueryThreshold = -1;
        this.mStackTrace = new DatabaseObjectNotClosedException().fillInStackTrace();
        this.mFactory = mFactory;
        this.mPrograms = new WeakHashMap<SQLiteClosable, Object>();
        this.mErrorHandler = mErrorHandler;
    }
    
    public SQLiteDatabase(final String s, final char[] array, final CursorFactory cursorFactory, final int n) {
        this(s, cursorFactory, n, null);
        this.openDatabaseInternal(array, null);
    }
    
    public SQLiteDatabase(final String s, final char[] array, final CursorFactory cursorFactory, final int n, final SQLiteDatabaseHook sqLiteDatabaseHook) {
        this(s, cursorFactory, n, null);
        this.openDatabaseInternal(array, sqLiteDatabaseHook);
    }
    
    private void checkLockHoldTime() {
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        final long lng = elapsedRealtime - this.mLockAcquiredWallTime;
        if (lng < 2000L && !Log.isLoggable("Database", 2) && elapsedRealtime - this.mLastLockMessageTime < 20000L) {
            return;
        }
        if (lng > 300L) {
            final int i = (int)((Debug.threadCpuTimeNanos() - this.mLockAcquiredThreadTime) / 1000000L);
            if (i > 100 || lng > 2000L) {
                this.mLastLockMessageTime = elapsedRealtime;
                final StringBuilder sb = new StringBuilder();
                sb.append("lock held on ");
                sb.append(this.mPath);
                sb.append(" for ");
                sb.append(lng);
                sb.append("ms. Thread time was ");
                sb.append(i);
                sb.append("ms");
                final String string = sb.toString();
                if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING_STACK_TRACE) {
                    Log.d("Database", string, (Throwable)new Exception());
                }
                else {
                    Log.d("Database", string);
                }
            }
        }
    }
    
    private void closeClosable() {
        this.deallocCachedSqlStatements();
        final Iterator<Map.Entry<SQLiteClosable, Object>> iterator = this.mPrograms.entrySet().iterator();
        while (iterator.hasNext()) {
            final SQLiteClosable sqLiteClosable = iterator.next().getKey();
            if (sqLiteClosable != null) {
                sqLiteClosable.onAllReferencesReleasedFromContainer();
            }
        }
    }
    
    private boolean containsNull(final char[] array) {
        boolean b2;
        final boolean b = b2 = false;
        if (array != null) {
            b2 = b;
            if (array.length > 0) {
                final int length = array.length;
                int n = 0;
                while (true) {
                    b2 = b;
                    if (n >= length) {
                        break;
                    }
                    if (array[n] == '\0') {
                        b2 = true;
                        break;
                    }
                    ++n;
                }
            }
        }
        return b2;
    }
    
    public static SQLiteDatabase create(final CursorFactory cursorFactory, final String s) {
        char[] charArray;
        if (s == null) {
            charArray = null;
        }
        else {
            charArray = s.toCharArray();
        }
        return openDatabase(":memory:", charArray, cursorFactory, 268435456);
    }
    
    public static SQLiteDatabase create(final CursorFactory cursorFactory, final char[] array) {
        return openDatabase(":memory:", array, cursorFactory, 268435456);
    }
    
    private native void dbclose();
    
    private native void dbopen(final String p0, final int p1);
    
    private void deallocCachedSqlStatements() {
        synchronized (this.mCompiledQueries) {
            final Iterator<SQLiteCompiledSql> iterator = this.mCompiledQueries.values().iterator();
            while (iterator.hasNext()) {
                iterator.next().releaseSqlStatement();
            }
            this.mCompiledQueries.clear();
        }
    }
    
    private native void enableSqlProfiling(final String p0);
    
    private native void enableSqlTracing(final String p0);
    
    public static String findEditTable(final String s) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalStateException("Invalid tables");
        }
        final int index = s.indexOf(32);
        final int index2 = s.indexOf(44);
        if (index > 0 && (index < index2 || index2 < 0)) {
            return s.substring(0, index);
        }
        if (index2 > 0 && (index2 < index || index < 0)) {
            return s.substring(0, index2);
        }
        return s;
    }
    
    private static ArrayList<SQLiteDatabase> getActiveDatabases() {
        final ArrayList<SQLiteDatabase> list = new ArrayList<SQLiteDatabase>();
        synchronized (SQLiteDatabase.sActiveDatabases) {
            list.addAll(SQLiteDatabase.sActiveDatabases.keySet());
            return list;
        }
    }
    
    private static ArrayList<Pair<String, String>> getAttachedDbs(final SQLiteDatabase sqLiteDatabase) {
        if (!sqLiteDatabase.isOpen()) {
            return null;
        }
        final ArrayList<Pair> list = (ArrayList<Pair>)new ArrayList<Pair<String, String>>();
        final Cursor rawQuery = sqLiteDatabase.rawQuery("pragma database_list;", null);
        while (rawQuery.moveToNext()) {
            list.add(new Pair((Object)rawQuery.getString(1), (Object)rawQuery.getString(2)));
        }
        rawQuery.close();
        return (ArrayList<Pair<String, String>>)list;
    }
    
    private byte[] getBytes(final char[] array) {
        if (array != null && array.length != 0) {
            final ByteBuffer encode = Charset.forName("UTF-8").encode(CharBuffer.wrap(array));
            final byte[] dst = new byte[encode.limit()];
            encode.get(dst);
            return dst;
        }
        return null;
    }
    
    static ArrayList<SQLiteDebug.DbStats> getDbStats() {
        final ArrayList<SQLiteDebug.DbStats> list = new ArrayList<SQLiteDebug.DbStats>();
        for (final SQLiteDatabase sqLiteDatabase : getActiveDatabases()) {
            if (sqLiteDatabase != null) {
                if (!sqLiteDatabase.isOpen()) {
                    continue;
                }
                int native_getDbLookaside = sqLiteDatabase.native_getDbLookaside();
                final String path = sqLiteDatabase.getPath();
                int lastIndex = path.lastIndexOf("/");
                if (lastIndex != -1) {
                    ++lastIndex;
                }
                else {
                    lastIndex = 0;
                }
                final String substring = path.substring(lastIndex);
                final ArrayList<Pair<String, String>> attachedDbs = getAttachedDbs(sqLiteDatabase);
                if (attachedDbs == null) {
                    continue;
                }
                for (int i = 0; i < attachedDbs.size(); ++i) {
                    final Pair<String, String> pair = attachedDbs.get(i);
                    final StringBuilder sb = new StringBuilder();
                    sb.append((String)pair.first);
                    sb.append(".page_count;");
                    final long pragmaVal = getPragmaVal(sqLiteDatabase, sb.toString());
                    String str;
                    if (i == 0) {
                        str = substring;
                    }
                    else {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("  (attached) ");
                        sb2.append((String)pair.first);
                        str = sb2.toString();
                        if (((String)pair.second).trim().length() > 0) {
                            int lastIndex2 = ((String)pair.second).lastIndexOf("/");
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append(str);
                            sb3.append(" : ");
                            final String s = (String)pair.second;
                            if (lastIndex2 != -1) {
                                ++lastIndex2;
                            }
                            else {
                                lastIndex2 = 0;
                            }
                            sb3.append(s.substring(lastIndex2));
                            str = sb3.toString();
                        }
                        native_getDbLookaside = 0;
                    }
                    if (pragmaVal > 0L) {
                        list.add(new SQLiteDebug.DbStats(str, pragmaVal, sqLiteDatabase.getPageSize(), native_getDbLookaside));
                    }
                }
            }
        }
        return list;
    }
    
    private String getPathForLogs() {
        if (this.mPathForLogs != null) {
            return this.mPathForLogs;
        }
        if (this.mPath == null) {
            return null;
        }
        if (this.mPath.indexOf(64) == -1) {
            this.mPathForLogs = this.mPath;
        }
        else {
            this.mPathForLogs = SQLiteDatabase.EMAIL_IN_DB_PATTERN.matcher(this.mPath).replaceAll("XX@YY");
        }
        return this.mPathForLogs;
    }
    
    private static long getPragmaVal(final SQLiteDatabase sqLiteDatabase, final String str) {
        if (!sqLiteDatabase.isOpen()) {
            return 0L;
        }
        final SQLiteProgram sqLiteProgram = null;
        SQLiteProgram sqLiteProgram2;
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("PRAGMA ");
            sb.append(str);
            final SQLiteStatement sqLiteStatement = new SQLiteStatement(sqLiteDatabase, sb.toString());
            try {
                final long simpleQueryForLong = sqLiteStatement.simpleQueryForLong();
                if (sqLiteStatement != null) {
                    sqLiteStatement.close();
                }
                return simpleQueryForLong;
            }
            finally {}
        }
        finally {
            sqLiteProgram2 = sqLiteProgram;
        }
        if (sqLiteProgram2 != null) {
            sqLiteProgram2.close();
        }
    }
    
    private String getTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ", Locale.US).format(System.currentTimeMillis());
    }
    
    private native void key(final byte[] p0) throws SQLException;
    
    private void keyDatabase(final SQLiteDatabaseHook sqLiteDatabaseHook, final Runnable runnable) {
        if (sqLiteDatabaseHook != null) {
            sqLiteDatabaseHook.preKey(this);
        }
        if (runnable != null) {
            runnable.run();
        }
        if (sqLiteDatabaseHook != null) {
            sqLiteDatabaseHook.postKey(this);
        }
        if (SQLiteDebug.DEBUG_SQL_CACHE) {
            this.mTimeOpened = this.getTime();
        }
        try {
            final Cursor rawQuery = this.rawQuery("select count(*) from sqlite_master;", new String[0]);
            if (rawQuery != null) {
                rawQuery.moveToFirst();
                rawQuery.getInt(0);
                rawQuery.close();
            }
        }
        catch (RuntimeException ex) {
            Log.e("Database", ex.getMessage(), (Throwable)ex);
            throw ex;
        }
    }
    
    private native void key_mutf8(final char[] p0) throws SQLException;
    
    private static void loadICUData(final Context p0, final File p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: aload_1        
        //     5: ldc_w           "icu"
        //     8: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    11: astore_1       
        //    12: new             Ljava/io/File;
        //    15: dup            
        //    16: aload_1        
        //    17: ldc_w           "icudt46l.dat"
        //    20: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    23: astore_2       
        //    24: aconst_null    
        //    25: astore_3       
        //    26: aconst_null    
        //    27: astore          4
        //    29: aconst_null    
        //    30: astore          5
        //    32: aload_1        
        //    33: invokevirtual   java/io/File.exists:()Z
        //    36: ifne            44
        //    39: aload_1        
        //    40: invokevirtual   java/io/File.mkdirs:()Z
        //    43: pop            
        //    44: aload_2        
        //    45: invokevirtual   java/io/File.exists:()Z
        //    48: ifne            164
        //    51: new             Ljava/util/zip/ZipInputStream;
        //    54: astore_1       
        //    55: aload_1        
        //    56: aload_0        
        //    57: invokevirtual   android/content/Context.getAssets:()Landroid/content/res/AssetManager;
        //    60: ldc_w           "icudt46l.zip"
        //    63: invokevirtual   android/content/res/AssetManager.open:(Ljava/lang/String;)Ljava/io/InputStream;
        //    66: invokespecial   java/util/zip/ZipInputStream.<init>:(Ljava/io/InputStream;)V
        //    69: aload_1        
        //    70: astore_0       
        //    71: aload_3        
        //    72: astore          4
        //    74: aload_1        
        //    75: invokevirtual   java/util/zip/ZipInputStream.getNextEntry:()Ljava/util/zip/ZipEntry;
        //    78: pop            
        //    79: aload_1        
        //    80: astore_0       
        //    81: aload_3        
        //    82: astore          4
        //    84: new             Ljava/io/FileOutputStream;
        //    87: astore          6
        //    89: aload_1        
        //    90: astore_0       
        //    91: aload_3        
        //    92: astore          4
        //    94: aload           6
        //    96: aload_2        
        //    97: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //   100: sipush          1024
        //   103: newarray        B
        //   105: astore_3       
        //   106: aload_1        
        //   107: aload_3        
        //   108: invokevirtual   java/util/zip/ZipInputStream.read:([B)I
        //   111: istore          7
        //   113: aload_1        
        //   114: astore          4
        //   116: aload           6
        //   118: astore_0       
        //   119: iload           7
        //   121: ifle            169
        //   124: aload           6
        //   126: aload_3        
        //   127: iconst_0       
        //   128: iload           7
        //   130: invokevirtual   java/io/OutputStream.write:([BII)V
        //   133: goto            106
        //   136: astore_0       
        //   137: aload           6
        //   139: astore          4
        //   141: aload_0        
        //   142: astore          6
        //   144: aload_1        
        //   145: astore_0       
        //   146: aload           6
        //   148: astore_1       
        //   149: goto            307
        //   152: astore_3       
        //   153: goto            234
        //   156: astore_3       
        //   157: aload           5
        //   159: astore          6
        //   161: goto            234
        //   164: aconst_null    
        //   165: astore_0       
        //   166: aload_0        
        //   167: astore          4
        //   169: aload           4
        //   171: ifnull          186
        //   174: aload           4
        //   176: invokevirtual   java/util/zip/ZipInputStream.close:()V
        //   179: goto            186
        //   182: astore_0       
        //   183: goto            201
        //   186: aload_0        
        //   187: ifnull          220
        //   190: aload_0        
        //   191: invokevirtual   java/io/OutputStream.flush:()V
        //   194: aload_0        
        //   195: invokevirtual   java/io/OutputStream.close:()V
        //   198: goto            220
        //   201: ldc             "Database"
        //   203: ldc_w           "Error in closing streams IO streams after expanding ICU dat file"
        //   206: aload_0        
        //   207: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   210: pop            
        //   211: new             Ljava/lang/RuntimeException;
        //   214: dup            
        //   215: aload_0        
        //   216: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/Throwable;)V
        //   219: athrow         
        //   220: return         
        //   221: astore_1       
        //   222: aconst_null    
        //   223: astore_0       
        //   224: goto            307
        //   227: astore_3       
        //   228: aconst_null    
        //   229: astore_1       
        //   230: aload           5
        //   232: astore          6
        //   234: aload_1        
        //   235: astore_0       
        //   236: aload           6
        //   238: astore          4
        //   240: ldc             "Database"
        //   242: ldc_w           "Error copying icu dat file"
        //   245: aload_3        
        //   246: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   249: pop            
        //   250: aload_1        
        //   251: astore_0       
        //   252: aload           6
        //   254: astore          4
        //   256: aload_2        
        //   257: invokevirtual   java/io/File.exists:()Z
        //   260: ifeq            274
        //   263: aload_1        
        //   264: astore_0       
        //   265: aload           6
        //   267: astore          4
        //   269: aload_2        
        //   270: invokevirtual   java/io/File.delete:()Z
        //   273: pop            
        //   274: aload_1        
        //   275: astore_0       
        //   276: aload           6
        //   278: astore          4
        //   280: new             Ljava/lang/RuntimeException;
        //   283: astore          5
        //   285: aload_1        
        //   286: astore_0       
        //   287: aload           6
        //   289: astore          4
        //   291: aload           5
        //   293: aload_3        
        //   294: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/Throwable;)V
        //   297: aload_1        
        //   298: astore_0       
        //   299: aload           6
        //   301: astore          4
        //   303: aload           5
        //   305: athrow         
        //   306: astore_1       
        //   307: aload_0        
        //   308: ifnull          322
        //   311: aload_0        
        //   312: invokevirtual   java/util/zip/ZipInputStream.close:()V
        //   315: goto            322
        //   318: astore_0       
        //   319: goto            340
        //   322: aload           4
        //   324: ifnull          359
        //   327: aload           4
        //   329: invokevirtual   java/io/OutputStream.flush:()V
        //   332: aload           4
        //   334: invokevirtual   java/io/OutputStream.close:()V
        //   337: goto            359
        //   340: ldc             "Database"
        //   342: ldc_w           "Error in closing streams IO streams after expanding ICU dat file"
        //   345: aload_0        
        //   346: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   349: pop            
        //   350: new             Ljava/lang/RuntimeException;
        //   353: dup            
        //   354: aload_0        
        //   355: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/Throwable;)V
        //   358: athrow         
        //   359: aload_1        
        //   360: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  32     44     227    234    Ljava/lang/Exception;
        //  32     44     221    227    Any
        //  44     69     227    234    Ljava/lang/Exception;
        //  44     69     221    227    Any
        //  74     79     156    164    Ljava/lang/Exception;
        //  74     79     306    307    Any
        //  84     89     156    164    Ljava/lang/Exception;
        //  84     89     306    307    Any
        //  94     100    156    164    Ljava/lang/Exception;
        //  94     100    306    307    Any
        //  100    106    152    156    Ljava/lang/Exception;
        //  100    106    136    152    Any
        //  106    113    152    156    Ljava/lang/Exception;
        //  106    113    136    152    Any
        //  124    133    152    156    Ljava/lang/Exception;
        //  124    133    136    152    Any
        //  174    179    182    186    Ljava/io/IOException;
        //  190    198    182    186    Ljava/io/IOException;
        //  240    250    306    307    Any
        //  256    263    306    307    Any
        //  269    274    306    307    Any
        //  280    285    306    307    Any
        //  291    297    306    307    Any
        //  303    306    306    307    Any
        //  311    315    318    322    Ljava/io/IOException;
        //  327    337    318    322    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0106:
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
    
    public static void loadLibs(final Context context) {
        synchronized (SQLiteDatabase.class) {
            loadLibs(context, context.getFilesDir());
        }
    }
    
    public static void loadLibs(final Context context, final File file) {
        synchronized (SQLiteDatabase.class) {
            loadLibs(context, file, (LibraryLoader)new LibraryLoader() {
                @Override
                public void loadLibraries(final String... array) {
                    for (int i = 0; i < array.length; ++i) {
                        System.loadLibrary(array[i]);
                    }
                }
            });
        }
    }
    
    public static void loadLibs(final Context context, final File file, final LibraryLoader libraryLoader) {
        synchronized (SQLiteDatabase.class) {
            libraryLoader.loadLibraries("sqlcipher");
        }
    }
    
    public static void loadLibs(final Context context, final LibraryLoader libraryLoader) {
        synchronized (SQLiteDatabase.class) {
            loadLibs(context, context.getFilesDir(), libraryLoader);
        }
    }
    
    private void lockForced() {
        this.mLock.lock();
        if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING && this.mLock.getHoldCount() == 1) {
            this.mLockAcquiredWallTime = SystemClock.elapsedRealtime();
            this.mLockAcquiredThreadTime = Debug.threadCpuTimeNanos();
        }
    }
    
    private void markTableSyncable(final String str, final String str2, final String str3, final String s) {
        this.lock();
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("SELECT _sync_dirty FROM ");
            sb.append(str3);
            sb.append(" LIMIT 0");
            this.native_execSQL(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("SELECT ");
            sb2.append(str2);
            sb2.append(" FROM ");
            sb2.append(str);
            sb2.append(" LIMIT 0");
            this.native_execSQL(sb2.toString());
            this.unlock();
            final SyncUpdateInfo syncUpdateInfo = new SyncUpdateInfo(str3, s, str2);
            synchronized (this.mSyncUpdateInfo) {
                this.mSyncUpdateInfo.put(str, syncUpdateInfo);
            }
        }
        finally {
            this.unlock();
        }
    }
    
    private native int native_getDbLookaside();
    
    private native void native_key(final char[] p0) throws SQLException;
    
    private native void native_rawExecSQL(final String p0);
    
    private native void native_rekey(final String p0) throws SQLException;
    
    private native int native_status(final int p0, final boolean p1);
    
    public static SQLiteDatabase openDatabase(final String s, final String s2, final CursorFactory cursorFactory, final int n) {
        return openDatabase(s, s2, cursorFactory, n, null);
    }
    
    public static SQLiteDatabase openDatabase(final String s, final String s2, final CursorFactory cursorFactory, final int n, final SQLiteDatabaseHook sqLiteDatabaseHook) {
        return openDatabase(s, s2, cursorFactory, n, sqLiteDatabaseHook, null);
    }
    
    public static SQLiteDatabase openDatabase(final String s, final String s2, final CursorFactory cursorFactory, final int n, final SQLiteDatabaseHook sqLiteDatabaseHook, final DatabaseErrorHandler databaseErrorHandler) {
        char[] charArray;
        if (s2 == null) {
            charArray = null;
        }
        else {
            charArray = s2.toCharArray();
        }
        return openDatabase(s, charArray, cursorFactory, n, sqLiteDatabaseHook, databaseErrorHandler);
    }
    
    public static SQLiteDatabase openDatabase(final String s, final char[] array, final CursorFactory cursorFactory, final int n) {
        return openDatabase(s, array, cursorFactory, n, null, null);
    }
    
    public static SQLiteDatabase openDatabase(final String s, final char[] array, final CursorFactory cursorFactory, final int n, final SQLiteDatabaseHook sqLiteDatabaseHook) {
        return openDatabase(s, array, cursorFactory, n, sqLiteDatabaseHook, null);
    }
    
    public static SQLiteDatabase openDatabase(final String str, final char[] array, final CursorFactory cursorFactory, final int n, final SQLiteDatabaseHook sqLiteDatabaseHook, DatabaseErrorHandler databaseErrorHandler) {
        if (databaseErrorHandler == null) {
            databaseErrorHandler = new DefaultDatabaseErrorHandler();
        }
        SQLiteDatabase key;
        try {
            key = new SQLiteDatabase(str, cursorFactory, n, databaseErrorHandler);
            try {
                key.openDatabaseInternal(array, sqLiteDatabaseHook);
            }
            catch (SQLiteDatabaseCorruptException ex) {}
        }
        catch (SQLiteDatabaseCorruptException ex) {
            key = null;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Calling error handler for corrupt database ");
        sb.append(str);
        final SQLiteDatabaseCorruptException ex;
        Log.e("Database", sb.toString(), (Throwable)ex);
        databaseErrorHandler.onCorruption(key);
        key = new SQLiteDatabase(str, cursorFactory, n, databaseErrorHandler);
        key.openDatabaseInternal(array, sqLiteDatabaseHook);
        if (SQLiteDebug.DEBUG_SQL_STATEMENTS) {
            key.enableSqlTracing(str);
        }
        if (SQLiteDebug.DEBUG_SQL_TIME) {
            key.enableSqlProfiling(str);
        }
        synchronized (SQLiteDatabase.sActiveDatabases) {
            SQLiteDatabase.sActiveDatabases.put(key, null);
            return key;
        }
    }
    
    private void openDatabaseInternal(final char[] array, final SQLiteDatabaseHook sqLiteDatabaseHook) {
        final byte[] bytes = this.getBytes(array);
        this.dbopen(this.mPath, this.mFlags);
        int length = 0;
        int n = 0;
        int i = 0;
        try {
            try {
                this.keyDatabase(sqLiteDatabaseHook, new Runnable() {
                    @Override
                    public void run() {
                        if (bytes != null && bytes.length > 0) {
                            SQLiteDatabase.this.key(bytes);
                        }
                    }
                });
                if (bytes != null && bytes.length > 0) {
                    for (length = bytes.length; i < length; ++i) {
                        n = bytes[i];
                    }
                }
            }
            finally {
                this.dbclose();
                if (SQLiteDebug.DEBUG_SQL_CACHE) {
                    this.mTimeClosed = this.getTime();
                }
                if (bytes != null && bytes.length > 0) {
                    for (int length2 = bytes.length, j = n; j < length2; ++j) {
                        final byte b = bytes[j];
                    }
                }
                // iftrue(Label_0161:, n2 >= length3)
                // iftrue(Label_0125:, bytes == null || bytes.length <= 0)
            Block_11:
                while (true) {
                    int n2 = 0;
                    Block_14: {
                        break Block_14;
                        this.keyDatabase(sqLiteDatabaseHook, new Runnable() {
                            @Override
                            public void run() {
                                if (array != null) {
                                    SQLiteDatabase.this.key_mutf8(array);
                                }
                            }
                        });
                        break Block_11;
                        Label_0161: {
                            return;
                        }
                        final int length3 = bytes.length;
                        n2 = length;
                        continue;
                    }
                    final byte b2 = bytes[n2];
                    ++n2;
                    continue;
                }
                this.rekey(bytes);
                Label_0125:;
            }
            // iftrue(Label_0161:, bytes == null || bytes.length <= 0)
        }
        catch (RuntimeException ex) {}
    }
    
    public static SQLiteDatabase openOrCreateDatabase(final File file, final String s, final CursorFactory cursorFactory) {
        return openOrCreateDatabase(file, s, cursorFactory, null);
    }
    
    public static SQLiteDatabase openOrCreateDatabase(final File file, final String s, final CursorFactory cursorFactory, final SQLiteDatabaseHook sqLiteDatabaseHook) {
        return openOrCreateDatabase(file, s, cursorFactory, sqLiteDatabaseHook, null);
    }
    
    public static SQLiteDatabase openOrCreateDatabase(final File file, final String s, final CursorFactory cursorFactory, final SQLiteDatabaseHook sqLiteDatabaseHook, final DatabaseErrorHandler databaseErrorHandler) {
        String path;
        if (file == null) {
            path = null;
        }
        else {
            path = file.getPath();
        }
        return openOrCreateDatabase(path, s, cursorFactory, sqLiteDatabaseHook, databaseErrorHandler);
    }
    
    public static SQLiteDatabase openOrCreateDatabase(final String s, final String s2, final CursorFactory cursorFactory) {
        return openDatabase(s, s2, cursorFactory, 268435456, null);
    }
    
    public static SQLiteDatabase openOrCreateDatabase(final String s, final String s2, final CursorFactory cursorFactory, final SQLiteDatabaseHook sqLiteDatabaseHook) {
        return openDatabase(s, s2, cursorFactory, 268435456, sqLiteDatabaseHook);
    }
    
    public static SQLiteDatabase openOrCreateDatabase(final String s, final String s2, final CursorFactory cursorFactory, final SQLiteDatabaseHook sqLiteDatabaseHook, final DatabaseErrorHandler databaseErrorHandler) {
        char[] charArray;
        if (s2 == null) {
            charArray = null;
        }
        else {
            charArray = s2.toCharArray();
        }
        return openDatabase(s, charArray, cursorFactory, 268435456, sqLiteDatabaseHook, databaseErrorHandler);
    }
    
    public static SQLiteDatabase openOrCreateDatabase(final String s, final char[] array, final CursorFactory cursorFactory) {
        return openDatabase(s, array, cursorFactory, 268435456, null);
    }
    
    public static SQLiteDatabase openOrCreateDatabase(final String s, final char[] array, final CursorFactory cursorFactory, final SQLiteDatabaseHook sqLiteDatabaseHook) {
        return openDatabase(s, array, cursorFactory, 268435456, sqLiteDatabaseHook);
    }
    
    public static SQLiteDatabase openOrCreateDatabase(final String s, final char[] array, final CursorFactory cursorFactory, final SQLiteDatabaseHook sqLiteDatabaseHook, final DatabaseErrorHandler databaseErrorHandler) {
        return openDatabase(s, array, cursorFactory, 268435456, sqLiteDatabaseHook, databaseErrorHandler);
    }
    
    private native void rekey(final byte[] p0) throws SQLException;
    
    public static native int releaseMemory();
    
    public static native void setICURoot(final String p0);
    
    private void unlockForced() {
        if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING && this.mLock.getHoldCount() == 1) {
            this.checkLockHoldTime();
        }
        this.mLock.unlock();
    }
    
    private boolean yieldIfContendedHelper(final boolean b, long n) {
        if (this.mLock.getQueueLength() == 0) {
            this.mLockAcquiredWallTime = SystemClock.elapsedRealtime();
            this.mLockAcquiredThreadTime = Debug.threadCpuTimeNanos();
            return false;
        }
        this.setTransactionSuccessful();
        final SQLiteTransactionListener mTransactionListener = this.mTransactionListener;
        this.endTransaction();
        if (b && this.isDbLockedByCurrentThread()) {
            throw new IllegalStateException("Db locked more than once. yielfIfContended cannot yield");
        }
        if (n > 0L) {
            while (n > 0L) {
                long n2;
                if (n < 1000L) {
                    n2 = n;
                }
                else {
                    n2 = 1000L;
                }
                try {
                    Thread.sleep(n2);
                }
                catch (InterruptedException ex) {
                    Thread.interrupted();
                }
                if (this.mLock.getQueueLength() == 0) {
                    break;
                }
                n -= 1000L;
            }
        }
        this.beginTransactionWithListener(mTransactionListener);
        return true;
    }
    
    void addSQLiteClosable(final SQLiteClosable key) {
        this.lock();
        try {
            this.mPrograms.put(key, null);
        }
        finally {
            this.unlock();
        }
    }
    
    void addToCompiledQueries(final String str, final SQLiteCompiledSql sqLiteCompiledSql) {
        if (this.mMaxSqlCacheSize == 0) {
            if (SQLiteDebug.DEBUG_SQL_CACHE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("|NOT adding_sql_to_cache|");
                sb.append(this.getPath());
                sb.append("|");
                sb.append(str);
                Log.v("Database", sb.toString());
            }
            return;
        }
        synchronized (this.mCompiledQueries) {
            if (this.mCompiledQueries.get(str) != null) {
                return;
            }
            if (this.mCompiledQueries.size() == this.mMaxSqlCacheSize) {
                if (++this.mCacheFullWarnings == 1) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Reached MAX size for compiled-sql statement cache for database ");
                    sb2.append(this.getPath());
                    sb2.append("; i.e., NO space for this sql statement in cache: ");
                    sb2.append(str);
                    sb2.append(". Please change your sql statements to use '?' for ");
                    sb2.append("bindargs, instead of using actual values");
                    Log.w("Database", sb2.toString());
                }
            }
            else {
                this.mCompiledQueries.put(str, sqLiteCompiledSql);
                if (SQLiteDebug.DEBUG_SQL_CACHE) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("|adding_sql_to_cache|");
                    sb3.append(this.getPath());
                    sb3.append("|");
                    sb3.append(this.mCompiledQueries.size());
                    sb3.append("|");
                    sb3.append(str);
                    Log.v("Database", sb3.toString());
                }
            }
        }
    }
    
    public void beginTransaction() {
        this.beginTransactionWithListener(null);
    }
    
    public void beginTransactionWithListener(final SQLiteTransactionListener mTransactionListener) {
        this.lockForced();
        if (!this.isOpen()) {
            throw new IllegalStateException("database not open");
        }
        try {
            if (this.mLock.getHoldCount() <= 1) {
                this.execSQL("BEGIN EXCLUSIVE;");
                this.mTransactionListener = mTransactionListener;
                this.mTransactionIsSuccessful = true;
                this.mInnerTransactionIsSuccessful = false;
                if (mTransactionListener != null) {
                    try {
                        mTransactionListener.onBegin();
                    }
                    catch (RuntimeException ex) {
                        this.execSQL("ROLLBACK;");
                        throw ex;
                    }
                }
                return;
            }
            if (this.mInnerTransactionIsSuccessful) {
                final IllegalStateException ex2 = new IllegalStateException("Cannot call beginTransaction between calling setTransactionSuccessful and endTransaction");
                Log.e("Database", "beginTransaction() failed", (Throwable)ex2);
                throw ex2;
            }
        }
        finally {
            this.unlockForced();
        }
    }
    
    public void changePassword(final String s) throws SQLiteException {
        if (!this.isOpen()) {
            throw new SQLiteException("database not open");
        }
        if (s != null) {
            final byte[] bytes = this.getBytes(s.toCharArray());
            this.rekey(bytes);
            for (final byte b : bytes) {}
        }
    }
    
    public void changePassword(final char[] array) throws SQLiteException {
        if (!this.isOpen()) {
            throw new SQLiteException("database not open");
        }
        if (array != null) {
            final byte[] bytes = this.getBytes(array);
            this.rekey(bytes);
            for (final byte b : bytes) {}
        }
    }
    
    public void close() {
        if (!this.isOpen()) {
            return;
        }
        this.lock();
        try {
            this.closeClosable();
            this.onAllReferencesReleased();
        }
        finally {
            this.unlock();
        }
    }
    
    public SQLiteStatement compileStatement(final String s) throws SQLException {
        this.lock();
        if (!this.isOpen()) {
            throw new IllegalStateException("database not open");
        }
        try {
            return new SQLiteStatement(this, s);
        }
        finally {
            this.unlock();
        }
    }
    
    public int delete(String s, final String str, final String[] array) {
        this.lock();
        if (!this.isOpen()) {
            throw new IllegalStateException("database not open");
        }
        Object o2;
        final Object o = o2 = null;
        Label_0258: {
            try {
                try {
                    o2 = o;
                    final StringBuilder sb = new StringBuilder();
                    o2 = o;
                    sb.append("DELETE FROM ");
                    o2 = o;
                    sb.append(s);
                    o2 = o;
                    if (!TextUtils.isEmpty((CharSequence)str)) {
                        o2 = o;
                        o2 = o;
                        final StringBuilder sb2 = new StringBuilder();
                        o2 = o;
                        sb2.append(" WHERE ");
                        o2 = o;
                        sb2.append((String)str);
                        o2 = o;
                        s = sb2.toString();
                    }
                    else {
                        s = "";
                    }
                    o2 = o;
                    sb.append(s);
                    o2 = o;
                    s = (String)this.compileStatement(sb.toString());
                    if (array != null) {
                        try {
                            int n;
                            for (int length = array.length, i = 0; i < length; i = n) {
                                n = i + 1;
                                DatabaseUtils.bindObjectToProgram((SQLiteProgram)s, n, array[i]);
                            }
                        }
                        catch (SQLiteDatabaseCorruptException str) {}
                        finally {
                            o2 = s;
                            s = (String)str;
                            break Label_0258;
                        }
                    }
                    ((SQLiteStatement)s).execute();
                    final int lastChangeCount = this.lastChangeCount();
                    if (s != null) {
                        ((SQLiteProgram)s).close();
                    }
                    this.unlock();
                    return lastChangeCount;
                }
                finally {}
            }
            catch (SQLiteDatabaseCorruptException str) {}
            this.onCorruption();
            throw str;
        }
        if (o2 != null) {
            ((SQLiteProgram)o2).close();
        }
        this.unlock();
    }
    
    public void endTransaction() {
        if (!this.isOpen()) {
            throw new IllegalStateException("database not open");
        }
        if (!this.mLock.isHeldByCurrentThread()) {
            throw new IllegalStateException("no transaction pending");
        }
        try {
            if (this.mInnerTransactionIsSuccessful) {
                this.mInnerTransactionIsSuccessful = false;
            }
            else {
                this.mTransactionIsSuccessful = false;
            }
            if (this.mLock.getHoldCount() != 1) {
                return;
            }
            final RuntimeException ex;
            Label_0133: {
                if (this.mTransactionListener != null) {
                    try {
                        if (this.mTransactionIsSuccessful) {
                            this.mTransactionListener.onCommit();
                        }
                        else {
                            this.mTransactionListener.onRollback();
                        }
                    }
                    catch (RuntimeException ex) {
                        this.mTransactionIsSuccessful = false;
                        break Label_0133;
                    }
                }
                ex = null;
            }
            if (this.mTransactionIsSuccessful) {
                this.execSQL("COMMIT;");
            }
            else {
                try {
                    this.execSQL("ROLLBACK;");
                    if (ex != null) {
                        throw ex;
                    }
                }
                catch (SQLException ex2) {
                    Log.d("Database", "exception during rollback, maybe the DB previously performed an auto-rollback");
                }
            }
        }
        finally {
            this.mTransactionListener = null;
            this.unlockForced();
        }
    }
    
    public void execSQL(final String s) throws SQLException {
        SystemClock.uptimeMillis();
        this.lock();
        if (!this.isOpen()) {
            throw new IllegalStateException("database not open");
        }
        try {
            try {
                this.native_execSQL(s);
                this.unlock();
                return;
            }
            finally {}
        }
        catch (SQLiteDatabaseCorruptException ex) {
            this.onCorruption();
            throw ex;
        }
        this.unlock();
    }
    
    public void execSQL(String compileStatement, final Object[] array) throws SQLException {
        if (array == null) {
            throw new IllegalArgumentException("Empty bindArgs");
        }
        SystemClock.uptimeMillis();
        this.lock();
        if (!this.isOpen()) {
            throw new IllegalStateException("database not open");
        }
        final SQLiteProgram sqLiteProgram = null;
        final SQLiteProgram sqLiteProgram2 = null;
        SQLiteProgram sqLiteProgram3 = null;
        Label_0140: {
            Label_0128: {
                try {
                    try {
                        compileStatement = (String)this.compileStatement(compileStatement);
                        if (array != null) {
                            try {
                                int n;
                                for (int length = array.length, i = 0; i < length; i = n) {
                                    n = i + 1;
                                    DatabaseUtils.bindObjectToProgram((SQLiteProgram)compileStatement, n, array[i]);
                                }
                            }
                            catch (SQLiteDatabaseCorruptException ex2) {
                                break Label_0128;
                            }
                            finally {
                                break Label_0140;
                            }
                        }
                        ((SQLiteStatement)compileStatement).execute();
                        if (compileStatement != null) {
                            ((SQLiteProgram)compileStatement).close();
                        }
                        this.unlock();
                        return;
                    }
                    finally {
                        sqLiteProgram3 = sqLiteProgram2;
                    }
                }
                catch (SQLiteDatabaseCorruptException ex) {
                    sqLiteProgram3 = sqLiteProgram;
                }
            }
            this.onCorruption();
            throw;
        }
        if (sqLiteProgram3 != null) {
            sqLiteProgram3.close();
        }
        this.unlock();
    }
    
    @Override
    protected void finalize() {
        if (this.isOpen()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("close() was never explicitly called on database '");
            sb.append(this.mPath);
            sb.append("' ");
            Log.e("Database", sb.toString(), this.mStackTrace);
            this.closeClosable();
            this.onAllReferencesReleased();
        }
    }
    
    SQLiteCompiledSql getCompiledStatementForSql(final String str) {
        Object mCompiledQueries = this.mCompiledQueries;
        synchronized (mCompiledQueries) {
            if (this.mMaxSqlCacheSize == 0) {
                if (SQLiteDebug.DEBUG_SQL_CACHE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("|cache NOT found|");
                    sb.append(this.getPath());
                    Log.v("Database", sb.toString());
                }
                return null;
            }
            final SQLiteCompiledSql sqLiteCompiledSql = this.mCompiledQueries.get(str);
            final boolean b = sqLiteCompiledSql != null;
            // monitorexit(mCompiledQueries)
            if (b) {
                ++this.mNumCacheHits;
            }
            else {
                ++this.mNumCacheMisses;
            }
            if (SQLiteDebug.DEBUG_SQL_CACHE) {
                mCompiledQueries = new StringBuilder();
                ((StringBuilder)mCompiledQueries).append("|cache_stats|");
                ((StringBuilder)mCompiledQueries).append(this.getPath());
                ((StringBuilder)mCompiledQueries).append("|");
                ((StringBuilder)mCompiledQueries).append(this.mCompiledQueries.size());
                ((StringBuilder)mCompiledQueries).append("|");
                ((StringBuilder)mCompiledQueries).append(this.mNumCacheHits);
                ((StringBuilder)mCompiledQueries).append("|");
                ((StringBuilder)mCompiledQueries).append(this.mNumCacheMisses);
                ((StringBuilder)mCompiledQueries).append("|");
                ((StringBuilder)mCompiledQueries).append(b);
                ((StringBuilder)mCompiledQueries).append("|");
                ((StringBuilder)mCompiledQueries).append(this.mTimeOpened);
                ((StringBuilder)mCompiledQueries).append("|");
                ((StringBuilder)mCompiledQueries).append(this.mTimeClosed);
                ((StringBuilder)mCompiledQueries).append("|");
                ((StringBuilder)mCompiledQueries).append(str);
                Log.v("Database", ((StringBuilder)mCompiledQueries).toString());
            }
            return sqLiteCompiledSql;
        }
    }
    
    public int getMaxSqlCacheSize() {
        synchronized (this) {
            return this.mMaxSqlCacheSize;
        }
    }
    
    public long getMaximumSize() {
        this.lock();
        if (!this.isOpen()) {
            throw new IllegalStateException("database not open");
        }
        SQLiteProgram sqLiteProgram = null;
        try {
            final SQLiteStatement sqLiteStatement = new SQLiteStatement(this, "PRAGMA max_page_count;");
            try {
                final long simpleQueryForLong = sqLiteStatement.simpleQueryForLong();
                final long pageSize = this.getPageSize();
                if (sqLiteStatement != null) {
                    sqLiteStatement.close();
                }
                this.unlock();
                return simpleQueryForLong * pageSize;
            }
            finally {
                sqLiteProgram = sqLiteStatement;
            }
        }
        finally {}
        if (sqLiteProgram != null) {
            sqLiteProgram.close();
        }
        this.unlock();
    }
    
    public long getPageSize() {
        this.lock();
        if (!this.isOpen()) {
            throw new IllegalStateException("database not open");
        }
        SQLiteProgram sqLiteProgram = null;
        try {
            final SQLiteStatement sqLiteStatement = new SQLiteStatement(this, "PRAGMA page_size;");
            try {
                final long simpleQueryForLong = sqLiteStatement.simpleQueryForLong();
                if (sqLiteStatement != null) {
                    sqLiteStatement.close();
                }
                this.unlock();
                return simpleQueryForLong;
            }
            finally {
                sqLiteProgram = sqLiteStatement;
            }
        }
        finally {}
        if (sqLiteProgram != null) {
            sqLiteProgram.close();
        }
        this.unlock();
    }
    
    public final String getPath() {
        return this.mPath;
    }
    
    public Map<String, String> getSyncedTables() {
        synchronized (this.mSyncUpdateInfo) {
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            for (final String key : this.mSyncUpdateInfo.keySet()) {
                final SyncUpdateInfo syncUpdateInfo = this.mSyncUpdateInfo.get(key);
                if (syncUpdateInfo.deletedTable != null) {
                    hashMap.put(key, syncUpdateInfo.deletedTable);
                }
            }
            return hashMap;
        }
    }
    
    public int getVersion() {
        this.lock();
        if (!this.isOpen()) {
            throw new IllegalStateException("database not open");
        }
        SQLiteProgram sqLiteProgram = null;
        try {
            final SQLiteStatement sqLiteStatement = new SQLiteStatement(this, "PRAGMA user_version;");
            try {
                final int n = (int)sqLiteStatement.simpleQueryForLong();
                if (sqLiteStatement != null) {
                    sqLiteStatement.close();
                }
                this.unlock();
                return n;
            }
            finally {
                sqLiteProgram = sqLiteStatement;
            }
        }
        finally {}
        if (sqLiteProgram != null) {
            sqLiteProgram.close();
        }
        this.unlock();
    }
    
    public boolean inTransaction() {
        return this.mLock.getHoldCount() > 0;
    }
    
    public long insert(final String str, final String s, final ContentValues contentValues) {
        try {
            return this.insertWithOnConflict(str, s, contentValues, 0);
        }
        catch (SQLException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Error inserting <redacted values> into ");
            sb.append(str);
            Log.e("Database", sb.toString(), (Throwable)ex);
            return -1L;
        }
    }
    
    public long insertOrThrow(final String s, final String s2, final ContentValues contentValues) throws SQLException {
        return this.insertWithOnConflict(s, s2, contentValues, 0);
    }
    
    public long insertWithOnConflict(final String str, String str2, ContentValues compileStatement, int i) {
        if (!this.isOpen()) {
            throw new IllegalStateException("database not open");
        }
        final StringBuilder sb = new StringBuilder(152);
        sb.append("INSERT");
        sb.append(SQLiteDatabase.CONFLICT_VALUES[i]);
        sb.append(" INTO ");
        sb.append(str);
        final StringBuilder s = new StringBuilder(40);
        final int n = 0;
        final SQLiteDatabaseCorruptException ex = null;
        Set<Map.Entry<String, V>> valueSet;
        if (compileStatement != null && ((ContentValues)compileStatement).size() > 0) {
            valueSet = (Set<Map.Entry<String, V>>)((ContentValues)compileStatement).valueSet();
            final Iterator<Map.Entry<String, V>> iterator = valueSet.iterator();
            sb.append('(');
            i = 0;
            while (iterator.hasNext()) {
                if (i != 0) {
                    sb.append(", ");
                    s.append(", ");
                }
                sb.append(iterator.next().getKey());
                s.append('?');
                i = 1;
            }
            sb.append(')');
        }
        else {
            compileStatement = (SQLiteDatabaseCorruptException)new StringBuilder();
            ((StringBuilder)compileStatement).append("(");
            ((StringBuilder)compileStatement).append((String)str2);
            ((StringBuilder)compileStatement).append(") ");
            sb.append(((StringBuilder)compileStatement).toString());
            s.append("NULL");
            valueSet = null;
        }
        sb.append(" VALUES(");
        sb.append((CharSequence)s);
        sb.append(");");
        this.lock();
        str2 = ex;
        try {
            try {
                compileStatement = (SQLiteDatabaseCorruptException)this.compileStatement(sb.toString());
                if (valueSet != null) {
                    try {
                        final int size = valueSet.size();
                        final Iterator<Map.Entry<String, V>> iterator2 = valueSet.iterator();
                        i = n;
                        while (i < size) {
                            str2 = (SQLiteDatabaseCorruptException)iterator2.next();
                            ++i;
                            try {
                                DatabaseUtils.bindObjectToProgram((SQLiteProgram)compileStatement, i, ((Map.Entry<K, Object>)str2).getValue());
                            }
                            catch (SQLiteDatabaseCorruptException str2) {}
                        }
                    }
                    catch (SQLiteDatabaseCorruptException ex2) {}
                }
                ((SQLiteStatement)compileStatement).execute();
                long lastInsertRow;
                if (this.lastChangeCount() > 0) {
                    lastInsertRow = this.lastInsertRow();
                }
                else {
                    lastInsertRow = -1L;
                }
                if (lastInsertRow == -1L) {
                    str2 = (SQLiteDatabaseCorruptException)new StringBuilder();
                    ((StringBuilder)str2).append("Error inserting <redacted values> using <redacted sql> into ");
                    ((StringBuilder)str2).append(str);
                    Log.e("Database", ((StringBuilder)str2).toString());
                }
                else if (Log.isLoggable("Database", 2)) {
                    str2 = (SQLiteDatabaseCorruptException)new StringBuilder();
                    ((StringBuilder)str2).append("Inserting row ");
                    ((StringBuilder)str2).append(lastInsertRow);
                    ((StringBuilder)str2).append(" from <redacted values> using <redacted sql> into ");
                    ((StringBuilder)str2).append(str);
                    Log.v("Database", ((StringBuilder)str2).toString());
                }
                if (compileStatement != null) {
                    ((SQLiteProgram)compileStatement).close();
                }
                this.unlock();
                return lastInsertRow;
            }
            finally {}
        }
        catch (SQLiteDatabaseCorruptException compileStatement) {}
        this.onCorruption();
        throw compileStatement;
        if (str2 != null) {
            ((SQLiteProgram)str2).close();
        }
        this.unlock();
    }
    
    public boolean isDbLockedByCurrentThread() {
        return this.mLock.isHeldByCurrentThread();
    }
    
    public boolean isDbLockedByOtherThreads() {
        return !this.mLock.isHeldByCurrentThread() && this.mLock.isLocked();
    }
    
    public boolean isInCompiledSqlCache(final String s) {
        synchronized (this.mCompiledQueries) {
            return this.mCompiledQueries.containsKey(s);
        }
    }
    
    public boolean isOpen() {
        return this.mNativeHandle != 0L;
    }
    
    public boolean isReadOnly() {
        final int mFlags = this.mFlags;
        boolean b = true;
        if ((mFlags & 0x1) != 0x1) {
            b = false;
        }
        return b;
    }
    
    native int lastChangeCount();
    
    native long lastInsertRow();
    
    void lock() {
        if (!this.mLockingEnabled) {
            return;
        }
        this.mLock.lock();
        if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING && this.mLock.getHoldCount() == 1) {
            this.mLockAcquiredWallTime = SystemClock.elapsedRealtime();
            this.mLockAcquiredThreadTime = Debug.threadCpuTimeNanos();
        }
    }
    
    public void markTableSyncable(final String s, final String s2) {
        if (!this.isOpen()) {
            throw new SQLiteException("database not open");
        }
        this.markTableSyncable(s, "_id", s, s2);
    }
    
    public void markTableSyncable(final String s, final String s2, final String s3) {
        if (!this.isOpen()) {
            throw new SQLiteException("database not open");
        }
        this.markTableSyncable(s, s2, s3, null);
    }
    
    native void native_execSQL(final String p0) throws SQLException;
    
    native void native_setLocale(final String p0, final int p1);
    
    public boolean needUpgrade(final int n) {
        return n > this.getVersion();
    }
    
    @Override
    protected void onAllReferencesReleased() {
        if (this.isOpen()) {
            if (SQLiteDebug.DEBUG_SQL_CACHE) {
                this.mTimeClosed = this.getTime();
            }
            this.dbclose();
            synchronized (SQLiteDatabase.sActiveDatabases) {
                SQLiteDatabase.sActiveDatabases.remove(this);
            }
        }
    }
    
    void onCorruption() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Calling error handler for corrupt database (detected) ");
        sb.append(this.mPath);
        Log.e("Database", sb.toString());
        this.mErrorHandler.onCorruption(this);
    }
    
    public void purgeFromCompiledSqlCache(final String s) {
        synchronized (this.mCompiledQueries) {
            this.mCompiledQueries.remove(s);
        }
    }
    
    public Cursor query(final String s, final String[] array, final String s2, final String[] array2, final String s3, final String s4, final String s5) {
        return this.query(false, s, array, s2, array2, s3, s4, s5, null);
    }
    
    public Cursor query(final String s, final String[] array, final String s2, final String[] array2, final String s3, final String s4, final String s5, final String s6) {
        return this.query(false, s, array, s2, array2, s3, s4, s5, s6);
    }
    
    public Cursor query(final boolean b, final String s, final String[] array, final String s2, final String[] array2, final String s3, final String s4, final String s5, final String s6) {
        return this.queryWithFactory(null, b, s, array, s2, array2, s3, s4, s5, s6);
    }
    
    public Cursor queryWithFactory(final CursorFactory cursorFactory, final boolean b, final String s, final String[] array, final String s2, final String[] array2, final String s3, final String s4, final String s5, final String s6) {
        if (!this.isOpen()) {
            throw new IllegalStateException("database not open");
        }
        return this.rawQueryWithFactory(cursorFactory, SQLiteQueryBuilder.buildQueryString(b, s, array, s2, s3, s4, s5, s6), array2, findEditTable(s));
    }
    
    public void rawExecSQL(final String s) {
        SystemClock.uptimeMillis();
        this.lock();
        if (!this.isOpen()) {
            throw new IllegalStateException("database not open");
        }
        try {
            try {
                this.native_rawExecSQL(s);
                this.unlock();
                return;
            }
            finally {}
        }
        catch (SQLiteDatabaseCorruptException ex) {
            this.onCorruption();
            throw ex;
        }
        this.unlock();
    }
    
    public Cursor rawQuery(String s, final Object[] array) {
        if (!this.isOpen()) {
            throw new IllegalStateException("database not open");
        }
        long currentTimeMillis = 0L;
        final int mSlowQueryThreshold = this.mSlowQueryThreshold;
        int count = -1;
        if (mSlowQueryThreshold != -1) {
            currentTimeMillis = System.currentTimeMillis();
        }
        s = (String)new SQLiteDirectCursorDriver(this, s, null);
        try {
            final Cursor query = ((SQLiteDirectCursorDriver)s).query(this.mFactory, array);
            if (this.mSlowQueryThreshold != -1) {
                if (query != null) {
                    count = query.getCount();
                }
                currentTimeMillis = System.currentTimeMillis() - currentTimeMillis;
                if (currentTimeMillis >= this.mSlowQueryThreshold) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("query (");
                    sb.append(currentTimeMillis);
                    sb.append(" ms): ");
                    sb.append(((SQLiteDirectCursorDriver)s).toString());
                    sb.append(", args are <redacted>, count is ");
                    sb.append(count);
                    Log.v("Database", sb.toString());
                }
            }
            return new CrossProcessCursorWrapper(query);
        }
        finally {
            if (this.mSlowQueryThreshold != -1) {
                final long lng = System.currentTimeMillis() - currentTimeMillis;
                if (lng >= this.mSlowQueryThreshold) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("query (");
                    sb2.append(lng);
                    sb2.append(" ms): ");
                    sb2.append(((SQLiteDirectCursorDriver)s).toString());
                    sb2.append(", args are <redacted>, count is ");
                    sb2.append(-1);
                    Log.v("Database", sb2.toString());
                }
            }
        }
    }
    
    public Cursor rawQuery(final String s, final String[] array) {
        return this.rawQueryWithFactory(null, s, array, null);
    }
    
    public Cursor rawQuery(final String s, final String[] array, final int n, final int n2) {
        final CursorWrapper cursorWrapper = (CursorWrapper)this.rawQueryWithFactory(null, s, array, null);
        ((SQLiteCursor)cursorWrapper.getWrappedCursor()).setLoadStyle(n, n2);
        return cursorWrapper;
    }
    
    public Cursor rawQueryWithFactory(CursorFactory mFactory, String s, final String[] array, final String s2) {
        if (!this.isOpen()) {
            throw new IllegalStateException("database not open");
        }
        long currentTimeMillis = 0L;
        final int mSlowQueryThreshold = this.mSlowQueryThreshold;
        int count = -1;
        if (mSlowQueryThreshold != -1) {
            currentTimeMillis = System.currentTimeMillis();
        }
        s = (String)new SQLiteDirectCursorDriver(this, s, s2);
        Label_0065: {
            if (mFactory != null) {
                break Label_0065;
            }
            try {
                mFactory = this.mFactory;
                final Cursor query = ((SQLiteCursorDriver)s).query(mFactory, array);
                if (this.mSlowQueryThreshold != -1) {
                    if (query != null) {
                        count = query.getCount();
                    }
                    currentTimeMillis = System.currentTimeMillis() - currentTimeMillis;
                    if (currentTimeMillis >= this.mSlowQueryThreshold) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("query (");
                        sb.append(currentTimeMillis);
                        sb.append(" ms): ");
                        sb.append(s.toString());
                        sb.append(", args are <redacted>, count is ");
                        sb.append(count);
                        Log.v("Database", sb.toString());
                    }
                }
                return new CrossProcessCursorWrapper(query);
            }
            finally {
                if (this.mSlowQueryThreshold != -1) {
                    final long lng = System.currentTimeMillis() - currentTimeMillis;
                    if (lng >= this.mSlowQueryThreshold) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("query (");
                        sb2.append(lng);
                        sb2.append(" ms): ");
                        sb2.append(s.toString());
                        sb2.append(", args are <redacted>, count is ");
                        sb2.append(-1);
                        Log.v("Database", sb2.toString());
                    }
                }
            }
        }
    }
    
    void removeSQLiteClosable(final SQLiteClosable key) {
        this.lock();
        try {
            this.mPrograms.remove(key);
        }
        finally {
            this.unlock();
        }
    }
    
    public long replace(final String str, final String s, final ContentValues contentValues) {
        try {
            return this.insertWithOnConflict(str, s, contentValues, 5);
        }
        catch (SQLException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Error inserting <redacted values> into ");
            sb.append(str);
            Log.e("Database", sb.toString(), (Throwable)ex);
            return -1L;
        }
    }
    
    public long replaceOrThrow(final String s, final String s2, final ContentValues contentValues) throws SQLException {
        return this.insertWithOnConflict(s, s2, contentValues, 5);
    }
    
    public void resetCompiledSqlCache() {
        synchronized (this.mCompiledQueries) {
            this.mCompiledQueries.clear();
        }
    }
    
    void rowUpdated(final String str, final long lng) {
        Object mSyncUpdateInfo = this.mSyncUpdateInfo;
        synchronized (mSyncUpdateInfo) {
            final SyncUpdateInfo syncUpdateInfo = this.mSyncUpdateInfo.get(str);
            // monitorexit(mSyncUpdateInfo)
            if (syncUpdateInfo != null) {
                mSyncUpdateInfo = new StringBuilder();
                ((StringBuilder)mSyncUpdateInfo).append("UPDATE ");
                ((StringBuilder)mSyncUpdateInfo).append(syncUpdateInfo.masterTable);
                ((StringBuilder)mSyncUpdateInfo).append(" SET _sync_dirty=1 WHERE _id=(SELECT ");
                ((StringBuilder)mSyncUpdateInfo).append(syncUpdateInfo.foreignKey);
                ((StringBuilder)mSyncUpdateInfo).append(" FROM ");
                ((StringBuilder)mSyncUpdateInfo).append(str);
                ((StringBuilder)mSyncUpdateInfo).append(" WHERE _id=");
                ((StringBuilder)mSyncUpdateInfo).append(lng);
                ((StringBuilder)mSyncUpdateInfo).append(")");
                this.execSQL(((StringBuilder)mSyncUpdateInfo).toString());
            }
        }
    }
    
    public void setLocale(final Locale locale) {
        this.lock();
        try {
            this.native_setLocale(locale.toString(), this.mFlags);
        }
        finally {
            this.unlock();
        }
    }
    
    public void setLockingEnabled(final boolean mLockingEnabled) {
        this.mLockingEnabled = mLockingEnabled;
    }
    
    public void setMaxSqlCacheSize(final int mMaxSqlCacheSize) {
        // monitorenter(this)
        Label_0045: {
            if (mMaxSqlCacheSize > 250 || mMaxSqlCacheSize < 0) {
                break Label_0045;
            }
            try {
                if (mMaxSqlCacheSize < this.mMaxSqlCacheSize) {
                    throw new IllegalStateException("cannot set cacheSize to a value less than the value set with previous setMaxSqlCacheSize() call.");
                }
                this.mMaxSqlCacheSize = mMaxSqlCacheSize;
                return;
                throw new IllegalStateException("expected value between 0 and 250");
            }
            finally {
            }
            // monitorexit(this)
        }
    }
    
    public long setMaximumSize(long simpleQueryForLong) {
        this.lock();
        if (!this.isOpen()) {
            throw new IllegalStateException("database not open");
        }
        SQLiteProgram sqLiteProgram = null;
        try {
            final long pageSize = this.getPageSize();
            long lng = simpleQueryForLong / pageSize;
            if (simpleQueryForLong % pageSize != 0L) {
                ++lng;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("PRAGMA max_page_count = ");
            sb.append(lng);
            final SQLiteStatement sqLiteStatement = new SQLiteStatement(this, sb.toString());
            try {
                simpleQueryForLong = sqLiteStatement.simpleQueryForLong();
                if (sqLiteStatement != null) {
                    sqLiteStatement.close();
                }
                this.unlock();
                return simpleQueryForLong * pageSize;
            }
            finally {
                sqLiteProgram = sqLiteStatement;
            }
        }
        finally {}
        if (sqLiteProgram != null) {
            sqLiteProgram.close();
        }
        this.unlock();
    }
    
    public void setPageSize(final long lng) {
        final StringBuilder sb = new StringBuilder();
        sb.append("PRAGMA page_size = ");
        sb.append(lng);
        this.execSQL(sb.toString());
    }
    
    public void setTransactionSuccessful() {
        if (!this.isOpen()) {
            throw new IllegalStateException("database not open");
        }
        if (!this.mLock.isHeldByCurrentThread()) {
            throw new IllegalStateException("no transaction pending");
        }
        if (this.mInnerTransactionIsSuccessful) {
            throw new IllegalStateException("setTransactionSuccessful may only be called once per call to beginTransaction");
        }
        this.mInnerTransactionIsSuccessful = true;
    }
    
    public void setVersion(final int i) {
        final StringBuilder sb = new StringBuilder();
        sb.append("PRAGMA user_version = ");
        sb.append(i);
        this.execSQL(sb.toString());
    }
    
    public int status(final int n, final boolean b) {
        return this.native_status(n, b);
    }
    
    void unlock() {
        if (!this.mLockingEnabled) {
            return;
        }
        if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING && this.mLock.getHoldCount() == 1) {
            this.checkLockHoldTime();
        }
        this.mLock.unlock();
    }
    
    public int update(final String s, final ContentValues contentValues, final String s2, final String[] array) {
        return this.updateWithOnConflict(s, contentValues, s2, array, 0);
    }
    
    public int updateWithOnConflict(String s, ContentValues iterator, String compileStatement, final String[] ex, int i) {
        if (iterator == null || iterator.size() == 0) {
            throw new IllegalArgumentException("Empty values");
        }
        final StringBuilder sb = new StringBuilder(120);
        sb.append("UPDATE ");
        sb.append(SQLiteDatabase.CONFLICT_VALUES[i]);
        sb.append(s);
        sb.append(" SET ");
        final Set valueSet = iterator.valueSet();
        final Iterator<Map.Entry<String, V>> iterator2 = valueSet.iterator();
        while (iterator2.hasNext()) {
            sb.append(iterator2.next().getKey());
            sb.append("=?");
            if (iterator2.hasNext()) {
                sb.append(", ");
            }
        }
        if (!TextUtils.isEmpty((CharSequence)compileStatement)) {
            sb.append(" WHERE ");
            sb.append((String)compileStatement);
        }
        this.lock();
        if (!this.isOpen()) {
            throw new IllegalStateException("database not open");
        }
        final SQLiteDatabaseCorruptException ex2 = null;
        final String s2 = null;
        iterator = null;
        try {
            try {
                compileStatement = (SQLiteDatabaseCorruptException)this.compileStatement(sb.toString());
                try {
                    final int size = valueSet.size();
                    iterator = (ContentValues)valueSet.iterator();
                    final int n = 0;
                    i = 1;
                    for (int j = 0; j < size; ++j) {
                        DatabaseUtils.bindObjectToProgram((SQLiteProgram)compileStatement, i, ((Iterator<Map.Entry<K, Object>>)iterator).next().getValue());
                        ++i;
                    }
                    if (ex != null) {
                        final int length = ex.length;
                        int n2 = i;
                        for (i = n; i < length; ++i) {
                            ((SQLiteProgram)compileStatement).bindString(n2, ex[i]);
                            ++n2;
                        }
                    }
                    ((SQLiteStatement)compileStatement).execute();
                    i = this.lastChangeCount();
                    if (Log.isLoggable("Database", 2)) {
                        iterator = (ContentValues)new StringBuilder();
                        ((StringBuilder)iterator).append("Updated ");
                        ((StringBuilder)iterator).append(i);
                        ((StringBuilder)iterator).append(" rows using <redacted values> and <redacted sql> for ");
                        ((StringBuilder)iterator).append(s);
                        Log.v("Database", ((StringBuilder)iterator).toString());
                    }
                    if (compileStatement != null) {
                        ((SQLiteProgram)compileStatement).close();
                    }
                    this.unlock();
                    return i;
                }
                catch (SQLException ex) {}
                catch (SQLiteDatabaseCorruptException ex3) {
                    iterator = (ContentValues)compileStatement;
                    compileStatement = ex3;
                }
                finally {
                    iterator = (ContentValues)compileStatement;
                }
            }
            finally {}
        }
        catch (SQLException ex) {
            compileStatement = ex2;
        }
        catch (SQLiteDatabaseCorruptException compileStatement) {
            s = s2;
        }
        this.onCorruption();
        throw compileStatement;
        if (iterator != null) {
            ((SQLiteProgram)iterator).close();
        }
        this.unlock();
    }
    
    @Deprecated
    public boolean yieldIfContended() {
        return this.isOpen() && this.yieldIfContendedHelper(false, -1L);
    }
    
    public boolean yieldIfContendedSafely() {
        return this.isOpen() && this.yieldIfContendedHelper(true, -1L);
    }
    
    public boolean yieldIfContendedSafely(final long n) {
        return this.isOpen() && this.yieldIfContendedHelper(true, n);
    }
    
    public interface CursorFactory
    {
        Cursor newCursor(final SQLiteDatabase p0, final SQLiteCursorDriver p1, final String p2, final SQLiteQuery p3);
    }
    
    public interface LibraryLoader
    {
        void loadLibraries(final String... p0);
    }
    
    private static class SyncUpdateInfo
    {
        String deletedTable;
        String foreignKey;
        String masterTable;
        
        SyncUpdateInfo(final String masterTable, final String deletedTable, final String foreignKey) {
            this.masterTable = masterTable;
            this.deletedTable = deletedTable;
            this.foreignKey = foreignKey;
        }
    }
}
