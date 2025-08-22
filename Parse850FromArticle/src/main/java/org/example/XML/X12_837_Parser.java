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
 * The X12_837_Parser class provides methods for parsing, converting, and
 * serializing EDI (Electronic Data Interchange) formats, specifically focusing
 * on the X12 837 Healthcare Claim transactions. It leverages XML, JSON, and YAML
 * mappers for format transformations.
 */
@Slf4j
public class X12_837_Parser {

    private static final XmlMapper xmlMapper = new XmlMapper();

    private static final JsonMapper jsonMapper = new JsonMapper();

    private static final YAMLMapper yamlMapper = new YAMLMapper();

    static {
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Configure XML mapper to not include null or empty values
        xmlMapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
        xmlMapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY);
    }

    /**
     * Parses the given X12 837 EDI string and returns its XML representation.
     *
     * @param ediString The input EDI string to be parsed into XML format.
     * @return A string containing the XML representation of the provided EDI input.
     * @throws IOException  If an I/O error occurs during the parsing process.
     * @throws SAXException If an error occurs while parsing the EDI input.
     */
    public static String parseEDI(String ediString) throws IOException, SAXException {
        return parseEDI(ediString.getBytes());
    }

    /**
     * Converts the given EDI data in byte array format into an XML string representation.
     *
     * @param ediInput The input EDI data as a byte array that needs to be converted to XML.
     * @return A string containing the XML representation of the given EDI data.
     * @throws IOException  If an I/O error occurs during the conversion process.
     * @throws SAXException If an error occurs while parsing the EDI input.
     */
    public static String parseEDI(byte[] ediInput) throws IOException, SAXException {
        StringSink result;
        try (Smooks ediToXml = new Smooks("parse-837-config.xml")) {
            log.debug("Loaded EDI input file with {} bytes", ediInput.length);

            result = new StringSink();
            ediToXml.filterSource(new StreamSource<>(new ByteArrayInputStream(ediInput)), result);
        }
        String xmlResult = result.getResult();
        log.info("Successfully converted EDI to XML");
        log.debug("XML result: {}", xmlResult);
        System.out.printf("Converted to XML:%s %n", xmlResult);
        return xmlResult;
    }

    /**
     * Parse XML string into X12_837_Interchange object.
     *
     * @param xml The XML string to parse
     * @return The parsed X12_837_Interchange object
     * @throws IOException If parsing fails
     */
    public static X12_837_Interchange parseXML(String xml) throws IOException {
        try {
            log.debug("Parsing XML to X12_837_Interchange: {}", xml);
            X12_837_Interchange result = xmlMapper.readValue(xml, X12_837_Interchange.class);
            log.debug("Successfully parsed XML to X12_837_Interchange");
            return result;
        } catch (Exception e) {
            log.error("Error parsing XML to X12_837_Interchange: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Convert X12_837_Interchange object to XML string.
     *
     * @param interchange The X12_837_Interchange object to convert
     * @return The XML string representation
     * @throws IOException If conversion fails
     */
    public static String toXml(X12_837_Interchange interchange) throws IOException {
        try {
            log.debug("Converting X12_837_Interchange to XML");
            String result = xmlMapper.writeValueAsString(interchange);
            log.debug("Successfully converted X12_837_Interchange to XML: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error converting X12_837_Interchange to XML: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Convert X12_837_Interchange object to JSON string.
     *
     * @param interchange The X12_837_Interchange object to convert
     * @return The JSON string representation
     * @throws IOException If conversion fails
     */
    public static String toJson(X12_837_Interchange interchange) throws IOException {
        try {
            log.debug("Converting X12_837_Interchange to JSON");
            String result = jsonMapper.writeValueAsString(interchange);
            log.debug("Successfully converted X12_837_Interchange to JSON: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error converting X12_837_Interchange to JSON: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Converts a given X12_837_Interchange object to its YAML string representation.
     *
     * @param interchange The X12_837_Interchange object to convert into YAML format.
     * @return A string representing the YAML serialization of the given X12_837_Interchange object.
     * @throws IOException If an error occurs during the YAML conversion process.
     */
    public static String toYaml(X12_837_Interchange interchange) throws IOException {
        String result = null;
        try {
            log.debug("Converting X12_837_Interchange to YAML");
            result = yamlMapper.writeValueAsString(interchange);
            log.debug("Successfully converted X12_837_Interchange to YAML: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error converting X12_837_Interchange to YAML: {}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * Converts the given XML string into its EDI representation.
     *
     * @param xmlResult The input XML string to be converted into EDI format.
     * @return A string containing the EDI representation of the provided XML input.
     * @throws IOException  If an I/O error occurs during the conversion process.
     * @throws SAXException If an error occurs while parsing the XML input.
     */
    public static String xmlToEDI(String xmlResult) throws IOException, SAXException {
        StringSink ediResult;
        try (Smooks xmlToEdi = new Smooks("serialize-837-config.xml")) {

            xmlToEdi.setFilterSettings(FilterSettings.newSaxNgSettings().setDefaultSerializationOn(false));
            final byte[] xmlInput = xmlResult.getBytes();
            log.debug("Prepared XML input with {} bytes", xmlInput.length);

            ediResult = new StringSink();
            xmlToEdi.filterSource(new StreamSource<>(new ByteArrayInputStream(xmlInput)), ediResult);
        }
        log.info("Successfully converted XML back to EDI");
        log.debug("EDI result: {}", ediResult.getResult());
        return ediResult.toString();
    }

    /**
     * Converts the given {@link X12_837_Interchange} object into its EDI string representation.
     *
     * @param interchange The X12_837_Interchange object to be converted into an EDI string.
     * @return A string containing the EDI representation of the provided X12_837_Interchange object.
     * @throws IOException  If an I/O error occurs during the conversion process.
     * @throws SAXException If an error occurs while parsing the intermediate XML representation.
     */
    public static String toEdiString(X12_837_Interchange interchange) throws IOException, SAXException {
        return xmlToEDI(toXml(interchange));
    }

}