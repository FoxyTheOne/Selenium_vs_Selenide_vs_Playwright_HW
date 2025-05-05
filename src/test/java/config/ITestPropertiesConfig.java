package config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:${env}.properties",
        "classpath:default.properties"
})
public interface ITestPropertiesConfig extends Config {
    @Config.Key("isRemote")
    Boolean isRemote();
}
