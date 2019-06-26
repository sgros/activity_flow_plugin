package menion.android.whereyougo.gui.activity.wherigo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.gui.IRefreshable;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.gui.activity.SatelliteActivity;
import menion.android.whereyougo.gui.extension.DataInfo;
import menion.android.whereyougo.gui.extension.IconedListAdapter;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.maps.utils.MapDataProvider;
import menion.android.whereyougo.maps.utils.MapHelper;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;
import menion.android.whereyougo.utils.Utils;
import menion.android.whereyougo.utils.UtilsFormat;
import p005cz.matejcik.openwig.Engine;
import p005cz.matejcik.openwig.Player;
import p005cz.matejcik.openwig.Task;
import p005cz.matejcik.openwig.Thing;
import p005cz.matejcik.openwig.Zone;

public class MainMenuActivity extends CustomActivity implements IRefreshable {
    private static final int DOUBLE_PRESS_HK_BACK_PERIOD = 666;
    private static final String TAG = "CartridgeMainMenu";
    private long lastPressedTime = 0;
    private OnItemClickListener listClick;
    private MenuItem saveGameMainMenuItem;

    /* renamed from: menion.android.whereyougo.gui.activity.wherigo.MainMenuActivity$10 */
    class C027710 implements Runnable {
        C027710() {
        }

        public void run() {
            if (C0322A.getMain() != null && Engine.instance != null && Engine.instance.cartridge != null) {
                ArrayList<DataInfo> data = new ArrayList();
                data.add(new DataInfo(MainMenuActivity.this.getString(C0254R.string.locations) + " (" + Engine.instance.cartridge.visibleZones() + ")", MainMenuActivity.this.getVisibleZonesDescription(), (int) C0254R.C0252drawable.icon_locations));
                data.add(new DataInfo(MainMenuActivity.this.getString(C0254R.string.you_see) + " (" + Engine.instance.cartridge.visibleThings() + ")", MainMenuActivity.this.getVisibleCartridgeThingsDescription(), (int) C0254R.C0252drawable.icon_search));
                data.add(new DataInfo(MainMenuActivity.this.getString(C0254R.string.inventory) + " (" + Engine.instance.player.visibleThings() + ")", MainMenuActivity.this.getVisiblePlayerThingsDescription(), (int) C0254R.C0252drawable.icon_inventory));
                data.add(new DataInfo(MainMenuActivity.this.getString(C0254R.string.tasks) + " (" + Engine.instance.cartridge.visibleTasks() + ")", MainMenuActivity.this.getVisibleTasksDescription(), (int) C0254R.C0252drawable.icon_tasks));
                ListView lv = new ListView(MainMenuActivity.this);
                IconedListAdapter adapter = new IconedListAdapter(MainMenuActivity.this, data, lv);
                adapter.setMinHeight((int) Utils.getDpPixels(70.0f));
                adapter.setTextView02Visible(0, true);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(MainMenuActivity.this.listClick);
                CustomDialog.setContent(MainMenuActivity.this, lv, 0, true, false);
            }
        }
    }

