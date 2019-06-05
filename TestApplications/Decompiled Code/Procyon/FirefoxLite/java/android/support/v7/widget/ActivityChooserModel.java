// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.widget;

import java.math.BigDecimal;
import android.content.ComponentName;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.content.pm.ResolveInfo;
import java.util.HashMap;
import android.content.Intent;
import android.content.Context;
import java.util.List;
import java.util.Map;
import android.database.DataSetObservable;

class ActivityChooserModel extends DataSetObservable
{
    static final String LOG_TAG = "ActivityChooserModel";
    private static final Map<String, ActivityChooserModel> sDataModelRegistry;
    private static final Object sRegistryLock;
    private final List<ActivityResolveInfo> mActivities;
    private OnChooseActivityListener mActivityChoserModelPolicy;
    private ActivitySorter mActivitySorter;
    boolean mCanReadHistoricalData;
    final Context mContext;
    private final List<HistoricalRecord> mHistoricalRecords;
    private boolean mHistoricalRecordsChanged;
    final String mHistoryFileName;
    private int mHistoryMaxSize;
    private final Object mInstanceLock;
    private Intent mIntent;
    private boolean mReadShareHistoryCalled;
    private boolean mReloadActivities;
    
    static {
        sRegistryLock = new Object();
        sDataModelRegistry = new HashMap<String, ActivityChooserModel>();
    }
    
    private boolean addHistoricalRecord(final HistoricalRecord historicalRecord) {
        final boolean add = this.mHistoricalRecords.add(historicalRecord);
        if (add) {
            this.mHistoricalRecordsChanged = true;
            this.pruneExcessiveHistoricalRecordsIfNeeded();
            this.persistHistoricalDataIfNeeded();
            this.sortActivitiesIfNeeded();
            this.notifyChanged();
        }
        return add;
    }
    
    private void ensureConsistentState() {
        final boolean loadActivitiesIfNeeded = this.loadActivitiesIfNeeded();
        final boolean historicalDataIfNeeded = this.readHistoricalDataIfNeeded();
        this.pruneExcessiveHistoricalRecordsIfNeeded();
        if (loadActivitiesIfNeeded | historicalDataIfNeeded) {
            this.sortActivitiesIfNeeded();
            this.notifyChanged();
        }
    }
    
