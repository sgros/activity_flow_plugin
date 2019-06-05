package org.mozilla.focus.webkit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.support.p001v4.view.GravityCompat;
import android.support.p001v4.view.ViewCompat;
import android.support.p001v4.widget.DrawerLayout;
import android.support.p001v4.widget.DrawerLayout.LayoutParams;
import android.support.p004v7.widget.AppCompatTextView;
import android.text.TextUtils.TruncateAt;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.webkit.WebBackForwardList;
import android.webkit.WebHistoryItem;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.mozilla.urlutils.UrlUtils;

public class WebViewDebugOverlay {
    private LinearLayout backForwardList;
    private LinearLayout callbackList;
    private DrawerLayout drawerLayout;
    private LinearLayout viewTreeList;
    private Runnable viewTreeUpdateRunnable;
    private WebView webView;

    /* renamed from: org.mozilla.focus.webkit.WebViewDebugOverlay$1 */
    class C05551 implements Runnable {
        C05551() {
        }

        public void run() {
            if (WebViewDebugOverlay.this.webView != null && WebViewDebugOverlay.this.webView.isAttachedToWindow()) {
                WebViewDebugOverlay.this.viewTreeList.removeAllViews();
                WebViewDebugOverlay.this.updateViewTreeList(WebViewDebugOverlay.this.webView, 0);
            }
        }
    }

    private static class FullScreenDrawerLayout extends DrawerLayout {
        public FullScreenDrawerLayout(Context context) {
            super(context);
        }

