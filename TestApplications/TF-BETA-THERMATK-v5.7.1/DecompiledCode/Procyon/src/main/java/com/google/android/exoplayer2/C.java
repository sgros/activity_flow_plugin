// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import java.util.UUID;

public final class C
{
    public static final UUID CLEARKEY_UUID;
    public static final UUID COMMON_PSSH_UUID;
    public static final UUID PLAYREADY_UUID;
    public static final UUID UUID_NIL;
    public static final UUID WIDEVINE_UUID;
    
    static {
        UUID_NIL = new UUID(0L, 0L);
        COMMON_PSSH_UUID = new UUID(1186680826959645954L, -5988876978535335093L);
        CLEARKEY_UUID = new UUID(-2129748144642739255L, 8654423357094679310L);
        WIDEVINE_UUID = new UUID(-1301668207276963122L, -6645017420763422227L);
        PLAYREADY_UUID = new UUID(-7348484286925749626L, -6083546864340672619L);
    }
    
    public static long msToUs(final long n) {
        long n2 = n;
        if (n != -9223372036854775807L) {
            if (n == Long.MIN_VALUE) {
                n2 = n;
            }
            else {
                n2 = n * 1000L;
            }
        }
        return n2;
    }
    
    public static long usToMs(final long n) {
        long n2 = n;
        if (n != -9223372036854775807L) {
            if (n == Long.MIN_VALUE) {
                n2 = n;
            }
            else {
                n2 = n / 1000L;
            }
        }
        return n2;
    }
}
