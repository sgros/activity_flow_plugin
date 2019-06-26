// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.activity.wherigo;

import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.geo.location.SatellitePosition;
import java.util.ArrayList;
import android.os.Bundle;
import menion.android.whereyougo.utils.UtilsFormat;
import cz.matejcik.openwig.Zone;
import java.util.Vector;
import cz.matejcik.openwig.Task;
import cz.matejcik.openwig.Action;
import menion.android.whereyougo.maps.utils.MapDataProvider;
import menion.android.whereyougo.maps.utils.MapHelper;
import android.app.Activity;
import menion.android.whereyougo.gui.activity.MainActivity;
import android.view.View;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Thing;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.guide.IGuide;
import menion.android.whereyougo.guide.Guide;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.gui.utils.UtilsWherigo;
import cz.matejcik.openwig.Media;
import menion.android.whereyougo.preferences.Locale;
import android.widget.TextView;
import cz.matejcik.openwig.EventTable;
import menion.android.whereyougo.geo.location.ILocationEventListener;
import menion.android.whereyougo.gui.IRefreshable;
import menion.android.whereyougo.gui.extension.activity.MediaActivity;

public class DetailsActivity extends MediaActivity implements IRefreshable, ILocationEventListener
{
    private static final String TAG = "Details";
    public static EventTable et;
    private static final String[] taskStates;
    private TextView tvDescription;
    private TextView tvDistance;
    private TextView tvName;
    private TextView tvState;
    
    static {
        taskStates = new String[] { Locale.getString(2131165231), Locale.getString(2131165204), Locale.getString(2131165201) };
    }
    
    private void enableGuideOnEventTable() {
        final Location location = UtilsWherigo.extractLocation(DetailsActivity.et);
        if (location != null) {
            A.getGuidingContent().guideStart(new Guide(DetailsActivity.et.name, location));
        }
        else {
            Logger.d("Details", "enableGuideOnEventTable(), waypoint 'null'");
        }
    }
    
    private void setBottomMenu() {
        String s = null;
        String s2 = null;
        final String s3 = null;
        Object o = null;
        Object o2 = null;
        final CustomDialog.OnClickListener onClickListener = null;
        final boolean located = DetailsActivity.et.isLocated();
        int size = 0;
        Vector<Object> validActions = null;
        if (DetailsActivity.et instanceof Thing) {
            final Thing thing = (Thing)DetailsActivity.et;
            Logger.d("Details", "actions:" + (thing.visibleActions() + Engine.instance.cartridge.visibleUniversalActions()));
            validActions = ListActionsActivity.getValidActions(thing);
            size = validActions.size();
            Logger.d("Details", "validActions:" + size);
        }
        Logger.d("Details", "setBottomMenu(), loc:" + DetailsActivity.et.isLocated() + ", et:" + DetailsActivity.et + ", act:" + size);
        if (located) {
            s = this.getString(2131165345);
            o = new CustomDialog.OnClickListener() {
                @Override
                public boolean onClick(final CustomDialog customDialog, final View view, final int n) {
                    try {
                        DetailsActivity.this.enableGuideOnEventTable();
                        MainActivity.callGudingScreen(DetailsActivity.this);
                        return true;
                    }
                    catch (Exception ex) {
                        Logger.w("Details", "btn01.click() - unknown problem");
                        return true;
                    }
                }
            };
            s2 = this.getString(2131165221);
            o2 = new CustomDialog.OnClickListener() {
                @Override
                public boolean onClick(final CustomDialog customDialog, final View view, final int n) {
                    final MapDataProvider mapDataProvider = MapHelper.getMapDataProvider();
                    mapDataProvider.clear();
                    mapDataProvider.addAll();
                    MainActivity.wui.showScreen(14, DetailsActivity.et);
                    return true;
                }
            };
        }
        String s4 = s;
        CustomDialog.OnClickListener onClickListener2 = (CustomDialog.OnClickListener)o;
        String s5 = s2;
        CustomDialog.OnClickListener onClickListener3 = (CustomDialog.OnClickListener)o2;
        String s6 = s3;
        Object o3 = onClickListener;
        if (size > 0) {
            if (located) {
                s6 = this.getString(2131165326, new Object[] { size });
                o3 = new CustomDialog.OnClickListener() {
                    @Override
                    public boolean onClick(final CustomDialog customDialog, final View view, final int n) {
                        ListActionsActivity.reset((Thing)DetailsActivity.et);
                        MainActivity.wui.showScreen(12, DetailsActivity.et);
                        return true;
                    }
                };
                onClickListener3 = (CustomDialog.OnClickListener)o2;
                s5 = s2;
                onClickListener2 = (CustomDialog.OnClickListener)o;
                s4 = s;
            }
            else if (size <= 3) {
                if (size > 0) {
                    final Action action = validActions.get(0);
                    s = action.text;
                    o = new CustomDialog.OnClickListener() {
                        @Override
                        public boolean onClick(final CustomDialog customDialog, final View view, final int n) {
                            ListActionsActivity.reset((Thing)DetailsActivity.et);
                            ListActionsActivity.callAction(action);
                            return true;
                        }
                    };
                }
                if (size > 1) {
                    final Action action2 = validActions.get(1);
                    s2 = action2.text;
                    o2 = new CustomDialog.OnClickListener() {
                        @Override
                        public boolean onClick(final CustomDialog customDialog, final View view, final int n) {
                            ListActionsActivity.reset((Thing)DetailsActivity.et);
                            ListActionsActivity.callAction(action2);
                            return true;
                        }
                    };
                }
                s4 = s;
                onClickListener2 = (CustomDialog.OnClickListener)o;
                s5 = s2;
                onClickListener3 = (CustomDialog.OnClickListener)o2;
                s6 = s3;
                o3 = onClickListener;
                if (size > 2) {
                    final Action action3 = validActions.get(2);
                    s6 = action3.text;
                    o3 = new CustomDialog.OnClickListener() {
                        @Override
                        public boolean onClick(final CustomDialog customDialog, final View view, final int n) {
                            ListActionsActivity.reset((Thing)DetailsActivity.et);
                            ListActionsActivity.callAction(action3);
                            return true;
                        }
                    };
                    s4 = s;
                    onClickListener2 = (CustomDialog.OnClickListener)o;
                    s5 = s2;
                    onClickListener3 = (CustomDialog.OnClickListener)o2;
                }
            }
            else {
                s6 = this.getString(2131165326, new Object[] { size });
                o3 = new CustomDialog.OnClickListener() {
                    @Override
                    public boolean onClick(final CustomDialog customDialog, final View view, final int n) {
                        ListActionsActivity.reset((Thing)DetailsActivity.et);
                        MainActivity.wui.showScreen(12, DetailsActivity.et);
                        return true;
                    }
                };
                s4 = s;
                onClickListener2 = (CustomDialog.OnClickListener)o;
                s5 = s2;
                onClickListener3 = (CustomDialog.OnClickListener)o2;
            }
        }
        CustomDialog.setBottom(this, s4, onClickListener2, s5, onClickListener3, s6, (CustomDialog.OnClickListener)o3);
        if (DetailsActivity.et instanceof Task) {
            this.tvState.setText((CharSequence)DetailsActivity.taskStates[((Task)DetailsActivity.et).state()]);
        }
    }
    
