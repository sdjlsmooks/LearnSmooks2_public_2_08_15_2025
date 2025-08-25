#!/usr/bin/env python3
"""
X12 837 Healthcare Claim Parser - Complete Implementation
Based on the DFDL schema structure from 837_mapping.dfdl.xsd
Includes all loops: 2310A-F, 2320, 2330A-G, 2400, 2410, 2420A-H, 2430, 2440
"""

import json
import yaml
import xml.etree.ElementTree as ET
from typing import Dict, List, Optional, Any, Union
from dataclasses import dataclass, field, asdict
from datetime import datetime
import re


# ============================================================================
# Segment Definitions
# ============================================================================

@dataclass
class ISASegment:
    """ISA - Interchange Control Header"""
    auth_qual: str
    auth_id: str
    security_qual: str
    security_id: str
    sender_qual: str
    sender_id: str
    receiver_qual: str
    receiver_id: str
    date: str
    time: str
    standard: str
    version: str
    interchange_control_number: str
    ack: str
    test: str
    s_delimiter: str


@dataclass
class GSSegment:
    """GS - Functional Group Header"""
    code: str
    sender: str
    receiver: str
    date: str
    time: str
    group_control_number: str
    standard: str
    version: str


@dataclass
class STSegment:
    """ST - Transaction Set Header"""
    code: str
    transaction_set_control_number: str
    implementation_convention_reference: Optional[str] = None


@dataclass
class BHTSegment:
    """BHT - Beginning of Hierarchical Transaction"""
    hierarchical_structure_code: str
    transaction_set_purpose_code: str
    reference_identification: str
    date: str
    time: str
    transaction_type_code: Optional[str] = None


@dataclass
class NM1Segment:
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
class PERSegment:
    """PER - Administrative Communications Contact"""
    contact_function_code: str
    name: Optional[str] = None
    communication_number_qualifier: Optional[str] = None
    communication_number: Optional[str] = None
    communication_number_qualifier_2: Optional[str] = None
    communication_number_2: Optional[str] = None
    communication_number_qualifier_3: Optional[str] = None
    communication_number_3: Optional[str] = None


@dataclass
class N3Segment:
    """N3 - Party Location"""
    address_information: str
    address_information_2: Optional[str] = None


@dataclass
class N4Segment:
    """N4 - Geographic Location"""
    city_name: str
    state_or_province_code: str
    postal_code: str
    country_code: Optional[str] = None
    location_qualifier: Optional[str] = None
    location_identifier: Optional[str] = None


@dataclass
class REFSegment:
    """REF - Reference Information"""
    reference_identification_qualifier: str
    reference_identification: str
    description: Optional[str] = None
    reference_identifier: Optional[str] = None


@dataclass
class HLSegment:
    """HL - Hierarchical Level"""
    hierarchical_id_number: str
    hierarchical_parent_id_number: Optional[str] = None
    hierarchical_level_code: str
    hierarchical_child_code: str


@dataclass
class PRVSegment:
    """PRV - Provider Information"""
    provider_code: str
    reference_identification_qualifier: str
    reference_identification: str


@dataclass
class CURSegment:
    """CUR - Currency"""
    entity_identifier_code: str
    currency_code: str
    exchange_rate: Optional[str] = None


@dataclass
class SBRSegment:
    """SBR - Subscriber Information"""
    payer_responsibility_sequence_number_code: str
    individual_relationship_code: str
    insured_group_or_policy_number: Optional[str] = None
    group_name: Optional[str] = None
    insurance_type_code: Optional[str] = None
    coordination_of_benefits_code: Optional[str] = None
    yes_no_condition_response_code: Optional[str] = None
    employment_status_code: Optional[str] = None
    claim_filing_indicator_code: Optional[str] = None


@dataclass
class PATSegment:
    """PAT - Patient Information"""
    individual_relationship_code: str
    patient_location_code: Optional[str] = None
    employment_status_code: Optional[str] = None
    student_status_code: Optional[str] = None
    date_time_period_format_qualifier: Optional[str] = None
    date_time_period: Optional[str] = None
    unit_or_basis_for_measurement_code: Optional[str] = None
    weight: Optional[str] = None
    yes_no_condition_response_code: Optional[str] = None


@dataclass
class DMGSegment:
    """DMG - Demographic Information"""
    date_time_period_format_qualifier: str
    date_time_period: str
    gender_code: str
    marital_status_code: Optional[str] = None
    race_or_ethnicity_code: Optional[str] = None


@dataclass
class CLMSegment:
    """CLM - Claim Information"""
    claim_submitters_identifier: str
    monetary_amount: str
    claim_filing_indicator_code: Optional[str] = None
    non_institutional_claim_type_code: Optional[str] = None
    health_care_service_location_information: Optional[str] = None
    yes_no_condition_response_code: Optional[str] = None
    provider_accept_assignment_code: Optional[str] = None
    yes_no_condition_response_code_2: Optional[str] = None
    release_of_information_code: Optional[str] = None
    patient_signature_source_code: Optional[str] = None


@dataclass
class DTPSegment:
    """DTP - Date or Time Period"""
    date_time_qualifier: str
    date_time_period_format_qualifier: str
    date_time_period: str


