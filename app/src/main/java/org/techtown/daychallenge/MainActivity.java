package org.techtown.daychallenge;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.techtown.daychallenge.ui.Category.CategoryFragment;
import org.techtown.daychallenge.ui.Challenge.ChallengeFragment;

public class MainActivity extends AppCompatActivity {
    /*bon
    CategoryFragment categoryFragment;
    ChallengeFragment challengeFragment;
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*bon
        categoryFragment = new CategoryFragment();
        challengeFragment = new ChallengeFragment();
        */
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupWithNavController(navView, navController);
    }
    /*bon
    public void onFragmentChanged(int index){
        if (index == 0){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, challengeFragment).commit();
        }
        else if (index == 1){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, categoryFragment).commit();
        }
    }*/

}
