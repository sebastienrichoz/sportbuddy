name := "counter"

scalaVersion in ThisBuild := "2.11.8"

val shared =
  crossProject
    .crossType(CrossType.Pure)
    .settings(
      libraryDependencies ++= Seq(
        "com.lihaoyi" %%% "autowire" % "0.2.6",
        "io.suzaku" %%% "boopickle" % "1.2.6"
      )
    )

val sharedJS = shared.js
val sharedJVM = shared.jvm

val client =
  project
    .dependsOn(sharedJS)
    .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
    .settings(
      libraryDependencies ++= Seq(
        "org.scala-js" %%% "scalajs-dom" % "0.9.2",
        "in.nvilla" %%% "monadic-html" % "0.3.2"
      ),
      scalaJSUseMainModuleInitializer := true
    )

val server =
  project
    .dependsOn(sharedJVM)
    .enablePlugins(PlayScala)
    .settings(
      scalaJSProjects := Seq(client),
      pipelineStages in Assets := Seq(scalaJSPipeline),
      libraryDependencies ++= Seq(
        "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
      )
    )

val standaloneClient =
  project
    .enablePlugins(ScalaJSPlugin)
    .settings(
      scalaJSUseMainModuleInitializer := true,
      libraryDependencies ++= Seq(
        "org.scala-js" %%% "scalajs-dom" % "0.9.2",
        "in.nvilla" %%% "monadic-html" % "0.3.2"
      )
    )
