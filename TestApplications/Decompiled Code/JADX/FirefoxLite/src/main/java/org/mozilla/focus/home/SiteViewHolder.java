package org.mozilla.focus.home;

import android.support.p004v7.widget.AppCompatImageView;
import android.support.p004v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.mozilla.focus.C0427R;
import org.mozilla.rocket.home.pinsite.PinViewWrapper;

class SiteViewHolder extends ViewHolder {
    AppCompatImageView img;
    PinViewWrapper pinView;
    TextView text;

    public SiteViewHolder(View view) {
        super(view);
        this.img = (AppCompatImageView) view.findViewById(C0427R.C0426id.content_image);
        this.text = (TextView) view.findViewById(2131296685);
        this.pinView = new PinViewWrapper((ViewGroup) view.findViewById(C0427R.C0426id.pin_indicator));
    }
}
