// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.drm;

import android.os.Message;
import android.annotation.SuppressLint;
import android.os.Handler;
import java.util.Map;
import java.util.Arrays;
import android.media.NotProvisionedException;
import com.google.android.exoplayer2.util.Util;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;
import android.util.Pair;
import com.google.android.exoplayer2.C;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;
import com.google.android.exoplayer2.util.Log;
import java.util.Collections;
import com.google.android.exoplayer2.util.Assertions;
import android.os.Looper;
import java.util.UUID;
import java.util.List;
import android.os.HandlerThread;
import java.util.HashMap;
import com.google.android.exoplayer2.util.EventDispatcher;
import android.annotation.TargetApi;

@TargetApi(18)
class DefaultDrmSession<T extends ExoMediaCrypto> implements DrmSession<T>
{
    final MediaDrmCallback callback;
    private ExoMediaDrm.KeyRequest currentKeyRequest;
    private ExoMediaDrm.ProvisionRequest currentProvisionRequest;
    private final EventDispatcher<DefaultDrmSessionEventListener> eventDispatcher;
    private final int initialDrmRequestRetryCount;
    private DrmSessionException lastException;
    private T mediaCrypto;
    private final ExoMediaDrm<T> mediaDrm;
    private final int mode;
    private byte[] offlineLicenseKeySetId;
    private int openCount;
    private final HashMap<String, String> optionalKeyRequestParameters;
    private PostRequestHandler postRequestHandler;
    final PostResponseHandler postResponseHandler;
    private final ProvisioningManager<T> provisioningManager;
    private HandlerThread requestHandlerThread;
    public final List<DrmInitData.SchemeData> schemeDatas;
    private byte[] sessionId;
    private int state;
    final UUID uuid;
    
    public DefaultDrmSession(final UUID uuid, final ExoMediaDrm<T> mediaDrm, final ProvisioningManager<T> provisioningManager, final List<DrmInitData.SchemeData> list, final int mode, final byte[] offlineLicenseKeySetId, final HashMap<String, String> optionalKeyRequestParameters, final MediaDrmCallback callback, final Looper looper, final EventDispatcher<DefaultDrmSessionEventListener> eventDispatcher, final int initialDrmRequestRetryCount) {
        if (mode == 1 || mode == 3) {
            Assertions.checkNotNull(offlineLicenseKeySetId);
        }
        this.uuid = uuid;
        this.provisioningManager = provisioningManager;
        this.mediaDrm = mediaDrm;
        this.mode = mode;
        if (offlineLicenseKeySetId != null) {
            this.offlineLicenseKeySetId = offlineLicenseKeySetId;
            this.schemeDatas = null;
        }
        else {
            Assertions.checkNotNull(list);
            this.schemeDatas = Collections.unmodifiableList((List<? extends DrmInitData.SchemeData>)list);
        }
        this.optionalKeyRequestParameters = optionalKeyRequestParameters;
        this.callback = callback;
        this.initialDrmRequestRetryCount = initialDrmRequestRetryCount;
        this.eventDispatcher = eventDispatcher;
        this.state = 2;
        this.postResponseHandler = new PostResponseHandler(looper);
        (this.requestHandlerThread = new HandlerThread("DrmRequestHandler")).start();
        this.postRequestHandler = new PostRequestHandler(this.requestHandlerThread.getLooper());
    }
    
