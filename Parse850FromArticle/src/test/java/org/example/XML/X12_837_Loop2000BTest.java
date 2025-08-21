package org.example.XML;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test class for Loop_2000B_SubscriberDetail functionality.
 */
public class X12_837_Loop2000BTest {

    /**
     * X12 837 EDI message with Loop 2000A and Loop 2000B (Subscriber Detail).
     */
    private static final String EDI_WITH_LOOP_2000B = 
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
        "HL*2*1*22*0~" +
        "SBR*P*18*GROUP12345~" +
        "PAT*****D8*19970314~" +
        "SE*16*0001~" +
        "GE*1*1~" +
        "IEA*1*000000001~";

    /**
     * X12 837 EDI message with multiple Loop 2000B occurrences.
     */
    private static final String EDI_WITH_MULTIPLE_LOOP_2000B = 
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
        "HL*2*1*22*1~" +
        "SBR*P*18*GROUP12345~" +
        "HL*3*1*22*0~" +
        "SBR*S*01*GROUP67890~" +
        "SE*17*0001~" +
        "GE*1*1~" +
        "IEA*1*000000001~";

    @Test
    public void testLoop2000BSubscriberDetail() {
        try {
            String xml = X12_837_Parser.parseEDI(EDI_WITH_LOOP_2000B);
            assertNotNull("XML result should not be null", xml);
            assertTrue("XML should contain root element", xml.contains("X12_835_Interchange"));
            assertTrue("XML should contain Loop_2000A_BillingProviderDetail", xml.contains("Loop_2000A_BillingProviderDetail"));
            assertTrue("XML should contain Loop_2000B_SubscriberDetail", xml.contains("Loop_2000B_SubscriberDetail"));
            
            // Parse to object and verify Loop 2000B fields
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Verify Loop 2000A exists
            assertNotNull("Loop 2000A should not be null", interchange.getLoop2000ABillingProviderDetail());
            assertEquals("Should have 1 Loop 2000A occurrence", 1, interchange.getLoop2000ABillingProviderDetail().size());
            
            // Verify Loop 2000B exists
            assertNotNull("Loop 2000B should not be null", interchange.getLoop2000BSubscriberDetail());
            assertEquals("Should have 1 Loop 2000B occurrence", 1, interchange.getLoop2000BSubscriberDetail().size());
            
            X12_837_Interchange.Loop2000BSubscriberDetail loop2000B = interchange.getLoop2000BSubscriberDetail().get(0);
            
            // Verify HL segment (Subscriber Hierarchical Level)
            assertNotNull("Subscriber hierarchical level should not be null", loop2000B.getSubscriberHierarchicalLevel());
            assertEquals("2", loop2000B.getSubscriberHierarchicalLevel().getHierarchicalIdNumber());
            assertEquals("1", loop2000B.getSubscriberHierarchicalLevel().getHierarchicalParentIdNumber());
            assertEquals("22", loop2000B.getSubscriberHierarchicalLevel().getHierarchicalLevelCode());
            assertEquals("0", loop2000B.getSubscriberHierarchicalLevel().getHierarchicalChildCode());
            
            // Verify SBR segment (Subscriber Information)
            assertNotNull("Subscriber information should not be null", loop2000B.getSubscriberInformation());
            assertEquals("P", loop2000B.getSubscriberInformation().getPayerResponsibilitySequenceNumberCode());
            assertEquals("18", loop2000B.getSubscriberInformation().getIndividualRelationshipCode());
            assertEquals("GROUP12345", loop2000B.getSubscriberInformation().getInsuredGroupOrPolicyNumber());
            // Other fields are optional and not included in simplified test data
            
            // PAT segment is optional and not included in test data
            assertEquals("19970314", loop2000B.getPatientInformation().getDate());
            System.out.println("Successfully parsed EDI with Loop 2000B:");
            System.out.println(xml);
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse EDI with Loop 2000B: " + e.getMessage());
        }
    }

    @Test
    public void testMultipleLoop2000BSubscriberDetail() {
        try {
            String xml = X12_837_Parser.parseEDI(EDI_WITH_MULTIPLE_LOOP_2000B);
            assertNotNull("XML result should not be null", xml);
            assertTrue("XML should contain Loop_2000B_SubscriberDetail", xml.contains("Loop_2000B_SubscriberDetail"));
            
            // Parse to object and verify multiple Loop 2000B occurrences
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Verify Loop 2000B has multiple occurrences
            assertNotNull("Loop 2000B should not be null", interchange.getLoop2000BSubscriberDetail());
            assertEquals("Should have 2 Loop 2000B occurrences", 2, interchange.getLoop2000BSubscriberDetail().size());
            
            // Verify first Loop 2000B
            X12_837_Interchange.Loop2000BSubscriberDetail loop2000B_1 = interchange.getLoop2000BSubscriberDetail().get(0);
            assertEquals("2", loop2000B_1.getSubscriberHierarchicalLevel().getHierarchicalIdNumber());
            assertEquals("22", loop2000B_1.getSubscriberHierarchicalLevel().getHierarchicalLevelCode());
            assertEquals("P", loop2000B_1.getSubscriberInformation().getPayerResponsibilitySequenceNumberCode());
            assertEquals("GROUP12345", loop2000B_1.getSubscriberInformation().getInsuredGroupOrPolicyNumber());
            
            // Verify second Loop 2000B
            X12_837_Interchange.Loop2000BSubscriberDetail loop2000B_2 = interchange.getLoop2000BSubscriberDetail().get(1);
            assertEquals("3", loop2000B_2.getSubscriberHierarchicalLevel().getHierarchicalIdNumber());
            assertEquals("22", loop2000B_2.getSubscriberHierarchicalLevel().getHierarchicalLevelCode());
            assertEquals("S", loop2000B_2.getSubscriberInformation().getPayerResponsibilitySequenceNumberCode());
            assertEquals("GROUP67890", loop2000B_2.getSubscriberInformation().getInsuredGroupOrPolicyNumber());


            System.out.println("Successfully parsed EDI with multiple Loop 2000B occurrences");
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse EDI with multiple Loop 2000B: " + e.getMessage());
        }
    }
}