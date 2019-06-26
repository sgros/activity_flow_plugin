package menion.android.whereyougo.utils;

import android.os.Environment;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Locale;

public class FileSystem {
   public static String CACHE = "cache/";
   public static String CACHE_AUDIO;
   private static final String CARD_ROOT = "{CARD_ROOT}";
   private static final String[] EXTERNAL_DIRECTORIES = new String[]{"{CARD_ROOT}external_sd", "{CARD_ROOT}_externalsd", "{CARD_ROOT}sd", "{CARD_ROOT}emmc", "{CARD_ROOT}ext_sd", "/Removable/MicroSD", "/mnt/emms", "/mnt/external1"};
   public static String ROOT = null;
   private static final String TAG = "FileSystem";

   static {
      CACHE_AUDIO = CACHE + "audio/";
   }

   public static boolean backupFile(File var0) {
      boolean var3;
      try {
         if (var0.length() > 0L) {
            StringBuilder var2 = new StringBuilder();
            File var1 = new File(var2.append(var0.getAbsolutePath()).append(".bak").toString());
            copyFile(var0, var1);
         }
      } catch (IOException var4) {
         var3 = false;
         return var3;
      }

      var3 = true;
      return var3;
   }

   public static void checkFolders(String var0) {
      try {
         File var1 = new File(var0);
         var1.getParentFile().mkdirs();
      } catch (Exception var2) {
         Logger.e("FileSystem", "checkFolders(" + var0 + "), ex: " + var2.toString());
      }

   }

