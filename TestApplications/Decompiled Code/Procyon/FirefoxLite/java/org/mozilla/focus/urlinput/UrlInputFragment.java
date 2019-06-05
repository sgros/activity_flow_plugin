// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.urlinput;

import android.text.style.StyleSpan;
import android.text.SpannableStringBuilder;
import java.util.Locale;
import java.util.List;
import android.view.View$OnFocusChangeListener;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.TypeCastException;
import android.view.LayoutInflater;
import org.mozilla.focus.web.WebViewProvider;
import android.content.Context;
import org.mozilla.focus.search.SearchEngineManager;
import android.widget.TextView;
import org.mozilla.focus.utils.SearchUtils;
import org.mozilla.focus.utils.ViewUtils;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.SupportUtils;
import android.webkit.URLUtil;
import java.util.ArrayList;
import android.arch.lifecycle.Observer;
import org.mozilla.focus.Inject;
import org.mozilla.rocket.urlinput.QuickSearchAdapter;
import kotlin.Unit;
import org.mozilla.rocket.urlinput.QuickSearch;
import kotlin.jvm.functions.Function1;
import android.support.v7.widget.LinearLayoutManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.support.v4.app.FragmentActivity;
import org.mozilla.focus.widget.FragmentListener;
import kotlin.jvm.internal.Intrinsics;
import mozilla.components.ui.autocomplete.InlineAutocompleteEditText;
import org.mozilla.focus.widget.FlowLayout;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import mozilla.components.browser.domains.DomainAutoCompleteProvider;
import java.util.HashMap;
import org.mozilla.focus.navigation.ScreenNavigator;
import android.view.View$OnLongClickListener;
import android.view.View$OnClickListener;
import android.support.v4.app.Fragment;

public final class UrlInputFragment extends Fragment implements View$OnClickListener, View$OnLongClickListener, UrlInputScreen, View
{
    public static final Companion Companion;
    private HashMap _$_findViewCache;
    private boolean allowSuggestion;
    private boolean autoCompleteInProgress;
    private final DomainAutoCompleteProvider autoCompleteProvider;
    private android.view.View clearView;
    private android.view.View dismissView;
    private long lastRequestTime;
    private Presenter presenter;
    private RecyclerView quickSearchRecyclerView;
    private ViewGroup quickSearchView;
    private FlowLayout suggestionView;
    private InlineAutocompleteEditText urlView;
    
    static {
        Companion = new Companion(null);
    }
    
    public UrlInputFragment() {
        this.autoCompleteProvider = new DomainAutoCompleteProvider();
    }
    
    public static final UrlInputFragment create(final String s, final String s2, final boolean b) {
        return UrlInputFragment.Companion.create(s, s2, b);
    }
    
    private final boolean detectThrottle() {
        final long currentTimeMillis = System.currentTimeMillis();
        final boolean b = currentTimeMillis - this.lastRequestTime < 300;
        this.lastRequestTime = currentTimeMillis;
        return b;
    }
    
    private final void dismiss() {
        final FragmentActivity activity = this.getActivity();
        if (activity instanceof FragmentListener) {
            ((FragmentListener)activity).onNotified(this, FragmentListener.TYPE.DISMISS_URL_INPUT, true);
        }
    }
    
    private final void initByArguments() {
        final Bundle arguments = this.getArguments();
        boolean boolean1 = false;
        if (arguments != null && arguments.containsKey("url")) {
            final String string = arguments.getString("url");
            final InlineAutocompleteEditText urlView = this.urlView;
            if (urlView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }
            final String text = string;
            urlView.setText((CharSequence)text);
            final android.view.View clearView = this.clearView;
            if (clearView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("clearView");
            }
            int visibility;
            if (TextUtils.isEmpty((CharSequence)text)) {
                visibility = 8;
            }
            else {
                visibility = 0;
            }
            clearView.setVisibility(visibility);
        }
        if (arguments != null) {
            boolean1 = arguments.getBoolean("allow_suggestion", true);
        }
        this.allowSuggestion = boolean1;
    }
    
