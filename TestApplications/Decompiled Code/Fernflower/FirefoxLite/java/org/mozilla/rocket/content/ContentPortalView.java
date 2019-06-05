package org.mozilla.rocket.content;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import java.util.ArrayList;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.rocket.content.data.ShoppingLink;

public final class ContentPortalView extends CoordinatorLayout implements ContentPortalListener {
   public static final ContentPortalView.Companion Companion = new ContentPortalView.Companion((DefaultConstructorMarker)null);
   private LinearLayout bottomSheet;
   private BottomSheetBehavior bottomSheetBehavior;
   private NewsAdapter newsAdapter;
   private View newsEmptyView;
   private LinearLayoutManager newsListLayoutManager;
   private ContentPortalView.NewsListListener newsListListener;
   private ProgressBar newsProgressCenter;
   private RecyclerView recyclerView;

   public ContentPortalView(Context var1) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      super(var1);
   }

   public ContentPortalView(Context var1, AttributeSet var2) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      super(var1, var2);
   }

   public ContentPortalView(Context var1, AttributeSet var2, int var3) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      super(var1, var2, var3);
   }

   private final void init() {
      this.setupContentPortalView();
      if (AppConfigWrapper.hasEcommerceShoppingLink()) {
         this.setupViewShoppingLink();
      } else {
         this.setupViewNews();
      }

   }

   private final void setupContentPortalView() {
      this.setOnClickListener((OnClickListener)(new OnClickListener() {
         public final void onClick(View var1) {
            ContentPortalView.this.hide();
         }
      }));
      this.bottomSheet = (LinearLayout)this.findViewById(2131296328);
      this.bottomSheetBehavior = BottomSheetBehavior.from((View)this.bottomSheet);
      BottomSheetBehavior var1 = this.bottomSheetBehavior;
      if (var1 != null) {
         var1.setState(3);
      }

      var1 = this.bottomSheetBehavior;
      if (var1 != null) {
         var1.setBottomSheetCallback((BottomSheetBehavior.BottomSheetCallback)(new BottomSheetBehavior.BottomSheetCallback() {
            public void onSlide(View var1, float var2) {
               Intrinsics.checkParameterIsNotNull(var1, "bottomSheet");
            }

            public void onStateChanged(View var1, int var2) {
               Intrinsics.checkParameterIsNotNull(var1, "bottomSheet");
               if (var2 == 5) {
                  ContentPortalView.this.hide();
               }

            }
         }));
      }

   }

   private final void setupViewNews() {
      Object var1 = this.getContext().getSystemService("layout_inflater");
      if (var1 != null) {
         ((LayoutInflater)var1).inflate(2131492921, (ViewGroup)this.bottomSheet);
         Button var3 = (Button)this.findViewById(2131296546);
         if (var3 != null) {
            var3.setOnClickListener((OnClickListener)(new OnClickListener() {
               public final void onClick(View var1) {
                  ContentPortalView.NewsListListener var2 = ContentPortalView.this.getNewsListListener();
                  if (var2 != null) {
                     var2.loadMore();
                  }

               }
            }));
         }

         this.recyclerView = (RecyclerView)this.findViewById(2131296543);
         this.newsEmptyView = this.findViewById(2131296422);
         this.newsProgressCenter = (ProgressBar)this.findViewById(2131296544);
         this.newsAdapter = new NewsAdapter((ContentPortalListener)this);
         RecyclerView var4 = this.recyclerView;
         if (var4 != null) {
            var4.setAdapter((RecyclerView.Adapter)this.newsAdapter);
         }

         this.newsListLayoutManager = new LinearLayoutManager(this.getContext(), 1, false);
         var4 = this.recyclerView;
         if (var4 != null) {
            var4.setLayoutManager((RecyclerView.LayoutManager)this.newsListLayoutManager);
         }

         final LinearLayoutManager var5 = this.newsListLayoutManager;
         if (var5 != null) {
            RecyclerView var2 = this.recyclerView;
            if (var2 != null) {
               var2.addOnScrollListener((RecyclerView.OnScrollListener)(new RecyclerView.OnScrollListener() {
                  public void onScrolled(RecyclerView var1, int var2, int var3) {
                     Intrinsics.checkParameterIsNotNull(var1, "recyclerView");
                     super.onScrolled(var1, var2, var3);
                     var2 = var5.getItemCount();
                     if (var5.getChildCount() + var5.findLastVisibleItemPosition() + 10 >= var2) {
                        ContentPortalView.NewsListListener var4 = ContentPortalView.this.getNewsListListener();
                        if (var4 != null) {
                           var4.loadMore();
                        }
                     }

                  }
               }));
            }
         }

      } else {
         throw new TypeCastException("null cannot be cast to non-null type android.view.LayoutInflater");
      }
   }

   private final void setupViewShoppingLink() {
      Object var1 = this.getContext().getSystemService("layout_inflater");
      if (var1 != null) {
         ((LayoutInflater)var1).inflate(2131492923, (ViewGroup)this.bottomSheet);
         this.recyclerView = (RecyclerView)this.findViewById(2131296387);
         ShoppingLinkAdapter var3 = new ShoppingLinkAdapter((ContentPortalListener)this);
         RecyclerView var2 = this.recyclerView;
         if (var2 != null) {
            var2.setLayoutManager((RecyclerView.LayoutManager)(new GridLayoutManager(this.getContext(), 2)));
         }

         var2 = this.recyclerView;
         if (var2 != null) {
            var2.setAdapter((RecyclerView.Adapter)var3);
         }

         ArrayList var4 = AppConfigWrapper.getEcommerceShoppingLinks();
         var4.add(new ShoppingLink("", "footer", "", ""));
         var3.submitList((List)var4);
      } else {
         throw new TypeCastException("null cannot be cast to non-null type android.view.LayoutInflater");
      }
   }

   private final void showInternal() {
      this.setVisibility(0);
      BottomSheetBehavior var1 = this.bottomSheetBehavior;
      if (var1 != null) {
         var1.setState(3);
      }

   }

   public final ContentPortalView.NewsListListener getNewsListListener() {
      return this.newsListListener;
   }

   public final boolean hide() {
      if (this.getVisibility() == 8) {
         return false;
      } else {
         HomeFragmentViewState.reset();
         NewsRepository.Companion.reset();
         Animation var1 = AnimationUtils.loadAnimation(this.getContext(), 2130771990);
         if (var1 != null) {
            var1.setAnimationListener((AnimationListener)(new AnimationListener() {
               public void onAnimationEnd(Animation var1) {
                  ContentPortalView.this.setVisibility(8);
               }

               public void onAnimationRepeat(Animation var1) {
               }

               public void onAnimationStart(Animation var1) {
               }
            }));
            this.startAnimation(var1);
         }

         return true;
      }
   }

   public void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.init();
   }

   public void onItemClicked(String var1) {
      Intrinsics.checkParameterIsNotNull(var1, "url");
      ScreenNavigator.get(this.getContext()).showBrowserScreen(var1, true, false);
      LinearLayoutManager var3 = this.newsListLayoutManager;
      if (var3 != null) {
         int var2 = var3.findFirstVisibleItemPosition();
         HomeFragmentViewState.INSTANCE.setLastScrollPos(var2);
      }

   }

   public final void onResume() {
      if (HomeFragmentViewState.INSTANCE.isLastOpenNews()) {
         this.show(false);
      } else {
         this.hide();
      }

   }

   public void onStatus(List var1) {
      RecyclerView var2;
      View var3;
      ProgressBar var4;
      BottomSheetBehavior var5;
      if (var1 == null) {
         var2 = this.recyclerView;
         if (var2 != null) {
            var2.setVisibility(8);
         }

         var3 = this.newsEmptyView;
         if (var3 != null) {
            var3.setVisibility(8);
         }

         var4 = this.newsProgressCenter;
         if (var4 != null) {
            var4.setVisibility(0);
         }

         var5 = this.bottomSheetBehavior;
         if (var5 != null) {
            var5.setSkipCollapsed(true);
         }
      } else if (var1.size() == 0) {
         var2 = this.recyclerView;
         if (var2 != null) {
            var2.setVisibility(8);
         }

         var3 = this.newsEmptyView;
         if (var3 != null) {
            var3.setVisibility(0);
         }

         var4 = this.newsProgressCenter;
         if (var4 != null) {
            var4.setVisibility(8);
         }

         var5 = this.bottomSheetBehavior;
         if (var5 != null) {
            var5.setSkipCollapsed(true);
         }
      } else {
         var2 = this.recyclerView;
         if (var2 != null) {
            var2.setVisibility(0);
         }

         var3 = this.newsEmptyView;
         if (var3 != null) {
            var3.setVisibility(8);
         }

         var4 = this.newsProgressCenter;
         if (var4 != null) {
            var4.setVisibility(8);
         }
      }

   }

   public final void setNewsContent(List var1) {
      this.onStatus(var1);
      NewsAdapter var2 = this.newsAdapter;
      if (var2 != null) {
         var2.submitList(var1);
      }

      Integer var5 = HomeFragmentViewState.INSTANCE.getLastScrollPos();
      if (var5 != null) {
         int var3 = ((Number)var5).intValue();
         Integer var4;
         if (var1 != null) {
            var4 = var1.size();
         } else {
            var4 = null;
         }

         if (var4 != null && var4 > var3) {
            LinearLayoutManager var6 = this.newsListLayoutManager;
            if (var6 != null) {
               var6.scrollToPosition(var3);
            }

            HomeFragmentViewState.INSTANCE.setLastScrollPos((Integer)null);
         }
      }

   }

   public final void setNewsListListener(ContentPortalView.NewsListListener var1) {
      this.newsListListener = var1;
   }

   public final void show(boolean var1) {
      if (this.getVisibility() != 0) {
         HomeFragmentViewState.lastOpenNews();
         ContentPortalView.NewsListListener var2 = this.newsListListener;
         if (var2 != null) {
            Context var3 = this.getContext();
            Intrinsics.checkExpressionValueIsNotNull(var3, "context");
            var2.onShow(var3);
         }

         if (!var1) {
            this.showInternal();
         } else {
            Animation var4 = AnimationUtils.loadAnimation(this.getContext(), 2130771989);
            if (var4 != null) {
               var4.setAnimationListener((AnimationListener)(new AnimationListener() {
                  public void onAnimationEnd(Animation var1) {
                     ContentPortalView.this.showInternal();
                  }

                  public void onAnimationRepeat(Animation var1) {
                  }

                  public void onAnimationStart(Animation var1) {
                  }
               }));
               this.startAnimation(var4);
            }

         }
      }
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }
   }

   public interface NewsListListener {
      void loadMore();

      void onShow(Context var1);
   }
}
