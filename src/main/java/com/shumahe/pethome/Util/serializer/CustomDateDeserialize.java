package com.shumahe.pethome.Util.serializer;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateDeserialize extends JsonDeserializer<Date> {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        Date date = null;
        try {
            date = sdf.parse(jp.getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
