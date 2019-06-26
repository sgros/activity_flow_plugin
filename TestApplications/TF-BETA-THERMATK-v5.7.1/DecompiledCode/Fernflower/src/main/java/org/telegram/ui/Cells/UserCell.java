package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.NotificationsSettingsActivity;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox;
import org.telegram.ui.Components.CheckBoxSquare;
import org.telegram.ui.Components.LayoutHelper;

public class UserCell extends FrameLayout {
   private TextView adminTextView;
   private AvatarDrawable avatarDrawable;
   private BackupImageView avatarImageView;
   private CheckBox checkBox;
   private CheckBoxSquare checkBoxBig;
   private int currentAccount;
   private int currentDrawable;
   private int currentId;
   private CharSequence currentName;
   private TLObject currentObject;
   private CharSequence currentStatus;
   private TLRPC.EncryptedChat encryptedChat;
   private ImageView imageView;
   private TLRPC.FileLocation lastAvatar;
   private String lastName;
   private int lastStatus;
   private SimpleTextView nameTextView;
   private boolean needDivider;
   private int statusColor;
   private int statusOnlineColor;
   private SimpleTextView statusTextView;

   public UserCell(Context var1, int var2, int var3, boolean var4) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.statusColor = Theme.getColor("windowBackgroundWhiteGrayText");
      this.statusOnlineColor = Theme.getColor("windowBackgroundWhiteBlueText");
      this.avatarDrawable = new AvatarDrawable();
      this.avatarImageView = new BackupImageView(var1);
      this.avatarImageView.setRoundRadius(AndroidUtilities.dp(24.0F));
      BackupImageView var5 = this.avatarImageView;
      byte var6;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      float var7;
      if (LocaleController.isRTL) {
         var7 = 0.0F;
      } else {
         var7 = (float)(var2 + 7);
      }

      float var8;
      if (LocaleController.isRTL) {
         var8 = (float)(var2 + 7);
      } else {
         var8 = 0.0F;
      }

      this.addView(var5, LayoutHelper.createFrame(46, 46.0F, var6 | 48, var7, 6.0F, var8, 0.0F));
      this.nameTextView = new SimpleTextView(var1);
      this.nameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.nameTextView.setTextSize(16);
      SimpleTextView var15 = this.nameTextView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      var15.setGravity(var6 | 48);
      var15 = this.nameTextView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      boolean var9 = LocaleController.isRTL;
      byte var10 = 18;
      byte var11;
      int var19;
      if (var9) {
         if (var3 == 2) {
            var11 = 18;
         } else {
            var11 = 0;
         }

         var19 = var11 + 28;
      } else {
         var19 = var2 + 64;
      }

      var8 = (float)var19;
      if (LocaleController.isRTL) {
         var19 = var2 + 64;
      } else {
         if (var3 == 2) {
            var11 = var10;
         } else {
            var11 = 0;
         }

         var19 = var11 + 28;
      }

      var7 = (float)var19;
      this.addView(var15, LayoutHelper.createFrame(-1, 20.0F, var6 | 48, var8, 10.0F, var7, 0.0F));
      this.statusTextView = new SimpleTextView(var1);
      this.statusTextView.setTextSize(15);
      var15 = this.statusTextView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      var15.setGravity(var6 | 48);
      var15 = this.statusTextView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      if (LocaleController.isRTL) {
         var7 = 28.0F;
      } else {
         var7 = (float)(var2 + 64);
      }

      if (LocaleController.isRTL) {
         var8 = (float)(var2 + 64);
      } else {
         var8 = 28.0F;
      }

