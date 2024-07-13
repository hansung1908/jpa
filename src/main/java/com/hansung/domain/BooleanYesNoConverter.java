package com.hansung.domain;

import jakarta.persistence.AttributeConverter;

import java.util.Objects;

// implements를 통해 AttributeConverter의 타입 컨버터 기능 구현
public class BooleanYesNoConverter implements AttributeConverter<Boolean, String> {
    // 자바 -> db
    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return Objects.equals(Boolean.TRUE, attribute) ? "Y" : "N";
    }

    // db -> 자바
    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return "Y".equals(dbData) ? true : false;
    }
}

