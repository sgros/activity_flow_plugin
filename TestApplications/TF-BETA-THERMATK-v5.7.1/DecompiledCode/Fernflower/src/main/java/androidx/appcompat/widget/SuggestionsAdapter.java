package androidx.appcompat.widget;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$id;
import androidx.core.content.ContextCompat;
import androidx.cursoradapter.widget.ResourceCursorAdapter;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.WeakHashMap;

class SuggestionsAdapter extends ResourceCursorAdapter implements OnClickListener {
   private boolean mClosed = false;
   private final int mCommitIconResId;
   private int mFlagsCol = -1;
   private int mIconName1Col = -1;
   private int mIconName2Col = -1;
   private final WeakHashMap mOutsideDrawablesCache;
   private final Context mProviderContext;
   private int mQueryRefinement = 1;
   private final SearchManager mSearchManager;
   private final SearchView mSearchView;
   private final SearchableInfo mSearchable;
   private int mText1Col = -1;
   private int mText2Col = -1;
   private int mText2UrlCol = -1;
   private ColorStateList mUrlColor;

   public SuggestionsAdapter(Context var1, SearchView var2, SearchableInfo var3, WeakHashMap var4) {
      super(var1, var2.getSuggestionRowLayout(), (Cursor)null, true);
      this.mSearchManager = (SearchManager)super.mContext.getSystemService("search");
      this.mSearchView = var2;
      this.mSearchable = var3;
      this.mCommitIconResId = var2.getSuggestionCommitIconResId();
      this.mProviderContext = var1;
      this.mOutsideDrawablesCache = var4;
   }

   private Drawable checkIconCache(String var1) {
      ConstantState var2 = (ConstantState)this.mOutsideDrawablesCache.get(var1);
      return var2 == null ? null : var2.newDrawable();
   }

   private CharSequence formatUrl(CharSequence var1) {
      if (this.mUrlColor == null) {
         TypedValue var2 = new TypedValue();
         super.mContext.getTheme().resolveAttribute(R$attr.textColorSearchUrl, var2, true);
         this.mUrlColor = super.mContext.getResources().getColorStateList(var2.resourceId);
      }

      SpannableString var3 = new SpannableString(var1);
      var3.setSpan(new TextAppearanceSpan((String)null, 0, 0, this.mUrlColor, (ColorStateList)null), 0, var1.length(), 33);
      return var3;
   }

   private Drawable getActivityIcon(ComponentName var1) {
      PackageManager var2 = super.mContext.getPackageManager();

      ActivityInfo var3;
      try {
         var3 = var2.getActivityInfo(var1, 128);
      } catch (NameNotFoundException var5) {
         Log.w("SuggestionsAdapter", var5.toString());
         return null;
      }

      int var4 = var3.getIconResource();
      if (var4 == 0) {
         return null;
      } else {
         Drawable var6 = var2.getDrawable(var1.getPackageName(), var4, var3.applicationInfo);
         if (var6 == null) {
            StringBuilder var7 = new StringBuilder();
            var7.append("Invalid icon resource ");
            var7.append(var4);
            var7.append(" for ");
            var7.append(var1.flattenToShortString());
            Log.w("SuggestionsAdapter", var7.toString());
            return null;
         } else {
            return var6;
         }
      }
   }

   private Drawable getActivityIconWithCache(ComponentName var1) {
      String var2 = var1.flattenToShortString();
      boolean var3 = this.mOutsideDrawablesCache.containsKey(var2);
      Object var4 = null;
      Drawable var5 = null;
      ConstantState var6;
      if (var3) {
         var6 = (ConstantState)this.mOutsideDrawablesCache.get(var2);
         Drawable var7;
         if (var6 == null) {
            var7 = var5;
         } else {
            var7 = var6.newDrawable(this.mProviderContext.getResources());
         }

         return var7;
      } else {
         var5 = this.getActivityIcon(var1);
         if (var5 == null) {
            var6 = (ConstantState)var4;
         } else {
            var6 = var5.getConstantState();
         }

         this.mOutsideDrawablesCache.put(var2, var6);
         return var5;
      }
   }

   public static String getColumnString(Cursor var0, String var1) {
      return getStringOrNull(var0, var0.getColumnIndex(var1));
   }

   private Drawable getDefaultIcon1(Cursor var1) {
      Drawable var2 = this.getActivityIconWithCache(this.mSearchable.getSearchActivity());
      return var2 != null ? var2 : super.mContext.getPackageManager().getDefaultActivityIcon();
   }

   private Drawable getDrawable(Uri param1) {
      // $FF: Couldn't be decompiled
   }

