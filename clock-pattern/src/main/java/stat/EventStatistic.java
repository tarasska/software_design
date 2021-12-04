package stat;

import java.util.Map;

public interface EventStatistic<T> {
    void incEvent(String name);

    T getEventStatisticByName(String name);
    Map<String, T> getAllEventStatistic();

    void printStatistic();
}
