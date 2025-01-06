package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyBot extends TelegramLongPollingBot {

    private int questionIndex = 0;
    private int score = 0;
    private final String[][] questions = {
            {"Yo'l harakati belgilari qanday maqsadda o'rnatiladi?",
                    "Transport vositalarini boshqarish uchun",
                    "Haydovchilarga yo'lni topishda yordam berish uchun",
                    "Yo'l harakati xavfsizligini ta'minlash uchun",
                    "Barchasi to'g'ri",
                    "Barchasi to'g'ri"},
            {"Qaysi belgi yo'lning asosiy yo'nalishini ko'rsatadi?",
                    "Ogohlantiruvchi belgi",
                    "Axborot belgi",
                    "Buyruq belgi",
                    "Imtiyoz belgi",
                    "Axborot belgi"},
            {"Transport vositalarini boshqarishda qaysi holatda quloqchin (naushnik) ishlatish mumkin?",
                    "Faqat bir quloqda",
                    "Hech qachon",
                    "Faqat shovqinli hududlarda",
                    "Faqat qo'shiq eshitishda",
                    "Faqat bir quloqda"},
            {"Qanday holatda yo'lning chap tomonida harakatlanish mumkin?",
                    "Faqat yo'l ta'mirlanayotgan bo'lsa",
                    "Faqat quvib o'tishda",
                    "Faqat tirbandlik bo'lsa",
                    "Hech qachon",
                    "Faqat quvib o'tishda"},
            {"Avtotransport vositasining eng kam harakat tezligi qaysi belgi bilan belgilanadi?",
                    "Ogohlantiruvchi belgi",
                    "Buyruq belgi",
                    "Axborot belgi",
                    "Cheklov belgi",
                    "Buyruq belgi"},
            {"Qaysi vaziyatda svetoforning qizil chirog'ida o'tish mumkin?",
                    "Favqulodda holatda",
                    "Politsiya xodimi ruxsat berganda",
                    "Har doim taqiqlanadi",
                    "Faqat tunda",
                    "Politsiya xodimi ruxsat berganda"},
            {"Quvib o'tishda qaysi masofani saqlash kerak?",
                    "Kamida 1 metr",
                    "Kamida 1,5 metr",
                    "Kamida 2 metr",
                    "Kamida 0,5 metr",
                    "Kamida 1,5 metr"},
            {"Avtotransport vositasining chapga burilishi qanday amalga oshiriladi?",
                    "To'xtamasdan",
                    "Yo'lning o'ng tomonida",
                    "Yo'lning markazidan",
                    "Chap tomondan",
                    "Yo'lning markazidan"},
            {"Yo'l-transport hodisasi yuz berganda birinchi navbatda nima qilish kerak?",
                    "Politsiyani chaqirish",
                    "Transport vositasini chetga olish",
                    "Jabrlanganlarga yordam ko'rsatish",
                    "Hodisa joyidan ketish",
                    "Jabrlanganlarga yordam ko'rsatish"},
            {"Yo'lovchilarni tashishda qaysi holatda qoidalar buziladi?",
                    "Yo'lovchilar xavfsizlik kamarini taqmasa",
                    "Yo'lovchilar turgan holda bo'lsa",
                    "Yo'lovchilar eshikni ochib o'tirishsa",
                    "Barcha holatlarda",
                    "Barcha holatlarda"}
    };

    private final String[] cities = {"Toshkent", "Samarkand", "Buxoro", "Andijon", "Farg'ona", "Namangan", "Nukus", "Xiva", "Termiz", "Jizzax"};
    private final String[][] districts = {
            {"Chilonzor", "Yunusobod", "Mirzo Ulug'bek", "Shayxontohur"},
            {"Samarqand shahar", "Jomboy", "Kattaqo'rg'on", "Narpay"},
            {"Buxoro shahar", "Kogon", "Qorako'l", "Peshku"},
            {"Andijon shahar", "Asaka", "Xonobod", "Shahrixon"},
            {"Farg'ona shahar", "Quva", "Beshariq", "Rishton"},
            {"Namangan shahar", "Chortoq", "Uychi", "Tuproqqala"},
            {"Nukus shahar", "Toshkent", "Ellikqala", "Karakalpak"},
            {"Xiva shahar", "Urgench", "Shavat", "Khorezm"},
            {"Termiz shahar", "Sariosiyo", "Sherabad", "Boysun"},
            {"Jizzax shahar", "Zafarobod", "Baxmal", "Gallaorol"}
    };

    private String selectedCity = "";
    private String selectedDistrict = "";
    private String userName = "";

    @Override
    public String getBotUsername() {
        return "@PravaPro_Bot"; // O'zingizning bot username'ni yozing
    }

    @Override
    public String getBotToken() {
        return "8093952591:AAEWwOF7hYdBKYLiifBUigF0g7F-jHaFb0o"; // O'zingizning bot token'ni yozing
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            if (messageText.equals("/start")) {
                sendCities(update.getMessage().getChatId().toString());
            } else {
                userName = update.getMessage().getFrom().getFirstName(); // Foydalanuvchi ismini olish
            }
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }

    private void sendCities(String chatId) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        for (int i = 0; i < cities.length; i++) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton cityButton = new InlineKeyboardButton();
            cityButton.setText(cities[i]);
            cityButton.setCallbackData("city_" + i);  // Shaharni tanlash
            rowInline.add(cityButton);
            rowsInline.add(rowInline);
        }

        markupInline.setKeyboard(rowsInline);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Salom, " + userName + "! Qaysi shaharni tanlaysiz?");
        sendMessage.setReplyMarkup(markupInline);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendDistricts(String chatId, int cityIndex) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        for (String district : districts[cityIndex]) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton districtButton = new InlineKeyboardButton();
            districtButton.setText(district);
            districtButton.setCallbackData("district_" + district);  // Tumanni tanlash
            rowInline.add(districtButton);
            rowsInline.add(rowInline);
        }

        InlineKeyboardButton startButton = new InlineKeyboardButton();
        startButton.setText("Testni boshlash");
        startButton.setCallbackData("start_quiz");

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(startButton);
        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Tanlangan shahar: " + cities[cityIndex] + ". Qaysi tumanni tanlaysiz?");
        sendMessage.setReplyMarkup(markupInline);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        String chatId = callbackQuery.getMessage().getChatId().toString();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if (data.startsWith("city_")) {
            int cityIndex = Integer.parseInt(data.substring(5));  // Shaharni tanlash
            selectedCity = cities[cityIndex];
            sendDistricts(chatId, cityIndex);  // Tanlangan shahar bo'yicha tumanlarni ko'rsatish
        } else if (data.startsWith("district_")) {
            selectedDistrict = data.substring(10);  // Tumanni tanlash
            sendMessage.setText("Tanlangan shahar: " + selectedCity + ", tuman: " + selectedDistrict + ". Testni boshlaymizmi?");
            sendMessage.setReplyMarkup(createTestStartButton());
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (data.equals("start_quiz")) {
            sendQuestion(chatId);  // Testni boshlash
        } else if (isCorrectAnswer(data)) {
            score++;  // To'g'ri javobni hisoblash
            sendMessage.setText("To'g'ri javob! Keyingi savolga o'tamiz.");
            questionIndex++;
            sendMessage.setReplyMarkup(createNextQuestionButton());
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            sendQuestion(chatId);  // Keyingi savolga o'tish
        } else {
            sendMessage.setText("Xato javob. Keyingi savolga o'tamiz.");
            questionIndex++;
            sendMessage.setReplyMarkup(createNextQuestionButton());
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            sendQuestion(chatId);  // Keyingi savolga o'tish
        }
    }

    private ReplyKeyboard createNextQuestionButton() {
        return null;
    }

    private boolean isCorrectAnswer(String answer) {
        String[] currentQuestion = questions[questionIndex];
        return currentQuestion[5].equals(answer);  // To'g'ri javobni tekshirish
    }

    private InlineKeyboardMarkup createTestStartButton() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("Ha");
        yesButton.setCallbackData("start_quiz");

        InlineKeyboardButton noButton = new InlineKeyboardButton();
        noButton.setText("Yoq");
        noButton.setCallbackData("no_quiz");

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(yesButton);
        rowInline.add(noButton);
        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    private void sendQuestion(String chatId) {
        if (questionIndex < questions.length) {
            String[] currentQuestion = questions[questionIndex];
            String questionText = currentQuestion[0];
            String[] options = {currentQuestion[1], currentQuestion[2], currentQuestion[3], currentQuestion[4]};

            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

            for (String option : options) {
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(option);
                button.setCallbackData(option);  // Javobni callbackData sifatida qo'shish
                rowInline.add(button);
                rowsInline.add(rowInline);
            }

            markupInline.setKeyboard(rowsInline);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(questionText);
            sendMessage.setReplyMarkup(markupInline);

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            finishTest(chatId);
        }
    }

    private void finishTest(String chatId) {
        Random rand = new Random();
        int prize = rand.nextInt(100000);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Siz testni tugatdingiz! Siz " + prize + " so'm yutdingiz.\n" +
                "Sovrinni olish uchun quyidagi kanalga kiring:\n" +
                "https://t.me/pravaprod");

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}