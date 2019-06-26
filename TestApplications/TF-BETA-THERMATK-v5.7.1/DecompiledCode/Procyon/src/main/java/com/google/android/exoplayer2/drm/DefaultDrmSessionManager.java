// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.drm;

import android.os.Message;
import android.annotation.SuppressLint;
import com.google.android.exoplayer2.util.Log;
import android.os.Handler;
import java.util.Iterator;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.C;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
import android.os.Looper;
import java.util.HashMap;
import com.google.android.exoplayer2.util.EventDispatcher;
import android.annotation.TargetApi;

@TargetApi(18)
public class DefaultDrmSessionManager<T extends ExoMediaCrypto> implements DrmSessionManager<T>, ProvisioningManager<T>
{
    private final MediaDrmCallback callback;
    private final EventDispatcher<DefaultDrmSessionEventListener> eventDispatcher;
    private final int initialDrmRequestRetryCount;
    private final ExoMediaDrm<T> mediaDrm;
    volatile MediaDrmHandler mediaDrmHandler;
    private int mode;
    private final boolean multiSession;
    private byte[] offlineLicenseKeySetId;
    private final HashMap<String, String> optionalKeyRequestParameters;
    private Looper playbackLooper;
    private final List<DefaultDrmSession<T>> provisioningSessions;
    private final List<DefaultDrmSession<T>> sessions;
    private final UUID uuid;
    
    private static List<DrmInitData.SchemeData> getSchemeDatas(final DrmInitData drmInitData, final UUID obj, final boolean b) {
        final ArrayList<DrmInitData.SchemeData> list = new ArrayList<DrmInitData.SchemeData>(drmInitData.schemeDataCount);
        for (int i = 0; i < drmInitData.schemeDataCount; ++i) {
            final DrmInitData.SchemeData value = drmInitData.get(i);
            if ((value.matches(obj) || (C.CLEARKEY_UUID.equals(obj) && value.matches(C.COMMON_PSSH_UUID))) && (value.data != null || b)) {
                list.add(value);
            }
        }
        return list;
    }
    
    @Override
    public DrmSession<T> acquireSession(final Looper playbackLooper, final DrmInitData drmInitData) {
        final Looper playbackLooper2 = this.playbackLooper;
        Assertions.checkState(playbackLooper2 == null || playbackLooper2 == playbackLooper);
        if (this.sessions.isEmpty()) {
            this.playbackLooper = playbackLooper;
            if (this.mediaDrmHandler == null) {
                this.mediaDrmHandler = new MediaDrmHandler(playbackLooper);
            }
        }
        final byte[] offlineLicenseKeySetId = this.offlineLicenseKeySetId;
        final DefaultDrmSession<T> defaultDrmSession = null;
        List<DrmInitData.SchemeData> schemeDatas;
        if (offlineLicenseKeySetId == null) {
            schemeDatas = getSchemeDatas(drmInitData, this.uuid, false);
            if (schemeDatas.isEmpty()) {
                final MissingSchemeDataException ex = new MissingSchemeDataException(this.uuid);
                this.eventDispatcher.dispatch(new _$$Lambda$DefaultDrmSessionManager$lsU4S5fVqixyNsHyDBIvI3jEzVc(ex));
                return new ErrorStateDrmSession<T>(new DrmSession.DrmSessionException(ex));
            }
        }
        else {
            schemeDatas = null;
        }
        DefaultDrmSession<T> defaultDrmSession2;
        if (!this.multiSession) {
            if (this.sessions.isEmpty()) {
                defaultDrmSession2 = defaultDrmSession;
            }
            else {
                defaultDrmSession2 = this.sessions.get(0);
            }
        }
        else {
            final Iterator<DefaultDrmSession<T>> iterator = this.sessions.iterator();
            do {
                defaultDrmSession2 = defaultDrmSession;
                if (!iterator.hasNext()) {
                    break;
                }
                defaultDrmSession2 = iterator.next();
            } while (!Util.areEqual(defaultDrmSession2.schemeDatas, schemeDatas));
        }
        DefaultDrmSession<T> defaultDrmSession3;
        if (defaultDrmSession2 == null) {
            defaultDrmSession3 = new DefaultDrmSession<T>(this.uuid, this.mediaDrm, (DefaultDrmSession.ProvisioningManager<T>)this, schemeDatas, this.mode, this.offlineLicenseKeySetId, this.optionalKeyRequestParameters, this.callback, playbackLooper, this.eventDispatcher, this.initialDrmRequestRetryCount);
            this.sessions.add(defaultDrmSession3);
        }
        else {
            defaultDrmSession3 = defaultDrmSession2;
        }
        defaultDrmSession3.acquire();
        return defaultDrmSession3;
    }
    
