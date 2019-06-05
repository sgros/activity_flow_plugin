package org.mapsforge.map.rendertheme.rule;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.mapsforge.core.util.IOUtils;
import org.mapsforge.map.rendertheme.GraphicAdapter;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public final class RenderThemeHandler extends DefaultHandler {
   private static final String ELEMENT_NAME_RULE = "rule";
   private static final Logger LOGGER = Logger.getLogger(RenderThemeHandler.class.getName());
   private static final String UNEXPECTED_ELEMENT = "unexpected element: ";
   private Rule currentRule;
   private final Stack elementStack = new Stack();
   private final GraphicAdapter graphicAdapter;
   private int level;
   private final String relativePathPrefix;
   private RenderTheme renderTheme;
   private final Stack ruleStack = new Stack();

   private RenderThemeHandler(GraphicAdapter var1, String var2) {
      this.graphicAdapter = var1;
      this.relativePathPrefix = var2;
   }

   private void checkElement(String var1, RenderThemeHandler.Element var2) throws SAXException {
      switch(var2) {
      case RENDER_THEME:
         if (!this.elementStack.empty()) {
            throw new SAXException("unexpected element: " + var1);
         }
         break;
      case RULE:
         var2 = (RenderThemeHandler.Element)this.elementStack.peek();
         if (var2 != RenderThemeHandler.Element.RENDER_THEME && var2 != RenderThemeHandler.Element.RULE) {
            throw new SAXException("unexpected element: " + var1);
         }
         break;
      case RENDERING_INSTRUCTION:
         if (this.elementStack.peek() != RenderThemeHandler.Element.RULE) {
            throw new SAXException("unexpected element: " + var1);
         }
         break;
      default:
         throw new SAXException("unknown enum value: " + var2);
      }

   }

   private void checkState(String var1, RenderThemeHandler.Element var2) throws SAXException {
      this.checkElement(var1, var2);
      this.elementStack.push(var2);
   }

   public static RenderTheme getRenderTheme(GraphicAdapter var0, XmlRenderTheme var1) throws SAXException, ParserConfigurationException, IOException {
      RenderThemeHandler var2 = new RenderThemeHandler(var0, var1.getRelativePathPrefix());
      XMLReader var3 = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
      var3.setContentHandler(var2);
      InputStream var35 = null;

      InputStream var36;
      RenderTheme var38;
      label266: {
         Throwable var10000;
         label270: {
            boolean var10001;
            try {
               var36 = var1.getRenderThemeAsStream();
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label270;
            }

            var35 = var36;

            InputSource var4;
            try {
               var4 = new InputSource;
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label270;
            }

            var35 = var36;

            try {
               var4.<init>(var36);
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label270;
            }

            var35 = var36;

            try {
               var3.parse(var4);
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label270;
            }

            var35 = var36;

            label249:
            try {
               var38 = var2.renderTheme;
               break label266;
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break label249;
            }
         }

         Throwable var37 = var10000;
         IOUtils.closeQuietly(var35);
         throw var37;
      }

      IOUtils.closeQuietly(var36);
      return var38;
   }

   public void endDocument() {
      if (this.renderTheme == null) {
         throw new IllegalArgumentException("missing element: rules");
      } else {
         this.renderTheme.setLevels(this.level);
         this.renderTheme.complete();
      }
   }

   public void endElement(String var1, String var2, String var3) {
      this.elementStack.pop();
      if ("rule".equals(var3)) {
         this.ruleStack.pop();
         if (this.ruleStack.empty()) {
            this.renderTheme.addRule(this.currentRule);
         } else {
            this.currentRule = (Rule)this.ruleStack.peek();
         }
      }

   }

   public void error(SAXParseException var1) {
      LOGGER.log(Level.SEVERE, (String)null, var1);
   }

   public void startElement(String param1, String param2, String param3, Attributes param4) throws SAXException {
      // $FF: Couldn't be decompiled
   }

   public void warning(SAXParseException var1) {
      LOGGER.log(Level.SEVERE, (String)null, var1);
   }

   private static enum Element {
      RENDERING_INSTRUCTION,
      RENDER_THEME,
      RULE;
   }
}
