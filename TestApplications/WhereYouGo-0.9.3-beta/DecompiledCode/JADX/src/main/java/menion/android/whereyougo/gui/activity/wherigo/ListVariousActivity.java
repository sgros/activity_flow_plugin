package menion.android.whereyougo.gui.activity.wherigo;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.p000v4.internal.view.SupportMenu;
import android.support.p000v4.view.InputDeviceCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Vector;
import locus.api.objects.extra.Location;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.gui.IRefreshable;
import menion.android.whereyougo.gui.extension.DataInfo;
import menion.android.whereyougo.gui.extension.IconedListAdapter;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog.OnClickListener;
import menion.android.whereyougo.gui.utils.UtilsWherigo;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.Images;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.Utils;
import menion.android.whereyougo.utils.UtilsFormat;
import p005cz.matejcik.openwig.Action;
import p005cz.matejcik.openwig.Engine;
import p005cz.matejcik.openwig.EventTable;
import p005cz.matejcik.openwig.Media;
import p005cz.matejcik.openwig.Thing;

public abstract class ListVariousActivity extends CustomActivity implements IRefreshable {
    private static final String TAG = "ListVarious";
    private static final Paint paintArrow = new Paint();
    private static final Paint paintArrowBorder = new Paint();
    private static final Paint paintText = new Paint();
    /* renamed from: lv */
    private ListView f100lv;
    private final Vector<Object> stuff = new Vector();
    private String title;

    /* renamed from: menion.android.whereyougo.gui.activity.wherigo.ListVariousActivity$2 */
    class C02762 implements Runnable {

        /* renamed from: menion.android.whereyougo.gui.activity.wherigo.ListVariousActivity$2$1 */
        class C02751 implements OnItemClickListener {
            C02751() {
            }

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Logger.m20d(ListVariousActivity.TAG, "onItemClick:" + position);
                Object s = null;
                synchronized (this) {
                    if (position >= 0) {
                        if (position < ListVariousActivity.this.stuff.size()) {
                            s = ListVariousActivity.this.stuff.get(position);
                        }
                    }
                }
                if (s != null) {
                    ListVariousActivity.this.callStuff(s);
                }
            }
        }

        C02762() {
        }

