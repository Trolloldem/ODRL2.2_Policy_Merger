package com.company;

import Actions.Action;
import Assets.Asset;
import Assets.AssetCollection;
import Assets.AssetTree;
import Policy.Policy;
import Policy.Set;
import Rule.Rule;

import java.util.*;

import Parser.policyReader;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
public class Main {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Main.class);
        BasicConfigurator.configure();

/**



       TEST EREDITà ASSET


        Asset root = new Asset();
        Asset rootChild1 = new Asset();
        rootChild1.setParent(root);
        Asset rootChild2 = new Asset();
        rootChild2.setParent(root);
        Asset rootChild3 = new Asset();
        rootChild3.setParent(root);

        Rule play = new Permission(Action.PLAY);
        Rule anon = new Prohibition(Action.ANONYMIZE);
        Rule display = new Prohibition(Action.DISPLAY);
        ArrayList<Rule> ruleRoot = new ArrayList<Rule>();
        ArrayList<Rule> ruleChild2 = new ArrayList<Rule>();
        ruleRoot.add(display);
        ruleChild2.add(play);
        ruleChild2.add(anon);
        Set policyChild2 = new Set(ruleChild2,rootChild2);
        Set policyRoot = new Set(ruleRoot,root);
        AssetTree tree = new AssetTree(root);

        tree.setPolicy(policyChild2);
        tree.setPolicy(policyRoot,false);

        for (Map.Entry<Action, String> entry : ((Set)rootChild2.getPolicy()).getUseTree().getAllStates().entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }

        System.out.println("=======================\n" +
                            "Interseco DALLA ROOT");

        ArrayList<Rule> ruleRoot2 = new ArrayList<Rule>();
        ruleRoot2.add(new Permission(Action.ANONYMIZE));
        Set policyRoot2 = new Set(ruleRoot2,root);
        tree.intersectPolicy(policyRoot2);

        for (Map.Entry<Action, String> entry : ((Set)rootChild2.getPolicy()).getUseTree().getAllStates().entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
        System.out.println("=======================\n" +
                "UNISCO DALLA ROOT");

        ArrayList<Rule> ruleRoot3 = new ArrayList<Rule>();
        ruleRoot3.add(new Permission(Action.WATERMARK));
        Set policyRoot3 = new Set(ruleRoot3,root);
        tree.unitePolicy(policyRoot3);

        for (Map.Entry<Action, String> entry : ((Set)rootChild3.getPolicy()).getUseTree().getAllStates().entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }



 TEST DA FILE
 **/
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
    }



}
