package org.example.XML;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test class for Loop 2410 and Loop 2420A-H
 */
public class X12_837_Loop2410_2420Test {

    /**
     * Sample X12 837 EDI message with Loop 2410 and 2420 segments
     */
    private static final String EDI_WITH_LOOP_2410_2420 = 
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
        // Loop 2400 - Service Line 1 with Drug Information
        "LX*1~" +
        "SV1*HC:J7040*250.00*UN*1***1~" +  // Injection service
        "DTP*472*D8*20210901~" +
        // Loop 2410 - Drug Identification
        "LIN**N4*12345678901~" +  // NDC drug code
        "CTP****10*UN~" +  // Drug quantity
        "REF*XZ*RX12345~" +  // Prescription number
        // Loop 2420A - Rendering Provider
        "NM1*82*1*SMITH*JANE*A***XX*9876543210~" +
        "PRV*PE*PXC*207Q00000X~" +  // Family Medicine specialty
        "REF*0B*STATE12345~" +  // State license number
        "REF*1G*UPIN98765~" +  // UPIN
        // Loop 2400 - Service Line 2 with Service Facility
        "LX*2~" +
        "SV1*HC:99214*350.00*UN*1***2~" +
        "DTP*472*D8*20210901~" +
        // Loop 2420C - Service Facility Location
        "NM1*77*2*GENERAL HOSPITAL*****XX*1122334455~" +
        "N3*789 HOSPITAL WAY~" +
        "N4*HEALTHCARE CITY*CA*90220~" +
        "REF*G2*FACILITY789~" +
        // Loop 2400 - Service Line 3 with Multiple Providers
        "LX*3~" +
        "SV1*HC:99215*400.00*UN*1***3~" +
        "DTP*472*D8*20210901~" +
        // Loop 2420D - Supervising Provider
        "NM1*DQ*1*JOHNSON*ROBERT*M***XX*1234567890~" +
        "REF*G2*SUPER123~" +
        // Loop 2420E - Ordering Provider
        "NM1*DK*1*WILLIAMS*DAVID*L***XX*2345678901~" +
        "N3*321 PROVIDER LANE~" +
        "N4*MEDICAL CITY*TX*75001~" +
        "REF*0B*TXLIC456~" +
        "PER*IC*CONTACT PERSON*TE*5551234567~" +
        // Loop 2420F - Referring Provider
        "NM1*DN*1*BROWN*MICHAEL*J***XX*3456789012~" +
        "REF*G2*REFER789~" +
        // Loop 2400 - Service Line 4 with Ambulance
        "LX*4~" +
        "SV1*HC:A0425*500.00*UN*1***4~" +  // Ambulance service
        "DTP*472*D8*20210901~" +
        // Loop 2420G - Ambulance Pick-up Location
        "NM1*PW*2*PICKUP LOCATION~" +
        "N3*100 ACCIDENT SCENE~" +
        "N4*EMERGENCY CITY*CA*90230~" +
        // Loop 2420H - Ambulance Drop-off Location
        "NM1*45*2*EMERGENCY ROOM~" +
        "N3*500 HOSPITAL ENTRANCE~" +
        "N4*HOSPITAL CITY*CA*90240~" +
        "SE*60*0001~" +
        "GE*1*1~" +
        "IEA*1*000000001~";

