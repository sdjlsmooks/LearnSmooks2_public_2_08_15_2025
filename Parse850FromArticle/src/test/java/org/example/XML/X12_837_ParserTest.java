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
     * Sample X12 837 EDI message for testing.
     * This is a simplified example of an 837 Healthcare Claim transaction.
     */
    private static final String SAMPLE_837_EDI =
            "ISA*00*          *00*          *ZZ*SENDER123      *ZZ*RECEIVER456    *210901*1200*^*00501*000000001*0*P*:~" +
                    "GS*HC*SENDER123*RECEIVER456*20210901*1200*1*X*005010X223A2~" +
                    "ST*837*0001*005010X223A2~" +
                    "BHT*0019*00*REF123456*20210901*1200*CH~" +
                    "NM1*41*2*SUBMITTER ORGANIZATION*****46*SUB12345~" +
                    "NM1*40*2*RECEIVER CORPORATION*****46*REC67890~" +
                    "HL*1**20*1~" +
                    "PRV*BI*PXC*123456789~" +
                    "CUR*85*USD~" +
                    "NM1*85*2*BILLING PROVIDER INC*****XX*1234567890~" +
                    "N3*123 MAIN STREET*SUITE 100~" +
                    "N4*ANYTOWN*CA*90210~" +
                    "REF*EI*12-3456789~" +
                    "REF*SY*PROVIDER789~" +
                    "REF*0B*STATE1234~" +
                    "PER*IC*JOHN BILLING*TE*5551234567*FX*5551234568~" +
                    "PER*IC*JANE ADMIN*EM*admin@provider.com~" +
                    "NM1*87*2*PAY TO PROVIDER LLC*****XX*9876543210~" +
                    "N3*456 PAYMENT BOULEVARD*FLOOR 5~" +
                    "N4*PAYMENTVILLE*NY*10001~" +
                    "NM1*PE*2*HEALTH PLAN INC*****PI*HP123456789~" +
                    "N3*789 INSURANCE WAY*BUILDING A~" +
                    "N4*PLANTOWN*TX*75001~" +
                    "REF*2U*SECONDARY987~" +
                    "REF*EI*98-7654321~" +
                    "SE*25*0001~" +
                    "GE*1*1~" +
                    "IEA*1*000000001~";

    @Test
    public void testParseEDIToXML() {
        try {
            String xml = X12_837_Parser.parseEDI(SAMPLE_837_EDI);
            assertNotNull("XML result should not be null", xml);
            assertTrue("XML should contain root element", xml.contains("X12_837_Interchange"));
            assertTrue("XML should contain interchange header", xml.contains("interchange-header"));
            assertTrue("XML should contain BHT segment", xml.contains("beginning-hierarchical-transaction"));
            System.out.println("Parsed XML: " + xml);
        } catch (IOException | SAXException e) {
            fail("Failed to parse EDI to XML: " + e.getMessage());
        }
    }

    @Test
    public void testParseXMLToObject() {
        try {
            String xml = X12_837_Parser.parseEDI(SAMPLE_837_EDI);
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            
            // Verify basic structure
            assertNotNull("Interchange object should not be null", interchange);
            assertNotNull("Interchange header should not be null", interchange.getInterchangeHeader());
            assertNotNull("Group header should not be null", interchange.getGroupHeader());
            assertNotNull("Transaction set header should not be null", interchange.getTransactionSetHeader());
            assertNotNull("BHT should not be null", interchange.getBeginningHierarchicalTransaction());
            
            // Verify interchange header
            assertEquals("00", interchange.getInterchangeHeader().getAuthQual());
            assertEquals("ZZ", interchange.getInterchangeHeader().getSenderQual());
            assertEquals("SENDER123", interchange.getInterchangeHeader().getSenderId().trim());
            assertEquals("RECEIVER456", interchange.getInterchangeHeader().getReceiverId().trim());
            
            // Verify transaction set header
            assertEquals("837", interchange.getTransactionSetHeader().getCode());
            assertEquals("0001", interchange.getTransactionSetHeader().getTransactionSetControlNumber());
            assertEquals("005010X223A2", interchange.getTransactionSetHeader().getImplementationConventionReference());
            
            // Verify BHT segment
            assertEquals("0019", interchange.getBeginningHierarchicalTransaction().getHierarchicalStructureCode());
            assertEquals("REF123456", interchange.getBeginningHierarchicalTransaction().getReferenceIdentification());
            
            // Verify Loop 1000A - Submitter
            assertNotNull("Loop 1000A should not be null", interchange.getLoop1000ASubmitterName());
            assertEquals("SUBMITTER ORGANIZATION", interchange.getLoop1000ASubmitterName().getSubmitterName().getNameLastOrOrganizationName());
            
            // Verify Loop 1000B - Receiver
            assertNotNull("Loop 1000B should not be null", interchange.getLoop1000BReceiverName());
            assertEquals("RECEIVER CORPORATION", interchange.getLoop1000BReceiverName().getReceiverName().getNameLastOrOrganizationName());
            
            // Verify Loop 2000A - Billing Provider Detail
            assertNotNull("Loop 2000A should not be null", interchange.getLoop2000ABillingProviderDetail());
            assertEquals("Should have at least one Loop 2000A", 1, interchange.getLoop2000ABillingProviderDetail().size());
            
            X12_837_Interchange.Loop2000ABillingProviderDetail loop2000A = interchange.getLoop2000ABillingProviderDetail().get(0);
            assertNotNull("Loop 2010AA should not be null", loop2000A.getLoop2010AABillingProviderDetailHL());
            assertEquals("BILLING PROVIDER INC", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderName().getNameLastOrOrganizationName());
            assertEquals("123 MAIN STREET", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderAddress().getAddressInformation());
            assertEquals("SUITE 100", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderAddress().getAddressInformation2());
            
            // Verify Loop 2010AB - Pay-To Address
            assertNotNull("Loop 2010AB should not be null", loop2000A.getLoop2010ABPayToAddressName());
            assertEquals("PAY TO PROVIDER LLC", loop2000A.getLoop2010ABPayToAddressName().getPayToAddressName().getNameLastOrOrganizationName());
            assertEquals("456 PAYMENT BOULEVARD", loop2000A.getLoop2010ABPayToAddressName().getPayToAddress().getAddressInformation());
            assertEquals("FLOOR 5", loop2000A.getLoop2010ABPayToAddressName().getPayToAddress().getAddressInformation2());
            
            // Verify Loop 2010AC - Pay-To Plan
            assertNotNull("Loop 2010AC should not be null", loop2000A.getLoop2010ACPayToPlanName());
            assertEquals("HEALTH PLAN INC", loop2000A.getLoop2010ACPayToPlanName().getPayToPlanName().getNameLastOrOrganizationName());
            assertEquals("789 INSURANCE WAY", loop2000A.getLoop2010ACPayToPlanName().getPayToPlanAddress().getAddressInformation());
            assertEquals("BUILDING A", loop2000A.getLoop2010ACPayToPlanName().getPayToPlanAddress().getAddressInformation2());
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse XML to object: " + e.getMessage());
        }
    }

    @Test
    public void testObjectToJson() {
        try {
            String xml = X12_837_Parser.parseEDI(SAMPLE_837_EDI);
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            
            String json = X12_837_Parser.toJson(interchange);
            assertNotNull("JSON result should not be null", json);
            assertTrue("JSON should contain transaction code", json.contains("\"code\":\"837\""));
            assertTrue("JSON should contain interchange header", json.contains("interchange-header"));
            
            System.out.println("JSON output: " + json);
            
        } catch (IOException | SAXException e) {
            fail("Failed to convert to JSON: " + e.getMessage());
        }
    }

    @Test
    public void testObjectToYaml() {
        try {
            String xml = X12_837_Parser.parseEDI(SAMPLE_837_EDI);
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            
            String yaml = X12_837_Parser.toYaml(interchange);
            assertNotNull("YAML result should not be null", yaml);
            assertTrue("YAML should contain transaction code", yaml.contains("code: \"837\""));
            
            System.out.println("YAML output: " + yaml);
            
        } catch (IOException | SAXException e) {
            fail("Failed to convert to YAML: " + e.getMessage());
        }
    }

    @Test
    public void testRoundTripConversion() {
        try {
            // Parse EDI to XML
            String xml = X12_837_Parser.parseEDI(SAMPLE_837_EDI);
            
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
            assertTrue("EDI should contain ST segment with 837 code", ediFromXml.contains("ST*837"));
            assertTrue("EDI should contain IEA segment", ediFromXml.contains("IEA"));
            
            System.out.println("Round-trip EDI output: " + ediFromXml);
            
        } catch (IOException | SAXException e) {
            fail("Failed round-trip conversion: " + e.getMessage());
        }
    }

    @Test
    public void testDirectObjectToEdi() {
        try {
            String xml = X12_837_Parser.parseEDI(SAMPLE_837_EDI);
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            
            String edi = X12_837_Parser.toEdiString(interchange);
            assertNotNull("EDI result should not be null", edi);
            assertTrue("EDI should contain ISA segment", edi.contains("ISA"));
            assertTrue("EDI should contain IEA segment", edi.contains("IEA"));
            
        } catch (IOException | SAXException e) {
            fail("Failed to convert object to EDI: " + e.getMessage());
        }
    }
    
    @Test
    public void testComprehensiveEDIParsing() {
        try {
            // Parse EDI to XML
            String xml = X12_837_Parser.parseEDI(SAMPLE_837_EDI);
            assertNotNull("XML result should not be null", xml);
            
            // Parse XML to Object
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Verify all segments are present and correctly parsed
            verifyInterchangeStructure(interchange);
            verifyLoop1000Segments(interchange);
            verifyLoop2000ASegments(interchange);
            
            // Test JSON conversion
            String json = X12_837_Parser.toJson(interchange);
            assertNotNull("JSON should not be null", json);
            assertTrue("JSON should contain billing provider", json.contains("BILLING PROVIDER INC"));
            assertTrue("JSON should contain pay-to provider", json.contains("PAY TO PROVIDER LLC"));
            assertTrue("JSON should contain health plan", json.contains("HEALTH PLAN INC"));
            
            // Test YAML conversion
            String yaml = X12_837_Parser.toYaml(interchange);
            assertNotNull("YAML should not be null", yaml);
            assertTrue("YAML should contain billing provider", yaml.contains("BILLING PROVIDER INC"));
            
            System.out.println("Successfully parsed and validated comprehensive EDI structure");
            
        } catch (IOException | SAXException e) {
            fail("Failed comprehensive EDI parsing: " + e.getMessage());
        }
    }
    
    private void verifyInterchangeStructure(X12_837_Interchange interchange) {
        // Interchange header verification
        assertEquals("ISA auth qual", "00", interchange.getInterchangeHeader().getAuthQual());
        assertEquals("ISA sender qual", "ZZ", interchange.getInterchangeHeader().getSenderQual());
        assertEquals("ISA sender ID", "SENDER123      ", interchange.getInterchangeHeader().getSenderId());
        assertEquals("ISA receiver ID", "RECEIVER456    ", interchange.getInterchangeHeader().getReceiverId());
        assertEquals("ISA date", "210901", interchange.getInterchangeHeader().getDate());
        assertEquals("ISA time", "1200", interchange.getInterchangeHeader().getTime());
        assertEquals("ISA control number", "000000001", interchange.getInterchangeHeader().getInterchangeControlNumber());
        
        // Group header verification
        assertEquals("GS functional code", "HC", interchange.getGroupHeader().getCode());
        assertEquals("GS sender", "SENDER123", interchange.getGroupHeader().getSender());
        assertEquals("GS receiver", "RECEIVER456", interchange.getGroupHeader().getReceiver());
        assertEquals("GS version", "005010X223A2", interchange.getGroupHeader().getVersion());
        
        // Transaction set header verification
        assertEquals("ST transaction code", "837", interchange.getTransactionSetHeader().getCode());
        assertEquals("ST control number", "0001", interchange.getTransactionSetHeader().getTransactionSetControlNumber());
        assertEquals("ST implementation", "005010X223A2", interchange.getTransactionSetHeader().getImplementationConventionReference());
        
        // BHT verification
        assertEquals("BHT structure code", "0019", interchange.getBeginningHierarchicalTransaction().getHierarchicalStructureCode());
        assertEquals("BHT purpose code", "00", interchange.getBeginningHierarchicalTransaction().getTransactionSetPurposeCode());
        assertEquals("BHT reference", "REF123456", interchange.getBeginningHierarchicalTransaction().getReferenceIdentification());
        assertEquals("BHT date", "20210901", interchange.getBeginningHierarchicalTransaction().getDate());
        assertEquals("BHT time", "1200", interchange.getBeginningHierarchicalTransaction().getTime());
        assertEquals("BHT type code", "CH", interchange.getBeginningHierarchicalTransaction().getTransactionTypeCode());
    }
    
    private void verifyLoop1000Segments(X12_837_Interchange interchange) {
        // Loop 1000A - Submitter verification
        assertNotNull("Loop 1000A", interchange.getLoop1000ASubmitterName());
        assertEquals("Submitter entity code", "41", interchange.getLoop1000ASubmitterName().getSubmitterName().getEntityIdentifierCode());
        assertEquals("Submitter name", "SUBMITTER ORGANIZATION", interchange.getLoop1000ASubmitterName().getSubmitterName().getNameLastOrOrganizationName());
        assertEquals("Submitter ID qualifier", "46", interchange.getLoop1000ASubmitterName().getSubmitterName().getIdentificationCodeQualifier());
        assertEquals("Submitter ID", "SUB12345", interchange.getLoop1000ASubmitterName().getSubmitterName().getIdentificationCode());
        
        // Loop 1000B - Receiver verification
        assertNotNull("Loop 1000B", interchange.getLoop1000BReceiverName());
        assertEquals("Receiver entity code", "40", interchange.getLoop1000BReceiverName().getReceiverName().getEntityIdentifierCode());
        assertEquals("Receiver name", "RECEIVER CORPORATION", interchange.getLoop1000BReceiverName().getReceiverName().getNameLastOrOrganizationName());
        assertEquals("Receiver ID qualifier", "46", interchange.getLoop1000BReceiverName().getReceiverName().getIdentificationCodeQualifier());
        assertEquals("Receiver ID", "REC67890", interchange.getLoop1000BReceiverName().getReceiverName().getIdentificationCode());
    }
    
    private void verifyLoop2000ASegments(X12_837_Interchange interchange) {
        assertNotNull("Loop 2000A list", interchange.getLoop2000ABillingProviderDetail());
        assertEquals("Loop 2000A count", 1, interchange.getLoop2000ABillingProviderDetail().size());
        
        X12_837_Interchange.Loop2000ABillingProviderDetail loop2000A = interchange.getLoop2000ABillingProviderDetail().get(0);
        
        // HL segment verification
        assertEquals("HL ID number", "1", loop2000A.getBillingProviderHierarchicalLevel().getHierarchicalIdNumber());
        assertEquals("HL level code", "20", loop2000A.getBillingProviderHierarchicalLevel().getHierarchicalLevelCode());
        assertEquals("HL child code", "1", loop2000A.getBillingProviderHierarchicalLevel().getHierarchicalChildCode());
        
        // PRV segment verification
        assertNotNull("PRV segment", loop2000A.getBillingProviderSpecialtyInformation());
        assertEquals("PRV provider code", "BI", loop2000A.getBillingProviderSpecialtyInformation().getProviderCode());
        assertEquals("PRV reference qualifier", "PXC", loop2000A.getBillingProviderSpecialtyInformation().getReferenceIdentificationQualifier());
        assertEquals("PRV reference ID", "123456789", loop2000A.getBillingProviderSpecialtyInformation().getReferenceIdentification());
        
        // CUR segment verification
        assertNotNull("CUR segment", loop2000A.getForeignCurrencyInformation());
        assertEquals("CUR entity code", "85", loop2000A.getForeignCurrencyInformation().getEntityIdentifierCode());
        assertEquals("CUR currency code", "USD", loop2000A.getForeignCurrencyInformation().getCurrencyCode());
        
        // Loop 2010AA - Billing Provider verification
        assertNotNull("Loop 2010AA", loop2000A.getLoop2010AABillingProviderDetailHL());
        assertEquals("Billing provider entity code", "85", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderName().getEntityIdentifierCode());
        assertEquals("Billing provider name", "BILLING PROVIDER INC", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderName().getNameLastOrOrganizationName());
        assertEquals("Billing provider ID qualifier", "XX", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderName().getIdentificationCodeQualifier());
        assertEquals("Billing provider ID", "1234567890", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderName().getIdentificationCode());
        
        // Address verification
        assertEquals("Billing provider address line 1", "123 MAIN STREET", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderAddress().getAddressInformation());
        assertEquals("Billing provider address line 2", "SUITE 100", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderAddress().getAddressInformation2());
        assertEquals("Billing provider city", "ANYTOWN", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderCityStateZip().getCityName());
        assertEquals("Billing provider state", "CA", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderCityStateZip().getStateOrProvinceCode());
        assertEquals("Billing provider zip", "90210", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderCityStateZip().getPostalCode());
        
        // REF segments verification
        assertNotNull("Tax ID REF", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderTaxId());
        assertEquals("Tax ID qualifier", "EI", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderTaxId().getReferenceIdentificationQualifier());
        assertEquals("Tax ID", "12-3456789", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderTaxId().getReferenceIdentification());
        
        assertNotNull("UPIN/License REF", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderUpinLicenseInfo());
        assertEquals("UPIN qualifier", "SY", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderUpinLicenseInfo().get(0).getReferenceIdentificationQualifier());
        assertEquals("UPIN", "PROVIDER789", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderUpinLicenseInfo().get(0).getReferenceIdentification());
        
        // PER segments verification
        assertNotNull("Contact info", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderContactInfo());
        assertTrue("Should have contact info", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderContactInfo().size() >= 2);
        assertEquals("First contact name", "JOHN BILLING", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderContactInfo().get(0).getName());
        assertEquals("Second contact name", "JANE ADMIN", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderContactInfo().get(1).getName());
        
        // Loop 2010AB - Pay-To Address verification
        assertNotNull("Loop 2010AB", loop2000A.getLoop2010ABPayToAddressName());
        assertEquals("Pay-to entity code", "87", loop2000A.getLoop2010ABPayToAddressName().getPayToAddressName().getEntityIdentifierCode());
        assertEquals("Pay-to name", "PAY TO PROVIDER LLC", loop2000A.getLoop2010ABPayToAddressName().getPayToAddressName().getNameLastOrOrganizationName());
        assertEquals("Pay-to ID", "9876543210", loop2000A.getLoop2010ABPayToAddressName().getPayToAddressName().getIdentificationCode());
        assertEquals("Pay-to address line 1", "456 PAYMENT BOULEVARD", loop2000A.getLoop2010ABPayToAddressName().getPayToAddress().getAddressInformation());
        assertEquals("Pay-to address line 2", "FLOOR 5", loop2000A.getLoop2010ABPayToAddressName().getPayToAddress().getAddressInformation2());
        assertEquals("Pay-to city", "PAYMENTVILLE", loop2000A.getLoop2010ABPayToAddressName().getPayToCityStateZip().getCityName());
        assertEquals("Pay-to state", "NY", loop2000A.getLoop2010ABPayToAddressName().getPayToCityStateZip().getStateOrProvinceCode());
        assertEquals("Pay-to zip", "10001", loop2000A.getLoop2010ABPayToAddressName().getPayToCityStateZip().getPostalCode());
        
        // Loop 2010AC - Pay-To Plan verification
        assertNotNull("Loop 2010AC", loop2000A.getLoop2010ACPayToPlanName());
        assertEquals("Plan entity code", "PE", loop2000A.getLoop2010ACPayToPlanName().getPayToPlanName().getEntityIdentifierCode());
        assertEquals("Plan name", "HEALTH PLAN INC", loop2000A.getLoop2010ACPayToPlanName().getPayToPlanName().getNameLastOrOrganizationName());
        assertEquals("Plan ID qualifier", "PI", loop2000A.getLoop2010ACPayToPlanName().getPayToPlanName().getIdentificationCodeQualifier());
        assertEquals("Plan ID", "HP123456789", loop2000A.getLoop2010ACPayToPlanName().getPayToPlanName().getIdentificationCode());
        assertEquals("Plan address line 1", "789 INSURANCE WAY", loop2000A.getLoop2010ACPayToPlanName().getPayToPlanAddress().getAddressInformation());
        assertEquals("Plan address line 2", "BUILDING A", loop2000A.getLoop2010ACPayToPlanName().getPayToPlanAddress().getAddressInformation2());
        assertEquals("Plan city", "PLANTOWN", loop2000A.getLoop2010ACPayToPlanName().getPayToPlanCityStateZip().getCityName());
        assertEquals("Plan state", "TX", loop2000A.getLoop2010ACPayToPlanName().getPayToPlanCityStateZip().getStateOrProvinceCode());
        assertEquals("Plan zip", "75001", loop2000A.getLoop2010ACPayToPlanName().getPayToPlanCityStateZip().getPostalCode());
        
        // Plan REF segments verification
        assertNotNull("Plan secondary ID", loop2000A.getLoop2010ACPayToPlanName().getPayToPlanSecondaryId());
        assertEquals("Plan secondary ID qualifier", "2U", loop2000A.getLoop2010ACPayToPlanName().getPayToPlanSecondaryId().getReferenceIdentificationQualifier());
        assertEquals("Plan secondary ID", "SECONDARY987", loop2000A.getLoop2010ACPayToPlanName().getPayToPlanSecondaryId().getReferenceIdentification());
        
        assertNotNull("Plan tax ID", loop2000A.getLoop2010ACPayToPlanName().getPayToPlanTaxId());
        assertEquals("Plan tax ID qualifier", "EI", loop2000A.getLoop2010ACPayToPlanName().getPayToPlanTaxId().getReferenceIdentificationQualifier());
        assertEquals("Plan tax ID", "98-7654321", loop2000A.getLoop2010ACPayToPlanName().getPayToPlanTaxId().getReferenceIdentification());
    }
}