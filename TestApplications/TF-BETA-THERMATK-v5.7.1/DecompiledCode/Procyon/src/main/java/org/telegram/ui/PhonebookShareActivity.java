// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.View$MeasureSpec;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ImageView$ScaleType;
import android.widget.ImageView;
import org.telegram.ui.Components.CheckBoxSquare;
import org.telegram.ui.Cells.DividerCell;
import android.view.ViewGroup;
import org.telegram.ui.Cells.EmptyCell;
import android.content.res.Configuration;
import android.widget.Toast;
import android.content.ClipData;
import org.telegram.messenger.ApplicationLoader;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import java.util.Locale;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.FileLog;
import android.content.Intent;
import org.telegram.ui.Cells.ShadowSectionCell;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.messenger.LocaleController;
import android.view.View$OnClickListener;
import android.text.TextUtils$TruncateAt;
import android.view.animation.LayoutAnimationController;
import org.telegram.ui.Components.LayoutHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Canvas;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.ui.ActionBar.ActionBar;
import android.view.ViewTreeObserver$OnPreDrawListener;
import org.telegram.ui.ActionBar.ActionBarLayout;
import android.provider.ContactsContract$Contacts;
import java.io.File;
import android.net.Uri;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.AndroidUtilities;
import java.util.ArrayList;
import android.widget.TextView;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import org.telegram.tgnet.TLRPC;
import android.widget.FrameLayout;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.ActionBar.BaseFragment;

public class PhonebookShareActivity extends BaseFragment
{
    private ListAdapter adapter;
    private BackupImageView avatarImage;
    private FrameLayout bottomLayout;
    private TLRPC.User currentUser;
    private PhonebookSelectActivity.PhonebookSelectActivityDelegate delegate;
    private int detailRow;
    private int emptyRow;
    private int extraHeight;
    private View extraHeightView;
    private boolean isImport;
    private LinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private TextView nameTextView;
    private ArrayList<AndroidUtilities.VcardItem> other;
    private int overscrollRow;
    private int phoneDividerRow;
    private int phoneEndRow;
    private int phoneStartRow;
    private ArrayList<AndroidUtilities.VcardItem> phones;
    private int rowCount;
    private View shadowView;
    private TextView shareTextView;
    private int vcardEndRow;
    private int vcardStartRow;
    
    public PhonebookShareActivity(final ContactsController.Contact contact, final Uri uri, final File file, String string) {
        this.other = new ArrayList<AndroidUtilities.VcardItem>();
        this.phones = new ArrayList<AndroidUtilities.VcardItem>();
        final ArrayList<AndroidUtilities.VcardItem> list = new ArrayList<AndroidUtilities.VcardItem>();
        ArrayList<TLRPC.User> list2;
        if (uri != null) {
            list2 = AndroidUtilities.loadVCardFromStream(uri, super.currentAccount, false, list, string);
        }
        else if (file != null) {
            list2 = AndroidUtilities.loadVCardFromStream(Uri.fromFile(file), super.currentAccount, false, list, string);
            file.delete();
            this.isImport = true;
        }
        else {
            final String key = contact.key;
            if (key != null) {
                list2 = AndroidUtilities.loadVCardFromStream(Uri.withAppendedPath(ContactsContract$Contacts.CONTENT_VCARD_URI, key), super.currentAccount, true, list, string);
            }
            else {
                this.currentUser = contact.user;
                final AndroidUtilities.VcardItem e = new AndroidUtilities.VcardItem();
                e.type = 0;
                final ArrayList<String> vcardData = e.vcardData;
                final StringBuilder sb = new StringBuilder();
                sb.append("TEL;MOBILE:+");
                sb.append(this.currentUser.phone);
                string = sb.toString();
                vcardData.add(e.fullData = string);
                this.phones.add(e);
                list2 = null;
            }
        }
        if (list2 != null) {
        Label_0335:
            for (int i = 0; i < list.size(); ++i) {
                final AndroidUtilities.VcardItem vcardItem = list.get(i);
                if (vcardItem.type == 0) {
                    int j = 0;
                    while (true) {
                        while (j < this.phones.size()) {
                            if (this.phones.get(j).getValue(false).equals(vcardItem.getValue(false))) {
                                final boolean b = true;
                                if (b) {
                                    vcardItem.checked = false;
                                    continue Label_0335;
                                }
                                this.phones.add(vcardItem);
                                continue Label_0335;
                            }
                            else {
                                ++j;
                            }
                        }
                        final boolean b = false;
                        continue;
                    }
                }
                this.other.add(vcardItem);
            }
            if (list2 != null && !list2.isEmpty()) {
                this.currentUser = list2.get(0);
                if (contact != null) {
                    final TLRPC.User user = contact.user;
                    if (user != null) {
                        this.currentUser.photo = user.photo;
                    }
                }
            }
        }
    }
    
