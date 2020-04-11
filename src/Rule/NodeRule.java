package Rule;

import Actions.Action;

import java.util.HashMap;
/**
 * Classe relativa alla gestione di un nodo dell'albero dei permessi di una policy
 */
public class NodeRule {
    private Action azione;
    private String stato;
    private HashMap<Action,NodeRule> includedActions=new HashMap<Action, NodeRule>();
    private int prohibIncluded=0;
    private Boolean prohibIncludedByChild=false;
    private NodeRule father=null;



    /**
     * Metodo per il recupero dell'azione rappresentata dal nodo
     * @return Azione rappresentata dal nodo
     */
    public Action getAction(){
        return azione;
    }

    /**
     * Incremento del conteggio del numero di azioni incluse proibite. Quando tutte le azioni incluse
     * sono proibite, anche l'azione rappresentata dal nodo risulterà vietata
     */
    public void addProhibitedCount(){
        prohibIncluded++;
        if(prohibIncluded==azione.getIncludedBy().length){
            this.setProhibited();
        }
        if(this.father!=null){
            this.father.setProhibIncludedByChild();
        }
    }

    /**
     * Set a true della flag che indica se a più di un solo nodo di profondità è stata proibita un'azione inclusa
     */
    public void setProhibIncludedByChild(){
        this.prohibIncludedByChild=true;
        if(this.father!=null){
            this.father.setProhibIncludedByChild();
        }
    }
    public NodeRule(Action a){
        azione=a;
        stato="Undefined";
        if(a.getIncludedBy().length>0) {
            for (Action included : a.getIncludedBy()) {
                includedActions.put(included, new NodeRule(included, this));
            }
        }
    }
    public NodeRule(Action a,NodeRule father){
        azione=a;
        stato="Undefined";
        this.father=father;
        for(Action included:a.getIncludedBy()){
            includedActions.put(included,new NodeRule(included,this));
        }
    }
    /**
     * Setta l'azione rappresentata dal nodo come "Permitted" a meno che questa non fosse già esplicitamente vietata.
     * Il permesso viene propagato anche alle azioni incluse dall'azione rappresentata dal nodo.
     */
    public void setPermitted(){
        if (!(this.stato.equals("Prohibited"))){
            this.stato="Permitted";

            NodeRule node=null;
            for(Action included:azione.getIncludedBy()){
                node=includedActions.get(included);
                node.setPermitted();
            }
        }
        if(prohibIncludedByChild && prohibIncluded>0 && !(this.stato.equals("Prohibited")))
            this.stato="Undefined";
    }
    /**
     * Setta l'azione rappresentata dal nodo come "Prohibited", inoltre incrementa il conteggio delle azioni incluse
     * proibite del nodo padre. Qualora l'azione padre fosse stata permessa, viene settata come "Undefined".
     * Il divieto viene propagato anche alle azioni incluse dall'azione rappresentata dal nodo.
     */
    public void setProhibited(){
        if (!(this.stato.equals("Prohibited"))){

            this.stato="Prohibited";
            NodeRule node=null;
            for(Action included:azione.getIncludedBy()){
                node=includedActions.get(included);
                node.setProhibited();
            }
            if(this.father!=null){
                this.father.setUndefined();
                this.father.addProhibitedCount();
                this.father.setProhibIncludedByChild();
            }
        }

    }
    /**
     * Setta l'azione rappresentata dal nodo come "Undefined" a meno che questa non fosse esplicitamente vietata.
     */
    private void setUndefined(){
        if (!(this.stato.equals("Prohibited"))){
            this.stato="Undefined";
        }
    }
    /**
     * Recupera una mappa contenente i nodi figli, indicizzati mediante l'azione rappresentata
     * @return HashMap<Action,NodeRule>, dove le chiavi sono le azioni rappresentate, mentre gli elementi i nodi che le
     * rappresentano
     **/
    public HashMap<Action,NodeRule> getChilds(){
        return includedActions;
    }
    /**
     * Recupera il permesso relativo all'azione rappresentata dal nodo
     * @return Stringa rappresentate il permesso relativo all'azione rappresentata dal nodo
     **/
    public String getState(){
        return stato;
    }
}
