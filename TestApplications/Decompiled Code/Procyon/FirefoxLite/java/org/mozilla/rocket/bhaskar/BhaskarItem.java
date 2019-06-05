// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.bhaskar;

import kotlin.jvm.internal.Intrinsics;
import java.util.List;
import org.mozilla.lite.partner.NewsItem;

public final class BhaskarItem implements NewsItem
{
    private final String articleFrom;
    private final String category;
    private final String city;
    private final String description;
    private final String id;
    private final String imageUrl;
    private final String keywords;
    private final String language;
    private final String newsUrl;
    private final String partner;
    private final String province;
    private final String source;
    private final String subcategory;
    private final String summary;
    private final List<String> tags;
    private final long time;
    private final String title;
    
    public BhaskarItem(final String id, final String imageUrl, final String title, final String newsUrl, final long time, final String summary, final String language, final String category, final String subcategory, final String keywords, final String description, final List<String> tags, final String articleFrom, final String province, final String city) {
        Intrinsics.checkParameterIsNotNull(id, "id");
        Intrinsics.checkParameterIsNotNull(title, "title");
        Intrinsics.checkParameterIsNotNull(newsUrl, "newsUrl");
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.newsUrl = newsUrl;
        this.time = time;
        this.summary = summary;
        this.language = language;
        this.category = category;
        this.subcategory = subcategory;
        this.keywords = keywords;
        this.description = description;
        this.tags = tags;
        this.articleFrom = articleFrom;
        this.province = province;
        this.city = city;
        this.source = "DainikBhaskar.com";
        this.partner = "DainikBhaskar.com";
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o instanceof BhaskarItem) {
                final BhaskarItem bhaskarItem = (BhaskarItem)o;
                if (Intrinsics.areEqual(this.getId(), bhaskarItem.getId()) && Intrinsics.areEqual(this.getImageUrl(), bhaskarItem.getImageUrl()) && Intrinsics.areEqual(this.getTitle(), bhaskarItem.getTitle()) && Intrinsics.areEqual(this.getNewsUrl(), bhaskarItem.getNewsUrl()) && this.getTime() == bhaskarItem.getTime() && Intrinsics.areEqual(this.summary, bhaskarItem.summary) && Intrinsics.areEqual(this.language, bhaskarItem.language) && Intrinsics.areEqual(this.getCategory(), bhaskarItem.getCategory()) && Intrinsics.areEqual(this.getSubcategory(), bhaskarItem.getSubcategory()) && Intrinsics.areEqual(this.keywords, bhaskarItem.keywords) && Intrinsics.areEqual(this.description, bhaskarItem.description) && Intrinsics.areEqual(this.tags, bhaskarItem.tags) && Intrinsics.areEqual(this.articleFrom, bhaskarItem.articleFrom) && Intrinsics.areEqual(this.province, bhaskarItem.province) && Intrinsics.areEqual(this.city, bhaskarItem.city)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    @Override
    public String getCategory() {
        return this.category;
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public String getImageUrl() {
        return this.imageUrl;
    }
    
    @Override
    public String getNewsUrl() {
        return this.newsUrl;
    }
    
    @Override
    public String getPartner() {
        return this.partner;
    }
    
    @Override
    public String getSource() {
        return this.source;
    }
    
    @Override
    public String getSubcategory() {
        return this.subcategory;
    }
    
    @Override
    public long getTime() {
        return this.time;
    }
    
    @Override
    public String getTitle() {
        return this.title;
    }
    
    @Override
    public int hashCode() {
        final String id = this.getId();
        int hashCode = 0;
        int hashCode2;
        if (id != null) {
            hashCode2 = id.hashCode();
        }
        else {
            hashCode2 = 0;
        }
        final String imageUrl = this.getImageUrl();
        int hashCode3;
        if (imageUrl != null) {
            hashCode3 = imageUrl.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        final String title = this.getTitle();
        int hashCode4;
        if (title != null) {
            hashCode4 = title.hashCode();
        }
        else {
            hashCode4 = 0;
        }
        final String newsUrl = this.getNewsUrl();
        int hashCode5;
        if (newsUrl != null) {
            hashCode5 = newsUrl.hashCode();
        }
        else {
            hashCode5 = 0;
        }
        final long time = this.getTime();
        final int n = (int)(time ^ time >>> 32);
        final String summary = this.summary;
        int hashCode6;
        if (summary != null) {
            hashCode6 = summary.hashCode();
        }
        else {
            hashCode6 = 0;
        }
        final String language = this.language;
        int hashCode7;
        if (language != null) {
            hashCode7 = language.hashCode();
        }
        else {
            hashCode7 = 0;
        }
        final String category = this.getCategory();
        int hashCode8;
        if (category != null) {
            hashCode8 = category.hashCode();
        }
        else {
            hashCode8 = 0;
        }
        final String subcategory = this.getSubcategory();
        int hashCode9;
        if (subcategory != null) {
            hashCode9 = subcategory.hashCode();
        }
        else {
            hashCode9 = 0;
        }
        final String keywords = this.keywords;
        int hashCode10;
        if (keywords != null) {
            hashCode10 = keywords.hashCode();
        }
        else {
            hashCode10 = 0;
        }
        final String description = this.description;
        int hashCode11;
        if (description != null) {
            hashCode11 = description.hashCode();
        }
        else {
            hashCode11 = 0;
        }
        final List<String> tags = this.tags;
        int hashCode12;
        if (tags != null) {
            hashCode12 = tags.hashCode();
        }
        else {
            hashCode12 = 0;
        }
        final String articleFrom = this.articleFrom;
        int hashCode13;
        if (articleFrom != null) {
            hashCode13 = articleFrom.hashCode();
        }
        else {
            hashCode13 = 0;
        }
        final String province = this.province;
        int hashCode14;
        if (province != null) {
            hashCode14 = province.hashCode();
        }
        else {
            hashCode14 = 0;
        }
        final String city = this.city;
        if (city != null) {
            hashCode = city.hashCode();
        }
        return (((((((((((((hashCode2 * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + n) * 31 + hashCode6) * 31 + hashCode7) * 31 + hashCode8) * 31 + hashCode9) * 31 + hashCode10) * 31 + hashCode11) * 31 + hashCode12) * 31 + hashCode13) * 31 + hashCode14) * 31 + hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BhaskarItem(id=");
        sb.append(this.getId());
        sb.append(", imageUrl=");
        sb.append(this.getImageUrl());
        sb.append(", title=");
        sb.append(this.getTitle());
        sb.append(", newsUrl=");
        sb.append(this.getNewsUrl());
        sb.append(", time=");
        sb.append(this.getTime());
        sb.append(", summary=");
        sb.append(this.summary);
        sb.append(", language=");
        sb.append(this.language);
        sb.append(", category=");
        sb.append(this.getCategory());
        sb.append(", subcategory=");
        sb.append(this.getSubcategory());
        sb.append(", keywords=");
        sb.append(this.keywords);
        sb.append(", description=");
        sb.append(this.description);
        sb.append(", tags=");
        sb.append(this.tags);
        sb.append(", articleFrom=");
        sb.append(this.articleFrom);
        sb.append(", province=");
        sb.append(this.province);
        sb.append(", city=");
        sb.append(this.city);
        sb.append(")");
        return sb.toString();
    }
}
