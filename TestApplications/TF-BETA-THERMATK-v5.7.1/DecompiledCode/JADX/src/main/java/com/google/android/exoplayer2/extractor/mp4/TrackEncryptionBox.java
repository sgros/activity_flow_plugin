package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.extractor.TrackOutput.CryptoData;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;

public final class TrackEncryptionBox {
    public final CryptoData cryptoData;
    public final byte[] defaultInitializationVector;
    public final boolean isEncrypted;
    public final int perSampleIvSize;
    public final String schemeType;

    public TrackEncryptionBox(boolean z, String str, int i, byte[] bArr, int i2, int i3, byte[] bArr2) {
        int i4 = 1;
        int i5 = i == 0 ? 1 : 0;
        if (bArr2 != null) {
            i4 = 0;
        }
        Assertions.checkArgument(i4 ^ i5);
        this.isEncrypted = z;
        this.schemeType = str;
        this.perSampleIvSize = i;
        this.defaultInitializationVector = bArr2;
        this.cryptoData = new CryptoData(schemeToCryptoMode(str), bArr, i2, i3);
    }

    private static int schemeToCryptoMode(String str) {
        if (str == null) {
            return 1;
        }
        int i = -1;
        switch (str.hashCode()) {
            case 3046605:
                if (str.equals("cbc1")) {
                    i = 2;
                    break;
                }
                break;
            case 3046671:
                if (str.equals("cbcs")) {
                    i = 3;
                    break;
                }
                break;
            case 3049879:
                if (str.equals("cenc")) {
                    i = 0;
                    break;
                }
                break;
            case 3049895:
                if (str.equals("cens")) {
                    i = 1;
                    break;
                }
                break;
        }
        if (i == 0 || i == 1) {
            return 1;
        }
        if (i == 2 || i == 3) {
            return 2;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported protection scheme type '");
        stringBuilder.append(str);
        stringBuilder.append("'. Assuming AES-CTR crypto mode.");
        Log.m18w("TrackEncryptionBox", stringBuilder.toString());
        return 1;
    }
}
