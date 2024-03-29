package test.java;

import Assets.Asset;
import Assets.AssetCollection;
import Assets.AssetTree;
import Parser.policyReader;
import Policy.Policy;
import Rule.Rule;
import Writer.documentProducer;
import mergingProcedure.merger;
import org.apache.jena.atlas.lib.Pair;
import org.junit.jupiter.api.Test;
import Policy.Set;
import java.io.File;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;


public class runScenario {


    @Test void intersectChildWithNoPropagation(){
        String examplePath = "./src/test/java/doc1.jsonld";
        Map<AssetCollection, List<Rule>> mappa = policyReader.readPolicyRules(examplePath);
        Asset every = new Asset("EveryAsset");
        Map<String,Asset> assets = policyReader.readAssets(examplePath);

        for(Map.Entry<String,Asset> entry : assets.entrySet()) {

            if (entry.getValue().getParents().size() == 0)
                entry.getValue().addParent(every);
        }

        assets.put("EveryAsset", every);

        AssetTree tree = new AssetTree(every);

        for(Map.Entry<AssetCollection,List<Rule>> entry : mappa.entrySet()){
            if(entry.getValue()!=null && entry.getValue().size() >0 ){

                Policy actPolicy = new Set(entry.getValue(),assets.get(entry.getKey().getURI()));
                tree.setPolicy(actPolicy,false);
            }
        }


        String secondPath = "./src/test/java/doc3.jsonld";
        Map<AssetCollection, List<Rule>>  mappaSecond = policyReader.readPolicyRules(secondPath);
        Map<String,Asset>assetsSecond = policyReader.readAssets(secondPath);
        Asset everySecond = new Asset("EveryAsset");

        for(Map.Entry<String,Asset> entry : assetsSecond.entrySet()) {
            if (entry.getValue().getParents().size() == 0)
                entry.getValue().addParent(everySecond);
        }

        assetsSecond.put("EveryAsset", everySecond);

        AssetTree treeSecond = new AssetTree(everySecond);

        for(Map.Entry<AssetCollection,List<Rule>> entry : mappaSecond.entrySet()){
            if(entry.getValue()!=null && entry.getValue().size() >0 ){

                Policy actPolicy = new Set(entry.getValue(),assetsSecond.get(entry.getKey().getURI()));

                treeSecond.setPolicy(actPolicy,false);
            }
        }

        Pair<AssetTree, Map<String, AssetCollection>> mergingResult = merger.intersectionOnCommon(assets,assetsSecond,tree,treeSecond);

        AssetTree resTree = mergingResult.getLeft();
        Map<String, AssetCollection> hier = mergingResult.getRight();
        documentProducer.produceDocument(resTree,hier,"./src/test/java/outputs/intersectOnCommon1-3.ttl");

    }

    @Test
    public void intersectChildWithCollaborative(){


        String examplePath = "./src/test/java/doc1.jsonld";
        Map<AssetCollection, List<Rule>> mappa = policyReader.readPolicyRules(examplePath);
        Asset every = new Asset("EveryAsset");
        Map<String,Asset> assets = policyReader.readAssets(examplePath);

        for(Map.Entry<String,Asset> entry : assets.entrySet()) {

            if (entry.getValue().getParents().size() == 0)
                entry.getValue().addParent(every);
        }

        assets.put("EveryAsset", every);

        AssetTree tree = new AssetTree(every);

        for(Map.Entry<AssetCollection,List<Rule>> entry : mappa.entrySet()){
            if(entry.getValue()!=null && entry.getValue().size() >0 ){

                Policy actPolicy = new Set(entry.getValue(),assets.get(entry.getKey().getURI()));
                tree.setPolicy(actPolicy,false);
            }
        }

        String secondPath = "./src/test/java/doc3.jsonld";
        Map<AssetCollection, List<Rule>>  mappaSecond = policyReader.readPolicyRules(secondPath);
        Map<String,Asset>assetsSecond = policyReader.readAssets(secondPath);
        Asset everySecond = new Asset("EveryAsset");

        for(Map.Entry<String,Asset> entry : assetsSecond.entrySet()) {
            if (entry.getValue().getParents().size() == 0)
                entry.getValue().addParent(everySecond);
        }

        assetsSecond.put("EveryAsset", everySecond);

        AssetTree treeSecond = new AssetTree(everySecond);

        for(Map.Entry<AssetCollection,List<Rule>> entry : mappaSecond.entrySet()){
            if(entry.getValue()!=null && entry.getValue().size() >0 ){

                Policy actPolicy = new Set(entry.getValue(),assetsSecond.get(entry.getKey().getURI()));

                treeSecond.setPolicy(actPolicy,false);
            }
        }

        Pair<AssetTree, Map<String, AssetCollection>> mergingResult = merger.intersection(assets,assetsSecond,tree,treeSecond);

        AssetTree resTree = mergingResult.getLeft();
        Map<String, AssetCollection> hier = mergingResult.getRight();
        documentProducer.produceDocument(resTree,hier,"./src/test/java/outputs/intersect1-3.ttl");

    }

