package org.mozilla.focus.screenshot;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore.Images.Media;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import java.io.File;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import org.mozilla.focus.activity.BaseActivity;
import org.mozilla.focus.provider.QueryHandler;
import org.mozilla.focus.screenshot.model.ImageInfo;
import org.mozilla.focus.screenshot.model.Screenshot;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.permissionhandler.PermissionHandle;
import org.mozilla.permissionhandler.PermissionHandler;
import org.mozilla.rocket.content.HomeFragmentViewState;
import org.mozilla.threadutils.ThreadUtils;

public class ScreenshotViewerActivity extends BaseActivity implements OnClickListener, QueryHandler.AsyncDeleteListener {
   private Toolbar mBottomToolBar;
   private ImageView mImgPlaceholder;
   private SubsamplingScaleImageView mImgScreenshot;
   private ArrayList mInfoItems = new ArrayList();
   private boolean mIsImageReady = false;
   private ProgressBar mProgressBar;
   private Screenshot mScreenshot;
   private SubsamplingScaleImageView.OnImageEventListener onImageEventListener = new SubsamplingScaleImageView.OnImageEventListener() {
      public void onImageLoadError(Exception var1) {
         ScreenshotViewerActivity.this.hideProgressBar();
      }

      public void onImageLoaded() {
      }

      public void onPreviewLoadError(Exception var1) {
      }

      public void onPreviewReleased() {
      }

      public void onReady() {
         ScreenshotViewerActivity.this.hideProgressBar();
      }

      public void onTileLoadError(Exception var1) {
      }
   };
   private PermissionHandler permissionHandler;

   public static String getFileSizeText(long var0) {
      DecimalFormat var2 = new DecimalFormat("0.00");
      float var3 = (float)var0;
      StringBuilder var4;
      if (var3 < 1048576.0F) {
         var4 = new StringBuilder();
         var4.append(var2.format((double)(var3 / 1024.0F)));
         var4.append(" KB");
         return var4.toString();
      } else if (var3 < 1.07374182E9F) {
         var4 = new StringBuilder();
         var4.append(var2.format((double)(var3 / 1048576.0F)));
         var4.append(" MB");
         return var4.toString();
      } else if (var3 < 1.09951163E12F) {
         var4 = new StringBuilder();
         var4.append(var2.format((double)(var3 / 1.07374182E9F)));
         var4.append(" GB");
         return var4.toString();
      } else {
         return "";
      }
   }

   public static final void goScreenshotViewerActivityOnResult(Activity var0, Screenshot var1) {
      Intent var2 = new Intent(var0, ScreenshotViewerActivity.class);
      var2.putExtra("extra_screenshot", var1);
      var0.startActivityForResult(var2, 1000);
   }

   private void hideProgressBar() {
      this.mIsImageReady = true;
      if (this.mProgressBar != null && this.mProgressBar.isShown()) {
         this.mProgressBar.setVisibility(8);
      }

   }

   private void initInfoItemArray() {
      this.mInfoItems.clear();
      this.mInfoItems.add(new ImageInfo(this.getString(2131755385, new Object[]{""})));
      this.mInfoItems.add(new ImageInfo(this.getString(2131755383, new Object[]{""})));
      this.mInfoItems.add(new ImageInfo(this.getString(2131755384, new Object[]{""})));
      this.mInfoItems.add(new ImageInfo(this.getString(2131755386, new Object[]{""})));
      this.mInfoItems.add(new ImageInfo(this.getString(2131755382, new Object[]{""})));
      this.mInfoItems.add(new ImageInfo(this.getString(2131755387, new Object[]{""})));
   }

   private void initScreenshotInfo(boolean var1) {
      if (this.mScreenshot != null) {
         (new ScreenshotViewerActivity.ScreenshotInfoTask(this, this.mScreenshot, this.mInfoItems, var1)).execute(new Void[0]);
         ThreadUtils.postToBackgroundThread((Runnable)(new _$$Lambda$ScreenshotViewerActivity$EX4pVRu55Asmujhv6gJW3gkQsN8(this)));
      }

   }

