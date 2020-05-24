package Writer;


import Actions.Action;
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
import java.util.HashMap;
import java.util.Map;

public class documentProducer {
    public static String resPath = "./src/main/java/Parser/output.ttl";

    public static void produceDocument(AssetTree tree, Map<String, AssetCollection> assets, String path) {
        if(path == null){
            path = resPath;
        }

        Model m = ModelFactory.createDefaultModel();
        m.setNsPrefix("odrl", ODRL_vocab.NS);
        Resource policy;
        policy = m.createResource("http://ID");
        policy.addProperty(RDF.type, ODRL_vocab.Set);
        Map<String, Policy> allRules = tree.recoverAllPolicy();
        RDFList permList = null;
        RDFList prohibList = null;
        for(Map.Entry<String,Policy> URIRules : allRules.entrySet()){
            Resource target = processTargetNode(assets.get(URIRules.getKey()),  m);

            Map<Action,String > useTransferMap = new HashMap<Action,String>();

            if(URIRules.getValue() != null){

                useTransferMap.putAll(URIRules.getValue().getUseTree().getAllStates());
                useTransferMap.putAll(URIRules.getValue().getTransferTree().getAllStates());

                for( Map.Entry<Action,String> rule : useTransferMap.entrySet()){
                    Resource ruleNode = null;
                    if(rule.getValue().equals("Permitted")) {
                        ruleNode = processRuleNode(rule.getKey(), target, m, true);
                        //policy.addProperty(ODRL_vocab.permission, ruleNode);
                        if(permList==null)
                            permList = m.createList(ruleNode);
                        else
                            permList.add(ruleNode);
                    }
                    if(rule.getValue().equals("Prohibited")) {
                        ruleNode = processRuleNode(rule.getKey(), target, m, false);
                        //policy.addProperty(ODRL_vocab.prohibition, ruleNode);
                        if(prohibList == null)
                            prohibList = m.createList(ruleNode);
                        else
                            prohibList.add(ruleNode);
                    }

                }
                if(permList.size()>0)
                    policy.addProperty(ODRL_vocab.permission,permList);
                if(prohibList.size()>0)
                    policy.addProperty(ODRL_vocab.prohibition,prohibList);
            }
        }






        try {
            RDFDataMgr.write(new FileOutputStream(path), m, RDFFormat.TURTLE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private static Resource processRuleNode(Action rule, Resource target, Model m, boolean isPermission) {

        Resource res = m.createResource();
        Resource type = isPermission ? ODRL_vocab.Permission : ODRL_vocab.Prohibition;
        res.addProperty(RDF.type,type);
        Resource action = null;
        try {
            Field vocabField = ODRL_vocab.class.getField(rule.getName());
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
