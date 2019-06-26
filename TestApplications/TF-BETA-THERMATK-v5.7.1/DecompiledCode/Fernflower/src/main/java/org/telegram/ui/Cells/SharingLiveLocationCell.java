package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.text.TextUtils;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import org.osmdroid.util.GeoPoint;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.LocationActivity;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.LayoutHelper;

public class SharingLiveLocationCell extends FrameLayout {
   private AvatarDrawable avatarDrawable;
   private BackupImageView avatarImageView;
   private int currentAccount;
   private LocationController.SharingLocationInfo currentInfo;
   private SimpleTextView distanceTextView;
   private Runnable invalidateRunnable = new Runnable() {
      public void run() {
         SharingLiveLocationCell var1 = SharingLiveLocationCell.this;
         var1.invalidate((int)var1.rect.left - 5, (int)SharingLiveLocationCell.this.rect.top - 5, (int)SharingLiveLocationCell.this.rect.right + 5, (int)SharingLiveLocationCell.this.rect.bottom + 5);
         AndroidUtilities.runOnUIThread(SharingLiveLocationCell.this.invalidateRunnable, 1000L);
      }
   };
   private LocationActivity.LiveLocation liveLocation;
   private Location location = new Location("network");
   private SimpleTextView nameTextView;
   private RectF rect = new RectF();

   public SharingLiveLocationCell(Context var1, boolean var2) {
      super(var1);
      this.avatarImageView = new BackupImageView(var1);
      this.avatarImageView.setRoundRadius(AndroidUtilities.dp(20.0F));
      this.avatarDrawable = new AvatarDrawable();
      this.nameTextView = new SimpleTextView(var1);
      this.nameTextView.setTextSize(16);
      this.nameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      SimpleTextView var3 = this.nameTextView;
      boolean var4 = LocaleController.isRTL;
      byte var5 = 5;
      byte var6;
      if (var4) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      var3.setGravity(var6);
      float var7;
      float var8;
      SimpleTextView var9;
      if (var2) {
         BackupImageView var11 = this.avatarImageView;
         if (LocaleController.isRTL) {
            var6 = 5;
         } else {
            var6 = 3;
         }

         if (LocaleController.isRTL) {
            var7 = 0.0F;
         } else {
            var7 = 17.0F;
         }

         if (LocaleController.isRTL) {
            var8 = 17.0F;
         } else {
            var8 = 0.0F;
         }

         this.addView(var11, LayoutHelper.createFrame(40, 40.0F, var6 | 48, var7, 13.0F, var8, 0.0F));
         var3 = this.nameTextView;
         if (LocaleController.isRTL) {
            var6 = 5;
         } else {
            var6 = 3;
         }

         if (LocaleController.isRTL) {
            var7 = 54.0F;
         } else {
            var7 = 73.0F;
         }

         if (LocaleController.isRTL) {
            var8 = 73.0F;
         } else {
            var8 = 54.0F;
         }

         this.addView(var3, LayoutHelper.createFrame(-1, 20.0F, var6 | 48, var7, 12.0F, var8, 0.0F));
         this.distanceTextView = new SimpleTextView(var1);
         this.distanceTextView.setTextSize(14);
         this.distanceTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
         var9 = this.distanceTextView;
         if (LocaleController.isRTL) {
            var6 = 5;
         } else {
            var6 = 3;
         }

         var9.setGravity(var6);
         var9 = this.distanceTextView;
         if (!LocaleController.isRTL) {
            var5 = 3;
         }

         if (LocaleController.isRTL) {
            var7 = 54.0F;
         } else {
            var7 = 73.0F;
         }

         if (LocaleController.isRTL) {
            var8 = 73.0F;
         } else {
            var8 = 54.0F;
         }

         this.addView(var9, LayoutHelper.createFrame(-1, 20.0F, var5 | 48, var7, 37.0F, var8, 0.0F));
      } else {
         BackupImageView var10 = this.avatarImageView;
         if (LocaleController.isRTL) {
            var6 = 5;
         } else {
            var6 = 3;
         }

         if (LocaleController.isRTL) {
            var7 = 0.0F;
         } else {
            var7 = 17.0F;
         }

         if (LocaleController.isRTL) {
            var8 = 17.0F;
         } else {
            var8 = 0.0F;
         }

         this.addView(var10, LayoutHelper.createFrame(40, 40.0F, var6 | 48, var7, 7.0F, var8, 0.0F));
         var9 = this.nameTextView;
         if (!LocaleController.isRTL) {
            var5 = 3;
         }

         if (LocaleController.isRTL) {
            var7 = 54.0F;
         } else {
            var7 = 74.0F;
         }

         if (LocaleController.isRTL) {
            var8 = 74.0F;
         } else {
            var8 = 54.0F;
         }

         this.addView(var9, LayoutHelper.createFrame(-2, -2.0F, var5 | 48, var7, 17.0F, var8, 0.0F));
      }

      this.setWillNotDraw(false);
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      AndroidUtilities.runOnUIThread(this.invalidateRunnable);
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      AndroidUtilities.cancelRunOnUIThread(this.invalidateRunnable);
   }

