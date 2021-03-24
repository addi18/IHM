package serie05;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import serie05.event.ResourceLoaderEvent;
import serie05.event.ResourceLoaderListener;
import serie05.model.application.FilteringModel;
import serie05.model.filters.Factor;
import serie05.model.filters.Filter;
import serie05.model.filters.Prefix;
import serie05.model.filters.RegExp;
import serie05.model.filters.Suffix;
import serie05.model.technical.PhraseLoader;
import serie05.util.StringTranslator;
import serie05.util.Phrase;

public final class FilteringPhrasesByStringTester
        extends FilteringTester<Phrase, String> {

    // ATTRIBUTS

    private static final File INPUT_FILE =
            getInputResource("data/text/contenu.txt");
    private static final List<Filter<Phrase, String>> FILTERS;
    static {
        FILTERS = new ArrayList<Filter<Phrase, String>>();
        FILTERS.add(new Prefix<Phrase>());
        FILTERS.add(new Factor<Phrase>());
        FILTERS.add(new Suffix<Phrase>());
        FILTERS.add(new RegExp<Phrase>());
    }

    // CONSTRUCTEURS

    public FilteringPhrasesByStringTester() {
        super(new StringTranslator());
    }

    // OUTILS

    @Override
    protected FilteringModel<Phrase, String> createModel() {
        return new FilteringModel<Phrase, String>(
                FILTERS, new PhraseLoader(INPUT_FILE));
    }

    @Override
    protected ResourceLoaderListener<Phrase>
            createResourceLoaderListener() {
        return new ResourceLoaderListener<Phrase>() {
            @Override
            public void resourceLoaded(
                    ResourceLoaderEvent<Phrase> e) {
                getFilterablePane().addElement(e.getResource());
            }
        };
    }

    // POINTS D'ENTREE

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
            new Runnable() {
                @Override
                public void run() {
                    new FilteringPhrasesByStringTester().display();
                }
            }
        );
    }
}
