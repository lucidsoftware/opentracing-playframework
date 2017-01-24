# OpenTracing for [Play Framework](https://www.playframework.com/)

[![Build Status](https://travis-ci.org/lucidsoftware/opentracing-playframework.svg?branch=master)](https://travis-ci.org/lucidsoftware/opentracing-playframework)

![Maven Version](https://img.shields.io/maven-central/v/com.lucidchart/opentracing-playframework.svg)

## Example

```scala
import io.opentracing.play.active

class Filters extends DefaultHttpFilters(
  new ActiveTracingFilter(
    ContentTagger,      // tags content headers
    HttpVersionTagger,  // tags HTTP version
    RemoteSpanTagger,   // tags remote address
    StandardSpanTagger, // tags standard OpenTracing tags
    TagsSpanTagger      // tags Play request tags
  )
)
```

The `ActiveTracingFilter` uses `SpanManager` and the global `Tracer`.

## Versions

Use the appropriate version for your version of Play

```scala
// Play 2.3.x-2.4.x
libraryDependencies += "com.lucidchart" % "opentracing-play-active" % "0.0-2.3"

// Play 2.5.x
libraryDependencies += "com.lucidchart" % "opentracing-play-active" % "0.0-2.5"
```


