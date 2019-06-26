// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.PhotoAlbumPickerActivity;
import android.os.Parcelable;
import android.content.Context;
import androidx.core.content.FileProvider;
import android.os.Build$VERSION;
import android.content.Intent;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import org.telegram.messenger.FileLog;
import java.io.OutputStream;
import java.io.FileOutputStream;
import android.graphics.Bitmap$CompressFormat;
import android.net.Uri;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.SendMessagesHelper;
import java.util.ArrayList;
import org.telegram.ui.ActionBar.BaseFragment;
import android.app.Activity;
import java.io.File;

public class WallpaperUpdater
{
    private String currentPicturePath;
    private File currentWallpaperPath;
    private WallpaperUpdaterDelegate delegate;
    private Activity parentActivity;
    private BaseFragment parentFragment;
    private File picturePath;
    
    public WallpaperUpdater(final Activity parentActivity, final BaseFragment parentFragment, final WallpaperUpdaterDelegate delegate) {
        this.picturePath = null;
        this.parentActivity = parentActivity;
        this.parentFragment = parentFragment;
        this.delegate = delegate;
    }
    
    private void didSelectPhotos(final ArrayList<SendMessagesHelper.SendingMediaInfo> list) {
        try {
            if (!list.isEmpty()) {
                final SendMessagesHelper.SendingMediaInfo sendingMediaInfo = list.get(0);
                if (sendingMediaInfo.path != null) {
                    final File directory = FileLoader.getDirectory(4);
                    final StringBuilder sb = new StringBuilder();
                    sb.append(Utilities.random.nextInt());
                    sb.append(".jpg");
                    this.currentWallpaperPath = new File(directory, sb.toString());
                    final Point realScreenSize = AndroidUtilities.getRealScreenSize();
                    final Bitmap loadBitmap = ImageLoader.loadBitmap(sendingMediaInfo.path, null, (float)realScreenSize.x, (float)realScreenSize.y, true);
                    loadBitmap.compress(Bitmap$CompressFormat.JPEG, 87, (OutputStream)new FileOutputStream(this.currentWallpaperPath));
                    this.delegate.didSelectWallpaper(this.currentWallpaperPath, loadBitmap, true);
                }
            }
        }
        catch (Throwable t) {
            FileLog.e(t);
        }
    }
    
    public void cleanup() {
    }
    
    public String getCurrentPicturePath() {
        return this.currentPicturePath;
    }
    
    public void onActivityResult(final int n, final int n2, Intent parent) {
        if (n2 == -1) {
            if (n == 10) {
                AndroidUtilities.addMediaToGallery(this.currentPicturePath);
                Exception ex5 = null;
                Label_0224: {
                    Bitmap loadBitmap = null;
                    Object o;
                    try {
                        parent = (Exception)FileLoader.getDirectory(4);
                        final StringBuilder sb = new StringBuilder();
                        sb.append(Utilities.random.nextInt());
                        sb.append(".jpg");
                        this.currentWallpaperPath = new File((File)parent, sb.toString());
                        parent = (Exception)AndroidUtilities.getRealScreenSize();
                        loadBitmap = ImageLoader.loadBitmap(this.currentPicturePath, null, (float)((Point)parent).x, (float)((Point)parent).y, true);
                        o = (parent = (Exception)new FileOutputStream(this.currentWallpaperPath));
                        try {
                            final Bitmap bitmap = loadBitmap;
                            final Bitmap$CompressFormat bitmap$CompressFormat = Bitmap$CompressFormat.JPEG;
                            final int n3 = 87;
                            final Object o2 = o;
                            bitmap.compress(bitmap$CompressFormat, n3, (OutputStream)o2);
                            parent = (Exception)o;
                            final WallpaperUpdater wallpaperUpdater = this;
                            final WallpaperUpdaterDelegate wallpaperUpdaterDelegate = wallpaperUpdater.delegate;
                            final WallpaperUpdater wallpaperUpdater2 = this;
                            final File file = wallpaperUpdater2.currentWallpaperPath;
                            final Bitmap bitmap2 = loadBitmap;
                            final boolean b = false;
                            wallpaperUpdaterDelegate.didSelectWallpaper(file, bitmap2, b);
                            final Object o3 = o;
                            ((FileOutputStream)o3).close();
                        }
                        catch (Exception ex2) {}
                    }
                    catch (Exception ex2) {
                        o = null;
                    }
                    finally {
                        final Exception ex;
                        parent = ex;
                        break Label_0224;
                    }
                    try {
                        final Bitmap bitmap = loadBitmap;
                        final Bitmap$CompressFormat bitmap$CompressFormat = Bitmap$CompressFormat.JPEG;
                        final int n3 = 87;
                        final Object o2 = o;
                        bitmap.compress(bitmap$CompressFormat, n3, (OutputStream)o2);
                        parent = (Exception)o;
                        final WallpaperUpdater wallpaperUpdater = this;
                        final WallpaperUpdaterDelegate wallpaperUpdaterDelegate = wallpaperUpdater.delegate;
                        final WallpaperUpdater wallpaperUpdater2 = this;
                        final File file = wallpaperUpdater2.currentWallpaperPath;
                        final Bitmap bitmap2 = loadBitmap;
                        final boolean b = false;
                        wallpaperUpdaterDelegate.didSelectWallpaper(file, bitmap2, b);
                        Label_0204: {
                            try {
                                final Object o3 = o;
                                ((FileOutputStream)o3).close();
                                break Label_0204;
                                while (true) {
                                    ((FileOutputStream)o).close();
                                    break Label_0204;
                                    parent = (Exception)o;
                                    final Exception ex2;
                                    FileLog.e(ex2);
                                    continue;
                                }
                            }
                            // iftrue(Label_0204:, o == null)
                            catch (Exception parent) {
                                FileLog.e(parent);
                            }
                        }
                        this.currentPicturePath = null;
                        return;
                    }
                    finally {
                        final Exception ex3 = parent;
                        final Exception ex4;
                        parent = ex4;
                        ex5 = ex3;
                    }
                }
                if (ex5 != null) {
                    try {
                        ((FileOutputStream)ex5).close();
                    }
                    catch (Exception ex6) {
                        FileLog.e(ex6);
                    }
                }
                throw parent;
            }
            if (n == 11 && parent != null) {
                if (((Intent)parent).getData() != null) {
                    try {
                        final File directory = FileLoader.getDirectory(4);
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(Utilities.random.nextInt());
                        sb2.append(".jpg");
                        this.currentWallpaperPath = new File(directory, sb2.toString());
                        final Point realScreenSize = AndroidUtilities.getRealScreenSize();
                        final Bitmap loadBitmap2 = ImageLoader.loadBitmap(null, ((Intent)parent).getData(), (float)realScreenSize.x, (float)realScreenSize.y, true);
                        loadBitmap2.compress(Bitmap$CompressFormat.JPEG, 87, (OutputStream)new FileOutputStream(this.currentWallpaperPath));
                        this.delegate.didSelectWallpaper(this.currentWallpaperPath, loadBitmap2, false);
                    }
                    catch (Exception ex7) {
                        FileLog.e(ex7);
                    }
                }
            }
        }
    }
    
