package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.Theme;
import android.database.DataSetObserver;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.ThemedSpinnerAdapter;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.TintableBackgroundView;
import androidx.core.view.ViewCompat;

public class AppCompatSpinner extends Spinner implements TintableBackgroundView {
    private static final int[] ATTRS_ANDROID_SPINNERMODE = new int[]{16843505};
    private final AppCompatBackgroundHelper mBackgroundTintHelper;
    int mDropDownWidth;
    private ForwardingListener mForwardingListener;
    DropdownPopup mPopup;
    private final Context mPopupContext;
    private final boolean mPopupSet;
    private SpinnerAdapter mTempAdapter;
    final Rect mTempRect;

    private static class DropDownAdapter implements ListAdapter, SpinnerAdapter {
        private SpinnerAdapter mAdapter;
        private ListAdapter mListAdapter;

        public int getItemViewType(int i) {
            return 0;
        }

        public int getViewTypeCount() {
            return 1;
        }

        public DropDownAdapter(SpinnerAdapter spinnerAdapter, Theme theme) {
            this.mAdapter = spinnerAdapter;
            if (spinnerAdapter instanceof ListAdapter) {
                this.mListAdapter = (ListAdapter) spinnerAdapter;
            }
            if (theme == null) {
                return;
            }
            if (VERSION.SDK_INT >= 23 && (spinnerAdapter instanceof ThemedSpinnerAdapter)) {
                ThemedSpinnerAdapter themedSpinnerAdapter = (ThemedSpinnerAdapter) spinnerAdapter;
                if (themedSpinnerAdapter.getDropDownViewTheme() != theme) {
                    themedSpinnerAdapter.setDropDownViewTheme(theme);
                }
            } else if (spinnerAdapter instanceof ThemedSpinnerAdapter) {
                ThemedSpinnerAdapter themedSpinnerAdapter2 = (ThemedSpinnerAdapter) spinnerAdapter;
                if (themedSpinnerAdapter2.getDropDownViewTheme() == null) {
                    themedSpinnerAdapter2.setDropDownViewTheme(theme);
                }
            }
        }

        public int getCount() {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            return spinnerAdapter == null ? 0 : spinnerAdapter.getCount();
        }

        public Object getItem(int i) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            return spinnerAdapter == null ? null : spinnerAdapter.getItem(i);
        }

