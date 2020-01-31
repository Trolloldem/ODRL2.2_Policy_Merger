package com.company;

import Actions.Action;
import Policy.Set;
import Rule.Rule;
import Rule.Permission;
import Rule.Prohibition;
import Rule.RuleTree;
import java.util.Map;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
/** uso generico su singole regole
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
 **/
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
    }

}
