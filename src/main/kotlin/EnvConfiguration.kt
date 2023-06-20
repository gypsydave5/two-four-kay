import com.natpryce.konfig.*
import org.http4k.routing.ResourceLoader


class EnvConfiguration : Configuration {
    override val config: com.natpryce.konfig.Configuration =
        ConfigurationProperties.systemProperties() overriding
                EnvironmentVariables() overriding
                ConfigurationProperties.fromResource("defaults.properties")

    override val server_port = config[Key("server.port", intType)]
    override val env: String = config[Key("env", stringType)]

    override val publicResources: ResourceLoader = if (env == "dev") {
        ResourceLoader.Directory("src/main/resources/public")
    } else {
        ResourceLoader.Classpath("/public")
    }
}