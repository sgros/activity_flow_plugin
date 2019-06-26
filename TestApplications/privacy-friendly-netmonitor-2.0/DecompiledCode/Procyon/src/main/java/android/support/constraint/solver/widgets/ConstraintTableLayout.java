// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import java.util.ArrayList;

public class ConstraintTableLayout extends ConstraintWidgetContainer
{
    public static final int ALIGN_CENTER = 0;
    private static final int ALIGN_FULL = 3;
    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_RIGHT = 2;
    private ArrayList<Guideline> mHorizontalGuidelines;
    private ArrayList<HorizontalSlice> mHorizontalSlices;
    private int mNumCols;
    private int mNumRows;
    private int mPadding;
    private boolean mVerticalGrowth;
    private ArrayList<Guideline> mVerticalGuidelines;
    private ArrayList<VerticalSlice> mVerticalSlices;
    private LinearSystem system;
    
    public ConstraintTableLayout() {
        this.mVerticalGrowth = true;
        this.mNumCols = 0;
        this.mNumRows = 0;
        this.mPadding = 8;
        this.mVerticalSlices = new ArrayList<VerticalSlice>();
        this.mHorizontalSlices = new ArrayList<HorizontalSlice>();
        this.mVerticalGuidelines = new ArrayList<Guideline>();
        this.mHorizontalGuidelines = new ArrayList<Guideline>();
        this.system = null;
    }
    
    public ConstraintTableLayout(final int n, final int n2) {
        super(n, n2);
        this.mVerticalGrowth = true;
        this.mNumCols = 0;
        this.mNumRows = 0;
        this.mPadding = 8;
        this.mVerticalSlices = new ArrayList<VerticalSlice>();
        this.mHorizontalSlices = new ArrayList<HorizontalSlice>();
        this.mVerticalGuidelines = new ArrayList<Guideline>();
        this.mHorizontalGuidelines = new ArrayList<Guideline>();
        this.system = null;
    }
    
    public ConstraintTableLayout(final int n, final int n2, final int n3, final int n4) {
        super(n, n2, n3, n4);
        this.mVerticalGrowth = true;
        this.mNumCols = 0;
        this.mNumRows = 0;
        this.mPadding = 8;
        this.mVerticalSlices = new ArrayList<VerticalSlice>();
        this.mHorizontalSlices = new ArrayList<HorizontalSlice>();
        this.mVerticalGuidelines = new ArrayList<Guideline>();
        this.mHorizontalGuidelines = new ArrayList<Guideline>();
        this.system = null;
    }
    
