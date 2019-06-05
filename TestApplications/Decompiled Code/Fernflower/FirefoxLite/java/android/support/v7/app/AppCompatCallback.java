package android.support.v7.app;

import android.support.v7.view.ActionMode;

public interface AppCompatCallback {
   void onSupportActionModeFinished(ActionMode var1);

   void onSupportActionModeStarted(ActionMode var1);

   ActionMode onWindowStartingSupportActionMode(ActionMode.Callback var1);
}