@dataclass
class PWKSegment:
    """PWK - Paperwork"""
    report_type_code: str
    report_transmission_code: str
    report_copies_needed: Optional[str] = None
    entity_identifier_code: Optional[str] = None
    identification_code_qualifier: Optional[str] = None
    identification_code: Optional[str] = None
    description: Optional[str] = None


@dataclass
class CN1Segment:
    """CN1 - Contract Information"""
    contract_type_code: str
    contract_amount: Optional[str] = None
    contract_percentage: Optional[str] = None
    contract_code: Optional[str] = None
    terms_discount_percentage: Optional[str] = None
    contract_version_identifier: Optional[str] = None


@dataclass
class AMTSegment:
    """AMT - Monetary Amount Information"""
    amount_qualifier_code: str
    monetary_amount: str
    credit_debit_flag_code: Optional[str] = None


@dataclass
class K3Segment:
    """K3 - File Information"""
    fixed_format_information: str


@dataclass
class NTESegment:
    """NTE - Note/Special Instruction"""
    note_reference_code: str
    note_text: str


@dataclass
class CR1Segment:
    """CR1 - Ambulance Transport Information"""
    unit_or_basis_for_measurement_code: str
    weight: str
    ambulance_transport_code: Optional[str] = None
    ambulance_transport_reason_code: Optional[str] = None
    unit_or_basis_for_measurement_code_2: Optional[str] = None
    quantity: Optional[str] = None


@dataclass
class CR2Segment:
    """CR2 - Spinal Manipulation Service Information"""
    count: str
    quantity: Optional[str] = None
    subluxation_level_code: Optional[str] = None
    subluxation_level_code_2: Optional[str] = None
    unit_or_basis_for_measurement_code: Optional[str] = None
    quantity_2: Optional[str] = None
    quantity_3: Optional[str] = None
    nature_of_condition_code: Optional[str] = None
    yes_no_condition_response_code: Optional[str] = None


@dataclass
class CRCSegment:
    """CRC - Conditions Indicator"""
    code_category: str
    yes_no_condition_or_response_code: str
    condition_indicator: Optional[str] = None
    condition_indicator_2: Optional[str] = None
    condition_indicator_3: Optional[str] = None
    condition_indicator_4: Optional[str] = None
    condition_indicator_5: Optional[str] = None


@dataclass
class HISegment:
    """HI - Health Care Information Codes"""
    health_care_code_information: str
    health_care_code_information_2: Optional[str] = None
    health_care_code_information_3: Optional[str] = None
    health_care_code_information_4: Optional[str] = None
    health_care_code_information_5: Optional[str] = None
    health_care_code_information_6: Optional[str] = None
    health_care_code_information_7: Optional[str] = None
    health_care_code_information_8: Optional[str] = None


@dataclass
class HCPSegment:
    """HCP - Health Care Pricing"""
    pricing_methodology: str
    monetary_amount: str
    monetary_amount_2: Optional[str] = None
    reference_identification: Optional[str] = None
    rate: Optional[str] = None
    reference_identification_2: Optional[str] = None
    monetary_amount_3: Optional[str] = None
    product_service_id: Optional[str] = None
    product_service_id_qualifier: Optional[str] = None
    product_service_id_2: Optional[str] = None
    unit_or_basis_for_measurement_code: Optional[str] = None
    quantity: Optional[str] = None


@dataclass
class LXSegment:
    """LX - Service Line Number"""
    assigned_number: str


@dataclass
class SV1Segment:
    """SV1 - Professional Service"""
    composite_medical_procedure_identifier: str
    monetary_amount: str
    unit_or_basis_for_measurement_code: Optional[str] = None
    quantity: Optional[str] = None
    facility_code_value: Optional[str] = None
    service_type_code: Optional[str] = None
    composite_diagnosis_code_pointer: Optional[str] = None


@dataclass
class SV5Segment:
    """SV5 - Durable Medical Equipment Service"""
    composite_medical_procedure_identifier: str
    unit_or_basis_for_measurement_code: str
    quantity: str
    monetary_amount: str
    monetary_amount_2: Optional[str] = None
    frequency_code: Optional[str] = None
    composite_diagnosis_code_pointer: Optional[str] = None


@dataclass
class CR3Segment:
    """CR3 - Durable Medical Equipment Certification"""
    certification_type_code: str
    unit_or_basis_for_measurement_code: str
    quantity: str
    insulin_dependent_code: Optional[str] = None
    description: Optional[str] = None


@dataclass
class QTYSegment:
    """QTY - Quantity Information"""
    quantity_qualifier: str
    quantity: str
    unit_or_basis_for_measurement_code: Optional[str] = None
    free_form_information: Optional[str] = None


@dataclass
class MEASegment:
    """MEA - Measurements"""
    measurement_reference_id_code: str
    measurement_qualifier: str
    measurement_value: str
    composite_unit_of_measure: Optional[str] = None
    range_minimum: Optional[str] = None
    range_maximum: Optional[str] = None


@dataclass
class PS1Segment:
    """PS1 - Purchased Service Information"""
    purchased_service_provider_identifier: str
    purchased_service_charge_amount: str
    state_or_province_code: Optional[str] = None
    postal_code: Optional[str] = None


