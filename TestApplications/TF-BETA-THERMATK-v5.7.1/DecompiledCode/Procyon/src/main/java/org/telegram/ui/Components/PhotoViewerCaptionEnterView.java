// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.messenger.UserConfig;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Point;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.messenger.MessagesController;
import android.annotation.SuppressLint;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import android.view.KeyEvent;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.Emoji;
import android.text.style.ImageSpan;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View$OnKeyListener;
import android.text.InputFilter$LengthFilter;
import android.text.InputFilter;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ActionMode$Callback;
import android.os.Build$VERSION;
import org.telegram.messenger.FileLog;
import android.view.View$MeasureSpec;
import org.telegram.messenger.LocaleController;
import android.view.View$OnClickListener;
import org.telegram.messenger.AndroidUtilities;
import android.widget.ImageView$ScaleType;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout;
import android.content.Context;
import android.view.View;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.text.TextPaint;
import android.widget.ImageView;
import android.view.ActionMode;
import org.telegram.messenger.NotificationCenter;
import android.widget.FrameLayout;

public class PhotoViewerCaptionEnterView extends FrameLayout implements NotificationCenterDelegate, SizeNotifierFrameLayoutPhotoDelegate
{
    float animationProgress;
    private int audioInterfaceState;
    private int captionMaxLength;
    private ActionMode currentActionMode;
    private PhotoViewerCaptionEnterViewDelegate delegate;
    private ImageView emojiButton;
    private int emojiPadding;
    private EmojiView emojiView;
    private boolean forceFloatingEmoji;
    private boolean innerTextChange;
    private int keyboardHeight;
    private int keyboardHeightLand;
    private boolean keyboardVisible;
    private int lastSizeChangeValue1;
    private boolean lastSizeChangeValue2;
    private String lengthText;
    private TextPaint lengthTextPaint;
    private EditTextCaption messageEditText;
    private AnimatorSet runningAnimation;
    private AnimatorSet runningAnimation2;
    private ObjectAnimator runningAnimationAudio;
    private int runningAnimationType;
    private SizeNotifierFrameLayoutPhoto sizeNotifierLayout;
    private View windowView;
    
