package org.example.XML;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Simple test to verify Loop 2310A-F classes compile and can be instantiated
 */
public class X12_837_Loop2310SimpleTest {

    @Test
    public void testLoop2310ACreation() {
        // Test Loop 2310A - Referring Provider
        X12_837_Loop2310_Classes.Loop2310AReferringProviderName loop2310A = 
            new X12_837_Loop2310_Classes.Loop2310AReferringProviderName();
        
        X12_837_Interchange.NM1Segment nm1 = new X12_837_Interchange.NM1Segment();
        nm1.setEntityIdentifierCode("DN");
        nm1.setEntityTypeQualifier("1");
        nm1.setNameLastOrOrganizationName("WILLIAMS");
        nm1.setNameFirst("MICHAEL");
        
        loop2310A.setReferringProviderName(nm1);
        
        assertNotNull(loop2310A.getReferringProviderName());
        assertEquals("DN", loop2310A.getReferringProviderName().getEntityIdentifierCode());
        assertEquals("WILLIAMS", loop2310A.getReferringProviderName().getNameLastOrOrganizationName());
    }
    
    @Test
    public void testLoop2310BCreation() {
        // Test Loop 2310B - Rendering Provider
        X12_837_Loop2310_Classes.Loop2310BRenderingProviderName loop2310B = 
            new X12_837_Loop2310_Classes.Loop2310BRenderingProviderName();
        
        X12_837_Interchange.NM1Segment nm1 = new X12_837_Interchange.NM1Segment();
        nm1.setEntityIdentifierCode("82");
        nm1.setEntityTypeQualifier("1");
        nm1.setNameLastOrOrganizationName("ANDERSON");
        nm1.setNameFirst("JENNIFER");
        
        loop2310B.setRenderingProviderName(nm1);
        
        X12_837_Interchange.PRVSegment prv = new X12_837_Interchange.PRVSegment();
        prv.setProviderCode("PE");
        prv.setReferenceIdentificationQualifier("PXC");
        prv.setReferenceIdentification("207Q00000X");
        
        loop2310B.setRenderingProviderSpecialtyInfo(prv);
        
        assertNotNull(loop2310B.getRenderingProviderName());
        assertNotNull(loop2310B.getRenderingProviderSpecialtyInfo());
        assertEquals("82", loop2310B.getRenderingProviderName().getEntityIdentifierCode());
        assertEquals("PE", loop2310B.getRenderingProviderSpecialtyInfo().getProviderCode());
    }
    
    @Test
    public void testLoop2310CCreation() {
        // Test Loop 2310C - Service Facility Location
        X12_837_Loop2310_Classes.Loop2310CServiceFacilityLocationName loop2310C = 
            new X12_837_Loop2310_Classes.Loop2310CServiceFacilityLocationName();
        
        X12_837_Interchange.NM1Segment nm1 = new X12_837_Interchange.NM1Segment();
        nm1.setEntityIdentifierCode("77");
        nm1.setEntityTypeQualifier("2");
        nm1.setNameLastOrOrganizationName("CITY GENERAL HOSPITAL");
        
        loop2310C.setServiceFacilityLocationName(nm1);
        
        X12_837_Interchange.N3Segment n3 = new X12_837_Interchange.N3Segment();
        n3.setAddressInformation("1500 HOSPITAL BOULEVARD");
        
        loop2310C.setServiceFacilityLocationAddress(n3);
        
        X12_837_Interchange.N4Segment n4 = new X12_837_Interchange.N4Segment();
        n4.setCityName("MEDICAL CENTER");
        n4.setStateOrProvinceCode("FL");
        n4.setPostalCode("33103");
        
        loop2310C.setServiceFacilityLocationCityStateZip(n4);
        
        assertNotNull(loop2310C.getServiceFacilityLocationName());
        assertNotNull(loop2310C.getServiceFacilityLocationAddress());
        assertNotNull(loop2310C.getServiceFacilityLocationCityStateZip());
        assertEquals("77", loop2310C.getServiceFacilityLocationName().getEntityIdentifierCode());
        assertEquals("1500 HOSPITAL BOULEVARD", loop2310C.getServiceFacilityLocationAddress().getAddressInformation());
        assertEquals("MEDICAL CENTER", loop2310C.getServiceFacilityLocationCityStateZip().getCityName());
    }
    
