// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.activity.wherigo;

import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;
import android.widget.ListAdapter;
import menion.android.whereyougo.gui.extension.IconedListAdapter;
import cz.matejcik.openwig.Action;
import cz.matejcik.openwig.Thing;
import menion.android.whereyougo.gui.extension.DataInfo;
import java.util.ArrayList;
import android.content.Context;
import android.app.Activity;
import android.view.View;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import menion.android.whereyougo.utils.A;
import android.os.Bundle;
import android.graphics.BitmapFactory;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Media;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.UtilsFormat;
import android.graphics.Path;
import menion.android.whereyougo.geo.location.LocationState;
import cz.matejcik.openwig.Zone;
import menion.android.whereyougo.geo.location.Location;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import menion.android.whereyougo.utils.Images;
import android.graphics.Bitmap;
import cz.matejcik.openwig.EventTable;
import android.graphics.Paint$Style;
import android.graphics.Typeface;
import menion.android.whereyougo.utils.Utils;
import java.util.Vector;
import android.widget.ListView;
import android.graphics.Paint;
import menion.android.whereyougo.gui.IRefreshable;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;

public abstract class ListVariousActivity extends CustomActivity implements IRefreshable
{
    private static final String TAG = "ListVarious";
    private static final Paint paintArrow;
    private static final Paint paintArrowBorder;
    private static final Paint paintText;
    private ListView lv;
    private final Vector<Object> stuff;
    private String title;
    
    static {
        (paintText = new Paint()).setColor(-65536);
        ListVariousActivity.paintText.setTextSize(Utils.getDpPixels(12.0f));
        ListVariousActivity.paintText.setTypeface(Typeface.DEFAULT_BOLD);
        ListVariousActivity.paintText.setAntiAlias(true);
        (paintArrow = new Paint()).setColor(-256);
        ListVariousActivity.paintArrow.setAntiAlias(true);
        ListVariousActivity.paintArrow.setStyle(Paint$Style.FILL);
        (paintArrowBorder = new Paint()).setColor(-16777216);
        ListVariousActivity.paintArrowBorder.setAntiAlias(true);
        ListVariousActivity.paintArrowBorder.setStyle(Paint$Style.STROKE);
    }
    
    public ListVariousActivity() {
        this.stuff = new Vector<Object>();
    }
    
    private Bitmap getLocatedIcon(EventTable obj) {
        if (!((EventTable)obj).isLocated()) {
            obj = Images.IMAGE_EMPTY_B;
        }
        else {
            while (true) {
                while (true) {
                    Location location = null;
                    Label_0642: {
                        try {
                            final Bitmap bitmap = Bitmap.createBitmap((int)Utils.getDpPixels(80.0f), (int)Utils.getDpPixels(40.0f), Bitmap$Config.ARGB_8888);
                            final Canvas canvas = new Canvas(bitmap);
                            canvas.drawColor(0);
                            location = new Location("ListVarious");
                            if (!(obj instanceof Zone)) {
                                break Label_0642;
                            }
                            location.setLatitude(((Zone)obj).nearestPoint.latitude);
                            location.setLongitude(((Zone)obj).nearestPoint.longitude);
                            final float bearingTo = LocationState.getLocation().bearingTo(location);
                            final float distanceTo = LocationState.getLocation().distanceTo(location);
                            final int n = bitmap.getHeight() / 2;
                            final int n2 = bitmap.getHeight() / 2;
                            final double n3 = bearingTo / 57.29578f;
                            final float n4 = (float)(Math.sin(n3) * (n * 0.9));
                            final float n5 = (float)(Math.cos(n3) * (n * 0.9));
                            final double n6 = (180.0f + bearingTo) / 57.29578f;
                            final float n7 = (float)(Math.sin(n6) * (n * 0.2));
                            final float n8 = (float)(Math.cos(n6) * (n * 0.2));
                            final double n9 = (140.0f + bearingTo) / 57.29578f;
                            final float n10 = (float)(Math.sin(n9) * (n * 0.6));
                            final float n11 = (float)(Math.cos(n9) * (n * 0.6));
                            final double n12 = (220.0f + bearingTo) / 57.29578f;
                            final float n13 = (float)(Math.sin(n12) * (n * 0.6));
                            final float n14 = (float)(Math.cos(n12) * (n * 0.6));
                            final Path path = new Path();
                            path.moveTo(n + n4, n2 - n5);
                            path.lineTo(n + n7, n2 - n8);
                            path.lineTo(n + n10, n2 - n11);
                            canvas.drawPath(path, ListVariousActivity.paintArrow);
                            final Path path2 = new Path();
                            path2.moveTo(n + n4, n2 - n5);
                            path2.lineTo(n + n7, n2 - n8);
                            path2.lineTo(n + n13, n2 - n14);
                            canvas.drawPath(path2, ListVariousActivity.paintArrow);
                            canvas.drawLine(n + n4, n2 - n5, n + n10, n2 - n11, ListVariousActivity.paintArrowBorder);
                            canvas.drawLine(n + n4, n2 - n5, n + n13, n2 - n14, ListVariousActivity.paintArrowBorder);
                            canvas.drawLine(n + n7, n2 - n8, n + n10, n2 - n11, ListVariousActivity.paintArrowBorder);
                            canvas.drawLine(n + n7, n2 - n8, n + n13, n2 - n14, ListVariousActivity.paintArrowBorder);
                            canvas.drawText(UtilsFormat.formatDistance(distanceTo, false), (float)(n * 2 + 2), n2 + ListVariousActivity.paintText.getTextSize() / 2.0f, ListVariousActivity.paintText);
                            obj = bitmap;
                        }
                        catch (Exception ex) {
                            Logger.e("ListVarious", "getLocatedIcon(" + obj + ")", ex);
                            obj = Images.IMAGE_EMPTY_B;
                        }
                        break;
                    }
                    location.setLatitude(((EventTable)obj).position.latitude);
                    location.setLongitude(((EventTable)obj).position.longitude);
                    continue;
                }
            }
        }
        return (Bitmap)obj;
    }
    
