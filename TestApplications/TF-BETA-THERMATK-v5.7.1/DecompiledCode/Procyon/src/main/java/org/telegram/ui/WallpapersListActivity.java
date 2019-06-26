// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.SpannableString;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.MediaController;
import org.telegram.ui.Components.CombinedDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.Intent;
import android.widget.TextView;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import android.graphics.drawable.Drawable;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.messenger.MessagesStorage;
import org.telegram.ui.ActionBar.ActionBarMenu;
import android.view.animation.LayoutAnimationController;
import android.graphics.Canvas;
import android.widget.FrameLayout;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.view.View$OnTouchListener;
import android.widget.EditText;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import java.util.HashMap;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.SendMessagesHelper;
import android.os.Bundle;
import org.telegram.messenger.UserConfig;
import android.text.TextUtils;
import android.content.SharedPreferences$Editor;
import android.content.DialogInterface;
import org.telegram.messenger.LocaleController;
import android.graphics.Paint$Style;
import android.content.Context;
import java.util.Collection;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.graphics.Bitmap;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import android.view.MotionEvent;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.AndroidUtilities;
import android.view.WindowManager;
import android.view.ViewTreeObserver$OnPreDrawListener;
import java.io.File;
import org.telegram.messenger.ApplicationLoader;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.Cells.WallpaperCell;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.Components.WallpaperUpdater;
import org.telegram.ui.Components.NumberTextView;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.graphics.Paint;
import android.util.LongSparseArray;
import android.view.View;
import java.util.ArrayList;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class WallpapersListActivity extends BaseFragment implements NotificationCenterDelegate
{
    public static final int TYPE_ALL = 0;
    public static final int TYPE_COLOR = 1;
    private static final int[] defaultColors;
    private static final int delete = 4;
    private static final int forward = 3;
    private static final int[] searchColors;
    private static final String[] searchColorsNames;
    private static final int[] searchColorsNamesR;
    private ArrayList<View> actionModeViews;
    private ColorWallpaper addedColorWallpaper;
    private FileWallpaper addedFileWallpaper;
    private ArrayList<Object> allWallPapers;
    private LongSparseArray<Object> allWallPapersDict;
    private FileWallpaper catsWallpaper;
    private Paint colorFramePaint;
    private Paint colorPaint;
    private int columnsCount;
    private int currentType;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private boolean loadingWallpapers;
    private ArrayList<Object> patterns;
    private AlertDialog progressDialog;
    private int resetInfoRow;
    private int resetRow;
    private int resetSectionRow;
    private int rowCount;
    private boolean scrolling;
    private SearchAdapter searchAdapter;
    private EmptyTextProgressView searchEmptyView;
    private ActionBarMenuItem searchItem;
    private int sectionRow;
    private long selectedBackground;
    private boolean selectedBackgroundBlurred;
    private boolean selectedBackgroundMotion;
    private int selectedColor;
    private float selectedIntensity;
    private NumberTextView selectedMessagesCountTextView;
    private long selectedPattern;
    private LongSparseArray<Object> selectedWallPapers;
    private int setColorRow;
    private FileWallpaper themeWallpaper;
    private int totalWallpaperRows;
    private WallpaperUpdater updater;
    private int uploadImageRow;
    private int wallPaperStartRow;
    private ArrayList<Object> wallPapers;
    
    static {
        defaultColors = new int[] { -1, -2826262, -4993567, -9783318, -16740912, -2891046, -3610935, -3808859, -10375058, -3289169, -5789547, -8622222, -10322, -18835, -2193583, -1059360, -2383431, -20561, -955808, -1524502, -6974739, -2507680, -5145015, -2765065, -2142101, -7613748, -12811138, -14524116, -14398084, -12764283, -10129027, -15195603, -16777216 };
        searchColors = new int[] { -16746753, -65536, -30208, -13824, -16718798, -14702165, -9240406, -409915, -9224159, -16777216, -10725281, -1 };
        searchColorsNames = new String[] { "Blue", "Red", "Orange", "Yellow", "Green", "Teal", "Purple", "Pink", "Brown", "Black", "Gray", "White" };
        searchColorsNamesR = new int[] { 2131558843, 2131560550, 2131560129, 2131561133, 2131559600, 2131560862, 2131560520, 2131560450, 2131558862, 2131558832, 2131559599, 2131561114 };
    }
    
    public WallpapersListActivity(final int currentType) {
        this.actionModeViews = new ArrayList<View>();
        this.columnsCount = 3;
        this.allWallPapers = new ArrayList<Object>();
        this.allWallPapersDict = (LongSparseArray<Object>)new LongSparseArray();
        this.wallPapers = new ArrayList<Object>();
        this.patterns = new ArrayList<Object>();
        this.selectedWallPapers = (LongSparseArray<Object>)new LongSparseArray();
        this.currentType = currentType;
    }
    
    private void fillWallpapersWithCustom() {
        if (this.currentType != 0) {
            return;
        }
        MessagesController.getGlobalMainSettings();
        final ColorWallpaper addedColorWallpaper = this.addedColorWallpaper;
        if (addedColorWallpaper != null) {
            this.wallPapers.remove(addedColorWallpaper);
            this.addedColorWallpaper = null;
        }
        final FileWallpaper addedFileWallpaper = this.addedFileWallpaper;
        if (addedFileWallpaper != null) {
            this.wallPapers.remove(addedFileWallpaper);
            this.addedFileWallpaper = null;
        }
        final FileWallpaper catsWallpaper = this.catsWallpaper;
        if (catsWallpaper == null) {
            this.catsWallpaper = new FileWallpaper(1000001L, 2131165299, 2131165338);
        }
        else {
            this.wallPapers.remove(catsWallpaper);
        }
        final FileWallpaper themeWallpaper = this.themeWallpaper;
        if (themeWallpaper != null) {
            this.wallPapers.remove(themeWallpaper);
        }
        Collections.sort(this.wallPapers, new _$$Lambda$WallpapersListActivity$_kR2j3QKuwClJW1mMrZH7ooQBYo(this, Theme.getCurrentTheme().isDark()));
        if (Theme.hasWallpaperFromTheme()) {
            if (this.themeWallpaper == null) {
                this.themeWallpaper = new FileWallpaper(-2L, -2, -2);
            }
            this.wallPapers.add(0, this.themeWallpaper);
        }
        else {
            this.themeWallpaper = null;
        }
        final long selectedBackground = this.selectedBackground;
        if (selectedBackground != -1L && (selectedBackground == 1000001L || (selectedBackground >= -100L && selectedBackground <= 0L) || this.allWallPapersDict.indexOfKey(this.selectedBackground) >= 0)) {
            final int selectedColor = this.selectedColor;
            if (selectedColor != 0) {
                final long selectedBackground2 = this.selectedBackground;
                if (selectedBackground2 >= -100L && this.selectedPattern < -1L) {
                    this.addedColorWallpaper = new ColorWallpaper(selectedBackground2, selectedColor);
                    this.wallPapers.add(0, this.addedColorWallpaper);
                }
            }
        }
        else {
            final long selectedPattern = this.selectedPattern;
            String child = "wallpaper.jpg";
            if (selectedPattern != 0L) {
                this.addedColorWallpaper = new ColorWallpaper(this.selectedBackground, this.selectedColor, selectedPattern, this.selectedIntensity, this.selectedBackgroundMotion, new File(ApplicationLoader.getFilesDirFixed(), "wallpaper.jpg"));
                this.wallPapers.add(0, this.addedColorWallpaper);
            }
            else {
                final int selectedColor2 = this.selectedColor;
                if (selectedColor2 != 0) {
                    this.addedColorWallpaper = new ColorWallpaper(this.selectedBackground, selectedColor2);
                    this.wallPapers.add(0, this.addedColorWallpaper);
                }
                else {
                    final long selectedBackground3 = this.selectedBackground;
                    final File file = new File(ApplicationLoader.getFilesDirFixed(), "wallpaper.jpg");
                    final File filesDirFixed = ApplicationLoader.getFilesDirFixed();
                    if (this.selectedBackgroundBlurred) {
                        child = "wallpaper_original.jpg";
                    }
                    this.addedFileWallpaper = new FileWallpaper(selectedBackground3, file, new File(filesDirFixed, child));
                    this.wallPapers.add(0, this.addedFileWallpaper);
                }
            }
        }
        if (this.selectedBackground == 1000001L) {
            this.wallPapers.add(0, this.catsWallpaper);
        }
        else {
            this.wallPapers.add(this.catsWallpaper);
        }
        this.updateRows();
    }
    
    private void fixLayout() {
        final RecyclerListView listView = this.listView;
        if (listView != null) {
            listView.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
                public boolean onPreDraw() {
                    WallpapersListActivity.this.fixLayoutInternal();
                    if (WallpapersListActivity.this.listView != null) {
                        WallpapersListActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
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
        if (AndroidUtilities.isTablet()) {
            this.columnsCount = 3;
        }
        else if (rotation != 3 && rotation != 1) {
            this.columnsCount = 3;
        }
        else {
            this.columnsCount = 5;
        }
        this.updateRows();
    }
    
    private long getWallPaperId(final Object o) {
        if (o instanceof TLRPC.TL_wallPaper) {
            return ((TLRPC.TL_wallPaper)o).id;
        }
        if (o instanceof ColorWallpaper) {
            return ((ColorWallpaper)o).id;
        }
        if (o instanceof FileWallpaper) {
            return ((FileWallpaper)o).id;
        }
        return 0L;
    }
    
    private void loadWallpapers() {
        final int size = this.allWallPapers.size();
        long n = 0L;
        for (int i = 0; i < size; ++i) {
            final Object value = this.allWallPapers.get(i);
            if (value instanceof TLRPC.TL_wallPaper) {
                final long id = ((TLRPC.TL_wallPaper)value).id;
                n = ((n * 20261L + 2147483648L + (int)(id >> 32)) % 2147483648L * 20261L + 2147483648L + (int)id) % 2147483648L;
            }
        }
        final TLRPC.TL_account_getWallPapers tl_account_getWallPapers = new TLRPC.TL_account_getWallPapers();
        tl_account_getWallPapers.hash = (int)n;
        ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_account_getWallPapers, new _$$Lambda$WallpapersListActivity$rkTFtQlkaXRQn8loH7KW7DjD08g(this)), super.classGuid);
    }
    
    private void onItemClick(final WallpaperCell wallpaperCell, final Object o, final int n) {
        if (super.actionBar.isActionModeShowed()) {
            if (!(o instanceof TLRPC.TL_wallPaper)) {
                return;
            }
            final TLRPC.TL_wallPaper tl_wallPaper = (TLRPC.TL_wallPaper)o;
            if (this.selectedWallPapers.indexOfKey(tl_wallPaper.id) >= 0) {
                this.selectedWallPapers.remove(tl_wallPaper.id);
            }
            else {
                this.selectedWallPapers.put(tl_wallPaper.id, (Object)tl_wallPaper);
            }
            if (this.selectedWallPapers.size() == 0) {
                super.actionBar.hideActionMode();
            }
            else {
                this.selectedMessagesCountTextView.setNumber(this.selectedWallPapers.size(), true);
            }
            this.scrolling = false;
            wallpaperCell.setChecked(n, this.selectedWallPapers.indexOfKey(tl_wallPaper.id) >= 0, true);
        }
        else {
            final long wallPaperId = this.getWallPaperId(o);
            Object o2 = o;
            if (o instanceof TLRPC.TL_wallPaper) {
                final TLRPC.TL_wallPaper tl_wallPaper2 = (TLRPC.TL_wallPaper)o;
                o2 = o;
                if (tl_wallPaper2.pattern) {
                    final long id = tl_wallPaper2.id;
                    final TLRPC.TL_wallPaperSettings settings = tl_wallPaper2.settings;
                    o2 = new ColorWallpaper(id, settings.background_color, id, settings.intensity / 100.0f, settings.motion, null);
                }
            }
            final WallpaperActivity wallpaperActivity = new WallpaperActivity(o2, null);
            if (this.currentType == 1) {
                wallpaperActivity.setDelegate((WallpaperActivity.WallpaperActivityDelegate)new _$$Lambda$tPCre3L2K_38M9O_G5mv57D0Uc4(this));
            }
            if (this.selectedBackground == wallPaperId) {
                wallpaperActivity.setInitialModes(this.selectedBackgroundBlurred, this.selectedBackgroundMotion);
            }
            wallpaperActivity.setPatterns(this.patterns);
            this.presentFragment(wallpaperActivity);
        }
    }
    
    private boolean onItemLongClick(final WallpaperCell wallpaperCell, final Object o, final int n) {
        if (!super.actionBar.isActionModeShowed() && this.getParentActivity() != null && o instanceof TLRPC.TL_wallPaper) {
            final TLRPC.TL_wallPaper tl_wallPaper = (TLRPC.TL_wallPaper)o;
            AndroidUtilities.hideKeyboard(this.getParentActivity().getCurrentFocus());
            this.selectedWallPapers.put(tl_wallPaper.id, (Object)tl_wallPaper);
            this.selectedMessagesCountTextView.setNumber(1, false);
            final AnimatorSet set = new AnimatorSet();
            final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
            for (int i = 0; i < this.actionModeViews.size(); ++i) {
                final View view = this.actionModeViews.get(i);
                AndroidUtilities.clearDrawableAnimation(view);
                list.add(ObjectAnimator.ofFloat((Object)view, View.SCALE_Y, new float[] { 0.1f, 1.0f }));
            }
            set.playTogether((Collection)list);
            set.setDuration(250L);
            set.start();
            this.scrolling = false;
            super.actionBar.showActionMode();
            wallpaperCell.setChecked(n, true, true);
            return true;
        }
        return false;
    }
    
    private void updateRows() {
        this.rowCount = 0;
        if (this.currentType == 0) {
            this.uploadImageRow = this.rowCount++;
            this.setColorRow = this.rowCount++;
            this.sectionRow = this.rowCount++;
        }
        else {
            this.uploadImageRow = -1;
            this.setColorRow = -1;
            this.sectionRow = -1;
        }
        if (!this.wallPapers.isEmpty()) {
            this.totalWallpaperRows = (int)Math.ceil(this.wallPapers.size() / (float)this.columnsCount);
            final int rowCount = this.rowCount;
            this.wallPaperStartRow = rowCount;
            this.rowCount = rowCount + this.totalWallpaperRows;
        }
        else {
            this.wallPaperStartRow = -1;
        }
        if (this.currentType == 0) {
            this.resetSectionRow = this.rowCount++;
            this.resetRow = this.rowCount++;
            this.resetInfoRow = this.rowCount++;
        }
        else {
            this.resetSectionRow = -1;
            this.resetRow = -1;
            this.resetInfoRow = -1;
        }
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            this.scrolling = true;
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
    }
    
    private void updateRowsSelection() {
        for (int childCount = this.listView.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.listView.getChildAt(i);
            if (child instanceof WallpaperCell) {
                final WallpaperCell wallpaperCell = (WallpaperCell)child;
                for (int j = 0; j < 5; ++j) {
                    wallpaperCell.setChecked(j, false, true);
                }
            }
        }
    }
    
    @Override
    public View createView(final Context context) {
        this.colorPaint = new Paint(1);
        (this.colorFramePaint = new Paint(1)).setStrokeWidth((float)AndroidUtilities.dp(1.0f));
        this.colorFramePaint.setStyle(Paint$Style.STROKE);
        this.colorFramePaint.setColor(855638016);
        this.updater = new WallpaperUpdater(this.getParentActivity(), this, (WallpaperUpdater.WallpaperUpdaterDelegate)new WallpaperUpdater.WallpaperUpdaterDelegate() {
            @Override
            public void didSelectWallpaper(final File file, final Bitmap bitmap, final boolean b) {
                WallpapersListActivity.this.presentFragment(new WallpaperActivity(new FileWallpaper(-1L, file, file), bitmap), b);
            }
            
            @Override
            public void needOpenColorPicker() {
            }
        });
        super.hasOwnBackground = true;
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        final int currentType = this.currentType;
        if (currentType == 0) {
            super.actionBar.setTitle(LocaleController.getString("ChatBackground", 2131559024));
        }
        else if (currentType == 1) {
            super.actionBar.setTitle(LocaleController.getString("SelectColorTitle", 2131560679));
        }
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    if (WallpapersListActivity.this.actionBar.isActionModeShowed()) {
                        WallpapersListActivity.this.selectedWallPapers.clear();
                        WallpapersListActivity.this.actionBar.hideActionMode();
                        WallpapersListActivity.this.updateRowsSelection();
                    }
                    else {
                        WallpapersListActivity.this.finishFragment();
                    }
                }
                else if (n == 4) {
                    if (WallpapersListActivity.this.getParentActivity() == null) {
                        return;
                    }
                    final AlertDialog.Builder builder = new AlertDialog.Builder((Context)WallpapersListActivity.this.getParentActivity());
                    builder.setMessage(LocaleController.formatString("DeleteChatBackgroundsAlert", 2131559239, new Object[0]));
                    builder.setTitle(LocaleController.getString("AppName", 2131558635));
                    builder.setPositiveButton(LocaleController.getString("OK", 2131560097), (DialogInterface$OnClickListener)new _$$Lambda$WallpapersListActivity$2$K8aHn505ku1qQXr9dNGqIjZPpyE(this));
                    builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                    WallpapersListActivity.this.showDialog(builder.create());
                }
                else if (n == 3) {
                    final Bundle bundle = new Bundle();
                    bundle.putBoolean("onlySelect", true);
                    bundle.putInt("dialogsType", 3);
                    final DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                    dialogsActivity.setDelegate((DialogsActivity.DialogsActivityDelegate)new _$$Lambda$WallpapersListActivity$2$_pR5kSEFy3SmzguvrtK_EnN_KAw(this));
                    WallpapersListActivity.this.presentFragment(dialogsActivity);
                }
            }
        });
        if (this.currentType == 0) {
            (this.searchItem = super.actionBar.createMenu().addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener((ActionBarMenuItem.ActionBarMenuItemSearchListener)new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
                @Override
                public void onCaptionCleared() {
                    WallpapersListActivity.this.searchAdapter.clearColor();
                    WallpapersListActivity.this.searchItem.setSearchFieldHint(LocaleController.getString("SearchBackgrounds", 2131560641));
                }
                
                @Override
                public void onSearchCollapse() {
                    WallpapersListActivity.this.listView.setAdapter(WallpapersListActivity.this.listAdapter);
                    WallpapersListActivity.this.listView.invalidate();
                    WallpapersListActivity.this.searchAdapter.processSearch(null, true);
                    WallpapersListActivity.this.searchItem.setSearchFieldCaption(null);
                    this.onCaptionCleared();
                }
                
                @Override
                public void onSearchExpand() {
                    WallpapersListActivity.this.listView.setAdapter(WallpapersListActivity.this.searchAdapter);
                    WallpapersListActivity.this.listView.invalidate();
                }
                
                @Override
                public void onTextChanged(final EditText editText) {
                    WallpapersListActivity.this.searchAdapter.processSearch(editText.getText().toString(), false);
                }
            })).setSearchFieldHint(LocaleController.getString("SearchBackgrounds", 2131560641));
            final ActionBarMenu actionMode = super.actionBar.createActionMode(false);
            actionMode.setBackgroundColor(Theme.getColor("actionBarDefault"));
            super.actionBar.setItemsColor(Theme.getColor("actionBarDefaultIcon"), true);
            super.actionBar.setItemsBackgroundColor(Theme.getColor("actionBarDefaultSelector"), true);
            (this.selectedMessagesCountTextView = new NumberTextView(actionMode.getContext())).setTextSize(18);
            this.selectedMessagesCountTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.selectedMessagesCountTextView.setTextColor(Theme.getColor("actionBarDefaultIcon"));
            this.selectedMessagesCountTextView.setOnTouchListener((View$OnTouchListener)_$$Lambda$WallpapersListActivity$kDCUFe0ixQyVZvdd5on4Sg4XaNc.INSTANCE);
            actionMode.addView((View)this.selectedMessagesCountTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(0, -1, 1.0f, 65, 0, 0, 0));
            this.actionModeViews.add((View)actionMode.addItemWithWidth(3, 2131165627, AndroidUtilities.dp(54.0f)));
            this.actionModeViews.add((View)actionMode.addItemWithWidth(4, 2131165623, AndroidUtilities.dp(54.0f)));
            this.selectedWallPapers.clear();
        }
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        (this.listView = new RecyclerListView(context) {
            private Paint paint = new Paint();
            
            @Override
            public boolean hasOverlappingRendering() {
                return false;
            }
            
            @Override
            public void onDraw(final Canvas canvas) {
                Object viewHolderForAdapterPosition;
                if (this.getAdapter() == WallpapersListActivity.this.listAdapter && WallpapersListActivity.this.resetInfoRow != -1) {
                    viewHolderForAdapterPosition = this.findViewHolderForAdapterPosition(WallpapersListActivity.this.resetInfoRow);
                }
                else {
                    viewHolderForAdapterPosition = null;
                }
                final int measuredHeight = this.getMeasuredHeight();
                int bottom = 0;
                Label_0074: {
                    if (viewHolderForAdapterPosition != null) {
                        bottom = ((ViewHolder)viewHolderForAdapterPosition).itemView.getBottom();
                        if (((ViewHolder)viewHolderForAdapterPosition).itemView.getBottom() < measuredHeight) {
                            break Label_0074;
                        }
                    }
                    bottom = measuredHeight;
                }
                this.paint.setColor(Theme.getColor("windowBackgroundWhite"));
                final float n = (float)this.getMeasuredWidth();
                final float n2 = (float)bottom;
                canvas.drawRect(0.0f, 0.0f, n, n2, this.paint);
                if (bottom != measuredHeight) {
                    this.paint.setColor(Theme.getColor("windowBackgroundGray"));
                    canvas.drawRect(0.0f, n2, (float)this.getMeasuredWidth(), (float)measuredHeight, this.paint);
                }
            }
        }).setClipToPadding(false);
        this.listView.setHorizontalScrollBarEnabled(false);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation((LayoutAnimationController)null);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new LinearLayoutManager(context, 1, false) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        }));
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter = new ListAdapter(context));
        this.searchAdapter = new SearchAdapter(context);
        this.listView.setGlowColor(Theme.getColor("avatar_backgroundActionBarBlue"));
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$WallpapersListActivity$PdMdfLigg_iWQcF5CiT4cSCfNQ8(this));
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                boolean b = true;
                if (n == 1) {
                    AndroidUtilities.hideKeyboard(WallpapersListActivity.this.getParentActivity().getCurrentFocus());
                }
                final WallpapersListActivity this$0 = WallpapersListActivity.this;
                if (n == 0) {
                    b = false;
                }
                this$0.scrolling = b;
            }
            
            @Override
            public void onScrolled(final RecyclerView recyclerView, int n, int firstVisibleItemPosition) {
                if (WallpapersListActivity.this.listView.getAdapter() == WallpapersListActivity.this.searchAdapter) {
                    firstVisibleItemPosition = WallpapersListActivity.this.layoutManager.findFirstVisibleItemPosition();
                    if (firstVisibleItemPosition == -1) {
                        n = 0;
                    }
                    else {
                        n = Math.abs(WallpapersListActivity.this.layoutManager.findLastVisibleItemPosition() - firstVisibleItemPosition) + 1;
                    }
                    if (n > 0) {
                        final int itemCount = ((RecyclerView.LayoutManager)WallpapersListActivity.this.layoutManager).getItemCount();
                        if (n != 0 && firstVisibleItemPosition + n > itemCount - 2) {
                            WallpapersListActivity.this.searchAdapter.loadMoreResults();
                        }
                    }
                }
            }
        });
        (this.searchEmptyView = new EmptyTextProgressView(context)).setVisibility(8);
        this.searchEmptyView.setShowAtCenter(true);
        this.searchEmptyView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.searchEmptyView.setText(LocaleController.getString("NoResult", 2131559943));
        this.listView.setEmptyView((View)this.searchEmptyView);
        frameLayout.addView((View)this.searchEmptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.updateRows();
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(int i, int size, final Object... array) {
        if (i == NotificationCenter.wallpapersDidLoad) {
            i = 0;
            final ArrayList c = (ArrayList)array[0];
            this.patterns.clear();
            if (this.currentType != 1) {
                this.wallPapers.clear();
                this.allWallPapers.clear();
                this.allWallPapersDict.clear();
                this.allWallPapers.addAll(c);
            }
            TLRPC.TL_wallPaper tl_wallPaper;
            for (size = c.size(); i < size; ++i) {
                tl_wallPaper = (TLRPC.TL_wallPaper)c.get(i);
                if (tl_wallPaper.pattern) {
                    this.patterns.add(tl_wallPaper);
                }
                if (this.currentType != 1 && (!tl_wallPaper.pattern || tl_wallPaper.settings != null)) {
                    this.allWallPapersDict.put(tl_wallPaper.id, (Object)tl_wallPaper);
                    this.wallPapers.add(tl_wallPaper);
                }
            }
            this.selectedBackground = Theme.getSelectedBackgroundId();
            this.fillWallpapersWithCustom();
            this.loadWallpapers();
        }
        else if (i == NotificationCenter.didSetNewWallpapper) {
            final RecyclerListView listView = this.listView;
            if (listView != null) {
                listView.invalidateViews();
            }
            final ActionBar actionBar = super.actionBar;
            if (actionBar != null) {
                actionBar.closeSearchField();
            }
        }
        else if (i == NotificationCenter.wallpapersNeedReload) {
            MessagesStorage.getInstance(super.currentAccount).getWallpapers();
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, 0, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, 0, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription((View)this.listView, 0, new Class[] { GraySectionCell.class }, new String[] { "textView" }, null, null, null, "key_graySectionText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { GraySectionCell.class }, null, null, null, "graySection"), new ThemeDescription((View)this.searchEmptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"), new ThemeDescription((View)this.searchEmptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"), new ThemeDescription((View)this.searchEmptyView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite") };
    }
    
    @Override
    public void onActivityResultFragment(final int n, final int n2, final Intent intent) {
        this.updater.onActivityResult(n, n2, intent);
    }
    
    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.fixLayout();
    }
    
    @Override
    public boolean onFragmentCreate() {
        if (this.currentType == 0) {
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.wallpapersDidLoad);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.wallpapersNeedReload);
            MessagesStorage.getInstance(super.currentAccount).getWallpapers();
        }
        else {
            int n = 0;
            while (true) {
                final int[] defaultColors = WallpapersListActivity.defaultColors;
                if (n >= defaultColors.length) {
                    break;
                }
                this.wallPapers.add(new ColorWallpaper(-(n + 3), defaultColors[n]));
                ++n;
            }
            if (this.currentType == 1 && this.patterns.isEmpty()) {
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.wallpapersDidLoad);
                MessagesStorage.getInstance(super.currentAccount).getWallpapers();
            }
        }
        return super.onFragmentCreate();
    }
    
    @Override
    public void onFragmentDestroy() {
        final int currentType = this.currentType;
        if (currentType == 0) {
            this.searchAdapter.onDestroy();
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.wallpapersDidLoad);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.wallpapersNeedReload);
        }
        else if (currentType == 1) {
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.wallpapersDidLoad);
        }
        this.updater.cleanup();
        super.onFragmentDestroy();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        this.selectedBackground = Theme.getSelectedBackgroundId();
        this.selectedPattern = globalMainSettings.getLong("selectedPattern", 0L);
        this.selectedColor = globalMainSettings.getInt("selectedColor", 0);
        this.selectedIntensity = globalMainSettings.getFloat("selectedIntensity", 1.0f);
        this.selectedBackgroundMotion = globalMainSettings.getBoolean("selectedBackgroundMotion", false);
        this.selectedBackgroundBlurred = globalMainSettings.getBoolean("selectedBackgroundBlurred", false);
        this.fillWallpapersWithCustom();
        this.fixLayout();
    }
    
    @Override
    public void restoreSelfArgs(final Bundle bundle) {
        this.updater.setCurrentPicturePath(bundle.getString("path"));
    }
    
    @Override
    public void saveSelfArgs(final Bundle bundle) {
        final String currentPicturePath = this.updater.getCurrentPicturePath();
        if (currentPicturePath != null) {
            bundle.putString("path", currentPicturePath);
        }
    }
    
    private class ColorCell extends View
    {
        private int color;
        
        public ColorCell(final Context context) {
            super(context);
        }
        
        protected void onDraw(final Canvas canvas) {
            WallpapersListActivity.this.colorPaint.setColor(this.color);
            canvas.drawCircle((float)AndroidUtilities.dp(25.0f), (float)AndroidUtilities.dp(31.0f), (float)AndroidUtilities.dp(18.0f), WallpapersListActivity.this.colorPaint);
            if (this.color == Theme.getColor("windowBackgroundWhite")) {
                canvas.drawCircle((float)AndroidUtilities.dp(25.0f), (float)AndroidUtilities.dp(31.0f), (float)AndroidUtilities.dp(18.0f), WallpapersListActivity.this.colorFramePaint);
            }
        }
        
        protected void onMeasure(final int n, final int n2) {
            this.setMeasuredDimension(AndroidUtilities.dp(50.0f), AndroidUtilities.dp(62.0f));
        }
        
        public void setColor(final int color) {
            this.color = color;
        }
    }
    
    public static class ColorWallpaper
    {
        public int color;
        public long id;
        public float intensity;
        public boolean motion;
        public File path;
        public TLRPC.TL_wallPaper pattern;
        public long patternId;
        
        public ColorWallpaper(final long id, final int n) {
            this.id = id;
            this.color = (0xFF000000 | n);
            this.intensity = 1.0f;
        }
        
        public ColorWallpaper(final long id, final int n, final long patternId, final float intensity, final boolean motion, final File path) {
            this.id = id;
            this.color = (0xFF000000 | n);
            this.patternId = patternId;
            this.intensity = intensity;
            this.path = path;
            this.motion = motion;
        }
    }
    
    public static class FileWallpaper
    {
        public long id;
        public File originalPath;
        public File path;
        public int resId;
        public int thumbResId;
        
        public FileWallpaper(final long id, final int resId, final int thumbResId) {
            this.id = id;
            this.resId = resId;
            this.thumbResId = thumbResId;
        }
        
        public FileWallpaper(final long id, final File path, final File originalPath) {
            this.id = id;
            this.path = path;
            this.originalPath = originalPath;
        }
        
        public FileWallpaper(final long id, final String pathname) {
            this.id = id;
            this.path = new File(pathname);
        }
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            return WallpapersListActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == WallpapersListActivity.this.uploadImageRow || n == WallpapersListActivity.this.setColorRow || n == WallpapersListActivity.this.resetRow) {
                return 0;
            }
            if (n == WallpapersListActivity.this.sectionRow || n == WallpapersListActivity.this.resetSectionRow) {
                return 1;
            }
            if (n == WallpapersListActivity.this.resetInfoRow) {
                return 3;
            }
            return 2;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType != 2) {
                    if (itemViewType == 3) {
                        final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                        if (i == WallpapersListActivity.this.resetInfoRow) {
                            textInfoPrivacyCell.setText(LocaleController.getString("ResetChatBackgroundsInfo", 2131560596));
                        }
                    }
                }
                else {
                    final WallpaperCell wallpaperCell = (WallpaperCell)viewHolder.itemView;
                    final int n = (i - WallpapersListActivity.this.wallPaperStartRow) * WallpapersListActivity.this.columnsCount;
                    i = WallpapersListActivity.this.columnsCount;
                    wallpaperCell.setParams(i, n == 0, n / WallpapersListActivity.this.columnsCount == WallpapersListActivity.this.totalWallpaperRows - 1);
                    int index;
                    Object value;
                    long id;
                    for (i = 0; i < WallpapersListActivity.this.columnsCount; ++i) {
                        index = n + i;
                        if (index < WallpapersListActivity.this.wallPapers.size()) {
                            value = WallpapersListActivity.this.wallPapers.get(index);
                        }
                        else {
                            value = null;
                        }
                        wallpaperCell.setWallpaper(WallpapersListActivity.this.currentType, i, value, WallpapersListActivity.this.selectedBackground, null, false);
                        if (value instanceof TLRPC.TL_wallPaper) {
                            id = ((TLRPC.TL_wallPaper)value).id;
                        }
                        else {
                            id = 0L;
                        }
                        if (WallpapersListActivity.this.actionBar.isActionModeShowed()) {
                            wallpaperCell.setChecked(i, WallpapersListActivity.this.selectedWallPapers.indexOfKey(id) >= 0, WallpapersListActivity.this.scrolling ^ true);
                        }
                        else {
                            wallpaperCell.setChecked(i, false, WallpapersListActivity.this.scrolling ^ true);
                        }
                    }
                }
            }
            else {
                final TextCell textCell = (TextCell)viewHolder.itemView;
                if (i == WallpapersListActivity.this.uploadImageRow) {
                    textCell.setTextAndIcon(LocaleController.getString("SelectFromGallery", 2131560683), 2131165792, true);
                }
                else if (i == WallpapersListActivity.this.setColorRow) {
                    textCell.setTextAndIcon(LocaleController.getString("SetColor", 2131560733), 2131165589, false);
                }
                else if (i == WallpapersListActivity.this.resetRow) {
                    textCell.setText(LocaleController.getString("ResetChatBackgrounds", 2131560593), false);
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int n) {
            Object o;
            if (n != 0) {
                final int n2 = 2131165395;
                if (n != 1) {
                    if (n != 3) {
                        o = new WallpaperCell(this.mContext) {
                            @Override
                            protected void onWallpaperClick(final Object o, final int n) {
                                WallpapersListActivity.this.onItemClick(this, o, n);
                            }
                            
                            @Override
                            protected boolean onWallpaperLongClick(final Object o, final int n) {
                                return WallpapersListActivity.this.onItemLongClick(this, o, n);
                            }
                        };
                    }
                    else {
                        o = new TextInfoPrivacyCell(this.mContext);
                        final CombinedDrawable backgroundDrawable = new CombinedDrawable((Drawable)new ColorDrawable(Theme.getColor("windowBackgroundGray")), Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                        backgroundDrawable.setFullsize(true);
                        ((View)o).setBackgroundDrawable((Drawable)backgroundDrawable);
                    }
                }
                else {
                    o = new ShadowSectionCell(this.mContext);
                    final Context mContext = this.mContext;
                    if (WallpapersListActivity.this.wallPaperStartRow == -1) {
                        n = n2;
                    }
                    else {
                        n = 2131165394;
                    }
                    final CombinedDrawable backgroundDrawable2 = new CombinedDrawable((Drawable)new ColorDrawable(Theme.getColor("windowBackgroundGray")), Theme.getThemedDrawable(mContext, n, "windowBackgroundGrayShadow"));
                    backgroundDrawable2.setFullsize(true);
                    ((View)o).setBackgroundDrawable((Drawable)backgroundDrawable2);
                }
            }
            else {
                o = new TextCell(this.mContext);
            }
            return new RecyclerListView.Holder((View)o);
        }
    }
    
    private class SearchAdapter extends SelectionAdapter
    {
        private boolean bingSearchEndReached;
        private int imageReqId;
        private RecyclerListView innerListView;
        private String lastSearchImageString;
        private String lastSearchString;
        private int lastSearchToken;
        private Context mContext;
        private String nextImagesSearchOffset;
        private ArrayList<MediaController.SearchImage> searchResult;
        private HashMap<String, MediaController.SearchImage> searchResultKeys;
        private Runnable searchRunnable;
        private boolean searchingUser;
        private String selectedColor;
        final /* synthetic */ WallpapersListActivity this$0;
        
        public SearchAdapter(final Context mContext) {
            this.searchResult = new ArrayList<MediaController.SearchImage>();
            this.searchResultKeys = new HashMap<String, MediaController.SearchImage>();
            this.bingSearchEndReached = true;
            this.mContext = mContext;
        }
        
        private void doSearch(final String lastSearchString) {
            this.searchResult.clear();
            this.searchResultKeys.clear();
            this.searchImages(lastSearchString, "", this.bingSearchEndReached = true);
            this.lastSearchString = lastSearchString;
            this.notifyDataSetChanged();
        }
        
        private void processSearch(final String str, final boolean b) {
            String string = str;
            if (str != null) {
                string = str;
                if (this.selectedColor != null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("#color");
                    sb.append(this.selectedColor);
                    sb.append(" ");
                    sb.append(str);
                    string = sb.toString();
                }
            }
            final Runnable searchRunnable = this.searchRunnable;
            if (searchRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(searchRunnable);
                this.searchRunnable = null;
            }
            if (TextUtils.isEmpty((CharSequence)string)) {
                this.searchResult.clear();
                this.searchResultKeys.clear();
                this.bingSearchEndReached = true;
                this.lastSearchString = null;
                if (this.imageReqId != 0) {
                    ConnectionsManager.getInstance(WallpapersListActivity.this.currentAccount).cancelRequest(this.imageReqId, true);
                    this.imageReqId = 0;
                }
                WallpapersListActivity.this.searchEmptyView.showTextView();
            }
            else {
                WallpapersListActivity.this.searchEmptyView.showProgress();
                if (b) {
                    this.doSearch(string);
                }
                else {
                    AndroidUtilities.runOnUIThread(this.searchRunnable = new _$$Lambda$WallpapersListActivity$SearchAdapter$A_mUNZ6ShjO2kmNuqCpt4p0OR_4(this, string), 500L);
                }
            }
            this.notifyDataSetChanged();
        }
        
        private void searchBotUser() {
            if (this.searchingUser) {
                return;
            }
            this.searchingUser = true;
            final TLRPC.TL_contacts_resolveUsername tl_contacts_resolveUsername = new TLRPC.TL_contacts_resolveUsername();
            tl_contacts_resolveUsername.username = MessagesController.getInstance(WallpapersListActivity.this.currentAccount).imageSearchBot;
            ConnectionsManager.getInstance(WallpapersListActivity.this.currentAccount).sendRequest(tl_contacts_resolveUsername, new _$$Lambda$WallpapersListActivity$SearchAdapter$4mWogDs9zLmdvnE5jd5DGo_Q4ak(this));
        }
        
        private void searchImages(final String s, final String offset, final boolean b) {
            if (this.imageReqId != 0) {
                ConnectionsManager.getInstance(WallpapersListActivity.this.currentAccount).cancelRequest(this.imageReqId, true);
                this.imageReqId = 0;
            }
            this.lastSearchImageString = s;
            final TLObject userOrChat = MessagesController.getInstance(WallpapersListActivity.this.currentAccount).getUserOrChat(MessagesController.getInstance(WallpapersListActivity.this.currentAccount).imageSearchBot);
            if (!(userOrChat instanceof TLRPC.User)) {
                if (b) {
                    this.searchBotUser();
                }
                return;
            }
            final TLRPC.User user = (TLRPC.User)userOrChat;
            final TLRPC.TL_messages_getInlineBotResults tl_messages_getInlineBotResults = new TLRPC.TL_messages_getInlineBotResults();
            final StringBuilder sb = new StringBuilder();
            sb.append("#wallpaper ");
            sb.append(s);
            tl_messages_getInlineBotResults.query = sb.toString();
            tl_messages_getInlineBotResults.bot = MessagesController.getInstance(WallpapersListActivity.this.currentAccount).getInputUser(user);
            tl_messages_getInlineBotResults.offset = offset;
            tl_messages_getInlineBotResults.peer = new TLRPC.TL_inputPeerEmpty();
            final int lastSearchToken = this.lastSearchToken + 1;
            this.lastSearchToken = lastSearchToken;
            this.imageReqId = ConnectionsManager.getInstance(WallpapersListActivity.this.currentAccount).sendRequest(tl_messages_getInlineBotResults, new _$$Lambda$WallpapersListActivity$SearchAdapter$EUxupb60ZwBIv1eg9iblwmPpQ9k(this, lastSearchToken));
            ConnectionsManager.getInstance(WallpapersListActivity.this.currentAccount).bindRequestToGuid(this.imageReqId, WallpapersListActivity.this.classGuid);
        }
        
        public void clearColor() {
            this.processSearch(this.selectedColor = null, true);
        }
        
        public RecyclerListView getInnerListView() {
            return this.innerListView;
        }
        
        @Override
        public int getItemCount() {
            if (TextUtils.isEmpty((CharSequence)this.lastSearchString)) {
                return 2;
            }
            return (int)Math.ceil(this.searchResult.size() / (float)WallpapersListActivity.this.columnsCount);
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (!TextUtils.isEmpty((CharSequence)this.lastSearchString)) {
                return 0;
            }
            if (n == 0) {
                return 2;
            }
            return 1;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return viewHolder.getItemViewType() != 2;
        }
        
        public void loadMoreResults() {
            if (!this.bingSearchEndReached) {
                if (this.imageReqId == 0) {
                    this.searchImages(this.lastSearchString, this.nextImagesSearchOffset, true);
                }
            }
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType == 2) {
                    ((GraySectionCell)viewHolder.itemView).setText(LocaleController.getString("SearchByColor", 2131560642));
                }
            }
            else {
                final WallpaperCell wallpaperCell = (WallpaperCell)viewHolder.itemView;
                final int n = i * WallpapersListActivity.this.columnsCount;
                final int n2 = (int)Math.ceil(this.searchResult.size() / (float)WallpapersListActivity.this.columnsCount);
                i = WallpapersListActivity.this.columnsCount;
                boolean b = true;
                final boolean b2 = n == 0;
                if (n / WallpapersListActivity.this.columnsCount != n2 - 1) {
                    b = false;
                }
                wallpaperCell.setParams(i, b2, b);
                int index;
                Object value;
                for (i = 0; i < WallpapersListActivity.this.columnsCount; ++i) {
                    index = n + i;
                    if (index < this.searchResult.size()) {
                        value = this.searchResult.get(index);
                    }
                    else {
                        value = null;
                    }
                    wallpaperCell.setWallpaper(WallpapersListActivity.this.currentType, i, value, 0L, null, false);
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object innerListView = null;
            if (n != 0) {
                if (n != 1) {
                    if (n == 2) {
                        innerListView = new GraySectionCell(this.mContext);
                    }
                }
                else {
                    innerListView = new RecyclerListView(this.mContext) {
                        @Override
                        public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                            if (this.getParent() != null && this.getParent().getParent() != null) {
                                this.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                            }
                            return super.onInterceptTouchEvent(motionEvent);
                        }
                    };
                    ((RecyclerView)innerListView).setItemAnimator(null);
                    ((ViewGroup)innerListView).setLayoutAnimation((LayoutAnimationController)null);
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(this.mContext) {
                        @Override
                        public boolean supportsPredictiveItemAnimations() {
                            return false;
                        }
                    };
                    ((ViewGroup)innerListView).setPadding(AndroidUtilities.dp(7.0f), 0, AndroidUtilities.dp(7.0f), 0);
                    ((RecyclerView)innerListView).setClipToPadding(false);
                    layoutManager.setOrientation(0);
                    ((RecyclerView)innerListView).setLayoutManager((RecyclerView.LayoutManager)layoutManager);
                    ((RecyclerListView)innerListView).setAdapter(new CategoryAdapterRecycler());
                    ((RecyclerListView)innerListView).setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$WallpapersListActivity$SearchAdapter$oLQrjTlnoZYzJvyN_dx2q_irBAw(this));
                    this.innerListView = (RecyclerListView)innerListView;
                }
            }
            else {
                innerListView = new WallpaperCell(this.mContext) {
                    @Override
                    protected void onWallpaperClick(final Object o, final int n) {
                        WallpapersListActivity.this.presentFragment(new WallpaperActivity(o, null));
                    }
                };
            }
            if (n == 1) {
                ((View)innerListView).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(60.0f)));
            }
            else {
                ((View)innerListView).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            }
            return new RecyclerListView.Holder((View)innerListView);
        }
        
        public void onDestroy() {
            if (this.imageReqId != 0) {
                ConnectionsManager.getInstance(WallpapersListActivity.this.currentAccount).cancelRequest(this.imageReqId, true);
                this.imageReqId = 0;
            }
        }
        
        private class CategoryAdapterRecycler extends SelectionAdapter
        {
            @Override
            public int getItemCount() {
                return WallpapersListActivity.searchColors.length;
            }
            
            @Override
            public boolean isEnabled(final ViewHolder viewHolder) {
                return true;
            }
            
            @Override
            public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
                ((ColorCell)viewHolder.itemView).setColor(WallpapersListActivity.searchColors[n]);
            }
            
            @Override
            public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
                final SearchAdapter this$1 = SearchAdapter.this;
                return new RecyclerListView.Holder(this$1.this$0.new ColorCell(this$1.mContext));
            }
        }
    }
}
