package com.example.pr0103;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.app.ProgressDialog.show;
import static com.example.pr0103.Transform.APP_PREFERENCES;
import static com.example.pr0103.Transform.SaveUser;
import static com.example.pr0103.Transform.StringNoNull;
import static com.example.pr0103.Transform.Vibrate;
import static com.example.pr0103.UserStaticInfo.AGE;
import static com.example.pr0103.UserStaticInfo.LOGIN;
import static com.example.pr0103.UserStaticInfo.NAME;
import static com.example.pr0103.UserStaticInfo.PASSWORD;
import static com.example.pr0103.UserStaticInfo.PROFILE_ID;
import static com.example.pr0103.UserStaticInfo.STATE;
import static com.example.pr0103.UserStaticInfo.USERS_PROFILE_INFO;
import static com.example.pr0103.UserStaticInfo.USERS_SIGN_IN_INFO;
import static com.example.pr0103.UserStaticInfo.profileId;

public class SignActivity extends AppCompatActivity {

    private EditText NewLoginTextView, NewPasswordTextView, NewNameTextView, NewAgeTextView, NewStateTextView;
    private EditText LoginTextView, PasswordTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        Init();

        CheckSignInInfo();
    }

    private void CheckSignInInfo() {
        SharedPreferences sp = getSharedPreferences(APP_PREFERENCES,
                Context.MODE_PRIVATE);
        String login = sp.getString(LOGIN,"");
        String password = sp.getString(PASSWORD,"");
        LoginTextView.setText(login);
        PasswordTextView.setText(password);
        SignIn();
    }

    private void Init() {
        TabHost tabHost = findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");

        //Вкладка "Вход"
        tabSpec.setContent(R.id.tabSignIn);
        tabSpec.setIndicator("Вход");
        //Добавляем в панель вкладок
        tabHost.addTab(tabSpec);

        //Вкладка "Регистрация"
        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.tabSignUp);
        tabSpec.setIndicator("Регистрация");
        //Добавляем в панель вкладок
        tabHost.addTab(tabSpec);
        //устанавливаем первую вкладку активной
        tabHost.setCurrentTab(0);

        LoginTextView = findViewById(R.id.LoginTextView);
        PasswordTextView = findViewById(R.id.PasswordTextView);
        NewLoginTextView = findViewById(R.id.NewLoginTextView);
        NewPasswordTextView = findViewById(R.id.NewPasswordTextView);
        NewNameTextView = findViewById(R.id.NewNameTextView);
        NewAgeTextView = findViewById(R.id.NewAgeTextView);
        NewStateTextView = findViewById(R.id.NewStateTextView);
    }

    public void SignIn(View view){SignIn();}

    public void SignIn(){
        if(EditTextNoNullWithAnimation(PasswordTextView) && EditTextNoNullWithAnimation(LoginTextView)) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(USERS_SIGN_IN_INFO);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String login = getLogin();
                    Object value = dataSnapshot.child(login).child(PASSWORD).getValue();
                    if(value!=null){
                        if(value.toString().equals(getPassword())){
                            goNext(dataSnapshot.child(PROFILE_ID).toString(), login, getPassword());
                        }
                        else CantSignIn();
                    }
                    else CantSignIn();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
        else {
            Vibrate(SignActivity.this);

            Toast.makeText(SignActivity.this,
                    getResources().getText(R.string.NullParametersMessage),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void CantSignIn() {
        Toast.makeText(SignActivity.this,
                getResources().getText(R.string.CantSignInMessage), Toast.LENGTH_SHORT).show();
    }

    private String getLogin() {
        return LoginTextView.getText().toString();
    }

    private String getPassword() {
        return PasswordTextView.getText().toString();
    }

    private String getNewLogin(){
        return NewLoginTextView.getText().toString();
    }

    private String getNewPassword(){
        return NewPasswordTextView.getText().toString();
    }

    private int getNewAge(){
        try{
            return Transform.parseIntOrDefault(NewAgeTextView.getText().toString(),0);
        }
        catch (Exception NumberFormatException){
            return 0;
        }
    }

    private String getNewName(){
        return NewNameTextView.getText().toString();
    }

    private String getNewState(){
        return  NewStateTextView.getText().toString();
    }

    private void goNext(String profileId, String login, String password) {
        SaveUser(getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE),
                getLogin(), getPassword());
        UserStaticInfo.profileId = profileId;
        //Переходим к LoadedUserDateActivity
        Intent intent = new Intent(this, LoadedUserDataActivity.class);
        startActivity(intent);
        //закрываем окно входа, чтобы пользователь не мог к ним вернуться нажав назад
        finish();
    }

    public void SignUp(View view){
        //getNewAge по дефолту 0, можно не проверять
        if(EditTextNoNullWithAnimation(NewLoginTextView) &&
                EditTextNoNullWithAnimation(NewPasswordTextView) &&
                EditTextNoNullWithAnimation(NewNameTextView) &&
                EditTextNoNullWithAnimation(NewStateTextView)){
            FirebaseDatabase database1 = FirebaseDatabase.getInstance();
            DatabaseReference myRef =
                    database1.getReference(USERS_SIGN_IN_INFO).child(getNewLogin());
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.child(PASSWORD).exists()){
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        //id для профиля пользователя его формирует сама Firebase
                        String id = database.getReference(USERS_PROFILE_INFO).push().getKey();
                        String login = getNewLogin();
                        //добавляем пользователя
                        database.getReference(USERS_SIGN_IN_INFO).child(login).child(PASSWORD).setValue(getNewPassword());
                        database.getReference(USERS_SIGN_IN_INFO).child(login).child(PROFILE_ID).setValue(id);
                        //добавляем профиль пользователя
                        database.getReference(USERS_PROFILE_INFO).child(id).child(AGE).setValue(getNewAge());
                        database.getReference(USERS_PROFILE_INFO).child(id).child(NAME).setValue(getNewName());
                        database.getReference(USERS_PROFILE_INFO).child(id).child(STATE).setValue(getNewState());

                        goNext(id, login, getNewPassword());
                    }
                    else
                        Toast.makeText(SignActivity.this,
                                getResources().getText(R.string.UserExitMessage),
                                Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            Vibrate(SignActivity.this);
            Toast.makeText(SignActivity.this,
                    getResources().getText(R.string.NullParametersMessage),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean EditTextNoNullWithAnimation(EditText animationTextView) {
        boolean NoNullText = StringNoNull(animationTextView.getText().toString());
        Animation animation = AnimationUtils.loadAnimation(SignActivity.this,
                R.anim.error_edit);
        if(!NoNullText)
            animationTextView.startAnimation(animation);
        return NoNullText;
    }
}
