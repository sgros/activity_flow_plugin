// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.Paint;
import android.annotation.SuppressLint;
import android.text.Layout$Alignment;
import android.text.TextUtils$TruncateAt;
import android.os.Build$VERSION;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;
import android.text.TextPaint;
import org.telegram.messenger.FileLog;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import android.view.ViewGroup$MarginLayoutParams;
import android.content.DialogInterface$OnShowListener;
import android.content.DialogInterface$OnClickListener;
import android.graphics.drawable.Drawable;
import org.telegram.ui.ActionBar.Theme;
import android.view.View$MeasureSpec;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.AlertDialog;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ActionMode;
import android.view.ActionMode$Callback;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.style.CharacterStyle;
import android.content.Context;
import android.text.StaticLayout;

public class EditTextCaption extends EditTextBoldCursor
{
    private String caption;
    private StaticLayout captionLayout;
    private boolean copyPasteShowed;
    private EditTextCaptionDelegate delegate;
    private int hintColor;
    private int selectionEnd;
    private int selectionStart;
    private int triesCount;
    private int userNameLength;
    private int xOffset;
    private int yOffset;
    
    public EditTextCaption(final Context context) {
        super(context);
        this.triesCount = 0;
        this.selectionStart = -1;
        this.selectionEnd = -1;
    }
    
