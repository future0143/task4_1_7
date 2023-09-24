package validator.matcher;

import org.hamcrest.Matcher;

import java.time.LocalDateTime;

public class DateMatchers {

    public static Matcher<Object> isAfter(LocalDateTime nowTime) {
        return new IsAfter<>(nowTime);
    }
}
