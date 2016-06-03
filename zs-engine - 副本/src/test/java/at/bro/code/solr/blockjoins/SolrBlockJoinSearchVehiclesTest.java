//package at.bro.code.solr;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//
//import org.apache.solr.client.solrj.SolrServerException;
//import org.apache.solr.common.SolrDocument;
//import org.apache.solr.common.SolrInputDocument;
//import org.apache.solr.common.params.ModifiableSolrParams;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.testng.Assert;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//import at.bro.code.solr.api.MilagePackageReceived;
//import at.bro.code.solr.api.Price;
//import at.bro.code.solr.api.Rating;
//import at.bro.code.solr.api.Vehicle;
//import at.bro.code.solr.utils.SolrUtils;
//
//@Test
//public class SolrBlockJoinSearchVehiclesTest extends BaseSolrBlockJoinTest {
//
//    private final static Logger LOG = LoggerFactory.getLogger(SolrBlockJoinSearchVehiclesTest.class);
//    private final List<Vehicle> availableVehicles = new ArrayList<>();
//
//    @Override
//    protected String getSolrServerId() {
//        return SolRApplication.VEHICLES_SOLR;
//    }
//
//    @BeforeClass
//    void setup() {
//        // all dates are 2014-12-XX 00:00:00
//        final Vehicle v1 = new Vehicle(1L, "audi", "a4", Arrays.asList(new Price(createDate(1), createDate(10), 1.,
//                "linz"), new Price(createDate(11), createDate(15), 2., "linz"), new Price(createDate(9),
//                        createDate(12), 1., "salzburg")), Arrays.asList(new Rating("linz", "A+"), new Rating("salzburg", "A")),
//                Arrays.asList(new MilagePackageReceived("linz", 100, 1000, 5000)));
//        saveDocument(v1);
//        final Vehicle v2 = new Vehicle(2L, "audi", "a3", Arrays.asList(new Price(createDate(3), createDate(10), 1.,
//                "linz"), new Price(createDate(11), createDate(15), 10., "linz"), new Price(createDate(9),
//                        createDate(12), 12., "salzburg")), Arrays.asList(new Rating("linz", "A")),
//                Arrays.asList(new MilagePackageReceived("salzburg", 10000)));
//        saveDocument(v2);
//        final Vehicle v3 = new Vehicle(3L, "vw", "sharan", Arrays.asList(new Price(createDate(3), createDate(10), 1.,
//                "linz"), new Price(createDate(11), createDate(15), 10., "linz"), new Price(createDate(9),
//                        createDate(12), 1., "salzburg")), Arrays.asList(new Rating("linz", "B"), new Rating("salzburg", "A-")),
//                Arrays.asList(new MilagePackageReceived("linz", 100, 1200, 5000), new MilagePackageReceived("salzburg",
//                        10000)));
//        saveDocument(v3);
//        final Vehicle v4 = new Vehicle(4L, "vw", "golf", Arrays.asList(new Price(createDate(3), createDate(10), 1.,
//                "wels")), Collections.<Rating> emptyList(), Collections.<MilagePackageReceived> emptyList());
//        saveDocument(v4);
//        availableVehicles.add(v1);
//        availableVehicles.add(v2);
//        availableVehicles.add(v3);
//        availableVehicles.add(v4);
//    }
//
//    @Test
//    void testFindAllVehiclesInLinz() throws SolrServerException {
//        final List<Vehicle> vehicles = querySolr("+location_s:linz", "", "location_s:linz");
//
//        Assert.assertEquals(vehicles.size(), 3);
//    }
//
//    @Test
//    void testFindAllCurrentPricesForVehicles() throws SolrServerException {
//        final List<Vehicle> vehicles = querySolr("", "", prepareCurrentDateValidity("2014-12-4T12:00:00.999Z"));
//
//        Assert.assertEquals(vehicles.size(), 4);
//        for (final Vehicle v : vehicles) {
//            Assert.assertEquals(v.getPrices().size(), 1);
//            // no other child documents are fetched
//            Assert.assertEquals(v.getRatings().isEmpty(), true);
//        }
//    }
//
//    @Test
//    void testFindMultipleChildrenWithoutRestrictions() throws SolrServerException {
//        final List<Vehicle> vehicles = querySolr("", "", "");
//
//        Assert.assertEquals(vehicles, availableVehicles);
//    }
//
//    @Test
//    void testFindAllCurrentPricesAndLoadAllRatings() throws SolrServerException {
//        final List<Vehicle> vehicles = querySolr("", "", "(type_s:" + PRICE_TYPE + " AND "
//                + prepareCurrentDateValidity("2014-12-4T12:00:00.999Z") + ")OR(type_s:" + RATING_TYPE + ")");
//
//        Assert.assertEquals(vehicles.size(), 4);
//        boolean ratingsFetched = false;
//        for (final Vehicle v : vehicles) {
//            Assert.assertEquals(v.getPrices().size(), 1);
//            ratingsFetched = ratingsFetched || !v.getRatings().isEmpty();
//        }
//        Assert.assertEquals(ratingsFetched, true, "there should be at least one vehicle with ratings");
//    }
//
//    @Test
//    void testFindAllCurrentPricesAndLoadAllRatingsAndMPRForSpecificLocation() throws SolrServerException {
//        final List<Vehicle> vehicles = querySolr("location_s:linz", "", "((type_s:" + PRICE_TYPE + " AND "
//                + prepareCurrentDateValidity("2014-12-4T12:00:00.999Z") + ")OR(type_s:" + RATING_TYPE + ")OR(type_s:"
//                + MPR_TYPE + "))AND(location_s:linz)");
//
//        Assert.assertEquals(vehicles.size(), 3);
//        boolean ratingsFetched = false;
//        boolean mprFetched = false;
//        for (final Vehicle v : vehicles) {
//            Assert.assertEquals(v.getPrices().size(), 1);
//            ratingsFetched = ratingsFetched || (!v.getRatings().isEmpty() && v.getRatings().size() == 1);
//            mprFetched = mprFetched || (!v.getMilagePackages().isEmpty() && v.getMilagePackages().size() == 1);
//        }
//        Assert.assertEquals(ratingsFetched, true, "there should be at least one vehicle with ratings");
//        Assert.assertEquals(mprFetched, true, "there should be at least one vehicle with mileage package received");
//    }
//
//    @Test
//    void testFindAllVehicleWithMilagePackageRange() throws SolrServerException {
//        final List<Vehicle> vehicles = querySolr("(type_s:" + MPR_TYPE + ")AND(mpr_i_m:[0 TO *])", "", "((type_s:"
//                + PRICE_TYPE + ")OR(type_s:" + RATING_TYPE + ")OR((type_s:" + MPR_TYPE + ")AND(mpr_i_m:[0 TO *])))");
//
//        Assert.assertEquals(vehicles.size(), 3);
//    }
//
//    /* *******
//     * private helper methods
//     */
//
//    private String prepareCurrentDateValidity(String date) {
//        final String dateValidityFormat = "validFrom_date:[* TO %1$s]&validTo_date:[%1$s TO *]";
//        return String.format(dateValidityFormat, date);
//    }
//
//    private List<Vehicle> querySolr(String parentChildRestriction, String parentFilterRestriction,
//            String childRestriction) throws SolrServerException {
//        final ModifiableSolrParams params = baseParams(parentChildRestriction, parentFilterRestriction,
//                childRestriction);
//
//        final List<Vehicle> vehicles = new ArrayList<>();
//        final List<SolrDocument> results = SolrUtils.expandResults(solrTemplate.getSolrServer().query(params), "id");
//        for (final SolrDocument doc : results) {
//            vehicles.add(loadVehicle(doc));
//        }
//        return vehicles;
//    }
//
//    private static ModifiableSolrParams baseParams(String parentChildRestriction, String parentFilterRestriction,
//            String childRestriction) {
//        return SolrUtils.baseBlockJoinParams(
//                SolrUtils.prepareParentSelector("type_s:" + VEHICLE_TYPE, parentChildRestriction),
//                parentFilterRestriction, childRestriction);
//    }
//
//    private static final String VEHICLE_TYPE = "vehicle";
//    private static final String PRICE_TYPE = "price";
//    private static final String RATING_TYPE = "rating";
//    private static final String MPR_TYPE = "milage";
//
//    private void saveDocument(Vehicle vehicle) {
//        final SolrInputDocument vDoc = new SolrInputDocument();
//        vDoc.addField("id", vehicle.getId());
//        vDoc.addField("model_s", vehicle.getModel());
//        vDoc.addField("brand_s", vehicle.getBrand());
//        vDoc.addField("type_s", VEHICLE_TYPE);
//
//        for (final Price p : vehicle.getPrices()) {
//            final SolrInputDocument child = new SolrInputDocument();
//            child.addField("validFrom_date", p.getValidFrom());
//            child.addField("validTo_date", p.getValidTo());
//            child.addField("location_s", p.getLocation());
//            child.addField("value_d", p.getValue().doubleValue());
//            child.addField("type_s", PRICE_TYPE);
//
//            vDoc.addChildDocument(child);
//        }
//        for (final Rating r : vehicle.getRatings()) {
//            final SolrInputDocument child = new SolrInputDocument();
//            child.addField("location_s", r.getLocation());
//            child.addField("rating_s", r.getRating());
//            child.addField("type_s", RATING_TYPE);
//
//            vDoc.addChildDocument(child);
//        }
//        for (final MilagePackageReceived mpr : vehicle.getMilagePackages()) {
//            final SolrInputDocument child = new SolrInputDocument();
//            child.addField("location_s", mpr.getLocation());
//            child.addField("type_s", MPR_TYPE);
//            for (int i = 0; i < mpr.getMilages().length; i++) {
//                child.addField("mpr_i_m", mpr.getMilages()[i]);
//            }
//
//            vDoc.addChildDocument(child);
//        }
//
//        solrTemplate.saveDocument(vDoc);
//        solrTemplate.commit();
//    }
//
//    private Vehicle loadVehicle(SolrDocument vDoc) {
//        final Long id = Long.parseLong((String) vDoc.get("id"));
//        final String model = (String) vDoc.get("model_s");
//        final String brand = (String) vDoc.get("brand_s");
//
//        final List<Price> prices = loadPrices(vDoc.getChildDocuments());
//        final List<Rating> ratings = loadRatings(vDoc.getChildDocuments());
//        final List<MilagePackageReceived> mileages = loadMilagePackageReceiveds(vDoc.getChildDocuments());
//
//        return new Vehicle(id, brand, model, prices, ratings, mileages);
//    }
//
//    private List<Rating> loadRatings(List<SolrDocument> docs) {
//        final List<Rating> ratings = new ArrayList<>();
//        for (final SolrDocument d : docs) {
//            if (RATING_TYPE.equals(d.get("type_s"))) {
//                ratings.add(new Rating((String) d.get("location_s"), (String) d.get("rating_s")));
//            }
//        }
//        return ratings;
//    }
//
//    private List<MilagePackageReceived> loadMilagePackageReceiveds(List<SolrDocument> docs) {
//        final List<MilagePackageReceived> mpr = new ArrayList<>();
//        for (final SolrDocument d : docs) {
//            if (MPR_TYPE.equals(d.get("type_s"))) {
//                mpr.add(new MilagePackageReceived((String) d.get("location_s"), d.getFieldValues("mpr_i_m").toArray(
//                        new Integer[0])));
//            }
//        }
//        return mpr;
//    }
//
//    private List<Price> loadPrices(List<SolrDocument> docs) {
//        final List<Price> prices = new ArrayList<>();
//        if (docs == null) {
//            return prices;
//        }
//        for (final SolrDocument d : docs) {
//            if (PRICE_TYPE.equals(d.get("type_s"))) {
//
//                prices.add(new Price((Date) d.get("validFrom_date"), (Date) d.get("validTo_date"), (Double) d
//                        .get("value_d"), (String) d.get("location_s")));
//            }
//        }
//        return prices;
//    }
//
//}
