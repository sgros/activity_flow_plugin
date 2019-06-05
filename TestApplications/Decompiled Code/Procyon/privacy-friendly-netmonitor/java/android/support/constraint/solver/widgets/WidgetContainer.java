// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import java.util.ArrayList;

public class WidgetContainer extends ConstraintWidget
{
    protected ArrayList<ConstraintWidget> mChildren;
    
    public WidgetContainer() {
        this.mChildren = new ArrayList<ConstraintWidget>();
    }
    
    public WidgetContainer(final int n, final int n2) {
        super(n, n2);
        this.mChildren = new ArrayList<ConstraintWidget>();
    }
    
    public WidgetContainer(final int n, final int n2, final int n3, final int n4) {
        super(n, n2, n3, n4);
        this.mChildren = new ArrayList<ConstraintWidget>();
    }
    
    public static Rectangle getBounds(final ArrayList<ConstraintWidget> list) {
        final Rectangle rectangle = new Rectangle();
        if (list.size() == 0) {
            return rectangle;
        }
        final int size = list.size();
        int n = Integer.MAX_VALUE;
        int i = 0;
        int n2 = Integer.MAX_VALUE;
        int n4;
        int n3 = n4 = 0;
        while (i < size) {
            final ConstraintWidget constraintWidget = list.get(i);
            int x;
            if (constraintWidget.getX() < (x = n)) {
                x = constraintWidget.getX();
            }
            int y;
            if (constraintWidget.getY() < (y = n2)) {
                y = constraintWidget.getY();
            }
            int right;
            if (constraintWidget.getRight() > (right = n3)) {
                right = constraintWidget.getRight();
            }
            int bottom;
            if (constraintWidget.getBottom() > (bottom = n4)) {
                bottom = constraintWidget.getBottom();
            }
            ++i;
            n = x;
            n2 = y;
            n3 = right;
            n4 = bottom;
        }
        rectangle.setBounds(n, n2, n3 - n, n4 - n2);
        return rectangle;
    }
    
    public void add(final ConstraintWidget e) {
        this.mChildren.add(e);
        if (e.getParent() != null) {
            ((WidgetContainer)e.getParent()).remove(e);
        }
        e.setParent(this);
    }
    
    public ConstraintWidget findWidget(final float n, final float n2) {
        final int drawX = this.getDrawX();
        final int drawY = this.getDrawY();
        final int width = this.getWidth();
        final int height = this.getHeight();
        WidgetContainer widgetContainer;
        if (n >= drawX && n <= width + drawX && n2 >= drawY && n2 <= height + drawY) {
            widgetContainer = this;
        }
        else {
            widgetContainer = null;
        }
        WidgetContainer widgetContainer2;
        for (int i = 0; i < this.mChildren.size(); ++i, widgetContainer = widgetContainer2) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            WidgetContainer widgetContainer3;
            if (constraintWidget instanceof WidgetContainer) {
                final ConstraintWidget widget = ((WidgetContainer)constraintWidget).findWidget(n, n2);
                widgetContainer2 = widgetContainer;
                if (widget == null) {
                    continue;
                }
                widgetContainer3 = (WidgetContainer)widget;
            }
            else {
                final int drawX2 = constraintWidget.getDrawX();
                final int drawY2 = constraintWidget.getDrawY();
                final int width2 = constraintWidget.getWidth();
                final int height2 = constraintWidget.getHeight();
                widgetContainer2 = widgetContainer;
                if (n < drawX2) {
                    continue;
                }
                widgetContainer2 = widgetContainer;
                if (n > width2 + drawX2) {
                    continue;
                }
                widgetContainer2 = widgetContainer;
                if (n2 < drawY2) {
                    continue;
                }
                widgetContainer2 = widgetContainer;
                if (n2 > height2 + drawY2) {
                    continue;
                }
                widgetContainer3 = (WidgetContainer)constraintWidget;
            }
            widgetContainer2 = widgetContainer3;
        }
        return widgetContainer;
    }
    
    public ArrayList<ConstraintWidget> findWidgets(int i, int size, final int n, final int n2) {
        final ArrayList<ConstraintWidget> list = new ArrayList<ConstraintWidget>();
        final Rectangle rectangle = new Rectangle();
        rectangle.setBounds(i, size, n, n2);
        ConstraintWidget e;
        Rectangle rectangle2;
        for (size = this.mChildren.size(), i = 0; i < size; ++i) {
            e = this.mChildren.get(i);
            rectangle2 = new Rectangle();
            rectangle2.setBounds(e.getDrawX(), e.getDrawY(), e.getWidth(), e.getHeight());
            if (rectangle.intersects(rectangle2)) {
                list.add(e);
            }
        }
        return list;
    }
    
    public ArrayList<ConstraintWidget> getChildren() {
        return this.mChildren;
    }
    
    public ConstraintWidgetContainer getRootConstraintContainer() {
        ConstraintWidget parent = this.getParent();
        ConstraintWidgetContainer constraintWidgetContainer;
        if (this instanceof ConstraintWidgetContainer) {
            constraintWidgetContainer = (ConstraintWidgetContainer)this;
        }
        else {
            constraintWidgetContainer = null;
        }
        while (parent != null) {
            final ConstraintWidget parent2 = parent.getParent();
            if (parent instanceof ConstraintWidgetContainer) {
                constraintWidgetContainer = (ConstraintWidgetContainer)parent;
            }
            parent = parent2;
        }
        return constraintWidgetContainer;
    }
    
    public void layout() {
        this.updateDrawPosition();
        if (this.mChildren == null) {
            return;
        }
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof WidgetContainer) {
                ((WidgetContainer)constraintWidget).layout();
            }
        }
    }
    
    public void remove(final ConstraintWidget o) {
        this.mChildren.remove(o);
        o.setParent(null);
    }
    
    public void removeAllChildren() {
        this.mChildren.clear();
    }
    
    @Override
    public void reset() {
        this.mChildren.clear();
        super.reset();
    }
    
    @Override
    public void resetGroups() {
        super.resetGroups();
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            this.mChildren.get(i).resetGroups();
        }
    }
    
    @Override
    public void resetSolverVariables(final Cache cache) {
        super.resetSolverVariables(cache);
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            this.mChildren.get(i).resetSolverVariables(cache);
        }
    }
    
    @Override
    public void setOffset(int i, int size) {
        super.setOffset(i, size);
        for (size = this.mChildren.size(), i = 0; i < size; ++i) {
            this.mChildren.get(i).setOffset(this.getRootX(), this.getRootY());
        }
    }
    
    @Override
    public void updateDrawPosition() {
        super.updateDrawPosition();
        if (this.mChildren == null) {
            return;
        }
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            constraintWidget.setOffset(this.getDrawX(), this.getDrawY());
            if (!(constraintWidget instanceof ConstraintWidgetContainer)) {
                constraintWidget.updateDrawPosition();
            }
        }
    }
}
