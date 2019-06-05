package org.mozilla.focus.webkit;

import android.content.res.Resources;
import android.support.p001v4.util.ArrayMap;
import android.webkit.WebView;
import org.mozilla.focus.utils.HtmlLoader;
import org.mozilla.rocket.C0769R;

public class ErrorPage {
    public static boolean supportsErrorCode(int i) {
        return true;
    }

    public static void loadErrorPage(WebView webView, String str, int i) {
        String loadResourceFile = HtmlLoader.loadResourceFile(webView.getContext(), C0769R.raw.errorpage_style, null);
        ArrayMap arrayMap = new ArrayMap();
        Resources resources = webView.getContext().getResources();
        String string;
        if (i == -10) {
            string = resources.getString(C0769R.string.error_page_content_text_unsupported_scheme);
            arrayMap.put("%pageTitle%", resources.getString(C0769R.string.error_page_title_unsupported_scheme));
            arrayMap.put("%messageShort%", resources.getString(C0769R.string.error_page_title_unsupported_scheme));
            arrayMap.put("%messageLong%", resources.getString(C0769R.string.error_page_message, new Object[]{string, ""}));
        } else {
            string = resources.getString(C0769R.string.error_page_recover_a);
            String string2 = resources.getString(C0769R.string.error_page_recover_b);
            arrayMap.put("%pageTitle%", resources.getString(C0769R.string.error_page_title));
            arrayMap.put("%messageShort%", resources.getString(C0769R.string.error_page_title));
            arrayMap.put("%messageLong%", resources.getString(C0769R.string.error_page_message, new Object[]{string, string2}));
        }
        arrayMap.put("%button%", resources.getString(C0769R.string.error_page_button));
        arrayMap.put("%css%", loadResourceFile);
        arrayMap.put("%imageContentInBase64%", HtmlLoader.loadPngAsDataURI(webView.getContext(), 2131230856));
        String loadResourceFile2 = HtmlLoader.loadResourceFile(webView.getContext(), C0769R.raw.errorpage, arrayMap);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.loadDataWithBaseURL(str, loadResourceFile2, "text/html", "UTF8", str);
    }
}