   private Drawable getDrawableFromResourceValue(String var1) {
      Drawable var2 = null;
      Drawable var3 = var2;
      if (var1 != null) {
         var3 = var2;
         if (!var1.isEmpty()) {
            if ("0".equals(var1)) {
               var3 = var2;
            } else {
               label39: {
                  StringBuilder var9;
                  label47: {
                     boolean var10001;
                     int var4;
                     String var10;
                     try {
                        var4 = Integer.parseInt(var1);
                        var9 = new StringBuilder();
                        var9.append("android.resource://");
                        var9.append(this.mProviderContext.getPackageName());
                        var9.append("/");
                        var9.append(var4);
                        var10 = var9.toString();
                        var2 = this.checkIconCache(var10);
                     } catch (NumberFormatException var7) {
                        var10001 = false;
                        break label39;
                     } catch (NotFoundException var8) {
                        var10001 = false;
                        break label47;
                     }

                     if (var2 != null) {
                        return var2;
                     }

                     try {
                        var2 = ContextCompat.getDrawable(this.mProviderContext, var4);
                        this.storeInIconCache(var10, var2);
                        return var2;
                     } catch (NumberFormatException var5) {
                        var10001 = false;
                        break label39;
                     } catch (NotFoundException var6) {
                        var10001 = false;
                     }
                  }

                  var9 = new StringBuilder();
                  var9.append("Icon resource not found: ");
                  var9.append(var1);
                  Log.w("SuggestionsAdapter", var9.toString());
                  return null;
               }

               var3 = this.checkIconCache(var1);
               if (var3 != null) {
                  return var3;
               }

               var3 = this.getDrawable(Uri.parse(var1));
               this.storeInIconCache(var1, var3);
            }
         }
      }

      return var3;
   }

   private Drawable getIcon1(Cursor var1) {
      int var2 = this.mIconName1Col;
      if (var2 == -1) {
         return null;
      } else {
         Drawable var3 = this.getDrawableFromResourceValue(var1.getString(var2));
         return var3 != null ? var3 : this.getDefaultIcon1(var1);
      }
   }

   private Drawable getIcon2(Cursor var1) {
      int var2 = this.mIconName2Col;
      return var2 == -1 ? null : this.getDrawableFromResourceValue(var1.getString(var2));
   }

   private static String getStringOrNull(Cursor var0, int var1) {
      if (var1 == -1) {
         return null;
      } else {
         try {
            String var3 = var0.getString(var1);
            return var3;
         } catch (Exception var2) {
            Log.e("SuggestionsAdapter", "unexpected error retrieving valid column from cursor, did the remote process die?", var2);
            return null;
         }
      }
   }

   private void setViewDrawable(ImageView var1, Drawable var2, int var3) {
      var1.setImageDrawable(var2);
      if (var2 == null) {
         var1.setVisibility(var3);
      } else {
         var1.setVisibility(0);
         var2.setVisible(false, false);
         var2.setVisible(true, false);
      }

   }

   private void setViewText(TextView var1, CharSequence var2) {
      var1.setText(var2);
      if (TextUtils.isEmpty(var2)) {
         var1.setVisibility(8);
      } else {
         var1.setVisibility(0);
      }

   }

   private void storeInIconCache(String var1, Drawable var2) {
      if (var2 != null) {
         this.mOutsideDrawablesCache.put(var1, var2.getConstantState());
      }

   }

   private void updateSpinnerState(Cursor var1) {
      Bundle var2;
      if (var1 != null) {
         var2 = var1.getExtras();
      } else {
         var2 = null;
      }

      if (var2 != null && var2.getBoolean("in_progress")) {
      }

   }

   public void bindView(View var1, Context var2, Cursor var3) {
      SuggestionsAdapter.ChildViewCache var9 = (SuggestionsAdapter.ChildViewCache)var1.getTag();
      int var4 = this.mFlagsCol;
      if (var4 != -1) {
         var4 = var3.getInt(var4);
      } else {
         var4 = 0;
      }

      String var7;
      if (var9.mText1 != null) {
         var7 = getStringOrNull(var3, this.mText1Col);
         this.setViewText(var9.mText1, var7);
      }

      if (var9.mText2 != null) {
         var7 = getStringOrNull(var3, this.mText2UrlCol);
         Object var8;
         if (var7 != null) {
            var8 = this.formatUrl(var7);
         } else {
            var8 = getStringOrNull(var3, this.mText2Col);
         }

         TextView var5;
         if (TextUtils.isEmpty((CharSequence)var8)) {
            var5 = var9.mText1;
            if (var5 != null) {
               var5.setSingleLine(false);
               var9.mText1.setMaxLines(2);
            }
         } else {
            var5 = var9.mText1;
            if (var5 != null) {
               var5.setSingleLine(true);
               var9.mText1.setMaxLines(1);
            }
         }

         this.setViewText(var9.mText2, (CharSequence)var8);
      }

      ImageView var10 = var9.mIcon1;
      if (var10 != null) {
         this.setViewDrawable(var10, this.getIcon1(var3), 4);
      }

      var10 = var9.mIcon2;
      if (var10 != null) {
         this.setViewDrawable(var10, this.getIcon2(var3), 8);
      }

      int var6 = this.mQueryRefinement;
      if (var6 == 2 || var6 == 1 && (var4 & 1) != 0) {
         var9.mIconRefine.setVisibility(0);
         var9.mIconRefine.setTag(var9.mText1.getText());
         var9.mIconRefine.setOnClickListener(this);
      } else {
         var9.mIconRefine.setVisibility(8);
      }

   }

