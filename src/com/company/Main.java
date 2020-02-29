package com.company;

import Actions.Action;
import Assets.Asset;
import Assets.AssetTree;
import Policy.Set;
import Rule.Rule;
import Rule.Permission;
import Rule.Prohibition;
import Rule.RuleTree;
import javafx.beans.property.SetProperty;

import java.util.Map;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
/** TEST GENERICO

        Set p=new Set(new ArrayList<Rule>());
        RuleTree tree=p.getUseTree();
        tree.setActionPermitted(Action.SHARING);
        tree.setActionProhibited(Action.SHARING);
        System.out.println(tree.getActionState(Action.SHARING));
        tree.setActionProhibited(Action.PLAY);
        tree.setActionPermitted(Action.PLAY);
        System.out.println(tree.getActionState(Action.PLAY));
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


 **/

        Asset root = new Asset();
        Asset rootChild1 = new Asset();
        rootChild1.setParent(root);
        Asset rootChild2 = new Asset();
        rootChild2.setParent(root);
        Asset rootChild3 = new Asset();
        rootChild3.setParent(root);

        Rule display = new Permission(Action.DISPLAY);
        Rule play = new Prohibition(Action.PLAY);
        ArrayList<Rule> ruleRoot = new ArrayList<Rule>();
        ArrayList<Rule> ruleChild2 = new ArrayList<Rule>();
        ruleRoot.add(display);
        ruleChild2.add(play);
        Set policyChild2 = new Set(ruleChild2,rootChild2);
        Set policyRoot = new Set(ruleRoot,root);
        AssetTree tree = new AssetTree(root);

        tree.setPolicy(policyChild2);
        tree.setPolicy(policyRoot,false);

        for (Map.Entry<Action, String> entry : ((Set)rootChild2.getPolicy()).getUseTree().getAllStates().entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }

        Set intPolicy = new Set(ruleRoot,rootChild2);
        tree.intersectPolicy(intPolicy);
        System.out.println("\nAFTER INTERSECT\n");
        for (Map.Entry<Action, String> entry : ((Set)rootChild2.getPolicy()).getUseTree().getAllStates().entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }

        ArrayList<Rule> ruleuse= new ArrayList<Rule>();
        ruleuse.add(new Permission(Action.USE));
        Set allPolicy = new Set(ruleuse,rootChild2);
        tree.unitePolicy(allPolicy);
        System.out.println("\nAFTER INTERSECT\n");
        for (Map.Entry<Action, String> entry : ((Set)rootChild2.getPolicy()).getUseTree().getAllStates().entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }


    }



}
