package org.example.XML;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Comprehensive test class for X12 837 Healthcare Claim with all currently defined loops and segments.
 */
public class X12_837_ComprehensiveTest {

    /**
     * Comprehensive X12 837 EDI message with all currently defined loops and segments in the schema.
     */
    private static final String COMPREHENSIVE_EDI_837 = 
        // ISA - Interchange Control Header
        "ISA*00*          *00*          *ZZ*SENDER123      *ZZ*RECEIVER456    *210901*1200*^*00501*000000001*0*P*:~" +
        // GS - Functional Group Header
        "GS*HC*SENDER123*RECEIVER456*20210901*1200*1*X*005010X223A2~" +
        // ST - Transaction Set Header
        "ST*837*0001*005010X223A2~" +
        // BHT - Beginning of Hierarchical Transaction
        "BHT*0019*00*REF123456*20210901*1200*CH~" +
        
        // Loop 1000A - Submitter Name
        "NM1*41*2*SUBMITTER ORGANIZATION*****46*SUB12345~" +
        "PER*IC*JOHN DOE*TE*5551234567*FX*5559876543*EM*john.doe@example.com~" +
        
        // Loop 1000B - Receiver Name
        "NM1*40*2*RECEIVER CORPORATION*****46*REC67890~" +
        
        // Loop 2000A - Billing Provider Detail
        "HL*1**20*1~" +  // Billing Provider Hierarchical Level
        "PRV*BI*PXC*123456789~" +  // Billing Provider Specialty Information
        "CUR*85*USD~" +  // Foreign Currency Information
        
        // Loop 2010AA - Billing Provider Name
        "NM1*85*2*MEDICAL CENTER*****XX*1234567890~" +
        "N3*123 MAIN STREET~" +
        "N4*ANYTOWN*CA*90210~" +
        "REF*EI*987654321~" +
        "REF*SY*SSN123456789~" +
        "PER*IC*BILLING CONTACT*TE*5551112222*FX*5553334444~" +
        
        // Loop 2010AB - Pay-To Address Name
        "NM1*87*2*PAYMENT CENTER*****XX*9876543210~" +
        "N3*456 PAYMENT AVE~" +
        "N4*PAYTOWN*NY*10001~" +
        
        // Loop 2010AC - Pay-To Plan Name
        "NM1*PE*2*INSURANCE PLAN*****PI*PLAN12345~" +
        "N3*789 INSURANCE BLVD*SUITE 100~" +
        "N4*INSURANCEVILLE*TX*75001~" +
        "REF*2U*PLANREF789~" +
        "REF*FY*FISCAL456~" +
        
        // Loop 2000B - Subscriber Detail
        "HL*2*1*22*0~" +  // Subscriber Hierarchical Level (no child for now)
        "SBR*P*18*GROUP12345******CI~" +  // Subscriber Information
        "PAT*01~" +  // Patient Information
        
        // Loop 2010BA - Subscriber Name
        "NM1*IL*1*SMITH*JOHN*A***MI*MEMBER123456~" +
        "N3*100 PATIENT STREET~" +
        "N4*PATIENTVILLE*CA*90001~" +
        "DMG*D8*19800101*M~" +
        "REF*Y4*PROPERTY123~" +
        "REF*1W*MEMBER987~" +
        "PER*IC*PATIENT CONTACT*TE*5557778888~" +
        
        // Loop 2010BB - Payer Name
        "NM1*PR*2*INSURANCE COMPANY*****PI*PAYER567890~" +
        "N3*200 PAYER LANE~" +
        "N4*PAYERCITY*FL*33001~" +
        "REF*2U*PAYERREF123~" +
        "REF*G2*PAYERID456~" +
        "REF*FY*FISCAL789~" +
        "REF*NF*NAIC012~" +
        "REF*TJ*TAXID345~" +
        
        // Loop 2300 - Claim Information (comprehensive with all segments)
        "CLM*CLAIM789*5000.00*MC*11:B:1*PLACE*Y*A*Y*Y*P~" +
        
