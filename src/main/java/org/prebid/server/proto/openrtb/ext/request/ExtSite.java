package org.prebid.server.proto.openrtb.ext.request;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Defines the contract for bidrequest.site.ext
 */
@AllArgsConstructor(staticName = "of")
@Value
public class ExtSite {

    /**
     * AMP should be 1 if the request comes from an AMP page, and 0 if not.
     */
    Integer amp;
}
