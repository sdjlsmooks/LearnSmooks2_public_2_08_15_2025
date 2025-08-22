package org.example.XML;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.*;
import org.example.XML.X12_837_Interchange.PERSegment;

/**
 * Simple test class for X12_837_Parser with minimal EDI structure.
 */
public class X12_837_SimpleTest {

    /**
     * Minimal X12 835 EDI message with just the required envelope segments.
     */
    private static final String MINIMAL_835_EDI = 
        "ISA*00*          *00*          *ZZ*SENDER123      *ZZ*RECEIVER456    *210901*1200*^*00501*000000001*0*P*:~" +
        "GS*HP*SENDER123*RECEIVER456*20210901*1200*1*X*005010X221A1~" +
        "ST*835*0001*005010X221A1~" +
        "SE*3*0001~" +
        "GE*1*1~" +
        "IEA*1*000000001~";
    
    /**
     * X12 837 EDI message with BHT segment.
     */
    private static final String EDI_WITH_BHT = 
        "ISA*00*          *00*          *ZZ*SENDER123      *ZZ*RECEIVER456    *210901*1200*^*00501*000000001*0*P*:~" +
        "GS*HC*SENDER123*RECEIVER456*20210901*1200*1*X*005010X223A2~" +
        "ST*837*0001*005010X223A2~" +
        "BHT*0019*00*REF123456*20210901*1200*CH~" +
        "SE*4*0001~" +
        "GE*1*1~" +
        "IEA*1*000000001~";
    
    /**
     * X12 837 EDI message with BHT segment and Loop 1000A.
     */
    private static final String EDI_WITH_LOOP_1000A = 
        "ISA*00*          *00*          *ZZ*SENDER123      *ZZ*RECEIVER456    *210901*1200*^*00501*000000001*0*P*:~" +
        "GS*HC*SENDER123*RECEIVER456*20210901*1200*1*X*005010X223A2~" +
        "ST*837*0001*005010X223A2~" +
        "BHT*0019*00*REF123456*20210901*1200*CH~" +
        "NM1*41*2*SUBMITTER ORGANIZATION*****46*SUB12345~" +
        "PER*IC*JOHN DOE*TE*5551234567*EX*123~" +
        "PER*IC*JANE SMITH*TE*5559876543*FX*9876543210~" +
        "SE*7*0001~" +
        "GE*1*1~" +
        "IEA*1*000000001~";
    
    /**
     * X12 837 EDI message with BHT segment, Loop 1000A, and Loop 1000B.
     */
    private static final String EDI_WITH_LOOPS_1000A_AND_1000B = 
        "ISA*00*          *00*          *ZZ*SENDER123      *ZZ*RECEIVER456    *210901*1200*^*00501*000000001*0*P*:~" +
        "GS*HC*SENDER123*RECEIVER456*20210901*1200*1*X*005010X223A2~" +
        "ST*837*0001*005010X223A2~" +
        "BHT*0019*00*REF123456*20210901*1200*CH~" +
        "NM1*41*2*SUBMITTER ORGANIZATION*****46*SUB12345~" +
        "PER*IC*JOHN DOE*TE*5551234567*EX*123~" +
        "PER*IC*JANE SMITH*TE*5559876543*FX*9876543210~" +
        "NM1*40*2*RECEIVER CORPORATION*****46*REC67890~" +
        "SE*8*0001~" +
        "GE*1*1~" +
        "IEA*1*000000001~";
    
    /**
     * X12 837 EDI message with Loop 2000A (Billing Provider Detail) - only level code 20.
     */
    private static final String EDI_WITH_LOOP_2000A = 
        "ISA*00*          *00*          *ZZ*SENDER123      *ZZ*RECEIVER456    *210901*1200*^*00501*000000001*0*P*:~" +
        "GS*HC*SENDER123*RECEIVER456*20210901*1200*1*X*005010X223A2~" +
        "ST*837*0001*005010X223A2~" +
        "BHT*0019*00*REF123456*20210901*1200*CH~" +
        "NM1*41*2*SUBMITTER ORGANIZATION*****46*SUB12345~" +
        "NM1*40*2*RECEIVER CORPORATION*****46*REC67890~" +
        "HL*1**20*1~" +
        "PRV*BI*PXC*123456789~" +
        "CUR*85*USD~" +
        "HL*2**20*1~" +
        "PRV*PT*PXC*987654321**203BF0100Y~" +
        "SE*11*0001~" +
        "GE*1*1~" +
        "IEA*1*000000001~";

