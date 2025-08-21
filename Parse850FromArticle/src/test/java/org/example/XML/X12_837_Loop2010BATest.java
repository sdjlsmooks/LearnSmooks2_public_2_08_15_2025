package org.example.XML;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test class for Loop_2010BA_SubscriberName functionality.
 */
public class X12_837_Loop2010BATest {

    /**
     * X12 837 EDI message with Loop 2000B containing Loop 2010BA (Subscriber Name).
     */
    private static final String EDI_WITH_LOOP_2010BA = 
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
        "NM1*IL*1*DOE*JOHN*M***MI*ABC123456~" +
        "N3*456 SUBSCRIBER STREET~" +
        "N4*SUBTOWN*NY*12345~" +
        "DMG*D8*19800501*M~" +
        "REF*SY*SSN123456789~" +
        "REF*Y4*CLAIM987654~" +
        "PER*IC*JANE DOE*TE*5559876543~" +
        "SE*18*0001~" +
        "GE*1*1~" +
        "IEA*1*000000001~";

    @Test
    public void testLoop2010BASubscriberName() {
        try {
            String xml = X12_837_Parser.parseEDI(EDI_WITH_LOOP_2010BA);
            assertNotNull("XML result should not be null", xml);
            assertTrue("XML should contain root element", xml.contains("X12_835_Interchange"));
            assertTrue("XML should contain Loop_2000B_SubscriberDetail", xml.contains("Loop_2000B_SubscriberDetail"));
            assertTrue("XML should contain Loop_2010BA_SubscriberName", xml.contains("Loop_2010BA_SubscriberName"));
            
            // Parse to object and verify Loop 2010BA fields
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Verify Loop 2000B exists
            assertNotNull("Loop 2000B should not be null", interchange.getLoop2000BSubscriberDetail());
            assertEquals("Should have 1 Loop 2000B occurrence", 1, interchange.getLoop2000BSubscriberDetail().size());
            
            X12_837_Interchange.Loop2000BSubscriberDetail loop2000B = interchange.getLoop2000BSubscriberDetail().get(0);
            
            // Verify Loop 2010BA exists within Loop 2000B
            assertNotNull("Loop 2010BA should not be null", loop2000B.getLoop2010BASubscriberName());
            X12_837_Interchange.Loop2010BASubscriberName loop2010BA = loop2000B.getLoop2010BASubscriberName();
            
            // Verify NM1 segment (Subscriber Name)
            assertNotNull("Subscriber name should not be null", loop2010BA.getSubscriberName());
            assertEquals("IL", loop2010BA.getSubscriberName().getEntityIdentifierCode());
            assertEquals("1", loop2010BA.getSubscriberName().getEntityTypeQualifier());
            assertEquals("DOE", loop2010BA.getSubscriberName().getNameLastOrOrganizationName());
            assertEquals("JOHN", loop2010BA.getSubscriberName().getNameFirst());
            assertEquals("M", loop2010BA.getSubscriberName().getNameMiddle());
            assertEquals("MI", loop2010BA.getSubscriberName().getIdentificationCodeQualifier());
            assertEquals("ABC123456", loop2010BA.getSubscriberName().getIdentificationCode());
            
            // Verify N3 segment (Subscriber Address)
            assertNotNull("Subscriber address should not be null", loop2010BA.getSubscriberAddress());
            assertEquals("456 SUBSCRIBER STREET", loop2010BA.getSubscriberAddress().getAddressInformation());
            
            // Verify N4 segment (Subscriber City/State/Zip)
            assertNotNull("Subscriber city/state/zip should not be null", loop2010BA.getSubscriberCityStateZip());
            assertEquals("SUBTOWN", loop2010BA.getSubscriberCityStateZip().getCityName());
            assertEquals("NY", loop2010BA.getSubscriberCityStateZip().getStateOrProvinceCode());
            assertEquals("12345", loop2010BA.getSubscriberCityStateZip().getPostalCode());
            
            // Verify DMG segment (Demographic Info)
            assertNotNull("Subscriber demographic info should not be null", loop2010BA.getSubscriberDemographicInfo());
            assertEquals("D8", loop2010BA.getSubscriberDemographicInfo().getDateTimePeriodFormatQualifier());
            assertEquals("19800501", loop2010BA.getSubscriberDemographicInfo().getDateTimePeriod());
            assertEquals("M", loop2010BA.getSubscriberDemographicInfo().getGenderCode());
            
            // Verify first REF segment (Secondary ID)
            assertNotNull("Subscriber secondary ID should not be null", loop2010BA.getSubscriberSecondaryId());
            assertEquals("SY", loop2010BA.getSubscriberSecondaryId().getReferenceIdentificationQualifier());
            assertEquals("SSN123456789", loop2010BA.getSubscriberSecondaryId().getReferenceIdentification());
            
            // Verify second REF segment (Property and Casualty Claim Number)
            assertNotNull("Property and casualty claim number should not be null", loop2010BA.getPropertyAndCasualtyClaimNumber());
            assertEquals("Y4", loop2010BA.getPropertyAndCasualtyClaimNumber().getReferenceIdentificationQualifier());
            assertEquals("CLAIM987654", loop2010BA.getPropertyAndCasualtyClaimNumber().getReferenceIdentification());
            
            // Verify PER segment (Contact Info)
            assertNotNull("Property casualty contact info should not be null", loop2010BA.getPropertyCasualtyContactInfo());
            assertEquals("IC", loop2010BA.getPropertyCasualtyContactInfo().getContactFunctionCode());
            assertEquals("JANE DOE", loop2010BA.getPropertyCasualtyContactInfo().getName());
            assertEquals("TE", loop2010BA.getPropertyCasualtyContactInfo().getCommunicationNumberQualifier());
            assertEquals("5559876543", loop2010BA.getPropertyCasualtyContactInfo().getCommunicationNumber());
            
            System.out.println("Successfully parsed EDI with Loop 2010BA:");
            System.out.println(xml);
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse EDI with Loop 2010BA: " + e.getMessage());
        }
    }
}