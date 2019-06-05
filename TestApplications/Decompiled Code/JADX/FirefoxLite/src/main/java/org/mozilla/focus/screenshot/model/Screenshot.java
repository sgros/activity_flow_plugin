package org.mozilla.focus.screenshot.model;

import java.io.Serializable;

public class Screenshot implements Serializable {
    private String category = "";
    private int categoryVersion = 0;
    /* renamed from: id */
    private long f51id;
    private String imageUri;
    private long timestamp;
    private String title;
    private String url;

    public Screenshot(String str, String str2, long j, String str3) {
        this.title = str;
        this.url = str2;
        this.timestamp = j;
        this.imageUri = str3;
    }

    public long getId() {
        return this.f51id;
    }

    public void setId(long j) {
        this.f51id = j;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String getUrl() {
        return this.url;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long j) {
        this.timestamp = j;
    }

    public String getImageUri() {
        return this.imageUri;
    }

    public void setImageUri(String str) {
        this.imageUri = str;
    }

    public void setCategory(String str) {
        this.category = str;
    }

    public String getCategory() {
        return this.category;
    }

    public int getCategoryVersion() {
        return this.categoryVersion;
    }

    public void setCategoryVersion(int i) {
        this.categoryVersion = i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Screenshot{id=");
        stringBuilder.append(this.f51id);
        stringBuilder.append(", title='");
        stringBuilder.append(this.title);
        stringBuilder.append('\'');
        stringBuilder.append(", url='");
        stringBuilder.append(this.url);
        stringBuilder.append('\'');
        stringBuilder.append(", timestamp=");
        stringBuilder.append(this.timestamp);
        stringBuilder.append(", imageUri='");
        stringBuilder.append(this.imageUri);
        stringBuilder.append('\'');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
