package com.example.priceapi.infrastructure.adapter.in.support.util;

import com.example.priceapi.infrastructure.adapter.support.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;


class DateUtilsTest {

    /** Regex para el patrón yyyy-dd-MM'T'HH:mm:ss */
    private static final String TIMESTAMP_REGEX = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}";

    @Test
    void getCurrentTimestampMatchesPattern() {
        String ts = DateUtils.getCurrentTimestamp();
        assertThat(ts)
                .isNotNull()
                .matches(TIMESTAMP_REGEX
                );
    }

    @Test
    void getCurrentTimestampIsParseableWithSameFormatter() throws ParseException {
        String ts = DateUtils.getCurrentTimestamp();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM'T'HH:mm:ss");
        sdf.setLenient(false);
        assertThatCode(() -> sdf.parse(ts))
                .doesNotThrowAnyException();
    }
}