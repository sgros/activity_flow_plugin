package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;

public class GroupCreateSpan extends View {
   private static Paint backPaint = new Paint(1);
   private static TextPaint textPaint = new TextPaint(1);
   private AvatarDrawable avatarDrawable;
   private int[] colors;
   private ContactsController.Contact currentContact;
   private Drawable deleteDrawable;
   private boolean deleting;
   private ImageReceiver imageReceiver;
   private String key;
   private long lastUpdateTime;
   private StaticLayout nameLayout;
   private float progress;
   private RectF rect;
   private int textWidth;
   private float textX;
   private int uid;

   public GroupCreateSpan(Context var1, ContactsController.Contact var2) {
      this(var1, (TLObject)null, var2);
   }

   public GroupCreateSpan(Context var1, TLObject var2) {
      this(var1, var2, (ContactsController.Contact)null);
   }

   public GroupCreateSpan(Context var1, TLObject var2, ContactsController.Contact var3) {
      String var6;
      ImageLocation var9;
      Object var10;
      label29: {
         super(var1);
         this.rect = new RectF();
         this.colors = new int[8];
         this.currentContact = var3;
         this.deleteDrawable = this.getResources().getDrawable(2131165372);
         textPaint.setTextSize((float)AndroidUtilities.dp(14.0F));
         this.avatarDrawable = new AvatarDrawable();
         this.avatarDrawable.setTextSize(AndroidUtilities.dp(12.0F));
         Object var7;
         ImageLocation var8;
         if (var2 instanceof TLRPC.User) {
            var7 = (TLRPC.User)var2;
            this.avatarDrawable.setInfo((TLRPC.User)var7);
            this.uid = ((TLRPC.User)var7).id;
            var6 = UserObject.getFirstName((TLRPC.User)var7);
            var8 = ImageLocation.getForUser((TLRPC.User)var7, false);
         } else {
            if (!(var2 instanceof TLRPC.Chat)) {
               this.avatarDrawable.setInfo(0, var3.first_name, var3.last_name, false);
               this.uid = var3.contact_id;
               this.key = var3.key;
               if (!TextUtils.isEmpty(var3.first_name)) {
                  var6 = var3.first_name;
               } else {
                  var6 = var3.last_name;
               }

               var9 = null;
               var10 = var9;
               break label29;
            }

            var7 = (TLRPC.Chat)var2;
            this.avatarDrawable.setInfo((TLRPC.Chat)var7);
            this.uid = -((TLRPC.Chat)var7).id;
            var6 = ((TLRPC.Chat)var7).title;
            var8 = ImageLocation.getForChat((TLRPC.Chat)var7, false);
         }

         Object var4 = var7;
         var9 = var8;
         var10 = var4;
      }

      this.imageReceiver = new ImageReceiver();
      this.imageReceiver.setRoundRadius(AndroidUtilities.dp(16.0F));
      this.imageReceiver.setParentView(this);
      this.imageReceiver.setImageCoords(0, 0, AndroidUtilities.dp(32.0F), AndroidUtilities.dp(32.0F));
      int var5;
      if (AndroidUtilities.isTablet()) {
         var5 = AndroidUtilities.dp(366.0F) / 2;
      } else {
         android.graphics.Point var11 = AndroidUtilities.displaySize;
         var5 = (Math.min(var11.x, var11.y) - AndroidUtilities.dp(164.0F)) / 2;
      }

      this.nameLayout = new StaticLayout(TextUtils.ellipsize(var6.replace('\n', ' '), textPaint, (float)var5, TruncateAt.END), textPaint, 1000, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
      if (this.nameLayout.getLineCount() > 0) {
         this.textWidth = (int)Math.ceil((double)this.nameLayout.getLineWidth(0));
         this.textX = -this.nameLayout.getLineLeft(0);
      }

      this.imageReceiver.setImage(var9, "50_50", this.avatarDrawable, 0, (String)null, var10, 1);
      this.updateColors();
   }

   public void cancelDeleteAnimation() {
      if (this.deleting) {
         this.deleting = false;
         this.lastUpdateTime = System.currentTimeMillis();
         this.invalidate();
      }
   }

   public ContactsController.Contact getContact() {
      return this.currentContact;
   }

   public String getKey() {
      return this.key;
   }

   public int getUid() {
      return this.uid;
   }

   public boolean isDeleting() {
      return this.deleting;
   }

   protected void onDraw(Canvas var1) {
      if (this.deleting && this.progress != 1.0F || !this.deleting && this.progress != 0.0F) {
         long var4;
         label29: {
            long var2 = System.currentTimeMillis() - this.lastUpdateTime;
            if (var2 >= 0L) {
               var4 = var2;
               if (var2 <= 17L) {
                  break label29;
               }
            }

            var4 = 17L;
         }

         if (this.deleting) {
            this.progress += (float)var4 / 120.0F;
            if (this.progress >= 1.0F) {
               this.progress = 1.0F;
            }
         } else {
            this.progress -= (float)var4 / 120.0F;
            if (this.progress < 0.0F) {
               this.progress = 0.0F;
            }
         }

         this.invalidate();
      }

      var1.save();
      this.rect.set(0.0F, 0.0F, (float)this.getMeasuredWidth(), (float)AndroidUtilities.dp(32.0F));
      Paint var6 = backPaint;
      int[] var7 = this.colors;
      int var8 = var7[6];
      float var9 = (float)(var7[7] - var7[6]);
      float var10 = this.progress;
      var6.setColor(Color.argb(var8 + (int)(var9 * var10), var7[0] + (int)((float)(var7[1] - var7[0]) * var10), var7[2] + (int)((float)(var7[3] - var7[2]) * var10), var7[4] + (int)((float)(var7[5] - var7[4]) * var10)));
      var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(16.0F), (float)AndroidUtilities.dp(16.0F), backPaint);
      this.imageReceiver.draw(var1);
      if (this.progress != 0.0F) {
         var8 = this.avatarDrawable.getColor();
         var9 = (float)Color.alpha(var8) / 255.0F;
         backPaint.setColor(var8);
         backPaint.setAlpha((int)(this.progress * 255.0F * var9));
         var1.drawCircle((float)AndroidUtilities.dp(16.0F), (float)AndroidUtilities.dp(16.0F), (float)AndroidUtilities.dp(16.0F), backPaint);
         var1.save();
         var1.rotate((1.0F - this.progress) * 45.0F, (float)AndroidUtilities.dp(16.0F), (float)AndroidUtilities.dp(16.0F));
         this.deleteDrawable.setBounds(AndroidUtilities.dp(11.0F), AndroidUtilities.dp(11.0F), AndroidUtilities.dp(21.0F), AndroidUtilities.dp(21.0F));
         this.deleteDrawable.setAlpha((int)(this.progress * 255.0F));
         this.deleteDrawable.draw(var1);
         var1.restore();
      }

      var1.translate(this.textX + (float)AndroidUtilities.dp(41.0F), (float)AndroidUtilities.dp(8.0F));
      this.nameLayout.draw(var1);
      var1.restore();
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.setText(this.nameLayout.getText());
      if (this.isDeleting() && VERSION.SDK_INT >= 21) {
         var1.addAction(new AccessibilityAction(AccessibilityAction.ACTION_CLICK.getId(), LocaleController.getString("Delete", 2131559227)));
      }

   }

