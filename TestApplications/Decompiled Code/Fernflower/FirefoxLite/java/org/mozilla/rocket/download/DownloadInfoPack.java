package org.mozilla.rocket.download;

import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;

public final class DownloadInfoPack {
   private long index;
   private ArrayList list;
   private int notifyType;

   public DownloadInfoPack(ArrayList var1, int var2, long var3) {
      Intrinsics.checkParameterIsNotNull(var1, "list");
      super();
      this.list = var1;
      this.notifyType = var2;
      this.index = var3;
   }

   public final long getIndex() {
      return this.index;
   }

   public final ArrayList getList() {
      return this.list;
   }

   public final int getNotifyType() {
      return this.notifyType;
   }

   public final void setIndex(long var1) {
      this.index = var1;
   }

   public final void setNotifyType(int var1) {
      this.notifyType = var1;
   }
}
