package org.mozilla.focus.webkit.matcher.util;

public abstract class FocusString {
   final int offsetEnd;
   final int offsetStart;
   protected final String string;

   private FocusString(String var1, int var2, int var3) {
      this.string = var1;
      this.offsetStart = var2;
      this.offsetEnd = var3;
      if (var2 > var3 || var2 < 0 || var3 < 0) {
         throw new StringIndexOutOfBoundsException("Cannot create negative-length String");
      }
   }

   // $FF: synthetic method
   FocusString(String var1, int var2, int var3, Object var4) {
      this(var1, var2, var3);
   }

   public static FocusString create(String var0) {
      return new FocusString.ForwardString(var0, 0, var0.length());
   }

   public abstract char charAt(int var1);

   protected abstract boolean isReversed();

   public int length() {
      return this.offsetEnd - this.offsetStart;
   }

   public FocusString reverse() {
      return (FocusString)(this.isReversed() ? new FocusString.ForwardString(this.string, this.offsetStart, this.offsetEnd) : new FocusString.ReverseString(this.string, this.offsetStart, this.offsetEnd));
   }

   public abstract FocusString substring(int var1);

   private static class ForwardString extends FocusString {
      public ForwardString(String var1, int var2, int var3) {
         super(var1, var2, var3, null);
      }

      public char charAt(int var1) {
         if (var1 <= this.length()) {
            return this.string.charAt(var1 + this.offsetStart);
         } else {
            throw new StringIndexOutOfBoundsException();
         }
      }

      protected boolean isReversed() {
         return false;
      }

      public FocusString substring(int var1) {
         return new FocusString.ForwardString(this.string, this.offsetStart + var1, this.offsetEnd);
      }
   }

   private static class ReverseString extends FocusString {
      public ReverseString(String var1, int var2, int var3) {
         super(var1, var2, var3, null);
      }

      public char charAt(int var1) {
         if (var1 <= this.length()) {
            return this.string.charAt(this.length() - 1 - var1 + this.offsetStart);
         } else {
            throw new StringIndexOutOfBoundsException();
         }
      }

      protected boolean isReversed() {
         return true;
      }

      public FocusString substring(int var1) {
         return new FocusString.ReverseString(this.string, this.offsetStart, this.offsetEnd - var1);
      }
   }
}
