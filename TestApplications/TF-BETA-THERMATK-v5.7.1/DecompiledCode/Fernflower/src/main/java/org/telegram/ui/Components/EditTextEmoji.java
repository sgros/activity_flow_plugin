package org.telegram.ui.Components;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;

public class EditTextEmoji extends FrameLayout implements NotificationCenter.NotificationCenterDelegate, SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate {
   public static final int STYLE_DIALOG = 1;
   public static final int STYLE_FRAGMENT = 0;
   private int currentStyle;
   private EditTextEmoji.EditTextEmojiDelegate delegate;
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
   private Runnable openKeyboardRunnable = new Runnable() {
      public void run() {
         if (!EditTextEmoji.this.destroyed && EditTextEmoji.this.editText != null && EditTextEmoji.this.waitingForKeyboardOpen && !EditTextEmoji.this.keyboardVisible && !AndroidUtilities.usingHardwareInput && !AndroidUtilities.isInMultiwindow && AndroidUtilities.isTablet()) {
            EditTextEmoji.this.editText.requestFocus();
            AndroidUtilities.showKeyboard(EditTextEmoji.this.editText);
            AndroidUtilities.cancelRunOnUIThread(EditTextEmoji.this.openKeyboardRunnable);
            AndroidUtilities.runOnUIThread(EditTextEmoji.this.openKeyboardRunnable, 100L);
         }

      }
   };
   private BaseFragment parentFragment;
   private boolean showKeyboardOnResume;
   private SizeNotifierFrameLayout sizeNotifierLayout;
   private boolean waitingForKeyboardOpen;

   public EditTextEmoji(Context var1, SizeNotifierFrameLayout var2, BaseFragment var3, int var4) {
      super(var1);
      this.currentStyle = var4;
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
      this.parentFragment = var3;
      this.sizeNotifierLayout = var2;
      this.sizeNotifierLayout.setDelegate(this);
      this.editText = new EditTextBoldCursor(var1) {
         public boolean onTouchEvent(MotionEvent var1) {
            if (EditTextEmoji.this.isPopupShowing() && var1.getAction() == 0) {
               EditTextEmoji var2 = EditTextEmoji.this;
               byte var3;
               if (AndroidUtilities.usingHardwareInput) {
                  var3 = 0;
               } else {
                  var3 = 2;
               }

               var2.showPopup(var3);
               EditTextEmoji.this.openKeyboardInternal();
            }

            if (var1.getAction() == 0 && !AndroidUtilities.showKeyboard(this)) {
               this.clearFocus();
               this.requestFocus();
            }

            try {
               boolean var4 = super.onTouchEvent(var1);
               return var4;
            } catch (Exception var5) {
               FileLog.e((Throwable)var5);
               return false;
            }
         }
      };
      this.editText.setTextSize(1, 16.0F);
      this.editText.setImeOptions(268435456);
      this.editText.setInputType(16385);
      EditTextBoldCursor var12 = this.editText;
      var12.setFocusable(var12.isEnabled());
      this.editText.setCursorSize(AndroidUtilities.dp(20.0F));
      this.editText.setCursorWidth(1.5F);
      byte var5 = 5;
      if (var4 == 0) {
         var12 = this.editText;
         byte var6;
         if (LocaleController.isRTL) {
            var6 = 5;
         } else {
            var6 = 3;
         }

         var12.setGravity(var6 | 16);
         this.editText.setBackgroundDrawable(Theme.createEditTextDrawable(var1, false));
         this.editText.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
         this.editText.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.editText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         var12 = this.editText;
         int var14;
         if (LocaleController.isRTL) {
            var14 = AndroidUtilities.dp(40.0F);
         } else {
            var14 = 0;
         }

         int var7;
         if (LocaleController.isRTL) {
            var7 = 0;
         } else {
            var7 = AndroidUtilities.dp(40.0F);
         }

         var12.setPadding(var14, 0, var7, AndroidUtilities.dp(8.0F));
         var12 = this.editText;
         boolean var8 = LocaleController.isRTL;
         float var9 = 0.0F;
         float var10;
         if (var8) {
            var10 = 11.0F;
         } else {
            var10 = 0.0F;
         }

         if (!LocaleController.isRTL) {
            var9 = 11.0F;
         }

         this.addView(var12, LayoutHelper.createFrame(-1, -2.0F, 19, var10, 1.0F, var9, 0.0F));
      } else {
         this.editText.setGravity(19);
         this.editText.setHintTextColor(Theme.getColor("dialogTextHint"));
         this.editText.setTextColor(Theme.getColor("dialogTextBlack"));
         this.editText.setBackgroundDrawable((Drawable)null);
         this.editText.setPadding(0, 0, 0, 0);
         this.addView(this.editText, LayoutHelper.createFrame(-1, -1.0F, 19, 48.0F, 0.0F, 0.0F, 0.0F));
      }

      this.emojiButton = new ImageView(var1);
      this.emojiButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_messagePanelIcons"), Mode.MULTIPLY));
      this.emojiButton.setScaleType(ScaleType.CENTER_INSIDE);
      if (var4 == 0) {
         this.emojiButton.setPadding(0, 0, 0, AndroidUtilities.dp(7.0F));
         this.emojiButton.setImageResource(2131165852);
         ImageView var11 = this.emojiButton;
         byte var13 = var5;
         if (LocaleController.isRTL) {
            var13 = 3;
         }

         this.addView(var11, LayoutHelper.createFrame(48, 48.0F, var13 | 16, 0.0F, 0.0F, 0.0F, 0.0F));
      } else {
         this.emojiButton.setImageResource(2131165492);
         this.addView(this.emojiButton, LayoutHelper.createFrame(48, 48.0F, 19, 0.0F, 0.0F, 0.0F, 0.0F));
      }

      this.emojiButton.setOnClickListener(new _$$Lambda$EditTextEmoji$2rCQ8jv3el2lKWMAASuH1xjI9xg(this));
      this.emojiButton.setContentDescription(LocaleController.getString("Emoji", 2131559331));
   }

