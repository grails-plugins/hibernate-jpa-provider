class HibernateJpaProviderGrailsPlugin {
    // the plugin version
    def version = "1.0.0.M1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.0 > *"
    // the other plugins this plugin depends on
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    def title = "Hibernate Jpa Provider Plugin" // Headline display name of the plugin
    def author = "Graeme Rocher"
    def authorEmail = "grocher@vmware.com"
    def description = '''\
Integrates Hibernate as a JPA provider
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/hibernate-jpa-provider"

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
    def organization = [ name: "SpringSource", url: "http://www.springsource.com/" ]

    // Any additional developers beyond the author specified above.
    def developers = [ [ name: "Graeme Rocher", email: "grocher@vmware.com" ]]

    // Location of the plugin's issue tracker.
    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPGORMJPA" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/SpringSource/grails-data-mapping/tree/master/grails-plugins" ]
    

    def observe = ['domainClass']
    
    def doWithSpring = {
        def domainClassPackages = application.domainClasses.collect { it.clazz.getPackage().name }.unique()
        if(!domainClassPackages) domainClassPackages = [application.metadata.getApplicationName()]
        
        entityManagerFactory(org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean) {
            packagesToScan = domainClassPackages                
            dataSource = ref("dataSource")
            jpaVendorAdapter = ref("jpaVendorAdapter")
            jpaPropertyMap = ref("hibernateProperties")
        }
        
    	transactionManager(org.springframework.orm.jpa.JpaTransactionManager) {
  	        entityManagerFactory = entityManagerFactory
  	    }        
  	    
  	    addAlias("jpaTransactionManager", "transactionManager")
  	    
        def hibConfig = application.config.hibernate
        def ds = application.config.dataSource
        if (ds.loggingSql || ds.logSql) {
            hibConfig."hibernate.show_sql" = "true"
        }
        if (ds.formatSql) {
            hibConfig."hibernate.format_sql" = "true"
        }

        if (ds.dialect) {
            if (ds.dialect instanceof Class) {
                hibConfig."hibernate.dialect" = ds.dialect.name
            }
            else {
                hibConfig."hibernate.dialect" = ds.dialect.toString()
            }
        }
        else {
            def vendorToDialect = new Properties()
            def hibernateDialects = application.classLoader.getResource("hibernate-dialects.properties")
            if (hibernateDialects) {
                def p = new Properties()
                p.load(hibernateDialects.openStream())
                for (entry in p) {
                    vendorToDialect[entry.value] = "org.hibernate.dialect.${entry.key}".toString()
                }
            }            
            "dialectDetector"(org.codehaus.groovy.grails.orm.hibernate.support.HibernateDialectDetectorFactoryBean) {
                dataSource = ref("dataSource")
                vendorNameDialectMappings = vendorToDialect
            }
            hibConfig."hibernate.dialect" = ref("dialectDetector")
        }

        hibConfig."hibernate.hbm2ddl.auto" = ds.dbCreate ?: ''        
        "hibernateProperties"(org.springframework.beans.factory.config.PropertiesFactoryBean) { bean ->
              bean.scope = "prototype"
              properties = hibConfig
        }        
        jpaVendorAdapter(org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter)
    }




    def onChange = { event ->
       def beans = beans {
           def domainClassPackages = application.domainClasses.collect { it.clazz.getPackage().name }.unique()
           if(!domainClassPackages) domainClassPackages = [application.metadata.getApplicationName()]

           entityManagerFactory(org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean) {
               packagesToScan = domainClassPackages                
               dataSource = ref("dataSource")
               jpaVendorAdapter = ref("jpaVendorAdapter")
               jpaPropertyMap = ref("hibernateProperties")
           }       
       	   transactionManager(org.springframework.orm.jpa.JpaTransactionManager) {
     	        entityManagerFactory = entityManagerFactory
     	   }        

     	   addAlias("jpaTransactionManager", "transactionManager")
               
       }
       
       beans.registerBeans(event.ctx)
    }

}
