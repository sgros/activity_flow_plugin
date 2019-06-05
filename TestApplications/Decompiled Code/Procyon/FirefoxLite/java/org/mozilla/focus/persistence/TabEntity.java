// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.persistence;

public class TabEntity
{
    private String id;
    private String parentId;
    private String title;
    private String url;
    
    public TabEntity(final String s, final String s2) {
        this(s, s2, "", "");
    }
    
    public TabEntity(final String id, final String parentId, final String title, final String url) {
        this.id = id;
        this.parentId = parentId;
        this.title = title;
        this.url = url;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getParentId() {
        return this.parentId;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getUrl() {
        return this.url;
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
        sb.append("TabEntity{id='");
        sb.append(this.id);
        sb.append('\'');
        sb.append(", parentId='");
        sb.append(this.parentId);
        sb.append('\'');
        sb.append(", title='");
        sb.append(this.title);
        sb.append('\'');
        sb.append(", url='");
        sb.append(this.url);
        sb.append('}');
        return sb.toString();
    }
}
