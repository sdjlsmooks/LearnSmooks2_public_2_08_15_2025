package org.example.XML;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Test for Loop 2330A-G classes
 */
public class X12_837_Loop2330Test {

    @Test
    public void testLoop2330AOtherSubscriberName() {
        // Test Loop 2330A - Other Subscriber Name
        X12_837_Loop2330_Classes.Loop2330AOtherSubscriberName loop2330A = 
            new X12_837_Loop2330_Classes.Loop2330AOtherSubscriberName();
        
        X12_837_Interchange.NM1Segment nm1 = new X12_837_Interchange.NM1Segment();
        nm1.setEntityIdentifierCode("IL");
        nm1.setEntityTypeQualifier("1");
        nm1.setNameLastOrOrganizationName("SMITH");
        nm1.setNameFirst("JOHN");
        
        loop2330A.setOtherSubscriberName(nm1);
        
        X12_837_Interchange.N3Segment n3 = new X12_837_Interchange.N3Segment();
        n3.setAddressInformation("123 MAIN STREET");
        loop2330A.setOtherSubscriberAddress(n3);
        
        X12_837_Interchange.N4Segment n4 = new X12_837_Interchange.N4Segment();
        n4.setCityName("ANYTOWN");
        n4.setStateOrProvinceCode("FL");
        n4.setPostalCode("33101");
        loop2330A.setOtherSubscriberCityStateZip(n4);
        
        assertNotNull(loop2330A.getOtherSubscriberName());
        assertEquals("IL", loop2330A.getOtherSubscriberName().getEntityIdentifierCode());
        assertEquals("SMITH", loop2330A.getOtherSubscriberName().getNameLastOrOrganizationName());
        assertNotNull(loop2330A.getOtherSubscriberAddress());
        assertEquals("123 MAIN STREET", loop2330A.getOtherSubscriberAddress().getAddressInformation());
    }
    
    @Test
    public void testLoop2330BOtherPayerName() {
        // Test Loop 2330B - Other Payer Name
        X12_837_Loop2330_Classes.Loop2330BOtherPayerName loop2330B = 
            new X12_837_Loop2330_Classes.Loop2330BOtherPayerName();
        
        X12_837_Interchange.NM1Segment nm1 = new X12_837_Interchange.NM1Segment();
        nm1.setEntityIdentifierCode("PR");
        nm1.setEntityTypeQualifier("2");
        nm1.setNameLastOrOrganizationName("MEDICARE");
        
        loop2330B.setOtherPayerName(nm1);
        
        X12_837_Interchange.DTPSegment dtp = new X12_837_Interchange.DTPSegment();
        dtp.setDateTimeQualifier("573");
        dtp.setDateTimePeriodFormatQualifier("D8");
        dtp.setDateTimePeriod("20250101");
        loop2330B.setClaimCheckRemittanceDate(dtp);
        
        List<X12_837_Interchange.REFSegment> refs = new ArrayList<>();
        X12_837_Interchange.REFSegment ref = new X12_837_Interchange.REFSegment();
        ref.setReferenceIdentificationQualifier("2U");
        ref.setReferenceIdentification("PAYER001");
        refs.add(ref);
        loop2330B.setOtherPayerSecondaryIdentifier(refs);
        
        assertNotNull(loop2330B.getOtherPayerName());
        assertEquals("PR", loop2330B.getOtherPayerName().getEntityIdentifierCode());
        assertEquals("MEDICARE", loop2330B.getOtherPayerName().getNameLastOrOrganizationName());
        assertNotNull(loop2330B.getClaimCheckRemittanceDate());
        assertEquals("573", loop2330B.getClaimCheckRemittanceDate().getDateTimeQualifier());
    }
    
    @Test
    public void testLoop2330COtherPayerReferringProvider() {
        // Test Loop 2330C - Other Payer Referring Provider
        X12_837_Loop2330_Classes.Loop2330COtherPayerReferringProvider loop2330C = 
            new X12_837_Loop2330_Classes.Loop2330COtherPayerReferringProvider();
        
        X12_837_Interchange.NM1Segment nm1 = new X12_837_Interchange.NM1Segment();
        nm1.setEntityIdentifierCode("DN");
        nm1.setEntityTypeQualifier("1");
        nm1.setNameLastOrOrganizationName("JONES");
        nm1.setNameFirst("DAVID");
        
        loop2330C.setOtherPayerReferringProvider(nm1);
        
        List<X12_837_Interchange.REFSegment> refs = new ArrayList<>();
        X12_837_Interchange.REFSegment ref = new X12_837_Interchange.REFSegment();
        ref.setReferenceIdentificationQualifier("0B");
        ref.setReferenceIdentification("123456789");
        refs.add(ref);
        loop2330C.setOtherPayerReferringProviderSecondaryIdentifier(refs);
        
        assertNotNull(loop2330C.getOtherPayerReferringProvider());
        assertEquals("DN", loop2330C.getOtherPayerReferringProvider().getEntityIdentifierCode());
        assertEquals("JONES", loop2330C.getOtherPayerReferringProvider().getNameLastOrOrganizationName());
        assertNotNull(loop2330C.getOtherPayerReferringProviderSecondaryIdentifier());
        assertEquals(1, loop2330C.getOtherPayerReferringProviderSecondaryIdentifier().size());
    }
    
