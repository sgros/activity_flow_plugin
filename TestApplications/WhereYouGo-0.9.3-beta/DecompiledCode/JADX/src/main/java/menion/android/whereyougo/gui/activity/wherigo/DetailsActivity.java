package menion.android.whereyougo.gui.activity.wherigo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Vector;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.geo.location.ILocationEventListener;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.geo.location.SatellitePosition;
import menion.android.whereyougo.gui.IRefreshable;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.gui.extension.activity.MediaActivity;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog.OnClickListener;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.gui.utils.UtilsWherigo;
import menion.android.whereyougo.guide.Guide;
import menion.android.whereyougo.maps.utils.MapDataProvider;
import menion.android.whereyougo.maps.utils.MapHelper;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.UtilsFormat;
import p005cz.matejcik.openwig.Action;
import p005cz.matejcik.openwig.Engine;
import p005cz.matejcik.openwig.EventTable;
import p005cz.matejcik.openwig.Media;
import p005cz.matejcik.openwig.Task;
import p005cz.matejcik.openwig.Thing;
import p005cz.matejcik.openwig.Zone;

public class DetailsActivity extends MediaActivity implements IRefreshable, ILocationEventListener {
    private static final String TAG = "Details";
    /* renamed from: et */
    public static EventTable f101et;
    private static final String[] taskStates = new String[]{Locale.getString(C0254R.string.pending), Locale.getString(C0254R.string.finished), Locale.getString(C0254R.string.failed)};
    private TextView tvDescription;
    private TextView tvDistance;
    private TextView tvName;
    private TextView tvState;

    /* renamed from: menion.android.whereyougo.gui.activity.wherigo.DetailsActivity$1 */
    class C02721 implements Runnable {
        C02721() {
        }

        public void run() {
            if (DetailsActivity.this.stillValid()) {
                DetailsActivity.this.tvName.setText(DetailsActivity.f101et.name);
                DetailsActivity.this.tvDescription.setText(UtilsGUI.simpleHtml(DetailsActivity.f101et.description));
                DetailsActivity.this.setMedia((Media) DetailsActivity.f101et.table.rawget("Media"));
                DetailsActivity.this.updateNavi();
                DetailsActivity.this.setBottomMenu();
                return;
            }
            Logger.m20d(DetailsActivity.TAG, "refresh(), not valid anymore");
            DetailsActivity.this.finish();
        }
    }

    /* renamed from: menion.android.whereyougo.gui.activity.wherigo.DetailsActivity$2 */
    class C04092 implements OnClickListener {
        C04092() {
        }

        public boolean onClick(CustomDialog dialog, View v, int btn) {
            try {
                DetailsActivity.this.enableGuideOnEventTable();
                MainActivity.callGudingScreen(DetailsActivity.this);
            } catch (Exception e) {
                Logger.m26w(DetailsActivity.TAG, "btn01.click() - unknown problem");
            }
            return true;
        }
    }

    /* renamed from: menion.android.whereyougo.gui.activity.wherigo.DetailsActivity$3 */
    class C04103 implements OnClickListener {
        C04103() {
        }

        public boolean onClick(CustomDialog dialog, View v, int btn) {
            MapDataProvider mdp = MapHelper.getMapDataProvider();
            mdp.clear();
            mdp.addAll();
            MainActivity.wui.showScreen(14, DetailsActivity.f101et);
            return true;
        }
    }

    /* renamed from: menion.android.whereyougo.gui.activity.wherigo.DetailsActivity$4 */
    class C04114 implements OnClickListener {
        C04114() {
        }

        public boolean onClick(CustomDialog dialog, View v, int btn) {
            ListActionsActivity.reset((Thing) DetailsActivity.f101et);
            MainActivity.wui.showScreen(12, DetailsActivity.f101et);
            return true;
        }
    }

    /* renamed from: menion.android.whereyougo.gui.activity.wherigo.DetailsActivity$8 */
    class C04158 implements OnClickListener {
        C04158() {
        }

        public boolean onClick(CustomDialog dialog, View v, int btn) {
            ListActionsActivity.reset((Thing) DetailsActivity.f101et);
            MainActivity.wui.showScreen(12, DetailsActivity.f101et);
            return true;
        }
    }

    private void enableGuideOnEventTable() {
        Location loc = UtilsWherigo.extractLocation(f101et);
        if (loc != null) {
            C0322A.getGuidingContent().guideStart(new Guide(f101et.name, loc));
        } else {
            Logger.m20d(TAG, "enableGuideOnEventTable(), waypoint 'null'");
        }
    }

    public String getName() {
        return TAG;
    }

    public int getPriority() {
        return 2;
    }

    public boolean isRequired() {
        return false;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (C0322A.getMain() == null || Engine.instance == null) {
            finish();
        } else {
            setContentView(C0254R.layout.layout_details);
        }
    }

    public void onGpsStatusChanged(int event, ArrayList<SatellitePosition> arrayList) {
    }

    public void onLocationChanged(Location location) {
        refresh();
    }

