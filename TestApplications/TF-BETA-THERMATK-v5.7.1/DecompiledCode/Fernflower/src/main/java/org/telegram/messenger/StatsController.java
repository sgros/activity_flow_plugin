package org.telegram.messenger;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.File;
import java.io.RandomAccessFile;

public class StatsController {
   private static volatile StatsController[] Instance = new StatsController[3];
   private static final int TYPES_COUNT = 7;
   public static final int TYPE_AUDIOS = 3;
   public static final int TYPE_CALLS = 0;
   public static final int TYPE_FILES = 5;
   public static final int TYPE_MESSAGES = 1;
   public static final int TYPE_MOBILE = 0;
   public static final int TYPE_PHOTOS = 4;
   public static final int TYPE_ROAMING = 2;
   public static final int TYPE_TOTAL = 6;
   public static final int TYPE_VIDEOS = 2;
   public static final int TYPE_WIFI = 1;
   private static final ThreadLocal lastStatsSaveTime = new ThreadLocal() {
      protected Long initialValue() {
         return System.currentTimeMillis() - 1000L;
      }
   };
   private static DispatchQueue statsSaveQueue = new DispatchQueue("statsSaveQueue");
   private byte[] buffer = new byte[8];
   private int[] callsTotalTime = new int[3];
   private long lastInternalStatsSaveTime;
   private long[][] receivedBytes = new long[3][7];
   private int[][] receivedItems = new int[3][7];
   private long[] resetStatsDate = new long[3];
   private Runnable saveRunnable = new Runnable() {
      public void run() {
         long var1 = System.currentTimeMillis();
         if (Math.abs(var1 - StatsController.this.lastInternalStatsSaveTime) >= 2000L) {
            StatsController.this.lastInternalStatsSaveTime = var1;

            boolean var10001;
            try {
               StatsController.this.statsFile.seek(0L);
            } catch (Exception var9) {
               var10001 = false;
               return;
            }

            int var3 = 0;

            while(true) {
               if (var3 >= 3) {
                  try {
                     StatsController.this.statsFile.getFD().sync();
                  } catch (Exception var6) {
                     var10001 = false;
                  }
                  break;
               }

               for(int var4 = 0; var4 < 7; ++var4) {
                  try {
                     StatsController.this.statsFile.write(StatsController.this.longToBytes(StatsController.this.sentBytes[var3][var4]), 0, 8);
                     StatsController.this.statsFile.write(StatsController.this.longToBytes(StatsController.this.receivedBytes[var3][var4]), 0, 8);
                     StatsController.this.statsFile.write(StatsController.this.intToBytes(StatsController.this.sentItems[var3][var4]), 0, 4);
                     StatsController.this.statsFile.write(StatsController.this.intToBytes(StatsController.this.receivedItems[var3][var4]), 0, 4);
                  } catch (Exception var7) {
                     var10001 = false;
                     return;
                  }
               }

               try {
                  StatsController.this.statsFile.write(StatsController.this.intToBytes(StatsController.this.callsTotalTime[var3]), 0, 4);
                  StatsController.this.statsFile.write(StatsController.this.longToBytes(StatsController.this.resetStatsDate[var3]), 0, 8);
               } catch (Exception var8) {
                  var10001 = false;
                  break;
               }

               ++var3;
            }

         }
      }
   };
   private long[][] sentBytes = new long[3][7];
   private int[][] sentItems = new int[3][7];
   private RandomAccessFile statsFile;

