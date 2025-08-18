package org.example.XML;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

import java.util.List;

/**
 * Java representation of an X12 835 interchange structure.
 * <p>
 * This POJO mirrors the key structure defined by src/main/resources/835_mapping.dfdl.xsd
 * and follows the same style as X12_850_Interchange for consistency.
 * <p>
 * It is intended for convenient JSON/XML binding (e.g., via Jackson) if/when
 * the application needs to work with the parsed 835 XML as a typed object graph.
 */
@Data
@JsonRootName("X12_835_Interchange")
public class X12_835_Interchange {

    // Top-level segments
    @JsonProperty("interchange-header")
    private InterchangeHeader interchangeHeader;

    @JsonProperty("group-header")
    private GroupHeader groupHeader;

    @JsonProperty("transaction-set-header")
    private TransactionSetHeader transactionSetHeader;

    @JsonProperty("HealthCareClaimPayment")
    private HealthCareClaimPayment healthCareClaimPayment;

    @JsonProperty("transaction-set-trailer")
    private TransactionSetTrailer transactionSetTrailer;

    @JsonProperty("functional-group-trailer")
    private FunctionalGroupTrailer functionalGroupTrailer;

    @JsonProperty("interchange-control-trailer")
    private InterchangeControlTrailer interchangeControlTrailer;

    // --- Headers (ISA/GS/ST) ---

    @Data
    public static class InterchangeHeader {
        // ISA segment fields (names aligned with 850 model semantics)
        @JsonProperty("auth-qual")
        private String authQual;     // I01

        @JsonProperty("auth-id")
        private String authId;       // I02

        @JsonProperty("security-qual")
        private String securityQual; // I03

        @JsonProperty("security-id")
        private String securityId;   // I04

        @JsonProperty("sender-qual")
        private String senderQual;   // I05

        @JsonProperty("sender-id")
        private String senderId;     // I06

        @JsonProperty("receiver-qual")
        private String receiverQual; // I07

        @JsonProperty("receiver-id")
        private String receiverId;   // I08

        @JsonProperty("date")
        private String date;         // I09

        @JsonProperty("time")
        private String time;         // I10

        @JsonProperty("standard")
        private String standard;     // I11

        @JsonProperty("version")
        private String version;      // I12

        @JsonProperty("interchange-control-number")
        private String interchangeControlNumber; // I13

        @JsonProperty("ack")
        private String ack;          // I14

        @JsonProperty("test")
        private String test;         // I15

        @JsonProperty("s-delimiter")
        private String sDelimiter;   // I16
    }

    @Data
    public static class GroupHeader {
        // GS segment
        @JsonProperty("code")
        private String code; // GS01

        @JsonProperty("sender")
        private String sender; // GS02

        @JsonProperty("receiver")
        private String receiver; // GS03

        @JsonProperty("date")
        private String date; // GS04

        @JsonProperty("time")
        private String time; // GS05

        @JsonProperty("group-control-number")
        private String groupControlNumber; // GS06

        @JsonProperty("standard")
        private String standard; // GS07

        @JsonProperty("version")
        private String version; // GS08
    }

    @Data
    public static class TransactionSetHeader {
        // ST segment
        @JsonProperty("code")
        private String code; // ST01 (should be 835)

        @JsonProperty("transaction-set-control-number")
        private String transactionSetControlNumber; // ST02

        @JsonProperty("implementation-convention-reference")
        private String implementationConventionReference; // ST03 (optional)
    }

    // --- HealthCareClaimPayment (BPR/TRN/etc + loops) ---

    @Data
    public static class HealthCareClaimPayment {
        @JsonProperty("financial-information")
        private BPRSegment financialInformation; // BPR

        @JsonProperty("reassociation-trace-number")
        private TRNSegment reassociationTraceNumber; // TRN

        @JsonProperty("foreign-currency-information")
        private CURSegment foreignCurrencyInformation; // CUR (optional)

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("receiver-identification")
        private List<REFSegment> receiverIdentification; // REF*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("version-identification")
        private List<REFSegment> versionIdentification; // REF*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("production-date")
        private List<DTMSegment> productionDate; // DTM*

        @JsonProperty("Loop_1000A_Payer")
        private Loop1000APayer loop1000APayer;

