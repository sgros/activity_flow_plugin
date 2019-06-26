// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.activity.wherigo;

import menion.android.whereyougo.gui.SaveGame;
import android.widget.ListAdapter;
import menion.android.whereyougo.utils.Utils;
import menion.android.whereyougo.gui.extension.IconedListAdapter;
import android.widget.ListView;
import menion.android.whereyougo.gui.extension.DataInfo;
import java.util.ArrayList;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.view.KeyEvent;
import java.io.File;
import menion.android.whereyougo.utils.UtilsFormat;
import android.view.Menu;
import menion.android.whereyougo.maps.utils.MapDataProvider;
import menion.android.whereyougo.maps.utils.MapHelper;
import android.content.Intent;
import menion.android.whereyougo.gui.activity.SatelliteActivity;
import android.content.Context;
import menion.android.whereyougo.utils.ManagerNotify;
import menion.android.whereyougo.preferences.Preferences;
import android.graphics.Bitmap;
import android.app.Activity;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import cz.matejcik.openwig.EventTable;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.utils.Logger;
import android.view.View;
import android.widget.AdapterView;
import menion.android.whereyougo.utils.A;
import android.os.Bundle;
import cz.matejcik.openwig.Task;
import cz.matejcik.openwig.Player;
import cz.matejcik.openwig.Thing;
import java.util.Vector;
import cz.matejcik.openwig.Zone;
import cz.matejcik.openwig.Engine;
import android.view.MenuItem;
import android.widget.AdapterView$OnItemClickListener;
import menion.android.whereyougo.gui.IRefreshable;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;

public class MainMenuActivity extends CustomActivity implements IRefreshable
{
    private static final int DOUBLE_PRESS_HK_BACK_PERIOD = 666;
    private static final String TAG = "CartridgeMainMenu";
    private long lastPressedTime;
    private AdapterView$OnItemClickListener listClick;
    private MenuItem saveGameMainMenuItem;
    
    public MainMenuActivity() {
        this.lastPressedTime = 0L;
    }
    
    private String getVisibleCartridgeThingsDescription() {
        String str = null;
        final Vector zones = Engine.instance.cartridge.zones;
        String string;
        for (int i = 0; i < zones.size(); ++i, str = string) {
            final String visibleThingsDescription = this.getVisibleThingsDescription(zones.elementAt(i));
            string = str;
            if (visibleThingsDescription != null) {
                String string2;
                if (str == null) {
                    string2 = "";
                }
                else {
                    string2 = str + ", ";
                }
                string = string2 + visibleThingsDescription;
            }
        }
        return str;
    }
    
    private String getVisiblePlayerThingsDescription() {
        final Player player = Engine.instance.player;
        String string = null;
        Object o = null;
        while (true) {
            final Object next = player.inventory.next(o);
            if (next == null) {
                break;
            }
            final Object rawget = player.inventory.rawget(next);
            o = next;
            if (!(rawget instanceof Thing)) {
                continue;
            }
            o = next;
            if (!((Thing)rawget).isVisible()) {
                continue;
            }
            String string2;
            if (string == null) {
                string2 = "";
            }
            else {
                string2 = string + ", ";
            }
            string = string2 + ((Thing)rawget).name;
            o = next;
        }
        return string;
    }
    
    private int getVisibleTasksCount() {
        int n = 0;
        int n2;
        for (int i = 0; i < Engine.instance.cartridge.tasks.size(); ++i, n = n2) {
            n2 = n;
            if (((Task)Engine.instance.cartridge.tasks.elementAt(i)).isVisible()) {
                n2 = n + 1;
            }
        }
        return n;
    }
    
    private String getVisibleTasksDescription() {
        String str = null;
        String string;
        for (int i = 0; i < Engine.instance.cartridge.tasks.size(); ++i, str = string) {
            final Task task = Engine.instance.cartridge.tasks.elementAt(i);
            string = str;
            if (task.isVisible()) {
                String string2;
                if (str == null) {
                    string2 = "";
                }
                else {
                    string2 = str + ", ";
                }
                string = string2 + task.name;
            }
        }
        return str;
    }
    
    private String getVisibleThingsDescription(final Zone zone) {
        String string = null;
        if (!zone.showThings()) {
            string = null;
        }
        else {
            Object o = null;
            while (true) {
                final Object next = zone.inventory.next(o);
                if (next == null) {
                    break;
                }
                final Object rawget = zone.inventory.rawget(next);
                o = next;
                if (rawget instanceof Player) {
                    continue;
                }
                o = next;
                if (!(rawget instanceof Thing)) {
                    continue;
                }
                o = next;
                if (!((Thing)rawget).isVisible()) {
                    continue;
                }
                String string2;
                if (string == null) {
                    string2 = "";
                }
                else {
                    string2 = string + ", ";
                }
                string = string2 + ((Thing)rawget).name;
                o = next;
            }
        }
        return string;
    }
    
