package Rule;

import Actions.Action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Classe relativa alla gestione dell'albero dei permessi di una policy
 */
public class RuleTree {
    private NodeRule root=null;

    public RuleTree(Action root){
        this.root=new NodeRule(root);
    }

    /**
     * Metodo per il recupero di un nodo relativo alle
     * @param a Azione di cui si vuole recuperare il nodo
     * @return NodeRule relativo all'azione ricercata
     */
    public NodeRule getActionNode(Action a){
        ArrayList<Action> steps=new ArrayList<Action>();
        Action visited=a;
        NodeRule visitedNode=root;
        while(visited!=null){
            steps.add(visited);
            visited=visited.getIncludedIn();
        }
        if(visitedNode.getAction().getName().equals(steps.get(steps.size()-1).getName())){
            steps.remove(steps.size()-1);
            while(!(visitedNode.getAction().getName().equals(a.getName()))){
                visitedNode=visitedNode.getChilds().get(steps.get(steps.size()-1));
                steps.remove(steps.size()-1);
            }
        }else{
            return null;
        }

        return visitedNode;
    }
    /**
     * Recupera il nodo rappresentante l'azione più inclusiva permessa che include l'azione passata come parametro
     * @param a Azione di cui si vuole il permesso più inclusivo
     * @return NodeRule relativo all'azione più inclusiva permessa che include l'azione a; casi particolari: root
     * dell'albero, il nodo contenente a, null qualora a non fosse permessa.
     */
    public NodeRule getLargestPermission(Action a){
        ArrayList<Action> steps=new ArrayList<Action>();
        Action visited=a;
        NodeRule visitedNode=root;
        while(visited!=null){
            steps.add(visited);
            visited=visited.getIncludedIn();
        }
        if(visitedNode.getAction().getName().equals(steps.get(steps.size()-1).getName())){
            steps.remove(steps.size()-1);

            if(visitedNode.getState().equals("Prohibited"))
                return null;
            if(visitedNode.getState().equals("Permitted"))
                return visitedNode;
            while(!(visitedNode.getAction().getName().equals(a.getName()))){
                visitedNode=visitedNode.getChilds().get(steps.get(steps.size()-1));
                steps.remove(steps.size()-1);

                if(visitedNode.getState().equals("Prohibited"))
                    return null;
                if(visitedNode.getState().equals("Permitted"))
                    return visitedNode;
            }
        }else{
            return null;
        }

        return null;
    }
    /**
     * Setta l'azione desiderata come "Permitted"
     * @param a Azione di cui si vuole settera il permesso
     */
    public void setActionPermitted(Action a){
        NodeRule node=this.getActionNode(a);
        if(node!=null){
            node.setPermitted();
        }
    }
    /**
     * Setta l'azione desiderata come "Prohibited"
     * @param a Azione di cui si vuole settera il permesso
     */
    public void setActionProhibited(Action a){
        NodeRule node=this.getActionNode(a);
        if(node!=null){
            node.setProhibited();
        }
    }
    /**
     * Recupera il permesso relativo all'azione desiderata
     * @param a Azione di cui si vuole recuperare il permesso
     * @return Stringa rappresentante il permesso settato per l'azione
     */
    public String getActionState(Action a){
        ArrayList<Action> steps=new ArrayList<Action>();
        Action visited=a;
        NodeRule visitedNode=root;
        while(visited!=null){
            steps.add(visited);
            visited=visited.getIncludedIn();
        }
        if(visitedNode.getAction().getName().equals(steps.get(steps.size()-1).getName())){
            steps.remove(steps.size()-1);

            if(visitedNode.getState().equals("Prohibited") || visitedNode.getState().equals("Permitted"))
                return visitedNode.getState();
            while(!(visitedNode.getAction().getName().equals(a.getName()))){
                visitedNode=visitedNode.getChilds().get(steps.get(steps.size()-1));
                steps.remove(steps.size()-1);

                if(visitedNode.getState().equals("Prohibited") || visitedNode.getState().equals("Permitted"))
                    return visitedNode.getState();
            }
        }else{
            return null;
        }

        return visitedNode.getState();
    }
    /**
     * Recupera un dizionario contenente tutti i permessi, indicizzati per azione
     * @return ConcurrentHashMap<Action,String>, la chiave è l'azione di cui, l'elemento è la stringa che ne
     * rappresenta il permesso
     */
    public ConcurrentHashMap<Action,String> getAllStates(){
        ConcurrentHashMap<Action,String> res=new ConcurrentHashMap<>();
        HashMap<Action,String> tmp=new HashMap<>();
        for(Action a:Action.values()){
            tmp.put(a,this.getActionState(a));
        }
        for (Map.Entry<Action, String> entry : tmp.entrySet()) {
            if(entry.getValue()!=null) {
                res.put(entry.getKey(),entry.getValue());
            }
        }
        return res;
    }

}