    public PhotoViewerCaptionEnterView(final Context context, final SizeNotifierFrameLayoutPhoto sizeNotifierLayout, final View windowView) {
        super(context);
        this.captionMaxLength = 1024;
        this.animationProgress = 0.0f;
        this.setWillNotDraw(false);
        this.setBackgroundColor(2130706432);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.windowView = windowView;
        this.sizeNotifierLayout = sizeNotifierLayout;
        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(0);
        this.addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 2.0f, 0.0f, 0.0f, 0.0f));
        final FrameLayout frameLayout = new FrameLayout(context);
        linearLayout.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(0, -2, 1.0f));
        (this.emojiButton = new ImageView(context)).setImageResource(2131165492);
        this.emojiButton.setScaleType(ImageView$ScaleType.CENTER_INSIDE);
        this.emojiButton.setPadding(AndroidUtilities.dp(4.0f), AndroidUtilities.dp(1.0f), 0, 0);
        frameLayout.addView((View)this.emojiButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48, 83));
        this.emojiButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewerCaptionEnterView$ZqTKgld1Ygi5nBpyMN_nLjvd2fU(this));
        this.emojiButton.setContentDescription((CharSequence)LocaleController.getString("Emoji", 2131559331));
        (this.lengthTextPaint = new TextPaint(1)).setTextSize((float)AndroidUtilities.dp(13.0f));
        this.lengthTextPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.lengthTextPaint.setColor(-2500135);
        this.messageEditText = new EditTextCaption(context) {
            @Override
            protected void onMeasure(final int n, final int n2) {
                try {
                    super.onMeasure(n, n2);
                }
                catch (Exception ex) {
                    this.setMeasuredDimension(View$MeasureSpec.getSize(n), AndroidUtilities.dp(51.0f));
                    FileLog.e(ex);
                }
            }
            
            protected void onSelectionChanged(final int n, final int n2) {
                super.onSelectionChanged(n, n2);
                if (n != n2) {
                    this.fixHandleView(false);
                }
                else {
                    this.fixHandleView(true);
                }
            }
        };
        if (Build$VERSION.SDK_INT >= 23 && this.windowView != null) {
            this.messageEditText.setCustomSelectionActionModeCallback((ActionMode$Callback)new ActionMode$Callback() {
                public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
                    return false;
                }
                
                public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
                    PhotoViewerCaptionEnterView.this.currentActionMode = actionMode;
                    return true;
                }
                
                public void onDestroyActionMode(final ActionMode actionMode) {
                    if (PhotoViewerCaptionEnterView.this.currentActionMode == actionMode) {
                        PhotoViewerCaptionEnterView.this.currentActionMode = null;
                    }
                }
                
                public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
                    if (Build$VERSION.SDK_INT >= 23) {
                        PhotoViewerCaptionEnterView.this.fixActionMode(actionMode);
                    }
                    return true;
                }
            });
            this.messageEditText.setCustomInsertionActionModeCallback((ActionMode$Callback)new ActionMode$Callback() {
                public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
                    return false;
                }
                
                public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
                    PhotoViewerCaptionEnterView.this.currentActionMode = actionMode;
                    return true;
                }
                
                public void onDestroyActionMode(final ActionMode actionMode) {
                    if (PhotoViewerCaptionEnterView.this.currentActionMode == actionMode) {
                        PhotoViewerCaptionEnterView.this.currentActionMode = null;
                    }
                }
                
                public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
                    if (Build$VERSION.SDK_INT >= 23) {
                        PhotoViewerCaptionEnterView.this.fixActionMode(actionMode);
                    }
                    return true;
                }
            });
        }
        this.messageEditText.setHint((CharSequence)LocaleController.getString("AddCaption", 2131558566));
        this.messageEditText.setImeOptions(268435456);
        final EditTextCaption messageEditText = this.messageEditText;
        messageEditText.setInputType(messageEditText.getInputType() | 0x4000);
        this.messageEditText.setMaxLines(4);
        this.messageEditText.setHorizontallyScrolling(false);
        this.messageEditText.setTextSize(1, 18.0f);
        this.messageEditText.setGravity(80);
        this.messageEditText.setPadding(0, AndroidUtilities.dp(11.0f), 0, AndroidUtilities.dp(12.0f));
        this.messageEditText.setBackgroundDrawable((Drawable)null);
        this.messageEditText.setCursorColor(-1);
        this.messageEditText.setCursorSize(AndroidUtilities.dp(20.0f));
        this.messageEditText.setTextColor(-1);
        this.messageEditText.setHintTextColor(-1291845633);
        this.messageEditText.setFilters(new InputFilter[] { (InputFilter)new InputFilter$LengthFilter(this.captionMaxLength) });
        frameLayout.addView((View)this.messageEditText, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 83, 52.0f, 0.0f, 6.0f, 0.0f));
        this.messageEditText.setOnKeyListener((View$OnKeyListener)new _$$Lambda$PhotoViewerCaptionEnterView$26OMPpPrCbxQi1_Ug7Wt6hHeAnU(this));
        this.messageEditText.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewerCaptionEnterView$JGqljW5ddJcqpomGAK04x75zT4E(this));
        this.messageEditText.addTextChangedListener((TextWatcher)new TextWatcher() {
            boolean processChange = false;
            
            public void afterTextChanged(final Editable editable) {
                final int i = PhotoViewerCaptionEnterView.this.captionMaxLength - PhotoViewerCaptionEnterView.this.messageEditText.length();
                if (i <= 128) {
                    PhotoViewerCaptionEnterView.this.lengthText = String.format("%d", i);
                }
                else {
                    PhotoViewerCaptionEnterView.this.lengthText = null;
                }
                PhotoViewerCaptionEnterView.this.invalidate();
                if (PhotoViewerCaptionEnterView.this.innerTextChange) {
                    return;
                }
                if (this.processChange) {
                    final ImageSpan[] array = (ImageSpan[])editable.getSpans(0, editable.length(), (Class)ImageSpan.class);
                    for (int j = 0; j < array.length; ++j) {
                        editable.removeSpan((Object)array[j]);
                    }
                    Emoji.replaceEmoji((CharSequence)editable, PhotoViewerCaptionEnterView.this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                    this.processChange = false;
                }
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                if (PhotoViewerCaptionEnterView.this.innerTextChange) {
                    return;
                }
                if (PhotoViewerCaptionEnterView.this.delegate != null) {
                    PhotoViewerCaptionEnterView.this.delegate.onTextChanged(charSequence);
                }
                if (n2 != n3 && n3 - n2 > 1) {
                    this.processChange = true;
                }
            }
        });
        final CombinedDrawable imageDrawable = new CombinedDrawable(Theme.createCircleDrawable(AndroidUtilities.dp(16.0f), -10043398), context.getResources().getDrawable(2131165484).mutate(), 0, AndroidUtilities.dp(1.0f));
        imageDrawable.setCustomSize(AndroidUtilities.dp(32.0f), AndroidUtilities.dp(32.0f));
        final ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView$ScaleType.CENTER);
        imageView.setImageDrawable((Drawable)imageDrawable);
        linearLayout.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(48, 48, 80));
        imageView.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewerCaptionEnterView$XlBwyOEM4HKN92dDL_UcK7dQCVE(this));
        imageView.setContentDescription((CharSequence)LocaleController.getString("Done", 2131559299));
    }
    
    private void createEmojiView() {
        if (this.emojiView != null) {
            return;
        }
        (this.emojiView = new EmojiView(false, false, this.getContext(), false, null)).setDelegate((EmojiView.EmojiViewDelegate)new EmojiView.EmojiViewDelegate() {
            @Override
            public boolean onBackspace() {
                if (PhotoViewerCaptionEnterView.this.messageEditText.length() == 0) {
                    return false;
                }
                PhotoViewerCaptionEnterView.this.messageEditText.dispatchKeyEvent(new KeyEvent(0, 67));
                return true;
            }
            
            @Override
            public void onEmojiSelected(final String s) {
                if (PhotoViewerCaptionEnterView.this.messageEditText.length() + s.length() > PhotoViewerCaptionEnterView.this.captionMaxLength) {
                    return;
                }
                Label_0045: {
                    int selectionEnd;
                    if ((selectionEnd = PhotoViewerCaptionEnterView.this.messageEditText.getSelectionEnd()) >= 0) {
                        break Label_0045;
                    }
                    selectionEnd = 0;
                    try {
                        try {
                            PhotoViewerCaptionEnterView.this.innerTextChange = true;
                            final CharSequence replaceEmoji = Emoji.replaceEmoji(s, PhotoViewerCaptionEnterView.this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                            PhotoViewerCaptionEnterView.this.messageEditText.setText((CharSequence)PhotoViewerCaptionEnterView.this.messageEditText.getText().insert(selectionEnd, replaceEmoji));
                            final int n = selectionEnd + replaceEmoji.length();
                            PhotoViewerCaptionEnterView.this.messageEditText.setSelection(n, n);
                        }
                        finally {}
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
                PhotoViewerCaptionEnterView.this.innerTextChange = false;
                return;
                PhotoViewerCaptionEnterView.this.innerTextChange = false;
            }
        });
        this.sizeNotifierLayout.addView((View)this.emojiView);
    }
    
    @SuppressLint({ "PrivateApi" })
    private void fixActionMode(final ActionMode actionMode) {
        try {
            final Class<?> forName = Class.forName("com.android.internal.view.FloatingActionMode");
            final Field declaredField = forName.getDeclaredField("mFloatingToolbar");
            declaredField.setAccessible(true);
            final Object value = declaredField.get(actionMode);
            final Class<?> forName2 = Class.forName("com.android.internal.widget.FloatingToolbar");
            final Field declaredField2 = forName2.getDeclaredField("mPopup");
            final Field declaredField3 = forName2.getDeclaredField("mWidthChanged");
            declaredField2.setAccessible(true);
            declaredField3.setAccessible(true);
            final Object value2 = declaredField2.get(value);
            final Field declaredField4 = Class.forName("com.android.internal.widget.FloatingToolbar$FloatingToolbarPopup").getDeclaredField("mParent");
            declaredField4.setAccessible(true);
            if (declaredField4.get(value2) != this.windowView) {
                declaredField4.set(value2, this.windowView);
                final Method declaredMethod = forName.getDeclaredMethod("updateViewLocationInWindow", (Class<?>[])new Class[0]);
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(actionMode, new Object[0]);
            }
        }
        catch (Throwable t) {
            FileLog.e(t);
        }
    }
    
    private void onWindowSizeChanged() {
        int height = this.sizeNotifierLayout.getHeight();
        if (!this.keyboardVisible) {
            height -= this.emojiPadding;
        }
        final PhotoViewerCaptionEnterViewDelegate delegate = this.delegate;
        if (delegate != null) {
            delegate.onWindowSizeChanged(height);
        }
    }
    
    private void openKeyboardInternal() {
        int n;
        if (AndroidUtilities.usingHardwareInput) {
            n = 0;
        }
        else {
            n = 2;
        }
        this.showPopup(n);
        this.openKeyboard();
    }
    
    private void showPopup(int n) {
        if (n == 1) {
            if (this.emojiView == null) {
                this.createEmojiView();
            }
            this.emojiView.setVisibility(0);
            if (this.keyboardHeight <= 0) {
                this.keyboardHeight = MessagesController.getGlobalEmojiSettings().getInt("kbd_height", AndroidUtilities.dp(200.0f));
            }
            if (this.keyboardHeightLand <= 0) {
                this.keyboardHeightLand = MessagesController.getGlobalEmojiSettings().getInt("kbd_height_land3", AndroidUtilities.dp(200.0f));
            }
            final Point displaySize = AndroidUtilities.displaySize;
            if (displaySize.x > displaySize.y) {
                n = this.keyboardHeightLand;
            }
            else {
                n = this.keyboardHeight;
            }
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.emojiView.getLayoutParams();
            layoutParams.width = AndroidUtilities.displaySize.x;
            layoutParams.height = n;
            this.emojiView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            if (!AndroidUtilities.isInMultiwindow && !this.forceFloatingEmoji) {
                AndroidUtilities.hideKeyboard((View)this.messageEditText);
            }
            final SizeNotifierFrameLayoutPhoto sizeNotifierLayout = this.sizeNotifierLayout;
            if (sizeNotifierLayout != null) {
                this.emojiPadding = n;
                sizeNotifierLayout.requestLayout();
                this.emojiButton.setImageResource(2131165487);
                this.onWindowSizeChanged();
            }
        }
        else {
            final ImageView emojiButton = this.emojiButton;
            if (emojiButton != null) {
                emojiButton.setImageResource(2131165492);
            }
            final EmojiView emojiView = this.emojiView;
            if (emojiView != null) {
                emojiView.setVisibility(8);
            }
            if (this.sizeNotifierLayout != null) {
                if (n == 0) {
                    this.emojiPadding = 0;
                }
                this.sizeNotifierLayout.requestLayout();
                this.onWindowSizeChanged();
            }
        }
    }
    
    public void addEmojiToRecent(final String s) {
        this.createEmojiView();
        this.emojiView.addEmojiToRecent(s);
    }
    
    public void closeKeyboard() {
        AndroidUtilities.hideKeyboard((View)this.messageEditText);
    }
    
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.emojiDidLoad) {
            final EmojiView emojiView = this.emojiView;
            if (emojiView != null) {
                emojiView.invalidateViews();
            }
        }
    }
    
    public int getCursorPosition() {
        final EditTextCaption messageEditText = this.messageEditText;
        if (messageEditText == null) {
            return 0;
        }
        return messageEditText.getSelectionStart();
    }
    
    public int getEmojiPadding() {
        return this.emojiPadding;
    }
    
    public CharSequence getFieldCharSequence() {
        return AndroidUtilities.getTrimmedString((CharSequence)this.messageEditText.getText());
    }
    
    public int getSelectionLength() {
        final EditTextCaption messageEditText = this.messageEditText;
        if (messageEditText == null) {
            return 0;
        }
        try {
            return messageEditText.getSelectionEnd() - this.messageEditText.getSelectionStart();
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return 0;
        }
    }
    
    public boolean hideActionMode() {
        if (Build$VERSION.SDK_INT >= 23) {
            final ActionMode currentActionMode = this.currentActionMode;
            if (currentActionMode != null) {
                try {
                    currentActionMode.finish();
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                this.currentActionMode = null;
                return true;
            }
        }
        return false;
    }
    
    public void hidePopup() {
        if (this.isPopupShowing()) {
            this.showPopup(0);
        }
    }
    
    public boolean isKeyboardVisible() {
        return (AndroidUtilities.usingHardwareInput && this.getTag() != null) || this.keyboardVisible;
    }
    
    public boolean isPopupShowing() {
        final EmojiView emojiView = this.emojiView;
        return emojiView != null && emojiView.getVisibility() == 0;
    }
    
    public boolean isPopupView(final View view) {
        return view == this.emojiView;
    }
    
    public void onCreate() {
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
        this.sizeNotifierLayout.setDelegate((SizeNotifierFrameLayoutPhoto.SizeNotifierFrameLayoutPhotoDelegate)this);
    }
    
    public void onDestroy() {
        this.hidePopup();
        if (this.isKeyboardVisible()) {
            this.closeKeyboard();
        }
        this.keyboardVisible = false;
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
        final SizeNotifierFrameLayoutPhoto sizeNotifierLayout = this.sizeNotifierLayout;
        if (sizeNotifierLayout != null) {
            sizeNotifierLayout.setDelegate(null);
        }
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.lengthText != null && this.getMeasuredHeight() > AndroidUtilities.dp(48.0f)) {
            canvas.drawText(this.lengthText, (float)((AndroidUtilities.dp(56.0f) - (int)Math.ceil(this.lengthTextPaint.measureText(this.lengthText))) / 2), (float)(this.getMeasuredHeight() - AndroidUtilities.dp(48.0f)), (Paint)this.lengthTextPaint);
            final float animationProgress = this.animationProgress;
            if (animationProgress < 1.0f) {
                this.animationProgress = animationProgress + 0.14166667f;
                this.invalidate();
                if (this.animationProgress >= 1.0f) {
                    this.animationProgress = 1.0f;
                }
                this.lengthTextPaint.setAlpha((int)(this.animationProgress * 255.0f));
            }
        }
        else {
            this.lengthTextPaint.setAlpha(0);
            this.animationProgress = 0.0f;
        }
    }
    
    public void onSizeChanged(final int lastSizeChangeValue1, final boolean lastSizeChangeValue2) {
        if (lastSizeChangeValue1 > AndroidUtilities.dp(50.0f) && this.keyboardVisible && !AndroidUtilities.isInMultiwindow && !this.forceFloatingEmoji) {
            if (lastSizeChangeValue2) {
                this.keyboardHeightLand = lastSizeChangeValue1;
                MessagesController.getGlobalEmojiSettings().edit().putInt("kbd_height_land3", this.keyboardHeightLand).commit();
            }
            else {
                this.keyboardHeight = lastSizeChangeValue1;
                MessagesController.getGlobalEmojiSettings().edit().putInt("kbd_height", this.keyboardHeight).commit();
            }
        }
        if (this.isPopupShowing()) {
            int height;
            if (lastSizeChangeValue2) {
                height = this.keyboardHeightLand;
            }
            else {
                height = this.keyboardHeight;
            }
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.emojiView.getLayoutParams();
            if (layoutParams.width != AndroidUtilities.displaySize.x || layoutParams.height != height) {
                layoutParams.width = AndroidUtilities.displaySize.x;
                layoutParams.height = height;
                this.emojiView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                if (this.sizeNotifierLayout != null) {
                    this.emojiPadding = layoutParams.height;
                    this.sizeNotifierLayout.requestLayout();
                    this.onWindowSizeChanged();
                }
            }
        }
        if (this.lastSizeChangeValue1 == lastSizeChangeValue1 && this.lastSizeChangeValue2 == lastSizeChangeValue2) {
            this.onWindowSizeChanged();
            return;
        }
        this.lastSizeChangeValue1 = lastSizeChangeValue1;
        this.lastSizeChangeValue2 = lastSizeChangeValue2;
        final boolean keyboardVisible = this.keyboardVisible;
        this.keyboardVisible = (lastSizeChangeValue1 > 0);
        if (this.keyboardVisible && this.isPopupShowing()) {
            this.showPopup(0);
        }
        if (this.emojiPadding != 0) {
            final boolean keyboardVisible2 = this.keyboardVisible;
            if (!keyboardVisible2 && keyboardVisible2 != keyboardVisible && !this.isPopupShowing()) {
                this.emojiPadding = 0;
                this.sizeNotifierLayout.requestLayout();
            }
        }
        this.onWindowSizeChanged();
    }
    
    public void openKeyboard() {
        int selection;
        try {
            selection = this.messageEditText.getSelectionStart();
        }
        catch (Exception ex) {
            selection = this.messageEditText.length();
            FileLog.e(ex);
        }
        final MotionEvent obtain = MotionEvent.obtain(0L, 0L, 0, 0.0f, 0.0f, 0);
        this.messageEditText.onTouchEvent(obtain);
        obtain.recycle();
        final MotionEvent obtain2 = MotionEvent.obtain(0L, 0L, 1, 0.0f, 0.0f, 0);
        this.messageEditText.onTouchEvent(obtain2);
        obtain2.recycle();
        AndroidUtilities.showKeyboard((View)this.messageEditText);
        try {
            this.messageEditText.setSelection(selection);
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
    }
    
    public void replaceWithText(final int n, final int n2, final CharSequence charSequence, final boolean b) {
        try {
            final SpannableStringBuilder text = new SpannableStringBuilder((CharSequence)this.messageEditText.getText());
            text.replace(n, n2 + n, charSequence);
            if (b) {
                Emoji.replaceEmoji((CharSequence)text, this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            }
            this.messageEditText.setText((CharSequence)text);
            if (charSequence.length() + n <= this.messageEditText.length()) {
                this.messageEditText.setSelection(n + charSequence.length());
            }
            else {
                this.messageEditText.setSelection(this.messageEditText.length());
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public void setDelegate(final PhotoViewerCaptionEnterViewDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setFieldFocused(final boolean b) {
        final EditTextCaption messageEditText = this.messageEditText;
        if (messageEditText == null) {
            return;
        }
        if (b) {
            if (!messageEditText.isFocused()) {
                this.messageEditText.postDelayed((Runnable)new _$$Lambda$PhotoViewerCaptionEnterView$DD_m3yT_F769ozmEz6bVJGE02zA(this), 600L);
            }
        }
        else if (messageEditText.isFocused() && !this.keyboardVisible) {
            this.messageEditText.clearFocus();
        }
    }
    
    public void setFieldText(final CharSequence text) {
        final EditTextCaption messageEditText = this.messageEditText;
        if (messageEditText == null) {
            return;
        }
        messageEditText.setText(text);
        final EditTextCaption messageEditText2 = this.messageEditText;
        messageEditText2.setSelection(messageEditText2.getText().length());
        final PhotoViewerCaptionEnterViewDelegate delegate = this.delegate;
        if (delegate != null) {
            delegate.onTextChanged((CharSequence)this.messageEditText.getText());
        }
        final int captionMaxLength = this.captionMaxLength;
        this.captionMaxLength = MessagesController.getInstance(UserConfig.selectedAccount).maxCaptionLength;
        final int captionMaxLength2 = this.captionMaxLength;
        if (captionMaxLength != captionMaxLength2) {
            this.messageEditText.setFilters(new InputFilter[] { (InputFilter)new InputFilter$LengthFilter(captionMaxLength2) });
        }
    }
    
    public void setForceFloatingEmoji(final boolean forceFloatingEmoji) {
        this.forceFloatingEmoji = forceFloatingEmoji;
    }
    
    public interface PhotoViewerCaptionEnterViewDelegate
    {
        void onCaptionEnter();
        
        void onTextChanged(final CharSequence p0);
        
        void onWindowSizeChanged(final int p0);
    }
}
