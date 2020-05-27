package io.github.laskowski.shell.script;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandsMapper {
    private final Class<?> clazz;
    private final Object object;

    public CommandsMapper(Class<?> clazz) {
        this.clazz = clazz;
        object = createNewInstance();
    }

    public List<CommandEntity> getCommands() {
        List<CommandEntity> commandEntities = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            Command command = field.getAnnotation(Command.class);
            if (command != null) {

                Object variableValue;
                String variableValueAsString;
                try {
                    variableValue = field.get(object);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                Class<?> fieldType = field.getType();

                if (fieldType.isAssignableFrom(String.class)) {
                    variableValueAsString = (String) variableValue;
                } else if (fieldType.isAssignableFrom(Optional.class)) {
                    Optional<String> optional = (Optional<String>) variableValue;
                    if (optional.isPresent()) {
                        variableValueAsString = optional.get();
                    } else {
                        continue;
                    }
                } else {
                    throw new IllegalArgumentException(String.format("Field: [%s] has unsupported type: [%s]", field.getName(), fieldType.toString()));
                }

                commandEntities.add(new CommandEntity() {

                    @Override
                    public CommandType getCommandType() {
                        return command.type();
                    }

                    @Override
                    public String getAnnotationValue() {
                        return command.value();
                    }

                    @Override
                    public String getVariableValue() {
                        return variableValueAsString;
                    }
                });
            }

            field.setAccessible(false);
        }

        return commandEntities;
    }

    private Object createNewInstance() {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
