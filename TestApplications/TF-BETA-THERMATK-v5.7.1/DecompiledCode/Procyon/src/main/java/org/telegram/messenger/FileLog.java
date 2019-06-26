// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.Locale;
import android.util.Log;
import java.io.OutputStreamWriter;
import org.telegram.messenger.time.FastDateFormat;
import java.io.File;

public class FileLog
{
    private static volatile FileLog Instance;
    private static final String tag = "tmessages";
    private File currentFile;
    private FastDateFormat dateFormat;
    private boolean initied;
    private DispatchQueue logQueue;
    private File networkFile;
    private OutputStreamWriter streamWriter;
    
    public FileLog() {
        this.streamWriter = null;
        this.dateFormat = null;
        this.logQueue = null;
        this.currentFile = null;
        this.networkFile = null;
        if (!BuildVars.LOGS_ENABLED) {
            return;
        }
        this.init();
    }
    
    public static void cleanupLogs() {
        ensureInitied();
        final File externalFilesDir = ApplicationLoader.applicationContext.getExternalFilesDir((String)null);
        if (externalFilesDir == null) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(externalFilesDir.getAbsolutePath());
        sb.append("/logs");
        final File[] listFiles = new File(sb.toString()).listFiles();
        if (listFiles != null) {
            for (int i = 0; i < listFiles.length; ++i) {
                final File file = listFiles[i];
                if (getInstance().currentFile == null || !file.getAbsolutePath().equals(getInstance().currentFile.getAbsolutePath())) {
                    if (getInstance().networkFile == null || !file.getAbsolutePath().equals(getInstance().networkFile.getAbsolutePath())) {
                        file.delete();
                    }
                }
            }
        }
    }
    
    public static void d(final String s) {
        if (!BuildVars.LOGS_ENABLED) {
            return;
        }
        ensureInitied();
        Log.d("tmessages", s);
        if (getInstance().streamWriter != null) {
            getInstance().logQueue.postRunnable(new _$$Lambda$FileLog$eWlFO8runhKiadqnBdmm0EveMAs(s));
        }
    }
    
    public static void e(final String s) {
        if (!BuildVars.LOGS_ENABLED) {
            return;
        }
        ensureInitied();
        Log.e("tmessages", s);
        if (getInstance().streamWriter != null) {
            getInstance().logQueue.postRunnable(new _$$Lambda$FileLog$i7yDAXBrl68gcx0blG9TP5Nmrmw(s));
        }
    }
    
    public static void e(final String s, final Throwable t) {
        if (!BuildVars.LOGS_ENABLED) {
            return;
        }
        ensureInitied();
        Log.e("tmessages", s, t);
        if (getInstance().streamWriter != null) {
            getInstance().logQueue.postRunnable(new _$$Lambda$FileLog$U3_7mjWjDO5tBtPnAliQ7P97K6k(s, t));
        }
    }
    
    public static void e(final Throwable t) {
        if (!BuildVars.LOGS_ENABLED) {
            return;
        }
        ensureInitied();
        t.printStackTrace();
        if (getInstance().streamWriter != null) {
            getInstance().logQueue.postRunnable(new _$$Lambda$FileLog$nIyr75ATOMaf7ha7_MTqnmuRzrQ(t));
        }
        else {
            t.printStackTrace();
        }
    }
    
    public static void ensureInitied() {
        getInstance().init();
    }
    
    public static FileLog getInstance() {
        final FileLog instance;
        if ((instance = FileLog.Instance) == null) {
            synchronized (FileLog.class) {
                if (FileLog.Instance == null) {
                    FileLog.Instance = new FileLog();
                }
            }
        }
        return instance;
    }
    
    public static String getNetworkLogPath() {
        if (!BuildVars.LOGS_ENABLED) {
            return "";
        }
        try {
            final File externalFilesDir = ApplicationLoader.applicationContext.getExternalFilesDir((String)null);
            if (externalFilesDir == null) {
                return "";
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(externalFilesDir.getAbsolutePath());
            sb.append("/logs");
            final File parent = new File(sb.toString());
            parent.mkdirs();
            final FileLog instance = getInstance();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(getInstance().dateFormat.format(System.currentTimeMillis()));
            sb2.append("_net.txt");
            instance.networkFile = new File(parent, sb2.toString());
            return getInstance().networkFile.getAbsolutePath();
        }
        catch (Throwable t) {
            t.printStackTrace();
            return "";
        }
    }
    
    public static void w(final String s) {
        if (!BuildVars.LOGS_ENABLED) {
            return;
        }
        ensureInitied();
        Log.w("tmessages", s);
        if (getInstance().streamWriter != null) {
            getInstance().logQueue.postRunnable(new _$$Lambda$FileLog$CtxtnEkTmpoT5uv6cLRigOvvNc8(s));
        }
    }
    
    public void init() {
        if (this.initied) {
            return;
        }
        this.dateFormat = FastDateFormat.getInstance("dd_MM_yyyy_HH_mm_ss", Locale.US);
        try {
            final File externalFilesDir = ApplicationLoader.applicationContext.getExternalFilesDir((String)null);
            if (externalFilesDir == null) {
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(externalFilesDir.getAbsolutePath());
            sb.append("/logs");
            final File parent = new File(sb.toString());
            parent.mkdirs();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(this.dateFormat.format(System.currentTimeMillis()));
            sb2.append(".txt");
            this.currentFile = new File(parent, sb2.toString());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            this.logQueue = new DispatchQueue("logQueue");
            this.currentFile.createNewFile();
            this.streamWriter = new OutputStreamWriter(new FileOutputStream(this.currentFile));
            final OutputStreamWriter streamWriter = this.streamWriter;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("-----start log ");
            sb3.append(this.dateFormat.format(System.currentTimeMillis()));
            sb3.append("-----\n");
            streamWriter.write(sb3.toString());
            this.streamWriter.flush();
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
        }
        this.initied = true;
    }
}
