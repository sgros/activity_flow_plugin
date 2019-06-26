package org.telegram.p004ui;

import org.telegram.messenger.MediaController.AlbumEntry;
import org.telegram.p004ui.Cells.PhotoPickerAlbumsCell.PhotoPickerAlbumsCellDelegate;
import org.telegram.p004ui.PhotoAlbumPickerActivity.ListAdapter;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PhotoAlbumPickerActivity$ListAdapter$wak8B6ZyJqrggtYN7y4fwAyLKCg */
public final /* synthetic */ class C3811x7e4d1f69 implements PhotoPickerAlbumsCellDelegate {
    private final /* synthetic */ ListAdapter f$0;

    public /* synthetic */ C3811x7e4d1f69(ListAdapter listAdapter) {
        this.f$0 = listAdapter;
    }

    public final void didSelectAlbum(AlbumEntry albumEntry) {
        this.f$0.lambda$onCreateViewHolder$0$PhotoAlbumPickerActivity$ListAdapter(albumEntry);
    }
}
