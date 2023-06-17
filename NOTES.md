Use a Download task to get the tailwindcss binary. Get the right one using

```kotlin
task<Download>("downloadTailwind") {
    println(DefaultNativePlatform.getCurrentArchitecture())
    println(DefaultNativePlatform.getCurrentOperatingSystem())
}
```
