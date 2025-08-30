package com.bharatiscript.padaayifruits;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    ImageView questionImage;
    Button[] optionButtons = new Button[4];
    TextView scoreText, categoryText;

    List<Question> allQuestions = new ArrayList<>();
    List<Question> selectedQuestions;
    int currentQuestionIndex = 0;
    int score = 0;
    String selectedCategory = "fruits";

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("CATEGORY")) {
            selectedCategory = intent.getStringExtra("CATEGORY");
        }


        scoreText = findViewById(R.id.scoreText);
        categoryText = findViewById(R.id.categoryText);
        questionImage = findViewById(R.id.questionImage);
        optionButtons[0] = findViewById(R.id.option1);
        optionButtons[1] = findViewById(R.id.option2);
        optionButtons[2] = findViewById(R.id.option3);
        optionButtons[3] = findViewById(R.id.option4);

        setCategoryTitle();

        loadQuestionsByCategory();
        Collections.shuffle(allQuestions);
        int questionCount = Math.min(5, allQuestions.size());
        selectedQuestions = allQuestions.subList(0, questionCount);

        showQuestion();

        for (int i = 0; i < 4; i++) {
            final int index = i;
            optionButtons[i].setOnClickListener(v -> handleAnswer(index));
        }
    }

    private void setCategoryTitle() {
        String title = "";
        switch (selectedCategory) {
            case "fruits":
                title = "फल (Fruits)";
                break;
            case "animals":
                title = "जानवर (Animals)";
                break;
            case "vegetables":
                title = "सब्जियां (Vegetables)";
                break;
            case "patriots":
                title = "पतनी (Patriots)";
                break;
            case "birds":
                title = "पेंगुइन (Birds)";
                break;
            case "monuments":
                title = "स्मारक (Monuments)";
                break;
        }
        categoryText.setText(title);
    }

    private void showQuestion() {
        if (currentQuestionIndex >= selectedQuestions.size()) {
            showScorePopup();
            return;
        }

        Question q = selectedQuestions.get(currentQuestionIndex);
        questionImage.setImageResource(q.imageResId);
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(q.options[i]);
        }
        scoreText.setText("Score: " + score);
    }

    private void handleAnswer(int selectedIndex) {
        Question q = selectedQuestions.get(currentQuestionIndex);
        if (selectedIndex == q.correctIndex) {
            score++;
        }
        currentQuestionIndex++;
        showQuestion();
    }

    private void showScorePopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("🎉 Quiz Complete 🎉");

        String message = "Your Score: " + score + "/" + selectedQuestions.size() +
                "\n\nधन्यवाद खेलने के लिए!\n\nआप क्या करना चाहेंगे?";
        builder.setMessage(message);
        builder.setCancelable(false);

        builder.setPositiveButton("Retry", (dialog, which) -> {
            restartQuiz();
        });

        builder.setNeutralButton("Categories", (dialog, which) -> {
            goToCategories();
        });

        builder.setNegativeButton("Home", (dialog, which) -> {
            goToHome();
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        handler.postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
                goToHome();
            }
        }, 15000);
    }

    private void goToCategories() {
        startActivity(new Intent(this, CategorySelectionActivity.class));
        finish();
    }

    private void goToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicService.pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicService.resumeMusic();
    }

    private void loadQuestionsByCategory() {
        allQuestions.clear();

        switch (selectedCategory) {
            case "fruits":
                loadFruitQuestions();
                break;
            case "animals":
                loadAnimalQuestions();
                break;
            case "vegetables":
                loadVegetableQuestions();
                break;
            case "monuments":
                loadMonumentQuestions();
                break;
            case "birds":
                loadBirdsQuestions();
                break;
            case "patriots":
                loadPatriotsQuestions();
                break;
            default:
                loadFruitQuestions();
                break;
        }
    }

    private void loadFruitQuestions() {
        allQuestions.add(new Question(R.drawable.apple, new String[]{"नींबू", "सेब", "पपीता", "तरबूज"}, 1));
        allQuestions.add(new Question(R.drawable.mango, new String[]{"नारियल", "नींबू", "लीची", "आम"}, 3));
        allQuestions.add(new Question(R.drawable.papaya, new String[]{"अंगूर", "नारियल", "नींबू", "पपीता"}, 3));
        allQuestions.add(new Question(R.drawable.grapes, new String[]{"अंगूर", "लीची", "संतरा", "केला"}, 0));
        allQuestions.add(new Question(R.drawable.coconut, new String[]{"केला", "तरबूज", "नारियल", "आम"}, 2));
        allQuestions.add(new Question(R.drawable.orange, new String[]{"रसभरी", "आम", "सेब", "संतरा"}, 3));
        allQuestions.add(new Question(R.drawable.pineapple, new String[]{"सेब", "केला", "अनानास", "अंगूर"}, 2));
        allQuestions.add(new Question(R.drawable.pomegranate, new String[]{"तरबूज", "आम", "संतरा", "अनार"}, 3));
        allQuestions.add(new Question(R.drawable.banana, new String[]{"आम", "सेब", "लीची", "केला"}, 3));
        allQuestions.add(new Question(R.drawable.lemon, new String[]{"अनानास", "नींबू", "केला", "तरबूज"}, 1));
        allQuestions.add(new Question(R.drawable.pear, new String[]{"नाशपाती", "नींबू", "केला", "आम"}, 0));
        allQuestions.add(new Question(R.drawable.jackfruit, new String[]{"कटहल", "आम", "लीची", "नींबू"}, 0));
    }

    private void loadBirdsQuestions(){
        allQuestions.add(new Question(R.drawable.penguin, new String[]{"कबूतर", "पेंगुइन", "कौवा", "तोता"}, 1));
        allQuestions.add(new Question(R.drawable.pigeon, new String[]{"कबूतर", "मोर", "गौरैया", "चील"}, 0));
        allQuestions.add(new Question(R.drawable.sparrow, new String[]{"गौरैया", "उल्लू", "स्वान", "कौवा"}, 0));
        allQuestions.add(new Question(R.drawable.eagle, new String[]{"मोर", "चील", "तोता", "कबूतर"}, 1));
        allQuestions.add(new Question(R.drawable.crow, new String[]{"कौवा", "स्वान", "तोता", "गौरैया"}, 0));
        allQuestions.add(new Question(R.drawable.peacock, new String[]{"तोता", "पेंगुइन", "मोर", "उल्लू"}, 2));
        allQuestions.add(new Question(R.drawable.woodpecker, new String[]{"स्वान", "गौरैया", "कठफोड़वा", "तोता"}, 2));
        allQuestions.add(new Question(R.drawable.owl, new String[]{"कबूतर", "उल्लू", "मोर", "कौवा"}, 1));
        allQuestions.add(new Question(R.drawable.swan, new String[]{"तोता", "पेंगुइन", "स्वान", "चील"}, 2));
        allQuestions.add(new Question(R.drawable.parrot, new String[]{"कौवा", "मोर", "तोता", "उल्लू"}, 2));
    }

    private void loadPatriotsQuestions(){
        allQuestions.add(new Question(R.drawable.bhagatsingh, new String[]{"भगत सिंह", "चंद्रशेखर आज़ाद", "सुखदेव", "मंगल पांडे"}, 0));
        allQuestions.add(new Question(R.drawable.chandrashekharazad, new String[]{"महात्मा गांधी", "चंद्रशेखर आज़ाद", "लाल बहादुर शास्त्री", "तांत्या टोपे"}, 1));
        allQuestions.add(new Question(R.drawable.khudirambose, new String[]{"सुभाष चंद्र बोस", "खुदीराम बोस", "राम प्रसाद बिस्मिल", "सुखदेव"}, 1));
        allQuestions.add(new Question(R.drawable.lalbahadurshastri, new String[]{"लाल बहादुर शास्त्री", "भगत सिंह", "महात्मा गांधी", "रानी लक्ष्मीबाई"}, 0));
        allQuestions.add(new Question(R.drawable.mahatmagandhi, new String[]{"सुभाष चंद्र बोस", "लाल बहादुर शास्त्री", "महात्मा गांधी", "सरदार वल्लभभाई पटेल"}, 2));
        allQuestions.add(new Question(R.drawable.mangalpandey, new String[]{"तांत्या टोपे", "मंगल पांडे", "राम प्रसाद बिस्मिल", "भगत सिंह"}, 1));
        allQuestions.add(new Question(R.drawable.ramprasadbismil, new String[]{"राम प्रसाद बिस्मिल", "सुखदेव", "महात्मा गांधी", "खुदीराम बोस"}, 0));
        allQuestions.add(new Question(R.drawable.ranilaxmibai, new String[]{"रानी लक्ष्मीबाई", "सुभाष चंद्र बोस", "भगत सिंह", "लाल बहादुर शास्त्री"}, 0));
        allQuestions.add(new Question(R.drawable.sardarvallabhbhaipatel, new String[]{"चंद्रशेखर आज़ाद", "सुभाष चंद्र बोस", "सरदार वल्लभभाई पटेल", "महात्मा गांधी"}, 2));
        allQuestions.add(new Question(R.drawable.subhashchandrabose, new String[]{"सुभाष चंद्र बोस", "राम प्रसाद बिस्मिल", "भगत सिंह", "लाल बहादुर शास्त्री"}, 0));
        allQuestions.add(new Question(R.drawable.sukhdev, new String[]{"सुखदेव", "मंगल पांडे", "चंद्रशेखर आज़ाद", "खुदीराम बोस"}, 0));
        allQuestions.add(new Question(R.drawable.tatyatope, new String[]{"राम प्रसाद बिस्मिल", "तांत्या टोपे", "सुभाष चंद्र बोस", "लाल बहादुर शास्त्री"}, 1));
    }


    private void loadAnimalQuestions() {
        allQuestions.add(new Question(R.drawable.cow, new String[]{"गाय", "भैंस", "बकरी", "घोड़ा"}, 0));
        allQuestions.add(new Question(R.drawable.dog, new String[]{"कुत्ता", "बिल्ली", "भेड़", "सुअर"}, 0));
        allQuestions.add(new Question(R.drawable.cat, new String[]{"कुत्ता", "बिल्ली", "चूहा", "खरगोश"}, 1));
        allQuestions.add(new Question(R.drawable.donkey, new String[]{"घोड़ा", "गाय", "गधा", "बकरी"}, 2));
        allQuestions.add(new Question(R.drawable.deer, new String[]{"हिरण", "खरगोश", "घोड़ा", "गाय"}, 0));
        allQuestions.add(new Question(R.drawable.fox, new String[]{"कुत्ता", "भेड़िया", "लोमड़ी", "बिल्ली"}, 2));
        allQuestions.add(new Question(R.drawable.giraffe, new String[]{"हाथी", "जिराफ़", "ऊँट", "घोड़ा"}, 1));
        allQuestions.add(new Question(R.drawable.goat, new String[]{"भेड़", "गधा", "बकरी", "गाय"}, 2));
        allQuestions.add(new Question(R.drawable.horse, new String[]{"गधा", "घोड़ा", "ऊँट", "कुत्ता"}, 1));
        allQuestions.add(new Question(R.drawable.hen, new String[]{"बतख", "मुर्गी", "कबूतर", "मोर"}, 1));
    }


    private void loadVegetableQuestions() {
        allQuestions.add(new Question(R.drawable.tomato, new String[]{"टमाटर", "आलू", "प्याज", "गाजर"}, 0));
        allQuestions.add(new Question(R.drawable.potatoe, new String[]{"शकरकंद", "आलू", "मूली", "चुकंदर"}, 1));
        allQuestions.add(new Question(R.drawable.onion, new String[]{"लहसुन", "अदरक", "प्याज", "मिर्च"}, 2));
        allQuestions.add(new Question(R.drawable.carrot, new String[]{"मूली", "चुकंदर", "शलगम", "गाजर"}, 3));
        allQuestions.add(new Question(R.drawable.cabbage, new String[]{"पत्तागोभी", "फूलगोभी", "पालक", "मेथी"}, 0));
        allQuestions.add(new Question(R.drawable.cucumber, new String[]{"खीरा", "तोरी", "लौकी", "ककड़ी"}, 0));
        allQuestions.add(new Question(R.drawable.cauliflower, new String[]{"फूलगोभी", "पत्तागोभी", "ब्रोकली", "पालक"}, 0));
        allQuestions.add(new Question(R.drawable.corn, new String[]{"मक्का", "चना", "अरहर", "सोयाबीन"}, 0));
        allQuestions.add(new Question(R.drawable.brinjal, new String[]{"करेला", "तोरी", "बैंगन", "भिंडी"}, 2));
        allQuestions.add(new Question(R.drawable.ladyfinger, new String[]{"बैंगन", "करेला", "भिंडी", "तोरी"}, 2));
    }


    private void loadMonumentQuestions() {
        allQuestions.add(new Question(R.drawable.taj_mahal, new String[]{"ताजमहल", "लाल किला", "कुतुब मीनार", "हवा महल"}, 0));
        allQuestions.add(new Question(R.drawable.red_fort, new String[]{"आगरा किला", "लाल किला", "मेहरानगढ़", "चित्तौड़गढ़"}, 1));
        allQuestions.add(new Question(R.drawable.qutub_minar, new String[]{"चारमीनार", "गेटवे ऑफ इंडिया", "कुतुब मीनार", "विक्टोरिया मेमोरियल"}, 2));
        allQuestions.add(new Question(R.drawable.hawa_mahal, new String[]{"सिटी पैलेस", "हवा महल", "अम्बर पैलेस", "जल महल"}, 1));
        allQuestions.add(new Question(R.drawable.gateway_of_india, new String[]{"इंडिया गेट", "गेटवे ऑफ इंडिया", "चारमीनार", "विक्टोरिया मेमोरियल"}, 1));
        allQuestions.add(new Question(R.drawable.char_minar, new String[]{"चारमीनार", "कुतुब मीनार", "गेटवे ऑफ इंडिया", "हवा महल"}, 0));
        allQuestions.add(new Question(R.drawable.india_gate, new String[]{"इंडिया गेट", "गेटवे ऑफ इंडिया", "अशोक स्तंभ", "विजय स्तंभ"}, 0));
        allQuestions.add(new Question(R.drawable.victoria_memorial, new String[]{"अक्षरधाम मंदिर", "विक्टोरिया मेमोरियल", "बिरला मंदिर", "कमल मंदिर"}, 1));
        allQuestions.add(new Question(R.drawable.sanchi_stupa, new String[]{"हिंदू धर्म", "इस्लाम", "सिख धर्म", "बौद्ध धर्म"}, 3));
        allQuestions.add(new Question(R.drawable.mysore_palace, new String[]{"लाल किला", "हवा महल", "सांची स्तूप", "मैसूर पैलेस"}, 3));
    }


    private void restartQuiz() {
        score = 0;
        currentQuestionIndex = 0;
        Collections.shuffle(allQuestions);
        int questionCount = Math.min(5, allQuestions.size());
        selectedQuestions = allQuestions.subList(0, questionCount);
        showQuestion();
    }
}