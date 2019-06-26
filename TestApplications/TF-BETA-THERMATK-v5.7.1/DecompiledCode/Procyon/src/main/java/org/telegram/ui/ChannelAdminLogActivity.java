// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.ui.Cells.BotHelpCell;
import org.telegram.ui.Components.EmbedBottomSheet;
import android.text.style.ClickableSpan;
import org.telegram.ui.Components.URLSpanReplacement;
import org.telegram.ui.ActionBar.BottomSheet;
import android.text.style.URLSpan;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Components.URLSpanUserMention;
import android.widget.Toast;
import org.telegram.ui.Components.URLSpanMono;
import android.text.style.CharacterStyle;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Cells.ChatMessageCell$ChatMessageCellDelegate$_CC;
import android.view.ViewGroup;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import android.content.res.Configuration;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.Components.AdminLogFilterAlert;
import android.content.DialogInterface$OnShowListener;
import android.app.DatePickerDialog$OnDateSetListener;
import android.app.DatePickerDialog;
import java.util.Calendar;
import org.telegram.ui.Cells.ChatLoadingCell;
import org.telegram.ui.Cells.ChatUnreadCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.widget.ImageView$ScaleType;
import android.graphics.PorterDuffColorFilter;
import android.view.View$OnClickListener;
import android.graphics.ColorFilter;
import androidx.recyclerview.widget.LinearSmoothScrollerMiddle;
import android.view.animation.LayoutAnimationController;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View$OnTouchListener;
import android.view.View$MeasureSpec;
import android.widget.EditText;
import android.graphics.drawable.Drawable;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.Components.PipRoundVideoView;
import android.app.Activity;
import org.telegram.ui.ActionBar.Theme;
import android.os.Parcelable;
import androidx.core.content.FileProvider;
import org.telegram.messenger.MediaController;
import android.text.TextUtils;
import org.telegram.messenger.FileLog;
import android.content.Intent;
import android.net.Uri;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import android.widget.DatePicker;
import android.content.DialogInterface;
import android.view.MotionEvent;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import org.telegram.messenger.FileLoader;
import java.io.File;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DataQuery;
import android.view.ViewTreeObserver$OnPreDrawListener;
import android.widget.FrameLayout$LayoutParams;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff$Mode;
import android.graphics.Path$Direction;
import android.graphics.Canvas;
import android.annotation.TargetApi;
import android.graphics.Outline;
import android.view.ViewOutlineProvider;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.Components.StickersAlert;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.messenger.ChatObject;
import android.os.Bundle;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.AndroidUtilities;
import android.os.Build$VERSION;
import android.view.TextureView;
import android.util.SparseArray;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.SimpleTextView;
import android.view.View;
import org.telegram.ui.Components.RadialProgressView;
import android.util.LongSparseArray;
import java.util.HashMap;
import org.telegram.messenger.MessageObject;
import org.telegram.ui.Cells.ChatActionCell;
import android.animation.AnimatorSet;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout;
import org.telegram.ui.Components.ChatAvatarContainer;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import android.graphics.Path;
import android.graphics.Paint;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class ChannelAdminLogActivity extends BaseFragment implements NotificationCenterDelegate
{
    private ArrayList<TLRPC.ChannelParticipant> admins;
    private Paint aspectPaint;
    private Path aspectPath;
    private AspectRatioFrameLayout aspectRatioFrameLayout;
    private ChatAvatarContainer avatarContainer;
    private FrameLayout bottomOverlayChat;
    private TextView bottomOverlayChatText;
    private ImageView bottomOverlayImage;
    private ChatActivityAdapter chatAdapter;
    private LinearLayoutManager chatLayoutManager;
    private RecyclerListView chatListView;
    private ArrayList<ChatMessageCell> chatMessageCellsCache;
    private boolean checkTextureViewPosition;
    private SizeNotifierFrameLayout contentView;
    protected TLRPC.Chat currentChat;
    private TLRPC.TL_channelAdminLogEventsFilter currentFilter;
    private boolean currentFloatingDateOnScreen;
    private boolean currentFloatingTopIsNotMessage;
    private TextView emptyView;
    private FrameLayout emptyViewContainer;
    private boolean endReached;
    private AnimatorSet floatingDateAnimation;
    private ChatActionCell floatingDateView;
    private boolean loading;
    private int loadsCount;
    protected ArrayList<MessageObject> messages;
    private HashMap<String, ArrayList<MessageObject>> messagesByDays;
    private LongSparseArray<MessageObject> messagesDict;
    private int[] mid;
    private int minDate;
    private long minEventId;
    private boolean openAnimationEnded;
    private boolean paused;
    private RadialProgressView progressBar;
    private FrameLayout progressView;
    private View progressView2;
    private PhotoViewer.PhotoViewerProvider provider;
    private FrameLayout roundVideoContainer;
    private MessageObject scrollToMessage;
    private int scrollToOffsetOnRecreate;
    private int scrollToPositionOnRecreate;
    private boolean scrollingFloatingDate;
    private ImageView searchCalendarButton;
    private FrameLayout searchContainer;
    private SimpleTextView searchCountText;
    private ImageView searchDownButton;
    private ActionBarMenuItem searchItem;
    private String searchQuery;
    private ImageView searchUpButton;
    private boolean searchWas;
    private SparseArray<TLRPC.User> selectedAdmins;
    private MessageObject selectedObject;
    private TextureView videoTextureView;
    private boolean wasPaused;
    
    public ChannelAdminLogActivity(final TLRPC.Chat currentChat) {
        this.chatMessageCellsCache = new ArrayList<ChatMessageCell>();
        this.mid = new int[] { 2 };
        this.scrollToPositionOnRecreate = -1;
        this.scrollToOffsetOnRecreate = 0;
        this.paused = true;
        this.wasPaused = false;
        this.messagesDict = (LongSparseArray<MessageObject>)new LongSparseArray();
        this.messagesByDays = new HashMap<String, ArrayList<MessageObject>>();
        this.messages = new ArrayList<MessageObject>();
        this.currentFilter = null;
        this.searchQuery = "";
        this.provider = new PhotoViewer.EmptyPhotoViewerProvider() {
            @Override
            public PlaceProviderObject getPlaceForPhoto(final MessageObject messageObject, final TLRPC.FileLocation fileLocation, int statusBarHeight, final boolean b) {
                final int childCount = ChannelAdminLogActivity.this.chatListView.getChildCount();
                final int n = 0;
                statusBarHeight = 0;
                while (true) {
                    final ImageReceiver imageReceiver = null;
                    if (statusBarHeight >= childCount) {
                        return null;
                    }
                    final View child = ChannelAdminLogActivity.this.chatListView.getChildAt(statusBarHeight);
                    ImageReceiver imageReceiver2;
                    if (child instanceof ChatMessageCell) {
                        imageReceiver2 = imageReceiver;
                        if (messageObject != null) {
                            final ChatMessageCell chatMessageCell = (ChatMessageCell)child;
                            final MessageObject messageObject2 = chatMessageCell.getMessageObject();
                            imageReceiver2 = imageReceiver;
                            if (messageObject2 != null) {
                                imageReceiver2 = imageReceiver;
                                if (messageObject2.getId() == messageObject.getId()) {
                                    imageReceiver2 = chatMessageCell.getPhotoImage();
                                }
                            }
                        }
                    }
                    else {
                        imageReceiver2 = imageReceiver;
                        if (child instanceof ChatActionCell) {
                            final ChatActionCell chatActionCell = (ChatActionCell)child;
                            final MessageObject messageObject3 = chatActionCell.getMessageObject();
                            imageReceiver2 = imageReceiver;
                            if (messageObject3 != null) {
                                if (messageObject != null) {
                                    imageReceiver2 = imageReceiver;
                                    if (messageObject3.getId() == messageObject.getId()) {
                                        imageReceiver2 = chatActionCell.getPhotoImage();
                                    }
                                }
                                else {
                                    imageReceiver2 = imageReceiver;
                                    if (fileLocation != null) {
                                        imageReceiver2 = imageReceiver;
                                        if (messageObject3.photoThumbs != null) {
                                            int index = 0;
                                            while (true) {
                                                imageReceiver2 = imageReceiver;
                                                if (index >= messageObject3.photoThumbs.size()) {
                                                    break;
                                                }
                                                final TLRPC.FileLocation location = messageObject3.photoThumbs.get(index).location;
                                                if (location.volume_id == fileLocation.volume_id && location.local_id == fileLocation.local_id) {
                                                    imageReceiver2 = chatActionCell.getPhotoImage();
                                                    break;
                                                }
                                                ++index;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (imageReceiver2 != null) {
                        final int[] array = new int[2];
                        child.getLocationInWindow(array);
                        final PlaceProviderObject placeProviderObject = new PhotoViewer.PlaceProviderObject();
                        placeProviderObject.viewX = array[0];
                        final int n2 = array[1];
                        if (Build$VERSION.SDK_INT >= 21) {
                            statusBarHeight = n;
                        }
                        else {
                            statusBarHeight = AndroidUtilities.statusBarHeight;
                        }
                        placeProviderObject.viewY = n2 - statusBarHeight;
                        placeProviderObject.parentView = (View)ChannelAdminLogActivity.this.chatListView;
                        placeProviderObject.imageReceiver = imageReceiver2;
                        placeProviderObject.thumb = imageReceiver2.getBitmapSafe();
                        placeProviderObject.radius = imageReceiver2.getRoundRadius();
                        placeProviderObject.isEvent = true;
                        return placeProviderObject;
                    }
                    ++statusBarHeight;
                }
            }
        };
        this.currentChat = currentChat;
    }
    
    private void addCanBanUser(final Bundle bundle, final int n) {
        final TLRPC.Chat currentChat = this.currentChat;
        if (currentChat.megagroup && this.admins != null) {
            if (ChatObject.canBlockUsers(currentChat)) {
                int i = 0;
                while (i < this.admins.size()) {
                    final TLRPC.ChannelParticipant channelParticipant = this.admins.get(i);
                    if (channelParticipant.user_id == n) {
                        if (!channelParticipant.can_edit) {
                            return;
                        }
                        break;
                    }
                    else {
                        ++i;
                    }
                }
                bundle.putInt("ban_chat_id", this.currentChat.id);
            }
        }
    }
    
    private void alertUserOpenError(final MessageObject messageObject) {
        if (this.getParentActivity() == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
        builder.setTitle(LocaleController.getString("AppName", 2131558635));
        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
        if (messageObject.type == 3) {
            builder.setMessage(LocaleController.getString("NoPlayerInstalled", 2131559938));
        }
        else {
            builder.setMessage(LocaleController.formatString("NoHandleAppInstalled", 2131559926, messageObject.getDocument().mime_type));
        }
        this.showDialog(builder.create());
    }
    
    private void checkScrollForLoad(final boolean b) {
        final LinearLayoutManager chatLayoutManager = this.chatLayoutManager;
        if (chatLayoutManager != null) {
            if (!this.paused) {
                final int firstVisibleItemPosition = chatLayoutManager.findFirstVisibleItemPosition();
                int n;
                if (firstVisibleItemPosition == -1) {
                    n = 0;
                }
                else {
                    n = Math.abs(this.chatLayoutManager.findLastVisibleItemPosition() - firstVisibleItemPosition) + 1;
                }
                if (n > 0) {
                    this.chatAdapter.getItemCount();
                    int n2;
                    if (b) {
                        n2 = 25;
                    }
                    else {
                        n2 = 5;
                    }
                    if (firstVisibleItemPosition <= n2 && !this.loading && !this.endReached) {
                        this.loadMessages(false);
                    }
                }
            }
        }
    }
    
    private void createMenu(final View view) {
        MessageObject selectedObject;
        if (view instanceof ChatMessageCell) {
            selectedObject = ((ChatMessageCell)view).getMessageObject();
        }
        else if (view instanceof ChatActionCell) {
            selectedObject = ((ChatActionCell)view).getMessageObject();
        }
        else {
            selectedObject = null;
        }
        if (selectedObject == null) {
            return;
        }
        final int messageType = this.getMessageType(selectedObject);
        this.selectedObject = selectedObject;
        if (this.getParentActivity() == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
        final ArrayList<String> list = new ArrayList<String>();
        final ArrayList<Integer> list2 = new ArrayList<Integer>();
        final MessageObject selectedObject2 = this.selectedObject;
        if (selectedObject2.type == 0 || selectedObject2.caption != null) {
            list.add(LocaleController.getString("Copy", 2131559163));
            list2.add(3);
        }
        if (messageType == 1) {
            final TLRPC.TL_channelAdminLogEvent currentEvent = this.selectedObject.currentEvent;
            if (currentEvent != null) {
                final TLRPC.ChannelAdminLogEventAction action = currentEvent.action;
                if (action instanceof TLRPC.TL_channelAdminLogEventActionChangeStickerSet) {
                    final TLRPC.InputStickerSet new_stickerset = action.new_stickerset;
                    TLRPC.InputStickerSet prev_stickerset = null;
                    Label_0201: {
                        if (new_stickerset != null) {
                            prev_stickerset = new_stickerset;
                            if (!(new_stickerset instanceof TLRPC.TL_inputStickerSetEmpty)) {
                                break Label_0201;
                            }
                        }
                        prev_stickerset = this.selectedObject.currentEvent.action.prev_stickerset;
                    }
                    if (prev_stickerset != null) {
                        this.showDialog(new StickersAlert((Context)this.getParentActivity(), this, prev_stickerset, null, null));
                        return;
                    }
                }
            }
        }
        else if (messageType == 3) {
            final TLRPC.MessageMedia media = this.selectedObject.messageOwner.media;
            if (media instanceof TLRPC.TL_messageMediaWebPage && MessageObject.isNewGifDocument(media.webpage.document)) {
                list.add(LocaleController.getString("SaveToGIFs", 2131560629));
                list2.add(11);
            }
        }
        else if (messageType == 4) {
            if (this.selectedObject.isVideo()) {
                list.add(LocaleController.getString("SaveToGallery", 2131560630));
                list2.add(4);
                list.add(LocaleController.getString("ShareFile", 2131560748));
                list2.add(6);
            }
            else if (this.selectedObject.isMusic()) {
                list.add(LocaleController.getString("SaveToMusic", 2131560632));
                list2.add(10);
                list.add(LocaleController.getString("ShareFile", 2131560748));
                list2.add(6);
            }
            else if (this.selectedObject.getDocument() != null) {
                if (MessageObject.isNewGifDocument(this.selectedObject.getDocument())) {
                    list.add(LocaleController.getString("SaveToGIFs", 2131560629));
                    list2.add(11);
                }
                list.add(LocaleController.getString("SaveToDownloads", 2131560628));
                list2.add(10);
                list.add(LocaleController.getString("ShareFile", 2131560748));
                list2.add(6);
            }
            else {
                list.add(LocaleController.getString("SaveToGallery", 2131560630));
                list2.add(4);
            }
        }
        else if (messageType == 5) {
            list.add(LocaleController.getString("ApplyLocalizationFile", 2131558638));
            list2.add(5);
            list.add(LocaleController.getString("SaveToDownloads", 2131560628));
            list2.add(10);
            list.add(LocaleController.getString("ShareFile", 2131560748));
            list2.add(6);
        }
        else if (messageType == 10) {
            list.add(LocaleController.getString("ApplyThemeFile", 2131558640));
            list2.add(5);
            list.add(LocaleController.getString("SaveToDownloads", 2131560628));
            list2.add(10);
            list.add(LocaleController.getString("ShareFile", 2131560748));
            list2.add(6);
        }
        else if (messageType == 6) {
            list.add(LocaleController.getString("SaveToGallery", 2131560630));
            list2.add(7);
            list.add(LocaleController.getString("SaveToDownloads", 2131560628));
            list2.add(10);
            list.add(LocaleController.getString("ShareFile", 2131560748));
            list2.add(6);
        }
        else if (messageType == 7) {
            if (this.selectedObject.isMask()) {
                list.add(LocaleController.getString("AddToMasks", 2131558592));
            }
            else {
                list.add(LocaleController.getString("AddToStickers", 2131558593));
            }
            list2.add(9);
        }
        else if (messageType == 8) {
            final TLRPC.User user = MessagesController.getInstance(super.currentAccount).getUser(this.selectedObject.messageOwner.media.user_id);
            if (user != null && user.id != UserConfig.getInstance(super.currentAccount).getClientUserId() && ContactsController.getInstance(super.currentAccount).contactsDict.get(user.id) == null) {
                list.add(LocaleController.getString("AddContactTitle", 2131558569));
                list2.add(15);
            }
            final String phone_number = this.selectedObject.messageOwner.media.phone_number;
            if (phone_number != null || phone_number.length() != 0) {
                list.add(LocaleController.getString("Copy", 2131559163));
                list2.add(16);
                list.add(LocaleController.getString("Call", 2131558869));
                list2.add(17);
            }
        }
        if (list2.isEmpty()) {
            return;
        }
        builder.setItems(list.toArray(new CharSequence[0]), (DialogInterface$OnClickListener)new _$$Lambda$ChannelAdminLogActivity$BZ9YJjVsQ77SD_mlnHBXJsrpGqE(this, list2));
        builder.setTitle(LocaleController.getString("Message", 2131559845));
        this.showDialog(builder.create());
    }
    
    private TextureView createTextureView(final boolean b) {
        if (super.parentLayout == null) {
            return null;
        }
        if (this.roundVideoContainer == null) {
            if (Build$VERSION.SDK_INT >= 21) {
                (this.roundVideoContainer = new FrameLayout(this.getParentActivity()) {
                    public void setTranslationY(final float translationY) {
                        super.setTranslationY(translationY);
                        ChannelAdminLogActivity.this.contentView.invalidate();
                    }
                }).setOutlineProvider((ViewOutlineProvider)new ViewOutlineProvider() {
                    @TargetApi(21)
                    public void getOutline(final View view, final Outline outline) {
                        final int roundMessageSize = AndroidUtilities.roundMessageSize;
                        outline.setOval(0, 0, roundMessageSize, roundMessageSize);
                    }
                });
                this.roundVideoContainer.setClipToOutline(true);
            }
            else {
                this.roundVideoContainer = new FrameLayout(this.getParentActivity()) {
                    protected void dispatchDraw(final Canvas canvas) {
                        super.dispatchDraw(canvas);
                        canvas.drawPath(ChannelAdminLogActivity.this.aspectPath, ChannelAdminLogActivity.this.aspectPaint);
                    }
                    
                    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
                        super.onSizeChanged(n, n2, n3, n4);
                        ChannelAdminLogActivity.this.aspectPath.reset();
                        final Path access$3600 = ChannelAdminLogActivity.this.aspectPath;
                        final float n5 = (float)(n / 2);
                        access$3600.addCircle(n5, (float)(n2 / 2), n5, Path$Direction.CW);
                        ChannelAdminLogActivity.this.aspectPath.toggleInverseFillType();
                    }
                    
                    public void setTranslationY(final float translationY) {
                        super.setTranslationY(translationY);
                        ChannelAdminLogActivity.this.contentView.invalidate();
                    }
                    
                    public void setVisibility(final int visibility) {
                        super.setVisibility(visibility);
                        if (visibility == 0) {
                            this.setLayerType(2, (Paint)null);
                        }
                    }
                };
                this.aspectPath = new Path();
                (this.aspectPaint = new Paint(1)).setColor(-16777216);
                this.aspectPaint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.CLEAR));
            }
            this.roundVideoContainer.setWillNotDraw(false);
            this.roundVideoContainer.setVisibility(4);
            (this.aspectRatioFrameLayout = new AspectRatioFrameLayout((Context)this.getParentActivity())).setBackgroundColor(0);
            if (b) {
                this.roundVideoContainer.addView((View)this.aspectRatioFrameLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            }
            (this.videoTextureView = new TextureView((Context)this.getParentActivity())).setOpaque(false);
            this.aspectRatioFrameLayout.addView((View)this.videoTextureView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        }
        if (this.roundVideoContainer.getParent() == null) {
            final SizeNotifierFrameLayout contentView = this.contentView;
            final FrameLayout roundVideoContainer = this.roundVideoContainer;
            final int roundMessageSize = AndroidUtilities.roundMessageSize;
            contentView.addView((View)roundVideoContainer, 1, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(roundMessageSize, roundMessageSize));
        }
        this.roundVideoContainer.setVisibility(4);
        this.aspectRatioFrameLayout.setDrawingReady(false);
        return this.videoTextureView;
    }
    
    private void destroyTextureView() {
        final FrameLayout roundVideoContainer = this.roundVideoContainer;
        if (roundVideoContainer != null) {
            if (roundVideoContainer.getParent() != null) {
                this.contentView.removeView((View)this.roundVideoContainer);
                this.aspectRatioFrameLayout.setDrawingReady(false);
                this.roundVideoContainer.setVisibility(4);
                if (Build$VERSION.SDK_INT < 21) {
                    this.roundVideoContainer.setLayerType(0, (Paint)null);
                }
            }
        }
    }
    
    private void fixLayout() {
        final ChatAvatarContainer avatarContainer = this.avatarContainer;
        if (avatarContainer != null) {
            avatarContainer.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
                public boolean onPreDraw() {
                    if (ChannelAdminLogActivity.this.avatarContainer != null) {
                        ChannelAdminLogActivity.this.avatarContainer.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
                    }
                    return true;
                }
            });
        }
    }
    
    private String getMessageContent(final MessageObject messageObject, final int n, final boolean b) {
        String str;
        final String s = str = "";
        if (b) {
            final int from_id = messageObject.messageOwner.from_id;
            str = s;
            if (n != from_id) {
                if (from_id > 0) {
                    final TLRPC.User user = MessagesController.getInstance(super.currentAccount).getUser(messageObject.messageOwner.from_id);
                    str = s;
                    if (user != null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(ContactsController.formatName(user.first_name, user.last_name));
                        sb.append(":\n");
                        str = sb.toString();
                    }
                }
                else {
                    str = s;
                    if (from_id < 0) {
                        final TLRPC.Chat chat = MessagesController.getInstance(super.currentAccount).getChat(-messageObject.messageOwner.from_id);
                        str = s;
                        if (chat != null) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append(chat.title);
                            sb2.append(":\n");
                            str = sb2.toString();
                        }
                    }
                }
            }
        }
        String s2;
        if (messageObject.type == 0 && messageObject.messageOwner.message != null) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(str);
            sb3.append(messageObject.messageOwner.message);
            s2 = sb3.toString();
        }
        else {
            final TLRPC.Message messageOwner = messageObject.messageOwner;
            if (messageOwner.media != null && messageOwner.message != null) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(str);
                sb4.append(messageObject.messageOwner.message);
                s2 = sb4.toString();
            }
            else {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append(str);
                sb5.append((Object)messageObject.messageText);
                s2 = sb5.toString();
            }
        }
        return s2;
    }
    
    private int getMessageType(final MessageObject messageObject) {
        if (messageObject == null) {
            return -1;
        }
        final int type = messageObject.type;
        if (type == 6) {
            return -1;
        }
        if (type != 10 && type != 11 && type != 16) {
            if (messageObject.isVoice()) {
                return 2;
            }
            if (messageObject.isSticker()) {
                final TLRPC.InputStickerSet inputStickerSet = messageObject.getInputStickerSet();
                if (inputStickerSet instanceof TLRPC.TL_inputStickerSetID) {
                    if (!DataQuery.getInstance(super.currentAccount).isStickerPackInstalled(inputStickerSet.id)) {
                        return 7;
                    }
                }
                else if (inputStickerSet instanceof TLRPC.TL_inputStickerSetShortName && !DataQuery.getInstance(super.currentAccount).isStickerPackInstalled(inputStickerSet.short_name)) {
                    return 7;
                }
            }
            else if ((!messageObject.isRoundVideo() || (messageObject.isRoundVideo() && BuildVars.DEBUG_VERSION)) && (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto || messageObject.getDocument() != null || messageObject.isMusic() || messageObject.isVideo())) {
                final int n = 0;
                final String attachPath = messageObject.messageOwner.attachPath;
                int n2 = n;
                if (attachPath != null) {
                    n2 = n;
                    if (attachPath.length() != 0) {
                        n2 = n;
                        if (new File(messageObject.messageOwner.attachPath).exists()) {
                            n2 = 1;
                        }
                    }
                }
                int n3;
                if ((n3 = n2) == 0) {
                    n3 = n2;
                    if (FileLoader.getPathToMessage(messageObject.messageOwner).exists()) {
                        n3 = 1;
                    }
                }
                if (n3 != 0) {
                    if (messageObject.getDocument() != null) {
                        final String mime_type = messageObject.getDocument().mime_type;
                        if (mime_type != null) {
                            if (messageObject.getDocumentName().toLowerCase().endsWith("attheme")) {
                                return 10;
                            }
                            if (mime_type.endsWith("/xml")) {
                                return 5;
                            }
                            if (mime_type.endsWith("/png") || mime_type.endsWith("/jpg") || mime_type.endsWith("/jpeg")) {
                                return 6;
                            }
                        }
                    }
                    return 4;
                }
            }
            else {
                if (messageObject.type == 12) {
                    return 8;
                }
                if (messageObject.isMediaEmpty()) {
                    return 3;
                }
            }
            return 2;
        }
        else {
            if (messageObject.getId() == 0) {
                return -1;
            }
            return 1;
        }
    }
    
    private void hideFloatingDateView(final boolean b) {
        if (this.floatingDateView.getTag() != null && !this.currentFloatingDateOnScreen && (!this.scrollingFloatingDate || this.currentFloatingTopIsNotMessage)) {
            this.floatingDateView.setTag((Object)null);
            if (b) {
                (this.floatingDateAnimation = new AnimatorSet()).setDuration(150L);
                this.floatingDateAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.floatingDateView, "alpha", new float[] { 0.0f }) });
                this.floatingDateAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        if (animator.equals(ChannelAdminLogActivity.this.floatingDateAnimation)) {
                            ChannelAdminLogActivity.this.floatingDateAnimation = null;
                        }
                    }
                });
                this.floatingDateAnimation.setStartDelay(500L);
                this.floatingDateAnimation.start();
            }
            else {
                final AnimatorSet floatingDateAnimation = this.floatingDateAnimation;
                if (floatingDateAnimation != null) {
                    floatingDateAnimation.cancel();
                    this.floatingDateAnimation = null;
                }
                this.floatingDateView.setAlpha(0.0f);
            }
        }
    }
    
    private void loadAdmins() {
        final TLRPC.TL_channels_getParticipants tl_channels_getParticipants = new TLRPC.TL_channels_getParticipants();
        tl_channels_getParticipants.channel = MessagesController.getInputChannel(this.currentChat);
        tl_channels_getParticipants.filter = new TLRPC.TL_channelParticipantsAdmins();
        tl_channels_getParticipants.offset = 0;
        tl_channels_getParticipants.limit = 200;
        ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_channels_getParticipants, new _$$Lambda$ChannelAdminLogActivity$v_o39r5RzWtyA17rINStweDzJAI(this)), super.classGuid);
    }
    
    private void loadMessages(final boolean b) {
        if (this.loading) {
            return;
        }
        int i = 0;
        if (b) {
            this.minEventId = Long.MAX_VALUE;
            final FrameLayout progressView = this.progressView;
            if (progressView != null) {
                progressView.setVisibility(0);
                this.emptyViewContainer.setVisibility(4);
                this.chatListView.setEmptyView(null);
            }
            this.messagesDict.clear();
            this.messages.clear();
            this.messagesByDays.clear();
        }
        this.loading = true;
        final TLRPC.TL_channels_getAdminLog tl_channels_getAdminLog = new TLRPC.TL_channels_getAdminLog();
        tl_channels_getAdminLog.channel = MessagesController.getInputChannel(this.currentChat);
        tl_channels_getAdminLog.q = this.searchQuery;
        tl_channels_getAdminLog.limit = 50;
        if (!b && !this.messages.isEmpty()) {
            tl_channels_getAdminLog.max_id = this.minEventId;
        }
        else {
            tl_channels_getAdminLog.max_id = 0L;
        }
        tl_channels_getAdminLog.min_id = 0L;
        final TLRPC.TL_channelAdminLogEventsFilter currentFilter = this.currentFilter;
        if (currentFilter != null) {
            tl_channels_getAdminLog.flags |= 0x1;
            tl_channels_getAdminLog.events_filter = currentFilter;
        }
        if (this.selectedAdmins != null) {
            tl_channels_getAdminLog.flags |= 0x2;
            while (i < this.selectedAdmins.size()) {
                tl_channels_getAdminLog.admins.add(MessagesController.getInstance(super.currentAccount).getInputUser((TLRPC.User)this.selectedAdmins.valueAt(i)));
                ++i;
            }
        }
        this.updateEmptyPlaceholder();
        ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_channels_getAdminLog, new _$$Lambda$ChannelAdminLogActivity$eETtXTeGU9y5fKwho2AV8a_Izoc(this));
        if (b) {
            final ChatActivityAdapter chatAdapter = this.chatAdapter;
            if (chatAdapter != null) {
                chatAdapter.notifyDataSetChanged();
            }
        }
    }
    
    private void moveScrollToLastMessage() {
        if (this.chatListView != null && !this.messages.isEmpty()) {
            this.chatLayoutManager.scrollToPositionWithOffset(this.messages.size() - 1, -100000 - this.chatListView.getPaddingTop());
        }
    }
    
    private void processSelectedOption(int type) {
        final MessageObject selectedObject = this.selectedObject;
        if (selectedObject == null) {
            return;
        }
        final int n = 3;
        final int n2 = 0;
        switch (type) {
            case 17: {
                try {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("tel:");
                    sb.append(this.selectedObject.messageOwner.media.phone_number);
                    final Intent intent = new Intent("android.intent.action.DIAL", Uri.parse(sb.toString()));
                    intent.addFlags(268435456);
                    this.getParentActivity().startActivityForResult(intent, 500);
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                break;
            }
            case 16: {
                AndroidUtilities.addToClipboard(selectedObject.messageOwner.media.phone_number);
                break;
            }
            case 15: {
                final Bundle bundle = new Bundle();
                bundle.putInt("user_id", this.selectedObject.messageOwner.media.user_id);
                bundle.putString("phone", this.selectedObject.messageOwner.media.phone_number);
                bundle.putBoolean("addContact", true);
                this.presentFragment(new ContactAddActivity(bundle));
                break;
            }
            case 11: {
                MessagesController.getInstance(super.currentAccount).saveGif(this.selectedObject, selectedObject.getDocument());
                break;
            }
            case 10: {
                if (Build$VERSION.SDK_INT >= 23 && this.getParentActivity().checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                    this.getParentActivity().requestPermissions(new String[] { "android.permission.WRITE_EXTERNAL_STORAGE" }, 4);
                    this.selectedObject = null;
                    return;
                }
                String s;
                if (TextUtils.isEmpty((CharSequence)(s = FileLoader.getDocumentFileName(this.selectedObject.getDocument())))) {
                    s = this.selectedObject.getFileName();
                }
                final String attachPath = this.selectedObject.messageOwner.attachPath;
                String s2;
                if ((s2 = attachPath) != null) {
                    s2 = attachPath;
                    if (attachPath.length() > 0) {
                        s2 = attachPath;
                        if (!new File(attachPath).exists()) {
                            s2 = null;
                        }
                    }
                }
                String string = null;
                Label_0447: {
                    if (s2 != null) {
                        string = s2;
                        if (s2.length() != 0) {
                            break Label_0447;
                        }
                    }
                    string = FileLoader.getPathToMessage(this.selectedObject.messageOwner).toString();
                }
                final Activity parentActivity = this.getParentActivity();
                if (this.selectedObject.isMusic()) {
                    type = n;
                }
                else {
                    type = 2;
                }
                String mime_type;
                if (this.selectedObject.getDocument() != null) {
                    mime_type = this.selectedObject.getDocument().mime_type;
                }
                else {
                    mime_type = "";
                }
                MediaController.saveFile(string, (Context)parentActivity, type, s, mime_type);
                break;
            }
            case 9: {
                this.showDialog(new StickersAlert((Context)this.getParentActivity(), this, this.selectedObject.getInputStickerSet(), null, null));
                break;
            }
            case 7: {
                String attachPath2;
                final String pathname = attachPath2 = selectedObject.messageOwner.attachPath;
                if (pathname != null) {
                    attachPath2 = pathname;
                    if (pathname.length() > 0) {
                        attachPath2 = pathname;
                        if (!new File(pathname).exists()) {
                            attachPath2 = null;
                        }
                    }
                }
                String string2 = null;
                Label_0617: {
                    if (attachPath2 != null) {
                        string2 = attachPath2;
                        if (attachPath2.length() != 0) {
                            break Label_0617;
                        }
                    }
                    string2 = FileLoader.getPathToMessage(this.selectedObject.messageOwner).toString();
                }
                if (Build$VERSION.SDK_INT >= 23 && this.getParentActivity().checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                    this.getParentActivity().requestPermissions(new String[] { "android.permission.WRITE_EXTERNAL_STORAGE" }, 4);
                    this.selectedObject = null;
                    return;
                }
                MediaController.saveFile(string2, (Context)this.getParentActivity(), 0, null, null);
                break;
            }
            case 6: {
                String attachPath3;
                final String pathname2 = attachPath3 = selectedObject.messageOwner.attachPath;
                if (pathname2 != null) {
                    attachPath3 = pathname2;
                    if (pathname2.length() > 0) {
                        attachPath3 = pathname2;
                        if (!new File(pathname2).exists()) {
                            attachPath3 = null;
                        }
                    }
                }
                String string3 = null;
                Label_0754: {
                    if (attachPath3 != null) {
                        string3 = attachPath3;
                        if (attachPath3.length() != 0) {
                            break Label_0754;
                        }
                    }
                    string3 = FileLoader.getPathToMessage(this.selectedObject.messageOwner).toString();
                }
                final Intent intent2 = new Intent("android.intent.action.SEND");
                intent2.setType(this.selectedObject.getDocument().mime_type);
                if (Build$VERSION.SDK_INT >= 24) {
                    try {
                        intent2.putExtra("android.intent.extra.STREAM", (Parcelable)FileProvider.getUriForFile((Context)this.getParentActivity(), "org.telegram.messenger.provider", new File(string3)));
                        intent2.setFlags(1);
                    }
                    catch (Exception ex2) {
                        intent2.putExtra("android.intent.extra.STREAM", (Parcelable)Uri.fromFile(new File(string3)));
                    }
                }
                else {
                    intent2.putExtra("android.intent.extra.STREAM", (Parcelable)Uri.fromFile(new File(string3)));
                }
                this.getParentActivity().startActivityForResult(Intent.createChooser(intent2, (CharSequence)LocaleController.getString("ShareFile", 2131560748)), 500);
                break;
            }
            case 5: {
                final String attachPath4 = selectedObject.messageOwner.attachPath;
                File file = null;
                Label_0953: {
                    if (attachPath4 != null && attachPath4.length() != 0) {
                        file = new File(this.selectedObject.messageOwner.attachPath);
                        if (file.exists()) {
                            break Label_0953;
                        }
                    }
                    file = null;
                }
                File file2 = file;
                if (file == null) {
                    final File pathToMessage = FileLoader.getPathToMessage(this.selectedObject.messageOwner);
                    file2 = file;
                    if (pathToMessage.exists()) {
                        file2 = pathToMessage;
                    }
                }
                if (file2 == null) {
                    break;
                }
                if (file2.getName().toLowerCase().endsWith("attheme")) {
                    final LinearLayoutManager chatLayoutManager = this.chatLayoutManager;
                    if (chatLayoutManager != null) {
                        if (chatLayoutManager.findLastVisibleItemPosition() < ((RecyclerView.LayoutManager)this.chatLayoutManager).getItemCount() - 1) {
                            this.scrollToPositionOnRecreate = this.chatLayoutManager.findFirstVisibleItemPosition();
                            final RecyclerListView.Holder holder = (RecyclerListView.Holder)this.chatListView.findViewHolderForAdapterPosition(this.scrollToPositionOnRecreate);
                            if (holder != null) {
                                this.scrollToOffsetOnRecreate = holder.itemView.getTop();
                            }
                            else {
                                this.scrollToPositionOnRecreate = -1;
                            }
                        }
                        else {
                            this.scrollToPositionOnRecreate = -1;
                        }
                    }
                    final Theme.ThemeInfo applyThemeFile = Theme.applyThemeFile(file2, this.selectedObject.getDocumentName(), true);
                    if (applyThemeFile != null) {
                        this.presentFragment(new ThemePreviewActivity(file2, applyThemeFile));
                        break;
                    }
                    this.scrollToPositionOnRecreate = -1;
                    if (this.getParentActivity() == null) {
                        this.selectedObject = null;
                        return;
                    }
                    final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
                    builder.setTitle(LocaleController.getString("AppName", 2131558635));
                    builder.setMessage(LocaleController.getString("IncorrectTheme", 2131559664));
                    builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                    this.showDialog(builder.create());
                    break;
                }
                else {
                    if (LocaleController.getInstance().applyLanguageFile(file2, super.currentAccount)) {
                        this.presentFragment(new LanguageSelectActivity());
                        break;
                    }
                    if (this.getParentActivity() == null) {
                        this.selectedObject = null;
                        return;
                    }
                    final AlertDialog.Builder builder2 = new AlertDialog.Builder((Context)this.getParentActivity());
                    builder2.setTitle(LocaleController.getString("AppName", 2131558635));
                    builder2.setMessage(LocaleController.getString("IncorrectLocalization", 2131559663));
                    builder2.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                    this.showDialog(builder2.create());
                    break;
                }
                break;
            }
            case 4: {
                String attachPath5;
                final String pathname3 = attachPath5 = selectedObject.messageOwner.attachPath;
                if (pathname3 != null) {
                    attachPath5 = pathname3;
                    if (pathname3.length() > 0) {
                        attachPath5 = pathname3;
                        if (!new File(pathname3).exists()) {
                            attachPath5 = null;
                        }
                    }
                }
                String string4 = null;
                Label_1399: {
                    if (attachPath5 != null) {
                        string4 = attachPath5;
                        if (attachPath5.length() != 0) {
                            break Label_1399;
                        }
                    }
                    string4 = FileLoader.getPathToMessage(this.selectedObject.messageOwner).toString();
                }
                type = this.selectedObject.type;
                if (type != 3 && type != 1) {
                    break;
                }
                if (Build$VERSION.SDK_INT >= 23 && this.getParentActivity().checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                    this.getParentActivity().requestPermissions(new String[] { "android.permission.WRITE_EXTERNAL_STORAGE" }, 4);
                    this.selectedObject = null;
                    return;
                }
                final Activity parentActivity2 = this.getParentActivity();
                type = n2;
                if (this.selectedObject.type == 3) {
                    type = 1;
                }
                MediaController.saveFile(string4, (Context)parentActivity2, type, null, null);
                break;
            }
            case 3: {
                AndroidUtilities.addToClipboard(this.getMessageContent(selectedObject, 0, true));
                break;
            }
        }
        this.selectedObject = null;
    }
    
    private void removeMessageObject(final MessageObject o) {
        final int index = this.messages.indexOf(o);
        if (index == -1) {
            return;
        }
        this.messages.remove(index);
        final ChatActivityAdapter chatAdapter = this.chatAdapter;
        if (chatAdapter != null) {
            chatAdapter.notifyItemRemoved(chatAdapter.messagesStartRow + this.messages.size() - index - 1);
        }
    }
    
    private void updateBottomOverlay() {
    }
    
    private void updateEmptyPlaceholder() {
        if (this.emptyView == null) {
            return;
        }
        if (!TextUtils.isEmpty((CharSequence)this.searchQuery)) {
            this.emptyView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(5.0f));
            this.emptyView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("EventLogEmptyTextSearch", 2131559409, this.searchQuery)));
        }
        else if (this.selectedAdmins == null && this.currentFilter == null) {
            this.emptyView.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
            if (this.currentChat.megagroup) {
                this.emptyView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.getString("EventLogEmpty", 2131559406)));
            }
            else {
                this.emptyView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.getString("EventLogEmptyChannel", 2131559407)));
            }
        }
        else {
            this.emptyView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(5.0f));
            this.emptyView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.getString("EventLogEmptySearch", 2131559408)));
        }
    }
    
    private void updateMessagesVisisblePart() {
        final RecyclerListView chatListView = this.chatListView;
        if (chatListView == null) {
            return;
        }
        final int childCount = chatListView.getChildCount();
        final int measuredHeight = this.chatListView.getMeasuredHeight();
        int i = 0;
        int n = 0;
        Object o = null;
        View view = null;
        View view2 = null;
        int n2 = Integer.MAX_VALUE;
        int n3 = Integer.MAX_VALUE;
        while (i < childCount) {
            final View child = this.chatListView.getChildAt(i);
            final boolean b = child instanceof ChatMessageCell;
            int n4 = n;
            if (b) {
                final ChatMessageCell chatMessageCell = (ChatMessageCell)child;
                final int top = chatMessageCell.getTop();
                chatMessageCell.getBottom();
                int n5;
                if (top >= 0) {
                    n5 = 0;
                }
                else {
                    n5 = -top;
                }
                int measuredHeight2;
                if ((measuredHeight2 = chatMessageCell.getMeasuredHeight()) > measuredHeight) {
                    measuredHeight2 = n5 + measuredHeight;
                }
                chatMessageCell.setVisiblePart(n5, measuredHeight2 - n5);
                final MessageObject messageObject = chatMessageCell.getMessageObject();
                n4 = n;
                if (this.roundVideoContainer != null) {
                    n4 = n;
                    if (messageObject.isRoundVideo()) {
                        n4 = n;
                        if (MediaController.getInstance().isPlayingMessage(messageObject)) {
                            final ImageReceiver photoImage = chatMessageCell.getPhotoImage();
                            this.roundVideoContainer.setTranslationX((float)photoImage.getImageX());
                            this.roundVideoContainer.setTranslationY((float)(super.fragmentView.getPaddingTop() + top + photoImage.getImageY()));
                            super.fragmentView.invalidate();
                            this.roundVideoContainer.invalidate();
                            n4 = 1;
                        }
                    }
                }
            }
            View view3;
            View view4;
            int n6;
            if (child.getBottom() <= this.chatListView.getPaddingTop()) {
                view3 = view;
                view4 = view2;
                n6 = n3;
            }
            else {
                final int bottom = child.getBottom();
                Object o2 = o;
                int n7;
                if (bottom < (n7 = n2)) {
                    if (b || child instanceof ChatActionCell) {
                        o = child;
                    }
                    n7 = bottom;
                    view2 = child;
                    o2 = o;
                }
                o = o2;
                view3 = view;
                view4 = view2;
                n2 = n7;
                n6 = n3;
                if (child instanceof ChatActionCell) {
                    o = o2;
                    view3 = view;
                    view4 = view2;
                    n2 = n7;
                    n6 = n3;
                    if (((ChatActionCell)child).getMessageObject().isDateObject) {
                        if (child.getAlpha() != 1.0f) {
                            child.setAlpha(1.0f);
                        }
                        o = o2;
                        view3 = view;
                        view4 = view2;
                        n2 = n7;
                        if (bottom < (n6 = n3)) {
                            n6 = bottom;
                            n2 = n7;
                            view4 = view2;
                            view3 = child;
                            o = o2;
                        }
                    }
                }
            }
            ++i;
            n = n4;
            view = view3;
            view2 = view4;
            n3 = n6;
        }
        final FrameLayout roundVideoContainer = this.roundVideoContainer;
        if (roundVideoContainer != null) {
            if (n == 0) {
                roundVideoContainer.setTranslationY((float)(-AndroidUtilities.roundMessageSize - 100));
                super.fragmentView.invalidate();
                final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                if (playingMessageObject != null && playingMessageObject.isRoundVideo() && this.checkTextureViewPosition) {
                    MediaController.getInstance().setCurrentVideoVisible(false);
                }
            }
            else {
                MediaController.getInstance().setCurrentVideoVisible(true);
            }
        }
        if (o != null) {
            MessageObject messageObject2;
            if (o instanceof ChatMessageCell) {
                messageObject2 = ((ChatMessageCell)o).getMessageObject();
            }
            else {
                messageObject2 = ((ChatActionCell)o).getMessageObject();
            }
            this.floatingDateView.setCustomDate(messageObject2.messageOwner.date);
        }
        final boolean b2 = false;
        this.currentFloatingDateOnScreen = false;
        boolean currentFloatingTopIsNotMessage = b2;
        if (!(view2 instanceof ChatMessageCell)) {
            currentFloatingTopIsNotMessage = b2;
            if (!(view2 instanceof ChatActionCell)) {
                currentFloatingTopIsNotMessage = true;
            }
        }
        this.currentFloatingTopIsNotMessage = currentFloatingTopIsNotMessage;
        if (view != null) {
            if (view.getTop() <= this.chatListView.getPaddingTop() && !this.currentFloatingTopIsNotMessage) {
                if (view.getAlpha() != 0.0f) {
                    view.setAlpha(0.0f);
                }
                final AnimatorSet floatingDateAnimation = this.floatingDateAnimation;
                if (floatingDateAnimation != null) {
                    floatingDateAnimation.cancel();
                    this.floatingDateAnimation = null;
                }
                if (this.floatingDateView.getTag() == null) {
                    this.floatingDateView.setTag((Object)1);
                }
                if (this.floatingDateView.getAlpha() != 1.0f) {
                    this.floatingDateView.setAlpha(1.0f);
                }
                this.currentFloatingDateOnScreen = true;
            }
            else {
                if (view.getAlpha() != 1.0f) {
                    view.setAlpha(1.0f);
                }
                this.hideFloatingDateView(this.currentFloatingTopIsNotMessage ^ true);
            }
            final int n8 = view.getBottom() - this.chatListView.getPaddingTop();
            if (n8 > this.floatingDateView.getMeasuredHeight() && n8 < this.floatingDateView.getMeasuredHeight() * 2) {
                final ChatActionCell floatingDateView = this.floatingDateView;
                floatingDateView.setTranslationY((float)(-floatingDateView.getMeasuredHeight() * 2 + n8));
            }
            else {
                this.floatingDateView.setTranslationY(0.0f);
            }
        }
        else {
            this.hideFloatingDateView(true);
            this.floatingDateView.setTranslationY(0.0f);
        }
    }
    
    private void updateTextureViewPosition() {
        while (true) {
            for (int childCount = this.chatListView.getChildCount(), i = 0; i < childCount; ++i) {
                final View child = this.chatListView.getChildAt(i);
                if (child instanceof ChatMessageCell) {
                    final ChatMessageCell chatMessageCell = (ChatMessageCell)child;
                    final MessageObject messageObject = chatMessageCell.getMessageObject();
                    if (this.roundVideoContainer != null && messageObject.isRoundVideo() && MediaController.getInstance().isPlayingMessage(messageObject)) {
                        final ImageReceiver photoImage = chatMessageCell.getPhotoImage();
                        this.roundVideoContainer.setTranslationX((float)photoImage.getImageX());
                        this.roundVideoContainer.setTranslationY((float)(super.fragmentView.getPaddingTop() + chatMessageCell.getTop() + photoImage.getImageY()));
                        super.fragmentView.invalidate();
                        this.roundVideoContainer.invalidate();
                        final boolean b = true;
                        if (this.roundVideoContainer != null) {
                            final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                            if (!b) {
                                this.roundVideoContainer.setTranslationY((float)(-AndroidUtilities.roundMessageSize - 100));
                                super.fragmentView.invalidate();
                                if (playingMessageObject != null && playingMessageObject.isRoundVideo() && (this.checkTextureViewPosition || PipRoundVideoView.getInstance() != null)) {
                                    MediaController.getInstance().setCurrentVideoVisible(false);
                                }
                            }
                            else {
                                MediaController.getInstance().setCurrentVideoVisible(true);
                            }
                        }
                        return;
                    }
                }
            }
            final boolean b = false;
            continue;
        }
    }
    
    @Override
    public View createView(final Context context) {
        if (this.chatMessageCellsCache.isEmpty()) {
            for (int i = 0; i < 8; ++i) {
                this.chatMessageCellsCache.add(new ChatMessageCell(context));
            }
        }
        this.searchWas = false;
        super.hasOwnBackground = true;
        Theme.createChatResources(context, false);
        super.actionBar.setAddToContainer(false);
        super.actionBar.setOccupyStatusBar(Build$VERSION.SDK_INT >= 21 && !AndroidUtilities.isTablet());
        super.actionBar.setBackButtonDrawable(new BackDrawable(false));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    ChannelAdminLogActivity.this.finishFragment();
                }
            }
        });
        (this.avatarContainer = new ChatAvatarContainer(context, null, false)).setOccupyStatusBar(AndroidUtilities.isTablet() ^ true);
        super.actionBar.addView((View)this.avatarContainer, 0, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1.0f, 51, 56.0f, 0.0f, 40.0f, 0.0f));
        (this.searchItem = super.actionBar.createMenu().addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener((ActionBarMenuItem.ActionBarMenuItemSearchListener)new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
            @Override
            public void onSearchCollapse() {
                ChannelAdminLogActivity.this.searchQuery = "";
                ChannelAdminLogActivity.this.avatarContainer.setVisibility(0);
                if (ChannelAdminLogActivity.this.searchWas) {
                    ChannelAdminLogActivity.this.searchWas = false;
                    ChannelAdminLogActivity.this.loadMessages(true);
                }
                ChannelAdminLogActivity.this.updateBottomOverlay();
            }
            
            @Override
            public void onSearchExpand() {
                ChannelAdminLogActivity.this.avatarContainer.setVisibility(8);
                ChannelAdminLogActivity.this.updateBottomOverlay();
            }
            
            @Override
            public void onSearchPressed(final EditText editText) {
                ChannelAdminLogActivity.this.searchWas = true;
                ChannelAdminLogActivity.this.searchQuery = editText.getText().toString();
                ChannelAdminLogActivity.this.loadMessages(true);
            }
        })).setSearchFieldHint(LocaleController.getString("Search", 2131560640));
        this.avatarContainer.setEnabled(false);
        this.avatarContainer.setTitle(this.currentChat.title);
        this.avatarContainer.setSubtitle(LocaleController.getString("EventLogAllEvents", 2131559385));
        this.avatarContainer.setChatAvatar(this.currentChat);
        super.fragmentView = (View)new SizeNotifierFrameLayout(context) {
            protected boolean drawChild(final Canvas canvas, final View view, final long n) {
                final boolean drawChild = super.drawChild(canvas, view, n);
                if (view == ChannelAdminLogActivity.this.actionBar && ChannelAdminLogActivity.this.parentLayout != null) {
                    final ActionBarLayout access$1300 = ChannelAdminLogActivity.this.parentLayout;
                    int measuredHeight;
                    if (ChannelAdminLogActivity.this.actionBar.getVisibility() == 0) {
                        measuredHeight = ChannelAdminLogActivity.this.actionBar.getMeasuredHeight();
                    }
                    else {
                        measuredHeight = 0;
                    }
                    access$1300.drawHeaderShadow(canvas, measuredHeight);
                }
                return drawChild;
            }
            
            @Override
            protected boolean isActionBarVisible() {
                return ChannelAdminLogActivity.this.actionBar.getVisibility() == 0;
            }
            
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                if (playingMessageObject != null && playingMessageObject.isRoundVideo() && playingMessageObject.eventId != 0L && playingMessageObject.getDialogId() == -ChannelAdminLogActivity.this.currentChat.id) {
                    MediaController.getInstance().setTextureView(ChannelAdminLogActivity.this.createTextureView(false), ChannelAdminLogActivity.this.aspectRatioFrameLayout, ChannelAdminLogActivity.this.roundVideoContainer, true);
                }
            }
            
            @Override
            protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                    final View child = this.getChildAt(i);
                    if (child.getVisibility() != 8) {
                        final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)child.getLayoutParams();
                        final int measuredWidth = child.getMeasuredWidth();
                        final int measuredHeight = child.getMeasuredHeight();
                        int gravity;
                        if ((gravity = frameLayout$LayoutParams.gravity) == -1) {
                            gravity = 51;
                        }
                        final int n5 = gravity & 0x70;
                        final int n6 = gravity & 0x7 & 0x7;
                        int leftMargin = 0;
                        Label_0169: {
                            int n7;
                            int n8;
                            if (n6 != 1) {
                                if (n6 != 5) {
                                    leftMargin = frameLayout$LayoutParams.leftMargin;
                                    break Label_0169;
                                }
                                n7 = n3 - measuredWidth;
                                n8 = frameLayout$LayoutParams.rightMargin;
                            }
                            else {
                                n7 = (n3 - n - measuredWidth) / 2 + frameLayout$LayoutParams.leftMargin;
                                n8 = frameLayout$LayoutParams.rightMargin;
                            }
                            leftMargin = n7 - n8;
                        }
                        int topMargin = 0;
                        Label_0313: {
                            int n9;
                            int n10;
                            if (n5 != 16) {
                                if (n5 != 48) {
                                    if (n5 != 80) {
                                        topMargin = frameLayout$LayoutParams.topMargin;
                                        break Label_0313;
                                    }
                                    n9 = n4 - n2 - measuredHeight;
                                    n10 = frameLayout$LayoutParams.bottomMargin;
                                }
                                else {
                                    final int n11 = topMargin = frameLayout$LayoutParams.topMargin + this.getPaddingTop();
                                    if (child == ChannelAdminLogActivity.this.actionBar) {
                                        break Label_0313;
                                    }
                                    topMargin = n11;
                                    if (ChannelAdminLogActivity.this.actionBar.getVisibility() == 0) {
                                        topMargin = n11 + ChannelAdminLogActivity.this.actionBar.getMeasuredHeight();
                                    }
                                    break Label_0313;
                                }
                            }
                            else {
                                n9 = (n4 - n2 - measuredHeight) / 2 + frameLayout$LayoutParams.topMargin;
                                n10 = frameLayout$LayoutParams.bottomMargin;
                            }
                            topMargin = n9 - n10;
                        }
                        int n13 = 0;
                        Label_0404: {
                            int paddingTop;
                            if (child == ChannelAdminLogActivity.this.emptyViewContainer) {
                                final int dp = AndroidUtilities.dp(24.0f);
                                int n12;
                                if (ChannelAdminLogActivity.this.actionBar.getVisibility() == 0) {
                                    n12 = ChannelAdminLogActivity.this.actionBar.getMeasuredHeight() / 2;
                                }
                                else {
                                    n12 = 0;
                                }
                                paddingTop = dp - n12;
                            }
                            else {
                                n13 = topMargin;
                                if (child != ChannelAdminLogActivity.this.actionBar) {
                                    break Label_0404;
                                }
                                paddingTop = this.getPaddingTop();
                            }
                            n13 = topMargin - paddingTop;
                        }
                        child.layout(leftMargin, n13, measuredWidth + leftMargin, measuredHeight + n13);
                    }
                }
                ChannelAdminLogActivity.this.updateMessagesVisisblePart();
                this.notifyHeightChanged();
            }
            
            protected void onMeasure(final int n, final int n2) {
                final int size = View$MeasureSpec.getSize(n);
                final int size2 = View$MeasureSpec.getSize(n2);
                this.setMeasuredDimension(size, size2);
                final int n3 = size2 - this.getPaddingTop();
                this.measureChildWithMargins((View)ChannelAdminLogActivity.this.actionBar, n, 0, n2, 0);
                final int measuredHeight = ChannelAdminLogActivity.this.actionBar.getMeasuredHeight();
                int n4 = n3;
                if (ChannelAdminLogActivity.this.actionBar.getVisibility() == 0) {
                    n4 = n3 - measuredHeight;
                }
                this.getKeyboardHeight();
                for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                    final View child = this.getChildAt(i);
                    if (child != null && child.getVisibility() != 8) {
                        if (child != ChannelAdminLogActivity.this.actionBar) {
                            if (child != ChannelAdminLogActivity.this.chatListView && child != ChannelAdminLogActivity.this.progressView) {
                                if (child == ChannelAdminLogActivity.this.emptyViewContainer) {
                                    child.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(n4, 1073741824));
                                }
                                else {
                                    this.measureChildWithMargins(child, n, 0, n2, 0);
                                }
                            }
                            else {
                                child.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(Math.max(AndroidUtilities.dp(10.0f), n4 - AndroidUtilities.dp(50.0f)), 1073741824));
                            }
                        }
                    }
                }
            }
        };
        (this.contentView = (SizeNotifierFrameLayout)super.fragmentView).setOccupyStatusBar(AndroidUtilities.isTablet() ^ true);
        this.contentView.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());
        (this.emptyViewContainer = new FrameLayout(context)).setVisibility(4);
        this.contentView.addView((View)this.emptyViewContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2, 17));
        this.emptyViewContainer.setOnTouchListener((View$OnTouchListener)_$$Lambda$ChannelAdminLogActivity$Xh6zBvXYfqVbautwsQ1g7PDF8js.INSTANCE);
        (this.emptyView = new TextView(context)).setTextSize(1, 14.0f);
        this.emptyView.setGravity(17);
        this.emptyView.setTextColor(Theme.getColor("chat_serviceText"));
        this.emptyView.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(10.0f), Theme.getServiceMessageColor()));
        this.emptyView.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
        this.emptyViewContainer.addView((View)this.emptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 17, 16.0f, 0.0f, 16.0f, 0.0f));
        (this.chatListView = new RecyclerListView(context) {
            @Override
            public boolean drawChild(final Canvas canvas, final View view, final long n) {
                final boolean drawChild = super.drawChild(canvas, view, n);
                if (view instanceof ChatMessageCell) {
                    final ChatMessageCell chatMessageCell = (ChatMessageCell)view;
                    final ImageReceiver avatarImage = chatMessageCell.getAvatarImage();
                    if (avatarImage != null) {
                        final int top = view.getTop();
                        if (chatMessageCell.isPinnedBottom()) {
                            final ViewHolder childViewHolder = ChannelAdminLogActivity.this.chatListView.getChildViewHolder(view);
                            if (childViewHolder != null && ChannelAdminLogActivity.this.chatListView.findViewHolderForAdapterPosition(childViewHolder.getAdapterPosition() + 1) != null) {
                                avatarImage.setImageY(-AndroidUtilities.dp(1000.0f));
                                avatarImage.draw(canvas);
                                return drawChild;
                            }
                        }
                        int n2 = top;
                        Label_0217: {
                            if (chatMessageCell.isPinnedTop()) {
                                ViewHolder viewHolder = ChannelAdminLogActivity.this.chatListView.getChildViewHolder(view);
                                n2 = top;
                                if (viewHolder != null) {
                                    n2 = top;
                                    View itemView;
                                    int top2;
                                    do {
                                        viewHolder = ChannelAdminLogActivity.this.chatListView.findViewHolderForAdapterPosition(viewHolder.getAdapterPosition() - 1);
                                        if (viewHolder == null) {
                                            break Label_0217;
                                        }
                                        top2 = viewHolder.itemView.getTop();
                                        itemView = viewHolder.itemView;
                                        n2 = top2;
                                        if (!(itemView instanceof ChatMessageCell)) {
                                            break Label_0217;
                                        }
                                        n2 = top2;
                                    } while (((ChatMessageCell)itemView).isPinnedTop());
                                    n2 = top2;
                                }
                            }
                        }
                        final int n3 = view.getTop() + chatMessageCell.getLayoutHeight();
                        final int n4 = ChannelAdminLogActivity.this.chatListView.getHeight() - ChannelAdminLogActivity.this.chatListView.getPaddingBottom();
                        int n5;
                        if ((n5 = n3) > n4) {
                            n5 = n4;
                        }
                        int n6 = n5;
                        if (n5 - AndroidUtilities.dp(48.0f) < n2) {
                            n6 = AndroidUtilities.dp(48.0f) + n2;
                        }
                        avatarImage.setImageY(n6 - AndroidUtilities.dp(44.0f));
                        avatarImage.draw(canvas);
                    }
                }
                return drawChild;
            }
        }).setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$ChannelAdminLogActivity$Th_a2zOFfCYw_f0pzM6kJHBXd6E(this));
        this.chatListView.setTag((Object)1);
        this.chatListView.setVerticalScrollBarEnabled(true);
        this.chatListView.setAdapter(this.chatAdapter = new ChatActivityAdapter(context));
        this.chatListView.setClipToPadding(false);
        this.chatListView.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(3.0f));
        this.chatListView.setItemAnimator(null);
        this.chatListView.setLayoutAnimation((LayoutAnimationController)null);
        (this.chatLayoutManager = new LinearLayoutManager(context) {
            @Override
            public void smoothScrollToPosition(final RecyclerView recyclerView, final State state, final int targetPosition) {
                final LinearSmoothScrollerMiddle linearSmoothScrollerMiddle = new LinearSmoothScrollerMiddle(recyclerView.getContext());
                ((RecyclerView.SmoothScroller)linearSmoothScrollerMiddle).setTargetPosition(targetPosition);
                ((RecyclerView.LayoutManager)this).startSmoothScroll(linearSmoothScrollerMiddle);
            }
            
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        }).setOrientation(1);
        this.chatLayoutManager.setStackFromEnd(true);
        this.chatListView.setLayoutManager((RecyclerView.LayoutManager)this.chatLayoutManager);
        this.contentView.addView((View)this.chatListView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.chatListView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private final int scrollValue = AndroidUtilities.dp(100.0f);
            private float totalDy = 0.0f;
            
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                if (n == 1) {
                    ChannelAdminLogActivity.this.scrollingFloatingDate = true;
                    ChannelAdminLogActivity.this.checkTextureViewPosition = true;
                }
                else if (n == 0) {
                    ChannelAdminLogActivity.this.scrollingFloatingDate = false;
                    ChannelAdminLogActivity.this.checkTextureViewPosition = false;
                    ChannelAdminLogActivity.this.hideFloatingDateView(true);
                }
            }
            
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                ChannelAdminLogActivity.this.chatListView.invalidate();
                if (n2 != 0 && ChannelAdminLogActivity.this.scrollingFloatingDate && !ChannelAdminLogActivity.this.currentFloatingTopIsNotMessage && ChannelAdminLogActivity.this.floatingDateView.getTag() == null) {
                    if (ChannelAdminLogActivity.this.floatingDateAnimation != null) {
                        ChannelAdminLogActivity.this.floatingDateAnimation.cancel();
                    }
                    ChannelAdminLogActivity.this.floatingDateView.setTag((Object)1);
                    ChannelAdminLogActivity.this.floatingDateAnimation = new AnimatorSet();
                    ChannelAdminLogActivity.this.floatingDateAnimation.setDuration(150L);
                    ChannelAdminLogActivity.this.floatingDateAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)ChannelAdminLogActivity.this.floatingDateView, "alpha", new float[] { 1.0f }) });
                    ChannelAdminLogActivity.this.floatingDateAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            if (animator.equals(ChannelAdminLogActivity.this.floatingDateAnimation)) {
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
        final int scrollToPositionOnRecreate = this.scrollToPositionOnRecreate;
        if (scrollToPositionOnRecreate != -1) {
            this.chatLayoutManager.scrollToPositionWithOffset(scrollToPositionOnRecreate, this.scrollToOffsetOnRecreate);
            this.scrollToPositionOnRecreate = -1;
        }
        (this.progressView = new FrameLayout(context)).setVisibility(4);
        this.contentView.addView((View)this.progressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        (this.progressView2 = new View(context)).setBackgroundResource(2131165872);
        this.progressView2.getBackground().setColorFilter((ColorFilter)Theme.colorFilter);
        this.progressView.addView(this.progressView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(36, 36, 17));
        (this.progressBar = new RadialProgressView(context)).setSize(AndroidUtilities.dp(28.0f));
        this.progressBar.setProgressColor(Theme.getColor("chat_serviceText"));
        this.progressView.addView((View)this.progressBar, (ViewGroup$LayoutParams)LayoutHelper.createFrame(32, 32, 17));
        (this.floatingDateView = new ChatActionCell(context)).setAlpha(0.0f);
        this.contentView.addView((View)this.floatingDateView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 4.0f, 0.0f, 0.0f));
        this.contentView.addView((View)super.actionBar);
        (this.bottomOverlayChat = new FrameLayout(context) {
            public void onDraw(final Canvas canvas) {
                final int intrinsicHeight = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
                Theme.chat_composeShadowDrawable.setBounds(0, 0, this.getMeasuredWidth(), intrinsicHeight);
                Theme.chat_composeShadowDrawable.draw(canvas);
                canvas.drawRect(0.0f, (float)intrinsicHeight, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
            }
        }).setWillNotDraw(false);
        this.bottomOverlayChat.setPadding(0, AndroidUtilities.dp(3.0f), 0, 0);
        this.contentView.addView((View)this.bottomOverlayChat, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 51, 80));
        this.bottomOverlayChat.setOnClickListener((View$OnClickListener)new _$$Lambda$ChannelAdminLogActivity$PzYgcbyw3IRYlthKzfCR6dwSeeo(this));
        (this.bottomOverlayChatText = new TextView(context)).setTextSize(1, 15.0f);
        this.bottomOverlayChatText.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.bottomOverlayChatText.setTextColor(Theme.getColor("chat_fieldOverlayText"));
        this.bottomOverlayChatText.setText((CharSequence)LocaleController.getString("SETTINGS", 2131560623).toUpperCase());
        this.bottomOverlayChat.addView((View)this.bottomOverlayChatText, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 17));
        (this.bottomOverlayImage = new ImageView(context)).setImageResource(2131165548);
        this.bottomOverlayImage.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_fieldOverlayText"), PorterDuff$Mode.MULTIPLY));
        this.bottomOverlayImage.setScaleType(ImageView$ScaleType.CENTER);
        this.bottomOverlayChat.addView((View)this.bottomOverlayImage, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f, 53, 3.0f, 0.0f, 0.0f, 0.0f));
        this.bottomOverlayImage.setContentDescription((CharSequence)LocaleController.getString("BotHelp", 2131558850));
        this.bottomOverlayImage.setOnClickListener((View$OnClickListener)new _$$Lambda$ChannelAdminLogActivity$Hkc6JExHTyVzGS38RTThrIB1hTM(this));
        (this.searchContainer = new FrameLayout(context) {
            public void onDraw(final Canvas canvas) {
                final int intrinsicHeight = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
                Theme.chat_composeShadowDrawable.setBounds(0, 0, this.getMeasuredWidth(), intrinsicHeight);
                Theme.chat_composeShadowDrawable.draw(canvas);
                canvas.drawRect(0.0f, (float)intrinsicHeight, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
            }
        }).setWillNotDraw(false);
        this.searchContainer.setVisibility(4);
        this.searchContainer.setFocusable(true);
        this.searchContainer.setFocusableInTouchMode(true);
        this.searchContainer.setClickable(true);
        this.searchContainer.setPadding(0, AndroidUtilities.dp(3.0f), 0, 0);
        this.contentView.addView((View)this.searchContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 51, 80));
        (this.searchCalendarButton = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.searchCalendarButton.setImageResource(2131165615);
        this.searchCalendarButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_searchPanelIcons"), PorterDuff$Mode.MULTIPLY));
        this.searchContainer.addView((View)this.searchCalendarButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48, 53));
        this.searchCalendarButton.setOnClickListener((View$OnClickListener)new _$$Lambda$ChannelAdminLogActivity$qxESsL3_0yZHIVgE4y2ybeWozac(this));
        (this.searchCountText = new SimpleTextView(context)).setTextColor(Theme.getColor("chat_searchPanelText"));
        this.searchCountText.setTextSize(15);
        this.searchCountText.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.searchContainer.addView((View)this.searchCountText, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 19, 108.0f, 0.0f, 0.0f, 0.0f));
        this.chatAdapter.updateRows();
        if (this.loading && this.messages.isEmpty()) {
            this.progressView.setVisibility(0);
            this.chatListView.setEmptyView(null);
        }
        else {
            this.progressView.setVisibility(4);
            this.chatListView.setEmptyView((View)this.emptyViewContainer);
        }
        this.updateEmptyPlaceholder();
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(int i, int n, final Object... array) {
        if (i == NotificationCenter.emojiDidLoad) {
            final RecyclerListView chatListView = this.chatListView;
            if (chatListView != null) {
                chatListView.invalidateViews();
            }
        }
        else {
            final int messagePlayingDidStart = NotificationCenter.messagePlayingDidStart;
            n = 0;
            if (i == messagePlayingDidStart) {
                if (((MessageObject)array[0]).isRoundVideo()) {
                    MediaController.getInstance().setTextureView(this.createTextureView(true), this.aspectRatioFrameLayout, this.roundVideoContainer, true);
                    this.updateTextureViewPosition();
                }
                final RecyclerListView chatListView2 = this.chatListView;
                if (chatListView2 != null) {
                    View child;
                    ChatMessageCell chatMessageCell;
                    MessageObject messageObject;
                    for (n = chatListView2.getChildCount(), i = 0; i < n; ++i) {
                        child = this.chatListView.getChildAt(i);
                        if (child instanceof ChatMessageCell) {
                            chatMessageCell = (ChatMessageCell)child;
                            messageObject = chatMessageCell.getMessageObject();
                            if (messageObject != null) {
                                if (!messageObject.isVoice() && !messageObject.isMusic()) {
                                    if (messageObject.isRoundVideo()) {
                                        chatMessageCell.checkVideoPlayback(false);
                                        if (!MediaController.getInstance().isPlayingMessage(messageObject) && messageObject.audioProgress != 0.0f) {
                                            messageObject.resetPlayingProgress();
                                            chatMessageCell.invalidate();
                                        }
                                    }
                                }
                                else {
                                    chatMessageCell.updateButtonState(false, true, false);
                                }
                            }
                        }
                    }
                }
            }
            else if (i != NotificationCenter.messagePlayingDidReset && i != NotificationCenter.messagePlayingPlayStateChanged) {
                if (i == NotificationCenter.messagePlayingProgressDidChanged) {
                    final Integer n2 = (Integer)array[0];
                    final RecyclerListView chatListView3 = this.chatListView;
                    if (chatListView3 != null) {
                        int childCount;
                        View child2;
                        ChatMessageCell chatMessageCell2;
                        MessageObject messageObject2;
                        MessageObject playingMessageObject;
                        for (childCount = chatListView3.getChildCount(), i = n; i < childCount; ++i) {
                            child2 = this.chatListView.getChildAt(i);
                            if (child2 instanceof ChatMessageCell) {
                                chatMessageCell2 = (ChatMessageCell)child2;
                                messageObject2 = chatMessageCell2.getMessageObject();
                                if (messageObject2 != null && messageObject2.getId() == n2) {
                                    playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                                    if (playingMessageObject != null) {
                                        messageObject2.audioProgress = playingMessageObject.audioProgress;
                                        messageObject2.audioProgressSec = playingMessageObject.audioProgressSec;
                                        messageObject2.audioPlayerDuration = playingMessageObject.audioPlayerDuration;
                                        chatMessageCell2.updatePlayingMessageProgress();
                                        break;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                else if (i == NotificationCenter.didSetNewWallpapper && super.fragmentView != null) {
                    this.contentView.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());
                    this.progressView2.getBackground().setColorFilter((ColorFilter)Theme.colorFilter);
                    final TextView emptyView = this.emptyView;
                    if (emptyView != null) {
                        emptyView.getBackground().setColorFilter((ColorFilter)Theme.colorFilter);
                    }
                    this.chatListView.invalidateViews();
                }
            }
            else {
                final RecyclerListView chatListView4 = this.chatListView;
                if (chatListView4 != null) {
                    View child3;
                    ChatMessageCell chatMessageCell3;
                    MessageObject messageObject3;
                    for (n = chatListView4.getChildCount(), i = 0; i < n; ++i) {
                        child3 = this.chatListView.getChildAt(i);
                        if (child3 instanceof ChatMessageCell) {
                            chatMessageCell3 = (ChatMessageCell)child3;
                            messageObject3 = chatMessageCell3.getMessageObject();
                            if (messageObject3 != null) {
                                if (!messageObject3.isVoice() && !messageObject3.isMusic()) {
                                    if (messageObject3.isRoundVideo() && !MediaController.getInstance().isPlayingMessage(messageObject3)) {
                                        chatMessageCell3.checkVideoPlayback(true);
                                    }
                                }
                                else {
                                    chatMessageCell3.updateButtonState(false, true, false);
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
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final ThemeDescription themeDescription = new ThemeDescription(super.fragmentView, 0, null, null, null, null, "chat_wallpaper");
        final ThemeDescription themeDescription2 = new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault");
        final ThemeDescription themeDescription3 = new ThemeDescription((View)this.chatListView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault");
        final ThemeDescription themeDescription4 = new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon");
        final ThemeDescription themeDescription5 = new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector");
        final ThemeDescription themeDescription6 = new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, "actionBarDefaultSubmenuBackground");
        final ThemeDescription themeDescription7 = new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, "actionBarDefaultSubmenuItem");
        final ThemeDescription themeDescription8 = new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "actionBarDefaultSubmenuItemIcon");
        final ThemeDescription themeDescription9 = new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault");
        final ThemeDescription themeDescription10 = new ThemeDescription((View)this.chatListView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault");
        final ThemeDescription themeDescription11 = new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon");
        final ThemeDescription themeDescription12 = new ThemeDescription(this.avatarContainer.getTitleTextView(), ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "actionBarDefaultTitle");
        final ThemeDescription themeDescription13 = new ThemeDescription(this.avatarContainer.getSubtitleTextView(), ThemeDescription.FLAG_TEXTCOLOR, null, new Paint[] { Theme.chat_statusPaint, Theme.chat_statusRecordPaint }, null, null, "actionBarDefaultSubtitle", null);
        final ThemeDescription themeDescription14 = new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector");
        final ThemeDescription themeDescription15 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, null, "avatar_text");
        final ThemeDescription themeDescription16 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "avatar_backgroundRed");
        final ThemeDescription themeDescription17 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "avatar_backgroundOrange");
        final ThemeDescription themeDescription18 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "avatar_backgroundViolet");
        final ThemeDescription themeDescription19 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "avatar_backgroundGreen");
        final ThemeDescription themeDescription20 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "avatar_backgroundCyan");
        final ThemeDescription themeDescription21 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "avatar_backgroundBlue");
        final ThemeDescription themeDescription22 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "avatar_backgroundPink");
        final ThemeDescription themeDescription23 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "avatar_nameInMessageRed");
        final ThemeDescription themeDescription24 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "avatar_nameInMessageOrange");
        final ThemeDescription themeDescription25 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "avatar_nameInMessageViolet");
        final ThemeDescription themeDescription26 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "avatar_nameInMessageGreen");
        final ThemeDescription themeDescription27 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "avatar_nameInMessageCyan");
        final ThemeDescription themeDescription28 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "avatar_nameInMessageBlue");
        final ThemeDescription themeDescription29 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "avatar_nameInMessagePink");
        final ThemeDescription themeDescription30 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgInDrawable, Theme.chat_msgInMediaDrawable }, null, "chat_inBubble");
        final ThemeDescription themeDescription31 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgInSelectedDrawable, Theme.chat_msgInMediaSelectedDrawable }, null, "chat_inBubbleSelected");
        final ThemeDescription themeDescription32 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgInShadowDrawable, Theme.chat_msgInMediaShadowDrawable }, null, "chat_inBubbleShadow");
        final ThemeDescription themeDescription33 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable }, null, "chat_outBubble");
        final ThemeDescription themeDescription34 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgOutSelectedDrawable, Theme.chat_msgOutMediaSelectedDrawable }, null, "chat_outBubbleSelected");
        final ThemeDescription themeDescription35 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgOutShadowDrawable, Theme.chat_msgOutMediaShadowDrawable }, null, "chat_outBubbleShadow");
        final ThemeDescription themeDescription36 = new ThemeDescription((View)this.chatListView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { ChatActionCell.class }, (Paint)Theme.chat_actionTextPaint, null, null, "chat_serviceText");
        final ThemeDescription themeDescription37 = new ThemeDescription((View)this.chatListView, ThemeDescription.FLAG_LINKCOLOR, new Class[] { ChatActionCell.class }, (Paint)Theme.chat_actionTextPaint, null, null, "chat_serviceLink");
        final ThemeDescription themeDescription38 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_shareIconDrawable, Theme.chat_botInlineDrawable, Theme.chat_botLinkDrawalbe, Theme.chat_goIconDrawable }, null, "chat_serviceIcon");
        final ThemeDescription themeDescription39 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class, ChatActionCell.class }, null, null, null, "chat_serviceBackground");
        final ThemeDescription themeDescription40 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class, ChatActionCell.class }, null, null, null, "chat_serviceBackgroundSelected");
        final ThemeDescription themeDescription41 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_messageTextIn");
        final ThemeDescription themeDescription42 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_messageTextOut");
        final ThemeDescription themeDescription43 = new ThemeDescription((View)this.chatListView, ThemeDescription.FLAG_LINKCOLOR, new Class[] { ChatMessageCell.class }, null, null, null, "chat_messageLinkIn", null);
        final ThemeDescription themeDescription44 = new ThemeDescription((View)this.chatListView, ThemeDescription.FLAG_LINKCOLOR, new Class[] { ChatMessageCell.class }, null, null, null, "chat_messageLinkOut", null);
        final ThemeDescription themeDescription45 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgOutCheckDrawable, Theme.chat_msgOutHalfCheckDrawable }, null, "chat_outSentCheck");
        final ThemeDescription themeDescription46 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgOutCheckSelectedDrawable, Theme.chat_msgOutHalfCheckSelectedDrawable }, null, "chat_outSentCheckSelected");
        final ThemeDescription themeDescription47 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgOutClockDrawable }, null, "chat_outSentClock");
        final ThemeDescription themeDescription48 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgOutSelectedClockDrawable }, null, "chat_outSentClockSelected");
        final ThemeDescription themeDescription49 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgInClockDrawable }, null, "chat_inSentClock");
        final ThemeDescription themeDescription50 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgInSelectedClockDrawable }, null, "chat_inSentClockSelected");
        final ThemeDescription themeDescription51 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgMediaCheckDrawable, Theme.chat_msgMediaHalfCheckDrawable }, null, "chat_mediaSentCheck");
        final ThemeDescription themeDescription52 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgStickerHalfCheckDrawable, Theme.chat_msgStickerCheckDrawable, Theme.chat_msgStickerClockDrawable, Theme.chat_msgStickerViewsDrawable }, null, "chat_serviceText");
        final ThemeDescription themeDescription53 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgMediaClockDrawable }, null, "chat_mediaSentClock");
        final ThemeDescription themeDescription54 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgOutViewsDrawable }, null, "chat_outViews");
        final ThemeDescription themeDescription55 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgOutViewsSelectedDrawable }, null, "chat_outViewsSelected");
        final ThemeDescription themeDescription56 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgInViewsDrawable }, null, "chat_inViews");
        final ThemeDescription themeDescription57 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgInViewsSelectedDrawable }, null, "chat_inViewsSelected");
        final ThemeDescription themeDescription58 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgMediaViewsDrawable }, null, "chat_mediaViews");
        final ThemeDescription themeDescription59 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgOutMenuDrawable }, null, "chat_outMenu");
        final ThemeDescription themeDescription60 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgOutMenuSelectedDrawable }, null, "chat_outMenuSelected");
        final ThemeDescription themeDescription61 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgInMenuDrawable }, null, "chat_inMenu");
        final ThemeDescription themeDescription62 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgInMenuSelectedDrawable }, null, "chat_inMenuSelected");
        final ThemeDescription themeDescription63 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgMediaMenuDrawable }, null, "chat_mediaMenu");
        final ThemeDescription themeDescription64 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgOutInstantDrawable, Theme.chat_msgOutCallDrawable }, null, "chat_outInstant");
        final ThemeDescription themeDescription65 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgOutCallSelectedDrawable }, null, "chat_outInstantSelected");
        final ThemeDescription themeDescription66 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgInInstantDrawable, Theme.chat_msgInCallDrawable }, null, "chat_inInstant");
        final ThemeDescription themeDescription67 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgInCallSelectedDrawable }, null, "chat_inInstantSelected");
        final ThemeDescription themeDescription68 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgCallUpGreenDrawable }, null, "chat_outUpCall");
        final ThemeDescription themeDescription69 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgCallDownRedDrawable }, null, "chat_inUpCall");
        final ThemeDescription themeDescription70 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgCallDownGreenDrawable }, null, "chat_inDownCall");
        final ThemeDescription themeDescription71 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, Theme.chat_msgErrorPaint, null, null, "chat_sentError");
        final ThemeDescription themeDescription72 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgErrorDrawable }, null, "chat_sentErrorIcon");
        final ThemeDescription themeDescription73 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, (Paint)Theme.chat_durationPaint, null, null, "chat_previewDurationText");
        final ThemeDescription themeDescription74 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, (Paint)Theme.chat_gamePaint, null, null, "chat_previewGameText");
        final ThemeDescription themeDescription75 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inPreviewInstantText");
        final ThemeDescription themeDescription76 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outPreviewInstantText");
        final ThemeDescription themeDescription77 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inPreviewInstantSelectedText");
        final ThemeDescription themeDescription78 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outPreviewInstantSelectedText");
        final ThemeDescription themeDescription79 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, Theme.chat_deleteProgressPaint, null, null, "chat_secretTimeText");
        final ThemeDescription themeDescription80 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_stickerNameText");
        final ThemeDescription themeDescription81 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, (Paint)Theme.chat_botButtonPaint, null, null, "chat_botButtonText");
        final ThemeDescription themeDescription82 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, Theme.chat_botProgressPaint, null, null, "chat_botProgress");
        final ThemeDescription themeDescription83 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inForwardedNameText");
        final ThemeDescription themeDescription84 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outForwardedNameText");
        final ThemeDescription themeDescription85 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inViaBotNameText");
        final ThemeDescription themeDescription86 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outViaBotNameText");
        final ThemeDescription themeDescription87 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_stickerViaBotNameText");
        final ThemeDescription themeDescription88 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inReplyLine");
        final ThemeDescription themeDescription89 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outReplyLine");
        final ThemeDescription themeDescription90 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_stickerReplyLine");
        final ThemeDescription themeDescription91 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inReplyNameText");
        final ThemeDescription themeDescription92 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outReplyNameText");
        final ThemeDescription themeDescription93 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_stickerReplyNameText");
        final ThemeDescription themeDescription94 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inReplyMessageText");
        final ThemeDescription themeDescription95 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outReplyMessageText");
        final ThemeDescription themeDescription96 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inReplyMediaMessageText");
        final ThemeDescription themeDescription97 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outReplyMediaMessageText");
        final ThemeDescription themeDescription98 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inReplyMediaMessageSelectedText");
        final ThemeDescription themeDescription99 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outReplyMediaMessageSelectedText");
        final ThemeDescription themeDescription100 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_stickerReplyMessageText");
        final ThemeDescription themeDescription101 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inPreviewLine");
        final ThemeDescription themeDescription102 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outPreviewLine");
        final ThemeDescription themeDescription103 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inSiteNameText");
        final ThemeDescription themeDescription104 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outSiteNameText");
        final ThemeDescription themeDescription105 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inContactNameText");
        final ThemeDescription themeDescription106 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outContactNameText");
        final ThemeDescription themeDescription107 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inContactPhoneText");
        final ThemeDescription themeDescription108 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outContactPhoneText");
        final ThemeDescription themeDescription109 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_mediaProgress");
        final ThemeDescription themeDescription110 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inAudioProgress");
        final ThemeDescription themeDescription111 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outAudioProgress");
        final ThemeDescription themeDescription112 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inAudioSelectedProgress");
        final ThemeDescription themeDescription113 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outAudioSelectedProgress");
        final ThemeDescription themeDescription114 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_mediaTimeText");
        final ThemeDescription themeDescription115 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inTimeText");
        final ThemeDescription themeDescription116 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outTimeText");
        final ThemeDescription themeDescription117 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inTimeSelectedText");
        final ThemeDescription themeDescription118 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outTimeSelectedText");
        final ThemeDescription themeDescription119 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inAudioPerfomerText");
        final ThemeDescription themeDescription120 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outAudioPerfomerText");
        final ThemeDescription themeDescription121 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inAudioTitleText");
        final ThemeDescription themeDescription122 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outAudioTitleText");
        final ThemeDescription themeDescription123 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inAudioDurationText");
        final ThemeDescription themeDescription124 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outAudioDurationText");
        final ThemeDescription themeDescription125 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inAudioDurationSelectedText");
        final ThemeDescription themeDescription126 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outAudioDurationSelectedText");
        final ThemeDescription themeDescription127 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inAudioSeekbar");
        final ThemeDescription themeDescription128 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outAudioSeekbar");
        final ThemeDescription themeDescription129 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inAudioSeekbarSelected");
        final ThemeDescription themeDescription130 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outAudioSeekbarSelected");
        final ThemeDescription themeDescription131 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inAudioSeekbarFill");
        final ThemeDescription themeDescription132 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inAudioCacheSeekbar");
        final ThemeDescription themeDescription133 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outAudioSeekbarFill");
        final ThemeDescription themeDescription134 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outAudioCacheSeekbar");
        final ThemeDescription themeDescription135 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inVoiceSeekbar");
        final ThemeDescription themeDescription136 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outVoiceSeekbar");
        final ThemeDescription themeDescription137 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inVoiceSeekbarSelected");
        final ThemeDescription themeDescription138 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outVoiceSeekbarSelected");
        final ThemeDescription themeDescription139 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inVoiceSeekbarFill");
        final ThemeDescription themeDescription140 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outVoiceSeekbarFill");
        final ThemeDescription themeDescription141 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inFileProgress");
        final ThemeDescription themeDescription142 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outFileProgress");
        final ThemeDescription themeDescription143 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inFileProgressSelected");
        final ThemeDescription themeDescription144 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outFileProgressSelected");
        final ThemeDescription themeDescription145 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inFileNameText");
        final ThemeDescription themeDescription146 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outFileNameText");
        final ThemeDescription themeDescription147 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inFileInfoText");
        final ThemeDescription themeDescription148 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outFileInfoText");
        final ThemeDescription themeDescription149 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inFileInfoSelectedText");
        final ThemeDescription themeDescription150 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outFileInfoSelectedText");
        final ThemeDescription themeDescription151 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inFileBackground");
        final ThemeDescription themeDescription152 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outFileBackground");
        final ThemeDescription themeDescription153 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inFileBackgroundSelected");
        final ThemeDescription themeDescription154 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outFileBackgroundSelected");
        final ThemeDescription themeDescription155 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inVenueInfoText");
        final ThemeDescription themeDescription156 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outVenueInfoText");
        final ThemeDescription themeDescription157 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inVenueInfoSelectedText");
        final ThemeDescription themeDescription158 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outVenueInfoSelectedText");
        final ThemeDescription themeDescription159 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_mediaInfoText");
        final ThemeDescription themeDescription160 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, Theme.chat_urlPaint, null, null, "chat_linkSelectBackground");
        final ThemeDescription themeDescription161 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, Theme.chat_textSearchSelectionPaint, null, null, "chat_textSelectBackground");
        final ThemeDescription themeDescription162 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outLoader");
        final ThemeDescription themeDescription163 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outMediaIcon");
        final ThemeDescription themeDescription164 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outLoaderSelected");
        final ThemeDescription themeDescription165 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outMediaIconSelected");
        final ThemeDescription themeDescription166 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inLoader");
        final ThemeDescription themeDescription167 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inMediaIcon");
        final ThemeDescription themeDescription168 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inLoaderSelected");
        final ThemeDescription themeDescription169 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inMediaIconSelected");
        final RecyclerListView chatListView = this.chatListView;
        final int flag_BACKGROUNDFILTER = ThemeDescription.FLAG_BACKGROUNDFILTER;
        final Drawable[][] chat_photoStatesDrawables = Theme.chat_photoStatesDrawables;
        final ThemeDescription themeDescription170 = new ThemeDescription((View)chatListView, flag_BACKGROUNDFILTER, new Class[] { ChatMessageCell.class }, null, new Drawable[] { chat_photoStatesDrawables[0][0], chat_photoStatesDrawables[1][0], chat_photoStatesDrawables[2][0], chat_photoStatesDrawables[3][0] }, null, "chat_mediaLoaderPhoto");
        final RecyclerListView chatListView2 = this.chatListView;
        final Drawable[][] chat_photoStatesDrawables2 = Theme.chat_photoStatesDrawables;
        final ThemeDescription themeDescription171 = new ThemeDescription((View)chatListView2, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { chat_photoStatesDrawables2[0][0], chat_photoStatesDrawables2[1][0], chat_photoStatesDrawables2[2][0], chat_photoStatesDrawables2[3][0] }, null, "chat_mediaLoaderPhotoIcon");
        final RecyclerListView chatListView3 = this.chatListView;
        final int flag_BACKGROUNDFILTER2 = ThemeDescription.FLAG_BACKGROUNDFILTER;
        final Drawable[][] chat_photoStatesDrawables3 = Theme.chat_photoStatesDrawables;
        final ThemeDescription themeDescription172 = new ThemeDescription((View)chatListView3, flag_BACKGROUNDFILTER2, new Class[] { ChatMessageCell.class }, null, new Drawable[] { chat_photoStatesDrawables3[0][1], chat_photoStatesDrawables3[1][1], chat_photoStatesDrawables3[2][1], chat_photoStatesDrawables3[3][1] }, null, "chat_mediaLoaderPhotoSelected");
        final RecyclerListView chatListView4 = this.chatListView;
        final Drawable[][] chat_photoStatesDrawables4 = Theme.chat_photoStatesDrawables;
        final ThemeDescription themeDescription173 = new ThemeDescription((View)chatListView4, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { chat_photoStatesDrawables4[0][1], chat_photoStatesDrawables4[1][1], chat_photoStatesDrawables4[2][1], chat_photoStatesDrawables4[3][1] }, null, "chat_mediaLoaderPhotoIconSelected");
        final RecyclerListView chatListView5 = this.chatListView;
        final int flag_BACKGROUNDFILTER3 = ThemeDescription.FLAG_BACKGROUNDFILTER;
        final Drawable[][] chat_photoStatesDrawables5 = Theme.chat_photoStatesDrawables;
        final ThemeDescription themeDescription174 = new ThemeDescription((View)chatListView5, flag_BACKGROUNDFILTER3, new Class[] { ChatMessageCell.class }, null, new Drawable[] { chat_photoStatesDrawables5[7][0], chat_photoStatesDrawables5[8][0] }, null, "chat_outLoaderPhoto");
        final RecyclerListView chatListView6 = this.chatListView;
        final Drawable[][] chat_photoStatesDrawables6 = Theme.chat_photoStatesDrawables;
        final ThemeDescription themeDescription175 = new ThemeDescription((View)chatListView6, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { chat_photoStatesDrawables6[7][0], chat_photoStatesDrawables6[8][0] }, null, "chat_outLoaderPhotoIcon");
        final RecyclerListView chatListView7 = this.chatListView;
        final int flag_BACKGROUNDFILTER4 = ThemeDescription.FLAG_BACKGROUNDFILTER;
        final Drawable[][] chat_photoStatesDrawables7 = Theme.chat_photoStatesDrawables;
        final ThemeDescription themeDescription176 = new ThemeDescription((View)chatListView7, flag_BACKGROUNDFILTER4, new Class[] { ChatMessageCell.class }, null, new Drawable[] { chat_photoStatesDrawables7[7][1], chat_photoStatesDrawables7[8][1] }, null, "chat_outLoaderPhotoSelected");
        final RecyclerListView chatListView8 = this.chatListView;
        final Drawable[][] chat_photoStatesDrawables8 = Theme.chat_photoStatesDrawables;
        final ThemeDescription themeDescription177 = new ThemeDescription((View)chatListView8, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { chat_photoStatesDrawables8[7][1], chat_photoStatesDrawables8[8][1] }, null, "chat_outLoaderPhotoIconSelected");
        final RecyclerListView chatListView9 = this.chatListView;
        final int flag_BACKGROUNDFILTER5 = ThemeDescription.FLAG_BACKGROUNDFILTER;
        final Drawable[][] chat_photoStatesDrawables9 = Theme.chat_photoStatesDrawables;
        final ThemeDescription themeDescription178 = new ThemeDescription((View)chatListView9, flag_BACKGROUNDFILTER5, new Class[] { ChatMessageCell.class }, null, new Drawable[] { chat_photoStatesDrawables9[10][0], chat_photoStatesDrawables9[11][0] }, null, "chat_inLoaderPhoto");
        final RecyclerListView chatListView10 = this.chatListView;
        final Drawable[][] chat_photoStatesDrawables10 = Theme.chat_photoStatesDrawables;
        final ThemeDescription themeDescription179 = new ThemeDescription((View)chatListView10, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { chat_photoStatesDrawables10[10][0], chat_photoStatesDrawables10[11][0] }, null, "chat_inLoaderPhotoIcon");
        final RecyclerListView chatListView11 = this.chatListView;
        final int flag_BACKGROUNDFILTER6 = ThemeDescription.FLAG_BACKGROUNDFILTER;
        final Drawable[][] chat_photoStatesDrawables11 = Theme.chat_photoStatesDrawables;
        final ThemeDescription themeDescription180 = new ThemeDescription((View)chatListView11, flag_BACKGROUNDFILTER6, new Class[] { ChatMessageCell.class }, null, new Drawable[] { chat_photoStatesDrawables11[10][1], chat_photoStatesDrawables11[11][1] }, null, "chat_inLoaderPhotoSelected");
        final RecyclerListView chatListView12 = this.chatListView;
        final Drawable[][] chat_photoStatesDrawables12 = Theme.chat_photoStatesDrawables;
        final ThemeDescription themeDescription181 = new ThemeDescription((View)chatListView12, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { chat_photoStatesDrawables12[10][1], chat_photoStatesDrawables12[11][1] }, null, "chat_inLoaderPhotoIconSelected");
        final ThemeDescription themeDescription182 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_photoStatesDrawables[9][0] }, null, "chat_outFileIcon");
        final ThemeDescription themeDescription183 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_photoStatesDrawables[9][1] }, null, "chat_outFileSelectedIcon");
        final RecyclerListView chatListView13 = this.chatListView;
        final Drawable drawable = Theme.chat_photoStatesDrawables[12][0];
        final View view = null;
        final ThemeDescription themeDescription184 = new ThemeDescription((View)chatListView13, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { drawable }, null, "chat_inFileIcon");
        final ThemeDescription themeDescription185 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_photoStatesDrawables[12][1] }, null, "chat_inFileSelectedIcon");
        final ThemeDescription themeDescription186 = new ThemeDescription((View)this.chatListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_contactDrawable[0] }, null, "chat_inContactBackground");
        final ThemeDescription themeDescription187 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_contactDrawable[0] }, null, "chat_inContactIcon");
        final ThemeDescription themeDescription188 = new ThemeDescription((View)this.chatListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_contactDrawable[1] }, null, "chat_outContactBackground");
        final ThemeDescription themeDescription189 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_contactDrawable[1] }, null, "chat_outContactIcon");
        final ThemeDescription themeDescription190 = new ThemeDescription((View)this.chatListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_locationDrawable[0] }, null, "chat_inLocationBackground");
        final ThemeDescription themeDescription191 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_locationDrawable[0] }, null, "chat_inLocationIcon");
        final ThemeDescription themeDescription192 = new ThemeDescription((View)this.chatListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_locationDrawable[1] }, null, "chat_outLocationBackground");
        final ThemeDescription themeDescription193 = new ThemeDescription((View)this.chatListView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_locationDrawable[1] }, null, "chat_outLocationIcon");
        final ThemeDescription themeDescription194 = new ThemeDescription((View)this.bottomOverlayChat, 0, null, Theme.chat_composeBackgroundPaint, null, null, "chat_messagePanelBackground");
        final ThemeDescription themeDescription195 = new ThemeDescription((View)this.bottomOverlayChat, 0, null, null, new Drawable[] { Theme.chat_composeShadowDrawable }, null, "chat_messagePanelShadow");
        final ThemeDescription themeDescription196 = new ThemeDescription((View)this.bottomOverlayChatText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "chat_fieldOverlayText");
        final ThemeDescription themeDescription197 = new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "chat_serviceText");
        final ThemeDescription themeDescription198 = new ThemeDescription(this.progressBar, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "chat_serviceText");
        final ThemeDescription themeDescription199 = new ThemeDescription((View)this.chatListView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[] { ChatUnreadCell.class }, new String[] { "backgroundLayout" }, null, null, null, "chat_unreadMessagesStartBackground");
        final ThemeDescription themeDescription200 = new ThemeDescription((View)this.chatListView, ThemeDescription.FLAG_IMAGECOLOR, new Class[] { ChatUnreadCell.class }, new String[] { "imageView" }, null, null, null, "chat_unreadMessagesStartArrowIcon");
        final ThemeDescription themeDescription201 = new ThemeDescription((View)this.chatListView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { ChatUnreadCell.class }, new String[] { "textView" }, null, null, null, "chat_unreadMessagesStartText");
        final ThemeDescription themeDescription202 = new ThemeDescription(this.progressView2, ThemeDescription.FLAG_SERVICEBACKGROUND, null, null, null, null, "chat_serviceBackground");
        final ThemeDescription themeDescription203 = new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_SERVICEBACKGROUND, null, null, null, null, "chat_serviceBackground");
        final ThemeDescription themeDescription204 = new ThemeDescription((View)this.chatListView, ThemeDescription.FLAG_SERVICEBACKGROUND, new Class[] { ChatLoadingCell.class }, new String[] { "textView" }, null, null, null, "chat_serviceBackground");
        final ThemeDescription themeDescription205 = new ThemeDescription((View)this.chatListView, ThemeDescription.FLAG_PROGRESSBAR, new Class[] { ChatLoadingCell.class }, new String[] { "textView" }, null, null, null, "chat_serviceText");
        final ChatAvatarContainer avatarContainer = this.avatarContainer;
        Object timeItem;
        if (avatarContainer != null) {
            timeItem = avatarContainer.getTimeItem();
        }
        else {
            timeItem = null;
        }
        final ThemeDescription themeDescription206 = new ThemeDescription((View)timeItem, 0, null, null, null, null, "chat_secretTimerBackground");
        final ChatAvatarContainer avatarContainer2 = this.avatarContainer;
        Object timeItem2 = view;
        if (avatarContainer2 != null) {
            timeItem2 = avatarContainer2.getTimeItem();
        }
        return new ThemeDescription[] { themeDescription, themeDescription2, themeDescription3, themeDescription4, themeDescription5, themeDescription6, themeDescription7, themeDescription8, themeDescription9, themeDescription10, themeDescription11, themeDescription12, themeDescription13, themeDescription14, themeDescription15, themeDescription16, themeDescription17, themeDescription18, themeDescription19, themeDescription20, themeDescription21, themeDescription22, themeDescription23, themeDescription24, themeDescription25, themeDescription26, themeDescription27, themeDescription28, themeDescription29, themeDescription30, themeDescription31, themeDescription32, themeDescription33, themeDescription34, themeDescription35, themeDescription36, themeDescription37, themeDescription38, themeDescription39, themeDescription40, themeDescription41, themeDescription42, themeDescription43, themeDescription44, themeDescription45, themeDescription46, themeDescription47, themeDescription48, themeDescription49, themeDescription50, themeDescription51, themeDescription52, themeDescription53, themeDescription54, themeDescription55, themeDescription56, themeDescription57, themeDescription58, themeDescription59, themeDescription60, themeDescription61, themeDescription62, themeDescription63, themeDescription64, themeDescription65, themeDescription66, themeDescription67, themeDescription68, themeDescription69, themeDescription70, themeDescription71, themeDescription72, themeDescription73, themeDescription74, themeDescription75, themeDescription76, themeDescription77, themeDescription78, themeDescription79, themeDescription80, themeDescription81, themeDescription82, themeDescription83, themeDescription84, themeDescription85, themeDescription86, themeDescription87, themeDescription88, themeDescription89, themeDescription90, themeDescription91, themeDescription92, themeDescription93, themeDescription94, themeDescription95, themeDescription96, themeDescription97, themeDescription98, themeDescription99, themeDescription100, themeDescription101, themeDescription102, themeDescription103, themeDescription104, themeDescription105, themeDescription106, themeDescription107, themeDescription108, themeDescription109, themeDescription110, themeDescription111, themeDescription112, themeDescription113, themeDescription114, themeDescription115, themeDescription116, themeDescription117, themeDescription118, themeDescription119, themeDescription120, themeDescription121, themeDescription122, themeDescription123, themeDescription124, themeDescription125, themeDescription126, themeDescription127, themeDescription128, themeDescription129, themeDescription130, themeDescription131, themeDescription132, themeDescription133, themeDescription134, themeDescription135, themeDescription136, themeDescription137, themeDescription138, themeDescription139, themeDescription140, themeDescription141, themeDescription142, themeDescription143, themeDescription144, themeDescription145, themeDescription146, themeDescription147, themeDescription148, themeDescription149, themeDescription150, themeDescription151, themeDescription152, themeDescription153, themeDescription154, themeDescription155, themeDescription156, themeDescription157, themeDescription158, themeDescription159, themeDescription160, themeDescription161, themeDescription162, themeDescription163, themeDescription164, themeDescription165, themeDescription166, themeDescription167, themeDescription168, themeDescription169, themeDescription170, themeDescription171, themeDescription172, themeDescription173, themeDescription174, themeDescription175, themeDescription176, themeDescription177, themeDescription178, themeDescription179, themeDescription180, themeDescription181, themeDescription182, themeDescription183, themeDescription184, themeDescription185, themeDescription186, themeDescription187, themeDescription188, themeDescription189, themeDescription190, themeDescription191, themeDescription192, themeDescription193, themeDescription194, themeDescription195, themeDescription196, themeDescription197, themeDescription198, themeDescription199, themeDescription200, themeDescription201, themeDescription202, themeDescription203, themeDescription204, themeDescription205, themeDescription206, new ThemeDescription((View)timeItem2, 0, null, null, null, null, "chat_secretTimerText") };
    }
    
    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        this.fixLayout();
        final Dialog visibleDialog = super.visibleDialog;
        if (visibleDialog instanceof DatePickerDialog) {
            visibleDialog.dismiss();
        }
    }
    
    @Override
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
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidStart);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        final SizeNotifierFrameLayout contentView = this.contentView;
        if (contentView != null) {
            contentView.onPause();
        }
        this.paused = true;
        this.wasPaused = true;
    }
    
    @Override
    protected void onRemoveFromParent() {
        MediaController.getInstance().setTextureView(this.videoTextureView, null, null, false);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final SizeNotifierFrameLayout contentView = this.contentView;
        if (contentView != null) {
            contentView.onResume();
        }
        this.checkScrollForLoad(this.paused = false);
        if (this.wasPaused) {
            this.wasPaused = false;
            final ChatActivityAdapter chatAdapter = this.chatAdapter;
            if (chatAdapter != null) {
                chatAdapter.notifyDataSetChanged();
            }
        }
        this.fixLayout();
    }
    
    public void onTransitionAnimationEnd(final boolean b, final boolean b2) {
        NotificationCenter.getInstance(super.currentAccount).setAnimationInProgress(false);
        if (b) {
            this.openAnimationEnded = true;
        }
    }
    
    public void onTransitionAnimationStart(final boolean b, final boolean b2) {
        NotificationCenter.getInstance(super.currentAccount).setAllowedNotificationsDutingAnimation(new int[] { NotificationCenter.chatInfoDidLoad, NotificationCenter.dialogsNeedReload, NotificationCenter.closeChats, NotificationCenter.messagesDidLoad, NotificationCenter.botKeyboardDidLoad });
        NotificationCenter.getInstance(super.currentAccount).setAnimationInProgress(true);
        if (b) {
            this.openAnimationEnded = false;
        }
    }
    
    public void openVCard(final String str, final String s, final String s2) {
        try {
            final File parent = new File(FileLoader.getDirectory(4), "sharing/");
            parent.mkdirs();
            final File file = new File(parent, "vcard.vcf");
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(str);
            bufferedWriter.close();
            this.presentFragment(new PhonebookShareActivity(null, null, file, ContactsController.formatName(s, s2)));
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public void showOpenUrlAlert(final String s, final boolean b) {
        if (!Browser.isInternalUrl(s, null) && b) {
            final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
            builder.setTitle(LocaleController.getString("OpenUrlTitle", 2131560121));
            builder.setMessage(LocaleController.formatString("OpenUrlAlert2", 2131560118, s));
            builder.setPositiveButton(LocaleController.getString("Open", 2131560110), (DialogInterface$OnClickListener)new _$$Lambda$ChannelAdminLogActivity$iAIppxrAcTSiaPaqrhtDiQvqAXs(this, s));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
            this.showDialog(builder.create());
        }
        else {
            Browser.openUrl((Context)this.getParentActivity(), s, true);
        }
    }
    
    public class ChatActivityAdapter extends Adapter
    {
        private int loadingUpRow;
        private Context mContext;
        private int messagesEndRow;
        private int messagesStartRow;
        private int rowCount;
        final /* synthetic */ ChannelAdminLogActivity this$0;
        
        public ChatActivityAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            return this.rowCount;
        }
        
        @Override
        public long getItemId(final int n) {
            return -1L;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n >= this.messagesStartRow && n < this.messagesEndRow) {
                final ArrayList<MessageObject> messages = ChannelAdminLogActivity.this.messages;
                return messages.get(messages.size() - (n - this.messagesStartRow) - 1).contentType;
            }
            return 4;
        }
        
        @Override
        public void notifyDataSetChanged() {
            this.updateRows();
            try {
                super.notifyDataSetChanged();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        
        @Override
        public void notifyItemChanged(final int n) {
            this.updateRows();
            try {
                super.notifyItemChanged(n);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        
        @Override
        public void notifyItemInserted(final int n) {
            this.updateRows();
            try {
                super.notifyItemInserted(n);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        
        @Override
        public void notifyItemMoved(final int n, final int n2) {
            this.updateRows();
            try {
                super.notifyItemMoved(n, n2);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        
        @Override
        public void notifyItemRangeChanged(final int n, final int n2) {
            this.updateRows();
            try {
                super.notifyItemRangeChanged(n, n2);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        
        @Override
        public void notifyItemRangeInserted(final int n, final int n2) {
            this.updateRows();
            try {
                super.notifyItemRangeInserted(n, n2);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        
        @Override
        public void notifyItemRangeRemoved(final int n, final int n2) {
            this.updateRows();
            try {
                super.notifyItemRangeRemoved(n, n2);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        
        @Override
        public void notifyItemRemoved(final int n) {
            this.updateRows();
            try {
                super.notifyItemRemoved(n);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int from_id) {
            final int loadingUpRow = this.loadingUpRow;
            boolean progressVisible = false;
            boolean b = true;
            if (from_id == loadingUpRow) {
                final ChatLoadingCell chatLoadingCell = (ChatLoadingCell)viewHolder.itemView;
                if (ChannelAdminLogActivity.this.loadsCount > 1) {
                    progressVisible = true;
                }
                chatLoadingCell.setProgressVisible(progressVisible);
            }
            else if (from_id >= this.messagesStartRow && from_id < this.messagesEndRow) {
                final ArrayList<MessageObject> messages = ChannelAdminLogActivity.this.messages;
                final MessageObject messageObject = messages.get(messages.size() - (from_id - this.messagesStartRow) - 1);
                final View itemView = viewHolder.itemView;
                if (itemView instanceof ChatMessageCell) {
                    final ChatMessageCell chatMessageCell = (ChatMessageCell)itemView;
                    chatMessageCell.isChat = true;
                    final int n = from_id + 1;
                    final int itemViewType = this.getItemViewType(n);
                    final int itemViewType2 = this.getItemViewType(from_id - 1);
                    boolean b2 = false;
                    Label_0274: {
                        if (!(messageObject.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup) && itemViewType == viewHolder.getItemViewType()) {
                            final ArrayList<MessageObject> messages2 = ChannelAdminLogActivity.this.messages;
                            final MessageObject messageObject2 = messages2.get(messages2.size() - (n - this.messagesStartRow) - 1);
                            if (messageObject2.isOutOwner() == messageObject.isOutOwner()) {
                                final TLRPC.Message messageOwner = messageObject2.messageOwner;
                                final int from_id2 = messageOwner.from_id;
                                final TLRPC.Message messageOwner2 = messageObject.messageOwner;
                                if (from_id2 == messageOwner2.from_id && Math.abs(messageOwner.date - messageOwner2.date) <= 300) {
                                    b2 = true;
                                    break Label_0274;
                                }
                            }
                        }
                        b2 = false;
                    }
                    Label_0385: {
                        if (itemViewType2 == viewHolder.getItemViewType()) {
                            final ArrayList<MessageObject> messages3 = ChannelAdminLogActivity.this.messages;
                            final MessageObject messageObject3 = messages3.get(messages3.size() - (from_id - this.messagesStartRow));
                            if (!(messageObject3.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup) && messageObject3.isOutOwner() == messageObject.isOutOwner()) {
                                final TLRPC.Message messageOwner3 = messageObject3.messageOwner;
                                from_id = messageOwner3.from_id;
                                final TLRPC.Message messageOwner4 = messageObject.messageOwner;
                                if (from_id == messageOwner4.from_id && Math.abs(messageOwner3.date - messageOwner4.date) <= 300) {
                                    break Label_0385;
                                }
                            }
                        }
                        b = false;
                    }
                    chatMessageCell.setMessageObject(messageObject, null, b2, b);
                    chatMessageCell.setHighlighted(false);
                    chatMessageCell.setHighlightedText(null);
                }
                else if (itemView instanceof ChatActionCell) {
                    final ChatActionCell chatActionCell = (ChatActionCell)itemView;
                    chatActionCell.setMessageObject(messageObject);
                    chatActionCell.setAlpha(1.0f);
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n == 0) {
                if (!ChannelAdminLogActivity.this.chatMessageCellsCache.isEmpty()) {
                    o = ChannelAdminLogActivity.this.chatMessageCellsCache.get(0);
                    ChannelAdminLogActivity.this.chatMessageCellsCache.remove(0);
                }
                else {
                    o = new ChatMessageCell(this.mContext);
                }
                final ChatMessageCell chatMessageCell = (ChatMessageCell)o;
                chatMessageCell.setDelegate((ChatMessageCell.ChatMessageCellDelegate)new ChatMessageCell.ChatMessageCellDelegate() {
                    @Override
                    public boolean canPerformActions() {
                        return true;
                    }
                    
                    @Override
                    public void didLongPress(final ChatMessageCell chatMessageCell, final float n, final float n2) {
                        ChannelAdminLogActivity.this.createMenu((View)chatMessageCell);
                    }
                    
                    @Override
                    public void didPressBotButton(final ChatMessageCell chatMessageCell, final TLRPC.KeyboardButton keyboardButton) {
                    }
                    
                    @Override
                    public void didPressCancelSendButton(final ChatMessageCell chatMessageCell) {
                    }
                    
                    @Override
                    public void didPressChannelAvatar(final ChatMessageCell chatMessageCell, final TLRPC.Chat chat, final int n, final float n2, final float n3) {
                        if (chat != null && chat != ChannelAdminLogActivity.this.currentChat) {
                            final Bundle bundle = new Bundle();
                            bundle.putInt("chat_id", chat.id);
                            if (n != 0) {
                                bundle.putInt("message_id", n);
                            }
                            if (MessagesController.getInstance(ChannelAdminLogActivity.this.currentAccount).checkCanOpenChat(bundle, ChannelAdminLogActivity.this)) {
                                ChannelAdminLogActivity.this.presentFragment(new ChatActivity(bundle), true);
                            }
                        }
                    }
                    
                    @Override
                    public void didPressImage(final ChatMessageCell chatMessageCell, final float n, final float n2) {
                        final MessageObject messageObject = chatMessageCell.getMessageObject();
                        if (messageObject.type == 13) {
                            final ChannelAdminLogActivity this$0 = ChannelAdminLogActivity.this;
                            this$0.showDialog(new StickersAlert((Context)this$0.getParentActivity(), ChannelAdminLogActivity.this, messageObject.getInputStickerSet(), null, null));
                        }
                        else {
                            if (!messageObject.isVideo()) {
                                final int type = messageObject.type;
                                if (type != 1 && (type != 0 || messageObject.isWebpageDocument())) {
                                    if (!messageObject.isGif()) {
                                        final int type2 = messageObject.type;
                                        final File file = null;
                                        if (type2 == 3) {
                                            File file2 = file;
                                            try {
                                                if (messageObject.messageOwner.attachPath != null) {
                                                    file2 = file;
                                                    if (messageObject.messageOwner.attachPath.length() != 0) {
                                                        file2 = new File(messageObject.messageOwner.attachPath);
                                                    }
                                                }
                                                File pathToMessage = null;
                                                Label_0189: {
                                                    if (file2 != null) {
                                                        pathToMessage = file2;
                                                        if (file2.exists()) {
                                                            break Label_0189;
                                                        }
                                                    }
                                                    pathToMessage = FileLoader.getPathToMessage(messageObject.messageOwner);
                                                }
                                                final Intent intent = new Intent("android.intent.action.VIEW");
                                                if (Build$VERSION.SDK_INT >= 24) {
                                                    intent.setFlags(1);
                                                    intent.setDataAndType(FileProvider.getUriForFile((Context)ChannelAdminLogActivity.this.getParentActivity(), "org.telegram.messenger.provider", pathToMessage), "video/mp4");
                                                }
                                                else {
                                                    intent.setDataAndType(Uri.fromFile(pathToMessage), "video/mp4");
                                                }
                                                ChannelAdminLogActivity.this.getParentActivity().startActivityForResult(intent, 500);
                                            }
                                            catch (Exception ex) {
                                                ChannelAdminLogActivity.this.alertUserOpenError(messageObject);
                                            }
                                            return;
                                        }
                                        if (type2 == 4) {
                                            if (!AndroidUtilities.isGoogleMapsInstalled(ChannelAdminLogActivity.this)) {
                                                return;
                                            }
                                            final LocationActivity locationActivity = new LocationActivity(0);
                                            locationActivity.setMessageObject(messageObject);
                                            ChannelAdminLogActivity.this.presentFragment(locationActivity);
                                            return;
                                        }
                                        else {
                                            if (type2 == 9 || type2 == 0) {
                                                if (messageObject.getDocumentName().toLowerCase().endsWith("attheme")) {
                                                    final String attachPath = messageObject.messageOwner.attachPath;
                                                    File file3 = null;
                                                    Label_0418: {
                                                        if (attachPath != null && attachPath.length() != 0) {
                                                            file3 = new File(messageObject.messageOwner.attachPath);
                                                            if (file3.exists()) {
                                                                break Label_0418;
                                                            }
                                                        }
                                                        file3 = null;
                                                    }
                                                    File file4 = file3;
                                                    if (file3 == null) {
                                                        final File pathToMessage2 = FileLoader.getPathToMessage(messageObject.messageOwner);
                                                        file4 = file3;
                                                        if (pathToMessage2.exists()) {
                                                            file4 = pathToMessage2;
                                                        }
                                                    }
                                                    if (ChannelAdminLogActivity.this.chatLayoutManager != null) {
                                                        if (ChannelAdminLogActivity.this.chatLayoutManager.findLastVisibleItemPosition() < ((RecyclerView.LayoutManager)ChannelAdminLogActivity.this.chatLayoutManager).getItemCount() - 1) {
                                                            final ChannelAdminLogActivity this$2 = ChannelAdminLogActivity.this;
                                                            this$2.scrollToPositionOnRecreate = this$2.chatLayoutManager.findFirstVisibleItemPosition();
                                                            final RecyclerListView.Holder holder = (RecyclerListView.Holder)ChannelAdminLogActivity.this.chatListView.findViewHolderForAdapterPosition(ChannelAdminLogActivity.this.scrollToPositionOnRecreate);
                                                            if (holder != null) {
                                                                ChannelAdminLogActivity.this.scrollToOffsetOnRecreate = holder.itemView.getTop();
                                                            }
                                                            else {
                                                                ChannelAdminLogActivity.this.scrollToPositionOnRecreate = -1;
                                                            }
                                                        }
                                                        else {
                                                            ChannelAdminLogActivity.this.scrollToPositionOnRecreate = -1;
                                                        }
                                                    }
                                                    final Theme.ThemeInfo applyThemeFile = Theme.applyThemeFile(file4, messageObject.getDocumentName(), true);
                                                    if (applyThemeFile != null) {
                                                        ChannelAdminLogActivity.this.presentFragment(new ThemePreviewActivity(file4, applyThemeFile));
                                                        return;
                                                    }
                                                    ChannelAdminLogActivity.this.scrollToPositionOnRecreate = -1;
                                                }
                                                try {
                                                    AndroidUtilities.openForView(messageObject, ChannelAdminLogActivity.this.getParentActivity());
                                                }
                                                catch (Exception ex2) {
                                                    ChannelAdminLogActivity.this.alertUserOpenError(messageObject);
                                                }
                                            }
                                            return;
                                        }
                                    }
                                }
                            }
                            PhotoViewer.getInstance().setParentActivity(ChannelAdminLogActivity.this.getParentActivity());
                            PhotoViewer.getInstance().openPhoto(messageObject, 0L, 0L, ChannelAdminLogActivity.this.provider);
                        }
                    }
                    
                    @Override
                    public void didPressInstantButton(final ChatMessageCell chatMessageCell, final int n) {
                        final MessageObject messageObject = chatMessageCell.getMessageObject();
                        if (n == 0) {
                            final TLRPC.MessageMedia media = messageObject.messageOwner.media;
                            if (media != null) {
                                final TLRPC.WebPage webpage = media.webpage;
                                if (webpage != null && webpage.cached_page != null) {
                                    ArticleViewer.getInstance().setParentActivity(ChannelAdminLogActivity.this.getParentActivity(), ChannelAdminLogActivity.this);
                                    ArticleViewer.getInstance().open(messageObject);
                                }
                            }
                        }
                        else if (n == 5) {
                            final ChannelAdminLogActivity this$0 = ChannelAdminLogActivity.this;
                            final TLRPC.MessageMedia media2 = messageObject.messageOwner.media;
                            this$0.openVCard(media2.vcard, media2.first_name, media2.last_name);
                        }
                        else {
                            final TLRPC.MessageMedia media3 = messageObject.messageOwner.media;
                            if (media3 != null && media3.webpage != null) {
                                Browser.openUrl((Context)ChannelAdminLogActivity.this.getParentActivity(), messageObject.messageOwner.media.webpage.url);
                            }
                        }
                    }
                    
                    @Override
                    public void didPressOther(final ChatMessageCell chatMessageCell, final float n, final float n2) {
                        ChannelAdminLogActivity.this.createMenu((View)chatMessageCell);
                    }
                    
                    @Override
                    public void didPressReplyMessage(final ChatMessageCell chatMessageCell, final int n) {
                    }
                    
                    @Override
                    public void didPressShare(final ChatMessageCell chatMessageCell) {
                        if (ChannelAdminLogActivity.this.getParentActivity() == null) {
                            return;
                        }
                        final ChatActivityAdapter this$1 = ChatActivityAdapter.this;
                        this$1.this$0.showDialog(ShareAlert.createShareAlert(this$1.mContext, chatMessageCell.getMessageObject(), null, ChatObject.isChannel(ChannelAdminLogActivity.this.currentChat) && !ChannelAdminLogActivity.this.currentChat.megagroup, null, false));
                    }
                    
                    @Override
                    public void didPressUrl(final MessageObject messageObject, final CharacterStyle characterStyle, final boolean b) {
                        if (characterStyle == null) {
                            return;
                        }
                        if (characterStyle instanceof URLSpanMono) {
                            ((URLSpanMono)characterStyle).copyToClipboard();
                            Toast.makeText((Context)ChannelAdminLogActivity.this.getParentActivity(), (CharSequence)LocaleController.getString("TextCopied", 2131560887), 0).show();
                        }
                        else if (characterStyle instanceof URLSpanUserMention) {
                            final TLRPC.User user = MessagesController.getInstance(ChannelAdminLogActivity.this.currentAccount).getUser(Utilities.parseInt(((URLSpanUserMention)characterStyle).getURL()));
                            if (user != null) {
                                MessagesController.openChatOrProfileWith(user, null, ChannelAdminLogActivity.this, 0, false);
                            }
                        }
                        else if (characterStyle instanceof URLSpanNoUnderline) {
                            final String url = ((URLSpanNoUnderline)characterStyle).getURL();
                            if (url.startsWith("@")) {
                                MessagesController.getInstance(ChannelAdminLogActivity.this.currentAccount).openByUserName(url.substring(1), ChannelAdminLogActivity.this, 0);
                            }
                            else if (url.startsWith("#")) {
                                final DialogsActivity dialogsActivity = new DialogsActivity(null);
                                dialogsActivity.setSearchString(url);
                                ChannelAdminLogActivity.this.presentFragment(dialogsActivity);
                            }
                        }
                        else {
                            final String url2 = ((URLSpan)characterStyle).getURL();
                            if (b) {
                                final BottomSheet.Builder builder = new BottomSheet.Builder((Context)ChannelAdminLogActivity.this.getParentActivity());
                                builder.setTitle(url2);
                                builder.setItems(new CharSequence[] { LocaleController.getString("Open", 2131560110), LocaleController.getString("Copy", 2131559163) }, (DialogInterface$OnClickListener)new _$$Lambda$ChannelAdminLogActivity$ChatActivityAdapter$1$D0xLcgtrHEslS6nrwCqImW0Sa1U(this, url2));
                                ChannelAdminLogActivity.this.showDialog(builder.create());
                            }
                            else if (characterStyle instanceof URLSpanReplacement) {
                                ChannelAdminLogActivity.this.showOpenUrlAlert(((URLSpanReplacement)characterStyle).getURL(), true);
                            }
                            else if (characterStyle instanceof URLSpan) {
                                final TLRPC.MessageMedia media = messageObject.messageOwner.media;
                                if (media instanceof TLRPC.TL_messageMediaWebPage) {
                                    final TLRPC.WebPage webpage = media.webpage;
                                    if (webpage != null && webpage.cached_page != null) {
                                        final String lowerCase = url2.toLowerCase();
                                        final String lowerCase2 = messageObject.messageOwner.media.webpage.url.toLowerCase();
                                        if ((lowerCase.contains("telegra.ph") || lowerCase.contains("t.me/iv")) && (lowerCase.contains(lowerCase2) || lowerCase2.contains(lowerCase))) {
                                            ArticleViewer.getInstance().setParentActivity(ChannelAdminLogActivity.this.getParentActivity(), ChannelAdminLogActivity.this);
                                            ArticleViewer.getInstance().open(messageObject);
                                            return;
                                        }
                                    }
                                }
                                Browser.openUrl((Context)ChannelAdminLogActivity.this.getParentActivity(), url2, true);
                            }
                            else if (characterStyle instanceof ClickableSpan) {
                                ((ClickableSpan)characterStyle).onClick(ChannelAdminLogActivity.this.fragmentView);
                            }
                        }
                    }
                    
                    @Override
                    public void didPressUserAvatar(final ChatMessageCell chatMessageCell, final TLRPC.User user, final float n, final float n2) {
                        if (user != null && user.id != UserConfig.getInstance(ChannelAdminLogActivity.this.currentAccount).getClientUserId()) {
                            final Bundle bundle = new Bundle();
                            bundle.putInt("user_id", user.id);
                            ChannelAdminLogActivity.this.addCanBanUser(bundle, user.id);
                            final ProfileActivity profileActivity = new ProfileActivity(bundle);
                            profileActivity.setPlayProfileAnimation(false);
                            ChannelAdminLogActivity.this.presentFragment(profileActivity);
                        }
                    }
                    
                    @Override
                    public void didPressViaBot(final ChatMessageCell chatMessageCell, final String s) {
                    }
                    
                    @Override
                    public void didPressVoteButton(final ChatMessageCell chatMessageCell, final TLRPC.TL_pollAnswer tl_pollAnswer) {
                    }
                    
                    @Override
                    public boolean isChatAdminCell(final int n) {
                        return false;
                    }
                    
                    @Override
                    public void needOpenWebView(final String s, final String s2, final String s3, final String s4, final int n, final int n2) {
                        EmbedBottomSheet.show(ChatActivityAdapter.this.mContext, s2, s3, s4, s, n, n2);
                    }
                    
                    @Override
                    public boolean needPlayMessage(final MessageObject messageObject) {
                        if (!messageObject.isVoice() && !messageObject.isRoundVideo()) {
                            return messageObject.isMusic() && MediaController.getInstance().setPlaylist(ChannelAdminLogActivity.this.messages, messageObject);
                        }
                        final boolean playMessage = MediaController.getInstance().playMessage(messageObject);
                        MediaController.getInstance().setVoiceMessagesPlaylist(null, false);
                        return playMessage;
                    }
                });
                chatMessageCell.setAllowAssistant(true);
            }
            else if (n == 1) {
                o = new ChatActionCell(this.mContext);
                ((ChatActionCell)o).setDelegate((ChatActionCell.ChatActionCellDelegate)new ChatActionCell.ChatActionCellDelegate() {
                    @Override
                    public void didClickImage(final ChatActionCell chatActionCell) {
                        final MessageObject messageObject = chatActionCell.getMessageObject();
                        PhotoViewer.getInstance().setParentActivity(ChannelAdminLogActivity.this.getParentActivity());
                        final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 640);
                        if (closestPhotoSizeWithSize != null) {
                            PhotoViewer.getInstance().openPhoto(closestPhotoSizeWithSize.location, ChannelAdminLogActivity.this.provider);
                        }
                        else {
                            PhotoViewer.getInstance().openPhoto(messageObject, 0L, 0L, ChannelAdminLogActivity.this.provider);
                        }
                    }
                    
                    @Override
                    public void didLongPress(final ChatActionCell chatActionCell, final float n, final float n2) {
                        ChannelAdminLogActivity.this.createMenu((View)chatActionCell);
                    }
                    
                    @Override
                    public void didPressBotButton(final MessageObject messageObject, final TLRPC.KeyboardButton keyboardButton) {
                    }
                    
                    @Override
                    public void didPressReplyMessage(final ChatActionCell chatActionCell, final int n) {
                    }
                    
                    @Override
                    public void needOpenUserProfile(final int n) {
                        if (n < 0) {
                            final Bundle bundle = new Bundle();
                            bundle.putInt("chat_id", -n);
                            if (MessagesController.getInstance(ChannelAdminLogActivity.this.currentAccount).checkCanOpenChat(bundle, ChannelAdminLogActivity.this)) {
                                ChannelAdminLogActivity.this.presentFragment(new ChatActivity(bundle), true);
                            }
                        }
                        else if (n != UserConfig.getInstance(ChannelAdminLogActivity.this.currentAccount).getClientUserId()) {
                            final Bundle bundle2 = new Bundle();
                            bundle2.putInt("user_id", n);
                            ChannelAdminLogActivity.this.addCanBanUser(bundle2, n);
                            final ProfileActivity profileActivity = new ProfileActivity(bundle2);
                            profileActivity.setPlayProfileAnimation(false);
                            ChannelAdminLogActivity.this.presentFragment(profileActivity);
                        }
                    }
                });
            }
            else if (n == 2) {
                o = new ChatUnreadCell(this.mContext);
            }
            else if (n == 3) {
                o = new BotHelpCell(this.mContext);
                ((BotHelpCell)o).setDelegate((BotHelpCell.BotHelpCellDelegate)new _$$Lambda$ChannelAdminLogActivity$ChatActivityAdapter$17meO0dSqjA1BWDg9Mv1dvdogDs(this));
            }
            else if (n == 4) {
                o = new ChatLoadingCell(this.mContext);
            }
            else {
                o = null;
            }
            ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder((View)o);
        }
        
        @Override
        public void onViewAttachedToWindow(final ViewHolder viewHolder) {
            final View itemView = viewHolder.itemView;
            if (itemView instanceof ChatMessageCell) {
                final ChatMessageCell chatMessageCell = (ChatMessageCell)itemView;
                chatMessageCell.getMessageObject();
                chatMessageCell.setBackgroundDrawable((Drawable)null);
                chatMessageCell.setCheckPressed(true, false);
                chatMessageCell.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
                    public boolean onPreDraw() {
                        chatMessageCell.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
                        final int measuredHeight = ChannelAdminLogActivity.this.chatListView.getMeasuredHeight();
                        final int top = chatMessageCell.getTop();
                        chatMessageCell.getBottom();
                        int n;
                        if (top >= 0) {
                            n = 0;
                        }
                        else {
                            n = -top;
                        }
                        int measuredHeight2;
                        if ((measuredHeight2 = chatMessageCell.getMeasuredHeight()) > measuredHeight) {
                            measuredHeight2 = n + measuredHeight;
                        }
                        chatMessageCell.setVisiblePart(n, measuredHeight2 - n);
                        return true;
                    }
                });
                chatMessageCell.setHighlighted(false);
            }
        }
        
        public void updateRowWithMessageObject(final MessageObject o) {
            final int index = ChannelAdminLogActivity.this.messages.indexOf(o);
            if (index == -1) {
                return;
            }
            this.notifyItemChanged(this.messagesStartRow + ChannelAdminLogActivity.this.messages.size() - index - 1);
        }
        
        public void updateRows() {
            this.rowCount = 0;
            if (!ChannelAdminLogActivity.this.messages.isEmpty()) {
                if (!ChannelAdminLogActivity.this.endReached) {
                    this.loadingUpRow = this.rowCount++;
                }
                else {
                    this.loadingUpRow = -1;
                }
                final int rowCount = this.rowCount;
                this.messagesStartRow = rowCount;
                this.rowCount = rowCount + ChannelAdminLogActivity.this.messages.size();
                this.messagesEndRow = this.rowCount;
            }
            else {
                this.loadingUpRow = -1;
                this.messagesStartRow = -1;
                this.messagesEndRow = -1;
            }
        }
    }
}