    @Test
    public void testMinimalEDIStructure() {
        try {
            String xml = X12_837_Parser.parseEDI(MINIMAL_835_EDI);
            assertNotNull("XML result should not be null", xml);
            assertTrue("XML should contain root element", xml.contains("X12_837_Interchange"));
            assertTrue("XML should contain interchange header", xml.contains("interchange-header"));
            assertTrue("XML should contain group header", xml.contains("group-header"));
            assertTrue("XML should contain transaction set header", xml.contains("transaction-set-header"));
            assertTrue("XML should contain transaction set trailer", xml.contains("transaction-set-trailer"));
            assertTrue("XML should contain functional group trailer", xml.contains("functional-group-trailer"));
            assertTrue("XML should contain interchange control trailer", xml.contains("interchange-control-trailer"));
            
            System.out.println("Successfully parsed minimal EDI structure:");
            System.out.println(xml);
        } catch (IOException | SAXException e) {
            fail("Failed to parse minimal EDI: " + e.getMessage());
        }
    }

    @Test
    public void testParseAndConvertToObject() {
        try {
            String xml = X12_837_Parser.parseEDI(MINIMAL_835_EDI);
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            
            assertNotNull("Interchange object should not be null", interchange);
            assertNotNull("Interchange header should not be null", interchange.getInterchangeHeader());
            assertEquals("00", interchange.getInterchangeHeader().getAuthQual());
            assertEquals("ZZ", interchange.getInterchangeHeader().getSenderQual());
            assertEquals("SENDER123", interchange.getInterchangeHeader().getSenderId().trim());
            
            assertNotNull("Group header should not be null", interchange.getGroupHeader());
            assertEquals("HP", interchange.getGroupHeader().getCode());
            
            assertNotNull("Transaction set header should not be null", interchange.getTransactionSetHeader());
            assertEquals("835", interchange.getTransactionSetHeader().getCode());
            assertEquals("0001", interchange.getTransactionSetHeader().getTransactionSetControlNumber());
            
            assertNotNull("Transaction set trailer should not be null", interchange.getTransactionSetTrailer());
            assertEquals("3", interchange.getTransactionSetTrailer().getNumberOfIncludedSegments());
            
            assertNotNull("Functional group trailer should not be null", interchange.getFunctionalGroupTrailer());
            assertEquals("1", interchange.getFunctionalGroupTrailer().getNumberOfTransactionSets());
            
            assertNotNull("Interchange control trailer should not be null", interchange.getInterchangeControlTrailer());
            assertEquals("1", interchange.getInterchangeControlTrailer().getNumberOfFunctionGroupsIncluded());
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse and convert: " + e.getMessage());
        }
    }

    @Test
    public void testEDIWithBHTSegment() {
        try {
            String xml = X12_837_Parser.parseEDI(EDI_WITH_BHT);
            assertNotNull("XML result should not be null", xml);
            assertTrue("XML should contain root element", xml.contains("X12_837_Interchange"));
            assertTrue("XML should contain BHT segment", xml.contains("beginning-hierarchical-transaction"));
            
            // Parse to object and verify BHT fields
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Verify BHT segment exists and has correct values
            assertNotNull("BHT segment should not be null", interchange.getBeginningHierarchicalTransaction());
            assertEquals("0019", interchange.getBeginningHierarchicalTransaction().getHierarchicalStructureCode());
            assertEquals("00", interchange.getBeginningHierarchicalTransaction().getTransactionSetPurposeCode());
            assertEquals("REF123456", interchange.getBeginningHierarchicalTransaction().getReferenceIdentification());
            assertEquals("20210901", interchange.getBeginningHierarchicalTransaction().getDate());
            assertEquals("1200", interchange.getBeginningHierarchicalTransaction().getTime());
            assertEquals("CH", interchange.getBeginningHierarchicalTransaction().getTransactionTypeCode());
            
            // Verify transaction set code is 837
            assertEquals("837", interchange.getTransactionSetHeader().getCode());
            
            System.out.println("Successfully parsed EDI with BHT segment:");
            System.out.println(xml);
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse EDI with BHT segment: " + e.getMessage());
        }
    }