      this.addView(var15, LayoutHelper.createFrame(-1, 20.0F, var6 | 48, var7, 32.0F, var8, 0.0F));
      this.imageView = new ImageView(var1);
      this.imageView.setScaleType(ScaleType.CENTER);
      this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), Mode.MULTIPLY));
      this.imageView.setVisibility(8);
      ImageView var16 = this.imageView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      if (LocaleController.isRTL) {
         var7 = 0.0F;
      } else {
         var7 = 16.0F;
      }

      if (LocaleController.isRTL) {
         var8 = 16.0F;
      } else {
         var8 = 0.0F;
      }

      this.addView(var16, LayoutHelper.createFrame(-2, -2.0F, var6 | 16, var7, 0.0F, var8, 0.0F));
      byte var13;
      if (var3 == 2) {
         this.checkBoxBig = new CheckBoxSquare(var1, false);
         CheckBoxSquare var17 = this.checkBoxBig;
         if (LocaleController.isRTL) {
            var13 = 3;
         } else {
            var13 = 5;
         }

         if (LocaleController.isRTL) {
            var7 = 19.0F;
         } else {
            var7 = 0.0F;
         }

         if (LocaleController.isRTL) {
            var8 = 0.0F;
         } else {
            var8 = 19.0F;
         }

         this.addView(var17, LayoutHelper.createFrame(18, 18.0F, var13 | 16, var7, 0.0F, var8, 0.0F));
      } else if (var3 == 1) {
         this.checkBox = new CheckBox(var1, 2131165802);
         this.checkBox.setVisibility(4);
         this.checkBox.setColor(Theme.getColor("checkbox"), Theme.getColor("checkboxCheck"));
         CheckBox var18 = this.checkBox;
         byte var14;
         if (LocaleController.isRTL) {
            var14 = 5;
         } else {
            var14 = 3;
         }

         if (LocaleController.isRTL) {
            var7 = 0.0F;
         } else {
            var7 = (float)(var2 + 37);
         }

         if (LocaleController.isRTL) {
            var8 = (float)(var2 + 37);
         } else {
            var8 = 0.0F;
         }

         this.addView(var18, LayoutHelper.createFrame(22, 22.0F, var14 | 48, var7, 40.0F, var8, 0.0F));
      }

      if (var4) {
         this.adminTextView = new TextView(var1);
         this.adminTextView.setTextSize(1, 14.0F);
         this.adminTextView.setTextColor(Theme.getColor("profile_creatorIcon"));
         TextView var12 = this.adminTextView;
         if (LocaleController.isRTL) {
            var13 = 3;
         } else {
            var13 = 5;
         }

         if (LocaleController.isRTL) {
            var7 = 23.0F;
         } else {
            var7 = 0.0F;
         }

         if (LocaleController.isRTL) {
            var8 = 0.0F;
         } else {
            var8 = 23.0F;
         }

         this.addView(var12, LayoutHelper.createFrame(-2, -2.0F, var13 | 48, var7, 10.0F, var8, 0.0F));
      }

      this.setFocusable(true);
   }

   public boolean hasOverlappingRendering() {
      return false;
   }

   public void invalidate() {
      super.invalidate();
      CheckBoxSquare var1 = this.checkBoxBig;
      if (var1 != null) {
         var1.invalidate();
      }

   }

   protected void onDraw(Canvas var1) {
      if (this.needDivider) {
         float var2;
         if (LocaleController.isRTL) {
            var2 = 0.0F;
         } else {
            var2 = (float)AndroidUtilities.dp(68.0F);
         }

         float var3 = (float)(this.getMeasuredHeight() - 1);
         int var4 = this.getMeasuredWidth();
         int var5;
         if (LocaleController.isRTL) {
            var5 = AndroidUtilities.dp(68.0F);
         } else {
            var5 = 0;
         }

         var1.drawLine(var2, var3, (float)(var4 - var5), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
      }

   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      CheckBoxSquare var2 = this.checkBoxBig;
      if (var2 != null && var2.getVisibility() == 0) {
         var1.setCheckable(true);
         var1.setChecked(this.checkBoxBig.isChecked());
         var1.setClassName("android.widget.CheckBox");
      } else {
         CheckBox var3 = this.checkBox;
         if (var3 != null && var3.getVisibility() == 0) {
            var1.setCheckable(true);
            var1.setChecked(this.checkBox.isChecked());
            var1.setClassName("android.widget.CheckBox");
         }
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(58.0F) + this.needDivider, 1073741824));
   }

   public void setAvatarPadding(int var1) {
      LayoutParams var2 = (LayoutParams)this.avatarImageView.getLayoutParams();
      boolean var3 = LocaleController.isRTL;
      float var4 = 0.0F;
      float var5;
      if (var3) {
         var5 = 0.0F;
      } else {
         var5 = (float)(var1 + 7);
      }

      var2.leftMargin = AndroidUtilities.dp(var5);
      if (LocaleController.isRTL) {
         var5 = (float)(var1 + 7);
      } else {
         var5 = 0.0F;
      }

      var2.rightMargin = AndroidUtilities.dp(var5);
      this.avatarImageView.setLayoutParams(var2);
      var2 = (LayoutParams)this.nameTextView.getLayoutParams();
      var3 = LocaleController.isRTL;
      byte var6 = 18;
      byte var7;
      int var10;
      if (var3) {
         if (this.checkBoxBig != null) {
            var7 = 18;
         } else {
            var7 = 0;
         }

         var10 = var7 + 28;
      } else {
         var10 = var1 + 64;
      }

      var2.leftMargin = AndroidUtilities.dp((float)var10);
      if (LocaleController.isRTL) {
         var5 = (float)(var1 + 64);
      } else {
         if (this.checkBoxBig != null) {
            var7 = var6;
         } else {
            var7 = 0;
         }

         var5 = (float)(var7 + 28);
      }

      var2.rightMargin = AndroidUtilities.dp(var5);
      var2 = (LayoutParams)this.statusTextView.getLayoutParams();
      var3 = LocaleController.isRTL;
      float var8 = 28.0F;
      if (var3) {
         var5 = 28.0F;
      } else {
         var5 = (float)(var1 + 64);
      }

      var2.leftMargin = AndroidUtilities.dp(var5);
      var5 = var8;
      if (LocaleController.isRTL) {
         var5 = (float)(var1 + 64);
      }

      var2.rightMargin = AndroidUtilities.dp(var5);
      CheckBox var9 = this.checkBox;
      if (var9 != null) {
         var2 = (LayoutParams)var9.getLayoutParams();
         if (LocaleController.isRTL) {
            var5 = 0.0F;
         } else {
            var5 = (float)(var1 + 37);
         }

         var2.leftMargin = AndroidUtilities.dp(var5);
         var5 = var4;
         if (LocaleController.isRTL) {
            var5 = (float)(var1 + 37);
         }

         var2.rightMargin = AndroidUtilities.dp(var5);
      }

   }

   public void setCheckDisabled(boolean var1) {
      CheckBoxSquare var2 = this.checkBoxBig;
      if (var2 != null) {
         var2.setDisabled(var1);
      }

   }

   public void setChecked(boolean var1, boolean var2) {
      CheckBox var3 = this.checkBox;
      if (var3 != null) {
         if (var3.getVisibility() != 0) {
            this.checkBox.setVisibility(0);
         }

         this.checkBox.setChecked(var1, var2);
      } else {
         CheckBoxSquare var4 = this.checkBoxBig;
         if (var4 != null) {
            if (var4.getVisibility() != 0) {
               this.checkBoxBig.setVisibility(0);
            }

            this.checkBoxBig.setChecked(var1, var2);
         }
      }

   }

   public void setCurrentId(int var1) {
      this.currentId = var1;
   }

   public void setData(TLObject var1, CharSequence var2, CharSequence var3, int var4) {
      this.setData(var1, (TLRPC.EncryptedChat)null, var2, var3, var4, false);
   }

   public void setData(TLObject var1, CharSequence var2, CharSequence var3, int var4, boolean var5) {
      this.setData(var1, (TLRPC.EncryptedChat)null, var2, var3, var4, var5);
   }

   public void setData(TLObject var1, TLRPC.EncryptedChat var2, CharSequence var3, CharSequence var4, int var5, boolean var6) {
      if (var1 == null && var3 == null && var4 == null) {
         this.currentStatus = null;
         this.currentName = null;
         this.currentObject = null;
         this.nameTextView.setText("");
         this.statusTextView.setText("");
         this.avatarImageView.setImageDrawable((Drawable)null);
      } else {
         this.encryptedChat = var2;
         this.currentStatus = var4;
         this.currentName = var3;
         this.currentObject = var1;
         this.currentDrawable = var5;
         this.needDivider = var6;
         this.setWillNotDraw(this.needDivider ^ true);
         this.update(0);
      }
   }

   public void setException(NotificationsSettingsActivity.NotificationException var1, CharSequence var2, boolean var3) {
      boolean var4 = var1.hasCustom;
      int var5 = var1.notify;
      int var6 = var1.muteUntil;
      boolean var7 = false;
      String var8;
      int var14;
      if (var5 == 3 && var6 != Integer.MAX_VALUE) {
         var14 = var6 - ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
         if (var14 <= 0) {
            if (var4) {
               var8 = LocaleController.getString("NotificationsCustom", 2131560059);
            } else {
               var8 = LocaleController.getString("NotificationsUnmuted", 2131560094);
            }
         } else if (var14 < 3600) {
            var8 = LocaleController.formatString("WillUnmuteIn", 2131561122, LocaleController.formatPluralString("Minutes", var14 / 60));
         } else if (var14 < 86400) {
            var8 = LocaleController.formatString("WillUnmuteIn", 2131561122, LocaleController.formatPluralString("Hours", (int)Math.ceil((double)((float)var14 / 60.0F / 60.0F))));
         } else if (var14 < 31536000) {
            var8 = LocaleController.formatString("WillUnmuteIn", 2131561122, LocaleController.formatPluralString("Days", (int)Math.ceil((double)((float)var14 / 60.0F / 60.0F / 24.0F))));
         } else {
            var8 = null;
         }
      } else {
         if (var5 == 0 || var5 == 1) {
            var7 = true;
         }

         if (var7 && var4) {
            var8 = LocaleController.getString("NotificationsCustom", 2131560059);
         } else if (var7) {
            var8 = LocaleController.getString("NotificationsUnmuted", 2131560094);
         } else {
            var8 = LocaleController.getString("NotificationsMuted", 2131560076);
         }
      }

      String var9 = var8;
      if (var8 == null) {
         var9 = LocaleController.getString("NotificationsOff", 2131560078);
      }

      long var10 = var1.did;
      var14 = (int)var10;
      var6 = (int)(var10 >> 32);
      TLRPC.User var12;
      if (var14 != 0) {
         if (var14 > 0) {
            var12 = MessagesController.getInstance(this.currentAccount).getUser(var14);
            if (var12 != null) {
               this.setData(var12, (TLRPC.EncryptedChat)null, var2, var9, 0, var3);
            }
         } else {
            TLRPC.Chat var13 = MessagesController.getInstance(this.currentAccount).getChat(-var14);
            if (var13 != null) {
               this.setData(var13, (TLRPC.EncryptedChat)null, var2, var9, 0, var3);
            }
         }
      } else {
         TLRPC.EncryptedChat var15 = MessagesController.getInstance(this.currentAccount).getEncryptedChat(var6);
         if (var15 != null) {
            var12 = MessagesController.getInstance(this.currentAccount).getUser(var15.user_id);
            if (var12 != null) {
               this.setData(var12, var15, var2, var9, 0, false);
            }
         }
      }

   }

   public void setIsAdmin(int var1) {
      TextView var2 = this.adminTextView;
      if (var2 != null) {
         byte var3;
         if (var1 != 0) {
            var3 = 0;
         } else {
            var3 = 8;
         }

         var2.setVisibility(var3);
         if (var1 == 1) {
            this.adminTextView.setText(LocaleController.getString("ChannelCreator", 2131558942));
         } else if (var1 == 2) {
            this.adminTextView.setText(LocaleController.getString("ChannelAdmin", 2131558926));
         }

         if (var1 != 0) {
            CharSequence var4 = this.adminTextView.getText();
            int var6 = (int)Math.ceil((double)this.adminTextView.getPaint().measureText(var4, 0, var4.length()));
            SimpleTextView var5 = this.nameTextView;
            if (LocaleController.isRTL) {
               var1 = AndroidUtilities.dp(6.0F) + var6;
            } else {
               var1 = 0;
            }

            if (!LocaleController.isRTL) {
               var6 += AndroidUtilities.dp(6.0F);
            } else {
               var6 = 0;
            }

            var5.setPadding(var1, 0, var6, 0);
         } else {
            this.nameTextView.setPadding(0, 0, 0, 0);
         }

      }
   }

   public void setNameTypeface(Typeface var1) {
      this.nameTextView.setTypeface(var1);
   }

   public void setStatusColors(int var1, int var2) {
      this.statusColor = var1;
      this.statusOnlineColor = var2;
   }

   public void update(int var1) {
      TLRPC.Chat var4;
      TLRPC.User var11;
      Object var13;
      label186: {
         TLObject var2 = this.currentObject;
         if (var2 instanceof TLRPC.User) {
            var11 = (TLRPC.User)var2;
            TLRPC.UserProfilePhoto var3 = var11.photo;
            if (var3 != null) {
               var13 = var3.photo_small;
               var4 = null;
               break label186;
            }
         } else {
            if (var2 instanceof TLRPC.Chat) {
               var4 = (TLRPC.Chat)var2;
               TLRPC.ChatPhoto var12 = var4.photo;
               if (var12 != null) {
                  var13 = var12.photo_small;
                  var11 = null;
               } else {
                  var11 = null;
                  var13 = var11;
               }
               break label186;
            }

            var11 = null;
         }

         var13 = null;
         var4 = null;
      }

      String var9;
      TLRPC.UserStatus var14;
      String var15;
      if (var1 != 0) {
         boolean var6;
         label177: {
            label176: {
               if ((var1 & 2) != 0) {
                  if (this.lastAvatar != null && var13 == null) {
                     break label176;
                  }

                  TLRPC.FileLocation var5 = this.lastAvatar;
                  if (var5 == null && var13 != null && var5 != null && var13 != null && (var5.volume_id != ((TLRPC.FileLocation)var13).volume_id || var5.local_id != ((TLRPC.FileLocation)var13).local_id)) {
                     break label176;
                  }
               }

               var6 = false;
               break label177;
            }

            var6 = true;
         }

         boolean var7 = var6;
         if (var11 != null) {
            var7 = var6;
            if (!var6) {
               var7 = var6;
               if ((var1 & 4) != 0) {
                  var14 = var11.status;
                  int var8;
                  if (var14 != null) {
                     var8 = var14.expires;
                  } else {
                     var8 = 0;
                  }

                  var7 = var6;
                  if (var8 != this.lastStatus) {
                     var7 = true;
                  }
               }
            }
         }

         if (!var7 && this.currentName == null && this.lastName != null && (var1 & 1) != 0) {
            if (var11 != null) {
               var9 = UserObject.getUserName(var11);
            } else {
               var9 = var4.title;
            }

            var15 = var9;
            if (!var9.equals(this.lastName)) {
               var7 = true;
               var15 = var9;
            }
         } else {
            var15 = null;
         }

         if (!var7) {
            return;
         }
      } else {
         var15 = null;
      }

      CharSequence var20;
      if (var11 != null) {
         this.avatarDrawable.setInfo(var11);
         TLRPC.UserStatus var19 = var11.status;
         if (var19 != null) {
            this.lastStatus = var19.expires;
         } else {
            this.lastStatus = 0;
         }
      } else if (var4 != null) {
         this.avatarDrawable.setInfo(var4);
      } else {
         var20 = this.currentName;
         if (var20 != null) {
            this.avatarDrawable.setInfo(this.currentId, var20.toString(), (String)null, false);
         } else {
            this.avatarDrawable.setInfo(this.currentId, "#", (String)null, false);
         }
      }

      var20 = this.currentName;
      if (var20 != null) {
         this.lastName = null;
         this.nameTextView.setText(var20);
      } else {
         if (var11 != null) {
            var9 = var15;
            if (var15 == null) {
               var9 = UserObject.getUserName(var11);
            }

            this.lastName = var9;
         } else {
            var9 = var15;
            if (var15 == null) {
               var9 = var4.title;
            }

            this.lastName = var9;
         }

         this.nameTextView.setText(this.lastName);
      }

      if (this.currentStatus != null) {
         this.statusTextView.setTextColor(this.statusColor);
         this.statusTextView.setText(this.currentStatus);
      } else if (var11 != null) {
         if (var11.bot) {
            label198: {
               this.statusTextView.setTextColor(this.statusColor);
               if (!var11.bot_chat_history) {
                  TextView var17 = this.adminTextView;
                  if (var17 == null || var17.getVisibility() != 0) {
                     this.statusTextView.setText(LocaleController.getString("BotStatusCantRead", 2131558858));
                     break label198;
                  }
               }

               this.statusTextView.setText(LocaleController.getString("BotStatusRead", 2131558859));
            }
         } else {
            label141: {
               if (var11.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                  var14 = var11.status;
                  if ((var14 == null || var14.expires <= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) && !MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(var11.id)) {
                     this.statusTextView.setTextColor(this.statusColor);
                     this.statusTextView.setText(LocaleController.formatUserStatus(this.currentAccount, var11));
                     break label141;
                  }
               }

               this.statusTextView.setTextColor(this.statusOnlineColor);
               this.statusTextView.setText(LocaleController.getString("Online", 2131560100));
            }
         }
      }

      int var16 = this.imageView.getVisibility();
      byte var10 = 8;
      if (var16 == 0 && this.currentDrawable == 0 || this.imageView.getVisibility() == 8 && this.currentDrawable != 0) {
         ImageView var18 = this.imageView;
         if (this.currentDrawable != 0) {
            var10 = 0;
         }

         var18.setVisibility(var10);
         this.imageView.setImageResource(this.currentDrawable);
      }

      this.lastAvatar = (TLRPC.FileLocation)var13;
      if (var11 != null) {
         this.avatarImageView.setImage((ImageLocation)ImageLocation.getForUser(var11, false), "50_50", (Drawable)this.avatarDrawable, (Object)var11);
      } else if (var4 != null) {
         this.avatarImageView.setImage((ImageLocation)ImageLocation.getForChat(var4, false), "50_50", (Drawable)this.avatarDrawable, (Object)var4);
      }

   }
}
