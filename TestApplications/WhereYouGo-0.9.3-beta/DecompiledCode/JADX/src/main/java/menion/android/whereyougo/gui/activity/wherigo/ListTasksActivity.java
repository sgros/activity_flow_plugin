package menion.android.whereyougo.gui.activity.wherigo;

import android.graphics.Bitmap;
import java.util.Vector;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.utils.Images;
import p005cz.matejcik.openwig.Engine;
import p005cz.matejcik.openwig.Task;

public class ListTasksActivity extends ListVariousActivity {
    private static final Bitmap[] stateIcons = new Bitmap[3];

    static {
        stateIcons[0] = Images.getImageB(C0254R.C0252drawable.task_pending);
        stateIcons[1] = Images.getImageB(C0254R.C0252drawable.task_done);
        stateIcons[2] = Images.getImageB(C0254R.C0252drawable.task_failed);
    }

    /* Access modifiers changed, original: protected */
    public void callStuff(Object what) {
        Task z = (Task) what;
        if (z.hasEvent("OnClick")) {
            Engine.callEvent(z, "OnClick", null);
        } else {
            MainActivity.wui.showScreen(1, z);
        }
        finish();
    }

    /* Access modifiers changed, original: protected */
    public Bitmap getStuffIcon(Object what) {
        Bitmap bmp = super.getStuffIcon(what);
        if (bmp == Images.IMAGE_EMPTY_B) {
            bmp = stateIcons[0];
        }
        return ((Task) what).state() == 0 ? bmp : Images.overlayBitmapToCenter(bmp, stateIcons[((Task) what).state()]);
    }

    /* Access modifiers changed, original: protected */
    public String getStuffName(Object what) {
        return ((Task) what).name;
    }

    /* Access modifiers changed, original: protected */
    public Vector<Object> getValidStuff() {
        Vector<Object> newtasks = new Vector();
        for (int i = 0; i < Engine.instance.cartridge.tasks.size(); i++) {
            Task t = (Task) Engine.instance.cartridge.tasks.get(i);
            if (t.isVisible()) {
                newtasks.add(t);
            }
        }
        return newtasks;
    }

    /* Access modifiers changed, original: protected */
    public boolean stillValid() {
        return true;
    }
}