    public void openGallery() {
        final BaseFragment parentFragment = this.parentFragment;
        if (parentFragment != null) {
            if (Build$VERSION.SDK_INT >= 23 && parentFragment.getParentActivity() != null && this.parentFragment.getParentActivity().checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
                this.parentFragment.getParentActivity().requestPermissions(new String[] { "android.permission.READ_EXTERNAL_STORAGE" }, 4);
                return;
            }
            final PhotoAlbumPickerActivity photoAlbumPickerActivity = new PhotoAlbumPickerActivity(2, false, false, null);
            photoAlbumPickerActivity.setAllowSearchImages(false);
            photoAlbumPickerActivity.setDelegate((PhotoAlbumPickerActivity.PhotoAlbumPickerActivityDelegate)new PhotoAlbumPickerActivity.PhotoAlbumPickerActivityDelegate() {
                @Override
                public void didSelectPhotos(final ArrayList<SendMessagesHelper.SendingMediaInfo> list) {
                    WallpaperUpdater.this.didSelectPhotos(list);
                }
                
                @Override
                public void startPhotoSelectActivity() {
                    try {
                        final Intent intent = new Intent("android.intent.action.PICK");
                        intent.setType("image/*");
                        WallpaperUpdater.this.parentActivity.startActivityForResult(intent, 11);
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
            });
            this.parentFragment.presentFragment(photoAlbumPickerActivity);
        }
        else {
            final Intent intent = new Intent("android.intent.action.PICK");
            intent.setType("image/*");
            this.parentActivity.startActivityForResult(intent, 11);
        }
    }
    
    public void setCurrentPicturePath(final String currentPicturePath) {
        this.currentPicturePath = currentPicturePath;
    }
    
    public void showAlert(final boolean b) {
        final BottomSheet.Builder builder = new BottomSheet.Builder((Context)this.parentActivity);
        builder.setTitle(LocaleController.getString("ChoosePhoto", 2131559091));
        CharSequence[] array;
        int[] array2;
        if (b) {
            array = new CharSequence[] { LocaleController.getString("ChooseTakePhoto", 2131559097), LocaleController.getString("SelectFromGallery", 2131560683), LocaleController.getString("SelectColor", 2131560678), LocaleController.getString("Default", 2131559225) };
            array2 = null;
        }
        else {
            array = new CharSequence[] { LocaleController.getString("ChooseTakePhoto", 2131559097), LocaleController.getString("SelectFromGallery", 2131560683) };
            final int[] array3;
            array2 = (array3 = new int[2]);
            array3[0] = 2131165571;
            array3[1] = 2131165792;
        }
        builder.setItems(array, array2, (DialogInterface$OnClickListener)new _$$Lambda$WallpaperUpdater$vK87huFk9jAjs7SXqPz2knBtx88(this, b));
        builder.show();
    }
    
    public interface WallpaperUpdaterDelegate
    {
        void didSelectWallpaper(final File p0, final Bitmap p1, final boolean p2);
        
        void needOpenColorPicker();
    }
}
