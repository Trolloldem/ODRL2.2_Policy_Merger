package Assets;

import Policy.Policy;
import Policy.Set;
import Rule.Rule;

import java.util.ArrayList;

public class Asset implements AssetCollection{

    private AssetCollection parent;
    private ArrayList<AssetCollection> children;
    private Policy policy;

    public Asset(){
        parent=null;
        children=new ArrayList<AssetCollection>();
        policy = null;
    }


    private void resetPolicy(){
        this.policy = null;
    }

    @Override
    public Policy getPolicy() {
        return policy;
    }

    @Override
    public AssetCollection getParent() {
        return parent;
    }

    @Override
    public ArrayList<AssetCollection>  getChildren() {
        if (children.size()>0)
            return children;
        else
            return null;

    }

    @Override
    public void setParent(AssetCollection parent) {
        if(this.parent==null){
            this.parent = parent;
            parent.addChild(this);
        }
    }

    @Override
    public void addChild(AssetCollection child) {
        if(!(this.children.contains(child))){
            this.children.add(child);
            child.setParent(this);
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
                ArrayList<Rule> resRules = thisPolicy.getRules();
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
                ArrayList<Rule> resRules = thisPolicy.getRules();
                res = new Set(resRules);
            }
            child.setPolicy(res);
            child.propagateUnion();
        }
    }
}
