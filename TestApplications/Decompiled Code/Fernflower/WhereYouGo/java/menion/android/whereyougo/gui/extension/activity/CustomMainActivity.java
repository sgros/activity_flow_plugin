package menion.android.whereyougo.gui.extension.activity;

import android.app.ActivityManager;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Debug;
import android.os.StatFs;
import android.view.KeyEvent;
import java.io.File;
import java.io.StringReader;
import menion.android.whereyougo.MainApplication;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.Utils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public abstract class CustomMainActivity extends CustomActivity {
   public static final int CLOSE_DESTROY_APP_DIALOG_ADDITIONAL_TEXT = 2;
   public static final int CLOSE_DESTROY_APP_DIALOG_NO_TEXT = 1;
   public static final int CLOSE_DESTROY_APP_NO_DIALOG = 0;
   public static final int CLOSE_HIDE_APP = 3;
   private static final String[] DIRS;
   public static final int FINISH_EXIT = 0;
   public static final int FINISH_EXIT_FORCE = 1;
   public static final int FINISH_NONE = -1;
   public static final int FINISH_REINSTALL = 5;
   public static final int FINISH_RESTART = 2;
   public static final int FINISH_RESTART_FACTORY_RESET = 4;
   public static final int FINISH_RESTART_FORCE = 3;
   private static final String TAG = "CustomMain";
   private static boolean callRegisterOnly;
   private static boolean callSecondInit;
   private boolean finish = false;
   private int finishType = -1;

   static {
      DIRS = new String[]{FileSystem.CACHE};
   }

   private void clearPackageFromMemory() {
      (new Thread(new Runnable() {
         public void run() {
            try {
               ActivityManager var1 = (ActivityManager)CustomMainActivity.this.getApplicationContext().getSystemService("activity");
               Thread.sleep(1250L);
               var1.killBackgroundProcesses(CustomMainActivity.this.getPackageName());
            } catch (Exception var2) {
               Logger.e("CustomMain", "clearPackageFromMemory()", var2);
            }

         }
      })).start();
   }

   public static String getNewsFromTo(int var0, int var1) {
      String var2;
      String var3;
      String var4;
      label212: {
         var2 = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>";
         var3 = loadAssetString("news.xml");
         if (var3 != null) {
            var4 = var3;
            if (var3.length() != 0) {
               break label212;
            }
         }

         var4 = loadAssetString("news.xml");
      }

      var3 = var2;
      if (var4 != null) {
         var3 = var2;
         if (var4.length() > 0) {
            var3 = var2;

            Exception var10000;
            label216: {
               XmlPullParser var5;
               boolean var10001;
               try {
                  var5 = XmlPullParserFactory.newInstance().newPullParser();
               } catch (Exception var31) {
                  var10000 = var31;
                  var10001 = false;
                  break label216;
               }

               var3 = var2;

               StringReader var6;
               try {
                  var6 = new StringReader;
               } catch (Exception var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label216;
               }

               var3 = var2;

               try {
                  var6.<init>(var4);
               } catch (Exception var29) {
                  var10000 = var29;
                  var10001 = false;
                  break label216;
               }

               var3 = var2;

               try {
                  var5.setInput(var6);
               } catch (Exception var28) {
                  var10000 = var28;
                  var10001 = false;
                  break label216;
               }

               boolean var7 = false;

               while(true) {
                  var3 = var2;

                  int var8;
                  try {
                     var8 = var5.nextToken();
                  } catch (Exception var25) {
                     var10000 = var25;
                     var10001 = false;
                     break;
                  }

                  StringBuilder var33;
                  if (var8 == 2) {
                     var3 = var2;

                     try {
                        var4 = var5.getName();
                     } catch (Exception var19) {
                        var10000 = var19;
                        var10001 = false;
                        break;
                     }

                     var3 = var2;

                     label219: {
                        try {
                           if (!var4.equalsIgnoreCase("update")) {
                              break label219;
                           }
                        } catch (Exception var26) {
                           var10000 = var26;
                           var10001 = false;
                           break;
                        }

                        var3 = var2;

                        String var34;
                        try {
                           var34 = var5.getAttributeValue((String)null, "name");
                        } catch (Exception var14) {
                           var10000 = var14;
                           var10001 = false;
                           break;
                        }

                        var3 = var2;

                        int var35;
                        try {
                           var35 = Utils.parseInt(var5.getAttributeValue((String)null, "id"));
                        } catch (Exception var13) {
                           var10000 = var13;
                           var10001 = false;
                           break;
                        }

                        if (var35 > var0 && var35 <= var1) {
                           var7 = true;
                           var3 = var2;

                           try {
                              var33 = new StringBuilder;
                           } catch (Exception var12) {
                              var10000 = var12;
                              var10001 = false;
                              break;
                           }

                           var3 = var2;

                           try {
                              var33.<init>();
                           } catch (Exception var11) {
                              var10000 = var11;
                              var10001 = false;
                              break;
                           }

                           var3 = var2;

                           try {
                              var2 = var33.append(var2).append("<h4>").append(var34).append("</h4><ul>").toString();
                              continue;
                           } catch (Exception var10) {
                              var10000 = var10;
                              var10001 = false;
                              break;
                           }
                        }

                        var7 = false;
                        continue;
                     }

                     var3 = var2;

                     try {
                        if (!var4.equalsIgnoreCase("li")) {
                           continue;
                        }
                     } catch (Exception var18) {
                        var10000 = var18;
                        var10001 = false;
                        break;
                     }

                     if (var7) {
                        var3 = var2;

                        try {
                           var33 = new StringBuilder;
                        } catch (Exception var17) {
                           var10000 = var17;
                           var10001 = false;
                           break;
                        }

                        var3 = var2;

                        try {
                           var33.<init>();
                        } catch (Exception var16) {
                           var10000 = var16;
                           var10001 = false;
                           break;
                        }

                        var3 = var2;

                        try {
                           var2 = var33.append(var2).append("<li>").append(var5.nextText()).append("</li>").toString();
                        } catch (Exception var15) {
                           var10000 = var15;
                           var10001 = false;
                           break;
                        }
                     }
                  } else if (var8 == 3) {
                     var3 = var2;

                     try {
                        var4 = var5.getName();
                     } catch (Exception var24) {
                        var10000 = var24;
                        var10001 = false;
                        break;
                     }

                     var3 = var2;

                     label221: {
                        try {
                           if (!var4.equalsIgnoreCase("update")) {
                              break label221;
                           }
                        } catch (Exception var27) {
                           var10000 = var27;
                           var10001 = false;
                           break;
                        }

                        if (!var7) {
                           continue;
                        }

                        var7 = false;
                        var3 = var2;

                        try {
                           var33 = new StringBuilder;
                        } catch (Exception var22) {
                           var10000 = var22;
                           var10001 = false;
                           break;
                        }

                        var3 = var2;

                        try {
                           var33.<init>();
                        } catch (Exception var21) {
                           var10000 = var21;
                           var10001 = false;
                           break;
                        }

                        var3 = var2;

                        try {
                           var2 = var33.append(var2).append("</ul>").toString();
                           continue;
                        } catch (Exception var20) {
                           var10000 = var20;
                           var10001 = false;
                           break;
                        }
                     }

                     var3 = var2;

                     boolean var9;
                     try {
                        var9 = var4.equals("document");
                     } catch (Exception var23) {
                        var10000 = var23;
                        var10001 = false;
                        break;
                     }

                     if (var9) {
                        var3 = var2;
                        return var3 + "</body></html>";
                     }
                  }
               }
            }

            Exception var32 = var10000;
            Logger.e("CustomMain", "getNews()", var32);
         }
      }

      return var3 + "</body></html>";
   }

   public static String loadAssetString(String param0) {
      // $FF: Couldn't be decompiled
   }

   public boolean dispatchKeyEvent(KeyEvent var1) {
      boolean var2 = true;
      if (var1.getAction() == 0 && var1.getKeyCode() == 4) {
         switch(this.getCloseValue()) {
         case 0:
            this.finish = true;
            this.finish();
            return var2;
         case 1:
            this.showDialogFinish(0);
            return var2;
         case 2:
            this.showDialogFinish(0);
            return var2;
         }
      }

      var2 = super.dispatchKeyEvent(var1);
      return var2;
   }

   protected abstract void eventCreateLayout();

   protected abstract void eventDestroyApp();

   protected abstract void eventFirstInit();

   protected abstract void eventRegisterOnly();

   protected abstract void eventSecondInit();

   public void finishForceSilent() {
      this.finish = true;
      this.finish();
   }

   protected abstract String getCloseAdditionalText();

   protected abstract int getCloseValue();

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      A.registerMain(this);
      callSecondInit = false;
      callRegisterOnly = false;
      if (A.getApp() == null) {
         A.registerApp((MainApplication)this.getApplication());
         this.testFileSystem();
         if (!Utils.isPermissionAllowed("android.permission.ACCESS_FINE_LOCATION") || !Preferences.GPS && !Preferences.GPS_START_AUTOMATICALLY) {
            LocationState.setGpsOff(this);
         } else {
            LocationState.setGpsOn(this);
         }

         this.eventFirstInit();
         setScreenBasic(this);
         this.eventCreateLayout();
         callSecondInit = true;
      } else {
         setScreenBasic(this);
         this.eventCreateLayout();
         callRegisterOnly = true;
      }

   }

   public void onDestroy() {
      if (this.finish) {
         Debug.stopMethodTracing();
         boolean var1 = Utils.isPermissionAllowed("android.permission.KILL_BACKGROUND_PROCESSES");
         this.eventDestroyApp();
         PreferenceValues.disableWakeLock();
         PreferenceValues.setLastKnownLocation();
         LocationState.destroy(this);
         A.destroy();
         super.onDestroy();
         if (var1) {
            this.clearPackageFromMemory();
         }
      } else {
         super.onDestroy();
      }

   }

   public void onResumeExtra() {
      if (callSecondInit) {
         callSecondInit = false;
         this.eventSecondInit();
      }

      if (callRegisterOnly) {
         callRegisterOnly = false;
         this.eventRegisterOnly();
      }

   }

   public void showDialogFinish(int var1) {
      if (var1 != -1) {
         this.finishType = var1;
         this.runOnUiThread(new Runnable() {
            public void run() {
               boolean var1 = true;
               String var2 = Locale.getString(2131165302);
               String var3 = "";
               if (CustomMainActivity.this.finishType == 3 || CustomMainActivity.this.finishType == 4 || CustomMainActivity.this.finishType == 5 || CustomMainActivity.this.finishType == 1) {
                  var1 = false;
               }

               switch(CustomMainActivity.this.finishType) {
               case 0:
                  var3 = Locale.getString(2131165198);
                  break;
               case 1:
                  var2 = Locale.getString(2131165213);
                  var3 = Locale.getString(2131165318);
                  break;
               case 2:
                  var3 = Locale.getString(2131165320);
                  break;
               case 3:
                  var2 = Locale.getString(2131165213);
                  var3 = Locale.getString(2131165319);
                  break;
               case 4:
                  var2 = Locale.getString(2131165213);
                  var3 = Locale.getString(2131165319);
                  break;
               case 5:
                  var2 = Locale.getString(2131165213);
                  var3 = Locale.getString(2131165223);
               }

               Builder var4 = new Builder(CustomMainActivity.this);
               var4.setCancelable(var1);
               var4.setTitle(var2);
               var4.setIcon(2130837546);
               var4.setMessage(var3);
               var4.setPositiveButton(2131165230, new OnClickListener() {
                  public void onClick(DialogInterface var1, int var2) {
                     if (CustomMainActivity.this.finishType != 0 && CustomMainActivity.this.finishType != 1) {
                        if (CustomMainActivity.this.finishType != 2 && CustomMainActivity.this.finishType != 3 && CustomMainActivity.this.finishType != 4) {
                           if (CustomMainActivity.this.finishType == 5) {
                              CustomMainActivity.this.showDialogFinish(1);
                           }
                        } else {
                           CustomMainActivity.this.finish = true;
                           CustomMainActivity.this.finish();
                        }
                     } else {
                        CustomMainActivity.this.finish = true;
                        CustomMainActivity.this.finish();
                     }

                  }
               });
               if (var1) {
                  var4.setNegativeButton(2131165190, (OnClickListener)null);
               }

               var4.show();
            }
         });
      }

   }

   protected boolean testFileSystem() {
      boolean var1 = true;
      int var2 = 0;
      boolean var3 = var1;
      if (DIRS != null) {
         if (DIRS.length == 0) {
            var3 = var1;
         } else if (FileSystem.createRoot("WhereYouGo")) {
            String[] var4 = DIRS;
            int var5 = var4.length;

            while(true) {
               var3 = var1;
               if (var2 >= var5) {
                  break;
               }

               (new File(var4[var2])).mkdirs();
               ++var2;
            }
         } else {
            UtilsGUI.showDialogError(this, 2131165203, new OnClickListener() {
               public void onClick(DialogInterface var1, int var2) {
               }
            });
            var3 = false;
         }
      }

      return var3;
   }

   protected boolean testFreeSpace() {
      boolean var1 = false;
      if (DIRS != null && DIRS.length != 0) {
         long var3;
         int var5;
         try {
            StatFs var2 = new StatFs(FileSystem.ROOT);
            var3 = (long)var2.getBlockSize();
            var5 = var2.getAvailableBlocks();
         } catch (Exception var6) {
            return var1;
         }

         var3 = var3 * (long)var5 / 1048576L;
         if (var3 > 0L && var3 < 5L) {
            UtilsGUI.showDialogError(this, this.getString(2131165229, new Object[]{FileSystem.ROOT, var3 + "MB"}), new OnClickListener() {
               public void onClick(DialogInterface var1, int var2) {
               }
            });
         } else {
            var1 = true;
         }
      } else {
         var1 = true;
      }

      return var1;
   }
}
