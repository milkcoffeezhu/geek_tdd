package com.geek.tdd.args;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhuxs2
 * @date 2024/4/18 22:35:39
 */
public class Args {
    public static <T> T parse(Class<T> optionsClass, String... args) {
        try {
            Constructor<?> constructor = optionsClass.getDeclaredConstructors()[0];
            List<String> arguments = Arrays.asList(args);

            Object[] values = Arrays.stream(constructor.getParameters()).map(it -> parseOption(it, arguments)).toArray();
            return (T) constructor.newInstance(values);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object parseOption(Parameter parameter, List<String> arguments) {

        Object value = null;
        Option option = parameter.getAnnotation(Option.class);
        if (parameter.getType() == boolean.class) {
            value = arguments.contains("-" + option.value());
        }

        if (parameter.getType() == int.class) {
            if (arguments.isEmpty()) {
                value = 0;
            } else {
                value = Integer.parseInt(arguments.get(arguments.indexOf("-" + option.value()) + 1));
            }
        }

        if (parameter.getType() == String.class) {
            if (arguments.isEmpty()) {
                value = "";
            } else {
                value = arguments.get(arguments.indexOf("-" + option.value()) + 1);
            }
        }
        return value;
    }
}
