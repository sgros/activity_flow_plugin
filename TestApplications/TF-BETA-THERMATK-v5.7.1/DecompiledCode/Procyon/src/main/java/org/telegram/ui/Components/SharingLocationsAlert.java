// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.ui.Cells.SharingLiveLocationCell;
import android.annotation.SuppressLint;
import java.util.ArrayList;
import android.view.ViewGroup;
import android.view.View$OnClickListener;
import org.telegram.messenger.LocaleController;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.ui.ContentPreviewViewer;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.AndroidUtilities;
import android.os.Build$VERSION;
import android.view.View$MeasureSpec;
import android.view.MotionEvent;
import android.graphics.Canvas;
import android.widget.FrameLayout;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import java.util.regex.Pattern;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BottomSheet;

public class SharingLocationsAlert extends BottomSheet implements NotificationCenterDelegate
{
    private ListAdapter adapter;
    private SharingLocationsAlertDelegate delegate;
    private boolean ignoreLayout;
    private RecyclerListView listView;
    private int reqId;
    private int scrollOffsetY;
    private Drawable shadowDrawable;
    private TextView textView;
    private Pattern urlPattern;
    
    public SharingLocationsAlert(final Context context, final SharingLocationsAlertDelegate delegate) {
        super(context, false, 0);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.liveLocationsChanged);
        this.delegate = delegate;
        (this.shadowDrawable = context.getResources().getDrawable(2131165823).mutate()).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("dialogBackground"), PorterDuff$Mode.MULTIPLY));
        (super.containerView = (ViewGroup)new FrameLayout(context) {
            protected void onDraw(final Canvas canvas) {
                SharingLocationsAlert.this.shadowDrawable.setBounds(0, SharingLocationsAlert.this.scrollOffsetY - SharingLocationsAlert.this.backgroundPaddingTop, this.getMeasuredWidth(), this.getMeasuredHeight());
                SharingLocationsAlert.this.shadowDrawable.draw(canvas);
            }
            
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0 && SharingLocationsAlert.this.scrollOffsetY != 0 && motionEvent.getY() < SharingLocationsAlert.this.scrollOffsetY) {
                    SharingLocationsAlert.this.dismiss();
                    return true;
                }
                return super.onInterceptTouchEvent(motionEvent);
            }
            
            protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                super.onLayout(b, n, n2, n3, n4);
                SharingLocationsAlert.this.updateLayout();
            }
            
            protected void onMeasure(final int n, int dp) {
                int size;
                dp = (size = View$MeasureSpec.getSize(dp));
                if (Build$VERSION.SDK_INT >= 21) {
                    size = dp - AndroidUtilities.statusBarHeight;
                }
                this.getMeasuredWidth();
                final int a = AndroidUtilities.dp(56.0f) + AndroidUtilities.dp(56.0f) + 1 + LocationController.getLocationsCount() * AndroidUtilities.dp(54.0f);
                dp = size / 5;
                if (a < dp * 3) {
                    dp = AndroidUtilities.dp(8.0f);
                }
                else {
                    final int n2 = dp *= 2;
                    if (a < size) {
                        dp = n2 - (size - a);
                    }
                }
                if (SharingLocationsAlert.this.listView.getPaddingTop() != dp) {
                    SharingLocationsAlert.this.ignoreLayout = true;
                    SharingLocationsAlert.this.listView.setPadding(0, dp, 0, AndroidUtilities.dp(8.0f));
                    SharingLocationsAlert.this.ignoreLayout = false;
                }
                super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(Math.min(a, size), 1073741824));
            }
            
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                return !SharingLocationsAlert.this.isDismissed() && super.onTouchEvent(motionEvent);
            }
            
            public void requestLayout() {
                if (SharingLocationsAlert.this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        }).setWillNotDraw(false);
        final ViewGroup containerView = super.containerView;
        final int backgroundPaddingLeft = super.backgroundPaddingLeft;
        containerView.setPadding(backgroundPaddingLeft, 0, backgroundPaddingLeft, 0);
        (this.listView = new RecyclerListView(context) {
            @Override
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                final ContentPreviewViewer instance = ContentPreviewViewer.getInstance();
                final RecyclerListView access$100 = SharingLocationsAlert.this.listView;
                boolean b = false;
                final boolean onInterceptTouchEvent = instance.onInterceptTouchEvent(motionEvent, access$100, 0, null);
                if (super.onInterceptTouchEvent(motionEvent) || onInterceptTouchEvent) {
                    b = true;
                }
                return b;
            }
            
            @Override
            public void requestLayout() {
                if (SharingLocationsAlert.this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        }).setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(this.getContext(), 1, false));
        this.listView.setAdapter(this.adapter = new ListAdapter(context));
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setClipToPadding(false);
        this.listView.setEnabled(true);
        this.listView.setGlowColor(Theme.getColor("dialogScrollGlow"));
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                SharingLocationsAlert.this.updateLayout();
            }
        });
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$SharingLocationsAlert$DpSNSHcc4un3mf9rnSsA_SYRUYE(this));
        super.containerView.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 48.0f));
        final View view = new View(context);
        view.setBackgroundResource(2131165408);
        super.containerView.addView(view, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 3.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
        final PickerBottomLayout pickerBottomLayout = new PickerBottomLayout(context, false);
        pickerBottomLayout.setBackgroundColor(Theme.getColor("dialogBackground"));
        super.containerView.addView((View)pickerBottomLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 83));
        pickerBottomLayout.cancelButton.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
        pickerBottomLayout.cancelButton.setTextColor(Theme.getColor("dialogTextRed"));
        pickerBottomLayout.cancelButton.setText((CharSequence)LocaleController.getString("StopAllLocationSharings", 2131560821));
        pickerBottomLayout.cancelButton.setOnClickListener((View$OnClickListener)new _$$Lambda$SharingLocationsAlert$_EuBrHBpuV07T7IQJTP904l7r08(this));
        pickerBottomLayout.doneButtonTextView.setTextColor(Theme.getColor("dialogTextBlue2"));
        pickerBottomLayout.doneButtonTextView.setText((CharSequence)LocaleController.getString("Close", 2131559117).toUpperCase());
        pickerBottomLayout.doneButton.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
        pickerBottomLayout.doneButton.setOnClickListener((View$OnClickListener)new _$$Lambda$SharingLocationsAlert$0An2GQnWbLXoCb_gmzP5F2Gw9Lc(this));
        pickerBottomLayout.doneButtonBadgeTextView.setVisibility(8);
        this.adapter.notifyDataSetChanged();
    }
    
    private LocationController.SharingLocationInfo getLocation(int i) {
        final int n = 0;
        int index = i;
        ArrayList<LocationController.SharingLocationInfo> sharingLocationsUI;
        for (i = n; i < 3; ++i) {
            sharingLocationsUI = LocationController.getInstance(i).sharingLocationsUI;
            if (index < sharingLocationsUI.size()) {
                return (LocationController.SharingLocationInfo)sharingLocationsUI.get(index);
            }
            index -= sharingLocationsUI.size();
        }
        return null;
    }
    
    @SuppressLint({ "NewApi" })
    private void updateLayout() {
        if (this.listView.getChildCount() <= 0) {
            final RecyclerListView listView = this.listView;
            listView.setTopGlowOffset(this.scrollOffsetY = listView.getPaddingTop());
            super.containerView.invalidate();
            return;
        }
        final View child = this.listView.getChildAt(0);
        final RecyclerListView.Holder holder = (RecyclerListView.Holder)this.listView.findContainingViewHolder(child);
        int scrollOffsetY = child.getTop() - AndroidUtilities.dp(8.0f);
        if (scrollOffsetY <= 0 || holder == null || ((RecyclerView.ViewHolder)holder).getAdapterPosition() != 0) {
            scrollOffsetY = 0;
        }
        if (this.scrollOffsetY != scrollOffsetY) {
            this.listView.setTopGlowOffset(this.scrollOffsetY = scrollOffsetY);
            super.containerView.invalidate();
        }
    }
    
    @Override
    protected boolean canDismissWithSwipe() {
        return false;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.liveLocationsChanged) {
            if (LocationController.getLocationsCount() == 0) {
                this.dismiss();
            }
            else {
                this.adapter.notifyDataSetChanged();
            }
        }
    }
    
    @Override
    public void dismiss() {
        super.dismiss();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.liveLocationsChanged);
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context context;
        
        public ListAdapter(final Context context) {
            this.context = context;
        }
        
        @Override
        public int getItemCount() {
            return LocationController.getLocationsCount() + 1;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == 0) {
                return 1;
            }
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    if (SharingLocationsAlert.this.textView != null) {
                        SharingLocationsAlert.this.textView.setText((CharSequence)LocaleController.formatString("SharingLiveLocationTitle", 2131560772, LocaleController.formatPluralString("Chats", LocationController.getLocationsCount())));
                    }
                }
            }
            else {
                ((SharingLiveLocationCell)viewHolder.itemView).setDialog(SharingLocationsAlert.this.getLocation(n - 1));
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            FrameLayout frameLayout;
            if (n != 0) {
                frameLayout = new FrameLayout(this.context) {
                    protected void onDraw(final Canvas canvas) {
                        canvas.drawLine(0.0f, (float)AndroidUtilities.dp(40.0f), (float)this.getMeasuredWidth(), (float)AndroidUtilities.dp(40.0f), Theme.dividerPaint);
                    }
                    
                    protected void onMeasure(final int n, final int n2) {
                        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f) + 1, 1073741824));
                    }
                };
                frameLayout.setWillNotDraw(false);
                SharingLocationsAlert.this.textView = new TextView(this.context);
                SharingLocationsAlert.this.textView.setTextColor(Theme.getColor("dialogIcon"));
                SharingLocationsAlert.this.textView.setTextSize(1, 14.0f);
                SharingLocationsAlert.this.textView.setGravity(17);
                SharingLocationsAlert.this.textView.setPadding(0, 0, 0, AndroidUtilities.dp(8.0f));
                frameLayout.addView((View)SharingLocationsAlert.this.textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 40.0f));
            }
            else {
                frameLayout = new SharingLiveLocationCell(this.context, false);
            }
            return new RecyclerListView.Holder((View)frameLayout);
        }
    }
    
    public interface SharingLocationsAlertDelegate
    {
        void didSelectLocation(final LocationController.SharingLocationInfo p0);
    }
}
