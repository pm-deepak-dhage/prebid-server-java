package org.prebid.server.auction;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.prebid.server.proto.response.Bid;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class TargetingKeywordsCreatorTest {

    @Test
    public void shouldReturnTargetingKeywordsForOrdinaryBid() {
        // given
        final Bid bid = Bid.builder().bidder("bidder1").price(BigDecimal.ONE).dealId("dealId1").cacheId("cacheId1")
                .width(50).height(100).build();

        // when
        final Map<String, String> keywords = TargetingKeywordsCreator.create("low", false)
                .makeFor(bid, false);

        // then
        assertThat(keywords).containsOnly(
                entry("hb_pb_bidder1", "1.00"),
                entry("hb_bidder_bidder1", "bidder1"),
                entry("hb_cache_id_bidder1", "cacheId1"),
                entry("hb_size_bidder1", "50x100"),
                entry("hb_deal_bidder1", "dealId1"));
    }

    @Test
    public void shouldReturnTargetingKeywordsForOrdinaryBidOpenrtb() {
        // given
        final com.iab.openrtb.response.Bid bid = com.iab.openrtb.response.Bid.builder().price(BigDecimal.ONE)
                .dealid("dealId1").w(50).h(100).build();

        // when
        final Map<String, String> keywords = TargetingKeywordsCreator.create("low", false)
                .makeFor(bid, "bidder1", false, null);

        // then
        assertThat(keywords).containsOnly(
                entry("hb_pb_bidder1", "1.00"),
                entry("hb_bidder_bidder1", "bidder1"),
                entry("hb_size_bidder1", "50x100"),
                entry("hb_deal_bidder1", "dealId1"));
    }

    @Test
    public void shouldReturnTargetingKeywordsWithEntireKeys() {
        // given
        final Bid bid = Bid.builder().bidder("veryververyverylongbidder1").price(BigDecimal.ONE).dealId("dealId1")
                .cacheId("cacheId1").width(50).height(100).build();

        // when
        final Map<String, String> keywords = TargetingKeywordsCreator.create("low", false)
                .makeFor(bid, false);

        // then
        assertThat(keywords).containsOnly(
                entry("hb_pb_veryververyverylongbidder1", "1.00"),
                entry("hb_bidder_veryververyverylongbidder1", "veryververyverylongbidder1"),
                entry("hb_cache_id_veryververyverylongbidder1", "cacheId1"),
                entry("hb_size_veryververyverylongbidder1", "50x100"),
                entry("hb_deal_veryververyverylongbidder1", "dealId1"));
    }

    @Test
    public void shouldReturnTargetingKeywordsWithEntireKeysOpenrtb() {
        // given
        final com.iab.openrtb.response.Bid bid = com.iab.openrtb.response.Bid.builder().price(BigDecimal.ONE)
                .dealid("dealId1").w(50).h(100).build();

        // when
        final Map<String, String> keywords = TargetingKeywordsCreator.create("low", false)
                .makeFor(bid, "veryververyverylongbidder1", false, null);

        // then
        assertThat(keywords).containsOnly(
                entry("hb_pb_veryververyverylongbidder1", "1.00"),
                entry("hb_bidder_veryververyverylongbidder1", "veryververyverylongbidder1"),
                entry("hb_size_veryververyverylongbidder1", "50x100"),
                entry("hb_deal_veryververyverylongbidder1", "dealId1"));
    }

    @Test
    public void shouldReturnTargetingKeywordsForWinningBid() {
        // given
        final Bid bid = Bid.builder().bidder("bidder1").price(BigDecimal.ONE).dealId("dealId1").cacheId("cacheId1")
                .width(50).height(100).build();

        // when
        final Map<String, String> keywords = TargetingKeywordsCreator.create("low", false)
                .makeFor(bid, true);

        // then
        assertThat(keywords).containsOnly(
                entry("hb_pb_bidder1", "1.00"),
                entry("hb_bidder_bidder1", "bidder1"),
                entry("hb_cache_id_bidder1", "cacheId1"),
                entry("hb_size_bidder1", "50x100"),
                entry("hb_deal_bidder1", "dealId1"),
                entry("hb_pb", "1.00"),
                entry("hb_bidder", "bidder1"),
                entry("hb_cache_id", "cacheId1"),
                entry("hb_size", "50x100"),
                entry("hb_deal", "dealId1"),
                entry("hb_creative_loadtype", "html"));
    }

    @Test
    public void shouldReturnTargetingKeywordsForWinningBidOpenrtb() {
        // given
        final com.iab.openrtb.response.Bid bid = com.iab.openrtb.response.Bid.builder().price(BigDecimal.ONE)
                .dealid("dealId1").w(50).h(100).build();

        // when
        final Map<String, String> keywords = TargetingKeywordsCreator.create("low", false)
                .makeFor(bid, "bidder1", true, "cacheId1");

        // then
        assertThat(keywords).containsOnly(
                entry("hb_pb_bidder1", "1.00"),
                entry("hb_bidder_bidder1", "bidder1"),
                entry("hb_size_bidder1", "50x100"),
                entry("hb_deal_bidder1", "dealId1"),
                entry("hb_pb", "1.00"),
                entry("hb_bidder", "bidder1"),
                entry("hb_size", "50x100"),
                entry("hb_deal", "dealId1"),
                entry("hb_creative_loadtype", "html"),
                entry("hb_cache_id", "cacheId1"),
                entry("hb_cache_id_bidder1", "cacheId1"));
    }

    @Test
    public void shouldFallbackToDefaultPriceIfInvalidPriceGranularity() {
        // given
        final Bid bid = Bid.builder().bidder("").price(BigDecimal.valueOf(3.87)).build();

        // when
        final Map<String, String> keywords = TargetingKeywordsCreator.create("invalid", false)
                .makeFor(bid, true);

        // then
        assertThat(keywords).contains(entry("hb_pb", StringUtils.EMPTY));
    }

    @Test
    public void shouldFallbackToDefaultPriceIfInvalidPriceGranularityOpenrtb() {
        // given
        final com.iab.openrtb.response.Bid bid = com.iab.openrtb.response.Bid.builder()
                .price(BigDecimal.valueOf(3.87)).build();

        // when
        final Map<String, String> keywords = TargetingKeywordsCreator.create("invalid", false)
                .makeFor(bid, "", true, null);

        // then
        assertThat(keywords).contains(entry("hb_pb", "0.0"));
    }

    @Test
    public void shouldUseDefaultPriceGranularity() {
        // given
        final Bid bid = Bid.builder().bidder("").price(BigDecimal.valueOf(3.87)).build();

        // when
        final Map<String, String> keywords = TargetingKeywordsCreator.create(null, false).makeFor(bid, true);

        // then
        assertThat(keywords).contains(entry("hb_pb", "3.80"));
    }

    @Test
    public void shouldUseDefaultPriceGranularityOpenrtb() {
        // given
        final com.iab.openrtb.response.Bid bid = com.iab.openrtb.response.Bid.builder()
                .price(BigDecimal.valueOf(3.87)).build();

        // when
        final Map<String, String> keywords = TargetingKeywordsCreator.create(null, false)
                .makeFor(bid, "", true, null);

        // then
        assertThat(keywords).contains(entry("hb_pb", "3.80"));
    }

    @Test
    public void shouldReturnDemandSdkLoadtypeForAudienceNetworkBidder() {
        // given
        final Bid bid = Bid.builder().bidder("audienceNetwork").price(BigDecimal.ONE).build();

        // when
        final Map<String, String> keywords = TargetingKeywordsCreator.create(null, false).makeFor(bid, true);

        // then
        assertThat(keywords).contains(entry("hb_creative_loadtype", "demand_sdk"));
    }

    @Test
    public void shouldReturnDemandSdkLoadtypeForAudienceNetworkBidderOpenrtb() {
        // given
        final com.iab.openrtb.response.Bid bid = com.iab.openrtb.response.Bid.builder().price(BigDecimal.ONE).build();

        // when
        final Map<String, String> keywords = TargetingKeywordsCreator.create(null, false)
                .makeFor(bid, "audienceNetwork", true, null);

        // then
        assertThat(keywords).contains(entry("hb_creative_loadtype", "demand_sdk"));
    }

    @Test
    public void shouldNotIncludeCacheIdAndDealIdAndSize() {
        // given
        final Bid bid = Bid.builder().bidder("bidder").price(BigDecimal.ONE).build();

        // when
        final Map<String, String> keywords = TargetingKeywordsCreator.create(null, false).makeFor(bid, true);

        // then
        assertThat(keywords).doesNotContainKeys("hb_cache_id_bidder", "hb_deal_bidder", "hb_size_bidder",
                "hb_cache_id", "hb_deal", "hb_size");
    }

    @Test
    public void shouldNotIncludeCacheIdAndDealIdAndSizeOpenrtb() {
        // given
        final com.iab.openrtb.response.Bid bid = com.iab.openrtb.response.Bid.builder().price(BigDecimal.ONE).build();

        // when
        final Map<String, String> keywords = TargetingKeywordsCreator.create(null, false)
                .makeFor(bid, "bidder", true, null);

        // then
        assertThat(keywords).doesNotContainKeys("hb_cache_id_bidder", "hb_deal_bidder", "hb_size_bidder",
                "hb_cache_id", "hb_deal", "hb_size");
    }

    @Test
    public void shouldReturnEnvKeyForAppRequest() {
        // given
        final Bid bid = Bid.builder().bidder("bidder").price(BigDecimal.ONE).build();

        // when
        final Map<String, String> keywords = TargetingKeywordsCreator.create(null, true).makeFor(bid, true);

        // then
        assertThat(keywords).contains(
                entry("hb_env", "mobile-app"),
                entry("hb_env_bidder", "mobile-app"));
    }

    @Test
    public void shouldReturnEnvKeyForAppRequestOpenrtb() {
        // given
        final com.iab.openrtb.response.Bid bid = com.iab.openrtb.response.Bid.builder().price(BigDecimal.ONE).build();

        // when
        final Map<String, String> keywords = TargetingKeywordsCreator.create(null, true)
                .makeFor(bid, "bidder", true, null);

        // then
        assertThat(keywords).contains(
                entry("hb_env", "mobile-app"),
                entry("hb_env_bidder", "mobile-app"));
    }

    @Test
    public void shouldParseValidPriceGranularity() {
        assertThat(TargetingKeywordsCreator.create("med", false).isPriceGranularityValid()).isTrue();
    }

    @Test
    public void shouldTolerateInvalidPriceGranularity() {
        assertThat(TargetingKeywordsCreator.create("invalid", false).isPriceGranularityValid()).isFalse();
    }

    @Test
    public void isNonZeroCpmShouldReturnFalse() {
        assertThat(TargetingKeywordsCreator.create("med", false).isNonZeroCpm(BigDecimal.ZERO)).isFalse();
    }

    @Test
    public void isNonZeroCpmShouldReturnTrue() {
        assertThat(TargetingKeywordsCreator.create("med", false).isNonZeroCpm(BigDecimal.ONE)).isTrue();
    }
}
