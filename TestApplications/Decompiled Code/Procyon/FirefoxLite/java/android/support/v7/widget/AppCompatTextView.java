// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.widget;

import android.os.Build$VERSION;
import android.view.ActionMode$Callback;
import android.graphics.drawable.Drawable;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.EditorInfo;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import java.util.concurrent.ExecutionException;
import android.support.v4.widget.TextViewCompat;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v4.text.PrecomputedTextCompat;
import java.util.concurrent.Future;
import android.support.v4.widget.AutoSizeableTextView;
import android.support.v4.view.TintableBackgroundView;
import android.widget.TextView;

public class AppCompatTextView extends TextView implements TintableBackgroundView, AutoSizeableTextView
{
    private final AppCompatBackgroundHelper mBackgroundTintHelper;
    private Future<PrecomputedTextCompat> mPrecomputedTextFuture;
    private final AppCompatTextHelper mTextHelper;
    
    public AppCompatTextView(final Context context) {
        this(context, null);
    }
    
    public AppCompatTextView(final Context context, final AttributeSet set) {
        this(context, set, 16842884);
    }
    
    public AppCompatTextView(final Context context, final AttributeSet set, final int n) {
        super(TintContextWrapper.wrap(context), set, n);
        (this.mBackgroundTintHelper = new AppCompatBackgroundHelper((View)this)).loadFromAttributes(set, n);
        (this.mTextHelper = new AppCompatTextHelper(this)).loadFromAttributes(set, n);
        this.mTextHelper.applyCompoundDrawablesTints();
    }
    
