package android.support.v4.content;

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
   private static final String ATTR_NAME = "name";
   private static final String ATTR_PATH = "path";
   private static final String[] COLUMNS = new String[]{"_display_name", "_size"};
   private static final File DEVICE_ROOT = new File("/");
   private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
   private static final String TAG_CACHE_PATH = "cache-path";
   private static final String TAG_EXTERNAL = "external-path";
   private static final String TAG_EXTERNAL_CACHE = "external-cache-path";
   private static final String TAG_EXTERNAL_FILES = "external-files-path";
   private static final String TAG_FILES_PATH = "files-path";
   private static final String TAG_ROOT_PATH = "root-path";
   private static HashMap sCache = new HashMap();
   private FileProvider.PathStrategy mStrategy;

   private static File buildPath(File var0, String... var1) {
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         if (var4 != null) {
            var0 = new File(var0, var4);
         }
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
               throw new IllegalArgumentException("Invalid mode: " + var0);
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
      XmlResourceParser var3 = var0.getPackageManager().resolveContentProvider(var1, 128).loadXmlMetaData(var0.getPackageManager(), "android.support.FILE_PROVIDER_PATHS");
      if (var3 == null) {
         throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
      } else {
         while(true) {
            int var4 = var3.next();
            if (var4 == 1) {
               return var2;
            }

            if (var4 == 2) {
               String var5 = var3.getName();
               String var6 = var3.getAttributeValue((String)null, "name");
               String var7 = var3.getAttributeValue((String)null, "path");
               Object var8 = null;
               File var9;
               if ("root-path".equals(var5)) {
                  var9 = DEVICE_ROOT;
               } else if ("files-path".equals(var5)) {
                  var9 = var0.getFilesDir();
               } else if ("cache-path".equals(var5)) {
                  var9 = var0.getCacheDir();
               } else if ("external-path".equals(var5)) {
                  var9 = Environment.getExternalStorageDirectory();
               } else {
                  File[] var10;
                  if ("external-files-path".equals(var5)) {
                     var10 = ContextCompat.getExternalFilesDirs(var0, (String)null);
                     var9 = (File)var8;
                     if (var10.length > 0) {
                        var9 = var10[0];
                     }
                  } else {
                     var9 = (File)var8;
                     if ("external-cache-path".equals(var5)) {
                        var10 = ContextCompat.getExternalCacheDirs(var0);
                        var9 = (File)var8;
                        if (var10.length > 0) {
                           var9 = var10[0];
                        }
                     }
                  }
               }

               if (var9 != null) {
                  var2.addRoot(var6, buildPath(var9, var7));
               }
            }
         }
      }
   }

   public void attachInfo(Context var1, ProviderInfo var2) {
      super.attachInfo(var1, var2);
      if (var2.exported) {
         throw new SecurityException("Provider must not be exported");
      } else if (!var2.grantUriPermissions) {
         throw new SecurityException("Provider must grant uri permissions");
      } else {
         this.mStrategy = getPathStrategy(var1, var2.authority);
      }
   }

   public int delete(Uri var1, String var2, String[] var3) {
      byte var4;
      if (this.mStrategy.getFileForUri(var1).delete()) {
         var4 = 1;
      } else {
         var4 = 0;
      }

      return var4;
   }

   public String getType(Uri var1) {
      File var3 = this.mStrategy.getFileForUri(var1);
      int var2 = var3.getName().lastIndexOf(46);
      String var4;
      if (var2 >= 0) {
         var4 = var3.getName().substring(var2 + 1);
         var4 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var4);
         if (var4 != null) {
            return var4;
         }
      }

      var4 = "application/octet-stream";
      return var4;
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
      for(var8 = 0; var7 < var6; ++var7) {
         String var14 = var10[var7];
         int var9;
         if ("_display_name".equals(var14)) {
            var15[var8] = "_display_name";
            var9 = var8 + 1;
            var11[var8] = var13.getName();
            var8 = var9;
         } else if ("_size".equals(var14)) {
            var15[var8] = "_size";
            var9 = var8 + 1;
            var11[var8] = var13.length();
            var8 = var9;
         }
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

      public SimplePathStrategy(String var1) {
         this.mAuthority = var1;
      }

      public void addRoot(String var1, File var2) {
         if (TextUtils.isEmpty(var1)) {
            throw new IllegalArgumentException("Name must not be empty");
         } else {
            File var3;
            try {
               var3 = var2.getCanonicalFile();
            } catch (IOException var4) {
               throw new IllegalArgumentException("Failed to resolve canonical path for " + var2, var4);
            }

            this.mRoots.put(var1, var3);
         }
      }

      public File getFileForUri(Uri var1) {
         String var2 = var1.getEncodedPath();
         int var3 = var2.indexOf(47, 1);
         String var4 = Uri.decode(var2.substring(1, var3));
         var2 = Uri.decode(var2.substring(var3 + 1));
         File var8 = (File)this.mRoots.get(var4);
         if (var8 == null) {
            throw new IllegalArgumentException("Unable to find configured root for " + var1);
         } else {
            File var6 = new File(var8, var2);

            File var7;
            try {
               var7 = var6.getCanonicalFile();
            } catch (IOException var5) {
               throw new IllegalArgumentException("Failed to resolve canonical path for " + var6);
            }

            if (!var7.getPath().startsWith(var8.getPath())) {
               throw new SecurityException("Resolved path jumped beyond configured root");
            } else {
               return var7;
            }
         }
      }

      public Uri getUriForFile(File var1) {
         String var2;
         try {
            var2 = var1.getCanonicalPath();
         } catch (IOException var6) {
            throw new IllegalArgumentException("Failed to resolve canonical path for " + var1);
         }

         Entry var7 = null;
         Iterator var3 = this.mRoots.entrySet().iterator();

         while(true) {
            Entry var4;
            String var5;
            do {
               do {
                  if (!var3.hasNext()) {
                     if (var7 == null) {
                        throw new IllegalArgumentException("Failed to find configured root that contains " + var2);
                     }

                     String var9 = ((File)var7.getValue()).getPath();
                     if (var9.endsWith("/")) {
                        var9 = var2.substring(var9.length());
                     } else {
                        var9 = var2.substring(var9.length() + 1);
                     }

                     String var8 = Uri.encode((String)var7.getKey()) + '/' + Uri.encode(var9, "/");
                     return (new Builder()).scheme("content").authority(this.mAuthority).encodedPath(var8).build();
                  }

                  var4 = (Entry)var3.next();
                  var5 = ((File)var4.getValue()).getPath();
               } while(!var2.startsWith(var5));
            } while(var7 != null && var5.length() <= ((File)var7.getValue()).getPath().length());

            var7 = var4;
         }
      }
   }
}
