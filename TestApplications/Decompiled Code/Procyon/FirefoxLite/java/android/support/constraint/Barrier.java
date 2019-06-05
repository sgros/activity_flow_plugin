// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint;

import android.os.Build$VERSION;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.content.Context;

public class Barrier extends ConstraintHelper
{
    private android.support.constraint.solver.widgets.Barrier mBarrier;
    private int mIndicatedType;
    private int mResolvedType;
    
    public Barrier(final Context context) {
        super(context);
        this.mIndicatedType = 0;
        this.mResolvedType = 0;
        super.setVisibility(8);
    }
    
    public int getType() {
        return this.mIndicatedType;
    }
    
    @Override
    protected void init(final AttributeSet set) {
        super.init(set);
        this.mBarrier = new android.support.constraint.solver.widgets.Barrier();
        if (set != null) {
            final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(set, R.styleable.ConstraintLayout_Layout);
            for (int indexCount = obtainStyledAttributes.getIndexCount(), i = 0; i < indexCount; ++i) {
                final int index = obtainStyledAttributes.getIndex(i);
                if (index == R.styleable.ConstraintLayout_Layout_barrierDirection) {
                    this.setType(obtainStyledAttributes.getInt(index, 0));
                }
                else if (index == R.styleable.ConstraintLayout_Layout_barrierAllowsGoneWidgets) {
                    this.mBarrier.setAllowsGoneWidget(obtainStyledAttributes.getBoolean(index, true));
                }
            }
        }
        this.mHelperWidget = this.mBarrier;
        this.validateParams();
    }
    
    public void setType(int n) {
        this.mIndicatedType = n;
        this.mResolvedType = n;
        if (Build$VERSION.SDK_INT < 17) {
            if (this.mIndicatedType == 5) {
                this.mResolvedType = 0;
            }
            else if (this.mIndicatedType == 6) {
                this.mResolvedType = 1;
            }
        }
        else {
            if (1 == this.getResources().getConfiguration().getLayoutDirection()) {
                n = 1;
            }
            else {
                n = 0;
            }
            if (n != 0) {
                if (this.mIndicatedType == 5) {
                    this.mResolvedType = 1;
                }
                else if (this.mIndicatedType == 6) {
                    this.mResolvedType = 0;
                }
            }
            else if (this.mIndicatedType == 5) {
                this.mResolvedType = 0;
            }
            else if (this.mIndicatedType == 6) {
                this.mResolvedType = 1;
            }
        }
        this.mBarrier.setBarrierType(this.mResolvedType);
    }
}
