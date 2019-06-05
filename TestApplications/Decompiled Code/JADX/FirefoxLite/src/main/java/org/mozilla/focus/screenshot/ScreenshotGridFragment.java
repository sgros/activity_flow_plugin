package org.mozilla.focus.screenshot;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.p001v4.content.ContextCompat;
import android.support.p001v4.graphics.drawable.DrawableCompat;
import android.support.p004v7.widget.GridLayoutManager;
import android.support.p004v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.fragment.PanelFragment;
import org.mozilla.focus.fragment.PanelFragmentStatusListener;
import org.mozilla.rocket.C0769R;

public class ScreenshotGridFragment extends PanelFragment implements PanelFragmentStatusListener {
    private ScreenshotItemAdapter mAdapter;
    private ViewGroup mContainerEmptyView;
    private RecyclerView mContainerRecyclerView;

    public static ScreenshotGridFragment newInstance() {
        return new ScreenshotGridFragment();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C0769R.layout.fragment_screenshot_grid, viewGroup, false);
        this.mContainerRecyclerView = (RecyclerView) inflate.findViewById(C0427R.C0426id.screenshot_grid_recycler_view);
        this.mContainerEmptyView = (ViewGroup) inflate.findViewById(C0427R.C0426id.empty_view_container);
        TextView textView = (TextView) inflate.findViewById(C0427R.C0426id.screenshot_grid_empty_text);
        Drawable mutate = getResources().getDrawable(2131230814, null).mutate();
        mutate.setBounds(0, 0, getResources().getDimensionPixelSize(C0769R.dimen.screenshot_empty_img_size), getResources().getDimensionPixelSize(C0769R.dimen.screenshot_empty_img_size));
        DrawableCompat.setTint(mutate, ContextCompat.getColor(getActivity(), C0769R.color.colorDownloadSubText));
        ImageSpan imageSpan = new ImageSpan(mutate);
        String string = getString(C0769R.string.screenshot_grid_empty_text_msg_prefix);
        String string2 = getString(C0769R.string.screenshot_grid_empty_text_msg_postfix);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string);
        stringBuilder.append(string2);
        SpannableString spannableString = new SpannableString(stringBuilder.toString());
        int length = string.length();
        spannableString.setSpan(imageSpan, length, length + 1, 18);
        textView.setText(spannableString);
        return inflate;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        this.mAdapter = new ScreenshotItemAdapter(this.mContainerRecyclerView, getActivity(), this, gridLayoutManager);
        this.mContainerRecyclerView.setLayoutManager(gridLayoutManager);
        this.mContainerRecyclerView.addItemDecoration(new ItemOffsetDecoration(3, getResources().getDimensionPixelSize(C0769R.dimen.screenshot_grid_cell_padding)));
        this.mContainerRecyclerView.setAdapter(this.mAdapter);
    }

    public void onStatus(int i) {
        if (i == 0) {
            this.mContainerRecyclerView.setVisibility(8);
            this.mContainerEmptyView.setVisibility(0);
        } else if (1 == i) {
            this.mContainerRecyclerView.setVisibility(0);
            this.mContainerEmptyView.setVisibility(8);
        } else {
            this.mContainerRecyclerView.setVisibility(8);
            this.mContainerEmptyView.setVisibility(8);
        }
    }

    public void notifyItemDelete(long j) {
        if (this.mAdapter != null) {
            this.mAdapter.onItemDelete(j);
        }
    }

    public void tryLoadMore() {
        this.mAdapter.tryLoadMore();
    }
}
