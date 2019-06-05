package org.mozilla.focus.screenshot;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
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
import android.support.p001v4.app.ActivityCompat;
import android.support.p004v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.adjust.sdk.Constants;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.OnImageEventListener;
import java.io.File;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.activity.BaseActivity;
import org.mozilla.focus.provider.QueryHandler.AsyncDeleteListener;
import org.mozilla.focus.screenshot.model.ImageInfo;
import org.mozilla.focus.screenshot.model.Screenshot;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.permissionhandler.PermissionHandle;
import org.mozilla.permissionhandler.PermissionHandler;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.content.HomeFragmentViewState;
import org.mozilla.threadutils.ThreadUtils;

public class ScreenshotViewerActivity extends BaseActivity implements OnClickListener, AsyncDeleteListener {
    private Toolbar mBottomToolBar;
    private ImageView mImgPlaceholder;
    private SubsamplingScaleImageView mImgScreenshot;
    private ArrayList<ImageInfo> mInfoItems = new ArrayList();
    private boolean mIsImageReady = false;
    private ProgressBar mProgressBar;
    private Screenshot mScreenshot;
    private OnImageEventListener onImageEventListener = new C07387();
    private PermissionHandler permissionHandler;

    /* renamed from: org.mozilla.focus.screenshot.ScreenshotViewerActivity$2 */
    class C05072 implements Runnable {
        C05072() {
        }

