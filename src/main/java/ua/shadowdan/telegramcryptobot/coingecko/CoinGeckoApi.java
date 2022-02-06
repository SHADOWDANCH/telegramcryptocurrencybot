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
    private final Map<String, BasicCoinModel> nameCoinCache = new HashMap<>();
    private long lastCoinsCacheRefresh = 0L;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public BasicCoinModel getCoinBySymbol(String symbol) {
        refreshCache();

        return symbolCoinCache.get(symbol);
    }

    public BasicCoinModel getCoinByName(String name) {
        refreshCache();

        return nameCoinCache.get(name);
    }

    @SneakyThrows
    public ExtendedCoinModel getCoinData(String id) {
        // FIXME: maybe should be cached
        return objectMapper.readValue(new URL(String.format(COIN_DATA_URL, id)), ExtendedCoinModel.class);
    }

    @SneakyThrows
    private void refreshCache() {
        if ((System.currentTimeMillis() - lastCoinsCacheRefresh) < COINS_LIST_CACHE_LIFETIME) {
            return;
        }

        symbolCoinCache.clear();
        nameCoinCache.clear();

        final BasicCoinModel[] coins = objectMapper.readValue(new URL(COINS_LIST_URL), BasicCoinModel[].class);

        for (BasicCoinModel coin : coins) {
            symbolCoinCache.putIfAbsent(coin.getSymbol(), coin);
            nameCoinCache.putIfAbsent(coin.getName(), coin);
        }

        lastCoinsCacheRefresh = System.currentTimeMillis();
        System.out.println("Cache refreshed!");
    }
}
