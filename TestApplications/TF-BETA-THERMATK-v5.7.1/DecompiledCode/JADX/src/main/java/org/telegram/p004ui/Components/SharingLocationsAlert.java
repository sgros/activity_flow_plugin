package org.telegram.p004ui.Components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import java.util.regex.Pattern;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.LocationController.SharingLocationInfo;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.p004ui.ActionBar.BottomSheet;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Cells.SharingLiveLocationCell;
import org.telegram.p004ui.Components.RecyclerListView.Holder;
import org.telegram.p004ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.p004ui.ContentPreviewViewer;

/* renamed from: org.telegram.ui.Components.SharingLocationsAlert */
public class SharingLocationsAlert extends BottomSheet implements NotificationCenterDelegate {
    private ListAdapter adapter;
    private SharingLocationsAlertDelegate delegate;
    private boolean ignoreLayout;
    private RecyclerListView listView;
    private int reqId;
    private int scrollOffsetY;
    private Drawable shadowDrawable;
    private TextView textView;
    private Pattern urlPattern;

    /* renamed from: org.telegram.ui.Components.SharingLocationsAlert$SharingLocationsAlertDelegate */
    public interface SharingLocationsAlertDelegate {
        void didSelectLocation(SharingLocationInfo sharingLocationInfo);
    }

