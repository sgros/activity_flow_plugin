package org.telegram.p004ui.Components;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.EmojiView.EmojiViewDelegate;
import org.telegram.p004ui.Components.EmojiView.EmojiViewDelegate.C2837-CC;
import org.telegram.p004ui.Components.SizeNotifierFrameLayoutPhoto.SizeNotifierFrameLayoutPhotoDelegate;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.InputStickerSet;
import org.telegram.tgnet.TLRPC.StickerSet;
import org.telegram.tgnet.TLRPC.StickerSetCovered;

/* renamed from: org.telegram.ui.Components.PhotoViewerCaptionEnterView */
public class PhotoViewerCaptionEnterView extends FrameLayout implements NotificationCenterDelegate, SizeNotifierFrameLayoutPhotoDelegate {
    float animationProgress = 0.0f;
    private int audioInterfaceState;
    private int captionMaxLength = 1024;
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

    /* renamed from: org.telegram.ui.Components.PhotoViewerCaptionEnterView$2 */
    class C29192 implements Callback {
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return false;
        }

        C29192() {
        }

        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            PhotoViewerCaptionEnterView.this.currentActionMode = actionMode;
            return true;
        }

        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            if (VERSION.SDK_INT >= 23) {
                PhotoViewerCaptionEnterView.this.fixActionMode(actionMode);
            }
            return true;
        }

        public void onDestroyActionMode(ActionMode actionMode) {
            if (PhotoViewerCaptionEnterView.this.currentActionMode == actionMode) {
                PhotoViewerCaptionEnterView.this.currentActionMode = null;
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.PhotoViewerCaptionEnterView$3 */
    class C29203 implements Callback {
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return false;
        }

        C29203() {
        }

        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            PhotoViewerCaptionEnterView.this.currentActionMode = actionMode;
            return true;
        }

        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            if (VERSION.SDK_INT >= 23) {
                PhotoViewerCaptionEnterView.this.fixActionMode(actionMode);
            }
            return true;
        }

        public void onDestroyActionMode(ActionMode actionMode) {
            if (PhotoViewerCaptionEnterView.this.currentActionMode == actionMode) {
                PhotoViewerCaptionEnterView.this.currentActionMode = null;
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.PhotoViewerCaptionEnterView$4 */
    class C29214 implements TextWatcher {
        boolean processChange = false;

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        C29214() {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!PhotoViewerCaptionEnterView.this.innerTextChange) {
                if (PhotoViewerCaptionEnterView.this.delegate != null) {
                    PhotoViewerCaptionEnterView.this.delegate.onTextChanged(charSequence);
                }
                if (i2 != i3 && i3 - i2 > 1) {
                    this.processChange = true;
                }
            }
        }

        public void afterTextChanged(Editable editable) {
            if (PhotoViewerCaptionEnterView.this.captionMaxLength - PhotoViewerCaptionEnterView.this.messageEditText.length() <= 128) {
                PhotoViewerCaptionEnterView.this.lengthText = String.format("%d", new Object[]{Integer.valueOf(PhotoViewerCaptionEnterView.this.captionMaxLength - PhotoViewerCaptionEnterView.this.messageEditText.length())});
            } else {
                PhotoViewerCaptionEnterView.this.lengthText = null;
            }
            PhotoViewerCaptionEnterView.this.invalidate();
            if (!PhotoViewerCaptionEnterView.this.innerTextChange && this.processChange) {
                ImageSpan[] imageSpanArr = (ImageSpan[]) editable.getSpans(0, editable.length(), ImageSpan.class);
                for (Object removeSpan : imageSpanArr) {
                    editable.removeSpan(removeSpan);
                }
                Emoji.replaceEmoji(editable, PhotoViewerCaptionEnterView.this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.m26dp(20.0f), false);
                this.processChange = false;
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.PhotoViewerCaptionEnterView$PhotoViewerCaptionEnterViewDelegate */
    public interface PhotoViewerCaptionEnterViewDelegate {
        void onCaptionEnter();

        void onTextChanged(CharSequence charSequence);

        void onWindowSizeChanged(int i);
    }

    /* renamed from: org.telegram.ui.Components.PhotoViewerCaptionEnterView$5 */
    class C41295 implements EmojiViewDelegate {
        public /* synthetic */ boolean isExpanded() {
            return C2837-CC.$default$isExpanded(this);
        }

        public /* synthetic */ boolean isSearchOpened() {
            return C2837-CC.$default$isSearchOpened(this);
        }

        public /* synthetic */ void onClearEmojiRecent() {
            C2837-CC.$default$onClearEmojiRecent(this);
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

        C41295() {
        }

        public boolean onBackspace() {
            if (PhotoViewerCaptionEnterView.this.messageEditText.length() == 0) {
                return false;
            }
            PhotoViewerCaptionEnterView.this.messageEditText.dispatchKeyEvent(new KeyEvent(0, 67));
            return true;
        }

        public void onEmojiSelected(String str) {
            if (PhotoViewerCaptionEnterView.this.messageEditText.length() + str.length() <= PhotoViewerCaptionEnterView.this.captionMaxLength) {
                int selectionEnd = PhotoViewerCaptionEnterView.this.messageEditText.getSelectionEnd();
                if (selectionEnd < 0) {
                    selectionEnd = 0;
                }
                try {
                    PhotoViewerCaptionEnterView.this.innerTextChange = true;
                    CharSequence replaceEmoji = Emoji.replaceEmoji(str, PhotoViewerCaptionEnterView.this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.m26dp(20.0f), false);
                    PhotoViewerCaptionEnterView.this.messageEditText.setText(PhotoViewerCaptionEnterView.this.messageEditText.getText().insert(selectionEnd, replaceEmoji));
                    selectionEnd += replaceEmoji.length();
                    PhotoViewerCaptionEnterView.this.messageEditText.setSelection(selectionEnd, selectionEnd);
                } catch (Exception e) {
                    FileLog.m30e(e);
                } catch (Throwable th) {
                    PhotoViewerCaptionEnterView.this.innerTextChange = false;
                }
                PhotoViewerCaptionEnterView.this.innerTextChange = false;
            }
        }
    }

    public PhotoViewerCaptionEnterView(Context context, SizeNotifierFrameLayoutPhoto sizeNotifierFrameLayoutPhoto, View view) {
        Context context2 = context;
        super(context);
        setWillNotDraw(false);
        setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.windowView = view;
        this.sizeNotifierLayout = sizeNotifierFrameLayoutPhoto;
        LinearLayout linearLayout = new LinearLayout(context2);
        linearLayout.setOrientation(0);
        addView(linearLayout, LayoutHelper.createFrame(-1, -2.0f, 51, 2.0f, 0.0f, 0.0f, 0.0f));
        FrameLayout frameLayout = new FrameLayout(context2);
        linearLayout.addView(frameLayout, LayoutHelper.createLinear(0, -2, 1.0f));
        this.emojiButton = new ImageView(context2);
        this.emojiButton.setImageResource(C1067R.C1065drawable.input_smile);
        this.emojiButton.setScaleType(ScaleType.CENTER_INSIDE);
        this.emojiButton.setPadding(AndroidUtilities.m26dp(4.0f), AndroidUtilities.m26dp(1.0f), 0, 0);
        frameLayout.addView(this.emojiButton, LayoutHelper.createFrame(48, 48, 83));
        this.emojiButton.setOnClickListener(new C2648xcf019e03(this));
        this.emojiButton.setContentDescription(LocaleController.getString("Emoji", C1067R.string.Emoji));
        this.lengthTextPaint = new TextPaint(1);
        this.lengthTextPaint.setTextSize((float) AndroidUtilities.m26dp(13.0f));
        this.lengthTextPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.lengthTextPaint.setColor(-2500135);
        this.messageEditText = new EditTextCaption(context2) {
            /* Access modifiers changed, original: protected */
            public void onMeasure(int i, int i2) {
                try {
                    super.onMeasure(i, i2);
                } catch (Exception e) {
                    setMeasuredDimension(MeasureSpec.getSize(i), AndroidUtilities.m26dp(51.0f));
                    FileLog.m30e(e);
                }
            }

            /* Access modifiers changed, original: protected */
            public void onSelectionChanged(int i, int i2) {
                super.onSelectionChanged(i, i2);
                if (i != i2) {
                    fixHandleView(false);
                } else {
                    fixHandleView(true);
                }
            }
        };
        if (VERSION.SDK_INT >= 23 && this.windowView != null) {
            this.messageEditText.setCustomSelectionActionModeCallback(new C29192());
            this.messageEditText.setCustomInsertionActionModeCallback(new C29203());
        }
        this.messageEditText.setHint(LocaleController.getString("AddCaption", C1067R.string.AddCaption));
        this.messageEditText.setImeOptions(268435456);
        EditTextCaption editTextCaption = this.messageEditText;
        editTextCaption.setInputType(editTextCaption.getInputType() | 16384);
        this.messageEditText.setMaxLines(4);
        this.messageEditText.setHorizontallyScrolling(false);
        this.messageEditText.setTextSize(1, 18.0f);
        this.messageEditText.setGravity(80);
        this.messageEditText.setPadding(0, AndroidUtilities.m26dp(11.0f), 0, AndroidUtilities.m26dp(12.0f));
        this.messageEditText.setBackgroundDrawable(null);
        this.messageEditText.setCursorColor(-1);
        this.messageEditText.setCursorSize(AndroidUtilities.m26dp(20.0f));
        this.messageEditText.setTextColor(-1);
        this.messageEditText.setHintTextColor(-1291845633);
        this.messageEditText.setFilters(new InputFilter[]{new LengthFilter(this.captionMaxLength)});
        frameLayout.addView(this.messageEditText, LayoutHelper.createFrame(-1, -2.0f, 83, 52.0f, 0.0f, 6.0f, 0.0f));
        this.messageEditText.setOnKeyListener(new C2644x11a1d047(this));
        this.messageEditText.setOnClickListener(new C2646xcd687099(this));
        this.messageEditText.addTextChangedListener(new C29214());
        CombinedDrawable combinedDrawable = new CombinedDrawable(Theme.createCircleDrawable(AndroidUtilities.m26dp(16.0f), -10043398), context.getResources().getDrawable(C1067R.C1065drawable.input_done).mutate(), 0, AndroidUtilities.m26dp(1.0f));
        combinedDrawable.setCustomSize(AndroidUtilities.m26dp(32.0f), AndroidUtilities.m26dp(32.0f));
        ImageView imageView = new ImageView(context2);
        imageView.setScaleType(ScaleType.CENTER);
        imageView.setImageDrawable(combinedDrawable);
        linearLayout.addView(imageView, LayoutHelper.createLinear(48, 48, 80));
        imageView.setOnClickListener(new C2647x7a682708(this));
        imageView.setContentDescription(LocaleController.getString("Done", C1067R.string.Done));
    }

    public /* synthetic */ void lambda$new$0$PhotoViewerCaptionEnterView(View view) {
        if (isPopupShowing()) {
            openKeyboardInternal();
        } else {
            showPopup(1);
        }
    }

    public /* synthetic */ boolean lambda$new$1$PhotoViewerCaptionEnterView(View view, int i, KeyEvent keyEvent) {
        if (i == 4) {
            if (this.windowView != null && hideActionMode()) {
                return true;
            }
            if (!this.keyboardVisible && isPopupShowing()) {
                if (keyEvent.getAction() == 1) {
                    showPopup(0);
                }
                return true;
            }
        }
        return false;
    }

    public /* synthetic */ void lambda$new$2$PhotoViewerCaptionEnterView(View view) {
        if (isPopupShowing()) {
            showPopup(AndroidUtilities.usingHardwareInput ? 0 : 2);
        }
    }

    public /* synthetic */ void lambda$new$3$PhotoViewerCaptionEnterView(View view) {
        this.delegate.onCaptionEnter();
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        if (this.lengthText == null || getMeasuredHeight() <= AndroidUtilities.m26dp(48.0f)) {
            this.lengthTextPaint.setAlpha(0);
            this.animationProgress = 0.0f;
            return;
        }
        canvas.drawText(this.lengthText, (float) ((AndroidUtilities.m26dp(56.0f) - ((int) Math.ceil((double) this.lengthTextPaint.measureText(this.lengthText)))) / 2), (float) (getMeasuredHeight() - AndroidUtilities.m26dp(48.0f)), this.lengthTextPaint);
        float f = this.animationProgress;
        if (f < 1.0f) {
            this.animationProgress = f + 0.14166667f;
            invalidate();
            if (this.animationProgress >= 1.0f) {
                this.animationProgress = 1.0f;
            }
            this.lengthTextPaint.setAlpha((int) (this.animationProgress * 255.0f));
        }
    }

    public void setForceFloatingEmoji(boolean z) {
        this.forceFloatingEmoji = z;
    }

    public boolean hideActionMode() {
        if (VERSION.SDK_INT >= 23) {
            ActionMode actionMode = this.currentActionMode;
            if (actionMode != null) {
                try {
                    actionMode.finish();
                } catch (Exception e) {
                    FileLog.m30e(e);
                }
                this.currentActionMode = null;
                return true;
            }
        }
        return false;
    }

    @SuppressLint({"PrivateApi"})
    private void fixActionMode(ActionMode actionMode) {
        try {
            Class cls = Class.forName("com.android.internal.view.FloatingActionMode");
            Field declaredField = cls.getDeclaredField("mFloatingToolbar");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(actionMode);
            Class cls2 = Class.forName("com.android.internal.widget.FloatingToolbar");
            Field declaredField2 = cls2.getDeclaredField("mPopup");
            Field declaredField3 = cls2.getDeclaredField("mWidthChanged");
            declaredField2.setAccessible(true);
            declaredField3.setAccessible(true);
            obj = declaredField2.get(obj);
            declaredField3 = Class.forName("com.android.internal.widget.FloatingToolbar$FloatingToolbarPopup").getDeclaredField("mParent");
            declaredField3.setAccessible(true);
            if (((View) declaredField3.get(obj)) != this.windowView) {
                declaredField3.set(obj, this.windowView);
                Method declaredMethod = cls.getDeclaredMethod("updateViewLocationInWindow", new Class[0]);
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(actionMode, new Object[0]);
            }
        } catch (Throwable th) {
            FileLog.m30e(th);
        }
    }

    private void onWindowSizeChanged() {
        int height = this.sizeNotifierLayout.getHeight();
        if (!this.keyboardVisible) {
            height -= this.emojiPadding;
        }
        PhotoViewerCaptionEnterViewDelegate photoViewerCaptionEnterViewDelegate = this.delegate;
        if (photoViewerCaptionEnterViewDelegate != null) {
            photoViewerCaptionEnterViewDelegate.onWindowSizeChanged(height);
        }
    }

    public void onCreate() {
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
        this.sizeNotifierLayout.setDelegate(this);
    }

    public void onDestroy() {
        hidePopup();
        if (isKeyboardVisible()) {
            closeKeyboard();
        }
        this.keyboardVisible = false;
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
        SizeNotifierFrameLayoutPhoto sizeNotifierFrameLayoutPhoto = this.sizeNotifierLayout;
        if (sizeNotifierFrameLayoutPhoto != null) {
            sizeNotifierFrameLayoutPhoto.setDelegate(null);
        }
    }

    public void setDelegate(PhotoViewerCaptionEnterViewDelegate photoViewerCaptionEnterViewDelegate) {
        this.delegate = photoViewerCaptionEnterViewDelegate;
    }

    public void setFieldText(CharSequence charSequence) {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption != null) {
            editTextCaption.setText(charSequence);
            EditTextCaption editTextCaption2 = this.messageEditText;
            editTextCaption2.setSelection(editTextCaption2.getText().length());
            PhotoViewerCaptionEnterViewDelegate photoViewerCaptionEnterViewDelegate = this.delegate;
            if (photoViewerCaptionEnterViewDelegate != null) {
                photoViewerCaptionEnterViewDelegate.onTextChanged(this.messageEditText.getText());
            }
            int i = this.captionMaxLength;
            this.captionMaxLength = MessagesController.getInstance(UserConfig.selectedAccount).maxCaptionLength;
            if (i != this.captionMaxLength) {
                this.messageEditText.setFilters(new InputFilter[]{new LengthFilter(this.captionMaxLength)});
            }
        }
    }

    public int getSelectionLength() {
        EditTextCaption editTextCaption = this.messageEditText;
        int i = 0;
        if (editTextCaption == null) {
            return 0;
        }
        try {
            int selectionEnd = editTextCaption.getSelectionEnd();
            i = this.messageEditText.getSelectionStart();
            return selectionEnd - i;
        } catch (Exception e) {
            FileLog.m30e(e);
            return i;
        }
    }

    public int getCursorPosition() {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null) {
            return 0;
        }
        return editTextCaption.getSelectionStart();
    }

    private void createEmojiView() {
        if (this.emojiView == null) {
            this.emojiView = new EmojiView(false, false, getContext(), false, null);
            this.emojiView.setDelegate(new C41295());
            this.sizeNotifierLayout.addView(this.emojiView);
        }
    }

    public void addEmojiToRecent(String str) {
        createEmojiView();
        this.emojiView.addEmojiToRecent(str);
    }

    public void replaceWithText(int i, int i2, CharSequence charSequence, boolean z) {
        try {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.messageEditText.getText());
            spannableStringBuilder.replace(i, i2 + i, charSequence);
            if (z) {
                Emoji.replaceEmoji(spannableStringBuilder, this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.m26dp(20.0f), false);
            }
            this.messageEditText.setText(spannableStringBuilder);
            if (charSequence.length() + i <= this.messageEditText.length()) {
                this.messageEditText.setSelection(i + charSequence.length());
            } else {
                this.messageEditText.setSelection(this.messageEditText.length());
            }
        } catch (Exception e) {
            FileLog.m30e(e);
        }
    }

    public void setFieldFocused(boolean z) {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption != null) {
            if (z) {
                if (!editTextCaption.isFocused()) {
                    this.messageEditText.postDelayed(new C2645x8c914992(this), 600);
                }
            } else if (editTextCaption.isFocused() && !this.keyboardVisible) {
                this.messageEditText.clearFocus();
            }
        }
    }

    public /* synthetic */ void lambda$setFieldFocused$4$PhotoViewerCaptionEnterView() {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption != null) {
            try {
                editTextCaption.requestFocus();
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }
    }

    public CharSequence getFieldCharSequence() {
        return AndroidUtilities.getTrimmedString(this.messageEditText.getText());
    }

    public int getEmojiPadding() {
        return this.emojiPadding;
    }

    public boolean isPopupView(View view) {
        return view == this.emojiView;
    }

    private void showPopup(int i) {
        if (i == 1) {
            if (this.emojiView == null) {
                createEmojiView();
            }
            this.emojiView.setVisibility(0);
            if (this.keyboardHeight <= 0) {
                this.keyboardHeight = MessagesController.getGlobalEmojiSettings().getInt("kbd_height", AndroidUtilities.m26dp(200.0f));
            }
            if (this.keyboardHeightLand <= 0) {
                this.keyboardHeightLand = MessagesController.getGlobalEmojiSettings().getInt("kbd_height_land3", AndroidUtilities.m26dp(200.0f));
            }
            Point point = AndroidUtilities.displaySize;
            i = point.x > point.y ? this.keyboardHeightLand : this.keyboardHeight;
            LayoutParams layoutParams = (LayoutParams) this.emojiView.getLayoutParams();
            layoutParams.width = AndroidUtilities.displaySize.x;
            layoutParams.height = i;
            this.emojiView.setLayoutParams(layoutParams);
            if (!(AndroidUtilities.isInMultiwindow || this.forceFloatingEmoji)) {
                AndroidUtilities.hideKeyboard(this.messageEditText);
            }
            SizeNotifierFrameLayoutPhoto sizeNotifierFrameLayoutPhoto = this.sizeNotifierLayout;
            if (sizeNotifierFrameLayoutPhoto != null) {
                this.emojiPadding = i;
                sizeNotifierFrameLayoutPhoto.requestLayout();
                this.emojiButton.setImageResource(C1067R.C1065drawable.input_keyboard);
                onWindowSizeChanged();
                return;
            }
            return;
        }
        ImageView imageView = this.emojiButton;
        if (imageView != null) {
            imageView.setImageResource(C1067R.C1065drawable.input_smile);
        }
        EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.setVisibility(8);
        }
        if (this.sizeNotifierLayout != null) {
            if (i == 0) {
                this.emojiPadding = 0;
            }
            this.sizeNotifierLayout.requestLayout();
            onWindowSizeChanged();
        }
    }

    public void hidePopup() {
        if (isPopupShowing()) {
            showPopup(0);
        }
    }

    private void openKeyboardInternal() {
        showPopup(AndroidUtilities.usingHardwareInput ? 0 : 2);
        openKeyboard();
    }

    public void openKeyboard() {
        int selectionStart;
        try {
            selectionStart = this.messageEditText.getSelectionStart();
        } catch (Exception e) {
            int length = this.messageEditText.length();
            FileLog.m30e(e);
            selectionStart = length;
        }
        MotionEvent obtain = MotionEvent.obtain(0, 0, 0, 0.0f, 0.0f, 0);
        this.messageEditText.onTouchEvent(obtain);
        obtain.recycle();
        obtain = MotionEvent.obtain(0, 0, 1, 0.0f, 0.0f, 0);
        this.messageEditText.onTouchEvent(obtain);
        obtain.recycle();
        AndroidUtilities.showKeyboard(this.messageEditText);
        try {
            this.messageEditText.setSelection(selectionStart);
        } catch (Exception e2) {
            FileLog.m30e(e2);
        }
    }

    public boolean isPopupShowing() {
        EmojiView emojiView = this.emojiView;
        return emojiView != null && emojiView.getVisibility() == 0;
    }

    public void closeKeyboard() {
        AndroidUtilities.hideKeyboard(this.messageEditText);
    }

    public boolean isKeyboardVisible() {
        return (AndroidUtilities.usingHardwareInput && getTag() != null) || this.keyboardVisible;
    }

    public void onSizeChanged(int i, boolean z) {
        if (i > AndroidUtilities.m26dp(50.0f) && this.keyboardVisible && !AndroidUtilities.isInMultiwindow && !this.forceFloatingEmoji) {
            if (z) {
                this.keyboardHeightLand = i;
                MessagesController.getGlobalEmojiSettings().edit().putInt("kbd_height_land3", this.keyboardHeightLand).commit();
            } else {
                this.keyboardHeight = i;
                MessagesController.getGlobalEmojiSettings().edit().putInt("kbd_height", this.keyboardHeight).commit();
            }
        }
        if (isPopupShowing()) {
            int i2;
            if (z) {
                i2 = this.keyboardHeightLand;
            } else {
                i2 = this.keyboardHeight;
            }
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
        onWindowSizeChanged();
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.emojiDidLoad) {
            EmojiView emojiView = this.emojiView;
            if (emojiView != null) {
                emojiView.invalidateViews();
            }
        }
    }
}
