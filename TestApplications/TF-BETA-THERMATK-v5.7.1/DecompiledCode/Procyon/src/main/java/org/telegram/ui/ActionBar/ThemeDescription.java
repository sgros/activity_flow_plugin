// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.ActionBar;

import java.util.ArrayList;
import android.view.ViewGroup;
import org.telegram.ui.Components.RecyclerListView;
import androidx.viewpager.widget.ViewPager;
import org.telegram.messenger.AndroidUtilities;
import android.widget.ScrollView;
import android.widget.EditText;
import org.telegram.ui.Components.ChatBigEmptyView;
import org.telegram.ui.Components.NumberTextView;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.EmptyTextProgressView;
import android.graphics.drawable.ShapeDrawable;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.ScamDrawable;
import org.telegram.ui.Components.GroupCreateSpan;
import org.telegram.messenger.FileLog;
import org.telegram.ui.Components.SeekBarView;
import org.telegram.ui.Components.LineProgressView;
import android.text.TextPaint;
import org.telegram.ui.Components.RadioButton;
import org.telegram.ui.Components.GroupCreateCheckBox;
import org.telegram.ui.Components.CheckBox;
import android.graphics.drawable.GradientDrawable;
import org.telegram.ui.Components.LetterDrawable;
import org.telegram.ui.Components.BackupImageView;
import android.widget.ImageView;
import org.telegram.ui.Components.TypefaceSpan;
import android.text.SpannedString;
import org.telegram.ui.Components.EditTextCaption;
import org.telegram.ui.Components.MessageBackgroundDrawable;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.SimpleColorFilter;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.LottieAnimationView;
import android.widget.TextView;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.graphics.drawable.RippleDrawable;
import android.os.Build$VERSION;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.ColorDrawable;
import org.telegram.ui.Components.CombinedDrawable;
import com.airbnb.lottie.LottieDrawable;
import org.telegram.ui.Components.EditTextEmoji;
import android.view.View;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import java.lang.reflect.Field;
import java.util.HashMap;

public class ThemeDescription
{
    public static int FLAG_AB_AM_BACKGROUND = 1048576;
    public static int FLAG_AB_AM_ITEMSCOLOR = 512;
    public static int FLAG_AB_AM_SELECTORCOLOR = 4194304;
    public static int FLAG_AB_AM_TOPBACKGROUND = 2097152;
    public static int FLAG_AB_ITEMSCOLOR = 64;
    public static int FLAG_AB_SEARCH = 134217728;
    public static int FLAG_AB_SEARCHPLACEHOLDER = 67108864;
    public static int FLAG_AB_SELECTORCOLOR = 256;
    public static int FLAG_AB_SUBMENUBACKGROUND = Integer.MIN_VALUE;
    public static int FLAG_AB_SUBMENUITEM = 1073741824;
    public static int FLAG_AB_SUBTITLECOLOR = 1024;
    public static int FLAG_AB_TITLECOLOR = 128;
    public static int FLAG_BACKGROUND = 1;
    public static int FLAG_BACKGROUNDFILTER = 32;
    public static int FLAG_CELLBACKGROUNDCOLOR = 16;
    public static int FLAG_CHECKBOX = 8192;
    public static int FLAG_CHECKBOXCHECK = 16384;
    public static int FLAG_CHECKTAG = 262144;
    public static int FLAG_CURSORCOLOR = 16777216;
    public static int FLAG_DRAWABLESELECTEDSTATE = 65536;
    public static int FLAG_FASTSCROLL = 33554432;
    public static int FLAG_HINTTEXTCOLOR = 8388608;
    public static int FLAG_IMAGECOLOR = 8;
    public static int FLAG_LINKCOLOR = 2;
    public static int FLAG_LISTGLOWCOLOR = 32768;
    public static int FLAG_PROGRESSBAR = 2048;
    public static int FLAG_SECTIONS = 524288;
    public static int FLAG_SELECTOR = 4096;
    public static int FLAG_SELECTORWHITE = 268435456;
    public static int FLAG_SERVICEBACKGROUND = 536870912;
    public static int FLAG_TEXTCOLOR = 4;
    public static int FLAG_USEBACKGROUNDDRAWABLE = 131072;
    private HashMap<String, Field> cachedFields;
    private int changeFlags;
    private int currentColor;
    private String currentKey;
    private int defaultColor;
    private ThemeDescriptionDelegate delegate;
    private Drawable[] drawablesToUpdate;
    private Class[] listClasses;
    private String[] listClassesFieldName;
    private String lottieLayerName;
    private HashMap<String, Boolean> notFoundCachedFields;
    private Paint[] paintToUpdate;
    private int previousColor;
    private boolean[] previousIsDefault;
    private View viewToInvalidate;
    
