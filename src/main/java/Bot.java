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

    public static void main(String[] args) {
        ApiContextInitializer.init();
         TelegramBotsApi telegramBot = new TelegramBotsApi();
            try{
                telegramBot.registerBot(new Bot());
            }catch(TelegramApiException e){
                e.printStackTrace();

            }

    }
    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();
        if(message != null && message.hasText()){
            switch(message.getText()){
                case "/help":
                    sendMsg(message, "Can I help you?");
                    break;
                case "/setting":
                    sendMsg(message, "What we will change?");
                    break;
                default:
                    try{
                        sendMsg(message, Weather.getWeather(message.getText(), model));
                    }catch (IOException ex){
                        sendMsg(message, "City not found!");
                    }
            }
        }

    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try{
            setButtons(sendMessage);
            sendMessage(sendMessage);

        }catch (TelegramApiException ex){
            ex.printStackTrace();
        }
    }


    public void setButtons(SendMessage sendMessage){
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(keyboard);
        keyboard.setSelective(true);
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/setting"));

        keyboardRowList.add(keyboardFirstRow);
        keyboard.setKeyboard(keyboardRowList);

    }
    public String getBotUsername() {
        return "NotificationBot";
    }

    public String getBotToken() {
        return "850911996:AAGN3oiHTePaKm7oFOp_Lq4SElJQnrEKRmY";
    }
}
