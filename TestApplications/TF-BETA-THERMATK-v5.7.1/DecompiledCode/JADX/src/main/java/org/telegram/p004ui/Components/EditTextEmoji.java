package org.telegram.p004ui.Components;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.text.Editable;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.p004ui.ActionBar.AlertDialog.Builder;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.EmojiView.EmojiViewDelegate;
import org.telegram.p004ui.Components.EmojiView.EmojiViewDelegate.C2837-CC;
import org.telegram.p004ui.Components.SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.InputStickerSet;
import org.telegram.tgnet.TLRPC.StickerSet;
import org.telegram.tgnet.TLRPC.StickerSetCovered;

/* renamed from: org.telegram.ui.Components.EditTextEmoji */
public class EditTextEmoji extends FrameLayout implements NotificationCenterDelegate, SizeNotifierFrameLayoutDelegate {
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
    private boolean isPaused = true;
    private int keyboardHeight;
    private int keyboardHeightLand;
    private boolean keyboardVisible;
    private int lastSizeChangeValue1;
    private boolean lastSizeChangeValue2;
    private Runnable openKeyboardRunnable = new C28051();
    private BaseFragment parentFragment;
    private boolean showKeyboardOnResume;
    private SizeNotifierFrameLayout sizeNotifierLayout;
    private boolean waitingForKeyboardOpen;

    /* renamed from: org.telegram.ui.Components.EditTextEmoji$1 */
    class C28051 implements Runnable {
        C28051() {
        }

