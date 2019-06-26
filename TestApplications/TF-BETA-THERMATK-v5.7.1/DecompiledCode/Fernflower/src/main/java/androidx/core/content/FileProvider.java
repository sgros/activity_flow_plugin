package androidx.core.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.xmlpull.v1.XmlPullParserException;

public class FileProvider extends ContentProvider {
   private static final String[] COLUMNS = new String[]{"_display_name", "_size"};
   private static final File DEVICE_ROOT = new File("/");
   private static HashMap sCache = new HashMap();
   private FileProvider.PathStrategy mStrategy;

   private static File buildPath(File var0, String... var1) {
      int var2 = var1.length;

      File var5;
      for(int var3 = 0; var3 < var2; var0 = var5) {
         String var4 = var1[var3];
         var5 = var0;
         if (var4 != null) {
            var5 = new File(var0, var4);
         }

         ++var3;
      }

      return var0;
   }

   private static Object[] copyOf(Object[] var0, int var1) {
      Object[] var2 = new Object[var1];
      System.arraycopy(var0, 0, var2, 0, var1);
      return var2;
   }

   private static String[] copyOf(String[] var0, int var1) {
      String[] var2 = new String[var1];
      System.arraycopy(var0, 0, var2, 0, var1);
      return var2;
   }

   private static FileProvider.PathStrategy getPathStrategy(Context param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   public static Uri getUriForFile(Context var0, String var1, File var2) {
      return getPathStrategy(var0, var1).getUriForFile(var2);
   }

   private static int modeToMode(String var0) {
      int var1;
      if ("r".equals(var0)) {
         var1 = 268435456;
      } else if (!"w".equals(var0) && !"wt".equals(var0)) {
         if ("wa".equals(var0)) {
            var1 = 704643072;
         } else if ("rw".equals(var0)) {
            var1 = 939524096;
         } else {
            if (!"rwt".equals(var0)) {
               StringBuilder var2 = new StringBuilder();
               var2.append("Invalid mode: ");
               var2.append(var0);
               throw new IllegalArgumentException(var2.toString());
            }

            var1 = 1006632960;
         }
      } else {
         var1 = 738197504;
      }

      return var1;
   }

   private static FileProvider.PathStrategy parsePathStrategy(Context var0, String var1) throws IOException, XmlPullParserException {
      FileProvider.SimplePathStrategy var2 = new FileProvider.SimplePathStrategy(var1);
      ProviderInfo var3 = var0.getPackageManager().resolveContentProvider(var1, 128);
      if (var3 != null) {
         XmlResourceParser var4 = var3.loadXmlMetaData(var0.getPackageManager(), "android.support.FILE_PROVIDER_PATHS");
         if (var4 != null) {
            while(true) {
               int var5 = var4.next();
               if (var5 == 1) {
                  return var2;
               }

               if (var5 == 2) {
                  String var6 = var4.getName();
                  var3 = null;
                  String var7 = var4.getAttributeValue((String)null, "name");
                  String var8 = var4.getAttributeValue((String)null, "path");
                  File var10;
                  if ("root-path".equals(var6)) {
                     var10 = DEVICE_ROOT;
                  } else if ("files-path".equals(var6)) {
                     var10 = var0.getFilesDir();
                  } else if ("cache-path".equals(var6)) {
                     var10 = var0.getCacheDir();
                  } else if ("external-path".equals(var6)) {
                     var10 = Environment.getExternalStorageDirectory();
                  } else {
                     File[] var11;
                     if ("external-files-path".equals(var6)) {
                        var11 = ContextCompat.getExternalFilesDirs(var0, (String)null);
                        var10 = var3;
                        if (var11.length > 0) {
                           var10 = var11[0];
                        }
                     } else if ("external-cache-path".equals(var6)) {
                        var11 = ContextCompat.getExternalCacheDirs(var0);
                        var10 = var3;
                        if (var11.length > 0) {
                           var10 = var11[0];
                        }
                     } else {
                        var10 = var3;
                        if (VERSION.SDK_INT >= 21) {
                           var10 = var3;
                           if ("external-media-path".equals(var6)) {
                              var11 = var0.getExternalMediaDirs();
                              var10 = var3;
                              if (var11.length > 0) {
                                 var10 = var11[0];
                              }
                           }
                        }
                     }
                  }

                  if (var10 != null) {
                     var2.addRoot(var7, buildPath(var10, var8));
                  }
               }
            }
         } else {
            throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
         }
      } else {
         StringBuilder var9 = new StringBuilder();
         var9.append("Couldn't find meta-data for provider with authority ");
         var9.append(var1);
         throw new IllegalArgumentException(var9.toString());
      }
   }

   public void attachInfo(Context var1, ProviderInfo var2) {
      super.attachInfo(var1, var2);
      if (!var2.exported) {
         if (var2.grantUriPermissions) {
            this.mStrategy = getPathStrategy(var1, var2.authority);
         } else {
            throw new SecurityException("Provider must grant uri permissions");
         }
      } else {
         throw new SecurityException("Provider must not be exported");
      }
   }

   public int delete(Uri var1, String var2, String[] var3) {
      return this.mStrategy.getFileForUri(var1).delete();
   }

   public String getType(Uri var1) {
      File var3 = this.mStrategy.getFileForUri(var1);
      int var2 = var3.getName().lastIndexOf(46);
      if (var2 >= 0) {
         String var4 = var3.getName().substring(var2 + 1);
         var4 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var4);
         if (var4 != null) {
            return var4;
         }
      }

      return "application/octet-stream";
   }

