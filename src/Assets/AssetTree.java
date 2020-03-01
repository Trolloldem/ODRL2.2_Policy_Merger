package Assets;
import Policy.Policy;
import Policy.Set;


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
            if(node.getChildren()!=null)
                for(AssetCollection child: node.getChildren()){
                    unitePolicyChild(child,p);
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
            if(node.getChildren()!=null)
                for(AssetCollection child: node.getChildren()){
                    intersectPolicyChild(child,p);
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
            for(AssetCollection child: rootAsset.getChildren()){
                unitePolicyChild(child,p);
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
            for(AssetCollection child: rootAsset.getChildren()){
                intersectPolicyChild(child,p);
            }
        }
    }

}
