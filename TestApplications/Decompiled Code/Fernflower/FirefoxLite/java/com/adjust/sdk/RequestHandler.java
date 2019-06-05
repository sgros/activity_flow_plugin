package com.adjust.sdk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;

public class RequestHandler implements IRequestHandler {
   private ILogger logger = AdjustFactory.getLogger();
   private WeakReference packageHandlerWeakRef;
   private CustomScheduledExecutor scheduledExecutor = new CustomScheduledExecutor("RequestHandler", false);

   public RequestHandler(IPackageHandler var1) {
      this.init(var1);
   }

   private void closePackageI(ActivityPackage var1, String var2, Throwable var3) {
      String var5 = String.format("%s. (%s) Will retry later", var1.getFailureMessage(), Util.getReasonString(var2, var3));
      this.logger.error(var5);
      ResponseData var4 = ResponseData.buildResponseData(var1);
      var4.message = var5;
      IPackageHandler var6 = (IPackageHandler)this.packageHandlerWeakRef.get();
      if (var6 != null) {
         var6.closeFirstPackage(var4, var1);
      }
   }

   private void sendI(ActivityPackage var1, int var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append("https://app.adjust.com");
      var3.append(var1.getPath());
      String var17 = var3.toString();

      UnsupportedEncodingException var25;
      label82: {
         SocketTimeoutException var24;
         label83: {
            IOException var23;
            label84: {
               Throwable var10000;
               label68: {
                  ResponseData var4;
                  boolean var10001;
                  IPackageHandler var18;
                  try {
                     var4 = UtilNetworking.createPOSTHttpsURLConnection(var17, var1, var2);
                     var18 = (IPackageHandler)this.packageHandlerWeakRef.get();
                  } catch (UnsupportedEncodingException var13) {
                     var25 = var13;
                     var10001 = false;
                     break label82;
                  } catch (SocketTimeoutException var14) {
                     var24 = var14;
                     var10001 = false;
                     break label83;
                  } catch (IOException var15) {
                     var23 = var15;
                     var10001 = false;
                     break label84;
                  } catch (Throwable var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label68;
                  }

                  if (var18 == null) {
                     return;
                  }

                  try {
                     if (var4.jsonResponse == null) {
                        var18.closeFirstPackage(var4, var1);
                        return;
                     }
                  } catch (UnsupportedEncodingException var9) {
                     var25 = var9;
                     var10001 = false;
                     break label82;
                  } catch (SocketTimeoutException var10) {
                     var24 = var10;
                     var10001 = false;
                     break label83;
                  } catch (IOException var11) {
                     var23 = var11;
                     var10001 = false;
                     break label84;
                  } catch (Throwable var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label68;
                  }

                  try {
                     var18.sendNextPackage(var4);
                     return;
                  } catch (UnsupportedEncodingException var5) {
                     var25 = var5;
                     var10001 = false;
                     break label82;
                  } catch (SocketTimeoutException var6) {
                     var24 = var6;
                     var10001 = false;
                     break label83;
                  } catch (IOException var7) {
                     var23 = var7;
                     var10001 = false;
                     break label84;
                  } catch (Throwable var8) {
                     var10000 = var8;
                     var10001 = false;
                  }
               }

               Throwable var19 = var10000;
               this.sendNextPackageI(var1, "Runtime exception", var19);
               return;
            }

            IOException var20 = var23;
            this.closePackageI(var1, "Request failed", var20);
            return;
         }

         SocketTimeoutException var21 = var24;
         this.closePackageI(var1, "Request timed out", var21);
         return;
      }

      UnsupportedEncodingException var22 = var25;
      this.sendNextPackageI(var1, "Failed to encode parameters", var22);
   }

   private void sendNextPackageI(ActivityPackage var1, String var2, Throwable var3) {
      var2 = String.format("%s. (%s)", var1.getFailureMessage(), Util.getReasonString(var2, var3));
      this.logger.error(var2);
      ResponseData var4 = ResponseData.buildResponseData(var1);
      var4.message = var2;
      IPackageHandler var5 = (IPackageHandler)this.packageHandlerWeakRef.get();
      if (var5 != null) {
         var5.sendNextPackage(var4);
      }
   }

   public void init(IPackageHandler var1) {
      this.packageHandlerWeakRef = new WeakReference(var1);
   }

   public void sendPackage(final ActivityPackage var1, final int var2) {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            RequestHandler.this.sendI(var1, var2);
         }
      });
   }

   public void teardown() {
      this.logger.verbose("RequestHandler teardown");
      if (this.scheduledExecutor != null) {
         try {
            this.scheduledExecutor.shutdownNow();
         } catch (SecurityException var2) {
         }
      }

      if (this.packageHandlerWeakRef != null) {
         this.packageHandlerWeakRef.clear();
      }

      this.scheduledExecutor = null;
      this.packageHandlerWeakRef = null;
      this.logger = null;
   }
}