   public Uri insert(Uri var1, ContentValues var2) {
      throw new UnsupportedOperationException("No external inserts");
   }

   public boolean onCreate() {
      return true;
   }

   public ParcelFileDescriptor openFile(Uri var1, String var2) throws FileNotFoundException {
      return ParcelFileDescriptor.open(this.mStrategy.getFileForUri(var1), modeToMode(var2));
   }

   public Cursor query(Uri var1, String[] var2, String var3, String[] var4, String var5) {
      File var13 = this.mStrategy.getFileForUri(var1);
      String[] var10 = var2;
      if (var2 == null) {
         var10 = COLUMNS;
      }

      String[] var15 = new String[var10.length];
      Object[] var11 = new Object[var10.length];
      int var6 = var10.length;
      int var7 = 0;

      int var8;
      int var9;
      for(var8 = 0; var7 < var6; var8 = var9) {
         label22: {
            String var14 = var10[var7];
            if ("_display_name".equals(var14)) {
               var15[var8] = "_display_name";
               var9 = var8 + 1;
               var11[var8] = var13.getName();
               var8 = var9;
            } else {
               var9 = var8;
               if (!"_size".equals(var14)) {
                  break label22;
               }

               var15[var8] = "_size";
               var9 = var8 + 1;
               var11[var8] = var13.length();
               var8 = var9;
            }

            var9 = var8;
         }

         ++var7;
      }

      var10 = copyOf(var15, var8);
      var11 = copyOf(var11, var8);
      MatrixCursor var12 = new MatrixCursor(var10, 1);
      var12.addRow(var11);
      return var12;
   }

   public int update(Uri var1, ContentValues var2, String var3, String[] var4) {
      throw new UnsupportedOperationException("No external updates");
   }

   interface PathStrategy {
      File getFileForUri(Uri var1);

      Uri getUriForFile(File var1);
   }

   static class SimplePathStrategy implements FileProvider.PathStrategy {
      private final String mAuthority;
      private final HashMap mRoots = new HashMap();

      SimplePathStrategy(String var1) {
         this.mAuthority = var1;
      }

      void addRoot(String var1, File var2) {
         if (!TextUtils.isEmpty(var1)) {
            File var5;
            try {
               var5 = var2.getCanonicalFile();
            } catch (IOException var4) {
               StringBuilder var3 = new StringBuilder();
               var3.append("Failed to resolve canonical path for ");
               var3.append(var2);
               throw new IllegalArgumentException(var3.toString(), var4);
            }

            this.mRoots.put(var1, var5);
         } else {
            throw new IllegalArgumentException("Name must not be empty");
         }
      }

      public File getFileForUri(Uri var1) {
         String var2 = var1.getEncodedPath();
         int var3 = var2.indexOf(47, 1);
         String var4 = Uri.decode(var2.substring(1, var3));
         var2 = Uri.decode(var2.substring(var3 + 1));
         File var8 = (File)this.mRoots.get(var4);
         StringBuilder var9;
         if (var8 != null) {
            File var6 = new File(var8, var2);

            File var7;
            try {
               var7 = var6.getCanonicalFile();
            } catch (IOException var5) {
               var9 = new StringBuilder();
               var9.append("Failed to resolve canonical path for ");
               var9.append(var6);
               throw new IllegalArgumentException(var9.toString());
            }

            if (var7.getPath().startsWith(var8.getPath())) {
               return var7;
            } else {
               throw new SecurityException("Resolved path jumped beyond configured root");
            }
         } else {
            var9 = new StringBuilder();
            var9.append("Unable to find configured root for ");
            var9.append(var1);
            throw new IllegalArgumentException(var9.toString());
         }
      }

      public Uri getUriForFile(File var1) {
         String var2;
         try {
            var2 = var1.getCanonicalPath();
         } catch (IOException var6) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Failed to resolve canonical path for ");
            var4.append(var1);
            throw new IllegalArgumentException(var4.toString());
         }

         Entry var7 = null;
         Iterator var3 = this.mRoots.entrySet().iterator();

         while(true) {
            String var5;
            Entry var11;
            do {
               do {
                  if (!var3.hasNext()) {
                     if (var7 != null) {
                        String var12 = ((File)var7.getValue()).getPath();
                        if (var12.endsWith("/")) {
                           var12 = var2.substring(var12.length());
                        } else {
                           var12 = var2.substring(var12.length() + 1);
                        }

                        StringBuilder var9 = new StringBuilder();
                        var9.append(Uri.encode((String)var7.getKey()));
                        var9.append('/');
                        var9.append(Uri.encode(var12, "/"));
                        String var10 = var9.toString();
                        return (new Builder()).scheme("content").authority(this.mAuthority).encodedPath(var10).build();
                     }

                     StringBuilder var8 = new StringBuilder();
                     var8.append("Failed to find configured root that contains ");
                     var8.append(var2);
                     throw new IllegalArgumentException(var8.toString());
                  }

                  var11 = (Entry)var3.next();
                  var5 = ((File)var11.getValue()).getPath();
               } while(!var2.startsWith(var5));
            } while(var7 != null && var5.length() <= ((File)var7.getValue()).getPath().length());

            var7 = var11;
         }
      }
   }
}