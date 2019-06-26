package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
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
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.LayoutHelper;

public class GroupCreateUserCell extends FrameLayout {
   private AvatarDrawable avatarDrawable;
   private BackupImageView avatarImageView;
   private CheckBox2 checkBox;
   private int currentAccount;
   private CharSequence currentName;
   private TLObject currentObject;
   private CharSequence currentStatus;
   private TLRPC.FileLocation lastAvatar;
   private String lastName;
   private int lastStatus;
   private SimpleTextView nameTextView;
   private SimpleTextView statusTextView;

   public GroupCreateUserCell(Context var1, boolean var2, int var3) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
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
         var8 = (float)(var3 + 13);
      }

      float var9;
      if (LocaleController.isRTL) {
         var9 = (float)(var3 + 13);
      } else {
         var9 = 0.0F;
      }

      this.addView(var4, LayoutHelper.createFrame(46, 46.0F, var7 | 48, var8, 6.0F, var9, 0.0F));
      this.nameTextView = new SimpleTextView(var1);
      this.nameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.nameTextView.setTextSize(16);
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
      byte var10 = 28;
      byte var11;
      if (var5) {
         var11 = 28;
      } else {
         var11 = 72;
      }

      var8 = (float)(var11 + var3);
      if (LocaleController.isRTL) {
         var11 = 72;
      } else {
         var11 = 28;
      }

      this.addView(var14, LayoutHelper.createFrame(-1, 20.0F, var7 | 48, var8, 10.0F, (float)(var11 + var3), 0.0F));
      this.statusTextView = new SimpleTextView(var1);
      this.statusTextView.setTextSize(15);
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
         var11 = 28;
      } else {
         var11 = 72;
      }

      var8 = (float)(var11 + var3);
      var11 = var10;
      if (LocaleController.isRTL) {
         var11 = 72;
      }

      this.addView(var14, LayoutHelper.createFrame(-1, 20.0F, var7 | 48, var8, 32.0F, (float)(var11 + var3), 0.0F));
      if (var2) {
         this.checkBox = new CheckBox2(var1);
         this.checkBox.setColor((String)null, "windowBackgroundWhite", "checkboxCheck");
         this.checkBox.setSize(21);
         this.checkBox.setDrawUnchecked(false);
         this.checkBox.setDrawBackgroundAsArc(3);
         CheckBox2 var12 = this.checkBox;
         byte var13;
         if (LocaleController.isRTL) {
            var13 = var6;
         } else {
            var13 = 3;
         }

         if (LocaleController.isRTL) {
            var8 = 0.0F;
         } else {
            var8 = 40.0F;
         }

         if (LocaleController.isRTL) {
            var9 = 39.0F;
         } else {
            var9 = 0.0F;
         }

         this.addView(var12, LayoutHelper.createFrame(24, 24.0F, var13 | 48, var8, 33.0F, var9, 0.0F));
      }

   }

   public TLObject getObject() {
      return this.currentObject;
   }

   public boolean hasOverlappingRendering() {
      return false;
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(58.0F), 1073741824));
   }

   public void recycle() {
      this.avatarImageView.getImageReceiver().cancelLoadImage();
   }

   public void setCheckBoxEnabled(boolean var1) {
      this.checkBox.setEnabled(var1);
   }

   public void setChecked(boolean var1, boolean var2) {
      this.checkBox.setChecked(var1, var2);
   }

   public void setObject(TLObject var1, CharSequence var2, CharSequence var3) {
      this.currentObject = var1;
      this.currentStatus = var3;
      this.currentName = var2;
      this.update(0);
   }

   public void update(int var1) {
      TLObject var2 = this.currentObject;
      if (var2 != null) {
         TLRPC.FileLocation var4;
         boolean var5;
         TLRPC.FileLocation var10;
         String var12;
         String var14;
         CharSequence var16;
         if (var2 instanceof TLRPC.User) {
            TLRPC.User var3 = (TLRPC.User)var2;
            TLRPC.UserProfilePhoto var9 = var3.photo;
            if (var9 != null) {
               var10 = var9.photo_small;
            } else {
               var10 = null;
            }

            TLRPC.UserStatus var13;
            if (var1 == 0) {
               var14 = null;
            } else {
               label197: {
                  label196: {
                     if ((var1 & 2) != 0) {
                        if (this.lastAvatar != null && var10 == null || this.lastAvatar == null && var10 != null) {
                           break label196;
                        }

                        var4 = this.lastAvatar;
                        if (var4 != null && var10 != null && (var4.volume_id != var10.volume_id || var4.local_id != var10.local_id)) {
                           break label196;
                        }
                     }

                     var5 = false;
                     break label197;
                  }

                  var5 = true;
               }

               boolean var6 = var5;
               if (var3 != null) {
                  var6 = var5;
                  if (this.currentStatus == null) {
                     var6 = var5;
                     if (!var5) {
                        var6 = var5;
                        if ((var1 & 4) != 0) {
                           var13 = var3.status;
                           int var7;
                           if (var13 != null) {
                              var7 = var13.expires;
                           } else {
                              var7 = 0;
                           }

                           var6 = var5;
                           if (var7 != this.lastStatus) {
                              var6 = true;
                           }
                        }
                     }
                  }
               }

               if (!var6 && this.currentName == null && this.lastName != null && (var1 & 1) != 0) {
                  var12 = UserObject.getUserName(var3);
                  var14 = var12;
                  if (!var12.equals(this.lastName)) {
                     var6 = true;
                     var14 = var12;
                  }
               } else {
                  var14 = null;
               }

               if (!var6) {
                  return;
               }
            }

            this.avatarDrawable.setInfo(var3);
            TLRPC.UserStatus var15 = var3.status;
            if (var15 != null) {
               var1 = var15.expires;
            } else {
               var1 = 0;
            }

            this.lastStatus = var1;
            var16 = this.currentName;
            if (var16 != null) {
               this.lastName = null;
               this.nameTextView.setText(var16, true);
            } else {
               var12 = var14;
               if (var14 == null) {
                  var12 = UserObject.getUserName(var3);
               }

               this.lastName = var12;
               this.nameTextView.setText(this.lastName);
            }

            if (this.currentStatus == null) {
               if (var3.bot) {
                  this.statusTextView.setTag("windowBackgroundWhiteGrayText");
                  this.statusTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
                  this.statusTextView.setText(LocaleController.getString("Bot", 2131558848));
               } else {
                  label216: {
                     if (var3.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                        var13 = var3.status;
                        if ((var13 == null || var13.expires <= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) && !MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(var3.id)) {
                           this.statusTextView.setTag("windowBackgroundWhiteGrayText");
                           this.statusTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
                           this.statusTextView.setText(LocaleController.formatUserStatus(this.currentAccount, var3));
                           break label216;
                        }
                     }

                     this.statusTextView.setTag("windowBackgroundWhiteBlueText");
                     this.statusTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText"));
                     this.statusTextView.setText(LocaleController.getString("Online", 2131560100));
                  }
               }
            }

            this.avatarImageView.setImage((ImageLocation)ImageLocation.getForUser(var3, false), "50_50", (Drawable)this.avatarDrawable, (Object)var3);
         } else {
            TLRPC.Chat var11 = (TLRPC.Chat)var2;
            TLRPC.ChatPhoto var17 = var11.photo;
            if (var17 != null) {
               var10 = var17.photo_small;
            } else {
               var10 = null;
            }

            if (var1 == 0) {
               var14 = null;
            } else {
               label153: {
                  label152: {
                     if ((var1 & 2) != 0) {
                        if (this.lastAvatar != null && var10 == null || this.lastAvatar == null && var10 != null) {
                           break label152;
                        }

                        var4 = this.lastAvatar;
                        if (var4 != null && var10 != null && (var4.volume_id != var10.volume_id || var4.local_id != var10.local_id)) {
                           break label152;
                        }
                     }

                     var5 = false;
                     break label153;
                  }

                  var5 = true;
               }

               label136: {
                  if (!var5 && this.currentName == null) {
                     String var8 = this.lastName;
                     if (var8 != null && (var1 & 1) != 0) {
                        var12 = var11.title;
                        var14 = var12;
                        if (!var12.equals(var8)) {
                           var5 = true;
                           var14 = var12;
                        }
                        break label136;
                     }
                  }

                  var14 = null;
               }

               if (!var5) {
                  return;
               }
            }

            this.avatarDrawable.setInfo(var11);
            var16 = this.currentName;
            if (var16 != null) {
               this.lastName = null;
               this.nameTextView.setText(var16, true);
            } else {
               var12 = var14;
               if (var14 == null) {
                  var12 = var11.title;
               }

               this.lastName = var12;
               this.nameTextView.setText(this.lastName);
            }

            if (this.currentStatus == null) {
               this.statusTextView.setTag("windowBackgroundWhiteGrayText");
               this.statusTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
               var1 = var11.participants_count;
               if (var1 != 0) {
                  this.statusTextView.setText(LocaleController.formatPluralString("Members", var1));
               } else if (TextUtils.isEmpty(var11.username)) {
                  this.statusTextView.setText(LocaleController.getString("MegaPrivate", 2131559831));
               } else {
                  this.statusTextView.setText(LocaleController.getString("MegaPublic", 2131559834));
               }
            }

            this.avatarImageView.setImage((ImageLocation)ImageLocation.getForChat(var11, false), "50_50", (Drawable)this.avatarDrawable, (Object)var11);
         }

         CharSequence var18 = this.currentStatus;
         if (var18 != null) {
            this.statusTextView.setText(var18, true);
            this.statusTextView.setTag("windowBackgroundWhiteGrayText");
            this.statusTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
         }

      }
   }
}
