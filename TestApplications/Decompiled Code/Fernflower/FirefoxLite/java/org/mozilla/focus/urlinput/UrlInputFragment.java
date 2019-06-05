package org.mozilla.focus.urlinput;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.webkit.URLUtil;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KDeclarationContainer;
import kotlin.text.StringsKt;
import mozilla.components.browser.domains.DomainAutoCompleteProvider;
import mozilla.components.ui.autocomplete.InlineAutocompleteEditText;
import org.mozilla.focus.Inject;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.search.SearchEngineManager;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.SearchUtils;
import org.mozilla.focus.utils.SupportUtils;
import org.mozilla.focus.utils.ViewUtils;
import org.mozilla.focus.web.WebViewProvider;
import org.mozilla.focus.widget.FlowLayout;
import org.mozilla.focus.widget.FragmentListener;
import org.mozilla.rocket.urlinput.QuickSearch;
import org.mozilla.rocket.urlinput.QuickSearchAdapter;

public final class UrlInputFragment extends Fragment implements OnClickListener, OnLongClickListener, ScreenNavigator.UrlInputScreen, UrlInputContract.View {
   public static final UrlInputFragment.Companion Companion = new UrlInputFragment.Companion((DefaultConstructorMarker)null);
   private HashMap _$_findViewCache;
   private boolean allowSuggestion;
   private boolean autoCompleteInProgress;
   private final DomainAutoCompleteProvider autoCompleteProvider = new DomainAutoCompleteProvider();
   private View clearView;
   private View dismissView;
   private long lastRequestTime;
   private UrlInputContract.Presenter presenter;
   private RecyclerView quickSearchRecyclerView;
   private ViewGroup quickSearchView;
   private FlowLayout suggestionView;
   private InlineAutocompleteEditText urlView;

   // $FF: synthetic method
   public static final InlineAutocompleteEditText access$getUrlView$p(UrlInputFragment var0) {
      InlineAutocompleteEditText var1 = var0.urlView;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("urlView");
      }

