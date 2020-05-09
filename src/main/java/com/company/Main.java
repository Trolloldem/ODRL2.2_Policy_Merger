package com.company;

import Actions.Action;
import Assets.Asset;
import Assets.AssetCollection;
import Assets.AssetTree;
import Policy.Policy;
import Policy.Set;
import Rule.Rule;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;

import Parser.policyReader;
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
       try{
           PrintStream o = new PrintStream(new File("./src/main/java/Parser/output.txt"));




           // Assign o to output stream
           System.setOut(o);


       }catch (Exception e){
           System.err.println(e);
       };
//TODO Questo procedimento assume che vi sia stata una UNION nella gerarchia della policy di partenza
        String examplePath = "./src/main/java/Parser/doc2.jsonld";
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
        String secondPath = "./src/main/java/Parser/doc1.jsonld";
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

        java.util.Set<String> commonURI = new HashSet<String>(assets.keySet());
        java.util.Set<String> uriSetSecond = assetsSecond.keySet();
        Map<String, AssetCollection> finalAssets = new HashMap<String, AssetCollection>();
        commonURI.retainAll(uriSetSecond);
        AssetCollection lastEvery = new Asset("EveryAsset");
        finalAssets.put("EveryAsset",lastEvery);
        for(String uri : commonURI){
            if(uri.equals("EveryAsset"))
                continue;
            if(!finalAssets.containsKey(uri))
                finalAssets.put(uri,new Asset(uri));

            AssetCollection parentFirst = assets.get(uri).getParent();
            AssetCollection parentSecond = assetsSecond.get(uri).getParent();

            if(!parentFirst.getURI().equals(parentSecond.getURI()) && (parentFirst.getURI().equals("EveryAsset") || parentSecond.getURI().equals("EveryAsset"))){
                AssetCollection actParent = parentFirst.getURI().equals("EveryAsset") ? new Asset(parentSecond.getURI()) : new Asset(parentFirst.getURI());

                if(finalAssets.containsKey(actParent.getURI())){
                    finalAssets.get(actParent.getURI()).addChild(finalAssets.get(uri));
                }else {
                    actParent.addChild(finalAssets.get(uri));
                    finalAssets.put(actParent.getURI(),actParent);
                }
            }
            if(parentFirst.getURI().equals(parentSecond.getURI())){
                if(finalAssets.containsKey(parentFirst.getURI())){
                    finalAssets.get(parentFirst.getURI()).addChild(finalAssets.get(uri));
                }else {
                    AssetCollection actParent = new Asset(parentFirst.getURI());
                    actParent.addChild(finalAssets.get(uri));
                    finalAssets.put(actParent.getURI(),actParent);
                }
            }
        }

        for(String uri: assets.keySet()){
            if(!commonURI.contains(uri)){
                if(!finalAssets.containsKey(uri))
                    finalAssets.put(uri,new Asset(uri));

                if(finalAssets.containsKey(assets.get(uri).getParent().getURI())){
                    finalAssets.get(assets.get(uri).getParent().getURI()).addChild(finalAssets.get(uri));
                }else {
                    AssetCollection actParent = new Asset(assets.get(uri).getParent().getURI());
                    actParent.addChild(finalAssets.get(uri));
                    finalAssets.put(actParent.getURI(),actParent);
                }
            }
        }

        for(String uri : uriSetSecond){
            if(!commonURI.contains(uri)){
                if(!finalAssets.containsKey(uri))
                    finalAssets.put(uri,new Asset(uri));

                if(finalAssets.containsKey(assetsSecond.get(uri).getParent().getURI())){
                    finalAssets.get(assetsSecond.get(uri).getParent().getURI()).addChild(finalAssets.get(uri));
                }else {
                    AssetCollection actParent = new Asset(assetsSecond.get(uri).getParent().getURI());
                    actParent.addChild(finalAssets.get(uri));
                    finalAssets.put(actParent.getURI(),actParent);
                }
            }
        }
        Map<String,Policy> policyFirstTree = tree.recoverAllPolicy();
        Map<String,Policy> policySecondTree = treeSecond.recoverAllPolicy();
        AssetTree finalTree = new AssetTree(lastEvery);

        for(Map.Entry<String,AssetCollection> asset: finalAssets.entrySet()){
            Policy firstPolicy = policyFirstTree.get(asset.getKey());
            Policy secondPolicy = policySecondTree.get(asset.getKey());

            if(firstPolicy != null && secondPolicy != null){
                Policy intersection = firstPolicy.IntersectWith(secondPolicy);
                intersection.setTarget(asset.getValue());
                finalTree.setPolicy(intersection,true);
            }else if(firstPolicy != null || secondPolicy != null ){
                Policy toSet = firstPolicy == null ? secondPolicy : firstPolicy;
                toSet.setTarget(asset.getValue());
                finalTree.setPolicy(toSet,true);
            }
        }
        System.out.println(finalTree);
    }

}
