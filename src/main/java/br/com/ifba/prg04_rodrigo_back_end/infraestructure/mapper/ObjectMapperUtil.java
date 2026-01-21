package br.com.ifba.prg04_rodrigo_back_end.infraestructure.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies; // <--- Importante
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ObjectMapperUtil {

    private final ModelMapper modelMapper;

    public ObjectMapperUtil() {
        this.modelMapper = new ModelMapper();

        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    public <D, T> D map(final T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

    public <D, T> List<D> mapAll(final List<T> entityList, Class<D> outClass) {
        return entityList.stream()
                .map(entity -> map(entity, outClass))
                .collect(Collectors.toList());
    }
}