        /* Access modifiers changed, original: protected */
        public void onMeasure(int i, int i2) {
            int mode = MeasureSpec.getMode(i);
            int mode2 = MeasureSpec.getMode(i2);
            int size = MeasureSpec.getSize(i);
            int size2 = MeasureSpec.getSize(i2);
            if (mode == 1073741824 && mode2 == 1073741824) {
                setMeasuredDimension(size, size2);
                mode = getChildCount();
                Object obj = null;
                Object obj2 = null;
                for (int i3 = 0; i3 < mode; i3++) {
                    View childAt = getChildAt(i3);
                    if (childAt.getVisibility() != 8) {
                        LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                        if (isContentView(childAt)) {
                            childAt.measure(MeasureSpec.makeMeasureSpec((size - layoutParams.leftMargin) - layoutParams.rightMargin, 1073741824), MeasureSpec.makeMeasureSpec((size2 - layoutParams.topMargin) - layoutParams.bottomMargin, 1073741824));
                        } else if (isDrawerView(childAt)) {
                            if (VERSION.SDK_INT >= 21) {
                                int i4 = (int) (getResources().getDisplayMetrics().density * 10.0f);
                                if (((int) ViewCompat.getElevation(childAt)) != i4) {
                                    ViewCompat.setElevation(childAt, (float) i4);
                                }
                            }
                            Object obj3 = (getDrawerViewAbsoluteGravity(childAt) & 7) == 3 ? 1 : null;
                            if ((obj3 == null || obj == null) && (obj3 != null || obj2 == null)) {
                                if (obj3 != null) {
                                    obj = 1;
                                } else {
                                    obj2 = 1;
                                }
                                childAt.measure(getChildMeasureSpec(i, layoutParams.leftMargin + layoutParams.rightMargin, layoutParams.width), getChildMeasureSpec(i2, layoutParams.topMargin + layoutParams.bottomMargin, layoutParams.height));
                            } else {
                                throw new IllegalStateException("Duplicate drawers on the same edge");
                            }
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Child ");
                            stringBuilder.append(childAt);
                            stringBuilder.append(" at index ");
                            stringBuilder.append(i3);
                            stringBuilder.append(" does not have a valid layout_gravity - must be Gravity.LEFT, Gravity.RIGHT or Gravity.NO_GRAVITY");
                            throw new IllegalStateException(stringBuilder.toString());
                        }
                    }
                }
                return;
            }
            throw new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isContentView(View view) {
            return ((LayoutParams) view.getLayoutParams()).gravity == 0;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isDrawerView(View view) {
            int absoluteGravity = GravityCompat.getAbsoluteGravity(((LayoutParams) view.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(view));
            return ((absoluteGravity & 3) == 0 && (absoluteGravity & 5) == 0) ? false : true;
        }

        /* Access modifiers changed, original: 0000 */
        public int getDrawerViewAbsoluteGravity(View view) {
            return GravityCompat.getAbsoluteGravity(((LayoutParams) view.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(this));
        }
    }

    private static class NoOpOverlay extends WebViewDebugOverlay {
        public boolean isEnable() {
            return false;
        }

        /* synthetic */ NoOpOverlay(Context context, C05551 c05551) {
            this(context);
        }

        private NoOpOverlay(Context context) {
            super(context, null);
        }
    }

    public static boolean isSupport() {
        return false;
    }

    public boolean isEnable() {
        return true;
    }

    /* synthetic */ WebViewDebugOverlay(Context context, C05551 c05551) {
        this(context);
    }

    public static WebViewDebugOverlay create(Context context) {
        if (isSupport()) {
            return new WebViewDebugOverlay(context);
        }
        return new NoOpOverlay(context, null);
    }

    private WebViewDebugOverlay(Context context) {
        this.viewTreeUpdateRunnable = new C05551();
        if (isEnable()) {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(1);
            linearLayout.setBackgroundColor(Color.parseColor("#99000000"));
            insertSectionTitle("WebViewClient", linearLayout);
            linearLayout.addView(createCallbackList(context), new LinearLayout.LayoutParams(-1, -2));
            insertSectionTitle("BackForwardList", linearLayout);
            linearLayout.addView(createBackForwardList(context), new LinearLayout.LayoutParams(-1, -2));
            insertSectionTitle("View tree", linearLayout);
            linearLayout.addView(createViewTreeList(context), new LinearLayout.LayoutParams(-1, -1));
            this.drawerLayout = new FullScreenDrawerLayout(context);
            this.drawerLayout.setScrimColor(0);
            this.drawerLayout.addView(new View(context) {
                View target;

                @SuppressLint({"ClickableViewAccessibility"})
                public boolean onTouchEvent(MotionEvent motionEvent) {
                    if (this.target != null) {
                        boolean onTouchEvent = this.target.onTouchEvent(motionEvent);
                        if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                            this.target = null;
                        }
                        return onTouchEvent;
                    }
                    if (WebViewDebugOverlay.this.webView.getChildCount() != 0) {
                        for (int i = 0; i < WebViewDebugOverlay.this.webView.getChildCount(); i++) {
                            View childAt = WebViewDebugOverlay.this.webView.getChildAt(i);
                            if (childAt != WebViewDebugOverlay.this.drawerLayout && childAt.onTouchEvent(motionEvent)) {
                                this.target = childAt;
                                return true;
                            }
                        }
                    }
                    return WebViewDebugOverlay.this.webView.onTouchEvent(motionEvent);
                }
            }, new ViewGroup.LayoutParams(-1, -1));
            LayoutParams layoutParams = new LayoutParams(-1, -1);
            layoutParams.gravity = 8388611;
            linearLayout.setLayoutParams(layoutParams);
            this.drawerLayout.addView(linearLayout);
        }
    }

    public void onWebViewScrolled(int i, int i2) {
        if (isEnable()) {
            this.drawerLayout.setTranslationY((float) i2);
            this.drawerLayout.setTranslationX((float) i);
        }
    }

    public void updateHistory() {
        if (isEnable()) {
            this.backForwardList.removeAllViews();
            WebBackForwardList copyBackForwardList = this.webView.copyBackForwardList();
            int size = copyBackForwardList.getSize();
            int currentIndex = copyBackForwardList.getCurrentIndex();
            if (currentIndex >= 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("size:");
                stringBuilder.append(size);
                stringBuilder.append(", curr:");
                stringBuilder.append(currentIndex);
                insertHistory(stringBuilder.toString(), -1);
                int i = currentIndex - 2;
                currentIndex += 2;
                if (i < 0) {
                    currentIndex -= i;
                    i = 0;
                }
                if (currentIndex >= size) {
                    i = Math.max(0, i - ((currentIndex - size) + 1));
                    currentIndex = size - 1;
                }
                if (i != 0) {
                    insertHistory("...", -1);
                }
                while (i <= currentIndex) {
                    WebHistoryItem itemAtIndex = copyBackForwardList.getItemAtIndex(i);
                    String replaceAll = itemAtIndex.getOriginalUrl().replaceAll("https://", "");
                    String replaceAll2 = itemAtIndex.getUrl().replaceAll("https://", "");
                    size = copyBackForwardList.getCurrentIndex() == i ? (UrlUtils.isInternalErrorURL(itemAtIndex.getOriginalUrl()) || UrlUtils.isInternalErrorURL(itemAtIndex.getUrl())) ? -65536 : -16711936 : -3355444;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(i);
                    stringBuilder2.append(": ");
                    stringBuilder2.append(replaceAll);
                    insertHistory(stringBuilder2.toString(), size);
                    if (!replaceAll.equals(replaceAll2)) {
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("-> ");
                        stringBuilder3.append(replaceAll2);
                        insertHistory(stringBuilder3.toString(), size);
                    }
                    insertDivider(this.backForwardList);
                    i++;
                }
                updateViewTree();
            }
        }
    }

    public void recordLifecycle(String str, boolean z) {
        if (isEnable()) {
            while (this.callbackList.getChildCount() + 1 > 10) {
                this.callbackList.removeViewAt(0);
            }
            if (z && this.callbackList.getChildCount() != 0) {
                insertDivider(this.callbackList);
            }
            insertCallback(str.replace("https://", ""), -3355444);
        }
    }

    public void onLoadUrlCalled() {
        if (isEnable()) {
            this.callbackList.removeAllViews();
        }
    }

    public void bindWebView(WebView webView) {
        if (isEnable()) {
            this.webView = webView;
            webView.addView(this.drawerLayout, new ViewGroup.LayoutParams(-1, -1));
            updateHistory();
            this.drawerLayout.setElevation(100.0f);
        }
    }

    private void updateViewTree() {
        if (isEnable()) {
            this.webView.removeCallbacks(this.viewTreeUpdateRunnable);
            this.viewTreeList.removeAllViews();
            insertText("updating...", -3355444, this.viewTreeList);
            this.webView.postDelayed(this.viewTreeUpdateRunnable, 1500);
        }
    }

    private void updateViewTreeList(View view, int i) {
        if (view instanceof ViewGroup) {
            StringBuilder stringBuilder = new StringBuilder();
            int i2 = 0;
            while (i2 < i) {
                int i3 = i - 1;
                if (i2 < i3) {
                    stringBuilder.append("  ");
                    i2++;
                } else if (i2 == i3) {
                    stringBuilder.append("|-");
                    i2 += 2;
                } else {
                    stringBuilder.append("--");
                    i2 += 2;
                }
            }
            stringBuilder.append(view.getClass().getSimpleName());
            if (view instanceof WebView) {
                WebHistoryItem currentItem = ((WebView) view).copyBackForwardList().getCurrentItem();
                if (currentItem != null) {
                    stringBuilder.append("(");
                    stringBuilder.append(currentItem.getOriginalUrl());
                    stringBuilder.append(")");
                }
            }
            insertText(stringBuilder.toString(), -3355444, this.viewTreeList);
            if (view != this.viewTreeList) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i4 = 0; i4 < viewGroup.getChildCount(); i4++) {
                    updateViewTreeList(viewGroup.getChildAt(i4), i + 1);
                }
            }
        }
    }

