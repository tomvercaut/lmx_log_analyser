package uzg.rt.lmx.log.model.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uzg.rt.lmx.log.model.License;
import uzg.rt.lmx.log.model.Version;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RayStationLicense implements License {
    private Version version = new Version();
    private String type = "";
    private String name = "";
    private String hash = "";

    @Override
    public Version getVersion() {
        return version;
    }

    @Override
    public boolean isClinical() {
        return type.toLowerCase().equals("clinical");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getHash() {
        return hash;
    }
}
