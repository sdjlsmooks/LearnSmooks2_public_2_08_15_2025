package org.example.XML;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test class for extended Loop_2300_ClaimInformation functionality with additional segments.
 */
public class X12_837_Loop2300ExtendedTest {

    /**
     * X12 837 EDI message with Loop 2300 containing additional segments after AMT.
     */
    private static final String EDI_WITH_EXTENDED_LOOP_2300 = 
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
        // Loop 2300 - Claim Information with extended segments
        "CLM*CLAIM123*1250.00*MC*11:B:1**Y*A*Y*Y~" +
        "AMT*F5*250.00~" +  // Patient Amount Paid
        // REF segments
        "REF*4N*AUTH123~" +  // Service Authorization Exception Code
        "REF*MA*MEDICARE123~" +  // Mandatory Medicare
        "REF*EW*MAMMO456~" +  // Mammogram Certification
        "REF*9F*REFERRAL789~" +  // Referral Number
        "REF*G1*PRIOR123~" +  // Prior Authorization
        "REF*F8*PAYER456~" +  // Payer Claim Control Number
        "REF*X4*CLIA789~" +  // Clinical Laboratory Improvement
        "REF*9A*REPRICE123~" +  // Repriced Claim Number
        "REF*9C*ADJREPRICE456~" +  // Adjusted Repriced Claim Number
        "REF*LX*DEVICE789~" +  // Investigational Device Ex Number
        "REF*D9*TXN123~" +  // Claim ID for TXN Intermediaries
        "REF*EA*MRN456789~" +  // Medical Record Number
        "REF*P4*DEMO789~" +  // Demo Project ID
        "REF*1J*CAREPLAN123~" +  // Care Plan Oversight
        // K3 File Information
        "K3*FILE INFO LINE 1~" +
        "K3*FILE INFO LINE 2~" +
        // NTE Claim Note
        "NTE*ADD*This is a claim note with additional information~" +
        // CR1 Ambulance Transport Info
        "CR1*LB*150*I*A*MI*5~" +
        // CR2 Spinal Manipulation Info
        "CR2*8*12*C2*C3*UN*5*10~" +
        // CRC segments
        "CRC*07*Y*01*02~" +  // Ambulance Certification
        "CRC*E1*N*38~" +  // Patient Condition Vision
        "CRC*75*Y*65~" +  // Homebound Indicator
        "CRC*ZZ*Y*01~" +  // EPSDT Referral
        // HI Health Care Diagnosis Code
        "HI*BK:25000~" +  // Diagnosis Code
        // HCP Claim Pricing/Repricing Info
        "HCP*00*1500.00*1450.00~" +
        "SE*39*0001~" +
        "GE*1*1~" +
        "IEA*1*000000001~";

