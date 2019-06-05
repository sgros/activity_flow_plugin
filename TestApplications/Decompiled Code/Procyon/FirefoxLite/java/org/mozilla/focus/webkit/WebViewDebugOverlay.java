// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.webkit;

import android.os.Build$VERSION;
import android.view.View$MeasureSpec;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.webkit.WebBackForwardList;
import org.mozilla.urlutils.UrlUtils;
import android.webkit.WebHistoryItem;
import android.view.ViewGroup;
import android.text.TextUtils$TruncateAt;
import android.support.v7.widget.AppCompatTextView;
import android.view.ViewGroup$MarginLayoutParams;
import android.graphics.Typeface;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import android.graphics.Color;
import android.view.View;
import android.content.Context;
import android.webkit.WebView;
import android.support.v4.widget.DrawerLayout;
import android.widget.LinearLayout;

public class WebViewDebugOverlay
{
    private LinearLayout backForwardList;
    private LinearLayout callbackList;
    private DrawerLayout drawerLayout;
    private LinearLayout viewTreeList;
    private Runnable viewTreeUpdateRunnable;
    private WebView webView;
    
    private WebViewDebugOverlay(final Context context) {
        this.viewTreeUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                if (WebViewDebugOverlay.this.webView != null && WebViewDebugOverlay.this.webView.isAttachedToWindow()) {
                    WebViewDebugOverlay.this.viewTreeList.removeAllViews();
                    WebViewDebugOverlay.this.updateViewTreeList((View)WebViewDebugOverlay.this.webView, 0);
                }
            }
        };
        if (this.isEnable()) {
            final LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(1);
            linearLayout.setBackgroundColor(Color.parseColor("#99000000"));
            this.insertSectionTitle("WebViewClient", linearLayout);
            linearLayout.addView(this.createCallbackList(context), (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-1, -2));
            this.insertSectionTitle("BackForwardList", linearLayout);
            linearLayout.addView(this.createBackForwardList(context), (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-1, -2));
            this.insertSectionTitle("View tree", linearLayout);
            linearLayout.addView(this.createViewTreeList(context), (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-1, -1));
            (this.drawerLayout = new FullScreenDrawerLayout(context)).setScrimColor(0);
            this.drawerLayout.addView((View)new View(context) {
                View target;
                
                @SuppressLint({ "ClickableViewAccessibility" })
                public boolean onTouchEvent(final MotionEvent motionEvent) {
                    if (this.target != null) {
                        final boolean onTouchEvent = this.target.onTouchEvent(motionEvent);
                        if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                            this.target = null;
                        }
                        return onTouchEvent;
                    }
                    if (WebViewDebugOverlay.this.webView.getChildCount() != 0) {
                        for (int i = 0; i < WebViewDebugOverlay.this.webView.getChildCount(); ++i) {
                            final View child = WebViewDebugOverlay.this.webView.getChildAt(i);
                            if (child != WebViewDebugOverlay.this.drawerLayout) {
                                if (child.onTouchEvent(motionEvent)) {
                                    this.target = child;
                                    return true;
                                }
                            }
                        }
                    }
                    return WebViewDebugOverlay.this.webView.onTouchEvent(motionEvent);
                }
            }, new ViewGroup$LayoutParams(-1, -1));
            final DrawerLayout.LayoutParams layoutParams = new DrawerLayout.LayoutParams(-1, -1);
            layoutParams.gravity = 8388611;
            linearLayout.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            this.drawerLayout.addView((View)linearLayout);
        }
    }
    
    public static WebViewDebugOverlay create(final Context context) {
        if (isSupport()) {
            return new WebViewDebugOverlay(context);
        }
        return new NoOpOverlay(context);
    }
    
    private View createBackForwardList(final Context context) {
        (this.backForwardList = new LinearLayout(context)).setOrientation(1);
        return (View)this.backForwardList;
    }
    
    private View createCallbackList(final Context context) {
        (this.callbackList = new LinearLayout(context)).setOrientation(1);
        return (View)this.callbackList;
    }
    
    private View createViewTreeList(final Context context) {
        (this.viewTreeList = new LinearLayout(context)).setOrientation(1);
        return (View)this.viewTreeList;
    }
    
    private void insertCallback(final String s, final int n) {
        this.insertText(s, n, this.callbackList);
    }
    
    private void insertDivider(final LinearLayout linearLayout) {
        final View view = new View(this.webView.getContext());
        view.setBackgroundColor(-1);
        linearLayout.addView(view, new ViewGroup$LayoutParams(-1, 1));
    }
    
    private void insertHistory(final String s, final int n) {
        this.insertText(s, n, this.backForwardList);
    }
    
    private void insertSectionTitle(final String text, final LinearLayout linearLayout) {
        if (this.isEnable()) {
            final TextView textView = new TextView(linearLayout.getContext());
            textView.setText((CharSequence)text);
            textView.setTextColor(-1);
            textView.setTextSize(1, 16.0f);
            textView.setTypeface(Typeface.MONOSPACE);
            final float density = textView.getResources().getDisplayMetrics().density;
            final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = new ViewGroup$MarginLayoutParams(-1, -2);
            int topMargin;
            if (linearLayout.getChildCount() == 0) {
                topMargin = 0;
            }
            else {
                topMargin = (int)(density * 16.0f);
            }
            viewGroup$MarginLayoutParams.topMargin = topMargin;
            linearLayout.addView((View)textView, (ViewGroup$LayoutParams)viewGroup$MarginLayoutParams);
        }
    }
    
    private void insertText(final String text, final int textColor, final LinearLayout linearLayout) {
        final AppCompatTextView appCompatTextView = new AppCompatTextView(this.callbackList.getContext());
        appCompatTextView.setTextColor(textColor);
        appCompatTextView.setTextSize(1, 12.0f);
        appCompatTextView.setText((CharSequence)text);
        appCompatTextView.setMaxLines(1);
        appCompatTextView.setSingleLine();
        appCompatTextView.setEllipsize(TextUtils$TruncateAt.END);
        appCompatTextView.setTypeface(Typeface.MONOSPACE);
        linearLayout.addView((View)appCompatTextView);
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
    
    private void updateViewTreeList(final View view, final int n) {
        if (!(view instanceof ViewGroup)) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        final int n2 = 0;
        int i = 0;
        while (i < n) {
            final int n3 = n - 1;
            if (i < n3) {
                sb.append("  ");
                ++i;
            }
            else if (i == n3) {
                sb.append("|-");
                i += 2;
            }
            else {
                sb.append("--");
                i += 2;
            }
        }
        sb.append(view.getClass().getSimpleName());
        if (view instanceof WebView) {
            final WebHistoryItem currentItem = ((WebView)view).copyBackForwardList().getCurrentItem();
            if (currentItem != null) {
                sb.append("(");
                sb.append(currentItem.getOriginalUrl());
                sb.append(")");
            }
        }
        this.insertText(sb.toString(), -3355444, this.viewTreeList);
        if (view == this.viewTreeList) {
            return;
        }
        final ViewGroup viewGroup = (ViewGroup)view;
        for (int j = n2; j < viewGroup.getChildCount(); ++j) {
            this.updateViewTreeList(viewGroup.getChildAt(j), n + 1);
        }
    }
    
    public void bindWebView(final WebView webView) {
        if (this.isEnable()) {
            (this.webView = webView).addView((View)this.drawerLayout, new ViewGroup$LayoutParams(-1, -1));
            this.updateHistory();
            this.drawerLayout.setElevation(100.0f);
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
    
    public void onWebViewScrolled(final int n, final int n2) {
        if (this.isEnable()) {
            this.drawerLayout.setTranslationY((float)n2);
            this.drawerLayout.setTranslationX((float)n);
        }
    }
    
    public void recordLifecycle(final String s, final boolean b) {
        if (this.isEnable()) {
            while (this.callbackList.getChildCount() + 1 > 10) {
                this.callbackList.removeViewAt(0);
            }
            if (b && this.callbackList.getChildCount() != 0) {
                this.insertDivider(this.callbackList);
            }
            this.insertCallback(s.replace("https://", ""), -3355444);
        }
    }
    
    public void updateHistory() {
        if (this.isEnable()) {
            this.backForwardList.removeAllViews();
            final WebBackForwardList copyBackForwardList = this.webView.copyBackForwardList();
            final int size = copyBackForwardList.getSize();
            final int currentIndex = copyBackForwardList.getCurrentIndex();
            if (currentIndex < 0) {
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("size:");
            sb.append(size);
            sb.append(", curr:");
            sb.append(currentIndex);
            this.insertHistory(sb.toString(), -1);
            final int n = currentIndex - 2;
            int n3;
            final int n2 = n3 = currentIndex + 2;
            int n4;
            if ((n4 = n) < 0) {
                n3 = n2 - n;
                n4 = 0;
            }
            int n5 = n3;
            int max = n4;
            if (n3 >= size) {
                max = Math.max(0, n4 - (n3 - size + 1));
                n5 = size - 1;
            }
            int i;
            if ((i = max) != 0) {
                this.insertHistory("...", -1);
                i = max;
            }
            while (i <= n5) {
                final WebHistoryItem itemAtIndex = copyBackForwardList.getItemAtIndex(i);
                final String replaceAll = itemAtIndex.getOriginalUrl().replaceAll("https://", "");
                final String replaceAll2 = itemAtIndex.getUrl().replaceAll("https://", "");
                int n6;
                if (copyBackForwardList.getCurrentIndex() == i) {
                    if (!UrlUtils.isInternalErrorURL(itemAtIndex.getOriginalUrl()) && !UrlUtils.isInternalErrorURL(itemAtIndex.getUrl())) {
                        n6 = -16711936;
                    }
                    else {
                        n6 = -65536;
                    }
                }
                else {
                    n6 = -3355444;
                }
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(i);
                sb2.append(": ");
                sb2.append(replaceAll);
                this.insertHistory(sb2.toString(), n6);
                if (!replaceAll.equals(replaceAll2)) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("-> ");
                    sb3.append(replaceAll2);
                    this.insertHistory(sb3.toString(), n6);
                }
                this.insertDivider(this.backForwardList);
                ++i;
            }
            this.updateViewTree();
        }
    }
    
    private static class FullScreenDrawerLayout extends DrawerLayout
    {
        public FullScreenDrawerLayout(final Context context) {
            super(context);
        }
        
        @Override
        int getDrawerViewAbsoluteGravity(final View view) {
            return GravityCompat.getAbsoluteGravity(((LayoutParams)view.getLayoutParams()).gravity, ViewCompat.getLayoutDirection((View)this));
        }
        
        @Override
        boolean isContentView(final View view) {
            return ((LayoutParams)view.getLayoutParams()).gravity == 0;
        }
        
        @Override
        boolean isDrawerView(final View view) {
            final int absoluteGravity = GravityCompat.getAbsoluteGravity(((LayoutParams)view.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(view));
            return (absoluteGravity & 0x3) != 0x0 || (absoluteGravity & 0x5) != 0x0;
        }
        
        @Override
        protected void onMeasure(final int n, final int n2) {
            final int mode = View$MeasureSpec.getMode(n);
            final int mode2 = View$MeasureSpec.getMode(n2);
            final int size = View$MeasureSpec.getSize(n);
            final int size2 = View$MeasureSpec.getSize(n2);
            if (mode == 1073741824 && mode2 == 1073741824) {
                this.setMeasuredDimension(size, size2);
                final int childCount = this.getChildCount();
                int i = 0;
                int n3 = 0;
                int n4 = 0;
                while (i < childCount) {
                    final View child = this.getChildAt(i);
                    if (child.getVisibility() != 8) {
                        final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                        if (this.isContentView(child)) {
                            child.measure(View$MeasureSpec.makeMeasureSpec(size - layoutParams.leftMargin - layoutParams.rightMargin, 1073741824), View$MeasureSpec.makeMeasureSpec(size2 - layoutParams.topMargin - layoutParams.bottomMargin, 1073741824));
                        }
                        else {
                            if (!this.isDrawerView(child)) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Child ");
                                sb.append(child);
                                sb.append(" at index ");
                                sb.append(i);
                                sb.append(" does not have a valid layout_gravity - must be Gravity.LEFT, Gravity.RIGHT or Gravity.NO_GRAVITY");
                                throw new IllegalStateException(sb.toString());
                            }
                            if (Build$VERSION.SDK_INT >= 21) {
                                final int n5 = (int)(this.getResources().getDisplayMetrics().density * 10.0f);
                                if ((int)ViewCompat.getElevation(child) != n5) {
                                    ViewCompat.setElevation(child, (float)n5);
                                }
                            }
                            final boolean b = (this.getDrawerViewAbsoluteGravity(child) & 0x7) == 0x3;
                            if ((b && n3 != 0) || (!b && n4 != 0)) {
                                throw new IllegalStateException("Duplicate drawers on the same edge");
                            }
                            if (b) {
                                n3 = 1;
                            }
                            else {
                                n4 = 1;
                            }
                            child.measure(getChildMeasureSpec(n, layoutParams.leftMargin + layoutParams.rightMargin, layoutParams.width), getChildMeasureSpec(n2, layoutParams.topMargin + layoutParams.bottomMargin, layoutParams.height));
                        }
                    }
                    ++i;
                }
                return;
            }
            throw new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
        }
    }
    
    private static class NoOpOverlay extends WebViewDebugOverlay
    {
        private NoOpOverlay(final Context context) {
            super(context, null);
        }
        
        @Override
        public boolean isEnable() {
            return false;
        }
    }
}