    private void consumeTextFutureAndSetBlocking() {
        if (this.mPrecomputedTextFuture == null) {
            return;
        }
        try {
            final Future<PrecomputedTextCompat> mPrecomputedTextFuture = this.mPrecomputedTextFuture;
            this.mPrecomputedTextFuture = null;
            TextViewCompat.setPrecomputedText(this, mPrecomputedTextFuture.get());
        }
        catch (InterruptedException | ExecutionException ex) {}
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.applySupportBackgroundTint();
        }
        if (this.mTextHelper != null) {
            this.mTextHelper.applyCompoundDrawablesTints();
        }
    }
    
    public int getAutoSizeMaxTextSize() {
        if (AppCompatTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            return super.getAutoSizeMaxTextSize();
        }
        if (this.mTextHelper != null) {
            return this.mTextHelper.getAutoSizeMaxTextSize();
        }
        return -1;
    }
    
    public int getAutoSizeMinTextSize() {
        if (AppCompatTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            return super.getAutoSizeMinTextSize();
        }
        if (this.mTextHelper != null) {
            return this.mTextHelper.getAutoSizeMinTextSize();
        }
        return -1;
    }
    
    public int getAutoSizeStepGranularity() {
        if (AppCompatTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            return super.getAutoSizeStepGranularity();
        }
        if (this.mTextHelper != null) {
            return this.mTextHelper.getAutoSizeStepGranularity();
        }
        return -1;
    }
    
    public int[] getAutoSizeTextAvailableSizes() {
        if (AppCompatTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            return super.getAutoSizeTextAvailableSizes();
        }
        if (this.mTextHelper != null) {
            return this.mTextHelper.getAutoSizeTextAvailableSizes();
        }
        return new int[0];
    }
    
    public int getAutoSizeTextType() {
        final boolean platform_SUPPORTS_AUTOSIZE = AppCompatTextView.PLATFORM_SUPPORTS_AUTOSIZE;
        int n = 0;
        if (platform_SUPPORTS_AUTOSIZE) {
            if (super.getAutoSizeTextType() == 1) {
                n = 1;
            }
            return n;
        }
        if (this.mTextHelper != null) {
            return this.mTextHelper.getAutoSizeTextType();
        }
        return 0;
    }
    
    public int getFirstBaselineToTopHeight() {
        return TextViewCompat.getFirstBaselineToTopHeight(this);
    }
    
    public int getLastBaselineToBottomHeight() {
        return TextViewCompat.getLastBaselineToBottomHeight(this);
    }
    
    public ColorStateList getSupportBackgroundTintList() {
        ColorStateList supportBackgroundTintList;
        if (this.mBackgroundTintHelper != null) {
            supportBackgroundTintList = this.mBackgroundTintHelper.getSupportBackgroundTintList();
        }
        else {
            supportBackgroundTintList = null;
        }
        return supportBackgroundTintList;
    }
    
    public PorterDuff$Mode getSupportBackgroundTintMode() {
        PorterDuff$Mode supportBackgroundTintMode;
        if (this.mBackgroundTintHelper != null) {
            supportBackgroundTintMode = this.mBackgroundTintHelper.getSupportBackgroundTintMode();
        }
        else {
            supportBackgroundTintMode = null;
        }
        return supportBackgroundTintMode;
    }
    
    public CharSequence getText() {
        this.consumeTextFutureAndSetBlocking();
        return super.getText();
    }
    
    public PrecomputedTextCompat.Params getTextMetricsParamsCompat() {
        return TextViewCompat.getTextMetricsParams(this);
    }
    
    public InputConnection onCreateInputConnection(final EditorInfo editorInfo) {
        return AppCompatHintHelper.onCreateInputConnection(super.onCreateInputConnection(editorInfo), editorInfo, (View)this);
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        if (this.mTextHelper != null) {
            this.mTextHelper.onLayout(b, n, n2, n3, n4);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.consumeTextFutureAndSetBlocking();
        super.onMeasure(n, n2);
    }
    
    protected void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
        super.onTextChanged(charSequence, n, n2, n3);
        if (this.mTextHelper != null && !AppCompatTextView.PLATFORM_SUPPORTS_AUTOSIZE && this.mTextHelper.isAutoSizeEnabled()) {
            this.mTextHelper.autoSizeText();
        }
    }
    
    public void setAutoSizeTextTypeUniformWithConfiguration(final int n, final int n2, final int n3, final int n4) throws IllegalArgumentException {
        if (AppCompatTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            super.setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
        }
        else if (this.mTextHelper != null) {
            this.mTextHelper.setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
        }
    }
    
    public void setAutoSizeTextTypeUniformWithPresetSizes(final int[] array, final int n) throws IllegalArgumentException {
        if (AppCompatTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            super.setAutoSizeTextTypeUniformWithPresetSizes(array, n);
        }
        else if (this.mTextHelper != null) {
            this.mTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(array, n);
        }
    }
    
    public void setAutoSizeTextTypeWithDefaults(final int n) {
        if (AppCompatTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            super.setAutoSizeTextTypeWithDefaults(n);
        }
        else if (this.mTextHelper != null) {
            this.mTextHelper.setAutoSizeTextTypeWithDefaults(n);
        }
    }
    
    public void setBackgroundDrawable(final Drawable backgroundDrawable) {
        super.setBackgroundDrawable(backgroundDrawable);
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.onSetBackgroundDrawable(backgroundDrawable);
        }
    }
    
    public void setBackgroundResource(final int backgroundResource) {
        super.setBackgroundResource(backgroundResource);
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.onSetBackgroundResource(backgroundResource);
        }
    }
    
    public void setCustomSelectionActionModeCallback(final ActionMode$Callback actionMode$Callback) {
        super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback(this, actionMode$Callback));
    }
    
    public void setFirstBaselineToTopHeight(final int firstBaselineToTopHeight) {
        if (Build$VERSION.SDK_INT >= 28) {
            super.setFirstBaselineToTopHeight(firstBaselineToTopHeight);
        }
        else {
            TextViewCompat.setFirstBaselineToTopHeight(this, firstBaselineToTopHeight);
        }
    }
    
    public void setLastBaselineToBottomHeight(final int lastBaselineToBottomHeight) {
        if (Build$VERSION.SDK_INT >= 28) {
            super.setLastBaselineToBottomHeight(lastBaselineToBottomHeight);
        }
        else {
            TextViewCompat.setLastBaselineToBottomHeight(this, lastBaselineToBottomHeight);
        }
    }
    
    public void setLineHeight(final int n) {
        TextViewCompat.setLineHeight(this, n);
    }
    
    public void setPrecomputedText(final PrecomputedTextCompat precomputedTextCompat) {
        TextViewCompat.setPrecomputedText(this, precomputedTextCompat);
    }
    
    public void setSupportBackgroundTintList(final ColorStateList supportBackgroundTintList) {
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.setSupportBackgroundTintList(supportBackgroundTintList);
        }
    }
    
    public void setSupportBackgroundTintMode(final PorterDuff$Mode supportBackgroundTintMode) {
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.setSupportBackgroundTintMode(supportBackgroundTintMode);
        }
    }
    
    public void setTextAppearance(final Context context, final int n) {
        super.setTextAppearance(context, n);
        if (this.mTextHelper != null) {
            this.mTextHelper.onSetTextAppearance(context, n);
        }
    }
    
    public void setTextFuture(final Future<PrecomputedTextCompat> mPrecomputedTextFuture) {
        this.mPrecomputedTextFuture = mPrecomputedTextFuture;
        this.requestLayout();
    }
    
    public void setTextMetricsParamsCompat(final PrecomputedTextCompat.Params params) {
        TextViewCompat.setTextMetricsParams(this, params);
    }
    
    public void setTextSize(final int n, final float n2) {
        if (AppCompatTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            super.setTextSize(n, n2);
        }
        else if (this.mTextHelper != null) {
            this.mTextHelper.setTextSize(n, n2);
        }
    }
}
