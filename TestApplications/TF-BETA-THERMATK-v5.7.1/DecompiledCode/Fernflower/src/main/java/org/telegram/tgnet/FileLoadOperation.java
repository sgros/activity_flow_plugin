package org.telegram.tgnet;

import java.io.File;

public class FileLoadOperation {
   private long address;
   private FileLoadOperationDelegate delegate;
   private boolean isForceRequest;
   private boolean started;

   public FileLoadOperation(int var1, long var2, long var4, long var6, int var8, byte[] var9, byte[] var10, String var11, int var12, int var13, File var14, File var15, FileLoadOperationDelegate var16) {
      this.address = native_createLoadOpetation(var1, var2, var4, var6, var8, var9, var10, var11, var12, var13, var14.getAbsolutePath(), var15.getAbsolutePath(), var16);
      this.delegate = var16;
   }

   public static native void native_cancelLoadOperation(long var0);

   public static native long native_createLoadOpetation(int var0, long var1, long var3, long var5, int var7, byte[] var8, byte[] var9, String var10, int var11, int var12, String var13, String var14, Object var15);

   public static native void native_startLoadOperation(long var0);

   public void cancel() {
      if (this.started) {
         long var1 = this.address;
         if (var1 != 0L) {
            native_cancelLoadOperation(var1);
         }
      }

   }

   public boolean isForceRequest() {
      return this.isForceRequest;
   }

   public void setForceRequest(boolean var1) {
      this.isForceRequest = var1;
   }

   public void start() {
      if (!this.started) {
         long var1 = this.address;
         if (var1 == 0L) {
            this.delegate.onFailed(0);
         } else {
            this.started = true;
            native_startLoadOperation(var1);
         }
      }
   }

   public boolean wasStarted() {
      return this.started;
   }
}
