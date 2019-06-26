package com.google.android.exoplayer2.drm;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.NotProvisionedException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Pair;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.EventDispatcher;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

@TargetApi(18)
class DefaultDrmSession implements DrmSession {
   final MediaDrmCallback callback;
   private ExoMediaDrm.KeyRequest currentKeyRequest;
   private ExoMediaDrm.ProvisionRequest currentProvisionRequest;
   private final EventDispatcher eventDispatcher;
   private final int initialDrmRequestRetryCount;
   private DrmSession.DrmSessionException lastException;
   private ExoMediaCrypto mediaCrypto;
   private final ExoMediaDrm mediaDrm;
   private final int mode;
   private byte[] offlineLicenseKeySetId;
   private int openCount;
   private final HashMap optionalKeyRequestParameters;
   private DefaultDrmSession.PostRequestHandler postRequestHandler;
   final DefaultDrmSession.PostResponseHandler postResponseHandler;
   private final DefaultDrmSession.ProvisioningManager provisioningManager;
   private HandlerThread requestHandlerThread;
   public final List schemeDatas;
   private byte[] sessionId;
   private int state;
   final UUID uuid;

   public DefaultDrmSession(UUID var1, ExoMediaDrm var2, DefaultDrmSession.ProvisioningManager var3, List var4, int var5, byte[] var6, HashMap var7, MediaDrmCallback var8, Looper var9, EventDispatcher var10, int var11) {
      if (var5 == 1 || var5 == 3) {
         Assertions.checkNotNull(var6);
      }

      this.uuid = var1;
      this.provisioningManager = var3;
      this.mediaDrm = var2;
      this.mode = var5;
      if (var6 != null) {
         this.offlineLicenseKeySetId = var6;
         this.schemeDatas = null;
      } else {
         Assertions.checkNotNull(var4);
         this.schemeDatas = Collections.unmodifiableList((List)var4);
      }

      this.optionalKeyRequestParameters = var7;
      this.callback = var8;
      this.initialDrmRequestRetryCount = var11;
      this.eventDispatcher = var10;
      this.state = 2;
      this.postResponseHandler = new DefaultDrmSession.PostResponseHandler(var9);
      this.requestHandlerThread = new HandlerThread("DrmRequestHandler");
      this.requestHandlerThread.start();
      this.postRequestHandler = new DefaultDrmSession.PostRequestHandler(this.requestHandlerThread.getLooper());
   }