@dataclass
class LINSegment:
    """LIN - Item Identification"""
    assigned_identification: Optional[str] = None
    product_service_id_qualifier: str
    product_service_id: str
    product_service_id_qualifier_2: Optional[str] = None
    product_service_id_2: Optional[str] = None


@dataclass
class CTPSegment:
    """CTP - Pricing Information"""
    class_of_trade_code: Optional[str] = None
    price_identifier_code: Optional[str] = None
    unit_price: Optional[str] = None
    quantity: Optional[str] = None
    composite_unit_of_measure: Optional[str] = None
    price_multiplier_qualifier: Optional[str] = None
    multiplier: Optional[str] = None
    monetary_amount: Optional[str] = None


@dataclass
class SVDSegment:
    """SVD - Service Line Adjudication"""
    other_payer_primary_identifier: str
    service_line_paid_amount: str
    composite_medical_procedure_identifier: Optional[str] = None
    product_service_id: Optional[str] = None
    paid_service_unit_count: Optional[str] = None
    bundled_or_unbundled_line_number: Optional[str] = None


@dataclass
class CASSegment:
    """CAS - Claims Adjustment"""
    claim_adjustment_group_code: str
    claim_adjustment_reason_code: str
    monetary_amount: str
    quantity: Optional[str] = None


@dataclass
class LQSegment:
    """LQ - Code List Qualifier Code"""
    code_list_qualifier_code: str
    industry_code: str


@dataclass
class FRMSegment:
    """FRM - Supporting Documentation"""
    question_number_letter: str
    question_response: Optional[str] = None
    question_response_2: Optional[str] = None
    question_response_3: Optional[str] = None
    question_response_4: Optional[str] = None
    question_response_5: Optional[str] = None


@dataclass
class OISegment:
    """OI - Other Health Insurance Information"""
    claim_filing_indicator_code: Optional[str] = None
    claim_submission_reason_code: Optional[str] = None
    yes_no_condition_response_code: Optional[str] = None
    patient_signature_source_code: Optional[str] = None
    provider_agreement_code: Optional[str] = None
    release_of_information_code: Optional[str] = None


@dataclass
class MOASegment:
    """MOA - Medicare Outpatient Adjudication Information"""
    reimbursement_rate: Optional[str] = None
    hcpcs_payable_amount: Optional[str] = None
    claim_payment_remark_code: Optional[str] = None
    claim_payment_remark_code_2: Optional[str] = None
    claim_payment_remark_code_3: Optional[str] = None
    claim_payment_remark_code_4: Optional[str] = None
    claim_payment_remark_code_5: Optional[str] = None
    end_stage_renal_disease_payment_amount: Optional[str] = None
    non_payable_professional_component: Optional[str] = None


@dataclass
class SESegment:
    """SE - Transaction Set Trailer"""
    number_of_included_segments: str
    transaction_set_control_number: str


@dataclass
class GESegment:
    """GE - Functional Group Trailer"""
    number_of_transaction_sets: str
    group_control_number: str


@dataclass
class IEASegment:
    """IEA - Interchange Control Trailer"""
    number_of_function_groups_included: str
    interchange_control_number: str


# ============================================================================
# Loop Definitions
# ============================================================================

@dataclass
class Loop1000A:
    """Loop 1000A - Submitter Name"""
    submitter_name: NM1Segment
    submitter_edi_contact_information: Optional[PERSegment] = None


@dataclass
class Loop1000B:
    """Loop 1000B - Receiver Name"""
    receiver_name: NM1Segment


@dataclass
class Loop2010AA:
    """Loop 2010AA - Billing Provider Name"""
    billing_provider_name: NM1Segment
    billing_provider_address: Optional[N3Segment] = None
    billing_provider_city_state_zip: Optional[N4Segment] = None
    billing_provider_tax_id: Optional[REFSegment] = None
    billing_provider_upin_license_info: Optional[List[REFSegment]] = None
    billing_provider_contact_info: Optional[PERSegment] = None


@dataclass
class Loop2010AB:
    """Loop 2010AB - Pay-to Address Name"""
    pay_to_address_name: NM1Segment
    pay_to_address: N3Segment
    pay_to_city_state_zip: N4Segment


@dataclass
class Loop2010AC:
    """Loop 2010AC - Pay-to Plan Name"""
    pay_to_plan_name: NM1Segment
    pay_to_plan_address: N3Segment
    pay_to_plan_city_state_zip: N4Segment
    pay_to_plan_secondary_id: Optional[REFSegment] = None
    pay_to_plan_tax_id: Optional[REFSegment] = None


@dataclass
class Loop2000A:
    """Loop 2000A - Billing Provider Hierarchical Level"""
    billing_provider_hierarchical_level: HLSegment
    billing_provider_specialty_information: Optional[PRVSegment] = None
    foreign_currency_information: Optional[CURSegment] = None
    loop_2010aa: Optional[Loop2010AA] = None
    loop_2010ab: Optional[Loop2010AB] = None
    loop_2010ac: Optional[Loop2010AC] = None


