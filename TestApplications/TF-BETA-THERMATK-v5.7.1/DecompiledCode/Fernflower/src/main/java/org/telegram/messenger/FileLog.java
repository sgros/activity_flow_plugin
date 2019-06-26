package org.telegram.messenger;

import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Locale;
import org.telegram.messenger.time.FastDateFormat;

public class FileLog {
   private static volatile FileLog Instance;
   private static final String tag = "tmessages";
   private File currentFile = null;
   private FastDateFormat dateFormat = null;
   private boolean initied;
   private DispatchQueue logQueue = null;
   private File networkFile = null;
   private OutputStreamWriter streamWriter = null;

   public FileLog() {
      if (BuildVars.LOGS_ENABLED) {
         this.init();
      }
   }

   public static void cleanupLogs() {
      ensureInitied();
      File var0 = ApplicationLoader.applicationContext.getExternalFilesDir((String)null);
      if (var0 != null) {
         StringBuilder var1 = new StringBuilder();
         var1.append(var0.getAbsolutePath());
         var1.append("/logs");
         File[] var3 = (new File(var1.toString())).listFiles();
         if (var3 != null) {
            for(int var2 = 0; var2 < var3.length; ++var2) {
               var0 = var3[var2];
               if ((getInstance().currentFile == null || !var0.getAbsolutePath().equals(getInstance().currentFile.getAbsolutePath())) && (getInstance().networkFile == null || !var0.getAbsolutePath().equals(getInstance().networkFile.getAbsolutePath()))) {
                  var0.delete();
               }
            }
         }

      }
   }

   public static void d(String var0) {
      if (BuildVars.LOGS_ENABLED) {
         ensureInitied();
         Log.d("tmessages", var0);
         if (getInstance().streamWriter != null) {
            getInstance().logQueue.postRunnable(new _$$Lambda$FileLog$eWlFO8runhKiadqnBdmm0EveMAs(var0));
         }

      }
   }

   public static void e(String var0) {
      if (BuildVars.LOGS_ENABLED) {
         ensureInitied();
         Log.e("tmessages", var0);
         if (getInstance().streamWriter != null) {
            getInstance().logQueue.postRunnable(new _$$Lambda$FileLog$i7yDAXBrl68gcx0blG9TP5Nmrmw(var0));
         }

      }
   }

   public static void e(String var0, Throwable var1) {
      if (BuildVars.LOGS_ENABLED) {
         ensureInitied();
         Log.e("tmessages", var0, var1);
         if (getInstance().streamWriter != null) {
            getInstance().logQueue.postRunnable(new _$$Lambda$FileLog$U3_7mjWjDO5tBtPnAliQ7P97K6k(var0, var1));
         }

      }
   }

   public static void e(Throwable var0) {
      if (BuildVars.LOGS_ENABLED) {
         ensureInitied();
         var0.printStackTrace();
         if (getInstance().streamWriter != null) {
            getInstance().logQueue.postRunnable(new _$$Lambda$FileLog$nIyr75ATOMaf7ha7_MTqnmuRzrQ(var0));
         } else {
            var0.printStackTrace();
         }

      }
   }

   public static void ensureInitied() {
      getInstance().init();
   }

   public static FileLog getInstance() {
      FileLog var0 = Instance;
      FileLog var1 = var0;
      if (var0 == null) {
         synchronized(FileLog.class){}

         Throwable var10000;
         boolean var10001;
         label206: {
            try {
               var0 = Instance;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label206;
            }

            var1 = var0;
            if (var0 == null) {
               try {
                  var1 = new FileLog();
                  Instance = var1;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label206;
               }
            }

            label193:
            try {
               return var1;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label193;
            }
         }

         while(true) {
            Throwable var22 = var10000;

            try {
               throw var22;
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               continue;
            }
         }
      } else {
         return var1;
      }
   }

   public static String getNetworkLogPath() {
      if (!BuildVars.LOGS_ENABLED) {
         return "";
      } else {
         Throwable var10000;
         label29: {
            File var0;
            boolean var10001;
            try {
               var0 = ApplicationLoader.applicationContext.getExternalFilesDir((String)null);
            } catch (Throwable var5) {
               var10000 = var5;
               var10001 = false;
               break label29;
            }

            if (var0 == null) {
               return "";
            }

            try {
               StringBuilder var2 = new StringBuilder();
               var2.append(var0.getAbsolutePath());
               var2.append("/logs");
               File var7 = new File(var2.toString());
               var7.mkdirs();
               FileLog var9 = getInstance();
               StringBuilder var6 = new StringBuilder();
               var6.append(getInstance().dateFormat.format(System.currentTimeMillis()));
               var6.append("_net.txt");
               File var3 = new File(var7, var6.toString());
               var9.networkFile = var3;
               String var8 = getInstance().networkFile.getAbsolutePath();
               return var8;
            } catch (Throwable var4) {
               var10000 = var4;
               var10001 = false;
            }
         }

         Throwable var1 = var10000;
         var1.printStackTrace();
         return "";
      }
   }

