#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-html-jvm:0.6.11")

import kotlinx.html.*; import kotlinx.html.stream.*; import kotlinx.html.attributes.*

val addressee = args.firstOrNull() ?: "World"

print(createHTML().html {
    body {
        h1 { +"Hello, $addressee!" }
    }
})

