// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.text.InputFilter;
import android.text.Editable;
import android.graphics.Point;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.Emoji;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import android.view.KeyEvent;
import android.content.DialogInterface;
import org.telegram.tgnet.TLRPC;
import android.view.View$OnClickListener;
import android.widget.ImageView$ScaleType;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.FileLog;
import android.view.MotionEvent;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import org.telegram.ui.ActionBar.BaseFragment;
import android.widget.ImageView;
import org.telegram.messenger.NotificationCenter;
import android.widget.FrameLayout;

public class EditTextEmoji extends FrameLayout implements NotificationCenterDelegate, SizeNotifierFrameLayoutDelegate
{
    public static final int STYLE_DIALOG = 1;
    public static final int STYLE_FRAGMENT = 0;
    private int currentStyle;
    private EditTextEmojiDelegate delegate;
    private boolean destroyed;
    private EditTextBoldCursor editText;
    private ImageView emojiButton;
    private int emojiPadding;
    private EmojiView emojiView;
    private boolean emojiViewVisible;
    private int innerTextChange;
    private boolean isPaused;
    private int keyboardHeight;
    private int keyboardHeightLand;
    private boolean keyboardVisible;
    private int lastSizeChangeValue1;
    private boolean lastSizeChangeValue2;
    private Runnable openKeyboardRunnable;
    private BaseFragment parentFragment;
    private boolean showKeyboardOnResume;
    private SizeNotifierFrameLayout sizeNotifierLayout;
    private boolean waitingForKeyboardOpen;
    