    private final void initQuickSearch(android.view.View viewById) {
        final android.view.View viewById2 = viewById.findViewById(2131296579);
        Intrinsics.checkExpressionValueIsNotNull(viewById2, "view.findViewById(R.id.quick_search_container)");
        this.quickSearchView = (ViewGroup)viewById2;
        viewById = viewById.findViewById(2131296581);
        Intrinsics.checkExpressionValueIsNotNull(viewById, "view.findViewById(R.id.quick_search_recycler_view)");
        this.quickSearchRecyclerView = (RecyclerView)viewById;
        final RecyclerView quickSearchRecyclerView = this.quickSearchRecyclerView;
        if (quickSearchRecyclerView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("quickSearchRecyclerView");
        }
        quickSearchRecyclerView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(this.getContext(), 0, false));
        final QuickSearchAdapter quickSearchAdapter = new QuickSearchAdapter((Function1<? super QuickSearch, Unit>)new UrlInputFragment$initQuickSearch$quickSearchAdapter.UrlInputFragment$initQuickSearch$quickSearchAdapter$1(this));
        final RecyclerView quickSearchRecyclerView2 = this.quickSearchRecyclerView;
        if (quickSearchRecyclerView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("quickSearchRecyclerView");
        }
        quickSearchRecyclerView2.setAdapter((RecyclerView.Adapter)quickSearchAdapter);
        Inject.obtainQuickSearchViewModel(this.getActivity()).getQuickSearchObservable().observe(this.getViewLifecycleOwner(), (Observer<ArrayList<QuickSearch>>)new UrlInputFragment$initQuickSearch.UrlInputFragment$initQuickSearch$1(quickSearchAdapter));
    }
    
    private final void onCommit() {
        final InlineAutocompleteEditText urlView = this.urlView;
        if (urlView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("urlView");
        }
        String s = urlView.getAutocompleteResult().getFormattedText();
        Label_0094: {
            if (s.length() != 0) {
                final InlineAutocompleteEditText urlView2 = this.urlView;
                if (urlView2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("urlView");
                }
                if (URLUtil.isValidUrl(String.valueOf(urlView2.getText()))) {
                    break Label_0094;
                }
            }
            final InlineAutocompleteEditText urlView3 = this.urlView;
            if (urlView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }
            s = String.valueOf(urlView3.getText());
        }
        this.search(s);
        TelemetryWrapper.urlBarEvent(SupportUtils.isUrl(s), false);
    }
    
    private final void onFilter(final String s, final InlineAutocompleteEditText inlineAutocompleteEditText) {
        if (!this.isVisible()) {
            return;
        }
        this.autoCompleteInProgress = true;
        final DomainAutoCompleteProvider.Result autocomplete = this.autoCompleteProvider.autocomplete(s);
        if (inlineAutocompleteEditText != null) {
            inlineAutocompleteEditText.applyAutocompleteResult(new InlineAutocompleteEditText.AutocompleteResult(autocomplete.getText(), autocomplete.getSource(), autocomplete.getSize(), (Function1<? super String, String>)new UrlInputFragment$onFilter.UrlInputFragment$onFilter$1(autocomplete)));
        }
        this.autoCompleteInProgress = false;
    }
    
    private final void onSuggestionClicked(final CharSequence charSequence) {
        this.search(charSequence.toString());
        TelemetryWrapper.urlBarEvent(SupportUtils.isUrl(charSequence.toString()), true);
    }
    
    private final void onTextChange(final String s, final String s2) {
        if (this.autoCompleteInProgress) {
            return;
        }
        if (this.allowSuggestion) {
            final Presenter presenter = this.presenter;
            if (presenter == null) {
                Intrinsics.throwUninitializedPropertyAccessException("presenter");
            }
            presenter.onInput(s, this.detectThrottle());
        }
        int visibility;
        if (TextUtils.isEmpty((CharSequence)s)) {
            visibility = 8;
        }
        else {
            visibility = 0;
        }
        final android.view.View clearView = this.clearView;
        if (clearView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("clearView");
        }
        clearView.setVisibility(visibility);
    }
    
    private final boolean openUrl(final String s) {
        final Bundle arguments = this.getArguments();
        final boolean b = arguments != null && arguments.containsKey("parent_frag_tag") && Intrinsics.areEqual("home_screen", arguments.getString("parent_frag_tag"));
        Object activity = this.getActivity();
        final boolean b2 = activity instanceof FragmentListener;
        if (b2) {
            if (!b2) {
                activity = null;
            }
            final FragmentListener fragmentListener = (FragmentListener)activity;
            FragmentListener.TYPE type;
            if (b) {
                type = FragmentListener.TYPE.OPEN_URL_IN_NEW_TAB;
            }
            else {
                type = FragmentListener.TYPE.OPEN_URL_IN_CURRENT_TAB;
            }
            if (fragmentListener != null) {
                fragmentListener.onNotified(this, type, s);
            }
        }
        return b;
    }
    
    private final void search(String s) {
        final String s2 = s;
        int length = s2.length();
        final int n = 1;
        --length;
        int i = 0;
        int n2 = 0;
        while (i <= length) {
            int n3;
            if (n2 == 0) {
                n3 = i;
            }
            else {
                n3 = length;
            }
            final boolean b = s2.charAt(n3) <= ' ';
            if (n2 == 0) {
                if (!b) {
                    n2 = 1;
                }
                else {
                    ++i;
                }
            }
            else {
                if (!b) {
                    break;
                }
                --length;
            }
        }
        int n4;
        if (s2.subSequence(i, length + 1).toString().length() == 0) {
            n4 = n;
        }
        else {
            n4 = 0;
        }
        if (n4 == 0) {
            final InlineAutocompleteEditText urlView = this.urlView;
            if (urlView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }
            ViewUtils.hideKeyboard((android.view.View)urlView);
            if (SupportUtils.isUrl(s)) {
                s = SupportUtils.normalize(s);
            }
            else {
                s = SearchUtils.createSearchUrl(this.getContext(), s);
            }
            Intrinsics.checkExpressionValueIsNotNull(s, "url");
            if (this.openUrl(s)) {
                TelemetryWrapper.addNewTabFromHome();
            }
        }
    }
    
    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }
    }
    
    public UrlInputFragment getFragment() {
        return this;
    }
    
    public void onClick(final android.view.View view) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        final int id = view.getId();
        if (id != 2131296368) {
            if (id != 2131296410) {
                if (id != 2131296676) {
                    throw new IllegalStateException("Unhandled view in onClick()");
                }
                final CharSequence text = ((TextView)view).getText();
                Intrinsics.checkExpressionValueIsNotNull(text, "(view as TextView).text");
                this.onSuggestionClicked(text);
            }
            else {
                this.dismiss();
                TelemetryWrapper.searchDismiss();
            }
        }
        else {
            final InlineAutocompleteEditText urlView = this.urlView;
            if (urlView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }
            urlView.setText((CharSequence)"");
            final InlineAutocompleteEditText urlView2 = this.urlView;
            if (urlView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }
            urlView2.requestFocus();
            TelemetryWrapper.searchClear();
        }
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.presenter = new UrlInputPresenter(SearchEngineManager.getInstance().getDefaultSearchEngine((Context)this.getActivity()), WebViewProvider.getUserAgentString((Context)this.getActivity()));
        final Context context = this.getContext();
        if (context != null) {
            final DomainAutoCompleteProvider autoCompleteProvider = this.autoCompleteProvider;
            Intrinsics.checkExpressionValueIsNotNull(context, "it");
            final Context applicationContext = context.getApplicationContext();
            Intrinsics.checkExpressionValueIsNotNull(applicationContext, "it.applicationContext");
            DomainAutoCompleteProvider.initialize$default(autoCompleteProvider, applicationContext, true, false, false, 8, (Object)null);
        }
    }
    
    @Override
    public android.view.View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(layoutInflater, "inflater");
        final android.view.View inflate = layoutInflater.inflate(2131492975, viewGroup, false);
        final android.view.View viewById = inflate.findViewById(2131296410);
        Intrinsics.checkExpressionValueIsNotNull(viewById, "view.findViewById(R.id.dismiss)");
        this.dismissView = viewById;
        final android.view.View dismissView = this.dismissView;
        if (dismissView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dismissView");
        }
        final View$OnClickListener view$OnClickListener = (View$OnClickListener)this;
        dismissView.setOnClickListener(view$OnClickListener);
        final android.view.View viewById2 = inflate.findViewById(2131296368);
        Intrinsics.checkExpressionValueIsNotNull(viewById2, "view.findViewById(R.id.clear)");
        this.clearView = viewById2;
        final android.view.View clearView = this.clearView;
        if (clearView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("clearView");
        }
        clearView.setOnClickListener(view$OnClickListener);
        final android.view.View viewById3 = inflate.findViewById(2131296633);
        if (viewById3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type org.mozilla.focus.widget.FlowLayout");
        }
        this.suggestionView = (FlowLayout)viewById3;
        final android.view.View viewById4 = inflate.findViewById(2131296714);
        if (viewById4 != null) {
            this.urlView = (InlineAutocompleteEditText)viewById4;
            final InlineAutocompleteEditText urlView = this.urlView;
            if (urlView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }
            final UrlInputFragment urlInputFragment = this;
            urlView.setOnTextChangeListener((Function2<? super String, ? super String, Unit>)new UrlInputFragment$onCreateView.UrlInputFragment$onCreateView$1(urlInputFragment));
            final InlineAutocompleteEditText urlView2 = this.urlView;
            if (urlView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }
            urlView2.setOnCommitListener((Function0<Unit>)new UrlInputFragment$onCreateView.UrlInputFragment$onCreateView$2(urlInputFragment));
            final InlineAutocompleteEditText urlView3 = this.urlView;
            if (urlView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }
            urlView3.setOnFocusChangeListener((View$OnFocusChangeListener)new UrlInputFragment$onCreateView.UrlInputFragment$onCreateView$3(this));
            final InlineAutocompleteEditText urlView4 = this.urlView;
            if (urlView4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }
            urlView4.setOnFilterListener((Function2<? super String, ? super InlineAutocompleteEditText, Unit>)new UrlInputFragment$onCreateView.UrlInputFragment$onCreateView$4(urlInputFragment));
            final InlineAutocompleteEditText urlView5 = this.urlView;
            if (urlView5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }
            final InlineAutocompleteEditText urlView6 = this.urlView;
            if (urlView6 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }
            urlView5.setImeOptions(urlView6.getImeOptions() | 0x1000000);
            this.initByArguments();
            Intrinsics.checkExpressionValueIsNotNull(inflate, "view");
            this.initQuickSearch(inflate);
            return inflate;
        }
        throw new TypeCastException("null cannot be cast to non-null type mozilla.components.ui.autocomplete.InlineAutocompleteEditText");
    }
    
    public boolean onLongClick(final android.view.View view) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        if (view.getId() != 2131296676) {
            return false;
        }
        this.setUrlText(((TextView)view).getText());
        TelemetryWrapper.searchSuggestionLongClick();
        return true;
    }
    
    @Override
    public void onStart() {
        super.onStart();
        final Presenter presenter = this.presenter;
        if (presenter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("presenter");
        }
        presenter.setView(this);
        final InlineAutocompleteEditText urlView = this.urlView;
        if (urlView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("urlView");
        }
        urlView.requestFocus();
    }
    
    @Override
    public void onStop() {
        super.onStop();
        final Presenter presenter = this.presenter;
        if (presenter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("presenter");
        }
        presenter.setView(null);
    }
    
    public void setQuickSearchVisible(final boolean b) {
        if (b) {
            final ViewGroup quickSearchView = this.quickSearchView;
            if (quickSearchView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("quickSearchView");
            }
            quickSearchView.setVisibility(0);
        }
        else {
            final ViewGroup quickSearchView2 = this.quickSearchView;
            if (quickSearchView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("quickSearchView");
            }
            quickSearchView2.setVisibility(8);
        }
    }
    
    public void setSuggestions(final List<? extends CharSequence> list) {
        final FlowLayout suggestionView = this.suggestionView;
        if (suggestionView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("suggestionView");
        }
        suggestionView.removeAllViews();
        if (list == null) {
            return;
        }
        final InlineAutocompleteEditText urlView = this.urlView;
        if (urlView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("urlView");
        }
        final String s = urlView.getOriginalText();
        int length = s.length();
        final int n = 0;
        --length;
        int i = 0;
        int n2 = 0;
        while (i <= length) {
            int n3;
            if (n2 == 0) {
                n3 = i;
            }
            else {
                n3 = length;
            }
            final boolean b = s.charAt(n3) <= ' ';
            if (n2 == 0) {
                if (!b) {
                    n2 = 1;
                }
                else {
                    ++i;
                }
            }
            else {
                if (!b) {
                    break;
                }
                --length;
            }
        }
        final String string = s.subSequence(i, length + 1).toString();
        final Locale default1 = Locale.getDefault();
        Intrinsics.checkExpressionValueIsNotNull(default1, "Locale.getDefault()");
        if (string != null) {
            final String lowerCase = string.toLowerCase(default1);
            Intrinsics.checkExpressionValueIsNotNull(lowerCase, "(this as java.lang.String).toLowerCase(locale)");
            for (int size = list.size(), j = n; j < size; ++j) {
                final android.view.View inflate = android.view.View.inflate(this.getContext(), 2131493020, (ViewGroup)null);
                if (inflate == null) {
                    throw new TypeCastException("null cannot be cast to non-null type android.widget.TextView");
                }
                final TextView textView = (TextView)inflate;
                final String string2 = list.get(j).toString();
                final Locale default2 = Locale.getDefault();
                Intrinsics.checkExpressionValueIsNotNull(default2, "Locale.getDefault()");
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                final String lowerCase2 = string2.toLowerCase(default2);
                Intrinsics.checkExpressionValueIsNotNull(lowerCase2, "(this as java.lang.String).toLowerCase(locale)");
                final int indexOf$default = StringsKt__StringsKt.indexOf$default(lowerCase2, lowerCase, 0, false, 6, null);
                if (indexOf$default != -1) {
                    final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder((CharSequence)list.get(j));
                    spannableStringBuilder.setSpan((Object)new StyleSpan(1), indexOf$default, lowerCase.length() + indexOf$default, 33);
                    textView.setText((CharSequence)spannableStringBuilder);
                }
                else {
                    textView.setText((CharSequence)list.get(j));
                }
                textView.setOnClickListener((View$OnClickListener)this);
                textView.setOnLongClickListener((View$OnLongClickListener)this);
                final FlowLayout suggestionView2 = this.suggestionView;
                if (suggestionView2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("suggestionView");
                }
                suggestionView2.addView((android.view.View)textView);
            }
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
    }
    
    public void setUrlText(final CharSequence text) {
        if (text != null) {
            final InlineAutocompleteEditText urlView = this.urlView;
            if (urlView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }
            urlView.setText(text);
            final InlineAutocompleteEditText urlView2 = this.urlView;
            if (urlView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }
            urlView2.setSelection(text.length());
        }
    }
    
    public static final class Companion
    {
        private Companion() {
        }
        
        public final UrlInputFragment create(final String s, final String s2, final boolean b) {
            final Bundle arguments = new Bundle();
            arguments.putString("url", s);
            arguments.putString("parent_frag_tag", s2);
            arguments.putBoolean("allow_suggestion", b);
            final UrlInputFragment urlInputFragment = new UrlInputFragment();
            urlInputFragment.setArguments(arguments);
            return urlInputFragment;
        }
    }
}
