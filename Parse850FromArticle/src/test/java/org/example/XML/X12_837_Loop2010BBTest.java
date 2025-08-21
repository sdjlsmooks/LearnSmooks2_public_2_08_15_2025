package org.example.XML;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test class for Loop_2010BB_PayerName functionality.
 */
public class X12_837_Loop2010BBTest {

    /**
     * X12 837 EDI message with Loop 2000B containing both Loop 2010BA and Loop 2010BB.
     */
    private static final String EDI_WITH_LOOP_2010BB = 
        "ISA*00*          *00*          *ZZ*SENDER123      *ZZ*RECEIVER456    *210901*1200*^*00501*000000001*0*P*:~" +
        "GS*HC*SENDER123*RECEIVER456*20210901*1200*1*X*005010X223A2~" +
        "ST*837*0001*005010X223A2~" +
        "BHT*0019*00*REF123456*20210901*1200*CH~" +
        "NM1*41*2*SUBMITTER ORGANIZATION*****46*SUB12345~" +
        "NM1*40*2*RECEIVER CORPORATION*****46*REC67890~" +
        "HL*1**20*1~" +
        "PRV*BI*PXC*123456789~" +
        "CUR*85*USD~" +
        "HL*2*1*22*0~" +
        "SBR*P*18*GROUP12345~" +
        // Loop 2010BA - Subscriber Name
        "NM1*IL*1*DOE*JOHN*M***MI*ABC123456~" +
        "N3*456 SUBSCRIBER STREET~" +
        "N4*SUBTOWN*NY*12345~" +
        "DMG*D8*19800501*M~" +
        "REF*SY*SSN123456789~" +
        "REF*Y4*CLAIM987654~" +
        "PER*IC*JANE DOE*TE*5559876543~" +
        // Loop 2010BB - Payer Name
        "NM1*PR*2*INSURANCE COMPANY LLC*****PI*INS987654~" +
        "N3*789 PAYER AVENUE*FLOOR 5~" +
        "N4*PAYERVILLE*TX*75001~" +
        "REF*2U*PAYER123~" +
        "REF*G2*PAYER456~" +
        "REF*LU*PAYER789~" +
        "REF*0B*BILLING123~" +
        "REF*1G*BILLING456~" +
        "SE*27*0001~" +
        "GE*1*1~" +
        "IEA*1*000000001~";

    @Test
    public void testLoop2010BBPayerName() {
        try {
            String xml = X12_837_Parser.parseEDI(EDI_WITH_LOOP_2010BB);
            assertNotNull("XML result should not be null", xml);
            assertTrue("XML should contain root element", xml.contains("X12_835_Interchange"));
            assertTrue("XML should contain Loop_2000B_SubscriberDetail", xml.contains("Loop_2000B_SubscriberDetail"));
            assertTrue("XML should contain Loop_2010BA_SubscriberName", xml.contains("Loop_2010BA_SubscriberName"));
            assertTrue("XML should contain Loop_2010BB_PayerName", xml.contains("Loop_2010BB_PayerName"));
            
            // Parse to object and verify Loop 2010BB fields
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Verify Loop 2000B exists
            assertNotNull("Loop 2000B should not be null", interchange.getLoop2000BSubscriberDetail());
            assertEquals("Should have 1 Loop 2000B occurrence", 1, interchange.getLoop2000BSubscriberDetail().size());
            
            X12_837_Interchange.Loop2000BSubscriberDetail loop2000B = interchange.getLoop2000BSubscriberDetail().get(0);
            
            // Verify Loop 2010BA exists (Subscriber Name)
            assertNotNull("Loop 2010BA should not be null", loop2000B.getLoop2010BASubscriberName());
            
            // Verify Loop 2010BB exists (Payer Name)
            assertNotNull("Loop 2010BB should not be null", loop2000B.getLoop2010BBPayerName());
            X12_837_Interchange.Loop2010BBPayerName loop2010BB = loop2000B.getLoop2010BBPayerName();
            
            // Verify NM1 segment (Payer Name)
            assertNotNull("Payer name should not be null", loop2010BB.getPayerName());
            assertEquals("PR", loop2010BB.getPayerName().getEntityIdentifierCode());
            assertEquals("2", loop2010BB.getPayerName().getEntityTypeQualifier());
            assertEquals("INSURANCE COMPANY LLC", loop2010BB.getPayerName().getNameLastOrOrganizationName());
            assertEquals("PI", loop2010BB.getPayerName().getIdentificationCodeQualifier());
            assertEquals("INS987654", loop2010BB.getPayerName().getIdentificationCode());
            
            // Verify N3 segment (Payer Address)
            assertNotNull("Payer address should not be null", loop2010BB.getPayerAddress());
            assertEquals("789 PAYER AVENUE", loop2010BB.getPayerAddress().getAddressInformation());
            assertEquals("FLOOR 5", loop2010BB.getPayerAddress().getAddressInformation2());
            
            // Verify N4 segment (Payer City/State/Zip)
            assertNotNull("Payer city/state/zip should not be null", loop2010BB.getPayerCityStateZip());
            assertEquals("PAYERVILLE", loop2010BB.getPayerCityStateZip().getCityName());
            assertEquals("TX", loop2010BB.getPayerCityStateZip().getStateOrProvinceCode());
            assertEquals("75001", loop2010BB.getPayerCityStateZip().getPostalCode());
            
            // Verify payer secondary ID REF segments (up to 3)
            assertNotNull("Payer secondary ID list should not be null", loop2010BB.getPayerSecondaryId());
            assertEquals("Should have 3 payer secondary ID REF segments", 3, loop2010BB.getPayerSecondaryId().size());
            
            // First payer secondary ID
            assertEquals("2U", loop2010BB.getPayerSecondaryId().get(0).getReferenceIdentificationQualifier());
            assertEquals("PAYER123", loop2010BB.getPayerSecondaryId().get(0).getReferenceIdentification());
            
            // Second payer secondary ID
            assertEquals("G2", loop2010BB.getPayerSecondaryId().get(1).getReferenceIdentificationQualifier());
            assertEquals("PAYER456", loop2010BB.getPayerSecondaryId().get(1).getReferenceIdentification());
            
            // Third payer secondary ID
            assertEquals("LU", loop2010BB.getPayerSecondaryId().get(2).getReferenceIdentificationQualifier());
            assertEquals("PAYER789", loop2010BB.getPayerSecondaryId().get(2).getReferenceIdentification());
            
            // Verify billing provider secondary ID REF segments (up to 2)
            assertNotNull("Billing provider secondary ID list should not be null", loop2010BB.getBillingProviderSecondaryId());
            assertEquals("Should have 2 billing provider secondary ID REF segments", 2, loop2010BB.getBillingProviderSecondaryId().size());
            
            // First billing provider secondary ID
            assertEquals("0B", loop2010BB.getBillingProviderSecondaryId().get(0).getReferenceIdentificationQualifier());
            assertEquals("BILLING123", loop2010BB.getBillingProviderSecondaryId().get(0).getReferenceIdentification());
            
            // Second billing provider secondary ID
            assertEquals("1G", loop2010BB.getBillingProviderSecondaryId().get(1).getReferenceIdentificationQualifier());
            assertEquals("BILLING456", loop2010BB.getBillingProviderSecondaryId().get(1).getReferenceIdentification());
            
            System.out.println("Successfully parsed EDI with Loop 2010BB:");
            System.out.println(xml);
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse EDI with Loop 2010BB: " + e.getMessage());
        }
    }
}