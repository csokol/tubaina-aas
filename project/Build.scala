import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "tubaina-aas"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    
    "net.htmlparser.jericho" % "jericho-html" % "3.1",
	"de.java2html" % "java2html" % "5.0",
	//"log4j" % "log4j" % "1.2.12",
	"commons-io" % "commons-io" % "2.4",
	"commons-cli" % "commons-cli" % "1.1",
	"org.apache.httpcomponents" % "httpclient" % "4.1.3",
	"org.freemarker" % "freemarker" % "2.3.19",
	"com.google.code.gson" % "gson" % "2.1",
	"com.lowagie" % "itext" % "2.1.7",
	"com.thoughtworks.xstream" % "xstream" % "1.4.2",
	"org.apache.commons" % "commons-exec" % "1.1",
	"net.lingala.zip4j" % "zip4j" % "1.3.1",
	"org.eclipse.mylyn.github" % "org.eclipse.egit.github.core" % "2.1.3"
            
    
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    resolvers += "maven" at "https://repository.jboss.org/nexus/content/groups/public/",      
    ebeanEnabled := true
  )

}
