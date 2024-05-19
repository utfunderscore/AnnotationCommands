package com.readutf.commands.core.adapter.primitive;

import com.readutf.commands.core.param.CommandParameter;
import com.readutf.commands.core.adapter.TypeAdapter;
import com.readutf.commands.core.exception.TypeAdapterException;

import java.util.ArrayList;
import java.util.List;

public class EnumParser implements TypeAdapter<Enum> {

    @Override
    public Enum<?> adapt(String parameterValue, CommandParameter commandParameter) throws TypeAdapterException {

        Class<?> parameterType = commandParameter.getType();
        if(!parameterType.isAssignableFrom(Enum.class)) {
            throw new IllegalArgumentException("Parameter type is not an enum");
        }

        Class<? extends Enum> enumType = parameterType.asSubclass(Enum.class);

        List<String> availableEnums = new ArrayList<>();

        for (Enum<?> enumConstant : enumType.getEnumConstants()) {
            if (enumConstant.name().equalsIgnoreCase(parameterValue)) {
                return enumConstant;
            }
        }

        throw new TypeAdapterException("Failed to convert " + parameterValue + " to an enum");
    }
}
