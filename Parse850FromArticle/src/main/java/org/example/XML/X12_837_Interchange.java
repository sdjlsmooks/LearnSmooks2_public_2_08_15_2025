package org.example.XML;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

import java.util.List;

/**
 * Represents an X12 837 Healthcare Claim Transaction Set interchange structure.
 * This class encapsulates the complete hierarchy of an 837 EDI message including
 * headers, claim information, service lines, and trailers.
 * 
 * Note: The schema appears to be for 835 (Payment/Advice) but we're adapting it for 837 (Claim).
 * The structure follows the X12 835 format from the provided DFDL schema.
 */
@Data
@JsonRootName("X12_835_Interchange")
public class X12_837_Interchange {

    @JsonProperty("interchange-header")
    private InterchangeHeader interchangeHeader;

    @JsonProperty("group-header")
    private GroupHeader groupHeader;

    @JsonProperty("transaction-set-header")
    private TransactionSetHeader transactionSetHeader;

    @JsonProperty("beginning-hierarchical-transaction")
    private BeginningHierarchicalTransaction beginningHierarchicalTransaction;

    @JsonProperty("Loop_1000A_SubmitterName")
    private Loop1000ASubmitterName loop1000ASubmitterName;

    @JsonProperty("Loop_1000B_ReceiverName")
    private Loop1000BReceiverName loop1000BReceiverName;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("Loop_2000A_BillingProviderDetail")
    private List<Loop2000ABillingProviderDetail> loop2000ABillingProviderDetail;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("Loop_2000B_SubscriberDetail")
    private List<Loop2000BSubscriberDetail> loop2000BSubscriberDetail;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("Loop_2300_ClaimInformation")
    private List<Loop2300ClaimInformation> loop2300ClaimInformation;

    @JsonProperty("financial-information")
    private FinancialInformation financialInformation;

    @JsonProperty("payer-identification")
    private PayerIdentification payerIdentification;

    @JsonProperty("payee-identification")
    private PayeeIdentification payeeIdentification;

    @JsonProperty("header-number")
    private HeaderNumber headerNumber;

    @JsonProperty("provider-summary")
    private ProviderSummary providerSummary;

    @JsonProperty("claim-payment")
    private List<ClaimPayment> claimPayments;

    @JsonProperty("provider-adjustment")
    private ProviderAdjustment providerAdjustment;

    @JsonProperty("transaction-set-trailer")
    private TransactionSetTrailer transactionSetTrailer;

    @JsonProperty("functional-group-trailer")
    private FunctionalGroupTrailer functionalGroupTrailer;

    @JsonProperty("interchange-control-trailer")
    private InterchangeControlTrailer interchangeControlTrailer;

    // Nested Classes

    @Data
    public static class InterchangeHeader {
        @JsonProperty("auth-qual")
        private String authQual;
        @JsonProperty("auth-id")
        private String authId;
        @JsonProperty("security-qual")
        private String securityQual;
        @JsonProperty("security-id")
        private String securityId;
        @JsonProperty("sender-qual")
        private String senderQual;
        @JsonProperty("sender-id")
        private String senderId;
        @JsonProperty("receiver-qual")
        private String receiverQual;
        @JsonProperty("receiver-id")
        private String receiverId;
        @JsonProperty("date")
        private String date;
        @JsonProperty("time")
        private String time;
        @JsonProperty("standard")
        private String standard;
        @JsonProperty("version")
        private String version;
        @JsonProperty("interchange-control-number")
        private String interchangeControlNumber;
        @JsonProperty("ack")
        private String ack;
        @JsonProperty("test")
        private String test;
        @JsonProperty("s-delimiter")
        private String sDelimiter;
    }

    @Data
    public static class GroupHeader {
        @JsonProperty("code")
        private String code;
        @JsonProperty("sender")
        private String sender;
        @JsonProperty("receiver")
        private String receiver;
        @JsonProperty("date")
        private String date;
        @JsonProperty("time")
        private String time;
        @JsonProperty("group-control-number")
        private String groupControlNumber;
        @JsonProperty("standard")
        private String standard;
        @JsonProperty("version")
        private String version;
    }

    @Data
    public static class TransactionSetHeader {
        @JsonProperty("code")
        private String code;
        @JsonProperty("transaction-set-control-number")
        private String transactionSetControlNumber;
        @JsonProperty("implementation-convention-reference")
        private String implementationConventionReference;
    }

    @Data
    public static class BeginningHierarchicalTransaction {
        @JsonProperty("hierarchical-structure-code")
        private String hierarchicalStructureCode;
        @JsonProperty("transaction-set-purpose-code")
        private String transactionSetPurposeCode;
        @JsonProperty("reference-identification")
        private String referenceIdentification;
        @JsonProperty("date")
        private String date;
        @JsonProperty("time")
        private String time;
        @JsonProperty("transaction-type-code")
        private String transactionTypeCode;
    }

    @Data
    public static class Loop1000ASubmitterName {
        @JsonProperty("submitter-name")
        private NM1Segment submitterName;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("submitter-edi-contact-information")
        private List<PERSegment> submitterEdiContactInformation;
    }

    @Data
    public static class Loop1000BReceiverName {
        @JsonProperty("receiver-name")
        private NM1Segment receiverName;
    }

    @Data
    public static class Loop2000ABillingProviderDetail {
        @JsonProperty("billing-provider-hierarchical-level")
        private HLSegment billingProviderHierarchicalLevel;
        
        @JsonProperty("billing-provider-specialty-information")
        private PRVSegment billingProviderSpecialtyInformation;
        
        @JsonProperty("foreign-currency-information")
        private CURSegment foreignCurrencyInformation;
        
        @JsonProperty("Loop_2010AA_BillingProviderDetailHL")
        private Loop2010AABillingProviderDetailHL loop2010AABillingProviderDetailHL;
        