    private void applyTextStyleToSelection(final TypefaceSpan typefaceSpan) {
        int n = this.selectionStart;
        int n2 = 0;
        Label_0041: {
            if (n >= 0) {
                n2 = this.selectionEnd;
                if (n2 >= 0) {
                    this.selectionEnd = -1;
                    this.selectionStart = -1;
                    break Label_0041;
                }
            }
            n = this.getSelectionStart();
            n2 = this.getSelectionEnd();
        }
        final Editable text = this.getText();
        final CharacterStyle[] array = (CharacterStyle[])text.getSpans(n, n2, (Class)CharacterStyle.class);
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; ++i) {
                final CharacterStyle characterStyle = array[i];
                final int spanStart = text.getSpanStart((Object)characterStyle);
                final int spanEnd = text.getSpanEnd((Object)characterStyle);
                text.removeSpan((Object)characterStyle);
                if (spanStart < n) {
                    text.setSpan((Object)characterStyle, spanStart, n, 33);
                }
                if (spanEnd > n2) {
                    text.setSpan((Object)characterStyle, n2, spanEnd, 33);
                }
            }
        }
        if (typefaceSpan != null) {
            text.setSpan((Object)typefaceSpan, n, n2, 33);
        }
        final EditTextCaptionDelegate delegate = this.delegate;
        if (delegate != null) {
            delegate.onSpansChanged();
        }
    }
    
    private ActionMode$Callback overrideCallback(final ActionMode$Callback actionMode$Callback) {
        return (ActionMode$Callback)new ActionMode$Callback() {
            public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
                if (menuItem.getItemId() == 2131230847) {
                    EditTextCaption.this.makeSelectedRegular();
                    actionMode.finish();
                    return true;
                }
                if (menuItem.getItemId() == 2131230842) {
                    EditTextCaption.this.makeSelectedBold();
                    actionMode.finish();
                    return true;
                }
                if (menuItem.getItemId() == 2131230844) {
                    EditTextCaption.this.makeSelectedItalic();
                    actionMode.finish();
                    return true;
                }
                if (menuItem.getItemId() == 2131230846) {
                    EditTextCaption.this.makeSelectedMono();
                    actionMode.finish();
                    return true;
                }
                if (menuItem.getItemId() == 2131230845) {
                    EditTextCaption.this.makeSelectedUrl();
                    actionMode.finish();
                    return true;
                }
                try {
                    return actionMode$Callback.onActionItemClicked(actionMode, menuItem);
                }
                catch (Exception ex) {
                    return true;
                }
            }
            
            public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
                EditTextCaption.this.copyPasteShowed = true;
                return actionMode$Callback.onCreateActionMode(actionMode, menu);
            }
            
            public void onDestroyActionMode(final ActionMode actionMode) {
                EditTextCaption.this.copyPasteShowed = false;
                actionMode$Callback.onDestroyActionMode(actionMode);
            }
            
            public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
                return actionMode$Callback.onPrepareActionMode(actionMode, menu);
            }
        };
    }
    
    public String getCaption() {
        return this.caption;
    }
    
    public void makeSelectedBold() {
        this.applyTextStyleToSelection(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")));
    }
    
    public void makeSelectedItalic() {
        this.applyTextStyleToSelection(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/ritalic.ttf")));
    }
    
    public void makeSelectedMono() {
        this.applyTextStyleToSelection(new TypefaceSpan(Typeface.MONOSPACE));
    }
    
    public void makeSelectedRegular() {
        this.applyTextStyleToSelection(null);
    }
    
    public void makeSelectedUrl() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(LocaleController.getString("CreateLink", 2131559169));
        final EditTextBoldCursor view = new EditTextBoldCursor(this.getContext()) {
            @Override
            protected void onMeasure(final int n, final int n2) {
                super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f), 1073741824));
            }
        };
        view.setTextSize(1, 18.0f);
        view.setText((CharSequence)"http://");
        view.setTextColor(Theme.getColor("dialogTextBlack"));
        view.setHintText(LocaleController.getString("URL", 2131560927));
        view.setHeaderHintColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
        view.setSingleLine(true);
        view.setFocusable(true);
        view.setTransformHintToHeader(true);
        view.setLineColors(Theme.getColor("windowBackgroundWhiteInputField"), Theme.getColor("windowBackgroundWhiteInputFieldActivated"), Theme.getColor("windowBackgroundWhiteRedText3"));
        view.setImeOptions(6);
        view.setBackgroundDrawable((Drawable)null);
        view.requestFocus();
        view.setPadding(0, 0, 0, 0);
        builder.setView((View)view);
        int n = this.selectionStart;
        int n2 = 0;
        Label_0187: {
            if (n >= 0) {
                n2 = this.selectionEnd;
                if (n2 >= 0) {
                    this.selectionEnd = -1;
                    this.selectionStart = -1;
                    break Label_0187;
                }
            }
            n = this.getSelectionStart();
            n2 = this.getSelectionEnd();
        }
        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), (DialogInterface$OnClickListener)new _$$Lambda$EditTextCaption$BQIhHIR0EWfMGyyXmJJ_pkFKO1Y(this, n, n2, view));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
        builder.show().setOnShowListener((DialogInterface$OnShowListener)new _$$Lambda$EditTextCaption$8tXURyNItaU0tMIyLqohmCvoG40(view));
        final ViewGroup$MarginLayoutParams layoutParams = (ViewGroup$MarginLayoutParams)view.getLayoutParams();
        if (layoutParams != null) {
            if (layoutParams instanceof FrameLayout$LayoutParams) {
                ((FrameLayout$LayoutParams)layoutParams).gravity = 1;
            }
            final int dp = AndroidUtilities.dp(24.0f);
            layoutParams.leftMargin = dp;
            layoutParams.rightMargin = dp;
            layoutParams.height = AndroidUtilities.dp(36.0f);
            view.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        }
        view.setSelection(0, view.getText().length());
    }
    
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        try {
            if (this.captionLayout != null && this.userNameLength == this.length()) {
                final TextPaint paint = this.getPaint();
                final int color = this.getPaint().getColor();
                ((Paint)paint).setColor(this.hintColor);
                canvas.save();
                canvas.translate((float)this.xOffset, (float)this.yOffset);
                this.captionLayout.draw(canvas);
                canvas.restore();
                ((Paint)paint).setColor(color);
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    @Override
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (!TextUtils.isEmpty((CharSequence)this.caption)) {
            if (Build$VERSION.SDK_INT >= 26) {
                accessibilityNodeInfo.setHintText((CharSequence)this.caption);
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append((Object)accessibilityNodeInfo.getText());
                sb.append(", ");
                sb.append(this.caption);
                accessibilityNodeInfo.setText((CharSequence)sb.toString());
            }
        }
    }
    
    @SuppressLint({ "DrawAllocation" })
    @Override
    protected void onMeasure(int index, int paddingRight) {
        try {
            super.onMeasure(index, paddingRight);
        }
        catch (Exception ex) {
            this.setMeasuredDimension(View$MeasureSpec.getSize(index), AndroidUtilities.dp(51.0f));
            FileLog.e(ex);
        }
        this.captionLayout = null;
        final String caption = this.caption;
        if (caption != null && caption.length() > 0) {
            final Editable text = this.getText();
            if (((CharSequence)text).length() > 1 && ((CharSequence)text).charAt(0) == '@') {
                index = TextUtils.indexOf((CharSequence)text, ' ');
                if (index != -1) {
                    final TextPaint paint = this.getPaint();
                    ++index;
                    final CharSequence subSequence = ((CharSequence)text).subSequence(0, index);
                    index = (int)Math.ceil(paint.measureText((CharSequence)text, 0, index));
                    final int measuredWidth = this.getMeasuredWidth();
                    final int paddingLeft = this.getPaddingLeft();
                    paddingRight = this.getPaddingRight();
                    this.userNameLength = subSequence.length();
                    final String caption2 = this.caption;
                    paddingRight = measuredWidth - paddingLeft - paddingRight - index;
                    final CharSequence ellipsize = TextUtils.ellipsize((CharSequence)caption2, paint, (float)paddingRight, TextUtils$TruncateAt.END);
                    this.xOffset = index;
                    try {
                        this.captionLayout = new StaticLayout(ellipsize, this.getPaint(), paddingRight, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                        if (this.captionLayout.getLineCount() > 0) {
                            this.xOffset += (int)(-this.captionLayout.getLineLeft(0));
                        }
                        this.yOffset = (this.getMeasuredHeight() - this.captionLayout.getLineBottom(0)) / 2 + AndroidUtilities.dp(0.5f);
                    }
                    catch (Exception ex2) {
                        FileLog.e(ex2);
                    }
                }
            }
        }
    }
    
    public void onWindowFocusChanged(final boolean b) {
        if (Build$VERSION.SDK_INT < 23 && !b && this.copyPasteShowed) {
            return;
        }
        super.onWindowFocusChanged(b);
    }
    
    public void setCaption(String caption) {
        final String caption2 = this.caption;
        if ((caption2 != null && caption2.length() != 0) || (caption != null && caption.length() != 0)) {
            final String caption3 = this.caption;
            if (caption3 == null || caption == null || !caption3.equals(caption)) {
                this.caption = caption;
                caption = this.caption;
                if (caption != null) {
                    this.caption = caption.replace('\n', ' ');
                }
                this.requestLayout();
            }
        }
    }
    
    public void setDelegate(final EditTextCaptionDelegate delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public void setHintColor(final int n) {
        super.setHintColor(n);
        this.hintColor = n;
        this.invalidate();
    }
    
    public void setSelectionOverride(final int selectionStart, final int selectionEnd) {
        this.selectionStart = selectionStart;
        this.selectionEnd = selectionEnd;
    }
    
    public ActionMode startActionMode(final ActionMode$Callback actionMode$Callback) {
        return super.startActionMode(this.overrideCallback(actionMode$Callback));
    }
    
    public ActionMode startActionMode(final ActionMode$Callback actionMode$Callback, final int n) {
        return super.startActionMode(this.overrideCallback(actionMode$Callback), n);
    }
    
    public interface EditTextCaptionDelegate
    {
        void onSpansChanged();
    }
}
