package org.telegram.messenger.secretmedia;

import android.content.Context;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;

public final class ExtendedDefaultDataSourceFactory implements DataSource.Factory {
   private final DataSource.Factory baseDataSourceFactory;
   private final Context context;
   private final TransferListener listener;

   public ExtendedDefaultDataSourceFactory(Context var1, TransferListener var2, DataSource.Factory var3) {
      this.context = var1.getApplicationContext();
      this.listener = var2;
      this.baseDataSourceFactory = var3;
   }

   public ExtendedDefaultDataSourceFactory(Context var1, String var2) {
      this(var1, (String)var2, (TransferListener)null);
   }

   public ExtendedDefaultDataSourceFactory(Context var1, String var2, TransferListener var3) {
      this(var1, (TransferListener)var3, (DataSource.Factory)(new DefaultHttpDataSourceFactory(var2, var3)));
   }

   public ExtendedDefaultDataSource createDataSource() {
      return new ExtendedDefaultDataSource(this.context, this.listener, this.baseDataSourceFactory.createDataSource());
   }
}
