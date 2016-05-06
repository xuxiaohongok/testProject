package at.bro.code.solr.blockjoins;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Base spring configuration class
 *
 * @author brosenberger
 *
 */
@ComponentScan
@Configuration
public class SolRApplication {

    public static final String RELATIONS_SOLR = "at.bro.code.solr.relations";
    public static final String VEHICLES_SOLR = "at.bro.code.solr.vehicles";
}
