package org.telegram.messenger;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.service.chooser.ChooserTargetService;
import android.text.TextUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.LaunchActivity;

@TargetApi(23)
public class TgChooserTargetService extends ChooserTargetService {
   private RectF bitmapRect;
   private Paint roundPaint;

   private Icon createRoundBitmap(File param1) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$onGetChooserTargets$0$TgChooserTargetService(int var1, List var2, ComponentName var3, CountDownLatch var4) {
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();
      ArrayList var7 = new ArrayList();

      ArrayList var8;
      ArrayList var9;
      int var13;
      int var14;
      label155: {
         Exception var10000;
         label159: {
            SQLiteCursor var10;
            boolean var10001;
            try {
               var8 = new ArrayList();
               var8.add(UserConfig.getInstance(var1).getClientUserId());
               var9 = new ArrayList();
               var10 = MessagesStorage.getInstance(var1).getDatabase().queryFinalized(String.format(Locale.US, "SELECT did FROM dialogs ORDER BY date DESC LIMIT %d,%d", 0, 30));
            } catch (Exception var22) {
               var10000 = var22;
               var10001 = false;
               break label159;
            }

            while(true) {
               long var11;
               try {
                  if (!var10.next()) {
                     break;
                  }

                  var11 = var10.longValue(0);
               } catch (Exception var25) {
                  var10000 = var25;
                  var10001 = false;
                  break label159;
               }

               var13 = (int)var11;
               var14 = (int)(var11 >> 32);
               if (var13 != 0 && var14 != 1) {
                  if (var13 > 0) {
                     try {
                        if (!var8.contains(var13)) {
                           var8.add(var13);
                        }
                     } catch (Exception var24) {
                        var10000 = var24;
                        var10001 = false;
                        break label159;
                     }
                  } else {
                     var14 = -var13;

                     try {
                        if (!var9.contains(var14)) {
                           var9.add(var14);
                        }
                     } catch (Exception var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label159;
                     }
                  }

                  try {
                     var5.add(var13);
                     if (var5.size() != 8) {
                        continue;
                     }
                     break;
                  } catch (Exception var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label159;
                  }
               }
            }

            boolean var15;
            try {
               var10.dispose();
               var15 = var9.isEmpty();
            } catch (Exception var20) {
               var10000 = var20;
               var10001 = false;
               break label159;
            }

            if (!var15) {
               try {
                  MessagesStorage.getInstance(var1).getChatsInternal(TextUtils.join(",", var9), var6);
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label159;
               }
            }

            try {
               if (!var8.isEmpty()) {
                  MessagesStorage.getInstance(var1).getUsersInternal(TextUtils.join(",", var8), var7);
               }
               break label155;
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
            }
         }

         Exception var30 = var10000;
         FileLog.e((Throwable)var30);
      }

      SharedConfig.directShareHash = Utilities.random.nextLong();
      ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).edit().putLong("directShareHash", SharedConfig.directShareHash).commit();

      for(var1 = 0; var1 < var5.size(); ++var1) {
         Bundle var16 = new Bundle();
         var14 = (Integer)var5.get(var1);
         Icon var31 = null;
         var9 = null;
         TLRPC.ChatPhoto var17 = null;
         var8 = null;
         String var27;
         TLRPC.FileLocation var32;
         if (var14 > 0) {
            label164: {
               for(var13 = 0; var13 < var7.size(); ++var13) {
                  TLRPC.User var28 = (TLRPC.User)var7.get(var13);
                  if (var28.id == var14) {
                     if (!var28.bot) {
                        var16.putLong("dialogId", (long)var14);
                        var16.putLong("hash", SharedConfig.directShareHash);
                        TLRPC.UserProfilePhoto var33 = var28.photo;
                        var31 = var8;
                        if (var33 != null) {
                           var32 = var33.photo_small;
                           var31 = var8;
                           if (var32 != null) {
                              var31 = this.createRoundBitmap(FileLoader.getPathToAttach(var32, true));
                           }
                        }

                        var27 = ContactsController.formatName(var28.first_name, var28.last_name);
                        break label164;
                     }
                     break;
                  }
               }

               var27 = null;
            }
         } else {
            label170: {
               for(var13 = 0; var13 < var6.size(); ++var13) {
                  TLRPC.Chat var26 = (TLRPC.Chat)var6.get(var13);
                  if (var26.id == -var14) {
                     if (ChatObject.isNotInChat(var26) || ChatObject.isChannel(var26) && !var26.megagroup) {
                        break;
                     }

                     var16.putLong("dialogId", (long)var14);
                     var16.putLong("hash", SharedConfig.directShareHash);
                     var17 = var26.photo;
                     var31 = var9;
                     if (var17 != null) {
                        var32 = var17.photo_small;
                        var31 = var9;
                        if (var32 != null) {
                           var31 = this.createRoundBitmap(FileLoader.getPathToAttach(var32, true));
                        }
                     }

                     var27 = var26.title;
                     break label170;
                  }
               }

               var27 = null;
               var31 = var17;
            }
         }

         if (var27 != null) {
            Icon var29 = var31;
            if (var31 == null) {
               var29 = Icon.createWithResource(ApplicationLoader.applicationContext, 2131165549);
            }

            var2.add(new ChooserTarget(var27, var29, 1.0F, var3, var16));
         }
      }

      var4.countDown();
   }

   public List onGetChooserTargets(ComponentName var1, IntentFilter var2) {
      int var3 = UserConfig.selectedAccount;
      ArrayList var6 = new ArrayList();
      if (!UserConfig.getInstance(var3).isClientActivated()) {
         return var6;
      } else if (!MessagesController.getGlobalMainSettings().getBoolean("direct_share", true)) {
         return var6;
      } else {
         ImageLoader.getInstance();
         CountDownLatch var7 = new CountDownLatch(1);
         ComponentName var4 = new ComponentName(this.getPackageName(), LaunchActivity.class.getCanonicalName());
         MessagesStorage.getInstance(var3).getStorageQueue().postRunnable(new _$$Lambda$TgChooserTargetService$Tk7TkprF_pFKgMncgdcbNzQeTqs(this, var3, var6, var4, var7));

         try {
            var7.await();
         } catch (Exception var5) {
            FileLog.e((Throwable)var5);
         }

         return var6;
      }
   }
}
