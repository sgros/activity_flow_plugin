package org.mapsforge.map.rendertheme.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Stack;
import java.util.regex.Pattern;
import org.mapsforge.map.rendertheme.XmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class RuleBuilder {
    private static final String CLOSED = "closed";
    /* renamed from: E */
    private static final String f76E = "e";
    /* renamed from: K */
    private static final String f77K = "k";
    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\|");
    private static final String STRING_NEGATION = "~";
    private static final String STRING_WILDCARD = "*";
    /* renamed from: V */
    private static final String f78V = "v";
    private static final String ZOOM_MAX = "zoom-max";
    private static final String ZOOM_MIN = "zoom-min";
    private Closed closed = Closed.ANY;
    ClosedMatcher closedMatcher;
    private Element element;
    ElementMatcher elementMatcher;
    private List<String> keyList;
    private String keys;
    private final Stack<Rule> ruleStack;
    private List<String> valueList;
    private String values;
    byte zoomMax = Byte.MAX_VALUE;
    byte zoomMin = (byte) 0;

    private static ClosedMatcher getClosedMatcher(Closed closed) {
        switch (closed) {
            case YES:
                return ClosedWayMatcher.INSTANCE;
            case NO:
                return LinearWayMatcher.INSTANCE;
            case ANY:
                return AnyMatcher.INSTANCE;
            default:
                throw new IllegalArgumentException("unknown closed value: " + closed);
        }
    }

    private static ElementMatcher getElementMatcher(Element element) {
        switch (element) {
            case NODE:
                return ElementNodeMatcher.INSTANCE;
            case WAY:
                return ElementWayMatcher.INSTANCE;
            case ANY:
                return AnyMatcher.INSTANCE;
            default:
                throw new IllegalArgumentException("unknown element value: " + element);
        }
    }

    private static AttributeMatcher getKeyMatcher(List<String> keyList) {
        if (STRING_WILDCARD.equals(keyList.get(0))) {
            return AnyMatcher.INSTANCE;
        }
        AttributeMatcher attributeMatcher = (AttributeMatcher) Rule.MATCHERS_CACHE_KEY.get(keyList);
        if (attributeMatcher != null) {
            return attributeMatcher;
        }
        attributeMatcher = new KeyMatcher(keyList);
        Rule.MATCHERS_CACHE_KEY.put(keyList, attributeMatcher);
        return attributeMatcher;
    }

    private static AttributeMatcher getValueMatcher(List<String> valueList) {
        if (STRING_WILDCARD.equals(valueList.get(0))) {
            return AnyMatcher.INSTANCE;
        }
        AttributeMatcher attributeMatcher = (AttributeMatcher) Rule.MATCHERS_CACHE_VALUE.get(valueList);
        if (attributeMatcher != null) {
            return attributeMatcher;
        }
        attributeMatcher = new ValueMatcher(valueList);
        Rule.MATCHERS_CACHE_VALUE.put(valueList, attributeMatcher);
        return attributeMatcher;
    }

    public RuleBuilder(String elementName, Attributes attributes, Stack<Rule> ruleStack) throws SAXException {
        this.ruleStack = ruleStack;
        extractValues(elementName, attributes);
    }

    public Rule build() {
        if (this.valueList.remove(STRING_NEGATION)) {
            return new NegativeRule(this, new NegativeMatcher(this.keyList, this.valueList));
        }
        return new PositiveRule(this, RuleOptimizer.optimize(getKeyMatcher(this.keyList), this.ruleStack), RuleOptimizer.optimize(getValueMatcher(this.valueList), this.ruleStack));
    }

    private void extractValues(String elementName, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String name = attributes.getQName(i);
            String value = attributes.getValue(i);
            if (f76E.equals(name)) {
                this.element = Element.valueOf(value.toUpperCase(Locale.ENGLISH));
            } else if (f77K.equals(name)) {
                this.keys = value;
            } else if (f78V.equals(name)) {
                this.values = value;
            } else if (CLOSED.equals(name)) {
                this.closed = Closed.valueOf(value.toUpperCase(Locale.ENGLISH));
            } else if (ZOOM_MIN.equals(name)) {
                this.zoomMin = XmlUtils.parseNonNegativeByte(name, value);
            } else if (ZOOM_MAX.equals(name)) {
                this.zoomMax = XmlUtils.parseNonNegativeByte(name, value);
            } else {
                throw XmlUtils.createSAXException(elementName, name, value, i);
            }
        }
        validate(elementName);
        this.keyList = new ArrayList(Arrays.asList(SPLIT_PATTERN.split(this.keys)));
        this.valueList = new ArrayList(Arrays.asList(SPLIT_PATTERN.split(this.values)));
        this.elementMatcher = getElementMatcher(this.element);
        this.closedMatcher = getClosedMatcher(this.closed);
        this.elementMatcher = RuleOptimizer.optimize(this.elementMatcher, this.ruleStack);
        this.closedMatcher = RuleOptimizer.optimize(this.closedMatcher, this.ruleStack);
    }

    private void validate(String elementName) throws SAXException {
        XmlUtils.checkMandatoryAttribute(elementName, f76E, this.element);
        XmlUtils.checkMandatoryAttribute(elementName, f77K, this.keys);
        XmlUtils.checkMandatoryAttribute(elementName, f78V, this.values);
        if (this.zoomMin > this.zoomMax) {
            throw new SAXException("'zoom-min' > 'zoom-max': " + this.zoomMin + ' ' + this.zoomMax);
        }
    }
}
