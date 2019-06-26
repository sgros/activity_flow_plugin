package org.telegram.ui.Components;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.view.View;
import android.widget.TextView;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PhotoAlbumPickerActivity;
import org.telegram.ui.PhotoCropActivity;
import org.telegram.ui.PhotoPickerActivity;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;

public class ImageUpdater implements NotificationCenter.NotificationCenterDelegate, PhotoCropActivity.PhotoEditActivityDelegate {
   private TLRPC.PhotoSize bigPhoto;
   private boolean clearAfterUpdate;
   private int currentAccount;
   public String currentPicturePath;
   public ImageUpdater.ImageUpdaterDelegate delegate;
   private String finalPath;
   private ImageReceiver imageReceiver;
   public BaseFragment parentFragment;
   private File picturePath;
   private boolean searchAvailable;
   private TLRPC.PhotoSize smallPhoto;
   private boolean uploadAfterSelect;
   public String uploadingImage;

   public ImageUpdater() {
      this.currentAccount = UserConfig.selectedAccount;
      this.picturePath = null;
      this.searchAvailable = true;
      this.uploadAfterSelect = true;
      this.imageReceiver = new ImageReceiver((View)null);
   }

   private void didSelectPhotos(ArrayList var1) {
      if (!var1.isEmpty()) {
         SendMessagesHelper.SendingMediaInfo var2 = (SendMessagesHelper.SendingMediaInfo)var1.get(0);
         String var6 = var2.path;
         Object var3 = null;
         Bitmap var7;
         if (var6 != null) {
            var7 = ImageLoader.loadBitmap(var6, (Uri)null, 800.0F, 800.0F, true);
         } else {
            MediaController.SearchImage var4 = var2.searchImage;
            var7 = (Bitmap)var3;
            if (var4 != null) {
               TLRPC.Photo var8 = var4.photo;
               File var10;
               if (var8 != null) {
                  TLRPC.PhotoSize var5 = FileLoader.getClosestPhotoSizeWithSize(var8.sizes, AndroidUtilities.getPhotoSize());
                  var7 = (Bitmap)var3;
                  if (var5 != null) {
                     var10 = FileLoader.getPathToAttach(var5, true);
                     this.finalPath = var10.getAbsolutePath();
                     if (!var10.exists()) {
                        File var11 = FileLoader.getPathToAttach(var5, false);
                        var10 = var11;
                        if (!var11.exists()) {
                           var10 = null;
                        }
                     }

                     if (var10 != null) {
                        var7 = ImageLoader.loadBitmap(var10.getAbsolutePath(), (Uri)null, 800.0F, 800.0F, true);
                     } else {
                        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileDidLoad);
                        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileDidFailedLoad);
                        this.uploadingImage = FileLoader.getAttachFileName(var5.location);
                        this.imageReceiver.setImage(ImageLocation.getForPhoto(var5, var2.searchImage.photo), (String)null, (Drawable)null, "jpg", (Object)null, 1);
                        var7 = (Bitmap)var3;
                     }
                  }
               } else {
                  var7 = (Bitmap)var3;
                  if (var4.imageUrl != null) {
                     StringBuilder var9 = new StringBuilder();
                     var9.append(Utilities.MD5(var2.searchImage.imageUrl));
                     var9.append(".");
                     var9.append(ImageLoader.getHttpUrlExtension(var2.searchImage.imageUrl, "jpg"));
                     var6 = var9.toString();
                     var10 = new File(FileLoader.getDirectory(4), var6);
                     this.finalPath = var10.getAbsolutePath();
                     if (var10.exists() && var10.length() != 0L) {
                        var7 = ImageLoader.loadBitmap(var10.getAbsolutePath(), (Uri)null, 800.0F, 800.0F, true);
                     } else {
                        this.uploadingImage = var2.searchImage.imageUrl;
                        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.httpFileDidLoad);
                        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.httpFileDidFailedLoad);
                        this.imageReceiver.setImage(var2.searchImage.imageUrl, (String)null, (Drawable)null, "jpg", 1);
                        var7 = (Bitmap)var3;
                     }
                  }
               }
            }
         }

         this.processBitmap(var7);
      }

   }

   private void processBitmap(Bitmap var1) {
      if (var1 != null) {
         this.bigPhoto = ImageLoader.scaleAndSaveImage(var1, 800.0F, 800.0F, 80, false, 320, 320);
         this.smallPhoto = ImageLoader.scaleAndSaveImage(var1, 150.0F, 150.0F, 80, false, 150, 150);
         TLRPC.PhotoSize var2 = this.smallPhoto;
         if (var2 != null) {
            try {
               Bitmap var9 = BitmapFactory.decodeFile(FileLoader.getPathToAttach(var2, true).getAbsolutePath());
               StringBuilder var3 = new StringBuilder();
               var3.append(this.smallPhoto.location.volume_id);
               var3.append("_");
               var3.append(this.smallPhoto.location.local_id);
               var3.append("@50_50");
               String var10 = var3.toString();
               ImageLoader var4 = ImageLoader.getInstance();
               BitmapDrawable var5 = new BitmapDrawable(var9);
               var4.putImageToCache(var5, var10);
            } catch (Throwable var6) {
            }
         }

         var1.recycle();
         if (this.bigPhoto != null) {
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            StringBuilder var7 = new StringBuilder();
            var7.append(FileLoader.getDirectory(4));
            var7.append("/");
            var7.append(this.bigPhoto.location.volume_id);
            var7.append("_");
            var7.append(this.bigPhoto.location.local_id);
            var7.append(".jpg");
            this.uploadingImage = var7.toString();
            if (this.uploadAfterSelect) {
               NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileDidUpload);
               NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileDidFailUpload);
               FileLoader.getInstance(this.currentAccount).uploadFile(this.uploadingImage, false, true, 16777216);
            }

            ImageUpdater.ImageUpdaterDelegate var8 = this.delegate;
            if (var8 != null) {
               var8.didUploadPhoto((TLRPC.InputFile)null, this.bigPhoto, this.smallPhoto);
            }
         }

      }
   }

   private void startCrop(String var1, Uri var2) {
      Exception var10000;
      label50: {
         LaunchActivity var3;
         boolean var10001;
         try {
            var3 = (LaunchActivity)this.parentFragment.getParentActivity();
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
            break label50;
         }

         if (var3 == null) {
            return;
         }

         Bundle var4;
         try {
            var4 = new Bundle();
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
            break label50;
         }

         if (var1 != null) {
            try {
               var4.putString("photoPath", var1);
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label50;
            }
         } else if (var2 != null) {
            try {
               var4.putParcelable("photoUri", var2);
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label50;
            }
         }

         try {
            PhotoCropActivity var5 = new PhotoCropActivity(var4);
            var5.setDelegate(this);
            var3.presentFragment(var5);
            return;
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
         }
      }

      Exception var11 = var10000;
      FileLog.e((Throwable)var11);
      this.processBitmap(ImageLoader.loadBitmap(var1, var2, 800.0F, 800.0F, true));
   }

   public void clear() {
      if (this.uploadingImage != null) {
         this.clearAfterUpdate = true;
      } else {
         this.parentFragment = null;
         this.delegate = null;
      }

   }

   public void didFinishEdit(Bitmap var1) {
      this.processBitmap(var1);
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.FileDidUpload) {
         if (((String)var3[0]).equals(this.uploadingImage)) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileDidUpload);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileDidFailUpload);
            ImageUpdater.ImageUpdaterDelegate var4 = this.delegate;
            if (var4 != null) {
               var4.didUploadPhoto((TLRPC.InputFile)var3[1], this.bigPhoto, this.smallPhoto);
            }

            this.uploadingImage = null;
            if (this.clearAfterUpdate) {
               this.imageReceiver.setImageBitmap((Drawable)null);
               this.parentFragment = null;
               this.delegate = null;
            }
         }
      } else if (var1 == NotificationCenter.FileDidFailUpload) {
         if (((String)var3[0]).equals(this.uploadingImage)) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileDidUpload);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileDidFailUpload);
            this.uploadingImage = null;
            if (this.clearAfterUpdate) {
               this.imageReceiver.setImageBitmap((Drawable)null);
               this.parentFragment = null;
               this.delegate = null;
            }
         }
      } else if ((var1 == NotificationCenter.fileDidLoad || var1 == NotificationCenter.fileDidFailedLoad || var1 == NotificationCenter.httpFileDidLoad || var1 == NotificationCenter.httpFileDidFailedLoad) && ((String)var3[0]).equals(this.uploadingImage)) {
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileDidLoad);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileDidFailedLoad);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.httpFileDidLoad);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.httpFileDidFailedLoad);
         this.uploadingImage = null;
         if (var1 != NotificationCenter.fileDidLoad && var1 != NotificationCenter.httpFileDidLoad) {
            this.imageReceiver.setImageBitmap((Drawable)null);
         } else {
            this.processBitmap(ImageLoader.loadBitmap(this.finalPath, (Uri)null, 800.0F, 800.0F, true));
         }
      }

   }

   // $FF: synthetic method
   public void lambda$openMenu$0$ImageUpdater(Runnable var1, DialogInterface var2, int var3) {
      if (var3 == 0) {
         this.openCamera();
      } else if (var3 == 1) {
         this.openGallery();
      } else if (this.searchAvailable && var3 == 2) {
         this.openSearch();
      } else if (this.searchAvailable && var3 == 3 || var3 == 2) {
         var1.run();
      }

   }

   public void onActivityResult(int var1, int var2, Intent var3) {
      if (var2 == -1) {
         if (var1 == 13) {
            PhotoViewer.getInstance().setParentActivity(this.parentFragment.getParentActivity());
            short var5 = 0;

            label34: {
               try {
                  ExifInterface var6 = new ExifInterface(this.currentPicturePath);
                  var2 = var6.getAttributeInt("Orientation", 1);
               } catch (Exception var4) {
                  FileLog.e((Throwable)var4);
                  var5 = 0;
                  break label34;
               }

               if (var2 != 3) {
                  if (var2 != 6) {
                     if (var2 == 8) {
                        var5 = 270;
                     }
                  } else {
                     var5 = 90;
                  }
               } else {
                  var5 = 180;
               }
            }

            final ArrayList var7 = new ArrayList();
            var7.add(new MediaController.PhotoEntry(0, 0, 0L, this.currentPicturePath, var5, false));
            PhotoViewer.getInstance().openPhotoForSelect(var7, 0, 1, new PhotoViewer.EmptyPhotoViewerProvider() {
               public boolean allowCaption() {
                  return false;
               }

               public boolean canScrollAway() {
                  return false;
               }

               public void sendButtonPressed(int var1, VideoEditedInfo var2) {
                  MediaController.PhotoEntry var3 = (MediaController.PhotoEntry)var7.get(0);
                  String var4 = var3.imagePath;
                  if (var4 == null) {
                     var4 = var3.path;
                     if (var4 == null) {
                        var4 = null;
                     }
                  }

                  Bitmap var5 = ImageLoader.loadBitmap(var4, (Uri)null, 800.0F, 800.0F, true);
                  ImageUpdater.this.processBitmap(var5);
               }
            }, (ChatActivity)null);
            AndroidUtilities.addMediaToGallery(this.currentPicturePath);
            this.currentPicturePath = null;
         } else if (var1 == 14 && var3 != null && var3.getData() != null) {
            this.startCrop((String)null, var3.getData());
         }
      }

   }

   public void openCamera() {
      BaseFragment var1 = this.parentFragment;
      if (var1 != null && var1.getParentActivity() != null) {
         Exception var10000;
         label66: {
            boolean var10001;
            try {
               if (VERSION.SDK_INT >= 23 && this.parentFragment.getParentActivity().checkSelfPermission("android.permission.CAMERA") != 0) {
                  this.parentFragment.getParentActivity().requestPermissions(new String[]{"android.permission.CAMERA"}, 19);
                  return;
               }
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label66;
            }

            Intent var2;
            File var11;
            try {
               var2 = new Intent("android.media.action.IMAGE_CAPTURE");
               var11 = AndroidUtilities.generatePicturePath();
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label66;
            }

            if (var11 != null) {
               int var3;
               try {
                  var3 = VERSION.SDK_INT;
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label66;
               }

               if (var3 >= 24) {
                  try {
                     var2.putExtra("output", FileProvider.getUriForFile(this.parentFragment.getParentActivity(), "org.telegram.messenger.provider", var11));
                     var2.addFlags(2);
                     var2.addFlags(1);
                  } catch (Exception var7) {
                     var10000 = var7;
                     var10001 = false;
                     break label66;
                  }
               } else {
                  try {
                     var2.putExtra("output", Uri.fromFile(var11));
                  } catch (Exception var6) {
                     var10000 = var6;
                     var10001 = false;
                     break label66;
                  }
               }

               try {
                  this.currentPicturePath = var11.getAbsolutePath();
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
                  break label66;
               }
            }

            try {
               this.parentFragment.startActivityForResult(var2, 13);
               return;
            } catch (Exception var4) {
               var10000 = var4;
               var10001 = false;
            }
         }

         Exception var12 = var10000;
         FileLog.e((Throwable)var12);
      }

   }

   public void openGallery() {
      BaseFragment var1 = this.parentFragment;
      if (var1 != null) {
         if (VERSION.SDK_INT >= 23 && var1 != null && var1.getParentActivity() != null && this.parentFragment.getParentActivity().checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
            this.parentFragment.getParentActivity().requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 4);
         } else {
            PhotoAlbumPickerActivity var2 = new PhotoAlbumPickerActivity(1, false, false, (ChatActivity)null);
            var2.setDelegate(new PhotoAlbumPickerActivity.PhotoAlbumPickerActivityDelegate() {
               public void didSelectPhotos(ArrayList var1) {
                  ImageUpdater.this.didSelectPhotos(var1);
               }

               public void startPhotoSelectActivity() {
                  try {
                     Intent var1 = new Intent("android.intent.action.GET_CONTENT");
                     var1.setType("image/*");
                     ImageUpdater.this.parentFragment.startActivityForResult(var1, 14);
                  } catch (Exception var2) {
                     FileLog.e((Throwable)var2);
                  }

               }
            });
            this.parentFragment.presentFragment(var2);
         }
      }
   }

   public void openMenu(boolean var1, Runnable var2) {
      BaseFragment var3 = this.parentFragment;
      if (var3 != null && var3.getParentActivity() != null) {
         BottomSheet.Builder var4 = new BottomSheet.Builder(this.parentFragment.getParentActivity());
         var4.setTitle(LocaleController.getString("ChoosePhoto", 2131559091));
         boolean var5 = this.searchAvailable;
         byte var6 = 3;
         CharSequence[] var7;
         int[] var9;
         if (var5) {
            if (var1) {
               var7 = new CharSequence[]{LocaleController.getString("ChooseTakePhoto", 2131559097), LocaleController.getString("ChooseFromGallery", 2131559087), LocaleController.getString("ChooseFromSearch", 2131559088), LocaleController.getString("DeletePhoto", 2131559256)};
               var9 = new int[]{2131165571, 2131165792, 2131165593, 2131165348};
            } else {
               var7 = new CharSequence[]{LocaleController.getString("ChooseTakePhoto", 2131559097), LocaleController.getString("ChooseFromGallery", 2131559087), LocaleController.getString("ChooseFromSearch", 2131559088)};
               var9 = new int[]{2131165571, 2131165792, 2131165593};
            }
         } else if (var1) {
            var7 = new CharSequence[]{LocaleController.getString("ChooseTakePhoto", 2131559097), LocaleController.getString("ChooseFromGallery", 2131559087), LocaleController.getString("DeletePhoto", 2131559256)};
            var9 = new int[]{2131165571, 2131165792, 2131165348};
         } else {
            var7 = new CharSequence[]{LocaleController.getString("ChooseTakePhoto", 2131559097), LocaleController.getString("ChooseFromGallery", 2131559087)};
            var9 = new int[]{2131165571, 2131165792};
         }

         var4.setItems(var7, var9, new _$$Lambda$ImageUpdater$ZfeU_OSr8fUwIgo0j9MadtpPC64(this, var2));
         BottomSheet var10 = var4.create();
         this.parentFragment.showDialog(var10);
         TextView var8 = var10.getTitleView();
         if (var8 != null) {
            var8.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            var8.setTextSize(1, 18.0F);
            var8.setTextColor(Theme.getColor("dialogTextBlack"));
         }

         if (!this.searchAvailable) {
            var6 = 2;
         }

         var10.setItemColor(var6, Theme.getColor("dialogTextRed2"), Theme.getColor("dialogRedIcon"));
      }

   }

   public void openSearch() {
      if (this.parentFragment != null) {
         final HashMap var1 = new HashMap();
         final ArrayList var2 = new ArrayList();
         PhotoPickerActivity var3 = new PhotoPickerActivity(0, (MediaController.AlbumEntry)null, var1, var2, new ArrayList(), 1, false, (ChatActivity)null);
         var3.setDelegate(new PhotoPickerActivity.PhotoPickerActivityDelegate() {
            private boolean sendPressed;

            private void sendSelectedPhotos(HashMap var1x, ArrayList var2x) {
            }

            public void actionButtonPressed(boolean var1x) {
               if (!var1.isEmpty() && ImageUpdater.this.delegate != null && !this.sendPressed && !var1x) {
                  this.sendPressed = true;
                  ArrayList var2x = new ArrayList();

                  for(int var3 = 0; var3 < var2.size(); ++var3) {
                     Object var4 = var1.get(var2.get(var3));
                     SendMessagesHelper.SendingMediaInfo var5 = new SendMessagesHelper.SendingMediaInfo();
                     var2x.add(var5);
                     if (var4 instanceof MediaController.SearchImage) {
                        MediaController.SearchImage var6 = (MediaController.SearchImage)var4;
                        String var8 = var6.imagePath;
                        if (var8 != null) {
                           var5.path = var8;
                        } else {
                           var5.searchImage = var6;
                        }

                        CharSequence var9 = var6.caption;
                        Object var7 = null;
                        if (var9 != null) {
                           var8 = var9.toString();
                        } else {
                           var8 = null;
                        }

                        var5.caption = var8;
                        var5.entities = var6.entities;
                        ArrayList var10 = (ArrayList)var7;
                        if (!var6.stickers.isEmpty()) {
                           var10 = new ArrayList(var6.stickers);
                        }

                        var5.masks = var10;
                        var5.ttl = var6.ttl;
                     }
                  }

                  ImageUpdater.this.didSelectPhotos(var2x);
               }

            }

            public void selectedPhotosChanged() {
            }
         });
         var3.setInitialSearchString(this.delegate.getInitialSearchString());
         this.parentFragment.presentFragment(var3);
      }
   }

   public void setSearchAvailable(boolean var1) {
      this.searchAvailable = var1;
   }

   public void setUploadAfterSelect(boolean var1) {
      this.uploadAfterSelect = var1;
   }

   public interface ImageUpdaterDelegate {
      void didUploadPhoto(TLRPC.InputFile var1, TLRPC.PhotoSize var2, TLRPC.PhotoSize var3);

      String getInitialSearchString();
   }
}
