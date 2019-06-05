// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.persistence;

public class BookmarkModel
{
    private String id;
    private String title;
    private String url;
    
    public BookmarkModel(final String id, final String title, final String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BookmarkModel{id='");
        sb.append(this.id);
        sb.append('\'');
        sb.append(", title='");
        sb.append(this.title);
        sb.append('\'');
        sb.append(", url='");
        sb.append(this.url);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }
}
