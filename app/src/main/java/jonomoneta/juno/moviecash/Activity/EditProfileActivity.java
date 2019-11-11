package jonomoneta.juno.moviecash.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import jonomoneta.juno.moviecash.CustomEditText;
import jonomoneta.juno.moviecash.CustomRadioButton;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.ImageFilePath;
import jonomoneta.juno.moviecash.Model.Response.UserDetailsResponse;
import jonomoneta.juno.moviecash.MyApplication;
import jonomoneta.juno.moviecash.PreferenceSettings;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.PermissionUtils;
import jonomoneta.juno.moviecash.Utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static jonomoneta.juno.moviecash.Activity.ProfileActivity.editProf;

public class EditProfileActivity extends AppCompatActivity {

    private CustomTextView bDateTextView;
    private ImageView backBtn, btnDone, calendarBtn, pickImgBtn;
    private Calendar myCalendar;
    private CircleImageView profileImageView;
    private CustomEditText userNameEditText, refferalCodeEditText;
    private PreferenceSettings mPreferenceSettings;
    private ProgressBar progressBar;
    private String gender = "male", base64String = "", selectedType = "";
    private LinearLayout refLL;
    private boolean isNewUser;
    private View refView;
    private Bitmap bMap;
    private CustomRadioButton maleRadioBtn, femaleRadioBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();

