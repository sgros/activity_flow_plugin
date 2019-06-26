package com.google.android.exoplayer2.upstream;

public interface TransferListener {
   void onBytesTransferred(DataSource var1, DataSpec var2, boolean var3, int var4);

   void onTransferEnd(DataSource var1, DataSpec var2, boolean var3);

   void onTransferInitializing(DataSource var1, DataSpec var2, boolean var3);

   void onTransferStart(DataSource var1, DataSpec var2, boolean var3);
}
