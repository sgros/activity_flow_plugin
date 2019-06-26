// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.voip;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import java.util.Enumeration;
import java.util.Iterator;
import org.telegram.messenger.FileLog;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Inet6Address;
import java.net.Inet4Address;
import android.net.LinkAddress;
import android.annotation.TargetApi;
import android.net.LinkProperties;
import android.net.Network;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.telephony.SubscriptionManager;
import android.os.Build$VERSION;
import org.telegram.messenger.ApplicationLoader;
import android.telephony.TelephonyManager;

public class JNIUtilities
{
    public static String[] getCarrierInfo() {
        TelephonyManager forSubscriptionId;
        final TelephonyManager telephonyManager = forSubscriptionId = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
        if (Build$VERSION.SDK_INT >= 24) {
            forSubscriptionId = telephonyManager.createForSubscriptionId(SubscriptionManager.getDefaultDataSubscriptionId());
        }
        if (!TextUtils.isEmpty((CharSequence)forSubscriptionId.getNetworkOperatorName())) {
            final String networkOperator = forSubscriptionId.getNetworkOperator();
            String substring = "";
            String substring2;
            if (networkOperator != null && networkOperator.length() > 3) {
                substring = networkOperator.substring(0, 3);
                substring2 = networkOperator.substring(3);
            }
            else {
                substring2 = "";
            }
            return new String[] { forSubscriptionId.getNetworkOperatorName(), forSubscriptionId.getNetworkCountryIso().toUpperCase(), substring, substring2 };
        }
        return null;
    }
    
    @TargetApi(23)
    public static String getCurrentNetworkInterfaceName() {
        final ConnectivityManager connectivityManager = (ConnectivityManager)ApplicationLoader.applicationContext.getSystemService("connectivity");
        final Network activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork == null) {
            return null;
        }
        final LinkProperties linkProperties = connectivityManager.getLinkProperties(activeNetwork);
        if (linkProperties == null) {
            return null;
        }
        return linkProperties.getInterfaceName();
    }
    
    public static String[] getLocalNetworkAddressesAndInterfaceName() {
        final ConnectivityManager connectivityManager = (ConnectivityManager)ApplicationLoader.applicationContext.getSystemService("connectivity");
        final int sdk_INT = Build$VERSION.SDK_INT;
        String hostAddress = null;
        if (sdk_INT >= 23) {
            final Network activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork == null) {
                return null;
            }
            final LinkProperties linkProperties = connectivityManager.getLinkProperties(activeNetwork);
            if (linkProperties == null) {
                return null;
            }
            final Iterator iterator = linkProperties.getLinkAddresses().iterator();
            String hostAddress2 = null;
            while (iterator.hasNext()) {
                final InetAddress address = iterator.next().getAddress();
                if (address instanceof Inet4Address) {
                    if (address.isLinkLocalAddress()) {
                        continue;
                    }
                    hostAddress = address.getHostAddress();
                }
                else {
                    if (!(address instanceof Inet6Address) || address.isLinkLocalAddress() || (address.getAddress()[0] & 0xF0) == 0xF0) {
                        continue;
                    }
                    hostAddress2 = address.getHostAddress();
                }
            }
            return new String[] { linkProperties.getInterfaceName(), hostAddress, hostAddress2 };
        }
        else {
            try {
                final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                if (networkInterfaces == null) {
                    return null;
                }
                while (networkInterfaces.hasMoreElements()) {
                    final NetworkInterface networkInterface = networkInterfaces.nextElement();
                    if (!networkInterface.isLoopback()) {
                        if (!networkInterface.isUp()) {
                            continue;
                        }
                        final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                        String hostAddress4;
                        String hostAddress3 = hostAddress4 = null;
                        while (inetAddresses.hasMoreElements()) {
                            final InetAddress inetAddress = inetAddresses.nextElement();
                            if (inetAddress instanceof Inet4Address) {
                                if (inetAddress.isLinkLocalAddress()) {
                                    continue;
                                }
                                hostAddress3 = inetAddress.getHostAddress();
                            }
                            else {
                                if (!(inetAddress instanceof Inet6Address) || inetAddress.isLinkLocalAddress() || (inetAddress.getAddress()[0] & 0xF0) == 0xF0) {
                                    continue;
                                }
                                hostAddress4 = inetAddress.getHostAddress();
                            }
                        }
                        return new String[] { networkInterface.getName(), hostAddress3, hostAddress4 };
                    }
                }
                return null;
            }
            catch (Exception ex) {
                FileLog.e(ex);
                return null;
            }
        }
    }
    
    public static int getMaxVideoResolution() {
        return 320;
    }
    
    public static String getSupportedVideoCodecs() {
        return "";
    }
    
    public static int[] getWifiInfo() {
        try {
            final WifiInfo connectionInfo = ((WifiManager)ApplicationLoader.applicationContext.getSystemService("wifi")).getConnectionInfo();
            return new int[] { connectionInfo.getRssi(), connectionInfo.getLinkSpeed() };
        }
        catch (Exception ex) {
            return null;
        }
    }
}
