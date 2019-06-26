package org.telegram.messenger.voip;

public class EncryptionKeyEmojifier {
   private static final String[] emojis = new String[]{"\ud83d\ude09", "\ud83d\ude0d", "\ud83d\ude1b", "\ud83d\ude2d", "\ud83d\ude31", "\ud83d\ude21", "\ud83d\ude0e", "\ud83d\ude34", "\ud83d\ude35", "\ud83d\ude08", "\ud83d\ude2c", "\ud83d\ude07", "\ud83d\ude0f", "\ud83d\udc6e", "\ud83d\udc77", "\ud83d\udc82", "\ud83d\udc76", "\ud83d\udc68", "\ud83d\udc69", "\ud83d\udc74", "\ud83d\udc75", "\ud83d\ude3b", "\ud83d\ude3d", "\ud83d\ude40", "\ud83d\udc7a", "\ud83d\ude48", "\ud83d\ude49", "\ud83d\ude4a", "\ud83d\udc80", "\ud83d\udc7d", "\ud83d\udca9", "\ud83d\udd25", "\ud83d\udca5", "\ud83d\udca4", "\ud83d\udc42", "\ud83d\udc40", "\ud83d\udc43", "\ud83d\udc45", "\ud83d\udc44", "\ud83d\udc4d", "\ud83d\udc4e", "\ud83d\udc4c", "\ud83d\udc4a", "✌", "✋", "\ud83d\udc50", "\ud83d\udc46", "\ud83d\udc47", "\ud83d\udc49", "\ud83d\udc48", "\ud83d\ude4f", "\ud83d\udc4f", "\ud83d\udcaa", "\ud83d\udeb6", "\ud83c\udfc3", "\ud83d\udc83", "\ud83d\udc6b", "\ud83d\udc6a", "\ud83d\udc6c", "\ud83d\udc6d", "\ud83d\udc85", "\ud83c\udfa9", "\ud83d\udc51", "\ud83d\udc52", "\ud83d\udc5f", "\ud83d\udc5e", "\ud83d\udc60", "\ud83d\udc55", "\ud83d\udc57", "\ud83d\udc56", "\ud83d\udc59", "\ud83d\udc5c", "\ud83d\udc53", "\ud83c\udf80", "\ud83d\udc84", "\ud83d\udc9b", "\ud83d\udc99", "\ud83d\udc9c", "\ud83d\udc9a", "\ud83d\udc8d", "\ud83d\udc8e", "\ud83d\udc36", "\ud83d\udc3a", "\ud83d\udc31", "\ud83d\udc2d", "\ud83d\udc39", "\ud83d\udc30", "\ud83d\udc38", "\ud83d\udc2f", "\ud83d\udc28", "\ud83d\udc3b", "\ud83d\udc37", "\ud83d\udc2e", "\ud83d\udc17", "\ud83d\udc34", "\ud83d\udc11", "\ud83d\udc18", "\ud83d\udc3c", "\ud83d\udc27", "\ud83d\udc25", "\ud83d\udc14", "\ud83d\udc0d", "\ud83d\udc22", "\ud83d\udc1b", "\ud83d\udc1d", "\ud83d\udc1c", "\ud83d\udc1e", "\ud83d\udc0c", "\ud83d\udc19", "\ud83d\udc1a", "\ud83d\udc1f", "\ud83d\udc2c", "\ud83d\udc0b", "\ud83d\udc10", "\ud83d\udc0a", "\ud83d\udc2b", "\ud83c\udf40", "\ud83c\udf39", "\ud83c\udf3b", "\ud83c\udf41", "\ud83c\udf3e", "\ud83c\udf44", "\ud83c\udf35", "\ud83c\udf34", "\ud83c\udf33", "\ud83c\udf1e", "\ud83c\udf1a", "\ud83c\udf19", "\ud83c\udf0e", "\ud83c\udf0b", "⚡", "☔", "❄", "⛄", "\ud83c\udf00", "\ud83c\udf08", "\ud83c\udf0a", "\ud83c\udf93", "\ud83c\udf86", "\ud83c\udf83", "\ud83d\udc7b", "\ud83c\udf85", "\ud83c\udf84", "\ud83c\udf81", "\ud83c\udf88", "\ud83d\udd2e", "\ud83c\udfa5", "\ud83d\udcf7", "\ud83d\udcbf", "\ud83d\udcbb", "☎", "\ud83d\udce1", "\ud83d\udcfa", "\ud83d\udcfb", "\ud83d\udd09", "\ud83d\udd14", "⏳", "⏰", "⌚", "\ud83d\udd12", "\ud83d\udd11", "\ud83d\udd0e", "\ud83d\udca1", "\ud83d\udd26", "\ud83d\udd0c", "\ud83d\udd0b", "\ud83d\udebf", "\ud83d\udebd", "\ud83d\udd27", "\ud83d\udd28", "\ud83d\udeaa", "\ud83d\udeac", "\ud83d\udca3", "\ud83d\udd2b", "\ud83d\udd2a", "\ud83d\udc8a", "\ud83d\udc89", "\ud83d\udcb0", "\ud83d\udcb5", "\ud83d\udcb3", "✉", "\ud83d\udceb", "\ud83d\udce6", "\ud83d\udcc5", "\ud83d\udcc1", "✂", "\ud83d\udccc", "\ud83d\udcce", "✒", "✏", "\ud83d\udcd0", "\ud83d\udcda", "\ud83d\udd2c", "\ud83d\udd2d", "\ud83c\udfa8", "\ud83c\udfac", "\ud83c\udfa4", "\ud83c\udfa7", "\ud83c\udfb5", "\ud83c\udfb9", "\ud83c\udfbb", "\ud83c\udfba", "\ud83c\udfb8", "\ud83d\udc7e", "\ud83c\udfae", "\ud83c\udccf", "\ud83c\udfb2", "\ud83c\udfaf", "\ud83c\udfc8", "\ud83c\udfc0", "⚽", "⚾", "\ud83c\udfbe", "\ud83c\udfb1", "\ud83c\udfc9", "\ud83c\udfb3", "\ud83c\udfc1", "\ud83c\udfc7", "\ud83c\udfc6", "\ud83c\udfca", "\ud83c\udfc4", "☕", "\ud83c\udf7c", "\ud83c\udf7a", "\ud83c\udf77", "\ud83c\udf74", "\ud83c\udf55", "\ud83c\udf54", "\ud83c\udf5f", "\ud83c\udf57", "\ud83c\udf71", "\ud83c\udf5a", "\ud83c\udf5c", "\ud83c\udf61", "\ud83c\udf73", "\ud83c\udf5e", "\ud83c\udf69", "\ud83c\udf66", "\ud83c\udf82", "\ud83c\udf70", "\ud83c\udf6a", "\ud83c\udf6b", "\ud83c\udf6d", "\ud83c\udf6f", "\ud83c\udf4e", "\ud83c\udf4f", "\ud83c\udf4a", "\ud83c\udf4b", "\ud83c\udf52", "\ud83c\udf47", "\ud83c\udf49", "\ud83c\udf53", "\ud83c\udf51", "\ud83c\udf4c", "\ud83c\udf50", "\ud83c\udf4d", "\ud83c\udf46", "\ud83c\udf45", "\ud83c\udf3d", "\ud83c\udfe1", "\ud83c\udfe5", "\ud83c\udfe6", "⛪", "\ud83c\udff0", "⛺", "\ud83c\udfed", "\ud83d\uddfb", "\ud83d\uddfd", "\ud83c\udfa0", "\ud83c\udfa1", "⛲", "\ud83c\udfa2", "\ud83d\udea2", "\ud83d\udea4", "⚓", "\ud83d\ude80", "✈", "\ud83d\ude81", "\ud83d\ude82", "\ud83d\ude8b", "\ud83d\ude8e", "\ud83d\ude8c", "\ud83d\ude99", "\ud83d\ude97", "\ud83d\ude95", "\ud83d\ude9b", "\ud83d\udea8", "\ud83d\ude94", "\ud83d\ude92", "\ud83d\ude91", "\ud83d\udeb2", "\ud83d\udea0", "\ud83d\ude9c", "\ud83d\udea6", "⚠", "\ud83d\udea7", "⛽", "\ud83c\udfb0", "\ud83d\uddff", "\ud83c\udfaa", "\ud83c\udfad", "\ud83c\uddef\ud83c\uddf5", "\ud83c\uddf0\ud83c\uddf7", "\ud83c\udde9\ud83c\uddea", "\ud83c\udde8\ud83c\uddf3", "\ud83c\uddfa\ud83c\uddf8", "\ud83c\uddeb\ud83c\uddf7", "\ud83c\uddea\ud83c\uddf8", "\ud83c\uddee\ud83c\uddf9", "\ud83c\uddf7\ud83c\uddfa", "\ud83c\uddec\ud83c\udde7", "1⃣", "2⃣", "3⃣", "4⃣", "5⃣", "6⃣", "7⃣", "8⃣", "9⃣", "0⃣", "\ud83d\udd1f", "❗", "❓", "♥", "♦", "\ud83d\udcaf", "\ud83d\udd17", "\ud83d\udd31", "\ud83d\udd34", "\ud83d\udd35", "\ud83d\udd36", "\ud83d\udd37"};
   private static final int[] offsets = new int[]{0, 4, 8, 12, 16};