        @JsonProperty("Loop_2010AB_PayToAddressName")
        private Loop2010ABPayToAddressName loop2010ABPayToAddressName;
        
        @JsonProperty("Loop_2010AC_PayToPlanName")
        private Loop2010ACPayToPlanName loop2010ACPayToPlanName;
    }

    @Data
    public static class Loop2010AABillingProviderDetailHL {
        @JsonProperty("billing-provider-name")
        private NM1Segment billingProviderName;
        
        @JsonProperty("billing-provider-address")
        private N3Segment billingProviderAddress;
        
        @JsonProperty("billing-provider-city-state-zip")
        private N4Segment billingProviderCityStateZip;
        
        @JsonProperty("billing-provider-tax-id")
        private REFSegment billingProviderTaxId;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("billing-provider-upin-license-info")
        private List<REFSegment> billingProviderUpinLicenseInfo;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("billing-provider-contact-info")
        private List<PERSegment> billingProviderContactInfo;
    }

    @Data
    public static class Loop2010ABPayToAddressName {
        @JsonProperty("pay-to-address-name")
        private NM1Segment payToAddressName;
        
        @JsonProperty("pay-to-address")
        private N3Segment payToAddress;
        
        @JsonProperty("pay-to-city-state-zip")
        private N4Segment payToCityStateZip;
    }

    @Data
    public static class Loop2010ACPayToPlanName {
        @JsonProperty("pay-to-plan-name")
        private NM1Segment payToPlanName;
        
        @JsonProperty("pay-to-plan-address")
        private N3Segment payToPlanAddress;
        
        @JsonProperty("pay-to-plan-city-state-zip")
        private N4Segment payToPlanCityStateZip;
        
        @JsonProperty("pay-to-plan-secondary-id")
        private REFSegment payToPlanSecondaryId;
        
        @JsonProperty("pay-to-plan-tax-id")
        private REFSegment payToPlanTaxId;
    }

    @Data
    public static class Loop2000BSubscriberDetail {
        @JsonProperty("subscriber-hierarchical-level")
        private HLSegment subscriberHierarchicalLevel;
        
        @JsonProperty("subscriber-information")
        private SBRSegment subscriberInformation;
        
        @JsonProperty("patient-information")
        private PATSegment patientInformation;
        
        @JsonProperty("Loop_2010BA_SubscriberName")
        private Loop2010BASubscriberName loop2010BASubscriberName;
        
        @JsonProperty("Loop_2010BB_PayerName")
        private Loop2010BBPayerName loop2010BBPayerName;
    }

    @Data
    public static class Loop2010BASubscriberName {
        @JsonProperty("subscriber-name")
        private NM1Segment subscriberName;
        
        @JsonProperty("subscriber-address")
        private N3Segment subscriberAddress;
        
        @JsonProperty("subscriber-city-state-zip")
        private N4Segment subscriberCityStateZip;
        
        @JsonProperty("subscriber-demographic-info")
        private DMGSegment subscriberDemographicInfo;
        
        @JsonProperty("subscriber-secondary-id")
        private REFSegment subscriberSecondaryId;
        
        @JsonProperty("property-and-casualty-claim-number")
        private REFSegment propertyAndCasualtyClaimNumber;
        
        @JsonProperty("property-casualty-contact-info")
        private PERSegment propertyCasualtyContactInfo;
    }

    @Data
    public static class Loop2010BBPayerName {
        @JsonProperty("payer-name")
        private NM1Segment payerName;
        
        @JsonProperty("payer-address")
        private N3Segment payerAddress;
        
        @JsonProperty("payer-city-state-zip")
        private N4Segment payerCityStateZip;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("payer-secondary-id")
        private List<REFSegment> payerSecondaryId;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("billing-provider-secondary-id")
        private List<REFSegment> billingProviderSecondaryId;
    }

    @Data
    public static class Loop2300ClaimInformation {
        @JsonProperty("claim-information")
        private CLMSegment claimInformation;
        
        @JsonProperty("onset-illness-or-symptom")
        private DTPSegment onsetIllnessOrSymptom;
        
        @JsonProperty("initial-treatment-date")
        private DTPSegment initialTreatmentDate;
        
        @JsonProperty("last-seen-date")
        private DTPSegment lastSeenDate;
        
        @JsonProperty("acute-manifestation-date")
        private DTPSegment acuteManifestationDate;
        
        @JsonProperty("accident-date")
        private DTPSegment accidentDate;
        
        @JsonProperty("last-menstrual-period-date")
        private DTPSegment lastMenstrualPeriodDate;
        
        @JsonProperty("last-x-ray-date")
        private DTPSegment lastXRayDate;
        
        @JsonProperty("hearing-vision-prescription-date")
        private DTPSegment hearingVisionPrescriptionDate;
        
        @JsonProperty("disability-date")
        private DTPSegment disabilityDate;
        
        @JsonProperty("last-worked-date")
        private DTPSegment lastWorkedDate;
        
        @JsonProperty("authorized-return-to-work-date")
        private DTPSegment authorizedReturnToWorkDate;
        
        @JsonProperty("admission-date")
        private DTPSegment admissionDate;
        
        @JsonProperty("discharge-date")
        private DTPSegment dischargeDate;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("assumed-relinquished-care-date")
        private List<DTPSegment> assumedRelinquishedCareDate;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("property-casualty-date")
        private List<DTPSegment> propertyCasualtyDate;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("repricer-received-date")
        private List<DTPSegment> repricerReceivedDate;
        
        @JsonProperty("claim-supplemental-information")
        private PWKSegment claimSupplementalInformation;
        
        @JsonProperty("contract-information")
        private CN1Segment contractInformation;
        
        @JsonProperty("patient-amount-paid")
        private AMTSegment patientAmountPaid;
        
