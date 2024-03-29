package Assets;

import Policy.Policy;
import Policy.Set;
import Rule.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe relativa alla gestione di un asset singolo
 */
public class Asset implements AssetCollection {
    private String URI;
    private ArrayList<AssetCollection> parents;
    private ArrayList<AssetCollection> children;
    private Policy policy;

    public Asset(){
        URI = null;
        parents=new ArrayList<AssetCollection>();
        children=new ArrayList<AssetCollection>();
        policy = null;
    }
    public Asset(String URI){
        this.URI = URI;
        parents=new ArrayList<AssetCollection>();
        children=new ArrayList<AssetCollection>();
        policy = null;
    }

    /**
     * Resetta a null la policy dell'Asset
     */
    protected void resetPolicy(){
        this.policy = null;
    }

    /**
     * Riscrive la policy senza controllare che il nodo ne avesse una in precedenza
     *
     * @param p Policy che sostituisce la policy attuale
     */
    @Override
    public void OverwritePolicy(Policy p) {
        this.policy = p;
    }

    /**
     * Getter dell' URI dell'asset
     *
     * @return Stringa contenente l'URI dell'asset
     */
    @Override
    public String getURI() {
        return URI;
    }

    /**
     * Setter dell' URI dell'asset
     *
     * @param URI Stringa contenente l'URI dell'asset
     */
    @Override
    public void setURI(String URI) {
    this.URI = URI;
    }

    @Override
    public Policy getPolicy() {
        return policy;
    }

    @Override
    public ArrayList<AssetCollection> getParents() {
        return parents;
    }

    @Override
    public ArrayList<AssetCollection>  getChildren() {
        if (children.size()>0)
            return children;
        else
            return null;

    }

    @Override
    public void addParents(ArrayList<AssetCollection> parent) {

            this.parents = parent;
            parent.stream().forEach(parentNode -> parentNode.addChild(this,true));

    }

    @Override
    public void addParent(AssetCollection parent) {
        if(!this.parents.contains(parent))
            this.parents.add(parent);
        else{
            for(int i=0; i<this.parents.size(); i++){
                AssetCollection node = this.parents.get(i);
                if(node.equals(parent)){
                    this.parents.set(i, parent);
                    break;
                }
            }
        }
        parent.addChild(this,true);
    }

    @Override
    public void addParent(AssetCollection parent,boolean secondary) {
        if(!this.parents.contains(parent))
            this.parents.add(parent);
        else{
            for(int i=0; i<this.parents.size(); i++){
                AssetCollection node = this.parents.get(i);
                if(node.equals(parent)){
                    this.parents.set(i, parent);
                    break;
                }
            }
        }

    }




    @Override
    public void addChild(AssetCollection child) {
        if(!this.children.contains(child))
            this.children.add(child);
        else{
            for(int i=0; i<this.children.size(); i++){
                AssetCollection node = this.children.get(i);
                if(node.equals(child)){
                    this.children.set(i, child);
                    break;
                }
            }
        }

        child.addParent(this,true);
    }

    @Override
    public void addChild(AssetCollection child,boolean secondary) {
        if(!this.children.contains(child))
            this.children.add(child);
        else{
            for(int i=0; i<this.children.size(); i++){
                AssetCollection node = this.children.get(i);
                if(node.equals(child)){
                    this.children.set(i, child);
                    break;
                }
            }
        }
    }

    @Override
    public void setPolicy(Policy p) {
        if(this.policy==null)
            this.policy=p;
    }



    @Override
    public void propagateIntersection() {
        Policy thisPolicy = this.policy;
        for(AssetCollection child: this.children){

            Set res=null;
            Policy childPolicy = child.getPolicy();
            if(childPolicy!=null){
                res = (Set)thisPolicy.IntersectWith(childPolicy);
                ((Asset)child).resetPolicy();

            }else{
                List<Rule> resRules = thisPolicy.getRules();
                res = new Set(resRules);
            }
            child.setPolicy(res);
            child.propagateIntersection();
        }

    }

