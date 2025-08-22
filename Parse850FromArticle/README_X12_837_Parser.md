# X12 837 Healthcare Claim Parser for Python

A Python-based parser for X12 837 Healthcare Claim transactions, based on the DFDL schema structure from `837_mapping.dfdl.xsd`. This parser supports both Professional (837P) and Institutional (837I) claim formats.

## Features

- Parse X12 837 EDI messages into Python objects
- Convert parsed data to JSON and YAML formats
- Convert Python objects back to EDI format
- Support for multiple claims and service lines
- Comprehensive data model matching the DFDL schema structure

## Installation

### Prerequisites
```bash
pip install pyyaml
```

Or install from requirements.txt:
```bash
pip install -r requirements.txt
```

## Usage

### Basic Parsing Example

```python
from x12_837_parser import X12_837_Parser

# Initialize parser
parser = X12_837_Parser()

# Parse EDI string
edi_content = """ISA*00*          *00*          *ZZ*SENDER         *ZZ*RECEIVER       *230101*1200*^*00501*000000001*0*P*:~
GS*HC*SENDER*RECEIVER*20230101*1200*1*X*005010X222A1~
ST*837*0001*005010X222A1~
BHT*0019*00*123456789*20230101*1200*CH~
NM1*41*2*SUBMITTER ORGANIZATION*****46*123456789~
CLM*PATIENT123*500.00***11:B:1*Y*A*Y*Y~
SE*6*0001~
GE*1*1~
IEA*1*000000001~"""

# Parse to Python object
interchange = parser.parse_edi_to_object(edi_content)

# Convert to JSON
json_output = parser.to_json(interchange)
print(json_output)

# Convert to YAML
yaml_output = parser.to_yaml(interchange)
print(yaml_output)
```

### Parsing from File

```python
# Read EDI file
with open('sample_837_professional.edi', 'r') as f:
    edi_content = f.read()

# Parse the EDI
interchange = parser.parse_edi_to_object(edi_content)

# Access parsed data
if interchange.claims:
    for claim in interchange.claims:
        print(f"Claim ID: {claim.claim.patient_control_number}")
        print(f"Amount: ${claim.claim.monetary_amount}")
        
        # Access service lines
        for service_line in claim.service_lines:
            if service_line.professional_service:
                print(f"  Service: {service_line.professional_service.monetary_amount}")
```

## Data Model

The parser uses a comprehensive data model that matches the X12 837 structure:

### Main Components

- **X12_837_Interchange**: Root container for the entire transaction
- **ISA_InterchangeHeader**: Interchange control header
- **GS_FunctionalGroupHeader**: Functional group header  
- **ST_TransactionSetHeader**: Transaction set header
- **BHT_BeginningHierarchicalTransaction**: Beginning of hierarchical transaction

### Loop Structures

- **Loop1000A**: Submitter Name
- **Loop1000B**: Receiver Name
- **Loop2010AA**: Billing Provider Name
- **Loop2300**: Claim Information
- **Loop2400**: Service Line

### Key Segments

- **NM1_Segment**: Individual or Organizational Name
- **CLM_Segment**: Health Claim
- **HI_Segment**: Health Care Information Codes
- **SV1_Segment**: Professional Service
- **DTP_Segment**: Date or Time Period
- **REF_Segment**: Reference Information
- **PER_Segment**: Administrative Communications Contact

## Supported Segments

The parser currently supports the following X12 837 segments:

- ISA (Interchange Control Header)
- GS (Functional Group Header)
- ST (Transaction Set Header)
- BHT (Beginning of Hierarchical Transaction)
- NM1 (Individual/Organizational Name)
- PER (Administrative Communications Contact)
- HL (Hierarchical Level)
- CLM (Health Claim)
- HI (Health Care Information Codes)
- DTP (Date or Time Period)
- LX (Assigned Number)
- SV1 (Professional Service)
- REF (Reference Information)
- SE (Transaction Set Trailer)
- GE (Functional Group Trailer)
- IEA (Interchange Control Trailer)

## Testing

Run the comprehensive test suite:

```bash
python test_837_parser.py
```

The test suite includes:
- Individual segment parsing tests
- Professional claim (837P) parsing
- Institutional claim (837I) parsing
- JSON conversion
- YAML conversion
- Complex multi-claim parsing
- EDI round-trip conversion

## Sample Files

The repository includes sample EDI files for testing:

- `sample_837_professional.edi` - Professional claim example
- `sample_837_institutional.edi` - Institutional claim example

## Output Files

When running tests, the following output files are generated:

- `output_837.json` - JSON representation of parsed EDI
- `output_837.yaml` - YAML representation of parsed EDI

## Limitations

Current implementation has the following limitations:

1. Simplified EDI-to-object round-trip conversion (full implementation would require handling all segments)
2. JSON-to-object deserialization is not fully implemented
3. Not all X12 837 segments are fully supported (focus on core segments)

## Future Enhancements

- Complete support for all X12 837 segments
- Full bidirectional conversion (EDI ↔ JSON ↔ Object)
- Validation against X12 837 specifications
- Support for batch processing of multiple EDI files
- Integration with database storage
- Support for X12 835 (Healthcare Claim Payment/Advice)

## License

This implementation is based on the DFDL schema structure and follows X12 EDI standards.