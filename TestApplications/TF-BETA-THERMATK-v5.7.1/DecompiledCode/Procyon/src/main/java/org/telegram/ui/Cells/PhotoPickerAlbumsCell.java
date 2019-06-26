// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.os.Build$VERSION;
import android.view.MotionEvent;
import org.telegram.ui.ActionBar.Theme;
import android.text.TextUtils$TruncateAt;
import android.widget.LinearLayout;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.TextView;
import org.telegram.ui.Components.BackupImageView;
import android.view.View$MeasureSpec;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import android.view.View$OnClickListener;
import android.view.View;
import android.content.Context;
import org.telegram.messenger.MediaController;
import android.widget.FrameLayout;

public class PhotoPickerAlbumsCell extends FrameLayout
{
    private MediaController.AlbumEntry[] albumEntries;
    private AlbumView[] albumViews;
    private int albumsCount;
    private PhotoPickerAlbumsCellDelegate delegate;
    
    public PhotoPickerAlbumsCell(final Context context) {
        super(context);
        this.albumEntries = new MediaController.AlbumEntry[4];
        this.albumViews = new AlbumView[4];
        for (int i = 0; i < 4; ++i) {
            this.addView((View)(this.albumViews[i] = new AlbumView(context)));
            this.albumViews[i].setVisibility(4);
            this.albumViews[i].setTag((Object)i);
            this.albumViews[i].setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    if (PhotoPickerAlbumsCell.this.delegate != null) {
                        PhotoPickerAlbumsCell.this.delegate.didSelectAlbum(PhotoPickerAlbumsCell.this.albumEntries[(int)view.getTag()]);
                    }
                }
            });
        }
    }
    
    protected void onMeasure(final int n, int n2) {
        if (AndroidUtilities.isTablet()) {
            n2 = (AndroidUtilities.dp(490.0f) - (this.albumsCount + 1) * AndroidUtilities.dp(4.0f)) / this.albumsCount;
        }
        else {
            n2 = (AndroidUtilities.displaySize.x - (this.albumsCount + 1) * AndroidUtilities.dp(4.0f)) / this.albumsCount;
        }
        for (int i = 0; i < this.albumsCount; ++i) {
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.albumViews[i].getLayoutParams();
            layoutParams.topMargin = AndroidUtilities.dp(4.0f);
            layoutParams.leftMargin = (AndroidUtilities.dp(4.0f) + n2) * i;
            layoutParams.width = n2;
            layoutParams.height = n2;
            layoutParams.gravity = 51;
            this.albumViews[i].setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        }
        super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(4.0f) + n2, 1073741824));
    }
    
    public void setAlbum(final int n, final MediaController.AlbumEntry albumEntry) {
        this.albumEntries[n] = albumEntry;
        if (albumEntry != null) {
            final AlbumView albumView = this.albumViews[n];
            albumView.imageView.setOrientation(0, true);
            final MediaController.PhotoEntry coverPhoto = albumEntry.coverPhoto;
            if (coverPhoto != null && coverPhoto.path != null) {
                albumView.imageView.setOrientation(albumEntry.coverPhoto.orientation, true);
                if (albumEntry.coverPhoto.isVideo) {
                    final BackupImageView access$200 = albumView.imageView;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("vthumb://");
                    sb.append(albumEntry.coverPhoto.imageId);
                    sb.append(":");
                    sb.append(albumEntry.coverPhoto.path);
                    access$200.setImage(sb.toString(), null, this.getContext().getResources().getDrawable(2131165697));
                }
                else {
                    final BackupImageView access$201 = albumView.imageView;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("thumb://");
                    sb2.append(albumEntry.coverPhoto.imageId);
                    sb2.append(":");
                    sb2.append(albumEntry.coverPhoto.path);
                    access$201.setImage(sb2.toString(), null, this.getContext().getResources().getDrawable(2131165697));
                }
            }
            else {
                albumView.imageView.setImageResource(2131165697);
            }
            albumView.nameTextView.setText((CharSequence)albumEntry.bucketName);
            albumView.countTextView.setText((CharSequence)String.format("%d", albumEntry.photos.size()));
        }
        else {
            this.albumViews[n].setVisibility(4);
        }
    }
    
    public void setAlbumsCount(final int albumsCount) {
        int n = 0;
        while (true) {
            final AlbumView[] albumViews = this.albumViews;
            if (n >= albumViews.length) {
                break;
            }
            final AlbumView albumView = albumViews[n];
            int visibility;
            if (n < albumsCount) {
                visibility = 0;
            }
            else {
                visibility = 4;
            }
            albumView.setVisibility(visibility);
            ++n;
        }
        this.albumsCount = albumsCount;
    }
    
    public void setDelegate(final PhotoPickerAlbumsCellDelegate delegate) {
        this.delegate = delegate;
    }
    
    private class AlbumView extends FrameLayout
    {
        private TextView countTextView;
        private BackupImageView imageView;
        private TextView nameTextView;
        private View selector;
        
        public AlbumView(final Context context) {
            super(context);
            this.addView((View)(this.imageView = new BackupImageView(context)), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            final LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(0);
            linearLayout.setBackgroundColor(2130706432);
            this.addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 28, 83));
            (this.nameTextView = new TextView(context)).setTextSize(1, 13.0f);
            this.nameTextView.setTextColor(-1);
            this.nameTextView.setSingleLine(true);
            this.nameTextView.setEllipsize(TextUtils$TruncateAt.END);
            this.nameTextView.setMaxLines(1);
            this.nameTextView.setGravity(16);
            linearLayout.addView((View)this.nameTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(0, -1, 1.0f, 8, 0, 0, 0));
            (this.countTextView = new TextView(context)).setTextSize(1, 13.0f);
            this.countTextView.setTextColor(-5592406);
            this.countTextView.setSingleLine(true);
            this.countTextView.setEllipsize(TextUtils$TruncateAt.END);
            this.countTextView.setMaxLines(1);
            this.countTextView.setGravity(16);
            linearLayout.addView((View)this.countTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -1, 4.0f, 0.0f, 4.0f, 0.0f));
            (this.selector = new View(context)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.addView(this.selector, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            if (Build$VERSION.SDK_INT >= 21) {
                this.selector.drawableHotspotChanged(motionEvent.getX(), motionEvent.getY());
            }
            return super.onTouchEvent(motionEvent);
        }
    }
    
    public interface PhotoPickerAlbumsCellDelegate
    {
        void didSelectAlbum(final MediaController.AlbumEntry p0);
    }
}
