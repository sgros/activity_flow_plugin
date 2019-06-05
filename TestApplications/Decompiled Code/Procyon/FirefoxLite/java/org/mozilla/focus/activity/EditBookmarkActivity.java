// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.activity;

import android.widget.Toast;
import android.view.Menu;
import android.support.v7.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.view.View$OnClickListener;
import android.support.v4.content.ContextCompat;
import android.content.Context;
import android.support.v4.graphics.drawable.DrawableCompat;
import org.mozilla.focus.R;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import org.mozilla.focus.viewmodel.BookmarkViewModel;
import android.widget.TextView;
import android.widget.EditText;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.PropertyReference1;
import kotlin.reflect.KDeclarationContainer;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty1;
import android.view.MenuItem;
import android.text.TextWatcher;
import android.view.View$OnFocusChangeListener;
import kotlin.Lazy;
import org.mozilla.focus.persistence.BookmarkModel;
import java.util.HashMap;
import kotlin.reflect.KProperty;

public final class EditBookmarkActivity extends BaseActivity
{
    static final /* synthetic */ KProperty[] $$delegatedProperties;
    private HashMap _$_findViewCache;
    private BookmarkModel bookmark;
    private final Lazy buttonClearLocation$delegate;
    private final Lazy buttonClearName$delegate;
    private final Lazy editTextLocation$delegate;
    private final Lazy editTextName$delegate;
    private final View$OnFocusChangeListener focusChangeListener;
    private final Lazy itemId$delegate;
    private final Lazy labelLocation$delegate;
    private final Lazy labelName$delegate;
    private boolean locationChanged;
    private boolean locationEmpty;
    private final TextWatcher locationWatcher;
    private MenuItem menuItemSave;
    private boolean nameChanged;
    private final TextWatcher nameWatcher;
    private final Lazy originalLocation$delegate;
    private final Lazy originalName$delegate;
    private final Lazy viewModel$delegate;
    private final Lazy viewModelFactory$delegate;
    
