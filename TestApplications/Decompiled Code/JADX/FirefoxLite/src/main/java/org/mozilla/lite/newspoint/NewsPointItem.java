package org.mozilla.lite.newspoint;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.lite.partner.NewsItem;

/* compiled from: NewsPointItem.kt */
public final class NewsPointItem implements NewsItem {
    private final String category;
    /* renamed from: dm */
    private final String f63dm;
    /* renamed from: fu */
    private final String f64fu;
    /* renamed from: id */
    private final String f65id;
    private final String imageUrl;
    private final String imageid;
    private final String lang;
    private final Long lid;
    /* renamed from: m */
    private final String f66m;
    private final String newsUrl;
    private final String partner;
    private final Long pid;
    private final String pnu;
    private final String source = "Newspoint";
    private final String subcategory;
    private final List<String> tags;
    private final long time;
    private final String title;
    /* renamed from: wu */
    private final String f67wu;

    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof NewsPointItem) {
                NewsPointItem newsPointItem = (NewsPointItem) obj;
                if (Intrinsics.areEqual(getId(), newsPointItem.getId()) && Intrinsics.areEqual(getImageUrl(), newsPointItem.getImageUrl()) && Intrinsics.areEqual(getTitle(), newsPointItem.getTitle()) && Intrinsics.areEqual(getNewsUrl(), newsPointItem.getNewsUrl())) {
                    if (!((getTime() == newsPointItem.getTime() ? 1 : null) != null && Intrinsics.areEqual(this.imageid, newsPointItem.imageid) && Intrinsics.areEqual(getPartner(), newsPointItem.getPartner()) && Intrinsics.areEqual(this.f63dm, newsPointItem.f63dm) && Intrinsics.areEqual(this.pid, newsPointItem.pid) && Intrinsics.areEqual(this.lid, newsPointItem.lid) && Intrinsics.areEqual(this.lang, newsPointItem.lang) && Intrinsics.areEqual(getCategory(), newsPointItem.getCategory()) && Intrinsics.areEqual(this.f67wu, newsPointItem.f67wu) && Intrinsics.areEqual(this.pnu, newsPointItem.pnu) && Intrinsics.areEqual(this.f64fu, newsPointItem.f64fu) && Intrinsics.areEqual(getSubcategory(), newsPointItem.getSubcategory()) && Intrinsics.areEqual(this.f66m, newsPointItem.f66m) && Intrinsics.areEqual(this.tags, newsPointItem.tags))) {
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
        imageUrl = this.imageid;
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        imageUrl = getPartner();
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        imageUrl = this.f63dm;
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        Long l = this.pid;
        hashCode = (hashCode + (l != null ? l.hashCode() : 0)) * 31;
        l = this.lid;
        hashCode = (hashCode + (l != null ? l.hashCode() : 0)) * 31;
        imageUrl = this.lang;
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        imageUrl = getCategory();
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        imageUrl = this.f67wu;
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        imageUrl = this.pnu;
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        imageUrl = this.f64fu;
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        imageUrl = getSubcategory();
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        imageUrl = this.f66m;
        hashCode = (hashCode + (imageUrl != null ? imageUrl.hashCode() : 0)) * 31;
        List list = this.tags;
        if (list != null) {
            i = list.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("NewsPointItem(id=");
        stringBuilder.append(getId());
        stringBuilder.append(", imageUrl=");
        stringBuilder.append(getImageUrl());
        stringBuilder.append(", title=");
        stringBuilder.append(getTitle());
        stringBuilder.append(", newsUrl=");
        stringBuilder.append(getNewsUrl());
        stringBuilder.append(", time=");
        stringBuilder.append(getTime());
        stringBuilder.append(", imageid=");
        stringBuilder.append(this.imageid);
        stringBuilder.append(", partner=");
        stringBuilder.append(getPartner());
        stringBuilder.append(", dm=");
        stringBuilder.append(this.f63dm);
        stringBuilder.append(", pid=");
        stringBuilder.append(this.pid);
        stringBuilder.append(", lid=");
        stringBuilder.append(this.lid);
        stringBuilder.append(", lang=");
        stringBuilder.append(this.lang);
        stringBuilder.append(", category=");
        stringBuilder.append(getCategory());
        stringBuilder.append(", wu=");
        stringBuilder.append(this.f67wu);
        stringBuilder.append(", pnu=");
        stringBuilder.append(this.pnu);
        stringBuilder.append(", fu=");
        stringBuilder.append(this.f64fu);
        stringBuilder.append(", subcategory=");
        stringBuilder.append(getSubcategory());
        stringBuilder.append(", m=");
        stringBuilder.append(this.f66m);
        stringBuilder.append(", tags=");
        stringBuilder.append(this.tags);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public NewsPointItem(String str, String str2, String str3, String str4, long j, String str5, String str6, String str7, Long l, Long l2, String str8, String str9, String str10, String str11, String str12, String str13, String str14, List<String> list) {
        String str15 = str;
        String str16 = str3;
        String str17 = str4;
        Intrinsics.checkParameterIsNotNull(str, "id");
        Intrinsics.checkParameterIsNotNull(str3, "title");
        Intrinsics.checkParameterIsNotNull(str4, "newsUrl");
        this.f65id = str15;
        this.imageUrl = str2;
        this.title = str16;
        this.newsUrl = str17;
        this.time = j;
        this.imageid = str5;
        this.partner = str6;
        this.f63dm = str7;
        this.pid = l;
        this.lid = l2;
        this.lang = str8;
        this.category = str9;
        this.f67wu = str10;
        this.pnu = str11;
        this.f64fu = str12;
        this.subcategory = str13;
        this.f66m = str14;
        this.tags = list;
    }

    public String getId() {
        return this.f65id;
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

    public String getPartner() {
        return this.partner;
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
}