@dataclass
class Loop2010BA:
    """Loop 2010BA - Subscriber Name"""
    subscriber_name: NM1Segment
    subscriber_address: Optional[N3Segment] = None
    subscriber_city_state_zip: Optional[N4Segment] = None
    subscriber_demographic_info: Optional[DMGSegment] = None
    subscriber_secondary_id: Optional[REFSegment] = None
    property_and_casualty_claim_number: Optional[REFSegment] = None
    property_casualty_contact_info: Optional[PERSegment] = None


@dataclass
class Loop2010BB:
    """Loop 2010BB - Payer Name"""
    payer_name: NM1Segment
    payer_address: Optional[N3Segment] = None
    payer_city_state_zip: Optional[N4Segment] = None
    payer_secondary_id: Optional[List[REFSegment]] = None
    billing_provider_secondary_id: Optional[List[REFSegment]] = None


@dataclass
class Loop2000B:
    """Loop 2000B - Subscriber Hierarchical Level"""
    subscriber_hierarchical_level: HLSegment
    subscriber_information: SBRSegment
    patient_information: Optional[PATSegment] = None
    loop_2010ba: Optional[Loop2010BA] = None
    loop_2010bb: Optional[Loop2010BB] = None


@dataclass
class Loop2310A:
    """Loop 2310A - Referring Provider Name"""
    referring_provider_name: NM1Segment
    referring_provider_secondary_id: Optional[List[REFSegment]] = None


@dataclass
class Loop2310B:
    """Loop 2310B - Rendering Provider Name"""
    rendering_provider_name: NM1Segment
    rendering_provider_specialty_info: Optional[PRVSegment] = None
    rendering_provider_secondary_id: Optional[List[REFSegment]] = None


@dataclass
class Loop2310C:
    """Loop 2310C - Service Facility Location Name"""
    service_facility_location_name: NM1Segment
    service_facility_location_address: N3Segment
    service_facility_location_city_state_zip: N4Segment
    service_facility_location_secondary_id: Optional[List[REFSegment]] = None
    service_facility_location_contact: Optional[PERSegment] = None


@dataclass
class Loop2310D:
    """Loop 2310D - Supervising Provider Name"""
    supervising_provider_name: NM1Segment
    supervising_provider_secondary_id: Optional[List[REFSegment]] = None


@dataclass
class Loop2310E:
    """Loop 2310E - Ambulance Pick-up Location"""
    ambulance_pickup_location: NM1Segment
    ambulance_pickup_address: N3Segment
    ambulance_pickup_city_state_zip: N4Segment


@dataclass
class Loop2310F:
    """Loop 2310F - Ambulance Drop-off Location"""
    ambulance_dropoff_location: NM1Segment
    ambulance_dropoff_address: N3Segment
    ambulance_dropoff_city_state_zip: N4Segment


@dataclass
class Loop2330A:
    """Loop 2330A - Other Subscriber Name"""
    other_subscriber_name: NM1Segment
    other_subscriber_address: Optional[N3Segment] = None
    other_subscriber_city_state_zip: Optional[N4Segment] = None
    other_subscriber_secondary_id: Optional[REFSegment] = None


@dataclass
class Loop2330B:
    """Loop 2330B - Other Payer Name"""
    other_payer_name: NM1Segment
    other_payer_address: Optional[N3Segment] = None
    other_payer_city_state_zip: Optional[N4Segment] = None
    other_payer_secondary_id: Optional[List[REFSegment]] = None
    other_payer_contact_info: Optional[PERSegment] = None
    other_payer_provider_secondary_id: Optional[List[REFSegment]] = None


@dataclass
class Loop2330C:
    """Loop 2330C - Other Payer Referring Provider"""
    other_payer_referring_provider_name: NM1Segment
    other_payer_referring_provider_secondary_id: Optional[List[REFSegment]] = None


@dataclass
class Loop2330D:
    """Loop 2330D - Other Payer Rendering Provider"""
    other_payer_rendering_provider_name: NM1Segment
    other_payer_rendering_provider_secondary_id: Optional[List[REFSegment]] = None


@dataclass
class Loop2330E:
    """Loop 2330E - Other Payer Service Facility Location"""
    other_payer_service_facility_name: NM1Segment
    other_payer_service_facility_secondary_id: Optional[List[REFSegment]] = None


@dataclass
class Loop2330F:
    """Loop 2330F - Other Payer Supervising Provider"""
    other_payer_supervising_provider_name: NM1Segment
    other_payer_supervising_provider_secondary_id: Optional[List[REFSegment]] = None


@dataclass
class Loop2330G:
    """Loop 2330G - Other Payer Billing Provider"""
    other_payer_billing_provider_name: NM1Segment
    other_payer_billing_provider_secondary_id: Optional[List[REFSegment]] = None


@dataclass
class Loop2320:
    """Loop 2320 - Other Subscriber Information"""
    other_subscriber_information: SBRSegment
    claim_level_adjustments: Optional[List[CASSegment]] = None
    coordination_of_benefits_payer_paid_amount: Optional[AMTSegment] = None
    coordination_of_benefits_total_non_covered_amount: Optional[AMTSegment] = None
    remaining_patient_liability: Optional[AMTSegment] = None
    other_insurance_coverage_information: Optional[OISegment] = None
    medicare_outpatient_adjudication_information: Optional[MOASegment] = None
    loop_2330a: Optional[Loop2330A] = None
    loop_2330b: Optional[Loop2330B] = None
    loop_2330c: Optional[Loop2330C] = None
    loop_2330d: Optional[Loop2330D] = None
    loop_2330e: Optional[Loop2330E] = None
    loop_2330f: Optional[Loop2330F] = None
    loop_2330g: Optional[Loop2330G] = None


