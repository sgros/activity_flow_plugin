// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.activity.wherigo;

import java.util.Vector;
import menion.android.whereyougo.gui.activity.MainActivity;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Task;
import menion.android.whereyougo.utils.Images;
import android.graphics.Bitmap;

public class ListTasksActivity extends ListVariousActivity
{
    private static final Bitmap[] stateIcons;
    
    static {
        (stateIcons = new Bitmap[3])[0] = Images.getImageB(2130837571);
        ListTasksActivity.stateIcons[1] = Images.getImageB(2130837569);
        ListTasksActivity.stateIcons[2] = Images.getImageB(2130837570);
    }
    
    @Override
    protected void callStuff(final Object o) {
        final Task task = (Task)o;
        if (task.hasEvent("OnClick")) {
            Engine.callEvent(task, "OnClick", null);
        }
        else {
            MainActivity.wui.showScreen(1, task);
        }
        this.finish();
    }
    
    protected Bitmap getStuffIcon(final Object o) {
        Bitmap bitmap;
        if ((bitmap = super.getStuffIcon(o)) == Images.IMAGE_EMPTY_B) {
            bitmap = ListTasksActivity.stateIcons[0];
        }
        if (((Task)o).state() != 0) {
            bitmap = Images.overlayBitmapToCenter(bitmap, ListTasksActivity.stateIcons[((Task)o).state()]);
        }
        return bitmap;
    }
    
    @Override
    protected String getStuffName(final Object o) {
        return ((Task)o).name;
    }
    
    @Override
    protected Vector<Object> getValidStuff() {
        final Vector<Task> vector = (Vector<Task>)new Vector<Object>();
        for (int i = 0; i < Engine.instance.cartridge.tasks.size(); ++i) {
            final Task e = Engine.instance.cartridge.tasks.get(i);
            if (e.isVisible()) {
                vector.add(e);
            }
        }
        return (Vector<Object>)vector;
    }
    
    @Override
    protected boolean stillValid() {
        return true;
    }
}
