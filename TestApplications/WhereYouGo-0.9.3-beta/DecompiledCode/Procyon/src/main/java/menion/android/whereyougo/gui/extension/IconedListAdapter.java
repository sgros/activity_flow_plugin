// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.extension;

import android.graphics.Bitmap;
import android.view.ViewGroup$LayoutParams;
import menion.android.whereyougo.utils.Const;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.Images;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ListView;
import android.view.View;
import menion.android.whereyougo.utils.Utils;
import java.util.ArrayList;
import android.content.Context;
import android.widget.BaseAdapter;

public class IconedListAdapter extends BaseAdapter
{
    private static final int PADDING;
    private static final String TAG = "IconedListAdapter";
    private static final int TYPE_LIST_VIEW = 0;
    private static final int TYPE_OTHER = 2;
    private static final int TYPE_SPINNER_VIEW = 1;
    private final Context context;
    private final ArrayList<DataInfo> mData;
    private int minHeight;
    private float multiplyImageSize;
    private boolean textView02HideIfEmpty;
    private int textView02Visibility;
    private int type;
    
    static {
        PADDING = (int)Utils.getDpPixels(4.0f);
    }
    
    public IconedListAdapter(final Context context, final ArrayList<DataInfo> mData, final View view) {
        this.type = 0;
        this.textView02Visibility = 0;
        this.textView02HideIfEmpty = false;
        this.minHeight = Integer.MIN_VALUE;
        this.multiplyImageSize = 1.0f;
        this.mData = mData;
        if (view instanceof ListView) {
            ((ListView)view).setBackgroundColor(-1);
            this.type = 0;
        }
        else if (view instanceof Spinner) {
            this.type = 1;
        }
        else {
            this.setTextView02Visible(8, true);
            this.type = 2;
        }
        this.context = context;
    }
    
    private static LinearLayout createEmptyView(final Context context) {
        return (LinearLayout)LinearLayout.inflate(context, 2130903049, (ViewGroup)null);
    }
    