        // REF segments for various reference numbers
        @JsonProperty("service-authorization-exception-code")
        private REFSegment serviceAuthorizationExceptionCode;
        
        @JsonProperty("mandatory-medicare")
        private REFSegment mandatoryMedicare;
        
        @JsonProperty("mammogram-certification")
        private REFSegment mammogramCertification;
        
        @JsonProperty("referral-number")
        private REFSegment referralNumber;
        
        @JsonProperty("prior-authorization")
        private REFSegment priorAuthorization;
        
        @JsonProperty("payer-claim-control-number")
        private REFSegment payerClaimControlNumber;
        
        @JsonProperty("clinical-laboratory-improvement")
        private REFSegment clinicalLaboratoryImprovement;
        
        @JsonProperty("repriced-claim-number")
        private REFSegment repricedClaimNumber;
        
        @JsonProperty("adjusted-repriced-claim-number")
        private REFSegment adjustedRepricedClaimNumber;
        
        @JsonProperty("investigational-device-ex-number")
        private REFSegment investigationalDeviceExNumber;
        
        @JsonProperty("claim-id-for-txn-intermediaries")
        private REFSegment claimIdForTxnIntermediaries;
        
        @JsonProperty("medical-record-number")
        private REFSegment medicalRecordNumber;
        
        @JsonProperty("demo-project-id")
        private REFSegment demoProjectId;
        
        @JsonProperty("care-plan-oversight")
        private REFSegment carePlanOversight;
        
        // K3 File Information
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("file-information")
        private List<K3Segment> fileInformation;
        
        // NTE Claim Note
        @JsonProperty("claim-note")
        private NTESegment claimNote;
        
        // CR1 Ambulance Transport Info
        @JsonProperty("ambulance-transport-info")
        private CR1Segment ambulanceTransportInfo;
        
        // CR2 Spinal Manipulation Info
        @JsonProperty("spinal-manipulation-info")
        private CR2Segment spinalManipulationInfo;
        
        // CRC segments for various certifications
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("ambulance-certification")
        private List<CRCSegment> ambulanceCertification;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("patient-condition-vision")
        private List<CRCSegment> patientConditionVision;
        
        @JsonProperty("homebound-indicator")
        private CRCSegment homeboundIndicator;
        
        @JsonProperty("epsdt-referral")
        private CRCSegment epsdtReferral;
        
        // HI segments for diagnosis and procedure codes
        @JsonProperty("health-care-diagnosis-code")
        private HISegment healthCareDiagnosisCode;
        
        @JsonProperty("anesthesia-related-proc")
        private HISegment anesthesiaRelatedProc;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("condition-info")
        private List<HISegment> conditionInfo;
        
        // HCP Claim Pricing/Repricing Info
        @JsonProperty("claim-pricing-repricing-info")
        private HCPSegment claimPricingRepricingInfo;
    }

    @Data
    public static class HLSegment {
        @JsonProperty("hierarchical-id-number")
        private String hierarchicalIdNumber;
        @JsonProperty("hierarchical-parent-id-number")
        private String hierarchicalParentIdNumber;
        @JsonProperty("hierarchical-level-code")
        private String hierarchicalLevelCode;
        @JsonProperty("hierarchical-child-code")
        private String hierarchicalChildCode;
    }

    @Data
    public static class PRVSegment {
        @JsonProperty("provider-code")
        private String providerCode;
        @JsonProperty("reference-identification-qualifier")
        private String referenceIdentificationQualifier;
        @JsonProperty("reference-identification")
        private String referenceIdentification;
        @JsonProperty("state-or-province-code")
        private String stateOrProvinceCode;
        @JsonProperty("specialty-information")
        private String specialtyInformation;
        @JsonProperty("provider-organization-code")
        private String providerOrganizationCode;
    }

    @Data
    public static class SBRSegment {
        @JsonProperty("payer-responsibility-sequence-number-code")
        private String payerResponsibilitySequenceNumberCode;
        @JsonProperty("individual-relationship-code")
        private String individualRelationshipCode;
        @JsonProperty("insured-group-or-policy-number")
        private String insuredGroupOrPolicyNumber;
        @JsonProperty("other-insured-group-name")
        private String otherInsuredGroupName;
        @JsonProperty("insurance-type-code")
        private String insuranceTypeCode;
        @JsonProperty("coordination-of-benefits-code")
        private String coordinationOfBenefitsCode;
        @JsonProperty("yes-no-condition-response-code")
        private String yesNoConditionResponseCode;
        @JsonProperty("employment-status-code")
        private String employmentStatusCode;
        @JsonProperty("claim-filing-indicator-code")
        private String claimFilingIndicatorCode;
    }

    @Data
    public static class PATSegment {
        @JsonProperty("individual-relationship-code")
        private String individualRelationshipCode;
        @JsonProperty("patient-location-code")
        private String patientLocationCode;
        @JsonProperty("employment-status-code")
        private String employmentStatusCode;
        @JsonProperty("student-status-code")
        private String studentStatusCode;
        @JsonProperty("date-time-qualifier")
        private String dateTimeQualifier;
        @JsonProperty("date")
        private String date;
        @JsonProperty("unit-or-basis-for-measurement-code")
        private String unitOrBasisForMeasurementCode;
        @JsonProperty("weight")
        private String weight;
        @JsonProperty("yes-no-condition-response-code")
        private String yesNoConditionResponseCode;
    }

