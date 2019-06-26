package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.nio.ByteBuffer;
import java.util.UUID;

public final class PsshAtomUtil {
   public static byte[] buildPsshAtom(UUID var0, byte[] var1) {
      return buildPsshAtom(var0, (UUID[])null, var1);
   }

   public static byte[] buildPsshAtom(UUID var0, UUID[] var1, byte[] var2) {
      byte var3 = 0;
      int var4;
      if (var2 != null) {
         var4 = var2.length;
      } else {
         var4 = 0;
      }

      int var5 = var4 + 32;
      var4 = var5;
      if (var1 != null) {
         var4 = var5 + var1.length * 16 + 4;
      }

      ByteBuffer var6 = ByteBuffer.allocate(var4);
      var6.putInt(var4);
      var6.putInt(Atom.TYPE_pssh);
      if (var1 != null) {
         var4 = 16777216;
      } else {
         var4 = 0;
      }

      var6.putInt(var4);
      var6.putLong(var0.getMostSignificantBits());
      var6.putLong(var0.getLeastSignificantBits());
      if (var1 != null) {
         var6.putInt(var1.length);
         var5 = var1.length;

         for(var4 = var3; var4 < var5; ++var4) {
            var0 = var1[var4];
            var6.putLong(var0.getMostSignificantBits());
            var6.putLong(var0.getLeastSignificantBits());
         }
      }

      if (var2 != null && var2.length != 0) {
         var6.putInt(var2.length);
         var6.put(var2);
      }

      return var6.array();
   }

   private static PsshAtomUtil.PsshAtom parsePsshAtom(byte[] var0) {
      ParsableByteArray var1 = new ParsableByteArray(var0);
      if (var1.limit() < 32) {
         return null;
      } else {
         var1.setPosition(0);
         if (var1.readInt() != var1.bytesLeft() + 4) {
            return null;
         } else if (var1.readInt() != Atom.TYPE_pssh) {
            return null;
         } else {
            int var2 = Atom.parseFullAtomVersion(var1.readInt());
            if (var2 > 1) {
               StringBuilder var5 = new StringBuilder();
               var5.append("Unsupported pssh version: ");
               var5.append(var2);
               Log.w("PsshAtomUtil", var5.toString());
               return null;
            } else {
               UUID var3 = new UUID(var1.readLong(), var1.readLong());
               if (var2 == 1) {
                  var1.skipBytes(var1.readUnsignedIntToInt() * 16);
               }

               int var4 = var1.readUnsignedIntToInt();
               if (var4 != var1.bytesLeft()) {
                  return null;
               } else {
                  var0 = new byte[var4];
                  var1.readBytes(var0, 0, var4);
                  return new PsshAtomUtil.PsshAtom(var3, var2, var0);
               }
            }
         }
      }
   }

   public static UUID parseUuid(byte[] var0) {
      PsshAtomUtil.PsshAtom var1 = parsePsshAtom(var0);
      return var1 == null ? null : var1.uuid;
   }

   private static class PsshAtom {
      private final byte[] schemeData;
      private final UUID uuid;
      private final int version;

      public PsshAtom(UUID var1, int var2, byte[] var3) {
         this.uuid = var1;
         this.version = var2;
         this.schemeData = var3;
      }
   }
}
