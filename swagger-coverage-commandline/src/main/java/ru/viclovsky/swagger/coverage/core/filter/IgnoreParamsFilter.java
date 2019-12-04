package ru.viclovsky.swagger.coverage.core.filter;

import io.swagger.models.Operation;
import io.swagger.models.ParamType;
import io.swagger.models.parameters.Parameter;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IgnoreParamsFilter implements SwaggerCoverageFilter {

    private final static Logger LOGGER = Logger.getLogger(IgnoreParamsFilter.class);

    private List<String> names;
    private ParamType type;

    public IgnoreParamsFilter(List<String> names, ParamType type) {
        this.names = names;
        this.type = type;
    }

    @Override
    public void apply(Operation operation) {
        if (Objects.nonNull(operation.getParameters())) {
            Set<Parameter> params = operation.getParameters().stream()
                    .filter(equalsParam(names, type)).collect(Collectors.toSet());

            params.forEach(p -> {
                LOGGER.debug(String.format("Remove %s: [%s]", p.getIn(),
                        p.getName()));
                operation.getParameters().remove(p);
            });

        }
    }

    private static Predicate<Parameter> equalsParam(List<String> names, ParamType type) {
        return p -> (names.contains(p.getName()) && p.getIn().equals(type.toValue()));
    }
}