    @Test
    public void testLoop2310DCreation() {
        // Test Loop 2310D - Supervising Provider
        X12_837_Loop2310_Classes.Loop2310DSupervisingProviderName loop2310D = 
            new X12_837_Loop2310_Classes.Loop2310DSupervisingProviderName();
        
        X12_837_Interchange.NM1Segment nm1 = new X12_837_Interchange.NM1Segment();
        nm1.setEntityIdentifierCode("DK");
        nm1.setEntityTypeQualifier("1");
        nm1.setNameLastOrOrganizationName("SUPERVISING");
        nm1.setNameFirst("DAVID");
        
        loop2310D.setSupervisingProviderName(nm1);
        
        assertNotNull(loop2310D.getSupervisingProviderName());
        assertEquals("DK", loop2310D.getSupervisingProviderName().getEntityIdentifierCode());
        assertEquals("SUPERVISING", loop2310D.getSupervisingProviderName().getNameLastOrOrganizationName());
    }
    
    @Test
    public void testLoop2310ECreation() {
        // Test Loop 2310E - Ambulance Pickup Location
        X12_837_Loop2310_Classes.Loop2310EAmbulancePickupLocation loop2310E = 
            new X12_837_Loop2310_Classes.Loop2310EAmbulancePickupLocation();
        
        X12_837_Interchange.NM1Segment nm1 = new X12_837_Interchange.NM1Segment();
        nm1.setEntityIdentifierCode("PW");
        nm1.setEntityTypeQualifier("2");
        nm1.setNameLastOrOrganizationName("METRO AMBULANCE SERVICE");
        
        loop2310E.setAmbulancePickupLocationName(nm1);
        
        X12_837_Interchange.N3Segment n3 = new X12_837_Interchange.N3Segment();
        n3.setAddressInformation("100 EMERGENCY WAY");
        
        loop2310E.setAmbulancePickupAddress(n3);
        
        X12_837_Interchange.N4Segment n4 = new X12_837_Interchange.N4Segment();
        n4.setCityName("PICKUP CITY");
        n4.setStateOrProvinceCode("FL");
        n4.setPostalCode("33104");
        
        loop2310E.setAmbulancePickupCityStateZip(n4);
        
        assertNotNull(loop2310E.getAmbulancePickupLocationName());
        assertNotNull(loop2310E.getAmbulancePickupAddress());
        assertNotNull(loop2310E.getAmbulancePickupCityStateZip());
        assertEquals("PW", loop2310E.getAmbulancePickupLocationName().getEntityIdentifierCode());
        assertEquals("100 EMERGENCY WAY", loop2310E.getAmbulancePickupAddress().getAddressInformation());
        assertEquals("PICKUP CITY", loop2310E.getAmbulancePickupCityStateZip().getCityName());
    }
    
    @Test
    public void testLoop2310FCreation() {
        // Test Loop 2310F - Ambulance Dropoff Location
        X12_837_Loop2310_Classes.Loop2310FAmbulanceDropoffLocation loop2310F = 
            new X12_837_Loop2310_Classes.Loop2310FAmbulanceDropoffLocation();
        
        X12_837_Interchange.NM1Segment nm1 = new X12_837_Interchange.NM1Segment();
        nm1.setEntityIdentifierCode("45");
        nm1.setEntityTypeQualifier("2");
        nm1.setNameLastOrOrganizationName("CITY GENERAL HOSPITAL EMERGENCY");
        
        loop2310F.setAmbulanceDropoffLocationName(nm1);
        
        X12_837_Interchange.N3Segment n3 = new X12_837_Interchange.N3Segment();
        n3.setAddressInformation("1500 HOSPITAL BOULEVARD");
        
        loop2310F.setAmbulanceDropoffAddress(n3);
        
        X12_837_Interchange.N4Segment n4 = new X12_837_Interchange.N4Segment();
        n4.setCityName("MEDICAL CENTER");
        n4.setStateOrProvinceCode("FL");
        n4.setPostalCode("33103");
        
        loop2310F.setAmbulanceDropoffCityStateZip(n4);
        
        assertNotNull(loop2310F.getAmbulanceDropoffLocationName());
        assertNotNull(loop2310F.getAmbulanceDropoffAddress());
        assertNotNull(loop2310F.getAmbulanceDropoffCityStateZip());
        assertEquals("45", loop2310F.getAmbulanceDropoffLocationName().getEntityIdentifierCode());
        assertEquals("1500 HOSPITAL BOULEVARD", loop2310F.getAmbulanceDropoffAddress().getAddressInformation());
        assertEquals("MEDICAL CENTER", loop2310F.getAmbulanceDropoffCityStateZip().getCityName());
    }
    
