// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.voip;

import android.app.PendingIntent$CanceledException;
import androidx.core.app.NotificationManagerCompat;
import android.annotation.SuppressLint;
import android.os.Parcelable;
import android.view.KeyEvent;
import org.telegram.messenger.LocaleController;
import android.app.Notification$Builder;
import org.telegram.messenger.NotificationsController;
import java.util.Arrays;
import android.os.IBinder;
import org.telegram.ui.VoIPActivity;
import android.app.Activity;
import android.annotation.TargetApi;
import android.net.Uri;
import android.app.PendingIntent;
import android.content.Intent;
import org.telegram.ui.VoIPFeedbackActivity;
import org.telegram.messenger.NotificationCenter;
import java.math.BigInteger;
import org.telegram.messenger.MessagesStorage;
import java.util.Iterator;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.content.Context;
import android.widget.Toast;
import org.telegram.ui.Components.voip.VoIPHelper;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.Collections;
import org.telegram.messenger.MessagesController;
import java.io.IOException;
import org.telegram.messenger.Utilities;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.AndroidUtilities;
import android.os.Bundle;
import android.telecom.TelecomManager;
import org.telegram.messenger.ContactsController;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import android.app.KeyguardManager;
import org.telegram.messenger.XiaomiUtilities;
import android.os.Build$VERSION;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.BuildVars;
import java.util.ArrayList;
import java.util.List;
import org.telegram.tgnet.TLRPC;

public class VoIPService extends VoIPBaseService
{
    public static final int CALL_MAX_LAYER;
    public static final int CALL_MIN_LAYER = 65;
    public static final int STATE_BUSY = 17;
    public static final int STATE_EXCHANGING_KEYS = 12;
    public static final int STATE_HANGING_UP = 10;
    public static final int STATE_REQUESTING = 14;
    public static final int STATE_RINGING = 16;
    public static final int STATE_WAITING = 13;
    public static final int STATE_WAITING_INCOMING = 15;
    public static TLRPC.PhoneCall callIShouldHavePutIntoIntent;
    private byte[] a_or_b;
    private byte[] authKey;
    private TLRPC.PhoneCall call;
    private int callReqId;
    private String debugLog;
    private Runnable delayedStartOutgoingCall;
    private boolean endCallAfterRequest;
    private boolean forceRating;
    private byte[] g_a;
    private byte[] g_a_hash;
    private byte[] groupCallEncryptionKey;
    private long groupCallKeyFingerprint;
    private List<Integer> groupUsersToAdd;
    private boolean joiningGroupCall;
    private long keyFingerprint;
    private boolean needSendDebugLog;
    private int peerCapabilities;
    private ArrayList<TLRPC.PhoneCall> pendingUpdates;
    private boolean startedRinging;
    private boolean upgrading;
    private TLRPC.User user;
    
    static {
        CALL_MAX_LAYER = VoIPController.getConnectionMaxLayer();
    }
    
    public VoIPService() {
        this.needSendDebugLog = false;
        this.endCallAfterRequest = false;
        this.pendingUpdates = new ArrayList<TLRPC.PhoneCall>();
        this.groupUsersToAdd = new ArrayList<Integer>();
        this.startedRinging = false;
    }
    
