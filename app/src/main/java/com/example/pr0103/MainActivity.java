package com.example.pr0103;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.pr0103.UserStaticInfo.POSITION;
import static com.example.pr0103.UserStaticInfo.users;

public class MainActivity extends AppCompatActivity {
    //ListView
    ListView listView;
    //контекст
    Context context;
    LayoutInflater layoutInflater;
    //адаптер для отображения
    static UserListAdapter userListAdapter;
    //панель профиля пользователя
    FrameLayout UserPanel;
        static TextView NameTextView;
    static TextView StateTextView;
    static TextView AgeTextView;
    private int positionActiveUser;

    public static void UpdateListAndUserPanel(User user) {
        //обновление ListView
        userListAdapter.notifyDataSetChanged();
        InitPanel(user);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new UserStaticInfo();
        Init();
    }

    private static void InitPanel(User item) {
        NameTextView.setText(item.getName());
        StateTextView.setText(item.getState());
        AgeTextView.setText(String.valueOf(item.getAge()));
    }
    private void Init() {
        //инициализируем UserPanel
        UserPanel = findViewById(R.id.userPanel);
            NameTextView = findViewById(R.id.NameTextView);
            StateTextView = findViewById(R.id.StateTextView);
            AgeTextView = findViewById(R.id.AgeTextView);
        //получаем ListView
        listView = findViewById(R.id.listView);
        //инициализируем контекст
        context = this;
        //инициализируем LayoutInflater
        layoutInflater = LayoutInflater.from(context);
        //инициализируем адаптер
        userListAdapter = new UserListAdapter();
        //устанавливаем адаптер
        listView.setAdapter(userListAdapter);
    }

    public void GoToUserProfile(int position){
        //Намерение открытия UserActivity
        Intent intent = new Intent(context, UserActivity.class);
        //Передаем в активити позицию элемента списка
        intent.putExtra(POSITION, position);
        startActivity(intent);
    }

    public void BackToList(View view) {
        UserVisibility(false);
    }

    private void UserVisibility(boolean visible) {
        if(visible)
            UserPanel.setVisibility(View.VISIBLE);
        else
            UserPanel.setVisibility(View.GONE);
    }

    public void EditUser(View view) {
        GoToUserProfile(positionActiveUser);
    }

    private class UserListAdapter extends BaseAdapter {
        /**
         * Возвращает длину списка пользователей
         * @return длина списка пользователей
         */

        @Override
        public int getCount() {
            return users.size();
        }

        /**
         * Возвращает объект из списка пользователей
         * @return объект из списка пользователей
         */

        @Override
        public User getItem(int position) {
            return users.get(position);
        }

        /**
         * Возвращает позицию объекта в списке пользователей
         * @return позиция объекта в списке пользователей
         */

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * Создает отображаемый элемент списка
         * @param position позиция
         * @param currentView view которое будем возвращать
         * @param parent родитель - ViewGroup
         * @return отображаемый элемент списка
         */

        @Override
        public View getView(final int position, View currentView, ViewGroup parent) {
            //пользователь из списка
            User currentUser = getItem(position);
            //"наполняем" view разметкой "item_user"
            currentView = layoutInflater.inflate(R.layout.item_user, parent, false);

            //получаем NameTextView из currentView(который содержит R.Layout.item_user)
            TextView nameView = currentView.findViewById(R.id.NameTextView);
            //получаем StateTextView из currentView(который содержит R.Layout.item_user)
            TextView stateView = currentView.findViewById(R.id.StateTextView);

            //получаем StateRound из currentView(который содержит R.Layout.item_user)
            FrameLayout StateRound = currentView.findViewById(R.id.StateRound);
            //устанавливаем отображение статуса сигнала
            switch (currentUser.getStateSignal()){
                case 0:
                    StateRound.setBackgroundResource(R.drawable.back_offline);
                    break;
                case 1:
                    StateRound.setBackgroundResource(R.drawable.back_online);
                    break;
                case 2:
                    StateRound.setBackgroundResource(R.drawable.back_departed);
                    break;
            }

            //устанавливаем нужный текст
            nameView.setText(currentUser.getName());
            stateView.setText(currentUser.getState());

            //назначаем событие при клике
            currentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    positionActiveUser = position;
                    InitPanel(getItem(position));
                    UserVisibility(true);
                }
            });
            //возвращаем отображаемый элемент списка
            return currentView;
        }
    }
}
