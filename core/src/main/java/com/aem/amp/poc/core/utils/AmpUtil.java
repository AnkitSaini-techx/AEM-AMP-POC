package com.aem.amp.poc.core.utils;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.policies.ContentPolicy;
import com.day.cq.wcm.api.policies.ContentPolicyManager;
import com.drew.lang.annotations.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmpUtil {

    private static final Logger LOG = LoggerFactory.getLogger(AmpUtil.class);

    private static final String AMP_MODE_PROP = "ampMode";

    public static final String AMP_SELECTOR = "amp";

    public static final String DOT = ".";

    /**
     * Retrieves the AMP mode value of the requested resource.
     * @param slingRequest Request used to resolve the resource and AMP mode value from.
     * @return The AMP mode value.
     */
    public static AMP_MODE getAmpMode(@NotNull SlingHttpServletRequest slingRequest) {

        PageManager pageManager = slingRequest.getResourceResolver().adaptTo(PageManager.class);

        if (pageManager != null) {
            Page page = pageManager.getContainingPage(slingRequest.getResource());

            if (page != null) {
                String ampMode = page.getProperties().get(AMP_MODE_PROP, "");
                if (!ampMode.isEmpty()) {
                    return AMP_MODE.fromString(ampMode);
                }
            }
        }

        return AMP_MODE.fromString(getAmpPropertyFromPolicy(slingRequest));
    }

    /**
     * Retrieves the value of the amp property from the request resource's content policy.
     * @param slingRequest The request used to get the resource and its content policy.
     * @return The value of the property of the resource's content policy. Returns empty String if fails to read the content
     * policy.
     */
    @NotNull
    private static String getAmpPropertyFromPolicy(@NotNull SlingHttpServletRequest slingRequest) {

        String ampProperty = StringUtils.EMPTY;
        ContentPolicyManager policyManager = slingRequest.getResourceResolver().adaptTo(ContentPolicyManager.class);
        if (policyManager != null) {
            ContentPolicy contentPolicy = policyManager.getPolicy(slingRequest.getResource());
            if (contentPolicy != null) {
                ampProperty = contentPolicy.getProperties().get(AMP_MODE_PROP, StringUtils.EMPTY);
            }
        }
        return ampProperty;
    }

    public enum AMP_MODE {
        AMP_ONLY("ampOnly"),
        NO_AMP("noAmp"),
        PAIRED_AMP("pairedAmp");

        private String text;

        AMP_MODE(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public static AMP_MODE fromString(String ampMode) {
            for (AMP_MODE mode: AMP_MODE.values()) {
                if (mode.getText().equalsIgnoreCase(ampMode)) {
                    return mode;
                }
            }
            return NO_AMP;
        }
    }

}
