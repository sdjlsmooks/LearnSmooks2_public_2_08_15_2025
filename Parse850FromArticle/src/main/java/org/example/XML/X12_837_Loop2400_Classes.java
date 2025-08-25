package org.example.XML;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

import java.util.List;

/**
 * Loop 2400 - Service Line Information classes for X12 837 Healthcare Claim.
 * This loop contains service line level details for professional claims.
 */
public class X12_837_Loop2400_Classes {

    @Data
    public static class Loop2400ServiceLineInformation {
        @JsonProperty("service-line-number")
        private LXSegment serviceLineNumber;
        
        @JsonProperty("professional-service")
        private SV1Segment professionalService;
        
        @JsonProperty("durable-medical-equipment")
        private SV5Segment durableMedicalEquipment;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("line-supplemental-information")
        private List<X12_837_Interchange.PWKSegment> lineSupplementalInformation;
        
        @JsonProperty("ambulance-transport-info")
        private X12_837_Interchange.CR1Segment ambulanceTransportInfo;
        
        @JsonProperty("dme-certification")
        private CR3Segment dmeCertification;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("ambulance-certification")
        private List<X12_837_Interchange.CRCSegment> ambulanceCertification;
        
        @JsonProperty("hospice-employee-indicator")
        private X12_837_Interchange.CRCSegment hospiceEmployeeIndicator;
        
        @JsonProperty("condition-indicator-dme")
        private X12_837_Interchange.CRCSegment conditionIndicatorDme;
        
        @JsonProperty("service-date")
        private X12_837_Interchange.DTPSegment serviceDate;
        
        @JsonProperty("prescription-date")
        private X12_837_Interchange.DTPSegment prescriptionDate;
        
        @JsonProperty("certification-date")
        private X12_837_Interchange.DTPSegment certificationDate;
        
        @JsonProperty("begin-therapy-date")
        private X12_837_Interchange.DTPSegment beginTherapyDate;
        
        @JsonProperty("last-certification-date")
        private X12_837_Interchange.DTPSegment lastCertificationDate;
        
        @JsonProperty("last-seen-date")
        private X12_837_Interchange.DTPSegment lastSeenDate;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("test-date")
        private List<X12_837_Interchange.DTPSegment> testDate;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("oxygen-test-date")
        private List<X12_837_Interchange.DTPSegment> oxygenTestDate;
        
        @JsonProperty("shipped-date")
        private X12_837_Interchange.DTPSegment shippedDate;
        
        @JsonProperty("last-x-ray-date-line")
        private X12_837_Interchange.DTPSegment lastXRayDateLine;
        
        @JsonProperty("initial-treatment-date-line")
        private X12_837_Interchange.DTPSegment initialTreatmentDateLine;
        
        @JsonProperty("ambulance-patient-count")
        private QTYSegment ambulancePatientCount;
        
        @JsonProperty("obstetric-additional-units")
        private QTYSegment obstetricAdditionalUnits;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("test-result")
        private List<MEASegment> testResult;
        
        @JsonProperty("contract-information-line")
        private X12_837_Interchange.CN1Segment contractInformationLine;
        
        @JsonProperty("repriced-line-item-ref")
        private X12_837_Interchange.REFSegment repricedLineItemRef;
        
        @JsonProperty("adjusted-repriced-line-ref")
        private X12_837_Interchange.REFSegment adjustedRepricedLineRef;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("prior-authorization-line")
        private List<X12_837_Interchange.REFSegment> priorAuthorizationLine;
        
        @JsonProperty("line-item-control-number")
        private X12_837_Interchange.REFSegment lineItemControlNumber;
        
        @JsonProperty("mammography-cert-number-line")
        private X12_837_Interchange.REFSegment mammographyCertNumberLine;
        
        @JsonProperty("clia-number-line")
        private X12_837_Interchange.REFSegment cliaNumberLine;
        
        @JsonProperty("referring-clia-number")
        private X12_837_Interchange.REFSegment referringCliaNumber;
        
        @JsonProperty("immunization-batch-number")
        private X12_837_Interchange.REFSegment immunizationBatchNumber;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("referral-number-line")
        private List<X12_837_Interchange.REFSegment> referralNumberLine;
        
        @JsonProperty("sales-tax-amount")
        private X12_837_Interchange.AMTSegment salesTaxAmount;
        
        @JsonProperty("postage-claimed-amount")
        private X12_837_Interchange.AMTSegment postageClaimedAmount;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("file-information-line")
        private List<X12_837_Interchange.K3Segment> fileInformationLine;
        
