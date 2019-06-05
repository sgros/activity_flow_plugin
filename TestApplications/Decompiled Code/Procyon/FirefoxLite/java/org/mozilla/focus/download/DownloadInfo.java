// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.download;

import android.text.TextUtils;
import java.util.Formatter;
import java.util.Calendar;
import java.util.Locale;

public class DownloadInfo
{
    private String Date;
    private Long DownloadId;
    private String FileExtension;
    private String FileName;
    private String FileUri;
    private String MediaUri;
    private String MimeType;
    private Long RowId;
    private String Size;
    private int Status;
    private boolean isRead;
    private double sizeSoFar;
    private double sizeTotal;
    
    public DownloadInfo() {
        this.FileName = "";
        this.MediaUri = "";
        this.MimeType = "";
        this.FileUri = "";
        this.FileExtension = "";
    }
    
    private String convertByteToReadable(double d) {
        String[] array;
        int n;
        for (array = new String[] { "bytes", "KB", "MB", "GB" }, n = 0; n < array.length && d >= 1024.0; d /= 1024.0, ++n) {}
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.getDefault(), "%.1f", d));
        sb.append(array[n]);
        return sb.toString();
    }
    
    private String convertMillis(final long timeInMillis) {
        final Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(timeInMillis);
        return new Formatter().format("%tB %td", instance, instance).toString();
    }
    
    public boolean existInDownloadManager() {
        return this.getStatus() != -1;
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
    
    public void setDate(final long n) {
        this.Date = this.convertMillis(n);
    }
    
    public void setDownloadId(final Long downloadId) {
        this.DownloadId = downloadId;
    }
    
    public void setFileExtension(final String fileExtension) {
        if (!TextUtils.isEmpty((CharSequence)fileExtension)) {
            this.FileExtension = fileExtension;
        }
    }
    
    public void setFileName(final String fileName) {
        if (!TextUtils.isEmpty((CharSequence)fileName)) {
            this.FileName = fileName;
        }
    }
    
    public void setFileUri(final String fileUri) {
        if (!TextUtils.isEmpty((CharSequence)fileUri)) {
            this.FileUri = fileUri;
        }
    }
    
    public void setMediaUri(final String mediaUri) {
        if (!TextUtils.isEmpty((CharSequence)mediaUri)) {
            this.MediaUri = mediaUri;
        }
    }
    
    public void setMimeType(final String mimeType) {
        if (!TextUtils.isEmpty((CharSequence)mimeType)) {
            this.MimeType = mimeType;
        }
    }
    
    public void setRowId(final Long rowId) {
        this.RowId = rowId;
    }
    
    public void setSize(final double n) {
        this.Size = this.convertByteToReadable(n);
    }
    
    public void setSizeSoFar(final double sizeSoFar) {
        this.sizeSoFar = sizeSoFar;
    }
    
    public void setSizeTotal(final double sizeTotal) {
        this.sizeTotal = sizeTotal;
    }
    
    public void setStatusInt(final int status) {
        this.Status = status;
    }
}