   // $FF: synthetic method
   public static void lambda$initScreenshotInfo$0(ScreenshotViewerActivity var0) {
      var0.mScreenshot.setCategory(ScreenshotManager.getInstance().getCategory(var0, var0.mScreenshot.getUrl()));
      var0.mScreenshot.setCategoryVersion(ScreenshotManager.getInstance().getCategoryVersion());
   }

   private void onDeleteClick() {
      Builder var1 = new Builder(this, 2131820546);
      var1.setMessage(2131755381);
      var1.setPositiveButton(2131755079, new android.content.DialogInterface.OnClickListener() {
         public void onClick(DialogInterface var1, int var2) {
            TelemetryWrapper.deleteCaptureImage(ScreenshotViewerActivity.this.mScreenshot.getCategory(), ScreenshotViewerActivity.this.mScreenshot.getCategoryVersion());
            ScreenshotViewerActivity.this.proceedDelete();
         }
      });
      var1.setNegativeButton(2131755060, (android.content.DialogInterface.OnClickListener)null);
      var1.create().show();
   }

   private void onEditClick() {
      ThreadUtils.postToBackgroundThread(new Runnable() {
         public void run() {
            ContentResolver var1 = ScreenshotViewerActivity.this.getContentResolver();
            Uri var2 = Media.EXTERNAL_CONTENT_URI;
            String var3 = ScreenshotViewerActivity.this.mScreenshot.getImageUri();
            Cursor var7 = var1.query(var2, new String[]{"_id"}, "_data=?", new String[]{var3}, (String)null);
            if (var7 != null && var7.moveToFirst()) {
               int var4 = var7.getInt(var7.getColumnIndex("_id"));
               var7.close();
               Uri var6 = Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI, String.valueOf(var4));
               Intent var8 = new Intent("android.intent.action.EDIT");
               var8.setDataAndType(var6, "image/*");
               var8.addFlags(1);

               try {
                  ScreenshotViewerActivity.this.startActivityForResult(Intent.createChooser(var8, (CharSequence)null), 102);
                  TelemetryWrapper.editCaptureImage(true, ScreenshotViewerActivity.this.mScreenshot.getCategory(), ScreenshotViewerActivity.this.mScreenshot.getCategoryVersion());
               } catch (ActivityNotFoundException var5) {
                  TelemetryWrapper.editCaptureImage(false, ScreenshotViewerActivity.this.mScreenshot.getCategory(), ScreenshotViewerActivity.this.mScreenshot.getCategoryVersion());
               }
            }

         }
      });
   }

   private void onInfoClick() {
      Builder var1 = new Builder(this, 2131820754);
      var1.setTitle(2131755388);
      var1.setAdapter(new ScreenshotViewerActivity.InfoItemAdapter(this, this.mInfoItems), (android.content.DialogInterface.OnClickListener)null);
      var1.setPositiveButton(2131755061, (android.content.DialogInterface.OnClickListener)null);
      AlertDialog var2 = var1.create();
      if (var2.getListView() != null) {
         var2.getListView().setSelector(17170445);
      }

      var2.show();
   }

   private void onShareClick() {
      if (this.mImgScreenshot.isImageLoaded()) {
         ThreadUtils.postToBackgroundThread(new Runnable() {
            public void run() {
               ContentResolver var1 = ScreenshotViewerActivity.this.getContentResolver();
               Uri var2 = Media.EXTERNAL_CONTENT_URI;
               String var3 = ScreenshotViewerActivity.this.mScreenshot.getImageUri();
               Cursor var6 = var1.query(var2, new String[]{"_id"}, "_data=?", new String[]{var3}, (String)null);
               if (var6 != null && var6.moveToFirst()) {
                  int var4 = var6.getInt(var6.getColumnIndex("_id"));
                  var6.close();
                  Uri var7 = Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI, String.valueOf(var4));
                  Intent var8 = new Intent("android.intent.action.SEND");
                  var8.putExtra("android.intent.extra.STREAM", var7);
                  var8.setType("image/*");
                  var8.addFlags(1);

                  try {
                     ScreenshotViewerActivity.this.startActivity(Intent.createChooser(var8, (CharSequence)null));
                     TelemetryWrapper.shareCaptureImage(false, ScreenshotViewerActivity.this.mScreenshot.getCategory(), ScreenshotViewerActivity.this.mScreenshot.getCategoryVersion());
                  } catch (ActivityNotFoundException var5) {
                  }
               }

            }
         });
      } else {
         this.setupView(true);
         this.initScreenshotInfo(true);
      }

   }

   private void proceedDelete() {
      if (this.mScreenshot != null) {
         ThreadUtils.postToBackgroundThread(new Runnable() {
            public void run() {
               File var1 = new File(ScreenshotViewerActivity.this.mScreenshot.getImageUri());
               if (var1.exists()) {
                  try {
                     var1.delete();
                  } catch (Exception var2) {
                  }
               }

            }
         });
         ScreenshotManager.getInstance().delete(this.mScreenshot.getId(), this);
      }

   }

   private void setupView(boolean var1) {
      ImageView var2 = this.mImgPlaceholder;
      byte var3 = 0;
      byte var4;
      if (var1) {
         var4 = 8;
      } else {
         var4 = 0;
      }

      var2.setVisibility(var4);
      SubsamplingScaleImageView var5 = this.mImgScreenshot;
      if (var1) {
         var4 = var3;
      } else {
         var4 = 8;
      }

      var5.setVisibility(var4);
      this.findViewById(2131296614).setEnabled(var1);
      this.findViewById(2131296612).setEnabled(var1);
      this.findViewById(2131296615).setEnabled(var1);
      this.findViewById(2131296613).setEnabled(var1);
   }

   private void showProgressBar(long var1) {
      (new Handler()).postDelayed(new Runnable() {
         public void run() {
            if (!ScreenshotViewerActivity.this.mIsImageReady && ScreenshotViewerActivity.this.mProgressBar != null) {
               ScreenshotViewerActivity.this.mProgressBar.setVisibility(0);
            }

         }
      }, var1);
   }

   public void applyLocale() {
   }

   protected void onActivityResult(int var1, int var2, Intent var3) {
      super.onActivityResult(var1, var2, var3);
      if (var2 == 0 && var1 == 102) {
         this.setupView(true);
         this.initScreenshotInfo(false);
      }

      this.permissionHandler.onActivityResult(this, var1, var2, var3);
   }

   public void onClick(View var1) {
      switch(var1.getId()) {
      case 2131296611:
         this.permissionHandler.tryAction((Activity)this, "android.permission.READ_EXTERNAL_STORAGE", 3, (Parcelable)null);
         break;
      case 2131296612:
         this.permissionHandler.tryAction((Activity)this, "android.permission.READ_EXTERNAL_STORAGE", 1, (Parcelable)null);
         break;
      case 2131296613:
         TelemetryWrapper.showCaptureInfo(this.mScreenshot.getCategory(), this.mScreenshot.getCategoryVersion());
         this.onInfoClick();
         break;
      case 2131296614:
         TelemetryWrapper.openCaptureLink(this.mScreenshot.getCategory(), this.mScreenshot.getCategoryVersion());
         HomeFragmentViewState.reset();
         Intent var4 = new Intent();
         var4.putExtra("extra_url", this.mScreenshot.getUrl());
         this.setResult(101, var4);
         this.finish();
         break;
      case 2131296615:
         this.permissionHandler.tryAction((Activity)this, "android.permission.READ_EXTERNAL_STORAGE", 2, (Parcelable)null);
         break;
      case 2131296616:
         Toolbar var3 = this.mBottomToolBar;
         byte var2;
         if (this.mBottomToolBar.getVisibility() == 0) {
            var2 = 8;
         } else {
            var2 = 0;
         }

         var3.setVisibility(var2);
      }

   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.permissionHandler = new PermissionHandler(new PermissionHandle() {
         private void doAction(int var1) {
            switch(var1) {
            case 0:
               this.viewScreenshot();
               break;
            case 1:
               ScreenshotViewerActivity.this.onEditClick();
               break;
            case 2:
               ScreenshotViewerActivity.this.onShareClick();
               break;
            case 3:
               ScreenshotViewerActivity.this.onDeleteClick();
               break;
            default:
               throw new IllegalArgumentException("Unknown Action");
            }

         }

         private void viewScreenshot() {
            ScreenshotViewerActivity.this.setupView(true);
            ScreenshotViewerActivity.this.initScreenshotInfo(false);
         }

         public void doActionDirect(String var1, int var2, Parcelable var3) {
            this.doAction(var2);
         }

         public void doActionGranted(String var1, int var2, Parcelable var3) {
            this.doAction(var2);
         }

         public void doActionNoPermission(String var1, int var2, Parcelable var3) {
         }

         public void doActionSetting(String var1, int var2, Parcelable var3) {
            this.doAction(var2);
         }

         public Snackbar makeAskAgainSnackBar(int var1) {
            return PermissionHandler.makeAskAgainSnackBar((Activity)ScreenshotViewerActivity.this, ScreenshotViewerActivity.this.findViewById(2131296594), 2131755291);
         }

         public void permissionDeniedToast(int var1) {
            Toast.makeText(ScreenshotViewerActivity.this, 2131755292, 1).show();
         }

         public void requestPermissions(int var1) {
            ActivityCompat.requestPermissions(ScreenshotViewerActivity.this, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, var1);
         }
      });
      this.setContentView(2131492896);
      this.mImgPlaceholder = (ImageView)this.findViewById(2131296617);
      this.mImgScreenshot = (SubsamplingScaleImageView)this.findViewById(2131296616);
      this.mImgScreenshot.setPanLimit(1);
      this.mImgScreenshot.setMinimumScaleType(3);
      this.mImgScreenshot.setMinScale(1.0F);
      this.mImgScreenshot.setOnClickListener(this);
      this.mImgScreenshot.setOnImageEventListener(this.onImageEventListener);
      this.mProgressBar = (ProgressBar)this.findViewById(2131296609);
      this.mBottomToolBar = (Toolbar)this.findViewById(2131296610);
      this.findViewById(2131296614).setOnClickListener(this);
      this.findViewById(2131296612).setOnClickListener(this);
      this.findViewById(2131296615).setOnClickListener(this);
      this.findViewById(2131296613).setOnClickListener(this);
      this.findViewById(2131296611).setOnClickListener(this);
      this.mScreenshot = (Screenshot)this.getIntent().getSerializableExtra("extra_screenshot");
      this.initInfoItemArray();
      if (this.mScreenshot != null) {
         this.permissionHandler.tryAction((Activity)this, "android.permission.READ_EXTERNAL_STORAGE", 0, (Parcelable)null);
      } else {
         this.finish();
      }

   }

   public void onDeleteComplete(int var1, long var2) {
      if (var1 > 0) {
         Intent var4 = new Intent();
         var4.putExtra("extra_screenshot_item_id", var2);
         this.setResult(100, var4);
         this.finish();
      }

   }

   public void onRequestPermissionsResult(int var1, String[] var2, int[] var3) {
      super.onRequestPermissionsResult(var1, var2, var3);
      this.permissionHandler.onRequestPermissionsResult(this, var1, var2, var3);
   }

   public void onRestoreInstanceState(Bundle var1) {
      this.permissionHandler.onRestoreInstanceState(var1);
   }

   public void onSaveInstanceState(Bundle var1) {
      this.permissionHandler.onSaveInstanceState(var1);
      super.onSaveInstanceState(var1);
   }

   private static class InfoItemAdapter extends ArrayAdapter {
      public InfoItemAdapter(Context var1, ArrayList var2) {
         super(var1, 0, var2);
      }

      public View getView(int var1, View var2, ViewGroup var3) {
         ImageInfo var4 = (ImageInfo)this.getItem(var1);
         View var5 = var2;
         if (var2 == null) {
            var5 = LayoutInflater.from(this.getContext()).inflate(2131492982, var3, false);
         }

         ((TextView)var5.findViewById(2131296607)).setText(var4.title);
         if (var1 == 4) {
            var5.setOnLongClickListener(new OnLongClickListener() {
               public boolean onLongClick(View var1) {
                  return false;
               }
            });
         }

         return var5;
      }
   }

   private static class ScreenshotInfoTask extends AsyncTask {
      private final WeakReference activityRef;
      private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
      private String fileSizeText;
      private int height;
      private ImageSource imageSource;
      private final ArrayList infoItems;
      private final Screenshot screenshot;
      private int width;
      private final boolean withShare;

      public ScreenshotInfoTask(ScreenshotViewerActivity var1, Screenshot var2, ArrayList var3, boolean var4) {
         this.activityRef = new WeakReference(var1);
         this.screenshot = var2;
         this.infoItems = var3;
         this.withShare = var4;
      }

      protected Void doInBackground(Void... var1) {
         ScreenshotViewerActivity var3 = (ScreenshotViewerActivity)this.activityRef.get();
         if (var3 != null && !var3.isFinishing() && !var3.isDestroyed()) {
            File var2 = new File(this.screenshot.getImageUri());
            if (var2.exists()) {
               Options var4 = new Options();
               var4.inJustDecodeBounds = true;
               BitmapFactory.decodeFile(this.screenshot.getImageUri(), var4);
               this.width = var4.outWidth;
               this.height = var4.outHeight;
               this.imageSource = ImageSource.uri(Uri.fromFile(new File(this.screenshot.getImageUri())));
               this.fileSizeText = ScreenshotViewerActivity.getFileSizeText(var2.length());
            }

            return null;
         } else {
            return null;
         }
      }

      protected void onPostExecute(Void var1) {
         ScreenshotViewerActivity var3 = (ScreenshotViewerActivity)this.activityRef.get();
         if (var3 != null && !var3.isFinishing() && !var3.isDestroyed()) {
            if (this.screenshot.getTimestamp() > 0L) {
               Calendar var2 = Calendar.getInstance();
               var2.setTimeInMillis(this.screenshot.getTimestamp());
               ((ImageInfo)this.infoItems.get(0)).title = var3.getString(2131755385, new Object[]{this.dateFormat.format(var2.getTime())});
            }

            ((ImageInfo)this.infoItems.get(1)).title = var3.getString(2131755383, new Object[]{String.format(Locale.getDefault(), "%dx%d", this.width, this.height)});
            ((ImageInfo)this.infoItems.get(2)).title = var3.getString(2131755384, new Object[]{this.fileSizeText});
            ((ImageInfo)this.infoItems.get(3)).title = var3.getString(2131755386, new Object[]{this.screenshot.getTitle()});
            ((ImageInfo)this.infoItems.get(4)).title = var3.getString(2131755382, new Object[]{this.screenshot.getCategory()});
            ((ImageInfo)this.infoItems.get(5)).title = var3.getString(2131755387, new Object[]{this.screenshot.getUrl()});
            if (this.imageSource != null) {
               var3.mImgScreenshot.setImage(this.imageSource, ImageViewState.ALIGN_TOP);
               if (this.withShare) {
                  var3.onShareClick();
               }
            } else {
               var3.hideProgressBar();
               var3.setupView(false);
               Toast.makeText(var3, 2131755254, 1).show();
            }

         }
      }

      protected void onPreExecute() {
         ScreenshotViewerActivity var1 = (ScreenshotViewerActivity)this.activityRef.get();
         if (var1 != null) {
            var1.showProgressBar(700L);
         }

      }
   }
}
