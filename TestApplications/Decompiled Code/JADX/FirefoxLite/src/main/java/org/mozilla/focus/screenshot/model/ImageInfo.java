package org.mozilla.focus.screenshot.model;

public class ImageInfo {
    public String title;

    public ImageInfo(String str) {
        this.title = str;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ImageInfo{title='");
        stringBuilder.append(this.title);
        stringBuilder.append('\'');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
