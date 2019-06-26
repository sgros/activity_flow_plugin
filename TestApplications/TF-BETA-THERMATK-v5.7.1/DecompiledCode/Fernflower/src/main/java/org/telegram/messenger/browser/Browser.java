package org.telegram.messenger.browser;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import java.lang.ref.WeakReference;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.CustomTabsCopyReceiver;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.ShareBroadcastReceiver;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.support.customtabs.CustomTabsCallback;
import org.telegram.messenger.support.customtabs.CustomTabsClient;
import org.telegram.messenger.support.customtabs.CustomTabsIntent;
import org.telegram.messenger.support.customtabs.CustomTabsServiceConnection;
import org.telegram.messenger.support.customtabs.CustomTabsSession;
import org.telegram.messenger.support.customtabsclient.shared.CustomTabsHelper;
import org.telegram.messenger.support.customtabsclient.shared.ServiceConnection;
import org.telegram.messenger.support.customtabsclient.shared.ServiceConnectionCallback;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;

public class Browser {
   private static WeakReference currentCustomTabsActivity;
   private static CustomTabsClient customTabsClient;
   private static WeakReference customTabsCurrentSession;
   private static String customTabsPackageToBind;
   private static CustomTabsServiceConnection customTabsServiceConnection;
   private static CustomTabsSession customTabsSession;

   public static void bindCustomTabsService(Activity var0) {
      WeakReference var1 = currentCustomTabsActivity;
      Activity var4;
      if (var1 == null) {
         var4 = null;
      } else {
         var4 = (Activity)var1.get();
      }

      if (var4 != null && var4 != var0) {
         unbindCustomTabsService(var4);
      }

      if (customTabsClient == null) {
         currentCustomTabsActivity = new WeakReference(var0);

         try {
            if (TextUtils.isEmpty(customTabsPackageToBind)) {
               customTabsPackageToBind = CustomTabsHelper.getPackageNameToUse(var0);
               if (customTabsPackageToBind == null) {
                  return;
               }
            }

            ServiceConnectionCallback var2 = new ServiceConnectionCallback() {
               public void onServiceConnected(CustomTabsClient var1) {
                  Browser.customTabsClient = var1;
                  if (SharedConfig.customTabs && Browser.customTabsClient != null) {
                     try {
                        Browser.customTabsClient.warmup(0L);
                     } catch (Exception var2) {
                        FileLog.e((Throwable)var2);
                     }
                  }

               }

               public void onServiceDisconnected() {
                  Browser.customTabsClient = null;
               }
            };
            ServiceConnection var5 = new ServiceConnection(var2);
            customTabsServiceConnection = var5;
            if (!CustomTabsClient.bindCustomTabsService(var0, customTabsPackageToBind, customTabsServiceConnection)) {
               customTabsServiceConnection = null;
            }
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }

      }
   }

   private static CustomTabsSession getCurrentSession() {
      WeakReference var0 = customTabsCurrentSession;
      CustomTabsSession var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = (CustomTabsSession)var0.get();
      }

