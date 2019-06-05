// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import java.io.Serializable;
import org.mapsforge.core.model.Tile;
import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Rect;

class LabelPlacement
{
    private static final int LABEL_DISTANCE_TO_LABEL = 2;
    private static final int LABEL_DISTANCE_TO_SYMBOL = 2;
    private static final int PLACEMENT_MODEL = 1;
    private static final int START_DISTANCE_TO_SYMBOLS = 4;
    private static final int SYMBOL_DISTANCE_TO_SYMBOL = 2;
    final DependencyCache dependencyCache;
    PointTextContainer label;
    Rect rect1;
    Rect rect2;
    ReferencePosition referencePosition;
    SymbolContainer symbolContainer;
    
    LabelPlacement() {
        this.dependencyCache = new DependencyCache();
    }
    
    private void centerLabels(final List<PointTextContainer> list) {
        for (int i = 0; i < list.size(); ++i) {
            this.label = list.get(i);
            this.label.x -= this.label.boundary.width() / 2;
        }
    }
    
    private void preprocessAreaLabels(final List<PointTextContainer> list) {
        this.centerLabels(list);
        this.removeOutOfTileAreaLabels(list);
        this.removeOverlappingAreaLabels(list);
        if (!list.isEmpty()) {
            this.dependencyCache.removeAreaLabelsInAlreadyDrawnAreas(list);
        }
    }
    
    private void preprocessLabels(final List<PointTextContainer> list) {
        this.removeOutOfTileLabels(list);
    }
    
    private void preprocessSymbols(final List<SymbolContainer> list) {
        this.removeOutOfTileSymbols(list);
        this.removeOverlappingSymbols(list);
        this.dependencyCache.removeSymbolsFromDrawnAreas(list);
    }
    
