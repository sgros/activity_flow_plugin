package com.adjust.sdk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SdkClickHandler implements ISdkClickHandler {
   private WeakReference activityHandlerWeakRef;
   private BackoffStrategy backoffStrategy;
   private ILogger logger;
   private List packageQueue;
   private boolean paused;
   private CustomScheduledExecutor scheduledExecutor;

   public SdkClickHandler(IActivityHandler var1, boolean var2) {
      this.init(var1, var2);
      this.logger = AdjustFactory.getLogger();
      this.scheduledExecutor = new CustomScheduledExecutor("SdkClickHandler", false);
      this.backoffStrategy = AdjustFactory.getSdkClickBackoffStrategy();
   }

   private void logErrorMessageI(ActivityPackage var1, String var2, Throwable var3) {
      String var4 = String.format("%s. (%s)", var1.getFailureMessage(), Util.getReasonString(var2, var3));
      this.logger.error(var4);
   }

   private void retrySendingI(ActivityPackage var1) {
      int var2 = var1.increaseRetries();
      this.logger.error("Retrying sdk_click package for the %d time", var2);
      this.sendSdkClick(var1);
   }

   private void sendNextSdkClick() {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            SdkClickHandler.this.sendNextSdkClickI();
         }
      });
   }

   private void sendNextSdkClickI() {
      if (!this.paused) {
         if (!this.packageQueue.isEmpty()) {
            final ActivityPackage var1 = (ActivityPackage)this.packageQueue.remove(0);
            int var2 = var1.getRetries();
            Runnable var3 = new Runnable() {
               public void run() {
                  SdkClickHandler.this.sendSdkClickI(var1);
                  SdkClickHandler.this.sendNextSdkClick();
               }
            };
            if (var2 <= 0) {
               var3.run();
            } else {
               long var4 = Util.getWaitingTime(var2, this.backoffStrategy);
               double var6 = (double)var4 / 1000.0D;
               String var8 = Util.SecondsDisplayFormat.format(var6);
               this.logger.verbose("Waiting for %s seconds before retrying sdk_click for the %d time", var8, var2);
               this.scheduledExecutor.schedule(var3, var4, TimeUnit.MILLISECONDS);
            }
         }
      }
   }

   private void sendSdkClickI(ActivityPackage var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("https://app.adjust.com");
      var2.append(var1.getPath());
      String var16 = var2.toString();

      UnsupportedEncodingException var24;
      label86: {
         SocketTimeoutException var23;
         label87: {
            IOException var22;
            label88: {
               Throwable var10000;
               label70: {
                  ResponseData var3;
                  boolean var10001;
                  try {
                     var3 = UtilNetworking.createPOSTHttpsURLConnection(var16, var1, this.packageQueue.size() - 1);
                     if (var3.jsonResponse == null) {
                        this.retrySendingI(var1);
                        return;
                     }
                  } catch (UnsupportedEncodingException var12) {
                     var24 = var12;
                     var10001 = false;
                     break label86;
                  } catch (SocketTimeoutException var13) {
                     var23 = var13;
                     var10001 = false;
                     break label87;
                  } catch (IOException var14) {
                     var22 = var14;
                     var10001 = false;
                     break label88;
                  } catch (Throwable var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label70;
                  }

                  IActivityHandler var17;
                  try {
                     var17 = (IActivityHandler)this.activityHandlerWeakRef.get();
                  } catch (UnsupportedEncodingException var8) {
                     var24 = var8;
                     var10001 = false;
                     break label86;
                  } catch (SocketTimeoutException var9) {
                     var23 = var9;
                     var10001 = false;
                     break label87;
                  } catch (IOException var10) {
                     var22 = var10;
                     var10001 = false;
                     break label88;
                  } catch (Throwable var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label70;
                  }

                  if (var17 == null) {
                     return;
                  }

                  try {
                     var17.finishedTrackingActivity(var3);
                     return;
                  } catch (UnsupportedEncodingException var4) {
                     var24 = var4;
                     var10001 = false;
                     break label86;
                  } catch (SocketTimeoutException var5) {
                     var23 = var5;
                     var10001 = false;
                     break label87;
                  } catch (IOException var6) {
                     var22 = var6;
                     var10001 = false;
                     break label88;
                  } catch (Throwable var7) {
                     var10000 = var7;
                     var10001 = false;
                  }
               }

               Throwable var18 = var10000;
               this.logErrorMessageI(var1, "Sdk_click runtime exception", var18);
               return;
            }

            IOException var19 = var22;
            this.logErrorMessageI(var1, "Sdk_click request failed. Will retry later", var19);
            this.retrySendingI(var1);
            return;
         }

         SocketTimeoutException var20 = var23;
         this.logErrorMessageI(var1, "Sdk_click request timed out. Will retry later", var20);
         this.retrySendingI(var1);
         return;
      }

      UnsupportedEncodingException var21 = var24;
      this.logErrorMessageI(var1, "Sdk_click failed to encode parameters", var21);
   }

   public void init(IActivityHandler var1, boolean var2) {
      this.paused = var2 ^ true;
      this.packageQueue = new ArrayList();
      this.activityHandlerWeakRef = new WeakReference(var1);
   }

   public void pauseSending() {
      this.paused = true;
   }

   public void resumeSending() {
      this.paused = false;
      this.sendNextSdkClick();
   }

   public void sendSdkClick(final ActivityPackage var1) {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            SdkClickHandler.this.packageQueue.add(var1);
            SdkClickHandler.this.logger.debug("Added sdk_click %d", SdkClickHandler.this.packageQueue.size());
            SdkClickHandler.this.logger.verbose("%s", var1.getExtendedString());
            SdkClickHandler.this.sendNextSdkClick();
         }
      });
   }

   public void teardown() {
      this.logger.verbose("SdkClickHandler teardown");
      if (this.scheduledExecutor != null) {
         try {
            this.scheduledExecutor.shutdownNow();
         } catch (SecurityException var2) {
         }
      }

      if (this.packageQueue != null) {
         this.packageQueue.clear();
      }

      if (this.activityHandlerWeakRef != null) {
         this.activityHandlerWeakRef.clear();
      }

      this.scheduledExecutor = null;
      this.logger = null;
      this.packageQueue = null;
      this.backoffStrategy = null;
   }
}
