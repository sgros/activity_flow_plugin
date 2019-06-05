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
   private static final String E = "e";
   private static final String K = "k";
   private static final Pattern SPLIT_PATTERN = Pattern.compile("\\|");
   private static final String STRING_NEGATION = "~";
   private static final String STRING_WILDCARD = "*";
   private static final String V = "v";
   private static final String ZOOM_MAX = "zoom-max";
   private static final String ZOOM_MIN = "zoom-min";
   private Closed closed;
   ClosedMatcher closedMatcher;
   private Element element;
   ElementMatcher elementMatcher;
   private List keyList;
   private String keys;
   private final Stack ruleStack;
   private List valueList;
   private String values;
   byte zoomMax;
   byte zoomMin;

   public RuleBuilder(String var1, Attributes var2, Stack var3) throws SAXException {
      this.ruleStack = var3;
      this.closed = Closed.ANY;
      this.zoomMin = (byte)0;
      this.zoomMax = (byte)127;
      this.extractValues(var1, var2);
   }

   private void extractValues(String var1, Attributes var2) throws SAXException {
      for(int var3 = 0; var3 < var2.getLength(); ++var3) {
         String var4 = var2.getQName(var3);
         String var5 = var2.getValue(var3);
         if ("e".equals(var4)) {
            this.element = Element.valueOf(var5.toUpperCase(Locale.ENGLISH));
         } else if ("k".equals(var4)) {
            this.keys = var5;
         } else if ("v".equals(var4)) {
            this.values = var5;
         } else if ("closed".equals(var4)) {
            this.closed = Closed.valueOf(var5.toUpperCase(Locale.ENGLISH));
         } else if ("zoom-min".equals(var4)) {
            this.zoomMin = XmlUtils.parseNonNegativeByte(var4, var5);
         } else {
            if (!"zoom-max".equals(var4)) {
               throw XmlUtils.createSAXException(var1, var4, var5, var3);
            }

            this.zoomMax = XmlUtils.parseNonNegativeByte(var4, var5);
         }
      }

      this.validate(var1);
      this.keyList = new ArrayList(Arrays.asList(SPLIT_PATTERN.split(this.keys)));
      this.valueList = new ArrayList(Arrays.asList(SPLIT_PATTERN.split(this.values)));
      this.elementMatcher = getElementMatcher(this.element);
      this.closedMatcher = getClosedMatcher(this.closed);
      this.elementMatcher = RuleOptimizer.optimize(this.elementMatcher, this.ruleStack);
      this.closedMatcher = RuleOptimizer.optimize(this.closedMatcher, this.ruleStack);
   }

   private static ClosedMatcher getClosedMatcher(Closed var0) {
      Object var1;
      switch(var0) {
      case YES:
         var1 = ClosedWayMatcher.INSTANCE;
         break;
      case NO:
         var1 = LinearWayMatcher.INSTANCE;
         break;
      case ANY:
         var1 = AnyMatcher.INSTANCE;
         break;
      default:
         throw new IllegalArgumentException("unknown closed value: " + var0);
      }

      return (ClosedMatcher)var1;
   }

   private static ElementMatcher getElementMatcher(Element var0) {
      Object var1;
      switch(var0) {
      case NODE:
         var1 = ElementNodeMatcher.INSTANCE;
         break;
      case WAY:
         var1 = ElementWayMatcher.INSTANCE;
         break;
      case ANY:
         var1 = AnyMatcher.INSTANCE;
         break;
      default:
         throw new IllegalArgumentException("unknown element value: " + var0);
      }

      return (ElementMatcher)var1;
   }

   private static AttributeMatcher getKeyMatcher(List var0) {
      Object var1;
      if ("*".equals(var0.get(0))) {
         var1 = AnyMatcher.INSTANCE;
      } else {
         AttributeMatcher var2 = (AttributeMatcher)Rule.MATCHERS_CACHE_KEY.get(var0);
         var1 = var2;
         if (var2 == null) {
            var1 = new KeyMatcher(var0);
            Rule.MATCHERS_CACHE_KEY.put(var0, var1);
         }
      }

      return (AttributeMatcher)var1;
   }

   private static AttributeMatcher getValueMatcher(List var0) {
      Object var1;
      if ("*".equals(var0.get(0))) {
         var1 = AnyMatcher.INSTANCE;
      } else {
         AttributeMatcher var2 = (AttributeMatcher)Rule.MATCHERS_CACHE_VALUE.get(var0);
         var1 = var2;
         if (var2 == null) {
            var1 = new ValueMatcher(var0);
            Rule.MATCHERS_CACHE_VALUE.put(var0, var1);
         }
      }

      return (AttributeMatcher)var1;
   }

   private void validate(String var1) throws SAXException {
      XmlUtils.checkMandatoryAttribute(var1, "e", this.element);
      XmlUtils.checkMandatoryAttribute(var1, "k", this.keys);
      XmlUtils.checkMandatoryAttribute(var1, "v", this.values);
      if (this.zoomMin > this.zoomMax) {
         throw new SAXException("'zoom-min' > 'zoom-max': " + this.zoomMin + ' ' + this.zoomMax);
      }
   }

   public Rule build() {
      Object var1;
      if (this.valueList.remove("~")) {
         var1 = new NegativeRule(this, new NegativeMatcher(this.keyList, this.valueList));
      } else {
         AttributeMatcher var3 = getKeyMatcher(this.keyList);
         AttributeMatcher var2 = getValueMatcher(this.valueList);
         var1 = new PositiveRule(this, RuleOptimizer.optimize(var3, this.ruleStack), RuleOptimizer.optimize(var2, this.ruleStack));
      }

      return (Rule)var1;
   }
}
