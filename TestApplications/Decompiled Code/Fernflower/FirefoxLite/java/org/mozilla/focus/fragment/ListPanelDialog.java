package org.mozilla.focus.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import org.mozilla.focus.history.BrowsingHistoryFragment;
import org.mozilla.focus.screenshot.ScreenshotGridFragment;
import org.mozilla.focus.telemetry.TelemetryWrapper;

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

   private PanelFragment createFragmentByType(int var1) {
      switch(var1) {
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

   private int getTitle(int var1) {
      switch(var1) {
      case 2:
         return 2131755237;
      case 3:
         return 2131755239;
      case 4:
         return 2131755230;
      default:
         return 2131755233;
      }
   }

   public static ListPanelDialog newInstance(int var0) {
      ListPanelDialog var1 = new ListPanelDialog();
      Bundle var2 = new Bundle();
      var2.putInt("TYPE", var0);
      var1.setArguments(var2);
      return var1;
   }

   private void setSelectedItem(int var1) {
      this.getArguments().putInt("TYPE", var1);
      this.toggleSelectedItem();
   }

   private void showItem(int var1) {
      if (this.firstLaunch || this.getArguments().getInt("TYPE") != var1) {
         this.title.setText(this.getTitle(var1));
         this.setSelectedItem(var1);
         this.showPanelFragment(this.createFragmentByType(var1));
      }

   }

   private void showPanelFragment(PanelFragment var1) {
      this.getChildFragmentManager().beginTransaction().replace(2131296499, var1).commit();
   }

   private void toggleSelectedItem() {
      this.firstLaunch = false;
      this.bookmarksTouchArea.setSelected(false);
      this.downloadsTouchArea.setSelected(false);
      this.historyTouchArea.setSelected(false);
      this.screenshotsTouchArea.setSelected(false);
      switch(this.getArguments().getInt("TYPE")) {
      case 1:
         this.downloadsTouchArea.setSelected(true);
         break;
      case 2:
         this.historyTouchArea.setSelected(true);
         break;
      case 3:
         this.screenshotsTouchArea.setSelected(true);
         break;
      case 4:
         this.bookmarksTouchArea.setSelected(true);
         break;
      default:
         StringBuilder var1 = new StringBuilder();
         var1.append("There is no view type ");
         var1.append(this.getArguments().getInt("TYPE"));
         throw new RuntimeException(var1.toString());
      }

   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setStyle(1, 2131820747);
   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      View var5 = var1.inflate(2131492968, var2, false);
      this.title = (TextView)var5.findViewById(2131296697);
      View var4 = var5.findViewById(2131296328);
      this.scrollView = (NestedScrollView)var5.findViewById(2131296499);
      this.scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
         public void onScrollChange(NestedScrollView var1, int var2, int var3, int var4, int var5) {
            var2 = var1.getMeasuredHeight();
            if (var3 > var5 && var1.getChildAt(0).getMeasuredHeight() - var1.getMeasuredHeight() - var3 < var2) {
               final PanelFragment var6 = (PanelFragment)ListPanelDialog.this.getChildFragmentManager().findFragmentById(2131296499);
               if (var6 != null && var6.isVisible()) {
                  (new Thread(new Runnable() {
                     public void run() {
                        var6.tryLoadMore();
                     }
                  })).start();
               }
            }

         }
      });
      this.bottomSheetBehavior = BottomSheetBehavior.from(var4);
      this.bottomSheetBehavior.setState(4);
      this.bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
         private int collapseHeight = -1;
         private float translationY = -2.14748365E9F;

         public void onSlide(View var1, float var2) {
            float var3 = 0.0F;
            if (var2 < 0.0F) {
               if (this.collapseHeight < 0) {
                  this.collapseHeight = ListPanelDialog.this.bottomSheetBehavior.getPeekHeight();
               }

               var3 = (float)this.collapseHeight * -var2;
            }

            if (Float.compare(this.translationY, var3) != 0) {
               this.translationY = var3;
               ListPanelDialog.this.divider.setTranslationY(var3);
               ListPanelDialog.this.panelBottom.setTranslationY(var3);
            }

         }

         public void onStateChanged(View var1, int var2) {
            if (var2 == 5) {
               ListPanelDialog.this.dismissAllowingStateLoss();
            }

         }
      });
      var5.findViewById(2131296374).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            ListPanelDialog.this.dismissAllowingStateLoss();
         }
      });
      this.divider = var5.findViewById(2131296412);
      this.panelBottom = var5.findViewById(2131296559);
      this.bookmarksTouchArea = var5.findViewById(2131296324);
      this.bookmarksTouchArea.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            ListPanelDialog.this.showItem(4);
            TelemetryWrapper.showPanelBookmark();
         }
      });
      this.downloadsTouchArea = var5.findViewById(2131296419);
      this.downloadsTouchArea.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            ListPanelDialog.this.showItem(1);
            TelemetryWrapper.showPanelDownload();
         }
      });
      this.historyTouchArea = var5.findViewById(2131296456);
      this.historyTouchArea.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            ListPanelDialog.this.showItem(2);
            TelemetryWrapper.showPanelHistory();
         }
      });
      this.screenshotsTouchArea = var5.findViewById(2131296618);
      this.screenshotsTouchArea.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            ListPanelDialog.this.showItem(3);
            TelemetryWrapper.showPanelCapture();
         }
      });
      return var5;
   }

   public void onResume() {
      super.onResume();
      this.showItem(this.getArguments().getInt("TYPE"));
   }
}
