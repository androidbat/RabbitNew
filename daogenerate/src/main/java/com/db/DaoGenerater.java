package com.db;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class DaoGenerater {

    public static void main(String[] args) throws Exception
    {
        Schema schema = new Schema(4, "de.greenrobot.daoexample");

        addNote(schema);

        new DaoGenerator().generateAll(schema, "D:/AndroidStudioProjects/RabbitNew/app/src/main/java-gen");
    }

    private static void addNote(Schema schema)
    {
        Entity note = schema.addEntity("Note");
        note.addIdProperty();
        note.addStringProperty("text").notNull();
        note.addStringProperty("comment");
        note.addDateProperty("date");
        note.addLongProperty("mAaa");
    }
}
