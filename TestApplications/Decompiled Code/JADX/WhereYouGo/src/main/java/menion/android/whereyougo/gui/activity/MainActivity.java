package menion.android.whereyougo.gui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.app.Fragment;
import android.support.p000v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Vector;
import locus.api.objects.extra.Location;
import locus.api.objects.extra.Waypoint;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.MainApplication;
import menion.android.whereyougo.VersionInfo;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.gui.dialog.AboutDialog;
import menion.android.whereyougo.gui.dialog.ChooseCartridgeDialog;
import menion.android.whereyougo.gui.dialog.ChooseSavegameDialog;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;
import menion.android.whereyougo.gui.extension.activity.CustomMainActivity;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.maps.utils.MapDataProvider;
import menion.android.whereyougo.maps.utils.MapHelper;
import menion.android.whereyougo.network.activity.DownloadCartridgeActivity;
import menion.android.whereyougo.openwig.WLocationService;
import menion.android.whereyougo.openwig.WSaveFile;
import menion.android.whereyougo.openwig.WSeekableFile;
import menion.android.whereyougo.openwig.WUI;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;
import p005cz.matejcik.openwig.Engine;
import p005cz.matejcik.openwig.formats.CartridgeFile;

public class MainActivity extends CustomMainActivity {
    private static final String TAG = "Main";
    public static CartridgeFile cartridgeFile;
    private static Vector<CartridgeFile> cartridgeFiles;
    public static String selectedFile;
    private static final WLocationService wLocationService = new WLocationService();
    public static final WUI wui = new WUI();

    /* renamed from: menion.android.whereyougo.gui.activity.MainActivity$1 */
    static class C02641 implements Runnable {
        C02641() {
        }

        public void run() {
            try {
                FileSystem.backupFile(MainActivity.getSaveFile());
            } catch (Exception e) {
            }
        }
    }

    /* renamed from: menion.android.whereyougo.gui.activity.MainActivity$2 */
    class C02652 implements OnClickListener {
        C02652() {
        }

        public void onClick(View v) {
            switch (v.getId()) {
                case C0254R.C0253id.button_logo /*2131492961*/:
                    MainActivity.this.getSupportFragmentManager().beginTransaction().add(new AboutDialog(), "DIALOG_TAG_MAIN").commitAllowingStateLoss();
                    return;
                case C0254R.C0253id.button_start /*2131492962*/:
                    MainActivity.this.clickStart();
                    return;
                case C0254R.C0253id.button_map /*2131492963*/:
                    MainActivity.this.clickMap();
                    return;
                case C0254R.C0253id.button_gps /*2131492964*/:
                    MainActivity.this.startActivity(new Intent(MainActivity.this, SatelliteActivity.class));
                    return;
                case C0254R.C0253id.button_settings /*2131492965*/:
                    MainActivity.this.startActivity(new Intent(MainActivity.this, XmlSettingsActivity.class));
                    return;
                default:
                    return;
            }
        }
    }

    static {
        wui.setOnSavingStarted(new C02641());
    }

    public static boolean callGudingScreen(Activity activity) {
        activity.startActivity(new Intent(activity, GuidingActivity.class));
        return true;
    }

    public static File getSaveFile() throws IOException {
        try {
            return new File(selectedFile.substring(0, selectedFile.length() - 3) + "ows");
        } catch (SecurityException e) {
            Logger.m22e(TAG, "getSyncFile()", e);
            return null;
        }
    }

    public static File getLogFile() throws IOException {
        try {
            return new File(selectedFile.substring(0, selectedFile.length() - 3) + "owl");
        } catch (SecurityException e) {
            Logger.m22e(TAG, "getSyncFile()", e);
            return null;
        }
    }

    public static String getSelectedFile() {
        return selectedFile;
    }

    public static void setSelectedFile(String filepath) {
        selectedFile = filepath;
    }

    private static void loadCartridge(OutputStream log) {
        try {
            WUI.startProgressDialog();
            Engine.newInstance(cartridgeFile, log, wui, wLocationService).start();
        } catch (Throwable th) {
        }
    }

    private static void restoreCartridge(OutputStream log) {
        try {
            WUI.startProgressDialog();
            Engine.newInstance(cartridgeFile, log, wui, wLocationService).restore();
        } catch (Throwable th) {
        }
    }

