package mergingProcedure;

import Assets.Asset;
import Assets.AssetCollection;
import Assets.AssetTree;
import Policy.Policy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class merger {

    /**
     * Precedura per intersezione di 2 alberi di Policy:
     * recupera tutte le policy di entrambi gli alberi, ne fa l'intersezione e le assegna in un nuovo albero
     * @param assets Map<String,Asset> relativa alla prima policy
     * @param assetsSecond Map<String,Asset>  relativa alla seconda policy
     * @param tree AssetTree relativo alla prima policy
     * @param treeSecond AssetTree relativo alla seconda policy
     * @return AssetTree relativo alla nuova policy
     */
    public static AssetTree intersection(Map<String,Asset> assets,Map<String,Asset> assetsSecond, AssetTree tree, AssetTree treeSecond){
        java.util.Set<String> commonURI = new HashSet<String>(assets.keySet());
        java.util.Set<String> uriSetSecond = assetsSecond.keySet();
        Map<String, AssetCollection> finalAssets = new HashMap<String, AssetCollection>();
        commonURI.retainAll(uriSetSecond);
        AssetCollection lastEvery = new Asset("EveryAsset");
        finalAssets.put("EveryAsset",lastEvery);
        for(String uri : commonURI){
            if(uri.equals("EveryAsset"))
                continue;
            if(!finalAssets.containsKey(uri))
                finalAssets.put(uri,new Asset(uri));

            AssetCollection parentFirst = assets.get(uri).getParent();
            AssetCollection parentSecond = assetsSecond.get(uri).getParent();

            if(!parentFirst.getURI().equals(parentSecond.getURI()) && (parentFirst.getURI().equals("EveryAsset") || parentSecond.getURI().equals("EveryAsset"))){
                AssetCollection actParent = parentFirst.getURI().equals("EveryAsset") ? new Asset(parentSecond.getURI()) : new Asset(parentFirst.getURI());

                if(finalAssets.containsKey(actParent.getURI())){
                    finalAssets.get(actParent.getURI()).addChild(finalAssets.get(uri));
                }else {
                    actParent.addChild(finalAssets.get(uri));
                    finalAssets.put(actParent.getURI(),actParent);
                }
            }
            if(parentFirst.getURI().equals(parentSecond.getURI())){
                if(finalAssets.containsKey(parentFirst.getURI())){
                    finalAssets.get(parentFirst.getURI()).addChild(finalAssets.get(uri));
                }else {
                    AssetCollection actParent = new Asset(parentFirst.getURI());
                    actParent.addChild(finalAssets.get(uri));
                    finalAssets.put(actParent.getURI(),actParent);
                }
            }
        }

        for(String uri: assets.keySet()){
            if(!commonURI.contains(uri)){
                if(!finalAssets.containsKey(uri))
                    finalAssets.put(uri,new Asset(uri));

                if(finalAssets.containsKey(assets.get(uri).getParent().getURI())){
                    finalAssets.get(assets.get(uri).getParent().getURI()).addChild(finalAssets.get(uri));
                }else {
                    AssetCollection actParent = new Asset(assets.get(uri).getParent().getURI());
                    actParent.addChild(finalAssets.get(uri));
                    finalAssets.put(actParent.getURI(),actParent);
                }
            }
        }

        for(String uri : uriSetSecond){
            if(!commonURI.contains(uri)){
                if(!finalAssets.containsKey(uri))
                    finalAssets.put(uri,new Asset(uri));

                if(finalAssets.containsKey(assetsSecond.get(uri).getParent().getURI())){
                    finalAssets.get(assetsSecond.get(uri).getParent().getURI()).addChild(finalAssets.get(uri));
                }else {
                    AssetCollection actParent = new Asset(assetsSecond.get(uri).getParent().getURI());
                    actParent.addChild(finalAssets.get(uri));
                    finalAssets.put(actParent.getURI(),actParent);
                }
            }
        }
        Map<String, Policy> policyFirstTree = tree.recoverAllPolicy();
        Map<String,Policy> policySecondTree = treeSecond.recoverAllPolicy();
        AssetTree finalTree = new AssetTree(lastEvery);

        for(Map.Entry<String,AssetCollection> asset: finalAssets.entrySet()){
            Policy firstPolicy = policyFirstTree.get(asset.getKey());
            Policy secondPolicy = policySecondTree.get(asset.getKey());

            if(firstPolicy != null && secondPolicy != null){
                Policy intersection = firstPolicy.IntersectWith(secondPolicy);
                intersection.setTarget(asset.getValue());
                finalTree.setPolicy(intersection,true);
            }else if(firstPolicy != null || secondPolicy != null ){
                Policy toSet = firstPolicy == null ? secondPolicy : firstPolicy;
                toSet.setTarget(asset.getValue());
                finalTree.setPolicy(toSet,true);
            }
        }
        return finalTree;
    }

    public static AssetTree union(Map<String,Asset> assets,Map<String,Asset> assetsSecond, AssetTree tree, AssetTree treeSecond){
        java.util.Set<String> commonURI = new HashSet<String>(assets.keySet());
        java.util.Set<String> uriSetSecond = assetsSecond.keySet();
        Map<String, AssetCollection> finalAssets = new HashMap<String, AssetCollection>();
        commonURI.retainAll(uriSetSecond);
        AssetCollection lastEvery = new Asset("EveryAsset");
        finalAssets.put("EveryAsset",lastEvery);
        for(String uri : commonURI){
            if(uri.equals("EveryAsset"))
                continue;
            if(!finalAssets.containsKey(uri))
                finalAssets.put(uri,new Asset(uri));

            AssetCollection parentFirst = assets.get(uri).getParent();
            AssetCollection parentSecond = assetsSecond.get(uri).getParent();

            if(!parentFirst.getURI().equals(parentSecond.getURI()) && (parentFirst.getURI().equals("EveryAsset") || parentSecond.getURI().equals("EveryAsset"))){
                AssetCollection actParent = parentFirst.getURI().equals("EveryAsset") ? new Asset(parentSecond.getURI()) : new Asset(parentFirst.getURI());

                if(finalAssets.containsKey(actParent.getURI())){
                    finalAssets.get(actParent.getURI()).addChild(finalAssets.get(uri));
                }else {
                    actParent.addChild(finalAssets.get(uri));
                    finalAssets.put(actParent.getURI(),actParent);
                }
            }
            if(parentFirst.getURI().equals(parentSecond.getURI())){
                if(finalAssets.containsKey(parentFirst.getURI())){
                    finalAssets.get(parentFirst.getURI()).addChild(finalAssets.get(uri));
                }else {
                    AssetCollection actParent = new Asset(parentFirst.getURI());
                    actParent.addChild(finalAssets.get(uri));
                    finalAssets.put(actParent.getURI(),actParent);
                }
            }
        }

        for(String uri: assets.keySet()){
            if(!commonURI.contains(uri)){
                if(!finalAssets.containsKey(uri))
                    finalAssets.put(uri,new Asset(uri));

                if(finalAssets.containsKey(assets.get(uri).getParent().getURI())){
                    finalAssets.get(assets.get(uri).getParent().getURI()).addChild(finalAssets.get(uri));
                }else {
                    AssetCollection actParent = new Asset(assets.get(uri).getParent().getURI());
                    actParent.addChild(finalAssets.get(uri));
                    finalAssets.put(actParent.getURI(),actParent);
                }
            }
        }

        for(String uri : uriSetSecond){
            if(!commonURI.contains(uri)){
                if(!finalAssets.containsKey(uri))
                    finalAssets.put(uri,new Asset(uri));

                if(finalAssets.containsKey(assetsSecond.get(uri).getParent().getURI())){
                    finalAssets.get(assetsSecond.get(uri).getParent().getURI()).addChild(finalAssets.get(uri));
                }else {
                    AssetCollection actParent = new Asset(assetsSecond.get(uri).getParent().getURI());
                    actParent.addChild(finalAssets.get(uri));
                    finalAssets.put(actParent.getURI(),actParent);
                }
            }
        }

        Map<String, Policy> policyFirstTree = tree.recoverAllPolicy();
        Map<String,Policy> policySecondTree = treeSecond.recoverAllPolicy();
        AssetTree finalTree = new AssetTree(lastEvery);

        for(Map.Entry<String,AssetCollection> asset: finalAssets.entrySet()){
            Policy firstPolicy = policyFirstTree.get(asset.getKey());
            Policy secondPolicy = policySecondTree.get(asset.getKey());

            if(firstPolicy != null && secondPolicy != null){
                Policy union = firstPolicy.UniteWith(secondPolicy);
                union.setTarget(asset.getValue());
                finalTree.setPolicy(union,false);
            }else if(firstPolicy != null || secondPolicy != null ){
                Policy toSet = firstPolicy == null ? secondPolicy : firstPolicy;
                toSet.setTarget(asset.getValue());
                finalTree.setPolicy(toSet,false);
            }
        }
        return finalTree;
    }
}
