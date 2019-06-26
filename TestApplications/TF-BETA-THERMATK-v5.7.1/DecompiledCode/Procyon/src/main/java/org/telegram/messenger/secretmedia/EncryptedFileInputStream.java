// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.secretmedia;

import java.io.IOException;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.SecureDocumentKey;
import java.io.RandomAccessFile;
import java.io.File;
import java.io.FileInputStream;

public class EncryptedFileInputStream extends FileInputStream
{
    private static final int MODE_CBC = 1;
    private static final int MODE_CTR = 0;
    private int currentMode;
    private int fileOffset;
    private byte[] iv;
    private byte[] key;
    
    public EncryptedFileInputStream(final File file, final File file2) throws Exception {
        super(file);
        this.key = new byte[32];
        this.iv = new byte[16];
        this.currentMode = 0;
        final RandomAccessFile randomAccessFile = new RandomAccessFile(file2, "r");
        randomAccessFile.read(this.key, 0, 32);
        randomAccessFile.read(this.iv, 0, 16);
        randomAccessFile.close();
    }
    
    public EncryptedFileInputStream(final File file, final SecureDocumentKey secureDocumentKey) throws Exception {
        super(file);
        this.key = new byte[32];
        this.iv = new byte[16];
        this.currentMode = 1;
        final byte[] file_key = secureDocumentKey.file_key;
        final byte[] key = this.key;
        System.arraycopy(file_key, 0, key, 0, key.length);
        final byte[] file_iv = secureDocumentKey.file_iv;
        final byte[] iv = this.iv;
        System.arraycopy(file_iv, 0, iv, 0, iv.length);
    }
    
    public static void decryptBytesWithKeyFile(final byte[] array, final int n, final int n2, final File file) throws Exception {
        final byte[] b = new byte[32];
        final byte[] b2 = new byte[16];
        final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        randomAccessFile.read(b, 0, 32);
        randomAccessFile.read(b2, 0, 16);
        randomAccessFile.close();
        Utilities.aesCtrDecryptionByteArray(array, b, b2, n, n2, 0);
    }
    
    public static void decryptBytesWithKeyFile(final byte[] array, final int n, final int n2, final SecureDocumentKey secureDocumentKey) {
        Utilities.aesCbcEncryptionByteArraySafe(array, secureDocumentKey.file_key, secureDocumentKey.file_iv, n, n2, 0, 0);
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        if (this.currentMode == 1 && this.fileOffset == 0) {
            final byte[] b2 = new byte[32];
            super.read(b2, 0, 32);
            Utilities.aesCbcEncryptionByteArraySafe(b, this.key, this.iv, off, len, this.fileOffset, 0);
            this.fileOffset += 32;
            this.skip((b2[0] & 0xFF) - 32);
        }
        final int read = super.read(b, off, len);
        final int currentMode = this.currentMode;
        if (currentMode == 1) {
            Utilities.aesCbcEncryptionByteArraySafe(b, this.key, this.iv, off, len, this.fileOffset, 0);
        }
        else if (currentMode == 0) {
            Utilities.aesCtrDecryptionByteArray(b, this.key, this.iv, off, len, this.fileOffset);
        }
        this.fileOffset += len;
        return read;
    }
    
    @Override
    public long skip(final long n) throws IOException {
        this.fileOffset += (int)n;
        return super.skip(n);
    }
}