    @Data
    public static class DMGSegment {
        @JsonProperty("date-time-period-format-qualifier")
        private String dateTimePeriodFormatQualifier;
        @JsonProperty("date-time-period")
        private String dateTimePeriod;
        @JsonProperty("gender-code")
        private String genderCode;
        @JsonProperty("marital-status-code")
        private String maritalStatusCode;
        @JsonProperty("race-or-ethnicity-code")
        private String raceOrEthnicityCode;
        @JsonProperty("citizenship-status-code")
        private String citizenshipStatusCode;
        @JsonProperty("country-code")
        private String countryCode;
        @JsonProperty("basis-of-verification-code")
        private String basisOfVerificationCode;
        @JsonProperty("quantity")
        private String quantity;
    }

    @Data
    public static class FinancialInformation {
        @JsonProperty("BPR")
        private BPRSegment bpr;
        @JsonProperty("TRN")
        private TRNSegment trn;
        @JsonProperty("CUR")
        private CURSegment cur;
        @JsonProperty("REF")
        private List<REFSegment> ref;
        @JsonProperty("DTM")
        private List<DTMSegment> dtm;
    }

    @Data
    public static class BPRSegment {
        @JsonProperty("transaction-handling-code")
        private String transactionHandlingCode;
        @JsonProperty("monetary-amount")
        private String monetaryAmount;
        @JsonProperty("credit-debit-flag-code")
        private String creditDebitFlagCode;
        @JsonProperty("payment-method-code")
        private String paymentMethodCode;
        @JsonProperty("payment-format-code")
        private String paymentFormatCode;
        @JsonProperty("depository-financial-institution-dfi-identification-number-qualifier")
        private String dfiIdNumberQualifier;
        @JsonProperty("depository-financial-institution-dfi-identification-number")
        private String dfiIdNumber;
        @JsonProperty("account-number-qualifier")
        private String accountNumberQualifier;
        @JsonProperty("sender-bank-account-number")
        private String senderBankAccountNumber;
        @JsonProperty("originating-company-identifier")
        private String originatingCompanyIdentifier;
        @JsonProperty("originating-company-supplemental-code")
        private String originatingCompanySupplementalCode;
        @JsonProperty("receiver-or-provider-bank-id-number-qualifier")
        private String receiverBankIdNumberQualifier;
        @JsonProperty("receiver-or-provider-bank-id-number")
        private String receiverBankIdNumber;
        @JsonProperty("receiver-or-provider-account-number-qualifier")
        private String receiverAccountNumberQualifier;
        @JsonProperty("receiver-or-provider-account-number")
        private String receiverAccountNumber;
        @JsonProperty("date")
        private String date;
        @JsonProperty("business-function-code")
        private String businessFunctionCode;
    }

    @Data
    public static class TRNSegment {
        @JsonProperty("trace-type-code")
        private String traceTypeCode;
        @JsonProperty("reference-identification")
        private String referenceIdentification;
        @JsonProperty("originating-company-identifier")
        private String originatingCompanyIdentifier;
        @JsonProperty("reference-identification-2")
        private String referenceIdentification2;
    }

    @Data
    public static class CURSegment {
        @JsonProperty("entity-identifier-code")
        private String entityIdentifierCode;
        @JsonProperty("currency-code")
        private String currencyCode;
        @JsonProperty("exchange-rate")
        private String exchangeRate;
        @JsonProperty("entity-identifier-code-2")
        private String entityIdentifierCode2;
        @JsonProperty("currency-code-2")
        private String currencyCode2;
        @JsonProperty("currency-market-exchange-code")
        private String currencyMarketExchangeCode;
        @JsonProperty("date-time-qualifier")
        private String dateTimeQualifier;
        @JsonProperty("date")
        private String date;
        @JsonProperty("time")
        private String time;
    }

    @Data
    public static class REFSegment {
        @JsonProperty("reference-identification-qualifier")
        private String referenceIdentificationQualifier;
        @JsonProperty("reference-identification")
        private String referenceIdentification;
        @JsonProperty("description")
        private String description;
        @JsonProperty("reference-identifier")
        private ReferenceIdentifier referenceIdentifier;
    }

    @Data
    public static class ReferenceIdentifier {
        @JsonProperty("reference-identification-qualifier")
        private String referenceIdentificationQualifier;
        @JsonProperty("reference-identification")
        private String referenceIdentification;
        @JsonProperty("reference-identification-qualifier-2")
        private String referenceIdentificationQualifier2;
        @JsonProperty("reference-identification-2")
        private String referenceIdentification2;
        @JsonProperty("reference-identification-qualifier-3")
        private String referenceIdentificationQualifier3;
        @JsonProperty("reference-identification-3")
        private String referenceIdentification3;
    }

    @Data
    public static class DTMSegment {
        @JsonProperty("date-time-qualifier")
        private String dateTimeQualifier;
        @JsonProperty("date")
        private String date;
        @JsonProperty("time")
        private String time;
        @JsonProperty("time-code")
        private String timeCode;
        @JsonProperty("date-time-period-format-qualifier")
        private String dateTimePeriodFormatQualifier;
        @JsonProperty("date-time-period")
        private String dateTimePeriod;
    }

    @Data
    public static class PayerIdentification {
        @JsonProperty("N1")
        private N1Segment n1;
        @JsonProperty("N3")
        private N3Segment n3;
        @JsonProperty("N4")
        private N4Segment n4;
        @JsonProperty("REF")
        private List<REFSegment> ref;
        @JsonProperty("PER")
        private PERSegment per;
    }

    @Data
    public static class PayeeIdentification {
        @JsonProperty("N1")
        private N1Segment n1;
        @JsonProperty("N3")
        private N3Segment n3;
        @JsonProperty("N4")
        private N4Segment n4;
        @JsonProperty("REF")
        private List<REFSegment> ref;
        @JsonProperty("RDM")
        private RDMSegment rdm;
    }

