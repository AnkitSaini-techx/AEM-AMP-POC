package com.aem.amp.poc.core.models;

import org.osgi.annotation.versioning.ConsumerType;

import java.util.Collections;
import java.util.Map;

/**
 * Defines the {@code AmpPage} Sling Model used for AMP extension for the {@code /apps/core/wcm/components/page/v2/page} component.
 *
 * @since com.adobe.cq.wcm.core.components.models 12.14.0
 */
@ConsumerType
public interface AmpPage {

    /**
     * Getter for the relative page link to either the AMP version or base HTML version of the page.
     *
     * @return the page link
     */
    default Map<String, String> getPageLinkAttrs() {
        return Collections.emptyMap();
    }

    /**
     * Getter for whether the current request has the AMP selector
     *
     * @return {@code true} if the current request has the AMP selector, {@code false} otherwise
     */
    default boolean isAmpSelector() {
        return false;
    }

    /**
     * Getter for whether the current page is AMP enabled.
     *
     * @return boolean for AMP enabled
     */
    default boolean isAmpEnabled() {
        return false;
    }
}