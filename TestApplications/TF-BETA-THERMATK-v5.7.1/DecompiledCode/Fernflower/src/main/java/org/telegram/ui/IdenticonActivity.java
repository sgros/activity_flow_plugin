package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.EmojiData;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.IdenticonDrawable;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.URLSpanReplacement;

public class IdenticonActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private AnimatorSet animatorSet;
   private int chat_id;
   private TextView codeTextView;
   private FrameLayout container;
   private boolean emojiSelected;
   private String emojiText;
   private TextView emojiTextView;
   private AnimatorSet hintAnimatorSet;
   private LinearLayout linearLayout;
   private LinearLayout linearLayout1;
   private TextView textView;
   private int textWidth;

   public IdenticonActivity(Bundle var1) {
      super(var1);
   }

   // $FF: synthetic method
   static View access$1000(IdenticonActivity var0) {
      return var0.fragmentView;
   }

   // $FF: synthetic method
   static View access$1100(IdenticonActivity var0) {
      return var0.fragmentView;
   }

   // $FF: synthetic method
   static View access$500(IdenticonActivity var0) {
      return var0.fragmentView;
   }

   // $FF: synthetic method
   static View access$600(IdenticonActivity var0) {
      return var0.fragmentView;
   }

   // $FF: synthetic method
   static View access$800(IdenticonActivity var0) {
      return var0.fragmentView;
   }

   // $FF: synthetic method
   static View access$900(IdenticonActivity var0) {
      return var0.fragmentView;
   }

   private void fixLayout() {
      super.fragmentView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
         public boolean onPreDraw() {
            if (IdenticonActivity.access$500(IdenticonActivity.this) == null) {
               return true;
            } else {
               IdenticonActivity.access$600(IdenticonActivity.this).getViewTreeObserver().removeOnPreDrawListener(this);
               int var1 = ((WindowManager)ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
               if (var1 != 3 && var1 != 1) {
                  IdenticonActivity.this.linearLayout.setOrientation(1);
               } else {
                  IdenticonActivity.this.linearLayout.setOrientation(0);
               }

               IdenticonActivity.access$1100(IdenticonActivity.this).setPadding(IdenticonActivity.access$800(IdenticonActivity.this).getPaddingLeft(), 0, IdenticonActivity.access$900(IdenticonActivity.this).getPaddingRight(), IdenticonActivity.access$1000(IdenticonActivity.this).getPaddingBottom());
               return true;
            }
         }
      });
   }

   // $FF: synthetic method
   static boolean lambda$createView$0(View var0, MotionEvent var1) {
      return true;
   }

   private void updateEmojiButton(boolean var1) {
      AnimatorSet var2 = this.animatorSet;
      if (var2 != null) {
         var2.cancel();
         this.animatorSet = null;
      }

      float var3 = 1.0F;
      TextView var4;
      float var5;
      if (var1) {
         this.animatorSet = new AnimatorSet();
         var2 = this.animatorSet;
         var4 = this.emojiTextView;
         if (this.emojiSelected) {
            var5 = 1.0F;
         } else {
            var5 = 0.0F;
         }

         ObjectAnimator var12 = ObjectAnimator.ofFloat(var4, "alpha", new float[]{var5});
         TextView var6 = this.codeTextView;
         if (this.emojiSelected) {
            var5 = 0.0F;
         } else {
            var5 = 1.0F;
         }

         ObjectAnimator var14 = ObjectAnimator.ofFloat(var6, "alpha", new float[]{var5});
         TextView var7 = this.emojiTextView;
         if (this.emojiSelected) {
            var5 = 1.0F;
         } else {
            var5 = 0.0F;
         }

         ObjectAnimator var15 = ObjectAnimator.ofFloat(var7, "scaleX", new float[]{var5});
         TextView var8 = this.emojiTextView;
         if (this.emojiSelected) {
            var5 = 1.0F;
         } else {
            var5 = 0.0F;
         }

         ObjectAnimator var16 = ObjectAnimator.ofFloat(var8, "scaleY", new float[]{var5});
         TextView var9 = this.codeTextView;
         if (this.emojiSelected) {
            var5 = 0.0F;
         } else {
            var5 = 1.0F;
         }

         ObjectAnimator var17 = ObjectAnimator.ofFloat(var9, "scaleX", new float[]{var5});
         TextView var10 = this.codeTextView;
         if (this.emojiSelected) {
            var3 = 0.0F;
         }

         var2.playTogether(new Animator[]{var12, var14, var15, var16, var17, ObjectAnimator.ofFloat(var10, "scaleY", new float[]{var3})});
         this.animatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               if (var1.equals(IdenticonActivity.this.animatorSet)) {
                  IdenticonActivity.this.animatorSet = null;
               }

            }
         });
         this.animatorSet.setInterpolator(new DecelerateInterpolator());
         this.animatorSet.setDuration(150L);
         this.animatorSet.start();
      } else {
         TextView var11 = this.emojiTextView;
         if (this.emojiSelected) {
            var5 = 1.0F;
         } else {
            var5 = 0.0F;
         }

         var11.setAlpha(var5);
         var11 = this.codeTextView;
         if (this.emojiSelected) {
            var5 = 0.0F;
         } else {
            var5 = 1.0F;
         }

         var11.setAlpha(var5);
         var11 = this.emojiTextView;
         if (this.emojiSelected) {
            var5 = 1.0F;
         } else {
            var5 = 0.0F;
         }

         var11.setScaleX(var5);
         var11 = this.emojiTextView;
         if (this.emojiSelected) {
            var5 = 1.0F;
         } else {
            var5 = 0.0F;
         }

         var11.setScaleY(var5);
         var11 = this.codeTextView;
         if (this.emojiSelected) {
            var5 = 0.0F;
         } else {
            var5 = 1.0F;
         }

         var11.setScaleX(var5);
         var11 = this.codeTextView;
         if (this.emojiSelected) {
            var3 = 0.0F;
         }

         var11.setScaleY(var3);
      }

      var4 = this.emojiTextView;
      String var13;
      if (!this.emojiSelected) {
         var13 = "chat_emojiPanelIcon";
      } else {
         var13 = "chat_emojiPanelIconSelected";
      }

      var4.setTag(var13);
   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("EncryptionKey", 2131559360));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               IdenticonActivity.this.finishFragment();
            }

         }
      });
      super.fragmentView = new FrameLayout(var1);
      View var2 = super.fragmentView;
      FrameLayout var3 = (FrameLayout)var2;
      var2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      super.fragmentView.setOnTouchListener(_$$Lambda$IdenticonActivity$Yvzzx489TCib4oTluPwFgAoS_54.INSTANCE);
      this.linearLayout = new LinearLayout(var1);
      this.linearLayout.setOrientation(1);
      this.linearLayout.setWeightSum(100.0F);
      var3.addView(this.linearLayout, LayoutHelper.createFrame(-1, -1.0F));
      FrameLayout var13 = new FrameLayout(var1);
      var13.setPadding(AndroidUtilities.dp(20.0F), AndroidUtilities.dp(20.0F), AndroidUtilities.dp(20.0F), AndroidUtilities.dp(20.0F));
      this.linearLayout.addView(var13, LayoutHelper.createLinear(-1, -1, 50.0F));
      ImageView var16 = new ImageView(var1);
      var16.setScaleType(ScaleType.FIT_XY);
      var13.addView(var16, LayoutHelper.createFrame(-1, -1.0F));
      this.container = new FrameLayout(var1) {
         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            super.onLayout(var1, var2, var3, var4, var5);
            if (IdenticonActivity.this.codeTextView != null) {
               var2 = IdenticonActivity.this.codeTextView.getLeft() + IdenticonActivity.this.codeTextView.getMeasuredWidth() / 2 - IdenticonActivity.this.emojiTextView.getMeasuredWidth() / 2;
               var3 = (IdenticonActivity.this.codeTextView.getMeasuredHeight() - IdenticonActivity.this.emojiTextView.getMeasuredHeight()) / 2 + IdenticonActivity.this.linearLayout1.getTop() - AndroidUtilities.dp(16.0F);
               IdenticonActivity.this.emojiTextView.layout(var2, var3, IdenticonActivity.this.emojiTextView.getMeasuredWidth() + var2, IdenticonActivity.this.emojiTextView.getMeasuredHeight() + var3);
            }

         }
      };
      this.container.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      this.linearLayout.addView(this.container, LayoutHelper.createLinear(-1, -1, 50.0F));
      this.linearLayout1 = new LinearLayout(var1);
      this.linearLayout1.setOrientation(1);
      this.linearLayout1.setPadding(AndroidUtilities.dp(10.0F), 0, AndroidUtilities.dp(10.0F), 0);
      this.container.addView(this.linearLayout1, LayoutHelper.createFrame(-2, -2, 17));
      this.codeTextView = new TextView(var1);
      this.codeTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
      this.codeTextView.setGravity(17);
      this.codeTextView.setTypeface(Typeface.MONOSPACE);
      this.codeTextView.setTextSize(1, 16.0F);
      this.linearLayout1.addView(this.codeTextView, LayoutHelper.createLinear(-2, -2, 1));
      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
      this.textView.setLinkTextColor(Theme.getColor("windowBackgroundWhiteLinkText"));
      this.textView.setTextSize(1, 16.0F);
      this.textView.setLinksClickable(true);
      this.textView.setClickable(true);
      this.textView.setGravity(17);
      this.textView.setMovementMethod(new IdenticonActivity.LinkMovementMethodMy());
      this.linearLayout1.addView(this.textView, LayoutHelper.createFrame(-2, -2, 1));
      this.emojiTextView = new TextView(var1);
      this.emojiTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
      this.emojiTextView.setGravity(17);
      this.emojiTextView.setTextSize(1, 32.0F);
      this.container.addView(this.emojiTextView, LayoutHelper.createFrame(-2, -2.0F));
      TLRPC.EncryptedChat var11 = MessagesController.getInstance(super.currentAccount).getEncryptedChat(this.chat_id);
      if (var11 != null) {
         IdenticonDrawable var14 = new IdenticonDrawable();
         var16.setImageDrawable(var14);
         var14.setEncryptedChat(var11);
         TLRPC.User var4 = MessagesController.getInstance(super.currentAccount).getUser(var11.user_id);
         SpannableStringBuilder var17 = new SpannableStringBuilder();
         StringBuilder var15 = new StringBuilder();
         byte[] var5 = var11.key_hash;
         int var6;
         if (var5.length > 16) {
            String var18 = Utilities.bytesToHex(var5);

            for(var6 = 0; var6 < 32; ++var6) {
               if (var6 != 0) {
                  if (var6 % 8 == 0) {
                     var17.append('\n');
                  } else if (var6 % 4 == 0) {
                     var17.append(' ');
                  }
               }

               int var7 = var6 * 2;
               var17.append(var18.substring(var7, var7 + 2));
               var17.append(' ');
            }

            var17.append("\n");

            for(var6 = 0; var6 < 5; ++var6) {
               var5 = var11.key_hash;
               int var8 = var6 * 4 + 16;
               byte var20 = var5[var8];
               byte var9 = var5[var8 + 1];
               byte var10 = var5[var8 + 2];
               byte var21 = var5[var8 + 3];
               if (var6 != 0) {
                  var15.append(" ");
               }

               String[] var19 = EmojiData.emojiSecret;
               var15.append(var19[(var21 & 255 | (var20 & 127) << 24 | (var9 & 255) << 16 | (var10 & 255) << 8) % var19.length]);
            }

            this.emojiText = var15.toString();
         }

         this.codeTextView.setText(var17.toString());
         var17.clear();
         String var12 = var4.first_name;
         var17.append(AndroidUtilities.replaceTags(LocaleController.formatString("EncryptionKeyDescription", 2131559361, var12, var12)));
         var6 = var17.toString().indexOf("telegram.org");
         if (var6 != -1) {
            var17.setSpan(new URLSpanReplacement(LocaleController.getString("EncryptionKeyLink", 2131559362)), var6, var6 + 12, 33);
         }

         this.textView.setText(var17);
      }

      this.updateEmojiButton(false);
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.emojiDidLoad) {
         TextView var4 = this.emojiTextView;
         if (var4 != null) {
            var4.invalidate();
         }
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      return new ThemeDescription[]{new ThemeDescription(this.container, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"), new ThemeDescription(this.textView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"), new ThemeDescription(this.codeTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"), new ThemeDescription(this.textView, ThemeDescription.FLAG_LINKCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteLinkText")};
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      this.fixLayout();
   }

   public boolean onFragmentCreate() {
      this.chat_id = this.getArguments().getInt("chat_id");
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
      return super.onFragmentCreate();
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
   }

   public void onResume() {
      super.onResume();
      this.fixLayout();
   }

   protected void onTransitionAnimationEnd(boolean var1, boolean var2) {
      if (var1 && !var2) {
         String var3 = this.emojiText;
         if (var3 != null) {
            TextView var4 = this.emojiTextView;
            var4.setText(Emoji.replaceEmoji(var3, var4.getPaint().getFontMetricsInt(), AndroidUtilities.dp(32.0F), false));
         }
      }

   }

   private static class LinkMovementMethodMy extends LinkMovementMethod {
      private LinkMovementMethodMy() {
      }

      // $FF: synthetic method
      LinkMovementMethodMy(Object var1) {
         this();
      }

      public boolean onTouchEvent(TextView var1, Spannable var2, MotionEvent var3) {
         try {
            boolean var4 = super.onTouchEvent(var1, var2, var3);
            return var4;
         } catch (Exception var5) {
            FileLog.e((Throwable)var5);
            return false;
         }
      }
   }
}
