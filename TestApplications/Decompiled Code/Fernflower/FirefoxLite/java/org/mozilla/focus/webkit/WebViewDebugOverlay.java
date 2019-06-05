package org.mozilla.focus.webkit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils.TruncateAt;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.webkit.WebBackForwardList;
import android.webkit.WebHistoryItem;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import org.mozilla.urlutils.UrlUtils;

public class WebViewDebugOverlay {
   private LinearLayout backForwardList;
   private LinearLayout callbackList;
   private DrawerLayout drawerLayout;
   private LinearLayout viewTreeList;
   private Runnable viewTreeUpdateRunnable;
   private WebView webView;

   private WebViewDebugOverlay(Context var1) {
      this.viewTreeUpdateRunnable = new Runnable() {
         public void run() {
            if (WebViewDebugOverlay.this.webView != null && WebViewDebugOverlay.this.webView.isAttachedToWindow()) {
               WebViewDebugOverlay.this.viewTreeList.removeAllViews();
               WebViewDebugOverlay.this.updateViewTreeList(WebViewDebugOverlay.this.webView, 0);
            }

         }
      };
      if (this.isEnable()) {
         LinearLayout var2 = new LinearLayout(var1);
         var2.setOrientation(1);
         var2.setBackgroundColor(Color.parseColor("#99000000"));
         this.insertSectionTitle("WebViewClient", var2);
         var2.addView(this.createCallbackList(var1), new LayoutParams(-1, -2));
         this.insertSectionTitle("BackForwardList", var2);
         var2.addView(this.createBackForwardList(var1), new LayoutParams(-1, -2));
         this.insertSectionTitle("View tree", var2);
         var2.addView(this.createViewTreeList(var1), new LayoutParams(-1, -1));
         this.drawerLayout = new WebViewDebugOverlay.FullScreenDrawerLayout(var1);
         this.drawerLayout.setScrimColor(0);
         View var3 = new View(var1) {
            View target;

            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouchEvent(MotionEvent var1) {
               if (this.target != null) {
                  boolean var2 = this.target.onTouchEvent(var1);
                  if (var1.getAction() == 1 || var1.getAction() == 3) {
                     this.target = null;
                  }

                  return var2;
               } else {
                  if (WebViewDebugOverlay.this.webView.getChildCount() != 0) {
                     for(int var3 = 0; var3 < WebViewDebugOverlay.this.webView.getChildCount(); ++var3) {
                        View var4 = WebViewDebugOverlay.this.webView.getChildAt(var3);
                        if (var4 != WebViewDebugOverlay.this.drawerLayout && var4.onTouchEvent(var1)) {
                           this.target = var4;
                           return true;
                        }
                     }
                  }

                  return WebViewDebugOverlay.this.webView.onTouchEvent(var1);
               }
            }
         };
         this.drawerLayout.addView(var3, new android.view.ViewGroup.LayoutParams(-1, -1));
         DrawerLayout.LayoutParams var4 = new DrawerLayout.LayoutParams(-1, -1);
         var4.gravity = 8388611;
         var2.setLayoutParams(var4);
         this.drawerLayout.addView(var2);
      }

   }

   // $FF: synthetic method
   WebViewDebugOverlay(Context var1, Object var2) {
      this(var1);
   }

   public static WebViewDebugOverlay create(Context var0) {
      return (WebViewDebugOverlay)(isSupport() ? new WebViewDebugOverlay(var0) : new WebViewDebugOverlay.NoOpOverlay(var0));
   }

   private View createBackForwardList(Context var1) {
      this.backForwardList = new LinearLayout(var1);
      this.backForwardList.setOrientation(1);
      return this.backForwardList;
   }

   private View createCallbackList(Context var1) {
      this.callbackList = new LinearLayout(var1);
      this.callbackList.setOrientation(1);
      return this.callbackList;
   }

   private View createViewTreeList(Context var1) {
      this.viewTreeList = new LinearLayout(var1);
      this.viewTreeList.setOrientation(1);
      return this.viewTreeList;
   }

   private void insertCallback(String var1, int var2) {
      this.insertText(var1, var2, this.callbackList);
   }

   private void insertDivider(LinearLayout var1) {
      View var2 = new View(this.webView.getContext());
      var2.setBackgroundColor(-1);
      var1.addView(var2, new android.view.ViewGroup.LayoutParams(-1, 1));
   }

   private void insertHistory(String var1, int var2) {
      this.insertText(var1, var2, this.backForwardList);
   }