   private StatsController(int var1) {
      File var2 = ApplicationLoader.getFilesDirFixed();
      if (var1 != 0) {
         File var3 = ApplicationLoader.getFilesDirFixed();
         StringBuilder var13 = new StringBuilder();
         var13.append("account");
         var13.append(var1);
         var13.append("/");
         var2 = new File(var3, var13.toString());
         var2.mkdirs();
      }

      boolean var22;
      label110: {
         label105: {
            boolean var10001;
            try {
               File var4 = new File(var2, "stats2.dat");
               RandomAccessFile var14 = new RandomAccessFile(var4, "rw");
               this.statsFile = var14;
               if (this.statsFile.length() <= 0L) {
                  break label105;
               }
            } catch (Exception var12) {
               var10001 = false;
               break label105;
            }

            int var5 = 0;
            boolean var6 = false;

            label95:
            while(true) {
               if (var5 >= 3) {
                  if (var6) {
                     try {
                        this.saveStats();
                     } catch (Exception var8) {
                        var10001 = false;
                        break;
                     }
                  }

                  var22 = false;
                  break label110;
               }

               for(int var7 = 0; var7 < 7; ++var7) {
                  try {
                     this.statsFile.readFully(this.buffer, 0, 8);
                     this.sentBytes[var5][var7] = this.bytesToLong(this.buffer);
                     this.statsFile.readFully(this.buffer, 0, 8);
                     this.receivedBytes[var5][var7] = this.bytesToLong(this.buffer);
                     this.statsFile.readFully(this.buffer, 0, 4);
                     this.sentItems[var5][var7] = this.bytesToInt(this.buffer);
                     this.statsFile.readFully(this.buffer, 0, 4);
                     this.receivedItems[var5][var7] = this.bytesToInt(this.buffer);
                  } catch (Exception var9) {
                     var10001 = false;
                     break label95;
                  }
               }

               try {
                  this.statsFile.readFully(this.buffer, 0, 4);
                  this.callsTotalTime[var5] = this.bytesToInt(this.buffer);
                  this.statsFile.readFully(this.buffer, 0, 8);
                  this.resetStatsDate[var5] = this.bytesToLong(this.buffer);
               } catch (Exception var10) {
                  var10001 = false;
                  break;
               }

               label92: {
                  try {
                     if (this.resetStatsDate[var5] != 0L) {
                        break label92;
                     }

                     this.resetStatsDate[var5] = System.currentTimeMillis();
                  } catch (Exception var11) {
                     var10001 = false;
                     break;
                  }

                  var6 = true;
               }

               ++var5;
            }
         }

         var22 = true;
      }

      if (var22) {
         SharedPreferences var15;
         StringBuilder var17;
         if (var1 == 0) {
            var15 = ApplicationLoader.applicationContext.getSharedPreferences("stats", 0);
         } else {
            Context var16 = ApplicationLoader.applicationContext;
            var17 = new StringBuilder();
            var17.append("stats");
            var17.append(var1);
            var15 = var16.getSharedPreferences(var17.toString(), 0);
         }

         var1 = 0;

         for(var22 = false; var1 < 3; ++var1) {
            int[] var18 = this.callsTotalTime;
            StringBuilder var19 = new StringBuilder();
            var19.append("callsTotalTime");
            var19.append(var1);
            var18[var1] = var15.getInt(var19.toString(), 0);
            long[] var20 = this.resetStatsDate;
            var17 = new StringBuilder();
            var17.append("resetStatsDate");
            var17.append(var1);
            var20[var1] = var15.getLong(var17.toString(), 0L);

            long[] var21;
            for(int var23 = 0; var23 < 7; ++var23) {
               var21 = this.sentBytes[var1];
               var19 = new StringBuilder();
               var19.append("sentBytes");
               var19.append(var1);
               var19.append("_");
               var19.append(var23);
               var21[var23] = var15.getLong(var19.toString(), 0L);
               var21 = this.receivedBytes[var1];
               var19 = new StringBuilder();
               var19.append("receivedBytes");
               var19.append(var1);
               var19.append("_");
               var19.append(var23);
               var21[var23] = var15.getLong(var19.toString(), 0L);
               var18 = this.sentItems[var1];
               var19 = new StringBuilder();
               var19.append("sentItems");
               var19.append(var1);
               var19.append("_");
               var19.append(var23);
               var18[var23] = var15.getInt(var19.toString(), 0);
               var18 = this.receivedItems[var1];
               var19 = new StringBuilder();
               var19.append("receivedItems");
               var19.append(var1);
               var19.append("_");
               var19.append(var23);
               var18[var23] = var15.getInt(var19.toString(), 0);
            }

            var21 = this.resetStatsDate;
            if (var21[var1] == 0L) {
               var21[var1] = System.currentTimeMillis();
               var22 = true;
            }
         }

         if (var22) {
            this.saveStats();
         }
      }

   }

   private int bytesToInt(byte[] var1) {
      byte var2 = var1[0];
      byte var3 = var1[1];
      byte var4 = var1[2];
      return var1[3] & 255 | var2 << 24 | (var3 & 255) << 16 | (var4 & 255) << 8;
   }

   private long bytesToLong(byte[] var1) {
      return ((long)var1[0] & 255L) << 56 | ((long)var1[1] & 255L) << 48 | ((long)var1[2] & 255L) << 40 | ((long)var1[3] & 255L) << 32 | ((long)var1[4] & 255L) << 24 | ((long)var1[5] & 255L) << 16 | ((long)var1[6] & 255L) << 8 | 255L & (long)var1[7];
   }

