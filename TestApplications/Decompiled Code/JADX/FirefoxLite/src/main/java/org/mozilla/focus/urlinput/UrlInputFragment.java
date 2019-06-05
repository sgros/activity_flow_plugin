package org.mozilla.focus.urlinput;

import android.content.Context;
import android.os.Bundle;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentActivity;
import android.support.p004v7.widget.LinearLayoutManager;
import android.support.p004v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import mozilla.components.browser.domains.DomainAutoCompleteProvider;
import mozilla.components.browser.domains.DomainAutoCompleteProvider.Result;
import mozilla.components.p006ui.autocomplete.InlineAutocompleteEditText;
import mozilla.components.p006ui.autocomplete.InlineAutocompleteEditText.AutocompleteResult;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.Inject;
import org.mozilla.focus.navigation.ScreenNavigator.UrlInputScreen;
import org.mozilla.focus.search.SearchEngineManager;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.SearchUtils;
import org.mozilla.focus.utils.SupportUtils;
import org.mozilla.focus.utils.ViewUtils;
import org.mozilla.focus.web.WebViewProvider;
import org.mozilla.focus.widget.FlowLayout;
import org.mozilla.focus.widget.FragmentListener;
import org.mozilla.focus.widget.FragmentListener.TYPE;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.urlinput.QuickSearchAdapter;

/* compiled from: UrlInputFragment.kt */
public final class UrlInputFragment extends Fragment implements OnClickListener, OnLongClickListener, UrlInputScreen, View {
    public static final Companion Companion = new Companion();
    private HashMap _$_findViewCache;
    private boolean allowSuggestion;
    private boolean autoCompleteInProgress;
    private final DomainAutoCompleteProvider autoCompleteProvider = new DomainAutoCompleteProvider();
    private View clearView;
    private View dismissView;
    private long lastRequestTime;
    private Presenter presenter;
    private RecyclerView quickSearchRecyclerView;
    private ViewGroup quickSearchView;
    private FlowLayout suggestionView;
    private InlineAutocompleteEditText urlView;

    /* compiled from: UrlInputFragment.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final UrlInputFragment create(String str, String str2, boolean z) {
            Bundle bundle = new Bundle();
            bundle.putString("url", str);
            bundle.putString("parent_frag_tag", str2);
            bundle.putBoolean("allow_suggestion", z);
            UrlInputFragment urlInputFragment = new UrlInputFragment();
            urlInputFragment.setArguments(bundle);
            return urlInputFragment;
        }
    }

    public static final UrlInputFragment create(String str, String str2, boolean z) {
        return Companion.create(str, str2, z);
    }

    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }
    }

    public UrlInputFragment getFragment() {
        return this;
    }

    public /* synthetic */ void onDestroyView() {
        super.onDestroyView();
        _$_clearFindViewByIdCache();
    }

