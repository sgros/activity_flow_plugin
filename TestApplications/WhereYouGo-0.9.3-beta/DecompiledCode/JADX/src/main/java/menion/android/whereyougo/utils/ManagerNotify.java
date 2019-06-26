package menion.android.whereyougo.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.preferences.PreferenceValues;

public class ManagerNotify {
    private static final String TAG = "ManagerNotify";

    public static void toastInternetProblem() {
        toastLongMessage(Locale.getString(C0254R.string.problem_with_internet_connection));
    }

    public static void toastLongMessage(Context context, String msg) {
        toastMessage(context, msg, 1);
    }

    public static void toastLongMessage(int msg) {
        toastLongMessage(Locale.getString(msg));
    }

    public static void toastLongMessage(String msg) {
        toastLongMessage(PreferenceValues.getCurrentActivity(), msg);
    }

    private static void toastMessage(final Context context, final String msg, final int time) {
        Logger.m20d(TAG, "toastMessage(" + context + ", " + msg + ", " + time + ")");
        if (context != null && msg != null && msg.length() != 0) {
            try {
                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(context, msg, time).show();
                        }
                    });
                } else {
                    Toast.makeText(context, msg, time).show();
                }
            } catch (Exception e) {
                Logger.m22e(TAG, "toastMessage(" + context + ", " + msg + ", " + time + ")", e);
            }
        }
    }

    public static void toastShortMessage(Context context, String msg) {
        toastMessage(context, msg, 0);
    }

    public static void toastShortMessage(int msg) {
        toastShortMessage(Locale.getString(msg));
    }

    public static void toastShortMessage(String msg) {
        toastShortMessage(PreferenceValues.getCurrentActivity(), msg);
    }

    public static void toastUnexpectedProblem() {
        toastLongMessage(Locale.getString(C0254R.string.unexpected_problem));
    }
}