    private boolean loadActivitiesIfNeeded() {
        final boolean mReloadActivities = this.mReloadActivities;
        int i = 0;
        if (mReloadActivities && this.mIntent != null) {
            this.mReloadActivities = false;
            this.mActivities.clear();
            for (List queryIntentActivities = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 0); i < queryIntentActivities.size(); ++i) {
                this.mActivities.add(new ActivityResolveInfo(queryIntentActivities.get(i)));
            }
            return true;
        }
        return false;
    }
    
    private void persistHistoricalDataIfNeeded() {
        if (!this.mReadShareHistoryCalled) {
            throw new IllegalStateException("No preceding call to #readHistoricalData");
        }
        if (!this.mHistoricalRecordsChanged) {
            return;
        }
        this.mHistoricalRecordsChanged = false;
        if (!TextUtils.isEmpty((CharSequence)this.mHistoryFileName)) {
            new PersistHistoryAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[] { new ArrayList(this.mHistoricalRecords), this.mHistoryFileName });
        }
    }
    
    private void pruneExcessiveHistoricalRecordsIfNeeded() {
        final int n = this.mHistoricalRecords.size() - this.mHistoryMaxSize;
        if (n <= 0) {
            return;
        }
        this.mHistoricalRecordsChanged = true;
        for (int i = 0; i < n; ++i) {
            final HistoricalRecord historicalRecord = this.mHistoricalRecords.remove(0);
        }
    }
    
    private boolean readHistoricalDataIfNeeded() {
        if (this.mCanReadHistoricalData && this.mHistoricalRecordsChanged && !TextUtils.isEmpty((CharSequence)this.mHistoryFileName)) {
            this.mCanReadHistoricalData = false;
            this.mReadShareHistoryCalled = true;
            this.readHistoricalDataImpl();
            return true;
        }
        return false;
    }
    
    private void readHistoricalDataImpl() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        android/support/v7/widget/ActivityChooserModel.mContext:Landroid/content/Context;
        //     4: aload_0        
        //     5: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //     8: invokevirtual   android/content/Context.openFileInput:(Ljava/lang/String;)Ljava/io/FileInputStream;
        //    11: astore_1       
        //    12: invokestatic    android/util/Xml.newPullParser:()Lorg/xmlpull/v1/XmlPullParser;
        //    15: astore_2       
        //    16: aload_2        
        //    17: aload_1        
        //    18: ldc             "UTF-8"
        //    20: invokeinterface org/xmlpull/v1/XmlPullParser.setInput:(Ljava/io/InputStream;Ljava/lang/String;)V
        //    25: iconst_0       
        //    26: istore_3       
        //    27: iload_3        
        //    28: iconst_1       
        //    29: if_icmpeq       47
        //    32: iload_3        
        //    33: iconst_2       
        //    34: if_icmpeq       47
        //    37: aload_2        
        //    38: invokeinterface org/xmlpull/v1/XmlPullParser.next:()I
        //    43: istore_3       
        //    44: goto            27
        //    47: ldc             "historical-records"
        //    49: aload_2        
        //    50: invokeinterface org/xmlpull/v1/XmlPullParser.getName:()Ljava/lang/String;
        //    55: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    58: ifeq            207
        //    61: aload_0        
        //    62: getfield        android/support/v7/widget/ActivityChooserModel.mHistoricalRecords:Ljava/util/List;
        //    65: astore          4
        //    67: aload           4
        //    69: invokeinterface java/util/List.clear:()V
        //    74: aload_2        
        //    75: invokeinterface org/xmlpull/v1/XmlPullParser.next:()I
        //    80: istore_3       
        //    81: iload_3        
        //    82: iconst_1       
        //    83: if_icmpne       97
        //    86: aload_1        
        //    87: ifnull          339
        //    90: aload_1        
        //    91: invokevirtual   java/io/FileInputStream.close:()V
        //    94: goto            339
        //    97: iload_3        
        //    98: iconst_3       
        //    99: if_icmpeq       74
        //   102: iload_3        
        //   103: iconst_4       
        //   104: if_icmpne       110
        //   107: goto            74
        //   110: ldc             "historical-record"
        //   112: aload_2        
        //   113: invokeinterface org/xmlpull/v1/XmlPullParser.getName:()Ljava/lang/String;
        //   118: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   121: ifeq            192
        //   124: aload_2        
        //   125: aconst_null    
        //   126: ldc             "activity"
        //   128: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   133: astore          5
        //   135: aload_2        
        //   136: aconst_null    
        //   137: ldc             "time"
        //   139: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   144: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
        //   147: lstore          6
        //   149: aload_2        
        //   150: aconst_null    
        //   151: ldc             "weight"
        //   153: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   158: invokestatic    java/lang/Float.parseFloat:(Ljava/lang/String;)F
        //   161: fstore          8
        //   163: new             Landroid/support/v7/widget/ActivityChooserModel$HistoricalRecord;
        //   166: astore          9
        //   168: aload           9
        //   170: aload           5
        //   172: lload           6
        //   174: fload           8
        //   176: invokespecial   android/support/v7/widget/ActivityChooserModel$HistoricalRecord.<init>:(Ljava/lang/String;JF)V
        //   179: aload           4
        //   181: aload           9
        //   183: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   188: pop            
        //   189: goto            74
        //   192: new             Lorg/xmlpull/v1/XmlPullParserException;
        //   195: astore          5
        //   197: aload           5
        //   199: ldc             "Share records file not well-formed."
        //   201: invokespecial   org/xmlpull/v1/XmlPullParserException.<init>:(Ljava/lang/String;)V
        //   204: aload           5
        //   206: athrow         
        //   207: new             Lorg/xmlpull/v1/XmlPullParserException;
        //   210: astore          5
        //   212: aload           5
        //   214: ldc             "Share records file does not start with historical-records tag."
        //   216: invokespecial   org/xmlpull/v1/XmlPullParserException.<init>:(Ljava/lang/String;)V
        //   219: aload           5
        //   221: athrow         
        //   222: astore          5
        //   224: goto            340
        //   227: astore          5
        //   229: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
        //   232: astore          9
        //   234: new             Ljava/lang/StringBuilder;
        //   237: astore          4
        //   239: aload           4
        //   241: invokespecial   java/lang/StringBuilder.<init>:()V
        //   244: aload           4
        //   246: ldc_w           "Error reading historical recrod file: "
        //   249: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   252: pop            
        //   253: aload           4
        //   255: aload_0        
        //   256: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //   259: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   262: pop            
        //   263: aload           9
        //   265: aload           4
        //   267: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   270: aload           5
        //   272: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   275: pop            
        //   276: aload_1        
        //   277: ifnull          339
        //   280: goto            90
        //   283: astore          5
        //   285: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
        //   288: astore          4
        //   290: new             Ljava/lang/StringBuilder;
        //   293: astore          9
        //   295: aload           9
        //   297: invokespecial   java/lang/StringBuilder.<init>:()V
        //   300: aload           9
        //   302: ldc_w           "Error reading historical recrod file: "
        //   305: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   308: pop            
        //   309: aload           9
        //   311: aload_0        
        //   312: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //   315: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   318: pop            
        //   319: aload           4
        //   321: aload           9
        //   323: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   326: aload           5
        //   328: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   331: pop            
        //   332: aload_1        
        //   333: ifnull          339
        //   336: goto            90
        //   339: return         
        //   340: aload_1        
        //   341: ifnull          348
        //   344: aload_1        
        //   345: invokevirtual   java/io/FileInputStream.close:()V
        //   348: aload           5
        //   350: athrow         
        //   351: astore_1       
        //   352: return         
        //   353: astore_1       
        //   354: goto            339
        //   357: astore_1       
        //   358: goto            348
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                   
        //  -----  -----  -----  -----  ---------------------------------------
        //  0      12     351    353    Ljava/io/FileNotFoundException;
        //  12     25     283    339    Lorg/xmlpull/v1/XmlPullParserException;
        //  12     25     227    283    Ljava/io/IOException;
        //  12     25     222    351    Any
        //  37     44     283    339    Lorg/xmlpull/v1/XmlPullParserException;
        //  37     44     227    283    Ljava/io/IOException;
        //  37     44     222    351    Any
        //  47     74     283    339    Lorg/xmlpull/v1/XmlPullParserException;
        //  47     74     227    283    Ljava/io/IOException;
        //  47     74     222    351    Any
        //  74     81     283    339    Lorg/xmlpull/v1/XmlPullParserException;
        //  74     81     227    283    Ljava/io/IOException;
        //  74     81     222    351    Any
        //  90     94     353    357    Ljava/io/IOException;
        //  110    189    283    339    Lorg/xmlpull/v1/XmlPullParserException;
        //  110    189    227    283    Ljava/io/IOException;
        //  110    189    222    351    Any
        //  192    207    283    339    Lorg/xmlpull/v1/XmlPullParserException;
        //  192    207    227    283    Ljava/io/IOException;
        //  192    207    222    351    Any
        //  207    222    283    339    Lorg/xmlpull/v1/XmlPullParserException;
        //  207    222    227    283    Ljava/io/IOException;
        //  207    222    222    351    Any
        //  229    276    222    351    Any
        //  285    332    222    351    Any
        //  344    348    357    361    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 165 out-of-bounds for length 165
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    
    private boolean sortActivitiesIfNeeded() {
        if (this.mActivitySorter != null && this.mIntent != null && !this.mActivities.isEmpty() && !this.mHistoricalRecords.isEmpty()) {
            this.mActivitySorter.sort(this.mIntent, this.mActivities, Collections.unmodifiableList((List<? extends HistoricalRecord>)this.mHistoricalRecords));
            return true;
        }
        return false;
    }
    
    public Intent chooseActivity(final int n) {
        synchronized (this.mInstanceLock) {
            if (this.mIntent == null) {
                return null;
            }
            this.ensureConsistentState();
            final ActivityResolveInfo activityResolveInfo = this.mActivities.get(n);
            final ComponentName component = new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name);
            final Intent intent = new Intent(this.mIntent);
            intent.setComponent(component);
            if (this.mActivityChoserModelPolicy != null && this.mActivityChoserModelPolicy.onChooseActivity(this, new Intent(intent))) {
                return null;
            }
            this.addHistoricalRecord(new HistoricalRecord(component, System.currentTimeMillis(), 1.0f));
            return intent;
        }
    }
    
    public ResolveInfo getActivity(final int n) {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            return this.mActivities.get(n).resolveInfo;
        }
    }
    
    public int getActivityCount() {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            return this.mActivities.size();
        }
    }
    
    public int getActivityIndex(final ResolveInfo resolveInfo) {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            final List<ActivityResolveInfo> mActivities = this.mActivities;
            for (int size = mActivities.size(), i = 0; i < size; ++i) {
                if (mActivities.get(i).resolveInfo == resolveInfo) {
                    return i;
                }
            }
            return -1;
        }
    }
    
    public ResolveInfo getDefaultActivity() {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            if (!this.mActivities.isEmpty()) {
                return this.mActivities.get(0).resolveInfo;
            }
            return null;
        }
    }
    
    public void setDefaultActivity(final int n) {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            final ActivityResolveInfo activityResolveInfo = this.mActivities.get(n);
            final ActivityResolveInfo activityResolveInfo2 = this.mActivities.get(0);
            float n2;
            if (activityResolveInfo2 != null) {
                n2 = activityResolveInfo2.weight - activityResolveInfo.weight + 5.0f;
            }
            else {
                n2 = 1.0f;
            }
            this.addHistoricalRecord(new HistoricalRecord(new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name), System.currentTimeMillis(), n2));
        }
    }
    
    public static final class ActivityResolveInfo implements Comparable<ActivityResolveInfo>
    {
        public final ResolveInfo resolveInfo;
        public float weight;
        
        public ActivityResolveInfo(final ResolveInfo resolveInfo) {
            this.resolveInfo = resolveInfo;
        }
        
        @Override
        public int compareTo(final ActivityResolveInfo activityResolveInfo) {
            return Float.floatToIntBits(activityResolveInfo.weight) - Float.floatToIntBits(this.weight);
        }
        
        @Override
        public boolean equals(final Object o) {
            return this == o || (o != null && this.getClass() == o.getClass() && Float.floatToIntBits(this.weight) == Float.floatToIntBits(((ActivityResolveInfo)o).weight));
        }
        
        @Override
        public int hashCode() {
            return Float.floatToIntBits(this.weight) + 31;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append("resolveInfo:");
            sb.append(this.resolveInfo.toString());
            sb.append("; weight:");
            sb.append(new BigDecimal(this.weight));
            sb.append("]");
            return sb.toString();
        }
    }
    
    public interface ActivitySorter
    {
        void sort(final Intent p0, final List<ActivityResolveInfo> p1, final List<HistoricalRecord> p2);
    }
    
    public static final class HistoricalRecord
    {
        public final ComponentName activity;
        public final long time;
        public final float weight;
        
        public HistoricalRecord(final ComponentName activity, final long time, final float weight) {
            this.activity = activity;
            this.time = time;
            this.weight = weight;
        }
        
        public HistoricalRecord(final String s, final long n, final float n2) {
            this(ComponentName.unflattenFromString(s), n, n2);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final HistoricalRecord historicalRecord = (HistoricalRecord)o;
            if (this.activity == null) {
                if (historicalRecord.activity != null) {
                    return false;
                }
            }
            else if (!this.activity.equals((Object)historicalRecord.activity)) {
                return false;
            }
            return this.time == historicalRecord.time && Float.floatToIntBits(this.weight) == Float.floatToIntBits(historicalRecord.weight);
        }
        
        @Override
        public int hashCode() {
            int hashCode;
            if (this.activity == null) {
                hashCode = 0;
            }
            else {
                hashCode = this.activity.hashCode();
            }
            return ((hashCode + 31) * 31 + (int)(this.time ^ this.time >>> 32)) * 31 + Float.floatToIntBits(this.weight);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append("; activity:");
            sb.append(this.activity);
            sb.append("; time:");
            sb.append(this.time);
            sb.append("; weight:");
            sb.append(new BigDecimal(this.weight));
            sb.append("]");
            return sb.toString();
        }
    }
    
    public interface OnChooseActivityListener
    {
        boolean onChooseActivity(final ActivityChooserModel p0, final Intent p1);
    }
    
    private final class PersistHistoryAsyncTask extends AsyncTask<Object, Void, Void>
    {
        PersistHistoryAsyncTask() {
        }
        
        public Void doInBackground(final Object... p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: iconst_0       
            //     2: aaload         
            //     3: checkcast       Ljava/util/List;
            //     6: astore_2       
            //     7: aload_1        
            //     8: iconst_1       
            //     9: aaload         
            //    10: checkcast       Ljava/lang/String;
            //    13: astore_3       
            //    14: aload_0        
            //    15: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //    18: getfield        android/support/v7/widget/ActivityChooserModel.mContext:Landroid/content/Context;
            //    21: aload_3        
            //    22: iconst_0       
            //    23: invokevirtual   android/content/Context.openFileOutput:(Ljava/lang/String;I)Ljava/io/FileOutputStream;
            //    26: astore_1       
            //    27: invokestatic    android/util/Xml.newSerializer:()Lorg/xmlpull/v1/XmlSerializer;
            //    30: astore          4
            //    32: aload           4
            //    34: aload_1        
            //    35: aconst_null    
            //    36: invokeinterface org/xmlpull/v1/XmlSerializer.setOutput:(Ljava/io/OutputStream;Ljava/lang/String;)V
            //    41: aload           4
            //    43: ldc             "UTF-8"
            //    45: iconst_1       
            //    46: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
            //    49: invokeinterface org/xmlpull/v1/XmlSerializer.startDocument:(Ljava/lang/String;Ljava/lang/Boolean;)V
            //    54: aload           4
            //    56: aconst_null    
            //    57: ldc             "historical-records"
            //    59: invokeinterface org/xmlpull/v1/XmlSerializer.startTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //    64: pop            
            //    65: aload_2        
            //    66: invokeinterface java/util/List.size:()I
            //    71: istore          5
            //    73: iconst_0       
            //    74: istore          6
            //    76: iload           6
            //    78: iload           5
            //    80: if_icmpge       176
            //    83: aload_2        
            //    84: iconst_0       
            //    85: invokeinterface java/util/List.remove:(I)Ljava/lang/Object;
            //    90: checkcast       Landroid/support/v7/widget/ActivityChooserModel$HistoricalRecord;
            //    93: astore_3       
            //    94: aload           4
            //    96: aconst_null    
            //    97: ldc             "historical-record"
            //    99: invokeinterface org/xmlpull/v1/XmlSerializer.startTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   104: pop            
            //   105: aload           4
            //   107: aconst_null    
            //   108: ldc             "activity"
            //   110: aload_3        
            //   111: getfield        android/support/v7/widget/ActivityChooserModel$HistoricalRecord.activity:Landroid/content/ComponentName;
            //   114: invokevirtual   android/content/ComponentName.flattenToString:()Ljava/lang/String;
            //   117: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   122: pop            
            //   123: aload           4
            //   125: aconst_null    
            //   126: ldc             "time"
            //   128: aload_3        
            //   129: getfield        android/support/v7/widget/ActivityChooserModel$HistoricalRecord.time:J
            //   132: invokestatic    java/lang/String.valueOf:(J)Ljava/lang/String;
            //   135: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   140: pop            
            //   141: aload           4
            //   143: aconst_null    
            //   144: ldc             "weight"
            //   146: aload_3        
            //   147: getfield        android/support/v7/widget/ActivityChooserModel$HistoricalRecord.weight:F
            //   150: invokestatic    java/lang/String.valueOf:(F)Ljava/lang/String;
            //   153: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   158: pop            
            //   159: aload           4
            //   161: aconst_null    
            //   162: ldc             "historical-record"
            //   164: invokeinterface org/xmlpull/v1/XmlSerializer.endTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   169: pop            
            //   170: iinc            6, 1
            //   173: goto            76
            //   176: aload           4
            //   178: aconst_null    
            //   179: ldc             "historical-records"
            //   181: invokeinterface org/xmlpull/v1/XmlSerializer.endTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   186: pop            
            //   187: aload           4
            //   189: invokeinterface org/xmlpull/v1/XmlSerializer.endDocument:()V
            //   194: aload_0        
            //   195: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   198: iconst_1       
            //   199: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   202: aload_1        
            //   203: ifnull          394
            //   206: aload_1        
            //   207: invokevirtual   java/io/FileOutputStream.close:()V
            //   210: goto            394
            //   213: astore_3       
            //   214: goto            396
            //   217: astore          4
            //   219: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   222: astore_2       
            //   223: new             Ljava/lang/StringBuilder;
            //   226: astore_3       
            //   227: aload_3        
            //   228: invokespecial   java/lang/StringBuilder.<init>:()V
            //   231: aload_3        
            //   232: ldc             "Error writing historical record file: "
            //   234: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   237: pop            
            //   238: aload_3        
            //   239: aload_0        
            //   240: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   243: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   246: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   249: pop            
            //   250: aload_2        
            //   251: aload_3        
            //   252: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   255: aload           4
            //   257: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   260: pop            
            //   261: aload_0        
            //   262: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   265: iconst_1       
            //   266: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   269: aload_1        
            //   270: ifnull          394
            //   273: goto            206
            //   276: astore_3       
            //   277: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   280: astore          4
            //   282: new             Ljava/lang/StringBuilder;
            //   285: astore_2       
            //   286: aload_2        
            //   287: invokespecial   java/lang/StringBuilder.<init>:()V
            //   290: aload_2        
            //   291: ldc             "Error writing historical record file: "
            //   293: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   296: pop            
            //   297: aload_2        
            //   298: aload_0        
            //   299: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   302: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   305: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   308: pop            
            //   309: aload           4
            //   311: aload_2        
            //   312: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   315: aload_3        
            //   316: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   319: pop            
            //   320: aload_0        
            //   321: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   324: iconst_1       
            //   325: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   328: aload_1        
            //   329: ifnull          394
            //   332: goto            206
            //   335: astore_2       
            //   336: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   339: astore          4
            //   341: new             Ljava/lang/StringBuilder;
            //   344: astore_3       
            //   345: aload_3        
            //   346: invokespecial   java/lang/StringBuilder.<init>:()V
            //   349: aload_3        
            //   350: ldc             "Error writing historical record file: "
            //   352: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   355: pop            
            //   356: aload_3        
            //   357: aload_0        
            //   358: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   361: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   364: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   367: pop            
            //   368: aload           4
            //   370: aload_3        
            //   371: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   374: aload_2        
            //   375: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   378: pop            
            //   379: aload_0        
            //   380: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   383: iconst_1       
            //   384: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   387: aload_1        
            //   388: ifnull          394
            //   391: goto            206
            //   394: aconst_null    
            //   395: areturn        
            //   396: aload_0        
            //   397: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   400: iconst_1       
            //   401: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   404: aload_1        
            //   405: ifnull          412
            //   408: aload_1        
            //   409: invokevirtual   java/io/FileOutputStream.close:()V
            //   412: aload_3        
            //   413: athrow         
            //   414: astore_2       
            //   415: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   418: astore          4
            //   420: new             Ljava/lang/StringBuilder;
            //   423: dup            
            //   424: invokespecial   java/lang/StringBuilder.<init>:()V
            //   427: astore_1       
            //   428: aload_1        
            //   429: ldc             "Error writing historical record file: "
            //   431: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   434: pop            
            //   435: aload_1        
            //   436: aload_3        
            //   437: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   440: pop            
            //   441: aload           4
            //   443: aload_1        
            //   444: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   447: aload_2        
            //   448: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   451: pop            
            //   452: aconst_null    
            //   453: areturn        
            //   454: astore_1       
            //   455: goto            394
            //   458: astore_1       
            //   459: goto            412
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                                
            //  -----  -----  -----  -----  ------------------------------------
            //  14     27     414    454    Ljava/io/FileNotFoundException;
            //  32     73     335    394    Ljava/lang/IllegalArgumentException;
            //  32     73     276    335    Ljava/lang/IllegalStateException;
            //  32     73     217    276    Ljava/io/IOException;
            //  32     73     213    414    Any
            //  83     170    335    394    Ljava/lang/IllegalArgumentException;
            //  83     170    276    335    Ljava/lang/IllegalStateException;
            //  83     170    217    276    Ljava/io/IOException;
            //  83     170    213    414    Any
            //  176    194    335    394    Ljava/lang/IllegalArgumentException;
            //  176    194    276    335    Ljava/lang/IllegalStateException;
            //  176    194    217    276    Ljava/io/IOException;
            //  176    194    213    414    Any
            //  206    210    454    458    Ljava/io/IOException;
            //  219    261    213    414    Any
            //  277    320    213    414    Any
            //  336    379    213    414    Any
            //  408    412    458    462    Ljava/io/IOException;
            // 
            // The error that occurred was:
            // 
            // java.lang.IndexOutOfBoundsException: Index 229 out-of-bounds for length 229
            //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
            //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
            //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
            //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
            //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
            //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    }
}
