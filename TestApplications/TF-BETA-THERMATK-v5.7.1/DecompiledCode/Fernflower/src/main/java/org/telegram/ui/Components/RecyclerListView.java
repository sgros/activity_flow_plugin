package org.telegram.ui.Components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Path.Direction;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import android.util.SparseIntArray;
import android.util.StateSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.MeasureSpec;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;

public class RecyclerListView extends RecyclerView {
   private static int[] attributes;
   private static boolean gotAttributes;
   private Runnable clickRunnable;
   private int currentChildPosition;
   private View currentChildView;
   private int currentFirst = -1;
   private int currentVisible = -1;
   private boolean disableHighlightState;
   private boolean disallowInterceptTouchEvents;
   private View emptyView;
   private RecyclerListView.FastScroll fastScroll;
   private GestureDetector gestureDetector;
   private ArrayList headers;
   private ArrayList headersCache;
   private boolean hiddenByEmptyView;
   private boolean ignoreOnScroll;
   private boolean instantClick;
   private boolean interceptedByChild;
   private boolean isChildViewEnabled;
   private long lastAlphaAnimationTime;
   private boolean longPressCalled;
   private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
      public void onChanged() {
         RecyclerListView.this.checkIfEmpty();
         RecyclerListView.this.currentFirst = -1;
         if (RecyclerListView.this.removeHighlighSelectionRunnable == null) {
            RecyclerListView.this.selectorRect.setEmpty();
         }

         RecyclerListView.this.invalidate();
      }

      public void onItemRangeInserted(int var1, int var2) {
         RecyclerListView.this.checkIfEmpty();
      }

