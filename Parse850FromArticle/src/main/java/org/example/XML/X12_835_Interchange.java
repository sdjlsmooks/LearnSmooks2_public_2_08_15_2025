package org.example.XML;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

import java.util.List;

/**
 * Java representation of an X12 835 interchange structure.
 *
 * This POJO mirrors the key structure defined by src/main/resources/835_mapping.dfdl.xsd
 * and follows the same style as X12_850_Interchange for consistency.
 *
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
        @JsonProperty("BPR01") private String bpr01;
        @JsonProperty("BPR02") private String bpr02;
        @JsonProperty("BPR03") private String bpr03;
        @JsonProperty("BPR04") private String bpr04;
    }

    @Data
    public static class TRNSegment {
        @JsonProperty("TRN01") private String trn01;
        @JsonProperty("TRN02") private String trn02;
        @JsonProperty("TRN03") private String trn03;
    }

    @Data
    public static class CURSegment {
        @JsonProperty("entity-identifier-code") private String entityIdentifierCode;
        @JsonProperty("currency-code") private String currencyCode;
        @JsonProperty("exchange-rate") private String exchangeRate;
        @JsonProperty("entity-identifier-code-2") private String entityIdentifierCode2;
        @JsonProperty("currency-code-2") private String currencyCode2;
        @JsonProperty("currency-market-exchange-code") private String currencyMarketExchangeCode;
        @JsonProperty("date-time-qualifier") private String dateTimeQualifier;
        @JsonProperty("date") private String date;
        @JsonProperty("time") private String time;
        @JsonProperty("date-time-qualifier-2") private String dateTimeQualifier2;
        @JsonProperty("date-2") private String date2;
        @JsonProperty("time-2") private String time2;
        @JsonProperty("date-time-qualifier-3") private String dateTimeQualifier3;
        @JsonProperty("date-time-period") private String dateTimePeriod;
        @JsonProperty("date-time-period-format-qualifier") private String dateTimePeriodFormatQualifier;
        @JsonProperty("date-time-period-2") private String dateTimePeriod2;
        @JsonProperty("date-time-period-format-qualifier-2") private String dateTimePeriodFormatQualifier2;
        @JsonProperty("date-time-period-3") private String dateTimePeriod3;
        @JsonProperty("date-time-period-format-qualifier-3") private String dateTimePeriodFormatQualifier3;
        @JsonProperty("date-time-period-4") private String dateTimePeriod4;
        @JsonProperty("date-time-period-format-qualifier-4") private String dateTimePeriodFormatQualifier4;
    }

    @Data
    public static class REFSegment {
        @JsonProperty("REF01") private String ref01;
        @JsonProperty("REF02") private String ref02;
        @JsonProperty("REF03") private String ref03;
    }

    @Data
    public static class DTMSegment {
        @JsonProperty("DTM01") private String dtm01;
        @JsonProperty("DTM02") private String dtm02;
        @JsonProperty("DTM03") private String dtm03;
    }

    @Data
    public static class N1Segment {
        @JsonProperty("N101") private String n101;
        @JsonProperty("N102") private String n102;
        @JsonProperty("N103") private String n103;
        @JsonProperty("N104") private String n104;
    }

    @Data
    public static class N3Segment {
        @JsonProperty("N301") private String n301;
        @JsonProperty("N302") private String n302;
    }

    @Data
    public static class N4Segment {
        @JsonProperty("N401") private String n401;
        @JsonProperty("N402") private String n402;
        @JsonProperty("N403") private String n403;
        @JsonProperty("N404") private String n404;
    }

    @Data
    public static class PERSegment {
        @JsonProperty("PER01") private String per01;
        @JsonProperty("PER02") private String per02;
        @JsonProperty("PER03") private String per03;
        @JsonProperty("PER04") private String per04;
    }

    @Data
    public static class RDMSegment {
        @JsonProperty("RDM01") private String rdm01;
        @JsonProperty("RDM02") private String rdm02;
    }

    @Data
    public static class LXSegment {
        @JsonProperty("LX01") private String lx01;
    }

    @Data
    public static class TS3Segment {
        @JsonProperty("TS301") private String ts301;
        @JsonProperty("TS302") private String ts302;
        @JsonProperty("TS303") private String ts303;
    }

    @Data
    public static class TS2Segment {
        @JsonProperty("TS201") private String ts201;
        @JsonProperty("TS202") private String ts202;
        @JsonProperty("TS203") private String ts203;
    }

    @Data
    public static class CLPSegment {
        @JsonProperty("CLP01") private String clp01;
        @JsonProperty("CLP02") private String clp02;
        @JsonProperty("CLP03") private String clp03;
        @JsonProperty("CLP04") private String clp04;
    }

    @Data
    public static class CASSegment {
        @JsonProperty("CAS01") private String cas01;
        @JsonProperty("CAS02") private String cas02;
        @JsonProperty("CAS03") private String cas03;
    }

    @Data
    public static class NM1Segment {
        @JsonProperty("NM101") private String nm101;
        @JsonProperty("NM102") private String nm102;
        @JsonProperty("NM103") private String nm103;
        @JsonProperty("NM109") private String nm109;
    }

    @Data
    public static class MIASegment {
        @JsonProperty("MIA01") private String mia01;
    }

    @Data
    public static class MOASegment {
        @JsonProperty("MOA01") private String moa01;
    }

    @Data
    public static class AMTSegment {
        @JsonProperty("AMT01") private String amt01;
        @JsonProperty("AMT02") private String amt02;
    }

    @Data
    public static class QTYSegment {
        @JsonProperty("QTY01") private String qty01;
        @JsonProperty("QTY02") private String qty02;
    }

    @Data
    public static class SVCSegment {
        @JsonProperty("SVC01") private String svc01;
        @JsonProperty("SVC02") private String svc02;
        @JsonProperty("SVC03") private String svc03;
    }

    @Data
    public static class LQSegment {
        @JsonProperty("code-list-qualifier-code") private String codeListQualifierCode;
        @JsonProperty("industry-code") private String industryCode;
    }

    @Data
    public static class PLBSegment {
        @JsonProperty("PLB01") private String plb01;
        @JsonProperty("PLB02") private String plb02;
        @JsonProperty("PLB04") private String plb04;
    }
}
