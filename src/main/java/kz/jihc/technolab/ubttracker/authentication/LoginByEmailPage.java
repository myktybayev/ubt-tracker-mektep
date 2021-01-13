package kz.jihc.technolab.ubttracker.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import kz.jihc.technolab.ubttracker.MainActivity;
import kz.jihc.technolab.ubttracker.R;

public class LoginByEmailPage extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    TextInputEditText email;
    TextInputEditText password;
    FirebaseAuth auth;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        initWidgets();
        auth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(this);
    }
    /*
    mektep@ubt.kz
    123123
     */
    public void initWidgets() {
        btnLogin = findViewById(R.id.btnLogin);
        email = findViewById(R.id.emailToLogin);
        password = findViewById(R.id.passwordToLogin);
        progressBar = findViewById(R.id.progressBarForLogin);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        final String emails = email.getText().toString();
        String passwords = password.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.GONE);

        if (TextUtils.isEmpty(emails) || TextUtils.isEmpty(passwords)) {

            Snackbar.make(btnLogin, getString(R.string.fill_all), 1000).show();
            progressBar.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);

        } else {
            auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(LoginByEmailPage.this, MainActivity.class);
                            startActivity(intent);

                        } else {

                            String subbed = getString(R.string.login_or_pass_error);
                            Snackbar.make(btnLogin, subbed, 1000).setActionTextColor(getResources().getColor(R.color.red)).show();
                            progressBar.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);

                        }
                    });
        }
    }
}