      public void onItemRangeRemoved(int var1, int var2) {
         RecyclerListView.this.checkIfEmpty();
      }
   };
   private RecyclerListView.OnInterceptTouchListener onInterceptTouchListener;
   private RecyclerListView.OnItemClickListener onItemClickListener;
   private RecyclerListView.OnItemClickListenerExtended onItemClickListenerExtended;
   private RecyclerListView.OnItemLongClickListener onItemLongClickListener;
   private RecyclerListView.OnItemLongClickListenerExtended onItemLongClickListenerExtended;
   private RecyclerView.OnScrollListener onScrollListener;
   private RecyclerListView.IntReturnCallback pendingHighlightPosition;
   private View pinnedHeader;
   private float pinnedHeaderShadowAlpha;
   private Drawable pinnedHeaderShadowDrawable;
   private float pinnedHeaderShadowTargetAlpha;
   private Runnable removeHighlighSelectionRunnable;
   private boolean scrollEnabled = true;
   private boolean scrollingByUser;
   private int sectionOffset;
   private RecyclerListView.SectionsAdapter sectionsAdapter;
   private int sectionsCount;
   private int sectionsType;
   private Runnable selectChildRunnable;
   private Drawable selectorDrawable;
   private int selectorPosition;
   private android.graphics.Rect selectorRect = new android.graphics.Rect();
   private boolean selfOnLayout;
   private int startSection;
   private boolean wasPressed;

   @SuppressLint({"PrivateApi"})
   public RecyclerListView(Context var1) {
      super(var1);
      this.setGlowColor(Theme.getColor("actionBarDefault"));
      this.selectorDrawable = Theme.getSelectorDrawable(false);
      this.selectorDrawable.setCallback(this);

      try {
         if (!gotAttributes) {
            attributes = this.getResourceDeclareStyleableIntArray("com.android.internal", "View");
            gotAttributes = true;
         }

         TypedArray var2 = var1.getTheme().obtainStyledAttributes(attributes);
         View.class.getDeclaredMethod("initializeScrollbars", TypedArray.class).invoke(this, var2);
         var2.recycle();
      } catch (Throwable var3) {
         FileLog.e(var3);
      }

      super.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrollStateChanged(RecyclerView var1, int var2) {
            boolean var3 = false;
            if (var2 != 0 && RecyclerListView.this.currentChildView != null) {
               if (RecyclerListView.this.selectChildRunnable != null) {
                  AndroidUtilities.cancelRunOnUIThread(RecyclerListView.this.selectChildRunnable);
                  RecyclerListView.this.selectChildRunnable = null;
               }

               MotionEvent var4 = MotionEvent.obtain(0L, 0L, 3, 0.0F, 0.0F, 0);

               try {
                  RecyclerListView.this.gestureDetector.onTouchEvent(var4);
               } catch (Exception var6) {
                  FileLog.e((Throwable)var6);
               }

               RecyclerListView.this.currentChildView.onTouchEvent(var4);
               var4.recycle();
               View var8 = RecyclerListView.this.currentChildView;
               RecyclerListView var5 = RecyclerListView.this;
               var5.onChildPressed(var5.currentChildView, false);
               RecyclerListView.this.currentChildView = null;
               RecyclerListView.this.removeSelection(var8, (MotionEvent)null);
               RecyclerListView.this.interceptedByChild = false;
            }

            if (RecyclerListView.this.onScrollListener != null) {
               RecyclerListView.this.onScrollListener.onScrollStateChanged(var1, var2);
            }

            RecyclerListView var7 = RecyclerListView.this;
            if (var2 == 1 || var2 == 2) {
               var3 = true;
            }

            var7.scrollingByUser = var3;
         }

         public void onScrolled(RecyclerView var1, int var2, int var3) {
            if (RecyclerListView.this.onScrollListener != null) {
               RecyclerListView.this.onScrollListener.onScrolled(var1, var2, var3);
            }

            if (RecyclerListView.this.selectorPosition != -1) {
               RecyclerListView.this.selectorRect.offset(-var2, -var3);
               RecyclerListView.this.selectorDrawable.setBounds(RecyclerListView.this.selectorRect);
               RecyclerListView.this.invalidate();
            } else {
               RecyclerListView.this.selectorRect.setEmpty();
            }

            RecyclerListView.this.checkSection();
         }
      });
      this.addOnItemTouchListener(new RecyclerListView.RecyclerListViewItemClickListener(var1));
   }

   private void checkIfEmpty() {
      RecyclerView.Adapter var1 = this.getAdapter();
      byte var2 = 0;
      if (var1 != null && this.emptyView != null) {
         boolean var3;
         if (this.getAdapter().getItemCount() == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         View var5 = this.emptyView;
         byte var4;
         if (var3) {
            var4 = 0;
         } else {
            var4 = 8;
         }

         var5.setVisibility(var4);
         var4 = var2;
         if (var3) {
            var4 = 4;
         }

         this.setVisibility(var4);
         this.hiddenByEmptyView = true;
      } else {
         if (this.hiddenByEmptyView && this.getVisibility() != 0) {
            this.setVisibility(0);
            this.hiddenByEmptyView = false;
         }

      }
   }

   private void ensurePinnedHeaderLayout(View var1, boolean var2) {
      if (var1.isLayoutRequested() || var2) {
         int var3 = this.sectionsType;
         int var5;
         if (var3 == 1) {
            android.view.ViewGroup.LayoutParams var4 = var1.getLayoutParams();
            var3 = MeasureSpec.makeMeasureSpec(var4.height, 1073741824);
            var5 = MeasureSpec.makeMeasureSpec(var4.width, 1073741824);

            try {
               var1.measure(var5, var3);
            } catch (Exception var7) {
               FileLog.e((Throwable)var7);
            }
         } else if (var3 == 2) {
            var5 = MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824);
            var3 = MeasureSpec.makeMeasureSpec(0, 0);

            try {
               var1.measure(var5, var3);
            } catch (Exception var6) {
               FileLog.e((Throwable)var6);
            }
         }

         var1.layout(0, 0, var1.getMeasuredWidth(), var1.getMeasuredHeight());
      }

   }

   private int[] getDrawableStateForSelector() {
      int[] var1 = this.onCreateDrawableState(1);
      var1[var1.length - 1] = 16842919;
      return var1;
   }

   private View getSectionHeaderView(int var1, View var2) {
      boolean var3;
      if (var2 == null) {
         var3 = true;
      } else {
         var3 = false;
      }

      var2 = this.sectionsAdapter.getSectionHeaderView(var1, var2);
      if (var3) {
         this.ensurePinnedHeaderLayout(var2, false);
      }

      return var2;
   }

   private void highlightRowInternal(RecyclerListView.IntReturnCallback var1, boolean var2) {
      Runnable var3 = this.removeHighlighSelectionRunnable;
      if (var3 != null) {
         AndroidUtilities.cancelRunOnUIThread(var3);
         this.removeHighlighSelectionRunnable = null;
      }

      RecyclerView.ViewHolder var6 = this.findViewHolderForAdapterPosition(var1.run());
      if (var6 != null) {
         this.positionSelector(var6.getLayoutPosition(), var6.itemView);
         Drawable var4 = this.selectorDrawable;
         if (var4 != null) {
            var4 = var4.getCurrent();
            if (var4 instanceof TransitionDrawable) {
               if (this.onItemLongClickListener == null && this.onItemClickListenerExtended == null) {
                  ((TransitionDrawable)var4).resetTransition();
               } else {
                  ((TransitionDrawable)var4).startTransition(ViewConfiguration.getLongPressTimeout());
               }
            }

            if (VERSION.SDK_INT >= 21) {
               this.selectorDrawable.setHotspot((float)(var6.itemView.getMeasuredWidth() / 2), (float)(var6.itemView.getMeasuredHeight() / 2));
            }
         }

         var4 = this.selectorDrawable;
         if (var4 != null && var4.isStateful() && this.selectorDrawable.setState(this.getDrawableStateForSelector())) {
            this.invalidateDrawable(this.selectorDrawable);
         }

         _$$Lambda$RecyclerListView$9OyE8_R_oHAnqiquiqoGBlUXLQE var5 = new _$$Lambda$RecyclerListView$9OyE8_R_oHAnqiquiqoGBlUXLQE(this);
         this.removeHighlighSelectionRunnable = var5;
         AndroidUtilities.runOnUIThread(var5, 700L);
      } else if (var2) {
         this.pendingHighlightPosition = var1;
      }

   }

   private void positionSelector(int var1, View var2) {
      this.positionSelector(var1, var2, false, -1.0F, -1.0F);
   }

   private void positionSelector(int var1, View var2, boolean var3, float var4, float var5) {
      Runnable var6 = this.removeHighlighSelectionRunnable;
      if (var6 != null) {
         AndroidUtilities.cancelRunOnUIThread(var6);
         this.removeHighlighSelectionRunnable = null;
         this.pendingHighlightPosition = null;
      }

      if (this.selectorDrawable != null) {
         boolean var7;
         if (var1 != this.selectorPosition) {
            var7 = true;
         } else {
            var7 = false;
         }

         int var8;
         if (this.getAdapter() instanceof RecyclerListView.SelectionAdapter) {
            var8 = ((RecyclerListView.SelectionAdapter)this.getAdapter()).getSelectionBottomPadding(var2);
         } else {
            var8 = 0;
         }

         if (var1 != -1) {
            this.selectorPosition = var1;
         }

         this.selectorRect.set(var2.getLeft(), var2.getTop(), var2.getRight(), var2.getBottom() - var8);
         boolean var9 = var2.isEnabled();
         if (this.isChildViewEnabled != var9) {
            this.isChildViewEnabled = var9;
         }

         if (var7) {
            this.selectorDrawable.setVisible(false, false);
            this.selectorDrawable.setState(StateSet.NOTHING);
         }

         this.selectorDrawable.setBounds(this.selectorRect);
         if (var7 && this.getVisibility() == 0) {
            this.selectorDrawable.setVisible(true, false);
         }

         if (VERSION.SDK_INT >= 21 && var3) {
            this.selectorDrawable.setHotspot(var4, var5);
         }

      }
   }

   private void removeSelection(View var1, MotionEvent var2) {
      if (var1 != null) {
         if (var1 != null && var1.isEnabled()) {
            this.positionSelector(this.currentChildPosition, var1);
            Drawable var3 = this.selectorDrawable;
            if (var3 != null) {
               var3 = var3.getCurrent();
               if (var3 instanceof TransitionDrawable) {
                  ((TransitionDrawable)var3).resetTransition();
               }

               if (var2 != null && VERSION.SDK_INT >= 21) {
                  this.selectorDrawable.setHotspot(var2.getX(), var2.getY());
               }
            }
         } else {
            this.selectorRect.setEmpty();
         }

         this.updateSelectorState();
      }
   }

   private void updateSelectorState() {
      Drawable var1 = this.selectorDrawable;
      if (var1 != null && var1.isStateful()) {
         if (this.currentChildView != null) {
            if (this.selectorDrawable.setState(this.getDrawableStateForSelector())) {
               this.invalidateDrawable(this.selectorDrawable);
            }
         } else if (this.removeHighlighSelectionRunnable == null) {
            this.selectorDrawable.setState(StateSet.NOTHING);
         }
      }

   }

   protected boolean allowSelectChildAtPosition(float var1, float var2) {
      return true;
   }

   public boolean canScrollVertically(int var1) {
      boolean var2;
      if (this.scrollEnabled && super.canScrollVertically(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void cancelClickRunnables(boolean var1) {
      Runnable var2 = this.selectChildRunnable;
      if (var2 != null) {
         AndroidUtilities.cancelRunOnUIThread(var2);
         this.selectChildRunnable = null;
      }

      View var3 = this.currentChildView;
      if (var3 != null) {
         if (var1) {
            this.onChildPressed(var3, false);
         }

         this.currentChildView = null;
         this.removeSelection(var3, (MotionEvent)null);
      }

      var2 = this.clickRunnable;
      if (var2 != null) {
         AndroidUtilities.cancelRunOnUIThread(var2);
         this.clickRunnable = null;
      }

      this.interceptedByChild = false;
   }

   public void checkSection() {
      if (this.scrollingByUser && this.fastScroll != null || this.sectionsType != 0 && this.sectionsAdapter != null) {
         RecyclerView.LayoutManager var1 = this.getLayoutManager();
         if (var1 instanceof LinearLayoutManager) {
            LinearLayoutManager var2 = (LinearLayoutManager)var1;
            if (var2.getOrientation() == 1) {
               RecyclerListView.SectionsAdapter var3 = this.sectionsAdapter;
               int var4;
               int var7;
               RecyclerView.Adapter var16;
               if (var3 != null) {
                  var4 = this.sectionsType;
                  View var15 = null;
                  byte var5 = 0;
                  int var6;
                  int var8;
                  int var9;
                  View var17;
                  if (var4 == 1) {
                     var6 = var2.findFirstVisibleItemPosition();
                     var7 = Math.abs(var2.findLastVisibleItemPosition() - var6) + 1;
                     if (var6 == -1) {
                        return;
                     }

                     if (this.scrollingByUser && this.fastScroll != null) {
                        var16 = this.getAdapter();
                        if (var16 instanceof RecyclerListView.FastScrollAdapter) {
                           this.fastScroll.setProgress(Math.min(1.0F, (float)var6 / (float)(var16.getItemCount() - var7 + 1)));
                        }
                     }

                     this.headersCache.addAll(this.headers);
                     this.headers.clear();
                     if (this.sectionsAdapter.getItemCount() == 0) {
                        return;
                     }

                     if (this.currentFirst != var6 || this.currentVisible != var7) {
                        this.currentFirst = var6;
                        this.currentVisible = var7;
                        this.sectionsCount = 1;
                        this.startSection = this.sectionsAdapter.getSectionForPosition(var6);

                        for(var4 = this.sectionsAdapter.getCountForSection(this.startSection) + var6 - this.sectionsAdapter.getPositionInSectionForPosition(var6); var4 < var6 + var7; ++this.sectionsCount) {
                           var4 += this.sectionsAdapter.getCountForSection(this.startSection + this.sectionsCount);
                        }
                     }

                     var4 = this.startSection;

                     for(var7 = var6; var4 < this.startSection + this.sectionsCount; ++var4) {
                        if (!this.headersCache.isEmpty()) {
                           var15 = (View)this.headersCache.get(0);
                           this.headersCache.remove(0);
                        } else {
                           var15 = null;
                        }

                        var15 = this.getSectionHeaderView(var4, var15);
                        this.headers.add(var15);
                        var8 = this.sectionsAdapter.getCountForSection(var4);
                        if (var4 == this.startSection) {
                           var9 = this.sectionsAdapter.getPositionInSectionForPosition(var7);
                           if (var9 == var8 - 1) {
                              var15.setTag(-var15.getHeight());
                           } else if (var9 == var8 - 2) {
                              var17 = this.getChildAt(var7 - var6);
                              if (var17 != null) {
                                 var9 = var17.getTop();
                              } else {
                                 var9 = -AndroidUtilities.dp(100.0F);
                              }

                              if (var9 < 0) {
                                 var15.setTag(var9);
                              } else {
                                 var15.setTag(0);
                              }
                           } else {
                              var15.setTag(0);
                           }

                           var9 = var8 - this.sectionsAdapter.getPositionInSectionForPosition(var6);
                        } else {
                           var17 = this.getChildAt(var7 - var6);
                           if (var17 != null) {
                              var15.setTag(var17.getTop());
                              var9 = var8;
                           } else {
                              var15.setTag(-AndroidUtilities.dp(100.0F));
                              var9 = var8;
                           }
                        }

                        var7 += var9;
                     }
                  } else if (var4 == 2) {
                     this.pinnedHeaderShadowTargetAlpha = 0.0F;
                     if (var3.getItemCount() == 0) {
                        return;
                     }

                     int var10 = this.getChildCount();
                     var17 = null;
                     var8 = 0;
                     var7 = Integer.MAX_VALUE;
                     var9 = 0;

                     int var13;
                     for(var6 = Integer.MAX_VALUE; var8 < var10; var6 = var13) {
                        View var18 = this.getChildAt(var8);
                        int var11 = var18.getBottom();
                        View var12;
                        if (var11 <= this.sectionOffset + this.getPaddingTop()) {
                           var12 = var15;
                           var13 = var6;
                        } else {
                           var4 = var7;
                           if (var11 < var7) {
                              var15 = var18;
                              var4 = var11;
                           }

                           int var14 = Math.max(var9, var11);
                           if (var11 < this.sectionOffset + this.getPaddingTop() + AndroidUtilities.dp(32.0F)) {
                              var7 = var4;
                              var9 = var14;
                              var12 = var15;
                              var13 = var6;
                           } else {
                              var7 = var4;
                              var9 = var14;
                              var12 = var15;
                              var13 = var6;
                              if (var11 < var6) {
                                 var17 = var18;
                                 var13 = var11;
                                 var12 = var15;
                                 var9 = var14;
                                 var7 = var4;
                              }
                           }
                        }

                        ++var8;
                        var15 = var12;
                     }

                     if (var15 == null) {
                        return;
                     }

                     RecyclerView.ViewHolder var19 = this.getChildViewHolder(var15);
                     if (var19 == null) {
                        return;
                     }

                     var7 = var19.getAdapterPosition();
                     var4 = this.sectionsAdapter.getSectionForPosition(var7);
                     if (var4 < 0) {
                        return;
                     }

                     if (this.currentFirst != var4 || this.pinnedHeader == null) {
                        this.pinnedHeader = this.getSectionHeaderView(var4, this.pinnedHeader);
                        this.currentFirst = var4;
                     }

                     if (this.pinnedHeader != null && var17 != null && var17.getClass() != this.pinnedHeader.getClass()) {
                        this.pinnedHeaderShadowTargetAlpha = 1.0F;
                     }

                     var4 = this.sectionsAdapter.getCountForSection(var4);
                     var8 = this.sectionsAdapter.getPositionInSectionForPosition(var7);
                     var6 = this.getPaddingTop();
                     if (var9 != 0 && var9 < this.getMeasuredHeight() - this.getPaddingBottom()) {
                        var7 = var5;
                     } else {
                        var7 = this.sectionOffset;
                     }

                     if (var8 == var4 - 1) {
                        var4 = this.pinnedHeader.getHeight();
                        if (var15 != null) {
                           var9 = var15.getTop() - var6 - this.sectionOffset + var15.getHeight();
                           if (var9 < var4) {
                              var4 = var9 - var4;
                           } else {
                              var4 = var6;
                           }
                        } else {
                           var4 = -AndroidUtilities.dp(100.0F);
                        }

                        if (var4 < 0) {
                           this.pinnedHeader.setTag(var6 + var7 + var4);
                        } else {
                           this.pinnedHeader.setTag(var6 + var7);
                        }
                     } else {
                        this.pinnedHeader.setTag(var6 + var7);
                     }

                     this.invalidate();
                  }
               } else {
                  var4 = var2.findFirstVisibleItemPosition();
                  var7 = Math.abs(var2.findLastVisibleItemPosition() - var4);
                  if (var4 == -1) {
                     return;
                  }

                  if (this.scrollingByUser && this.fastScroll != null) {
                     var16 = this.getAdapter();
                     if (var16 instanceof RecyclerListView.FastScrollAdapter) {
                        this.fastScroll.setProgress(Math.min(1.0F, (float)var4 / (float)(var16.getItemCount() - (var7 + 1) + 1)));
                     }
                  }
               }
            }
         }
      }

   }

   protected void dispatchDraw(Canvas var1) {
      super.dispatchDraw(var1);
      int var2 = this.sectionsType;
      float var3 = 0.0F;
      int var5;
      if (var2 == 1) {
         if (this.sectionsAdapter == null || this.headers.isEmpty()) {
            return;
         }

         for(var2 = 0; var2 < this.headers.size(); ++var2) {
            View var12 = (View)this.headers.get(var2);
            var5 = var1.save();
            int var6 = (Integer)var12.getTag();
            if (LocaleController.isRTL) {
               var3 = (float)(this.getWidth() - var12.getWidth());
            } else {
               var3 = 0.0F;
            }

            var1.translate(var3, (float)var6);
            var1.clipRect(0, 0, this.getWidth(), var12.getMeasuredHeight());
            var12.draw(var1);
            var1.restoreToCount(var5);
         }
      } else if (var2 == 2) {
         if (this.sectionsAdapter == null || this.pinnedHeader == null) {
            return;
         }

         var2 = var1.save();
         var5 = (Integer)this.pinnedHeader.getTag();
         if (LocaleController.isRTL) {
            var3 = (float)(this.getWidth() - this.pinnedHeader.getWidth());
         }

         var1.translate(var3, (float)var5);
         Drawable var4 = this.pinnedHeaderShadowDrawable;
         if (var4 != null) {
            var4.setBounds(0, this.pinnedHeader.getMeasuredHeight(), this.getWidth(), this.pinnedHeader.getMeasuredHeight() + this.pinnedHeaderShadowDrawable.getIntrinsicHeight());
            this.pinnedHeaderShadowDrawable.setAlpha((int)(this.pinnedHeaderShadowAlpha * 255.0F));
            this.pinnedHeaderShadowDrawable.draw(var1);
            long var7 = SystemClock.uptimeMillis();
            long var9 = Math.min(20L, var7 - this.lastAlphaAnimationTime);
            this.lastAlphaAnimationTime = var7;
            var3 = this.pinnedHeaderShadowAlpha;
            float var11 = this.pinnedHeaderShadowTargetAlpha;
            if (var3 < var11) {
               this.pinnedHeaderShadowAlpha = var3 + (float)var9 / 180.0F;
               if (this.pinnedHeaderShadowAlpha > var11) {
                  this.pinnedHeaderShadowAlpha = var11;
               }

               this.invalidate();
            } else if (var3 > var11) {
               this.pinnedHeaderShadowAlpha = var3 - (float)var9 / 180.0F;
               if (this.pinnedHeaderShadowAlpha < var11) {
                  this.pinnedHeaderShadowAlpha = var11;
               }

               this.invalidate();
            }
         }

         var1.clipRect(0, 0, this.getWidth(), this.pinnedHeader.getMeasuredHeight());
         this.pinnedHeader.draw(var1);
         var1.restoreToCount(var2);
      }

      if (!this.selectorRect.isEmpty()) {
         this.selectorDrawable.setBounds(this.selectorRect);
         this.selectorDrawable.draw(var1);
      }

   }

   public boolean dispatchNestedPreScroll(int var1, int var2, int[] var3, int[] var4, int var5) {
      if (this.longPressCalled) {
         RecyclerListView.OnItemLongClickListenerExtended var6 = this.onItemLongClickListenerExtended;
         if (var6 != null) {
            var6.onMove((float)var1, (float)var2);
         }

         var3[0] = var1;
         var3[1] = var2;
         return true;
      } else {
         return super.dispatchNestedPreScroll(var1, var2, var3, var4, var5);
      }
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      this.updateSelectorState();
   }

   public View findChildViewUnder(float var1, float var2) {
      int var3 = this.getChildCount();

      for(int var4 = 0; var4 < 2; ++var4) {
         for(int var5 = var3 - 1; var5 >= 0; --var5) {
            View var6 = this.getChildAt(var5);
            float var7 = 0.0F;
            float var8;
            if (var4 == 0) {
               var8 = var6.getTranslationX();
            } else {
               var8 = 0.0F;
            }

            if (var4 == 0) {
               var7 = var6.getTranslationY();
            }

            if (var1 >= (float)var6.getLeft() + var8 && var1 <= (float)var6.getRight() + var8 && var2 >= (float)var6.getTop() + var7 && var2 <= (float)var6.getBottom() + var7) {
               return var6;
            }
         }
      }

      return null;
   }

   public View getEmptyView() {
      return this.emptyView;
   }

   public ArrayList getHeaders() {
      return this.headers;
   }

   public ArrayList getHeadersCache() {
      return this.headersCache;
   }

   public RecyclerListView.OnItemClickListener getOnItemClickListener() {
      return this.onItemClickListener;
   }

   public View getPinnedHeader() {
      return this.pinnedHeader;
   }

   protected View getPressedChildView() {
      return this.currentChildView;
   }

   public int[] getResourceDeclareStyleableIntArray(String param1, String param2) {
      // $FF: Couldn't be decompiled
   }

   public boolean hasOverlappingRendering() {
      return false;
   }

   public void hideSelector() {
      View var1 = this.currentChildView;
      if (var1 != null) {
         this.onChildPressed(var1, false);
         this.currentChildView = null;
         this.removeSelection(var1, (MotionEvent)null);
      }

   }

   public void highlightRow(RecyclerListView.IntReturnCallback var1) {
      this.highlightRowInternal(var1, true);
   }

   public void invalidateViews() {
      int var1 = this.getChildCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         this.getChildAt(var2).invalidate();
      }

   }

   public void jumpDrawablesToCurrentState() {
      super.jumpDrawablesToCurrentState();
      Drawable var1 = this.selectorDrawable;
      if (var1 != null) {
         var1.jumpToCurrentState();
      }

   }

   // $FF: synthetic method
   public void lambda$highlightRowInternal$0$RecyclerListView() {
      this.removeHighlighSelectionRunnable = null;
      this.pendingHighlightPosition = null;
      Drawable var1 = this.selectorDrawable;
      if (var1 != null) {
         var1 = var1.getCurrent();
         if (var1 instanceof TransitionDrawable) {
            ((TransitionDrawable)var1).resetTransition();
         }
      }

      var1 = this.selectorDrawable;
      if (var1 != null && var1.isStateful()) {
         this.selectorDrawable.setState(StateSet.NOTHING);
      }

   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      RecyclerListView.FastScroll var1 = this.fastScroll;
      if (var1 != null && var1.getParent() != this.getParent()) {
         ViewGroup var2 = (ViewGroup)this.fastScroll.getParent();
         if (var2 != null) {
            var2.removeView(this.fastScroll);
         }

         ((ViewGroup)this.getParent()).addView(this.fastScroll);
      }

   }

   public void onChildAttachedToWindow(View var1) {
      if (this.getAdapter() instanceof RecyclerListView.SelectionAdapter) {
         RecyclerView.ViewHolder var2 = this.findContainingViewHolder(var1);
         if (var2 != null) {
            var1.setEnabled(((RecyclerListView.SelectionAdapter)this.getAdapter()).isEnabled(var2));
         }
      } else {
         var1.setEnabled(false);
      }

      super.onChildAttachedToWindow(var1);
   }

   protected void onChildPressed(View var1, boolean var2) {
      if (!this.disableHighlightState) {
         var1.setPressed(var2);
      }
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.selectorPosition = -1;
      this.selectorRect.setEmpty();
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      boolean var2 = this.isEnabled();
      boolean var3 = false;
      if (!var2) {
         return false;
      } else {
         if (this.disallowInterceptTouchEvents) {
            this.requestDisallowInterceptTouchEvent(true);
         }

         RecyclerListView.OnInterceptTouchListener var4 = this.onInterceptTouchListener;
         if (var4 != null && var4.onInterceptTouchEvent(var1) || super.onInterceptTouchEvent(var1)) {
            var3 = true;
         }

         return var3;
      }
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      RecyclerListView.FastScroll var6 = this.fastScroll;
      if (var6 != null) {
         this.selfOnLayout = true;
         if (LocaleController.isRTL) {
            var6.layout(0, var3, var6.getMeasuredWidth(), this.fastScroll.getMeasuredHeight() + var3);
         } else {
            var2 = this.getMeasuredWidth() - this.fastScroll.getMeasuredWidth();
            var6 = this.fastScroll;
            var6.layout(var2, var3, var6.getMeasuredWidth() + var2, this.fastScroll.getMeasuredHeight() + var3);
         }

         this.selfOnLayout = false;
      }

      this.checkSection();
      RecyclerListView.IntReturnCallback var7 = this.pendingHighlightPosition;
      if (var7 != null) {
         this.highlightRowInternal(var7, false);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      RecyclerListView.FastScroll var3 = this.fastScroll;
      if (var3 != null) {
         var3.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(132.0F), 1073741824), MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
      }

   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      var1 = this.sectionsType;
      if (var1 == 1) {
         if (this.sectionsAdapter == null || this.headers.isEmpty()) {
            return;
         }

         for(var1 = 0; var1 < this.headers.size(); ++var1) {
            this.ensurePinnedHeaderLayout((View)this.headers.get(var1), true);
         }
      } else if (var1 == 2 && this.sectionsAdapter != null) {
         View var5 = this.pinnedHeader;
         if (var5 != null) {
            this.ensurePinnedHeaderLayout(var5, true);
         }
      }

   }

   public void setAdapter(RecyclerView.Adapter var1) {
      RecyclerView.Adapter var2 = this.getAdapter();
      if (var2 != null) {
         var2.unregisterAdapterDataObserver(this.observer);
      }

      ArrayList var3 = this.headers;
      if (var3 != null) {
         var3.clear();
         this.headersCache.clear();
      }

      this.currentFirst = -1;
      this.selectorPosition = -1;
      this.selectorRect.setEmpty();
      this.pinnedHeader = null;
      if (var1 instanceof RecyclerListView.SectionsAdapter) {
         this.sectionsAdapter = (RecyclerListView.SectionsAdapter)var1;
      } else {
         this.sectionsAdapter = null;
      }

      super.setAdapter(var1);
      if (var1 != null) {
         var1.registerAdapterDataObserver(this.observer);
      }

      this.checkIfEmpty();
   }

   public void setDisableHighlightState(boolean var1) {
      this.disableHighlightState = var1;
   }

   public void setDisallowInterceptTouchEvents(boolean var1) {
      this.disallowInterceptTouchEvents = var1;
   }

   public void setEmptyView(View var1) {
      if (this.emptyView != var1) {
         this.emptyView = var1;
         this.checkIfEmpty();
      }
   }

   public void setFastScrollEnabled() {
      this.fastScroll = new RecyclerListView.FastScroll(this.getContext());
      if (this.getParent() != null) {
         ((ViewGroup)this.getParent()).addView(this.fastScroll);
      }

   }

   public void setFastScrollVisible(boolean var1) {
      RecyclerListView.FastScroll var2 = this.fastScroll;
      if (var2 != null) {
         byte var3;
         if (var1) {
            var3 = 0;
         } else {
            var3 = 8;
         }

         var2.setVisibility(var3);
      }
   }

   public void setInstantClick(boolean var1) {
      this.instantClick = var1;
   }

   public void setListSelectorColor(int var1) {
      Theme.setSelectorDrawableColor(this.selectorDrawable, var1, true);
   }

   public void setOnInterceptTouchListener(RecyclerListView.OnInterceptTouchListener var1) {
      this.onInterceptTouchListener = var1;
   }

   public void setOnItemClickListener(RecyclerListView.OnItemClickListener var1) {
      this.onItemClickListener = var1;
   }

   public void setOnItemClickListener(RecyclerListView.OnItemClickListenerExtended var1) {
      this.onItemClickListenerExtended = var1;
   }

   public void setOnItemLongClickListener(RecyclerListView.OnItemLongClickListener var1) {
      this.onItemLongClickListener = var1;
      GestureDetector var2 = this.gestureDetector;
      boolean var3;
      if (var1 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      var2.setIsLongpressEnabled(var3);
   }

   public void setOnItemLongClickListener(RecyclerListView.OnItemLongClickListenerExtended var1) {
      this.onItemLongClickListenerExtended = var1;
      GestureDetector var2 = this.gestureDetector;
      boolean var3;
      if (var1 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      var2.setIsLongpressEnabled(var3);
   }

   public void setOnScrollListener(RecyclerView.OnScrollListener var1) {
      this.onScrollListener = var1;
   }

   public void setPinnedHeaderShadowDrawable(Drawable var1) {
      this.pinnedHeaderShadowDrawable = var1;
   }

   public void setPinnedSectionOffsetY(int var1) {
      this.sectionOffset = var1;
      this.invalidate();
   }

   public void setScrollEnabled(boolean var1) {
      this.scrollEnabled = var1;
   }

   public void setSectionsType(int var1) {
      this.sectionsType = var1;
      if (this.sectionsType == 1) {
         this.headers = new ArrayList();
         this.headersCache = new ArrayList();
      }

   }

   public void setSelectorDrawableColor(int var1) {
      Drawable var2 = this.selectorDrawable;
      if (var2 != null) {
         var2.setCallback((Callback)null);
      }

      this.selectorDrawable = Theme.getSelectorDrawable(var1, false);
      this.selectorDrawable.setCallback(this);
   }

   public void setVerticalScrollBarEnabled(boolean var1) {
      if (attributes != null) {
         super.setVerticalScrollBarEnabled(var1);
      }

   }

   public void setVisibility(int var1) {
      super.setVisibility(var1);
      if (var1 != 0) {
         this.hiddenByEmptyView = false;
      }

   }

   public void stopScroll() {
      try {
         super.stopScroll();
      } catch (NullPointerException var2) {
      }

   }

   public void updateFastScrollColors() {
      RecyclerListView.FastScroll var1 = this.fastScroll;
      if (var1 != null) {
         var1.updateColors();
      }

   }

   public boolean verifyDrawable(Drawable var1) {
      boolean var2;
      if (this.selectorDrawable != var1 && !super.verifyDrawable(var1)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   private class FastScroll extends View {
      private float bubbleProgress;
      private int[] colors = new int[6];
      private String currentLetter;
      private long lastUpdateTime;
      private float lastY;
      private StaticLayout letterLayout;
      private TextPaint letterPaint = new TextPaint(1);
      private StaticLayout oldLetterLayout;
      private Paint paint = new Paint(1);
      private Path path = new Path();
      private boolean pressed;
      private float progress;
      private float[] radii = new float[8];
      private RectF rect = new RectF();
      private int scrollX;
      private float startDy;
      private float textX;
      private float textY;

      public FastScroll(Context var2) {
         super(var2);
         this.letterPaint.setTextSize((float)AndroidUtilities.dp(45.0F));

         for(int var3 = 0; var3 < 8; ++var3) {
            this.radii[var3] = (float)AndroidUtilities.dp(44.0F);
         }

         float var4;
         if (LocaleController.isRTL) {
            var4 = 10.0F;
         } else {
            var4 = 117.0F;
         }

         this.scrollX = AndroidUtilities.dp(var4);
         this.updateColors();
      }

      private void getCurrentLetter() {
         RecyclerView.LayoutManager var1 = RecyclerListView.this.getLayoutManager();
         if (var1 instanceof LinearLayoutManager) {
            LinearLayoutManager var4 = (LinearLayoutManager)var1;
            if (var4.getOrientation() == 1) {
               RecyclerView.Adapter var2 = RecyclerListView.this.getAdapter();
               if (var2 instanceof RecyclerListView.FastScrollAdapter) {
                  RecyclerListView.FastScrollAdapter var7 = (RecyclerListView.FastScrollAdapter)var2;
                  int var3 = var7.getPositionForScrollProgress(this.progress);
                  var4.scrollToPositionWithOffset(var3, 0);
                  String var5 = var7.getLetter(var3);
                  if (var5 == null) {
                     StaticLayout var6 = this.letterLayout;
                     if (var6 != null) {
                        this.oldLetterLayout = var6;
                     }

                     this.letterLayout = null;
                  } else if (!var5.equals(this.currentLetter)) {
                     this.letterLayout = new StaticLayout(var5, this.letterPaint, 1000, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                     this.oldLetterLayout = null;
                     if (this.letterLayout.getLineCount() > 0) {
                        this.letterLayout.getLineWidth(0);
                        this.letterLayout.getLineLeft(0);
                        if (LocaleController.isRTL) {
                           this.textX = (float)AndroidUtilities.dp(10.0F) + ((float)AndroidUtilities.dp(88.0F) - this.letterLayout.getLineWidth(0)) / 2.0F - this.letterLayout.getLineLeft(0);
                        } else {
                           this.textX = ((float)AndroidUtilities.dp(88.0F) - this.letterLayout.getLineWidth(0)) / 2.0F - this.letterLayout.getLineLeft(0);
                        }

                        this.textY = (float)((AndroidUtilities.dp(88.0F) - this.letterLayout.getHeight()) / 2);
                     }
                  }
               }
            }
         }

      }

      private void setProgress(float var1) {
         this.progress = var1;
         this.invalidate();
      }

      private void updateColors() {
         int var1 = Theme.getColor("fastScrollInactive");
         int var2 = Theme.getColor("fastScrollActive");
         this.paint.setColor(var1);
         this.letterPaint.setColor(Theme.getColor("fastScrollText"));
         this.colors[0] = Color.red(var1);
         this.colors[1] = Color.red(var2);
         this.colors[2] = Color.green(var1);
         this.colors[3] = Color.green(var2);
         this.colors[4] = Color.blue(var1);
         this.colors[5] = Color.blue(var2);
         this.invalidate();
      }

      public void layout(int var1, int var2, int var3, int var4) {
         if (RecyclerListView.this.selfOnLayout) {
            super.layout(var1, var2, var3, var4);
         }
      }

      protected void onDraw(Canvas var1) {
         Paint var2 = this.paint;
         int[] var3 = this.colors;
         int var4 = var3[0];
         float var5 = (float)(var3[1] - var3[0]);
         float var6 = this.bubbleProgress;
         var2.setColor(Color.argb(255, var4 + (int)(var5 * var6), var3[2] + (int)((float)(var3[3] - var3[2]) * var6), var3[4] + (int)((float)(var3[5] - var3[4]) * var6)));
         int var7 = (int)Math.ceil((double)((float)(this.getMeasuredHeight() - AndroidUtilities.dp(54.0F)) * this.progress));
         this.rect.set((float)this.scrollX, (float)(AndroidUtilities.dp(12.0F) + var7), (float)(this.scrollX + AndroidUtilities.dp(5.0F)), (float)(AndroidUtilities.dp(42.0F) + var7));
         var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(2.0F), this.paint);
         if (this.pressed || this.bubbleProgress != 0.0F) {
            this.paint.setAlpha((int)(this.bubbleProgress * 255.0F));
            int var8 = AndroidUtilities.dp(30.0F);
            var4 = var7 - AndroidUtilities.dp(46.0F);
            if (var4 <= AndroidUtilities.dp(12.0F)) {
               var5 = (float)(AndroidUtilities.dp(12.0F) - var4);
               var4 = AndroidUtilities.dp(12.0F);
            } else {
               var5 = 0.0F;
            }

            var1.translate((float)AndroidUtilities.dp(10.0F), (float)var4);
            float var9;
            if (var5 <= (float)AndroidUtilities.dp(29.0F)) {
               var9 = (float)AndroidUtilities.dp(44.0F);
               var6 = (float)AndroidUtilities.dp(4.0F) + var5 / (float)AndroidUtilities.dp(29.0F) * (float)AndroidUtilities.dp(40.0F);
               var5 = var9;
            } else {
               var9 = (float)AndroidUtilities.dp(29.0F);
               var6 = (float)AndroidUtilities.dp(44.0F);
               var5 = (float)AndroidUtilities.dp(4.0F) + (1.0F - (var5 - var9) / (float)AndroidUtilities.dp(29.0F)) * (float)AndroidUtilities.dp(40.0F);
            }

            label105: {
               float[] var16;
               label106: {
                  if (LocaleController.isRTL) {
                     var16 = this.radii;
                     if (var16[0] != var5 || var16[6] != var6) {
                        break label106;
                     }
                  }

                  if (LocaleController.isRTL) {
                     break label105;
                  }

                  var16 = this.radii;
                  if (var16[2] == var5 && var16[4] == var6) {
                     break label105;
                  }
               }

               if (LocaleController.isRTL) {
                  var16 = this.radii;
                  var16[1] = var5;
                  var16[0] = var5;
                  var16[7] = var6;
                  var16[6] = var6;
               } else {
                  var16 = this.radii;
                  var16[3] = var5;
                  var16[2] = var5;
                  var16[5] = var6;
                  var16[4] = var6;
               }

               this.path.reset();
               RectF var17 = this.rect;
               if (LocaleController.isRTL) {
                  var5 = (float)AndroidUtilities.dp(10.0F);
               } else {
                  var5 = 0.0F;
               }

               if (LocaleController.isRTL) {
                  var6 = 98.0F;
               } else {
                  var6 = 88.0F;
               }

               var17.set(var5, 0.0F, (float)AndroidUtilities.dp(var6), (float)AndroidUtilities.dp(88.0F));
               this.path.addRoundRect(this.rect, this.radii, Direction.CW);
               this.path.close();
            }

            StaticLayout var18 = this.letterLayout;
            if (var18 == null) {
               var18 = this.oldLetterLayout;
            }

            if (var18 != null) {
               var1.save();
               var5 = this.bubbleProgress;
               var1.scale(var5, var5, (float)this.scrollX, (float)(var8 + var7 - var4));
               var1.drawPath(this.path, this.paint);
               var1.translate(this.textX, this.textY);
               var18.draw(var1);
               var1.restore();
            }
         }

         if (this.pressed && this.letterLayout != null && this.bubbleProgress < 1.0F || (!this.pressed || this.letterLayout == null) && this.bubbleProgress > 0.0F) {
            long var10;
            long var14;
            label70: {
               var10 = System.currentTimeMillis();
               long var12 = var10 - this.lastUpdateTime;
               if (var12 >= 0L) {
                  var14 = var12;
                  if (var12 <= 17L) {
                     break label70;
                  }
               }

               var14 = 17L;
            }

            this.lastUpdateTime = var10;
            this.invalidate();
            if (this.pressed && this.letterLayout != null) {
               this.bubbleProgress += (float)var14 / 120.0F;
               if (this.bubbleProgress > 1.0F) {
                  this.bubbleProgress = 1.0F;
               }
            } else {
               this.bubbleProgress -= (float)var14 / 120.0F;
               if (this.bubbleProgress < 0.0F) {
                  this.bubbleProgress = 0.0F;
               }
            }
         }

      }

      protected void onMeasure(int var1, int var2) {
         this.setMeasuredDimension(AndroidUtilities.dp(132.0F), MeasureSpec.getSize(var2));
      }

      public boolean onTouchEvent(MotionEvent var1) {
         int var2 = var1.getAction();
         float var3;
         float var4;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 == 2) {
                  if (!this.pressed) {
                     return true;
                  }

                  var3 = var1.getY();
                  var4 = (float)AndroidUtilities.dp(12.0F) + this.startDy;
                  float var5 = (float)(this.getMeasuredHeight() - AndroidUtilities.dp(42.0F)) + this.startDy;
                  if (var3 >= var4) {
                     var4 = var3;
                     if (var3 > var5) {
                        var4 = var5;
                     }
                  }

                  var3 = this.lastY;
                  this.lastY = var4;
                  this.progress += (var4 - var3) / (float)(this.getMeasuredHeight() - AndroidUtilities.dp(54.0F));
                  var4 = this.progress;
                  if (var4 < 0.0F) {
                     this.progress = 0.0F;
                  } else if (var4 > 1.0F) {
                     this.progress = 1.0F;
                  }

                  this.getCurrentLetter();
                  this.invalidate();
                  return true;
               }

               if (var2 != 3) {
                  return super.onTouchEvent(var1);
               }
            }

            this.pressed = false;
            this.lastUpdateTime = System.currentTimeMillis();
            this.invalidate();
            return true;
         } else {
            var3 = var1.getX();
            this.lastY = var1.getY();
            var4 = (float)Math.ceil((double)((float)(this.getMeasuredHeight() - AndroidUtilities.dp(54.0F)) * this.progress)) + (float)AndroidUtilities.dp(12.0F);
            if ((!LocaleController.isRTL || var3 <= (float)AndroidUtilities.dp(25.0F)) && (LocaleController.isRTL || var3 >= (float)AndroidUtilities.dp(107.0F))) {
               var3 = this.lastY;
               if (var3 >= var4 && var3 <= (float)AndroidUtilities.dp(30.0F) + var4) {
                  this.startDy = this.lastY - var4;
                  this.pressed = true;
                  this.lastUpdateTime = System.currentTimeMillis();
                  this.getCurrentLetter();
                  this.invalidate();
                  return true;
               }
            }

            return false;
         }
      }
   }

   public abstract static class FastScrollAdapter extends RecyclerListView.SelectionAdapter {
      public abstract String getLetter(int var1);

      public abstract int getPositionForScrollProgress(float var1);
   }

   public static class Holder extends RecyclerView.ViewHolder {
      public Holder(View var1) {
         super(var1);
      }
   }

   public interface IntReturnCallback {
      int run();
   }

   public interface OnInterceptTouchListener {
      boolean onInterceptTouchEvent(MotionEvent var1);
   }

   public interface OnItemClickListener {
      void onItemClick(View var1, int var2);
   }

   public interface OnItemClickListenerExtended {
      void onItemClick(View var1, int var2, float var3, float var4);
   }

   public interface OnItemLongClickListener {
      boolean onItemClick(View var1, int var2);
   }

   public interface OnItemLongClickListenerExtended {
      boolean onItemClick(View var1, int var2, float var3, float var4);

      void onLongClickRelease();

      void onMove(float var1, float var2);
   }

   private class RecyclerListViewItemClickListener implements RecyclerView.OnItemTouchListener {
      public RecyclerListViewItemClickListener(Context var2) {
         RecyclerListView.this.gestureDetector = new GestureDetector(var2, new OnGestureListener() {
            public boolean onDown(MotionEvent var1) {
               return false;
            }

            public boolean onFling(MotionEvent var1, MotionEvent var2, float var3, float var4) {
               return false;
            }

            public void onLongPress(MotionEvent var1) {
               if (RecyclerListView.this.currentChildView != null && RecyclerListView.this.currentChildPosition != -1 && (RecyclerListView.this.onItemLongClickListener != null || RecyclerListView.this.onItemLongClickListenerExtended != null)) {
                  View var2 = RecyclerListView.this.currentChildView;
                  if (RecyclerListView.this.onItemLongClickListener != null) {
                     if (RecyclerListView.this.onItemLongClickListener.onItemClick(RecyclerListView.this.currentChildView, RecyclerListView.this.currentChildPosition)) {
                        var2.performHapticFeedback(0);
                        var2.sendAccessibilityEvent(2);
                     }
                  } else if (RecyclerListView.this.onItemLongClickListenerExtended != null && RecyclerListView.this.onItemLongClickListenerExtended.onItemClick(RecyclerListView.this.currentChildView, RecyclerListView.this.currentChildPosition, var1.getX() - RecyclerListView.this.currentChildView.getX(), var1.getY() - RecyclerListView.this.currentChildView.getY())) {
                     var2.performHapticFeedback(0);
                     var2.sendAccessibilityEvent(2);
                     RecyclerListView.this.longPressCalled = true;
                  }
               }

            }

            public boolean onScroll(MotionEvent var1, MotionEvent var2, float var3, float var4) {
               return false;
            }

            public void onShowPress(MotionEvent var1) {
            }

            public boolean onSingleTapUp(MotionEvent var1) {
               if (RecyclerListView.this.currentChildView != null && (RecyclerListView.this.onItemClickListener != null || RecyclerListView.this.onItemClickListenerExtended != null)) {
                  RecyclerListView var2 = RecyclerListView.this;
                  var2.onChildPressed(var2.currentChildView, true);
                  final View var3 = RecyclerListView.this.currentChildView;
                  final int var4 = RecyclerListView.this.currentChildPosition;
                  final float var5 = var1.getX();
                  final float var6 = var1.getY();
                  if (RecyclerListView.this.instantClick && var4 != -1) {
                     var3.playSoundEffect(0);
                     var3.sendAccessibilityEvent(1);
                     if (RecyclerListView.this.onItemClickListener != null) {
                        RecyclerListView.this.onItemClickListener.onItemClick(var3, var4);
                     } else if (RecyclerListView.this.onItemClickListenerExtended != null) {
                        RecyclerListView.this.onItemClickListenerExtended.onItemClick(var3, var4, var5 - var3.getX(), var6 - var3.getY());
                     }
                  }

                  var2 = RecyclerListView.this;
                  Runnable var8 = new Runnable() {
                     public void run() {
                        if (this == RecyclerListView.this.clickRunnable) {
                           RecyclerListView.this.clickRunnable = null;
                        }

                        View var1 = var3;
                        if (var1 != null) {
                           RecyclerListView.this.onChildPressed(var1, false);
                           if (!RecyclerListView.this.instantClick) {
                              var3.playSoundEffect(0);
                              var3.sendAccessibilityEvent(1);
                              if (var4 != -1) {
                                 if (RecyclerListView.this.onItemClickListener != null) {
                                    RecyclerListView.this.onItemClickListener.onItemClick(var3, var4);
                                 } else if (RecyclerListView.this.onItemClickListenerExtended != null) {
                                    RecyclerListView.OnItemClickListenerExtended var3x = RecyclerListView.this.onItemClickListenerExtended;
                                    View var2 = var3;
                                    var3x.onItemClick(var2, var4, var5 - var2.getX(), var6 - var3.getY());
                                 }
                              }
                           }
                        }

                     }
                  };
                  var2.clickRunnable = var8;
                  AndroidUtilities.runOnUIThread(var8, (long)ViewConfiguration.getPressedStateDuration());
                  if (RecyclerListView.this.selectChildRunnable != null) {
                     View var7 = RecyclerListView.this.currentChildView;
                     AndroidUtilities.cancelRunOnUIThread(RecyclerListView.this.selectChildRunnable);
                     RecyclerListView.this.selectChildRunnable = null;
                     RecyclerListView.this.currentChildView = null;
                     RecyclerListView.this.interceptedByChild = false;
                     RecyclerListView.this.removeSelection(var7, var1);
                  }
               }

               return true;
            }
         });
         RecyclerListView.this.gestureDetector.setIsLongpressEnabled(false);
      }

      // $FF: synthetic method
      public void lambda$onInterceptTouchEvent$0$RecyclerListView$RecyclerListViewItemClickListener() {
         if (RecyclerListView.this.selectChildRunnable != null && RecyclerListView.this.currentChildView != null) {
            RecyclerListView var1 = RecyclerListView.this;
            var1.onChildPressed(var1.currentChildView, true);
            RecyclerListView.this.selectChildRunnable = null;
         }

      }

      public boolean onInterceptTouchEvent(RecyclerView var1, MotionEvent var2) {
         int var3 = var2.getActionMasked();
         boolean var4;
         if (RecyclerListView.this.getScrollState() == 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         RecyclerListView var7;
         if ((var3 == 0 || var3 == 5) && RecyclerListView.this.currentChildView == null && var4) {
            float var5 = var2.getX();
            float var6 = var2.getY();
            RecyclerListView.this.longPressCalled = false;
            if (RecyclerListView.this.allowSelectChildAtPosition(var5, var6)) {
               var7 = RecyclerListView.this;
               var7.currentChildView = var7.findChildViewUnder(var5, var6);
            }

            if (RecyclerListView.this.currentChildView instanceof ViewGroup) {
               var5 = var2.getX() - (float)RecyclerListView.this.currentChildView.getLeft();
               var6 = var2.getY() - (float)RecyclerListView.this.currentChildView.getTop();
               ViewGroup var15 = (ViewGroup)RecyclerListView.this.currentChildView;

               for(int var8 = var15.getChildCount() - 1; var8 >= 0; --var8) {
                  View var9 = var15.getChildAt(var8);
                  if (var5 >= (float)var9.getLeft() && var5 <= (float)var9.getRight() && var6 >= (float)var9.getTop() && var6 <= (float)var9.getBottom() && var9.isClickable()) {
                     RecyclerListView.this.currentChildView = null;
                     break;
                  }
               }
            }

            RecyclerListView.this.currentChildPosition = -1;
            if (RecyclerListView.this.currentChildView != null) {
               var7 = RecyclerListView.this;
               var7.currentChildPosition = var1.getChildPosition(var7.currentChildView);
               MotionEvent var11 = MotionEvent.obtain(0L, 0L, var2.getActionMasked(), var2.getX() - (float)RecyclerListView.this.currentChildView.getLeft(), var2.getY() - (float)RecyclerListView.this.currentChildView.getTop(), 0);
               if (RecyclerListView.this.currentChildView.onTouchEvent(var11)) {
                  RecyclerListView.this.interceptedByChild = true;
               }

               var11.recycle();
            }
         }

         if (RecyclerListView.this.currentChildView != null && !RecyclerListView.this.interceptedByChild && var2 != null) {
            try {
               RecyclerListView.this.gestureDetector.onTouchEvent(var2);
            } catch (Exception var10) {
               FileLog.e((Throwable)var10);
            }
         }

         if (var3 != 0 && var3 != 5) {
            if ((var3 == 1 || var3 == 6 || var3 == 3 || !var4) && RecyclerListView.this.currentChildView != null) {
               if (RecyclerListView.this.selectChildRunnable != null) {
                  AndroidUtilities.cancelRunOnUIThread(RecyclerListView.this.selectChildRunnable);
                  RecyclerListView.this.selectChildRunnable = null;
               }

               View var14 = RecyclerListView.this.currentChildView;
               var7 = RecyclerListView.this;
               var7.onChildPressed(var7.currentChildView, false);
               RecyclerListView.this.currentChildView = null;
               RecyclerListView.this.interceptedByChild = false;
               RecyclerListView.this.removeSelection(var14, var2);
               if ((var3 == 1 || var3 == 6 || var3 == 3) && RecyclerListView.this.onItemLongClickListenerExtended != null && RecyclerListView.this.longPressCalled) {
                  RecyclerListView.this.onItemLongClickListenerExtended.onLongClickRelease();
                  RecyclerListView.this.longPressCalled = false;
               }
            }
         } else if (!RecyclerListView.this.interceptedByChild && RecyclerListView.this.currentChildView != null) {
            RecyclerListView.this.selectChildRunnable = new _$$Lambda$RecyclerListView$RecyclerListViewItemClickListener$XWyZ4ltKefT0aojGOGmh_BAE5L4(this);
            AndroidUtilities.runOnUIThread(RecyclerListView.this.selectChildRunnable, (long)ViewConfiguration.getTapTimeout());
            if (RecyclerListView.this.currentChildView.isEnabled()) {
               RecyclerListView var12 = RecyclerListView.this;
               var12.positionSelector(var12.currentChildPosition, RecyclerListView.this.currentChildView);
               if (RecyclerListView.this.selectorDrawable != null) {
                  Drawable var13 = RecyclerListView.this.selectorDrawable.getCurrent();
                  if (var13 instanceof TransitionDrawable) {
                     if (RecyclerListView.this.onItemLongClickListener == null && RecyclerListView.this.onItemClickListenerExtended == null) {
                        ((TransitionDrawable)var13).resetTransition();
                     } else {
                        ((TransitionDrawable)var13).startTransition(ViewConfiguration.getLongPressTimeout());
                     }
                  }

                  if (VERSION.SDK_INT >= 21) {
                     RecyclerListView.this.selectorDrawable.setHotspot(var2.getX(), var2.getY());
                  }
               }

               RecyclerListView.this.updateSelectorState();
            } else {
               RecyclerListView.this.selectorRect.setEmpty();
            }
         }

         return false;
      }

      public void onRequestDisallowInterceptTouchEvent(boolean var1) {
         RecyclerListView.this.cancelClickRunnables(true);
      }

      public void onTouchEvent(RecyclerView var1, MotionEvent var2) {
      }
   }

   public abstract static class SectionsAdapter extends RecyclerListView.FastScrollAdapter {
      private int count;
      private SparseIntArray sectionCache;
      private int sectionCount;
      private SparseIntArray sectionCountCache;
      private SparseIntArray sectionPositionCache;

      public SectionsAdapter() {
         this.cleanupCache();
      }

      private void cleanupCache() {
         SparseIntArray var1 = this.sectionCache;
         if (var1 == null) {
            this.sectionCache = new SparseIntArray();
            this.sectionPositionCache = new SparseIntArray();
            this.sectionCountCache = new SparseIntArray();
         } else {
            var1.clear();
            this.sectionPositionCache.clear();
            this.sectionCountCache.clear();
         }

         this.count = -1;
         this.sectionCount = -1;
      }

      private int internalGetCountForSection(int var1) {
         int var2 = this.sectionCountCache.get(var1, Integer.MAX_VALUE);
         if (var2 != Integer.MAX_VALUE) {
            return var2;
         } else {
            var2 = this.getCountForSection(var1);
            this.sectionCountCache.put(var1, var2);
            return var2;
         }
      }

      private int internalGetSectionCount() {
         int var1 = this.sectionCount;
         if (var1 >= 0) {
            return var1;
         } else {
            this.sectionCount = this.getSectionCount();
            return this.sectionCount;
         }
      }

      public abstract int getCountForSection(int var1);

      public final Object getItem(int var1) {
         return this.getItem(this.getSectionForPosition(var1), this.getPositionInSectionForPosition(var1));
      }

      public abstract Object getItem(int var1, int var2);

      public int getItemCount() {
         int var1 = this.count;
         if (var1 >= 0) {
            return var1;
         } else {
            var1 = 0;
            this.count = 0;

            for(int var2 = this.internalGetSectionCount(); var1 < var2; ++var1) {
               this.count += this.internalGetCountForSection(var1);
            }

            return this.count;
         }
      }

      public final int getItemViewType(int var1) {
         return this.getItemViewType(this.getSectionForPosition(var1), this.getPositionInSectionForPosition(var1));
      }

      public abstract int getItemViewType(int var1, int var2);

      public int getPositionInSectionForPosition(int var1) {
         int var2 = this.sectionPositionCache.get(var1, Integer.MAX_VALUE);
         if (var2 != Integer.MAX_VALUE) {
            return var2;
         } else {
            int var3 = this.internalGetSectionCount();
            var2 = 0;

            int var5;
            for(int var4 = 0; var2 < var3; var4 = var5) {
               var5 = this.internalGetCountForSection(var2) + var4;
               if (var1 >= var4 && var1 < var5) {
                  var2 = var1 - var4;
                  this.sectionPositionCache.put(var1, var2);
                  return var2;
               }

               ++var2;
            }

            return -1;
         }
      }

      public abstract int getSectionCount();

      public final int getSectionForPosition(int var1) {
         int var2 = this.sectionCache.get(var1, Integer.MAX_VALUE);
         if (var2 != Integer.MAX_VALUE) {
            return var2;
         } else {
            int var3 = this.internalGetSectionCount();
            var2 = 0;

            int var5;
            for(int var4 = 0; var2 < var3; var4 = var5) {
               var5 = this.internalGetCountForSection(var2) + var4;
               if (var1 >= var4 && var1 < var5) {
                  this.sectionCache.put(var1, var2);
                  return var2;
               }

               ++var2;
            }

            return -1;
         }
      }

      public abstract View getSectionHeaderView(int var1, View var2);

      public abstract boolean isEnabled(int var1, int var2);

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getAdapterPosition();
         return this.isEnabled(this.getSectionForPosition(var2), this.getPositionInSectionForPosition(var2));
      }

      public void notifyDataSetChanged() {
         this.cleanupCache();
         super.notifyDataSetChanged();
      }

      public void notifySectionsChanged() {
         this.cleanupCache();
      }

      public abstract void onBindViewHolder(int var1, int var2, RecyclerView.ViewHolder var3);

      public final void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         this.onBindViewHolder(this.getSectionForPosition(var2), this.getPositionInSectionForPosition(var2), var1);
      }
   }

   public abstract static class SelectionAdapter extends RecyclerView.Adapter {
      public int getSelectionBottomPadding(View var1) {
         return 0;
      }

      public abstract boolean isEnabled(RecyclerView.ViewHolder var1);
   }
}
