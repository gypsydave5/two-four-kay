import com.natpryce.konfig.*


class EnvConfiguration : Configuration {
    private val config: com.natpryce.konfig.Configuration =
        ConfigurationProperties.systemProperties() overriding
                EnvironmentVariables() overriding
                ConfigurationProperties.fromResource("defaults.properties")

    override val server_port = config[Key("server.port", intType)]
    override val env: String = config[Key("env", stringType)]
}