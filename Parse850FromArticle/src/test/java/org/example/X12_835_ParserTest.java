package org.example;

import org.example.XML.X12_835_Parser;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class X12_835_ParserTest {

    @Test
    public void testParseAndSerialize835() throws Exception {
        String fileName = "input835_3.edi";
        byte[] ediBytes = Files.readAllBytes(
                Paths.get(X12_835_ParserTest.class.getClassLoader().getResource(fileName).toURI())
        );
        String ediString = Files.readString(Paths.get(X12_835_ParserTest.class.getClassLoader().getResource(fileName).toURI()));
        assertNotNull(ediString);
        System.out.println("Original EDI: " + ediString);
        String xml = X12_835_Parser.parseEDI(ediBytes);
        assertNotNull(xml);
        System.out.println("XML: " + xml);
        // Validate the parsed XML has required structure
        assertTrue("XML should contain interchange-header", xml.contains("interchange-header"));
        assertTrue("XML should contain group-header", xml.contains("group-header"));

        String edi = X12_835_Parser.toEDIString(xml);
        String fixedEDI = edi.replaceAll("\\?:", ":");
        assertNotNull(edi);
        System.out.println("Parsed   EDI: " + edi);
        System.out.println("Fixed    EDI: " + fixedEDI);
        assertTrue("EDI should contain interchange-header", edi.contains("ISA"));
        assertTrue("EDI should contain group-header", edi.contains("GS"));
        // Verify that fixedEDI contains ISA and GS segments
        assertTrue("fixedEDI should contain ISA segment", fixedEDI.contains("ISA"));
        assertTrue("fixedEDI should contain GS segment", fixedEDI.contains("GS"));

    }
}
