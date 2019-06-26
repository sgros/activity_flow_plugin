package com.google.android.exoplayer2.drm;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.EventDispatcher;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@TargetApi(18)
public class DefaultDrmSessionManager implements DrmSessionManager, DefaultDrmSession.ProvisioningManager {
   private final MediaDrmCallback callback;
   private final EventDispatcher eventDispatcher;
   private final int initialDrmRequestRetryCount;
   private final ExoMediaDrm mediaDrm;
   volatile DefaultDrmSessionManager.MediaDrmHandler mediaDrmHandler;
   private int mode;
   private final boolean multiSession;
   private byte[] offlineLicenseKeySetId;
   private final HashMap optionalKeyRequestParameters;
   private Looper playbackLooper;
   private final List provisioningSessions;
   private final List sessions;
   private final UUID uuid;

   private static List getSchemeDatas(DrmInitData var0, UUID var1, boolean var2) {
      ArrayList var3 = new ArrayList(var0.schemeDataCount);

      for(int var4 = 0; var4 < var0.schemeDataCount; ++var4) {
         DrmInitData.SchemeData var5 = var0.get(var4);
         boolean var6;
         if (var5.matches(var1) || C.CLEARKEY_UUID.equals(var1) && var5.matches(C.COMMON_PSSH_UUID)) {
            var6 = true;
         } else {
            var6 = false;
         }

         if (var6 && (var5.data != null || var2)) {
            var3.add(var5);
         }
      }

      return var3;
   }

   // $FF: synthetic method
   static void lambda$acquireSession$0(DefaultDrmSessionManager.MissingSchemeDataException var0, DefaultDrmSessionEventListener var1) {
      var1.onDrmSessionManagerError(var0);
   }

   public DrmSession acquireSession(Looper var1, DrmInitData var2) {
      Looper var3 = this.playbackLooper;
      boolean var4;
      if (var3 != null && var3 != var1) {
         var4 = false;
      } else {
         var4 = true;
      }

      Assertions.checkState(var4);
      if (this.sessions.isEmpty()) {
         this.playbackLooper = var1;
         if (this.mediaDrmHandler == null) {
            this.mediaDrmHandler = new DefaultDrmSessionManager.MediaDrmHandler(var1);
         }
      }

      byte[] var10 = this.offlineLicenseKeySetId;
      Object var5 = null;
      List var11;
      if (var10 == null) {
         var11 = getSchemeDatas(var2, this.uuid, false);
         if (var11.isEmpty()) {
            DefaultDrmSessionManager.MissingSchemeDataException var7 = new DefaultDrmSessionManager.MissingSchemeDataException(this.uuid);
            this.eventDispatcher.dispatch(new _$$Lambda$DefaultDrmSessionManager$lsU4S5fVqixyNsHyDBIvI3jEzVc(var7));
            return new ErrorStateDrmSession(new DrmSession.DrmSessionException(var7));
         }
      } else {
         var11 = null;
      }

      DefaultDrmSession var9;
      if (!this.multiSession) {
         if (this.sessions.isEmpty()) {
            var9 = (DefaultDrmSession)var5;
         } else {
            var9 = (DefaultDrmSession)this.sessions.get(0);
         }
      } else {
         Iterator var6 = this.sessions.iterator();

         do {
            var9 = (DefaultDrmSession)var5;
            if (!var6.hasNext()) {
               break;
            }

            var9 = (DefaultDrmSession)var6.next();
         } while(!Util.areEqual(var9.schemeDatas, var11));
      }

      DefaultDrmSession var8;
      if (var9 == null) {
         var8 = new DefaultDrmSession(this.uuid, this.mediaDrm, this, var11, this.mode, this.offlineLicenseKeySetId, this.optionalKeyRequestParameters, this.callback, var1, this.eventDispatcher, this.initialDrmRequestRetryCount);
         this.sessions.add(var8);
      } else {
         var8 = var9;
      }

      var8.acquire();
      return var8;
   }

   public final void addListener(Handler var1, DefaultDrmSessionEventListener var2) {
      this.eventDispatcher.addListener(var1, var2);
   }

   public boolean canAcquireSession(DrmInitData var1) {
      byte[] var2 = this.offlineLicenseKeySetId;
      boolean var3 = true;
      if (var2 != null) {
         return true;
      } else {
         if (getSchemeDatas(var1, this.uuid, true).isEmpty()) {
            if (var1.schemeDataCount != 1 || !var1.get(0).matches(C.COMMON_PSSH_UUID)) {
               return false;
            }

            StringBuilder var6 = new StringBuilder();
            var6.append("DrmInitData only contains common PSSH SchemeData. Assuming support for: ");
            var6.append(this.uuid);
            Log.w("DefaultDrmSessionMgr", var6.toString());
         }

         String var5 = var1.schemeType;
         boolean var4 = var3;
         if (var5 != null) {
            if ("cenc".equals(var5)) {
               var4 = var3;
            } else {
               if (!"cbc1".equals(var5) && !"cbcs".equals(var5) && !"cens".equals(var5)) {
                  return true;
               }

               if (Util.SDK_INT >= 25) {
                  var4 = var3;
               } else {
                  var4 = false;
               }
            }
         }

         return var4;
      }
   }

   public void onProvisionCompleted() {
      Iterator var1 = this.provisioningSessions.iterator();

      while(var1.hasNext()) {
         ((DefaultDrmSession)var1.next()).onProvisionCompleted();
      }

      this.provisioningSessions.clear();
   }

   public void onProvisionError(Exception var1) {
      Iterator var2 = this.provisioningSessions.iterator();

      while(var2.hasNext()) {
         ((DefaultDrmSession)var2.next()).onProvisionError(var1);
      }

      this.provisioningSessions.clear();
   }

   public void provisionRequired(DefaultDrmSession var1) {
      if (!this.provisioningSessions.contains(var1)) {
         this.provisioningSessions.add(var1);
         if (this.provisioningSessions.size() == 1) {
            var1.provision();
         }

      }
   }

   public void releaseSession(DrmSession var1) {
      if (!(var1 instanceof ErrorStateDrmSession)) {
         DefaultDrmSession var2 = (DefaultDrmSession)var1;
         if (var2.release()) {
            this.sessions.remove(var2);
            if (this.provisioningSessions.size() > 1 && this.provisioningSessions.get(0) == var2) {
               ((DefaultDrmSession)this.provisioningSessions.get(1)).provision();
            }

            this.provisioningSessions.remove(var2);
         }

      }
   }

   @SuppressLint({"HandlerLeak"})
   private class MediaDrmHandler extends Handler {
      public MediaDrmHandler(Looper var2) {
         super(var2);
      }

      public void handleMessage(Message var1) {
         byte[] var2 = (byte[])var1.obj;
         if (var2 != null) {
            Iterator var3 = DefaultDrmSessionManager.this.sessions.iterator();

            while(var3.hasNext()) {
               DefaultDrmSession var4 = (DefaultDrmSession)var3.next();
               if (var4.hasSessionId(var2)) {
                  var4.onMediaDrmEvent(var1.what);
                  break;
               }
            }

         }
      }
   }

   public static final class MissingSchemeDataException extends Exception {
      private MissingSchemeDataException(UUID var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Media does not support uuid: ");
         var2.append(var1);
         super(var2.toString());
      }

      // $FF: synthetic method
      MissingSchemeDataException(UUID var1, Object var2) {
         this(var1);
      }
   }
}
