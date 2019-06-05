// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.screenshot.model;

import java.io.Serializable;

public class Screenshot implements Serializable
{
    private String category;
    private int categoryVersion;
    private long id;
    private String imageUri;
    private long timestamp;
    private String title;
    private String url;
    
    public Screenshot() {
        this.category = "";
        this.categoryVersion = 0;
    }
    
    public Screenshot(final String title, final String url, final long timestamp, final String imageUri) {
        this.category = "";
        this.categoryVersion = 0;
        this.title = title;
        this.url = url;
        this.timestamp = timestamp;
        this.imageUri = imageUri;
    }
    
    public String getCategory() {
        return this.category;
    }
    
    public int getCategoryVersion() {
        return this.categoryVersion;
    }
    
    public long getId() {
        return this.id;
    }
    
    public String getImageUri() {
        return this.imageUri;
    }
    
    public long getTimestamp() {
        return this.timestamp;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public void setCategory(final String category) {
        this.category = category;
    }
    
    public void setCategoryVersion(final int categoryVersion) {
        this.categoryVersion = categoryVersion;
    }
    
    public void setId(final long id) {
        this.id = id;
    }
    
    public void setImageUri(final String imageUri) {
        this.imageUri = imageUri;
    }
    
    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Screenshot{id=");
        sb.append(this.id);
        sb.append(", title='");
        sb.append(this.title);
        sb.append('\'');
        sb.append(", url='");
        sb.append(this.url);
        sb.append('\'');
        sb.append(", timestamp=");
        sb.append(this.timestamp);
        sb.append(", imageUri='");
        sb.append(this.imageUri);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }
}
