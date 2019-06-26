// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.ui.Components.RadialProgressView;
import android.view.ViewGroup;
import org.telegram.ui.Components.CheckBox;
import android.text.TextUtils;
import android.content.res.Configuration;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.MessagesStorage;
import android.content.DialogInterface;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import android.widget.ImageView$ScaleType;
import android.view.View$OnClickListener;
import android.graphics.Rect;
import android.view.animation.LayoutAnimationController;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.LocaleController;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.ApplicationLoader;
import android.view.WindowManager;
import android.view.ViewTreeObserver$OnPreDrawListener;
import android.widget.EditText;
import org.telegram.ui.Components.BackupImageView;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.messenger.SharedConfig;
import java.io.Serializable;
import android.view.View;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.ImageReceiver;
import org.telegram.ui.Cells.PhotoPickerPhotoCell;
import org.telegram.messenger.AndroidUtilities;
import android.os.Build$VERSION;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.MessageObject;
import java.util.HashMap;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.messenger.MediaController;
import java.util.ArrayList;
import org.telegram.ui.Components.PickerBottomLayout;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.GridLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.animation.AnimatorSet;
import android.widget.FrameLayout;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class PhotoPickerActivity extends BaseFragment implements NotificationCenterDelegate
{
    private boolean allowCaption;
    private boolean allowIndices;
    private ChatActivity chatActivity;
    private PhotoPickerActivityDelegate delegate;
    private EmptyTextProgressView emptyView;
    private FrameLayout frameLayout;
    private AnimatorSet hintAnimation;
    private Runnable hintHideRunnable;
    private TextView hintTextView;
    private ImageView imageOrderToggleButton;
    private int imageReqId;
    private boolean imageSearchEndReached;
    private String initialSearchString;
    private int itemWidth;
    private String lastSearchImageString;
    private String lastSearchString;
    private int lastSearchToken;
    private GridLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private boolean loadingRecent;
    private int maxSelectedPhotos;
    private String nextImagesSearchOffset;
    private PickerBottomLayout pickerBottomLayout;
    private PhotoViewer.PhotoViewerProvider provider;
    private ArrayList<MediaController.SearchImage> recentImages;
    private ActionBarMenuItem searchItem;
    private ArrayList<MediaController.SearchImage> searchResult;
    private HashMap<String, MediaController.SearchImage> searchResultKeys;
    private HashMap<String, MediaController.SearchImage> searchResultUrls;
    private boolean searching;
    private boolean searchingUser;
    private int selectPhotoType;
    private MediaController.AlbumEntry selectedAlbum;
    private HashMap<Object, Object> selectedPhotos;
    private ArrayList<Object> selectedPhotosOrder;
    private boolean sendPressed;
    private int type;
    
    public PhotoPickerActivity(final int type, final MediaController.AlbumEntry selectedAlbum, final HashMap<Object, Object> selectedPhotos, final ArrayList<Object> selectedPhotosOrder, final ArrayList<MediaController.SearchImage> recentImages, final int selectPhotoType, final boolean allowCaption, final ChatActivity chatActivity) {
        this.searchResult = new ArrayList<MediaController.SearchImage>();
        this.searchResultKeys = new HashMap<String, MediaController.SearchImage>();
        this.searchResultUrls = new HashMap<String, MediaController.SearchImage>();
        this.imageSearchEndReached = true;
        this.itemWidth = 100;
        this.provider = new PhotoViewer.EmptyPhotoViewerProvider() {
            @Override
            public boolean allowCaption() {
                return PhotoPickerActivity.this.allowCaption;
            }
            
            @Override
            public boolean allowGroupPhotos() {
                return PhotoPickerActivity.this.imageOrderToggleButton != null;
            }
            
            @Override
            public boolean cancelButtonPressed() {
                PhotoPickerActivity.this.delegate.actionButtonPressed(true);
                PhotoPickerActivity.this.finishFragment();
                return true;
            }
            
            @Override
            public PlaceProviderObject getPlaceForPhoto(final MessageObject messageObject, final TLRPC.FileLocation fileLocation, int statusBarHeight, final boolean b) {
                final PhotoPickerPhotoCell access$000 = PhotoPickerActivity.this.getCellForIndex(statusBarHeight);
                if (access$000 != null) {
                    final int[] array = new int[2];
                    access$000.photoImage.getLocationInWindow(array);
                    final PlaceProviderObject placeProviderObject = new PhotoViewer.PlaceProviderObject();
                    placeProviderObject.viewX = array[0];
                    final int n = array[1];
                    if (Build$VERSION.SDK_INT >= 21) {
                        statusBarHeight = 0;
                    }
                    else {
                        statusBarHeight = AndroidUtilities.statusBarHeight;
                    }
                    placeProviderObject.viewY = n - statusBarHeight;
                    placeProviderObject.parentView = (View)PhotoPickerActivity.this.listView;
                    placeProviderObject.imageReceiver = access$000.photoImage.getImageReceiver();
                    placeProviderObject.thumb = placeProviderObject.imageReceiver.getBitmapSafe();
                    placeProviderObject.scale = access$000.photoImage.getScaleX();
                    access$000.showCheck(false);
                    return placeProviderObject;
                }
                return null;
            }
            
            @Override
            public int getSelectedCount() {
                return PhotoPickerActivity.this.selectedPhotos.size();
            }
            
            @Override
            public HashMap<Object, Object> getSelectedPhotos() {
                return PhotoPickerActivity.this.selectedPhotos;
            }
            
            @Override
            public ArrayList<Object> getSelectedPhotosOrder() {
                return PhotoPickerActivity.this.selectedPhotosOrder;
            }
            
            @Override
            public ImageReceiver.BitmapHolder getThumbForPhoto(final MessageObject messageObject, final TLRPC.FileLocation fileLocation, final int n) {
                final PhotoPickerPhotoCell access$000 = PhotoPickerActivity.this.getCellForIndex(n);
                if (access$000 != null) {
                    return access$000.photoImage.getImageReceiver().getBitmapSafe();
                }
                return null;
            }
            
            @Override
            public boolean isPhotoChecked(final int n) {
                final MediaController.AlbumEntry access$200 = PhotoPickerActivity.this.selectedAlbum;
                final boolean b = true;
                boolean b2 = true;
                if (access$200 != null) {
                    if (n < 0 || n >= PhotoPickerActivity.this.selectedAlbum.photos.size() || !PhotoPickerActivity.this.selectedPhotos.containsKey(PhotoPickerActivity.this.selectedAlbum.photos.get(n).imageId)) {
                        b2 = false;
                    }
                    return b2;
                }
                ArrayList list;
                if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                    list = PhotoPickerActivity.this.recentImages;
                }
                else {
                    list = PhotoPickerActivity.this.searchResult;
                }
                return n >= 0 && n < list.size() && PhotoPickerActivity.this.selectedPhotos.containsKey(list.get(n).id) && b;
            }
            
            @Override
            public boolean scaleToFill() {
                return false;
            }
            
            @Override
            public void sendButtonPressed(final int n, final VideoEditedInfo editedInfo) {
                if (PhotoPickerActivity.this.selectedPhotos.isEmpty()) {
                    if (PhotoPickerActivity.this.selectedAlbum != null) {
                        if (n < 0 || n >= PhotoPickerActivity.this.selectedAlbum.photos.size()) {
                            return;
                        }
                        final MediaController.PhotoEntry photoEntry = PhotoPickerActivity.this.selectedAlbum.photos.get(n);
                        photoEntry.editedInfo = editedInfo;
                        PhotoPickerActivity.this.addToSelectedPhotos(photoEntry, -1);
                    }
                    else {
                        ArrayList list;
                        if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                            list = PhotoPickerActivity.this.recentImages;
                        }
                        else {
                            list = PhotoPickerActivity.this.searchResult;
                        }
                        if (n < 0 || n >= list.size()) {
                            return;
                        }
                        PhotoPickerActivity.this.addToSelectedPhotos(list.get(n), -1);
                    }
                }
                PhotoPickerActivity.this.sendSelectedPhotos();
            }
            
            @Override
            public int setPhotoChecked(int n, final VideoEditedInfo editedInfo) {
                final MediaController.AlbumEntry access$200 = PhotoPickerActivity.this.selectedAlbum;
                final int n2 = -1;
                int n3;
                boolean b;
                if (access$200 != null) {
                    if (n < 0 || n >= PhotoPickerActivity.this.selectedAlbum.photos.size()) {
                        return -1;
                    }
                    final MediaController.PhotoEntry photoEntry = PhotoPickerActivity.this.selectedAlbum.photos.get(n);
                    n3 = PhotoPickerActivity.this.addToSelectedPhotos(photoEntry, -1);
                    if (n3 == -1) {
                        photoEntry.editedInfo = editedInfo;
                        n3 = PhotoPickerActivity.this.selectedPhotosOrder.indexOf(photoEntry.imageId);
                        b = true;
                    }
                    else {
                        photoEntry.editedInfo = null;
                        b = false;
                    }
                }
                else {
                    ArrayList list;
                    if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                        list = PhotoPickerActivity.this.recentImages;
                    }
                    else {
                        list = PhotoPickerActivity.this.searchResult;
                    }
                    if (n < 0 || n >= list.size()) {
                        return -1;
                    }
                    final MediaController.SearchImage searchImage = list.get(n);
                    n3 = PhotoPickerActivity.this.addToSelectedPhotos(searchImage, -1);
                    if (n3 == -1) {
                        n3 = PhotoPickerActivity.this.selectedPhotosOrder.indexOf(searchImage.id);
                        b = true;
                    }
                    else {
                        b = false;
                    }
                }
                for (int childCount = PhotoPickerActivity.this.listView.getChildCount(), i = 0; i < childCount; ++i) {
                    final View child = PhotoPickerActivity.this.listView.getChildAt(i);
                    if ((int)child.getTag() == n) {
                        final PhotoPickerPhotoCell photoPickerPhotoCell = (PhotoPickerPhotoCell)child;
                        n = n2;
                        if (PhotoPickerActivity.this.allowIndices) {
                            n = n3;
                        }
                        photoPickerPhotoCell.setChecked(n, b, false);
                        break;
                    }
                }
                PhotoPickerActivity.this.pickerBottomLayout.updateSelectedCount(PhotoPickerActivity.this.selectedPhotos.size(), true);
                PhotoPickerActivity.this.delegate.selectedPhotosChanged();
                return n3;
            }
            
            @Override
            public int setPhotoUnchecked(final Object o) {
                Serializable o2;
                if (o instanceof MediaController.PhotoEntry) {
                    o2 = ((MediaController.PhotoEntry)o).imageId;
                }
                else if (o instanceof MediaController.SearchImage) {
                    o2 = ((MediaController.SearchImage)o).id;
                }
                else {
                    o2 = null;
                }
                if (o2 == null) {
                    return -1;
                }
                if (PhotoPickerActivity.this.selectedPhotos.containsKey(o2)) {
                    PhotoPickerActivity.this.selectedPhotos.remove(o2);
                    final int index = PhotoPickerActivity.this.selectedPhotosOrder.indexOf(o2);
                    if (index >= 0) {
                        PhotoPickerActivity.this.selectedPhotosOrder.remove(index);
                    }
                    if (PhotoPickerActivity.this.allowIndices) {
                        PhotoPickerActivity.this.updateCheckedPhotoIndices();
                    }
                    return index;
                }
                return -1;
            }
            
            @Override
            public void toggleGroupPhotosEnabled() {
                if (PhotoPickerActivity.this.imageOrderToggleButton != null) {
                    final ImageView access$1500 = PhotoPickerActivity.this.imageOrderToggleButton;
                    Object colorFilter;
                    if (SharedConfig.groupPhotosEnabled) {
                        colorFilter = new PorterDuffColorFilter(-10043398, PorterDuff$Mode.MULTIPLY);
                    }
                    else {
                        colorFilter = null;
                    }
                    access$1500.setColorFilter((ColorFilter)colorFilter);
                }
            }
            
            @Override
            public void updatePhotoAtIndex(final int n) {
                final PhotoPickerPhotoCell access$000 = PhotoPickerActivity.this.getCellForIndex(n);
                if (access$000 != null) {
                    if (PhotoPickerActivity.this.selectedAlbum != null) {
                        access$000.photoImage.setOrientation(0, true);
                        final MediaController.PhotoEntry photoEntry = PhotoPickerActivity.this.selectedAlbum.photos.get(n);
                        final String thumbPath = photoEntry.thumbPath;
                        if (thumbPath != null) {
                            access$000.photoImage.setImage(thumbPath, null, access$000.getContext().getResources().getDrawable(2131165697));
                        }
                        else if (photoEntry.path != null) {
                            access$000.photoImage.setOrientation(photoEntry.orientation, true);
                            if (photoEntry.isVideo) {
                                final BackupImageView photoImage = access$000.photoImage;
                                final StringBuilder sb = new StringBuilder();
                                sb.append("vthumb://");
                                sb.append(photoEntry.imageId);
                                sb.append(":");
                                sb.append(photoEntry.path);
                                photoImage.setImage(sb.toString(), null, access$000.getContext().getResources().getDrawable(2131165697));
                            }
                            else {
                                final BackupImageView photoImage2 = access$000.photoImage;
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("thumb://");
                                sb2.append(photoEntry.imageId);
                                sb2.append(":");
                                sb2.append(photoEntry.path);
                                photoImage2.setImage(sb2.toString(), null, access$000.getContext().getResources().getDrawable(2131165697));
                            }
                        }
                        else {
                            access$000.photoImage.setImageResource(2131165697);
                        }
                    }
                    else {
                        ArrayList list;
                        if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                            list = PhotoPickerActivity.this.recentImages;
                        }
                        else {
                            list = PhotoPickerActivity.this.searchResult;
                        }
                        access$000.setImage(list.get(n));
                    }
                }
            }
            
            @Override
            public void willHidePhotoViewer() {
                for (int childCount = PhotoPickerActivity.this.listView.getChildCount(), i = 0; i < childCount; ++i) {
                    final View child = PhotoPickerActivity.this.listView.getChildAt(i);
                    if (child instanceof PhotoPickerPhotoCell) {
                        ((PhotoPickerPhotoCell)child).showCheck(true);
                    }
                }
            }
            
            @Override
            public void willSwitchFromPhoto(final MessageObject messageObject, final TLRPC.FileLocation fileLocation, final int n) {
                for (int childCount = PhotoPickerActivity.this.listView.getChildCount(), i = 0; i < childCount; ++i) {
                    final View child = PhotoPickerActivity.this.listView.getChildAt(i);
                    if (child.getTag() != null) {
                        final PhotoPickerPhotoCell photoPickerPhotoCell = (PhotoPickerPhotoCell)child;
                        final int intValue = (int)child.getTag();
                        if (PhotoPickerActivity.this.selectedAlbum != null) {
                            if (intValue < 0) {
                                continue;
                            }
                            if (intValue >= PhotoPickerActivity.this.selectedAlbum.photos.size()) {
                                continue;
                            }
                        }
                        else {
                            ArrayList list;
                            if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                                list = PhotoPickerActivity.this.recentImages;
                            }
                            else {
                                list = PhotoPickerActivity.this.searchResult;
                            }
                            if (intValue < 0) {
                                continue;
                            }
                            if (intValue >= list.size()) {
                                continue;
                            }
                        }
                        if (intValue == n) {
                            photoPickerPhotoCell.showCheck(true);
                            break;
                        }
                    }
                }
            }
        };
        this.selectedAlbum = selectedAlbum;
        this.selectedPhotos = selectedPhotos;
        this.selectedPhotosOrder = selectedPhotosOrder;
        this.type = type;
        this.recentImages = recentImages;
        this.selectPhotoType = selectPhotoType;
        this.chatActivity = chatActivity;
        this.allowCaption = allowCaption;
    }
    
    private int addToSelectedPhotos(final Object value, final int n) {
        final boolean b = value instanceof MediaController.PhotoEntry;
        Serializable e;
        if (b) {
            e = ((MediaController.PhotoEntry)value).imageId;
        }
        else if (value instanceof MediaController.SearchImage) {
            e = ((MediaController.SearchImage)value).id;
        }
        else {
            e = null;
        }
        if (e == null) {
            return -1;
        }
        if (this.selectedPhotos.containsKey(e)) {
            this.selectedPhotos.remove(e);
            final int index = this.selectedPhotosOrder.indexOf(e);
            if (index >= 0) {
                this.selectedPhotosOrder.remove(index);
            }
            if (this.allowIndices) {
                this.updateCheckedPhotoIndices();
            }
            if (n >= 0) {
                if (b) {
                    ((MediaController.PhotoEntry)value).reset();
                }
                else if (value instanceof MediaController.SearchImage) {
                    ((MediaController.SearchImage)value).reset();
                }
                this.provider.updatePhotoAtIndex(n);
            }
            return index;
        }
        this.selectedPhotos.put(e, value);
        this.selectedPhotosOrder.add(e);
        return -1;
    }
    
    private void fixLayout() {
        final RecyclerListView listView = this.listView;
        if (listView != null) {
            listView.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
                public boolean onPreDraw() {
                    PhotoPickerActivity.this.fixLayoutInternal();
                    if (PhotoPickerActivity.this.listView != null) {
                        PhotoPickerActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
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
        final int firstVisibleItemPosition = this.layoutManager.findFirstVisibleItemPosition();
        final int rotation = ((WindowManager)ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
        final boolean tablet = AndroidUtilities.isTablet();
        int spanCount = 3;
        if (!tablet) {
            if (rotation == 3 || rotation == 1) {
                spanCount = 5;
            }
        }
        this.layoutManager.setSpanCount(spanCount);
        if (AndroidUtilities.isTablet()) {
            this.itemWidth = (AndroidUtilities.dp(490.0f) - (spanCount + 1) * AndroidUtilities.dp(4.0f)) / spanCount;
        }
        else {
            this.itemWidth = (AndroidUtilities.displaySize.x - (spanCount + 1) * AndroidUtilities.dp(4.0f)) / spanCount;
        }
        this.listAdapter.notifyDataSetChanged();
        this.layoutManager.scrollToPosition(firstVisibleItemPosition);
        if (this.selectedAlbum == null) {
            this.emptyView.setPadding(0, 0, 0, (int)((AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight()) * 0.4f));
        }
    }
    
    private PhotoPickerPhotoCell getCellForIndex(final int n) {
        for (int childCount = this.listView.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.listView.getChildAt(i);
            if (child instanceof PhotoPickerPhotoCell) {
                final PhotoPickerPhotoCell photoPickerPhotoCell = (PhotoPickerPhotoCell)child;
                final int intValue = (int)photoPickerPhotoCell.photoImage.getTag();
                final MediaController.AlbumEntry selectedAlbum = this.selectedAlbum;
                if (selectedAlbum != null) {
                    if (intValue < 0) {
                        continue;
                    }
                    if (intValue >= selectedAlbum.photos.size()) {
                        continue;
                    }
                }
                else {
                    ArrayList<MediaController.SearchImage> list;
                    if (this.searchResult.isEmpty() && this.lastSearchString == null) {
                        list = this.recentImages;
                    }
                    else {
                        list = this.searchResult;
                    }
                    if (intValue < 0) {
                        continue;
                    }
                    if (intValue >= list.size()) {
                        continue;
                    }
                }
                if (intValue == n) {
                    return photoPickerPhotoCell;
                }
            }
        }
        return null;
    }
    
    private void hideHint() {
        (this.hintAnimation = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.hintTextView, "alpha", new float[] { 0.0f }) });
        this.hintAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator animator) {
                if (animator.equals(PhotoPickerActivity.this.hintAnimation)) {
                    PhotoPickerActivity.this.hintHideRunnable = null;
                    PhotoPickerActivity.this.hintHideRunnable = null;
                }
            }
            
            public void onAnimationEnd(final Animator animator) {
                if (animator.equals(PhotoPickerActivity.this.hintAnimation)) {
                    PhotoPickerActivity.this.hintAnimation = null;
                    PhotoPickerActivity.this.hintHideRunnable = null;
                    if (PhotoPickerActivity.this.hintTextView != null) {
                        PhotoPickerActivity.this.hintTextView.setVisibility(8);
                    }
                }
            }
        });
        this.hintAnimation.setDuration(300L);
        this.hintAnimation.start();
    }
    
    private void processSearch(final EditText editText) {
        if (editText.getText().toString().length() == 0) {
            return;
        }
        this.searchResult.clear();
        this.searchResultKeys.clear();
        this.imageSearchEndReached = true;
        this.searchImages(this.type == 1, editText.getText().toString(), "", true);
        this.lastSearchString = editText.getText().toString();
        if (this.lastSearchString.length() == 0) {
            this.lastSearchString = null;
            this.emptyView.setText("");
        }
        else {
            this.emptyView.setText(LocaleController.getString("NoResult", 2131559943));
        }
        this.updateSearchInterface();
    }
    
    private void searchBotUser(final boolean b) {
        if (this.searchingUser) {
            return;
        }
        this.searchingUser = true;
        final TLRPC.TL_contacts_resolveUsername tl_contacts_resolveUsername = new TLRPC.TL_contacts_resolveUsername();
        String username;
        if (b) {
            username = MessagesController.getInstance(super.currentAccount).gifSearchBot;
        }
        else {
            username = MessagesController.getInstance(super.currentAccount).imageSearchBot;
        }
        tl_contacts_resolveUsername.username = username;
        ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_contacts_resolveUsername, new _$$Lambda$PhotoPickerActivity$67brVZzCZFPPW5KLbmmY3AJwLD4(this, b));
    }
    
    private void searchImages(final boolean b, final String lastSearchImageString, final String offset, final boolean b2) {
        if (this.searching) {
            this.searching = false;
            if (this.imageReqId != 0) {
                ConnectionsManager.getInstance(super.currentAccount).cancelRequest(this.imageReqId, true);
                this.imageReqId = 0;
            }
        }
        this.lastSearchImageString = lastSearchImageString;
        this.searching = true;
        final MessagesController instance = MessagesController.getInstance(super.currentAccount);
        String s;
        if (b) {
            s = MessagesController.getInstance(super.currentAccount).gifSearchBot;
        }
        else {
            s = MessagesController.getInstance(super.currentAccount).imageSearchBot;
        }
        final TLObject userOrChat = instance.getUserOrChat(s);
        if (!(userOrChat instanceof TLRPC.User)) {
            if (b2) {
                this.searchBotUser(b);
            }
            return;
        }
        final TLRPC.User user = (TLRPC.User)userOrChat;
        final TLRPC.TL_messages_getInlineBotResults tl_messages_getInlineBotResults = new TLRPC.TL_messages_getInlineBotResults();
        String query;
        if ((query = lastSearchImageString) == null) {
            query = "";
        }
        tl_messages_getInlineBotResults.query = query;
        tl_messages_getInlineBotResults.bot = MessagesController.getInstance(super.currentAccount).getInputUser(user);
        tl_messages_getInlineBotResults.offset = offset;
        final ChatActivity chatActivity = this.chatActivity;
        if (chatActivity != null) {
            final int n = (int)chatActivity.getDialogId();
            if (n != 0) {
                tl_messages_getInlineBotResults.peer = MessagesController.getInstance(super.currentAccount).getInputPeer(n);
            }
            else {
                tl_messages_getInlineBotResults.peer = new TLRPC.TL_inputPeerEmpty();
            }
        }
        else {
            tl_messages_getInlineBotResults.peer = new TLRPC.TL_inputPeerEmpty();
        }
        final int lastSearchToken = this.lastSearchToken + 1;
        this.lastSearchToken = lastSearchToken;
        this.imageReqId = ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_messages_getInlineBotResults, new _$$Lambda$PhotoPickerActivity$uL23aS4dzDeSWXHf7OAjR3N3zqg(this, lastSearchToken, b));
        ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(this.imageReqId, super.classGuid);
    }
    
    private void sendSelectedPhotos() {
        if (!this.selectedPhotos.isEmpty()) {
            final PhotoPickerActivityDelegate delegate = this.delegate;
            if (delegate != null) {
                if (!this.sendPressed) {
                    this.sendPressed = true;
                    delegate.actionButtonPressed(false);
                    if (this.selectPhotoType != 2) {
                        this.finishFragment();
                    }
                }
            }
        }
    }
    
    private void showHint(final boolean b, final boolean b2) {
        if (this.getParentActivity() != null && super.fragmentView != null) {
            if (!b || this.hintTextView != null) {
                if (this.hintTextView == null) {
                    (this.hintTextView = new TextView((Context)this.getParentActivity())).setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(3.0f), Theme.getColor("chat_gifSaveHintBackground")));
                    this.hintTextView.setTextColor(Theme.getColor("chat_gifSaveHintText"));
                    this.hintTextView.setTextSize(1, 14.0f);
                    this.hintTextView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(7.0f));
                    this.hintTextView.setGravity(16);
                    this.hintTextView.setAlpha(0.0f);
                    this.frameLayout.addView((View)this.hintTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 81, 5.0f, 0.0f, 5.0f, 51.0f));
                }
                if (b) {
                    final AnimatorSet hintAnimation = this.hintAnimation;
                    if (hintAnimation != null) {
                        hintAnimation.cancel();
                        this.hintAnimation = null;
                    }
                    AndroidUtilities.cancelRunOnUIThread(this.hintHideRunnable);
                    this.hintHideRunnable = null;
                    this.hideHint();
                    return;
                }
                final TextView hintTextView = this.hintTextView;
                int n;
                String s;
                if (b2) {
                    n = 2131559612;
                    s = "GroupPhotosHelp";
                }
                else {
                    n = 2131560786;
                    s = "SinglePhotosHelp";
                }
                hintTextView.setText((CharSequence)LocaleController.getString(s, n));
                final Runnable hintHideRunnable = this.hintHideRunnable;
                if (hintHideRunnable != null) {
                    final AnimatorSet hintAnimation2 = this.hintAnimation;
                    if (hintAnimation2 == null) {
                        AndroidUtilities.cancelRunOnUIThread(hintHideRunnable);
                        AndroidUtilities.runOnUIThread(this.hintHideRunnable = new _$$Lambda$PhotoPickerActivity$aYB7K3WSG2cRcI6ke4K4lfSNCNw(this), 2000L);
                        return;
                    }
                    hintAnimation2.cancel();
                    this.hintAnimation = null;
                }
                else if (this.hintAnimation != null) {
                    return;
                }
                this.hintTextView.setVisibility(0);
                (this.hintAnimation = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.hintTextView, "alpha", new float[] { 1.0f }) });
                this.hintAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationCancel(final Animator animator) {
                        if (animator.equals(PhotoPickerActivity.this.hintAnimation)) {
                            PhotoPickerActivity.this.hintAnimation = null;
                        }
                    }
                    
                    public void onAnimationEnd(final Animator animator) {
                        if (animator.equals(PhotoPickerActivity.this.hintAnimation)) {
                            PhotoPickerActivity.this.hintAnimation = null;
                            final PhotoPickerActivity this$0 = PhotoPickerActivity.this;
                            final _$$Lambda$PhotoPickerActivity$8$zJKelkpjam7V_XUMpJypLa8CZ48 $$Lambda$PhotoPickerActivity$8$zJKelkpjam7V_XUMpJypLa8CZ48 = new _$$Lambda$PhotoPickerActivity$8$zJKelkpjam7V_XUMpJypLa8CZ48(this);
                            this$0.hintHideRunnable = $$Lambda$PhotoPickerActivity$8$zJKelkpjam7V_XUMpJypLa8CZ48;
                            AndroidUtilities.runOnUIThread($$Lambda$PhotoPickerActivity$8$zJKelkpjam7V_XUMpJypLa8CZ48, 2000L);
                        }
                    }
                });
                this.hintAnimation.setDuration(300L);
                this.hintAnimation.start();
            }
        }
    }
    
    private void updateCheckedPhotoIndices() {
        if (!this.allowIndices) {
            return;
        }
        for (int childCount = this.listView.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.listView.getChildAt(i);
            if (child instanceof PhotoPickerPhotoCell) {
                final PhotoPickerPhotoCell photoPickerPhotoCell = (PhotoPickerPhotoCell)child;
                final Integer n = (Integer)photoPickerPhotoCell.getTag();
                final MediaController.AlbumEntry selectedAlbum = this.selectedAlbum;
                int n2 = -1;
                if (selectedAlbum != null) {
                    final MediaController.PhotoEntry photoEntry = selectedAlbum.photos.get(n);
                    if (this.allowIndices) {
                        n2 = this.selectedPhotosOrder.indexOf(photoEntry.imageId);
                    }
                    photoPickerPhotoCell.setNum(n2);
                }
                else {
                    MediaController.SearchImage searchImage;
                    if (this.searchResult.isEmpty() && this.lastSearchString == null) {
                        searchImage = this.recentImages.get(n);
                    }
                    else {
                        searchImage = this.searchResult.get(n);
                    }
                    if (this.allowIndices) {
                        n2 = this.selectedPhotosOrder.indexOf(searchImage.id);
                    }
                    photoPickerPhotoCell.setNum(n2);
                }
            }
        }
    }
    
    private void updateSearchInterface() {
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
        if ((this.searching && this.searchResult.isEmpty()) || (this.loadingRecent && this.lastSearchString == null)) {
            this.emptyView.showProgress();
        }
        else {
            this.emptyView.showTextView();
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackgroundColor(-13421773);
        final ActionBar actionBar = super.actionBar;
        final boolean b = false;
        actionBar.setItemsBackgroundColor(-12763843, false);
        super.actionBar.setTitleColor(-1);
        super.actionBar.setItemsColor(-1, false);
        super.actionBar.setBackButtonImage(2131165409);
        final MediaController.AlbumEntry selectedAlbum = this.selectedAlbum;
        if (selectedAlbum != null) {
            super.actionBar.setTitle(selectedAlbum.bucketName);
        }
        else {
            final int type = this.type;
            if (type == 0) {
                super.actionBar.setTitle(LocaleController.getString("SearchImagesTitle", 2131560652));
            }
            else if (type == 1) {
                super.actionBar.setTitle(LocaleController.getString("SearchGifsTitle", 2131560649));
            }
        }
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    PhotoPickerActivity.this.finishFragment();
                }
            }
        });
        if (this.selectedAlbum == null) {
            this.searchItem = super.actionBar.createMenu().addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener((ActionBarMenuItem.ActionBarMenuItemSearchListener)new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
                @Override
                public boolean canCollapseSearch() {
                    PhotoPickerActivity.this.finishFragment();
                    return false;
                }
                
                @Override
                public void onSearchExpand() {
                }
                
                @Override
                public void onSearchPressed(final EditText editText) {
                    PhotoPickerActivity.this.processSearch(editText);
                }
                
                @Override
                public void onTextChanged(final EditText editText) {
                    if (editText.getText().length() == 0) {
                        PhotoPickerActivity.this.searchResult.clear();
                        PhotoPickerActivity.this.searchResultKeys.clear();
                        PhotoPickerActivity.this.lastSearchString = null;
                        PhotoPickerActivity.this.imageSearchEndReached = true;
                        PhotoPickerActivity.this.searching = false;
                        if (PhotoPickerActivity.this.imageReqId != 0) {
                            ConnectionsManager.getInstance(PhotoPickerActivity.this.currentAccount).cancelRequest(PhotoPickerActivity.this.imageReqId, true);
                            PhotoPickerActivity.this.imageReqId = 0;
                        }
                        PhotoPickerActivity.this.emptyView.setText("");
                        PhotoPickerActivity.this.updateSearchInterface();
                    }
                }
            });
        }
        if (this.selectedAlbum == null) {
            final int type2 = this.type;
            if (type2 == 0) {
                this.searchItem.setSearchFieldHint(LocaleController.getString("SearchImagesTitle", 2131560652));
            }
            else if (type2 == 1) {
                this.searchItem.setSearchFieldHint(LocaleController.getString("SearchGifsTitle", 2131560649));
            }
        }
        super.fragmentView = (View)new FrameLayout(context);
        (this.frameLayout = (FrameLayout)super.fragmentView).setBackgroundColor(-16777216);
        (this.listView = new RecyclerListView(context)).setPadding(AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f));
        this.listView.setClipToPadding(false);
        this.listView.setHorizontalScrollBarEnabled(false);
        this.listView.setVerticalScrollBarEnabled(false);
        final RecyclerListView listView = this.listView;
        final ColorFilter colorFilter = null;
        listView.setItemAnimator(null);
        this.listView.setLayoutAnimation((LayoutAnimationController)null);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new GridLayoutManager(context, 4) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        }));
        this.listView.addItemDecoration((RecyclerView.ItemDecoration)new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(final Rect rect, final View view, final RecyclerView recyclerView, final State state) {
                super.getItemOffsets(rect, view, recyclerView, state);
                final int itemCount = state.getItemCount();
                final int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
                final int spanCount = PhotoPickerActivity.this.layoutManager.getSpanCount();
                final int n = (int)Math.ceil(itemCount / (float)spanCount);
                final int n2 = childAdapterPosition / spanCount;
                final int n3 = 0;
                int dp;
                if (childAdapterPosition % spanCount != spanCount - 1) {
                    dp = AndroidUtilities.dp(4.0f);
                }
                else {
                    dp = 0;
                }
                rect.right = dp;
                int dp2 = n3;
                if (n2 != n - 1) {
                    dp2 = AndroidUtilities.dp(4.0f);
                }
                rect.bottom = dp2;
            }
        });
        final FrameLayout frameLayout = this.frameLayout;
        final RecyclerListView listView2 = this.listView;
        float n;
        if (this.selectPhotoType != 0) {
            n = 0.0f;
        }
        else {
            n = 48.0f;
        }
        frameLayout.addView((View)listView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, n));
        this.listView.setAdapter(this.listAdapter = new ListAdapter(context));
        this.listView.setGlowColor(-13421773);
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$PhotoPickerActivity$zD_mr7IpQctZxAyf7EKDChtrGZc(this));
        if (this.selectedAlbum == null) {
            this.listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)new _$$Lambda$PhotoPickerActivity$Hdsa1stRiohWHt8IIV2h_tg2fmI(this));
        }
        (this.emptyView = new EmptyTextProgressView(context)).setTextColor(-8355712);
        this.emptyView.setProgressBarColor(-1);
        this.emptyView.setShowAtCenter(true);
        if (this.selectedAlbum != null) {
            this.emptyView.setText(LocaleController.getString("NoPhotos", 2131559937));
        }
        else {
            this.emptyView.setText("");
        }
        final FrameLayout frameLayout2 = this.frameLayout;
        final EmptyTextProgressView emptyView = this.emptyView;
        float n2;
        if (this.selectPhotoType != 0) {
            n2 = 0.0f;
        }
        else {
            n2 = 48.0f;
        }
        frameLayout2.addView((View)emptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, n2));
        if (this.selectedAlbum == null) {
            this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                    if (n == 1) {
                        AndroidUtilities.hideKeyboard(PhotoPickerActivity.this.getParentActivity().getCurrentFocus());
                    }
                }
                
                @Override
                public void onScrolled(final RecyclerView recyclerView, int n, int firstVisibleItemPosition) {
                    firstVisibleItemPosition = PhotoPickerActivity.this.layoutManager.findFirstVisibleItemPosition();
                    boolean b = false;
                    if (firstVisibleItemPosition == -1) {
                        n = 0;
                    }
                    else {
                        n = Math.abs(PhotoPickerActivity.this.layoutManager.findLastVisibleItemPosition() - firstVisibleItemPosition) + 1;
                    }
                    if (n > 0) {
                        final int itemCount = ((RecyclerView.LayoutManager)PhotoPickerActivity.this.layoutManager).getItemCount();
                        if (n != 0 && firstVisibleItemPosition + n > itemCount - 2 && !PhotoPickerActivity.this.searching && !PhotoPickerActivity.this.imageSearchEndReached) {
                            final PhotoPickerActivity this$0 = PhotoPickerActivity.this;
                            if (this$0.type == 1) {
                                b = true;
                            }
                            this$0.searchImages(b, PhotoPickerActivity.this.lastSearchString, PhotoPickerActivity.this.nextImagesSearchOffset, true);
                        }
                    }
                }
            });
            this.updateSearchInterface();
        }
        this.pickerBottomLayout = new PickerBottomLayout(context);
        this.frameLayout.addView((View)this.pickerBottomLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 80));
        this.pickerBottomLayout.cancelButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoPickerActivity$KmEu4SYAqKOt718fgSd59sdqoWM(this));
        this.pickerBottomLayout.doneButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoPickerActivity$mGdvO16vUkJ4QAJ7aZX42E0io7E(this));
        if (this.selectPhotoType != 0) {
            this.pickerBottomLayout.setVisibility(8);
        }
        else if (this.selectedAlbum != null || this.type == 0) {
            final ChatActivity chatActivity = this.chatActivity;
            if (chatActivity != null && chatActivity.allowGroupPhotos()) {
                (this.imageOrderToggleButton = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
                this.imageOrderToggleButton.setImageResource(2131165757);
                final ImageView imageOrderToggleButton = this.imageOrderToggleButton;
                int n3;
                String s;
                if (SharedConfig.groupPhotosEnabled) {
                    n3 = 2131559612;
                    s = "GroupPhotosHelp";
                }
                else {
                    n3 = 2131560786;
                    s = "SinglePhotosHelp";
                }
                imageOrderToggleButton.setContentDescription((CharSequence)LocaleController.getString(s, n3));
                this.pickerBottomLayout.addView((View)this.imageOrderToggleButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, -1, 17));
                this.imageOrderToggleButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoPickerActivity$dTiSUg75sRfWPRlo26sk1jZD__Q(this));
                final ImageView imageOrderToggleButton2 = this.imageOrderToggleButton;
                Object colorFilter2 = colorFilter;
                if (SharedConfig.groupPhotosEnabled) {
                    colorFilter2 = new PorterDuffColorFilter(-10043398, PorterDuff$Mode.MULTIPLY);
                }
                imageOrderToggleButton2.setColorFilter((ColorFilter)colorFilter2);
            }
        }
        boolean allowIndices = false;
        Label_0981: {
            if (this.selectedAlbum == null) {
                allowIndices = b;
                if (this.type != 0) {
                    break Label_0981;
                }
            }
            allowIndices = b;
            if (this.maxSelectedPhotos <= 0) {
                allowIndices = true;
            }
        }
        this.allowIndices = allowIndices;
        this.listView.setEmptyView((View)this.emptyView);
        this.pickerBottomLayout.updateSelectedCount(this.selectedPhotos.size(), true);
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.closeChats) {
            this.removeSelfFromStack();
        }
        else if (n == NotificationCenter.recentImagesDidLoad && this.selectedAlbum == null && this.type == (int)array[0]) {
            this.recentImages = (ArrayList<MediaController.SearchImage>)array[1];
            this.loadingRecent = false;
            this.updateSearchInterface();
        }
    }
    
    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.fixLayout();
    }
    
    @Override
    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.recentImagesDidLoad);
        if (this.selectedAlbum == null && this.recentImages.isEmpty()) {
            MessagesStorage.getInstance(super.currentAccount).loadWebRecent(this.type);
            this.loadingRecent = true;
        }
        return super.onFragmentCreate();
    }
    
    @Override
    public void onFragmentDestroy() {
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.recentImagesDidLoad);
        if (this.imageReqId != 0) {
            ConnectionsManager.getInstance(super.currentAccount).cancelRequest(this.imageReqId, true);
            this.imageReqId = 0;
        }
        super.onFragmentDestroy();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
        final ActionBarMenuItem searchItem = this.searchItem;
        if (searchItem != null) {
            searchItem.openSearch(true);
            if (!TextUtils.isEmpty((CharSequence)this.initialSearchString)) {
                this.searchItem.setSearchFieldText(this.initialSearchString, false);
                this.initialSearchString = null;
                this.processSearch(this.searchItem.getSearchField());
            }
            this.getParentActivity().getWindow().setSoftInputMode(32);
        }
        this.fixLayout();
    }
    
    public void onTransitionAnimationEnd(final boolean b, final boolean b2) {
        if (b) {
            final ActionBarMenuItem searchItem = this.searchItem;
            if (searchItem != null) {
                AndroidUtilities.showKeyboard((View)searchItem.getSearchField());
            }
        }
    }
    
    public void setDelegate(final PhotoPickerActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setInitialSearchString(final String initialSearchString) {
        this.initialSearchString = initialSearchString;
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
            if (PhotoPickerActivity.this.selectedAlbum != null) {
                return PhotoPickerActivity.this.selectedAlbum.photos.size();
            }
            if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                return PhotoPickerActivity.this.recentImages.size();
            }
            return PhotoPickerActivity.this.searchResult.size() + ((PhotoPickerActivity.this.imageSearchEndReached ^ true) ? 1 : 0);
        }
        
        @Override
        public long getItemId(final int n) {
            return n;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (PhotoPickerActivity.this.selectedAlbum == null && (!PhotoPickerActivity.this.searchResult.isEmpty() || PhotoPickerActivity.this.lastSearchString != null || n >= PhotoPickerActivity.this.recentImages.size()) && n >= PhotoPickerActivity.this.searchResult.size()) {
                return 1;
            }
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final MediaController.AlbumEntry access$200 = PhotoPickerActivity.this.selectedAlbum;
            final boolean b = true;
            final boolean b2 = true;
            boolean b3 = b;
            if (access$200 == null) {
                final int adapterPosition = viewHolder.getAdapterPosition();
                if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                    return adapterPosition < PhotoPickerActivity.this.recentImages.size() && b2;
                }
                b3 = (adapterPosition < PhotoPickerActivity.this.searchResult.size() && b);
            }
            return b3;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int duration) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    final ViewGroup$LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
                    if (layoutParams != null) {
                        layoutParams.width = PhotoPickerActivity.this.itemWidth;
                        layoutParams.height = PhotoPickerActivity.this.itemWidth;
                        viewHolder.itemView.setLayoutParams(layoutParams);
                    }
                }
            }
            else {
                final PhotoPickerPhotoCell photoPickerPhotoCell = (PhotoPickerPhotoCell)viewHolder.itemView;
                photoPickerPhotoCell.itemWidth = PhotoPickerActivity.this.itemWidth;
                final BackupImageView photoImage = photoPickerPhotoCell.photoImage;
                photoImage.setTag((Object)duration);
                photoPickerPhotoCell.setTag((Object)duration);
                final int n = 0;
                photoImage.setOrientation(0, true);
                final MediaController.AlbumEntry access$200 = PhotoPickerActivity.this.selectedAlbum;
                int n2 = -1;
                boolean b;
                if (access$200 != null) {
                    final MediaController.PhotoEntry photoEntry = PhotoPickerActivity.this.selectedAlbum.photos.get(duration);
                    final String thumbPath = photoEntry.thumbPath;
                    if (thumbPath != null) {
                        photoImage.setImage(thumbPath, null, this.mContext.getResources().getDrawable(2131165697));
                    }
                    else if (photoEntry.path != null) {
                        photoImage.setOrientation(photoEntry.orientation, true);
                        if (photoEntry.isVideo) {
                            photoPickerPhotoCell.videoInfoContainer.setVisibility(0);
                            duration = photoEntry.duration;
                            final int i = duration / 60;
                            photoPickerPhotoCell.videoTextView.setText((CharSequence)String.format("%d:%02d", i, duration - i * 60));
                            final StringBuilder sb = new StringBuilder();
                            sb.append(LocaleController.getString("AttachVideo", 2131558733));
                            sb.append(", ");
                            sb.append(LocaleController.formatCallDuration(photoEntry.duration));
                            photoPickerPhotoCell.setContentDescription((CharSequence)sb.toString());
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("vthumb://");
                            sb2.append(photoEntry.imageId);
                            sb2.append(":");
                            sb2.append(photoEntry.path);
                            photoImage.setImage(sb2.toString(), null, this.mContext.getResources().getDrawable(2131165697));
                        }
                        else {
                            photoPickerPhotoCell.videoInfoContainer.setVisibility(4);
                            photoPickerPhotoCell.setContentDescription((CharSequence)LocaleController.getString("AttachPhoto", 2131558727));
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("thumb://");
                            sb3.append(photoEntry.imageId);
                            sb3.append(":");
                            sb3.append(photoEntry.path);
                            photoImage.setImage(sb3.toString(), null, this.mContext.getResources().getDrawable(2131165697));
                        }
                    }
                    else {
                        photoImage.setImageResource(2131165697);
                    }
                    if (PhotoPickerActivity.this.allowIndices) {
                        n2 = PhotoPickerActivity.this.selectedPhotosOrder.indexOf(photoEntry.imageId);
                    }
                    photoPickerPhotoCell.setChecked(n2, PhotoPickerActivity.this.selectedPhotos.containsKey(photoEntry.imageId), false);
                    b = PhotoViewer.isShowingImage(photoEntry.path);
                }
                else {
                    MediaController.SearchImage image;
                    if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                        image = PhotoPickerActivity.this.recentImages.get(duration);
                    }
                    else {
                        image = PhotoPickerActivity.this.searchResult.get(duration);
                    }
                    photoPickerPhotoCell.setImage(image);
                    photoPickerPhotoCell.videoInfoContainer.setVisibility(4);
                    if (PhotoPickerActivity.this.allowIndices) {
                        n2 = PhotoPickerActivity.this.selectedPhotosOrder.indexOf(image.id);
                    }
                    photoPickerPhotoCell.setChecked(n2, PhotoPickerActivity.this.selectedPhotos.containsKey(image.id), false);
                    b = PhotoViewer.isShowingImage(image.getPathToAttach());
                }
                photoImage.getImageReceiver().setVisible(!b, true);
                final CheckBox checkBox = photoPickerPhotoCell.checkBox;
                Label_0748: {
                    if (PhotoPickerActivity.this.selectPhotoType == 0) {
                        duration = n;
                        if (!b) {
                            break Label_0748;
                        }
                    }
                    duration = 8;
                }
                checkBox.setVisibility(duration);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int visibility) {
            FrameLayout frameLayout;
            if (visibility != 0) {
                frameLayout = new FrameLayout(this.mContext);
                final RadialProgressView radialProgressView = new RadialProgressView(this.mContext);
                radialProgressView.setProgressColor(-1);
                frameLayout.addView((View)radialProgressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            }
            else {
                frameLayout = new PhotoPickerPhotoCell(this.mContext, true);
                ((PhotoPickerPhotoCell)frameLayout).checkFrame.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoPickerActivity$ListAdapter$LuMWeDklP_a8AE5HSJIZdrtM3pI(this));
                final FrameLayout checkFrame = ((PhotoPickerPhotoCell)frameLayout).checkFrame;
                if (PhotoPickerActivity.this.selectPhotoType != 0) {
                    visibility = 8;
                }
                else {
                    visibility = 0;
                }
                checkFrame.setVisibility(visibility);
            }
            return new RecyclerListView.Holder((View)frameLayout);
        }
    }
    
    public interface PhotoPickerActivityDelegate
    {
        void actionButtonPressed(final boolean p0);
        
        void selectedPhotosChanged();
    }
}
