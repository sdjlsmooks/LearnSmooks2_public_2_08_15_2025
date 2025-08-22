#!/usr/bin/env python3
"""
Test script for X12 837 Healthcare Claim Parser
"""

import os
import json
import yaml
from x12_837_parser import X12_837_Parser, X12_837_Interchange


def test_parse_professional_claim():
    """Test parsing of professional (837P) claim"""
    print("Testing Professional Claim (837P) Parsing...")
    print("-" * 50)
    
    parser = X12_837_Parser()
    
    # Read the sample professional claim
    with open('sample_837_professional.edi', 'r') as f:
        edi_content = f.read()
    
    # Parse the EDI
    interchange = parser.parse_edi_to_object(edi_content)
    
    # Validate basic structure
    assert interchange.interchange_header is not None, "ISA segment not parsed"
    assert interchange.group_header is not None, "GS segment not parsed"
    assert interchange.transaction_set_header is not None, "ST segment not parsed"
    
    # Validate interchange header
    assert interchange.interchange_header.interchange_sender_id.strip() == "SUBMITTERID", "Sender ID mismatch"
    assert interchange.interchange_header.interchange_receiver_id.strip() == "RECEIVERID", "Receiver ID mismatch"
    assert interchange.interchange_header.interchange_date == "230315", "Date mismatch"
    
    # Validate transaction set
    assert interchange.transaction_set_header.transaction_set_id_code == "837", "Transaction type should be 837"
    
    # Validate submitter/receiver
    if interchange.submitter:
        assert interchange.submitter.submitter_name.name_last_or_organization_name == "ABC MEDICAL BILLING SERVICE"
    
    # Validate claims
    if interchange.claims:
        print(f"Number of claims parsed: {len(interchange.claims)}")
        for i, claim in enumerate(interchange.claims):
            print(f"  Claim {i+1}: Patient Control Number: {claim.claim.patient_control_number}")
            print(f"           Amount: ${claim.claim.monetary_amount}")
            if claim.service_lines:
                print(f"           Service Lines: {len(claim.service_lines)}")
    
    print("[PASS] Professional claim parsing successful\n")
    return interchange


def test_parse_institutional_claim():
    """Test parsing of institutional (837I) claim"""
    print("Testing Institutional Claim (837I) Parsing...")
    print("-" * 50)
    
    parser = X12_837_Parser()
    
    # Read the sample institutional claim
    with open('sample_837_institutional.edi', 'r') as f:
        edi_content = f.read()
    
    # Parse the EDI
    interchange = parser.parse_edi_to_object(edi_content)
    
    # Validate basic structure
    assert interchange.interchange_header is not None, "ISA segment not parsed"
    assert interchange.group_header is not None, "GS segment not parsed"
    assert interchange.transaction_set_header is not None, "ST segment not parsed"
    
    # Validate interchange header
    assert interchange.interchange_header.interchange_sender_id.strip() == "HOSPITAL001", "Sender ID mismatch"
    assert interchange.interchange_header.interchange_receiver_id.strip() == "PAYER001", "Receiver ID mismatch"
    
    # Validate transaction set
    assert interchange.transaction_set_header.transaction_set_id_code == "837", "Transaction type should be 837"
    
    # Validate claims
    if interchange.claims:
        print(f"Number of claims parsed: {len(interchange.claims)}")
        for i, claim in enumerate(interchange.claims):
            print(f"  Claim {i+1}: Patient Control Number: {claim.claim.patient_control_number}")
            print(f"           Amount: ${claim.claim.monetary_amount}")
            if claim.hi_diagnosis_codes:
                print(f"           Diagnosis Codes: {len(claim.hi_diagnosis_codes)} HI segments")
    
    print("[PASS] Institutional claim parsing successful\n")
    return interchange


def test_json_conversion(interchange: X12_837_Interchange):
    """Test conversion to JSON format"""
    print("Testing JSON Conversion...")
    print("-" * 50)
    
    parser = X12_837_Parser()
    
    # Convert to JSON
    json_output = parser.to_json(interchange)
    
    # Validate JSON structure
    json_data = json.loads(json_output)
    assert 'interchange_header' in json_data, "Missing interchange_header in JSON"
    assert 'group_header' in json_data, "Missing group_header in JSON"
    assert 'transaction_set_header' in json_data, "Missing transaction_set_header in JSON"
    
    # Save JSON to file
    with open('output_837.json', 'w') as f:
        f.write(json_output)
    
    print(f"[PASS] JSON conversion successful")
    print(f"  Output saved to: output_837.json")
    print(f"  JSON size: {len(json_output)} characters\n")
    
    # Print sample of JSON
    print("Sample JSON output (first 500 chars):")
    print(json_output[:500] + "..." if len(json_output) > 500 else json_output)
    print()