    @Test
    public void testLoop2300WithAllLoops() {
        // Test that Loop2300ClaimInformation can contain all 2310 loops
        X12_837_Interchange.Loop2300ClaimInformation claim = 
            new X12_837_Interchange.Loop2300ClaimInformation();
        
        // Set basic claim info
        X12_837_Interchange.CLMSegment clm = new X12_837_Interchange.CLMSegment();
        clm.setClaimSubmittersIdentifier("CLAIM001");
        clm.setMonetaryAmount("1500.00");
        claim.setClaimInformation(clm);
        
        // Add Loop 2310B - Rendering Provider
        X12_837_Loop2310_Classes.Loop2310BRenderingProviderName loop2310B = 
            new X12_837_Loop2310_Classes.Loop2310BRenderingProviderName();
        X12_837_Interchange.NM1Segment nm1B = new X12_837_Interchange.NM1Segment();
        nm1B.setEntityIdentifierCode("82");
        loop2310B.setRenderingProviderName(nm1B);
        claim.setLoop2310BRenderingProviderName(loop2310B);
        
        // Add Loop 2310C - Service Facility
        X12_837_Loop2310_Classes.Loop2310CServiceFacilityLocationName loop2310C = 
            new X12_837_Loop2310_Classes.Loop2310CServiceFacilityLocationName();
        X12_837_Interchange.NM1Segment nm1C = new X12_837_Interchange.NM1Segment();
        nm1C.setEntityIdentifierCode("77");
        loop2310C.setServiceFacilityLocationName(nm1C);
        claim.setLoop2310CServiceFacilityLocationName(loop2310C);
        
        // Verify
        assertNotNull(claim.getClaimInformation());
        assertNotNull(claim.getLoop2310BRenderingProviderName());
        assertNotNull(claim.getLoop2310CServiceFacilityLocationName());
        assertEquals("CLAIM001", claim.getClaimInformation().getClaimSubmittersIdentifier());
        assertEquals("82", claim.getLoop2310BRenderingProviderName().getRenderingProviderName().getEntityIdentifierCode());
        assertEquals("77", claim.getLoop2310CServiceFacilityLocationName().getServiceFacilityLocationName().getEntityIdentifierCode());
    }

