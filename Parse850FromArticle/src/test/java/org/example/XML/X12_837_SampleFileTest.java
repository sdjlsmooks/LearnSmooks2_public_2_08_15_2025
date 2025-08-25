package org.example.XML;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Test class specifically for parsing the sample_837_professional.edi file
 */
public class X12_837_SampleFileTest {

    @Test
    public void testParseSample837ProfessionalFile() throws IOException, SAXException {
        // Read the sample file
        String ediContent = new String(Files.readAllBytes(Paths.get("sample_837_professional.edi")));
        
        System.out.println("Parsing sample_837_professional.edi file...");
        
        // Parse EDI to XML
        String xml = X12_837_Parser.parseEDI(ediContent);
        assertNotNull("XML result should not be null", xml);
        
        // Basic structure checks
        assertTrue("Should contain interchange header", xml.contains("<interchange-header>"));
        assertTrue("Should contain group header", xml.contains("<group-header>"));
        assertTrue("Should contain transaction set header", xml.contains("<transaction-set-header>"));
        
        // Check for hierarchical levels
        assertTrue("Should contain billing provider HL", xml.contains("<billing-provider-hierarchical-level>"));
        assertTrue("Should contain subscriber HL", xml.contains("<subscriber-hierarchical-level>"));
        
        // Check for claim information
        assertTrue("Should contain claim information", xml.contains("<claim-information>"));
        assertTrue("Should contain patient control number PATIENT001", xml.contains("PATIENT001"));
        assertTrue("Should contain claim amount 1250.00", xml.contains("1250.00"));
        
        // Check for dates (fixed dates)
        assertTrue("Should contain admission date", xml.contains("<date-time-qualifier>435</date-time-qualifier>"));
        assertTrue("Should contain discharge date", xml.contains("<date-time-qualifier>096</date-time-qualifier>"));
        
        // Check for diagnosis codes
        assertTrue("Should contain diagnosis codes", xml.contains("<health-care-diagnosis-code>"));
        assertTrue("Should contain diagnosis M7989", xml.contains("M7989"));
        assertTrue("Should contain diagnosis E119", xml.contains("E119"));
        
        // Check for providers (Loop 2310)
        assertTrue("Should contain attending provider", xml.contains("JONES"));
        assertTrue("Should contain rendering provider", xml.contains("SMITH"));
        assertTrue("Should contain service facility", xml.contains("GENERAL HOSPITAL"));
        
        // Check for service lines (Loop 2400)
        assertTrue("Should contain service lines", xml.contains("<service-line-number>"));
        assertTrue("Should contain 5 service lines", xml.contains("<assigned-number>5</assigned-number>"));
        
        // Parse XML to Object to verify structure
        X12_837_Interchange interchange = X12_837_Parser.parseXML(xml);
        assertNotNull("Interchange object should not be null", interchange);
        
        // Verify interchange header
        assertEquals("SUBMITTERID", interchange.getInterchangeHeader().getSenderId().trim());
        assertEquals("RECEIVERID", interchange.getInterchangeHeader().getReceiverId().trim());
        
        // Verify hierarchical structure
        assertNotNull("Should have billing provider", interchange.getLoop2000ABillingProviderDetail());
        assertNotNull("Should have subscriber", interchange.getLoop2000BSubscriberDetail());
        // Note: Loop 2000C is part of the subscriber/dependent hierarchy
        
        // Verify claim
        assertNotNull("Should have claims", interchange.getLoop2300ClaimInformation());
        assertFalse("Should have at least one claim", interchange.getLoop2300ClaimInformation().isEmpty());
        
        X12_837_Interchange.Loop2300ClaimInformation claim = 
            interchange.getLoop2300ClaimInformation().get(0);
        assertEquals("PATIENT001", claim.getClaimInformation().getClaimSubmittersIdentifier());
        assertEquals("1250.00", claim.getClaimInformation().getMonetaryAmount());
        
        // Verify service lines
        assertNotNull("Should have service lines", claim.getLoop2400ServiceLineInformation());
        assertEquals("Should have 5 service lines", 5, claim.getLoop2400ServiceLineInformation().size());
        
        // Verify first service line
        X12_837_Loop2400_Classes.Loop2400ServiceLineInformation firstLine = 
            claim.getLoop2400ServiceLineInformation().get(0);
        assertEquals("1", firstLine.getServiceLineNumber().getAssignedNumber());
        assertNotNull("Should have professional service", firstLine.getProfessionalService());
        assertEquals("HC:99213:25", firstLine.getProfessionalService().getCompositeMedicalProcedureIdentifier());
        assertEquals("150.00", firstLine.getProfessionalService().getMonetaryAmount());
        
        // Verify last service line (drug with J-code)
        X12_837_Loop2400_Classes.Loop2400ServiceLineInformation lastLine = 
            claim.getLoop2400ServiceLineInformation().get(4);
        assertEquals("5", lastLine.getServiceLineNumber().getAssignedNumber());
        assertEquals("HC:J3420:JW", lastLine.getProfessionalService().getCompositeMedicalProcedureIdentifier());
        assertEquals("775.00", lastLine.getProfessionalService().getMonetaryAmount());
        assertEquals("15", lastLine.getProfessionalService().getQuantity());
        
        System.out.println("Successfully parsed sample_837_professional.edi file!");
        System.out.println("Found " + claim.getLoop2400ServiceLineInformation().size() + " service lines");
        System.out.println("Total claim amount: " + claim.getClaimInformation().getMonetaryAmount());
    }
}