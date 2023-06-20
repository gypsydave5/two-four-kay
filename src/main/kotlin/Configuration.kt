import org.http4k.routing.ResourceLoader

interface Configuration {
    val server_port: Int
    val env: String
    val config: com.natpryce.konfig.Configuration
    val publicResources: ResourceLoader
}