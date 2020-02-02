package Policy;
import Rule.Rule;
import Rule.Permission;
import Rule.Prohibition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import Rule.RuleTree;
import Actions.Action;
import Rule.NodeRule;

public class Set implements Policy {
    private String type="Set";
    private ArrayList<Rule> RuleList;
    private RuleTree useTree=new RuleTree(Action.USE);
    private RuleTree transferTree=new RuleTree(Action.TRANSFER);
    private ArrayList<Prohibition> listProhib = new ArrayList<Prohibition>();

    public Set(ArrayList<Rule> RuleList){
        this.RuleList=RuleList;

        for(Rule r:this.RuleList){
            if(r instanceof Permission){

                useTree.setActionPermitted(r.getAction());
                transferTree.setActionPermitted(r.getAction());

            }else if(r instanceof Prohibition){

                useTree.setActionProhibited(r.getAction());
                transferTree.setActionProhibited(r.getAction());
                listProhib.add((Prohibition)r);
            }
        }
    }
    /**
     * Getter della lista dei divieti presenti nella policy
     * @return ArrayList di Prohibition
     */
    public ArrayList<Prohibition> getListProhib(){
        return listProhib;
    }
    /**
     * Getter della lista di regole presenti nella policy
     * @return ArrayList di Rule
     */
    @Override
    public ArrayList<Rule> getRules() {
        return RuleList;
    }
    /**
     * Getter del tipo di policy
     * @return Stringa "Set"
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Interseca i permessi della policy di cui si chiama il metodo con quelli della policy p
     * @param p : policy con la quale si vuole intersecare la policy attuale
     * @return Policy creata tramite intersezione
     */
    @Override
    public Set IntersectWith(Policy p) {
        Set res=null;
        if(p instanceof Set) {
            ArrayList<Rule> resRuleList = new ArrayList<Rule>();
            RuleTree thisUseTree = this.getUseTree();
            RuleTree pUseTree = ((Set) p).getUseTree();
            RuleTree thisTransferTree = this.getTransferTree();
            RuleTree pTransferTree = ((Set) p).getTransferTree();

            ConcurrentHashMap<Action, String> thisStates = thisUseTree.getAllStates();
            ConcurrentHashMap<Action, String> pStates = pUseTree.getAllStates();
            for (Map.Entry<Action, String> entry : thisStates.entrySet()) {

                if (pStates.get(entry.getKey()).equals("Prohibited") || entry.getValue().equals("Prohibited")) {
                    resRuleList.add(new Prohibition(entry.getKey()));
                    continue;
                }
                if (pStates.get(entry.getKey()).equals("Undefined") || entry.getValue().equals("Undefined")) {
                    continue;
                }
                resRuleList.add(new Permission(entry.getKey()));
            }
            thisStates = thisTransferTree.getAllStates();
            pStates = pTransferTree.getAllStates();
            for (Map.Entry<Action, String> entry : thisStates.entrySet()) {

                if (pStates.get(entry.getKey()).equals("Prohibited") || entry.getValue().equals("Prohibited")) {
                    resRuleList.add(new Prohibition(entry.getKey()));
                    continue;
                }
                if (pStates.get(entry.getKey()).equals("Undefined") || entry.getValue().equals("Undefined")) {
                    continue;
                }
                resRuleList.add(new Permission(entry.getKey()));
            }
            resRuleList.addAll(this.getListProhib());
            resRuleList.addAll(((Set) p).getListProhib());
            res = new Set(resRuleList);
        }
        return res;
       /** Set res=null;
        ArrayList<Rule> resRuleList = new ArrayList<Rule>();
        ArrayList<Action> thisPermittedAction=new ArrayList<>();
        ArrayList<Action> pPermittedAction=new ArrayList<>();
        NodeRule visited=null;
        /**
         * Per entrambe le policy da intersecare si estrae una lista dei permessi più inclusivi da loro presentati,
         * il procedimento è ripetuto per entrambi gli alberi dei permessi

        if(p instanceof Set){
            for(Rule r : RuleList){
                if(r instanceof Permission){
                    visited=useTree.getLargestPermission(r.getAction());
                    if(visited!=null){
                        if(!thisPermittedAction.contains(visited.getAction())){
                            thisPermittedAction.add(visited.getAction());
                        }
                    }
                    visited=transferTree.getLargestPermission(r.getAction());
                    if(visited!=null){
                        if(!thisPermittedAction.contains(visited.getAction())){
                            thisPermittedAction.add(visited.getAction());
                        }
                    }
                }
            }
            for(Rule r : p.getRules()){
                if(r instanceof Permission){
                    visited=((Set) p).getUseTree().getLargestPermission(r.getAction());
                    if(visited!=null){
                        if(!pPermittedAction.contains(visited.getAction())){
                            pPermittedAction.add(visited.getAction());
                        }
                    }
                    visited=((Set) p).getTransferTree().getLargestPermission(r.getAction());
                    if(visited!=null){
                        if(!pPermittedAction.contains(visited.getAction())){
                            pPermittedAction.add(visited.getAction());
                        }
                    }
                }
            }
            /**
             * Per ogni permesso di entrambe le policy, si controlla se l'altra policy permette la stessa azione o
             * solamente un subset di questa

            for(Action a : thisPermittedAction){
                List<Action> listChild=Arrays.asList(a.getIncludedBy());
                for(Action sec: pPermittedAction){
                    if(listChild.contains(sec)){
                        resRuleList.add(new Permission(sec));
                    }
                    if(sec.getName().equals(a.getName())){
                        resRuleList.add(new Permission(sec));
                        break;
                    }
                }
            }
            for(Action a : pPermittedAction){
                List<Action> listChild=Arrays.asList(a.getIncludedBy());
                for(Action sec: thisPermittedAction){
                    if(listChild.contains(sec)){
                        resRuleList.add(new Permission(sec));
                    }
                    if(sec.getName().equals(a.getName())){
                        resRuleList.add(new Permission(sec));
                        break;
                    }
                }
            }
            //Si aggiungono alla lista anche i divieti di entrambe, non è necessario fare preprocessing
            resRuleList.addAll(listProhib);
            resRuleList.addAll(((Set) p).getListProhib());
            res = new Set(resRuleList);
        }
        return res;**/
    }

    /**
     * Unisce i permessi della policy di cui si chiama il metodo con quelli della policy p
     * @param p : policy con la quale si vuole unire la policy attuale
     * @return Policy creata tramite unione
     */
    @Override
    public Set UniteWith(Policy p) {

        Set res=null;
        ArrayList<Rule> resRuleList = new ArrayList<Rule>();
        if(p instanceof Set){
            resRuleList.addAll(this.RuleList);
            resRuleList.addAll(p.getRules());
            res=new Set(resRuleList);
        }
        return res;
    }
    /**
     * Getter per albero dei permessi relativo alle sottoazioni di USE
     * @return RuleTree dei permessi relativi alle sottoazioni di USE
     */
    public RuleTree getUseTree(){
        return useTree;
    }
    /**
     * Getter per albero dei permessi relativo alle sottoazioni di TRANSFER
     * @return RuleTree dei permessi relativi alle sottoazioni di TRANSFER
     */
    public RuleTree getTransferTree(){
        return transferTree;
    }
}
