// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme.rule;

import org.mapsforge.core.model.Tag;
import org.mapsforge.map.rendertheme.RenderCallback;
import java.util.ArrayList;
import org.mapsforge.map.rendertheme.renderinstruction.RenderInstruction;
import java.util.List;
import org.mapsforge.core.util.LRUCache;

public class RenderTheme
{
    private static final int MATCHING_CACHE_SIZE = 512;
    private final float baseStrokeWidth;
    private final float baseTextSize;
    private int levels;
    private final int mapBackground;
    private final LRUCache<MatchingCacheKey, List<RenderInstruction>> matchingCache;
    private final ArrayList<Rule> rulesList;
    
    RenderTheme(final RenderThemeBuilder renderThemeBuilder) {
        this.baseStrokeWidth = renderThemeBuilder.baseStrokeWidth;
        this.baseTextSize = renderThemeBuilder.baseTextSize;
        this.mapBackground = renderThemeBuilder.mapBackground;
        this.rulesList = new ArrayList<Rule>();
        this.matchingCache = new LRUCache<MatchingCacheKey, List<RenderInstruction>>(512);
    }
    
    private void matchWay(final RenderCallback renderCallback, final List<Tag> list, final byte b, final Closed closed) {
        final MatchingCacheKey matchingCacheKey = new MatchingCacheKey(list, b, closed);
        final List<RenderInstruction> list2 = this.matchingCache.get(matchingCacheKey);
        if (list2 != null) {
            for (int i = 0; i < list2.size(); ++i) {
                list2.get(i).renderWay(renderCallback, list);
            }
        }
        else {
            final ArrayList<RenderInstruction> value = new ArrayList<RenderInstruction>();
            for (int j = 0; j < this.rulesList.size(); ++j) {
                this.rulesList.get(j).matchWay(renderCallback, list, b, closed, value);
            }
            this.matchingCache.put(matchingCacheKey, value);
        }
    }
    
    void addRule(final Rule e) {
        this.rulesList.add(e);
    }
    
    void complete() {
        this.rulesList.trimToSize();
        for (int i = 0; i < this.rulesList.size(); ++i) {
            this.rulesList.get(i).onComplete();
        }
    }
    
    public void destroy() {
        this.matchingCache.clear();
        for (int i = 0; i < this.rulesList.size(); ++i) {
            this.rulesList.get(i).onDestroy();
        }
    }
    
    public int getLevels() {
        return this.levels;
    }
    
    public int getMapBackground() {
        return this.mapBackground;
    }
    
    public void matchClosedWay(final RenderCallback renderCallback, final List<Tag> list, final byte b) {
        this.matchWay(renderCallback, list, b, Closed.YES);
    }
    
    public void matchLinearWay(final RenderCallback renderCallback, final List<Tag> list, final byte b) {
        this.matchWay(renderCallback, list, b, Closed.NO);
    }
    
    public void matchNode(final RenderCallback renderCallback, final List<Tag> list, final byte b) {
        for (int i = 0; i < this.rulesList.size(); ++i) {
            this.rulesList.get(i).matchNode(renderCallback, list, b);
        }
    }
    
    public void scaleStrokeWidth(final float n) {
        for (int i = 0; i < this.rulesList.size(); ++i) {
            this.rulesList.get(i).scaleStrokeWidth(this.baseStrokeWidth * n);
        }
    }
    
    public void scaleTextSize(final float n) {
        for (int i = 0; i < this.rulesList.size(); ++i) {
            this.rulesList.get(i).scaleTextSize(this.baseTextSize * n);
        }
    }
    
    void setLevels(final int levels) {
        this.levels = levels;
    }
}
