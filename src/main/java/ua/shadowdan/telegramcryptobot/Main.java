package ua.shadowdan.telegramcryptobot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ua.shadowdan.telegramcryptobot.bot.CryptoBot;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new CryptoBot());

        if (System.getProperty("heroku.bypass.sleep") != null) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    final int statusCode = getHttpConnectionStatus("https://www.google.com");
                    System.out.println("Ping OK! " + statusCode);
                }
            }, 0L, TimeUnit.MINUTES.toMillis(10L));
        }
    }

    private static int getHttpConnectionStatus(String url) {
        int code = 0;
        try {
            URL urlObj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(3000);
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36");
            con.connect();
            code = con.getResponseCode();
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }
}
