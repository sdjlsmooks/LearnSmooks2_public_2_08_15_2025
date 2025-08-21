package org.example.XML;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test class for Loop_2010AC_PayToPlanName functionality.
 */
public class X12_837_Loop2010ACTest {

    /**
     * X12 837 EDI message with Loop 2000A containing Loop 2010AA, 2010AB, and 2010AC.
     */
    private static final String EDI_WITH_LOOP_2010AC = 
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
    public void testLoop2010ACPayToPlanName() {
        try {
            String xml = X12_837_Parser.parseEDI(EDI_WITH_LOOP_2010AC);
            assertNotNull("XML result should not be null", xml);
            assertTrue("XML should contain root element", xml.contains("X12_835_Interchange"));
            assertTrue("XML should contain Loop_2000A_BillingProviderDetail", xml.contains("Loop_2000A_BillingProviderDetail"));
            assertTrue("XML should contain Loop_2010AA_BillingProviderDetailHL", xml.contains("Loop_2010AA_BillingProviderDetailHL"));
            assertTrue("XML should contain Loop_2010AB_PayToAddressName", xml.contains("Loop_2010AB_PayToAddressName"));
            assertTrue("XML should contain Loop_2010AC_PayToPlanName", xml.contains("Loop_2010AC_PayToPlanName"));
            
            // Parse to object and verify Loop 2010AC fields
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Verify Loop 2000A exists
            assertNotNull("Loop 2000A should not be null", interchange.getLoop2000ABillingProviderDetail());
            assertEquals("Should have 1 Loop 2000A occurrence", 1, interchange.getLoop2000ABillingProviderDetail().size());
            
            X12_837_Interchange.Loop2000ABillingProviderDetail loop2000A = interchange.getLoop2000ABillingProviderDetail().get(0);
            
            // Verify Loop 2010AA exists within Loop 2000A
            assertNotNull("Loop 2010AA should not be null", loop2000A.getLoop2010AABillingProviderDetailHL());
            X12_837_Interchange.Loop2010AABillingProviderDetailHL loop2010AA = loop2000A.getLoop2010AABillingProviderDetailHL();
            
            // Verify Loop 2010AA fields
            assertNotNull("Billing provider name should not be null", loop2010AA.getBillingProviderName());
            assertEquals("85", loop2010AA.getBillingProviderName().getEntityIdentifierCode());
            assertEquals("2", loop2010AA.getBillingProviderName().getEntityTypeQualifier());
            assertEquals("BILLING PROVIDER INC", loop2010AA.getBillingProviderName().getNameLastOrOrganizationName());
            
            // Verify Loop 2010AB exists within Loop 2000A
            assertNotNull("Loop 2010AB should not be null", loop2000A.getLoop2010ABPayToAddressName());
            X12_837_Interchange.Loop2010ABPayToAddressName loop2010AB = loop2000A.getLoop2010ABPayToAddressName();
            
            // Verify Loop 2010AB fields
            assertNotNull("Pay-to address name should not be null", loop2010AB.getPayToAddressName());
            assertEquals("87", loop2010AB.getPayToAddressName().getEntityIdentifierCode());
            assertEquals("2", loop2010AB.getPayToAddressName().getEntityTypeQualifier());
            assertEquals("PAY TO PROVIDER LLC", loop2010AB.getPayToAddressName().getNameLastOrOrganizationName());
            
            // Verify Loop 2010AC exists within Loop 2000A
            assertNotNull("Loop 2010AC should not be null", loop2000A.getLoop2010ACPayToPlanName());
            X12_837_Interchange.Loop2010ACPayToPlanName loop2010AC = loop2000A.getLoop2010ACPayToPlanName();
            
            // Verify NM1 segment (Pay-To Plan Name)
            assertNotNull("Pay-to plan name should not be null", loop2010AC.getPayToPlanName());
            assertEquals("PE", loop2010AC.getPayToPlanName().getEntityIdentifierCode());
            assertEquals("2", loop2010AC.getPayToPlanName().getEntityTypeQualifier());
            assertEquals("HEALTH PLAN INC", loop2010AC.getPayToPlanName().getNameLastOrOrganizationName());
            assertEquals("PI", loop2010AC.getPayToPlanName().getIdentificationCodeQualifier());
            assertEquals("HP123456789", loop2010AC.getPayToPlanName().getIdentificationCode());
            
            // Verify N3 segment (Pay-To Plan Address)
            assertNotNull("Pay-to plan address should not be null", loop2010AC.getPayToPlanAddress());
            assertEquals("789 INSURANCE WAY", loop2010AC.getPayToPlanAddress().getAddressInformation());
            assertEquals("BUILDING A", loop2010AC.getPayToPlanAddress().getAddressInformation2());
            
            // Verify N4 segment (Pay-To Plan City/State/Zip)
            assertNotNull("Pay-to plan city/state/zip should not be null", loop2010AC.getPayToPlanCityStateZip());
            assertEquals("PLANTOWN", loop2010AC.getPayToPlanCityStateZip().getCityName());
            assertEquals("TX", loop2010AC.getPayToPlanCityStateZip().getStateOrProvinceCode());
            assertEquals("75001", loop2010AC.getPayToPlanCityStateZip().getPostalCode());
            
            // Verify REF segment (Secondary ID)
            assertNotNull("Pay-to plan secondary ID should not be null", loop2010AC.getPayToPlanSecondaryId());
            assertEquals("2U", loop2010AC.getPayToPlanSecondaryId().getReferenceIdentificationQualifier());
            assertEquals("SECONDARY987", loop2010AC.getPayToPlanSecondaryId().getReferenceIdentification());
            
            // Verify REF segment (Tax ID)
            assertNotNull("Pay-to plan tax ID should not be null", loop2010AC.getPayToPlanTaxId());
            assertEquals("EI", loop2010AC.getPayToPlanTaxId().getReferenceIdentificationQualifier());
            assertEquals("98-7654321", loop2010AC.getPayToPlanTaxId().getReferenceIdentification());
            
            System.out.println("Successfully parsed EDI with Loop 2010AC:");
            System.out.println(xml);
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse EDI with Loop 2010AC: " + e.getMessage());
        }
    }
}