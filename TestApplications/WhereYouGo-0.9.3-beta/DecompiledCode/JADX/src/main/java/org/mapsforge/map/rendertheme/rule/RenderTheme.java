package org.mapsforge.map.rendertheme.rule;

import java.util.ArrayList;
import java.util.List;
import org.mapsforge.core.model.Tag;
import org.mapsforge.core.util.LRUCache;
import org.mapsforge.map.rendertheme.RenderCallback;
import org.mapsforge.map.rendertheme.renderinstruction.RenderInstruction;

public class RenderTheme {
    private static final int MATCHING_CACHE_SIZE = 512;
    private final float baseStrokeWidth;
    private final float baseTextSize;
    private int levels;
    private final int mapBackground;
    private final LRUCache<MatchingCacheKey, List<RenderInstruction>> matchingCache = new LRUCache(512);
    private final ArrayList<Rule> rulesList = new ArrayList();

    RenderTheme(RenderThemeBuilder renderThemeBuilder) {
        this.baseStrokeWidth = renderThemeBuilder.baseStrokeWidth;
        this.baseTextSize = renderThemeBuilder.baseTextSize;
        this.mapBackground = renderThemeBuilder.mapBackground;
    }

    public void destroy() {
        this.matchingCache.clear();
        int n = this.rulesList.size();
        for (int i = 0; i < n; i++) {
            ((Rule) this.rulesList.get(i)).onDestroy();
        }
    }

    public int getLevels() {
        return this.levels;
    }

    public int getMapBackground() {
        return this.mapBackground;
    }

    public void matchClosedWay(RenderCallback renderCallback, List<Tag> tags, byte zoomLevel) {
        matchWay(renderCallback, tags, zoomLevel, Closed.YES);
    }

    public void matchLinearWay(RenderCallback renderCallback, List<Tag> tags, byte zoomLevel) {
        matchWay(renderCallback, tags, zoomLevel, Closed.NO);
    }

    public void matchNode(RenderCallback renderCallback, List<Tag> tags, byte zoomLevel) {
        int n = this.rulesList.size();
        for (int i = 0; i < n; i++) {
            ((Rule) this.rulesList.get(i)).matchNode(renderCallback, tags, zoomLevel);
        }
    }

    public void scaleStrokeWidth(float scaleFactor) {
        int n = this.rulesList.size();
        for (int i = 0; i < n; i++) {
            ((Rule) this.rulesList.get(i)).scaleStrokeWidth(this.baseStrokeWidth * scaleFactor);
        }
    }

    public void scaleTextSize(float scaleFactor) {
        int n = this.rulesList.size();
        for (int i = 0; i < n; i++) {
            ((Rule) this.rulesList.get(i)).scaleTextSize(this.baseTextSize * scaleFactor);
        }
    }

    private void matchWay(RenderCallback renderCallback, List<Tag> tags, byte zoomLevel, Closed closed) {
        MatchingCacheKey matchingCacheKey = new MatchingCacheKey(tags, zoomLevel, closed);
        List<RenderInstruction> matchingList = (List) this.matchingCache.get(matchingCacheKey);
        int n;
        int i;
        if (matchingList != null) {
            n = matchingList.size();
            for (i = 0; i < n; i++) {
                ((RenderInstruction) matchingList.get(i)).renderWay(renderCallback, tags);
            }
            return;
        }
        matchingList = new ArrayList();
        n = this.rulesList.size();
        for (i = 0; i < n; i++) {
            ((Rule) this.rulesList.get(i)).matchWay(renderCallback, tags, zoomLevel, closed, matchingList);
        }
        this.matchingCache.put(matchingCacheKey, matchingList);
    }

    /* Access modifiers changed, original: 0000 */
    public void addRule(Rule rule) {
        this.rulesList.add(rule);
    }

    /* Access modifiers changed, original: 0000 */
    public void complete() {
        this.rulesList.trimToSize();
        int n = this.rulesList.size();
        for (int i = 0; i < n; i++) {
            ((Rule) this.rulesList.get(i)).onComplete();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setLevels(int levels) {
        this.levels = levels;
    }
}