        public void run() {
            Cursor query = ScreenshotViewerActivity.this.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{"_id"}, "_data=?", new String[]{ScreenshotViewerActivity.this.mScreenshot.getImageUri()}, null);
            if (query != null && query.moveToFirst()) {
                int i = query.getInt(query.getColumnIndex("_id"));
                query.close();
                Uri withAppendedPath = Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI, String.valueOf(i));
                Intent intent = new Intent("android.intent.action.EDIT");
                intent.setDataAndType(withAppendedPath, "image/*");
                intent.addFlags(1);
                try {
                    ScreenshotViewerActivity.this.startActivityForResult(Intent.createChooser(intent, null), 102);
                    TelemetryWrapper.editCaptureImage(true, ScreenshotViewerActivity.this.mScreenshot.getCategory(), ScreenshotViewerActivity.this.mScreenshot.getCategoryVersion());
                } catch (ActivityNotFoundException unused) {
                    TelemetryWrapper.editCaptureImage(false, ScreenshotViewerActivity.this.mScreenshot.getCategory(), ScreenshotViewerActivity.this.mScreenshot.getCategoryVersion());
                }
            }
        }
    }

    /* renamed from: org.mozilla.focus.screenshot.ScreenshotViewerActivity$3 */
    class C05083 implements Runnable {
        C05083() {
        }

        public void run() {
            Cursor query = ScreenshotViewerActivity.this.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{"_id"}, "_data=?", new String[]{ScreenshotViewerActivity.this.mScreenshot.getImageUri()}, null);
            if (query != null && query.moveToFirst()) {
                int i = query.getInt(query.getColumnIndex("_id"));
                query.close();
                Uri withAppendedPath = Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI, String.valueOf(i));
                Intent intent = new Intent("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.STREAM", withAppendedPath);
                intent.setType("image/*");
                intent.addFlags(1);
                try {
                    ScreenshotViewerActivity.this.startActivity(Intent.createChooser(intent, null));
                    TelemetryWrapper.shareCaptureImage(false, ScreenshotViewerActivity.this.mScreenshot.getCategory(), ScreenshotViewerActivity.this.mScreenshot.getCategoryVersion());
                } catch (ActivityNotFoundException unused) {
                }
            }
        }
    }

    /* renamed from: org.mozilla.focus.screenshot.ScreenshotViewerActivity$4 */
    class C05094 implements DialogInterface.OnClickListener {
        C05094() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            TelemetryWrapper.deleteCaptureImage(ScreenshotViewerActivity.this.mScreenshot.getCategory(), ScreenshotViewerActivity.this.mScreenshot.getCategoryVersion());
            ScreenshotViewerActivity.this.proceedDelete();
        }
    }

    /* renamed from: org.mozilla.focus.screenshot.ScreenshotViewerActivity$5 */
    class C05105 implements Runnable {
        C05105() {
        }

        public void run() {
            File file = new File(ScreenshotViewerActivity.this.mScreenshot.getImageUri());
            if (file.exists()) {
                try {
                    file.delete();
                } catch (Exception unused) {
                }
            }
        }
    }

    /* renamed from: org.mozilla.focus.screenshot.ScreenshotViewerActivity$6 */
    class C05116 implements Runnable {
        C05116() {
        }

        public void run() {
            if (!ScreenshotViewerActivity.this.mIsImageReady && ScreenshotViewerActivity.this.mProgressBar != null) {
                ScreenshotViewerActivity.this.mProgressBar.setVisibility(0);
            }
        }
    }

    private static class InfoItemAdapter extends ArrayAdapter<ImageInfo> {

        /* renamed from: org.mozilla.focus.screenshot.ScreenshotViewerActivity$InfoItemAdapter$1 */
        class C05121 implements OnLongClickListener {
            public boolean onLongClick(View view) {
                return false;
            }

            C05121() {
            }
        }

        public InfoItemAdapter(Context context, ArrayList<ImageInfo> arrayList) {
            super(context, 0, arrayList);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            ImageInfo imageInfo = (ImageInfo) getItem(i);
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(C0769R.layout.item_screenshot_info_dialog, viewGroup, false);
            }
            ((TextView) view.findViewById(C0427R.C0426id.screenshot_info_dialog_text)).setText(imageInfo.title);
            if (i == 4) {
                view.setOnLongClickListener(new C05121());
            }
            return view;
        }
    }

    private static class ScreenshotInfoTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<ScreenshotViewerActivity> activityRef;
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        private String fileSizeText;
        private int height;
        private ImageSource imageSource;
        private final ArrayList<ImageInfo> infoItems;
        private final Screenshot screenshot;
        private int width;
        private final boolean withShare;

        public ScreenshotInfoTask(ScreenshotViewerActivity screenshotViewerActivity, Screenshot screenshot, ArrayList<ImageInfo> arrayList, boolean z) {
            this.activityRef = new WeakReference(screenshotViewerActivity);
            this.screenshot = screenshot;
            this.infoItems = arrayList;
            this.withShare = z;
        }

        /* Access modifiers changed, original: protected */
        public void onPreExecute() {
            ScreenshotViewerActivity screenshotViewerActivity = (ScreenshotViewerActivity) this.activityRef.get();
            if (screenshotViewerActivity != null) {
                screenshotViewerActivity.showProgressBar(700);
            }
        }

        /* Access modifiers changed, original: protected|varargs */
        public Void doInBackground(Void... voidArr) {
            ScreenshotViewerActivity screenshotViewerActivity = (ScreenshotViewerActivity) this.activityRef.get();
            if (screenshotViewerActivity == null || screenshotViewerActivity.isFinishing() || screenshotViewerActivity.isDestroyed()) {
                return null;
            }
            File file = new File(this.screenshot.getImageUri());
            if (file.exists()) {
                Options options = new Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(this.screenshot.getImageUri(), options);
                this.width = options.outWidth;
                this.height = options.outHeight;
                this.imageSource = ImageSource.uri(Uri.fromFile(new File(this.screenshot.getImageUri())));
                this.fileSizeText = ScreenshotViewerActivity.getFileSizeText(file.length());
            }
            return null;
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(Void voidR) {
            ScreenshotViewerActivity screenshotViewerActivity = (ScreenshotViewerActivity) this.activityRef.get();
            if (screenshotViewerActivity != null && !screenshotViewerActivity.isFinishing() && !screenshotViewerActivity.isDestroyed()) {
                if (this.screenshot.getTimestamp() > 0) {
                    Calendar.getInstance().setTimeInMillis(this.screenshot.getTimestamp());
                    ((ImageInfo) this.infoItems.get(0)).title = screenshotViewerActivity.getString(C0769R.string.screenshot_image_viewer_dialog_info_time1, new Object[]{this.dateFormat.format(r0.getTime())});
                }
                ImageInfo imageInfo = (ImageInfo) this.infoItems.get(1);
                Object[] objArr = new Object[1];
                objArr[0] = String.format(Locale.getDefault(), "%dx%d", new Object[]{Integer.valueOf(this.width), Integer.valueOf(this.height)});
                imageInfo.title = screenshotViewerActivity.getString(C0769R.string.screenshot_image_viewer_dialog_info_resolution1, objArr);
                ((ImageInfo) this.infoItems.get(2)).title = screenshotViewerActivity.getString(C0769R.string.screenshot_image_viewer_dialog_info_size1, new Object[]{this.fileSizeText});
                ((ImageInfo) this.infoItems.get(3)).title = screenshotViewerActivity.getString(C0769R.string.screenshot_image_viewer_dialog_info_title1, new Object[]{this.screenshot.getTitle()});
                ((ImageInfo) this.infoItems.get(4)).title = screenshotViewerActivity.getString(C0769R.string.screenshot_image_viewer_dialog_info_category, new Object[]{this.screenshot.getCategory()});
                ((ImageInfo) this.infoItems.get(5)).title = screenshotViewerActivity.getString(C0769R.string.screenshot_image_viewer_dialog_info_url1, new Object[]{this.screenshot.getUrl()});
                if (this.imageSource != null) {
                    screenshotViewerActivity.mImgScreenshot.setImage(this.imageSource, ImageViewState.ALIGN_TOP);
                    if (this.withShare) {
                        screenshotViewerActivity.onShareClick();
                    }
                } else {
                    screenshotViewerActivity.hideProgressBar();
                    screenshotViewerActivity.setupView(false);
                    Toast.makeText(screenshotViewerActivity, C0769R.string.message_cannot_find_screenshot, 1).show();
                }
            }
        }
    }

    /* renamed from: org.mozilla.focus.screenshot.ScreenshotViewerActivity$1 */
    class C07371 implements PermissionHandle {
        public void doActionNoPermission(String str, int i, Parcelable parcelable) {
        }

        C07371() {
        }

        private void viewScreenshot() {
            ScreenshotViewerActivity.this.setupView(true);
            ScreenshotViewerActivity.this.initScreenshotInfo(false);
        }

        private void doAction(int i) {
            switch (i) {
                case 0:
                    viewScreenshot();
                    return;
                case 1:
                    ScreenshotViewerActivity.this.onEditClick();
                    return;
                case 2:
                    ScreenshotViewerActivity.this.onShareClick();
                    return;
                case 3:
                    ScreenshotViewerActivity.this.onDeleteClick();
                    return;
                default:
                    throw new IllegalArgumentException("Unknown Action");
            }
        }

        public void doActionDirect(String str, int i, Parcelable parcelable) {
            doAction(i);
        }

        public void doActionGranted(String str, int i, Parcelable parcelable) {
            doAction(i);
        }

        public void doActionSetting(String str, int i, Parcelable parcelable) {
            doAction(i);
        }

        public Snackbar makeAskAgainSnackBar(int i) {
            return PermissionHandler.makeAskAgainSnackBar(ScreenshotViewerActivity.this, ScreenshotViewerActivity.this.findViewById(C0427R.C0426id.root), (int) C0769R.string.permission_toast_storage);
        }

        public void requestPermissions(int i) {
            ActivityCompat.requestPermissions(ScreenshotViewerActivity.this, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, i);
        }

        public void permissionDeniedToast(int i) {
            Toast.makeText(ScreenshotViewerActivity.this, C0769R.string.permission_toast_storage_deny, 1).show();
        }
    }

    /* renamed from: org.mozilla.focus.screenshot.ScreenshotViewerActivity$7 */
    class C07387 implements OnImageEventListener {
        public void onImageLoaded() {
        }

        public void onPreviewLoadError(Exception exception) {
        }

        public void onPreviewReleased() {
        }

        public void onTileLoadError(Exception exception) {
        }

        C07387() {
        }

        public void onReady() {
            ScreenshotViewerActivity.this.hideProgressBar();
        }

        public void onImageLoadError(Exception exception) {
            ScreenshotViewerActivity.this.hideProgressBar();
        }
    }

    public void applyLocale() {
    }

    public static final void goScreenshotViewerActivityOnResult(Activity activity, Screenshot screenshot) {
        Intent intent = new Intent(activity, ScreenshotViewerActivity.class);
        intent.putExtra("extra_screenshot", screenshot);
        activity.startActivityForResult(intent, Constants.ONE_SECOND);
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.permissionHandler = new PermissionHandler(new C07371());
        setContentView((int) C0769R.layout.activity_screenshot_viewer);
        this.mImgPlaceholder = (ImageView) findViewById(C0427R.C0426id.screenshot_viewer_image_placeholder);
        this.mImgScreenshot = (SubsamplingScaleImageView) findViewById(C0427R.C0426id.screenshot_viewer_image);
        this.mImgScreenshot.setPanLimit(1);
        this.mImgScreenshot.setMinimumScaleType(3);
        this.mImgScreenshot.setMinScale(1.0f);
        this.mImgScreenshot.setOnClickListener(this);
        this.mImgScreenshot.setOnImageEventListener(this.onImageEventListener);
        this.mProgressBar = (ProgressBar) findViewById(C0427R.C0426id.screenshot_progressbar);
        this.mBottomToolBar = (Toolbar) findViewById(C0427R.C0426id.screenshot_viewer_btm_toolbar);
        findViewById(C0427R.C0426id.screenshot_viewer_btn_open_url).setOnClickListener(this);
        findViewById(C0427R.C0426id.screenshot_viewer_btn_edit).setOnClickListener(this);
        findViewById(C0427R.C0426id.screenshot_viewer_btn_share).setOnClickListener(this);
        findViewById(C0427R.C0426id.screenshot_viewer_btn_info).setOnClickListener(this);
        findViewById(C0427R.C0426id.screenshot_viewer_btn_delete).setOnClickListener(this);
        this.mScreenshot = (Screenshot) getIntent().getSerializableExtra("extra_screenshot");
        initInfoItemArray();
        if (this.mScreenshot != null) {
            this.permissionHandler.tryAction((Activity) this, "android.permission.READ_EXTERNAL_STORAGE", 0, null);
        } else {
            finish();
        }
    }

    public void onRestoreInstanceState(Bundle bundle) {
        this.permissionHandler.onRestoreInstanceState(bundle);
    }

    public void onSaveInstanceState(Bundle bundle) {
        this.permissionHandler.onSaveInstanceState(bundle);
        super.onSaveInstanceState(bundle);
    }

    /* Access modifiers changed, original: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == 0 && i == 102) {
            setupView(true);
            initScreenshotInfo(false);
        }
        this.permissionHandler.onActivityResult(this, i, i2, intent);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0427R.C0426id.screenshot_viewer_btn_delete /*2131296611*/:
                this.permissionHandler.tryAction((Activity) this, "android.permission.READ_EXTERNAL_STORAGE", 3, null);
                return;
            case C0427R.C0426id.screenshot_viewer_btn_edit /*2131296612*/:
                this.permissionHandler.tryAction((Activity) this, "android.permission.READ_EXTERNAL_STORAGE", 1, null);
                return;
            case C0427R.C0426id.screenshot_viewer_btn_info /*2131296613*/:
                TelemetryWrapper.showCaptureInfo(this.mScreenshot.getCategory(), this.mScreenshot.getCategoryVersion());
                onInfoClick();
                return;
            case C0427R.C0426id.screenshot_viewer_btn_open_url /*2131296614*/:
                TelemetryWrapper.openCaptureLink(this.mScreenshot.getCategory(), this.mScreenshot.getCategoryVersion());
                HomeFragmentViewState.reset();
                Intent intent = new Intent();
                intent.putExtra("extra_url", this.mScreenshot.getUrl());
                setResult(101, intent);
                finish();
                return;
            case C0427R.C0426id.screenshot_viewer_btn_share /*2131296615*/:
                this.permissionHandler.tryAction((Activity) this, "android.permission.READ_EXTERNAL_STORAGE", 2, null);
                return;
            case C0427R.C0426id.screenshot_viewer_image /*2131296616*/:
                this.mBottomToolBar.setVisibility(this.mBottomToolBar.getVisibility() == 0 ? 8 : 0);
                return;
            default:
                return;
        }
    }

    public void onDeleteComplete(int i, long j) {
        if (i > 0) {
            Intent intent = new Intent();
            intent.putExtra("extra_screenshot_item_id", j);
            setResult(100, intent);
            finish();
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        this.permissionHandler.onRequestPermissionsResult(this, i, strArr, iArr);
    }

    private void setupView(boolean z) {
        int i = 0;
        this.mImgPlaceholder.setVisibility(z ? 8 : 0);
        SubsamplingScaleImageView subsamplingScaleImageView = this.mImgScreenshot;
        if (!z) {
            i = 8;
        }
        subsamplingScaleImageView.setVisibility(i);
        findViewById(C0427R.C0426id.screenshot_viewer_btn_open_url).setEnabled(z);
        findViewById(C0427R.C0426id.screenshot_viewer_btn_edit).setEnabled(z);
        findViewById(C0427R.C0426id.screenshot_viewer_btn_share).setEnabled(z);
        findViewById(C0427R.C0426id.screenshot_viewer_btn_info).setEnabled(z);
    }

    private void initInfoItemArray() {
        this.mInfoItems.clear();
        this.mInfoItems.add(new ImageInfo(getString(C0769R.string.screenshot_image_viewer_dialog_info_time1, new Object[]{""})));
        this.mInfoItems.add(new ImageInfo(getString(C0769R.string.screenshot_image_viewer_dialog_info_resolution1, new Object[]{""})));
        this.mInfoItems.add(new ImageInfo(getString(C0769R.string.screenshot_image_viewer_dialog_info_size1, new Object[]{""})));
        this.mInfoItems.add(new ImageInfo(getString(C0769R.string.screenshot_image_viewer_dialog_info_title1, new Object[]{""})));
        this.mInfoItems.add(new ImageInfo(getString(C0769R.string.screenshot_image_viewer_dialog_info_category, new Object[]{""})));
        this.mInfoItems.add(new ImageInfo(getString(C0769R.string.screenshot_image_viewer_dialog_info_url1, new Object[]{""})));
    }

    private void initScreenshotInfo(boolean z) {
        if (this.mScreenshot != null) {
            new ScreenshotInfoTask(this, this.mScreenshot, this.mInfoItems, z).execute(new Void[0]);
            ThreadUtils.postToBackgroundThread(new C0505-$$Lambda$ScreenshotViewerActivity$EX4pVRu55Asmujhv6gJW3gkQsN8(this));
        }
    }

    public static /* synthetic */ void lambda$initScreenshotInfo$0(ScreenshotViewerActivity screenshotViewerActivity) {
        screenshotViewerActivity.mScreenshot.setCategory(ScreenshotManager.getInstance().getCategory(screenshotViewerActivity, screenshotViewerActivity.mScreenshot.getUrl()));
        screenshotViewerActivity.mScreenshot.setCategoryVersion(ScreenshotManager.getInstance().getCategoryVersion());
    }

    private void onEditClick() {
        ThreadUtils.postToBackgroundThread(new C05072());
    }

    private void onShareClick() {
        if (this.mImgScreenshot.isImageLoaded()) {
            ThreadUtils.postToBackgroundThread(new C05083());
            return;
        }
        setupView(true);
        initScreenshotInfo(true);
    }

    private void onInfoClick() {
        Builder builder = new Builder(this, C0769R.style.CustomDialogStyle);
        builder.setTitle(C0769R.string.screenshot_image_viewer_dialog_title);
        builder.setAdapter(new InfoItemAdapter(this, this.mInfoItems), null);
        builder.setPositiveButton(C0769R.string.action_ok, null);
        AlertDialog create = builder.create();
        if (create.getListView() != null) {
            create.getListView().setSelector(17170445);
        }
        create.show();
    }

    private void onDeleteClick() {
        Builder builder = new Builder(this, C0769R.style.AlertDialogStyle);
        builder.setMessage(C0769R.string.screenshot_image_viewer_dialog_delete_msg);
        builder.setPositiveButton(C0769R.string.browsing_history_menu_delete, new C05094());
        builder.setNegativeButton(C0769R.string.action_cancel, null);
        builder.create().show();
    }

    public static String getFileSizeText(long j) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        float f = (float) j;
        StringBuilder stringBuilder;
        if (f < 1048576.0f) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(decimalFormat.format((double) (f / 1024.0f)));
            stringBuilder.append(" KB");
            return stringBuilder.toString();
        } else if (f < 1.07374182E9f) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(decimalFormat.format((double) (f / 1048576.0f)));
            stringBuilder2.append(" MB");
            return stringBuilder2.toString();
        } else if (f >= 1.09951163E12f) {
            return "";
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(decimalFormat.format((double) (f / 1.07374182E9f)));
            stringBuilder.append(" GB");
            return stringBuilder.toString();
        }
    }

    private void proceedDelete() {
        if (this.mScreenshot != null) {
            ThreadUtils.postToBackgroundThread(new C05105());
            ScreenshotManager.getInstance().delete(this.mScreenshot.getId(), this);
        }
    }

    private void showProgressBar(long j) {
        new Handler().postDelayed(new C05116(), j);
    }

    private void hideProgressBar() {
        this.mIsImageReady = true;
        if (this.mProgressBar != null && this.mProgressBar.isShown()) {
            this.mProgressBar.setVisibility(8);
        }
    }
}