   // $FF: synthetic method
   static void lambda$d$3(String var0) {
      try {
         OutputStreamWriter var1 = getInstance().streamWriter;
         StringBuilder var2 = new StringBuilder();
         var2.append(getInstance().dateFormat.format(System.currentTimeMillis()));
         var2.append(" D/tmessages: ");
         var2.append(var0);
         var2.append("\n");
         var1.write(var2.toString());
         getInstance().streamWriter.flush();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   // $FF: synthetic method
   static void lambda$e$0(String var0, Throwable var1) {
      try {
         OutputStreamWriter var2 = getInstance().streamWriter;
         StringBuilder var3 = new StringBuilder();
         var3.append(getInstance().dateFormat.format(System.currentTimeMillis()));
         var3.append(" E/tmessages: ");
         var3.append(var0);
         var3.append("\n");
         var2.write(var3.toString());
         getInstance().streamWriter.write(var1.toString());
         getInstance().streamWriter.flush();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   // $FF: synthetic method
   static void lambda$e$1(String var0) {
      try {
         OutputStreamWriter var1 = getInstance().streamWriter;
         StringBuilder var2 = new StringBuilder();
         var2.append(getInstance().dateFormat.format(System.currentTimeMillis()));
         var2.append(" E/tmessages: ");
         var2.append(var0);
         var2.append("\n");
         var1.write(var2.toString());
         getInstance().streamWriter.flush();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   // $FF: synthetic method
   static void lambda$e$2(Throwable param0) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   static void lambda$w$4(String var0) {
      try {
         OutputStreamWriter var1 = getInstance().streamWriter;
         StringBuilder var2 = new StringBuilder();
         var2.append(getInstance().dateFormat.format(System.currentTimeMillis()));
         var2.append(" W/tmessages: ");
         var2.append(var0);
         var2.append("\n");
         var1.write(var2.toString());
         getInstance().streamWriter.flush();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void w(String var0) {
      if (BuildVars.LOGS_ENABLED) {
         ensureInitied();
         Log.w("tmessages", var0);
         if (getInstance().streamWriter != null) {
            getInstance().logQueue.postRunnable(new _$$Lambda$FileLog$CtxtnEkTmpoT5uv6cLRigOvvNc8(var0));
         }

      }
   }

   public void init() {
      if (!this.initied) {
         this.dateFormat = FastDateFormat.getInstance("dd_MM_yyyy_HH_mm_ss", Locale.US);

         StringBuilder var7;
         label35: {
            Exception var10000;
            label43: {
               File var1;
               boolean var10001;
               try {
                  var1 = ApplicationLoader.applicationContext.getExternalFilesDir((String)null);
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label43;
               }

               if (var1 == null) {
                  return;
               }

               try {
                  StringBuilder var3 = new StringBuilder();
                  var3.append(var1.getAbsolutePath());
                  var3.append("/logs");
                  File var9 = new File(var3.toString());
                  var9.mkdirs();
                  var7 = new StringBuilder();
                  var7.append(this.dateFormat.format(System.currentTimeMillis()));
                  var7.append(".txt");
                  File var13 = new File(var9, var7.toString());
                  this.currentFile = var13;
                  break label35;
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
               }
            }

            Exception var2 = var10000;
            var2.printStackTrace();
         }

         try {
            DispatchQueue var10 = new DispatchQueue("logQueue");
            this.logQueue = var10;
            this.currentFile.createNewFile();
            FileOutputStream var11 = new FileOutputStream(this.currentFile);
            OutputStreamWriter var8 = new OutputStreamWriter(var11);
            this.streamWriter = var8;
            OutputStreamWriter var12 = this.streamWriter;
            var7 = new StringBuilder();
            var7.append("-----start log ");
            var7.append(this.dateFormat.format(System.currentTimeMillis()));
            var7.append("-----\n");
            var12.write(var7.toString());
            this.streamWriter.flush();
         } catch (Exception var4) {
            var4.printStackTrace();
         }

         this.initied = true;
      }
   }
}