    private void setChildrenConnections() {
        final int size = this.mChildren.size();
        int i = 0;
        int n = 0;
        while (i < size) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            final int n2 = n + constraintWidget.getContainerItemSkip();
            final int mNumCols = this.mNumCols;
            final HorizontalSlice horizontalSlice = this.mHorizontalSlices.get(n2 / this.mNumCols);
            final VerticalSlice verticalSlice = this.mVerticalSlices.get(n2 % mNumCols);
            final ConstraintWidget left = verticalSlice.left;
            final ConstraintWidget right = verticalSlice.right;
            final ConstraintWidget top = horizontalSlice.top;
            final ConstraintWidget bottom = horizontalSlice.bottom;
            constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT).connect(left.getAnchor(ConstraintAnchor.Type.LEFT), this.mPadding);
            if (right instanceof Guideline) {
                constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).connect(right.getAnchor(ConstraintAnchor.Type.LEFT), this.mPadding);
            }
            else {
                constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).connect(right.getAnchor(ConstraintAnchor.Type.RIGHT), this.mPadding);
            }
            switch (verticalSlice.alignment) {
                case 3: {
                    constraintWidget.setHorizontalDimensionBehaviour(DimensionBehaviour.MATCH_CONSTRAINT);
                    break;
                }
                case 2: {
                    constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT).setStrength(ConstraintAnchor.Strength.WEAK);
                    constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).setStrength(ConstraintAnchor.Strength.STRONG);
                    break;
                }
                case 1: {
                    constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT).setStrength(ConstraintAnchor.Strength.STRONG);
                    constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).setStrength(ConstraintAnchor.Strength.WEAK);
                    break;
                }
            }
            constraintWidget.getAnchor(ConstraintAnchor.Type.TOP).connect(top.getAnchor(ConstraintAnchor.Type.TOP), this.mPadding);
            if (bottom instanceof Guideline) {
                constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(bottom.getAnchor(ConstraintAnchor.Type.TOP), this.mPadding);
            }
            else {
                constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(bottom.getAnchor(ConstraintAnchor.Type.BOTTOM), this.mPadding);
            }
            n = n2 + 1;
            ++i;
        }
    }
    
    private void setHorizontalSlices() {
        this.mHorizontalSlices.clear();
        final float n = 100.0f / this.mNumRows;
        ConstraintWidget bottom = this;
        int i = 0;
        float n2 = n;
        while (i < this.mNumRows) {
            final HorizontalSlice e = new HorizontalSlice();
            e.top = bottom;
            if (i < this.mNumRows - 1) {
                final Guideline guideline = new Guideline();
                guideline.setOrientation(0);
                guideline.setParent(this);
                guideline.setGuidePercent((int)n2);
                n2 += n;
                e.bottom = guideline;
                this.mHorizontalGuidelines.add(guideline);
            }
            else {
                e.bottom = this;
            }
            bottom = e.bottom;
            this.mHorizontalSlices.add(e);
            ++i;
        }
        this.updateDebugSolverNames();
    }
    
    private void setVerticalSlices() {
        this.mVerticalSlices.clear();
        final float n = 100.0f / this.mNumCols;
        int i = 0;
        ConstraintWidget right = this;
        float n2 = n;
        while (i < this.mNumCols) {
            final VerticalSlice e = new VerticalSlice();
            e.left = right;
            if (i < this.mNumCols - 1) {
                final Guideline guideline = new Guideline();
                guideline.setOrientation(1);
                guideline.setParent(this);
                guideline.setGuidePercent((int)n2);
                n2 += n;
                e.right = guideline;
                this.mVerticalGuidelines.add(guideline);
            }
            else {
                e.right = this;
            }
            right = e.right;
            this.mVerticalSlices.add(e);
            ++i;
        }
        this.updateDebugSolverNames();
    }
    
    private void updateDebugSolverNames() {
        if (this.system == null) {
            return;
        }
        final int size = this.mVerticalGuidelines.size();
        final int n = 0;
        for (int i = 0; i < size; ++i) {
            final Guideline guideline = this.mVerticalGuidelines.get(i);
            final LinearSystem system = this.system;
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getDebugName());
            sb.append(".VG");
            sb.append(i);
            guideline.setDebugSolverName(system, sb.toString());
        }
        for (int size2 = this.mHorizontalGuidelines.size(), j = n; j < size2; ++j) {
            final Guideline guideline2 = this.mHorizontalGuidelines.get(j);
            final LinearSystem system2 = this.system;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(this.getDebugName());
            sb2.append(".HG");
            sb2.append(j);
            guideline2.setDebugSolverName(system2, sb2.toString());
        }
    }
    
    @Override
    public void addToSolver(final LinearSystem linearSystem, final int n) {
        super.addToSolver(linearSystem, n);
        final int size = this.mChildren.size();
        if (size == 0) {
            return;
        }
        this.setTableDimensions();
        if (linearSystem == this.mSystem) {
            final int size2 = this.mVerticalGuidelines.size();
            final int n2 = 0;
            int index = 0;
            while (true) {
                boolean positionRelaxed = true;
                if (index >= size2) {
                    break;
                }
                final Guideline guideline = this.mVerticalGuidelines.get(index);
                if (this.getHorizontalDimensionBehaviour() != DimensionBehaviour.WRAP_CONTENT) {
                    positionRelaxed = false;
                }
                guideline.setPositionRelaxed(positionRelaxed);
                guideline.addToSolver(linearSystem, n);
                ++index;
            }
            final int size3 = this.mHorizontalGuidelines.size();
            int index2 = 0;
            int i;
            while (true) {
                i = n2;
                if (index2 >= size3) {
                    break;
                }
                final Guideline guideline2 = this.mHorizontalGuidelines.get(index2);
                guideline2.setPositionRelaxed(this.getVerticalDimensionBehaviour() == DimensionBehaviour.WRAP_CONTENT);
                guideline2.addToSolver(linearSystem, n);
                ++index2;
            }
            while (i < size) {
                this.mChildren.get(i).addToSolver(linearSystem, n);
                ++i;
            }
        }
    }
    
    public void computeGuidelinesPercentPositions() {
        final int size = this.mVerticalGuidelines.size();
        final int n = 0;
        for (int i = 0; i < size; ++i) {
            this.mVerticalGuidelines.get(i).inferRelativePercentPosition();
        }
        for (int size2 = this.mHorizontalGuidelines.size(), j = n; j < size2; ++j) {
            this.mHorizontalGuidelines.get(j).inferRelativePercentPosition();
        }
    }
    
    public void cycleColumnAlignment(final int index) {
        final VerticalSlice verticalSlice = this.mVerticalSlices.get(index);
        switch (verticalSlice.alignment) {
            case 2: {
                verticalSlice.alignment = 1;
                break;
            }
            case 1: {
                verticalSlice.alignment = 0;
                break;
            }
            case 0: {
                verticalSlice.alignment = 2;
                break;
            }
        }
        this.setChildrenConnections();
    }
    
    public String getColumnAlignmentRepresentation(final int index) {
        final VerticalSlice verticalSlice = this.mVerticalSlices.get(index);
        if (verticalSlice.alignment == 1) {
            return "L";
        }
        if (verticalSlice.alignment == 0) {
            return "C";
        }
        if (verticalSlice.alignment == 3) {
            return "F";
        }
        if (verticalSlice.alignment == 2) {
            return "R";
        }
        return "!";
    }
    
    public String getColumnsAlignmentRepresentation() {
        final int size = this.mVerticalSlices.size();
        String s = "";
        String s2;
        for (int i = 0; i < size; ++i, s = s2) {
            final VerticalSlice verticalSlice = this.mVerticalSlices.get(i);
            if (verticalSlice.alignment == 1) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append("L");
                s2 = sb.toString();
            }
            else if (verticalSlice.alignment == 0) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(s);
                sb2.append("C");
                s2 = sb2.toString();
            }
            else if (verticalSlice.alignment == 3) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(s);
                sb3.append("F");
                s2 = sb3.toString();
            }
            else {
                s2 = s;
                if (verticalSlice.alignment == 2) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append(s);
                    sb4.append("R");
                    s2 = sb4.toString();
                }
            }
        }
        return s;
    }
    
    @Override
    public ArrayList<Guideline> getHorizontalGuidelines() {
        return this.mHorizontalGuidelines;
    }
    
    public int getNumCols() {
        return this.mNumCols;
    }
    
    public int getNumRows() {
        return this.mNumRows;
    }
    
    public int getPadding() {
        return this.mPadding;
    }
    
    @Override
    public String getType() {
        return "ConstraintTableLayout";
    }
    
    @Override
    public ArrayList<Guideline> getVerticalGuidelines() {
        return this.mVerticalGuidelines;
    }
    
    @Override
    public boolean handlesInternalConstraints() {
        return true;
    }
    
    public boolean isVerticalGrowth() {
        return this.mVerticalGrowth;
    }
    
    public void setColumnAlignment(final int index, final int alignment) {
        if (index < this.mVerticalSlices.size()) {
            this.mVerticalSlices.get(index).alignment = alignment;
            this.setChildrenConnections();
        }
    }
    
    public void setColumnAlignment(final String s) {
        for (int length = s.length(), i = 0; i < length; ++i) {
            final char char1 = s.charAt(i);
            if (char1 == 'L') {
                this.setColumnAlignment(i, 1);
            }
            else if (char1 == 'C') {
                this.setColumnAlignment(i, 0);
            }
            else if (char1 == 'F') {
                this.setColumnAlignment(i, 3);
            }
            else if (char1 == 'R') {
                this.setColumnAlignment(i, 2);
            }
            else {
                this.setColumnAlignment(i, 0);
            }
        }
    }
    
    @Override
    public void setDebugSolverName(final LinearSystem system, final String s) {
        super.setDebugSolverName(this.system = system, s);
        this.updateDebugSolverNames();
    }
    
    public void setNumCols(final int mNumCols) {
        if (this.mVerticalGrowth && this.mNumCols != mNumCols) {
            this.mNumCols = mNumCols;
            this.setVerticalSlices();
            this.setTableDimensions();
        }
    }
    
    public void setNumRows(final int mNumRows) {
        if (!this.mVerticalGrowth && this.mNumCols != mNumRows) {
            this.mNumRows = mNumRows;
            this.setHorizontalSlices();
            this.setTableDimensions();
        }
    }
    
    public void setPadding(final int mPadding) {
        if (mPadding > 1) {
            this.mPadding = mPadding;
        }
    }
    
    public void setTableDimensions() {
        final int size = this.mChildren.size();
        int i = 0;
        int n = 0;
        while (i < size) {
            n += this.mChildren.get(i).getContainerItemSkip();
            ++i;
        }
        final int n2 = size + n;
        if (this.mVerticalGrowth) {
            if (this.mNumCols == 0) {
                this.setNumCols(1);
            }
            int mNumRows;
            final int n3 = mNumRows = n2 / this.mNumCols;
            if (this.mNumCols * n3 < n2) {
                mNumRows = n3 + 1;
            }
            if (this.mNumRows == mNumRows && this.mVerticalGuidelines.size() == this.mNumCols - 1) {
                return;
            }
            this.mNumRows = mNumRows;
            this.setHorizontalSlices();
        }
        else {
            if (this.mNumRows == 0) {
                this.setNumRows(1);
            }
            int mNumCols;
            final int n4 = mNumCols = n2 / this.mNumRows;
            if (this.mNumRows * n4 < n2) {
                mNumCols = n4 + 1;
            }
            if (this.mNumCols == mNumCols && this.mHorizontalGuidelines.size() == this.mNumRows - 1) {
                return;
            }
            this.mNumCols = mNumCols;
            this.setVerticalSlices();
        }
        this.setChildrenConnections();
    }
    
    public void setVerticalGrowth(final boolean mVerticalGrowth) {
        this.mVerticalGrowth = mVerticalGrowth;
    }
    
    @Override
    public void updateFromSolver(final LinearSystem linearSystem, final int n) {
        super.updateFromSolver(linearSystem, n);
        if (linearSystem == this.mSystem) {
            final int size = this.mVerticalGuidelines.size();
            final int n2 = 0;
            for (int i = 0; i < size; ++i) {
                this.mVerticalGuidelines.get(i).updateFromSolver(linearSystem, n);
            }
            for (int size2 = this.mHorizontalGuidelines.size(), j = n2; j < size2; ++j) {
                this.mHorizontalGuidelines.get(j).updateFromSolver(linearSystem, n);
            }
        }
    }
    
    class HorizontalSlice
    {
        ConstraintWidget bottom;
        int padding;
        ConstraintWidget top;
    }
    
    class VerticalSlice
    {
        int alignment;
        ConstraintWidget left;
        int padding;
        ConstraintWidget right;
        
        VerticalSlice() {
            this.alignment = 1;
        }
    }
}