    @Data
    public static class N1Segment {
        @JsonProperty("entity-identifier-code")
        private String entityIdentifierCode;
        @JsonProperty("name")
        private String name;
        @JsonProperty("identification-code-qualifier")
        private String identificationCodeQualifier;
        @JsonProperty("identification-code")
        private String identificationCode;
        @JsonProperty("entity-identifier-code-2")
        private String entityIdentifierCode2;
        @JsonProperty("identification-code-2")
        private String identificationCode2;
    }

    @Data
    public static class N3Segment {
        @JsonProperty("address-information")
        private String addressInformation;
        @JsonProperty("address-information-2")
        private String addressInformation2;
    }

    @Data
    public static class N4Segment {
        @JsonProperty("city-name")
        private String cityName;
        @JsonProperty("state-or-province-code")
        private String stateOrProvinceCode;
        @JsonProperty("postal-code")
        private String postalCode;
        @JsonProperty("country-code")
        private String countryCode;
        @JsonProperty("location-qualifier")
        private String locationQualifier;
        @JsonProperty("location-identifier")
        private String locationIdentifier;
    }

    @Data
    public static class PERSegment {
        @JsonProperty("contact-function-code")
        private String contactFunctionCode;
        @JsonProperty("name")
        private String name;
        @JsonProperty("communication-number-qualifier")
        private String communicationNumberQualifier;
        @JsonProperty("communication-number")
        private String communicationNumber;
        @JsonProperty("communication-number-qualifier-2")
        private String communicationNumberQualifier2;
        @JsonProperty("communication-number-2")
        private String communicationNumber2;
        @JsonProperty("communication-number-qualifier-3")
        private String communicationNumberQualifier3;
        @JsonProperty("communication-number-3")
        private String communicationNumber3;
        @JsonProperty("contact-inquiry-reference")
        private String contactInquiryReference;
    }

    @Data
    public static class RDMSegment {
        @JsonProperty("report-transmission-code")
        private String reportTransmissionCode;
        @JsonProperty("name")
        private String name;
        @JsonProperty("communication-number-qualifier")
        private String communicationNumberQualifier;
        @JsonProperty("communication-number")
        private String communicationNumber;
    }

    @Data
    public static class HeaderNumber {
        @JsonProperty("LX")
        private LXSegment lx;
    }

    @Data
    public static class LXSegment {
        @JsonProperty("assigned-number")
        private String assignedNumber;
    }

    @Data
    public static class ProviderSummary {
        @JsonProperty("TS3")
        private TS3Segment ts3;
        @JsonProperty("TS2")
        private TS2Segment ts2;
    }

    @Data
    public static class TS3Segment {
        @JsonProperty("reference-identification")
        private String referenceIdentification;
        @JsonProperty("facility-code-value")
        private String facilityCodeValue;
        @JsonProperty("date")
        private String date;
        @JsonProperty("quantity")
        private String quantity;
        @JsonProperty("monetary-amount")
        private String monetaryAmount;
        @JsonProperty("monetary-amount-2")
        private String monetaryAmount2;
        @JsonProperty("monetary-amount-3")
        private String monetaryAmount3;
        @JsonProperty("monetary-amount-4")
        private String monetaryAmount4;
        @JsonProperty("monetary-amount-5")
        private String monetaryAmount5;
    }

    @Data
    public static class TS2Segment {
        @JsonProperty("supplemental-amount-1")
        private String supplementalAmount1;
        @JsonProperty("supplemental-amount-2")
        private String supplementalAmount2;
        @JsonProperty("supplemental-amount-3")
        private String supplementalAmount3;
        @JsonProperty("supplemental-amount-4")
        private String supplementalAmount4;
        @JsonProperty("supplemental-amount-5")
        private String supplementalAmount5;
    }

    @Data
    public static class ClaimPayment {
        @JsonProperty("CLP")
        private CLPSegment clp;
        @JsonProperty("CAS")
        private List<CASSegment> cas;
        @JsonProperty("NM1")
        private List<NM1Segment> nm1;
        @JsonProperty("MIA")
        private MIASegment mia;
        @JsonProperty("MOA")
        private MOASegment moa;
        @JsonProperty("REF")
        private List<REFSegment> ref;
        @JsonProperty("DTM")
        private List<DTMSegment> dtm;
        @JsonProperty("PER")
        private List<PERSegment> per;
        @JsonProperty("AMT")
        private List<AMTSegment> amt;
        @JsonProperty("QTY")
        private List<QTYSegment> qty;
        @JsonProperty("service-payment-information")
        private List<ServicePaymentInformation> servicePaymentInformation;
    }

    @Data
    public static class CLPSegment {
        @JsonProperty("claim-submitters-identifier")
        private String claimSubmittersIdentifier;
        @JsonProperty("claim-status-code")
        private String claimStatusCode;
        @JsonProperty("total-claim-charge-amount")
        private String totalClaimChargeAmount;
        @JsonProperty("claim-payment-amount")
        private String claimPaymentAmount;
        @JsonProperty("patient-responsibility-amount")
        private String patientResponsibilityAmount;
        @JsonProperty("claim-filing-indicator-code")
        private String claimFilingIndicatorCode;
        @JsonProperty("payer-claim-control-number")
        private String payerClaimControlNumber;
        @JsonProperty("facility-type-code")
        private String facilityTypeCode;
        @JsonProperty("claim-frequency-code")
        private String claimFrequencyCode;
        @JsonProperty("patient-status-code")
        private String patientStatusCode;
        @JsonProperty("diagnosis-related-group-code")
        private String diagnosisRelatedGroupCode;
        @JsonProperty("drg-weight")
        private String drgWeight;
        @JsonProperty("discharge-fraction")
        private String dischargeFraction;
        @JsonProperty("ymd-date")
        private String ymdDate;
    }

