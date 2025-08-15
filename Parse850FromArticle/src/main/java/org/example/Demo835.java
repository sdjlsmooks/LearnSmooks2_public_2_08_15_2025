package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.XML.X12_835_Parser;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class Demo835 {
    public static void main(String[] args) throws Exception {
        // Load an 835 EDI message from resources
        try {
            byte[] ediBytes = Files.readAllBytes(
                    Paths.get(Demo835.class.getClassLoader().getResource("input835.edi").toURI())
            );
            String originalEDIString = Files.readString(Paths.get(Demo835.class.getClassLoader().getResource("input835.edi").toURI()));
            System.out.println("Original      835 EDI:\n" + originalEDIString);

            // EDI -> XML
            String xml = X12_835_Parser.parseEDI(ediBytes);
            System.out.println("Parsed 835 XML:\n" + xml);

            // XML -> EDI
            String edi = X12_835_Parser.xmlToEDI(xml);
            System.out.println("Re-serialized 835 EDI:\n" + edi);
        } catch (IOException e) {
            log.error("IOException Error reading EDI file: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            log.error("URISyntaxException Error reading EDI file: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (SAXException e) {
            log.error("SAXException Error reading EDI file: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
        }
    }
}
