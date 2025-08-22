#!/usr/bin/env python3
"""
X12 837 Healthcare Claim Parser
Based on the DFDL schema structure from 837_mapping.dfdl.xsd
"""

import json
import yaml
from typing import Dict, List, Optional, Any
from dataclasses import dataclass, field, asdict
from datetime import datetime
import re


@dataclass
class Segment:
    """Base class for X12 segments"""
    segment_id: str
    elements: List[str] = field(default_factory=list)
    
    def to_dict(self) -> Dict:
        return asdict(self)


@dataclass
class ISA_InterchangeHeader:
    """ISA - Interchange Control Header"""
    authorization_info_qualifier: str
    authorization_info: str
    security_info_qualifier: str
    security_info: str
    interchange_id_qualifier_sender: str
    interchange_sender_id: str
    interchange_id_qualifier_receiver: str
    interchange_receiver_id: str
    interchange_date: str
    interchange_time: str
    repetition_separator: str
    interchange_control_version: str
    interchange_control_number: str
    acknowledgment_requested: str
    usage_indicator: str
    component_element_separator: str


@dataclass
class GS_FunctionalGroupHeader:
    """GS - Functional Group Header"""
    functional_id_code: str
    application_sender_code: str
    application_receiver_code: str
    date: str
    time: str
    group_control_number: str
    responsible_agency_code: str
    version_release_industry_id: str


@dataclass
class ST_TransactionSetHeader:
    """ST - Transaction Set Header"""
    transaction_set_id_code: str
    transaction_set_control_number: str
    implementation_convention_reference: Optional[str] = None


@dataclass
class BHT_BeginningHierarchicalTransaction:
    """BHT - Beginning of Hierarchical Transaction"""
    hierarchical_structure_code: str
    transaction_set_purpose_code: str
    originator_application_transaction_id: str
    transaction_set_creation_date: str
    transaction_set_creation_time: str
    claim_or_encounter_identifier: Optional[str] = None


@dataclass
class NM1_Segment:
    """NM1 - Individual or Organizational Name"""
    entity_identifier_code: str
    entity_type_qualifier: str
    name_last_or_organization_name: str
    name_first: Optional[str] = None
    name_middle: Optional[str] = None
    name_prefix: Optional[str] = None
    name_suffix: Optional[str] = None
    identification_code_qualifier: Optional[str] = None
    identification_code: Optional[str] = None


@dataclass
class PER_Segment:
    """PER - Administrative Communications Contact"""
    contact_function_code: str
    name: Optional[str] = None
    communication_number_qualifier_1: Optional[str] = None
    communication_number_1: Optional[str] = None
    communication_number_qualifier_2: Optional[str] = None
    communication_number_2: Optional[str] = None
    communication_number_qualifier_3: Optional[str] = None
    communication_number_3: Optional[str] = None


@dataclass
class HL_Segment:
    """HL - Hierarchical Level"""
    hierarchical_id_number: str
    hierarchical_level_code: str
    hierarchical_child_code: str
    hierarchical_parent_id_number: Optional[str] = None


@dataclass
class CLM_Segment:
    """CLM - Health Claim"""
    patient_control_number: str
    monetary_amount: str
    claim_filing_indicator_code: Optional[str] = None
    non_institutional_claim_type_code: Optional[str] = None
    facility_code_value: Optional[str] = None
    facility_code_qualifier: Optional[str] = None
    claim_frequency_type_code: Optional[str] = None
    yes_no_condition_response_code: Optional[str] = None
    provider_accept_assignment_code: Optional[str] = None
    yes_no_condition_response_code_2: Optional[str] = None
    release_of_information_code: Optional[str] = None
    patient_signature_source_code: Optional[str] = None


@dataclass
class HI_Segment:
    """HI - Health Care Information Codes"""
    health_care_code_information: List[Dict[str, str]] = field(default_factory=list)


