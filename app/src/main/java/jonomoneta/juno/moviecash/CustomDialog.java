package jonomoneta.juno.moviecash;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.squareup.picasso.Picasso;

public class CustomDialog extends Dialog {

    public AppCompatActivity activity;
    public Dialog dialog;
    public TouchImageView prevImageView;
    public String image;

    public CustomDialog(@NonNull AppCompatActivity activity, String image) {
        super(activity);
        this.activity = activity;
        this.image = image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        prevImageView = findViewById(R.id.prevImageView);
        String original[] = image.split("/");
        String finalImg = "";
        for (int i = 0; i < original.length; i++) {
            if (i != original.length - 1) {
                finalImg += original[i]+"/";
            } else {
                String tmp = original[i].replace("T_", "");
                finalImg += tmp;
            }

        }
        Log.e("final", finalImg);
        Picasso.get().load(finalImg).placeholder(R.drawable.user).error(R.drawable.user).into(prevImageView);

    }

}
