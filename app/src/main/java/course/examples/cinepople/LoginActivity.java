package course.examples.cinepople;

import android.os.Bundle;
import android.util.Log; // Import the Log class

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.FirebaseApp; // Add a semicolon

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // It's generally recommended to initialize Firebase in an Application class,
        // but initializing it here will also work.
        FirebaseApp.initializeApp(this); // Add a semicolon
        Log.d("FIREBASE", "Firebase initialized successfully!");

        // The following lines seem to be duplicated. You can keep one set.
        // enableEdgeToEdge(); // This method doesn't exist by default
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}