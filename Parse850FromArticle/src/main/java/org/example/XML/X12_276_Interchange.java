package org.example.XML;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

import java.util.List;

/**
 * Java representation of an X12 276 Health Care Claim Status Request interchange structure.
 * <p>
 * This POJO mirrors the key structure defined by src/main/resources/276_mapping.dfdl.xsd
 * and follows the same style as X12_835_Interchange for consistency.
 * <p>
 * It is intended for convenient JSON/XML binding (e.g., via Jackson) if/when
 * the application needs to work with the parsed 276 XML as a typed object graph.
 */
@Data
@JsonRootName("X12_276_Interchange")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class X12_276_Interchange {

    // Top-level segments
    @JsonProperty("interchange-header")
    private InterchangeHeader interchangeHeader;

    @JsonProperty("group-header")
    private GroupHeader groupHeader;

    @JsonProperty("transaction-set-header")
    private TransactionSetHeader transactionSetHeader;

    @JsonProperty("beginning-hierarchical-transaction")
    private BeginningHierarchicalTransaction beginningHierarchicalTransaction;

    @JsonProperty("Loop_2000A_InformationSourceDetail")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Loop2000A> loop2000A;

    @JsonProperty("transaction-set-trailer")
    private TransactionSetTrailer transactionSetTrailer;

    @JsonProperty("group-trailer")
    private GroupTrailer groupTrailer;

    @JsonProperty("interchange-trailer")
    private InterchangeTrailer interchangeTrailer;

    // --- Headers (ISA/GS/ST/BHT) ---

    /**
     * Represents the ISA (Interchange Control Header) segment of an X12 276 transaction.
     */
    @Data
    public static class InterchangeHeader {
        @JsonProperty("authorization-info-qualifier")
        private String authorizationInfoQualifier;

        @JsonProperty("authorization-info")
        private String authorizationInfo;

        @JsonProperty("security-info-qualifier")
        private String securityInfoQualifier;

        @JsonProperty("security-info")
        private String securityInfo;

        @JsonProperty("interchange-sender-id-qualifier")
        private String interchangeSenderIdQualifier;

        @JsonProperty("interchange-sender-id")
        private String interchangeSenderId;

        @JsonProperty("interchange-receiver-id-qualifier")
        private String interchangeReceiverIdQualifier;

        @JsonProperty("interchange-receiver-id")
        private String interchangeReceiverId;

        @JsonProperty("interchange-date")
        private String interchangeDate;

        @JsonProperty("interchange-time")
        private String interchangeTime;

        @JsonProperty("repetition-separator")
        private String repetitionSeparator;

        @JsonProperty("interchange-control-version")
        private String interchangeControlVersion;

        @JsonProperty("interchange-control-number")
        private String interchangeControlNumber;

        @JsonProperty("acknowledgment-requested")
        private String acknowledgmentRequested;

        @JsonProperty("usage-indicator")
        private String usageIndicator;

        @JsonProperty("component-separator")
        private String componentSeparator;
    }

    /**
     * Represents the GS (Functional Group Header) segment of an X12 276 transaction.
     */
    @Data
    public static class GroupHeader {
        @JsonProperty("functional-group-id")
        private String functionalGroupId;

        @JsonProperty("application-sender-code")
        private String applicationSenderCode;

        @JsonProperty("application-receiver-code")
        private String applicationReceiverCode;

        @JsonProperty("group-date")
        private String groupDate;

        @JsonProperty("group-time")
        private String groupTime;

        @JsonProperty("group-control-number")
        private String groupControlNumber;

        @JsonProperty("responsible-agency-code")
        private String responsibleAgencyCode;

        @JsonProperty("version-release-industry-code")
        private String versionReleaseIndustryCode;
    }

    /**
     * Represents the ST (Transaction Set Header) segment of an X12 276 transaction.
     */
    @Data
    public static class TransactionSetHeader {
        @JsonProperty("transaction-set-id")
        private String transactionSetId;

        @JsonProperty("transaction-set-control-number")
        private String transactionSetControlNumber;

        @JsonProperty("implementation-convention-reference")
        private String implementationConventionReference;
    }

    /**
     * Represents the BHT (Beginning of Hierarchical Transaction) segment.
     */
    @Data
    public static class BeginningHierarchicalTransaction {
        @JsonProperty("hierarchical-structure-code")
        private String hierarchicalStructureCode;

        @JsonProperty("transaction-set-purpose-code")
        private String transactionSetPurposeCode;

        @JsonProperty("reference-identification")
        private String referenceIdentification;

        @JsonProperty("transaction-creation-date")
        private String transactionCreationDate;

        @JsonProperty("transaction-creation-time")
        private String transactionCreationTime;

        @JsonProperty("transaction-type-code")
        private String transactionTypeCode;
    }

    // --- Loop 2000A - Information Source Detail ---
    
    @Data
    public static class Loop2000A {
        @JsonProperty("information-source-level")
        private HierarchicalLevel informationSourceLevel;

        @JsonProperty("Loop_2100A_InformationSourceName")
        private Loop2100A loop2100A;

        @JsonProperty("Loop_2000B_InformationReceiverDetail")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Loop2000B> loop2000B;
    }

    @Data
    public static class HierarchicalLevel {
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
    public static class Loop2100A {
        @JsonProperty("information-source-name")
        private NameSegment informationSourceName;

        @JsonProperty("information-source-contact-info")
        private ContactInfo informationSourceContactInfo;
    }

    // --- Loop 2000B - Information Receiver Detail ---
    
    @Data
    public static class Loop2000B {
        @JsonProperty("information-receiver-level")
        private HierarchicalLevel informationReceiverLevel;

        @JsonProperty("Loop_2100B_InformationReceiverName")
        private Loop2100B loop2100B;

        @JsonProperty("Loop_2000C_ServiceProviderDetail")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Loop2000C> loop2000C;
    }

    @Data
    public static class Loop2100B {
        @JsonProperty("information-receiver-name")
        private NameSegment informationReceiverName;

        @JsonProperty("information-receiver-additional-id")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<ReferenceSegment> informationReceiverAdditionalId;
    }

    // --- Loop 2000C - Service Provider Detail ---
    
    @Data
    public static class Loop2000C {
        @JsonProperty("service-provider-level")
        private HierarchicalLevel serviceProviderLevel;

        @JsonProperty("Loop_2100C_ServiceProviderName")
        private Loop2100C loop2100C;

        @JsonProperty("Loop_2000D_SubscriberDetail")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Loop2000D> loop2000D;
    }

    @Data
    public static class Loop2100C {
        @JsonProperty("service-provider-name")
        private NameSegment serviceProviderName;

        @JsonProperty("provider-secondary-id")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<ReferenceSegment> providerSecondaryId;
    }

    // --- Loop 2000D - Subscriber Detail ---
    
    @Data
    public static class Loop2000D {
        @JsonProperty("subscriber-level")
        private HierarchicalLevel subscriberLevel;

        @JsonProperty("subscriber-demographic-info")
        private DemographicSegment subscriberDemographicInfo;

        @JsonProperty("Loop_2100D_SubscriberName")
        private Loop2100D loop2100D;

        @JsonProperty("Loop_2200D_ClaimStatusTracking")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Loop2200D> loop2200D;

        @JsonProperty("Loop_2000E_DependentDetail")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Loop2000E> loop2000E;
    }

    @Data
    public static class Loop2100D {
        @JsonProperty("subscriber-name")
        private NameSegment subscriberName;

        @JsonProperty("subscriber-additional-id")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<ReferenceSegment> subscriberAdditionalId;
    }

    @Data
    public static class Loop2200D {
        @JsonProperty("claim-status-tracking-number")
        private TrackingSegment claimStatusTrackingNumber;

        @JsonProperty("payer-claim-control-number")
        private ReferenceSegment payerClaimControlNumber;

        @JsonProperty("institutional-bill-type-id")
        private ReferenceSegment institutionalBillTypeId;

        @JsonProperty("application-or-location-system-id")
        private ReferenceSegment applicationOrLocationSystemId;

        @JsonProperty("group-number")
        private ReferenceSegment groupNumber;

        @JsonProperty("patient-control-number")
        private ReferenceSegment patientControlNumber;

        @JsonProperty("pharmacy-prescription-number")
        private ReferenceSegment pharmacyPrescriptionNumber;

        @JsonProperty("claim-submitted-charges")
        private AmountSegment claimSubmittedCharges;

        @JsonProperty("claim-service-date")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<DateSegment> claimServiceDate;

        @JsonProperty("Loop_2210D_ServiceLineInfo")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Loop2210D> loop2210D;
    }

    @Data
    public static class Loop2210D {
        @JsonProperty("service-line-info")
        private ServiceSegment serviceLineInfo;

        @JsonProperty("service-line-item-id")
        private ReferenceSegment serviceLineItemId;

        @JsonProperty("service-line-date")
        private DateSegment serviceLineDate;
    }

    // --- Loop 2000E - Dependent Detail ---
    
    @Data
    public static class Loop2000E {
        @JsonProperty("dependent-level")
        private HierarchicalLevel dependentLevel;

        @JsonProperty("dependent-demographic-info")
        private DemographicSegment dependentDemographicInfo;

        @JsonProperty("Loop_2100E_DependentName")
        private Loop2100E loop2100E;

        @JsonProperty("Loop_2200E_DependentClaimStatusTracking")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Loop2200E> loop2200E;
    }

    @Data
    public static class Loop2100E {
        @JsonProperty("dependent-name")
        private NameSegment dependentName;

        @JsonProperty("dependent-additional-id")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<ReferenceSegment> dependentAdditionalId;
    }

    @Data
    public static class Loop2200E {
        @JsonProperty("dependent-claim-status-tracking-number")
        private TrackingSegment dependentClaimStatusTrackingNumber;

        @JsonProperty("dependent-payer-claim-control-number")
        private ReferenceSegment dependentPayerClaimControlNumber;

        @JsonProperty("dependent-institutional-bill-type-id")
        private ReferenceSegment dependentInstitutionalBillTypeId;

        @JsonProperty("dependent-application-or-location-system-id")
        private ReferenceSegment dependentApplicationOrLocationSystemId;

        @JsonProperty("dependent-group-number")
        private ReferenceSegment dependentGroupNumber;

        @JsonProperty("dependent-patient-control-number")
        private ReferenceSegment dependentPatientControlNumber;

        @JsonProperty("dependent-pharmacy-prescription-number")
        private ReferenceSegment dependentPharmacyPrescriptionNumber;

        @JsonProperty("dependent-claim-submitted-charges")
        private AmountSegment dependentClaimSubmittedCharges;

        @JsonProperty("dependent-claim-service-date")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<DateSegment> dependentClaimServiceDate;

        @JsonProperty("Loop_2210E_DependentServiceLineInfo")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Loop2210E> loop2210E;
    }

    @Data
    public static class Loop2210E {
        @JsonProperty("dependent-service-line-info")
        private ServiceSegment dependentServiceLineInfo;

        @JsonProperty("dependent-service-line-item-id")
        private ReferenceSegment dependentServiceLineItemId;

        @JsonProperty("dependent-service-line-date")
        private DateSegment dependentServiceLineDate;
    }

    // --- Common Segment Definitions ---

    @Data
    public static class NameSegment {
        @JsonProperty("entity-identifier-code")
        private String entityIdentifierCode;

        @JsonProperty("entity-type-qualifier")
        private String entityTypeQualifier;

        @JsonProperty("name-last-or-organization")
        private String nameLastOrOrganization;

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
    }

    @Data
    public static class ContactInfo {
        @JsonProperty("contact-function-code")
        private String contactFunctionCode;

        @JsonProperty("contact-name")
        private String contactName;

        @JsonProperty("communication-number-qualifier-1")
        private String communicationNumberQualifier1;

        @JsonProperty("communication-number-1")
        private String communicationNumber1;

        @JsonProperty("communication-number-qualifier-2")
        private String communicationNumberQualifier2;

        @JsonProperty("communication-number-2")
        private String communicationNumber2;

        @JsonProperty("communication-number-qualifier-3")
        private String communicationNumberQualifier3;

        @JsonProperty("communication-number-3")
        private String communicationNumber3;
    }

    @Data
    public static class ReferenceSegment {
        @JsonProperty("reference-identification-qualifier")
        private String referenceIdentificationQualifier;

        @JsonProperty("reference-identification")
        private String referenceIdentification;

        @JsonProperty("description")
        private String description;
    }

    @Data
    public static class DemographicSegment {
        @JsonProperty("date-time-period-format-qualifier")
        private String dateTimePeriodFormatQualifier;

        @JsonProperty("date-of-birth")
        private String dateOfBirth;

        @JsonProperty("gender-code")
        private String genderCode;
    }

    @Data
    public static class TrackingSegment {
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
    public static class AmountSegment {
        @JsonProperty("amount-qualifier-code")
        private String amountQualifierCode;

        @JsonProperty("monetary-amount")
        private String monetaryAmount;
    }

    @Data
    public static class DateSegment {
        @JsonProperty("date-time-qualifier")
        private String dateTimeQualifier;

        @JsonProperty("date-time-period-format-qualifier")
        private String dateTimePeriodFormatQualifier;

        @JsonProperty("date-time-period")
        private String dateTimePeriod;
    }

    @Data
    public static class ServiceSegment {
        @JsonProperty("composite-medical-procedure")
        private String compositeMedicalProcedure;

        @JsonProperty("monetary-amount")
        private String monetaryAmount;

        @JsonProperty("monetary-amount-2")
        private String monetaryAmount2;

        @JsonProperty("product-service-id")
        private String productServiceId;

        @JsonProperty("quantity")
        private String quantity;
    }

    // --- Trailers ---

    @Data
    public static class TransactionSetTrailer {
        @JsonProperty("number-of-segments")
        private String numberOfSegments;

        @JsonProperty("transaction-set-control-number")
        private String transactionSetControlNumber;
    }

    @Data
    public static class GroupTrailer {
        @JsonProperty("number-of-transaction-sets")
        private String numberOfTransactionSets;

        @JsonProperty("group-control-number")
        private String groupControlNumber;
    }

    @Data
    public static class InterchangeTrailer {
        @JsonProperty("number-of-functional-groups")
        private String numberOfFunctionalGroups;

        @JsonProperty("interchange-control-number")
        private String interchangeControlNumber;
    }
}