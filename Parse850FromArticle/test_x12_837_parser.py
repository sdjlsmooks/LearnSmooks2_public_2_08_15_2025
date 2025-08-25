#!/usr/bin/env python3
"""
Test file for X12 837 Healthcare Claim Parser
Tests parsing of all loops including 2310, 2400, 2410, 2420A-H, 2430, and 2440
"""

import unittest
from x12_837_parser import X12_837_Parser


class TestX12837Parser(unittest.TestCase):
    """Test cases for X12 837 parser"""
    
    def setUp(self):
        """Set up test parser"""
        self.parser = X12_837_Parser()
    
    def test_parse_basic_claim(self):
        """Test parsing a basic 837 claim"""
        edi = """ISA*00*          *00*          *ZZ*SENDER123      *ZZ*RECEIVER456    *210901*1200*^*00501*000000001*0*P*:~
GS*HC*SENDER123*RECEIVER456*20210901*1200*1*X*005010X223A2~
ST*837*0001*005010X223A2~
BHT*0019*00*REF123456*20210901*1200*CH~
NM1*41*2*SUBMITTER ORGANIZATION*****46*SUB12345~
NM1*40*2*RECEIVER CORPORATION*****46*REC67890~
HL*1**20*1~
NM1*85*2*BILLING PROVIDER INC*****XX*1234567890~
HL*2*1*22*0~
CLM*CLAIM001*1000.00***11:B:1*Y*A*Y~
HI*ABK:E119~
LX*1~
SV1*HC:99213*250.00*UN*1***1~
DTP*472*D8*20210901~
SE*14*0001~
GE*1*1~
IEA*1*000000001~"""
        
        interchange = self.parser.parse_edi_to_object(edi)
        
        # Verify basic structure
        self.assertIsNotNone(interchange)
        self.assertIsNotNone(interchange.interchange_header)
        self.assertEqual(interchange.interchange_header.interchange_sender_id.strip(), "SENDER123")
        self.assertEqual(interchange.interchange_header.interchange_receiver_id.strip(), "RECEIVER456")
        
        # Verify claim
        self.assertIsNotNone(interchange.claims)
        self.assertEqual(len(interchange.claims), 1)
        claim = interchange.claims[0]
        self.assertEqual(claim.claim.patient_control_number, "CLAIM001")
        self.assertEqual(claim.claim.monetary_amount, "1000.00")
        
        # Verify service line
        self.assertIsNotNone(claim.service_lines)
        self.assertEqual(len(claim.service_lines), 1)
        service_line = claim.service_lines[0]
        self.assertEqual(service_line.service_line_number, "1")
        self.assertIsNotNone(service_line.professional_service)
        self.assertEqual(service_line.professional_service.monetary_amount, "250.00")
    
    def test_parse_loop_2310(self):
        """Test parsing Loop 2310 (Claim level providers)"""
        edi = """ISA*00*          *00*          *ZZ*SENDER123      *ZZ*RECEIVER456    *210901*1200*^*00501*000000001*0*P*:~
GS*HC*SENDER123*RECEIVER456*20210901*1200*1*X*005010X223A2~
ST*837*0001*005010X223A2~
BHT*0019*00*REF123456*20210901*1200*CH~
NM1*41*2*SUBMITTER ORGANIZATION*****46*SUB12345~
NM1*40*2*RECEIVER CORPORATION*****46*REC67890~
HL*1**20*1~
NM1*85*2*BILLING PROVIDER INC*****XX*1234567890~
HL*2*1*22*0~
CLM*CLAIM001*1000.00***11:B:1*Y*A*Y~
NM1*DN*1*SMITH*JOHN*D***XX*9876543210~
REF*G2*REF123~
NM1*82*1*DOE*JANE*M***XX*1234567890~
REF*0B*LIC456~
NM1*77*2*MEMORIAL HOSPITAL*****XX*5555555555~
N3*123 HOSPITAL WAY~
N4*CHICAGO*IL*60601~
REF*G2*HOSP789~
LX*1~
SV1*HC:99213*250.00*UN*1***1~
DTP*472*D8*20210901~
SE*21*0001~
GE*1*1~
IEA*1*000000001~"""
        
        interchange = self.parser.parse_edi_to_object(edi)
        claim = interchange.claims[0]
        
        # Verify Loop 2310A - Referring Provider
        self.assertIsNotNone(claim.referring_provider)
        self.assertEqual(claim.referring_provider.referring_provider_name.name_last_or_organization_name, "SMITH")
        self.assertEqual(claim.referring_provider.referring_provider_name.name_first, "JOHN")
        self.assertIsNotNone(claim.referring_provider.referring_provider_secondary_identification)
        self.assertEqual(len(claim.referring_provider.referring_provider_secondary_identification), 1)
        self.assertEqual(claim.referring_provider.referring_provider_secondary_identification[0].reference_identification, "REF123")
        
        # Verify Loop 2310B - Rendering Provider
        self.assertIsNotNone(claim.rendering_provider)
        self.assertEqual(claim.rendering_provider.rendering_provider_name.name_last_or_organization_name, "DOE")
        self.assertEqual(claim.rendering_provider.rendering_provider_name.name_first, "JANE")
        
        # Verify Loop 2310C - Service Facility Location
        self.assertIsNotNone(claim.service_facility_location)
        self.assertEqual(claim.service_facility_location.service_facility_name.name_last_or_organization_name, "MEMORIAL HOSPITAL")
        self.assertIsNotNone(claim.service_facility_location.service_facility_address)
        self.assertEqual(claim.service_facility_location.service_facility_address.address_information_1, "123 HOSPITAL WAY")
        self.assertIsNotNone(claim.service_facility_location.service_facility_city_state_zip)
        self.assertEqual(claim.service_facility_location.service_facility_city_state_zip.city_name, "CHICAGO")
        self.assertEqual(claim.service_facility_location.service_facility_city_state_zip.state_or_province_code, "IL")
        self.assertEqual(claim.service_facility_location.service_facility_city_state_zip.postal_code, "60601")
    
    def test_parse_loop_2410_2420(self):
        """Test parsing Loop 2410 (Drug Identification) and Loop 2420 (Service Line Providers)"""
        edi = """ISA*00*          *00*          *ZZ*SENDER123      *ZZ*RECEIVER456    *210901*1200*^*00501*000000001*0*P*:~
GS*HC*SENDER123*RECEIVER456*20210901*1200*1*X*005010X223A2~
ST*837*0001*005010X223A2~
BHT*0019*00*REF123456*20210901*1200*CH~
NM1*41*2*SUBMITTER ORGANIZATION*****46*SUB12345~
NM1*40*2*RECEIVER CORPORATION*****46*REC67890~
HL*1**20*1~
NM1*85*2*BILLING PROVIDER INC*****XX*1234567890~
HL*2*1*22*0~
CLM*CLAIM001*1000.00***11:B:1*Y*A*Y~
LX*1~
SV1*HC:99213*250.00*UN*1***1~
DTP*472*D8*20210901~
LIN**N4*12345678901~
CTP**AWP*10.50*100*EA~
NM1*82*1*JOHNSON*ROBERT****XX*8888888888~
REF*0B*PROV123~
NM1*77*2*CLINIC ABC*****XX*7777777777~
N3*456 CLINIC STREET~
N4*NEW YORK*NY*10001~
SE*20*0001~
GE*1*1~
IEA*1*000000001~"""
        
        interchange = self.parser.parse_edi_to_object(edi)
        service_line = interchange.claims[0].service_lines[0]
        
        # Verify Loop 2410 - Drug Identification
        self.assertIsNotNone(service_line.drug_identification)
        self.assertIsNotNone(service_line.drug_identification.drug_identification)
        self.assertEqual(service_line.drug_identification.drug_identification.product_service_id_qualifier, "N4")
        self.assertEqual(service_line.drug_identification.drug_identification.product_service_id, "12345678901")
        
        # Verify CTP segment in Loop 2410
        self.assertIsNotNone(service_line.drug_identification.drug_quantity)
        self.assertEqual(service_line.drug_identification.drug_quantity.price_identifier_code, "AWP")
        self.assertEqual(service_line.drug_identification.drug_quantity.unit_price, "10.50")
        self.assertEqual(service_line.drug_identification.drug_quantity.quantity, "100")
        
        # Verify Loop 2420A - Rendering Provider
        self.assertIsNotNone(service_line.rendering_provider)
        self.assertEqual(service_line.rendering_provider.rendering_provider_name.name_last_or_organization_name, "JOHNSON")
        self.assertEqual(service_line.rendering_provider.rendering_provider_name.name_first, "ROBERT")
        
        # Verify Loop 2420C - Service Facility Location
        self.assertIsNotNone(service_line.service_facility_location)
        self.assertEqual(service_line.service_facility_location.service_facility_name.name_last_or_organization_name, "CLINIC ABC")
        self.assertIsNotNone(service_line.service_facility_location.service_facility_address)
        self.assertEqual(service_line.service_facility_location.service_facility_address.address_information_1, "456 CLINIC STREET")
    
    def test_parse_loop_2430_2440(self):
        """Test parsing Loop 2430 (Line Adjudication) and Loop 2440 (Form Identification)"""
        edi = """ISA*00*          *00*          *ZZ*SENDER123      *ZZ*RECEIVER456    *210901*1200*^*00501*000000001*0*P*:~
GS*HC*SENDER123*RECEIVER456*20210901*1200*1*X*005010X223A2~
ST*837*0001*005010X223A2~
BHT*0019*00*REF123456*20210901*1200*CH~
NM1*41*2*SUBMITTER ORGANIZATION*****46*SUB12345~
NM1*40*2*RECEIVER CORPORATION*****46*REC67890~
HL*1**20*1~
NM1*85*2*BILLING PROVIDER INC*****XX*1234567890~
HL*2*1*22*0~
CLM*CLAIM001*1000.00***11:B:1*Y*A*Y~
LX*1~
SV1*HC:99213*250.00*UN*1***1~
DTP*472*D8*20210901~
SVD*PAYER123*200.00*HC:99213**1~
CAS*PR*1*50.00~
DTP*573*D8*20210815~
AMT*EAF*50.00~
LQ*UT*1234~
FRM*1*Y~
FRM*2*N~
FRM*3A*RESPONSE TEXT~
SE*19*0001~
GE*1*1~
IEA*1*000000001~"""
        
        interchange = self.parser.parse_edi_to_object(edi)
        service_line = interchange.claims[0].service_lines[0]
        
        # Verify Loop 2430 - Line Adjudication Information
        self.assertIsNotNone(service_line.line_adjudication_information)
        self.assertEqual(len(service_line.line_adjudication_information), 1)
        adjudication = service_line.line_adjudication_information[0]
        
        # Verify SVD segment
        self.assertIsNotNone(adjudication.line_adjudication_information)
        self.assertEqual(adjudication.line_adjudication_information.other_payer_primary_identifier, "PAYER123")
        self.assertEqual(adjudication.line_adjudication_information.service_line_paid_amount, "200.00")
        self.assertEqual(adjudication.line_adjudication_information.composite_medical_procedure_identifier, "HC:99213")
        
        # Verify CAS segment
        self.assertIsNotNone(adjudication.line_adjustment)
        self.assertEqual(len(adjudication.line_adjustment), 1)
        self.assertEqual(adjudication.line_adjustment[0].claim_adjustment_group_code, "PR")
        self.assertEqual(adjudication.line_adjustment[0].claim_adjustment_reason_code, "1")
        self.assertEqual(adjudication.line_adjustment[0].monetary_amount, "50.00")
        
        # Verify DTP segment (check/remittance date)
        self.assertIsNotNone(adjudication.line_check_or_remittance_date)
        self.assertEqual(adjudication.line_check_or_remittance_date.date_time_qualifier, "573")
        self.assertEqual(adjudication.line_check_or_remittance_date.date_time_period, "20210815")
        
        # Verify AMT segment (remaining patient liability)
        self.assertIsNotNone(adjudication.remaining_patient_liability)
        self.assertEqual(adjudication.remaining_patient_liability.amount_qualifier_code, "EAF")
        self.assertEqual(adjudication.remaining_patient_liability.monetary_amount, "50.00")
        
        # Verify Loop 2440 - Form Identification Code
        self.assertIsNotNone(service_line.form_identification_codes)
        self.assertEqual(len(service_line.form_identification_codes), 1)
        form = service_line.form_identification_codes[0]
        
        # Verify LQ segment
        self.assertIsNotNone(form.form_identification_code)
        self.assertEqual(form.form_identification_code.code_list_qualifier_code, "UT")
        self.assertEqual(form.form_identification_code.industry_code, "1234")
        
        # Verify FRM segments
        self.assertIsNotNone(form.supporting_documentation)
        self.assertEqual(len(form.supporting_documentation), 3)
        
        self.assertEqual(form.supporting_documentation[0].question_number_letter, "1")
        self.assertEqual(form.supporting_documentation[0].question_response, "Y")
        
        self.assertEqual(form.supporting_documentation[1].question_number_letter, "2")
        self.assertEqual(form.supporting_documentation[1].question_response, "N")
        
        self.assertEqual(form.supporting_documentation[2].question_number_letter, "3A")
        self.assertEqual(form.supporting_documentation[2].question_response, "RESPONSE TEXT")
    
    def test_json_conversion(self):
        """Test conversion to JSON format"""
        edi = """ISA*00*          *00*          *ZZ*SENDER123      *ZZ*RECEIVER456    *210901*1200*^*00501*000000001*0*P*:~
GS*HC*SENDER123*RECEIVER456*20210901*1200*1*X*005010X223A2~
ST*837*0001*005010X223A2~
BHT*0019*00*REF123456*20210901*1200*CH~
NM1*41*2*SUBMITTER ORGANIZATION*****46*SUB12345~
CLM*CLAIM001*1000.00***11:B:1*Y*A*Y~
SE*6*0001~
GE*1*1~
IEA*1*000000001~"""
        
        interchange = self.parser.parse_edi_to_object(edi)
        json_output = self.parser.to_json(interchange)
        
        self.assertIsNotNone(json_output)
        self.assertIn('SENDER123', json_output)
        self.assertIn('"patient_control_number": "CLAIM001"', json_output)
    
    def test_yaml_conversion(self):
        """Test conversion to YAML format"""
        edi = """ISA*00*          *00*          *ZZ*SENDER123      *ZZ*RECEIVER456    *210901*1200*^*00501*000000001*0*P*:~
GS*HC*SENDER123*RECEIVER456*20210901*1200*1*X*005010X223A2~
ST*837*0001*005010X223A2~
BHT*0019*00*REF123456*20210901*1200*CH~
NM1*41*2*SUBMITTER ORGANIZATION*****46*SUB12345~
CLM*CLAIM001*1000.00***11:B:1*Y*A*Y~
SE*6*0001~
GE*1*1~
IEA*1*000000001~"""
        
        interchange = self.parser.parse_edi_to_object(edi)
        yaml_output = self.parser.to_yaml(interchange)
        
        self.assertIsNotNone(yaml_output)
        self.assertIn('SENDER123', yaml_output)
        self.assertIn('patient_control_number: CLAIM001', yaml_output)


if __name__ == '__main__':
    unittest.main()