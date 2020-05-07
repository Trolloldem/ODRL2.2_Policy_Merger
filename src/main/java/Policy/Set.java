package Policy;
import Assets.AssetCollection;
import Rule.Rule;
import Rule.Permission;
import Rule.Prohibition;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import Rule.RuleTree;
import Actions.Action;


public class Set implements Policy {
    private String type="Set";
    private List<Rule> RuleList;
    private RuleTree useTree=new RuleTree(Action.USE);
    private RuleTree transferTree=new RuleTree(Action.TRANSFER);
    private ArrayList<Prohibition> listProhib = new ArrayList<Prohibition>();
    private AssetCollection target;


    public Set(List<Rule> RuleList){
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
    public Set(AssetCollection target){
        this.RuleList = new ArrayList<Rule>();
        this.target= target;
    }

    public Set(List<Rule> RuleList, AssetCollection target){
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

        this.target=target;
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
    public List<Rule> getRules() {
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
        //Intersezione: una regola è permessa se e solo se è permessa esplicitamente da entrambi

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


    }

    /**
     * Getter dell'asset target della policy
     *
     * @return AssetCollection target della policy
     */
    @Override
    public AssetCollection getTarget() {
        return target;
    }

    /**
     * Setter dell'asset target della policy
     *
     * @param target: AssetCollection target della policy
     */
    @Override
    public void setTarget(AssetCollection target) {
        this.target=target;
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
    @Override
    public RuleTree getUseTree(){
        return useTree;
    }
    /**
     * Getter per albero dei permessi relativo alle sottoazioni di TRANSFER
     * @return RuleTree dei permessi relativi alle sottoazioni di TRANSFER
     */
    @Override
    public RuleTree getTransferTree(){
        return transferTree;
    }
}
