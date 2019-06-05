package org.mozilla.focus.widget;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import java.util.Arrays;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.ViewUtils;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.rocket.tabs.TabViewEngineSession;

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

   public FindInPage(View var1) {
      Intrinsics.checkParameterIsNotNull(var1, "rootView");
      super();
      var1 = var1.findViewById(2131296437);
      Intrinsics.checkExpressionValueIsNotNull(var1, "rootView.findViewById(R.id.find_in_page)");
      this.container = var1;
      var1 = this.container.findViewById(2131296441);
      Intrinsics.checkExpressionValueIsNotNull(var1, "container.findViewById(R….find_in_page_query_text)");
      this.queryText = (TextView)var1;
      var1 = this.container.findViewById(2131296442);
      Intrinsics.checkExpressionValueIsNotNull(var1, "container.findViewById(R…find_in_page_result_text)");
      this.resultText = (TextView)var1;
      var1 = this.container.findViewById(2131296439);
      Intrinsics.checkExpressionValueIsNotNull(var1, "container.findViewById(R.id.find_in_page_next_btn)");
      this.nextBtn = var1;
      var1 = this.container.findViewById(2131296440);
      Intrinsics.checkExpressionValueIsNotNull(var1, "container.findViewById(R.id.find_in_page_prev_btn)");
      this.prevBtn = var1;
      var1 = this.container.findViewById(2131296438);
      Intrinsics.checkExpressionValueIsNotNull(var1, "container.findViewById(R…d.find_in_page_close_btn)");
      this.closeBtn = var1;
      String var2 = this.container.getContext().getString(2131755198);
      Intrinsics.checkExpressionValueIsNotNull(var2, "container.context.getStr…ring.find_in_page_result)");
      this.resultFormat = var2;
      var2 = this.container.getContext().getString(2131755057);
      Intrinsics.checkExpressionValueIsNotNull(var2, "container.context.getStr…lity_find_in_page_result)");
      this.accessibilityFormat = var2;
      this.initViews();
   }

   private final void initViews() {
      final Function0 var1 = new Function0() {
         public final WebView invoke() {
            Session var1 = FindInPage.this.session;
            if (var1 != null) {
               TabViewEngineSession var2 = var1.getEngineSession();
               if (var2 != null) {
                  TabView var3 = var2.getTabView();
                  if (var3 != null) {
                     if (var3 != null) {
                        return (WebView)var3;
                     }

                     throw new TypeCastException("null cannot be cast to non-null type android.webkit.WebView");
                  }
               }
            }

            return null;
         }
      };
      this.closeBtn.setOnClickListener((OnClickListener)(new OnClickListener() {
         public final void onClick(View var1) {
            FindInPage.this.hide();
         }
      }));
      this.queryText.setOnClickListener((OnClickListener)(new OnClickListener() {
         public final void onClick(View var1) {
            FindInPage.this.queryText.setCursorVisible(true);
         }
      }));
      this.prevBtn.setOnClickListener((OnClickListener)(new OnClickListener() {
         public final void onClick(View var1x) {
            WebView var2 = var1.invoke();
            if (var2 != null) {
               var2.findNext(false);
            }

            TelemetryWrapper.findInPage(TelemetryWrapper.FIND_IN_PAGE.CLICK_PREVIOUS);
         }
      }));
      this.nextBtn.setOnClickListener((OnClickListener)(new OnClickListener() {
         public final void onClick(View var1x) {
            WebView var2 = var1.invoke();
            if (var2 != null) {
               var2.findNext(true);
            }

            TelemetryWrapper.findInPage(TelemetryWrapper.FIND_IN_PAGE.CLICK_NEXT);
         }
      }));
      this.queryText.addTextChangedListener((TextWatcher)(new TextWatcher() {
         public void afterTextChanged(Editable var1x) {
         }

         public void beforeTextChanged(CharSequence var1x, int var2, int var3, int var4) {
         }

         public void onTextChanged(CharSequence var1x, int var2, int var3, int var4) {
            if (var1x != null) {
               WebView var5 = var1.invoke();
               if (var5 != null) {
                  var5.findAllAsync(var1x.toString());
               }
            }

         }
      }));
      this.queryText.setOnEditorActionListener((OnEditorActionListener)(new OnEditorActionListener() {
         public final boolean onEditorAction(TextView var1, int var2, KeyEvent var3) {
            if (var2 == 6) {
               ViewUtils.hideKeyboard((View)FindInPage.this.queryText);
               FindInPage.this.queryText.setCursorVisible(false);
            }

            return false;
         }
      }));
   }

   public final void hide() {
      if (this.container.getVisibility() == 0) {
         ViewUtils.hideKeyboard((View)this.queryText);
         this.queryText.setText((CharSequence)null);
         this.queryText.clearFocus();
         this.container.setVisibility(8);
      }
   }

   public boolean onBackPressed() {
      boolean var1;
      if (this.container.getVisibility() == 0) {
         this.hide();
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public final void onFindResultReceived(mozilla.components.browser.session.Session.FindResult var1) {
      Intrinsics.checkParameterIsNotNull(var1, "result");
      int var2 = var1.getActiveMatchOrdinal();
      int var3 = var1.getNumberOfMatches();
      if (var3 > 0) {
         ++var2;
         TextView var6 = this.resultText;
         StringCompanionObject var4 = StringCompanionObject.INSTANCE;
         String var7 = this.resultFormat;
         Object[] var5 = new Object[]{var2, var3};
         var7 = String.format(var7, Arrays.copyOf(var5, var5.length));
         Intrinsics.checkExpressionValueIsNotNull(var7, "java.lang.String.format(format, *args)");
         var6.setText((CharSequence)var7);
         var6 = this.resultText;
         var4 = StringCompanionObject.INSTANCE;
         String var8 = this.accessibilityFormat;
         Object[] var9 = new Object[]{var2, var3};
         var7 = String.format(var8, Arrays.copyOf(var9, var9.length));
         Intrinsics.checkExpressionValueIsNotNull(var7, "java.lang.String.format(format, *args)");
         var6.setContentDescription((CharSequence)var7);
      } else {
         this.resultText.setText((CharSequence)"");
         this.resultText.setContentDescription((CharSequence)"");
      }

   }

   public final void show(Session var1) {
      if (this.container.getVisibility() != 0) {
         if (var1 != null) {
            this.session = var1;
            this.container.setVisibility(0);
            (new Handler(Looper.getMainLooper())).post((Runnable)(new Runnable() {
               public final void run() {
                  FindInPage.this.queryText.requestFocus();
                  ViewUtils.showKeyboard((View)FindInPage.this.queryText);
               }
            }));
         }

      }
   }
}
