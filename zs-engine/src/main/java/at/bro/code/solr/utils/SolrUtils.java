package at.bro.code.solr.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;

/**
 *
 * @author brosenberger
 *
 */
public final class SolrUtils {

    private SolrUtils() {
        // should not be instantiated
    }

    public static List<SolrDocument> expandResults(QueryResponse queryResponse, String idField) {
        final SolrDocumentList results = queryResponse.getResults();
        final List<SolrDocument> documents = new ArrayList<SolrDocument>();
        final Map<String, SolrDocumentList> expandedResults = queryResponse.getExpandedResults();
        for (int i = 0; i < results.getNumFound(); i++) {
            final SolrDocument solrDocument = results.get(i);
            final SolrDocumentList children = expandedResults.get(solrDocument.get(idField));
            if (children != null) {
                solrDocument.addChildDocuments(children);
            }

            // TODO possibly add grandchildren?

            documents.add(solrDocument);
        }
        return documents;
    }
//{!parent which=\'type_s:parent\' v='+relation_s:daughter'} , "id:m*", "relation_s:d*"
    public static ModifiableSolrParams baseBlockJoinParams(String q, String fq, String expandFQ) {
        return baseBlockJoinParams(q, fq, "_root_", "*:*", expandFQ);
    }

    public static ModifiableSolrParams baseBlockJoinParams(String q, String fq, String expandField, String expandQ,
            String expandFQ) {
        final ModifiableSolrParams params = new ModifiableSolrParams();
        params.add("q", q);
        if (StringUtils.isNotBlank(fq)) {
            params.add("fq", fq);
        }
        params.add("expand", "true");
        params.add("expand.field", expandField);
        params.add("expand.q", "*:*");
        // attention! the default is 5 rows, so be carefull about that! - bty
        // Integer.MAX_VALUE is not allowed as it is about +300
        params.add("expand.rows", "100");
        if (StringUtils.isNotBlank(expandFQ)) {
            params.add("expand.fq", expandFQ);
        } else {
            params.add("expand.fq", "*:*");
        }
        return params;
    }

    public static String prepareParentSelector(String which, String vParam) {
        String v = StringUtils.EMPTY;
        if (!StringUtils.isBlank(vParam)) {
            v = "v='" + vParam + "'";
        }
        return "{!parent which=\'" + which + "\' " + v + "}";
    }
}
