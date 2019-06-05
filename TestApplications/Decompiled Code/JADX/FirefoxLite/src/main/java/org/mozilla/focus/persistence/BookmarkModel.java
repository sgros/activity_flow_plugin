package org.mozilla.focus.persistence;

public class BookmarkModel {
    /* renamed from: id */
    private String f48id;
    private String title;
    private String url;

    public BookmarkModel(String str, String str2, String str3) {
        this.f48id = str;
        this.title = str2;
        this.url = str3;
    }

    public String getId() {
        return this.f48id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getUrl() {
        return this.url;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("BookmarkModel{id='");
        stringBuilder.append(this.f48id);
        stringBuilder.append('\'');
        stringBuilder.append(", title='");
        stringBuilder.append(this.title);
        stringBuilder.append('\'');
        stringBuilder.append(", url='");
        stringBuilder.append(this.url);
        stringBuilder.append('\'');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
