// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.screenshot;

import com.davemorrissey.labs.subscaleview.ImageViewState;
import java.util.Calendar;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory$Options;
import java.util.Locale;
import com.davemorrissey.labs.subscaleview.ImageSource;
import java.text.SimpleDateFormat;
import java.lang.ref.WeakReference;
import android.os.AsyncTask;
import android.view.View$OnLongClickListener;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;
import android.widget.ArrayAdapter;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;
import android.support.design.widget.Snackbar;
import org.mozilla.permissionhandler.PermissionHandle;
import android.os.Bundle;
import org.mozilla.rocket.content.HomeFragmentViewState;
import android.view.View;
import android.os.Handler;
import java.io.File;
import android.os.Parcelable;
import android.app.AlertDialog;
import android.widget.ListAdapter;
import android.database.Cursor;
import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.provider.MediaStore$Images$Media;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import org.mozilla.threadutils.ThreadUtils;
import java.io.Serializable;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import java.text.DecimalFormat;
import org.mozilla.permissionhandler.PermissionHandler;
import org.mozilla.focus.screenshot.model.Screenshot;
import android.widget.ProgressBar;
import org.mozilla.focus.screenshot.model.ImageInfo;
import java.util.ArrayList;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import org.mozilla.focus.provider.QueryHandler;
import android.view.View$OnClickListener;
import org.mozilla.focus.activity.BaseActivity;

public class ScreenshotViewerActivity extends BaseActivity implements View$OnClickListener, AsyncDeleteListener
{
    private Toolbar mBottomToolBar;
    private ImageView mImgPlaceholder;
    private SubsamplingScaleImageView mImgScreenshot;
    private ArrayList<ImageInfo> mInfoItems;
    private boolean mIsImageReady;
    private ProgressBar mProgressBar;
    private Screenshot mScreenshot;
    private SubsamplingScaleImageView.OnImageEventListener onImageEventListener;
    private PermissionHandler permissionHandler;
    
    public ScreenshotViewerActivity() {
        this.mInfoItems = new ArrayList<ImageInfo>();
        this.mIsImageReady = false;
        this.onImageEventListener = new SubsamplingScaleImageView.OnImageEventListener() {
            @Override
            public void onImageLoadError(final Exception ex) {
                ScreenshotViewerActivity.this.hideProgressBar();
            }
            
            @Override
            public void onImageLoaded() {
            }
            
            @Override
            public void onPreviewLoadError(final Exception ex) {
            }
            
            @Override
            public void onPreviewReleased() {
            }
            
            @Override
            public void onReady() {
                ScreenshotViewerActivity.this.hideProgressBar();
            }
            
            @Override
            public void onTileLoadError(final Exception ex) {
            }
        };
    }
    
