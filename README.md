### THE SIMPLEST OF THE SIMPLE HTTP SERVER ON THE MARKET

*attach to your pom and start discovering*

```
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>com.github.ygaros</groupId>
            <artifactId>mini-server</artifactId>
            <version>latest</version>
        </dependency>
    </dependencies>
```

*fluent interface*

```
HttpServer server = new HttpServerBuilder()
                .withPort(9876)
                .withDefaultResourcesPath()
                .withFileMapping()
                .withRestHandler(HttpMethod.GET, "/rest", (request) -> "rest-responde")
                .withHTMLHandler(HttpMethod.GET, "/index", (request) -> "index.html")
                .withImageHandler(HttpMethod.GET, "/dir", (request) -> "kot.png")
                .withTextFileHandler(HttpMethod.GET, "/txt", (request) -> "plik.txt")
                .build();
```


<sup>
Please keep in mind that this project is still in progress, and You may experience bugs on the way.
</sup>