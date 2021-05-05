package ua.shadowdan.telegramcryptobot.bot;

import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shadowdan.telegramcryptobot.Constants;
import ua.shadowdan.telegramcryptobot.command.PriceCommand;

public class CryptoBot extends TelegramLongPollingCommandBot {

    public CryptoBot() {
        register(new PriceCommand());
        register(new HelpCommand());
    }

    @Override
    public String getBotUsername() {
        return Constants.BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return Constants.BOT_TOKEN;
    }

    @Override
    public void processNonCommandUpdate(Update update) { }
}
