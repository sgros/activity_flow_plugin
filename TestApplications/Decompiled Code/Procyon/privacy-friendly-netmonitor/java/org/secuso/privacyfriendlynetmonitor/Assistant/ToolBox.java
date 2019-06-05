// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Assistant;

import java.io.File;
import android.content.Context;
import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.Iterator;
import android.app.ActivityManager$RunningServiceInfo;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.PassiveService;
import android.app.ActivityManager;
import java.util.Enumeration;
import android.util.Log;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class ToolBox
{
    private static final char[] hexCode;
    
    static {
        hexCode = "0123456789ABCDEF".toCharArray();
    }
    
    public static long fourBytesToLong(final byte[] src) {
        final ByteBuffer allocate = ByteBuffer.allocate(8);
        allocate.position(4);
        allocate.put(src);
        allocate.position(0);
        return allocate.getLong();
    }
    
    public static InetAddress getLocalAddress() {
        try {
            final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                final Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    final InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress;
                    }
                }
            }
            return null;
        }
        catch (Exception ex) {
            Log.e("NetMonitor", "Error while obtaining local address");
            ex.printStackTrace();
        }
        return null;
    }
    
    public static byte[] hexStringToByteArray(final String s) {
        final byte[] array = new byte[s.length() / 2];
        for (int i = 0; i < array.length; ++i) {
            final int beginIndex = i * 2;
            array[i] = (byte)Integer.parseInt(s.substring(beginIndex, beginIndex + 2), 16);
        }
        return array;
    }
    
    private int hexToBin(final char c) {
        if ('0' <= c && c <= '9') {
            return c - '0';
        }
        if ('A' <= c && c <= 'F') {
            return c - 'A' + 10;
        }
        if ('a' <= c && c <= 'f') {
            return c - 'a' + 10;
        }
        return -1;
    }
    
    public static String hexToIp4(final String s) {
        final StringBuilder sb = new StringBuilder();
        for (int i = s.length() - 1; i >= 0; i -= 2) {
            sb.append(Integer.parseInt(s.substring(i - 1, i + 1), 16));
            sb.append(".");
        }
        return sb.substring(0, sb.length() - 1).toString();
    }
    
    public static String hexToIp6(final String s) {
        final StringBuilder sb = new StringBuilder();
        int endIndex;
        for (int i = 0; i < s.length(); i = endIndex) {
            endIndex = i + 8;
            final String substring = s.substring(i, endIndex);
            for (int j = substring.length() - 1; j >= 0; j -= 2) {
                sb.append(substring.substring(j - 1, j + 1));
                String str;
                if (j == 5) {
                    str = ":";
                }
                else {
                    str = "";
                }
                sb.append(str);
            }
            sb.append(":");
        }
        return sb.substring(0, sb.length() - 1).toString();
    }
    
    public static byte[] intToTwoBytes(final int n) {
        final ByteBuffer allocate = ByteBuffer.allocate(4);
        final byte[] dst = new byte[2];
        allocate.putInt(n);
        allocate.position(2);
        allocate.get(dst);
        return dst;
    }
    
    public static boolean isAnalyzerServiceRunning() {
        final Iterator<ActivityManager$RunningServiceInfo> iterator = ((ActivityManager)RunStore.getContext().getSystemService("activity")).getRunningServices(Integer.MAX_VALUE).iterator();
        while (iterator.hasNext()) {
            if (PassiveService.class.getName().equals(iterator.next().service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    
    public static byte[] longToFourBytes(final long n) {
        final ByteBuffer allocate = ByteBuffer.allocate(8);
        final byte[] dst = new byte[4];
        allocate.putLong(n);
        allocate.position(4);
        allocate.get(dst);
        return dst;
    }
    
    public static String printExportHexString(final byte[] array) {
        final String printHexBinary = printHexBinary(array);
        String string = "000000 ";
        int endIndex;
        for (int beginIndex = 0; beginIndex + 1 < printHexBinary.length(); beginIndex = endIndex) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(" ");
            endIndex = beginIndex + 2;
            sb.append(printHexBinary.substring(beginIndex, endIndex));
            string = sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(string);
        sb2.append(" ......");
        return sb2.toString();
    }
    
    public static String printHexBinary(final byte[] array) {
        int i = 0;
        final StringBuilder sb = new StringBuilder(array.length * 2);
        while (i < array.length) {
            final byte b = array[i];
            sb.append(ToolBox.hexCode[b >> 4 & 0xF]);
            sb.append(ToolBox.hexCode[b & 0xF]);
            ++i;
        }
        return sb.toString();
    }
    
    public static byte[] reverseByteArray(final byte[] array) {
        int n = 0;
        final byte[] array2 = new byte[array.length];
        for (int i = array.length - 1; i >= 0; --i) {
            array2[n] = array[i];
            ++n;
        }
        return array2;
    }
    
    public static int searchByteArray(final byte[] array, final byte[] array2) {
        final Byte[] a2 = new Byte[array2.length];
        for (int i = 0; i < array2.length; ++i) {
            a2[i] = array2[i];
        }
        final int n = -1;
        final ArrayDeque<Object> arrayDeque = new ArrayDeque<Object>(array.length);
        int n2 = 0;
        int n3;
        while (true) {
            n3 = n;
            if (n2 >= array.length) {
                break;
            }
            if (arrayDeque.size() == a2.length) {
                if (Arrays.equals(arrayDeque.toArray(new Byte[0]), a2)) {
                    n3 = n2 - a2.length;
                    break;
                }
                arrayDeque.pop();
                arrayDeque.addLast(array[n2]);
            }
            else {
                arrayDeque.addLast(array[n2]);
            }
            ++n2;
        }
        return n3;
    }
    
    public static int twoBytesToInt(final byte[] src) {
        final ByteBuffer allocate = ByteBuffer.allocate(4);
        allocate.position(2);
        allocate.put(src);
        allocate.position(0);
        return allocate.getInt();
    }
    
    public String getIfs(final Context context) {
        final StringBuilder sb = new StringBuilder();
        sb.append(context.getFilesDir().getAbsolutePath());
        sb.append(File.separator);
        sb.append("iflist");
        final String string = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("rm ");
        sb2.append(string);
        ExecCom.user(sb2.toString());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("netcfg | grep UP -> ");
        sb3.append(string);
        ExecCom.user(sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("cat ");
        sb4.append(string);
        return ExecCom.userForResult(sb4.toString());
    }
}