      return var1;
   }

   private static CustomTabsSession getSession() {
      CustomTabsClient var0 = customTabsClient;
      if (var0 == null) {
         customTabsSession = null;
      } else if (customTabsSession == null) {
         customTabsSession = var0.newSession(new Browser.NavigationCallback());
         setCurrentSession(customTabsSession);
      }

      return customTabsSession;
   }

   public static boolean isInternalUri(Uri var0, boolean[] var1) {
      String var2 = var0.getHost();
      if (var2 != null) {
         var2 = var2.toLowerCase();
      } else {
         var2 = "";
      }

      if ("tg".equals(var0.getScheme())) {
         return true;
      } else {
         String var3;
         if ("telegram.dog".equals(var2)) {
            var3 = var0.getPath();
            if (var3 != null && var3.length() > 1) {
               var3 = var3.substring(1).toLowerCase();
               if (!var3.startsWith("blog") && !var3.equals("iv") && !var3.startsWith("faq") && !var3.equals("apps") && !var3.startsWith("s/")) {
                  return true;
               }

               if (var1 != null) {
                  var1[0] = true;
               }

               return false;
            }
         } else if ("telegram.me".equals(var2) || "t.me".equals(var2)) {
            var3 = var0.getPath();
            if (var3 != null && var3.length() > 1) {
               var3 = var3.substring(1).toLowerCase();
               if (!var3.equals("iv") && !var3.startsWith("s/")) {
                  return true;
               }

               if (var1 != null) {
                  var1[0] = true;
               }
            }
         }

         return false;
      }
   }

   public static boolean isInternalUrl(String var0, boolean[] var1) {
      return isInternalUri(Uri.parse(var0), var1);
   }

   public static boolean isPassportUrl(String var0) {
      if (var0 == null) {
         return false;
      } else {
         boolean var1;
         try {
            var0 = var0.toLowerCase();
            if (var0.startsWith("tg:passport") || var0.startsWith("tg://passport") || var0.startsWith("tg:secureid")) {
               return true;
            }

            if (!var0.contains("resolve")) {
               return false;
            }

            var1 = var0.contains("domain=telegrampassport");
         } catch (Throwable var2) {
            return false;
         }

         if (var1) {
            return true;
         } else {
            return false;
         }
      }
   }

   // $FF: synthetic method
   static void lambda$null$0(AlertDialog[] var0, TLObject var1, int var2, Uri var3, Context var4, boolean var5) {
      try {
         var0[0].dismiss();
      } catch (Throwable var9) {
      }

      boolean var12;
      label23: {
         var0[0] = null;
         boolean var6 = var1 instanceof TLRPC.TL_messageMediaWebPage;
         boolean var7 = true;
         if (var6) {
            TLRPC.TL_messageMediaWebPage var11 = (TLRPC.TL_messageMediaWebPage)var1;
            TLRPC.WebPage var10 = var11.webpage;
            if (var10 instanceof TLRPC.TL_webPage && var10.cached_page != null) {
               NotificationCenter.getInstance(var2).postNotificationName(NotificationCenter.openArticle, var11.webpage, var3.toString());
               var12 = var7;
               break label23;
            }
         }

         var12 = false;
      }

      if (!var12) {
         openUrl(var4, var3, var5, false);
      }

   }

   // $FF: synthetic method
   static void lambda$null$2(int var0, DialogInterface var1) {
      ConnectionsManager.getInstance(UserConfig.selectedAccount).cancelRequest(var0, true);
   }

   // $FF: synthetic method
   static void lambda$openUrl$1(AlertDialog[] var0, int var1, Uri var2, Context var3, boolean var4, TLObject var5, TLRPC.TL_error var6) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$Browser$37_x46nyg5sVKyQShvHeLalxsGQ(var0, var5, var1, var2, var3, var4));
   }

   // $FF: synthetic method
   static void lambda$openUrl$3(AlertDialog[] var0, int var1) {
      if (var0[0] != null) {
         AlertDialog var2 = var0[0];

         try {
            _$$Lambda$Browser$gkq8pxwFDBSthMGmf2aFQ3i1KTE var3 = new _$$Lambda$Browser$gkq8pxwFDBSthMGmf2aFQ3i1KTE(var1);
            var2.setOnCancelListener(var3);
            var0[0].show();
         } catch (Exception var4) {
         }

      }
   }

   public static void openUrl(Context var0, Uri var1) {
      openUrl(var0, var1, true);
   }

   public static void openUrl(Context var0, Uri var1, boolean var2) {
      openUrl(var0, var1, var2, true);
   }

   public static void openUrl(Context var0, Uri var1, boolean var2, boolean var3) {
      if (var0 != null && var1 != null) {
         int var5 = UserConfig.selectedAccount;
         boolean[] var6 = new boolean[]{false};
         boolean var7 = isInternalUri(var1, var6);
         boolean var10001;
         if (var3) {
            label528: {
               try {
                  if (!var1.getHost().toLowerCase().equals("telegra.ph") && !var1.toString().toLowerCase().contains("telegram.org/faq")) {
                     break label528;
                  }
               } catch (Exception var67) {
                  var10001 = false;
                  break label528;
               }

               AlertDialog[] var8;
               AlertDialog var9;
               try {
                  var8 = new AlertDialog[1];
                  var9 = new AlertDialog(var0, 3);
               } catch (Exception var66) {
                  var10001 = false;
                  break label528;
               }

               var8[0] = var9;

               try {
                  TLRPC.TL_messages_getWebPagePreview var10 = new TLRPC.TL_messages_getWebPagePreview();
                  var10.message = var1.toString();
                  ConnectionsManager var11 = ConnectionsManager.getInstance(UserConfig.selectedAccount);
                  _$$Lambda$Browser$UGNHp8wusapZ_BRGXGaD_Ayz4_I var77 = new _$$Lambda$Browser$UGNHp8wusapZ_BRGXGaD_Ayz4_I(var8, var5, var1, var0, var2);
                  var5 = var11.sendRequest(var10, var77);
                  _$$Lambda$Browser$tzkgx_C1l2oH_szh01yqocI6uLg var79 = new _$$Lambda$Browser$tzkgx_C1l2oH_szh01yqocI6uLg(var8, var5);
                  AndroidUtilities.runOnUIThread(var79, 1000L);
                  return;
               } catch (Exception var65) {
                  var10001 = false;
               }
            }
         }

         Uri var80 = var1;

         Exception var10000;
         label502: {
            Exception var69;
            label529: {
               String var81;
               label500: {
                  label530: {
                     try {
                        if (var1.getScheme() == null) {
                           break label530;
                        }
                     } catch (Exception var64) {
                        var10000 = var64;
                        var10001 = false;
                        break label529;
                     }

                     var80 = var1;

                     try {
                        var81 = var1.getScheme().toLowerCase();
                        break label500;
                     } catch (Exception var63) {
                        var10000 = var63;
                        var10001 = false;
                        break label529;
                     }
                  }

                  var81 = "";
               }

               var80 = var1;

               Uri var76;
               label489: {
                  label531: {
                     try {
                        if ("http".equals(var81)) {
                           break label531;
                        }
                     } catch (Exception var62) {
                        var10000 = var62;
                        var10001 = false;
                        break label529;
                     }

                     var80 = var1;

                     try {
                        var3 = "https".equals(var81);
                     } catch (Exception var61) {
                        var10000 = var61;
                        var10001 = false;
                        break label529;
                     }

                     var76 = var1;
                     if (!var3) {
                        break label489;
                     }
                  }

                  try {
                     var76 = var1.normalizeScheme();
                  } catch (Exception var60) {
                     var69 = var60;
                     var80 = var1;

                     try {
                        FileLog.e((Throwable)var69);
                     } catch (Exception var59) {
                        var10000 = var59;
                        var10001 = false;
                        break label529;
                     }

                     var76 = var1;
                  }
               }

               var1 = var76;
               if (!var2) {
                  break label502;
               }

               var80 = var76;
               var1 = var76;

               try {
                  if (!SharedConfig.customTabs) {
                     break label502;
                  }
               } catch (Exception var58) {
                  var10000 = var58;
                  var10001 = false;
                  break label529;
               }

               var1 = var76;
               if (var7) {
                  break label502;
               }

               var80 = var76;

               try {
                  var2 = var81.equals("tel");
               } catch (Exception var57) {
                  var10000 = var57;
                  var10001 = false;
                  break label529;
               }

               var1 = var76;
               if (var2) {
                  break label502;
               }

               Object var4 = null;

               Intent var70;
               List var82;
               String[] var85;
               StringBuilder var87;
               label533: {
                  String[] var71;
                  label548: {
                     label534: {
                        try {
                           var70 = new Intent("android.intent.action.VIEW", Uri.parse("http://www.google.com"));
                           var82 = var0.getPackageManager().queryIntentActivities(var70, 0);
                        } catch (Exception var56) {
                           var10001 = false;
                           break label534;
                        }

                        if (var82 != null) {
                           try {
                              if (!var82.isEmpty()) {
                                 var71 = new String[var82.size()];
                                 break label548;
                              }
                           } catch (Exception var55) {
                              var10001 = false;
                           }
                        }
                     }

                     var85 = null;
                     break label533;
                  }

                  var5 = 0;

                  while(true) {
                     var85 = var71;

                     try {
                        if (var5 >= var82.size()) {
                           break;
                        }

                        var71[var5] = ((ResolveInfo)var82.get(var5)).activityInfo.packageName;
                        if (BuildVars.LOGS_ENABLED) {
                           var87 = new StringBuilder();
                           var87.append("default browser name = ");
                           var87.append(var71[var5]);
                           FileLog.d(var87.toString());
                        }
                     } catch (Exception var54) {
                        var85 = var71;
                        break;
                     }

                     ++var5;
                  }
               }

               List var72 = (List)var4;

               Intent var84;
               label441: {
                  label537: {
                     try {
                        var84 = new Intent;
                     } catch (Exception var53) {
                        var10001 = false;
                        break label537;
                     }

                     var72 = (List)var4;

                     try {
                        var84.<init>("android.intent.action.VIEW", var76);
                     } catch (Exception var52) {
                        var10001 = false;
                        break label537;
                     }

                     var72 = (List)var4;

                     List var73;
                     try {
                        var73 = var0.getPackageManager().queryIntentActivities(var84, 0);
                     } catch (Exception var51) {
                        var10001 = false;
                        break label537;
                     }

                     int var12;
                     if (var85 != null) {
                        var5 = 0;

                        while(true) {
                           var72 = var73;

                           try {
                              if (var5 >= var73.size()) {
                                 break;
                              }
                           } catch (Exception var48) {
                              var10001 = false;
                              break label537;
                           }

                           var12 = 0;

                           int var13;
                           label539: {
                              while(true) {
                                 var13 = var5;
                                 var72 = var73;

                                 try {
                                    if (var12 >= var85.length) {
                                       break label539;
                                    }
                                 } catch (Exception var49) {
                                    var10001 = false;
                                    break label537;
                                 }

                                 var72 = var73;

                                 try {
                                    if (var85[var12].equals(((ResolveInfo)var73.get(var5)).activityInfo.packageName)) {
                                       break;
                                    }
                                 } catch (Exception var50) {
                                    var10001 = false;
                                    break label537;
                                 }

                                 ++var12;
                              }

                              var72 = var73;

                              try {
                                 var73.remove(var5);
                              } catch (Exception var44) {
                                 var10001 = false;
                                 break label537;
                              }

                              var13 = var5 - 1;
                           }

                           var5 = var13 + 1;
                        }
                     } else {
                        var5 = 0;

                        while(true) {
                           var72 = var73;

                           try {
                              if (var5 >= var73.size()) {
                                 break;
                              }
                           } catch (Exception var45) {
                              var10001 = false;
                              break label537;
                           }

                           var72 = var73;

                           label542: {
                              label543: {
                                 try {
                                    if (((ResolveInfo)var73.get(var5)).activityInfo.packageName.toLowerCase().contains("browser")) {
                                       break label543;
                                    }
                                 } catch (Exception var47) {
                                    var10001 = false;
                                    break label537;
                                 }

                                 var12 = var5;
                                 var72 = var73;

                                 try {
                                    if (!((ResolveInfo)var73.get(var5)).activityInfo.packageName.toLowerCase().contains("chrome")) {
                                       break label542;
                                    }
                                 } catch (Exception var46) {
                                    var10001 = false;
                                    break label537;
                                 }
                              }

                              var72 = var73;

                              try {
                                 var73.remove(var5);
                              } catch (Exception var43) {
                                 var10001 = false;
                                 break label537;
                              }

                              var12 = var5 - 1;
                           }

                           var5 = var12 + 1;
                        }
                     }

                     var82 = var73;
                     var72 = var73;

                     try {
                        if (!BuildVars.LOGS_ENABLED) {
                           break label441;
                        }
                     } catch (Exception var42) {
                        var10001 = false;
                        break label537;
                     }

                     var5 = 0;

                     while(true) {
                        var82 = var73;
                        var72 = var73;

                        try {
                           if (var5 >= var73.size()) {
                              break label441;
                           }
                        } catch (Exception var41) {
                           var10001 = false;
                           break;
                        }

                        var72 = var73;

                        try {
                           var87 = new StringBuilder;
                        } catch (Exception var40) {
                           var10001 = false;
                           break;
                        }

                        var72 = var73;

                        try {
                           var87.<init>();
                        } catch (Exception var39) {
                           var10001 = false;
                           break;
                        }

                        var72 = var73;

                        try {
                           var87.append("device has ");
                        } catch (Exception var38) {
                           var10001 = false;
                           break;
                        }

                        var72 = var73;

                        try {
                           var87.append(((ResolveInfo)var73.get(var5)).activityInfo.packageName);
                        } catch (Exception var37) {
                           var10001 = false;
                           break;
                        }

                        var72 = var73;

                        try {
                           var87.append(" to open ");
                        } catch (Exception var36) {
                           var10001 = false;
                           break;
                        }

                        var72 = var73;

                        try {
                           var87.append(var76.toString());
                        } catch (Exception var35) {
                           var10001 = false;
                           break;
                        }

                        var72 = var73;

                        try {
                           FileLog.d(var87.toString());
                        } catch (Exception var34) {
                           var10001 = false;
                           break;
                        }

                        ++var5;
                     }
                  }

                  var82 = var72;
               }

               if (!var6[0] && var82 != null) {
                  var80 = var76;
                  var1 = var76;

                  try {
                     if (!var82.isEmpty()) {
                        break label502;
                     }
                  } catch (Exception var33) {
                     var10000 = var33;
                     var10001 = false;
                     break label529;
                  }
               }

               var80 = var76;

               try {
                  var70 = new Intent;
               } catch (Exception var32) {
                  var10000 = var32;
                  var10001 = false;
                  break label529;
               }

               var80 = var76;

               try {
                  var70.<init>(ApplicationLoader.applicationContext, ShareBroadcastReceiver.class);
               } catch (Exception var31) {
                  var10000 = var31;
                  var10001 = false;
                  break label529;
               }

               var80 = var76;

               try {
                  var70.setAction("android.intent.action.SEND");
               } catch (Exception var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label529;
               }

               var80 = var76;

               Context var74;
               try {
                  var74 = ApplicationLoader.applicationContext;
               } catch (Exception var29) {
                  var10000 = var29;
                  var10001 = false;
                  break label529;
               }

               var80 = var76;

               try {
                  var84 = new Intent;
               } catch (Exception var28) {
                  var10000 = var28;
                  var10001 = false;
                  break label529;
               }

               var80 = var76;

               try {
                  var84.<init>(ApplicationLoader.applicationContext, CustomTabsCopyReceiver.class);
               } catch (Exception var27) {
                  var10000 = var27;
                  var10001 = false;
                  break label529;
               }

               var80 = var76;

               PendingIntent var88;
               try {
                  var88 = PendingIntent.getBroadcast(var74, 0, var84, 134217728);
               } catch (Exception var26) {
                  var10000 = var26;
                  var10001 = false;
                  break label529;
               }

               var80 = var76;

               CustomTabsIntent.Builder var75;
               try {
                  var75 = new CustomTabsIntent.Builder;
               } catch (Exception var25) {
                  var10000 = var25;
                  var10001 = false;
                  break label529;
               }

               var80 = var76;

               try {
                  var75.<init>(getSession());
               } catch (Exception var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label529;
               }

               var80 = var76;

               try {
                  var75.addMenuItem(LocaleController.getString("CopyLink", 2131559164), var88);
               } catch (Exception var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label529;
               }

               var80 = var76;

               try {
                  var75.setToolbarColor(Theme.getColor("actionBarDefault"));
               } catch (Exception var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label529;
               }

               var80 = var76;

               try {
                  var75.setShowTitle(true);
               } catch (Exception var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label529;
               }

               var80 = var76;

               try {
                  var75.setActionButton(BitmapFactory.decodeResource(var0.getResources(), 2131165214), LocaleController.getString("ShareFile", 2131560748), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 0, var70, 0), false);
               } catch (Exception var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label529;
               }

               var80 = var76;

               CustomTabsIntent var83;
               try {
                  var83 = var75.build();
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label529;
               }

               var80 = var76;

               try {
                  var83.setUseNewTask();
               } catch (Exception var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label529;
               }

               var80 = var76;

               try {
                  var83.launchUrl(var0, var76);
                  return;
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
               }
            }

            var69 = var10000;
            FileLog.e((Throwable)var69);
            var1 = var80;
         }

         label545: {
            Intent var78;
            try {
               var78 = new Intent("android.intent.action.VIEW", var1);
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break label545;
            }

            if (var7) {
               try {
                  ComponentName var86 = new ComponentName(var0.getPackageName(), LaunchActivity.class.getName());
                  var78.setComponent(var86);
               } catch (Exception var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label545;
               }
            }

            try {
               var78.putExtra("create_new_tab", true);
               var78.putExtra("com.android.browser.application_id", var0.getPackageName());
               var0.startActivity(var78);
               return;
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
            }
         }

         Exception var68 = var10000;
         FileLog.e((Throwable)var68);
      }

   }

   public static void openUrl(Context var0, String var1) {
      if (var1 != null) {
         openUrl(var0, Uri.parse(var1), true);
      }
   }

   public static void openUrl(Context var0, String var1, boolean var2) {
      if (var0 != null && var1 != null) {
         openUrl(var0, Uri.parse(var1), var2);
      }

   }

   public static void openUrl(Context var0, String var1, boolean var2, boolean var3) {
      openUrl(var0, Uri.parse(var1), var2, var3);
   }

   private static void setCurrentSession(CustomTabsSession var0) {
      customTabsCurrentSession = new WeakReference(var0);
   }

   public static void unbindCustomTabsService(Activity var0) {
      if (customTabsServiceConnection != null) {
         WeakReference var1 = currentCustomTabsActivity;
         Activity var3;
         if (var1 == null) {
            var3 = null;
         } else {
            var3 = (Activity)var1.get();
         }

         if (var3 == var0) {
            currentCustomTabsActivity.clear();
         }

         try {
            var0.unbindService(customTabsServiceConnection);
         } catch (Exception var2) {
         }

         customTabsClient = null;
         customTabsSession = null;
      }
   }

   private static class NavigationCallback extends CustomTabsCallback {
      private NavigationCallback() {
      }

      // $FF: synthetic method
      NavigationCallback(Object var1) {
         this();
      }

      public void onNavigationEvent(int var1, Bundle var2) {
      }
   }
}
