// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.voip;

import android.os.Bundle;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.PhoneAccountHandle;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.BuildVars;
import android.annotation.TargetApi;
import android.telecom.ConnectionService;

@TargetApi(26)
public class TelegramConnectionService extends ConnectionService
{
    public void onCreate() {
        super.onCreate();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.w("ConnectionService created");
        }
    }
    
    public Connection onCreateIncomingConnection(final PhoneAccountHandle phoneAccountHandle, final ConnectionRequest connectionRequest) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("onCreateIncomingConnection ");
        }
        final Bundle extras = connectionRequest.getExtras();
        if (extras.getInt("call_type") != 1) {
            extras.getInt("call_type");
            return null;
        }
        final VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance == null) {
            return null;
        }
        if (sharedInstance.isOutgoing()) {
            return null;
        }
        return sharedInstance.getConnectionAndStartCall();
    }
    
    public void onCreateIncomingConnectionFailed(final PhoneAccountHandle phoneAccountHandle, final ConnectionRequest connectionRequest) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.e("onCreateIncomingConnectionFailed ");
        }
        if (VoIPBaseService.getSharedInstance() != null) {
            VoIPBaseService.getSharedInstance().callFailedFromConnectionService();
        }
    }
    
    public Connection onCreateOutgoingConnection(final PhoneAccountHandle phoneAccountHandle, final ConnectionRequest connectionRequest) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("onCreateOutgoingConnection ");
        }
        final Bundle extras = connectionRequest.getExtras();
        if (extras.getInt("call_type") != 1) {
            extras.getInt("call_type");
            return null;
        }
        final VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance == null) {
            return null;
        }
        return sharedInstance.getConnectionAndStartCall();
    }
    
    public void onCreateOutgoingConnectionFailed(final PhoneAccountHandle phoneAccountHandle, final ConnectionRequest connectionRequest) {
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
