package org.mozilla.focus.site;

import android.support.p004v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.mozilla.focus.C0427R;

public class SiteItemViewHolder extends ViewHolder {
    public FrameLayout btnMore;
    public ImageView imgFav;
    public ViewGroup rootView;
    public TextView textMain;
    public TextView textSecondary;

    public SiteItemViewHolder(View view) {
        super(view);
        this.rootView = (ViewGroup) view.findViewById(C0427R.C0426id.history_item_root_view);
        this.imgFav = (ImageView) view.findViewById(C0427R.C0426id.history_item_img_fav);
        this.textMain = (TextView) view.findViewById(C0427R.C0426id.history_item_text_main);
        this.textSecondary = (TextView) view.findViewById(C0427R.C0426id.history_item_text_secondary);
        this.btnMore = (FrameLayout) view.findViewById(C0427R.C0426id.history_item_btn_more);
    }
}