    public final void addListener(final Handler handler, final DefaultDrmSessionEventListener defaultDrmSessionEventListener) {
        this.eventDispatcher.addListener(handler, defaultDrmSessionEventListener);
    }
    
    @Override
    public boolean canAcquireSession(final DrmInitData drmInitData) {
        final byte[] offlineLicenseKeySetId = this.offlineLicenseKeySetId;
        final boolean b = true;
        if (offlineLicenseKeySetId != null) {
            return true;
        }
        if (getSchemeDatas(drmInitData, this.uuid, true).isEmpty()) {
            if (drmInitData.schemeDataCount != 1 || !drmInitData.get(0).matches(C.COMMON_PSSH_UUID)) {
                return false;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("DrmInitData only contains common PSSH SchemeData. Assuming support for: ");
            sb.append(this.uuid);
            Log.w("DefaultDrmSessionMgr", sb.toString());
        }
        final String schemeType = drmInitData.schemeType;
        boolean b2 = b;
        if (schemeType != null) {
            if ("cenc".equals(schemeType)) {
                b2 = b;
            }
            else {
                if (!"cbc1".equals(schemeType) && !"cbcs".equals(schemeType) && !"cens".equals(schemeType)) {
                    return true;
                }
                b2 = (Util.SDK_INT >= 25 && b);
            }
        }
        return b2;
    }
    
    @Override
    public void onProvisionCompleted() {
        final Iterator<DefaultDrmSession<T>> iterator = this.provisioningSessions.iterator();
        while (iterator.hasNext()) {
            iterator.next().onProvisionCompleted();
        }
        this.provisioningSessions.clear();
    }
    
    @Override
    public void onProvisionError(final Exception ex) {
        final Iterator<DefaultDrmSession<T>> iterator = this.provisioningSessions.iterator();
        while (iterator.hasNext()) {
            iterator.next().onProvisionError(ex);
        }
        this.provisioningSessions.clear();
    }
    
    @Override
    public void provisionRequired(final DefaultDrmSession<T> defaultDrmSession) {
        if (this.provisioningSessions.contains(defaultDrmSession)) {
            return;
        }
        this.provisioningSessions.add(defaultDrmSession);
        if (this.provisioningSessions.size() == 1) {
            defaultDrmSession.provision();
        }
    }
    
    @Override
    public void releaseSession(final DrmSession<T> drmSession) {
        if (drmSession instanceof ErrorStateDrmSession) {
            return;
        }
        final DefaultDrmSession<T> defaultDrmSession = (DefaultDrmSession<T>)drmSession;
        if (defaultDrmSession.release()) {
            this.sessions.remove(defaultDrmSession);
            if (this.provisioningSessions.size() > 1 && this.provisioningSessions.get(0) == defaultDrmSession) {
                this.provisioningSessions.get(1).provision();
            }
            this.provisioningSessions.remove(defaultDrmSession);
        }
    }
    
    @SuppressLint({ "HandlerLeak" })
    private class MediaDrmHandler extends Handler
    {
        public MediaDrmHandler(final Looper looper) {
            super(looper);
        }
        
        public void handleMessage(final Message message) {
            final byte[] array = (byte[])message.obj;
            if (array == null) {
                return;
            }
            for (final DefaultDrmSession defaultDrmSession : DefaultDrmSessionManager.this.sessions) {
                if (defaultDrmSession.hasSessionId(array)) {
                    defaultDrmSession.onMediaDrmEvent(message.what);
                    break;
                }
            }
        }
    }
    
    public static final class MissingSchemeDataException extends Exception
    {
        private MissingSchemeDataException(final UUID obj) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Media does not support uuid: ");
            sb.append(obj);
            super(sb.toString());
        }
    }
}
