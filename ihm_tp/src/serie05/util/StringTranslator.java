package serie05.util;


public final class StringTranslator implements Translator<String> {

    @Override
    public String translateToValue(String text) {
        return text;
    }

    @Override
    public String translateToText(String value) {
        return value;
    }
}
