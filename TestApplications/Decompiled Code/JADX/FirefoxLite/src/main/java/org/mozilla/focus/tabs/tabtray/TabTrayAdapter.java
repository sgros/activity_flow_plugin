package org.mozilla.focus.tabs.tabtray;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.p001v4.content.ContextCompat;
import android.support.p004v7.widget.RecyclerView;
import android.support.p004v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.utils.DimenUtils;
import org.mozilla.icon.FavIconUtils;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.nightmode.themed.ThemedRecyclerView;
import org.mozilla.rocket.nightmode.themed.ThemedRelativeLayout;
import org.mozilla.rocket.nightmode.themed.ThemedTextView;
import org.mozilla.rocket.tabs.Session;

public class TabTrayAdapter extends Adapter<ViewHolder> {
    private Session focusedTab;
    private boolean isNight;
    private HashMap<String, Drawable> localIconCache = new HashMap();
    private RequestManager requestManager;
    private TabClickListener tabClickListener;
    private List<Session> tabs = new ArrayList();

    static class InternalTabClickListener implements OnClickListener {
        private ViewHolder holder;
        private TabClickListener tabClickListener;

        InternalTabClickListener(ViewHolder viewHolder, TabClickListener tabClickListener) {
            this.holder = viewHolder;
            this.tabClickListener = tabClickListener;
        }

        public void onClick(View view) {
            if (this.tabClickListener != null) {
                int adapterPosition = this.holder.getAdapterPosition();
                if (adapterPosition != -1) {
                    dispatchOnClick(view, adapterPosition);
                }
            }
        }

        private void dispatchOnClick(View view, int i) {
            int id = view.getId();
            if (id == C0427R.C0426id.close_button) {
                this.tabClickListener.onTabCloseClick(i);
            } else if (id == C0427R.C0426id.root_view) {
                this.tabClickListener.onTabClick(i);
            }
        }
    }

    public interface TabClickListener {
        void onTabClick(int i);

        void onTabCloseClick(int i);
    }

    static class ViewHolder extends android.support.p004v7.widget.RecyclerView.ViewHolder {
        View closeButton;
        ThemedRelativeLayout rootView;
        ImageView websiteIcon;
        ThemedTextView websiteSubtitle;
        ThemedTextView websiteTitle;

        ViewHolder(View view) {
            super(view);
            this.rootView = (ThemedRelativeLayout) view.findViewById(C0427R.C0426id.root_view);
            this.websiteTitle = (ThemedTextView) view.findViewById(C0427R.C0426id.website_title);
            this.websiteSubtitle = (ThemedTextView) view.findViewById(C0427R.C0426id.website_subtitle);
            this.closeButton = view.findViewById(C0427R.C0426id.close_button);
            this.websiteIcon = (ImageView) view.findViewById(C0427R.C0426id.website_icon);
        }
    }

