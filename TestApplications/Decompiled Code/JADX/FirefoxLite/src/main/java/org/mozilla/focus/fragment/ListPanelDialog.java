package org.mozilla.focus.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.p001v4.app.DialogFragment;
import android.support.p001v4.widget.NestedScrollView;
import android.support.p001v4.widget.NestedScrollView.OnScrollChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.history.BrowsingHistoryFragment;
import org.mozilla.focus.screenshot.ScreenshotGridFragment;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.rocket.C0769R;

public class ListPanelDialog extends DialogFragment {
    private View bookmarksTouchArea;
    private BottomSheetBehavior bottomSheetBehavior;
    private View divider;
    private View downloadsTouchArea;
    private boolean firstLaunch = true;
    private View historyTouchArea;
    private View panelBottom;
    private View screenshotsTouchArea;
    private NestedScrollView scrollView;
    private TextView title;

    /* renamed from: org.mozilla.focus.fragment.ListPanelDialog$3 */
    class C04733 implements OnClickListener {
        C04733() {
        }

        public void onClick(View view) {
            ListPanelDialog.this.dismissAllowingStateLoss();
        }
    }

    /* renamed from: org.mozilla.focus.fragment.ListPanelDialog$4 */
    class C04744 implements OnClickListener {
        C04744() {
        }

        public void onClick(View view) {
            ListPanelDialog.this.showItem(4);
            TelemetryWrapper.showPanelBookmark();
        }
    }

    /* renamed from: org.mozilla.focus.fragment.ListPanelDialog$5 */
    class C04755 implements OnClickListener {
        C04755() {
        }

        public void onClick(View view) {
            ListPanelDialog.this.showItem(1);
            TelemetryWrapper.showPanelDownload();
        }
    }

    /* renamed from: org.mozilla.focus.fragment.ListPanelDialog$6 */
    class C04766 implements OnClickListener {
        C04766() {
        }

        public void onClick(View view) {
            ListPanelDialog.this.showItem(2);
            TelemetryWrapper.showPanelHistory();
        }
    }

    /* renamed from: org.mozilla.focus.fragment.ListPanelDialog$7 */
    class C04777 implements OnClickListener {
        C04777() {
        }

        public void onClick(View view) {
            ListPanelDialog.this.showItem(3);
            TelemetryWrapper.showPanelCapture();
        }
    }

    /* renamed from: org.mozilla.focus.fragment.ListPanelDialog$1 */
    class C06951 implements OnScrollChangeListener {
        C06951() {
        }

        public void onScrollChange(NestedScrollView nestedScrollView, int i, int i2, int i3, int i4) {
            i = nestedScrollView.getMeasuredHeight();
            if (i2 > i4 && (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight()) - i2 < i) {
                final PanelFragment panelFragment = (PanelFragment) ListPanelDialog.this.getChildFragmentManager().findFragmentById(C0427R.C0426id.main_content);
                if (panelFragment != null && panelFragment.isVisible()) {
                    new Thread(new Runnable() {
                        public void run() {
                            panelFragment.tryLoadMore();
                        }
                    }).start();
                }
            }
        }
    }

    /* renamed from: org.mozilla.focus.fragment.ListPanelDialog$2 */
    class C06962 extends BottomSheetCallback {
        private int collapseHeight = -1;
        private float translationY = -2.14748365E9f;

        C06962() {
        }

        public void onStateChanged(View view, int i) {
            if (i == 5) {
                ListPanelDialog.this.dismissAllowingStateLoss();
            }
        }

        public void onSlide(View view, float f) {
            float f2 = 0.0f;
            if (f < 0.0f) {
                if (this.collapseHeight < 0) {
                    this.collapseHeight = ListPanelDialog.this.bottomSheetBehavior.getPeekHeight();
                }
                f2 = ((float) this.collapseHeight) * (-f);
            }
            if (Float.compare(this.translationY, f2) != 0) {
                this.translationY = f2;
                ListPanelDialog.this.divider.setTranslationY(f2);
                ListPanelDialog.this.panelBottom.setTranslationY(f2);
            }
        }
    }

