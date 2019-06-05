package org.mozilla.focus.search;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.adjust.sdk.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchEngine {
    Bitmap icon;
    private final String identifier;
    String name;
    List<Uri> resultsUris = new ArrayList();
    Uri suggestUri;

    SearchEngine(String str) {
        this.identifier = str;
    }

    public String getName() {
        return this.name;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public Bitmap getIcon() {
        return this.icon;
    }

    public String buildSearchUrl(String str) {
        if (this.resultsUris.isEmpty()) {
            return str;
        }
        return paramSubstitution(Uri.decode(((Uri) this.resultsUris.get(0)).toString()), Uri.encode(str));
    }

    public String buildSearchSuggestionUrl(String str) {
        if (this.suggestUri == null) {
            return null;
        }
        return paramSubstitution(Uri.decode(this.suggestUri.toString()), Uri.encode(str));
    }

    private String paramSubstitution(String str, String str2) {
        String country;
        if (VERSION.SDK_INT >= 24) {
            country = Locale.getDefault().getCountry();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Locale.getDefault().getLanguage());
            if (TextUtils.isEmpty(country)) {
                country = "";
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("_");
                stringBuilder2.append(country);
                country = stringBuilder2.toString();
            }
            stringBuilder.append(country);
            country = stringBuilder.toString();
        } else {
            country = Locale.getDefault().toString();
        }
        return str.replaceAll("\\{moz:locale\\}", country).replaceAll("\\{moz:distributionID\\}", "").replaceAll("\\{moz:official\\}", "unofficial").replaceAll("\\{searchTerms\\??\\}", str2).replaceAll("\\{inputEncoding\\??\\}", Constants.ENCODING).replaceAll("\\{language\\??\\}", country).replaceAll("\\{outputEncoding\\??\\}", Constants.ENCODING).replaceAll("\\{(?:\\w+:)?\\w+\\?\\}", "");
    }
}