   private void createEmojiView() {
      if (this.emojiView == null) {
         this.emojiView = new EmojiView(false, false, this.getContext(), false, (TLRPC.ChatFull)null);
         this.emojiView.setVisibility(8);
         if (AndroidUtilities.isTablet()) {
            this.emojiView.setForseMultiwindowLayout(true);
         }

         this.emojiView.setDelegate(new EmojiView.EmojiViewDelegate() {
            // $FF: synthetic method
            public boolean isExpanded() {
               return EmojiView$EmojiViewDelegate$_CC.$default$isExpanded(this);
            }

            // $FF: synthetic method
            public boolean isSearchOpened() {
               return EmojiView$EmojiViewDelegate$_CC.$default$isSearchOpened(this);
            }

            // $FF: synthetic method
            public void lambda$onClearEmojiRecent$0$EditTextEmoji$3(DialogInterface var1, int var2) {
               EditTextEmoji.this.emojiView.clearRecentEmoji();
            }

            public boolean onBackspace() {
               if (EditTextEmoji.this.editText.length() == 0) {
                  return false;
               } else {
                  EditTextEmoji.this.editText.dispatchKeyEvent(new KeyEvent(0, 67));
                  return true;
               }
            }

            public void onClearEmojiRecent() {
               AlertDialog.Builder var1 = new AlertDialog.Builder(EditTextEmoji.this.getContext());
               var1.setTitle(LocaleController.getString("AppName", 2131558635));
               var1.setMessage(LocaleController.getString("ClearRecentEmoji", 2131559113));
               var1.setPositiveButton(LocaleController.getString("ClearButton", 2131559102).toUpperCase(), new _$$Lambda$EditTextEmoji$3$WhQ1USJV_3duKh9fhejflvEi8U0(this));
               var1.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
               if (EditTextEmoji.this.parentFragment != null) {
                  EditTextEmoji.this.parentFragment.showDialog(var1.create());
               } else {
                  var1.show();
               }

            }

            public void onEmojiSelected(String var1) {
               int var2 = EditTextEmoji.this.editText.getSelectionEnd();
               int var3 = var2;
               if (var2 < 0) {
                  var3 = 0;
               }

               try {
                  EditTextEmoji.this.innerTextChange = 2;
                  CharSequence var8 = Emoji.replaceEmoji(var1, EditTextEmoji.this.editText.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0F), false);
                  EditTextEmoji.this.editText.setText(EditTextEmoji.this.editText.getText().insert(var3, var8));
                  var3 += var8.length();
                  EditTextEmoji.this.editText.setSelection(var3, var3);
               } catch (Exception var6) {
                  FileLog.e((Throwable)var6);
               } finally {
                  EditTextEmoji.this.innerTextChange = 0;
               }

            }

            // $FF: synthetic method
            public void onGifSelected(Object var1, Object var2) {
               EmojiView$EmojiViewDelegate$_CC.$default$onGifSelected(this, var1, var2);
            }

            // $FF: synthetic method
            public void onSearchOpenClose(int var1) {
               EmojiView$EmojiViewDelegate$_CC.$default$onSearchOpenClose(this, var1);
            }

            // $FF: synthetic method
            public void onShowStickerSet(TLRPC.StickerSet var1, TLRPC.InputStickerSet var2) {
               EmojiView$EmojiViewDelegate$_CC.$default$onShowStickerSet(this, var1, var2);
            }

            // $FF: synthetic method
            public void onStickerSelected(TLRPC.Document var1, Object var2) {
               EmojiView$EmojiViewDelegate$_CC.$default$onStickerSelected(this, var1, var2);
            }

            // $FF: synthetic method
            public void onStickerSetAdd(TLRPC.StickerSetCovered var1) {
               EmojiView$EmojiViewDelegate$_CC.$default$onStickerSetAdd(this, var1);
            }

            // $FF: synthetic method
            public void onStickerSetRemove(TLRPC.StickerSetCovered var1) {
               EmojiView$EmojiViewDelegate$_CC.$default$onStickerSetRemove(this, var1);
            }

            // $FF: synthetic method
            public void onStickersGroupClick(int var1) {
               EmojiView$EmojiViewDelegate$_CC.$default$onStickersGroupClick(this, var1);
            }

            // $FF: synthetic method
            public void onStickersSettingsClick() {
               EmojiView$EmojiViewDelegate$_CC.$default$onStickersSettingsClick(this);
            }

            // $FF: synthetic method
            public void onTabOpened(int var1) {
               EmojiView$EmojiViewDelegate$_CC.$default$onTabOpened(this, var1);
            }
         });
         this.sizeNotifierLayout.addView(this.emojiView);
      }
   }

   private void onWindowSizeChanged() {
      int var1 = this.sizeNotifierLayout.getHeight();
      int var2 = var1;
      if (!this.keyboardVisible) {
         var2 = var1 - this.emojiPadding;
      }

      EditTextEmoji.EditTextEmojiDelegate var3 = this.delegate;
      if (var3 != null) {
         var3.onWindowSizeChanged(var2);
      }

   }

   private void openKeyboardInternal() {
      byte var1;
      if (!AndroidUtilities.usingHardwareInput && !this.isPaused) {
         var1 = 2;
      } else {
         var1 = 0;
      }

      this.showPopup(var1);
      this.editText.requestFocus();
      AndroidUtilities.showKeyboard(this.editText);
      if (this.isPaused) {
         this.showKeyboardOnResume = true;
      } else if (!AndroidUtilities.usingHardwareInput && !this.keyboardVisible && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
         this.waitingForKeyboardOpen = true;
         AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
         AndroidUtilities.runOnUIThread(this.openKeyboardRunnable, 100L);
      }

   }

   private void showPopup(int var1) {
      if (var1 == 1) {
         if (this.emojiView == null) {
            this.createEmojiView();
         }

         this.emojiView.setVisibility(0);
         this.emojiViewVisible = true;
         EmojiView var2 = this.emojiView;
         if (this.keyboardHeight <= 0) {
            if (AndroidUtilities.isTablet()) {
               this.keyboardHeight = AndroidUtilities.dp(150.0F);
            } else {
               this.keyboardHeight = MessagesController.getGlobalEmojiSettings().getInt("kbd_height", AndroidUtilities.dp(200.0F));
            }
         }

         if (this.keyboardHeightLand <= 0) {
            if (AndroidUtilities.isTablet()) {
               this.keyboardHeightLand = AndroidUtilities.dp(150.0F);
            } else {
               this.keyboardHeightLand = MessagesController.getGlobalEmojiSettings().getInt("kbd_height_land3", AndroidUtilities.dp(200.0F));
            }
         }

         android.graphics.Point var3 = AndroidUtilities.displaySize;
         if (var3.x > var3.y) {
            var1 = this.keyboardHeightLand;
         } else {
            var1 = this.keyboardHeight;
         }

         LayoutParams var6 = (LayoutParams)var2.getLayoutParams();
         var6.height = var1;
         var2.setLayoutParams(var6);
         if (!AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
            AndroidUtilities.hideKeyboard(this.editText);
         }

         SizeNotifierFrameLayout var4 = this.sizeNotifierLayout;
         if (var4 != null) {
            this.emojiPadding = var1;
            var4.requestLayout();
            this.emojiButton.setImageResource(2131165487);
            this.onWindowSizeChanged();
         }
      } else {
         ImageView var5 = this.emojiButton;
         if (var5 != null) {
            if (this.currentStyle == 0) {
               var5.setImageResource(2131165852);
            } else {
               var5.setImageResource(2131165492);
            }
         }

         if (this.emojiView != null) {
            this.emojiViewVisible = false;
            if (AndroidUtilities.usingHardwareInput || AndroidUtilities.isInMultiwindow) {
               this.emojiView.setVisibility(8);
            }
         }

         if (this.sizeNotifierLayout != null) {
            if (var1 == 0) {
               this.emojiPadding = 0;
            }

            this.sizeNotifierLayout.requestLayout();
            this.onWindowSizeChanged();
         }
      }

   }

   public void closeKeyboard() {
      AndroidUtilities.hideKeyboard(this.editText);
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.emojiDidLoad) {
         EmojiView var4 = this.emojiView;
         if (var4 != null) {
            var4.invalidateViews();
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
         EmojiView var1 = this.emojiView;
         if (var1 != null && var1.getVisibility() != 8) {
            this.emojiView.setVisibility(8);
         }
      }

   }

   public void hidePopup(boolean var1) {
      if (this.isPopupShowing()) {
         this.showPopup(0);
      }

      if (var1) {
         this.hideEmojiView();
      }

   }

   public boolean isKeyboardVisible() {
      return this.keyboardVisible;
   }

   public boolean isPopupShowing() {
      return this.emojiViewVisible;
   }

   public boolean isPopupView(View var1) {
      boolean var2;
      if (var1 == this.emojiView) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   // $FF: synthetic method
   public void lambda$new$0$EditTextEmoji(View var1) {
      if (this.emojiButton.isEnabled()) {
         if (!this.isPopupShowing()) {
            boolean var2 = true;
            this.showPopup(1);
            EmojiView var3 = this.emojiView;
            if (this.editText.length() <= 0) {
               var2 = false;
            }

            var3.onOpen(var2);
            this.editText.requestFocus();
         } else {
            this.openKeyboardInternal();
         }

      }
   }

   public int length() {
      return this.editText.length();
   }

   public void onDestroy() {
      this.destroyed = true;
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
      EmojiView var1 = this.emojiView;
      if (var1 != null) {
         var1.onDestroy();
      }

      SizeNotifierFrameLayout var2 = this.sizeNotifierLayout;
      if (var2 != null) {
         var2.setDelegate((SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate)null);
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
         AndroidUtilities.showKeyboard(this.editText);
         if (!AndroidUtilities.usingHardwareInput && !this.keyboardVisible && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
            this.waitingForKeyboardOpen = true;
            AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
            AndroidUtilities.runOnUIThread(this.openKeyboardRunnable, 100L);
         }
      }

   }

   public void onSizeChanged(int var1, boolean var2) {
      if (var1 > AndroidUtilities.dp(50.0F) && this.keyboardVisible && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
         if (var2) {
            this.keyboardHeightLand = var1;
            MessagesController.getGlobalEmojiSettings().edit().putInt("kbd_height_land3", this.keyboardHeightLand).commit();
         } else {
            this.keyboardHeight = var1;
            MessagesController.getGlobalEmojiSettings().edit().putInt("kbd_height", this.keyboardHeight).commit();
         }
      }

      if (this.isPopupShowing()) {
         int var3;
         if (var2) {
            var3 = this.keyboardHeightLand;
         } else {
            var3 = this.keyboardHeight;
         }

         LayoutParams var4 = (LayoutParams)this.emojiView.getLayoutParams();
         if (var4.width != AndroidUtilities.displaySize.x || var4.height != var3) {
            var4.width = AndroidUtilities.displaySize.x;
            var4.height = var3;
            this.emojiView.setLayoutParams(var4);
            if (this.sizeNotifierLayout != null) {
               this.emojiPadding = var4.height;
               this.sizeNotifierLayout.requestLayout();
               this.onWindowSizeChanged();
            }
         }
      }

      if (this.lastSizeChangeValue1 == var1 && this.lastSizeChangeValue2 == var2) {
         this.onWindowSizeChanged();
      } else {
         this.lastSizeChangeValue1 = var1;
         this.lastSizeChangeValue2 = var2;
         boolean var5 = this.keyboardVisible;
         if (var1 > 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.keyboardVisible = var2;
         if (this.keyboardVisible && this.isPopupShowing()) {
            this.showPopup(0);
         }

         if (this.emojiPadding != 0) {
            var2 = this.keyboardVisible;
            if (!var2 && var2 != var5 && !this.isPopupShowing()) {
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
   }

   public void openKeyboard() {
      AndroidUtilities.showKeyboard(this.editText);
   }

   public void setDelegate(EditTextEmoji.EditTextEmojiDelegate var1) {
      this.delegate = var1;
   }

   public void setEnabled(boolean var1) {
      this.editText.setEnabled(var1);
      ImageView var2 = this.emojiButton;
      byte var3;
      if (var1) {
         var3 = 0;
      } else {
         var3 = 8;
      }

      var2.setVisibility(var3);
      if (var1) {
         EditTextBoldCursor var5 = this.editText;
         int var6;
         if (LocaleController.isRTL) {
            var6 = AndroidUtilities.dp(40.0F);
         } else {
            var6 = 0;
         }

         int var4;
         if (LocaleController.isRTL) {
            var4 = 0;
         } else {
            var4 = AndroidUtilities.dp(40.0F);
         }

         var5.setPadding(var6, 0, var4, AndroidUtilities.dp(8.0F));
      } else {
         this.editText.setPadding(0, 0, 0, AndroidUtilities.dp(8.0F));
      }

   }

   public void setFilters(InputFilter[] var1) {
      this.editText.setFilters(var1);
   }

   public void setFocusable(boolean var1) {
      this.editText.setFocusable(var1);
   }

   public void setHint(CharSequence var1) {
      this.editText.setHint(var1);
   }

   public void setMaxLines(int var1) {
      this.editText.setMaxLines(var1);
   }

   public void setSelection(int var1) {
      this.editText.setSelection(var1);
   }

   public void setText(CharSequence var1) {
      this.editText.setText(var1);
   }

   public interface EditTextEmojiDelegate {
      void onWindowSizeChanged(int var1);
   }
}