        // DTP segments - All date types
        "DTP*431*D8*20210815~" +  // Onset of Illness
        "DTP*454*D8*20210816~" +  // Initial Treatment Date
        "DTP*304*D8*20210820~" +  // Last Seen Date
        "DTP*453*D8*20210817~" +  // Acute Manifestation Date
        "DTP*439*D8*20210814~" +  // Accident Date
        "DTP*484*D8*20210701~" +  // Last Menstrual Period
        "DTP*455*D8*20210710~" +  // Last X-Ray Date
        "DTP*471*D8*20210705~" +  // Hearing/Vision Prescription Date
        "DTP*360*D8*20210801~" +  // Disability Date (Initial)
        "DTP*297*D8*20210810~" +  // Last Worked Date
        "DTP*296*D8*20210901~" +  // Authorized Return to Work Date
        "DTP*435*D8*20210815~" +  // Admission Date
        "DTP*096*D8*20210825~" +  // Discharge Date
        "DTP*090*D8*20210821~" +  // Assumed/Relinquished Care Date 1
        "DTP*090*D8*20210822~" +  // Assumed/Relinquished Care Date 2
        "DTP*444*D8*20210814~" +  // Property Casualty Date 1
        "DTP*444*D8*20210815~" +  // Property Casualty Date 2
        "DTP*050*D8*20210820~" +  // Repricer Received Date 1
        "DTP*050*D8*20210821~" +  // Repricer Received Date 2
        
        // PWK - Claim Supplemental Information
        "PWK*OZ*BM*1*AC*AA*ATT123456*Attachment Description~" +
        
        // CN1 - Contract Information
        "CN1*01*2500.00*90*CONTRACT789*8*V2.0~" +
        
        // AMT - Patient Amount Paid
        "AMT*F5*500.00~" +
        
        // REF segments - All reference types
        "REF*4N*AUTH123~" +  // Service Authorization Exception Code
        "REF*MA*MEDICARE123~" +  // Mandatory Medicare (Section 4081)
        "REF*EW*MAMMO456~" +  // Mammography Certification Number
        "REF*9F*REFERRAL789~" +  // Referral Number
        "REF*G1*PRIOR123~" +  // Prior Authorization
        "REF*F8*PAYER456~" +  // Payer Claim Control Number
        "REF*X4*CLIA789~" +  // Clinical Laboratory Improvement Amendment
        "REF*9A*REPRICE123~" +  // Repriced Claim Number
        "REF*9C*ADJREPRICE456~" +  // Adjusted Repriced Claim Number
        "REF*LX*DEVICE789~" +  // Investigational Device Exemption Number
        "REF*D9*TXN123~" +  // Claim Identifier for Transmission Intermediaries
        "REF*EA*MRN456789~" +  // Medical Record Number
        "REF*P4*DEMO789~" +  // Demonstration Project Identifier
        "REF*1J*CAREPLAN123~" +  // Care Plan Oversight
        
        // K3 - File Information
        "K3*FILE INFO LINE 1~" +
        "K3*FILE INFO LINE 2~" +
        "K3*FILE INFO LINE 3~" +
        
        // NTE - Claim Note
        "NTE*ADD*Additional claim information for processing~" +
        
        // CR1 - Ambulance Transport Information
        "CR1*LB*180*I*A*DH*12~" +
        
        // CR2 - Spinal Manipulation Service Information
        "CR2*12*18*C3*C4*UN*10*20*A*Y~" +
        
        // CRC segments with discriminators
        "CRC*07*Y*01*02*03~" +  // Ambulance Certification
        "CRC*E1*N*38~" +  // Patient Condition Information (Vision)
        "CRC*75*Y*65~" +  // Homebound Indicator
        "CRC*ZZ*Y*01~" +  // EPSDT Referral
        
        // HI - Health Care Diagnosis Code
        "HI*BK:25000*BF:53081~" +  // Principal and Other Diagnosis
        
        // HCP - Claim Pricing/Repricing Information
        "HCP*01*2000.00*1950.00*PROVIDER*10*9000.00*03*8500.00*04*250.00*UN*7~" +
        
