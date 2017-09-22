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
import android.provider.ContactsContract;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ihc.ihc_app.FirebaseHelper;
import ihc.ihc_app.R;
import ihc.ihc_app.models.Topico;

public class InterestsActivity extends BaseActivity {
    private static final String TAG = "TopicosInteresse" ;

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;

    private Button btnSubmit;

    private TopicoAdapter adapter;
    private List<Topico> mList = new ArrayList<>();
    private ListViewCompat mListView;

    public  boolean first = true;

    public InterestsActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        btnSubmit = (Button) findViewById(R.id.btn_submit_topics);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitTopics();
            }
        });

        mListView = (ListViewCompat) findViewById(R.id.topics_list);

        adapter = new TopicoAdapter(this, mList);
        mListView.setAdapter(adapter);

        DatabaseReference topics = FirebaseHelper.getAllTopics().getRef();
        topics.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                    String firebaseKey = (String)childSnapShot.getKey();
                    Topico t = new Topico(Integer.valueOf(firebaseKey),(String) childSnapShot.getValue());
                    mList.add(t);
                    //Log.d(TAG,"Topic::ID::"+t.getId()+"::Name::"+t.getNome());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Read failed: " + databaseError.getMessage());
            }


        });

        //Log.d(TAG, "ALLVALUES::"+adapter.getAllSelectedNames().toString());
    }

    public void submitTopics(){
        DatabaseReference ref = FirebaseHelper.getRef();
        if (mAuth.getCurrentUser() != null){
            FirebaseUser user = mAuth.getCurrentUser();
            showProgressDialog();
            ref.child("usuarios").child(user.getUid()).child("topicos_interesse").setValue(adapter.getAllSelectedNames());
            ref.child("usuarios").child(user.getUid()).child("insertTopics").setValue(false);
            hideProgressDialog();
            moveTo(MainActivity.class, true);
        }else{
            Log.d(TAG, "Submit::Erro");
        }
    }

}