    @Test
    public void testLoop1000ASubmitterName() {
        try {
            String xml = X12_837_Parser.parseEDI(EDI_WITH_LOOP_1000A);
            assertNotNull("XML result should not be null", xml);
            assertTrue("XML should contain root element", xml.contains("X12_837_Interchange"));
            assertTrue("XML should contain Loop_1000A_SubmitterName", xml.contains("Loop_1000A_SubmitterName"));
            assertTrue("XML should contain submitter-name", xml.contains("submitter-name"));
            assertTrue("XML should contain submitter-edi-contact-information", xml.contains("submitter-edi-contact-information"));
            
            // Parse to object and verify Loop 1000A fields
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Verify Loop 1000A exists
            assertNotNull("Loop 1000A should not be null", interchange.getLoop1000ASubmitterName());
            
            // Verify NM1 segment (Submitter Name)
            assertNotNull("Submitter name segment should not be null", interchange.getLoop1000ASubmitterName().getSubmitterName());
            assertEquals("41", interchange.getLoop1000ASubmitterName().getSubmitterName().getEntityIdentifierCode());
            assertEquals("2", interchange.getLoop1000ASubmitterName().getSubmitterName().getEntityTypeQualifier());
            assertEquals("SUBMITTER ORGANIZATION", interchange.getLoop1000ASubmitterName().getSubmitterName().getNameLastOrOrganizationName());
            assertEquals("46", interchange.getLoop1000ASubmitterName().getSubmitterName().getIdentificationCodeQualifier());
            assertEquals("SUB12345", interchange.getLoop1000ASubmitterName().getSubmitterName().getIdentificationCode());
            
            // Verify PER segments (Contact Information)
            assertNotNull("Contact information should not be null", interchange.getLoop1000ASubmitterName().getSubmitterEdiContactInformation());
            assertEquals("Should have 2 PER segments", 2, interchange.getLoop1000ASubmitterName().getSubmitterEdiContactInformation().size());
            
            // Verify first PER segment
            PERSegment per1 = interchange.getLoop1000ASubmitterName().getSubmitterEdiContactInformation().get(0);
            assertEquals("IC", per1.getContactFunctionCode());
            assertEquals("JOHN DOE", per1.getName());
            assertEquals("TE", per1.getCommunicationNumberQualifier());
            assertEquals("5551234567", per1.getCommunicationNumber());
            assertEquals("EX", per1.getCommunicationNumberQualifier2());
            assertEquals("123", per1.getCommunicationNumber2());
            
            // Verify second PER segment
            PERSegment per2 = interchange.getLoop1000ASubmitterName().getSubmitterEdiContactInformation().get(1);
            assertEquals("IC", per2.getContactFunctionCode());
            assertEquals("JANE SMITH", per2.getName());
            assertEquals("TE", per2.getCommunicationNumberQualifier());
            assertEquals("5559876543", per2.getCommunicationNumber());
            assertEquals("FX", per2.getCommunicationNumberQualifier2());
            assertEquals("9876543210", per2.getCommunicationNumber2());
            
            System.out.println("Successfully parsed EDI with Loop 1000A:");
            System.out.println(xml);
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse EDI with Loop 1000A: " + e.getMessage());
        }
    }

    @Test
    public void testLoop1000AAndLoop1000B() {
        try {
            String xml = X12_837_Parser.parseEDI(EDI_WITH_LOOPS_1000A_AND_1000B);
            assertNotNull("XML result should not be null", xml);
            assertTrue("XML should contain root element", xml.contains("X12_837_Interchange"));
            assertTrue("XML should contain Loop_1000A_SubmitterName", xml.contains("Loop_1000A_SubmitterName"));
            assertTrue("XML should contain Loop_1000B_ReceiverName", xml.contains("Loop_1000B_ReceiverName"));
            
            // Parse to object and verify both loops
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Verify Loop 1000A exists
            assertNotNull("Loop 1000A should not be null", interchange.getLoop1000ASubmitterName());
            
            // Verify NM1 segment in Loop 1000A (Submitter Name)
            assertNotNull("Submitter name segment should not be null", interchange.getLoop1000ASubmitterName().getSubmitterName());
            assertEquals("41", interchange.getLoop1000ASubmitterName().getSubmitterName().getEntityIdentifierCode());
            assertEquals("2", interchange.getLoop1000ASubmitterName().getSubmitterName().getEntityTypeQualifier());
            assertEquals("SUBMITTER ORGANIZATION", interchange.getLoop1000ASubmitterName().getSubmitterName().getNameLastOrOrganizationName());
            assertEquals("46", interchange.getLoop1000ASubmitterName().getSubmitterName().getIdentificationCodeQualifier());
            assertEquals("SUB12345", interchange.getLoop1000ASubmitterName().getSubmitterName().getIdentificationCode());
            
            // Verify PER segments in Loop 1000A (Contact Information)
            assertNotNull("Contact information should not be null", interchange.getLoop1000ASubmitterName().getSubmitterEdiContactInformation());
            assertEquals("Should have 2 PER segments", 2, interchange.getLoop1000ASubmitterName().getSubmitterEdiContactInformation().size());
            
            // Verify Loop 1000B exists
            assertNotNull("Loop 1000B should not be null", interchange.getLoop1000BReceiverName());
            
            // Verify NM1 segment in Loop 1000B (Receiver Name)
            assertNotNull("Receiver name segment should not be null", interchange.getLoop1000BReceiverName().getReceiverName());
            assertEquals("40", interchange.getLoop1000BReceiverName().getReceiverName().getEntityIdentifierCode());
            assertEquals("2", interchange.getLoop1000BReceiverName().getReceiverName().getEntityTypeQualifier());
            assertEquals("RECEIVER CORPORATION", interchange.getLoop1000BReceiverName().getReceiverName().getNameLastOrOrganizationName());
            assertEquals("46", interchange.getLoop1000BReceiverName().getReceiverName().getIdentificationCodeQualifier());
            assertEquals("REC67890", interchange.getLoop1000BReceiverName().getReceiverName().getIdentificationCode());
            
            System.out.println("Successfully parsed EDI with Loop 1000A and Loop 1000B:");
            System.out.println(xml);
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse EDI with Loop 1000A and Loop 1000B: " + e.getMessage());
        }
    }