def test_yaml_conversion(interchange: X12_837_Interchange):
    """Test conversion to YAML format"""
    print("Testing YAML Conversion...")
    print("-" * 50)
    
    parser = X12_837_Parser()
    
    # Convert to YAML
    yaml_output = parser.to_yaml(interchange)
    
    # Validate YAML structure
    yaml_data = yaml.safe_load(yaml_output)
    assert 'interchange_header' in yaml_data, "Missing interchange_header in YAML"
    assert 'group_header' in yaml_data, "Missing group_header in YAML"
    assert 'transaction_set_header' in yaml_data, "Missing transaction_set_header in YAML"
    
    # Save YAML to file
    with open('output_837.yaml', 'w') as f:
        f.write(yaml_output)
    
    print(f"[PASS] YAML conversion successful")
    print(f"  Output saved to: output_837.yaml")
    print(f"  YAML size: {len(yaml_output)} characters\n")
    
    # Print sample of YAML
    print("Sample YAML output (first 500 chars):")
    print(yaml_output[:500] + "..." if len(yaml_output) > 500 else yaml_output)
    print()


def test_segment_parsing():
    """Test parsing of individual segments"""
    print("Testing Individual Segment Parsing...")
    print("-" * 50)
    
    parser = X12_837_Parser()
    
    # Test ISA parsing
    isa_elements = ['ISA', '00', '          ', '00', '          ', 'ZZ', 'SENDER123      ', 
                    'ZZ', 'RECEIVER456    ', '230315', '1234', '^', '00501', '000000001', 
                    '0', 'P', ':']
    isa = parser._parse_isa(isa_elements)
    assert isa.interchange_sender_id == 'SENDER123      ', "ISA sender parsing failed"
    print("[PASS] ISA segment parsing successful")
    
    # Test NM1 parsing
    nm1_elements = ['NM1', '85', '2', 'MEDICAL CLINIC', '', '', '', '', 'XX', '1234567890']
    nm1 = parser._parse_nm1(nm1_elements)
    assert nm1.entity_identifier_code == '85', "NM1 entity code parsing failed"
    assert nm1.name_last_or_organization_name == 'MEDICAL CLINIC', "NM1 name parsing failed"
    print("[PASS] NM1 segment parsing successful")
    
    # Test CLM parsing
    clm_elements = ['CLM', 'CLAIM001', '1500.00', '', '', '11:B:1', '', 'Y', 'A', 'Y', 'Y']
    clm = parser._parse_clm(clm_elements)
    assert clm.patient_control_number == 'CLAIM001', "CLM patient control number parsing failed"
    assert clm.monetary_amount == '1500.00', "CLM amount parsing failed"
    print("[PASS] CLM segment parsing successful")
    
    # Test DTP parsing
    dtp_elements = ['DTP', '472', 'D8', '20230315']
    dtp = parser._parse_dtp(dtp_elements)
    assert dtp.date_time_qualifier == '472', "DTP qualifier parsing failed"
    assert dtp.date_time_period == '20230315', "DTP date parsing failed"
    print("[PASS] DTP segment parsing successful")
    
    print()