        public void run() {
            if (ListVariousActivity.this.stillValid()) {
                int i;
                Object s;
                Vector<Object> newStuff = ListVariousActivity.this.getValidStuff();
                int scrollY = ListVariousActivity.this.f100lv.getFirstVisiblePosition();
                ListVariousActivity.this.stuff.clear();
                for (i = 0; i < newStuff.size(); i++) {
                    s = newStuff.get(i);
                    if (s != null) {
                        ListVariousActivity.this.stuff.add(s);
                    }
                }
                ArrayList<DataInfo> data = new ArrayList();
                for (i = 0; i < ListVariousActivity.this.stuff.size(); i++) {
                    DataInfo dataInfo;
                    s = ListVariousActivity.this.stuff.get(i);
                    if (s instanceof Thing) {
                        dataInfo = new DataInfo(((Thing) s).name, null, ListVariousActivity.this.getStuffIcon(s));
                    } else if (s instanceof Action) {
                        dataInfo = new DataInfo(((Action) s).text, null, ListVariousActivity.this.getStuffIcon(s));
                    } else {
                        dataInfo = new DataInfo(s.toString(), null, ListVariousActivity.this.getStuffIcon(s));
                    }
                    data.add(dataInfo);
                }
                IconedListAdapter adapter = new IconedListAdapter(ListVariousActivity.this, data, ListVariousActivity.this.f100lv);
                adapter.setMultiplyImageSize(1.5f);
                adapter.setTextView02Visible(0, true);
                ListVariousActivity.this.f100lv.setAdapter(adapter);
                ListVariousActivity.this.f100lv.setOnItemClickListener(new C02751());
                ListVariousActivity.this.f100lv.setSelectionFromTop(scrollY, 5);
                return;
            }
            ListVariousActivity.this.finish();
        }
    }

    /* renamed from: menion.android.whereyougo.gui.activity.wherigo.ListVariousActivity$1 */
    class C04171 implements OnClickListener {
        C04171() {
        }

        public boolean onClick(CustomDialog dialog, View v, int btn) {
            ListVariousActivity.this.finish();
            return true;
        }
    }

    public abstract void callStuff(Object obj);

    public abstract String getStuffName(Object obj);

    public abstract Vector<Object> getValidStuff();

    public abstract boolean stillValid();

    static {
        paintText.setColor(SupportMenu.CATEGORY_MASK);
        paintText.setTextSize(Utils.getDpPixels(12.0f));
        paintText.setTypeface(Typeface.DEFAULT_BOLD);
        paintText.setAntiAlias(true);
        paintArrow.setColor(InputDeviceCompat.SOURCE_ANY);
        paintArrow.setAntiAlias(true);
        paintArrow.setStyle(Style.FILL);
        paintArrowBorder.setColor(-16777216);
        paintArrowBorder.setAntiAlias(true);
        paintArrowBorder.setStyle(Style.STROKE);
    }

    private Bitmap getLocatedIcon(EventTable thing) {
        if (!thing.isLocated()) {
            return Images.IMAGE_EMPTY_B;
        }
        try {
            Bitmap bitmap = Bitmap.createBitmap((int) Utils.getDpPixels(80.0f), (int) Utils.getDpPixels(40.0f), Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);
            c.drawColor(0);
            Location location = UtilsWherigo.extractLocation(thing);
            float azimuth = LocationState.getLocation().bearingTo(location);
            float distance = LocationState.getLocation().distanceTo(location);
            int radius = bitmap.getHeight() / 2;
            int cX = radius;
            int cY = bitmap.getHeight() / 2;
            double a = (double) (azimuth / 57.29578f);
            float x1 = (float) (Math.sin(a) * (((double) radius) * 0.9d));
            float y1 = (float) (Math.cos(a) * (((double) radius) * 0.9d));
            a = (double) ((180.0f + azimuth) / 57.29578f);
            float x2 = (float) (Math.sin(a) * (((double) radius) * 0.2d));
            float y2 = (float) (Math.cos(a) * (((double) radius) * 0.2d));
            a = (double) ((140.0f + azimuth) / 57.29578f);
            float x3 = (float) (Math.sin(a) * (((double) radius) * 0.6d));
            float y3 = (float) (Math.cos(a) * (((double) radius) * 0.6d));
            a = (double) ((220.0f + azimuth) / 57.29578f);
            float x4 = (float) (Math.sin(a) * (((double) radius) * 0.6d));
            float y4 = (float) (Math.cos(a) * (((double) radius) * 0.6d));
            Path path = new Path();
            path.moveTo(((float) cX) + x1, ((float) cY) - y1);
            path.lineTo(((float) cX) + x2, ((float) cY) - y2);
            path.lineTo(((float) cX) + x3, ((float) cY) - y3);
            c.drawPath(path, paintArrow);
            path = new Path();
            path.moveTo(((float) cX) + x1, ((float) cY) - y1);
            path.lineTo(((float) cX) + x2, ((float) cY) - y2);
            path.lineTo(((float) cX) + x4, ((float) cY) - y4);
            c.drawPath(path, paintArrow);
            c.drawLine(((float) cX) + x1, ((float) cY) - y1, ((float) cX) + x3, ((float) cY) - y3, paintArrowBorder);
            c.drawLine(((float) cX) + x1, ((float) cY) - y1, ((float) cX) + x4, ((float) cY) - y4, paintArrowBorder);
            c.drawLine(((float) cX) + x2, ((float) cY) - y2, ((float) cX) + x3, ((float) cY) - y3, paintArrowBorder);
            c.drawLine(((float) cX) + x2, ((float) cY) - y2, ((float) cX) + x4, ((float) cY) - y4, paintArrowBorder);
            c.drawText(UtilsFormat.formatDistance((double) distance, false), (float) ((radius * 2) + 2), ((float) cY) + (paintText.getTextSize() / 2.0f), paintText);
            return bitmap;
        } catch (Exception e) {
            Logger.m22e(TAG, "getLocatedIcon(" + thing + ")", e);
            return Images.IMAGE_EMPTY_B;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Bitmap getStuffIcon(Object object) {
        if (((EventTable) object).isLocated()) {
            return getLocatedIcon((EventTable) object);
        }
        Media media = (Media) ((EventTable) object).table.rawget("Icon");
        if (media != null) {
            try {
                byte[] icon = Engine.mediaFile(media);
                if (icon != null) {
                    return BitmapFactory.decodeByteArray(icon, 0, icon.length);
                }
            } catch (Exception e) {
                Logger.m22e(TAG, "getStuffIcon()", e);
            }
        }
        return Images.IMAGE_EMPTY_B;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (C0322A.getMain() == null || Engine.instance == null) {
            finish();
            return;
        }
        setContentView(C0254R.layout.custom_dialog);
        if (getIntent().getStringExtra("title") != null) {
            this.title = getIntent().getStringExtra("title");
        }
        CustomDialog.setTitle(this, this.title, null, C0254R.C0252drawable.ic_cancel, new C04171());
        this.f100lv = new ListView(this);
        CustomDialog.setContent(this, this.f100lv, 0, false, true);
        CustomDialog.setBottom(this, null, null, null, null, null, null);
    }

    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        runOnUiThread(new C02762());
    }
}
