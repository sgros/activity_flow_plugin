// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.firstrun;

import android.text.SpannableStringBuilder;
import org.mozilla.focus.activity.InfoActivity;
import android.view.View;
import android.text.style.ClickableSpan;
import android.text.Spannable;
import org.mozilla.focus.utils.SupportUtils;
import android.content.Context;

public class FirstRunLibrary
{
    public static FirstrunPage buildLifeFeedFirstrun(final Context context) {
        final String sumoURLForTopic = SupportUtils.getSumoURLForTopic(context, "firefox-lite-feed");
        final String string = context.getString(2131755245);
        return new FirstrunPage(context.getString(2131755211), (CharSequence)linkTextSpan(context, context.getString(2131755210, new Object[] { string, "%s" }), context.getString(2131755049), sumoURLForTopic, string), 2131230952);
    }
    
    private static Spannable linkTextSpan(final Context context, String format, final String str, final String s, final String s2) {
        format = String.format(format, str);
        final int index = format.indexOf(str);
        final int length = str.length();
        final ClickableSpan clickableSpan = new ClickableSpan() {
            public void onClick(final View view) {
                context.startActivity(InfoActivity.getIntentFor(context, s, s2));
            }
        };
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder((CharSequence)format);
        spannableStringBuilder.setSpan((Object)clickableSpan, index, length + index, 18);
        return (Spannable)spannableStringBuilder;
    }
}
