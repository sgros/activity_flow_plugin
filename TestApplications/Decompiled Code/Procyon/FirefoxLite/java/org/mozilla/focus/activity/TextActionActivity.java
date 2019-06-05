// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.activity;

import android.net.Uri;
import android.text.TextUtils;
import android.content.Intent;
import android.content.Context;
import org.mozilla.focus.utils.SearchUtils;
import org.mozilla.rocket.component.LaunchIntentDispatcher;
import org.mozilla.focus.utils.SafeIntent;
import android.os.Bundle;
import android.app.Activity;

public class TextActionActivity extends Activity
{
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        final SafeIntent safeIntent = new SafeIntent(this.getIntent());
        final String action = safeIntent.getAction();
        final int hashCode = action.hashCode();
        int n = 0;
        Label_0072: {
            if (hashCode != 1703997026) {
                if (hashCode == 1937529752) {
                    if (action.equals("android.intent.action.WEB_SEARCH")) {
                        n = 1;
                        break Label_0072;
                    }
                }
            }
            else if (action.equals("android.intent.action.PROCESS_TEXT")) {
                n = 0;
                break Label_0072;
            }
            n = -1;
        }
        CharSequence charSequence = null;
        String s = null;
        switch (n) {
            default: {
                charSequence = "";
                s = null;
                break;
            }
            case 1: {
                charSequence = safeIntent.getStringExtra("query");
                s = LaunchIntentDispatcher.LaunchMethod.EXTRA_BOOL_WEB_SEARCH.getValue();
                break;
            }
            case 0: {
                charSequence = safeIntent.getCharSequenceExtra("android.intent.extra.PROCESS_TEXT");
                s = LaunchIntentDispatcher.LaunchMethod.EXTRA_BOOL_TEXT_SELECTION.getValue();
                break;
            }
        }
        final String searchUrl = SearchUtils.createSearchUrl((Context)this, charSequence.toString());
        final Intent intent = new Intent();
        intent.setClassName((Context)this, "org.mozilla.rocket.activity.MainActivity");
        intent.setAction("android.intent.action.VIEW");
        if (!TextUtils.isEmpty((CharSequence)s)) {
            intent.putExtra(s, true);
        }
        intent.setData(Uri.parse(searchUrl));
        this.startActivity(intent);
        this.finish();
    }
}