    @Test
    public void testLoop2330DOtherPayerRenderingProvider() {
        // Test Loop 2330D - Other Payer Rendering Provider
        X12_837_Loop2330_Classes.Loop2330DOtherPayerRenderingProvider loop2330D = 
            new X12_837_Loop2330_Classes.Loop2330DOtherPayerRenderingProvider();
        
        X12_837_Interchange.NM1Segment nm1 = new X12_837_Interchange.NM1Segment();
        nm1.setEntityIdentifierCode("82");
        nm1.setEntityTypeQualifier("1");
        nm1.setNameLastOrOrganizationName("BROWN");
        nm1.setNameFirst("SUSAN");
        
        loop2330D.setOtherPayerRenderingProvider(nm1);
        
        assertNotNull(loop2330D.getOtherPayerRenderingProvider());
        assertEquals("82", loop2330D.getOtherPayerRenderingProvider().getEntityIdentifierCode());
        assertEquals("BROWN", loop2330D.getOtherPayerRenderingProvider().getNameLastOrOrganizationName());
    }
    
    @Test
    public void testLoop2330EOtherPayerServiceFacilityLocation() {
        // Test Loop 2330E - Other Payer Service Facility Location
        X12_837_Loop2330_Classes.Loop2330EOtherPayerServiceFacilityLocation loop2330E = 
            new X12_837_Loop2330_Classes.Loop2330EOtherPayerServiceFacilityLocation();
        
        X12_837_Interchange.NM1Segment nm1 = new X12_837_Interchange.NM1Segment();
        nm1.setEntityIdentifierCode("77");
        nm1.setEntityTypeQualifier("2");
        nm1.setNameLastOrOrganizationName("REGIONAL MEDICAL CENTER");
        
        loop2330E.setOtherPayerServiceFacilityLocation(nm1);
        
        assertNotNull(loop2330E.getOtherPayerServiceFacilityLocation());
        assertEquals("77", loop2330E.getOtherPayerServiceFacilityLocation().getEntityIdentifierCode());
        assertEquals("REGIONAL MEDICAL CENTER", loop2330E.getOtherPayerServiceFacilityLocation().getNameLastOrOrganizationName());
    }
    
    @Test
    public void testLoop2330FOtherPayerSupervisingProvider() {
        // Test Loop 2330F - Other Payer Supervising Provider
        X12_837_Loop2330_Classes.Loop2330FOtherPayerSupervisingProvider loop2330F = 
            new X12_837_Loop2330_Classes.Loop2330FOtherPayerSupervisingProvider();
        
        X12_837_Interchange.NM1Segment nm1 = new X12_837_Interchange.NM1Segment();
        nm1.setEntityIdentifierCode("DQ");
        nm1.setEntityTypeQualifier("1");
        nm1.setNameLastOrOrganizationName("WILSON");
        nm1.setNameFirst("ROBERT");
        
        loop2330F.setOtherPayerSupervisingProvider(nm1);
        
        assertNotNull(loop2330F.getOtherPayerSupervisingProvider());
        assertEquals("DQ", loop2330F.getOtherPayerSupervisingProvider().getEntityIdentifierCode());
        assertEquals("WILSON", loop2330F.getOtherPayerSupervisingProvider().getNameLastOrOrganizationName());
    }
    
