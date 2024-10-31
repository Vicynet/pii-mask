package dev.vicynet.piimask;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class PiiMaskModule extends SimpleModule {
    public PiiMaskModule() {
        super("PiiMaskModule", Version.unknownVersion());
        addSerializer(String.class, new MaskDataSerializer());
    }
}
