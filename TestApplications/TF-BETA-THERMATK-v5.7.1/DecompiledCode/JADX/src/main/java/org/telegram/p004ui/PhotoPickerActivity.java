package org.telegram.p004ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.RecyclerView.State;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageReceiver.BitmapHolder;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController.AlbumEntry;
import org.telegram.messenger.MediaController.PhotoEntry;
import org.telegram.messenger.MediaController.SearchImage;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.p004ui.ActionBar.ActionBarMenuItem;
import org.telegram.p004ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener;
import org.telegram.p004ui.ActionBar.AlertDialog.Builder;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Cells.PhotoPickerPhotoCell;
import org.telegram.p004ui.Components.BackupImageView;
import org.telegram.p004ui.Components.CheckBox;
import org.telegram.p004ui.Components.EmptyTextProgressView;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.PickerBottomLayout;
import org.telegram.p004ui.Components.RadialProgressView;
import org.telegram.p004ui.Components.RecyclerListView;
import org.telegram.p004ui.Components.RecyclerListView.Holder;
import org.telegram.p004ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.p004ui.PhotoViewer.EmptyPhotoViewerProvider;
import org.telegram.p004ui.PhotoViewer.PhotoViewerProvider;
import org.telegram.p004ui.PhotoViewer.PlaceProviderObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.BotInlineResult;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.DocumentAttribute;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.Photo;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_contacts_resolveUsername;
import org.telegram.tgnet.TLRPC.TL_contacts_resolvedPeer;
import org.telegram.tgnet.TLRPC.TL_documentAttributeImageSize;
import org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inputPeerEmpty;
import org.telegram.tgnet.TLRPC.TL_messages_getInlineBotResults;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.WebDocument;
import org.telegram.tgnet.TLRPC.messages_BotResults;

/* renamed from: org.telegram.ui.PhotoPickerActivity */
public class PhotoPickerActivity extends BaseFragment implements NotificationCenterDelegate {
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
    private boolean imageSearchEndReached = true;
    private String initialSearchString;
    private int itemWidth = 100;
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
    private PhotoViewerProvider provider = new C42791();
    private ArrayList<SearchImage> recentImages;
    private ActionBarMenuItem searchItem;
    private ArrayList<SearchImage> searchResult = new ArrayList();
    private HashMap<String, SearchImage> searchResultKeys = new HashMap();
    private HashMap<String, SearchImage> searchResultUrls = new HashMap();
    private boolean searching;
    private boolean searchingUser;
    private int selectPhotoType;
    private AlbumEntry selectedAlbum;
    private HashMap<Object, Object> selectedPhotos;
    private ArrayList<Object> selectedPhotosOrder;
    private boolean sendPressed;
    private int type;

    /* renamed from: org.telegram.ui.PhotoPickerActivity$7 */
    class C31557 extends AnimatorListenerAdapter {
        C31557() {
        }

        public void onAnimationEnd(Animator animator) {
            if (animator.equals(PhotoPickerActivity.this.hintAnimation)) {
                PhotoPickerActivity.this.hintAnimation = null;
                PhotoPickerActivity.this.hintHideRunnable = null;
                if (PhotoPickerActivity.this.hintTextView != null) {
                    PhotoPickerActivity.this.hintTextView.setVisibility(8);
                }
            }
        }

        public void onAnimationCancel(Animator animator) {
            if (animator.equals(PhotoPickerActivity.this.hintAnimation)) {
                PhotoPickerActivity.this.hintHideRunnable = null;
                PhotoPickerActivity.this.hintHideRunnable = null;
            }
        }
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$8 */
    class C31568 extends AnimatorListenerAdapter {
        C31568() {
        }

        public void onAnimationEnd(Animator animator) {
            if (animator.equals(PhotoPickerActivity.this.hintAnimation)) {
                PhotoPickerActivity.this.hintAnimation = null;
                PhotoPickerActivity photoPickerActivity = PhotoPickerActivity.this;
                C1834-$$Lambda$PhotoPickerActivity$8$zJKelkpjam7V-XUMpJypLa8CZ48 c1834-$$Lambda$PhotoPickerActivity$8$zJKelkpjam7V-XUMpJypLa8CZ48 = new C1834-$$Lambda$PhotoPickerActivity$8$zJKelkpjam7V-XUMpJypLa8CZ48(this);
                photoPickerActivity.hintHideRunnable = c1834-$$Lambda$PhotoPickerActivity$8$zJKelkpjam7V-XUMpJypLa8CZ48;
                AndroidUtilities.runOnUIThread(c1834-$$Lambda$PhotoPickerActivity$8$zJKelkpjam7V-XUMpJypLa8CZ48, 2000);
            }
        }

        public /* synthetic */ void lambda$onAnimationEnd$0$PhotoPickerActivity$8() {
            PhotoPickerActivity.this.hideHint();
        }

        public void onAnimationCancel(Animator animator) {
            if (animator.equals(PhotoPickerActivity.this.hintAnimation)) {
                PhotoPickerActivity.this.hintAnimation = null;
            }
        }
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$9 */
    class C31579 implements OnPreDrawListener {
        C31579() {
        }

        public boolean onPreDraw() {
            PhotoPickerActivity.this.fixLayoutInternal();
            if (PhotoPickerActivity.this.listView != null) {
                PhotoPickerActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
            }
            return true;
        }
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$PhotoPickerActivityDelegate */
    public interface PhotoPickerActivityDelegate {
        void actionButtonPressed(boolean z);

        void selectedPhotosChanged();
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$2 */
    class C42752 extends ActionBarMenuOnItemClick {
        C42752() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                PhotoPickerActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$3 */
    class C42763 extends ActionBarMenuItemSearchListener {
        public void onSearchExpand() {
        }

        C42763() {
        }

        public boolean canCollapseSearch() {
            PhotoPickerActivity.this.finishFragment();
            return false;
        }

        public void onTextChanged(EditText editText) {
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

        public void onSearchPressed(EditText editText) {
            PhotoPickerActivity.this.processSearch(editText);
        }
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$5 */
    class C42775 extends ItemDecoration {
        C42775() {
        }

        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, State state) {
            super.getItemOffsets(rect, view, recyclerView, state);
            int itemCount = state.getItemCount();
            int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
            int spanCount = PhotoPickerActivity.this.layoutManager.getSpanCount();
            itemCount = (int) Math.ceil((double) (((float) itemCount) / ((float) spanCount)));
            int i = childAdapterPosition / spanCount;
            int i2 = 0;
            rect.right = childAdapterPosition % spanCount != spanCount + -1 ? AndroidUtilities.m26dp(4.0f) : 0;
            if (i != itemCount - 1) {
                i2 = AndroidUtilities.m26dp(4.0f);
            }
            rect.bottom = i2;
        }
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$6 */
    class C42786 extends OnScrollListener {
        C42786() {
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 1) {
                AndroidUtilities.hideKeyboard(PhotoPickerActivity.this.getParentActivity().getCurrentFocus());
            }
        }

        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            int i3;
            int findFirstVisibleItemPosition = PhotoPickerActivity.this.layoutManager.findFirstVisibleItemPosition();
            boolean z = false;
            if (findFirstVisibleItemPosition == -1) {
                i3 = 0;
            } else {
                i3 = Math.abs(PhotoPickerActivity.this.layoutManager.findLastVisibleItemPosition() - findFirstVisibleItemPosition) + 1;
            }
            if (i3 > 0) {
                int itemCount = PhotoPickerActivity.this.layoutManager.getItemCount();
                if (i3 != 0 && findFirstVisibleItemPosition + i3 > itemCount - 2 && !PhotoPickerActivity.this.searching && !PhotoPickerActivity.this.imageSearchEndReached) {
                    PhotoPickerActivity photoPickerActivity = PhotoPickerActivity.this;
                    if (photoPickerActivity.type == 1) {
                        z = true;
                    }
                    photoPickerActivity.searchImages(z, PhotoPickerActivity.this.lastSearchString, PhotoPickerActivity.this.nextImagesSearchOffset, true);
                }
            }
        }
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$1 */
    class C42791 extends EmptyPhotoViewerProvider {
        public boolean scaleToFill() {
            return false;
        }

