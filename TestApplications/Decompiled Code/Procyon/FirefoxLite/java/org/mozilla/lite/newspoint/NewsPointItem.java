// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.lite.newspoint;

import kotlin.jvm.internal.Intrinsics;
import java.util.List;
import org.mozilla.lite.partner.NewsItem;

public final class NewsPointItem implements NewsItem
{
    private final String category;
    private final String dm;
    private final String fu;
    private final String id;
    private final String imageUrl;
    private final String imageid;
    private final String lang;
    private final Long lid;
    private final String m;
    private final String newsUrl;
    private final String partner;
    private final Long pid;
    private final String pnu;
    private final String source;
    private final String subcategory;
    private final List<String> tags;
    private final long time;
    private final String title;
    private final String wu;
    
    public NewsPointItem(final String id, final String imageUrl, final String title, final String newsUrl, final long time, final String imageid, final String partner, final String dm, final Long pid, final Long lid, final String lang, final String category, final String wu, final String pnu, final String fu, final String subcategory, final String m, final List<String> tags) {
        Intrinsics.checkParameterIsNotNull(id, "id");
        Intrinsics.checkParameterIsNotNull(title, "title");
        Intrinsics.checkParameterIsNotNull(newsUrl, "newsUrl");
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.newsUrl = newsUrl;
        this.time = time;
        this.imageid = imageid;
        this.partner = partner;
        this.dm = dm;
        this.pid = pid;
        this.lid = lid;
        this.lang = lang;
        this.category = category;
        this.wu = wu;
        this.pnu = pnu;
        this.fu = fu;
        this.subcategory = subcategory;
        this.m = m;
        this.tags = tags;
        this.source = "Newspoint";
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o instanceof NewsPointItem) {
                final NewsPointItem newsPointItem = (NewsPointItem)o;
                if (Intrinsics.areEqual(this.getId(), newsPointItem.getId()) && Intrinsics.areEqual(this.getImageUrl(), newsPointItem.getImageUrl()) && Intrinsics.areEqual(this.getTitle(), newsPointItem.getTitle()) && Intrinsics.areEqual(this.getNewsUrl(), newsPointItem.getNewsUrl()) && this.getTime() == newsPointItem.getTime() && Intrinsics.areEqual(this.imageid, newsPointItem.imageid) && Intrinsics.areEqual(this.getPartner(), newsPointItem.getPartner()) && Intrinsics.areEqual(this.dm, newsPointItem.dm) && Intrinsics.areEqual(this.pid, newsPointItem.pid) && Intrinsics.areEqual(this.lid, newsPointItem.lid) && Intrinsics.areEqual(this.lang, newsPointItem.lang) && Intrinsics.areEqual(this.getCategory(), newsPointItem.getCategory()) && Intrinsics.areEqual(this.wu, newsPointItem.wu) && Intrinsics.areEqual(this.pnu, newsPointItem.pnu) && Intrinsics.areEqual(this.fu, newsPointItem.fu) && Intrinsics.areEqual(this.getSubcategory(), newsPointItem.getSubcategory()) && Intrinsics.areEqual(this.m, newsPointItem.m) && Intrinsics.areEqual(this.tags, newsPointItem.tags)) {
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
        final String imageid = this.imageid;
        int hashCode6;
        if (imageid != null) {
            hashCode6 = imageid.hashCode();
        }
        else {
            hashCode6 = 0;
        }
        final String partner = this.getPartner();
        int hashCode7;
        if (partner != null) {
            hashCode7 = partner.hashCode();
        }
        else {
            hashCode7 = 0;
        }
        final String dm = this.dm;
        int hashCode8;
        if (dm != null) {
            hashCode8 = dm.hashCode();
        }
        else {
            hashCode8 = 0;
        }
        final Long pid = this.pid;
        int hashCode9;
        if (pid != null) {
            hashCode9 = pid.hashCode();
        }
        else {
            hashCode9 = 0;
        }
        final Long lid = this.lid;
        int hashCode10;
        if (lid != null) {
            hashCode10 = lid.hashCode();
        }
        else {
            hashCode10 = 0;
        }
        final String lang = this.lang;
        int hashCode11;
        if (lang != null) {
            hashCode11 = lang.hashCode();
        }
        else {
            hashCode11 = 0;
        }
        final String category = this.getCategory();
        int hashCode12;
        if (category != null) {
            hashCode12 = category.hashCode();
        }
        else {
            hashCode12 = 0;
        }
        final String wu = this.wu;
        int hashCode13;
        if (wu != null) {
            hashCode13 = wu.hashCode();
        }
        else {
            hashCode13 = 0;
        }
        final String pnu = this.pnu;
        int hashCode14;
        if (pnu != null) {
            hashCode14 = pnu.hashCode();
        }
        else {
            hashCode14 = 0;
        }
        final String fu = this.fu;
        int hashCode15;
        if (fu != null) {
            hashCode15 = fu.hashCode();
        }
        else {
            hashCode15 = 0;
        }
        final String subcategory = this.getSubcategory();
        int hashCode16;
        if (subcategory != null) {
            hashCode16 = subcategory.hashCode();
        }
        else {
            hashCode16 = 0;
        }
        final String m = this.m;
        int hashCode17;
        if (m != null) {
            hashCode17 = m.hashCode();
        }
        else {
            hashCode17 = 0;
        }
        final List<String> tags = this.tags;
        if (tags != null) {
            hashCode = tags.hashCode();
        }
        return ((((((((((((((((hashCode2 * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + n) * 31 + hashCode6) * 31 + hashCode7) * 31 + hashCode8) * 31 + hashCode9) * 31 + hashCode10) * 31 + hashCode11) * 31 + hashCode12) * 31 + hashCode13) * 31 + hashCode14) * 31 + hashCode15) * 31 + hashCode16) * 31 + hashCode17) * 31 + hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("NewsPointItem(id=");
        sb.append(this.getId());
        sb.append(", imageUrl=");
        sb.append(this.getImageUrl());
        sb.append(", title=");
        sb.append(this.getTitle());
        sb.append(", newsUrl=");
        sb.append(this.getNewsUrl());
        sb.append(", time=");
        sb.append(this.getTime());
        sb.append(", imageid=");
        sb.append(this.imageid);
        sb.append(", partner=");
        sb.append(this.getPartner());
        sb.append(", dm=");
        sb.append(this.dm);
        sb.append(", pid=");
        sb.append(this.pid);
        sb.append(", lid=");
        sb.append(this.lid);
        sb.append(", lang=");
        sb.append(this.lang);
        sb.append(", category=");
        sb.append(this.getCategory());
        sb.append(", wu=");
        sb.append(this.wu);
        sb.append(", pnu=");
        sb.append(this.pnu);
        sb.append(", fu=");
        sb.append(this.fu);
        sb.append(", subcategory=");
        sb.append(this.getSubcategory());
        sb.append(", m=");
        sb.append(this.m);
        sb.append(", tags=");
        sb.append(this.tags);
        sb.append(")");
        return sb.toString();
    }
}
