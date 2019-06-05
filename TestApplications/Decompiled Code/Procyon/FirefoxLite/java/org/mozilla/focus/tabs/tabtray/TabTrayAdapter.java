// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.tabs.tabtray;

import android.widget.ImageView;
import org.mozilla.rocket.nightmode.themed.ThemedRelativeLayout;
import android.view.View;
import java.util.Collection;
import android.view.View$OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import org.mozilla.rocket.nightmode.themed.ThemedTextView;
import android.content.res.Resources;
import org.mozilla.rocket.nightmode.themed.ThemedRecyclerView;
import android.support.v4.content.ContextCompat;
import android.graphics.drawable.BitmapDrawable;
import org.mozilla.icon.FavIconUtils;
import android.graphics.Bitmap;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import org.mozilla.focus.utils.DimenUtils;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.RequestManager;
import android.graphics.drawable.Drawable;
import java.util.HashMap;
import org.mozilla.rocket.tabs.Session;
import android.support.v7.widget.RecyclerView;

public class TabTrayAdapter extends Adapter<ViewHolder>
{
    private Session focusedTab;
    private boolean isNight;
    private HashMap<String, Drawable> localIconCache;
    private RequestManager requestManager;
    private TabClickListener tabClickListener;
    private List<Session> tabs;
    
    TabTrayAdapter(final RequestManager requestManager) {
        this.tabs = new ArrayList<Session>();
        this.localIconCache = new HashMap<String, Drawable>();
        this.requestManager = requestManager;
    }
    
    private String getTitle(final Session session, final ViewHolder viewHolder) {
        final String title = session.getTitle();
        final String value = String.valueOf(viewHolder.websiteTitle.getText());
        if (TextUtils.isEmpty((CharSequence)title)) {
            String s = value;
            if (TextUtils.isEmpty((CharSequence)value)) {
                s = "";
            }
            return s;
        }
        return title;
    }
    
    private void loadCachedFavicon(final Session session, final ViewHolder viewHolder) {
        final RequestOptions dontAnimate = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).dontAnimate();
        final Bitmap favicon = session.getFavicon();
        this.requestManager.load(new FaviconModel(session.getUrl(), DimenUtils.getFavIconType(viewHolder.itemView.getResources(), favicon), favicon)).apply(dontAnimate).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(final GlideException ex, final Object o, final Target<Drawable> target, final boolean b) {
                TabTrayAdapter.this.loadGeneratedFavicon(session, viewHolder);
                return true;
            }
            