def test_complex_edi_parsing():
    """Test parsing of a complex EDI message with multiple claims and service lines"""
    print("Testing Complex EDI Parsing...")
    print("-" * 50)
    
    complex_edi = """ISA*00*          *00*          *ZZ*MULTICLAIM     *ZZ*BIGPAYER       *230415*1500*^*00501*000003001*0*P*:~
GS*HC*MULTICLAIM*BIGPAYER*20230415*1500*3001*X*005010X222A1~
ST*837*0003*005010X222A1~
BHT*0019*00*BATCH001*20230415*1500*CH~
NM1*41*2*SUPER BILLING CORP*****46*999888777~
NM1*40*2*MEGA INSURANCE CO*****46*111222333~
HL*1**20*1~
NM1*85*2*ADVANCED MEDICAL GROUP*****XX*9988776655~
HL*2*1*22*1~
CLM*CLAIM001*850.00***11:B:1*Y*A*Y*Y~
HI*ABK:J189*ABF:K7211~
DTP*472*D8*20230410~
LX*1~
SV1*HC:99213*250.00*UN*1***1~
DTP*472*D8*20230410~
LX*2~
SV1*HC:87086*150.00*UN*1***1:2~
DTP*472*D8*20230410~
LX*3~
SV1*HC:90715*450.00*UN*1***1~
DTP*472*D8*20230410~
CLM*CLAIM002*1200.00***11:B:1*Y*A*Y*Y~
HI*ABK:E119~
DTP*472*D8*20230412~
LX*1~
SV1*HC:99214*350.00*UN*1***1~
DTP*472*D8*20230412~
LX*2~
SV1*HC:80053*850.00*UN*1***1~
DTP*472*D8*20230412~
SE*30*0003~
GE*1*3001~
IEA*1*000003001~"""
    
    parser = X12_837_Parser()
    interchange = parser.parse_edi_to_object(complex_edi)
    
    # Validate multiple claims
    assert len(interchange.claims) >= 2, "Should have parsed at least 2 claims"
    
    claim1 = interchange.claims[0]
    assert claim1.claim.patient_control_number == "CLAIM001", "First claim ID mismatch"
    assert claim1.claim.monetary_amount == "850.00", "First claim amount mismatch"
    assert len(claim1.service_lines) >= 3, "First claim should have at least 3 service lines"
    
    claim2 = interchange.claims[1]
    assert claim2.claim.patient_control_number == "CLAIM002", "Second claim ID mismatch"
    assert claim2.claim.monetary_amount == "1200.00", "Second claim amount mismatch"
    assert len(claim2.service_lines) >= 2, "Second claim should have at least 2 service lines"
    
    print(f"[PASS] Complex EDI parsing successful")
    print(f"  Total claims: {len(interchange.claims)}")
    print(f"  Claim 1 service lines: {len(claim1.service_lines)}")
    print(f"  Claim 2 service lines: {len(claim2.service_lines)}")
    print()


def test_edi_roundtrip():
    """Test converting EDI to object and back to EDI"""
    print("Testing EDI Round-trip Conversion...")
    print("-" * 50)
    
    simple_edi = """ISA*00*          *00*          *ZZ*SENDER         *ZZ*RECEIVER       *230501*0900*^*00501*000004001*0*P*:~
GS*HC*SENDER*RECEIVER*20230501*0900*4001*X*005010X222A1~
ST*837*0004*005010X222A1~
SE*3*0004~
GE*1*4001~
IEA*1*000004001~"""
    
    parser = X12_837_Parser()
    
    # Parse to object
    interchange = parser.parse_edi_to_object(simple_edi)
    
    # Convert back to EDI
    reconstructed_edi = parser.to_edi(interchange)
    
    # Compare key segments
    original_segments = simple_edi.replace('\n', '').split('~')
    reconstructed_segments = reconstructed_edi.split('~')
    
    # Check ISA segment
    assert reconstructed_segments[0].startswith('ISA'), "Reconstructed EDI should start with ISA"
    assert 'SENDER' in reconstructed_segments[0], "Sender ID should be preserved"
    assert 'RECEIVER' in reconstructed_segments[0], "Receiver ID should be preserved"
    
    print("[PASS] EDI round-trip conversion successful")
    print(f"  Original segments: {len(original_segments)}")
    print(f"  Reconstructed segments: {len(reconstructed_segments)}")
    print()


def main():
    """Run all tests"""
    print("=" * 60)
    print("X12 837 Healthcare Claim Parser Test Suite")
    print("=" * 60)
    print()
    
    try:
        # Test individual segment parsing
        test_segment_parsing()
        
        # Test professional claim
        prof_interchange = test_parse_professional_claim()
        
        # Test institutional claim
        inst_interchange = test_parse_institutional_claim()
        
        # Test format conversions
        test_json_conversion(prof_interchange)
        test_yaml_conversion(prof_interchange)
        
        # Test complex parsing
        test_complex_edi_parsing()
        
        # Test round-trip conversion
        test_edi_roundtrip()
        
        print("=" * 60)
        print("ALL TESTS PASSED SUCCESSFULLY! [PASS]")
        print("=" * 60)
        
    except AssertionError as e:
        print(f"\n[FAIL] TEST FAILED: {e}")
        raise
    except Exception as e:
        print(f"\n[ERROR] UNEXPECTED ERROR: {e}")
        raise


if __name__ == "__main__":
    main()