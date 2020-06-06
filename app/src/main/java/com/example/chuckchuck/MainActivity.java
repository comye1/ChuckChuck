package com.example.chuckchuck;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private Frag1 frag1;
    private Frag2 frag2;
    private Frag3 frag3;

    private static final int REQUEST = 9999;
    private static final int RC_SIGN_NEW = 9002;
    private static final int RC_SIGN_EX = 9003;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST){

            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();

            if(resultCode == RC_SIGN_NEW){
                Toast.makeText(getApplicationContext(), mUser.getEmail() + "로 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
            }else if (resultCode == RC_SIGN_EX){
                Toast.makeText(getApplicationContext(), "로그인 상태입니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                finishAffinity();
            }

            setContentView(R.layout.activity_main);


            bottomNavigationView = findViewById(R.id.bottomNav);

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.action_home:
                            setFrag(0);
                            break;
                        case R.id.action_dashboard:
                            setFrag(1);
                            break;
                        case R.id.action_settings:
                            setFrag(2);
                            break;
                    }
                    return true;
                }
            });

            frag1 = new Frag1();
            frag2 = new Frag2();
            frag3 = new Frag3();
            setFrag(0); //첫 fragment
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //authActivity 통해서 result 받기
        Intent intent = new Intent(this, AuthActivity.class);
        startActivityForResult(intent, REQUEST);

        ///



    }


    //fragment 교체
    private void setFrag(int n){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        switch(n){
            case 0:
                fragmentTransaction.replace(R.id.mainFrame, frag1);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentTransaction.replace(R.id.mainFrame, frag2);
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentTransaction.replace(R.id.mainFrame, frag3);
                fragmentTransaction.commit();
                break;
        }
    }
}
