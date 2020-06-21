package com.fiek.regionalsnowforecast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Region;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivRegisterBack;
    TextView tvBackToLogin;
    EditText etName, etEmail, etAddress, etRegion, etPassword, etConfirmPassword;
    Button btnRegister;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseDatabase userDatabase;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();


        ivRegisterBack = findViewById(R.id.ivRegisterBack);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etRegion = findViewById(R.id.etRegion);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.registerProgressBar);
        TextView tvSignup = findViewById(R.id.tvSignUp);
        ConstraintLayout registerBackground = findViewById(R.id.registerBackground);
        tvSignup.setOnClickListener(this);
        registerBackground.setOnClickListener(this);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addUsers();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                try {
                    if (checkRegisterInput()) {
                        progressBar.setVisibility(View.GONE);
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    finish();
                                    Intent intent = new Intent(RegisterActivity.this, ResortsActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.user_signed_up), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                } catch (Exception ex) {
                    Log.e("asd", ex.getMessage());
                }
            }
        });


        ivRegisterBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.rightenter, R.anim.rightexit);
            }
        });

        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.rightenter, R.anim.rightexit);
            }
        });
    }

    public void addUsers() {
        userDatabase = FirebaseDatabase.getInstance();
        reference = userDatabase.getReference("users");
        final String name = etName.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String address = etAddress.getText().toString().trim();
        final String region = etRegion.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        try {
            if (checkRegisterInput()) {
                progressBar.setVisibility(View.GONE);
                String id = reference.push().getKey();
                User user = new User(name, email, address, region, id);

                reference.child(id).setValue(user);
            }
        } catch (Exception ex) {
            Log.e("Sign Up Error: ", ex.getMessage());
        }
    }

    public static boolean isEmail(EditText etInput) {
        CharSequence email = etInput.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean isEmpty(EditText etInput) {
        CharSequence str = etInput.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public static boolean passwordsMatch(EditText etPass, EditText etConfirmPass) {
        CharSequence strPass = etPass.getText().toString();
        CharSequence strConfirmPass = etConfirmPass.getText().toString();
        return strPass.equals(strConfirmPass);
    }

    public static boolean passwordValidation(EditText etInput) {
        CharSequence strPassword = etInput.getText().toString();
        Pattern PASSWORD_PATTERN = Pattern.compile("[a-zA-Z0-9\\!\\@\\#\\$\\.]{8,24}");

        return !TextUtils.isEmpty(strPassword) && PASSWORD_PATTERN.matcher(strPassword).matches();
    }

    public boolean checkRegisterInput() {

        if (isEmpty(etName)) {
            etName.setError(getText(R.string.nameerror));
            etName.requestFocus();
            return false;
        } else if (!isEmail(etEmail)) {
            etEmail.setError(getText(R.string.emailerror));
            etEmail.requestFocus();
            return false;
        } else if (isEmpty(etAddress)) {
            etAddress.setError(getText(R.string.addresserror));
            etAddress.requestFocus();
            return false;
        } else if (isEmpty(etRegion)) {
            etRegion.setError(getText(R.string.regionerror));
            etRegion.requestFocus();
            return false;
        } else if (!passwordValidation(etPassword)) {
            etPassword.setError(getText(R.string.passwordlengtherror));
            etPassword.requestFocus();
            return false;
        } else if (isEmpty(etConfirmPassword)) {
            etConfirmPassword.setError(getText(R.string.emptyconfirmpassword));
            etConfirmPassword.requestFocus();
            return false;
        } else if (!passwordsMatch(etPassword, etConfirmPassword)) {
            Toast.makeText(RegisterActivity.this, getText(R.string.passwordmatcherror), Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvSignUp || v.getId() == R.id.registerBackground) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}