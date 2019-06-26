// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.extractor.TrackOutput;

public final class TrackEncryptionBox
{
    public final TrackOutput.CryptoData cryptoData;
    public final byte[] defaultInitializationVector;
    public final boolean isEncrypted;
    public final int perSampleIvSize;
    public final String schemeType;
    
    public TrackEncryptionBox(final boolean isEncrypted, final String schemeType, final int perSampleIvSize, final byte[] array, final int n, final int n2, final byte[] defaultInitializationVector) {
        boolean b = true;
        final boolean b2 = perSampleIvSize == 0;
        if (defaultInitializationVector != null) {
            b = false;
        }
        Assertions.checkArgument(b ^ b2);
        this.isEncrypted = isEncrypted;
        this.schemeType = schemeType;
        this.perSampleIvSize = perSampleIvSize;
        this.defaultInitializationVector = defaultInitializationVector;
        this.cryptoData = new TrackOutput.CryptoData(schemeToCryptoMode(schemeType), array, n, n2);
    }
    
    private static int schemeToCryptoMode(final String str) {
        if (str == null) {
            return 1;
        }
        int n = -1;
        switch (str.hashCode()) {
            case 3049895: {
                if (str.equals("cens")) {
                    n = 1;
                    break;
                }
                break;
            }
            case 3049879: {
                if (str.equals("cenc")) {
                    n = 0;
                    break;
                }
                break;
            }
            case 3046671: {
                if (str.equals("cbcs")) {
                    n = 3;
                    break;
                }
                break;
            }
            case 3046605: {
                if (str.equals("cbc1")) {
                    n = 2;
                    break;
                }
                break;
            }
        }
        if (n == 0 || n == 1) {
            return 1;
        }
        if (n != 2 && n != 3) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unsupported protection scheme type '");
            sb.append(str);
            sb.append("'. Assuming AES-CTR crypto mode.");
            Log.w("TrackEncryptionBox", sb.toString());
            return 1;
        }
        return 2;
    }
}
