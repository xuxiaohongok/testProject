//package at.bro.code.solr.blockjoins.solr;
//
//import javax.annotation.Resource;
//
//import org.apache.solr.client.solrj.SolrServer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.data.solr.core.SolrTemplate;
//import org.springframework.data.solr.server.support.EmbeddedSolrServerFactoryBean;
//
//import at.bro.code.solr.blockjoins.SolRApplication;
//
//@Configuration
//@PropertySource("classpath:application.properties")
//public class EmbeddedSolrConfiguration {
//
//    @Resource
//    private org.springframework.core.env.Environment environment;
//
//    @Bean
//    public SolrServer solrServerRelations() throws Exception {
//        final EmbeddedSolrServerFactoryBean factory = new EmbeddedSolrServerFactoryBean();
//
//        factory.setSolrHome(environment.getRequiredProperty("solr.solr.home.relations"));
//
//        return factory.getObject();
//    }
//
//    @Bean(name = { SolRApplication.RELATIONS_SOLR })
//    public SolrTemplate solrTemplateRelations() throws Exception {
//        return new SolrTemplate(solrServerRelations());
//    }
//
//    @Bean
//    public SolrServer solrServerVehicles() throws Exception {
//        final EmbeddedSolrServerFactoryBean factory = new EmbeddedSolrServerFactoryBean();
//
//        factory.setSolrHome(environment.getRequiredProperty("solr.solr.home.vehicles"));
//
//        return factory.getObject();
//    }
//
//    @Bean(name = { SolRApplication.VEHICLES_SOLR })
//    public SolrTemplate solrTemplateVehicles() throws Exception {
//        return new SolrTemplate(solrServerVehicles());
//    }
//}
