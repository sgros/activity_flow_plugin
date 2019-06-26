// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.emsg;

import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;

public final class EventMessageEncoder
{
    private final ByteArrayOutputStream byteArrayOutputStream;
    private final DataOutputStream dataOutputStream;
    
    public EventMessageEncoder() {
        this.byteArrayOutputStream = new ByteArrayOutputStream(512);
        this.dataOutputStream = new DataOutputStream(this.byteArrayOutputStream);
    }
    
    private static void writeNullTerminatedString(final DataOutputStream dataOutputStream, final String s) throws IOException {
        dataOutputStream.writeBytes(s);
        dataOutputStream.writeByte(0);
    }
    
    private static void writeUnsignedInt(final DataOutputStream dataOutputStream, final long n) throws IOException {
        dataOutputStream.writeByte((int)(n >>> 24) & 0xFF);
        dataOutputStream.writeByte((int)(n >>> 16) & 0xFF);
        dataOutputStream.writeByte((int)(n >>> 8) & 0xFF);
        dataOutputStream.writeByte((int)n & 0xFF);
    }
    
    public byte[] encode(final EventMessage eventMessage, long scaleLargeTimestamp) {
        Assertions.checkArgument(scaleLargeTimestamp >= 0L);
        this.byteArrayOutputStream.reset();
        try {
            writeNullTerminatedString(this.dataOutputStream, eventMessage.schemeIdUri);
            String value;
            if (eventMessage.value != null) {
                value = eventMessage.value;
            }
            else {
                value = "";
            }
            writeNullTerminatedString(this.dataOutputStream, value);
            writeUnsignedInt(this.dataOutputStream, scaleLargeTimestamp);
            writeUnsignedInt(this.dataOutputStream, Util.scaleLargeTimestamp(eventMessage.presentationTimeUs, scaleLargeTimestamp, 1000000L));
            scaleLargeTimestamp = Util.scaleLargeTimestamp(eventMessage.durationMs, scaleLargeTimestamp, 1000L);
            writeUnsignedInt(this.dataOutputStream, scaleLargeTimestamp);
            writeUnsignedInt(this.dataOutputStream, eventMessage.id);
            this.dataOutputStream.write(eventMessage.messageData);
            this.dataOutputStream.flush();
            return this.byteArrayOutputStream.toByteArray();
        }
        catch (IOException cause) {
            throw new RuntimeException(cause);
        }
    }
}
