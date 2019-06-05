package android.support.p003v7.app;

import android.content.Context;
import android.support.annotation.RequiresApi;
import android.support.p003v7.app.AppCompatDelegateImplV23.AppCompatWindowCallbackV23;
import android.support.p003v7.app.AppCompatDelegateImplV9.PanelFeatureState;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.Window;
import android.view.Window.Callback;
import java.util.List;

@RequiresApi(24)
/* renamed from: android.support.v7.app.AppCompatDelegateImplN */
class AppCompatDelegateImplN extends AppCompatDelegateImplV23 {

    /* renamed from: android.support.v7.app.AppCompatDelegateImplN$AppCompatWindowCallbackN */
    class AppCompatWindowCallbackN extends AppCompatWindowCallbackV23 {
        AppCompatWindowCallbackN(Callback callback) {
            super(callback);
        }

        public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
            PanelFeatureState panelState = AppCompatDelegateImplN.this.getPanelState(0, true);
            if (panelState == null || panelState.menu == null) {
                super.onProvideKeyboardShortcuts(list, menu, i);
            } else {
                super.onProvideKeyboardShortcuts(list, panelState.menu, i);
            }
        }
    }

    AppCompatDelegateImplN(Context context, Window window, AppCompatCallback appCompatCallback) {
        super(context, window, appCompatCallback);
    }

    /* Access modifiers changed, original: 0000 */
    public Callback wrapWindowCallback(Callback callback) {
        return new AppCompatWindowCallbackN(callback);
    }
}