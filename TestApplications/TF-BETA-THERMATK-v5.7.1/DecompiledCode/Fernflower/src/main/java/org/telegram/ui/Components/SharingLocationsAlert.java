package org.telegram.ui.Components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.regex.Pattern;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ContentPreviewViewer;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.SharingLiveLocationCell;

public class SharingLocationsAlert extends BottomSheet implements NotificationCenter.NotificationCenterDelegate {
   private SharingLocationsAlert.ListAdapter adapter;
   private SharingLocationsAlert.SharingLocationsAlertDelegate delegate;
   private boolean ignoreLayout;
   private RecyclerListView listView;
   private int reqId;
   private int scrollOffsetY;
   private Drawable shadowDrawable;
   private TextView textView;
   private Pattern urlPattern;

   public SharingLocationsAlert(Context var1, SharingLocationsAlert.SharingLocationsAlertDelegate var2) {
      super(var1, false, 0);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.liveLocationsChanged);
      this.delegate = var2;
      this.shadowDrawable = var1.getResources().getDrawable(2131165823).mutate();
      this.shadowDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogBackground"), Mode.MULTIPLY));
      super.containerView = new FrameLayout(var1) {
         protected void onDraw(Canvas var1) {
            SharingLocationsAlert.this.shadowDrawable.setBounds(0, SharingLocationsAlert.this.scrollOffsetY - SharingLocationsAlert.access$400(SharingLocationsAlert.this), this.getMeasuredWidth(), this.getMeasuredHeight());
            SharingLocationsAlert.this.shadowDrawable.draw(var1);
         }

         public boolean onInterceptTouchEvent(MotionEvent var1) {
            if (var1.getAction() == 0 && SharingLocationsAlert.this.scrollOffsetY != 0 && var1.getY() < (float)SharingLocationsAlert.this.scrollOffsetY) {
               SharingLocationsAlert.this.dismiss();
               return true;
            } else {
               return super.onInterceptTouchEvent(var1);
            }
         }

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            super.onLayout(var1, var2, var3, var4, var5);
            SharingLocationsAlert.this.updateLayout();
         }

         protected void onMeasure(int var1, int var2) {
            var2 = MeasureSpec.getSize(var2);
            int var3 = var2;
            if (VERSION.SDK_INT >= 21) {
               var3 = var2 - AndroidUtilities.statusBarHeight;
            }

            this.getMeasuredWidth();
            int var4 = AndroidUtilities.dp(56.0F) + AndroidUtilities.dp(56.0F) + 1 + LocationController.getLocationsCount() * AndroidUtilities.dp(54.0F);
            var2 = var3 / 5;
            if (var4 < var2 * 3) {
               var2 = AndroidUtilities.dp(8.0F);
            } else {
               int var5 = var2 * 2;
               var2 = var5;
               if (var4 < var3) {
                  var2 = var5 - (var3 - var4);
               }
            }

            if (SharingLocationsAlert.this.listView.getPaddingTop() != var2) {
               SharingLocationsAlert.this.ignoreLayout = true;
               SharingLocationsAlert.this.listView.setPadding(0, var2, 0, AndroidUtilities.dp(8.0F));
               SharingLocationsAlert.this.ignoreLayout = false;
            }

            super.onMeasure(var1, MeasureSpec.makeMeasureSpec(Math.min(var4, var3), 1073741824));
         }

         public boolean onTouchEvent(MotionEvent var1) {
            boolean var2;
            if (!SharingLocationsAlert.this.isDismissed() && super.onTouchEvent(var1)) {
               var2 = true;
            } else {
               var2 = false;
            }

            return var2;
         }