   public void changeCursor(Cursor var1) {
      if (this.mClosed) {
         Log.w("SuggestionsAdapter", "Tried to change cursor after adapter was closed.");
         if (var1 != null) {
            var1.close();
         }

      } else {
         Exception var10000;
         label34: {
            boolean var10001;
            try {
               super.changeCursor(var1);
            } catch (Exception var3) {
               var10000 = var3;
               var10001 = false;
               break label34;
            }

            if (var1 == null) {
               return;
            }

            try {
               this.mText1Col = var1.getColumnIndex("suggest_text_1");
               this.mText2Col = var1.getColumnIndex("suggest_text_2");
               this.mText2UrlCol = var1.getColumnIndex("suggest_text_2_url");
               this.mIconName1Col = var1.getColumnIndex("suggest_icon_1");
               this.mIconName2Col = var1.getColumnIndex("suggest_icon_2");
               this.mFlagsCol = var1.getColumnIndex("suggest_flags");
               return;
            } catch (Exception var2) {
               var10000 = var2;
               var10001 = false;
            }
         }

         Exception var4 = var10000;
         Log.e("SuggestionsAdapter", "error changing cursor and caching columns", var4);
      }
   }

   public CharSequence convertToString(Cursor var1) {
      if (var1 == null) {
         return null;
      } else {
         String var2 = getColumnString(var1, "suggest_intent_query");
         if (var2 != null) {
            return var2;
         } else {
            if (this.mSearchable.shouldRewriteQueryFromData()) {
               var2 = getColumnString(var1, "suggest_intent_data");
               if (var2 != null) {
                  return var2;
               }
            }

            if (this.mSearchable.shouldRewriteQueryFromText()) {
               String var3 = getColumnString(var1, "suggest_text_1");
               if (var3 != null) {
                  return var3;
               }
            }

            return null;
         }
      }
   }

   Drawable getDrawableFromResourceUri(Uri var1) throws FileNotFoundException {
      String var2 = var1.getAuthority();
      StringBuilder var8;
      if (!TextUtils.isEmpty(var2)) {
         Resources var3;
         try {
            var3 = super.mContext.getPackageManager().getResourcesForApplication(var2);
         } catch (NameNotFoundException var7) {
            var8 = new StringBuilder();
            var8.append("No package found for authority: ");
            var8.append(var1);
            throw new FileNotFoundException(var8.toString());
         }

         List var4 = var1.getPathSegments();
         if (var4 != null) {
            int var5 = var4.size();
            if (var5 == 1) {
               try {
                  var5 = Integer.parseInt((String)var4.get(0));
               } catch (NumberFormatException var6) {
                  var8 = new StringBuilder();
                  var8.append("Single path segment is not a resource ID: ");
                  var8.append(var1);
                  throw new FileNotFoundException(var8.toString());
               }
            } else {
               if (var5 != 2) {
                  var8 = new StringBuilder();
                  var8.append("More than two path segments: ");
                  var8.append(var1);
                  throw new FileNotFoundException(var8.toString());
               }

               var5 = var3.getIdentifier((String)var4.get(1), (String)var4.get(0), var2);
            }

            if (var5 != 0) {
               return var3.getDrawable(var5);
            } else {
               var8 = new StringBuilder();
               var8.append("No resource found for: ");
               var8.append(var1);
               throw new FileNotFoundException(var8.toString());
            }
         } else {
            var8 = new StringBuilder();
            var8.append("No path: ");
            var8.append(var1);
            throw new FileNotFoundException(var8.toString());
         }
      } else {
         var8 = new StringBuilder();
         var8.append("No authority: ");
         var8.append(var1);
         throw new FileNotFoundException(var8.toString());
      }
   }

