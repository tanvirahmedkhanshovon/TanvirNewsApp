package com.tanvir.newsapp.database;

import androidx.room.TypeConverter;

import com.tanvir.newsapp.model.Source;

public class SourceConverter {
    @TypeConverter
    public static Source fromString(String value) {
        Source source = new Source();
        source.setName(value);
        return source;

    }

    @TypeConverter
    public static String fromSource(Source source) {

        return source.getName();
    }
}