    TabTrayAdapter(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0769R.layout.item_tab_tray, viewGroup, false));
        InternalTabClickListener internalTabClickListener = new InternalTabClickListener(viewHolder, this.tabClickListener);
        viewHolder.itemView.setOnClickListener(internalTabClickListener);
        viewHolder.closeButton.setOnClickListener(internalTabClickListener);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemView.setSelected(this.tabs.get(i) == this.focusedTab);
        Resources resources = viewHolder.itemView.getResources();
        Session session = (Session) this.tabs.get(i);
        CharSequence title = getTitle(session, viewHolder);
        ThemedTextView themedTextView = viewHolder.websiteTitle;
        if (TextUtils.isEmpty(title)) {
            title = resources.getString(C0769R.string.app_name);
        }
        themedTextView.setText(title);
        if (!TextUtils.isEmpty(session.getUrl())) {
            viewHolder.websiteSubtitle.setText(session.getUrl());
        }
        setFavicon(session, viewHolder);
        viewHolder.rootView.setNightMode(this.isNight);
        viewHolder.websiteTitle.setNightMode(this.isNight);
        viewHolder.websiteSubtitle.setNightMode(this.isNight);
    }

    public void onViewRecycled(ViewHolder viewHolder) {
        viewHolder.websiteTitle.setText("");
        viewHolder.websiteSubtitle.setText("");
        updateFavicon(viewHolder, null);
    }

    public int getItemCount() {
        return this.tabs.size();
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (recyclerView instanceof ThemedRecyclerView) {
            this.isNight = ((ThemedRecyclerView) recyclerView).isNightMode();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setTabClickListener(TabClickListener tabClickListener) {
        this.tabClickListener = tabClickListener;
    }

    /* Access modifiers changed, original: 0000 */
    public void setData(List<Session> list) {
        this.tabs.clear();
        this.tabs.addAll(list);
    }

    /* Access modifiers changed, original: 0000 */
    public List<Session> getData() {
        return this.tabs;
    }

    /* Access modifiers changed, original: 0000 */
    public void setFocusedTab(Session session) {
        this.focusedTab = session;
    }

    /* Access modifiers changed, original: 0000 */
    public Session getFocusedTab() {
        return this.focusedTab;
    }

    private String getTitle(Session session, ViewHolder viewHolder) {
        String title = session.getTitle();
        String valueOf = String.valueOf(viewHolder.websiteTitle.getText());
        if (!TextUtils.isEmpty(title)) {
            return title;
        }
        if (TextUtils.isEmpty(valueOf)) {
            valueOf = "";
        }
        return valueOf;
    }

    private void setFavicon(Session session, ViewHolder viewHolder) {
        if (!TextUtils.isEmpty(session.getUrl())) {
            loadCachedFavicon(session, viewHolder);
        }
    }

    private void loadCachedFavicon(final Session session, final ViewHolder viewHolder) {
        RequestOptions dontAnimate = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).dontAnimate();
        Bitmap favicon = session.getFavicon();
        this.requestManager.load(new FaviconModel(session.getUrl(), DimenUtils.getFavIconType(viewHolder.itemView.getResources(), favicon), favicon)).apply(dontAnimate).listener(new RequestListener<Drawable>() {
            public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                return false;
            }

            public boolean onLoadFailed(GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                TabTrayAdapter.this.loadGeneratedFavicon(session, viewHolder);
                return true;
            }
        }).into(new SimpleTarget<Drawable>() {
            public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                TabTrayAdapter.this.updateFavicon(viewHolder, drawable);
            }
        });
    }

    private void loadGeneratedFavicon(Session session, ViewHolder viewHolder) {
        int i;
        Character valueOf = Character.valueOf(FavIconUtils.getRepresentativeCharacter(session.getUrl()));
        Bitmap favicon = session.getFavicon();
        if (favicon == null) {
            i = -1;
        } else {
            i = FavIconUtils.getDominantColor(favicon);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(valueOf.toString());
        stringBuilder.append("_");
        stringBuilder.append(Integer.toHexString(i));
        String stringBuilder2 = stringBuilder.toString();
        if (this.localIconCache.containsKey(stringBuilder2)) {
            updateFavicon(viewHolder, (Drawable) this.localIconCache.get(stringBuilder2));
            return;
        }
        BitmapDrawable bitmapDrawable = new BitmapDrawable(viewHolder.itemView.getResources(), DimenUtils.getInitialBitmap(viewHolder.itemView.getResources(), valueOf.charValue(), i));
        this.localIconCache.put(stringBuilder2, bitmapDrawable);
        updateFavicon(viewHolder, bitmapDrawable);
    }

    private void updateFavicon(ViewHolder viewHolder, Drawable drawable) {
        if (drawable != null) {
            viewHolder.websiteIcon.setImageDrawable(drawable);
            viewHolder.websiteIcon.setBackgroundColor(0);
            return;
        }
        viewHolder.websiteIcon.setImageResource(2131230858);
        viewHolder.websiteIcon.setBackgroundColor(ContextCompat.getColor(viewHolder.websiteIcon.getContext(), C0769R.color.tabTrayItemIconBackground));
    }
}
