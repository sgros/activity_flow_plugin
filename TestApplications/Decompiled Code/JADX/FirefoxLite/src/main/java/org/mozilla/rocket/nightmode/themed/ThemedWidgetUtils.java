package org.mozilla.rocket.nightmode.themed;

import org.mozilla.rocket.C0769R;

/* compiled from: ThemedWidgetUtils.kt */
public final class ThemedWidgetUtils {
    public static final ThemedWidgetUtils INSTANCE = new ThemedWidgetUtils();
    private static final int[] STATE_NIGHT_MODE = new int[]{C0769R.attr.state_night};

    private ThemedWidgetUtils() {
    }

    public final int[] getSTATE_NIGHT_MODE() {
        return STATE_NIGHT_MODE;
    }
}
