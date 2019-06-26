// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata;

import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import android.os.Message;
import java.util.Arrays;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.Assertions;
import android.os.Looper;
import android.os.Handler;
import com.google.android.exoplayer2.FormatHolder;
import android.os.Handler$Callback;
import com.google.android.exoplayer2.BaseRenderer;

public final class MetadataRenderer extends BaseRenderer implements Handler$Callback
{
    private final MetadataInputBuffer buffer;
    private MetadataDecoder decoder;
    private final MetadataDecoderFactory decoderFactory;
    private final FormatHolder formatHolder;
    private boolean inputStreamEnded;
    private final MetadataOutput output;
    private final Handler outputHandler;
    private final Metadata[] pendingMetadata;
    private int pendingMetadataCount;
    private int pendingMetadataIndex;
    private final long[] pendingMetadataTimestamps;
    
    public MetadataRenderer(final MetadataOutput metadataOutput, final Looper looper) {
        this(metadataOutput, looper, MetadataDecoderFactory.DEFAULT);
    }
    
    public MetadataRenderer(final MetadataOutput metadataOutput, final Looper looper, final MetadataDecoderFactory metadataDecoderFactory) {
        super(4);
        Assertions.checkNotNull(metadataOutput);
        this.output = metadataOutput;
        Handler handler;
        if (looper == null) {
            handler = null;
        }
        else {
            handler = Util.createHandler(looper, (Handler$Callback)this);
        }
        this.outputHandler = handler;
        Assertions.checkNotNull(metadataDecoderFactory);
        this.decoderFactory = metadataDecoderFactory;
        this.formatHolder = new FormatHolder();
        this.buffer = new MetadataInputBuffer();
        this.pendingMetadata = new Metadata[5];
        this.pendingMetadataTimestamps = new long[5];
    }
    
    private void flushPendingMetadata() {
        Arrays.fill(this.pendingMetadata, null);
        this.pendingMetadataIndex = 0;
        this.pendingMetadataCount = 0;
    }
    
    private void invokeRenderer(final Metadata metadata) {
        final Handler outputHandler = this.outputHandler;
        if (outputHandler != null) {
            outputHandler.obtainMessage(0, (Object)metadata).sendToTarget();
        }
        else {
            this.invokeRendererInternal(metadata);
        }
    }
    
    private void invokeRendererInternal(final Metadata metadata) {
        this.output.onMetadata(metadata);
    }
    
    public boolean handleMessage(final Message message) {
        if (message.what == 0) {
            this.invokeRendererInternal((Metadata)message.obj);
            return true;
        }
        throw new IllegalStateException();
    }
    
    public boolean isEnded() {
        return this.inputStreamEnded;
    }
    
    public boolean isReady() {
        return true;
    }
    
    @Override
    protected void onDisabled() {
        this.flushPendingMetadata();
        this.decoder = null;
    }
    
    @Override
    protected void onPositionReset(final long n, final boolean b) {
        this.flushPendingMetadata();
        this.inputStreamEnded = false;
    }
    
    @Override
    protected void onStreamChanged(final Format[] array, final long n) throws ExoPlaybackException {
        this.decoder = this.decoderFactory.createDecoder(array[0]);
    }
    
    public void render(final long n, final long n2) throws ExoPlaybackException {
        if (!this.inputStreamEnded && this.pendingMetadataCount < 5) {
            this.buffer.clear();
            if (this.readSource(this.formatHolder, this.buffer, false) == -4) {
                if (this.buffer.isEndOfStream()) {
                    this.inputStreamEnded = true;
                }
                else if (!this.buffer.isDecodeOnly()) {
                    final MetadataInputBuffer buffer = this.buffer;
                    buffer.subsampleOffsetUs = this.formatHolder.format.subsampleOffsetUs;
                    buffer.flip();
                    final int n3 = (this.pendingMetadataIndex + this.pendingMetadataCount) % 5;
                    final Metadata decode = this.decoder.decode(this.buffer);
                    if (decode != null) {
                        this.pendingMetadata[n3] = decode;
                        this.pendingMetadataTimestamps[n3] = this.buffer.timeUs;
                        ++this.pendingMetadataCount;
                    }
                }
            }
        }
        if (this.pendingMetadataCount > 0) {
            final long[] pendingMetadataTimestamps = this.pendingMetadataTimestamps;
            final int pendingMetadataIndex = this.pendingMetadataIndex;
            if (pendingMetadataTimestamps[pendingMetadataIndex] <= n) {
                this.invokeRenderer(this.pendingMetadata[pendingMetadataIndex]);
                final Metadata[] pendingMetadata = this.pendingMetadata;
                final int pendingMetadataIndex2 = this.pendingMetadataIndex;
                pendingMetadata[pendingMetadataIndex2] = null;
                this.pendingMetadataIndex = (pendingMetadataIndex2 + 1) % 5;
                --this.pendingMetadataCount;
            }
        }
    }
    
    public int supportsFormat(final Format format) {
        if (this.decoderFactory.supportsFormat(format)) {
            int n;
            if (BaseRenderer.supportsFormatDrm(null, format.drmInitData)) {
                n = 4;
            }
            else {
                n = 2;
            }
            return n;
        }
        return 0;
    }
}
