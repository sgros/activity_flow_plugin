package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.PhotoPickerPhotoCell;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PickerBottomLayout;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.RecyclerListView;

public class PhotoPickerActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private boolean allowCaption;
   private boolean allowIndices;
   private ChatActivity chatActivity;
   private PhotoPickerActivity.PhotoPickerActivityDelegate delegate;
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
   private PhotoPickerActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private boolean loadingRecent;
   private int maxSelectedPhotos;
   private String nextImagesSearchOffset;
   private PickerBottomLayout pickerBottomLayout;
   private PhotoViewer.PhotoViewerProvider provider = new PhotoViewer.EmptyPhotoViewerProvider() {
      public boolean allowCaption() {
         return PhotoPickerActivity.this.allowCaption;
      }

      public boolean allowGroupPhotos() {
         boolean var1;
         if (PhotoPickerActivity.this.imageOrderToggleButton != null) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public boolean cancelButtonPressed() {
         PhotoPickerActivity.this.delegate.actionButtonPressed(true);
         PhotoPickerActivity.this.finishFragment();
         return true;
      }

      public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject var1, TLRPC.FileLocation var2, int var3, boolean var4) {
         PhotoPickerPhotoCell var8 = PhotoPickerActivity.this.getCellForIndex(var3);
         if (var8 != null) {
            int[] var5 = new int[2];
            var8.photoImage.getLocationInWindow(var5);
            PhotoViewer.PlaceProviderObject var7 = new PhotoViewer.PlaceProviderObject();
            var7.viewX = var5[0];
            int var6 = var5[1];
            if (VERSION.SDK_INT >= 21) {
               var3 = 0;
            } else {
               var3 = AndroidUtilities.statusBarHeight;
            }

            var7.viewY = var6 - var3;
            var7.parentView = PhotoPickerActivity.this.listView;
            var7.imageReceiver = var8.photoImage.getImageReceiver();
            var7.thumb = var7.imageReceiver.getBitmapSafe();
            var7.scale = var8.photoImage.getScaleX();
            var8.showCheck(false);
            return var7;
         } else {
            return null;
         }
      }

      public int getSelectedCount() {
         return PhotoPickerActivity.this.selectedPhotos.size();
      }

      public HashMap getSelectedPhotos() {
         return PhotoPickerActivity.this.selectedPhotos;
      }

      public ArrayList getSelectedPhotosOrder() {
         return PhotoPickerActivity.this.selectedPhotosOrder;
      }

      public ImageReceiver.BitmapHolder getThumbForPhoto(MessageObject var1, TLRPC.FileLocation var2, int var3) {
         PhotoPickerPhotoCell var4 = PhotoPickerActivity.this.getCellForIndex(var3);
         return var4 != null ? var4.photoImage.getImageReceiver().getBitmapSafe() : null;
      }

      public boolean isPhotoChecked(int var1) {
         MediaController.AlbumEntry var2 = PhotoPickerActivity.this.selectedAlbum;
         boolean var3 = true;
         boolean var4 = true;
         if (var2 != null) {
            if (var1 < 0 || var1 >= PhotoPickerActivity.this.selectedAlbum.photos.size() || !PhotoPickerActivity.this.selectedPhotos.containsKey(((MediaController.PhotoEntry)PhotoPickerActivity.this.selectedAlbum.photos.get(var1)).imageId)) {
               var4 = false;
            }

            return var4;
         } else {
            ArrayList var5;
            if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
               var5 = PhotoPickerActivity.this.recentImages;
            } else {
               var5 = PhotoPickerActivity.this.searchResult;
            }

            if (var1 >= 0 && var1 < var5.size() && PhotoPickerActivity.this.selectedPhotos.containsKey(((MediaController.SearchImage)var5.get(var1)).id)) {
               var4 = var3;
            } else {
               var4 = false;
            }

            return var4;
         }
      }

      public boolean scaleToFill() {
         return false;
      }

      public void sendButtonPressed(int var1, VideoEditedInfo var2) {
         if (PhotoPickerActivity.this.selectedPhotos.isEmpty()) {
            if (PhotoPickerActivity.this.selectedAlbum != null) {
               if (var1 < 0 || var1 >= PhotoPickerActivity.this.selectedAlbum.photos.size()) {
                  return;
               }

               MediaController.PhotoEntry var3 = (MediaController.PhotoEntry)PhotoPickerActivity.this.selectedAlbum.photos.get(var1);
               var3.editedInfo = var2;
               PhotoPickerActivity.this.addToSelectedPhotos(var3, -1);
            } else {
               ArrayList var4;
               if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                  var4 = PhotoPickerActivity.this.recentImages;
               } else {
                  var4 = PhotoPickerActivity.this.searchResult;
               }

               if (var1 < 0 || var1 >= var4.size()) {
                  return;
               }

               PhotoPickerActivity.this.addToSelectedPhotos(var4.get(var1), -1);
            }
         }

         PhotoPickerActivity.this.sendSelectedPhotos();
      }

      public int setPhotoChecked(int var1, VideoEditedInfo var2) {
         MediaController.AlbumEntry var3 = PhotoPickerActivity.this.selectedAlbum;
         byte var4 = -1;
         int var5;
         boolean var6;
         if (var3 != null) {
            if (var1 < 0 || var1 >= PhotoPickerActivity.this.selectedAlbum.photos.size()) {
               return -1;
            }

            MediaController.PhotoEntry var10 = (MediaController.PhotoEntry)PhotoPickerActivity.this.selectedAlbum.photos.get(var1);
            var5 = PhotoPickerActivity.this.addToSelectedPhotos(var10, -1);
            if (var5 == -1) {
               var10.editedInfo = var2;
               var5 = PhotoPickerActivity.this.selectedPhotosOrder.indexOf(var10.imageId);
               var6 = true;
            } else {
               var10.editedInfo = null;
               var6 = false;
            }
         } else {
            ArrayList var9;
            if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
               var9 = PhotoPickerActivity.this.recentImages;
            } else {
               var9 = PhotoPickerActivity.this.searchResult;
            }

            if (var1 < 0 || var1 >= var9.size()) {
               return -1;
            }

            MediaController.SearchImage var11 = (MediaController.SearchImage)var9.get(var1);
            var5 = PhotoPickerActivity.this.addToSelectedPhotos(var11, -1);
            if (var5 == -1) {
               var5 = PhotoPickerActivity.this.selectedPhotosOrder.indexOf(var11.id);
               var6 = true;
            } else {
               var6 = false;
            }
         }

         int var7 = PhotoPickerActivity.this.listView.getChildCount();

         for(int var8 = 0; var8 < var7; ++var8) {
            View var12 = PhotoPickerActivity.this.listView.getChildAt(var8);
            if ((Integer)var12.getTag() == var1) {
               PhotoPickerPhotoCell var13 = (PhotoPickerPhotoCell)var12;
               var1 = var4;
               if (PhotoPickerActivity.this.allowIndices) {
                  var1 = var5;
               }

               var13.setChecked(var1, var6, false);
               break;
            }
         }

         PhotoPickerActivity.this.pickerBottomLayout.updateSelectedCount(PhotoPickerActivity.this.selectedPhotos.size(), true);
         PhotoPickerActivity.this.delegate.selectedPhotosChanged();
         return var5;
      }

      public int setPhotoUnchecked(Object var1) {
         if (var1 instanceof MediaController.PhotoEntry) {
            var1 = ((MediaController.PhotoEntry)var1).imageId;
         } else if (var1 instanceof MediaController.SearchImage) {
            var1 = ((MediaController.SearchImage)var1).id;
         } else {
            var1 = null;
         }

         if (var1 == null) {
            return -1;
         } else if (PhotoPickerActivity.this.selectedPhotos.containsKey(var1)) {
            PhotoPickerActivity.this.selectedPhotos.remove(var1);
            int var2 = PhotoPickerActivity.this.selectedPhotosOrder.indexOf(var1);
            if (var2 >= 0) {
               PhotoPickerActivity.this.selectedPhotosOrder.remove(var2);
            }

            if (PhotoPickerActivity.this.allowIndices) {
               PhotoPickerActivity.this.updateCheckedPhotoIndices();
            }

            return var2;
         } else {
            return -1;
         }
      }

      public void toggleGroupPhotosEnabled() {
         if (PhotoPickerActivity.this.imageOrderToggleButton != null) {
            ImageView var1 = PhotoPickerActivity.this.imageOrderToggleButton;
            PorterDuffColorFilter var2;
            if (SharedConfig.groupPhotosEnabled) {
               var2 = new PorterDuffColorFilter(-10043398, Mode.MULTIPLY);
            } else {
               var2 = null;
            }

            var1.setColorFilter(var2);
         }

      }

      public void updatePhotoAtIndex(int var1) {
         PhotoPickerPhotoCell var2 = PhotoPickerActivity.this.getCellForIndex(var1);
         if (var2 != null) {
            if (PhotoPickerActivity.this.selectedAlbum != null) {
               var2.photoImage.setOrientation(0, true);
               MediaController.PhotoEntry var3 = (MediaController.PhotoEntry)PhotoPickerActivity.this.selectedAlbum.photos.get(var1);
               String var4 = var3.thumbPath;
               if (var4 != null) {
                  var2.photoImage.setImage(var4, (String)null, var2.getContext().getResources().getDrawable(2131165697));
               } else if (var3.path != null) {
                  var2.photoImage.setOrientation(var3.orientation, true);
                  BackupImageView var5;
                  StringBuilder var7;
                  if (var3.isVideo) {
                     var5 = var2.photoImage;
                     var7 = new StringBuilder();
                     var7.append("vthumb://");
                     var7.append(var3.imageId);
                     var7.append(":");
                     var7.append(var3.path);
                     var5.setImage(var7.toString(), (String)null, var2.getContext().getResources().getDrawable(2131165697));
                  } else {
                     var5 = var2.photoImage;
                     var7 = new StringBuilder();
                     var7.append("thumb://");
                     var7.append(var3.imageId);
                     var7.append(":");
                     var7.append(var3.path);
                     var5.setImage(var7.toString(), (String)null, var2.getContext().getResources().getDrawable(2131165697));
                  }
               } else {
                  var2.photoImage.setImageResource(2131165697);
               }
            } else {
               ArrayList var6;
               if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                  var6 = PhotoPickerActivity.this.recentImages;
               } else {
                  var6 = PhotoPickerActivity.this.searchResult;
               }

               var2.setImage((MediaController.SearchImage)var6.get(var1));
            }
         }

      }

      public void willHidePhotoViewer() {
         int var1 = PhotoPickerActivity.this.listView.getChildCount();

         for(int var2 = 0; var2 < var1; ++var2) {
            View var3 = PhotoPickerActivity.this.listView.getChildAt(var2);
            if (var3 instanceof PhotoPickerPhotoCell) {
               ((PhotoPickerPhotoCell)var3).showCheck(true);
            }
         }

      }

      public void willSwitchFromPhoto(MessageObject var1, TLRPC.FileLocation var2, int var3) {
         int var4 = PhotoPickerActivity.this.listView.getChildCount();

         for(int var5 = 0; var5 < var4; ++var5) {
            View var7 = PhotoPickerActivity.this.listView.getChildAt(var5);
            if (var7.getTag() != null) {
               PhotoPickerPhotoCell var9 = (PhotoPickerPhotoCell)var7;
               int var6 = (Integer)var7.getTag();
               if (PhotoPickerActivity.this.selectedAlbum != null) {
                  if (var6 < 0 || var6 >= PhotoPickerActivity.this.selectedAlbum.photos.size()) {
                     continue;
                  }
               } else {
                  ArrayList var8;
                  if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                     var8 = PhotoPickerActivity.this.recentImages;
                  } else {
                     var8 = PhotoPickerActivity.this.searchResult;
                  }

                  if (var6 < 0 || var6 >= var8.size()) {
                     continue;
                  }
               }

               if (var6 == var3) {
                  var9.showCheck(true);
                  break;
               }
            }
         }

      }
   };
   private ArrayList recentImages;
   private ActionBarMenuItem searchItem;
   private ArrayList searchResult = new ArrayList();
   private HashMap searchResultKeys = new HashMap();
   private HashMap searchResultUrls = new HashMap();
   private boolean searching;
   private boolean searchingUser;
   private int selectPhotoType;
   private MediaController.AlbumEntry selectedAlbum;
   private HashMap selectedPhotos;
   private ArrayList selectedPhotosOrder;
   private boolean sendPressed;
   private int type;

   public PhotoPickerActivity(int var1, MediaController.AlbumEntry var2, HashMap var3, ArrayList var4, ArrayList var5, int var6, boolean var7, ChatActivity var8) {
      this.selectedAlbum = var2;
      this.selectedPhotos = var3;
      this.selectedPhotosOrder = var4;
      this.type = var1;
      this.recentImages = var5;
      this.selectPhotoType = var6;
      this.chatActivity = var8;
      this.allowCaption = var7;
   }

   // $FF: synthetic method
   static int access$2000(PhotoPickerActivity var0) {
      return var0.currentAccount;
   }

   private int addToSelectedPhotos(Object var1, int var2) {
      boolean var3 = var1 instanceof MediaController.PhotoEntry;
      Object var4;
      if (var3) {
         var4 = ((MediaController.PhotoEntry)var1).imageId;
      } else if (var1 instanceof MediaController.SearchImage) {
         var4 = ((MediaController.SearchImage)var1).id;
      } else {
         var4 = null;
      }

      if (var4 == null) {
         return -1;
      } else if (this.selectedPhotos.containsKey(var4)) {
         this.selectedPhotos.remove(var4);
         int var5 = this.selectedPhotosOrder.indexOf(var4);
         if (var5 >= 0) {
            this.selectedPhotosOrder.remove(var5);
         }

         if (this.allowIndices) {
            this.updateCheckedPhotoIndices();
         }

         if (var2 >= 0) {
            if (var3) {
               ((MediaController.PhotoEntry)var1).reset();
            } else if (var1 instanceof MediaController.SearchImage) {
               ((MediaController.SearchImage)var1).reset();
            }

            this.provider.updatePhotoAtIndex(var2);
         }

         return var5;
      } else {
         this.selectedPhotos.put(var4, var1);
         this.selectedPhotosOrder.add(var4);
         return -1;
      }
   }

   private void fixLayout() {
      RecyclerListView var1 = this.listView;
      if (var1 != null) {
         var1.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
               PhotoPickerActivity.this.fixLayoutInternal();
               if (PhotoPickerActivity.this.listView != null) {
                  PhotoPickerActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
               }

               return true;
            }
         });
      }

   }

   private void fixLayoutInternal() {
      if (this.getParentActivity() != null) {
         int var1 = this.layoutManager.findFirstVisibleItemPosition();
         int var2 = ((WindowManager)ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
         boolean var3 = AndroidUtilities.isTablet();
         byte var4 = 3;
         if (!var3 && (var2 == 3 || var2 == 1)) {
            var4 = 5;
         }

         this.layoutManager.setSpanCount(var4);
         if (AndroidUtilities.isTablet()) {
            this.itemWidth = (AndroidUtilities.dp(490.0F) - (var4 + 1) * AndroidUtilities.dp(4.0F)) / var4;
         } else {
            this.itemWidth = (AndroidUtilities.displaySize.x - (var4 + 1) * AndroidUtilities.dp(4.0F)) / var4;
         }

         this.listAdapter.notifyDataSetChanged();
         this.layoutManager.scrollToPosition(var1);
         if (this.selectedAlbum == null) {
            this.emptyView.setPadding(0, 0, 0, (int)((float)(AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight()) * 0.4F));
         }

      }
   }

   private PhotoPickerPhotoCell getCellForIndex(int var1) {
      int var2 = this.listView.getChildCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         View var4 = this.listView.getChildAt(var3);
         if (var4 instanceof PhotoPickerPhotoCell) {
            PhotoPickerPhotoCell var5 = (PhotoPickerPhotoCell)var4;
            int var6 = (Integer)var5.photoImage.getTag();
            MediaController.AlbumEntry var7 = this.selectedAlbum;
            if (var7 != null) {
               if (var6 < 0 || var6 >= var7.photos.size()) {
                  continue;
               }
            } else {
               ArrayList var8;
               if (this.searchResult.isEmpty() && this.lastSearchString == null) {
                  var8 = this.recentImages;
               } else {
                  var8 = this.searchResult;
               }

               if (var6 < 0 || var6 >= var8.size()) {
                  continue;
               }
            }

            if (var6 == var1) {
               return var5;
            }
         }
      }

      return null;
   }

   private void hideHint() {
      this.hintAnimation = new AnimatorSet();
      this.hintAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.hintTextView, "alpha", new float[]{0.0F})});
      this.hintAnimation.addListener(new AnimatorListenerAdapter() {
         public void onAnimationCancel(Animator var1) {
            if (var1.equals(PhotoPickerActivity.this.hintAnimation)) {
               PhotoPickerActivity.this.hintHideRunnable = null;
               PhotoPickerActivity.this.hintHideRunnable = null;
            }

         }

         public void onAnimationEnd(Animator var1) {
            if (var1.equals(PhotoPickerActivity.this.hintAnimation)) {
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

   // $FF: synthetic method
   public static void lambda$aYB7K3WSG2cRcI6ke4K4lfSNCNw(PhotoPickerActivity var0) {
      var0.hideHint();
   }

   private void processSearch(EditText var1) {
      if (var1.getText().toString().length() != 0) {
         this.searchResult.clear();
         this.searchResultKeys.clear();
         this.imageSearchEndReached = true;
         boolean var2;
         if (this.type == 1) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.searchImages(var2, var1.getText().toString(), "", true);
         this.lastSearchString = var1.getText().toString();
         if (this.lastSearchString.length() == 0) {
            this.lastSearchString = null;
            this.emptyView.setText("");
         } else {
            this.emptyView.setText(LocaleController.getString("NoResult", 2131559943));
         }

         this.updateSearchInterface();
      }
   }

   private void searchBotUser(boolean var1) {
      if (!this.searchingUser) {
         this.searchingUser = true;
         TLRPC.TL_contacts_resolveUsername var2 = new TLRPC.TL_contacts_resolveUsername();
         String var3;
         if (var1) {
            var3 = MessagesController.getInstance(super.currentAccount).gifSearchBot;
         } else {
            var3 = MessagesController.getInstance(super.currentAccount).imageSearchBot;
         }

         var2.username = var3;
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var2, new _$$Lambda$PhotoPickerActivity$67brVZzCZFPPW5KLbmmY3AJwLD4(this, var1));
      }
   }

   private void searchImages(boolean var1, String var2, String var3, boolean var4) {
      if (this.searching) {
         this.searching = false;
         if (this.imageReqId != 0) {
            ConnectionsManager.getInstance(super.currentAccount).cancelRequest(this.imageReqId, true);
            this.imageReqId = 0;
         }
      }

      this.lastSearchImageString = var2;
      this.searching = true;
      MessagesController var5 = MessagesController.getInstance(super.currentAccount);
      String var6;
      if (var1) {
         var6 = MessagesController.getInstance(super.currentAccount).gifSearchBot;
      } else {
         var6 = MessagesController.getInstance(super.currentAccount).imageSearchBot;
      }

      TLObject var11 = var5.getUserOrChat(var6);
      if (!(var11 instanceof TLRPC.User)) {
         if (var4) {
            this.searchBotUser(var1);
         }

      } else {
         TLRPC.User var7 = (TLRPC.User)var11;
         TLRPC.TL_messages_getInlineBotResults var10 = new TLRPC.TL_messages_getInlineBotResults();
         var6 = var2;
         if (var2 == null) {
            var6 = "";
         }

         var10.query = var6;
         var10.bot = MessagesController.getInstance(super.currentAccount).getInputUser(var7);
         var10.offset = var3;
         ChatActivity var9 = this.chatActivity;
         int var8;
         if (var9 != null) {
            var8 = (int)var9.getDialogId();
            if (var8 != 0) {
               var10.peer = MessagesController.getInstance(super.currentAccount).getInputPeer(var8);
            } else {
               var10.peer = new TLRPC.TL_inputPeerEmpty();
            }
         } else {
            var10.peer = new TLRPC.TL_inputPeerEmpty();
         }

         var8 = this.lastSearchToken + 1;
         this.lastSearchToken = var8;
         this.imageReqId = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var10, new _$$Lambda$PhotoPickerActivity$uL23aS4dzDeSWXHf7OAjR3N3zqg(this, var8, var1));
         ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(this.imageReqId, super.classGuid);
      }
   }

   private void sendSelectedPhotos() {
      if (!this.selectedPhotos.isEmpty()) {
         PhotoPickerActivity.PhotoPickerActivityDelegate var1 = this.delegate;
         if (var1 != null && !this.sendPressed) {
            this.sendPressed = true;
            var1.actionButtonPressed(false);
            if (this.selectPhotoType != 2) {
               this.finishFragment();
            }
         }
      }

   }

   private void showHint(boolean var1, boolean var2) {
      if (this.getParentActivity() != null && super.fragmentView != null && (!var1 || this.hintTextView != null)) {
         if (this.hintTextView == null) {
            this.hintTextView = new TextView(this.getParentActivity());
            this.hintTextView.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(3.0F), Theme.getColor("chat_gifSaveHintBackground")));
            this.hintTextView.setTextColor(Theme.getColor("chat_gifSaveHintText"));
            this.hintTextView.setTextSize(1, 14.0F);
            this.hintTextView.setPadding(AndroidUtilities.dp(8.0F), AndroidUtilities.dp(7.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(7.0F));
            this.hintTextView.setGravity(16);
            this.hintTextView.setAlpha(0.0F);
            this.frameLayout.addView(this.hintTextView, LayoutHelper.createFrame(-2, -2.0F, 81, 5.0F, 0.0F, 5.0F, 51.0F));
         }

         AnimatorSet var6;
         if (var1) {
            var6 = this.hintAnimation;
            if (var6 != null) {
               var6.cancel();
               this.hintAnimation = null;
            }

            AndroidUtilities.cancelRunOnUIThread(this.hintHideRunnable);
            this.hintHideRunnable = null;
            this.hideHint();
            return;
         }

         TextView var4 = this.hintTextView;
         String var3;
         int var5;
         if (var2) {
            var5 = 2131559612;
            var3 = "GroupPhotosHelp";
         } else {
            var5 = 2131560786;
            var3 = "SinglePhotosHelp";
         }

         var4.setText(LocaleController.getString(var3, var5));
         Runnable var7 = this.hintHideRunnable;
         if (var7 != null) {
            var6 = this.hintAnimation;
            if (var6 == null) {
               AndroidUtilities.cancelRunOnUIThread(var7);
               _$$Lambda$PhotoPickerActivity$aYB7K3WSG2cRcI6ke4K4lfSNCNw var8 = new _$$Lambda$PhotoPickerActivity$aYB7K3WSG2cRcI6ke4K4lfSNCNw(this);
               this.hintHideRunnable = var8;
               AndroidUtilities.runOnUIThread(var8, 2000L);
               return;
            }

            var6.cancel();
            this.hintAnimation = null;
         } else if (this.hintAnimation != null) {
            return;
         }

         this.hintTextView.setVisibility(0);
         this.hintAnimation = new AnimatorSet();
         this.hintAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.hintTextView, "alpha", new float[]{1.0F})});
         this.hintAnimation.addListener(new AnimatorListenerAdapter() {
            // $FF: synthetic method
            public void lambda$onAnimationEnd$0$PhotoPickerActivity$8() {
               PhotoPickerActivity.this.hideHint();
            }

            public void onAnimationCancel(Animator var1) {
               if (var1.equals(PhotoPickerActivity.this.hintAnimation)) {
                  PhotoPickerActivity.this.hintAnimation = null;
               }

            }

            public void onAnimationEnd(Animator var1) {
               if (var1.equals(PhotoPickerActivity.this.hintAnimation)) {
                  PhotoPickerActivity.this.hintAnimation = null;
                  PhotoPickerActivity var3 = PhotoPickerActivity.this;
                  _$$Lambda$PhotoPickerActivity$8$zJKelkpjam7V_XUMpJypLa8CZ48 var2 = new _$$Lambda$PhotoPickerActivity$8$zJKelkpjam7V_XUMpJypLa8CZ48(this);
                  var3.hintHideRunnable = var2;
                  AndroidUtilities.runOnUIThread(var2, 2000L);
               }

            }
         });
         this.hintAnimation.setDuration(300L);
         this.hintAnimation.start();
      }

   }

   private void updateCheckedPhotoIndices() {
      if (this.allowIndices) {
         int var1 = this.listView.getChildCount();

         for(int var2 = 0; var2 < var1; ++var2) {
            View var3 = this.listView.getChildAt(var2);
            if (var3 instanceof PhotoPickerPhotoCell) {
               PhotoPickerPhotoCell var4 = (PhotoPickerPhotoCell)var3;
               Integer var5 = (Integer)var4.getTag();
               MediaController.AlbumEntry var7 = this.selectedAlbum;
               int var6 = -1;
               if (var7 != null) {
                  MediaController.PhotoEntry var9 = (MediaController.PhotoEntry)var7.photos.get(var5);
                  if (this.allowIndices) {
                     var6 = this.selectedPhotosOrder.indexOf(var9.imageId);
                  }

                  var4.setNum(var6);
               } else {
                  MediaController.SearchImage var8;
                  if (this.searchResult.isEmpty() && this.lastSearchString == null) {
                     var8 = (MediaController.SearchImage)this.recentImages.get(var5);
                  } else {
                     var8 = (MediaController.SearchImage)this.searchResult.get(var5);
                  }

                  if (this.allowIndices) {
                     var6 = this.selectedPhotosOrder.indexOf(var8.id);
                  }

                  var4.setNum(var6);
               }
            }
         }

      }
   }

   private void updateSearchInterface() {
      PhotoPickerActivity.ListAdapter var1 = this.listAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

      if ((!this.searching || !this.searchResult.isEmpty()) && (!this.loadingRecent || this.lastSearchString != null)) {
         this.emptyView.showTextView();
      } else {
         this.emptyView.showProgress();
      }

   }

   public View createView(Context var1) {
      super.actionBar.setBackgroundColor(-13421773);
      ActionBar var2 = super.actionBar;
      boolean var3 = false;
      var2.setItemsBackgroundColor(-12763843, false);
      super.actionBar.setTitleColor(-1);
      super.actionBar.setItemsColor(-1, false);
      super.actionBar.setBackButtonImage(2131165409);
      MediaController.AlbumEntry var10 = this.selectedAlbum;
      int var4;
      if (var10 != null) {
         super.actionBar.setTitle(var10.bucketName);
      } else {
         var4 = this.type;
         if (var4 == 0) {
            super.actionBar.setTitle(LocaleController.getString("SearchImagesTitle", 2131560652));
         } else if (var4 == 1) {
            super.actionBar.setTitle(LocaleController.getString("SearchGifsTitle", 2131560649));
         }
      }

      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               PhotoPickerActivity.this.finishFragment();
            }

         }
      });
      if (this.selectedAlbum == null) {
         this.searchItem = super.actionBar.createMenu().addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
            public boolean canCollapseSearch() {
               PhotoPickerActivity.this.finishFragment();
               return false;
            }

            public void onSearchExpand() {
            }

            public void onSearchPressed(EditText var1) {
               PhotoPickerActivity.this.processSearch(var1);
            }

            public void onTextChanged(EditText var1) {
               if (var1.getText().length() == 0) {
                  PhotoPickerActivity.this.searchResult.clear();
                  PhotoPickerActivity.this.searchResultKeys.clear();
                  PhotoPickerActivity.this.lastSearchString = null;
                  PhotoPickerActivity.this.imageSearchEndReached = true;
                  PhotoPickerActivity.this.searching = false;
                  if (PhotoPickerActivity.this.imageReqId != 0) {
                     ConnectionsManager.getInstance(PhotoPickerActivity.access$2000(PhotoPickerActivity.this)).cancelRequest(PhotoPickerActivity.this.imageReqId, true);
                     PhotoPickerActivity.this.imageReqId = 0;
                  }

                  PhotoPickerActivity.this.emptyView.setText("");
                  PhotoPickerActivity.this.updateSearchInterface();
               }

            }
         });
      }

      if (this.selectedAlbum == null) {
         var4 = this.type;
         if (var4 == 0) {
            this.searchItem.setSearchFieldHint(LocaleController.getString("SearchImagesTitle", 2131560652));
         } else if (var4 == 1) {
            this.searchItem.setSearchFieldHint(LocaleController.getString("SearchGifsTitle", 2131560649));
         }
      }

      super.fragmentView = new FrameLayout(var1);
      this.frameLayout = (FrameLayout)super.fragmentView;
      this.frameLayout.setBackgroundColor(-16777216);
      this.listView = new RecyclerListView(var1);
      this.listView.setPadding(AndroidUtilities.dp(4.0F), AndroidUtilities.dp(4.0F), AndroidUtilities.dp(4.0F), AndroidUtilities.dp(4.0F));
      this.listView.setClipToPadding(false);
      this.listView.setHorizontalScrollBarEnabled(false);
      this.listView.setVerticalScrollBarEnabled(false);
      RecyclerListView var5 = this.listView;
      var2 = null;
      var5.setItemAnimator((RecyclerView.ItemAnimator)null);
      this.listView.setLayoutAnimation((LayoutAnimationController)null);
      var5 = this.listView;
      GridLayoutManager var6 = new GridLayoutManager(var1, 4) {
         public boolean supportsPredictiveItemAnimations() {
            return false;
         }
      };
      this.layoutManager = var6;
      var5.setLayoutManager(var6);
      this.listView.addItemDecoration(new RecyclerView.ItemDecoration() {
         public void getItemOffsets(Rect var1, View var2, RecyclerView var3, RecyclerView.State var4) {
            super.getItemOffsets(var1, var2, var3, var4);
            int var5 = var4.getItemCount();
            int var6 = var3.getChildAdapterPosition(var2);
            int var7 = PhotoPickerActivity.this.layoutManager.getSpanCount();
            int var8 = (int)Math.ceil((double)((float)var5 / (float)var7));
            int var9 = var6 / var7;
            byte var10 = 0;
            if (var6 % var7 != var7 - 1) {
               var7 = AndroidUtilities.dp(4.0F);
            } else {
               var7 = 0;
            }

            var1.right = var7;
            var7 = var10;
            if (var9 != var8 - 1) {
               var7 = AndroidUtilities.dp(4.0F);
            }

            var1.bottom = var7;
         }
      });
      FrameLayout var12 = this.frameLayout;
      RecyclerListView var13 = this.listView;
      float var7;
      if (this.selectPhotoType != 0) {
         var7 = 0.0F;
      } else {
         var7 = 48.0F;
      }

      var12.addView(var13, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 0.0F, 0.0F, var7));
      var5 = this.listView;
      PhotoPickerActivity.ListAdapter var15 = new PhotoPickerActivity.ListAdapter(var1);
      this.listAdapter = var15;
      var5.setAdapter(var15);
      this.listView.setGlowColor(-13421773);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$PhotoPickerActivity$zD_mr7IpQctZxAyf7EKDChtrGZc(this)));
      if (this.selectedAlbum == null) {
         this.listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)(new _$$Lambda$PhotoPickerActivity$Hdsa1stRiohWHt8IIV2h_tg2fmI(this)));
      }

      this.emptyView = new EmptyTextProgressView(var1);
      this.emptyView.setTextColor(-8355712);
      this.emptyView.setProgressBarColor(-1);
      this.emptyView.setShowAtCenter(true);
      if (this.selectedAlbum != null) {
         this.emptyView.setText(LocaleController.getString("NoPhotos", 2131559937));
      } else {
         this.emptyView.setText("");
      }

      FrameLayout var17 = this.frameLayout;
      EmptyTextProgressView var14 = this.emptyView;
      if (this.selectPhotoType != 0) {
         var7 = 0.0F;
      } else {
         var7 = 48.0F;
      }

      var17.addView(var14, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 0.0F, 0.0F, var7));
      if (this.selectedAlbum == null) {
         this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView var1, int var2) {
               if (var2 == 1) {
                  AndroidUtilities.hideKeyboard(PhotoPickerActivity.this.getParentActivity().getCurrentFocus());
               }

            }

            public void onScrolled(RecyclerView var1, int var2, int var3) {
               var3 = PhotoPickerActivity.this.layoutManager.findFirstVisibleItemPosition();
               boolean var4 = false;
               if (var3 == -1) {
                  var2 = 0;
               } else {
                  var2 = Math.abs(PhotoPickerActivity.this.layoutManager.findLastVisibleItemPosition() - var3) + 1;
               }

               if (var2 > 0) {
                  int var5 = PhotoPickerActivity.this.layoutManager.getItemCount();
                  if (var2 != 0 && var3 + var2 > var5 - 2 && !PhotoPickerActivity.this.searching && !PhotoPickerActivity.this.imageSearchEndReached) {
                     PhotoPickerActivity var6 = PhotoPickerActivity.this;
                     if (var6.type == 1) {
                        var4 = true;
                     }

                     var6.searchImages(var4, PhotoPickerActivity.this.lastSearchString, PhotoPickerActivity.this.nextImagesSearchOffset, true);
                  }
               }

            }
         });
         this.updateSearchInterface();
      }

      this.pickerBottomLayout = new PickerBottomLayout(var1);
      this.frameLayout.addView(this.pickerBottomLayout, LayoutHelper.createFrame(-1, 48, 80));
      this.pickerBottomLayout.cancelButton.setOnClickListener(new _$$Lambda$PhotoPickerActivity$KmEu4SYAqKOt718fgSd59sdqoWM(this));
      this.pickerBottomLayout.doneButton.setOnClickListener(new _$$Lambda$PhotoPickerActivity$mGdvO16vUkJ4QAJ7aZX42E0io7E(this));
      if (this.selectPhotoType != 0) {
         this.pickerBottomLayout.setVisibility(8);
      } else if (this.selectedAlbum != null || this.type == 0) {
         ChatActivity var16 = this.chatActivity;
         if (var16 != null && var16.allowGroupPhotos()) {
            this.imageOrderToggleButton = new ImageView(var1);
            this.imageOrderToggleButton.setScaleType(ScaleType.CENTER);
            this.imageOrderToggleButton.setImageResource(2131165757);
            ImageView var18 = this.imageOrderToggleButton;
            String var9;
            if (SharedConfig.groupPhotosEnabled) {
               var4 = 2131559612;
               var9 = "GroupPhotosHelp";
            } else {
               var4 = 2131560786;
               var9 = "SinglePhotosHelp";
            }

            var18.setContentDescription(LocaleController.getString(var9, var4));
            this.pickerBottomLayout.addView(this.imageOrderToggleButton, LayoutHelper.createFrame(48, -1, 17));
            this.imageOrderToggleButton.setOnClickListener(new _$$Lambda$PhotoPickerActivity$dTiSUg75sRfWPRlo26sk1jZD__Q(this));
            var18 = this.imageOrderToggleButton;
            PorterDuffColorFilter var11 = var2;
            if (SharedConfig.groupPhotosEnabled) {
               var11 = new PorterDuffColorFilter(-10043398, Mode.MULTIPLY);
            }

            var18.setColorFilter(var11);
         }
      }

      boolean var8;
      label70: {
         if (this.selectedAlbum == null) {
            var8 = var3;
            if (this.type != 0) {
               break label70;
            }
         }

         var8 = var3;
         if (this.maxSelectedPhotos <= 0) {
            var8 = true;
         }
      }

      this.allowIndices = var8;
      this.listView.setEmptyView(this.emptyView);
      this.pickerBottomLayout.updateSelectedCount(this.selectedPhotos.size(), true);
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.closeChats) {
         this.removeSelfFromStack();
      } else if (var1 == NotificationCenter.recentImagesDidLoad && this.selectedAlbum == null && this.type == (Integer)var3[0]) {
         this.recentImages = (ArrayList)var3[1];
         this.loadingRecent = false;
         this.updateSearchInterface();
      }

   }

   // $FF: synthetic method
   public void lambda$createView$0$PhotoPickerActivity(View var1, int var2) {
      MediaController.AlbumEntry var5 = this.selectedAlbum;
      ArrayList var6;
      if (var5 != null) {
         var6 = var5.photos;
      } else if (this.searchResult.isEmpty() && this.lastSearchString == null) {
         var6 = this.recentImages;
      } else {
         var6 = this.searchResult;
      }

      if (var2 >= 0 && var2 < var6.size()) {
         ActionBarMenuItem var3 = this.searchItem;
         if (var3 != null) {
            AndroidUtilities.hideKeyboard(var3.getSearchField());
         }

         int var4 = this.selectPhotoType;
         byte var7;
         if (var4 == 1) {
            var7 = 1;
         } else if (var4 == 2) {
            var7 = 3;
         } else if (this.chatActivity == null) {
            var7 = 4;
         } else {
            var7 = 0;
         }

         PhotoViewer.getInstance().setParentActivity(this.getParentActivity());
         PhotoViewer.getInstance().setMaxSelectedPhotos(this.maxSelectedPhotos);
         PhotoViewer.getInstance().openPhotoForSelect(var6, var2, var7, this.provider, this.chatActivity);
      }

   }

   // $FF: synthetic method
   public boolean lambda$createView$2$PhotoPickerActivity(View var1, int var2) {
      if (this.searchResult.isEmpty() && this.lastSearchString == null) {
         AlertDialog.Builder var3 = new AlertDialog.Builder(this.getParentActivity());
         var3.setTitle(LocaleController.getString("AppName", 2131558635));
         var3.setMessage(LocaleController.getString("ClearSearch", 2131559114));
         var3.setPositiveButton(LocaleController.getString("ClearButton", 2131559102).toUpperCase(), new _$$Lambda$PhotoPickerActivity$V8BRocCcdRNwJ4kn9l4217_mJco(this));
         var3.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         this.showDialog(var3.create());
         return true;
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   public void lambda$createView$3$PhotoPickerActivity(View var1) {
      this.delegate.actionButtonPressed(true);
      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$createView$4$PhotoPickerActivity(View var1) {
      this.sendSelectedPhotos();
   }

   // $FF: synthetic method
   public void lambda$createView$5$PhotoPickerActivity(View var1) {
      SharedConfig.toggleGroupPhotosEnabled();
      ImageView var2 = this.imageOrderToggleButton;
      PorterDuffColorFilter var4;
      if (SharedConfig.groupPhotosEnabled) {
         var4 = new PorterDuffColorFilter(-10043398, Mode.MULTIPLY);
      } else {
         var4 = null;
      }

      var2.setColorFilter(var4);
      this.showHint(false, SharedConfig.groupPhotosEnabled);
      this.updateCheckedPhotoIndices();
      var2 = this.imageOrderToggleButton;
      int var3;
      String var5;
      if (SharedConfig.groupPhotosEnabled) {
         var3 = 2131559612;
         var5 = "GroupPhotosHelp";
      } else {
         var3 = 2131560786;
         var5 = "SinglePhotosHelp";
      }

      var2.setContentDescription(LocaleController.getString(var5, var3));
   }

   // $FF: synthetic method
   public void lambda$null$1$PhotoPickerActivity(DialogInterface var1, int var2) {
      this.recentImages.clear();
      PhotoPickerActivity.ListAdapter var3 = this.listAdapter;
      if (var3 != null) {
         var3.notifyDataSetChanged();
      }

      MessagesStorage.getInstance(super.currentAccount).clearWebRecent(this.type);
   }

   // $FF: synthetic method
   public void lambda$null$6$PhotoPickerActivity(TLObject var1, boolean var2) {
      TLRPC.TL_contacts_resolvedPeer var3 = (TLRPC.TL_contacts_resolvedPeer)var1;
      MessagesController.getInstance(super.currentAccount).putUsers(var3.users, false);
      MessagesController.getInstance(super.currentAccount).putChats(var3.chats, false);
      MessagesStorage.getInstance(super.currentAccount).putUsersAndChats(var3.users, var3.chats, true, true);
      String var4 = this.lastSearchImageString;
      this.lastSearchImageString = null;
      this.searchImages(var2, var4, "", false);
   }

   // $FF: synthetic method
   public void lambda$null$8$PhotoPickerActivity(int var1, TLObject var2, boolean var3) {
      if (var1 == this.lastSearchToken) {
         int var4 = this.searchResult.size();
         if (var2 != null) {
            TLRPC.messages_BotResults var5 = (TLRPC.messages_BotResults)var2;
            this.nextImagesSearchOffset = var5.next_offset;
            int var6 = var5.results.size();
            int var7 = 0;

            int var9;
            for(var1 = 0; var7 < var6; var1 = var9) {
               label148: {
                  TLRPC.BotInlineResult var8 = (TLRPC.BotInlineResult)var5.results.get(var7);
                  if (var3 == 0) {
                     var9 = var1;
                     if (!"photo".equals(var8.type)) {
                        break label148;
                     }
                  }

                  if (var3 != 0 && !"gif".equals(var8.type)) {
                     var9 = var1;
                  } else if (this.searchResultKeys.containsKey(var8.id)) {
                     var9 = var1;
                  } else {
                     label159: {
                        MediaController.SearchImage var13 = new MediaController.SearchImage();
                        TLRPC.Photo var10;
                        TLRPC.DocumentAttribute var14;
                        TLRPC.PhotoSize var17;
                        if (var3 != 0 && var8.document != null) {
                           for(var9 = 0; var9 < var8.document.attributes.size(); ++var9) {
                              var14 = (TLRPC.DocumentAttribute)var8.document.attributes.get(var9);
                              if (var14 instanceof TLRPC.TL_documentAttributeImageSize || var14 instanceof TLRPC.TL_documentAttributeVideo) {
                                 var13.width = var14.w;
                                 var13.height = var14.h;
                                 break;
                              }
                           }

                           TLRPC.Document var16 = var8.document;
                           var13.document = var16;
                           var13.size = 0;
                           var10 = var8.photo;
                           if (var10 != null && var16 != null) {
                              var17 = FileLoader.getClosestPhotoSizeWithSize(var10.sizes, this.itemWidth, true);
                              if (var17 != null) {
                                 var8.document.thumbs.add(var17);
                                 TLRPC.Document var18 = var8.document;
                                 var18.flags |= 1;
                              }
                           }
                        } else {
                           label156: {
                              if (var3 == 0) {
                                 var10 = var8.photo;
                                 if (var10 != null) {
                                    TLRPC.PhotoSize var11 = FileLoader.getClosestPhotoSizeWithSize(var10.sizes, AndroidUtilities.getPhotoSize());
                                    var17 = FileLoader.getClosestPhotoSizeWithSize(var8.photo.sizes, 320);
                                    if (var11 == null) {
                                       var9 = var1;
                                       break label159;
                                    }

                                    var13.width = var11.w;
                                    var13.height = var11.h;
                                    var13.photoSize = var11;
                                    var13.photo = var8.photo;
                                    var13.size = var11.size;
                                    var13.thumbPhotoSize = var17;
                                    break label156;
                                 }
                              }

                              if (var8.content == null) {
                                 var9 = var1;
                                 break label159;
                              }

                              for(var9 = 0; var9 < var8.content.attributes.size(); ++var9) {
                                 var14 = (TLRPC.DocumentAttribute)var8.content.attributes.get(var9);
                                 if (var14 instanceof TLRPC.TL_documentAttributeImageSize) {
                                    var13.width = var14.w;
                                    var13.height = var14.h;
                                    break;
                                 }
                              }

                              TLRPC.WebDocument var15 = var8.thumb;
                              if (var15 != null) {
                                 var13.thumbUrl = var15.url;
                              } else {
                                 var13.thumbUrl = null;
                              }

                              var15 = var8.content;
                              var13.imageUrl = var15.url;
                              if (var3 != 0) {
                                 var9 = 0;
                              } else {
                                 var9 = var15.size;
                              }

                              var13.size = var9;
                           }
                        }

                        var13.id = var8.id;
                        var13.type = var3;
                        var13.localUrl = "";
                        this.searchResult.add(var13);
                        this.searchResultKeys.put(var13.id, var13);
                        var9 = var1 + 1;
                     }
                  }
               }

               ++var7;
            }

            boolean var12;
            if (var4 != this.searchResult.size() && this.nextImagesSearchOffset != null) {
               var12 = false;
            } else {
               var12 = true;
            }

            this.imageSearchEndReached = var12;
         } else {
            var1 = 0;
         }

         this.searching = false;
         if (var1 != 0) {
            this.listAdapter.notifyItemRangeInserted(var4, var1);
         } else if (this.imageSearchEndReached) {
            this.listAdapter.notifyItemRemoved(this.searchResult.size() - 1);
         }

         if ((!this.searching || !this.searchResult.isEmpty()) && (!this.loadingRecent || this.lastSearchString != null)) {
            this.emptyView.showTextView();
         } else {
            this.emptyView.showProgress();
         }

      }
   }

   // $FF: synthetic method
   public void lambda$searchBotUser$7$PhotoPickerActivity(boolean var1, TLObject var2, TLRPC.TL_error var3) {
      if (var2 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$PhotoPickerActivity$QV1g94ydF_QhinuWtXUlhLtLgBk(this, var2, var1));
      }

   }

   // $FF: synthetic method
   public void lambda$searchImages$9$PhotoPickerActivity(int var1, boolean var2, TLObject var3, TLRPC.TL_error var4) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PhotoPickerActivity$ff4Wa8_RyuGsGlKqqm3MiT5Xaeo(this, var1, var3, var2));
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      this.fixLayout();
   }

   public boolean onFragmentCreate() {
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.closeChats);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.recentImagesDidLoad);
      if (this.selectedAlbum == null && this.recentImages.isEmpty()) {
         MessagesStorage.getInstance(super.currentAccount).loadWebRecent(this.type);
         this.loadingRecent = true;
      }

      return super.onFragmentCreate();
   }

   public void onFragmentDestroy() {
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.closeChats);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.recentImagesDidLoad);
      if (this.imageReqId != 0) {
         ConnectionsManager.getInstance(super.currentAccount).cancelRequest(this.imageReqId, true);
         this.imageReqId = 0;
      }

      super.onFragmentDestroy();
   }

   public void onResume() {
      super.onResume();
      PhotoPickerActivity.ListAdapter var1 = this.listAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

      ActionBarMenuItem var2 = this.searchItem;
      if (var2 != null) {
         var2.openSearch(true);
         if (!TextUtils.isEmpty(this.initialSearchString)) {
            this.searchItem.setSearchFieldText(this.initialSearchString, false);
            this.initialSearchString = null;
            this.processSearch(this.searchItem.getSearchField());
         }

         this.getParentActivity().getWindow().setSoftInputMode(32);
      }

      this.fixLayout();
   }

   public void onTransitionAnimationEnd(boolean var1, boolean var2) {
      if (var1) {
         ActionBarMenuItem var3 = this.searchItem;
         if (var3 != null) {
            AndroidUtilities.showKeyboard(var3.getSearchField());
         }
      }

   }

   public void setDelegate(PhotoPickerActivity.PhotoPickerActivityDelegate var1) {
      this.delegate = var1;
   }

   public void setInitialSearchString(String var1) {
      this.initialSearchString = var1;
   }

   public void setMaxSelectedPhotos(int var1) {
      this.maxSelectedPhotos = var1;
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         if (PhotoPickerActivity.this.selectedAlbum == null) {
            return PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null ? PhotoPickerActivity.this.recentImages.size() : PhotoPickerActivity.this.searchResult.size() + (PhotoPickerActivity.this.imageSearchEndReached ^ 1);
         } else {
            return PhotoPickerActivity.this.selectedAlbum.photos.size();
         }
      }

      public long getItemId(int var1) {
         return (long)var1;
      }

      public int getItemViewType(int var1) {
         return PhotoPickerActivity.this.selectedAlbum == null && (!PhotoPickerActivity.this.searchResult.isEmpty() || PhotoPickerActivity.this.lastSearchString != null || var1 >= PhotoPickerActivity.this.recentImages.size()) && var1 >= PhotoPickerActivity.this.searchResult.size() ? 1 : 0;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         MediaController.AlbumEntry var2 = PhotoPickerActivity.this.selectedAlbum;
         boolean var3 = true;
         boolean var4 = true;
         boolean var5 = var3;
         if (var2 == null) {
            int var6 = var1.getAdapterPosition();
            if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
               if (var6 < PhotoPickerActivity.this.recentImages.size()) {
                  var5 = var4;
               } else {
                  var5 = false;
               }

               return var5;
            }

            if (var6 < PhotoPickerActivity.this.searchResult.size()) {
               var5 = var3;
            } else {
               var5 = false;
            }
         }

         return var5;
      }

      // $FF: synthetic method
      public void lambda$onCreateViewHolder$0$PhotoPickerActivity$ListAdapter(View var1) {
         int var2 = (Integer)((View)var1.getParent()).getTag();
         MediaController.AlbumEntry var3 = PhotoPickerActivity.this.selectedAlbum;
         byte var4 = -1;
         boolean var5;
         int var6;
         if (var3 != null) {
            MediaController.PhotoEntry var7 = (MediaController.PhotoEntry)PhotoPickerActivity.this.selectedAlbum.photos.get(var2);
            var5 = PhotoPickerActivity.this.selectedPhotos.containsKey(var7.imageId) ^ true;
            if (var5 && PhotoPickerActivity.this.maxSelectedPhotos > 0 && PhotoPickerActivity.this.selectedPhotos.size() >= PhotoPickerActivity.this.maxSelectedPhotos) {
               return;
            }

            var6 = var4;
            if (PhotoPickerActivity.this.allowIndices) {
               var6 = var4;
               if (var5) {
                  var6 = PhotoPickerActivity.this.selectedPhotosOrder.size();
               }
            }

            ((PhotoPickerPhotoCell)var1.getParent()).setChecked(var6, var5, true);
            PhotoPickerActivity.this.addToSelectedPhotos(var7, var2);
         } else {
            AndroidUtilities.hideKeyboard(PhotoPickerActivity.this.getParentActivity().getCurrentFocus());
            MediaController.SearchImage var8;
            if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
               var8 = (MediaController.SearchImage)PhotoPickerActivity.this.recentImages.get((Integer)((View)var1.getParent()).getTag());
            } else {
               var8 = (MediaController.SearchImage)PhotoPickerActivity.this.searchResult.get((Integer)((View)var1.getParent()).getTag());
            }

            var5 = PhotoPickerActivity.this.selectedPhotos.containsKey(var8.id) ^ true;
            if (var5 && PhotoPickerActivity.this.maxSelectedPhotos > 0 && PhotoPickerActivity.this.selectedPhotos.size() >= PhotoPickerActivity.this.maxSelectedPhotos) {
               return;
            }

            var6 = var4;
            if (PhotoPickerActivity.this.allowIndices) {
               var6 = var4;
               if (var5) {
                  var6 = PhotoPickerActivity.this.selectedPhotosOrder.size();
               }
            }

            ((PhotoPickerPhotoCell)var1.getParent()).setChecked(var6, var5, true);
            PhotoPickerActivity.this.addToSelectedPhotos(var8, var2);
         }

         PhotoPickerActivity.this.pickerBottomLayout.updateSelectedCount(PhotoPickerActivity.this.selectedPhotos.size(), true);
         PhotoPickerActivity.this.delegate.selectedPhotosChanged();
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         if (var3 != 0) {
            if (var3 == 1) {
               LayoutParams var4 = var1.itemView.getLayoutParams();
               if (var4 != null) {
                  var4.width = PhotoPickerActivity.this.itemWidth;
                  var4.height = PhotoPickerActivity.this.itemWidth;
                  var1.itemView.setLayoutParams(var4);
               }
            }
         } else {
            PhotoPickerPhotoCell var17 = (PhotoPickerPhotoCell)var1.itemView;
            var17.itemWidth = PhotoPickerActivity.this.itemWidth;
            BackupImageView var5 = var17.photoImage;
            var5.setTag(var2);
            var17.setTag(var2);
            byte var6 = 0;
            var5.setOrientation(0, true);
            MediaController.AlbumEntry var11 = PhotoPickerActivity.this.selectedAlbum;
            var3 = -1;
            boolean var9;
            if (var11 != null) {
               MediaController.PhotoEntry var13 = (MediaController.PhotoEntry)PhotoPickerActivity.this.selectedAlbum.photos.get(var2);
               String var7 = var13.thumbPath;
               if (var7 != null) {
                  var5.setImage(var7, (String)null, this.mContext.getResources().getDrawable(2131165697));
               } else if (var13.path != null) {
                  var5.setOrientation(var13.orientation, true);
                  StringBuilder var18;
                  if (var13.isVideo) {
                     var17.videoInfoContainer.setVisibility(0);
                     var2 = var13.duration;
                     int var8 = var2 / 60;
                     var17.videoTextView.setText(String.format("%d:%02d", var8, var2 - var8 * 60));
                     var18 = new StringBuilder();
                     var18.append(LocaleController.getString("AttachVideo", 2131558733));
                     var18.append(", ");
                     var18.append(LocaleController.formatCallDuration(var13.duration));
                     var17.setContentDescription(var18.toString());
                     var18 = new StringBuilder();
                     var18.append("vthumb://");
                     var18.append(var13.imageId);
                     var18.append(":");
                     var18.append(var13.path);
                     var5.setImage(var18.toString(), (String)null, this.mContext.getResources().getDrawable(2131165697));
                  } else {
                     var17.videoInfoContainer.setVisibility(4);
                     var17.setContentDescription(LocaleController.getString("AttachPhoto", 2131558727));
                     var18 = new StringBuilder();
                     var18.append("thumb://");
                     var18.append(var13.imageId);
                     var18.append(":");
                     var18.append(var13.path);
                     var5.setImage(var18.toString(), (String)null, this.mContext.getResources().getDrawable(2131165697));
                  }
               } else {
                  var5.setImageResource(2131165697);
               }

               if (PhotoPickerActivity.this.allowIndices) {
                  var3 = PhotoPickerActivity.this.selectedPhotosOrder.indexOf(var13.imageId);
               }

               var17.setChecked(var3, PhotoPickerActivity.this.selectedPhotos.containsKey(var13.imageId), false);
               var9 = PhotoViewer.isShowingImage(var13.path);
            } else {
               MediaController.SearchImage var12;
               if (PhotoPickerActivity.this.searchResult.isEmpty() && PhotoPickerActivity.this.lastSearchString == null) {
                  var12 = (MediaController.SearchImage)PhotoPickerActivity.this.recentImages.get(var2);
               } else {
                  var12 = (MediaController.SearchImage)PhotoPickerActivity.this.searchResult.get(var2);
               }

               var17.setImage(var12);
               var17.videoInfoContainer.setVisibility(4);
               if (PhotoPickerActivity.this.allowIndices) {
                  var3 = PhotoPickerActivity.this.selectedPhotosOrder.indexOf(var12.id);
               }

               var17.setChecked(var3, PhotoPickerActivity.this.selectedPhotos.containsKey(var12.id), false);
               var9 = PhotoViewer.isShowingImage(var12.getPathToAttach());
            }

            ImageReceiver var15 = var5.getImageReceiver();
            boolean var10;
            if (!var9) {
               var10 = true;
            } else {
               var10 = false;
            }

            byte var14;
            CheckBox var16;
            label49: {
               var15.setVisible(var10, true);
               var16 = var17.checkBox;
               if (PhotoPickerActivity.this.selectPhotoType == 0) {
                  var14 = var6;
                  if (!var9) {
                     break label49;
                  }
               }

               var14 = 8;
            }

            var16.setVisibility(var14);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var4;
         if (var2 != 0) {
            var4 = new FrameLayout(this.mContext);
            RadialProgressView var3 = new RadialProgressView(this.mContext);
            var3.setProgressColor(-1);
            ((FrameLayout)var4).addView(var3, LayoutHelper.createFrame(-1, -1.0F));
         } else {
            var4 = new PhotoPickerPhotoCell(this.mContext, true);
            ((PhotoPickerPhotoCell)var4).checkFrame.setOnClickListener(new _$$Lambda$PhotoPickerActivity$ListAdapter$LuMWeDklP_a8AE5HSJIZdrtM3pI(this));
            FrameLayout var6 = ((PhotoPickerPhotoCell)var4).checkFrame;
            byte var5;
            if (PhotoPickerActivity.this.selectPhotoType != 0) {
               var5 = 8;
            } else {
               var5 = 0;
            }

            var6.setVisibility(var5);
         }

         return new RecyclerListView.Holder((View)var4);
      }
   }

   public interface PhotoPickerActivityDelegate {
      void actionButtonPressed(boolean var1);

      void selectedPhotosChanged();
   }
}