      return var1;
   }

   public static final UrlInputFragment create(String var0, String var1, boolean var2) {
      return Companion.create(var0, var1, var2);
   }

   private final boolean detectThrottle() {
      long var1 = System.currentTimeMillis();
      boolean var3;
      if (var1 - this.lastRequestTime < (long)300) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.lastRequestTime = var1;
      return var3;
   }

   private final void dismiss() {
      FragmentActivity var1 = this.getActivity();
      if (var1 instanceof FragmentListener) {
         ((FragmentListener)var1).onNotified((Fragment)this, FragmentListener.TYPE.DISMISS_URL_INPUT, true);
      }

   }

   private final void initByArguments() {
      Bundle var1 = this.getArguments();
      boolean var2 = false;
      if (var1 != null && var1.containsKey("url")) {
         String var3 = var1.getString("url");
         InlineAutocompleteEditText var4 = this.urlView;
         if (var4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("urlView");
         }

         CharSequence var6 = (CharSequence)var3;
         var4.setText(var6);
         View var7 = this.clearView;
         if (var7 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("clearView");
         }

         byte var5;
         if (TextUtils.isEmpty(var6)) {
            var5 = 8;
         } else {
            var5 = 0;
         }

         var7.setVisibility(var5);
      }

      if (var1 != null) {
         var2 = var1.getBoolean("allow_suggestion", true);
      }

      this.allowSuggestion = var2;
   }

   private final void initQuickSearch(View var1) {
      View var2 = var1.findViewById(2131296579);
      Intrinsics.checkExpressionValueIsNotNull(var2, "view.findViewById(R.id.quick_search_container)");
      this.quickSearchView = (ViewGroup)var2;
      var1 = var1.findViewById(2131296581);
      Intrinsics.checkExpressionValueIsNotNull(var1, "view.findViewById(R.id.quick_search_recycler_view)");
      this.quickSearchRecyclerView = (RecyclerView)var1;
      RecyclerView var3 = this.quickSearchRecyclerView;
      if (var3 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("quickSearchRecyclerView");
      }

      var3.setLayoutManager((RecyclerView.LayoutManager)(new LinearLayoutManager(this.getContext(), 0, false)));
      final QuickSearchAdapter var4 = new QuickSearchAdapter((Function1)(new Function1() {
         public final void invoke(QuickSearch var1) {
            Intrinsics.checkParameterIsNotNull(var1, "quickSearch");
            if (TextUtils.isEmpty((CharSequence)UrlInputFragment.access$getUrlView$p(UrlInputFragment.this).getText())) {
               UrlInputFragment.this.openUrl(var1.getHomeUrl());
            } else {
               UrlInputFragment.this.openUrl(var1.generateLink(UrlInputFragment.access$getUrlView$p(UrlInputFragment.this).getOriginalText()));
            }

            TelemetryWrapper.clickQuickSearchEngine(var1.getName());
         }
      }));
      RecyclerView var5 = this.quickSearchRecyclerView;
      if (var5 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("quickSearchRecyclerView");
      }

      var5.setAdapter((RecyclerView.Adapter)var4);
      Inject.obtainQuickSearchViewModel(this.getActivity()).getQuickSearchObservable().observe(this.getViewLifecycleOwner(), (Observer)(new Observer() {
         public final void onChanged(ArrayList var1) {
            var4.submitList((List)var1);
         }
      }));
   }

   private final void onCommit() {
      InlineAutocompleteEditText var1 = this.urlView;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("urlView");
      }

      String var4 = var1.getAutocompleteResult().getFormattedText();
      boolean var2;
      if (((CharSequence)var4).length() == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      label31: {
         if (!var2) {
            InlineAutocompleteEditText var3 = this.urlView;
            if (var3 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }

            if (URLUtil.isValidUrl(String.valueOf(var3.getText()))) {
               break label31;
            }
         }

         var1 = this.urlView;
         if (var1 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("urlView");
         }

         var4 = String.valueOf(var1.getText());
      }

      this.search(var4);
      TelemetryWrapper.urlBarEvent(SupportUtils.isUrl(var4), false);
   }

   private final void onFilter(String var1, InlineAutocompleteEditText var2) {
      if (this.isVisible()) {
         this.autoCompleteInProgress = true;
         final DomainAutoCompleteProvider.Result var3 = this.autoCompleteProvider.autocomplete(var1);
         if (var2 != null) {
            var2.applyAutocompleteResult(new InlineAutocompleteEditText.AutocompleteResult(var3.getText(), var3.getSource(), var3.getSize(), (Function1)(new Function1() {
               public final String invoke(String var1) {
                  Intrinsics.checkParameterIsNotNull(var1, "it");
                  return var3.getUrl();
               }
            })));
         }

         this.autoCompleteInProgress = false;
      }
   }

   private final void onSuggestionClicked(CharSequence var1) {
      this.search(var1.toString());
      TelemetryWrapper.urlBarEvent(SupportUtils.isUrl(var1.toString()), true);
   }

   private final void onTextChange(String var1, String var2) {
      if (!this.autoCompleteInProgress) {
         if (this.allowSuggestion) {
            UrlInputContract.Presenter var5 = this.presenter;
            if (var5 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("presenter");
            }

            var5.onInput((CharSequence)var1, this.detectThrottle());
         }

         byte var3;
         if (TextUtils.isEmpty((CharSequence)var1)) {
            var3 = 8;
         } else {
            var3 = 0;
         }

         View var4 = this.clearView;
         if (var4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("clearView");
         }

         var4.setVisibility(var3);
      }
   }

   private final boolean openUrl(String var1) {
      Bundle var2 = this.getArguments();
      boolean var3;
      if (var2 != null && var2.containsKey("parent_frag_tag")) {
         var3 = Intrinsics.areEqual("home_screen", var2.getString("parent_frag_tag"));
      } else {
         var3 = false;
      }

      FragmentActivity var6 = this.getActivity();
      boolean var4 = var6 instanceof FragmentListener;
      if (var4) {
         if (!var4) {
            var6 = null;
         }

         FragmentListener var5 = (FragmentListener)var6;
         FragmentListener.TYPE var7;
         if (var3) {
            var7 = FragmentListener.TYPE.OPEN_URL_IN_NEW_TAB;
         } else {
            var7 = FragmentListener.TYPE.OPEN_URL_IN_CURRENT_TAB;
         }

         if (var5 != null) {
            var5.onNotified((Fragment)this, var7, var1);
         }
      }

      return var3;
   }

   private final void search(String var1) {
      CharSequence var2 = (CharSequence)var1;
      int var3 = var2.length();
      boolean var4 = true;
      --var3;
      int var5 = 0;
      boolean var6 = false;

      while(var5 <= var3) {
         int var7;
         if (!var6) {
            var7 = var5;
         } else {
            var7 = var3;
         }

         boolean var10;
         if (var2.charAt(var7) <= ' ') {
            var10 = true;
         } else {
            var10 = false;
         }

         if (!var6) {
            if (!var10) {
               var6 = true;
            } else {
               ++var5;
            }
         } else {
            if (!var10) {
               break;
            }

            --var3;
         }
      }

      boolean var9;
      if (((CharSequence)var2.subSequence(var5, var3 + 1).toString()).length() == 0) {
         var9 = var4;
      } else {
         var9 = false;
      }

      if (!var9) {
         InlineAutocompleteEditText var8 = this.urlView;
         if (var8 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("urlView");
         }

         ViewUtils.hideKeyboard((View)var8);
         if (SupportUtils.isUrl(var1)) {
            var1 = SupportUtils.normalize(var1);
         } else {
            var1 = SearchUtils.createSearchUrl(this.getContext(), var1);
         }

         Intrinsics.checkExpressionValueIsNotNull(var1, "url");
         if (this.openUrl(var1)) {
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

   public void onClick(View var1) {
      Intrinsics.checkParameterIsNotNull(var1, "view");
      int var2 = var1.getId();
      if (var2 != 2131296368) {
         if (var2 != 2131296410) {
            if (var2 != 2131296676) {
               throw (Throwable)(new IllegalStateException("Unhandled view in onClick()"));
            }

            CharSequence var3 = ((TextView)var1).getText();
            Intrinsics.checkExpressionValueIsNotNull(var3, "(view as TextView).text");
            this.onSuggestionClicked(var3);
         } else {
            this.dismiss();
            TelemetryWrapper.searchDismiss();
         }
      } else {
         InlineAutocompleteEditText var4 = this.urlView;
         if (var4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("urlView");
         }

         var4.setText((CharSequence)"");
         var4 = this.urlView;
         if (var4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("urlView");
         }

         var4.requestFocus();
         TelemetryWrapper.searchClear();
      }

   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      String var3 = WebViewProvider.getUserAgentString((Context)this.getActivity());
      this.presenter = (UrlInputContract.Presenter)(new UrlInputPresenter(SearchEngineManager.getInstance().getDefaultSearchEngine((Context)this.getActivity()), var3));
      Context var2 = this.getContext();
      if (var2 != null) {
         DomainAutoCompleteProvider var4 = this.autoCompleteProvider;
         Intrinsics.checkExpressionValueIsNotNull(var2, "it");
         var2 = var2.getApplicationContext();
         Intrinsics.checkExpressionValueIsNotNull(var2, "it.applicationContext");
         DomainAutoCompleteProvider.initialize$default(var4, var2, true, false, false, 8, (Object)null);
      }

   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      Intrinsics.checkParameterIsNotNull(var1, "inflater");
      View var4 = var1.inflate(2131492975, var2, false);
      View var5 = var4.findViewById(2131296410);
      Intrinsics.checkExpressionValueIsNotNull(var5, "view.findViewById(R.id.dismiss)");
      this.dismissView = var5;
      View var7 = this.dismissView;
      if (var7 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("dismissView");
      }

      OnClickListener var6 = (OnClickListener)this;
      var7.setOnClickListener(var6);
      var7 = var4.findViewById(2131296368);
      Intrinsics.checkExpressionValueIsNotNull(var7, "view.findViewById(R.id.clear)");
      this.clearView = var7;
      var7 = this.clearView;
      if (var7 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("clearView");
      }

      var7.setOnClickListener(var6);
      var5 = var4.findViewById(2131296633);
      if (var5 != null) {
         this.suggestionView = (FlowLayout)var5;
         var5 = var4.findViewById(2131296714);
         if (var5 != null) {
            this.urlView = (InlineAutocompleteEditText)var5;
            InlineAutocompleteEditText var10 = this.urlView;
            if (var10 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }

            UrlInputFragment var8 = (UrlInputFragment)this;
            var10.setOnTextChangeListener((Function2)(new Function2(var8) {
               public final String getName() {
                  return "onTextChange";
               }

               public final KDeclarationContainer getOwner() {
                  return Reflection.getOrCreateKotlinClass(UrlInputFragment.class);
               }

               public final String getSignature() {
                  return "onTextChange(Ljava/lang/String;Ljava/lang/String;)V";
               }

               public final void invoke(String var1, String var2) {
                  Intrinsics.checkParameterIsNotNull(var1, "p1");
                  Intrinsics.checkParameterIsNotNull(var2, "p2");
                  ((UrlInputFragment)this.receiver).onTextChange(var1, var2);
               }
            }));
            var10 = this.urlView;
            if (var10 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }

            var10.setOnCommitListener((Function0)(new Function0(var8) {
               public final String getName() {
                  return "onCommit";
               }

               public final KDeclarationContainer getOwner() {
                  return Reflection.getOrCreateKotlinClass(UrlInputFragment.class);
               }

               public final String getSignature() {
                  return "onCommit()V";
               }

               public final void invoke() {
                  ((UrlInputFragment)this.receiver).onCommit();
               }
            }));
            var10 = this.urlView;
            if (var10 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }

            var10.setOnFocusChangeListener((OnFocusChangeListener)(new OnFocusChangeListener() {
               public final void onFocusChange(View var1, boolean var2) {
                  if (var2) {
                     ViewUtils.showKeyboard((View)UrlInputFragment.access$getUrlView$p(UrlInputFragment.this));
                  } else {
                     ViewUtils.hideKeyboard((View)UrlInputFragment.access$getUrlView$p(UrlInputFragment.this));
                  }

               }
            }));
            var10 = this.urlView;
            if (var10 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }

            var10.setOnFilterListener((Function2)(new Function2(var8) {
               public final String getName() {
                  return "onFilter";
               }

               public final KDeclarationContainer getOwner() {
                  return Reflection.getOrCreateKotlinClass(UrlInputFragment.class);
               }

               public final String getSignature() {
                  return "onFilter(Ljava/lang/String;Lmozilla/components/ui/autocomplete/InlineAutocompleteEditText;)V";
               }

               public final void invoke(String var1, InlineAutocompleteEditText var2) {
                  Intrinsics.checkParameterIsNotNull(var1, "p1");
                  ((UrlInputFragment)this.receiver).onFilter(var1, var2);
               }
            }));
            var10 = this.urlView;
            if (var10 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }

            InlineAutocompleteEditText var9 = this.urlView;
            if (var9 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("urlView");
            }

            var10.setImeOptions(var9.getImeOptions() | 16777216);
            this.initByArguments();
            Intrinsics.checkExpressionValueIsNotNull(var4, "view");
            this.initQuickSearch(var4);
            return var4;
         } else {
            throw new TypeCastException("null cannot be cast to non-null type mozilla.components.ui.autocomplete.InlineAutocompleteEditText");
         }
      } else {
         throw new TypeCastException("null cannot be cast to non-null type org.mozilla.focus.widget.FlowLayout");
      }
   }

   // $FF: synthetic method
   public void onDestroyView() {
      super.onDestroyView();
      this._$_clearFindViewByIdCache();
   }

   public boolean onLongClick(View var1) {
      Intrinsics.checkParameterIsNotNull(var1, "view");
      if (var1.getId() != 2131296676) {
         return false;
      } else {
         this.setUrlText(((TextView)var1).getText());
         TelemetryWrapper.searchSuggestionLongClick();
         return true;
      }
   }

   public void onStart() {
      super.onStart();
      UrlInputContract.Presenter var1 = this.presenter;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("presenter");
      }

      var1.setView((UrlInputContract.View)this);
      InlineAutocompleteEditText var2 = this.urlView;
      if (var2 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("urlView");
      }

      var2.requestFocus();
   }

   public void onStop() {
      super.onStop();
      UrlInputContract.Presenter var1 = this.presenter;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("presenter");
      }

      var1.setView((UrlInputContract.View)null);
   }

   public void setQuickSearchVisible(boolean var1) {
      ViewGroup var2;
      if (var1) {
         var2 = this.quickSearchView;
         if (var2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("quickSearchView");
         }

         var2.setVisibility(0);
      } else {
         var2 = this.quickSearchView;
         if (var2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("quickSearchView");
         }

         var2.setVisibility(8);
      }

   }

   public void setSuggestions(List var1) {
      FlowLayout var2 = this.suggestionView;
      if (var2 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("suggestionView");
      }

      var2.removeAllViews();
      if (var1 != null) {
         InlineAutocompleteEditText var11 = this.urlView;
         if (var11 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("urlView");
         }

         CharSequence var12 = (CharSequence)var11.getOriginalText();
         int var3 = var12.length();
         byte var4 = 0;
         --var3;
         int var5 = 0;
         boolean var6 = false;

         while(var5 <= var3) {
            int var7;
            if (!var6) {
               var7 = var5;
            } else {
               var7 = var3;
            }

            boolean var15;
            if (var12.charAt(var7) <= ' ') {
               var15 = true;
            } else {
               var15 = false;
            }

            if (!var6) {
               if (!var15) {
                  var6 = true;
               } else {
                  ++var5;
               }
            } else {
               if (!var15) {
                  break;
               }

               --var3;
            }
         }

         String var13 = var12.subSequence(var5, var3 + 1).toString();
         Locale var8 = Locale.getDefault();
         Intrinsics.checkExpressionValueIsNotNull(var8, "Locale.getDefault()");
         if (var13 != null) {
            var13 = var13.toLowerCase(var8);
            Intrinsics.checkExpressionValueIsNotNull(var13, "(this as java.lang.String).toLowerCase(locale)");
            var5 = ((Collection)var1).size();

            for(var3 = var4; var3 < var5; ++var3) {
               View var16 = View.inflate(this.getContext(), 2131493020, (ViewGroup)null);
               if (var16 == null) {
                  throw new TypeCastException("null cannot be cast to non-null type android.widget.TextView");
               }

               TextView var17 = (TextView)var16;
               String var9 = var1.get(var3).toString();
               Locale var10 = Locale.getDefault();
               Intrinsics.checkExpressionValueIsNotNull(var10, "Locale.getDefault()");
               if (var9 == null) {
                  throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
               }

               String var18 = var9.toLowerCase(var10);
               Intrinsics.checkExpressionValueIsNotNull(var18, "(this as java.lang.String).toLowerCase(locale)");
               int var14 = StringsKt.indexOf$default((CharSequence)var18, var13, 0, false, 6, (Object)null);
               if (var14 != -1) {
                  SpannableStringBuilder var19 = new SpannableStringBuilder((CharSequence)var1.get(var3));
                  var19.setSpan(new StyleSpan(1), var14, var13.length() + var14, 33);
                  var17.setText((CharSequence)var19);
               } else {
                  var17.setText((CharSequence)var1.get(var3));
               }

               var17.setOnClickListener((OnClickListener)this);
               var17.setOnLongClickListener((OnLongClickListener)this);
               FlowLayout var20 = this.suggestionView;
               if (var20 == null) {
                  Intrinsics.throwUninitializedPropertyAccessException("suggestionView");
               }

               var20.addView((View)var17);
            }

         } else {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
         }
      }
   }

   public void setUrlText(CharSequence var1) {
      if (var1 != null) {
         InlineAutocompleteEditText var2 = this.urlView;
         if (var2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("urlView");
         }

         var2.setText(var1);
         var2 = this.urlView;
         if (var2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("urlView");
         }

         var2.setSelection(var1.length());
      }

   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      public final UrlInputFragment create(String var1, String var2, boolean var3) {
         Bundle var4 = new Bundle();
         var4.putString("url", var1);
         var4.putString("parent_frag_tag", var2);
         var4.putBoolean("allow_suggestion", var3);
         UrlInputFragment var5 = new UrlInputFragment();
         var5.setArguments(var4);
         return var5;
      }
   }
}
