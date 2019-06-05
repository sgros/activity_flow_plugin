package org.mozilla.focus.download;

import android.text.TextUtils;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;

public class DownloadInfo {
   private String Date;
   private Long DownloadId;
   private String FileExtension = "";
   private String FileName = "";
   private String FileUri = "";
   private String MediaUri = "";
   private String MimeType = "";
   private Long RowId;
   private String Size;
   private int Status;
   private boolean isRead;
   private double sizeSoFar;
   private double sizeTotal;

   private String convertByteToReadable(double var1) {
      String[] var3 = new String[]{"bytes", "KB", "MB", "GB"};

      int var4;
      for(var4 = 0; var4 < var3.length && var1 >= 1024.0D; ++var4) {
         var1 /= 1024.0D;
      }

      StringBuilder var5 = new StringBuilder();
      var5.append(String.format(Locale.getDefault(), "%.1f", var1));
      var5.append(var3[var4]);
      return var5.toString();
   }

   private String convertMillis(long var1) {
      Calendar var3 = Calendar.getInstance();
      var3.setTimeInMillis(var1);
      return (new Formatter()).format("%tB %td", var3, var3).toString();
   }

   public boolean existInDownloadManager() {
      boolean var1;
      if (this.getStatus() != -1) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public String getDate() {
      return this.Date;
   }

   public Long getDownloadId() {
      return this.DownloadId;
   }

   public String getFileExtension() {
      return this.FileExtension;
   }

   public String getFileName() {
      return this.FileName;
   }

   public String getFileUri() {
      return this.FileUri;
   }

   public String getMimeType() {
      return this.MimeType;
   }

   public Long getRowId() {
      return this.RowId;
   }

   public String getSize() {
      return this.Size;
   }

   public double getSizeSoFar() {
      return this.sizeSoFar;
   }

   public double getSizeTotal() {
      return this.sizeTotal;
   }

   public int getStatus() {
      return this.Status;
   }

   public boolean isRead() {
      return this.isRead;
   }

   public void setDate(long var1) {
      this.Date = this.convertMillis(var1);
   }

   public void setDownloadId(Long var1) {
      this.DownloadId = var1;
   }

   public void setFileExtension(String var1) {
      if (!TextUtils.isEmpty(var1)) {
         this.FileExtension = var1;
      }

   }

   public void setFileName(String var1) {
      if (!TextUtils.isEmpty(var1)) {
         this.FileName = var1;
      }

   }

   public void setFileUri(String var1) {
      if (!TextUtils.isEmpty(var1)) {
         this.FileUri = var1;
      }

   }

   public void setMediaUri(String var1) {
      if (!TextUtils.isEmpty(var1)) {
         this.MediaUri = var1;
      }

   }

   public void setMimeType(String var1) {
      if (!TextUtils.isEmpty(var1)) {
         this.MimeType = var1;
      }

   }

   public void setRowId(Long var1) {
      this.RowId = var1;
   }

   public void setSize(double var1) {
      this.Size = this.convertByteToReadable(var1);
   }

   public void setSizeSoFar(double var1) {
      this.sizeSoFar = var1;
   }

   public void setSizeTotal(double var1) {
      this.sizeTotal = var1;
   }

   public void setStatusInt(int var1) {
      this.Status = var1;
   }
}
