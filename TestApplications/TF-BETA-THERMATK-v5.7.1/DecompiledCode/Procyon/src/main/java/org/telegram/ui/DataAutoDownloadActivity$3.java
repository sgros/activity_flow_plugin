// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import java.util.Collection;
import android.animation.Animator;
import java.util.ArrayList;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextCheckCell;
import android.animation.AnimatorSet;
import org.telegram.ui.Cells.MaxFileSizeCell;

class DataAutoDownloadActivity$3 extends MaxFileSizeCell
{
    final /* synthetic */ DataAutoDownloadActivity this$0;
    final /* synthetic */ AnimatorSet[] val$animatorSet;
    final /* synthetic */ TextCheckCell[] val$checkCell;
    final /* synthetic */ TextInfoPrivacyCell val$infoCell;
    final /* synthetic */ int val$position;
    
    DataAutoDownloadActivity$3(final DataAutoDownloadActivity this$0, final Context context, final int val$position, final TextInfoPrivacyCell val$infoCell, final TextCheckCell[] val$checkCell, final AnimatorSet[] val$animatorSet) {
        this.this$0 = this$0;
        this.val$position = val$position;
        this.val$infoCell = val$infoCell;
        this.val$checkCell = val$checkCell;
        this.val$animatorSet = val$animatorSet;
        super(context);
    }
    
    @Override
    protected void didChangedSizeValue(final int n) {
        if (this.val$position == this.this$0.videosRow) {
            final TextInfoPrivacyCell val$infoCell = this.val$infoCell;
            boolean b = true;
            val$infoCell.setText(LocaleController.formatString("AutoDownloadPreloadVideoInfo", 2131558772, AndroidUtilities.formatFileSize(n)));
            if (n <= 2097152) {
                b = false;
            }
            if (b != this.val$checkCell[0].isEnabled()) {
                final ArrayList<Animator> list = new ArrayList<Animator>();
                this.val$checkCell[0].setEnabled(b, list);
                final AnimatorSet[] val$animatorSet = this.val$animatorSet;
                if (val$animatorSet[0] != null) {
                    val$animatorSet[0].cancel();
                    this.val$animatorSet[0] = null;
                }
                (this.val$animatorSet[0] = new AnimatorSet()).playTogether((Collection)list);
                this.val$animatorSet[0].addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        if (animator.equals(DataAutoDownloadActivity$3.this.val$animatorSet[0])) {
                            DataAutoDownloadActivity$3.this.val$animatorSet[0] = null;
                        }
                    }
                });
                this.val$animatorSet[0].setDuration(150L);
                this.val$animatorSet[0].start();
            }
        }
    }
}
