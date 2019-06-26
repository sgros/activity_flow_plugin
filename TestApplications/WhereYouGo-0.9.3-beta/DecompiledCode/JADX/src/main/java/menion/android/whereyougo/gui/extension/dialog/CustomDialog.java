package menion.android.whereyougo.gui.extension.dialog;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.utils.Utils;

public class CustomDialog {
    public static final int BOTTOM_COLOR_A3 = -2236963;
    public static final int NO_IMAGE = Integer.MIN_VALUE;
    private static final int TITLE_BUTTON_LEFT = 2;
    private static final int TITLE_BUTTON_RIGHT = 1;

    public interface OnClickListener {
        boolean onClick(CustomDialog customDialog, View view, int i);
    }

    private static void addViewToContent(View viewContent, LayoutParams llLp, View view) {
        LinearLayout llContent = (LinearLayout) viewContent;
        llContent.removeAllViews();
        if (llLp == null) {
            llContent.addView(view, new LayoutParams(-1, -2));
        } else {
            llContent.addView(view, llLp);
        }
    }

    public static void setBottom(Activity activity, String positiveButtonText, OnClickListener positiveButtonClickListener, String neutralButtonText, OnClickListener neutralButtonClickListener, String negativeButtonText, OnClickListener negativeButtonClickListener) {
        setCustomDialogBottom(activity.findViewById(C0254R.C0253id.linear_layout_bottom), positiveButtonText, positiveButtonClickListener, neutralButtonText, neutralButtonClickListener, negativeButtonText, negativeButtonClickListener);
    }

    private static boolean setButton(View layout, int btnId, final int btnType, String text, final OnClickListener click) {
        if (text == null || click == null) {
            layout.findViewById(btnId).setVisibility(8);
            return false;
        }
        Button btn = (Button) layout.findViewById(btnId);
        btn.setText(text);
        btn.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View v) {
                click.onClick(null, v, btnType);
            }
        });
        btn.setVisibility(0);
        return true;
    }

    public static void setContent(Activity activity, View view, int margins, boolean fillHeight, boolean dialog) {
        int i;
        int i2 = -2;
        if (dialog) {
            UtilsGUI.setWindowDialogCorrectWidth(activity.getWindow());
        }
        if (fillHeight) {
            i = -1;
        } else {
            i = -2;
        }
        LayoutParams lp = new LayoutParams(-1, i);
        if (margins > 0) {
            lp.setMargins(margins, activity.getResources().getDimensionPixelSize(C0254R.dimen.shadow_height) + margins, margins, margins);
        }
        LinearLayout llCon = (LinearLayout) activity.findViewById(C0254R.C0253id.linear_layout_content);
        if (fillHeight) {
            i2 = -1;
        }
        llCon.setLayoutParams(new RelativeLayout.LayoutParams(-1, i2));
        addViewToContent(llCon, lp, view);
    }

    private static void setCustomDialogBottom(View view, String positiveButtonText, OnClickListener positiveButtonClickListener, String neutralButtonText, OnClickListener neutralButtonClickListener, String negativeButtonText, OnClickListener negativeButtonClickListener) {
        if (Utils.isAndroid30OrMore()) {
            view.findViewById(C0254R.C0253id.linear_layout_bottom).setBackgroundColor(BOTTOM_COLOR_A3);
        }
        int btnCount = 0;
        if (setButton(view, C0254R.C0253id.button_positive, -1, positiveButtonText, positiveButtonClickListener)) {
            btnCount = 0 + 1;
        }
        if (setButton(view, C0254R.C0253id.button_negative, -2, negativeButtonText, negativeButtonClickListener)) {
            btnCount++;
        }
        if (setButton(view, C0254R.C0253id.button_neutral, -3, neutralButtonText, neutralButtonClickListener)) {
            btnCount++;
        }
        if (btnCount == 0) {
            view.findViewById(C0254R.C0253id.linear_layout_bottom).setVisibility(8);
        } else if (btnCount == 1) {
            view.findViewById(C0254R.C0253id.linear_layout_bottom).setVisibility(0);
            view.findViewById(C0254R.C0253id.linear_layout_left_spacer).setVisibility(0);
            view.findViewById(C0254R.C0253id.linear_layout_right_spacer).setVisibility(0);
        } else {
            view.findViewById(C0254R.C0253id.linear_layout_bottom).setVisibility(0);
            view.findViewById(C0254R.C0253id.linear_layout_left_spacer).setVisibility(8);
            view.findViewById(C0254R.C0253id.linear_layout_right_spacer).setVisibility(8);
        }
    }

    private static void setCustomDialogTitle(View view, CharSequence titleText, Bitmap titleImage, int titleExtraImg1, OnClickListener titleExtraClick1, int titleExtraImg2, OnClickListener titleExtraClick2) {
        if (titleImage == null && titleText == null && titleExtraImg1 == Integer.MIN_VALUE && titleExtraImg2 == Integer.MIN_VALUE) {
            view.findViewById(C0254R.C0253id.linear_layout_title).setVisibility(8);
            return;
        }
        if (titleImage == null) {
            view.findViewById(C0254R.C0253id.image_view_title_logo).setVisibility(4);
        } else {
            ((ImageView) view.findViewById(C0254R.C0253id.image_view_title_logo)).setImageBitmap(titleImage);
        }
        ((TextView) view.findViewById(C0254R.C0253id.text_view_title_text)).setText(titleText);
        setCustomDialogTitleButton(view, 1, titleExtraImg1, titleExtraClick1);
        setCustomDialogTitleButton(view, 2, titleExtraImg2, titleExtraClick2);
    }

    private static void setCustomDialogTitleButton(View view, int button, int titleExtraImg, final OnClickListener titleExtraClick) {
        if (titleExtraImg != Integer.MIN_VALUE && titleExtraClick != null) {
            ImageView iv;
            ImageButton ib;
            if (button == 1) {
                iv = (ImageView) view.findViewById(C0254R.C0253id.image_view_separator1);
                ib = (ImageButton) view.findViewById(C0254R.C0253id.image_button_title1);
            } else {
                iv = (ImageView) view.findViewById(C0254R.C0253id.image_view_separator2);
                ib = (ImageButton) view.findViewById(C0254R.C0253id.image_button_title2);
            }
            iv.setVisibility(0);
            ib.setVisibility(0);
            ib.setImageResource(titleExtraImg);
            ib.setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(View v) {
                    titleExtraClick.onClick(null, v, 0);
                }
            });
        }
    }

    public static void setTitle(Activity activity, CharSequence titleText, Bitmap titleImage, int titleExtraImg, OnClickListener titleExtraClick) {
        setCustomDialogTitle(activity.findViewById(C0254R.C0253id.linear_layout_main), titleText, titleImage, titleExtraImg, titleExtraClick, Integer.MIN_VALUE, null);
    }
}