        public long getItemId(int i) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            return spinnerAdapter == null ? -1 : spinnerAdapter.getItemId(i);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            return getDropDownView(i, view, viewGroup);
        }

        public View getDropDownView(int i, View view, ViewGroup viewGroup) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter == null) {
                return null;
            }
            return spinnerAdapter.getDropDownView(i, view, viewGroup);
        }

        public boolean hasStableIds() {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            return spinnerAdapter != null && spinnerAdapter.hasStableIds();
        }

        public void registerDataSetObserver(DataSetObserver dataSetObserver) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter != null) {
                spinnerAdapter.registerDataSetObserver(dataSetObserver);
            }
        }

        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter != null) {
                spinnerAdapter.unregisterDataSetObserver(dataSetObserver);
            }
        }

        public boolean areAllItemsEnabled() {
            ListAdapter listAdapter = this.mListAdapter;
            return listAdapter != null ? listAdapter.areAllItemsEnabled() : true;
        }

        public boolean isEnabled(int i) {
            ListAdapter listAdapter = this.mListAdapter;
            return listAdapter != null ? listAdapter.isEnabled(i) : true;
        }

        public boolean isEmpty() {
            return getCount() == 0;
        }
    }

    private class DropdownPopup extends ListPopupWindow {
        ListAdapter mAdapter;
        private CharSequence mHintText;
        private final Rect mVisibleRect = new Rect();

        /* renamed from: androidx.appcompat.widget.AppCompatSpinner$DropdownPopup$2 */
        class C00122 implements OnGlobalLayoutListener {
            C00122() {
            }

            public void onGlobalLayout() {
                DropdownPopup dropdownPopup = DropdownPopup.this;
                if (dropdownPopup.isVisibleToUser(AppCompatSpinner.this)) {
                    DropdownPopup.this.computeContentWidth();
                    super.show();
                    return;
                }
                DropdownPopup.this.dismiss();
            }
        }

        public DropdownPopup(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, i);
            setAnchorView(AppCompatSpinner.this);
            setModal(true);
            setPromptPosition(0);
            setOnItemClickListener(new OnItemClickListener(AppCompatSpinner.this) {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    AppCompatSpinner.this.setSelection(i);
                    if (AppCompatSpinner.this.getOnItemClickListener() != null) {
                        DropdownPopup dropdownPopup = DropdownPopup.this;
                        AppCompatSpinner.this.performItemClick(view, i, dropdownPopup.mAdapter.getItemId(i));
                    }
                    DropdownPopup.this.dismiss();
                }
            });
        }

        public void setAdapter(ListAdapter listAdapter) {
            super.setAdapter(listAdapter);
            this.mAdapter = listAdapter;
        }

        public CharSequence getHintText() {
            return this.mHintText;
        }

        public void setPromptText(CharSequence charSequence) {
            this.mHintText = charSequence;
        }

        /* Access modifiers changed, original: 0000 */
        public void computeContentWidth() {
            Drawable background = getBackground();
            int i = 0;
            if (background != null) {
                background.getPadding(AppCompatSpinner.this.mTempRect);
                i = ViewUtils.isLayoutRtl(AppCompatSpinner.this) ? AppCompatSpinner.this.mTempRect.right : -AppCompatSpinner.this.mTempRect.left;
            } else {
                Rect rect = AppCompatSpinner.this.mTempRect;
                rect.right = 0;
                rect.left = 0;
            }
            int paddingLeft = AppCompatSpinner.this.getPaddingLeft();
            int paddingRight = AppCompatSpinner.this.getPaddingRight();
            int width = AppCompatSpinner.this.getWidth();
            AppCompatSpinner appCompatSpinner = AppCompatSpinner.this;
            int i2 = appCompatSpinner.mDropDownWidth;
            if (i2 == -2) {
                int compatMeasureContentWidth = appCompatSpinner.compatMeasureContentWidth((SpinnerAdapter) this.mAdapter, getBackground());
                i2 = AppCompatSpinner.this.getContext().getResources().getDisplayMetrics().widthPixels;
                Rect rect2 = AppCompatSpinner.this.mTempRect;
                i2 = (i2 - rect2.left) - rect2.right;
                if (compatMeasureContentWidth > i2) {
                    compatMeasureContentWidth = i2;
                }
                setContentWidth(Math.max(compatMeasureContentWidth, (width - paddingLeft) - paddingRight));
            } else if (i2 == -1) {
                setContentWidth((width - paddingLeft) - paddingRight);
            } else {
                setContentWidth(i2);
            }
            setHorizontalOffset(ViewUtils.isLayoutRtl(AppCompatSpinner.this) ? i + ((width - paddingRight) - getWidth()) : i + paddingLeft);
        }

        public void show() {
            boolean isShowing = isShowing();
            computeContentWidth();
            setInputMethodMode(2);
            super.show();
            getListView().setChoiceMode(1);
            setSelection(AppCompatSpinner.this.getSelectedItemPosition());
            if (!isShowing) {
                ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
                if (viewTreeObserver != null) {
                    final C00122 c00122 = new C00122();
                    viewTreeObserver.addOnGlobalLayoutListener(c00122);
                    setOnDismissListener(new OnDismissListener() {
                        public void onDismiss() {
                            ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
                            if (viewTreeObserver != null) {
                                viewTreeObserver.removeGlobalOnLayoutListener(c00122);
                            }
                        }
                    });
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isVisibleToUser(View view) {
            return ViewCompat.isAttachedToWindow(view) && view.getGlobalVisibleRect(this.mVisibleRect);
        }
    }

    public AppCompatSpinner(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, -1);
    }

    public AppCompatSpinner(Context context, AttributeSet attributeSet, int i, int i2) {
        this(context, attributeSet, i, i2, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0070  */
    /* JADX WARNING: Missing block: B:21:0x0056, code skipped:
            if (r12 != null) goto L_0x0058;
     */
    /* JADX WARNING: Missing block: B:22:0x0058, code skipped:
            r12.recycle();
     */
    /* JADX WARNING: Missing block: B:30:0x006a, code skipped:
            if (r12 != null) goto L_0x0058;
     */
    public AppCompatSpinner(android.content.Context r8, android.util.AttributeSet r9, int r10, int r11, android.content.res.Resources.Theme r12) {
        /*
        r7 = this;
        r7.<init>(r8, r9, r10);
        r0 = new android.graphics.Rect;
        r0.<init>();
        r7.mTempRect = r0;
        r0 = androidx.appcompat.R$styleable.Spinner;
        r1 = 0;
        r0 = androidx.appcompat.widget.TintTypedArray.obtainStyledAttributes(r8, r9, r0, r10, r1);
        r2 = new androidx.appcompat.widget.AppCompatBackgroundHelper;
        r2.<init>(r7);
        r7.mBackgroundTintHelper = r2;
        r2 = 0;
        if (r12 == 0) goto L_0x0023;
    L_0x001b:
        r3 = new androidx.appcompat.view.ContextThemeWrapper;
        r3.<init>(r8, r12);
        r7.mPopupContext = r3;
        goto L_0x003e;
    L_0x0023:
        r12 = androidx.appcompat.R$styleable.Spinner_popupTheme;
        r12 = r0.getResourceId(r12, r1);
        if (r12 == 0) goto L_0x0033;
    L_0x002b:
        r3 = new androidx.appcompat.view.ContextThemeWrapper;
        r3.<init>(r8, r12);
        r7.mPopupContext = r3;
        goto L_0x003e;
    L_0x0033:
        r12 = android.os.Build.VERSION.SDK_INT;
        r3 = 23;
        if (r12 >= r3) goto L_0x003b;
    L_0x0039:
        r12 = r8;
        goto L_0x003c;
    L_0x003b:
        r12 = r2;
    L_0x003c:
        r7.mPopupContext = r12;
    L_0x003e:
        r12 = r7.mPopupContext;
        r3 = 1;
        if (r12 == 0) goto L_0x00ac;
    L_0x0043:
        r12 = -1;
        if (r11 != r12) goto L_0x0074;
    L_0x0046:
        r12 = ATTRS_ANDROID_SPINNERMODE;	 Catch:{ Exception -> 0x0061, all -> 0x005e }
        r12 = r8.obtainStyledAttributes(r9, r12, r10, r1);	 Catch:{ Exception -> 0x0061, all -> 0x005e }
        r4 = r12.hasValue(r1);	 Catch:{ Exception -> 0x005c }
        if (r4 == 0) goto L_0x0056;
    L_0x0052:
        r11 = r12.getInt(r1, r1);	 Catch:{ Exception -> 0x005c }
    L_0x0056:
        if (r12 == 0) goto L_0x0074;
    L_0x0058:
        r12.recycle();
        goto L_0x0074;
    L_0x005c:
        r4 = move-exception;
        goto L_0x0063;
    L_0x005e:
        r8 = move-exception;
        r12 = r2;
        goto L_0x006e;
    L_0x0061:
        r4 = move-exception;
        r12 = r2;
    L_0x0063:
        r5 = "AppCompatSpinner";
        r6 = "Could not read android:spinnerMode";
        android.util.Log.i(r5, r6, r4);	 Catch:{ all -> 0x006d }
        if (r12 == 0) goto L_0x0074;
    L_0x006c:
        goto L_0x0058;
    L_0x006d:
        r8 = move-exception;
    L_0x006e:
        if (r12 == 0) goto L_0x0073;
    L_0x0070:
        r12.recycle();
    L_0x0073:
        throw r8;
    L_0x0074:
        if (r11 != r3) goto L_0x00ac;
    L_0x0076:
        r11 = new androidx.appcompat.widget.AppCompatSpinner$DropdownPopup;
        r12 = r7.mPopupContext;
        r11.<init>(r12, r9, r10);
        r12 = r7.mPopupContext;
        r4 = androidx.appcompat.R$styleable.Spinner;
        r12 = androidx.appcompat.widget.TintTypedArray.obtainStyledAttributes(r12, r9, r4, r10, r1);
        r1 = androidx.appcompat.R$styleable.Spinner_android_dropDownWidth;
        r4 = -2;
        r1 = r12.getLayoutDimension(r1, r4);
        r7.mDropDownWidth = r1;
        r1 = androidx.appcompat.R$styleable.Spinner_android_popupBackground;
        r1 = r12.getDrawable(r1);
        r11.setBackgroundDrawable(r1);
        r1 = androidx.appcompat.R$styleable.Spinner_android_prompt;
        r1 = r0.getString(r1);
        r11.setPromptText(r1);
        r12.recycle();
        r7.mPopup = r11;
        r12 = new androidx.appcompat.widget.AppCompatSpinner$1;
        r12.<init>(r7, r11);
        r7.mForwardingListener = r12;
    L_0x00ac:
        r11 = androidx.appcompat.R$styleable.Spinner_android_entries;
        r11 = r0.getTextArray(r11);
        if (r11 == 0) goto L_0x00c4;
    L_0x00b4:
        r12 = new android.widget.ArrayAdapter;
        r1 = 17367048; // 0x1090008 float:2.5162948E-38 double:8.580462E-317;
        r12.<init>(r8, r1, r11);
        r8 = androidx.appcompat.R$layout.support_simple_spinner_dropdown_item;
        r12.setDropDownViewResource(r8);
        r7.setAdapter(r12);
    L_0x00c4:
        r0.recycle();
        r7.mPopupSet = r3;
        r8 = r7.mTempAdapter;
        if (r8 == 0) goto L_0x00d2;
    L_0x00cd:
        r7.setAdapter(r8);
        r7.mTempAdapter = r2;
    L_0x00d2:
        r8 = r7.mBackgroundTintHelper;
        r8.loadFromAttributes(r9, r10);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.AppCompatSpinner.<init>(android.content.Context, android.util.AttributeSet, int, int, android.content.res.Resources$Theme):void");
    }

    public Context getPopupContext() {
        if (this.mPopup != null) {
            return this.mPopupContext;
        }
        return VERSION.SDK_INT >= 23 ? super.getPopupContext() : null;
    }

    public void setPopupBackgroundDrawable(Drawable drawable) {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            dropdownPopup.setBackgroundDrawable(drawable);
        } else if (VERSION.SDK_INT >= 16) {
            super.setPopupBackgroundDrawable(drawable);
        }
    }

    public void setPopupBackgroundResource(int i) {
        setPopupBackgroundDrawable(AppCompatResources.getDrawable(getPopupContext(), i));
    }

    public Drawable getPopupBackground() {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            return dropdownPopup.getBackground();
        }
        return VERSION.SDK_INT >= 16 ? super.getPopupBackground() : null;
    }

    public void setDropDownVerticalOffset(int i) {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            dropdownPopup.setVerticalOffset(i);
        } else if (VERSION.SDK_INT >= 16) {
            super.setDropDownVerticalOffset(i);
        }
    }

    public int getDropDownVerticalOffset() {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            return dropdownPopup.getVerticalOffset();
        }
        return VERSION.SDK_INT >= 16 ? super.getDropDownVerticalOffset() : 0;
    }

    public void setDropDownHorizontalOffset(int i) {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            dropdownPopup.setHorizontalOffset(i);
        } else if (VERSION.SDK_INT >= 16) {
            super.setDropDownHorizontalOffset(i);
        }
    }

    public int getDropDownHorizontalOffset() {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            return dropdownPopup.getHorizontalOffset();
        }
        return VERSION.SDK_INT >= 16 ? super.getDropDownHorizontalOffset() : 0;
    }

    public void setDropDownWidth(int i) {
        if (this.mPopup != null) {
            this.mDropDownWidth = i;
        } else if (VERSION.SDK_INT >= 16) {
            super.setDropDownWidth(i);
        }
    }

    public int getDropDownWidth() {
        if (this.mPopup != null) {
            return this.mDropDownWidth;
        }
        return VERSION.SDK_INT >= 16 ? super.getDropDownWidth() : 0;
    }

    public void setAdapter(SpinnerAdapter spinnerAdapter) {
        if (this.mPopupSet) {
            super.setAdapter(spinnerAdapter);
            if (this.mPopup != null) {
                Context context = this.mPopupContext;
                if (context == null) {
                    context = getContext();
                }
                this.mPopup.setAdapter(new DropDownAdapter(spinnerAdapter, context.getTheme()));
            }
            return;
        }
        this.mTempAdapter = spinnerAdapter;
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null && dropdownPopup.isShowing()) {
            this.mPopup.dismiss();
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        ForwardingListener forwardingListener = this.mForwardingListener;
        if (forwardingListener == null || !forwardingListener.onTouch(this, motionEvent)) {
            return super.onTouchEvent(motionEvent);
        }
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mPopup != null && MeasureSpec.getMode(i) == Integer.MIN_VALUE) {
            setMeasuredDimension(Math.min(Math.max(getMeasuredWidth(), compatMeasureContentWidth(getAdapter(), getBackground())), MeasureSpec.getSize(i)), getMeasuredHeight());
        }
    }

    public boolean performClick() {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup == null) {
            return super.performClick();
        }
        if (!dropdownPopup.isShowing()) {
            this.mPopup.show();
        }
        return true;
    }

    public void setPrompt(CharSequence charSequence) {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            dropdownPopup.setPromptText(charSequence);
        } else {
            super.setPrompt(charSequence);
        }
    }

    public CharSequence getPrompt() {
        DropdownPopup dropdownPopup = this.mPopup;
        return dropdownPopup != null ? dropdownPopup.getHintText() : super.getPrompt();
    }

    public void setBackgroundResource(int i) {
        super.setBackgroundResource(i);
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.onSetBackgroundResource(i);
        }
    }

    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.onSetBackgroundDrawable(drawable);
        }
    }

    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.setSupportBackgroundTintList(colorStateList);
        }
    }

    public ColorStateList getSupportBackgroundTintList() {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        return appCompatBackgroundHelper != null ? appCompatBackgroundHelper.getSupportBackgroundTintList() : null;
    }

    public void setSupportBackgroundTintMode(Mode mode) {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.setSupportBackgroundTintMode(mode);
        }
    }

    public Mode getSupportBackgroundTintMode() {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        return appCompatBackgroundHelper != null ? appCompatBackgroundHelper.getSupportBackgroundTintMode() : null;
    }

    /* Access modifiers changed, original: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.applySupportBackgroundTint();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int compatMeasureContentWidth(SpinnerAdapter spinnerAdapter, Drawable drawable) {
        int i = 0;
        if (spinnerAdapter == null) {
            return 0;
        }
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 0);
        int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 0);
        int max = Math.max(0, getSelectedItemPosition());
        int min = Math.min(spinnerAdapter.getCount(), max + 15);
        View view = null;
        int i2 = 0;
        for (max = Math.max(0, max - (15 - (min - max))); max < min; max++) {
            int itemViewType = spinnerAdapter.getItemViewType(max);
            if (itemViewType != i) {
                view = null;
                i = itemViewType;
            }
            view = spinnerAdapter.getView(max, view, this);
            if (view.getLayoutParams() == null) {
                view.setLayoutParams(new LayoutParams(-2, -2));
            }
            view.measure(makeMeasureSpec, makeMeasureSpec2);
            i2 = Math.max(i2, view.getMeasuredWidth());
        }
        if (drawable != null) {
            drawable.getPadding(this.mTempRect);
            Rect rect = this.mTempRect;
            i2 += rect.left + rect.right;
        }
        return i2;
    }
}
