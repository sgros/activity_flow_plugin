// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import java.util.Collection;
import org.telegram.ui.PhotoPickerActivity;
import java.util.HashMap;
import android.widget.TextView;
import org.telegram.ui.ActionBar.Theme;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.PhotoAlbumPickerActivity;
import android.content.Context;
import androidx.core.content.FileProvider;
import android.os.Build$VERSION;
import org.telegram.ui.ChatActivity;
import org.telegram.messenger.VideoEditedInfo;
import androidx.exifinterface.media.ExifInterface;
import org.telegram.ui.PhotoViewer;
import android.content.Intent;
import android.content.DialogInterface;
import org.telegram.messenger.FileLog;
import android.os.Parcelable;
import android.os.Bundle;
import org.telegram.ui.LaunchActivity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.BitmapFactory;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.Utilities;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.tgnet.TLObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.AndroidUtilities;
import android.net.Uri;
import org.telegram.messenger.ImageLoader;
import android.graphics.Bitmap;
import org.telegram.messenger.SendMessagesHelper;
import java.util.ArrayList;
import android.view.View;
import org.telegram.messenger.UserConfig;
import java.io.File;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.messenger.ImageReceiver;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.PhotoCropActivity;
import org.telegram.messenger.NotificationCenter;

public class ImageUpdater implements NotificationCenterDelegate, PhotoEditActivityDelegate
{
    private TLRPC.PhotoSize bigPhoto;
    private boolean clearAfterUpdate;
    private int currentAccount;
    public String currentPicturePath;
    public ImageUpdaterDelegate delegate;
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
        this.imageReceiver = new ImageReceiver(null);
    }
    
    private void didSelectPhotos(final ArrayList<SendMessagesHelper.SendingMediaInfo> list) {
        if (!list.isEmpty()) {
            final SendMessagesHelper.SendingMediaInfo sendingMediaInfo = list.get(0);
            final String path = sendingMediaInfo.path;
            final Bitmap bitmap = null;
            Bitmap bitmap2;
            if (path != null) {
                bitmap2 = ImageLoader.loadBitmap(path, null, 800.0f, 800.0f, true);
            }
            else {
                final MediaController.SearchImage searchImage = sendingMediaInfo.searchImage;
                bitmap2 = bitmap;
                if (searchImage != null) {
                    final TLRPC.Photo photo = searchImage.photo;
                    if (photo != null) {
                        final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, AndroidUtilities.getPhotoSize());
                        bitmap2 = bitmap;
                        if (closestPhotoSizeWithSize != null) {
                            File file = FileLoader.getPathToAttach(closestPhotoSizeWithSize, true);
                            this.finalPath = file.getAbsolutePath();
                            if (!file.exists() && !(file = FileLoader.getPathToAttach(closestPhotoSizeWithSize, false)).exists()) {
                                file = null;
                            }
                            if (file != null) {
                                bitmap2 = ImageLoader.loadBitmap(file.getAbsolutePath(), null, 800.0f, 800.0f, true);
                            }
                            else {
                                NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileDidLoad);
                                NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileDidFailedLoad);
                                this.uploadingImage = FileLoader.getAttachFileName(closestPhotoSizeWithSize.location);
                                this.imageReceiver.setImage(ImageLocation.getForPhoto(closestPhotoSizeWithSize, sendingMediaInfo.searchImage.photo), null, null, "jpg", null, 1);
                                bitmap2 = bitmap;
                            }
                        }
                    }
                    else {
                        bitmap2 = bitmap;
                        if (searchImage.imageUrl != null) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append(Utilities.MD5(sendingMediaInfo.searchImage.imageUrl));
                            sb.append(".");
                            sb.append(ImageLoader.getHttpUrlExtension(sendingMediaInfo.searchImage.imageUrl, "jpg"));
                            final File file2 = new File(FileLoader.getDirectory(4), sb.toString());
                            this.finalPath = file2.getAbsolutePath();
                            if (file2.exists() && file2.length() != 0L) {
                                bitmap2 = ImageLoader.loadBitmap(file2.getAbsolutePath(), null, 800.0f, 800.0f, true);
                            }
                            else {
                                this.uploadingImage = sendingMediaInfo.searchImage.imageUrl;
                                NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.httpFileDidLoad);
                                NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.httpFileDidFailedLoad);
                                this.imageReceiver.setImage(sendingMediaInfo.searchImage.imageUrl, null, null, "jpg", 1);
                                bitmap2 = bitmap;
                            }
                        }
                    }
                }
            }
            this.processBitmap(bitmap2);
        }
    }
    
    private void processBitmap(final Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        this.bigPhoto = ImageLoader.scaleAndSaveImage(bitmap, 800.0f, 800.0f, 80, false, 320, 320);
        this.smallPhoto = ImageLoader.scaleAndSaveImage(bitmap, 150.0f, 150.0f, 80, false, 150, 150);
        final TLRPC.PhotoSize smallPhoto = this.smallPhoto;
        if (smallPhoto != null) {
            try {
                final Bitmap decodeFile = BitmapFactory.decodeFile(FileLoader.getPathToAttach(smallPhoto, true).getAbsolutePath());
                final StringBuilder sb = new StringBuilder();
                sb.append(this.smallPhoto.location.volume_id);
                sb.append("_");
                sb.append(this.smallPhoto.location.local_id);
                sb.append("@50_50");
                ImageLoader.getInstance().putImageToCache(new BitmapDrawable(decodeFile), sb.toString());
            }
            catch (Throwable t) {}
        }
        bitmap.recycle();
        if (this.bigPhoto != null) {
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(FileLoader.getDirectory(4));
            sb2.append("/");
            sb2.append(this.bigPhoto.location.volume_id);
            sb2.append("_");
            sb2.append(this.bigPhoto.location.local_id);
            sb2.append(".jpg");
            this.uploadingImage = sb2.toString();
            if (this.uploadAfterSelect) {
                NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileDidUpload);
                NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileDidFailUpload);
                FileLoader.getInstance(this.currentAccount).uploadFile(this.uploadingImage, false, true, 16777216);
            }
            final ImageUpdaterDelegate delegate = this.delegate;
            if (delegate != null) {
                delegate.didUploadPhoto(null, this.bigPhoto, this.smallPhoto);
            }
        }
    }
    
    private void startCrop(final String s, final Uri uri) {
        try {
            final LaunchActivity launchActivity = (LaunchActivity)this.parentFragment.getParentActivity();
            if (launchActivity == null) {
                return;
            }
            final Bundle bundle = new Bundle();
            if (s != null) {
                bundle.putString("photoPath", s);
            }
            else if (uri != null) {
                bundle.putParcelable("photoUri", (Parcelable)uri);
            }
            final PhotoCropActivity photoCropActivity = new PhotoCropActivity(bundle);
            photoCropActivity.setDelegate((PhotoCropActivity.PhotoEditActivityDelegate)this);
            launchActivity.presentFragment(photoCropActivity);
        }
        catch (Exception ex) {
            FileLog.e(ex);
            this.processBitmap(ImageLoader.loadBitmap(s, uri, 800.0f, 800.0f, true));
        }
    }
    
    public void clear() {
        if (this.uploadingImage != null) {
            this.clearAfterUpdate = true;
        }
        else {
            this.parentFragment = null;
            this.delegate = null;
        }
    }
    
    @Override
    public void didFinishEdit(final Bitmap bitmap) {
        this.processBitmap(bitmap);
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.FileDidUpload) {
            if (((String)array[0]).equals(this.uploadingImage)) {
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileDidUpload);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileDidFailUpload);
                final ImageUpdaterDelegate delegate = this.delegate;
                if (delegate != null) {
                    delegate.didUploadPhoto((TLRPC.InputFile)array[1], this.bigPhoto, this.smallPhoto);
                }
                this.uploadingImage = null;
                if (this.clearAfterUpdate) {
                    this.imageReceiver.setImageBitmap((Drawable)null);
                    this.parentFragment = null;
                    this.delegate = null;
                }
            }
        }
        else if (n == NotificationCenter.FileDidFailUpload) {
            if (((String)array[0]).equals(this.uploadingImage)) {
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileDidUpload);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileDidFailUpload);
                this.uploadingImage = null;
                if (this.clearAfterUpdate) {
                    this.imageReceiver.setImageBitmap((Drawable)null);
                    this.parentFragment = null;
                    this.delegate = null;
                }
            }
        }
        else if ((n == NotificationCenter.fileDidLoad || n == NotificationCenter.fileDidFailedLoad || n == NotificationCenter.httpFileDidLoad || n == NotificationCenter.httpFileDidFailedLoad) && ((String)array[0]).equals(this.uploadingImage)) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileDidLoad);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileDidFailedLoad);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.httpFileDidLoad);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.httpFileDidFailedLoad);
            this.uploadingImage = null;
            if (n != NotificationCenter.fileDidLoad && n != NotificationCenter.httpFileDidLoad) {
                this.imageReceiver.setImageBitmap((Drawable)null);
            }
            else {
                this.processBitmap(ImageLoader.loadBitmap(this.finalPath, null, 800.0f, 800.0f, true));
            }
        }
    }
    
    public void onActivityResult(int n, int attributeInt, final Intent intent) {
        if (attributeInt == -1) {
            if (n == 13) {
                PhotoViewer.getInstance().setParentActivity(this.parentFragment.getParentActivity());
                n = 0;
                try {
                    attributeInt = new ExifInterface(this.currentPicturePath).getAttributeInt("Orientation", 1);
                    if (attributeInt != 3) {
                        if (attributeInt != 6) {
                            if (attributeInt == 8) {
                                n = 270;
                            }
                        }
                        else {
                            n = 90;
                        }
                    }
                    else {
                        n = 180;
                    }
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                    n = 0;
                }
                final ArrayList<Object> list = new ArrayList<Object>();
                list.add(new MediaController.PhotoEntry(0, 0, 0L, this.currentPicturePath, n, false));
                PhotoViewer.getInstance().openPhotoForSelect(list, 0, 1, (PhotoViewer.PhotoViewerProvider)new PhotoViewer.EmptyPhotoViewerProvider() {
                    @Override
                    public boolean allowCaption() {
                        return false;
                    }
                    
                    @Override
                    public boolean canScrollAway() {
                        return false;
                    }
                    
                    @Override
                    public void sendButtonPressed(final int n, final VideoEditedInfo videoEditedInfo) {
                        final MediaController.PhotoEntry photoEntry = list.get(0);
                        String s = photoEntry.imagePath;
                        if (s == null) {
                            s = photoEntry.path;
                            if (s == null) {
                                s = null;
                            }
                        }
                        ImageUpdater.this.processBitmap(ImageLoader.loadBitmap(s, null, 800.0f, 800.0f, true));
                    }
                }, null);
                AndroidUtilities.addMediaToGallery(this.currentPicturePath);
                this.currentPicturePath = null;
            }
            else if (n == 14 && intent != null) {
                if (intent.getData() != null) {
                    this.startCrop(null, intent.getData());
                }
            }
        }
    }
    
    public void openCamera() {
        final BaseFragment parentFragment = this.parentFragment;
        if (parentFragment != null) {
            if (parentFragment.getParentActivity() != null) {
                try {
                    if (Build$VERSION.SDK_INT >= 23 && this.parentFragment.getParentActivity().checkSelfPermission("android.permission.CAMERA") != 0) {
                        this.parentFragment.getParentActivity().requestPermissions(new String[] { "android.permission.CAMERA" }, 19);
                        return;
                    }
                    final Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    final File generatePicturePath = AndroidUtilities.generatePicturePath();
                    if (generatePicturePath != null) {
                        if (Build$VERSION.SDK_INT >= 24) {
                            intent.putExtra("output", (Parcelable)FileProvider.getUriForFile((Context)this.parentFragment.getParentActivity(), "org.telegram.messenger.provider", generatePicturePath));
                            intent.addFlags(2);
                            intent.addFlags(1);
                        }
                        else {
                            intent.putExtra("output", (Parcelable)Uri.fromFile(generatePicturePath));
                        }
                        this.currentPicturePath = generatePicturePath.getAbsolutePath();
                    }
                    this.parentFragment.startActivityForResult(intent, 13);
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
        }
    }
    
    public void openGallery() {
        final BaseFragment parentFragment = this.parentFragment;
        if (parentFragment == null) {
            return;
        }
        if (Build$VERSION.SDK_INT >= 23 && parentFragment != null && parentFragment.getParentActivity() != null && this.parentFragment.getParentActivity().checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
            this.parentFragment.getParentActivity().requestPermissions(new String[] { "android.permission.READ_EXTERNAL_STORAGE" }, 4);
            return;
        }
        final PhotoAlbumPickerActivity photoAlbumPickerActivity = new PhotoAlbumPickerActivity(1, false, false, null);
        photoAlbumPickerActivity.setDelegate((PhotoAlbumPickerActivity.PhotoAlbumPickerActivityDelegate)new PhotoAlbumPickerActivity.PhotoAlbumPickerActivityDelegate() {
            @Override
            public void didSelectPhotos(final ArrayList<SendMessagesHelper.SendingMediaInfo> list) {
                ImageUpdater.this.didSelectPhotos(list);
            }
            
            @Override
            public void startPhotoSelectActivity() {
                try {
                    final Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    ImageUpdater.this.parentFragment.startActivityForResult(intent, 14);
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
        });
        this.parentFragment.presentFragment(photoAlbumPickerActivity);
    }
    
    public void openMenu(final boolean b, final Runnable runnable) {
        final BaseFragment parentFragment = this.parentFragment;
        if (parentFragment != null) {
            if (parentFragment.getParentActivity() != null) {
                final BottomSheet.Builder builder = new BottomSheet.Builder((Context)this.parentFragment.getParentActivity());
                builder.setTitle(LocaleController.getString("ChoosePhoto", 2131559091));
                final boolean searchAvailable = this.searchAvailable;
                int n = 3;
                CharSequence[] array;
                int[] array2;
                if (searchAvailable) {
                    if (b) {
                        array = new CharSequence[] { LocaleController.getString("ChooseTakePhoto", 2131559097), LocaleController.getString("ChooseFromGallery", 2131559087), LocaleController.getString("ChooseFromSearch", 2131559088), LocaleController.getString("DeletePhoto", 2131559256) };
                        final int[] array3;
                        array2 = (array3 = new int[4]);
                        array3[0] = 2131165571;
                        array3[1] = 2131165792;
                        array3[2] = 2131165593;
                        array3[3] = 2131165348;
                    }
                    else {
                        array = new CharSequence[] { LocaleController.getString("ChooseTakePhoto", 2131559097), LocaleController.getString("ChooseFromGallery", 2131559087), LocaleController.getString("ChooseFromSearch", 2131559088) };
                        final int[] array4;
                        array2 = (array4 = new int[3]);
                        array4[0] = 2131165571;
                        array4[1] = 2131165792;
                        array4[2] = 2131165593;
                    }
                }
                else if (b) {
                    array = new CharSequence[] { LocaleController.getString("ChooseTakePhoto", 2131559097), LocaleController.getString("ChooseFromGallery", 2131559087), LocaleController.getString("DeletePhoto", 2131559256) };
                    final int[] array5;
                    array2 = (array5 = new int[3]);
                    array5[0] = 2131165571;
                    array5[1] = 2131165792;
                    array5[2] = 2131165348;
                }
                else {
                    array = new CharSequence[] { LocaleController.getString("ChooseTakePhoto", 2131559097), LocaleController.getString("ChooseFromGallery", 2131559087) };
                    final int[] array6;
                    array2 = (array6 = new int[2]);
                    array6[0] = 2131165571;
                    array6[1] = 2131165792;
                }
                builder.setItems(array, array2, (DialogInterface$OnClickListener)new _$$Lambda$ImageUpdater$ZfeU_OSr8fUwIgo0j9MadtpPC64(this, runnable));
                final BottomSheet create = builder.create();
                this.parentFragment.showDialog(create);
                final TextView titleView = create.getTitleView();
                if (titleView != null) {
                    titleView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                    titleView.setTextSize(1, 18.0f);
                    titleView.setTextColor(Theme.getColor("dialogTextBlack"));
                }
                if (!this.searchAvailable) {
                    n = 2;
                }
                create.setItemColor(n, Theme.getColor("dialogTextRed2"), Theme.getColor("dialogRedIcon"));
            }
        }
    }
    
    public void openSearch() {
        if (this.parentFragment == null) {
            return;
        }
        final HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        final ArrayList<Object> list = new ArrayList<Object>();
        final PhotoPickerActivity photoPickerActivity = new PhotoPickerActivity(0, null, hashMap, list, new ArrayList<MediaController.SearchImage>(), 1, false, null);
        photoPickerActivity.setDelegate((PhotoPickerActivity.PhotoPickerActivityDelegate)new PhotoPickerActivity.PhotoPickerActivityDelegate() {
            private boolean sendPressed;
            
            private void sendSelectedPhotos(final HashMap<Object, Object> hashMap, final ArrayList<Object> list) {
            }
            
            @Override
            public void actionButtonPressed(final boolean b) {
                if (!hashMap.isEmpty() && ImageUpdater.this.delegate != null && !this.sendPressed) {
                    if (!b) {
                        this.sendPressed = true;
                        final ArrayList<SendMessagesHelper.SendingMediaInfo> list = new ArrayList<SendMessagesHelper.SendingMediaInfo>();
                        for (int i = 0; i < list.size(); ++i) {
                            final Object value = hashMap.get(list.get(i));
                            final SendMessagesHelper.SendingMediaInfo e = new SendMessagesHelper.SendingMediaInfo();
                            list.add(e);
                            if (value instanceof MediaController.SearchImage) {
                                final MediaController.SearchImage searchImage = (MediaController.SearchImage)value;
                                final String imagePath = searchImage.imagePath;
                                if (imagePath != null) {
                                    e.path = imagePath;
                                }
                                else {
                                    e.searchImage = searchImage;
                                }
                                final CharSequence caption = searchImage.caption;
                                final ArrayList<TLRPC.InputDocument> list2 = null;
                                String string;
                                if (caption != null) {
                                    string = caption.toString();
                                }
                                else {
                                    string = null;
                                }
                                e.caption = string;
                                e.entities = searchImage.entities;
                                ArrayList<TLRPC.InputDocument> masks = list2;
                                if (!searchImage.stickers.isEmpty()) {
                                    masks = new ArrayList<TLRPC.InputDocument>(searchImage.stickers);
                                }
                                e.masks = masks;
                                e.ttl = searchImage.ttl;
                            }
                        }
                        ImageUpdater.this.didSelectPhotos(list);
                    }
                }
            }
            
            @Override
            public void selectedPhotosChanged() {
            }
        });
        photoPickerActivity.setInitialSearchString(this.delegate.getInitialSearchString());
        this.parentFragment.presentFragment(photoPickerActivity);
    }
    
    public void setSearchAvailable(final boolean searchAvailable) {
        this.searchAvailable = searchAvailable;
    }
    
    public void setUploadAfterSelect(final boolean uploadAfterSelect) {
        this.uploadAfterSelect = uploadAfterSelect;
    }
    
    public interface ImageUpdaterDelegate
    {
        void didUploadPhoto(final TLRPC.InputFile p0, final TLRPC.PhotoSize p1, final TLRPC.PhotoSize p2);
        
        String getInitialSearchString();
    }
}
