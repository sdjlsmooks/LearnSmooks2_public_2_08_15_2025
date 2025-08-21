import org.example.XML.X12_837_Parser;

public class TestLoop1000A {
    public static void main(String[] args) throws Exception {
        String edi = "ISA*00*          *00*          *ZZ*SENDER123      *ZZ*RECEIVER456    *210901*1200*^*00501*000000001*0*P*:~" +
            "GS*HC*SENDER123*RECEIVER456*20210901*1200*1*X*005010X223A2~" +
            "ST*837*0001*005010X223A2~" +
            "BHT*0019*00*REF123456*20210901*1200*CH~" +
            "NM1*41*2*SUBMITTER ORGANIZATION*****46*SUB12345~" +
            "PER*IC*JOHN DOE*TE*5551234567*EX*123~" +
            "PER*IC*JANE SMITH*TE*5559876543*FX*9876543210~" +
            "SE*7*0001~" +
            "GE*1*1~" +
            "IEA*1*000000001~";
        
        String xml = X12_837_Parser.parseEDI(edi);
        // Format XML for readability
        xml = xml.replaceAll("><", ">\n<");
        System.out.println(xml);
    }
}