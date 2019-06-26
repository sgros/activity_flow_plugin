// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.widget;

import java.math.BigDecimal;
import android.content.ComponentName;
import java.util.Collections;
import java.util.Collection;
import android.os.AsyncTask;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.content.Context;
import java.util.List;
import java.util.Map;
import android.database.DataSetObservable;

class ActivityChooserModel extends DataSetObservable
{
    static final String ATTRIBUTE_ACTIVITY = "activity";
    static final String ATTRIBUTE_TIME = "time";
    static final String ATTRIBUTE_WEIGHT = "weight";
    static final boolean DEBUG = false;
    private static final int DEFAULT_ACTIVITY_INFLATION = 5;
    private static final float DEFAULT_HISTORICAL_RECORD_WEIGHT = 1.0f;
    public static final String DEFAULT_HISTORY_FILE_NAME = "activity_choser_model_history.xml";
    public static final int DEFAULT_HISTORY_MAX_LENGTH = 50;
    private static final String HISTORY_FILE_EXTENSION = ".xml";
    private static final int INVALID_INDEX = -1;
    static final String LOG_TAG = "ActivityChooserModel";
    static final String TAG_HISTORICAL_RECORD = "historical-record";
    static final String TAG_HISTORICAL_RECORDS = "historical-records";
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
    
    private ActivityChooserModel(final Context context, final String s) {
        this.mInstanceLock = new Object();
        this.mActivities = new ArrayList<ActivityResolveInfo>();
        this.mHistoricalRecords = new ArrayList<HistoricalRecord>();
        this.mActivitySorter = (ActivitySorter)new DefaultSorter();
        this.mHistoryMaxSize = 50;
        this.mCanReadHistoricalData = true;
        this.mReadShareHistoryCalled = false;
        this.mHistoricalRecordsChanged = true;
        this.mReloadActivities = false;
        this.mContext = context.getApplicationContext();
        if (!TextUtils.isEmpty((CharSequence)s) && !s.endsWith(".xml")) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(".xml");
            this.mHistoryFileName = sb.toString();
        }
        else {
            this.mHistoryFileName = s;
        }
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
    