    private void fixLayout() {
        final View fragmentView = super.fragmentView;
        if (fragmentView == null) {
            return;
        }
        fragmentView.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
            public boolean onPreDraw() {
                if (PhonebookShareActivity.this.fragmentView != null) {
                    PhonebookShareActivity.this.needLayout();
                    PhonebookShareActivity.this.fragmentView.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
                }
                return true;
            }
        });
    }
    
    private void needLayout() {
        final boolean occupyStatusBar = super.actionBar.getOccupyStatusBar();
        final int n = 0;
        int statusBarHeight;
        if (occupyStatusBar) {
            statusBarHeight = AndroidUtilities.statusBarHeight;
        }
        else {
            statusBarHeight = 0;
        }
        final int topMargin = statusBarHeight + ActionBar.getCurrentActionBarHeight();
        final RecyclerListView listView = this.listView;
        if (listView != null) {
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)listView.getLayoutParams();
            if (layoutParams.topMargin != topMargin) {
                layoutParams.topMargin = topMargin;
                this.listView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                this.extraHeightView.setTranslationY((float)topMargin);
            }
        }
        if (this.avatarImage != null) {
            final float scaleY = this.extraHeight / (float)AndroidUtilities.dp(88.0f);
            this.extraHeightView.setScaleY(scaleY);
            this.shadowView.setTranslationY((float)(topMargin + this.extraHeight));
            final BackupImageView avatarImage = this.avatarImage;
            final float n2 = (18.0f * scaleY + 42.0f) / 42.0f;
            avatarImage.setScaleX(n2);
            this.avatarImage.setScaleY(n2);
            int statusBarHeight2 = n;
            if (super.actionBar.getOccupyStatusBar()) {
                statusBarHeight2 = AndroidUtilities.statusBarHeight;
            }
            final float n3 = (float)statusBarHeight2;
            final float n4 = ActionBar.getCurrentActionBarHeight() / 2.0f;
            final float density = AndroidUtilities.density;
            this.avatarImage.setTranslationX(-AndroidUtilities.dp(47.0f) * scaleY);
            final BackupImageView avatarImage2 = this.avatarImage;
            final double n5 = n3 + n4 * (scaleY + 1.0f) - 21.0f * density + density * 27.0f * scaleY;
            avatarImage2.setTranslationY((float)Math.ceil(n5));
            this.nameTextView.setTranslationX(AndroidUtilities.density * -21.0f * scaleY);
            this.nameTextView.setTranslationY((float)Math.floor(n5) - (float)Math.ceil(AndroidUtilities.density) + (float)Math.floor(AndroidUtilities.density * 7.0f * scaleY));
            final TextView nameTextView = this.nameTextView;
            final float n6 = scaleY * 0.12f + 1.0f;
            nameTextView.setScaleX(n6);
            this.nameTextView.setScaleY(n6);
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackgroundColor(Theme.getColor("avatar_backgroundActionBarBlue"));
        super.actionBar.setItemsBackgroundColor(Theme.getColor("avatar_actionBarSelectorBlue"), false);
        super.actionBar.setItemsColor(Theme.getColor("avatar_actionBarIconBlue"), false);
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAddToContainer(false);
        this.extraHeight = 88;
        if (AndroidUtilities.isTablet()) {
            super.actionBar.setOccupyStatusBar(false);
        }
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    PhonebookShareActivity.this.finishFragment();
                }
            }
        });
        (super.fragmentView = (View)new FrameLayout(context) {
            protected boolean drawChild(final Canvas canvas, final View view, final long n) {
                if (view == PhonebookShareActivity.this.listView) {
                    final boolean drawChild = super.drawChild(canvas, view, n);
                    if (PhonebookShareActivity.this.parentLayout != null) {
                        final int childCount = this.getChildCount();
                        final int n2 = 0;
                        int n3 = 0;
                        int measuredHeight;
                        while (true) {
                            measuredHeight = n2;
                            if (n3 >= childCount) {
                                break;
                            }
                            final View child = this.getChildAt(n3);
                            if (child != view) {
                                if (child instanceof ActionBar && child.getVisibility() == 0) {
                                    measuredHeight = n2;
                                    if (((ActionBar)child).getCastShadows()) {
                                        measuredHeight = child.getMeasuredHeight();
                                        break;
                                    }
                                    break;
                                }
                            }
                            ++n3;
                        }
                        PhonebookShareActivity.this.parentLayout.drawHeaderShadow(canvas, measuredHeight);
                    }
                    return drawChild;
                }
                return super.drawChild(canvas, view, n);
            }
        }).setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        (this.listView = new RecyclerListView(context)).setVerticalScrollBarEnabled(false);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new LinearLayoutManager(context, 1, false) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        }));
        this.listView.setGlowColor(Theme.getColor("avatar_backgroundActionBarBlue"));
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 48.0f));
        this.listView.setAdapter(new ListAdapter(context));
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation((LayoutAnimationController)null);
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$PhonebookShareActivity$87T4KO49pRqiUUhu1YF8Uwv0ffg(this));
        this.listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)new _$$Lambda$PhonebookShareActivity$ih_UP_iiSTWRH840i078MY0XuHs(this));
        frameLayout.addView((View)super.actionBar);
        (this.extraHeightView = new View(context)).setPivotY(0.0f);
        this.extraHeightView.setBackgroundColor(Theme.getColor("avatar_backgroundActionBarBlue"));
        frameLayout.addView(this.extraHeightView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 88.0f));
        (this.shadowView = new View(context)).setBackgroundResource(2131165407);
        frameLayout.addView(this.shadowView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 3.0f));
        (this.avatarImage = new BackupImageView(context)).setRoundRadius(AndroidUtilities.dp(21.0f));
        this.avatarImage.setPivotX(0.0f);
        this.avatarImage.setPivotY(0.0f);
        frameLayout.addView((View)this.avatarImage, (ViewGroup$LayoutParams)LayoutHelper.createFrame(42, 42.0f, 51, 64.0f, 0.0f, 0.0f, 0.0f));
        (this.nameTextView = new TextView(context)).setTextColor(Theme.getColor("profile_title"));
        this.nameTextView.setTextSize(1, 18.0f);
        this.nameTextView.setLines(1);
        this.nameTextView.setMaxLines(1);
        this.nameTextView.setSingleLine(true);
        this.nameTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.nameTextView.setGravity(3);
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.nameTextView.setPivotX(0.0f);
        this.nameTextView.setPivotY(0.0f);
        frameLayout.addView((View)this.nameTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, 118.0f, 8.0f, 10.0f, 0.0f));
        this.needLayout();
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, int top, int n) {
                if (((RecyclerView.LayoutManager)PhonebookShareActivity.this.layoutManager).getItemCount() == 0) {
                    return;
                }
                top = 0;
                n = 0;
                final View child = recyclerView.getChildAt(0);
                if (child != null) {
                    if (PhonebookShareActivity.this.layoutManager.findFirstVisibleItemPosition() == 0) {
                        final int dp = AndroidUtilities.dp(88.0f);
                        top = n;
                        if (child.getTop() < 0) {
                            top = child.getTop();
                        }
                        top += dp;
                    }
                    if (PhonebookShareActivity.this.extraHeight != top) {
                        PhonebookShareActivity.this.extraHeight = top;
                        PhonebookShareActivity.this.needLayout();
                    }
                }
            }
        });
        (this.bottomLayout = new FrameLayout(context)).setBackgroundDrawable(Theme.createSelectorWithBackgroundDrawable(Theme.getColor("passport_authorizeBackground"), Theme.getColor("passport_authorizeBackgroundSelected")));
        frameLayout.addView((View)this.bottomLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 80));
        this.bottomLayout.setOnClickListener((View$OnClickListener)new _$$Lambda$PhonebookShareActivity$r6QmJiqDVDqQ_a81o_t3bySVxWU(this));
        (this.shareTextView = new TextView(context)).setCompoundDrawablePadding(AndroidUtilities.dp(8.0f));
        this.shareTextView.setTextColor(Theme.getColor("passport_authorizeText"));
        if (this.isImport) {
            this.shareTextView.setText((CharSequence)LocaleController.getString("AddContactChat", 2131558568));
        }
        else {
            this.shareTextView.setText((CharSequence)LocaleController.getString("ContactShare", 2131559146));
        }
        this.shareTextView.setTextSize(1, 14.0f);
        this.shareTextView.setGravity(17);
        this.shareTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.bottomLayout.addView((View)this.shareTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 17));
        final View view = new View(context);
        view.setBackgroundResource(2131165408);
        frameLayout.addView(view, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 3.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
        final AvatarDrawable avatarDrawable = new AvatarDrawable();
        avatarDrawable.setProfile(true);
        final TLRPC.User currentUser = this.currentUser;
        avatarDrawable.setInfo(5, currentUser.first_name, currentUser.last_name, false);
        avatarDrawable.setColor(Theme.getColor("avatar_backgroundInProfileBlue"));
        this.avatarImage.setImage(ImageLocation.getForUser(this.currentUser, false), "50_50", avatarDrawable, this.currentUser);
        final TextView nameTextView = this.nameTextView;
        final TLRPC.User currentUser2 = this.currentUser;
        nameTextView.setText((CharSequence)ContactsController.formatName(currentUser2.first_name, currentUser2.last_name));
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { TextCheckBoxCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.shareTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "passport_authorizeText"), new ThemeDescription((View)this.bottomLayout, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "passport_authorizeBackground"), new ThemeDescription((View)this.bottomLayout, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "passport_authorizeBackgroundSelected"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckBoxCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckBoxCell.class }, null, null, null, "checkboxSquareUnchecked"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckBoxCell.class }, null, null, null, "checkboxSquareDisabled"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckBoxCell.class }, null, null, null, "checkboxSquareBackground"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckBoxCell.class }, null, null, null, "checkboxSquareCheck") };
    }
    
    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.fixLayout();
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        if (this.currentUser == null) {
            return false;
        }
        this.rowCount = 0;
        this.overscrollRow = this.rowCount++;
        this.emptyRow = this.rowCount++;
        if (this.phones.isEmpty()) {
            this.phoneStartRow = -1;
            this.phoneEndRow = -1;
        }
        else {
            final int rowCount = this.rowCount;
            this.phoneStartRow = rowCount;
            this.rowCount = rowCount + this.phones.size();
            this.phoneEndRow = this.rowCount;
        }
        if (this.other.isEmpty()) {
            this.phoneDividerRow = -1;
            this.vcardStartRow = -1;
            this.vcardEndRow = -1;
        }
        else {
            if (this.phones.isEmpty()) {
                this.phoneDividerRow = -1;
            }
            else {
                this.phoneDividerRow = this.rowCount++;
            }
            final int rowCount2 = this.rowCount;
            this.vcardStartRow = rowCount2;
            this.rowCount = rowCount2 + this.other.size();
            this.vcardEndRow = this.rowCount;
        }
        this.detailRow = this.rowCount++;
        return true;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        this.fixLayout();
    }
    
    public void setDelegate(final PhonebookSelectActivity.PhonebookSelectActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            return PhonebookShareActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == PhonebookShareActivity.this.emptyRow || n == PhonebookShareActivity.this.overscrollRow) {
                return 0;
            }
            if ((n >= PhonebookShareActivity.this.phoneStartRow && n < PhonebookShareActivity.this.phoneEndRow) || (n >= PhonebookShareActivity.this.vcardStartRow && n < PhonebookShareActivity.this.vcardEndRow)) {
                return 1;
            }
            if (n == PhonebookShareActivity.this.phoneDividerRow) {
                return 2;
            }
            if (n == PhonebookShareActivity.this.detailRow) {
                return 3;
            }
            return 2;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int adapterPosition = viewHolder.getAdapterPosition();
            return (adapterPosition >= PhonebookShareActivity.this.phoneStartRow && adapterPosition < PhonebookShareActivity.this.phoneEndRow) || (adapterPosition >= PhonebookShareActivity.this.vcardStartRow && adapterPosition < PhonebookShareActivity.this.vcardEndRow);
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    final TextCheckBoxCell textCheckBoxCell = (TextCheckBoxCell)viewHolder.itemView;
                    final int access$1100 = PhonebookShareActivity.this.phoneStartRow;
                    int n2 = 0;
                    AndroidUtilities.VcardItem vcardItem;
                    if (n >= access$1100 && n < PhonebookShareActivity.this.phoneEndRow) {
                        vcardItem = PhonebookShareActivity.this.phones.get(n - PhonebookShareActivity.this.phoneStartRow);
                        if (n == PhonebookShareActivity.this.phoneStartRow) {
                            n2 = 2131165791;
                            vcardItem = vcardItem;
                        }
                    }
                    else {
                        final AndroidUtilities.VcardItem vcardItem2 = vcardItem = PhonebookShareActivity.this.other.get(n - PhonebookShareActivity.this.vcardStartRow);
                        if (n == PhonebookShareActivity.this.vcardStartRow) {
                            n2 = 2131165786;
                            vcardItem = vcardItem2;
                        }
                    }
                    textCheckBoxCell.setVCardItem(vcardItem, n2);
                }
            }
            else if (n == PhonebookShareActivity.this.overscrollRow) {
                ((EmptyCell)viewHolder.itemView).setHeight(AndroidUtilities.dp(88.0f));
            }
            else {
                ((EmptyCell)viewHolder.itemView).setHeight(AndroidUtilities.dp(16.0f));
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            o = null;
                        }
                        else {
                            o = new ShadowSectionCell(this.mContext);
                            ((View)o).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                        }
                    }
                    else {
                        o = new DividerCell(this.mContext);
                        ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        ((View)o).setPadding(AndroidUtilities.dp(72.0f), AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f));
                    }
                }
                else {
                    o = new TextCheckBoxCell(this.mContext);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                }
            }
            else {
                o = new EmptyCell(this.mContext);
                ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder((View)o);
        }
    }
    
    public class TextCheckBoxCell extends FrameLayout
    {
        private CheckBoxSquare checkBox;
        private ImageView imageView;
        private TextView textView;
        private TextView valueTextView;
        
        public TextCheckBoxCell(final Context context) {
            super(context);
            (this.textView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.textView.setTextSize(1, 16.0f);
            this.textView.setSingleLine(false);
            final TextView textView = this.textView;
            final boolean isRTL = LocaleController.isRTL;
            final int n = 5;
            int n2;
            if (isRTL) {
                n2 = 5;
            }
            else {
                n2 = 3;
            }
            textView.setGravity(n2 | 0x30);
            this.textView.setEllipsize(TextUtils$TruncateAt.END);
            final TextView textView2 = this.textView;
            int n3;
            if (LocaleController.isRTL) {
                n3 = 5;
            }
            else {
                n3 = 3;
            }
            float n5;
            if (LocaleController.isRTL) {
                int n4;
                if (PhonebookShareActivity.this.isImport) {
                    n4 = 17;
                }
                else {
                    n4 = 64;
                }
                n5 = (float)n4;
            }
            else {
                n5 = 71.0f;
            }
            float n6;
            if (LocaleController.isRTL) {
                n6 = 71.0f;
            }
            else {
                int n7;
                if (PhonebookShareActivity.this.isImport) {
                    n7 = 17;
                }
                else {
                    n7 = 64;
                }
                n6 = (float)n7;
            }
            this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n3 | 0x30, n5, 10.0f, n6, 0.0f));
            (this.valueTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
            this.valueTextView.setTextSize(1, 13.0f);
            this.valueTextView.setLines(1);
            this.valueTextView.setMaxLines(1);
            this.valueTextView.setSingleLine(true);
            final TextView valueTextView = this.valueTextView;
            int gravity;
            if (LocaleController.isRTL) {
                gravity = 5;
            }
            else {
                gravity = 3;
            }
            valueTextView.setGravity(gravity);
            final TextView valueTextView2 = this.valueTextView;
            int n8;
            if (LocaleController.isRTL) {
                n8 = 5;
            }
            else {
                n8 = 3;
            }
            float n10;
            if (LocaleController.isRTL) {
                int n9;
                if (PhonebookShareActivity.this.isImport) {
                    n9 = 17;
                }
                else {
                    n9 = 64;
                }
                n10 = (float)n9;
            }
            else {
                n10 = 71.0f;
            }
            float n11;
            if (LocaleController.isRTL) {
                n11 = 71.0f;
            }
            else {
                int n12;
                if (PhonebookShareActivity.this.isImport) {
                    n12 = 17;
                }
                else {
                    n12 = 64;
                }
                n11 = (float)n12;
            }
            this.addView((View)valueTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n8, n10, 35.0f, n11, 0.0f));
            (this.imageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
            this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), PorterDuff$Mode.MULTIPLY));
            final ImageView imageView = this.imageView;
            int n13;
            if (LocaleController.isRTL) {
                n13 = 5;
            }
            else {
                n13 = 3;
            }
            float n14;
            if (LocaleController.isRTL) {
                n14 = 0.0f;
            }
            else {
                n14 = 16.0f;
            }
            float n15;
            if (LocaleController.isRTL) {
                n15 = 16.0f;
            }
            else {
                n15 = 0.0f;
            }
            this.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n13 | 0x30, n14, 20.0f, n15, 0.0f));
            if (!PhonebookShareActivity.this.isImport) {
                (this.checkBox = new CheckBoxSquare(context, false)).setDuplicateParentStateEnabled(false);
                this.checkBox.setFocusable(false);
                this.checkBox.setFocusableInTouchMode(false);
                this.checkBox.setClickable(false);
                final CheckBoxSquare checkBox = this.checkBox;
                int n16 = n;
                if (LocaleController.isRTL) {
                    n16 = 3;
                }
                this.addView((View)checkBox, (ViewGroup$LayoutParams)LayoutHelper.createFrame(18, 18.0f, n16 | 0x10, 19.0f, 0.0f, 19.0f, 0.0f));
            }
        }
        
        public void invalidate() {
            super.invalidate();
            final CheckBoxSquare checkBox = this.checkBox;
            if (checkBox != null) {
                checkBox.invalidate();
            }
        }
        
        public boolean isChecked() {
            final CheckBoxSquare checkBox = this.checkBox;
            return checkBox != null && checkBox.isChecked();
        }
        
        protected void onLayout(final boolean b, int n, final int n2, final int n3, final int n4) {
            super.onLayout(b, n, n2, n3, n4);
            n = this.textView.getMeasuredHeight() + AndroidUtilities.dp(13.0f);
            final TextView valueTextView = this.valueTextView;
            valueTextView.layout(valueTextView.getLeft(), n, this.valueTextView.getRight(), this.valueTextView.getMeasuredHeight() + n);
        }
        
        protected void onMeasure(final int n, final int n2) {
            this.measureChildWithMargins((View)this.textView, n, 0, n2, 0);
            this.measureChildWithMargins((View)this.valueTextView, n, 0, n2, 0);
            this.measureChildWithMargins((View)this.imageView, n, 0, n2, 0);
            final CheckBoxSquare checkBox = this.checkBox;
            if (checkBox != null) {
                this.measureChildWithMargins((View)checkBox, n, 0, n2, 0);
            }
            this.setMeasuredDimension(View$MeasureSpec.getSize(n), Math.max(AndroidUtilities.dp(64.0f), this.textView.getMeasuredHeight() + this.valueTextView.getMeasuredHeight() + AndroidUtilities.dp(20.0f)));
        }
        
        public void setChecked(final boolean b) {
            final CheckBoxSquare checkBox = this.checkBox;
            if (checkBox != null) {
                checkBox.setChecked(b, true);
            }
        }
        
        public void setVCardItem(final AndroidUtilities.VcardItem vcardItem, final int imageResource) {
            this.textView.setText((CharSequence)vcardItem.getValue(true));
            this.valueTextView.setText((CharSequence)vcardItem.getType());
            final CheckBoxSquare checkBox = this.checkBox;
            if (checkBox != null) {
                checkBox.setChecked(vcardItem.checked, false);
            }
            if (imageResource != 0) {
                this.imageView.setImageResource(imageResource);
            }
            else {
                this.imageView.setImageDrawable((Drawable)null);
            }
        }
    }
}