    public static final /* synthetic */ InlineAutocompleteEditText access$getUrlView$p(UrlInputFragment urlInputFragment) {
        InlineAutocompleteEditText inlineAutocompleteEditText = urlInputFragment.urlView;
        if (inlineAutocompleteEditText == null) {
            Intrinsics.throwUninitializedPropertyAccessException("urlView");
        }
        return inlineAutocompleteEditText;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.presenter = new UrlInputPresenter(SearchEngineManager.getInstance().getDefaultSearchEngine(getActivity()), WebViewProvider.getUserAgentString(getActivity()));
        Context context = getContext();
        if (context != null) {
            DomainAutoCompleteProvider domainAutoCompleteProvider = this.autoCompleteProvider;
            Intrinsics.checkExpressionValueIsNotNull(context, "it");
            Context applicationContext = context.getApplicationContext();
            Intrinsics.checkExpressionValueIsNotNull(applicationContext, "it.applicationContext");
            DomainAutoCompleteProvider.initialize$default(domainAutoCompleteProvider, applicationContext, true, false, false, 8, null);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(layoutInflater, "inflater");
        View inflate = layoutInflater.inflate(C0769R.layout.fragment_urlinput, viewGroup, false);
        View findViewById = inflate.findViewById(C0427R.C0426id.dismiss);
        Intrinsics.checkExpressionValueIsNotNull(findViewById, "view.findViewById(R.id.dismiss)");
        this.dismissView = findViewById;
        findViewById = this.dismissView;
        if (findViewById == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dismissView");
        }
        OnClickListener onClickListener = this;
        findViewById.setOnClickListener(onClickListener);
        findViewById = inflate.findViewById(C0427R.C0426id.clear);
        Intrinsics.checkExpressionValueIsNotNull(findViewById, "view.findViewById(R.id.clear)");
        this.clearView = findViewById;
        findViewById = this.clearView;
        if (findViewById == null) {
            Intrinsics.throwUninitializedPropertyAccessException("clearView");
        }
        findViewById.setOnClickListener(onClickListener);
        findViewById = inflate.findViewById(C0427R.C0426id.search_suggestion);
        if (findViewById != null) {
            this.suggestionView = (FlowLayout) findViewById;
            findViewById = inflate.findViewById(C0427R.C0426id.url_edit);
            if (findViewById != null) {
                this.urlView = (InlineAutocompleteEditText) findViewById;
                InlineAutocompleteEditText inlineAutocompleteEditText = this.urlView;
                if (inlineAutocompleteEditText == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("urlView");
                }
                UrlInputFragment urlInputFragment = this;
                inlineAutocompleteEditText.setOnTextChangeListener(new UrlInputFragment$onCreateView$1(urlInputFragment));
                inlineAutocompleteEditText = this.urlView;
                if (inlineAutocompleteEditText == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("urlView");
                }
                inlineAutocompleteEditText.setOnCommitListener(new UrlInputFragment$onCreateView$2(urlInputFragment));
                inlineAutocompleteEditText = this.urlView;
                if (inlineAutocompleteEditText == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("urlView");
                }
                inlineAutocompleteEditText.setOnFocusChangeListener(new UrlInputFragment$onCreateView$3(this));
                inlineAutocompleteEditText = this.urlView;
                if (inlineAutocompleteEditText == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("urlView");
                }
                inlineAutocompleteEditText.setOnFilterListener(new UrlInputFragment$onCreateView$4(urlInputFragment));
                inlineAutocompleteEditText = this.urlView;
                if (inlineAutocompleteEditText == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("urlView");
                }
                InlineAutocompleteEditText inlineAutocompleteEditText2 = this.urlView;
                if (inlineAutocompleteEditText2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("urlView");
                }
                inlineAutocompleteEditText.setImeOptions(inlineAutocompleteEditText2.getImeOptions() | 16777216);
                initByArguments();
                Intrinsics.checkExpressionValueIsNotNull(inflate, "view");
                initQuickSearch(inflate);
                return inflate;
            }
            throw new TypeCastException("null cannot be cast to non-null type mozilla.components.ui.autocomplete.InlineAutocompleteEditText");
        }
        throw new TypeCastException("null cannot be cast to non-null type org.mozilla.focus.widget.FlowLayout");
    }

    private final void initQuickSearch(View view) {
        View findViewById = view.findViewById(C0427R.C0426id.quick_search_container);
        Intrinsics.checkExpressionValueIsNotNull(findViewById, "view.findViewById(R.id.quick_search_container)");
        this.quickSearchView = (ViewGroup) findViewById;
        view = view.findViewById(C0427R.C0426id.quick_search_recycler_view);
        Intrinsics.checkExpressionValueIsNotNull(view, "view.findViewById(R.id.quick_search_recycler_view)");
        this.quickSearchRecyclerView = (RecyclerView) view;
        RecyclerView recyclerView = this.quickSearchRecyclerView;
        if (recyclerView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("quickSearchRecyclerView");
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        QuickSearchAdapter quickSearchAdapter = new QuickSearchAdapter(new UrlInputFragment$initQuickSearch$quickSearchAdapter$1(this));
        RecyclerView recyclerView2 = this.quickSearchRecyclerView;
        if (recyclerView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("quickSearchRecyclerView");
        }
        recyclerView2.setAdapter(quickSearchAdapter);
        Inject.obtainQuickSearchViewModel(getActivity()).getQuickSearchObservable().observe(getViewLifecycleOwner(), new UrlInputFragment$initQuickSearch$1(quickSearchAdapter));
    }

    public void onStart() {
        super.onStart();
        Presenter presenter = this.presenter;
        if (presenter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("presenter");
        }
        presenter.setView(this);
        InlineAutocompleteEditText inlineAutocompleteEditText = this.urlView;
        if (inlineAutocompleteEditText == null) {
            Intrinsics.throwUninitializedPropertyAccessException("urlView");
        }
        inlineAutocompleteEditText.requestFocus();
    }

    public void onStop() {
        super.onStop();
        Presenter presenter = this.presenter;
        if (presenter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("presenter");
        }
        presenter.setView(null);
    }

    public boolean onLongClick(View view) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        if (view.getId() != C0427R.C0426id.suggestion_item) {
            return false;
        }
        setUrlText(((TextView) view).getText());
        TelemetryWrapper.searchSuggestionLongClick();
        return true;
    }

    public void onClick(View view) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        int id = view.getId();
        if (id == C0427R.C0426id.clear) {
            InlineAutocompleteEditText inlineAutocompleteEditText = this.urlView;
            if (inlineAutocompleteEditText == null) {
                Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }
            inlineAutocompleteEditText.setText("");
            inlineAutocompleteEditText = this.urlView;
            if (inlineAutocompleteEditText == null) {
                Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }
            inlineAutocompleteEditText.requestFocus();
            TelemetryWrapper.searchClear();
        } else if (id == C0427R.C0426id.dismiss) {
            dismiss();
            TelemetryWrapper.searchDismiss();
        } else if (id == C0427R.C0426id.suggestion_item) {
            CharSequence text = ((TextView) view).getText();
            Intrinsics.checkExpressionValueIsNotNull(text, "(view as TextView).text");
            onSuggestionClicked(text);
        } else {
            throw new IllegalStateException("Unhandled view in onClick()");
        }
    }

