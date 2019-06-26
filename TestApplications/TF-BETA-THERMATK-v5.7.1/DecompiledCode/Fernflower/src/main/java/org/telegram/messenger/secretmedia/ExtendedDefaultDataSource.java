package org.telegram.messenger.secretmedia;

import android.content.Context;
import android.net.Uri;
import com.google.android.exoplayer2.upstream.AssetDataSource;
import com.google.android.exoplayer2.upstream.ContentDataSource;
import com.google.android.exoplayer2.upstream.DataSchemeDataSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Predicate;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.telegram.messenger.FileStreamLoadOperation;

public final class ExtendedDefaultDataSource implements DataSource {
   private static final String SCHEME_ASSET = "asset";
   private static final String SCHEME_CONTENT = "content";
   private static final String SCHEME_RAW = "rawresource";
   private static final String SCHEME_RTMP = "rtmp";
   private static final String TAG = "ExtendedDefaultDataSource";
   private DataSource assetDataSource;
   private final DataSource baseDataSource;
   private DataSource contentDataSource;
   private final Context context;
   private DataSource dataSchemeDataSource;
   private DataSource dataSource;
   private DataSource encryptedFileDataSource;
   private DataSource fileDataSource;
   private DataSource rawResourceDataSource;
   private DataSource rtmpDataSource;
   private final List transferListeners;

   public ExtendedDefaultDataSource(Context var1, DataSource var2) {
      this.context = var1.getApplicationContext();
      Assertions.checkNotNull(var2);
      this.baseDataSource = (DataSource)var2;
      this.transferListeners = new ArrayList();
   }

   @Deprecated
   public ExtendedDefaultDataSource(Context var1, TransferListener var2, DataSource var3) {
      this(var1, var3);
      if (var2 != null) {
         this.transferListeners.add(var2);
      }

   }

   @Deprecated
   public ExtendedDefaultDataSource(Context var1, TransferListener var2, String var3, int var4, int var5, boolean var6) {
      this(var1, var2, new DefaultHttpDataSource(var3, (Predicate)null, var2, var4, var5, var6, (HttpDataSource.RequestProperties)null));
   }

   @Deprecated
   public ExtendedDefaultDataSource(Context var1, TransferListener var2, String var3, boolean var4) {
      this(var1, var2, var3, 8000, 8000, var4);
   }

   public ExtendedDefaultDataSource(Context var1, String var2, int var3, int var4, boolean var5) {
      this(var1, new DefaultHttpDataSource(var2, (Predicate)null, var3, var4, var5, (HttpDataSource.RequestProperties)null));
   }

   public ExtendedDefaultDataSource(Context var1, String var2, boolean var3) {
      this(var1, var2, 8000, 8000, var3);
   }

   private void addListenersToDataSource(DataSource var1) {
      for(int var2 = 0; var2 < this.transferListeners.size(); ++var2) {
         var1.addTransferListener((TransferListener)this.transferListeners.get(var2));
      }

   }

   private DataSource getAssetDataSource() {
      if (this.assetDataSource == null) {
         this.assetDataSource = new AssetDataSource(this.context);
         this.addListenersToDataSource(this.assetDataSource);
      }

      return this.assetDataSource;
   }

   private DataSource getContentDataSource() {
      if (this.contentDataSource == null) {
         this.contentDataSource = new ContentDataSource(this.context);
         this.addListenersToDataSource(this.contentDataSource);
      }

      return this.contentDataSource;
   }

   private DataSource getDataSchemeDataSource() {
      if (this.dataSchemeDataSource == null) {
         this.dataSchemeDataSource = new DataSchemeDataSource();
         this.addListenersToDataSource(this.dataSchemeDataSource);
      }

      return this.dataSchemeDataSource;
   }

   private DataSource getEncryptedFileDataSource() {
      if (this.encryptedFileDataSource == null) {
         this.encryptedFileDataSource = new EncryptedFileDataSource();
         this.addListenersToDataSource(this.encryptedFileDataSource);
      }

      return this.encryptedFileDataSource;
   }

   private DataSource getFileDataSource() {
      if (this.fileDataSource == null) {
         this.fileDataSource = new FileDataSource();
         this.addListenersToDataSource(this.fileDataSource);
      }

      return this.fileDataSource;
   }

