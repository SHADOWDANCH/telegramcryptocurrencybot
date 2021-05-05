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

public class PriceCommand extends BotCommand {

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
            answer.setText( "<b>" + coinBySymbol.getName() + "</b> \n\n"
                    + "<u>Current price:</u>  " + marketData.getCurrentPrice().get("usd") + "$\n"
                    + "<u>Market cap:</u>  " + marketData.getMarketCapitalization().get("usd") + "$\n"
                    + "<u>Total volume:</u>  " + marketData.getTotalVolume().get("usd") + "$\n"
                    + "<u>All Time High:</u>  " + marketData.getAllTimeHigh().get("usd") + "$\n"
            );
        }

        absSender.execute(answer);
    }
}
