package org.telegram.p004ui.Components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.text.Editable;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.CharacterStyle;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.FileLog;

/* renamed from: org.telegram.ui.Components.EditTextCaption */
public class EditTextCaption extends EditTextBoldCursor {
    private String caption;
    private StaticLayout captionLayout;
    private boolean copyPasteShowed;
    private EditTextCaptionDelegate delegate;
    private int hintColor;
    private int selectionEnd = -1;
    private int selectionStart = -1;
    private int triesCount = 0;
    private int userNameLength;
    private int xOffset;
    private int yOffset;

    /* renamed from: org.telegram.ui.Components.EditTextCaption$EditTextCaptionDelegate */
    public interface EditTextCaptionDelegate {
        void onSpansChanged();
    }

    public EditTextCaption(Context context) {
        super(context);
    }

    public void setCaption(String str) {
        String str2 = this.caption;
        if ((str2 != null && str2.length() != 0) || (str != null && str.length() != 0)) {
            str2 = this.caption;
            if (str2 == null || str == null || !str2.equals(str)) {
                this.caption = str;
                str = this.caption;
                if (str != null) {
                    this.caption = str.replace(10, ' ');
                }
                requestLayout();
            }
        }
    }

    public void setDelegate(EditTextCaptionDelegate editTextCaptionDelegate) {
        this.delegate = editTextCaptionDelegate;
    }