    private List<PointTextContainer> processFourPointGreedy(final List<PointTextContainer> list, final List<SymbolContainer> list2, final List<PointTextContainer> list3) {
        final ArrayList<PointTextContainer> list4 = new ArrayList<PointTextContainer>();
        final ReferencePosition[] array = new ReferencePosition[list.size() * 4];
        final PriorityQueue<Object> priorityQueue = (PriorityQueue<Object>)new PriorityQueue<ReferencePosition>(list.size() * 4 * 2 + list.size() / 10 * 2, (Comparator<? super ReferencePosition>)ReferencePositionYComparator.INSTANCE);
        final PriorityQueue<Object> priorityQueue2 = (PriorityQueue<Object>)new PriorityQueue<ReferencePosition>(list.size() * 4 * 2 + list.size() / 10 * 2, (Comparator<? super ReferencePosition>)ReferencePositionHeightComparator.INSTANCE);
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) != null) {
                if (list.get(i).symbol != null) {
                    final PointTextContainer pointTextContainer = list.get(i);
                    array[i * 4] = new ReferencePosition(pointTextContainer.x - pointTextContainer.boundary.width() / 2, pointTextContainer.y - pointTextContainer.symbol.symbol.getHeight() / 2 - 4, i, (float)pointTextContainer.boundary.width(), (float)pointTextContainer.boundary.height(), pointTextContainer.symbol);
                    array[i * 4 + 1] = new ReferencePosition(pointTextContainer.x - pointTextContainer.boundary.width() / 2, pointTextContainer.y + pointTextContainer.symbol.symbol.getHeight() / 2 + pointTextContainer.boundary.height() + 4, i, (float)pointTextContainer.boundary.width(), (float)pointTextContainer.boundary.height(), pointTextContainer.symbol);
                    array[i * 4 + 2] = new ReferencePosition(pointTextContainer.x - pointTextContainer.symbol.symbol.getWidth() / 2 - pointTextContainer.boundary.width() - 4, pointTextContainer.y + pointTextContainer.boundary.height() / 2, i, (float)pointTextContainer.boundary.width(), (float)pointTextContainer.boundary.height(), pointTextContainer.symbol);
                    array[i * 4 + 3] = new ReferencePosition(pointTextContainer.x + pointTextContainer.symbol.symbol.getWidth() / 2 + 4, pointTextContainer.y + pointTextContainer.boundary.height() / 2 - 0.10000000149011612, i, (float)pointTextContainer.boundary.width(), (float)pointTextContainer.boundary.height(), pointTextContainer.symbol);
                }
                else {
                    array[i * 4] = new ReferencePosition(list.get(i).x - list.get(i).boundary.width() / 2, list.get(i).y, i, (float)list.get(i).boundary.width(), (float)list.get(i).boundary.height(), null);
                    array[i * 4 + 1] = null;
                    array[i * 4 + 3] = (array[i * 4 + 2] = null);
                }
            }
        }
        this.removeNonValidateReferencePosition(array, list2, list3);
        for (int j = 0; j < array.length; ++j) {
            this.referencePosition = array[j];
            if (this.referencePosition != null) {
                priorityQueue.add(this.referencePosition);
                priorityQueue2.add(this.referencePosition);
            }
        }
        while (priorityQueue.size() != 0) {
            this.referencePosition = priorityQueue.remove();
            this.label = list.get(this.referencePosition.nodeNumber);
            list4.add(new PointTextContainer(this.label.text, this.referencePosition.x, this.referencePosition.y, this.label.paintFront, this.label.paintBack, this.label.symbol));
            if (priorityQueue.size() == 0) {
                break;
            }
            priorityQueue.remove(array[this.referencePosition.nodeNumber * 4 + 0]);
            priorityQueue.remove(array[this.referencePosition.nodeNumber * 4 + 1]);
            priorityQueue.remove(array[this.referencePosition.nodeNumber * 4 + 2]);
            priorityQueue.remove(array[this.referencePosition.nodeNumber * 4 + 3]);
            priorityQueue2.remove(array[this.referencePosition.nodeNumber * 4 + 0]);
            priorityQueue2.remove(array[this.referencePosition.nodeNumber * 4 + 1]);
            priorityQueue2.remove(array[this.referencePosition.nodeNumber * 4 + 2]);
            priorityQueue2.remove(array[this.referencePosition.nodeNumber * 4 + 3]);
            final LinkedList<ReferencePosition> c = new LinkedList<ReferencePosition>();
            while (priorityQueue2.size() != 0 && priorityQueue2.peek().x < this.referencePosition.x + this.referencePosition.width) {
                c.add((Object)priorityQueue2.remove());
            }
            int n;
            for (int k = 0; k < c.size(); k = n + 1) {
                n = k;
                if (c.get(k).x <= this.referencePosition.x + this.referencePosition.width) {
                    n = k;
                    if (c.get(k).y >= this.referencePosition.y - c.get(k).height) {
                        n = k;
                        if (c.get(k).y <= this.referencePosition.y + c.get(k).height) {
                            priorityQueue.remove(c.get(k));
                            c.remove(k);
                            n = k - 1;
                        }
                    }
                }
            }
            priorityQueue2.addAll(c);
        }
        return list4;
    }
    
    private List<PointTextContainer> processTwoPointGreedy(final List<PointTextContainer> list, final List<SymbolContainer> list2, final List<PointTextContainer> list3) {
        final ArrayList<PointTextContainer> list4 = new ArrayList<PointTextContainer>();
        final ReferencePosition[] array = new ReferencePosition[list.size() * 2];
        final PriorityQueue<Object> priorityQueue = (PriorityQueue<Object>)new PriorityQueue<ReferencePosition>(list.size() * 2 + list.size() / 10 * 2, (Comparator<? super ReferencePosition>)ReferencePositionWidthComparator.INSTANCE);
        final PriorityQueue<Object> priorityQueue2 = (PriorityQueue<Object>)new PriorityQueue<ReferencePosition>(list.size() * 2 + list.size() / 10 * 2, (Comparator<? super ReferencePosition>)ReferencePositionXComparator.INSTANCE);
        for (int i = 0; i < list.size(); ++i) {
            this.label = list.get(i);
            if (this.label.symbol != null) {
                array[i * 2] = new ReferencePosition(this.label.x - this.label.boundary.width() / 2 - 0.10000000149011612, this.label.y - this.label.boundary.height() - 4.0, i, (float)this.label.boundary.width(), (float)this.label.boundary.height(), this.label.symbol);
                array[i * 2 + 1] = new ReferencePosition(this.label.x - this.label.boundary.width() / 2, this.label.y + this.label.symbol.symbol.getHeight() + 4.0, i, (float)this.label.boundary.width(), (float)this.label.boundary.height(), this.label.symbol);
            }
            else {
                array[i * 2] = new ReferencePosition(this.label.x - this.label.boundary.width() / 2 - 0.10000000149011612, this.label.y, i, (float)this.label.boundary.width(), (float)this.label.boundary.height(), null);
                array[i * 2 + 1] = null;
            }
        }
        this.removeNonValidateReferencePosition(array, list2, list3);
        for (int j = 0; j < array.length; ++j) {
            this.referencePosition = array[j];
            if (this.referencePosition != null) {
                priorityQueue2.add(this.referencePosition);
                priorityQueue.add(this.referencePosition);
            }
        }
        while (priorityQueue.size() != 0) {
            this.referencePosition = priorityQueue.remove();
            this.label = list.get(this.referencePosition.nodeNumber);
            list4.add(new PointTextContainer(this.label.text, this.referencePosition.x, this.referencePosition.y, this.label.paintFront, this.label.paintBack, this.referencePosition.symbol));
            priorityQueue.remove(array[this.referencePosition.nodeNumber * 2 + 1]);
            if (priorityQueue.size() == 0) {
                break;
            }
            priorityQueue2.remove(this.referencePosition);
            priorityQueue2.remove(array[this.referencePosition.nodeNumber * 2 + 1]);
            final LinkedList<ReferencePosition> c = new LinkedList<ReferencePosition>();
            while (priorityQueue2.size() != 0 && priorityQueue2.peek().x < this.referencePosition.x + this.referencePosition.width) {
                c.add((Object)priorityQueue2.remove());
            }
            int n;
            for (int k = 0; k < c.size(); k = n + 1) {
                n = k;
                if (c.get(k).x <= this.referencePosition.x + this.referencePosition.width) {
                    n = k;
                    if (c.get(k).y >= this.referencePosition.y - c.get(k).height) {
                        n = k;
                        if (c.get(k).y <= this.referencePosition.y + c.get(k).height) {
                            priorityQueue.remove(c.get(k));
                            c.remove(k);
                            n = k - 1;
                        }
                    }
                }
            }
            priorityQueue2.addAll(c);
        }
        return list4;
    }
    
    private void removeEmptySymbolReferences(final List<PointTextContainer> list, final List<SymbolContainer> list2) {
        for (int i = 0; i < list.size(); ++i) {
            this.label = list.get(i);
            if (!list2.contains(this.label.symbol)) {
                this.label.symbol = null;
            }
        }
    }
    
    private void removeNonValidateReferencePosition(final ReferencePosition[] array, final List<SymbolContainer> list, final List<PointTextContainer> list2) {
        for (int i = 0; i < list.size(); ++i) {
            this.symbolContainer = list.get(i);
            this.rect1 = new Rect((int)this.symbolContainer.point.x - 2, (int)this.symbolContainer.point.y - 2, (int)this.symbolContainer.point.x + this.symbolContainer.symbol.getWidth() + 2, (int)this.symbolContainer.point.y + this.symbolContainer.symbol.getHeight() + 2);
            for (int j = 0; j < array.length; ++j) {
                if (array[j] != null) {
                    this.rect2 = new Rect((int)array[j].x, (int)(array[j].y - array[j].height), (int)(array[j].x + array[j].width), (int)array[j].y);
                    if (Rect.intersects(this.rect2, this.rect1)) {
                        array[j] = null;
                    }
                }
            }
        }
        for (final PointTextContainer pointTextContainer : list2) {
            this.rect1 = new Rect((int)pointTextContainer.x - 2, (int)pointTextContainer.y - pointTextContainer.boundary.height() - 2, (int)pointTextContainer.x + pointTextContainer.boundary.width() + 2, (int)pointTextContainer.y + 2);
            for (int k = 0; k < array.length; ++k) {
                if (array[k] != null) {
                    this.rect2 = new Rect((int)array[k].x, (int)(array[k].y - array[k].height), (int)(array[k].x + array[k].width), (int)array[k].y);
                    if (Rect.intersects(this.rect2, this.rect1)) {
                        array[k] = null;
                    }
                }
            }
        }
        this.dependencyCache.removeReferencePointsFromDependencyCache(array);
    }
    
    private void removeOutOfTileAreaLabels(final List<PointTextContainer> list) {
        int n;
        for (int i = 0; i < list.size(); i = n + 1) {
            this.label = list.get(i);
            if (this.label.x > 256.0) {
                list.remove(i);
                n = i - 1;
            }
            else if (this.label.y - this.label.boundary.height() > 256.0) {
                list.remove(i);
                n = i - 1;
            }
            else if (this.label.x + this.label.boundary.width() < 0.0) {
                list.remove(i);
                n = i - 1;
            }
            else {
                n = i;
                if (this.label.y + this.label.boundary.height() < 0.0) {
                    list.remove(i);
                    n = i - 1;
                }
            }
        }
    }
    
    private void removeOutOfTileLabels(final List<PointTextContainer> list) {
        int i = 0;
        while (i < list.size()) {
            this.label = list.get(i);
            if (this.label.x - this.label.boundary.width() / 2 > 256.0) {
                list.remove(i);
                this.label = null;
            }
            else if (this.label.y - this.label.boundary.height() > 256.0) {
                list.remove(i);
                this.label = null;
            }
            else if (this.label.x - this.label.boundary.width() / 2 + this.label.boundary.width() < 0.0) {
                list.remove(i);
                this.label = null;
            }
            else if (this.label.y < 0.0) {
                list.remove(i);
                this.label = null;
            }
            else {
                ++i;
            }
        }
    }
    
    private void removeOutOfTileSymbols(final List<SymbolContainer> list) {
        int i = 0;
        while (i < list.size()) {
            this.symbolContainer = list.get(i);
            if (this.symbolContainer.point.x > 256.0) {
                list.remove(i);
            }
            else if (this.symbolContainer.point.y > 256.0) {
                list.remove(i);
            }
            else if (this.symbolContainer.point.x + this.symbolContainer.symbol.getWidth() < 0.0) {
                list.remove(i);
            }
            else if (this.symbolContainer.point.y + this.symbolContainer.symbol.getHeight() < 0.0) {
                list.remove(i);
            }
            else {
                ++i;
            }
        }
    }
    
    private void removeOverlappingAreaLabels(final List<PointTextContainer> list) {
        for (int i = 0; i < list.size(); ++i) {
            this.label = list.get(i);
            this.rect1 = new Rect((int)this.label.x - 2, (int)this.label.y - 2, (int)(this.label.x + this.label.boundary.width()) + 2, (int)(this.label.y + this.label.boundary.height() + 2));
            int n;
            for (int j = i + 1; j < list.size(); j = n + 1) {
                if ((n = j) != i) {
                    this.label = list.get(j);
                    this.rect2 = new Rect((int)this.label.x, (int)this.label.y, (int)(this.label.x + this.label.boundary.width()), (int)(this.label.y + this.label.boundary.height()));
                    n = j;
                    if (Rect.intersects(this.rect1, this.rect2)) {
                        list.remove(j);
                        n = j - 1;
                    }
                }
            }
        }
    }
    
    private void removeOverlappingSymbolsWithAreaLabels(final List<SymbolContainer> list, final List<PointTextContainer> list2) {
        for (int i = 0; i < list2.size(); ++i) {
            this.label = list2.get(i);
            this.rect1 = new Rect((int)this.label.x - 2, (int)(this.label.y - this.label.boundary.height()) - 2, (int)(this.label.x + this.label.boundary.width() + 2), (int)(this.label.y + 2));
            int n;
            for (int j = 0; j < list.size(); j = n + 1) {
                this.symbolContainer = list.get(j);
                this.rect2 = new Rect((int)this.symbolContainer.point.x, (int)this.symbolContainer.point.y, (int)(this.symbolContainer.point.x + this.symbolContainer.symbol.getWidth()), (int)(this.symbolContainer.point.y + this.symbolContainer.symbol.getHeight()));
                n = j;
                if (Rect.intersects(this.rect1, this.rect2)) {
                    list.remove(j);
                    n = j - 1;
                }
            }
        }
    }
    
    List<PointTextContainer> placeLabels(List<PointTextContainer> list, final List<SymbolContainer> list2, final List<PointTextContainer> list3, final Tile tile) {
        final List<PointTextContainer> list4 = list;
        this.dependencyCache.generateTileAndDependencyOnTile(tile);
        this.preprocessAreaLabels(list3);
        this.preprocessLabels(list4);
        this.preprocessSymbols(list2);
        this.removeEmptySymbolReferences(list4, list2);
        this.removeOverlappingSymbolsWithAreaLabels(list2, list3);
        this.dependencyCache.removeOverlappingObjectsWithDependencyOnTile(list4, list3, list2);
        list = list4;
        if (!list4.isEmpty()) {
            switch (true) {
                default: {
                    list = list4;
                    break;
                }
                case 0: {
                    list = this.processTwoPointGreedy(list4, list2, list3);
                    break;
                }
                case 1: {
                    list = this.processFourPointGreedy(list4, list2, list3);
                    break;
                }
            }
        }
        this.dependencyCache.fillDependencyOnTile(list, list2, list3);
        return list;
    }
    
    void removeOverlappingSymbols(final List<SymbolContainer> list) {
        for (int i = 0; i < list.size(); ++i) {
            this.symbolContainer = list.get(i);
            this.rect1 = new Rect((int)this.symbolContainer.point.x - 2, (int)this.symbolContainer.point.y - 2, (int)this.symbolContainer.point.x + this.symbolContainer.symbol.getWidth() + 2, (int)this.symbolContainer.point.y + this.symbolContainer.symbol.getHeight() + 2);
            int n;
            for (int j = i + 1; j < list.size(); j = n + 1) {
                if ((n = j) != i) {
                    this.symbolContainer = list.get(j);
                    this.rect2 = new Rect((int)this.symbolContainer.point.x, (int)this.symbolContainer.point.y, (int)this.symbolContainer.point.x + this.symbolContainer.symbol.getWidth(), (int)this.symbolContainer.point.y + this.symbolContainer.symbol.getHeight());
                    n = j;
                    if (Rect.intersects(this.rect2, this.rect1)) {
                        list.remove(j);
                        n = j - 1;
                    }
                }
            }
        }
    }
    
    static class ReferencePosition
    {
        final float height;
        final int nodeNumber;
        SymbolContainer symbol;
        final float width;
        final double x;
        final double y;
        
        ReferencePosition(final double x, final double y, final int nodeNumber, final float width, final float height, final SymbolContainer symbol) {
            this.x = x;
            this.y = y;
            this.nodeNumber = nodeNumber;
            this.width = width;
            this.height = height;
            this.symbol = symbol;
        }
    }
    
    static final class ReferencePositionHeightComparator implements Comparator<ReferencePosition>, Serializable
    {
        static final ReferencePositionHeightComparator INSTANCE;
        private static final long serialVersionUID = 1L;
        
        static {
            INSTANCE = new ReferencePositionHeightComparator();
        }
        
        private ReferencePositionHeightComparator() {
        }
        
        @Override
        public int compare(final ReferencePosition referencePosition, final ReferencePosition referencePosition2) {
            int n;
            if (referencePosition.y - referencePosition.height < referencePosition2.y - referencePosition2.height) {
                n = -1;
            }
            else if (referencePosition.y - referencePosition.height > referencePosition2.y - referencePosition2.height) {
                n = 1;
            }
            else {
                n = 0;
            }
            return n;
        }
    }
    
    static final class ReferencePositionWidthComparator implements Comparator<ReferencePosition>, Serializable
    {
        static final ReferencePositionWidthComparator INSTANCE;
        private static final long serialVersionUID = 1L;
        
        static {
            INSTANCE = new ReferencePositionWidthComparator();
        }
        
        private ReferencePositionWidthComparator() {
        }
        
        @Override
        public int compare(final ReferencePosition referencePosition, final ReferencePosition referencePosition2) {
            int n;
            if (referencePosition.x + referencePosition.width < referencePosition2.x + referencePosition2.width) {
                n = -1;
            }
            else if (referencePosition.x + referencePosition.width > referencePosition2.x + referencePosition2.width) {
                n = 1;
            }
            else {
                n = 0;
            }
            return n;
        }
    }
    
    static final class ReferencePositionXComparator implements Comparator<ReferencePosition>, Serializable
    {
        static final ReferencePositionXComparator INSTANCE;
        private static final long serialVersionUID = 1L;
        
        static {
            INSTANCE = new ReferencePositionXComparator();
        }
        
        private ReferencePositionXComparator() {
        }
        
        @Override
        public int compare(final ReferencePosition referencePosition, final ReferencePosition referencePosition2) {
            int n;
            if (referencePosition.x < referencePosition2.x) {
                n = -1;
            }
            else if (referencePosition.x > referencePosition2.x) {
                n = 1;
            }
            else {
                n = 0;
            }
            return n;
        }
    }
    
    static final class ReferencePositionYComparator implements Comparator<ReferencePosition>, Serializable
    {
        static final ReferencePositionYComparator INSTANCE;
        private static final long serialVersionUID = 1L;
        
        static {
            INSTANCE = new ReferencePositionYComparator();
        }
        
        private ReferencePositionYComparator() {
        }
        
        @Override
        public int compare(final ReferencePosition referencePosition, final ReferencePosition referencePosition2) {
            int n;
            if (referencePosition.y < referencePosition2.y) {
                n = -1;
            }
            else if (referencePosition.y > referencePosition2.y) {
                n = 1;
            }
            else {
                n = 0;
            }
            return n;
        }
    }
}
