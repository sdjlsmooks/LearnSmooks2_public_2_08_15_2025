#!/usr/bin/env python3
"""
Test script for X12 837 Healthcare Claim Parser - Loop 2310A-F Testing
Tests the newly added Loop 2310A through 2310F structures
"""

import os
import json
import yaml
from x12_837_parser import X12_837_Parser, X12_837_Interchange


def test_loop_2310_parsing():
    """Test parsing of Loop 2310A through 2310F"""
    print("Testing Loop 2310A-F Parsing...")
    print("-" * 50)
    
    parser = X12_837_Parser()
    
    # Read the sample with 2310 loops
    with open('sample_837_with_2310_loops.edi', 'r') as f:
        edi_content = f.read()
    
    # Parse the EDI
    interchange = parser.parse_edi_to_object(edi_content)
    
    # Validate basic structure
    assert interchange.interchange_header is not None, "ISA segment not parsed"
    assert interchange.claims is not None and len(interchange.claims) > 0, "No claims parsed"
    
    # Get first claim for testing
    claim1 = interchange.claims[0]
    
    # Test Loop 2310A - Referring Provider
    print("Testing Loop 2310A - Referring Provider...")
    assert claim1.referring_provider is not None, "Loop 2310A not parsed"
    assert claim1.referring_provider.referring_provider_name is not None, "Referring provider name missing"
    assert claim1.referring_provider.referring_provider_name.entity_identifier_code == "DN", "Wrong entity code for referring provider"
    assert claim1.referring_provider.referring_provider_name.name_last_or_organization_name == "WILLIAMS", "Referring provider name mismatch"
    assert claim1.referring_provider.referring_provider_name.name_first == "MICHAEL", "Referring provider first name mismatch"
    
    # Check REF segments
    if claim1.referring_provider.referring_provider_secondary_identification:
        print(f"  Found {len(claim1.referring_provider.referring_provider_secondary_identification)} REF segments")
        for ref in claim1.referring_provider.referring_provider_secondary_identification:
            print(f"    REF: {ref.reference_identification_qualifier} = {ref.reference_identification}")
    
    # Test Loop 2310B - Rendering Provider
    print("Testing Loop 2310B - Rendering Provider...")
    assert claim1.rendering_provider is not None, "Loop 2310B not parsed"
    assert claim1.rendering_provider.rendering_provider_name is not None, "Rendering provider name missing"
    assert claim1.rendering_provider.rendering_provider_name.entity_identifier_code == "82", "Wrong entity code for rendering provider"
    assert claim1.rendering_provider.rendering_provider_name.name_last_or_organization_name == "ANDERSON", "Rendering provider name mismatch"
    
    # Test Loop 2310C - Service Facility Location
    print("Testing Loop 2310C - Service Facility Location...")
    assert claim1.service_facility_location is not None, "Loop 2310C not parsed"
    assert claim1.service_facility_location.service_facility_name is not None, "Service facility name missing"
    assert claim1.service_facility_location.service_facility_name.entity_identifier_code == "77", "Wrong entity code for service facility"
    assert claim1.service_facility_location.service_facility_name.name_last_or_organization_name == "CITY GENERAL HOSPITAL", "Service facility name mismatch"
    
    # Check N3 and N4 segments
    assert claim1.service_facility_location.service_facility_address is not None, "Service facility address missing"
    assert claim1.service_facility_location.service_facility_address.address_information_1 == "1500 HOSPITAL BOULEVARD", "Service facility address mismatch"
    
    assert claim1.service_facility_location.service_facility_city_state_zip is not None, "Service facility city/state/zip missing"
    assert claim1.service_facility_location.service_facility_city_state_zip.city_name == "MEDICAL CENTER", "Service facility city mismatch"
    assert claim1.service_facility_location.service_facility_city_state_zip.state_or_province_code == "FL", "Service facility state mismatch"
    assert claim1.service_facility_location.service_facility_city_state_zip.postal_code == "33103", "Service facility zip mismatch"
    
    # Check PER segment
    if claim1.service_facility_location.service_facility_contact_information:
        per = claim1.service_facility_location.service_facility_contact_information
        print(f"  Contact: {per.name}, Phone: {per.communication_number_1}")
    
    # Test Loop 2310D - Supervising Provider
    print("Testing Loop 2310D - Supervising Provider...")
    assert claim1.supervising_provider is not None, "Loop 2310D not parsed"
    assert claim1.supervising_provider.supervising_provider_name is not None, "Supervising provider name missing"
    assert claim1.supervising_provider.supervising_provider_name.entity_identifier_code == "DK", "Wrong entity code for supervising provider"
    assert claim1.supervising_provider.supervising_provider_name.name_last_or_organization_name == "SUPERVISING", "Supervising provider name mismatch"
    
    # Test Loop 2310E - Ambulance Pick-up Location
    print("Testing Loop 2310E - Ambulance Pick-up Location...")
    assert claim1.ambulance_pickup_location is not None, "Loop 2310E not parsed"
    assert claim1.ambulance_pickup_location.ambulance_pickup_location_name is not None, "Ambulance pickup name missing"
    assert claim1.ambulance_pickup_location.ambulance_pickup_location_name.entity_identifier_code == "PW", "Wrong entity code for ambulance pickup"
    assert claim1.ambulance_pickup_location.ambulance_pickup_location_name.name_last_or_organization_name == "METRO AMBULANCE SERVICE", "Ambulance pickup name mismatch"
    
    assert claim1.ambulance_pickup_location.ambulance_pickup_address is not None, "Ambulance pickup address missing"
    assert claim1.ambulance_pickup_location.ambulance_pickup_address.address_information_1 == "100 EMERGENCY WAY", "Ambulance pickup address mismatch"
    
    assert claim1.ambulance_pickup_location.ambulance_pickup_city_state_zip is not None, "Ambulance pickup city/state/zip missing"
    assert claim1.ambulance_pickup_location.ambulance_pickup_city_state_zip.city_name == "PICKUP CITY", "Ambulance pickup city mismatch"
    
    # Test Loop 2310F - Ambulance Drop-off Location
    print("Testing Loop 2310F - Ambulance Drop-off Location...")
    assert claim1.ambulance_dropoff_location is not None, "Loop 2310F not parsed"
    assert claim1.ambulance_dropoff_location.ambulance_dropoff_location_name is not None, "Ambulance dropoff name missing"
    assert claim1.ambulance_dropoff_location.ambulance_dropoff_location_name.entity_identifier_code == "45", "Wrong entity code for ambulance dropoff"
    assert claim1.ambulance_dropoff_location.ambulance_dropoff_location_name.name_last_or_organization_name == "CITY GENERAL HOSPITAL EMERGENCY", "Ambulance dropoff name mismatch"
    
    assert claim1.ambulance_dropoff_location.ambulance_dropoff_address is not None, "Ambulance dropoff address missing"
    assert claim1.ambulance_dropoff_location.ambulance_dropoff_city_state_zip is not None, "Ambulance dropoff city/state/zip missing"
    
    print("[PASS] All Loop 2310A-F structures parsed successfully\n")
    return interchange