   protected void onMeasure(int var1, int var2) {
      this.setMeasuredDimension(AndroidUtilities.dp(57.0F) + this.textWidth, AndroidUtilities.dp(32.0F));
   }

   public void startDeleteAnimation() {
      if (!this.deleting) {
         this.deleting = true;
         this.lastUpdateTime = System.currentTimeMillis();
         this.invalidate();
      }
   }

   public void updateColors() {
      int var1 = Theme.getColor("avatar_backgroundGroupCreateSpanBlue");
      int var2 = Theme.getColor("groupcreate_spanBackground");
      int var3 = Theme.getColor("groupcreate_spanText");
      int var4 = Theme.getColor("groupcreate_spanDelete");
      this.colors[0] = Color.red(var2);
      this.colors[1] = Color.red(var1);
      this.colors[2] = Color.green(var2);
      this.colors[3] = Color.green(var1);
      this.colors[4] = Color.blue(var2);
      this.colors[5] = Color.blue(var1);
      this.colors[6] = Color.alpha(var2);
      this.colors[7] = Color.alpha(var1);
      textPaint.setColor(var3);
      this.deleteDrawable.setColorFilter(new PorterDuffColorFilter(var4, Mode.MULTIPLY));
      backPaint.setColor(var2);
      this.avatarDrawable.setColor(AvatarDrawable.getColorForId(5));
   }
}
