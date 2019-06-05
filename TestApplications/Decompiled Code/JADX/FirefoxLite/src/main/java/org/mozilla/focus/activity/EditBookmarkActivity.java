package org.mozilla.focus.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.p001v4.content.ContextCompat;
import android.support.p001v4.graphics.drawable.DrawableCompat;
import android.support.p004v7.app.ActionBar;
import android.support.p004v7.widget.Toolbar;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import kotlin.Lazy;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.persistence.BookmarkModel;
import org.mozilla.focus.viewmodel.BookmarkViewModel;
import org.mozilla.focus.viewmodel.BookmarkViewModel.Factory;
import org.mozilla.rocket.C0769R;

/* compiled from: EditBookmarkActivity.kt */
public final class EditBookmarkActivity extends BaseActivity {
    static final /* synthetic */ KProperty[] $$delegatedProperties = new KProperty[]{Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "itemId", "getItemId()Ljava/lang/String;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "viewModelFactory", "getViewModelFactory()Lorg/mozilla/focus/viewmodel/BookmarkViewModel$Factory;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "viewModel", "getViewModel()Lorg/mozilla/focus/viewmodel/BookmarkViewModel;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "editTextName", "getEditTextName()Landroid/widget/EditText;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "editTextLocation", "getEditTextLocation()Landroid/widget/EditText;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "labelName", "getLabelName()Landroid/widget/TextView;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "labelLocation", "getLabelLocation()Landroid/widget/TextView;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "originalName", "getOriginalName()Ljava/lang/String;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "originalLocation", "getOriginalLocation()Ljava/lang/String;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "buttonClearName", "getButtonClearName()Landroid/widget/ImageButton;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "buttonClearLocation", "getButtonClearLocation()Landroid/widget/ImageButton;"))};
    private HashMap _$_findViewCache;
    private BookmarkModel bookmark;
    private final Lazy buttonClearLocation$delegate = LazyKt__LazyJVMKt.lazy(new EditBookmarkActivity$buttonClearLocation$2(this));
    private final Lazy buttonClearName$delegate = LazyKt__LazyJVMKt.lazy(new EditBookmarkActivity$buttonClearName$2(this));
    private final Lazy editTextLocation$delegate = LazyKt__LazyJVMKt.lazy(new EditBookmarkActivity$editTextLocation$2(this));
    private final Lazy editTextName$delegate = LazyKt__LazyJVMKt.lazy(new EditBookmarkActivity$editTextName$2(this));
    private final OnFocusChangeListener focusChangeListener = new EditBookmarkActivity$focusChangeListener$1(this);
    private final Lazy itemId$delegate = LazyKt__LazyJVMKt.lazy(new EditBookmarkActivity$itemId$2(this));
    private final Lazy labelLocation$delegate = LazyKt__LazyJVMKt.lazy(new EditBookmarkActivity$labelLocation$2(this));
    private final Lazy labelName$delegate = LazyKt__LazyJVMKt.lazy(new EditBookmarkActivity$labelName$2(this));
    private boolean locationChanged;
    private boolean locationEmpty;
    private final TextWatcher locationWatcher = new EditBookmarkActivity$locationWatcher$1(this);
    private MenuItem menuItemSave;
    private boolean nameChanged;
    private final TextWatcher nameWatcher = new EditBookmarkActivity$nameWatcher$1(this);
    private final Lazy originalLocation$delegate = LazyKt__LazyJVMKt.lazy(new EditBookmarkActivity$originalLocation$2(this));
    private final Lazy originalName$delegate = LazyKt__LazyJVMKt.lazy(new EditBookmarkActivity$originalName$2(this));
    private final Lazy viewModel$delegate = LazyKt__LazyJVMKt.lazy(new EditBookmarkActivity$viewModel$2(this));
    private final Lazy viewModelFactory$delegate = LazyKt__LazyJVMKt.lazy(new EditBookmarkActivity$viewModelFactory$2(this));

    private final ImageButton getButtonClearLocation() {
        Lazy lazy = this.buttonClearLocation$delegate;
        KProperty kProperty = $$delegatedProperties[10];
        return (ImageButton) lazy.getValue();
    }

    private final ImageButton getButtonClearName() {
        Lazy lazy = this.buttonClearName$delegate;
        KProperty kProperty = $$delegatedProperties[9];
        return (ImageButton) lazy.getValue();
    }

    private final EditText getEditTextLocation() {
        Lazy lazy = this.editTextLocation$delegate;
        KProperty kProperty = $$delegatedProperties[4];
        return (EditText) lazy.getValue();
    }

    private final EditText getEditTextName() {
        Lazy lazy = this.editTextName$delegate;
        KProperty kProperty = $$delegatedProperties[3];
        return (EditText) lazy.getValue();
    }

    private final String getItemId() {
        Lazy lazy = this.itemId$delegate;
        KProperty kProperty = $$delegatedProperties[0];
        return (String) lazy.getValue();
    }

    private final TextView getLabelLocation() {
        Lazy lazy = this.labelLocation$delegate;
        KProperty kProperty = $$delegatedProperties[6];
        return (TextView) lazy.getValue();
    }

    private final TextView getLabelName() {
        Lazy lazy = this.labelName$delegate;
        KProperty kProperty = $$delegatedProperties[5];
        return (TextView) lazy.getValue();
    }

    private final String getOriginalLocation() {
        Lazy lazy = this.originalLocation$delegate;
        KProperty kProperty = $$delegatedProperties[8];
        return (String) lazy.getValue();
    }

    private final String getOriginalName() {
        Lazy lazy = this.originalName$delegate;
        KProperty kProperty = $$delegatedProperties[7];
        return (String) lazy.getValue();
    }

    private final BookmarkViewModel getViewModel() {
        Lazy lazy = this.viewModel$delegate;
        KProperty kProperty = $$delegatedProperties[2];
        return (BookmarkViewModel) lazy.getValue();
    }

    private final Factory getViewModelFactory() {
        Lazy lazy = this.viewModelFactory$delegate;
        KProperty kProperty = $$delegatedProperties[1];
        return (Factory) lazy.getValue();
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        view = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), view);
        return view;
    }

    public void applyLocale() {
    }

    public static final /* synthetic */ BookmarkModel access$getBookmark$p(EditBookmarkActivity editBookmarkActivity) {
        BookmarkModel bookmarkModel = editBookmarkActivity.bookmark;
        if (bookmarkModel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bookmark");
        }
        return bookmarkModel;
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0769R.layout.activity_edit_bookmark);
        setSupportActionBar((Toolbar) _$_findCachedViewById(C0427R.C0426id.toolbar));
        Drawable wrap = DrawableCompat.wrap(getResources().getDrawable(2131230855, getTheme()));
        Intrinsics.checkExpressionValueIsNotNull(wrap, "DrawableCompat.wrap(reso…wable.edit_close, theme))");
        DrawableCompat.setTint(wrap, ContextCompat.getColor(this, C0769R.color.paletteWhite100));
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(wrap);
        }
        getEditTextName().addTextChangedListener(this.nameWatcher);
        getEditTextLocation().addTextChangedListener(this.locationWatcher);
        getEditTextName().setOnFocusChangeListener(this.focusChangeListener);
        getEditTextLocation().setOnFocusChangeListener(this.focusChangeListener);
        getButtonClearName().setOnClickListener(new EditBookmarkActivity$onCreate$1(this));
        getButtonClearLocation().setOnClickListener(new EditBookmarkActivity$onCreate$2(this));
        getViewModel().getBookmarkById(getItemId()).observe(this, new EditBookmarkActivity$onCreate$3(this));
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        getEditTextName().removeTextChangedListener(this.nameWatcher);
        getEditTextLocation().removeTextChangedListener(this.locationWatcher);
        super.onDestroy();
    }

    private final boolean isSaveValid() {
        return !this.locationEmpty && (this.nameChanged || this.locationChanged);
    }

    public final void setupMenuItemSave() {
        if (this.menuItemSave != null) {
            MenuItem menuItem = this.menuItemSave;
            if (menuItem == null) {
                Intrinsics.throwUninitializedPropertyAccessException("menuItemSave");
            }
            menuItem.setEnabled(isSaveValid());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        Intrinsics.checkParameterIsNotNull(menu, "menu");
        MenuItem add = menu.add(0, 1, 0, C0769R.string.bookmark_edit_save);
        Intrinsics.checkExpressionValueIsNotNull(add, "menu.add(Menu.NONE, SAVE…tring.bookmark_edit_save)");
        this.menuItemSave = add;
        add = this.menuItemSave;
        if (add == null) {
            Intrinsics.throwUninitializedPropertyAccessException("menuItemSave");
        }
        add.setShowAsAction(2);
        setupMenuItemSave();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Integer valueOf = menuItem != null ? Integer.valueOf(menuItem.getItemId()) : null;
        if (valueOf != null && valueOf.intValue() == 1) {
            BookmarkViewModel viewModel = getViewModel();
            BookmarkModel bookmarkModel = this.bookmark;
            if (bookmarkModel == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bookmark");
            }
            viewModel.updateBookmark(new BookmarkModel(bookmarkModel.getId(), getEditTextName().getText().toString(), getEditTextLocation().getText().toString()));
            Toast.makeText(this, C0769R.string.bookmark_edit_success, 1).show();
            finish();
        } else if (valueOf != null && valueOf.intValue() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
