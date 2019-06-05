// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.banner;

import android.view.ViewGroup;
import org.json.JSONException;
import org.mozilla.rocket.util.LoggerWrapper;
import java.util.Locale;
import org.json.JSONObject;
import java.util.ArrayList;
import android.content.Context;
import java.util.List;
import android.support.v7.widget.RecyclerView;

public class BannerAdapter extends Adapter<BannerViewHolder>
{
    private List<BannerDAO> DAOs;
    private Context context;
    private OnClickListener onClickListener;
    
    public BannerAdapter(final String[] array, final OnClickListener onClickListener) throws JSONException {
        this.DAOs = new ArrayList<BannerDAO>();
        this.onClickListener = onClickListener;
        for (int i = 0; i < array.length; ++i) {
            final JSONObject jsonObject = new JSONObject(array[i]);
            final BannerDAO bannerDAO = new BannerDAO();
            bannerDAO.type = jsonObject.getString("type");
            bannerDAO.values = jsonObject.getJSONArray("values");
            bannerDAO.id = jsonObject.getString("id");
            if (this.getItemViewType(bannerDAO.type) == -1) {
                LoggerWrapper.throwOrWarn("BannerAdapter", String.format(Locale.US, "Unknown view type: %s in page %d", bannerDAO.type, i));
            }
            else {
                this.DAOs.add(bannerDAO);
            }
        }
    }
    
    private int getItemViewType(final String s) {
        final int hashCode = s.hashCode();
        int n = 0;
        Label_0070: {
            if (hashCode != 93508654) {
                if (hashCode != 1342277043) {
                    if (hashCode == 1754382089) {
                        if (s.equals("single_button")) {
                            n = 0;
                            break Label_0070;
                        }
                    }
                }
                else if (s.equals("four_sites")) {
                    n = 1;
                    break Label_0070;
                }
            }
            else if (s.equals("basic")) {
                n = 2;
                break Label_0070;
            }
            n = -1;
        }
        switch (n) {
            default: {
                return -1;
            }
            case 2: {
                return 0;
            }
            case 1: {
                return 2;
            }
            case 0: {
                return 1;
            }
        }
    }
    
    public String getFirstDAOId() {
        if (this.DAOs.size() <= 0) {
            LoggerWrapper.throwOrWarn("BannerAdapter", "Invalid banner size");
            return "NO_ID";
        }
        return this.DAOs.get(0).id;
    }
    
    @Override
    public int getItemCount() {
        return this.DAOs.size();
    }
    
    @Override
    public int getItemViewType(final int n) {
        return this.getItemViewType(this.DAOs.get(n).type);
    }
    
    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        this.context = recyclerView.getContext();
    }
    
    public void onBindViewHolder(final BannerViewHolder bannerViewHolder, final int n) {
        bannerViewHolder.onBindViewHolder(this.context, this.DAOs.get(n));
    }
    
    public BannerViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        switch (n) {
            default: {
                return new BasicViewHolder(viewGroup, this.onClickListener);
            }
            case 2: {
                return new FourSitesViewHolder(viewGroup, this.onClickListener);
            }
            case 1: {
                return new SingleButtonViewHolder(viewGroup, this.onClickListener);
            }
        }
    }
    
    @Override
    public void onDetachedFromRecyclerView(final RecyclerView recyclerView) {
        this.context = null;
    }
}
