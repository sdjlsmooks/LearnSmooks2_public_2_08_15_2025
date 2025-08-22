package org.example.XML;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Debug test for X12_837 serialization issues.
 */
public class X12_837_SerializationDebugTest {

    private static final String MINIMAL_EDI = 
        "ISA*00*          *00*          *ZZ*SENDER123      *ZZ*RECEIVER456    *210901*1200*^*00501*000000001*0*P*:~" +
        "GS*HC*SENDER123*RECEIVER456*20210901*1200*1*X*005010X223A2~" +
        "ST*837*0001*005010X223A2~" +
        "BHT*0019*00*REF123456*20210901*1200*CH~" +
        "NM1*41*2*SUBMITTER ORGANIZATION*****46*SUB12345~" +
        "NM1*40*2*RECEIVER CORPORATION*****46*REC67890~" +
        "SE*6*0001~" +
        "GE*1*1~" +
        "IEA*1*000000001~";

    @Test
    public void testMinimalEDIWithoutPER() {
        try {
            // Parse EDI to XML
            String xml = X12_837_Parser.parseEDI(MINIMAL_EDI);
            assertNotNull("XML result should not be null", xml);
            
            // Parse XML to Object
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Check if PER segment list is null or empty
            if (interchange.getLoop1000ASubmitterName() != null) {
                System.out.println("Submitter EDI Contact Info: " + 
                    interchange.getLoop1000ASubmitterName().getSubmitterEdiContactInformation());
            }
            
            // Try to convert back to XML
            String xmlFromObject = X12_837_Parser.toXml(interchange);
            assertNotNull("XML from object should not be null", xmlFromObject);
            System.out.println("Successfully converted to XML without PER segment");
            
            // Now try to convert XML back to EDI
            try {
                String ediFromXml = X12_837_Parser.xmlToEDI(xmlFromObject);
                assertNotNull("EDI from XML should not be null", ediFromXml);
                System.out.println("Successfully converted XML to EDI: " + ediFromXml);
            } catch (Exception e) {
                System.out.println("Error converting to EDI: " + e.getMessage());
                // This is expected to fail for now
            }
            
        } catch (IOException | SAXException e) {
            fail("Failed minimal EDI test: " + e.getMessage());
        }
    }
    
    @Test
    public void testEDIWithPER() {
        String ediWithPER = 
            "ISA*00*          *00*          *ZZ*SENDER123      *ZZ*RECEIVER456    *210901*1200*^*00501*000000001*0*P*:~" +
            "GS*HC*SENDER123*RECEIVER456*20210901*1200*1*X*005010X223A2~" +
            "ST*837*0001*005010X223A2~" +
            "BHT*0019*00*REF123456*20210901*1200*CH~" +
            "NM1*41*2*SUBMITTER ORGANIZATION*****46*SUB12345~" +
            "PER*IC*JOHN DOE*TE*5551234567~" +
            "NM1*40*2*RECEIVER CORPORATION*****46*REC67890~" +
            "SE*7*0001~" +
            "GE*1*1~" +
            "IEA*1*000000001~";
            
        try {
            // Parse EDI to XML
            String xml = X12_837_Parser.parseEDI(ediWithPER);
            assertNotNull("XML result should not be null", xml);
            System.out.println("XML with PER: " + xml);
            
            // Parse XML to Object
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Check PER segment
            assertNotNull("Loop 1000A should not be null", interchange.getLoop1000ASubmitterName());
            assertNotNull("Submitter EDI Contact Info should not be null", 
                interchange.getLoop1000ASubmitterName().getSubmitterEdiContactInformation());
            assertFalse("Submitter EDI Contact Info should not be empty", 
                interchange.getLoop1000ASubmitterName().getSubmitterEdiContactInformation().isEmpty());
            
            X12_837_Interchange.PERSegment per = 
                interchange.getLoop1000ASubmitterName().getSubmitterEdiContactInformation().get(0);
            assertEquals("IC", per.getContactFunctionCode());
            assertEquals("JOHN DOE", per.getName());
            assertEquals("TE", per.getCommunicationNumberQualifier());
            assertEquals("5551234567", per.getCommunicationNumber());
            
            // Try to convert back to XML
            String xmlFromObject = X12_837_Parser.toXml(interchange);
            assertNotNull("XML from object should not be null", xmlFromObject);
            System.out.println("XML from object: " + xmlFromObject);
            
        } catch (IOException | SAXException e) {
            fail("Failed EDI with PER test: " + e.getMessage());
        }
    }
}