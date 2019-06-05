// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.webkit;

import android.content.res.Resources;
import android.support.v4.util.ArrayMap;
import java.util.Map;
import org.mozilla.focus.utils.HtmlLoader;
import android.webkit.WebView;

public class ErrorPage
{
    public static void loadErrorPage(final WebView webView, final String s, final int n) {
        final String loadResourceFile = HtmlLoader.loadResourceFile(webView.getContext(), 2131689479, null);
        final ArrayMap<String, String> arrayMap = new ArrayMap<String, String>();
        final Resources resources = webView.getContext().getResources();
        if (n == -10) {
            final String string = resources.getString(2131755142);
            arrayMap.put("%pageTitle%", resources.getString(2131755147));
            arrayMap.put("%messageShort%", resources.getString(2131755147));
            arrayMap.put("%messageLong%", resources.getString(2131755143, new Object[] { string, "" }));
        }
        else {
            final String string2 = resources.getString(2131755144);
            final String string3 = resources.getString(2131755145);
            arrayMap.put("%pageTitle%", resources.getString(2131755146));
            arrayMap.put("%messageShort%", resources.getString(2131755146));
            arrayMap.put("%messageLong%", resources.getString(2131755143, new Object[] { string2, string3 }));
        }
        arrayMap.put("%button%", resources.getString(2131755141));
        arrayMap.put("%css%", loadResourceFile);
        arrayMap.put("%imageContentInBase64%", HtmlLoader.loadPngAsDataURI(webView.getContext(), 2131230856));
        final String loadResourceFile2 = HtmlLoader.loadResourceFile(webView.getContext(), 2131689478, arrayMap);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.loadDataWithBaseURL(s, loadResourceFile2, "text/html", "UTF8", s);
    }
    
    public static boolean supportsErrorCode(final int n) {
        return true;
    }
}
