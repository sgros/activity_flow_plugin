package org.mapsforge.map.rendertheme.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mapsforge.core.model.Tag;
import org.mapsforge.map.rendertheme.RenderCallback;
import org.mapsforge.map.rendertheme.renderinstruction.RenderInstruction;

abstract class Rule {
    static final Map<List<String>, AttributeMatcher> MATCHERS_CACHE_KEY = new HashMap();
    static final Map<List<String>, AttributeMatcher> MATCHERS_CACHE_VALUE = new HashMap();
    final ClosedMatcher closedMatcher;
    final ElementMatcher elementMatcher;
    private final ArrayList<RenderInstruction> renderInstructions = new ArrayList(4);
    private final ArrayList<Rule> subRules = new ArrayList(4);
    final byte zoomMax;
    final byte zoomMin;

    public abstract boolean matchesNode(List<Tag> list, byte b);

    public abstract boolean matchesWay(List<Tag> list, byte b, Closed closed);

    Rule(RuleBuilder ruleBuilder) {
        this.closedMatcher = ruleBuilder.closedMatcher;
        this.elementMatcher = ruleBuilder.elementMatcher;
        this.zoomMax = ruleBuilder.zoomMax;
        this.zoomMin = ruleBuilder.zoomMin;
    }

    /* Access modifiers changed, original: 0000 */
    public void addRenderingInstruction(RenderInstruction renderInstruction) {
        this.renderInstructions.add(renderInstruction);
    }

    /* Access modifiers changed, original: 0000 */
    public void addSubRule(Rule rule) {
        this.subRules.add(rule);
    }

    /* Access modifiers changed, original: 0000 */
    public void matchNode(RenderCallback renderCallback, List<Tag> tags, byte zoomLevel) {
        if (matchesNode(tags, zoomLevel)) {
            int i;
            int n = this.renderInstructions.size();
            for (i = 0; i < n; i++) {
                ((RenderInstruction) this.renderInstructions.get(i)).renderNode(renderCallback, tags);
            }
            n = this.subRules.size();
            for (i = 0; i < n; i++) {
                ((Rule) this.subRules.get(i)).matchNode(renderCallback, tags, zoomLevel);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void matchWay(RenderCallback renderCallback, List<Tag> tags, byte zoomLevel, Closed closed, List<RenderInstruction> matchingList) {
        if (matchesWay(tags, zoomLevel, closed)) {
            int i;
            int n = this.renderInstructions.size();
            for (i = 0; i < n; i++) {
                ((RenderInstruction) this.renderInstructions.get(i)).renderWay(renderCallback, tags);
                matchingList.add(this.renderInstructions.get(i));
            }
            n = this.subRules.size();
            for (i = 0; i < n; i++) {
                ((Rule) this.subRules.get(i)).matchWay(renderCallback, tags, zoomLevel, closed, matchingList);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onComplete() {
        MATCHERS_CACHE_KEY.clear();
        MATCHERS_CACHE_VALUE.clear();
        this.renderInstructions.trimToSize();
        this.subRules.trimToSize();
        int n = this.subRules.size();
        for (int i = 0; i < n; i++) {
            ((Rule) this.subRules.get(i)).onComplete();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onDestroy() {
        int i;
        int n = this.renderInstructions.size();
        for (i = 0; i < n; i++) {
            ((RenderInstruction) this.renderInstructions.get(i)).destroy();
        }
        n = this.subRules.size();
        for (i = 0; i < n; i++) {
            ((Rule) this.subRules.get(i)).onDestroy();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void scaleStrokeWidth(float scaleFactor) {
        int i;
        int n = this.renderInstructions.size();
        for (i = 0; i < n; i++) {
            ((RenderInstruction) this.renderInstructions.get(i)).scaleStrokeWidth(scaleFactor);
        }
        n = this.subRules.size();
        for (i = 0; i < n; i++) {
            ((Rule) this.subRules.get(i)).scaleStrokeWidth(scaleFactor);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void scaleTextSize(float scaleFactor) {
        int i;
        int n = this.renderInstructions.size();
        for (i = 0; i < n; i++) {
            ((RenderInstruction) this.renderInstructions.get(i)).scaleTextSize(scaleFactor);
        }
        n = this.subRules.size();
        for (i = 0; i < n; i++) {
            ((Rule) this.subRules.get(i)).scaleTextSize(scaleFactor);
        }
    }
}