   public View getDropDownView(int var1, View var2, ViewGroup var3) {
      try {
         var2 = super.getDropDownView(var1, var2, var3);
         return var2;
      } catch (RuntimeException var4) {
         Log.w("SuggestionsAdapter", "Search suggestions cursor threw exception.", var4);
         View var5 = this.newDropDownView(super.mContext, super.mCursor, var3);
         if (var5 != null) {
            ((SuggestionsAdapter.ChildViewCache)var5.getTag()).mText1.setText(var4.toString());
         }

         return var5;
      }
   }

   Cursor getSearchManagerSuggestions(SearchableInfo var1, String var2, int var3) {
      Object var4 = null;
      if (var1 == null) {
         return null;
      } else {
         String var5 = var1.getSuggestAuthority();
         if (var5 == null) {
            return null;
         } else {
            Builder var9 = (new Builder()).scheme("content").authority(var5).query("").fragment("");
            String var6 = var1.getSuggestPath();
            if (var6 != null) {
               var9.appendEncodedPath(var6);
            }

            var9.appendPath("search_suggest_query");
            var6 = var1.getSuggestSelection();
            String[] var7;
            if (var6 != null) {
               var7 = new String[]{var2};
            } else {
               var9.appendPath(var2);
               var7 = (String[])var4;
            }

            if (var3 > 0) {
               var9.appendQueryParameter("limit", String.valueOf(var3));
            }

            Uri var8 = var9.build();
            return super.mContext.getContentResolver().query(var8, (String[])null, var6, var7, (String)null);
         }
      }
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      try {
         var2 = super.getView(var1, var2, var3);
         return var2;
      } catch (RuntimeException var4) {
         Log.w("SuggestionsAdapter", "Search suggestions cursor threw exception.", var4);
         View var5 = this.newView(super.mContext, super.mCursor, var3);
         if (var5 != null) {
            ((SuggestionsAdapter.ChildViewCache)var5.getTag()).mText1.setText(var4.toString());
         }

         return var5;
      }
   }

   public boolean hasStableIds() {
      return false;
   }

   public View newView(Context var1, Cursor var2, ViewGroup var3) {
      View var4 = super.newView(var1, var2, var3);
      var4.setTag(new SuggestionsAdapter.ChildViewCache(var4));
      ((ImageView)var4.findViewById(R$id.edit_query)).setImageResource(this.mCommitIconResId);
      return var4;
   }

   public void notifyDataSetChanged() {
      super.notifyDataSetChanged();
      this.updateSpinnerState(this.getCursor());
   }

   public void notifyDataSetInvalidated() {
      super.notifyDataSetInvalidated();
      this.updateSpinnerState(this.getCursor());
   }

   public void onClick(View var1) {
      Object var2 = var1.getTag();
      if (var2 instanceof CharSequence) {
         this.mSearchView.onQueryRefine((CharSequence)var2);
      }

   }

   public Cursor runQueryOnBackgroundThread(CharSequence var1) {
      String var4;
      if (var1 == null) {
         var4 = "";
      } else {
         var4 = var1.toString();
      }

      if (this.mSearchView.getVisibility() == 0 && this.mSearchView.getWindowVisibility() == 0) {
         RuntimeException var10000;
         label37: {
            boolean var10001;
            Cursor var5;
            try {
               var5 = this.getSearchManagerSuggestions(this.mSearchable, var4, 50);
            } catch (RuntimeException var3) {
               var10000 = var3;
               var10001 = false;
               break label37;
            }

            if (var5 == null) {
               return null;
            }

            try {
               var5.getCount();
               return var5;
            } catch (RuntimeException var2) {
               var10000 = var2;
               var10001 = false;
            }
         }

         RuntimeException var6 = var10000;
         Log.w("SuggestionsAdapter", "Search suggestions query threw an exception.", var6);
      }

      return null;
   }

   public void setQueryRefinement(int var1) {
      this.mQueryRefinement = var1;
   }

   private static final class ChildViewCache {
      public final ImageView mIcon1;
      public final ImageView mIcon2;
      public final ImageView mIconRefine;
      public final TextView mText1;
      public final TextView mText2;

      public ChildViewCache(View var1) {
         this.mText1 = (TextView)var1.findViewById(16908308);
         this.mText2 = (TextView)var1.findViewById(16908309);
         this.mIcon1 = (ImageView)var1.findViewById(16908295);
         this.mIcon2 = (ImageView)var1.findViewById(16908296);
         this.mIconRefine = (ImageView)var1.findViewById(R$id.edit_query);
      }
   }
}
