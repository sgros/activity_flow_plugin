// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.measurement;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.io.IOException;
import java.io.Closeable;
import org.mozilla.telemetry.util.IOUtils;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import org.mozilla.telemetry.event.TelemetryEvent;
import org.json.JSONArray;
import android.content.SharedPreferences;
import mozilla.components.support.base.log.logger.Logger;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class EventsMeasurement extends TelemetryMeasurement
{
    private static final String LOG_TAG = "EventsMeasurement";
    private TelemetryConfiguration configuration;
    private Logger logger;
    
    public EventsMeasurement(final TelemetryConfiguration configuration) {
        super("events");
        this.configuration = configuration;
        this.logger = new Logger("telemetry/events");
    }
    
    private void countEvent() {
        synchronized (this) {
            final SharedPreferences sharedPreferences = this.configuration.getSharedPreferences();
            sharedPreferences.edit().putLong("event_count", sharedPreferences.getLong("event_count", 0L) + 1L).apply();
        }
    }
    
    private JSONArray readAndClearEventsFromDisk() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: monitorenter   
        //     2: new             Lorg/json/JSONArray;
        //     5: astore_1       
        //     6: aload_1        
        //     7: invokespecial   org/json/JSONArray.<init>:()V
        //    10: aload_0        
        //    11: invokevirtual   org/mozilla/telemetry/measurement/EventsMeasurement.getEventFile:()Ljava/io/File;
        //    14: astore_2       
        //    15: aconst_null    
        //    16: astore_3       
        //    17: aconst_null    
        //    18: astore          4
        //    20: aload           4
        //    22: astore          5
        //    24: new             Ljava/io/FileInputStream;
        //    27: astore          6
        //    29: aload           4
        //    31: astore          5
        //    33: aload           6
        //    35: aload_2        
        //    36: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    39: aload           6
        //    41: astore          5
        //    43: new             Ljava/io/BufferedReader;
        //    46: astore_3       
        //    47: aload           6
        //    49: astore          5
        //    51: new             Ljava/io/InputStreamReader;
        //    54: astore          4
        //    56: aload           6
        //    58: astore          5
        //    60: aload           4
        //    62: aload           6
        //    64: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //    67: aload           6
        //    69: astore          5
        //    71: aload_3        
        //    72: aload           4
        //    74: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //    77: aload           6
        //    79: astore          5
        //    81: aload_3        
        //    82: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //    85: astore          7
        //    87: aload           7
        //    89: ifnull          154
        //    92: aload           6
        //    94: astore          5
        //    96: new             Lorg/json/JSONArray;
        //    99: astore          4
        //   101: aload           6
        //   103: astore          5
        //   105: aload           4
        //   107: aload           7
        //   109: invokespecial   org/json/JSONArray.<init>:(Ljava/lang/String;)V
        //   112: aload           6
        //   114: astore          5
        //   116: aload_1        
        //   117: aload           4
        //   119: invokevirtual   org/json/JSONArray.put:(Ljava/lang/Object;)Lorg/json/JSONArray;
        //   122: pop            
        //   123: aload           6
        //   125: astore          5
        //   127: aload_0        
        //   128: invokespecial   org/mozilla/telemetry/measurement/EventsMeasurement.resetEventCount:()V
        //   131: goto            77
        //   134: astore          4
        //   136: aload           6
        //   138: astore          5
        //   140: aload_0        
        //   141: getfield        org/mozilla/telemetry/measurement/EventsMeasurement.logger:Lmozilla/components/support/base/log/logger/Logger;
        //   144: ldc             "Could not parse event from disk"
        //   146: aload           4
        //   148: invokevirtual   mozilla/components/support/base/log/logger/Logger.warn:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   151: goto            77
        //   154: aload           6
        //   156: invokestatic    org/mozilla/telemetry/util/IOUtils.safeClose:(Ljava/io/Closeable;)V
        //   159: aload_2        
        //   160: invokevirtual   java/io/File.delete:()Z
        //   163: ifne            256
        //   166: aload_0        
        //   167: getfield        org/mozilla/telemetry/measurement/EventsMeasurement.logger:Lmozilla/components/support/base/log/logger/Logger;
        //   170: astore          5
        //   172: new             Ljava/io/IOException;
        //   175: dup            
        //   176: invokespecial   java/io/IOException.<init>:()V
        //   179: astore          6
        //   181: aload           5
        //   183: ldc             "Events file could not be deleted"
        //   185: aload           6
        //   187: invokevirtual   mozilla/components/support/base/log/logger/Logger.warn:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   190: goto            256
        //   193: astore_3       
        //   194: goto            211
        //   197: astore          5
        //   199: goto            265
        //   202: astore          6
        //   204: goto            318
        //   207: astore_3       
        //   208: aconst_null    
        //   209: astore          6
        //   211: aload           6
        //   213: astore          5
        //   215: aload_0        
        //   216: getfield        org/mozilla/telemetry/measurement/EventsMeasurement.logger:Lmozilla/components/support/base/log/logger/Logger;
        //   219: ldc             "IOException while reading events from disk"
        //   221: aload_3        
        //   222: invokevirtual   mozilla/components/support/base/log/logger/Logger.warn:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   225: aload           6
        //   227: invokestatic    org/mozilla/telemetry/util/IOUtils.safeClose:(Ljava/io/Closeable;)V
        //   230: aload_2        
        //   231: invokevirtual   java/io/File.delete:()Z
        //   234: ifne            256
        //   237: aload_0        
        //   238: getfield        org/mozilla/telemetry/measurement/EventsMeasurement.logger:Lmozilla/components/support/base/log/logger/Logger;
        //   241: astore          5
        //   243: new             Ljava/io/IOException;
        //   246: astore          6
        //   248: aload           6
        //   250: invokespecial   java/io/IOException.<init>:()V
        //   253: goto            181
        //   256: aload_0        
        //   257: monitorexit    
        //   258: aload_1        
        //   259: areturn        
        //   260: astore          6
        //   262: goto            318
        //   265: aload           6
        //   267: astore          5
        //   269: new             Lorg/json/JSONArray;
        //   272: dup            
        //   273: invokespecial   org/json/JSONArray.<init>:()V
        //   276: astore_3       
        //   277: aload           6
        //   279: invokestatic    org/mozilla/telemetry/util/IOUtils.safeClose:(Ljava/io/Closeable;)V
        //   282: aload_2        
        //   283: invokevirtual   java/io/File.delete:()Z
        //   286: ifne            314
        //   289: aload_0        
        //   290: getfield        org/mozilla/telemetry/measurement/EventsMeasurement.logger:Lmozilla/components/support/base/log/logger/Logger;
        //   293: astore          5
        //   295: new             Ljava/io/IOException;
        //   298: astore          6
        //   300: aload           6
        //   302: invokespecial   java/io/IOException.<init>:()V
        //   305: aload           5
        //   307: ldc             "Events file could not be deleted"
        //   309: aload           6
        //   311: invokevirtual   mozilla/components/support/base/log/logger/Logger.warn:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   314: aload_0        
        //   315: monitorexit    
        //   316: aload_3        
        //   317: areturn        
        //   318: aload           5
        //   320: invokestatic    org/mozilla/telemetry/util/IOUtils.safeClose:(Ljava/io/Closeable;)V
        //   323: aload_2        
        //   324: invokevirtual   java/io/File.delete:()Z
        //   327: ifne            352
        //   330: aload_0        
        //   331: getfield        org/mozilla/telemetry/measurement/EventsMeasurement.logger:Lmozilla/components/support/base/log/logger/Logger;
        //   334: astore          5
        //   336: new             Ljava/io/IOException;
        //   339: astore_3       
        //   340: aload_3        
        //   341: invokespecial   java/io/IOException.<init>:()V
        //   344: aload           5
        //   346: ldc             "Events file could not be deleted"
        //   348: aload_3        
        //   349: invokevirtual   mozilla/components/support/base/log/logger/Logger.warn:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   352: aload           6
        //   354: athrow         
        //   355: astore          6
        //   357: aload_0        
        //   358: monitorexit    
        //   359: aload           6
        //   361: athrow         
        //   362: astore          6
        //   364: aload_3        
        //   365: astore          6
        //   367: goto            265
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                           
        //  -----  -----  -----  -----  -------------------------------
        //  2      15     355    362    Any
        //  24     29     362    370    Ljava/io/FileNotFoundException;
        //  24     29     207    211    Ljava/io/IOException;
        //  24     29     202    207    Any
        //  33     39     362    370    Ljava/io/FileNotFoundException;
        //  33     39     207    211    Ljava/io/IOException;
        //  33     39     202    207    Any
        //  43     47     197    202    Ljava/io/FileNotFoundException;
        //  43     47     193    197    Ljava/io/IOException;
        //  43     47     260    265    Any
        //  51     56     197    202    Ljava/io/FileNotFoundException;
        //  51     56     193    197    Ljava/io/IOException;
        //  51     56     260    265    Any
        //  60     67     197    202    Ljava/io/FileNotFoundException;
        //  60     67     193    197    Ljava/io/IOException;
        //  60     67     260    265    Any
        //  71     77     197    202    Ljava/io/FileNotFoundException;
        //  71     77     193    197    Ljava/io/IOException;
        //  71     77     260    265    Any
        //  81     87     197    202    Ljava/io/FileNotFoundException;
        //  81     87     193    197    Ljava/io/IOException;
        //  81     87     260    265    Any
        //  96     101    134    154    Lorg/json/JSONException;
        //  96     101    197    202    Ljava/io/FileNotFoundException;
        //  96     101    193    197    Ljava/io/IOException;
        //  96     101    260    265    Any
        //  105    112    134    154    Lorg/json/JSONException;
        //  105    112    197    202    Ljava/io/FileNotFoundException;
        //  105    112    193    197    Ljava/io/IOException;
        //  105    112    260    265    Any
        //  116    123    134    154    Lorg/json/JSONException;
        //  116    123    197    202    Ljava/io/FileNotFoundException;
        //  116    123    193    197    Ljava/io/IOException;
        //  116    123    260    265    Any
        //  127    131    134    154    Lorg/json/JSONException;
        //  127    131    197    202    Ljava/io/FileNotFoundException;
        //  127    131    193    197    Ljava/io/IOException;
        //  127    131    260    265    Any
        //  140    151    197    202    Ljava/io/FileNotFoundException;
        //  140    151    193    197    Ljava/io/IOException;
        //  140    151    260    265    Any
        //  154    181    355    362    Any
        //  181    190    355    362    Any
        //  215    225    260    265    Any
        //  225    253    355    362    Any
        //  269    277    202    207    Any
        //  277    314    355    362    Any
        //  318    352    355    362    Any
        //  352    355    355    362    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0077:
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
    
    private void resetEventCount() {
        synchronized (this) {
            this.configuration.getSharedPreferences().edit().putLong("event_count", 0L).apply();
        }
    }
    
    private void saveEventToDisk(final TelemetryEvent telemetryEvent) {
        // monitorenter(this)
        Writer out = null;
        OutputStream outputStream = null;
        OutputStream out2 = null;
        try {
            try {
                outputStream = outputStream;
                out2 = new FileOutputStream(this.getEventFile(), true);
                try {
                    out = new OutputStreamWriter(out2);
                    final BufferedWriter bufferedWriter = new BufferedWriter(out);
                    bufferedWriter.write(telemetryEvent.toJSON());
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    this.countEvent();
                    final OutputStream outputStream2 = out2;
                    IOUtils.safeClose(outputStream2);
                }
                catch (IOException ex2) {
                    final Closeable closeable = out2;
                    final IOException ex = ex2;
                }
            }
            finally {
                out2 = outputStream;
            }
        }
        catch (IOException ex) {
            final Closeable closeable = out;
        }
        try {
            final OutputStream outputStream2 = out2;
            IOUtils.safeClose(outputStream2);
            return;
            final IOException ex;
            this.logger.warn("IOException while writing event to disk", ex);
            final Closeable closeable;
            IOUtils.safeClose(closeable);
            return;
            IOUtils.safeClose(out2);
        }
        finally {
        }
        // monitorexit(this)
    }
    
    public void add(final TelemetryEvent telemetryEvent) {
        this.saveEventToDisk(telemetryEvent);
    }
    
    @Override
    public Object flush() {
        return this.readAndClearEventsFromDisk();
    }
    
    public long getEventCount() {
        return this.configuration.getSharedPreferences().getLong("event_count", 0L);
    }
    
    File getEventFile() {
        return new File(this.configuration.getDataDirectory(), "events1");
    }
}
