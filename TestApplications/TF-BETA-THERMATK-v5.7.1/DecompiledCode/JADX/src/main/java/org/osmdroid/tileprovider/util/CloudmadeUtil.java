package org.osmdroid.tileprovider.util;

import android.content.SharedPreferences.Editor;

public class CloudmadeUtil {
    public static boolean DEBUGMODE = false;
    private static String mAndroidId = "android_id";
    private static String mKey = "";
    private static Editor mPreferenceEditor = null;
    private static String mToken = "";

    public static String getCloudmadeKey() {
        return mKey;
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:88:0x018d */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0163 A:{SYNTHETIC, Splitter:B:65:0x0163} */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x016a A:{SYNTHETIC, Splitter:B:69:0x016a} */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0171 A:{SYNTHETIC, Splitter:B:73:0x0171} */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x017c A:{SYNTHETIC, Splitter:B:78:0x017c} */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0183 A:{SYNTHETIC, Splitter:B:82:0x0183} */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x018a A:{SYNTHETIC, Splitter:B:86:0x018a} */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0163 A:{SYNTHETIC, Splitter:B:65:0x0163} */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x016a A:{SYNTHETIC, Splitter:B:69:0x016a} */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0171 A:{SYNTHETIC, Splitter:B:73:0x0171} */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x017c A:{SYNTHETIC, Splitter:B:78:0x017c} */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0183 A:{SYNTHETIC, Splitter:B:82:0x0183} */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x018a A:{SYNTHETIC, Splitter:B:86:0x018a} */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0163 A:{SYNTHETIC, Splitter:B:65:0x0163} */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x016a A:{SYNTHETIC, Splitter:B:69:0x016a} */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0171 A:{SYNTHETIC, Splitter:B:73:0x0171} */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x017c A:{SYNTHETIC, Splitter:B:78:0x017c} */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0183 A:{SYNTHETIC, Splitter:B:82:0x0183} */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x018a A:{SYNTHETIC, Splitter:B:86:0x018a} */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x017c A:{SYNTHETIC, Splitter:B:78:0x017c} */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0183 A:{SYNTHETIC, Splitter:B:82:0x0183} */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x018a A:{SYNTHETIC, Splitter:B:86:0x018a} */
    /* JADX WARNING: Can't wrap try/catch for region: R(7:75|76|(0)|(0)|(0)|88|89) */
    public static java.lang.String getCloudmadeToken() {
        /*
        r0 = mToken;
        r0 = r0.length();
        if (r0 != 0) goto L_0x0193;
    L_0x0008:
        r0 = mToken;
        monitor-enter(r0);
        r1 = mToken;	 Catch:{ all -> 0x0190 }
        r1 = r1.length();	 Catch:{ all -> 0x0190 }
        if (r1 != 0) goto L_0x018e;
    L_0x0013:
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0190 }
        r1.<init>();	 Catch:{ all -> 0x0190 }
        r2 = "http://auth.cloudmade.com/token/";
        r1.append(r2);	 Catch:{ all -> 0x0190 }
        r2 = mKey;	 Catch:{ all -> 0x0190 }
        r1.append(r2);	 Catch:{ all -> 0x0190 }
        r2 = "?userid=";
        r1.append(r2);	 Catch:{ all -> 0x0190 }
        r2 = mAndroidId;	 Catch:{ all -> 0x0190 }
        r1.append(r2);	 Catch:{ all -> 0x0190 }
        r1 = r1.toString();	 Catch:{ all -> 0x0190 }
        r2 = 0;
        r3 = new java.net.URL;	 Catch:{ IOException -> 0x0148, all -> 0x0144 }
        r3.<init>(r1);	 Catch:{ IOException -> 0x0148, all -> 0x0144 }
        r1 = r3.openConnection();	 Catch:{ IOException -> 0x0148, all -> 0x0144 }
        r1 = (java.net.HttpURLConnection) r1;	 Catch:{ IOException -> 0x0148, all -> 0x0144 }
        r3 = 1;
        r1.setDoOutput(r3);	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r3 = "POST";
        r1.setRequestMethod(r3);	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r3 = "Content-Type";
        r4 = "application/x-www-form-urlencoded";
        r1.setRequestProperty(r3, r4);	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r3 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r3 = r3.getUserAgentHttpHeader();	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r4 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r4 = r4.getUserAgentValue();	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r1.setRequestProperty(r3, r4);	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r3 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r3 = r3.getAdditionalHttpRequestProperties();	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r3 = r3.entrySet();	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r3 = r3.iterator();	 Catch:{ IOException -> 0x013f, all -> 0x013c }
    L_0x006f:
        r4 = r3.hasNext();	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        if (r4 == 0) goto L_0x008b;
    L_0x0075:
        r4 = r3.next();	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r4 = (java.util.Map.Entry) r4;	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r5 = r4.getKey();	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r5 = (java.lang.String) r5;	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r4 = r4.getValue();	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r4 = (java.lang.String) r4;	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r1.setRequestProperty(r5, r4);	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        goto L_0x006f;
    L_0x008b:
        r1.connect();	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r3 = DEBUGMODE;	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        if (r3 == 0) goto L_0x00ac;
    L_0x0092:
        r3 = "OsmDroid";
        r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r4.<init>();	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r5 = "Response from Cloudmade auth: ";
        r4.append(r5);	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r5 = r1.getResponseMessage();	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r4.append(r5);	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r4 = r4.toString();	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        android.util.Log.d(r3, r4);	 Catch:{ IOException -> 0x013f, all -> 0x013c }
    L_0x00ac:
        r3 = r1.getResponseCode();	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r4 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r3 != r4) goto L_0x0126;
    L_0x00b4:
        r3 = new java.io.InputStreamReader;	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r4 = r1.getInputStream();	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r5 = "UTF-8";
        r3.<init>(r4, r5);	 Catch:{ IOException -> 0x013f, all -> 0x013c }
        r4 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x011f, all -> 0x0119 }
        r5 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r4.<init>(r3, r5);	 Catch:{ IOException -> 0x011f, all -> 0x0119 }
        r5 = r4.readLine();	 Catch:{ IOException -> 0x0113, all -> 0x010c }
        r6 = DEBUGMODE;	 Catch:{ IOException -> 0x0113, all -> 0x010c }
        if (r6 == 0) goto L_0x00e4;
    L_0x00ce:
        r6 = "OsmDroid";
        r7 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0113, all -> 0x010c }
        r7.<init>();	 Catch:{ IOException -> 0x0113, all -> 0x010c }
        r8 = "First line from Cloudmade auth: ";
        r7.append(r8);	 Catch:{ IOException -> 0x0113, all -> 0x010c }
        r7.append(r5);	 Catch:{ IOException -> 0x0113, all -> 0x010c }
        r7 = r7.toString();	 Catch:{ IOException -> 0x0113, all -> 0x010c }
        android.util.Log.d(r6, r7);	 Catch:{ IOException -> 0x0113, all -> 0x010c }
    L_0x00e4:
        r5 = r5.trim();	 Catch:{ IOException -> 0x0113, all -> 0x010c }
        mToken = r5;	 Catch:{ IOException -> 0x0113, all -> 0x010c }
        r5 = mToken;	 Catch:{ IOException -> 0x0113, all -> 0x010c }
        r5 = r5.length();	 Catch:{ IOException -> 0x0113, all -> 0x010c }
        if (r5 <= 0) goto L_0x0103;
    L_0x00f2:
        r5 = mPreferenceEditor;	 Catch:{ IOException -> 0x0113, all -> 0x010c }
        r6 = "CLOUDMADE_TOKEN";
        r7 = mToken;	 Catch:{ IOException -> 0x0113, all -> 0x010c }
        r5.putString(r6, r7);	 Catch:{ IOException -> 0x0113, all -> 0x010c }
        r5 = mPreferenceEditor;	 Catch:{ IOException -> 0x0113, all -> 0x010c }
        r5.commit();	 Catch:{ IOException -> 0x0113, all -> 0x010c }
        mPreferenceEditor = r2;	 Catch:{ IOException -> 0x0113, all -> 0x010c }
        goto L_0x010a;
    L_0x0103:
        r2 = "OsmDroid";
        r5 = "No authorization token received from Cloudmade";
        android.util.Log.e(r2, r5);	 Catch:{ IOException -> 0x0113, all -> 0x010c }
    L_0x010a:
        r2 = r4;
        goto L_0x0127;
    L_0x010c:
        r2 = move-exception;
        r9 = r3;
        r3 = r2;
        r2 = r4;
        r4 = r9;
        goto L_0x017a;
    L_0x0113:
        r2 = move-exception;
        r9 = r2;
        r2 = r1;
        r1 = r3;
        r3 = r9;
        goto L_0x014b;
    L_0x0119:
        r4 = move-exception;
        r9 = r4;
        r4 = r3;
        r3 = r9;
        goto L_0x017a;
    L_0x011f:
        r4 = move-exception;
        r9 = r2;
        r2 = r1;
        r1 = r3;
        r3 = r4;
        r4 = r9;
        goto L_0x014b;
    L_0x0126:
        r3 = r2;
    L_0x0127:
        if (r1 == 0) goto L_0x012e;
    L_0x0129:
        r1.disconnect();	 Catch:{ Exception -> 0x012d }
        goto L_0x012e;
    L_0x012e:
        if (r2 == 0) goto L_0x0135;
    L_0x0130:
        r2.close();	 Catch:{ Exception -> 0x0134 }
        goto L_0x0135;
    L_0x0135:
        if (r3 == 0) goto L_0x018e;
    L_0x0137:
        r3.close();	 Catch:{ Exception -> 0x018e }
        goto L_0x018e;
    L_0x013c:
        r3 = move-exception;
        r4 = r2;
        goto L_0x017a;
    L_0x013f:
        r3 = move-exception;
        r4 = r2;
        r2 = r1;
        r1 = r4;
        goto L_0x014b;
    L_0x0144:
        r3 = move-exception;
        r1 = r2;
        r4 = r1;
        goto L_0x017a;
    L_0x0148:
        r3 = move-exception;
        r1 = r2;
        r4 = r1;
    L_0x014b:
        r5 = "OsmDroid";
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0175 }
        r6.<init>();	 Catch:{ all -> 0x0175 }
        r7 = "No authorization token received from Cloudmade: ";
        r6.append(r7);	 Catch:{ all -> 0x0175 }
        r6.append(r3);	 Catch:{ all -> 0x0175 }
        r3 = r6.toString();	 Catch:{ all -> 0x0175 }
        android.util.Log.e(r5, r3);	 Catch:{ all -> 0x0175 }
        if (r2 == 0) goto L_0x0168;
    L_0x0163:
        r2.disconnect();	 Catch:{ Exception -> 0x0167 }
        goto L_0x0168;
    L_0x0168:
        if (r4 == 0) goto L_0x016f;
    L_0x016a:
        r4.close();	 Catch:{ Exception -> 0x016e }
        goto L_0x016f;
    L_0x016f:
        if (r1 == 0) goto L_0x018e;
    L_0x0171:
        r1.close();	 Catch:{ Exception -> 0x018e }
        goto L_0x018e;
    L_0x0175:
        r3 = move-exception;
        r9 = r4;
        r4 = r1;
        r1 = r2;
        r2 = r9;
    L_0x017a:
        if (r1 == 0) goto L_0x0181;
    L_0x017c:
        r1.disconnect();	 Catch:{ Exception -> 0x0180 }
        goto L_0x0181;
    L_0x0181:
        if (r2 == 0) goto L_0x0188;
    L_0x0183:
        r2.close();	 Catch:{ Exception -> 0x0187 }
        goto L_0x0188;
    L_0x0188:
        if (r4 == 0) goto L_0x018d;
    L_0x018a:
        r4.close();	 Catch:{ Exception -> 0x018d }
    L_0x018d:
        throw r3;	 Catch:{ all -> 0x0190 }
    L_0x018e:
        monitor-exit(r0);	 Catch:{ all -> 0x0190 }
        goto L_0x0193;
    L_0x0190:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0190 }
        throw r1;
    L_0x0193:
        r0 = mToken;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.util.CloudmadeUtil.getCloudmadeToken():java.lang.String");
    }
}
