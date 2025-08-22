package org.example.XML;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

import java.util.List;

/**
 * X12 837 Loop 2310A-F Classes for Professional Claims
 * Following X12 5010 specification
 */
public class X12_837_Loop2310_Classes {

    /**
     * Loop 2310A - Referring Provider Name
     */
    @Data
    public static class Loop2310AReferringProviderName {
        @JsonProperty("referring-provider-name")
        private X12_837_Interchange.NM1Segment referringProviderName;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("referring-provider-secondary-id")
        private List<X12_837_Interchange.REFSegment> referringProviderSecondaryId;
    }

    /**
     * Loop 2310B - Rendering Provider Name
     */
    @Data
    public static class Loop2310BRenderingProviderName {
        @JsonProperty("rendering-provider-name")
        private X12_837_Interchange.NM1Segment renderingProviderName;

        @JsonProperty("rendering-provider-specialty-info")
        private X12_837_Interchange.PRVSegment renderingProviderSpecialtyInfo;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("rendering-provider-secondary-id")
        private List<X12_837_Interchange.REFSegment> renderingProviderSecondaryId;
    }

    /**
     * Loop 2310C - Service Facility Location Name
     */
    @Data
    public static class Loop2310CServiceFacilityLocationName {
        @JsonProperty("service-facility-location-name")
        private X12_837_Interchange.NM1Segment serviceFacilityLocationName;

        @JsonProperty("service-facility-location-address")
        private X12_837_Interchange.N3Segment serviceFacilityLocationAddress;

        @JsonProperty("service-facility-location-city-state-zip")
        private X12_837_Interchange.N4Segment serviceFacilityLocationCityStateZip;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("service-facility-location-secondary-id")
        private List<X12_837_Interchange.REFSegment> serviceFacilityLocationSecondaryId;

        @JsonProperty("service-facility-contact-info")
        private X12_837_Interchange.PERSegment serviceFacilityContactInfo;
    }

    /**
     * Loop 2310D - Supervising Provider Name
     */
    @Data
    public static class Loop2310DSupervisingProviderName {
        @JsonProperty("supervising-provider-name")
        private X12_837_Interchange.NM1Segment supervisingProviderName;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("supervising-provider-secondary-id")
        private List<X12_837_Interchange.REFSegment> supervisingProviderSecondaryId;
    }

    /**
     * Loop 2310E - Ambulance Pick-up Location
     */
    @Data
    public static class Loop2310EAmbulancePickupLocation {
        @JsonProperty("ambulance-pickup-location-name")
        private X12_837_Interchange.NM1Segment ambulancePickupLocationName;

        @JsonProperty("ambulance-pickup-address")
        private X12_837_Interchange.N3Segment ambulancePickupAddress;

        @JsonProperty("ambulance-pickup-city-state-zip")
        private X12_837_Interchange.N4Segment ambulancePickupCityStateZip;
    }

    /**
     * Loop 2310F - Ambulance Drop-off Location
     */
    @Data
    public static class Loop2310FAmbulanceDropoffLocation {
        @JsonProperty("ambulance-dropoff-location-name")
        private X12_837_Interchange.NM1Segment ambulanceDropoffLocationName;

        @JsonProperty("ambulance-dropoff-address")
        private X12_837_Interchange.N3Segment ambulanceDropoffAddress;

        @JsonProperty("ambulance-dropoff-city-state-zip")
        private X12_837_Interchange.N4Segment ambulanceDropoffCityStateZip;
    }

    /**
     * Loop 2320 - Other Subscriber Information
     */
    @Data
    public static class Loop2320OtherSubscriberInformation {
        @JsonProperty("subscriber-information")
        private X12_837_Interchange.SBRSegment subscriberInformation;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("claim-adjustment")
        private List<X12_837_Interchange.CASSegment> claimAdjustment;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("other-payer-paid-amount")
        private List<X12_837_Interchange.AMTSegment> otherPayerPaidAmount;

        @JsonProperty("other-insurance-coverage-information")
        private X12_837_Interchange.OISegment otherInsuranceCoverageInformation;

        @JsonProperty("medicare-outpatient-adjudication")
        private X12_837_Interchange.MOASegment medicareOutpatientAdjudication;

        // Loop 2330A - Other Subscriber Name
        @JsonProperty("Loop_2330A_OtherSubscriberName")
        private X12_837_Loop2330_Classes.Loop2330AOtherSubscriberName loop2330AOtherSubscriberName;

        // Loop 2330B - Other Payer Name
        @JsonProperty("Loop_2330B_OtherPayerName")
        private X12_837_Loop2330_Classes.Loop2330BOtherPayerName loop2330BOtherPayerName;

        // Loop 2330C - Other Payer Referring Provider
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("Loop_2330C_OtherPayerReferringProvider")
        private List<X12_837_Loop2330_Classes.Loop2330COtherPayerReferringProvider> loop2330COtherPayerReferringProvider;

        // Loop 2330D - Other Payer Rendering Provider
        @JsonProperty("Loop_2330D_OtherPayerRenderingProvider")
        private X12_837_Loop2330_Classes.Loop2330DOtherPayerRenderingProvider loop2330DOtherPayerRenderingProvider;

        // Loop 2330E - Other Payer Service Facility Location
        @JsonProperty("Loop_2330E_OtherPayerServiceFacilityLocation")
        private X12_837_Loop2330_Classes.Loop2330EOtherPayerServiceFacilityLocation loop2330EOtherPayerServiceFacilityLocation;

        // Loop 2330F - Other Payer Supervising Provider
        @JsonProperty("Loop_2330F_OtherPayerSupervisingProvider")
        private X12_837_Loop2330_Classes.Loop2330FOtherPayerSupervisingProvider loop2330FOtherPayerSupervisingProvider;

        // Loop 2330G - Other Payer Billing Provider
        @JsonProperty("Loop_2330G_OtherPayerBillingProvider")
        private X12_837_Loop2330_Classes.Loop2330GOtherPayerBillingProvider loop2330GOtherPayerBillingProvider;
    }
}