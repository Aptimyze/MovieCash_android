package jonomoneta.juno.moviecash.Activity;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import jonomoneta.juno.moviecash.R;

import android.os.Bundle;
import android.view.View;

public class HelpActivity extends AppCompatActivity {

    CircleImageView closeBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        closeBTN = findViewById(R.id.closeBTN);

        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