   protected void onDraw(Canvas var1) {
      if (this.currentInfo != null || this.liveLocation != null) {
         LocationController.SharingLocationInfo var2 = this.currentInfo;
         int var3;
         int var4;
         if (var2 != null) {
            var3 = var2.stopTime;
            var4 = var2.period;
         } else {
            TLRPC.Message var12 = this.liveLocation.object;
            var3 = var12.date;
            var4 = var12.media.period;
            var3 += var4;
         }

         int var5 = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
         if (var3 >= var5) {
            var3 -= var5;
            float var6 = (float)Math.abs(var3) / (float)var4;
            boolean var7 = LocaleController.isRTL;
            float var8 = 48.0F;
            float var9 = 18.0F;
            float var10;
            float var11;
            RectF var13;
            if (var7) {
               var13 = this.rect;
               var10 = (float)AndroidUtilities.dp(13.0F);
               if (this.distanceTextView == null) {
                  var9 = 12.0F;
               }

               var11 = (float)AndroidUtilities.dp(var9);
               var9 = (float)AndroidUtilities.dp(43.0F);
               if (this.distanceTextView == null) {
                  var8 = 42.0F;
               }

               var13.set(var10, var11, var9, (float)AndroidUtilities.dp(var8));
            } else {
               var13 = this.rect;
               var10 = (float)(this.getMeasuredWidth() - AndroidUtilities.dp(43.0F));
               if (this.distanceTextView == null) {
                  var9 = 12.0F;
               }

               var9 = (float)AndroidUtilities.dp(var9);
               var11 = (float)(this.getMeasuredWidth() - AndroidUtilities.dp(13.0F));
               if (this.distanceTextView == null) {
                  var8 = 42.0F;
               }

               var13.set(var10, var9, var11, (float)AndroidUtilities.dp(var8));
            }

            if (this.distanceTextView == null) {
               var4 = Theme.getColor("dialog_liveLocationProgress");
            } else {
               var4 = Theme.getColor("location_liveLocationProgress");
            }

            Theme.chat_radialProgress2Paint.setColor(var4);
            Theme.chat_livePaint.setColor(var4);
            var1.drawArc(this.rect, -90.0F, var6 * -360.0F, false, Theme.chat_radialProgress2Paint);
            String var14 = LocaleController.formatLocationLeftTime(var3);
            var8 = Theme.chat_livePaint.measureText(var14);
            var9 = this.rect.centerX();
            var6 = var8 / 2.0F;
            if (this.distanceTextView != null) {
               var8 = 37.0F;
            } else {
               var8 = 31.0F;
            }

            var1.drawText(var14, var9 - var6, (float)AndroidUtilities.dp(var8), Theme.chat_livePaint);
         }
      }
   }

   protected void onMeasure(int var1, int var2) {
      var1 = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824);
      float var3;
      if (this.distanceTextView != null) {
         var3 = 66.0F;
      } else {
         var3 = 54.0F;
      }