   private void insertSectionTitle(String var1, LinearLayout var2) {
      if (this.isEnable()) {
         TextView var3 = new TextView(var2.getContext());
         var3.setText(var1);
         var3.setTextColor(-1);
         var3.setTextSize(1, 16.0F);
         var3.setTypeface(Typeface.MONOSPACE);
         float var4 = var3.getResources().getDisplayMetrics().density;
         MarginLayoutParams var6 = new MarginLayoutParams(-1, -2);
         int var5;
         if (var2.getChildCount() == 0) {
            var5 = 0;
         } else {
            var5 = (int)(var4 * 16.0F);
         }

         var6.topMargin = var5;
         var2.addView(var3, var6);
      }

   }

   private void insertText(String var1, int var2, LinearLayout var3) {
      AppCompatTextView var4 = new AppCompatTextView(this.callbackList.getContext());
      var4.setTextColor(var2);
      var4.setTextSize(1, 12.0F);
      var4.setText(var1);
      var4.setMaxLines(1);
      var4.setSingleLine();
      var4.setEllipsize(TruncateAt.END);
      var4.setTypeface(Typeface.MONOSPACE);
      var3.addView(var4);
   }

   public static boolean isSupport() {
      return false;
   }

   private void updateViewTree() {
      if (this.isEnable()) {
         this.webView.removeCallbacks(this.viewTreeUpdateRunnable);
         this.viewTreeList.removeAllViews();
         this.insertText("updating...", -3355444, this.viewTreeList);
         this.webView.postDelayed(this.viewTreeUpdateRunnable, 1500L);
      }

   }

   private void updateViewTreeList(View var1, int var2) {
      if (var1 instanceof ViewGroup) {
         StringBuilder var3 = new StringBuilder();
         byte var4 = 0;
         int var5 = 0;

         while(var5 < var2) {
            int var6 = var2 - 1;
            if (var5 < var6) {
               var3.append("  ");
               ++var5;
            } else if (var5 == var6) {
               var3.append("|-");
               var5 += 2;
            } else {
               var3.append("--");
               var5 += 2;
            }
         }

         var3.append(var1.getClass().getSimpleName());
         if (var1 instanceof WebView) {
            WebHistoryItem var7 = ((WebView)var1).copyBackForwardList().getCurrentItem();
            if (var7 != null) {
               var3.append("(");
               var3.append(var7.getOriginalUrl());
               var3.append(")");
            }
         }

         this.insertText(var3.toString(), -3355444, this.viewTreeList);
         if (var1 != this.viewTreeList) {
            ViewGroup var8 = (ViewGroup)var1;

            for(var5 = var4; var5 < var8.getChildCount(); ++var5) {
               this.updateViewTreeList(var8.getChildAt(var5), var2 + 1);
            }

         }
      }
   }

   public void bindWebView(WebView var1) {
      if (this.isEnable()) {
         this.webView = var1;
         var1.addView(this.drawerLayout, new android.view.ViewGroup.LayoutParams(-1, -1));
         this.updateHistory();
         this.drawerLayout.setElevation(100.0F);
      }

   }

   public boolean isEnable() {
      return true;
   }

   public void onLoadUrlCalled() {
      if (this.isEnable()) {
         this.callbackList.removeAllViews();
      }

   }

   public void onWebViewScrolled(int var1, int var2) {
      if (this.isEnable()) {
         this.drawerLayout.setTranslationY((float)var2);
         this.drawerLayout.setTranslationX((float)var1);
      }

   }

   public void recordLifecycle(String var1, boolean var2) {
      if (this.isEnable()) {
         while(true) {
            if (this.callbackList.getChildCount() + 1 <= 10) {
               if (var2 && this.callbackList.getChildCount() != 0) {
                  this.insertDivider(this.callbackList);
               }

               this.insertCallback(var1.replace("https://", ""), -3355444);
               break;
            }

            this.callbackList.removeViewAt(0);
         }
      }

   }

