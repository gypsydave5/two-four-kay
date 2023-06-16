import com.natpryce.konfig.*
import org.http4k.routing.ResourceLoader


class Configuration {
    val config =
        ConfigurationProperties.systemProperties() overriding
                EnvironmentVariables() overriding
                ConfigurationProperties.fromResource("defaults.properties")

    val server_port = config[Key("server.port", intType)]
    val env = config[Key("env", stringType)]

    val publicResources = if (env == "dev") {
        ResourceLoader.Directory("src/main/resources/public")
    } else {
        ResourceLoader.Classpath("/public")
    }
}