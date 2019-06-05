package org.mozilla.focus.activity;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import org.mozilla.focus.R;
import org.mozilla.focus.persistence.BookmarkModel;
import org.mozilla.focus.persistence.BookmarksDatabase;
import org.mozilla.focus.repository.BookmarkRepository;
import org.mozilla.focus.viewmodel.BookmarkViewModel;

public final class EditBookmarkActivity extends BaseActivity {
   // $FF: synthetic field
   static final KProperty[] $$delegatedProperties = new KProperty[]{(KProperty)Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "itemId", "getItemId()Ljava/lang/String;")), (KProperty)Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "viewModelFactory", "getViewModelFactory()Lorg/mozilla/focus/viewmodel/BookmarkViewModel$Factory;")), (KProperty)Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "viewModel", "getViewModel()Lorg/mozilla/focus/viewmodel/BookmarkViewModel;")), (KProperty)Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "editTextName", "getEditTextName()Landroid/widget/EditText;")), (KProperty)Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "editTextLocation", "getEditTextLocation()Landroid/widget/EditText;")), (KProperty)Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "labelName", "getLabelName()Landroid/widget/TextView;")), (KProperty)Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "labelLocation", "getLabelLocation()Landroid/widget/TextView;")), (KProperty)Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "originalName", "getOriginalName()Ljava/lang/String;")), (KProperty)Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "originalLocation", "getOriginalLocation()Ljava/lang/String;")), (KProperty)Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "buttonClearName", "getButtonClearName()Landroid/widget/ImageButton;")), (KProperty)Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(EditBookmarkActivity.class), "buttonClearLocation", "getButtonClearLocation()Landroid/widget/ImageButton;"))};
   private HashMap _$_findViewCache;
   private BookmarkModel bookmark;
   private final Lazy buttonClearLocation$delegate = LazyKt.lazy((Function0)(new Function0() {
      public final ImageButton invoke() {
         return (ImageButton)EditBookmarkActivity.this.findViewById(2131296319);
      }
   }));
   private final Lazy buttonClearName$delegate = LazyKt.lazy((Function0)(new Function0() {
      public final ImageButton invoke() {
         return (ImageButton)EditBookmarkActivity.this.findViewById(2131296322);
      }
   }));
   private final Lazy editTextLocation$delegate = LazyKt.lazy((Function0)(new Function0() {
      public final EditText invoke() {
         return (EditText)EditBookmarkActivity.this.findViewById(2131296318);
      }
   }));
   private final Lazy editTextName$delegate = LazyKt.lazy((Function0)(new Function0() {
      public final EditText invoke() {
         return (EditText)EditBookmarkActivity.this.findViewById(2131296321);
      }
   }));
   private final OnFocusChangeListener focusChangeListener = (OnFocusChangeListener)(new OnFocusChangeListener() {
      public final void onFocusChange(View var1, boolean var2) {
         Intrinsics.checkExpressionValueIsNotNull(var1, "v");
         int var3 = var1.getId();
         if (var3 != 2131296318) {
            if (var3 == 2131296321) {
               EditBookmarkActivity.this.getLabelName().setActivated(var2);
            }
         } else {
            EditBookmarkActivity.this.getLabelLocation().setActivated(var2);
         }

      }
   });
   private final Lazy itemId$delegate = LazyKt.lazy((Function0)(new Function0() {
      public final String invoke() {
         return EditBookmarkActivity.this.getIntent().getStringExtra("ITEM_UUID_KEY");
      }
   }));
   private final Lazy labelLocation$delegate = LazyKt.lazy((Function0)(new Function0() {
      public final TextView invoke() {
         return (TextView)EditBookmarkActivity.this.findViewById(2131296320);
      }
   }));
   private final Lazy labelName$delegate = LazyKt.lazy((Function0)(new Function0() {
      public final TextView invoke() {
         return (TextView)EditBookmarkActivity.this.findViewById(2131296323);
      }
   }));
   private boolean locationChanged;
   private boolean locationEmpty;
   private final TextWatcher locationWatcher = (TextWatcher)(new TextWatcher() {
      public void afterTextChanged(Editable var1) {
         if (EditBookmarkActivity.this.bookmark != null) {
            EditBookmarkActivity.this.locationChanged = Intrinsics.areEqual(String.valueOf(var1), EditBookmarkActivity.this.getOriginalLocation()) ^ true;
            EditBookmarkActivity.this.locationEmpty = TextUtils.isEmpty((CharSequence)var1);
            EditBookmarkActivity.this.setupMenuItemSave();
         }

      }

      public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }

      public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }
   });
   private MenuItem menuItemSave;
   private boolean nameChanged;
   private final TextWatcher nameWatcher = (TextWatcher)(new TextWatcher() {
      public void afterTextChanged(Editable var1) {
         if (EditBookmarkActivity.this.bookmark != null) {
            EditBookmarkActivity.this.nameChanged = Intrinsics.areEqual(String.valueOf(var1), EditBookmarkActivity.this.getOriginalName()) ^ true;
            EditBookmarkActivity.this.setupMenuItemSave();
         }

      }

      public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }

      public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }
   });
   private final Lazy originalLocation$delegate = LazyKt.lazy((Function0)(new Function0() {
      public final String invoke() {
         return EditBookmarkActivity.access$getBookmark$p(EditBookmarkActivity.this).getUrl();
      }
   }));
   private final Lazy originalName$delegate = LazyKt.lazy((Function0)(new Function0() {
      public final String invoke() {
         return EditBookmarkActivity.access$getBookmark$p(EditBookmarkActivity.this).getTitle();
      }
   }));
   private final Lazy viewModel$delegate = LazyKt.lazy((Function0)(new Function0() {
      public final BookmarkViewModel invoke() {
         return (BookmarkViewModel)ViewModelProviders.of((FragmentActivity)EditBookmarkActivity.this, (ViewModelProvider.Factory)EditBookmarkActivity.this.getViewModelFactory()).get(BookmarkViewModel.class);
      }
   }));
   private final Lazy viewModelFactory$delegate = LazyKt.lazy((Function0)(new Function0() {
      public final BookmarkViewModel.Factory invoke() {
         return new BookmarkViewModel.Factory(BookmarkRepository.getInstance(BookmarksDatabase.getInstance((Context)EditBookmarkActivity.this)));
      }
   }));

   // $FF: synthetic method
   public static final BookmarkModel access$getBookmark$p(EditBookmarkActivity var0) {
      BookmarkModel var1 = var0.bookmark;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("bookmark");
      }

      return var1;
   }

   private final ImageButton getButtonClearLocation() {
      Lazy var1 = this.buttonClearLocation$delegate;
      KProperty var2 = $$delegatedProperties[10];
      return (ImageButton)var1.getValue();
   }

   private final ImageButton getButtonClearName() {
      Lazy var1 = this.buttonClearName$delegate;
      KProperty var2 = $$delegatedProperties[9];
      return (ImageButton)var1.getValue();
   }

   private final EditText getEditTextLocation() {
      Lazy var1 = this.editTextLocation$delegate;
      KProperty var2 = $$delegatedProperties[4];
      return (EditText)var1.getValue();
   }

   private final EditText getEditTextName() {
      Lazy var1 = this.editTextName$delegate;
      KProperty var2 = $$delegatedProperties[3];
      return (EditText)var1.getValue();
   }

   private final String getItemId() {
      Lazy var1 = this.itemId$delegate;
      KProperty var2 = $$delegatedProperties[0];
      return (String)var1.getValue();
   }

   private final TextView getLabelLocation() {
      Lazy var1 = this.labelLocation$delegate;
      KProperty var2 = $$delegatedProperties[6];
      return (TextView)var1.getValue();
   }

   private final TextView getLabelName() {
      Lazy var1 = this.labelName$delegate;
      KProperty var2 = $$delegatedProperties[5];
      return (TextView)var1.getValue();
   }

   private final String getOriginalLocation() {
      Lazy var1 = this.originalLocation$delegate;
      KProperty var2 = $$delegatedProperties[8];
      return (String)var1.getValue();
   }

   private final String getOriginalName() {
      Lazy var1 = this.originalName$delegate;
      KProperty var2 = $$delegatedProperties[7];
      return (String)var1.getValue();
   }

   private final BookmarkViewModel getViewModel() {
      Lazy var1 = this.viewModel$delegate;
      KProperty var2 = $$delegatedProperties[2];
      return (BookmarkViewModel)var1.getValue();
   }

   private final BookmarkViewModel.Factory getViewModelFactory() {
      Lazy var1 = this.viewModelFactory$delegate;
      KProperty var2 = $$delegatedProperties[1];
      return (BookmarkViewModel.Factory)var1.getValue();
   }

   private final boolean isSaveValid() {
      boolean var1;
      if (this.locationEmpty || !this.nameChanged && !this.locationChanged) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public View _$_findCachedViewById(int var1) {
      if (this._$_findViewCache == null) {
         this._$_findViewCache = new HashMap();
      }

      View var2 = (View)this._$_findViewCache.get(var1);
      View var3 = var2;
      if (var2 == null) {
         var3 = this.findViewById(var1);
         this._$_findViewCache.put(var1, var3);
      }

      return var3;
   }

   public void applyLocale() {
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131492892);
      this.setSupportActionBar((Toolbar)this._$_findCachedViewById(R.id.toolbar));
      Drawable var3 = DrawableCompat.wrap(this.getResources().getDrawable(2131230855, this.getTheme()));
      Intrinsics.checkExpressionValueIsNotNull(var3, "DrawableCompat.wrap(reso…wable.edit_close, theme))");
      DrawableCompat.setTint(var3, ContextCompat.getColor((Context)this, 2131099846));
      ActionBar var2 = this.getSupportActionBar();
      if (var2 != null) {
         var2.setDisplayHomeAsUpEnabled(true);
      }

      var2 = this.getSupportActionBar();
      if (var2 != null) {
         var2.setHomeAsUpIndicator(var3);
      }

      this.getEditTextName().addTextChangedListener(this.nameWatcher);
      this.getEditTextLocation().addTextChangedListener(this.locationWatcher);
      this.getEditTextName().setOnFocusChangeListener(this.focusChangeListener);
      this.getEditTextLocation().setOnFocusChangeListener(this.focusChangeListener);
      this.getButtonClearName().setOnClickListener((OnClickListener)(new OnClickListener() {
         public final void onClick(View var1) {
            EditBookmarkActivity.this.getEditTextName().getText().clear();
         }
      }));
      this.getButtonClearLocation().setOnClickListener((OnClickListener)(new OnClickListener() {
         public final void onClick(View var1) {
            EditBookmarkActivity.this.getEditTextLocation().getText().clear();
         }
      }));
      this.getViewModel().getBookmarkById(this.getItemId()).observe((LifecycleOwner)this, (Observer)(new Observer() {
         public final void onChanged(BookmarkModel var1) {
            if (var1 != null) {
               EditBookmarkActivity.this.bookmark = var1;
               EditBookmarkActivity.this.getEditTextName().setText((CharSequence)EditBookmarkActivity.access$getBookmark$p(EditBookmarkActivity.this).getTitle());
               EditBookmarkActivity.this.getEditTextLocation().setText((CharSequence)EditBookmarkActivity.access$getBookmark$p(EditBookmarkActivity.this).getUrl());
            }

         }
      }));
   }

   public boolean onCreateOptionsMenu(Menu var1) {
      Intrinsics.checkParameterIsNotNull(var1, "menu");
      MenuItem var2 = var1.add(0, 1, 0, 2131755066);
      Intrinsics.checkExpressionValueIsNotNull(var2, "menu.add(Menu.NONE, SAVE…tring.bookmark_edit_save)");
      this.menuItemSave = var2;
      var2 = this.menuItemSave;
      if (var2 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("menuItemSave");
      }

      var2.setShowAsAction(2);
      this.setupMenuItemSave();
      return true;
   }

   protected void onDestroy() {
      this.getEditTextName().removeTextChangedListener(this.nameWatcher);
      this.getEditTextLocation().removeTextChangedListener(this.locationWatcher);
      super.onDestroy();
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      Integer var2;
      if (var1 != null) {
         var2 = var1.getItemId();
      } else {
         var2 = null;
      }

      if (var2 != null && var2 == 1) {
         BookmarkViewModel var4 = this.getViewModel();
         BookmarkModel var3 = this.bookmark;
         if (var3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bookmark");
         }

         var4.updateBookmark(new BookmarkModel(var3.getId(), this.getEditTextName().getText().toString(), this.getEditTextLocation().getText().toString()));
         Toast.makeText((Context)this, 2131755067, 1).show();
         this.finish();
      } else if (var2 != null && var2 == 16908332) {
         this.onBackPressed();
      }

      return super.onOptionsItemSelected(var1);
   }

   public final void setupMenuItemSave() {
      if (((EditBookmarkActivity)this).menuItemSave != null) {
         MenuItem var1 = this.menuItemSave;
         if (var1 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("menuItemSave");
         }

         var1.setEnabled(this.isSaveValid());
      }

   }
}
