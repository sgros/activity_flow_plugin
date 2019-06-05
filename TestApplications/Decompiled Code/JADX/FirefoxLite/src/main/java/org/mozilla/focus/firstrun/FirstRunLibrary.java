package org.mozilla.focus.firstrun;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.view.View;
import org.mozilla.focus.activity.InfoActivity;
import org.mozilla.focus.utils.SupportUtils;
import org.mozilla.rocket.C0769R;

public class FirstRunLibrary {
    public static FirstrunPage buildLifeFeedFirstrun(Context context) {
        String sumoURLForTopic = SupportUtils.getSumoURLForTopic(context, "firefox-lite-feed");
        String string = context.getString(C0769R.string.life_feed);
        return new FirstrunPage(context.getString(C0769R.string.first_run_page6_title), linkTextSpan(context, context.getString(C0769R.string.first_run_page6_text, new Object[]{string, "%s"}), context.getString(C0769R.string.about_link_learn_more), sumoURLForTopic, string), 2131230952);
    }

    private static Spannable linkTextSpan(final Context context, String str, String str2, final String str3, final String str4) {
        str = String.format(str, new Object[]{str2});
        int indexOf = str.indexOf(str2);
        int length = str2.length() + indexOf;
        C04571 c04571 = new ClickableSpan() {
            public void onClick(View view) {
                context.startActivity(InfoActivity.getIntentFor(context, str3, str4));
            }
        };
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        spannableStringBuilder.setSpan(c04571, indexOf, length, 18);
        return spannableStringBuilder;
    }
}
