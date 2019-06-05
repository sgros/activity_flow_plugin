package org.mozilla.focus.persistence;

public class TabEntity {
    /* renamed from: id */
    private String f49id;
    private String parentId;
    private String title;
    private String url;

    public TabEntity(String str, String str2) {
        this(str, str2, "", "");
    }

    public TabEntity(String str, String str2, String str3, String str4) {
        this.f49id = str;
        this.parentId = str2;
        this.title = str3;
        this.url = str4;
    }

    public String getId() {
        return this.f49id;
    }

    public String getParentId() {
        return this.parentId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("TabEntity{id='");
        stringBuilder.append(this.f49id);
        stringBuilder.append('\'');
        stringBuilder.append(", parentId='");
        stringBuilder.append(this.parentId);
        stringBuilder.append('\'');
        stringBuilder.append(", title='");
        stringBuilder.append(this.title);
        stringBuilder.append('\'');
        stringBuilder.append(", url='");
        stringBuilder.append(this.url);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
