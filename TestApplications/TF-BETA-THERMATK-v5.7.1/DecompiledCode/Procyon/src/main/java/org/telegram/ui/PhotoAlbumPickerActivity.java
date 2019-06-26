// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.ui.Cells.PhotoPickerSearchCell;
import android.view.ViewGroup;
import org.telegram.ui.Cells.PhotoPickerAlbumsCell;
import android.content.res.Configuration;
import java.util.Iterator;
import android.view.View$OnClickListener;
import org.telegram.ui.Components.RadialProgressView;
import android.view.View$OnTouchListener;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.ActionBar;
import android.content.Context;
import org.telegram.messenger.MessagesStorage;
import java.util.Collection;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.SendMessagesHelper;
import android.view.MotionEvent;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import android.view.WindowManager;
import android.view.ViewTreeObserver$OnPreDrawListener;
import java.util.HashMap;
import android.widget.FrameLayout;
import org.telegram.ui.Components.PickerBottomLayout;
import org.telegram.ui.Components.RecyclerListView;
import android.widget.TextView;
import org.telegram.messenger.MediaController;
import java.util.ArrayList;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class PhotoAlbumPickerActivity extends BaseFragment implements NotificationCenterDelegate
{
    private ArrayList<MediaController.AlbumEntry> albumsSorted;
    private boolean allowCaption;
    private boolean allowGifs;
    private boolean allowSearchImages;
    private ChatActivity chatActivity;
    private int columnsCount;
    private PhotoAlbumPickerActivityDelegate delegate;
    private TextView emptyView;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private boolean loading;
    private int maxSelectedPhotos;
    private PickerBottomLayout pickerBottomLayout;
    private FrameLayout progressView;
    private ArrayList<MediaController.SearchImage> recentGifImages;
    private HashMap<String, MediaController.SearchImage> recentImagesGifKeys;
    private HashMap<String, MediaController.SearchImage> recentImagesWebKeys;
    private ArrayList<MediaController.SearchImage> recentWebImages;
    private int selectPhotoType;
    private HashMap<Object, Object> selectedPhotos;
    private ArrayList<Object> selectedPhotosOrder;
    private boolean sendPressed;
    
    public PhotoAlbumPickerActivity(final int selectPhotoType, final boolean allowGifs, final boolean allowCaption, final ChatActivity chatActivity) {
        this.selectedPhotos = new HashMap<Object, Object>();
        this.selectedPhotosOrder = new ArrayList<Object>();
        this.albumsSorted = null;
        this.recentImagesWebKeys = new HashMap<String, MediaController.SearchImage>();
        this.recentImagesGifKeys = new HashMap<String, MediaController.SearchImage>();
        this.recentWebImages = new ArrayList<MediaController.SearchImage>();
        this.recentGifImages = new ArrayList<MediaController.SearchImage>();
        this.loading = false;
        this.columnsCount = 2;
        this.allowSearchImages = true;
        this.chatActivity = chatActivity;
        this.selectPhotoType = selectPhotoType;
        this.allowGifs = allowGifs;
        this.allowCaption = allowCaption;
    }
    
    private void fixLayout() {
        final RecyclerListView listView = this.listView;
        if (listView != null) {
            listView.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
                public boolean onPreDraw() {
                    PhotoAlbumPickerActivity.this.fixLayoutInternal();
                    if (PhotoAlbumPickerActivity.this.listView != null) {
                        PhotoAlbumPickerActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
                    }
                    return true;
                }
            });
        }
    }
    
    private void fixLayoutInternal() {
        if (this.getParentActivity() == null) {
            return;
        }
        final int rotation = ((WindowManager)ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
        this.columnsCount = 2;
        if (!AndroidUtilities.isTablet() && (rotation == 3 || rotation == 1)) {
            this.columnsCount = 4;
        }
        this.listAdapter.notifyDataSetChanged();
    }
    
    private void openPhotoPicker(final MediaController.AlbumEntry albumEntry, final int n) {
        ArrayList<MediaController.SearchImage> list = null;
        Label_0031: {
            if (albumEntry == null) {
                if (n == 0) {
                    list = this.recentWebImages;
                    break Label_0031;
                }
                if (n == 1) {
                    list = this.recentGifImages;
                    break Label_0031;
                }
            }
            list = null;
        }
        PhotoPickerActivity photoPickerActivity;
        if (albumEntry != null) {
            photoPickerActivity = new PhotoPickerActivity(n, albumEntry, this.selectedPhotos, this.selectedPhotosOrder, list, this.selectPhotoType, this.allowCaption, this.chatActivity);
            photoPickerActivity.setDelegate((PhotoPickerActivity.PhotoPickerActivityDelegate)new PhotoPickerActivity.PhotoPickerActivityDelegate() {
                @Override
                public void actionButtonPressed(final boolean b) {
                    PhotoAlbumPickerActivity.this.removeSelfFromStack();
                    if (!b) {
                        final PhotoAlbumPickerActivity this$0 = PhotoAlbumPickerActivity.this;
                        this$0.sendSelectedPhotos(this$0.selectedPhotos, PhotoAlbumPickerActivity.this.selectedPhotosOrder);
                    }
                }
                
                @Override
                public void selectedPhotosChanged() {
                    if (PhotoAlbumPickerActivity.this.pickerBottomLayout != null) {
                        PhotoAlbumPickerActivity.this.pickerBottomLayout.updateSelectedCount(PhotoAlbumPickerActivity.this.selectedPhotos.size(), true);
                    }
                }
            });
        }
        else {
            final HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
            final ArrayList<Object> list2 = new ArrayList<Object>();
            photoPickerActivity = new PhotoPickerActivity(n, albumEntry, hashMap, list2, list, this.selectPhotoType, this.allowCaption, this.chatActivity);
            photoPickerActivity.setDelegate((PhotoPickerActivity.PhotoPickerActivityDelegate)new PhotoPickerActivity.PhotoPickerActivityDelegate() {
                @Override
                public void actionButtonPressed(final boolean b) {
                    PhotoAlbumPickerActivity.this.removeSelfFromStack();
                    if (!b) {
                        PhotoAlbumPickerActivity.this.sendSelectedPhotos(hashMap, list2);
                    }
                }
                
                @Override
                public void selectedPhotosChanged() {
                }
            });
        }
        photoPickerActivity.setMaxSelectedPhotos(this.maxSelectedPhotos);
        this.presentFragment(photoPickerActivity);
    }
    
    private void sendSelectedPhotos(final HashMap<Object, Object> hashMap, final ArrayList<Object> list) {
        if (!hashMap.isEmpty() && this.delegate != null) {
            if (!this.sendPressed) {
                this.sendPressed = true;
                final ArrayList<SendMessagesHelper.SendingMediaInfo> list2 = new ArrayList<SendMessagesHelper.SendingMediaInfo>();
                int i = 0;
                int n = 0;
                int n2 = 0;
                while (i < list.size()) {
                    final MediaController.PhotoEntry value = hashMap.get(list.get(i));
                    final SendMessagesHelper.SendingMediaInfo e = new SendMessagesHelper.SendingMediaInfo();
                    list2.add(e);
                    final boolean b = value instanceof MediaController.PhotoEntry;
                    final ArrayList<TLRPC.InputDocument> list3 = null;
                    final ArrayList<TLRPC.InputDocument> list4 = null;
                    int n3;
                    int n4;
                    if (b) {
                        final MediaController.PhotoEntry photoEntry = value;
                        if (photoEntry.isVideo) {
                            e.path = photoEntry.path;
                            e.videoEditedInfo = photoEntry.editedInfo;
                        }
                        else {
                            final String imagePath = photoEntry.imagePath;
                            if (imagePath != null) {
                                e.path = imagePath;
                            }
                            else {
                                final String path = photoEntry.path;
                                if (path != null) {
                                    e.path = path;
                                }
                            }
                        }
                        e.isVideo = photoEntry.isVideo;
                        final CharSequence caption = photoEntry.caption;
                        String string;
                        if (caption != null) {
                            string = caption.toString();
                        }
                        else {
                            string = null;
                        }
                        e.caption = string;
                        e.entities = photoEntry.entities;
                        ArrayList<TLRPC.InputDocument> masks = list4;
                        if (!photoEntry.stickers.isEmpty()) {
                            masks = new ArrayList<TLRPC.InputDocument>(photoEntry.stickers);
                        }
                        e.masks = masks;
                        e.ttl = photoEntry.ttl;
                        n3 = n;
                        n4 = n2;
                    }
                    else {
                        n3 = n;
                        n4 = n2;
                        if (value instanceof MediaController.SearchImage) {
                            final MediaController.SearchImage element = (MediaController.SearchImage)value;
                            final String imagePath2 = element.imagePath;
                            if (imagePath2 != null) {
                                e.path = imagePath2;
                            }
                            else {
                                e.searchImage = element;
                            }
                            final CharSequence caption2 = element.caption;
                            String string2;
                            if (caption2 != null) {
                                string2 = caption2.toString();
                            }
                            else {
                                string2 = null;
                            }
                            e.caption = string2;
                            e.entities = element.entities;
                            ArrayList<TLRPC.InputDocument> masks2 = list3;
                            if (!element.stickers.isEmpty()) {
                                masks2 = new ArrayList<TLRPC.InputDocument>(element.stickers);
                            }
                            e.masks = masks2;
                            e.ttl = element.ttl;
                            element.date = (int)(System.currentTimeMillis() / 1000L);
                            final int type = element.type;
                            if (type == 0) {
                                final MediaController.SearchImage searchImage = this.recentImagesWebKeys.get(element.id);
                                if (searchImage != null) {
                                    this.recentWebImages.remove(searchImage);
                                    this.recentWebImages.add(0, searchImage);
                                }
                                else {
                                    this.recentWebImages.add(0, element);
                                }
                                n3 = 1;
                                n4 = n2;
                            }
                            else {
                                n3 = n;
                                n4 = n2;
                                if (type == 1) {
                                    final MediaController.SearchImage searchImage2 = this.recentImagesGifKeys.get(element.id);
                                    if (searchImage2 != null) {
                                        this.recentGifImages.remove(searchImage2);
                                        this.recentGifImages.add(0, searchImage2);
                                    }
                                    else {
                                        this.recentGifImages.add(0, element);
                                    }
                                    n4 = 1;
                                    n3 = n;
                                }
                            }
                        }
                    }
                    ++i;
                    n = n3;
                    n2 = n4;
                }
                if (n != 0) {
                    MessagesStorage.getInstance(super.currentAccount).putWebRecent(this.recentWebImages);
                }
                if (n2 != 0) {
                    MessagesStorage.getInstance(super.currentAccount).putWebRecent(this.recentGifImages);
                }
                this.delegate.didSelectPhotos(list2);
            }
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackgroundColor(-13421773);
        super.actionBar.setTitleColor(-1);
        super.actionBar.setItemsColor(-1, false);
        super.actionBar.setItemsBackgroundColor(-12763843, false);
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    PhotoAlbumPickerActivity.this.finishFragment();
                }
                else if (n == 1 && PhotoAlbumPickerActivity.this.delegate != null) {
                    PhotoAlbumPickerActivity.this.finishFragment(false);
                    PhotoAlbumPickerActivity.this.delegate.startPhotoSelectActivity();
                }
            }
        });
        super.actionBar.createMenu().addItem(1, 2131165416).setContentDescription((CharSequence)LocaleController.getString("AccDescrMoreOptions", 2131558443));
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        frameLayout.setBackgroundColor(-16777216);
        super.actionBar.setTitle(LocaleController.getString("Gallery", 2131559585));
        (this.listView = new RecyclerListView(context)).setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f));
        this.listView.setClipToPadding(false);
        this.listView.setHorizontalScrollBarEnabled(false);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, false));
        this.listView.setDrawingCacheEnabled(false);
        frameLayout.addView((View)this.listView);
        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.listView.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.bottomMargin = AndroidUtilities.dp(48.0f);
        this.listView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        this.listView.setAdapter(this.listAdapter = new ListAdapter(context));
        this.listView.setGlowColor(-13421773);
        (this.emptyView = new TextView(context)).setTextColor(-8355712);
        this.emptyView.setTextSize(20.0f);
        this.emptyView.setGravity(17);
        this.emptyView.setVisibility(8);
        this.emptyView.setText((CharSequence)LocaleController.getString("NoPhotos", 2131559937));
        frameLayout.addView((View)this.emptyView);
        final FrameLayout$LayoutParams layoutParams2 = (FrameLayout$LayoutParams)this.emptyView.getLayoutParams();
        layoutParams2.width = -1;
        layoutParams2.height = -1;
        layoutParams2.bottomMargin = AndroidUtilities.dp(48.0f);
        this.emptyView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
        this.emptyView.setOnTouchListener((View$OnTouchListener)_$$Lambda$PhotoAlbumPickerActivity$2ZdkXHoPXptp2wpUszGZ5G4bMiQ.INSTANCE);
        (this.progressView = new FrameLayout(context)).setVisibility(8);
        frameLayout.addView((View)this.progressView);
        final FrameLayout$LayoutParams layoutParams3 = (FrameLayout$LayoutParams)this.progressView.getLayoutParams();
        layoutParams3.width = -1;
        layoutParams3.height = -1;
        layoutParams3.bottomMargin = AndroidUtilities.dp(48.0f);
        this.progressView.setLayoutParams((ViewGroup$LayoutParams)layoutParams3);
        this.progressView.addView((View)new RadialProgressView(context));
        final FrameLayout$LayoutParams layoutParams4 = (FrameLayout$LayoutParams)this.progressView.getLayoutParams();
        layoutParams4.width = -2;
        layoutParams4.height = -2;
        layoutParams4.gravity = 17;
        this.progressView.setLayoutParams((ViewGroup$LayoutParams)layoutParams4);
        frameLayout.addView((View)(this.pickerBottomLayout = new PickerBottomLayout(context)));
        final FrameLayout$LayoutParams layoutParams5 = (FrameLayout$LayoutParams)this.pickerBottomLayout.getLayoutParams();
        layoutParams5.width = -1;
        layoutParams5.height = AndroidUtilities.dp(48.0f);
        layoutParams5.gravity = 80;
        this.pickerBottomLayout.setLayoutParams((ViewGroup$LayoutParams)layoutParams5);
        this.pickerBottomLayout.cancelButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoAlbumPickerActivity$jb2cegQfO3FSxtdYFSya79907zs(this));
        this.pickerBottomLayout.doneButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoAlbumPickerActivity$vKBIYwKpMugrhprvLvaDbPzl8M0(this));
        Label_0711: {
            if (this.loading) {
                final ArrayList<MediaController.AlbumEntry> albumsSorted = this.albumsSorted;
                if (albumsSorted == null || (albumsSorted != null && albumsSorted.isEmpty())) {
                    this.progressView.setVisibility(0);
                    this.listView.setEmptyView(null);
                    break Label_0711;
                }
            }
            this.progressView.setVisibility(8);
            this.listView.setEmptyView((View)this.emptyView);
        }
        this.pickerBottomLayout.updateSelectedCount(this.selectedPhotos.size(), true);
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(int n, final int n2, final Object... array) {
        if (n == NotificationCenter.albumsDidLoad) {
            n = (int)array[0];
            if (super.classGuid == n) {
                if (this.selectPhotoType == 0 && this.allowSearchImages) {
                    this.albumsSorted = (ArrayList<MediaController.AlbumEntry>)array[1];
                }
                else {
                    this.albumsSorted = (ArrayList<MediaController.AlbumEntry>)array[2];
                }
                final FrameLayout progressView = this.progressView;
                if (progressView != null) {
                    progressView.setVisibility(8);
                }
                final RecyclerListView listView = this.listView;
                if (listView != null && listView.getEmptyView() == null) {
                    this.listView.setEmptyView((View)this.emptyView);
                }
                final ListAdapter listAdapter = this.listAdapter;
                if (listAdapter != null) {
                    ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
                }
                this.loading = false;
            }
        }
        else if (n == NotificationCenter.closeChats) {
            this.removeSelfFromStack();
        }
        else if (n == NotificationCenter.recentImagesDidLoad) {
            n = (int)array[0];
            if (n == 0) {
                this.recentWebImages = (ArrayList<MediaController.SearchImage>)array[1];
                this.recentImagesWebKeys.clear();
                for (final MediaController.SearchImage value : this.recentWebImages) {
                    this.recentImagesWebKeys.put(value.id, value);
                }
            }
            else if (n == 1) {
                this.recentGifImages = (ArrayList<MediaController.SearchImage>)array[1];
                this.recentImagesGifKeys.clear();
                for (final MediaController.SearchImage value2 : this.recentGifImages) {
                    this.recentImagesGifKeys.put(value2.id, value2);
                }
            }
        }
    }
    
    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.fixLayout();
    }
    
    @Override
    public boolean onFragmentCreate() {
        this.loading = true;
        MediaController.loadGalleryPhotosAlbums(super.classGuid);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.albumsDidLoad);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.recentImagesDidLoad);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.closeChats);
        return super.onFragmentCreate();
    }
    
    @Override
    public void onFragmentDestroy() {
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.albumsDidLoad);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.recentImagesDidLoad);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.closeChats);
        super.onFragmentDestroy();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
        this.fixLayout();
    }
    
    public void setAllowSearchImages(final boolean allowSearchImages) {
        this.allowSearchImages = allowSearchImages;
    }
    
    public void setDelegate(final PhotoAlbumPickerActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setMaxSelectedPhotos(final int maxSelectedPhotos) {
        this.maxSelectedPhotos = maxSelectedPhotos;
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            final boolean access$700 = PhotoAlbumPickerActivity.this.allowSearchImages;
            final int n = 0;
            int n2 = 0;
            if (!access$700) {
                if (PhotoAlbumPickerActivity.this.albumsSorted != null) {
                    n2 = (int)Math.ceil(PhotoAlbumPickerActivity.this.albumsSorted.size() / (float)PhotoAlbumPickerActivity.this.columnsCount);
                }
                return n2;
            }
            int n3 = n;
            if (PhotoAlbumPickerActivity.this.albumsSorted != null) {
                n3 = (int)Math.ceil(PhotoAlbumPickerActivity.this.albumsSorted.size() / (float)PhotoAlbumPickerActivity.this.columnsCount);
            }
            return n3 + 1;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (!PhotoAlbumPickerActivity.this.allowSearchImages) {
                return 0;
            }
            if (n == 0) {
                return 1;
            }
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return true;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            if (viewHolder.getItemViewType() == 0) {
                final PhotoPickerAlbumsCell photoPickerAlbumsCell = (PhotoPickerAlbumsCell)viewHolder.itemView;
                photoPickerAlbumsCell.setAlbumsCount(PhotoAlbumPickerActivity.this.columnsCount);
                for (int i = 0; i < PhotoAlbumPickerActivity.this.columnsCount; ++i) {
                    int n2;
                    if (!PhotoAlbumPickerActivity.this.allowSearchImages) {
                        n2 = PhotoAlbumPickerActivity.this.columnsCount * n;
                    }
                    else {
                        n2 = (n - 1) * PhotoAlbumPickerActivity.this.columnsCount;
                    }
                    final int index = n2 + i;
                    if (index < PhotoAlbumPickerActivity.this.albumsSorted.size()) {
                        photoPickerAlbumsCell.setAlbum(i, (MediaController.AlbumEntry)PhotoAlbumPickerActivity.this.albumsSorted.get(index));
                    }
                    else {
                        photoPickerAlbumsCell.setAlbum(i, null);
                    }
                }
                photoPickerAlbumsCell.requestLayout();
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n != 0) {
                o = new PhotoPickerSearchCell(this.mContext, PhotoAlbumPickerActivity.this.allowGifs);
                ((PhotoPickerSearchCell)o).setDelegate((PhotoPickerSearchCell.PhotoPickerSearchCellDelegate)new _$$Lambda$PhotoAlbumPickerActivity$ListAdapter$q2tmI74nlxknK7EWcU_vq6eNttA(this));
            }
            else {
                o = new PhotoPickerAlbumsCell(this.mContext);
                ((PhotoPickerAlbumsCell)o).setDelegate((PhotoPickerAlbumsCell.PhotoPickerAlbumsCellDelegate)new _$$Lambda$PhotoAlbumPickerActivity$ListAdapter$wak8B6ZyJqrggtYN7y4fwAyLKCg(this));
            }
            return new RecyclerListView.Holder((View)o);
        }
    }
    
    public interface PhotoAlbumPickerActivityDelegate
    {
        void didSelectPhotos(final ArrayList<SendMessagesHelper.SendingMediaInfo> p0);
        
        void startPhotoSelectActivity();
    }
}
