package org.telegram.ui;

import org.telegram.messenger.MediaController;
import org.telegram.ui.Cells.PhotoPickerAlbumsCell;

// $FF: synthetic class
public final class _$$Lambda$PhotoAlbumPickerActivity$ListAdapter$wak8B6ZyJqrggtYN7y4fwAyLKCg implements PhotoPickerAlbumsCell.PhotoPickerAlbumsCellDelegate {
   // $FF: synthetic field
   private final PhotoAlbumPickerActivity.ListAdapter f$0;

   // $FF: synthetic method
   public _$$Lambda$PhotoAlbumPickerActivity$ListAdapter$wak8B6ZyJqrggtYN7y4fwAyLKCg(PhotoAlbumPickerActivity.ListAdapter var1) {
      this.f$0 = var1;
   }

   public final void didSelectAlbum(MediaController.AlbumEntry var1) {
      this.f$0.lambda$onCreateViewHolder$0$PhotoAlbumPickerActivity$ListAdapter(var1);
   }
}
