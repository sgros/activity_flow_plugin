package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public final class LocalBroadcastManager {
   private static final boolean DEBUG = false;
   static final int MSG_EXEC_PENDING_BROADCASTS = 1;
   private static final String TAG = "LocalBroadcastManager";
   private static LocalBroadcastManager mInstance;
   private static final Object mLock = new Object();
   private final HashMap mActions = new HashMap();
   private final Context mAppContext;
   private final Handler mHandler;
   private final ArrayList mPendingBroadcasts = new ArrayList();
   private final HashMap mReceivers = new HashMap();

   private LocalBroadcastManager(Context var1) {
      this.mAppContext = var1;
      this.mHandler = new Handler(var1.getMainLooper()) {
         public void handleMessage(Message var1) {
            switch(var1.what) {
            case 1:
               LocalBroadcastManager.this.executePendingBroadcasts();
               break;
            default:
               super.handleMessage(var1);
            }

         }
      };
   }

   private void executePendingBroadcasts() {
      label263:
      while(true) {
         HashMap var1 = this.mReceivers;
         synchronized(var1){}

         Throwable var10000;
         boolean var10001;
         label260: {
            int var2;
            try {
               var2 = this.mPendingBroadcasts.size();
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label260;
            }

            if (var2 <= 0) {
               label253:
               try {
                  return;
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label253;
               }
            } else {
               label265: {
                  LocalBroadcastManager.BroadcastRecord[] var26;
                  try {
                     var26 = new LocalBroadcastManager.BroadcastRecord[var2];
                     this.mPendingBroadcasts.toArray(var26);
                     this.mPendingBroadcasts.clear();
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label265;
                  }

                  var2 = 0;

                  while(true) {
                     if (var2 >= var26.length) {
                        continue label263;
                     }

                     LocalBroadcastManager.BroadcastRecord var25 = var26[var2];

                     for(int var4 = 0; var4 < var25.receivers.size(); ++var4) {
                        ((LocalBroadcastManager.ReceiverRecord)var25.receivers.get(var4)).receiver.onReceive(this.mAppContext, var25.intent);
                     }

                     ++var2;
                  }
               }
            }
         }

         while(true) {
            Throwable var3 = var10000;

            try {
               throw var3;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public static LocalBroadcastManager getInstance(Context var0) {
      Object var1 = mLock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (mInstance == null) {
               LocalBroadcastManager var2 = new LocalBroadcastManager(var0.getApplicationContext());
               mInstance = var2;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            LocalBroadcastManager var16 = mInstance;
            return var16;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public void registerReceiver(BroadcastReceiver var1, IntentFilter var2) {
      HashMap var3 = this.mReceivers;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label585: {
         LocalBroadcastManager.ReceiverRecord var4;
         ArrayList var5;
         try {
            var4 = new LocalBroadcastManager.ReceiverRecord(var2, var1);
            var5 = (ArrayList)this.mReceivers.get(var1);
         } catch (Throwable var79) {
            var10000 = var79;
            var10001 = false;
            break label585;
         }

         ArrayList var6 = var5;
         if (var5 == null) {
            try {
               var6 = new ArrayList(1);
               this.mReceivers.put(var1, var6);
            } catch (Throwable var78) {
               var10000 = var78;
               var10001 = false;
               break label585;
            }
         }

         try {
            var6.add(var2);
         } catch (Throwable var77) {
            var10000 = var77;
            var10001 = false;
            break label585;
         }

         int var7 = 0;

         while(true) {
            String var82;
            try {
               if (var7 >= var2.countActions()) {
                  break;
               }

               var82 = var2.getAction(var7);
               var6 = (ArrayList)this.mActions.get(var82);
            } catch (Throwable var76) {
               var10000 = var76;
               var10001 = false;
               break label585;
            }

            ArrayList var80 = var6;
            if (var6 == null) {
               try {
                  var80 = new ArrayList(1);
                  this.mActions.put(var82, var80);
               } catch (Throwable var75) {
                  var10000 = var75;
                  var10001 = false;
                  break label585;
               }
            }

            try {
               var80.add(var4);
            } catch (Throwable var74) {
               var10000 = var74;
               var10001 = false;
               break label585;
            }

            ++var7;
         }

         label557:
         try {
            return;
         } catch (Throwable var73) {
            var10000 = var73;
            var10001 = false;
            break label557;
         }
      }

      while(true) {
         Throwable var81 = var10000;

         try {
            throw var81;
         } catch (Throwable var72) {
            var10000 = var72;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean sendBroadcast(Intent var1) {
      HashMap var2 = this.mReceivers;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label3260: {
         String var3;
         String var4;
         Uri var5;
         String var6;
         Set var7;
         boolean var8;
         label3237: {
            label3236: {
               try {
                  var3 = var1.getAction();
                  var4 = var1.resolveTypeIfNeeded(this.mAppContext.getContentResolver());
                  var5 = var1.getData();
                  var6 = var1.getScheme();
                  var7 = var1.getCategories();
                  if ((var1.getFlags() & 8) != 0) {
                     break label3236;
                  }
               } catch (Throwable var357) {
                  var10000 = var357;
                  var10001 = false;
                  break label3260;
               }

               var8 = false;
               break label3237;
            }

            var8 = true;
         }

         StringBuilder var9;
         if (var8) {
            try {
               var9 = new StringBuilder();
               Log.v("LocalBroadcastManager", var9.append("Resolving type ").append(var4).append(" scheme ").append(var6).append(" of intent ").append(var1).toString());
            } catch (Throwable var356) {
               var10000 = var356;
               var10001 = false;
               break label3260;
            }
         }

         ArrayList var10;
         try {
            var10 = (ArrayList)this.mActions.get(var1.getAction());
         } catch (Throwable var355) {
            var10000 = var355;
            var10001 = false;
            break label3260;
         }

         boolean var15;
         if (var10 != null) {
            if (var8) {
               try {
                  var9 = new StringBuilder();
                  Log.v("LocalBroadcastManager", var9.append("Action list: ").append(var10).toString());
               } catch (Throwable var352) {
                  var10000 = var352;
                  var10001 = false;
                  break label3260;
               }
            }

            ArrayList var11 = null;
            int var12 = 0;

            while(true) {
               LocalBroadcastManager.ReceiverRecord var13;
               try {
                  if (var12 >= var10.size()) {
                     break;
                  }

                  var13 = (LocalBroadcastManager.ReceiverRecord)var10.get(var12);
               } catch (Throwable var353) {
                  var10000 = var353;
                  var10001 = false;
                  break label3260;
               }

               if (var8) {
                  try {
                     var9 = new StringBuilder();
                     Log.v("LocalBroadcastManager", var9.append("Matching against filter ").append(var13.filter).toString());
                  } catch (Throwable var351) {
                     var10000 = var351;
                     var10001 = false;
                     break label3260;
                  }
               }

               ArrayList var361;
               label3247: {
                  label3215: {
                     try {
                        if (!var13.broadcasting) {
                           break label3215;
                        }
                     } catch (Throwable var354) {
                        var10000 = var354;
                        var10001 = false;
                        break label3260;
                     }

                     var361 = var11;
                     if (var8) {
                        try {
                           Log.v("LocalBroadcastManager", "  Filter's target already added");
                        } catch (Throwable var350) {
                           var10000 = var350;
                           var10001 = false;
                           break label3260;
                        }

                        var361 = var11;
                     }
                     break label3247;
                  }

                  int var14;
                  try {
                     var14 = var13.filter.match(var3, var4, var6, var5, var7, "LocalBroadcastManager");
                  } catch (Throwable var349) {
                     var10000 = var349;
                     var10001 = false;
                     break label3260;
                  }

                  if (var14 >= 0) {
                     if (var8) {
                        try {
                           var9 = new StringBuilder();
                           Log.v("LocalBroadcastManager", var9.append("  Filter matched!  match=0x").append(Integer.toHexString(var14)).toString());
                        } catch (Throwable var348) {
                           var10000 = var348;
                           var10001 = false;
                           break label3260;
                        }
                     }

                     var361 = var11;
                     if (var11 == null) {
                        try {
                           var361 = new ArrayList();
                        } catch (Throwable var347) {
                           var10000 = var347;
                           var10001 = false;
                           break label3260;
                        }
                     }

                     try {
                        var361.add(var13);
                        var13.broadcasting = true;
                     } catch (Throwable var346) {
                        var10000 = var346;
                        var10001 = false;
                        break label3260;
                     }
                  } else {
                     var361 = var11;
                     if (var8) {
                        String var362;
                        switch(var14) {
                        case -4:
                           var362 = "category";
                           break;
                        case -3:
                           var362 = "action";
                           break;
                        case -2:
                           var362 = "data";
                           break;
                        case -1:
                           var362 = "type";
                           break;
                        default:
                           var362 = "unknown reason";
                        }

                        try {
                           StringBuilder var363 = new StringBuilder();
                           Log.v("LocalBroadcastManager", var363.append("  Filter did not match: ").append(var362).toString());
                        } catch (Throwable var345) {
                           var10000 = var345;
                           var10001 = false;
                           break label3260;
                        }

                        var361 = var11;
                     }
                  }
               }

               ++var12;
               var11 = var361;
            }

            if (var11 != null) {
               int var360 = 0;

               while(true) {
                  try {
                     if (var360 >= var11.size()) {
                        break;
                     }

                     ((LocalBroadcastManager.ReceiverRecord)var11.get(var360)).broadcasting = false;
                  } catch (Throwable var343) {
                     var10000 = var343;
                     var10001 = false;
                     break label3260;
                  }

                  ++var360;
               }

               try {
                  ArrayList var359 = this.mPendingBroadcasts;
                  LocalBroadcastManager.BroadcastRecord var364 = new LocalBroadcastManager.BroadcastRecord(var1, var11);
                  var359.add(var364);
                  if (!this.mHandler.hasMessages(1)) {
                     this.mHandler.sendEmptyMessage(1);
                  }
               } catch (Throwable var342) {
                  var10000 = var342;
                  var10001 = false;
                  break label3260;
               }

               var15 = true;

               try {
                  return var15;
               } catch (Throwable var341) {
                  var10000 = var341;
                  var10001 = false;
                  break label3260;
               }
            }
         }

         try {
            ;
         } catch (Throwable var344) {
            var10000 = var344;
            var10001 = false;
            break label3260;
         }

         var15 = false;
         return var15;
      }

      while(true) {
         Throwable var358 = var10000;

         try {
            throw var358;
         } catch (Throwable var340) {
            var10000 = var340;
            var10001 = false;
            continue;
         }
      }
   }

   public void sendBroadcastSync(Intent var1) {
      if (this.sendBroadcast(var1)) {
         this.executePendingBroadcasts();
      }

   }

   public void unregisterReceiver(BroadcastReceiver var1) {
      HashMap var2 = this.mReceivers;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label1107: {
         ArrayList var3;
         try {
            var3 = (ArrayList)this.mReceivers.remove(var1);
         } catch (Throwable var120) {
            var10000 = var120;
            var10001 = false;
            break label1107;
         }

         if (var3 == null) {
            try {
               ;
            } catch (Throwable var115) {
               var10000 = var115;
               var10001 = false;
               break label1107;
            }
         } else {
            int var4 = 0;

            while(true) {
               IntentFilter var5;
               try {
                  if (var4 >= var3.size()) {
                     break;
                  }

                  var5 = (IntentFilter)var3.get(var4);
               } catch (Throwable var116) {
                  var10000 = var116;
                  var10001 = false;
                  break label1107;
               }

               int var6 = 0;

               while(true) {
                  String var7;
                  ArrayList var8;
                  try {
                     if (var6 >= var5.countActions()) {
                        break;
                     }

                     var7 = var5.getAction(var6);
                     var8 = (ArrayList)this.mActions.get(var7);
                  } catch (Throwable var117) {
                     var10000 = var117;
                     var10001 = false;
                     break label1107;
                  }

                  if (var8 != null) {
                     int var9 = 0;

                     while(true) {
                        try {
                           if (var9 >= var8.size()) {
                              break;
                           }
                        } catch (Throwable var118) {
                           var10000 = var118;
                           var10001 = false;
                           break label1107;
                        }

                        int var10 = var9;

                        label1086: {
                           try {
                              if (((LocalBroadcastManager.ReceiverRecord)var8.get(var9)).receiver != var1) {
                                 break label1086;
                              }

                              var8.remove(var9);
                           } catch (Throwable var119) {
                              var10000 = var119;
                              var10001 = false;
                              break label1107;
                           }

                           var10 = var9 - 1;
                        }

                        var9 = var10 + 1;
                     }

                     try {
                        if (var8.size() <= 0) {
                           this.mActions.remove(var7);
                        }
                     } catch (Throwable var114) {
                        var10000 = var114;
                        var10001 = false;
                        break label1107;
                     }
                  }

                  ++var6;
               }

               ++var4;
            }

            try {
               ;
            } catch (Throwable var113) {
               var10000 = var113;
               var10001 = false;
               break label1107;
            }
         }

         label1060:
         try {
            return;
         } catch (Throwable var112) {
            var10000 = var112;
            var10001 = false;
            break label1060;
         }
      }

      while(true) {
         Throwable var121 = var10000;

         try {
            throw var121;
         } catch (Throwable var111) {
            var10000 = var111;
            var10001 = false;
            continue;
         }
      }
   }

   private static class BroadcastRecord {
      final Intent intent;
      final ArrayList receivers;

      BroadcastRecord(Intent var1, ArrayList var2) {
         this.intent = var1;
         this.receivers = var2;
      }
   }

   private static class ReceiverRecord {
      boolean broadcasting;
      final IntentFilter filter;
      final BroadcastReceiver receiver;

      ReceiverRecord(IntentFilter var1, BroadcastReceiver var2) {
         this.filter = var1;
         this.receiver = var2;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(128);
         var1.append("Receiver{");
         var1.append(this.receiver);
         var1.append(" filter=");
         var1.append(this.filter);
         var1.append("}");
         return var1.toString();
      }
   }
}