    @RequiresNonNull({ "sessionId" })
    private void doLicense(final boolean b) {
        final int mode = this.mode;
        if (mode != 0 && mode != 1) {
            if (mode != 2) {
                if (mode == 3) {
                    Assertions.checkNotNull(this.offlineLicenseKeySetId);
                    if (this.restoreKeys()) {
                        this.postKeyRequest(this.offlineLicenseKeySetId, 3, b);
                    }
                }
            }
            else if (this.offlineLicenseKeySetId == null) {
                this.postKeyRequest(this.sessionId, 2, b);
            }
            else if (this.restoreKeys()) {
                this.postKeyRequest(this.sessionId, 2, b);
            }
        }
        else if (this.offlineLicenseKeySetId == null) {
            this.postKeyRequest(this.sessionId, 1, b);
        }
        else if (this.state == 4 || this.restoreKeys()) {
            final long licenseDurationRemainingSec = this.getLicenseDurationRemainingSec();
            if (this.mode == 0 && licenseDurationRemainingSec <= 60L) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Offline license has expired or will expire soon. Remaining seconds: ");
                sb.append(licenseDurationRemainingSec);
                Log.d("DefaultDrmSession", sb.toString());
                this.postKeyRequest(this.sessionId, 2, b);
            }
            else if (licenseDurationRemainingSec <= 0L) {
                this.onError(new KeysExpiredException());
            }
            else {
                this.state = 4;
                this.eventDispatcher.dispatch((EventDispatcher.Event<DefaultDrmSessionEventListener>)_$$Lambda$tzysvANfjWo6mXRxYD2fQMdks_4.INSTANCE);
            }
        }
    }
    
    private long getLicenseDurationRemainingSec() {
        if (!C.WIDEVINE_UUID.equals(this.uuid)) {
            return Long.MAX_VALUE;
        }
        final Pair<Long, Long> licenseDurationRemainingSec = WidevineUtil.getLicenseDurationRemainingSec(this);
        Assertions.checkNotNull(licenseDurationRemainingSec);
        final Pair<Long, Long> pair = licenseDurationRemainingSec;
        return Math.min((long)pair.first, (long)pair.second);
    }
    
    @EnsuresNonNullIf(expression = { "sessionId" }, result = true)
    private boolean isOpen() {
        final int state = this.state;
        return state == 3 || state == 4;
    }
    
    private void onError(final Exception ex) {
        this.lastException = new DrmSessionException(ex);
        this.eventDispatcher.dispatch(new _$$Lambda$DefaultDrmSession$_nKOJC1w2998gRg4Cg4l2mjlp30(ex));
        if (this.state != 4) {
            this.state = 1;
        }
    }
    
    private void onKeyResponse(final Object o, final Object o2) {
        if (o == this.currentKeyRequest) {
            if (this.isOpen()) {
                this.currentKeyRequest = null;
                if (o2 instanceof Exception) {
                    this.onKeysError((Exception)o2);
                    return;
                }
                try {
                    final byte[] array = (byte[])o2;
                    if (this.mode == 3) {
                        final ExoMediaDrm<T> mediaDrm = this.mediaDrm;
                        final byte[] offlineLicenseKeySetId = this.offlineLicenseKeySetId;
                        Util.castNonNull(offlineLicenseKeySetId);
                        mediaDrm.provideKeyResponse(offlineLicenseKeySetId, array);
                        this.eventDispatcher.dispatch((EventDispatcher.Event<DefaultDrmSessionEventListener>)_$$Lambda$tzysvANfjWo6mXRxYD2fQMdks_4.INSTANCE);
                    }
                    else {
                        final byte[] provideKeyResponse = this.mediaDrm.provideKeyResponse(this.sessionId, array);
                        if ((this.mode == 2 || (this.mode == 0 && this.offlineLicenseKeySetId != null)) && provideKeyResponse != null && provideKeyResponse.length != 0) {
                            this.offlineLicenseKeySetId = provideKeyResponse;
                        }
                        this.state = 4;
                        this.eventDispatcher.dispatch((EventDispatcher.Event<DefaultDrmSessionEventListener>)_$$Lambda$wyKVEWJALn1OyjwryLo2GUxlQ2M.INSTANCE);
                    }
                }
                catch (Exception ex) {
                    this.onKeysError(ex);
                }
            }
        }
    }
    
    private void onKeysError(final Exception ex) {
        if (ex instanceof NotProvisionedException) {
            this.provisioningManager.provisionRequired(this);
        }
        else {
            this.onError(ex);
        }
    }
    
    private void onKeysRequired() {
        if (this.mode == 0 && this.state == 4) {
            Util.castNonNull(this.sessionId);
            this.doLicense(false);
        }
    }
    
    private void onProvisionResponse(final Object o, final Object o2) {
        if (o == this.currentProvisionRequest) {
            if (this.state == 2 || this.isOpen()) {
                this.currentProvisionRequest = null;
                if (o2 instanceof Exception) {
                    this.provisioningManager.onProvisionError((Exception)o2);
                    return;
                }
                try {
                    this.mediaDrm.provideProvisionResponse((byte[])o2);
                    this.provisioningManager.onProvisionCompleted();
                }
                catch (Exception ex) {
                    this.provisioningManager.onProvisionError(ex);
                }
            }
        }
    }
    
    @EnsuresNonNullIf(expression = { "sessionId" }, result = true)
    private boolean openInternal(final boolean b) {
        if (this.isOpen()) {
            return true;
        }
        try {
            this.sessionId = this.mediaDrm.openSession();
            this.eventDispatcher.dispatch((EventDispatcher.Event<DefaultDrmSessionEventListener>)_$$Lambda$jFcVU4qXZB2nhSZWHXCB9S7MtRI.INSTANCE);
            this.mediaCrypto = this.mediaDrm.createMediaCrypto(this.sessionId);
            this.state = 3;
            return true;
        }
        catch (Exception ex) {
            this.onError(ex);
        }
        catch (NotProvisionedException ex2) {
            if (b) {
                this.provisioningManager.provisionRequired(this);
            }
            else {
                this.onError((Exception)ex2);
            }
        }
        return false;
    }
    
    private void postKeyRequest(final byte[] array, final int n, final boolean b) {
        try {
            this.currentKeyRequest = this.mediaDrm.getKeyRequest(array, this.schemeDatas, n, this.optionalKeyRequestParameters);
            this.postRequestHandler.post(1, this.currentKeyRequest, b);
        }
        catch (Exception ex) {
            this.onKeysError(ex);
        }
    }
    
    @RequiresNonNull({ "sessionId", "offlineLicenseKeySetId" })
    private boolean restoreKeys() {
        try {
            this.mediaDrm.restoreKeys(this.sessionId, this.offlineLicenseKeySetId);
            return true;
        }
        catch (Exception ex) {
            Log.e("DefaultDrmSession", "Error trying to restore Widevine keys.", ex);
            this.onError(ex);
            return false;
        }
    }
    
    public void acquire() {
        final int openCount = this.openCount + 1;
        this.openCount = openCount;
        if (openCount == 1) {
            if (this.state == 1) {
                return;
            }
            if (this.openInternal(true)) {
                this.doLicense(true);
            }
        }
    }
    
    @Override
    public final DrmSessionException getError() {
        DrmSessionException lastException;
        if (this.state == 1) {
            lastException = this.lastException;
        }
        else {
            lastException = null;
        }
        return lastException;
    }
    
    @Override
    public final T getMediaCrypto() {
        return this.mediaCrypto;
    }
    
    @Override
    public final int getState() {
        return this.state;
    }
    
    public boolean hasSessionId(final byte[] a2) {
        return Arrays.equals(this.sessionId, a2);
    }
    
    public void onMediaDrmEvent(final int n) {
        if (n == 2) {
            this.onKeysRequired();
        }
    }
    
    public void onProvisionCompleted() {
        if (this.openInternal(false)) {
            this.doLicense(true);
        }
    }
    
    public void onProvisionError(final Exception ex) {
        this.onError(ex);
    }
    
    public void provision() {
        this.currentProvisionRequest = this.mediaDrm.getProvisionRequest();
        this.postRequestHandler.post(0, this.currentProvisionRequest, true);
    }
    
    @Override
    public Map<String, String> queryKeyStatus() {
        final byte[] sessionId = this.sessionId;
        Map<String, String> queryKeyStatus;
        if (sessionId == null) {
            queryKeyStatus = null;
        }
        else {
            queryKeyStatus = this.mediaDrm.queryKeyStatus(sessionId);
        }
        return queryKeyStatus;
    }
    
    public boolean release() {
        final int openCount = this.openCount - 1;
        this.openCount = openCount;
        if (openCount == 0) {
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
            final byte[] sessionId = this.sessionId;
            if (sessionId != null) {
                this.mediaDrm.closeSession(sessionId);
                this.sessionId = null;
                this.eventDispatcher.dispatch((EventDispatcher.Event<DefaultDrmSessionEventListener>)_$$Lambda$1U2yJBSMBm8ESUSz9LUzNXtoVus.INSTANCE);
            }
            return true;
        }
        return false;
    }
    
    @SuppressLint({ "HandlerLeak" })
    private class PostRequestHandler extends Handler
    {
        public PostRequestHandler(final Looper looper) {
            super(looper);
        }
        
        private long getRetryDelayMillis(final int n) {
            return Math.min((n - 1) * 1000, 5000);
        }
        
        private boolean maybeRetryRequest(Message obtain) {
            if (obtain.arg1 != 1) {
                return false;
            }
            final int arg2 = obtain.arg2 + 1;
            if (arg2 > DefaultDrmSession.this.initialDrmRequestRetryCount) {
                return false;
            }
            obtain = Message.obtain(obtain);
            obtain.arg2 = arg2;
            this.sendMessageDelayed(obtain, this.getRetryDelayMillis(arg2));
            return true;
        }
        
        public void handleMessage(final Message message) {
            final Object obj = message.obj;
            byte[] array = null;
            try {
                final int what = message.what;
                if (what != 0) {
                    if (what != 1) {
                        throw new RuntimeException();
                    }
                    array = DefaultDrmSession.this.callback.executeKeyRequest(DefaultDrmSession.this.uuid, (ExoMediaDrm.KeyRequest)obj);
                }
                else {
                    array = DefaultDrmSession.this.callback.executeProvisionRequest(DefaultDrmSession.this.uuid, (ExoMediaDrm.ProvisionRequest)obj);
                }
            }
            catch (Exception array) {
                if (this.maybeRetryRequest(message)) {
                    return;
                }
            }
            DefaultDrmSession.this.postResponseHandler.obtainMessage(message.what, (Object)Pair.create(obj, (Object)array)).sendToTarget();
        }
        
        void post(final int n, final Object o, final boolean b) {
            this.obtainMessage(n, (int)(b ? 1 : 0), 0, o).sendToTarget();
        }
    }
    
    @SuppressLint({ "HandlerLeak" })
    private class PostResponseHandler extends Handler
    {
        public PostResponseHandler(final Looper looper) {
            super(looper);
        }
        
        public void handleMessage(final Message message) {
            final Pair pair = (Pair)message.obj;
            final Object first = pair.first;
            final Object second = pair.second;
            final int what = message.what;
            if (what != 0) {
                if (what == 1) {
                    DefaultDrmSession.this.onKeyResponse(first, second);
                }
            }
            else {
                DefaultDrmSession.this.onProvisionResponse(first, second);
            }
        }
    }
    
    public interface ProvisioningManager<T extends ExoMediaCrypto>
    {
        void onProvisionCompleted();
        
        void onProvisionError(final Exception p0);
        
        void provisionRequired(final DefaultDrmSession<T> p0);
    }
}