    public void makeSelectedBold() {
        applyTextStyleToSelection(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")));
    }

    public void makeSelectedItalic() {
        applyTextStyleToSelection(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/ritalic.ttf")));
    }

    public void makeSelectedMono() {
        applyTextStyleToSelection(new TypefaceSpan(Typeface.MONOSPACE));
    }

    /* JADX WARNING: Removed duplicated region for block: B:8:0x00be  */
    public void makeSelectedUrl() {
        /*
        r9 = this;
        r0 = new org.telegram.ui.ActionBar.AlertDialog$Builder;
        r1 = r9.getContext();
        r0.<init>(r1);
        r1 = "CreateLink";
        r2 = 2131559169; // 0x7f0d0301 float:1.8743674E38 double:1.0531301575E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
        r0.setTitle(r1);
        r1 = new org.telegram.ui.Components.EditTextCaption$1;
        r2 = r9.getContext();
        r1.<init>(r2);
        r2 = 1;
        r3 = 1099956224; // 0x41900000 float:18.0 double:5.43450582E-315;
        r1.setTextSize(r2, r3);
        r3 = "http://";
        r1.setText(r3);
        r3 = "dialogTextBlack";
        r3 = org.telegram.p004ui.ActionBar.Theme.getColor(r3);
        r1.setTextColor(r3);
        r3 = "URL";
        r4 = 2131560927; // 0x7f0d09df float:1.874724E38 double:1.053131026E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
        r1.setHintText(r3);
        r3 = "windowBackgroundWhiteBlueHeader";
        r3 = org.telegram.p004ui.ActionBar.Theme.getColor(r3);
        r1.setHeaderHintColor(r3);
        r1.setSingleLine(r2);
        r1.setFocusable(r2);
        r1.setTransformHintToHeader(r2);
        r3 = "windowBackgroundWhiteInputField";
        r3 = org.telegram.p004ui.ActionBar.Theme.getColor(r3);
        r4 = "windowBackgroundWhiteInputFieldActivated";
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r4);
        r5 = "windowBackgroundWhiteRedText3";
        r5 = org.telegram.p004ui.ActionBar.Theme.getColor(r5);
        r1.setLineColors(r3, r4, r5);
        r3 = 6;
        r1.setImeOptions(r3);
        r3 = 0;
        r1.setBackgroundDrawable(r3);
        r1.requestFocus();
        r4 = 0;
        r1.setPadding(r4, r4, r4, r4);
        r0.setView(r1);
        r5 = r9.selectionStart;
        if (r5 < 0) goto L_0x0085;
    L_0x007b:
        r6 = r9.selectionEnd;
        if (r6 < 0) goto L_0x0085;
    L_0x007f:
        r7 = -1;
        r9.selectionEnd = r7;
        r9.selectionStart = r7;
        goto L_0x008d;
    L_0x0085:
        r5 = r9.getSelectionStart();
        r6 = r9.getSelectionEnd();
    L_0x008d:
        r7 = 2131560097; // 0x7f0d06a1 float:1.8745557E38 double:1.053130616E-314;
        r8 = "OK";
        r7 = org.telegram.messenger.LocaleController.getString(r8, r7);
        r8 = new org.telegram.ui.Components.-$$Lambda$EditTextCaption$BQIhHIR0EWfMGyyXmJJ-pkFKO1Y;
        r8.<init>(r9, r5, r6, r1);
        r0.setPositiveButton(r7, r8);
        r5 = 2131558891; // 0x7f0d01eb float:1.874311E38 double:1.05313002E-314;
        r6 = "Cancel";
        r5 = org.telegram.messenger.LocaleController.getString(r6, r5);
        r0.setNegativeButton(r5, r3);
        r0 = r0.show();
        r3 = new org.telegram.ui.Components.-$$Lambda$EditTextCaption$8tXURyNItaU0tMIyLqohmCvoG40;
        r3.<init>(r1);
        r0.setOnShowListener(r3);
        r0 = r1.getLayoutParams();
        r0 = (android.view.ViewGroup.MarginLayoutParams) r0;
        if (r0 == 0) goto L_0x00dc;
    L_0x00be:
        r3 = r0 instanceof android.widget.FrameLayout.LayoutParams;
        if (r3 == 0) goto L_0x00c7;
    L_0x00c2:
        r3 = r0;
        r3 = (android.widget.FrameLayout.LayoutParams) r3;
        r3.gravity = r2;
    L_0x00c7:
        r2 = 1103101952; // 0x41c00000 float:24.0 double:5.450047783E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0.leftMargin = r2;
        r0.rightMargin = r2;
        r2 = 1108344832; // 0x42100000 float:36.0 double:5.47595105E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0.height = r2;
        r1.setLayoutParams(r0);
    L_0x00dc:
        r0 = r1.getText();
        r0 = r0.length();
        r1.setSelection(r4, r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Components.EditTextCaption.makeSelectedUrl():void");
    }

    public /* synthetic */ void lambda$makeSelectedUrl$0$EditTextCaption(int i, int i2, EditTextBoldCursor editTextBoldCursor, DialogInterface dialogInterface, int i3) {
        Editable text = getText();
        CharacterStyle[] characterStyleArr = (CharacterStyle[]) text.getSpans(i, i2, CharacterStyle.class);
        if (characterStyleArr != null && characterStyleArr.length > 0) {
            for (Object obj : characterStyleArr) {
                int spanStart = text.getSpanStart(obj);
                int spanEnd = text.getSpanEnd(obj);
                text.removeSpan(obj);
                if (spanStart < i) {
                    text.setSpan(obj, spanStart, i, 33);
                }
                if (spanEnd > i2) {
                    text.setSpan(obj, i2, spanEnd, 33);
                }
            }
        }
        try {
            text.setSpan(new URLSpanReplacement(editTextBoldCursor.getText().toString()), i, i2, 33);
        } catch (Exception unused) {
        }
        EditTextCaptionDelegate editTextCaptionDelegate = this.delegate;
        if (editTextCaptionDelegate != null) {
            editTextCaptionDelegate.onSpansChanged();
        }
    }

    static /* synthetic */ void lambda$makeSelectedUrl$1(EditTextBoldCursor editTextBoldCursor, DialogInterface dialogInterface) {
        editTextBoldCursor.requestFocus();
        AndroidUtilities.showKeyboard(editTextBoldCursor);
    }

    public void makeSelectedRegular() {
        applyTextStyleToSelection(null);
    }

    public void setSelectionOverride(int i, int i2) {
        this.selectionStart = i;
        this.selectionEnd = i2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x002d  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0049  */
    /* JADX WARNING: Removed duplicated region for block: B:28:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0050  */
    private void applyTextStyleToSelection(org.telegram.p004ui.Components.TypefaceSpan r10) {
        /*
        r9 = this;
        r0 = r9.selectionStart;
        if (r0 < 0) goto L_0x000e;
    L_0x0004:
        r1 = r9.selectionEnd;
        if (r1 < 0) goto L_0x000e;
    L_0x0008:
        r2 = -1;
        r9.selectionEnd = r2;
        r9.selectionStart = r2;
        goto L_0x0016;
    L_0x000e:
        r0 = r9.getSelectionStart();
        r1 = r9.getSelectionEnd();
    L_0x0016:
        r2 = r9.getText();
        r3 = android.text.style.CharacterStyle.class;
        r3 = r2.getSpans(r0, r1, r3);
        r3 = (android.text.style.CharacterStyle[]) r3;
        r4 = 33;
        if (r3 == 0) goto L_0x0047;
    L_0x0026:
        r5 = r3.length;
        if (r5 <= 0) goto L_0x0047;
    L_0x0029:
        r5 = 0;
    L_0x002a:
        r6 = r3.length;
        if (r5 >= r6) goto L_0x0047;
    L_0x002d:
        r6 = r3[r5];
        r7 = r2.getSpanStart(r6);
        r8 = r2.getSpanEnd(r6);
        r2.removeSpan(r6);
        if (r7 >= r0) goto L_0x003f;
    L_0x003c:
        r2.setSpan(r6, r7, r0, r4);
    L_0x003f:
        if (r8 <= r1) goto L_0x0044;
    L_0x0041:
        r2.setSpan(r6, r1, r8, r4);
    L_0x0044:
        r5 = r5 + 1;
        goto L_0x002a;
    L_0x0047:
        if (r10 == 0) goto L_0x004c;
    L_0x0049:
        r2.setSpan(r10, r0, r1, r4);
    L_0x004c:
        r10 = r9.delegate;
        if (r10 == 0) goto L_0x0053;
    L_0x0050:
        r10.onSpansChanged();
    L_0x0053:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Components.EditTextCaption.applyTextStyleToSelection(org.telegram.ui.Components.TypefaceSpan):void");
    }

    public void onWindowFocusChanged(boolean z) {
        if (VERSION.SDK_INT >= 23 || z || !this.copyPasteShowed) {
            super.onWindowFocusChanged(z);
        }
    }

    private Callback overrideCallback(final Callback callback) {
        return new Callback() {
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                EditTextCaption.this.copyPasteShowed = true;
                return callback.onCreateActionMode(actionMode, menu);
            }

            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return callback.onPrepareActionMode(actionMode, menu);
            }

            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                if (menuItem.getItemId() == C1067R.C1066id.menu_regular) {
                    EditTextCaption.this.makeSelectedRegular();
                    actionMode.finish();
                    return true;
                } else if (menuItem.getItemId() == C1067R.C1066id.menu_bold) {
                    EditTextCaption.this.makeSelectedBold();
                    actionMode.finish();
                    return true;
                } else if (menuItem.getItemId() == C1067R.C1066id.menu_italic) {
                    EditTextCaption.this.makeSelectedItalic();
                    actionMode.finish();
                    return true;
                } else if (menuItem.getItemId() == C1067R.C1066id.menu_mono) {
                    EditTextCaption.this.makeSelectedMono();
                    actionMode.finish();
                    return true;
                } else if (menuItem.getItemId() == C1067R.C1066id.menu_link) {
                    EditTextCaption.this.makeSelectedUrl();
                    actionMode.finish();
                    return true;
                } else {
                    try {
                        return callback.onActionItemClicked(actionMode, menuItem);
                    } catch (Exception unused) {
                        return true;
                    }
                }
            }

            public void onDestroyActionMode(ActionMode actionMode) {
                EditTextCaption.this.copyPasteShowed = false;
                callback.onDestroyActionMode(actionMode);
            }
        };
    }

    public ActionMode startActionMode(Callback callback, int i) {
        return super.startActionMode(overrideCallback(callback), i);
    }

    public ActionMode startActionMode(Callback callback) {
        return super.startActionMode(overrideCallback(callback));
    }

    /* Access modifiers changed, original: protected */
    @SuppressLint({"DrawAllocation"})
    public void onMeasure(int i, int i2) {
        try {
            super.onMeasure(i, i2);
        } catch (Exception e) {
            setMeasuredDimension(MeasureSpec.getSize(i), AndroidUtilities.m26dp(51.0f));
            FileLog.m30e(e);
        }
        this.captionLayout = null;
        String str = this.caption;
        if (str != null && str.length() > 0) {
            Editable text = getText();
            if (text.length() > 1 && text.charAt(0) == '@') {
                int indexOf = TextUtils.indexOf(text, ' ');
                if (indexOf != -1) {
                    TextPaint paint = getPaint();
                    indexOf++;
                    CharSequence subSequence = text.subSequence(0, indexOf);
                    i = (int) Math.ceil((double) paint.measureText(text, 0, indexOf));
                    indexOf = (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
                    this.userNameLength = subSequence.length();
                    int i3 = indexOf - i;
                    CharSequence ellipsize = TextUtils.ellipsize(this.caption, paint, (float) i3, TruncateAt.END);
                    this.xOffset = i;
                    try {
                        this.captionLayout = new StaticLayout(ellipsize, getPaint(), i3, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                        if (this.captionLayout.getLineCount() > 0) {
                            this.xOffset = (int) (((float) this.xOffset) + (-this.captionLayout.getLineLeft(0)));
                        }
                        this.yOffset = ((getMeasuredHeight() - this.captionLayout.getLineBottom(0)) / 2) + AndroidUtilities.m26dp(0.5f);
                    } catch (Exception e2) {
                        FileLog.m30e(e2);
                    }
                }
            }
        }
    }

    public String getCaption() {
        return this.caption;
    }

    public void setHintColor(int i) {
        super.setHintColor(i);
        this.hintColor = i;
        invalidate();
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            if (this.captionLayout != null && this.userNameLength == length()) {
                TextPaint paint = getPaint();
                int color = getPaint().getColor();
                paint.setColor(this.hintColor);
                canvas.save();
                canvas.translate((float) this.xOffset, (float) this.yOffset);
                this.captionLayout.draw(canvas);
                canvas.restore();
                paint.setColor(color);
            }
        } catch (Exception e) {
            FileLog.m30e(e);
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (!TextUtils.isEmpty(this.caption)) {
            if (VERSION.SDK_INT >= 26) {
                accessibilityNodeInfo.setHintText(this.caption);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(accessibilityNodeInfo.getText());
            stringBuilder.append(", ");
            stringBuilder.append(this.caption);
            accessibilityNodeInfo.setText(stringBuilder.toString());
        }
    }
}