@dataclass
class SV1_Segment:
    """SV1 - Professional Service"""
    composite_medical_procedure_identifier: Dict[str, str]
    monetary_amount: str
    unit_or_basis_for_measurement_code: Optional[str] = None
    quantity: Optional[str] = None
    facility_code_value: Optional[str] = None
    service_type_code: Optional[str] = None
    composite_diagnosis_code_pointer: Optional[List[str]] = None


@dataclass
class DTP_Segment:
    """DTP - Date or Time Period"""
    date_time_qualifier: str
    date_time_period_format_qualifier: str
    date_time_period: str


@dataclass
class REF_Segment:
    """REF - Reference Information"""
    reference_identification_qualifier: str
    reference_identification: str
    description: Optional[str] = None


@dataclass
class Loop1000A:
    """Loop 1000A - Submitter Name"""
    submitter_name: NM1_Segment
    submitter_contact: Optional[List[PER_Segment]] = None


@dataclass
class Loop1000B:
    """Loop 1000B - Receiver Name"""
    receiver_name: NM1_Segment


@dataclass
class Loop2010AA:
    """Loop 2010AA - Billing Provider Name"""
    billing_provider_name: NM1_Segment
    billing_provider_address: Optional[Dict[str, str]] = None
    billing_provider_city_state_zip: Optional[Dict[str, str]] = None
    billing_provider_tax_id: Optional[REF_Segment] = None
    billing_provider_contact: Optional[List[PER_Segment]] = None


@dataclass
class N3_Segment:
    """N3 - Party Location"""
    address_information_1: str
    address_information_2: Optional[str] = None


@dataclass
class N4_Segment:
    """N4 - Geographic Location"""
    city_name: str
    state_or_province_code: Optional[str] = None
    postal_code: Optional[str] = None
    country_code: Optional[str] = None
    location_qualifier: Optional[str] = None
    location_identifier: Optional[str] = None
    country_subdivision_code: Optional[str] = None


@dataclass
class Loop2310A:
    """Loop 2310A - Referring Provider Name"""
    referring_provider_name: NM1_Segment
    referring_provider_secondary_identification: Optional[List[REF_Segment]] = None


@dataclass
class Loop2310B:
    """Loop 2310B - Rendering Provider Name"""
    rendering_provider_name: NM1_Segment
    rendering_provider_secondary_identification: Optional[List[REF_Segment]] = None


@dataclass
class Loop2310C:
    """Loop 2310C - Service Facility Location Name"""
    service_facility_name: NM1_Segment
    service_facility_address: Optional[N3_Segment] = None
    service_facility_city_state_zip: Optional[N4_Segment] = None
    service_facility_secondary_identification: Optional[List[REF_Segment]] = None
    service_facility_contact_information: Optional[PER_Segment] = None


@dataclass
class Loop2310D:
    """Loop 2310D - Supervising Provider Name"""
    supervising_provider_name: NM1_Segment
    supervising_provider_secondary_identification: Optional[List[REF_Segment]] = None


@dataclass
class Loop2310E:
    """Loop 2310E - Ambulance Pick-up Location"""
    ambulance_pickup_location_name: NM1_Segment
    ambulance_pickup_address: Optional[N3_Segment] = None
    ambulance_pickup_city_state_zip: Optional[N4_Segment] = None


@dataclass
class Loop2310F:
    """Loop 2310F - Ambulance Drop-off Location"""
    ambulance_dropoff_location_name: NM1_Segment
    ambulance_dropoff_address: Optional[N3_Segment] = None
    ambulance_dropoff_city_state_zip: Optional[N4_Segment] = None


