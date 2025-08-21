package org.example.XML;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test class for X12_837_Parser functionality.
 */
public class X12_837_ParserTest {

    /**
     * Sample X12 835 EDI message for testing.
     * This is a simplified example of an 835 Healthcare Claim Payment/Advice transaction.
     */
    private static final String SAMPLE_835_EDI = 
        "ISA*00*          *00*          *ZZ*SENDER123      *ZZ*RECEIVER456    *210901*1200*^*00501*000000001*0*P*:~" +
        "GS*HP*SENDER123*RECEIVER456*20210901*1200*1*X*005010X221A1~" +
        "ST*835*0001*005010X221A1~" +
        "SE*6*0001~" +
        "GE*1*1~" +
        "IEA*1*000000001~";

    @Test
    public void testParseEDIToXML() {
        try {
            String xml = X12_837_Parser.parseEDI(SAMPLE_835_EDI);
            assertNotNull("XML result should not be null", xml);
            assertTrue("XML should contain root element", xml.contains("X12_835_Interchange"));
            assertTrue("XML should contain interchange header", xml.contains("interchange-header"));
            System.out.println("Parsed XML: " + xml);
        } catch (IOException | SAXException e) {
            fail("Failed to parse EDI to XML: " + e.getMessage());
        }
    }

    @Test
    public void testParseXMLToObject() {
        try {
            String xml = X12_837_Parser.parseEDI(SAMPLE_835_EDI);
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            
            assertNotNull("Interchange object should not be null", interchange);
            assertNotNull("Interchange header should not be null", interchange.getInterchangeHeader());
            assertNotNull("Group header should not be null", interchange.getGroupHeader());
            assertNotNull("Transaction set header should not be null", interchange.getTransactionSetHeader());
            
            assertEquals("00", interchange.getInterchangeHeader().getAuthQual());
            assertEquals("ZZ", interchange.getInterchangeHeader().getSenderQual());
            assertEquals("SENDER123", interchange.getInterchangeHeader().getSenderId().trim());
            assertEquals("835", interchange.getTransactionSetHeader().getCode());
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse XML to object: " + e.getMessage());
        }
    }

    @Test
    public void testObjectToJson() {
        try {
            String xml = X12_837_Parser.parseEDI(SAMPLE_835_EDI);
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            
            String json = X12_837_Parser.toJson(interchange);
            assertNotNull("JSON result should not be null", json);
            assertTrue("JSON should contain transaction code", json.contains("\"code\":\"835\""));
            assertTrue("JSON should contain interchange header", json.contains("interchange-header"));
            
            System.out.println("JSON output: " + json);
            
        } catch (IOException | SAXException e) {
            fail("Failed to convert to JSON: " + e.getMessage());
        }
    }

    @Test
    public void testObjectToYaml() {
        try {
            String xml = X12_837_Parser.parseEDI(SAMPLE_835_EDI);
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            
            String yaml = X12_837_Parser.toYaml(interchange);
            assertNotNull("YAML result should not be null", yaml);
            assertTrue("YAML should contain transaction code", yaml.contains("code: \"835\""));
            
            System.out.println("YAML output: " + yaml);
            
        } catch (IOException | SAXException e) {
            fail("Failed to convert to YAML: " + e.getMessage());
        }
    }

    @Test
    public void testRoundTripConversion() {
        try {
            // Parse EDI to XML
            String xml = X12_837_Parser.parseEDI(SAMPLE_835_EDI);
            
            // Parse XML to Object
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            
            // Convert Object back to XML
            String xmlFromObject = X12_837_Parser.toXml(interchange);
            assertNotNull("XML from object should not be null", xmlFromObject);
            
            // Convert XML back to EDI
            String ediFromXml = X12_837_Parser.xmlToEDI(xmlFromObject);
            assertNotNull("EDI from XML should not be null", ediFromXml);
            
            // The EDI output might have slight formatting differences, but should contain key segments
            assertTrue("EDI should contain ISA segment", ediFromXml.contains("ISA"));
            assertTrue("EDI should contain ST segment with 835 code", ediFromXml.contains("ST*835"));
            assertTrue("EDI should contain IEA segment", ediFromXml.contains("IEA"));
            
            System.out.println("Round-trip EDI output: " + ediFromXml);
            
        } catch (IOException | SAXException e) {
            fail("Failed round-trip conversion: " + e.getMessage());
        }
    }

    @Test
    public void testDirectObjectToEdi() {
        try {
            String xml = X12_837_Parser.parseEDI(SAMPLE_835_EDI);
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            
            String edi = X12_837_Parser.toEdiString(interchange);
            assertNotNull("EDI result should not be null", edi);
            assertTrue("EDI should contain ISA segment", edi.contains("ISA"));
            assertTrue("EDI should contain IEA segment", edi.contains("IEA"));
            
        } catch (IOException | SAXException e) {
            fail("Failed to convert object to EDI: " + e.getMessage());
        }
    }
}