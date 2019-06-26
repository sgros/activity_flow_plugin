package org.telegram.messenger.voip;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import android.telecom.PhoneAccountHandle;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;

@TargetApi(26)
public class TelegramConnectionService extends ConnectionService {
   public void onCreate() {
      super.onCreate();
      if (BuildVars.LOGS_ENABLED) {
         FileLog.w("ConnectionService created");
      }

   }

   public Connection onCreateIncomingConnection(PhoneAccountHandle var1, ConnectionRequest var2) {
      if (BuildVars.LOGS_ENABLED) {
         FileLog.d("onCreateIncomingConnection ");
      }

      Bundle var3 = var2.getExtras();
      if (var3.getInt("call_type") == 1) {
         VoIPService var4 = VoIPService.getSharedInstance();
         if (var4 == null) {
            return null;
         } else {
            return var4.isOutgoing() ? null : var4.getConnectionAndStartCall();
         }
      } else {
         var3.getInt("call_type");
         return null;
      }
   }

   public void onCreateIncomingConnectionFailed(PhoneAccountHandle var1, ConnectionRequest var2) {
      if (BuildVars.LOGS_ENABLED) {
         FileLog.e("onCreateIncomingConnectionFailed ");
      }

      if (VoIPBaseService.getSharedInstance() != null) {
         VoIPBaseService.getSharedInstance().callFailedFromConnectionService();
      }

   }

   public Connection onCreateOutgoingConnection(PhoneAccountHandle var1, ConnectionRequest var2) {
      if (BuildVars.LOGS_ENABLED) {
         FileLog.d("onCreateOutgoingConnection ");
      }

      Bundle var3 = var2.getExtras();
      if (var3.getInt("call_type") == 1) {
         VoIPService var4 = VoIPService.getSharedInstance();
         return var4 == null ? null : var4.getConnectionAndStartCall();
      } else {
         var3.getInt("call_type");
         return null;
      }
   }

   public void onCreateOutgoingConnectionFailed(PhoneAccountHandle var1, ConnectionRequest var2) {
      if (BuildVars.LOGS_ENABLED) {
         FileLog.e("onCreateOutgoingConnectionFailed ");
      }

      if (VoIPBaseService.getSharedInstance() != null) {
         VoIPBaseService.getSharedInstance().callFailedFromConnectionService();
      }

   }

   public void onDestroy() {
      super.onDestroy();
      if (BuildVars.LOGS_ENABLED) {
         FileLog.w("ConnectionService destroyed");
      }

   }
}
