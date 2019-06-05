package org.mozilla.rocket.widget;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.util.Log;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.Settings;
import org.mozilla.rocket.content.NewsSourceManager;

public class NewsSourcePreference extends ListPreference {
    public NewsSourcePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public NewsSourcePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public NewsSourcePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public NewsSourcePreference(Context context) {
        super(context);
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToActivity() {
        super.onAttachedToActivity();
        String[] strArr = new String[]{"DainikBhaskar.com", "Newspoint"};
        setEntries(strArr);
        setEntryValues(strArr);
        setSummary(getValue());
    }

    /* Access modifiers changed, original: protected */
    public void onDialogClosed(boolean z) {
        super.onDialogClosed(z);
        if (z) {
            persistString(getValue());
            Settings.getInstance(getContext()).setPriority("pref_int_news_priority", 2);
            setSummary(getValue());
            NewsSourceManager.getInstance().setNewsSource(getValue());
            NewsSourceManager.getInstance().setNewsSourceUrl(AppConfigWrapper.getLifeFeedProviderUrl(getContext(), getValue()));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("User setup pref:");
            stringBuilder.append(getValue());
            Log.d("NewsSource", stringBuilder.toString());
        }
    }
}
