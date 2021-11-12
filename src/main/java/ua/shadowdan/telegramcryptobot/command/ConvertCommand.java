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

public class ConvertCommand extends BotCommand {

    private static final String CORRECT_RESPONSE_MESSAGE_FORMAT = "\u2705 {0,number,#.#########} {1} = {2,number,#.#########} {3}";

    private final CoinGeckoApi coinGeckoApi;

    public ConvertCommand(CoinGeckoApi coinGeckoApi) {
        super("convert", "Convert price");

        this.coinGeckoApi = coinGeckoApi;
    }

    @Override
    @SneakyThrows(TelegramApiException.class)
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        if (arguments.length < 3) {
            absSender.execute(
                    SendMessage.builder()
                            .chatId(chat.getId().toString())
                            .text("Use /" + getCommandIdentifier() + " <coin symbol> <convert to symbol> <amount>")
                            .build()
            );
            return;
        }

        final String coinSymbol = arguments[0].toLowerCase();
        final String convertToSymbol = arguments[1].toLowerCase();
        final float amount;
        try {
            amount = Float.parseFloat(arguments[2]);
        } catch (NumberFormatException ex) {
            absSender.execute(
                    SendMessage.builder()
                            .chatId(chat.getId().toString())
                            .text("\u274C Invalid number format.")
                            .build()
            );
            return;
        }

        final BasicCoinModel coin = coinGeckoApi.getCoinBySymbol(coinSymbol);
        if (coin == null) {
            final SendMessage answer = new SendMessage();
            answer.setChatId(chat.getId().toString());
            answer.setText("\u274C Unknown coin symbol.");
            absSender.execute(answer);
            return;
        }

        final ExtendedCoinModel.CoinMarketData coinData = coinGeckoApi.getCoinData(coin.getId()).getMarketData();
        final Float price = coinData.getCurrentPrice().get(convertToSymbol);
        if (price == null) {
            absSender.execute(
                    SendMessage.builder()
                            .chatId(chat.getId().toString())
                            .text("\u274C Can't convert to specified asset.")
                            .build()
            );
            return;
        }

        absSender.execute(
                SendMessage.builder()
                        .chatId(chat.getId().toString())
                        .text(
                                MessageFormat.format(
                                        CORRECT_RESPONSE_MESSAGE_FORMAT,
                                        amount,
                                        coinSymbol.toUpperCase(),
                                        amount * price,
                                        convertToSymbol.toUpperCase()
                                )
                        )
                        .build()
        );
    }
}
