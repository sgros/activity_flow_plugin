package org.telegram.ui.Cells;

import android.content.Context;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.LayoutHelper;

public class InviteUserCell extends FrameLayout {
   private AvatarDrawable avatarDrawable = new AvatarDrawable();
   private BackupImageView avatarImageView;
   private CheckBox2 checkBox;
   private ContactsController.Contact currentContact;
   private CharSequence currentName;
   private SimpleTextView nameTextView;
   private SimpleTextView statusTextView;

   public InviteUserCell(Context var1, boolean var2) {
      super(var1);
      this.avatarImageView = new BackupImageView(var1);
      this.avatarImageView.setRoundRadius(AndroidUtilities.dp(24.0F));
      BackupImageView var3 = this.avatarImageView;
      boolean var4 = LocaleController.isRTL;
      byte var5 = 5;
      byte var6;
      if (var4) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      float var7;
      if (LocaleController.isRTL) {
         var7 = 0.0F;
      } else {
         var7 = 11.0F;
      }

      float var8;
      if (LocaleController.isRTL) {
         var8 = 11.0F;
      } else {
         var8 = 0.0F;
      }

      this.addView(var3, LayoutHelper.createFrame(50, 50.0F, var6 | 48, var7, 11.0F, var8, 0.0F));
      this.nameTextView = new SimpleTextView(var1);
      this.nameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.nameTextView.setTextSize(17);
      SimpleTextView var10 = this.nameTextView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      var10.setGravity(var6 | 48);
      var10 = this.nameTextView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      if (LocaleController.isRTL) {
         var7 = 28.0F;
      } else {
         var7 = 72.0F;
      }

      if (LocaleController.isRTL) {
         var8 = 72.0F;
      } else {
         var8 = 28.0F;
      }

      this.addView(var10, LayoutHelper.createFrame(-1, 20.0F, var6 | 48, var7, 14.0F, var8, 0.0F));
      this.statusTextView = new SimpleTextView(var1);
      this.statusTextView.setTextSize(16);
      var10 = this.statusTextView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      var10.setGravity(var6 | 48);
      var10 = this.statusTextView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      if (LocaleController.isRTL) {
         var7 = 28.0F;
      } else {
         var7 = 72.0F;
      }

      if (LocaleController.isRTL) {
         var8 = 72.0F;
      } else {
         var8 = 28.0F;
      }

      this.addView(var10, LayoutHelper.createFrame(-1, 20.0F, var6 | 48, var7, 39.0F, var8, 0.0F));
      if (var2) {
         this.checkBox = new CheckBox2(var1);
         this.checkBox.setColor((String)null, "windowBackgroundWhite", "checkboxCheck");
         this.checkBox.setSize(21);
         this.checkBox.setDrawUnchecked(false);
         this.checkBox.setDrawBackgroundAsArc(3);
         CheckBox2 var9 = this.checkBox;
         if (LocaleController.isRTL) {
            var6 = var5;
         } else {
            var6 = 3;
         }

         if (LocaleController.isRTL) {
            var7 = 0.0F;
         } else {
            var7 = 40.0F;
         }

         if (LocaleController.isRTL) {
            var8 = 39.0F;
         } else {
            var8 = 0.0F;
         }

         this.addView(var9, LayoutHelper.createFrame(24, 24.0F, var6 | 48, var7, 40.0F, var8, 0.0F));
      }

   }

   public ContactsController.Contact getContact() {
      return this.currentContact;
   }

   public boolean hasOverlappingRendering() {
      return false;
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(72.0F), 1073741824));
   }

   public void recycle() {
      this.avatarImageView.getImageReceiver().cancelLoadImage();
   }

   public void setChecked(boolean var1, boolean var2) {
      this.checkBox.setChecked(var1, var2);
   }

   public void setUser(ContactsController.Contact var1, CharSequence var2) {
      this.currentContact = var1;
      this.currentName = var2;
      this.update(0);
   }

   public void update(int var1) {
      ContactsController.Contact var2 = this.currentContact;
      if (var2 != null) {
         this.avatarDrawable.setInfo(var2.contact_id, var2.first_name, var2.last_name, false);
         CharSequence var4 = this.currentName;
         if (var4 != null) {
            this.nameTextView.setText(var4, true);
         } else {
            SimpleTextView var5 = this.nameTextView;
            ContactsController.Contact var3 = this.currentContact;
            var5.setText(ContactsController.formatName(var3.first_name, var3.last_name));
         }

         this.statusTextView.setTag("windowBackgroundWhiteGrayText");
         this.statusTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
         var2 = this.currentContact;
         var1 = var2.imported;
         if (var1 > 0) {
            this.statusTextView.setText(LocaleController.formatPluralString("TelegramContacts", var1));
         } else {
            this.statusTextView.setText((CharSequence)var2.phones.get(0));
         }

         this.avatarImageView.setImageDrawable(this.avatarDrawable);
      }
   }
}
