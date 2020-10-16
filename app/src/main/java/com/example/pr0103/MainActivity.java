package com.example.pr0103;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //ListView
    ListView listView;
    //контекст
    Context context;
    LayoutInflater layoutInflater;
    //список пользователей
    List<User> users = new ArrayList<>();
    //адаптер для отображения
    UserListAdapter userListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AddUsersInList();
        Init();
    }

    private void AddUsersInList() {
        users.add(new User("Анастасия", "можно уже все", 19));
        users.add(new User("Анастасия", "можно уже все", 19));
        users.add(new User("Анастасия", "можно уже все", 19));
        users.add(new User("Анастасия", "можно уже все", 19));
        users.add(new User("Анастасия", "можно уже все", 19));
    }

    private void Init() {
        //получаем ListView
        listView = findViewById(R.id.listView);
        //инициализируем контекст
        context = this;
        //инициализируем LayoutInflater
        layoutInflater = LayoutInflater.from(context);
        UserListAdapter userListAdapter = new UserListAdapter();
        //устанавливаем адаптер
        listView.setAdapter(userListAdapter);
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
        public View getView(int position, View currentView, ViewGroup parent) {
            //пользователь из списка
            User currentUser = getItem(position);
            //"наполняем" view разметкой "item_user"
            currentView = layoutInflater.inflate(R.layout.item_user, parent, false);

            //получаем NameTextView из currentView(который содержит R.Layout.item_user)
            TextView nameView = currentView.findViewById(R.id.NameTextView);
            //получаем StateTextView из currentView(который содержит R.Layout.item_user)
            TextView stateView = currentView.findViewById(R.id.StateTextView);

            //устанавливаем нужный текст
            nameView.setText(currentUser.getName());
            stateView.setText(currentUser.getState());
            //возвращаем отображаемый элемент списка
            return currentView;
        }
    }
}
