package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;

public class ManageChatUserCell extends FrameLayout {
   private AvatarDrawable avatarDrawable;
   private BackupImageView avatarImageView;
   private int currentAccount;
   private CharSequence currentName;
   private TLObject currentObject;
   private CharSequence currrntStatus;
   private ManageChatUserCell.ManageChatUserCellDelegate delegate;
   private boolean isAdmin;
   private TLRPC.FileLocation lastAvatar;
   private String lastName;
   private int lastStatus;
   private int namePadding;
   private SimpleTextView nameTextView;
   private boolean needDivider;
   private ImageView optionsButton;
   private int statusColor;
   private int statusOnlineColor;
   private SimpleTextView statusTextView;

   public ManageChatUserCell(Context var1, int var2, int var3, boolean var4) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.statusColor = Theme.getColor("windowBackgroundWhiteGrayText");
      this.statusOnlineColor = Theme.getColor("windowBackgroundWhiteBlueText");
      this.namePadding = var3;
      this.avatarDrawable = new AvatarDrawable();
      this.avatarImageView = new BackupImageView(var1);
      this.avatarImageView.setRoundRadius(AndroidUtilities.dp(23.0F));
      BackupImageView var5 = this.avatarImageView;
      boolean var6 = LocaleController.isRTL;
      byte var7 = 5;
      byte var12;
      if (var6) {
         var12 = 5;
      } else {
         var12 = 3;
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

      this.addView(var5, LayoutHelper.createFrame(46, 46.0F, var12 | 48, var8, 8.0F, var9, 0.0F));
      this.nameTextView = new SimpleTextView(var1);
      this.nameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.nameTextView.setTextSize(17);
      this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      SimpleTextView var13 = this.nameTextView;
      byte var11;
      if (LocaleController.isRTL) {
         var11 = 5;
      } else {
         var11 = 3;
      }

      var13.setGravity(var11 | 48);
      var13 = this.nameTextView;
      if (LocaleController.isRTL) {
         var11 = 5;
      } else {
         var11 = 3;
      }

      if (LocaleController.isRTL) {
         var8 = 46.0F;
      } else {
         var8 = (float)(this.namePadding + 68);
      }

      if (LocaleController.isRTL) {
         var9 = (float)(this.namePadding + 68);
      } else {
         var9 = 46.0F;
      }

      this.addView(var13, LayoutHelper.createFrame(-1, 20.0F, var11 | 48, var8, 11.5F, var9, 0.0F));
      this.statusTextView = new SimpleTextView(var1);
      this.statusTextView.setTextSize(14);
      var13 = this.statusTextView;
      if (LocaleController.isRTL) {
         var11 = 5;
      } else {
         var11 = 3;
      }

      var13.setGravity(var11 | 48);
      var13 = this.statusTextView;
      if (LocaleController.isRTL) {
         var11 = 5;
      } else {
         var11 = 3;
      }

      if (LocaleController.isRTL) {
         var8 = 28.0F;
      } else {
         var8 = (float)(this.namePadding + 68);
      }

      if (LocaleController.isRTL) {
         var9 = (float)(this.namePadding + 68);
      } else {
         var9 = 28.0F;
      }

      this.addView(var13, LayoutHelper.createFrame(-1, 20.0F, var11 | 48, var8, 34.5F, var9, 0.0F));
      if (var4) {
         this.optionsButton = new ImageView(var1);
         this.optionsButton.setFocusable(false);
         this.optionsButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("stickers_menuSelector")));
         this.optionsButton.setImageResource(2131165416);
         this.optionsButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("stickers_menu"), Mode.MULTIPLY));
         this.optionsButton.setScaleType(ScaleType.CENTER);
         ImageView var10 = this.optionsButton;
         var11 = var7;
         if (LocaleController.isRTL) {
            var11 = 3;
         }

         this.addView(var10, LayoutHelper.createFrame(52, 64, var11 | 48));
         this.optionsButton.setOnClickListener(new _$$Lambda$ManageChatUserCell$oJTkyKgCBYt9FR4kYNNgwqbXXuY(this));
         this.optionsButton.setContentDescription(LocaleController.getString("AccDescrUserOptions", 2131558480));
      }

   }

   public TLObject getCurrentObject() {
      return this.currentObject;
   }

   public boolean hasOverlappingRendering() {
      return false;
   }

   // $FF: synthetic method
   public void lambda$new$0$ManageChatUserCell(View var1) {
      this.delegate.onOptionsButtonCheck(this, true);
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

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0F) + this.needDivider, 1073741824));
   }

   public void recycle() {
      this.avatarImageView.getImageReceiver().cancelLoadImage();
   }

   public void setData(TLObject var1, CharSequence var2, CharSequence var3, boolean var4) {
      if (var1 == null) {
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
         if (this.optionsButton != null) {
            boolean var5 = this.delegate.onOptionsButtonCheck(this, false);
            ImageView var14 = this.optionsButton;
            byte var6;
            if (var5) {
               var6 = 0;
            } else {
               var6 = 4;
            }

            var14.setVisibility(var6);
            SimpleTextView var15 = this.nameTextView;
            boolean var7 = LocaleController.isRTL;
            byte var8 = 5;
            byte var9;
            if (var7) {
               var9 = 5;
            } else {
               var9 = 3;
            }

            var7 = LocaleController.isRTL;
            byte var10 = 46;
            int var16;
            if (var7) {
               if (var5) {
                  var16 = 46;
               } else {
                  var16 = 28;
               }
            } else {
               var16 = this.namePadding + 68;
            }

            float var11 = (float)var16;
            float var12;
            if (var3 != null && var3.length() <= 0) {
               var12 = 20.5F;
            } else {
               var12 = 11.5F;
            }

            if (LocaleController.isRTL) {
               var16 = this.namePadding + 68;
            } else if (var5) {
               var16 = 46;
            } else {
               var16 = 28;
            }

            float var13 = (float)var16;
            var15.setLayoutParams(LayoutHelper.createFrame(-1, 20.0F, var9 | 48, var11, var12, var13, 0.0F));
            var15 = this.statusTextView;
            if (LocaleController.isRTL) {
               var9 = var8;
            } else {
               var9 = 3;
            }

            if (LocaleController.isRTL) {
               if (var5) {
                  var16 = 46;
               } else {
                  var16 = 28;
               }
            } else {
               var16 = this.namePadding + 68;
            }

            var11 = (float)var16;
            if (LocaleController.isRTL) {
               var12 = (float)(this.namePadding + 68);
            } else {
               if (var5) {
                  var6 = var10;
               } else {
                  var6 = 28;
               }

               var12 = (float)var6;
            }

            var15.setLayoutParams(LayoutHelper.createFrame(-1, 20.0F, var9 | 48, var11, 34.5F, var12, 0.0F));
         }

         this.needDivider = var4;
         this.setWillNotDraw(this.needDivider ^ true);
         this.update(0);
      }
   }

   public void setDelegate(ManageChatUserCell.ManageChatUserCellDelegate var1) {
      this.delegate = var1;
   }

   public void setIsAdmin(boolean var1) {
      this.isAdmin = var1;
   }

   public void setStatusColors(int var1, int var2) {
      this.statusColor = var1;
      this.statusOnlineColor = var2;
   }

   public void update(int var1) {
      TLObject var2 = this.currentObject;
      if (var2 != null) {
         TLRPC.FileLocation var4;
         boolean var5;
         String var8;
         TLRPC.FileLocation var11;
         String var14;
         CharSequence var17;
         if (var2 instanceof TLRPC.User) {
            TLRPC.User var3 = (TLRPC.User)var2;
            TLRPC.UserProfilePhoto var10 = var3.photo;
            if (var10 != null) {
               var4 = var10.photo_small;
            } else {
               var4 = null;
            }

            TLRPC.UserStatus var12;
            if (var1 == 0) {
               var14 = null;
            } else {
               label202: {
                  label201: {
                     if ((var1 & 2) != 0) {
                        if (this.lastAvatar != null && var4 == null || this.lastAvatar == null && var4 != null) {
                           break label201;
                        }

                        var11 = this.lastAvatar;
                        if (var11 != null && var4 != null && (var11.volume_id != var4.volume_id || var11.local_id != var4.local_id)) {
                           break label201;
                        }
                     }

                     var5 = false;
                     break label202;
                  }

                  var5 = true;
               }

               boolean var6 = var5;
               if (var3 != null) {
                  var6 = var5;
                  if (!var5) {
                     var6 = var5;
                     if ((var1 & 4) != 0) {
                        var12 = var3.status;
                        int var7;
                        if (var12 != null) {
                           var7 = var12.expires;
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

               if (!var6 && this.currentName == null && this.lastName != null && (var1 & 1) != 0) {
                  var8 = UserObject.getUserName(var3);
                  var14 = var8;
                  if (!var8.equals(this.lastName)) {
                     var6 = true;
                     var14 = var8;
                  }
               } else {
                  var14 = null;
               }

               if (!var6) {
                  return;
               }
            }

            this.avatarDrawable.setInfo(var3);
            TLRPC.UserStatus var16 = var3.status;
            if (var16 != null) {
               this.lastStatus = var16.expires;
            } else {
               this.lastStatus = 0;
            }

            var17 = this.currentName;
            if (var17 != null) {
               this.lastName = null;
               this.nameTextView.setText(var17);
            } else {
               var8 = var14;
               if (var14 == null) {
                  var8 = UserObject.getUserName(var3);
               }

               this.lastName = var8;
               this.nameTextView.setText(this.lastName);
            }

            if (this.currrntStatus != null) {
               this.statusTextView.setTextColor(this.statusColor);
               this.statusTextView.setText(this.currrntStatus);
            } else if (var3.bot) {
               this.statusTextView.setTextColor(this.statusColor);
               if (!var3.bot_chat_history && !this.isAdmin) {
                  this.statusTextView.setText(LocaleController.getString("BotStatusCantRead", 2131558858));
               } else {
                  this.statusTextView.setText(LocaleController.getString("BotStatusRead", 2131558859));
               }
            } else {
               label221: {
                  if (var3.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                     var12 = var3.status;
                     if ((var12 == null || var12.expires <= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) && !MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(var3.id)) {
                        this.statusTextView.setTextColor(this.statusColor);
                        this.statusTextView.setText(LocaleController.formatUserStatus(this.currentAccount, var3));
                        break label221;
                     }
                  }

                  this.statusTextView.setTextColor(this.statusOnlineColor);
                  this.statusTextView.setText(LocaleController.getString("Online", 2131560100));
               }
            }

            this.lastAvatar = var4;
            this.avatarImageView.setImage((ImageLocation)ImageLocation.getForUser(var3, false), "50_50", (Drawable)this.avatarDrawable, (Object)var3);
         } else if (var2 instanceof TLRPC.Chat) {
            TLRPC.Chat var13 = (TLRPC.Chat)var2;
            TLRPC.ChatPhoto var15 = var13.photo;
            if (var15 != null) {
               var4 = var15.photo_small;
            } else {
               var4 = null;
            }

            if (var1 == 0) {
               var14 = null;
            } else {
               label154: {
                  label153: {
                     if ((var1 & 2) != 0) {
                        if (this.lastAvatar != null && var4 == null || this.lastAvatar == null && var4 != null) {
                           break label153;
                        }

                        var11 = this.lastAvatar;
                        if (var11 != null && var4 != null && (var11.volume_id != var4.volume_id || var11.local_id != var4.local_id)) {
                           break label153;
                        }
                     }

                     var5 = false;
                     break label154;
                  }

                  var5 = true;
               }

               label137: {
                  if (!var5 && this.currentName == null) {
                     String var9 = this.lastName;
                     if (var9 != null && (var1 & 1) != 0) {
                        var8 = var13.title;
                        var14 = var8;
                        if (!var8.equals(var9)) {
                           var5 = true;
                           var14 = var8;
                        }
                        break label137;
                     }
                  }

                  var14 = null;
               }

               if (!var5) {
                  return;
               }
            }

            this.avatarDrawable.setInfo(var13);
            var17 = this.currentName;
            if (var17 != null) {
               this.lastName = null;
               this.nameTextView.setText(var17);
            } else {
               var8 = var14;
               if (var14 == null) {
                  var8 = var13.title;
               }

               this.lastName = var8;
               this.nameTextView.setText(this.lastName);
            }

            if (this.currrntStatus != null) {
               this.statusTextView.setTextColor(this.statusColor);
               this.statusTextView.setText(this.currrntStatus);
            } else {
               this.statusTextView.setTextColor(this.statusColor);
               var1 = var13.participants_count;
               if (var1 != 0) {
                  this.statusTextView.setText(LocaleController.formatPluralString("Members", var1));
               } else if (TextUtils.isEmpty(var13.username)) {
                  this.statusTextView.setText(LocaleController.getString("MegaPrivate", 2131559831));
               } else {
                  this.statusTextView.setText(LocaleController.getString("MegaPublic", 2131559834));
               }
            }

            this.lastAvatar = var4;
            this.avatarImageView.setImage((ImageLocation)ImageLocation.getForChat(var13, false), "50_50", (Drawable)this.avatarDrawable, (Object)var13);
         }

      }
   }

   public interface ManageChatUserCellDelegate {
      boolean onOptionsButtonCheck(ManageChatUserCell var1, boolean var2);
   }
}
