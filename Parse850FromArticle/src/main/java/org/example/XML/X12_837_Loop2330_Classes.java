package org.example.XML;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

import java.util.List;

/**
 * X12 837 Loop 2330A-G Classes for Professional Claims
 * Following X12 5010 specification - Other Payer Name loops
 */
public class X12_837_Loop2330_Classes {

    /**
     * Loop 2330A - Other Subscriber Name
     */
    @Data
    public static class Loop2330AOtherSubscriberName {
        @JsonProperty("other-subscriber-name")
        private X12_837_Interchange.NM1Segment otherSubscriberName;

        @JsonProperty("other-subscriber-address")
        private X12_837_Interchange.N3Segment otherSubscriberAddress;

        @JsonProperty("other-subscriber-city-state-zip")
        private X12_837_Interchange.N4Segment otherSubscriberCityStateZip;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("other-subscriber-secondary-identification")
        private List<X12_837_Interchange.REFSegment> otherSubscriberSecondaryIdentification;
    }

    /**
     * Loop 2330B - Other Payer Name
     */
    @Data
    public static class Loop2330BOtherPayerName {
        @JsonProperty("other-payer-name")
        private X12_837_Interchange.NM1Segment otherPayerName;

        @JsonProperty("other-payer-address")
        private X12_837_Interchange.N3Segment otherPayerAddress;

        @JsonProperty("other-payer-city-state-zip")
        private X12_837_Interchange.N4Segment otherPayerCityStateZip;

        @JsonProperty("claim-check-remittance-date")
        private X12_837_Interchange.DTPSegment claimCheckRemittanceDate;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("other-payer-secondary-identifier")
        private List<X12_837_Interchange.REFSegment> otherPayerSecondaryIdentifier;

        @JsonProperty("other-payer-prior-authorization-number")
        private X12_837_Interchange.REFSegment otherPayerPriorAuthorizationNumber;

        @JsonProperty("other-payer-referral-number")
        private X12_837_Interchange.REFSegment otherPayerReferralNumber;

        @JsonProperty("other-payer-claim-adjustment-indicator")
        private X12_837_Interchange.REFSegment otherPayerClaimAdjustmentIndicator;

        @JsonProperty("other-payer-claim-control-number")
        private X12_837_Interchange.REFSegment otherPayerClaimControlNumber;

        @JsonProperty("other-payer-contact-information")
        private X12_837_Interchange.PERSegment otherPayerContactInformation;
    }

    /**
     * Loop 2330C - Other Payer Referring Provider
     */
    @Data
    public static class Loop2330COtherPayerReferringProvider {
        @JsonProperty("other-payer-referring-provider")
        private X12_837_Interchange.NM1Segment otherPayerReferringProvider;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("other-payer-referring-provider-secondary-identifier")
        private List<X12_837_Interchange.REFSegment> otherPayerReferringProviderSecondaryIdentifier;
    }

    /**
     * Loop 2330D - Other Payer Rendering Provider
     */
    @Data
    public static class Loop2330DOtherPayerRenderingProvider {
        @JsonProperty("other-payer-rendering-provider")
        private X12_837_Interchange.NM1Segment otherPayerRenderingProvider;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("other-payer-rendering-provider-secondary-identifier")
        private List<X12_837_Interchange.REFSegment> otherPayerRenderingProviderSecondaryIdentifier;
    }

    /**
     * Loop 2330E - Other Payer Service Facility Location
     */
    @Data
    public static class Loop2330EOtherPayerServiceFacilityLocation {
        @JsonProperty("other-payer-service-facility-location")
        private X12_837_Interchange.NM1Segment otherPayerServiceFacilityLocation;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("other-payer-service-facility-location-secondary-identifier")
        private List<X12_837_Interchange.REFSegment> otherPayerServiceFacilityLocationSecondaryIdentifier;
    }

    /**
     * Loop 2330F - Other Payer Supervising Provider
     */
    @Data
    public static class Loop2330FOtherPayerSupervisingProvider {
        @JsonProperty("other-payer-supervising-provider")
        private X12_837_Interchange.NM1Segment otherPayerSupervisingProvider;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("other-payer-supervising-provider-secondary-identifier")
        private List<X12_837_Interchange.REFSegment> otherPayerSupervisingProviderSecondaryIdentifier;
    }

    /**
     * Loop 2330G - Other Payer Billing Provider
     */
    @Data
    public static class Loop2330GOtherPayerBillingProvider {
        @JsonProperty("other-payer-billing-provider")
        private X12_837_Interchange.NM1Segment otherPayerBillingProvider;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("other-payer-billing-provider-secondary-identifier")
        private List<X12_837_Interchange.REFSegment> otherPayerBillingProviderSecondaryIdentifier;
    }
}