    public static void startSelectedCartridge(boolean restore) {
        try {
            File file = getLogFile();
            FileOutputStream fos = null;
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                fos = new FileOutputStream(file, true);
            } catch (Exception e) {
                Logger.m22e(TAG, "onResume() - create empty saveGame file", e);
            }
            if (restore) {
                restoreCartridge(fos);
            } else {
                loadCartridge(fos);
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static void refreshCartridges() {
        boolean z;
        String str = TAG;
        StringBuilder append = new StringBuilder().append("refreshCartridges(), ");
        if (selectedFile == null) {
            z = true;
        } else {
            z = false;
        }
        Logger.m26w(str, append.append(z).toString());
        File[] files = FileSystem.getFiles(FileSystem.ROOT, "gwc");
        cartridgeFiles = new Vector();
        ArrayList<Waypoint> wpts = new ArrayList();
        if (files != null) {
            for (File file : files) {
                try {
                    CartridgeFile cart = CartridgeFile.read(new WSeekableFile(file), new WSaveFile(file));
                    if (cart != null) {
                        cart.filename = file.getAbsolutePath();
                        Location loc = new Location(TAG);
                        loc.setLatitude(cart.latitude);
                        loc.setLongitude(cart.longitude);
                        Waypoint waypoint = new Waypoint(cart.name, loc);
                        cartridgeFiles.add(cart);
                        wpts.add(waypoint);
                    }
                } catch (Exception e) {
                    Logger.m26w(TAG, "refreshCartridge(), file:" + file + ", e:" + e.toString());
                    ManagerNotify.toastShortMessage(Locale.getString(C0254R.string.invalid_cartridge, actualFile.getName()));
                }
            }
        }
        if (wpts.size() > 0) {
        }
    }

    public static void openCartridge(CartridgeFile cartridgeFile) {
        CustomActivity activity = C0322A.getMain();
        if (activity != null) {
            try {
                cartridgeFile = cartridgeFile;
                selectedFile = cartridgeFile.filename;
                activity.getSupportFragmentManager().beginTransaction().add(ChooseSavegameDialog.newInstance(getSaveFile()), "DIALOG_TAG_CHOOSE_SAVE_FILE").commitAllowingStateLoss();
            } catch (Exception e) {
                Logger.m22e(TAG, "onCreate()", e);
            }
        }
    }

    private void clickMap() {
        MapDataProvider mdp = MapHelper.getMapDataProvider();
        mdp.clear();
        mdp.addCartridges(cartridgeFiles);
        wui.showScreen(14, null);
    }

    private void clickStart() {
        if (isAnyCartridgeAvailable()) {
            Fragment dialog = new ChooseCartridgeDialog();
            dialog.setParams(cartridgeFiles);
            getSupportFragmentManager().beginTransaction().add(dialog, "DIALOG_TAG_CHOOSE_CARTRIDGE").commitAllowingStateLoss();
        }
    }

    /* Access modifiers changed, original: protected */
    public void eventCreateLayout() {
        setContentView(C0254R.layout.layout_main);
        ((TextView) findViewById(C0254R.C0253id.title_text)).setText(MainApplication.APP_NAME);
        UtilsGUI.setButtons(this, new int[]{C0254R.C0253id.button_start, C0254R.C0253id.button_map, C0254R.C0253id.button_gps, C0254R.C0253id.button_settings, C0254R.C0253id.button_logo}, new C02652(), null);
    }

    /* Access modifiers changed, original: protected */
    public void eventDestroyApp() {
        ((NotificationManager) getSystemService("notification")).cancelAll();
    }

    /* Access modifiers changed, original: protected */
    public void eventFirstInit() {
        VersionInfo.afterStartAction();
    }

    /* Access modifiers changed, original: protected */
    public void eventRegisterOnly() {
    }

    /* Access modifiers changed, original: protected */
    public void eventSecondInit() {
    }

    /* Access modifiers changed, original: protected */
    public String getCloseAdditionalText() {
        return null;
    }

    /* Access modifiers changed, original: protected */
    public int getCloseValue() {
        return 0;
    }

    private boolean isAnyCartridgeAvailable() {
        if (cartridgeFiles != null && cartridgeFiles.size() != 0) {
            return true;
        }
        UtilsGUI.showDialogInfo((Activity) this, getString(C0254R.string.no_wherigo_cartridge_available, new Object[]{FileSystem.ROOT, MainApplication.APP_NAME}));
        return false;
    }

    @TargetApi(23)
    private void checkPermissions() {
        String[] permissions = new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != 0) {
                ActivityCompat.requestPermissions(this, permissions, 0);
                return;
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        testFileSystem();
        if (Preferences.GPS || Preferences.GPS_START_AUTOMATICALLY) {
            LocationState.setGpsOn(this);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (VERSION.SDK_INT >= 23) {
            checkPermissions();
        }
        Intent intent;
        if ("android.intent.action.VIEW".equals(getIntent().getAction())) {
            intent = new Intent(getIntent());
            intent.setClass(this, DownloadCartridgeActivity.class);
            startActivity(intent);
            finish();
        } else if ("android.intent.action.SEND".equals(getIntent().getAction())) {
            try {
                Uri uri = Uri.parse(getIntent().getStringExtra("android.intent.extra.TEXT"));
                if (uri.getQueryParameter("CGUID") == null) {
                    throw new Exception("Invalid URL");
                }
                intent = new Intent(this, DownloadCartridgeActivity.class);
                intent.setData(uri);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                ManagerNotify.toastShortMessage(this, getString(C0254R.string.invalid_url));
            }
        } else {
            String cguid = getIntent() == null ? null : getIntent().getStringExtra("cguid");
            if (cguid != null) {
                File file = FileSystem.findFile(cguid);
                if (file != null) {
                    openCartridge(file);
                }
            }
        }
    }

    public void onResume() {
        super.onResume();
        refreshCartridges();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0254R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case C0254R.C0253id.menu_geocaching /*2131492988*/:
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://geocaching.com/")));
                return true;
            case C0254R.C0253id.menu_wherigo /*2131492989*/:
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://wherigo.com/")));
                return true;
            case C0254R.C0253id.menu_github /*2131492990*/:
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/biylda/WhereYouGo")));
                break;
        }
        return false;
    }

    private void openCartridge(File file) {
        CartridgeFile cart = null;
        try {
            cart = CartridgeFile.read(new WSeekableFile(file), new WSaveFile(file));
            if (cart != null) {
                cart.filename = file.getAbsolutePath();
                try {
                    openCartridge(cart);
                } catch (Exception e) {
                    Logger.m22e(TAG, "onCreate()", e);
                }
            }
        } catch (Exception e2) {
            Logger.m26w(TAG, "openCartridge(), file:" + file + ", e:" + e2.toString());
            ManagerNotify.toastShortMessage(Locale.getString(C0254R.string.invalid_cartridge, file.getName()));
        }
    }
}