        C42791() {
        }

        public PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, FileLocation fileLocation, int i, boolean z) {
            PhotoPickerPhotoCell access$000 = PhotoPickerActivity.this.getCellForIndex(i);
            if (access$000 == null) {
                return null;
            }
            int[] iArr = new int[2];
            access$000.photoImage.getLocationInWindow(iArr);
            PlaceProviderObject placeProviderObject = new PlaceProviderObject();
            placeProviderObject.viewX = iArr[0];
            placeProviderObject.viewY = iArr[1] - (VERSION.SDK_INT >= 21 ? 0 : AndroidUtilities.statusBarHeight);
            placeProviderObject.parentView = PhotoPickerActivity.this.listView;
            placeProviderObject.imageReceiver = access$000.photoImage.getImageReceiver();
            placeProviderObject.thumb = placeProviderObject.imageReceiver.getBitmapSafe();
            placeProviderObject.scale = access$000.photoImage.getScaleX();
            access$000.showCheck(false);
            return placeProviderObject;
        }

        public void updatePhotoAtIndex(int i) {
            PhotoPickerPhotoCell access$000 = PhotoPickerActivity.this.getCellForIndex(i);
            if (access$000 == null) {
                return;
            }
            if (PhotoPickerActivity.this.selectedAlbum != null) {
                access$000.photoImage.setOrientation(0, true);
                PhotoEntry photoEntry = (PhotoEntry) PhotoPickerActivity.this.selectedAlbum.photos.get(i);
                String str = photoEntry.thumbPath;
                if (str != null) {
                    access$000.photoImage.setImage(str, null, access$000.getContext().getResources().getDrawable(C1067R.C1065drawable.nophotos));
                    return;
                } else if (photoEntry.path != null) {
                    access$000.photoImage.setOrientation(photoEntry.orientation, true);
                    String str2 = ":";
                    BackupImageView backupImageView;
                    StringBuilder stringBuilder;
                    if (photoEntry.isVideo) {
                        backupImageView = access$000.photoImage;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("vthumb://");
                        stringBuilder.append(photoEntry.imageId);
                        stringBuilder.append(str2);
                        stringBuilder.append(photoEntry.path);
                        backupImageView.setImage(stringBuilder.toString(), null, access$000.getContext().getResources().getDrawable(C1067R.C1065drawable.nophotos));
                        return;
                    }
                    backupImageView = access$000.photoImage;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("thumb://");
                    stringBuilder.append(photoEntry.imageId);
                    stringBuilder.append(str2);
                    stringBuilder.append(photoEntry.path);
                    backupImageView.setImage(stringBuilder.toString(), null, access$000.getContext().getResources().getDrawable(C1067R.C1065drawable.nophotos));
                    return;
                } else {
                    access$000.photoImage.setImageResource(C1067R.C1065drawable.nophotos);
                    return;
                }
            }
            ArrayList access$500;
            if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                access$500 = PhotoPickerActivity.this.recentImages;
            } else {
                access$500 = PhotoPickerActivity.this.searchResult;
            }
            access$000.setImage((SearchImage) access$500.get(i));
        }

        public boolean allowCaption() {
            return PhotoPickerActivity.this.allowCaption;
        }

        public BitmapHolder getThumbForPhoto(MessageObject messageObject, FileLocation fileLocation, int i) {
            PhotoPickerPhotoCell access$000 = PhotoPickerActivity.this.getCellForIndex(i);
            return access$000 != null ? access$000.photoImage.getImageReceiver().getBitmapSafe() : null;
        }

        public void willSwitchFromPhoto(MessageObject messageObject, FileLocation fileLocation, int i) {
            int childCount = PhotoPickerActivity.this.listView.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt = PhotoPickerActivity.this.listView.getChildAt(i2);
                if (childAt.getTag() != null) {
                    PhotoPickerPhotoCell photoPickerPhotoCell = (PhotoPickerPhotoCell) childAt;
                    int intValue = ((Integer) childAt.getTag()).intValue();
                    if (PhotoPickerActivity.this.selectedAlbum == null) {
                        ArrayList access$500;
                        if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                            access$500 = PhotoPickerActivity.this.recentImages;
                        } else {
                            access$500 = PhotoPickerActivity.this.searchResult;
                        }
                        if (intValue < 0) {
                            continue;
                        } else if (intValue >= access$500.size()) {
                            continue;
                        }
                    } else if (intValue < 0) {
                        continue;
                    } else if (intValue >= PhotoPickerActivity.this.selectedAlbum.photos.size()) {
                        continue;
                    }
                    if (intValue == i) {
                        photoPickerPhotoCell.showCheck(true);
                        return;
                    }
                }
            }
        }

        public void willHidePhotoViewer() {
            int childCount = PhotoPickerActivity.this.listView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = PhotoPickerActivity.this.listView.getChildAt(i);
                if (childAt instanceof PhotoPickerPhotoCell) {
                    ((PhotoPickerPhotoCell) childAt).showCheck(true);
                }
            }
        }

        public boolean isPhotoChecked(int i) {
            boolean z = true;
            if (PhotoPickerActivity.this.selectedAlbum != null) {
                if (i < 0 || i >= PhotoPickerActivity.this.selectedAlbum.photos.size() || !PhotoPickerActivity.this.selectedPhotos.containsKey(Integer.valueOf(((PhotoEntry) PhotoPickerActivity.this.selectedAlbum.photos.get(i)).imageId))) {
                    z = false;
                }
                return z;
            }
            ArrayList access$500;
            if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                access$500 = PhotoPickerActivity.this.recentImages;
            } else {
                access$500 = PhotoPickerActivity.this.searchResult;
            }
            if (i < 0 || i >= access$500.size() || !PhotoPickerActivity.this.selectedPhotos.containsKey(((SearchImage) access$500.get(i)).f55id)) {
                z = false;
            }
            return z;
        }

        public int setPhotoUnchecked(Object obj) {
            obj = obj instanceof PhotoEntry ? Integer.valueOf(((PhotoEntry) obj).imageId) : obj instanceof SearchImage ? ((SearchImage) obj).f55id : null;
            if (obj == null || !PhotoPickerActivity.this.selectedPhotos.containsKey(obj)) {
                return -1;
            }
            PhotoPickerActivity.this.selectedPhotos.remove(obj);
            int indexOf = PhotoPickerActivity.this.selectedPhotosOrder.indexOf(obj);
            if (indexOf >= 0) {
                PhotoPickerActivity.this.selectedPhotosOrder.remove(indexOf);
            }
            if (PhotoPickerActivity.this.allowIndices) {
                PhotoPickerActivity.this.updateCheckedPhotoIndices();
            }
            return indexOf;
        }

