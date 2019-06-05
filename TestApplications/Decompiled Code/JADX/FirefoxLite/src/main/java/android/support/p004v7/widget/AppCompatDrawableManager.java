package android.support.p004v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.Theme;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.LayerDrawable;
import android.os.Build.VERSION;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.p001v4.content.ContextCompat;
import android.support.p001v4.graphics.ColorUtils;
import android.support.p001v4.graphics.drawable.DrawableCompat;
import android.support.p001v4.util.ArrayMap;
import android.support.p001v4.util.LongSparseArray;
import android.support.p001v4.util.LruCache;
import android.support.p001v4.util.SparseArrayCompat;
import android.support.p004v7.appcompat.C0187R;
import android.support.p004v7.content.res.AppCompatResources;
import android.support.p004v7.graphics.drawable.AnimatedStateListDrawableCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: android.support.v7.widget.AppCompatDrawableManager */
public final class AppCompatDrawableManager {
    private static final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY = new int[]{C0187R.C0185drawable.abc_popup_background_mtrl_mult, C0187R.C0185drawable.abc_cab_background_internal_bg, C0187R.C0185drawable.abc_menu_hardkey_panel_mtrl_mult};
    private static final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED = new int[]{C0187R.C0185drawable.abc_textfield_activated_mtrl_alpha, C0187R.C0185drawable.abc_textfield_search_activated_mtrl_alpha, C0187R.C0185drawable.abc_cab_background_top_mtrl_alpha, C0187R.C0185drawable.abc_text_cursor_material, C0187R.C0185drawable.abc_text_select_handle_left_mtrl_dark, C0187R.C0185drawable.abc_text_select_handle_middle_mtrl_dark, C0187R.C0185drawable.abc_text_select_handle_right_mtrl_dark, C0187R.C0185drawable.abc_text_select_handle_left_mtrl_light, C0187R.C0185drawable.abc_text_select_handle_middle_mtrl_light, C0187R.C0185drawable.abc_text_select_handle_right_mtrl_light};
    private static final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL = new int[]{C0187R.C0185drawable.abc_textfield_search_default_mtrl_alpha, C0187R.C0185drawable.abc_textfield_default_mtrl_alpha, C0187R.C0185drawable.abc_ab_share_pack_mtrl_alpha};
    private static final ColorFilterLruCache COLOR_FILTER_CACHE = new ColorFilterLruCache(6);
    private static final Mode DEFAULT_MODE = Mode.SRC_IN;
    private static AppCompatDrawableManager INSTANCE;
    private static final int[] TINT_CHECKABLE_BUTTON_LIST = new int[]{C0187R.C0185drawable.abc_btn_check_material, C0187R.C0185drawable.abc_btn_radio_material};
    private static final int[] TINT_COLOR_CONTROL_NORMAL = new int[]{C0187R.C0185drawable.abc_ic_commit_search_api_mtrl_alpha, C0187R.C0185drawable.abc_seekbar_tick_mark_material, C0187R.C0185drawable.abc_ic_menu_share_mtrl_alpha, C0187R.C0185drawable.abc_ic_menu_copy_mtrl_am_alpha, C0187R.C0185drawable.abc_ic_menu_cut_mtrl_alpha, C0187R.C0185drawable.abc_ic_menu_selectall_mtrl_alpha, C0187R.C0185drawable.abc_ic_menu_paste_mtrl_am_alpha};
    private static final int[] TINT_COLOR_CONTROL_STATE_LIST = new int[]{C0187R.C0185drawable.abc_tab_indicator_material, C0187R.C0185drawable.abc_textfield_search_material};
    private ArrayMap<String, InflateDelegate> mDelegates;
    private final WeakHashMap<Context, LongSparseArray<WeakReference<ConstantState>>> mDrawableCaches = new WeakHashMap(0);
    private boolean mHasCheckedVectorDrawableSetup;
    private SparseArrayCompat<String> mKnownDrawableIdTags;
    private WeakHashMap<Context, SparseArrayCompat<ColorStateList>> mTintLists;
    private TypedValue mTypedValue;

    /* renamed from: android.support.v7.widget.AppCompatDrawableManager$InflateDelegate */
    private interface InflateDelegate {
        Drawable createFromXmlInner(Context context, XmlPullParser xmlPullParser, AttributeSet attributeSet, Theme theme);
    }

