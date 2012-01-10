grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsCentral()
        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenCentral()
        //mavenLocal()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        compile('org.hibernate:hibernate-entitymanager:3.6.7.Final') {
            exclude group:'org.hibernate', name:'hibernate-core'            
            exclude group:'commons-logging', name:'commons-logging'            
            exclude group:'commons-collections', name:'commons-collections'
            exclude group:'org.slf4j', name:'slf4j-api'
            exclude group:'xml-apis', name:'xml-apis'            
            exclude group:'dom4j', name:'dom4j'                                    
        }
        compile('org.hibernate:hibernate-core:3.6.7.Final') {
            exclude group:'commons-logging', name:'commons-logging'            
            exclude group:'commons-collections', name:'commons-collections'
            exclude group:'org.slf4j', name:'slf4j-api'
            exclude group:'xml-apis', name:'xml-apis'            
            exclude group:'dom4j', name:'dom4j'                        
        }
        compile('org.hibernate:hibernate-validator:4.1.0.Final') {
            exclude group:'commons-logging', name:'commons-logging'            
            exclude group:'commons-collections', name:'commons-collections'
            exclude group:'org.slf4j', name:'slf4j-api'
        }

        runtime 'javassist:javassist:3.12.0.GA'
        runtime 'antlr:antlr:2.7.6'
        runtime('dom4j:dom4j:1.6.1') {
            exclude group:'xml-apis', name:'xml-apis'
        }
        runtime('org.hibernate:hibernate-ehcache:3.6.7.Final') {
             exclude group:'commons-logging', name:'commons-logging'
             exclude group:'commons-collections', name:'commons-collections'
             exclude group:'org.slf4j', name:'slf4j-api'
             exclude group:'xml-apis', name:'xml-apis'                         
             exclude group:'dom4j', name:'dom4j'                                     
             exclude group:'org.hibernate', name:'hibernate-core'
             exclude group:'net.sf.ehcache', name:'ehcache'
             exclude group:'net.sf.ehcache', name:'ehcache-core'             
        }
    }

    plugins {
        build(":tomcat:$grailsVersion",
              ":release:1.0.0.RC3") {
            export = false
        }
    }
}