        public int setPhotoChecked(int i, VideoEditedInfo videoEditedInfo) {
            int indexOf;
            boolean z;
            int access$1100;
            int i2 = -1;
            if (PhotoPickerActivity.this.selectedAlbum == null) {
                ArrayList access$500;
                if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                    access$500 = PhotoPickerActivity.this.recentImages;
                } else {
                    access$500 = PhotoPickerActivity.this.searchResult;
                }
                if (i < 0 || i >= access$500.size()) {
                    return -1;
                }
                SearchImage searchImage = (SearchImage) access$500.get(i);
                int access$11002 = PhotoPickerActivity.this.addToSelectedPhotos(searchImage, -1);
                if (access$11002 == -1) {
                    indexOf = PhotoPickerActivity.this.selectedPhotosOrder.indexOf(searchImage.f55id);
                    z = true;
                } else {
                    indexOf = access$11002;
                    z = false;
                }
            } else if (i < 0 || i >= PhotoPickerActivity.this.selectedAlbum.photos.size()) {
                return -1;
            } else {
                boolean z2;
                PhotoEntry photoEntry = (PhotoEntry) PhotoPickerActivity.this.selectedAlbum.photos.get(i);
                access$1100 = PhotoPickerActivity.this.addToSelectedPhotos(photoEntry, -1);
                if (access$1100 == -1) {
                    photoEntry.editedInfo = videoEditedInfo;
                    access$1100 = PhotoPickerActivity.this.selectedPhotosOrder.indexOf(Integer.valueOf(photoEntry.imageId));
                    z2 = true;
                } else {
                    photoEntry.editedInfo = null;
                    z2 = false;
                }
                z = z2;
                indexOf = access$1100;
            }
            access$1100 = PhotoPickerActivity.this.listView.getChildCount();
            for (int i3 = 0; i3 < access$1100; i3++) {
                View childAt = PhotoPickerActivity.this.listView.getChildAt(i3);
                if (((Integer) childAt.getTag()).intValue() == i) {
                    PhotoPickerPhotoCell photoPickerPhotoCell = (PhotoPickerPhotoCell) childAt;
                    if (PhotoPickerActivity.this.allowIndices) {
                        i2 = indexOf;
                    }
                    photoPickerPhotoCell.setChecked(i2, z, false);
                    PhotoPickerActivity.this.pickerBottomLayout.updateSelectedCount(PhotoPickerActivity.this.selectedPhotos.size(), true);
                    PhotoPickerActivity.this.delegate.selectedPhotosChanged();
                    return indexOf;
                }
            }
            PhotoPickerActivity.this.pickerBottomLayout.updateSelectedCount(PhotoPickerActivity.this.selectedPhotos.size(), true);
            PhotoPickerActivity.this.delegate.selectedPhotosChanged();
            return indexOf;
        }

        public boolean cancelButtonPressed() {
            PhotoPickerActivity.this.delegate.actionButtonPressed(true);
            PhotoPickerActivity.this.finishFragment();
            return true;
        }

        public int getSelectedCount() {
            return PhotoPickerActivity.this.selectedPhotos.size();
        }

