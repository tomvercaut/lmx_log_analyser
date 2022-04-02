package uzg.rt.lmx.log.model;

import lombok.Data;
@Data
public class Version {
    private String major;
    private String minor;
    private String patch;

    public Version() {
        major = "";
        minor = "";
        patch = "";
    }

    public Version(String major, String minor, String patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public Version(int major, int minor, int patch) {
        this.major = String.valueOf(major);
        this.minor = String.valueOf(minor);
        this.patch = String.valueOf(patch);
    }
}