    @Data
    public static class CLMSegment {
        @JsonProperty("claim-submitters-identifier")
        private String claimSubmittersIdentifier;
        @JsonProperty("monetary-amount")
        private String monetaryAmount;
        @JsonProperty("claim-filing-indicator-code")
        private String claimFilingIndicatorCode;
        @JsonProperty("non-institutional-claim-type-code")
        private String nonInstitutionalClaimTypeCode;
        @JsonProperty("health-care-service-location-information")
        private String healthCareServiceLocationInformation;
        @JsonProperty("yes-no-condition-response-code")
        private String yesNoConditionResponseCode;
        @JsonProperty("provider-accept-assignment-code")
        private String providerAcceptAssignmentCode;
        @JsonProperty("yes-no-condition-response-code-2")
        private String yesNoConditionResponseCode2;
        @JsonProperty("release-of-information-code")
        private String releaseOfInformationCode;
        @JsonProperty("patient-signature-source-code")
        private String patientSignatureSourceCode;
        @JsonProperty("related-causes-information")
        private String relatedCausesInformation;
        @JsonProperty("special-program-code")
        private String specialProgramCode;
        @JsonProperty("yes-no-condition-response-code-3")
        private String yesNoConditionResponseCode3;
        @JsonProperty("level-of-service-code")
        private String levelOfServiceCode;
        @JsonProperty("yes-no-condition-response-code-4")
        private String yesNoConditionResponseCode4;
        @JsonProperty("provider-agreement-code")
        private String providerAgreementCode;
        @JsonProperty("claim-status-code")
        private String claimStatusCode;
        @JsonProperty("yes-no-condition-response-code-5")
        private String yesNoConditionResponseCode5;
        @JsonProperty("claim-submission-reason-code")
        private String claimSubmissionReasonCode;
        @JsonProperty("delay-reason-code")
        private String delayReasonCode;
    }

    @Data
    public static class DTPSegment {
        @JsonProperty("date-time-qualifier")
        private String dateTimeQualifier;
        @JsonProperty("date-time-period-format-qualifier")
        private String dateTimePeriodFormatQualifier;
        @JsonProperty("date-time-period")
        private String dateTimePeriod;
    }

    @Data
    public static class PWKSegment {
        @JsonProperty("report-type-code")
        private String reportTypeCode;
        @JsonProperty("report-transmission-code")
        private String reportTransmissionCode;
        @JsonProperty("report-copies-needed")
        private String reportCopiesNeeded;
        @JsonProperty("entity-identifier-code")
        private String entityIdentifierCode;
        @JsonProperty("identification-code-qualifier")
        private String identificationCodeQualifier;
        @JsonProperty("identification-code")
        private String identificationCode;
        @JsonProperty("description")
        private String description;
        @JsonProperty("actions-indicated")
        private String actionsIndicated;
        @JsonProperty("request-category-code")
        private String requestCategoryCode;
    }

    @Data
    public static class CN1Segment {
        @JsonProperty("contract-type-code")
        private String contractTypeCode;
        @JsonProperty("contract-amount")
        private String contractAmount;
        @JsonProperty("contract-percentage")
        private String contractPercentage;
        @JsonProperty("contract-code")
        private String contractCode;
        @JsonProperty("terms-discount-percentage")
        private String termsDiscountPercentage;
        @JsonProperty("contract-version-identifier")
        private String contractVersionIdentifier;
    }

    @Data
    public static class K3Segment {
        @JsonProperty("fixed-format-information")
        private String fixedFormatInformation;
        @JsonProperty("record-format-code")
        private String recordFormatCode;
        @JsonProperty("situation-code")
        private String situationCode;
    }

    @Data
    public static class NTESegment {
        @JsonProperty("note-reference-code")
        private String noteReferenceCode;
        @JsonProperty("note-text")
        private String noteText;
    }

    @Data
    public static class CR1Segment {
        @JsonProperty("unit-or-basis-for-measurement-code")
        private String unitOrBasisForMeasurementCode;
        @JsonProperty("weight")
        private String weight;
        @JsonProperty("ambulance-transport-code")
        private String ambulanceTransportCode;
        @JsonProperty("ambulance-transport-reason-code")
        private String ambulanceTransportReasonCode;
        @JsonProperty("unit-or-basis-for-measurement-code-2")
        private String unitOrBasisForMeasurementCode2;
        @JsonProperty("quantity")
        private String quantity;
        @JsonProperty("address-information")
        private String addressInformation;
        @JsonProperty("address-information-2")
        private String addressInformation2;
        @JsonProperty("description")
        private String description;
        @JsonProperty("description-2")
        private String description2;
    }

    @Data
    public static class CR2Segment {
        @JsonProperty("count")
        private String count;
        @JsonProperty("quantity")
        private String quantity;
        @JsonProperty("subluxation-level-code")
        private String subluxationLevelCode;
        @JsonProperty("subluxation-level-code-2")
        private String subluxationLevelCode2;
        @JsonProperty("unit-or-basis-for-measurement-code")
        private String unitOrBasisForMeasurementCode;
        @JsonProperty("quantity-2")
        private String quantity2;
        @JsonProperty("quantity-3")
        private String quantity3;
        @JsonProperty("nature-of-condition-code")
        private String natureOfConditionCode;
        @JsonProperty("yes-no-condition-response-code")
        private String yesNoConditionResponseCode;
        @JsonProperty("description")
        private String description;
        @JsonProperty("description-2")
        private String description2;
        @JsonProperty("yes-no-condition-response-code-2")
        private String yesNoConditionResponseCode2;
    }

    @Data
    public static class CRCSegment {
        @JsonProperty("code-category")
        private String codeCategory;
        @JsonProperty("yes-no-condition-or-response-code")
        private String yesNoConditionOrResponseCode;
        @JsonProperty("condition-indicator")
        private String conditionIndicator;
        @JsonProperty("condition-indicator-2")
        private String conditionIndicator2;
        @JsonProperty("condition-indicator-3")
        private String conditionIndicator3;
        @JsonProperty("condition-indicator-4")
        private String conditionIndicator4;
        @JsonProperty("condition-indicator-5")
        private String conditionIndicator5;
    }