    @Test
    public void testLoop2410DrugIdentification() {
        try {
            // Parse EDI to XML
            String xml = X12_837_Parser.parseEDI(EDI_WITH_LOOP_2410_2420);
            assertNotNull("XML result should not be null", xml);
            
            // Check for Loop 2410 elements
            assertTrue("XML should contain Loop 2410", xml.contains("Loop_2410_DrugIdentification"));
            assertTrue("XML should contain drug identification", xml.contains("drug-identification"));
            assertTrue("XML should contain drug quantity", xml.contains("drug-quantity"));
            assertTrue("XML should contain prescription number", xml.contains("prescription-number"));
            
            // Parse XML to Object
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Navigate to Loop 2400
            assertNotNull("Loop 2300 should not be null", interchange.getLoop2300ClaimInformation());
            X12_837_Interchange.Loop2300ClaimInformation claim = interchange.getLoop2300ClaimInformation().get(0);
            assertNotNull("Loop 2400 should not be null", claim.getLoop2400ServiceLineInformation());
            
            // Get first service line with drug information
            X12_837_Loop2400_Classes.Loop2400ServiceLineInformation line1 = 
                claim.getLoop2400ServiceLineInformation().get(0);
            
            // Verify Loop 2410 - Drug Identification
            assertNotNull("Loop 2410 should not be null", line1.getLoop2410DrugIdentification());
            X12_837_Loop2410_2420_Classes.Loop2410DrugIdentification drug = line1.getLoop2410DrugIdentification();
            
            assertNotNull("Drug identification should not be null", drug.getDrugIdentification());
            assertEquals("N4", drug.getDrugIdentification().getProductServiceIdQualifier());
            assertEquals("12345678901", drug.getDrugIdentification().getProductServiceId());
            
            assertNotNull("Drug quantity should not be null", drug.getDrugQuantity());
            assertEquals("10", drug.getDrugQuantity().getQuantity());
            assertEquals("UN", drug.getDrugQuantity().getCompositeUnitOfMeasure());
            
            assertNotNull("Prescription number should not be null", drug.getPrescriptionNumber());
            assertEquals("XZ", drug.getPrescriptionNumber().getReferenceIdentificationQualifier());
            assertEquals("RX12345", drug.getPrescriptionNumber().getReferenceIdentification());
            
            System.out.println("Successfully parsed Loop 2410 Drug Identification");
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse EDI with Loop 2410: " + e.getMessage());
        }
    }
    
