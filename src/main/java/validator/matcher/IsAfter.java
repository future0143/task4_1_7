package validator.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class IsAfter<T> extends BaseMatcher<T> {

    private final LocalDateTime nowTime;

    public IsAfter(LocalDateTime nowTime) {
        this.nowTime = nowTime;
    }

    @Override
    public boolean matches(Object requestTime) {
        Date date = (Date) requestTime;

        LocalDateTime updatedAt = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return updatedAt.isAfter(nowTime);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("today").appendValue(nowTime);
    }
}
