// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.fragment;

import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.view.View$OnClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import org.mozilla.focus.history.BrowsingHistoryFragment;
import org.mozilla.focus.screenshot.ScreenshotGridFragment;
import android.widget.TextView;
import android.support.v4.widget.NestedScrollView;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;
import android.support.v4.app.DialogFragment;

public class ListPanelDialog extends DialogFragment
{
    private View bookmarksTouchArea;
    private BottomSheetBehavior bottomSheetBehavior;
    private View divider;
    private View downloadsTouchArea;
    private boolean firstLaunch;
    private View historyTouchArea;
    private View panelBottom;
    private View screenshotsTouchArea;
    private NestedScrollView scrollView;
    private TextView title;
    
    public ListPanelDialog() {
        this.firstLaunch = true;
    }
    
    private PanelFragment createFragmentByType(final int n) {
        switch (n) {
            default: {
                return DownloadsFragment.newInstance();
            }
            case 4: {
                return BookmarksFragment.newInstance();
            }
            case 3: {
                return ScreenshotGridFragment.newInstance();
            }
            case 2: {
                return BrowsingHistoryFragment.newInstance();
            }
        }
    }
    
    private int getTitle(final int n) {
        switch (n) {
            default: {
                return 2131755233;
            }
            case 4: {
                return 2131755230;
            }
            case 3: {
                return 2131755239;
            }
            case 2: {
                return 2131755237;
            }
        }
    }
    
    public static ListPanelDialog newInstance(final int n) {
        final ListPanelDialog listPanelDialog = new ListPanelDialog();
        final Bundle arguments = new Bundle();
        arguments.putInt("TYPE", n);
        listPanelDialog.setArguments(arguments);
        return listPanelDialog;
    }
    
    private void setSelectedItem(final int n) {
        this.getArguments().putInt("TYPE", n);
        this.toggleSelectedItem();
    }
    
    private void showItem(final int selectedItem) {
        if (this.firstLaunch || this.getArguments().getInt("TYPE") != selectedItem) {
            this.title.setText(this.getTitle(selectedItem));
            this.setSelectedItem(selectedItem);
            this.showPanelFragment(this.createFragmentByType(selectedItem));
        }
    }
    
    private void showPanelFragment(final PanelFragment panelFragment) {
        this.getChildFragmentManager().beginTransaction().replace(2131296499, panelFragment).commit();
    }
    
    private void toggleSelectedItem() {
        this.firstLaunch = false;
        this.bookmarksTouchArea.setSelected(false);
        this.downloadsTouchArea.setSelected(false);
        this.historyTouchArea.setSelected(false);
        this.screenshotsTouchArea.setSelected(false);
        switch (this.getArguments().getInt("TYPE")) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("There is no view type ");
                sb.append(this.getArguments().getInt("TYPE"));
                throw new RuntimeException(sb.toString());
            }
            case 4: {
                this.bookmarksTouchArea.setSelected(true);
                break;
            }
            case 3: {
                this.screenshotsTouchArea.setSelected(true);
                break;
            }
            case 2: {
                this.historyTouchArea.setSelected(true);
                break;
            }
            case 1: {
                this.downloadsTouchArea.setSelected(true);
                break;
            }
        }
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setStyle(1, 2131820747);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2131492968, viewGroup, false);
        this.title = (TextView)inflate.findViewById(2131296697);
        final View viewById = inflate.findViewById(2131296328);
        (this.scrollView = (NestedScrollView)inflate.findViewById(2131296499)).setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(final NestedScrollView nestedScrollView, int measuredHeight, final int n, final int n2, final int n3) {
                measuredHeight = nestedScrollView.getMeasuredHeight();
                if (n > n3 && nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight() - n < measuredHeight) {
                    final PanelFragment panelFragment = (PanelFragment)ListPanelDialog.this.getChildFragmentManager().findFragmentById(2131296499);
                    if (panelFragment != null && panelFragment.isVisible()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                panelFragment.tryLoadMore();
                            }
                        }).start();
                    }
                }
            }
        });
        (this.bottomSheetBehavior = BottomSheetBehavior.from(viewById)).setState(4);
        this.bottomSheetBehavior.setBottomSheetCallback((BottomSheetBehavior.BottomSheetCallback)new BottomSheetBehavior.BottomSheetCallback() {
            private int collapseHeight = -1;
            private float translationY = -2.14748365E9f;
            
            @Override
            public void onSlide(final View view, final float n) {
                float n2 = 0.0f;
                if (n < 0.0f) {
                    if (this.collapseHeight < 0) {
                        this.collapseHeight = ListPanelDialog.this.bottomSheetBehavior.getPeekHeight();
                    }
                    n2 = this.collapseHeight * -n;
                }
                if (Float.compare(this.translationY, n2) != 0) {
                    this.translationY = n2;
                    ListPanelDialog.this.divider.setTranslationY(n2);
                    ListPanelDialog.this.panelBottom.setTranslationY(n2);
                }
            }
            
            @Override
            public void onStateChanged(final View view, final int n) {
                if (n == 5) {
                    ListPanelDialog.this.dismissAllowingStateLoss();
                }
            }
        });
        inflate.findViewById(2131296374).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                ListPanelDialog.this.dismissAllowingStateLoss();
            }
        });
        this.divider = inflate.findViewById(2131296412);
        this.panelBottom = inflate.findViewById(2131296559);
        (this.bookmarksTouchArea = inflate.findViewById(2131296324)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                ListPanelDialog.this.showItem(4);
                TelemetryWrapper.showPanelBookmark();
            }
        });
        (this.downloadsTouchArea = inflate.findViewById(2131296419)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                ListPanelDialog.this.showItem(1);
                TelemetryWrapper.showPanelDownload();
            }
        });
        (this.historyTouchArea = inflate.findViewById(2131296456)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                ListPanelDialog.this.showItem(2);
                TelemetryWrapper.showPanelHistory();
            }
        });
        (this.screenshotsTouchArea = inflate.findViewById(2131296618)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                ListPanelDialog.this.showItem(3);
                TelemetryWrapper.showPanelCapture();
            }
        });
        return inflate;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        this.showItem(this.getArguments().getInt("TYPE"));
    }
}
