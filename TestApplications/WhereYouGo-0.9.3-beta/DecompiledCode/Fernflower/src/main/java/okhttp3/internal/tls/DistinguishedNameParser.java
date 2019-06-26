package okhttp3.internal.tls;

import javax.security.auth.x500.X500Principal;

final class DistinguishedNameParser {
   private int beg;
   private char[] chars;
   private int cur;
   private final String dn;
   private int end;
   private final int length;
   private int pos;

   public DistinguishedNameParser(X500Principal var1) {
      this.dn = var1.getName("RFC2253");
      this.length = this.dn.length();
   }

   private String escapedAV() {
      this.beg = this.pos;
      this.end = this.pos;

      String var3;
      while(this.pos < this.length) {
         char[] var1;
         int var2;
         switch(this.chars[this.pos]) {
         case ' ':
            this.cur = this.end;
            ++this.pos;
            var1 = this.chars;
            var2 = this.end++;

            for(var1[var2] = (char)32; this.pos < this.length && this.chars[this.pos] == ' '; ++this.pos) {
               var1 = this.chars;
               var2 = this.end++;
               var1[var2] = (char)32;
            }

            if (this.pos == this.length || this.chars[this.pos] == ',' || this.chars[this.pos] == '+' || this.chars[this.pos] == ';') {
               var3 = new String(this.chars, this.beg, this.cur - this.beg);
               return var3;
            }
            break;
         case '+':
         case ',':
         case ';':
            var3 = new String(this.chars, this.beg, this.end - this.beg);
            return var3;
         case '\\':
            var1 = this.chars;
            var2 = this.end++;
            var1[var2] = this.getEscaped();
            ++this.pos;
            break;
         default:
            var1 = this.chars;
            var2 = this.end++;
            var1[var2] = (char)this.chars[this.pos];
            ++this.pos;
         }
      }

      var3 = new String(this.chars, this.beg, this.end - this.beg);
      return var3;
   }

   private int getByte(int var1) {
      if (var1 + 1 >= this.length) {
         throw new IllegalStateException("Malformed DN: " + this.dn);
      } else {
         char var2 = this.chars[var1];
         int var4;
         if (var2 >= '0' && var2 <= '9') {
            var4 = var2 - 48;
         } else if (var2 >= 'a' && var2 <= 'f') {
            var4 = var2 - 87;
         } else {
            if (var2 < 'A' || var2 > 'F') {
               throw new IllegalStateException("Malformed DN: " + this.dn);
            }

            var4 = var2 - 55;
         }

         char var3 = this.chars[var1 + 1];
         if (var3 >= '0' && var3 <= '9') {
            var1 = var3 - 48;
         } else if (var3 >= 'a' && var3 <= 'f') {
            var1 = var3 - 87;
         } else {
            if (var3 < 'A' || var3 > 'F') {
               throw new IllegalStateException("Malformed DN: " + this.dn);
            }

            var1 = var3 - 55;
         }

         return (var4 << 4) + var1;
      }
   }

   private char getEscaped() {
      ++this.pos;
      if (this.pos == this.length) {
         throw new IllegalStateException("Unexpected end of DN: " + this.dn);
      } else {
         char var1;
         char var2;
         switch(this.chars[this.pos]) {
         case ' ':
         case '"':
         case '#':
         case '%':
         case '*':
         case '+':
         case ',':
         case ';':
         case '<':
         case '=':
         case '>':
         case '\\':
         case '_':
            var1 = this.chars[this.pos];
            var2 = var1;
            break;
         default:
            var1 = this.getUTF8();
            var2 = var1;
         }

         return var2;
      }
   }

   private char getUTF8() {
      byte var1 = 63;
      int var2 = this.getByte(this.pos);
      ++this.pos;
      char var3;
      char var7;
      if (var2 < 128) {
         var7 = (char)var2;
         var3 = var7;
      } else {
         var3 = (char)var1;
         if (var2 >= 192) {
            var3 = (char)var1;
            if (var2 <= 247) {
               byte var4;
               if (var2 <= 223) {
                  var4 = 1;
                  var2 &= 31;
               } else if (var2 <= 239) {
                  var4 = 2;
                  var2 &= 15;
               } else {
                  var4 = 3;
                  var2 &= 7;
               }

               int var5 = 0;

               while(true) {
                  if (var5 >= var4) {
                     var7 = (char)var2;
                     var3 = var7;
                     break;
                  }

                  ++this.pos;
                  var3 = (char)var1;
                  if (this.pos == this.length) {
                     break;
                  }

                  var3 = (char)var1;
                  if (this.chars[this.pos] != '\\') {
                     break;
                  }

                  ++this.pos;
                  int var6 = this.getByte(this.pos);
                  ++this.pos;
                  var3 = (char)var1;
                  if ((var6 & 192) != 128) {
                     break;
                  }

                  var2 = (var2 << 6) + (var6 & 63);
                  ++var5;
               }
            }
         }
      }

      return var3;
   }