    @Test
    public void  intersectCollaborativeWithNewNodes(){


        String examplePath = "./src/test/java/doc1.jsonld";
        Map<AssetCollection, List<Rule>> mappa = policyReader.readPolicyRules(examplePath);
        Asset every = new Asset("EveryAsset");
        Map<String,Asset> assets = policyReader.readAssets(examplePath);

        for(Map.Entry<String,Asset> entry : assets.entrySet()) {

            if (entry.getValue().getParents().size() == 0)
                entry.getValue().addParent(every);
        }

        assets.put("EveryAsset", every);

        AssetTree tree = new AssetTree(every);

        for(Map.Entry<AssetCollection,List<Rule>> entry : mappa.entrySet()){
            if(entry.getValue()!=null && entry.getValue().size() >0 ){

                Policy actPolicy = new Set(entry.getValue(),assets.get(entry.getKey().getURI()));
                tree.setPolicy(actPolicy,false);
            }
        }


        String secondPath = "./src/test/java/doc2.jsonld";
        Map<AssetCollection, List<Rule>>  mappaSecond = policyReader.readPolicyRules(secondPath);
        Map<String,Asset>assetsSecond = policyReader.readAssets(secondPath);
        Asset everySecond = new Asset("EveryAsset");

        for(Map.Entry<String,Asset> entry : assetsSecond.entrySet()) {
            if (entry.getValue().getParents().size() == 0)
                entry.getValue().addParent(everySecond);
        }

        assetsSecond.put("EveryAsset", everySecond);

        AssetTree treeSecond = new AssetTree(everySecond);

        for(Map.Entry<AssetCollection,List<Rule>> entry : mappaSecond.entrySet()){
            if(entry.getValue()!=null && entry.getValue().size() >0 ){

                Policy actPolicy = new Set(entry.getValue(),assetsSecond.get(entry.getKey().getURI()));

                treeSecond.setPolicy(actPolicy,false);
            }
        }


        Pair<AssetTree, Map<String, AssetCollection>> mergingResult = merger.intersection(assets,assetsSecond,tree,treeSecond);

        AssetTree resTree = mergingResult.getLeft();
        Map<String, AssetCollection> hier = mergingResult.getRight();
        documentProducer.produceDocument(resTree,hier,"./src/test/java/outputs/intersect1-2.ttl");
    }
    @Test
    public void  unionCollaborativeWithNewNodes(){


        String examplePath = "./src/test/java/doc1.jsonld";
        Map<AssetCollection, List<Rule>> mappa = policyReader.readPolicyRules(examplePath);
        Asset every = new Asset("EveryAsset");
        Map<String,Asset> assets = policyReader.readAssets(examplePath);

        for(Map.Entry<String,Asset> entry : assets.entrySet()) {

            if (entry.getValue().getParents().size() == 0)
                entry.getValue().addParent(every);
        }

        assets.put("EveryAsset", every);

        AssetTree tree = new AssetTree(every);

        for(Map.Entry<AssetCollection,List<Rule>> entry : mappa.entrySet()){
            if(entry.getValue()!=null && entry.getValue().size() >0 ){

                Policy actPolicy = new Set(entry.getValue(),assets.get(entry.getKey().getURI()));
                tree.setPolicy(actPolicy,false);
            }
        }


        String secondPath = "./src/test/java/doc2.jsonld";
        Map<AssetCollection, List<Rule>>  mappaSecond = policyReader.readPolicyRules(secondPath);
        Map<String,Asset>assetsSecond = policyReader.readAssets(secondPath);
        Asset everySecond = new Asset("EveryAsset");

        for(Map.Entry<String,Asset> entry : assetsSecond.entrySet()) {
            if (entry.getValue().getParents().size() == 0)
                entry.getValue().addParent(everySecond);
        }

        assetsSecond.put("EveryAsset", everySecond);

        AssetTree treeSecond = new AssetTree(everySecond);

        for(Map.Entry<AssetCollection,List<Rule>> entry : mappaSecond.entrySet()){
            if(entry.getValue()!=null && entry.getValue().size() >0 ){

                Policy actPolicy = new Set(entry.getValue(),assetsSecond.get(entry.getKey().getURI()));

                treeSecond.setPolicy(actPolicy,false);
            }
        }


        Pair<AssetTree, Map<String, AssetCollection>> mergingResult = merger.union(assets,assetsSecond,tree,treeSecond);

        AssetTree resTree = mergingResult.getLeft();
        Map<String, AssetCollection> hier = mergingResult.getRight();
        documentProducer.produceDocument(resTree,hier,"./src/test/java/outputs/union1-2.ttl");
    }

