// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.decoder;

import android.media.MediaCodec$CryptoInfo$Pattern;
import android.annotation.TargetApi;
import com.google.android.exoplayer2.util.Util;
import android.media.MediaCodec$CryptoInfo;

public final class CryptoInfo
{
    public int clearBlocks;
    public int encryptedBlocks;
    private final MediaCodec$CryptoInfo frameworkCryptoInfo;
    public byte[] iv;
    public byte[] key;
    public int mode;
    public int[] numBytesOfClearData;
    public int[] numBytesOfEncryptedData;
    public int numSubSamples;
    private final PatternHolderV24 patternHolder;
    
    public CryptoInfo() {
        MediaCodec$CryptoInfo frameworkCryptoInfoV16;
        if (Util.SDK_INT >= 16) {
            frameworkCryptoInfoV16 = this.newFrameworkCryptoInfoV16();
        }
        else {
            frameworkCryptoInfoV16 = null;
        }
        this.frameworkCryptoInfo = frameworkCryptoInfoV16;
        PatternHolderV24 patternHolder;
        if (Util.SDK_INT >= 24) {
            patternHolder = new PatternHolderV24(this.frameworkCryptoInfo);
        }
        else {
            patternHolder = null;
        }
        this.patternHolder = patternHolder;
    }
    
    @TargetApi(16)
    private MediaCodec$CryptoInfo newFrameworkCryptoInfoV16() {
        return new MediaCodec$CryptoInfo();
    }
    
    @TargetApi(16)
    private void updateFrameworkCryptoInfoV16() {
        final MediaCodec$CryptoInfo frameworkCryptoInfo = this.frameworkCryptoInfo;
        frameworkCryptoInfo.numSubSamples = this.numSubSamples;
        frameworkCryptoInfo.numBytesOfClearData = this.numBytesOfClearData;
        frameworkCryptoInfo.numBytesOfEncryptedData = this.numBytesOfEncryptedData;
        frameworkCryptoInfo.key = this.key;
        frameworkCryptoInfo.iv = this.iv;
        frameworkCryptoInfo.mode = this.mode;
        if (Util.SDK_INT >= 24) {
            this.patternHolder.set(this.encryptedBlocks, this.clearBlocks);
        }
    }
    
    @TargetApi(16)
    public MediaCodec$CryptoInfo getFrameworkCryptoInfoV16() {
        return this.frameworkCryptoInfo;
    }
    
    public void set(final int numSubSamples, final int[] numBytesOfClearData, final int[] numBytesOfEncryptedData, final byte[] key, final byte[] iv, final int mode, final int encryptedBlocks, final int clearBlocks) {
        this.numSubSamples = numSubSamples;
        this.numBytesOfClearData = numBytesOfClearData;
        this.numBytesOfEncryptedData = numBytesOfEncryptedData;
        this.key = key;
        this.iv = iv;
        this.mode = mode;
        this.encryptedBlocks = encryptedBlocks;
        this.clearBlocks = clearBlocks;
        if (Util.SDK_INT >= 16) {
            this.updateFrameworkCryptoInfoV16();
        }
    }
    
    @TargetApi(24)
    private static final class PatternHolderV24
    {
        private final MediaCodec$CryptoInfo frameworkCryptoInfo;
        private final MediaCodec$CryptoInfo$Pattern pattern;
        
        private PatternHolderV24(final MediaCodec$CryptoInfo frameworkCryptoInfo) {
            this.frameworkCryptoInfo = frameworkCryptoInfo;
            this.pattern = new MediaCodec$CryptoInfo$Pattern(0, 0);
        }
        
        private void set(final int n, final int n2) {
            this.pattern.set(n, n2);
            this.frameworkCryptoInfo.setPattern(this.pattern);
        }
    }
}