   public static void copyFile(File var0, File var1) throws IOException {
      if (!var0.equals(var1)) {
         if (!var1.exists()) {
            var1.createNewFile();
         }

         FileChannel var2 = null;
         FileChannel var3 = null;
         FileChannel var4 = var3;
         FileChannel var5 = var2;

         label763: {
            Throwable var10000;
            label764: {
               FileInputStream var6;
               boolean var10001;
               try {
                  var6 = new FileInputStream;
               } catch (Throwable var94) {
                  var10000 = var94;
                  var10001 = false;
                  break label764;
               }

               var4 = var3;
               var5 = var2;

               try {
                  var6.<init>(var0);
               } catch (Throwable var93) {
                  var10000 = var93;
                  var10001 = false;
                  break label764;
               }

               var4 = var3;
               var5 = var2;

               try {
                  var2 = var6.getChannel();
               } catch (Throwable var92) {
                  var10000 = var92;
                  var10001 = false;
                  break label764;
               }

               var4 = var3;
               var5 = var2;

               FileOutputStream var96;
               try {
                  var96 = new FileOutputStream;
               } catch (Throwable var91) {
                  var10000 = var91;
                  var10001 = false;
                  break label764;
               }

               var4 = var3;
               var5 = var2;

               try {
                  var96.<init>(var1);
               } catch (Throwable var90) {
                  var10000 = var90;
                  var10001 = false;
                  break label764;
               }

               var4 = var3;
               var5 = var2;

               try {
                  var3 = var96.getChannel();
               } catch (Throwable var89) {
                  var10000 = var89;
                  var10001 = false;
                  break label764;
               }

               var4 = var3;
               var5 = var2;

               label730:
               try {
                  var3.transferFrom(var2, 0L, var2.size());
                  break label763;
               } catch (Throwable var88) {
                  var10000 = var88;
                  var10001 = false;
                  break label730;
               }
            }

            Throwable var95 = var10000;
            if (var5 != null) {
               try {
                  var5.close();
               } catch (IOException var85) {
               }
            }

            if (var4 != null) {
               try {
                  var4.close();
               } catch (IOException var84) {
               }
            }

            throw var95;
         }

         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var87) {
            }
         }

         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var86) {
            }
         }

         var1.setLastModified(var0.lastModified());
      }

   }

   public static boolean createRoot(String var0) {
      boolean var1 = false;
      boolean var2;
      if (ROOT != null && (new File(ROOT)).exists()) {
         var2 = true;
      } else {
         Exception var10000;
         label101: {
            String var3;
            boolean var10001;
            try {
               var3 = getExternalStorageDir();
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break label101;
            }

            var2 = var1;
            if (var3 == null) {
               return var2;
            }

            StringBuilder var4 = null;

            String[] var5;
            int var6;
            try {
               var5 = EXTERNAL_DIRECTORIES;
               var6 = var5.length;
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label101;
            }

            int var7 = 0;

            String var8;
            while(true) {
               var8 = var4;
               if (var7 >= var6) {
                  break;
               }

               String var9 = var5[var7];
               var8 = var9;

               try {
                  if (var9.contains("{CARD_ROOT}")) {
                     var8 = var9.replace("{CARD_ROOT}", var3);
                  }
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label101;
               }

               try {
                  File var22 = new File(var8);
                  if (var22.exists()) {
                     var4 = new StringBuilder();
                     var8 = var4.append(var8).append("/").toString();
                     break;
                  }
               } catch (Exception var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label101;
               }

               ++var7;
            }

            File var20;
            StringBuilder var23;
            try {
               var23 = new StringBuilder();
               var20 = new File(var23.append(var3).append(var0).toString());
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label101;
            }

            if (var8 == null) {
               try {
                  var2 = setRootDirectory(var3, var20.getAbsolutePath());
                  return var2;
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
               }
            } else {
               label103: {
                  File var21;
                  try {
                     var23 = new StringBuilder();
                     var21 = new File(var23.append(var8).append(var0).toString());
                     if (var21.exists()) {
                        var2 = setRootDirectory(var8, var21.getAbsolutePath());
                        return var2;
                     }
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label103;
                  }

                  try {
                     if (var20.exists()) {
                        var2 = setRootDirectory(var3, var20.getAbsolutePath());
                        return var2;
                     }
                  } catch (Exception var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label103;
                  }

                  try {
                     var2 = setRootDirectory(var8, var21.getAbsolutePath());
                     return var2;
                  } catch (Exception var11) {
                     var10000 = var11;
                     var10001 = false;
                  }
               }
            }
         }

         Exception var19 = var10000;
         Logger.e("FileSystem", "createRoot(), ex: " + var19.toString());
         var2 = var1;
      }

      return var2;
   }

   public static File findFile(String var0) {
      return findFile(var0, "gwc");
   }

   public static File findFile(String var0, String var1) {
      File[] var2 = getFiles(ROOT, var1);
      File var6;
      if (var2 == null) {
         var6 = null;
      } else {
         int var3 = var2.length;
         int var4 = 0;

         while(true) {
            if (var4 >= var3) {
               var6 = null;
               break;
            }

            File var5 = var2[var4];
            var6 = var5;
            if (var5.getName().startsWith(var0)) {
               break;
            }

            ++var4;
         }
      }

      return var6;
   }

   public static String getExternalStorageDir() {
      String var0 = Environment.getExternalStorageDirectory().getAbsolutePath();
      String var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = var0;
         if (!var0.endsWith("/")) {
            var1 = var0 + "/";
         }
      }

      return var1;
   }

   public static File[] getFiles(String var0, final String var1) {
      return getFiles2(var0, new FileFilter() {
         public boolean accept(File var1x) {
            return var1x.getName().toLowerCase(Locale.getDefault()).endsWith(var1);
         }
      });
   }

   public static File[] getFiles2(String var0, FileFilter var1) {
      File[] var4;
      File[] var5;
      label32: {
         try {
            File var2 = new File(var0);
            if (!var2.exists()) {
               var5 = new File[0];
               break label32;
            }

            var5 = var2.listFiles(var1);
         } catch (Exception var3) {
            Logger.e("FileSystem", "getFiles2(), folder: " + var0);
            var4 = new File[0];
            return var4;
         }

         var4 = var5;
         return var4;
      }

      var4 = var5;
      return var4;
   }

   public static String getRoot() {
      if (ROOT == null) {
         createRoot("WhereYouGo");
      }

      return ROOT;
   }

   public static void saveBytes(String param0, byte[] param1) {
      // $FF: Couldn't be decompiled
   }

   public static boolean setRootDirectory(String var0) {
      return setRootDirectory((String)null, var0);
   }

   private static boolean setRootDirectory(String var0, String var1) {
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 != null) {
         if (var1.equals("")) {
            var3 = var2;
         } else {
            var0 = var1;
            if (!var1.endsWith("/")) {
               var0 = var1 + "/";
            }

            File var4 = new File(var0);
            if (!var4.exists()) {
               var3 = var2;
               if (!var4.mkdir()) {
                  return var3;
               }
            }

            ROOT = var0;
            var3 = true;
         }
      }

      return var3;
   }
}