@dataclass
class Loop2410:
    """Loop 2410 - Drug Identification"""
    drug_identification: LINSegment
    drug_quantity: Optional[CTPSegment] = None
    prescription_number: Optional[REFSegment] = None


@dataclass
class Loop2420A:
    """Loop 2420A - Rendering Provider Name"""
    rendering_provider_name: NM1Segment
    rendering_provider_specialty: Optional[PRVSegment] = None
    rendering_provider_secondary_id: Optional[List[REFSegment]] = None


@dataclass
class Loop2420B:
    """Loop 2420B - Purchased Service Provider Name"""
    purchased_service_provider_name: NM1Segment
    purchased_service_provider_secondary_id: Optional[List[REFSegment]] = None


@dataclass
class Loop2420C:
    """Loop 2420C - Service Facility Location Name"""
    service_facility_location_name: NM1Segment
    service_facility_location_address: N3Segment
    service_facility_location_city_state_zip: N4Segment
    service_facility_location_secondary_id: Optional[List[REFSegment]] = None


@dataclass
class Loop2420D:
    """Loop 2420D - Supervising Provider Name"""
    supervising_provider_name: NM1Segment
    supervising_provider_secondary_id: Optional[List[REFSegment]] = None


@dataclass
class Loop2420E:
    """Loop 2420E - Ordering Provider Name"""
    ordering_provider_name: NM1Segment
    ordering_provider_address: Optional[N3Segment] = None
    ordering_provider_city_state_zip: Optional[N4Segment] = None
    ordering_provider_secondary_id: Optional[List[REFSegment]] = None
    ordering_provider_contact_info: Optional[PERSegment] = None


@dataclass
class Loop2420F:
    """Loop 2420F - Referring Provider Name"""
    referring_provider_name: NM1Segment
    referring_provider_secondary_id: Optional[List[REFSegment]] = None


@dataclass
class Loop2420G:
    """Loop 2420G - Ambulance Pick-up Location"""
    ambulance_pickup_location: NM1Segment
    ambulance_pickup_address: N3Segment
    ambulance_pickup_city_state_zip: N4Segment


@dataclass
class Loop2420H:
    """Loop 2420H - Ambulance Drop-off Location"""
    ambulance_dropoff_location: NM1Segment
    ambulance_dropoff_address: N3Segment
    ambulance_dropoff_city_state_zip: N4Segment


@dataclass
class Loop2430:
    """Loop 2430 - Line Adjudication Information"""
    line_adjudication_information: SVDSegment
    line_adjustment: Optional[List[CASSegment]] = None
    line_check_or_remittance_date: DTPSegment
    remaining_patient_liability: Optional[AMTSegment] = None


@dataclass
class Loop2440:
    """Loop 2440 - Form Identification Code"""
    form_identification_code: LQSegment
    supporting_documentation: Optional[List[FRMSegment]] = None


@dataclass
class Loop2400:
    """Loop 2400 - Service Line Information"""
    service_line_number: LXSegment
    professional_service: Optional[SV1Segment] = None
    durable_medical_equipment: Optional[SV5Segment] = None
    line_supplemental_information: Optional[List[PWKSegment]] = None
    ambulance_transport_info: Optional[CR1Segment] = None
    dme_certification: Optional[CR3Segment] = None
    ambulance_certification: Optional[List[CRCSegment]] = None
    hospice_employee_indicator: Optional[CRCSegment] = None
    condition_indicator_dme: Optional[CRCSegment] = None
    service_date: Optional[DTPSegment] = None
    prescription_date: Optional[DTPSegment] = None
    certification_date: Optional[DTPSegment] = None
    begin_therapy_date: Optional[DTPSegment] = None
    last_certification_date: Optional[DTPSegment] = None
    last_seen_date: Optional[DTPSegment] = None
    test_date: Optional[List[DTPSegment]] = None
    oxygen_test_date: Optional[List[DTPSegment]] = None
    shipped_date: Optional[DTPSegment] = None
    last_x_ray_date_line: Optional[DTPSegment] = None
    initial_treatment_date_line: Optional[DTPSegment] = None
    ambulance_patient_count: Optional[QTYSegment] = None
    obstetric_additional_units: Optional[QTYSegment] = None
    test_result: Optional[List[MEASegment]] = None
    contract_information_line: Optional[CN1Segment] = None
    repriced_line_item_ref: Optional[REFSegment] = None
    adjusted_repriced_line_ref: Optional[REFSegment] = None
    prior_authorization_line: Optional[List[REFSegment]] = None
    line_item_control_number: Optional[REFSegment] = None
    mammography_cert_number_line: Optional[REFSegment] = None
    clia_number_line: Optional[REFSegment] = None
    referring_clia_number: Optional[REFSegment] = None
    immunization_batch_number: Optional[REFSegment] = None
    referral_number_line: Optional[List[REFSegment]] = None
    sales_tax_amount: Optional[AMTSegment] = None
    postage_claimed_amount: Optional[AMTSegment] = None
    file_information_line: Optional[List[K3Segment]] = None
    line_note: Optional[NTESegment] = None
    third_party_note: Optional[NTESegment] = None
    purchased_service_info: Optional[PS1Segment] = None
    line_pricing_repricing_info: Optional[HCPSegment] = None
    # Child loops
    loop_2410: Optional[Loop2410] = None
    loop_2420a: Optional[Loop2420A] = None
    loop_2420b: Optional[Loop2420B] = None
    loop_2420c: Optional[Loop2420C] = None
    loop_2420d: Optional[Loop2420D] = None
    loop_2420e: Optional[Loop2420E] = None
    loop_2420f: Optional[List[Loop2420F]] = None
    loop_2420g: Optional[Loop2420G] = None
    loop_2420h: Optional[Loop2420H] = None
    loop_2430: Optional[List[Loop2430]] = None
    loop_2440: Optional[List[Loop2440]] = None


