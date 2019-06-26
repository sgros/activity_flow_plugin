package menion.android.whereyougo.network;

import android.content.Context;
import android.os.AsyncTask;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import menion.android.whereyougo.utils.Logger;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadCartridgeTask extends AsyncTask<String, Progress, Boolean> {
    private static final String DOWNLOAD = "http://www.wherigo.com/cartridge/download.aspx";
    private static final String LOGIN = "https://www.wherigo.com/login/default.aspx";
    private static final String TAG = "DownloadCartridgeTask";
    private String errorMessage;
    private OkHttpClient httpClient;
    private final String password;
    private final String username;

    public class Progress {
        long completed;
        String message;
        final State state;
        final Task task;
        long total;

        public Progress(Task task, State state) {
            this.task = task;
            this.state = state;
        }

        public Progress(Task task, State state, String message) {
            this.task = task;
            this.state = state;
            this.message = message;
        }

        public Progress(Task task, long completed, long total) {
            this.state = State.WORKING;
            this.task = task;
            this.total = total;
            this.completed = completed;
        }

        public Task getTask() {
            return this.task;
        }

        public State getState() {
            return this.state;
        }

        public String getMessage() {
            return this.message;
        }

        public long getTotal() {
            return this.total;
        }

        public long getCompleted() {
            return this.completed;
        }
    }

    public enum State {
        WORKING,
        SUCCESS,
        FAIL
    }

    public enum Task {
        INIT,
        PING,
        LOGIN,
        DOWNLOAD,
        DOWNLOAD_SINGLE,
        LOGOUT
    }

    public DownloadCartridgeTask(Context context, String username, String password) {
        this.username = username;
        this.password = password;
    }

    /* Access modifiers changed, original: protected|varargs */
    public Boolean doInBackground(String... arg0) {
        boolean z = init() && ping() && login() && download(arg0) && logout();
        return Boolean.valueOf(z);
    }

    private boolean init() {
        try {
            System.setProperty("http.keepAlive", "false");
            this.httpClient = new Builder().sslSocketFactory(new TLSSocketFactory()).cookieJar(new NonPersistentCookieJar()).connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            Logger.m22e(TAG, "init()", e);
            this.errorMessage = e.getMessage();
        }
        if (this.httpClient == null) {
            publishProgress(new Progress[]{new Progress(Task.INIT, State.FAIL, this.errorMessage)});
        }
        if (this.httpClient != null) {
            return true;
        }
        return false;
    }

    private boolean ping() {
        return handleRequest(new Request.Builder().url(LOGIN).build(), Task.PING) != null;
    }

    private boolean login() {
        Request request = new Request.Builder().url(LOGIN).post(new FormBody.Builder().add("__EVENTTARGET", "").add("__EVENTARGUMENT", "").add("ctl00$ContentPlaceHolder1$Login1$Login1$UserName", this.username).add("ctl00$ContentPlaceHolder1$Login1$Login1$Password", this.password).add("ctl00$ContentPlaceHolder1$Login1$Login1$LoginButton", "Sign In").build()).build();
        publishProgress(new Progress[]{new Progress(Task.LOGIN, State.WORKING)});
        Response response = handleRequest(request);
        if (response == null || LOGIN.equals(response.request().url().toString())) {
            publishProgress(new Progress[]{new Progress(Task.LOGIN, State.FAIL, this.errorMessage)});
            return false;
        }
        publishProgress(new Progress[]{new Progress(Task.LOGIN, State.SUCCESS)});
        return true;
    }

    private boolean logout() {
        return handleRequest(new Request.Builder().url(LOGIN).post(new FormBody.Builder().add("__EVENTTARGET", "ctl00$ProfileWidget$LoginStatus1$ctl00").add("__EVENTARGUMENT", "").build()).build(), Task.LOGOUT) != null;
    }

    private boolean download(String[] cguid) {
        publishProgress(new Progress[]{new Progress(Task.DOWNLOAD, State.WORKING)});
        int i = 0;
        while (i < cguid.length) {
            if (download(cguid[i])) {
                Progress[] progressArr = new Progress[1];
                progressArr[0] = new Progress(Task.DOWNLOAD, (long) i, (long) cguid.length);
                publishProgress(progressArr);
                i++;
            } else {
                publishProgress(new Progress[]{new Progress(Task.DOWNLOAD, State.FAIL, this.errorMessage)});
                return false;
            }
        }
        return true;
    }

    private boolean download(String cguid) {
        Response response = handleRequest(new Request.Builder().url("http://www.wherigo.com/cartridge/download.aspx?CGUID=" + cguid).post(new FormBody.Builder().add("__EVENTTARGET", "").add("__EVENTARGUMENT", "").add("ctl00$ContentPlaceHolder1$uxDeviceList", "4").add("ctl00$ContentPlaceHolder1$btnDownload", "Download Now").build()).build());
        if (response != null) {
            if ("application/octet-stream".equals(response.body().contentType().toString())) {
                String filename;
                String contentDisposition = response.header("Content-Disposition", "");
                String pattern = "(?i)^ *attachment *; *filename *= *(.*) *$";
                if (contentDisposition.matches(pattern)) {
                    filename = cguid + "_" + contentDisposition.replaceFirst(pattern, "$1");
                } else {
                    filename = cguid + ".gwc";
                }
                return download(filename, response.body().byteStream(), Long.parseLong(response.header("Content-Length", "0")));
            }
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x00dd  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00bc  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00bc  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00dd  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00d9  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00dd  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00bc  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00d9  */
    private boolean download(java.lang.String r20, java.io.InputStream r21, long r22) {
        /*
        r19 = this;
        r15 = new java.io.File;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = menion.android.whereyougo.utils.FileSystem.ROOT;
        r3 = r3.append(r4);
        r0 = r20;
        r3 = r3.append(r0);
        r3 = r3.toString();
        r15.<init>(r3);
        r6 = 0;
        r3 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r13 = new byte[r3];
        r2 = 0;
        r11 = 0;
        r10 = new java.io.BufferedInputStream;	 Catch:{ IOException -> 0x00e6 }
        r0 = r21;
        r10.<init>(r0);	 Catch:{ IOException -> 0x00e6 }
        r12 = new java.io.BufferedOutputStream;	 Catch:{ IOException -> 0x00e8, all -> 0x00df }
        r3 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x00e8, all -> 0x00df }
        r3.<init>(r15);	 Catch:{ IOException -> 0x00e8, all -> 0x00df }
        r12.<init>(r3);	 Catch:{ IOException -> 0x00e8, all -> 0x00df }
        r3 = 1;
        r0 = new menion.android.whereyougo.network.DownloadCartridgeTask.Progress[r3];	 Catch:{ IOException -> 0x0080, all -> 0x00e2 }
        r17 = r0;
        r18 = 0;
        r3 = new menion.android.whereyougo.network.DownloadCartridgeTask$Progress;	 Catch:{ IOException -> 0x0080, all -> 0x00e2 }
        r5 = menion.android.whereyougo.network.DownloadCartridgeTask.Task.DOWNLOAD_SINGLE;	 Catch:{ IOException -> 0x0080, all -> 0x00e2 }
        r4 = r19;
        r8 = r22;
        r3.<init>(r5, r6, r8);	 Catch:{ IOException -> 0x0080, all -> 0x00e2 }
        r17[r18] = r3;	 Catch:{ IOException -> 0x0080, all -> 0x00e2 }
        r0 = r19;
        r1 = r17;
        r0.publishProgress(r1);	 Catch:{ IOException -> 0x0080, all -> 0x00e2 }
    L_0x004e:
        r16 = r10.read(r13);	 Catch:{ IOException -> 0x0080, all -> 0x00e2 }
        if (r16 <= 0) goto L_0x00be;
    L_0x0054:
        r3 = r19.isCancelled();	 Catch:{ IOException -> 0x0080, all -> 0x00e2 }
        if (r3 != 0) goto L_0x00be;
    L_0x005a:
        r3 = 0;
        r0 = r16;
        r12.write(r13, r3, r0);	 Catch:{ IOException -> 0x0080, all -> 0x00e2 }
        r0 = r16;
        r4 = (long) r0;	 Catch:{ IOException -> 0x0080, all -> 0x00e2 }
        r6 = r6 + r4;
        r3 = 1;
        r0 = new menion.android.whereyougo.network.DownloadCartridgeTask.Progress[r3];	 Catch:{ IOException -> 0x0080, all -> 0x00e2 }
        r17 = r0;
        r18 = 0;
        r3 = new menion.android.whereyougo.network.DownloadCartridgeTask$Progress;	 Catch:{ IOException -> 0x0080, all -> 0x00e2 }
        r5 = menion.android.whereyougo.network.DownloadCartridgeTask.Task.DOWNLOAD_SINGLE;	 Catch:{ IOException -> 0x0080, all -> 0x00e2 }
        r4 = r19;
        r8 = r22;
        r3.<init>(r5, r6, r8);	 Catch:{ IOException -> 0x0080, all -> 0x00e2 }
        r17[r18] = r3;	 Catch:{ IOException -> 0x0080, all -> 0x00e2 }
        r0 = r19;
        r1 = r17;
        r0.publishProgress(r1);	 Catch:{ IOException -> 0x0080, all -> 0x00e2 }
        goto L_0x004e;
    L_0x0080:
        r14 = move-exception;
        r11 = r12;
        r2 = r10;
    L_0x0083:
        r3 = "DownloadCartridgeTask";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ce }
        r4.<init>();	 Catch:{ all -> 0x00ce }
        r5 = "download(";
        r4 = r4.append(r5);	 Catch:{ all -> 0x00ce }
        r0 = r20;
        r4 = r4.append(r0);	 Catch:{ all -> 0x00ce }
        r5 = ")";
        r4 = r4.append(r5);	 Catch:{ all -> 0x00ce }
        r4 = r4.toString();	 Catch:{ all -> 0x00ce }
        menion.android.whereyougo.utils.Logger.m22e(r3, r4, r14);	 Catch:{ all -> 0x00ce }
        r3 = r14.getMessage();	 Catch:{ all -> 0x00ce }
        r0 = r19;
        r0.errorMessage = r3;	 Catch:{ all -> 0x00ce }
        menion.android.whereyougo.utils.Utils.closeStream(r2);
        menion.android.whereyougo.utils.Utils.closeStream(r11);
        r3 = (r6 > r22 ? 1 : (r6 == r22 ? 0 : -1));
        if (r3 == 0) goto L_0x00b8;
    L_0x00b5:
        r15.delete();
    L_0x00b8:
        r3 = (r6 > r22 ? 1 : (r6 == r22 ? 0 : -1));
        if (r3 != 0) goto L_0x00dd;
    L_0x00bc:
        r3 = 1;
    L_0x00bd:
        return r3;
    L_0x00be:
        menion.android.whereyougo.utils.Utils.closeStream(r10);
        menion.android.whereyougo.utils.Utils.closeStream(r12);
        r3 = (r6 > r22 ? 1 : (r6 == r22 ? 0 : -1));
        if (r3 == 0) goto L_0x00eb;
    L_0x00c8:
        r15.delete();
        r11 = r12;
        r2 = r10;
        goto L_0x00b8;
    L_0x00ce:
        r3 = move-exception;
    L_0x00cf:
        menion.android.whereyougo.utils.Utils.closeStream(r2);
        menion.android.whereyougo.utils.Utils.closeStream(r11);
        r4 = (r6 > r22 ? 1 : (r6 == r22 ? 0 : -1));
        if (r4 == 0) goto L_0x00dc;
    L_0x00d9:
        r15.delete();
    L_0x00dc:
        throw r3;
    L_0x00dd:
        r3 = 0;
        goto L_0x00bd;
    L_0x00df:
        r3 = move-exception;
        r2 = r10;
        goto L_0x00cf;
    L_0x00e2:
        r3 = move-exception;
        r11 = r12;
        r2 = r10;
        goto L_0x00cf;
    L_0x00e6:
        r14 = move-exception;
        goto L_0x0083;
    L_0x00e8:
        r14 = move-exception;
        r2 = r10;
        goto L_0x0083;
    L_0x00eb:
        r11 = r12;
        r2 = r10;
        goto L_0x00b8;
        */
        throw new UnsupportedOperationException("Method not decompiled: menion.android.whereyougo.network.DownloadCartridgeTask.download(java.lang.String, java.io.InputStream, long):boolean");
    }

    private Response handleRequest(Request request, Task task) {
        publishProgress(new Progress[]{new Progress(task, State.WORKING)});
        Response response = handleRequest(request);
        if (response != null) {
            publishProgress(new Progress[]{new Progress(task, State.SUCCESS)});
        } else {
            publishProgress(new Progress[]{new Progress(task, State.FAIL, this.errorMessage)});
        }
        return response;
    }

    private Response handleRequest(Request request) {
        if (isCancelled()) {
            return null;
        }
        try {
            Response response = this.httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response;
            }
            throw new IOException("Request " + request.toString() + " failed: " + response);
        } catch (Exception e) {
            Logger.m22e(TAG, "handleRequest(" + request.toString() + ")", e);
            this.errorMessage = e.getMessage();
            return null;
        }
    }
}