   private static int bytesToInt(byte[] var0, int var1) {
      byte var2 = var0[var1];
      byte var3 = var0[var1 + 1];
      byte var4 = var0[var1 + 2];
      return var0[var1 + 3] & 255 | (var2 & 127) << 24 | (var3 & 255) << 16 | (var4 & 255) << 8;
   }

   private static long bytesToLong(byte[] var0, int var1) {
      long var2 = (long)var0[var1];
      long var4 = (long)var0[var1 + 1];
      long var6 = (long)var0[var1 + 2];
      long var8 = (long)var0[var1 + 3];
      long var10 = (long)var0[var1 + 4];
      long var12 = (long)var0[var1 + 5];
      long var14 = (long)var0[var1 + 6];
      return (long)var0[var1 + 7] & 255L | (var2 & 127L) << 56 | (var4 & 255L) << 48 | (var6 & 255L) << 40 | (var8 & 255L) << 32 | (var10 & 255L) << 24 | (var12 & 255L) << 16 | (var14 & 255L) << 8;
   }

   public static String[] emojify(byte[] var0) {
      if (var0.length != 32) {
         throw new IllegalArgumentException("sha256 needs to be exactly 32 bytes");
      } else {
         String[] var1 = new String[5];

         for(int var2 = 0; var2 < 5; ++var2) {
            var1[var2] = emojis[bytesToInt(var0, offsets[var2]) % emojis.length];
         }

         return var1;
      }
   }

   public static String[] emojifyForCall(byte[] var0) {
      String[] var1 = new String[4];

      for(int var2 = 0; var2 < 4; ++var2) {
         var1[var2] = emojis[(int)(bytesToLong(var0, var2 * 8) % (long)emojis.length)];
      }

      return var1;
   }
}
