// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.tgnet.SerializedData;

public class MessageKeyData
{
    public byte[] aesIv;
    public byte[] aesKey;
    
    public static MessageKeyData generateMessageKeyData(byte[] array, final byte[] array2, final boolean b, final int n) {
        final MessageKeyData messageKeyData = new MessageKeyData();
        if (array != null && array.length != 0) {
            int n2;
            if (b) {
                n2 = 8;
            }
            else {
                n2 = 0;
            }
            if (n != 1) {
                if (n == 2) {
                    final SerializedData serializedData = new SerializedData();
                    serializedData.writeBytes(array2, 0, 16);
                    serializedData.writeBytes(array, n2, 36);
                    final byte[] computeSHA256 = Utilities.computeSHA256(serializedData.toByteArray());
                    serializedData.cleanup();
                    final SerializedData serializedData2 = new SerializedData();
                    serializedData2.writeBytes(array, n2 + 40, 36);
                    serializedData2.writeBytes(array2, 0, 16);
                    array = Utilities.computeSHA256(serializedData2.toByteArray());
                    serializedData2.cleanup();
                    final SerializedData serializedData3 = new SerializedData();
                    serializedData3.writeBytes(computeSHA256, 0, 8);
                    serializedData3.writeBytes(array, 8, 16);
                    serializedData3.writeBytes(computeSHA256, 24, 8);
                    messageKeyData.aesKey = serializedData3.toByteArray();
                    serializedData3.cleanup();
                    final SerializedData serializedData4 = new SerializedData();
                    serializedData4.writeBytes(array, 0, 8);
                    serializedData4.writeBytes(computeSHA256, 8, 16);
                    serializedData4.writeBytes(array, 24, 8);
                    messageKeyData.aesIv = serializedData4.toByteArray();
                    serializedData4.cleanup();
                }
            }
            else {
                final SerializedData serializedData5 = new SerializedData();
                serializedData5.writeBytes(array2);
                serializedData5.writeBytes(array, n2, 32);
                final byte[] computeSHA257 = Utilities.computeSHA1(serializedData5.toByteArray());
                serializedData5.cleanup();
                final SerializedData serializedData6 = new SerializedData();
                serializedData6.writeBytes(array, n2 + 32, 16);
                serializedData6.writeBytes(array2);
                serializedData6.writeBytes(array, n2 + 48, 16);
                final byte[] computeSHA258 = Utilities.computeSHA1(serializedData6.toByteArray());
                serializedData6.cleanup();
                final SerializedData serializedData7 = new SerializedData();
                serializedData7.writeBytes(array, n2 + 64, 32);
                serializedData7.writeBytes(array2);
                final byte[] computeSHA259 = Utilities.computeSHA1(serializedData7.toByteArray());
                serializedData7.cleanup();
                final SerializedData serializedData8 = new SerializedData();
                serializedData8.writeBytes(array2);
                serializedData8.writeBytes(array, n2 + 96, 32);
                array = Utilities.computeSHA1(serializedData8.toByteArray());
                serializedData8.cleanup();
                final SerializedData serializedData9 = new SerializedData();
                serializedData9.writeBytes(computeSHA257, 0, 8);
                serializedData9.writeBytes(computeSHA258, 8, 12);
                serializedData9.writeBytes(computeSHA259, 4, 12);
                messageKeyData.aesKey = serializedData9.toByteArray();
                serializedData9.cleanup();
                final SerializedData serializedData10 = new SerializedData();
                serializedData10.writeBytes(computeSHA257, 8, 12);
                serializedData10.writeBytes(computeSHA258, 0, 8);
                serializedData10.writeBytes(computeSHA259, 16, 4);
                serializedData10.writeBytes(array, 0, 8);
                messageKeyData.aesIv = serializedData10.toByteArray();
                serializedData10.cleanup();
            }
            return messageKeyData;
        }
        messageKeyData.aesIv = null;
        messageKeyData.aesKey = null;
        return messageKeyData;
    }
}