        @JsonProperty("Loop_1000B_Payee")
        private Loop1000BPayee loop1000BPayee;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("Loop_2000_Header")
        private List<Loop2000Header> loop2000Header; // repeating

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("provider-adjustment")
        private List<PLBSegment> providerAdjustment; // PLB*
    }

    // --- Loop 1000A (Payer) ---

    @Data
    public static class Loop1000APayer {
        @JsonProperty("payer-identification")
        private N1Segment payerIdentification; // N1

        @JsonProperty("payer-address")
        private N3Segment payerAddress; // N3

        @JsonProperty("payer-city-state-zip")
        private N4Segment payerCityStateZip; // N4

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("additional-payer-id")
        private List<REFSegment> additionalPayerId; // REF*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("payer-contact-info")
        private List<PERSegment> payerContactInfo; // PER*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("payer-technical-contact-info")
        private List<PERSegment> payerTechnicalContactInfo; // PER*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("payer-website")
        private List<PERSegment> payerWebsite; // PER*
    }

    // --- Loop 1000B (Payee) ---

    @Data
    public static class Loop1000BPayee {
        @JsonProperty("payee-identification")
        private N1Segment payeeIdentification; // N1

        @JsonProperty("payee-address")
        private N3Segment payeeAddress; // N3

        @JsonProperty("payee-city-state-zip")
        private N4Segment payeeCityStateZip; // N4

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("payee-identification-ref")
        private List<REFSegment> payeeIdentificationRef; // REF*

        @JsonProperty("remittance-delivery-method")
        private RDMSegment remittanceDeliveryMethod; // RDM
    }

    // --- Loop 2000 Header (LX/TS3/TS2 + Loop 2100) ---

    @Data
    public static class Loop2000Header {
        @JsonProperty("header-number")
        private LXSegment headerNumber; // LX

        @JsonProperty("provider-summary-info")
        private TS3Segment providerSummaryInfo; // TS3

        @JsonProperty("provider-supplemental-summary")
        private TS2Segment providerSupplementalSummary; // TS2

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("Loop_2100_ClaimPayment")
        private List<Loop2100ClaimPayment> loop2100ClaimPayment; // repeating
    }

    @Data
    public static class Loop2100ClaimPayment {
        @JsonProperty("claim-payment-information")
        private CLPSegment claimPaymentInformation; // CLP

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("claim-adjustment")
        private List<CASSegment> claimAdjustment; // CAS*

        @JsonProperty("patient-name")
        private NM1Segment patientName; // NM1

        @JsonProperty("insured-name")
        private NM1Segment insuredName; // NM1 (optional)

        @JsonProperty("corrected-patient-insured")
        private NM1Segment correctedPatientInsured; // NM1 (optional)

        @JsonProperty("service-provider-name")
        private NM1Segment serviceProviderName; // NM1 (optional)

        @JsonProperty("crossover-carrier-name")
        private NM1Segment crossoverCarrierName; // NM1 (optional)

        @JsonProperty("corrected-priority-payer-name")
        private NM1Segment correctedPriorityPayerName; // NM1 (optional)

        @JsonProperty("inpatient-adjudication")
        private MIASegment inpatientAdjudication; // MIA (optional)

        @JsonProperty("outpatient-adjudication")
        private MOASegment outpatientAdjudication; // MOA (optional)

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("other-claim-related-id")
        private List<REFSegment> otherClaimRelatedId; // REF*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("rendering-provider-id")
        private List<REFSegment> renderingProviderId; // REF*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("statement-from-or-to-date")
        private List<DTMSegment> statementFromOrToDate; // DTM*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("coverage-expiration")
        private List<DTMSegment> coverageExpiration; // DTM*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("claim-received")
        private List<DTMSegment> claimReceived; // DTM*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("claim-contact-info")
        private List<PERSegment> claimContactInfo; // PER*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("claim-level-amounts")
        private List<AMTSegment> claimLevelAmounts; // AMT*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("claim-level-quantity")
        private List<QTYSegment> claimLevelQuantity; // QTY*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("Loop_2110_ServicePayment")
        private List<Loop2110ServicePayment> loop2110ServicePayment; // repeating up to 999
    }

