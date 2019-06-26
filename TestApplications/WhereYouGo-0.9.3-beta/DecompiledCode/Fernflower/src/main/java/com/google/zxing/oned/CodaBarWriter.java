package com.google.zxing.oned;

public final class CodaBarWriter extends OneDimensionalCodeWriter {
   private static final char[] ALT_START_END_CHARS = new char[]{'T', 'N', '*', 'E'};
   private static final char[] CHARS_WHICH_ARE_TEN_LENGTH_EACH_AFTER_DECODED = new char[]{'/', ':', '+', '.'};
   private static final char DEFAULT_GUARD;
   private static final char[] START_END_CHARS = new char[]{'A', 'B', 'C', 'D'};

   static {
      DEFAULT_GUARD = (char)START_END_CHARS[0];
   }

   public boolean[] encode(String var1) {
      String var2;
      boolean var10;
      if (var1.length() < 2) {
         var2 = DEFAULT_GUARD + var1 + DEFAULT_GUARD;
      } else {
         char var5 = Character.toUpperCase(var1.charAt(0));
         char var6 = Character.toUpperCase(var1.charAt(var1.length() - 1));
         boolean var7 = CodaBarReader.arrayContains(START_END_CHARS, var5);
         boolean var8 = CodaBarReader.arrayContains(START_END_CHARS, var6);
         boolean var9 = CodaBarReader.arrayContains(ALT_START_END_CHARS, var5);
         var10 = CodaBarReader.arrayContains(ALT_START_END_CHARS, var6);
         if (var7) {
            var2 = var1;
            if (!var8) {
               throw new IllegalArgumentException("Invalid start/end guards: " + var1);
            }
         } else if (var9) {
            var2 = var1;
            if (!var10) {
               throw new IllegalArgumentException("Invalid start/end guards: " + var1);
            }
         } else {
            if (var8 || var10) {
               throw new IllegalArgumentException("Invalid start/end guards: " + var1);
            }

            var2 = DEFAULT_GUARD + var1 + DEFAULT_GUARD;
         }
      }

      int var3 = 20;

      int var4;
      for(var4 = 1; var4 < var2.length() - 1; ++var4) {
         if (!Character.isDigit(var2.charAt(var4)) && var2.charAt(var4) != '-' && var2.charAt(var4) != '$') {
            if (!CodaBarReader.arrayContains(CHARS_WHICH_ARE_TEN_LENGTH_EACH_AFTER_DECODED, var2.charAt(var4))) {
               throw new IllegalArgumentException("Cannot encode : '" + var2.charAt(var4) + '\'');
            }

            var3 += 10;
         } else {
            var3 += 9;
         }
      }

      boolean[] var15 = new boolean[var2.length() - 1 + var3];
      var4 = 0;

      for(int var11 = 0; var11 < var2.length(); var4 = var3) {
         char var16;
         label67: {
            char var12 = Character.toUpperCase(var2.charAt(var11));
            if (var11 != 0) {
               var16 = var12;
               if (var11 != var2.length() - 1) {
                  break label67;
               }
            }

            switch(var12) {
            case '*':
               var16 = 'C';
               break;
            case 'E':
               var16 = 'D';
               break;
            case 'N':
               var16 = 'B';
               break;
            case 'T':
               var16 = 'A';
               break;
            default:
               var16 = var12;
            }
         }

         byte var13 = 0;
         int var14 = 0;

         int var17;
         while(true) {
            var17 = var13;
            if (var14 >= CodaBarReader.ALPHABET.length) {
               break;
            }

            if (var16 == CodaBarReader.ALPHABET[var14]) {
               var17 = CodaBarReader.CHARACTER_ENCODINGS[var14];
               break;
            }

            ++var14;
         }

         var10 = true;
         var3 = 0;
         var14 = 0;

         while(true) {
            while(var14 < 7) {
               var15[var4] = var10;
               ++var4;
               if ((var17 >> 6 - var14 & 1) != 0 && var3 != 1) {
                  ++var3;
               } else {
                  if (!var10) {
                     var10 = true;
                  } else {
                     var10 = false;
                  }

                  ++var14;
                  var3 = 0;
               }
            }

            var3 = var4;
            if (var11 < var2.length() - 1) {
               var15[var4] = false;
               var3 = var4 + 1;
            }

            ++var11;
            break;
         }
      }

      return var15;
   }
}
