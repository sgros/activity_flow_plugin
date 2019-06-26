package org.osmdroid.tileprovider.util;

import android.os.Build.VERSION;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

public class StorageUtils {

    public static class StorageInfo {
        String displayName = "";
        public final int display_number;
        public long freeSpace = 0;
        public final boolean internal;
        public final String path;
        public boolean readonly;

        public StorageInfo(String str, boolean z, boolean z2, int i) {
            this.path = str;
            this.internal = z;
            this.display_number = i;
            if (VERSION.SDK_INT >= 9) {
                this.freeSpace = new File(str).getFreeSpace();
            }
            if (z2) {
                this.readonly = z2;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(File.separator);
                stringBuilder.append(UUID.randomUUID().toString());
                File file = new File(stringBuilder.toString());
                try {
                    file.createNewFile();
                    file.delete();
                    this.readonly = false;
                } catch (Throwable unused) {
                    this.readonly = true;
                }
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            if (z) {
                stringBuilder2.append("Internal SD card");
            } else if (i > 1) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("SD card ");
                stringBuilder3.append(i);
                stringBuilder2.append(stringBuilder3.toString());
            } else {
                stringBuilder2.append("SD card");
            }
            if (z2) {
                stringBuilder2.append(" (Read only)");
            }
            this.displayName = stringBuilder2.toString();
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:125:0x020d in {4, 12, 13, 18, 32, 34, 35, 36, 39, 41, 43, 47, 59, 63, 64, 66, 68, 69, 72, 74, 75, 79, 81, 86, 87, 101, 102, 107, 108, 111, 113, 116, 117, 118, 119, 122, 123, 124} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private static java.util.Set<java.io.File> getAllStorageLocationsRevised() {
        /*
        r0 = " ";
        r1 = ":";
        r2 = new java.util.HashSet;
        r2.<init>();
        r3 = "EXTERNAL_STORAGE";
        r3 = java.lang.System.getenv(r3);
        if (r3 == 0) goto L_0x0030;
        r4 = new java.io.File;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r5.append(r3);
        r3 = java.io.File.separator;
        r5.append(r3);
        r3 = r5.toString();
        r4.<init>(r3);
        r3 = isWritable(r4);
        if (r3 == 0) goto L_0x0030;
        r2.add(r4);
        r3 = "SECONDARY_STORAGE";
        r3 = java.lang.System.getenv(r3);
        r4 = 0;
        if (r3 == 0) goto L_0x0067;
        r5 = java.io.File.pathSeparator;
        r3 = r3.split(r5);
        r5 = 0;
        r6 = r3.length;
        if (r5 >= r6) goto L_0x0067;
        r6 = new java.io.File;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = r3[r5];
        r7.append(r8);
        r8 = java.io.File.separator;
        r7.append(r8);
        r7 = r7.toString();
        r6.<init>(r7);
        r7 = isWritable(r6);
        if (r7 == 0) goto L_0x0064;
        r2.add(r6);
        r5 = r5 + 1;
        goto L_0x0040;
        r3 = android.os.Environment.getExternalStorageDirectory();
        if (r3 == 0) goto L_0x007a;
        r3 = android.os.Environment.getExternalStorageDirectory();
        r5 = isWritable(r3);
        if (r5 == 0) goto L_0x007a;
        r2.add(r3);
        r3 = new java.util.ArrayList;
        r5 = 10;
        r3.<init>(r5);
        r6 = new java.util.ArrayList;
        r6.<init>(r5);
        r7 = "/mnt/sdcard";
        r3.add(r7);
        r6.add(r7);
        r8 = 0;
        r9 = 1;
        r10 = new java.io.File;	 Catch:{ Exception -> 0x00d1, all -> 0x00cd }
        r11 = "/proc/mounts";	 Catch:{ Exception -> 0x00d1, all -> 0x00cd }
        r10.<init>(r11);	 Catch:{ Exception -> 0x00d1, all -> 0x00cd }
        r11 = r10.exists();	 Catch:{ Exception -> 0x00d1, all -> 0x00cd }
        if (r11 == 0) goto L_0x00c6;	 Catch:{ Exception -> 0x00d1, all -> 0x00cd }
        r11 = new java.util.Scanner;	 Catch:{ Exception -> 0x00d1, all -> 0x00cd }
        r11.<init>(r10);	 Catch:{ Exception -> 0x00d1, all -> 0x00cd }
        r10 = r11.hasNext();	 Catch:{ Exception -> 0x00c4 }
        if (r10 == 0) goto L_0x00c7;	 Catch:{ Exception -> 0x00c4 }
        r10 = r11.nextLine();	 Catch:{ Exception -> 0x00c4 }
        r12 = "/dev/block/vold/";	 Catch:{ Exception -> 0x00c4 }
        r12 = r10.startsWith(r12);	 Catch:{ Exception -> 0x00c4 }
        if (r12 == 0) goto L_0x00a2;	 Catch:{ Exception -> 0x00c4 }
        r10 = r10.split(r0);	 Catch:{ Exception -> 0x00c4 }
        r10 = r10[r9];	 Catch:{ Exception -> 0x00c4 }
        r12 = r10.equals(r7);	 Catch:{ Exception -> 0x00c4 }
        if (r12 != 0) goto L_0x00a2;	 Catch:{ Exception -> 0x00c4 }
        r3.add(r10);	 Catch:{ Exception -> 0x00c4 }
        goto L_0x00a2;
        r10 = move-exception;
        goto L_0x00d3;
        r11 = r8;
        if (r11 == 0) goto L_0x00d9;
        r11.close();	 Catch:{ Exception -> 0x00d9 }
        goto L_0x00d9;
        r0 = move-exception;
        r11 = r8;
        goto L_0x0207;
        r10 = move-exception;
        r11 = r8;
        r10.printStackTrace();	 Catch:{ all -> 0x0206 }
        if (r11 == 0) goto L_0x00d9;
        goto L_0x00c9;
    L_0x00d9:
        r10 = new java.io.File;	 Catch:{ Exception -> 0x012e }
        r11 = "/system/etc/vold.fstab";	 Catch:{ Exception -> 0x012e }
        r10.<init>(r11);	 Catch:{ Exception -> 0x012e }
        r11 = r10.exists();	 Catch:{ Exception -> 0x012e }
        if (r11 == 0) goto L_0x0124;	 Catch:{ Exception -> 0x012e }
        r11 = new java.util.Scanner;	 Catch:{ Exception -> 0x012e }
        r11.<init>(r10);	 Catch:{ Exception -> 0x012e }
        r8 = r11.hasNext();	 Catch:{ Exception -> 0x0121, all -> 0x011e }
        if (r8 == 0) goto L_0x011c;	 Catch:{ Exception -> 0x0121, all -> 0x011e }
        r8 = r11.nextLine();	 Catch:{ Exception -> 0x0121, all -> 0x011e }
        r10 = "dev_mount";	 Catch:{ Exception -> 0x0121, all -> 0x011e }
        r10 = r8.startsWith(r10);	 Catch:{ Exception -> 0x0121, all -> 0x011e }
        if (r10 == 0) goto L_0x00eb;	 Catch:{ Exception -> 0x0121, all -> 0x011e }
        r8 = r8.split(r0);	 Catch:{ Exception -> 0x0121, all -> 0x011e }
        r10 = 2;	 Catch:{ Exception -> 0x0121, all -> 0x011e }
        r8 = r8[r10];	 Catch:{ Exception -> 0x0121, all -> 0x011e }
        r10 = r8.contains(r1);	 Catch:{ Exception -> 0x0121, all -> 0x011e }
        if (r10 == 0) goto L_0x0112;	 Catch:{ Exception -> 0x0121, all -> 0x011e }
        r10 = r8.indexOf(r1);	 Catch:{ Exception -> 0x0121, all -> 0x011e }
        r8 = r8.substring(r4, r10);	 Catch:{ Exception -> 0x0121, all -> 0x011e }
        r10 = r8.equals(r7);	 Catch:{ Exception -> 0x0121, all -> 0x011e }
        if (r10 != 0) goto L_0x00eb;	 Catch:{ Exception -> 0x0121, all -> 0x011e }
        r6.add(r8);	 Catch:{ Exception -> 0x0121, all -> 0x011e }
        goto L_0x00eb;
        r8 = r11;
        goto L_0x0124;
        r0 = move-exception;
        goto L_0x0200;
        r0 = move-exception;
        r8 = r11;
        goto L_0x012f;
        if (r8 == 0) goto L_0x0135;
        r8.close();	 Catch:{ Exception -> 0x0135 }
        goto L_0x0135;
        r0 = move-exception;
        r11 = r8;
        goto L_0x0200;
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x012a }
        if (r8 == 0) goto L_0x0135;
        goto L_0x0126;
        r0 = 0;
        r7 = r3.size();
        if (r0 >= r7) goto L_0x0150;
        r7 = r3.get(r0);
        r7 = (java.lang.String) r7;
        r7 = r6.contains(r7);
        if (r7 != 0) goto L_0x014e;
        r7 = r0 + -1;
        r3.remove(r0);
        r0 = r7;
        r0 = r0 + r9;
        goto L_0x0136;
        r6.clear();
        r0 = new java.util.ArrayList;
        r0.<init>(r5);
        r5 = r3.iterator();
        r6 = r5.hasNext();
        if (r6 == 0) goto L_0x01fc;
        r6 = r5.next();
        r6 = (java.lang.String) r6;
        r7 = new java.io.File;
        r7.<init>(r6);
        r6 = r7.exists();
        if (r6 == 0) goto L_0x015c;
        r6 = r7.isDirectory();
        if (r6 == 0) goto L_0x015c;
        r6 = r7.canWrite();
        if (r6 == 0) goto L_0x015c;
        r6 = r7.listFiles();
        r8 = "[";
        if (r6 == 0) goto L_0x01b8;
        r10 = r6.length;
        r11 = r8;
        r8 = 0;
        if (r8 >= r10) goto L_0x01b7;
        r12 = r6[r8];
        r13 = new java.lang.StringBuilder;
        r13.<init>();
        r13.append(r11);
        r11 = r12.getName();
        r11 = r11.hashCode();
        r13.append(r11);
        r13.append(r1);
        r11 = r12.length();
        r13.append(r11);
        r11 = ", ";
        r13.append(r11);
        r11 = r13.toString();
        r8 = r8 + 1;
        goto L_0x018a;
        r8 = r11;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r6.append(r8);
        r8 = "]";
        r6.append(r8);
        r6 = r6.toString();
        r8 = r0.contains(r6);
        if (r8 != 0) goto L_0x015c;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r10 = "sdCard_";
        r8.append(r10);
        r10 = r2.size();
        r8.append(r10);
        r8.toString();
        r8 = r2.size();
        if (r8 != 0) goto L_0x01ea;
        goto L_0x01ee;
        r8 = r2.size();
        r0.add(r6);
        r6 = isWritable(r7);
        if (r6 == 0) goto L_0x015c;
        r2.add(r7);
        goto L_0x015c;
        r3.clear();
        return r2;
        if (r11 == 0) goto L_0x0205;
        r11.close();	 Catch:{ Exception -> 0x0205 }
        throw r0;
        r0 = move-exception;
        if (r11 == 0) goto L_0x020c;
        r11.close();	 Catch:{ Exception -> 0x020c }
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.util.StorageUtils.getAllStorageLocationsRevised():java.util.Set");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:117:0x018d in {5, 7, 8, 15, 17, 18, 21, 23, 28, 29, 31, 32, 35, 37, 48, 51, 54, 67, 73, 76, 78, 80, 82, 84, 85, 88, 91, 92, 96, 98, 106, 107, 108, 110, 111, 114, 115, 116} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public static java.util.List<org.osmdroid.tileprovider.util.StorageUtils.StorageInfo> getStorageList() {
        /*
        r0 = "StorageUtils";
        r1 = "/proc/mounts";
        r2 = "mounted_ro";
        r3 = "";
        r4 = new java.util.ArrayList;
        r4.<init>();
        r5 = android.os.Environment.getExternalStorageDirectory();	 Catch:{ Throwable -> 0x001c }
        if (r5 == 0) goto L_0x0020;	 Catch:{ Throwable -> 0x001c }
        r5 = android.os.Environment.getExternalStorageDirectory();	 Catch:{ Throwable -> 0x001c }
        r5 = r5.getPath();	 Catch:{ Throwable -> 0x001c }
        goto L_0x0021;
        r5 = move-exception;
        r5.printStackTrace();
        r5 = r3;
        r6 = 1;
        r7 = 0;
        r8 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Throwable -> 0x0031 }
        r9 = 9;	 Catch:{ Throwable -> 0x0031 }
        if (r8 < r9) goto L_0x0035;	 Catch:{ Throwable -> 0x0031 }
        r8 = android.os.Environment.isExternalStorageRemovable();	 Catch:{ Throwable -> 0x0031 }
        if (r8 != 0) goto L_0x0035;
        r8 = 1;
        goto L_0x0036;
        r8 = move-exception;
        r8.printStackTrace();
        r8 = 0;
        r3 = android.os.Environment.getExternalStorageState();	 Catch:{ Throwable -> 0x003b }
        goto L_0x003f;
        r9 = move-exception;
        r9.printStackTrace();
        r9 = "mounted";	 Catch:{ Throwable -> 0x004f }
        r9 = r3.equals(r9);	 Catch:{ Throwable -> 0x004f }
        if (r9 != 0) goto L_0x004d;	 Catch:{ Throwable -> 0x004f }
        r3 = r3.equals(r2);	 Catch:{ Throwable -> 0x004f }
        if (r3 == 0) goto L_0x0053;
        r3 = 1;
        goto L_0x0054;
        r3 = move-exception;
        r3.printStackTrace();
        r3 = 0;
        r9 = android.os.Environment.getExternalStorageState();	 Catch:{ Throwable -> 0x005d }
        r2 = r9.equals(r2);	 Catch:{ Throwable -> 0x005d }
        goto L_0x0062;
        r2 = move-exception;
        r2.printStackTrace();
        r2 = 1;
        r9 = 0;
        r10 = -1;
        r11 = new java.util.HashSet;	 Catch:{ FileNotFoundException -> 0x013d, IOException -> 0x0133 }
        r11.<init>();	 Catch:{ FileNotFoundException -> 0x013d, IOException -> 0x0133 }
        r12 = new java.io.BufferedReader;	 Catch:{ FileNotFoundException -> 0x013d, IOException -> 0x0133 }
        r13 = new java.io.FileReader;	 Catch:{ FileNotFoundException -> 0x013d, IOException -> 0x0133 }
        r13.<init>(r1);	 Catch:{ FileNotFoundException -> 0x013d, IOException -> 0x0133 }
        r12.<init>(r13);	 Catch:{ FileNotFoundException -> 0x013d, IOException -> 0x0133 }
        android.util.Log.d(r0, r1);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r1 = 1;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r9 = r12.readLine();	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        if (r9 == 0) goto L_0x010e;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        android.util.Log.d(r0, r9);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r13 = "vfat";	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r13 = r9.contains(r13);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        if (r13 != 0) goto L_0x0090;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r13 = "/mnt";	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r13 = r9.contains(r13);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        if (r13 == 0) goto L_0x0077;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r13 = new java.util.StringTokenizer;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r14 = " ";	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r13.<init>(r9, r14);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r13.nextToken();	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r14 = r13.nextToken();	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r15 = r11.contains(r14);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        if (r15 == 0) goto L_0x00a5;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        goto L_0x0077;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r13.nextToken();	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r13 = r13.nextToken();	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r15 = ",";	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r13 = r13.split(r15);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r13 = java.util.Arrays.asList(r13);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r15 = "ro";	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r13 = r13.contains(r15);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r15 = r14.equals(r5);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        if (r15 == 0) goto L_0x00ce;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r11.add(r5);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r9 = new org.osmdroid.tileprovider.util.StorageUtils$StorageInfo;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r9.<init>(r5, r8, r13, r10);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r4.add(r7, r9);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        goto L_0x0077;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r15 = "/dev/block/vold";	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r15 = r9.contains(r15);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        if (r15 == 0) goto L_0x0077;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r15 = "/mnt/secure";	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r15 = r9.contains(r15);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        if (r15 != 0) goto L_0x0077;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r15 = "/mnt/asec";	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r15 = r9.contains(r15);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        if (r15 != 0) goto L_0x0077;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r15 = "/mnt/obb";	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r15 = r9.contains(r15);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        if (r15 != 0) goto L_0x0077;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r15 = "/dev/mapper";	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r15 = r9.contains(r15);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        if (r15 != 0) goto L_0x0077;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r15 = "tmpfs";	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r9 = r9.contains(r15);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        if (r9 != 0) goto L_0x0077;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r11.add(r14);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r9 = new org.osmdroid.tileprovider.util.StorageUtils$StorageInfo;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r15 = r1 + 1;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r9.<init>(r14, r7, r13, r1);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r4.add(r9);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r1 = r15;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        goto L_0x0077;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r0 = r11.contains(r5);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        if (r0 != 0) goto L_0x0124;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        if (r3 == 0) goto L_0x0124;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r0 = r5.length();	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        if (r0 <= 0) goto L_0x0124;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r0 = new org.osmdroid.tileprovider.util.StorageUtils$StorageInfo;	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r0.<init>(r5, r8, r2, r10);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r4.add(r7, r0);	 Catch:{ FileNotFoundException -> 0x012d, IOException -> 0x012a, all -> 0x0128 }
        r12.close();	 Catch:{ IOException -> 0x0144 }
        goto L_0x0144;
        r0 = move-exception;
        goto L_0x0187;
        r0 = move-exception;
        r9 = r12;
        goto L_0x0134;
        r0 = move-exception;
        r9 = r12;
        goto L_0x013e;
        r0 = move-exception;
        r12 = r9;
        goto L_0x0187;
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0130 }
        if (r9 == 0) goto L_0x0144;
        r9.close();	 Catch:{ IOException -> 0x0144 }
        goto L_0x0144;
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0130 }
        if (r9 == 0) goto L_0x0144;
        goto L_0x0139;
        r0 = getAllStorageLocationsRevised();
        r0 = r0.iterator();
        r1 = r0.hasNext();
        if (r1 == 0) goto L_0x0186;
        r1 = r0.next();
        r1 = (java.io.File) r1;
        r2 = 0;
        r3 = r4.size();
        if (r2 >= r3) goto L_0x0176;
        r3 = r4.get(r2);
        r3 = (org.osmdroid.tileprovider.util.StorageUtils.StorageInfo) r3;
        r3 = r3.path;
        r5 = r1.getAbsolutePath();
        r3 = r3.equals(r5);
        if (r3 == 0) goto L_0x0173;
        r2 = 1;
        goto L_0x0177;
        r2 = r2 + 1;
        goto L_0x0159;
        r2 = 0;
        if (r2 != 0) goto L_0x014c;
        r2 = new org.osmdroid.tileprovider.util.StorageUtils$StorageInfo;
        r1 = r1.getAbsolutePath();
        r2.<init>(r1, r7, r7, r10);
        r4.add(r2);
        goto L_0x014c;
        return r4;
        if (r12 == 0) goto L_0x018c;
        r12.close();	 Catch:{ IOException -> 0x018c }
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.util.StorageUtils.getStorageList():java.util.List");
    }

    public static File getStorage() {
        List storageList = getStorageList();
        StorageInfo storageInfo = null;
        for (int i = 0; i < storageList.size(); i++) {
            StorageInfo storageInfo2 = (StorageInfo) storageList.get(i);
            if (!storageInfo2.readonly && isWritable(new File(storageInfo2.path)) && (storageInfo == null || storageInfo.freeSpace < storageInfo2.freeSpace)) {
                storageInfo = storageInfo2;
            }
        }
        if (storageInfo != null) {
            return new File(storageInfo.path);
        }
        try {
            return Environment.getExternalStorageDirectory();
        } catch (Exception unused) {
            return null;
        }
    }

    public static boolean isWritable(File file) {
        String str = "StorageUtils";
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(file.getAbsolutePath());
            stringBuilder.append(File.separator);
            stringBuilder.append("osm.tmp");
            File file2 = new File(stringBuilder.toString());
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            fileOutputStream.write("hi".getBytes());
            fileOutputStream.close();
            stringBuilder = new StringBuilder();
            stringBuilder.append(file.getAbsolutePath());
            stringBuilder.append(" is writable");
            Log.i(str, stringBuilder.toString());
            file2.delete();
            return true;
        } catch (Throwable unused) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(file.getAbsolutePath());
            stringBuilder2.append(" is NOT writable");
            Log.i(str, stringBuilder2.toString());
            return false;
        }
    }
}
