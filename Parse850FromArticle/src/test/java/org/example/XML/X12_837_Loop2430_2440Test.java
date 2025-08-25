package org.example.XML;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test class for Loop 2430 and Loop 2440
 */
public class X12_837_Loop2430_2440Test {

    /**
     * Sample X12 837 EDI message with Loop 2430 and 2440 segments
     */
    private static final String EDI_WITH_LOOP_2430_2440 = 
        "ISA*00*          *00*          *ZZ*SENDER123      *ZZ*RECEIVER456    *210901*1200*^*00501*000000001*0*P*:~" +
        "GS*HC*SENDER123*RECEIVER456*20210901*1200*1*X*005010X223A2~" +
        "ST*837*0001*005010X223A2~" +
        "BHT*0019*00*REF123456*20210901*1200*CH~" +
        "NM1*41*2*SUBMITTER ORGANIZATION*****46*SUB12345~" +
        "NM1*40*2*RECEIVER CORPORATION*****46*REC67890~" +
        "HL*1**20*1~" +  // Billing Provider Hierarchical Level
        "NM1*85*2*BILLING PROVIDER INC*****XX*1234567890~" +
        "N3*123 MAIN STREET~" +
        "N4*ANYTOWN*CA*90210~" +
        "REF*EI*123456789~" +
        "HL*2*1*22*0~" +  // Subscriber Hierarchical Level
        "SBR*P*18*******CI~" +
        "NM1*IL*1*DOE*JOHN*A***MI*123456789~" +
        "N3*456 PATIENT STREET~" +
        "N4*PATIENTVILLE*CA*90211~" +
        "DMG*D8*19700101*M~" +
        "NM1*PR*2*INSURANCE COMPANY*****PI*INS987654~" +
        "CLM*CLAIM001*1000.00***11:B:1*Y*A*Y~" +  // Loop 2300 - Claim Information
        // Loop 2400 - Service Line 1 with Adjudication Information
        "LX*1~" +
        "SV1*HC:99213*250.00*UN*1***1~" +
        "DTP*472*D8*20210901~" +
        // Loop 2430 - Line Adjudication Information
        "SVD*PAYER123*200.00*HC:99213**1~" +  // Other payer paid $200
        "CAS*PR*1*50.00~" +  // Patient responsibility $50
        "DTP*573*D8*20210815~" +  // Check/remittance date
        "AMT*EAF*50.00~" +  // Remaining patient liability
        // Loop 2400 - Service Line 2 with Form Identification
        "LX*2~" +
        "SV1*HC:99214*350.00*UN*1***2~" +
        "DTP*472*D8*20210901~" +
        // Loop 2440 - Form Identification Code
        "LQ*UT*1234~" +  // Form identification code
        "FRM*1*Y~" +  // Question 1: Yes
        "FRM*2*N~" +  // Question 2: No
        "FRM*3A*RESPONSE TEXT~" +  // Question 3A: Text response
        // Loop 2400 - Service Line 3 with Multiple Adjudications
        "LX*3~" +
        "SV1*HC:99215*400.00*UN*1***3~" +
        "DTP*472*D8*20210901~" +
        // First Loop 2430 - Primary payer
        "SVD*PRIMARY789*300.00*HC:99215**1~" +
        "CAS*CO*42*50.00~" +  // Contractual adjustment
        "CAS*PR*2*50.00~" +  // Coinsurance
        "DTP*573*D8*20210810~" +
        // Second Loop 2430 - Secondary payer
        "SVD*SECONDARY456*40.00*HC:99215**1~" +
        "CAS*PR*3*10.00~" +  // Copayment
        "DTP*573*D8*20210820~" +
        "AMT*EAF*10.00~" +
        // Loop 2440 - Multiple form responses
        "LQ*AS*FORM456~" +
        "FRM*Q1*ANSWER1*ANSWER2*ANSWER3~" +  // Multiple responses
        "FRM*Q2*TEXT RESPONSE FOR Q2~" +
        "FRM*Q3*A*B*C*D*E~" +  // All 5 response fields used
        "SE*48*0001~" +
        "GE*1*1~" +
        "IEA*1*000000001~";

