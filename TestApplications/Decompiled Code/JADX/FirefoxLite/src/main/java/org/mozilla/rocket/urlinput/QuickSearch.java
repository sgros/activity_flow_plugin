package org.mozilla.rocket.urlinput;

import android.net.Uri;
import java.util.Arrays;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.text.Regex;

/* compiled from: QuickSearch.kt */
public final class QuickSearch {
    private String homeUrl;
    private String icon;
    private String name;
    private boolean patternEncode;
    private boolean permitSpace;
    private String searchUrlPattern;
    private String urlPrefix;
    private String urlSuffix;

    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof QuickSearch) {
                QuickSearch quickSearch = (QuickSearch) obj;
                if (Intrinsics.areEqual(this.name, quickSearch.name) && Intrinsics.areEqual(this.icon, quickSearch.icon) && Intrinsics.areEqual(this.searchUrlPattern, quickSearch.searchUrlPattern) && Intrinsics.areEqual(this.homeUrl, quickSearch.homeUrl) && Intrinsics.areEqual(this.urlPrefix, quickSearch.urlPrefix) && Intrinsics.areEqual(this.urlSuffix, quickSearch.urlSuffix)) {
                    if ((this.patternEncode == quickSearch.patternEncode ? 1 : null) != null) {
                        if ((this.permitSpace == quickSearch.permitSpace ? 1 : null) != null) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        String str = this.name;
        int i = 0;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.icon;
        hashCode = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        str2 = this.searchUrlPattern;
        hashCode = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        str2 = this.homeUrl;
        hashCode = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        str2 = this.urlPrefix;
        hashCode = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        str2 = this.urlSuffix;
        if (str2 != null) {
            i = str2.hashCode();
        }
        hashCode = (hashCode + i) * 31;
        i = this.patternEncode;
        if (i != 0) {
            i = 1;
        }
        hashCode = (hashCode + i) * 31;
        i = this.permitSpace;
        if (i != 0) {
            i = 1;
        }
        return hashCode + i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("QuickSearch(name=");
        stringBuilder.append(this.name);
        stringBuilder.append(", icon=");
        stringBuilder.append(this.icon);
        stringBuilder.append(", searchUrlPattern=");
        stringBuilder.append(this.searchUrlPattern);
        stringBuilder.append(", homeUrl=");
        stringBuilder.append(this.homeUrl);
        stringBuilder.append(", urlPrefix=");
        stringBuilder.append(this.urlPrefix);
        stringBuilder.append(", urlSuffix=");
        stringBuilder.append(this.urlSuffix);
        stringBuilder.append(", patternEncode=");
        stringBuilder.append(this.patternEncode);
        stringBuilder.append(", permitSpace=");
        stringBuilder.append(this.permitSpace);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public QuickSearch(String str, String str2, String str3, String str4, String str5, String str6, boolean z, boolean z2) {
        Intrinsics.checkParameterIsNotNull(str, "name");
        Intrinsics.checkParameterIsNotNull(str2, "icon");
        Intrinsics.checkParameterIsNotNull(str3, "searchUrlPattern");
        Intrinsics.checkParameterIsNotNull(str4, "homeUrl");
        Intrinsics.checkParameterIsNotNull(str5, "urlPrefix");
        Intrinsics.checkParameterIsNotNull(str6, "urlSuffix");
        this.name = str;
        this.icon = str2;
        this.searchUrlPattern = str3;
        this.homeUrl = str4;
        this.urlPrefix = str5;
        this.urlSuffix = str6;
        this.patternEncode = z;
        this.permitSpace = z2;
    }

    public final String getName() {
        return this.name;
    }

    public final String getIcon() {
        return this.icon;
    }

    public final String getHomeUrl() {
        return this.homeUrl;
    }

    public final String generateLink(String str) {
        Intrinsics.checkParameterIsNotNull(str, "keyword");
        if (!this.permitSpace) {
            str = new Regex("\\s").replace(str, "");
        }
        StringCompanionObject stringCompanionObject;
        Object[] objArr;
        if (this.patternEncode) {
            stringCompanionObject = StringCompanionObject.INSTANCE;
            objArr = new Object[]{str};
            str = String.format(this.searchUrlPattern, Arrays.copyOf(objArr, objArr.length));
            Intrinsics.checkExpressionValueIsNotNull(str, "java.lang.String.format(format, *args)");
            str = Uri.encode(str);
        } else {
            stringCompanionObject = StringCompanionObject.INSTANCE;
            objArr = new Object[]{Uri.encode(str)};
            str = String.format(this.searchUrlPattern, Arrays.copyOf(objArr, objArr.length));
            Intrinsics.checkExpressionValueIsNotNull(str, "java.lang.String.format(format, *args)");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.urlPrefix);
        stringBuilder.append(str);
        stringBuilder.append(this.urlSuffix);
        return stringBuilder.toString();
    }
}
