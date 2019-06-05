// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.widget;

import android.util.Log;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.rocket.content.NewsSourceManager;
import org.mozilla.focus.utils.Settings;
import android.util.AttributeSet;
import android.content.Context;
import android.preference.ListPreference;

public class NewsSourcePreference extends ListPreference
{
    public NewsSourcePreference(final Context context) {
        super(context);
    }
    
    public NewsSourcePreference(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public NewsSourcePreference(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public NewsSourcePreference(final Context context, final AttributeSet set, final int n, final int n2) {
        super(context, set, n, n2);
    }
    
    protected void onAttachedToActivity() {
        super.onAttachedToActivity();
        final String[] array = { "DainikBhaskar.com", "Newspoint" };
        this.setEntries((CharSequence[])array);
        this.setEntryValues((CharSequence[])array);
        this.setSummary((CharSequence)this.getValue());
    }
    
    protected void onDialogClosed(final boolean b) {
        super.onDialogClosed(b);
        if (b) {
            this.persistString(this.getValue());
            Settings.getInstance(this.getContext()).setPriority("pref_int_news_priority", 2);
            this.setSummary((CharSequence)this.getValue());
            NewsSourceManager.getInstance().setNewsSource(this.getValue());
            NewsSourceManager.getInstance().setNewsSourceUrl(AppConfigWrapper.getLifeFeedProviderUrl(this.getContext(), this.getValue()));
            final StringBuilder sb = new StringBuilder();
            sb.append("User setup pref:");
            sb.append(this.getValue());
            Log.d("NewsSource", sb.toString());
        }
    }
}
