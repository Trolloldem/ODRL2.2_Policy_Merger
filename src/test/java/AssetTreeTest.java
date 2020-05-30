package test.java;

import Actions.Action;
import Assets.Asset;
import Assets.AssetTree;
import Policy.Policy;
import Policy.Set;
import Rule.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssetTreeTest {

    @Test
    public void inheritPolicyIntersection(){
        Asset root = new Asset("root");
        Asset Child1 = new Asset("Child1");
        Child1.addParent(root);
        Asset Child2 = new Asset("Child2");
        Child2.addParent(root);
        Asset Child3 = new Asset("Child3");
        Child3.addParent(root);
        Asset deepestChild = new Asset("Deepest");
        deepestChild.addParent(Child1);

        Rule delPerm = new Permission(Action.DELETE);
        Rule play = new Permission(Action.PLAY);
        Rule anon = new Prohibition(Action.ANONYMIZE);
        Rule inst = new Prohibition(Action.INSTALL);
        Rule ann = new Permission(Action.ANNOTATE);
        Rule del = new Prohibition(Action.DELETE);
        Rule use = new Permission(Action.USE);

        ArrayList<Rule> ruleRoot = new ArrayList<Rule>();
        ArrayList<Rule> ruleChild1 = new ArrayList<Rule>();
        ArrayList<Rule> ruleChild2 = new ArrayList<Rule>();
        ArrayList<Rule> ruleDeepest = new ArrayList<Rule>();

        ruleDeepest.add(use);
        ruleDeepest.add(inst);

        ruleRoot.add(play);
        ruleRoot.add(anon);
        ruleRoot.add(delPerm);

        ruleChild1.add(ann);
        ruleChild1.add(del);

        ruleChild2.add(play);
        ruleChild2.add(inst);


        Set policyChild1 = new Set(ruleChild1,Child1);
        Set policyChild2 = new Set(ruleChild2,Child2);
        Set policyRoot = new Set(ruleRoot,root);
        Set policyDeepest = new Set(ruleDeepest, deepestChild);

        AssetTree tree = new AssetTree(root);
        tree.setPolicy(policyDeepest,true);
        tree.setPolicy(policyRoot,true);
        tree.setPolicy(policyChild1,true);
        tree.setPolicy(policyChild2,true);

       java.util.Set<Asset> assetSet = new HashSet<Asset>();
       assetSet.add(root);
       assetSet.add(Child1);
       assetSet.add(Child2);
       assetSet.add(Child3);
       assetSet.add(deepestChild);

       for(Asset actAsset : assetSet){
           for(Map.Entry<Action,String > ruleState : actAsset.getPolicy().getUseTree().getAllStates().entrySet()){
               switch (actAsset.getURI()){
                   case "root":
                   case "Child3":
                       if(ruleState.getKey().equals(Action.PLAY) || ruleState.getKey().equals(Action.DISPLAY) ||ruleState.getKey().equals(Action.DELETE))
                       assertEquals("Permitted",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Permitted");
                       else if(ruleState.getKey().equals(Action.ANONYMIZE) )
                           assertEquals("Prohibited",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Prohibited");
                       else
                           assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        break;
                   case "Child1":
                       if(ruleState.getKey().equals(Action.PLAY) || ruleState.getKey().equals(Action.DISPLAY) || ruleState.getKey().equals(Action.ANNOTATE) )
                           assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                       else if(ruleState.getKey().equals(Action.ANONYMIZE) || ruleState.getKey().equals(Action.DELETE))
                           assertEquals("Prohibited",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Prohibited");
                       else
                           assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                       break;
                   case "Child2":
                       if(ruleState.getKey().equals(Action.PLAY) || ruleState.getKey().equals(Action.DISPLAY)  )
                           assertEquals("Permitted",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Permitted");
                       else if(ruleState.getKey().equals(Action.ANONYMIZE) || ruleState.getKey().equals(Action.INSTALL))
                           assertEquals("Prohibited",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Prohibited");
                       else if(ruleState.getKey().equals(Action.DELETE))
                           assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                       else
                           assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                       break;
                   case "Deepest":

                       if(ruleState.getKey().equals(Action.PLAY) || ruleState.getKey().equals(Action.DISPLAY) || ruleState.getKey().equals(Action.ANNOTATE) || ruleState.getKey().equals(Action.USE))
                           assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                       else if(ruleState.getKey().equals(Action.ANONYMIZE) || ruleState.getKey().equals(Action.DELETE) || ruleState.getKey().equals(Action.INSTALL))
                           assertEquals("Prohibited",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Prohibited");
                       else
                           assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                       break;
               }
           }
       }
    }

    @Test
    public void inheritPolicyUnion(){
        Asset root = new Asset("root");
        Asset Child1 = new Asset("Child1");
        Child1.addParent(root);
        Asset Child2 = new Asset("Child2");
        Child2.addParent(root);
        Asset Child3 = new Asset("Child3");
        Child3.addParent(root);
        Asset deepestChild = new Asset("Deepest");
        deepestChild.addParent(Child1);

        Rule delPerm = new Permission(Action.DELETE);
        Rule play = new Permission(Action.PLAY);
        Rule anon = new Prohibition(Action.ANONYMIZE);
        Rule inst = new Prohibition(Action.INSTALL);
        Rule ann = new Permission(Action.ANNOTATE);
        Rule del = new Prohibition(Action.DELETE);
        Rule use = new Permission(Action.USE);

        ArrayList<Rule> ruleRoot = new ArrayList<Rule>();
        ArrayList<Rule> ruleChild1 = new ArrayList<Rule>();
        ArrayList<Rule> ruleChild2 = new ArrayList<Rule>();
        ArrayList<Rule> ruleDeepest = new ArrayList<Rule>();

        ruleDeepest.add(use);
        ruleDeepest.add(inst);

        ruleRoot.add(play);
        ruleRoot.add(anon);
        ruleRoot.add(delPerm);

        ruleChild1.add(ann);
        ruleChild1.add(del);

        ruleChild2.add(play);
        ruleChild2.add(inst);


        Set policyChild1 = new Set(ruleChild1,Child1);
        Set policyChild2 = new Set(ruleChild2,Child2);
        Set policyRoot = new Set(ruleRoot,root);
        Set policyDeepest = new Set(ruleDeepest,deepestChild);

        AssetTree tree = new AssetTree(root);

        tree.setPolicy(policyDeepest,true);
        tree.setPolicy(policyRoot,false);
        tree.setPolicy(policyChild1,false);
        tree.setPolicy(policyChild2,false);

        java.util.Set<Asset> assetSet = new HashSet<Asset>();
        assetSet.add(root);
        assetSet.add(Child1);
        assetSet.add(Child2);
        assetSet.add(Child3);
        assetSet.add(deepestChild);

        for(Asset actAsset : assetSet){
            for(Map.Entry<Action,String > ruleState : actAsset.getPolicy().getUseTree().getAllStates().entrySet()){
                switch (actAsset.getURI()){
                    case "root":
                    case "Child3":
                        if(ruleState.getKey().equals(Action.PLAY) || ruleState.getKey().equals(Action.DISPLAY) ||ruleState.getKey().equals(Action.DELETE))
                            assertEquals("Permitted",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Permitted");
                        else if(ruleState.getKey().equals(Action.ANONYMIZE) )
                            assertEquals("Prohibited",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Prohibited");
                        else
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        break;
                    case "Child1":
                        if(ruleState.getKey().equals(Action.PLAY) || ruleState.getKey().equals(Action.DISPLAY) || ruleState.getKey().equals(Action.ANNOTATE) )
                            assertEquals("Permitted",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Permitted");
                        else if(ruleState.getKey().equals(Action.ANONYMIZE) || ruleState.getKey().equals(Action.DELETE))
                            assertEquals("Prohibited",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Prohibited");
                        else
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        break;
                    case "Child2":
                        if(ruleState.getKey().equals(Action.PLAY) || ruleState.getKey().equals(Action.DISPLAY) || ruleState.getKey().equals(Action.DELETE) )
                            assertEquals("Permitted",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Permitted");
                        else if(ruleState.getKey().equals(Action.ANONYMIZE) || ruleState.getKey().equals(Action.INSTALL))
                            assertEquals("Prohibited",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Prohibited");

                        else
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        break;
                    case "Deepest":

                        if(ruleState.getKey().equals(Action.PLAY) || ruleState.getKey().equals(Action.DISPLAY) || ruleState.getKey().equals(Action.ANNOTATE) )
                            assertEquals("Permitted",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Permitted");
                        else if(ruleState.getKey().equals(Action.ANONYMIZE) || ruleState.getKey().equals(Action.DELETE) || ruleState.getKey().equals(Action.INSTALL))
                            assertEquals("Prohibited",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Prohibited");
                        else if(ruleState.getKey().equals(Action.USE))
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        else
                            assertEquals("Permitted",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Permitted");
                        break;
                }
            }
        }
    }

    @Test
    public void policyUnion(){
        Asset root = new Asset("root");
        Asset Child1 = new Asset("Child1");
        Child1.addParent(root);
        Asset Child2 = new Asset("Child2");
        Child2.addParent(root);
        Asset Child3 = new Asset("Child3");
        Child3.addParent(root);
        Asset deepestChild = new Asset("Deepest");
        deepestChild.addParent(Child1);

        Rule delPerm = new Permission(Action.DELETE);
        Rule play = new Permission(Action.PLAY);
        Rule anon = new Prohibition(Action.ANONYMIZE);
        Rule inst = new Prohibition(Action.INSTALL);
        Rule ann = new Permission(Action.ANNOTATE);
        Rule del = new Prohibition(Action.DELETE);
        Rule use = new Permission(Action.USE);

        ArrayList<Rule> ruleRoot = new ArrayList<Rule>();
        ArrayList<Rule> ruleChild1 = new ArrayList<Rule>();
        ArrayList<Rule> ruleChild2 = new ArrayList<Rule>();
        ArrayList<Rule> ruleDeepest = new ArrayList<Rule>();

        ruleDeepest.add(use);
        ruleDeepest.add(inst);

        ruleRoot.add(play);
        ruleRoot.add(anon);
        ruleRoot.add(delPerm);

        ruleChild1.add(ann);
        ruleChild1.add(del);

        ruleChild2.add(play);
        ruleChild2.add(inst);


        Set policyChild1 = new Set(ruleChild1,Child1);
        Set policyChild2 = new Set(ruleChild2,Child2);
        Set policyRoot = new Set(ruleRoot,root);
        Set policyDeepest = new Set(ruleDeepest, deepestChild);

        AssetTree tree = new AssetTree(root);
        tree.setPolicy(policyDeepest,true);
        tree.setPolicy(policyRoot,true);
        tree.setPolicy(policyChild1,true);
        tree.setPolicy(policyChild2,true);

        java.util.Set<Asset> assetSet = new HashSet<Asset>();
        assetSet.add(root);
        assetSet.add(Child1);
        assetSet.add(Child2);
        assetSet.add(Child3);
        assetSet.add(deepestChild);

        ArrayList<Rule> ruleUnionChild3 = new ArrayList<Rule>();
        ArrayList<Rule> ruleUnionRoot = new ArrayList<Rule>();
        ArrayList<Rule> ruleUnionDeepest = new ArrayList<Rule>();

        Rule displProhib = new Prohibition(Action.DISPLAY);
        Rule arch = new Permission(Action.ARCHIVE);
        Rule exe = new Permission(Action.EXECUTE);
        Rule uninst = new Permission(Action.UNINSTALL);
        Rule stream = new Prohibition(Action.STREAM);

        ruleUnionDeepest.add(uninst);
        ruleUnionDeepest.add(stream);

        ruleUnionChild3.add(displProhib);
        ruleUnionChild3.add(arch);

        ruleUnionRoot.add(inst);
        ruleUnionRoot.add(exe);

        Set unionChild3 = new Set(ruleUnionChild3,Child3);
        Set rootUnion = new Set(ruleUnionRoot,root);
        Set unionDeepest = new Set(ruleUnionDeepest, deepestChild);

        tree.unitePolicy(unionChild3);
        tree.unitePolicy(rootUnion);
        tree.unitePolicy(unionDeepest);

        for(Asset actAsset : assetSet){
            for(Map.Entry<Action,String > ruleState : actAsset.getPolicy().getUseTree().getAllStates().entrySet()){
                switch (actAsset.getURI()){
                    case "root":
                        if(ruleState.getKey().equals(Action.PLAY) || ruleState.getKey().equals(Action.DISPLAY) ||ruleState.getKey().equals(Action.DELETE) || ruleState.getKey().equals(Action.EXECUTE))
                            assertEquals("Permitted",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Permitted");
                        else if(ruleState.getKey().equals(Action.ANONYMIZE) || ruleState.getKey().equals(Action.INSTALL))
                            assertEquals("Prohibited",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Prohibited");
                        else
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        break;
                    case "Child3":
                        if(ruleState.getKey().equals(Action.ARCHIVE) ||ruleState.getKey().equals(Action.DELETE) || ruleState.getKey().equals(Action.EXECUTE))
                            assertEquals("Permitted",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Permitted");
                        else if(ruleState.getKey().equals(Action.ANONYMIZE) || ruleState.getKey().equals(Action.INSTALL) || ruleState.getKey().equals(Action.PLAY) || ruleState.getKey().equals(Action.DISPLAY))
                            assertEquals("Prohibited",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Prohibited");
                        else
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        break;
                    case "Child1":
                        if(ruleState.getKey().equals(Action.PLAY) || ruleState.getKey().equals(Action.DISPLAY) || ruleState.getKey().equals(Action.EXECUTE) )
                            assertEquals("Permitted",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        else if(ruleState.getKey().equals(Action.ANONYMIZE) || ruleState.getKey().equals(Action.DELETE) || ruleState.getKey().equals(Action.INSTALL))
                            assertEquals("Prohibited",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Prohibited");
                        else
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        break;
                    case "Child2":
                        if(ruleState.getKey().equals(Action.PLAY) || ruleState.getKey().equals(Action.DISPLAY) || ruleState.getKey().equals(Action.EXECUTE)  ||ruleState.getKey().equals(Action.DELETE) )
                            assertEquals("Permitted",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Permitted");
                        else if(ruleState.getKey().equals(Action.ANONYMIZE) || ruleState.getKey().equals(Action.INSTALL))
                            assertEquals("Prohibited",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Prohibited");

                        else
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        break;
                    case "Deepest":

                        if(ruleState.getKey().equals(Action.PLAY) || ruleState.getKey().equals(Action.DISPLAY) || ruleState.getKey().equals(Action.EXECUTE) || ruleState.getKey().equals(Action.UNINSTALL))
                            assertEquals("Permitted",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        else if(ruleState.getKey().equals(Action.ANONYMIZE) || ruleState.getKey().equals(Action.DELETE) || ruleState.getKey().equals(Action.INSTALL) || ruleState.getKey().equals(Action.STREAM))
                            assertEquals("Prohibited",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Prohibited");
                        else if(ruleState.getKey().equals(Action.ANNOTATE) || ruleState.getKey().equals(Action.USE))
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        else
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        break;
                }
            }
        }
    }

    @Test
    public void policyIntersection(){
        Asset root = new Asset("root");
        Asset Child1 = new Asset("Child1");
        Child1.addParent(root);
        Asset Child2 = new Asset("Child2");
        Child2.addParent(root);
        Asset Child3 = new Asset("Child3");
        Child3.addParent(root);
        Asset deepestChild = new Asset("Deepest");
        deepestChild.addParent(Child1);

        Rule delPerm = new Permission(Action.DELETE);
        Rule play = new Permission(Action.PLAY);
        Rule anon = new Prohibition(Action.ANONYMIZE);
        Rule inst = new Prohibition(Action.INSTALL);
        Rule ann = new Permission(Action.ANNOTATE);
        Rule del = new Prohibition(Action.DELETE);
        Rule use = new Permission(Action.USE);

        ArrayList<Rule> ruleRoot = new ArrayList<Rule>();
        ArrayList<Rule> ruleChild1 = new ArrayList<Rule>();
        ArrayList<Rule> ruleChild2 = new ArrayList<Rule>();
        ArrayList<Rule> ruleDeepest = new ArrayList<Rule>();

        ruleDeepest.add(use);
        ruleDeepest.add(inst);

        ruleRoot.add(play);
        ruleRoot.add(anon);
        ruleRoot.add(delPerm);

        ruleChild1.add(ann);
        ruleChild1.add(del);

        ruleChild2.add(play);
        ruleChild2.add(inst);


        Set policyChild1 = new Set(ruleChild1,Child1);
        Set policyChild2 = new Set(ruleChild2,Child2);
        Set policyRoot = new Set(ruleRoot,root);
        Set policyDeepest = new Set(ruleDeepest, deepestChild);

        AssetTree tree = new AssetTree(root);
        tree.setPolicy(policyDeepest,true);
        tree.setPolicy(policyRoot,true);
        tree.setPolicy(policyChild1,true);
        tree.setPolicy(policyChild2,true);

        java.util.Set<Asset> assetSet = new HashSet<Asset>();
        assetSet.add(root);
        assetSet.add(Child1);
        assetSet.add(Child2);
        assetSet.add(Child3);
        assetSet.add(deepestChild);

        ArrayList<Rule> ruleUnionChild3 = new ArrayList<Rule>();
        ArrayList<Rule> ruleUnionRoot = new ArrayList<Rule>();
        ArrayList<Rule> ruleUnionDeepest = new ArrayList<Rule>();

        Rule displProhib = new Prohibition(Action.DISPLAY);
        Rule arch = new Permission(Action.ARCHIVE);
        Rule exe = new Permission(Action.EXECUTE);
        Rule uninst = new Permission(Action.UNINSTALL);
        Rule stream = new Prohibition(Action.STREAM);

        ruleUnionDeepest.add(uninst);
        ruleUnionDeepest.add(stream);

        ruleUnionChild3.add(displProhib);
        ruleUnionChild3.add(arch);

        ruleUnionRoot.add(inst);
        ruleUnionRoot.add(exe);

        Set unionChild3 = new Set(ruleUnionChild3,Child3);
        Set rootUnion = new Set(ruleUnionRoot,root);
        Set unionDeepest = new Set(ruleUnionDeepest, deepestChild);

        tree.intersectPolicy(unionChild3);
        tree.intersectPolicy(rootUnion);
        tree.intersectPolicy(unionDeepest);

        for(Asset actAsset : assetSet){
            for(Map.Entry<Action,String > ruleState : actAsset.getPolicy().getUseTree().getAllStates().entrySet()){
                switch (actAsset.getURI()){
                    case "root":
                        if(ruleState.getKey().equals(Action.PLAY) || ruleState.getKey().equals(Action.DISPLAY) ||ruleState.getKey().equals(Action.DELETE) || ruleState.getKey().equals(Action.EXECUTE))
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        else if(ruleState.getKey().equals(Action.ANONYMIZE) || ruleState.getKey().equals(Action.INSTALL))
                            assertEquals("Prohibited",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Prohibited");

                        else
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        break;
                    case "Child3":
                        if(ruleState.getKey().equals(Action.DELETE) )
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        else if(ruleState.getKey().equals(Action.ANONYMIZE) || ruleState.getKey().equals(Action.INSTALL) || ruleState.getKey().equals(Action.PLAY) || ruleState.getKey().equals(Action.DISPLAY))
                            assertEquals("Prohibited",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Prohibited");
                        else
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        break;
                    case "Child1":
                        if(ruleState.getKey().equals(Action.PLAY) || ruleState.getKey().equals(Action.DISPLAY) || ruleState.getKey().equals(Action.ANNOTATE) )
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        else if(ruleState.getKey().equals(Action.ANONYMIZE) || ruleState.getKey().equals(Action.DELETE) || ruleState.getKey().equals(Action.INSTALL))
                            assertEquals("Prohibited",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Prohibited");
                        else
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        break;
                    case "Child2":
                        if(ruleState.getKey().equals(Action.PLAY) || ruleState.getKey().equals(Action.DISPLAY)  )
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        else if(ruleState.getKey().equals(Action.ANONYMIZE) || ruleState.getKey().equals(Action.INSTALL))
                            assertEquals("Prohibited",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Prohibited");
                        else if(ruleState.getKey().equals(Action.DELETE))
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        else
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        break;
                    case "Deepest":

                        if(ruleState.getKey().equals(Action.PLAY) || ruleState.getKey().equals(Action.DISPLAY) || ruleState.getKey().equals(Action.ANNOTATE) || ruleState.getKey().equals(Action.USE))
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        else if(ruleState.getKey().equals(Action.ANONYMIZE) || ruleState.getKey().equals(Action.DELETE) || ruleState.getKey().equals(Action.INSTALL) || ruleState.getKey().equals(Action.STREAM))
                            assertEquals("Prohibited",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Prohibited");
                        else
                            assertEquals("Undefined",ruleState.getValue(), "L'azione "+ruleState.getKey()+" deve essere Undefined");
                        break;
                }
            }
        }
    }

}
