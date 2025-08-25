package org.example.XML;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

import java.util.List;

/**
 * Loop 2430 and 2440 classes for X12 837 Healthcare Claim.
 * Loop 2430 - Line Adjudication Information
 * Loop 2440 - Form Identification Code
 */
public class X12_837_Loop2430_2440_Classes {

    /**
     * Loop 2430 - Line Adjudication Information
     * Contains information about how the service line was adjudicated by other payers
     */
    @Data
    public static class Loop2430LineAdjudicationInformation {
        @JsonProperty("line-adjudication-information")
        private SVDSegment lineAdjudicationInformation;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("line-adjustment")
        private List<X12_837_Interchange.CASSegment> lineAdjustment;
        
        @JsonProperty("line-check-or-remittance-date")
        private X12_837_Interchange.DTPSegment lineCheckOrRemittanceDate;
        
        @JsonProperty("remaining-patient-liability")
        private X12_837_Interchange.AMTSegment remainingPatientLiability;
    }
    
    /**
     * Loop 2440 - Form Identification Code
     * Contains supporting documentation codes and responses
     */
    @Data
    public static class Loop2440FormIdentificationCode {
        @JsonProperty("form-identification-code")
        private X12_837_Interchange.LQSegment formIdentificationCode;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("supporting-documentation")
        private List<FRMSegment> supportingDocumentation;
    }
    
    /**
     * SVD - Service Line Adjudication Information Segment
     */
    @Data
    public static class SVDSegment {
        @JsonProperty("other-payer-primary-identifier")
        private String otherPayerPrimaryIdentifier;
        
        @JsonProperty("service-line-paid-amount")
        private String serviceLinePaidAmount;
        
        @JsonProperty("composite-medical-procedure-identifier")
        private String compositeMedicalProcedureIdentifier;
        
        @JsonProperty("product-service-id")
        private String productServiceId;
        
        @JsonProperty("paid-service-unit-count")
        private String paidServiceUnitCount;
        
        @JsonProperty("bundled-or-unbundled-line-number")
        private String bundledOrUnbundledLineNumber;
    }
    
    /**
     * FRM - Supporting Documentation Segment
     */
    @Data
    public static class FRMSegment {
        @JsonProperty("question-number-letter")
        private String questionNumberLetter;
        
        @JsonProperty("question-response")
        private String questionResponse;
        
        @JsonProperty("question-response-2")
        private String questionResponse2;
        
        @JsonProperty("question-response-3")
        private String questionResponse3;
        
        @JsonProperty("question-response-4")
        private String questionResponse4;
        
        @JsonProperty("question-response-5")
        private String questionResponse5;
    }
}