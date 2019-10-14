import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    private Shedule shedule = new Shedule();
    private Information info = new Information();
    private String action = "";


    public static void main(String[] args) {
        ApiContextInitializer.init();
        Bot.disableWarning();
        TelegramBotsApi telegramBot = new TelegramBotsApi();
        try {
            telegramBot.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        if (message != null && message.hasText()) {
            switch (action) {
                case "Week is awaiting!":
                    shedule.setWeek(message.getText());
                    sendMsg(message, "Set time!\uD83D\uDD56", false);
                    action= "Time is awaiting!";
                    break;
                case "Time is awaiting!":
                    shedule.setTime(message.getText());
                    sendMsg(message, "Set location!\uD83D\uDCCD", false);
                    action = "Location is awaiting";
                    break;
                case "Location is awaiting":
                    shedule.setLocation(message.getText());
                    sendMsg(message, "Set date!\uD83D\uDCC6", false);
                    action = "Date is awaiting";
                    break;
                case "Date is awaiting":
                    shedule.setDate(message.getText());
                    sendMsg(message, "Set description!\uD83D\uDCDD", false);
                    action = "Desc is awaiting!";
                    break;
                case "Desc is awaiting!":
                    shedule.setDesc(message.getText());
                    sendMsg(message, "Shedule is set!\uD83D\uDCC3", false);
                    info.addInformation(shedule);
                    action = "";
                    break;
                case "Wait for date":
                    info.getSheduleByDate(message, message.getText());
                    action = "";
                    break;
                    default:
                    switch (message.getText()) {
                        case "/help":
                            sendMsg(message, "Can I help you?\uD83D\uDE0F", true);
                            break;
                        case "/setting":
                            sendMsg(message, "Let's set your shedule!\uD83D\uDE09", true);
                            sendMsg(message, "Set week!\uD83D\uDCCB", false);
                            action = "Week is awaiting!";
                            shedule = new Shedule();
                            break;
                        case "/shedule":
                            info.showFullShedule(message);
                            break;
                        case "/sheduleByDate":
                            sendMsg(message, "Type in date\uD83D\uDCC6", false);
                            action = "Wait for date";
                            break;
                        default:
                            try {
                                sendMsg(message, Weather.getWeather(message.getText()), true);
                            } catch (IOException ex) {
                                sendMsg(message, "City not found!\uD83D\uDE30", true);
                            }
                    }
            }
        }
    }

    public void sendMsg(Message message, String text, boolean reply) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        if (reply) {
            sendMessage.setReplyToMessageId(message.getMessageId());
        }
        sendMessage.setText(text);
        try {
            setButtons(sendMessage);
            sendMessage(sendMessage);

        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }

    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(keyboard);
        keyboard.setSelective(true);
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/setting"));
        keyboardFirstRow.add(new KeyboardButton("/shedule"));
        keyboardFirstRow.add(new KeyboardButton("/sheduleByDate"));

        keyboardRowList.add(keyboardFirstRow);
        keyboard.setKeyboard(keyboardRowList);

    }

    public String getBotUsername() {
        return "NotificationBot";
    }

    public String getBotToken() {
        return "850911996:AAGN3oiHTePaKm7oFOp_Lq4SElJQnrEKRmY";
    }

    public static void disableWarning() {
        System.err.close();
        System.setErr(System.out);
    }
}
