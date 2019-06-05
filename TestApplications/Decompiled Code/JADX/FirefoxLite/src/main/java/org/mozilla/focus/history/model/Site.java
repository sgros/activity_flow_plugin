package org.mozilla.focus.history.model;

import org.mozilla.focus.utils.AppConstants;

public class Site {
    private String favIconUri;
    /* renamed from: id */
    private long f43id;
    private boolean isDefault = false;
    private long lastViewTimestamp;
    private String title;
    private String url;
    private long viewCount;

    public Site(long j, String str, String str2, long j2, long j3, String str3) {
        this.f43id = j;
        this.title = str;
        this.url = str2;
        this.viewCount = j2;
        this.lastViewTimestamp = j3;
        this.favIconUri = str3;
    }

    public long getId() {
        return this.f43id;
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

    public void setViewCount(long j) {
        this.viewCount = j;
    }

    public long getLastViewTimestamp() {
        return this.lastViewTimestamp;
    }

    public String getFavIconUri() {
        return this.favIconUri;
    }

    public boolean equals(Object obj) {
        return (obj instanceof Site) && ((Site) obj).getId() == getId();
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return AppConstants.isReleaseBuild() ? toStringRelease() : toStringNormal();
    }

    private String toStringRelease() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Site{id='");
        stringBuilder.append(this.f43id);
        stringBuilder.append('\'');
        stringBuilder.append(", viewCount='");
        stringBuilder.append(this.viewCount);
        stringBuilder.append('\'');
        stringBuilder.append(", lastViewTimestamp='");
        stringBuilder.append(this.lastViewTimestamp);
        stringBuilder.append('\'');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    private String toStringNormal() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Site{id='");
        stringBuilder.append(this.f43id);
        stringBuilder.append('\'');
        stringBuilder.append(", title='");
        stringBuilder.append(this.title);
        stringBuilder.append('\'');
        stringBuilder.append(", url='");
        stringBuilder.append(this.url);
        stringBuilder.append('\'');
        stringBuilder.append(", viewCount='");
        stringBuilder.append(this.viewCount);
        stringBuilder.append('\'');
        stringBuilder.append(", lastViewTimestamp='");
        stringBuilder.append(this.lastViewTimestamp);
        stringBuilder.append('\'');
        stringBuilder.append(", favIconUri='");
        stringBuilder.append(this.favIconUri);
        stringBuilder.append('\'');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public boolean isDefault() {
        return this.isDefault;
    }

    public void setDefault(boolean z) {
        this.isDefault = z;
    }
}
