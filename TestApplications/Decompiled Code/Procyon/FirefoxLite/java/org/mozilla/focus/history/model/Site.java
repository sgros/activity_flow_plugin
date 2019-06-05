// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.history.model;

import org.mozilla.focus.utils.AppConstants;

public class Site
{
    private String favIconUri;
    private long id;
    private boolean isDefault;
    private long lastViewTimestamp;
    private String title;
    private String url;
    private long viewCount;
    
    public Site(final long id, final String title, final String url, final long viewCount, final long lastViewTimestamp, final String favIconUri) {
        this.isDefault = false;
        this.id = id;
        this.title = title;
        this.url = url;
        this.viewCount = viewCount;
        this.lastViewTimestamp = lastViewTimestamp;
        this.favIconUri = favIconUri;
    }
    
    private String toStringNormal() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Site{id='");
        sb.append(this.id);
        sb.append('\'');
        sb.append(", title='");
        sb.append(this.title);
        sb.append('\'');
        sb.append(", url='");
        sb.append(this.url);
        sb.append('\'');
        sb.append(", viewCount='");
        sb.append(this.viewCount);
        sb.append('\'');
        sb.append(", lastViewTimestamp='");
        sb.append(this.lastViewTimestamp);
        sb.append('\'');
        sb.append(", favIconUri='");
        sb.append(this.favIconUri);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }
    
    private String toStringRelease() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Site{id='");
        sb.append(this.id);
        sb.append('\'');
        sb.append(", viewCount='");
        sb.append(this.viewCount);
        sb.append('\'');
        sb.append(", lastViewTimestamp='");
        sb.append(this.lastViewTimestamp);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof Site && ((Site)o).getId() == this.getId();
    }
    
    public String getFavIconUri() {
        return this.favIconUri;
    }
    
    public long getId() {
        return this.id;
    }
    
    public long getLastViewTimestamp() {
        return this.lastViewTimestamp;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public long getViewCount() {
        return this.viewCount;
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    public boolean isDefault() {
        return this.isDefault;
    }
    
    public void setDefault(final boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    public void setViewCount(final long viewCount) {
        this.viewCount = viewCount;
    }
    
    @Override
    public String toString() {
        String s;
        if (AppConstants.isReleaseBuild()) {
            s = this.toStringRelease();
        }
        else {
            s = this.toStringNormal();
        }
        return s;
    }
}
