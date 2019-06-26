// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme.rule;

import org.mapsforge.core.model.Tag;
import org.mapsforge.map.rendertheme.RenderCallback;
import java.util.HashMap;
import org.mapsforge.map.rendertheme.renderinstruction.RenderInstruction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

abstract class Rule
{
    static final Map<List<String>, AttributeMatcher> MATCHERS_CACHE_KEY;
    static final Map<List<String>, AttributeMatcher> MATCHERS_CACHE_VALUE;
    final ClosedMatcher closedMatcher;
    final ElementMatcher elementMatcher;
    private final ArrayList<RenderInstruction> renderInstructions;
    private final ArrayList<Rule> subRules;
    final byte zoomMax;
    final byte zoomMin;
    
    static {
        MATCHERS_CACHE_KEY = new HashMap<List<String>, AttributeMatcher>();
        MATCHERS_CACHE_VALUE = new HashMap<List<String>, AttributeMatcher>();
    }
    
    Rule(final RuleBuilder ruleBuilder) {
        this.closedMatcher = ruleBuilder.closedMatcher;
        this.elementMatcher = ruleBuilder.elementMatcher;
        this.zoomMax = ruleBuilder.zoomMax;
        this.zoomMin = ruleBuilder.zoomMin;
        this.renderInstructions = new ArrayList<RenderInstruction>(4);
        this.subRules = new ArrayList<Rule>(4);
    }
    
    void addRenderingInstruction(final RenderInstruction e) {
        this.renderInstructions.add(e);
    }
    
    void addSubRule(final Rule e) {
        this.subRules.add(e);
    }
    
    void matchNode(final RenderCallback renderCallback, final List<Tag> list, final byte b) {
        if (this.matchesNode(list, b)) {
            for (int i = 0; i < this.renderInstructions.size(); ++i) {
                this.renderInstructions.get(i).renderNode(renderCallback, list);
            }
            for (int j = 0; j < this.subRules.size(); ++j) {
                this.subRules.get(j).matchNode(renderCallback, list, b);
            }
        }
    }
    
    void matchWay(final RenderCallback renderCallback, final List<Tag> list, final byte b, final Closed closed, final List<RenderInstruction> list2) {
        if (this.matchesWay(list, b, closed)) {
            for (int i = 0; i < this.renderInstructions.size(); ++i) {
                this.renderInstructions.get(i).renderWay(renderCallback, list);
                list2.add(this.renderInstructions.get(i));
            }
            for (int j = 0; j < this.subRules.size(); ++j) {
                this.subRules.get(j).matchWay(renderCallback, list, b, closed, list2);
            }
        }
    }
    
    abstract boolean matchesNode(final List<Tag> p0, final byte p1);
    
    abstract boolean matchesWay(final List<Tag> p0, final byte p1, final Closed p2);
    
    void onComplete() {
        Rule.MATCHERS_CACHE_KEY.clear();
        Rule.MATCHERS_CACHE_VALUE.clear();
        this.renderInstructions.trimToSize();
        this.subRules.trimToSize();
        for (int i = 0; i < this.subRules.size(); ++i) {
            this.subRules.get(i).onComplete();
        }
    }
    
    void onDestroy() {
        for (int i = 0; i < this.renderInstructions.size(); ++i) {
            this.renderInstructions.get(i).destroy();
        }
        for (int j = 0; j < this.subRules.size(); ++j) {
            this.subRules.get(j).onDestroy();
        }
    }
    
    void scaleStrokeWidth(final float n) {
        for (int i = 0; i < this.renderInstructions.size(); ++i) {
            this.renderInstructions.get(i).scaleStrokeWidth(n);
        }
        for (int j = 0; j < this.subRules.size(); ++j) {
            this.subRules.get(j).scaleStrokeWidth(n);
        }
    }
    
    void scaleTextSize(final float n) {
        for (int i = 0; i < this.renderInstructions.size(); ++i) {
            this.renderInstructions.get(i).scaleTextSize(n);
        }
        for (int j = 0; j < this.subRules.size(); ++j) {
            this.subRules.get(j).scaleTextSize(n);
        }
    }
}