@dataclass
class Loop2300:
    """Loop 2300 - Claim Information"""
    claim: CLM_Segment
    dates: List[DTP_Segment] = field(default_factory=list)
    claim_supplemental_information: Optional[List[Dict]] = None
    hi_diagnosis_codes: Optional[List[HI_Segment]] = None
    # Loop 2310A - Referring Provider
    referring_provider: Optional[Loop2310A] = None
    # Loop 2310B - Rendering Provider  
    rendering_provider: Optional[Loop2310B] = None
    # Loop 2310C - Service Facility Location
    service_facility_location: Optional[Loop2310C] = None
    # Loop 2310D - Supervising Provider
    supervising_provider: Optional[Loop2310D] = None
    # Loop 2310E - Ambulance Pick-up Location
    ambulance_pickup_location: Optional[Loop2310E] = None
    # Loop 2310F - Ambulance Drop-off Location
    ambulance_dropoff_location: Optional[Loop2310F] = None
    # Loop 2400 - Service Lines
    service_lines: List['Loop2400'] = field(default_factory=list)


@dataclass
class Loop2400:
    """Loop 2400 - Service Line"""
    service_line_number: str
    professional_service: Optional[SV1_Segment] = None
    service_dates: List[DTP_Segment] = field(default_factory=list)
    service_line_supplemental_info: Optional[List[Dict]] = None


@dataclass
class X12_837_Interchange:
    """Complete X12 837 Healthcare Claim Interchange"""
    interchange_header: ISA_InterchangeHeader
    group_header: GS_FunctionalGroupHeader
    transaction_set_header: ST_TransactionSetHeader
    beginning_hierarchical_transaction: Optional[BHT_BeginningHierarchicalTransaction] = None
    submitter: Optional[Loop1000A] = None
    receiver: Optional[Loop1000B] = None
    billing_provider: Optional[Loop2010AA] = None
    claims: List[Loop2300] = field(default_factory=list)


