package com.example.app.views.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.FacesConverter;
import jakarta.faces.convert.Converter;

import java.util.UUID;

@FacesConverter(forClass = UUID.class)
public class UuidConverter implements Converter<UUID>{

    @Override
    public UUID getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return UUID.fromString(s);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, UUID uuid) {
        return uuid.toString();
    }
}