def test_multiple_claims_with_2310_loops():
    """Test parsing multiple claims with different 2310 loop configurations"""
    print("Testing Multiple Claims with Different 2310 Loops...")
    print("-" * 50)
    
    parser = X12_837_Parser()
    
    # Read the sample with multiple claims
    with open('sample_837_with_2310_loops.edi', 'r') as f:
        edi_content = f.read()
    
    # Parse the EDI
    interchange = parser.parse_edi_to_object(edi_content)
    
    # Should have 2 claims
    assert len(interchange.claims) == 2, f"Expected 2 claims, got {len(interchange.claims)}"
    
    claim1 = interchange.claims[0]
    claim2 = interchange.claims[1]
    
    # First claim should have all 2310 loops
    print("Claim 1 - Should have all 2310 loops:")
    print(f"  2310A (Referring): {'Yes' if claim1.referring_provider else 'No'}")
    print(f"  2310B (Rendering): {'Yes' if claim1.rendering_provider else 'No'}")
    print(f"  2310C (Service Facility): {'Yes' if claim1.service_facility_location else 'No'}")
    print(f"  2310D (Supervising): {'Yes' if claim1.supervising_provider else 'No'}")
    print(f"  2310E (Ambulance Pickup): {'Yes' if claim1.ambulance_pickup_location else 'No'}")
    print(f"  2310F (Ambulance Dropoff): {'Yes' if claim1.ambulance_dropoff_location else 'No'}")
    
    # Second claim should have fewer 2310 loops
    print("\nClaim 2 - Should have some 2310 loops:")
    print(f"  2310A (Referring): {'Yes' if claim2.referring_provider else 'No'}")
    print(f"  2310B (Rendering): {'Yes' if claim2.rendering_provider else 'No'}")
    print(f"  2310C (Service Facility): {'Yes' if claim2.service_facility_location else 'No'}")
    print(f"  2310D (Supervising): {'Yes' if claim2.supervising_provider else 'No'}")
    print(f"  2310E (Ambulance Pickup): {'Yes' if claim2.ambulance_pickup_location else 'No'}")
    print(f"  2310F (Ambulance Dropoff): {'Yes' if claim2.ambulance_dropoff_location else 'No'}")
    
    print("\n[PASS] Multiple claims with 2310 loops parsed successfully\n")