    /* renamed from: menion.android.whereyougo.gui.activity.wherigo.MainMenuActivity$1 */
    class C02781 implements OnItemClickListener {
        C02781() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Logger.m20d(MainMenuActivity.TAG, "onItemClick:" + position);
            switch (position) {
                case 0:
                    if (Engine.instance.cartridge.visibleZones() >= 1) {
                        MainActivity.wui.showScreen(4, null);
                        return;
                    }
                    return;
                case 1:
                    if (Engine.instance.cartridge.visibleThings() >= 1) {
                        MainActivity.wui.showScreen(3, null);
                        return;
                    }
                    return;
                case 2:
                    if (Engine.instance.player.visibleThings() >= 1) {
                        MainActivity.wui.showScreen(2, null);
                        return;
                    }
                    return;
                case 3:
                    if (MainMenuActivity.this.getVisibleTasksCount() > 0) {
                        MainActivity.wui.showScreen(5, null);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: menion.android.whereyougo.gui.activity.wherigo.MainMenuActivity$8 */
    class C02828 implements OnClickListener {
        C02828() {
        }

        public void onClick(DialogInterface dialog, int which) {
            new SaveGameOnExit(MainMenuActivity.this, null).execute(new Void[0]);
            MainActivity.selectedFile = null;
            DetailsActivity.f101et = null;
        }
    }

    /* renamed from: menion.android.whereyougo.gui.activity.wherigo.MainMenuActivity$9 */
    class C02839 implements OnClickListener {
        C02839() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Engine.kill();
            MainActivity.selectedFile = null;
            DetailsActivity.f101et = null;
            MainMenuActivity.this.finish();
        }
    }

    /* renamed from: menion.android.whereyougo.gui.activity.wherigo.MainMenuActivity$2 */
    class C04182 implements CustomDialog.OnClickListener {
        C04182() {
        }

        public boolean onClick(CustomDialog dialog, View v, int btn) {
            MainMenuActivity.this.openOptionsMenu();
            return true;
        }
    }

    /* renamed from: menion.android.whereyougo.gui.activity.wherigo.MainMenuActivity$3 */
    class C04193 implements CustomDialog.OnClickListener {

        /* renamed from: menion.android.whereyougo.gui.activity.wherigo.MainMenuActivity$3$1 */
        class C02791 implements Runnable {
            C02791() {
            }

            public void run() {
                ManagerNotify.toastShortMessage(MainMenuActivity.this, MainMenuActivity.this.getString(C0254R.string.save_game_ok));
                MainActivity.wui.setOnSavingFinished(null);
            }
        }

        C04193() {
        }

        public boolean onClick(CustomDialog dialog, View v, int btn) {
            MainActivity.wui.setOnSavingFinished(new C02791());
            new SaveGame().execute(new Void[0]);
            return true;
        }
    }

    /* renamed from: menion.android.whereyougo.gui.activity.wherigo.MainMenuActivity$4 */
    class C04204 implements CustomDialog.OnClickListener {
        C04204() {
        }

        public boolean onClick(CustomDialog dialog, View v, int btn) {
            MainMenuActivity.this.startActivity(new Intent(MainMenuActivity.this, SatelliteActivity.class));
            return true;
        }
    }

    /* renamed from: menion.android.whereyougo.gui.activity.wherigo.MainMenuActivity$5 */
    class C04215 implements CustomDialog.OnClickListener {
        C04215() {
        }

        public boolean onClick(CustomDialog dialog, View v, int btn) {
            MapDataProvider mdp = MapHelper.getMapDataProvider();
            mdp.clear();
            mdp.addAll();
            MainActivity.wui.showScreen(14, null);
            return true;
        }
    }

    private class SaveGame extends menion.android.whereyougo.gui.SaveGame {
        public SaveGame() {
            super(MainMenuActivity.this);
        }
    }

    private class SaveGameOnExit extends SaveGame {
        private SaveGameOnExit() {
            super();
        }

        /* synthetic */ SaveGameOnExit(MainMenuActivity x0, C02781 x1) {
            this();
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(Void result) {
            super.onPostExecute(result);
            Engine.kill();
            MainMenuActivity.this.finish();
        }
    }

    private String getVisibleCartridgeThingsDescription() {
        String description = null;
        Vector<Zone> zones = Engine.instance.cartridge.zones;
        for (int i = 0; i < zones.size(); i++) {
            String des = getVisibleThingsDescription((Zone) zones.elementAt(i));
            if (des != null) {
                if (description == null) {
                    description = "";
                } else {
                    description = description + ", ";
                }
                description = description + des;
            }
        }
        return description;
    }

    private String getVisiblePlayerThingsDescription() {
        Player p = Engine.instance.player;
        String description = null;
        Object key = null;
        while (true) {
            key = p.inventory.next(key);
            if (key == null) {
                return description;
            }
            Object o = p.inventory.rawget(key);
            if ((o instanceof Thing) && ((Thing) o).isVisible()) {
                if (description == null) {
                    description = "";
                } else {
                    description = description + ", ";
                }
                description = description + ((Thing) o).name;
            }
        }
    }

    private int getVisibleTasksCount() {
        int count = 0;
        for (int i = 0; i < Engine.instance.cartridge.tasks.size(); i++) {
            if (((Task) Engine.instance.cartridge.tasks.elementAt(i)).isVisible()) {
                count++;
            }
        }
        return count;
    }

    private String getVisibleTasksDescription() {
        String description = null;
        for (int i = 0; i < Engine.instance.cartridge.tasks.size(); i++) {
            Task a = (Task) Engine.instance.cartridge.tasks.elementAt(i);
            if (a.isVisible()) {
                if (description == null) {
                    description = "";
                } else {
                    description = description + ", ";
                }
                description = description + a.name;
            }
        }
        return description;
    }

    private String getVisibleThingsDescription(Zone z) {
        String description = null;
        if (!z.showThings()) {
            return null;
        }
        Object key = null;
        while (true) {
            key = z.inventory.next(key);
            if (key == null) {
                return description;
            }
            Object o = z.inventory.rawget(key);
            if (!(o instanceof Player) && (o instanceof Thing) && ((Thing) o).isVisible()) {
                if (description == null) {
                    description = "";
                } else {
                    description = description + ", ";
                }
                description = description + ((Thing) o).name;
            }
        }
    }

    private String getVisibleZonesDescription() {
        String description = null;
        Vector<Zone> zones = Engine.instance.cartridge.zones;
        for (int i = 0; i < zones.size(); i++) {
            Zone z = (Zone) zones.get(i);
            if (z.isVisible()) {
                if (description == null) {
                    description = "";
                } else {
                    description = description + ", ";
                }
                description = description + z.name;
                if (z.contains(Engine.instance.player)) {
                    description = description + String.format(" (%s)", new Object[]{getString(C0254R.string.zone_state_inside)});
                }
            }
        }
        return description;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (C0322A.getMain() == null || Engine.instance == null) {
            finish();
            return;
        }
        String saveGameText;
        CustomDialog.OnClickListener saveGameListener;
        setContentView(C0254R.layout.custom_dialog);
        this.listClick = new C02781();
        CustomDialog.setTitle(this, Engine.instance.cartridge.name, null, Integer.MIN_VALUE, null);
        if (Preferences.GLOBAL_SAVEGAME_SLOTS > 0) {
            saveGameText = getString(C0254R.string.save_game_slots);
            saveGameListener = new C04182();
        } else {
            saveGameText = getString(C0254R.string.save_game);
            saveGameListener = new C04193();
        }
        CustomDialog.setBottom(this, getString(C0254R.string.gps), new C04204(), getString(C0254R.string.map), new C04215(), saveGameText, saveGameListener);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        try {
            String text;
            File saveFile = MainActivity.getSaveFile();
            if (saveFile.exists()) {
                text = String.format("%s: %s", new Object[]{getString(C0254R.string.save_file_main), UtilsFormat.formatDatetime(saveFile.lastModified())});
            } else {
                text = String.format("%s", new Object[]{getString(C0254R.string.save_file_main)});
            }
            this.saveGameMainMenuItem = menu.add(0, 0, Preferences.GLOBAL_SAVEGAME_SLOTS, text);
            for (int slot = 1; slot <= Preferences.GLOBAL_SAVEGAME_SLOTS; slot++) {
                if (new File(saveFile.getAbsolutePath() + "." + slot).exists()) {
                    text = String.format("%s %d: %s", new Object[]{getString(C0254R.string.save_game_slot), Integer.valueOf(slot), UtilsFormat.formatDatetime(new File(saveFile.getAbsolutePath() + "." + slot).lastModified())});
                } else {
                    text = String.format("%s %d", new Object[]{getString(C0254R.string.save_game_slot), Integer.valueOf(slot)});
                }
                menu.add(0, slot, Preferences.GLOBAL_SAVEGAME_SLOTS - slot, text);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getGroupId() != 0) {
            return false;
        }
        if (item.getItemId() == 0) {
            MainActivity.wui.setOnSavingFinished(new Runnable() {
                public void run() {
                    try {
                        File saveFile = MainActivity.getSaveFile();
                        item.setTitle(String.format("%s: %s", new Object[]{MainMenuActivity.this.getString(C0254R.string.save_file_main), UtilsFormat.formatDatetime(saveFile.lastModified())}));
                        ManagerNotify.toastShortMessage(MainMenuActivity.this, MainMenuActivity.this.getString(C0254R.string.save_game_ok));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    MainActivity.wui.setOnSavingFinished(null);
                }
            });
        } else {
            MainActivity.wui.setOnSavingFinished(new Runnable() {
                public void run() {
                    try {
                        File saveFile = MainActivity.getSaveFile();
                        FileSystem.copyFile(saveFile, new File(saveFile.getAbsolutePath() + "." + item.getItemId()));
                        MainMenuActivity.this.saveGameMainMenuItem.setTitle(String.format("%s: %s", new Object[]{MainMenuActivity.this.getString(C0254R.string.save_file_main), UtilsFormat.formatDatetime(saveFile.lastModified())}));
                        item.setTitle(String.format("%s %d: %s", new Object[]{MainMenuActivity.this.getString(C0254R.string.save_game_slot), Integer.valueOf(item.getItemId()), UtilsFormat.formatDatetime(slotFile.lastModified())}));
                        ManagerNotify.toastShortMessage(MainMenuActivity.this, String.format("%s %d\n%s", new Object[]{MainMenuActivity.this.getString(C0254R.string.save_game_slot), Integer.valueOf(item.getItemId()), MainMenuActivity.this.getText(C0254R.string.save_game_ok)}));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    MainActivity.wui.setOnSavingFinished(null);
                }
            });
        }
        new SaveGame().execute(new Void[0]);
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Logger.m20d(TAG, "onKeyDown(" + keyCode + ", " + event + ")");
        if (event.getKeyCode() == 4) {
            if (!Preferences.GLOBAL_DOUBLE_CLICK || event.getDownTime() - this.lastPressedTime < 666) {
                UtilsGUI.showDialogQuestion((Activity) this, (int) C0254R.string.save_game_before_exit, new C02828(), new C02839(), null);
                return true;
            }
            this.lastPressedTime = event.getDownTime();
            ManagerNotify.toastShortMessage((int) C0254R.string.msg_exit_game);
            return true;
        } else if (event.getKeyCode() == 84 || super.onKeyDown(keyCode, event)) {
            return true;
        } else {
            return false;
        }
    }

    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        runOnUiThread(new C027710());
    }
}
