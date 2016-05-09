//package at.bro.code.solr;
//
//import java.util.Calendar;
//import java.util.Date;
//
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.springframework.data.solr.core.SolrTemplate;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
//
//public abstract class BaseSolrBlockJoinTest {
//
//    protected SolrTemplate solrTemplate;
//
//    @BeforeClass
//    void setupSuite() {
//        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
//        context.register(SolRApplication.class);
//        context.refresh();
//        solrTemplate = context.getBean(getSolrServerId(), SolrTemplate.class);
//    }
//
//    @AfterClass
//    void tearDown() {
//        // remove all old documents
//        solrTemplate.delete(new SimpleQuery("*:*"));
//    }
//
//    public static Date createDate(final int dayOfMonth) {
//        final int year = 2014;
//        final int month = 12;
//        final Calendar cal = Calendar.getInstance();
//        cal.set(year, month - 1, dayOfMonth);
//        return DateUtils.truncate(cal, Calendar.DAY_OF_MONTH).getTime();
//    }
//
//    protected abstract String getSolrServerId();
//
//}