    /* renamed from: org.telegram.ui.Components.SharingLocationsAlert$3 */
    class C41403 extends OnScrollListener {
        C41403() {
        }

        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            SharingLocationsAlert.this.updateLayout();
        }
    }

    /* renamed from: org.telegram.ui.Components.SharingLocationsAlert$ListAdapter */
    private class ListAdapter extends SelectionAdapter {
        private Context context;

        public int getItemViewType(int i) {
            return i == 0 ? 1 : 0;
        }

        public ListAdapter(Context context) {
            this.context = context;
        }

        public int getItemCount() {
            return LocationController.getLocationsCount() + 1;
        }

        public boolean isEnabled(ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View c29421;
            if (i != 0) {
                c29421 = new FrameLayout(this.context) {
                    /* Access modifiers changed, original: protected */
                    public void onMeasure(int i, int i2) {
                        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(48.0f) + 1, 1073741824));
                    }

                    /* Access modifiers changed, original: protected */
                    public void onDraw(Canvas canvas) {
                        canvas.drawLine(0.0f, (float) AndroidUtilities.m26dp(40.0f), (float) getMeasuredWidth(), (float) AndroidUtilities.m26dp(40.0f), Theme.dividerPaint);
                    }
                };
                c29421.setWillNotDraw(false);
                SharingLocationsAlert.this.textView = new TextView(this.context);
                SharingLocationsAlert.this.textView.setTextColor(Theme.getColor(Theme.key_dialogIcon));
                SharingLocationsAlert.this.textView.setTextSize(1, 14.0f);
                SharingLocationsAlert.this.textView.setGravity(17);
                SharingLocationsAlert.this.textView.setPadding(0, 0, 0, AndroidUtilities.m26dp(8.0f));
                c29421.addView(SharingLocationsAlert.this.textView, LayoutHelper.createFrame(-1, 40.0f));
            } else {
                c29421 = new SharingLiveLocationCell(this.context, false);
            }
            return new Holder(c29421);
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                ((SharingLiveLocationCell) viewHolder.itemView).setDialog(SharingLocationsAlert.this.getLocation(i - 1));
            } else if (itemViewType == 1 && SharingLocationsAlert.this.textView != null) {
                TextView access$600 = SharingLocationsAlert.this.textView;
                Object[] objArr = new Object[1];
                objArr[0] = LocaleController.formatPluralString("Chats", LocationController.getLocationsCount());
                access$600.setText(LocaleController.formatString("SharingLiveLocationTitle", C1067R.string.SharingLiveLocationTitle, objArr));
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean canDismissWithSwipe() {
        return false;
    }

    public SharingLocationsAlert(Context context, SharingLocationsAlertDelegate sharingLocationsAlertDelegate) {
        super(context, false, 0);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.liveLocationsChanged);
        this.delegate = sharingLocationsAlertDelegate;
        this.shadowDrawable = context.getResources().getDrawable(C1067R.C1065drawable.sheet_shadow).mutate();
        Drawable drawable = this.shadowDrawable;
        String str = Theme.key_dialogBackground;
        drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(str), Mode.MULTIPLY));
        this.containerView = new FrameLayout(context) {
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() != 0 || SharingLocationsAlert.this.scrollOffsetY == 0 || motionEvent.getY() >= ((float) SharingLocationsAlert.this.scrollOffsetY)) {
                    return super.onInterceptTouchEvent(motionEvent);
                }
                SharingLocationsAlert.this.dismiss();
                return true;
            }

            public boolean onTouchEvent(MotionEvent motionEvent) {
                return !SharingLocationsAlert.this.isDismissed() && super.onTouchEvent(motionEvent);
            }

            /* Access modifiers changed, original: protected */
            public void onMeasure(int i, int i2) {
                i2 = MeasureSpec.getSize(i2);
                if (VERSION.SDK_INT >= 21) {
                    i2 -= AndroidUtilities.statusBarHeight;
                }
                getMeasuredWidth();
                int dp = ((AndroidUtilities.m26dp(56.0f) + AndroidUtilities.m26dp(56.0f)) + 1) + (LocationController.getLocationsCount() * AndroidUtilities.m26dp(54.0f));
                int i3 = i2 / 5;
                if (dp < i3 * 3) {
                    i3 = AndroidUtilities.m26dp(8.0f);
                } else {
                    i3 *= 2;
                    if (dp < i2) {
                        i3 -= i2 - dp;
                    }
                }
                if (SharingLocationsAlert.this.listView.getPaddingTop() != i3) {
                    SharingLocationsAlert.this.ignoreLayout = true;
                    SharingLocationsAlert.this.listView.setPadding(0, i3, 0, AndroidUtilities.m26dp(8.0f));
                    SharingLocationsAlert.this.ignoreLayout = false;
                }
                super.onMeasure(i, MeasureSpec.makeMeasureSpec(Math.min(dp, i2), 1073741824));
            }

            /* Access modifiers changed, original: protected */
            public void onLayout(boolean z, int i, int i2, int i3, int i4) {
                super.onLayout(z, i, i2, i3, i4);
                SharingLocationsAlert.this.updateLayout();
            }

            public void requestLayout() {
                if (!SharingLocationsAlert.this.ignoreLayout) {
                    super.requestLayout();
                }
            }

            /* Access modifiers changed, original: protected */
            public void onDraw(Canvas canvas) {
                SharingLocationsAlert.this.shadowDrawable.setBounds(0, SharingLocationsAlert.this.scrollOffsetY - SharingLocationsAlert.this.backgroundPaddingTop, getMeasuredWidth(), getMeasuredHeight());
                SharingLocationsAlert.this.shadowDrawable.draw(canvas);
            }
        };
        this.containerView.setWillNotDraw(false);
        ViewGroup viewGroup = this.containerView;
        int i = this.backgroundPaddingLeft;
        viewGroup.setPadding(i, 0, i, 0);
        this.listView = new RecyclerListView(context) {
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                boolean onInterceptTouchEvent = ContentPreviewViewer.getInstance().onInterceptTouchEvent(motionEvent, SharingLocationsAlert.this.listView, 0, null);
                if (super.onInterceptTouchEvent(motionEvent) || onInterceptTouchEvent) {
                    return true;
                }
                return false;
            }

            public void requestLayout() {
                if (!SharingLocationsAlert.this.ignoreLayout) {
                    super.requestLayout();
                }
            }
        };
        this.listView.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
        RecyclerListView recyclerListView = this.listView;
        ListAdapter listAdapter = new ListAdapter(context);
        this.adapter = listAdapter;
        recyclerListView.setAdapter(listAdapter);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setClipToPadding(false);
        this.listView.setEnabled(true);
        this.listView.setGlowColor(Theme.getColor(Theme.key_dialogScrollGlow));
        this.listView.setOnScrollListener(new C41403());
        this.listView.setOnItemClickListener(new C4056-$$Lambda$SharingLocationsAlert$DpSNSHcc4un3mf9rnSsA-SYRUYE(this));
        this.containerView.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 48.0f));
        View view = new View(context);
        view.setBackgroundResource(C1067R.C1065drawable.header_shadow_reverse);
        this.containerView.addView(view, LayoutHelper.createFrame(-1, 3.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
        PickerBottomLayout pickerBottomLayout = new PickerBottomLayout(context, false);
        pickerBottomLayout.setBackgroundColor(Theme.getColor(str));
        this.containerView.addView(pickerBottomLayout, LayoutHelper.createFrame(-1, 48, 83));
        pickerBottomLayout.cancelButton.setPadding(AndroidUtilities.m26dp(18.0f), 0, AndroidUtilities.m26dp(18.0f), 0);
        pickerBottomLayout.cancelButton.setTextColor(Theme.getColor(Theme.key_dialogTextRed));
        pickerBottomLayout.cancelButton.setText(LocaleController.getString("StopAllLocationSharings", C1067R.string.StopAllLocationSharings));
        pickerBottomLayout.cancelButton.setOnClickListener(new C2672-$$Lambda$SharingLocationsAlert$_EuBrHBpuV07T7IQJTP904l7r08(this));
        pickerBottomLayout.doneButtonTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlue2));
        pickerBottomLayout.doneButtonTextView.setText(LocaleController.getString("Close", C1067R.string.Close).toUpperCase());
        pickerBottomLayout.doneButton.setPadding(AndroidUtilities.m26dp(18.0f), 0, AndroidUtilities.m26dp(18.0f), 0);
        pickerBottomLayout.doneButton.setOnClickListener(new C2671-$$Lambda$SharingLocationsAlert$0An2GQnWbLXoCb_gmzP5F2Gw9Lc(this));
        pickerBottomLayout.doneButtonBadgeTextView.setVisibility(8);
        this.adapter.notifyDataSetChanged();
    }

    public /* synthetic */ void lambda$new$0$SharingLocationsAlert(View view, int i) {
        i--;
        if (i >= 0 && i < LocationController.getLocationsCount()) {
            this.delegate.didSelectLocation(getLocation(i));
            dismiss();
        }
    }

    public /* synthetic */ void lambda$new$1$SharingLocationsAlert(View view) {
        for (int i = 0; i < 3; i++) {
            LocationController.getInstance(i).removeAllLocationSharings();
        }
        dismiss();
    }

    public /* synthetic */ void lambda$new$2$SharingLocationsAlert(View view) {
        dismiss();
    }

    @SuppressLint({"NewApi"})
    private void updateLayout() {
        if (this.listView.getChildCount() <= 0) {
            RecyclerListView recyclerListView = this.listView;
            int paddingTop = recyclerListView.getPaddingTop();
            this.scrollOffsetY = paddingTop;
            recyclerListView.setTopGlowOffset(paddingTop);
            this.containerView.invalidate();
            return;
        }
        View childAt = this.listView.getChildAt(0);
        Holder holder = (Holder) this.listView.findContainingViewHolder(childAt);
        int top = childAt.getTop() - AndroidUtilities.m26dp(8.0f);
        if (top <= 0 || holder == null || holder.getAdapterPosition() != 0) {
            top = 0;
        }
        if (this.scrollOffsetY != top) {
            RecyclerListView recyclerListView2 = this.listView;
            this.scrollOffsetY = top;
            recyclerListView2.setTopGlowOffset(top);
            this.containerView.invalidate();
        }
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i != NotificationCenter.liveLocationsChanged) {
            return;
        }
        if (LocationController.getLocationsCount() == 0) {
            dismiss();
        } else {
            this.adapter.notifyDataSetChanged();
        }
    }

    private SharingLocationInfo getLocation(int i) {
        for (int i2 = 0; i2 < 3; i2++) {
            ArrayList arrayList = LocationController.getInstance(i2).sharingLocationsUI;
            if (i < arrayList.size()) {
                return (SharingLocationInfo) arrayList.get(i);
            }
            i -= arrayList.size();
        }
        return null;
    }

    public void dismiss() {
        super.dismiss();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.liveLocationsChanged);
    }
}
