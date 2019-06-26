package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import java.io.IOException;
import java.util.Map;

public interface DataSource {
   void addTransferListener(TransferListener var1);

   void close() throws IOException;

   Map getResponseHeaders();

   Uri getUri();

   long open(DataSpec var1) throws IOException;

   int read(byte[] var1, int var2, int var3) throws IOException;

   public interface Factory {
      DataSource createDataSource();
   }
}
