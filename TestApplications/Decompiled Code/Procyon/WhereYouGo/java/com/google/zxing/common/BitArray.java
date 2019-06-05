// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.common;

import java.util.Arrays;

public final class BitArray implements Cloneable
{
    private int[] bits;
    private int size;
    
    public BitArray() {
        this.size = 0;
        this.bits = new int[1];
    }
    
    public BitArray(final int size) {
        this.size = size;
        this.bits = makeArray(size);
    }
    
    BitArray(final int[] bits, final int size) {
        this.bits = bits;
        this.size = size;
    }
    
    private void ensureCapacity(final int n) {
        if (n > this.bits.length << 5) {
            final int[] array = makeArray(n);
            System.arraycopy(this.bits, 0, array, 0, this.bits.length);
            this.bits = array;
        }
    }
    
    private static int[] makeArray(final int n) {
        return new int[(n + 31) / 32];
    }
    
    public void appendBit(final boolean b) {
        this.ensureCapacity(this.size + 1);
        if (b) {
            final int[] bits = this.bits;
            final int n = this.size / 32;
            bits[n] |= 1 << (this.size & 0x1F);
        }
        ++this.size;
    }
    
    public void appendBitArray(final BitArray bitArray) {
        final int size = bitArray.size;
        this.ensureCapacity(this.size + size);
        for (int i = 0; i < size; ++i) {
            this.appendBit(bitArray.get(i));
        }
    }
    
    public void appendBits(final int n, int i) {
        if (i < 0 || i > 32) {
            throw new IllegalArgumentException("Num bits must be between 0 and 32");
        }
        this.ensureCapacity(this.size + i);
        while (i > 0) {
            this.appendBit((n >> i - 1 & 0x1) == 0x1);
            --i;
        }
    }
    
    public void clear() {
        for (int length = this.bits.length, i = 0; i < length; ++i) {
            this.bits[i] = 0;
        }
    }
    
