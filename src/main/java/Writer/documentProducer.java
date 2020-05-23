package Writer;


import Assets.AssetCollection;
import Assets.AssetTree;
import Parser.ODRL_vocab;
import Policy.Policy;
import Rule.Rule;
import Rule.Permission;
import Rule.Prohibition;
import com.company.Main;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.Map;

public class documentProducer {
    public static String resPath = "./src/main/java/Parser/output.ttl";

    public static void produceDocument(AssetTree tree, Map<String, AssetCollection> assets) {
        Model m = ModelFactory.createDefaultModel();
        m.setNsPrefix("odrl", ODRL_vocab.NS);
        Resource policy;
        policy = m.createResource("http://ID");
        policy.addProperty(RDF.type, ODRL_vocab.Set);
        Map<String, Policy> allRules = tree.recoverAllPolicy();

        for(Map.Entry<String,Policy> URIRules : allRules.entrySet()){
            Resource target = processTargetNode(assets.get(URIRules.getKey()),  m);
            if(URIRules.getValue() != null){
                for( Rule rule : URIRules.getValue().getRules()){
                    Resource ruleNode = null;
                    if(rule instanceof Permission) {
                        ruleNode = processRuleNode(rule, target, m, true);
                        policy.addProperty(ODRL_vocab.permission, ruleNode);
                    }
                    else {
                        ruleNode = processRuleNode(rule, target, m, false);
                        policy.addProperty(ODRL_vocab.prohibition, ruleNode);
                    }

                }
            }
        }




        System.out.println("--- DEFAULT ---");

        try {
            RDFDataMgr.write(new FileOutputStream(resPath), m, RDFFormat.TURTLE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private static Resource processRuleNode(Rule rule, Resource target, Model m, boolean isPermission) {

        Resource res = m.createResource();
        Resource type = isPermission ? ODRL_vocab.Permission : ODRL_vocab.Prohibition;
        res.addProperty(RDF.type,type);
        Resource action = null;
        try {
            Field vocabField = ODRL_vocab.class.getField(rule.getAction().getName());
            action = (Resource) vocabField.get(new ODRL_vocab());
        } catch (NoSuchFieldException e) {
            action = null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        res.addProperty(ODRL_vocab.action, action);

        res.addProperty(ODRL_vocab.target,target);
        return res;
    }

    private static Resource processTargetNode(AssetCollection assetCollection, Model m) {
        Resource actualTarget = m.getResource(assetCollection.getURI());
        Resource parentTarget = assetCollection.getParent() != null ? m.getResource(assetCollection.getParent().getURI()) : null;
        if(parentTarget != null)
            actualTarget.addProperty(ODRL_vocab.partOf, parentTarget);
        return actualTarget;

    }

}