            @Override
            public boolean onResourceReady(final Drawable drawable, final Object o, final Target<Drawable> target, final DataSource dataSource, final boolean b) {
                return false;
            }
        }).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(final Drawable drawable, final Transition<? super Drawable> transition) {
                TabTrayAdapter.this.updateFavicon(viewHolder, drawable);
            }
        });
    }
    
    private void loadGeneratedFavicon(final Session session, final ViewHolder viewHolder) {
        final Character value = FavIconUtils.getRepresentativeCharacter(session.getUrl());
        final Bitmap favicon = session.getFavicon();
        int dominantColor;
        if (favicon == null) {
            dominantColor = -1;
        }
        else {
            dominantColor = FavIconUtils.getDominantColor(favicon);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(value.toString());
        sb.append("_");
        sb.append(Integer.toHexString(dominantColor));
        final String string = sb.toString();
        if (this.localIconCache.containsKey(string)) {
            this.updateFavicon(viewHolder, this.localIconCache.get(string));
        }
        else {
            final BitmapDrawable value2 = new BitmapDrawable(viewHolder.itemView.getResources(), DimenUtils.getInitialBitmap(viewHolder.itemView.getResources(), value, dominantColor));
            this.localIconCache.put(string, (Drawable)value2);
            this.updateFavicon(viewHolder, (Drawable)value2);
        }
    }
    
    private void setFavicon(final Session session, final ViewHolder viewHolder) {
        if (TextUtils.isEmpty((CharSequence)session.getUrl())) {
            return;
        }
        this.loadCachedFavicon(session, viewHolder);
    }
    
    private void updateFavicon(final ViewHolder viewHolder, final Drawable imageDrawable) {
        if (imageDrawable != null) {
            viewHolder.websiteIcon.setImageDrawable(imageDrawable);
            viewHolder.websiteIcon.setBackgroundColor(0);
        }
        else {
            viewHolder.websiteIcon.setImageResource(2131230858);
            viewHolder.websiteIcon.setBackgroundColor(ContextCompat.getColor(viewHolder.websiteIcon.getContext(), 2131099889));
        }
    }
    
    List<Session> getData() {
        return this.tabs;
    }
    
    Session getFocusedTab() {
        return this.focusedTab;
    }
    
    @Override
    public int getItemCount() {
        return this.tabs.size();
    }
    
    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (recyclerView instanceof ThemedRecyclerView) {
            this.isNight = ((ThemedRecyclerView)recyclerView).isNightMode();
        }
    }
    
    public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
        viewHolder.itemView.setSelected(this.tabs.get(n) == this.focusedTab);
        final Resources resources = viewHolder.itemView.getResources();
        final Session session = this.tabs.get(n);
        final String title = this.getTitle(session, viewHolder);
        final ThemedTextView websiteTitle = viewHolder.websiteTitle;
        String string = title;
        if (TextUtils.isEmpty((CharSequence)title)) {
            string = resources.getString(2131755062);
        }
        websiteTitle.setText((CharSequence)string);
        if (!TextUtils.isEmpty((CharSequence)session.getUrl())) {
            viewHolder.websiteSubtitle.setText((CharSequence)session.getUrl());
        }
        this.setFavicon(session, viewHolder);
        viewHolder.rootView.setNightMode(this.isNight);
        viewHolder.websiteTitle.setNightMode(this.isNight);
        viewHolder.websiteSubtitle.setNightMode(this.isNight);
    }
    
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        final ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2131492986, viewGroup, false));
        final InternalTabClickListener internalTabClickListener = new InternalTabClickListener(viewHolder, this.tabClickListener);
        viewHolder.itemView.setOnClickListener((View$OnClickListener)internalTabClickListener);
        viewHolder.closeButton.setOnClickListener((View$OnClickListener)internalTabClickListener);
        return viewHolder;
    }
    
    public void onViewRecycled(final ViewHolder viewHolder) {
        viewHolder.websiteTitle.setText((CharSequence)"");
        viewHolder.websiteSubtitle.setText((CharSequence)"");
        this.updateFavicon(viewHolder, null);
    }
    
    void setData(final List<Session> list) {
        this.tabs.clear();
        this.tabs.addAll(list);
    }
    
    void setFocusedTab(final Session focusedTab) {
        this.focusedTab = focusedTab;
    }
    
    void setTabClickListener(final TabClickListener tabClickListener) {
        this.tabClickListener = tabClickListener;
    }
    
    static class InternalTabClickListener implements View$OnClickListener
    {
        private ViewHolder holder;
        private TabClickListener tabClickListener;
        
        InternalTabClickListener(final ViewHolder holder, final TabClickListener tabClickListener) {
            this.holder = holder;
            this.tabClickListener = tabClickListener;
        }
        
        private void dispatchOnClick(final View view, final int n) {
            final int id = view.getId();
            if (id != 2131296372) {
                if (id == 2131296596) {
                    this.tabClickListener.onTabClick(n);
                }
            }
            else {
                this.tabClickListener.onTabCloseClick(n);
            }
        }
        
        public void onClick(final View view) {
            if (this.tabClickListener == null) {
                return;
            }
            final int adapterPosition = ((RecyclerView.ViewHolder)this.holder).getAdapterPosition();
            if (adapterPosition != -1) {
                this.dispatchOnClick(view, adapterPosition);
            }
        }
    }
    
    public interface TabClickListener
    {
        void onTabClick(final int p0);
        
        void onTabCloseClick(final int p0);
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View closeButton;
        ThemedRelativeLayout rootView;
        ImageView websiteIcon;
        ThemedTextView websiteSubtitle;
        ThemedTextView websiteTitle;
        
        ViewHolder(final View view) {
            super(view);
            this.rootView = (ThemedRelativeLayout)view.findViewById(2131296596);
            this.websiteTitle = (ThemedTextView)view.findViewById(2131296722);
            this.websiteSubtitle = (ThemedTextView)view.findViewById(2131296721);
            this.closeButton = view.findViewById(2131296372);
            this.websiteIcon = (ImageView)view.findViewById(2131296720);
        }
    }
}