    /* renamed from: android.support.v7.widget.AppCompatDrawableManager$AsldcInflateDelegate */
    static class AsldcInflateDelegate implements InflateDelegate {
        AsldcInflateDelegate() {
        }

        public Drawable createFromXmlInner(Context context, XmlPullParser xmlPullParser, AttributeSet attributeSet, Theme theme) {
            try {
                return AnimatedStateListDrawableCompat.createFromXmlInner(context, context.getResources(), xmlPullParser, attributeSet, theme);
            } catch (Exception e) {
                Log.e("AsldcInflateDelegate", "Exception while inflating <animated-selector>", e);
                return null;
            }
        }
    }

    /* renamed from: android.support.v7.widget.AppCompatDrawableManager$AvdcInflateDelegate */
    private static class AvdcInflateDelegate implements InflateDelegate {
        AvdcInflateDelegate() {
        }

        public Drawable createFromXmlInner(Context context, XmlPullParser xmlPullParser, AttributeSet attributeSet, Theme theme) {
            try {
                return AnimatedVectorDrawableCompat.createFromXmlInner(context, context.getResources(), xmlPullParser, attributeSet, theme);
            } catch (Exception e) {
                Log.e("AvdcInflateDelegate", "Exception while inflating <animated-vector>", e);
                return null;
            }
        }
    }

    /* renamed from: android.support.v7.widget.AppCompatDrawableManager$ColorFilterLruCache */
    private static class ColorFilterLruCache extends LruCache<Integer, PorterDuffColorFilter> {
        public ColorFilterLruCache(int i) {
            super(i);
        }

        /* Access modifiers changed, original: 0000 */
        public PorterDuffColorFilter get(int i, Mode mode) {
            return (PorterDuffColorFilter) get(Integer.valueOf(ColorFilterLruCache.generateCacheKey(i, mode)));
        }

        /* Access modifiers changed, original: 0000 */
        public PorterDuffColorFilter put(int i, Mode mode, PorterDuffColorFilter porterDuffColorFilter) {
            return (PorterDuffColorFilter) put(Integer.valueOf(ColorFilterLruCache.generateCacheKey(i, mode)), porterDuffColorFilter);
        }

        private static int generateCacheKey(int i, Mode mode) {
            return ((i + 31) * 31) + mode.hashCode();
        }
    }

    /* renamed from: android.support.v7.widget.AppCompatDrawableManager$VdcInflateDelegate */
    private static class VdcInflateDelegate implements InflateDelegate {
        VdcInflateDelegate() {
        }

        public Drawable createFromXmlInner(Context context, XmlPullParser xmlPullParser, AttributeSet attributeSet, Theme theme) {
            try {
                return VectorDrawableCompat.createFromXmlInner(context.getResources(), xmlPullParser, attributeSet, theme);
            } catch (Exception e) {
                Log.e("VdcInflateDelegate", "Exception while inflating <vector>", e);
                return null;
            }
        }
    }

    public static synchronized AppCompatDrawableManager get() {
        AppCompatDrawableManager appCompatDrawableManager;
        synchronized (AppCompatDrawableManager.class) {
            if (INSTANCE == null) {
                INSTANCE = new AppCompatDrawableManager();
                AppCompatDrawableManager.installDefaultInflateDelegates(INSTANCE);
            }
            appCompatDrawableManager = INSTANCE;
        }
        return appCompatDrawableManager;
    }

    private static void installDefaultInflateDelegates(AppCompatDrawableManager appCompatDrawableManager) {
        if (VERSION.SDK_INT < 24) {
            appCompatDrawableManager.addDelegate("vector", new VdcInflateDelegate());
            appCompatDrawableManager.addDelegate("animated-vector", new AvdcInflateDelegate());
            appCompatDrawableManager.addDelegate("animated-selector", new AsldcInflateDelegate());
        }
    }

