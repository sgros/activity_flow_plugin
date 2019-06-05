package android.support.p004v7.app;

import android.support.p004v7.view.ActionMode;
import android.support.p004v7.view.ActionMode.Callback;

/* renamed from: android.support.v7.app.AppCompatCallback */
public interface AppCompatCallback {
    void onSupportActionModeFinished(ActionMode actionMode);

    void onSupportActionModeStarted(ActionMode actionMode);

    ActionMode onWindowStartingSupportActionMode(Callback callback);
}
