// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.widget;

import android.os.Handler;
import android.os.Looper;
import java.util.Arrays;
import kotlin.jvm.internal.StringCompanionObject;
import org.mozilla.focus.utils.ViewUtils;
import android.widget.TextView$OnEditorActionListener;
import android.text.TextWatcher;
import android.view.View$OnClickListener;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.rocket.tabs.Session;
import android.widget.TextView;
import android.view.View;

public final class FindInPage implements BackKeyHandleable
{
    private final String accessibilityFormat;
    private final View closeBtn;
    private final View container;
    private final View nextBtn;
    private final View prevBtn;
    private final TextView queryText;
    private final String resultFormat;
    private final TextView resultText;
    private Session session;
    
    public FindInPage(View view) {
        Intrinsics.checkParameterIsNotNull(view, "rootView");
        view = view.findViewById(2131296437);
        Intrinsics.checkExpressionValueIsNotNull(view, "rootView.findViewById(R.id.find_in_page)");
        this.container = view;
        view = this.container.findViewById(2131296441);
        Intrinsics.checkExpressionValueIsNotNull(view, "container.findViewById(R\u2026.find_in_page_query_text)");
        this.queryText = (TextView)view;
        view = this.container.findViewById(2131296442);
        Intrinsics.checkExpressionValueIsNotNull(view, "container.findViewById(R\u2026find_in_page_result_text)");
        this.resultText = (TextView)view;
        view = this.container.findViewById(2131296439);
        Intrinsics.checkExpressionValueIsNotNull(view, "container.findViewById(R.id.find_in_page_next_btn)");
        this.nextBtn = view;
        view = this.container.findViewById(2131296440);
        Intrinsics.checkExpressionValueIsNotNull(view, "container.findViewById(R.id.find_in_page_prev_btn)");
        this.prevBtn = view;
        view = this.container.findViewById(2131296438);
        Intrinsics.checkExpressionValueIsNotNull(view, "container.findViewById(R\u2026d.find_in_page_close_btn)");
        this.closeBtn = view;
        final String string = this.container.getContext().getString(2131755198);
        Intrinsics.checkExpressionValueIsNotNull(string, "container.context.getStr\u2026ring.find_in_page_result)");
        this.resultFormat = string;
        final String string2 = this.container.getContext().getString(2131755057);
        Intrinsics.checkExpressionValueIsNotNull(string2, "container.context.getStr\u2026lity_find_in_page_result)");
        this.accessibilityFormat = string2;
        this.initViews();
    }
    
    private final void initViews() {
        final FindInPage$initViews.FindInPage$initViews$1 findInPage$initViews$1 = new FindInPage$initViews.FindInPage$initViews$1(this);
        this.closeBtn.setOnClickListener((View$OnClickListener)new FindInPage$initViews.FindInPage$initViews$2(this));
        this.queryText.setOnClickListener((View$OnClickListener)new FindInPage$initViews.FindInPage$initViews$3(this));
        this.prevBtn.setOnClickListener((View$OnClickListener)new FindInPage$initViews.FindInPage$initViews$4(findInPage$initViews$1));
        this.nextBtn.setOnClickListener((View$OnClickListener)new FindInPage$initViews.FindInPage$initViews$5(findInPage$initViews$1));
        this.queryText.addTextChangedListener((TextWatcher)new FindInPage$initViews.FindInPage$initViews$6(findInPage$initViews$1));
        this.queryText.setOnEditorActionListener((TextView$OnEditorActionListener)new FindInPage$initViews.FindInPage$initViews$7(this));
    }
    
    public final void hide() {
        if (this.container.getVisibility() != 0) {
            return;
        }
        ViewUtils.hideKeyboard((View)this.queryText);
        this.queryText.setText((CharSequence)null);
        this.queryText.clearFocus();
        this.container.setVisibility(8);
    }
    
    @Override
    public boolean onBackPressed() {
        boolean b;
        if (this.container.getVisibility() == 0) {
            this.hide();
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    public final void onFindResultReceived(final mozilla.components.browser.session.Session.FindResult findResult) {
        Intrinsics.checkParameterIsNotNull(findResult, "result");
        int activeMatchOrdinal = findResult.getActiveMatchOrdinal();
        final int numberOfMatches = findResult.getNumberOfMatches();
        if (numberOfMatches > 0) {
            ++activeMatchOrdinal;
            final TextView resultText = this.resultText;
            final StringCompanionObject instance = StringCompanionObject.INSTANCE;
            final String resultFormat = this.resultFormat;
            final Object[] original = { activeMatchOrdinal, numberOfMatches };
            final String format = String.format(resultFormat, Arrays.copyOf(original, original.length));
            Intrinsics.checkExpressionValueIsNotNull(format, "java.lang.String.format(format, *args)");
            resultText.setText((CharSequence)format);
            final TextView resultText2 = this.resultText;
            final StringCompanionObject instance2 = StringCompanionObject.INSTANCE;
            final String accessibilityFormat = this.accessibilityFormat;
            final Object[] original2 = { activeMatchOrdinal, numberOfMatches };
            final String format2 = String.format(accessibilityFormat, Arrays.copyOf(original2, original2.length));
            Intrinsics.checkExpressionValueIsNotNull(format2, "java.lang.String.format(format, *args)");
            resultText2.setContentDescription((CharSequence)format2);
        }
        else {
            this.resultText.setText((CharSequence)"");
            this.resultText.setContentDescription((CharSequence)"");
        }
    }
    
    public final void show(final Session session) {
        if (this.container.getVisibility() == 0) {
            return;
        }
        if (session != null) {
            this.session = session;
            this.container.setVisibility(0);
            new Handler(Looper.getMainLooper()).post((Runnable)new FindInPage$show.FindInPage$show$1(this));
        }
    }
}