def test_json_yaml_conversion_with_2310():
    """Test JSON and YAML conversion with 2310 loops"""
    print("Testing JSON/YAML Conversion with 2310 Loops...")
    print("-" * 50)
    
    parser = X12_837_Parser()
    
    # Read and parse the sample
    with open('sample_837_with_2310_loops.edi', 'r') as f:
        edi_content = f.read()
    
    interchange = parser.parse_edi_to_object(edi_content)
    
    # Convert to JSON
    json_output = parser.to_json(interchange)
    json_data = json.loads(json_output)
    
    # Verify 2310 loops are in JSON
    claim_json = json_data['claims'][0]
    assert 'referring_provider' in claim_json, "referring_provider missing in JSON"
    assert 'rendering_provider' in claim_json, "rendering_provider missing in JSON"
    assert 'service_facility_location' in claim_json, "service_facility_location missing in JSON"
    assert 'supervising_provider' in claim_json, "supervising_provider missing in JSON"
    assert 'ambulance_pickup_location' in claim_json, "ambulance_pickup_location missing in JSON"
    assert 'ambulance_dropoff_location' in claim_json, "ambulance_dropoff_location missing in JSON"
    
    # Save JSON with 2310 loops
    with open('output_837_with_2310.json', 'w') as f:
        f.write(json_output)
    print(f"  JSON saved to: output_837_with_2310.json")
    
    # Convert to YAML
    yaml_output = parser.to_yaml(interchange)
    yaml_data = yaml.safe_load(yaml_output)
    
    # Verify 2310 loops are in YAML
    claim_yaml = yaml_data['claims'][0]
    assert 'referring_provider' in claim_yaml, "referring_provider missing in YAML"
    assert 'rendering_provider' in claim_yaml, "rendering_provider missing in YAML"
    assert 'service_facility_location' in claim_yaml, "service_facility_location missing in YAML"
    
    # Save YAML with 2310 loops
    with open('output_837_with_2310.yaml', 'w') as f:
        f.write(yaml_output)
    print(f"  YAML saved to: output_837_with_2310.yaml")
    
    print("\n[PASS] JSON/YAML conversion with 2310 loops successful\n")


def test_entity_code_recognition():
    """Test that entity codes are correctly recognized for 2310 loops"""
    print("Testing Entity Code Recognition for 2310 Loops...")
    print("-" * 50)
    
    parser = X12_837_Parser()
    
    # Create a simple EDI with specific entity codes
    test_edi = """ISA*00*          *00*          *ZZ*TEST           *ZZ*RECEIVER       *230501*1200*^*00501*000000001*0*P*:~
GS*HC*TEST*RECEIVER*20230501*1200*1*X*005010X222A1~
ST*837*0001*005010X222A1~
BHT*0019*00*TEST123*20230501*1200*CH~
NM1*41*2*SUBMITTER*****46*123456789~
NM1*40*2*RECEIVER*****46*987654321~
HL*1**20*1~
NM1*85*2*BILLING PROVIDER*****XX*1234567890~
HL*2*1*22*0~
CLM*TEST001*1000.00***11:B:1*Y*A*Y*Y~
NM1*DN*1*REFERRING*DOCTOR****XX*1111111111~
NM1*82*1*RENDERING*PROVIDER****XX*2222222222~
NM1*77*2*SERVICE*FACILITY****XX*3333333333~
N3*123 FACILITY ST~
N4*CITY*ST*12345~
NM1*DK*1*SUPERVISING*DOCTOR****XX*4444444444~
NM1*PW*2*AMBULANCE*PICKUP****XX*5555555555~
N3*456 PICKUP ST~
N4*PICKUP*ST*54321~
NM1*45*2*AMBULANCE*DROPOFF****XX*6666666666~
N3*789 DROPOFF ST~
N4*DROPOFF*ST*67890~
SE*23*0001~
GE*1*1~
IEA*1*000000001~"""
    
    # Parse the test EDI
    interchange = parser.parse_edi_to_object(test_edi)
    
    # Verify entity codes were correctly recognized
    claim = interchange.claims[0]
    
    entity_tests = [
        ('DN', claim.referring_provider, 'referring_provider'),
        ('82', claim.rendering_provider, 'rendering_provider'),
        ('77', claim.service_facility_location, 'service_facility_location'),
        ('DK', claim.supervising_provider, 'supervising_provider'),
        ('PW', claim.ambulance_pickup_location, 'ambulance_pickup_location'),
        ('45', claim.ambulance_dropoff_location, 'ambulance_dropoff_location')
    ]
    
    for entity_code, loop_obj, loop_name in entity_tests:
        assert loop_obj is not None, f"Loop {loop_name} with entity code {entity_code} not parsed"
        print(f"  Entity code {entity_code:3s} -> {loop_name:30s} [PASS]")
    
    print("\n[PASS] All entity codes correctly recognized\n")


def main():
    """Run all Loop 2310 tests"""
    print("=" * 60)
    print("X12 837 Parser - Loop 2310A-F Test Suite")
    print("=" * 60)
    print()
    
    try:
        # Test basic 2310 loop parsing
        test_loop_2310_parsing()
        
        # Test multiple claims with different 2310 configurations
        test_multiple_claims_with_2310_loops()
        
        # Test JSON/YAML conversion
        test_json_yaml_conversion_with_2310()
        
        # Test entity code recognition
        test_entity_code_recognition()
        
        print("=" * 60)
        print("ALL LOOP 2310 TESTS PASSED SUCCESSFULLY! [PASS]")
        print("=" * 60)
        
    except AssertionError as e:
        print(f"\n[FAIL] TEST FAILED: {e}")
        raise
    except Exception as e:
        print(f"\n[ERROR] UNEXPECTED ERROR: {e}")
        raise


if __name__ == "__main__":
    main()