        public void sendButtonPressed(int i, VideoEditedInfo videoEditedInfo) {
            if (PhotoPickerActivity.this.selectedPhotos.isEmpty()) {
                if (PhotoPickerActivity.this.selectedAlbum == null) {
                    ArrayList access$500;
                    if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                        access$500 = PhotoPickerActivity.this.recentImages;
                    } else {
                        access$500 = PhotoPickerActivity.this.searchResult;
                    }
                    if (i >= 0 && i < access$500.size()) {
                        PhotoPickerActivity.this.addToSelectedPhotos(access$500.get(i), -1);
                    } else {
                        return;
                    }
                } else if (i >= 0 && i < PhotoPickerActivity.this.selectedAlbum.photos.size()) {
                    PhotoEntry photoEntry = (PhotoEntry) PhotoPickerActivity.this.selectedAlbum.photos.get(i);
                    photoEntry.editedInfo = videoEditedInfo;
                    PhotoPickerActivity.this.addToSelectedPhotos(photoEntry, -1);
                } else {
                    return;
                }
            }
            PhotoPickerActivity.this.sendSelectedPhotos();
        }

        public void toggleGroupPhotosEnabled() {
            if (PhotoPickerActivity.this.imageOrderToggleButton != null) {
                PhotoPickerActivity.this.imageOrderToggleButton.setColorFilter(SharedConfig.groupPhotosEnabled ? new PorterDuffColorFilter(-10043398, Mode.MULTIPLY) : null);
            }
        }

        public ArrayList<Object> getSelectedPhotosOrder() {
            return PhotoPickerActivity.this.selectedPhotosOrder;
        }

        public HashMap<Object, Object> getSelectedPhotos() {
            return PhotoPickerActivity.this.selectedPhotos;
        }

        public boolean allowGroupPhotos() {
            return PhotoPickerActivity.this.imageOrderToggleButton != null;
        }
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$ListAdapter */
    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public long getItemId(int i) {
            return (long) i;
        }

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public boolean isEnabled(ViewHolder viewHolder) {
            boolean z = true;
            if (PhotoPickerActivity.this.selectedAlbum == null) {
                int adapterPosition = viewHolder.getAdapterPosition();
                if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                    if (adapterPosition >= PhotoPickerActivity.this.recentImages.size()) {
                        z = false;
                    }
                    return z;
                } else if (adapterPosition >= PhotoPickerActivity.this.searchResult.size()) {
                    z = false;
                }
            }
            return z;
        }

        public int getItemCount() {
            if (PhotoPickerActivity.this.selectedAlbum != null) {
                return PhotoPickerActivity.this.selectedAlbum.photos.size();
            }
            if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                return PhotoPickerActivity.this.recentImages.size();
            }
            return PhotoPickerActivity.this.searchResult.size() + (PhotoPickerActivity.this.imageSearchEndReached ^ 1);
        }

        public /* synthetic */ void lambda$onCreateViewHolder$0$PhotoPickerActivity$ListAdapter(View view) {
            int intValue = ((Integer) ((View) view.getParent()).getTag()).intValue();
            int i = -1;
            int containsKey;
            if (PhotoPickerActivity.this.selectedAlbum != null) {
                PhotoEntry photoEntry = (PhotoEntry) PhotoPickerActivity.this.selectedAlbum.photos.get(intValue);
                containsKey = PhotoPickerActivity.this.selectedPhotos.containsKey(Integer.valueOf(photoEntry.imageId)) ^ 1;
                if (containsKey == 0 || PhotoPickerActivity.this.maxSelectedPhotos <= 0 || PhotoPickerActivity.this.selectedPhotos.size() < PhotoPickerActivity.this.maxSelectedPhotos) {
                    if (PhotoPickerActivity.this.allowIndices && containsKey != 0) {
                        i = PhotoPickerActivity.this.selectedPhotosOrder.size();
                    }
                    ((PhotoPickerPhotoCell) view.getParent()).setChecked(i, containsKey, true);
                    PhotoPickerActivity.this.addToSelectedPhotos(photoEntry, intValue);
                } else {
                    return;
                }
            }
            SearchImage searchImage;
            AndroidUtilities.hideKeyboard(PhotoPickerActivity.this.getParentActivity().getCurrentFocus());
            if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                searchImage = (SearchImage) PhotoPickerActivity.this.recentImages.get(((Integer) ((View) view.getParent()).getTag()).intValue());
            } else {
                searchImage = (SearchImage) PhotoPickerActivity.this.searchResult.get(((Integer) ((View) view.getParent()).getTag()).intValue());
            }
            containsKey = PhotoPickerActivity.this.selectedPhotos.containsKey(searchImage.f55id) ^ 1;
            if (containsKey == 0 || PhotoPickerActivity.this.maxSelectedPhotos <= 0 || PhotoPickerActivity.this.selectedPhotos.size() < PhotoPickerActivity.this.maxSelectedPhotos) {
                if (PhotoPickerActivity.this.allowIndices && containsKey != 0) {
                    i = PhotoPickerActivity.this.selectedPhotosOrder.size();
                }
                ((PhotoPickerPhotoCell) view.getParent()).setChecked(i, containsKey, true);
                PhotoPickerActivity.this.addToSelectedPhotos(searchImage, intValue);
            } else {
                return;
            }
            PhotoPickerActivity.this.pickerBottomLayout.updateSelectedCount(PhotoPickerActivity.this.selectedPhotos.size(), true);
            PhotoPickerActivity.this.delegate.selectedPhotosChanged();
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View frameLayout;
            if (i != 0) {
                frameLayout = new FrameLayout(this.mContext);
                RadialProgressView radialProgressView = new RadialProgressView(this.mContext);
                radialProgressView.setProgressColor(-1);
                frameLayout.addView(radialProgressView, LayoutHelper.createFrame(-1, -1.0f));
            } else {
                frameLayout = new PhotoPickerPhotoCell(this.mContext, true);
                frameLayout.checkFrame.setOnClickListener(new C1836x65aec827(this));
                frameLayout.checkFrame.setVisibility(PhotoPickerActivity.this.selectPhotoType != 0 ? 8 : 0);
            }
            return new Holder(frameLayout);
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                boolean isShowingImage;
                PhotoPickerPhotoCell photoPickerPhotoCell = (PhotoPickerPhotoCell) viewHolder.itemView;
                photoPickerPhotoCell.itemWidth = PhotoPickerActivity.this.itemWidth;
                BackupImageView backupImageView = photoPickerPhotoCell.photoImage;
                backupImageView.setTag(Integer.valueOf(i));
                photoPickerPhotoCell.setTag(Integer.valueOf(i));
                int i2 = 0;
                backupImageView.setOrientation(0, true);
                int i3 = -1;
                if (PhotoPickerActivity.this.selectedAlbum != null) {
                    PhotoEntry photoEntry = (PhotoEntry) PhotoPickerActivity.this.selectedAlbum.photos.get(i);
                    String str = photoEntry.thumbPath;
                    if (str != null) {
                        backupImageView.setImage(str, null, this.mContext.getResources().getDrawable(C1067R.C1065drawable.nophotos));
                    } else if (photoEntry.path != null) {
                        backupImageView.setOrientation(photoEntry.orientation, true);
                        String str2 = ":";
                        StringBuilder stringBuilder;
                        if (photoEntry.isVideo) {
                            photoPickerPhotoCell.videoInfoContainer.setVisibility(0);
                            int i4 = photoEntry.duration;
                            i4 -= (i4 / 60) * 60;
                            photoPickerPhotoCell.videoTextView.setText(String.format("%d:%02d", new Object[]{Integer.valueOf(r5), Integer.valueOf(i4)}));
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(LocaleController.getString("AttachVideo", C1067R.string.AttachVideo));
                            stringBuilder.append(", ");
                            stringBuilder.append(LocaleController.formatCallDuration(photoEntry.duration));
                            photoPickerPhotoCell.setContentDescription(stringBuilder.toString());
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("vthumb://");
                            stringBuilder.append(photoEntry.imageId);
                            stringBuilder.append(str2);
                            stringBuilder.append(photoEntry.path);
                            backupImageView.setImage(stringBuilder.toString(), null, this.mContext.getResources().getDrawable(C1067R.C1065drawable.nophotos));
                        } else {
                            photoPickerPhotoCell.videoInfoContainer.setVisibility(4);
                            photoPickerPhotoCell.setContentDescription(LocaleController.getString("AttachPhoto", C1067R.string.AttachPhoto));
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("thumb://");
                            stringBuilder.append(photoEntry.imageId);
                            stringBuilder.append(str2);
                            stringBuilder.append(photoEntry.path);
                            backupImageView.setImage(stringBuilder.toString(), null, this.mContext.getResources().getDrawable(C1067R.C1065drawable.nophotos));
                        }
                    } else {
                        backupImageView.setImageResource(C1067R.C1065drawable.nophotos);
                    }
                    if (PhotoPickerActivity.this.allowIndices) {
                        i3 = PhotoPickerActivity.this.selectedPhotosOrder.indexOf(Integer.valueOf(photoEntry.imageId));
                    }
                    photoPickerPhotoCell.setChecked(i3, PhotoPickerActivity.this.selectedPhotos.containsKey(Integer.valueOf(photoEntry.imageId)), false);
                    isShowingImage = PhotoViewer.isShowingImage(photoEntry.path);
                } else {
                    SearchImage searchImage;
                    if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                        searchImage = (SearchImage) PhotoPickerActivity.this.recentImages.get(i);
                    } else {
                        searchImage = (SearchImage) PhotoPickerActivity.this.searchResult.get(i);
                    }
                    photoPickerPhotoCell.setImage(searchImage);
                    photoPickerPhotoCell.videoInfoContainer.setVisibility(4);
                    if (PhotoPickerActivity.this.allowIndices) {
                        i3 = PhotoPickerActivity.this.selectedPhotosOrder.indexOf(searchImage.f55id);
                    }
                    photoPickerPhotoCell.setChecked(i3, PhotoPickerActivity.this.selectedPhotos.containsKey(searchImage.f55id), false);
                    isShowingImage = PhotoViewer.isShowingImage(searchImage.getPathToAttach());
                }
                backupImageView.getImageReceiver().setVisible(!isShowingImage, true);
                CheckBox checkBox = photoPickerPhotoCell.checkBox;
                if (PhotoPickerActivity.this.selectPhotoType != 0 || isShowingImage) {
                    i2 = 8;
                }
                checkBox.setVisibility(i2);
            } else if (itemViewType == 1) {
                LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.width = PhotoPickerActivity.this.itemWidth;
                    layoutParams.height = PhotoPickerActivity.this.itemWidth;
                    viewHolder.itemView.setLayoutParams(layoutParams);
                }
            }
        }

        public int getItemViewType(int i) {
            return (PhotoPickerActivity.this.selectedAlbum != null || ((PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null && i < PhotoPickerActivity.this.recentImages.size()) || i < PhotoPickerActivity.this.searchResult.size())) ? 0 : 1;
        }
    }

    public PhotoPickerActivity(int i, AlbumEntry albumEntry, HashMap<Object, Object> hashMap, ArrayList<Object> arrayList, ArrayList<SearchImage> arrayList2, int i2, boolean z, ChatActivity chatActivity) {
        this.selectedAlbum = albumEntry;
        this.selectedPhotos = hashMap;
        this.selectedPhotosOrder = arrayList;
        this.type = i;
        this.recentImages = arrayList2;
        this.selectPhotoType = i2;
        this.chatActivity = chatActivity;
        this.allowCaption = z;
    }

    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recentImagesDidLoad);
        if (this.selectedAlbum == null && this.recentImages.isEmpty()) {
            MessagesStorage.getInstance(this.currentAccount).loadWebRecent(this.type);
            this.loadingRecent = true;
        }
        return super.onFragmentCreate();
    }

    public void onFragmentDestroy() {
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recentImagesDidLoad);
        if (this.imageReqId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.imageReqId, true);
            this.imageReqId = 0;
        }
        super.onFragmentDestroy();
    }

    public View createView(Context context) {
        int i;
        Context context2 = context;
        this.actionBar.setBackgroundColor(Theme.ACTION_BAR_MEDIA_PICKER_COLOR);
        boolean z = false;
        this.actionBar.setItemsBackgroundColor(Theme.ACTION_BAR_PICKER_SELECTOR_COLOR, false);
        this.actionBar.setTitleColor(-1);
        this.actionBar.setItemsColor(-1, false);
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        AlbumEntry albumEntry = this.selectedAlbum;
        String str = "SearchGifsTitle";
        String str2 = "SearchImagesTitle";
        if (albumEntry != null) {
            this.actionBar.setTitle(albumEntry.bucketName);
        } else {
            i = this.type;
            if (i == 0) {
                this.actionBar.setTitle(LocaleController.getString(str2, C1067R.string.SearchImagesTitle));
            } else if (i == 1) {
                this.actionBar.setTitle(LocaleController.getString(str, C1067R.string.SearchGifsTitle));
            }
        }
        this.actionBar.setActionBarMenuOnItemClick(new C42752());
        if (this.selectedAlbum == null) {
            this.searchItem = this.actionBar.createMenu().addItem(0, (int) C1067R.C1065drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new C42763());
        }
        if (this.selectedAlbum == null) {
            i = this.type;
            if (i == 0) {
                this.searchItem.setSearchFieldHint(LocaleController.getString(str2, C1067R.string.SearchImagesTitle));
            } else if (i == 1) {
                this.searchItem.setSearchFieldHint(LocaleController.getString(str, C1067R.string.SearchGifsTitle));
            }
        }
        this.fragmentView = new FrameLayout(context2);
        this.frameLayout = (FrameLayout) this.fragmentView;
        this.frameLayout.setBackgroundColor(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
        this.listView = new RecyclerListView(context2);
        this.listView.setPadding(AndroidUtilities.m26dp(4.0f), AndroidUtilities.m26dp(4.0f), AndroidUtilities.m26dp(4.0f), AndroidUtilities.m26dp(4.0f));
        this.listView.setClipToPadding(false);
        this.listView.setHorizontalScrollBarEnabled(false);
        this.listView.setVerticalScrollBarEnabled(false);
        ColorFilter colorFilter = null;
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation(null);
        RecyclerListView recyclerListView = this.listView;
        C42804 c42804 = new GridLayoutManager(context2, 4) {
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
        this.layoutManager = c42804;
        recyclerListView.setLayoutManager(c42804);
        this.listView.addItemDecoration(new C42775());
        this.frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, this.selectPhotoType != 0 ? 0.0f : 48.0f));
        recyclerListView = this.listView;
        ListAdapter listAdapter = new ListAdapter(context2);
        this.listAdapter = listAdapter;
        recyclerListView.setAdapter(listAdapter);
        this.listView.setGlowColor(Theme.ACTION_BAR_MEDIA_PICKER_COLOR);
        this.listView.setOnItemClickListener(new C3815-$$Lambda$PhotoPickerActivity$zD_mr7IpQctZxAyf7EKDChtrGZc(this));
        if (this.selectedAlbum == null) {
            this.listView.setOnItemLongClickListener(new C3813-$$Lambda$PhotoPickerActivity$Hdsa1stRiohWHt8IIV2h-tg2fmI(this));
        }
        this.emptyView = new EmptyTextProgressView(context2);
        this.emptyView.setTextColor(-8355712);
        this.emptyView.setProgressBarColor(-1);
        this.emptyView.setShowAtCenter(true);
        if (this.selectedAlbum != null) {
            this.emptyView.setText(LocaleController.getString("NoPhotos", C1067R.string.NoPhotos));
        } else {
            this.emptyView.setText("");
        }
        this.frameLayout.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, this.selectPhotoType != 0 ? 0.0f : 48.0f));
        if (this.selectedAlbum == null) {
            this.listView.setOnScrollListener(new C42786());
            updateSearchInterface();
        }
        this.pickerBottomLayout = new PickerBottomLayout(context2);
        this.frameLayout.addView(this.pickerBottomLayout, LayoutHelper.createFrame(-1, 48, 80));
        this.pickerBottomLayout.cancelButton.setOnClickListener(new C1835-$$Lambda$PhotoPickerActivity$KmEu4SYAqKOt718fgSd59sdqoWM(this));
        this.pickerBottomLayout.doneButton.setOnClickListener(new C1842-$$Lambda$PhotoPickerActivity$mGdvO16vUkJ4QAJ7aZX42E0io7E(this));
        if (this.selectPhotoType != 0) {
            this.pickerBottomLayout.setVisibility(8);
        } else if (this.selectedAlbum != null || this.type == 0) {
            ChatActivity chatActivity = this.chatActivity;
            if (chatActivity != null && chatActivity.allowGroupPhotos()) {
                String str3;
                this.imageOrderToggleButton = new ImageView(context2);
                this.imageOrderToggleButton.setScaleType(ScaleType.CENTER);
                this.imageOrderToggleButton.setImageResource(C1067R.C1065drawable.photos_group);
                ImageView imageView = this.imageOrderToggleButton;
                if (SharedConfig.groupPhotosEnabled) {
                    i = C1067R.string.GroupPhotosHelp;
                    str3 = "GroupPhotosHelp";
                } else {
                    i = C1067R.string.SinglePhotosHelp;
                    str3 = "SinglePhotosHelp";
                }
                imageView.setContentDescription(LocaleController.getString(str3, i));
                this.pickerBottomLayout.addView(this.imageOrderToggleButton, LayoutHelper.createFrame(48, -1, 17));
                this.imageOrderToggleButton.setOnClickListener(new C1840-$$Lambda$PhotoPickerActivity$dTiSUg75sRfWPRlo26sk1jZD--Q(this));
                imageView = this.imageOrderToggleButton;
                if (SharedConfig.groupPhotosEnabled) {
                    colorFilter = new PorterDuffColorFilter(-10043398, Mode.MULTIPLY);
                }
                imageView.setColorFilter(colorFilter);
            }
        }
        if ((this.selectedAlbum != null || this.type == 0) && this.maxSelectedPhotos <= 0) {
            z = true;
        }
        this.allowIndices = z;
        this.listView.setEmptyView(this.emptyView);
        this.pickerBottomLayout.updateSelectedCount(this.selectedPhotos.size(), true);
        return this.fragmentView;
    }

    public /* synthetic */ void lambda$createView$0$PhotoPickerActivity(View view, int i) {
        ArrayList arrayList;
        AlbumEntry albumEntry = this.selectedAlbum;
        if (albumEntry != null) {
            arrayList = albumEntry.photos;
        } else if (this.searchResult.isEmpty() && this.lastSearchString == null) {
            arrayList = this.recentImages;
        } else {
            arrayList = this.searchResult;
        }
        ArrayList arrayList2 = arrayList;
        if (i >= 0 && i < arrayList2.size()) {
            ActionBarMenuItem actionBarMenuItem = this.searchItem;
            if (actionBarMenuItem != null) {
                AndroidUtilities.hideKeyboard(actionBarMenuItem.getSearchField());
            }
            int i2 = this.selectPhotoType;
            int i3 = i2 == 1 ? 1 : i2 == 2 ? 3 : this.chatActivity == null ? 4 : 0;
            PhotoViewer.getInstance().setParentActivity(getParentActivity());
            PhotoViewer.getInstance().setMaxSelectedPhotos(this.maxSelectedPhotos);
            PhotoViewer.getInstance().openPhotoForSelect(arrayList2, i, i3, this.provider, this.chatActivity);
        }
    }

    public /* synthetic */ boolean lambda$createView$2$PhotoPickerActivity(View view, int i) {
        if (!this.searchResult.isEmpty() || this.lastSearchString != null) {
            return false;
        }
        Builder builder = new Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
        builder.setMessage(LocaleController.getString("ClearSearch", C1067R.string.ClearSearch));
        builder.setPositiveButton(LocaleController.getString("ClearButton", C1067R.string.ClearButton).toUpperCase(), new C1838-$$Lambda$PhotoPickerActivity$V8BRocCcdRNwJ4kn9l4217_mJco(this));
        builder.setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null);
        showDialog(builder.create());
        return true;
    }

    public /* synthetic */ void lambda$null$1$PhotoPickerActivity(DialogInterface dialogInterface, int i) {
        this.recentImages.clear();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        MessagesStorage.getInstance(this.currentAccount).clearWebRecent(this.type);
    }

    public /* synthetic */ void lambda$createView$3$PhotoPickerActivity(View view) {
        this.delegate.actionButtonPressed(true);
        finishFragment();
    }

    public /* synthetic */ void lambda$createView$4$PhotoPickerActivity(View view) {
        sendSelectedPhotos();
    }

    public /* synthetic */ void lambda$createView$5$PhotoPickerActivity(View view) {
        int i;
        String str;
        SharedConfig.toggleGroupPhotosEnabled();
        this.imageOrderToggleButton.setColorFilter(SharedConfig.groupPhotosEnabled ? new PorterDuffColorFilter(-10043398, Mode.MULTIPLY) : null);
        showHint(false, SharedConfig.groupPhotosEnabled);
        updateCheckedPhotoIndices();
        ImageView imageView = this.imageOrderToggleButton;
        if (SharedConfig.groupPhotosEnabled) {
            i = C1067R.string.GroupPhotosHelp;
            str = "GroupPhotosHelp";
        } else {
            i = C1067R.string.SinglePhotosHelp;
            str = "SinglePhotosHelp";
        }
        imageView.setContentDescription(LocaleController.getString(str, i));
    }

    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        ActionBarMenuItem actionBarMenuItem = this.searchItem;
        if (actionBarMenuItem != null) {
            actionBarMenuItem.openSearch(true);
            if (!TextUtils.isEmpty(this.initialSearchString)) {
                this.searchItem.setSearchFieldText(this.initialSearchString, false);
                this.initialSearchString = null;
                processSearch(this.searchItem.getSearchField());
            }
            getParentActivity().getWindow().setSoftInputMode(32);
        }
        fixLayout();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        fixLayout();
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.closeChats) {
            removeSelfFromStack();
        } else if (i == NotificationCenter.recentImagesDidLoad && this.selectedAlbum == null && this.type == ((Integer) objArr[0]).intValue()) {
            this.recentImages = (ArrayList) objArr[1];
            this.loadingRecent = false;
            updateSearchInterface();
        }
    }

    private void hideHint() {
        this.hintAnimation = new AnimatorSet();
        AnimatorSet animatorSet = this.hintAnimation;
        Animator[] animatorArr = new Animator[1];
        animatorArr[0] = ObjectAnimator.ofFloat(this.hintTextView, "alpha", new float[]{0.0f});
        animatorSet.playTogether(animatorArr);
        this.hintAnimation.addListener(new C31557());
        this.hintAnimation.setDuration(300);
        this.hintAnimation.start();
    }

    public void setInitialSearchString(String str) {
        this.initialSearchString = str;
    }

    private void processSearch(EditText editText) {
        if (editText.getText().toString().length() != 0) {
            this.searchResult.clear();
            this.searchResultKeys.clear();
            this.imageSearchEndReached = true;
            String str = "";
            searchImages(this.type == 1, editText.getText().toString(), str, true);
            this.lastSearchString = editText.getText().toString();
            if (this.lastSearchString.length() == 0) {
                this.lastSearchString = null;
                this.emptyView.setText(str);
            } else {
                this.emptyView.setText(LocaleController.getString("NoResult", C1067R.string.NoResult));
            }
            updateSearchInterface();
        }
    }

    public void setMaxSelectedPhotos(int i) {
        this.maxSelectedPhotos = i;
    }

    private void showHint(boolean z, boolean z2) {
        if (!(getParentActivity() == null || this.fragmentView == null || (z && this.hintTextView == null))) {
            if (this.hintTextView == null) {
                this.hintTextView = new TextView(getParentActivity());
                this.hintTextView.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.m26dp(3.0f), Theme.getColor(Theme.key_chat_gifSaveHintBackground)));
                this.hintTextView.setTextColor(Theme.getColor(Theme.key_chat_gifSaveHintText));
                this.hintTextView.setTextSize(1, 14.0f);
                this.hintTextView.setPadding(AndroidUtilities.m26dp(8.0f), AndroidUtilities.m26dp(7.0f), AndroidUtilities.m26dp(8.0f), AndroidUtilities.m26dp(7.0f));
                this.hintTextView.setGravity(16);
                this.hintTextView.setAlpha(0.0f);
                this.frameLayout.addView(this.hintTextView, LayoutHelper.createFrame(-2, -2.0f, 81, 5.0f, 0.0f, 5.0f, 51.0f));
            }
            AnimatorSet animatorSet;
            if (z) {
                animatorSet = this.hintAnimation;
                if (animatorSet != null) {
                    animatorSet.cancel();
                    this.hintAnimation = null;
                }
                AndroidUtilities.cancelRunOnUIThread(this.hintHideRunnable);
                this.hintHideRunnable = null;
                hideHint();
                return;
            }
            int i;
            String str;
            TextView textView = this.hintTextView;
            if (z2) {
                i = C1067R.string.GroupPhotosHelp;
                str = "GroupPhotosHelp";
            } else {
                i = C1067R.string.SinglePhotosHelp;
                str = "SinglePhotosHelp";
            }
            textView.setText(LocaleController.getString(str, i));
            Runnable runnable = this.hintHideRunnable;
            if (runnable != null) {
                AnimatorSet animatorSet2 = this.hintAnimation;
                if (animatorSet2 != null) {
                    animatorSet2.cancel();
                    this.hintAnimation = null;
                } else {
                    AndroidUtilities.cancelRunOnUIThread(runnable);
                    C1839-$$Lambda$PhotoPickerActivity$aYB7K3WSG2cRcI6ke4K4lfSNCNw c1839-$$Lambda$PhotoPickerActivity$aYB7K3WSG2cRcI6ke4K4lfSNCNw = new C1839-$$Lambda$PhotoPickerActivity$aYB7K3WSG2cRcI6ke4K4lfSNCNw(this);
                    this.hintHideRunnable = c1839-$$Lambda$PhotoPickerActivity$aYB7K3WSG2cRcI6ke4K4lfSNCNw;
                    AndroidUtilities.runOnUIThread(c1839-$$Lambda$PhotoPickerActivity$aYB7K3WSG2cRcI6ke4K4lfSNCNw, 2000);
                    return;
                }
            } else if (this.hintAnimation != null) {
                return;
            }
            this.hintTextView.setVisibility(0);
            this.hintAnimation = new AnimatorSet();
            animatorSet = this.hintAnimation;
            Animator[] animatorArr = new Animator[1];
            animatorArr[0] = ObjectAnimator.ofFloat(this.hintTextView, "alpha", new float[]{1.0f});
            animatorSet.playTogether(animatorArr);
            this.hintAnimation.addListener(new C31568());
            this.hintAnimation.setDuration(300);
            this.hintAnimation.start();
        }
    }

    private void updateCheckedPhotoIndices() {
        if (this.allowIndices) {
            int childCount = this.listView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.listView.getChildAt(i);
                if (childAt instanceof PhotoPickerPhotoCell) {
                    PhotoPickerPhotoCell photoPickerPhotoCell = (PhotoPickerPhotoCell) childAt;
                    Integer num = (Integer) photoPickerPhotoCell.getTag();
                    AlbumEntry albumEntry = this.selectedAlbum;
                    int i2 = -1;
                    if (albumEntry != null) {
                        PhotoEntry photoEntry = (PhotoEntry) albumEntry.photos.get(num.intValue());
                        if (this.allowIndices) {
                            i2 = this.selectedPhotosOrder.indexOf(Integer.valueOf(photoEntry.imageId));
                        }
                        photoPickerPhotoCell.setNum(i2);
                    } else {
                        SearchImage searchImage;
                        if (this.searchResult.isEmpty() && this.lastSearchString == null) {
                            searchImage = (SearchImage) this.recentImages.get(num.intValue());
                        } else {
                            searchImage = (SearchImage) this.searchResult.get(num.intValue());
                        }
                        if (this.allowIndices) {
                            i2 = this.selectedPhotosOrder.indexOf(searchImage.f55id);
                        }
                        photoPickerPhotoCell.setNum(i2);
                    }
                }
            }
        }
    }

    private PhotoPickerPhotoCell getCellForIndex(int i) {
        int childCount = this.listView.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = this.listView.getChildAt(i2);
            if (childAt instanceof PhotoPickerPhotoCell) {
                PhotoPickerPhotoCell photoPickerPhotoCell = (PhotoPickerPhotoCell) childAt;
                int intValue = ((Integer) photoPickerPhotoCell.photoImage.getTag()).intValue();
                AlbumEntry albumEntry = this.selectedAlbum;
                if (albumEntry == null) {
                    ArrayList arrayList;
                    if (this.searchResult.isEmpty() && this.lastSearchString == null) {
                        arrayList = this.recentImages;
                    } else {
                        arrayList = this.searchResult;
                    }
                    if (intValue < 0) {
                        continue;
                    } else if (intValue >= arrayList.size()) {
                        continue;
                    }
                } else if (intValue < 0) {
                    continue;
                } else if (intValue >= albumEntry.photos.size()) {
                    continue;
                }
                if (intValue == i) {
                    return photoPickerPhotoCell;
                }
            }
        }
        return null;
    }

    private int addToSelectedPhotos(Object obj, int i) {
        boolean z = obj instanceof PhotoEntry;
        Object valueOf = z ? Integer.valueOf(((PhotoEntry) obj).imageId) : obj instanceof SearchImage ? ((SearchImage) obj).f55id : null;
        if (valueOf == null) {
            return -1;
        }
        if (this.selectedPhotos.containsKey(valueOf)) {
            this.selectedPhotos.remove(valueOf);
            int indexOf = this.selectedPhotosOrder.indexOf(valueOf);
            if (indexOf >= 0) {
                this.selectedPhotosOrder.remove(indexOf);
            }
            if (this.allowIndices) {
                updateCheckedPhotoIndices();
            }
            if (i >= 0) {
                if (z) {
                    ((PhotoEntry) obj).reset();
                } else if (obj instanceof SearchImage) {
                    ((SearchImage) obj).reset();
                }
                this.provider.updatePhotoAtIndex(i);
            }
            return indexOf;
        }
        this.selectedPhotos.put(valueOf, obj);
        this.selectedPhotosOrder.add(valueOf);
        return -1;
    }

    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        if (z) {
            ActionBarMenuItem actionBarMenuItem = this.searchItem;
            if (actionBarMenuItem != null) {
                AndroidUtilities.showKeyboard(actionBarMenuItem.getSearchField());
            }
        }
    }

    private void updateSearchInterface() {
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        if ((this.searching && this.searchResult.isEmpty()) || (this.loadingRecent && this.lastSearchString == null)) {
            this.emptyView.showProgress();
        } else {
            this.emptyView.showTextView();
        }
    }

    private void searchBotUser(boolean z) {
        if (!this.searchingUser) {
            this.searchingUser = true;
            TL_contacts_resolveUsername tL_contacts_resolveUsername = new TL_contacts_resolveUsername();
            tL_contacts_resolveUsername.username = z ? MessagesController.getInstance(this.currentAccount).gifSearchBot : MessagesController.getInstance(this.currentAccount).imageSearchBot;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_contacts_resolveUsername, new C3812-$$Lambda$PhotoPickerActivity$67brVZzCZFPPW5KLbmmY3AJwLD4(this, z));
        }
    }

    public /* synthetic */ void lambda$searchBotUser$7$PhotoPickerActivity(boolean z, TLObject tLObject, TL_error tL_error) {
        if (tLObject != null) {
            AndroidUtilities.runOnUIThread(new C1837-$$Lambda$PhotoPickerActivity$QV1g94ydF_QhinuWtXUlhLtLgBk(this, tLObject, z));
        }
    }

    public /* synthetic */ void lambda$null$6$PhotoPickerActivity(TLObject tLObject, boolean z) {
        TL_contacts_resolvedPeer tL_contacts_resolvedPeer = (TL_contacts_resolvedPeer) tLObject;
        MessagesController.getInstance(this.currentAccount).putUsers(tL_contacts_resolvedPeer.users, false);
        MessagesController.getInstance(this.currentAccount).putChats(tL_contacts_resolvedPeer.chats, false);
        MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(tL_contacts_resolvedPeer.users, tL_contacts_resolvedPeer.chats, true, true);
        String str = this.lastSearchImageString;
        this.lastSearchImageString = null;
        searchImages(z, str, "", false);
    }

    private void searchImages(boolean z, String str, String str2, boolean z2) {
        if (this.searching) {
            this.searching = false;
            if (this.imageReqId != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.imageReqId, true);
                this.imageReqId = 0;
            }
        }
        this.lastSearchImageString = str;
        this.searching = true;
        TLObject userOrChat = MessagesController.getInstance(this.currentAccount).getUserOrChat(z ? MessagesController.getInstance(this.currentAccount).gifSearchBot : MessagesController.getInstance(this.currentAccount).imageSearchBot);
        if (userOrChat instanceof User) {
            User user = (User) userOrChat;
            TL_messages_getInlineBotResults tL_messages_getInlineBotResults = new TL_messages_getInlineBotResults();
            if (str == null) {
                str = "";
            }
            tL_messages_getInlineBotResults.query = str;
            tL_messages_getInlineBotResults.bot = MessagesController.getInstance(this.currentAccount).getInputUser(user);
            tL_messages_getInlineBotResults.offset = str2;
            ChatActivity chatActivity = this.chatActivity;
            if (chatActivity != null) {
                int dialogId = (int) chatActivity.getDialogId();
                if (dialogId != 0) {
                    tL_messages_getInlineBotResults.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(dialogId);
                } else {
                    tL_messages_getInlineBotResults.peer = new TL_inputPeerEmpty();
                }
            } else {
                tL_messages_getInlineBotResults.peer = new TL_inputPeerEmpty();
            }
            int i = this.lastSearchToken + 1;
            this.lastSearchToken = i;
            this.imageReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getInlineBotResults, new C3814-$$Lambda$PhotoPickerActivity$uL23aS4dzDeSWXHf7OAjR3N3zqg(this, i, z));
            ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(this.imageReqId, this.classGuid);
            return;
        }
        if (z2) {
            searchBotUser(z);
        }
    }

    public /* synthetic */ void lambda$searchImages$9$PhotoPickerActivity(int i, boolean z, TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1841-$$Lambda$PhotoPickerActivity$ff4Wa8-RyuGsGlKqqm3MiT5Xaeo(this, i, tLObject, z));
    }

    public /* synthetic */ void lambda$null$8$PhotoPickerActivity(int i, TLObject tLObject, boolean z) {
        if (i == this.lastSearchToken) {
            int i2;
            i = this.searchResult.size();
            if (tLObject != null) {
                messages_BotResults messages_botresults = (messages_BotResults) tLObject;
                this.nextImagesSearchOffset = messages_botresults.next_offset;
                int size = messages_botresults.results.size();
                i2 = 0;
                for (int i3 = 0; i3 < size; i3++) {
                    BotInlineResult botInlineResult = (BotInlineResult) messages_botresults.results.get(i3);
                    if (!z) {
                        if (!"photo".equals(botInlineResult.type)) {
                        }
                    }
                    if (z) {
                        if (!"gif".equals(botInlineResult.type)) {
                        }
                    }
                    if (!this.searchResultKeys.containsKey(botInlineResult.f432id)) {
                        SearchImage searchImage = new SearchImage();
                        PhotoSize closestPhotoSizeWithSize;
                        int i4;
                        DocumentAttribute documentAttribute;
                        if (!z || botInlineResult.document == null) {
                            if (!z) {
                                Photo photo = botInlineResult.photo;
                                if (photo != null) {
                                    closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, AndroidUtilities.getPhotoSize());
                                    PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(botInlineResult.photo.sizes, 320);
                                    if (closestPhotoSizeWithSize != null) {
                                        searchImage.width = closestPhotoSizeWithSize.f465w;
                                        searchImage.height = closestPhotoSizeWithSize.f464h;
                                        searchImage.photoSize = closestPhotoSizeWithSize;
                                        searchImage.photo = botInlineResult.photo;
                                        searchImage.size = closestPhotoSizeWithSize.size;
                                        searchImage.thumbPhotoSize = closestPhotoSizeWithSize2;
                                    }
                                }
                            }
                            if (botInlineResult.content != null) {
                                for (i4 = 0; i4 < botInlineResult.content.attributes.size(); i4++) {
                                    documentAttribute = (DocumentAttribute) botInlineResult.content.attributes.get(i4);
                                    if (documentAttribute instanceof TL_documentAttributeImageSize) {
                                        searchImage.width = documentAttribute.f444w;
                                        searchImage.height = documentAttribute.f443h;
                                        break;
                                    }
                                }
                                WebDocument webDocument = botInlineResult.thumb;
                                if (webDocument != null) {
                                    searchImage.thumbUrl = webDocument.url;
                                } else {
                                    searchImage.thumbUrl = null;
                                }
                                webDocument = botInlineResult.content;
                                searchImage.imageUrl = webDocument.url;
                                if (z) {
                                    i4 = 0;
                                } else {
                                    i4 = webDocument.size;
                                }
                                searchImage.size = i4;
                            }
                        } else {
                            for (i4 = 0; i4 < botInlineResult.document.attributes.size(); i4++) {
                                documentAttribute = (DocumentAttribute) botInlineResult.document.attributes.get(i4);
                                if ((documentAttribute instanceof TL_documentAttributeImageSize) || (documentAttribute instanceof TL_documentAttributeVideo)) {
                                    searchImage.width = documentAttribute.f444w;
                                    searchImage.height = documentAttribute.f443h;
                                    break;
                                }
                            }
                            Document document = botInlineResult.document;
                            searchImage.document = document;
                            searchImage.size = 0;
                            Photo photo2 = botInlineResult.photo;
                            if (!(photo2 == null || document == null)) {
                                closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(photo2.sizes, this.itemWidth, true);
                                if (closestPhotoSizeWithSize != null) {
                                    botInlineResult.document.thumbs.add(closestPhotoSizeWithSize);
                                    document = botInlineResult.document;
                                    document.flags |= 1;
                                }
                            }
                        }
                        searchImage.f55id = botInlineResult.f432id;
                        searchImage.type = z;
                        searchImage.localUrl = "";
                        this.searchResult.add(searchImage);
                        this.searchResultKeys.put(searchImage.f55id, searchImage);
                        i2++;
                    }
                }
                boolean z2 = i == this.searchResult.size() || this.nextImagesSearchOffset == null;
                this.imageSearchEndReached = z2;
            } else {
                i2 = 0;
            }
            this.searching = false;
            if (i2 != 0) {
                this.listAdapter.notifyItemRangeInserted(i, i2);
            } else if (this.imageSearchEndReached) {
                this.listAdapter.notifyItemRemoved(this.searchResult.size() - 1);
            }
            if ((this.searching && this.searchResult.isEmpty()) || (this.loadingRecent && this.lastSearchString == null)) {
                this.emptyView.showProgress();
            } else {
                this.emptyView.showTextView();
            }
        }
    }

    public void setDelegate(PhotoPickerActivityDelegate photoPickerActivityDelegate) {
        this.delegate = photoPickerActivityDelegate;
    }

    private void sendSelectedPhotos() {
        if (!this.selectedPhotos.isEmpty()) {
            PhotoPickerActivityDelegate photoPickerActivityDelegate = this.delegate;
            if (photoPickerActivityDelegate != null && !this.sendPressed) {
                this.sendPressed = true;
                photoPickerActivityDelegate.actionButtonPressed(false);
                if (this.selectPhotoType != 2) {
                    finishFragment();
                }
            }
        }
    }

    private void fixLayout() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            recyclerListView.getViewTreeObserver().addOnPreDrawListener(new C31579());
        }
    }

    private void fixLayoutInternal() {
        if (getParentActivity() != null) {
            int findFirstVisibleItemPosition = this.layoutManager.findFirstVisibleItemPosition();
            int rotation = ((WindowManager) ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
            int i = 3;
            if (!AndroidUtilities.isTablet() && (rotation == 3 || rotation == 1)) {
                i = 5;
            }
            this.layoutManager.setSpanCount(i);
            if (AndroidUtilities.isTablet()) {
                this.itemWidth = (AndroidUtilities.m26dp(490.0f) - ((i + 1) * AndroidUtilities.m26dp(4.0f))) / i;
            } else {
                this.itemWidth = (AndroidUtilities.displaySize.x - ((i + 1) * AndroidUtilities.m26dp(4.0f))) / i;
            }
            this.listAdapter.notifyDataSetChanged();
            this.layoutManager.scrollToPosition(findFirstVisibleItemPosition);
            if (this.selectedAlbum == null) {
                this.emptyView.setPadding(0, 0, 0, (int) (((float) (AndroidUtilities.displaySize.y - C2190ActionBar.getCurrentActionBarHeight())) * 0.4f));
            }
        }
    }
}