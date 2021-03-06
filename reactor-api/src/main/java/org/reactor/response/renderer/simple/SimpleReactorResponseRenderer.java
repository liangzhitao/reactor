package org.reactor.response.renderer.simple;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.ArrayListMultimap.create;
import static java.lang.String.format;

import java.io.PrintWriter;
import java.io.Writer;

import com.google.common.collect.Multimap;
import org.reactor.response.list.ListElementFormatter;
import org.reactor.response.renderer.AbstractAutoFlushableResponseRenderer;

public class SimpleReactorResponseRenderer extends AbstractAutoFlushableResponseRenderer {

    private static final String PROPERTY_TEXT = "text";
    private static final String PROPERTY_DOUBLE = "double";
    private static final String PROPERTY_LONG = "long";
    private static final String PROPERTY_LIST = "list";

    private String header;
    private Multimap<String, String> responseElements = create();

    @Override
    public void renderHeadLine(String headerTemplateToBeRendered, Object... templateParameters) {
        header = format(headerTemplateToBeRendered, templateParameters);
    }

    @Override
    public void renderTextLine(String lineId, String templateToBeRendered, Object... templateParameters) {
        responseElements.put(lineId, format(templateToBeRendered, templateParameters));
    }

    @Override
    public void renderTextLine(String templateToBeRendered, Object... templateParameters) {
        renderTextLine(PROPERTY_TEXT, templateToBeRendered, templateParameters);
    }

    @Override
    public <T> void renderListLine(String lineId, int index, T listElement, ListElementFormatter<T> formatter) {
        responseElements.put(lineId, formatter.formatListElement(index, listElement));
    }

    @Override
    public <T> void renderListLine(int index, T listElement, ListElementFormatter<T> formatter) {
        renderListLine(PROPERTY_LIST, index, listElement, formatter);
    }

    @Override
    public void renderDoubleLine(String lineId, double doubleValue) {
        responseElements.put(lineId, Double.toString(doubleValue));
    }

    @Override
    public void renderDoubleLine(double doubleValue) {
        renderDoubleLine(PROPERTY_DOUBLE, doubleValue);
    }

    @Override
    public void renderLongLine(String lineId, long longValue) {
        responseElements.put(lineId, Long.toString(longValue));
    }

    @Override
    public void renderLongLine(long longValue) {
        renderLongLine(PROPERTY_LONG, longValue);
    }

    @Override
    protected void commitBeforeFlush(Writer responseWriter) {
        PrintWriter printWriter = new PrintWriter(responseWriter);
        if (!isNullOrEmpty(header)) {
            printWriter.print(header);
        }
        responseElements.values().forEach(printWriter::print);
    }
}
