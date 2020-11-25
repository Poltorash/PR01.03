package com.example.pr0103;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static com.example.pr0103.Transform.parseIntOrDefault;
import static com.example.pr0103.UserStaticInfo.POSITION;
import static com.example.pr0103.UserStaticInfo.users;

public class UserActivity extends AppCompatActivity {

    private TextView NameTextView;
    private TextView StateTextView;
    private User activeUser;
    private TextView AgeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        int position = getIntent().getIntExtra(POSITION,0);
        activeUser = users.get(position);
        Init();
        setUserInfo();
    }

    private void setUserInfo() {
        NameTextView.setText(activeUser.getName());
        StateTextView.setText(activeUser.getState());
        AgeTextView.setText(String.valueOf(activeUser.getAge()));
    }

    private void Init() {
        NameTextView = findViewById(R.id.NameTextView);
        StateTextView = findViewById(R.id.StateTextView);
        AgeTextView = findViewById(R.id.AgeTextView);
    }

    public void Back(View view){
        onBackPressed();
    }

    public void Save(View view) {
        //изменяем параметры пользователя
        activeUser.setName(NameTextView.getText().toString());
        activeUser.setState(StateTextView.getText().toString());
        String age = AgeTextView.getText().toString();
        activeUser.setAge(parseIntOrDefault(age, activeUser.getAge()));
        //обновляем список
        MainActivity.UpdateListAndUserPanel(activeUser);
        //закрываем активити
        finish();
    }
}
