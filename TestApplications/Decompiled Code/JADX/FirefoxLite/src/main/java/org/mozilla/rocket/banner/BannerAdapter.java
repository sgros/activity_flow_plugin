package org.mozilla.rocket.banner;

import android.content.Context;
import android.support.p004v7.widget.RecyclerView;
import android.support.p004v7.widget.RecyclerView.Adapter;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.rocket.util.LoggerWrapper;

public class BannerAdapter extends Adapter<BannerViewHolder> {
    private List<BannerDAO> DAOs = new ArrayList();
    private Context context;
    private OnClickListener onClickListener;

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.context = recyclerView.getContext();
    }

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.context = null;
    }

    public BannerAdapter(String[] strArr, OnClickListener onClickListener) throws JSONException {
        this.onClickListener = onClickListener;
        for (String jSONObject : strArr) {
            JSONObject jSONObject2 = new JSONObject(jSONObject);
            BannerDAO bannerDAO = new BannerDAO();
            bannerDAO.type = jSONObject2.getString("type");
            bannerDAO.values = jSONObject2.getJSONArray("values");
            bannerDAO.f53id = jSONObject2.getString("id");
            if (getItemViewType(bannerDAO.type) == -1) {
                LoggerWrapper.throwOrWarn("BannerAdapter", String.format(Locale.US, "Unknown view type: %s in page %d", new Object[]{bannerDAO.type, Integer.valueOf(r0)}));
            } else {
                this.DAOs.add(bannerDAO);
            }
        }
    }

    public int getItemViewType(int i) {
        return getItemViewType(((BannerDAO) this.DAOs.get(i)).type);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x003a A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x003d A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003c A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003b A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x003a A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x003d A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003c A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003b A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x003a A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x003d A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003c A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003b A:{RETURN} */
    private int getItemViewType(java.lang.String r7) {
        /*
        r6 = this;
        r0 = r7.hashCode();
        r1 = 93508654; // 0x592d42e float:1.3807717E-35 double:4.61994135E-316;
        r2 = 0;
        r3 = 2;
        r4 = 1;
        r5 = -1;
        if (r0 == r1) goto L_0x002c;
    L_0x000d:
        r1 = 1342277043; // 0x500185b3 float:8.6920919E9 double:6.63172974E-315;
        if (r0 == r1) goto L_0x0022;
    L_0x0012:
        r1 = 1754382089; // 0x6891bf09 float:5.506137E24 double:8.6677992E-315;
        if (r0 == r1) goto L_0x0018;
    L_0x0017:
        goto L_0x0036;
    L_0x0018:
        r0 = "single_button";
        r7 = r7.equals(r0);
        if (r7 == 0) goto L_0x0036;
    L_0x0020:
        r7 = 0;
        goto L_0x0037;
    L_0x0022:
        r0 = "four_sites";
        r7 = r7.equals(r0);
        if (r7 == 0) goto L_0x0036;
    L_0x002a:
        r7 = 1;
        goto L_0x0037;
    L_0x002c:
        r0 = "basic";
        r7 = r7.equals(r0);
        if (r7 == 0) goto L_0x0036;
    L_0x0034:
        r7 = 2;
        goto L_0x0037;
    L_0x0036:
        r7 = -1;
    L_0x0037:
        switch(r7) {
            case 0: goto L_0x003d;
            case 1: goto L_0x003c;
            case 2: goto L_0x003b;
            default: goto L_0x003a;
        };
    L_0x003a:
        return r5;
    L_0x003b:
        return r2;
    L_0x003c:
        return r3;
    L_0x003d:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.rocket.banner.BannerAdapter.getItemViewType(java.lang.String):int");
    }

    public BannerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        switch (i) {
            case 1:
                return new SingleButtonViewHolder(viewGroup, this.onClickListener);
            case 2:
                return new FourSitesViewHolder(viewGroup, this.onClickListener);
            default:
                return new BasicViewHolder(viewGroup, this.onClickListener);
        }
    }

    public void onBindViewHolder(BannerViewHolder bannerViewHolder, int i) {
        bannerViewHolder.onBindViewHolder(this.context, (BannerDAO) this.DAOs.get(i));
    }

    public int getItemCount() {
        return this.DAOs.size();
    }

    public String getFirstDAOId() {
        if (this.DAOs.size() > 0) {
            return ((BannerDAO) this.DAOs.get(0)).f53id;
        }
        LoggerWrapper.throwOrWarn("BannerAdapter", "Invalid banner size");
        return "NO_ID";
    }
}
