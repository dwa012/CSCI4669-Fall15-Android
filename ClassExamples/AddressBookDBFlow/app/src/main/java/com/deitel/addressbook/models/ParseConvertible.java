package com.deitel.addressbook.models;

import com.parse.ParseException;
import com.parse.ParseObject;

/**
 * Created by danielward on 11/12/15.
 */
public interface ParseConvertible {
    public ParseObject toParseObject() throws ParseException;
    public void fromParseObject(ParseObject object);
}
