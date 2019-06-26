package org.telegram.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Cells.PhotoPickerAlbumsCell;
import org.telegram.ui.Cells.PhotoPickerSearchCell;
import org.telegram.ui.Components.PickerBottomLayout;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.RecyclerListView;

public class PhotoAlbumPickerActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private ArrayList albumsSorted = null;
   private boolean allowCaption;
   private boolean allowGifs;
   private boolean allowSearchImages = true;
   private ChatActivity chatActivity;
   private int columnsCount = 2;
   private PhotoAlbumPickerActivity.PhotoAlbumPickerActivityDelegate delegate;
   private TextView emptyView;
   private PhotoAlbumPickerActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private boolean loading = false;
   private int maxSelectedPhotos;
   private PickerBottomLayout pickerBottomLayout;
   private FrameLayout progressView;
   private ArrayList recentGifImages = new ArrayList();
   private HashMap recentImagesGifKeys = new HashMap();
   private HashMap recentImagesWebKeys = new HashMap();
   private ArrayList recentWebImages = new ArrayList();
   private int selectPhotoType;
   private HashMap selectedPhotos = new HashMap();
   private ArrayList selectedPhotosOrder = new ArrayList();
   private boolean sendPressed;

   public PhotoAlbumPickerActivity(int var1, boolean var2, boolean var3, ChatActivity var4) {
      this.chatActivity = var4;
      this.selectPhotoType = var1;
      this.allowGifs = var2;
      this.allowCaption = var3;
   }

   private void fixLayout() {
      RecyclerListView var1 = this.listView;
      if (var1 != null) {
         var1.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
               PhotoAlbumPickerActivity.this.fixLayoutInternal();
               if (PhotoAlbumPickerActivity.this.listView != null) {
                  PhotoAlbumPickerActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
               }

               return true;
            }
         });
      }

   }

   private void fixLayoutInternal() {
      if (this.getParentActivity() != null) {
         int var1 = ((WindowManager)ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
         this.columnsCount = 2;
         if (!AndroidUtilities.isTablet() && (var1 == 3 || var1 == 1)) {
            this.columnsCount = 4;
         }

         this.listAdapter.notifyDataSetChanged();
      }
   }

   // $FF: synthetic method
   static boolean lambda$createView$0(View var0, MotionEvent var1) {
      return true;
   }

   private void openPhotoPicker(MediaController.AlbumEntry var1, int var2) {
      ArrayList var3;
      label21: {
         if (var1 == null) {
            if (var2 == 0) {
               var3 = this.recentWebImages;
               break label21;
            }

            if (var2 == 1) {
               var3 = this.recentGifImages;
               break label21;
            }
         }

         var3 = null;
      }

      PhotoPickerActivity var6;
      if (var1 != null) {
         var6 = new PhotoPickerActivity(var2, var1, this.selectedPhotos, this.selectedPhotosOrder, var3, this.selectPhotoType, this.allowCaption, this.chatActivity);
         var6.setDelegate(new PhotoPickerActivity.PhotoPickerActivityDelegate() {
            public void actionButtonPressed(boolean var1) {
               PhotoAlbumPickerActivity.this.removeSelfFromStack();
               if (!var1) {
                  PhotoAlbumPickerActivity var2 = PhotoAlbumPickerActivity.this;
                  var2.sendSelectedPhotos(var2.selectedPhotos, PhotoAlbumPickerActivity.this.selectedPhotosOrder);
               }

            }

            public void selectedPhotosChanged() {
               if (PhotoAlbumPickerActivity.this.pickerBottomLayout != null) {
                  PhotoAlbumPickerActivity.this.pickerBottomLayout.updateSelectedCount(PhotoAlbumPickerActivity.this.selectedPhotos.size(), true);
               }

            }
         });
      } else {
         final HashMap var4 = new HashMap();
         final ArrayList var5 = new ArrayList();
         var6 = new PhotoPickerActivity(var2, var1, var4, var5, var3, this.selectPhotoType, this.allowCaption, this.chatActivity);
         var6.setDelegate(new PhotoPickerActivity.PhotoPickerActivityDelegate() {
            public void actionButtonPressed(boolean var1) {
               PhotoAlbumPickerActivity.this.removeSelfFromStack();
               if (!var1) {
                  PhotoAlbumPickerActivity.this.sendSelectedPhotos(var4, var5);
               }

            }

            public void selectedPhotosChanged() {
            }
         });
      }

      var6.setMaxSelectedPhotos(this.maxSelectedPhotos);
      this.presentFragment(var6);
   }

   private void sendSelectedPhotos(HashMap var1, ArrayList var2) {
      if (!var1.isEmpty() && this.delegate != null && !this.sendPressed) {
         this.sendPressed = true;
         ArrayList var3 = new ArrayList();
         int var4 = 0;
         boolean var5 = false;

         boolean var6;
         boolean var13;
         for(var6 = false; var4 < var2.size(); var6 = var13) {
            Object var7 = var1.get(var2.get(var4));
            SendMessagesHelper.SendingMediaInfo var8 = new SendMessagesHelper.SendingMediaInfo();
            var3.add(var8);
            boolean var9 = var7 instanceof MediaController.PhotoEntry;
            MediaController.PhotoEntry var10 = null;
            MediaController.SearchImage var11 = null;
            boolean var12;
            String var15;
            CharSequence var16;
            ArrayList var17;
            if (var9) {
               var10 = (MediaController.PhotoEntry)var7;
               if (var10.isVideo) {
                  var8.path = var10.path;
                  var8.videoEditedInfo = var10.editedInfo;
               } else {
                  var15 = var10.imagePath;
                  if (var15 != null) {
                     var8.path = var15;
                  } else {
                     var15 = var10.path;
                     if (var15 != null) {
                        var8.path = var15;
                     }
                  }
               }

               var8.isVideo = var10.isVideo;
               var16 = var10.caption;
               if (var16 != null) {
                  var15 = var16.toString();
               } else {
                  var15 = null;
               }

               var8.caption = var15;
               var8.entities = var10.entities;
               var17 = var11;
               if (!var10.stickers.isEmpty()) {
                  var17 = new ArrayList(var10.stickers);
               }

               var8.masks = var17;
               var8.ttl = var10.ttl;
               var12 = var5;
               var13 = var6;
            } else {
               var12 = var5;
               var13 = var6;
               if (var7 instanceof MediaController.SearchImage) {
                  var11 = (MediaController.SearchImage)var7;
                  var15 = var11.imagePath;
                  if (var15 != null) {
                     var8.path = var15;
                  } else {
                     var8.searchImage = var11;
                  }

                  var16 = var11.caption;
                  if (var16 != null) {
                     var15 = var16.toString();
                  } else {
                     var15 = null;
                  }

                  var8.caption = var15;
                  var8.entities = var11.entities;
                  var17 = var10;
                  if (!var11.stickers.isEmpty()) {
                     var17 = new ArrayList(var11.stickers);
                  }

                  var8.masks = var17;
                  var8.ttl = var11.ttl;
                  var11.date = (int)(System.currentTimeMillis() / 1000L);
                  int var14 = var11.type;
                  MediaController.SearchImage var18;
                  if (var14 == 0) {
                     var18 = (MediaController.SearchImage)this.recentImagesWebKeys.get(var11.id);
                     if (var18 != null) {
                        this.recentWebImages.remove(var18);
                        this.recentWebImages.add(0, var18);
                     } else {
                        this.recentWebImages.add(0, var11);
                     }

                     var12 = true;
                     var13 = var6;
                  } else {
                     var12 = var5;
                     var13 = var6;
                     if (var14 == 1) {
                        var18 = (MediaController.SearchImage)this.recentImagesGifKeys.get(var11.id);
                        if (var18 != null) {
                           this.recentGifImages.remove(var18);
                           this.recentGifImages.add(0, var18);
                        } else {
                           this.recentGifImages.add(0, var11);
                        }

                        var13 = true;
                        var12 = var5;
                     }
                  }
               }
            }

            ++var4;
            var5 = var12;
         }

         if (var5) {
            MessagesStorage.getInstance(super.currentAccount).putWebRecent(this.recentWebImages);
         }

         if (var6) {
            MessagesStorage.getInstance(super.currentAccount).putWebRecent(this.recentGifImages);
         }

         this.delegate.didSelectPhotos(var3);
      }

   }

   public View createView(Context var1) {
      label18: {
         super.actionBar.setBackgroundColor(-13421773);
         super.actionBar.setTitleColor(-1);
         super.actionBar.setItemsColor(-1, false);
         super.actionBar.setItemsBackgroundColor(-12763843, false);
         super.actionBar.setBackButtonImage(2131165409);
         super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            public void onItemClick(int var1) {
               if (var1 == -1) {
                  PhotoAlbumPickerActivity.this.finishFragment();
               } else if (var1 == 1 && PhotoAlbumPickerActivity.this.delegate != null) {
                  PhotoAlbumPickerActivity.this.finishFragment(false);
                  PhotoAlbumPickerActivity.this.delegate.startPhotoSelectActivity();
               }

            }
         });
         super.actionBar.createMenu().addItem(1, 2131165416).setContentDescription(LocaleController.getString("AccDescrMoreOptions", 2131558443));
         super.fragmentView = new FrameLayout(var1);
         FrameLayout var2 = (FrameLayout)super.fragmentView;
         var2.setBackgroundColor(-16777216);
         super.actionBar.setTitle(LocaleController.getString("Gallery", 2131559585));
         this.listView = new RecyclerListView(var1);
         this.listView.setPadding(AndroidUtilities.dp(4.0F), 0, AndroidUtilities.dp(4.0F), AndroidUtilities.dp(4.0F));
         this.listView.setClipToPadding(false);
         this.listView.setHorizontalScrollBarEnabled(false);
         this.listView.setVerticalScrollBarEnabled(false);
         this.listView.setLayoutManager(new LinearLayoutManager(var1, 1, false));
         this.listView.setDrawingCacheEnabled(false);
         var2.addView(this.listView);
         LayoutParams var3 = (LayoutParams)this.listView.getLayoutParams();
         var3.width = -1;
         var3.height = -1;
         var3.bottomMargin = AndroidUtilities.dp(48.0F);
         this.listView.setLayoutParams(var3);
         RecyclerListView var7 = this.listView;
         PhotoAlbumPickerActivity.ListAdapter var4 = new PhotoAlbumPickerActivity.ListAdapter(var1);
         this.listAdapter = var4;
         var7.setAdapter(var4);
         this.listView.setGlowColor(-13421773);
         this.emptyView = new TextView(var1);
         this.emptyView.setTextColor(-8355712);
         this.emptyView.setTextSize(20.0F);
         this.emptyView.setGravity(17);
         this.emptyView.setVisibility(8);
         this.emptyView.setText(LocaleController.getString("NoPhotos", 2131559937));
         var2.addView(this.emptyView);
         var3 = (LayoutParams)this.emptyView.getLayoutParams();
         var3.width = -1;
         var3.height = -1;
         var3.bottomMargin = AndroidUtilities.dp(48.0F);
         this.emptyView.setLayoutParams(var3);
         this.emptyView.setOnTouchListener(_$$Lambda$PhotoAlbumPickerActivity$2ZdkXHoPXptp2wpUszGZ5G4bMiQ.INSTANCE);
         this.progressView = new FrameLayout(var1);
         this.progressView.setVisibility(8);
         var2.addView(this.progressView);
         var3 = (LayoutParams)this.progressView.getLayoutParams();
         var3.width = -1;
         var3.height = -1;
         var3.bottomMargin = AndroidUtilities.dp(48.0F);
         this.progressView.setLayoutParams(var3);
         RadialProgressView var8 = new RadialProgressView(var1);
         this.progressView.addView(var8);
         var3 = (LayoutParams)this.progressView.getLayoutParams();
         var3.width = -2;
         var3.height = -2;
         var3.gravity = 17;
         this.progressView.setLayoutParams(var3);
         this.pickerBottomLayout = new PickerBottomLayout(var1);
         var2.addView(this.pickerBottomLayout);
         LayoutParams var5 = (LayoutParams)this.pickerBottomLayout.getLayoutParams();
         var5.width = -1;
         var5.height = AndroidUtilities.dp(48.0F);
         var5.gravity = 80;
         this.pickerBottomLayout.setLayoutParams(var5);
         this.pickerBottomLayout.cancelButton.setOnClickListener(new _$$Lambda$PhotoAlbumPickerActivity$jb2cegQfO3FSxtdYFSya79907zs(this));
         this.pickerBottomLayout.doneButton.setOnClickListener(new _$$Lambda$PhotoAlbumPickerActivity$vKBIYwKpMugrhprvLvaDbPzl8M0(this));
         if (this.loading) {
            ArrayList var6 = this.albumsSorted;
            if (var6 == null || var6 != null && var6.isEmpty()) {
               this.progressView.setVisibility(0);
               this.listView.setEmptyView((View)null);
               break label18;
            }
         }

         this.progressView.setVisibility(8);
         this.listView.setEmptyView(this.emptyView);
      }

      this.pickerBottomLayout.updateSelectedCount(this.selectedPhotos.size(), true);
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.albumsDidLoad) {
         var1 = (Integer)var3[0];
         if (super.classGuid == var1) {
            if (this.selectPhotoType == 0 && this.allowSearchImages) {
               this.albumsSorted = (ArrayList)var3[1];
            } else {
               this.albumsSorted = (ArrayList)var3[2];
            }

            FrameLayout var5 = this.progressView;
            if (var5 != null) {
               var5.setVisibility(8);
            }

            RecyclerListView var6 = this.listView;
            if (var6 != null && var6.getEmptyView() == null) {
               this.listView.setEmptyView(this.emptyView);
            }

            PhotoAlbumPickerActivity.ListAdapter var7 = this.listAdapter;
            if (var7 != null) {
               var7.notifyDataSetChanged();
            }

            this.loading = false;
         }
      } else if (var1 == NotificationCenter.closeChats) {
         this.removeSelfFromStack();
      } else if (var1 == NotificationCenter.recentImagesDidLoad) {
         var1 = (Integer)var3[0];
         if (var1 == 0) {
            this.recentWebImages = (ArrayList)var3[1];
            this.recentImagesWebKeys.clear();
            Iterator var8 = this.recentWebImages.iterator();

            while(var8.hasNext()) {
               MediaController.SearchImage var4 = (MediaController.SearchImage)var8.next();
               this.recentImagesWebKeys.put(var4.id, var4);
            }
         } else if (var1 == 1) {
            this.recentGifImages = (ArrayList)var3[1];
            this.recentImagesGifKeys.clear();
            Iterator var9 = this.recentGifImages.iterator();

            while(var9.hasNext()) {
               MediaController.SearchImage var10 = (MediaController.SearchImage)var9.next();
               this.recentImagesGifKeys.put(var10.id, var10);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$createView$1$PhotoAlbumPickerActivity(View var1) {
      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$createView$2$PhotoAlbumPickerActivity(View var1) {
      this.sendSelectedPhotos(this.selectedPhotos, this.selectedPhotosOrder);
      this.finishFragment();
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      this.fixLayout();
   }

   public boolean onFragmentCreate() {
      this.loading = true;
      MediaController.loadGalleryPhotosAlbums(super.classGuid);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.albumsDidLoad);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.recentImagesDidLoad);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.closeChats);
      return super.onFragmentCreate();
   }

   public void onFragmentDestroy() {
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.albumsDidLoad);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.recentImagesDidLoad);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.closeChats);
      super.onFragmentDestroy();
   }

   public void onResume() {
      super.onResume();
      PhotoAlbumPickerActivity.ListAdapter var1 = this.listAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

      this.fixLayout();
   }

   public void setAllowSearchImages(boolean var1) {
      this.allowSearchImages = var1;
   }

   public void setDelegate(PhotoAlbumPickerActivity.PhotoAlbumPickerActivityDelegate var1) {
      this.delegate = var1;
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
         boolean var1 = PhotoAlbumPickerActivity.this.allowSearchImages;
         byte var2 = 0;
         int var3 = 0;
         if (!var1) {
            if (PhotoAlbumPickerActivity.this.albumsSorted != null) {
               var3 = (int)Math.ceil((double)((float)PhotoAlbumPickerActivity.this.albumsSorted.size() / (float)PhotoAlbumPickerActivity.this.columnsCount));
            }

            return var3;
         } else {
            var3 = var2;
            if (PhotoAlbumPickerActivity.this.albumsSorted != null) {
               var3 = (int)Math.ceil((double)((float)PhotoAlbumPickerActivity.this.albumsSorted.size() / (float)PhotoAlbumPickerActivity.this.columnsCount));
            }

            return var3 + 1;
         }
      }

      public int getItemViewType(int var1) {
         if (!PhotoAlbumPickerActivity.this.allowSearchImages) {
            return 0;
         } else {
            return var1 == 0 ? 1 : 0;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return true;
      }

      // $FF: synthetic method
      public void lambda$onCreateViewHolder$0$PhotoAlbumPickerActivity$ListAdapter(MediaController.AlbumEntry var1) {
         PhotoAlbumPickerActivity.this.openPhotoPicker(var1, 0);
      }

      // $FF: synthetic method
      public void lambda$onCreateViewHolder$1$PhotoAlbumPickerActivity$ListAdapter(int var1) {
         PhotoAlbumPickerActivity.this.openPhotoPicker((MediaController.AlbumEntry)null, var1);
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         if (var1.getItemViewType() == 0) {
            PhotoPickerAlbumsCell var5 = (PhotoPickerAlbumsCell)var1.itemView;
            var5.setAlbumsCount(PhotoAlbumPickerActivity.this.columnsCount);

            for(int var3 = 0; var3 < PhotoAlbumPickerActivity.this.columnsCount; ++var3) {
               int var4;
               if (!PhotoAlbumPickerActivity.this.allowSearchImages) {
                  var4 = PhotoAlbumPickerActivity.this.columnsCount * var2;
               } else {
                  var4 = (var2 - 1) * PhotoAlbumPickerActivity.this.columnsCount;
               }

               var4 += var3;
               if (var4 < PhotoAlbumPickerActivity.this.albumsSorted.size()) {
                  var5.setAlbum(var3, (MediaController.AlbumEntry)PhotoAlbumPickerActivity.this.albumsSorted.get(var4));
               } else {
                  var5.setAlbum(var3, (MediaController.AlbumEntry)null);
               }
            }

            var5.requestLayout();
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            var3 = new PhotoPickerSearchCell(this.mContext, PhotoAlbumPickerActivity.this.allowGifs);
            ((PhotoPickerSearchCell)var3).setDelegate(new _$$Lambda$PhotoAlbumPickerActivity$ListAdapter$q2tmI74nlxknK7EWcU_vq6eNttA(this));
         } else {
            var3 = new PhotoPickerAlbumsCell(this.mContext);
            ((PhotoPickerAlbumsCell)var3).setDelegate(new _$$Lambda$PhotoAlbumPickerActivity$ListAdapter$wak8B6ZyJqrggtYN7y4fwAyLKCg(this));
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }

   public interface PhotoAlbumPickerActivityDelegate {
      void didSelectPhotos(ArrayList var1);

      void startPhotoSelectActivity();
   }
}
