package org.telegram.ui.Cells2;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox;
import org.telegram.ui.Components.CheckBoxSquare;
import org.telegram.ui.Components.LayoutHelper;

public class UserCell extends FrameLayout {
   private AvatarDrawable avatarDrawable;
   private BackupImageView avatarImageView;
   private CheckBox checkBox;
   private CheckBoxSquare checkBoxBig;
   private int currentAccount;
   private int currentDrawable;
   private int currentId;
   private CharSequence currentName;
   private TLObject currentObject;
   private CharSequence currrntStatus;
   private ImageView imageView;
   private TLRPC.FileLocation lastAvatar;
   private String lastName;
   private int lastStatus;
   private SimpleTextView nameTextView;
   private int statusColor;
   private int statusOnlineColor;
   private SimpleTextView statusTextView;

   public UserCell(Context var1, int var2, int var3) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.statusColor = Theme.getColor("windowBackgroundWhiteGrayText");
      this.statusOnlineColor = Theme.getColor("windowBackgroundWhiteBlueText");
      this.avatarDrawable = new AvatarDrawable();
      this.avatarImageView = new BackupImageView(var1);
      this.avatarImageView.setRoundRadius(AndroidUtilities.dp(24.0F));
      BackupImageView var4 = this.avatarImageView;
      boolean var5 = LocaleController.isRTL;
      byte var6 = 5;
      byte var7;
      if (var5) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      float var8;
      if (LocaleController.isRTL) {
         var8 = 0.0F;
      } else {
         var8 = (float)(var2 + 7);
      }

      float var9;
      if (LocaleController.isRTL) {
         var9 = (float)(var2 + 7);
      } else {
         var9 = 0.0F;
      }

      this.addView(var4, LayoutHelper.createFrame(48, 48.0F, var7 | 48, var8, 11.0F, var9, 0.0F));
      this.nameTextView = new SimpleTextView(var1);
      this.nameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.nameTextView.setTextSize(17);
      SimpleTextView var14 = this.nameTextView;
      if (LocaleController.isRTL) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      var14.setGravity(var7 | 48);
      var14 = this.nameTextView;
      if (LocaleController.isRTL) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      var5 = LocaleController.isRTL;
      byte var10 = 18;
      byte var11;
      int var16;
      if (var5) {
         if (var3 == 2) {
            var11 = 18;
         } else {
            var11 = 0;
         }

         var16 = var11 + 28;
      } else {
         var16 = var2 + 68;
      }

      var8 = (float)var16;
      if (LocaleController.isRTL) {
         var16 = var2 + 68;
      } else {
         if (var3 == 2) {
            var11 = var10;
         } else {
            var11 = 0;
         }

         var16 = var11 + 28;
      }

      var9 = (float)var16;
      this.addView(var14, LayoutHelper.createFrame(-1, 20.0F, var7 | 48, var8, 14.5F, var9, 0.0F));
      this.statusTextView = new SimpleTextView(var1);
      this.statusTextView.setTextSize(14);
      var14 = this.statusTextView;
      if (LocaleController.isRTL) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      var14.setGravity(var7 | 48);
      var14 = this.statusTextView;
      if (LocaleController.isRTL) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      if (LocaleController.isRTL) {
         var8 = 28.0F;
      } else {
         var8 = (float)(var2 + 68);
      }

      if (LocaleController.isRTL) {
         var9 = (float)(var2 + 68);
      } else {
         var9 = 28.0F;
      }