    @Test
    public void testLoop2330GOtherPayerBillingProvider() {
        // Test Loop 2330G - Other Payer Billing Provider
        X12_837_Loop2330_Classes.Loop2330GOtherPayerBillingProvider loop2330G = 
            new X12_837_Loop2330_Classes.Loop2330GOtherPayerBillingProvider();
        
        X12_837_Interchange.NM1Segment nm1 = new X12_837_Interchange.NM1Segment();
        nm1.setEntityIdentifierCode("85");
        nm1.setEntityTypeQualifier("2");
        nm1.setNameLastOrOrganizationName("HEALTHCARE BILLING SERVICES");
        
        loop2330G.setOtherPayerBillingProvider(nm1);
        
        List<X12_837_Interchange.REFSegment> refs = new ArrayList<>();
        X12_837_Interchange.REFSegment ref = new X12_837_Interchange.REFSegment();
        ref.setReferenceIdentificationQualifier("EI");
        ref.setReferenceIdentification("987654321");
        refs.add(ref);
        loop2330G.setOtherPayerBillingProviderSecondaryIdentifier(refs);
        
        assertNotNull(loop2330G.getOtherPayerBillingProvider());
        assertEquals("85", loop2330G.getOtherPayerBillingProvider().getEntityIdentifierCode());
        assertEquals("HEALTHCARE BILLING SERVICES", loop2330G.getOtherPayerBillingProvider().getNameLastOrOrganizationName());
        assertNotNull(loop2330G.getOtherPayerBillingProviderSecondaryIdentifier());
        assertEquals(1, loop2330G.getOtherPayerBillingProviderSecondaryIdentifier().size());
    }
    
    @Test
    public void testLoop2320WithAllLoop2330() {
        // Test that Loop2320 can contain all Loop 2330 sub-loops
        X12_837_Loop2310_Classes.Loop2320OtherSubscriberInformation loop2320 = 
            new X12_837_Loop2310_Classes.Loop2320OtherSubscriberInformation();
        
        // Set basic subscriber info
        X12_837_Interchange.SBRSegment sbr = new X12_837_Interchange.SBRSegment();
        sbr.setPayerResponsibilitySequenceNumberCode("P");
        sbr.setIndividualRelationshipCode("01");
        loop2320.setSubscriberInformation(sbr);
        
        // Add Loop 2330A - Other Subscriber Name
        X12_837_Loop2330_Classes.Loop2330AOtherSubscriberName loop2330A = 
            new X12_837_Loop2330_Classes.Loop2330AOtherSubscriberName();
        X12_837_Interchange.NM1Segment nm1A = new X12_837_Interchange.NM1Segment();
        nm1A.setEntityIdentifierCode("IL");
        loop2330A.setOtherSubscriberName(nm1A);
        loop2320.setLoop2330AOtherSubscriberName(loop2330A);
        
        // Add Loop 2330B - Other Payer Name
        X12_837_Loop2330_Classes.Loop2330BOtherPayerName loop2330B = 
            new X12_837_Loop2330_Classes.Loop2330BOtherPayerName();
        X12_837_Interchange.NM1Segment nm1B = new X12_837_Interchange.NM1Segment();
        nm1B.setEntityIdentifierCode("PR");
        loop2330B.setOtherPayerName(nm1B);
        loop2320.setLoop2330BOtherPayerName(loop2330B);
        
        // Add Loop 2330C - Other Payer Referring Provider
        List<X12_837_Loop2330_Classes.Loop2330COtherPayerReferringProvider> loop2330CList = new ArrayList<>();
        X12_837_Loop2330_Classes.Loop2330COtherPayerReferringProvider loop2330C = 
            new X12_837_Loop2330_Classes.Loop2330COtherPayerReferringProvider();
        X12_837_Interchange.NM1Segment nm1C = new X12_837_Interchange.NM1Segment();
        nm1C.setEntityIdentifierCode("DN");
        loop2330C.setOtherPayerReferringProvider(nm1C);
        loop2330CList.add(loop2330C);
        loop2320.setLoop2330COtherPayerReferringProvider(loop2330CList);
        
        // Verify
        assertNotNull(loop2320.getSubscriberInformation());
        assertEquals("P", loop2320.getSubscriberInformation().getPayerResponsibilitySequenceNumberCode());
        
        assertNotNull(loop2320.getLoop2330AOtherSubscriberName());
        assertEquals("IL", loop2320.getLoop2330AOtherSubscriberName().getOtherSubscriberName().getEntityIdentifierCode());
        
        assertNotNull(loop2320.getLoop2330BOtherPayerName());
        assertEquals("PR", loop2320.getLoop2330BOtherPayerName().getOtherPayerName().getEntityIdentifierCode());
        
        assertNotNull(loop2320.getLoop2330COtherPayerReferringProvider());
        assertEquals(1, loop2320.getLoop2330COtherPayerReferringProvider().size());
        assertEquals("DN", loop2320.getLoop2330COtherPayerReferringProvider().get(0).getOtherPayerReferringProvider().getEntityIdentifierCode());
    }
}