    private int getTitle(int i) {
        switch (i) {
            case 2:
                return C0769R.string.label_menu_history;
            case 3:
                return C0769R.string.label_menu_my_shots;
            case 4:
                return C0769R.string.label_menu_bookmark;
            default:
                return C0769R.string.label_menu_download;
        }
    }

    public static ListPanelDialog newInstance(int i) {
        ListPanelDialog listPanelDialog = new ListPanelDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("TYPE", i);
        listPanelDialog.setArguments(bundle);
        return listPanelDialog;
    }

    public void onResume() {
        super.onResume();
        showItem(getArguments().getInt("TYPE"));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(1, C0769R.style.BottomSheetTheme);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C0769R.layout.fragment_listpanel_dialog, viewGroup, false);
        this.title = (TextView) inflate.findViewById(2131296697);
        View findViewById = inflate.findViewById(C0427R.C0426id.bottom_sheet);
        this.scrollView = (NestedScrollView) inflate.findViewById(C0427R.C0426id.main_content);
        this.scrollView.setOnScrollChangeListener(new C06951());
        this.bottomSheetBehavior = BottomSheetBehavior.from(findViewById);
        this.bottomSheetBehavior.setState(4);
        this.bottomSheetBehavior.setBottomSheetCallback(new C06962());
        inflate.findViewById(2131296374).setOnClickListener(new C04733());
        this.divider = inflate.findViewById(C0427R.C0426id.divider);
        this.panelBottom = inflate.findViewById(C0427R.C0426id.panel_bottom);
        this.bookmarksTouchArea = inflate.findViewById(C0427R.C0426id.bookmarks);
        this.bookmarksTouchArea.setOnClickListener(new C04744());
        this.downloadsTouchArea = inflate.findViewById(C0427R.C0426id.downloads);
        this.downloadsTouchArea.setOnClickListener(new C04755());
        this.historyTouchArea = inflate.findViewById(C0427R.C0426id.history);
        this.historyTouchArea.setOnClickListener(new C04766());
        this.screenshotsTouchArea = inflate.findViewById(C0427R.C0426id.screenshots);
        this.screenshotsTouchArea.setOnClickListener(new C04777());
        return inflate;
    }

    private void setSelectedItem(int i) {
        getArguments().putInt("TYPE", i);
        toggleSelectedItem();
    }

    private void showItem(int i) {
        if (this.firstLaunch || getArguments().getInt("TYPE") != i) {
            this.title.setText(getTitle(i));
            setSelectedItem(i);
            showPanelFragment(createFragmentByType(i));
        }
    }

    private PanelFragment createFragmentByType(int i) {
        switch (i) {
            case 2:
                return BrowsingHistoryFragment.newInstance();
            case 3:
                return ScreenshotGridFragment.newInstance();
            case 4:
                return BookmarksFragment.newInstance();
            default:
                return DownloadsFragment.newInstance();
        }
    }

    private void showPanelFragment(PanelFragment panelFragment) {
        getChildFragmentManager().beginTransaction().replace(C0427R.C0426id.main_content, panelFragment).commit();
    }

    private void toggleSelectedItem() {
        this.firstLaunch = false;
        this.bookmarksTouchArea.setSelected(false);
        this.downloadsTouchArea.setSelected(false);
        this.historyTouchArea.setSelected(false);
        this.screenshotsTouchArea.setSelected(false);
        switch (getArguments().getInt("TYPE")) {
            case 1:
                this.downloadsTouchArea.setSelected(true);
                return;
            case 2:
                this.historyTouchArea.setSelected(true);
                return;
            case 3:
                this.screenshotsTouchArea.setSelected(true);
                return;
            case 4:
                this.bookmarksTouchArea.setSelected(true);
                return;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("There is no view type ");
                stringBuilder.append(getArguments().getInt("TYPE"));
                throw new RuntimeException(stringBuilder.toString());
        }
    }
}
