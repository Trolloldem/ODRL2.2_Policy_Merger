package Assets;
import Actions.Action;
import Policy.Policy;
import Policy.Set;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;


/**
 * Classe wrapper: fornisce metodi relativi alla gestione delle policy di un albero di asset
 */
public class AssetTree {

    private AssetCollection rootAsset;

    /**
     * Costruttore di AssetTree
     * @param root: AssetCollection radice dell'albero
     * @return: AssetTree con radice il parameto root
     */
    public AssetTree(AssetCollection root){
        rootAsset=root;
    }


    private void setPolicyChild(AssetCollection node,Policy p, Boolean intersectChildren){

        if(p.getTarget().equals(node) && node.getPolicy()==null){
            node.setPolicy(p);
            if(intersectChildren){
                node.propagateIntersection();
            }else{
                node.propagateUnion();
            }
        }else{
            // non potendo controllare l'ordine di setPolicy, qualora un nodo FIGLIO fosse stato settato prima della sua Root,
            // si rifà il procedimento di settaggio

            if(p.getTarget().equals(node) && node.getPolicy()!=null ){

                if(intersectChildren){
                    Set res = (Set) node.getPolicy();
                    res = res.IntersectWith(p);
                    ((Asset) node).resetPolicy();
                    node.setPolicy(res);
                    node.propagateIntersection();
                }else{
                    Set res = (Set) node.getPolicy();
                    res = res.UniteWith(p);
                    ((Asset) node).resetPolicy();
                    node.setPolicy(res);
                    node.propagateUnion();
                }
            }
            if(node.getChildren()!=null)
                for(AssetCollection child: node.getChildren()){
                    setPolicyChild(child,p,intersectChildren);
                }
        }
    }
    /**
     * Propagazione di unitePolicy al nodo figlio specificato come parametro
     * @param p: Policy che si unisce, se non presenta un AssetCollection come target, il metodo non fa nulla
     * @param node: AssectCollection figlio sul quale si sta propagando l'unione della policy
     */
    private void unitePolicyChild(AssetCollection node,Policy p){
        if(p.getTarget().equals(node) && node.getPolicy()!=null) {

            Set res = (Set) node.getPolicy();
            res = res.UniteWith(p);
            ((Asset) node).resetPolicy();
            node.setPolicy(res);
            node.propagateUnion();
        }else{
            if(p.getTarget().equals(node) && node.getPolicy()==null){
                node.setPolicy(p);
                node.propagateUnion();
            }else {
                if (node.getChildren() != null)
                    for (AssetCollection child : node.getChildren()) {
                        unitePolicyChild(child, p);
                    }
            }
        }
    }
    /**
     * Propagazione di intersectPolicy al nodo figlio specificato come parametro
     * @param p: Policy che si unisce, se non presenta un AssetCollection come target, il metodo non fa nulla
     * @param node: AssectCollection figlio sul quale si sta propagando l'intersezione della policy
     */
    private void intersectPolicyChild(AssetCollection node,Policy p){
        if(p.getTarget().equals(node) && node.getPolicy()!=null) {

            Set res = (Set) node.getPolicy();
            res = res.IntersectWith(p);
            ((Asset) node).resetPolicy();
            node.setPolicy(res);
            node.propagateIntersection();
        }else{
            if(p.getTarget().equals(node) && node.getPolicy()==null){
                node.setPolicy(p);
                node.propagateIntersection();
            }else{
                if(node.getChildren()!=null)
                    for(AssetCollection child: node.getChildren()){
                        intersectPolicyChild(child,p);
                    }
            }
        }
    }
    /**
     * Setta per l'AssetCollection target la policy p, propagandola agli asset figli tramite unione o intersezione
     * @param p: Policy che viene settata, se non presenta un AssetCollection come target, il metodo non fa nulla
     * @param intersectChildren: se settato a true, il metodo propaga la policy ai figli intersecandola con la loro policy attuale;
     *                         se settato a false, la policy viene unita a quella dei figli
     */
    public  void setPolicy(Policy p,Boolean intersectChildren){
        if(p.getTarget().equals(rootAsset) && rootAsset.getPolicy()==null){
            rootAsset.setPolicy(p);
            if(intersectChildren){
                rootAsset.propagateIntersection();
            }else{
                rootAsset.propagateUnion();
            }
        }else{
            for(AssetCollection child: rootAsset.getChildren()){
                setPolicyChild(child,p,intersectChildren);
            }
        }
    }
    /**
     * Setta per l'AssetCollection target la policy p, propagandola agli asset figli tramite intersezione
     * @param p: Policy che viene settata, se non presenta un AssetCollection come target, il metodo non fa nulla
     *
     */
    public void setPolicy(Policy p){

        setPolicy(p,true);


    }
    /**
     * Unione della Policy attuale dell'asset target della Policy p con p. La propagazione avviene per unione
     * @param p: Policy che si unisce, se non presenta un AssetCollection come target, il metodo non fa nulla
     *
     */
    public void unitePolicy(Policy p){
        if(p.getTarget().equals(rootAsset) && rootAsset.getPolicy()!=null) {

            Set res = (Set) rootAsset.getPolicy();
            res = res.UniteWith(p);
            ((Asset) rootAsset).resetPolicy();
            rootAsset.setPolicy(res);
            rootAsset.propagateUnion();
        }else{
            if(p.getTarget().equals(rootAsset) && rootAsset.getPolicy()==null){
                rootAsset.setPolicy(p);

                rootAsset.propagateUnion();
            }else{
                for(AssetCollection child: rootAsset.getChildren()){
                    unitePolicyChild(child,p);
            }

            }
        }
    }
    /**
     * Unione della Policy attuale dell'asset target della Policy p con p. La propagazione avviene per unione
     * @param p: Policy che si unisce, se non presenta un AssetCollection come target, il metodo non fa nulla
     *
     */
    public void intersectPolicy(Policy p){

        if(p.getTarget().equals(rootAsset) && rootAsset.getPolicy()!=null) {

            Set res = (Set) rootAsset.getPolicy();
            res = res.IntersectWith(p);
            ((Asset) rootAsset).resetPolicy();
            rootAsset.setPolicy(res);
            rootAsset.propagateIntersection();
        }else{
            if(p.getTarget().equals(rootAsset) && rootAsset.getPolicy()==null){
                rootAsset.setPolicy(p);

                rootAsset.propagateIntersection();

            }else {
                for (AssetCollection child : rootAsset.getChildren()) {
                    intersectPolicyChild(child, p);
                }
            }
        }
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        String res = "";
        Policy actPolicy = rootAsset.getPolicy();
        res = res + "==============\n" + rootAsset.getURI() + "\n" + "==============\n";
        Queue<AssetCollection> queue = new LinkedList<AssetCollection>();
        if (actPolicy != null){
            for (Map.Entry<Action, String> entry : actPolicy.getUseTree().getAllStates().entrySet()) {
                if(entry.getValue()!="Undefined")
                res = res + entry.getKey() + " : " + entry.getValue() + "\n";
            }
        }else {
            res = res + "No policy defined\n";
        }

        for(AssetCollection node : rootAsset.getChildren()){
            queue.add(node);

        }

        while (!queue.isEmpty()){
            AssetCollection node = queue.poll();
            res = addNode(res,node);
            if(node.getChildren()!=null && node.getChildren().size()>0)
            for(AssetCollection child : node.getChildren()){
                queue.add(child);
            }

        }

        return res;
    }

    private String addNode(String res,AssetCollection node){
        Policy actPolicy = node.getPolicy();
        res = res + "==============\n"+node.getURI()+"\n"+"==============\n";
        if (actPolicy != null){
            for(Map.Entry<Action,String> entry : actPolicy.getUseTree().getAllStates().entrySet()){
                if(entry.getValue()!="Undefined")
                res = res + entry.getKey()+" : "+entry.getValue()+"\n";
            }
        }else{
            res = res + "No policy defined\n";
        }
        return res;
    }

    public Map<String,Policy> recoverAllPolicy() {
        Map<String,Policy> res = new HashMap<>();
        res.put(rootAsset.getURI(),rootAsset.getPolicy());

        Queue<AssetCollection> queue = new LinkedList<AssetCollection>();


        for(AssetCollection node : rootAsset.getChildren()){
            queue.add(node);

        }

        while (!queue.isEmpty()){
            AssetCollection node = queue.poll();
            addNode(res,node);
            if(node.getChildren()!=null && node.getChildren().size()>0)
                for(AssetCollection child : node.getChildren()){
                    queue.add(child);
                }

        }

        return res;
    }

    private void addNode(Map<String,Policy> res,AssetCollection node){

        res.put(node.getURI(),node.getPolicy());

    }
}