    @Data
    public static class Loop2110ServicePayment {
        @JsonProperty("service-payment-information")
        private SVCSegment servicePaymentInformation; // SVC

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("service-date")
        private List<DTMSegment> serviceDate; // DTM*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("service-adjustment")
        private List<CASSegment> serviceAdjustment; // CAS*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("service-identification")
        private List<REFSegment> serviceIdentification; // REF*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("line-item-control-number")
        private List<REFSegment> lineItemControlNumber; // REF*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("rendering-provider-information")
        private List<REFSegment> renderingProviderInformation; // REF*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("healthcare-policy-identification")
        private List<REFSegment> healthcarePolicyIdentification; // REF*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("service-supplemental-amount")
        private List<AMTSegment> serviceSupplementalAmount; // AMT*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("service-supplemental-quantity")
        private List<QTYSegment> serviceSupplementalQuantity; // QTY*

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("healthcare-remark-codes")
        private List<LQSegment> healthcareRemarkCodes; // LQ*
    }

    // --- Trailers (SE/GE/IEA) ---

    @Data
    public static class TransactionSetTrailer {
        @JsonProperty("number-of-included-segments")
        private String numberOfIncludedSegments; // SE01

        @JsonProperty("transaction-set-control-number")
        private String transactionSetControlNumber; // SE02
    }

    @Data
    public static class FunctionalGroupTrailer {
        @JsonProperty("number-of-transaction-sets")
        private String numberOfTransactionSets; // GE01

        @JsonProperty("group-control-number")
        private String groupControlNumber; // GE02
    }

    @Data
    public static class InterchangeControlTrailer {
        @JsonProperty("number-of-function-groups-included")
        private String numberOfFunctionGroupsIncluded; // IEA01

        @JsonProperty("interchange-control-number")
        private String interchangeControlNumber; // IEA02
    }

    // --- Common segment representations (minimal fields for binding) ---

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
        private String depositoryFinancialInstitutionDfiIdentificationNumberQualifier;

        @JsonProperty("depository-financial-institution-dfi-identification-number")
        private String depositoryFinancialInstitutionDfiIdentificationNumber;

        @JsonProperty("account-number-qualifier")
        private String accountNumberQualifier;

        @JsonProperty("sender-bank-account-number")
        private String senderBankAccountNumber;

        @JsonProperty("originating-company-identifier")
        private String originatingCompanyIdentifier;

        @JsonProperty("originating-company-supplemental-code")
        private String originatingCompanySupplementalCode;

        @JsonProperty("receiver-or-provider-bank-id-number-qualifier")
        private String receiverOrProviderBankIdNumberQualifier;

        @JsonProperty("receiver-or-provider-bank-id-number")
        private String receiverOrProviderBankIdNumber;

        @JsonProperty("receiver-or-provider-account-number-qualifier")
        private String receiverOrProviderAccountNumberQualifier;

        @JsonProperty("receiver-or-provider-account-number")
        private String receiverOrProviderAccountNumber;

        @JsonProperty("date")
        private String date;

        @JsonProperty("business-function-code")
        private String businessFunctionCode;

        @JsonProperty("dfi-id-number-qualifier")
        private String dfiIdNumberQualifier;

        @JsonProperty("dfi-identification-number")
        private String dfiIdentificationNumber;

        @JsonProperty("account-number-qualifier-2")
        private String accountNumberQualifier2;

        @JsonProperty("sender-bank-account-number-2")
        private String senderBankAccountNumber2;
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

        @JsonProperty("date-time-qualifier-2")
        private String dateTimeQualifier2;

        @JsonProperty("date-2")
        private String date2;

        @JsonProperty("time-2")
        private String time2;

        @JsonProperty("date-time-qualifier-3")
        private String dateTimeQualifier3;

        @JsonProperty("date-time-period")
        private String dateTimePeriod;

        @JsonProperty("date-time-period-format-qualifier")
        private String dateTimePeriodFormatQualifier;

        @JsonProperty("date-time-period-2")
        private String dateTimePeriod2;

        @JsonProperty("date-time-period-format-qualifier-2")
        private String dateTimePeriodFormatQualifier2;

        @JsonProperty("date-time-period-3")
        private String dateTimePeriod3;