    public synchronized Drawable getDrawable(Context context, int i) {
        return getDrawable(context, i, false);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized Drawable getDrawable(Context context, int i, boolean z) {
        Drawable loadDrawableFromDelegates;
        checkVectorDrawableSetup(context);
        loadDrawableFromDelegates = loadDrawableFromDelegates(context, i);
        if (loadDrawableFromDelegates == null) {
            loadDrawableFromDelegates = createDrawableIfNeeded(context, i);
        }
        if (loadDrawableFromDelegates == null) {
            loadDrawableFromDelegates = ContextCompat.getDrawable(context, i);
        }
        if (loadDrawableFromDelegates != null) {
            loadDrawableFromDelegates = tintDrawable(context, i, z, loadDrawableFromDelegates);
        }
        if (loadDrawableFromDelegates != null) {
            DrawableUtils.fixDrawable(loadDrawableFromDelegates);
        }
        return loadDrawableFromDelegates;
    }

    public synchronized void onConfigurationChanged(Context context) {
        LongSparseArray longSparseArray = (LongSparseArray) this.mDrawableCaches.get(context);
        if (longSparseArray != null) {
            longSparseArray.clear();
        }
    }

    private static long createCacheKey(TypedValue typedValue) {
        return (((long) typedValue.assetCookie) << 32) | ((long) typedValue.data);
    }

    private Drawable createDrawableIfNeeded(Context context, int i) {
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        TypedValue typedValue = this.mTypedValue;
        context.getResources().getValue(i, typedValue, true);
        long createCacheKey = AppCompatDrawableManager.createCacheKey(typedValue);
        Drawable cachedDrawable = getCachedDrawable(context, createCacheKey);
        if (cachedDrawable != null) {
            return cachedDrawable;
        }
        if (i == C0187R.C0185drawable.abc_cab_background_top_material) {
            cachedDrawable = new LayerDrawable(new Drawable[]{getDrawable(context, C0187R.C0185drawable.abc_cab_background_internal_bg), getDrawable(context, C0187R.C0185drawable.abc_cab_background_top_mtrl_alpha)});
        }
        if (cachedDrawable != null) {
            cachedDrawable.setChangingConfigurations(typedValue.changingConfigurations);
            addDrawableToCache(context, createCacheKey, cachedDrawable);
        }
        return cachedDrawable;
    }

    private Drawable tintDrawable(Context context, int i, boolean z, Drawable drawable) {
        ColorStateList tintList = getTintList(context, i);
        LayerDrawable layerDrawable;
        if (tintList != null) {
            if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
                drawable = drawable.mutate();
            }
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTintList(drawable, tintList);
            Mode tintMode = AppCompatDrawableManager.getTintMode(i);
            if (tintMode == null) {
                return drawable;
            }
            DrawableCompat.setTintMode(drawable, tintMode);
            return drawable;
        } else if (i == C0187R.C0185drawable.abc_seekbar_track_material) {
            layerDrawable = (LayerDrawable) drawable;
            AppCompatDrawableManager.setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908288), ThemeUtils.getThemeAttrColor(context, C0187R.attr.colorControlNormal), DEFAULT_MODE);
            AppCompatDrawableManager.setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, C0187R.attr.colorControlNormal), DEFAULT_MODE);
            AppCompatDrawableManager.setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, C0187R.attr.colorControlActivated), DEFAULT_MODE);
            return drawable;
        } else if (i == C0187R.C0185drawable.abc_ratingbar_material || i == C0187R.C0185drawable.abc_ratingbar_indicator_material || i == C0187R.C0185drawable.abc_ratingbar_small_material) {
            layerDrawable = (LayerDrawable) drawable;
            AppCompatDrawableManager.setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908288), ThemeUtils.getDisabledThemeAttrColor(context, C0187R.attr.colorControlNormal), DEFAULT_MODE);
            AppCompatDrawableManager.setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, C0187R.attr.colorControlActivated), DEFAULT_MODE);
            AppCompatDrawableManager.setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, C0187R.attr.colorControlActivated), DEFAULT_MODE);
            return drawable;
        } else if (AppCompatDrawableManager.tintDrawableUsingColorFilter(context, i, drawable) || !z) {
            return drawable;
        } else {
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x00a0 A:{Catch:{ Exception -> 0x00a8 }} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0079 A:{Catch:{ Exception -> 0x00a8 }} */
    private android.graphics.drawable.Drawable loadDrawableFromDelegates(android.content.Context r10, int r11) {
        /*
        r9 = this;
        r0 = r9.mDelegates;
        r1 = 0;
        if (r0 == 0) goto L_0x00ba;
    L_0x0005:
        r0 = r9.mDelegates;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x00ba;
    L_0x000d:
        r0 = r9.mKnownDrawableIdTags;
        if (r0 == 0) goto L_0x002c;
    L_0x0011:
        r0 = r9.mKnownDrawableIdTags;
        r0 = r0.get(r11);
        r0 = (java.lang.String) r0;
        r2 = "appcompat_skip_skip";
        r2 = r2.equals(r0);
        if (r2 != 0) goto L_0x002b;
    L_0x0021:
        if (r0 == 0) goto L_0x0033;
    L_0x0023:
        r2 = r9.mDelegates;
        r0 = r2.get(r0);
        if (r0 != 0) goto L_0x0033;
    L_0x002b:
        return r1;
    L_0x002c:
        r0 = new android.support.v4.util.SparseArrayCompat;
        r0.<init>();
        r9.mKnownDrawableIdTags = r0;
    L_0x0033:
        r0 = r9.mTypedValue;
        if (r0 != 0) goto L_0x003e;
    L_0x0037:
        r0 = new android.util.TypedValue;
        r0.<init>();
        r9.mTypedValue = r0;
    L_0x003e:
        r0 = r9.mTypedValue;
        r1 = r10.getResources();
        r2 = 1;
        r1.getValue(r11, r0, r2);
        r3 = android.support.p004v7.widget.AppCompatDrawableManager.createCacheKey(r0);
        r5 = r9.getCachedDrawable(r10, r3);
        if (r5 == 0) goto L_0x0053;
    L_0x0052:
        return r5;
    L_0x0053:
        r6 = r0.string;
        if (r6 == 0) goto L_0x00b0;
    L_0x0057:
        r6 = r0.string;
        r6 = r6.toString();
        r7 = ".xml";
        r6 = r6.endsWith(r7);
        if (r6 == 0) goto L_0x00b0;
    L_0x0065:
        r1 = r1.getXml(r11);	 Catch:{ Exception -> 0x00a8 }
        r6 = android.util.Xml.asAttributeSet(r1);	 Catch:{ Exception -> 0x00a8 }
    L_0x006d:
        r7 = r1.next();	 Catch:{ Exception -> 0x00a8 }
        r8 = 2;
        if (r7 == r8) goto L_0x0077;
    L_0x0074:
        if (r7 == r2) goto L_0x0077;
    L_0x0076:
        goto L_0x006d;
    L_0x0077:
        if (r7 != r8) goto L_0x00a0;
    L_0x0079:
        r2 = r1.getName();	 Catch:{ Exception -> 0x00a8 }
        r7 = r9.mKnownDrawableIdTags;	 Catch:{ Exception -> 0x00a8 }
        r7.append(r11, r2);	 Catch:{ Exception -> 0x00a8 }
        r7 = r9.mDelegates;	 Catch:{ Exception -> 0x00a8 }
        r2 = r7.get(r2);	 Catch:{ Exception -> 0x00a8 }
        r2 = (android.support.p004v7.widget.AppCompatDrawableManager.InflateDelegate) r2;	 Catch:{ Exception -> 0x00a8 }
        if (r2 == 0) goto L_0x0095;
    L_0x008c:
        r7 = r10.getTheme();	 Catch:{ Exception -> 0x00a8 }
        r1 = r2.createFromXmlInner(r10, r1, r6, r7);	 Catch:{ Exception -> 0x00a8 }
        r5 = r1;
    L_0x0095:
        if (r5 == 0) goto L_0x00b0;
    L_0x0097:
        r0 = r0.changingConfigurations;	 Catch:{ Exception -> 0x00a8 }
        r5.setChangingConfigurations(r0);	 Catch:{ Exception -> 0x00a8 }
        r9.addDrawableToCache(r10, r3, r5);	 Catch:{ Exception -> 0x00a8 }
        goto L_0x00b0;
    L_0x00a0:
        r10 = new org.xmlpull.v1.XmlPullParserException;	 Catch:{ Exception -> 0x00a8 }
        r0 = "No start tag found";
        r10.<init>(r0);	 Catch:{ Exception -> 0x00a8 }
        throw r10;	 Catch:{ Exception -> 0x00a8 }
    L_0x00a8:
        r10 = move-exception;
        r0 = "AppCompatDrawableManag";
        r1 = "Exception while inflating drawable";
        android.util.Log.e(r0, r1, r10);
    L_0x00b0:
        if (r5 != 0) goto L_0x00b9;
    L_0x00b2:
        r10 = r9.mKnownDrawableIdTags;
        r0 = "appcompat_skip_skip";
        r10.append(r11, r0);
    L_0x00b9:
        return r5;
    L_0x00ba:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p004v7.widget.AppCompatDrawableManager.loadDrawableFromDelegates(android.content.Context, int):android.graphics.drawable.Drawable");
    }

    /* JADX WARNING: Missing block: B:18:0x002c, code skipped:
            return null;
     */
    private synchronized android.graphics.drawable.Drawable getCachedDrawable(android.content.Context r4, long r5) {
        /*
        r3 = this;
        monitor-enter(r3);
        r0 = r3.mDrawableCaches;	 Catch:{ all -> 0x002d }
        r0 = r0.get(r4);	 Catch:{ all -> 0x002d }
        r0 = (android.support.p001v4.util.LongSparseArray) r0;	 Catch:{ all -> 0x002d }
        r1 = 0;
        if (r0 != 0) goto L_0x000e;
    L_0x000c:
        monitor-exit(r3);
        return r1;
    L_0x000e:
        r2 = r0.get(r5);	 Catch:{ all -> 0x002d }
        r2 = (java.lang.ref.WeakReference) r2;	 Catch:{ all -> 0x002d }
        if (r2 == 0) goto L_0x002b;
    L_0x0016:
        r2 = r2.get();	 Catch:{ all -> 0x002d }
        r2 = (android.graphics.drawable.Drawable.ConstantState) r2;	 Catch:{ all -> 0x002d }
        if (r2 == 0) goto L_0x0028;
    L_0x001e:
        r4 = r4.getResources();	 Catch:{ all -> 0x002d }
        r4 = r2.newDrawable(r4);	 Catch:{ all -> 0x002d }
        monitor-exit(r3);
        return r4;
    L_0x0028:
        r0.delete(r5);	 Catch:{ all -> 0x002d }
    L_0x002b:
        monitor-exit(r3);
        return r1;
    L_0x002d:
        r4 = move-exception;
        monitor-exit(r3);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p004v7.widget.AppCompatDrawableManager.getCachedDrawable(android.content.Context, long):android.graphics.drawable.Drawable");
    }

    private synchronized boolean addDrawableToCache(Context context, long j, Drawable drawable) {
        ConstantState constantState = drawable.getConstantState();
        if (constantState == null) {
            return false;
        }
        LongSparseArray longSparseArray = (LongSparseArray) this.mDrawableCaches.get(context);
        if (longSparseArray == null) {
            longSparseArray = new LongSparseArray();
            this.mDrawableCaches.put(context, longSparseArray);
        }
        longSparseArray.put(j, new WeakReference(constantState));
        return true;
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized Drawable onDrawableLoadedFromResources(Context context, VectorEnabledTintResources vectorEnabledTintResources, int i) {
        Drawable loadDrawableFromDelegates = loadDrawableFromDelegates(context, i);
        if (loadDrawableFromDelegates == null) {
            loadDrawableFromDelegates = vectorEnabledTintResources.superGetDrawable(i);
        }
        if (loadDrawableFromDelegates == null) {
            return null;
        }
        return tintDrawable(context, i, false, loadDrawableFromDelegates);
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0061 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0046  */
    static boolean tintDrawableUsingColorFilter(android.content.Context r6, int r7, android.graphics.drawable.Drawable r8) {
        /*
        r0 = DEFAULT_MODE;
        r1 = COLORFILTER_TINT_COLOR_CONTROL_NORMAL;
        r1 = android.support.p004v7.widget.AppCompatDrawableManager.arrayContains(r1, r7);
        r2 = 16842801; // 0x1010031 float:2.3693695E-38 double:8.3214494E-317;
        r3 = -1;
        r4 = 0;
        r5 = 1;
        if (r1 == 0) goto L_0x0015;
    L_0x0010:
        r2 = android.support.p004v7.appcompat.C0187R.attr.colorControlNormal;
    L_0x0012:
        r7 = 1;
        r1 = -1;
        goto L_0x0044;
    L_0x0015:
        r1 = COLORFILTER_COLOR_CONTROL_ACTIVATED;
        r1 = android.support.p004v7.widget.AppCompatDrawableManager.arrayContains(r1, r7);
        if (r1 == 0) goto L_0x0020;
    L_0x001d:
        r2 = android.support.p004v7.appcompat.C0187R.attr.colorControlActivated;
        goto L_0x0012;
    L_0x0020:
        r1 = COLORFILTER_COLOR_BACKGROUND_MULTIPLY;
        r1 = android.support.p004v7.widget.AppCompatDrawableManager.arrayContains(r1, r7);
        if (r1 == 0) goto L_0x002b;
    L_0x0028:
        r0 = android.graphics.PorterDuff.Mode.MULTIPLY;
        goto L_0x0012;
    L_0x002b:
        r1 = android.support.p004v7.appcompat.C0187R.C0185drawable.abc_list_divider_mtrl_alpha;
        if (r7 != r1) goto L_0x003c;
    L_0x002f:
        r2 = 16842800; // 0x1010030 float:2.3693693E-38 double:8.321449E-317;
        r7 = 1109603123; // 0x42233333 float:40.8 double:5.482167836E-315;
        r7 = java.lang.Math.round(r7);
        r1 = r7;
        r7 = 1;
        goto L_0x0044;
    L_0x003c:
        r1 = android.support.p004v7.appcompat.C0187R.C0185drawable.abc_dialog_material_background;
        if (r7 != r1) goto L_0x0041;
    L_0x0040:
        goto L_0x0012;
    L_0x0041:
        r7 = 0;
        r1 = -1;
        r2 = 0;
    L_0x0044:
        if (r7 == 0) goto L_0x0061;
    L_0x0046:
        r7 = android.support.p004v7.widget.DrawableUtils.canSafelyMutateDrawable(r8);
        if (r7 == 0) goto L_0x0050;
    L_0x004c:
        r8 = r8.mutate();
    L_0x0050:
        r6 = android.support.p004v7.widget.ThemeUtils.getThemeAttrColor(r6, r2);
        r6 = android.support.p004v7.widget.AppCompatDrawableManager.getPorterDuffColorFilter(r6, r0);
        r8.setColorFilter(r6);
        if (r1 == r3) goto L_0x0060;
    L_0x005d:
        r8.setAlpha(r1);
    L_0x0060:
        return r5;
    L_0x0061:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p004v7.widget.AppCompatDrawableManager.tintDrawableUsingColorFilter(android.content.Context, int, android.graphics.drawable.Drawable):boolean");
    }

    private void addDelegate(String str, InflateDelegate inflateDelegate) {
        if (this.mDelegates == null) {
            this.mDelegates = new ArrayMap();
        }
        this.mDelegates.put(str, inflateDelegate);
    }

    private static boolean arrayContains(int[] iArr, int i) {
        for (int i2 : iArr) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    static Mode getTintMode(int i) {
        return i == C0187R.C0185drawable.abc_switch_thumb_material ? Mode.MULTIPLY : null;
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized ColorStateList getTintList(Context context, int i) {
        ColorStateList tintListFromCache;
        tintListFromCache = getTintListFromCache(context, i);
        if (tintListFromCache == null) {
            if (i == C0187R.C0185drawable.abc_edit_text_material) {
                tintListFromCache = AppCompatResources.getColorStateList(context, C0187R.color.abc_tint_edittext);
            } else if (i == C0187R.C0185drawable.abc_switch_track_mtrl_alpha) {
                tintListFromCache = AppCompatResources.getColorStateList(context, C0187R.color.abc_tint_switch_track);
            } else if (i == C0187R.C0185drawable.abc_switch_thumb_material) {
                tintListFromCache = createSwitchThumbColorStateList(context);
            } else if (i == C0187R.C0185drawable.abc_btn_default_mtrl_shape) {
                tintListFromCache = createDefaultButtonColorStateList(context);
            } else if (i == C0187R.C0185drawable.abc_btn_borderless_material) {
                tintListFromCache = createBorderlessButtonColorStateList(context);
            } else if (i == C0187R.C0185drawable.abc_btn_colored_material) {
                tintListFromCache = createColoredButtonColorStateList(context);
            } else {
                if (i != C0187R.C0185drawable.abc_spinner_mtrl_am_alpha) {
                    if (i != C0187R.C0185drawable.abc_spinner_textfield_background_material) {
                        if (AppCompatDrawableManager.arrayContains(TINT_COLOR_CONTROL_NORMAL, i)) {
                            tintListFromCache = ThemeUtils.getThemeAttrColorStateList(context, C0187R.attr.colorControlNormal);
                        } else if (AppCompatDrawableManager.arrayContains(TINT_COLOR_CONTROL_STATE_LIST, i)) {
                            tintListFromCache = AppCompatResources.getColorStateList(context, C0187R.color.abc_tint_default);
                        } else if (AppCompatDrawableManager.arrayContains(TINT_CHECKABLE_BUTTON_LIST, i)) {
                            tintListFromCache = AppCompatResources.getColorStateList(context, C0187R.color.abc_tint_btn_checkable);
                        } else if (i == C0187R.C0185drawable.abc_seekbar_thumb_material) {
                            tintListFromCache = AppCompatResources.getColorStateList(context, C0187R.color.abc_tint_seek_thumb);
                        }
                    }
                }
                tintListFromCache = AppCompatResources.getColorStateList(context, C0187R.color.abc_tint_spinner);
            }
            if (tintListFromCache != null) {
                addTintListToCache(context, i, tintListFromCache);
            }
        }
        return tintListFromCache;
    }

    private ColorStateList getTintListFromCache(Context context, int i) {
        ColorStateList colorStateList = null;
        if (this.mTintLists == null) {
            return null;
        }
        SparseArrayCompat sparseArrayCompat = (SparseArrayCompat) this.mTintLists.get(context);
        if (sparseArrayCompat != null) {
            colorStateList = (ColorStateList) sparseArrayCompat.get(i);
        }
        return colorStateList;
    }

    private void addTintListToCache(Context context, int i, ColorStateList colorStateList) {
        if (this.mTintLists == null) {
            this.mTintLists = new WeakHashMap();
        }
        SparseArrayCompat sparseArrayCompat = (SparseArrayCompat) this.mTintLists.get(context);
        if (sparseArrayCompat == null) {
            sparseArrayCompat = new SparseArrayCompat();
            this.mTintLists.put(context, sparseArrayCompat);
        }
        sparseArrayCompat.append(i, colorStateList);
    }

    private ColorStateList createDefaultButtonColorStateList(Context context) {
        return createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, C0187R.attr.colorButtonNormal));
    }

    private ColorStateList createBorderlessButtonColorStateList(Context context) {
        return createButtonColorStateList(context, 0);
    }

    private ColorStateList createColoredButtonColorStateList(Context context) {
        return createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, C0187R.attr.colorAccent));
    }

    private ColorStateList createButtonColorStateList(Context context, int i) {
        r1 = new int[4][];
        r0 = new int[4];
        int themeAttrColor = ThemeUtils.getThemeAttrColor(context, C0187R.attr.colorControlHighlight);
        int disabledThemeAttrColor = ThemeUtils.getDisabledThemeAttrColor(context, C0187R.attr.colorButtonNormal);
        r1[0] = ThemeUtils.DISABLED_STATE_SET;
        r0[0] = disabledThemeAttrColor;
        r1[1] = ThemeUtils.PRESSED_STATE_SET;
        r0[1] = ColorUtils.compositeColors(themeAttrColor, i);
        r1[2] = ThemeUtils.FOCUSED_STATE_SET;
        r0[2] = ColorUtils.compositeColors(themeAttrColor, i);
        r1[3] = ThemeUtils.EMPTY_STATE_SET;
        r0[3] = i;
        return new ColorStateList(r1, r0);
    }

    private ColorStateList createSwitchThumbColorStateList(Context context) {
        int[][] iArr = new int[3][];
        int[] iArr2 = new int[3];
        ColorStateList themeAttrColorStateList = ThemeUtils.getThemeAttrColorStateList(context, C0187R.attr.colorSwitchThumbNormal);
        if (themeAttrColorStateList == null || !themeAttrColorStateList.isStateful()) {
            iArr[0] = ThemeUtils.DISABLED_STATE_SET;
            iArr2[0] = ThemeUtils.getDisabledThemeAttrColor(context, C0187R.attr.colorSwitchThumbNormal);
            iArr[1] = ThemeUtils.CHECKED_STATE_SET;
            iArr2[1] = ThemeUtils.getThemeAttrColor(context, C0187R.attr.colorControlActivated);
            iArr[2] = ThemeUtils.EMPTY_STATE_SET;
            iArr2[2] = ThemeUtils.getThemeAttrColor(context, C0187R.attr.colorSwitchThumbNormal);
        } else {
            iArr[0] = ThemeUtils.DISABLED_STATE_SET;
            iArr2[0] = themeAttrColorStateList.getColorForState(iArr[0], 0);
            iArr[1] = ThemeUtils.CHECKED_STATE_SET;
            iArr2[1] = ThemeUtils.getThemeAttrColor(context, C0187R.attr.colorControlActivated);
            iArr[2] = ThemeUtils.EMPTY_STATE_SET;
            iArr2[2] = themeAttrColorStateList.getDefaultColor();
        }
        return new ColorStateList(iArr, iArr2);
    }

    static void tintDrawable(Drawable drawable, TintInfo tintInfo, int[] iArr) {
        if (!DrawableUtils.canSafelyMutateDrawable(drawable) || drawable.mutate() == drawable) {
            if (tintInfo.mHasTintList || tintInfo.mHasTintMode) {
                drawable.setColorFilter(AppCompatDrawableManager.createTintFilter(tintInfo.mHasTintList ? tintInfo.mTintList : null, tintInfo.mHasTintMode ? tintInfo.mTintMode : DEFAULT_MODE, iArr));
            } else {
                drawable.clearColorFilter();
            }
            if (VERSION.SDK_INT <= 23) {
                drawable.invalidateSelf();
            }
            return;
        }
        Log.d("AppCompatDrawableManag", "Mutated drawable is not the same instance as the input.");
    }

    private static PorterDuffColorFilter createTintFilter(ColorStateList colorStateList, Mode mode, int[] iArr) {
        return (colorStateList == null || mode == null) ? null : AppCompatDrawableManager.getPorterDuffColorFilter(colorStateList.getColorForState(iArr, 0), mode);
    }

    public static synchronized PorterDuffColorFilter getPorterDuffColorFilter(int i, Mode mode) {
        PorterDuffColorFilter porterDuffColorFilter;
        synchronized (AppCompatDrawableManager.class) {
            porterDuffColorFilter = COLOR_FILTER_CACHE.get(i, mode);
            if (porterDuffColorFilter == null) {
                porterDuffColorFilter = new PorterDuffColorFilter(i, mode);
                COLOR_FILTER_CACHE.put(i, mode, porterDuffColorFilter);
            }
        }
        return porterDuffColorFilter;
    }

    private static void setPorterDuffColorFilter(Drawable drawable, int i, Mode mode) {
        if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
            drawable = drawable.mutate();
        }
        if (mode == null) {
            mode = DEFAULT_MODE;
        }
        drawable.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(i, mode));
    }

    private void checkVectorDrawableSetup(Context context) {
        if (!this.mHasCheckedVectorDrawableSetup) {
            this.mHasCheckedVectorDrawableSetup = true;
            Drawable drawable = getDrawable(context, C0187R.C0185drawable.abc_vector_test);
            if (drawable == null || !AppCompatDrawableManager.isVectorDrawable(drawable)) {
                this.mHasCheckedVectorDrawableSetup = false;
                throw new IllegalStateException("This app has been built with an incorrect configuration. Please configure your build for VectorDrawableCompat.");
            }
        }
    }

    private static boolean isVectorDrawable(Drawable drawable) {
        return (drawable instanceof VectorDrawableCompat) || "android.graphics.drawable.VectorDrawable".equals(drawable.getClass().getName());
    }
}
