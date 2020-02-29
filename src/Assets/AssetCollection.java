package Assets;

import Policy.Policy;

import java.util.ArrayList;

public interface AssetCollection {

    public Policy getPolicy();

    public AssetCollection getParent();

    public ArrayList<AssetCollection> getChildren();

    public void setParent(AssetCollection parent);

    public void addChild(AssetCollection child);

    public void setPolicy(Policy p);

    public void propagateIntersection();

    public void propagateUnion();

}
