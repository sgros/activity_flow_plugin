package menion.android.whereyougo.gui.extension;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.utils.Const;
import menion.android.whereyougo.utils.Images;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.Utils;

public class IconedListAdapter extends BaseAdapter {
    private static final int PADDING = ((int) Utils.getDpPixels(4.0f));
    private static final String TAG = "IconedListAdapter";
    private static final int TYPE_LIST_VIEW = 0;
    private static final int TYPE_OTHER = 2;
    private static final int TYPE_SPINNER_VIEW = 1;
    private final Context context;
    private final ArrayList<DataInfo> mData;
    private int minHeight = Integer.MIN_VALUE;
    private float multiplyImageSize = 1.0f;
    private boolean textView02HideIfEmpty = false;
    private int textView02Visibility = 0;
    private int type = 0;

    public IconedListAdapter(Context context, ArrayList<DataInfo> data, View view) {
        this.mData = data;
        if (view instanceof ListView) {
            ((ListView) view).setBackgroundColor(-1);
            this.type = 0;
        } else if (view instanceof Spinner) {
            this.type = 1;
        } else {
            setTextView02Visible(8, true);
            this.type = 2;
        }
        this.context = context;
    }

    private static LinearLayout createEmptyView(Context context) {
        return (LinearLayout) LinearLayout.inflate(context, C0254R.layout.iconed_list_adapter, null);
    }

    public boolean areAllItemsEnabled() {
        return false;
    }

    public int getCount() {
        return this.mData.size();
    }

    public DataInfo getDataInfo(int position) {
        return (DataInfo) this.mData.get(position);
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = createEmptyView(this.context);
        }
        return getViewItem(position, convertView, true);
    }

    public Object getItem(int position) {
        return this.mData.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = createEmptyView(this.context);
        }
        return getViewItem(position, convertView, false);
    }

    private View getViewItem(int position, View convertView, boolean dropDown) {
        try {
            DataInfo di = (DataInfo) this.mData.get(position);
            LinearLayout llMain = (LinearLayout) convertView.findViewById(C0254R.C0253id.linear_layout_main);
            llMain.setPadding(PADDING, PADDING, PADDING, PADDING);
            if (this.minHeight != Integer.MIN_VALUE) {
                llMain.setMinimumHeight(this.minHeight);
            }
            TextView tv01 = (TextView) convertView.findViewById(C0254R.C0253id.layoutIconedListAdapterTextView01);
            TextView tv02 = (TextView) convertView.findViewById(C0254R.C0253id.layoutIconedListAdapterTextView02);
            ImageView iv01 = (ImageView) convertView.findViewById(C0254R.C0253id.layoutIconedListAdapterImageView01);
            ImageView iv02 = (ImageView) convertView.findViewById(C0254R.C0253id.layoutIconedListAdapterImageView02);
            tv01.setBackgroundColor(0);
            tv01.setTextColor(-16777216);
            String name = di.getName();
            if (name == null) {
                tv01.setVisibility(8);
            } else {
                tv01.setVisibility(0);
                tv01.setText(Html.fromHtml(name));
            }
            tv02.setTextColor(-12303292);
            if (this.textView02Visibility != 8) {
                tv02.setVisibility(0);
                String desc = di.getDescription();
                if (desc == null) {
                    desc = "";
                }
                if (desc.length() > 0) {
                    tv02.setText(Html.fromHtml(desc));
                } else if (this.textView02HideIfEmpty) {
                    tv02.setVisibility(8);
                } else {
                    tv02.setText(C0254R.string.no_description);
                }
            } else {
                tv02.setVisibility(8);
            }
            float multi = 1.0f;
            if (this.type == 1 && !dropDown) {
                multi = 0.75f;
            } else if (this.type == 1 && dropDown) {
                multi = 1.25f;
                tv01.setHeight((int) (((float) Images.SIZE_BIG) * 1.25f));
            } else if (this.type == 0) {
                multi = 1.0f;
            } else if (this.type == 2) {
                multi = 1.0f;
            }
            multi *= this.multiplyImageSize;
            int iv01Width = (int) (((float) Images.SIZE_BIG) * multi);
            if (di.getImage() != -1) {
                iv01.setImageResource(di.getImage());
            } else if (di.getImageD() != null) {
                iv01.setImageDrawable(di.getImageD());
            } else if (di.getImageB() != null) {
                Bitmap bitmap = di.getImageB();
                if (bitmap.getWidth() <= Const.SCREEN_WIDTH / 2 || di.getName() == null || di.getName().length() <= 0) {
                    if (bitmap.getWidth() > Const.SCREEN_WIDTH) {
                        bitmap = Images.resizeBitmap(bitmap, Const.SCREEN_WIDTH);
                    }
                } else {
                    bitmap = Images.resizeBitmap(bitmap, Const.SCREEN_WIDTH / 2);
                }
                iv01.setImageBitmap(bitmap);
            } else {
                iv01Width = 0;
            }
            LayoutParams params = iv01.getLayoutParams();
            params.width = iv01Width;
            params.height = (int) (((float) Images.SIZE_BIG) * multi);
            iv01.setLayoutParams(params);
            iv01.setVisibility(0);
            iv02.setVisibility(8);
            if (di.getImageRight() != null) {
                iv02.setVisibility(0);
                iv02.setImageBitmap(di.getImageRight());
            }
            if (di.enabled) {
                llMain.setBackgroundColor(0);
            } else {
                llMain.setBackgroundColor(-3355444);
            }
        } catch (Exception e) {
            Logger.m22e(TAG, "getView(" + position + ", " + convertView + ")", e);
        }
        convertView.forceLayout();
        return convertView;
    }

    public boolean isEnabled(int position) {
        try {
            return ((DataInfo) this.mData.get(position)).enabled;
        } catch (Exception e) {
            Logger.m22e(TAG, "isEnabled(" + position + ")", e);
            return false;
        }
    }

    public void setMinHeight(int i) {
        this.minHeight = i;
    }

    public void setMultiplyImageSize(float multiplyImageSize) {
        this.multiplyImageSize = multiplyImageSize;
    }

    public void setTextView02Visible(int visibility, boolean hideIfEmpty) {
        this.textView02Visibility = visibility;
        this.textView02HideIfEmpty = hideIfEmpty;
    }
}
