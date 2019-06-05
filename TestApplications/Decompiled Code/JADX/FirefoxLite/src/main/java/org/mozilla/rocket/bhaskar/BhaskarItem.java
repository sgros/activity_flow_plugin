package org.mozilla.rocket.bhaskar;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.lite.partner.NewsItem;

/* compiled from: BhaskarItem.kt */
public final class BhaskarItem implements NewsItem {
    private final String articleFrom;
    private final String category;
    private final String city;
    private final String description;
    /* renamed from: id */
    private final String f69id;
    private final String imageUrl;
    private final String keywords;
    private final String language;
    private final String newsUrl;
    private final String partner = "DainikBhaskar.com";
    private final String province;
    private final String source = "DainikBhaskar.com";
    private final String subcategory;
    private final String summary;
    private final List<String> tags;
    private final long time;
    private final String title;

    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof BhaskarItem) {
                BhaskarItem bhaskarItem = (BhaskarItem) obj;
                if (Intrinsics.areEqual(getId(), bhaskarItem.getId()) && Intrinsics.areEqual(getImageUrl(), bhaskarItem.getImageUrl()) && Intrinsics.areEqual(getTitle(), bhaskarItem.getTitle()) && Intrinsics.areEqual(getNewsUrl(), bhaskarItem.getNewsUrl())) {
                    if (!((getTime() == bhaskarItem.getTime() ? 1 : null) != null && Intrinsics.areEqual(this.summary, bhaskarItem.summary) && Intrinsics.areEqual(this.language, bhaskarItem.language) && Intrinsics.areEqual(getCategory(), bhaskarItem.getCategory()) && Intrinsics.areEqual(getSubcategory(), bhaskarItem.getSubcategory()) && Intrinsics.areEqual(this.keywords, bhaskarItem.keywords) && Intrinsics.areEqual(this.description, bhaskarItem.description) && Intrinsics.areEqual(this.tags, bhaskarItem.tags) && Intrinsics.areEqual(this.articleFrom, bhaskarItem.articleFrom) && Intrinsics.areEqual(this.province, bhaskarItem.province) && Intrinsics.areEqual(this.city, bhaskarItem.city))) {
                        return false;
                    }
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        String id = getId();
        int i = 0;
        int hashCode = (id != null ? id.hashCode() : 0) * 31;
        String imageUrl = getImageUrl();
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        imageUrl = getTitle();
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        imageUrl = getNewsUrl();
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        long time = getTime();
        hashCode = (hashCode + ((int) (time ^ (time >>> 32)))) * 31;
        imageUrl = this.summary;
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        imageUrl = this.language;
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        imageUrl = getCategory();
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        imageUrl = getSubcategory();
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        imageUrl = this.keywords;
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        imageUrl = this.description;
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        List list = this.tags;
        hashCode = (hashCode + (list != null ? list.hashCode() : 0)) * 31;
        imageUrl = this.articleFrom;
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        imageUrl = this.province;
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        imageUrl = this.city;
        if (imageUrl != null) {
            i = imageUrl.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("BhaskarItem(id=");
        stringBuilder.append(getId());
        stringBuilder.append(", imageUrl=");
        stringBuilder.append(getImageUrl());
        stringBuilder.append(", title=");
        stringBuilder.append(getTitle());
        stringBuilder.append(", newsUrl=");
        stringBuilder.append(getNewsUrl());
        stringBuilder.append(", time=");
        stringBuilder.append(getTime());
        stringBuilder.append(", summary=");
        stringBuilder.append(this.summary);
        stringBuilder.append(", language=");
        stringBuilder.append(this.language);
        stringBuilder.append(", category=");
        stringBuilder.append(getCategory());
        stringBuilder.append(", subcategory=");
        stringBuilder.append(getSubcategory());
        stringBuilder.append(", keywords=");
        stringBuilder.append(this.keywords);
        stringBuilder.append(", description=");
        stringBuilder.append(this.description);
        stringBuilder.append(", tags=");
        stringBuilder.append(this.tags);
        stringBuilder.append(", articleFrom=");
        stringBuilder.append(this.articleFrom);
        stringBuilder.append(", province=");
        stringBuilder.append(this.province);
        stringBuilder.append(", city=");
        stringBuilder.append(this.city);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public BhaskarItem(String str, String str2, String str3, String str4, long j, String str5, String str6, String str7, String str8, String str9, String str10, List<String> list, String str11, String str12, String str13) {
        String str14 = str;
        String str15 = str3;
        String str16 = str4;
        Intrinsics.checkParameterIsNotNull(str, "id");
        Intrinsics.checkParameterIsNotNull(str3, "title");
        Intrinsics.checkParameterIsNotNull(str4, "newsUrl");
        this.f69id = str14;
        this.imageUrl = str2;
        this.title = str15;
        this.newsUrl = str16;
        this.time = j;
        this.summary = str5;
        this.language = str6;
        this.category = str7;
        this.subcategory = str8;
        this.keywords = str9;
        this.description = str10;
        this.tags = list;
        this.articleFrom = str11;
        this.province = str12;
        this.city = str13;
    }

    public String getId() {
        return this.f69id;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public String getTitle() {
        return this.title;
    }

    public String getNewsUrl() {
        return this.newsUrl;
    }

    public long getTime() {
        return this.time;
    }

    public String getCategory() {
        return this.category;
    }

    public String getSubcategory() {
        return this.subcategory;
    }

    public String getSource() {
        return this.source;
    }

    public String getPartner() {
        return this.partner;
    }
}