    public EditTextEmoji(final Context context, final SizeNotifierFrameLayout sizeNotifierLayout, final BaseFragment parentFragment, int currentStyle) {
        super(context);
        this.isPaused = true;
        this.openKeyboardRunnable = new Runnable() {
            @Override
            public void run() {
                if (!EditTextEmoji.this.destroyed && EditTextEmoji.this.editText != null && EditTextEmoji.this.waitingForKeyboardOpen && !EditTextEmoji.this.keyboardVisible && !AndroidUtilities.usingHardwareInput && !AndroidUtilities.isInMultiwindow && AndroidUtilities.isTablet()) {
                    EditTextEmoji.this.editText.requestFocus();
                    AndroidUtilities.showKeyboard((View)EditTextEmoji.this.editText);
                    AndroidUtilities.cancelRunOnUIThread(EditTextEmoji.this.openKeyboardRunnable);
                    AndroidUtilities.runOnUIThread(EditTextEmoji.this.openKeyboardRunnable, 100L);
                }
            }
        };
        this.currentStyle = currentStyle;
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
        this.parentFragment = parentFragment;
        (this.sizeNotifierLayout = sizeNotifierLayout).setDelegate((SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate)this);
        (this.editText = new EditTextBoldCursor(context) {
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                if (EditTextEmoji.this.isPopupShowing() && motionEvent.getAction() == 0) {
                    final EditTextEmoji this$0 = EditTextEmoji.this;
                    int n;
                    if (AndroidUtilities.usingHardwareInput) {
                        n = 0;
                    }
                    else {
                        n = 2;
                    }
                    this$0.showPopup(n);
                    EditTextEmoji.this.openKeyboardInternal();
                }
                if (motionEvent.getAction() == 0 && !AndroidUtilities.showKeyboard((View)this)) {
                    this.clearFocus();
                    this.requestFocus();
                }
                try {
                    return super.onTouchEvent(motionEvent);
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                    return false;
                }
            }
        }).setTextSize(1, 16.0f);
        this.editText.setImeOptions(268435456);
        this.editText.setInputType(16385);
        final EditTextBoldCursor editText = this.editText;
        editText.setFocusable(editText.isEnabled());
        this.editText.setCursorSize(AndroidUtilities.dp(20.0f));
        this.editText.setCursorWidth(1.5f);
        final int n = 5;
        if (currentStyle == 0) {
            final EditTextBoldCursor editText2 = this.editText;
            int n2;
            if (LocaleController.isRTL) {
                n2 = 5;
            }
            else {
                n2 = 3;
            }
            editText2.setGravity(n2 | 0x10);
            this.editText.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
            this.editText.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.editText.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.editText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            final EditTextBoldCursor editText3 = this.editText;
            int dp;
            if (LocaleController.isRTL) {
                dp = AndroidUtilities.dp(40.0f);
            }
            else {
                dp = 0;
            }
            int dp2;
            if (LocaleController.isRTL) {
                dp2 = 0;
            }
            else {
                dp2 = AndroidUtilities.dp(40.0f);
            }
            editText3.setPadding(dp, 0, dp2, AndroidUtilities.dp(8.0f));
            final EditTextBoldCursor editText4 = this.editText;
            final boolean isRTL = LocaleController.isRTL;
            float n3 = 0.0f;
            float n4;
            if (isRTL) {
                n4 = 11.0f;
            }
            else {
                n4 = 0.0f;
            }
            if (!LocaleController.isRTL) {
                n3 = 11.0f;
            }
            this.addView((View)editText4, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 19, n4, 1.0f, n3, 0.0f));
        }
        else {
            this.editText.setGravity(19);
            this.editText.setHintTextColor(Theme.getColor("dialogTextHint"));
            this.editText.setTextColor(Theme.getColor("dialogTextBlack"));
            this.editText.setBackgroundDrawable((Drawable)null);
            this.editText.setPadding(0, 0, 0, 0);
            this.addView((View)this.editText, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 19, 48.0f, 0.0f, 0.0f, 0.0f));
        }
        (this.emojiButton = new ImageView(context)).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_messagePanelIcons"), PorterDuff$Mode.MULTIPLY));
        this.emojiButton.setScaleType(ImageView$ScaleType.CENTER_INSIDE);
        if (currentStyle == 0) {
            this.emojiButton.setPadding(0, 0, 0, AndroidUtilities.dp(7.0f));
            this.emojiButton.setImageResource(2131165852);
            final ImageView emojiButton = this.emojiButton;
            currentStyle = n;
            if (LocaleController.isRTL) {
                currentStyle = 3;
            }
            this.addView((View)emojiButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f, currentStyle | 0x10, 0.0f, 0.0f, 0.0f, 0.0f));
        }
        else {
            this.emojiButton.setImageResource(2131165492);
            this.addView((View)this.emojiButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f, 19, 0.0f, 0.0f, 0.0f, 0.0f));
        }
        this.emojiButton.setOnClickListener((View$OnClickListener)new _$$Lambda$EditTextEmoji$2rCQ8jv3el2lKWMAASuH1xjI9xg(this));
        this.emojiButton.setContentDescription((CharSequence)LocaleController.getString("Emoji", 2131559331));
    }
    
    private void createEmojiView() {
        if (this.emojiView != null) {
            return;
        }
        (this.emojiView = new EmojiView(false, false, this.getContext(), false, null)).setVisibility(8);
        if (AndroidUtilities.isTablet()) {
            this.emojiView.setForseMultiwindowLayout(true);
        }
        this.emojiView.setDelegate((EmojiView.EmojiViewDelegate)new EmojiView.EmojiViewDelegate() {
            @Override
            public boolean onBackspace() {
                if (EditTextEmoji.this.editText.length() == 0) {
                    return false;
                }
                EditTextEmoji.this.editText.dispatchKeyEvent(new KeyEvent(0, 67));
                return true;
            }
            
            @Override
            public void onClearEmojiRecent() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(EditTextEmoji.this.getContext());
                builder.setTitle(LocaleController.getString("AppName", 2131558635));
                builder.setMessage(LocaleController.getString("ClearRecentEmoji", 2131559113));
                builder.setPositiveButton(LocaleController.getString("ClearButton", 2131559102).toUpperCase(), (DialogInterface$OnClickListener)new _$$Lambda$EditTextEmoji$3$WhQ1USJV_3duKh9fhejflvEi8U0(this));
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                if (EditTextEmoji.this.parentFragment != null) {
                    EditTextEmoji.this.parentFragment.showDialog(builder.create());
                }
                else {
                    builder.show();
                }
            }
            
            @Override
            public void onEmojiSelected(final String s) {
                Label_0019: {
                    int selectionEnd;
                    if ((selectionEnd = EditTextEmoji.this.editText.getSelectionEnd()) >= 0) {
                        break Label_0019;
                    }
                    selectionEnd = 0;
                    try {
                        try {
                            EditTextEmoji.this.innerTextChange = 2;
                            final CharSequence replaceEmoji = Emoji.replaceEmoji(s, EditTextEmoji.this.editText.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                            EditTextEmoji.this.editText.setText((CharSequence)EditTextEmoji.this.editText.getText().insert(selectionEnd, replaceEmoji));
                            final int n = selectionEnd + replaceEmoji.length();
                            EditTextEmoji.this.editText.setSelection(n, n);
                        }
                        finally {}
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
                EditTextEmoji.this.innerTextChange = 0;
                return;
                EditTextEmoji.this.innerTextChange = 0;
            }
        });
        this.sizeNotifierLayout.addView((View)this.emojiView);
    }
    
    private void onWindowSizeChanged() {
        int height = this.sizeNotifierLayout.getHeight();
        if (!this.keyboardVisible) {
            height -= this.emojiPadding;
        }
        final EditTextEmojiDelegate delegate = this.delegate;
        if (delegate != null) {
            delegate.onWindowSizeChanged(height);
        }
    }
    
    private void openKeyboardInternal() {
        int n;
        if (!AndroidUtilities.usingHardwareInput && !this.isPaused) {
            n = 2;
        }
        else {
            n = 0;
        }
        this.showPopup(n);
        this.editText.requestFocus();
        AndroidUtilities.showKeyboard((View)this.editText);
        if (this.isPaused) {
            this.showKeyboardOnResume = true;
        }
        else if (!AndroidUtilities.usingHardwareInput && !this.keyboardVisible && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
            this.waitingForKeyboardOpen = true;
            AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
            AndroidUtilities.runOnUIThread(this.openKeyboardRunnable, 100L);
        }
    }
    
    private void showPopup(int n) {
        if (n == 1) {
            if (this.emojiView == null) {
                this.createEmojiView();
            }
            this.emojiView.setVisibility(0);
            this.emojiViewVisible = true;
            final EmojiView emojiView = this.emojiView;
            if (this.keyboardHeight <= 0) {
                if (AndroidUtilities.isTablet()) {
                    this.keyboardHeight = AndroidUtilities.dp(150.0f);
                }
                else {
                    this.keyboardHeight = MessagesController.getGlobalEmojiSettings().getInt("kbd_height", AndroidUtilities.dp(200.0f));
                }
            }
            if (this.keyboardHeightLand <= 0) {
                if (AndroidUtilities.isTablet()) {
                    this.keyboardHeightLand = AndroidUtilities.dp(150.0f);
                }
                else {
                    this.keyboardHeightLand = MessagesController.getGlobalEmojiSettings().getInt("kbd_height_land3", AndroidUtilities.dp(200.0f));
                }
            }
            final Point displaySize = AndroidUtilities.displaySize;
            if (displaySize.x > displaySize.y) {
                n = this.keyboardHeightLand;
            }
            else {
                n = this.keyboardHeight;
            }
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)((View)emojiView).getLayoutParams();
            layoutParams.height = n;
            ((View)emojiView).setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            if (!AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                AndroidUtilities.hideKeyboard((View)this.editText);
            }
            final SizeNotifierFrameLayout sizeNotifierLayout = this.sizeNotifierLayout;
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
                if (this.currentStyle == 0) {
                    emojiButton.setImageResource(2131165852);
                }
                else {
                    emojiButton.setImageResource(2131165492);
                }
            }
            if (this.emojiView != null) {
                this.emojiViewVisible = false;
                if (AndroidUtilities.usingHardwareInput || AndroidUtilities.isInMultiwindow) {
                    this.emojiView.setVisibility(8);
                }
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
    
    public void closeKeyboard() {
        AndroidUtilities.hideKeyboard((View)this.editText);
    }
    
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.emojiDidLoad) {
            final EmojiView emojiView = this.emojiView;
            if (emojiView != null) {
                emojiView.invalidateViews();
            }
        }
    }
    
    public EditTextBoldCursor getEditText() {
        return this.editText;
    }
    
    public int getEmojiPadding() {
        return this.emojiPadding;
    }
    
    public Editable getText() {
        return this.editText.getText();
    }
    
    public void hideEmojiView() {
        if (!this.emojiViewVisible) {
            final EmojiView emojiView = this.emojiView;
            if (emojiView != null && emojiView.getVisibility() != 8) {
                this.emojiView.setVisibility(8);
            }
        }
    }
    
    public void hidePopup(final boolean b) {
        if (this.isPopupShowing()) {
            this.showPopup(0);
        }
        if (b) {
            this.hideEmojiView();
        }
    }
    
    public boolean isKeyboardVisible() {
        return this.keyboardVisible;
    }
    
    public boolean isPopupShowing() {
        return this.emojiViewVisible;
    }
    
    public boolean isPopupView(final View view) {
        return view == this.emojiView;
    }
    
    public int length() {
        return this.editText.length();
    }
    
    public void onDestroy() {
        this.destroyed = true;
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
        final EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.onDestroy();
        }
        final SizeNotifierFrameLayout sizeNotifierLayout = this.sizeNotifierLayout;
        if (sizeNotifierLayout != null) {
            sizeNotifierLayout.setDelegate(null);
        }
    }
    
    public void onPause() {
        this.isPaused = true;
        this.closeKeyboard();
    }
    
    public void onResume() {
        this.isPaused = false;
        if (this.showKeyboardOnResume) {
            this.showKeyboardOnResume = false;
            this.editText.requestFocus();
            AndroidUtilities.showKeyboard((View)this.editText);
            if (!AndroidUtilities.usingHardwareInput && !this.keyboardVisible && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                this.waitingForKeyboardOpen = true;
                AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
                AndroidUtilities.runOnUIThread(this.openKeyboardRunnable, 100L);
            }
        }
    }
    
    public void onSizeChanged(final int lastSizeChangeValue1, final boolean lastSizeChangeValue2) {
        if (lastSizeChangeValue1 > AndroidUtilities.dp(50.0f) && this.keyboardVisible && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
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
        if (this.keyboardVisible && this.waitingForKeyboardOpen) {
            this.waitingForKeyboardOpen = false;
            AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
        }
        this.onWindowSizeChanged();
    }
    
    public void openKeyboard() {
        AndroidUtilities.showKeyboard((View)this.editText);
    }
    
    public void setDelegate(final EditTextEmojiDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setEnabled(final boolean enabled) {
        this.editText.setEnabled(enabled);
        final ImageView emojiButton = this.emojiButton;
        int visibility;
        if (enabled) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        emojiButton.setVisibility(visibility);
        if (enabled) {
            final EditTextBoldCursor editText = this.editText;
            int dp;
            if (LocaleController.isRTL) {
                dp = AndroidUtilities.dp(40.0f);
            }
            else {
                dp = 0;
            }
            int dp2;
            if (LocaleController.isRTL) {
                dp2 = 0;
            }
            else {
                dp2 = AndroidUtilities.dp(40.0f);
            }
            editText.setPadding(dp, 0, dp2, AndroidUtilities.dp(8.0f));
        }
        else {
            this.editText.setPadding(0, 0, 0, AndroidUtilities.dp(8.0f));
        }
    }
    
    public void setFilters(final InputFilter[] filters) {
        this.editText.setFilters(filters);
    }
    
    public void setFocusable(final boolean focusable) {
        this.editText.setFocusable(focusable);
    }
    
    public void setHint(final CharSequence hint) {
        this.editText.setHint(hint);
    }
    
    public void setMaxLines(final int maxLines) {
        this.editText.setMaxLines(maxLines);
    }
    
    public void setSelection(final int selection) {
        this.editText.setSelection(selection);
    }
    
    public void setText(final CharSequence text) {
        this.editText.setText(text);
    }
    
    public interface EditTextEmojiDelegate
    {
        void onWindowSizeChanged(final int p0);
    }
}
