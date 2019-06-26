package org.telegram.ui.Components;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build.VERSION;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.PhotoAlbumPickerActivity;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;

public class WallpaperUpdater {
   private String currentPicturePath;
   private File currentWallpaperPath;
   private WallpaperUpdater.WallpaperUpdaterDelegate delegate;
   private Activity parentActivity;
   private BaseFragment parentFragment;
   private File picturePath = null;

   public WallpaperUpdater(Activity var1, BaseFragment var2, WallpaperUpdater.WallpaperUpdaterDelegate var3) {
      this.parentActivity = var1;
      this.parentFragment = var2;
      this.delegate = var3;
   }

   private void didSelectPhotos(ArrayList var1) {
      try {
         if (!var1.isEmpty()) {
            SendMessagesHelper.SendingMediaInfo var6 = (SendMessagesHelper.SendingMediaInfo)var1.get(0);
            if (var6.path != null) {
               File var3 = FileLoader.getDirectory(4);
               StringBuilder var4 = new StringBuilder();
               var4.append(Utilities.random.nextInt());
               var4.append(".jpg");
               File var2 = new File(var3, var4.toString());
               this.currentWallpaperPath = var2;
               android.graphics.Point var8 = AndroidUtilities.getRealScreenSize();
               Bitmap var9 = ImageLoader.loadBitmap(var6.path, (Uri)null, (float)var8.x, (float)var8.y, true);
               FileOutputStream var7 = new FileOutputStream(this.currentWallpaperPath);
               var9.compress(CompressFormat.JPEG, 87, var7);
               this.delegate.didSelectWallpaper(this.currentWallpaperPath, var9, true);
            }
         }
      } catch (Throwable var5) {
         FileLog.e(var5);
      }

   }

   public void cleanup() {
   }

   public String getCurrentPicturePath() {
      return this.currentPicturePath;
   }

   // $FF: synthetic method
   public void lambda$showAlert$0$WallpaperUpdater(boolean var1, DialogInterface var2, int var3) {
      Exception var10000;
      Exception var16;
      boolean var10001;
      if (var3 == 0) {
         label90: {
            File var4;
            Intent var15;
            try {
               var15 = new Intent("android.media.action.IMAGE_CAPTURE");
               var4 = AndroidUtilities.generatePicturePath();
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label90;
            }

            if (var4 != null) {
               try {
                  var3 = VERSION.SDK_INT;
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label90;
               }

               if (var3 >= 24) {
                  try {
                     var15.putExtra("output", FileProvider.getUriForFile(this.parentActivity, "org.telegram.messenger.provider", var4));
                     var15.addFlags(2);
                     var15.addFlags(1);
                  } catch (Exception var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label90;
                  }
               } else {
                  try {
                     var15.putExtra("output", Uri.fromFile(var4));
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label90;
                  }
               }

               try {
                  this.currentPicturePath = var4.getAbsolutePath();
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label90;
               }
            }

            try {
               this.parentActivity.startActivityForResult(var15, 10);
               return;
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
            }
         }

         var16 = var10000;

         try {
            FileLog.e((Throwable)var16);
            return;
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
         }
      } else if (var3 == 1) {
         try {
            this.openGallery();
            return;
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
         }
      } else {
         if (!var1) {
            return;
         }

         if (var3 == 2) {
            try {
               this.delegate.needOpenColorPicker();
               return;
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
            }
         } else {
            if (var3 != 3) {
               return;
            }

            try {
               this.delegate.didSelectWallpaper((File)null, (Bitmap)null, false);
               return;
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
            }
         }
      }

      var16 = var10000;
      FileLog.e((Throwable)var16);
   }

   public void onActivityResult(int param1, int param2, Intent param3) {
      // $FF: Couldn't be decompiled
   }

   public void openGallery() {
      BaseFragment var1 = this.parentFragment;
      if (var1 != null) {
         if (VERSION.SDK_INT >= 23 && var1.getParentActivity() != null && this.parentFragment.getParentActivity().checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
            this.parentFragment.getParentActivity().requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 4);
            return;
         }

         PhotoAlbumPickerActivity var2 = new PhotoAlbumPickerActivity(2, false, false, (ChatActivity)null);
         var2.setAllowSearchImages(false);
         var2.setDelegate(new PhotoAlbumPickerActivity.PhotoAlbumPickerActivityDelegate() {
            public void didSelectPhotos(ArrayList var1) {
               WallpaperUpdater.this.didSelectPhotos(var1);
            }

            public void startPhotoSelectActivity() {
               try {
                  Intent var1 = new Intent("android.intent.action.PICK");
                  var1.setType("image/*");
                  WallpaperUpdater.this.parentActivity.startActivityForResult(var1, 11);
               } catch (Exception var2) {
                  FileLog.e((Throwable)var2);
               }

            }
         });
         this.parentFragment.presentFragment(var2);
      } else {
         Intent var3 = new Intent("android.intent.action.PICK");
         var3.setType("image/*");
         this.parentActivity.startActivityForResult(var3, 11);
      }

   }

   public void setCurrentPicturePath(String var1) {
      this.currentPicturePath = var1;
   }

   public void showAlert(boolean var1) {
      BottomSheet.Builder var2 = new BottomSheet.Builder(this.parentActivity);
      var2.setTitle(LocaleController.getString("ChoosePhoto", 2131559091));
      CharSequence[] var3;
      int[] var4;
      if (var1) {
         var3 = new CharSequence[]{LocaleController.getString("ChooseTakePhoto", 2131559097), LocaleController.getString("SelectFromGallery", 2131560683), LocaleController.getString("SelectColor", 2131560678), LocaleController.getString("Default", 2131559225)};
         var4 = null;
      } else {
         var3 = new CharSequence[]{LocaleController.getString("ChooseTakePhoto", 2131559097), LocaleController.getString("SelectFromGallery", 2131560683)};
         var4 = new int[]{2131165571, 2131165792};
      }

      var2.setItems(var3, var4, new _$$Lambda$WallpaperUpdater$vK87huFk9jAjs7SXqPz2knBtx88(this, var1));
      var2.show();
   }

   public interface WallpaperUpdaterDelegate {
      void didSelectWallpaper(File var1, Bitmap var2, boolean var3);

      void needOpenColorPicker();
   }
}
