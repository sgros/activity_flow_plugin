package org.mozilla.focus.widget;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import java.util.Arrays;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import mozilla.components.browser.session.Session.FindResult;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.utils.ViewUtils;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.tabs.Session;

/* compiled from: FindInPage.kt */
public final class FindInPage implements BackKeyHandleable {
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
        view = view.findViewById(C0427R.C0426id.find_in_page);
        Intrinsics.checkExpressionValueIsNotNull(view, "rootView.findViewById(R.id.find_in_page)");
        this.container = view;
        view = this.container.findViewById(C0427R.C0426id.find_in_page_query_text);
        Intrinsics.checkExpressionValueIsNotNull(view, "container.findViewById(R….find_in_page_query_text)");
        this.queryText = (TextView) view;
        view = this.container.findViewById(C0427R.C0426id.find_in_page_result_text);
        Intrinsics.checkExpressionValueIsNotNull(view, "container.findViewById(R…find_in_page_result_text)");
        this.resultText = (TextView) view;
        view = this.container.findViewById(C0427R.C0426id.find_in_page_next_btn);
        Intrinsics.checkExpressionValueIsNotNull(view, "container.findViewById(R.id.find_in_page_next_btn)");
        this.nextBtn = view;
        view = this.container.findViewById(C0427R.C0426id.find_in_page_prev_btn);
        Intrinsics.checkExpressionValueIsNotNull(view, "container.findViewById(R.id.find_in_page_prev_btn)");
        this.prevBtn = view;
        view = this.container.findViewById(C0427R.C0426id.find_in_page_close_btn);
        Intrinsics.checkExpressionValueIsNotNull(view, "container.findViewById(R…d.find_in_page_close_btn)");
        this.closeBtn = view;
        String string = this.container.getContext().getString(C0769R.string.find_in_page_result);
        Intrinsics.checkExpressionValueIsNotNull(string, "container.context.getStr…ring.find_in_page_result)");
        this.resultFormat = string;
        string = this.container.getContext().getString(C0769R.string.accessibility_find_in_page_result);
        Intrinsics.checkExpressionValueIsNotNull(string, "container.context.getStr…lity_find_in_page_result)");
        this.accessibilityFormat = string;
        initViews();
    }

    public boolean onBackPressed() {
        if (this.container.getVisibility() != 0) {
            return false;
        }
        hide();
        return true;
    }

    public final void onFindResultReceived(FindResult findResult) {
        Intrinsics.checkParameterIsNotNull(findResult, "result");
        int activeMatchOrdinal = findResult.getActiveMatchOrdinal();
        if (findResult.getNumberOfMatches() > 0) {
            activeMatchOrdinal++;
            TextView textView = this.resultText;
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            Object[] objArr = new Object[]{Integer.valueOf(activeMatchOrdinal), Integer.valueOf(r9)};
            String format = String.format(this.resultFormat, Arrays.copyOf(objArr, objArr.length));
            Intrinsics.checkExpressionValueIsNotNull(format, "java.lang.String.format(format, *args)");
            textView.setText(format);
            textView = this.resultText;
            stringCompanionObject = StringCompanionObject.INSTANCE;
            Object[] objArr2 = new Object[]{Integer.valueOf(activeMatchOrdinal), Integer.valueOf(r9)};
            String format2 = String.format(this.accessibilityFormat, Arrays.copyOf(objArr2, objArr2.length));
            Intrinsics.checkExpressionValueIsNotNull(format2, "java.lang.String.format(format, *args)");
            textView.setContentDescription(format2);
            return;
        }
        this.resultText.setText("");
        this.resultText.setContentDescription("");
    }

    public final void show(Session session) {
        if (!(this.container.getVisibility() == 0 || session == null)) {
            this.session = session;
            this.container.setVisibility(0);
            new Handler(Looper.getMainLooper()).post(new FindInPage$show$1(this));
        }
    }

    public final void hide() {
        if (this.container.getVisibility() == 0) {
            ViewUtils.hideKeyboard(this.queryText);
            this.queryText.setText((CharSequence) null);
            this.queryText.clearFocus();
            this.container.setVisibility(8);
        }
    }

    private final void initViews() {
        FindInPage$initViews$1 findInPage$initViews$1 = new FindInPage$initViews$1(this);
        this.closeBtn.setOnClickListener(new FindInPage$initViews$2(this));
        this.queryText.setOnClickListener(new FindInPage$initViews$3(this));
        this.prevBtn.setOnClickListener(new FindInPage$initViews$4(findInPage$initViews$1));
        this.nextBtn.setOnClickListener(new FindInPage$initViews$5(findInPage$initViews$1));
        this.queryText.addTextChangedListener(new FindInPage$initViews$6(findInPage$initViews$1));
        this.queryText.setOnEditorActionListener(new FindInPage$initViews$7(this));
    }
}
