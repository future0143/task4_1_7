package matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IsAfter<T> extends BaseMatcher<T> {

    private final LocalDateTime nowTime;

    public IsAfter(LocalDateTime nowTime) {
        this.nowTime = nowTime;
    }

    @Override
    public boolean matches(Object requestTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime updatedAt = LocalDateTime.parse((CharSequence) requestTime, formatter);
        return updatedAt.isAfter(nowTime);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("today").appendValue(nowTime);
    }
}
