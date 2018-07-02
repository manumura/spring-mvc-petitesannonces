package fr.manu.petitesannonces.dto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author emmanuel.mura
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"success", "challenge_ts", "hostname", "error-codes"})
public class RecaptchaResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("challenge_ts")
    private String challengeTs;

    @JsonProperty("hostname")
    private String hostname;

    @JsonProperty("error-codes")
    private ErrorCode[] errorCodes;

    /**
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @param success the success to set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * @return the challengeTs
     */
    public String getChallengeTs() {
        return challengeTs;
    }

    /**
     * @param challengeTs the challengeTs to set
     */
    public void setChallengeTs(String challengeTs) {
        this.challengeTs = challengeTs;
    }

    /**
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @param hostname the hostname to set
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * @return the errorCodes
     */
    public ErrorCode[] getErrorCodes() {
        return errorCodes;
    }

    /**
     * @param errorCodes the errorCodes to set
     */
    public void setErrorCodes(ErrorCode[] errorCodes) {
        this.errorCodes = errorCodes;
    }
    
    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
    @JsonIgnore
	@Override
	public String toString() {
		return "RecaptchaResponse [success=" + success + ", challengeTs=" + challengeTs + ", hostname=" + hostname
				+ ", errorCodes=" + Arrays.toString(errorCodes) + "]";
	}

	@JsonIgnore
    public boolean hasClientError() {
        ErrorCode[] errors = getErrorCodes();
        if (errors == null) {
            return false;
        }
        for (ErrorCode error : errors) {
            switch (error) {
                case INVALID_RESPONSE:
                case MISSING_RESPONSE:
                    return true;
                default:
                	continue;
            }
        }
        return false;
    }

    public enum ErrorCode {
    	
        MISSING_SECRET,
        INVALID_SECRET,
        MISSING_RESPONSE,
        INVALID_RESPONSE;

        private static Map<String, ErrorCode> errorsMap = new HashMap<String, ErrorCode>();

        static {
            errorsMap.put("missing-input-secret", MISSING_SECRET);
            errorsMap.put("invalid-input-secret", INVALID_SECRET);
            errorsMap.put("missing-input-response", MISSING_RESPONSE);
            errorsMap.put("invalid-input-response", INVALID_RESPONSE);
        }

        @JsonCreator
        public static ErrorCode forValue(String value) {
            return errorsMap.get(value.toLowerCase());
        }
    }
}
