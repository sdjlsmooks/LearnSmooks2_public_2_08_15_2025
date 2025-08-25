package org.example.XML;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

import java.util.List;

/**
 * Loop 2410 and 2420 classes for X12 837 Healthcare Claim.
 * Loop 2410 - Drug Identification
 * Loop 2420A-H - Service Line Level Provider Information
 */
public class X12_837_Loop2410_2420_Classes {

    /**
     * Loop 2410 - Drug Identification
     */
    @Data
    public static class Loop2410DrugIdentification {
        @JsonProperty("drug-identification")
        private LINSegment drugIdentification;
        
        @JsonProperty("drug-quantity")
        private CTPSegment drugQuantity;
        
        @JsonProperty("prescription-number")
        private X12_837_Interchange.REFSegment prescriptionNumber;
    }
    
    /**
     * Loop 2420A - Rendering Provider Name
     */
    @Data
    public static class Loop2420ARenderingProviderName {
        @JsonProperty("rendering-provider-name")
        private X12_837_Interchange.NM1Segment renderingProviderName;
        
        @JsonProperty("rendering-provider-specialty")
        private X12_837_Interchange.PRVSegment renderingProviderSpecialty;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("rendering-provider-secondary-id")
        private List<X12_837_Interchange.REFSegment> renderingProviderSecondaryId;
    }
    
    /**
     * Loop 2420B - Purchased Service Provider Name
     */
    @Data
    public static class Loop2420BPurchasedServiceProviderName {
        @JsonProperty("purchased-service-provider-name")
        private X12_837_Interchange.NM1Segment purchasedServiceProviderName;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("purchased-service-provider-secondary-id")
        private List<X12_837_Interchange.REFSegment> purchasedServiceProviderSecondaryId;
    }
    
    /**
     * Loop 2420C - Service Facility Location Name
     */
    @Data
    public static class Loop2420CServiceFacilityLocationName {
        @JsonProperty("service-facility-location-name")
        private X12_837_Interchange.NM1Segment serviceFacilityLocationName;
        
        @JsonProperty("service-facility-location-address")
        private X12_837_Interchange.N3Segment serviceFacilityLocationAddress;
        
        @JsonProperty("service-facility-location-city-state-zip")
        private X12_837_Interchange.N4Segment serviceFacilityLocationCityStateZip;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("service-facility-location-secondary-id")
        private List<X12_837_Interchange.REFSegment> serviceFacilityLocationSecondaryId;
    }
    
    /**
     * Loop 2420D - Supervising Provider Name
     */
    @Data
    public static class Loop2420DSupervisingProviderName {
        @JsonProperty("supervising-provider-name")
        private X12_837_Interchange.NM1Segment supervisingProviderName;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("supervising-provider-secondary-id")
        private List<X12_837_Interchange.REFSegment> supervisingProviderSecondaryId;
    }
    
    /**
     * Loop 2420E - Ordering Provider Name
     */
    @Data
    public static class Loop2420EOrderingProviderName {
        @JsonProperty("ordering-provider-name")
        private X12_837_Interchange.NM1Segment orderingProviderName;
        
        @JsonProperty("ordering-provider-address")
        private X12_837_Interchange.N3Segment orderingProviderAddress;
        
        @JsonProperty("ordering-provider-city-state-zip")
        private X12_837_Interchange.N4Segment orderingProviderCityStateZip;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("ordering-provider-secondary-id")
        private List<X12_837_Interchange.REFSegment> orderingProviderSecondaryId;
        
        @JsonProperty("ordering-provider-contact-info")
        private X12_837_Interchange.PERSegment orderingProviderContactInfo;
    }
    
    /**
     * Loop 2420F - Referring Provider Name
     */
    @Data
    public static class Loop2420FReferringProviderName {
        @JsonProperty("referring-provider-name")
        private X12_837_Interchange.NM1Segment referringProviderName;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("referring-provider-secondary-id")
        private List<X12_837_Interchange.REFSegment> referringProviderSecondaryId;
    }
    
    /**
     * Loop 2420G - Ambulance Pick-up Location
     */
    @Data
    public static class Loop2420GAmbulancePickupLocation {
        @JsonProperty("ambulance-pickup-location")
        private X12_837_Interchange.NM1Segment ambulancePickupLocation;
        
        @JsonProperty("ambulance-pickup-address")
        private X12_837_Interchange.N3Segment ambulancePickupAddress;
        
        @JsonProperty("ambulance-pickup-city-state-zip")
        private X12_837_Interchange.N4Segment ambulancePickupCityStateZip;
    }
    