    public ThemeDescription(View viewToInvalidate, final int changeFlags, final Class[] listClasses, final Paint paint, final Drawable[] drawablesToUpdate, final ThemeDescriptionDelegate delegate, final String currentKey) {
        this.previousIsDefault = new boolean[1];
        this.currentKey = currentKey;
        if (paint != null) {
            this.paintToUpdate = new Paint[] { paint };
        }
        this.drawablesToUpdate = drawablesToUpdate;
        this.viewToInvalidate = viewToInvalidate;
        this.changeFlags = changeFlags;
        this.listClasses = listClasses;
        this.delegate = delegate;
        viewToInvalidate = this.viewToInvalidate;
        if (viewToInvalidate instanceof EditTextEmoji) {
            this.viewToInvalidate = (View)((EditTextEmoji)viewToInvalidate).getEditText();
        }
    }
    
    public ThemeDescription(View viewToInvalidate, final int changeFlags, final Class[] listClasses, final Paint[] paintToUpdate, final Drawable[] drawablesToUpdate, final ThemeDescriptionDelegate delegate, final String currentKey, final Object o) {
        this.previousIsDefault = new boolean[1];
        this.currentKey = currentKey;
        this.paintToUpdate = paintToUpdate;
        this.drawablesToUpdate = drawablesToUpdate;
        this.viewToInvalidate = viewToInvalidate;
        this.changeFlags = changeFlags;
        this.listClasses = listClasses;
        this.delegate = delegate;
        viewToInvalidate = this.viewToInvalidate;
        if (viewToInvalidate instanceof EditTextEmoji) {
            this.viewToInvalidate = (View)((EditTextEmoji)viewToInvalidate).getEditText();
        }
    }
    
    public ThemeDescription(View viewToInvalidate, final int changeFlags, final Class[] listClasses, final LottieDrawable[] drawablesToUpdate, final String lottieLayerName, final String currentKey) {
        this.previousIsDefault = new boolean[1];
        this.currentKey = currentKey;
        this.lottieLayerName = lottieLayerName;
        this.drawablesToUpdate = drawablesToUpdate;
        this.viewToInvalidate = viewToInvalidate;
        this.changeFlags = changeFlags;
        this.listClasses = listClasses;
        viewToInvalidate = this.viewToInvalidate;
        if (viewToInvalidate instanceof EditTextEmoji) {
            this.viewToInvalidate = (View)((EditTextEmoji)viewToInvalidate).getEditText();
        }
    }
    
    public ThemeDescription(View viewToInvalidate, final int changeFlags, final Class[] listClasses, final String[] listClassesFieldName, final String lottieLayerName, final String currentKey) {
        this.previousIsDefault = new boolean[1];
        this.currentKey = currentKey;
        this.lottieLayerName = lottieLayerName;
        this.viewToInvalidate = viewToInvalidate;
        this.changeFlags = changeFlags;
        this.listClasses = listClasses;
        this.listClassesFieldName = listClassesFieldName;
        this.cachedFields = new HashMap<String, Field>();
        this.notFoundCachedFields = new HashMap<String, Boolean>();
        viewToInvalidate = this.viewToInvalidate;
        if (viewToInvalidate instanceof EditTextEmoji) {
            this.viewToInvalidate = (View)((EditTextEmoji)viewToInvalidate).getEditText();
        }
    }
    
    public ThemeDescription(View viewToInvalidate, final int changeFlags, final Class[] listClasses, final String[] listClassesFieldName, final Paint[] paintToUpdate, final Drawable[] drawablesToUpdate, final ThemeDescriptionDelegate delegate, final String currentKey) {
        this.previousIsDefault = new boolean[1];
        this.currentKey = currentKey;
        this.paintToUpdate = paintToUpdate;
        this.drawablesToUpdate = drawablesToUpdate;
        this.viewToInvalidate = viewToInvalidate;
        this.changeFlags = changeFlags;
        this.listClasses = listClasses;
        this.listClassesFieldName = listClassesFieldName;
        this.delegate = delegate;
        this.cachedFields = new HashMap<String, Field>();
        this.notFoundCachedFields = new HashMap<String, Boolean>();
        viewToInvalidate = this.viewToInvalidate;
        if (viewToInvalidate instanceof EditTextEmoji) {
            this.viewToInvalidate = (View)((EditTextEmoji)viewToInvalidate).getEditText();
        }
    }
    
    private boolean checkTag(final String s, final View view) {
        if (s != null) {
            if (view != null) {
                final Object tag = view.getTag();
                if (tag instanceof String) {
                    return ((String)tag).contains(s);
                }
            }
        }
        return false;
    }
    
