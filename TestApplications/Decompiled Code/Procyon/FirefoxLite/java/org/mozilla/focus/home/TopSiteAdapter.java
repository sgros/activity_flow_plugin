// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import org.mozilla.rocket.home.pinsite.PinViewWrapper;
import android.support.v7.widget.AppCompatImageView;
import android.os.StrictMode$ThreadPolicy;
import android.view.View;
import android.support.v4.view.ViewCompat;
import android.content.res.ColorStateList;
import android.os.StrictMode;
import android.content.Context;
import org.mozilla.focus.utils.DimenUtils;
import android.content.res.Resources;
import org.mozilla.icon.FavIconUtils;
import android.graphics.Bitmap;
import java.util.Collection;
import java.util.ArrayList;
import org.mozilla.focus.history.model.Site;
import java.util.List;
import org.mozilla.rocket.home.pinsite.PinSiteManager;
import android.view.View$OnLongClickListener;
import android.view.View$OnClickListener;
import android.support.v7.widget.RecyclerView;

class TopSiteAdapter extends Adapter<SiteViewHolder>
{
    private final View$OnClickListener clickListener;
    private final View$OnLongClickListener longClickListener;
    private final PinSiteManager pinSiteManager;
    private List<Site> sites;
    
    TopSiteAdapter(final List<Site> list, final View$OnClickListener clickListener, final View$OnLongClickListener longClickListener, final PinSiteManager pinSiteManager) {
        (this.sites = new ArrayList<Site>()).addAll(list);
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
        this.pinSiteManager = pinSiteManager;
    }
    
    private int addWhiteToColorCode(int n, final float n2) {
        if ((n += (int)(n2 * 255.0f / 2.0f)) > 255) {
            n = 255;
        }
        return n;
    }
    
    private int calculateBackgroundColor(final Bitmap bitmap) {
        final int dominantColor = FavIconUtils.getDominantColor(bitmap);
        return (0xFF000000 & dominantColor) + (this.addWhiteToColorCode((0xFF0000 & dominantColor) >> 16, 0.25f) << 16) + (this.addWhiteToColorCode((0xFF00 & dominantColor) >> 8, 0.25f) << 8) + this.addWhiteToColorCode(dominantColor & 0xFF, 0.25f);
    }
    
    private Bitmap createFavicon(final Resources resources, final String s, final int n) {
        return DimenUtils.getInitialBitmap(resources, FavIconUtils.getRepresentativeCharacter(s), n);
    }
    
    private Bitmap getBestFavicon(final Resources resources, final String s, final Bitmap bitmap) {
        if (bitmap == null) {
            return this.createFavicon(resources, s, -1);
        }
        if (DimenUtils.iconTooBlurry(resources, bitmap.getWidth())) {
            return this.createFavicon(resources, s, FavIconUtils.getDominantColor(bitmap));
        }
        return bitmap;
    }
    
    private Bitmap getFavicon(final Context context, final Site site) {
        final String favIconUri = site.getFavIconUri();
        Bitmap bitmapFromUri;
        if (favIconUri != null) {
            bitmapFromUri = FavIconUtils.getBitmapFromUri(context, favIconUri);
        }
        else {
            bitmapFromUri = null;
        }
        return this.getBestFavicon(context.getResources(), site.getUrl(), bitmapFromUri);
    }
    
    @Override
    public int getItemCount() {
        return this.sites.size();
    }
    
    public void onBindViewHolder(final SiteViewHolder siteViewHolder, int visibility) {
        final Site tag = this.sites.get(visibility);
        siteViewHolder.text.setText((CharSequence)tag.getTitle());
        final StrictMode$ThreadPolicy allowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
        final Bitmap favicon = this.getFavicon(siteViewHolder.itemView.getContext(), tag);
        StrictMode.setThreadPolicy(allowThreadDiskWrites);
        final AppCompatImageView img = siteViewHolder.img;
        visibility = 0;
        img.setVisibility(0);
        siteViewHolder.img.setImageBitmap(favicon);
        final int calculateBackgroundColor = this.calculateBackgroundColor(favicon);
        ViewCompat.setBackgroundTintList((View)siteViewHolder.img, ColorStateList.valueOf(calculateBackgroundColor));
        final PinViewWrapper pinView = siteViewHolder.pinView;
        if (!this.pinSiteManager.isPinned(tag)) {
            visibility = 8;
        }
        pinView.setVisibility(visibility);
        siteViewHolder.pinView.setPinColor(calculateBackgroundColor);
        siteViewHolder.itemView.setTag((Object)tag);
        if (this.clickListener != null) {
            siteViewHolder.itemView.setOnClickListener(this.clickListener);
        }
        if (this.longClickListener != null) {
            siteViewHolder.itemView.setOnLongClickListener(this.longClickListener);
        }
    }
    
    public SiteViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        return new SiteViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2131492987, viewGroup, false));
    }
    
    public void setSites(final List<Site> sites) {
        this.sites = sites;
        ((RecyclerView.Adapter)this).notifyDataSetChanged();
    }
}