    public static String getFileSizeText(final long n) {
        final DecimalFormat decimalFormat = new DecimalFormat("0.00");
        final float n2 = (float)n;
        if (n2 < 1048576.0f) {
            final StringBuilder sb = new StringBuilder();
            sb.append(decimalFormat.format(n2 / 1024.0f));
            sb.append(" KB");
            return sb.toString();
        }
        if (n2 < 1.07374182E9f) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(decimalFormat.format(n2 / 1048576.0f));
            sb2.append(" MB");
            return sb2.toString();
        }
        if (n2 < 1.09951163E12f) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(decimalFormat.format(n2 / 1.07374182E9f));
            sb3.append(" GB");
            return sb3.toString();
        }
        return "";
    }
    
    public static final void goScreenshotViewerActivityOnResult(final Activity activity, final Screenshot screenshot) {
        final Intent intent = new Intent((Context)activity, (Class)ScreenshotViewerActivity.class);
        intent.putExtra("extra_screenshot", (Serializable)screenshot);
        activity.startActivityForResult(intent, 1000);
    }
    
    private void hideProgressBar() {
        this.mIsImageReady = true;
        if (this.mProgressBar != null && this.mProgressBar.isShown()) {
            this.mProgressBar.setVisibility(8);
        }
    }
    
    private void initInfoItemArray() {
        this.mInfoItems.clear();
        this.mInfoItems.add(new ImageInfo(this.getString(2131755385, new Object[] { "" })));
        this.mInfoItems.add(new ImageInfo(this.getString(2131755383, new Object[] { "" })));
        this.mInfoItems.add(new ImageInfo(this.getString(2131755384, new Object[] { "" })));
        this.mInfoItems.add(new ImageInfo(this.getString(2131755386, new Object[] { "" })));
        this.mInfoItems.add(new ImageInfo(this.getString(2131755382, new Object[] { "" })));
        this.mInfoItems.add(new ImageInfo(this.getString(2131755387, new Object[] { "" })));
    }
    
    private void initScreenshotInfo(final boolean b) {
        if (this.mScreenshot != null) {
            new ScreenshotInfoTask(this, this.mScreenshot, this.mInfoItems, b).execute((Object[])new Void[0]);
            ThreadUtils.postToBackgroundThread(new _$$Lambda$ScreenshotViewerActivity$EX4pVRu55Asmujhv6gJW3gkQsN8(this));
        }
    }
    
    private void onDeleteClick() {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this, 2131820546);
        alertDialog$Builder.setMessage(2131755381);
        alertDialog$Builder.setPositiveButton(2131755079, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                TelemetryWrapper.deleteCaptureImage(ScreenshotViewerActivity.this.mScreenshot.getCategory(), ScreenshotViewerActivity.this.mScreenshot.getCategoryVersion());
                ScreenshotViewerActivity.this.proceedDelete();
            }
        });
        alertDialog$Builder.setNegativeButton(2131755060, (DialogInterface$OnClickListener)null);
        alertDialog$Builder.create().show();
    }
    
    private void onEditClick() {
        ThreadUtils.postToBackgroundThread(new Runnable() {
            @Override
            public void run() {
                final Cursor query = ScreenshotViewerActivity.this.getContentResolver().query(MediaStore$Images$Media.EXTERNAL_CONTENT_URI, new String[] { "_id" }, "_data=?", new String[] { ScreenshotViewerActivity.this.mScreenshot.getImageUri() }, (String)null);
                if (query != null && query.moveToFirst()) {
                    final int int1 = query.getInt(query.getColumnIndex("_id"));
                    query.close();
                    final Uri withAppendedPath = Uri.withAppendedPath(MediaStore$Images$Media.EXTERNAL_CONTENT_URI, String.valueOf(int1));
                    final Intent intent = new Intent("android.intent.action.EDIT");
                    intent.setDataAndType(withAppendedPath, "image/*");
                    intent.addFlags(1);
                    try {
                        ScreenshotViewerActivity.this.startActivityForResult(Intent.createChooser(intent, (CharSequence)null), 102);
                        TelemetryWrapper.editCaptureImage(true, ScreenshotViewerActivity.this.mScreenshot.getCategory(), ScreenshotViewerActivity.this.mScreenshot.getCategoryVersion());
                    }
                    catch (ActivityNotFoundException ex) {
                        TelemetryWrapper.editCaptureImage(false, ScreenshotViewerActivity.this.mScreenshot.getCategory(), ScreenshotViewerActivity.this.mScreenshot.getCategoryVersion());
                    }
                }
            }
        });
    }
    
    private void onInfoClick() {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this, 2131820754);
        alertDialog$Builder.setTitle(2131755388);
        alertDialog$Builder.setAdapter((ListAdapter)new InfoItemAdapter((Context)this, this.mInfoItems), (DialogInterface$OnClickListener)null);
        alertDialog$Builder.setPositiveButton(2131755061, (DialogInterface$OnClickListener)null);
        final AlertDialog create = alertDialog$Builder.create();
        if (create.getListView() != null) {
            create.getListView().setSelector(17170445);
        }
        create.show();
    }
    
    private void onShareClick() {
        if (this.mImgScreenshot.isImageLoaded()) {
            ThreadUtils.postToBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    final Cursor query = ScreenshotViewerActivity.this.getContentResolver().query(MediaStore$Images$Media.EXTERNAL_CONTENT_URI, new String[] { "_id" }, "_data=?", new String[] { ScreenshotViewerActivity.this.mScreenshot.getImageUri() }, (String)null);
                    if (query == null || !query.moveToFirst()) {
                        return;
                    }
                    final int int1 = query.getInt(query.getColumnIndex("_id"));
                    query.close();
                    final Uri withAppendedPath = Uri.withAppendedPath(MediaStore$Images$Media.EXTERNAL_CONTENT_URI, String.valueOf(int1));
                    final Intent intent = new Intent("android.intent.action.SEND");
                    intent.putExtra("android.intent.extra.STREAM", (Parcelable)withAppendedPath);
                    intent.setType("image/*");
                    intent.addFlags(1);
                    try {
                        ScreenshotViewerActivity.this.startActivity(Intent.createChooser(intent, (CharSequence)null));
                        TelemetryWrapper.shareCaptureImage(false, ScreenshotViewerActivity.this.mScreenshot.getCategory(), ScreenshotViewerActivity.this.mScreenshot.getCategoryVersion());
                    }
                    catch (ActivityNotFoundException ex) {}
                }
            });
        }
        else {
            this.setupView(true);
            this.initScreenshotInfo(true);
        }
    }
    
    private void proceedDelete() {
        if (this.mScreenshot != null) {
            ThreadUtils.postToBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    final File file = new File(ScreenshotViewerActivity.this.mScreenshot.getImageUri());
                    if (!file.exists()) {
                        return;
                    }
                    try {
                        file.delete();
                    }
                    catch (Exception ex) {}
                }
            });
            ScreenshotManager.getInstance().delete(this.mScreenshot.getId(), this);
        }
    }
    
    private void setupView(final boolean b) {
        final ImageView mImgPlaceholder = this.mImgPlaceholder;
        final int n = 0;
        int visibility;
        if (b) {
            visibility = 8;
        }
        else {
            visibility = 0;
        }
        mImgPlaceholder.setVisibility(visibility);
        final SubsamplingScaleImageView mImgScreenshot = this.mImgScreenshot;
        int visibility2;
        if (b) {
            visibility2 = n;
        }
        else {
            visibility2 = 8;
        }
        mImgScreenshot.setVisibility(visibility2);
        this.findViewById(2131296614).setEnabled(b);
        this.findViewById(2131296612).setEnabled(b);
        this.findViewById(2131296615).setEnabled(b);
        this.findViewById(2131296613).setEnabled(b);
    }
    
    private void showProgressBar(final long n) {
        new Handler().postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                if (!ScreenshotViewerActivity.this.mIsImageReady && ScreenshotViewerActivity.this.mProgressBar != null) {
                    ScreenshotViewerActivity.this.mProgressBar.setVisibility(0);
                }
            }
        }, n);
    }
    
    @Override
    public void applyLocale() {
    }
    
    @Override
    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        super.onActivityResult(n, n2, intent);
        if (n2 == 0 && n == 102) {
            this.setupView(true);
            this.initScreenshotInfo(false);
        }
        this.permissionHandler.onActivityResult(this, n, n2, intent);
    }
    
    public void onClick(final View view) {
        switch (view.getId()) {
            case 2131296616: {
                final Toolbar mBottomToolBar = this.mBottomToolBar;
                int visibility;
                if (this.mBottomToolBar.getVisibility() == 0) {
                    visibility = 8;
                }
                else {
                    visibility = 0;
                }
                mBottomToolBar.setVisibility(visibility);
                break;
            }
            case 2131296615: {
                this.permissionHandler.tryAction(this, "android.permission.READ_EXTERNAL_STORAGE", 2, null);
                break;
            }
            case 2131296614: {
                TelemetryWrapper.openCaptureLink(this.mScreenshot.getCategory(), this.mScreenshot.getCategoryVersion());
                HomeFragmentViewState.reset();
                final Intent intent = new Intent();
                intent.putExtra("extra_url", this.mScreenshot.getUrl());
                this.setResult(101, intent);
                this.finish();
                break;
            }
            case 2131296613: {
                TelemetryWrapper.showCaptureInfo(this.mScreenshot.getCategory(), this.mScreenshot.getCategoryVersion());
                this.onInfoClick();
                break;
            }
            case 2131296612: {
                this.permissionHandler.tryAction(this, "android.permission.READ_EXTERNAL_STORAGE", 1, null);
                break;
            }
            case 2131296611: {
                this.permissionHandler.tryAction(this, "android.permission.READ_EXTERNAL_STORAGE", 3, null);
                break;
            }
        }
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.permissionHandler = new PermissionHandler(new PermissionHandle() {
            private void doAction(final int n) {
                switch (n) {
                    default: {
                        throw new IllegalArgumentException("Unknown Action");
                    }
                    case 3: {
                        ScreenshotViewerActivity.this.onDeleteClick();
                        break;
                    }
                    case 2: {
                        ScreenshotViewerActivity.this.onShareClick();
                        break;
                    }
                    case 1: {
                        ScreenshotViewerActivity.this.onEditClick();
                        break;
                    }
                    case 0: {
                        this.viewScreenshot();
                        break;
                    }
                }
            }
            
            private void viewScreenshot() {
                ScreenshotViewerActivity.this.setupView(true);
                ScreenshotViewerActivity.this.initScreenshotInfo(false);
            }
            
            @Override
            public void doActionDirect(final String s, final int n, final Parcelable parcelable) {
                this.doAction(n);
            }
            
            @Override
            public void doActionGranted(final String s, final int n, final Parcelable parcelable) {
                this.doAction(n);
            }
            
            @Override
            public void doActionNoPermission(final String s, final int n, final Parcelable parcelable) {
            }
            
            @Override
            public void doActionSetting(final String s, final int n, final Parcelable parcelable) {
                this.doAction(n);
            }
            
            @Override
            public Snackbar makeAskAgainSnackBar(final int n) {
                return PermissionHandler.makeAskAgainSnackBar(ScreenshotViewerActivity.this, ScreenshotViewerActivity.this.findViewById(2131296594), 2131755291);
            }
            
            @Override
            public void permissionDeniedToast(final int n) {
                Toast.makeText((Context)ScreenshotViewerActivity.this, 2131755292, 1).show();
            }
            
            @Override
            public void requestPermissions(final int n) {
                ActivityCompat.requestPermissions(ScreenshotViewerActivity.this, new String[] { "android.permission.READ_EXTERNAL_STORAGE" }, n);
            }
        });
        this.setContentView(2131492896);
        this.mImgPlaceholder = this.findViewById(2131296617);
        (this.mImgScreenshot = this.findViewById(2131296616)).setPanLimit(1);
        this.mImgScreenshot.setMinimumScaleType(3);
        this.mImgScreenshot.setMinScale(1.0f);
        this.mImgScreenshot.setOnClickListener((View$OnClickListener)this);
        this.mImgScreenshot.setOnImageEventListener(this.onImageEventListener);
        this.mProgressBar = this.findViewById(2131296609);
        this.mBottomToolBar = this.findViewById(2131296610);
        this.findViewById(2131296614).setOnClickListener((View$OnClickListener)this);
        this.findViewById(2131296612).setOnClickListener((View$OnClickListener)this);
        this.findViewById(2131296615).setOnClickListener((View$OnClickListener)this);
        this.findViewById(2131296613).setOnClickListener((View$OnClickListener)this);
        this.findViewById(2131296611).setOnClickListener((View$OnClickListener)this);
        this.mScreenshot = (Screenshot)this.getIntent().getSerializableExtra("extra_screenshot");
        this.initInfoItemArray();
        if (this.mScreenshot != null) {
            this.permissionHandler.tryAction(this, "android.permission.READ_EXTERNAL_STORAGE", 0, null);
        }
        else {
            this.finish();
        }
    }
    
    public void onDeleteComplete(final int n, final long n2) {
        if (n > 0) {
            final Intent intent = new Intent();
            intent.putExtra("extra_screenshot_item_id", n2);
            this.setResult(100, intent);
            this.finish();
        }
    }
    
    public void onRequestPermissionsResult(final int n, final String[] array, final int[] array2) {
        super.onRequestPermissionsResult(n, array, array2);
        this.permissionHandler.onRequestPermissionsResult((Context)this, n, array, array2);
    }
    
    public void onRestoreInstanceState(final Bundle bundle) {
        this.permissionHandler.onRestoreInstanceState(bundle);
    }
    
    public void onSaveInstanceState(final Bundle bundle) {
        this.permissionHandler.onSaveInstanceState(bundle);
        super.onSaveInstanceState(bundle);
    }
    
    private static class InfoItemAdapter extends ArrayAdapter<ImageInfo>
    {
        public InfoItemAdapter(final Context context, final ArrayList<ImageInfo> list) {
            super(context, 0, (List)list);
        }
        
        public View getView(final int n, final View view, final ViewGroup viewGroup) {
            final ImageInfo imageInfo = (ImageInfo)this.getItem(n);
            View inflate = view;
            if (view == null) {
                inflate = LayoutInflater.from(this.getContext()).inflate(2131492982, viewGroup, false);
            }
            ((TextView)inflate.findViewById(2131296607)).setText((CharSequence)imageInfo.title);
            if (n == 4) {
                inflate.setOnLongClickListener((View$OnLongClickListener)new View$OnLongClickListener() {
                    public boolean onLongClick(final View view) {
                        return false;
                    }
                });
            }
            return inflate;
        }
    }
    
    private static class ScreenshotInfoTask extends AsyncTask<Void, Void, Void>
    {
        private final WeakReference<ScreenshotViewerActivity> activityRef;
        private final SimpleDateFormat dateFormat;
        private String fileSizeText;
        private int height;
        private ImageSource imageSource;
        private final ArrayList<ImageInfo> infoItems;
        private final Screenshot screenshot;
        private int width;
        private final boolean withShare;
        
        public ScreenshotInfoTask(final ScreenshotViewerActivity referent, final Screenshot screenshot, final ArrayList<ImageInfo> infoItems, final boolean withShare) {
            this.dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            this.activityRef = new WeakReference<ScreenshotViewerActivity>(referent);
            this.screenshot = screenshot;
            this.infoItems = infoItems;
            this.withShare = withShare;
        }
        
        protected Void doInBackground(final Void... array) {
            final ScreenshotViewerActivity screenshotViewerActivity = this.activityRef.get();
            if (screenshotViewerActivity != null && !screenshotViewerActivity.isFinishing() && !screenshotViewerActivity.isDestroyed()) {
                final File file = new File(this.screenshot.getImageUri());
                if (file.exists()) {
                    final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
                    bitmapFactory$Options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(this.screenshot.getImageUri(), bitmapFactory$Options);
                    this.width = bitmapFactory$Options.outWidth;
                    this.height = bitmapFactory$Options.outHeight;
                    this.imageSource = ImageSource.uri(Uri.fromFile(new File(this.screenshot.getImageUri())));
                    this.fileSizeText = ScreenshotViewerActivity.getFileSizeText(file.length());
                }
                return null;
            }
            return null;
        }
        
        protected void onPostExecute(final Void void1) {
            final ScreenshotViewerActivity screenshotViewerActivity = this.activityRef.get();
            if (screenshotViewerActivity != null && !screenshotViewerActivity.isFinishing() && !screenshotViewerActivity.isDestroyed()) {
                if (this.screenshot.getTimestamp() > 0L) {
                    final Calendar instance = Calendar.getInstance();
                    instance.setTimeInMillis(this.screenshot.getTimestamp());
                    this.infoItems.get(0).title = screenshotViewerActivity.getString(2131755385, new Object[] { this.dateFormat.format(instance.getTime()) });
                }
                this.infoItems.get(1).title = screenshotViewerActivity.getString(2131755383, new Object[] { String.format(Locale.getDefault(), "%dx%d", this.width, this.height) });
                this.infoItems.get(2).title = screenshotViewerActivity.getString(2131755384, new Object[] { this.fileSizeText });
                this.infoItems.get(3).title = screenshotViewerActivity.getString(2131755386, new Object[] { this.screenshot.getTitle() });
                this.infoItems.get(4).title = screenshotViewerActivity.getString(2131755382, new Object[] { this.screenshot.getCategory() });
                this.infoItems.get(5).title = screenshotViewerActivity.getString(2131755387, new Object[] { this.screenshot.getUrl() });
                if (this.imageSource != null) {
                    screenshotViewerActivity.mImgScreenshot.setImage(this.imageSource, ImageViewState.ALIGN_TOP);
                    if (this.withShare) {
                        screenshotViewerActivity.onShareClick();
                    }
                }
                else {
                    screenshotViewerActivity.hideProgressBar();
                    screenshotViewerActivity.setupView(false);
                    Toast.makeText((Context)screenshotViewerActivity, 2131755254, 1).show();
                }
            }
        }
        
        protected void onPreExecute() {
            final ScreenshotViewerActivity screenshotViewerActivity = this.activityRef.get();
            if (screenshotViewerActivity != null) {
                screenshotViewerActivity.showProgressBar(700L);
            }
        }
    }
}
