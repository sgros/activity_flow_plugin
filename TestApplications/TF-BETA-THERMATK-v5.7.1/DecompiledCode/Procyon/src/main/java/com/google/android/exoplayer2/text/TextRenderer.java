// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text;

import com.google.android.exoplayer2.decoder.Decoder;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.ExoPlaybackException;
import android.os.Message;
import java.util.List;
import java.util.Collections;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.Assertions;
import android.os.Looper;
import com.google.android.exoplayer2.Format;
import android.os.Handler;
import com.google.android.exoplayer2.FormatHolder;
import android.os.Handler$Callback;
import com.google.android.exoplayer2.BaseRenderer;

public final class TextRenderer extends BaseRenderer implements Handler$Callback
{
    private SubtitleDecoder decoder;
    private final SubtitleDecoderFactory decoderFactory;
    private int decoderReplacementState;
    private final FormatHolder formatHolder;
    private boolean inputStreamEnded;
    private SubtitleInputBuffer nextInputBuffer;
    private SubtitleOutputBuffer nextSubtitle;
    private int nextSubtitleEventIndex;
    private final TextOutput output;
    private final Handler outputHandler;
    private boolean outputStreamEnded;
    private Format streamFormat;
    private SubtitleOutputBuffer subtitle;
    
    public TextRenderer(final TextOutput textOutput, final Looper looper) {
        this(textOutput, looper, SubtitleDecoderFactory.DEFAULT);
    }
    
    public TextRenderer(final TextOutput textOutput, final Looper looper, final SubtitleDecoderFactory decoderFactory) {
        super(3);
        Assertions.checkNotNull(textOutput);
        this.output = textOutput;
        Handler handler;
        if (looper == null) {
            handler = null;
        }
        else {
            handler = Util.createHandler(looper, (Handler$Callback)this);
        }
        this.outputHandler = handler;
        this.decoderFactory = decoderFactory;
        this.formatHolder = new FormatHolder();
    }
    
    private void clearOutput() {
        this.updateOutput(Collections.emptyList());
    }
    
    private long getNextEventTime() {
        final int nextSubtitleEventIndex = this.nextSubtitleEventIndex;
        long eventTime;
        if (nextSubtitleEventIndex != -1 && nextSubtitleEventIndex < this.subtitle.getEventTimeCount()) {
            eventTime = this.subtitle.getEventTime(this.nextSubtitleEventIndex);
        }
        else {
            eventTime = Long.MAX_VALUE;
        }
        return eventTime;
    }
    
    private void invokeUpdateOutputInternal(final List<Cue> list) {
        this.output.onCues(list);
    }
    
    private void releaseBuffers() {
        this.nextInputBuffer = null;
        this.nextSubtitleEventIndex = -1;
        final SubtitleOutputBuffer subtitle = this.subtitle;
        if (subtitle != null) {
            subtitle.release();
            this.subtitle = null;
        }
        final SubtitleOutputBuffer nextSubtitle = this.nextSubtitle;
        if (nextSubtitle != null) {
            nextSubtitle.release();
            this.nextSubtitle = null;
        }
    }
    
    private void releaseDecoder() {
        this.releaseBuffers();
        this.decoder.release();
        this.decoder = null;
        this.decoderReplacementState = 0;
    }
    
    private void replaceDecoder() {
        this.releaseDecoder();
        this.decoder = this.decoderFactory.createDecoder(this.streamFormat);
    }
    
    private void updateOutput(final List<Cue> list) {
        final Handler outputHandler = this.outputHandler;
        if (outputHandler != null) {
            outputHandler.obtainMessage(0, (Object)list).sendToTarget();
        }
        else {
            this.invokeUpdateOutputInternal(list);
        }
    }
    
    public boolean handleMessage(final Message message) {
        if (message.what == 0) {
            this.invokeUpdateOutputInternal((List<Cue>)message.obj);
            return true;
        }
        throw new IllegalStateException();
    }
    
    public boolean isEnded() {
        return this.outputStreamEnded;
    }
    
    public boolean isReady() {
        return true;
    }
    
    @Override
    protected void onDisabled() {
        this.streamFormat = null;
        this.clearOutput();
        this.releaseDecoder();
    }
    
