package org.telegram.p004ui;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.telegram.messenger.C1067R;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.MediaActionDrawable;

/* renamed from: org.telegram.ui.TestActivity */
public class TestActivity extends BaseFragment {
    int num = 0;
    /* renamed from: p */
    int f635p = 0;

    /* renamed from: org.telegram.ui.TestActivity$1 */
    class C43351 extends ActionBarMenuOnItemClick {
        C43351() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                TestActivity.this.finishFragment();
            }
        }
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle("Test");
        this.actionBar.setActionBarMenuOnItemClick(new C43351());
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
        this.fragmentView = frameLayout;
        MediaActionDrawable mediaActionDrawable = new MediaActionDrawable();
        mediaActionDrawable.setIcon(2, false);
        ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(mediaActionDrawable);
        imageView.getClass();
        mediaActionDrawable.setDelegate(new C3901-$$Lambda$pmzqDjiJ3K2EPQb0Rq1MYHdTzL0(imageView));
        frameLayout.addView(imageView, LayoutHelper.createFrame(48, 48, 17));
        frameLayout.setOnClickListener(new C2080-$$Lambda$TestActivity$8SA6jL3MHG2fMuwJNTKXrp0KhUw(mediaActionDrawable));
        return this.fragmentView;
    }

    static /* synthetic */ void lambda$createView$0(MediaActionDrawable mediaActionDrawable, View view) {
        int currentIcon = mediaActionDrawable.getCurrentIcon();
        boolean z = true;
        if (currentIcon == 2) {
            currentIcon = 3;
        } else if (currentIcon == 3) {
            currentIcon = 0;
        } else if (currentIcon == 0) {
            currentIcon = 2;
            z = false;
        }
        mediaActionDrawable.setIcon(currentIcon, z);
    }
}
