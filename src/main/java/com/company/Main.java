package com.company;


import Assets.Asset;
import Assets.AssetCollection;
import Assets.AssetTree;
import Policy.Policy;
import Policy.Set;
import Rule.Rule;
import Writer.documentProducer;
import mergingProcedure.merger;

import java.io.File;

import java.io.PrintStream;
import java.util.*;

import Parser.policyReader;
import org.apache.jena.atlas.lib.Pair;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
public class Main {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Main.class);
        BasicConfigurator.configure();



/**
 TEST DA FILE

        String examplePath = "./src/main/java/Parser/data.jsonld";
        Map<AssetCollection, List<Rule>>  mappa = policyReader.readPolicyRules(examplePath);
        Asset every = new Asset("EveryAsset");
        Map<String,Asset> assets = policyReader.readAssets(examplePath);

        for(Map.Entry<String,Asset> entry : assets.entrySet()) {
            if (entry.getValue().getParent() == null)
                entry.getValue().setParent(every);
        }

        assets.put("EveryAsset", every);

        AssetTree tree = new AssetTree(every);

        for(Map.Entry<AssetCollection,List<Rule>> entry : mappa.entrySet()){
            if(entry.getValue()!=null && entry.getValue().size() >0 ){

                Policy actPolicy = new Set(entry.getValue(),assets.get(entry.getKey().getURI()));
                tree.setPolicy(actPolicy,true);
            }
        }

        for(Map.Entry<String,Asset> entri : assets.entrySet()){
            System.out.println("===========================\n"+entri.getKey());
            if(entri.getValue().getPolicy()!=null)
            for (Map.Entry<Action, String> entry : ((Set)entri.getValue().getPolicy()).getUseTree().getAllStates().entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
        }

TEST 2 FILE
**/

        String examplePath = "./src/test/java/doc1.jsonld";
        Map<AssetCollection, List<Rule>>  mappa = policyReader.readPolicyRules(examplePath);
        Asset every = new Asset("EveryAsset");
        Map<String,Asset> assets = policyReader.readAssets(examplePath);

        for(Map.Entry<String,Asset> entry : assets.entrySet()) {

            if (entry.getValue().getParent() == null)
                entry.getValue().setParent(every);
        }

        assets.put("EveryAsset", every);

        AssetTree tree = new AssetTree(every);

        for(Map.Entry<AssetCollection,List<Rule>> entry : mappa.entrySet()){
            if(entry.getValue()!=null && entry.getValue().size() >0 ){

                Policy actPolicy = new Set(entry.getValue(),assets.get(entry.getKey().getURI()));
                tree.setPolicy(actPolicy,false);
            }
        }

        System.out.println(tree);

        System.out.println("SECONDA POLICY");
        String secondPath = "./src/test/java/doc2.jsonld";
        Map<AssetCollection, List<Rule>>  mappaSecond = policyReader.readPolicyRules(secondPath);
        Map<String,Asset>assetsSecond = policyReader.readAssets(secondPath);
        Asset everySecond = new Asset("EveryAsset");

        for(Map.Entry<String,Asset> entry : assetsSecond.entrySet()) {
            if (entry.getValue().getParent() == null)
                entry.getValue().setParent(everySecond);
        }

        assetsSecond.put("EveryAsset", everySecond);

        AssetTree treeSecond = new AssetTree(everySecond);

        for(Map.Entry<AssetCollection,List<Rule>> entry : mappaSecond.entrySet()){
            if(entry.getValue()!=null && entry.getValue().size() >0 ){

                Policy actPolicy = new Set(entry.getValue(),assetsSecond.get(entry.getKey().getURI()));

                treeSecond.setPolicy(actPolicy,false);
            }
        }

        System.out.println(treeSecond);
        Pair<AssetTree, Map<String, AssetCollection>> mergingResult = merger.intersection(assets,assetsSecond,tree,treeSecond);

        AssetTree resTree = mergingResult.getLeft();
        System.out.println(resTree.toString());
        Map<String, AssetCollection> hier = mergingResult.getRight();
        documentProducer.produceDocument(resTree,hier);

    }

}
