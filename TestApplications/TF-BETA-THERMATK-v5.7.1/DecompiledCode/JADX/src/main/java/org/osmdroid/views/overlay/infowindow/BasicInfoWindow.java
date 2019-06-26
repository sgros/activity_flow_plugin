package org.osmdroid.views.overlay.infowindow;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayWithIW;

public class BasicInfoWindow extends InfoWindow {
    static int mDescriptionId;
    static int mImageId;
    static int mSubDescriptionId;
    static int mTitleId;

    /* renamed from: org.osmdroid.views.overlay.infowindow.BasicInfoWindow$1 */
    class C02761 implements OnTouchListener {
        C02761() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 1) {
                BasicInfoWindow.this.close();
            }
            return true;
        }
    }

    public void onClose() {
    }

    private static void setResIds(Context context) {
        String packageName = context.getPackageName();
        mTitleId = context.getResources().getIdentifier("id/bubble_title", null, packageName);
        mDescriptionId = context.getResources().getIdentifier("id/bubble_description", null, packageName);
        mSubDescriptionId = context.getResources().getIdentifier("id/bubble_subdescription", null, packageName);
        mImageId = context.getResources().getIdentifier("id/bubble_image", null, packageName);
        if (mTitleId == 0 || mDescriptionId == 0 || mSubDescriptionId == 0 || mImageId == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("BasicInfoWindow: unable to get res ids in ");
            stringBuilder.append(packageName);
            Log.e("OsmDroid", stringBuilder.toString());
        }
    }

    public BasicInfoWindow(int i, MapView mapView) {
        super(i, mapView);
        if (mTitleId == 0) {
            setResIds(mapView.getContext());
        }
        this.mView.setOnTouchListener(new C02761());
    }

    public void onOpen(Object obj) {
        OverlayWithIW overlayWithIW = (OverlayWithIW) obj;
        CharSequence title = overlayWithIW.getTitle();
        String str = "";
        if (title == null) {
            title = str;
        }
        View view = this.mView;
        if (view == null) {
            Log.w("OsmDroid", "Error trapped, BasicInfoWindow.open, mView is null!");
            return;
        }
        TextView textView = (TextView) view.findViewById(mTitleId);
        if (textView != null) {
            textView.setText(title);
        }
        String snippet = overlayWithIW.getSnippet();
        if (snippet == null) {
            snippet = str;
        }
        ((TextView) this.mView.findViewById(mDescriptionId)).setText(Html.fromHtml(snippet));
        TextView textView2 = (TextView) this.mView.findViewById(mSubDescriptionId);
        String subDescription = overlayWithIW.getSubDescription();
        if (subDescription == null || str.equals(subDescription)) {
            textView2.setVisibility(8);
        } else {
            textView2.setText(Html.fromHtml(subDescription));
            textView2.setVisibility(0);
        }
    }
}