        initialize();
    }

    private void initialize() {
        myCalendar = Calendar.getInstance();
        backBtn = findViewById(R.id.backBtn);
        btnDone = findViewById(R.id.btnDone);
        calendarBtn = findViewById(R.id.calendarBtn);
        bDateTextView = findViewById(R.id.bDateTextView);
        pickImgBtn = findViewById(R.id.pickImgBtn);
        profileImageView = findViewById(R.id.profileImageView);
        userNameEditText = findViewById(R.id.userNameEditText);
        refferalCodeEditText = findViewById(R.id.refferalCodeEditText);
        progressBar = findViewById(R.id.progressBar);
        refLL = findViewById(R.id.refLL);
        refView = findViewById(R.id.refView);
        maleRadioBtn = findViewById(R.id.maleRadioBtn);
        femaleRadioBtn = findViewById(R.id.femaleRadioBtn);

        isNewUser = getIntent().getBooleanExtra("isNewUser", false);

        if (isNewUser) {
            refLL.setVisibility(View.VISIBLE);
            refView.setVisibility(View.VISIBLE);
        } else {
            refLL.setVisibility(View.GONE);
            refView.setVisibility(View.GONE);
        }

        try {

            if (mPreferenceSettings.getUserDetails().getResponseData() != null) {
                if (mPreferenceSettings.getUserDetails().getResponseData().getMovieTypeIDs() != null &&
                        mPreferenceSettings.getUserDetails().getResponseData().getMovieTypeIDs().length() > 0) {
                    selectedType = mPreferenceSettings.getUserDetails().getResponseData().getMovieTypeIDs();
                }
                if (mPreferenceSettings.getUserDetails().getResponseData().getName() != null &&
                        mPreferenceSettings.getUserDetails().getResponseData().getName().length() > 0) {
                    userNameEditText.setText(mPreferenceSettings.getUserDetails().getResponseData().getName());
                } else {
                    userNameEditText.setText("");
                }
                if (mPreferenceSettings.getUserDetails().getResponseData().getProfilePicture() != null &&
                        mPreferenceSettings.getUserDetails().getResponseData().getProfilePicture().length() > 0) {
                    String profilePicArray[] = mPreferenceSettings.getUserDetails().getResponseData().getProfilePicture().split("/");
                    base64String = profilePicArray[profilePicArray.length - 1];
                }
                if (mPreferenceSettings.getUserDetails().getResponseData().getGender() != null &&
                        mPreferenceSettings.getUserDetails().getResponseData().getGender().length() > 0) {
                    gender = mPreferenceSettings.getUserDetails().getResponseData().getGender();
                    if (gender.equalsIgnoreCase("male")) {
                        maleRadioBtn.setChecked(true);
                    } else {
                        femaleRadioBtn.setChecked(true);
                    }
                }
                Picasso.get().load(mPreferenceSettings.getUserDetails().getResponseData().getProfilePicture())
                        .placeholder(R.drawable.user).error(R.drawable.user).into(profileImageView);
                if (mPreferenceSettings.getUserDetails().getResponseData().getBirthDate().length() > 0) {
                    String dateTimeArray[] = mPreferenceSettings.getUserDetails().getResponseData().getBirthDate().split("T");
                    String brthdate = dateTimeArray[0];
                    if (Integer.parseInt(brthdate.split("-")[0]) != 1) {
                        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date date = format2.parse(brthdate);
                            bDateTextView.setText(format1.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        bDateTextView.setText("");
                    }
                } else {
                    bDateTextView.setText("");
                }

            }

        } catch (Exception e) {
            Log.e("excptn", e.getMessage());
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utility.isOnline(EditProfileActivity.this)) {
                    saveProfileDetailsAPI();
                } else {
                    Utility.noInternetError(EditProfileActivity.this);
                }

            }
        });

        bDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideKeyboard(EditProfileActivity.this);
                if (bDateTextView.getText().length() > 0) {
                    String date[] = bDateTextView.getText().toString().split("/");
                    myCalendar.set(Calendar.YEAR, Integer.parseInt(date[2]));
                    myCalendar.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
                    myCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[0]));
                }
                new DatePickerDialog(EditProfileActivity.this, R.style.datepicker,
                        date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideKeyboard(EditProfileActivity.this);
                if (bDateTextView.getText().length() > 0) {
                    String date[] = bDateTextView.getText().toString().split("/");
                    myCalendar.set(Calendar.YEAR, Integer.parseInt(date[2]));
                    myCalendar.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
                    myCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[0]));
                }
                new DatePickerDialog(EditProfileActivity.this, R.style.datepicker,
                        date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtils.requestPermission(EditProfileActivity.this, 1,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                }
            }
        });

        pickImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtils.requestPermission(EditProfileActivity.this, 1,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri selectedImage = data.getData();

                        String filePath = ImageFilePath.getPath(EditProfileActivity.this, selectedImage);
                        Log.e("imagePath", filePath);
                        bMap = exifInterface(filePath);
                        profileImageView.setImageBitmap(bMap);
                        base64String = Utility.getBase64(bMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private Bitmap exifInterface(String photoPath) {
        ExifInterface ei = null;
        Bitmap bitMp = BitmapFactory.decodeFile(String.valueOf(photoPath));
        try {
            ei = new ExifInterface(String.valueOf(photoPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = 0;
        if (ei != null) {
            orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
        }

        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                bMap = rotateBitmap(bitMp, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                bMap = rotateBitmap(bitMp, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                bMap = rotateBitmap(bitMp, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                bMap = bitMp;
        }
        return bMap;
    }

    public Bitmap rotateBitmap(Bitmap source, int ang) {
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(ang);
            bMap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
            return bMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveProfileDetailsAPI() {

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        String name;
        if (userNameEditText.getText().toString().trim().length() > 0) {
            name = userNameEditText.getText().toString().trim();
        } else {
            name = "";
        }
        String mobileNo = mPreferenceSettings.getMobileNumber();
        int id = 0;
        if (mPreferenceSettings.getUserDetails().getResponseData() != null) {
            id = mPreferenceSettings.getUserDetails().getResponseData().getID();
        }
        String bDate = null;
        if (bDateTextView.getText().toString().trim().length() > 0) {
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Date date = format1.parse(bDateTextView.getText().toString().trim());
                bDate = format2.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            bDate = "";
        }

        String modieTypeIds = "";
        String refCode = "";
        if (refferalCodeEditText.getText().toString().trim().length() > 0) {
            refCode = refferalCodeEditText.getText().toString().trim();
        }
        if (maleRadioBtn.isChecked()) {
            gender = "male";
        } else if (femaleRadioBtn.isChecked()) {
            gender = "female";
        }

        if (name.length() > 0) {
            if (bDate.length() > 0) {
                if (base64String.length() > 5) {
                    shwoProgress();
                    Call<UserDetailsResponse> saveUserDetails = retroInterface.saveUserProfile(id, name, mobileNo, bDate,
                            modieTypeIds, refCode, base64String, gender);
                    saveUserDetails.enqueue(new Callback<UserDetailsResponse>() {
                        @Override
                        public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                            hideProgress();
                            try {
                                if (response.body() != null) {
                                    if (response.body().getResponseCode() != null && response.body().getResponseCode().length() > 0) {
                                        if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                                            mPreferenceSettings.setUserDetails(response.body());
                                            if (mPreferenceSettings.getIsNewUser()) {
                                                startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                                                finish();
                                                mPreferenceSettings.setIsNewUser(false);

                                            } else {
                                                editProf = true;
                                                finish();
                                            }
                                        } else {
                                            Toast.makeText(EditProfileActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                            hideProgress();
                            Toast.makeText(EditProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(this, "Please select profile picture.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please select birth date.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter user name.", Toast.LENGTH_SHORT).show();
        }

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            Calendar curCalendar = Calendar.getInstance();
            if (year <= (curCalendar.get(Calendar.YEAR) - 18)) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                String apiFormat = "yyyy/MM/dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                SimpleDateFormat sdf2 = new SimpleDateFormat(apiFormat, Locale.US);

                bDateTextView.setText(sdf.format(myCalendar.getTime()));
            } else {
                bDateTextView.setText("");
                Toast.makeText(EditProfileActivity.this, "You must be atleast 18 !", Toast.LENGTH_SHORT).show();
            }

        }
    };

    private void shwoProgress() {

        progressBar.setVisibility(View.VISIBLE);
        btnDone.setVisibility(View.GONE);
    }

    private void hideProgress() {

        progressBar.setVisibility(View.GONE);
        btnDone.setVisibility(View.VISIBLE);
    }
}