    @Test
    public void testLoop2300ExtendedSegments() {
        try {
            String xml = X12_837_Parser.parseEDI(EDI_WITH_EXTENDED_LOOP_2300);
            assertNotNull("XML result should not be null", xml);
            assertTrue("XML should contain root element", xml.contains("X12_835_Interchange"));
            assertTrue("XML should contain Loop_2300_ClaimInformation", xml.contains("Loop_2300_ClaimInformation"));
            
            // Parse to object and verify Loop 2300 fields
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Verify Loop 2300 exists
            assertNotNull("Loop 2300 should not be null", interchange.getLoop2300ClaimInformation());
            assertEquals("Should have 1 Loop 2300 occurrence", 1, interchange.getLoop2300ClaimInformation().size());
            
            X12_837_Interchange.Loop2300ClaimInformation loop2300 = interchange.getLoop2300ClaimInformation().get(0);
            
            // Verify CLM segment
            assertNotNull("Claim information should not be null", loop2300.getClaimInformation());
            assertEquals("CLAIM123", loop2300.getClaimInformation().getClaimSubmittersIdentifier());
            
            // Verify AMT segment
            assertNotNull("Patient amount paid should not be null", loop2300.getPatientAmountPaid());
            assertEquals("F5", loop2300.getPatientAmountPaid().getAmountQualifierCode());
            assertEquals("250.00", loop2300.getPatientAmountPaid().getMonetaryAmount());
            
            // Verify REF segments
            assertNotNull("Service authorization exception code should not be null", loop2300.getServiceAuthorizationExceptionCode());
            assertEquals("4N", loop2300.getServiceAuthorizationExceptionCode().getReferenceIdentificationQualifier());
            assertEquals("AUTH123", loop2300.getServiceAuthorizationExceptionCode().getReferenceIdentification());
            
            assertNotNull("Mandatory medicare should not be null", loop2300.getMandatoryMedicare());
            assertEquals("MA", loop2300.getMandatoryMedicare().getReferenceIdentificationQualifier());
            assertEquals("MEDICARE123", loop2300.getMandatoryMedicare().getReferenceIdentification());
            
            assertNotNull("Referral number should not be null", loop2300.getReferralNumber());
            assertEquals("9F", loop2300.getReferralNumber().getReferenceIdentificationQualifier());
            assertEquals("REFERRAL789", loop2300.getReferralNumber().getReferenceIdentification());
            
            assertNotNull("Prior authorization should not be null", loop2300.getPriorAuthorization());
            assertEquals("G1", loop2300.getPriorAuthorization().getReferenceIdentificationQualifier());
            assertEquals("PRIOR123", loop2300.getPriorAuthorization().getReferenceIdentification());
            
            assertNotNull("Medical record number should not be null", loop2300.getMedicalRecordNumber());
            assertEquals("EA", loop2300.getMedicalRecordNumber().getReferenceIdentificationQualifier());
            assertEquals("MRN456789", loop2300.getMedicalRecordNumber().getReferenceIdentification());
            
            // Verify K3 segments
            assertNotNull("File information should not be null", loop2300.getFileInformation());
            assertEquals("Should have 2 K3 segments", 2, loop2300.getFileInformation().size());
            assertEquals("FILE INFO LINE 1", loop2300.getFileInformation().get(0).getFixedFormatInformation());
            assertEquals("FILE INFO LINE 2", loop2300.getFileInformation().get(1).getFixedFormatInformation());
            
            // Verify NTE segment
            assertNotNull("Claim note should not be null", loop2300.getClaimNote());
            assertEquals("ADD", loop2300.getClaimNote().getNoteReferenceCode());
            assertEquals("This is a claim note with additional information", loop2300.getClaimNote().getNoteText());
            
            // Verify CR1 segment
            assertNotNull("Ambulance transport info should not be null", loop2300.getAmbulanceTransportInfo());
            assertEquals("LB", loop2300.getAmbulanceTransportInfo().getUnitOrBasisForMeasurementCode());
            assertEquals("150", loop2300.getAmbulanceTransportInfo().getWeight());
            assertEquals("I", loop2300.getAmbulanceTransportInfo().getAmbulanceTransportCode());
            assertEquals("A", loop2300.getAmbulanceTransportInfo().getAmbulanceTransportReasonCode());
            
            // Verify CR2 segment
            assertNotNull("Spinal manipulation info should not be null", loop2300.getSpinalManipulationInfo());
            assertEquals("8", loop2300.getSpinalManipulationInfo().getCount());
            assertEquals("12", loop2300.getSpinalManipulationInfo().getQuantity());
            assertEquals("C2", loop2300.getSpinalManipulationInfo().getSubluxationLevelCode());
            assertEquals("C3", loop2300.getSpinalManipulationInfo().getSubluxationLevelCode2());
            
            // Verify CRC segments - With discriminators, they should be properly separated
            
            // Ambulance Certification (code-category='07')
            assertNotNull("Ambulance certification should not be null", loop2300.getAmbulanceCertification());
            assertEquals("Should have 1 ambulance certification", 1, loop2300.getAmbulanceCertification().size());
            assertEquals("07", loop2300.getAmbulanceCertification().get(0).getCodeCategory());
            assertEquals("Y", loop2300.getAmbulanceCertification().get(0).getYesNoConditionOrResponseCode());
            assertEquals("01", loop2300.getAmbulanceCertification().get(0).getConditionIndicator());
            assertEquals("02", loop2300.getAmbulanceCertification().get(0).getConditionIndicator2());
            
            // Patient Condition Vision (code-category='E1', 'E2', or 'E3')
            assertNotNull("Patient condition vision should not be null", loop2300.getPatientConditionVision());
            assertEquals("Should have 1 patient condition vision", 1, loop2300.getPatientConditionVision().size());
            assertEquals("E1", loop2300.getPatientConditionVision().get(0).getCodeCategory());
            assertEquals("N", loop2300.getPatientConditionVision().get(0).getYesNoConditionOrResponseCode());
            assertEquals("38", loop2300.getPatientConditionVision().get(0).getConditionIndicator());
            
            // Homebound Indicator (code-category='75')
            assertNotNull("Homebound indicator should not be null", loop2300.getHomeboundIndicator());
            assertEquals("75", loop2300.getHomeboundIndicator().getCodeCategory());
            assertEquals("Y", loop2300.getHomeboundIndicator().getYesNoConditionOrResponseCode());
            assertEquals("65", loop2300.getHomeboundIndicator().getConditionIndicator());
            
            // EPSDT Referral (code-category='ZZ')
            assertNotNull("EPSDT referral should not be null", loop2300.getEpsdtReferral());
            assertEquals("ZZ", loop2300.getEpsdtReferral().getCodeCategory());
            assertEquals("Y", loop2300.getEpsdtReferral().getYesNoConditionOrResponseCode());
            assertEquals("01", loop2300.getEpsdtReferral().getConditionIndicator());
            
            // Verify HI segment
            assertNotNull("Health care diagnosis code should not be null", loop2300.getHealthCareDiagnosisCode());
            assertEquals("BK:25000", loop2300.getHealthCareDiagnosisCode().getHealthCareCodeInformation());
            
            // Verify HCP segment
            assertNotNull("Claim pricing/repricing info should not be null", loop2300.getClaimPricingRepricingInfo());
            assertEquals("00", loop2300.getClaimPricingRepricingInfo().getPricingMethodology());
            assertEquals("1500.00", loop2300.getClaimPricingRepricingInfo().getMonetaryAmount());
            assertEquals("1450.00", loop2300.getClaimPricingRepricingInfo().getMonetaryAmount2());
            
            System.out.println("Successfully parsed EDI with extended Loop 2300:");
            System.out.println(xml);
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse EDI with extended Loop 2300: " + e.getMessage());
        }
    }
}