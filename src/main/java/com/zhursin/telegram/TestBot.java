package com.zhursin.telegram;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TestBot extends TelegramLongPollingBot {

  @Override public String getBotUsername() {
    return "DarkhanBot";
  }

  @Override public String getBotToken() {
    return "5937041290:AAEee3IMH1woEMG3wFbGjkV9k7fnm6OnmIU";
  }

  @Override
  @SneakyThrows
  public void onUpdateReceived(final Update update) {
    if (update.hasCallbackQuery()) {
      handleCallback(update.getCallbackQuery());
    }
    if (update.hasMessage()) {
      handleMessage(update.getMessage());
    }
  }

  @SneakyThrows
  private void handleCallback(CallbackQuery callbackQuery) {
    Message message = callbackQuery.getMessage();
    String action = callbackQuery.getData();
    List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
    buttons.add(
        Arrays.asList(
            InlineKeyboardButton.builder()
                .text("Ссылка")
                .url("https://t.me/startupordataraz")
                .callbackData("REFER")
                .build()
        )
    );
    switch (action) {
      case "STOCK":
        execute(
            SendMessage.builder()
                .chatId(message.getChatId())
                .text("Нажмите на ссылку ниже \uD83D\uDC47")
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build()
        );
        break;
      case "ORDER":
        execute(
          SendMessage.builder()
              .chatId(message.getChatId())
              .text("Сделать заказ временно не доступуен")
              .build()
        );
        break;
      case "STATUS":
        execute(
            SendMessage.builder()
                .chatId(message.getChatId())
                .text("Узнать статус пока не доступен")
                .build()
        );
        break;

      case "ABOUT":
        execute(sendInstructionMessage(message.getChatId()));
        break;
    }
  }

  @SneakyThrows
  private SendMessage sendInstructionMessage(final Long chatId) {
    return SendMessage.builder()
        .text("Наша компания занимается торговлей качественных кроссовок")
        .chatId(chatId)
        .build();
  }

  @SneakyThrows
  private void handleMessage(final Message message) {

    if (message.hasText() && message.hasEntities()) {
      Optional<MessageEntity> commandEntity =
          message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
      if (commandEntity.isPresent()) {
        String command = message.getText()
            .substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
        switch (command) {
          case "/start":
            execute(
                SendMessage.builder()
                    .text("Welcome, " + message.getFrom().getFirstName() + "!")
                    .chatId(message.getChatId()).build()
            );
            execute(sendInlineKeyBoardMessage(message.getChatId()));}
      }
    }
  }

  private SendMessage sendInlineKeyBoardMessage(Long chatId) {
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    InlineKeyboardButton inlineKeyboardButton0 = new InlineKeyboardButton();
    InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
    InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
    InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
    inlineKeyboardButton0.setText("Полное ознакомление");
    inlineKeyboardButton0.setCallbackData("ABOUT");
    inlineKeyboardButton1.setText("Сделать заказ");
    inlineKeyboardButton1.setCallbackData("ORDER");
    inlineKeyboardButton2.setText("Статус заказа");
    inlineKeyboardButton2.setCallbackData("STATUS");
    inlineKeyboardButton3.setText("В наличии");
    inlineKeyboardButton3.setCallbackData("STOCK");
    List<InlineKeyboardButton> keyboardButtonsRow0 = new ArrayList<>();
    List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
    List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
    List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
    keyboardButtonsRow0.add(inlineKeyboardButton0);
    keyboardButtonsRow1.add(inlineKeyboardButton1);
    keyboardButtonsRow2.add(inlineKeyboardButton2);
    keyboardButtonsRow3.add(inlineKeyboardButton3);
    List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
    rowList.add(keyboardButtonsRow0);
    rowList.add(keyboardButtonsRow1);
    rowList.add(keyboardButtonsRow2);
    rowList.add(keyboardButtonsRow3);
    inlineKeyboardMarkup.setKeyboard(rowList);
    return SendMessage.builder()
        .chatId(chatId)
        .replyMarkup(inlineKeyboardMarkup)
        .text("Пожалуйста, выберите действие:")
        .build();
  }

  @SneakyThrows
  public static void main(String[] args) {
    TestBot testBot = new TestBot();
    TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
    telegramBotsApi.registerBot(testBot);
  }
}
