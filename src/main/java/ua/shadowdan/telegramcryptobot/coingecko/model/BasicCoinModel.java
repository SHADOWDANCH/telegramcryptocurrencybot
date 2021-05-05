package ua.shadowdan.telegramcryptobot.coingecko.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BasicCoinModel {

    protected final String id;
    protected final String symbol;
    protected final String name;

    @JsonCreator
    public BasicCoinModel(@JsonProperty("id") String id,
                          @JsonProperty("symbol") String symbol,
                          @JsonProperty("name") String name) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "BasicCoinModel{" +
                "id='" + id + '\'' +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