class X12_837_Parser:
    """Parser for X12 837 Healthcare Claims"""
    
    def __init__(self, segment_terminator='~', element_separator='*', sub_element_separator=':'):
        self.segment_terminator = segment_terminator
        self.element_separator = element_separator
        self.sub_element_separator = sub_element_separator
        self.current_interchange = None
        
    def parse_edi_to_object(self, edi_content: str) -> X12_837_Interchange:
        """Parse X12 837 EDI string to Python object"""
        # Clean and normalize the EDI content
        edi_content = edi_content.replace('\r\n', '').replace('\n', '').replace('\r', '')
        
        # Split into segments
        segments = [seg.strip() for seg in edi_content.split(self.segment_terminator) if seg.strip()]
        
        interchange = None
        current_loop_2300 = None
        current_loop_2400 = None
        current_loop_2310 = None  # Track current 2310 loop
        current_loop_2310_type = None  # Track which 2310 loop we're in
        
        for segment in segments:
            elements = segment.split(self.element_separator)
            segment_id = elements[0] if elements else ''
            
            if segment_id == 'ISA':
                interchange = self._parse_isa(elements)
                self.current_interchange = X12_837_Interchange(
                    interchange_header=interchange,
                    group_header=None,
                    transaction_set_header=None
                )
                
            elif segment_id == 'GS':
                self.current_interchange.group_header = self._parse_gs(elements)
                
            elif segment_id == 'ST':
                self.current_interchange.transaction_set_header = self._parse_st(elements)
                
            elif segment_id == 'BHT':
                self.current_interchange.beginning_hierarchical_transaction = self._parse_bht(elements)
                
            elif segment_id == 'NM1':
                nm1 = self._parse_nm1(elements)
                entity_code = elements[1] if len(elements) > 1 else ''
                
                # Check if this is a 2310 loop NM1
                if current_loop_2300 and entity_code in ['DN', 'DQ', '82', '77', 'DK', 'PW', '45']:
                    # Determine which 2310 loop based on entity code
                    if entity_code in ['DN', 'DQ']:  # Referring Provider
                        current_loop_2310 = Loop2310A(referring_provider_name=nm1)
                        current_loop_2300.referring_provider = current_loop_2310
                        current_loop_2310_type = '2310A'
                    elif entity_code == '82':  # Rendering Provider
                        current_loop_2310 = Loop2310B(rendering_provider_name=nm1)
                        current_loop_2300.rendering_provider = current_loop_2310
                        current_loop_2310_type = '2310B'
                    elif entity_code == '77':  # Service Facility Location
                        current_loop_2310 = Loop2310C(service_facility_name=nm1)
                        current_loop_2300.service_facility_location = current_loop_2310
                        current_loop_2310_type = '2310C'
                    elif entity_code == 'DK':  # Supervising Provider
                        current_loop_2310 = Loop2310D(supervising_provider_name=nm1)
                        current_loop_2300.supervising_provider = current_loop_2310
                        current_loop_2310_type = '2310D'
                    elif entity_code == 'PW':  # Ambulance Pick-up Location
                        current_loop_2310 = Loop2310E(ambulance_pickup_location_name=nm1)
                        current_loop_2300.ambulance_pickup_location = current_loop_2310
                        current_loop_2310_type = '2310E'
                    elif entity_code == '45':  # Ambulance Drop-off Location
                        current_loop_2310 = Loop2310F(ambulance_dropoff_location_name=nm1)
                        current_loop_2300.ambulance_dropoff_location = current_loop_2310
                        current_loop_2310_type = '2310F'
                else:
                    # Handle other NM1 segments (1000A, 1000B, 2010AA)
                    self._parse_nm1_into_loops(elements)
                    current_loop_2310 = None
                    current_loop_2310_type = None
                
            elif segment_id == 'N3' and current_loop_2310:
                n3 = self._parse_n3(elements)
                if current_loop_2310_type == '2310C':
                    current_loop_2310.service_facility_address = n3
                elif current_loop_2310_type == '2310E':
                    current_loop_2310.ambulance_pickup_address = n3
                elif current_loop_2310_type == '2310F':
                    current_loop_2310.ambulance_dropoff_address = n3
                    
            elif segment_id == 'N4' and current_loop_2310:
                n4 = self._parse_n4(elements)
                if current_loop_2310_type == '2310C':
                    current_loop_2310.service_facility_city_state_zip = n4
                elif current_loop_2310_type == '2310E':
                    current_loop_2310.ambulance_pickup_city_state_zip = n4
                elif current_loop_2310_type == '2310F':
                    current_loop_2310.ambulance_dropoff_city_state_zip = n4
                    
            elif segment_id == 'REF' and current_loop_2310:
                ref = self._parse_ref(elements)
                # Add REF to secondary identification lists
                if current_loop_2310_type in ['2310A', '2310B', '2310C', '2310D']:
                    if current_loop_2310_type == '2310A':
                        if current_loop_2310.referring_provider_secondary_identification is None:
                            current_loop_2310.referring_provider_secondary_identification = []
                        current_loop_2310.referring_provider_secondary_identification.append(ref)
                    elif current_loop_2310_type == '2310B':
                        if current_loop_2310.rendering_provider_secondary_identification is None:
                            current_loop_2310.rendering_provider_secondary_identification = []
                        current_loop_2310.rendering_provider_secondary_identification.append(ref)
                    elif current_loop_2310_type == '2310C':
                        if current_loop_2310.service_facility_secondary_identification is None:
                            current_loop_2310.service_facility_secondary_identification = []
                        current_loop_2310.service_facility_secondary_identification.append(ref)
                    elif current_loop_2310_type == '2310D':
                        if current_loop_2310.supervising_provider_secondary_identification is None:
                            current_loop_2310.supervising_provider_secondary_identification = []
                        current_loop_2310.supervising_provider_secondary_identification.append(ref)
                        
            elif segment_id == 'PER' and current_loop_2310_type == '2310C':
                current_loop_2310.service_facility_contact_information = self._parse_per(elements)
                
            elif segment_id == 'CLM':
                current_loop_2300 = Loop2300(claim=self._parse_clm(elements))
                if self.current_interchange.claims is None:
                    self.current_interchange.claims = []
                self.current_interchange.claims.append(current_loop_2300)
                current_loop_2310 = None
                current_loop_2310_type = None
                
            elif segment_id == 'HI' and current_loop_2300:
                if current_loop_2300.hi_diagnosis_codes is None:
                    current_loop_2300.hi_diagnosis_codes = []
                current_loop_2300.hi_diagnosis_codes.append(self._parse_hi(elements))
                
            elif segment_id == 'DTP':
                dtp = self._parse_dtp(elements)
                if current_loop_2400:
                    current_loop_2400.service_dates.append(dtp)
                elif current_loop_2300:
                    current_loop_2300.dates.append(dtp)
                    
            elif segment_id == 'LX':
                # Service Line Number - marks beginning of Loop 2400
                if current_loop_2300:
                    current_loop_2400 = Loop2400(service_line_number=elements[1] if len(elements) > 1 else '')
                    current_loop_2300.service_lines.append(current_loop_2400)
                    current_loop_2310 = None
                    current_loop_2310_type = None
                    
            elif segment_id == 'SV1' and current_loop_2400:
                current_loop_2400.professional_service = self._parse_sv1(elements)
                
        return self.current_interchange
    
    def _parse_isa(self, elements: List[str]) -> ISA_InterchangeHeader:
        """Parse ISA segment"""
        return ISA_InterchangeHeader(
            authorization_info_qualifier=elements[1] if len(elements) > 1 else '',
            authorization_info=elements[2] if len(elements) > 2 else '',
            security_info_qualifier=elements[3] if len(elements) > 3 else '',
            security_info=elements[4] if len(elements) > 4 else '',
            interchange_id_qualifier_sender=elements[5] if len(elements) > 5 else '',
            interchange_sender_id=elements[6] if len(elements) > 6 else '',
            interchange_id_qualifier_receiver=elements[7] if len(elements) > 7 else '',
            interchange_receiver_id=elements[8] if len(elements) > 8 else '',
            interchange_date=elements[9] if len(elements) > 9 else '',
            interchange_time=elements[10] if len(elements) > 10 else '',
            repetition_separator=elements[11] if len(elements) > 11 else '',
            interchange_control_version=elements[12] if len(elements) > 12 else '',
            interchange_control_number=elements[13] if len(elements) > 13 else '',
            acknowledgment_requested=elements[14] if len(elements) > 14 else '',
            usage_indicator=elements[15] if len(elements) > 15 else '',
            component_element_separator=elements[16] if len(elements) > 16 else ''
        )
    
    def _parse_gs(self, elements: List[str]) -> GS_FunctionalGroupHeader:
        """Parse GS segment"""
        return GS_FunctionalGroupHeader(
            functional_id_code=elements[1] if len(elements) > 1 else '',
            application_sender_code=elements[2] if len(elements) > 2 else '',
            application_receiver_code=elements[3] if len(elements) > 3 else '',
            date=elements[4] if len(elements) > 4 else '',
            time=elements[5] if len(elements) > 5 else '',
            group_control_number=elements[6] if len(elements) > 6 else '',
            responsible_agency_code=elements[7] if len(elements) > 7 else '',
            version_release_industry_id=elements[8] if len(elements) > 8 else ''
        )
    
    def _parse_st(self, elements: List[str]) -> ST_TransactionSetHeader:
        """Parse ST segment"""
        return ST_TransactionSetHeader(
            transaction_set_id_code=elements[1] if len(elements) > 1 else '',
            transaction_set_control_number=elements[2] if len(elements) > 2 else '',
            implementation_convention_reference=elements[3] if len(elements) > 3 else None
        )
    
    def _parse_bht(self, elements: List[str]) -> BHT_BeginningHierarchicalTransaction:
        """Parse BHT segment"""
        return BHT_BeginningHierarchicalTransaction(
            hierarchical_structure_code=elements[1] if len(elements) > 1 else '',
            transaction_set_purpose_code=elements[2] if len(elements) > 2 else '',
            originator_application_transaction_id=elements[3] if len(elements) > 3 else '',
            transaction_set_creation_date=elements[4] if len(elements) > 4 else '',
            transaction_set_creation_time=elements[5] if len(elements) > 5 else '',
            claim_or_encounter_identifier=elements[6] if len(elements) > 6 else None
        )
    
    def _parse_nm1(self, elements: List[str]) -> NM1_Segment:
        """Parse NM1 segment"""
        return NM1_Segment(
            entity_identifier_code=elements[1] if len(elements) > 1 else '',
            entity_type_qualifier=elements[2] if len(elements) > 2 else '',
            name_last_or_organization_name=elements[3] if len(elements) > 3 else '',
            name_first=elements[4] if len(elements) > 4 else None,
            name_middle=elements[5] if len(elements) > 5 else None,
            name_prefix=elements[6] if len(elements) > 6 else None,
            name_suffix=elements[7] if len(elements) > 7 else None,
            identification_code_qualifier=elements[8] if len(elements) > 8 else None,
            identification_code=elements[9] if len(elements) > 9 else None
        )
    
    def _parse_nm1_into_loops(self, elements: List[str]):
        """Parse NM1 segment and place into appropriate loop"""
        nm1 = self._parse_nm1(elements)
        entity_code = elements[1] if len(elements) > 1 else ''
        
        # Determine which loop based on entity identifier code
        if entity_code == '41':  # Submitter
            if not self.current_interchange.submitter:
                self.current_interchange.submitter = Loop1000A(submitter_name=nm1)
        elif entity_code == '40':  # Receiver
            if not self.current_interchange.receiver:
                self.current_interchange.receiver = Loop1000B(receiver_name=nm1)
        elif entity_code == '85':  # Billing Provider
            if not self.current_interchange.billing_provider:
                self.current_interchange.billing_provider = Loop2010AA(billing_provider_name=nm1)
    
    def _parse_clm(self, elements: List[str]) -> CLM_Segment:
        """Parse CLM segment"""
        return CLM_Segment(
            patient_control_number=elements[1] if len(elements) > 1 else '',
            monetary_amount=elements[2] if len(elements) > 2 else '',
            claim_filing_indicator_code=elements[3] if len(elements) > 3 else None,
            non_institutional_claim_type_code=elements[4] if len(elements) > 4 else None,
            facility_code_value=elements[5].split(self.sub_element_separator)[0] if len(elements) > 5 and elements[5] else None,
            facility_code_qualifier=elements[5].split(self.sub_element_separator)[1] if len(elements) > 5 and self.sub_element_separator in elements[5] else None,
            claim_frequency_type_code=elements[6] if len(elements) > 6 else None,
            yes_no_condition_response_code=elements[7] if len(elements) > 7 else None,
            provider_accept_assignment_code=elements[8] if len(elements) > 8 else None,
            yes_no_condition_response_code_2=elements[9] if len(elements) > 9 else None,
            release_of_information_code=elements[10] if len(elements) > 10 else None,
            patient_signature_source_code=elements[11] if len(elements) > 11 else None
        )
    
    def _parse_hi(self, elements: List[str]) -> HI_Segment:
        """Parse HI segment for diagnosis codes"""
        hi = HI_Segment()
        
        # Parse health care code information (starting from element 1)
        for i in range(1, len(elements)):
            if elements[i] and self.sub_element_separator in elements[i]:
                parts = elements[i].split(self.sub_element_separator)
                code_info = {
                    'code_list_qualifier': parts[0] if len(parts) > 0 else '',
                    'code': parts[1] if len(parts) > 1 else ''
                }
                hi.health_care_code_information.append(code_info)
        
        return hi
    
    def _parse_sv1(self, elements: List[str]) -> SV1_Segment:
        """Parse SV1 segment"""
        # Parse composite medical procedure identifier
        procedure_id = {}
        if len(elements) > 1 and elements[1]:
            parts = elements[1].split(self.sub_element_separator)
            procedure_id = {
                'product_service_id_qualifier': parts[0] if len(parts) > 0 else '',
                'product_service_id': parts[1] if len(parts) > 1 else '',
                'procedure_modifier_1': parts[2] if len(parts) > 2 else None,
                'procedure_modifier_2': parts[3] if len(parts) > 3 else None,
                'procedure_modifier_3': parts[4] if len(parts) > 4 else None,
                'procedure_modifier_4': parts[5] if len(parts) > 5 else None
            }
        
        # Parse diagnosis code pointers
        diagnosis_pointers = None
        if len(elements) > 7 and elements[7]:
            diagnosis_pointers = elements[7].split(self.sub_element_separator)
        
        return SV1_Segment(
            composite_medical_procedure_identifier=procedure_id,
            monetary_amount=elements[2] if len(elements) > 2 else '',
            unit_or_basis_for_measurement_code=elements[3] if len(elements) > 3 else None,
            quantity=elements[4] if len(elements) > 4 else None,
            facility_code_value=elements[5] if len(elements) > 5 else None,
            service_type_code=elements[6] if len(elements) > 6 else None,
            composite_diagnosis_code_pointer=diagnosis_pointers
        )
    
    def _parse_dtp(self, elements: List[str]) -> DTP_Segment:
        """Parse DTP segment"""
        return DTP_Segment(
            date_time_qualifier=elements[1] if len(elements) > 1 else '',
            date_time_period_format_qualifier=elements[2] if len(elements) > 2 else '',
            date_time_period=elements[3] if len(elements) > 3 else ''
        )
    
    def _parse_n3(self, elements: List[str]) -> N3_Segment:
        """Parse N3 segment (Party Location)"""
        return N3_Segment(
            address_information_1=elements[1] if len(elements) > 1 else '',
            address_information_2=elements[2] if len(elements) > 2 else None
        )
    
    def _parse_n4(self, elements: List[str]) -> N4_Segment:
        """Parse N4 segment (Geographic Location)"""
        return N4_Segment(
            city_name=elements[1] if len(elements) > 1 else '',
            state_or_province_code=elements[2] if len(elements) > 2 else None,
            postal_code=elements[3] if len(elements) > 3 else None,
            country_code=elements[4] if len(elements) > 4 else None,
            location_qualifier=elements[5] if len(elements) > 5 else None,
            location_identifier=elements[6] if len(elements) > 6 else None,
            country_subdivision_code=elements[7] if len(elements) > 7 else None
        )
    
    def _parse_ref(self, elements: List[str]) -> REF_Segment:
        """Parse REF segment"""
        return REF_Segment(
            reference_identification_qualifier=elements[1] if len(elements) > 1 else '',
            reference_identification=elements[2] if len(elements) > 2 else '',
            description=elements[3] if len(elements) > 3 else None
        )
    
    def _parse_per(self, elements: List[str]) -> PER_Segment:
        """Parse PER segment (Administrative Communications Contact)"""
        return PER_Segment(
            contact_function_code=elements[1] if len(elements) > 1 else '',
            name=elements[2] if len(elements) > 2 else None,
            communication_number_qualifier_1=elements[3] if len(elements) > 3 else None,
            communication_number_1=elements[4] if len(elements) > 4 else None,
            communication_number_qualifier_2=elements[5] if len(elements) > 5 else None,
            communication_number_2=elements[6] if len(elements) > 6 else None,
            communication_number_qualifier_3=elements[7] if len(elements) > 7 else None,
            communication_number_3=elements[8] if len(elements) > 8 else None
        )
    
    def to_json(self, interchange: X12_837_Interchange, indent: int = 2) -> str:
        """Convert X12 837 interchange object to JSON"""
        return json.dumps(asdict(interchange), indent=indent, default=str)
    
    def to_yaml(self, interchange: X12_837_Interchange) -> str:
        """Convert X12 837 interchange object to YAML"""
        return yaml.dump(asdict(interchange), default_flow_style=False, sort_keys=False)
    
    def from_json(self, json_str: str) -> X12_837_Interchange:
        """Parse JSON string to X12 837 interchange object"""
        data = json.loads(json_str)
        # This would need proper deserialization logic
        # For now, returning None as placeholder
        return None
    
    def to_edi(self, interchange: X12_837_Interchange) -> str:
        """Convert X12 837 interchange object back to EDI format"""
        segments = []
        
        # ISA segment
        isa = interchange.interchange_header
        segments.append(f"ISA{self.element_separator}".join([
            'ISA',
            isa.authorization_info_qualifier,
            isa.authorization_info,
            isa.security_info_qualifier,
            isa.security_info,
            isa.interchange_id_qualifier_sender,
            isa.interchange_sender_id,
            isa.interchange_id_qualifier_receiver,
            isa.interchange_receiver_id,
            isa.interchange_date,
            isa.interchange_time,
            isa.repetition_separator,
            isa.interchange_control_version,
            isa.interchange_control_number,
            isa.acknowledgment_requested,
            isa.usage_indicator,
            isa.component_element_separator
        ]))
        
        # GS segment
        gs = interchange.group_header
        segments.append(self.element_separator.join([
            'GS',
            gs.functional_id_code,
            gs.application_sender_code,
            gs.application_receiver_code,
            gs.date,
            gs.time,
            gs.group_control_number,
            gs.responsible_agency_code,
            gs.version_release_industry_id
        ]))
        
        # ST segment
        st = interchange.transaction_set_header
        st_elements = ['ST', st.transaction_set_id_code, st.transaction_set_control_number]
        if st.implementation_convention_reference:
            st_elements.append(st.implementation_convention_reference)
        segments.append(self.element_separator.join(st_elements))
        
        # Add other segments as needed...
        # This is a simplified version - full implementation would handle all segments
        
        # SE segment (transaction set trailer)
        segments.append(f"SE{self.element_separator}{len(segments)-2}{self.element_separator}{st.transaction_set_control_number}")
        
        # GE segment (group trailer)
        segments.append(f"GE{self.element_separator}1{self.element_separator}{gs.group_control_number}")
        
        # IEA segment (interchange trailer)
        segments.append(f"IEA{self.element_separator}1{self.element_separator}{isa.interchange_control_number}")
        
        return self.segment_terminator.join(segments) + self.segment_terminator


