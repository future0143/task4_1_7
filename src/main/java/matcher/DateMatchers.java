package matcher;

import org.hamcrest.Matcher;

import java.time.LocalDateTime;

public class DateMatchers {

    public static Matcher<Object> isToday(LocalDateTime nowTime) {
        return new IsAfter<>(nowTime);
    }
}
