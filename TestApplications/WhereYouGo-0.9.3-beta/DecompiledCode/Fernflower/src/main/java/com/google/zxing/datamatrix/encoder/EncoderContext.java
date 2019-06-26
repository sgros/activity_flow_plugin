package com.google.zxing.datamatrix.encoder;

import com.google.zxing.Dimension;
import java.nio.charset.Charset;

final class EncoderContext {
   private final StringBuilder codewords;
   private Dimension maxSize;
   private Dimension minSize;
   private final String msg;
   private int newEncoding;
   int pos;
   private SymbolShapeHint shape;
   private int skipAtEnd;
   private SymbolInfo symbolInfo;

   EncoderContext(String var1) {
      byte[] var2 = var1.getBytes(Charset.forName("ISO-8859-1"));
      StringBuilder var3 = new StringBuilder(var2.length);
      int var4 = 0;

      for(int var5 = var2.length; var4 < var5; ++var4) {
         char var6 = (char)(var2[var4] & 255);
         if (var6 == '?' && var1.charAt(var4) != '?') {
            throw new IllegalArgumentException("Message contains characters outside ISO-8859-1 encoding.");
         }

         var3.append(var6);
      }

      this.msg = var3.toString();
      this.shape = SymbolShapeHint.FORCE_NONE;
      this.codewords = new StringBuilder(var1.length());
      this.newEncoding = -1;
   }

   private int getTotalMessageCharCount() {
      return this.msg.length() - this.skipAtEnd;
   }

   public int getCodewordCount() {
      return this.codewords.length();
   }

   public StringBuilder getCodewords() {
      return this.codewords;
   }

   public char getCurrent() {
      return this.msg.charAt(this.pos);
   }

   public char getCurrentChar() {
      return this.msg.charAt(this.pos);
   }

   public String getMessage() {
      return this.msg;
   }

   public int getNewEncoding() {
      return this.newEncoding;
   }

   public int getRemainingCharacters() {
      return this.getTotalMessageCharCount() - this.pos;
   }

   public SymbolInfo getSymbolInfo() {
      return this.symbolInfo;
   }

   public boolean hasMoreCharacters() {
      boolean var1;
      if (this.pos < this.getTotalMessageCharCount()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void resetEncoderSignal() {
      this.newEncoding = -1;
   }

   public void resetSymbolInfo() {
      this.symbolInfo = null;
   }

   public void setSizeConstraints(Dimension var1, Dimension var2) {
      this.minSize = var1;
      this.maxSize = var2;
   }

   public void setSkipAtEnd(int var1) {
      this.skipAtEnd = var1;
   }

   public void setSymbolShape(SymbolShapeHint var1) {
      this.shape = var1;
   }

   public void signalEncoderChange(int var1) {
      this.newEncoding = var1;
   }

   public void updateSymbolInfo() {
      this.updateSymbolInfo(this.getCodewordCount());
   }

   public void updateSymbolInfo(int var1) {
      if (this.symbolInfo == null || var1 > this.symbolInfo.getDataCapacity()) {
         this.symbolInfo = SymbolInfo.lookup(var1, this.shape, this.minSize, this.maxSize, true);
      }

   }

   public void writeCodeword(char var1) {
      this.codewords.append(var1);
   }

   public void writeCodewords(String var1) {
      this.codewords.append(var1);
   }
}
