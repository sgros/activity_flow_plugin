// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.extension.dialog;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Bitmap;
import menion.android.whereyougo.utils.Utils;
import android.widget.RelativeLayout$LayoutParams;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import android.view.View$OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout$LayoutParams;
import android.view.View;

public class CustomDialog
{
    public static final int BOTTOM_COLOR_A3 = -2236963;
    public static final int NO_IMAGE = Integer.MIN_VALUE;
    private static final int TITLE_BUTTON_LEFT = 2;
    private static final int TITLE_BUTTON_RIGHT = 1;
    
    private static void addViewToContent(final View view, final LinearLayout$LayoutParams linearLayout$LayoutParams, final View view2) {
        final LinearLayout linearLayout = (LinearLayout)view;
        linearLayout.removeAllViews();
        if (linearLayout$LayoutParams == null) {
            linearLayout.addView(view2, (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-1, -2));
        }
        else {
            linearLayout.addView(view2, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
        }
    }
    
    public static void setBottom(final Activity activity, final String s, final OnClickListener onClickListener, final String s2, final OnClickListener onClickListener2, final String s3, final OnClickListener onClickListener3) {
        setCustomDialogBottom(activity.findViewById(2131492930), s, onClickListener, s2, onClickListener2, s3, onClickListener3);
    }
    
    private static boolean setButton(final View view, final int n, final int n2, final String text, final OnClickListener onClickListener) {
        boolean b = false;
        if (text != null && onClickListener != null) {
            final Button button = (Button)view.findViewById(n);
            button.setText((CharSequence)text);
            button.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    onClickListener.onClick(null, view, n2);
                }
            });
            button.setVisibility(0);
            b = true;
        }
        else {
            view.findViewById(n).setVisibility(8);
        }
        return b;
    }
    
    public static void setContent(final Activity activity, final View view, int n, final boolean b, final boolean b2) {
        final int n2 = -2;
        if (b2) {
            UtilsGUI.setWindowDialogCorrectWidth(activity.getWindow());
        }
        int n3;
        if (b) {
            n3 = -1;
        }
        else {
            n3 = -2;
        }
        final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(-1, n3);
        if (n > 0) {
            linearLayout$LayoutParams.setMargins(n, activity.getResources().getDimensionPixelSize(2131427330) + n, n, n);
        }
        final LinearLayout linearLayout = (LinearLayout)activity.findViewById(2131492894);
        n = n2;
        if (b) {
            n = -1;
        }
        linearLayout.setLayoutParams((ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, n));
        addViewToContent((View)linearLayout, linearLayout$LayoutParams, view);
    }
    
    private static void setCustomDialogBottom(final View view, final String s, final OnClickListener onClickListener, final String s2, final OnClickListener onClickListener2, final String s3, final OnClickListener onClickListener3) {
        if (Utils.isAndroid30OrMore()) {
            view.findViewById(2131492930).setBackgroundColor(-2236963);
        }
        int n = 0;
        if (setButton(view, 2131492932, -1, s, onClickListener)) {
            n = 0 + 1;
        }
        int n2 = n;
        if (setButton(view, 2131492934, -2, s3, onClickListener3)) {
            n2 = n + 1;
        }
        int n3 = n2;
        if (setButton(view, 2131492933, -3, s2, onClickListener2)) {
            n3 = n2 + 1;
        }
        if (n3 == 0) {
            view.findViewById(2131492930).setVisibility(8);
        }
        else if (n3 == 1) {
            view.findViewById(2131492930).setVisibility(0);
            view.findViewById(2131492931).setVisibility(0);
            view.findViewById(2131492935).setVisibility(0);
        }
        else {
            view.findViewById(2131492930).setVisibility(0);
            view.findViewById(2131492931).setVisibility(8);
            view.findViewById(2131492935).setVisibility(8);
        }
    }
    
    private static void setCustomDialogTitle(final View view, final CharSequence text, final Bitmap imageBitmap, final int n, final OnClickListener onClickListener, final int n2, final OnClickListener onClickListener2) {
        if (imageBitmap == null && text == null && n == Integer.MIN_VALUE && n2 == Integer.MIN_VALUE) {
            view.findViewById(2131492887).setVisibility(8);
        }
        else {
            if (imageBitmap == null) {
                view.findViewById(2131492888).setVisibility(4);
            }
            else {
                ((ImageView)view.findViewById(2131492888)).setImageBitmap(imageBitmap);
            }
            ((TextView)view.findViewById(2131492889)).setText(text);
            setCustomDialogTitleButton(view, 1, n, onClickListener);
            setCustomDialogTitleButton(view, 2, n2, onClickListener2);
        }
    }
    
    private static void setCustomDialogTitleButton(final View view, final int n, final int imageResource, final OnClickListener onClickListener) {
        if (imageResource != Integer.MIN_VALUE && onClickListener != null) {
            ImageView imageView;
            ImageButton imageButton;
            if (n == 1) {
                imageView = (ImageView)view.findViewById(2131492892);
                imageButton = (ImageButton)view.findViewById(2131492893);
            }
            else {
                imageView = (ImageView)view.findViewById(2131492890);
                imageButton = (ImageButton)view.findViewById(2131492891);
            }
            imageView.setVisibility(0);
            imageButton.setVisibility(0);
            imageButton.setImageResource(imageResource);
            imageButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    onClickListener.onClick(null, view, 0);
                }
            });
        }
    }
    
    public static void setTitle(final Activity activity, final CharSequence charSequence, final Bitmap bitmap, final int n, final OnClickListener onClickListener) {
        setCustomDialogTitle(activity.findViewById(2131492886), charSequence, bitmap, n, onClickListener, Integer.MIN_VALUE, null);
    }
    
    public interface OnClickListener
    {
        boolean onClick(final CustomDialog p0, final View p1, final int p2);
    }
}
