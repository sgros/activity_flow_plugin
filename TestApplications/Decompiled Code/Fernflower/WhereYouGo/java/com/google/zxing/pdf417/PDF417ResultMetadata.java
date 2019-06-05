package com.google.zxing.pdf417;

public final class PDF417ResultMetadata {
   private String fileId;
   private boolean lastSegment;
   private int[] optionalData;
   private int segmentIndex;

   public String getFileId() {
      return this.fileId;
   }

   public int[] getOptionalData() {
      return this.optionalData;
   }

   public int getSegmentIndex() {
      return this.segmentIndex;
   }

   public boolean isLastSegment() {
      return this.lastSegment;
   }

   public void setFileId(String var1) {
      this.fileId = var1;
   }

   public void setLastSegment(boolean var1) {
      this.lastSegment = var1;
   }

   public void setOptionalData(int[] var1) {
      this.optionalData = var1;
   }

   public void setSegmentIndex(int var1) {
      this.segmentIndex = var1;
   }
}
