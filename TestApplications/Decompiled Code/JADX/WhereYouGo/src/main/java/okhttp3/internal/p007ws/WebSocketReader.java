package okhttp3.internal.p007ws;

import java.io.EOFException;
import java.io.IOException;
import java.net.ProtocolException;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;

/* renamed from: okhttp3.internal.ws.WebSocketReader */
final class WebSocketReader {
    boolean closed;
    long frameBytesRead;
    final FrameCallback frameCallback;
    long frameLength;
    final boolean isClient;
    boolean isControlFrame;
    boolean isFinalFrame;
    boolean isMasked;
    final byte[] maskBuffer = new byte[8192];
    final byte[] maskKey = new byte[4];
    int opcode;
    final BufferedSource source;

    /* renamed from: okhttp3.internal.ws.WebSocketReader$FrameCallback */
    public interface FrameCallback {
        void onReadClose(int i, String str);

        void onReadMessage(String str) throws IOException;

        void onReadMessage(ByteString byteString) throws IOException;

        void onReadPing(ByteString byteString);

        void onReadPong(ByteString byteString);
    }

    WebSocketReader(boolean isClient, BufferedSource source, FrameCallback frameCallback) {
        if (source == null) {
            throw new NullPointerException("source == null");
        } else if (frameCallback == null) {
            throw new NullPointerException("frameCallback == null");
        } else {
            this.isClient = isClient;
            this.source = source;
            this.frameCallback = frameCallback;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void processNextFrame() throws IOException {
        readHeader();
        if (this.isControlFrame) {
            readControlFrame();
        } else {
            readMessageFrame();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0067  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x006d  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0072  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x008a  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0077  */
    private void readHeader() throws java.io.IOException {
        /*
        r14 = this;
        r12 = 0;
        r8 = 1;
        r9 = 0;
        r5 = r14.closed;
        if (r5 == 0) goto L_0x0010;
    L_0x0008:
        r5 = new java.io.IOException;
        r8 = "closed";
        r5.<init>(r8);
        throw r5;
    L_0x0010:
        r5 = r14.source;
        r5 = r5.timeout();
        r6 = r5.timeoutNanos();
        r5 = r14.source;
        r5 = r5.timeout();
        r5.clearTimeout();
        r5 = r14.source;	 Catch:{ all -> 0x0058 }
        r5 = r5.readByte();	 Catch:{ all -> 0x0058 }
        r0 = r5 & 255;
        r5 = r14.source;
        r5 = r5.timeout();
        r10 = java.util.concurrent.TimeUnit.NANOSECONDS;
        r5.timeout(r6, r10);
        r5 = r0 & 15;
        r14.opcode = r5;
        r5 = r0 & 128;
        if (r5 == 0) goto L_0x0065;
    L_0x003e:
        r5 = r8;
    L_0x003f:
        r14.isFinalFrame = r5;
        r5 = r0 & 8;
        if (r5 == 0) goto L_0x0067;
    L_0x0045:
        r5 = r8;
    L_0x0046:
        r14.isControlFrame = r5;
        r5 = r14.isControlFrame;
        if (r5 == 0) goto L_0x0069;
    L_0x004c:
        r5 = r14.isFinalFrame;
        if (r5 != 0) goto L_0x0069;
    L_0x0050:
        r5 = new java.net.ProtocolException;
        r8 = "Control frames must be final.";
        r5.<init>(r8);
        throw r5;
    L_0x0058:
        r5 = move-exception;
        r8 = r14.source;
        r8 = r8.timeout();
        r9 = java.util.concurrent.TimeUnit.NANOSECONDS;
        r8.timeout(r6, r9);
        throw r5;
    L_0x0065:
        r5 = r9;
        goto L_0x003f;
    L_0x0067:
        r5 = r9;
        goto L_0x0046;
    L_0x0069:
        r5 = r0 & 64;
        if (r5 == 0) goto L_0x0086;
    L_0x006d:
        r2 = r8;
    L_0x006e:
        r5 = r0 & 32;
        if (r5 == 0) goto L_0x0088;
    L_0x0072:
        r3 = r8;
    L_0x0073:
        r5 = r0 & 16;
        if (r5 == 0) goto L_0x008a;
    L_0x0077:
        r4 = r8;
    L_0x0078:
        if (r2 != 0) goto L_0x007e;
    L_0x007a:
        if (r3 != 0) goto L_0x007e;
    L_0x007c:
        if (r4 == 0) goto L_0x008c;
    L_0x007e:
        r5 = new java.net.ProtocolException;
        r8 = "Reserved flags are unsupported.";
        r5.<init>(r8);
        throw r5;
    L_0x0086:
        r2 = r9;
        goto L_0x006e;
    L_0x0088:
        r3 = r9;
        goto L_0x0073;
    L_0x008a:
        r4 = r9;
        goto L_0x0078;
    L_0x008c:
        r5 = r14.source;
        r5 = r5.readByte();
        r1 = r5 & 255;
        r5 = r1 & 128;
        if (r5 == 0) goto L_0x00ac;
    L_0x0098:
        r14.isMasked = r8;
        r5 = r14.isMasked;
        r8 = r14.isClient;
        if (r5 != r8) goto L_0x00b1;
    L_0x00a0:
        r8 = new java.net.ProtocolException;
        r5 = r14.isClient;
        if (r5 == 0) goto L_0x00ae;
    L_0x00a6:
        r5 = "Server-sent frames must not be masked.";
    L_0x00a8:
        r8.<init>(r5);
        throw r8;
    L_0x00ac:
        r8 = r9;
        goto L_0x0098;
    L_0x00ae:
        r5 = "Client-sent frames must be masked.";
        goto L_0x00a8;
    L_0x00b1:
        r5 = r1 & 127;
        r8 = (long) r5;
        r14.frameLength = r8;
        r8 = r14.frameLength;
        r10 = 126; // 0x7e float:1.77E-43 double:6.23E-322;
        r5 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r5 != 0) goto L_0x00e1;
    L_0x00be:
        r5 = r14.source;
        r5 = r5.readShort();
        r8 = (long) r5;
        r10 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        r8 = r8 & r10;
        r14.frameLength = r8;
    L_0x00cb:
        r14.frameBytesRead = r12;
        r5 = r14.isControlFrame;
        if (r5 == 0) goto L_0x011c;
    L_0x00d1:
        r8 = r14.frameLength;
        r10 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r5 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r5 <= 0) goto L_0x011c;
    L_0x00d9:
        r5 = new java.net.ProtocolException;
        r8 = "Control frame must be less than 125B.";
        r5.<init>(r8);
        throw r5;
    L_0x00e1:
        r8 = r14.frameLength;
        r10 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r5 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r5 != 0) goto L_0x00cb;
    L_0x00e9:
        r5 = r14.source;
        r8 = r5.readLong();
        r14.frameLength = r8;
        r8 = r14.frameLength;
        r5 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1));
        if (r5 >= 0) goto L_0x00cb;
    L_0x00f7:
        r5 = new java.net.ProtocolException;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "Frame length 0x";
        r8 = r8.append(r9);
        r10 = r14.frameLength;
        r9 = java.lang.Long.toHexString(r10);
        r8 = r8.append(r9);
        r9 = " > 0x7FFFFFFFFFFFFFFF";
        r8 = r8.append(r9);
        r8 = r8.toString();
        r5.<init>(r8);
        throw r5;
    L_0x011c:
        r5 = r14.isMasked;
        if (r5 == 0) goto L_0x0127;
    L_0x0120:
        r5 = r14.source;
        r8 = r14.maskKey;
        r5.readFully(r8);
    L_0x0127:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.p007ws.WebSocketReader.readHeader():void");
    }

    private void readControlFrame() throws IOException {
        Buffer buffer = new Buffer();
        if (this.frameBytesRead < this.frameLength) {
            if (this.isClient) {
                this.source.readFully(buffer, this.frameLength);
            } else {
                while (this.frameBytesRead < this.frameLength) {
                    int read = this.source.read(this.maskBuffer, 0, (int) Math.min(this.frameLength - this.frameBytesRead, (long) this.maskBuffer.length));
                    if (read == -1) {
                        throw new EOFException();
                    }
                    WebSocketProtocol.toggleMask(this.maskBuffer, (long) read, this.maskKey, this.frameBytesRead);
                    buffer.write(this.maskBuffer, 0, read);
                    this.frameBytesRead += (long) read;
                }
            }
        }
        switch (this.opcode) {
            case 8:
                int code = 1005;
                String reason = "";
                long bufferSize = buffer.size();
                if (bufferSize == 1) {
                    throw new ProtocolException("Malformed close payload length of 1.");
                }
                if (bufferSize != 0) {
                    code = buffer.readShort();
                    reason = buffer.readUtf8();
                    String codeExceptionMessage = WebSocketProtocol.closeCodeExceptionMessage(code);
                    if (codeExceptionMessage != null) {
                        throw new ProtocolException(codeExceptionMessage);
                    }
                }
                this.frameCallback.onReadClose(code, reason);
                this.closed = true;
                return;
            case 9:
                this.frameCallback.onReadPing(buffer.readByteString());
                return;
            case 10:
                this.frameCallback.onReadPong(buffer.readByteString());
                return;
            default:
                throw new ProtocolException("Unknown control opcode: " + Integer.toHexString(this.opcode));
        }
    }

    private void readMessageFrame() throws IOException {
        int opcode = this.opcode;
        if (opcode == 1 || opcode == 2) {
            Buffer message = new Buffer();
            readMessage(message);
            if (opcode == 1) {
                this.frameCallback.onReadMessage(message.readUtf8());
                return;
            } else {
                this.frameCallback.onReadMessage(message.readByteString());
                return;
            }
        }
        throw new ProtocolException("Unknown opcode: " + Integer.toHexString(opcode));
    }

    /* Access modifiers changed, original: 0000 */
    public void readUntilNonControlFrame() throws IOException {
        while (!this.closed) {
            readHeader();
            if (this.isControlFrame) {
                readControlFrame();
            } else {
                return;
            }
        }
    }

    private void readMessage(Buffer sink) throws IOException {
        while (!this.closed) {
            long read;
            if (this.frameBytesRead == this.frameLength) {
                if (!this.isFinalFrame) {
                    readUntilNonControlFrame();
                    if (this.opcode != 0) {
                        throw new ProtocolException("Expected continuation opcode. Got: " + Integer.toHexString(this.opcode));
                    } else if (this.isFinalFrame && this.frameLength == 0) {
                        return;
                    }
                }
                return;
            }
            long toRead = this.frameLength - this.frameBytesRead;
            if (this.isMasked) {
                read = (long) this.source.read(this.maskBuffer, 0, (int) Math.min(toRead, (long) this.maskBuffer.length));
                if (read == -1) {
                    throw new EOFException();
                }
                WebSocketProtocol.toggleMask(this.maskBuffer, read, this.maskKey, this.frameBytesRead);
                sink.write(this.maskBuffer, 0, (int) read);
            } else {
                read = this.source.read(sink, toRead);
                if (read == -1) {
                    throw new EOFException();
                }
            }
            this.frameBytesRead += read;
        }
        throw new IOException("closed");
    }
}
