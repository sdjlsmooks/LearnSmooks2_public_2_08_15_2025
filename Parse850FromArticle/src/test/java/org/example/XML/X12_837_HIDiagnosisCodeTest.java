package org.example.XML;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test class for HI Health Care Diagnosis Code with ABK and BK prefixes.
 */
public class X12_837_HIDiagnosisCodeTest {

    /**
     * X12 837 EDI message with HI segment using BK prefix.
     */
    private static final String EDI_WITH_BK_PREFIX = 
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
        "CLM*CLAIM123*1000.00*MC*11:B:1**Y*A*Y*Y~" +
        // HI with BK prefix (Principal Diagnosis)
        "HI*BK:25000*BF:53081*BF:40390~" +
        "SE*13*0001~" +
        "GE*1*1~" +
        "IEA*1*000000001~";

    /**
     * X12 837 EDI message with HI segment using ABK prefix.
     */
    private static final String EDI_WITH_ABK_PREFIX = 
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
        "CLM*CLAIM123*1000.00*MC*11:B:1**Y*A*Y*Y~" +
        // HI with ABK prefix (Principal Diagnosis - ICD-10)
        "HI*ABK:E11.9*ABF:I10*ABF:Z79.4~" +
        "SE*13*0001~" +
        "GE*1*1~" +
        "IEA*1*000000001~";

    @Test
    public void testHIDiagnosisCodeWithBKPrefix() {
        try {
            String xml = X12_837_Parser.parseEDI(EDI_WITH_BK_PREFIX);
            assertNotNull("XML result should not be null", xml);
            assertTrue("XML should contain health-care-diagnosis-code", xml.contains("health-care-diagnosis-code"));
            
            // Parse to object and verify HI field
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Verify HI segment with BK prefix
            assertNotNull("Loop 2300 should not be null", interchange.getLoop2300ClaimInformation());
            assertEquals("Should have 1 Loop 2300", 1, interchange.getLoop2300ClaimInformation().size());
            X12_837_Interchange.Loop2300ClaimInformation loop2300 = interchange.getLoop2300ClaimInformation().get(0);
            
            assertNotNull("Health care diagnosis code should not be null", loop2300.getHealthCareDiagnosisCode());
            assertTrue("Diagnosis code should start with BK", 
                loop2300.getHealthCareDiagnosisCode().getHealthCareCodeInformation().startsWith("BK:"));
            assertEquals("BK:25000", loop2300.getHealthCareDiagnosisCode().getHealthCareCodeInformation());
            assertEquals("BF:53081", loop2300.getHealthCareDiagnosisCode().getHealthCareCodeInformation2());
            assertEquals("BF:40390", loop2300.getHealthCareDiagnosisCode().getHealthCareCodeInformation3());
            
            System.out.println("Successfully parsed HI segment with BK prefix");
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse EDI with BK prefix: " + e.getMessage());
        }
    }

    @Test
    public void testHIDiagnosisCodeWithABKPrefix() {
        try {
            String xml = X12_837_Parser.parseEDI(EDI_WITH_ABK_PREFIX);
            assertNotNull("XML result should not be null", xml);
            assertTrue("XML should contain health-care-diagnosis-code", xml.contains("health-care-diagnosis-code"));
            
            // Parse to object and verify HI field
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Verify HI segment with ABK prefix
            assertNotNull("Loop 2300 should not be null", interchange.getLoop2300ClaimInformation());
            assertEquals("Should have 1 Loop 2300", 1, interchange.getLoop2300ClaimInformation().size());
            X12_837_Interchange.Loop2300ClaimInformation loop2300 = interchange.getLoop2300ClaimInformation().get(0);
            
            assertNotNull("Health care diagnosis code should not be null", loop2300.getHealthCareDiagnosisCode());
            assertTrue("Diagnosis code should start with ABK", 
                loop2300.getHealthCareDiagnosisCode().getHealthCareCodeInformation().startsWith("ABK:"));
            assertEquals("ABK:E11.9", loop2300.getHealthCareDiagnosisCode().getHealthCareCodeInformation());
            assertEquals("ABF:I10", loop2300.getHealthCareDiagnosisCode().getHealthCareCodeInformation2());
            assertEquals("ABF:Z79.4", loop2300.getHealthCareDiagnosisCode().getHealthCareCodeInformation3());
            
            System.out.println("Successfully parsed HI segment with ABK prefix");
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse EDI with ABK prefix: " + e.getMessage());
        }
    }
}