    public BitArray clone() {
        return new BitArray(this.bits.clone(), this.size);
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        boolean b2;
        if (!(o instanceof BitArray)) {
            b2 = b;
        }
        else {
            final BitArray bitArray = (BitArray)o;
            b2 = b;
            if (this.size == bitArray.size) {
                b2 = b;
                if (Arrays.equals(this.bits, bitArray.bits)) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    public void flip(final int n) {
        final int[] bits = this.bits;
        final int n2 = n / 32;
        bits[n2] ^= 1 << (n & 0x1F);
    }
    
    public boolean get(final int n) {
        boolean b = true;
        if ((this.bits[n / 32] & 1 << (n & 0x1F)) == 0x0) {
            b = false;
        }
        return b;
    }
    
    public int[] getBitArray() {
        return this.bits;
    }
    
    public int getNextSet(int i) {
        if (i >= this.size) {
            i = this.size;
        }
        else {
            int n;
            for (n = i / 32, i = (this.bits[n] & ~((1 << (i & 0x1F)) - 1)); i == 0; i = this.bits[n]) {
                if (++n == this.bits.length) {
                    i = this.size;
                    return i;
                }
            }
            if ((i = (n << 5) + Integer.numberOfTrailingZeros(i)) > this.size) {
                i = this.size;
            }
        }
        return i;
    }
    
    public int getNextUnset(int i) {
        if (i >= this.size) {
            i = this.size;
        }
        else {
            int n;
            for (n = i / 32, i = (~this.bits[n] & ~((1 << (i & 0x1F)) - 1)); i == 0; i = ~this.bits[n]) {
                if (++n == this.bits.length) {
                    i = this.size;
                    return i;
                }
            }
            if ((i = (n << 5) + Integer.numberOfTrailingZeros(i)) > this.size) {
                i = this.size;
            }
        }
        return i;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public int getSizeInBytes() {
        return (this.size + 7) / 8;
    }
    
    @Override
    public int hashCode() {
        return this.size * 31 + Arrays.hashCode(this.bits);
    }
    
    public boolean isRange(final int n, int n2, final boolean b) {
        final boolean b2 = true;
        if (n2 < n || n < 0 || n2 > this.size) {
            throw new IllegalArgumentException();
        }
        boolean b3;
        if (n2 == n) {
            b3 = b2;
        }
        else {
            final int n3 = n2 - 1;
            final int n4 = n / 32;
            final int n5 = n3 / 32;
            int n6 = n4;
            while (true) {
                b3 = b2;
                if (n6 > n5) {
                    return b3;
                }
                if (n6 > n4) {
                    n2 = 0;
                }
                else {
                    n2 = (n & 0x1F);
                }
                int n7;
                if (n6 < n5) {
                    n7 = 31;
                }
                else {
                    n7 = (n3 & 0x1F);
                }
                final int n8 = (2 << n7) - (1 << n2);
                final int n9 = this.bits[n6];
                if (b) {
                    n2 = n8;
                }
                else {
                    n2 = 0;
                }
                if ((n9 & n8) != n2) {
                    break;
                }
                ++n6;
            }
            b3 = false;
        }
        return b3;
    }
    
    public void reverse() {
        final int[] bits = new int[this.bits.length];
        final int n = (this.size - 1) / 32;
        final int n2 = n + 1;
        for (int i = 0; i < n2; ++i) {
            final long n3 = this.bits[i];
            final long n4 = (n3 >> 1 & 0x55555555L) | (0x55555555L & n3) << 1;
            final long n5 = (n4 >> 2 & 0x33333333L) | (0x33333333L & n4) << 2;
            final long n6 = (n5 >> 4 & 0xF0F0F0FL) | (0xF0F0F0FL & n5) << 4;
            final long n7 = (n6 >> 8 & 0xFF00FFL) | (0xFF00FFL & n6) << 8;
            bits[n - i] = (int)((n7 >> 16 & 0xFFFFL) | (0xFFFFL & n7) << 16);
        }
        if (this.size != n2 << 5) {
            final int n8 = (n2 << 5) - this.size;
            int n9 = bits[0] >>> n8;
            for (int j = 1; j < n2; ++j) {
                final int n10 = bits[j];
                bits[j - 1] = (n9 | n10 << 32 - n8);
                n9 = n10 >>> n8;
            }
            bits[n2 - 1] = n9;
        }
        this.bits = bits;
    }
    
    public void set(final int n) {
        final int[] bits = this.bits;
        final int n2 = n / 32;
        bits[n2] |= 1 << (n & 0x1F);
    }
    
    public void setBulk(final int n, final int n2) {
        this.bits[n / 32] = n2;
    }
    
    public void setRange(final int n, int i) {
        if (i < n || n < 0 || i > this.size) {
            throw new IllegalArgumentException();
        }
        if (i != n) {
            final int n2 = i - 1;
            final int n3 = n / 32;
            int n4;
            int n5;
            int n6;
            int[] bits;
            for (n4 = n2 / 32, i = n3; i <= n4; ++i) {
                if (i > n3) {
                    n5 = 0;
                }
                else {
                    n5 = (n & 0x1F);
                }
                if (i < n4) {
                    n6 = 31;
                }
                else {
                    n6 = (n2 & 0x1F);
                }
                bits = this.bits;
                bits[i] |= (2 << n6) - (1 << n5);
            }
        }
    }
    
    public void toBytes(int i, final byte[] array, final int n, final int n2) {
        final int n3 = 0;
        int n4 = i;
        int n5;
        int j;
        int n6;
        for (i = n3; i < n2; ++i) {
            n5 = 0;
            for (j = 0; j < 8; ++j, n5 = n6) {
                n6 = n5;
                if (this.get(n4)) {
                    n6 = (n5 | 1 << 7 - j);
                }
                ++n4;
            }
            array[n + i] = (byte)n5;
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(this.size);
        for (int i = 0; i < this.size; ++i) {
            if ((i & 0x7) == 0x0) {
                sb.append(' ');
            }
            char c;
            if (this.get(i)) {
                c = 'X';
            }
            else {
                c = '.';
            }
            sb.append(c);
        }
        return sb.toString();
    }
    
    public void xor(final BitArray bitArray) {
        if (this.size != bitArray.size) {
            throw new IllegalArgumentException("Sizes don't match");
        }
        for (int i = 0; i < this.bits.length; ++i) {
            final int[] bits = this.bits;
            bits[i] ^= bitArray.bits[i];
        }
    }
}