        @JsonProperty("line-note")
        private X12_837_Interchange.NTESegment lineNote;
        
        @JsonProperty("third-party-note")
        private X12_837_Interchange.NTESegment thirdPartyNote;
        
        @JsonProperty("purchased-service-info")
        private PS1Segment purchasedServiceInfo;
        
        @JsonProperty("line-pricing-repricing-info")
        private X12_837_Interchange.HCPSegment linePricingRepricingInfo;
        
        // Loop 2410 - Drug Identification
        @JsonProperty("Loop_2410_DrugIdentification")
        private X12_837_Loop2410_2420_Classes.Loop2410DrugIdentification loop2410DrugIdentification;
        
        // Loop 2420A - Rendering Provider Name
        @JsonProperty("Loop_2420A_RenderingProviderName")
        private X12_837_Loop2410_2420_Classes.Loop2420ARenderingProviderName loop2420ARenderingProviderName;
        
        // Loop 2420B - Purchased Service Provider Name
        @JsonProperty("Loop_2420B_PurchasedServiceProviderName")
        private X12_837_Loop2410_2420_Classes.Loop2420BPurchasedServiceProviderName loop2420BPurchasedServiceProviderName;
        
        // Loop 2420C - Service Facility Location Name
        @JsonProperty("Loop_2420C_ServiceFacilityLocationName")
        private X12_837_Loop2410_2420_Classes.Loop2420CServiceFacilityLocationName loop2420CServiceFacilityLocationName;
        
        // Loop 2420D - Supervising Provider Name
        @JsonProperty("Loop_2420D_SupervisingProviderName")
        private X12_837_Loop2410_2420_Classes.Loop2420DSupervisingProviderName loop2420DSupervisingProviderName;
        
        // Loop 2420E - Ordering Provider Name
        @JsonProperty("Loop_2420E_OrderingProviderName")
        private X12_837_Loop2410_2420_Classes.Loop2420EOrderingProviderName loop2420EOrderingProviderName;
        
        // Loop 2420F - Referring Provider Name (can occur up to 2 times)
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("Loop_2420F_ReferringProviderName")
        private List<X12_837_Loop2410_2420_Classes.Loop2420FReferringProviderName> loop2420FReferringProviderName;
        
        // Loop 2420G - Ambulance Pick-up Location
        @JsonProperty("Loop_2420G_AmbulancePickupLocation")
        private X12_837_Loop2410_2420_Classes.Loop2420GAmbulancePickupLocation loop2420GAmbulancePickupLocation;
        
        // Loop 2420H - Ambulance Drop-off Location
        @JsonProperty("Loop_2420H_AmbulanceDropoffLocation")
        private X12_837_Loop2410_2420_Classes.Loop2420HAmbulanceDropoffLocation loop2420HAmbulanceDropoffLocation;
        
        // Loop 2430 - Line Adjudication Information (can occur up to 15 times)
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("Loop_2430_LineAdjudicationInformation")
        private List<X12_837_Loop2430_2440_Classes.Loop2430LineAdjudicationInformation> loop2430LineAdjudicationInformation;
        
        // Loop 2440 - Form Identification Code (unbounded)
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("Loop_2440_FormIdentificationCode")
        private List<X12_837_Loop2430_2440_Classes.Loop2440FormIdentificationCode> loop2440FormIdentificationCode;
    }
    
    @Data
    public static class LXSegment {
        @JsonProperty("assigned-number")
        private String assignedNumber;
    }
    
    @Data
    public static class SV1Segment {
        @JsonProperty("composite-medical-procedure-identifier")
        private String compositeMedicalProcedureIdentifier;
        
        @JsonProperty("monetary-amount")
        private String monetaryAmount;
        
        @JsonProperty("unit-or-basis-for-measurement-code")
        private String unitOrBasisForMeasurementCode;
        
        @JsonProperty("quantity")
        private String quantity;
        
        @JsonProperty("facility-code-value")
        private String facilityCodeValue;
        
        @JsonProperty("service-type-code")
        private String serviceTypeCode;
        
        @JsonProperty("composite-diagnosis-code-pointer")
        private String compositeDiagnosisCodePointer;
        
        @JsonProperty("monetary-amount-2")
        private String monetaryAmount2;
        
        @JsonProperty("yes-no-condition-response-code")
        private String yesNoConditionResponseCode;
        
