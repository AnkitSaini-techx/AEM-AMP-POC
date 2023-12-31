package com.aem.amp.poc.core.models.impl;

import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.Page;
import com.aem.amp.poc.core.models.AmpPage;
import com.aem.amp.poc.core.utils.AmpUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.aem.amp.poc.core.utils.AmpUtil.AMP_SELECTOR;
import static com.aem.amp.poc.core.utils.AmpUtil.DOT;
import static com.day.cq.wcm.foundation.List.URL_EXTENSION;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = {AmpPage.class})
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class AmpPageImpl implements AmpPage {

    private static final Logger LOG = LoggerFactory.getLogger(AmpPageImpl.class);

    @Self
    private SlingHttpServletRequest request;

    @ScriptVariable
    private Page currentPage;

    private Map<String, String> pageLinkAttrs;
    private AmpUtil.AMP_MODE ampMode;

    @PostConstruct
    protected void init() {
        ampMode = AmpUtil.getAmpMode(request);
    }

    @Override
    public Map<String, String> getPageLinkAttrs() {
        if (pageLinkAttrs == null) {
            pageLinkAttrs = new HashMap<>();
            String relValue;
            String hrefValue;
            if (isAmpSelector() || ampMode == AmpUtil.AMP_MODE.PAIRED_AMP) {
                relValue = "amphtml";
                hrefValue = currentPage.getPath() + DOT + AMP_SELECTOR + URL_EXTENSION;
            } else {
                relValue = "canonical";
                hrefValue = currentPage.getPath() + URL_EXTENSION;
            }

            pageLinkAttrs.put("rel", relValue);
            pageLinkAttrs.put("href", hrefValue);
        }
        return pageLinkAttrs;
    }

    @Override
    public boolean isAmpSelector() {
        return Arrays.asList(request.getRequestPathInfo().getSelectors()).contains(AMP_SELECTOR);
    }

    @Override
    public boolean isAmpEnabled() {
        return ampMode == AmpUtil.AMP_MODE.PAIRED_AMP || ampMode == AmpUtil.AMP_MODE.AMP_ONLY;
    }
}
