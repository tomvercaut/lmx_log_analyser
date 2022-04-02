package uzg.rt.lmx.apps.log_analyser;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data

public class AppOptions {
    private String inputPath = "";
    private String outputFilename = "";
    private List<Analysis> analyses = new ArrayList<>();

    public enum Analysis {
        OutOfLicense
    }
}
