package Assets;

import Actions.Action;
import Policy.Policy;
import Policy.Set;

import java.util.Map;

public class AssetTree {

    private AssetCollection rootAsset;

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

    public void setPolicy(Policy p){

        setPolicy(p,true);


    }

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
