package org.mozilla.focus.firstrun;

import android.content.Context;
import android.content.res.Resources;
import android.support.p004v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import org.mozilla.rocket.C0769R;

public class FirstrunCardView extends CardView {
    private int maxHeight;
    private int maxWidth;

    public FirstrunCardView(Context context) {
        super(context);
        init();
    }

    public FirstrunCardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public FirstrunCardView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        Resources resources = getResources();
        this.maxWidth = resources.getDimensionPixelSize(C0769R.dimen.firstrun_card_width);
        this.maxHeight = resources.getDimensionPixelSize(C0769R.dimen.firstrun_card_height);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        i = MeasureSpec.getSize(i);
        i2 = MeasureSpec.getSize(i2);
        super.onMeasure(MeasureSpec.makeMeasureSpec(Math.min(i, this.maxWidth), 1073741824), MeasureSpec.makeMeasureSpec(Math.min(i2, this.maxHeight), 1073741824));
    }
}
