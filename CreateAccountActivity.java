/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ihc.ihc_app.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;

import ihc.ihc_app.FirebaseHelper;
import ihc.ihc_app.R;
import ihc.ihc_app.models.Usuario;

public class  CreateAccountActivity extends BaseActivity {

    private static final String TAG = "CreateAccountActivity";
    private Toolbar toolbar;
    private EditText inputName, inputEmail, inputInstitution, inputPassword, inputRepeatPassword;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword, inputLayoutRepeatPassword;
    private Button btnSignUp;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private static Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        //Firebase
        mDatabase = FirebaseHelper.get().getReference();
        mAuth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputLayoutRepeatPassword = (TextInputLayout) findViewById(R.id.input_layout_repeatpassword);
        inputName = (EditText) findViewById(R.id.input_name);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputInstitution = (EditText) findViewById(R.id.input_institution);
        inputPassword = (EditText) findViewById(R.id.input_password);
        inputRepeatPassword = (EditText) findViewById(R.id.input_repeatpassword);
        btnSignUp = (Button) findViewById(R.id.btn_signup);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        inputRepeatPassword.addTextChangedListener(new MyTextWatcher(inputRepeatPassword));

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateName() || !validateEmail() || !validatePassword() || !validateRepeatPassword()) {
            return;
        }

        //Create the user and insert it's data on firebase
        Usuario user = new Usuario(inputName.getText().toString(), inputEmail.getText().toString() , inputInstitution.getText().toString(), inputPassword.getText().toString(), null, null, true, true, 0);
        //Not hacky at all :D
        CreateAccountActivity.user = user;

        //Create firebase account, user account and go to the next screen (interest topics)
        createAccountAndMoveOn(CreateAccountActivity.user);
    }

    public void createAccountAndMoveOn(Usuario user){
        createFirebaseUser(user);
    }

    private void createFirebaseUser(Usuario user){
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(user.email, user.senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            hideProgressDialog();
                            createUserAndMoveOn(CreateAccountActivity.user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, "Falha ao criar conta. Tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createUserAndMoveOn(Usuario user){
        showProgressDialog();
        if(mAuth.getCurrentUser() != null && user != null) {
            mDatabase.child("usuarios").child(mAuth.getCurrentUser().getUid()).setValue(user);
        }
        hideProgressDialog();
        moveOn();
    }

    public void moveOn(){
        startActivity(new Intent(CreateAccountActivity.this, ihc.ihc_app.activity.InterestsActivity.class));
        finish();
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateRepeatPassword() {
        if (inputRepeatPassword.getText().toString().trim().isEmpty() || !Objects.equals(inputRepeatPassword.getText().toString(), inputPassword.getText().toString())){
            inputLayoutRepeatPassword.setError(getString(R.string.err_msg_repeatpassword));
            requestFocus(inputRepeatPassword);
            return false;
        } else {
            inputLayoutRepeatPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
                case R.id.input_repeatpassword:
                    validateRepeatPassword();
                    break;
            }
        }
    }
}