    private void acknowledgeCall(final boolean b) {
        if (this.call instanceof TLRPC.TL_phoneCallDiscarded) {
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Call ");
                sb.append(this.call.id);
                sb.append(" was discarded before the service started, stopping");
                FileLog.w(sb.toString());
            }
            this.stopSelf();
            return;
        }
        if (Build$VERSION.SDK_INT >= 19 && XiaomiUtilities.isMIUI() && !XiaomiUtilities.isCustomPermissionGranted(10020) && ((KeyguardManager)this.getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("MIUI: no permission to show when locked but the screen is locked. ¯\\_(\u30c4)_/¯");
            }
            this.stopSelf();
            return;
        }
        final TLRPC.TL_phone_receivedCall tl_phone_receivedCall = new TLRPC.TL_phone_receivedCall();
        tl_phone_receivedCall.peer = new TLRPC.TL_inputPhoneCall();
        final TLRPC.TL_inputPhoneCall peer = tl_phone_receivedCall.peer;
        final TLRPC.PhoneCall call = this.call;
        peer.id = call.id;
        peer.access_hash = call.access_hash;
        ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_phone_receivedCall, new RequestDelegate() {
            final /* synthetic */ VoIPService this$0;
            
            @Override
            public void run(final TLObject tlObject, final TLRPC.TL_error tl_error) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (VoIPBaseService.sharedInstance == null) {
                            return;
                        }
                        if (BuildVars.LOGS_ENABLED) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("receivedCall response = ");
                            sb.append(tlObject);
                            FileLog.w(sb.toString());
                        }
                        if (tl_error != null) {
                            if (BuildVars.LOGS_ENABLED) {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("error on receivedCall: ");
                                sb2.append(tl_error);
                                FileLog.e(sb2.toString());
                            }
                            VoIPService.this.stopSelf();
                        }
                        else {
                            if (VoIPBaseService.USE_CONNECTION_SERVICE) {
                                ContactsController.getInstance(VoIPService.this.currentAccount).createOrUpdateConnectionServiceContact(VoIPService.this.user.id, VoIPService.this.user.first_name, VoIPService.this.user.last_name);
                                final TelecomManager telecomManager = (TelecomManager)VoIPService.this.getSystemService("telecom");
                                final Bundle bundle = new Bundle();
                                bundle.putInt("call_type", 1);
                                telecomManager.addNewIncomingCall(VoIPService.this.addAccountToTelecomManager(), bundle);
                            }
                            final RequestDelegate this$1 = RequestDelegate.this;
                            if (b) {
                                this$1.this$0.startRinging();
                            }
                        }
                    }
                });
            }
        }, 2);
    }
    
    private int convertDataSavingMode(final int n) {
        if (n != 3) {
            return n;
        }
        return ApplicationLoader.isRoaming() ? 1 : 0;
    }
    
    private void dumpCallObject() {
        try {
            if (BuildVars.LOGS_ENABLED) {
                for (final Field field : TLRPC.PhoneCall.class.getFields()) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(field.getName());
                    sb.append(" = ");
                    sb.append(field.get(this.call));
                    FileLog.d(sb.toString());
                }
            }
        }
        catch (Exception ex) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e(ex);
            }
        }
    }
    
    private String[] getEmoji() {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(this.authKey);
            byteArrayOutputStream.write(this.g_a);
            return EncryptionKeyEmojifier.emojifyForCall(Utilities.computeSHA256(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size()));
        }
        catch (IOException ex) {
            return EncryptionKeyEmojifier.emojifyForCall(Utilities.computeSHA256(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size()));
        }
    }
    
    public static VoIPService getSharedInstance() {
        final VoIPBaseService sharedInstance = VoIPBaseService.sharedInstance;
        VoIPService voIPService;
        if (sharedInstance instanceof VoIPService) {
            voIPService = (VoIPService)sharedInstance;
        }
        else {
            voIPService = null;
        }
        return voIPService;
    }
    
    private void initiateActualEncryptedCall() {
        final Runnable timeoutRunnable = super.timeoutRunnable;
        if (timeoutRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(timeoutRunnable);
            super.timeoutRunnable = null;
        }
        try {
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("InitCall: keyID=");
                sb.append(this.keyFingerprint);
                FileLog.d(sb.toString());
            }
            final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(super.currentAccount);
            final HashSet set = new HashSet<String>(notificationsSettings.getStringSet("calls_access_hashes", Collections.EMPTY_SET));
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(this.call.id);
            sb2.append(" ");
            sb2.append(this.call.access_hash);
            sb2.append(" ");
            sb2.append(System.currentTimeMillis());
            set.add(sb2.toString());
            while (set.size() > 20) {
                final Iterator<String> iterator = set.iterator();
                long n = Long.MAX_VALUE;
                Object o = null;
                while (iterator.hasNext()) {
                    final String s = iterator.next();
                    final String[] split = s.split(" ");
                    if (split.length < 2) {
                        iterator.remove();
                    }
                    else {
                        try {
                            final long long1 = Long.parseLong(split[2]);
                            if (long1 >= n) {
                                continue;
                            }
                            o = s;
                            n = long1;
                        }
                        catch (Exception ex2) {
                            iterator.remove();
                        }
                    }
                }
                if (o != null) {
                    set.remove(o);
                }
            }
            notificationsSettings.edit().putStringSet("calls_access_hashes", (Set)set).commit();
            final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            final VoIPController controller = super.controller;
            final double v = MessagesController.getInstance(super.currentAccount).callPacketTimeout;
            Double.isNaN(v);
            final double n2 = v / 1000.0;
            final double v2 = MessagesController.getInstance(super.currentAccount).callConnectTimeout;
            Double.isNaN(v2);
            controller.setConfig(n2, v2 / 1000.0, this.convertDataSavingMode(globalMainSettings.getInt("VoipDataSaving", VoIPHelper.getDataSavingDefault())), this.call.id);
            super.controller.setEncryptionKey(this.authKey, super.isOutgoing);
            final TLRPC.TL_phoneConnection[] array = this.call.connections.toArray(new TLRPC.TL_phoneConnection[this.call.connections.size()]);
            final SharedPreferences globalMainSettings2 = MessagesController.getGlobalMainSettings();
            super.controller.setRemoteEndpoints(array, this.call.p2p_allowed, globalMainSettings2.getBoolean("dbg_force_tcp_in_calls", false), this.call.protocol.max_layer);
            if (globalMainSettings2.getBoolean("dbg_force_tcp_in_calls", false)) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText((Context)VoIPService.this, (CharSequence)"This call uses TCP which will degrade its quality.", 0).show();
                    }
                });
            }
            if (globalMainSettings2.getBoolean("proxy_enabled", false) && globalMainSettings2.getBoolean("proxy_enabled_calls", false)) {
                final String string = globalMainSettings2.getString("proxy_ip", (String)null);
                final String string2 = globalMainSettings2.getString("proxy_secret", (String)null);
                if (!TextUtils.isEmpty((CharSequence)string) && TextUtils.isEmpty((CharSequence)string2)) {
                    super.controller.setProxy(string, globalMainSettings2.getInt("proxy_port", 0), globalMainSettings2.getString("proxy_user", (String)null), globalMainSettings2.getString("proxy_pass", (String)null));
                }
            }
            super.controller.start();
            this.updateNetworkType();
            super.controller.connect();
            super.controllerStarted = true;
            AndroidUtilities.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    final VoIPService this$0 = VoIPService.this;
                    if (this$0.controller == null) {
                        return;
                    }
                    this$0.updateStats();
                    AndroidUtilities.runOnUIThread(this, 5000L);
                }
            }, 5000L);
        }
        catch (Exception ex) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("error starting call", ex);
            }
            this.callFailed();
        }
    }
    
    private void processAcceptedCall() {
        this.dispatchStateChanged(12);
        final BigInteger m = new BigInteger(1, MessagesStorage.getInstance(super.currentAccount).getSecretPBytes());
        final BigInteger bigInteger = new BigInteger(1, this.call.g_b);
        if (!Utilities.isGoodGaAndGb(bigInteger, m)) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.w("stopping VoIP service, bad Ga and Gb");
            }
            this.callFailed();
            return;
        }
        final byte[] byteArray = bigInteger.modPow(new BigInteger(1, this.a_or_b), m).toByteArray();
        byte[] authKey;
        if (byteArray.length > 256) {
            authKey = new byte[256];
            System.arraycopy(byteArray, byteArray.length - 256, authKey, 0, 256);
        }
        else if (byteArray.length < 256) {
            final byte[] array = new byte[256];
            System.arraycopy(byteArray, 0, array, 256 - byteArray.length, byteArray.length);
            int n = 0;
            while (true) {
                authKey = array;
                if (n >= 256 - byteArray.length) {
                    break;
                }
                array[n] = 0;
                ++n;
            }
        }
        else {
            authKey = byteArray;
        }
        final byte[] computeSHA1 = Utilities.computeSHA1(authKey);
        final byte[] array2 = new byte[8];
        System.arraycopy(computeSHA1, computeSHA1.length - 8, array2, 0, 8);
        final long bytesToLong = Utilities.bytesToLong(array2);
        this.authKey = authKey;
        this.keyFingerprint = bytesToLong;
        final TLRPC.TL_phone_confirmCall tl_phone_confirmCall = new TLRPC.TL_phone_confirmCall();
        tl_phone_confirmCall.g_a = this.g_a;
        tl_phone_confirmCall.key_fingerprint = bytesToLong;
        tl_phone_confirmCall.peer = new TLRPC.TL_inputPhoneCall();
        final TLRPC.TL_inputPhoneCall peer = tl_phone_confirmCall.peer;
        final TLRPC.PhoneCall call = this.call;
        peer.id = call.id;
        peer.access_hash = call.access_hash;
        tl_phone_confirmCall.protocol = new TLRPC.TL_phoneCallProtocol();
        final TLRPC.TL_phoneCallProtocol protocol = tl_phone_confirmCall.protocol;
        protocol.max_layer = VoIPService.CALL_MAX_LAYER;
        protocol.min_layer = 65;
        protocol.udp_reflector = true;
        protocol.udp_p2p = true;
        ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_phone_confirmCall, new RequestDelegate() {
            @Override
            public void run(final TLObject tlObject, final TLRPC.TL_error tl_error) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tl_error != null) {
                            VoIPService.this.callFailed();
                        }
                        else {
                            VoIPService.this.call = ((TLRPC.TL_phone_phoneCall)tlObject).phone_call;
                            VoIPService.this.initiateActualEncryptedCall();
                        }
                    }
                });
            }
        });
    }
    
    private void startConnectingSound() {
        final int spPlayID = super.spPlayID;
        if (spPlayID != 0) {
            super.soundPool.stop(spPlayID);
        }
        super.spPlayID = super.soundPool.play(super.spConnectingId, 1.0f, 1.0f, 0, -1, 1.0f);
        if (super.spPlayID == 0) {
            AndroidUtilities.runOnUIThread(super.connectingSoundRunnable = new Runnable() {
                @Override
                public void run() {
                    if (VoIPBaseService.sharedInstance == null) {
                        return;
                    }
                    final VoIPService this$0 = VoIPService.this;
                    if (this$0.spPlayID == 0) {
                        this$0.spPlayID = this$0.soundPool.play(this$0.spConnectingId, 1.0f, 1.0f, 0, -1, 1.0f);
                    }
                    final VoIPService this$2 = VoIPService.this;
                    if (this$2.spPlayID == 0) {
                        AndroidUtilities.runOnUIThread(this, 100L);
                    }
                    else {
                        this$2.connectingSoundRunnable = null;
                    }
                }
            }, 100L);
        }
    }
    
    private void startOutgoingCall() {
        if (VoIPBaseService.USE_CONNECTION_SERVICE) {
            final CallConnection systemCallConnection = super.systemCallConnection;
            if (systemCallConnection != null) {
                systemCallConnection.setDialing();
            }
        }
        this.configureDeviceForCall();
        this.showNotification();
        this.startConnectingSound();
        this.dispatchStateChanged(14);
        AndroidUtilities.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didStartedCall, new Object[0]);
            }
        });
        Utilities.random.nextBytes(new byte[256]);
        final TLRPC.TL_messages_getDhConfig tl_messages_getDhConfig = new TLRPC.TL_messages_getDhConfig();
        tl_messages_getDhConfig.random_length = 256;
        final MessagesStorage instance = MessagesStorage.getInstance(super.currentAccount);
        tl_messages_getDhConfig.version = instance.getLastSecretVersion();
        this.callReqId = ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_messages_getDhConfig, new RequestDelegate() {
            final /* synthetic */ VoIPService this$0;
            
            @Override
            public void run(final TLObject tlObject, final TLRPC.TL_error obj) {
                VoIPService.this.callReqId = 0;
                if (VoIPService.this.endCallAfterRequest) {
                    VoIPService.this.callEnded();
                    return;
                }
                if (obj == null) {
                    final TLRPC.messages_DhConfig messages_DhConfig = (TLRPC.messages_DhConfig)tlObject;
                    if (tlObject instanceof TLRPC.TL_messages_dhConfig) {
                        if (!Utilities.isGoodPrime(messages_DhConfig.p, messages_DhConfig.g)) {
                            VoIPService.this.callFailed();
                            return;
                        }
                        instance.setSecretPBytes(messages_DhConfig.p);
                        instance.setSecretG(messages_DhConfig.g);
                        instance.setLastSecretVersion(messages_DhConfig.version);
                        final MessagesStorage val$messagesStorage = instance;
                        val$messagesStorage.saveSecretParams(val$messagesStorage.getLastSecretVersion(), instance.getSecretG(), instance.getSecretPBytes());
                    }
                    final byte[] magnitude = new byte[256];
                    for (int i = 0; i < 256; ++i) {
                        magnitude[i] = (byte)((byte)(Utilities.random.nextDouble() * 256.0) ^ messages_DhConfig.random[i]);
                    }
                    byte[] byteArray;
                    final byte[] array = byteArray = BigInteger.valueOf(instance.getSecretG()).modPow(new BigInteger(1, magnitude), new BigInteger(1, instance.getSecretPBytes())).toByteArray();
                    if (array.length > 256) {
                        byteArray = new byte[256];
                        System.arraycopy(array, 1, byteArray, 0, 256);
                    }
                    final TLRPC.TL_phone_requestCall tl_phone_requestCall = new TLRPC.TL_phone_requestCall();
                    tl_phone_requestCall.user_id = MessagesController.getInstance(VoIPService.this.currentAccount).getInputUser(VoIPService.this.user);
                    tl_phone_requestCall.protocol = new TLRPC.TL_phoneCallProtocol();
                    final TLRPC.TL_phoneCallProtocol protocol = tl_phone_requestCall.protocol;
                    protocol.udp_p2p = true;
                    protocol.udp_reflector = true;
                    protocol.min_layer = 65;
                    protocol.max_layer = VoIPService.CALL_MAX_LAYER;
                    VoIPService.this.g_a = byteArray;
                    tl_phone_requestCall.g_a_hash = Utilities.computeSHA256(byteArray, 0, byteArray.length);
                    tl_phone_requestCall.random_id = Utilities.random.nextInt();
                    ConnectionsManager.getInstance(VoIPService.this.currentAccount).sendRequest(tl_phone_requestCall, new RequestDelegate() {
                        final /* synthetic */ VoIPService$4 this$1;
                        
                        @Override
                        public void run(final TLObject tlObject, final TLRPC.TL_error tl_error) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    final TLRPC.TL_error val$error = tl_error;
                                    if (val$error == null) {
                                        VoIPService.this.call = ((TLRPC.TL_phone_phoneCall)tlObject).phone_call;
                                        final RequestDelegate this$2 = RequestDelegate.this;
                                        this$2.this$1.this$0.a_or_b = magnitude;
                                        VoIPService.this.dispatchStateChanged(13);
                                        if (VoIPService.this.endCallAfterRequest) {
                                            VoIPService.this.hangUp();
                                            return;
                                        }
                                        if (VoIPService.this.pendingUpdates.size() > 0 && VoIPService.this.call != null) {
                                            final Iterator<TLRPC.PhoneCall> iterator = VoIPService.this.pendingUpdates.iterator();
                                            while (iterator.hasNext()) {
                                                VoIPService.this.onCallUpdated(iterator.next());
                                            }
                                            VoIPService.this.pendingUpdates.clear();
                                        }
                                        VoIPService.this.timeoutRunnable = new Runnable() {
                                            @Override
                                            public void run() {
                                                VoIPService.this.timeoutRunnable = null;
                                                final TLRPC.TL_phone_discardCall tl_phone_discardCall = new TLRPC.TL_phone_discardCall();
                                                tl_phone_discardCall.peer = new TLRPC.TL_inputPhoneCall();
                                                tl_phone_discardCall.peer.access_hash = VoIPService.this.call.access_hash;
                                                tl_phone_discardCall.peer.id = VoIPService.this.call.id;
                                                tl_phone_discardCall.reason = new TLRPC.TL_phoneCallDiscardReasonMissed();
                                                ConnectionsManager.getInstance(VoIPService.this.currentAccount).sendRequest(tl_phone_discardCall, new RequestDelegate() {
                                                    @Override
                                                    public void run(final TLObject obj, final TLRPC.TL_error obj2) {
                                                        if (BuildVars.LOGS_ENABLED) {
                                                            if (obj2 != null) {
                                                                final StringBuilder sb = new StringBuilder();
                                                                sb.append("error on phone.discardCall: ");
                                                                sb.append(obj2);
                                                                FileLog.e(sb.toString());
                                                            }
                                                            else {
                                                                final StringBuilder sb2 = new StringBuilder();
                                                                sb2.append("phone.discardCall ");
                                                                sb2.append(obj);
                                                                FileLog.d(sb2.toString());
                                                            }
                                                        }
                                                        AndroidUtilities.runOnUIThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                VoIPService.this.callFailed();
                                                            }
                                                        });
                                                    }
                                                }, 2);
                                            }
                                        };
                                        final VoIPService this$3 = VoIPService.this;
                                        AndroidUtilities.runOnUIThread(this$3.timeoutRunnable, MessagesController.getInstance(this$3.currentAccount).callReceiveTimeout);
                                    }
                                    else if (val$error.code == 400 && "PARTICIPANT_VERSION_OUTDATED".equals(val$error.text)) {
                                        VoIPService.this.callFailed(-1);
                                    }
                                    else {
                                        final int code = tl_error.code;
                                        if (code == 403) {
                                            VoIPService.this.callFailed(-2);
                                        }
                                        else if (code == 406) {
                                            VoIPService.this.callFailed(-3);
                                        }
                                        else {
                                            if (BuildVars.LOGS_ENABLED) {
                                                final StringBuilder sb = new StringBuilder();
                                                sb.append("Error on phone.requestCall: ");
                                                sb.append(tl_error);
                                                FileLog.e(sb.toString());
                                            }
                                            VoIPService.this.callFailed();
                                        }
                                    }
                                }
                            });
                        }
                    }, 2);
                }
                else {
                    if (BuildVars.LOGS_ENABLED) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Error on getDhConfig ");
                        sb.append(obj);
                        FileLog.e(sb.toString());
                    }
                    VoIPService.this.callFailed();
                }
            }
        }, 2);
    }
    
    private void startRatingActivity() {
        try {
            PendingIntent.getActivity((Context)this, 0, new Intent((Context)this, (Class)VoIPFeedbackActivity.class).putExtra("call_id", this.call.id).putExtra("call_access_hash", this.call.access_hash).putExtra("account", super.currentAccount).addFlags(805306368), 0).send();
        }
        catch (Exception ex) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Error starting incall activity", ex);
            }
        }
    }
    
    @Override
    public void acceptIncomingCall() {
        this.stopRinging();
        this.showNotification();
        this.configureDeviceForCall();
        this.startConnectingSound();
        this.dispatchStateChanged(12);
        AndroidUtilities.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didStartedCall, new Object[0]);
            }
        });
        final MessagesStorage instance = MessagesStorage.getInstance(super.currentAccount);
        final TLRPC.TL_messages_getDhConfig tl_messages_getDhConfig = new TLRPC.TL_messages_getDhConfig();
        tl_messages_getDhConfig.random_length = 256;
        tl_messages_getDhConfig.version = instance.getLastSecretVersion();
        ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_messages_getDhConfig, new RequestDelegate() {
            @Override
            public void run(final TLObject tlObject, final TLRPC.TL_error tl_error) {
                if (tl_error == null) {
                    final TLRPC.messages_DhConfig messages_DhConfig = (TLRPC.messages_DhConfig)tlObject;
                    if (tlObject instanceof TLRPC.TL_messages_dhConfig) {
                        if (!Utilities.isGoodPrime(messages_DhConfig.p, messages_DhConfig.g)) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.e("stopping VoIP service, bad prime");
                            }
                            VoIPService.this.callFailed();
                            return;
                        }
                        instance.setSecretPBytes(messages_DhConfig.p);
                        instance.setSecretG(messages_DhConfig.g);
                        instance.setLastSecretVersion(messages_DhConfig.version);
                        MessagesStorage.getInstance(VoIPService.this.currentAccount).saveSecretParams(instance.getLastSecretVersion(), instance.getSecretG(), instance.getSecretPBytes());
                    }
                    final byte[] magnitude = new byte[256];
                    for (int i = 0; i < 256; ++i) {
                        magnitude[i] = (byte)((byte)(Utilities.random.nextDouble() * 256.0) ^ messages_DhConfig.random[i]);
                    }
                    if (VoIPService.this.call == null) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.e("call is null");
                        }
                        VoIPService.this.callFailed();
                        return;
                    }
                    VoIPService.this.a_or_b = magnitude;
                    final BigInteger modPow = BigInteger.valueOf(instance.getSecretG()).modPow(new BigInteger(1, magnitude), new BigInteger(1, instance.getSecretPBytes()));
                    final VoIPService this$0 = VoIPService.this;
                    this$0.g_a_hash = this$0.call.g_a_hash;
                    byte[] byteArray;
                    final byte[] array = byteArray = modPow.toByteArray();
                    if (array.length > 256) {
                        byteArray = new byte[256];
                        System.arraycopy(array, 1, byteArray, 0, 256);
                    }
                    final TLRPC.TL_phone_acceptCall tl_phone_acceptCall = new TLRPC.TL_phone_acceptCall();
                    tl_phone_acceptCall.g_b = byteArray;
                    tl_phone_acceptCall.peer = new TLRPC.TL_inputPhoneCall();
                    tl_phone_acceptCall.peer.id = VoIPService.this.call.id;
                    tl_phone_acceptCall.peer.access_hash = VoIPService.this.call.access_hash;
                    tl_phone_acceptCall.protocol = new TLRPC.TL_phoneCallProtocol();
                    final TLRPC.TL_phoneCallProtocol protocol = tl_phone_acceptCall.protocol;
                    protocol.udp_reflector = true;
                    protocol.udp_p2p = true;
                    protocol.min_layer = 65;
                    protocol.max_layer = VoIPService.CALL_MAX_LAYER;
                    ConnectionsManager.getInstance(VoIPService.this.currentAccount).sendRequest(tl_phone_acceptCall, new RequestDelegate() {
                        @Override
                        public void run(final TLObject tlObject, final TLRPC.TL_error tl_error) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (tl_error == null) {
                                        if (BuildVars.LOGS_ENABLED) {
                                            final StringBuilder sb = new StringBuilder();
                                            sb.append("accept call ok! ");
                                            sb.append(tlObject);
                                            FileLog.w(sb.toString());
                                        }
                                        VoIPService.this.call = ((TLRPC.TL_phone_phoneCall)tlObject).phone_call;
                                        if (VoIPService.this.call instanceof TLRPC.TL_phoneCallDiscarded) {
                                            final VoIPService this$0 = VoIPService.this;
                                            this$0.onCallUpdated(this$0.call);
                                        }
                                    }
                                    else {
                                        if (BuildVars.LOGS_ENABLED) {
                                            final StringBuilder sb2 = new StringBuilder();
                                            sb2.append("Error on phone.acceptCall: ");
                                            sb2.append(tl_error);
                                            FileLog.e(sb2.toString());
                                        }
                                        VoIPService.this.callFailed();
                                    }
                                }
                            });
                        }
                    }, 2);
                }
                else {
                    VoIPService.this.callFailed();
                }
            }
        });
    }
    
    @Override
    protected void callFailed(final int n) {
        if (this.call != null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("Discarding failed call");
            }
            final TLRPC.TL_phone_discardCall tl_phone_discardCall = new TLRPC.TL_phone_discardCall();
            tl_phone_discardCall.peer = new TLRPC.TL_inputPhoneCall();
            final TLRPC.TL_inputPhoneCall peer = tl_phone_discardCall.peer;
            final TLRPC.PhoneCall call = this.call;
            peer.access_hash = call.access_hash;
            peer.id = call.id;
            final VoIPController controller = super.controller;
            int duration;
            if (controller != null && super.controllerStarted) {
                duration = (int)(controller.getCallDuration() / 1000L);
            }
            else {
                duration = 0;
            }
            tl_phone_discardCall.duration = duration;
            final VoIPController controller2 = super.controller;
            long preferredRelayID;
            if (controller2 != null && super.controllerStarted) {
                preferredRelayID = controller2.getPreferredRelayID();
            }
            else {
                preferredRelayID = 0L;
            }
            tl_phone_discardCall.connection_id = preferredRelayID;
            tl_phone_discardCall.reason = new TLRPC.TL_phoneCallDiscardReasonDisconnect();
            ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_phone_discardCall, new RequestDelegate() {
                @Override
                public void run(final TLObject obj, final TLRPC.TL_error obj2) {
                    if (obj2 != null) {
                        if (BuildVars.LOGS_ENABLED) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("error on phone.discardCall: ");
                            sb.append(obj2);
                            FileLog.e(sb.toString());
                        }
                    }
                    else if (BuildVars.LOGS_ENABLED) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("phone.discardCall ");
                        sb2.append(obj);
                        FileLog.d(sb2.toString());
                    }
                }
            });
        }
        super.callFailed(n);
    }
    
    public boolean canUpgrate() {
        final int peerCapabilities = this.peerCapabilities;
        boolean b = true;
        if ((peerCapabilities & 0x1) != 0x1) {
            b = false;
        }
        return b;
    }
    
    public void debugCtl(final int n, final int n2) {
        final VoIPController controller = super.controller;
        if (controller != null) {
            controller.debugCtl(n, n2);
        }
    }
    
    @Override
    public void declineIncomingCall() {
        this.declineIncomingCall(1, null);
    }
    
    @Override
    public void declineIncomingCall(final int callDiscardReason, Runnable delayedStartOutgoingCall) {
        this.stopRinging();
        super.callDiscardReason = callDiscardReason;
        final int currentState = super.currentState;
        boolean b = true;
        if (currentState == 14) {
            delayedStartOutgoingCall = this.delayedStartOutgoingCall;
            if (delayedStartOutgoingCall != null) {
                AndroidUtilities.cancelRunOnUIThread(delayedStartOutgoingCall);
                this.callEnded();
            }
            else {
                this.dispatchStateChanged(10);
                this.endCallAfterRequest = true;
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        final VoIPService this$0 = VoIPService.this;
                        if (this$0.currentState == 10) {
                            this$0.callEnded();
                        }
                    }
                }, 5000L);
            }
            return;
        }
        if (currentState != 10) {
            if (currentState != 11) {
                this.dispatchStateChanged(10);
                if (this.call == null) {
                    if (delayedStartOutgoingCall != null) {
                        delayedStartOutgoingCall.run();
                    }
                    this.callEnded();
                    if (this.callReqId != 0) {
                        ConnectionsManager.getInstance(super.currentAccount).cancelRequest(this.callReqId, false);
                        this.callReqId = 0;
                    }
                    return;
                }
                final TLRPC.TL_phone_discardCall tl_phone_discardCall = new TLRPC.TL_phone_discardCall();
                tl_phone_discardCall.peer = new TLRPC.TL_inputPhoneCall();
                final TLRPC.TL_inputPhoneCall peer = tl_phone_discardCall.peer;
                final TLRPC.PhoneCall call = this.call;
                peer.access_hash = call.access_hash;
                peer.id = call.id;
                final VoIPController controller = super.controller;
                int duration;
                if (controller != null && super.controllerStarted) {
                    duration = (int)(controller.getCallDuration() / 1000L);
                }
                else {
                    duration = 0;
                }
                tl_phone_discardCall.duration = duration;
                final VoIPController controller2 = super.controller;
                long preferredRelayID;
                if (controller2 != null && super.controllerStarted) {
                    preferredRelayID = controller2.getPreferredRelayID();
                }
                else {
                    preferredRelayID = 0L;
                }
                tl_phone_discardCall.connection_id = preferredRelayID;
                if (callDiscardReason != 2) {
                    if (callDiscardReason != 3) {
                        if (callDiscardReason != 4) {
                            tl_phone_discardCall.reason = new TLRPC.TL_phoneCallDiscardReasonHangup();
                        }
                        else {
                            tl_phone_discardCall.reason = new TLRPC.TL_phoneCallDiscardReasonBusy();
                        }
                    }
                    else {
                        tl_phone_discardCall.reason = new TLRPC.TL_phoneCallDiscardReasonMissed();
                    }
                }
                else {
                    tl_phone_discardCall.reason = new TLRPC.TL_phoneCallDiscardReasonDisconnect();
                }
                if (ConnectionsManager.getInstance(super.currentAccount).getConnectionState() == 3) {
                    b = false;
                }
                Runnable runnable;
                if (b) {
                    if (delayedStartOutgoingCall != null) {
                        delayedStartOutgoingCall.run();
                    }
                    this.callEnded();
                    runnable = null;
                }
                else {
                    runnable = new Runnable() {
                        private boolean done = false;
                        
                        @Override
                        public void run() {
                            if (this.done) {
                                return;
                            }
                            this.done = true;
                            final Runnable val$onDone = delayedStartOutgoingCall;
                            if (val$onDone != null) {
                                val$onDone.run();
                            }
                            VoIPService.this.callEnded();
                        }
                    };
                    AndroidUtilities.runOnUIThread(runnable, (int)(VoIPServerConfig.getDouble("hangup_ui_timeout", 5.0) * 1000.0));
                }
                ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_phone_discardCall, new RequestDelegate() {
                    @Override
                    public void run(final TLObject obj, final TLRPC.TL_error obj2) {
                        if (obj2 != null) {
                            if (BuildVars.LOGS_ENABLED) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("error on phone.discardCall: ");
                                sb.append(obj2);
                                FileLog.e(sb.toString());
                            }
                        }
                        else {
                            if (obj instanceof TLRPC.TL_updates) {
                                MessagesController.getInstance(VoIPService.this.currentAccount).processUpdates((TLRPC.Updates)obj, false);
                            }
                            if (BuildVars.LOGS_ENABLED) {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("phone.discardCall ");
                                sb2.append(obj);
                                FileLog.d(sb2.toString());
                            }
                        }
                        if (!b) {
                            AndroidUtilities.cancelRunOnUIThread(runnable);
                            final Runnable val$onDone = delayedStartOutgoingCall;
                            if (val$onDone != null) {
                                val$onDone.run();
                            }
                        }
                    }
                }, 2);
            }
        }
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.appDidLogout) {
            this.callEnded();
        }
    }
    
    public void forceRating() {
        this.forceRating = true;
    }
    
    @Override
    public long getCallID() {
        final TLRPC.PhoneCall call = this.call;
        long id;
        if (call != null) {
            id = call.id;
        }
        else {
            id = 0L;
        }
        return id;
    }
    
    @TargetApi(26)
    @Override
    public CallConnection getConnectionAndStartCall() {
        if (super.systemCallConnection == null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("creating call connection");
            }
            (super.systemCallConnection = new CallConnection()).setInitializing();
            if (super.isOutgoing) {
                AndroidUtilities.runOnUIThread(this.delayedStartOutgoingCall = new Runnable() {
                    @Override
                    public void run() {
                        VoIPService.this.delayedStartOutgoingCall = null;
                        VoIPService.this.startOutgoingCall();
                    }
                }, 2000L);
            }
            final CallConnection systemCallConnection = super.systemCallConnection;
            final StringBuilder sb = new StringBuilder();
            sb.append("+99084");
            sb.append(this.user.id);
            systemCallConnection.setAddress(Uri.fromParts("tel", sb.toString(), (String)null), 1);
            final CallConnection systemCallConnection2 = super.systemCallConnection;
            final TLRPC.User user = this.user;
            systemCallConnection2.setCallerDisplayName(ContactsController.formatName(user.first_name, user.last_name), 1);
        }
        return super.systemCallConnection;
    }
    
    public byte[] getEncryptionKey() {
        return this.authKey;
    }
    
    public byte[] getGA() {
        return this.g_a;
    }
    
    @Override
    protected Class<? extends Activity> getUIActivityClass() {
        return VoIPActivity.class;
    }
    
    public TLRPC.User getUser() {
        return this.user;
    }
    
    @Override
    public void hangUp() {
        final int currentState = super.currentState;
        int n;
        if (currentState != 16 && (currentState != 13 || !super.isOutgoing)) {
            n = 1;
        }
        else {
            n = 3;
        }
        this.declineIncomingCall(n, null);
    }
    
    @Override
    public void hangUp(final Runnable runnable) {
        final int currentState = super.currentState;
        int n;
        if (currentState != 16 && (currentState != 13 || !super.isOutgoing)) {
            n = 1;
        }
        else {
            n = 3;
        }
        this.declineIncomingCall(n, runnable);
    }
    
    @Override
    protected boolean isRinging() {
        return super.currentState == 15;
    }
    
    public IBinder onBind(final Intent intent) {
        return null;
    }
    
    public void onCallUpdated(final TLRPC.PhoneCall call) {
        final TLRPC.PhoneCall call2 = this.call;
        if (call2 == null) {
            this.pendingUpdates.add(call);
            return;
        }
        if (call == null) {
            return;
        }
        if (call.id != call2.id) {
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onCallUpdated called with wrong call id (got ");
                sb.append(call.id);
                sb.append(", expected ");
                sb.append(this.call.id);
                sb.append(")");
                FileLog.w(sb.toString());
            }
            return;
        }
        if (call.access_hash == 0L) {
            call.access_hash = call2.access_hash;
        }
        if (BuildVars.LOGS_ENABLED) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Call updated: ");
            sb2.append(call);
            FileLog.d(sb2.toString());
            this.dumpCallObject();
        }
        this.call = call;
        if (call instanceof TLRPC.TL_phoneCallDiscarded) {
            this.needSendDebugLog = call.need_debug;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("call discarded, stopping service");
            }
            if (call.reason instanceof TLRPC.TL_phoneCallDiscardReasonBusy) {
                this.dispatchStateChanged(17);
                super.playingSound = true;
                super.soundPool.play(super.spBusyId, 1.0f, 1.0f, 0, -1, 1.0f);
                AndroidUtilities.runOnUIThread(super.afterSoundRunnable, 1500L);
                this.endConnectionServiceCall(1500L);
                this.stopSelf();
            }
            else {
                this.callEnded();
            }
            if (call.need_rating || this.forceRating || (super.controller != null && VoIPServerConfig.getBoolean("bad_call_rating", true) && super.controller.needRate())) {
                this.startRatingActivity();
            }
            if (this.debugLog == null) {
                final VoIPController controller = super.controller;
                if (controller != null) {
                    this.debugLog = controller.getDebugLog();
                }
            }
            if (this.needSendDebugLog && this.debugLog != null) {
                final TLRPC.TL_phone_saveCallDebug tl_phone_saveCallDebug = new TLRPC.TL_phone_saveCallDebug();
                tl_phone_saveCallDebug.debug = new TLRPC.TL_dataJSON();
                tl_phone_saveCallDebug.debug.data = this.debugLog;
                tl_phone_saveCallDebug.peer = new TLRPC.TL_inputPhoneCall();
                final TLRPC.TL_inputPhoneCall peer = tl_phone_saveCallDebug.peer;
                peer.access_hash = call.access_hash;
                peer.id = call.id;
                ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_phone_saveCallDebug, new RequestDelegate() {
                    @Override
                    public void run(final TLObject obj, final TLRPC.TL_error tl_error) {
                        if (BuildVars.LOGS_ENABLED) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Sent debug logs, response=");
                            sb.append(obj);
                            FileLog.d(sb.toString());
                        }
                    }
                });
            }
        }
        else if (call instanceof TLRPC.TL_phoneCall && this.authKey == null) {
            final byte[] g_a_or_b = call.g_a_or_b;
            if (g_a_or_b == null) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.w("stopping VoIP service, Ga == null");
                }
                this.callFailed();
                return;
            }
            if (!Arrays.equals(this.g_a_hash, Utilities.computeSHA256(g_a_or_b, 0, g_a_or_b.length))) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.w("stopping VoIP service, Ga hash doesn't match");
                }
                this.callFailed();
                return;
            }
            final byte[] g_a_or_b2 = call.g_a_or_b;
            this.g_a = g_a_or_b2;
            final BigInteger bigInteger = new BigInteger(1, g_a_or_b2);
            final BigInteger m = new BigInteger(1, MessagesStorage.getInstance(super.currentAccount).getSecretPBytes());
            if (!Utilities.isGoodGaAndGb(bigInteger, m)) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.w("stopping VoIP service, bad Ga and Gb (accepting)");
                }
                this.callFailed();
                return;
            }
            final byte[] byteArray = bigInteger.modPow(new BigInteger(1, this.a_or_b), m).toByteArray();
            byte[] authKey;
            if (byteArray.length > 256) {
                authKey = new byte[256];
                System.arraycopy(byteArray, byteArray.length - 256, authKey, 0, 256);
            }
            else if (byteArray.length < 256) {
                final byte[] array = new byte[256];
                System.arraycopy(byteArray, 0, array, 256 - byteArray.length, byteArray.length);
                int n = 0;
                while (true) {
                    authKey = array;
                    if (n >= 256 - byteArray.length) {
                        break;
                    }
                    array[n] = 0;
                    ++n;
                }
            }
            else {
                authKey = byteArray;
            }
            final byte[] computeSHA1 = Utilities.computeSHA1(authKey);
            final byte[] array2 = new byte[8];
            System.arraycopy(computeSHA1, computeSHA1.length - 8, array2, 0, 8);
            this.authKey = authKey;
            this.keyFingerprint = Utilities.bytesToLong(array2);
            if (this.keyFingerprint != call.key_fingerprint) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.w("key fingerprints don't match");
                }
                this.callFailed();
                return;
            }
            this.initiateActualEncryptedCall();
        }
        else if (call instanceof TLRPC.TL_phoneCallAccepted && this.authKey == null) {
            this.processAcceptedCall();
        }
        else if (super.currentState == 13 && call.receive_date != 0) {
            this.dispatchStateChanged(16);
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("!!!!!! CALL RECEIVED");
            }
            final Runnable connectingSoundRunnable = super.connectingSoundRunnable;
            if (connectingSoundRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(connectingSoundRunnable);
                super.connectingSoundRunnable = null;
            }
            final int spPlayID = super.spPlayID;
            if (spPlayID != 0) {
                super.soundPool.stop(spPlayID);
            }
            super.spPlayID = super.soundPool.play(super.spRingbackID, 1.0f, 1.0f, 0, -1, 1.0f);
            final Runnable timeoutRunnable = super.timeoutRunnable;
            if (timeoutRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(timeoutRunnable);
                super.timeoutRunnable = null;
            }
            AndroidUtilities.runOnUIThread(super.timeoutRunnable = new Runnable() {
                @Override
                public void run() {
                    final VoIPService this$0 = VoIPService.this;
                    this$0.declineIncomingCall(3, this$0.timeoutRunnable = null);
                }
            }, MessagesController.getInstance(super.currentAccount).callRingTimeout);
        }
    }
    
    public void onCallUpgradeRequestReceived() {
        this.upgradeToGroupCall(new ArrayList<Integer>());
    }
    
    @Override
    public void onConnectionStateChanged(final int n) {
        if (n == 3) {
            this.peerCapabilities = super.controller.getPeerCapabilities();
        }
        super.onConnectionStateChanged(n);
    }
    
    @Override
    protected void onControllerPreRelease() {
        if (this.debugLog == null) {
            this.debugLog = super.controller.getDebugLog();
        }
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        if (VoIPService.callIShouldHavePutIntoIntent != null && Build$VERSION.SDK_INT >= 26) {
            NotificationsController.checkOtherNotificationsChannel();
            this.startForeground(201, new Notification$Builder((Context)this, NotificationsController.OTHER_NOTIFICATIONS_CHANNEL).setSmallIcon(2131165698).setContentTitle((CharSequence)LocaleController.getString("VoipOutgoingCall", 2131561083)).setShowWhen(false).build());
        }
    }
    
    public void onGroupCallKeyReceived(byte[] groupCallEncryptionKey) {
        this.joiningGroupCall = true;
        this.groupCallEncryptionKey = groupCallEncryptionKey;
        final byte[] computeSHA1 = Utilities.computeSHA1(this.groupCallEncryptionKey);
        groupCallEncryptionKey = new byte[8];
        System.arraycopy(computeSHA1, computeSHA1.length - 8, groupCallEncryptionKey, 0, 8);
        this.groupCallKeyFingerprint = Utilities.bytesToLong(groupCallEncryptionKey);
    }
    
    public void onGroupCallKeySent() {
        final boolean isOutgoing = super.isOutgoing;
    }
    
    void onMediaButtonEvent(final KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 79 || keyEvent.getKeyCode() == 127 || keyEvent.getKeyCode() == 85) {
            final int action = keyEvent.getAction();
            boolean micMute = true;
            if (action == 1) {
                if (super.currentState == 15) {
                    this.acceptIncomingCall();
                }
                else {
                    if (this.isMicMute()) {
                        micMute = false;
                    }
                    this.setMicMute(micMute);
                    final Iterator<StateListener> iterator = super.stateListeners.iterator();
                    while (iterator.hasNext()) {
                        ((StateListener)iterator.next()).onAudioSettingsChanged();
                    }
                }
            }
        }
    }
    
    @SuppressLint({ "MissingPermission" })
    public int onStartCommand(final Intent intent, int intExtra, final int n) {
        if (VoIPBaseService.sharedInstance != null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Tried to start the VoIP service when it's already started");
            }
            return 2;
        }
        super.currentAccount = intent.getIntExtra("account", -1);
        if (super.currentAccount == -1) {
            throw new IllegalStateException("No account specified when starting VoIP service");
        }
        intExtra = intent.getIntExtra("user_id", 0);
        super.isOutgoing = intent.getBooleanExtra("is_outgoing", false);
        this.user = MessagesController.getInstance(super.currentAccount).getUser(intExtra);
        if (this.user == null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.w("VoIPService: user==null");
            }
            this.stopSelf();
            return 2;
        }
        VoIPBaseService.sharedInstance = this;
        if (super.isOutgoing) {
            this.dispatchStateChanged(14);
            if (VoIPBaseService.USE_CONNECTION_SERVICE) {
                final TelecomManager telecomManager = (TelecomManager)this.getSystemService("telecom");
                final Bundle bundle = new Bundle();
                final Bundle bundle2 = new Bundle();
                bundle.putParcelable("android.telecom.extra.PHONE_ACCOUNT_HANDLE", (Parcelable)this.addAccountToTelecomManager());
                bundle2.putInt("call_type", 1);
                bundle.putBundle("android.telecom.extra.OUTGOING_CALL_EXTRAS", bundle2);
                final ContactsController instance = ContactsController.getInstance(super.currentAccount);
                final TLRPC.User user = this.user;
                instance.createOrUpdateConnectionServiceContact(user.id, user.first_name, user.last_name);
                final StringBuilder sb = new StringBuilder();
                sb.append("+99084");
                sb.append(this.user.id);
                telecomManager.placeCall(Uri.fromParts("tel", sb.toString(), (String)null), bundle);
            }
            else {
                AndroidUtilities.runOnUIThread(this.delayedStartOutgoingCall = new Runnable() {
                    @Override
                    public void run() {
                        VoIPService.this.delayedStartOutgoingCall = null;
                        VoIPService.this.startOutgoingCall();
                    }
                }, 2000L);
            }
            if (intent.getBooleanExtra("start_incall_activity", false)) {
                this.startActivity(new Intent((Context)this, (Class)VoIPActivity.class).addFlags(268435456));
            }
        }
        else {
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.closeInCallActivity, new Object[0]);
            this.call = VoIPService.callIShouldHavePutIntoIntent;
            VoIPService.callIShouldHavePutIntoIntent = null;
            if (VoIPBaseService.USE_CONNECTION_SERVICE) {
                this.acknowledgeCall(false);
                this.showNotification();
            }
            else {
                this.acknowledgeCall(true);
            }
        }
        this.initializeAccountRelatedThings();
        return 2;
    }
    
    public void onUIForegroundStateChanged(final boolean b) {
        if (Build$VERSION.SDK_INT >= 21) {
            return;
        }
        if (super.currentState == 15) {
            if (b) {
                this.stopForeground(true);
            }
            else if (!((KeyguardManager)this.getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
                if (NotificationManagerCompat.from((Context)this).areNotificationsEnabled()) {
                    final TLRPC.User user = this.user;
                    this.showIncomingNotification(ContactsController.formatName(user.first_name, user.last_name), null, this.user, null, 0, VoIPActivity.class);
                }
                else {
                    this.declineIncomingCall(4, null);
                }
            }
            else {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        final Intent intent = new Intent((Context)VoIPService.this, (Class)VoIPActivity.class);
                        intent.addFlags(805306368);
                        try {
                            PendingIntent.getActivity((Context)VoIPService.this, 0, intent, 0).send();
                        }
                        catch (PendingIntent$CanceledException ex) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.e("error restarting activity", (Throwable)ex);
                            }
                            VoIPService.this.declineIncomingCall(4, null);
                        }
                        if (Build$VERSION.SDK_INT >= 26) {
                            VoIPService.this.showNotification();
                        }
                    }
                }, 500L);
            }
        }
    }
    
    @Override
    protected void showNotification() {
        final TLRPC.User user = this.user;
        final String formatName = ContactsController.formatName(user.first_name, user.last_name);
        final TLRPC.UserProfilePhoto photo = this.user.photo;
        TLRPC.FileLocation photo_small;
        if (photo != null) {
            photo_small = photo.photo_small;
        }
        else {
            photo_small = null;
        }
        this.showNotification(formatName, photo_small, VoIPActivity.class);
    }
    
    @Override
    protected void startRinging() {
        if (super.currentState == 15) {
            return;
        }
        if (VoIPBaseService.USE_CONNECTION_SERVICE) {
            final CallConnection systemCallConnection = super.systemCallConnection;
            if (systemCallConnection != null) {
                systemCallConnection.setRinging();
            }
        }
        if (BuildVars.LOGS_ENABLED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("starting ringing for call ");
            sb.append(this.call.id);
            FileLog.d(sb.toString());
        }
        this.dispatchStateChanged(15);
        if (Build$VERSION.SDK_INT >= 21) {
            final TLRPC.User user = this.user;
            this.showIncomingNotification(ContactsController.formatName(user.first_name, user.last_name), null, this.user, null, 0, VoIPActivity.class);
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("Showing incoming call notification");
            }
        }
        else {
            this.startRingtoneAndVibration(this.user.id);
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("Starting incall activity for incoming call");
            }
            try {
                PendingIntent.getActivity((Context)this, 12345, new Intent((Context)this, (Class)VoIPActivity.class).addFlags(268435456), 0).send();
            }
            catch (Exception ex) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("Error starting incall activity", ex);
                }
            }
        }
    }
    
    @Override
    public void startRingtoneAndVibration() {
        if (!this.startedRinging) {
            this.startRingtoneAndVibration(this.user.id);
            this.startedRinging = true;
        }
    }
    
    @Override
    protected void updateServerConfig() {
        final SharedPreferences mainSettings = MessagesController.getMainSettings(super.currentAccount);
        VoIPServerConfig.setConfig(mainSettings.getString("voip_server_config", "{}"));
        ConnectionsManager.getInstance(super.currentAccount).sendRequest(new TLRPC.TL_phone_getCallConfig(), new RequestDelegate() {
            @Override
            public void run(final TLObject tlObject, final TLRPC.TL_error tl_error) {
                if (tl_error == null) {
                    final String data = ((TLRPC.TL_dataJSON)tlObject).data;
                    VoIPServerConfig.setConfig(data);
                    mainSettings.edit().putString("voip_server_config", data).commit();
                }
            }
        });
    }
    
    public void upgradeToGroupCall(final List<Integer> groupUsersToAdd) {
        if (this.upgrading) {
            return;
        }
        this.groupUsersToAdd = groupUsersToAdd;
        if (!super.isOutgoing) {
            super.controller.requestCallUpgrade();
            return;
        }
        this.upgrading = true;
        this.groupCallEncryptionKey = new byte[256];
        Utilities.random.nextBytes(this.groupCallEncryptionKey);
        final byte[] groupCallEncryptionKey = this.groupCallEncryptionKey;
        groupCallEncryptionKey[0] &= 0x7F;
        final byte[] computeSHA1 = Utilities.computeSHA1(groupCallEncryptionKey);
        final byte[] array = new byte[8];
        System.arraycopy(computeSHA1, computeSHA1.length - 8, array, 0, 8);
        this.groupCallKeyFingerprint = Utilities.bytesToLong(array);
        super.controller.sendGroupCallKey(this.groupCallEncryptionKey);
    }
}
