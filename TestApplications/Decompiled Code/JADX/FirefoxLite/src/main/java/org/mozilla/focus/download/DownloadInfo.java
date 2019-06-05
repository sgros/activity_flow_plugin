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

    public void setRowId(Long l) {
        this.RowId = l;
    }

    public Long getRowId() {
        return this.RowId;
    }

    public void setFileExtension(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.FileExtension = str;
        }
    }

    public String getFileExtension() {
        return this.FileExtension;
    }

    public void setStatusInt(int i) {
        this.Status = i;
    }

    public int getStatus() {
        return this.Status;
    }

    public void setFileUri(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.FileUri = str;
        }
    }

    public String getFileUri() {
        return this.FileUri;
    }

    public void setMimeType(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.MimeType = str;
        }
    }

    public String getMimeType() {
        return this.MimeType;
    }

    public void setMediaUri(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.MediaUri = str;
        }
    }

    public void setDownloadId(Long l) {
        this.DownloadId = l;
    }

    public Long getDownloadId() {
        return this.DownloadId;
    }

    public void setFileName(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.FileName = str;
        }
    }

    public String getFileName() {
        return this.FileName;
    }

    public void setSize(double d) {
        this.Size = convertByteToReadable(d);
    }

    public String getSize() {
        return this.Size;
    }

    public void setDate(long j) {
        this.Date = convertMillis(j);
    }

    public String getDate() {
        return this.Date;
    }

    private String convertMillis(long j) {
        Calendar.getInstance().setTimeInMillis(j);
        return new Formatter().format("%tB %td", new Object[]{r0, r0}).toString();
    }

    private String convertByteToReadable(double d) {
        String[] strArr = new String[]{"bytes", "KB", "MB", "GB"};
        double d2 = d;
        int i = 0;
        while (i < strArr.length && d2 >= 1024.0d) {
            d2 /= 1024.0d;
            i++;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format(Locale.getDefault(), "%.1f", new Object[]{Double.valueOf(d2)}));
        stringBuilder.append(strArr[i]);
        return stringBuilder.toString();
    }

    public boolean existInDownloadManager() {
        return getStatus() != -1;
    }

    public void setSizeSoFar(double d) {
        this.sizeSoFar = d;
    }

    public double getSizeSoFar() {
        return this.sizeSoFar;
    }

    public void setSizeTotal(double d) {
        this.sizeTotal = d;
    }

    public double getSizeTotal() {
        return this.sizeTotal;
    }

    public boolean isRead() {
        return this.isRead;
    }
}
