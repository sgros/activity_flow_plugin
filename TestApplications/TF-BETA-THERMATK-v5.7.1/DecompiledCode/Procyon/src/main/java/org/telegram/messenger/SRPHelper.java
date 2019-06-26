// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;
import java.math.BigInteger;

public class SRPHelper
{
    public static byte[] getBigIntegerBytes(final BigInteger bigInteger) {
        final byte[] byteArray = bigInteger.toByteArray();
        if (byteArray.length > 256) {
            final byte[] array = new byte[256];
            System.arraycopy(byteArray, 1, array, 0, 256);
            return array;
        }
        if (byteArray.length < 256) {
            final byte[] array2 = new byte[256];
            System.arraycopy(byteArray, 0, array2, 256 - byteArray.length, byteArray.length);
            for (int i = 0; i < 256 - byteArray.length; ++i) {
                array2[i] = 0;
            }
            return array2;
        }
        return byteArray;
    }
    
    public static BigInteger getV(final byte[] array, final TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
        final BigInteger value = BigInteger.valueOf(tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.g);
        getBigIntegerBytes(value);
        return value.modPow(new BigInteger(1, getX(array, tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)), new BigInteger(1, tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.p));
    }
    
    public static byte[] getVBytes(final byte[] array, final TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
        if (!Utilities.isGoodPrime(tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.p, tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.g)) {
            return null;
        }
        return getBigIntegerBytes(getV(array, tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow));
    }
    
    public static byte[] getX(byte[] array, final TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
        final byte[] salt1 = tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.salt1;
        final byte[] computeSHA256 = Utilities.computeSHA256(new byte[][] { salt1, array, salt1 });
        array = tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.salt2;
        array = Utilities.computePBKDF2(Utilities.computeSHA256(new byte[][] { array, computeSHA256, array }), tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.salt1);
        final byte[] salt2 = tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.salt2;
        return Utilities.computeSHA256(new byte[][] { salt2, array, salt2 });
    }
    
    public static TLRPC.TL_inputCheckPasswordSRP startCheck(byte[] computeSHA256, final long srp_id, byte[] computeSHA257, final TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
        if (computeSHA256 != null && computeSHA257 != null && computeSHA257.length != 0) {
            if (Utilities.isGoodPrime(tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.p, tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.g)) {
                final BigInteger value = BigInteger.valueOf(tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.g);
                final byte[] bigIntegerBytes = getBigIntegerBytes(value);
                final BigInteger bigInteger = new BigInteger(1, tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.p);
                final BigInteger bigInteger2 = new BigInteger(1, Utilities.computeSHA256(new byte[][] { tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.p, bigIntegerBytes }));
                final BigInteger bigInteger3 = new BigInteger(1, computeSHA256);
                computeSHA256 = new byte[256];
                Utilities.random.nextBytes(computeSHA256);
                final BigInteger exponent = new BigInteger(1, computeSHA256);
                final byte[] bigIntegerBytes2 = getBigIntegerBytes(value.modPow(exponent, bigInteger));
                final BigInteger bigInteger4 = new BigInteger(1, computeSHA257);
                if (bigInteger4.compareTo(BigInteger.ZERO) > 0) {
                    if (bigInteger4.compareTo(bigInteger) < 0) {
                        final byte[] bigIntegerBytes3 = getBigIntegerBytes(bigInteger4);
                        final BigInteger bigInteger5 = new BigInteger(1, Utilities.computeSHA256(new byte[][] { bigIntegerBytes2, bigIntegerBytes3 }));
                        if (bigInteger5.compareTo(BigInteger.ZERO) == 0) {
                            return null;
                        }
                        BigInteger bigInteger7;
                        final BigInteger bigInteger6 = bigInteger7 = bigInteger4.subtract(bigInteger2.multiply(value.modPow(bigInteger3, bigInteger)).mod(bigInteger));
                        if (bigInteger6.compareTo(BigInteger.ZERO) < 0) {
                            bigInteger7 = bigInteger6.add(bigInteger);
                        }
                        if (!Utilities.isGoodGaAndGb(bigInteger7, bigInteger)) {
                            return null;
                        }
                        computeSHA257 = Utilities.computeSHA256(getBigIntegerBytes(bigInteger7.modPow(exponent.add(bigInteger5.multiply(bigInteger3)), bigInteger)));
                        computeSHA256 = Utilities.computeSHA256(tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.p);
                        final byte[] computeSHA258 = Utilities.computeSHA256(bigIntegerBytes);
                        for (int i = 0; i < computeSHA256.length; ++i) {
                            computeSHA256[i] ^= computeSHA258[i];
                        }
                        final TLRPC.TL_inputCheckPasswordSRP tl_inputCheckPasswordSRP = new TLRPC.TL_inputCheckPasswordSRP();
                        tl_inputCheckPasswordSRP.M1 = Utilities.computeSHA256(new byte[][] { computeSHA256, Utilities.computeSHA256(tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.salt1), Utilities.computeSHA256(tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.salt2), bigIntegerBytes2, bigIntegerBytes3, computeSHA257 });
                        tl_inputCheckPasswordSRP.A = bigIntegerBytes2;
                        tl_inputCheckPasswordSRP.srp_id = srp_id;
                        return tl_inputCheckPasswordSRP;
                    }
                }
            }
        }
        return null;
    }
}
