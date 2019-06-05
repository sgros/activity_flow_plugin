// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.urlinput;

import android.net.Uri;
import java.util.Arrays;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.text.Regex;
import kotlin.jvm.internal.Intrinsics;

public final class QuickSearch
{
    private String homeUrl;
    private String icon;
    private String name;
    private boolean patternEncode;
    private boolean permitSpace;
    private String searchUrlPattern;
    private String urlPrefix;
    private String urlSuffix;
    
    public QuickSearch(final String name, final String icon, final String searchUrlPattern, final String homeUrl, final String urlPrefix, final String urlSuffix, final boolean patternEncode, final boolean permitSpace) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(icon, "icon");
        Intrinsics.checkParameterIsNotNull(searchUrlPattern, "searchUrlPattern");
        Intrinsics.checkParameterIsNotNull(homeUrl, "homeUrl");
        Intrinsics.checkParameterIsNotNull(urlPrefix, "urlPrefix");
        Intrinsics.checkParameterIsNotNull(urlSuffix, "urlSuffix");
        this.name = name;
        this.icon = icon;
        this.searchUrlPattern = searchUrlPattern;
        this.homeUrl = homeUrl;
        this.urlPrefix = urlPrefix;
        this.urlSuffix = urlSuffix;
        this.patternEncode = patternEncode;
        this.permitSpace = permitSpace;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o instanceof QuickSearch) {
                final QuickSearch quickSearch = (QuickSearch)o;
                if (Intrinsics.areEqual(this.name, quickSearch.name) && Intrinsics.areEqual(this.icon, quickSearch.icon) && Intrinsics.areEqual(this.searchUrlPattern, quickSearch.searchUrlPattern) && Intrinsics.areEqual(this.homeUrl, quickSearch.homeUrl) && Intrinsics.areEqual(this.urlPrefix, quickSearch.urlPrefix) && Intrinsics.areEqual(this.urlSuffix, quickSearch.urlSuffix) && this.patternEncode == quickSearch.patternEncode && this.permitSpace == quickSearch.permitSpace) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public final String generateLink(String str) {
        Intrinsics.checkParameterIsNotNull(str, "keyword");
        String replace = str;
        if (!this.permitSpace) {
            replace = new Regex("\\s").replace(str, "");
        }
        if (this.patternEncode) {
            final StringCompanionObject instance = StringCompanionObject.INSTANCE;
            final String searchUrlPattern = this.searchUrlPattern;
            final Object[] original = { replace };
            str = String.format(searchUrlPattern, Arrays.copyOf(original, original.length));
            Intrinsics.checkExpressionValueIsNotNull(str, "java.lang.String.format(format, *args)");
            str = Uri.encode(str);
        }
        else {
            final StringCompanionObject instance2 = StringCompanionObject.INSTANCE;
            final String searchUrlPattern2 = this.searchUrlPattern;
            final Object[] original2 = { Uri.encode(replace) };
            str = String.format(searchUrlPattern2, Arrays.copyOf(original2, original2.length));
            Intrinsics.checkExpressionValueIsNotNull(str, "java.lang.String.format(format, *args)");
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(this.urlPrefix);
        sb.append(str);
        sb.append(this.urlSuffix);
        return sb.toString();
    }
    
    public final String getHomeUrl() {
        return this.homeUrl;
    }
    
    public final String getIcon() {
        return this.icon;
    }
    
    public final String getName() {
        return this.name;
    }
    
    @Override
    public int hashCode() {
        final String name = this.name;
        int hashCode = 0;
        int hashCode2;
        if (name != null) {
            hashCode2 = name.hashCode();
        }
        else {
            hashCode2 = 0;
        }
        final String icon = this.icon;
        int hashCode3;
        if (icon != null) {
            hashCode3 = icon.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        final String searchUrlPattern = this.searchUrlPattern;
        int hashCode4;
        if (searchUrlPattern != null) {
            hashCode4 = searchUrlPattern.hashCode();
        }
        else {
            hashCode4 = 0;
        }
        final String homeUrl = this.homeUrl;
        int hashCode5;
        if (homeUrl != null) {
            hashCode5 = homeUrl.hashCode();
        }
        else {
            hashCode5 = 0;
        }
        final String urlPrefix = this.urlPrefix;
        int hashCode6;
        if (urlPrefix != null) {
            hashCode6 = urlPrefix.hashCode();
        }
        else {
            hashCode6 = 0;
        }
        final String urlSuffix = this.urlSuffix;
        if (urlSuffix != null) {
            hashCode = urlSuffix.hashCode();
        }
        int patternEncode;
        if ((patternEncode = (this.patternEncode ? 1 : 0)) != 0) {
            patternEncode = 1;
        }
        int permitSpace;
        if ((permitSpace = (this.permitSpace ? 1 : 0)) != 0) {
            permitSpace = 1;
        }
        return ((((((hashCode2 * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode6) * 31 + hashCode) * 31 + patternEncode) * 31 + permitSpace;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("QuickSearch(name=");
        sb.append(this.name);
        sb.append(", icon=");
        sb.append(this.icon);
        sb.append(", searchUrlPattern=");
        sb.append(this.searchUrlPattern);
        sb.append(", homeUrl=");
        sb.append(this.homeUrl);
        sb.append(", urlPrefix=");
        sb.append(this.urlPrefix);
        sb.append(", urlSuffix=");
        sb.append(this.urlSuffix);
        sb.append(", patternEncode=");
        sb.append(this.patternEncode);
        sb.append(", permitSpace=");
        sb.append(this.permitSpace);
        sb.append(")");
        return sb.toString();
    }
}
