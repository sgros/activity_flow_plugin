// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.voip;

import android.os.Bundle;
import android.telecom.CallAudioState;
import android.telecom.Connection;
import android.net.ConnectivityManager;
import android.view.ViewGroup;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.BottomSheet;
import android.media.RingtoneManager;
import android.media.MediaPlayer$OnPreparedListener;
import org.telegram.messenger.NotificationsController;
import android.media.AudioAttributes;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.SpannableString;
import android.app.NotificationChannel;
import android.media.AudioAttributes$Builder;
import android.app.NotificationManager;
import android.net.Uri;
import org.telegram.messenger.LocaleController;
import android.app.Notification$Builder;
import java.util.List;
import android.annotation.SuppressLint;
import android.hardware.SensorEvent;
import org.telegram.ui.Components.voip.VoIPHelper;
import android.content.IntentFilter;
import android.media.AudioTrack;
import org.telegram.messenger.StatsController;
import org.telegram.tgnet.ConnectionsManager;
import java.lang.reflect.Method;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff$Mode;
import android.graphics.Paint;
import android.graphics.Path$Direction;
import android.graphics.Path;
import android.graphics.Canvas;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.Bitmap$Config;
import android.graphics.BitmapFactory;
import org.telegram.messenger.FileLoader;
import android.graphics.BitmapFactory$Options;
import org.telegram.messenger.ImageLoader;
import android.graphics.Bitmap;
import org.telegram.tgnet.TLObject;
import android.hardware.Sensor;
import android.os.PowerManager;
import android.hardware.SensorManager;
import android.telecom.DisconnectCause;
import org.telegram.messenger.AndroidUtilities;
import android.annotation.TargetApi;
import org.telegram.tgnet.TLRPC;
import android.graphics.drawable.Icon;
import android.telecom.PhoneAccount$Builder;
import org.telegram.messenger.ContactsController;
import android.content.ComponentName;
import org.telegram.messenger.UserConfig;
import android.telecom.TelecomManager;
import android.telecom.PhoneAccountHandle;
import org.telegram.messenger.MessagesController;
import android.os.Build;
import android.app.PendingIntent;
import org.telegram.ui.VoIPPermissionActivity;
import android.os.Build$VERSION;
import java.util.Iterator;
import android.telephony.TelephonyManager;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.BuildVars;
import android.content.Intent;
import android.content.Context;
import org.telegram.messenger.ApplicationLoader;
import android.media.AudioManager;
import android.os.Vibrator;
import java.util.ArrayList;
import android.media.SoundPool;
import android.media.MediaPlayer;
import android.content.BroadcastReceiver;
import android.app.Notification;
import android.net.NetworkInfo;
import android.os.PowerManager$WakeLock;
import android.bluetooth.BluetoothAdapter;
import org.telegram.messenger.NotificationCenter;
import android.media.AudioManager$OnAudioFocusChangeListener;
import android.hardware.SensorEventListener;
import android.app.Service;

public abstract class VoIPBaseService extends Service implements SensorEventListener, AudioManager$OnAudioFocusChangeListener, ConnectionStateListener, NotificationCenterDelegate
{
    public static final String ACTION_HEADSET_PLUG = "android.intent.action.HEADSET_PLUG";
    public static final int AUDIO_ROUTE_BLUETOOTH = 2;
    public static final int AUDIO_ROUTE_EARPIECE = 0;
    public static final int AUDIO_ROUTE_SPEAKER = 1;
    public static final int DISCARD_REASON_DISCONNECT = 2;
    public static final int DISCARD_REASON_HANGUP = 1;
    public static final int DISCARD_REASON_LINE_BUSY = 4;
    public static final int DISCARD_REASON_MISSED = 3;
    protected static final int ID_INCOMING_CALL_NOTIFICATION = 202;
    protected static final int ID_ONGOING_CALL_NOTIFICATION = 201;
    protected static final int PROXIMITY_SCREEN_OFF_WAKE_LOCK = 32;
    public static final int STATE_ENDED = 11;
    public static final int STATE_ESTABLISHED = 3;
    public static final int STATE_FAILED = 4;
    public static final int STATE_RECONNECTING = 5;
    public static final int STATE_WAIT_INIT = 1;
    public static final int STATE_WAIT_INIT_ACK = 2;
    protected static final boolean USE_CONNECTION_SERVICE;
    protected static VoIPBaseService sharedInstance;
    protected Runnable afterSoundRunnable;
    protected boolean audioConfigured;
    protected int audioRouteToSet;
    protected boolean bluetoothScoActive;
    protected BluetoothAdapter btAdapter;
    protected int callDiscardReason;
    protected Runnable connectingSoundRunnable;
    protected VoIPController controller;
    protected boolean controllerStarted;
    protected PowerManager$WakeLock cpuWakelock;
    protected int currentAccount;
    protected int currentState;
    protected boolean didDeleteConnectionServiceContact;
    protected boolean haveAudioFocus;
    protected boolean isBtHeadsetConnected;
    protected boolean isHeadsetPlugged;
    protected boolean isOutgoing;
    protected boolean isProximityNear;
    protected int lastError;
    protected long lastKnownDuration;
    protected NetworkInfo lastNetInfo;
    private Boolean mHasEarpiece;
    protected boolean micMute;
    protected boolean needPlayEndSound;
    protected boolean needSwitchToBluetoothAfterScoActivates;
    protected Notification ongoingCallNotification;
    protected boolean playingSound;
    protected Stats prevStats;
    protected PowerManager$WakeLock proximityWakelock;
    protected BroadcastReceiver receiver;
    protected MediaPlayer ringtonePlayer;
    protected int signalBarCount;
    protected SoundPool soundPool;
    protected int spBusyId;
    protected int spConnectingId;
    protected int spEndId;
    protected int spFailedID;
    protected int spPlayID;
    protected int spRingbackID;
    protected boolean speakerphoneStateToSet;
    protected ArrayList<StateListener> stateListeners;
    protected Stats stats;
    protected CallConnection systemCallConnection;
    protected Runnable timeoutRunnable;
    protected Vibrator vibrator;
    private boolean wasEstablished;
    
    static {
        USE_CONNECTION_SERVICE = isDeviceCompatibleWithConnectionServiceAPI();
    }
    
