package menion.android.whereyougo.network;

import android.content.Context;
import android.os.AsyncTask;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import menion.android.whereyougo.utils.Logger;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadCartridgeTask extends AsyncTask {
   private static final String DOWNLOAD = "http://www.wherigo.com/cartridge/download.aspx";
   private static final String LOGIN = "https://www.wherigo.com/login/default.aspx";
   private static final String TAG = "DownloadCartridgeTask";
   private String errorMessage;
   private OkHttpClient httpClient;
   private final String password;
   private final String username;

   public DownloadCartridgeTask(Context var1, String var2, String var3) {
      this.username = var2;
      this.password = var3;
   }

   private boolean download(String var1) {
      FormBody var2 = (new FormBody.Builder()).add("__EVENTTARGET", "").add("__EVENTARGUMENT", "").add("ctl00$ContentPlaceHolder1$uxDeviceList", "4").add("ctl00$ContentPlaceHolder1$btnDownload", "Download Now").build();
      Response var7 = this.handleRequest((new Request.Builder()).url("http://www.wherigo.com/cartridge/download.aspx?CGUID=" + var1).post(var2).build());
      boolean var6;
      if (var7 != null && "application/octet-stream".equals(var7.body().contentType().toString())) {
         String var3 = var7.header("Content-Disposition", "");
         if (var3.matches("(?i)^ *attachment *; *filename *= *(.*) *$")) {
            var1 = var1 + "_" + var3.replaceFirst("(?i)^ *attachment *; *filename *= *(.*) *$", "$1");
         } else {
            var1 = var1 + ".gwc";
         }

         long var4 = Long.parseLong(var7.header("Content-Length", "0"));
         var6 = this.download(var1, var7.body().byteStream(), var4);
      } else {
         var6 = false;
      }

      return var6;
   }

   private boolean download(String param1, InputStream param2, long param3) {
      // $FF: Couldn't be decompiled
   }

   private boolean download(String[] var1) {
      this.publishProgress(new DownloadCartridgeTask.Progress[]{new DownloadCartridgeTask.Progress(DownloadCartridgeTask.Task.DOWNLOAD, DownloadCartridgeTask.State.WORKING)});
      int var2 = 0;

      boolean var3;
      while(true) {
         if (var2 < var1.length) {
            if (this.download(var1[var2])) {
               this.publishProgress(new DownloadCartridgeTask.Progress[]{new DownloadCartridgeTask.Progress(DownloadCartridgeTask.Task.DOWNLOAD, (long)var2, (long)var1.length)});
               ++var2;
               continue;
            }

            this.publishProgress(new DownloadCartridgeTask.Progress[]{new DownloadCartridgeTask.Progress(DownloadCartridgeTask.Task.DOWNLOAD, DownloadCartridgeTask.State.FAIL, this.errorMessage)});
            var3 = false;
            break;
         }

         var3 = true;
         break;
      }

      return var3;
   }

   private Response handleRequest(Request var1) {
      Response var2;
      if (this.isCancelled()) {
         var2 = null;
      } else {
         Exception var10000;
         label28: {
            boolean var10001;
            Response var3;
            try {
               var3 = this.httpClient.newCall(var1).execute();
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label28;
            }

            var2 = var3;

            try {
               if (!var3.isSuccessful()) {
                  StringBuilder var4 = new StringBuilder();
                  IOException var8 = new IOException(var4.append("Request ").append(var1.toString()).append(" failed: ").append(var3).toString());
                  throw var8;
               }

               return var2;
            } catch (Exception var5) {
               var10000 = var5;
               var10001 = false;
            }
         }

         Exception var7 = var10000;
         Logger.e("DownloadCartridgeTask", "handleRequest(" + var1.toString() + ")", var7);
         this.errorMessage = var7.getMessage();
         var2 = null;
      }

      return var2;
   }

   private Response handleRequest(Request var1, DownloadCartridgeTask.Task var2) {
      this.publishProgress(new DownloadCartridgeTask.Progress[]{new DownloadCartridgeTask.Progress(var2, DownloadCartridgeTask.State.WORKING)});
      Response var3 = this.handleRequest(var1);
      if (var3 != null) {
         this.publishProgress(new DownloadCartridgeTask.Progress[]{new DownloadCartridgeTask.Progress(var2, DownloadCartridgeTask.State.SUCCESS)});
      } else {
         this.publishProgress(new DownloadCartridgeTask.Progress[]{new DownloadCartridgeTask.Progress(var2, DownloadCartridgeTask.State.FAIL, this.errorMessage)});
      }

      return var3;
   }

   private boolean init() {
      boolean var1 = true;

      label23: {
         Object var3;
         try {
            System.setProperty("http.keepAlive", "false");
            OkHttpClient.Builder var2 = new OkHttpClient.Builder();
            TLSSocketFactory var7 = new TLSSocketFactory();
            OkHttpClient.Builder var8 = var2.sslSocketFactory(var7);
            NonPersistentCookieJar var6 = new NonPersistentCookieJar();
            this.httpClient = var8.cookieJar(var6).connectTimeout(30L, TimeUnit.SECONDS).readTimeout(30L, TimeUnit.SECONDS).writeTimeout(30L, TimeUnit.SECONDS).build();
            break label23;
         } catch (KeyManagementException var4) {
            var3 = var4;
         } catch (NoSuchAlgorithmException var5) {
            var3 = var5;
         }

         Logger.e("DownloadCartridgeTask", "init()", (Exception)var3);
         this.errorMessage = ((GeneralSecurityException)var3).getMessage();
      }

      if (this.httpClient == null) {
         this.publishProgress(new DownloadCartridgeTask.Progress[]{new DownloadCartridgeTask.Progress(DownloadCartridgeTask.Task.INIT, DownloadCartridgeTask.State.FAIL, this.errorMessage)});
      }

      if (this.httpClient == null) {
         var1 = false;
      }

      return var1;
   }

   private boolean login() {
      boolean var1 = true;
      FormBody var2 = (new FormBody.Builder()).add("__EVENTTARGET", "").add("__EVENTARGUMENT", "").add("ctl00$ContentPlaceHolder1$Login1$Login1$UserName", this.username).add("ctl00$ContentPlaceHolder1$Login1$Login1$Password", this.password).add("ctl00$ContentPlaceHolder1$Login1$Login1$LoginButton", "Sign In").build();
      Request var3 = (new Request.Builder()).url("https://www.wherigo.com/login/default.aspx").post(var2).build();
      this.publishProgress(new DownloadCartridgeTask.Progress[]{new DownloadCartridgeTask.Progress(DownloadCartridgeTask.Task.LOGIN, DownloadCartridgeTask.State.WORKING)});
      Response var4 = this.handleRequest(var3);
      if (var4 != null && !"https://www.wherigo.com/login/default.aspx".equals(var4.request().url().toString())) {
         this.publishProgress(new DownloadCartridgeTask.Progress[]{new DownloadCartridgeTask.Progress(DownloadCartridgeTask.Task.LOGIN, DownloadCartridgeTask.State.SUCCESS)});
      } else {
         this.publishProgress(new DownloadCartridgeTask.Progress[]{new DownloadCartridgeTask.Progress(DownloadCartridgeTask.Task.LOGIN, DownloadCartridgeTask.State.FAIL, this.errorMessage)});
         var1 = false;
      }

      return var1;
   }

   private boolean logout() {
      FormBody var1 = (new FormBody.Builder()).add("__EVENTTARGET", "ctl00$ProfileWidget$LoginStatus1$ctl00").add("__EVENTARGUMENT", "").build();
      boolean var2;
      if (this.handleRequest((new Request.Builder()).url("https://www.wherigo.com/login/default.aspx").post(var1).build(), DownloadCartridgeTask.Task.LOGOUT) != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private boolean ping() {
      boolean var1;
      if (this.handleRequest((new Request.Builder()).url("https://www.wherigo.com/login/default.aspx").build(), DownloadCartridgeTask.Task.PING) != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected Boolean doInBackground(String... var1) {
      boolean var2;
      if (this.init() && this.ping() && this.login() && this.download(var1) && this.logout()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public class Progress {
      long completed;
      String message;
      final DownloadCartridgeTask.State state;
      final DownloadCartridgeTask.Task task;
      long total;

      public Progress(DownloadCartridgeTask.Task var2, long var3, long var5) {
         this.state = DownloadCartridgeTask.State.WORKING;
         this.task = var2;
         this.total = var5;
         this.completed = var3;
      }

      public Progress(DownloadCartridgeTask.Task var2, DownloadCartridgeTask.State var3) {
         this.task = var2;
         this.state = var3;
      }

      public Progress(DownloadCartridgeTask.Task var2, DownloadCartridgeTask.State var3, String var4) {
         this.task = var2;
         this.state = var3;
         this.message = var4;
      }

      public long getCompleted() {
         return this.completed;
      }

      public String getMessage() {
         return this.message;
      }

      public DownloadCartridgeTask.State getState() {
         return this.state;
      }

      public DownloadCartridgeTask.Task getTask() {
         return this.task;
      }

      public long getTotal() {
         return this.total;
      }
   }

   public static enum State {
      FAIL,
      SUCCESS,
      WORKING;
   }

   public static enum Task {
      DOWNLOAD,
      DOWNLOAD_SINGLE,
      INIT,
      LOGIN,
      LOGOUT,
      PING;
   }
}