        // SE - Transaction Set Trailer
        "SE*150*0001~" +
        // GE - Functional Group Trailer
        "GE*1*1~" +
        // IEA - Interchange Control Trailer
        "IEA*1*000000001~";

    @Test
    public void testComprehensive837Parsing() {
        try {
            String xml = X12_837_Parser.parseEDI(COMPREHENSIVE_EDI_837);
            assertNotNull("XML result should not be null", xml);
            assertTrue("XML should contain root element", xml.contains("X12_835_Interchange"));
            
            // Parse to object and verify major components
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Verify Interchange Header
            assertNotNull("Interchange header should not be null", interchange.getInterchangeHeader());
            assertEquals("00", interchange.getInterchangeHeader().getAuthQual());
            assertEquals("ZZ", interchange.getInterchangeHeader().getSenderQual());
            assertEquals("SENDER123      ", interchange.getInterchangeHeader().getSenderId());
            assertEquals("RECEIVER456    ", interchange.getInterchangeHeader().getReceiverId());
            
            // Verify Group Header
            assertNotNull("Group header should not be null", interchange.getGroupHeader());
            assertEquals("HC", interchange.getGroupHeader().getCode());
            assertEquals("005010X223A2", interchange.getGroupHeader().getVersion());
            
            // Verify Transaction Set Header
            assertNotNull("Transaction set header should not be null", interchange.getTransactionSetHeader());
            assertEquals("837", interchange.getTransactionSetHeader().getCode());
            assertEquals("0001", interchange.getTransactionSetHeader().getTransactionSetControlNumber());
            
            // Verify BHT
            assertNotNull("Beginning hierarchical transaction should not be null", interchange.getBeginningHierarchicalTransaction());
            assertEquals("0019", interchange.getBeginningHierarchicalTransaction().getHierarchicalStructureCode());
            assertEquals("REF123456", interchange.getBeginningHierarchicalTransaction().getReferenceIdentification());
            
            // Verify Loop 1000A - Submitter Name
            assertNotNull("Loop 1000A should not be null", interchange.getLoop1000ASubmitterName());
            assertNotNull("Submitter name should not be null", interchange.getLoop1000ASubmitterName().getSubmitterName());
            assertEquals("41", interchange.getLoop1000ASubmitterName().getSubmitterName().getEntityIdentifierCode());
            assertEquals("SUBMITTER ORGANIZATION", interchange.getLoop1000ASubmitterName().getSubmitterName().getNameLastOrOrganizationName());
            assertNotNull("Submitter contact should not be null", interchange.getLoop1000ASubmitterName().getSubmitterEdiContactInformation());
            assertEquals("JOHN DOE", interchange.getLoop1000ASubmitterName().getSubmitterEdiContactInformation().get(0).getName());
            
            // Verify Loop 1000B - Receiver Name
            assertNotNull("Loop 1000B should not be null", interchange.getLoop1000BReceiverName());
            assertNotNull("Receiver name should not be null", interchange.getLoop1000BReceiverName().getReceiverName());
            assertEquals("40", interchange.getLoop1000BReceiverName().getReceiverName().getEntityIdentifierCode());
            assertEquals("RECEIVER CORPORATION", interchange.getLoop1000BReceiverName().getReceiverName().getNameLastOrOrganizationName());
            
            // Verify Loop 2000A - Billing Provider Detail
            assertNotNull("Loop 2000A should not be null", interchange.getLoop2000ABillingProviderDetail());
            assertEquals("Should have at least 1 Loop 2000A", 1, interchange.getLoop2000ABillingProviderDetail().size());
            X12_837_Interchange.Loop2000ABillingProviderDetail loop2000A = interchange.getLoop2000ABillingProviderDetail().get(0);
            assertNotNull("Billing provider hierarchical level should not be null", loop2000A.getBillingProviderHierarchicalLevel());
            assertEquals("1", loop2000A.getBillingProviderHierarchicalLevel().getHierarchicalIdNumber());
            assertEquals("20", loop2000A.getBillingProviderHierarchicalLevel().getHierarchicalLevelCode());
            
            // Verify Loop 2010AA - Billing Provider Name
            assertNotNull("Loop 2010AA should not be null", loop2000A.getLoop2010AABillingProviderDetailHL());
            assertNotNull("Billing provider name should not be null", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderName());
            assertEquals("MEDICAL CENTER", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderName().getNameLastOrOrganizationName());
            assertNotNull("Billing provider address should not be null", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderAddress());
            assertEquals("123 MAIN STREET", loop2000A.getLoop2010AABillingProviderDetailHL().getBillingProviderAddress().getAddressInformation());
            
            // Verify Loop 2010AB - Pay-To Address Name
            assertNotNull("Loop 2010AB should not be null", loop2000A.getLoop2010ABPayToAddressName());
            assertNotNull("Pay-to address name should not be null", loop2000A.getLoop2010ABPayToAddressName().getPayToAddressName());
            assertEquals("PAYMENT CENTER", loop2000A.getLoop2010ABPayToAddressName().getPayToAddressName().getNameLastOrOrganizationName());
            
            // Verify Loop 2010AC - Pay-To Plan Name
            assertNotNull("Loop 2010AC should not be null", loop2000A.getLoop2010ACPayToPlanName());
            assertNotNull("Pay-to plan name should not be null", loop2000A.getLoop2010ACPayToPlanName().getPayToPlanName());
            assertEquals("INSURANCE PLAN", loop2000A.getLoop2010ACPayToPlanName().getPayToPlanName().getNameLastOrOrganizationName());
            
            // Verify Loop 2000B - Subscriber Detail
            assertNotNull("Loop 2000B should not be null", interchange.getLoop2000BSubscriberDetail());
            assertEquals("Should have at least 1 Loop 2000B", 1, interchange.getLoop2000BSubscriberDetail().size());
            X12_837_Interchange.Loop2000BSubscriberDetail loop2000B = interchange.getLoop2000BSubscriberDetail().get(0);
            assertNotNull("Subscriber hierarchical level should not be null", loop2000B.getSubscriberHierarchicalLevel());
            assertEquals("2", loop2000B.getSubscriberHierarchicalLevel().getHierarchicalIdNumber());
            assertEquals("1", loop2000B.getSubscriberHierarchicalLevel().getHierarchicalParentIdNumber());
            
            // Verify Loop 2010BA - Subscriber Name
            assertNotNull("Loop 2010BA should not be null", loop2000B.getLoop2010BASubscriberName());
            assertNotNull("Subscriber name should not be null", loop2000B.getLoop2010BASubscriberName().getSubscriberName());
            assertEquals("SMITH", loop2000B.getLoop2010BASubscriberName().getSubscriberName().getNameLastOrOrganizationName());
            assertEquals("JOHN", loop2000B.getLoop2010BASubscriberName().getSubscriberName().getNameFirst());
            
            // Verify Loop 2010BB - Payer Name
            assertNotNull("Loop 2010BB should not be null", loop2000B.getLoop2010BBPayerName());
            assertNotNull("Payer name should not be null", loop2000B.getLoop2010BBPayerName().getPayerName());
            assertEquals("INSURANCE COMPANY", loop2000B.getLoop2010BBPayerName().getPayerName().getNameLastOrOrganizationName());
            assertNotNull("Payer secondary id should not be null", loop2000B.getLoop2010BBPayerName().getPayerSecondaryId());
            assertTrue("Should have multiple payer secondary ids", loop2000B.getLoop2010BBPayerName().getPayerSecondaryId().size() > 1);
            
            // Verify Loop 2300 - Claim Information
            assertNotNull("Loop 2300 should not be null", interchange.getLoop2300ClaimInformation());
            assertEquals("Should have at least 1 Loop 2300", 1, interchange.getLoop2300ClaimInformation().size());
            X12_837_Interchange.Loop2300ClaimInformation loop2300 = interchange.getLoop2300ClaimInformation().get(0);
            
            // Verify CLM segment
            assertNotNull("Claim information should not be null", loop2300.getClaimInformation());
            assertEquals("CLAIM789", loop2300.getClaimInformation().getClaimSubmittersIdentifier());
            assertEquals("5000.00", loop2300.getClaimInformation().getMonetaryAmount());
            
            // Verify multiple DTP segments
            assertNotNull("Onset illness date should not be null", loop2300.getOnsetIllnessOrSymptom());
            assertEquals("20210815", loop2300.getOnsetIllnessOrSymptom().getDateTimePeriod());
            assertNotNull("Initial treatment date should not be null", loop2300.getInitialTreatmentDate());
            assertNotNull("Last seen date should not be null", loop2300.getLastSeenDate());
            assertNotNull("Acute manifestation date should not be null", loop2300.getAcuteManifestationDate());
            assertNotNull("Accident date should not be null", loop2300.getAccidentDate());
            assertNotNull("Last menstrual period date should not be null", loop2300.getLastMenstrualPeriodDate());
            assertNotNull("Last X-ray date should not be null", loop2300.getLastXRayDate());
            assertNotNull("Hearing/vision prescription date should not be null", loop2300.getHearingVisionPrescriptionDate());
            assertNotNull("Disability date should not be null", loop2300.getDisabilityDate());
            assertNotNull("Last worked date should not be null", loop2300.getLastWorkedDate());
            assertNotNull("Authorized return to work date should not be null", loop2300.getAuthorizedReturnToWorkDate());
            assertNotNull("Admission date should not be null", loop2300.getAdmissionDate());
            assertNotNull("Discharge date should not be null", loop2300.getDischargeDate());
            
            // Verify list DTP segments
            assertNotNull("Assumed/relinquished care dates should not be null", loop2300.getAssumedRelinquishedCareDate());
            assertEquals("Should have 2 assumed/relinquished care dates", 2, loop2300.getAssumedRelinquishedCareDate().size());
            assertNotNull("Property casualty dates should not be null", loop2300.getPropertyCasualtyDate());
            assertEquals("Should have 2 property casualty dates", 2, loop2300.getPropertyCasualtyDate().size());
            assertNotNull("Repricer received dates should not be null", loop2300.getRepricerReceivedDate());
            assertEquals("Should have 2 repricer received dates", 2, loop2300.getRepricerReceivedDate().size());
            
            // Verify PWK segment
            assertNotNull("Claim supplemental information should not be null", loop2300.getClaimSupplementalInformation());
            assertEquals("OZ", loop2300.getClaimSupplementalInformation().getReportTypeCode());
            assertEquals("ATT123456", loop2300.getClaimSupplementalInformation().getIdentificationCode());
            assertEquals("Attachment Description", loop2300.getClaimSupplementalInformation().getDescription());
            
            // Verify CN1 segment
            assertNotNull("Contract information should not be null", loop2300.getContractInformation());
            assertEquals("CONTRACT789", loop2300.getContractInformation().getContractCode());
            assertEquals("V2.0", loop2300.getContractInformation().getContractVersionIdentifier());
            
            // Verify AMT segment
            assertNotNull("Patient amount paid should not be null", loop2300.getPatientAmountPaid());
            assertEquals("500.00", loop2300.getPatientAmountPaid().getMonetaryAmount());
            
            // Verify all REF segments
            assertNotNull("Service authorization exception code should not be null", loop2300.getServiceAuthorizationExceptionCode());
            assertEquals("AUTH123", loop2300.getServiceAuthorizationExceptionCode().getReferenceIdentification());
            assertNotNull("Mandatory medicare should not be null", loop2300.getMandatoryMedicare());
            assertNotNull("Mammogram certification should not be null", loop2300.getMammogramCertification());
            assertNotNull("Referral number should not be null", loop2300.getReferralNumber());
            assertNotNull("Prior authorization should not be null", loop2300.getPriorAuthorization());
            assertNotNull("Payer claim control number should not be null", loop2300.getPayerClaimControlNumber());
            assertNotNull("Clinical laboratory improvement should not be null", loop2300.getClinicalLaboratoryImprovement());
            assertNotNull("Repriced claim number should not be null", loop2300.getRepricedClaimNumber());
            assertNotNull("Adjusted repriced claim number should not be null", loop2300.getAdjustedRepricedClaimNumber());
            assertNotNull("Investigational device ex number should not be null", loop2300.getInvestigationalDeviceExNumber());
            assertNotNull("Claim ID for TXN intermediaries should not be null", loop2300.getClaimIdForTxnIntermediaries());
            assertNotNull("Medical record number should not be null", loop2300.getMedicalRecordNumber());
            assertNotNull("Demo project ID should not be null", loop2300.getDemoProjectId());
            assertNotNull("Care plan oversight should not be null", loop2300.getCarePlanOversight());
            
            // Verify K3 segments
            assertNotNull("File information should not be null", loop2300.getFileInformation());
            assertEquals("Should have 3 K3 segments", 3, loop2300.getFileInformation().size());
            
            // Verify NTE segment
            assertNotNull("Claim note should not be null", loop2300.getClaimNote());
            assertEquals("ADD", loop2300.getClaimNote().getNoteReferenceCode());
            
            // Verify CR1 segment
            assertNotNull("Ambulance transport info should not be null", loop2300.getAmbulanceTransportInfo());
            assertEquals("LB", loop2300.getAmbulanceTransportInfo().getUnitOrBasisForMeasurementCode());
            assertEquals("180", loop2300.getAmbulanceTransportInfo().getWeight());
            
            // Verify CR2 segment
            assertNotNull("Spinal manipulation info should not be null", loop2300.getSpinalManipulationInfo());
            assertEquals("12", loop2300.getSpinalManipulationInfo().getCount());
            
            // Verify CRC segments with discriminators
            assertNotNull("Ambulance certification should not be null", loop2300.getAmbulanceCertification());
            assertEquals("07", loop2300.getAmbulanceCertification().get(0).getCodeCategory());
            assertNotNull("Patient condition vision should not be null", loop2300.getPatientConditionVision());
            assertEquals("E1", loop2300.getPatientConditionVision().get(0).getCodeCategory());
            assertNotNull("Homebound indicator should not be null", loop2300.getHomeboundIndicator());
            assertEquals("75", loop2300.getHomeboundIndicator().getCodeCategory());
            assertNotNull("EPSDT referral should not be null", loop2300.getEpsdtReferral());
            assertEquals("ZZ", loop2300.getEpsdtReferral().getCodeCategory());
            
            // Verify HI segments
            assertNotNull("Health care diagnosis code should not be null", loop2300.getHealthCareDiagnosisCode());
            assertTrue("Diagnosis code should contain BK", loop2300.getHealthCareDiagnosisCode().getHealthCareCodeInformation().startsWith("BK:"));
            
            // Verify HCP segment
            assertNotNull("Claim pricing/repricing info should not be null", loop2300.getClaimPricingRepricingInfo());
            assertEquals("01", loop2300.getClaimPricingRepricingInfo().getPricingMethodology());
            assertEquals("2000.00", loop2300.getClaimPricingRepricingInfo().getMonetaryAmount());
            
            // Verify Transaction Set Trailer
            assertNotNull("Transaction set trailer should not be null", interchange.getTransactionSetTrailer());
            assertEquals("0001", interchange.getTransactionSetTrailer().getTransactionSetControlNumber());
            
            // Verify Functional Group Trailer
            assertNotNull("Functional group trailer should not be null", interchange.getFunctionalGroupTrailer());
            assertEquals("1", interchange.getFunctionalGroupTrailer().getGroupControlNumber());
            
            // Verify Interchange Control Trailer
            assertNotNull("Interchange control trailer should not be null", interchange.getInterchangeControlTrailer());
            assertEquals("000000001", interchange.getInterchangeControlTrailer().getInterchangeControlNumber());
            
            System.out.println("Successfully parsed comprehensive EDI 837 with all defined loops and segments");
            System.out.println("XML length: " + xml.length() + " characters");
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse comprehensive EDI 837: " + e.getMessage());
        }
    }
}