    private View getViewItem(final int n, final View obj, final boolean b) {
        DataInfo dataInfo = null;
        LinearLayout linearLayout = null;
        TextView textView = null;
        TextView textView2 = null;
        ImageView imageView = null;
        ImageView imageView2;
        String name;
        String description;
        float n2;
        float n3;
        int width;
        ViewGroup$LayoutParams layoutParams;
        Bitmap imageB;
        Bitmap imageBitmap;
        Label_0214_Outer:Label_0252_Outer:Label_0337_Outer:
        while (true) {
        Label_0337:
            while (true) {
            Label_0627:
                while (true) {
                Label_0503:
                    while (true) {
                    Label_0444:
                        while (true) {
                            Label_0434: {
                                try {
                                    dataInfo = this.mData.get(n);
                                    linearLayout = (LinearLayout)obj.findViewById(2131492886);
                                    linearLayout.setPadding(IconedListAdapter.PADDING, IconedListAdapter.PADDING, IconedListAdapter.PADDING, IconedListAdapter.PADDING);
                                    if (this.minHeight != Integer.MIN_VALUE) {
                                        linearLayout.setMinimumHeight(this.minHeight);
                                    }
                                    textView = (TextView)obj.findViewById(2131492927);
                                    textView2 = (TextView)obj.findViewById(2131492928);
                                    imageView = (ImageView)obj.findViewById(2131492926);
                                    imageView2 = (ImageView)obj.findViewById(2131492929);
                                    textView.setBackgroundColor(0);
                                    textView.setTextColor(-16777216);
                                    name = dataInfo.getName();
                                    if (name == null) {
                                        textView.setVisibility(8);
                                    }
                                    else {
                                        textView.setVisibility(0);
                                        textView.setText((CharSequence)Html.fromHtml(name));
                                    }
                                    textView2.setTextColor(-12303292);
                                    if (this.textView02Visibility == 8) {
                                        break Label_0434;
                                    }
                                    textView2.setVisibility(0);
                                    if ((description = dataInfo.getDescription()) == null) {
                                        description = "";
                                    }
                                    if (description.length() > 0) {
                                        textView2.setText((CharSequence)Html.fromHtml(description));
                                        n2 = 1.0f;
                                        if (this.type != 1 || b) {
                                            break Label_0444;
                                        }
                                        n2 = 0.75f;
                                        n3 = n2 * this.multiplyImageSize;
                                        width = (int)(Images.SIZE_BIG * n3);
                                        if (dataInfo.getImage() == -1) {
                                            break Label_0503;
                                        }
                                        imageView.setImageResource(dataInfo.getImage());
                                        layoutParams = imageView.getLayoutParams();
                                        layoutParams.width = width;
                                        layoutParams.height = (int)(Images.SIZE_BIG * n3);
                                        imageView.setLayoutParams(layoutParams);
                                        imageView.setVisibility(0);
                                        imageView2.setVisibility(8);
                                        if (dataInfo.getImageRight() != null) {
                                            imageView2.setVisibility(0);
                                            imageView2.setImageBitmap(dataInfo.getImageRight());
                                        }
                                        if (dataInfo.enabled) {
                                            linearLayout.setBackgroundColor(0);
                                            obj.forceLayout();
                                            return obj;
                                        }
                                        break Label_0627;
                                    }
                                }
                                catch (Exception ex) {
                                    Logger.e("IconedListAdapter", "getView(" + n + ", " + obj + ")", ex);
                                    continue Label_0337;
                                }
                                if (this.textView02HideIfEmpty) {
                                    textView2.setVisibility(8);
                                    continue Label_0214_Outer;
                                }
                                textView2.setText(2131165225);
                                continue Label_0214_Outer;
                            }
                            textView2.setVisibility(8);
                            continue Label_0214_Outer;
                        }
                        if (this.type == 1 && b) {
                            n2 = 1.25f;
                            textView.setHeight((int)(Images.SIZE_BIG * 1.25f));
                            continue Label_0252_Outer;
                        }
                        if (this.type == 0) {
                            n2 = 1.0f;
                            continue Label_0252_Outer;
                        }
                        if (this.type == 2) {
                            n2 = 1.0f;
                            continue Label_0252_Outer;
                        }
                        continue Label_0252_Outer;
                    }
                    if (dataInfo.getImageD() != null) {
                        imageView.setImageDrawable(dataInfo.getImageD());
                        continue Label_0337_Outer;
                    }
                    if (dataInfo.getImageB() != null) {
                        imageB = dataInfo.getImageB();
                        if (imageB.getWidth() > Const.SCREEN_WIDTH / 2 && dataInfo.getName() != null && dataInfo.getName().length() > 0) {
                            imageBitmap = Images.resizeBitmap(imageB, Const.SCREEN_WIDTH / 2);
                        }
                        else {
                            imageBitmap = imageB;
                            if (imageB.getWidth() > Const.SCREEN_WIDTH) {
                                imageBitmap = Images.resizeBitmap(imageB, Const.SCREEN_WIDTH);
                            }
                        }
                        imageView.setImageBitmap(imageBitmap);
                        continue Label_0337_Outer;
                    }
                    width = 0;
                    continue Label_0337_Outer;
                }
                linearLayout.setBackgroundColor(-3355444);
                continue Label_0337;
            }
        }
    }
    
    public boolean areAllItemsEnabled() {
        return false;
    }
    
    public int getCount() {
        return this.mData.size();
    }
    
    public DataInfo getDataInfo(final int index) {
        return this.mData.get(index);
    }
    
    public View getDropDownView(final int n, final View view, final ViewGroup viewGroup) {
        Object emptyView = view;
        if (view == null) {
            emptyView = createEmptyView(this.context);
        }
        return this.getViewItem(n, (View)emptyView, true);
    }
    
    public Object getItem(final int index) {
        return this.mData.get(index);
    }
    
    public long getItemId(final int n) {
        return n;
    }
    
    public View getView(final int n, final View view, final ViewGroup viewGroup) {
        Object emptyView = view;
        if (view == null) {
            emptyView = createEmptyView(this.context);
        }
        return this.getViewItem(n, (View)emptyView, false);
    }
    
    public boolean isEnabled(final int n) {
        try {
            return this.mData.get(n).enabled;
        }
        catch (Exception ex) {
            Logger.e("IconedListAdapter", "isEnabled(" + n + ")", ex);
            return false;
        }
    }
    
    public void setMinHeight(final int minHeight) {
        this.minHeight = minHeight;
    }
    
    public void setMultiplyImageSize(final float multiplyImageSize) {
        this.multiplyImageSize = multiplyImageSize;
    }
    
    public void setTextView02Visible(final int textView02Visibility, final boolean textView02HideIfEmpty) {
        this.textView02Visibility = textView02Visibility;
        this.textView02HideIfEmpty = textView02HideIfEmpty;
    }
}
