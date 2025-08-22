package org.example.XML;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import lombok.extern.slf4j.Slf4j;
import org.smooks.FilterSettings;
import org.smooks.Smooks;
import org.smooks.io.sink.StringSink;
import org.smooks.io.source.StreamSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * X12_835_Parser provides EDI<->XML conversion for HIPAA 5010 X12 835
 * using Smooks 2.x and the 835_mapping.dfdl.xsd mapping.
 */
@Slf4j
public class X12_835_Parser {

    /**
     * Static instance of {@code XmlMapper} used for handling XML serialization and deserialization.
     * This mapper is utilized for converting XML strings to Java objects and vice versa,
     * particularly in the context of the X12 835 EDI parsing and serialization processes.
     */
    private static final XmlMapper xmlMapper = new XmlMapper();

    /**
     * A static instance of the {@code JsonMapper} class used for handling JSON
     * serialization and deserialization operations. This mapper serves as a
     * utility within the enclosing class to convert or process JSON data
     * efficiently and consistently across various methods.
     */
    private static final JsonMapper jsonMapper = new JsonMapper();


    /**
     * A static instance of the YAMLMapper class used for handling YAML serialization
     * and deserialization operations. This mapper is specifically configured to process
     * YAML data structures, allowing for conversion between Java objects and YAML representations.
     * It is used in various methods of the X12_850_Parser class to convert
     * X12_850_Interchange objects to and from YAML format.
     */
    private static final YAMLMapper yamlMapper = new YAMLMapper();

    static {
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Parse an X12 835 EDI string into XML using Smooks and the 835 DFDL mapping.
     */
    public static String parseEDI(String edi) throws IOException, SAXException {
        return parseEDI(edi.getBytes());
    }

    /**
     * Parse an X12 835 EDI byte[] into XML using Smooks and the 835 DFDL mapping.
     */
    public static String parseEDI(byte[] ediBytes) throws IOException, SAXException {
        try (Smooks smooks = new Smooks("parse-835-config.xml")) {
            log.debug("Parsing 835 EDI input ({} bytes)...", ediBytes.length);
            StringSink result = new StringSink();
            smooks.filterSource(new StreamSource<>(new ByteArrayInputStream(ediBytes)), result);
            String xml = result.getResult();
            log.info("Successfully converted 835 EDI to XML.");
            log.debug("835 XML:\n{}", xml);
            return xml;
        } catch (Exception ex) {
            // Surface parsing errors so we can fix schema/config issues (no fallback).
            log.error("Smooks failed to parse 835 EDI.", ex);
            throw ex;
        }
    }

    /**
     * Serialize XML (conforming to 835_mapping.dfdl.xsd) back to X12 835 EDI using Smooks.
     */
    public static String toEDIString(String xml) throws IOException, SAXException {
        try (Smooks smooks = new Smooks("serialize-835-config.xml")) {
            smooks.setFilterSettings(FilterSettings.newSaxNgSettings().setDefaultSerializationOn(false));
            final byte[] xmlBytes = xml.getBytes();
            log.debug("Serializing 835 XML input ({} bytes)...", xmlBytes.length);
            log.debug("Input XML structure: {}", xml.substring(0, Math.min(500, xml.length())));

            // Validate XML structure before processing
            if (!xml.contains("<interchange-header>")) {
                log.error("XML is missing required interchange-header element");
                throw new IllegalArgumentException("XML must contain interchange-header element as defined in 835_mapping.dfdl.xsd");
            }

            StringSink ediResult = new StringSink();
            smooks.filterSource(new StreamSource<>(new ByteArrayInputStream(xmlBytes)), ediResult);
            String edi = ediResult.getResult();
            log.info("Successfully converted XML to 835 EDI.");
            log.debug("835 EDI:\n{}", edi);
            return edi;
        } catch (Exception ex) {
            // Surface serialization errors as well to ensure schema/config are correct.
            log.error("Smooks failed to serialize 835 XML. Input XML: {}",
                    xml.substring(0, Math.min(200, xml.length())), ex);
            throw ex;
        }
    }

    /**
     * Parse XML string into X12_835_Interchange object.
     *
     * @param xml The XML string to parse
     * @return The parsed X12_835_Interchange object
     * @throws IOException If parsing fails
     */
    public static X12_835_Interchange parseXML(String xml) throws IOException {
        try {
            log.debug("Parsing XML to X12_835_Interchange: {}", xml);
            X12_835_Interchange result = xmlMapper.readValue(xml, X12_835_Interchange.class);
            log.debug("Successfully parsed XML to X12_850_Interchange");
            return result;
        } catch (Exception e) {
            log.error("Error parsing XML to X12_850_Interchange: {}", e.getMessage(), e);
            throw e;
        }
    }


    /**
     * Convert X12_835_Interchange object to XML string.
     *
     * @param interchange The X12_835_Interchange object to convert
     * @return The XML string representation
     * @throws IOException If conversion fails
     */
    public static String toXml(X12_835_Interchange interchange) throws IOException {
        try {
            log.debug("Converting X12_850_Interchange to XML");
            String result = xmlMapper.writeValueAsString(interchange);
            log.debug("Successfully converted X12_850_Interchange to XML: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error converting X12_850_Interchange to XML: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Convert X12_835_Interchange object to JSON string.
     *
     * @param interchange The X12_835_Interchange object to convert
     * @return The JSON string representation
     * @throws IOException If conversion fails
     */
    public static String toJson(X12_835_Interchange interchange) throws IOException {
        try {
            log.debug("Converting X12_850_Interchange to JSON");
            String result = jsonMapper.writeValueAsString(interchange);
            log.debug("Successfully converted X12_850_Interchange to JSON: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error converting X12_850_Interchange to JSON: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Converts a given X12_850_Interchange object to its YAML string representation.
     *
     * @param interchange The X12_850_Interchange object to convert into YAML format.
     * @return A string representing the YAML serialization of the given X12_850_Interchange object.
     * @throws IOException If an error occurs during the YAML conversion process.
     */
    public static String toYaml(X12_835_Interchange interchange) throws IOException {
        String result = null;
        try {
            log.debug("Converting X12_850_Interchange to YAML");
            result = yamlMapper.writeValueAsString(interchange);
            log.debug("Successfully converted X12_850_Interchange to YAML: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error converting X12_850_Interchange to YAML: {}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * Converts the given {@link X12_835_Interchange} object into its EDI string representation.
     *
     * @param interchange The X12_850_Interchange object to be converted into an EDI string.
     * @return A string containing the EDI representation of the provided X12_850_Interchange object.
     * @throws IOException  If an I/O error occurs during the conversion process.
     * @throws SAXException If an error occurs while parsing the intermediate XML representation.
     */
    public static String toEDIString(X12_835_Interchange interchange) throws IOException, SAXException {
        return toEDIString(toXml(interchange));
    }

}
