package org.example.XML;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test class for Loop_2010AB_PayToAddressName functionality.
 */
public class X12_837_Loop2010ABTest {

    /**
     * X12 837 EDI message with Loop 2000A containing both Loop 2010AA and Loop 2010AB.
     */
    private static final String EDI_WITH_LOOP_2010AB = 
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
        "SE*20*0001~" +
        "GE*1*1~" +
        "IEA*1*000000001~";

    @Test
    public void testLoop2010ABPayToAddressName() {
        try {
            String xml = X12_837_Parser.parseEDI(EDI_WITH_LOOP_2010AB);
            assertNotNull("XML result should not be null", xml);
            assertTrue("XML should contain root element", xml.contains("X12_837_Interchange"));
            assertTrue("XML should contain Loop_2000A_BillingProviderDetail", xml.contains("Loop_2000A_BillingProviderDetail"));
            assertTrue("XML should contain Loop_2010AA_BillingProviderDetailHL", xml.contains("Loop_2010AA_BillingProviderDetailHL"));
            assertTrue("XML should contain Loop_2010AB_PayToAddressName", xml.contains("Loop_2010AB_PayToAddressName"));
            
            // Parse to object and verify Loop 2010AB fields
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
            
            // Verify NM1 segment (Pay-To Address Name)
            assertNotNull("Pay-to address name should not be null", loop2010AB.getPayToAddressName());
            assertEquals("87", loop2010AB.getPayToAddressName().getEntityIdentifierCode());
            assertEquals("2", loop2010AB.getPayToAddressName().getEntityTypeQualifier());
            assertEquals("PAY TO PROVIDER LLC", loop2010AB.getPayToAddressName().getNameLastOrOrganizationName());
            assertEquals("XX", loop2010AB.getPayToAddressName().getIdentificationCodeQualifier());
            assertEquals("9876543210", loop2010AB.getPayToAddressName().getIdentificationCode());
            
            // Verify N3 segment (Pay-To Address)
            assertNotNull("Pay-to address should not be null", loop2010AB.getPayToAddress());
            assertEquals("456 PAYMENT BOULEVARD", loop2010AB.getPayToAddress().getAddressInformation());
            assertEquals("FLOOR 5", loop2010AB.getPayToAddress().getAddressInformation2());
            
            // Verify N4 segment (Pay-To City/State/Zip)
            assertNotNull("Pay-to city/state/zip should not be null", loop2010AB.getPayToCityStateZip());
            assertEquals("PAYMENTVILLE", loop2010AB.getPayToCityStateZip().getCityName());
            assertEquals("NY", loop2010AB.getPayToCityStateZip().getStateOrProvinceCode());
            assertEquals("10001", loop2010AB.getPayToCityStateZip().getPostalCode());
            
            System.out.println("Successfully parsed EDI with Loop 2010AB:");
            System.out.println(xml);
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse EDI with Loop 2010AB: " + e.getMessage());
        }
    }
}