    public static ActivityChooserModel get(final Context context, final String s) {
        synchronized (ActivityChooserModel.sRegistryLock) {
            ActivityChooserModel activityChooserModel;
            if ((activityChooserModel = ActivityChooserModel.sDataModelRegistry.get(s)) == null) {
                activityChooserModel = new ActivityChooserModel(context, s);
                ActivityChooserModel.sDataModelRegistry.put(s, activityChooserModel);
            }
            return activityChooserModel;
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
        //    18: ldc_w           "UTF-8"
        //    21: invokeinterface org/xmlpull/v1/XmlPullParser.setInput:(Ljava/io/InputStream;Ljava/lang/String;)V
        //    26: iconst_0       
        //    27: istore_3       
        //    28: iload_3        
        //    29: iconst_1       
        //    30: if_icmpeq       48
        //    33: iload_3        
        //    34: iconst_2       
        //    35: if_icmpeq       48
        //    38: aload_2        
        //    39: invokeinterface org/xmlpull/v1/XmlPullParser.next:()I
        //    44: istore_3       
        //    45: goto            28
        //    48: ldc             "historical-records"
        //    50: aload_2        
        //    51: invokeinterface org/xmlpull/v1/XmlPullParser.getName:()Ljava/lang/String;
        //    56: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    59: ifne            78
        //    62: new             Lorg/xmlpull/v1/XmlPullParserException;
        //    65: astore          4
        //    67: aload           4
        //    69: ldc_w           "Share records file does not start with historical-records tag."
        //    72: invokespecial   org/xmlpull/v1/XmlPullParserException.<init>:(Ljava/lang/String;)V
        //    75: aload           4
        //    77: athrow         
        //    78: aload_0        
        //    79: getfield        android/support/v7/widget/ActivityChooserModel.mHistoricalRecords:Ljava/util/List;
        //    82: astore          4
        //    84: aload           4
        //    86: invokeinterface java/util/List.clear:()V
        //    91: aload_2        
        //    92: invokeinterface org/xmlpull/v1/XmlPullParser.next:()I
        //    97: istore_3       
        //    98: iload_3        
        //    99: iconst_1       
        //   100: if_icmpne       114
        //   103: aload_1        
        //   104: ifnull          335
        //   107: aload_1        
        //   108: invokevirtual   java/io/FileInputStream.close:()V
        //   111: goto            335
        //   114: iload_3        
        //   115: iconst_3       
        //   116: if_icmpeq       91
        //   119: iload_3        
        //   120: iconst_4       
        //   121: if_icmpne       127
        //   124: goto            91
        //   127: ldc             "historical-record"
        //   129: aload_2        
        //   130: invokeinterface org/xmlpull/v1/XmlPullParser.getName:()Ljava/lang/String;
        //   135: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   138: ifne            157
        //   141: new             Lorg/xmlpull/v1/XmlPullParserException;
        //   144: astore          4
        //   146: aload           4
        //   148: ldc_w           "Share records file not well-formed."
        //   151: invokespecial   org/xmlpull/v1/XmlPullParserException.<init>:(Ljava/lang/String;)V
        //   154: aload           4
        //   156: athrow         
        //   157: aload_2        
        //   158: aconst_null    
        //   159: ldc             "activity"
        //   161: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   166: astore          5
        //   168: aload_2        
        //   169: aconst_null    
        //   170: ldc             "time"
        //   172: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   177: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
        //   180: lstore          6
        //   182: aload_2        
        //   183: aconst_null    
        //   184: ldc             "weight"
        //   186: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   191: invokestatic    java/lang/Float.parseFloat:(Ljava/lang/String;)F
        //   194: fstore          8
        //   196: new             Landroid/support/v7/widget/ActivityChooserModel$HistoricalRecord;
        //   199: astore          9
        //   201: aload           9
        //   203: aload           5
        //   205: lload           6
        //   207: fload           8
        //   209: invokespecial   android/support/v7/widget/ActivityChooserModel$HistoricalRecord.<init>:(Ljava/lang/String;JF)V
        //   212: aload           4
        //   214: aload           9
        //   216: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   221: pop            
        //   222: goto            91
        //   225: astore          4
        //   227: goto            336
        //   230: astore          4
        //   232: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
        //   235: astore_2       
        //   236: new             Ljava/lang/StringBuilder;
        //   239: astore          5
        //   241: aload           5
        //   243: invokespecial   java/lang/StringBuilder.<init>:()V
        //   246: aload           5
        //   248: ldc_w           "Error reading historical recrod file: "
        //   251: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   254: pop            
        //   255: aload           5
        //   257: aload_0        
        //   258: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //   261: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   264: pop            
        //   265: aload_2        
        //   266: aload           5
        //   268: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   271: aload           4
        //   273: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   276: pop            
        //   277: aload_1        
        //   278: ifnull          335
        //   281: goto            107
        //   284: astore          4
        //   286: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
        //   289: astore          5
        //   291: new             Ljava/lang/StringBuilder;
        //   294: astore_2       
        //   295: aload_2        
        //   296: invokespecial   java/lang/StringBuilder.<init>:()V
        //   299: aload_2        
        //   300: ldc_w           "Error reading historical recrod file: "
        //   303: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   306: pop            
        //   307: aload_2        
        //   308: aload_0        
        //   309: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //   312: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   315: pop            
        //   316: aload           5
        //   318: aload_2        
        //   319: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   322: aload           4
        //   324: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   327: pop            
        //   328: aload_1        
        //   329: ifnull          335
        //   332: goto            107
        //   335: return         
        //   336: aload_1        
        //   337: ifnull          344
        //   340: aload_1        
        //   341: invokevirtual   java/io/FileInputStream.close:()V
        //   344: aload           4
        //   346: athrow         
        //   347: astore_1       
        //   348: return         
        //   349: astore_1       
        //   350: goto            335
        //   353: astore_1       
        //   354: goto            344
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                   
        //  -----  -----  -----  -----  ---------------------------------------
        //  0      12     347    349    Ljava/io/FileNotFoundException;
        //  12     26     284    335    Lorg/xmlpull/v1/XmlPullParserException;
        //  12     26     230    284    Ljava/io/IOException;
        //  12     26     225    347    Any
        //  38     45     284    335    Lorg/xmlpull/v1/XmlPullParserException;
        //  38     45     230    284    Ljava/io/IOException;
        //  38     45     225    347    Any
        //  48     78     284    335    Lorg/xmlpull/v1/XmlPullParserException;
        //  48     78     230    284    Ljava/io/IOException;
        //  48     78     225    347    Any
        //  78     91     284    335    Lorg/xmlpull/v1/XmlPullParserException;
        //  78     91     230    284    Ljava/io/IOException;
        //  78     91     225    347    Any
        //  91     98     284    335    Lorg/xmlpull/v1/XmlPullParserException;
        //  91     98     230    284    Ljava/io/IOException;
        //  91     98     225    347    Any
        //  107    111    349    353    Ljava/io/IOException;
        //  127    157    284    335    Lorg/xmlpull/v1/XmlPullParserException;
        //  127    157    230    284    Ljava/io/IOException;
        //  127    157    225    347    Any
        //  157    222    284    335    Lorg/xmlpull/v1/XmlPullParserException;
        //  157    222    230    284    Ljava/io/IOException;
        //  157    222    225    347    Any
        //  232    277    225    347    Any
        //  286    328    225    347    Any
        //  340    344    353    357    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.util.ConcurrentModificationException
        //     at java.base/java.util.ArrayList$Itr.checkForComodification(ArrayList.java:937)
        //     at java.base/java.util.ArrayList$Itr.next(ArrayList.java:891)
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2863)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
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
    
    public int getHistoryMaxSize() {
        synchronized (this.mInstanceLock) {
            return this.mHistoryMaxSize;
        }
    }
    
    public int getHistorySize() {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            return this.mHistoricalRecords.size();
        }
    }
    
