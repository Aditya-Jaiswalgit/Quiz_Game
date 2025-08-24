package com.bharatiscript.padaayifruits;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        TextView nptel = findViewById(R.id.nptel_btn);
        TextView agt = findViewById(R.id.agt);
        TextView bs = findViewById(R.id.bs);
        Button startQuizBtn = findViewById(R.id.startQuizBtn);

        setupLinkClickListeners(nptel, agt, bs);

        startQuizBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, CategorySelectionActivity.class);
            startActivity(intent);
        });
    }

    private void setupLinkClickListeners(TextView nptel, TextView agt, TextView bs) {
        nptel.setOnClickListener(v -> {
            openUrl("https://nptel.ac.in", "NPTEL");
        });

        agt.setOnClickListener(v -> {
            openUrl("https://www.arvindguptatoys.com/toys.html", "Arvind Gupta Toys");
        });

        bs.setOnClickListener(v -> {
            openUrl("https://www.bharatiscript.com/", "Bharati Script");
        });
    }

    private void openUrl(String url, String siteName) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

            if (isPackageInstalled("com.android.chrome")) {
                browserIntent.setPackage("com.android.chrome");
            } else if (isPackageInstalled("com.android.browser")) {
                browserIntent.setPackage("com.android.browser");
            }

            startActivity(browserIntent);
            Toast.makeText(this, "Opening " + siteName + "...", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            try {
                Intent fallbackIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(fallbackIntent);
                Toast.makeText(this, "Opening " + siteName + "...", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Toast.makeText(this, "Unable to open " + siteName + ". No browser found.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isPackageInstalled(String packageName) {
        try {
            getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MusicService.class));
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