   public void updateHistory() {
      if (this.isEnable()) {
         this.backForwardList.removeAllViews();
         WebBackForwardList var1 = this.webView.copyBackForwardList();
         int var2 = var1.getSize();
         int var3 = var1.getCurrentIndex();
         if (var3 < 0) {
            return;
         }

         StringBuilder var4 = new StringBuilder();
         var4.append("size:");
         var4.append(var2);
         var4.append(", curr:");
         var4.append(var3);
         this.insertHistory(var4.toString(), -1);
         int var5 = var3 - 2;
         int var6 = var3 + 2;
         int var7 = var6;
         var3 = var5;
         if (var5 < 0) {
            var7 = var6 - var5;
            var3 = 0;
         }

         var6 = var7;
         var5 = var3;
         if (var7 >= var2) {
            var5 = Math.max(0, var3 - (var7 - var2 + 1));
            var6 = var2 - 1;
         }

         var7 = var5;
         if (var5 != 0) {
            this.insertHistory("...", -1);
            var7 = var5;
         }

         while(var7 <= var6) {
            WebHistoryItem var8 = var1.getItemAtIndex(var7);
            String var9 = var8.getOriginalUrl().replaceAll("https://", "");
            String var10 = var8.getUrl().replaceAll("https://", "");
            if (var1.getCurrentIndex() == var7) {
               if (!UrlUtils.isInternalErrorURL(var8.getOriginalUrl()) && !UrlUtils.isInternalErrorURL(var8.getUrl())) {
                  var3 = -16711936;
               } else {
                  var3 = -65536;
               }
            } else {
               var3 = -3355444;
            }

            StringBuilder var11 = new StringBuilder();
            var11.append(var7);
            var11.append(": ");
            var11.append(var9);
            this.insertHistory(var11.toString(), var3);
            if (!var9.equals(var10)) {
               StringBuilder var12 = new StringBuilder();
               var12.append("-> ");
               var12.append(var10);
               this.insertHistory(var12.toString(), var3);
            }

            this.insertDivider(this.backForwardList);
            ++var7;
         }

         this.updateViewTree();
      }

   }

   private static class FullScreenDrawerLayout extends DrawerLayout {
      public FullScreenDrawerLayout(Context var1) {
         super(var1);
      }

      int getDrawerViewAbsoluteGravity(View var1) {
         return GravityCompat.getAbsoluteGravity(((DrawerLayout.LayoutParams)var1.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(this));
      }

      boolean isContentView(View var1) {
         boolean var2;
         if (((DrawerLayout.LayoutParams)var1.getLayoutParams()).gravity == 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      boolean isDrawerView(View var1) {
         int var2 = GravityCompat.getAbsoluteGravity(((DrawerLayout.LayoutParams)var1.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(var1));
         boolean var3;
         if ((var2 & 3) == 0 && (var2 & 5) == 0) {
            var3 = false;
         } else {
            var3 = true;
         }

         return var3;
      }

      protected void onMeasure(int var1, int var2) {
         int var3 = MeasureSpec.getMode(var1);
         int var4 = MeasureSpec.getMode(var2);
         int var5 = MeasureSpec.getSize(var1);
         int var6 = MeasureSpec.getSize(var2);
         if (var3 == 1073741824 && var4 == 1073741824) {
            this.setMeasuredDimension(var5, var6);
            int var7 = this.getChildCount();
            int var8 = 0;
            boolean var12 = false;

            for(boolean var13 = false; var8 < var7; ++var8) {
               View var9 = this.getChildAt(var8);
               if (var9.getVisibility() != 8) {
                  DrawerLayout.LayoutParams var10 = (DrawerLayout.LayoutParams)var9.getLayoutParams();
                  if (this.isContentView(var9)) {
                     var9.measure(MeasureSpec.makeMeasureSpec(var5 - var10.leftMargin - var10.rightMargin, 1073741824), MeasureSpec.makeMeasureSpec(var6 - var10.topMargin - var10.bottomMargin, 1073741824));
                  } else {
                     if (!this.isDrawerView(var9)) {
                        StringBuilder var14 = new StringBuilder();
                        var14.append("Child ");
                        var14.append(var9);
                        var14.append(" at index ");
                        var14.append(var8);
                        var14.append(" does not have a valid layout_gravity - must be Gravity.LEFT, Gravity.RIGHT or Gravity.NO_GRAVITY");
                        throw new IllegalStateException(var14.toString());
                     }

                     if (VERSION.SDK_INT >= 21) {
                        int var11 = (int)(this.getResources().getDisplayMetrics().density * 10.0F);
                        if ((int)ViewCompat.getElevation(var9) != var11) {
                           ViewCompat.setElevation(var9, (float)var11);
                        }
                     }

                     boolean var15;
                     if ((this.getDrawerViewAbsoluteGravity(var9) & 7) == 3) {
                        var15 = true;
                     } else {
                        var15 = false;
                     }

                     if (var15 && var12 || !var15 && var13) {
                        throw new IllegalStateException("Duplicate drawers on the same edge");
                     }

                     if (var15) {
                        var12 = true;
                     } else {
                        var13 = true;
                     }

                     var9.measure(getChildMeasureSpec(var1, var10.leftMargin + var10.rightMargin, var10.width), getChildMeasureSpec(var2, var10.topMargin + var10.bottomMargin, var10.height));
                  }
               }
            }

         } else {
            throw new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
         }
      }
   }

   private static class NoOpOverlay extends WebViewDebugOverlay {
      private NoOpOverlay(Context var1) {
         super(var1, null);
      }

      // $FF: synthetic method
      NoOpOverlay(Context var1, Object var2) {
         this(var1);
      }

      public boolean isEnable() {
         return false;
      }
   }
}
