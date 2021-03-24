package serie05.util;

import serie05.model.filters.Filterable;

public class Phrase implements Filterable<String> {

    private final String data;

    public Phrase(String s) {
        data = s;
    }

    @Override
    public String filterableValue() {
        return data;
    }

    @Override
    public String toString() {
        return data;
    }
}
