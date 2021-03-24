package serie05.model.technical;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import serie05.util.Phrase;

public class PhraseLoader extends ResourceLoader<Phrase> {

    // ATTRIBUTS

    private final File inputFile;

    // CONSTRUCTEURS

    public PhraseLoader(File in) {
        inputFile = in;
    }

    // COMMANDES

    @Override
    public void loadResource() throws Exception {
        BufferedReader br = new BufferedReader(
                new FileReader(inputFile));
        try {
            String line = br.readLine();
            while (line != null) {
                // pour que ça rame...
                delayAction();
                if (!line.equals("")) {
                    fireResourceLoaded(new Phrase(line));
                }
                line = br.readLine();
            }
        } finally {
            br.close();
        }
    }
}
