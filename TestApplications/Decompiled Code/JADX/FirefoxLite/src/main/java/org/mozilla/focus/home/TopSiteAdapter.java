package org.mozilla.focus.home;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.support.p001v4.view.ViewCompat;
import android.support.p004v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import org.mozilla.focus.history.model.Site;
import org.mozilla.focus.utils.DimenUtils;
import org.mozilla.icon.FavIconUtils;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.home.pinsite.PinSiteManager;
import org.mozilla.rocket.home.pinsite.PinViewWrapper;

class TopSiteAdapter extends Adapter<SiteViewHolder> {
    private final OnClickListener clickListener;
    private final OnLongClickListener longClickListener;
    private final PinSiteManager pinSiteManager;
    private List<Site> sites = new ArrayList();

    private int addWhiteToColorCode(int i, float f) {
        i = (int) (((float) i) + ((f * 255.0f) / 2.0f));
        return i > 255 ? 255 : i;
    }

    TopSiteAdapter(List<Site> list, OnClickListener onClickListener, OnLongClickListener onLongClickListener, PinSiteManager pinSiteManager) {
        this.sites.addAll(list);
        this.clickListener = onClickListener;
        this.longClickListener = onLongClickListener;
        this.pinSiteManager = pinSiteManager;
    }

    public SiteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SiteViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0769R.layout.item_top_site, viewGroup, false));
    }

    public void onBindViewHolder(SiteViewHolder siteViewHolder, int i) {
        Site site = (Site) this.sites.get(i);
        siteViewHolder.text.setText(site.getTitle());
        ThreadPolicy allowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
        Bitmap favicon = getFavicon(siteViewHolder.itemView.getContext(), site);
        StrictMode.setThreadPolicy(allowThreadDiskWrites);
        int i2 = 0;
        siteViewHolder.img.setVisibility(0);
        siteViewHolder.img.setImageBitmap(favicon);
        int calculateBackgroundColor = calculateBackgroundColor(favicon);
        ViewCompat.setBackgroundTintList(siteViewHolder.img, ColorStateList.valueOf(calculateBackgroundColor));
        PinViewWrapper pinViewWrapper = siteViewHolder.pinView;
        if (!this.pinSiteManager.isPinned(site)) {
            i2 = 8;
        }
        pinViewWrapper.setVisibility(i2);
        siteViewHolder.pinView.setPinColor(calculateBackgroundColor);
        siteViewHolder.itemView.setTag(site);
        if (this.clickListener != null) {
            siteViewHolder.itemView.setOnClickListener(this.clickListener);
        }
        if (this.longClickListener != null) {
            siteViewHolder.itemView.setOnLongClickListener(this.longClickListener);
        }
    }

    private Bitmap getFavicon(Context context, Site site) {
        String favIconUri = site.getFavIconUri();
        return getBestFavicon(context.getResources(), site.getUrl(), favIconUri != null ? FavIconUtils.getBitmapFromUri(context, favIconUri) : null);
    }

    private Bitmap getBestFavicon(Resources resources, String str, Bitmap bitmap) {
        if (bitmap == null) {
            return createFavicon(resources, str, -1);
        }
        return DimenUtils.iconTooBlurry(resources, bitmap.getWidth()) ? createFavicon(resources, str, FavIconUtils.getDominantColor(bitmap)) : bitmap;
    }

    private Bitmap createFavicon(Resources resources, String str, int i) {
        return DimenUtils.getInitialBitmap(resources, FavIconUtils.getRepresentativeCharacter(str), i);
    }

    private int calculateBackgroundColor(Bitmap bitmap) {
        int dominantColor = FavIconUtils.getDominantColor(bitmap);
        int addWhiteToColorCode = addWhiteToColorCode((65280 & dominantColor) >> 8, 0.25f) << 8;
        return (((-16777216 & dominantColor) + (addWhiteToColorCode((16711680 & dominantColor) >> 16, 0.25f) << 16)) + addWhiteToColorCode) + addWhiteToColorCode(dominantColor & 255, 0.25f);
    }

    public int getItemCount() {
        return this.sites.size();
    }

    public void setSites(List<Site> list) {
        this.sites = list;
        notifyDataSetChanged();
    }
}
