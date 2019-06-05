package org.mozilla.focus.download;

import android.os.AsyncTask;

public class GetImgHeaderTask extends AsyncTask<String, Void, String> {
    public Callback callback;

    public interface Callback {
        void setMIMEType(String str);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    /* Access modifiers changed, original: protected|varargs */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0053 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0052 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x005a  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0052 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0053 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0037 A:{ExcHandler: all (th java.lang.Throwable), Splitter:B:3:0x0016} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0053 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0052 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0037 A:{ExcHandler: all (th java.lang.Throwable), Splitter:B:3:0x0016} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0052 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0053 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x005a  */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:13:0x0032, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:14:0x0033, code skipped:
            r4 = r0;
            r0 = r6;
            r6 = r4;
     */
    /* JADX WARNING: Missing block: B:15:0x0037, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:16:0x0039, code skipped:
            r3 = move-exception;
     */
    /* JADX WARNING: Missing block: B:17:0x003a, code skipped:
            r4 = r0;
            r0 = r6;
            r6 = r3;
            r3 = r4;
     */
    /* JADX WARNING: Missing block: B:25:0x004a, code skipped:
            r0.disconnect();
     */
    public java.lang.String doInBackground(java.lang.String... r6) {
        /*
        r5 = this;
        r0 = 10001; // 0x2711 float:1.4014E-41 double:4.941E-320;
        android.net.TrafficStats.setThreadStatsTag(r0);
        r0 = "";
        r1 = 0;
        r2 = 0;
        r3 = new java.net.URL;	 Catch:{ IOException -> 0x0042, all -> 0x003f }
        r6 = r6[r1];	 Catch:{ IOException -> 0x0042, all -> 0x003f }
        r3.<init>(r6);	 Catch:{ IOException -> 0x0042, all -> 0x003f }
        r6 = r3.openConnection();	 Catch:{ IOException -> 0x0042, all -> 0x003f }
        r6 = (java.net.HttpURLConnection) r6;	 Catch:{ IOException -> 0x0042, all -> 0x003f }
        r3 = "HEAD";
        r6.setRequestMethod(r3);	 Catch:{ IOException -> 0x0039, all -> 0x0037 }
        r3 = r6.getContentType();	 Catch:{ IOException -> 0x0039, all -> 0x0037 }
        r0 = r6.getResponseCode();	 Catch:{ IOException -> 0x0032, all -> 0x0037 }
        r6.disconnect();	 Catch:{ IOException -> 0x002c, all -> 0x0037 }
        if (r6 == 0) goto L_0x004e;
    L_0x0028:
        r6.disconnect();
        goto L_0x004e;
    L_0x002c:
        r1 = move-exception;
        r4 = r0;
        r0 = r6;
        r6 = r1;
        r1 = r4;
        goto L_0x0045;
    L_0x0032:
        r0 = move-exception;
        r4 = r0;
        r0 = r6;
        r6 = r4;
        goto L_0x0045;
    L_0x0037:
        r0 = move-exception;
        goto L_0x0058;
    L_0x0039:
        r3 = move-exception;
        r4 = r0;
        r0 = r6;
        r6 = r3;
        r3 = r4;
        goto L_0x0045;
    L_0x003f:
        r0 = move-exception;
        r6 = r2;
        goto L_0x0058;
    L_0x0042:
        r6 = move-exception;
        r3 = r0;
        r0 = r2;
    L_0x0045:
        r6.printStackTrace();	 Catch:{ all -> 0x0054 }
        if (r0 == 0) goto L_0x004d;
    L_0x004a:
        r0.disconnect();
    L_0x004d:
        r0 = r1;
    L_0x004e:
        r6 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r0 != r6) goto L_0x0053;
    L_0x0052:
        return r3;
    L_0x0053:
        return r2;
    L_0x0054:
        r6 = move-exception;
        r4 = r0;
        r0 = r6;
        r6 = r4;
    L_0x0058:
        if (r6 == 0) goto L_0x005d;
    L_0x005a:
        r6.disconnect();
    L_0x005d:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.focus.download.GetImgHeaderTask.doInBackground(java.lang.String[]):java.lang.String");
    }

    /* Access modifiers changed, original: protected */
    public void onPostExecute(String str) {
        super.onPostExecute(str);
        this.callback.setMIMEType(str);
    }
}
