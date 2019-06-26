// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.hls;

import com.google.android.exoplayer2.util.Assertions;
import java.security.InvalidKeyException;
import java.security.InvalidAlgorithmParameterException;
import java.io.InputStream;
import com.google.android.exoplayer2.upstream.DataSourceInputStream;
import java.security.spec.AlgorithmParameterSpec;
import java.security.Key;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import com.google.android.exoplayer2.upstream.DataSpec;
import android.net.Uri;
import java.util.List;
import java.util.Map;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.Cipher;
import java.io.IOException;
import com.google.android.exoplayer2.upstream.TransferListener;
import javax.crypto.CipherInputStream;
import com.google.android.exoplayer2.upstream.DataSource;

class Aes128DataSource implements DataSource
{
    private CipherInputStream cipherInputStream;
    private final byte[] encryptionIv;
    private final byte[] encryptionKey;
    private final DataSource upstream;
    
    public Aes128DataSource(final DataSource upstream, final byte[] encryptionKey, final byte[] encryptionIv) {
        this.upstream = upstream;
        this.encryptionKey = encryptionKey;
        this.encryptionIv = encryptionIv;
    }
    
    @Override
    public final void addTransferListener(final TransferListener transferListener) {
        this.upstream.addTransferListener(transferListener);
    }
    
    @Override
    public void close() throws IOException {
        if (this.cipherInputStream != null) {
            this.cipherInputStream = null;
            this.upstream.close();
        }
    }
    
    protected Cipher getCipherInstance() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance("AES/CBC/PKCS7Padding");
    }
    
    @Override
    public final Map<String, List<String>> getResponseHeaders() {
        return this.upstream.getResponseHeaders();
    }
    
    @Override
    public final Uri getUri() {
        return this.upstream.getUri();
    }
    
    @Override
    public final long open(DataSpec cause) throws IOException {
        try {
            final Cipher cipherInstance = this.getCipherInstance();
            final SecretKeySpec key = new SecretKeySpec(this.encryptionKey, "AES");
            final IvParameterSpec params = new IvParameterSpec(this.encryptionIv);
            try {
                cipherInstance.init(2, key, params);
                cause = (InvalidAlgorithmParameterException)new DataSourceInputStream(this.upstream, (DataSpec)cause);
                this.cipherInputStream = new CipherInputStream((InputStream)cause, cipherInstance);
                ((DataSourceInputStream)cause).open();
                return -1L;
            }
            catch (InvalidAlgorithmParameterException cause) {}
            catch (InvalidKeyException ex) {}
            throw new RuntimeException(cause);
        }
        catch (NoSuchPaddingException cause) {}
        catch (NoSuchAlgorithmException ex2) {}
        throw new RuntimeException(cause);
    }
    
    @Override
    public final int read(final byte[] b, int read, int len) throws IOException {
        Assertions.checkNotNull(this.cipherInputStream);
        len = (read = this.cipherInputStream.read(b, read, len));
        if (len < 0) {
            read = -1;
        }
        return read;
    }
}