    public void onResume() {
        super.onResume();
        Logger.m20d(TAG, "onResume(), et:" + f101et);
        if (f101et != null) {
            setTitle(f101et.name);
            this.tvName = (TextView) findViewById(C0254R.C0253id.layoutDetailsTextViewName);
            this.tvState = (TextView) findViewById(C0254R.C0253id.layoutDetailsTextViewState);
            this.tvDescription = (TextView) findViewById(C0254R.C0253id.layoutDetailsTextViewDescription);
            this.tvDistance = (TextView) findViewById(C0254R.C0253id.layoutDetailsTextViewDistance);
        } else {
            Logger.m24i(TAG, "onCreate(), et == null, end!");
            finish();
        }
        refresh();
    }

    public void onStart() {
        super.onStart();
        if (f101et instanceof Zone) {
            LocationState.addLocationChangeListener(this);
        }
    }

    public void onStatusChanged(String provider, int state, Bundle extras) {
    }

    public void onStop() {
        super.onStop();
        LocationState.removeLocationChangeListener(this);
    }

    public void refresh() {
        runOnUiThread(new C02721());
    }

    private void setBottomMenu() {
        String btn01 = null;
        String btn02 = null;
        String btn03 = null;
        OnClickListener btn01Click = null;
        OnClickListener btn02Click = null;
        OnClickListener btn03Click = null;
        boolean location = f101et.isLocated();
        int actions = 0;
        Vector<Object> validActions = null;
        if (f101et instanceof Thing) {
            Thing t = f101et;
            Logger.m20d(TAG, "actions:" + (t.visibleActions() + Engine.instance.cartridge.visibleUniversalActions()));
            validActions = ListActionsActivity.getValidActions(t);
            actions = validActions.size();
            Logger.m20d(TAG, "validActions:" + actions);
        }
        Logger.m20d(TAG, "setBottomMenu(), loc:" + f101et.isLocated() + ", et:" + f101et + ", act:" + actions);
        if (location) {
            btn01 = getString(C0254R.string.navigate);
            btn01Click = new C04092();
            btn02 = getString(C0254R.string.map);
            btn02Click = new C04103();
        }
        if (actions > 0) {
            if (location) {
                btn03 = getString(C0254R.string.actions_more, new Object[]{Integer.valueOf(actions)});
                btn03Click = new C04114();
            } else if (actions <= 3) {
                final Action action;
                if (actions > 0) {
                    action = (Action) validActions.get(0);
                    btn01 = action.text;
                    btn01Click = new OnClickListener() {
                        public boolean onClick(CustomDialog dialog, View v, int btn) {
                            ListActionsActivity.reset((Thing) DetailsActivity.f101et);
                            ListActionsActivity.callAction(action);
                            return true;
                        }
                    };
                }
                if (actions > 1) {
                    action = (Action) validActions.get(1);
                    btn02 = action.text;
                    btn02Click = new OnClickListener() {
                        public boolean onClick(CustomDialog dialog, View v, int btn) {
                            ListActionsActivity.reset((Thing) DetailsActivity.f101et);
                            ListActionsActivity.callAction(action);
                            return true;
                        }
                    };
                }
                if (actions > 2) {
                    action = (Action) validActions.get(2);
                    btn03 = action.text;
                    btn03Click = new OnClickListener() {
                        public boolean onClick(CustomDialog dialog, View v, int btn) {
                            ListActionsActivity.reset((Thing) DetailsActivity.f101et);
                            ListActionsActivity.callAction(action);
                            return true;
                        }
                    };
                }
            } else {
                btn03 = getString(C0254R.string.actions_more, new Object[]{Integer.valueOf(actions)});
                btn03Click = new C04158();
            }
        }
        CustomDialog.setBottom(this, btn01, btn01Click, btn02, btn02Click, btn03, btn03Click);
        if (f101et instanceof Task) {
            this.tvState.setText(taskStates[f101et.state()]);
        }
    }

    public boolean stillValid() {
        if (f101et == null) {
            return false;
        }
        if (f101et instanceof Thing) {
            return ((Thing) f101et).visibleToPlayer();
        }
        return f101et.isVisible();
    }

    private void updateNavi() {
        if (f101et instanceof Zone) {
            Zone z = f101et;
            String ss = getString(C0254R.string.zone_state_unknown);
            switch (z.contain) {
                case 0:
                    ss = getString(C0254R.string.zone_state_distant);
                    break;
                case 1:
                    ss = getString(C0254R.string.zone_state_near);
                    break;
                case 2:
                    ss = getString(C0254R.string.zone_state_inside);
                    break;
            }
            this.tvState.setText(getString(C0254R.string.zone_state, new Object[]{ss}));
            if (z.contain == 2) {
                this.tvDistance.setText(getString(C0254R.string.zone_distance, new Object[]{getString(C0254R.string.zone_state_inside)}));
                return;
            }
            this.tvDistance.setText(getString(C0254R.string.zone_distance, new Object[]{UtilsFormat.formatDistance(z.distance, false)}));
        }
    }
}