    @Test
    public void testLoop2000ABillingProviderDetail() {
        try {
            String xml = X12_837_Parser.parseEDI(EDI_WITH_LOOP_2000A);
            assertNotNull("XML result should not be null", xml);
            assertTrue("XML should contain root element", xml.contains("X12_837_Interchange"));
            assertTrue("XML should contain Loop_2000A_BillingProviderDetail", xml.contains("Loop_2000A_BillingProviderDetail"));
            
            // Parse to object and verify Loop 2000A fields
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Verify Loop 2000A exists and has multiple occurrences
            assertNotNull("Loop 2000A should not be null", interchange.getLoop2000ABillingProviderDetail());
            assertEquals("Should have 2 Loop 2000A occurrences", 2, interchange.getLoop2000ABillingProviderDetail().size());
            
            // Verify first Loop 2000A occurrence
            X12_837_Interchange.Loop2000ABillingProviderDetail loop1 = interchange.getLoop2000ABillingProviderDetail().get(0);
            
            // Verify HL segment in first loop
            assertNotNull("First HL segment should not be null", loop1.getBillingProviderHierarchicalLevel());
            assertEquals("1", loop1.getBillingProviderHierarchicalLevel().getHierarchicalIdNumber());
            assertNull("Parent ID should be null/empty", loop1.getBillingProviderHierarchicalLevel().getHierarchicalParentIdNumber());
            assertEquals("20", loop1.getBillingProviderHierarchicalLevel().getHierarchicalLevelCode());
            assertEquals("1", loop1.getBillingProviderHierarchicalLevel().getHierarchicalChildCode());
            
            // Verify PRV segment in first loop
            assertNotNull("First PRV segment should not be null", loop1.getBillingProviderSpecialtyInformation());
            assertEquals("BI", loop1.getBillingProviderSpecialtyInformation().getProviderCode());
            assertEquals("PXC", loop1.getBillingProviderSpecialtyInformation().getReferenceIdentificationQualifier());
            assertEquals("123456789", loop1.getBillingProviderSpecialtyInformation().getReferenceIdentification());
            
            // Verify CUR segment in first loop
            assertNotNull("First CUR segment should not be null", loop1.getForeignCurrencyInformation());
            assertEquals("85", loop1.getForeignCurrencyInformation().getEntityIdentifierCode());
            assertEquals("USD", loop1.getForeignCurrencyInformation().getCurrencyCode());
            
            // Verify second Loop 2000A occurrence
            X12_837_Interchange.Loop2000ABillingProviderDetail loop2 = interchange.getLoop2000ABillingProviderDetail().get(1);
            
            // Verify HL segment in second loop
            assertNotNull("Second HL segment should not be null", loop2.getBillingProviderHierarchicalLevel());
            assertEquals("2", loop2.getBillingProviderHierarchicalLevel().getHierarchicalIdNumber());
            assertNull("Parent ID should be null/empty for second loop", loop2.getBillingProviderHierarchicalLevel().getHierarchicalParentIdNumber());
            assertEquals("20", loop2.getBillingProviderHierarchicalLevel().getHierarchicalLevelCode());
            assertEquals("1", loop2.getBillingProviderHierarchicalLevel().getHierarchicalChildCode());
            
            // Verify PRV segment in second loop
            assertNotNull("Second PRV segment should not be null", loop2.getBillingProviderSpecialtyInformation());
            assertEquals("PT", loop2.getBillingProviderSpecialtyInformation().getProviderCode());
            assertEquals("PXC", loop2.getBillingProviderSpecialtyInformation().getReferenceIdentificationQualifier());
            assertEquals("987654321", loop2.getBillingProviderSpecialtyInformation().getReferenceIdentification());
            assertNull("State or province code should be null", loop2.getBillingProviderSpecialtyInformation().getStateOrProvinceCode());
            assertEquals("203BF0100Y", loop2.getBillingProviderSpecialtyInformation().getSpecialtyInformation());
            
            // CUR segment is optional and not present in second loop
            assertNull("Second CUR segment should be null", loop2.getForeignCurrencyInformation());
            
            System.out.println("Successfully parsed EDI with Loop 2000A (multiple occurrences):");
            System.out.println(xml);
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse EDI with Loop 2000A: " + e.getMessage());
        }
    }
}