      super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(var3), 1073741824));
   }

   public void setDialog(LocationController.SharingLocationInfo var1) {
      this.currentInfo = var1;
      int var2 = (int)var1.did;
      if (var2 > 0) {
         TLRPC.User var3 = MessagesController.getInstance(this.currentAccount).getUser(var2);
         if (var3 != null) {
            this.avatarDrawable.setInfo(var3);
            this.nameTextView.setText(ContactsController.formatName(var3.first_name, var3.last_name));
            this.avatarImageView.setImage((ImageLocation)ImageLocation.getForUser(var3, false), "50_50", (Drawable)this.avatarDrawable, (Object)var3);
         }
      } else {
         TLRPC.Chat var4 = MessagesController.getInstance(this.currentAccount).getChat(-var2);
         if (var4 != null) {
            this.avatarDrawable.setInfo(var4);
            this.nameTextView.setText(var4.title);
            this.avatarImageView.setImage((ImageLocation)ImageLocation.getForChat(var4, false), "50_50", (Drawable)this.avatarDrawable, (Object)var4);
         }
      }

   }

   public void setDialog(MessageObject var1, Location var2) {
      int var3 = var1.messageOwner.from_id;
      if (var1.isForwarded()) {
         TLRPC.MessageFwdHeader var4 = var1.messageOwner.fwd_from;
         var3 = var4.channel_id;
         if (var3 != 0) {
            var3 = -var3;
         } else {
            var3 = var4.from_id;
         }
      }

      this.currentAccount = var1.currentAccount;
      String var5;
      if (!TextUtils.isEmpty(var1.messageOwner.media.address)) {
         var5 = var1.messageOwner.media.address;
      } else {
         var5 = null;
      }

      String var8;
      if (!TextUtils.isEmpty(var1.messageOwner.media.title)) {
         var8 = var1.messageOwner.media.title;
         Drawable var6 = this.getResources().getDrawable(2131165762);
         var6.setColorFilter(new PorterDuffColorFilter(Theme.getColor("location_sendLocationIcon"), Mode.MULTIPLY));
         var3 = Theme.getColor("location_placeLocationBackground");
         CombinedDrawable var9 = new CombinedDrawable(Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(40.0F), var3, var3), var6);
         var9.setCustomSize(AndroidUtilities.dp(40.0F), AndroidUtilities.dp(40.0F));
         var9.setIconSize(AndroidUtilities.dp(24.0F), AndroidUtilities.dp(24.0F));
         this.avatarImageView.setImageDrawable(var9);
      } else {
         var8 = "";
         this.avatarDrawable = null;
         if (var3 > 0) {
            TLRPC.User var10 = MessagesController.getInstance(this.currentAccount).getUser(var3);
            if (var10 != null) {
               this.avatarDrawable = new AvatarDrawable(var10);
               var8 = UserObject.getUserName(var10);
               this.avatarImageView.setImage((ImageLocation)ImageLocation.getForUser(var10, false), "50_50", (Drawable)this.avatarDrawable, (Object)var10);
            }
         } else {
            TLRPC.Chat var11 = MessagesController.getInstance(this.currentAccount).getChat(-var3);
            if (var11 != null) {
               this.avatarDrawable = new AvatarDrawable(var11);
               var8 = var11.title;
               this.avatarImageView.setImage((ImageLocation)ImageLocation.getForChat(var11, false), "50_50", (Drawable)this.avatarDrawable, (Object)var11);
            }
         }
      }

      this.nameTextView.setText(var8);
      this.location.setLatitude(var1.messageOwner.media.geo.lat);
      this.location.setLongitude(var1.messageOwner.media.geo._long);
      if (var2 != null) {
         float var7 = this.location.distanceTo(var2);
         if (var5 != null) {
            if (var7 < 1000.0F) {
               this.distanceTextView.setText(String.format("%s - %d %s", var5, (int)var7, LocaleController.getString("MetersAway", 2131559857)));
            } else {
               this.distanceTextView.setText(String.format("%s - %.2f %s", var5, var7 / 1000.0F, LocaleController.getString("KMetersAway", 2131559709)));
            }
         } else if (var7 < 1000.0F) {
            this.distanceTextView.setText(String.format("%d %s", (int)var7, LocaleController.getString("MetersAway", 2131559857)));
         } else {
            this.distanceTextView.setText(String.format("%.2f %s", var7 / 1000.0F, LocaleController.getString("KMetersAway", 2131559709)));
         }
      } else if (var5 != null) {
         this.distanceTextView.setText(var5);
      } else {
         this.distanceTextView.setText(LocaleController.getString("Loading", 2131559768));
      }

   }

   public void setDialog(LocationActivity.LiveLocation var1, Location var2) {
      this.liveLocation = var1;
      int var3 = var1.id;
      if (var3 > 0) {
         TLRPC.User var4 = MessagesController.getInstance(this.currentAccount).getUser(var3);
         if (var4 != null) {
            this.avatarDrawable.setInfo(var4);
            this.nameTextView.setText(ContactsController.formatName(var4.first_name, var4.last_name));
            this.avatarImageView.setImage((ImageLocation)ImageLocation.getForUser(var4, false), "50_50", (Drawable)this.avatarDrawable, (Object)var4);
         }
      } else {
         TLRPC.Chat var10 = MessagesController.getInstance(this.currentAccount).getChat(-var3);
         if (var10 != null) {
            this.avatarDrawable.setInfo(var10);
            this.nameTextView.setText(var10.title);
            this.avatarImageView.setImage((ImageLocation)ImageLocation.getForChat(var10, false), "50_50", (Drawable)this.avatarDrawable, (Object)var10);
         }
      }

      GeoPoint var11 = var1.marker.getPosition();
      this.location.setLatitude(var11.getLatitude());
      this.location.setLongitude(var11.getLongitude());
      TLRPC.Message var8 = var1.object;
      var3 = var8.edit_date;
      long var5;
      if (var3 != 0) {
         var5 = (long)var3;
      } else {
         var5 = (long)var8.date;
      }

      String var9 = LocaleController.formatLocationUpdateDate(var5);
      if (var2 != null) {
         float var7 = this.location.distanceTo(var2);
         if (var7 < 1000.0F) {
            this.distanceTextView.setText(String.format("%s - %d %s", var9, (int)var7, LocaleController.getString("MetersAway", 2131559857)));
         } else {
            this.distanceTextView.setText(String.format("%s - %.2f %s", var9, var7 / 1000.0F, LocaleController.getString("KMetersAway", 2131559709)));
         }
      } else {
         this.distanceTextView.setText(var9);
      }

   }
}