        @JsonProperty("multiple-procedure-code")
        private String multipleProcedureCode;
        
        @JsonProperty("yes-no-condition-response-code-2")
        private String yesNoConditionResponseCode2;
        
        @JsonProperty("yes-no-condition-response-code-3")
        private String yesNoConditionResponseCode3;
        
        @JsonProperty("review-code")
        private String reviewCode;
        
        @JsonProperty("national-or-local-assigned-review-value")
        private String nationalOrLocalAssignedReviewValue;
        
        @JsonProperty("copay-status-code")
        private String copayStatusCode;
        
        @JsonProperty("health-care-professional-shortage-area-code")
        private String healthCareProfessionalShortageAreaCode;
        
        @JsonProperty("reference-identification")
        private String referenceIdentification;
        
        @JsonProperty("postal-code")
        private String postalCode;
        
        @JsonProperty("monetary-amount-3")
        private String monetaryAmount3;
        
        @JsonProperty("level-of-care-code")
        private String levelOfCareCode;
        
        @JsonProperty("provider-agreement-code")
        private String providerAgreementCode;
    }
    
    @Data
    public static class SV5Segment {
        @JsonProperty("composite-medical-procedure-identifier")
        private String compositeMedicalProcedureIdentifier;
        
        @JsonProperty("unit-or-basis-for-measurement-code")
        private String unitOrBasisForMeasurementCode;
        
        @JsonProperty("quantity")
        private String quantity;
        
        @JsonProperty("monetary-amount")
        private String monetaryAmount;
        
        @JsonProperty("monetary-amount-2")
        private String monetaryAmount2;
        
        @JsonProperty("frequency-code")
        private String frequencyCode;
        
        @JsonProperty("composite-diagnosis-code-pointer")
        private String compositeDiagnosisCodePointer;
    }
    
    @Data
    public static class CR3Segment {
        @JsonProperty("certification-type-code")
        private String certificationTypeCode;
        
        @JsonProperty("unit-or-basis-for-measurement-code")
        private String unitOrBasisForMeasurementCode;
        
        @JsonProperty("quantity")
        private String quantity;
        
        @JsonProperty("insulin-dependent-code")
        private String insulinDependentCode;
        
        @JsonProperty("description")
        private String description;
    }
    
    @Data
    public static class QTYSegment {
        @JsonProperty("quantity-qualifier")
        private String quantityQualifier;
        
        @JsonProperty("quantity")
        private String quantity;
        
        @JsonProperty("composite-unit-of-measure")
        private CompositeUnitOfMeasure compositeUnitOfMeasure;
        
        @JsonProperty("free-form-information")
        private String freeFormInformation;
    }
    
    @Data
    public static class CompositeUnitOfMeasure {
        @JsonProperty("unit-or-basis-for-measurement-code")
        private String unitOrBasisForMeasurementCode;
    }
    
    @Data
    public static class MEASegment {
        @JsonProperty("measurement-reference-id-code")
        private String measurementReferenceIdCode;
        
        @JsonProperty("measurement-qualifier")
        private String measurementQualifier;
        
        @JsonProperty("measurement-value")
        private String measurementValue;
        
        @JsonProperty("composite-unit-of-measure")
        private String compositeUnitOfMeasure;
        
        @JsonProperty("range-minimum")
        private String rangeMinimum;
        
        @JsonProperty("range-maximum")
        private String rangeMaximum;
        
        @JsonProperty("measurement-significance-code")
        private String measurementSignificanceCode;
        
        @JsonProperty("measurement-attribute-code")
        private String measurementAttributeCode;
        
        @JsonProperty("surface-layer-position-code")
        private String surfaceLayerPositionCode;
        
        @JsonProperty("measurement-method-or-device")
        private String measurementMethodOrDevice;
    }
    
    @Data
    public static class PS1Segment {
        @JsonProperty("purchased-service-provider-identifier")
        private String purchasedServiceProviderIdentifier;
        
        @JsonProperty("purchased-service-charge-amount")
        private String purchasedServiceChargeAmount;
        
        @JsonProperty("state-or-province-code")
        private String stateOrProvinceCode;
        
        @JsonProperty("postal-code")
        private String postalCode;
        
        @JsonProperty("purchased-service-charge-amount-2")
        private String purchasedServiceChargeAmount2;
        
        @JsonProperty("composite-unit-of-measure")
        private String compositeUnitOfMeasure;
        
        @JsonProperty("quantity")
        private String quantity;
    }
}