      this.addView(var14, LayoutHelper.createFrame(-1, 20.0F, var7 | 48, var8, 37.5F, var9, 0.0F));
      this.imageView = new ImageView(var1);
      this.imageView.setScaleType(ScaleType.CENTER);
      this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), Mode.MULTIPLY));
      this.imageView.setVisibility(8);
      ImageView var15 = this.imageView;
      if (LocaleController.isRTL) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      if (LocaleController.isRTL) {
         var8 = 0.0F;
      } else {
         var8 = 16.0F;
      }

      if (LocaleController.isRTL) {
         var9 = 16.0F;
      } else {
         var9 = 0.0F;
      }

      this.addView(var15, LayoutHelper.createFrame(-2, -2.0F, var7 | 16, var8, 0.0F, var9, 0.0F));
      if (var3 == 2) {
         this.checkBoxBig = new CheckBoxSquare(var1, false);
         CheckBoxSquare var12 = this.checkBoxBig;
         if (LocaleController.isRTL) {
            var6 = 3;
         }

         if (LocaleController.isRTL) {
            var8 = 19.0F;
         } else {
            var8 = 0.0F;
         }

         if (LocaleController.isRTL) {
            var9 = 0.0F;
         } else {
            var9 = 19.0F;
         }

         this.addView(var12, LayoutHelper.createFrame(18, 18.0F, var6 | 16, var8, 0.0F, var9, 0.0F));
      } else if (var3 == 1) {
         this.checkBox = new CheckBox(var1, 2131165802);
         this.checkBox.setVisibility(4);
         this.checkBox.setColor(Theme.getColor("checkbox"), Theme.getColor("checkboxCheck"));
         CheckBox var13 = this.checkBox;
         if (!LocaleController.isRTL) {
            var6 = 3;
         }

         if (LocaleController.isRTL) {
            var8 = 0.0F;
         } else {
            var8 = (float)(var2 + 37);
         }

         if (LocaleController.isRTL) {
            var9 = (float)(var2 + 37);
         } else {
            var9 = 0.0F;
         }

         this.addView(var13, LayoutHelper.createFrame(22, 22.0F, var6 | 48, var8, 41.0F, var9, 0.0F));
      }

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

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(70.0F), 1073741824));
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
      if (var1 == null && var2 == null && var3 == null) {
         this.currrntStatus = null;
         this.currentName = null;
         this.currentObject = null;
         this.nameTextView.setText("");
         this.statusTextView.setText("");
         this.avatarImageView.setImageDrawable((Drawable)null);
      } else {
         this.currrntStatus = var3;
         this.currentName = var2;
         this.currentObject = var1;
         this.currentDrawable = var4;
         this.update(0);
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
      Object var4;
      TLRPC.User var11;
      TLRPC.Chat var13;
      label196: {
         TLObject var2 = this.currentObject;
         if (var2 instanceof TLRPC.User) {
            var11 = (TLRPC.User)var2;
            TLRPC.UserProfilePhoto var3 = var11.photo;
            if (var3 != null) {
               var4 = var3.photo_small;
               var13 = null;
               break label196;
            }
         } else {
            if (var2 instanceof TLRPC.Chat) {
               var13 = (TLRPC.Chat)var2;
               TLRPC.ChatPhoto var12 = var13.photo;
               if (var12 != null) {
                  var4 = var12.photo_small;
                  var11 = null;
               } else {
                  var11 = null;
                  var4 = var11;
               }
               break label196;
            }

            var11 = null;
         }

         var4 = null;
         var13 = null;
      }

      String var17;
      if (var1 != 0) {
         boolean var6;
         label187: {
            label186: {
               if ((var1 & 2) != 0) {
                  if (this.lastAvatar != null && var4 == null) {
                     break label186;
                  }

                  TLRPC.FileLocation var5 = this.lastAvatar;
                  if (var5 == null && var4 != null && var5 != null && var4 != null && (var5.volume_id != ((TLRPC.FileLocation)var4).volume_id || var5.local_id != ((TLRPC.FileLocation)var4).local_id)) {
                     break label186;
                  }
               }

               var6 = false;
               break label187;
            }

            var6 = true;
         }

         boolean var7 = var6;
         if (var11 != null) {
            var7 = var6;
            if (!var6) {
               var7 = var6;
               if ((var1 & 4) != 0) {
                  TLRPC.UserStatus var15 = var11.status;
                  int var8;
                  if (var15 != null) {
                     var8 = var15.expires;
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

         String var9;
         if (!var7 && this.currentName == null && this.lastName != null && (var1 & 1) != 0) {
            if (var11 != null) {
               var17 = UserObject.getUserName(var11);
            } else {
               var17 = var13.title;
            }

            var9 = var17;
            if (!var17.equals(this.lastName)) {
               var7 = true;
               var9 = var17;
            }
         } else {
            var9 = null;
         }

         var17 = var9;
         if (!var7) {
            return;
         }
      } else {
         var17 = null;
      }

      this.lastAvatar = (TLRPC.FileLocation)var4;
      CharSequence var19;
      if (var11 != null) {
         this.avatarDrawable.setInfo(var11);
         TLRPC.UserStatus var18 = var11.status;
         if (var18 != null) {
            this.lastStatus = var18.expires;
         } else {
            this.lastStatus = 0;
         }
      } else if (var13 != null) {
         this.avatarDrawable.setInfo(var13);
      } else {
         var19 = this.currentName;
         if (var19 != null) {
            this.avatarDrawable.setInfo(this.currentId, var19.toString(), (String)null, false);
         } else {
            this.avatarDrawable.setInfo(this.currentId, "#", (String)null, false);
         }
      }

      var19 = this.currentName;
      if (var19 != null) {
         this.lastName = null;
         this.nameTextView.setText(var19);
      } else {
         String var21;
         if (var11 != null) {
            var21 = var17;
            if (var17 == null) {
               var21 = UserObject.getUserName(var11);
            }

            this.lastName = var21;
         } else {
            var21 = var17;
            if (var17 == null) {
               var21 = var13.title;
            }

            this.lastName = var21;
         }

         this.nameTextView.setText(this.lastName);
      }

      if (this.currrntStatus != null) {
         this.statusTextView.setTextColor(this.statusColor);
         this.statusTextView.setText(this.currrntStatus);
      } else if (var11 != null) {
         if (var11.bot) {
            this.statusTextView.setTextColor(this.statusColor);
            if (var11.bot_chat_history) {
               this.statusTextView.setText(LocaleController.getString("BotStatusRead", 2131558859));
            } else {
               this.statusTextView.setText(LocaleController.getString("BotStatusCantRead", 2131558858));
            }
         } else {
            label203: {
               if (var11.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                  TLRPC.UserStatus var16 = var11.status;
                  if ((var16 == null || var16.expires <= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) && !MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(var11.id)) {
                     this.statusTextView.setTextColor(this.statusColor);
                     this.statusTextView.setText(LocaleController.formatUserStatus(this.currentAccount, var11));
                     break label203;
                  }
               }

               this.statusTextView.setTextColor(this.statusOnlineColor);
               this.statusTextView.setText(LocaleController.getString("Online", 2131560100));
            }
         }

         this.avatarImageView.setImage((ImageLocation)ImageLocation.getForUser(var11, false), "50_50", (Drawable)this.avatarDrawable, (Object)var11);
      } else if (var13 != null) {
         this.statusTextView.setTextColor(this.statusColor);
         if (ChatObject.isChannel(var13) && !var13.megagroup) {
            var1 = var13.participants_count;
            if (var1 != 0) {
               this.statusTextView.setText(LocaleController.formatPluralString("Subscribers", var1));
            } else if (TextUtils.isEmpty(var13.username)) {
               this.statusTextView.setText(LocaleController.getString("ChannelPrivate", 2131558988));
            } else {
               this.statusTextView.setText(LocaleController.getString("ChannelPublic", 2131558991));
            }
         } else {
            var1 = var13.participants_count;
            if (var1 != 0) {
               this.statusTextView.setText(LocaleController.formatPluralString("Members", var1));
            } else if (TextUtils.isEmpty(var13.username)) {
               this.statusTextView.setText(LocaleController.getString("MegaPrivate", 2131559831));
            } else {
               this.statusTextView.setText(LocaleController.getString("MegaPublic", 2131559834));
            }
         }

         this.avatarImageView.setImage((ImageLocation)ImageLocation.getForChat(var13, false), "50_50", (Drawable)this.avatarDrawable, (Object)this.currentObject);
      }

      int var20 = this.imageView.getVisibility();
      byte var10 = 8;
      if (var20 == 0 && this.currentDrawable == 0 || this.imageView.getVisibility() == 8 && this.currentDrawable != 0) {
         ImageView var14 = this.imageView;
         if (this.currentDrawable != 0) {
            var10 = 0;
         }

         var14.setVisibility(var10);
         this.imageView.setImageResource(this.currentDrawable);
      }

   }
}