    @Override
    protected void onPositionReset(final long n, final boolean b) {
        this.clearOutput();
        this.inputStreamEnded = false;
        this.outputStreamEnded = false;
        if (this.decoderReplacementState != 0) {
            this.replaceDecoder();
        }
        else {
            this.releaseBuffers();
            this.decoder.flush();
        }
    }
    
    @Override
    protected void onStreamChanged(final Format[] array, final long n) throws ExoPlaybackException {
        this.streamFormat = array[0];
        if (this.decoder != null) {
            this.decoderReplacementState = 1;
        }
        else {
            this.decoder = this.decoderFactory.createDecoder(this.streamFormat);
        }
    }
    
    public void render(final long positionUs, long n) throws ExoPlaybackException {
        if (this.outputStreamEnded) {
            return;
        }
        if (this.nextSubtitle == null) {
            this.decoder.setPositionUs(positionUs);
            try {
                this.nextSubtitle = ((Decoder<I, SubtitleOutputBuffer, E>)this.decoder).dequeueOutputBuffer();
            }
            catch (SubtitleDecoderException ex) {
                throw ExoPlaybackException.createForRenderer(ex, this.getIndex());
            }
        }
        if (this.getState() != 2) {
            return;
        }
        int n2;
        if (this.subtitle != null) {
            n = this.getNextEventTime();
            n2 = 0;
            while (n <= positionUs) {
                ++this.nextSubtitleEventIndex;
                n = this.getNextEventTime();
                n2 = 1;
            }
        }
        else {
            n2 = 0;
        }
        final SubtitleOutputBuffer nextSubtitle = this.nextSubtitle;
        int n3 = n2;
        if (nextSubtitle != null) {
            if (nextSubtitle.isEndOfStream()) {
                n3 = n2;
                if (n2 == 0) {
                    n3 = n2;
                    if (this.getNextEventTime() == Long.MAX_VALUE) {
                        if (this.decoderReplacementState == 2) {
                            this.replaceDecoder();
                            n3 = n2;
                        }
                        else {
                            this.releaseBuffers();
                            this.outputStreamEnded = true;
                            n3 = n2;
                        }
                    }
                }
            }
            else {
                n3 = n2;
                if (this.nextSubtitle.timeUs <= positionUs) {
                    final SubtitleOutputBuffer subtitle = this.subtitle;
                    if (subtitle != null) {
                        subtitle.release();
                    }
                    this.subtitle = this.nextSubtitle;
                    this.nextSubtitle = null;
                    this.nextSubtitleEventIndex = this.subtitle.getNextEventTimeIndex(positionUs);
                    n3 = 1;
                }
            }
        }
        if (n3 != 0) {
            this.updateOutput(this.subtitle.getCues(positionUs));
        }
        if (this.decoderReplacementState == 2) {
            return;
        }
        try {
            while (!this.inputStreamEnded) {
                if (this.nextInputBuffer == null) {
                    this.nextInputBuffer = ((Decoder<SubtitleInputBuffer, O, E>)this.decoder).dequeueInputBuffer();
                    if (this.nextInputBuffer == null) {
                        return;
                    }
                }
                if (this.decoderReplacementState == 1) {
                    this.nextInputBuffer.setFlags(4);
                    ((Decoder<SubtitleInputBuffer, O, E>)this.decoder).queueInputBuffer(this.nextInputBuffer);
                    this.nextInputBuffer = null;
                    this.decoderReplacementState = 2;
                    return;
                }
                final int source = this.readSource(this.formatHolder, this.nextInputBuffer, false);
                if (source == -4) {
                    if (this.nextInputBuffer.isEndOfStream()) {
                        this.inputStreamEnded = true;
                    }
                    else {
                        this.nextInputBuffer.subsampleOffsetUs = this.formatHolder.format.subsampleOffsetUs;
                        this.nextInputBuffer.flip();
                    }
                    ((Decoder<SubtitleInputBuffer, O, E>)this.decoder).queueInputBuffer(this.nextInputBuffer);
                    this.nextInputBuffer = null;
                }
                else {
                    if (source == -3) {
                        break;
                    }
                    continue;
                }
            }
        }
        catch (SubtitleDecoderException ex2) {
            throw ExoPlaybackException.createForRenderer(ex2, this.getIndex());
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
        if (MimeTypes.isText(format.sampleMimeType)) {
            return 1;
        }
        return 0;
    }
}
