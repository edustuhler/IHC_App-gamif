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

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.transition.Visibility;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ihc.ihc_app.FirebaseHelper;
import ihc.ihc_app.R;
import ihc.ihc_app.models.Sessao;
import ihc.ihc_app.models.Trabalho;
import ihc.ihc_app.models.Usuario;

public class DetailsActivity extends BaseActivity {

    private static String TAG = DetailsActivity.class.getSimpleName();
    public static final String EXTRA_SESSION_KEY = "session_key";

    private Toolbar toolbar;
    private String mSessionkey;
    private DatabaseReference mSessionReference;
    private TextView mType, mTitle, mDescriptionBar, mDescription, mDate, mTimeStart, mTimeEnd;
    private ListView mTrabalhos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mSessionkey = getIntent().getStringExtra(EXTRA_SESSION_KEY);
        if (mSessionkey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_SESSION_KEY");
        }

        mSessionReference = FirebaseHelper.getAllSessions().getRef().child(mSessionkey);
        mSessionReference.keepSynced(true);

        mType = (TextView)findViewById(R.id.type_bar);
        mTitle = (TextView)findViewById(R.id.title);
        mDescriptionBar = (TextView)findViewById(R.id.description_bar);
        mDescription = (TextView)findViewById(R.id.description);
        mTrabalhos = (ListView)findViewById(R.id.trabalhos);
        mDate = (TextView)findViewById(R.id.date);
        mTimeStart = (TextView)findViewById(R.id.time_start);
        mTimeEnd = (TextView)findViewById(R.id.time_end);
    }

    public void onStart() {
        super.onStart();
        ValueEventListener sessionListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Sessao tmp = new Sessao();
                tmp.UID = mSessionkey;
                for( DataSnapshot sessionData : dataSnapshot.getChildren()){
                    tmp.addInfo(sessionData.getKey(), sessionData.getValue());
                }
                updateUI(tmp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(DetailsActivity.this, "Falha ao recuperar dados da sessão. Tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
            }
        };
        mSessionReference.addValueEventListener(sessionListener);
    }

    public void updateUI(Sessao s){
        mTitle.setText(s.getNome());
        if(s.getTrabalhos().size() > 0){
            mDescriptionBar.setText("Trabalhos");
            mDescription.setVisibility(View.GONE);

            TrabalhoAdapter adapter = new TrabalhoAdapter(getApplicationContext(), s.getTrabalhos());
            mTrabalhos.setAdapter(adapter);
            mTrabalhos.setVisibility(View.VISIBLE);

        }else{
            mDescription.setText(s.getDescricao());
            mTrabalhos.setVisibility(View.GONE);
            mDescription.setVisibility(View.VISIBLE);

        }
        mDate.setText(Sessao.getDate(s.getTimestamp_start()));
        mTimeStart.setText(Sessao.getTime(s.getTimestamp_start()));
        mTimeEnd.setText(Sessao.getTime(s.getTimestamp_start()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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
}