def main():
    """Example usage of the X12 837 parser"""
    parser = X12_837_Parser()
    
    # Example: Parse an EDI file
    sample_edi = """ISA*00*          *00*          *ZZ*SENDER         *ZZ*RECEIVER       *230101*1200*^*00501*000000001*0*P*:~
GS*HC*SENDER*RECEIVER*20230101*1200*1*X*005010X222A1~
ST*837*0001*005010X222A1~
BHT*0019*00*123456789*20230101*1200*CH~
NM1*41*2*SUBMITTER ORGANIZATION*****46*123456789~
NM1*40*2*RECEIVER ORGANIZATION*****46*987654321~
HL*1**20*1~
NM1*85*2*BILLING PROVIDER*****XX*1234567890~
HL*2*1*22*0~
CLM*PATIENT123*500.00***11:B:1*Y*A*Y*Y~
HI*ABK:E119~
LX*1~
SV1*HC:99213*100.00*UN*1~
DTP*472*D8*20230101~
SE*14*0001~
GE*1*1~
IEA*1*000000001~"""
    
    # Parse EDI to object
    interchange = parser.parse_edi_to_object(sample_edi)
    
    # Convert to JSON
    json_output = parser.to_json(interchange)
    print("JSON Output:")
    print(json_output)
    
    # Convert to YAML
    yaml_output = parser.to_yaml(interchange)
    print("\nYAML Output:")
    print(yaml_output)


if __name__ == "__main__":
    main()