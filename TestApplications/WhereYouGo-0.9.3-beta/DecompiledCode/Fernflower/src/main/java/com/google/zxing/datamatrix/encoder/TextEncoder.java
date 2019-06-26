package com.google.zxing.datamatrix.encoder;

final class TextEncoder extends C40Encoder {
   int encodeChar(char var1, StringBuilder var2) {
      int var3 = 1;
      if (var1 == ' ') {
         var2.append('\u0003');
      } else if (var1 >= '0' && var1 <= '9') {
         var2.append((char)(var1 - 48 + 4));
      } else if (var1 >= 'a' && var1 <= 'z') {
         var2.append((char)(var1 - 97 + 14));
      } else if (var1 >= 0 && var1 <= 31) {
         var2.append('\u0000');
         var2.append(var1);
         var3 = 2;
      } else if (var1 >= '!' && var1 <= '/') {
         var2.append('\u0001');
         var2.append((char)(var1 - 33));
         var3 = 2;
      } else if (var1 >= ':' && var1 <= '@') {
         var2.append('\u0001');
         var2.append((char)(var1 - 58 + 15));
         var3 = 2;
      } else if (var1 >= '[' && var1 <= '_') {
         var2.append('\u0001');
         var2.append((char)(var1 - 91 + 22));
         var3 = 2;
      } else if (var1 == '`') {
         var2.append('\u0002');
         var2.append((char)(var1 - 96));
         var3 = 2;
      } else if (var1 >= 'A' && var1 <= 'Z') {
         var2.append('\u0002');
         var2.append((char)(var1 - 65 + 1));
         var3 = 2;
      } else if (var1 >= '{' && var1 <= 127) {
         var2.append('\u0002');
         var2.append((char)(var1 - 123 + 27));
         var3 = 2;
      } else if (var1 >= 128) {
         var2.append("\u0001\u001e");
         var3 = this.encodeChar((char)(var1 - 128), var2) + 2;
      } else {
         HighLevelEncoder.illegalCharacter(var1);
         var3 = -1;
      }

      return var3;
   }

   public int getEncodingMode() {
      return 2;
   }
}