    public VoIPBaseService() {
        this.currentAccount = -1;
        this.currentState = 0;
        this.stateListeners = new ArrayList<StateListener>();
        this.stats = new VoIPController.Stats();
        this.prevStats = new VoIPController.Stats();
        this.afterSoundRunnable = new Runnable() {
            @Override
            public void run() {
                VoIPBaseService.this.soundPool.release();
                if (VoIPBaseService.USE_CONNECTION_SERVICE) {
                    return;
                }
                if (VoIPBaseService.this.isBtHeadsetConnected) {
                    ((AudioManager)ApplicationLoader.applicationContext.getSystemService("audio")).stopBluetoothSco();
                }
                ((AudioManager)ApplicationLoader.applicationContext.getSystemService("audio")).setSpeakerphoneOn(false);
            }
        };
        this.lastKnownDuration = 0L;
        this.receiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final boolean equals = "android.intent.action.HEADSET_PLUG".equals(intent.getAction());
                final boolean b = true;
                boolean isHeadsetPlugged = true;
                if (equals) {
                    final VoIPBaseService this$0 = VoIPBaseService.this;
                    if (intent.getIntExtra("state", 0) != 1) {
                        isHeadsetPlugged = false;
                    }
                    this$0.isHeadsetPlugged = isHeadsetPlugged;
                    final VoIPBaseService this$2 = VoIPBaseService.this;
                    if (this$2.isHeadsetPlugged) {
                        final PowerManager$WakeLock proximityWakelock = this$2.proximityWakelock;
                        if (proximityWakelock != null && proximityWakelock.isHeld()) {
                            VoIPBaseService.this.proximityWakelock.release();
                        }
                    }
                    final VoIPBaseService this$3 = VoIPBaseService.this;
                    this$3.isProximityNear = false;
                    this$3.updateOutputGainControlState();
                }
                else if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                    VoIPBaseService.this.updateNetworkType();
                }
                else if ("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED".equals(intent.getAction())) {
                    if (BuildVars.LOGS_ENABLED) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("bt headset state = ");
                        sb.append(intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0));
                        FileLog.e(sb.toString());
                    }
                    VoIPBaseService.this.updateBluetoothHeadsetState(intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0) == 2 && b);
                }
                else if ("android.media.ACTION_SCO_AUDIO_STATE_UPDATED".equals(intent.getAction())) {
                    final int intExtra = intent.getIntExtra("android.media.extra.SCO_AUDIO_STATE", 0);
                    if (BuildVars.LOGS_ENABLED) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Bluetooth SCO state updated: ");
                        sb2.append(intExtra);
                        FileLog.e(sb2.toString());
                    }
                    if (intExtra == 0) {
                        final VoIPBaseService this$4 = VoIPBaseService.this;
                        if (this$4.isBtHeadsetConnected && (!this$4.btAdapter.isEnabled() || VoIPBaseService.this.btAdapter.getProfileConnectionState(1) != 2)) {
                            VoIPBaseService.this.updateBluetoothHeadsetState(false);
                            return;
                        }
                    }
                    VoIPBaseService.this.bluetoothScoActive = (intExtra == 1);
                    final VoIPBaseService this$5 = VoIPBaseService.this;
                    if (this$5.bluetoothScoActive && this$5.needSwitchToBluetoothAfterScoActivates) {
                        this$5.needSwitchToBluetoothAfterScoActivates = false;
                        final AudioManager audioManager = (AudioManager)this$5.getSystemService("audio");
                        audioManager.setSpeakerphoneOn(false);
                        audioManager.setBluetoothScoOn(true);
                    }
                    final Iterator<StateListener> iterator = VoIPBaseService.this.stateListeners.iterator();
                    while (iterator.hasNext()) {
                        ((StateListener)iterator.next()).onAudioSettingsChanged();
                    }
                }
                else if ("android.intent.action.PHONE_STATE".equals(intent.getAction()) && TelephonyManager.EXTRA_STATE_OFFHOOK.equals(intent.getStringExtra("state"))) {
                    VoIPBaseService.this.hangUp();
                }
            }
        };
        this.mHasEarpiece = null;
        this.audioRouteToSet = 2;
        this.bluetoothScoActive = false;
        this.needSwitchToBluetoothAfterScoActivates = false;
        this.didDeleteConnectionServiceContact = false;
    }
    
    private void acceptIncomingCallFromNotification() {
        this.showNotification();
        if (Build$VERSION.SDK_INT >= 23 && this.checkSelfPermission("android.permission.RECORD_AUDIO") != 0) {
            try {
                PendingIntent.getActivity((Context)this, 0, new Intent((Context)this, (Class)VoIPPermissionActivity.class).addFlags(268435456), 0).send();
            }
            catch (Exception ex) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("Error starting permission activity", ex);
                }
            }
            return;
        }
        this.acceptIncomingCall();
        try {
            PendingIntent.getActivity((Context)this, 0, new Intent((Context)this, (Class)this.getUIActivityClass()).addFlags(805306368), 0).send();
        }
        catch (Exception ex2) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Error starting incall activity", ex2);
            }
        }
    }
    
    public static VoIPBaseService getSharedInstance() {
        return VoIPBaseService.sharedInstance;
    }
    
    public static boolean isAnyKindOfCallActive() {
        final VoIPService sharedInstance = VoIPService.getSharedInstance();
        boolean b = false;
        if (sharedInstance != null) {
            b = b;
            if (VoIPService.getSharedInstance().getCallState() != 15) {
                b = true;
            }
        }
        return b;
    }
    
    private static boolean isDeviceCompatibleWithConnectionServiceAPI() {
        final int sdk_INT = Build$VERSION.SDK_INT;
        boolean b = false;
        if (sdk_INT < 26) {
            return false;
        }
        if ("angler".equals(Build.PRODUCT) || "bullhead".equals(Build.PRODUCT) || "sailfish".equals(Build.PRODUCT) || "marlin".equals(Build.PRODUCT) || "walleye".equals(Build.PRODUCT) || "taimen".equals(Build.PRODUCT) || "blueline".equals(Build.PRODUCT) || "crosshatch".equals(Build.PRODUCT) || MessagesController.getGlobalMainSettings().getBoolean("dbg_force_connection_service", false)) {
            b = true;
        }
        return b;
    }
    
    public abstract void acceptIncomingCall();
    
    @TargetApi(26)
    protected PhoneAccountHandle addAccountToTelecomManager() {
        final TelecomManager telecomManager = (TelecomManager)this.getSystemService("telecom");
        final TLRPC.User currentUser = UserConfig.getInstance(this.currentAccount).getCurrentUser();
        final ComponentName componentName = new ComponentName((Context)this, (Class)TelegramConnectionService.class);
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(currentUser.id);
        final PhoneAccountHandle phoneAccountHandle = new PhoneAccountHandle(componentName, sb.toString());
        telecomManager.registerPhoneAccount(new PhoneAccount$Builder(phoneAccountHandle, (CharSequence)ContactsController.formatName(currentUser.first_name, currentUser.last_name)).setCapabilities(2048).setIcon(Icon.createWithResource((Context)this, 2131165446)).setHighlightColor(-13851168).addSupportedUriScheme("sip").build());
        return phoneAccountHandle;
    }
    
    protected void callEnded() {
        if (BuildVars.LOGS_ENABLED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Call ");
            sb.append(this.getCallID());
            sb.append(" ended");
            FileLog.d(sb.toString());
        }
        this.dispatchStateChanged(11);
        final boolean needPlayEndSound = this.needPlayEndSound;
        long n = 700L;
        if (needPlayEndSound) {
            this.playingSound = true;
            this.soundPool.play(this.spEndId, 1.0f, 1.0f, 0, 0, 1.0f);
            AndroidUtilities.runOnUIThread(this.afterSoundRunnable, 700L);
        }
        final Runnable timeoutRunnable = this.timeoutRunnable;
        if (timeoutRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(timeoutRunnable);
            this.timeoutRunnable = null;
        }
        if (!this.needPlayEndSound) {
            n = 0L;
        }
        this.endConnectionServiceCall(n);
        this.stopSelf();
    }
    
    protected void callFailed() {
        final VoIPController controller = this.controller;
        int lastError;
        if (controller != null && this.controllerStarted) {
            lastError = controller.getLastError();
        }
        else {
            lastError = 0;
        }
        this.callFailed(lastError);
    }
    
    protected void callFailed(final int n) {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("Call ");
            sb.append(this.getCallID());
            sb.append(" failed with error code ");
            sb.append(n);
            throw new Exception(sb.toString());
        }
        catch (Exception ex) {
            FileLog.e(ex);
            this.lastError = n;
            this.dispatchStateChanged(4);
            if (n != -3) {
                final SoundPool soundPool = this.soundPool;
                if (soundPool != null) {
                    this.playingSound = true;
                    soundPool.play(this.spFailedID, 1.0f, 1.0f, 0, 0, 1.0f);
                    AndroidUtilities.runOnUIThread(this.afterSoundRunnable, 1000L);
                }
            }
            if (VoIPBaseService.USE_CONNECTION_SERVICE) {
                final CallConnection systemCallConnection = this.systemCallConnection;
                if (systemCallConnection != null) {
                    systemCallConnection.setDisconnected(new DisconnectCause(1));
                    this.systemCallConnection.destroy();
                    this.systemCallConnection = null;
                }
            }
            this.stopSelf();
        }
    }
    
    void callFailedFromConnectionService() {
        if (this.isOutgoing) {
            this.callFailed(-5);
        }
        else {
            this.hangUp();
        }
    }
    
    protected void configureDeviceForCall() {
        this.needPlayEndSound = true;
        final AudioManager audioManager = (AudioManager)this.getSystemService("audio");
        while (true) {
            if (VoIPBaseService.USE_CONNECTION_SERVICE) {
                break Label_0155;
            }
            audioManager.setMode(3);
            audioManager.requestAudioFocus((AudioManager$OnAudioFocusChangeListener)this, 0, 1);
            Label_0129: {
                if (!this.isBluetoothHeadsetConnected() || !this.hasEarpiece()) {
                    break Label_0129;
                }
                final int audioRouteToSet = this.audioRouteToSet;
                Label_0116: {
                    if (audioRouteToSet == 0) {
                        break Label_0116;
                    }
                    Label_0103: {
                        if (audioRouteToSet == 1) {
                            break Label_0103;
                        }
                        if (audioRouteToSet != 2) {
                            break Label_0155;
                        }
                        Label_0090: {
                            if (this.bluetoothScoActive) {
                                break Label_0090;
                            }
                            this.needSwitchToBluetoothAfterScoActivates = true;
                            try {
                                audioManager.startBluetoothSco();
                                this.updateOutputGainControlState();
                                this.audioConfigured = true;
                                final SensorManager sensorManager = (SensorManager)this.getSystemService("sensor");
                                final Sensor defaultSensor = sensorManager.getDefaultSensor(8);
                                if (defaultSensor != null) {
                                    try {
                                        this.proximityWakelock = ((PowerManager)this.getSystemService("power")).newWakeLock(32, "telegram-voip-prx");
                                        sensorManager.registerListener((SensorEventListener)this, defaultSensor, 3);
                                    }
                                    catch (Exception ex) {
                                        if (BuildVars.LOGS_ENABLED) {
                                            FileLog.e("Error initializing proximity sensor", ex);
                                        }
                                    }
                                }
                                return;
                                audioManager.setBluetoothScoOn(true);
                                audioManager.setSpeakerphoneOn(false);
                                continue;
                                // iftrue(Label_0147:, !this.isBluetoothHeadsetConnected())
                                audioManager.setBluetoothScoOn(this.speakerphoneStateToSet);
                                continue;
                                audioManager.setBluetoothScoOn(false);
                                audioManager.setSpeakerphoneOn(true);
                                continue;
                                audioManager.setBluetoothScoOn(false);
                                audioManager.setSpeakerphoneOn(false);
                                continue;
                                Label_0147: {
                                    audioManager.setSpeakerphoneOn(this.speakerphoneStateToSet);
                                }
                                continue;
                            }
                            catch (Throwable t) {
                                continue;
                            }
                        }
                    }
                }
            }
            break;
        }
    }
    
    protected VoIPController createController() {
        return new VoIPController();
    }
    
    public abstract void declineIncomingCall();
    
    public abstract void declineIncomingCall(final int p0, final Runnable p1);
    
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.appDidLogout) {
            this.callEnded();
        }
    }
    
    protected void dispatchStateChanged(final int n) {
        if (BuildVars.LOGS_ENABLED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("== Call ");
            sb.append(this.getCallID());
            sb.append(" state changed to ");
            sb.append(n);
            sb.append(" ==");
            FileLog.d(sb.toString());
        }
        this.currentState = n;
        if (VoIPBaseService.USE_CONNECTION_SERVICE && n == 3) {
            final CallConnection systemCallConnection = this.systemCallConnection;
            if (systemCallConnection != null) {
                systemCallConnection.setActive();
            }
        }
        for (int i = 0; i < this.stateListeners.size(); ++i) {
            this.stateListeners.get(i).onStateChanged(n);
        }
    }
    
    protected void endConnectionServiceCall(final long n) {
        if (VoIPBaseService.USE_CONNECTION_SERVICE) {
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    final VoIPBaseService this$0 = VoIPBaseService.this;
                    final CallConnection systemCallConnection = this$0.systemCallConnection;
                    if (systemCallConnection != null) {
                        final int callDiscardReason = this$0.callDiscardReason;
                        int n = 2;
                        if (callDiscardReason != 1) {
                            if (callDiscardReason != 2) {
                                int n2 = 4;
                                if (callDiscardReason != 3) {
                                    if (callDiscardReason != 4) {
                                        systemCallConnection.setDisconnected(new DisconnectCause(3));
                                    }
                                    else {
                                        systemCallConnection.setDisconnected(new DisconnectCause(7));
                                    }
                                }
                                else {
                                    if (!this$0.isOutgoing) {
                                        n2 = 5;
                                    }
                                    systemCallConnection.setDisconnected(new DisconnectCause(n2));
                                }
                            }
                            else {
                                systemCallConnection.setDisconnected(new DisconnectCause(1));
                            }
                        }
                        else {
                            if (!this$0.isOutgoing) {
                                n = 6;
                            }
                            systemCallConnection.setDisconnected(new DisconnectCause(n));
                        }
                        VoIPBaseService.this.systemCallConnection.destroy();
                        VoIPBaseService.this.systemCallConnection = null;
                    }
                }
            };
            if (n > 0L) {
                AndroidUtilities.runOnUIThread(runnable, n);
            }
            else {
                runnable.run();
            }
        }
    }
    
    public int getAccount() {
        return this.currentAccount;
    }
    
    public long getCallDuration() {
        if (this.controllerStarted) {
            final VoIPController controller = this.controller;
            if (controller != null) {
                return this.lastKnownDuration = controller.getCallDuration();
            }
        }
        return this.lastKnownDuration;
    }
    
    public abstract long getCallID();
    
    public int getCallState() {
        return this.currentState;
    }
    
    public abstract CallConnection getConnectionAndStartCall();
    
    public int getCurrentAudioRoute() {
        if (VoIPBaseService.USE_CONNECTION_SERVICE) {
            final CallConnection systemCallConnection = this.systemCallConnection;
            if (systemCallConnection != null && systemCallConnection.getCallAudioState() != null) {
                final int route = this.systemCallConnection.getCallAudioState().getRoute();
                if (route != 1) {
                    if (route == 2) {
                        return 2;
                    }
                    if (route != 4) {
                        if (route != 8) {
                            return this.audioRouteToSet;
                        }
                        return 1;
                    }
                }
                return 0;
            }
            return this.audioRouteToSet;
        }
        if (!this.audioConfigured) {
            return this.audioRouteToSet;
        }
        final AudioManager audioManager = (AudioManager)this.getSystemService("audio");
        if (audioManager.isBluetoothScoOn()) {
            return 2;
        }
        if (audioManager.isSpeakerphoneOn()) {
            return 1;
        }
        return 0;
    }
    
    public String getDebugString() {
        return this.controller.getDebugString();
    }
    
    public int getLastError() {
        return this.lastError;
    }
    
    protected Bitmap getRoundAvatarBitmap(final TLObject tlObject) {
        final boolean b = tlObject instanceof TLRPC.User;
        final Bitmap bitmap = null;
        Bitmap bitmap2 = null;
        Label_0264: {
            Label_0139: {
                if (b) {
                    final TLRPC.User user = (TLRPC.User)tlObject;
                    final TLRPC.UserProfilePhoto photo = user.photo;
                    bitmap2 = bitmap;
                    if (photo == null) {
                        break Label_0264;
                    }
                    bitmap2 = bitmap;
                    if (photo.photo_small != null) {
                        final BitmapDrawable imageFromMemory = ImageLoader.getInstance().getImageFromMemory(user.photo.photo_small, null, "50_50");
                        Label_0082: {
                            if (imageFromMemory == null) {
                                try {
                                    final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
                                    bitmapFactory$Options.inMutable = true;
                                    bitmap2 = BitmapFactory.decodeFile(FileLoader.getPathToAttach(user.photo.photo_small, true).toString(), bitmapFactory$Options);
                                    break Label_0082;
                                }
                                catch (Throwable t) {
                                    FileLog.e(t);
                                    bitmap2 = bitmap;
                                    break Label_0264;
                                }
                                break Label_0139;
                            }
                            bitmap2 = imageFromMemory.getBitmap().copy(Bitmap$Config.ARGB_8888, true);
                        }
                    }
                    break Label_0264;
                }
            }
            final TLRPC.Chat chat = (TLRPC.Chat)tlObject;
            final TLRPC.ChatPhoto photo2 = chat.photo;
            bitmap2 = bitmap;
            if (photo2 != null) {
                bitmap2 = bitmap;
                if (photo2.photo_small != null) {
                    final BitmapDrawable imageFromMemory2 = ImageLoader.getInstance().getImageFromMemory(chat.photo.photo_small, null, "50_50");
                    if (imageFromMemory2 != null) {
                        bitmap2 = imageFromMemory2.getBitmap().copy(Bitmap$Config.ARGB_8888, true);
                    }
                    else {
                        try {
                            final BitmapFactory$Options bitmapFactory$Options2 = new BitmapFactory$Options();
                            bitmapFactory$Options2.inMutable = true;
                            bitmap2 = BitmapFactory.decodeFile(FileLoader.getPathToAttach(chat.photo.photo_small, true).toString(), bitmapFactory$Options2);
                        }
                        catch (Throwable t2) {
                            FileLog.e(t2);
                            bitmap2 = bitmap;
                        }
                    }
                }
            }
        }
        Bitmap bitmap3;
        if ((bitmap3 = bitmap2) == null) {
            Theme.createDialogsResources((Context)this);
            AvatarDrawable avatarDrawable;
            if (b) {
                avatarDrawable = new AvatarDrawable((TLRPC.User)tlObject);
            }
            else {
                avatarDrawable = new AvatarDrawable((TLRPC.Chat)tlObject);
            }
            bitmap3 = Bitmap.createBitmap(AndroidUtilities.dp(42.0f), AndroidUtilities.dp(42.0f), Bitmap$Config.ARGB_8888);
            avatarDrawable.setBounds(0, 0, bitmap3.getWidth(), bitmap3.getHeight());
            avatarDrawable.draw(new Canvas(bitmap3));
        }
        final Canvas canvas = new Canvas(bitmap3);
        final Path path = new Path();
        path.addCircle((float)(bitmap3.getWidth() / 2), (float)(bitmap3.getHeight() / 2), (float)(bitmap3.getWidth() / 2), Path$Direction.CW);
        path.toggleInverseFillType();
        final Paint paint = new Paint(1);
        paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.CLEAR));
        canvas.drawPath(path, paint);
        return bitmap3;
    }
    
    protected int getStatsNetworkType() {
        final NetworkInfo lastNetInfo = this.lastNetInfo;
        int n;
        if (lastNetInfo != null && lastNetInfo.getType() == 0) {
            if (this.lastNetInfo.isRoaming()) {
                n = 2;
            }
            else {
                n = 0;
            }
        }
        else {
            n = 1;
        }
        return n;
    }
    
    protected abstract Class<? extends Activity> getUIActivityClass();
    
    public void handleNotificationAction(final Intent intent) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getPackageName());
        sb.append(".END_CALL");
        if (sb.toString().equals(intent.getAction())) {
            this.stopForeground(true);
            this.hangUp();
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(this.getPackageName());
            sb2.append(".DECLINE_CALL");
            if (sb2.toString().equals(intent.getAction())) {
                this.stopForeground(true);
                this.declineIncomingCall(4, null);
            }
            else {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(this.getPackageName());
                sb3.append(".ANSWER_CALL");
                if (sb3.toString().equals(intent.getAction())) {
                    this.acceptIncomingCallFromNotification();
                }
            }
        }
    }
    
    public abstract void hangUp();
    
    public abstract void hangUp(final Runnable p0);
    
    public boolean hasEarpiece() {
        final boolean use_CONNECTION_SERVICE = VoIPBaseService.USE_CONNECTION_SERVICE;
        boolean b = false;
        if (use_CONNECTION_SERVICE) {
            final CallConnection systemCallConnection = this.systemCallConnection;
            if (systemCallConnection != null && systemCallConnection.getCallAudioState() != null) {
                if ((this.systemCallConnection.getCallAudioState().getSupportedRouteMask() & 0x5) != 0x0) {
                    b = true;
                }
                return b;
            }
        }
        if (((TelephonyManager)this.getSystemService("phone")).getPhoneType() != 0) {
            return true;
        }
        final Boolean mHasEarpiece = this.mHasEarpiece;
        if (mHasEarpiece != null) {
            return mHasEarpiece;
        }
        try {
            final AudioManager obj = (AudioManager)this.getSystemService("audio");
            final Method method = AudioManager.class.getMethod("getDevicesForStream", Integer.TYPE);
            final int int1 = AudioManager.class.getField("DEVICE_OUT_EARPIECE").getInt(null);
            if (((int)method.invoke(obj, 0) & int1) == int1) {
                this.mHasEarpiece = Boolean.TRUE;
            }
            else {
                this.mHasEarpiece = Boolean.FALSE;
            }
        }
        catch (Throwable t) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Error while checking earpiece! ", t);
            }
            this.mHasEarpiece = Boolean.TRUE;
        }
        return this.mHasEarpiece;
    }
    
    protected void initializeAccountRelatedThings() {
        this.updateServerConfig();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.appDidLogout);
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(false, false);
        (this.controller = this.createController()).setConnectionStateListener((VoIPController.ConnectionStateListener)this);
    }
    
    public boolean isBluetoothHeadsetConnected() {
        if (VoIPBaseService.USE_CONNECTION_SERVICE) {
            final CallConnection systemCallConnection = this.systemCallConnection;
            if (systemCallConnection != null && systemCallConnection.getCallAudioState() != null) {
                return (this.systemCallConnection.getCallAudioState().getSupportedRouteMask() & 0x2) != 0x0;
            }
        }
        return this.isBtHeadsetConnected;
    }
    
    protected boolean isFinished() {
        final int currentState = this.currentState;
        return currentState == 11 || currentState == 4;
    }
    
    public boolean isMicMute() {
        return this.micMute;
    }
    
    public boolean isOutgoing() {
        return this.isOutgoing;
    }
    
    protected boolean isRinging() {
        return false;
    }
    
    public boolean isSpeakerphoneOn() {
        if (VoIPBaseService.USE_CONNECTION_SERVICE) {
            final CallConnection systemCallConnection = this.systemCallConnection;
            if (systemCallConnection != null && systemCallConnection.getCallAudioState() != null) {
                final int route = this.systemCallConnection.getCallAudioState().getRoute();
                final boolean hasEarpiece = this.hasEarpiece();
                boolean b = true;
                if (hasEarpiece) {
                    if (route == 8) {
                        return b;
                    }
                }
                else if (route == 2) {
                    return b;
                }
                b = false;
                return b;
            }
        }
        if (this.audioConfigured && !VoIPBaseService.USE_CONNECTION_SERVICE) {
            final AudioManager audioManager = (AudioManager)this.getSystemService("audio");
            boolean b2;
            if (this.hasEarpiece()) {
                b2 = audioManager.isSpeakerphoneOn();
            }
            else {
                b2 = audioManager.isBluetoothScoOn();
            }
            return b2;
        }
        return this.speakerphoneStateToSet;
    }
    
    public void onAccuracyChanged(final Sensor sensor, final int n) {
    }
    
    public void onAudioFocusChange(final int n) {
        if (n == 1) {
            this.haveAudioFocus = true;
        }
        else {
            this.haveAudioFocus = false;
        }
    }
    
    public void onConnectionStateChanged(final int n) {
        if (n == 4) {
            this.callFailed();
            return;
        }
        if (n == 3) {
            final Runnable connectingSoundRunnable = this.connectingSoundRunnable;
            if (connectingSoundRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(connectingSoundRunnable);
                this.connectingSoundRunnable = null;
            }
            final int spPlayID = this.spPlayID;
            if (spPlayID != 0) {
                this.soundPool.stop(spPlayID);
                this.spPlayID = 0;
            }
            if (!this.wasEstablished) {
                this.wasEstablished = true;
                if (!this.isProximityNear) {
                    final Vibrator vibrator = (Vibrator)this.getSystemService("vibrator");
                    if (vibrator.hasVibrator()) {
                        vibrator.vibrate(100L);
                    }
                }
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        final VoIPBaseService this$0 = VoIPBaseService.this;
                        if (this$0.controller == null) {
                            return;
                        }
                        StatsController.getInstance(VoIPBaseService.this.currentAccount).incrementTotalCallsTime(this$0.getStatsNetworkType(), 5);
                        AndroidUtilities.runOnUIThread(this, 5000L);
                    }
                }, 5000L);
                if (this.isOutgoing) {
                    StatsController.getInstance(this.currentAccount).incrementSentItemsCount(this.getStatsNetworkType(), 0, 1);
                }
                else {
                    StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(this.getStatsNetworkType(), 0, 1);
                }
            }
        }
        if (n == 5) {
            final int spPlayID2 = this.spPlayID;
            if (spPlayID2 != 0) {
                this.soundPool.stop(spPlayID2);
            }
            this.spPlayID = this.soundPool.play(this.spConnectingId, 1.0f, 1.0f, 0, -1, 1.0f);
        }
        this.dispatchStateChanged(n);
    }
    
    protected void onControllerPreRelease() {
    }
    
    public void onCreate() {
        super.onCreate();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("=============== VoIPService STARTING ===============");
        }
        final AudioManager audioManager = (AudioManager)this.getSystemService("audio");
        if (Build$VERSION.SDK_INT >= 17 && audioManager.getProperty("android.media.property.OUTPUT_FRAMES_PER_BUFFER") != null) {
            VoIPController.setNativeBufferSize(Integer.parseInt(audioManager.getProperty("android.media.property.OUTPUT_FRAMES_PER_BUFFER")));
        }
        else {
            VoIPController.setNativeBufferSize(AudioTrack.getMinBufferSize(48000, 4, 2) / 2);
        }
        try {
            (this.cpuWakelock = ((PowerManager)this.getSystemService("power")).newWakeLock(1, "telegram-voip")).acquire();
            BluetoothAdapter defaultAdapter;
            if (audioManager.isBluetoothScoAvailableOffCall()) {
                defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            }
            else {
                defaultAdapter = null;
            }
            this.btAdapter = defaultAdapter;
            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            if (!VoIPBaseService.USE_CONNECTION_SERVICE) {
                intentFilter.addAction("android.intent.action.HEADSET_PLUG");
                if (this.btAdapter != null) {
                    intentFilter.addAction("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
                    intentFilter.addAction("android.media.ACTION_SCO_AUDIO_STATE_UPDATED");
                }
                intentFilter.addAction("android.intent.action.PHONE_STATE");
            }
            this.registerReceiver(this.receiver, intentFilter);
            boolean b = false;
            this.soundPool = new SoundPool(1, 0, 0);
            this.spConnectingId = this.soundPool.load((Context)this, 2131492875, 1);
            this.spRingbackID = this.soundPool.load((Context)this, 2131492878, 1);
            this.spFailedID = this.soundPool.load((Context)this, 2131492877, 1);
            this.spEndId = this.soundPool.load((Context)this, 2131492876, 1);
            this.spBusyId = this.soundPool.load((Context)this, 2131492874, 1);
            audioManager.registerMediaButtonEventReceiver(new ComponentName((Context)this, (Class)VoIPMediaButtonReceiver.class));
            if (!VoIPBaseService.USE_CONNECTION_SERVICE && this.btAdapter != null && this.btAdapter.isEnabled()) {
                if (this.btAdapter.getProfileConnectionState(1) == 2) {
                    b = true;
                }
                this.updateBluetoothHeadsetState(b);
                final Iterator<StateListener> iterator = this.stateListeners.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onAudioSettingsChanged();
                }
            }
        }
        catch (Exception ex) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("error initializing voip controller", ex);
            }
            this.callFailed();
        }
    }
    
    public void onDestroy() {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("=============== VoIPService STOPPING ===============");
        }
        this.stopForeground(true);
        this.stopRinging();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.appDidLogout);
        final SensorManager sensorManager = (SensorManager)this.getSystemService("sensor");
        if (sensorManager.getDefaultSensor(8) != null) {
            sensorManager.unregisterListener((SensorEventListener)this);
        }
        final PowerManager$WakeLock proximityWakelock = this.proximityWakelock;
        if (proximityWakelock != null && proximityWakelock.isHeld()) {
            this.proximityWakelock.release();
        }
        this.unregisterReceiver(this.receiver);
        final Runnable timeoutRunnable = this.timeoutRunnable;
        if (timeoutRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(timeoutRunnable);
            this.timeoutRunnable = null;
        }
        super.onDestroy();
        VoIPBaseService.sharedInstance = null;
        AndroidUtilities.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didEndedCall, new Object[0]);
            }
        });
        final VoIPController controller = this.controller;
        if (controller != null && this.controllerStarted) {
            this.lastKnownDuration = controller.getCallDuration();
            this.updateStats();
            StatsController.getInstance(this.currentAccount).incrementTotalCallsTime(this.getStatsNetworkType(), (int)(this.lastKnownDuration / 1000L) % 5);
            this.onControllerPreRelease();
            this.controller.release();
            this.controller = null;
        }
        this.cpuWakelock.release();
        final AudioManager audioManager = (AudioManager)this.getSystemService("audio");
        if (!VoIPBaseService.USE_CONNECTION_SERVICE) {
            if (this.isBtHeadsetConnected && !this.playingSound) {
                audioManager.stopBluetoothSco();
                audioManager.setSpeakerphoneOn(false);
            }
            try {
                audioManager.setMode(0);
            }
            catch (SecurityException ex) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("Error setting audio more to normal", ex);
                }
            }
            audioManager.abandonAudioFocus((AudioManager$OnAudioFocusChangeListener)this);
        }
        audioManager.unregisterMediaButtonEventReceiver(new ComponentName((Context)this, (Class)VoIPMediaButtonReceiver.class));
        if (this.haveAudioFocus) {
            audioManager.abandonAudioFocus((AudioManager$OnAudioFocusChangeListener)this);
        }
        if (!this.playingSound) {
            this.soundPool.release();
        }
        if (VoIPBaseService.USE_CONNECTION_SERVICE) {
            if (!this.didDeleteConnectionServiceContact) {
                ContactsController.getInstance(this.currentAccount).deleteConnectionServiceContact();
            }
            final CallConnection systemCallConnection = this.systemCallConnection;
            if (systemCallConnection != null && !this.playingSound) {
                systemCallConnection.destroy();
            }
        }
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
        VoIPHelper.lastCallTime = System.currentTimeMillis();
    }
    
    @SuppressLint({ "NewApi" })
    public void onSensorChanged(final SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 8) {
            final AudioManager audioManager = (AudioManager)this.getSystemService("audio");
            if (!this.isHeadsetPlugged && !audioManager.isSpeakerphoneOn()) {
                if (!this.isBluetoothHeadsetConnected() || !audioManager.isBluetoothScoOn()) {
                    final float[] values = sensorEvent.values;
                    boolean b = false;
                    if (values[0] < Math.min(sensorEvent.sensor.getMaximumRange(), 3.0f)) {
                        b = true;
                    }
                    if (b != this.isProximityNear) {
                        if (BuildVars.LOGS_ENABLED) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("proximity ");
                            sb.append(b);
                            FileLog.d(sb.toString());
                        }
                        this.isProximityNear = b;
                        try {
                            if (this.isProximityNear) {
                                this.proximityWakelock.acquire();
                            }
                            else {
                                this.proximityWakelock.release(1);
                            }
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                    }
                }
            }
        }
    }
    
    public void onSignalBarCountChanged(final int signalBarCount) {
        this.signalBarCount = signalBarCount;
        for (int i = 0; i < this.stateListeners.size(); ++i) {
            this.stateListeners.get(i).onSignalBarsCountChanged(signalBarCount);
        }
    }
    
    public void registerStateListener(final StateListener e) {
        this.stateListeners.add(e);
        final int currentState = this.currentState;
        if (currentState != 0) {
            e.onStateChanged(currentState);
        }
        final int signalBarCount = this.signalBarCount;
        if (signalBarCount != 0) {
            e.onSignalBarsCountChanged(signalBarCount);
        }
    }
    
    public void setMicMute(final boolean b) {
        this.micMute = b;
        final VoIPController controller = this.controller;
        if (controller != null) {
            controller.setMicMute(b);
        }
    }
    
    protected void showIncomingNotification(final String contentText, final CharSequence subText, final TLObject tlObject, final List<TLRPC.User> list, int sdk_INT, final Class<? extends Activity> clazz) {
        final Intent intent = new Intent((Context)this, (Class)clazz);
        intent.addFlags(805306368);
        final Notification$Builder setContentIntent = new Notification$Builder((Context)this).setContentTitle((CharSequence)LocaleController.getString("VoipInCallBranding", 2131561071)).setContentText((CharSequence)contentText).setSmallIcon(2131165698).setSubText(subText).setContentIntent(PendingIntent.getActivity((Context)this, 0, intent, 0));
        final Uri parse = Uri.parse("content://org.telegram.messenger.call_sound_provider/start_ringing");
        sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT >= 26) {
            final SharedPreferences globalNotificationsSettings = MessagesController.getGlobalNotificationsSettings();
            final int int1 = globalNotificationsSettings.getInt("calls_notification_channel", 0);
            final NotificationManager notificationManager = (NotificationManager)this.getSystemService("notification");
            final StringBuilder sb = new StringBuilder();
            sb.append("incoming_calls");
            sb.append(int1);
            final NotificationChannel notificationChannel = notificationManager.getNotificationChannel(sb.toString());
            if (notificationChannel != null) {
                notificationManager.deleteNotificationChannel(notificationChannel.getId());
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("incoming_calls2");
            sb2.append(int1);
            final NotificationChannel notificationChannel2 = notificationManager.getNotificationChannel(sb2.toString());
            sdk_INT = int1;
            boolean b = false;
            Label_0348: {
                if (notificationChannel2 != null) {
                    if (notificationChannel2.getImportance() >= 4 && parse.equals((Object)notificationChannel2.getSound()) && notificationChannel2.getVibrationPattern() == null && !notificationChannel2.shouldVibrate()) {
                        b = false;
                        sdk_INT = int1;
                        break Label_0348;
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("User messed up the notification channel; deleting it and creating a proper one");
                    }
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("incoming_calls2");
                    sb3.append(int1);
                    notificationManager.deleteNotificationChannel(sb3.toString());
                    sdk_INT = int1 + 1;
                    globalNotificationsSettings.edit().putInt("calls_notification_channel", sdk_INT).commit();
                }
                b = true;
            }
            if (b) {
                final AudioAttributes build = new AudioAttributes$Builder().setUsage(6).build();
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("incoming_calls2");
                sb4.append(sdk_INT);
                final NotificationChannel notificationChannel3 = new NotificationChannel(sb4.toString(), (CharSequence)LocaleController.getString("IncomingCalls", 2131559662), 4);
                notificationChannel3.setSound(parse, build);
                notificationChannel3.enableVibration(false);
                notificationChannel3.enableLights(false);
                notificationManager.createNotificationChannel(notificationChannel3);
            }
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("incoming_calls2");
            sb5.append(sdk_INT);
            setContentIntent.setChannelId(sb5.toString());
        }
        else if (sdk_INT >= 21) {
            setContentIntent.setSound(parse, 2);
        }
        final Intent intent2 = new Intent((Context)this, (Class)VoIPActionsReceiver.class);
        final StringBuilder sb6 = new StringBuilder();
        sb6.append(this.getPackageName());
        sb6.append(".DECLINE_CALL");
        intent2.setAction(sb6.toString());
        intent2.putExtra("call_id", this.getCallID());
        Object string = LocaleController.getString("VoipDeclineCall", 2131561064);
        if (Build$VERSION.SDK_INT >= 24) {
            string = new SpannableString((CharSequence)string);
            ((SpannableString)string).setSpan((Object)new ForegroundColorSpan(-769226), 0, ((CharSequence)string).length(), 0);
        }
        final PendingIntent broadcast = PendingIntent.getBroadcast((Context)this, 0, intent2, 268435456);
        setContentIntent.addAction(2131165430, (CharSequence)string, broadcast);
        final Intent intent3 = new Intent((Context)this, (Class)VoIPActionsReceiver.class);
        final StringBuilder sb7 = new StringBuilder();
        sb7.append(this.getPackageName());
        sb7.append(".ANSWER_CALL");
        intent3.setAction(sb7.toString());
        intent3.putExtra("call_id", this.getCallID());
        Object string2 = LocaleController.getString("VoipAnswerCall", 2131561056);
        if (Build$VERSION.SDK_INT >= 24) {
            string2 = new SpannableString((CharSequence)string2);
            ((SpannableString)string2).setSpan((Object)new ForegroundColorSpan(-16733696), 0, ((CharSequence)string2).length(), 0);
        }
        final PendingIntent broadcast2 = PendingIntent.getBroadcast((Context)this, 0, intent3, 268435456);
        setContentIntent.addAction(2131165429, (CharSequence)string2, broadcast2);
        setContentIntent.setPriority(2);
        if (Build$VERSION.SDK_INT >= 17) {
            setContentIntent.setShowWhen(false);
        }
        if (Build$VERSION.SDK_INT >= 21) {
            setContentIntent.setColor(-13851168);
            setContentIntent.setVibrate(new long[0]);
            setContentIntent.setCategory("call");
            setContentIntent.setFullScreenIntent(PendingIntent.getActivity((Context)this, 0, intent, 0), true);
            if (tlObject instanceof TLRPC.User) {
                final TLRPC.User user = (TLRPC.User)tlObject;
                if (!TextUtils.isEmpty((CharSequence)user.phone)) {
                    final StringBuilder sb8 = new StringBuilder();
                    sb8.append("tel:");
                    sb8.append(user.phone);
                    setContentIntent.addPerson(sb8.toString());
                }
            }
        }
        final Notification notification = setContentIntent.getNotification();
        if (Build$VERSION.SDK_INT >= 21) {
            final String packageName = this.getPackageName();
            if (LocaleController.isRTL) {
                sdk_INT = 2131361822;
            }
            else {
                sdk_INT = 2131361821;
            }
            final RemoteViews remoteViews = new RemoteViews(packageName, sdk_INT);
            remoteViews.setTextViewText(2131230851, (CharSequence)contentText);
            if (TextUtils.isEmpty(subText)) {
                remoteViews.setViewVisibility(2131230919, 8);
                if (UserConfig.getActivatedAccountsCount() > 1) {
                    final TLRPC.User currentUser = UserConfig.getInstance(this.currentAccount).getCurrentUser();
                    remoteViews.setTextViewText(2131230935, (CharSequence)LocaleController.formatString("VoipInCallBrandingWithName", 2131561072, ContactsController.formatName(currentUser.first_name, currentUser.last_name)));
                }
                else {
                    remoteViews.setTextViewText(2131230935, (CharSequence)LocaleController.getString("VoipInCallBranding", 2131561071));
                }
            }
            else {
                if (UserConfig.getActivatedAccountsCount() > 1) {
                    final TLRPC.User currentUser2 = UserConfig.getInstance(this.currentAccount).getCurrentUser();
                    remoteViews.setTextViewText(2131230919, (CharSequence)LocaleController.formatString("VoipAnsweringAsAccount", 2131561057, ContactsController.formatName(currentUser2.first_name, currentUser2.last_name)));
                }
                else {
                    remoteViews.setViewVisibility(2131230919, 8);
                }
                remoteViews.setTextViewText(2131230935, subText);
            }
            final Bitmap roundAvatarBitmap = this.getRoundAvatarBitmap(tlObject);
            remoteViews.setTextViewText(2131230786, (CharSequence)LocaleController.getString("VoipAnswerCall", 2131561056));
            remoteViews.setTextViewText(2131230813, (CharSequence)LocaleController.getString("VoipDeclineCall", 2131561064));
            remoteViews.setImageViewBitmap(2131230872, roundAvatarBitmap);
            remoteViews.setOnClickPendingIntent(2131230785, broadcast2);
            remoteViews.setOnClickPendingIntent(2131230812, broadcast);
            setContentIntent.setLargeIcon(roundAvatarBitmap);
            notification.bigContentView = remoteViews;
            notification.headsUpContentView = remoteViews;
        }
        this.startForeground(202, notification);
    }
    
    protected abstract void showNotification();
    
    protected void showNotification(String setContentIntent, final TLRPC.FileLocation fileLocation, final Class<? extends Activity> clazz) {
        final Intent intent = new Intent((Context)this, (Class)clazz);
        intent.addFlags(805306368);
        setContentIntent = (String)new Notification$Builder((Context)this).setContentTitle((CharSequence)LocaleController.getString("VoipOutgoingCall", 2131561083)).setContentText((CharSequence)setContentIntent).setSmallIcon(2131165698).setContentIntent(PendingIntent.getActivity((Context)this, 0, intent, 0));
        if (Build$VERSION.SDK_INT >= 16) {
            final Intent intent2 = new Intent((Context)this, (Class)VoIPActionsReceiver.class);
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getPackageName());
            sb.append(".END_CALL");
            intent2.setAction(sb.toString());
            ((Notification$Builder)setContentIntent).addAction(2131165430, (CharSequence)LocaleController.getString("VoipEndCall", 2131561065), PendingIntent.getBroadcast((Context)this, 0, intent2, 134217728));
            ((Notification$Builder)setContentIntent).setPriority(2);
        }
        if (Build$VERSION.SDK_INT >= 17) {
            ((Notification$Builder)setContentIntent).setShowWhen(false);
        }
        if (Build$VERSION.SDK_INT >= 21) {
            ((Notification$Builder)setContentIntent).setColor(-13851168);
        }
        if (Build$VERSION.SDK_INT >= 26) {
            NotificationsController.checkOtherNotificationsChannel();
            ((Notification$Builder)setContentIntent).setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
        }
        if (fileLocation != null) {
            final BitmapDrawable imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
            if (imageFromMemory != null) {
                ((Notification$Builder)setContentIntent).setLargeIcon(imageFromMemory.getBitmap());
            }
            else {
                try {
                    final float n = 160.0f / AndroidUtilities.dp(50.0f);
                    final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
                    int inSampleSize;
                    if (n < 1.0f) {
                        inSampleSize = 1;
                    }
                    else {
                        inSampleSize = (int)n;
                    }
                    bitmapFactory$Options.inSampleSize = inSampleSize;
                    final Bitmap decodeFile = BitmapFactory.decodeFile(FileLoader.getPathToAttach(fileLocation, true).toString(), bitmapFactory$Options);
                    if (decodeFile != null) {
                        ((Notification$Builder)setContentIntent).setLargeIcon(decodeFile);
                    }
                }
                catch (Throwable t) {
                    FileLog.e(t);
                }
            }
        }
        this.startForeground(201, this.ongoingCallNotification = ((Notification$Builder)setContentIntent).getNotification());
    }
    
    protected abstract void startRinging();
    
    public abstract void startRingtoneAndVibration();
    
    protected void startRingtoneAndVibration(int n) {
        final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
        final AudioManager audioManager = (AudioManager)this.getSystemService("audio");
        if (audioManager.getRingerMode() != 0) {
            if (!VoIPBaseService.USE_CONNECTION_SERVICE) {
                audioManager.requestAudioFocus((AudioManager$OnAudioFocusChangeListener)this, 2, 1);
            }
            (this.ringtonePlayer = new MediaPlayer()).setOnPreparedListener((MediaPlayer$OnPreparedListener)new MediaPlayer$OnPreparedListener() {
                public void onPrepared(final MediaPlayer mediaPlayer) {
                    VoIPBaseService.this.ringtonePlayer.start();
                }
            });
            this.ringtonePlayer.setLooping(true);
            this.ringtonePlayer.setAudioStreamType(2);
            try {
                final StringBuilder sb = new StringBuilder();
                sb.append("custom_");
                sb.append(n);
                String s;
                if (notificationsSettings.getBoolean(sb.toString(), false)) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("ringtone_path_");
                    sb2.append(n);
                    s = notificationsSettings.getString(sb2.toString(), RingtoneManager.getDefaultUri(1).toString());
                }
                else {
                    s = notificationsSettings.getString("CallsRingtonePath", RingtoneManager.getDefaultUri(1).toString());
                }
                this.ringtonePlayer.setDataSource((Context)this, Uri.parse(s));
                this.ringtonePlayer.prepareAsync();
            }
            catch (Exception ex) {
                FileLog.e(ex);
                final MediaPlayer ringtonePlayer = this.ringtonePlayer;
                if (ringtonePlayer != null) {
                    ringtonePlayer.release();
                    this.ringtonePlayer = null;
                }
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("custom_");
            sb3.append(n);
            if (notificationsSettings.getBoolean(sb3.toString(), false)) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("calls_vibrate_");
                sb4.append(n);
                n = notificationsSettings.getInt(sb4.toString(), 0);
            }
            else {
                n = notificationsSettings.getInt("vibrate_calls", 0);
            }
            if ((n != 2 && n != 4 && (audioManager.getRingerMode() == 1 || audioManager.getRingerMode() == 2)) || (n == 4 && audioManager.getRingerMode() == 1)) {
                this.vibrator = (Vibrator)this.getSystemService("vibrator");
                long n2 = 700L;
                if (n == 1) {
                    n2 = 350L;
                }
                else if (n == 3) {
                    n2 = 1400L;
                }
                this.vibrator.vibrate(new long[] { 0L, n2, 500L }, 0);
            }
        }
    }
    
    public void stopRinging() {
        final MediaPlayer ringtonePlayer = this.ringtonePlayer;
        if (ringtonePlayer != null) {
            ringtonePlayer.stop();
            this.ringtonePlayer.release();
            this.ringtonePlayer = null;
        }
        final Vibrator vibrator = this.vibrator;
        if (vibrator != null) {
            vibrator.cancel();
            this.vibrator = null;
        }
    }
    
    public void toggleSpeakerphoneOrShowRouteSheet(final Activity activity) {
        if (this.isBluetoothHeadsetConnected() && this.hasEarpiece()) {
            final BottomSheet.Builder builder = new BottomSheet.Builder((Context)activity);
            final String string = LocaleController.getString("VoipAudioRoutingBluetooth", 2131561058);
            int i = 0;
            final BottomSheet create = builder.setItems(new CharSequence[] { string, LocaleController.getString("VoipAudioRoutingEarpiece", 2131561059), LocaleController.getString("VoipAudioRoutingSpeaker", 2131561060) }, new int[] { 2131165428, 2131165461, 2131165476 }, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {
                    final AudioManager audioManager = (AudioManager)VoIPBaseService.this.getSystemService("audio");
                    if (VoIPBaseService.getSharedInstance() == null) {
                        return;
                    }
                Label_0192_Outer:
                    while (true) {
                        if (VoIPBaseService.USE_CONNECTION_SERVICE) {
                            final CallConnection systemCallConnection = VoIPBaseService.this.systemCallConnection;
                            if (systemCallConnection != null) {
                                if (n == 0) {
                                    systemCallConnection.setAudioRoute(2);
                                    break Label_0249;
                                }
                                if (n == 1) {
                                    systemCallConnection.setAudioRoute(5);
                                    break Label_0249;
                                }
                                if (n != 2) {
                                    break Label_0249;
                                }
                                systemCallConnection.setAudioRoute(8);
                                break Label_0249;
                            }
                        }
                        final VoIPBaseService this$0 = VoIPBaseService.this;
                        Label_0202: {
                            if (!this$0.audioConfigured || VoIPBaseService.USE_CONNECTION_SERVICE) {
                                break Label_0202;
                            }
                        Block_19_Outer:
                            while (true) {
                                Label_0182: {
                                    if (n != 0) {
                                        if (n == 1) {
                                            if (this$0.bluetoothScoActive) {
                                                audioManager.stopBluetoothSco();
                                            }
                                            audioManager.setSpeakerphoneOn(false);
                                            audioManager.setBluetoothScoOn(false);
                                            break Label_0192;
                                        }
                                        if (n != 2) {
                                            break Label_0192;
                                        }
                                        if (this$0.bluetoothScoActive) {
                                            audioManager.stopBluetoothSco();
                                        }
                                        audioManager.setBluetoothScoOn(false);
                                        audioManager.setSpeakerphoneOn(true);
                                        break Label_0192;
                                    }
                                    else if (this$0.bluetoothScoActive) {
                                        break Label_0182;
                                    }
                                    this$0.needSwitchToBluetoothAfterScoActivates = true;
                                    try {
                                        audioManager.startBluetoothSco();
                                        VoIPBaseService.this.updateOutputGainControlState();
                                        final Iterator<StateListener> iterator = VoIPBaseService.this.stateListeners.iterator();
                                        while (iterator.hasNext()) {
                                            ((StateListener)iterator.next()).onAudioSettingsChanged();
                                        }
                                        return;
                                        Label_0219: {
                                            VoIPBaseService.this.audioRouteToSet = 1;
                                        }
                                        continue Label_0192_Outer;
                                        Label_0241:
                                        VoIPBaseService.this.audioRouteToSet = 2;
                                        while (true) {
                                            continue Label_0192_Outer;
                                            audioManager.setBluetoothScoOn(true);
                                            audioManager.setSpeakerphoneOn(false);
                                            continue Block_19_Outer;
                                            Block_17: {
                                                break Block_17;
                                                Label_0230:
                                                VoIPBaseService.this.audioRouteToSet = 0;
                                                continue Label_0192_Outer;
                                            }
                                            continue;
                                        }
                                    }
                                    // iftrue(Label_0241:, n == 0)
                                    // iftrue(Label_0230:, n == 1)
                                    // iftrue(Label_0219:, n == 2)
                                    catch (Throwable t) {
                                        continue;
                                    }
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
            }).create();
            create.setBackgroundColor(-13948117);
            create.show();
            for (ViewGroup sheetContainer = create.getSheetContainer(); i < sheetContainer.getChildCount(); ++i) {
                ((BottomSheet.BottomSheetCell)sheetContainer.getChildAt(i)).setTextColor(-1);
            }
            return;
        }
        Label_0331: {
            if (VoIPBaseService.USE_CONNECTION_SERVICE) {
                final CallConnection systemCallConnection = this.systemCallConnection;
                if (systemCallConnection != null && systemCallConnection.getCallAudioState() != null) {
                    final boolean hasEarpiece = this.hasEarpiece();
                    int n = 5;
                    if (hasEarpiece) {
                        final CallConnection systemCallConnection2 = this.systemCallConnection;
                        if (systemCallConnection2.getCallAudioState().getRoute() != 8) {
                            n = 8;
                        }
                        systemCallConnection2.setAudioRoute(n);
                        break Label_0331;
                    }
                    final CallConnection systemCallConnection3 = this.systemCallConnection;
                    if (systemCallConnection3.getCallAudioState().getRoute() != 2) {
                        n = 2;
                    }
                    systemCallConnection3.setAudioRoute(n);
                    break Label_0331;
                }
            }
            if (this.audioConfigured && !VoIPBaseService.USE_CONNECTION_SERVICE) {
                final AudioManager audioManager = (AudioManager)this.getSystemService("audio");
                if (this.hasEarpiece()) {
                    audioManager.setSpeakerphoneOn(audioManager.isSpeakerphoneOn() ^ true);
                }
                else {
                    audioManager.setBluetoothScoOn(audioManager.isBluetoothScoOn() ^ true);
                }
                this.updateOutputGainControlState();
            }
            else {
                this.speakerphoneStateToSet ^= true;
            }
        }
        final Iterator<StateListener> iterator = this.stateListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onAudioSettingsChanged();
        }
    }
    
    public void unregisterStateListener(final StateListener o) {
        this.stateListeners.remove(o);
    }
    
    protected void updateBluetoothHeadsetState(final boolean b) {
        if (b == this.isBtHeadsetConnected) {
            return;
        }
        if (BuildVars.LOGS_ENABLED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("updateBluetoothHeadsetState: ");
            sb.append(b);
            FileLog.d(sb.toString());
        }
        this.isBtHeadsetConnected = b;
        final AudioManager audioManager = (AudioManager)this.getSystemService("audio");
        if (b && !this.isRinging() && this.currentState != 0) {
            if (this.bluetoothScoActive) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("SCO already active, setting audio routing");
                }
                audioManager.setSpeakerphoneOn(false);
                audioManager.setBluetoothScoOn(true);
            }
            else {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("startBluetoothSco");
                }
                this.needSwitchToBluetoothAfterScoActivates = true;
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            audioManager.startBluetoothSco();
                        }
                        catch (Throwable t) {}
                    }
                }, 500L);
            }
        }
        else {
            this.bluetoothScoActive = false;
        }
        final Iterator<StateListener> iterator = this.stateListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onAudioSettingsChanged();
        }
    }
    
    protected void updateNetworkType() {
        final NetworkInfo activeNetworkInfo = ((ConnectivityManager)this.getSystemService("connectivity")).getActiveNetworkInfo();
        this.lastNetInfo = activeNetworkInfo;
        int networkType = 1;
        Label_0168: {
            if (activeNetworkInfo != null) {
                final int type = activeNetworkInfo.getType();
                if (type != 0) {
                    if (type == 1) {
                        networkType = 6;
                        break Label_0168;
                    }
                    if (type == 9) {
                        networkType = 7;
                        break Label_0168;
                    }
                }
                else {
                    switch (activeNetworkInfo.getSubtype()) {
                        default: {
                            networkType = 11;
                        }
                        case 1: {
                            break Label_0168;
                        }
                        case 13: {
                            networkType = 5;
                            break Label_0168;
                        }
                        case 6:
                        case 8:
                        case 9:
                        case 10:
                        case 12:
                        case 15: {
                            networkType = 4;
                            break Label_0168;
                        }
                        case 3:
                        case 5: {
                            networkType = 3;
                            break Label_0168;
                        }
                        case 2:
                        case 7: {
                            networkType = 2;
                            break Label_0168;
                        }
                    }
                }
            }
            networkType = 0;
        }
        final VoIPController controller = this.controller;
        if (controller != null) {
            controller.setNetworkType(networkType);
        }
    }
    
    public void updateOutputGainControlState() {
        if (this.controller != null) {
            if (this.controllerStarted) {
                final boolean use_CONNECTION_SERVICE = VoIPBaseService.USE_CONNECTION_SERVICE;
                boolean audioOutputGainControlEnabled = false;
                final int n = 0;
                if (!use_CONNECTION_SERVICE) {
                    final AudioManager audioManager = (AudioManager)this.getSystemService("audio");
                    this.controller.setAudioOutputGainControlEnabled(this.hasEarpiece() && !audioManager.isSpeakerphoneOn() && !audioManager.isBluetoothScoOn() && !this.isHeadsetPlugged);
                    final VoIPController controller = this.controller;
                    int echoCancellationStrength = n;
                    if (!this.isHeadsetPlugged) {
                        if (this.hasEarpiece() && !audioManager.isSpeakerphoneOn() && !audioManager.isBluetoothScoOn() && !this.isHeadsetPlugged) {
                            echoCancellationStrength = n;
                        }
                        else {
                            echoCancellationStrength = 1;
                        }
                    }
                    controller.setEchoCancellationStrength(echoCancellationStrength);
                }
                else {
                    if (this.systemCallConnection.getCallAudioState().getRoute() == 1) {
                        audioOutputGainControlEnabled = true;
                    }
                    this.controller.setAudioOutputGainControlEnabled(audioOutputGainControlEnabled);
                    this.controller.setEchoCancellationStrength((audioOutputGainControlEnabled ^ true) ? 1 : 0);
                }
            }
        }
    }
    
    protected abstract void updateServerConfig();
    
    protected void updateStats() {
        this.controller.getStats(this.stats);
        final Stats stats = this.stats;
        final long bytesSentWifi = stats.bytesSentWifi;
        final Stats prevStats = this.prevStats;
        final long n = bytesSentWifi - prevStats.bytesSentWifi;
        final long n2 = stats.bytesRecvdWifi - prevStats.bytesRecvdWifi;
        final long n3 = stats.bytesSentMobile - prevStats.bytesSentMobile;
        final long n4 = stats.bytesRecvdMobile - prevStats.bytesRecvdMobile;
        this.stats = prevStats;
        this.prevStats = stats;
        if (n > 0L) {
            StatsController.getInstance(this.currentAccount).incrementSentBytesCount(1, 0, n);
        }
        if (n2 > 0L) {
            StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(1, 0, n2);
        }
        final int n5 = 2;
        if (n3 > 0L) {
            final StatsController instance = StatsController.getInstance(this.currentAccount);
            final NetworkInfo lastNetInfo = this.lastNetInfo;
            int n6;
            if (lastNetInfo != null && lastNetInfo.isRoaming()) {
                n6 = 2;
            }
            else {
                n6 = 0;
            }
            instance.incrementSentBytesCount(n6, 0, n3);
        }
        if (n4 > 0L) {
            final StatsController instance2 = StatsController.getInstance(this.currentAccount);
            final NetworkInfo lastNetInfo2 = this.lastNetInfo;
            int n7;
            if (lastNetInfo2 != null && lastNetInfo2.isRoaming()) {
                n7 = n5;
            }
            else {
                n7 = 0;
            }
            instance2.incrementReceivedBytesCount(n7, 0, n4);
        }
    }
    
    @TargetApi(26)
    public class CallConnection extends Connection
    {
        public CallConnection() {
            this.setConnectionProperties(128);
            this.setAudioModeIsVoip(true);
        }
        
        public void onAnswer() {
            VoIPBaseService.this.acceptIncomingCallFromNotification();
        }
        
        public void onCallAudioStateChanged(final CallAudioState obj) {
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("ConnectionService call audio state changed: ");
                sb.append(obj);
                FileLog.d(sb.toString());
            }
            final Iterator<StateListener> iterator = VoIPBaseService.this.stateListeners.iterator();
            while (iterator.hasNext()) {
                ((StateListener)iterator.next()).onAudioSettingsChanged();
            }
        }
        
        public void onCallEvent(final String str, final Bundle bundle) {
            super.onCallEvent(str, bundle);
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("ConnectionService onCallEvent ");
                sb.append(str);
                FileLog.d(sb.toString());
            }
        }
        
        public void onDisconnect() {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("ConnectionService onDisconnect");
            }
            this.setDisconnected(new DisconnectCause(2));
            this.destroy();
            final VoIPBaseService this$0 = VoIPBaseService.this;
            this$0.systemCallConnection = null;
            this$0.hangUp();
        }
        
        public void onReject() {
            final VoIPBaseService this$0 = VoIPBaseService.this;
            this$0.needPlayEndSound = false;
            this$0.declineIncomingCall(1, null);
        }
        
        public void onShowIncomingCallUi() {
            VoIPBaseService.this.startRinging();
        }
        
        public void onSilence() {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("onSlience");
            }
            VoIPBaseService.this.stopRinging();
        }
        
        public void onStateChanged(final int n) {
            super.onStateChanged(n);
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("ConnectionService onStateChanged ");
                sb.append(Connection.stateToString(n));
                FileLog.d(sb.toString());
            }
            if (n == 4) {
                ContactsController.getInstance(VoIPBaseService.this.currentAccount).deleteConnectionServiceContact();
                VoIPBaseService.this.didDeleteConnectionServiceContact = true;
            }
        }
    }
    
    public interface StateListener
    {
        void onAudioSettingsChanged();
        
        void onSignalBarsCountChanged(final int p0);
        
        void onStateChanged(final int p0);
    }
}
