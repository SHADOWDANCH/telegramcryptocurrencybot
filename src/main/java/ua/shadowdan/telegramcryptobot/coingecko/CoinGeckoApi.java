package ua.shadowdan.telegramcryptobot.coingecko;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import ua.shadowdan.telegramcryptobot.coingecko.model.BasicCoinModel;
import ua.shadowdan.telegramcryptobot.coingecko.model.ExtendedCoinModel;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CoinGeckoApi {

    private static final long COINS_LIST_CACHE_LIFETIME = TimeUnit.HOURS.toMillis(1L);

    private static final String COINS_LIST_URL = "https://api.coingecko.com/api/v3/coins/list";
    private static final String COIN_DATA_URL = "https://api.coingecko.com/api/v3/coins/%s?localization=false&tickers=false&market_data=true&community_data=false&developer_data=false&sparkline=false";

    private final Map<String, BasicCoinModel> symbolCoinCache = new HashMap<>();
    private long lastSymbolCoinMapRefresh = 0L;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public CoinGeckoApi() {
        refreshCache();
    }

    public BasicCoinModel getCoinBySymbol(String symbol) {
        if ((System.currentTimeMillis() - lastSymbolCoinMapRefresh) > COINS_LIST_CACHE_LIFETIME) {
            refreshCache();
        }

        return symbolCoinCache.get(symbol);
    }

    @SneakyThrows
    public ExtendedCoinModel getCoinData(String id) {
        // TODO: should cache
        return objectMapper.readValue(new URL(String.format(COIN_DATA_URL, id)), ExtendedCoinModel.class);
    }

    @SneakyThrows
    private void refreshCache() {
        final BasicCoinModel[] coins = objectMapper.readValue(new URL(COINS_LIST_URL), BasicCoinModel[].class);

        for (BasicCoinModel coin : coins) {
            symbolCoinCache.put(coin.getSymbol(), coin);
        }

        lastSymbolCoinMapRefresh = System.currentTimeMillis();
        System.out.println("Cache refreshed!");
    }
}
