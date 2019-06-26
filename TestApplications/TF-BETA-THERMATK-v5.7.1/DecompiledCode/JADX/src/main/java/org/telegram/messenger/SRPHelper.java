package org.telegram.messenger;

import java.math.BigInteger;
import org.telegram.tgnet.TLRPC.C1158xb6caa888;
import org.telegram.tgnet.TLRPC.TL_inputCheckPasswordSRP;

public class SRPHelper {
    public static byte[] getBigIntegerBytes(BigInteger bigInteger) {
        byte[] toByteArray = bigInteger.toByteArray();
        byte[] bArr;
        if (toByteArray.length > 256) {
            bArr = new byte[256];
            System.arraycopy(toByteArray, 1, bArr, 0, 256);
            return bArr;
        } else if (toByteArray.length >= 256) {
            return toByteArray;
        } else {
            bArr = new byte[256];
            System.arraycopy(toByteArray, 0, bArr, 256 - toByteArray.length, toByteArray.length);
            for (int i = 0; i < 256 - toByteArray.length; i++) {
                bArr[i] = (byte) 0;
            }
            return bArr;
        }
    }

    public static byte[] getX(byte[] bArr, C1158xb6caa888 c1158xb6caa888) {
        r1 = new byte[3][];
        byte[] bArr2 = c1158xb6caa888.salt1;
        r1[0] = bArr2;
        r1[1] = bArr;
        r1[2] = bArr2;
        byte[] computeSHA256 = Utilities.computeSHA256(r1);
        r2 = new byte[3][];
        byte[] bArr3 = c1158xb6caa888.salt2;
        r2[0] = bArr3;
        r2[1] = computeSHA256;
        r2[2] = bArr3;
        computeSHA256 = Utilities.computePBKDF2(Utilities.computeSHA256(r2), c1158xb6caa888.salt1);
        r0 = new byte[3][];
        byte[] bArr4 = c1158xb6caa888.salt2;
        r0[0] = bArr4;
        r0[1] = computeSHA256;
        r0[2] = bArr4;
        return Utilities.computeSHA256(r0);
    }

    public static BigInteger getV(byte[] bArr, C1158xb6caa888 c1158xb6caa888) {
        BigInteger valueOf = BigInteger.valueOf((long) c1158xb6caa888.f559g);
        getBigIntegerBytes(valueOf);
        return valueOf.modPow(new BigInteger(1, getX(bArr, c1158xb6caa888)), new BigInteger(1, c1158xb6caa888.f560p));
    }

    public static byte[] getVBytes(byte[] bArr, C1158xb6caa888 c1158xb6caa888) {
        if (Utilities.isGoodPrime(c1158xb6caa888.f560p, c1158xb6caa888.f559g)) {
            return getBigIntegerBytes(getV(bArr, c1158xb6caa888));
        }
        return null;
    }

    public static TL_inputCheckPasswordSRP startCheck(byte[] bArr, long j, byte[] bArr2, C1158xb6caa888 c1158xb6caa888) {
        byte[] bArr3 = bArr;
        byte[] bArr4 = bArr2;
        C1158xb6caa888 c1158xb6caa8882 = c1158xb6caa888;
        if (!(bArr3 == null || bArr4 == null || bArr4.length == 0 || !Utilities.isGoodPrime(c1158xb6caa8882.f560p, c1158xb6caa8882.f559g))) {
            BigInteger valueOf = BigInteger.valueOf((long) c1158xb6caa8882.f559g);
            byte[] bigIntegerBytes = getBigIntegerBytes(valueOf);
            BigInteger bigInteger = new BigInteger(1, c1158xb6caa8882.f560p);
            BigInteger bigInteger2 = new BigInteger(1, Utilities.computeSHA256(c1158xb6caa8882.f560p, bigIntegerBytes));
            BigInteger bigInteger3 = new BigInteger(1, bArr3);
            bArr3 = new byte[256];
            Utilities.random.nextBytes(bArr3);
            BigInteger bigInteger4 = new BigInteger(1, bArr3);
            bArr3 = getBigIntegerBytes(valueOf.modPow(bigInteger4, bigInteger));
            BigInteger bigInteger5 = new BigInteger(1, bArr4);
            if (bigInteger5.compareTo(BigInteger.ZERO) > 0 && bigInteger5.compareTo(bigInteger) < 0) {
                bArr4 = getBigIntegerBytes(bigInteger5);
                BigInteger bigInteger6 = new BigInteger(1, Utilities.computeSHA256(bArr3, bArr4));
                if (bigInteger6.compareTo(BigInteger.ZERO) == 0) {
                    return null;
                }
                valueOf = bigInteger5.subtract(bigInteger2.multiply(valueOf.modPow(bigInteger3, bigInteger)).mod(bigInteger));
                if (valueOf.compareTo(BigInteger.ZERO) < 0) {
                    valueOf = valueOf.add(bigInteger);
                }
                if (!Utilities.isGoodGaAndGb(valueOf, bigInteger)) {
                    return null;
                }
                byte[] computeSHA256 = Utilities.computeSHA256(getBigIntegerBytes(valueOf.modPow(bigInteger4.add(bigInteger6.multiply(bigInteger3)), bigInteger)));
                byte[] computeSHA2562 = Utilities.computeSHA256(c1158xb6caa8882.f560p);
                bigIntegerBytes = Utilities.computeSHA256(bigIntegerBytes);
                for (int i = 0; i < computeSHA2562.length; i++) {
                    computeSHA2562[i] = (byte) (bigIntegerBytes[i] ^ computeSHA2562[i]);
                }
                TL_inputCheckPasswordSRP tL_inputCheckPasswordSRP = new TL_inputCheckPasswordSRP();
                tL_inputCheckPasswordSRP.f544M1 = Utilities.computeSHA256(computeSHA2562, Utilities.computeSHA256(c1158xb6caa8882.salt1), Utilities.computeSHA256(c1158xb6caa8882.salt2), bArr3, bArr4, computeSHA256);
                tL_inputCheckPasswordSRP.f543A = bArr3;
                tL_inputCheckPasswordSRP.srp_id = j;
                return tL_inputCheckPasswordSRP;
            }
        }
        return null;
    }
}
