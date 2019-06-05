package com.adjust.sdk;

import android.content.Context;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class PackageHandler implements IPackageHandler {
   private static final String PACKAGE_QUEUE_FILENAME = "AdjustIoPackageQueue";
   private static final String PACKAGE_QUEUE_NAME = "Package queue";
   private WeakReference activityHandlerWeakRef;
   private BackoffStrategy backoffStrategy = AdjustFactory.getPackageHandlerBackoffStrategy();
   private Context context;
   private AtomicBoolean isSending;
   private ILogger logger = AdjustFactory.getLogger();
   private List packageQueue;
   private boolean paused;
   private IRequestHandler requestHandler;
   private CustomScheduledExecutor scheduledExecutor = new CustomScheduledExecutor("PackageHandler", false);

   public PackageHandler(IActivityHandler var1, Context var2, boolean var3) {
      this.init(var1, var2, var3);
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            PackageHandler.this.initI();
         }
      });
   }

   private void addI(ActivityPackage var1) {
      this.packageQueue.add(var1);
      this.logger.debug("Added package %d (%s)", this.packageQueue.size(), var1);
      this.logger.verbose("%s", var1.getExtendedString());
      this.writePackageQueueI();
   }

   public static Boolean deletePackageQueue(Context var0) {
      return var0.deleteFile("AdjustIoPackageQueue");
   }

   private void initI() {
      this.requestHandler = AdjustFactory.getRequestHandler(this);
      this.isSending = new AtomicBoolean();
      this.readPackageQueueI();
   }

   private void readPackageQueueI() {
      try {
         this.packageQueue = (List)Util.readObject(this.context, "AdjustIoPackageQueue", "Package queue", List.class);
      } catch (Exception var2) {
         this.logger.error("Failed to read %s file (%s)", "Package queue", var2.getMessage());
         this.packageQueue = null;
      }

      if (this.packageQueue != null) {
         this.logger.debug("Package handler read %d packages", this.packageQueue.size());
      } else {
         this.packageQueue = new ArrayList();
      }

   }

   private void sendFirstI() {
      if (!this.packageQueue.isEmpty()) {
         if (this.paused) {
            this.logger.debug("Package handler is paused");
         } else if (this.isSending.getAndSet(true)) {
            this.logger.verbose("Package handler is already sending");
         } else {
            ActivityPackage var1 = (ActivityPackage)this.packageQueue.get(0);
            this.requestHandler.sendPackage(var1, this.packageQueue.size() - 1);
         }
      }
   }

   private void sendNextI() {
      this.packageQueue.remove(0);
      this.writePackageQueueI();
      this.isSending.set(false);
      this.logger.verbose("Package handler can send");
      this.sendFirstI();
   }

   private void writePackageQueueI() {
      Util.writeObject(this.packageQueue, this.context, "AdjustIoPackageQueue", "Package queue");
      this.logger.debug("Package handler wrote %d packages", this.packageQueue.size());
   }

   public void addPackage(final ActivityPackage var1) {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            PackageHandler.this.addI(var1);
         }
      });
   }

   public void closeFirstPackage(ResponseData var1, ActivityPackage var2) {
      var1.willRetry = true;
      IActivityHandler var3 = (IActivityHandler)this.activityHandlerWeakRef.get();
      if (var3 != null) {
         var3.finishedTrackingActivity(var1);
      }

      Runnable var9 = new Runnable() {
         public void run() {
            PackageHandler.this.logger.verbose("Package handler can send");
            PackageHandler.this.isSending.set(false);
            PackageHandler.this.sendFirstPackage();
         }
      };
      if (var2 == null) {
         var9.run();
      } else {
         int var4 = var2.increaseRetries();
         long var5 = Util.getWaitingTime(var4, this.backoffStrategy);
         double var7 = (double)var5 / 1000.0D;
         String var10 = Util.SecondsDisplayFormat.format(var7);
         this.logger.verbose("Waiting for %s seconds before retrying the %d time", var10, var4);
         this.scheduledExecutor.schedule(var9, var5, TimeUnit.MILLISECONDS);
      }
   }

   public void init(IActivityHandler var1, Context var2, boolean var3) {
      this.activityHandlerWeakRef = new WeakReference(var1);
      this.context = var2;
      this.paused = var3 ^ true;
   }

   public void pauseSending() {
      this.paused = true;
   }

   public void resumeSending() {
      this.paused = false;
   }

   public void sendFirstPackage() {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            PackageHandler.this.sendFirstI();
         }
      });
   }

   public void sendNextPackage(ResponseData var1) {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            PackageHandler.this.sendNextI();
         }
      });
      IActivityHandler var2 = (IActivityHandler)this.activityHandlerWeakRef.get();
      if (var2 != null) {
         var2.finishedTrackingActivity(var1);
      }

   }

   public void teardown(boolean var1) {
      this.logger.verbose("PackageHandler teardown");
      if (this.scheduledExecutor != null) {
         try {
            this.scheduledExecutor.shutdownNow();
         } catch (SecurityException var3) {
         }
      }

      if (this.activityHandlerWeakRef != null) {
         this.activityHandlerWeakRef.clear();
      }

      if (this.requestHandler != null) {
         this.requestHandler.teardown();
      }

      if (this.packageQueue != null) {
         this.packageQueue.clear();
      }

      if (var1 && this.context != null) {
         deletePackageQueue(this.context);
      }

      this.scheduledExecutor = null;
      this.requestHandler = null;
      this.activityHandlerWeakRef = null;
      this.packageQueue = null;
      this.isSending = null;
      this.context = null;
      this.logger = null;
      this.backoffStrategy = null;
   }

   public void updatePackages(final SessionParameters var1) {
      if (var1 != null) {
         var1 = var1.deepCopy();
      } else {
         var1 = null;
      }

      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            PackageHandler.this.updatePackagesI(var1);
         }
      });
   }

   public void updatePackagesI(SessionParameters var1) {
      if (var1 != null) {
         this.logger.debug("Updating package handler queue");
         this.logger.verbose("Session callback parameters: %s", var1.callbackParameters);
         this.logger.verbose("Session partner parameters: %s", var1.partnerParameters);
         Iterator var2 = this.packageQueue.iterator();

         while(var2.hasNext()) {
            ActivityPackage var3 = (ActivityPackage)var2.next();
            Map var4 = var3.getParameters();
            PackageBuilder.addMapJson(var4, "callback_params", Util.mergeParameters(var1.callbackParameters, var3.getCallbackParameters(), "Callback"));
            PackageBuilder.addMapJson(var4, "partner_params", Util.mergeParameters(var1.partnerParameters, var3.getPartnerParameters(), "Partner"));
         }

         this.writePackageQueueI();
      }
   }
}
