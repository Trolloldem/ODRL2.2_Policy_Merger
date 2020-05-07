package test.java;

import Actions.Action;
import Policy.Policy;
import Policy.Set;
import Rule.Rule;
import Rule.Permission;
import Rule.Prohibition;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SetTest {
    @Test
    public void policyIntesection() {

        //Rule
        Rule playPerm = new Permission(Action.PLAY);
        Rule displayProhib = new Prohibition(Action.DISPLAY);
        Rule annotatePerm = new Permission(Action.ANNOTATE);
        Rule anonProhib = new Prohibition(Action.ANONYMIZE);

        List<Rule> ruleList1 = new ArrayList<Rule>();
        List<Rule> ruleList2 = new ArrayList<Rule>();

        ruleList1.add(playPerm);
        ruleList1.add(anonProhib);

        ruleList2.add(annotatePerm);
        ruleList2.add(displayProhib);

        Policy ruleSet1 = new Set(ruleList1);
        Policy ruleSet2 = new Set(ruleList2);

        Policy ruleSet3 = ruleSet1.IntersectWith(ruleSet2);


        for(Action tested : Action.values()){
            if(!(tested.equals(Action.TRANSFER) || tested.equals(Action.GIVE) || tested.equals(Action.SELL))){
                if(tested.equals(Action.PLAY) || tested.equals(Action.DISPLAY) || tested.equals(Action.ANONYMIZE)) {
                    assertEquals("Prohibited", ruleSet3.getUseTree().getAllStates().get(tested), "L'azione deve essere 'Prohibited'");
                }else if (tested.equals(Action.ANNOTATE)) {

                    assertEquals("Undefined", ruleSet3.getUseTree().getAllStates().get(tested), "L'azione ANNOTATE deve essere 'Undefined'");
                }else{
                    assertEquals("Undefined", ruleSet3.getUseTree().getAllStates().get(tested), "Le altre azioni devono essere 'Undefined'");
                }
            } else{
                assertEquals(null, ruleSet3.getUseTree().getAllStates().get(tested), "L'azione non è definita poiché non è una sottoazione di USE");
            }

        }


    }

    @Test
    public void policyUnion() {
        //Rule
        Rule playPerm = new Permission(Action.PLAY);
        Rule displayProhib = new Prohibition(Action.DISPLAY);
        Rule usePerm = new Permission(Action.USE);



        List<Rule> ruleList1 = new ArrayList<Rule>();
        List<Rule> ruleList2 = new ArrayList<Rule>();

        ruleList1.add(playPerm);

        ruleList2.add(usePerm);
        ruleList2.add(displayProhib);

        Policy ruleSet1 = new Set(ruleList1);
        Policy ruleSet2 = new Set(ruleList2);

        Policy ruleSet3 = ruleSet1.UniteWith(ruleSet2);


        for(Action tested : Action.values()){
            if(!(tested.equals(Action.TRANSFER) || tested.equals(Action.GIVE) || tested.equals(Action.SELL))){
                if(tested.equals(Action.PLAY) || tested.equals(Action.DISPLAY) ) {
                    assertEquals("Prohibited", ruleSet3.getUseTree().getAllStates().get(tested), "L'azione deve essere 'Prohibited'");
                }else if (tested.equals(Action.USE)) {

                    assertEquals("Undefined", ruleSet3.getUseTree().getAllStates().get(tested), "L'azione USE deve essere 'Undefined'");
                }else{
                    assertEquals("Permitted", ruleSet3.getUseTree().getAllStates().get(tested), "Le altre azioni devono essere 'Undefined'");
                }
            } else{
                assertEquals(null, ruleSet3.getUseTree().getAllStates().get(tested), "L'azione non è definita poiché non è una sottoazione di USE");
            }

        }
    }

    @Test
    public void policyUnionAndIntersect(){
        Rule playPerm = new Permission(Action.PLAY);
        Rule displayProhib = new Prohibition(Action.DISPLAY);
        Rule usePerm = new Permission(Action.USE);
        Rule annotatePerm = new Permission(Action.ANNOTATE);
        Rule anonProhib = new Prohibition(Action.ANONYMIZE);


        List<Rule> ruleList1 = new ArrayList<Rule>();
        List<Rule> ruleList2 = new ArrayList<Rule>();
        List<Rule> ruleList3 = new ArrayList<Rule>();

        ruleList1.add(playPerm);

        ruleList2.add(usePerm);
        ruleList2.add(displayProhib);

        ruleList3.add(annotatePerm);
        ruleList3.add(anonProhib);

        Policy ruleSet1 = new Set(ruleList1);
        Policy ruleSet2 = new Set(ruleList2);
        Policy ruleSet3 = new Set(ruleList3);

        Policy ruleSetUnion = ruleSet1.UniteWith(ruleSet2);

        Policy ruleSetIntersection = ruleSetUnion.IntersectWith(ruleSet3);

        for(Action tested : Action.values()){
            if(!(tested.equals(Action.TRANSFER) || tested.equals(Action.GIVE) || tested.equals(Action.SELL))){
                if(tested.equals(Action.PLAY) || tested.equals(Action.DISPLAY) || tested.equals(Action.ANONYMIZE) ) {
                    assertEquals("Prohibited", ruleSetIntersection.getUseTree().getAllStates().get(tested), "L'azione deve essere 'Prohibited'");
                }else if (tested.equals(Action.USE)) {

                    assertEquals("Undefined", ruleSetIntersection.getUseTree().getAllStates().get(tested), "L'azione USE deve essere 'Undefined'");
                }else if(tested.equals(Action.ANNOTATE)){
                    assertEquals("Permitted", ruleSetIntersection.getUseTree().getAllStates().get(tested), "L'azione ANNOTATE deve essere 'Permitted'");
                }else{
                    assertEquals("Undefined", ruleSetIntersection.getUseTree().getAllStates().get(tested), "Le altre azioni devono essere 'Undefined'");
                }
            } else{
                assertEquals(null, ruleSetIntersection.getUseTree().getAllStates().get(tested), "L'azione non è definita poiché non è una sottoazione di USE");
            }

        }
    }
}