    @Test
    public void testLoop2420ServiceLineProviders() {
        try {
            // Parse EDI to XML
            String xml = X12_837_Parser.parseEDI(EDI_WITH_LOOP_2410_2420);
            assertNotNull("XML result should not be null", xml);
            
            // Check for Loop 2420 elements
            assertTrue("XML should contain Loop 2420A", xml.contains("Loop_2420A_RenderingProviderName"));
            assertTrue("XML should contain Loop 2420C", xml.contains("Loop_2420C_ServiceFacilityLocationName"));
            assertTrue("XML should contain Loop 2420D", xml.contains("Loop_2420D_SupervisingProviderName"));
            assertTrue("XML should contain Loop 2420E", xml.contains("Loop_2420E_OrderingProviderName"));
            assertTrue("XML should contain Loop 2420F", xml.contains("Loop_2420F_ReferringProviderName"));
            assertTrue("XML should contain Loop 2420G", xml.contains("Loop_2420G_AmbulancePickupLocation"));
            assertTrue("XML should contain Loop 2420H", xml.contains("Loop_2420H_AmbulanceDropoffLocation"));
            
            // Parse XML to Object
            X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
            assertNotNull("Interchange object should not be null", interchange);
            
            // Navigate to Loop 2400
            X12_837_Interchange.Loop2300ClaimInformation claim = interchange.getLoop2300ClaimInformation().get(0);
            
            // Test Loop 2420A - Rendering Provider (Line 1)
            X12_837_Loop2400_Classes.Loop2400ServiceLineInformation line1 = 
                claim.getLoop2400ServiceLineInformation().get(0);
            assertNotNull("Loop 2420A should not be null", line1.getLoop2420ARenderingProviderName());
            X12_837_Loop2410_2420_Classes.Loop2420ARenderingProviderName rendering = 
                line1.getLoop2420ARenderingProviderName();
            assertEquals("82", rendering.getRenderingProviderName().getEntityIdentifierCode());
            assertEquals("SMITH", rendering.getRenderingProviderName().getNameLastOrOrganizationName());
            assertEquals("JANE", rendering.getRenderingProviderName().getNameFirst());
            assertNotNull("Rendering provider specialty should not be null", rendering.getRenderingProviderSpecialty());
            assertEquals("PE", rendering.getRenderingProviderSpecialty().getProviderCode());
            assertEquals("207Q00000X", rendering.getRenderingProviderSpecialty().getReferenceIdentification());
            
            // Test Loop 2420C - Service Facility Location (Line 2)
            X12_837_Loop2400_Classes.Loop2400ServiceLineInformation line2 = 
                claim.getLoop2400ServiceLineInformation().get(1);
            assertNotNull("Loop 2420C should not be null", line2.getLoop2420CServiceFacilityLocationName());
            X12_837_Loop2410_2420_Classes.Loop2420CServiceFacilityLocationName facility = 
                line2.getLoop2420CServiceFacilityLocationName();
            assertEquals("77", facility.getServiceFacilityLocationName().getEntityIdentifierCode());
            assertEquals("GENERAL HOSPITAL", facility.getServiceFacilityLocationName().getNameLastOrOrganizationName());
            assertEquals("789 HOSPITAL WAY", facility.getServiceFacilityLocationAddress().getAddressInformation());
            assertEquals("HEALTHCARE CITY", facility.getServiceFacilityLocationCityStateZip().getCityName());
            
            // Test Loop 2420D, 2420E, 2420F (Line 3)
            X12_837_Loop2400_Classes.Loop2400ServiceLineInformation line3 = 
                claim.getLoop2400ServiceLineInformation().get(2);
            
            // Loop 2420D - Supervising Provider
            assertNotNull("Loop 2420D should not be null", line3.getLoop2420DSupervisingProviderName());
            assertEquals("DQ", line3.getLoop2420DSupervisingProviderName()
                .getSupervisingProviderName().getEntityIdentifierCode());
            assertEquals("JOHNSON", line3.getLoop2420DSupervisingProviderName()
                .getSupervisingProviderName().getNameLastOrOrganizationName());
            
            // Loop 2420E - Ordering Provider
            assertNotNull("Loop 2420E should not be null", line3.getLoop2420EOrderingProviderName());
            assertEquals("DK", line3.getLoop2420EOrderingProviderName()
                .getOrderingProviderName().getEntityIdentifierCode());
            assertEquals("WILLIAMS", line3.getLoop2420EOrderingProviderName()
                .getOrderingProviderName().getNameLastOrOrganizationName());
            assertNotNull("Ordering provider contact should not be null", 
                line3.getLoop2420EOrderingProviderName().getOrderingProviderContactInfo());
            
            // Loop 2420F - Referring Provider
            assertNotNull("Loop 2420F should not be null", line3.getLoop2420FReferringProviderName());
            assertTrue("Should have at least one referring provider", 
                line3.getLoop2420FReferringProviderName().size() > 0);
            assertEquals("DN", line3.getLoop2420FReferringProviderName().get(0)
                .getReferringProviderName().getEntityIdentifierCode());
            
            // Test Loop 2420G and 2420H - Ambulance Locations (Line 4)
            X12_837_Loop2400_Classes.Loop2400ServiceLineInformation line4 = 
                claim.getLoop2400ServiceLineInformation().get(3);
            
            // Loop 2420G - Pick-up Location
            assertNotNull("Loop 2420G should not be null", line4.getLoop2420GAmbulancePickupLocation());
            assertEquals("PW", line4.getLoop2420GAmbulancePickupLocation()
                .getAmbulancePickupLocation().getEntityIdentifierCode());
            assertEquals("PICKUP LOCATION", line4.getLoop2420GAmbulancePickupLocation()
                .getAmbulancePickupLocation().getNameLastOrOrganizationName());
            assertEquals("100 ACCIDENT SCENE", line4.getLoop2420GAmbulancePickupLocation()
                .getAmbulancePickupAddress().getAddressInformation());
            
            // Loop 2420H - Drop-off Location
            assertNotNull("Loop 2420H should not be null", line4.getLoop2420HAmbulanceDropoffLocation());
            assertEquals("45", line4.getLoop2420HAmbulanceDropoffLocation()
                .getAmbulanceDropoffLocation().getEntityIdentifierCode());
            assertEquals("EMERGENCY ROOM", line4.getLoop2420HAmbulanceDropoffLocation()
                .getAmbulanceDropoffLocation().getNameLastOrOrganizationName());
            assertEquals("500 HOSPITAL ENTRANCE", line4.getLoop2420HAmbulanceDropoffLocation()
                .getAmbulanceDropoffAddress().getAddressInformation());
            
            System.out.println("Successfully parsed Loop 2420A-H Service Line Providers");
            
        } catch (IOException | SAXException e) {
            fail("Failed to parse EDI with Loop 2420: " + e.getMessage());
        }
    }
}