    private View createCallbackList(Context context) {
        this.callbackList = new LinearLayout(context);
        this.callbackList.setOrientation(1);
        return this.callbackList;
    }

    private View createBackForwardList(Context context) {
        this.backForwardList = new LinearLayout(context);
        this.backForwardList.setOrientation(1);
        return this.backForwardList;
    }

    private View createViewTreeList(Context context) {
        this.viewTreeList = new LinearLayout(context);
        this.viewTreeList.setOrientation(1);
        return this.viewTreeList;
    }

    private void insertSectionTitle(String str, LinearLayout linearLayout) {
        if (isEnable()) {
            TextView textView = new TextView(linearLayout.getContext());
            textView.setText(str);
            textView.setTextColor(-1);
            textView.setTextSize(1, 16.0f);
            textView.setTypeface(Typeface.MONOSPACE);
            float f = textView.getResources().getDisplayMetrics().density;
            MarginLayoutParams marginLayoutParams = new MarginLayoutParams(-1, -2);
            marginLayoutParams.topMargin = linearLayout.getChildCount() == 0 ? 0 : (int) (f * 16.0f);
            linearLayout.addView(textView, marginLayoutParams);
        }
    }

    private void insertHistory(String str, int i) {
        insertText(str, i, this.backForwardList);
    }

    private void insertCallback(String str, int i) {
        insertText(str, i, this.callbackList);
    }

    private void insertText(String str, int i, LinearLayout linearLayout) {
        AppCompatTextView appCompatTextView = new AppCompatTextView(this.callbackList.getContext());
        appCompatTextView.setTextColor(i);
        appCompatTextView.setTextSize(1, 12.0f);
        appCompatTextView.setText(str);
        appCompatTextView.setMaxLines(1);
        appCompatTextView.setSingleLine();
        appCompatTextView.setEllipsize(TruncateAt.END);
        appCompatTextView.setTypeface(Typeface.MONOSPACE);
        linearLayout.addView(appCompatTextView);
    }

    private void insertDivider(LinearLayout linearLayout) {
        View view = new View(this.webView.getContext());
        view.setBackgroundColor(-1);
        linearLayout.addView(view, new ViewGroup.LayoutParams(-1, 1));
    }
}