    /**
     * Loop 2420H - Ambulance Drop-off Location
     */
    @Data
    public static class Loop2420HAmbulanceDropoffLocation {
        @JsonProperty("ambulance-dropoff-location")
        private X12_837_Interchange.NM1Segment ambulanceDropoffLocation;
        
        @JsonProperty("ambulance-dropoff-address")
        private X12_837_Interchange.N3Segment ambulanceDropoffAddress;
        
        @JsonProperty("ambulance-dropoff-city-state-zip")
        private X12_837_Interchange.N4Segment ambulanceDropoffCityStateZip;
    }
    
    /**
     * LIN - Item Identification Segment
     */
    @Data
    public static class LINSegment {
        @JsonProperty("assigned-identification")
        private String assignedIdentification;
        
        @JsonProperty("product-service-id-qualifier")
        private String productServiceIdQualifier;
        
        @JsonProperty("product-service-id")
        private String productServiceId;
        
        @JsonProperty("product-service-id-qualifier-2")
        private String productServiceIdQualifier2;
        
        @JsonProperty("product-service-id-2")
        private String productServiceId2;
        
        @JsonProperty("product-service-id-qualifier-3")
        private String productServiceIdQualifier3;
        
        @JsonProperty("product-service-id-3")
        private String productServiceId3;
        
        @JsonProperty("product-service-id-qualifier-4")
        private String productServiceIdQualifier4;
        
        @JsonProperty("product-service-id-4")
        private String productServiceId4;
        
        @JsonProperty("product-service-id-qualifier-5")
        private String productServiceIdQualifier5;
        
        @JsonProperty("product-service-id-5")
        private String productServiceId5;
        
        @JsonProperty("product-service-id-qualifier-6")
        private String productServiceIdQualifier6;
        
        @JsonProperty("product-service-id-6")
        private String productServiceId6;
        
        @JsonProperty("product-service-id-qualifier-7")
        private String productServiceIdQualifier7;
        
        @JsonProperty("product-service-id-7")
        private String productServiceId7;
        
        @JsonProperty("product-service-id-qualifier-8")
        private String productServiceIdQualifier8;
        
        @JsonProperty("product-service-id-8")
        private String productServiceId8;
        
        @JsonProperty("product-service-id-qualifier-9")
        private String productServiceIdQualifier9;
        
        @JsonProperty("product-service-id-9")
        private String productServiceId9;
        
        @JsonProperty("product-service-id-qualifier-10")
        private String productServiceIdQualifier10;
        
        @JsonProperty("product-service-id-10")
        private String productServiceId10;
        
        @JsonProperty("product-service-id-qualifier-11")
        private String productServiceIdQualifier11;
        
        @JsonProperty("product-service-id-11")
        private String productServiceId11;
        
        @JsonProperty("product-service-id-qualifier-12")
        private String productServiceIdQualifier12;
        
        @JsonProperty("product-service-id-12")
        private String productServiceId12;
        
        @JsonProperty("product-service-id-qualifier-13")
        private String productServiceIdQualifier13;
        
        @JsonProperty("product-service-id-13")
        private String productServiceId13;
        
        @JsonProperty("product-service-id-qualifier-14")
        private String productServiceIdQualifier14;
        
        @JsonProperty("product-service-id-14")
        private String productServiceId14;
        
        @JsonProperty("product-service-id-qualifier-15")
        private String productServiceIdQualifier15;
        
        @JsonProperty("product-service-id-15")
        private String productServiceId15;
    }
    
    /**
     * CTP - Pricing Information Segment
     */
    @Data
    public static class CTPSegment {
        @JsonProperty("class-of-trade-code")
        private String classOfTradeCode;
        
        @JsonProperty("price-identifier-code")
        private String priceIdentifierCode;
        
        @JsonProperty("unit-price")
        private String unitPrice;
        
        @JsonProperty("quantity")
        private String quantity;
        
        @JsonProperty("composite-unit-of-measure")
        private String compositeUnitOfMeasure;
        
        @JsonProperty("price-multiplier-qualifier")
        private String priceMultiplierQualifier;
        
        @JsonProperty("multiplier")
        private String multiplier;
        
        @JsonProperty("monetary-amount")
        private String monetaryAmount;
        
        @JsonProperty("basis-of-unit-price-code")
        private String basisOfUnitPriceCode;
        
        @JsonProperty("condition-value")
        private String conditionValue;
        
        @JsonProperty("multiple-price-quantity")
        private String multiplePriceQuantity;
    }
}