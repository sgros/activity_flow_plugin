package com.google.android.exoplayer2;

import java.util.UUID;

public final class C {
   public static final UUID CLEARKEY_UUID = new UUID(-2129748144642739255L, 8654423357094679310L);
   public static final UUID COMMON_PSSH_UUID = new UUID(1186680826959645954L, -5988876978535335093L);
   public static final UUID PLAYREADY_UUID = new UUID(-7348484286925749626L, -6083546864340672619L);
   public static final UUID UUID_NIL = new UUID(0L, 0L);
   public static final UUID WIDEVINE_UUID = new UUID(-1301668207276963122L, -6645017420763422227L);

   public static long msToUs(long var0) {
      long var2 = var0;
      if (var0 != -9223372036854775807L) {
         if (var0 == Long.MIN_VALUE) {
            var2 = var0;
         } else {
            var2 = var0 * 1000L;
         }
      }

      return var2;
   }

   public static long usToMs(long var0) {
      long var2 = var0;
      if (var0 != -9223372036854775807L) {
         if (var0 == Long.MIN_VALUE) {
            var2 = var0;
         } else {
            var2 = var0 / 1000L;
         }
      }

      return var2;
   }
}
