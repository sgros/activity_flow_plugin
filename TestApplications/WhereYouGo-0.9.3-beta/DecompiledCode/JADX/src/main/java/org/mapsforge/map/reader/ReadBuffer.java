package org.mapsforge.map.reader;

import android.support.p000v4.media.TransportMediator;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

public class ReadBuffer {
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final Logger LOGGER = Logger.getLogger(ReadBuffer.class.getName());
    static final int MAXIMUM_BUFFER_SIZE = 2500000;
    private byte[] bufferData;
    private int bufferPosition;
    private final RandomAccessFile inputFile;

    ReadBuffer(RandomAccessFile inputFile) {
        this.inputFile = inputFile;
    }

    public byte readByte() {
        byte[] bArr = this.bufferData;
        int i = this.bufferPosition;
        this.bufferPosition = i + 1;
        return bArr[i];
    }

    public boolean readFromFile(int length) throws IOException {
        if (this.bufferData == null || this.bufferData.length < length) {
            if (length > MAXIMUM_BUFFER_SIZE) {
                LOGGER.warning("invalid read length: " + length);
                return false;
            }
            this.bufferData = new byte[length];
        }
        this.bufferPosition = 0;
        if (this.inputFile.read(this.bufferData, 0, length) == length) {
            return true;
        }
        return false;
    }

    public int readInt() {
        this.bufferPosition += 4;
        return Deserializer.getInt(this.bufferData, this.bufferPosition - 4);
    }

    public long readLong() {
        this.bufferPosition += 8;
        return Deserializer.getLong(this.bufferData, this.bufferPosition - 8);
    }

    public int readShort() {
        this.bufferPosition += 2;
        return Deserializer.getShort(this.bufferData, this.bufferPosition - 2);
    }

    public int readSignedInt() {
        byte[] bArr;
        int i;
        int variableByteDecode = 0;
        byte variableByteShift = (byte) 0;
        while ((this.bufferData[this.bufferPosition] & 128) != 0) {
            bArr = this.bufferData;
            i = this.bufferPosition;
            this.bufferPosition = i + 1;
            variableByteDecode |= (bArr[i] & TransportMediator.KEYCODE_MEDIA_PAUSE) << variableByteShift;
            variableByteShift = (byte) (variableByteShift + 7);
        }
        if ((this.bufferData[this.bufferPosition] & 64) != 0) {
            bArr = this.bufferData;
            i = this.bufferPosition;
            this.bufferPosition = i + 1;
            return -(((bArr[i] & 63) << variableByteShift) | variableByteDecode);
        }
        bArr = this.bufferData;
        i = this.bufferPosition;
        this.bufferPosition = i + 1;
        return ((bArr[i] & 63) << variableByteShift) | variableByteDecode;
    }

    public int readUnsignedInt() {
        byte[] bArr;
        int i;
        int variableByteDecode = 0;
        byte variableByteShift = (byte) 0;
        while ((this.bufferData[this.bufferPosition] & 128) != 0) {
            bArr = this.bufferData;
            i = this.bufferPosition;
            this.bufferPosition = i + 1;
            variableByteDecode |= (bArr[i] & TransportMediator.KEYCODE_MEDIA_PAUSE) << variableByteShift;
            variableByteShift = (byte) (variableByteShift + 7);
        }
        bArr = this.bufferData;
        i = this.bufferPosition;
        this.bufferPosition = i + 1;
        return (bArr[i] << variableByteShift) | variableByteDecode;
    }

    public String readUTF8EncodedString() {
        return readUTF8EncodedString(readUnsignedInt());
    }

    public String readUTF8EncodedString(int stringLength) {
        if (stringLength <= 0 || this.bufferPosition + stringLength > this.bufferData.length) {
            LOGGER.warning("invalid string length: " + stringLength);
            return null;
        }
        this.bufferPosition += stringLength;
        try {
            return new String(this.bufferData, this.bufferPosition - stringLength, stringLength, CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int getBufferPosition() {
        return this.bufferPosition;
    }

    /* Access modifiers changed, original: 0000 */
    public int getBufferSize() {
        return this.bufferData.length;
    }

    /* Access modifiers changed, original: 0000 */
    public void setBufferPosition(int bufferPosition) {
        this.bufferPosition = bufferPosition;
    }

    /* Access modifiers changed, original: 0000 */
    public void skipBytes(int bytes) {
        this.bufferPosition += bytes;
    }
}