    protected abstract void callStuff(final Object p0);
    
    Bitmap getStuffIcon(final Object o) {
        Bitmap bitmap;
        if (((EventTable)o).isLocated()) {
            bitmap = this.getLocatedIcon((EventTable)o);
        }
        else {
            final Media media = (Media)((EventTable)o).table.rawget("Icon");
            if (media != null) {
                try {
                    final byte[] mediaFile = Engine.mediaFile(media);
                    if (mediaFile != null) {
                        bitmap = BitmapFactory.decodeByteArray(mediaFile, 0, mediaFile.length);
                        return bitmap;
                    }
                }
                catch (Exception ex) {
                    Logger.e("ListVarious", "getStuffIcon()", ex);
                }
            }
            bitmap = Images.IMAGE_EMPTY_B;
        }
        return bitmap;
    }
    
    protected abstract String getStuffName(final Object p0);
    
    protected abstract Vector<Object> getValidStuff();
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (A.getMain() == null || Engine.instance == null) {
            this.finish();
        }
        else {
            this.setContentView(2130903042);
            if (this.getIntent().getStringExtra("title") != null) {
                this.title = this.getIntent().getStringExtra("title");
            }
            CustomDialog.setTitle(this, this.title, null, 2130837528, (CustomDialog.OnClickListener)new CustomDialog.OnClickListener() {
                @Override
                public boolean onClick(final CustomDialog customDialog, final View view, final int n) {
                    ListVariousActivity.this.finish();
                    return true;
                }
            });
            CustomDialog.setContent(this, (View)(this.lv = new ListView((Context)this)), 0, false, true);
            CustomDialog.setBottom(this, null, null, null, null, null, null);
        }
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
                if (!ListVariousActivity.this.stillValid()) {
                    ListVariousActivity.this.finish();
                }
                else {
                    final Vector<Object> validStuff = ListVariousActivity.this.getValidStuff();
                    final int firstVisiblePosition = ListVariousActivity.this.lv.getFirstVisiblePosition();
                    ListVariousActivity.this.stuff.clear();
                    for (int i = 0; i < validStuff.size(); ++i) {
                        final Object value = validStuff.get(i);
                        if (value != null) {
                            ListVariousActivity.this.stuff.add(value);
                        }
                    }
                    final ArrayList<DataInfo> list = new ArrayList<DataInfo>();
                    for (int j = 0; j < ListVariousActivity.this.stuff.size(); ++j) {
                        final Object value2 = ListVariousActivity.this.stuff.get(j);
                        DataInfo e;
                        if (value2 instanceof Thing) {
                            e = new DataInfo(((Thing)value2).name, null, ListVariousActivity.this.getStuffIcon(value2));
                        }
                        else if (value2 instanceof Action) {
                            e = new DataInfo(((Action)value2).text, null, ListVariousActivity.this.getStuffIcon(value2));
                        }
                        else {
                            e = new DataInfo(value2.toString(), null, ListVariousActivity.this.getStuffIcon(value2));
                        }
                        list.add(e);
                    }
                    final IconedListAdapter adapter = new IconedListAdapter((Context)ListVariousActivity.this, list, (View)ListVariousActivity.this.lv);
                    adapter.setMultiplyImageSize(1.5f);
                    adapter.setTextView02Visible(0, true);
                    ListVariousActivity.this.lv.setAdapter((ListAdapter)adapter);
                    ListVariousActivity.this.lv.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
                        public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                            Logger.d("ListVarious", "onItemClick:" + n);
                            final Object o = null;
                            // monitorenter(this)
                            Object value = o;
                            Label_0068: {
                                if (n < 0) {
                                    break Label_0068;
                                }
                                value = o;
                                try {
                                    if (n < ListVariousActivity.this.stuff.size()) {
                                        value = ListVariousActivity.this.stuff.get(n);
                                    }
                                    // monitorexit(this)
                                    if (value != null) {
                                        ListVariousActivity.this.callStuff(value);
                                    }
                                }
                                finally {
                                }
                                // monitorexit(this)
                            }
                        }
                    });
                    ListVariousActivity.this.lv.setSelectionFromTop(firstVisiblePosition, 5);
                }
            }
        });
    }
    
    protected abstract boolean stillValid();
}
