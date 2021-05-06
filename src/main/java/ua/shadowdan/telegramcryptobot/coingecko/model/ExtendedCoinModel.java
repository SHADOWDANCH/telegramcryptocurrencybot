package ua.shadowdan.telegramcryptobot.coingecko.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtendedCoinModel extends BasicCoinModel {

    public CoinMarketData getMarketData() {
        return marketData;
    }

    private final CoinMarketData marketData;

    @JsonCreator
    public ExtendedCoinModel(@JsonProperty("id") String id,
                             @JsonProperty("symbol") String symbol,
                             @JsonProperty("name") String name,
                             @JsonProperty("market_data") CoinMarketData marketData) {
        super(id, symbol, name);
        this.marketData = marketData;
    }

    @Override
    public String toString() {
        return "ExtendedCoinModel{" +
                "id='" + id + '\'' +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", marketData=" + marketData +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CoinMarketData {
        private final Map<String, Float> currentPrice;
        private final Map<String, Float> allTimeHigh;
        private final Map<String, Long> marketCapitalization;
        private final Map<String, Long> totalVolume;
        private final float priceChangePercent24h;
        private final float priceChangePercent7d;
        private final float priceChangePercent1y;

        @JsonCreator
        public CoinMarketData(@JsonProperty("current_price") Map<String, Float> currentPrice,
                              @JsonProperty("ath") Map<String, Float> allTimeHigh,
                              @JsonProperty("market_cap") Map<String, Long> marketCapitalization,
                              @JsonProperty("total_volume") Map<String, Long> totalVolume,
                              @JsonProperty("price_change_percentage_24h") float priceChangePercent24h,
                              @JsonProperty("price_change_percentage_7d") float priceChangePercent7d,
                              @JsonProperty("price_change_percentage_1y") float priceChangePercent1y) {
            this.currentPrice = currentPrice;
            this.allTimeHigh = allTimeHigh;
            this.marketCapitalization = marketCapitalization;
            this.totalVolume = totalVolume;
            this.priceChangePercent24h = priceChangePercent24h;
            this.priceChangePercent7d = priceChangePercent7d;
            this.priceChangePercent1y = priceChangePercent1y;
        }

        public Map<String, Float> getCurrentPrice() {
            return currentPrice;
        }

        public Map<String, Float> getAllTimeHigh() {
            return allTimeHigh;
        }

        public Map<String, Long> getMarketCapitalization() {
            return marketCapitalization;
        }

        public Map<String, Long> getTotalVolume() {
            return totalVolume;
        }

        public float getPriceChangePercent24h() {
            return priceChangePercent24h;
        }

        public float getPriceChangePercent7d() {
            return priceChangePercent7d;
        }

        public float getPriceChangePercent1y() {
            return priceChangePercent1y;
        }
    }
}
