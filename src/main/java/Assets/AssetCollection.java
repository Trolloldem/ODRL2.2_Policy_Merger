package Assets;

import Policy.Policy;

import java.util.ArrayList;
import java.util.Set;

public interface AssetCollection {

    /**
     * Riscrive la policy senza controllare che il nodo ne avesse una in precedenza
     * @param p Policy che sostituisce la policy attuale
     */
    public void OverwritePolicy(Policy p);
    /**
     * Getter dell' URI dell'asset
     * @return Stringa contenente l'URI dell'asset
     */
    public String getURI();



    /**
     * Setter dell' URI dell'asset
     * @param URI Stringa contenente l'URI dell'asset
*/
    public void setURI(String URI);


    /**
     * Getter della Policy assegnata all'asset
     * @return Policy che ha come target l'AssetCollection
     */
    public Policy getPolicy();

    /**
     * Getter del padre dell'asset
     * @return AssetCollection padre dell'AssetCollection su cui si chiama il metodo
     */
    public ArrayList<AssetCollection> getParents();
    /**
     * Getter dei figli dell'asset
     * @return ArrayList di AssetCollection contenente i figli dell'AssetCollection di cui si chiama il metodo
     */
    public ArrayList<AssetCollection> getChildren();

    /**
     * Setter del padre dell'asset
     * @param parents: AssetCollection che si vuole settare come padre
     */
    public void addParents(ArrayList<AssetCollection> parents);

    /**
     * Setter del padre dell'asset
     * @param parent: AssetCollection che si vuole settare come padre
     */
    public void addParent(AssetCollection parent);

    public void addParent(AssetCollection parent,boolean secondary);

    /**
     * Aggiunge un figlio alla lista dei figli dell'asset
     * @param child: AssetCollection che si vuole aggiungere come figlio
     */
    public void addChild(AssetCollection child);

    public void addChild(AssetCollection parent,boolean secondary);

    /**
     * Assegna una policy all'asset
     * @param p: Policy che si vuole assegnare all'asset
     */
    public void setPolicy(Policy p);
    /**
     * Propaga la policy dell'AssetCollection agli asset figli, intersecando le policy se questo non ne ha già una
     */
    public void propagateIntersection();
    /**
     * Propaga la policy dell'AssetCollection agli asset figli, unendo le policy
     */
    public void propagateUnion();

    public Set<String> getParentURIs();

}
