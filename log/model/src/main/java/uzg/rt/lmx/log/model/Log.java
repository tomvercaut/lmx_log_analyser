package uzg.rt.lmx.log.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Log {
    private final List<Entry> entries;

    public Log() {
        entries = new ArrayList<>();
    }

    public Log(@NotNull List<Entry> entries) {
        this.entries = entries;
    }

    public void add(@NotNull Entry entry) {
        entries.add(entry);
    }

    public void clear() {
        entries.clear();
    }

    public Stream<Entry> stream() {
        return entries.stream();
    }

    public int size() {
        return entries.size();
    }

    public Entry get(int i) {
        return entries.get(i);
    }
}
