// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint;

import android.view.ViewGroup$LayoutParams;
import android.graphics.Canvas;
import android.content.res.TypedArray;
import java.util.Arrays;
import android.util.Log;
import android.util.AttributeSet;
import android.content.Context;
import android.support.constraint.solver.widgets.Helper;
import android.view.View;

public abstract class ConstraintHelper extends View
{
    protected int mCount;
    protected Helper mHelperWidget;
    protected int[] mIds;
    private String mReferenceIds;
    protected boolean mUseViewMeasure;
    protected Context myContext;
    
    public ConstraintHelper(final Context myContext) {
        super(myContext);
        this.mIds = new int[32];
        this.mCount = 0;
        this.mHelperWidget = null;
        this.mUseViewMeasure = false;
        this.myContext = myContext;
        this.init(null);
    }
    
    private void addID(String trim) {
        if (trim == null) {
            return;
        }
        if (this.myContext == null) {
            return;
        }
        trim = trim.trim();
        int int1;
        try {
            int1 = R.id.class.getField(trim).getInt(null);
        }
        catch (Exception ex) {
            int1 = 0;
        }
        int identifier = int1;
        if (int1 == 0) {
            identifier = this.myContext.getResources().getIdentifier(trim, "id", this.myContext.getPackageName());
        }
        int intValue;
        if ((intValue = identifier) == 0) {
            intValue = identifier;
            if (this.isInEditMode()) {
                intValue = identifier;
                if (this.getParent() instanceof ConstraintLayout) {
                    final Object designInformation = ((ConstraintLayout)this.getParent()).getDesignInformation(0, trim);
                    intValue = identifier;
                    if (designInformation != null) {
                        intValue = identifier;
                        if (designInformation instanceof Integer) {
                            intValue = (int)designInformation;
                        }
                    }
                }
            }
        }
        if (intValue != 0) {
            this.setTag(intValue, null);
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not find id of \"");
            sb.append(trim);
            sb.append("\"");
            Log.w("ConstraintHelper", sb.toString());
        }
    }
    
    private void setIds(final String s) {
        if (s == null) {
            return;
        }
        int beginIndex = 0;
        while (true) {
            final int index = s.indexOf(44, beginIndex);
            if (index == -1) {
                break;
            }
            this.addID(s.substring(beginIndex, index));
            beginIndex = index + 1;
        }
        this.addID(s.substring(beginIndex));
    }
    
    public int[] getReferencedIds() {
        return Arrays.copyOf(this.mIds, this.mCount);
    }
    
    protected void init(final AttributeSet set) {
        if (set != null) {
            final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(set, R.styleable.ConstraintLayout_Layout);
            for (int indexCount = obtainStyledAttributes.getIndexCount(), i = 0; i < indexCount; ++i) {
                final int index = obtainStyledAttributes.getIndex(i);
                if (index == R.styleable.ConstraintLayout_Layout_constraint_referenced_ids) {
                    this.setIds(this.mReferenceIds = obtainStyledAttributes.getString(index));
                }
            }
        }
    }
    
    public void onDraw(final Canvas canvas) {
    }
    
    protected void onMeasure(final int n, final int n2) {
        if (this.mUseViewMeasure) {
            super.onMeasure(n, n2);
        }
        else {
            this.setMeasuredDimension(0, 0);
        }
    }
    
    public void setReferencedIds(final int[] array) {
        int i = 0;
        this.mCount = 0;
        while (i < array.length) {
            this.setTag(array[i], null);
            ++i;
        }
    }
    
    public void setTag(final int n, final Object o) {
        if (this.mCount + 1 > this.mIds.length) {
            this.mIds = Arrays.copyOf(this.mIds, this.mIds.length * 2);
        }
        this.mIds[this.mCount] = n;
        ++this.mCount;
    }
    
    public void updatePostLayout(final ConstraintLayout constraintLayout) {
    }
    
    public void updatePostMeasure(final ConstraintLayout constraintLayout) {
    }
    
    public void updatePreLayout(final ConstraintLayout constraintLayout) {
        if (this.isInEditMode()) {
            this.setIds(this.mReferenceIds);
        }
        if (this.mHelperWidget == null) {
            return;
        }
        this.mHelperWidget.removeAllIds();
        for (int i = 0; i < this.mCount; ++i) {
            final View viewById = constraintLayout.findViewById(this.mIds[i]);
            if (viewById != null) {
                this.mHelperWidget.add(constraintLayout.getViewWidget(viewById));
            }
        }
    }
    
    public void validateParams() {
        if (this.mHelperWidget == null) {
            return;
        }
        final ViewGroup$LayoutParams layoutParams = this.getLayoutParams();
        if (layoutParams instanceof ConstraintLayout.LayoutParams) {
            ((ConstraintLayout.LayoutParams)layoutParams).widget = this.mHelperWidget;
        }
    }
}
