package uzg.rt.lmx.log.model;

public interface License {
    Version getVersion();

    boolean isClinical();

    String getName();

    String getHash();
}
