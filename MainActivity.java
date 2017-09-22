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
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ihc.ihc_app.FirebaseHelper;
import ihc.ihc_app.R;
import ihc.ihc_app.models.Usuario;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private FirebaseAuth mAuth;
    private TextView user_initials;
    private TextView user_name;
    private TextView user_email;
    private static Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.drawer_open, R.string.drawer_close) {};
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            DatabaseReference ref = FirebaseHelper.getRef().child("usuarios").child(mAuth.getCurrentUser().getUid());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = Usuario.getUser(dataSnapshot);
                    updateUserUI();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            ref.keepSynced(true);
        }
        View headerLayout =  navigationView.getHeaderView(0);
        user_initials = (TextView) headerLayout.findViewById(R.id.user_initials);
        user_name = (TextView) headerLayout.findViewById(R.id.user_full_name);
        user_email = (TextView) headerLayout.findViewById(R.id.user_email);

        displayView(R.id.nav_sessions);

    }

    public void onStart() {
        super.onStart();
        // Check auth on Activity start
        updateUserUI();
    }

    public void updateUserUI(){
        if(user != null) {
            String fullname = user.nome + " " + user.sobrenome;
            user_initials.setText(getInitials(fullname));
            user_name.setText(fullname);
            user_email.setText(user.email);
        }else{
            user_initials.setText(R.string.du);
            user_name.setText(R.string.duname);
            user_email.setText(R.string.duemail);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                /*final Bundle bundle = new Bundle();
                final Intent intent = new Intent(MainActivity.class, SearchActivity.class);
                intent.putExtra(SearchManager.QUERY, query);
                intent.putExtras(bundle);
                startActivity(intent);*/

                return true;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                //TODO write your code what you want to perform on search text change
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displayView(item.getItemId());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayView(int id) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (id) {
            case R.id.nav_sessions:
                fragment = new SessaoFragment();
                title = getString(R.string.nav_item_sessions);
                break;
            case R.id.nav_favorites:
                fragment = new FavoritesFragment();
                title = getString(R.string.nav_item_favorites);
                break;
            case R.id.nav_recommended:
                fragment = new RecommendedFragment();
                title = getString(R.string.nav_item_recommended);
                break;
            case R.id.nav_settings:
                fragment = new FavoritesFragment();
                title = getString(R.string.nav_item_settings);
                break;
            case R.id.nav_desafio:
                fragment = new DesafiosFragment();
                title = getString(R.string.nav_item_desafio);
                break;
            case R.id.nav_codigo:
                callActivity(CodeActivity.class);
                title = getString(R.string.nav_item_codigo);
                break;
            case R.id.nav_ranking:
                fragment = new RankingFragment();
                title = getString(R.string.nav_item_ranking);
                break;
            case R.id.nav_escala:
                fragment = new EscalaFragment();
                title = getString(R.string.nav_item_escala);
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                finish();
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    public static String getInitials(String name) {
        //TODO: Refactor nessa gambiarra-monstro
        StringBuilder initials = new StringBuilder();
        boolean addNext = true;
        if (name != null) {
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);
                if (c == ' ' || c == '-' || c == '.') {
                    addNext = true;
                } else if (addNext) {
                    initials.append(c);
                    addNext = false;
                }
            }
        }
        StringBuilder two_initials = new StringBuilder();
        two_initials.append(initials.toString().charAt(0));
        two_initials.append(initials.toString().charAt(initials.toString().length()-1));
        return two_initials.toString();
    }

    private void callActivity(Class c){
        Intent it = new Intent(this, c);
        startActivity(it);
    }
}