    private String getVisibleZonesDescription() {
        String str = null;
        final Vector zones = Engine.instance.cartridge.zones;
        String str2;
        for (int i = 0; i < zones.size(); ++i, str = str2) {
            final Zone zone = zones.get(i);
            str2 = str;
            if (zone.isVisible()) {
                String string;
                if (str == null) {
                    string = "";
                }
                else {
                    string = str + ", ";
                }
                str2 = string + zone.name;
                if (zone.contains(Engine.instance.player)) {
                    str2 += String.format(" (%s)", this.getString(2131165410));
                }
            }
        }
        return str;
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (A.getMain() == null || Engine.instance == null) {
            this.finish();
        }
        else {
            this.setContentView(2130903042);
            this.listClick = (AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
                public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long n) {
                    Logger.d("CartridgeMainMenu", "onItemClick:" + i);
                    switch (i) {
                        case 0: {
                            if (Engine.instance.cartridge.visibleZones() >= 1) {
                                MainActivity.wui.showScreen(4, null);
                                break;
                            }
                            break;
                        }
                        case 1: {
                            if (Engine.instance.cartridge.visibleThings() >= 1) {
                                MainActivity.wui.showScreen(3, null);
                                break;
                            }
                            break;
                        }
                        case 2: {
                            if (Engine.instance.player.visibleThings() >= 1) {
                                MainActivity.wui.showScreen(2, null);
                                break;
                            }
                            break;
                        }
                        case 3: {
                            if (MainMenuActivity.this.getVisibleTasksCount() > 0) {
                                MainActivity.wui.showScreen(5, null);
                                break;
                            }
                            break;
                        }
                    }
                }
            };
            CustomDialog.setTitle(this, Engine.instance.cartridge.name, null, Integer.MIN_VALUE, null);
            String s;
            CustomDialog.OnClickListener onClickListener;
            if (Preferences.GLOBAL_SAVEGAME_SLOTS > 0) {
                s = this.getString(2131165403);
                onClickListener = new CustomDialog.OnClickListener() {
                    @Override
                    public boolean onClick(final CustomDialog customDialog, final View view, final int n) {
                        MainMenuActivity.this.openOptionsMenu();
                        return true;
                    }
                };
            }
            else {
                s = this.getString(2131165399);
                onClickListener = new CustomDialog.OnClickListener() {
                    @Override
                    public boolean onClick(final CustomDialog customDialog, final View view, final int n) {
                        MainActivity.wui.setOnSavingFinished(new Runnable() {
                            @Override
                            public void run() {
                                ManagerNotify.toastShortMessage((Context)MainMenuActivity.this, MainMenuActivity.this.getString(2131165401));
                                MainActivity.wui.setOnSavingFinished(null);
                            }
                        });
                        new SaveGame().execute((Object[])new Void[0]);
                        return true;
                    }
                };
            }
            CustomDialog.setBottom(this, this.getString(2131165205), (CustomDialog.OnClickListener)new CustomDialog.OnClickListener() {
                @Override
                public boolean onClick(final CustomDialog customDialog, final View view, final int n) {
                    MainMenuActivity.this.startActivity(new Intent((Context)MainMenuActivity.this, (Class)SatelliteActivity.class));
                    return true;
                }
            }, this.getString(2131165221), (CustomDialog.OnClickListener)new CustomDialog.OnClickListener() {
                @Override
                public boolean onClick(final CustomDialog customDialog, final View view, final int n) {
                    final MapDataProvider mapDataProvider = MapHelper.getMapDataProvider();
                    mapDataProvider.clear();
                    mapDataProvider.addAll();
                    MainActivity.wui.showScreen(14, null);
                    return true;
                }
            }, s, onClickListener);
        }
    }
    
    public boolean onCreateOptionsMenu(final Menu menu) {
        final boolean b = true;
        super.onCreateOptionsMenu(menu);
        boolean b2;
        try {
            final File saveFile = MainActivity.getSaveFile();
            String s;
            if (saveFile.exists()) {
                s = String.format("%s: %s", this.getString(2131165397), UtilsFormat.formatDatetime(saveFile.lastModified()));
            }
            else {
                s = String.format("%s", this.getString(2131165397));
            }
            this.saveGameMainMenuItem = menu.add(0, 0, Preferences.GLOBAL_SAVEGAME_SLOTS, (CharSequence)s);
            int i = 1;
            while (true) {
                b2 = b;
                if (i > Preferences.GLOBAL_SAVEGAME_SLOTS) {
                    break;
                }
                final File file = new File(saveFile.getAbsolutePath() + "." + i);
                String s2;
                if (file.exists()) {
                    s2 = String.format("%s %d: %s", this.getString(2131165402), i, UtilsFormat.formatDatetime(file.lastModified()));
                }
                else {
                    s2 = String.format("%s %d", this.getString(2131165402), i);
                }
                menu.add(0, i, Preferences.GLOBAL_SAVEGAME_SLOTS - i, (CharSequence)s2);
                ++i;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            b2 = false;
        }
        return b2;
    }
    
    public boolean onKeyDown(final int i, final KeyEvent obj) {
        final boolean b = true;
        Logger.d("CartridgeMainMenu", "onKeyDown(" + i + ", " + obj + ")");
        boolean b2;
        if (obj.getKeyCode() == 4) {
            if (!Preferences.GLOBAL_DOUBLE_CLICK || obj.getDownTime() - this.lastPressedTime < 666L) {
                UtilsGUI.showDialogQuestion(this, 2131165306, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        new SaveGameOnExit().execute((Object[])new Void[0]);
                        MainActivity.selectedFile = null;
                        DetailsActivity.et = null;
                    }
                }, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        Engine.kill();
                        MainActivity.selectedFile = null;
                        DetailsActivity.et = null;
                        MainMenuActivity.this.finish();
                    }
                }, null);
                b2 = b;
            }
            else {
                this.lastPressedTime = obj.getDownTime();
                ManagerNotify.toastShortMessage(2131165344);
                b2 = b;
            }
        }
        else {
            b2 = b;
            if (obj.getKeyCode() != 84) {
                b2 = b;
                if (!super.onKeyDown(i, obj)) {
                    b2 = false;
                }
            }
        }
        return b2;
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        boolean b = false;
        if (menuItem.getGroupId() == 0) {
            if (menuItem.getItemId() == 0) {
                MainActivity.wui.setOnSavingFinished(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                menuItem.setTitle((CharSequence)String.format("%s: %s", MainMenuActivity.this.getString(2131165397), UtilsFormat.formatDatetime(MainActivity.getSaveFile().lastModified())));
                                ManagerNotify.toastShortMessage((Context)MainMenuActivity.this, MainMenuActivity.this.getString(2131165401));
                                MainActivity.wui.setOnSavingFinished(null);
                            }
                            catch (Exception ex) {
                                ex.printStackTrace();
                                continue;
                            }
                            break;
                        }
                    }
                });
            }
            else {
                MainActivity.wui.setOnSavingFinished(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                final File saveFile = MainActivity.getSaveFile();
                                final File file = new File(saveFile.getAbsolutePath() + "." + menuItem.getItemId());
                                FileSystem.copyFile(saveFile, file);
                                MainMenuActivity.this.saveGameMainMenuItem.setTitle((CharSequence)String.format("%s: %s", MainMenuActivity.this.getString(2131165397), UtilsFormat.formatDatetime(saveFile.lastModified())));
                                menuItem.setTitle((CharSequence)String.format("%s %d: %s", MainMenuActivity.this.getString(2131165402), menuItem.getItemId(), UtilsFormat.formatDatetime(file.lastModified())));
                                ManagerNotify.toastShortMessage((Context)MainMenuActivity.this, String.format("%s %d\n%s", MainMenuActivity.this.getString(2131165402), menuItem.getItemId(), MainMenuActivity.this.getText(2131165401)));
                                MainActivity.wui.setOnSavingFinished(null);
                            }
                            catch (Exception ex) {
                                ex.printStackTrace();
                                continue;
                            }
                            break;
                        }
                    }
                });
            }
            new SaveGame().execute((Object[])new Void[0]);
            b = true;
        }
        return b;
    }
    
    public void onResume() {
        super.onResume();
        this.refresh();
    }
    
    @Override
    public void refresh() {
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                if (A.getMain() != null && Engine.instance != null && Engine.instance.cartridge != null) {
                    final ArrayList<DataInfo> list = new ArrayList<DataInfo>();
                    list.add(new DataInfo(MainMenuActivity.this.getString(2131165219) + " (" + Engine.instance.cartridge.visibleZones() + ")", MainMenuActivity.this.getVisibleZonesDescription(), 2130837556));
                    list.add(new DataInfo(MainMenuActivity.this.getString(2131165321) + " (" + Engine.instance.cartridge.visibleThings() + ")", MainMenuActivity.this.getVisibleCartridgeThingsDescription(), 2130837557));
                    list.add(new DataInfo(MainMenuActivity.this.getString(2131165216) + " (" + Engine.instance.player.visibleThings() + ")", MainMenuActivity.this.getVisiblePlayerThingsDescription(), 2130837555));
                    list.add(new DataInfo(MainMenuActivity.this.getString(2131165310) + " (" + Engine.instance.cartridge.visibleTasks() + ")", MainMenuActivity.this.getVisibleTasksDescription(), 2130837558));
                    final ListView listView = new ListView((Context)MainMenuActivity.this);
                    final IconedListAdapter adapter = new IconedListAdapter((Context)MainMenuActivity.this, list, (View)listView);
                    adapter.setMinHeight((int)Utils.getDpPixels(70.0f));
                    adapter.setTextView02Visible(0, true);
                    listView.setAdapter((ListAdapter)adapter);
                    listView.setOnItemClickListener(MainMenuActivity.this.listClick);
                    CustomDialog.setContent(MainMenuActivity.this, (View)listView, 0, true, false);
                }
            }
        });
    }
    
    private class SaveGame extends menion.android.whereyougo.gui.SaveGame
    {
        public SaveGame() {
            super((Context)MainMenuActivity.this);
        }
    }
    
    private class SaveGameOnExit extends SaveGame
    {
        @Override
        protected void onPostExecute(final Void void1) {
            super.onPostExecute(void1);
            Engine.kill();
            MainMenuActivity.this.finish();
        }
    }
}
