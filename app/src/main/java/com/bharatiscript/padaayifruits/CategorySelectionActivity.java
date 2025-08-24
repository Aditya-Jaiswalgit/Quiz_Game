package com.bharatiscript.padaayifruits;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CategorySelectionActivity extends AppCompatActivity {

    private Button btnFruits, btnAnimals, btnVegetables, btnMonuments;
    private TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_selection);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FrameLayout rootView = findViewById(android.R.id.content);
        View splashView = getLayoutInflater().inflate(R.layout.splash_overlay, null);
        rootView.addView(splashView);

        new Handler().postDelayed(() -> {
            rootView.removeView(splashView);
        }, 3000);

        initViews();
        setupClickListeners();
        startService(new Intent(this, MusicService.class));
    }

    private void initViews() {
        titleText = findViewById(R.id.titleText);
        btnFruits = findViewById(R.id.btnFruits);
        btnAnimals = findViewById(R.id.btnAnimals);
        btnVegetables = findViewById(R.id.btnVegetables);
        btnMonuments = findViewById(R.id.btnMonuments);
    }

    private void setupClickListeners() {
        btnFruits.setOnClickListener(v -> startQuiz("fruits"));
        btnAnimals.setOnClickListener(v -> startQuiz("animals"));
        btnVegetables.setOnClickListener(v -> startQuiz("vegetables"));
        btnMonuments.setOnClickListener(v -> startQuiz("monuments"));
    }

    private void startQuiz(String category) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("CATEGORY", category);
        startActivity(intent);
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
}