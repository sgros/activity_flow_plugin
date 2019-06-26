package org.telegram.ui.Components;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.InputFilter.LengthFilter;
import android.text.style.ImageSpan;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ActionMode.Callback;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;

public class PhotoViewerCaptionEnterView extends FrameLayout implements NotificationCenter.NotificationCenterDelegate, SizeNotifierFrameLayoutPhoto.SizeNotifierFrameLayoutPhotoDelegate {
   float animationProgress = 0.0F;
   private int audioInterfaceState;
   private int captionMaxLength = 1024;
   private ActionMode currentActionMode;
   private PhotoViewerCaptionEnterView.PhotoViewerCaptionEnterViewDelegate delegate;
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

   public PhotoViewerCaptionEnterView(Context var1, SizeNotifierFrameLayoutPhoto var2, View var3) {
      super(var1);
      this.setWillNotDraw(false);
      this.setBackgroundColor(2130706432);
      this.setFocusable(true);
      this.setFocusableInTouchMode(true);
      this.windowView = var3;
      this.sizeNotifierLayout = var2;
      LinearLayout var6 = new LinearLayout(var1);
      var6.setOrientation(0);
      this.addView(var6, LayoutHelper.createFrame(-1, -2.0F, 51, 2.0F, 0.0F, 0.0F, 0.0F));
      FrameLayout var7 = new FrameLayout(var1);
      var6.addView(var7, LayoutHelper.createLinear(0, -2, 1.0F));
      this.emojiButton = new ImageView(var1);
      this.emojiButton.setImageResource(2131165492);
      this.emojiButton.setScaleType(ScaleType.CENTER_INSIDE);
      this.emojiButton.setPadding(AndroidUtilities.dp(4.0F), AndroidUtilities.dp(1.0F), 0, 0);
      var7.addView(this.emojiButton, LayoutHelper.createFrame(48, 48, 83));
      this.emojiButton.setOnClickListener(new _$$Lambda$PhotoViewerCaptionEnterView$ZqTKgld1Ygi5nBpyMN_nLjvd2fU(this));
      this.emojiButton.setContentDescription(LocaleController.getString("Emoji", 2131559331));
      this.lengthTextPaint = new TextPaint(1);
      this.lengthTextPaint.setTextSize((float)AndroidUtilities.dp(13.0F));
      this.lengthTextPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.lengthTextPaint.setColor(-2500135);
      this.messageEditText = new EditTextCaption(var1) {
         protected void onMeasure(int var1, int var2) {
            try {
               super.onMeasure(var1, var2);
            } catch (Exception var4) {
               this.setMeasuredDimension(MeasureSpec.getSize(var1), AndroidUtilities.dp(51.0F));
               FileLog.e((Throwable)var4);
            }

         }

         protected void onSelectionChanged(int var1, int var2) {
            super.onSelectionChanged(var1, var2);
            if (var1 != var2) {
               this.fixHandleView(false);
            } else {
               this.fixHandleView(true);
            }

         }
      };
      if (VERSION.SDK_INT >= 23 && this.windowView != null) {
         this.messageEditText.setCustomSelectionActionModeCallback(new Callback() {
            public boolean onActionItemClicked(ActionMode var1, MenuItem var2) {
               return false;
            }

            public boolean onCreateActionMode(ActionMode var1, Menu var2) {
               PhotoViewerCaptionEnterView.this.currentActionMode = var1;
               return true;
            }

            public void onDestroyActionMode(ActionMode var1) {
               if (PhotoViewerCaptionEnterView.this.currentActionMode == var1) {
                  PhotoViewerCaptionEnterView.this.currentActionMode = null;
               }

            }

            public boolean onPrepareActionMode(ActionMode var1, Menu var2) {
               if (VERSION.SDK_INT >= 23) {
                  PhotoViewerCaptionEnterView.this.fixActionMode(var1);
               }

               return true;
            }
         });
         this.messageEditText.setCustomInsertionActionModeCallback(new Callback() {
            public boolean onActionItemClicked(ActionMode var1, MenuItem var2) {
               return false;
            }

            public boolean onCreateActionMode(ActionMode var1, Menu var2) {
               PhotoViewerCaptionEnterView.this.currentActionMode = var1;
               return true;
            }

            public void onDestroyActionMode(ActionMode var1) {
               if (PhotoViewerCaptionEnterView.this.currentActionMode == var1) {
                  PhotoViewerCaptionEnterView.this.currentActionMode = null;
               }

            }

            public boolean onPrepareActionMode(ActionMode var1, Menu var2) {
               if (VERSION.SDK_INT >= 23) {
                  PhotoViewerCaptionEnterView.this.fixActionMode(var1);
               }

               return true;
            }
         });
      }

      this.messageEditText.setHint(LocaleController.getString("AddCaption", 2131558566));
      this.messageEditText.setImeOptions(268435456);
      EditTextCaption var4 = this.messageEditText;
      var4.setInputType(var4.getInputType() | 16384);
      this.messageEditText.setMaxLines(4);
      this.messageEditText.setHorizontallyScrolling(false);
      this.messageEditText.setTextSize(1, 18.0F);
      this.messageEditText.setGravity(80);
      this.messageEditText.setPadding(0, AndroidUtilities.dp(11.0F), 0, AndroidUtilities.dp(12.0F));
      this.messageEditText.setBackgroundDrawable((Drawable)null);
      this.messageEditText.setCursorColor(-1);
      this.messageEditText.setCursorSize(AndroidUtilities.dp(20.0F));
      this.messageEditText.setTextColor(-1);
      this.messageEditText.setHintTextColor(-1291845633);
      LengthFilter var9 = new LengthFilter(this.captionMaxLength);
      this.messageEditText.setFilters(new InputFilter[]{var9});
      var7.addView(this.messageEditText, LayoutHelper.createFrame(-1, -2.0F, 83, 52.0F, 0.0F, 6.0F, 0.0F));
      this.messageEditText.setOnKeyListener(new _$$Lambda$PhotoViewerCaptionEnterView$26OMPpPrCbxQi1_Ug7Wt6hHeAnU(this));
      this.messageEditText.setOnClickListener(new _$$Lambda$PhotoViewerCaptionEnterView$JGqljW5ddJcqpomGAK04x75zT4E(this));
      this.messageEditText.addTextChangedListener(new TextWatcher() {
         boolean processChange = false;

         public void afterTextChanged(Editable var1) {
            int var2 = PhotoViewerCaptionEnterView.this.captionMaxLength - PhotoViewerCaptionEnterView.this.messageEditText.length();
            if (var2 <= 128) {
               PhotoViewerCaptionEnterView.this.lengthText = String.format("%d", var2);
            } else {
               PhotoViewerCaptionEnterView.this.lengthText = null;
            }

            PhotoViewerCaptionEnterView.this.invalidate();
            if (!PhotoViewerCaptionEnterView.this.innerTextChange) {
               if (this.processChange) {
                  ImageSpan[] var3 = (ImageSpan[])var1.getSpans(0, var1.length(), ImageSpan.class);

                  for(var2 = 0; var2 < var3.length; ++var2) {
                     var1.removeSpan(var3[var2]);
                  }

                  Emoji.replaceEmoji(var1, PhotoViewerCaptionEnterView.this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0F), false);
                  this.processChange = false;
               }

            }
         }

         public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }

         public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
            if (!PhotoViewerCaptionEnterView.this.innerTextChange) {
               if (PhotoViewerCaptionEnterView.this.delegate != null) {
                  PhotoViewerCaptionEnterView.this.delegate.onTextChanged(var1);
               }

               if (var3 != var4 && var4 - var3 > 1) {
                  this.processChange = true;
               }

            }
         }
      });
      CombinedDrawable var8 = new CombinedDrawable(Theme.createCircleDrawable(AndroidUtilities.dp(16.0F), -10043398), var1.getResources().getDrawable(2131165484).mutate(), 0, AndroidUtilities.dp(1.0F));
      var8.setCustomSize(AndroidUtilities.dp(32.0F), AndroidUtilities.dp(32.0F));
      ImageView var5 = new ImageView(var1);
      var5.setScaleType(ScaleType.CENTER);
      var5.setImageDrawable(var8);
      var6.addView(var5, LayoutHelper.createLinear(48, 48, 80));
      var5.setOnClickListener(new _$$Lambda$PhotoViewerCaptionEnterView$XlBwyOEM4HKN92dDL_UcK7dQCVE(this));
      var5.setContentDescription(LocaleController.getString("Done", 2131559299));
   }

   private void createEmojiView() {
      if (this.emojiView == null) {
         this.emojiView = new EmojiView(false, false, this.getContext(), false, (TLRPC.ChatFull)null);
         this.emojiView.setDelegate(new EmojiView.EmojiViewDelegate() {
            // $FF: synthetic method
            public boolean isExpanded() {
               return EmojiView$EmojiViewDelegate$_CC.$default$isExpanded(this);
            }

            // $FF: synthetic method
            public boolean isSearchOpened() {
               return EmojiView$EmojiViewDelegate$_CC.$default$isSearchOpened(this);
            }

            public boolean onBackspace() {
               if (PhotoViewerCaptionEnterView.this.messageEditText.length() == 0) {
                  return false;
               } else {
                  PhotoViewerCaptionEnterView.this.messageEditText.dispatchKeyEvent(new KeyEvent(0, 67));
                  return true;
               }
            }

            // $FF: synthetic method
            public void onClearEmojiRecent() {
               EmojiView$EmojiViewDelegate$_CC.$default$onClearEmojiRecent(this);
            }

            public void onEmojiSelected(String var1) {
               if (PhotoViewerCaptionEnterView.this.messageEditText.length() + var1.length() <= PhotoViewerCaptionEnterView.this.captionMaxLength) {
                  int var2 = PhotoViewerCaptionEnterView.this.messageEditText.getSelectionEnd();
                  int var3 = var2;
                  if (var2 < 0) {
                     var3 = 0;
                  }

                  try {
                     PhotoViewerCaptionEnterView.this.innerTextChange = true;
                     CharSequence var8 = Emoji.replaceEmoji(var1, PhotoViewerCaptionEnterView.this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0F), false);
                     PhotoViewerCaptionEnterView.this.messageEditText.setText(PhotoViewerCaptionEnterView.this.messageEditText.getText().insert(var3, var8));
                     var3 += var8.length();
                     PhotoViewerCaptionEnterView.this.messageEditText.setSelection(var3, var3);
                  } catch (Exception var6) {
                     FileLog.e((Throwable)var6);
                  } finally {
                     PhotoViewerCaptionEnterView.this.innerTextChange = false;
                  }

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

   @SuppressLint({"PrivateApi"})
   private void fixActionMode(ActionMode var1) {
      try {
         Class var2 = Class.forName("com.android.internal.view.FloatingActionMode");
         Field var3 = var2.getDeclaredField("mFloatingToolbar");
         var3.setAccessible(true);
         Object var4 = var3.get(var1);
         Class var5 = Class.forName("com.android.internal.widget.FloatingToolbar");
         var3 = var5.getDeclaredField("mPopup");
         Field var10 = var5.getDeclaredField("mWidthChanged");
         var3.setAccessible(true);
         var10.setAccessible(true);
         Object var8 = var3.get(var4);
         Field var9 = Class.forName("com.android.internal.widget.FloatingToolbar$FloatingToolbarPopup").getDeclaredField("mParent");
         var9.setAccessible(true);
         if ((View)var9.get(var8) != this.windowView) {
            var9.set(var8, this.windowView);
            Method var7 = var2.getDeclaredMethod("updateViewLocationInWindow");
            var7.setAccessible(true);
            var7.invoke(var1);
         }
      } catch (Throwable var6) {
         FileLog.e(var6);
      }

   }

   private void onWindowSizeChanged() {
      int var1 = this.sizeNotifierLayout.getHeight();
      int var2 = var1;
      if (!this.keyboardVisible) {
         var2 = var1 - this.emojiPadding;
      }

      PhotoViewerCaptionEnterView.PhotoViewerCaptionEnterViewDelegate var3 = this.delegate;
      if (var3 != null) {
         var3.onWindowSizeChanged(var2);
      }

   }

   private void openKeyboardInternal() {
      byte var1;
      if (AndroidUtilities.usingHardwareInput) {
         var1 = 0;
      } else {
         var1 = 2;
      }

      this.showPopup(var1);
      this.openKeyboard();
   }

   private void showPopup(int var1) {
      if (var1 == 1) {
         if (this.emojiView == null) {
            this.createEmojiView();
         }

         this.emojiView.setVisibility(0);
         if (this.keyboardHeight <= 0) {
            this.keyboardHeight = MessagesController.getGlobalEmojiSettings().getInt("kbd_height", AndroidUtilities.dp(200.0F));
         }

         if (this.keyboardHeightLand <= 0) {
            this.keyboardHeightLand = MessagesController.getGlobalEmojiSettings().getInt("kbd_height_land3", AndroidUtilities.dp(200.0F));
         }

         android.graphics.Point var2 = AndroidUtilities.displaySize;
         if (var2.x > var2.y) {
            var1 = this.keyboardHeightLand;
         } else {
            var1 = this.keyboardHeight;
         }

         LayoutParams var3 = (LayoutParams)this.emojiView.getLayoutParams();
         var3.width = AndroidUtilities.displaySize.x;
         var3.height = var1;
         this.emojiView.setLayoutParams(var3);
         if (!AndroidUtilities.isInMultiwindow && !this.forceFloatingEmoji) {
            AndroidUtilities.hideKeyboard(this.messageEditText);
         }

         SizeNotifierFrameLayoutPhoto var4 = this.sizeNotifierLayout;
         if (var4 != null) {
            this.emojiPadding = var1;
            var4.requestLayout();
            this.emojiButton.setImageResource(2131165487);
            this.onWindowSizeChanged();
         }
      } else {
         ImageView var5 = this.emojiButton;
         if (var5 != null) {
            var5.setImageResource(2131165492);
         }

         EmojiView var6 = this.emojiView;
         if (var6 != null) {
            var6.setVisibility(8);
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

   public void addEmojiToRecent(String var1) {
      this.createEmojiView();
      this.emojiView.addEmojiToRecent(var1);
   }

   public void closeKeyboard() {
      AndroidUtilities.hideKeyboard(this.messageEditText);
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.emojiDidLoad) {
         EmojiView var4 = this.emojiView;
         if (var4 != null) {
            var4.invalidateViews();
         }
      }

   }

   public int getCursorPosition() {
      EditTextCaption var1 = this.messageEditText;
      return var1 == null ? 0 : var1.getSelectionStart();
   }

   public int getEmojiPadding() {
      return this.emojiPadding;
   }

   public CharSequence getFieldCharSequence() {
      return AndroidUtilities.getTrimmedString(this.messageEditText.getText());
   }

   public int getSelectionLength() {
      EditTextCaption var1 = this.messageEditText;
      if (var1 == null) {
         return 0;
      } else {
         int var2;
         int var3;
         try {
            var2 = var1.getSelectionEnd();
            var3 = this.messageEditText.getSelectionStart();
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
            return 0;
         }

         return var2 - var3;
      }
   }

   public boolean hideActionMode() {
      if (VERSION.SDK_INT >= 23) {
         ActionMode var1 = this.currentActionMode;
         if (var1 != null) {
            try {
               var1.finish();
            } catch (Exception var2) {
               FileLog.e((Throwable)var2);
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
      boolean var1;
      if ((!AndroidUtilities.usingHardwareInput || this.getTag() == null) && !this.keyboardVisible) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isPopupShowing() {
      EmojiView var1 = this.emojiView;
      boolean var2;
      if (var1 != null && var1.getVisibility() == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
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
   public void lambda$new$0$PhotoViewerCaptionEnterView(View var1) {
      if (!this.isPopupShowing()) {
         this.showPopup(1);
      } else {
         this.openKeyboardInternal();
      }

   }

   // $FF: synthetic method
   public boolean lambda$new$1$PhotoViewerCaptionEnterView(View var1, int var2, KeyEvent var3) {
      if (var2 == 4) {
         if (this.windowView != null && this.hideActionMode()) {
            return true;
         }

         if (!this.keyboardVisible && this.isPopupShowing()) {
            if (var3.getAction() == 1) {
               this.showPopup(0);
            }

            return true;
         }
      }

      return false;
   }

   // $FF: synthetic method
   public void lambda$new$2$PhotoViewerCaptionEnterView(View var1) {
      if (this.isPopupShowing()) {
         byte var2;
         if (AndroidUtilities.usingHardwareInput) {
            var2 = 0;
         } else {
            var2 = 2;
         }

         this.showPopup(var2);
      }

   }

   // $FF: synthetic method
   public void lambda$new$3$PhotoViewerCaptionEnterView(View var1) {
      this.delegate.onCaptionEnter();
   }

   // $FF: synthetic method
   public void lambda$setFieldFocused$4$PhotoViewerCaptionEnterView() {
      EditTextCaption var1 = this.messageEditText;
      if (var1 != null) {
         try {
            var1.requestFocus();
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }
      }

   }

   public void onCreate() {
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
      this.sizeNotifierLayout.setDelegate(this);
   }

   public void onDestroy() {
      this.hidePopup();
      if (this.isKeyboardVisible()) {
         this.closeKeyboard();
      }

      this.keyboardVisible = false;
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
      SizeNotifierFrameLayoutPhoto var1 = this.sizeNotifierLayout;
      if (var1 != null) {
         var1.setDelegate((SizeNotifierFrameLayoutPhoto.SizeNotifierFrameLayoutPhotoDelegate)null);
      }

   }

   protected void onDraw(Canvas var1) {
      if (this.lengthText != null && this.getMeasuredHeight() > AndroidUtilities.dp(48.0F)) {
         int var2 = (int)Math.ceil((double)this.lengthTextPaint.measureText(this.lengthText));
         var2 = (AndroidUtilities.dp(56.0F) - var2) / 2;
         var1.drawText(this.lengthText, (float)var2, (float)(this.getMeasuredHeight() - AndroidUtilities.dp(48.0F)), this.lengthTextPaint);
         float var3 = this.animationProgress;
         if (var3 < 1.0F) {
            this.animationProgress = var3 + 0.14166667F;
            this.invalidate();
            if (this.animationProgress >= 1.0F) {
               this.animationProgress = 1.0F;
            }

            this.lengthTextPaint.setAlpha((int)(this.animationProgress * 255.0F));
         }
      } else {
         this.lengthTextPaint.setAlpha(0);
         this.animationProgress = 0.0F;
      }

   }

   public void onSizeChanged(int var1, boolean var2) {
      if (var1 > AndroidUtilities.dp(50.0F) && this.keyboardVisible && !AndroidUtilities.isInMultiwindow && !this.forceFloatingEmoji) {
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

         this.onWindowSizeChanged();
      }
   }

   public void openKeyboard() {
      int var1;
      try {
         var1 = this.messageEditText.getSelectionStart();
      } catch (Exception var4) {
         var1 = this.messageEditText.length();
         FileLog.e((Throwable)var4);
      }

      MotionEvent var2 = MotionEvent.obtain(0L, 0L, 0, 0.0F, 0.0F, 0);
      this.messageEditText.onTouchEvent(var2);
      var2.recycle();
      var2 = MotionEvent.obtain(0L, 0L, 1, 0.0F, 0.0F, 0);
      this.messageEditText.onTouchEvent(var2);
      var2.recycle();
      AndroidUtilities.showKeyboard(this.messageEditText);

      try {
         this.messageEditText.setSelection(var1);
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
      }

   }

   public void replaceWithText(int var1, int var2, CharSequence var3, boolean var4) {
      Exception var10000;
      label40: {
         SpannableStringBuilder var5;
         boolean var10001;
         try {
            var5 = new SpannableStringBuilder(this.messageEditText.getText());
            var5.replace(var1, var2 + var1, var3);
         } catch (Exception var8) {
            var10000 = var8;
            var10001 = false;
            break label40;
         }

         if (var4) {
            try {
               Emoji.replaceEmoji(var5, this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0F), false);
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label40;
            }
         }

         try {
            this.messageEditText.setText(var5);
            if (var3.length() + var1 <= this.messageEditText.length()) {
               this.messageEditText.setSelection(var1 + var3.length());
               return;
            }
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
            break label40;
         }

         try {
            this.messageEditText.setSelection(this.messageEditText.length());
            return;
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
         }
      }

      Exception var10 = var10000;
      FileLog.e((Throwable)var10);
   }

   public void setDelegate(PhotoViewerCaptionEnterView.PhotoViewerCaptionEnterViewDelegate var1) {
      this.delegate = var1;
   }

   public void setFieldFocused(boolean var1) {
      EditTextCaption var2 = this.messageEditText;
      if (var2 != null) {
         if (var1) {
            if (!var2.isFocused()) {
               this.messageEditText.postDelayed(new _$$Lambda$PhotoViewerCaptionEnterView$DD_m3yT_F769ozmEz6bVJGE02zA(this), 600L);
            }
         } else if (var2.isFocused() && !this.keyboardVisible) {
            this.messageEditText.clearFocus();
         }

      }
   }

   public void setFieldText(CharSequence var1) {
      EditTextCaption var2 = this.messageEditText;
      if (var2 != null) {
         var2.setText(var1);
         EditTextCaption var5 = this.messageEditText;
         var5.setSelection(var5.getText().length());
         PhotoViewerCaptionEnterView.PhotoViewerCaptionEnterViewDelegate var6 = this.delegate;
         if (var6 != null) {
            var6.onTextChanged(this.messageEditText.getText());
         }

         int var3 = this.captionMaxLength;
         this.captionMaxLength = MessagesController.getInstance(UserConfig.selectedAccount).maxCaptionLength;
         int var4 = this.captionMaxLength;
         if (var3 != var4) {
            LengthFilter var7 = new LengthFilter(var4);
            this.messageEditText.setFilters(new InputFilter[]{var7});
         }

      }
   }

   public void setForceFloatingEmoji(boolean var1) {
      this.forceFloatingEmoji = var1;
   }

   public interface PhotoViewerCaptionEnterViewDelegate {
      void onCaptionEnter();

      void onTextChanged(CharSequence var1);

      void onWindowSizeChanged(int var1);
   }
}
