package com.seizthat.seizethatparse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.logging.Logger;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "8E4dpuoNY8IK5OVqeiV2TEVRruUL1b25DKFlvY6U", "8vaWuxjVYv0a4pe86jQoCG1BDJwnuuFvHQ1sXvO7");
        userCheck();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void login(View view){
        EditText emailEditText = (EditText) findViewById(R.id.email);
        EditText passEditText = (EditText) findViewById(R.id.password);

        ParseUser.logInInBackground(emailEditText.getText().toString(), passEditText.getText().toString(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    if(user.getBoolean("isBusiness")){
                        if(user.getBoolean("profile") == false){
                            Intent intent = new Intent(LoginActivity.this, BusinessProfileActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),
                                    "Login Successful!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Intent intent = new Intent(LoginActivity.this, BusinessActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),
                                    "Login Successful!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(LoginActivity.this, ConsumerActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),
                                "Login Successful!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Password/Username incorrect. Try again or Sign up",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void signUp(View view){
        ParseUser user = new ParseUser();

        EditText emailEditText = (EditText) findViewById(R.id.email);
        EditText passEditText = (EditText) findViewById(R.id.password);
        CheckBox businessCheckBox = (CheckBox) findViewById(R.id.businessCheckbox);

        user.setUsername(emailEditText.getText().toString());
        user.setPassword(passEditText.getText().toString());
        user.setEmail(emailEditText.getText().toString());

        user.put("isBusiness", businessCheckBox.isChecked());
        user.put("profile", false);

        // other fields can be set just like with ParseObject

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    ParseUser user = ParseUser.getCurrentUser();
                    if(user.getBoolean("isBusiness")){
                        if(user.getBoolean("profile") == false){
                            Intent intent = new Intent(LoginActivity.this, BusinessProfileActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),
                                    "Sign up Successful!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Intent intent = new Intent(LoginActivity.this, BusinessActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),
                                    "Sign up Successful!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(LoginActivity.this, ConsumerActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),
                                "Sign up Successful!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }

    private void userCheck(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            if(currentUser.getBoolean("isBusiness")){
                Intent intent = new Intent(this, BusinessActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, ConsumerActivity.class);
                startActivity(intent);
            }
        } else {
            setContentView(R.layout.activity_my);
        }
    }
}
