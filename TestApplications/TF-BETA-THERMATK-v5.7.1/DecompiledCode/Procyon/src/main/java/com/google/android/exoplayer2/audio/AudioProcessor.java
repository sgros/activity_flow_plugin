// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;

public interface AudioProcessor
{
    public static final ByteBuffer EMPTY_BUFFER = ByteBuffer.allocateDirect(0).order(ByteOrder.nativeOrder());
    
    boolean configure(final int p0, final int p1, final int p2) throws UnhandledFormatException;
    
    void flush();
    
    ByteBuffer getOutput();
    
    int getOutputChannelCount();
    
    int getOutputEncoding();
    
    int getOutputSampleRateHz();
    
    boolean isActive();
    
    boolean isEnded();
    
    void queueEndOfStream();
    
    void queueInput(final ByteBuffer p0);
    
    void reset();
    
    public static final class UnhandledFormatException extends Exception
    {
        public UnhandledFormatException(final int i, final int j, final int k) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unhandled format: ");
            sb.append(i);
            sb.append(" Hz, ");
            sb.append(j);
            sb.append(" channels in encoding ");
            sb.append(k);
            super(sb.toString());
        }
    }
}