    @Test
    public void testLoop2430LineAdjudication() {
        try {
            // Parse EDI to XML
            String xml = X12_837_Parser.parseEDI(EDI_WITH_LOOP_2430_2440);
            assertNotNull("XML result should not be null", xml);
            
            // Check for Loop 2430 elements
            assertTrue("XML should contain Loop 2430", xml.contains("Loop_2430_LineAdjudicationInformation"));
            assertTrue("XML should contain line adjudication information", xml.contains("line-adjudication-information"));
            assertTrue("XML should contain line adjustment", xml.contains("line-adjustment"));
            assertTrue("XML should contain check/remittance date", xml.contains("line-check-or-remittance-date"));
            assertTrue("XML should contain remaining patient liability", xml.contains("remaining-patient-liability"));
            
            // Parse XML to Object
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Navigate to Loop 2400
            assertNotNull("Loop 2300 should not be null", interchange.getLoop2300ClaimInformation());
            X12_837_Interchange.Loop2300ClaimInformation claim = interchange.getLoop2300ClaimInformation().get(0);
            assertNotNull("Loop 2400 should not be null", claim.getLoop2400ServiceLineInformation());
            
            // Get first service line with adjudication
            X12_837_Loop2400_Classes.Loop2400ServiceLineInformation line1 = 
                claim.getLoop2400ServiceLineInformation().get(0);
            
            // Verify Loop 2430 - Line Adjudication Information
            assertNotNull("Loop 2430 should not be null", line1.getLoop2430LineAdjudicationInformation());
            assertTrue("Should have at least one adjudication", 
                line1.getLoop2430LineAdjudicationInformation().size() > 0);
            
            X12_837_Loop2430_2440_Classes.Loop2430LineAdjudicationInformation adjudication = 
                line1.getLoop2430LineAdjudicationInformation().get(0);
            
            // Verify SVD segment
            assertNotNull("SVD segment should not be null", adjudication.getLineAdjudicationInformation());
            assertEquals("PAYER123", adjudication.getLineAdjudicationInformation().getOtherPayerPrimaryIdentifier());
            assertEquals("200.00", adjudication.getLineAdjudicationInformation().getServiceLinePaidAmount());
            assertEquals("HC:99213", adjudication.getLineAdjudicationInformation().getCompositeMedicalProcedureIdentifier());
            assertEquals("1", adjudication.getLineAdjudicationInformation().getPaidServiceUnitCount());
            
            // Verify CAS segment (line adjustment)
            assertNotNull("Line adjustment should not be null", adjudication.getLineAdjustment());
            assertTrue("Should have at least one adjustment", adjudication.getLineAdjustment().size() > 0);
            assertEquals("PR", adjudication.getLineAdjustment().get(0).getClaimAdjustmentGroupCode());
            assertEquals("1", adjudication.getLineAdjustment().get(0).getClaimAdjustmentReasonCode());
            assertEquals("50.00", adjudication.getLineAdjustment().get(0).getMonetaryAmount());
            
            // Verify DTP segment (check/remittance date)
            assertNotNull("Check/remittance date should not be null", adjudication.getLineCheckOrRemittanceDate());
            assertEquals("573", adjudication.getLineCheckOrRemittanceDate().getDateTimeQualifier());
            assertEquals("20210815", adjudication.getLineCheckOrRemittanceDate().getDateTimePeriod());
            
            // Verify AMT segment (remaining patient liability)
            assertNotNull("Remaining patient liability should not be null", adjudication.getRemainingPatientLiability());
            assertEquals("EAF", adjudication.getRemainingPatientLiability().getAmountQualifierCode());
            assertEquals("50.00", adjudication.getRemainingPatientLiability().getMonetaryAmount());
            
            // Test service line 3 with multiple adjudications
            X12_837_Loop2400_Classes.Loop2400ServiceLineInformation line3 = 
                claim.getLoop2400ServiceLineInformation().get(2);
            assertNotNull("Loop 2430 for line 3 should not be null", line3.getLoop2430LineAdjudicationInformation());
            assertEquals("Should have 2 adjudications", 2, line3.getLoop2430LineAdjudicationInformation().size());
            
            // Verify primary payer adjudication
            X12_837_Loop2430_2440_Classes.Loop2430LineAdjudicationInformation primary = 
                line3.getLoop2430LineAdjudicationInformation().get(0);
            assertEquals("PRIMARY789", primary.getLineAdjudicationInformation().getOtherPayerPrimaryIdentifier());
            assertEquals("300.00", primary.getLineAdjudicationInformation().getServiceLinePaidAmount());
            
            // Verify secondary payer adjudication
            X12_837_Loop2430_2440_Classes.Loop2430LineAdjudicationInformation secondary = 
                line3.getLoop2430LineAdjudicationInformation().get(1);
            assertEquals("SECONDARY456", secondary.getLineAdjudicationInformation().getOtherPayerPrimaryIdentifier());
            assertEquals("40.00", secondary.getLineAdjudicationInformation().getServiceLinePaidAmount());
            assertNotNull("Secondary should have remaining liability", secondary.getRemainingPatientLiability());
            assertEquals("10.00", secondary.getRemainingPatientLiability().getMonetaryAmount());
            
            System.out.println("Successfully parsed Loop 2430 Line Adjudication Information");
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse EDI with Loop 2430: " + e.getMessage());
        }
    }
    
