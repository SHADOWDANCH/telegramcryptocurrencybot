package ua.shadowdan.telegramcryptobot.bot;

import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shadowdan.telegramcryptobot.command.CoinCommand;

public class CryptoBot extends TelegramLongPollingCommandBot {

    public CryptoBot() {
        register(new CoinCommand());
        register(new HelpCommand());
    }

    @Override
    public String getBotUsername() {
        return System.getProperty("cryptobot.telegram.username");
    }

    @Override
    public String getBotToken() {
        return System.getProperty("cryptobot.telegram.token");
    }

    @Override
    public void processNonCommandUpdate(Update update) { }
}
