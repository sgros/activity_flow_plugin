// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.screenshot;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.content.res.Resources$Theme;
import android.widget.TextView;
import android.view.View;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import org.mozilla.focus.fragment.PanelFragmentStatusListener;
import org.mozilla.focus.fragment.PanelFragment;

public class ScreenshotGridFragment extends PanelFragment implements PanelFragmentStatusListener
{
    private ScreenshotItemAdapter mAdapter;
    private ViewGroup mContainerEmptyView;
    private RecyclerView mContainerRecyclerView;
    
    public static ScreenshotGridFragment newInstance() {
        return new ScreenshotGridFragment();
    }
    
    public void notifyItemDelete(final long n) {
        if (this.mAdapter != null) {
            this.mAdapter.onItemDelete(n);
        }
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2131492973, viewGroup, false);
        this.mContainerRecyclerView = (RecyclerView)inflate.findViewById(2131296605);
        this.mContainerEmptyView = (ViewGroup)inflate.findViewById(2131296422);
        final TextView textView = (TextView)inflate.findViewById(2131296603);
        final Drawable mutate = this.getResources().getDrawable(2131230814, (Resources$Theme)null).mutate();
        mutate.setBounds(0, 0, this.getResources().getDimensionPixelSize(2131165437), this.getResources().getDimensionPixelSize(2131165437));
        DrawableCompat.setTint(mutate, ContextCompat.getColor((Context)this.getActivity(), 2131099709));
        final ImageSpan imageSpan = new ImageSpan(mutate);
        final String string = this.getString(2131755378);
        final String string2 = this.getString(2131755377);
        final StringBuilder sb = new StringBuilder();
        sb.append(string);
        sb.append(string2);
        final SpannableString text = new SpannableString((CharSequence)sb.toString());
        final int length = string.length();
        text.setSpan((Object)imageSpan, length, length + 1, 18);
        textView.setText((CharSequence)text);
        return inflate;
    }
    
    @Override
    public void onStatus(final int n) {
        if (n == 0) {
            this.mContainerRecyclerView.setVisibility(8);
            this.mContainerEmptyView.setVisibility(0);
        }
        else if (1 == n) {
            this.mContainerRecyclerView.setVisibility(0);
            this.mContainerEmptyView.setVisibility(8);
        }
        else {
            this.mContainerRecyclerView.setVisibility(8);
            this.mContainerEmptyView.setVisibility(8);
        }
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        final GridLayoutManager layoutManager = new GridLayoutManager((Context)this.getActivity(), 3);
        this.mAdapter = new ScreenshotItemAdapter(this.mContainerRecyclerView, this.getActivity(), this, layoutManager);
        this.mContainerRecyclerView.setLayoutManager((RecyclerView.LayoutManager)layoutManager);
        this.mContainerRecyclerView.addItemDecoration((RecyclerView.ItemDecoration)new ItemOffsetDecoration(3, this.getResources().getDimensionPixelSize(2131165438)));
        this.mContainerRecyclerView.setAdapter((RecyclerView.Adapter)this.mAdapter);
    }
    
    @Override
    public void tryLoadMore() {
        this.mAdapter.tryLoadMore();
    }
}