    @Test
    public void testLoop2320Creation() {
        // Test Loop 2320 - Other Subscriber Information
        X12_837_Loop2310_Classes.Loop2320OtherSubscriberInformation loop2320 = 
            new X12_837_Loop2310_Classes.Loop2320OtherSubscriberInformation();
        
        // Create and set SBR segment
        X12_837_Interchange.SBRSegment sbr = new X12_837_Interchange.SBRSegment();
        sbr.setPayerResponsibilitySequenceNumberCode("P");
        sbr.setIndividualRelationshipCode("18");
        sbr.setInsuredGroupOrPolicyNumber("GROUP123");
        sbr.setOtherInsuredGroupName("BLUE CROSS BLUE SHIELD");
        sbr.setInsuranceTypeCode("SP");
        
        loop2320.setSubscriberInformation(sbr);
        
        // Create and set CAS segment (claim adjustment)
        List<X12_837_Interchange.CASSegment> casSegments = new java.util.ArrayList<>();
        X12_837_Interchange.CASSegment cas = new X12_837_Interchange.CASSegment();
        cas.setClaimAdjustmentGroupCode("PR");
        cas.setClaimAdjustmentReasonCode("1");
        cas.setMonetaryAmount("25.00");
        casSegments.add(cas);
        loop2320.setClaimAdjustment(casSegments);
        
        // Create and set AMT segment (other payer paid amount)
        List<X12_837_Interchange.AMTSegment> amtSegments = new java.util.ArrayList<>();
        X12_837_Interchange.AMTSegment amt = new X12_837_Interchange.AMTSegment();
        amt.setAmountQualifierCode("D");
        amt.setMonetaryAmount("475.00");
        amtSegments.add(amt);
        loop2320.setOtherPayerPaidAmount(amtSegments);
        
        // Create and set OI segment (other insurance coverage info)
        X12_837_Interchange.OISegment oi = new X12_837_Interchange.OISegment();
        oi.setClaimFilingIndicatorCode("11");
        oi.setYesNoConditionResponseCode("Y");
        oi.setPatientSignatureSourceCode("P");
        oi.setProviderAgreementCode("Y");
        oi.setReleaseOfInformationCode("Y");
        loop2320.setOtherInsuranceCoverageInformation(oi);
        
        // Create and set MOA segment (medicare outpatient adjudication)
        X12_837_Interchange.MOASegment moa = new X12_837_Interchange.MOASegment();
        moa.setReimbursementRate("80");
        moa.setHcpcsPayableAmount("400.00");
        loop2320.setMedicareOutpatientAdjudication(moa);
        
        // Verify
        assertNotNull(loop2320.getSubscriberInformation());
        assertEquals("P", loop2320.getSubscriberInformation().getPayerResponsibilitySequenceNumberCode());
        assertEquals("18", loop2320.getSubscriberInformation().getIndividualRelationshipCode());
        
        assertNotNull(loop2320.getClaimAdjustment());
        assertEquals(1, loop2320.getClaimAdjustment().size());
        assertEquals("PR", loop2320.getClaimAdjustment().get(0).getClaimAdjustmentGroupCode());
        
        assertNotNull(loop2320.getOtherPayerPaidAmount());
        assertEquals(1, loop2320.getOtherPayerPaidAmount().size());
        assertEquals("D", loop2320.getOtherPayerPaidAmount().get(0).getAmountQualifierCode());
        
        assertNotNull(loop2320.getOtherInsuranceCoverageInformation());
        assertEquals("Y", loop2320.getOtherInsuranceCoverageInformation().getYesNoConditionResponseCode());
        
        assertNotNull(loop2320.getMedicareOutpatientAdjudication());
        assertEquals("80", loop2320.getMedicareOutpatientAdjudication().getReimbursementRate());
    }

    @Test
    public void testLoop2300WithLoop2320() {
        // Test that Loop2300ClaimInformation can contain Loop 2320
        X12_837_Interchange.Loop2300ClaimInformation claim = 
            new X12_837_Interchange.Loop2300ClaimInformation();
        
        // Set basic claim info
        X12_837_Interchange.CLMSegment clm = new X12_837_Interchange.CLMSegment();
        clm.setClaimSubmittersIdentifier("CLAIM002");
        clm.setMonetaryAmount("500.00");
        claim.setClaimInformation(clm);
        
        // Add Loop 2320 - Other Subscriber Information
        List<X12_837_Loop2310_Classes.Loop2320OtherSubscriberInformation> loop2320List = new java.util.ArrayList<>();
        
        X12_837_Loop2310_Classes.Loop2320OtherSubscriberInformation loop2320 = 
            new X12_837_Loop2310_Classes.Loop2320OtherSubscriberInformation();
        
        X12_837_Interchange.SBRSegment sbr = new X12_837_Interchange.SBRSegment();
        sbr.setPayerResponsibilitySequenceNumberCode("S");
        sbr.setIndividualRelationshipCode("01");
        loop2320.setSubscriberInformation(sbr);
        
        loop2320List.add(loop2320);
        claim.setLoop2320OtherSubscriberInformation(loop2320List);
        
        // Verify
        assertNotNull(claim.getClaimInformation());
        assertNotNull(claim.getLoop2320OtherSubscriberInformation());
        assertEquals(1, claim.getLoop2320OtherSubscriberInformation().size());
        assertEquals("S", claim.getLoop2320OtherSubscriberInformation().get(0).getSubscriberInformation().getPayerResponsibilitySequenceNumberCode());
    }
}