package com.company;

import Actions.Action;
import Assets.Asset;
import Assets.AssetCollection;
import Assets.AssetTree;
import Policy.Policy;
import Policy.Set;
import Rule.Rule;
import Rule.RuleTree;
import Rule.Permission;
import Rule.Prohibition;

import java.util.*;

import Parser.policyReader;
import org.apache.jena.base.Sys;

public class Main {

    public static void main(String[] args) {
/** TEST GENERICO

        Set p=new Set(new ArrayList<Rule>());
        RuleTree tree=p.getUseTree();
        tree.setActionPermitted(Action.SHARING);
        tree.setActionProhibited(Action.SHARING);
        System.out.println(tree.getActionState(Action.SHARING));
        tree.setActionPermitted(Action.USE);
        tree.setActionPermitted(Action.PLAY);
        tree.setActionProhibited(Action.DISPLAY);
        tree.setActionPermitted(Action.USE);
        System.out.println("USE è: "+tree.getActionState(Action.USE));
        RuleTree tt=p.getTransferTree();
        tt.setActionPermitted(Action.TRANSFER);
        tt.setActionProhibited(Action.SELL);
        System.out.println(tt.getActionState(Action.TRANSFER));
        System.out.println(tt.getActionState(Action.GIVE));
        System.out.println(tt.getActionState(Action.SELL));
        tree.setActionProhibited(Action.USE);
        System.out.println(tree.getActionState(Action.EXTRACT));
        for (Map.Entry<Action, String> entry : tt.getAllStates().entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
/**
TEST UNION di policy

        Rule share = new Permission(Action.USE);
        Rule allowDelete = new Permission(Action.DELETE);
        Rule denyPlay = new Prohibition(Action.PLAY);
        ArrayList<Rule> rule1=new ArrayList<Rule>();
        ArrayList<Rule> rule2=new ArrayList<Rule>();
        rule1.add(share);
        rule2.add(allowDelete);
        rule2.add(denyPlay);
        Set p1=new Set(rule1);
        Set p2=new Set(rule2);
        Set p3=p1.UniteWith(p2);
        for (Map.Entry<Action, String> entry : p3.getUseTree().getAllStates().entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }

 TEST INTERSEZIONE
        Rule play = new Permission(Action.PLAY);
        Rule share = new Permission(Action.SHARING);
        Rule allowDisplay = new Permission(Action.DISPLAY);
        Rule denyDELETE = new Prohibition(Action.DELETE);
        ArrayList<Rule> rule1=new ArrayList<Rule>();
        ArrayList<Rule> rule2=new ArrayList<Rule>();
        rule1.add(share);
        rule2.add(share);
        rule2.add(allowDisplay);
        rule2.add(denyDELETE);
        rule1.add(play);
        Set p1=new Set(rule1);
        Set p2=new Set(rule2);
        Set p3=p1.IntersectWith(p2);
        for (Map.Entry<Action, String> entry : p3.getUseTree().getAllStates().entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }

        TEST SEQUENZA INTERSECT E UNION

        Rule play = new Permission(Action.PLAY);
        Rule share = new Permission(Action.SHARING);
        Rule transform = new Prohibition(Action.PLAY);
        ArrayList<Rule> rule1=new ArrayList<Rule>();
        ArrayList<Rule> rule2=new ArrayList<Rule>();
        rule1.add(play);
        rule2.add(share);
        Set p1=new Set(rule1);
        Set p2=new Set(rule2);
        Set p3=p1.UniteWith(p2);
        for (Map.Entry<Action, String> entry : p3.getUseTree().getAllStates().entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
        ArrayList<Rule> rule4=new ArrayList<Rule>();
        rule4.add(share);
        rule4.add(transform);
        Set p4 = new Set(rule4);
        p3 = p3.IntersectWith(p4);
        for (Map.Entry<Action, String> entry : p3.getUseTree().getAllStates().entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
        p3 = p3.UniteWith(p1);
        for (Map.Entry<Action, String> entry : p3.getUseTree().getAllStates().entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }

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

//TODO cambiare policyReader con ruleReader
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
                System.out.println(entry.getValue());
                Policy actPolicy = new Set(entry.getValue(),assets.get(entry.getKey().getURI()));
                tree.setPolicy(actPolicy,false);
            }
        }

        for(Map.Entry<String,Asset> entri : assets.entrySet()){
            System.out.println("===========================\n"+entri.getKey());
            System.out.println(entri.getValue().getParent().getURI());
            for (Map.Entry<Action, String> entry : ((Set)entri.getValue().getPolicy()).getUseTree().getAllStates().entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
        }
    }



}