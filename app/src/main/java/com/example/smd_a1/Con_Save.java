package com.example.smd_a1;

import java.util.List;

public interface Con_Save {

    public void save(Person p);
    public void save(List<Person> personList);
    public List<Person> get();
    public Person get(String name);
}
