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
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ihc.ihc_app.FirebaseHelper;
import ihc.ihc_app.R;
import ihc.ihc_app.models.Usuario;

public class ChoserActivity extends BaseActivity {
    private static final String TAG = "ChoserActivity" ;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private Toolbar mToolbar;

    public ChoserActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choser);

        //Firebase
        mDatabase = FirebaseHelper.getRef();
        mAuth = FirebaseAuth.getInstance();


    }

    public void onStart() {
        super.onStart();
        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            DatabaseReference ref = mDatabase.child("usuarios").child(mAuth.getCurrentUser().getUid());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    checkNextScreen(Usuario.getUser(dataSnapshot));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            ref.keepSynced(true);
        }else{
            moveTo(SignInActivity.class, true);
        }
    }

    private void checkNextScreen(Usuario user){
        if(user.nome == null || user.sobrenome == null || user.email == null){
            mAuth.signOut();
            moveTo(SignInActivity.class, true);
        }else if(user.insertTopics){
            moveTo(InterestsActivity.class, true);
        }else{
            moveTo(MainActivity.class, true);
        }
    }

}