    @Data
    public static class HISegment {
        @JsonProperty("health-care-code-information")
        private String healthCareCodeInformation;
        @JsonProperty("health-care-code-information-2")
        private String healthCareCodeInformation2;
        @JsonProperty("health-care-code-information-3")
        private String healthCareCodeInformation3;
        @JsonProperty("health-care-code-information-4")
        private String healthCareCodeInformation4;
        @JsonProperty("health-care-code-information-5")
        private String healthCareCodeInformation5;
        @JsonProperty("health-care-code-information-6")
        private String healthCareCodeInformation6;
        @JsonProperty("health-care-code-information-7")
        private String healthCareCodeInformation7;
        @JsonProperty("health-care-code-information-8")
        private String healthCareCodeInformation8;
        @JsonProperty("health-care-code-information-9")
        private String healthCareCodeInformation9;
        @JsonProperty("health-care-code-information-10")
        private String healthCareCodeInformation10;
        @JsonProperty("health-care-code-information-11")
        private String healthCareCodeInformation11;
        @JsonProperty("health-care-code-information-12")
        private String healthCareCodeInformation12;
    }

    @Data
    public static class HCPSegment {
        @JsonProperty("pricing-methodology")
        private String pricingMethodology;
        @JsonProperty("monetary-amount")
        private String monetaryAmount;
        @JsonProperty("monetary-amount-2")
        private String monetaryAmount2;
        @JsonProperty("reference-identification")
        private String referenceIdentification;
        @JsonProperty("rate")
        private String rate;
        @JsonProperty("reference-identification-2")
        private String referenceIdentification2;
        @JsonProperty("monetary-amount-3")
        private String monetaryAmount3;
        @JsonProperty("product-service-id")
        private String productServiceId;
        @JsonProperty("product-service-id-qualifier")
        private String productServiceIdQualifier;
        @JsonProperty("product-service-id-2")
        private String productServiceId2;
        @JsonProperty("unit-or-basis-for-measurement-code")
        private String unitOrBasisForMeasurementCode;
        @JsonProperty("quantity")
        private String quantity;
        @JsonProperty("reject-reason-code")
        private String rejectReasonCode;
        @JsonProperty("policy-compliance-code")
        private String policyComplianceCode;
        @JsonProperty("exception-code")
        private String exceptionCode;
    }

    @Data
    public static class CASSegment {
        @JsonProperty("claim-adjustment-group-code")
        private String claimAdjustmentGroupCode;
        @JsonProperty("claim-adjustment-reason-code")
        private String claimAdjustmentReasonCode;
        @JsonProperty("monetary-amount")
        private String monetaryAmount;
        @JsonProperty("quantity")
        private String quantity;
        @JsonProperty("claim-adjustment-reason-code-2")
        private String claimAdjustmentReasonCode2;
        @JsonProperty("monetary-amount-2")
        private String monetaryAmount2;
        @JsonProperty("quantity-2")
        private String quantity2;
        @JsonProperty("claim-adjustment-reason-code-3")
        private String claimAdjustmentReasonCode3;
        @JsonProperty("monetary-amount-3")
        private String monetaryAmount3;
        @JsonProperty("quantity-3")
        private String quantity3;
    }

    @Data
    public static class NM1Segment {
        @JsonProperty("entity-identifier-code")
        private String entityIdentifierCode;
        @JsonProperty("entity-type-qualifier")
        private String entityTypeQualifier;
        @JsonProperty("name-last-or-organization-name")
        private String nameLastOrOrganizationName;
        @JsonProperty("name-first")
        private String nameFirst;
        @JsonProperty("name-middle")
        private String nameMiddle;
        @JsonProperty("name-prefix")
        private String namePrefix;
        @JsonProperty("name-suffix")
        private String nameSuffix;
        @JsonProperty("identification-code-qualifier")
        private String identificationCodeQualifier;
        @JsonProperty("identification-code")
        private String identificationCode;
        @JsonProperty("entity-relationship-code")
        private String entityRelationshipCode;
        @JsonProperty("entity-identifier-code-2")
        private String entityIdentifierCode2;
        @JsonProperty("entity-identifier-code-qualifier-2")
        private String entityIdentifierCodeQualifier2;
    }

    @Data
    public static class MIASegment {
        @JsonProperty("covered-days-or-visits-count")
        private String coveredDaysOrVisitsCount;
        @JsonProperty("pps-operating-outlier-amount")
        private String ppsOperatingOutlierAmount;
        @JsonProperty("lifetime-psychiatric-days-count")
        private String lifetimePsychiatricDaysCount;
        @JsonProperty("claim-drg-amount")
        private String claimDrgAmount;
        @JsonProperty("claim-payment-remark-code")
        private String claimPaymentRemarkCode;
        @JsonProperty("claim-disproportionate-share-amount")
        private String claimDisproportionateShareAmount;
        @JsonProperty("claim-msp-pass-through-amount")
        private String claimMspPassThroughAmount;
        @JsonProperty("claim-pps-capital-amount")
        private String claimPpsCapitalAmount;
        @JsonProperty("pps-capital-fsp-drg-amount")
        private String ppsCapitalFspDrgAmount;
        @JsonProperty("pps-capital-hsp-drg-amount")
        private String ppsCapitalHspDrgAmount;
        @JsonProperty("pps-capital-dsh-drg-amount")
        private String ppsCapitalDshDrgAmount;
        @JsonProperty("old-capital-amount")
        private String oldCapitalAmount;
        @JsonProperty("pps-capital-ime-amount")
        private String ppsCapitalImeAmount;
        @JsonProperty("pps-operating-hospital-specific-drg-amount")
        private String ppsOperatingHospitalSpecificDrgAmount;
        @JsonProperty("cost-report-day-count")
        private String costReportDayCount;
        @JsonProperty("pps-operating-federal-specific-drg-amount")
        private String ppsOperatingFederalSpecificDrgAmount;
        @JsonProperty("claim-pps-capital-outlier-amount")
        private String claimPpsCapitalOutlierAmount;
        @JsonProperty("claim-indirect-medical-education-amount")
        private String claimIndirectMedicalEducationAmount;
        @JsonProperty("nonpayable-professional-component-billed-amount")
        private String nonpayableProfessionalComponentBilledAmount;
    }