    private void updateNavi() {
        if (DetailsActivity.et instanceof Zone) {
            final Zone zone = (Zone)DetailsActivity.et;
            String s = this.getString(2131165412);
            switch (zone.contain) {
                case 0: {
                    s = this.getString(2131165409);
                    break;
                }
                case 1: {
                    s = this.getString(2131165411);
                    break;
                }
                case 2: {
                    s = this.getString(2131165410);
                    break;
                }
            }
            this.tvState.setText((CharSequence)this.getString(2131165408, new Object[] { s }));
            if (zone.contain == 2) {
                this.tvDistance.setText((CharSequence)this.getString(2131165407, new Object[] { this.getString(2131165410) }));
            }
            else {
                this.tvDistance.setText((CharSequence)this.getString(2131165407, new Object[] { UtilsFormat.formatDistance(zone.distance, false) }));
            }
        }
    }
    
    @Override
    public String getName() {
        return "Details";
    }
    
    @Override
    public int getPriority() {
        return 2;
    }
    
    @Override
    public boolean isRequired() {
        return false;
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (A.getMain() == null || Engine.instance == null) {
            this.finish();
        }
        else {
            this.setContentView(2130903052);
        }
    }
    
    @Override
    public void onGpsStatusChanged(final int n, final ArrayList<SatellitePosition> list) {
    }
    
    @Override
    public void onLocationChanged(final Location location) {
        this.refresh();
    }
    
    public void onResume() {
        super.onResume();
        Logger.d("Details", "onResume(), et:" + DetailsActivity.et);
        if (DetailsActivity.et != null) {
            this.setTitle((CharSequence)DetailsActivity.et.name);
            this.tvName = (TextView)this.findViewById(2131492940);
            this.tvState = (TextView)this.findViewById(2131492941);
            this.tvDescription = (TextView)this.findViewById(2131492943);
            this.tvDistance = (TextView)this.findViewById(2131492942);
        }
        else {
            Logger.i("Details", "onCreate(), et == null, end!");
            this.finish();
        }
        this.refresh();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        if (DetailsActivity.et instanceof Zone) {
            LocationState.addLocationChangeListener(this);
        }
    }
    
    @Override
    public void onStatusChanged(final String s, final int n, final Bundle bundle) {
    }
    
    public void onStop() {
        super.onStop();
        LocationState.removeLocationChangeListener(this);
    }
    
    @Override
    public void refresh() {
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                if (!DetailsActivity.this.stillValid()) {
                    Logger.d("Details", "refresh(), not valid anymore");
                    DetailsActivity.this.finish();
                }
                else {
                    DetailsActivity.this.tvName.setText((CharSequence)DetailsActivity.et.name);
                    DetailsActivity.this.tvDescription.setText(UtilsGUI.simpleHtml(DetailsActivity.et.description));
                    MediaActivity.this.setMedia((Media)DetailsActivity.et.table.rawget("Media"));
                    DetailsActivity.this.updateNavi();
                    DetailsActivity.this.setBottomMenu();
                }
            }
        });
    }
    
    public boolean stillValid() {
        boolean b;
        if (DetailsActivity.et != null) {
            if (DetailsActivity.et instanceof Thing) {
                b = ((Thing)DetailsActivity.et).visibleToPlayer();
            }
            else {
                b = DetailsActivity.et.isVisible();
            }
        }
        else {
            b = false;
        }
        return b;
    }
}