         public void requestLayout() {
            if (!SharingLocationsAlert.this.ignoreLayout) {
               super.requestLayout();
            }
         }
      };
      super.containerView.setWillNotDraw(false);
      ViewGroup var6 = super.containerView;
      int var3 = super.backgroundPaddingLeft;
      var6.setPadding(var3, 0, var3, 0);
      this.listView = new RecyclerListView(var1) {
         public boolean onInterceptTouchEvent(MotionEvent var1) {
            ContentPreviewViewer var2 = ContentPreviewViewer.getInstance();
            RecyclerListView var3 = SharingLocationsAlert.this.listView;
            boolean var4 = false;
            boolean var5 = var2.onInterceptTouchEvent(var1, var3, 0, (ContentPreviewViewer.ContentPreviewViewerDelegate)null);
            if (super.onInterceptTouchEvent(var1) || var5) {
               var4 = true;
            }

            return var4;
         }

         public void requestLayout() {
            if (!SharingLocationsAlert.this.ignoreLayout) {
               super.requestLayout();
            }
         }
      };
      this.listView.setLayoutManager(new LinearLayoutManager(this.getContext(), 1, false));
      RecyclerListView var7 = this.listView;
      SharingLocationsAlert.ListAdapter var4 = new SharingLocationsAlert.ListAdapter(var1);
      this.adapter = var4;
      var7.setAdapter(var4);
      this.listView.setVerticalScrollBarEnabled(false);
      this.listView.setClipToPadding(false);
      this.listView.setEnabled(true);
      this.listView.setGlowColor(Theme.getColor("dialogScrollGlow"));
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrolled(RecyclerView var1, int var2, int var3) {
            SharingLocationsAlert.this.updateLayout();
         }
      });
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$SharingLocationsAlert$DpSNSHcc4un3mf9rnSsA_SYRUYE(this)));
      super.containerView.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 0.0F, 0.0F, 48.0F));
      View var8 = new View(var1);
      var8.setBackgroundResource(2131165408);
      super.containerView.addView(var8, LayoutHelper.createFrame(-1, 3.0F, 83, 0.0F, 0.0F, 0.0F, 48.0F));
      PickerBottomLayout var5 = new PickerBottomLayout(var1, false);
      var5.setBackgroundColor(Theme.getColor("dialogBackground"));
      super.containerView.addView(var5, LayoutHelper.createFrame(-1, 48, 83));
      var5.cancelButton.setPadding(AndroidUtilities.dp(18.0F), 0, AndroidUtilities.dp(18.0F), 0);
      var5.cancelButton.setTextColor(Theme.getColor("dialogTextRed"));
      var5.cancelButton.setText(LocaleController.getString("StopAllLocationSharings", 2131560821));
      var5.cancelButton.setOnClickListener(new _$$Lambda$SharingLocationsAlert$_EuBrHBpuV07T7IQJTP904l7r08(this));
      var5.doneButtonTextView.setTextColor(Theme.getColor("dialogTextBlue2"));
      var5.doneButtonTextView.setText(LocaleController.getString("Close", 2131559117).toUpperCase());
      var5.doneButton.setPadding(AndroidUtilities.dp(18.0F), 0, AndroidUtilities.dp(18.0F), 0);
      var5.doneButton.setOnClickListener(new _$$Lambda$SharingLocationsAlert$0An2GQnWbLXoCb_gmzP5F2Gw9Lc(this));
      var5.doneButtonBadgeTextView.setVisibility(8);
      this.adapter.notifyDataSetChanged();
   }

   // $FF: synthetic method
   static int access$400(SharingLocationsAlert var0) {
      return var0.backgroundPaddingTop;
   }

   private LocationController.SharingLocationInfo getLocation(int var1) {
      byte var2 = 0;
      int var3 = var1;

      for(var1 = var2; var1 < 3; ++var1) {
         ArrayList var4 = LocationController.getInstance(var1).sharingLocationsUI;
         if (var3 < var4.size()) {
            return (LocationController.SharingLocationInfo)var4.get(var3);
         }

         var3 -= var4.size();
      }

      return null;
   }

   @SuppressLint({"NewApi"})
   private void updateLayout() {
      int var2;
      RecyclerListView var4;
      if (this.listView.getChildCount() <= 0) {
         var4 = this.listView;
         var2 = var4.getPaddingTop();
         this.scrollOffsetY = var2;
         var4.setTopGlowOffset(var2);
         super.containerView.invalidate();
      } else {
         View var1 = this.listView.getChildAt(0);
         RecyclerListView.Holder var3 = (RecyclerListView.Holder)this.listView.findContainingViewHolder(var1);
         var2 = var1.getTop() - AndroidUtilities.dp(8.0F);
         if (var2 <= 0 || var3 == null || var3.getAdapterPosition() != 0) {
            var2 = 0;
         }

         if (this.scrollOffsetY != var2) {
            var4 = this.listView;
            this.scrollOffsetY = var2;
            var4.setTopGlowOffset(var2);
            super.containerView.invalidate();
         }

      }
   }

   protected boolean canDismissWithSwipe() {
      return false;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.liveLocationsChanged) {
         if (LocationController.getLocationsCount() == 0) {
            this.dismiss();
         } else {
            this.adapter.notifyDataSetChanged();
         }
      }

   }

   public void dismiss() {
      super.dismiss();
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.liveLocationsChanged);
   }

   // $FF: synthetic method
   public void lambda$new$0$SharingLocationsAlert(View var1, int var2) {
      --var2;
      if (var2 >= 0 && var2 < LocationController.getLocationsCount()) {
         this.delegate.didSelectLocation(this.getLocation(var2));
         this.dismiss();
      }

   }

   // $FF: synthetic method
   public void lambda$new$1$SharingLocationsAlert(View var1) {
      for(int var2 = 0; var2 < 3; ++var2) {
         LocationController.getInstance(var2).removeAllLocationSharings();
      }

      this.dismiss();
   }

   // $FF: synthetic method
   public void lambda$new$2$SharingLocationsAlert(View var1) {
      this.dismiss();
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context context;

      public ListAdapter(Context var2) {
         this.context = var2;
      }

      public int getItemCount() {
         return LocationController.getLocationsCount() + 1;
      }

      public int getItemViewType(int var1) {
         return var1 == 0 ? 1 : 0;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         boolean var2;
         if (var1.getItemViewType() == 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         if (var3 != 0) {
            if (var3 == 1 && SharingLocationsAlert.this.textView != null) {
               SharingLocationsAlert.this.textView.setText(LocaleController.formatString("SharingLiveLocationTitle", 2131560772, LocaleController.formatPluralString("Chats", LocationController.getLocationsCount())));
            }
         } else {
            ((SharingLiveLocationCell)var1.itemView).setDialog(SharingLocationsAlert.this.getLocation(var2 - 1));
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            var3 = new FrameLayout(this.context) {
               protected void onDraw(Canvas var1) {
                  var1.drawLine(0.0F, (float)AndroidUtilities.dp(40.0F), (float)this.getMeasuredWidth(), (float)AndroidUtilities.dp(40.0F), Theme.dividerPaint);
               }

               protected void onMeasure(int var1, int var2) {
                  super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0F) + 1, 1073741824));
               }
            };
            ((FrameLayout)var3).setWillNotDraw(false);
            SharingLocationsAlert.this.textView = new TextView(this.context);
            SharingLocationsAlert.this.textView.setTextColor(Theme.getColor("dialogIcon"));
            SharingLocationsAlert.this.textView.setTextSize(1, 14.0F);
            SharingLocationsAlert.this.textView.setGravity(17);
            SharingLocationsAlert.this.textView.setPadding(0, 0, 0, AndroidUtilities.dp(8.0F));
            ((FrameLayout)var3).addView(SharingLocationsAlert.this.textView, LayoutHelper.createFrame(-1, 40.0F));
         } else {
            var3 = new SharingLiveLocationCell(this.context, false);
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }

   public interface SharingLocationsAlertDelegate {
      void didSelectLocation(LocationController.SharingLocationInfo var1);
   }
}
