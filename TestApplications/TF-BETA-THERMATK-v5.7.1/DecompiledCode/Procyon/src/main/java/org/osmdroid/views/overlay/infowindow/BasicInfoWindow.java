// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.views.overlay.infowindow;

import android.text.Html;
import android.widget.TextView;
import org.osmdroid.views.overlay.OverlayWithIW;
import android.util.Log;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View$OnTouchListener;
import org.osmdroid.views.MapView;

public class BasicInfoWindow extends InfoWindow
{
    static int mDescriptionId;
    static int mImageId;
    static int mSubDescriptionId;
    static int mTitleId;
    
    public BasicInfoWindow(final int n, final MapView mapView) {
        super(n, mapView);
        if (BasicInfoWindow.mTitleId == 0) {
            setResIds(mapView.getContext());
        }
        super.mView.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1) {
                    BasicInfoWindow.this.close();
                }
                return true;
            }
        });
    }
    
    private static void setResIds(final Context context) {
        final String packageName = context.getPackageName();
        BasicInfoWindow.mTitleId = context.getResources().getIdentifier("id/bubble_title", (String)null, packageName);
        BasicInfoWindow.mDescriptionId = context.getResources().getIdentifier("id/bubble_description", (String)null, packageName);
        BasicInfoWindow.mSubDescriptionId = context.getResources().getIdentifier("id/bubble_subdescription", (String)null, packageName);
        BasicInfoWindow.mImageId = context.getResources().getIdentifier("id/bubble_image", (String)null, packageName);
        if (BasicInfoWindow.mTitleId == 0 || BasicInfoWindow.mDescriptionId == 0 || BasicInfoWindow.mSubDescriptionId == 0 || BasicInfoWindow.mImageId == 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("BasicInfoWindow: unable to get res ids in ");
            sb.append(packageName);
            Log.e("OsmDroid", sb.toString());
        }
    }
    
    @Override
    public void onClose() {
    }
    
    @Override
    public void onOpen(final Object o) {
        final OverlayWithIW overlayWithIW = (OverlayWithIW)o;
        String title;
        if ((title = overlayWithIW.getTitle()) == null) {
            title = "";
        }
        final View mView = super.mView;
        if (mView == null) {
            Log.w("OsmDroid", "Error trapped, BasicInfoWindow.open, mView is null!");
            return;
        }
        final TextView textView = (TextView)mView.findViewById(BasicInfoWindow.mTitleId);
        if (textView != null) {
            textView.setText((CharSequence)title);
        }
        String snippet;
        if ((snippet = overlayWithIW.getSnippet()) == null) {
            snippet = "";
        }
        ((TextView)super.mView.findViewById(BasicInfoWindow.mDescriptionId)).setText((CharSequence)Html.fromHtml(snippet));
        final TextView textView2 = (TextView)super.mView.findViewById(BasicInfoWindow.mSubDescriptionId);
        final String subDescription = overlayWithIW.getSubDescription();
        if (subDescription != null && !"".equals(subDescription)) {
            textView2.setText((CharSequence)Html.fromHtml(subDescription));
            textView2.setVisibility(0);
        }
        else {
            textView2.setVisibility(8);
        }
    }
}
