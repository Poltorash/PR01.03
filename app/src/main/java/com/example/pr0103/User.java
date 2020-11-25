package com.example.pr0103;

import com.google.firebase.database.DataSnapshot;

import static com.example.pr0103.Transform.parseIntOrDefault;
import static com.example.pr0103.UserStaticInfo.AGE;
import static com.example.pr0103.UserStaticInfo.NAME;
import static com.example.pr0103.UserStaticInfo.STATE;

public class User {
    private String Name, State;
    private int Age;

    public User(DataSnapshot dataSnapshot) {
        Object NameObj = dataSnapshot.child(NAME).getValue();
        if(NameObj!=null)
            Name = NameObj.toString();
        Object StateObj = dataSnapshot.child(STATE).getValue();
        if(StateObj!=null)
            State = StateObj.toString();
        Object AgeObj = dataSnapshot.child(AGE).getValue();
        if(AgeObj!=null)
            Age = parseIntOrDefault(AgeObj.toString(),0);
    }

    public int getStateSignal(){
        return StateSignal;
    }

    public void setStateSignal(int stateSignal) {
        StateSignal = stateSignal;
    }

    public User(String name, String state, int age, int stateSignal){
        Name = name;
        State = state;
        Age = age;
        StateSignal = stateSignal;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getState() {
        return State;
    }

    private int StateSignal;

    public void setState(String state) {
        State = state;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }
}
