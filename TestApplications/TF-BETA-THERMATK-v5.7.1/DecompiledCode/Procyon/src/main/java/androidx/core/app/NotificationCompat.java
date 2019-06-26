// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.app;

import android.app.Notification$Action$Builder;
import android.app.Notification$Action;
import android.app.Notification$Builder;
import android.app.Notification$MessagingStyle$Message;
import android.app.Notification$MessagingStyle;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;
import androidx.core.text.BidiFormatter;
import android.content.res.ColorStateList;
import android.text.style.TextAppearanceSpan;
import android.text.TextUtils;
import java.util.List;
import android.app.Notification$InboxStyle;
import android.media.AudioAttributes$Builder;
import android.net.Uri;
import android.content.res.Resources;
import androidx.core.R$dimen;
import android.graphics.Bitmap;
import android.content.Context;
import android.widget.RemoteViews;
import android.app.Notification$BigTextStyle;
import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import android.app.PendingIntent;
import android.os.Build$VERSION;
import android.os.Bundle;
import android.app.Notification;

public class NotificationCompat
{
    public static Bundle getExtras(final Notification notification) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT >= 19) {
            return notification.extras;
        }
        if (sdk_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification);
        }
        return null;
    }
    
    public static class Action
    {
        public PendingIntent actionIntent;
        public int icon;
        private boolean mAllowGeneratedReplies;
        private final RemoteInput[] mDataOnlyRemoteInputs;
        final Bundle mExtras;
        private final RemoteInput[] mRemoteInputs;
        private final int mSemanticAction;
        boolean mShowsUserInterface;
        public CharSequence title;
        
        public Action(final int n, final CharSequence charSequence, final PendingIntent pendingIntent) {
            this(n, charSequence, pendingIntent, new Bundle(), null, null, true, 0, true);
        }
        
        Action(final int icon, final CharSequence charSequence, final PendingIntent actionIntent, Bundle mExtras, final RemoteInput[] mRemoteInputs, final RemoteInput[] mDataOnlyRemoteInputs, final boolean mAllowGeneratedReplies, final int mSemanticAction, final boolean mShowsUserInterface) {
            this.mShowsUserInterface = true;
            this.icon = icon;
            this.title = NotificationCompat.Builder.limitCharSequenceLength(charSequence);
            this.actionIntent = actionIntent;
            if (mExtras == null) {
                mExtras = new Bundle();
            }
            this.mExtras = mExtras;
            this.mRemoteInputs = mRemoteInputs;
            this.mDataOnlyRemoteInputs = mDataOnlyRemoteInputs;
            this.mAllowGeneratedReplies = mAllowGeneratedReplies;
            this.mSemanticAction = mSemanticAction;
            this.mShowsUserInterface = mShowsUserInterface;
        }
        
        public PendingIntent getActionIntent() {
            return this.actionIntent;
        }
        
        public boolean getAllowGeneratedReplies() {
            return this.mAllowGeneratedReplies;
        }
        
        public RemoteInput[] getDataOnlyRemoteInputs() {
            return this.mDataOnlyRemoteInputs;
        }
        
        public Bundle getExtras() {
            return this.mExtras;
        }
        
        public int getIcon() {
            return this.icon;
        }
        
        public RemoteInput[] getRemoteInputs() {
            return this.mRemoteInputs;
        }
        
        public int getSemanticAction() {
            return this.mSemanticAction;
        }
        
        public boolean getShowsUserInterface() {
            return this.mShowsUserInterface;
        }
        
        public CharSequence getTitle() {
            return this.title;
        }
        
        public static final class Builder
        {
            private boolean mAllowGeneratedReplies;
            private final Bundle mExtras;
            private final int mIcon;
            private final PendingIntent mIntent;
            private ArrayList<RemoteInput> mRemoteInputs;
            private int mSemanticAction;
            private boolean mShowsUserInterface;
            private final CharSequence mTitle;
            
            public Builder(final int n, final CharSequence charSequence, final PendingIntent pendingIntent) {
                this(n, charSequence, pendingIntent, new Bundle(), null, true, 0, true);
            }
            
            private Builder(final int mIcon, final CharSequence charSequence, final PendingIntent mIntent, final Bundle mExtras, final RemoteInput[] a, final boolean mAllowGeneratedReplies, final int mSemanticAction, final boolean mShowsUserInterface) {
                this.mAllowGeneratedReplies = true;
                this.mShowsUserInterface = true;
                this.mIcon = mIcon;
                this.mTitle = NotificationCompat.Builder.limitCharSequenceLength(charSequence);
                this.mIntent = mIntent;
                this.mExtras = mExtras;
                ArrayList<RemoteInput> mRemoteInputs;
                if (a == null) {
                    mRemoteInputs = null;
                }
                else {
                    mRemoteInputs = new ArrayList<RemoteInput>(Arrays.asList(a));
                }
                this.mRemoteInputs = mRemoteInputs;
                this.mAllowGeneratedReplies = mAllowGeneratedReplies;
                this.mSemanticAction = mSemanticAction;
                this.mShowsUserInterface = mShowsUserInterface;
            }
            
            public Builder addRemoteInput(final RemoteInput e) {
                if (this.mRemoteInputs == null) {
                    this.mRemoteInputs = new ArrayList<RemoteInput>();
                }
                this.mRemoteInputs.add(e);
                return this;
            }
            
            public Action build() {
                final ArrayList<RemoteInput> list = new ArrayList<RemoteInput>();
                final ArrayList<RemoteInput> list2 = new ArrayList<RemoteInput>();
                final ArrayList<RemoteInput> mRemoteInputs = this.mRemoteInputs;
                if (mRemoteInputs != null) {
                    for (final RemoteInput remoteInput : mRemoteInputs) {
                        if (remoteInput.isDataOnly()) {
                            list.add(remoteInput);
                        }
                        else {
                            list2.add(remoteInput);
                        }
                    }
                }
                final boolean empty = list.isEmpty();
                RemoteInput[] array = null;
                RemoteInput[] array2;
                if (empty) {
                    array2 = null;
                }
                else {
                    array2 = list.toArray(new RemoteInput[list.size()]);
                }
                if (!list2.isEmpty()) {
                    array = list2.toArray(new RemoteInput[list2.size()]);
                }
                return new Action(this.mIcon, this.mTitle, this.mIntent, this.mExtras, array, array2, this.mAllowGeneratedReplies, this.mSemanticAction, this.mShowsUserInterface);
            }
            
            public Builder setAllowGeneratedReplies(final boolean mAllowGeneratedReplies) {
                this.mAllowGeneratedReplies = mAllowGeneratedReplies;
                return this;
            }
            
            public Builder setSemanticAction(final int mSemanticAction) {
                this.mSemanticAction = mSemanticAction;
                return this;
            }
            
            public Builder setShowsUserInterface(final boolean mShowsUserInterface) {
                this.mShowsUserInterface = mShowsUserInterface;
                return this;
            }
        }
    }
    
    public static class BigTextStyle extends Style
    {
        private CharSequence mBigText;
        
        @Override
        public void apply(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build$VERSION.SDK_INT >= 16) {
                final Notification$BigTextStyle bigText = new Notification$BigTextStyle(notificationBuilderWithBuilderAccessor.getBuilder()).setBigContentTitle(super.mBigContentTitle).bigText(this.mBigText);
                if (super.mSummaryTextSet) {
                    bigText.setSummaryText(super.mSummaryText);
                }
            }
        }
        
        public BigTextStyle bigText(final CharSequence charSequence) {
            this.mBigText = NotificationCompat.Builder.limitCharSequenceLength(charSequence);
            return this;
        }
    }
    
    public static class Builder
    {
        public ArrayList<Action> mActions;
        int mBadgeIcon;
        RemoteViews mBigContentView;
        String mCategory;
        String mChannelId;
        int mColor;
        boolean mColorized;
        boolean mColorizedSet;
        CharSequence mContentInfo;
        PendingIntent mContentIntent;
        CharSequence mContentText;
        CharSequence mContentTitle;
        RemoteViews mContentView;
        public Context mContext;
        Bundle mExtras;
        PendingIntent mFullScreenIntent;
        int mGroupAlertBehavior;
        String mGroupKey;
        boolean mGroupSummary;
        RemoteViews mHeadsUpContentView;
        ArrayList<Action> mInvisibleActions;
        Bitmap mLargeIcon;
        boolean mLocalOnly;
        Notification mNotification;
        int mNumber;
        @Deprecated
        public ArrayList<String> mPeople;
        int mPriority;
        int mProgress;
        boolean mProgressIndeterminate;
        int mProgressMax;
        Notification mPublicVersion;
        CharSequence[] mRemoteInputHistory;
        String mShortcutId;
        boolean mShowWhen;
        String mSortKey;
        Style mStyle;
        CharSequence mSubText;
        RemoteViews mTickerView;
        long mTimeout;
        boolean mUseChronometer;
        int mVisibility;
        
        @Deprecated
        public Builder(final Context context) {
            this(context, null);
        }
        
        public Builder(final Context mContext, final String mChannelId) {
            this.mActions = new ArrayList<Action>();
            this.mInvisibleActions = new ArrayList<Action>();
            this.mShowWhen = true;
            this.mLocalOnly = false;
            this.mColor = 0;
            this.mVisibility = 0;
            this.mBadgeIcon = 0;
            this.mGroupAlertBehavior = 0;
            this.mNotification = new Notification();
            this.mContext = mContext;
            this.mChannelId = mChannelId;
            this.mNotification.when = System.currentTimeMillis();
            this.mNotification.audioStreamType = -1;
            this.mPriority = 0;
            this.mPeople = new ArrayList<String>();
        }
        
        protected static CharSequence limitCharSequenceLength(final CharSequence charSequence) {
            if (charSequence == null) {
                return charSequence;
            }
            CharSequence subSequence = charSequence;
            if (charSequence.length() > 5120) {
                subSequence = charSequence.subSequence(0, 5120);
            }
            return subSequence;
        }
        
        private Bitmap reduceLargeIconSize(final Bitmap bitmap) {
            Bitmap scaledBitmap = bitmap;
            if (bitmap != null) {
                if (Build$VERSION.SDK_INT >= 27) {
                    scaledBitmap = bitmap;
                }
                else {
                    final Resources resources = this.mContext.getResources();
                    final int dimensionPixelSize = resources.getDimensionPixelSize(R$dimen.compat_notification_large_icon_max_width);
                    final int dimensionPixelSize2 = resources.getDimensionPixelSize(R$dimen.compat_notification_large_icon_max_height);
                    if (bitmap.getWidth() <= dimensionPixelSize && bitmap.getHeight() <= dimensionPixelSize2) {
                        return bitmap;
                    }
                    final double v = dimensionPixelSize;
                    final double v2 = Math.max(1, bitmap.getWidth());
                    Double.isNaN(v);
                    Double.isNaN(v2);
                    final double a = v / v2;
                    final double v3 = dimensionPixelSize2;
                    final double v4 = Math.max(1, bitmap.getHeight());
                    Double.isNaN(v3);
                    Double.isNaN(v4);
                    final double min = Math.min(a, v3 / v4);
                    final double v5 = bitmap.getWidth();
                    Double.isNaN(v5);
                    final int n = (int)Math.ceil(v5 * min);
                    final double v6 = bitmap.getHeight();
                    Double.isNaN(v6);
                    scaledBitmap = Bitmap.createScaledBitmap(bitmap, n, (int)Math.ceil(v6 * min), true);
                }
            }
            return scaledBitmap;
        }
        
        private void setFlag(final int n, final boolean b) {
            if (b) {
                final Notification mNotification = this.mNotification;
                mNotification.flags |= n;
            }
            else {
                final Notification mNotification2 = this.mNotification;
                mNotification2.flags &= ~n;
            }
        }
        
        public Builder addAction(final int n, final CharSequence charSequence, final PendingIntent pendingIntent) {
            this.mActions.add(new Action(n, charSequence, pendingIntent));
            return this;
        }
        
        public Builder addAction(final Action e) {
            this.mActions.add(e);
            return this;
        }
        
        public Builder addPerson(final String e) {
            this.mPeople.add(e);
            return this;
        }
        
        public Notification build() {
            return new NotificationCompatBuilder(this).build();
        }
        
        public Builder extend(final Extender extender) {
            extender.extend(this);
            return this;
        }
        
        public int getColor() {
            return this.mColor;
        }
        
        public Bundle getExtras() {
            if (this.mExtras == null) {
                this.mExtras = new Bundle();
            }
            return this.mExtras;
        }
        
        public Builder setAutoCancel(final boolean b) {
            this.setFlag(16, b);
            return this;
        }
        
        public Builder setCategory(final String mCategory) {
            this.mCategory = mCategory;
            return this;
        }
        
        public Builder setChannelId(final String mChannelId) {
            this.mChannelId = mChannelId;
            return this;
        }
        
        public Builder setColor(final int mColor) {
            this.mColor = mColor;
            return this;
        }
        
        public Builder setContentIntent(final PendingIntent mContentIntent) {
            this.mContentIntent = mContentIntent;
            return this;
        }
        
        public Builder setContentText(final CharSequence charSequence) {
            this.mContentText = limitCharSequenceLength(charSequence);
            return this;
        }
        
        public Builder setContentTitle(final CharSequence charSequence) {
            this.mContentTitle = limitCharSequenceLength(charSequence);
            return this;
        }
        
        public Builder setDefaults(final int defaults) {
            final Notification mNotification = this.mNotification;
            mNotification.defaults = defaults;
            if ((defaults & 0x4) != 0x0) {
                mNotification.flags |= 0x1;
            }
            return this;
        }
        
        public Builder setDeleteIntent(final PendingIntent deleteIntent) {
            this.mNotification.deleteIntent = deleteIntent;
            return this;
        }
        
        public Builder setGroup(final String mGroupKey) {
            this.mGroupKey = mGroupKey;
            return this;
        }
        
        public Builder setGroupAlertBehavior(final int mGroupAlertBehavior) {
            this.mGroupAlertBehavior = mGroupAlertBehavior;
            return this;
        }
        
        public Builder setGroupSummary(final boolean mGroupSummary) {
            this.mGroupSummary = mGroupSummary;
            return this;
        }
        
        public Builder setLargeIcon(final Bitmap bitmap) {
            this.mLargeIcon = this.reduceLargeIconSize(bitmap);
            return this;
        }
        
        public Builder setLights(int ledARGB, final int ledOnMS, final int ledOffMS) {
            final Notification mNotification = this.mNotification;
            mNotification.ledARGB = ledARGB;
            mNotification.ledOnMS = ledOnMS;
            mNotification.ledOffMS = ledOffMS;
            if (mNotification.ledOnMS != 0 && mNotification.ledOffMS != 0) {
                ledARGB = 1;
            }
            else {
                ledARGB = 0;
            }
            final Notification mNotification2 = this.mNotification;
            mNotification2.flags = (ledARGB | (mNotification2.flags & 0xFFFFFFFE));
            return this;
        }
        
        public Builder setLocalOnly(final boolean mLocalOnly) {
            this.mLocalOnly = mLocalOnly;
            return this;
        }
        
        public Builder setNumber(final int mNumber) {
            this.mNumber = mNumber;
            return this;
        }
        
        public Builder setOngoing(final boolean b) {
            this.setFlag(2, b);
            return this;
        }
        
        public Builder setPriority(final int mPriority) {
            this.mPriority = mPriority;
            return this;
        }
        
        public Builder setProgress(final int mProgressMax, final int mProgress, final boolean mProgressIndeterminate) {
            this.mProgressMax = mProgressMax;
            this.mProgress = mProgress;
            this.mProgressIndeterminate = mProgressIndeterminate;
            return this;
        }
        
        public Builder setShortcutId(final String mShortcutId) {
            this.mShortcutId = mShortcutId;
            return this;
        }
        
        public Builder setShowWhen(final boolean mShowWhen) {
            this.mShowWhen = mShowWhen;
            return this;
        }
        
        public Builder setSmallIcon(final int icon) {
            this.mNotification.icon = icon;
            return this;
        }
        
        public Builder setSortKey(final String mSortKey) {
            this.mSortKey = mSortKey;
            return this;
        }
        
        public Builder setSound(final Uri sound, final int n) {
            final Notification mNotification = this.mNotification;
            mNotification.sound = sound;
            mNotification.audioStreamType = n;
            if (Build$VERSION.SDK_INT >= 21) {
                mNotification.audioAttributes = new AudioAttributes$Builder().setContentType(4).setLegacyStreamType(n).build();
            }
            return this;
        }
        
        public Builder setStyle(Style mStyle) {
            if (this.mStyle != mStyle) {
                this.mStyle = mStyle;
                mStyle = this.mStyle;
                if (mStyle != null) {
                    mStyle.setBuilder(this);
                }
            }
            return this;
        }
        
        public Builder setSubText(final CharSequence charSequence) {
            this.mSubText = limitCharSequenceLength(charSequence);
            return this;
        }
        
        public Builder setTicker(final CharSequence charSequence) {
            this.mNotification.tickerText = limitCharSequenceLength(charSequence);
            return this;
        }
        
        public Builder setVibrate(final long[] vibrate) {
            this.mNotification.vibrate = vibrate;
            return this;
        }
        
        public Builder setWhen(final long when) {
            this.mNotification.when = when;
            return this;
        }
    }
    
    public interface Extender
    {
        NotificationCompat.Builder extend(final NotificationCompat.Builder p0);
    }
    
    public static class InboxStyle extends Style
    {
        private ArrayList<CharSequence> mTexts;
        
        public InboxStyle() {
            this.mTexts = new ArrayList<CharSequence>();
        }
        
        public InboxStyle addLine(final CharSequence charSequence) {
            this.mTexts.add(NotificationCompat.Builder.limitCharSequenceLength(charSequence));
            return this;
        }
        
        @Override
        public void apply(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build$VERSION.SDK_INT >= 16) {
                final Notification$InboxStyle setBigContentTitle = new Notification$InboxStyle(notificationBuilderWithBuilderAccessor.getBuilder()).setBigContentTitle(super.mBigContentTitle);
                if (super.mSummaryTextSet) {
                    setBigContentTitle.setSummaryText(super.mSummaryText);
                }
                final Iterator<CharSequence> iterator = this.mTexts.iterator();
                while (iterator.hasNext()) {
                    setBigContentTitle.addLine((CharSequence)iterator.next());
                }
            }
        }
        
        public InboxStyle setBigContentTitle(final CharSequence charSequence) {
            super.mBigContentTitle = NotificationCompat.Builder.limitCharSequenceLength(charSequence);
            return this;
        }
        
        public InboxStyle setSummaryText(final CharSequence charSequence) {
            super.mSummaryText = NotificationCompat.Builder.limitCharSequenceLength(charSequence);
            super.mSummaryTextSet = true;
            return this;
        }
    }
    
    public static class MessagingStyle extends Style
    {
        private CharSequence mConversationTitle;
        private Boolean mIsGroupConversation;
        private final List<Message> mMessages;
        private Person mUser;
        
        @Deprecated
        public MessagingStyle(final CharSequence name) {
            this.mMessages = new ArrayList<Message>();
            final Person.Builder builder = new Person.Builder();
            builder.setName(name);
            this.mUser = builder.build();
        }
        
        private Message findLatestIncomingMessage() {
            for (int i = this.mMessages.size() - 1; i >= 0; --i) {
                final Message message = this.mMessages.get(i);
                if (message.getPerson() != null && !TextUtils.isEmpty(message.getPerson().getName())) {
                    return message;
                }
            }
            if (!this.mMessages.isEmpty()) {
                final List<Message> mMessages = this.mMessages;
                return mMessages.get(mMessages.size() - 1);
            }
            return null;
        }
        
        private boolean hasMessagesWithoutSender() {
            for (int i = this.mMessages.size() - 1; i >= 0; --i) {
                final Message message = this.mMessages.get(i);
                if (message.getPerson() != null && message.getPerson().getName() == null) {
                    return true;
                }
            }
            return false;
        }
        
        private TextAppearanceSpan makeFontColorSpan(final int n) {
            return new TextAppearanceSpan((String)null, 0, 0, ColorStateList.valueOf(n), (ColorStateList)null);
        }
        
        private CharSequence makeMessageLine(final Message message) {
            final BidiFormatter instance = BidiFormatter.getInstance();
            final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            final boolean b = Build$VERSION.SDK_INT >= 21;
            int n;
            if (b) {
                n = -16777216;
            }
            else {
                n = -1;
            }
            final Person person = message.getPerson();
            final String s = "";
            CharSequence name;
            if (person == null) {
                name = "";
            }
            else {
                name = message.getPerson().getName();
            }
            int color = n;
            CharSequence charSequence = name;
            if (TextUtils.isEmpty(name)) {
                final CharSequence name2 = this.mUser.getName();
                color = n;
                charSequence = name2;
                if (b) {
                    color = n;
                    charSequence = name2;
                    if (super.mBuilder.getColor() != 0) {
                        color = super.mBuilder.getColor();
                        charSequence = name2;
                    }
                }
            }
            final CharSequence unicodeWrap = instance.unicodeWrap(charSequence);
            spannableStringBuilder.append(unicodeWrap);
            spannableStringBuilder.setSpan((Object)this.makeFontColorSpan(color), spannableStringBuilder.length() - unicodeWrap.length(), spannableStringBuilder.length(), 33);
            CharSequence text;
            if (message.getText() == null) {
                text = s;
            }
            else {
                text = message.getText();
            }
            spannableStringBuilder.append((CharSequence)"  ").append(instance.unicodeWrap(text));
            return (CharSequence)spannableStringBuilder;
        }
        
        @Override
        public void addCompatExtras(final Bundle bundle) {
            super.addCompatExtras(bundle);
            bundle.putCharSequence("android.selfDisplayName", this.mUser.getName());
            bundle.putBundle("android.messagingStyleUser", this.mUser.toBundle());
            bundle.putCharSequence("android.hiddenConversationTitle", this.mConversationTitle);
            if (this.mConversationTitle != null && this.mIsGroupConversation) {
                bundle.putCharSequence("android.conversationTitle", this.mConversationTitle);
            }
            if (!this.mMessages.isEmpty()) {
                bundle.putParcelableArray("android.messages", (Parcelable[])Message.getBundleArrayForMessages(this.mMessages));
            }
            final Boolean mIsGroupConversation = this.mIsGroupConversation;
            if (mIsGroupConversation != null) {
                bundle.putBoolean("android.isGroupConversation", (boolean)mIsGroupConversation);
            }
        }
        
        public MessagingStyle addMessage(final Message message) {
            this.mMessages.add(message);
            if (this.mMessages.size() > 25) {
                this.mMessages.remove(0);
            }
            return this;
        }
        
        public MessagingStyle addMessage(final CharSequence charSequence, final long n, final Person person) {
            this.addMessage(new Message(charSequence, n, person));
            return this;
        }
        
        @Override
        public void apply(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            this.setGroupConversation(this.isGroupConversation());
            final int sdk_INT = Build$VERSION.SDK_INT;
            if (sdk_INT >= 24) {
                Notification$MessagingStyle notification$MessagingStyle;
                if (sdk_INT >= 28) {
                    notification$MessagingStyle = new Notification$MessagingStyle(this.mUser.toAndroidPerson());
                }
                else {
                    notification$MessagingStyle = new Notification$MessagingStyle(this.mUser.getName());
                }
                if (this.mIsGroupConversation || Build$VERSION.SDK_INT >= 28) {
                    notification$MessagingStyle.setConversationTitle(this.mConversationTitle);
                }
                if (Build$VERSION.SDK_INT >= 28) {
                    notification$MessagingStyle.setGroupConversation((boolean)this.mIsGroupConversation);
                }
                for (final Message message : this.mMessages) {
                    Notification$MessagingStyle$Message notification$MessagingStyle$Message;
                    if (Build$VERSION.SDK_INT >= 28) {
                        final Person person = message.getPerson();
                        final CharSequence text = message.getText();
                        final long timestamp = message.getTimestamp();
                        android.app.Person androidPerson;
                        if (person == null) {
                            androidPerson = null;
                        }
                        else {
                            androidPerson = person.toAndroidPerson();
                        }
                        notification$MessagingStyle$Message = new Notification$MessagingStyle$Message(text, timestamp, androidPerson);
                    }
                    else {
                        CharSequence name;
                        if (message.getPerson() != null) {
                            name = message.getPerson().getName();
                        }
                        else {
                            name = null;
                        }
                        notification$MessagingStyle$Message = new Notification$MessagingStyle$Message(message.getText(), message.getTimestamp(), name);
                    }
                    if (message.getDataMimeType() != null) {
                        notification$MessagingStyle$Message.setData(message.getDataMimeType(), message.getDataUri());
                    }
                    notification$MessagingStyle.addMessage(notification$MessagingStyle$Message);
                }
                notification$MessagingStyle.setBuilder(notificationBuilderWithBuilderAccessor.getBuilder());
            }
            else {
                final Message latestIncomingMessage = this.findLatestIncomingMessage();
                if (this.mConversationTitle != null && this.mIsGroupConversation) {
                    notificationBuilderWithBuilderAccessor.getBuilder().setContentTitle(this.mConversationTitle);
                }
                else if (latestIncomingMessage != null) {
                    notificationBuilderWithBuilderAccessor.getBuilder().setContentTitle((CharSequence)"");
                    if (latestIncomingMessage.getPerson() != null) {
                        notificationBuilderWithBuilderAccessor.getBuilder().setContentTitle(latestIncomingMessage.getPerson().getName());
                    }
                }
                if (latestIncomingMessage != null) {
                    final Notification$Builder builder = notificationBuilderWithBuilderAccessor.getBuilder();
                    CharSequence contentText;
                    if (this.mConversationTitle != null) {
                        contentText = this.makeMessageLine(latestIncomingMessage);
                    }
                    else {
                        contentText = latestIncomingMessage.getText();
                    }
                    builder.setContentText(contentText);
                }
                if (Build$VERSION.SDK_INT >= 16) {
                    final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                    final boolean b = this.mConversationTitle != null || this.hasMessagesWithoutSender();
                    for (int i = this.mMessages.size() - 1; i >= 0; --i) {
                        final Message message2 = this.mMessages.get(i);
                        CharSequence charSequence;
                        if (b) {
                            charSequence = this.makeMessageLine(message2);
                        }
                        else {
                            charSequence = message2.getText();
                        }
                        if (i != this.mMessages.size() - 1) {
                            spannableStringBuilder.insert(0, (CharSequence)"\n");
                        }
                        spannableStringBuilder.insert(0, charSequence);
                    }
                    new Notification$BigTextStyle(notificationBuilderWithBuilderAccessor.getBuilder()).setBigContentTitle((CharSequence)null).bigText((CharSequence)spannableStringBuilder);
                }
            }
        }
        
        public List<Message> getMessages() {
            return this.mMessages;
        }
        
        public boolean isGroupConversation() {
            final NotificationCompat.Builder mBuilder = super.mBuilder;
            final boolean b = false;
            boolean b2 = false;
            if (mBuilder != null && mBuilder.mContext.getApplicationInfo().targetSdkVersion < 28 && this.mIsGroupConversation == null) {
                if (this.mConversationTitle != null) {
                    b2 = true;
                }
                return b2;
            }
            final Boolean mIsGroupConversation = this.mIsGroupConversation;
            boolean booleanValue = b;
            if (mIsGroupConversation != null) {
                booleanValue = mIsGroupConversation;
            }
            return booleanValue;
        }
        
        public MessagingStyle setConversationTitle(final CharSequence mConversationTitle) {
            this.mConversationTitle = mConversationTitle;
            return this;
        }
        
        public MessagingStyle setGroupConversation(final boolean b) {
            this.mIsGroupConversation = b;
            return this;
        }
        
        public static final class Message
        {
            private String mDataMimeType;
            private Uri mDataUri;
            private Bundle mExtras;
            private final Person mPerson;
            private final CharSequence mText;
            private final long mTimestamp;
            
            public Message(final CharSequence mText, final long mTimestamp, final Person mPerson) {
                this.mExtras = new Bundle();
                this.mText = mText;
                this.mTimestamp = mTimestamp;
                this.mPerson = mPerson;
            }
            
            static Bundle[] getBundleArrayForMessages(final List<Message> list) {
                final Bundle[] array = new Bundle[list.size()];
                for (int size = list.size(), i = 0; i < size; ++i) {
                    array[i] = list.get(i).toBundle();
                }
                return array;
            }
            
            private Bundle toBundle() {
                final Bundle bundle = new Bundle();
                final CharSequence mText = this.mText;
                if (mText != null) {
                    bundle.putCharSequence("text", mText);
                }
                bundle.putLong("time", this.mTimestamp);
                final Person mPerson = this.mPerson;
                if (mPerson != null) {
                    bundle.putCharSequence("sender", mPerson.getName());
                    if (Build$VERSION.SDK_INT >= 28) {
                        bundle.putParcelable("sender_person", (Parcelable)this.mPerson.toAndroidPerson());
                    }
                    else {
                        bundle.putBundle("person", this.mPerson.toBundle());
                    }
                }
                final String mDataMimeType = this.mDataMimeType;
                if (mDataMimeType != null) {
                    bundle.putString("type", mDataMimeType);
                }
                final Uri mDataUri = this.mDataUri;
                if (mDataUri != null) {
                    bundle.putParcelable("uri", (Parcelable)mDataUri);
                }
                final Bundle mExtras = this.mExtras;
                if (mExtras != null) {
                    bundle.putBundle("extras", mExtras);
                }
                return bundle;
            }
            
            public String getDataMimeType() {
                return this.mDataMimeType;
            }
            
            public Uri getDataUri() {
                return this.mDataUri;
            }
            
            public Person getPerson() {
                return this.mPerson;
            }
            
            public CharSequence getText() {
                return this.mText;
            }
            
            public long getTimestamp() {
                return this.mTimestamp;
            }
            
            public Message setData(final String mDataMimeType, final Uri mDataUri) {
                this.mDataMimeType = mDataMimeType;
                this.mDataUri = mDataUri;
                return this;
            }
        }
    }
    
    public abstract static class Style
    {
        CharSequence mBigContentTitle;
        protected NotificationCompat.Builder mBuilder;
        CharSequence mSummaryText;
        boolean mSummaryTextSet;
        
        public Style() {
            this.mSummaryTextSet = false;
        }
        
        public void addCompatExtras(final Bundle bundle) {
        }
        
        public abstract void apply(final NotificationBuilderWithBuilderAccessor p0);
        
        public RemoteViews makeBigContentView(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            return null;
        }
        
        public RemoteViews makeContentView(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            return null;
        }
        
        public RemoteViews makeHeadsUpContentView(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            return null;
        }
        
        public void setBuilder(NotificationCompat.Builder mBuilder) {
            if (this.mBuilder != mBuilder) {
                this.mBuilder = mBuilder;
                mBuilder = this.mBuilder;
                if (mBuilder != null) {
                    mBuilder.setStyle(this);
                }
            }
        }
    }
    
    public static final class WearableExtender implements Extender
    {
        private ArrayList<Action> mActions;
        private Bitmap mBackground;
        private String mBridgeTag;
        private int mContentActionIndex;
        private int mContentIcon;
        private int mContentIconGravity;
        private int mCustomContentHeight;
        private int mCustomSizePreset;
        private String mDismissalId;
        private PendingIntent mDisplayIntent;
        private int mFlags;
        private int mGravity;
        private int mHintScreenTimeout;
        private ArrayList<Notification> mPages;
        
        public WearableExtender() {
            this.mActions = new ArrayList<Action>();
            this.mFlags = 1;
            this.mPages = new ArrayList<Notification>();
            this.mContentIconGravity = 8388613;
            this.mContentActionIndex = -1;
            this.mCustomSizePreset = 0;
            this.mGravity = 80;
        }
        
        private static Notification$Action getActionFromActionCompat(final Action action) {
            final Notification$Action$Builder notification$Action$Builder = new Notification$Action$Builder(action.getIcon(), action.getTitle(), action.getActionIntent());
            Bundle bundle;
            if (action.getExtras() != null) {
                bundle = new Bundle(action.getExtras());
            }
            else {
                bundle = new Bundle();
            }
            bundle.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
            if (Build$VERSION.SDK_INT >= 24) {
                notification$Action$Builder.setAllowGeneratedReplies(action.getAllowGeneratedReplies());
            }
            notification$Action$Builder.addExtras(bundle);
            final RemoteInput[] remoteInputs = action.getRemoteInputs();
            if (remoteInputs != null) {
                final android.app.RemoteInput[] fromCompat = RemoteInput.fromCompat(remoteInputs);
                for (int length = fromCompat.length, i = 0; i < length; ++i) {
                    notification$Action$Builder.addRemoteInput(fromCompat[i]);
                }
            }
            return notification$Action$Builder.build();
        }
        
        public WearableExtender addAction(final Action e) {
            this.mActions.add(e);
            return this;
        }
        
        public WearableExtender clone() {
            final WearableExtender wearableExtender = new WearableExtender();
            wearableExtender.mActions = new ArrayList<Action>(this.mActions);
            wearableExtender.mFlags = this.mFlags;
            wearableExtender.mDisplayIntent = this.mDisplayIntent;
            wearableExtender.mPages = new ArrayList<Notification>(this.mPages);
            wearableExtender.mBackground = this.mBackground;
            wearableExtender.mContentIcon = this.mContentIcon;
            wearableExtender.mContentIconGravity = this.mContentIconGravity;
            wearableExtender.mContentActionIndex = this.mContentActionIndex;
            wearableExtender.mCustomSizePreset = this.mCustomSizePreset;
            wearableExtender.mCustomContentHeight = this.mCustomContentHeight;
            wearableExtender.mGravity = this.mGravity;
            wearableExtender.mHintScreenTimeout = this.mHintScreenTimeout;
            wearableExtender.mDismissalId = this.mDismissalId;
            wearableExtender.mBridgeTag = this.mBridgeTag;
            return wearableExtender;
        }
        
        @Override
        public NotificationCompat.Builder extend(final NotificationCompat.Builder builder) {
            final Bundle bundle = new Bundle();
            if (!this.mActions.isEmpty()) {
                if (Build$VERSION.SDK_INT >= 16) {
                    final ArrayList<Bundle> list = new ArrayList<Bundle>(this.mActions.size());
                    for (final Action action : this.mActions) {
                        final int sdk_INT = Build$VERSION.SDK_INT;
                        if (sdk_INT >= 20) {
                            list.add((Bundle)getActionFromActionCompat(action));
                        }
                        else {
                            if (sdk_INT < 16) {
                                continue;
                            }
                            list.add(NotificationCompatJellybean.getBundleForAction(action));
                        }
                    }
                    bundle.putParcelableArrayList("actions", (ArrayList)list);
                }
                else {
                    bundle.putParcelableArrayList("actions", (ArrayList)null);
                }
            }
            final int mFlags = this.mFlags;
            if (mFlags != 1) {
                bundle.putInt("flags", mFlags);
            }
            final PendingIntent mDisplayIntent = this.mDisplayIntent;
            if (mDisplayIntent != null) {
                bundle.putParcelable("displayIntent", (Parcelable)mDisplayIntent);
            }
            if (!this.mPages.isEmpty()) {
                final ArrayList<Notification> mPages = this.mPages;
                bundle.putParcelableArray("pages", (Parcelable[])mPages.toArray((Parcelable[])new Notification[mPages.size()]));
            }
            final Bitmap mBackground = this.mBackground;
            if (mBackground != null) {
                bundle.putParcelable("background", (Parcelable)mBackground);
            }
            final int mContentIcon = this.mContentIcon;
            if (mContentIcon != 0) {
                bundle.putInt("contentIcon", mContentIcon);
            }
            final int mContentIconGravity = this.mContentIconGravity;
            if (mContentIconGravity != 8388613) {
                bundle.putInt("contentIconGravity", mContentIconGravity);
            }
            final int mContentActionIndex = this.mContentActionIndex;
            if (mContentActionIndex != -1) {
                bundle.putInt("contentActionIndex", mContentActionIndex);
            }
            final int mCustomSizePreset = this.mCustomSizePreset;
            if (mCustomSizePreset != 0) {
                bundle.putInt("customSizePreset", mCustomSizePreset);
            }
            final int mCustomContentHeight = this.mCustomContentHeight;
            if (mCustomContentHeight != 0) {
                bundle.putInt("customContentHeight", mCustomContentHeight);
            }
            final int mGravity = this.mGravity;
            if (mGravity != 80) {
                bundle.putInt("gravity", mGravity);
            }
            final int mHintScreenTimeout = this.mHintScreenTimeout;
            if (mHintScreenTimeout != 0) {
                bundle.putInt("hintScreenTimeout", mHintScreenTimeout);
            }
            final String mDismissalId = this.mDismissalId;
            if (mDismissalId != null) {
                bundle.putString("dismissalId", mDismissalId);
            }
            final String mBridgeTag = this.mBridgeTag;
            if (mBridgeTag != null) {
                bundle.putString("bridgeTag", mBridgeTag);
            }
            builder.getExtras().putBundle("android.wearable.EXTENSIONS", bundle);
            return builder;
        }
        
        public WearableExtender setBridgeTag(final String mBridgeTag) {
            this.mBridgeTag = mBridgeTag;
            return this;
        }
        
        public WearableExtender setDismissalId(final String mDismissalId) {
            this.mDismissalId = mDismissalId;
            return this;
        }
    }
}