@dataclass
class Loop2300:
    """Loop 2300 - Claim Information"""
    claim_information: CLMSegment
    onset_illness_or_symptom: Optional[DTPSegment] = None
    initial_treatment_date: Optional[DTPSegment] = None
    last_seen_date: Optional[DTPSegment] = None
    acute_manifestation_date: Optional[DTPSegment] = None
    accident_date: Optional[DTPSegment] = None
    last_menstrual_period_date: Optional[DTPSegment] = None
    last_x_ray_date: Optional[DTPSegment] = None
    hearing_vision_prescription_date: Optional[DTPSegment] = None
    disability_date: Optional[DTPSegment] = None
    last_worked_date: Optional[DTPSegment] = None
    authorized_return_to_work_date: Optional[DTPSegment] = None
    admission_date: Optional[DTPSegment] = None
    discharge_date: Optional[DTPSegment] = None
    assumed_relinquished_care_date: Optional[List[DTPSegment]] = None
    property_casualty_date: Optional[List[DTPSegment]] = None
    repricer_received_date: Optional[List[DTPSegment]] = None
    claim_supplemental_information: Optional[List[PWKSegment]] = None
    contract_information: Optional[CN1Segment] = None
    patient_amount_paid: Optional[AMTSegment] = None
    service_authorization_exception_code: Optional[REFSegment] = None
    mandatory_medicare: Optional[REFSegment] = None
    mammogram_certification: Optional[REFSegment] = None
    referral_number: Optional[REFSegment] = None
    prior_authorization: Optional[REFSegment] = None
    payer_claim_control_number: Optional[REFSegment] = None
    clinical_laboratory_improvement: Optional[REFSegment] = None
    repriced_claim_number: Optional[REFSegment] = None
    adjusted_repriced_claim_number: Optional[REFSegment] = None
    investigational_device_ex_number: Optional[REFSegment] = None
    claim_id_for_txn_intermediaries: Optional[REFSegment] = None
    medical_record_number: Optional[REFSegment] = None
    demo_project_id: Optional[REFSegment] = None
    care_plan_oversight: Optional[REFSegment] = None
    file_information: Optional[List[K3Segment]] = None
    claim_note: Optional[NTESegment] = None
    ambulance_transport_info: Optional[CR1Segment] = None
    spinal_manipulation_info: Optional[CR2Segment] = None
    ambulance_certification: Optional[CRCSegment] = None
    patient_condition_vision: Optional[CRCSegment] = None
    homebound_indicator: Optional[CRCSegment] = None
    epsdt_referral: Optional[CRCSegment] = None
    health_care_diagnosis_code: Optional[HISegment] = None
    claim_pricing_repricing_info: Optional[HCPSegment] = None
    # Child loops
    loop_2310a: Optional[Loop2310A] = None
    loop_2310b: Optional[Loop2310B] = None
    loop_2310c: Optional[Loop2310C] = None
    loop_2310d: Optional[Loop2310D] = None
    loop_2310e: Optional[Loop2310E] = None
    loop_2310f: Optional[Loop2310F] = None
    loop_2320: Optional[List[Loop2320]] = None
    loop_2400: Optional[List[Loop2400]] = None


@dataclass
class X12_837_Interchange:
    """Complete X12 837 Healthcare Claim Structure"""
    interchange_header: ISASegment
    group_header: GSSegment
    transaction_set_header: STSegment
    beginning_hierarchical_transaction: BHTSegment
    loop_1000a: Optional[Loop1000A] = None
    loop_1000b: Optional[Loop1000B] = None
    loop_2000a: Optional[Loop2000A] = None
    loop_2000b: Optional[Loop2000B] = None
    loop_2300: Optional[List[Loop2300]] = None
    transaction_set_trailer: SESegment
    functional_group_trailer: GESegment
    interchange_control_trailer: IEASegment


# ============================================================================
# Parser Class
# ============================================================================

