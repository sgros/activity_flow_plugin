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
        //   104: ifnull          338
        //   107: aload_1        
        //   108: invokevirtual   java/io/FileInputStream.close:()V
        //   111: goto            338
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
        //   227: goto            339
        //   230: astore          9
        //   232: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
        //   235: astore_2       
        //   236: new             Ljava/lang/StringBuilder;
        //   239: astore          4
        //   241: aload           4
        //   243: invokespecial   java/lang/StringBuilder.<init>:()V
        //   246: aload           4
        //   248: ldc_w           "Error reading historical recrod file: "
        //   251: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   254: pop            
        //   255: aload           4
        //   257: aload_0        
        //   258: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //   261: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   264: pop            
        //   265: aload_2        
        //   266: aload           4
        //   268: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   271: aload           9
        //   273: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   276: pop            
        //   277: aload_1        
        //   278: ifnull          338
        //   281: goto            107
        //   284: astore_2       
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
        //   326: aload_2        
        //   327: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   330: pop            
        //   331: aload_1        
        //   332: ifnull          338
        //   335: goto            107
        //   338: return         
        //   339: aload_1        
        //   340: ifnull          347
        //   343: aload_1        
        //   344: invokevirtual   java/io/FileInputStream.close:()V
        //   347: aload           4
        //   349: athrow         
        //   350: astore_1       
        //   351: return         
        //   352: astore_1       
        //   353: goto            338
        //   356: astore_1       
        //   357: goto            347
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                   
        //  -----  -----  -----  -----  ---------------------------------------
        //  0      12     350    352    Ljava/io/FileNotFoundException;
        //  12     26     284    338    Lorg/xmlpull/v1/XmlPullParserException;
        //  12     26     230    284    Ljava/io/IOException;
        //  12     26     225    350    Any
        //  38     45     284    338    Lorg/xmlpull/v1/XmlPullParserException;
        //  38     45     230    284    Ljava/io/IOException;
        //  38     45     225    350    Any
        //  48     78     284    338    Lorg/xmlpull/v1/XmlPullParserException;
        //  48     78     230    284    Ljava/io/IOException;
        //  48     78     225    350    Any
        //  78     91     284    338    Lorg/xmlpull/v1/XmlPullParserException;
        //  78     91     230    284    Ljava/io/IOException;
        //  78     91     225    350    Any
        //  91     98     284    338    Lorg/xmlpull/v1/XmlPullParserException;
        //  91     98     230    284    Ljava/io/IOException;
        //  91     98     225    350    Any
        //  107    111    352    356    Ljava/io/IOException;
        //  127    157    284    338    Lorg/xmlpull/v1/XmlPullParserException;
        //  127    157    230    284    Ljava/io/IOException;
        //  127    157    225    350    Any
        //  157    222    284    338    Lorg/xmlpull/v1/XmlPullParserException;
        //  157    222    230    284    Ljava/io/IOException;
        //  157    222    225    350    Any
        //  232    277    225    350    Any
        //  285    331    225    350    Any
        //  343    347    356    360    Ljava/io/IOException;
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
            //   203: ifnull          397
            //   206: aload_1        
            //   207: invokevirtual   java/io/FileOutputStream.close:()V
            //   210: goto            397
            //   213: astore_3       
            //   214: goto            399
            //   217: astore_2       
            //   218: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   221: astore_3       
            //   222: new             Ljava/lang/StringBuilder;
            //   225: astore          4
            //   227: aload           4
            //   229: invokespecial   java/lang/StringBuilder.<init>:()V
            //   232: aload           4
            //   234: ldc             "Error writing historical record file: "
            //   236: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   239: pop            
            //   240: aload           4
            //   242: aload_0        
            //   243: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   246: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   249: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   252: pop            
            //   253: aload_3        
            //   254: aload           4
            //   256: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   259: aload_2        
            //   260: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   263: pop            
            //   264: aload_0        
            //   265: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   268: iconst_1       
            //   269: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   272: aload_1        
            //   273: ifnull          397
            //   276: goto            206
            //   279: astore          4
            //   281: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   284: astore_2       
            //   285: new             Ljava/lang/StringBuilder;
            //   288: astore_3       
            //   289: aload_3        
            //   290: invokespecial   java/lang/StringBuilder.<init>:()V
            //   293: aload_3        
            //   294: ldc             "Error writing historical record file: "
            //   296: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   299: pop            
            //   300: aload_3        
            //   301: aload_0        
            //   302: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   305: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   308: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   311: pop            
            //   312: aload_2        
            //   313: aload_3        
            //   314: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   317: aload           4
            //   319: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   322: pop            
            //   323: aload_0        
            //   324: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   327: iconst_1       
            //   328: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   331: aload_1        
            //   332: ifnull          397
            //   335: goto            206
            //   338: astore          4
            //   340: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   343: astore_2       
            //   344: new             Ljava/lang/StringBuilder;
            //   347: astore_3       
            //   348: aload_3        
            //   349: invokespecial   java/lang/StringBuilder.<init>:()V
            //   352: aload_3        
            //   353: ldc             "Error writing historical record file: "
            //   355: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   358: pop            
            //   359: aload_3        
            //   360: aload_0        
            //   361: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   364: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   367: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   370: pop            
            //   371: aload_2        
            //   372: aload_3        
            //   373: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   376: aload           4
            //   378: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   381: pop            
            //   382: aload_0        
            //   383: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   386: iconst_1       
            //   387: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   390: aload_1        
            //   391: ifnull          397
            //   394: goto            206
            //   397: aconst_null    
            //   398: areturn        
            //   399: aload_0        
            //   400: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   403: iconst_1       
            //   404: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   407: aload_1        
            //   408: ifnull          415
            //   411: aload_1        
            //   412: invokevirtual   java/io/FileOutputStream.close:()V
            //   415: aload_3        
            //   416: athrow         
            //   417: astore          4
            //   419: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   422: astore_1       
            //   423: new             Ljava/lang/StringBuilder;
            //   426: dup            
            //   427: invokespecial   java/lang/StringBuilder.<init>:()V
            //   430: astore_2       
            //   431: aload_2        
            //   432: ldc             "Error writing historical record file: "
            //   434: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   437: pop            
            //   438: aload_2        
            //   439: aload_3        
            //   440: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   443: pop            
            //   444: aload_1        
            //   445: aload_2        
            //   446: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   449: aload           4
            //   451: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   454: pop            
            //   455: aconst_null    
            //   456: areturn        
            //   457: astore_1       
            //   458: goto            397
            //   461: astore_1       
            //   462: goto            415
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                                
            //  -----  -----  -----  -----  ------------------------------------
            //  14     27     417    457    Ljava/io/FileNotFoundException;
            //  32     73     338    397    Ljava/lang/IllegalArgumentException;
            //  32     73     279    338    Ljava/lang/IllegalStateException;
            //  32     73     217    279    Ljava/io/IOException;
            //  32     73     213    417    Any
            //  83     170    338    397    Ljava/lang/IllegalArgumentException;
            //  83     170    279    338    Ljava/lang/IllegalStateException;
            //  83     170    217    279    Ljava/io/IOException;
            //  83     170    213    417    Any
            //  176    194    338    397    Ljava/lang/IllegalArgumentException;
            //  176    194    279    338    Ljava/lang/IllegalStateException;
            //  176    194    217    279    Ljava/io/IOException;
            //  176    194    213    417    Any
            //  206    210    457    461    Ljava/io/IOException;
            //  218    264    213    417    Any
            //  281    323    213    417    Any
            //  340    382    213    417    Any
            //  411    415    461    465    Ljava/io/IOException;
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
