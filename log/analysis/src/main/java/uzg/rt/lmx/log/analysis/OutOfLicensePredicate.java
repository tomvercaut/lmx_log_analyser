package uzg.rt.lmx.log.analysis;

import uzg.rt.lmx.log.model.Entry;
import uzg.rt.lmx.log.model.LogLevel;

import java.util.Objects;
import java.util.function.Predicate;

public class OutOfLicensePredicate implements Predicate<Entry> {
    @Override
    public boolean test(Entry entry) {
        Objects.requireNonNull(entry);
        return entry.getLevel() == LogLevel.FAIL && entry.getErrorMessage().contains("No more licenses available");
    }
}