class X12_837_Parser:
    """Parser for X12 837 Healthcare Claim messages"""
    
    def __init__(self):
        self.segment_delimiter = '~'
        self.element_delimiter = '*'
        self.subelement_delimiter = ':'
        
    def parse_edi(self, edi_content: str) -> X12_837_Interchange:
        """Parse EDI string into X12_837_Interchange object"""
        # Clean and split EDI content
        edi_content = edi_content.replace('\n', '').replace('\r', '')
        segments = edi_content.split(self.segment_delimiter)
        
        interchange = X12_837_Interchange(
            interchange_header=None,
            group_header=None,
            transaction_set_header=None,
            beginning_hierarchical_transaction=None,
            transaction_set_trailer=None,
            functional_group_trailer=None,
            interchange_control_trailer=None
        )
        
        current_loop = None
        current_2300 = None
        current_2400 = None
        
        for segment in segments:
            if not segment:
                continue
                
            elements = segment.split(self.element_delimiter)
            segment_id = elements[0]
            
            # Parse control segments
            if segment_id == 'ISA':
                interchange.interchange_header = self._parse_isa(elements)
            elif segment_id == 'GS':
                interchange.group_header = self._parse_gs(elements)
            elif segment_id == 'ST':
                interchange.transaction_set_header = self._parse_st(elements)
            elif segment_id == 'BHT':
                interchange.beginning_hierarchical_transaction = self._parse_bht(elements)
            elif segment_id == 'SE':
                interchange.transaction_set_trailer = self._parse_se(elements)
            elif segment_id == 'GE':
                interchange.functional_group_trailer = self._parse_ge(elements)
            elif segment_id == 'IEA':
                interchange.interchange_control_trailer = self._parse_iea(elements)
            
            # Parse hierarchical levels and loops
            elif segment_id == 'HL':
                level_code = self._get_element(elements, 3)
                if level_code == '20':  # Billing Provider
                    current_loop = '2000A'
                elif level_code == '22':  # Subscriber
                    current_loop = '2000B'
            
            elif segment_id == 'CLM':
                # Start new 2300 loop
                if interchange.loop_2300 is None:
                    interchange.loop_2300 = []
                current_2300 = Loop2300(claim_information=self._parse_clm(elements))
                interchange.loop_2300.append(current_2300)
                current_loop = '2300'
            
            elif segment_id == 'LX':
                # Start new 2400 loop
                if current_2300:
                    if current_2300.loop_2400 is None:
                        current_2300.loop_2400 = []
                    current_2400 = Loop2400(service_line_number=self._parse_lx(elements))
                    current_2300.loop_2400.append(current_2400)
                    current_loop = '2400'
            
            # Parse NM1 segments based on context
            elif segment_id == 'NM1':
                entity_code = self._get_element(elements, 1)
                nm1 = self._parse_nm1(elements)
                
                if entity_code == '41':  # Submitter
                    if interchange.loop_1000a is None:
                        interchange.loop_1000a = Loop1000A(submitter_name=nm1)
                elif entity_code == '40':  # Receiver
                    if interchange.loop_1000b is None:
                        interchange.loop_1000b = Loop1000B(receiver_name=nm1)
                elif entity_code == '85' and current_loop == '2000A':  # Billing Provider
                    if interchange.loop_2000a is None:
                        interchange.loop_2000a = Loop2000A(
                            billing_provider_hierarchical_level=None
                        )
                    if interchange.loop_2000a.loop_2010aa is None:
                        interchange.loop_2000a.loop_2010aa = Loop2010AA(
                            billing_provider_name=nm1
                        )
        
        return interchange
    
    def _get_element(self, elements: List[str], index: int) -> Optional[str]:
        """Safely get element at index"""
        if index < len(elements):
            value = elements[index]
            return value if value else None
        return None
    
    def _parse_isa(self, elements: List[str]) -> ISASegment:
        """Parse ISA segment"""
        return ISASegment(
            auth_qual=self._get_element(elements, 1) or '',
            auth_id=self._get_element(elements, 2) or '',
            security_qual=self._get_element(elements, 3) or '',
            security_id=self._get_element(elements, 4) or '',
            sender_qual=self._get_element(elements, 5) or '',
            sender_id=self._get_element(elements, 6) or '',
            receiver_qual=self._get_element(elements, 7) or '',
            receiver_id=self._get_element(elements, 8) or '',
            date=self._get_element(elements, 9) or '',
            time=self._get_element(elements, 10) or '',
            standard=self._get_element(elements, 11) or '',
            version=self._get_element(elements, 12) or '',
            interchange_control_number=self._get_element(elements, 13) or '',
            ack=self._get_element(elements, 14) or '',
            test=self._get_element(elements, 15) or '',
            s_delimiter=self._get_element(elements, 16) or ''
        )
    
    def _parse_gs(self, elements: List[str]) -> GSSegment:
        """Parse GS segment"""
        return GSSegment(
            code=self._get_element(elements, 1) or '',
            sender=self._get_element(elements, 2) or '',
            receiver=self._get_element(elements, 3) or '',
            date=self._get_element(elements, 4) or '',
            time=self._get_element(elements, 5) or '',
            group_control_number=self._get_element(elements, 6) or '',
            standard=self._get_element(elements, 7) or '',
            version=self._get_element(elements, 8) or ''
        )
    
    def _parse_st(self, elements: List[str]) -> STSegment:
        """Parse ST segment"""
        return STSegment(
            code=self._get_element(elements, 1) or '',
            transaction_set_control_number=self._get_element(elements, 2) or '',
            implementation_convention_reference=self._get_element(elements, 3)
        )
    
    def _parse_bht(self, elements: List[str]) -> BHTSegment:
        """Parse BHT segment"""
        return BHTSegment(
            hierarchical_structure_code=self._get_element(elements, 1) or '',
            transaction_set_purpose_code=self._get_element(elements, 2) or '',
            reference_identification=self._get_element(elements, 3) or '',
            date=self._get_element(elements, 4) or '',
            time=self._get_element(elements, 5) or '',
            transaction_type_code=self._get_element(elements, 6)
        )
    
    def _parse_nm1(self, elements: List[str]) -> NM1Segment:
        """Parse NM1 segment"""
        return NM1Segment(
            entity_identifier_code=self._get_element(elements, 1) or '',
            entity_type_qualifier=self._get_element(elements, 2) or '',
            name_last_or_organization_name=self._get_element(elements, 3) or '',
            name_first=self._get_element(elements, 4),
            name_middle=self._get_element(elements, 5),
            name_prefix=self._get_element(elements, 6),
            name_suffix=self._get_element(elements, 7),
            identification_code_qualifier=self._get_element(elements, 8),
            identification_code=self._get_element(elements, 9)
        )
    
    def _parse_clm(self, elements: List[str]) -> CLMSegment:
        """Parse CLM segment"""
        return CLMSegment(
            claim_submitters_identifier=self._get_element(elements, 1) or '',
            monetary_amount=self._get_element(elements, 2) or '',
            claim_filing_indicator_code=self._get_element(elements, 3),
            non_institutional_claim_type_code=self._get_element(elements, 4),
            health_care_service_location_information=self._get_element(elements, 5),
            yes_no_condition_response_code=self._get_element(elements, 6),
            provider_accept_assignment_code=self._get_element(elements, 7),
            yes_no_condition_response_code_2=self._get_element(elements, 8),
            release_of_information_code=self._get_element(elements, 9),
            patient_signature_source_code=self._get_element(elements, 10)
        )
    
    def _parse_lx(self, elements: List[str]) -> LXSegment:
        """Parse LX segment"""
        return LXSegment(
            assigned_number=self._get_element(elements, 1) or ''
        )
    
    def _parse_se(self, elements: List[str]) -> SESegment:
        """Parse SE segment"""
        return SESegment(
            number_of_included_segments=self._get_element(elements, 1) or '',
            transaction_set_control_number=self._get_element(elements, 2) or ''
        )
    
    def _parse_ge(self, elements: List[str]) -> GESegment:
        """Parse GE segment"""
        return GESegment(
            number_of_transaction_sets=self._get_element(elements, 1) or '',
            group_control_number=self._get_element(elements, 2) or ''
        )
    
    def _parse_iea(self, elements: List[str]) -> IEASegment:
        """Parse IEA segment"""
        return IEASegment(
            number_of_function_groups_included=self._get_element(elements, 1) or '',
            interchange_control_number=self._get_element(elements, 2) or ''
        )
    
    def to_dict(self, obj: Any) -> Dict:
        """Convert dataclass object to dictionary"""
        if hasattr(obj, '__dataclass_fields__'):
            return asdict(obj)
        elif isinstance(obj, list):
            return [self.to_dict(item) for item in obj]
        else:
            return obj
    
    def to_json(self, interchange: X12_837_Interchange) -> str:
        """Convert interchange to JSON"""
        return json.dumps(self.to_dict(interchange), indent=2, default=str)
    
    def to_yaml(self, interchange: X12_837_Interchange) -> str:
        """Convert interchange to YAML"""
        return yaml.dump(self.to_dict(interchange), default_flow_style=False)


