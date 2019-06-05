package com.google.zxing.common;

import java.util.List;

public final class DecoderResult {
   private final List byteSegments;
   private final String ecLevel;
   private Integer erasures;
   private Integer errorsCorrected;
   private int numBits;
   private Object other;
   private final byte[] rawBytes;
   private final int structuredAppendParity;
   private final int structuredAppendSequenceNumber;
   private final String text;

   public DecoderResult(byte[] var1, String var2, List var3, String var4) {
      this(var1, var2, var3, var4, -1, -1);
   }

   public DecoderResult(byte[] var1, String var2, List var3, String var4, int var5, int var6) {
      this.rawBytes = var1;
      int var7;
      if (var1 == null) {
         var7 = 0;
      } else {
         var7 = var1.length * 8;
      }

      this.numBits = var7;
      this.text = var2;
      this.byteSegments = var3;
      this.ecLevel = var4;
      this.structuredAppendParity = var6;
      this.structuredAppendSequenceNumber = var5;
   }

   public List getByteSegments() {
      return this.byteSegments;
   }

   public String getECLevel() {
      return this.ecLevel;
   }

   public Integer getErasures() {
      return this.erasures;
   }

   public Integer getErrorsCorrected() {
      return this.errorsCorrected;
   }

   public int getNumBits() {
      return this.numBits;
   }

   public Object getOther() {
      return this.other;
   }

   public byte[] getRawBytes() {
      return this.rawBytes;
   }

   public int getStructuredAppendParity() {
      return this.structuredAppendParity;
   }

   public int getStructuredAppendSequenceNumber() {
      return this.structuredAppendSequenceNumber;
   }

   public String getText() {
      return this.text;
   }

   public boolean hasStructuredAppend() {
      boolean var1;
      if (this.structuredAppendParity >= 0 && this.structuredAppendSequenceNumber >= 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void setErasures(Integer var1) {
      this.erasures = var1;
   }

   public void setErrorsCorrected(Integer var1) {
      this.errorsCorrected = var1;
   }

   public void setNumBits(int var1) {
      this.numBits = var1;
   }

   public void setOther(Object var1) {
      this.other = var1;
   }
}