    private void processViewColor(final View view, final int n) {
        int n2 = 0;
        while (true) {
            final Class[] listClasses = this.listClasses;
            if (n2 >= listClasses.length) {
                break;
            }
            if (listClasses[n2].isInstance(view)) {
                view.invalidate();
                boolean b;
                if ((this.changeFlags & ThemeDescription.FLAG_CHECKTAG) != 0x0 && !this.checkTag(this.currentKey, view)) {
                    b = false;
                }
                else {
                    view.invalidate();
                    final int changeFlags = this.changeFlags;
                    if ((ThemeDescription.FLAG_BACKGROUNDFILTER & changeFlags) != 0x0) {
                        final Drawable background = view.getBackground();
                        if (background != null) {
                            if ((this.changeFlags & ThemeDescription.FLAG_CELLBACKGROUNDCOLOR) != 0x0) {
                                if (background instanceof CombinedDrawable) {
                                    final Drawable background2 = ((CombinedDrawable)background).getBackground();
                                    if (background2 instanceof ColorDrawable) {
                                        ((ColorDrawable)background2).setColor(n);
                                    }
                                }
                            }
                            else {
                                Drawable icon = null;
                                Label_0226: {
                                    if (background instanceof CombinedDrawable) {
                                        icon = ((CombinedDrawable)background).getIcon();
                                    }
                                    else {
                                        if (!(background instanceof StateListDrawable)) {
                                            icon = background;
                                            if (Build$VERSION.SDK_INT < 21) {
                                                break Label_0226;
                                            }
                                            icon = background;
                                            if (!(background instanceof RippleDrawable)) {
                                                break Label_0226;
                                            }
                                        }
                                        Theme.setSelectorDrawableColor(background, n, (this.changeFlags & ThemeDescription.FLAG_DRAWABLESELECTEDSTATE) != 0x0);
                                        icon = background;
                                    }
                                }
                                icon.setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
                            }
                        }
                    }
                    else if ((ThemeDescription.FLAG_CELLBACKGROUNDCOLOR & changeFlags) != 0x0) {
                        view.setBackgroundColor(n);
                    }
                    else if ((ThemeDescription.FLAG_TEXTCOLOR & changeFlags) != 0x0) {
                        if (view instanceof TextView) {
                            ((TextView)view).setTextColor(n);
                        }
                    }
                    else if ((ThemeDescription.FLAG_SERVICEBACKGROUND & changeFlags) != 0x0) {
                        final Drawable background3 = view.getBackground();
                        if (background3 != null) {
                            background3.setColorFilter((ColorFilter)Theme.colorFilter);
                        }
                    }
                    else if ((ThemeDescription.FLAG_SELECTOR & changeFlags) != 0x0) {
                        view.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                    }
                    else if ((changeFlags & ThemeDescription.FLAG_SELECTORWHITE) != 0x0) {
                        view.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                    }
                    b = true;
                }
                if (this.listClassesFieldName != null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(this.listClasses[n2]);
                    sb.append("_");
                    sb.append(this.listClassesFieldName[n2]);
                    final String string = sb.toString();
                    final HashMap<String, Boolean> notFoundCachedFields = this.notFoundCachedFields;
                    if (notFoundCachedFields == null || !notFoundCachedFields.containsKey(string)) {
                        try {
                            Field field;
                            if ((field = this.cachedFields.get(string)) == null) {
                                final Field declaredField = this.listClasses[n2].getDeclaredField(this.listClassesFieldName[n2]);
                                if ((field = declaredField) != null) {
                                    declaredField.setAccessible(true);
                                    this.cachedFields.put(string, declaredField);
                                    field = declaredField;
                                }
                            }
                            if (field != null) {
                                final Object value = field.get(view);
                                if (value != null) {
                                    if (b || !(value instanceof View) || this.checkTag(this.currentKey, (View)value)) {
                                        if (value instanceof View) {
                                            ((SimpleTextView)value).invalidate();
                                        }
                                        if (this.lottieLayerName != null && value instanceof LottieAnimationView) {
                                            ((LottieAnimationView)value).addValueCallback(new KeyPath(new String[] { this.lottieLayerName, "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(n)));
                                        }
                                        Object background4 = value;
                                        if ((this.changeFlags & ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE) != 0x0) {
                                            background4 = value;
                                            if (value instanceof View) {
                                                background4 = ((SimpleTextView)value).getBackground();
                                            }
                                        }
                                        if ((this.changeFlags & ThemeDescription.FLAG_BACKGROUND) != 0x0 && background4 instanceof View) {
                                            final View view2 = (View)background4;
                                            final Drawable background5 = view2.getBackground();
                                            if (background5 instanceof MessageBackgroundDrawable) {
                                                ((MessageBackgroundDrawable)background5).setColor(n);
                                            }
                                            else {
                                                view2.setBackgroundColor(n);
                                            }
                                        }
                                        else if (background4 instanceof EditTextCaption) {
                                            if ((this.changeFlags & ThemeDescription.FLAG_HINTTEXTCOLOR) != 0x0) {
                                                ((EditTextCaption)background4).setHintColor(n);
                                                ((EditTextCaption)background4).setHintTextColor(n);
                                            }
                                            else {
                                                ((EditTextCaption)background4).setTextColor(n);
                                            }
                                        }
                                        else if (background4 instanceof SimpleTextView) {
                                            if ((this.changeFlags & ThemeDescription.FLAG_LINKCOLOR) != 0x0) {
                                                ((SimpleTextView)background4).setLinkTextColor(n);
                                            }
                                            else {
                                                ((SimpleTextView)background4).setTextColor(n);
                                            }
                                        }
                                        else if (background4 instanceof TextView) {
                                            final TextView textView = (TextView)background4;
                                            if ((this.changeFlags & ThemeDescription.FLAG_IMAGECOLOR) != 0x0) {
                                                final Drawable[] compoundDrawables = textView.getCompoundDrawables();
                                                if (compoundDrawables != null) {
                                                    for (int i = 0; i < compoundDrawables.length; ++i) {
                                                        if (compoundDrawables[i] != null) {
                                                            compoundDrawables[i].setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
                                                        }
                                                    }
                                                }
                                            }
                                            else if ((this.changeFlags & ThemeDescription.FLAG_LINKCOLOR) != 0x0) {
                                                textView.getPaint().linkColor = n;
                                                textView.invalidate();
                                            }
                                            else if ((this.changeFlags & ThemeDescription.FLAG_FASTSCROLL) != 0x0) {
                                                final CharSequence text = textView.getText();
                                                if (text instanceof SpannedString) {
                                                    final TypefaceSpan[] array = (TypefaceSpan[])((SpannedString)text).getSpans(0, text.length(), (Class)TypefaceSpan.class);
                                                    if (array != null && array.length > 0) {
                                                        for (int j = 0; j < array.length; ++j) {
                                                            array[j].setColor(n);
                                                        }
                                                    }
                                                }
                                            }
                                            else {
                                                textView.setTextColor(n);
                                            }
                                        }
                                        else if (background4 instanceof ImageView) {
                                            ((ImageView)background4).setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
                                        }
                                        else if (background4 instanceof BackupImageView) {
                                            final Drawable staticThumb = ((BackupImageView)background4).getImageReceiver().getStaticThumb();
                                            if (staticThumb instanceof CombinedDrawable) {
                                                if ((this.changeFlags & ThemeDescription.FLAG_BACKGROUNDFILTER) != 0x0) {
                                                    ((CombinedDrawable)staticThumb).getBackground().setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
                                                }
                                                else {
                                                    ((CombinedDrawable)staticThumb).getIcon().setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
                                                }
                                            }
                                            else if (staticThumb != null) {
                                                staticThumb.setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
                                            }
                                        }
                                        else if (background4 instanceof Drawable) {
                                            if (background4 instanceof LetterDrawable) {
                                                if ((this.changeFlags & ThemeDescription.FLAG_BACKGROUNDFILTER) != 0x0) {
                                                    ((LetterDrawable)background4).setBackgroundColor(n);
                                                }
                                                else {
                                                    ((LetterDrawable)background4).setColor(n);
                                                }
                                            }
                                            else if (background4 instanceof CombinedDrawable) {
                                                if ((this.changeFlags & ThemeDescription.FLAG_BACKGROUNDFILTER) != 0x0) {
                                                    ((CombinedDrawable)background4).getBackground().setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
                                                }
                                                else {
                                                    ((CombinedDrawable)background4).getIcon().setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
                                                }
                                            }
                                            else if (!(background4 instanceof StateListDrawable) && (Build$VERSION.SDK_INT < 21 || !(background4 instanceof RippleDrawable))) {
                                                if (background4 instanceof GradientDrawable) {
                                                    ((GradientDrawable)background4).setColor(n);
                                                }
                                                else {
                                                    ((CombinedDrawable)background4).setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
                                                }
                                            }
                                            else {
                                                Theme.setSelectorDrawableColor((Drawable)background4, n, (this.changeFlags & ThemeDescription.FLAG_DRAWABLESELECTEDSTATE) != 0x0);
                                            }
                                        }
                                        else if (background4 instanceof CheckBox) {
                                            if ((this.changeFlags & ThemeDescription.FLAG_CHECKBOX) != 0x0) {
                                                ((CheckBox)background4).setBackgroundColor(n);
                                            }
                                            else if ((this.changeFlags & ThemeDescription.FLAG_CHECKBOXCHECK) != 0x0) {
                                                ((CheckBox)background4).setCheckColor(n);
                                            }
                                        }
                                        else if (background4 instanceof GroupCreateCheckBox) {
                                            ((GroupCreateCheckBox)background4).updateColors();
                                        }
                                        else if (background4 instanceof Integer) {
                                            field.set(view, n);
                                        }
                                        else if (background4 instanceof RadioButton) {
                                            if ((this.changeFlags & ThemeDescription.FLAG_CHECKBOX) != 0x0) {
                                                ((RadioButton)background4).setBackgroundColor(n);
                                                ((RadioButton)background4).invalidate();
                                            }
                                            else if ((this.changeFlags & ThemeDescription.FLAG_CHECKBOXCHECK) != 0x0) {
                                                ((RadioButton)background4).setCheckedColor(n);
                                                ((RadioButton)background4).invalidate();
                                            }
                                        }
                                        else if (background4 instanceof TextPaint) {
                                            if ((this.changeFlags & ThemeDescription.FLAG_LINKCOLOR) != 0x0) {
                                                ((TextPaint)background4).linkColor = n;
                                            }
                                            else {
                                                ((TextPaint)background4).setColor(n);
                                            }
                                        }
                                        else if (background4 instanceof LineProgressView) {
                                            if ((this.changeFlags & ThemeDescription.FLAG_PROGRESSBAR) != 0x0) {
                                                ((LineProgressView)background4).setProgressColor(n);
                                            }
                                            else {
                                                ((LineProgressView)background4).setBackColor(n);
                                            }
                                        }
                                        else if (background4 instanceof Paint) {
                                            ((Paint)background4).setColor(n);
                                        }
                                        else if (background4 instanceof SeekBarView) {
                                            if ((this.changeFlags & ThemeDescription.FLAG_PROGRESSBAR) != 0x0) {
                                                ((SeekBarView)background4).setOuterColor(n);
                                            }
                                            else {
                                                ((SeekBarView)background4).setInnerColor(n);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        catch (Throwable t) {
                            FileLog.e(t);
                            this.notFoundCachedFields.put(string, true);
                        }
                    }
                }
                else if (view instanceof GroupCreateSpan) {
                    ((GroupCreateSpan)view).updateColors();
                }
            }
            ++n2;
        }
    }
    
    public int getCurrentColor() {
        return this.currentColor;
    }
    
    public String getCurrentKey() {
        return this.currentKey;
    }
    
    public int getSetColor() {
        return Theme.getColor(this.currentKey);
    }
    
    public String getTitle() {
        return this.currentKey;
    }
    
    public void setColor(final int n, final boolean b) {
        this.setColor(n, b, true);
    }
    
    public void setColor(final int n, final boolean b, final boolean b2) {
        if (b2) {
            Theme.setColor(this.currentKey, n, b);
        }
        final Paint[] paintToUpdate = this.paintToUpdate;
        final int n2 = 0;
        if (paintToUpdate != null) {
            int n3 = 0;
            while (true) {
                final Paint[] paintToUpdate2 = this.paintToUpdate;
                if (n3 >= paintToUpdate2.length) {
                    break;
                }
                if ((this.changeFlags & ThemeDescription.FLAG_LINKCOLOR) != 0x0 && paintToUpdate2[n3] instanceof TextPaint) {
                    ((TextPaint)paintToUpdate2[n3]).linkColor = n;
                }
                else {
                    this.paintToUpdate[n3].setColor(n);
                }
                ++n3;
            }
        }
        if (this.drawablesToUpdate != null) {
            int n4 = 0;
            while (true) {
                final Drawable[] drawablesToUpdate = this.drawablesToUpdate;
                if (n4 >= drawablesToUpdate.length) {
                    break;
                }
                if (drawablesToUpdate[n4] != null) {
                    if (drawablesToUpdate[n4] instanceof ScamDrawable) {
                        ((ScamDrawable)drawablesToUpdate[n4]).setColor(n);
                    }
                    else if (drawablesToUpdate[n4] instanceof LottieDrawable) {
                        final String lottieLayerName = this.lottieLayerName;
                        if (lottieLayerName != null) {
                            ((LottieDrawable)drawablesToUpdate[n4]).addValueCallback(new KeyPath(new String[] { lottieLayerName, "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(n)));
                        }
                    }
                    else if (drawablesToUpdate[n4] instanceof CombinedDrawable) {
                        if ((this.changeFlags & ThemeDescription.FLAG_BACKGROUNDFILTER) != 0x0) {
                            ((CombinedDrawable)drawablesToUpdate[n4]).getBackground().setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
                        }
                        else {
                            ((CombinedDrawable)drawablesToUpdate[n4]).getIcon().setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
                        }
                    }
                    else if (drawablesToUpdate[n4] instanceof AvatarDrawable) {
                        ((AvatarDrawable)drawablesToUpdate[n4]).setColor(n);
                    }
                    else {
                        drawablesToUpdate[n4].setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
                    }
                }
                ++n4;
            }
        }
        final View viewToInvalidate = this.viewToInvalidate;
        if (viewToInvalidate != null && this.listClasses == null && this.listClassesFieldName == null && ((this.changeFlags & ThemeDescription.FLAG_CHECKTAG) == 0x0 || this.checkTag(this.currentKey, viewToInvalidate))) {
            if ((this.changeFlags & ThemeDescription.FLAG_BACKGROUND) != 0x0) {
                final Drawable background = this.viewToInvalidate.getBackground();
                if (background instanceof MessageBackgroundDrawable) {
                    ((MessageBackgroundDrawable)background).setColor(n);
                }
                else {
                    this.viewToInvalidate.setBackgroundColor(n);
                }
            }
            final int changeFlags = this.changeFlags;
            if ((ThemeDescription.FLAG_BACKGROUNDFILTER & changeFlags) != 0x0) {
                if ((changeFlags & ThemeDescription.FLAG_PROGRESSBAR) != 0x0) {
                    final View viewToInvalidate2 = this.viewToInvalidate;
                    if (viewToInvalidate2 instanceof EditTextBoldCursor) {
                        ((EditTextBoldCursor)viewToInvalidate2).setErrorLineColor(n);
                    }
                }
                else {
                    Drawable drawable2;
                    final Drawable drawable = drawable2 = this.viewToInvalidate.getBackground();
                    if (drawable instanceof CombinedDrawable) {
                        if ((this.changeFlags & ThemeDescription.FLAG_DRAWABLESELECTEDSTATE) != 0x0) {
                            drawable2 = ((CombinedDrawable)drawable).getBackground();
                        }
                        else {
                            drawable2 = ((CombinedDrawable)drawable).getIcon();
                        }
                    }
                    if (drawable2 != null) {
                        if (!(drawable2 instanceof StateListDrawable) && (Build$VERSION.SDK_INT < 21 || !(drawable2 instanceof RippleDrawable))) {
                            if (drawable2 instanceof ShapeDrawable) {
                                ((ShapeDrawable)drawable2).getPaint().setColor(n);
                            }
                            else {
                                drawable2.setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
                            }
                        }
                        else {
                            Theme.setSelectorDrawableColor(drawable2, n, (this.changeFlags & ThemeDescription.FLAG_DRAWABLESELECTEDSTATE) != 0x0);
                        }
                    }
                }
            }
        }
        final View viewToInvalidate3 = this.viewToInvalidate;
        if (viewToInvalidate3 instanceof ActionBar) {
            if ((this.changeFlags & ThemeDescription.FLAG_AB_ITEMSCOLOR) != 0x0) {
                ((ActionBar)viewToInvalidate3).setItemsColor(n, false);
            }
            if ((this.changeFlags & ThemeDescription.FLAG_AB_TITLECOLOR) != 0x0) {
                ((ActionBar)this.viewToInvalidate).setTitleColor(n);
            }
            if ((this.changeFlags & ThemeDescription.FLAG_AB_SELECTORCOLOR) != 0x0) {
                ((ActionBar)this.viewToInvalidate).setItemsBackgroundColor(n, false);
            }
            if ((this.changeFlags & ThemeDescription.FLAG_AB_AM_SELECTORCOLOR) != 0x0) {
                ((ActionBar)this.viewToInvalidate).setItemsBackgroundColor(n, true);
            }
            if ((this.changeFlags & ThemeDescription.FLAG_AB_AM_ITEMSCOLOR) != 0x0) {
                ((ActionBar)this.viewToInvalidate).setItemsColor(n, true);
            }
            if ((this.changeFlags & ThemeDescription.FLAG_AB_SUBTITLECOLOR) != 0x0) {
                ((ActionBar)this.viewToInvalidate).setSubtitleColor(n);
            }
            if ((this.changeFlags & ThemeDescription.FLAG_AB_AM_BACKGROUND) != 0x0) {
                ((ActionBar)this.viewToInvalidate).setActionModeColor(n);
            }
            if ((this.changeFlags & ThemeDescription.FLAG_AB_AM_TOPBACKGROUND) != 0x0) {
                ((ActionBar)this.viewToInvalidate).setActionModeTopColor(n);
            }
            if ((this.changeFlags & ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER) != 0x0) {
                ((ActionBar)this.viewToInvalidate).setSearchTextColor(n, true);
            }
            if ((this.changeFlags & ThemeDescription.FLAG_AB_SEARCH) != 0x0) {
                ((ActionBar)this.viewToInvalidate).setSearchTextColor(n, false);
            }
            final int changeFlags2 = this.changeFlags;
            if ((ThemeDescription.FLAG_AB_SUBMENUITEM & changeFlags2) != 0x0) {
                ((ActionBar)this.viewToInvalidate).setPopupItemsColor(n, (changeFlags2 & ThemeDescription.FLAG_IMAGECOLOR) != 0x0);
            }
            if ((this.changeFlags & ThemeDescription.FLAG_AB_SUBMENUBACKGROUND) != 0x0) {
                ((ActionBar)this.viewToInvalidate).setPopupBackgroundColor(n);
            }
        }
        final View viewToInvalidate4 = this.viewToInvalidate;
        if (viewToInvalidate4 instanceof EmptyTextProgressView) {
            final int changeFlags3 = this.changeFlags;
            if ((ThemeDescription.FLAG_TEXTCOLOR & changeFlags3) != 0x0) {
                ((EmptyTextProgressView)viewToInvalidate4).setTextColor(n);
            }
            else if ((changeFlags3 & ThemeDescription.FLAG_PROGRESSBAR) != 0x0) {
                ((EmptyTextProgressView)viewToInvalidate4).setProgressBarColor(n);
            }
        }
        final View viewToInvalidate5 = this.viewToInvalidate;
        if (viewToInvalidate5 instanceof RadialProgressView) {
            ((RadialProgressView)viewToInvalidate5).setProgressColor(n);
        }
        else if (viewToInvalidate5 instanceof LineProgressView) {
            if ((this.changeFlags & ThemeDescription.FLAG_PROGRESSBAR) != 0x0) {
                ((LineProgressView)viewToInvalidate5).setProgressColor(n);
            }
            else {
                ((LineProgressView)viewToInvalidate5).setBackColor(n);
            }
        }
        else if (viewToInvalidate5 instanceof ContextProgressView) {
            ((ContextProgressView)viewToInvalidate5).updateColors();
        }
        final int changeFlags4 = this.changeFlags;
        if ((ThemeDescription.FLAG_TEXTCOLOR & changeFlags4) != 0x0 && ((changeFlags4 & ThemeDescription.FLAG_CHECKTAG) == 0x0 || this.checkTag(this.currentKey, this.viewToInvalidate))) {
            final View viewToInvalidate6 = this.viewToInvalidate;
            if (viewToInvalidate6 instanceof TextView) {
                ((TextView)viewToInvalidate6).setTextColor(n);
            }
            else if (viewToInvalidate6 instanceof NumberTextView) {
                ((NumberTextView)viewToInvalidate6).setTextColor(n);
            }
            else if (viewToInvalidate6 instanceof SimpleTextView) {
                ((SimpleTextView)viewToInvalidate6).setTextColor(n);
            }
            else if (viewToInvalidate6 instanceof ChatBigEmptyView) {
                ((ChatBigEmptyView)viewToInvalidate6).setTextColor(n);
            }
        }
        if ((this.changeFlags & ThemeDescription.FLAG_CURSORCOLOR) != 0x0) {
            final View viewToInvalidate7 = this.viewToInvalidate;
            if (viewToInvalidate7 instanceof EditTextBoldCursor) {
                ((EditTextBoldCursor)viewToInvalidate7).setCursorColor(n);
            }
        }
        final int changeFlags5 = this.changeFlags;
        if ((ThemeDescription.FLAG_HINTTEXTCOLOR & changeFlags5) != 0x0) {
            final View viewToInvalidate8 = this.viewToInvalidate;
            if (viewToInvalidate8 instanceof EditTextBoldCursor) {
                if ((changeFlags5 & ThemeDescription.FLAG_PROGRESSBAR) != 0x0) {
                    ((EditTextBoldCursor)viewToInvalidate8).setHeaderHintColor(n);
                }
                else {
                    ((EditTextBoldCursor)viewToInvalidate8).setHintColor(n);
                }
            }
            else if (viewToInvalidate8 instanceof EditText) {
                ((EditTextBoldCursor)viewToInvalidate8).setHintTextColor(n);
            }
        }
        final View viewToInvalidate9 = this.viewToInvalidate;
        if (viewToInvalidate9 != null && (this.changeFlags & ThemeDescription.FLAG_SERVICEBACKGROUND) != 0x0) {
            final Drawable background2 = viewToInvalidate9.getBackground();
            if (background2 != null) {
                background2.setColorFilter((ColorFilter)Theme.colorFilter);
            }
        }
        final int changeFlags6 = this.changeFlags;
        if ((ThemeDescription.FLAG_IMAGECOLOR & changeFlags6) != 0x0 && ((changeFlags6 & ThemeDescription.FLAG_CHECKTAG) == 0x0 || this.checkTag(this.currentKey, this.viewToInvalidate))) {
            final View viewToInvalidate10 = this.viewToInvalidate;
            if (viewToInvalidate10 instanceof ImageView) {
                if ((this.changeFlags & ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE) != 0x0) {
                    final Drawable drawable3 = ((ImageView)viewToInvalidate10).getDrawable();
                    if (drawable3 instanceof StateListDrawable || (Build$VERSION.SDK_INT >= 21 && drawable3 instanceof RippleDrawable)) {
                        Theme.setSelectorDrawableColor(drawable3, n, (this.changeFlags & ThemeDescription.FLAG_DRAWABLESELECTEDSTATE) != 0x0);
                    }
                }
                else {
                    ((ImageView)viewToInvalidate10).setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
                }
            }
            else if (!(viewToInvalidate10 instanceof BackupImageView)) {
                if (viewToInvalidate10 instanceof SimpleTextView) {
                    ((SimpleTextView)viewToInvalidate10).setSideDrawablesColor(n);
                }
            }
        }
        final View viewToInvalidate11 = this.viewToInvalidate;
        if (viewToInvalidate11 instanceof ScrollView && (this.changeFlags & ThemeDescription.FLAG_LISTGLOWCOLOR) != 0x0) {
            AndroidUtilities.setScrollViewEdgeEffectColor((ScrollView)viewToInvalidate11, n);
        }
        final View viewToInvalidate12 = this.viewToInvalidate;
        if (viewToInvalidate12 instanceof ViewPager && (this.changeFlags & ThemeDescription.FLAG_LISTGLOWCOLOR) != 0x0) {
            AndroidUtilities.setViewPagerEdgeEffectColor((ViewPager)viewToInvalidate12, n);
        }
        final View viewToInvalidate13 = this.viewToInvalidate;
        if (viewToInvalidate13 instanceof RecyclerListView) {
            final RecyclerListView recyclerListView = (RecyclerListView)viewToInvalidate13;
            if ((this.changeFlags & ThemeDescription.FLAG_SELECTOR) != 0x0 && this.currentKey.equals("listSelectorSDK21")) {
                recyclerListView.setListSelectorColor(n);
            }
            if ((this.changeFlags & ThemeDescription.FLAG_FASTSCROLL) != 0x0) {
                recyclerListView.updateFastScrollColors();
            }
            if ((this.changeFlags & ThemeDescription.FLAG_LISTGLOWCOLOR) != 0x0) {
                recyclerListView.setGlowColor(n);
            }
            if ((this.changeFlags & ThemeDescription.FLAG_SECTIONS) != 0x0) {
                final ArrayList<View> headers = recyclerListView.getHeaders();
                if (headers != null) {
                    for (int i = 0; i < headers.size(); ++i) {
                        this.processViewColor(headers.get(i), n);
                    }
                }
                final ArrayList<View> headersCache = recyclerListView.getHeadersCache();
                if (headersCache != null) {
                    for (int j = 0; j < headersCache.size(); ++j) {
                        this.processViewColor(headersCache.get(j), n);
                    }
                }
                final View pinnedHeader = recyclerListView.getPinnedHeader();
                if (pinnedHeader != null) {
                    this.processViewColor(pinnedHeader, n);
                }
            }
        }
        else if (viewToInvalidate13 != null) {
            final Class[] listClasses = this.listClasses;
            if (listClasses == null || listClasses.length == 0) {
                final int changeFlags7 = this.changeFlags;
                if ((ThemeDescription.FLAG_SELECTOR & changeFlags7) != 0x0) {
                    this.viewToInvalidate.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                }
                else if ((changeFlags7 & ThemeDescription.FLAG_SELECTORWHITE) != 0x0) {
                    this.viewToInvalidate.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                }
            }
        }
        if (this.listClasses != null) {
            final View viewToInvalidate14 = this.viewToInvalidate;
            if (viewToInvalidate14 instanceof RecyclerListView) {
                final RecyclerListView recyclerListView2 = (RecyclerListView)viewToInvalidate14;
                recyclerListView2.getRecycledViewPool().clear();
                for (int hiddenChildCount = recyclerListView2.getHiddenChildCount(), k = 0; k < hiddenChildCount; ++k) {
                    this.processViewColor(recyclerListView2.getHiddenChildAt(k), n);
                }
                for (int cachedChildCount = recyclerListView2.getCachedChildCount(), l = 0; l < cachedChildCount; ++l) {
                    this.processViewColor(recyclerListView2.getCachedChildAt(l), n);
                }
                for (int attachedScrapChildCount = recyclerListView2.getAttachedScrapChildCount(), n5 = 0; n5 < attachedScrapChildCount; ++n5) {
                    this.processViewColor(recyclerListView2.getAttachedScrapChildAt(n5), n);
                }
            }
            final View viewToInvalidate15 = this.viewToInvalidate;
            if (viewToInvalidate15 instanceof ViewGroup) {
                final ViewGroup viewGroup = (ViewGroup)viewToInvalidate15;
                for (int childCount = viewGroup.getChildCount(), n6 = n2; n6 < childCount; ++n6) {
                    this.processViewColor(viewGroup.getChildAt(n6), n);
                }
            }
            this.processViewColor(this.viewToInvalidate, n);
        }
        this.currentColor = n;
        final ThemeDescriptionDelegate delegate = this.delegate;
        if (delegate != null) {
            delegate.didSetColor();
        }
        final View viewToInvalidate16 = this.viewToInvalidate;
        if (viewToInvalidate16 != null) {
            viewToInvalidate16.invalidate();
        }
    }
    
    public void setDefaultColor() {
        this.setColor(Theme.getDefaultColor(this.currentKey), true);
    }
    
    public ThemeDescriptionDelegate setDelegateDisabled() {
        final ThemeDescriptionDelegate delegate = this.delegate;
        this.delegate = null;
        return delegate;
    }
    
    public void setPreviousColor() {
        this.setColor(this.previousColor, this.previousIsDefault[0]);
    }
    
    public void startEditing() {
        final int color = Theme.getColor(this.currentKey, this.previousIsDefault);
        this.previousColor = color;
        this.currentColor = color;
    }
    
    public interface ThemeDescriptionDelegate
    {
        void didSetColor();
    }
}
