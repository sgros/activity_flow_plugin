package org.telegram.messenger.voip;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Iterator;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;

public class JNIUtilities {
   public static String[] getCarrierInfo() {
      TelephonyManager var0 = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
      TelephonyManager var1 = var0;
      if (VERSION.SDK_INT >= 24) {
         var1 = var0.createForSubscriptionId(SubscriptionManager.getDefaultDataSubscriptionId());
      }

      if (TextUtils.isEmpty(var1.getNetworkOperatorName())) {
         return null;
      } else {
         String var3 = var1.getNetworkOperator();
         String var2 = "";
         if (var3 != null && var3.length() > 3) {
            var2 = var3.substring(0, 3);
            var3 = var3.substring(3);
         } else {
            var3 = "";
         }

         return new String[]{var1.getNetworkOperatorName(), var1.getNetworkCountryIso().toUpperCase(), var2, var3};
      }
   }

   @TargetApi(23)
   public static String getCurrentNetworkInterfaceName() {
      ConnectivityManager var0 = (ConnectivityManager)ApplicationLoader.applicationContext.getSystemService("connectivity");
      Network var1 = var0.getActiveNetwork();
      if (var1 == null) {
         return null;
      } else {
         LinkProperties var2 = var0.getLinkProperties(var1);
         return var2 == null ? null : var2.getInterfaceName();
      }
   }

   public static String[] getLocalNetworkAddressesAndInterfaceName() {
      ConnectivityManager var0 = (ConnectivityManager)ApplicationLoader.applicationContext.getSystemService("connectivity");
      int var1 = VERSION.SDK_INT;
      String var2 = null;
      InetAddress var5;
      String var15;
      if (var1 >= 23) {
         Network var17 = var0.getActiveNetwork();
         if (var17 == null) {
            return null;
         } else {
            LinkProperties var14 = var0.getLinkProperties(var17);
            if (var14 == null) {
               return null;
            } else {
               Iterator var18 = var14.getLinkAddresses().iterator();
               var15 = null;

               while(var18.hasNext()) {
                  var5 = ((LinkAddress)var18.next()).getAddress();
                  if (var5 instanceof Inet4Address) {
                     if (!var5.isLinkLocalAddress()) {
                        var2 = var5.getHostAddress();
                     }
                  } else if (var5 instanceof Inet6Address && !var5.isLinkLocalAddress() && (var5.getAddress()[0] & 240) != 240) {
                     var15 = var5.getHostAddress();
                  }
               }

               return new String[]{var14.getInterfaceName(), var2, var15};
            }
         }
      } else {
         Exception var10000;
         label122: {
            Enumeration var3;
            boolean var10001;
            try {
               var3 = NetworkInterface.getNetworkInterfaces();
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label122;
            }

            if (var3 == null) {
               return null;
            }

            NetworkInterface var12;
            while(true) {
               try {
                  do {
                     if (!var3.hasMoreElements()) {
                        return null;
                     }

                     var12 = (NetworkInterface)var3.nextElement();
                  } while(var12.isLoopback());

                  if (var12.isUp()) {
                     break;
                  }
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label122;
               }
            }

            Enumeration var4;
            try {
               var4 = var12.getInetAddresses();
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label122;
            }

            var2 = null;
            var15 = var2;

            label98:
            while(true) {
               while(true) {
                  try {
                     if (!var4.hasMoreElements()) {
                        break label98;
                     }

                     var5 = (InetAddress)var4.nextElement();
                     if (!(var5 instanceof Inet4Address)) {
                        break;
                     }

                     if (!var5.isLinkLocalAddress()) {
                        var2 = var5.getHostAddress();
                     }
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label122;
                  }
               }

               try {
                  if (var5 instanceof Inet6Address && !var5.isLinkLocalAddress() && (var5.getAddress()[0] & 240) != 240) {
                     var15 = var5.getHostAddress();
                  }
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label122;
               }
            }

            try {
               String var13 = var12.getName();
               return new String[]{var13, var2, var15};
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
            }
         }

         Exception var16 = var10000;
         FileLog.e((Throwable)var16);
         return null;
      }
   }

   public static int getMaxVideoResolution() {
      return 320;
   }

   public static String getSupportedVideoCodecs() {
      return "";
   }

   public static int[] getWifiInfo() {
      int var1;
      int var2;
      try {
         WifiInfo var0 = ((WifiManager)ApplicationLoader.applicationContext.getSystemService("wifi")).getConnectionInfo();
         var1 = var0.getRssi();
         var2 = var0.getLinkSpeed();
      } catch (Exception var3) {
         return null;
      }

      return new int[]{var1, var2};
   }
}