    @Test
    public void testLoop2440FormIdentification() {
        try {
            // Parse EDI to XML
            String xml = X12_837_Parser.parseEDI(EDI_WITH_LOOP_2430_2440);
            assertNotNull("XML result should not be null", xml);
            
            // Check for Loop 2440 elements
            assertTrue("XML should contain Loop 2440", xml.contains("Loop_2440_FormIdentificationCode"));
            assertTrue("XML should contain form identification code", xml.contains("form-identification-code"));
            assertTrue("XML should contain supporting documentation", xml.contains("supporting-documentation"));
            
            // Parse XML to Object
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Navigate to Loop 2400
            X12_837_Interchange.Loop2300ClaimInformation claim = interchange.getLoop2300ClaimInformation().get(0);
            
            // Get second service line with form identification
            X12_837_Loop2400_Classes.Loop2400ServiceLineInformation line2 = 
                claim.getLoop2400ServiceLineInformation().get(1);
            
            // Verify Loop 2440 - Form Identification Code
            assertNotNull("Loop 2440 should not be null", line2.getLoop2440FormIdentificationCode());
            assertTrue("Should have at least one form", line2.getLoop2440FormIdentificationCode().size() > 0);
            
            X12_837_Loop2430_2440_Classes.Loop2440FormIdentificationCode form = 
                line2.getLoop2440FormIdentificationCode().get(0);
            
            // Verify LQ segment
            assertNotNull("LQ segment should not be null", form.getFormIdentificationCode());
            assertEquals("UT", form.getFormIdentificationCode().getCodeListQualifierCode());
            assertEquals("1234", form.getFormIdentificationCode().getIndustryCode());
            
            // Verify FRM segments
            assertNotNull("Supporting documentation should not be null", form.getSupportingDocumentation());
            assertEquals("Should have 3 FRM segments", 3, form.getSupportingDocumentation().size());
            
            // Verify first FRM
            X12_837_Loop2430_2440_Classes.FRMSegment frm1 = form.getSupportingDocumentation().get(0);
            assertEquals("1", frm1.getQuestionNumberLetter());
            assertEquals("Y", frm1.getQuestionResponse());
            
            // Verify second FRM
            X12_837_Loop2430_2440_Classes.FRMSegment frm2 = form.getSupportingDocumentation().get(1);
            assertEquals("2", frm2.getQuestionNumberLetter());
            assertEquals("N", frm2.getQuestionResponse());
            
            // Verify third FRM
            X12_837_Loop2430_2440_Classes.FRMSegment frm3 = form.getSupportingDocumentation().get(2);
            assertEquals("3A", frm3.getQuestionNumberLetter());
            assertEquals("RESPONSE TEXT", frm3.getQuestionResponse());
            
            // Test service line 3 with multiple forms and responses
            X12_837_Loop2400_Classes.Loop2400ServiceLineInformation line3 = 
                claim.getLoop2400ServiceLineInformation().get(2);
            assertNotNull("Loop 2440 for line 3 should not be null", line3.getLoop2440FormIdentificationCode());
            assertTrue("Should have at least one form", line3.getLoop2440FormIdentificationCode().size() > 0);
            
            X12_837_Loop2430_2440_Classes.Loop2440FormIdentificationCode form2 = 
                line3.getLoop2440FormIdentificationCode().get(0);
            assertEquals("AS", form2.getFormIdentificationCode().getCodeListQualifierCode());
            assertEquals("FORM456", form2.getFormIdentificationCode().getIndustryCode());
            
            // Verify FRM with multiple responses
            assertNotNull("Supporting documentation should not be null", form2.getSupportingDocumentation());
            assertEquals("Should have 3 FRM segments", 3, form2.getSupportingDocumentation().size());
            
            // Check FRM with multiple response fields
            X12_837_Loop2430_2440_Classes.FRMSegment frmMulti = form2.getSupportingDocumentation().get(0);
            assertEquals("Q1", frmMulti.getQuestionNumberLetter());
            assertEquals("ANSWER1", frmMulti.getQuestionResponse());
            assertEquals("ANSWER2", frmMulti.getQuestionResponse2());
            assertEquals("ANSWER3", frmMulti.getQuestionResponse3());
            
            // Check FRM with all 5 response fields
            X12_837_Loop2430_2440_Classes.FRMSegment frmAll = form2.getSupportingDocumentation().get(2);
            assertEquals("Q3", frmAll.getQuestionNumberLetter());
            assertEquals("A", frmAll.getQuestionResponse());
            assertEquals("B", frmAll.getQuestionResponse2());
            assertEquals("C", frmAll.getQuestionResponse3());
            assertEquals("D", frmAll.getQuestionResponse4());
            assertEquals("E", frmAll.getQuestionResponse5());
            
            System.out.println("Successfully parsed Loop 2440 Form Identification Code");
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse EDI with Loop 2440: " + e.getMessage());
        }
    }
}