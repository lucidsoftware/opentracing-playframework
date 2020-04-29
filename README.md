# OpenTracing for [Play Framework](https://www.playframework.com/)

[![Build Status](https://travis-ci.com/lucidsoftware/opentracing-playframework.svg?branch=master)](https://travis-ci.com/lucidsoftware/opentracing-playframework)
![Maven Version](https://img.shields.io/maven-central/v/com.lucidchart/opentracing-play_2.11.svg)

## Usage

### Add dependency

This project is cross-versioned against Play. The Play version appears as a suffix.

```scala
// Play 2.3.x-2.4.x
libraryDependencies += "com.lucidchart" % "opentracing-play-active" % "<version>-2.3"
```

```scala
// Play 2.5.x
libraryDependencies += "com.lucidchart" % "opentracing-play-active" % "<version>-2.5"
```

### Configure Akka

This project [`ThreadContextSpan`](https://github.com/lucidsoftware/opentracing-thread-context) and
[`GlobalTracer`](https://github.com/opentracing-contrib/java-globaltracer).

To propagate the thread-local span, add [akka-thread-context](https://github.com/lucidsoftware/akka-thread-context) as
a dependency and configure Akka to use `DispatchConfigurator`.

```hocon
akka.actor.default-dispatcher.type = com.lucidchart.akka.threadcontext.DefaultDispatcherConfigurator
```

### Add filters

Add `TracingFilter`, specifying the `SpanTagger`s that you want.

```scala
import io.opentracing.play.active

class Filters extends DefaultHttpFilters(
  new DefaultTracingFilter(
    new ContentTagger,               // content headers
    new HttpVersionTagger,           // HTTP version
    new StandardSpanTagger,          // standard OpenTracing tags
    new TagsSpanTagger(_ => true)    // Play request tags
  )
)
```
