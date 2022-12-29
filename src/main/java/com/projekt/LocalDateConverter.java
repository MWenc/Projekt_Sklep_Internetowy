package com.projekt;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
/**
 * Klasa dokonuje konwersji typów z datą
 * @author Michał Wenc
 * @version 1.0
 */
@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

    /**
     * Metoda ta konwertuje dane typu LocalDate na Date
     *
     * @param localDate
     * @return
     */
    @Override
    public Date convertToDatabaseColumn(LocalDate localDate) {
        return Optional.ofNullable(localDate)
                .map(Date::valueOf)
                .orElse(null);
    }

    /**
     * Metoda ta konwertuje dane typu Date na LocalDate
     *
     * @param date
     * @return
     */
    @Override
    public LocalDate convertToEntityAttribute(Date date) {
        return Optional.ofNullable(date)
                .map(Date::toLocalDate)
                .orElse(null);
    }
}
