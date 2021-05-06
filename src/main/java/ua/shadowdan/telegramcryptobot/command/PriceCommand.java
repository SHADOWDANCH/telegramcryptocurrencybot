package ua.shadowdan.telegramcryptobot.command;

import lombok.SneakyThrows;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.shadowdan.telegramcryptobot.coingecko.CoinGeckoApi;
import ua.shadowdan.telegramcryptobot.coingecko.model.BasicCoinModel;
import ua.shadowdan.telegramcryptobot.coingecko.model.ExtendedCoinModel;

import java.text.MessageFormat;

public class PriceCommand extends BotCommand {

    private static final String CORRECT_RESPONSE_MESSAGE_FORMAT =
            "<b>{0}</b> \n"
            + "\n"
            + "<u>Current price:</u> {1}$\n"
            + "<u>Market cap:</u> {2}$\n"
            + "<u>Total volume:</u> {3}$\n"
            + "<u>All Time High:</u> {4}$\n"
            + "\n"
            + "<u>Price change (24 hours):</u> {5,number,#.#%}\n"
            + "<u>Price change (7 days):</u> {6,number,#.#%}\n"
            + "<u>Price change (1 year):</u> {7,number,#.#%}\n";

    private final CoinGeckoApi coinGeckoApi = new CoinGeckoApi();

    public PriceCommand() {
        super("price", "Get's current price of coin/token");
    }

    @Override
    @SneakyThrows(TelegramApiException.class)
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        if (arguments.length < 1) {
            final SendMessage answer = new SendMessage();
            answer.setChatId(chat.getId().toString());
            answer.setText("Use /price <coin symbol>");
            absSender.execute(answer);
            return;
        }
        final String coinSymbol = arguments[0];
        final BasicCoinModel coinBySymbol = coinGeckoApi.getCoinBySymbol(coinSymbol);

        final SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        if (coinBySymbol == null) {
            answer.setText("Coin does not exists.");
        } else {
            final ExtendedCoinModel.CoinMarketData marketData = coinGeckoApi.getCoinData(coinBySymbol.getId()).getMarketData();
            answer.setParseMode("HTML");
            answer.setText(
                    MessageFormat.format(CORRECT_RESPONSE_MESSAGE_FORMAT,
                            coinBySymbol.getName(),
                            marketData.getCurrentPrice().get("usd"),
                            marketData.getMarketCapitalization().get("usd"),
                            marketData.getTotalVolume().get("usd"),
                            marketData.getAllTimeHigh().get("usd"),
                            marketData.getPriceChangePercent7d(),
                            marketData.getPriceChangePercent24h(),
                            marketData.getPriceChangePercent1y())
            );
        }

        absSender.execute(answer);
    }
}
