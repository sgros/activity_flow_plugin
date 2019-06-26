package org.secuso.privacyfriendlynetmonitor.Assistant;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.util.Log;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Enumeration;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.PassiveService;

public class ToolBox {
    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    private int hexToBin(char c) {
        return ('0' > c || c > '9') ? ('A' > c || c > 'F') ? ('a' > c || c > 'f') ? -1 : (c - 97) + 10 : (c - 65) + 10 : c - 48;
    }

    public static String printHexBinary(byte[] bArr) {
        StringBuilder stringBuilder = new StringBuilder(bArr.length * 2);
        for (byte b : bArr) {
            stringBuilder.append(hexCode[(b >> 4) & 15]);
            stringBuilder.append(hexCode[b & 15]);
        }
        return stringBuilder.toString();
    }

    public static byte[] hexStringToByteArray(String str) {
        byte[] bArr = new byte[(str.length() / 2)];
        for (int i = 0; i < bArr.length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) Integer.parseInt(str.substring(i2, i2 + 2), 16);
        }
        return bArr;
    }

    public static String printExportHexString(byte[] bArr) {
        String printHexBinary = printHexBinary(bArr);
        String str = "000000 ";
        int i = 0;
        while (i + 1 < printHexBinary.length()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(" ");
            int i2 = i + 2;
            stringBuilder.append(printHexBinary.substring(i, i2));
            i = i2;
            str = stringBuilder.toString();
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append(" ......");
        return stringBuilder2.toString();
    }

    public String getIfs(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(context.getFilesDir().getAbsolutePath());
        stringBuilder.append(File.separator);
        stringBuilder.append(Const.FILE_IF_LIST);
        String stringBuilder2 = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append("rm ");
        stringBuilder.append(stringBuilder2);
        ExecCom.user(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("netcfg | grep UP -> ");
        stringBuilder.append(stringBuilder2);
        ExecCom.user(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("cat ");
        stringBuilder.append(stringBuilder2);
        return ExecCom.userForResult(stringBuilder.toString());
    }

    public static InetAddress getLocalAddress() {
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration inetAddresses = ((NetworkInterface) networkInterfaces.nextElement()).getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(Const.LOG_TAG, "Error while obtaining local address");
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isAnalyzerServiceRunning() {
        for (RunningServiceInfo runningServiceInfo : ((ActivityManager) RunStore.getContext().getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (PassiveService.class.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static int searchByteArray(byte[] bArr, byte[] bArr2) {
        Byte[] bArr3 = new Byte[bArr2.length];
        for (int i = 0; i < bArr2.length; i++) {
            bArr3[i] = Byte.valueOf(bArr2[i]);
        }
        ArrayDeque arrayDeque = new ArrayDeque(bArr.length);
        for (int i2 = 0; i2 < bArr.length; i2++) {
            if (arrayDeque.size() != bArr3.length) {
                arrayDeque.addLast(Byte.valueOf(bArr[i2]));
            } else if (Arrays.equals((Byte[]) arrayDeque.toArray(new Byte[0]), bArr3)) {
                return i2 - bArr3.length;
            } else {
                arrayDeque.pop();
                arrayDeque.addLast(Byte.valueOf(bArr[i2]));
            }
        }
        return -1;
    }

    public static byte[] longToFourBytes(long j) {
        ByteBuffer allocate = ByteBuffer.allocate(8);
        byte[] bArr = new byte[4];
        allocate.putLong(j);
        allocate.position(4);
        allocate.get(bArr);
        return bArr;
    }

    public static byte[] intToTwoBytes(int i) {
        ByteBuffer allocate = ByteBuffer.allocate(4);
        byte[] bArr = new byte[2];
        allocate.putInt(i);
        allocate.position(2);
        allocate.get(bArr);
        return bArr;
    }

    public static long fourBytesToLong(byte[] bArr) {
        ByteBuffer allocate = ByteBuffer.allocate(8);
        allocate.position(4);
        allocate.put(bArr);
        allocate.position(0);
        return allocate.getLong();
    }

    public static int twoBytesToInt(byte[] bArr) {
        ByteBuffer allocate = ByteBuffer.allocate(4);
        allocate.position(2);
        allocate.put(bArr);
        allocate.position(0);
        return allocate.getInt();
    }

    public static byte[] reverseByteArray(byte[] bArr) {
        int i = 0;
        byte[] bArr2 = new byte[bArr.length];
        for (int length = bArr.length - 1; length >= 0; length--) {
            bArr2[i] = bArr[length];
            i++;
        }
        return bArr2;
    }

    public static String hexToIp6(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        while (i < str.length()) {
            int i2 = i + 8;
            String substring = str.substring(i, i2);
            int length = substring.length() - 1;
            while (length >= 0) {
                stringBuilder.append(substring.substring(length - 1, length + 1));
                stringBuilder.append(length == 5 ? ":" : "");
                length -= 2;
            }
            stringBuilder.append(":");
            i = i2;
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1).toString();
    }

    public static String hexToIp4(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int length = str.length() - 1; length >= 0; length -= 2) {
            stringBuilder.append(Integer.parseInt(str.substring(length - 1, length + 1), 16));
            stringBuilder.append(".");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1).toString();
    }
}