    public Intent getIntent() {
        synchronized (this.mInstanceLock) {
            return this.mIntent;
        }
    }
    
    public void setActivitySorter(final ActivitySorter mActivitySorter) {
        synchronized (this.mInstanceLock) {
            if (this.mActivitySorter == mActivitySorter) {
                return;
            }
            this.mActivitySorter = mActivitySorter;
            if (this.sortActivitiesIfNeeded()) {
                this.notifyChanged();
            }
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
    
    public void setHistoryMaxSize(final int mHistoryMaxSize) {
        synchronized (this.mInstanceLock) {
            if (this.mHistoryMaxSize == mHistoryMaxSize) {
                return;
            }
            this.mHistoryMaxSize = mHistoryMaxSize;
            this.pruneExcessiveHistoricalRecordsIfNeeded();
            if (this.sortActivitiesIfNeeded()) {
                this.notifyChanged();
            }
        }
    }
    
    public void setIntent(final Intent mIntent) {
        synchronized (this.mInstanceLock) {
            if (this.mIntent == mIntent) {
                return;
            }
            this.mIntent = mIntent;
            this.mReloadActivities = true;
            this.ensureConsistentState();
        }
    }
    
    public void setOnChooseActivityListener(final OnChooseActivityListener mActivityChoserModelPolicy) {
        synchronized (this.mInstanceLock) {
            this.mActivityChoserModelPolicy = mActivityChoserModelPolicy;
        }
    }
    
    public interface ActivityChooserModelClient
    {
        void setActivityChooserModel(final ActivityChooserModel p0);
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
            return 31 + Float.floatToIntBits(this.weight);
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
    
    private static final class DefaultSorter implements ActivitySorter
    {
        private static final float WEIGHT_DECAY_COEFFICIENT = 0.95f;
        private final Map<ComponentName, ActivityResolveInfo> mPackageNameToActivityMap;
        
        DefaultSorter() {
            this.mPackageNameToActivityMap = new HashMap<ComponentName, ActivityResolveInfo>();
        }
        
        @Override
        public void sort(final Intent intent, final List<ActivityResolveInfo> list, final List<HistoricalRecord> list2) {
            final Map<ComponentName, ActivityResolveInfo> mPackageNameToActivityMap = this.mPackageNameToActivityMap;
            mPackageNameToActivityMap.clear();
            for (int size = list.size(), i = 0; i < size; ++i) {
                final ActivityResolveInfo activityResolveInfo = list.get(i);
                activityResolveInfo.weight = 0.0f;
                mPackageNameToActivityMap.put(new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name), activityResolveInfo);
            }
            int j = list2.size() - 1;
            float n = 1.0f;
            while (j >= 0) {
                final HistoricalRecord historicalRecord = list2.get(j);
                final ActivityResolveInfo activityResolveInfo2 = mPackageNameToActivityMap.get(historicalRecord.activity);
                float n2 = n;
                if (activityResolveInfo2 != null) {
                    activityResolveInfo2.weight += historicalRecord.weight * n;
                    n2 = n * 0.95f;
                }
                --j;
                n = n2;
            }
            Collections.sort((List<Comparable>)list);
        }
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
            return 31 * ((hashCode + 31) * 31 + (int)(this.time ^ this.time >>> 32)) + Float.floatToIntBits(this.weight);
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
            //    30: astore_3       
            //    31: aload_3        
            //    32: aload_1        
            //    33: aconst_null    
            //    34: invokeinterface org/xmlpull/v1/XmlSerializer.setOutput:(Ljava/io/OutputStream;Ljava/lang/String;)V
            //    39: aload_3        
            //    40: ldc             "UTF-8"
            //    42: iconst_1       
            //    43: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
            //    46: invokeinterface org/xmlpull/v1/XmlSerializer.startDocument:(Ljava/lang/String;Ljava/lang/Boolean;)V
            //    51: aload_3        
            //    52: aconst_null    
            //    53: ldc             "historical-records"
            //    55: invokeinterface org/xmlpull/v1/XmlSerializer.startTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //    60: pop            
            //    61: aload_2        
            //    62: invokeinterface java/util/List.size:()I
            //    67: istore          4
            //    69: iconst_0       
            //    70: istore          5
            //    72: iload           5
            //    74: iload           4
            //    76: if_icmpge       171
            //    79: aload_2        
            //    80: iconst_0       
            //    81: invokeinterface java/util/List.remove:(I)Ljava/lang/Object;
            //    86: checkcast       Landroid/support/v7/widget/ActivityChooserModel$HistoricalRecord;
            //    89: astore          6
            //    91: aload_3        
            //    92: aconst_null    
            //    93: ldc             "historical-record"
            //    95: invokeinterface org/xmlpull/v1/XmlSerializer.startTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   100: pop            
            //   101: aload_3        
            //   102: aconst_null    
            //   103: ldc             "activity"
            //   105: aload           6
            //   107: getfield        android/support/v7/widget/ActivityChooserModel$HistoricalRecord.activity:Landroid/content/ComponentName;
            //   110: invokevirtual   android/content/ComponentName.flattenToString:()Ljava/lang/String;
            //   113: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   118: pop            
            //   119: aload_3        
            //   120: aconst_null    
            //   121: ldc             "time"
            //   123: aload           6
            //   125: getfield        android/support/v7/widget/ActivityChooserModel$HistoricalRecord.time:J
            //   128: invokestatic    java/lang/String.valueOf:(J)Ljava/lang/String;
            //   131: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   136: pop            
            //   137: aload_3        
            //   138: aconst_null    
            //   139: ldc             "weight"
            //   141: aload           6
            //   143: getfield        android/support/v7/widget/ActivityChooserModel$HistoricalRecord.weight:F
            //   146: invokestatic    java/lang/String.valueOf:(F)Ljava/lang/String;
            //   149: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   154: pop            
            //   155: aload_3        
            //   156: aconst_null    
            //   157: ldc             "historical-record"
            //   159: invokeinterface org/xmlpull/v1/XmlSerializer.endTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   164: pop            
            //   165: iinc            5, 1
            //   168: goto            72
            //   171: aload_3        
            //   172: aconst_null    
            //   173: ldc             "historical-records"
            //   175: invokeinterface org/xmlpull/v1/XmlSerializer.endTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   180: pop            
            //   181: aload_3        
            //   182: invokeinterface org/xmlpull/v1/XmlSerializer.endDocument:()V
            //   187: aload_0        
            //   188: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   191: iconst_1       
            //   192: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   195: aload_1        
            //   196: ifnull          387
            //   199: aload_1        
            //   200: invokevirtual   java/io/FileOutputStream.close:()V
            //   203: goto            387
            //   206: astore_2       
            //   207: goto            389
            //   210: astore          6
            //   212: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   215: astore_2       
            //   216: new             Ljava/lang/StringBuilder;
            //   219: astore_3       
            //   220: aload_3        
            //   221: invokespecial   java/lang/StringBuilder.<init>:()V
            //   224: aload_3        
            //   225: ldc             "Error writing historical record file: "
            //   227: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   230: pop            
            //   231: aload_3        
            //   232: aload_0        
            //   233: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   236: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   239: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   242: pop            
            //   243: aload_2        
            //   244: aload_3        
            //   245: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   248: aload           6
            //   250: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   253: pop            
            //   254: aload_0        
            //   255: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   258: iconst_1       
            //   259: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   262: aload_1        
            //   263: ifnull          387
            //   266: goto            199
            //   269: astore          6
            //   271: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   274: astore_3       
            //   275: new             Ljava/lang/StringBuilder;
            //   278: astore_2       
            //   279: aload_2        
            //   280: invokespecial   java/lang/StringBuilder.<init>:()V
            //   283: aload_2        
            //   284: ldc             "Error writing historical record file: "
            //   286: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   289: pop            
            //   290: aload_2        
            //   291: aload_0        
            //   292: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   295: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   298: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   301: pop            
            //   302: aload_3        
            //   303: aload_2        
            //   304: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   307: aload           6
            //   309: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   312: pop            
            //   313: aload_0        
            //   314: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   317: iconst_1       
            //   318: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   321: aload_1        
            //   322: ifnull          387
            //   325: goto            199
            //   328: astore          6
            //   330: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   333: astore_2       
            //   334: new             Ljava/lang/StringBuilder;
            //   337: astore_3       
            //   338: aload_3        
            //   339: invokespecial   java/lang/StringBuilder.<init>:()V
            //   342: aload_3        
            //   343: ldc             "Error writing historical record file: "
            //   345: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   348: pop            
            //   349: aload_3        
            //   350: aload_0        
            //   351: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   354: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   357: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   360: pop            
            //   361: aload_2        
            //   362: aload_3        
            //   363: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   366: aload           6
            //   368: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   371: pop            
            //   372: aload_0        
            //   373: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   376: iconst_1       
            //   377: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   380: aload_1        
            //   381: ifnull          387
            //   384: goto            199
            //   387: aconst_null    
            //   388: areturn        
            //   389: aload_0        
            //   390: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   393: iconst_1       
            //   394: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   397: aload_1        
            //   398: ifnull          405
            //   401: aload_1        
            //   402: invokevirtual   java/io/FileOutputStream.close:()V
            //   405: aload_2        
            //   406: athrow         
            //   407: astore          6
            //   409: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   412: astore_2       
            //   413: new             Ljava/lang/StringBuilder;
            //   416: dup            
            //   417: invokespecial   java/lang/StringBuilder.<init>:()V
            //   420: astore_1       
            //   421: aload_1        
            //   422: ldc             "Error writing historical record file: "
            //   424: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   427: pop            
            //   428: aload_1        
            //   429: aload_3        
            //   430: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   433: pop            
            //   434: aload_2        
            //   435: aload_1        
            //   436: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   439: aload           6
            //   441: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   444: pop            
            //   445: aconst_null    
            //   446: areturn        
            //   447: astore_1       
            //   448: goto            387
            //   451: astore_1       
            //   452: goto            405
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                                
            //  -----  -----  -----  -----  ------------------------------------
            //  14     27     407    447    Ljava/io/FileNotFoundException;
            //  31     69     328    387    Ljava/lang/IllegalArgumentException;
            //  31     69     269    328    Ljava/lang/IllegalStateException;
            //  31     69     210    269    Ljava/io/IOException;
            //  31     69     206    407    Any
            //  79     165    328    387    Ljava/lang/IllegalArgumentException;
            //  79     165    269    328    Ljava/lang/IllegalStateException;
            //  79     165    210    269    Ljava/io/IOException;
            //  79     165    206    407    Any
            //  171    187    328    387    Ljava/lang/IllegalArgumentException;
            //  171    187    269    328    Ljava/lang/IllegalStateException;
            //  171    187    210    269    Ljava/io/IOException;
            //  171    187    206    407    Any
            //  199    203    447    451    Ljava/io/IOException;
            //  212    254    206    407    Any
            //  271    313    206    407    Any
            //  330    372    206    407    Any
            //  401    405    451    455    Ljava/io/IOException;
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