   public static StatsController getInstance(int var0) {
      StatsController var1 = Instance[var0];
      StatsController var2 = var1;
      if (var1 == null) {
         synchronized(StatsController.class){}

         Throwable var10000;
         boolean var10001;
         label216: {
            try {
               var1 = Instance[var0];
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label216;
            }

            var2 = var1;
            if (var1 == null) {
               StatsController[] var23;
               try {
                  var23 = Instance;
                  var2 = new StatsController(var0);
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label216;
               }

               var23[var0] = var2;
            }

            label202:
            try {
               return var2;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label202;
            }
         }

         while(true) {
            Throwable var24 = var10000;

            try {
               throw var24;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               continue;
            }
         }
      } else {
         return var2;
      }
   }

   private byte[] intToBytes(int var1) {
      byte[] var2 = this.buffer;
      var2[0] = (byte)((byte)(var1 >>> 24));
      var2[1] = (byte)((byte)(var1 >>> 16));
      var2[2] = (byte)((byte)(var1 >>> 8));
      var2[3] = (byte)((byte)var1);
      return var2;
   }

   private byte[] longToBytes(long var1) {
      byte[] var3 = this.buffer;
      var3[0] = (byte)((byte)((int)(var1 >>> 56)));
      var3[1] = (byte)((byte)((int)(var1 >>> 48)));
      var3[2] = (byte)((byte)((int)(var1 >>> 40)));
      var3[3] = (byte)((byte)((int)(var1 >>> 32)));
      var3[4] = (byte)((byte)((int)(var1 >>> 24)));
      var3[5] = (byte)((byte)((int)(var1 >>> 16)));
      var3[6] = (byte)((byte)((int)(var1 >>> 8)));
      var3[7] = (byte)((byte)((int)var1));
      return var3;
   }

   private void saveStats() {
      long var1 = System.currentTimeMillis();
      if (Math.abs(var1 - (Long)lastStatsSaveTime.get()) >= 2000L) {
         lastStatsSaveTime.set(var1);
         statsSaveQueue.postRunnable(this.saveRunnable);
      }

   }

   public int getCallsTotalTime(int var1) {
      return this.callsTotalTime[var1];
   }

   public long getReceivedBytesCount(int var1, int var2) {
      if (var2 == 1) {
         long[][] var3 = this.receivedBytes;
         return var3[var1][6] - var3[var1][5] - var3[var1][3] - var3[var1][2] - var3[var1][4];
      } else {
         return this.receivedBytes[var1][var2];
      }
   }

   public int getRecivedItemsCount(int var1, int var2) {
      return this.receivedItems[var1][var2];
   }

   public long getResetStatsDate(int var1) {
      return this.resetStatsDate[var1];
   }

   public long getSentBytesCount(int var1, int var2) {
      if (var2 == 1) {
         long[][] var3 = this.sentBytes;
         return var3[var1][6] - var3[var1][5] - var3[var1][3] - var3[var1][2] - var3[var1][4];
      } else {
         return this.sentBytes[var1][var2];
      }
   }

   public int getSentItemsCount(int var1, int var2) {
      return this.sentItems[var1][var2];
   }

   public void incrementReceivedBytesCount(int var1, int var2, long var3) {
      long[] var5 = this.receivedBytes[var1];
      var5[var2] += var3;
      this.saveStats();
   }

   public void incrementReceivedItemsCount(int var1, int var2, int var3) {
      int[] var4 = this.receivedItems[var1];
      var4[var2] += var3;
      this.saveStats();
   }

   public void incrementSentBytesCount(int var1, int var2, long var3) {
      long[] var5 = this.sentBytes[var1];
      var5[var2] += var3;
      this.saveStats();
   }

   public void incrementSentItemsCount(int var1, int var2, int var3) {
      int[] var4 = this.sentItems[var1];
      var4[var2] += var3;
      this.saveStats();
   }

   public void incrementTotalCallsTime(int var1, int var2) {
      int[] var3 = this.callsTotalTime;
      var3[var1] += var2;
      this.saveStats();
   }

   public void resetStats(int var1) {
      this.resetStatsDate[var1] = System.currentTimeMillis();

      for(int var2 = 0; var2 < 7; ++var2) {
         this.sentBytes[var1][var2] = 0L;
         this.receivedBytes[var1][var2] = 0L;
         this.sentItems[var1][var2] = 0;
         this.receivedItems[var1][var2] = 0;
      }

      this.callsTotalTime[var1] = 0;
      this.saveStats();
   }
}