    private final void initByArguments() {
        Bundle arguments = getArguments();
        boolean z = false;
        if (arguments != null && arguments.containsKey("url")) {
            String string = arguments.getString("url");
            InlineAutocompleteEditText inlineAutocompleteEditText = this.urlView;
            if (inlineAutocompleteEditText == null) {
                Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }
            CharSequence charSequence = string;
            inlineAutocompleteEditText.setText(charSequence);
            View view = this.clearView;
            if (view == null) {
                Intrinsics.throwUninitializedPropertyAccessException("clearView");
            }
            view.setVisibility(TextUtils.isEmpty(charSequence) ? 8 : 0);
        }
        if (arguments != null) {
            z = arguments.getBoolean("allow_suggestion", true);
        }
        this.allowSuggestion = z;
    }

    private final void onSuggestionClicked(CharSequence charSequence) {
        search(charSequence.toString());
        TelemetryWrapper.urlBarEvent(SupportUtils.isUrl(charSequence.toString()), true);
    }

    private final void dismiss() {
        FragmentActivity activity = getActivity();
        if (activity instanceof FragmentListener) {
            ((FragmentListener) activity).onNotified(this, TYPE.DISMISS_URL_INPUT, Boolean.valueOf(true));
        }
    }

    /* JADX WARNING: Missing block: B:12:0x0035, code skipped:
            if (android.webkit.URLUtil.isValidUrl(java.lang.String.valueOf(r1.getText())) == false) goto L_0x0037;
     */
    private final void onCommit() {
        /*
        r4 = this;
        r0 = r4.urlView;
        if (r0 != 0) goto L_0x0009;
    L_0x0004:
        r1 = "urlView";
        kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r1);
    L_0x0009:
        r0 = r0.getAutocompleteResult();
        r0 = r0.getFormattedText();
        r1 = r0;
        r1 = (java.lang.CharSequence) r1;
        r1 = r1.length();
        r2 = 0;
        if (r1 != 0) goto L_0x001d;
    L_0x001b:
        r1 = 1;
        goto L_0x001e;
    L_0x001d:
        r1 = 0;
    L_0x001e:
        if (r1 != 0) goto L_0x0037;
    L_0x0020:
        r1 = r4.urlView;
        if (r1 != 0) goto L_0x0029;
    L_0x0024:
        r3 = "urlView";
        kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r3);
    L_0x0029:
        r1 = r1.getText();
        r1 = java.lang.String.valueOf(r1);
        r1 = android.webkit.URLUtil.isValidUrl(r1);
        if (r1 != 0) goto L_0x0048;
    L_0x0037:
        r0 = r4.urlView;
        if (r0 != 0) goto L_0x0040;
    L_0x003b:
        r1 = "urlView";
        kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r1);
    L_0x0040:
        r0 = r0.getText();
        r0 = java.lang.String.valueOf(r0);
    L_0x0048:
        r4.search(r0);
        r0 = org.mozilla.focus.utils.SupportUtils.isUrl(r0);
        org.mozilla.focus.telemetry.TelemetryWrapper.urlBarEvent(r0, r2);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.focus.urlinput.UrlInputFragment.onCommit():void");
    }

    private final boolean openUrl(String str) {
        Bundle arguments = getArguments();
        boolean areEqual = (arguments == null || !arguments.containsKey("parent_frag_tag")) ? false : Intrinsics.areEqual("home_screen", arguments.getString("parent_frag_tag"));
        FragmentActivity activity = getActivity();
        boolean z = activity instanceof FragmentListener;
        if (z) {
            TYPE type;
            if (!z) {
                activity = null;
            }
            FragmentListener fragmentListener = (FragmentListener) activity;
            if (areEqual) {
                type = TYPE.OPEN_URL_IN_NEW_TAB;
            } else {
                type = TYPE.OPEN_URL_IN_CURRENT_TAB;
            }
            if (fragmentListener != null) {
                fragmentListener.onNotified(this, type, str);
            }
        }
        return areEqual;
    }

    public void setUrlText(CharSequence charSequence) {
        if (charSequence != null) {
            InlineAutocompleteEditText inlineAutocompleteEditText = this.urlView;
            if (inlineAutocompleteEditText == null) {
                Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }
            inlineAutocompleteEditText.setText(charSequence);
            inlineAutocompleteEditText = this.urlView;
            if (inlineAutocompleteEditText == null) {
                Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }
            inlineAutocompleteEditText.setSelection(charSequence.length());
        }
    }

    public void setSuggestions(List<? extends CharSequence> list) {
        FlowLayout flowLayout = this.suggestionView;
        if (flowLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("suggestionView");
        }
        flowLayout.removeAllViews();
        if (list != null) {
            InlineAutocompleteEditText inlineAutocompleteEditText = this.urlView;
            if (inlineAutocompleteEditText == null) {
                Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }
            CharSequence originalText = inlineAutocompleteEditText.getOriginalText();
            int i = 0;
            int length = originalText.length() - 1;
            int i2 = 0;
            Object obj = null;
            while (i2 <= length) {
                Object obj2 = originalText.charAt(obj == null ? i2 : length) <= ' ' ? 1 : null;
                if (obj == null) {
                    if (obj2 == null) {
                        obj = 1;
                    } else {
                        i2++;
                    }
                } else if (obj2 == null) {
                    break;
                } else {
                    length--;
                }
            }
            String obj3 = originalText.subSequence(i2, length + 1).toString();
            Locale locale = Locale.getDefault();
            Intrinsics.checkExpressionValueIsNotNull(locale, "Locale.getDefault()");
            if (obj3 != null) {
                obj3 = obj3.toLowerCase(locale);
                Intrinsics.checkExpressionValueIsNotNull(obj3, "(this as java.lang.String).toLowerCase(locale)");
                i2 = list.size();
                while (i < i2) {
                    View inflate = View.inflate(getContext(), C0769R.layout.tag_text, null);
                    if (inflate != null) {
                        TextView textView = (TextView) inflate;
                        String obj4 = list.get(i).toString();
                        Locale locale2 = Locale.getDefault();
                        Intrinsics.checkExpressionValueIsNotNull(locale2, "Locale.getDefault()");
                        if (obj4 != null) {
                            obj4 = obj4.toLowerCase(locale2);
                            Intrinsics.checkExpressionValueIsNotNull(obj4, "(this as java.lang.String).toLowerCase(locale)");
                            int indexOf$default = StringsKt__StringsKt.indexOf$default((CharSequence) obj4, obj3, 0, false, 6, null);
                            if (indexOf$default != -1) {
                                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder((CharSequence) list.get(i));
                                spannableStringBuilder.setSpan(new StyleSpan(1), indexOf$default, obj3.length() + indexOf$default, 33);
                                textView.setText(spannableStringBuilder);
                            } else {
                                textView.setText((CharSequence) list.get(i));
                            }
                            textView.setOnClickListener(this);
                            textView.setOnLongClickListener(this);
                            FlowLayout flowLayout2 = this.suggestionView;
                            if (flowLayout2 == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("suggestionView");
                            }
                            flowLayout2.addView(textView);
                            i++;
                        } else {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                    }
                    throw new TypeCastException("null cannot be cast to non-null type android.widget.TextView");
                }
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
    }

    public void setQuickSearchVisible(boolean z) {
        ViewGroup viewGroup;
        if (z) {
            viewGroup = this.quickSearchView;
            if (viewGroup == null) {
                Intrinsics.throwUninitializedPropertyAccessException("quickSearchView");
            }
            viewGroup.setVisibility(0);
            return;
        }
        viewGroup = this.quickSearchView;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("quickSearchView");
        }
        viewGroup.setVisibility(8);
    }

    private final void onFilter(String str, InlineAutocompleteEditText inlineAutocompleteEditText) {
        if (isVisible()) {
            this.autoCompleteInProgress = true;
            Result autocomplete = this.autoCompleteProvider.autocomplete(str);
            if (inlineAutocompleteEditText != null) {
                inlineAutocompleteEditText.applyAutocompleteResult(new AutocompleteResult(autocomplete.getText(), autocomplete.getSource(), autocomplete.getSize(), new UrlInputFragment$onFilter$1(autocomplete)));
            }
            this.autoCompleteInProgress = false;
        }
    }

    private final void onTextChange(String str, String str2) {
        if (!this.autoCompleteInProgress) {
            if (this.allowSuggestion) {
                Presenter presenter = this.presenter;
                if (presenter == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("presenter");
                }
                presenter.onInput(str, detectThrottle());
            }
            int i = TextUtils.isEmpty((CharSequence) str) ? 8 : 0;
            View view = this.clearView;
            if (view == null) {
                Intrinsics.throwUninitializedPropertyAccessException("clearView");
            }
            view.setVisibility(i);
        }
    }

    private final boolean detectThrottle() {
        long currentTimeMillis = System.currentTimeMillis();
        boolean z = currentTimeMillis - this.lastRequestTime < ((long) 300);
        this.lastRequestTime = currentTimeMillis;
        return z;
    }

    private final void search(String str) {
        CharSequence charSequence = str;
        int i = 1;
        int length = charSequence.length() - 1;
        int i2 = 0;
        Object obj = null;
        while (i2 <= length) {
            Object obj2 = charSequence.charAt(obj == null ? i2 : length) <= ' ' ? 1 : null;
            if (obj == null) {
                if (obj2 == null) {
                    obj = 1;
                } else {
                    i2++;
                }
            } else if (obj2 == null) {
                break;
            } else {
                length--;
            }
        }
        if (charSequence.subSequence(i2, length + 1).toString().length() != 0) {
            i = 0;
        }
        if (i == 0) {
            Object normalize;
            InlineAutocompleteEditText inlineAutocompleteEditText = this.urlView;
            if (inlineAutocompleteEditText == null) {
                Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }
            ViewUtils.hideKeyboard(inlineAutocompleteEditText);
            if (SupportUtils.isUrl(str)) {
                normalize = SupportUtils.normalize(str);
            } else {
                normalize = SearchUtils.createSearchUrl(getContext(), str);
            }
            Intrinsics.checkExpressionValueIsNotNull(normalize, "url");
            if (openUrl(normalize)) {
                TelemetryWrapper.addNewTabFromHome();
            }
        }
    }
}