    @Override
    public void propagateUnion() {
        Policy thisPolicy = this.policy;
        for(AssetCollection child: this.children){

            Set res=null;
            Policy childPolicy = child.getPolicy();
            if(childPolicy!=null){
                res = (Set) thisPolicy.UniteWith(childPolicy);
                ((Asset) child).resetPolicy();
            }
            else{
                List<Rule> resRules = thisPolicy.getRules();
                res = new Set(resRules);
            }
            child.setPolicy(res);
            child.propagateUnion();
        }
    }

    @Override
    public java.util.Set<String> getParentURIs() {
        return this.parents.stream().map(asset -> asset.getURI()).collect(Collectors.toSet());
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The {@code equals} method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     *     {@code x}, {@code x.equals(x)} should return
     *     {@code true}.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     *     {@code x} and {@code y}, {@code x.equals(y)}
     *     should return {@code true} if and only if
     *     {@code y.equals(x)} returns {@code true}.
     * <li>It is <i>transitive</i>: for any non-null reference values
     *     {@code x}, {@code y}, and {@code z}, if
     *     {@code x.equals(y)} returns {@code true} and
     *     {@code y.equals(z)} returns {@code true}, then
     *     {@code x.equals(z)} should return {@code true}.
     * <li>It is <i>consistent</i>: for any non-null reference values
     *     {@code x} and {@code y}, multiple invocations of
     *     {@code x.equals(y)} consistently return {@code true}
     *     or consistently return {@code false}, provided no
     *     information used in {@code equals} comparisons on the
     *     objects is modified.
     * <li>For any non-null reference value {@code x},
     *     {@code x.equals(null)} should return {@code false}.
     * </ul>
     * <p>
     * The {@code equals} method for class {@code Object} implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values {@code x} and
     * {@code y}, this method returns {@code true} if and only
     * if {@code x} and {@code y} refer to the same object
     * ({@code x == y} has the value {@code true}).
     * <p>
     * Note that it is generally necessary to override the {@code hashCode}
     * method whenever this method is overridden, so as to maintain the
     * general contract for the {@code hashCode} method, which states
     * that equal objects must have equal hash codes.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @see #hashCode()
     * @see HashMap
     */
    @Override
    public boolean equals(Object obj) {

        if(obj instanceof Asset){
            Asset other = (Asset)obj;

            if(this.policy!=null && other.policy!=null)
                if(!this.policy.equals(other.policy))
               return false;
            if (this.URI!= null && other.URI!=null)
                if (!this.URI.equals(other.URI))
                return false;

            return true;
        }
        return false;
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables such as those provided by
     * {@link HashMap}.
     * <p>
     * The general contract of {@code hashCode} is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during
     *     an execution of a Java application, the {@code hashCode} method
     *     must consistently return the same integer, provided no information
     *     used in {@code equals} comparisons on the object is modified.
     *     This integer need not remain consistent from one execution of an
     *     application to another execution of the same application.
     * <li>If two objects are equal according to the {@code equals(Object)}
     *     method, then calling the {@code hashCode} method on each of
     *     the two objects must produce the same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal
     *     according to the {@link Object#equals(Object)}
     *     method, then calling the {@code hashCode} method on each of the
     *     two objects must produce distinct integer results.  However, the
     *     programmer should be aware that producing distinct integer results
     *     for unequal objects may improve the performance of hash tables.
     * </ul>
     * <p>
     * As much as is reasonably practical, the hashCode method defined by
     * class {@code Object} does return distinct integers for distinct
     * objects. (This is typically implemented by converting the internal
     * address of the object into an integer, but this implementation
     * technique is not required by the
     * Java&trade; programming language.)
     *
     * @return a hash code value for this object.
     * @see Object#equals(Object)
     * @see System#identityHashCode
     */
    @Override
    public int hashCode() {
        int result=0;

        if(this.policy!=null)
        result = result +this.policy.hashCode();
        if (this.URI!= null)
        result = result + this.URI.hashCode();
        return result;
    }
}