    @Test
    public void  simpleTest(){
        String examplePath = "./src/test/java/single1.jsonld";
        Map<AssetCollection, List<Rule>> mappa = policyReader.readPolicyRules(examplePath);
        Asset every = new Asset("EveryAsset");
        Map<String,Asset> assets = policyReader.readAssets(examplePath);

        for(Map.Entry<String,Asset> entry : assets.entrySet()) {

            if (entry.getValue().getParents().size() == 0)
                entry.getValue().addParent(every);
        }

        assets.put("EveryAsset", every);

        AssetTree tree = new AssetTree(every);

        for(Map.Entry<AssetCollection,List<Rule>> entry : mappa.entrySet()){
            if(entry.getValue()!=null && entry.getValue().size() >0 ){

                Policy actPolicy = new Set(entry.getValue(),assets.get(entry.getKey().getURI()));
                tree.setPolicy(actPolicy,false);
            }
        }


        String secondPath = "./src/test/java/single2.jsonld";
        Map<AssetCollection, List<Rule>>  mappaSecond = policyReader.readPolicyRules(secondPath);
        Map<String,Asset>assetsSecond = policyReader.readAssets(secondPath);
        Asset everySecond = new Asset("EveryAsset");

        for(Map.Entry<String,Asset> entry : assetsSecond.entrySet()) {
            if (entry.getValue().getParents().size() == 0)
                entry.getValue().addParent(everySecond);
        }

        assetsSecond.put("EveryAsset", everySecond);

        AssetTree treeSecond = new AssetTree(everySecond);

        for(Map.Entry<AssetCollection,List<Rule>> entry : mappaSecond.entrySet()){
            if(entry.getValue()!=null && entry.getValue().size() >0 ){

                Policy actPolicy = new Set(entry.getValue(),assetsSecond.get(entry.getKey().getURI()));

                treeSecond.setPolicy(actPolicy,false);
            }
        }


        Pair<AssetTree, Map<String, AssetCollection>> mergingResult = merger.union(assets,assetsSecond,tree,treeSecond);

        AssetTree resTree = mergingResult.getLeft();
        Map<String, AssetCollection> hier = mergingResult.getRight();
        documentProducer.produceDocument(resTree,hier,"./src/test/java/outputs/single.ttl");
    }

    @Test
    public void  complexTest(){
        String examplePath = "./src/test/java/complex1.jsonld";
        Map<AssetCollection, List<Rule>> mappa = policyReader.readPolicyRules(examplePath);
        Asset every = new Asset("EveryAsset");
        Map<String,Asset> assets = policyReader.readAssets(examplePath);

        for(Map.Entry<String,Asset> entry : assets.entrySet()) {

            if (entry.getValue().getParents().size() == 0)
                entry.getValue().addParent(every);
        }

        assets.put("EveryAsset", every);

        AssetTree tree = new AssetTree(every);

        for(Map.Entry<AssetCollection,List<Rule>> entry : mappa.entrySet()){
            if(entry.getValue()!=null && entry.getValue().size() >0 ){

                Policy actPolicy = new Set(entry.getValue(),assets.get(entry.getKey().getURI()));
                tree.setPolicy(actPolicy,false);
            }
        }


        String secondPath = "./src/test/java/complex2.jsonld";
        Map<AssetCollection, List<Rule>>  mappaSecond = policyReader.readPolicyRules(secondPath);
        Map<String,Asset>assetsSecond = policyReader.readAssets(secondPath);
        Asset everySecond = new Asset("EveryAsset");

        for(Map.Entry<String,Asset> entry : assetsSecond.entrySet()) {
            if (entry.getValue().getParents().size() == 0)
                entry.getValue().addParent(everySecond);
        }

        assetsSecond.put("EveryAsset", everySecond);

        AssetTree treeSecond = new AssetTree(everySecond);

        for(Map.Entry<AssetCollection,List<Rule>> entry : mappaSecond.entrySet()){
            if(entry.getValue()!=null && entry.getValue().size() >0 ){

                Policy actPolicy = new Set(entry.getValue(),assetsSecond.get(entry.getKey().getURI()));

                treeSecond.setPolicy(actPolicy,false);
            }
        }


        Pair<AssetTree, Map<String, AssetCollection>> mergingResult = merger.intersection(assets,assetsSecond,tree,treeSecond);

        AssetTree resTree = mergingResult.getLeft();
        Map<String, AssetCollection> hier = mergingResult.getRight();
        documentProducer.produceDocument(resTree,hier,"./src/test/java/outputs/complex.ttl");
    }

}
