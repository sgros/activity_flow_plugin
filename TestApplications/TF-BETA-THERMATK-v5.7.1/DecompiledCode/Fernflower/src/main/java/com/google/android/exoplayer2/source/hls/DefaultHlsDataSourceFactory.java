package com.google.android.exoplayer2.source.hls;

import com.google.android.exoplayer2.upstream.DataSource;

public final class DefaultHlsDataSourceFactory implements HlsDataSourceFactory {
   private final DataSource.Factory dataSourceFactory;

   public DefaultHlsDataSourceFactory(DataSource.Factory var1) {
      this.dataSourceFactory = var1;
   }

   public DataSource createDataSource(int var1) {
      return this.dataSourceFactory.createDataSource();
   }
}