   private String hexAV() {
      if (this.pos + 4 >= this.length) {
         throw new IllegalStateException("Unexpected end of DN: " + this.dn);
      } else {
         this.beg = this.pos++;

         int var3;
         label57:
         while(true) {
            if (this.pos == this.length || this.chars[this.pos] == '+' || this.chars[this.pos] == ',' || this.chars[this.pos] == ';') {
               this.end = this.pos;
               break;
            }

            if (this.chars[this.pos] == ' ') {
               this.end = this.pos++;

               while(true) {
                  if (this.pos >= this.length || this.chars[this.pos] != ' ') {
                     break label57;
                  }

                  ++this.pos;
               }
            }

            if (this.chars[this.pos] >= 'A' && this.chars[this.pos] <= 'F') {
               char[] var2 = this.chars;
               var3 = this.pos;
               var2[var3] = (char)((char)(var2[var3] + 32));
            }

            ++this.pos;
         }

         int var1 = this.end - this.beg;
         if (var1 >= 5 && (var1 & 1) != 0) {
            byte[] var5 = new byte[var1 / 2];
            var3 = 0;

            for(int var4 = this.beg + 1; var3 < var5.length; ++var3) {
               var5[var3] = (byte)((byte)this.getByte(var4));
               var4 += 2;
            }

            return new String(this.chars, this.beg, var1);
         } else {
            throw new IllegalStateException("Unexpected end of DN: " + this.dn);
         }
      }
   }

   private String nextAT() {
      while(this.pos < this.length && this.chars[this.pos] == ' ') {
         ++this.pos;
      }

      String var1;
      if (this.pos == this.length) {
         var1 = null;
      } else {
         for(this.beg = this.pos++; this.pos < this.length && this.chars[this.pos] != '=' && this.chars[this.pos] != ' '; ++this.pos) {
         }

         if (this.pos >= this.length) {
            throw new IllegalStateException("Unexpected end of DN: " + this.dn);
         }

         this.end = this.pos;
         if (this.chars[this.pos] == ' ') {
            while(true) {
               if (this.pos >= this.length || this.chars[this.pos] == '=' || this.chars[this.pos] != ' ') {
                  if (this.chars[this.pos] != '=' || this.pos == this.length) {
                     throw new IllegalStateException("Unexpected end of DN: " + this.dn);
                  }
                  break;
               }

               ++this.pos;
            }
         }

         ++this.pos;

         while(this.pos < this.length && this.chars[this.pos] == ' ') {
            ++this.pos;
         }

         if (this.end - this.beg > 4 && this.chars[this.beg + 3] == '.' && (this.chars[this.beg] == 'O' || this.chars[this.beg] == 'o') && (this.chars[this.beg + 1] == 'I' || this.chars[this.beg + 1] == 'i') && (this.chars[this.beg + 2] == 'D' || this.chars[this.beg + 2] == 'd')) {
            this.beg += 4;
         }

         var1 = new String(this.chars, this.beg, this.end - this.beg);
      }

      return var1;
   }

   private String quotedAV() {
      ++this.pos;
      this.beg = this.pos;

      for(this.end = this.beg; this.pos != this.length; ++this.end) {
         if (this.chars[this.pos] == '"') {
            ++this.pos;

            while(this.pos < this.length && this.chars[this.pos] == ' ') {
               ++this.pos;
            }

            return new String(this.chars, this.beg, this.end - this.beg);
         }

         if (this.chars[this.pos] == '\\') {
            this.chars[this.end] = this.getEscaped();
         } else {
            this.chars[this.end] = (char)this.chars[this.pos];
         }

         ++this.pos;
      }

      throw new IllegalStateException("Unexpected end of DN: " + this.dn);
   }

   public String findMostSpecific(String var1) {
      this.pos = 0;
      this.beg = 0;
      this.end = 0;
      this.cur = 0;
      this.chars = this.dn.toCharArray();
      String var2 = this.nextAT();
      String var3 = var2;
      if (var2 == null) {
         var2 = null;
      } else {
         while(true) {
            var2 = "";
            if (this.pos == this.length) {
               var2 = null;
               break;
            }

            switch(this.chars[this.pos]) {
            case '"':
               var2 = this.quotedAV();
               break;
            case '#':
               var2 = this.hexAV();
            case '+':
            case ',':
            case ';':
               break;
            default:
               var2 = this.escapedAV();
            }

            if (var1.equalsIgnoreCase(var3)) {
               break;
            }

            if (this.pos >= this.length) {
               var2 = null;
               break;
            }

            if (this.chars[this.pos] != ',' && this.chars[this.pos] != ';' && this.chars[this.pos] != '+') {
               throw new IllegalStateException("Malformed DN: " + this.dn);
            }

            ++this.pos;
            var2 = this.nextAT();
            var3 = var2;
            if (var2 == null) {
               throw new IllegalStateException("Malformed DN: " + this.dn);
            }
         }
      }

      return var2;
   }
}
