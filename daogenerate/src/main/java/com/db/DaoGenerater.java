package com.db;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class DaoGenerater {

    public static void main(String[] args) throws Exception
    {
        Schema schema = new Schema(3, "com.m.rabbit.bean");

        addNote(schema);

        new DaoGenerator().generateAll(schema, "D:/AndroidStudioProjects/RabbitNew/app/src/main/java-gen");
    }

    private static void addNote(Schema schema)
    {
        Entity note = schema.addEntity("User");
        note.addIdProperty().autoincrement();
        note.addStringProperty("name");
        note.addStringProperty("nickName");
        note.addStringProperty("phone");
        note.addIntProperty("gender");
        note.addIntProperty("age");
        note.addIntProperty("type");
    }
}
