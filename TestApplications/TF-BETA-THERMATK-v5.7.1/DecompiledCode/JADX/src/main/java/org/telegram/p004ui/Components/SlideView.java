package org.telegram.p004ui.Components;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

/* renamed from: org.telegram.ui.Components.SlideView */
public class SlideView extends LinearLayout {
    public String getHeaderName() {
        return "";
    }

    public boolean needBackButton() {
        return false;
    }

    public boolean onBackPressed(boolean z) {
        return true;
    }

    public void onCancelPressed() {
    }

    public void onDestroyActivity() {
    }

    public void onNextPressed() {
    }

    public void onShow() {
    }

    public void restoreStateParams(Bundle bundle) {
    }

    public void saveStateParams(Bundle bundle) {
    }

    public void setParams(Bundle bundle, boolean z) {
    }

    public SlideView(Context context) {
        super(context);
    }
}