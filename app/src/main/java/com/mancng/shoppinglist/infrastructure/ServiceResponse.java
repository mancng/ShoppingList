package com.mancng.shoppinglist.infrastructure;

import java.util.HashMap;

//To keep track if the user has made any error
public class ServiceResponse {

    private HashMap<String, String> propertyErrors;

    public ServiceResponse() {
        propertyErrors = new HashMap<>();
    }

    public void setPropertyErrors(String property, String error) {
        propertyErrors.put(property,error);
    }

    public String getPropertyError(String property) {
        return propertyErrors.get(property);
    }

    public boolean didSucceed() {
        return (propertyErrors.size() == 0);
    }
}
