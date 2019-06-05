// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.search;

import android.text.TextUtils;
import java.util.Locale;
import android.os.Build$VERSION;
import java.util.ArrayList;
import android.net.Uri;
import java.util.List;
import android.graphics.Bitmap;

public class SearchEngine
{
    Bitmap icon;
    private final String identifier;
    String name;
    List<Uri> resultsUris;
    Uri suggestUri;
    
    SearchEngine(final String identifier) {
        this.identifier = identifier;
        this.resultsUris = new ArrayList<Uri>();
    }
    
    private String paramSubstitution(final String s, final String replacement) {
        String s2;
        if (Build$VERSION.SDK_INT >= 24) {
            final String country = Locale.getDefault().getCountry();
            final StringBuilder sb = new StringBuilder();
            sb.append(Locale.getDefault().getLanguage());
            String string;
            if (TextUtils.isEmpty((CharSequence)country)) {
                string = "";
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("_");
                sb2.append(country);
                string = sb2.toString();
            }
            sb.append(string);
            s2 = sb.toString();
        }
        else {
            s2 = Locale.getDefault().toString();
        }
        return s.replaceAll("\\{moz:locale\\}", s2).replaceAll("\\{moz:distributionID\\}", "").replaceAll("\\{moz:official\\}", "unofficial").replaceAll("\\{searchTerms\\??\\}", replacement).replaceAll("\\{inputEncoding\\??\\}", "UTF-8").replaceAll("\\{language\\??\\}", s2).replaceAll("\\{outputEncoding\\??\\}", "UTF-8").replaceAll("\\{(?:\\w+:)?\\w+\\?\\}", "");
    }
    
    public String buildSearchSuggestionUrl(final String s) {
        if (this.suggestUri == null) {
            return null;
        }
        return this.paramSubstitution(Uri.decode(this.suggestUri.toString()), Uri.encode(s));
    }
    
    public String buildSearchUrl(final String s) {
        if (this.resultsUris.isEmpty()) {
            return s;
        }
        return this.paramSubstitution(Uri.decode(this.resultsUris.get(0).toString()), Uri.encode(s));
    }
    
    public Bitmap getIcon() {
        return this.icon;
    }
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    public String getName() {
        return this.name;
    }
}