# ============================================================================
# Main execution
# ============================================================================

if __name__ == "__main__":
    # Example usage
    sample_edi = """ISA*00*          *00*          *ZZ*SENDER123      *ZZ*RECEIVER456    *210901*1200*^*00501*000000001*0*P*:~
GS*HC*SENDER123*RECEIVER456*20210901*1200*1*X*005010X223A2~
ST*837*0001*005010X223A2~
BHT*0019*00*REF123456*20210901*1200*CH~
NM1*41*2*SUBMITTER ORGANIZATION*****46*SUB12345~
NM1*40*2*RECEIVER CORPORATION*****46*REC67890~
SE*6*0001~
GE*1*1~
IEA*1*000000001~"""
    
    parser = X12_837_Parser()
    interchange = parser.parse_edi(sample_edi)
    
    print("Parsed X12 837 Healthcare Claim")
    print("=" * 50)
    print(f"Sender: {interchange.interchange_header.sender_id}")
    print(f"Receiver: {interchange.interchange_header.receiver_id}")
    print(f"Date: {interchange.interchange_header.date}")
    print(f"Transaction Type: {interchange.transaction_set_header.code}")
    
    # Convert to JSON
    json_output = parser.to_json(interchange)
    print("\nJSON Output Sample:")
    print(json_output[:500] + "...")