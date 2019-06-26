package org.telegram.ui.Cells;

import android.content.Context;
import android.os.Build.VERSION;
import android.text.TextUtils.TruncateAt;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MediaController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;

public class PhotoPickerAlbumsCell extends FrameLayout {
   private MediaController.AlbumEntry[] albumEntries = new MediaController.AlbumEntry[4];
   private PhotoPickerAlbumsCell.AlbumView[] albumViews = new PhotoPickerAlbumsCell.AlbumView[4];
   private int albumsCount;
   private PhotoPickerAlbumsCell.PhotoPickerAlbumsCellDelegate delegate;

   public PhotoPickerAlbumsCell(Context var1) {
      super(var1);

      for(int var2 = 0; var2 < 4; ++var2) {
         this.albumViews[var2] = new PhotoPickerAlbumsCell.AlbumView(var1);
         this.addView(this.albumViews[var2]);
         this.albumViews[var2].setVisibility(4);
         this.albumViews[var2].setTag(var2);
         this.albumViews[var2].setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
               if (PhotoPickerAlbumsCell.this.delegate != null) {
                  PhotoPickerAlbumsCell.this.delegate.didSelectAlbum(PhotoPickerAlbumsCell.this.albumEntries[(Integer)var1.getTag()]);
               }

            }
         });
      }

   }

   protected void onMeasure(int var1, int var2) {
      if (AndroidUtilities.isTablet()) {
         var2 = (AndroidUtilities.dp(490.0F) - (this.albumsCount + 1) * AndroidUtilities.dp(4.0F)) / this.albumsCount;
      } else {
         var2 = (AndroidUtilities.displaySize.x - (this.albumsCount + 1) * AndroidUtilities.dp(4.0F)) / this.albumsCount;
      }

      for(int var3 = 0; var3 < this.albumsCount; ++var3) {
         LayoutParams var4 = (LayoutParams)this.albumViews[var3].getLayoutParams();
         var4.topMargin = AndroidUtilities.dp(4.0F);
         var4.leftMargin = (AndroidUtilities.dp(4.0F) + var2) * var3;
         var4.width = var2;
         var4.height = var2;
         var4.gravity = 51;
         this.albumViews[var3].setLayoutParams(var4);
      }

      super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(4.0F) + var2, 1073741824));
   }

   public void setAlbum(int var1, MediaController.AlbumEntry var2) {
      this.albumEntries[var1] = var2;
      if (var2 != null) {
         PhotoPickerAlbumsCell.AlbumView var3 = this.albumViews[var1];
         var3.imageView.setOrientation(0, true);
         MediaController.PhotoEntry var4 = var2.coverPhoto;
         if (var4 != null && var4.path != null) {
            var3.imageView.setOrientation(var2.coverPhoto.orientation, true);
            if (var2.coverPhoto.isVideo) {
               BackupImageView var5 = var3.imageView;
               StringBuilder var6 = new StringBuilder();
               var6.append("vthumb://");
               var6.append(var2.coverPhoto.imageId);
               var6.append(":");
               var6.append(var2.coverPhoto.path);
               var5.setImage(var6.toString(), (String)null, this.getContext().getResources().getDrawable(2131165697));
            } else {
               BackupImageView var7 = var3.imageView;
               StringBuilder var8 = new StringBuilder();
               var8.append("thumb://");
               var8.append(var2.coverPhoto.imageId);
               var8.append(":");
               var8.append(var2.coverPhoto.path);
               var7.setImage(var8.toString(), (String)null, this.getContext().getResources().getDrawable(2131165697));
            }
         } else {
            var3.imageView.setImageResource(2131165697);
         }

         var3.nameTextView.setText(var2.bucketName);
         var3.countTextView.setText(String.format("%d", var2.photos.size()));
      } else {
         this.albumViews[var1].setVisibility(4);
      }

   }

   public void setAlbumsCount(int var1) {
      int var2 = 0;

      while(true) {
         PhotoPickerAlbumsCell.AlbumView[] var3 = this.albumViews;
         if (var2 >= var3.length) {
            this.albumsCount = var1;
            return;
         }

         PhotoPickerAlbumsCell.AlbumView var5 = var3[var2];
         byte var4;
         if (var2 < var1) {
            var4 = 0;
         } else {
            var4 = 4;
         }

         var5.setVisibility(var4);
         ++var2;
      }
   }

   public void setDelegate(PhotoPickerAlbumsCell.PhotoPickerAlbumsCellDelegate var1) {
      this.delegate = var1;
   }

   private class AlbumView extends FrameLayout {
      private TextView countTextView;
      private BackupImageView imageView;
      private TextView nameTextView;
      private View selector;

      public AlbumView(Context var2) {
         super(var2);
         this.imageView = new BackupImageView(var2);
         this.addView(this.imageView, LayoutHelper.createFrame(-1, -1.0F));
         LinearLayout var3 = new LinearLayout(var2);
         var3.setOrientation(0);
         var3.setBackgroundColor(2130706432);
         this.addView(var3, LayoutHelper.createFrame(-1, 28, 83));
         this.nameTextView = new TextView(var2);
         this.nameTextView.setTextSize(1, 13.0F);
         this.nameTextView.setTextColor(-1);
         this.nameTextView.setSingleLine(true);
         this.nameTextView.setEllipsize(TruncateAt.END);
         this.nameTextView.setMaxLines(1);
         this.nameTextView.setGravity(16);
         var3.addView(this.nameTextView, LayoutHelper.createLinear(0, -1, 1.0F, 8, 0, 0, 0));
         this.countTextView = new TextView(var2);
         this.countTextView.setTextSize(1, 13.0F);
         this.countTextView.setTextColor(-5592406);
         this.countTextView.setSingleLine(true);
         this.countTextView.setEllipsize(TruncateAt.END);
         this.countTextView.setMaxLines(1);
         this.countTextView.setGravity(16);
         var3.addView(this.countTextView, LayoutHelper.createLinear(-2, -1, 4.0F, 0.0F, 4.0F, 0.0F));
         this.selector = new View(var2);
         this.selector.setBackgroundDrawable(Theme.getSelectorDrawable(false));
         this.addView(this.selector, LayoutHelper.createFrame(-1, -1.0F));
      }

      public boolean onTouchEvent(MotionEvent var1) {
         if (VERSION.SDK_INT >= 21) {
            this.selector.drawableHotspotChanged(var1.getX(), var1.getY());
         }

         return super.onTouchEvent(var1);
      }
   }

   public interface PhotoPickerAlbumsCellDelegate {
      void didSelectAlbum(MediaController.AlbumEntry var1);
   }
}
