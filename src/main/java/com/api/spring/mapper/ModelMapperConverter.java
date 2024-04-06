package com.api.spring.mapper;

import com.api.spring.model.Person;
import com.api.spring.vo.PersonVO;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class ModelMapperConverter {
    private static ModelMapper modelMapper = new ModelMapper();

    public static <O, D> D parseObject(O origin, Class<D> destination) {
        return modelMapper.map(origin, destination);
    }

    public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {
        List<D> destinationObjects = new ArrayList<>();
        for(O o : origin) {
            destinationObjects.add(modelMapper.map(o, destination));
        }
        return destinationObjects;
    }


}
