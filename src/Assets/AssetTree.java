package Assets;

import Policy.Policy;

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

}
