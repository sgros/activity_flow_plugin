package org.osmdroid.tileprovider.modules;

import android.util.Log;
import org.osmdroid.config.Configuration;

public class TileDownloader {
    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:241:0x04b8 in {2, 5, 6, 8, 12, 17, 22, 23, 24, 28, 29, 34, 46, 58, 59, 62, 63, 64, 68, 69, 70, 74, 78, 79, 80, 84, 88, 101, 105, 106, 107, 109, 111, 113, 115, 117, 119, 121, 123, 125, 127, 129, 131, 133, 134, 136, 137, 139, 140, 142, 143, 145, 146, 148, 149, 151, 153, 154, 156, 157, 159, 160, 162, 164, 165, 167, 168, 169, 171, 172, 174, 175, 177, 178, 180, 181, 182, 184, 185, 187, 189, 191, 193, 195, 197, 199, 201, 204, 207, 209, 211, 213, 214, 216, 219, 221, 223, 226, 228, 230, 233, 234, 235, 238, 239, 240} preds:[]
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
    public android.graphics.drawable.Drawable downloadTile(long r21, int r23, java.lang.String r24, org.osmdroid.tileprovider.modules.IFilesystemCache r25, org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase r26) throws org.osmdroid.tileprovider.modules.CantContinueException {
        /*
        r20 = this;
        r1 = r23;
        r2 = r24;
        r3 = "https://";
        r4 = " : ";
        r5 = 0;
        r6 = 3;
        if (r1 <= r6) goto L_0x000d;
        return r5;
        r6 = r26.getTileSourcePolicy();
        r6 = r6.normalizesUserAgent();
        if (r6 == 0) goto L_0x0020;
        r6 = org.osmdroid.config.Configuration.getInstance();
        r6 = r6.getNormalizedUserAgent();
        goto L_0x0021;
        r6 = r5;
        if (r6 != 0) goto L_0x002b;
        r6 = org.osmdroid.config.Configuration.getInstance();
        r6 = r6.getUserAgentValue();
        r7 = r26.getTileSourcePolicy();
        r7 = r7.acceptsUserAgent(r6);
        r8 = "OsmDroid";
        if (r7 != 0) goto L_0x004c;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Please configure a relevant user agent; current value is: ";
        r1.append(r2);
        r1.append(r6);
        r1 = r1.toString();
        android.util.Log.e(r8, r1);
        return r5;
        r7 = 1;
        r9 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        r9 = r9.isDebugMode();	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        if (r9 == 0) goto L_0x006b;	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        r9 = new java.lang.StringBuilder;	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        r9.<init>();	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        r10 = "Downloading Maptile from url: ";	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        r9.append(r10);	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        r9.append(r2);	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        r9 = r9.toString();	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        android.util.Log.d(r8, r9);	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        r9 = android.text.TextUtils.isEmpty(r24);	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        if (r9 == 0) goto L_0x0081;
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r5);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r5);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r5);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r5);
        r5.disconnect();	 Catch:{ Exception -> 0x0080 }
        return r5;
        r9 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        r9 = r9.getHttpProxy();	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        if (r9 == 0) goto L_0x009f;	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        r9 = new java.net.URL;	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        r9.<init>(r2);	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        r10 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        r10 = r10.getHttpProxy();	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        r9 = r9.openConnection(r10);	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        r9 = (java.net.HttpURLConnection) r9;	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        goto L_0x00aa;	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        r9 = new java.net.URL;	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        r9.<init>(r2);	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        r9 = r9.openConnection();	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        r9 = (java.net.HttpURLConnection) r9;	 Catch:{ UnknownHostException -> 0x0479, LowMemoryException -> 0x0444, FileNotFoundException -> 0x0412, IOException -> 0x03d9, Throwable -> 0x039c, all -> 0x0392 }
        r9.setUseCaches(r7);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r10 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r10 = r10.getUserAgentHttpHeader();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r9.setRequestProperty(r10, r6);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r6 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r6 = r6.getAdditionalHttpRequestProperties();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r6 = r6.entrySet();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r6 = r6.iterator();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r10 = r6.hasNext();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        if (r10 == 0) goto L_0x00e4;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r10 = r6.next();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r10 = (java.util.Map.Entry) r10;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r11 = r10.getKey();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r11 = (java.lang.String) r11;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r10 = r10.getValue();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r10 = (java.lang.String) r10;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r9.setRequestProperty(r11, r10);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        goto L_0x00c8;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r9.connect();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r6 = r9.getResponseCode();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r10 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        if (r6 == r10) goto L_0x01fb;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r6 = r9.getResponseCode();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r10 = 301; // 0x12d float:4.22E-43 double:1.487E-321;
        r11 = " HTTP response: ";
        if (r6 == r10) goto L_0x0107;
        r10 = 302; // 0x12e float:4.23E-43 double:1.49E-321;
        if (r6 == r10) goto L_0x0107;
        r10 = 307; // 0x133 float:4.3E-43 double:1.517E-321;
        if (r6 == r10) goto L_0x0107;
        r10 = 308; // 0x134 float:4.32E-43 double:1.52E-321;
        if (r6 == r10) goto L_0x0107;
        goto L_0x01b3;
        r6 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r6 = r6.isMapTileDownloaderFollowRedirects();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        if (r6 == 0) goto L_0x01b3;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r6 = "Location";	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r6 = r9.getHeaderField(r6);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        if (r6 == 0) goto L_0x01fb;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r10 = "/";	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r10 = r6.startsWith(r10);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        if (r10 == 0) goto L_0x0169;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r10 = new java.net.URL;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r10.<init>(r2);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r12 = r10.getPort();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r13 = r24.toLowerCase();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r13 = r13.startsWith(r3);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r14 = -1;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        if (r12 != r14) goto L_0x0146;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r2 = r24.toLowerCase();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r12 = "http://";	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r2 = r2.startsWith(r12);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        if (r2 == 0) goto L_0x0144;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r12 = 80;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        goto L_0x0146;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r12 = 443; // 0x1bb float:6.21E-43 double:2.19E-321;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r2 = new java.lang.StringBuilder;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r2.<init>();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        if (r13 == 0) goto L_0x014e;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        goto L_0x0150;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3 = "http";	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r2.append(r3);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3 = r10.getHost();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r2.append(r3);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3 = ":";	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r2.append(r3);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r2.append(r12);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r2.append(r6);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r6 = r2.toString();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r2 = new java.lang.StringBuilder;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r2.<init>();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3 = "Http redirect for MapTile: ";	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r2.append(r3);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3 = org.osmdroid.util.MapTileIndex.toString(r21);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r2.append(r3);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r2.append(r11);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3 = r9.getResponseMessage();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r2.append(r3);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3 = " to url ";	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r2.append(r3);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r2.append(r6);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r2 = r2.toString();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        android.util.Log.i(r8, r2);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r15 = r1 + 1;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r12 = r20;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r13 = r21;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r16 = r6;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r17 = r25;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r18 = r26;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r1 = r12.downloadTile(r13, r15, r16, r17, r18);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r5);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r5);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r5);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r5);
        r9.disconnect();	 Catch:{ Exception -> 0x01b2 }
        return r1;
        r1 = new java.lang.StringBuilder;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r1.<init>();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3 = "Problem downloading MapTile: ";	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r1.append(r3);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3 = org.osmdroid.util.MapTileIndex.toString(r21);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r1.append(r3);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r1.append(r11);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3 = r9.getResponseMessage();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r1.append(r3);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r1 = r1.toString();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        android.util.Log.w(r8, r1);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r1 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r1 = r1.isDebugMapTileDownloader();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        if (r1 == 0) goto L_0x01e2;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        android.util.Log.d(r8, r2);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r1 = org.osmdroid.tileprovider.util.Counters.tileDownloadErrors;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r1 = r1 + r7;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        org.osmdroid.tileprovider.util.Counters.tileDownloadErrors = r1;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r1 = r9.getErrorStream();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r1);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r5);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r5);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r5);
        r9.disconnect();	 Catch:{ Exception -> 0x01fa }
        return r5;
        r1 = "Content-Type";	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r1 = r9.getHeaderField(r1);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3 = r3.isDebugMapTileDownloader();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        if (r3 == 0) goto L_0x0222;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3 = new java.lang.StringBuilder;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3.<init>();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3.append(r2);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r6 = " success, mime is ";	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3.append(r6);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3.append(r1);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3 = r3.toString();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        android.util.Log.d(r8, r3);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        if (r1 == 0) goto L_0x0247;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3 = r1.toLowerCase();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r6 = "image";	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3 = r3.contains(r6);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        if (r3 != 0) goto L_0x0247;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3 = new java.lang.StringBuilder;	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3.<init>();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3.append(r2);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r2 = " success, however the mime type does not appear to be an image ";	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3.append(r2);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r3.append(r1);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r1 = r3.toString();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        android.util.Log.w(r8, r1);	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r1 = r9.getInputStream();	 Catch:{ UnknownHostException -> 0x0388, LowMemoryException -> 0x037f, FileNotFoundException -> 0x0375, IOException -> 0x036b, Throwable -> 0x0361, all -> 0x0358 }
        r2 = new java.io.ByteArrayOutputStream;	 Catch:{ UnknownHostException -> 0x034e, LowMemoryException -> 0x0344, FileNotFoundException -> 0x033a, IOException -> 0x0330, Throwable -> 0x0326, all -> 0x031c }
        r2.<init>();	 Catch:{ UnknownHostException -> 0x034e, LowMemoryException -> 0x0344, FileNotFoundException -> 0x033a, IOException -> 0x0330, Throwable -> 0x0326, all -> 0x031c }
        r3 = new java.io.BufferedOutputStream;	 Catch:{ UnknownHostException -> 0x0313, LowMemoryException -> 0x030e, FileNotFoundException -> 0x0306, IOException -> 0x02fe, Throwable -> 0x02f6, all -> 0x02ef }
        r6 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;	 Catch:{ UnknownHostException -> 0x0313, LowMemoryException -> 0x030e, FileNotFoundException -> 0x0306, IOException -> 0x02fe, Throwable -> 0x02f6, all -> 0x02ef }
        r3.<init>(r2, r6);	 Catch:{ UnknownHostException -> 0x0313, LowMemoryException -> 0x030e, FileNotFoundException -> 0x0306, IOException -> 0x02fe, Throwable -> 0x02f6, all -> 0x02ef }
        r6 = "Expires";	 Catch:{ UnknownHostException -> 0x02e9, LowMemoryException -> 0x02e3, FileNotFoundException -> 0x02dd, IOException -> 0x02d7, Throwable -> 0x02d1, all -> 0x02ca }
        r6 = r9.getHeaderField(r6);	 Catch:{ UnknownHostException -> 0x02e9, LowMemoryException -> 0x02e3, FileNotFoundException -> 0x02dd, IOException -> 0x02d7, Throwable -> 0x02d1, all -> 0x02ca }
        r10 = "Cache-Control";	 Catch:{ UnknownHostException -> 0x02e9, LowMemoryException -> 0x02e3, FileNotFoundException -> 0x02dd, IOException -> 0x02d7, Throwable -> 0x02d1, all -> 0x02ca }
        r10 = r9.getHeaderField(r10);	 Catch:{ UnknownHostException -> 0x02e9, LowMemoryException -> 0x02e3, FileNotFoundException -> 0x02dd, IOException -> 0x02d7, Throwable -> 0x02d1, all -> 0x02ca }
        r11 = java.lang.System.currentTimeMillis();	 Catch:{ UnknownHostException -> 0x02e9, LowMemoryException -> 0x02e3, FileNotFoundException -> 0x02dd, IOException -> 0x02d7, Throwable -> 0x02d1, all -> 0x02ca }
        r13 = r20;
        r10 = r13.computeExpirationTime(r6, r10, r11);	 Catch:{ UnknownHostException -> 0x02c8, LowMemoryException -> 0x02c6, FileNotFoundException -> 0x02c4, IOException -> 0x02c2, Throwable -> 0x02c0, all -> 0x02be }
        org.osmdroid.tileprovider.util.StreamUtils.copy(r1, r3);	 Catch:{ UnknownHostException -> 0x02c8, LowMemoryException -> 0x02c6, FileNotFoundException -> 0x02c4, IOException -> 0x02c2, Throwable -> 0x02c0, all -> 0x02be }
        r3.flush();	 Catch:{ UnknownHostException -> 0x02c8, LowMemoryException -> 0x02c6, FileNotFoundException -> 0x02c4, IOException -> 0x02c2, Throwable -> 0x02c0, all -> 0x02be }
        r6 = r2.toByteArray();	 Catch:{ UnknownHostException -> 0x02c8, LowMemoryException -> 0x02c6, FileNotFoundException -> 0x02c4, IOException -> 0x02c2, Throwable -> 0x02c0, all -> 0x02be }
        r12 = new java.io.ByteArrayInputStream;	 Catch:{ UnknownHostException -> 0x02c8, LowMemoryException -> 0x02c6, FileNotFoundException -> 0x02c4, IOException -> 0x02c2, Throwable -> 0x02c0, all -> 0x02be }
        r12.<init>(r6);	 Catch:{ UnknownHostException -> 0x02c8, LowMemoryException -> 0x02c6, FileNotFoundException -> 0x02c4, IOException -> 0x02c2, Throwable -> 0x02c0, all -> 0x02be }
        if (r25 == 0) goto L_0x0290;
        r19 = java.lang.Long.valueOf(r10);	 Catch:{ UnknownHostException -> 0x02ba, LowMemoryException -> 0x02b7, FileNotFoundException -> 0x02b3, IOException -> 0x02af, Throwable -> 0x02ab, all -> 0x02a6 }
        r14 = r25;	 Catch:{ UnknownHostException -> 0x02ba, LowMemoryException -> 0x02b7, FileNotFoundException -> 0x02b3, IOException -> 0x02af, Throwable -> 0x02ab, all -> 0x02a6 }
        r15 = r26;	 Catch:{ UnknownHostException -> 0x02ba, LowMemoryException -> 0x02b7, FileNotFoundException -> 0x02b3, IOException -> 0x02af, Throwable -> 0x02ab, all -> 0x02a6 }
        r16 = r21;	 Catch:{ UnknownHostException -> 0x02ba, LowMemoryException -> 0x02b7, FileNotFoundException -> 0x02b3, IOException -> 0x02af, Throwable -> 0x02ab, all -> 0x02a6 }
        r18 = r12;	 Catch:{ UnknownHostException -> 0x02ba, LowMemoryException -> 0x02b7, FileNotFoundException -> 0x02b3, IOException -> 0x02af, Throwable -> 0x02ab, all -> 0x02a6 }
        r14.saveFile(r15, r16, r18, r19);	 Catch:{ UnknownHostException -> 0x02ba, LowMemoryException -> 0x02b7, FileNotFoundException -> 0x02b3, IOException -> 0x02af, Throwable -> 0x02ab, all -> 0x02a6 }
        r12.reset();	 Catch:{ UnknownHostException -> 0x02ba, LowMemoryException -> 0x02b7, FileNotFoundException -> 0x02b3, IOException -> 0x02af, Throwable -> 0x02ab, all -> 0x02a6 }
        r6 = r26;	 Catch:{ UnknownHostException -> 0x02ba, LowMemoryException -> 0x02b7, FileNotFoundException -> 0x02b3, IOException -> 0x02af, Throwable -> 0x02ab, all -> 0x02a6 }
        r4 = r6.getDrawable(r12);	 Catch:{ UnknownHostException -> 0x02ba, LowMemoryException -> 0x02b7, FileNotFoundException -> 0x02b3, IOException -> 0x02af, Throwable -> 0x02ab, all -> 0x02a6 }
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r1);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r3);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r12);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r2);
        r9.disconnect();	 Catch:{ Exception -> 0x02a5 }
        return r4;
        r0 = move-exception;
        r5 = r1;
        r6 = r2;
        goto L_0x0323;
        r0 = move-exception;
        r4 = r2;
        goto L_0x02fc;
        r0 = move-exception;
        r6 = r2;
        goto L_0x0304;
        r0 = move-exception;
        r6 = r2;
        goto L_0x030c;
        r0 = move-exception;
        goto L_0x034a;
        r0 = move-exception;
        r6 = r2;
        goto L_0x0319;
        r0 = move-exception;
        goto L_0x02cd;
        r0 = move-exception;
        goto L_0x02d4;
        r0 = move-exception;
        goto L_0x02da;
        r0 = move-exception;
        goto L_0x02e0;
        r0 = move-exception;
        goto L_0x02e6;
        r0 = move-exception;
        goto L_0x02ec;
        r0 = move-exception;
        r13 = r20;
        r6 = r2;
        r12 = r5;
        goto L_0x0322;
        r0 = move-exception;
        r13 = r20;
        r4 = r2;
        r12 = r5;
        goto L_0x02fc;
        r0 = move-exception;
        r13 = r20;
        r6 = r2;
        r12 = r5;
        goto L_0x0304;
        r0 = move-exception;
        r13 = r20;
        r6 = r2;
        r12 = r5;
        goto L_0x030c;
        r0 = move-exception;
        r13 = r20;
        r12 = r5;
        goto L_0x034a;
        r0 = move-exception;
        r13 = r20;
        r6 = r2;
        r12 = r5;
        goto L_0x0319;
        r0 = move-exception;
        r13 = r20;
        r6 = r2;
        r3 = r5;
        r12 = r3;
        goto L_0x0322;
        r0 = move-exception;
        r13 = r20;
        r4 = r2;
        r3 = r5;
        r12 = r3;
        r2 = r1;
        goto L_0x032d;
        r0 = move-exception;
        r13 = r20;
        r6 = r2;
        r3 = r5;
        r12 = r3;
        r2 = r1;
        goto L_0x0337;
        r0 = move-exception;
        r13 = r20;
        r6 = r2;
        r3 = r5;
        r12 = r3;
        r2 = r1;
        goto L_0x0341;
        r0 = move-exception;
        r13 = r20;
        r3 = r5;
        goto L_0x0349;
        r0 = move-exception;
        r13 = r20;
        r6 = r2;
        r3 = r5;
        r12 = r3;
        r2 = r1;
        goto L_0x0355;
        r0 = move-exception;
        r13 = r20;
        r3 = r5;
        r6 = r3;
        r12 = r6;
        r5 = r1;
        r1 = r0;
        goto L_0x04a8;
        r0 = move-exception;
        r13 = r20;
        r2 = r1;
        r3 = r5;
        r4 = r3;
        r12 = r4;
        r1 = r0;
        goto L_0x03a5;
        r0 = move-exception;
        r13 = r20;
        r2 = r1;
        r3 = r5;
        r6 = r3;
        r12 = r6;
        r1 = r0;
        goto L_0x03e2;
        r0 = move-exception;
        r13 = r20;
        r2 = r1;
        r3 = r5;
        r6 = r3;
        r12 = r6;
        r1 = r0;
        goto L_0x041b;
        r0 = move-exception;
        r13 = r20;
        r2 = r5;
        r3 = r2;
        r12 = r3;
        r5 = r1;
        r1 = r0;
        goto L_0x044c;
        r0 = move-exception;
        r13 = r20;
        r2 = r1;
        r3 = r5;
        r6 = r3;
        r12 = r6;
        r1 = r0;
        goto L_0x0482;
        r0 = move-exception;
        r13 = r20;
        r1 = r0;
        r3 = r5;
        r6 = r3;
        r12 = r6;
        goto L_0x04a8;
        r0 = move-exception;
        r13 = r20;
        r1 = r0;
        r2 = r5;
        r3 = r2;
        r4 = r3;
        r12 = r4;
        goto L_0x03a5;
        r0 = move-exception;
        r13 = r20;
        r1 = r0;
        r2 = r5;
        r3 = r2;
        r6 = r3;
        r12 = r6;
        goto L_0x03e2;
        r0 = move-exception;
        r13 = r20;
        r1 = r0;
        r2 = r5;
        r3 = r2;
        r6 = r3;
        r12 = r6;
        goto L_0x041b;
        r0 = move-exception;
        r13 = r20;
        r1 = r0;
        r2 = r5;
        r3 = r2;
        r12 = r3;
        goto L_0x044c;
        r0 = move-exception;
        r13 = r20;
        r1 = r0;
        r2 = r5;
        r3 = r2;
        r6 = r3;
        r12 = r6;
        goto L_0x0482;
        r0 = move-exception;
        r13 = r20;
        r1 = r0;
        r3 = r5;
        r6 = r3;
        r9 = r6;
        r12 = r9;
        goto L_0x04a8;
        r0 = move-exception;
        r13 = r20;
        r1 = r0;
        r2 = r5;
        r3 = r2;
        r4 = r3;
        r9 = r4;
        r12 = r9;
        r6 = org.osmdroid.tileprovider.util.Counters.tileDownloadErrors;	 Catch:{ all -> 0x03d3 }
        r6 = r6 + r7;	 Catch:{ all -> 0x03d3 }
        org.osmdroid.tileprovider.util.Counters.tileDownloadErrors = r6;	 Catch:{ all -> 0x03d3 }
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x03d3 }
        r6.<init>();	 Catch:{ all -> 0x03d3 }
        r7 = "Error downloading MapTile: ";	 Catch:{ all -> 0x03d3 }
        r6.append(r7);	 Catch:{ all -> 0x03d3 }
        r7 = org.osmdroid.util.MapTileIndex.toString(r21);	 Catch:{ all -> 0x03d3 }
        r6.append(r7);	 Catch:{ all -> 0x03d3 }
        r6 = r6.toString();	 Catch:{ all -> 0x03d3 }
        android.util.Log.e(r8, r6, r1);	 Catch:{ all -> 0x03d3 }
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r2);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r3);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r12);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r4);
        r9.disconnect();	 Catch:{ Exception -> 0x04a7 }
        goto L_0x04a7;
        r0 = move-exception;
        r1 = r0;
        r5 = r2;
        r6 = r4;
        goto L_0x04a8;
        r0 = move-exception;
        r13 = r20;
        r1 = r0;
        r2 = r5;
        r3 = r2;
        r6 = r3;
        r9 = r6;
        r12 = r9;
        r10 = org.osmdroid.tileprovider.util.Counters.tileDownloadErrors;	 Catch:{ all -> 0x043f }
        r10 = r10 + r7;	 Catch:{ all -> 0x043f }
        org.osmdroid.tileprovider.util.Counters.tileDownloadErrors = r10;	 Catch:{ all -> 0x043f }
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x043f }
        r7.<init>();	 Catch:{ all -> 0x043f }
        r10 = "IOException downloading MapTile: ";	 Catch:{ all -> 0x043f }
        r7.append(r10);	 Catch:{ all -> 0x043f }
        r10 = org.osmdroid.util.MapTileIndex.toString(r21);	 Catch:{ all -> 0x043f }
        r7.append(r10);	 Catch:{ all -> 0x043f }
        r7.append(r4);	 Catch:{ all -> 0x043f }
        r7.append(r1);	 Catch:{ all -> 0x043f }
        r1 = r7.toString();	 Catch:{ all -> 0x043f }
        android.util.Log.w(r8, r1);	 Catch:{ all -> 0x043f }
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r2);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r3);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r12);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r6);
        goto L_0x03ce;
        r0 = move-exception;
        r13 = r20;
        r1 = r0;
        r2 = r5;
        r3 = r2;
        r6 = r3;
        r9 = r6;
        r12 = r9;
        r10 = org.osmdroid.tileprovider.util.Counters.tileDownloadErrors;	 Catch:{ all -> 0x043f }
        r10 = r10 + r7;	 Catch:{ all -> 0x043f }
        org.osmdroid.tileprovider.util.Counters.tileDownloadErrors = r10;	 Catch:{ all -> 0x043f }
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x043f }
        r7.<init>();	 Catch:{ all -> 0x043f }
        r10 = "Tile not found: ";	 Catch:{ all -> 0x043f }
        r7.append(r10);	 Catch:{ all -> 0x043f }
        r10 = org.osmdroid.util.MapTileIndex.toString(r21);	 Catch:{ all -> 0x043f }
        r7.append(r10);	 Catch:{ all -> 0x043f }
        r7.append(r4);	 Catch:{ all -> 0x043f }
        r7.append(r1);	 Catch:{ all -> 0x043f }
        r1 = r7.toString();	 Catch:{ all -> 0x043f }
        android.util.Log.w(r8, r1);	 Catch:{ all -> 0x043f }
        goto L_0x0405;
        r0 = move-exception;
        r1 = r0;
        r5 = r2;
        goto L_0x04a8;
        r0 = move-exception;
        r13 = r20;
        r1 = r0;
        r2 = r5;
        r3 = r2;
        r9 = r3;
        r12 = r9;
        r6 = org.osmdroid.tileprovider.util.Counters.countOOM;	 Catch:{ all -> 0x0475 }
        r6 = r6 + r7;	 Catch:{ all -> 0x0475 }
        org.osmdroid.tileprovider.util.Counters.countOOM = r6;	 Catch:{ all -> 0x0475 }
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0475 }
        r6.<init>();	 Catch:{ all -> 0x0475 }
        r7 = "LowMemoryException downloading MapTile: ";	 Catch:{ all -> 0x0475 }
        r6.append(r7);	 Catch:{ all -> 0x0475 }
        r7 = org.osmdroid.util.MapTileIndex.toString(r21);	 Catch:{ all -> 0x0475 }
        r6.append(r7);	 Catch:{ all -> 0x0475 }
        r6.append(r4);	 Catch:{ all -> 0x0475 }
        r6.append(r1);	 Catch:{ all -> 0x0475 }
        r4 = r6.toString();	 Catch:{ all -> 0x0475 }
        android.util.Log.w(r8, r4);	 Catch:{ all -> 0x0475 }
        r4 = new org.osmdroid.tileprovider.modules.CantContinueException;	 Catch:{ all -> 0x0475 }
        r4.<init>(r1);	 Catch:{ all -> 0x0475 }
        throw r4;	 Catch:{ all -> 0x0475 }
        r0 = move-exception;
        r1 = r0;
        r6 = r2;
        goto L_0x04a8;
        r0 = move-exception;
        r13 = r20;
        r1 = r0;
        r2 = r5;
        r3 = r2;
        r6 = r3;
        r9 = r6;
        r12 = r9;
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x043f }
        r10.<init>();	 Catch:{ all -> 0x043f }
        r11 = "UnknownHostException downloading MapTile: ";	 Catch:{ all -> 0x043f }
        r10.append(r11);	 Catch:{ all -> 0x043f }
        r11 = org.osmdroid.util.MapTileIndex.toString(r21);	 Catch:{ all -> 0x043f }
        r10.append(r11);	 Catch:{ all -> 0x043f }
        r10.append(r4);	 Catch:{ all -> 0x043f }
        r10.append(r1);	 Catch:{ all -> 0x043f }
        r1 = r10.toString();	 Catch:{ all -> 0x043f }
        android.util.Log.w(r8, r1);	 Catch:{ all -> 0x043f }
        r1 = org.osmdroid.tileprovider.util.Counters.tileDownloadErrors;	 Catch:{ all -> 0x043f }
        r1 = r1 + r7;	 Catch:{ all -> 0x043f }
        org.osmdroid.tileprovider.util.Counters.tileDownloadErrors = r1;	 Catch:{ all -> 0x043f }
        goto L_0x0405;
        return r5;
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r5);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r3);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r12);
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r6);
        r9.disconnect();	 Catch:{ Exception -> 0x04b7 }
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.TileDownloader.downloadTile(long, int, java.lang.String, org.osmdroid.tileprovider.modules.IFilesystemCache, org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase):android.graphics.drawable.Drawable");
    }

    public Long getHttpExpiresTime(String str) {
        if (str != null && str.length() > 0) {
            try {
                str = Long.valueOf(Configuration.getInstance().getHttpHeaderDateTimeFormat().parse(str).getTime());
                return str;
            } catch (Exception e) {
                if (Configuration.getInstance().isDebugMapTileDownloader()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to parse expiration tag for tile, server returned ");
                    stringBuilder.append(str);
                    Log.d("OsmDroid", stringBuilder.toString(), e);
                }
            }
        }
        return null;
    }

    public Long getHttpCacheControlDuration(String str) {
        if (str != null && str.length() > 0) {
            try {
                for (String str2 : str.split(", ")) {
                    if (str2.indexOf("max-age=") == 0) {
                        return Long.valueOf(str2.substring(8));
                    }
                }
            } catch (Exception e) {
                if (Configuration.getInstance().isDebugMapTileDownloader()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to parse cache control tag for tile, server returned ");
                    stringBuilder.append(str);
                    Log.d("OsmDroid", stringBuilder.toString(), e);
                }
            }
        }
        return null;
    }

    public long computeExpirationTime(String str, String str2, long j) {
        Long expirationOverrideDuration = Configuration.getInstance().getExpirationOverrideDuration();
        if (expirationOverrideDuration != null) {
            return j + expirationOverrideDuration.longValue();
        }
        long expirationExtendedDuration = Configuration.getInstance().getExpirationExtendedDuration();
        Long httpCacheControlDuration = getHttpCacheControlDuration(str2);
        if (httpCacheControlDuration != null) {
            return (j + (httpCacheControlDuration.longValue() * 1000)) + expirationExtendedDuration;
        }
        Long httpExpiresTime = getHttpExpiresTime(str);
        return httpExpiresTime != null ? httpExpiresTime.longValue() + expirationExtendedDuration : (j + 604800000) + expirationExtendedDuration;
    }
}
