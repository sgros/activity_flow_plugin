// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import java.util.List;
import java.util.Map;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;

public abstract class BaseDataSource implements DataSource
{
    private DataSpec dataSpec;
    private final boolean isNetwork;
    private int listenerCount;
    private final ArrayList<TransferListener> listeners;
    
    protected BaseDataSource(final boolean isNetwork) {
        this.isNetwork = isNetwork;
        this.listeners = new ArrayList<TransferListener>(1);
    }
    
    @Override
    public final void addTransferListener(final TransferListener transferListener) {
        if (!this.listeners.contains(transferListener)) {
            this.listeners.add(transferListener);
            ++this.listenerCount;
        }
    }
    
    protected final void bytesTransferred(final int n) {
        final DataSpec dataSpec = this.dataSpec;
        Util.castNonNull(dataSpec);
        final DataSpec dataSpec2 = dataSpec;
        for (int i = 0; i < this.listenerCount; ++i) {
            this.listeners.get(i).onBytesTransferred(this, dataSpec2, this.isNetwork, n);
        }
    }
    
    protected final void transferEnded() {
        final DataSpec dataSpec = this.dataSpec;
        Util.castNonNull(dataSpec);
        final DataSpec dataSpec2 = dataSpec;
        for (int i = 0; i < this.listenerCount; ++i) {
            this.listeners.get(i).onTransferEnd(this, dataSpec2, this.isNetwork);
        }
        this.dataSpec = null;
    }
    
    protected final void transferInitializing(final DataSpec dataSpec) {
        for (int i = 0; i < this.listenerCount; ++i) {
            this.listeners.get(i).onTransferInitializing(this, dataSpec, this.isNetwork);
        }
    }
    
    protected final void transferStarted(final DataSpec dataSpec) {
        this.dataSpec = dataSpec;
        for (int i = 0; i < this.listenerCount; ++i) {
            this.listeners.get(i).onTransferStart(this, dataSpec, this.isNetwork);
        }
    }
}