   private DataSource getRawResourceDataSource() {
      if (this.rawResourceDataSource == null) {
         this.rawResourceDataSource = new RawResourceDataSource(this.context);
         this.addListenersToDataSource(this.rawResourceDataSource);
      }

      return this.rawResourceDataSource;
   }

   private DataSource getRtmpDataSource() {
      if (this.rtmpDataSource == null) {
         try {
            this.rtmpDataSource = (DataSource)Class.forName("com.google.android.exoplayer2.ext.rtmp.RtmpDataSource").getConstructor().newInstance();
            this.addListenersToDataSource(this.rtmpDataSource);
         } catch (ClassNotFoundException var2) {
            Log.w("ExtendedDefaultDataSource", "Attempting to play RTMP stream without depending on the RTMP extension");
         } catch (Exception var3) {
            throw new RuntimeException("Error instantiating RTMP extension", var3);
         }

         if (this.rtmpDataSource == null) {
            this.rtmpDataSource = this.baseDataSource;
         }
      }

      return this.rtmpDataSource;
   }

   private DataSource getStreamDataSource() {
      FileStreamLoadOperation var1 = new FileStreamLoadOperation();
      this.addListenersToDataSource(var1);
      return var1;
   }

   private void maybeAddListenerToDataSource(DataSource var1, TransferListener var2) {
      if (var1 != null) {
         var1.addTransferListener(var2);
      }

   }

   public void addTransferListener(TransferListener var1) {
      this.baseDataSource.addTransferListener(var1);
      this.transferListeners.add(var1);
      this.maybeAddListenerToDataSource(this.fileDataSource, var1);
      this.maybeAddListenerToDataSource(this.assetDataSource, var1);
      this.maybeAddListenerToDataSource(this.contentDataSource, var1);
      this.maybeAddListenerToDataSource(this.rtmpDataSource, var1);
      this.maybeAddListenerToDataSource(this.dataSchemeDataSource, var1);
      this.maybeAddListenerToDataSource(this.rawResourceDataSource, var1);
   }

   public void close() throws IOException {
      DataSource var1 = this.dataSource;
      if (var1 != null) {
         try {
            var1.close();
         } finally {
            this.dataSource = null;
         }
      }

   }

   public Map getResponseHeaders() {
      DataSource var1 = this.dataSource;
      Map var2;
      if (var1 == null) {
         var2 = Collections.emptyMap();
      } else {
         var2 = var1.getResponseHeaders();
      }

      return var2;
   }

   public Uri getUri() {
      DataSource var1 = this.dataSource;
      Uri var2;
      if (var1 == null) {
         var2 = null;
      } else {
         var2 = var1.getUri();
      }

      return var2;
   }

   public long open(DataSpec var1) throws IOException {
      boolean var2;
      if (this.dataSource == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Assertions.checkState(var2);
      String var3 = var1.uri.getScheme();
      if (Util.isLocalFileUri(var1.uri)) {
         var3 = var1.uri.getPath();
         if (var3 != null && var3.startsWith("/android_asset/")) {
            this.dataSource = this.getAssetDataSource();
         } else if (var1.uri.getPath().endsWith(".enc")) {
            this.dataSource = this.getEncryptedFileDataSource();
         } else {
            this.dataSource = this.getFileDataSource();
         }
      } else if ("tg".equals(var3)) {
         this.dataSource = this.getStreamDataSource();
      } else if ("asset".equals(var3)) {
         this.dataSource = this.getAssetDataSource();
      } else if ("content".equals(var3)) {
         this.dataSource = this.getContentDataSource();
      } else if ("rtmp".equals(var3)) {
         this.dataSource = this.getRtmpDataSource();
      } else if ("data".equals(var3)) {
         this.dataSource = this.getDataSchemeDataSource();
      } else if ("rawresource".equals(var3)) {
         this.dataSource = this.getRawResourceDataSource();
      } else {
         this.dataSource = this.baseDataSource;
      }

      return this.dataSource.open(var1);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      DataSource var4 = this.dataSource;
      Assertions.checkNotNull(var4);
      return ((DataSource)var4).read(var1, var2, var3);
   }
}
