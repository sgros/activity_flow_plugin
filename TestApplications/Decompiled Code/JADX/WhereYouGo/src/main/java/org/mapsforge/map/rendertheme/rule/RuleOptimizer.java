package org.mapsforge.map.rendertheme.rule;

import java.util.Stack;
import java.util.logging.Logger;

final class RuleOptimizer {
    private static final Logger LOGGER = Logger.getLogger(RuleOptimizer.class.getName());

    private static AttributeMatcher optimizeKeyMatcher(AttributeMatcher attributeMatcher, Stack<Rule> ruleStack) {
        int i = 0;
        int n = ruleStack.size();
        while (i < n) {
            if ((ruleStack.get(i) instanceof PositiveRule) && ((PositiveRule) ruleStack.get(i)).keyMatcher.isCoveredBy(attributeMatcher)) {
                return AnyMatcher.INSTANCE;
            }
            i++;
        }
        return attributeMatcher;
    }

    private static AttributeMatcher optimizeValueMatcher(AttributeMatcher attributeMatcher, Stack<Rule> ruleStack) {
        int i = 0;
        int n = ruleStack.size();
        while (i < n) {
            if ((ruleStack.get(i) instanceof PositiveRule) && ((PositiveRule) ruleStack.get(i)).valueMatcher.isCoveredBy(attributeMatcher)) {
                return AnyMatcher.INSTANCE;
            }
            i++;
        }
        return attributeMatcher;
    }

    static AttributeMatcher optimize(AttributeMatcher attributeMatcher, Stack<Rule> ruleStack) {
        if ((attributeMatcher instanceof AnyMatcher) || (attributeMatcher instanceof NegativeMatcher)) {
            return attributeMatcher;
        }
        if (attributeMatcher instanceof KeyMatcher) {
            return optimizeKeyMatcher(attributeMatcher, ruleStack);
        }
        if (attributeMatcher instanceof ValueMatcher) {
            return optimizeValueMatcher(attributeMatcher, ruleStack);
        }
        throw new IllegalArgumentException("unknown AttributeMatcher: " + attributeMatcher);
    }

    static ClosedMatcher optimize(ClosedMatcher closedMatcher, Stack<Rule> ruleStack) {
        if (closedMatcher instanceof AnyMatcher) {
            return closedMatcher;
        }
        int n = ruleStack.size();
        for (int i = 0; i < n; i++) {
            if (((Rule) ruleStack.get(i)).closedMatcher.isCoveredBy(closedMatcher)) {
                return AnyMatcher.INSTANCE;
            }
            if (!closedMatcher.isCoveredBy(((Rule) ruleStack.get(i)).closedMatcher)) {
                LOGGER.warning("unreachable rule (closed)");
            }
        }
        return closedMatcher;
    }

    static ElementMatcher optimize(ElementMatcher elementMatcher, Stack<Rule> ruleStack) {
        if (elementMatcher instanceof AnyMatcher) {
            return elementMatcher;
        }
        int n = ruleStack.size();
        for (int i = 0; i < n; i++) {
            Rule rule = (Rule) ruleStack.get(i);
            if (rule.elementMatcher.isCoveredBy(elementMatcher)) {
                return AnyMatcher.INSTANCE;
            }
            if (!elementMatcher.isCoveredBy(rule.elementMatcher)) {
                LOGGER.warning("unreachable rule (e)");
            }
        }
        return elementMatcher;
    }

    private RuleOptimizer() {
        throw new IllegalStateException();
    }
}
