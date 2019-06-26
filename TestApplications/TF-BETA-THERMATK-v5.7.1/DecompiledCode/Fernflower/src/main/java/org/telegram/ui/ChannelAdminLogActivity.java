package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.LayoutAnimationController;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScrollerMiddle;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.BotHelpCell;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Cells.ChatLoadingCell;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Cells.ChatMessageCell$ChatMessageCellDelegate$_CC;
import org.telegram.ui.Cells.ChatUnreadCell;
import org.telegram.ui.Components.AdminLogFilterAlert;
import org.telegram.ui.Components.ChatAvatarContainer;
import org.telegram.ui.Components.EmbedBottomSheet;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PipRoundVideoView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.URLSpanMono;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.URLSpanReplacement;
import org.telegram.ui.Components.URLSpanUserMention;

public class ChannelAdminLogActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private ArrayList admins;
   private Paint aspectPaint;
   private Path aspectPath;
   private AspectRatioFrameLayout aspectRatioFrameLayout;
   private ChatAvatarContainer avatarContainer;
   private FrameLayout bottomOverlayChat;
   private TextView bottomOverlayChatText;
   private ImageView bottomOverlayImage;
   private ChannelAdminLogActivity.ChatActivityAdapter chatAdapter;
   private LinearLayoutManager chatLayoutManager;
   private RecyclerListView chatListView;
   private ArrayList chatMessageCellsCache = new ArrayList();
   private boolean checkTextureViewPosition;
   private SizeNotifierFrameLayout contentView;
   protected TLRPC.Chat currentChat;
   private TLRPC.TL_channelAdminLogEventsFilter currentFilter = null;
   private boolean currentFloatingDateOnScreen;
   private boolean currentFloatingTopIsNotMessage;
   private TextView emptyView;
   private FrameLayout emptyViewContainer;
   private boolean endReached;
   private AnimatorSet floatingDateAnimation;
   private ChatActionCell floatingDateView;
   private boolean loading;
   private int loadsCount;
   protected ArrayList messages = new ArrayList();
   private HashMap messagesByDays = new HashMap();
   private LongSparseArray messagesDict = new LongSparseArray();
   private int[] mid = new int[]{2};
   private int minDate;
   private long minEventId;
   private boolean openAnimationEnded;
   private boolean paused = true;
   private RadialProgressView progressBar;
   private FrameLayout progressView;
   private View progressView2;
   private PhotoViewer.PhotoViewerProvider provider = new PhotoViewer.EmptyPhotoViewerProvider() {
      public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject var1, TLRPC.FileLocation var2, int var3, boolean var4) {
         int var5 = ChannelAdminLogActivity.this.chatListView.getChildCount();
         byte var6 = 0;
         var3 = 0;

         while(true) {
            Object var7 = null;
            if (var3 >= var5) {
               return null;
            }

            View var8 = ChannelAdminLogActivity.this.chatListView.getChildAt(var3);
            ImageReceiver var9;
            int var12;
            if (var8 instanceof ChatMessageCell) {
               var9 = (ImageReceiver)var7;
               if (var1 != null) {
                  ChatMessageCell var15 = (ChatMessageCell)var8;
                  MessageObject var17 = var15.getMessageObject();
                  var9 = (ImageReceiver)var7;
                  if (var17 != null) {
                     var9 = (ImageReceiver)var7;
                     if (var17.getId() == var1.getId()) {
                        var9 = var15.getPhotoImage();
                     }
                  }
               }
            } else {
               var9 = (ImageReceiver)var7;
               if (var8 instanceof ChatActionCell) {
                  ChatActionCell var11 = (ChatActionCell)var8;
                  MessageObject var10 = var11.getMessageObject();
                  var9 = (ImageReceiver)var7;
                  if (var10 != null) {
                     if (var1 != null) {
                        var9 = (ImageReceiver)var7;
                        if (var10.getId() == var1.getId()) {
                           var9 = var11.getPhotoImage();
                        }
                     } else {
                        var9 = (ImageReceiver)var7;
                        if (var2 != null) {
                           var9 = (ImageReceiver)var7;
                           if (var10.photoThumbs != null) {
                              var12 = 0;

                              while(true) {
                                 var9 = (ImageReceiver)var7;
                                 if (var12 >= var10.photoThumbs.size()) {
                                    break;
                                 }

                                 TLRPC.FileLocation var16 = ((TLRPC.PhotoSize)var10.photoThumbs.get(var12)).location;
                                 if (var16.volume_id == var2.volume_id && var16.local_id == var2.local_id) {
                                    var9 = var11.getPhotoImage();
                                    break;
                                 }

                                 ++var12;
                              }
                           }
                        }
                     }
                  }
               }
            }

            if (var9 != null) {
               int[] var13 = new int[2];
               var8.getLocationInWindow(var13);
               PhotoViewer.PlaceProviderObject var14 = new PhotoViewer.PlaceProviderObject();
               var14.viewX = var13[0];
               var12 = var13[1];
               if (VERSION.SDK_INT >= 21) {
                  var3 = var6;
               } else {
                  var3 = AndroidUtilities.statusBarHeight;
               }

               var14.viewY = var12 - var3;
               var14.parentView = ChannelAdminLogActivity.this.chatListView;
               var14.imageReceiver = var9;
               var14.thumb = var9.getBitmapSafe();
               var14.radius = var9.getRoundRadius();
               var14.isEvent = true;
               return var14;
            }

            ++var3;
         }
      }
   };
   private FrameLayout roundVideoContainer;
   private MessageObject scrollToMessage;
   private int scrollToOffsetOnRecreate = 0;
   private int scrollToPositionOnRecreate = -1;
   private boolean scrollingFloatingDate;
   private ImageView searchCalendarButton;
   private FrameLayout searchContainer;
   private SimpleTextView searchCountText;
   private ImageView searchDownButton;
   private ActionBarMenuItem searchItem;
   private String searchQuery = "";
   private ImageView searchUpButton;
   private boolean searchWas;
   private SparseArray selectedAdmins;
   private MessageObject selectedObject;
   private TextureView videoTextureView;
   private boolean wasPaused = false;

   public ChannelAdminLogActivity(TLRPC.Chat var1) {
      this.currentChat = var1;
   }

   // $FF: synthetic method
   static ActionBarLayout access$1000(ChannelAdminLogActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static ActionBar access$1100(ChannelAdminLogActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$1200(ChannelAdminLogActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBarLayout access$1300(ChannelAdminLogActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static ActionBar access$1400(ChannelAdminLogActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$1500(ChannelAdminLogActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$1600(ChannelAdminLogActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$1700(ChannelAdminLogActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$1800(ChannelAdminLogActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$2100(ChannelAdminLogActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$2200(ChannelAdminLogActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$2300(ChannelAdminLogActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$2400(ChannelAdminLogActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$2500(ChannelAdminLogActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$2600(ChannelAdminLogActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static int access$4200(ChannelAdminLogActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4400(ChannelAdminLogActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4600(ChannelAdminLogActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4700(ChannelAdminLogActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static View access$4800(ChannelAdminLogActivity var0) {
      return var0.fragmentView;
   }

   // $FF: synthetic method
   static int access$5400(ChannelAdminLogActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$5500(ChannelAdminLogActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$5700(ChannelAdminLogActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBar access$900(ChannelAdminLogActivity var0) {
      return var0.actionBar;
   }

   private void addCanBanUser(Bundle var1, int var2) {
      TLRPC.Chat var3 = this.currentChat;
      if (var3.megagroup && this.admins != null && ChatObject.canBlockUsers(var3)) {
         for(int var4 = 0; var4 < this.admins.size(); ++var4) {
            TLRPC.ChannelParticipant var5 = (TLRPC.ChannelParticipant)this.admins.get(var4);
            if (var5.user_id == var2) {
               if (!var5.can_edit) {
                  return;
               }
               break;
            }
         }

         var1.putInt("ban_chat_id", this.currentChat.id);
      }

   }

   private void alertUserOpenError(MessageObject var1) {
      if (this.getParentActivity() != null) {
         AlertDialog.Builder var2 = new AlertDialog.Builder(this.getParentActivity());
         var2.setTitle(LocaleController.getString("AppName", 2131558635));
         var2.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         if (var1.type == 3) {
            var2.setMessage(LocaleController.getString("NoPlayerInstalled", 2131559938));
         } else {
            var2.setMessage(LocaleController.formatString("NoHandleAppInstalled", 2131559926, var1.getDocument().mime_type));
         }

         this.showDialog(var2.create());
      }
   }

   private void checkScrollForLoad(boolean var1) {
      LinearLayoutManager var2 = this.chatLayoutManager;
      if (var2 != null && !this.paused) {
         int var3 = var2.findFirstVisibleItemPosition();
         int var4;
         if (var3 == -1) {
            var4 = 0;
         } else {
            var4 = Math.abs(this.chatLayoutManager.findLastVisibleItemPosition() - var3) + 1;
         }

         if (var4 > 0) {
            this.chatAdapter.getItemCount();
            byte var5;
            if (var1) {
               var5 = 25;
            } else {
               var5 = 5;
            }

            if (var3 <= var5 && !this.loading && !this.endReached) {
               this.loadMessages(false);
            }
         }
      }

   }

   private void createMenu(View var1) {
      MessageObject var7;
      if (var1 instanceof ChatMessageCell) {
         var7 = ((ChatMessageCell)var1).getMessageObject();
      } else if (var1 instanceof ChatActionCell) {
         var7 = ((ChatActionCell)var1).getMessageObject();
      } else {
         var7 = null;
      }

      if (var7 != null) {
         int var2 = this.getMessageType(var7);
         this.selectedObject = var7;
         if (this.getParentActivity() != null) {
            AlertDialog.Builder var3 = new AlertDialog.Builder(this.getParentActivity());
            ArrayList var4 = new ArrayList();
            ArrayList var5 = new ArrayList();
            var7 = this.selectedObject;
            if (var7.type == 0 || var7.caption != null) {
               var4.add(LocaleController.getString("Copy", 2131559163));
               var5.add(3);
            }

            if (var2 == 1) {
               TLRPC.TL_channelAdminLogEvent var8 = this.selectedObject.currentEvent;
               if (var8 != null) {
                  TLRPC.ChannelAdminLogEventAction var9 = var8.action;
                  if (var9 instanceof TLRPC.TL_channelAdminLogEventActionChangeStickerSet) {
                     TLRPC.InputStickerSet var10;
                     label81: {
                        TLRPC.InputStickerSet var6 = var9.new_stickerset;
                        if (var6 != null) {
                           var10 = var6;
                           if (!(var6 instanceof TLRPC.TL_inputStickerSetEmpty)) {
                              break label81;
                           }
                        }

                        var10 = this.selectedObject.currentEvent.action.prev_stickerset;
                     }

                     if (var10 != null) {
                        this.showDialog(new StickersAlert(this.getParentActivity(), this, var10, (TLRPC.TL_messages_stickerSet)null, (StickersAlert.StickersAlertDelegate)null));
                        return;
                     }
                  }
               }
            } else if (var2 == 3) {
               TLRPC.MessageMedia var11 = this.selectedObject.messageOwner.media;
               if (var11 instanceof TLRPC.TL_messageMediaWebPage && MessageObject.isNewGifDocument(var11.webpage.document)) {
                  var4.add(LocaleController.getString("SaveToGIFs", 2131560629));
                  var5.add(11);
               }
            } else if (var2 == 4) {
               if (this.selectedObject.isVideo()) {
                  var4.add(LocaleController.getString("SaveToGallery", 2131560630));
                  var5.add(4);
                  var4.add(LocaleController.getString("ShareFile", 2131560748));
                  var5.add(6);
               } else if (this.selectedObject.isMusic()) {
                  var4.add(LocaleController.getString("SaveToMusic", 2131560632));
                  var5.add(10);
                  var4.add(LocaleController.getString("ShareFile", 2131560748));
                  var5.add(6);
               } else if (this.selectedObject.getDocument() != null) {
                  if (MessageObject.isNewGifDocument(this.selectedObject.getDocument())) {
                     var4.add(LocaleController.getString("SaveToGIFs", 2131560629));
                     var5.add(11);
                  }

                  var4.add(LocaleController.getString("SaveToDownloads", 2131560628));
                  var5.add(10);
                  var4.add(LocaleController.getString("ShareFile", 2131560748));
                  var5.add(6);
               } else {
                  var4.add(LocaleController.getString("SaveToGallery", 2131560630));
                  var5.add(4);
               }
            } else if (var2 == 5) {
               var4.add(LocaleController.getString("ApplyLocalizationFile", 2131558638));
               var5.add(5);
               var4.add(LocaleController.getString("SaveToDownloads", 2131560628));
               var5.add(10);
               var4.add(LocaleController.getString("ShareFile", 2131560748));
               var5.add(6);
            } else if (var2 == 10) {
               var4.add(LocaleController.getString("ApplyThemeFile", 2131558640));
               var5.add(5);
               var4.add(LocaleController.getString("SaveToDownloads", 2131560628));
               var5.add(10);
               var4.add(LocaleController.getString("ShareFile", 2131560748));
               var5.add(6);
            } else if (var2 == 6) {
               var4.add(LocaleController.getString("SaveToGallery", 2131560630));
               var5.add(7);
               var4.add(LocaleController.getString("SaveToDownloads", 2131560628));
               var5.add(10);
               var4.add(LocaleController.getString("ShareFile", 2131560748));
               var5.add(6);
            } else if (var2 == 7) {
               if (this.selectedObject.isMask()) {
                  var4.add(LocaleController.getString("AddToMasks", 2131558592));
               } else {
                  var4.add(LocaleController.getString("AddToStickers", 2131558593));
               }

               var5.add(9);
            } else if (var2 == 8) {
               TLRPC.User var12 = MessagesController.getInstance(super.currentAccount).getUser(this.selectedObject.messageOwner.media.user_id);
               if (var12 != null && var12.id != UserConfig.getInstance(super.currentAccount).getClientUserId() && ContactsController.getInstance(super.currentAccount).contactsDict.get(var12.id) == null) {
                  var4.add(LocaleController.getString("AddContactTitle", 2131558569));
                  var5.add(15);
               }

               String var13 = this.selectedObject.messageOwner.media.phone_number;
               if (var13 != null || var13.length() != 0) {
                  var4.add(LocaleController.getString("Copy", 2131559163));
                  var5.add(16);
                  var4.add(LocaleController.getString("Call", 2131558869));
                  var5.add(17);
               }
            }

            if (!var5.isEmpty()) {
               var3.setItems((CharSequence[])var4.toArray(new CharSequence[0]), new _$$Lambda$ChannelAdminLogActivity$BZ9YJjVsQ77SD_mlnHBXJsrpGqE(this, var5));
               var3.setTitle(LocaleController.getString("Message", 2131559845));
               this.showDialog(var3.create());
            }
         }
      }
   }

   private TextureView createTextureView(boolean var1) {
      if (super.parentLayout == null) {
         return null;
      } else {
         if (this.roundVideoContainer == null) {
            if (VERSION.SDK_INT >= 21) {
               this.roundVideoContainer = new FrameLayout(this.getParentActivity()) {
                  public void setTranslationY(float var1) {
                     super.setTranslationY(var1);
                     ChannelAdminLogActivity.this.contentView.invalidate();
                  }
               };
               this.roundVideoContainer.setOutlineProvider(new ViewOutlineProvider() {
                  @TargetApi(21)
                  public void getOutline(View var1, Outline var2) {
                     int var3 = AndroidUtilities.roundMessageSize;
                     var2.setOval(0, 0, var3, var3);
                  }
               });
               this.roundVideoContainer.setClipToOutline(true);
            } else {
               this.roundVideoContainer = new FrameLayout(this.getParentActivity()) {
                  protected void dispatchDraw(Canvas var1) {
                     super.dispatchDraw(var1);
                     var1.drawPath(ChannelAdminLogActivity.this.aspectPath, ChannelAdminLogActivity.this.aspectPaint);
                  }

                  protected void onSizeChanged(int var1, int var2, int var3, int var4) {
                     super.onSizeChanged(var1, var2, var3, var4);
                     ChannelAdminLogActivity.this.aspectPath.reset();
                     Path var5 = ChannelAdminLogActivity.this.aspectPath;
                     float var6 = (float)(var1 / 2);
                     var5.addCircle(var6, (float)(var2 / 2), var6, Direction.CW);
                     ChannelAdminLogActivity.this.aspectPath.toggleInverseFillType();
                  }

                  public void setTranslationY(float var1) {
                     super.setTranslationY(var1);
                     ChannelAdminLogActivity.this.contentView.invalidate();
                  }

                  public void setVisibility(int var1) {
                     super.setVisibility(var1);
                     if (var1 == 0) {
                        this.setLayerType(2, (Paint)null);
                     }

                  }
               };
               this.aspectPath = new Path();
               this.aspectPaint = new Paint(1);
               this.aspectPaint.setColor(-16777216);
               this.aspectPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
            }

            this.roundVideoContainer.setWillNotDraw(false);
            this.roundVideoContainer.setVisibility(4);
            this.aspectRatioFrameLayout = new AspectRatioFrameLayout(this.getParentActivity());
            this.aspectRatioFrameLayout.setBackgroundColor(0);
            if (var1) {
               this.roundVideoContainer.addView(this.aspectRatioFrameLayout, LayoutHelper.createFrame(-1, -1.0F));
            }

            this.videoTextureView = new TextureView(this.getParentActivity());
            this.videoTextureView.setOpaque(false);
            this.aspectRatioFrameLayout.addView(this.videoTextureView, LayoutHelper.createFrame(-1, -1.0F));
         }

         if (this.roundVideoContainer.getParent() == null) {
            SizeNotifierFrameLayout var2 = this.contentView;
            FrameLayout var3 = this.roundVideoContainer;
            int var4 = AndroidUtilities.roundMessageSize;
            var2.addView(var3, 1, new LayoutParams(var4, var4));
         }

         this.roundVideoContainer.setVisibility(4);
         this.aspectRatioFrameLayout.setDrawingReady(false);
         return this.videoTextureView;
      }
   }

   private void destroyTextureView() {
      FrameLayout var1 = this.roundVideoContainer;
      if (var1 != null && var1.getParent() != null) {
         this.contentView.removeView(this.roundVideoContainer);
         this.aspectRatioFrameLayout.setDrawingReady(false);
         this.roundVideoContainer.setVisibility(4);
         if (VERSION.SDK_INT < 21) {
            this.roundVideoContainer.setLayerType(0, (Paint)null);
         }
      }

   }

   private void fixLayout() {
      ChatAvatarContainer var1 = this.avatarContainer;
      if (var1 != null) {
         var1.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
               if (ChannelAdminLogActivity.this.avatarContainer != null) {
                  ChannelAdminLogActivity.this.avatarContainer.getViewTreeObserver().removeOnPreDrawListener(this);
               }

               return true;
            }
         });
      }

   }

   private String getMessageContent(MessageObject var1, int var2, boolean var3) {
      String var4 = "";
      String var5 = var4;
      if (var3) {
         int var6 = var1.messageOwner.from_id;
         var5 = var4;
         if (var2 != var6) {
            StringBuilder var11;
            if (var6 > 0) {
               TLRPC.User var7 = MessagesController.getInstance(super.currentAccount).getUser(var1.messageOwner.from_id);
               var5 = var4;
               if (var7 != null) {
                  var11 = new StringBuilder();
                  var11.append(ContactsController.formatName(var7.first_name, var7.last_name));
                  var11.append(":\n");
                  var5 = var11.toString();
               }
            } else {
               var5 = var4;
               if (var6 < 0) {
                  TLRPC.Chat var12 = MessagesController.getInstance(super.currentAccount).getChat(-var1.messageOwner.from_id);
                  var5 = var4;
                  if (var12 != null) {
                     var11 = new StringBuilder();
                     var11.append(var12.title);
                     var11.append(":\n");
                     var5 = var11.toString();
                  }
               }
            }
         }
      }

      String var8;
      StringBuilder var10;
      if (var1.type == 0 && var1.messageOwner.message != null) {
         var10 = new StringBuilder();
         var10.append(var5);
         var10.append(var1.messageOwner.message);
         var8 = var10.toString();
      } else {
         TLRPC.Message var9 = var1.messageOwner;
         if (var9.media != null && var9.message != null) {
            var10 = new StringBuilder();
            var10.append(var5);
            var10.append(var1.messageOwner.message);
            var8 = var10.toString();
         } else {
            var10 = new StringBuilder();
            var10.append(var5);
            var10.append(var1.messageText);
            var8 = var10.toString();
         }
      }

      return var8;
   }

   private int getMessageType(MessageObject var1) {
      if (var1 == null) {
         return -1;
      } else {
         int var2 = var1.type;
         if (var2 == 6) {
            return -1;
         } else if (var2 != 10 && var2 != 11 && var2 != 16) {
            if (var1.isVoice()) {
               return 2;
            } else {
               if (var1.isSticker()) {
                  TLRPC.InputStickerSet var5 = var1.getInputStickerSet();
                  if (var5 instanceof TLRPC.TL_inputStickerSetID) {
                     if (!DataQuery.getInstance(super.currentAccount).isStickerPackInstalled(var5.id)) {
                        return 7;
                     }
                  } else if (var5 instanceof TLRPC.TL_inputStickerSetShortName && !DataQuery.getInstance(super.currentAccount).isStickerPackInstalled(var5.short_name)) {
                     return 7;
                  }
               } else if (var1.isRoundVideo() && (!var1.isRoundVideo() || !BuildVars.DEBUG_VERSION) || !(var1.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) && var1.getDocument() == null && !var1.isMusic() && !var1.isVideo()) {
                  if (var1.type == 12) {
                     return 8;
                  }

                  if (var1.isMediaEmpty()) {
                     return 3;
                  }
               } else {
                  boolean var3 = false;
                  String var4 = var1.messageOwner.attachPath;
                  boolean var6 = var3;
                  if (var4 != null) {
                     var6 = var3;
                     if (var4.length() != 0) {
                        var6 = var3;
                        if ((new File(var1.messageOwner.attachPath)).exists()) {
                           var6 = true;
                        }
                     }
                  }

                  var3 = var6;
                  if (!var6) {
                     var3 = var6;
                     if (FileLoader.getPathToMessage(var1.messageOwner).exists()) {
                        var3 = true;
                     }
                  }

                  if (var3) {
                     if (var1.getDocument() != null) {
                        var4 = var1.getDocument().mime_type;
                        if (var4 != null) {
                           if (var1.getDocumentName().toLowerCase().endsWith("attheme")) {
                              return 10;
                           }

                           if (var4.endsWith("/xml")) {
                              return 5;
                           }

                           if (var4.endsWith("/png") || var4.endsWith("/jpg") || var4.endsWith("/jpeg")) {
                              return 6;
                           }
                        }
                     }

                     return 4;
                  }
               }

               return 2;
            }
         } else {
            return var1.getId() == 0 ? -1 : 1;
         }
      }
   }

   private void hideFloatingDateView(boolean var1) {
      if (this.floatingDateView.getTag() != null && !this.currentFloatingDateOnScreen && (!this.scrollingFloatingDate || this.currentFloatingTopIsNotMessage)) {
         this.floatingDateView.setTag((Object)null);
         if (var1) {
            this.floatingDateAnimation = new AnimatorSet();
            this.floatingDateAnimation.setDuration(150L);
            this.floatingDateAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.floatingDateView, "alpha", new float[]{0.0F})});
            this.floatingDateAnimation.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  if (var1.equals(ChannelAdminLogActivity.this.floatingDateAnimation)) {
                     ChannelAdminLogActivity.this.floatingDateAnimation = null;
                  }

               }
            });
            this.floatingDateAnimation.setStartDelay(500L);
            this.floatingDateAnimation.start();
         } else {
            AnimatorSet var2 = this.floatingDateAnimation;
            if (var2 != null) {
               var2.cancel();
               this.floatingDateAnimation = null;
            }

            this.floatingDateView.setAlpha(0.0F);
         }
      }

   }

   // $FF: synthetic method
   static boolean lambda$createView$2(View var0, MotionEvent var1) {
      return true;
   }

   // $FF: synthetic method
   static void lambda$null$8(DialogInterface var0, int var1) {
   }

   // $FF: synthetic method
   static void lambda$null$9(DatePicker var0, DialogInterface var1) {
      int var2 = var0.getChildCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         View var5 = var0.getChildAt(var3);
         android.view.ViewGroup.LayoutParams var4 = var5.getLayoutParams();
         var4.width = -1;
         var5.setLayoutParams(var4);
      }

   }

   private void loadAdmins() {
      TLRPC.TL_channels_getParticipants var1 = new TLRPC.TL_channels_getParticipants();
      var1.channel = MessagesController.getInputChannel(this.currentChat);
      var1.filter = new TLRPC.TL_channelParticipantsAdmins();
      var1.offset = 0;
      var1.limit = 200;
      int var2 = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var1, new _$$Lambda$ChannelAdminLogActivity$v_o39r5RzWtyA17rINStweDzJAI(this));
      ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(var2, super.classGuid);
   }

   private void loadMessages(boolean var1) {
      if (!this.loading) {
         int var2 = 0;
         if (var1) {
            this.minEventId = Long.MAX_VALUE;
            FrameLayout var3 = this.progressView;
            if (var3 != null) {
               var3.setVisibility(0);
               this.emptyViewContainer.setVisibility(4);
               this.chatListView.setEmptyView((View)null);
            }

            this.messagesDict.clear();
            this.messages.clear();
            this.messagesByDays.clear();
         }

         this.loading = true;
         TLRPC.TL_channels_getAdminLog var4 = new TLRPC.TL_channels_getAdminLog();
         var4.channel = MessagesController.getInputChannel(this.currentChat);
         var4.q = this.searchQuery;
         var4.limit = 50;
         if (!var1 && !this.messages.isEmpty()) {
            var4.max_id = this.minEventId;
         } else {
            var4.max_id = 0L;
         }

         var4.min_id = 0L;
         TLRPC.TL_channelAdminLogEventsFilter var5 = this.currentFilter;
         if (var5 != null) {
            var4.flags |= 1;
            var4.events_filter = var5;
         }

         if (this.selectedAdmins != null) {
            for(var4.flags |= 2; var2 < this.selectedAdmins.size(); ++var2) {
               var4.admins.add(MessagesController.getInstance(super.currentAccount).getInputUser((TLRPC.User)this.selectedAdmins.valueAt(var2)));
            }
         }

         this.updateEmptyPlaceholder();
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var4, new _$$Lambda$ChannelAdminLogActivity$eETtXTeGU9y5fKwho2AV8a_Izoc(this));
         if (var1) {
            ChannelAdminLogActivity.ChatActivityAdapter var6 = this.chatAdapter;
            if (var6 != null) {
               var6.notifyDataSetChanged();
            }
         }

      }
   }

   private void moveScrollToLastMessage() {
      if (this.chatListView != null && !this.messages.isEmpty()) {
         this.chatLayoutManager.scrollToPositionWithOffset(this.messages.size() - 1, -100000 - this.chatListView.getPaddingTop());
      }

   }

   private void processSelectedOption(int var1) {
      MessageObject var2 = this.selectedObject;
      if (var2 != null) {
         byte var3 = 3;
         byte var4 = 0;
         Activity var7;
         byte var10;
         String var14;
         String var15;
         File var16;
         switch(var1) {
         case 3:
            AndroidUtilities.addToClipboard(this.getMessageContent(var2, 0, true));
            break;
         case 4:
            var15 = var2.messageOwner.attachPath;
            var14 = var15;
            if (var15 != null) {
               var14 = var15;
               if (var15.length() > 0) {
                  var14 = var15;
                  if (!(new File(var15)).exists()) {
                     var14 = null;
                  }
               }
            }

            label184: {
               if (var14 != null) {
                  var15 = var14;
                  if (var14.length() != 0) {
                     break label184;
                  }
               }

               var15 = FileLoader.getPathToMessage(this.selectedObject.messageOwner).toString();
            }

            var1 = this.selectedObject.type;
            if (var1 == 3 || var1 == 1) {
               if (VERSION.SDK_INT >= 23 && this.getParentActivity().checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                  this.getParentActivity().requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
                  this.selectedObject = null;
                  return;
               }

               Activity var24 = this.getParentActivity();
               var10 = var4;
               if (this.selectedObject.type == 3) {
                  var10 = 1;
               }

               MediaController.saveFile(var15, var24, var10, (String)null, (String)null);
            }
            break;
         case 5:
            File var19;
            label175: {
               var14 = var2.messageOwner.attachPath;
               if (var14 != null && var14.length() != 0) {
                  var19 = new File(this.selectedObject.messageOwner.attachPath);
                  if (var19.exists()) {
                     break label175;
                  }
               }

               var19 = null;
            }

            File var17 = var19;
            if (var19 == null) {
               var16 = FileLoader.getPathToMessage(this.selectedObject.messageOwner);
               var17 = var19;
               if (var16.exists()) {
                  var17 = var16;
               }
            }

            if (var17 != null) {
               AlertDialog.Builder var23;
               if (var17.getName().toLowerCase().endsWith("attheme")) {
                  LinearLayoutManager var20 = this.chatLayoutManager;
                  if (var20 != null) {
                     if (var20.findLastVisibleItemPosition() < this.chatLayoutManager.getItemCount() - 1) {
                        this.scrollToPositionOnRecreate = this.chatLayoutManager.findFirstVisibleItemPosition();
                        RecyclerListView.Holder var21 = (RecyclerListView.Holder)this.chatListView.findViewHolderForAdapterPosition(this.scrollToPositionOnRecreate);
                        if (var21 != null) {
                           this.scrollToOffsetOnRecreate = var21.itemView.getTop();
                        } else {
                           this.scrollToPositionOnRecreate = -1;
                        }
                     } else {
                        this.scrollToPositionOnRecreate = -1;
                     }
                  }

                  Theme.ThemeInfo var22 = Theme.applyThemeFile(var17, this.selectedObject.getDocumentName(), true);
                  if (var22 != null) {
                     this.presentFragment(new ThemePreviewActivity(var17, var22));
                  } else {
                     this.scrollToPositionOnRecreate = -1;
                     if (this.getParentActivity() == null) {
                        this.selectedObject = null;
                        return;
                     }

                     var23 = new AlertDialog.Builder(this.getParentActivity());
                     var23.setTitle(LocaleController.getString("AppName", 2131558635));
                     var23.setMessage(LocaleController.getString("IncorrectTheme", 2131559664));
                     var23.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
                     this.showDialog(var23.create());
                  }
               } else if (LocaleController.getInstance().applyLanguageFile(var17, super.currentAccount)) {
                  this.presentFragment(new LanguageSelectActivity());
               } else {
                  if (this.getParentActivity() == null) {
                     this.selectedObject = null;
                     return;
                  }

                  var23 = new AlertDialog.Builder(this.getParentActivity());
                  var23.setTitle(LocaleController.getString("AppName", 2131558635));
                  var23.setMessage(LocaleController.getString("IncorrectLocalization", 2131559663));
                  var23.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
                  this.showDialog(var23.create());
               }
            }
            break;
         case 6:
            var15 = var2.messageOwner.attachPath;
            var14 = var15;
            if (var15 != null) {
               var14 = var15;
               if (var15.length() > 0) {
                  var14 = var15;
                  if (!(new File(var15)).exists()) {
                     var14 = null;
                  }
               }
            }

            label168: {
               if (var14 != null) {
                  var15 = var14;
                  if (var14.length() != 0) {
                     break label168;
                  }
               }

               var15 = FileLoader.getPathToMessage(this.selectedObject.messageOwner).toString();
            }

            Intent var18 = new Intent("android.intent.action.SEND");
            var18.setType(this.selectedObject.getDocument().mime_type);
            if (VERSION.SDK_INT >= 24) {
               try {
                  var7 = this.getParentActivity();
                  var16 = new File(var15);
                  var18.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(var7, "org.telegram.messenger.provider", var16));
                  var18.setFlags(1);
               } catch (Exception var8) {
                  var18.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(var15)));
               }
            } else {
               var18.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(var15)));
            }

            this.getParentActivity().startActivityForResult(Intent.createChooser(var18, LocaleController.getString("ShareFile", 2131560748)), 500);
            break;
         case 7:
            var15 = var2.messageOwner.attachPath;
            var14 = var15;
            if (var15 != null) {
               var14 = var15;
               if (var15.length() > 0) {
                  var14 = var15;
                  if (!(new File(var15)).exists()) {
                     var14 = null;
                  }
               }
            }

            label162: {
               if (var14 != null) {
                  var15 = var14;
                  if (var14.length() != 0) {
                     break label162;
                  }
               }

               var15 = FileLoader.getPathToMessage(this.selectedObject.messageOwner).toString();
            }

            if (VERSION.SDK_INT >= 23 && this.getParentActivity().checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
               this.getParentActivity().requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
               this.selectedObject = null;
               return;
            }

            MediaController.saveFile(var15, this.getParentActivity(), 0, (String)null, (String)null);
         case 8:
         case 12:
         case 13:
         case 14:
         default:
            break;
         case 9:
            this.showDialog(new StickersAlert(this.getParentActivity(), this, this.selectedObject.getInputStickerSet(), (TLRPC.TL_messages_stickerSet)null, (StickersAlert.StickersAlertDelegate)null));
            break;
         case 10:
            if (VERSION.SDK_INT >= 23 && this.getParentActivity().checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
               this.getParentActivity().requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
               this.selectedObject = null;
               return;
            }

            var14 = FileLoader.getDocumentFileName(this.selectedObject.getDocument());
            var15 = var14;
            if (TextUtils.isEmpty(var14)) {
               var15 = this.selectedObject.getFileName();
            }

            String var6 = this.selectedObject.messageOwner.attachPath;
            var14 = var6;
            if (var6 != null) {
               var14 = var6;
               if (var6.length() > 0) {
                  var14 = var6;
                  if (!(new File(var6)).exists()) {
                     var14 = null;
                  }
               }
            }

            label153: {
               if (var14 != null) {
                  var6 = var14;
                  if (var14.length() != 0) {
                     break label153;
                  }
               }

               var6 = FileLoader.getPathToMessage(this.selectedObject.messageOwner).toString();
            }

            var7 = this.getParentActivity();
            if (this.selectedObject.isMusic()) {
               var10 = var3;
            } else {
               var10 = 2;
            }

            if (this.selectedObject.getDocument() != null) {
               var14 = this.selectedObject.getDocument().mime_type;
            } else {
               var14 = "";
            }

            MediaController.saveFile(var6, var7, var10, var15, var14);
            break;
         case 11:
            TLRPC.Document var13 = var2.getDocument();
            MessagesController.getInstance(super.currentAccount).saveGif(this.selectedObject, var13);
            break;
         case 15:
            Bundle var12 = new Bundle();
            var12.putInt("user_id", this.selectedObject.messageOwner.media.user_id);
            var12.putString("phone", this.selectedObject.messageOwner.media.phone_number);
            var12.putBoolean("addContact", true);
            this.presentFragment(new ContactAddActivity(var12));
            break;
         case 16:
            AndroidUtilities.addToClipboard(var2.messageOwner.media.phone_number);
            break;
         case 17:
            try {
               StringBuilder var11 = new StringBuilder();
               var11.append("tel:");
               var11.append(this.selectedObject.messageOwner.media.phone_number);
               Intent var5 = new Intent("android.intent.action.DIAL", Uri.parse(var11.toString()));
               var5.addFlags(268435456);
               this.getParentActivity().startActivityForResult(var5, 500);
            } catch (Exception var9) {
               FileLog.e((Throwable)var9);
            }
         }

         this.selectedObject = null;
      }
   }

   private void removeMessageObject(MessageObject var1) {
      int var2 = this.messages.indexOf(var1);
      if (var2 != -1) {
         this.messages.remove(var2);
         ChannelAdminLogActivity.ChatActivityAdapter var3 = this.chatAdapter;
         if (var3 != null) {
            var3.notifyItemRemoved(var3.messagesStartRow + this.messages.size() - var2 - 1);
         }

      }
   }

   private void updateBottomOverlay() {
   }

   private void updateEmptyPlaceholder() {
      if (this.emptyView != null) {
         if (!TextUtils.isEmpty(this.searchQuery)) {
            this.emptyView.setPadding(AndroidUtilities.dp(8.0F), AndroidUtilities.dp(5.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(5.0F));
            this.emptyView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("EventLogEmptyTextSearch", 2131559409, this.searchQuery)));
         } else if (this.selectedAdmins == null && this.currentFilter == null) {
            this.emptyView.setPadding(AndroidUtilities.dp(16.0F), AndroidUtilities.dp(16.0F), AndroidUtilities.dp(16.0F), AndroidUtilities.dp(16.0F));
            if (this.currentChat.megagroup) {
               this.emptyView.setText(AndroidUtilities.replaceTags(LocaleController.getString("EventLogEmpty", 2131559406)));
            } else {
               this.emptyView.setText(AndroidUtilities.replaceTags(LocaleController.getString("EventLogEmptyChannel", 2131559407)));
            }
         } else {
            this.emptyView.setPadding(AndroidUtilities.dp(8.0F), AndroidUtilities.dp(5.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(5.0F));
            this.emptyView.setText(AndroidUtilities.replaceTags(LocaleController.getString("EventLogEmptySearch", 2131559408)));
         }

      }
   }

   private void updateMessagesVisisblePart() {
      RecyclerListView var1 = this.chatListView;
      if (var1 != null) {
         int var2 = var1.getChildCount();
         int var3 = this.chatListView.getMeasuredHeight();
         int var4 = 0;
         boolean var5 = false;
         View var20 = null;
         View var6 = null;
         View var7 = null;
         int var8 = Integer.MAX_VALUE;

         boolean var11;
         int var16;
         for(int var9 = Integer.MAX_VALUE; var4 < var2; var9 = var16) {
            View var10 = this.chatListView.getChildAt(var4);
            var11 = var10 instanceof ChatMessageCell;
            boolean var12 = var5;
            int var15;
            if (var11) {
               ChatMessageCell var13 = (ChatMessageCell)var10;
               int var14 = var13.getTop();
               var13.getBottom();
               int var27;
               if (var14 >= 0) {
                  var27 = 0;
               } else {
                  var27 = -var14;
               }

               var15 = var13.getMeasuredHeight();
               var16 = var15;
               if (var15 > var3) {
                  var16 = var27 + var3;
               }

               var13.setVisiblePart(var27, var16 - var27);
               MessageObject var17 = var13.getMessageObject();
               var12 = var5;
               if (this.roundVideoContainer != null) {
                  var12 = var5;
                  if (var17.isRoundVideo()) {
                     var12 = var5;
                     if (MediaController.getInstance().isPlayingMessage(var17)) {
                        ImageReceiver var29 = var13.getPhotoImage();
                        this.roundVideoContainer.setTranslationX((float)var29.getImageX());
                        this.roundVideoContainer.setTranslationY((float)(super.fragmentView.getPaddingTop() + var14 + var29.getImageY()));
                        super.fragmentView.invalidate();
                        this.roundVideoContainer.invalidate();
                        var12 = true;
                     }
                  }
               }
            }

            View var18;
            View var28;
            if (var10.getBottom() <= this.chatListView.getPaddingTop()) {
               var28 = var6;
               var18 = var7;
               var16 = var9;
            } else {
               var15 = var10.getBottom();
               View var30 = var20;
               int var24 = var8;
               if (var15 < var8) {
                  if (var11 || var10 instanceof ChatActionCell) {
                     var20 = var10;
                  }

                  var24 = var15;
                  var7 = var10;
                  var30 = var20;
               }

               var20 = var30;
               var28 = var6;
               var18 = var7;
               var8 = var24;
               var16 = var9;
               if (var10 instanceof ChatActionCell) {
                  var20 = var30;
                  var28 = var6;
                  var18 = var7;
                  var8 = var24;
                  var16 = var9;
                  if (((ChatActionCell)var10).getMessageObject().isDateObject) {
                     if (var10.getAlpha() != 1.0F) {
                        var10.setAlpha(1.0F);
                     }

                     var20 = var30;
                     var28 = var6;
                     var18 = var7;
                     var8 = var24;
                     var16 = var9;
                     if (var15 < var9) {
                        var16 = var15;
                        var8 = var24;
                        var18 = var7;
                        var28 = var10;
                        var20 = var30;
                     }
                  }
               }
            }

            ++var4;
            var5 = var12;
            var6 = var28;
            var7 = var18;
         }

         FrameLayout var25 = this.roundVideoContainer;
         if (var25 != null) {
            if (!var5) {
               var25.setTranslationY((float)(-AndroidUtilities.roundMessageSize - 100));
               super.fragmentView.invalidate();
               MessageObject var26 = MediaController.getInstance().getPlayingMessageObject();
               if (var26 != null && var26.isRoundVideo() && this.checkTextureViewPosition) {
                  MediaController.getInstance().setCurrentVideoVisible(false);
               }
            } else {
               MediaController.getInstance().setCurrentVideoVisible(true);
            }
         }

         if (var20 != null) {
            MessageObject var21;
            if (var20 instanceof ChatMessageCell) {
               var21 = ((ChatMessageCell)var20).getMessageObject();
            } else {
               var21 = ((ChatActionCell)var20).getMessageObject();
            }

            this.floatingDateView.setCustomDate(var21.messageOwner.date);
         }

         boolean var19 = false;
         this.currentFloatingDateOnScreen = false;
         var11 = var19;
         if (!(var7 instanceof ChatMessageCell)) {
            var11 = var19;
            if (!(var7 instanceof ChatActionCell)) {
               var11 = true;
            }
         }

         this.currentFloatingTopIsNotMessage = var11;
         if (var6 != null) {
            if (var6.getTop() <= this.chatListView.getPaddingTop() && !this.currentFloatingTopIsNotMessage) {
               if (var6.getAlpha() != 0.0F) {
                  var6.setAlpha(0.0F);
               }

               AnimatorSet var22 = this.floatingDateAnimation;
               if (var22 != null) {
                  var22.cancel();
                  this.floatingDateAnimation = null;
               }

               if (this.floatingDateView.getTag() == null) {
                  this.floatingDateView.setTag(1);
               }

               if (this.floatingDateView.getAlpha() != 1.0F) {
                  this.floatingDateView.setAlpha(1.0F);
               }

               this.currentFloatingDateOnScreen = true;
            } else {
               if (var6.getAlpha() != 1.0F) {
                  var6.setAlpha(1.0F);
               }

               this.hideFloatingDateView(this.currentFloatingTopIsNotMessage ^ true);
            }

            var8 = var6.getBottom() - this.chatListView.getPaddingTop();
            if (var8 > this.floatingDateView.getMeasuredHeight() && var8 < this.floatingDateView.getMeasuredHeight() * 2) {
               ChatActionCell var23 = this.floatingDateView;
               var23.setTranslationY((float)(-var23.getMeasuredHeight() * 2 + var8));
            } else {
               this.floatingDateView.setTranslationY(0.0F);
            }
         } else {
            this.hideFloatingDateView(true);
            this.floatingDateView.setTranslationY(0.0F);
         }

      }
   }

   private void updateTextureViewPosition() {
      int var1 = this.chatListView.getChildCount();
      int var2 = 0;

      boolean var5;
      while(true) {
         if (var2 >= var1) {
            var5 = false;
            break;
         }

         View var3 = this.chatListView.getChildAt(var2);
         if (var3 instanceof ChatMessageCell) {
            ChatMessageCell var6 = (ChatMessageCell)var3;
            MessageObject var4 = var6.getMessageObject();
            if (this.roundVideoContainer != null && var4.isRoundVideo() && MediaController.getInstance().isPlayingMessage(var4)) {
               ImageReceiver var8 = var6.getPhotoImage();
               this.roundVideoContainer.setTranslationX((float)var8.getImageX());
               this.roundVideoContainer.setTranslationY((float)(super.fragmentView.getPaddingTop() + var6.getTop() + var8.getImageY()));
               super.fragmentView.invalidate();
               this.roundVideoContainer.invalidate();
               var5 = true;
               break;
            }
         }

         ++var2;
      }

      if (this.roundVideoContainer != null) {
         MessageObject var7 = MediaController.getInstance().getPlayingMessageObject();
         if (!var5) {
            this.roundVideoContainer.setTranslationY((float)(-AndroidUtilities.roundMessageSize - 100));
            super.fragmentView.invalidate();
            if (var7 != null && var7.isRoundVideo() && (this.checkTextureViewPosition || PipRoundVideoView.getInstance() != null)) {
               MediaController.getInstance().setCurrentVideoVisible(false);
            }
         } else {
            MediaController.getInstance().setCurrentVideoVisible(true);
         }
      }

   }

   public View createView(Context var1) {
      int var2;
      if (this.chatMessageCellsCache.isEmpty()) {
         for(var2 = 0; var2 < 8; ++var2) {
            this.chatMessageCellsCache.add(new ChatMessageCell(var1));
         }
      }

      this.searchWas = false;
      super.hasOwnBackground = true;
      Theme.createChatResources(var1, false);
      super.actionBar.setAddToContainer(false);
      ActionBar var3 = super.actionBar;
      boolean var4;
      if (VERSION.SDK_INT >= 21 && !AndroidUtilities.isTablet()) {
         var4 = true;
      } else {
         var4 = false;
      }

      var3.setOccupyStatusBar(var4);
      super.actionBar.setBackButtonDrawable(new BackDrawable(false));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               ChannelAdminLogActivity.this.finishFragment();
            }

         }
      });
      this.avatarContainer = new ChatAvatarContainer(var1, (ChatActivity)null, false);
      this.avatarContainer.setOccupyStatusBar(AndroidUtilities.isTablet() ^ true);
      super.actionBar.addView(this.avatarContainer, 0, LayoutHelper.createFrame(-2, -1.0F, 51, 56.0F, 0.0F, 40.0F, 0.0F));
      this.searchItem = super.actionBar.createMenu().addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
         public void onSearchCollapse() {
            ChannelAdminLogActivity.this.searchQuery = "";
            ChannelAdminLogActivity.this.avatarContainer.setVisibility(0);
            if (ChannelAdminLogActivity.this.searchWas) {
               ChannelAdminLogActivity.this.searchWas = false;
               ChannelAdminLogActivity.this.loadMessages(true);
            }

            ChannelAdminLogActivity.this.updateBottomOverlay();
         }

         public void onSearchExpand() {
            ChannelAdminLogActivity.this.avatarContainer.setVisibility(8);
            ChannelAdminLogActivity.this.updateBottomOverlay();
         }

         public void onSearchPressed(EditText var1) {
            ChannelAdminLogActivity.this.searchWas = true;
            ChannelAdminLogActivity.this.searchQuery = var1.getText().toString();
            ChannelAdminLogActivity.this.loadMessages(true);
         }
      });
      this.searchItem.setSearchFieldHint(LocaleController.getString("Search", 2131560640));
      this.avatarContainer.setEnabled(false);
      this.avatarContainer.setTitle(this.currentChat.title);
      this.avatarContainer.setSubtitle(LocaleController.getString("EventLogAllEvents", 2131559385));
      this.avatarContainer.setChatAvatar(this.currentChat);
      super.fragmentView = new SizeNotifierFrameLayout(var1) {
         protected boolean drawChild(Canvas var1, View var2, long var3) {
            boolean var5 = super.drawChild(var1, var2, var3);
            if (var2 == ChannelAdminLogActivity.access$900(ChannelAdminLogActivity.this) && ChannelAdminLogActivity.access$1000(ChannelAdminLogActivity.this) != null) {
               ActionBarLayout var7 = ChannelAdminLogActivity.access$1300(ChannelAdminLogActivity.this);
               int var6;
               if (ChannelAdminLogActivity.access$1100(ChannelAdminLogActivity.this).getVisibility() == 0) {
                  var6 = ChannelAdminLogActivity.access$1200(ChannelAdminLogActivity.this).getMeasuredHeight();
               } else {
                  var6 = 0;
               }

               var7.drawHeaderShadow(var1, var6);
            }

            return var5;
         }

         protected boolean isActionBarVisible() {
            boolean var1;
            if (ChannelAdminLogActivity.access$1400(ChannelAdminLogActivity.this).getVisibility() == 0) {
               var1 = true;
            } else {
               var1 = false;
            }

            return var1;
         }

         protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            MessageObject var1 = MediaController.getInstance().getPlayingMessageObject();
            if (var1 != null && var1.isRoundVideo() && var1.eventId != 0L && var1.getDialogId() == (long)(-ChannelAdminLogActivity.this.currentChat.id)) {
               MediaController.getInstance().setTextureView(ChannelAdminLogActivity.this.createTextureView(false), ChannelAdminLogActivity.this.aspectRatioFrameLayout, ChannelAdminLogActivity.this.roundVideoContainer, true);
            }

         }

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            int var6 = this.getChildCount();

            for(int var7 = 0; var7 < var6; ++var7) {
               View var8 = this.getChildAt(var7);
               if (var8.getVisibility() != 8) {
                  LayoutParams var9 = (LayoutParams)var8.getLayoutParams();
                  int var10 = var8.getMeasuredWidth();
                  int var11 = var8.getMeasuredHeight();
                  int var12 = var9.gravity;
                  int var13 = var12;
                  if (var12 == -1) {
                     var13 = 51;
                  }

                  int var14;
                  label65: {
                     var14 = var13 & 112;
                     var13 = var13 & 7 & 7;
                     if (var13 != 1) {
                        if (var13 != 5) {
                           var12 = var9.leftMargin;
                           break label65;
                        }

                        var12 = var4 - var10;
                        var13 = var9.rightMargin;
                     } else {
                        var12 = (var4 - var2 - var10) / 2 + var9.leftMargin;
                        var13 = var9.rightMargin;
                     }

                     var12 -= var13;
                  }

                  label59: {
                     if (var14 != 16) {
                        if (var14 == 48) {
                           var14 = var9.topMargin + this.getPaddingTop();
                           var13 = var14;
                           if (var8 != ChannelAdminLogActivity.access$2100(ChannelAdminLogActivity.this)) {
                              var13 = var14;
                              if (ChannelAdminLogActivity.access$2200(ChannelAdminLogActivity.this).getVisibility() == 0) {
                                 var13 = var14 + ChannelAdminLogActivity.access$2300(ChannelAdminLogActivity.this).getMeasuredHeight();
                              }
                           }
                           break label59;
                        }

                        if (var14 != 80) {
                           var13 = var9.topMargin;
                           break label59;
                        }

                        var13 = var5 - var3 - var11;
                        var14 = var9.bottomMargin;
                     } else {
                        var13 = (var5 - var3 - var11) / 2 + var9.topMargin;
                        var14 = var9.bottomMargin;
                     }

                     var13 -= var14;
                  }

                  label51: {
                     if (var8 == ChannelAdminLogActivity.this.emptyViewContainer) {
                        int var15 = AndroidUtilities.dp(24.0F);
                        if (ChannelAdminLogActivity.access$2400(ChannelAdminLogActivity.this).getVisibility() == 0) {
                           var14 = ChannelAdminLogActivity.access$2500(ChannelAdminLogActivity.this).getMeasuredHeight() / 2;
                        } else {
                           var14 = 0;
                        }

                        var14 = var15 - var14;
                     } else {
                        var14 = var13;
                        if (var8 != ChannelAdminLogActivity.access$2600(ChannelAdminLogActivity.this)) {
                           break label51;
                        }

                        var14 = this.getPaddingTop();
                     }

                     var14 = var13 - var14;
                  }

                  var8.layout(var12, var14, var10 + var12, var11 + var14);
               }
            }

            ChannelAdminLogActivity.this.updateMessagesVisisblePart();
            this.notifyHeightChanged();
         }

         protected void onMeasure(int var1, int var2) {
            int var3 = MeasureSpec.getSize(var1);
            int var4 = MeasureSpec.getSize(var2);
            this.setMeasuredDimension(var3, var4);
            int var5 = var4 - this.getPaddingTop();
            this.measureChildWithMargins(ChannelAdminLogActivity.access$1500(ChannelAdminLogActivity.this), var1, 0, var2, 0);
            int var6 = ChannelAdminLogActivity.access$1600(ChannelAdminLogActivity.this).getMeasuredHeight();
            var4 = var5;
            if (ChannelAdminLogActivity.access$1700(ChannelAdminLogActivity.this).getVisibility() == 0) {
               var4 = var5 - var6;
            }

            this.getKeyboardHeight();
            var6 = this.getChildCount();

            for(var5 = 0; var5 < var6; ++var5) {
               View var7 = this.getChildAt(var5);
               if (var7 != null && var7.getVisibility() != 8 && var7 != ChannelAdminLogActivity.access$1800(ChannelAdminLogActivity.this)) {
                  if (var7 != ChannelAdminLogActivity.this.chatListView && var7 != ChannelAdminLogActivity.this.progressView) {
                     if (var7 == ChannelAdminLogActivity.this.emptyViewContainer) {
                        var7.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var4, 1073741824));
                     } else {
                        this.measureChildWithMargins(var7, var1, 0, var2, 0);
                     }
                  } else {
                     var7.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(Math.max(AndroidUtilities.dp(10.0F), var4 - AndroidUtilities.dp(50.0F)), 1073741824));
                  }
               }
            }

         }
      };
      this.contentView = (SizeNotifierFrameLayout)super.fragmentView;
      this.contentView.setOccupyStatusBar(AndroidUtilities.isTablet() ^ true);
      this.contentView.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());
      this.emptyViewContainer = new FrameLayout(var1);
      this.emptyViewContainer.setVisibility(4);
      this.contentView.addView(this.emptyViewContainer, LayoutHelper.createFrame(-1, -2, 17));
      this.emptyViewContainer.setOnTouchListener(_$$Lambda$ChannelAdminLogActivity$Xh6zBvXYfqVbautwsQ1g7PDF8js.INSTANCE);
      this.emptyView = new TextView(var1);
      this.emptyView.setTextSize(1, 14.0F);
      this.emptyView.setGravity(17);
      this.emptyView.setTextColor(Theme.getColor("chat_serviceText"));
      this.emptyView.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(10.0F), Theme.getServiceMessageColor()));
      this.emptyView.setPadding(AndroidUtilities.dp(16.0F), AndroidUtilities.dp(16.0F), AndroidUtilities.dp(16.0F), AndroidUtilities.dp(16.0F));
      this.emptyViewContainer.addView(this.emptyView, LayoutHelper.createFrame(-2, -2.0F, 17, 16.0F, 0.0F, 16.0F, 0.0F));
      this.chatListView = new RecyclerListView(var1) {
         public boolean drawChild(Canvas var1, View var2, long var3) {
            boolean var5 = super.drawChild(var1, var2, var3);
            if (var2 instanceof ChatMessageCell) {
               ChatMessageCell var6 = (ChatMessageCell)var2;
               ImageReceiver var7 = var6.getAvatarImage();
               if (var7 != null) {
                  int var8 = var2.getTop();
                  RecyclerView.ViewHolder var9;
                  if (var6.isPinnedBottom()) {
                     var9 = ChannelAdminLogActivity.this.chatListView.getChildViewHolder(var2);
                     if (var9 != null && ChannelAdminLogActivity.this.chatListView.findViewHolderForAdapterPosition(var9.getAdapterPosition() + 1) != null) {
                        var7.setImageY(-AndroidUtilities.dp(1000.0F));
                        var7.draw(var1);
                        return var5;
                     }
                  }

                  int var10 = var8;
                  if (var6.isPinnedTop()) {
                     var9 = ChannelAdminLogActivity.this.chatListView.getChildViewHolder(var2);
                     var10 = var8;
                     if (var9 != null) {
                        var10 = var8;

                        while(true) {
                           var9 = ChannelAdminLogActivity.this.chatListView.findViewHolderForAdapterPosition(var9.getAdapterPosition() - 1);
                           if (var9 == null) {
                              break;
                           }

                           var8 = var9.itemView.getTop();
                           View var11 = var9.itemView;
                           var10 = var8;
                           if (!(var11 instanceof ChatMessageCell)) {
                              break;
                           }

                           var10 = var8;
                           if (!((ChatMessageCell)var11).isPinnedTop()) {
                              var10 = var8;
                              break;
                           }
                        }
                     }
                  }

                  int var12 = var2.getTop() + var6.getLayoutHeight();
                  int var13 = ChannelAdminLogActivity.this.chatListView.getHeight() - ChannelAdminLogActivity.this.chatListView.getPaddingBottom();
                  var8 = var12;
                  if (var12 > var13) {
                     var8 = var13;
                  }

                  var12 = var8;
                  if (var8 - AndroidUtilities.dp(48.0F) < var10) {
                     var12 = AndroidUtilities.dp(48.0F) + var10;
                  }

                  var7.setImageY(var12 - AndroidUtilities.dp(44.0F));
                  var7.draw(var1);
               }
            }

            return var5;
         }
      };
      this.chatListView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$ChannelAdminLogActivity$Th_a2zOFfCYw_f0pzM6kJHBXd6E(this)));
      this.chatListView.setTag(1);
      this.chatListView.setVerticalScrollBarEnabled(true);
      RecyclerListView var6 = this.chatListView;
      ChannelAdminLogActivity.ChatActivityAdapter var5 = new ChannelAdminLogActivity.ChatActivityAdapter(var1);
      this.chatAdapter = var5;
      var6.setAdapter(var5);
      this.chatListView.setClipToPadding(false);
      this.chatListView.setPadding(0, AndroidUtilities.dp(4.0F), 0, AndroidUtilities.dp(3.0F));
      this.chatListView.setItemAnimator((RecyclerView.ItemAnimator)null);
      this.chatListView.setLayoutAnimation((LayoutAnimationController)null);
      this.chatLayoutManager = new LinearLayoutManager(var1) {
         public void smoothScrollToPosition(RecyclerView var1, RecyclerView.State var2, int var3) {
            LinearSmoothScrollerMiddle var4 = new LinearSmoothScrollerMiddle(var1.getContext());
            var4.setTargetPosition(var3);
            this.startSmoothScroll(var4);
         }

         public boolean supportsPredictiveItemAnimations() {
            return false;
         }
      };
      this.chatLayoutManager.setOrientation(1);
      this.chatLayoutManager.setStackFromEnd(true);
      this.chatListView.setLayoutManager(this.chatLayoutManager);
      this.contentView.addView(this.chatListView, LayoutHelper.createFrame(-1, -1.0F));
      this.chatListView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         private final int scrollValue = AndroidUtilities.dp(100.0F);
         private float totalDy = 0.0F;

         public void onScrollStateChanged(RecyclerView var1, int var2) {
            if (var2 == 1) {
               ChannelAdminLogActivity.this.scrollingFloatingDate = true;
               ChannelAdminLogActivity.this.checkTextureViewPosition = true;
            } else if (var2 == 0) {
               ChannelAdminLogActivity.this.scrollingFloatingDate = false;
               ChannelAdminLogActivity.this.checkTextureViewPosition = false;
               ChannelAdminLogActivity.this.hideFloatingDateView(true);
            }

         }

         public void onScrolled(RecyclerView var1, int var2, int var3) {
            ChannelAdminLogActivity.this.chatListView.invalidate();
            if (var3 != 0 && ChannelAdminLogActivity.this.scrollingFloatingDate && !ChannelAdminLogActivity.this.currentFloatingTopIsNotMessage && ChannelAdminLogActivity.this.floatingDateView.getTag() == null) {
               if (ChannelAdminLogActivity.this.floatingDateAnimation != null) {
                  ChannelAdminLogActivity.this.floatingDateAnimation.cancel();
               }

               ChannelAdminLogActivity.this.floatingDateView.setTag(1);
               ChannelAdminLogActivity.this.floatingDateAnimation = new AnimatorSet();
               ChannelAdminLogActivity.this.floatingDateAnimation.setDuration(150L);
               ChannelAdminLogActivity.this.floatingDateAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(ChannelAdminLogActivity.this.floatingDateView, "alpha", new float[]{1.0F})});
               ChannelAdminLogActivity.this.floatingDateAnimation.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     if (var1.equals(ChannelAdminLogActivity.this.floatingDateAnimation)) {
                        ChannelAdminLogActivity.this.floatingDateAnimation = null;
                     }

                  }
               });
               ChannelAdminLogActivity.this.floatingDateAnimation.start();
            }

            ChannelAdminLogActivity.this.checkScrollForLoad(true);
            ChannelAdminLogActivity.this.updateMessagesVisisblePart();
         }
      });
      var2 = this.scrollToPositionOnRecreate;
      if (var2 != -1) {
         this.chatLayoutManager.scrollToPositionWithOffset(var2, this.scrollToOffsetOnRecreate);
         this.scrollToPositionOnRecreate = -1;
      }

      this.progressView = new FrameLayout(var1);
      this.progressView.setVisibility(4);
      this.contentView.addView(this.progressView, LayoutHelper.createFrame(-1, -1, 51));
      this.progressView2 = new View(var1);
      this.progressView2.setBackgroundResource(2131165872);
      this.progressView2.getBackground().setColorFilter(Theme.colorFilter);
      this.progressView.addView(this.progressView2, LayoutHelper.createFrame(36, 36, 17));
      this.progressBar = new RadialProgressView(var1);
      this.progressBar.setSize(AndroidUtilities.dp(28.0F));
      this.progressBar.setProgressColor(Theme.getColor("chat_serviceText"));
      this.progressView.addView(this.progressBar, LayoutHelper.createFrame(32, 32, 17));
      this.floatingDateView = new ChatActionCell(var1);
      this.floatingDateView.setAlpha(0.0F);
      this.contentView.addView(this.floatingDateView, LayoutHelper.createFrame(-2, -2.0F, 49, 0.0F, 4.0F, 0.0F, 0.0F));
      this.contentView.addView(super.actionBar);
      this.bottomOverlayChat = new FrameLayout(var1) {
         public void onDraw(Canvas var1) {
            int var2 = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
            Theme.chat_composeShadowDrawable.setBounds(0, 0, this.getMeasuredWidth(), var2);
            Theme.chat_composeShadowDrawable.draw(var1);
            var1.drawRect(0.0F, (float)var2, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
         }
      };
      this.bottomOverlayChat.setWillNotDraw(false);
      this.bottomOverlayChat.setPadding(0, AndroidUtilities.dp(3.0F), 0, 0);
      this.contentView.addView(this.bottomOverlayChat, LayoutHelper.createFrame(-1, 51, 80));
      this.bottomOverlayChat.setOnClickListener(new _$$Lambda$ChannelAdminLogActivity$PzYgcbyw3IRYlthKzfCR6dwSeeo(this));
      this.bottomOverlayChatText = new TextView(var1);
      this.bottomOverlayChatText.setTextSize(1, 15.0F);
      this.bottomOverlayChatText.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.bottomOverlayChatText.setTextColor(Theme.getColor("chat_fieldOverlayText"));
      this.bottomOverlayChatText.setText(LocaleController.getString("SETTINGS", 2131560623).toUpperCase());
      this.bottomOverlayChat.addView(this.bottomOverlayChatText, LayoutHelper.createFrame(-2, -2, 17));
      this.bottomOverlayImage = new ImageView(var1);
      this.bottomOverlayImage.setImageResource(2131165548);
      this.bottomOverlayImage.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_fieldOverlayText"), Mode.MULTIPLY));
      this.bottomOverlayImage.setScaleType(ScaleType.CENTER);
      this.bottomOverlayChat.addView(this.bottomOverlayImage, LayoutHelper.createFrame(48, 48.0F, 53, 3.0F, 0.0F, 0.0F, 0.0F));
      this.bottomOverlayImage.setContentDescription(LocaleController.getString("BotHelp", 2131558850));
      this.bottomOverlayImage.setOnClickListener(new _$$Lambda$ChannelAdminLogActivity$Hkc6JExHTyVzGS38RTThrIB1hTM(this));
      this.searchContainer = new FrameLayout(var1) {
         public void onDraw(Canvas var1) {
            int var2 = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
            Theme.chat_composeShadowDrawable.setBounds(0, 0, this.getMeasuredWidth(), var2);
            Theme.chat_composeShadowDrawable.draw(var1);
            var1.drawRect(0.0F, (float)var2, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
         }
      };
      this.searchContainer.setWillNotDraw(false);
      this.searchContainer.setVisibility(4);
      this.searchContainer.setFocusable(true);
      this.searchContainer.setFocusableInTouchMode(true);
      this.searchContainer.setClickable(true);
      this.searchContainer.setPadding(0, AndroidUtilities.dp(3.0F), 0, 0);
      this.contentView.addView(this.searchContainer, LayoutHelper.createFrame(-1, 51, 80));
      this.searchCalendarButton = new ImageView(var1);
      this.searchCalendarButton.setScaleType(ScaleType.CENTER);
      this.searchCalendarButton.setImageResource(2131165615);
      this.searchCalendarButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_searchPanelIcons"), Mode.MULTIPLY));
      this.searchContainer.addView(this.searchCalendarButton, LayoutHelper.createFrame(48, 48, 53));
      this.searchCalendarButton.setOnClickListener(new _$$Lambda$ChannelAdminLogActivity$qxESsL3_0yZHIVgE4y2ybeWozac(this));
      this.searchCountText = new SimpleTextView(var1);
      this.searchCountText.setTextColor(Theme.getColor("chat_searchPanelText"));
      this.searchCountText.setTextSize(15);
      this.searchCountText.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.searchContainer.addView(this.searchCountText, LayoutHelper.createFrame(-1, -2.0F, 19, 108.0F, 0.0F, 0.0F, 0.0F));
      this.chatAdapter.updateRows();
      if (this.loading && this.messages.isEmpty()) {
         this.progressView.setVisibility(0);
         this.chatListView.setEmptyView((View)null);
      } else {
         this.progressView.setVisibility(4);
         this.chatListView.setEmptyView(this.emptyViewContainer);
      }

      this.updateEmptyPlaceholder();
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      RecyclerListView var8;
      if (var1 == NotificationCenter.emojiDidLoad) {
         var8 = this.chatListView;
         if (var8 != null) {
            var8.invalidateViews();
         }
      } else {
         int var4 = NotificationCenter.messagePlayingDidStart;
         byte var7 = 0;
         MessageObject var5;
         View var9;
         ChatMessageCell var10;
         if (var1 == var4) {
            if (((MessageObject)var3[0]).isRoundVideo()) {
               MediaController.getInstance().setTextureView(this.createTextureView(true), this.aspectRatioFrameLayout, this.roundVideoContainer, true);
               this.updateTextureViewPosition();
            }

            var8 = this.chatListView;
            if (var8 != null) {
               var2 = var8.getChildCount();

               for(var1 = 0; var1 < var2; ++var1) {
                  var9 = this.chatListView.getChildAt(var1);
                  if (var9 instanceof ChatMessageCell) {
                     var10 = (ChatMessageCell)var9;
                     var5 = var10.getMessageObject();
                     if (var5 != null) {
                        if (!var5.isVoice() && !var5.isMusic()) {
                           if (var5.isRoundVideo()) {
                              var10.checkVideoPlayback(false);
                              if (!MediaController.getInstance().isPlayingMessage(var5) && var5.audioProgress != 0.0F) {
                                 var5.resetPlayingProgress();
                                 var10.invalidate();
                              }
                           }
                        } else {
                           var10.updateButtonState(false, true, false);
                        }
                     }
                  }
               }
            }
         } else if (var1 != NotificationCenter.messagePlayingDidReset && var1 != NotificationCenter.messagePlayingPlayStateChanged) {
            if (var1 == NotificationCenter.messagePlayingProgressDidChanged) {
               Integer var11 = (Integer)var3[0];
               RecyclerListView var12 = this.chatListView;
               if (var12 != null) {
                  var4 = var12.getChildCount();

                  for(var1 = var7; var1 < var4; ++var1) {
                     View var13 = this.chatListView.getChildAt(var1);
                     if (var13 instanceof ChatMessageCell) {
                        ChatMessageCell var16 = (ChatMessageCell)var13;
                        MessageObject var6 = var16.getMessageObject();
                        if (var6 != null && var6.getId() == var11) {
                           MessageObject var14 = MediaController.getInstance().getPlayingMessageObject();
                           if (var14 != null) {
                              var6.audioProgress = var14.audioProgress;
                              var6.audioProgressSec = var14.audioProgressSec;
                              var6.audioPlayerDuration = var14.audioPlayerDuration;
                              var16.updatePlayingMessageProgress();
                           }
                           break;
                        }
                     }
                  }
               }
            } else if (var1 == NotificationCenter.didSetNewWallpapper && super.fragmentView != null) {
               this.contentView.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());
               this.progressView2.getBackground().setColorFilter(Theme.colorFilter);
               TextView var15 = this.emptyView;
               if (var15 != null) {
                  var15.getBackground().setColorFilter(Theme.colorFilter);
               }

               this.chatListView.invalidateViews();
            }
         } else {
            var8 = this.chatListView;
            if (var8 != null) {
               var2 = var8.getChildCount();

               for(var1 = 0; var1 < var2; ++var1) {
                  var9 = this.chatListView.getChildAt(var1);
                  if (var9 instanceof ChatMessageCell) {
                     var10 = (ChatMessageCell)var9;
                     var5 = var10.getMessageObject();
                     if (var5 != null) {
                        if (!var5.isVoice() && !var5.isMusic()) {
                           if (var5.isRoundVideo() && !MediaController.getInstance().isPlayingMessage(var5)) {
                              var10.checkVideoPlayback(true);
                           }
                        } else {
                           var10.updateButtonState(false, true, false);
                        }
                     }
                  }
               }
            }
         }
      }

   }

   public TLRPC.Chat getCurrentChat() {
      return this.currentChat;
   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(super.fragmentView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_wallpaper");
      ThemeDescription var2 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var3 = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuBackground");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuItem");
      ThemeDescription var8 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuItemIcon");
      ThemeDescription var9 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var10 = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var11 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var12 = new ThemeDescription(this.avatarContainer.getTitleTextView(), ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var13 = new ThemeDescription(this.avatarContainer.getSubtitleTextView(), ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, new Paint[]{Theme.chat_statusPaint, Theme.chat_statusRecordPaint}, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubtitle", (Object)null);
      ThemeDescription var14 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      RecyclerListView var15 = this.chatListView;
      Drawable var16 = Theme.avatar_broadcastDrawable;
      Drawable var17 = Theme.avatar_savedDrawable;
      ThemeDescription var18 = new ThemeDescription(var15, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16, var17}, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_text");
      ThemeDescription var19 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_backgroundRed");
      ThemeDescription var20 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_backgroundOrange");
      ThemeDescription var21 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_backgroundViolet");
      ThemeDescription var22 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_backgroundGreen");
      ThemeDescription var23 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_backgroundCyan");
      ThemeDescription var24 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_backgroundBlue");
      ThemeDescription var25 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_backgroundPink");
      ThemeDescription var26 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_nameInMessageRed");
      ThemeDescription var27 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_nameInMessageOrange");
      ThemeDescription var28 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_nameInMessageViolet");
      ThemeDescription var29 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_nameInMessageGreen");
      ThemeDescription var30 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_nameInMessageCyan");
      ThemeDescription var31 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_nameInMessageBlue");
      ThemeDescription var211 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_nameInMessagePink");
      RecyclerListView var213 = this.chatListView;
      var16 = Theme.chat_msgInDrawable;
      Drawable var32 = Theme.chat_msgInMediaDrawable;
      ThemeDescription var218 = new ThemeDescription(var213, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16, var32}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inBubble");
      var213 = this.chatListView;
      var16 = Theme.chat_msgInSelectedDrawable;
      Drawable var33 = Theme.chat_msgInMediaSelectedDrawable;
      ThemeDescription var219 = new ThemeDescription(var213, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16, var33}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inBubbleSelected");
      RecyclerListView var212 = this.chatListView;
      var17 = Theme.chat_msgInShadowDrawable;
      Drawable var34 = Theme.chat_msgInMediaShadowDrawable;
      ThemeDescription var220 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17, var34}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inBubbleShadow");
      RecyclerListView var35 = this.chatListView;
      var16 = Theme.chat_msgOutDrawable;
      var17 = Theme.chat_msgOutMediaDrawable;
      ThemeDescription var221 = new ThemeDescription(var35, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16, var17}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outBubble");
      var213 = this.chatListView;
      Drawable var36 = Theme.chat_msgOutSelectedDrawable;
      var16 = Theme.chat_msgOutMediaSelectedDrawable;
      ThemeDescription var222 = new ThemeDescription(var213, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var36, var16}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outBubbleSelected");
      RecyclerListView var37 = this.chatListView;
      var16 = Theme.chat_msgOutShadowDrawable;
      var17 = Theme.chat_msgOutMediaShadowDrawable;
      ThemeDescription var223 = new ThemeDescription(var37, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16, var17}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outBubbleShadow");
      var212 = this.chatListView;
      int var38 = ThemeDescription.FLAG_TEXTCOLOR;
      TextPaint var215 = Theme.chat_actionTextPaint;
      ThemeDescription var39 = new ThemeDescription(var212, var38, new Class[]{ChatActionCell.class}, var215, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_serviceText");
      var213 = this.chatListView;
      var38 = ThemeDescription.FLAG_LINKCOLOR;
      TextPaint var214 = Theme.chat_actionTextPaint;
      ThemeDescription var40 = new ThemeDescription(var213, var38, new Class[]{ChatActionCell.class}, var214, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_serviceLink");
      RecyclerListView var41 = this.chatListView;
      var16 = Theme.chat_shareIconDrawable;
      Drawable var42 = Theme.chat_botInlineDrawable;
      var17 = Theme.chat_botLinkDrawalbe;
      Drawable var43 = Theme.chat_goIconDrawable;
      ThemeDescription var227 = new ThemeDescription(var41, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16, var42, var17, var43}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_serviceIcon");
      ThemeDescription var226 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class, ChatActionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_serviceBackground");
      ThemeDescription var44 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class, ChatActionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_serviceBackgroundSelected");
      ThemeDescription var228 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_messageTextIn");
      ThemeDescription var45 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_messageTextOut");
      ThemeDescription var46 = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_LINKCOLOR, new Class[]{ChatMessageCell.class}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_messageLinkIn", (Object)null);
      ThemeDescription var47 = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_LINKCOLOR, new Class[]{ChatMessageCell.class}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_messageLinkOut", (Object)null);
      var212 = this.chatListView;
      var17 = Theme.chat_msgOutCheckDrawable;
      Drawable var48 = Theme.chat_msgOutHalfCheckDrawable;
      ThemeDescription var229 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17, var48}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outSentCheck");
      RecyclerListView var49 = this.chatListView;
      var16 = Theme.chat_msgOutCheckSelectedDrawable;
      var17 = Theme.chat_msgOutHalfCheckSelectedDrawable;
      ThemeDescription var230 = new ThemeDescription(var49, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16, var17}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outSentCheckSelected");
      var213 = this.chatListView;
      var16 = Theme.chat_msgOutClockDrawable;
      ThemeDescription var50 = new ThemeDescription(var213, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outSentClock");
      var212 = this.chatListView;
      var17 = Theme.chat_msgOutSelectedClockDrawable;
      ThemeDescription var51 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outSentClockSelected");
      var213 = this.chatListView;
      var16 = Theme.chat_msgInClockDrawable;
      ThemeDescription var52 = new ThemeDescription(var213, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inSentClock");
      var213 = this.chatListView;
      var16 = Theme.chat_msgInSelectedClockDrawable;
      ThemeDescription var53 = new ThemeDescription(var213, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inSentClockSelected");
      var213 = this.chatListView;
      Drawable var54 = Theme.chat_msgMediaCheckDrawable;
      var16 = Theme.chat_msgMediaHalfCheckDrawable;
      ThemeDescription var231 = new ThemeDescription(var213, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var54, var16}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_mediaSentCheck");
      var213 = this.chatListView;
      Drawable var55 = Theme.chat_msgStickerHalfCheckDrawable;
      Drawable var56 = Theme.chat_msgStickerCheckDrawable;
      Drawable var57 = Theme.chat_msgStickerClockDrawable;
      var16 = Theme.chat_msgStickerViewsDrawable;
      ThemeDescription var233 = new ThemeDescription(var213, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var55, var56, var57, var16}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_serviceText");
      var213 = this.chatListView;
      var16 = Theme.chat_msgMediaClockDrawable;
      ThemeDescription var232 = new ThemeDescription(var213, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_mediaSentClock");
      var212 = this.chatListView;
      var17 = Theme.chat_msgOutViewsDrawable;
      ThemeDescription var234 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outViews");
      var213 = this.chatListView;
      var16 = Theme.chat_msgOutViewsSelectedDrawable;
      ThemeDescription var58 = new ThemeDescription(var213, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outViewsSelected");
      var212 = this.chatListView;
      var17 = Theme.chat_msgInViewsDrawable;
      ThemeDescription var59 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inViews");
      var212 = this.chatListView;
      var17 = Theme.chat_msgInViewsSelectedDrawable;
      ThemeDescription var60 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inViewsSelected");
      var212 = this.chatListView;
      var17 = Theme.chat_msgMediaViewsDrawable;
      ThemeDescription var61 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_mediaViews");
      var213 = this.chatListView;
      var16 = Theme.chat_msgOutMenuDrawable;
      ThemeDescription var62 = new ThemeDescription(var213, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outMenu");
      var212 = this.chatListView;
      var17 = Theme.chat_msgOutMenuSelectedDrawable;
      ThemeDescription var63 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outMenuSelected");
      var212 = this.chatListView;
      var17 = Theme.chat_msgInMenuDrawable;
      ThemeDescription var64 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inMenu");
      var212 = this.chatListView;
      var17 = Theme.chat_msgInMenuSelectedDrawable;
      ThemeDescription var65 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inMenuSelected");
      var212 = this.chatListView;
      var17 = Theme.chat_msgMediaMenuDrawable;
      ThemeDescription var66 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_mediaMenu");
      var212 = this.chatListView;
      var17 = Theme.chat_msgOutInstantDrawable;
      Drawable var67 = Theme.chat_msgOutCallDrawable;
      ThemeDescription var235 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17, var67}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outInstant");
      var213 = this.chatListView;
      var16 = Theme.chat_msgOutCallSelectedDrawable;
      ThemeDescription var68 = new ThemeDescription(var213, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outInstantSelected");
      var212 = this.chatListView;
      var17 = Theme.chat_msgInInstantDrawable;
      Drawable var69 = Theme.chat_msgInCallDrawable;
      ThemeDescription var236 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17, var69}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inInstant");
      var213 = this.chatListView;
      var16 = Theme.chat_msgInCallSelectedDrawable;
      ThemeDescription var70 = new ThemeDescription(var213, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inInstantSelected");
      var212 = this.chatListView;
      var17 = Theme.chat_msgCallUpGreenDrawable;
      ThemeDescription var71 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outUpCall");
      var213 = this.chatListView;
      var16 = Theme.chat_msgCallDownRedDrawable;
      ThemeDescription var72 = new ThemeDescription(var213, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inUpCall");
      var212 = this.chatListView;
      var17 = Theme.chat_msgCallDownGreenDrawable;
      ThemeDescription var73 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inDownCall");
      var213 = this.chatListView;
      Paint var216 = Theme.chat_msgErrorPaint;
      ThemeDescription var74 = new ThemeDescription(var213, 0, new Class[]{ChatMessageCell.class}, var216, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_sentError");
      var212 = this.chatListView;
      var17 = Theme.chat_msgErrorDrawable;
      ThemeDescription var75 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_sentErrorIcon");
      var212 = this.chatListView;
      var215 = Theme.chat_durationPaint;
      ThemeDescription var76 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, var215, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_previewDurationText");
      var213 = this.chatListView;
      var214 = Theme.chat_gamePaint;
      ThemeDescription var77 = new ThemeDescription(var213, 0, new Class[]{ChatMessageCell.class}, var214, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_previewGameText");
      ThemeDescription var78 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inPreviewInstantText");
      ThemeDescription var79 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outPreviewInstantText");
      ThemeDescription var80 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inPreviewInstantSelectedText");
      ThemeDescription var81 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outPreviewInstantSelectedText");
      var212 = this.chatListView;
      Paint var217 = Theme.chat_deleteProgressPaint;
      ThemeDescription var82 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, var217, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_secretTimeText");
      ThemeDescription var83 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_stickerNameText");
      var213 = this.chatListView;
      var214 = Theme.chat_botButtonPaint;
      ThemeDescription var84 = new ThemeDescription(var213, 0, new Class[]{ChatMessageCell.class}, var214, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_botButtonText");
      var212 = this.chatListView;
      var217 = Theme.chat_botProgressPaint;
      ThemeDescription var85 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, var217, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_botProgress");
      ThemeDescription var86 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inForwardedNameText");
      ThemeDescription var87 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outForwardedNameText");
      ThemeDescription var88 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inViaBotNameText");
      ThemeDescription var89 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outViaBotNameText");
      ThemeDescription var90 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_stickerViaBotNameText");
      ThemeDescription var91 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inReplyLine");
      ThemeDescription var92 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outReplyLine");
      ThemeDescription var93 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_stickerReplyLine");
      ThemeDescription var94 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inReplyNameText");
      ThemeDescription var95 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outReplyNameText");
      ThemeDescription var96 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_stickerReplyNameText");
      ThemeDescription var97 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inReplyMessageText");
      ThemeDescription var98 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outReplyMessageText");
      ThemeDescription var99 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inReplyMediaMessageText");
      ThemeDescription var100 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outReplyMediaMessageText");
      ThemeDescription var101 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inReplyMediaMessageSelectedText");
      ThemeDescription var102 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outReplyMediaMessageSelectedText");
      ThemeDescription var103 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_stickerReplyMessageText");
      ThemeDescription var104 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inPreviewLine");
      ThemeDescription var105 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outPreviewLine");
      ThemeDescription var106 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inSiteNameText");
      ThemeDescription var107 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outSiteNameText");
      ThemeDescription var108 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inContactNameText");
      ThemeDescription var109 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outContactNameText");
      ThemeDescription var110 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inContactPhoneText");
      ThemeDescription var111 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outContactPhoneText");
      ThemeDescription var112 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_mediaProgress");
      ThemeDescription var113 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inAudioProgress");
      ThemeDescription var114 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outAudioProgress");
      ThemeDescription var115 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inAudioSelectedProgress");
      ThemeDescription var116 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outAudioSelectedProgress");
      ThemeDescription var117 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_mediaTimeText");
      ThemeDescription var118 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inTimeText");
      ThemeDescription var119 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outTimeText");
      ThemeDescription var120 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inTimeSelectedText");
      ThemeDescription var121 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outTimeSelectedText");
      ThemeDescription var122 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inAudioPerfomerText");
      ThemeDescription var123 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outAudioPerfomerText");
      ThemeDescription var124 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inAudioTitleText");
      ThemeDescription var125 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outAudioTitleText");
      ThemeDescription var126 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inAudioDurationText");
      ThemeDescription var127 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outAudioDurationText");
      ThemeDescription var128 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inAudioDurationSelectedText");
      ThemeDescription var129 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outAudioDurationSelectedText");
      ThemeDescription var130 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inAudioSeekbar");
      ThemeDescription var131 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outAudioSeekbar");
      ThemeDescription var132 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inAudioSeekbarSelected");
      ThemeDescription var133 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outAudioSeekbarSelected");
      ThemeDescription var134 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inAudioSeekbarFill");
      ThemeDescription var135 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inAudioCacheSeekbar");
      ThemeDescription var136 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outAudioSeekbarFill");
      ThemeDescription var137 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outAudioCacheSeekbar");
      ThemeDescription var138 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inVoiceSeekbar");
      ThemeDescription var139 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outVoiceSeekbar");
      ThemeDescription var140 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inVoiceSeekbarSelected");
      ThemeDescription var141 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outVoiceSeekbarSelected");
      ThemeDescription var142 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inVoiceSeekbarFill");
      ThemeDescription var143 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outVoiceSeekbarFill");
      ThemeDescription var144 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inFileProgress");
      ThemeDescription var145 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outFileProgress");
      ThemeDescription var146 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inFileProgressSelected");
      ThemeDescription var147 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outFileProgressSelected");
      ThemeDescription var148 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inFileNameText");
      ThemeDescription var149 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outFileNameText");
      ThemeDescription var150 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inFileInfoText");
      ThemeDescription var151 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outFileInfoText");
      ThemeDescription var152 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inFileInfoSelectedText");
      ThemeDescription var153 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outFileInfoSelectedText");
      ThemeDescription var154 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inFileBackground");
      ThemeDescription var155 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outFileBackground");
      ThemeDescription var156 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inFileBackgroundSelected");
      ThemeDescription var157 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outFileBackgroundSelected");
      ThemeDescription var158 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inVenueInfoText");
      ThemeDescription var159 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outVenueInfoText");
      ThemeDescription var160 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inVenueInfoSelectedText");
      ThemeDescription var161 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outVenueInfoSelectedText");
      ThemeDescription var162 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_mediaInfoText");
      var212 = this.chatListView;
      var217 = Theme.chat_urlPaint;
      ThemeDescription var163 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, var217, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_linkSelectBackground");
      var212 = this.chatListView;
      var217 = Theme.chat_textSearchSelectionPaint;
      ThemeDescription var164 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, var217, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_textSelectBackground");
      ThemeDescription var165 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outLoader");
      ThemeDescription var166 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outMediaIcon");
      ThemeDescription var167 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outLoaderSelected");
      ThemeDescription var168 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outMediaIconSelected");
      ThemeDescription var169 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inLoader");
      ThemeDescription var170 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inMediaIcon");
      ThemeDescription var171 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inLoaderSelected");
      ThemeDescription var172 = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inMediaIconSelected");
      var212 = this.chatListView;
      var38 = ThemeDescription.FLAG_BACKGROUNDFILTER;
      Drawable[][] var173 = Theme.chat_photoStatesDrawables;
      Drawable var174 = var173[0][0];
      var17 = var173[1][0];
      Drawable var175 = var173[2][0];
      Drawable var237 = var173[3][0];
      ThemeDescription var241 = new ThemeDescription(var212, var38, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var174, var17, var175, var237}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_mediaLoaderPhoto");
      RecyclerListView var238 = this.chatListView;
      Drawable[][] var176 = Theme.chat_photoStatesDrawables;
      var17 = var176[0][0];
      var16 = var176[1][0];
      var174 = var176[2][0];
      Drawable var242 = var176[3][0];
      ThemeDescription var240 = new ThemeDescription(var238, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17, var16, var174, var242}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_mediaLoaderPhotoIcon");
      var238 = this.chatListView;
      var38 = ThemeDescription.FLAG_BACKGROUNDFILTER;
      Drawable[][] var177 = Theme.chat_photoStatesDrawables;
      var17 = var177[0][1];
      var16 = var177[1][1];
      var242 = var177[2][1];
      Drawable var243 = var177[3][1];
      ThemeDescription var239 = new ThemeDescription(var238, var38, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17, var16, var242, var243}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_mediaLoaderPhotoSelected");
      RecyclerListView var245 = this.chatListView;
      Drawable[][] var178 = Theme.chat_photoStatesDrawables;
      var16 = var178[0][1];
      var242 = var178[1][1];
      var17 = var178[2][1];
      Drawable var246 = var178[3][1];
      ThemeDescription var244 = new ThemeDescription(var245, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16, var242, var17, var246}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_mediaLoaderPhotoIconSelected");
      var213 = this.chatListView;
      var38 = ThemeDescription.FLAG_BACKGROUNDFILTER;
      var177 = Theme.chat_photoStatesDrawables;
      var16 = var177[7][0];
      var243 = var177[8][0];
      ThemeDescription var247 = new ThemeDescription(var213, var38, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16, var243}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outLoaderPhoto");
      var212 = this.chatListView;
      var178 = Theme.chat_photoStatesDrawables;
      var17 = var178[7][0];
      var246 = var178[8][0];
      ThemeDescription var248 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17, var246}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outLoaderPhotoIcon");
      var213 = this.chatListView;
      var38 = ThemeDescription.FLAG_BACKGROUNDFILTER;
      Drawable[][] var179 = Theme.chat_photoStatesDrawables;
      var16 = var179[7][1];
      Drawable var249 = var179[8][1];
      ThemeDescription var250 = new ThemeDescription(var213, var38, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16, var249}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outLoaderPhotoSelected");
      var213 = this.chatListView;
      Drawable[][] var180 = Theme.chat_photoStatesDrawables;
      var16 = var180[7][1];
      Drawable var251 = var180[8][1];
      ThemeDescription var252 = new ThemeDescription(var213, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16, var251}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outLoaderPhotoIconSelected");
      var212 = this.chatListView;
      var38 = ThemeDescription.FLAG_BACKGROUNDFILTER;
      Drawable[][] var181 = Theme.chat_photoStatesDrawables;
      var17 = var181[10][0];
      Drawable var253 = var181[11][0];
      ThemeDescription var254 = new ThemeDescription(var212, var38, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17, var253}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inLoaderPhoto");
      var212 = this.chatListView;
      Drawable[][] var182 = Theme.chat_photoStatesDrawables;
      var17 = var182[10][0];
      Drawable var255 = var182[11][0];
      ThemeDescription var256 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17, var255}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inLoaderPhotoIcon");
      var213 = this.chatListView;
      var38 = ThemeDescription.FLAG_BACKGROUNDFILTER;
      Drawable[][] var183 = Theme.chat_photoStatesDrawables;
      var16 = var183[10][1];
      Drawable var257 = var183[11][1];
      ThemeDescription var258 = new ThemeDescription(var213, var38, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16, var257}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inLoaderPhotoSelected");
      var213 = this.chatListView;
      Drawable[][] var184 = Theme.chat_photoStatesDrawables;
      var16 = var184[10][1];
      Drawable var259 = var184[11][1];
      ThemeDescription var260 = new ThemeDescription(var213, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16, var259}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inLoaderPhotoIconSelected");
      var213 = this.chatListView;
      var16 = Theme.chat_photoStatesDrawables[9][0];
      ThemeDescription var185 = new ThemeDescription(var213, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outFileIcon");
      var212 = this.chatListView;
      var17 = Theme.chat_photoStatesDrawables[9][1];
      ThemeDescription var186 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var17}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outFileSelectedIcon");
      RecyclerListView var187 = this.chatListView;
      var16 = Theme.chat_photoStatesDrawables[12][0];
      var17 = null;
      ThemeDescription var261 = new ThemeDescription(var187, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inFileIcon");
      RecyclerListView var188 = this.chatListView;
      var16 = Theme.chat_photoStatesDrawables[12][1];
      ThemeDescription var262 = new ThemeDescription(var188, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inFileSelectedIcon");
      RecyclerListView var189 = this.chatListView;
      var38 = ThemeDescription.FLAG_BACKGROUNDFILTER;
      var16 = Theme.chat_contactDrawable[0];
      ThemeDescription var263 = new ThemeDescription(var189, var38, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inContactBackground");
      RecyclerListView var190 = this.chatListView;
      var16 = Theme.chat_contactDrawable[0];
      ThemeDescription var264 = new ThemeDescription(var190, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inContactIcon");
      var212 = this.chatListView;
      var38 = ThemeDescription.FLAG_BACKGROUNDFILTER;
      Drawable var191 = Theme.chat_contactDrawable[1];
      ThemeDescription var265 = new ThemeDescription(var212, var38, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var191}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outContactBackground");
      RecyclerListView var192 = this.chatListView;
      var16 = Theme.chat_contactDrawable[1];
      ThemeDescription var266 = new ThemeDescription(var192, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outContactIcon");
      var212 = this.chatListView;
      var38 = ThemeDescription.FLAG_BACKGROUNDFILTER;
      Drawable var193 = Theme.chat_locationDrawable[0];
      ThemeDescription var267 = new ThemeDescription(var212, var38, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var193}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inLocationBackground");
      var212 = this.chatListView;
      Drawable var194 = Theme.chat_locationDrawable[0];
      ThemeDescription var268 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var194}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inLocationIcon");
      RecyclerListView var195 = this.chatListView;
      var38 = ThemeDescription.FLAG_BACKGROUNDFILTER;
      var16 = Theme.chat_locationDrawable[1];
      ThemeDescription var269 = new ThemeDescription(var195, var38, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var16}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outLocationBackground");
      var212 = this.chatListView;
      Drawable var196 = Theme.chat_locationDrawable[1];
      ThemeDescription var197 = new ThemeDescription(var212, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var196}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outLocationIcon");
      ThemeDescription var198 = new ThemeDescription(this.bottomOverlayChat, 0, (Class[])null, Theme.chat_composeBackgroundPaint, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_messagePanelBackground");
      ThemeDescription var199 = new ThemeDescription(this.bottomOverlayChat, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_composeShadowDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_messagePanelShadow");
      ThemeDescription var200 = new ThemeDescription(this.bottomOverlayChatText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_fieldOverlayText");
      ThemeDescription var201 = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_serviceText");
      ThemeDescription var270 = new ThemeDescription(this.progressBar, ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_serviceText");
      ThemeDescription var202 = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[]{ChatUnreadCell.class}, new String[]{"backgroundLayout"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_unreadMessagesStartBackground");
      ThemeDescription var203 = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{ChatUnreadCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_unreadMessagesStartArrowIcon");
      ThemeDescription var204 = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{ChatUnreadCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_unreadMessagesStartText");
      ThemeDescription var205 = new ThemeDescription(this.progressView2, ThemeDescription.FLAG_SERVICEBACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_serviceBackground");
      ThemeDescription var206 = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_SERVICEBACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_serviceBackground");
      ThemeDescription var207 = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_SERVICEBACKGROUND, new Class[]{ChatLoadingCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_serviceBackground");
      ThemeDescription var208 = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_PROGRESSBAR, new Class[]{ChatLoadingCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_serviceText");
      ChatAvatarContainer var224 = this.avatarContainer;
      ImageView var225;
      if (var224 != null) {
         var225 = var224.getTimeItem();
      } else {
         var225 = null;
      }

      ThemeDescription var209 = new ThemeDescription(var225, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_secretTimerBackground");
      ChatAvatarContainer var210 = this.avatarContainer;
      var225 = var17;
      if (var210 != null) {
         var225 = var210.getTimeItem();
      }

      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var18, var19, var20, var21, var22, var23, var24, var25, var26, var27, var28, var29, var30, var31, var211, var218, var219, var220, var221, var222, var223, var39, var40, var227, var226, var44, var228, var45, var46, var47, var229, var230, var50, var51, var52, var53, var231, var233, var232, var234, var58, var59, var60, var61, var62, var63, var64, var65, var66, var235, var68, var236, var70, var71, var72, var73, var74, var75, var76, var77, var78, var79, var80, var81, var82, var83, var84, var85, var86, var87, var88, var89, var90, var91, var92, var93, var94, var95, var96, var97, var98, var99, var100, var101, var102, var103, var104, var105, var106, var107, var108, var109, var110, var111, var112, var113, var114, var115, var116, var117, var118, var119, var120, var121, var122, var123, var124, var125, var126, var127, var128, var129, var130, var131, var132, var133, var134, var135, var136, var137, var138, var139, var140, var141, var142, var143, var144, var145, var146, var147, var148, var149, var150, var151, var152, var153, var154, var155, var156, var157, var158, var159, var160, var161, var162, var163, var164, var165, var166, var167, var168, var169, var170, var171, var172, var241, var240, var239, var244, var247, var248, var250, var252, var254, var256, var258, var260, var185, var186, var261, var262, var263, var264, var265, var266, var267, var268, var269, var197, var198, var199, var200, var201, var270, var202, var203, var204, var205, var206, var207, var208, var209, new ThemeDescription(var225, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_secretTimerText")};
   }

   // $FF: synthetic method
   public void lambda$createMenu$11$ChannelAdminLogActivity(ArrayList var1, DialogInterface var2, int var3) {
      if (this.selectedObject != null && var3 >= 0 && var3 < var1.size()) {
         this.processSelectedOption((Integer)var1.get(var3));
      }

   }

   // $FF: synthetic method
   public void lambda$createView$10$ChannelAdminLogActivity(View var1) {
      if (this.getParentActivity() != null) {
         AndroidUtilities.hideKeyboard(this.searchItem.getSearchField());
         Calendar var8 = Calendar.getInstance();
         int var2 = var8.get(1);
         int var3 = var8.get(2);
         int var4 = var8.get(5);

         try {
            Activity var5 = this.getParentActivity();
            _$$Lambda$ChannelAdminLogActivity$0sxG9Ul5ZEHgnzFmOGlWQbHqyvo var6 = new _$$Lambda$ChannelAdminLogActivity$0sxG9Ul5ZEHgnzFmOGlWQbHqyvo(this);
            DatePickerDialog var9 = new DatePickerDialog(var5, var6, var2, var3, var4);
            DatePicker var10 = var9.getDatePicker();
            var10.setMinDate(1375315200000L);
            var10.setMaxDate(System.currentTimeMillis());
            var9.setButton(-1, LocaleController.getString("JumpToDate", 2131559707), var9);
            var9.setButton(-2, LocaleController.getString("Cancel", 2131558891), _$$Lambda$ChannelAdminLogActivity$v157pu_gWSgDQsCgD_IoOuRIDsE.INSTANCE);
            if (VERSION.SDK_INT >= 21) {
               _$$Lambda$ChannelAdminLogActivity$TCvk3Rm_FmQeaZ3KZrjzNCt3Urw var11 = new _$$Lambda$ChannelAdminLogActivity$TCvk3Rm_FmQeaZ3KZrjzNCt3Urw(var10);
               var9.setOnShowListener(var11);
            }

            this.showDialog(var9);
         } catch (Exception var7) {
            FileLog.e((Throwable)var7);
         }

      }
   }

   // $FF: synthetic method
   public void lambda$createView$3$ChannelAdminLogActivity(View var1, int var2) {
      this.createMenu(var1);
   }

   // $FF: synthetic method
   public void lambda$createView$5$ChannelAdminLogActivity(View var1) {
      if (this.getParentActivity() != null) {
         AdminLogFilterAlert var2 = new AdminLogFilterAlert(this.getParentActivity(), this.currentFilter, this.selectedAdmins, this.currentChat.megagroup);
         var2.setCurrentAdmins(this.admins);
         var2.setAdminLogFilterAlertDelegate(new _$$Lambda$ChannelAdminLogActivity$np_QiCzzUhW5eVeXU_cFYpLVjKA(this));
         this.showDialog(var2);
      }
   }

   // $FF: synthetic method
   public void lambda$createView$6$ChannelAdminLogActivity(View var1) {
      AlertDialog.Builder var2 = new AlertDialog.Builder(this.getParentActivity());
      if (this.currentChat.megagroup) {
         var2.setMessage(AndroidUtilities.replaceTags(LocaleController.getString("EventLogInfoDetail", 2131559421)));
      } else {
         var2.setMessage(AndroidUtilities.replaceTags(LocaleController.getString("EventLogInfoDetailChannel", 2131559422)));
      }

      var2.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
      var2.setTitle(LocaleController.getString("EventLogInfoTitle", 2131559423));
      this.showDialog(var2.create());
   }

   // $FF: synthetic method
   public void lambda$loadAdmins$13$ChannelAdminLogActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ChannelAdminLogActivity$nOrNBbemB40UwQEfR8VXtwD17_w(this, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$loadMessages$1$ChannelAdminLogActivity(TLObject var1, TLRPC.TL_error var2) {
      if (var1 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ChannelAdminLogActivity$K07rFS3bEoWzk9x_xNaPo7WXWaQ(this, (TLRPC.TL_channels_adminLogResults)var1));
      }

   }

   // $FF: synthetic method
   public void lambda$null$0$ChannelAdminLogActivity(TLRPC.TL_channels_adminLogResults var1) {
      MessagesController var2 = MessagesController.getInstance(super.currentAccount);
      ArrayList var3 = var1.users;
      byte var4 = 0;
      var2.putUsers(var3, false);
      MessagesController.getInstance(super.currentAccount).putChats(var1.chats, false);
      int var5 = this.messages.size();
      int var6 = 0;

      boolean var7;
      for(var7 = false; var6 < var1.events.size(); ++var6) {
         TLRPC.TL_channelAdminLogEvent var12 = (TLRPC.TL_channelAdminLogEvent)var1.events.get(var6);
         if (this.messagesDict.indexOfKey(var12.id) < 0) {
            this.minEventId = Math.min(this.minEventId, var12.id);
            MessageObject var11 = new MessageObject(super.currentAccount, var12, this.messages, this.messagesByDays, this.currentChat, this.mid);
            if (var11.contentType >= 0) {
               this.messagesDict.put(var12.id, var11);
            }

            var7 = true;
         }
      }

      var5 = this.messages.size() - var5;
      this.loading = false;
      if (!var7) {
         this.endReached = true;
      }

      this.progressView.setVisibility(4);
      this.chatListView.setEmptyView(this.emptyViewContainer);
      if (var5 != 0) {
         byte var14;
         if (this.endReached) {
            this.chatAdapter.notifyItemRangeChanged(0, 2);
            var14 = 1;
         } else {
            var14 = 0;
         }

         int var8 = this.chatLayoutManager.findLastVisibleItemPosition();
         View var10 = this.chatLayoutManager.findViewByPosition(var8);
         int var15;
         if (var10 == null) {
            var15 = var4;
         } else {
            var15 = var10.getTop();
         }

         int var9 = this.chatListView.getPaddingTop();
         if (var5 - var14 > 0) {
            int var13 = (var14 ^ 1) + 1;
            this.chatAdapter.notifyItemChanged(var13);
            this.chatAdapter.notifyItemRangeInserted(var13, var5 - var14);
         }

         if (var8 != -1) {
            this.chatLayoutManager.scrollToPositionWithOffset(var8 + var5 - var14, var15 - var9);
         }
      } else if (this.endReached) {
         this.chatAdapter.notifyItemRemoved(0);
      }

   }

   // $FF: synthetic method
   public void lambda$null$12$ChannelAdminLogActivity(TLRPC.TL_error var1, TLObject var2) {
      if (var1 == null) {
         TLRPC.TL_channels_channelParticipants var3 = (TLRPC.TL_channels_channelParticipants)var2;
         MessagesController.getInstance(super.currentAccount).putUsers(var3.users, false);
         this.admins = var3.participants;
         Dialog var4 = super.visibleDialog;
         if (var4 instanceof AdminLogFilterAlert) {
            ((AdminLogFilterAlert)var4).setCurrentAdmins(this.admins);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$4$ChannelAdminLogActivity(TLRPC.TL_channelAdminLogEventsFilter var1, SparseArray var2) {
      this.currentFilter = var1;
      this.selectedAdmins = var2;
      if (this.currentFilter == null && this.selectedAdmins == null) {
         this.avatarContainer.setSubtitle(LocaleController.getString("EventLogAllEvents", 2131559385));
      } else {
         this.avatarContainer.setSubtitle(LocaleController.getString("EventLogSelectedEvents", 2131559459));
      }

      this.loadMessages(true);
   }

   // $FF: synthetic method
   public void lambda$null$7$ChannelAdminLogActivity(DatePicker var1, int var2, int var3, int var4) {
      Calendar var7 = Calendar.getInstance();
      var7.clear();
      var7.set(var2, var3, var4);
      long var5 = var7.getTime().getTime() / 1000L;
      this.loadMessages(true);
   }

   // $FF: synthetic method
   public void lambda$showOpenUrlAlert$14$ChannelAdminLogActivity(String var1, DialogInterface var2, int var3) {
      Browser.openUrl(this.getParentActivity(), (String)var1, true);
   }

   public void onConfigurationChanged(Configuration var1) {
      this.fixLayout();
      Dialog var2 = super.visibleDialog;
      if (var2 instanceof DatePickerDialog) {
         var2.dismiss();
      }

   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidStart);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);
      this.loadMessages(true);
      this.loadAdmins();
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidStart);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
   }

   public void onPause() {
      super.onPause();
      SizeNotifierFrameLayout var1 = this.contentView;
      if (var1 != null) {
         var1.onPause();
      }

      this.paused = true;
      this.wasPaused = true;
   }

   protected void onRemoveFromParent() {
      MediaController.getInstance().setTextureView(this.videoTextureView, (AspectRatioFrameLayout)null, (FrameLayout)null, false);
   }

   public void onResume() {
      super.onResume();
      SizeNotifierFrameLayout var1 = this.contentView;
      if (var1 != null) {
         var1.onResume();
      }

      this.paused = false;
      this.checkScrollForLoad(false);
      if (this.wasPaused) {
         this.wasPaused = false;
         ChannelAdminLogActivity.ChatActivityAdapter var2 = this.chatAdapter;
         if (var2 != null) {
            var2.notifyDataSetChanged();
         }
      }

      this.fixLayout();
   }

   public void onTransitionAnimationEnd(boolean var1, boolean var2) {
      NotificationCenter.getInstance(super.currentAccount).setAnimationInProgress(false);
      if (var1) {
         this.openAnimationEnded = true;
      }

   }

   public void onTransitionAnimationStart(boolean var1, boolean var2) {
      NotificationCenter.getInstance(super.currentAccount).setAllowedNotificationsDutingAnimation(new int[]{NotificationCenter.chatInfoDidLoad, NotificationCenter.dialogsNeedReload, NotificationCenter.closeChats, NotificationCenter.messagesDidLoad, NotificationCenter.botKeyboardDidLoad});
      NotificationCenter.getInstance(super.currentAccount).setAnimationInProgress(true);
      if (var1) {
         this.openAnimationEnded = false;
      }

   }

   public void openVCard(String var1, String var2, String var3) {
      try {
         File var4 = new File(FileLoader.getDirectory(4), "sharing/");
         var4.mkdirs();
         File var5 = new File(var4, "vcard.vcf");
         FileWriter var6 = new FileWriter(var5);
         BufferedWriter var9 = new BufferedWriter(var6);
         var9.write(var1);
         var9.close();
         PhonebookShareActivity var8 = new PhonebookShareActivity((ContactsController.Contact)null, (Uri)null, var5, ContactsController.formatName(var2, var3));
         this.presentFragment(var8);
      } catch (Exception var7) {
         FileLog.e((Throwable)var7);
      }

   }

   public void showOpenUrlAlert(String var1, boolean var2) {
      if (!Browser.isInternalUrl(var1, (boolean[])null) && var2) {
         AlertDialog.Builder var3 = new AlertDialog.Builder(this.getParentActivity());
         var3.setTitle(LocaleController.getString("OpenUrlTitle", 2131560121));
         var3.setMessage(LocaleController.formatString("OpenUrlAlert2", 2131560118, var1));
         var3.setPositiveButton(LocaleController.getString("Open", 2131560110), new _$$Lambda$ChannelAdminLogActivity$iAIppxrAcTSiaPaqrhtDiQvqAXs(this, var1));
         var3.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         this.showDialog(var3.create());
      } else {
         Browser.openUrl(this.getParentActivity(), (String)var1, true);
      }

   }

   public class ChatActivityAdapter extends RecyclerView.Adapter {
      private int loadingUpRow;
      private Context mContext;
      private int messagesEndRow;
      private int messagesStartRow;
      private int rowCount;

      public ChatActivityAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         return this.rowCount;
      }

      public long getItemId(int var1) {
         return -1L;
      }

      public int getItemViewType(int var1) {
         if (var1 >= this.messagesStartRow && var1 < this.messagesEndRow) {
            ArrayList var2 = ChannelAdminLogActivity.this.messages;
            return ((MessageObject)var2.get(var2.size() - (var1 - this.messagesStartRow) - 1)).contentType;
         } else {
            return 4;
         }
      }

      // $FF: synthetic method
      public void lambda$onCreateViewHolder$0$ChannelAdminLogActivity$ChatActivityAdapter(String var1) {
         if (var1.startsWith("@")) {
            MessagesController.getInstance(ChannelAdminLogActivity.access$5700(ChannelAdminLogActivity.this)).openByUserName(var1.substring(1), ChannelAdminLogActivity.this, 0);
         } else if (var1.startsWith("#")) {
            DialogsActivity var2 = new DialogsActivity((Bundle)null);
            var2.setSearchString(var1);
            ChannelAdminLogActivity.this.presentFragment(var2);
         }

      }

      public void notifyDataSetChanged() {
         this.updateRows();

         try {
            super.notifyDataSetChanged();
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }

      }

      public void notifyItemChanged(int var1) {
         this.updateRows();

         try {
            super.notifyItemChanged(var1);
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }

      }

      public void notifyItemInserted(int var1) {
         this.updateRows();

         try {
            super.notifyItemInserted(var1);
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }

      }

      public void notifyItemMoved(int var1, int var2) {
         this.updateRows();

         try {
            super.notifyItemMoved(var1, var2);
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

      }

      public void notifyItemRangeChanged(int var1, int var2) {
         this.updateRows();

         try {
            super.notifyItemRangeChanged(var1, var2);
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

      }

      public void notifyItemRangeInserted(int var1, int var2) {
         this.updateRows();

         try {
            super.notifyItemRangeInserted(var1, var2);
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

      }

      public void notifyItemRangeRemoved(int var1, int var2) {
         this.updateRows();

         try {
            super.notifyItemRangeRemoved(var1, var2);
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

      }

      public void notifyItemRemoved(int var1) {
         this.updateRows();

         try {
            super.notifyItemRemoved(var1);
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }

      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = this.loadingUpRow;
         boolean var4 = false;
         boolean var5 = true;
         if (var2 == var3) {
            ChatLoadingCell var12 = (ChatLoadingCell)var1.itemView;
            if (ChannelAdminLogActivity.this.loadsCount > 1) {
               var4 = true;
            }

            var12.setProgressVisible(var4);
         } else if (var2 >= this.messagesStartRow && var2 < this.messagesEndRow) {
            ArrayList var6 = ChannelAdminLogActivity.this.messages;
            MessageObject var17 = (MessageObject)var6.get(var6.size() - (var2 - this.messagesStartRow) - 1);
            View var7 = var1.itemView;
            if (var7 instanceof ChatMessageCell) {
               ChatMessageCell var18;
               TLRPC.Message var20;
               label46: {
                  var18 = (ChatMessageCell)var7;
                  var18.isChat = true;
                  int var8 = var2 + 1;
                  int var9 = this.getItemViewType(var8);
                  var3 = this.getItemViewType(var2 - 1);
                  if (!(var17.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup) && var9 == var1.getItemViewType()) {
                     ArrayList var10 = ChannelAdminLogActivity.this.messages;
                     MessageObject var19 = (MessageObject)var10.get(var10.size() - (var8 - this.messagesStartRow) - 1);
                     if (var19.isOutOwner() == var17.isOutOwner()) {
                        TLRPC.Message var11 = var19.messageOwner;
                        var8 = var11.from_id;
                        var20 = var17.messageOwner;
                        if (var8 == var20.from_id && Math.abs(var11.date - var20.date) <= 300) {
                           var4 = true;
                           break label46;
                        }
                     }
                  }

                  var4 = false;
               }

               label38: {
                  if (var3 == var1.getItemViewType()) {
                     ArrayList var13 = ChannelAdminLogActivity.this.messages;
                     MessageObject var14 = (MessageObject)var13.get(var13.size() - (var2 - this.messagesStartRow));
                     if (!(var14.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup) && var14.isOutOwner() == var17.isOutOwner()) {
                        TLRPC.Message var15 = var14.messageOwner;
                        var2 = var15.from_id;
                        var20 = var17.messageOwner;
                        if (var2 == var20.from_id && Math.abs(var15.date - var20.date) <= 300) {
                           break label38;
                        }
                     }
                  }

                  var5 = false;
               }

               var18.setMessageObject(var17, (MessageObject.GroupedMessages)null, var4, var5);
               var18.setHighlighted(false);
               var18.setHighlightedText((String)null);
            } else if (var7 instanceof ChatActionCell) {
               ChatActionCell var16 = (ChatActionCell)var7;
               var16.setMessageObject(var17);
               var16.setAlpha(1.0F);
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var4;
         if (var2 == 0) {
            if (!ChannelAdminLogActivity.this.chatMessageCellsCache.isEmpty()) {
               var4 = (View)ChannelAdminLogActivity.this.chatMessageCellsCache.get(0);
               ChannelAdminLogActivity.this.chatMessageCellsCache.remove(0);
            } else {
               var4 = new ChatMessageCell(this.mContext);
            }

            ChatMessageCell var3 = (ChatMessageCell)var4;
            var3.setDelegate(new ChatMessageCell.ChatMessageCellDelegate() {
               public boolean canPerformActions() {
                  return true;
               }

               public void didLongPress(ChatMessageCell var1, float var2, float var3) {
                  ChannelAdminLogActivity.this.createMenu(var1);
               }

               public void didPressBotButton(ChatMessageCell var1, TLRPC.KeyboardButton var2) {
               }

               public void didPressCancelSendButton(ChatMessageCell var1) {
               }

               public void didPressChannelAvatar(ChatMessageCell var1, TLRPC.Chat var2, int var3, float var4, float var5) {
                  if (var2 != null && var2 != ChannelAdminLogActivity.this.currentChat) {
                     Bundle var6 = new Bundle();
                     var6.putInt("chat_id", var2.id);
                     if (var3 != 0) {
                        var6.putInt("message_id", var3);
                     }

                     if (MessagesController.getInstance(ChannelAdminLogActivity.access$4200(ChannelAdminLogActivity.this)).checkCanOpenChat(var6, ChannelAdminLogActivity.this)) {
                        ChannelAdminLogActivity.this.presentFragment(new ChatActivity(var6), true);
                     }
                  }

               }

               // $FF: synthetic method
               public void didPressHiddenForward(ChatMessageCell var1) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressHiddenForward(this, var1);
               }

               public void didPressImage(ChatMessageCell var1, float var2, float var3) {
                  MessageObject var4 = var1.getMessageObject();
                  ChannelAdminLogActivity var17;
                  if (var4.type == 13) {
                     var17 = ChannelAdminLogActivity.this;
                     var17.showDialog(new StickersAlert(var17.getParentActivity(), ChannelAdminLogActivity.this, var4.getInputStickerSet(), (TLRPC.TL_messages_stickerSet)null, (StickersAlert.StickersAlertDelegate)null));
                  } else {
                     if (!var4.isVideo()) {
                        int var5 = var4.type;
                        if (var5 != 1 && (var5 != 0 || var4.isWebpageDocument()) && !var4.isGif()) {
                           var5 = var4.type;
                           File var6 = null;
                           File var18;
                           if (var5 == 3) {
                              var18 = var6;

                              label149: {
                                 boolean var10001;
                                 label150: {
                                    try {
                                       if (var4.messageOwner.attachPath == null) {
                                          break label150;
                                       }
                                    } catch (Exception var16) {
                                       var10001 = false;
                                       break label149;
                                    }

                                    var18 = var6;

                                    try {
                                       if (var4.messageOwner.attachPath.length() != 0) {
                                          var18 = new File(var4.messageOwner.attachPath);
                                       }
                                    } catch (Exception var15) {
                                       var10001 = false;
                                       break label149;
                                    }
                                 }

                                 label124: {
                                    if (var18 != null) {
                                       var6 = var18;

                                       try {
                                          if (var18.exists()) {
                                             break label124;
                                          }
                                       } catch (Exception var14) {
                                          var10001 = false;
                                          break label149;
                                       }
                                    }

                                    try {
                                       var6 = FileLoader.getPathToMessage(var4.messageOwner);
                                    } catch (Exception var13) {
                                       var10001 = false;
                                       break label149;
                                    }
                                 }

                                 Intent var19;
                                 try {
                                    var19 = new Intent("android.intent.action.VIEW");
                                    var5 = VERSION.SDK_INT;
                                 } catch (Exception var12) {
                                    var10001 = false;
                                    break label149;
                                 }

                                 if (var5 >= 24) {
                                    try {
                                       var19.setFlags(1);
                                       var19.setDataAndType(FileProvider.getUriForFile(ChannelAdminLogActivity.this.getParentActivity(), "org.telegram.messenger.provider", var6), "video/mp4");
                                    } catch (Exception var11) {
                                       var10001 = false;
                                       break label149;
                                    }
                                 } else {
                                    try {
                                       var19.setDataAndType(Uri.fromFile(var6), "video/mp4");
                                    } catch (Exception var10) {
                                       var10001 = false;
                                       break label149;
                                    }
                                 }

                                 try {
                                    ChannelAdminLogActivity.this.getParentActivity().startActivityForResult(var19, 500);
                                    return;
                                 } catch (Exception var9) {
                                    var10001 = false;
                                 }
                              }

                              ChannelAdminLogActivity.this.alertUserOpenError(var4);
                              return;
                           } else {
                              if (var5 == 4) {
                                 if (!AndroidUtilities.isGoogleMapsInstalled(ChannelAdminLogActivity.this)) {
                                    return;
                                 }

                                 LocationActivity var20 = new LocationActivity(0);
                                 var20.setMessageObject(var4);
                                 ChannelAdminLogActivity.this.presentFragment(var20);
                              } else if (var5 == 9 || var5 == 0) {
                                 if (var4.getDocumentName().toLowerCase().endsWith("attheme")) {
                                    label96: {
                                       String var21 = var4.messageOwner.attachPath;
                                       if (var21 != null && var21.length() != 0) {
                                          var18 = new File(var4.messageOwner.attachPath);
                                          if (var18.exists()) {
                                             break label96;
                                          }
                                       }

                                       var18 = null;
                                    }

                                    var6 = var18;
                                    if (var18 == null) {
                                       File var7 = FileLoader.getPathToMessage(var4.messageOwner);
                                       var6 = var18;
                                       if (var7.exists()) {
                                          var6 = var7;
                                       }
                                    }

                                    if (ChannelAdminLogActivity.this.chatLayoutManager != null) {
                                       if (ChannelAdminLogActivity.this.chatLayoutManager.findLastVisibleItemPosition() < ChannelAdminLogActivity.this.chatLayoutManager.getItemCount() - 1) {
                                          var17 = ChannelAdminLogActivity.this;
                                          var17.scrollToPositionOnRecreate = var17.chatLayoutManager.findFirstVisibleItemPosition();
                                          RecyclerListView.Holder var22 = (RecyclerListView.Holder)ChannelAdminLogActivity.this.chatListView.findViewHolderForAdapterPosition(ChannelAdminLogActivity.this.scrollToPositionOnRecreate);
                                          if (var22 != null) {
                                             ChannelAdminLogActivity.this.scrollToOffsetOnRecreate = var22.itemView.getTop();
                                          } else {
                                             ChannelAdminLogActivity.this.scrollToPositionOnRecreate = -1;
                                          }
                                       } else {
                                          ChannelAdminLogActivity.this.scrollToPositionOnRecreate = -1;
                                       }
                                    }

                                    Theme.ThemeInfo var23 = Theme.applyThemeFile(var6, var4.getDocumentName(), true);
                                    if (var23 != null) {
                                       ChannelAdminLogActivity.this.presentFragment(new ThemePreviewActivity(var6, var23));
                                       return;
                                    }

                                    ChannelAdminLogActivity.this.scrollToPositionOnRecreate = -1;
                                 }

                                 try {
                                    AndroidUtilities.openForView(var4, ChannelAdminLogActivity.this.getParentActivity());
                                 } catch (Exception var8) {
                                    ChannelAdminLogActivity.this.alertUserOpenError(var4);
                                 }

                                 return;
                              }

                              return;
                           }
                        }
                     }

                     PhotoViewer.getInstance().setParentActivity(ChannelAdminLogActivity.this.getParentActivity());
                     PhotoViewer.getInstance().openPhoto(var4, 0L, 0L, ChannelAdminLogActivity.this.provider);
                  }

               }

               public void didPressInstantButton(ChatMessageCell var1, int var2) {
                  MessageObject var4 = var1.getMessageObject();
                  TLRPC.MessageMedia var3;
                  if (var2 == 0) {
                     var3 = var4.messageOwner.media;
                     if (var3 != null) {
                        TLRPC.WebPage var6 = var3.webpage;
                        if (var6 != null && var6.cached_page != null) {
                           ArticleViewer.getInstance().setParentActivity(ChannelAdminLogActivity.this.getParentActivity(), ChannelAdminLogActivity.this);
                           ArticleViewer.getInstance().open(var4);
                        }
                     }
                  } else if (var2 == 5) {
                     ChannelAdminLogActivity var7 = ChannelAdminLogActivity.this;
                     TLRPC.MessageMedia var5 = var4.messageOwner.media;
                     var7.openVCard(var5.vcard, var5.first_name, var5.last_name);
                  } else {
                     var3 = var4.messageOwner.media;
                     if (var3 != null && var3.webpage != null) {
                        Browser.openUrl(ChannelAdminLogActivity.this.getParentActivity(), (String)var4.messageOwner.media.webpage.url);
                     }
                  }

               }

               public void didPressOther(ChatMessageCell var1, float var2, float var3) {
                  ChannelAdminLogActivity.this.createMenu(var1);
               }

               public void didPressReplyMessage(ChatMessageCell var1, int var2) {
               }

               public void didPressShare(ChatMessageCell var1) {
                  if (ChannelAdminLogActivity.this.getParentActivity() != null) {
                     ChannelAdminLogActivity.ChatActivityAdapter var2 = ChatActivityAdapter.this;
                     ChannelAdminLogActivity var3 = ChannelAdminLogActivity.this;
                     Context var6 = var2.mContext;
                     MessageObject var5 = var1.getMessageObject();
                     boolean var4;
                     if (ChatObject.isChannel(ChannelAdminLogActivity.this.currentChat) && !ChannelAdminLogActivity.this.currentChat.megagroup) {
                        var4 = true;
                     } else {
                        var4 = false;
                     }

                     var3.showDialog(ShareAlert.createShareAlert(var6, var5, (String)null, var4, (String)null, false));
                  }
               }

               public void didPressUrl(MessageObject var1, CharacterStyle var2, boolean var3) {
                  if (var2 != null) {
                     if (var2 instanceof URLSpanMono) {
                        ((URLSpanMono)var2).copyToClipboard();
                        Toast.makeText(ChannelAdminLogActivity.this.getParentActivity(), LocaleController.getString("TextCopied", 2131560887), 0).show();
                     } else if (var2 instanceof URLSpanUserMention) {
                        TLRPC.User var8 = MessagesController.getInstance(ChannelAdminLogActivity.access$4600(ChannelAdminLogActivity.this)).getUser(Utilities.parseInt(((URLSpanUserMention)var2).getURL()));
                        if (var8 != null) {
                           MessagesController.openChatOrProfileWith(var8, (TLRPC.Chat)null, ChannelAdminLogActivity.this, 0, false);
                        }
                     } else {
                        String var11;
                        if (var2 instanceof URLSpanNoUnderline) {
                           var11 = ((URLSpanNoUnderline)var2).getURL();
                           if (var11.startsWith("@")) {
                              MessagesController.getInstance(ChannelAdminLogActivity.access$4700(ChannelAdminLogActivity.this)).openByUserName(var11.substring(1), ChannelAdminLogActivity.this, 0);
                           } else if (var11.startsWith("#")) {
                              DialogsActivity var7 = new DialogsActivity((Bundle)null);
                              var7.setSearchString(var11);
                              ChannelAdminLogActivity.this.presentFragment(var7);
                           }
                        } else {
                           String var4 = ((URLSpan)var2).getURL();
                           String var5;
                           if (var3) {
                              BottomSheet.Builder var6 = new BottomSheet.Builder(ChannelAdminLogActivity.this.getParentActivity());
                              var6.setTitle(var4);
                              var5 = LocaleController.getString("Open", 2131560110);
                              var11 = LocaleController.getString("Copy", 2131559163);
                              _$$Lambda$ChannelAdminLogActivity$ChatActivityAdapter$1$D0xLcgtrHEslS6nrwCqImW0Sa1U var12 = new _$$Lambda$ChannelAdminLogActivity$ChatActivityAdapter$1$D0xLcgtrHEslS6nrwCqImW0Sa1U(this, var4);
                              var6.setItems(new CharSequence[]{var5, var11}, var12);
                              ChannelAdminLogActivity.this.showDialog(var6.create());
                           } else if (var2 instanceof URLSpanReplacement) {
                              ChannelAdminLogActivity.this.showOpenUrlAlert(((URLSpanReplacement)var2).getURL(), true);
                           } else if (!(var2 instanceof URLSpan)) {
                              if (var2 instanceof ClickableSpan) {
                                 ((ClickableSpan)var2).onClick(ChannelAdminLogActivity.access$4800(ChannelAdminLogActivity.this));
                              }
                           } else {
                              TLRPC.MessageMedia var9 = var1.messageOwner.media;
                              if (var9 instanceof TLRPC.TL_messageMediaWebPage) {
                                 TLRPC.WebPage var10 = var9.webpage;
                                 if (var10 != null && var10.cached_page != null) {
                                    var11 = var4.toLowerCase();
                                    var5 = var1.messageOwner.media.webpage.url.toLowerCase();
                                    if ((var11.contains("telegra.ph") || var11.contains("t.me/iv")) && (var11.contains(var5) || var5.contains(var11))) {
                                       ArticleViewer.getInstance().setParentActivity(ChannelAdminLogActivity.this.getParentActivity(), ChannelAdminLogActivity.this);
                                       ArticleViewer.getInstance().open(var1);
                                       return;
                                    }
                                 }
                              }

                              Browser.openUrl(ChannelAdminLogActivity.this.getParentActivity(), (String)var4, true);
                           }
                        }
                     }

                  }
               }

               public void didPressUserAvatar(ChatMessageCell var1, TLRPC.User var2, float var3, float var4) {
                  if (var2 != null && var2.id != UserConfig.getInstance(ChannelAdminLogActivity.access$4400(ChannelAdminLogActivity.this)).getClientUserId()) {
                     Bundle var5 = new Bundle();
                     var5.putInt("user_id", var2.id);
                     ChannelAdminLogActivity.this.addCanBanUser(var5, var2.id);
                     ProfileActivity var6 = new ProfileActivity(var5);
                     var6.setPlayProfileAnimation(false);
                     ChannelAdminLogActivity.this.presentFragment(var6);
                  }

               }

               public void didPressViaBot(ChatMessageCell var1, String var2) {
               }

               public void didPressVoteButton(ChatMessageCell var1, TLRPC.TL_pollAnswer var2) {
               }

               // $FF: synthetic method
               public void didStartVideoStream(MessageObject var1) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didStartVideoStream(this, var1);
               }

               public boolean isChatAdminCell(int var1) {
                  return false;
               }

               // $FF: synthetic method
               public void lambda$didPressUrl$0$ChannelAdminLogActivity$ChatActivityAdapter$1(String var1, DialogInterface var2, int var3) {
                  if (var3 == 0) {
                     Browser.openUrl(ChannelAdminLogActivity.this.getParentActivity(), (String)var1, true);
                  } else if (var3 == 1) {
                     String var4;
                     if (var1.startsWith("mailto:")) {
                        var4 = var1.substring(7);
                     } else {
                        var4 = var1;
                        if (var1.startsWith("tel:")) {
                           var4 = var1.substring(4);
                        }
                     }

                     AndroidUtilities.addToClipboard(var4);
                  }

               }

               public void needOpenWebView(String var1, String var2, String var3, String var4, int var5, int var6) {
                  EmbedBottomSheet.show(ChatActivityAdapter.this.mContext, var2, var3, var4, var1, var5, var6);
               }

               public boolean needPlayMessage(MessageObject var1) {
                  if (!var1.isVoice() && !var1.isRoundVideo()) {
                     return var1.isMusic() ? MediaController.getInstance().setPlaylist(ChannelAdminLogActivity.this.messages, var1) : false;
                  } else {
                     boolean var2 = MediaController.getInstance().playMessage(var1);
                     MediaController.getInstance().setVoiceMessagesPlaylist((ArrayList)null, false);
                     return var2;
                  }
               }

               // $FF: synthetic method
               public void videoTimerReached() {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$videoTimerReached(this);
               }
            });
            var3.setAllowAssistant(true);
         } else if (var2 == 1) {
            var4 = new ChatActionCell(this.mContext);
            ((ChatActionCell)var4).setDelegate(new ChatActionCell.ChatActionCellDelegate() {
               public void didClickImage(ChatActionCell var1) {
                  MessageObject var3 = var1.getMessageObject();
                  PhotoViewer.getInstance().setParentActivity(ChannelAdminLogActivity.this.getParentActivity());
                  TLRPC.PhotoSize var2 = FileLoader.getClosestPhotoSizeWithSize(var3.photoThumbs, 640);
                  if (var2 != null) {
                     PhotoViewer.getInstance().openPhoto(var2.location, ChannelAdminLogActivity.this.provider);
                  } else {
                     PhotoViewer.getInstance().openPhoto(var3, 0L, 0L, ChannelAdminLogActivity.this.provider);
                  }

               }

               public void didLongPress(ChatActionCell var1, float var2, float var3) {
                  ChannelAdminLogActivity.this.createMenu(var1);
               }

               public void didPressBotButton(MessageObject var1, TLRPC.KeyboardButton var2) {
               }

               public void didPressReplyMessage(ChatActionCell var1, int var2) {
               }

               public void needOpenUserProfile(int var1) {
                  Bundle var2;
                  if (var1 < 0) {
                     var2 = new Bundle();
                     var2.putInt("chat_id", -var1);
                     if (MessagesController.getInstance(ChannelAdminLogActivity.access$5400(ChannelAdminLogActivity.this)).checkCanOpenChat(var2, ChannelAdminLogActivity.this)) {
                        ChannelAdminLogActivity.this.presentFragment(new ChatActivity(var2), true);
                     }
                  } else if (var1 != UserConfig.getInstance(ChannelAdminLogActivity.access$5500(ChannelAdminLogActivity.this)).getClientUserId()) {
                     var2 = new Bundle();
                     var2.putInt("user_id", var1);
                     ChannelAdminLogActivity.this.addCanBanUser(var2, var1);
                     ProfileActivity var3 = new ProfileActivity(var2);
                     var3.setPlayProfileAnimation(false);
                     ChannelAdminLogActivity.this.presentFragment(var3);
                  }

               }
            });
         } else if (var2 == 2) {
            var4 = new ChatUnreadCell(this.mContext);
         } else if (var2 == 3) {
            var4 = new BotHelpCell(this.mContext);
            ((BotHelpCell)var4).setDelegate(new _$$Lambda$ChannelAdminLogActivity$ChatActivityAdapter$17meO0dSqjA1BWDg9Mv1dvdogDs(this));
         } else if (var2 == 4) {
            var4 = new ChatLoadingCell(this.mContext);
         } else {
            var4 = null;
         }

         ((View)var4).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         return new RecyclerListView.Holder((View)var4);
      }

      public void onViewAttachedToWindow(RecyclerView.ViewHolder var1) {
         View var2 = var1.itemView;
         if (var2 instanceof ChatMessageCell) {
            final ChatMessageCell var3 = (ChatMessageCell)var2;
            var3.getMessageObject();
            var3.setBackgroundDrawable((Drawable)null);
            var3.setCheckPressed(true, false);
            var3.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
               public boolean onPreDraw() {
                  var3.getViewTreeObserver().removeOnPreDrawListener(this);
                  int var1 = ChannelAdminLogActivity.this.chatListView.getMeasuredHeight();
                  int var2 = var3.getTop();
                  var3.getBottom();
                  if (var2 >= 0) {
                     var2 = 0;
                  } else {
                     var2 = -var2;
                  }

                  int var3x = var3.getMeasuredHeight();
                  int var4 = var3x;
                  if (var3x > var1) {
                     var4 = var2 + var1;
                  }

                  var3.setVisiblePart(var2, var4 - var2);
                  return true;
               }
            });
            var3.setHighlighted(false);
         }

      }

      public void updateRowWithMessageObject(MessageObject var1) {
         int var2 = ChannelAdminLogActivity.this.messages.indexOf(var1);
         if (var2 != -1) {
            this.notifyItemChanged(this.messagesStartRow + ChannelAdminLogActivity.this.messages.size() - var2 - 1);
         }
      }

      public void updateRows() {
         this.rowCount = 0;
         if (!ChannelAdminLogActivity.this.messages.isEmpty()) {
            int var1;
            if (!ChannelAdminLogActivity.this.endReached) {
               var1 = this.rowCount++;
               this.loadingUpRow = var1;
            } else {
               this.loadingUpRow = -1;
            }

            var1 = this.rowCount;
            this.messagesStartRow = var1;
            this.rowCount = var1 + ChannelAdminLogActivity.this.messages.size();
            this.messagesEndRow = this.rowCount;
         } else {
            this.loadingUpRow = -1;
            this.messagesStartRow = -1;
            this.messagesEndRow = -1;
         }

      }
   }
}
