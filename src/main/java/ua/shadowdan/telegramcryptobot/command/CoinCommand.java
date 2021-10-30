package ua.shadowdan.telegramcryptobot.command;

import lombok.SneakyThrows;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.shadowdan.telegramcryptobot.Constants;
import ua.shadowdan.telegramcryptobot.coingecko.CoinGeckoApi;
import ua.shadowdan.telegramcryptobot.coingecko.model.BasicCoinModel;
import ua.shadowdan.telegramcryptobot.coingecko.model.ExtendedCoinModel;

import java.text.MessageFormat;

public class CoinCommand extends BotCommand {

    private static final String CORRECT_RESPONSE_MESSAGE_FORMAT =
            "<b>{0}</b> \n"
            + "\n"
            + "<u>\uD83D\uDCC8 Current price:</u> {1,number,#.#########}$\n"
            + "<u>\uD83D\uDCCA Market cap:</u> {2}$\n"
            + "<u>\uD83C\uDFC5 Total volume:</u> {3}$\n"
            + "<u>\uD83D\uDCAF All Time High:</u> {4,number,#.#########}$\n"
            + "\n"
            + "<u>{5} Price change (24 hours):</u> {6,number,#.#}%\n"
            + "<u>{7} Price change (7 days):</u> {8,number,#.#}%\n"
            + "<u>{9} Price change (1 year):</u> {10,number,#.#}%\n";

    private static final CoinGeckoApi coinGeckoApi = new CoinGeckoApi();

    public CoinCommand() {
        super("coin", "Get's current info about coin");
    }

    @Override
    @SneakyThrows(TelegramApiException.class)
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        if (arguments.length < 1) {
            final SendMessage answer = new SendMessage();
            answer.setChatId(chat.getId().toString());
            answer.setText("Use /" + getCommandIdentifier() + " <coin symbol>");
            absSender.execute(answer);
            return;
        }
        for (int i = 0; i < arguments.length && i < 3; i ++) {
            final String coinIdentifier = arguments[i];
            BasicCoinModel coinBySymbol;
            if ((coinBySymbol = coinGeckoApi.getCoinBySymbol(coinIdentifier)) == null) {
                coinBySymbol = coinGeckoApi.getCoinByName(coinIdentifier);
            }

            final SendMessage answer = new SendMessage();
            answer.setChatId(chat.getId().toString());
            if (coinBySymbol == null) {
                answer.setText("Coin does not exists.");
            } else {
                final ExtendedCoinModel.CoinMarketData marketData = coinGeckoApi.getCoinData(coinBySymbol.getId()).getMarketData();
                final float priceChangePercent24h = marketData.getPriceChangePercent24h();
                final float priceChangePercent7d = marketData.getPriceChangePercent7d();
                final float priceChangePercent1y = marketData.getPriceChangePercent1y();

                answer.setParseMode("HTML");
                answer.setText(
                        MessageFormat.format(CORRECT_RESPONSE_MESSAGE_FORMAT,
                                coinBySymbol.getName(),
                                marketData.getCurrentPrice().get("usd"),
                                marketData.getMarketCapitalization().get("usd"),
                                marketData.getTotalVolume().get("usd"),
                                marketData.getAllTimeHigh().get("usd"),
                                getPriceChangeEmoji(priceChangePercent24h),
                                priceChangePercent24h,
                                getPriceChangeEmoji(priceChangePercent7d),
                                priceChangePercent7d,
                                getPriceChangeEmoji(priceChangePercent1y),
                                priceChangePercent1y)
                );
            }

            absSender.execute(answer);
        }
    }

    private static String getPriceChangeEmoji(float price) {
        return price >= 0 ? Constants.PRICE_UP_EMOJI : Constants.PRICE_DOWN_EMOJI;
    }
}
