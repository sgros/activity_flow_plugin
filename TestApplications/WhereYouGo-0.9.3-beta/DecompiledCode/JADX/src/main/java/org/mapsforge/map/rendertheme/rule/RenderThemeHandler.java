package org.mapsforge.map.rendertheme.rule;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import locus.api.android.features.geocaching.fieldNotes.FieldNotesHelper.ColFieldNoteImage;
import org.mapsforge.core.util.IOUtils;
import org.mapsforge.map.rendertheme.GraphicAdapter;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.renderinstruction.AreaBuilder;
import org.mapsforge.map.rendertheme.renderinstruction.CaptionBuilder;
import org.mapsforge.map.rendertheme.renderinstruction.CircleBuilder;
import org.mapsforge.map.rendertheme.renderinstruction.LineBuilder;
import org.mapsforge.map.rendertheme.renderinstruction.LineSymbolBuilder;
import org.mapsforge.map.rendertheme.renderinstruction.PathTextBuilder;
import org.mapsforge.map.rendertheme.renderinstruction.SymbolBuilder;
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
    private final Stack<Element> elementStack = new Stack();
    private final GraphicAdapter graphicAdapter;
    private int level;
    private final String relativePathPrefix;
    private RenderTheme renderTheme;
    private final Stack<Rule> ruleStack = new Stack();

    private enum Element {
        RENDER_THEME,
        RENDERING_INSTRUCTION,
        RULE
    }

    public static RenderTheme getRenderTheme(GraphicAdapter graphicAdapter, XmlRenderTheme xmlRenderTheme) throws SAXException, ParserConfigurationException, IOException {
        RenderThemeHandler renderThemeHandler = new RenderThemeHandler(graphicAdapter, xmlRenderTheme.getRelativePathPrefix());
        XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        xmlReader.setContentHandler(renderThemeHandler);
        InputStream inputStream = null;
        try {
            inputStream = xmlRenderTheme.getRenderThemeAsStream();
            xmlReader.parse(new InputSource(inputStream));
            RenderTheme renderTheme = renderThemeHandler.renderTheme;
            return renderTheme;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private RenderThemeHandler(GraphicAdapter graphicAdapter, String relativePathPrefix) {
        this.graphicAdapter = graphicAdapter;
        this.relativePathPrefix = relativePathPrefix;
    }

    public void endDocument() {
        if (this.renderTheme == null) {
            throw new IllegalArgumentException("missing element: rules");
        }
        this.renderTheme.setLevels(this.level);
        this.renderTheme.complete();
    }

    public void endElement(String uri, String localName, String qName) {
        this.elementStack.pop();
        if (ELEMENT_NAME_RULE.equals(qName)) {
            this.ruleStack.pop();
            if (this.ruleStack.empty()) {
                this.renderTheme.addRule(this.currentRule);
            } else {
                this.currentRule = (Rule) this.ruleStack.peek();
            }
        }
    }

    public void error(SAXParseException exception) {
        LOGGER.log(Level.SEVERE, null, exception);
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            GraphicAdapter graphicAdapter;
            int i;
            if ("rendertheme".equals(qName)) {
                checkState(qName, Element.RENDER_THEME);
                this.renderTheme = new RenderThemeBuilder(this.graphicAdapter, qName, attributes).build();
            } else if (ELEMENT_NAME_RULE.equals(qName)) {
                checkState(qName, Element.RULE);
                Rule rule = new RuleBuilder(qName, attributes, this.ruleStack).build();
                if (!this.ruleStack.empty()) {
                    this.currentRule.addSubRule(rule);
                }
                this.currentRule = rule;
                this.ruleStack.push(this.currentRule);
            } else if ("area".equals(qName)) {
                checkState(qName, Element.RENDERING_INSTRUCTION);
                graphicAdapter = this.graphicAdapter;
                i = this.level;
                this.level = i + 1;
                this.currentRule.addRenderingInstruction(new AreaBuilder(graphicAdapter, qName, attributes, i, this.relativePathPrefix).build());
            } else if (ColFieldNoteImage.CAPTION.equals(qName)) {
                checkState(qName, Element.RENDERING_INSTRUCTION);
                this.currentRule.addRenderingInstruction(new CaptionBuilder(this.graphicAdapter, qName, attributes).build());
            } else if ("circle".equals(qName)) {
                checkState(qName, Element.RENDERING_INSTRUCTION);
                graphicAdapter = this.graphicAdapter;
                int i2 = this.level;
                this.level = i2 + 1;
                this.currentRule.addRenderingInstruction(new CircleBuilder(graphicAdapter, qName, attributes, i2).build());
            } else if ("line".equals(qName)) {
                checkState(qName, Element.RENDERING_INSTRUCTION);
                graphicAdapter = this.graphicAdapter;
                i = this.level;
                this.level = i + 1;
                this.currentRule.addRenderingInstruction(new LineBuilder(graphicAdapter, qName, attributes, i, this.relativePathPrefix).build());
            } else if ("lineSymbol".equals(qName)) {
                checkState(qName, Element.RENDERING_INSTRUCTION);
                this.currentRule.addRenderingInstruction(new LineSymbolBuilder(this.graphicAdapter, qName, attributes, this.relativePathPrefix).build());
            } else if ("pathText".equals(qName)) {
                checkState(qName, Element.RENDERING_INSTRUCTION);
                this.currentRule.addRenderingInstruction(new PathTextBuilder(this.graphicAdapter, qName, attributes).build());
            } else if ("symbol".equals(qName)) {
                checkState(qName, Element.RENDERING_INSTRUCTION);
                this.currentRule.addRenderingInstruction(new SymbolBuilder(this.graphicAdapter, qName, attributes, this.relativePathPrefix).build());
            } else {
                throw new SAXException("unknown element: " + qName);
            }
        } catch (IOException e) {
            throw new SAXException(null, e);
        }
    }

    public void warning(SAXParseException exception) {
        LOGGER.log(Level.SEVERE, null, exception);
    }

    private void checkElement(String elementName, Element element) throws SAXException {
        switch (element) {
            case RENDER_THEME:
                if (!this.elementStack.empty()) {
                    throw new SAXException(UNEXPECTED_ELEMENT + elementName);
                }
                return;
            case RULE:
                Element parentElement = (Element) this.elementStack.peek();
                if (parentElement != Element.RENDER_THEME && parentElement != Element.RULE) {
                    throw new SAXException(UNEXPECTED_ELEMENT + elementName);
                }
                return;
            case RENDERING_INSTRUCTION:
                if (this.elementStack.peek() != Element.RULE) {
                    throw new SAXException(UNEXPECTED_ELEMENT + elementName);
                }
                return;
            default:
                throw new SAXException("unknown enum value: " + element);
        }
    }

    private void checkState(String elementName, Element element) throws SAXException {
        checkElement(elementName, element);
        this.elementStack.push(element);
    }
}
