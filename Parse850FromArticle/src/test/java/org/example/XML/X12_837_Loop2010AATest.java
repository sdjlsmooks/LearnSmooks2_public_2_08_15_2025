package org.example.XML;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test class for Loop_2010AA_BillingProviderDetailHL functionality.
 */
public class X12_837_Loop2010AATest {

    /**
     * X12 837 EDI message with Loop 2000A containing Loop 2010AA (Billing Provider Detail).
     */
    private static final String EDI_WITH_LOOP_2010AA = 
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
        "SE*17*0001~" +
        "GE*1*1~" +
        "IEA*1*000000001~";

    @Test
    public void testLoop2010AABillingProviderDetail() {
        try {
            String xml = X12_837_Parser.parseEDI(EDI_WITH_LOOP_2010AA);
            assertNotNull("XML result should not be null", xml);
            assertTrue("XML should contain root element", xml.contains("X12_837_Interchange"));
            assertTrue("XML should contain Loop_2000A_BillingProviderDetail", xml.contains("Loop_2000A_BillingProviderDetail"));
            assertTrue("XML should contain Loop_2010AA_BillingProviderDetailHL", xml.contains("Loop_2010AA_BillingProviderDetailHL"));
            
            // Parse to object and verify Loop 2010AA fields
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Verify Loop 2000A exists
            assertNotNull("Loop 2000A should not be null", interchange.getLoop2000ABillingProviderDetail());
            assertEquals("Should have 1 Loop 2000A occurrence", 1, interchange.getLoop2000ABillingProviderDetail().size());
            
            X12_837_Interchange.Loop2000ABillingProviderDetail loop2000A = interchange.getLoop2000ABillingProviderDetail().get(0);
            
            // Verify Loop 2010AA exists within Loop 2000A
            assertNotNull("Loop 2010AA should not be null", loop2000A.getLoop2010AABillingProviderDetailHL());
            X12_837_Interchange.Loop2010AABillingProviderDetailHL loop2010AA = loop2000A.getLoop2010AABillingProviderDetailHL();
            
            // Verify NM1 segment (Billing Provider Name)
            assertNotNull("Billing provider name should not be null", loop2010AA.getBillingProviderName());
            assertEquals("85", loop2010AA.getBillingProviderName().getEntityIdentifierCode());
            assertEquals("2", loop2010AA.getBillingProviderName().getEntityTypeQualifier());
            assertEquals("BILLING PROVIDER INC", loop2010AA.getBillingProviderName().getNameLastOrOrganizationName());
            assertEquals("XX", loop2010AA.getBillingProviderName().getIdentificationCodeQualifier());
            assertEquals("1234567890", loop2010AA.getBillingProviderName().getIdentificationCode());
            
            // Verify N3 segment (Address)
            assertNotNull("Billing provider address should not be null", loop2010AA.getBillingProviderAddress());
            assertEquals("123 MAIN STREET", loop2010AA.getBillingProviderAddress().getAddressInformation());
            assertEquals("SUITE 100", loop2010AA.getBillingProviderAddress().getAddressInformation2());
            
            // Verify N4 segment (City/State/Zip)
            assertNotNull("Billing provider city/state/zip should not be null", loop2010AA.getBillingProviderCityStateZip());
            assertEquals("ANYTOWN", loop2010AA.getBillingProviderCityStateZip().getCityName());
            assertEquals("CA", loop2010AA.getBillingProviderCityStateZip().getStateOrProvinceCode());
            assertEquals("90210", loop2010AA.getBillingProviderCityStateZip().getPostalCode());
            
            // Verify REF segment (Tax ID - required)
            assertNotNull("Billing provider tax ID should not be null", loop2010AA.getBillingProviderTaxId());
            assertEquals("EI", loop2010AA.getBillingProviderTaxId().getReferenceIdentificationQualifier());
            assertEquals("12-3456789", loop2010AA.getBillingProviderTaxId().getReferenceIdentification());
            
            // Verify additional REF segments (UPIN/License Info - optional, up to 2)
            assertNotNull("UPIN/License info list should not be null", loop2010AA.getBillingProviderUpinLicenseInfo());
            assertEquals("Should have 2 UPIN/License REF segments", 2, loop2010AA.getBillingProviderUpinLicenseInfo().size());
            
            X12_837_Interchange.REFSegment ref1 = loop2010AA.getBillingProviderUpinLicenseInfo().get(0);
            assertEquals("SY", ref1.getReferenceIdentificationQualifier());
            assertEquals("PROVIDER789", ref1.getReferenceIdentification());
            
            X12_837_Interchange.REFSegment ref2 = loop2010AA.getBillingProviderUpinLicenseInfo().get(1);
            assertEquals("0B", ref2.getReferenceIdentificationQualifier());
            assertEquals("STATE1234", ref2.getReferenceIdentification());
            
            // Verify PER segments (Contact Info - optional, up to 2)
            assertNotNull("Contact info list should not be null", loop2010AA.getBillingProviderContactInfo());
            assertEquals("Should have 2 PER segments", 2, loop2010AA.getBillingProviderContactInfo().size());
            
            X12_837_Interchange.PERSegment per1 = loop2010AA.getBillingProviderContactInfo().get(0);
            assertEquals("IC", per1.getContactFunctionCode());
            assertEquals("JOHN BILLING", per1.getName());
            assertEquals("TE", per1.getCommunicationNumberQualifier());
            assertEquals("5551234567", per1.getCommunicationNumber());
            assertEquals("FX", per1.getCommunicationNumberQualifier2());
            assertEquals("5551234568", per1.getCommunicationNumber2());
            
            X12_837_Interchange.PERSegment per2 = loop2010AA.getBillingProviderContactInfo().get(1);
            assertEquals("IC", per2.getContactFunctionCode());
            assertEquals("JANE ADMIN", per2.getName());
            assertEquals("EM", per2.getCommunicationNumberQualifier());
            assertEquals("admin@provider.com", per2.getCommunicationNumber());
            
            System.out.println("Successfully parsed EDI with Loop 2010AA:");
            System.out.println(xml);
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse EDI with Loop 2010AA: " + e.getMessage());
        }
    }
}