    @Data
    public static class MOASegment {
        @JsonProperty("reimbursement-rate")
        private String reimbursementRate;
        @JsonProperty("hcpcs-payable-amount")
        private String hcpcsPayableAmount;
        @JsonProperty("claim-payment-remark-code")
        private String claimPaymentRemarkCode;
        @JsonProperty("claim-payment-remark-code-2")
        private String claimPaymentRemarkCode2;
        @JsonProperty("claim-payment-remark-code-3")
        private String claimPaymentRemarkCode3;
        @JsonProperty("claim-payment-remark-code-4")
        private String claimPaymentRemarkCode4;
        @JsonProperty("claim-payment-remark-code-5")
        private String claimPaymentRemarkCode5;
        @JsonProperty("end-stage-renal-disease-payment-amount")
        private String endStageRenalDiseasePaymentAmount;
        @JsonProperty("non-payable-professional-component-billed-amount")
        private String nonPayableProfessionalComponentBilledAmount;
    }

    @Data
    public static class AMTSegment {
        @JsonProperty("amount-qualifier-code")
        private String amountQualifierCode;
        @JsonProperty("monetary-amount")
        private String monetaryAmount;
        @JsonProperty("credit-debit-flag-code")
        private String creditDebitFlagCode;
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
        @JsonProperty("exponent")
        private String exponent;
        @JsonProperty("multiplier")
        private String multiplier;
    }

    @Data
    public static class ServicePaymentInformation {
        @JsonProperty("SVC")
        private SVCSegment svc;
        @JsonProperty("DTM")
        private List<DTMSegment> dtm;
        @JsonProperty("CAS")
        private List<CASSegment> cas;
        @JsonProperty("REF")
        private List<REFSegment> ref;
        @JsonProperty("AMT")
        private List<AMTSegment> amt;
        @JsonProperty("QTY")
        private List<QTYSegment> qty;
        @JsonProperty("LQ")
        private List<LQSegment> lq;
    }

    @Data
    public static class SVCSegment {
        @JsonProperty("composite-medical-procedure-identifier")
        private String compositeMedicalProcedureIdentifier;
        @JsonProperty("line-item-charge-amount")
        private String lineItemChargeAmount;
        @JsonProperty("line-item-provider-payment-amount")
        private String lineItemProviderPaymentAmount;
        @JsonProperty("national-uniform-billing-committee-revenue-code")
        private String nationalUniformBillingCommitteeRevenueCode;
        @JsonProperty("quantity")
        private String quantity;
        @JsonProperty("composite-medical-procedure-identifier-original")
        private String compositeMedicalProcedureIdentifierOriginal;
        @JsonProperty("units-of-service-count")
        private String unitsOfServiceCount;
    }

    @Data
    public static class LQSegment {
        @JsonProperty("code-list-qualifier-code")
        private String codeListQualifierCode;
        @JsonProperty("industry-code")
        private String industryCode;
    }

    @Data
    public static class ProviderAdjustment {
        @JsonProperty("PLB")
        private PLBSegment plb;
    }

    @Data
    public static class PLBSegment {
        @JsonProperty("provider-identifier")
        private String providerIdentifier;
        @JsonProperty("fiscal-period-date")
        private String fiscalPeriodDate;
        @JsonProperty("adjustment-identifier")
        private String adjustmentIdentifier;
        @JsonProperty("provider-adjustment-amount")
        private String providerAdjustmentAmount;
        @JsonProperty("adjustment-identifier-2")
        private String adjustmentIdentifier2;
        @JsonProperty("provider-adjustment-amount-2")
        private String providerAdjustmentAmount2;
        @JsonProperty("adjustment-identifier-3")
        private String adjustmentIdentifier3;
        @JsonProperty("provider-adjustment-amount-3")
        private String providerAdjustmentAmount3;
        @JsonProperty("adjustment-identifier-4")
        private String adjustmentIdentifier4;
        @JsonProperty("provider-adjustment-amount-4")
        private String providerAdjustmentAmount4;
        @JsonProperty("adjustment-identifier-5")
        private String adjustmentIdentifier5;
        @JsonProperty("provider-adjustment-amount-5")
        private String providerAdjustmentAmount5;
        @JsonProperty("adjustment-identifier-6")
        private String adjustmentIdentifier6;
        @JsonProperty("provider-adjustment-amount-6")
        private String providerAdjustmentAmount6;
    }

    @Data
    public static class TransactionSetTrailer {
        @JsonProperty("number-of-included-segments")
        private String numberOfIncludedSegments;
        @JsonProperty("transaction-set-control-number")
        private String transactionSetControlNumber;
    }

    @Data
    public static class FunctionalGroupTrailer {
        @JsonProperty("number-of-transaction-sets")
        private String numberOfTransactionSets;
        @JsonProperty("group-control-number")
        private String groupControlNumber;
    }

    @Data
    public static class InterchangeControlTrailer {
        @JsonProperty("number-of-function-groups-included")
        private String numberOfFunctionGroupsIncluded;
        @JsonProperty("interchange-control-number")
        private String interchangeControlNumber;
    }
}