package com.example.zhamada.gigyapoc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gigya.socialize.android.GSAPI;

public class HostAppMainAcitivity extends AppCompatActivity {
    private  Button mButtonLogout;
    private TextView mTextViewProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_app_main_acitivity);
        mTextViewProfile = (TextView) findViewById(R.id.txtProfile);
        Intent oIntent= getIntent();
        Bundle b = oIntent.getExtras();
        if(b!=null)
        {
            String j =(String) b.get("USER_PROFILE");
            mTextViewProfile.setText(j);
        }

        mButtonLogout = (Button) findViewById(R.id.btnLogout);
        mButtonLogout.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                logoffSession();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
       // fab.setOnClickListener(new View.OnClickListener() {
       //     @Override
       //     public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
       //                 .setAction("Action", null).show();
       //     }
       // });

    }
    private void logoffSession(){
        GSAPI.getInstance().logout();
        finish();
    }

}
