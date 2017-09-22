/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ihc.ihc_app.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;

import ihc.ihc_app.FirebaseHelper;
import ihc.ihc_app.R;

public class CodeActivity extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "CodeActivity";


    private EditText mCodeField;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendcode);

        //Firebase
        mAuth = FirebaseAuth.getInstance();


        // Views
        mCodeField = (EditText) findViewById(R.id.input_code);

        // Buttons
        findViewById(R.id.btn_send).setOnClickListener(this);
    }

    private void send(String code) {
        Log.d(TAG, "send:" + code);
        if (!validateForm()) {
            return;
        }

        Query codigoQuery = FirebaseHelper.getAllCodigos();

                if (codigoQuery.equalTo(code)!=null) {
                    Toast.makeText(CodeActivity.this, "Código Válido", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CodeActivity.this, "Código Inválido", Toast.LENGTH_SHORT).show();
                }


    }


    private boolean validateForm() {
        boolean valid = true;

        String code = mCodeField.getText().toString();
        if (TextUtils.isEmpty(code)) {
            mCodeField.setError("Required.");
            valid = false;
        } else {
            mCodeField.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_send) {
            send(mCodeField.getText().toString());
        }
    }


}