   @RequiresNonNull({"sessionId"})
   private void doLicense(boolean var1) {
      int var2 = this.mode;
      if (var2 != 0 && var2 != 1) {
         if (var2 != 2) {
            if (var2 == 3) {
               Assertions.checkNotNull(this.offlineLicenseKeySetId);
               if (this.restoreKeys()) {
                  this.postKeyRequest(this.offlineLicenseKeySetId, 3, var1);
               }
            }
         } else if (this.offlineLicenseKeySetId == null) {
            this.postKeyRequest(this.sessionId, 2, var1);
         } else if (this.restoreKeys()) {
            this.postKeyRequest(this.sessionId, 2, var1);
         }
      } else if (this.offlineLicenseKeySetId == null) {
         this.postKeyRequest(this.sessionId, 1, var1);
      } else if (this.state == 4 || this.restoreKeys()) {
         long var3 = this.getLicenseDurationRemainingSec();
         if (this.mode == 0 && var3 <= 60L) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Offline license has expired or will expire soon. Remaining seconds: ");
            var5.append(var3);
            Log.d("DefaultDrmSession", var5.toString());
            this.postKeyRequest(this.sessionId, 2, var1);
         } else if (var3 <= 0L) {
            this.onError(new KeysExpiredException());
         } else {
            this.state = 4;
            this.eventDispatcher.dispatch(_$$Lambda$tzysvANfjWo6mXRxYD2fQMdks_4.INSTANCE);
         }
      }

   }

   private long getLicenseDurationRemainingSec() {
      if (!C.WIDEVINE_UUID.equals(this.uuid)) {
         return Long.MAX_VALUE;
      } else {
         Pair var1 = WidevineUtil.getLicenseDurationRemainingSec(this);
         Assertions.checkNotNull(var1);
         var1 = (Pair)var1;
         return Math.min((Long)var1.first, (Long)var1.second);
      }
   }

   @EnsuresNonNullIf(
      expression = {"sessionId"},
      result = true
   )
   private boolean isOpen() {
      int var1 = this.state;
      boolean var2;
      if (var1 != 3 && var1 != 4) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   // $FF: synthetic method
   static void lambda$onError$0(Exception var0, DefaultDrmSessionEventListener var1) {
      var1.onDrmSessionManagerError(var0);
   }

   private void onError(Exception var1) {
      this.lastException = new DrmSession.DrmSessionException(var1);
      this.eventDispatcher.dispatch(new _$$Lambda$DefaultDrmSession$_nKOJC1w2998gRg4Cg4l2mjlp30(var1));
      if (this.state != 4) {
         this.state = 1;
      }

   }

   private void onKeyResponse(Object var1, Object var2) {
      if (var1 == this.currentKeyRequest && this.isOpen()) {
         this.currentKeyRequest = null;
         if (var2 instanceof Exception) {
            this.onKeysError((Exception)var2);
            return;
         }

         Exception var10000;
         label59: {
            byte[] var3;
            boolean var10001;
            try {
               var3 = (byte[])var2;
               if (this.mode == 3) {
                  ExoMediaDrm var10 = this.mediaDrm;
                  byte[] var11 = this.offlineLicenseKeySetId;
                  Util.castNonNull(var11);
                  var10.provideKeyResponse((byte[])var11, var3);
                  this.eventDispatcher.dispatch(_$$Lambda$tzysvANfjWo6mXRxYD2fQMdks_4.INSTANCE);
                  return;
               }
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label59;
            }

            label48: {
               byte[] var8;
               try {
                  var8 = this.mediaDrm.provideKeyResponse(this.sessionId, var3);
                  if (this.mode != 2 && (this.mode != 0 || this.offlineLicenseKeySetId == null)) {
                     break label48;
                  }
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label59;
               }

               if (var8 != null) {
                  try {
                     if (var8.length != 0) {
                        this.offlineLicenseKeySetId = var8;
                     }
                  } catch (Exception var5) {
                     var10000 = var5;
                     var10001 = false;
                     break label59;
                  }
               }
            }

            try {
               this.state = 4;
               this.eventDispatcher.dispatch(_$$Lambda$wyKVEWJALn1OyjwryLo2GUxlQ2M.INSTANCE);
               return;
            } catch (Exception var4) {
               var10000 = var4;
               var10001 = false;
            }
         }

         Exception var9 = var10000;
         this.onKeysError(var9);
      }

   }

   private void onKeysError(Exception var1) {
      if (var1 instanceof NotProvisionedException) {
         this.provisioningManager.provisionRequired(this);
      } else {
         this.onError(var1);
      }

   }

   private void onKeysRequired() {
      if (this.mode == 0 && this.state == 4) {
         Util.castNonNull(this.sessionId);
         this.doLicense(false);
      }

   }

   private void onProvisionResponse(Object var1, Object var2) {
      if (var1 == this.currentProvisionRequest && (this.state == 2 || this.isOpen())) {
         this.currentProvisionRequest = null;
         if (var2 instanceof Exception) {
            this.provisioningManager.onProvisionError((Exception)var2);
         } else {
            try {
               this.mediaDrm.provideProvisionResponse((byte[])var2);
            } catch (Exception var3) {
               this.provisioningManager.onProvisionError(var3);
               return;
            }

            this.provisioningManager.onProvisionCompleted();
         }
      }
   }

   @EnsuresNonNullIf(
      expression = {"sessionId"},
      result = true
   )
   private boolean openInternal(boolean var1) {
      if (this.isOpen()) {
         return true;
      } else {
         try {
            this.sessionId = this.mediaDrm.openSession();
            this.eventDispatcher.dispatch(_$$Lambda$jFcVU4qXZB2nhSZWHXCB9S7MtRI.INSTANCE);
            this.mediaCrypto = this.mediaDrm.createMediaCrypto(this.sessionId);
            this.state = 3;
            return true;
         } catch (NotProvisionedException var3) {
            if (var1) {
               this.provisioningManager.provisionRequired(this);
            } else {
               this.onError(var3);
            }
         } catch (Exception var4) {
            this.onError(var4);
         }

         return false;
      }
   }

   private void postKeyRequest(byte[] var1, int var2, boolean var3) {
      try {
         this.currentKeyRequest = this.mediaDrm.getKeyRequest(var1, this.schemeDatas, var2, this.optionalKeyRequestParameters);
         this.postRequestHandler.post(1, this.currentKeyRequest, var3);
      } catch (Exception var4) {
         this.onKeysError(var4);
      }

   }

   @RequiresNonNull({"sessionId", "offlineLicenseKeySetId"})
   private boolean restoreKeys() {
      try {
         this.mediaDrm.restoreKeys(this.sessionId, this.offlineLicenseKeySetId);
         return true;
      } catch (Exception var2) {
         Log.e("DefaultDrmSession", "Error trying to restore Widevine keys.", var2);
         this.onError(var2);
         return false;
      }
   }

   public void acquire() {
      int var1 = this.openCount + 1;
      this.openCount = var1;
      if (var1 == 1) {
         if (this.state == 1) {
            return;
         }

         if (this.openInternal(true)) {
            this.doLicense(true);
         }
      }

   }

   public final DrmSession.DrmSessionException getError() {
      DrmSession.DrmSessionException var1;
      if (this.state == 1) {
         var1 = this.lastException;
      } else {
         var1 = null;
      }

      return var1;
   }

   public final ExoMediaCrypto getMediaCrypto() {
      return this.mediaCrypto;
   }

   public final int getState() {
      return this.state;
   }

   public boolean hasSessionId(byte[] var1) {
      return Arrays.equals(this.sessionId, var1);
   }

   public void onMediaDrmEvent(int var1) {
      if (var1 == 2) {
         this.onKeysRequired();
      }

   }

   public void onProvisionCompleted() {
      if (this.openInternal(false)) {
         this.doLicense(true);
      }

   }

   public void onProvisionError(Exception var1) {
      this.onError(var1);
   }

   public void provision() {
      this.currentProvisionRequest = this.mediaDrm.getProvisionRequest();
      this.postRequestHandler.post(0, this.currentProvisionRequest, true);
   }

   public Map queryKeyStatus() {
      byte[] var1 = this.sessionId;
      Map var2;
      if (var1 == null) {
         var2 = null;
      } else {
         var2 = this.mediaDrm.queryKeyStatus(var1);
      }

      return var2;
   }

   public boolean release() {
      int var1 = this.openCount - 1;
      this.openCount = var1;
      if (var1 == 0) {
         this.state = 0;
         this.postResponseHandler.removeCallbacksAndMessages((Object)null);
         this.postRequestHandler.removeCallbacksAndMessages((Object)null);
         this.postRequestHandler = null;
         this.requestHandlerThread.quit();
         this.requestHandlerThread = null;
         this.mediaCrypto = null;
         this.lastException = null;
         this.currentKeyRequest = null;
         this.currentProvisionRequest = null;
         byte[] var2 = this.sessionId;
         if (var2 != null) {
            this.mediaDrm.closeSession(var2);
            this.sessionId = null;
            this.eventDispatcher.dispatch(_$$Lambda$1U2yJBSMBm8ESUSz9LUzNXtoVus.INSTANCE);
         }

         return true;
      } else {
         return false;
      }
   }

   @SuppressLint({"HandlerLeak"})
   private class PostRequestHandler extends Handler {
      public PostRequestHandler(Looper var2) {
         super(var2);
      }

      private long getRetryDelayMillis(int var1) {
         return (long)Math.min((var1 - 1) * 1000, 5000);
      }

      private boolean maybeRetryRequest(Message var1) {
         boolean var2;
         if (var1.arg1 == 1) {
            var2 = true;
         } else {
            var2 = false;
         }

         if (!var2) {
            return false;
         } else {
            int var3 = var1.arg2 + 1;
            if (var3 > DefaultDrmSession.this.initialDrmRequestRetryCount) {
               return false;
            } else {
               var1 = Message.obtain(var1);
               var1.arg2 = var3;
               this.sendMessageDelayed(var1, this.getRetryDelayMillis(var3));
               return true;
            }
         }
      }

      public void handleMessage(Message var1) {
         Object var2 = var1.obj;

         Object var4;
         label39: {
            Exception var10000;
            label38: {
               boolean var10001;
               int var3;
               try {
                  var3 = var1.what;
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label38;
               }

               if (var3 != 0) {
                  if (var3 == 1) {
                     try {
                        var4 = DefaultDrmSession.this.callback.executeKeyRequest(DefaultDrmSession.this.uuid, (ExoMediaDrm.KeyRequest)var2);
                        break label39;
                     } catch (Exception var6) {
                        var10000 = var6;
                        var10001 = false;
                     }
                  } else {
                     try {
                        RuntimeException var9 = new RuntimeException();
                        throw var9;
                     } catch (Exception var5) {
                        var10000 = var5;
                        var10001 = false;
                     }
                  }
               } else {
                  try {
                     var4 = DefaultDrmSession.this.callback.executeProvisionRequest(DefaultDrmSession.this.uuid, (ExoMediaDrm.ProvisionRequest)var2);
                     break label39;
                  } catch (Exception var7) {
                     var10000 = var7;
                     var10001 = false;
                  }
               }
            }

            var4 = var10000;
            if (this.maybeRetryRequest(var1)) {
               return;
            }
         }

         DefaultDrmSession.this.postResponseHandler.obtainMessage(var1.what, Pair.create(var2, var4)).sendToTarget();
      }

      void post(int var1, Object var2, boolean var3) {
         this.obtainMessage(var1, var3, 0, var2).sendToTarget();
      }
   }

   @SuppressLint({"HandlerLeak"})
   private class PostResponseHandler extends Handler {
      public PostResponseHandler(Looper var2) {
         super(var2);
      }

      public void handleMessage(Message var1) {
         Pair var2 = (Pair)var1.obj;
         Object var3 = var2.first;
         Object var5 = var2.second;
         int var4 = var1.what;
         if (var4 != 0) {
            if (var4 == 1) {
               DefaultDrmSession.this.onKeyResponse(var3, var5);
            }
         } else {
            DefaultDrmSession.this.onProvisionResponse(var3, var5);
         }

      }
   }

   public interface ProvisioningManager {
      void onProvisionCompleted();

      void onProvisionError(Exception var1);

      void provisionRequired(DefaultDrmSession var1);
   }
}
