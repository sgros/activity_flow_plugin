package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.upstream.DataSource.C0224-CC;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseDataSource implements DataSource {
    private DataSpec dataSpec;
    private final boolean isNetwork;
    private int listenerCount;
    private final ArrayList<TransferListener> listeners = new ArrayList(1);

    public /* synthetic */ Map<String, List<String>> getResponseHeaders() {
        return C0224-CC.$default$getResponseHeaders(this);
    }

    protected BaseDataSource(boolean z) {
        this.isNetwork = z;
    }

    public final void addTransferListener(TransferListener transferListener) {
        if (!this.listeners.contains(transferListener)) {
            this.listeners.add(transferListener);
            this.listenerCount++;
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void transferInitializing(DataSpec dataSpec) {
        for (int i = 0; i < this.listenerCount; i++) {
            ((TransferListener) this.listeners.get(i)).onTransferInitializing(this, dataSpec, this.isNetwork);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void transferStarted(DataSpec dataSpec) {
        this.dataSpec = dataSpec;
        for (int i = 0; i < this.listenerCount; i++) {
            ((TransferListener) this.listeners.get(i)).onTransferStart(this, dataSpec, this.isNetwork);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void bytesTransferred(int i) {
        DataSpec dataSpec = this.dataSpec;
        Util.castNonNull(dataSpec);
        dataSpec = dataSpec;
        for (int i2 = 0; i2 < this.listenerCount; i2++) {
            ((TransferListener) this.listeners.get(i2)).onBytesTransferred(this, dataSpec, this.isNetwork, i);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void transferEnded() {
        DataSpec dataSpec = this.dataSpec;
        Util.castNonNull(dataSpec);
        dataSpec = dataSpec;
        for (int i = 0; i < this.listenerCount; i++) {
            ((TransferListener) this.listeners.get(i)).onTransferEnd(this, dataSpec, this.isNetwork);
        }
        this.dataSpec = null;
    }
}