    static {
        $$delegatedProperties = new KProperty[] { Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "itemId", "getItemId()Ljava/lang/String;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "viewModelFactory", "getViewModelFactory()Lorg/mozilla/focus/viewmodel/BookmarkViewModel$Factory;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "viewModel", "getViewModel()Lorg/mozilla/focus/viewmodel/BookmarkViewModel;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "editTextName", "getEditTextName()Landroid/widget/EditText;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "editTextLocation", "getEditTextLocation()Landroid/widget/EditText;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "labelName", "getLabelName()Landroid/widget/TextView;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "labelLocation", "getLabelLocation()Landroid/widget/TextView;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "originalName", "getOriginalName()Ljava/lang/String;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "originalLocation", "getOriginalLocation()Ljava/lang/String;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "buttonClearName", "getButtonClearName()Landroid/widget/ImageButton;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "buttonClearLocation", "getButtonClearLocation()Landroid/widget/ImageButton;")) };
    }
    
    public EditBookmarkActivity() {
        this.itemId$delegate = LazyKt__LazyJVMKt.lazy((Function0<?>)new EditBookmarkActivity$itemId.EditBookmarkActivity$itemId$2(this));
        this.viewModelFactory$delegate = LazyKt__LazyJVMKt.lazy((Function0<?>)new EditBookmarkActivity$viewModelFactory.EditBookmarkActivity$viewModelFactory$2(this));
        this.viewModel$delegate = LazyKt__LazyJVMKt.lazy((Function0<?>)new EditBookmarkActivity$viewModel.EditBookmarkActivity$viewModel$2(this));
        this.editTextName$delegate = LazyKt__LazyJVMKt.lazy((Function0<?>)new EditBookmarkActivity$editTextName.EditBookmarkActivity$editTextName$2(this));
        this.editTextLocation$delegate = LazyKt__LazyJVMKt.lazy((Function0<?>)new EditBookmarkActivity$editTextLocation.EditBookmarkActivity$editTextLocation$2(this));
        this.labelName$delegate = LazyKt__LazyJVMKt.lazy((Function0<?>)new EditBookmarkActivity$labelName.EditBookmarkActivity$labelName$2(this));
        this.labelLocation$delegate = LazyKt__LazyJVMKt.lazy((Function0<?>)new EditBookmarkActivity$labelLocation.EditBookmarkActivity$labelLocation$2(this));
        this.originalName$delegate = LazyKt__LazyJVMKt.lazy((Function0<?>)new EditBookmarkActivity$originalName.EditBookmarkActivity$originalName$2(this));
        this.originalLocation$delegate = LazyKt__LazyJVMKt.lazy((Function0<?>)new EditBookmarkActivity$originalLocation.EditBookmarkActivity$originalLocation$2(this));
        this.buttonClearName$delegate = LazyKt__LazyJVMKt.lazy((Function0<?>)new EditBookmarkActivity$buttonClearName.EditBookmarkActivity$buttonClearName$2(this));
        this.buttonClearLocation$delegate = LazyKt__LazyJVMKt.lazy((Function0<?>)new EditBookmarkActivity$buttonClearLocation.EditBookmarkActivity$buttonClearLocation$2(this));
        this.nameWatcher = (TextWatcher)new EditBookmarkActivity$nameWatcher.EditBookmarkActivity$nameWatcher$1(this);
        this.locationWatcher = (TextWatcher)new EditBookmarkActivity$locationWatcher.EditBookmarkActivity$locationWatcher$1(this);
        this.focusChangeListener = (View$OnFocusChangeListener)new EditBookmarkActivity$focusChangeListener.EditBookmarkActivity$focusChangeListener$1(this);
    }
    
    private final ImageButton getButtonClearLocation() {
        final Lazy buttonClearLocation$delegate = this.buttonClearLocation$delegate;
        final KProperty kProperty = EditBookmarkActivity.$$delegatedProperties[10];
        return buttonClearLocation$delegate.getValue();
    }
    
    private final ImageButton getButtonClearName() {
        final Lazy buttonClearName$delegate = this.buttonClearName$delegate;
        final KProperty kProperty = EditBookmarkActivity.$$delegatedProperties[9];
        return buttonClearName$delegate.getValue();
    }
    
    private final EditText getEditTextLocation() {
        final Lazy editTextLocation$delegate = this.editTextLocation$delegate;
        final KProperty kProperty = EditBookmarkActivity.$$delegatedProperties[4];
        return editTextLocation$delegate.getValue();
    }
    
    private final EditText getEditTextName() {
        final Lazy editTextName$delegate = this.editTextName$delegate;
        final KProperty kProperty = EditBookmarkActivity.$$delegatedProperties[3];
        return editTextName$delegate.getValue();
    }
    
    private final String getItemId() {
        final Lazy itemId$delegate = this.itemId$delegate;
        final KProperty kProperty = EditBookmarkActivity.$$delegatedProperties[0];
        return itemId$delegate.getValue();
    }
    
    private final TextView getLabelLocation() {
        final Lazy labelLocation$delegate = this.labelLocation$delegate;
        final KProperty kProperty = EditBookmarkActivity.$$delegatedProperties[6];
        return labelLocation$delegate.getValue();
    }
    
    private final TextView getLabelName() {
        final Lazy labelName$delegate = this.labelName$delegate;
        final KProperty kProperty = EditBookmarkActivity.$$delegatedProperties[5];
        return labelName$delegate.getValue();
    }
    
    private final String getOriginalLocation() {
        final Lazy originalLocation$delegate = this.originalLocation$delegate;
        final KProperty kProperty = EditBookmarkActivity.$$delegatedProperties[8];
        return originalLocation$delegate.getValue();
    }
    
    private final String getOriginalName() {
        final Lazy originalName$delegate = this.originalName$delegate;
        final KProperty kProperty = EditBookmarkActivity.$$delegatedProperties[7];
        return originalName$delegate.getValue();
    }
    
    private final BookmarkViewModel getViewModel() {
        final Lazy viewModel$delegate = this.viewModel$delegate;
        final KProperty kProperty = EditBookmarkActivity.$$delegatedProperties[2];
        return viewModel$delegate.getValue();
    }
    
    private final BookmarkViewModel.Factory getViewModelFactory() {
        final Lazy viewModelFactory$delegate = this.viewModelFactory$delegate;
        final KProperty kProperty = EditBookmarkActivity.$$delegatedProperties[1];
        return viewModelFactory$delegate.getValue();
    }
    
    private final boolean isSaveValid() {
        return !this.locationEmpty && (this.nameChanged || this.locationChanged);
    }
    
    public View _$_findCachedViewById(final int n) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View viewById;
        if ((viewById = this._$_findViewCache.get(n)) == null) {
            viewById = this.findViewById(n);
            this._$_findViewCache.put(n, viewById);
        }
        return viewById;
    }
    
    @Override
    public void applyLocale() {
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131492892);
        this.setSupportActionBar((Toolbar)this._$_findCachedViewById(R.id.toolbar));
        final Drawable wrap = DrawableCompat.wrap(this.getResources().getDrawable(2131230855, this.getTheme()));
        Intrinsics.checkExpressionValueIsNotNull(wrap, "DrawableCompat.wrap(reso\u2026wable.edit_close, theme))");
        DrawableCompat.setTint(wrap, ContextCompat.getColor((Context)this, 2131099846));
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        final ActionBar supportActionBar2 = this.getSupportActionBar();
        if (supportActionBar2 != null) {
            supportActionBar2.setHomeAsUpIndicator(wrap);
        }
        this.getEditTextName().addTextChangedListener(this.nameWatcher);
        this.getEditTextLocation().addTextChangedListener(this.locationWatcher);
        this.getEditTextName().setOnFocusChangeListener(this.focusChangeListener);
        this.getEditTextLocation().setOnFocusChangeListener(this.focusChangeListener);
        this.getButtonClearName().setOnClickListener((View$OnClickListener)new EditBookmarkActivity$onCreate.EditBookmarkActivity$onCreate$1(this));
        this.getButtonClearLocation().setOnClickListener((View$OnClickListener)new EditBookmarkActivity$onCreate.EditBookmarkActivity$onCreate$2(this));
        this.getViewModel().getBookmarkById(this.getItemId()).observe(this, (Observer<BookmarkModel>)new EditBookmarkActivity$onCreate.EditBookmarkActivity$onCreate$3(this));
    }
    
    public boolean onCreateOptionsMenu(final Menu menu) {
        Intrinsics.checkParameterIsNotNull(menu, "menu");
        final MenuItem add = menu.add(0, 1, 0, 2131755066);
        Intrinsics.checkExpressionValueIsNotNull(add, "menu.add(Menu.NONE, SAVE\u2026tring.bookmark_edit_save)");
        this.menuItemSave = add;
        final MenuItem menuItemSave = this.menuItemSave;
        if (menuItemSave == null) {
            Intrinsics.throwUninitializedPropertyAccessException("menuItemSave");
        }
        menuItemSave.setShowAsAction(2);
        this.setupMenuItemSave();
        return true;
    }
    
    @Override
    protected void onDestroy() {
        this.getEditTextName().removeTextChangedListener(this.nameWatcher);
        this.getEditTextLocation().removeTextChangedListener(this.locationWatcher);
        super.onDestroy();
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        Integer value;
        if (menuItem != null) {
            value = menuItem.getItemId();
        }
        else {
            value = null;
        }
        if (value != null) {
            if (value == 1) {
                final BookmarkViewModel viewModel = this.getViewModel();
                final BookmarkModel bookmark = this.bookmark;
                if (bookmark == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("bookmark");
                }
                viewModel.updateBookmark(new BookmarkModel(bookmark.getId(), this.getEditTextName().getText().toString(), this.getEditTextLocation().getText().toString()));
                Toast.makeText((Context)this, 2131755067, 1).show();
                this.finish();
                return super.onOptionsItemSelected(menuItem);
            }
        }
        if (value != null) {
            if (value == 16908332) {
                this.onBackPressed();
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }
    
    public final void setupMenuItemSave() {
        if (this.menuItemSave != null) {
            final MenuItem menuItemSave = this.menuItemSave;
            if (menuItemSave == null) {
                Intrinsics.throwUninitializedPropertyAccessException("menuItemSave");
            }
            menuItemSave.setEnabled(this.isSaveValid());
        }
    }
}