        public void run() {
            if (!EditTextEmoji.this.destroyed && EditTextEmoji.this.editText != null && EditTextEmoji.this.waitingForKeyboardOpen && !EditTextEmoji.this.keyboardVisible && !AndroidUtilities.usingHardwareInput && !AndroidUtilities.isInMultiwindow && AndroidUtilities.isTablet()) {
                EditTextEmoji.this.editText.requestFocus();
                AndroidUtilities.showKeyboard(EditTextEmoji.this.editText);
                AndroidUtilities.cancelRunOnUIThread(EditTextEmoji.this.openKeyboardRunnable);
                AndroidUtilities.runOnUIThread(EditTextEmoji.this.openKeyboardRunnable, 100);
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.EditTextEmoji$EditTextEmojiDelegate */
    public interface EditTextEmojiDelegate {
        void onWindowSizeChanged(int i);
    }

    /* renamed from: org.telegram.ui.Components.EditTextEmoji$3 */
    class C40953 implements EmojiViewDelegate {
        public /* synthetic */ boolean isExpanded() {
            return C2837-CC.$default$isExpanded(this);
        }

        public /* synthetic */ boolean isSearchOpened() {
            return C2837-CC.$default$isSearchOpened(this);
        }

        public /* synthetic */ void onGifSelected(Object obj, Object obj2) {
            C2837-CC.$default$onGifSelected(this, obj, obj2);
        }

        public /* synthetic */ void onSearchOpenClose(int i) {
            C2837-CC.$default$onSearchOpenClose(this, i);
        }

        public /* synthetic */ void onShowStickerSet(StickerSet stickerSet, InputStickerSet inputStickerSet) {
            C2837-CC.$default$onShowStickerSet(this, stickerSet, inputStickerSet);
        }

        public /* synthetic */ void onStickerSelected(Document document, Object obj) {
            C2837-CC.$default$onStickerSelected(this, document, obj);
        }

        public /* synthetic */ void onStickerSetAdd(StickerSetCovered stickerSetCovered) {
            C2837-CC.$default$onStickerSetAdd(this, stickerSetCovered);
        }

        public /* synthetic */ void onStickerSetRemove(StickerSetCovered stickerSetCovered) {
            C2837-CC.$default$onStickerSetRemove(this, stickerSetCovered);
        }

        public /* synthetic */ void onStickersGroupClick(int i) {
            C2837-CC.$default$onStickersGroupClick(this, i);
        }

        public /* synthetic */ void onStickersSettingsClick() {
            C2837-CC.$default$onStickersSettingsClick(this);
        }

        public /* synthetic */ void onTabOpened(int i) {
            C2837-CC.$default$onTabOpened(this, i);
        }

        C40953() {
        }

        public boolean onBackspace() {
            if (EditTextEmoji.this.editText.length() == 0) {
                return false;
            }
            EditTextEmoji.this.editText.dispatchKeyEvent(new KeyEvent(0, 67));
            return true;
        }

        public void onEmojiSelected(String str) {
            int selectionEnd = EditTextEmoji.this.editText.getSelectionEnd();
            if (selectionEnd < 0) {
                selectionEnd = 0;
            }
            try {
                EditTextEmoji.this.innerTextChange = 2;
                CharSequence replaceEmoji = Emoji.replaceEmoji(str, EditTextEmoji.this.editText.getPaint().getFontMetricsInt(), AndroidUtilities.m26dp(20.0f), false);
                EditTextEmoji.this.editText.setText(EditTextEmoji.this.editText.getText().insert(selectionEnd, replaceEmoji));
                selectionEnd += replaceEmoji.length();
                EditTextEmoji.this.editText.setSelection(selectionEnd, selectionEnd);
            } catch (Exception e) {
                FileLog.m30e(e);
            } catch (Throwable th) {
                EditTextEmoji.this.innerTextChange = 0;
            }
            EditTextEmoji.this.innerTextChange = 0;
        }

        public void onClearEmojiRecent() {
            Builder builder = new Builder(EditTextEmoji.this.getContext());
            builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
            builder.setMessage(LocaleController.getString("ClearRecentEmoji", C1067R.string.ClearRecentEmoji));
            builder.setPositiveButton(LocaleController.getString("ClearButton", C1067R.string.ClearButton).toUpperCase(), new C2555-$$Lambda$EditTextEmoji$3$WhQ1USJV_3duKh9fhejflvEi8U0(this));
            builder.setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null);
            if (EditTextEmoji.this.parentFragment != null) {
                EditTextEmoji.this.parentFragment.showDialog(builder.create());
            } else {
                builder.show();
            }
        }

        public /* synthetic */ void lambda$onClearEmojiRecent$0$EditTextEmoji$3(DialogInterface dialogInterface, int i) {
            EditTextEmoji.this.emojiView.clearRecentEmoji();
        }
    }

    public EditTextEmoji(Context context, SizeNotifierFrameLayout sizeNotifierFrameLayout, BaseFragment baseFragment, int i) {
        super(context);
        this.currentStyle = i;
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
        this.parentFragment = baseFragment;
        this.sizeNotifierLayout = sizeNotifierFrameLayout;
        this.sizeNotifierLayout.setDelegate(this);
        this.editText = new EditTextBoldCursor(context) {
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (EditTextEmoji.this.isPopupShowing() && motionEvent.getAction() == 0) {
                    EditTextEmoji.this.showPopup(AndroidUtilities.usingHardwareInput ? 0 : 2);
                    EditTextEmoji.this.openKeyboardInternal();
                }
                if (motionEvent.getAction() == 0 && !AndroidUtilities.showKeyboard(this)) {
                    clearFocus();
                    requestFocus();
                }
                try {
                    return super.onTouchEvent(motionEvent);
                } catch (Exception e) {
                    FileLog.m30e(e);
                    return false;
                }
            }
        };
        this.editText.setTextSize(1, 16.0f);
        this.editText.setImeOptions(268435456);
        this.editText.setInputType(16385);
        EditTextBoldCursor editTextBoldCursor = this.editText;
        editTextBoldCursor.setFocusable(editTextBoldCursor.isEnabled());
        this.editText.setCursorSize(AndroidUtilities.m26dp(20.0f));
        this.editText.setCursorWidth(1.5f);
        int i2 = 5;
        if (i == 0) {
            this.editText.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
            this.editText.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
            this.editText.setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
            EditTextBoldCursor editTextBoldCursor2 = this.editText;
            String str = Theme.key_windowBackgroundWhiteBlackText;
            editTextBoldCursor2.setCursorColor(Theme.getColor(str));
            this.editText.setTextColor(Theme.getColor(str));
            this.editText.setPadding(LocaleController.isRTL ? AndroidUtilities.m26dp(40.0f) : 0, 0, LocaleController.isRTL ? 0 : AndroidUtilities.m26dp(40.0f), AndroidUtilities.m26dp(8.0f));
            editTextBoldCursor2 = this.editText;
            float f = 0.0f;
            float f2 = LocaleController.isRTL ? 11.0f : 0.0f;
            if (!LocaleController.isRTL) {
                f = 11.0f;
            }
            addView(editTextBoldCursor2, LayoutHelper.createFrame(-1, -2.0f, 19, f2, 1.0f, f, 0.0f));
        } else {
            this.editText.setGravity(19);
            this.editText.setHintTextColor(Theme.getColor(Theme.key_dialogTextHint));
            this.editText.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
            this.editText.setBackgroundDrawable(null);
            this.editText.setPadding(0, 0, 0, 0);
            addView(this.editText, LayoutHelper.createFrame(-1, -1.0f, 19, 48.0f, 0.0f, 0.0f, 0.0f));
        }
        this.emojiButton = new ImageView(context);
        this.emojiButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_messagePanelIcons), Mode.MULTIPLY));
        this.emojiButton.setScaleType(ScaleType.CENTER_INSIDE);
        if (i == 0) {
            this.emojiButton.setPadding(0, 0, 0, AndroidUtilities.m26dp(7.0f));
            this.emojiButton.setImageResource(C1067R.C1065drawable.smiles_tab_smiles);
            ImageView imageView = this.emojiButton;
            if (LocaleController.isRTL) {
                i2 = 3;
            }
            addView(imageView, LayoutHelper.createFrame(48, 48.0f, i2 | 16, 0.0f, 0.0f, 0.0f, 0.0f));
        } else {
            this.emojiButton.setImageResource(C1067R.C1065drawable.input_smile);
            addView(this.emojiButton, LayoutHelper.createFrame(48, 48.0f, 19, 0.0f, 0.0f, 0.0f, 0.0f));
        }
        this.emojiButton.setOnClickListener(new C2554-$$Lambda$EditTextEmoji$2rCQ8jv3el2lKWMAASuH1xjI9xg(this));
        this.emojiButton.setContentDescription(LocaleController.getString("Emoji", C1067R.string.Emoji));
    }

    public /* synthetic */ void lambda$new$0$EditTextEmoji(View view) {
        if (this.emojiButton.isEnabled()) {
            if (isPopupShowing()) {
                openKeyboardInternal();
            } else {
                boolean z = true;
                showPopup(1);
                EmojiView emojiView = this.emojiView;
                if (this.editText.length() <= 0) {
                    z = false;
                }
                emojiView.onOpen(z);
                this.editText.requestFocus();
            }
        }
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.emojiDidLoad) {
            EmojiView emojiView = this.emojiView;
            if (emojiView != null) {
                emojiView.invalidateViews();
            }
        }
    }

    public void setEnabled(boolean z) {
        this.editText.setEnabled(z);
        this.emojiButton.setVisibility(z ? 0 : 8);
        if (z) {
            this.editText.setPadding(LocaleController.isRTL ? AndroidUtilities.m26dp(40.0f) : 0, 0, LocaleController.isRTL ? 0 : AndroidUtilities.m26dp(40.0f), AndroidUtilities.m26dp(8.0f));
        } else {
            this.editText.setPadding(0, 0, 0, AndroidUtilities.m26dp(8.0f));
        }
    }

    public void setFocusable(boolean z) {
        this.editText.setFocusable(z);
    }

    public void hideEmojiView() {
        if (!this.emojiViewVisible) {
            EmojiView emojiView = this.emojiView;
            if (emojiView != null && emojiView.getVisibility() != 8) {
                this.emojiView.setVisibility(8);
            }
        }
    }

    public void setDelegate(EditTextEmojiDelegate editTextEmojiDelegate) {
        this.delegate = editTextEmojiDelegate;
    }

    public void onPause() {
        this.isPaused = true;
        closeKeyboard();
    }

    public void onResume() {
        this.isPaused = false;
        if (this.showKeyboardOnResume) {
            this.showKeyboardOnResume = false;
            this.editText.requestFocus();
            AndroidUtilities.showKeyboard(this.editText);
            if (!AndroidUtilities.usingHardwareInput && !this.keyboardVisible && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                this.waitingForKeyboardOpen = true;
                AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
                AndroidUtilities.runOnUIThread(this.openKeyboardRunnable, 100);
            }
        }
    }

    public void onDestroy() {
        this.destroyed = true;
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
        EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.onDestroy();
        }
        SizeNotifierFrameLayout sizeNotifierFrameLayout = this.sizeNotifierLayout;
        if (sizeNotifierFrameLayout != null) {
            sizeNotifierFrameLayout.setDelegate(null);
        }
    }

    public void setMaxLines(int i) {
        this.editText.setMaxLines(i);
    }

    public int length() {
        return this.editText.length();
    }

    public void setFilters(InputFilter[] inputFilterArr) {
        this.editText.setFilters(inputFilterArr);
    }

    public Editable getText() {
        return this.editText.getText();
    }

    public void setHint(CharSequence charSequence) {
        this.editText.setHint(charSequence);
    }

    public void setText(CharSequence charSequence) {
        this.editText.setText(charSequence);
    }

    public void setSelection(int i) {
        this.editText.setSelection(i);
    }

    public void hidePopup(boolean z) {
        if (isPopupShowing()) {
            showPopup(0);
        }
        if (z) {
            hideEmojiView();
        }
    }

    public void openKeyboard() {
        AndroidUtilities.showKeyboard(this.editText);
    }

    public void closeKeyboard() {
        AndroidUtilities.hideKeyboard(this.editText);
    }

    public boolean isPopupShowing() {
        return this.emojiViewVisible;
    }

    public boolean isKeyboardVisible() {
        return this.keyboardVisible;
    }

    private void openKeyboardInternal() {
        int i = (AndroidUtilities.usingHardwareInput || this.isPaused) ? 0 : 2;
        showPopup(i);
        this.editText.requestFocus();
        AndroidUtilities.showKeyboard(this.editText);
        if (this.isPaused) {
            this.showKeyboardOnResume = true;
        } else if (!AndroidUtilities.usingHardwareInput && !this.keyboardVisible && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
            this.waitingForKeyboardOpen = true;
            AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
            AndroidUtilities.runOnUIThread(this.openKeyboardRunnable, 100);
        }
    }

    private void showPopup(int i) {
        if (i == 1) {
            if (this.emojiView == null) {
                createEmojiView();
            }
            this.emojiView.setVisibility(0);
            this.emojiViewVisible = true;
            EmojiView emojiView = this.emojiView;
            if (this.keyboardHeight <= 0) {
                if (AndroidUtilities.isTablet()) {
                    this.keyboardHeight = AndroidUtilities.m26dp(150.0f);
                } else {
                    this.keyboardHeight = MessagesController.getGlobalEmojiSettings().getInt("kbd_height", AndroidUtilities.m26dp(200.0f));
                }
            }
            if (this.keyboardHeightLand <= 0) {
                if (AndroidUtilities.isTablet()) {
                    this.keyboardHeightLand = AndroidUtilities.m26dp(150.0f);
                } else {
                    this.keyboardHeightLand = MessagesController.getGlobalEmojiSettings().getInt("kbd_height_land3", AndroidUtilities.m26dp(200.0f));
                }
            }
            Point point = AndroidUtilities.displaySize;
            int i2 = point.x > point.y ? this.keyboardHeightLand : this.keyboardHeight;
            LayoutParams layoutParams = (LayoutParams) emojiView.getLayoutParams();
            layoutParams.height = i2;
            emojiView.setLayoutParams(layoutParams);
            if (!(AndroidUtilities.isInMultiwindow || AndroidUtilities.isTablet())) {
                AndroidUtilities.hideKeyboard(this.editText);
            }
            SizeNotifierFrameLayout sizeNotifierFrameLayout = this.sizeNotifierLayout;
            if (sizeNotifierFrameLayout != null) {
                this.emojiPadding = i2;
                sizeNotifierFrameLayout.requestLayout();
                this.emojiButton.setImageResource(C1067R.C1065drawable.input_keyboard);
                onWindowSizeChanged();
                return;
            }
            return;
        }
        ImageView imageView = this.emojiButton;
        if (imageView != null) {
            if (this.currentStyle == 0) {
                imageView.setImageResource(C1067R.C1065drawable.smiles_tab_smiles);
            } else {
                imageView.setImageResource(C1067R.C1065drawable.input_smile);
            }
        }
        if (this.emojiView != null) {
            this.emojiViewVisible = false;
            if (AndroidUtilities.usingHardwareInput || AndroidUtilities.isInMultiwindow) {
                this.emojiView.setVisibility(8);
            }
        }
        if (this.sizeNotifierLayout != null) {
            if (i == 0) {
                this.emojiPadding = 0;
            }
            this.sizeNotifierLayout.requestLayout();
            onWindowSizeChanged();
        }
    }

    private void onWindowSizeChanged() {
        int height = this.sizeNotifierLayout.getHeight();
        if (!this.keyboardVisible) {
            height -= this.emojiPadding;
        }
        EditTextEmojiDelegate editTextEmojiDelegate = this.delegate;
        if (editTextEmojiDelegate != null) {
            editTextEmojiDelegate.onWindowSizeChanged(height);
        }
    }

    private void createEmojiView() {
        if (this.emojiView == null) {
            this.emojiView = new EmojiView(false, false, getContext(), false, null);
            this.emojiView.setVisibility(8);
            if (AndroidUtilities.isTablet()) {
                this.emojiView.setForseMultiwindowLayout(true);
            }
            this.emojiView.setDelegate(new C40953());
            this.sizeNotifierLayout.addView(this.emojiView);
        }
    }

    public boolean isPopupView(View view) {
        return view == this.emojiView;
    }

    public int getEmojiPadding() {
        return this.emojiPadding;
    }

    public void onSizeChanged(int i, boolean z) {
        if (i > AndroidUtilities.m26dp(50.0f) && this.keyboardVisible && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
            if (z) {
                this.keyboardHeightLand = i;
                MessagesController.getGlobalEmojiSettings().edit().putInt("kbd_height_land3", this.keyboardHeightLand).commit();
            } else {
                this.keyboardHeight = i;
                MessagesController.getGlobalEmojiSettings().edit().putInt("kbd_height", this.keyboardHeight).commit();
            }
        }
        if (isPopupShowing()) {
            int i2 = z ? this.keyboardHeightLand : this.keyboardHeight;
            LayoutParams layoutParams = (LayoutParams) this.emojiView.getLayoutParams();
            if (!(layoutParams.width == AndroidUtilities.displaySize.x && layoutParams.height == i2)) {
                layoutParams.width = AndroidUtilities.displaySize.x;
                layoutParams.height = i2;
                this.emojiView.setLayoutParams(layoutParams);
                if (this.sizeNotifierLayout != null) {
                    this.emojiPadding = layoutParams.height;
                    this.sizeNotifierLayout.requestLayout();
                    onWindowSizeChanged();
                }
            }
        }
        if (this.lastSizeChangeValue1 == i && this.lastSizeChangeValue2 == z) {
            onWindowSizeChanged();
            return;
        }
        this.lastSizeChangeValue1 = i;
        this.lastSizeChangeValue2 = z;
        z = this.keyboardVisible;
        this.keyboardVisible = i > 0;
        if (this.keyboardVisible && isPopupShowing()) {
            showPopup(0);
        }
        if (this.emojiPadding != 0) {
            boolean z2 = this.keyboardVisible;
            if (!(z2 || z2 == z || isPopupShowing())) {
                this.emojiPadding = 0;
                this.sizeNotifierLayout.requestLayout();
            }
        }
        if (this.keyboardVisible && this.waitingForKeyboardOpen) {
            this.waitingForKeyboardOpen = false;
            AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
        }
        onWindowSizeChanged();
    }

    public EditTextBoldCursor getEditText() {
        return this.editText;
    }
}