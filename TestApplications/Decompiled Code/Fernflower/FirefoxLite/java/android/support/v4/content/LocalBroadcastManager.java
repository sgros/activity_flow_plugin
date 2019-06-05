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
            if (var1.what != 1) {
               super.handleMessage(var1);
            } else {
               LocalBroadcastManager.this.executePendingBroadcasts();
            }

         }
      };
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

   void executePendingBroadcasts() {
      label287:
      while(true) {
         HashMap var1 = this.mReceivers;
         synchronized(var1){}

         Throwable var10000;
         boolean var10001;
         label284: {
            int var2;
            try {
               var2 = this.mPendingBroadcasts.size();
            } catch (Throwable var26) {
               var10000 = var26;
               var10001 = false;
               break label284;
            }

            if (var2 <= 0) {
               label277:
               try {
                  return;
               } catch (Throwable var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label277;
               }
            } else {
               label289: {
                  LocalBroadcastManager.BroadcastRecord[] var28;
                  try {
                     var28 = new LocalBroadcastManager.BroadcastRecord[var2];
                     this.mPendingBroadcasts.toArray(var28);
                     this.mPendingBroadcasts.clear();
                  } catch (Throwable var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label289;
                  }

                  var2 = 0;

                  while(true) {
                     if (var2 >= var28.length) {
                        continue label287;
                     }

                     LocalBroadcastManager.BroadcastRecord var4 = var28[var2];
                     int var5 = var4.receivers.size();

                     for(int var6 = 0; var6 < var5; ++var6) {
                        LocalBroadcastManager.ReceiverRecord var27 = (LocalBroadcastManager.ReceiverRecord)var4.receivers.get(var6);
                        if (!var27.dead) {
                           var27.receiver.onReceive(this.mAppContext, var4.intent);
                        }
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
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               continue;
            }
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
            var6.add(var4);
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
      label3359: {
         String var3;
         String var4;
         Uri var5;
         String var6;
         Set var7;
         boolean var8;
         label3354: {
            label3353: {
               try {
                  var3 = var1.getAction();
                  var4 = var1.resolveTypeIfNeeded(this.mAppContext.getContentResolver());
                  var5 = var1.getData();
                  var6 = var1.getScheme();
                  var7 = var1.getCategories();
                  if ((var1.getFlags() & 8) != 0) {
                     break label3353;
                  }
               } catch (Throwable var395) {
                  var10000 = var395;
                  var10001 = false;
                  break label3359;
               }

               var8 = false;
               break label3354;
            }

            var8 = true;
         }

         StringBuilder var9;
         if (var8) {
            try {
               var9 = new StringBuilder();
               var9.append("Resolving type ");
               var9.append(var4);
               var9.append(" scheme ");
               var9.append(var6);
               var9.append(" of intent ");
               var9.append(var1);
               Log.v("LocalBroadcastManager", var9.toString());
            } catch (Throwable var394) {
               var10000 = var394;
               var10001 = false;
               break label3359;
            }
         }

         ArrayList var10;
         try {
            var10 = (ArrayList)this.mActions.get(var1.getAction());
         } catch (Throwable var393) {
            var10000 = var393;
            var10001 = false;
            break label3359;
         }

         if (var10 != null) {
            if (var8) {
               try {
                  var9 = new StringBuilder();
                  var9.append("Action list: ");
                  var9.append(var10);
                  Log.v("LocalBroadcastManager", var9.toString());
               } catch (Throwable var390) {
                  var10000 = var390;
                  var10001 = false;
                  break label3359;
               }
            }

            Object var11 = null;
            int var12 = 0;

            while(true) {
               LocalBroadcastManager.ReceiverRecord var13;
               try {
                  if (var12 >= var10.size()) {
                     break;
                  }

                  var13 = (LocalBroadcastManager.ReceiverRecord)var10.get(var12);
               } catch (Throwable var391) {
                  var10000 = var391;
                  var10001 = false;
                  break label3359;
               }

               if (var8) {
                  try {
                     var9 = new StringBuilder();
                     var9.append("Matching against filter ");
                     var9.append(var13.filter);
                     Log.v("LocalBroadcastManager", var9.toString());
                  } catch (Throwable var389) {
                     var10000 = var389;
                     var10001 = false;
                     break label3359;
                  }
               }

               label3362: {
                  label3332: {
                     try {
                        if (!var13.broadcasting) {
                           break label3332;
                        }
                     } catch (Throwable var392) {
                        var10000 = var392;
                        var10001 = false;
                        break label3359;
                     }

                     if (var8) {
                        try {
                           Log.v("LocalBroadcastManager", "  Filter's target already added");
                        } catch (Throwable var388) {
                           var10000 = var388;
                           var10001 = false;
                           break label3359;
                        }
                     }
                     break label3362;
                  }

                  IntentFilter var14;
                  try {
                     var14 = var13.filter;
                  } catch (Throwable var387) {
                     var10000 = var387;
                     var10001 = false;
                     break label3359;
                  }

                  int var15;
                  try {
                     var15 = var14.match(var3, var4, var6, var5, var7, "LocalBroadcastManager");
                  } catch (Throwable var386) {
                     var10000 = var386;
                     var10001 = false;
                     break label3359;
                  }

                  if (var15 >= 0) {
                     if (var8) {
                        try {
                           var11 = new StringBuilder();
                           ((StringBuilder)var11).append("  Filter matched!  match=0x");
                           ((StringBuilder)var11).append(Integer.toHexString(var15));
                           Log.v("LocalBroadcastManager", ((StringBuilder)var11).toString());
                        } catch (Throwable var385) {
                           var10000 = var385;
                           var10001 = false;
                           break label3359;
                        }
                     }

                     if (var11 == null) {
                        try {
                           var11 = new ArrayList();
                        } catch (Throwable var384) {
                           var10000 = var384;
                           var10001 = false;
                           break label3359;
                        }
                     } else {
                        var11 = var11;
                     }

                     try {
                        ((ArrayList)var11).add(var13);
                        var13.broadcasting = true;
                     } catch (Throwable var383) {
                        var10000 = var383;
                        var10001 = false;
                        break label3359;
                     }
                  } else if (var8) {
                     String var399;
                     switch(var15) {
                     case -4:
                        var399 = "category";
                        break;
                     case -3:
                        var399 = "action";
                        break;
                     case -2:
                        var399 = "data";
                        break;
                     case -1:
                        var399 = "type";
                        break;
                     default:
                        var399 = "unknown reason";
                     }

                     try {
                        StringBuilder var401 = new StringBuilder();
                        var401.append("  Filter did not match: ");
                        var401.append(var399);
                        Log.v("LocalBroadcastManager", var401.toString());
                     } catch (Throwable var382) {
                        var10000 = var382;
                        var10001 = false;
                        break label3359;
                     }
                  }
               }

               ++var12;
            }

            if (var11 != null) {
               int var398 = 0;

               while(true) {
                  try {
                     if (var398 >= ((ArrayList)var11).size()) {
                        break;
                     }

                     ((LocalBroadcastManager.ReceiverRecord)((ArrayList)var11).get(var398)).broadcasting = false;
                  } catch (Throwable var380) {
                     var10000 = var380;
                     var10001 = false;
                     break label3359;
                  }

                  ++var398;
               }

               try {
                  ArrayList var400 = this.mPendingBroadcasts;
                  LocalBroadcastManager.BroadcastRecord var397 = new LocalBroadcastManager.BroadcastRecord(var1, (ArrayList)var11);
                  var400.add(var397);
                  if (!this.mHandler.hasMessages(1)) {
                     this.mHandler.sendEmptyMessage(1);
                  }
               } catch (Throwable var379) {
                  var10000 = var379;
                  var10001 = false;
                  break label3359;
               }

               try {
                  return true;
               } catch (Throwable var378) {
                  var10000 = var378;
                  var10001 = false;
                  break label3359;
               }
            }
         }

         label3295:
         try {
            return false;
         } catch (Throwable var381) {
            var10000 = var381;
            var10001 = false;
            break label3295;
         }
      }

      while(true) {
         Throwable var396 = var10000;

         try {
            throw var396;
         } catch (Throwable var377) {
            var10000 = var377;
            var10001 = false;
            continue;
         }
      }
   }

   public void unregisterReceiver(BroadcastReceiver var1) {
      HashMap var2 = this.mReceivers;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label986: {
         ArrayList var3;
         try {
            var3 = (ArrayList)this.mReceivers.remove(var1);
         } catch (Throwable var120) {
            var10000 = var120;
            var10001 = false;
            break label986;
         }

         if (var3 == null) {
            label949:
            try {
               return;
            } catch (Throwable var112) {
               var10000 = var112;
               var10001 = false;
               break label949;
            }
         } else {
            label982: {
               int var4;
               try {
                  var4 = var3.size() - 1;
               } catch (Throwable var117) {
                  var10000 = var117;
                  var10001 = false;
                  break label982;
               }

               label981:
               while(true) {
                  if (var4 < 0) {
                     try {
                        return;
                     } catch (Throwable var113) {
                        var10000 = var113;
                        var10001 = false;
                        break;
                     }
                  }

                  LocalBroadcastManager.ReceiverRecord var5;
                  try {
                     var5 = (LocalBroadcastManager.ReceiverRecord)var3.get(var4);
                     var5.dead = true;
                  } catch (Throwable var116) {
                     var10000 = var116;
                     var10001 = false;
                     break;
                  }

                  int var6 = 0;

                  while(true) {
                     String var7;
                     ArrayList var8;
                     try {
                        if (var6 >= var5.filter.countActions()) {
                           break;
                        }

                        var7 = var5.filter.getAction(var6);
                        var8 = (ArrayList)this.mActions.get(var7);
                     } catch (Throwable var118) {
                        var10000 = var118;
                        var10001 = false;
                        break label981;
                     }

                     if (var8 != null) {
                        int var9;
                        try {
                           var9 = var8.size() - 1;
                        } catch (Throwable var115) {
                           var10000 = var115;
                           var10001 = false;
                           break label981;
                        }

                        while(true) {
                           if (var9 < 0) {
                              try {
                                 if (var8.size() <= 0) {
                                    this.mActions.remove(var7);
                                 }
                                 break;
                              } catch (Throwable var114) {
                                 var10000 = var114;
                                 var10001 = false;
                                 break label981;
                              }
                           }

                           try {
                              LocalBroadcastManager.ReceiverRecord var10 = (LocalBroadcastManager.ReceiverRecord)var8.get(var9);
                              if (var10.receiver == var1) {
                                 var10.dead = true;
                                 var8.remove(var9);
                              }
                           } catch (Throwable var119) {
                              var10000 = var119;
                              var10001 = false;
                              break label981;
                           }

                           --var9;
                        }
                     }

                     ++var6;
                  }

                  --var4;
               }
            }
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

   private static final class BroadcastRecord {
      final Intent intent;
      final ArrayList receivers;

      BroadcastRecord(Intent var1, ArrayList var2) {
         this.intent = var1;
         this.receivers = var2;
      }
   }

   private static final class ReceiverRecord {
      boolean broadcasting;
      boolean dead;
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
         if (this.dead) {
            var1.append(" DEAD");
         }

         var1.append("}");
         return var1.toString();
      }
   }
}