        @JsonProperty("date-time-period-format-qualifier-3")
        private String dateTimePeriodFormatQualifier3;

        @JsonProperty("date-time-period-4")
        private String dateTimePeriod4;

        @JsonProperty("date-time-period-format-qualifier-4")
        private String dateTimePeriodFormatQualifier4;
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
        private String referenceIdentifier;
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
    public static class N1Segment {
        @JsonProperty("entity-identifier-code")
        private String entityIdentifierCode;

        @JsonProperty("name")
        private String name;

        @JsonProperty("identification-code-qualifier")
        private String identificationCodeQualifier;

        @JsonProperty("identification-code")
        private String identificationCode;
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
    }

    @Data
    public static class RDMSegment {
        @JsonProperty("report-transmission-code")
        private String reportTransmissionCode;

        @JsonProperty("name")
        private String name;
    }

    @Data
    public static class LXSegment {
        @JsonProperty("assigned-number")
        private String assignedNumber;
    }

    @Data
    public static class TS3Segment {
        @JsonProperty("reference-identification")
        private String referenceIdentification;

        @JsonProperty("facility-code-value")
        private String facilityCodeValue;

        @JsonProperty("date")
        private String date;
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

        @JsonProperty("supplemental-amount-6")
        private String supplementalAmount6;

        @JsonProperty("supplemental-amount-7")
        private String supplementalAmount7;

        @JsonProperty("supplemental-amount-8")
        private String supplementalAmount8;

        @JsonProperty("supplemental-amount-9")
        private String supplementalAmount9;

        @JsonProperty("supplemental-amount-10")
        private String supplementalAmount10;

        @JsonProperty("supplemental-amount-11")
        private String supplementalAmount11;

        @JsonProperty("supplemental-amount-12")
        private String supplementalAmount12;

        @JsonProperty("supplemental-amount-13")
        private String supplementalAmount13;

        @JsonProperty("supplemental-amount-14")
        private String supplementalAmount14;

        @JsonProperty("supplemental-amount-15")
        private String supplementalAmount15;

        @JsonProperty("supplemental-amount-16")
        private String supplementalAmount16;

        @JsonProperty("supplemental-amount-17")
        private String supplementalAmount17;

        @JsonProperty("supplemental-amount-18")
        private String supplementalAmount18;

        @JsonProperty("supplemental-amount-19")
        private String supplementalAmount19;
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

        @JsonProperty("claim-adjustment-reason-code-4")
        private String claimAdjustmentReasonCode4;

        @JsonProperty("monetary-amount-4")
        private String monetaryAmount4;

        @JsonProperty("quantity-4")
        private String quantity4;

        @JsonProperty("claim-adjustment-reason-code-5")
        private String claimAdjustmentReasonCode5;

        @JsonProperty("monetary-amount-5")
        private String monetaryAmount5;

        @JsonProperty("quantity-5")
        private String quantity5;

        @JsonProperty("claim-adjustment-reason-code-6")
        private String claimAdjustmentReasonCode6;

        @JsonProperty("monetary-amount-6")
        private String monetaryAmount6;

        @JsonProperty("quantity-6")
        private String quantity6;
    }

    @Data
    public static class NM1Segment {
        @JsonProperty("entity-identifier-code")
        private String entityIdentifierCode;

        @JsonProperty("entity-type-qualifier")
        private String entityTypeQualifier;

        @JsonProperty("name-last-or-organization-name")
        private String nameLastOrOrganizationName;

        @JsonProperty("identification-code")
        private String identificationCode;
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

        @JsonProperty("claim-payment-remark-code-2")
        private String claimPaymentRemarkCode2;

        @JsonProperty("claim-payment-remark-code-3")
        private String claimPaymentRemarkCode3;

        @JsonProperty("claim-payment-remark-code-4")
        private String claimPaymentRemarkCode4;

        @JsonProperty("pps-capital-exception-amount")
        private String ppsCapitalExceptionAmount;

        @JsonProperty("pps-capital-disproportionate-share-drg-amount")
        private String ppsCapitalDisproportionateShareDrgAmount;
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
        private String compositeUnitOfMeasure;

        @JsonProperty("free-form-information")
        private String freeFormInformation;
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
}
