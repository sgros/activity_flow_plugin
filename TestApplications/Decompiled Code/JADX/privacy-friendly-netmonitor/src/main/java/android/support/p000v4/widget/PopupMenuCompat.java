package android.support.p000v4.widget;

import android.os.Build.VERSION;
import android.view.View.OnTouchListener;
import android.widget.PopupMenu;

/* renamed from: android.support.v4.widget.PopupMenuCompat */
public final class PopupMenuCompat {
    private PopupMenuCompat() {
    }

    public static OnTouchListener getDragToOpenListener(Object obj) {
        return VERSION.SDK_INT >= 19 ? ((PopupMenu) obj).getDragToOpenListener() : null;
    }
}
