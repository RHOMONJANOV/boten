package org.example;

public class TestStage {
    private int stage;
    private int score;
    private int questionIndex;
    private final String[][][] questions = {

            {
                    {"Yo'l harakati belgilari qanday maqsadda o'rnatiladi?", "Transport vositalarini boshqarish uchun", "Haydovchilarga yo'lni topishda yordam berish uchun", "Yo'l harakati xavfsizligini ta'minlash uchun", "Barchasi to'g'ri", "Barchasi to'g'ri"},
                    {"Qaysi belgi yo'lning asosiy yo'nalishini ko'rsatadi?", "Ogohlantiruvchi belgi", "Axborot belgi", "Buyruq belgi", "Imtiyoz belgi", "Axborot belgi"},

            },

            {
                    {"Avtotransport vositasining eng kam harakat tezligi qaysi belgi bilan belgilanadi?", "Ogohlantiruvchi belgi", "Buyruq belgi", "Axborot belgi", "Cheklov belgi", "Buyruq belgi"},
                    {"Qaysi vaziyatda svetoforning qizil chirog'ida o'tish mumkin?", "Favqulodda holatda", "Politsiya xodimi ruxsat berganda", "Har doim taqiqlanadi", "Faqat tunda", "Politsiya xodimi ruxsat berganda"},

            },

            {
                    {"Qanday holatda yo'lning chap tomonida harakatlanish mumkin?", "Faqat yo'l ta'mirlanayotgan bo'lsa", "Faqat quvib o'tishda", "Faqat tirbandlik bo'lsa", "Hech qachon", "Faqat quvib o'tishda"},
                    {"Avtotransport vositasining chapga burilishi qanday amalga oshiriladi?", "To'xtamasdan", "Yo'lning o'ng tomonida", "Yo'lning markazidan", "Chap tomondan", "Yo'lning markazidan"},

            }
    };

    public TestStage() {
        this.stage = 1;
        this.score = 0;
        this.questionIndex = 0;
    }

    // Savollarni olish
    public String getCurrentQuestion() {
        return questions[stage - 1][questionIndex][0];
    }

    // Javob variantlarini olish
    public String[] getCurrentOptions() {
        String[] options = new String[4];
        System.arraycopy(questions[stage - 1][questionIndex], 1, options, 0, 4);
        return options;
    }

    public boolean isCorrectAnswer(String answer) {
        return questions[stage - 1][questionIndex][5].equals(answer);
    }


    public boolean nextQuestion() {
        questionIndex++;
        if (questionIndex >= questions[stage - 1].length) {
            if (score >= 7) {
                stage++;
                if (stage > 3) {
                    return false;
                }
            }
            questionIndex = 0;
        }
        return true;
    }


    public String getResult() {
        return "Siz " + score + " ta to'g'ri javob berdingiz.\n" +
                "Keyingi bosqichga o'tish uchun 7 ta to'g'ri javob kerak!";
    }

    public void increaseScore() {
        score++;
    }

    public int getStage() {
        return stage;
    }

    public int getScore() {
        return score;
    }
}
