package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.SimpleColorFilter;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieValueCallback;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;

public class AvatarDrawable extends Drawable {
   public static final int AVATAR_TYPE_ARCHIVED = 3;
   public static final int AVATAR_TYPE_NORMAL = 0;
   public static final int AVATAR_TYPE_SAVED = 1;
   public static final int AVATAR_TYPE_SAVED_SMALL = 2;
   private float archivedAvatarProgress;
   private int avatarType;
   private int color;
   private boolean drawBrodcast;
   private boolean isProfile;
   private TextPaint namePaint;
   private StringBuilder stringBuilder;
   private float textHeight;
   private StaticLayout textLayout;
   private float textLeft;
   private float textWidth;

   public AvatarDrawable() {
      this.stringBuilder = new StringBuilder(5);
      this.namePaint = new TextPaint(1);
      this.namePaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.namePaint.setTextSize((float)AndroidUtilities.dp(18.0F));
   }

   public AvatarDrawable(TLRPC.Chat var1) {
      this(var1, false);
   }

   public AvatarDrawable(TLRPC.Chat var1, boolean var2) {
      this();
      this.isProfile = var2;
      if (var1 != null) {
         int var3 = var1.id;
         String var4 = var1.title;
         if (var3 < 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.setInfo(var3, var4, (String)null, var2, (String)null);
      }

   }

   public AvatarDrawable(TLRPC.User var1) {
      this(var1, false);
   }

   public AvatarDrawable(TLRPC.User var1, boolean var2) {
      this();
      this.isProfile = var2;
      if (var1 != null) {
         this.setInfo(var1.id, var1.first_name, var1.last_name, false, (String)null);
      }

   }

   public static int getButtonColorForId(int var0) {
      return Theme.getColor("avatar_actionBarSelectorBlue");
   }

   public static int getColorForId(int var0) {
      return Theme.getColor(Theme.keys_avatar_background[getColorIndex(var0)]);
   }

   public static int getColorIndex(int var0) {
      return var0 >= 0 && var0 < 7 ? var0 : Math.abs(var0 % Theme.keys_avatar_background.length);
   }

   public static int getIconColorForId(int var0) {
      return Theme.getColor("avatar_actionBarIconBlue");
   }

   public static int getNameColorForId(int var0) {
      return Theme.getColor(Theme.keys_avatar_nameInMessage[getColorIndex(var0)]);
   }

   public static int getProfileBackColorForId(int var0) {
      return Theme.getColor("avatar_backgroundActionBarBlue");
   }

   public static int getProfileColorForId(int var0) {
      return Theme.getColor(Theme.keys_avatar_background[getColorIndex(var0)]);
   }

   public static int getProfileTextColorForId(int var0) {
      return Theme.getColor("avatar_subtitleInProfileBlue");
   }

   public void draw(Canvas var1) {
      android.graphics.Rect var2 = this.getBounds();
      if (var2 != null) {
         int var3 = var2.width();
         this.namePaint.setColor(Theme.getColor("avatar_text"));
         Theme.avatar_backgroundPaint.setColor(this.color);
         var1.save();
         var1.translate((float)var2.left, (float)var2.top);
         float var4 = (float)var3;
         float var5 = var4 / 2.0F;
         var1.drawCircle(var5, var5, var5, Theme.avatar_backgroundPaint);
         int var6 = this.avatarType;
         int var7;
         int var8;
         int var9;
         if (var6 == 3) {
            if (this.archivedAvatarProgress != 0.0F) {
               Theme.avatar_backgroundPaint.setColor(Theme.getColor("avatar_backgroundArchived"));
               var1.drawCircle(var5, var5, this.archivedAvatarProgress * var5, Theme.avatar_backgroundPaint);
               if (Theme.dialogs_archiveAvatarDrawableRecolored) {
                  Theme.dialogs_archiveAvatarDrawable.addValueCallback(new KeyPath(new String[]{"Arrow1", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("avatar_backgroundArchived"))));
                  Theme.dialogs_archiveAvatarDrawable.addValueCallback(new KeyPath(new String[]{"Arrow2", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("avatar_backgroundArchived"))));
                  Theme.dialogs_archiveAvatarDrawableRecolored = false;
               }
            } else if (!Theme.dialogs_archiveAvatarDrawableRecolored) {
               Theme.dialogs_archiveAvatarDrawable.addValueCallback(new KeyPath(new String[]{"Arrow1", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("avatar_backgroundArchivedHidden"))));
               Theme.dialogs_archiveAvatarDrawable.addValueCallback(new KeyPath(new String[]{"Arrow2", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("avatar_backgroundArchivedHidden"))));
               Theme.dialogs_archiveAvatarDrawableRecolored = true;
            }

            var6 = Theme.dialogs_archiveAvatarDrawable.getIntrinsicWidth();
            var7 = Theme.dialogs_archiveAvatarDrawable.getIntrinsicHeight();
            var8 = (var3 - var6) / 2;
            var9 = (var3 - var7) / 2;
            var1.save();
            var1.translate((float)var8, (float)var9);
            Theme.dialogs_archiveAvatarDrawable.setBounds(var8, var9, var6 + var8, var7 + var9);
            Theme.dialogs_archiveAvatarDrawable.draw(var1);
            var1.restore();
         } else {
            label44: {
               Drawable var10;
               if (var6 != 0) {
                  var10 = Theme.avatar_savedDrawable;
                  if (var10 != null) {
                     var9 = var10.getIntrinsicWidth();
                     var8 = Theme.avatar_savedDrawable.getIntrinsicHeight();
                     var7 = var9;
                     var6 = var8;
                     if (this.avatarType == 2) {
                        var7 = (int)((float)var9 * 0.8F);
                        var6 = (int)((float)var8 * 0.8F);
                     }

                     var8 = (var3 - var7) / 2;
                     var9 = (var3 - var6) / 2;
                     Theme.avatar_savedDrawable.setBounds(var8, var9, var7 + var8, var6 + var9);
                     Theme.avatar_savedDrawable.draw(var1);
                     break label44;
                  }
               }

               if (this.drawBrodcast) {
                  var10 = Theme.avatar_broadcastDrawable;
                  if (var10 != null) {
                     var6 = (var3 - var10.getIntrinsicWidth()) / 2;
                     var7 = (var3 - Theme.avatar_broadcastDrawable.getIntrinsicHeight()) / 2;
                     var10 = Theme.avatar_broadcastDrawable;
                     var10.setBounds(var6, var7, var10.getIntrinsicWidth() + var6, Theme.avatar_broadcastDrawable.getIntrinsicHeight() + var7);
                     Theme.avatar_broadcastDrawable.draw(var1);
                     break label44;
                  }
               }

               if (this.textLayout != null) {
                  var1.translate((var4 - this.textWidth) / 2.0F - this.textLeft, (var4 - this.textHeight) / 2.0F);
                  this.textLayout.draw(var1);
               }
            }
         }

         var1.restore();
      }
   }

   public int getAvatarType() {
      return this.avatarType;
   }

   public int getColor() {
      return this.color;
   }

   public int getIntrinsicHeight() {
      return 0;
   }

   public int getIntrinsicWidth() {
      return 0;
   }

   public int getOpacity() {
      return -2;
   }

   public void setAlpha(int var1) {
   }

   public void setArchivedAvatarHiddenProgress(float var1) {
      this.archivedAvatarProgress = var1;
   }

   public void setAvatarType(int var1) {
      this.avatarType = var1;
      if (this.avatarType == 3) {
         this.color = Theme.getColor("avatar_backgroundArchivedHidden");
      } else {
         this.color = Theme.getColor("avatar_backgroundSaved");
      }

   }

   public void setColor(int var1) {
      this.color = var1;
   }

   public void setColorFilter(ColorFilter var1) {
   }

   public void setInfo(int var1, String var2, String var3, boolean var4) {
      this.setInfo(var1, var2, var3, var4, (String)null);
   }

   public void setInfo(int var1, String var2, String var3, boolean var4, String var5) {
      if (this.isProfile) {
         this.color = getProfileColorForId(var1);
      } else {
         this.color = getColorForId(var1);
      }

      String var6;
      String var7;
      label89: {
         this.drawBrodcast = var4;
         this.avatarType = 0;
         if (var2 != null) {
            var6 = var2;
            var7 = var3;
            if (var2.length() != 0) {
               break label89;
            }
         }

         var7 = null;
         var6 = var3;
      }

      this.stringBuilder.setLength(0);
      if (var5 != null) {
         this.stringBuilder.append(var5);
      } else {
         if (var6 != null && var6.length() > 0) {
            this.stringBuilder.appendCodePoint(var6.codePointAt(0));
         }

         if (var7 != null && var7.length() > 0) {
            var1 = var7.length() - 1;

            Integer var10;
            for(var10 = null; var1 >= 0 && (var10 == null || var7.charAt(var1) != ' '); --var1) {
               var10 = var7.codePointAt(var1);
            }

            if (VERSION.SDK_INT > 17) {
               this.stringBuilder.append("\u200c");
            }

            this.stringBuilder.appendCodePoint(var10);
         } else if (var6 != null && var6.length() > 0) {
            for(var1 = var6.length() - 1; var1 >= 0; --var1) {
               if (var6.charAt(var1) == ' ' && var1 != var6.length() - 1) {
                  int var8 = var1 + 1;
                  if (var6.charAt(var8) != ' ') {
                     if (VERSION.SDK_INT > 17) {
                        this.stringBuilder.append("\u200c");
                     }

                     this.stringBuilder.appendCodePoint(var6.codePointAt(var8));
                     break;
                  }
               }
            }
         }
      }

      if (this.stringBuilder.length() > 0) {
         var3 = this.stringBuilder.toString().toUpperCase();

         try {
            StaticLayout var11 = new StaticLayout(var3, this.namePaint, AndroidUtilities.dp(100.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
            this.textLayout = var11;
            if (this.textLayout.getLineCount() > 0) {
               this.textLeft = this.textLayout.getLineLeft(0);
               this.textWidth = this.textLayout.getLineWidth(0);
               this.textHeight = (float)this.textLayout.getLineBottom(0);
            }
         } catch (Exception var9) {
            FileLog.e((Throwable)var9);
         }
      } else {
         this.textLayout = null;
      }

   }

   public void setInfo(TLRPC.Chat var1) {
      if (var1 != null) {
         int var2 = var1.id;
         String var4 = var1.title;
         boolean var3;
         if (var2 < 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.setInfo(var2, var4, (String)null, var3, (String)null);
      }

   }

   public void setInfo(TLRPC.User var1) {
      if (var1 != null) {
         this.setInfo(var1.id, var1.first_name, var1.last_name, false, (String)null);
      }

   }

   public void setProfile(boolean var1) {
      this.isProfile = var1;
   }

   public void setTextSize(int var1) {
      this.namePaint.setTextSize((float)var1);
   }
}
