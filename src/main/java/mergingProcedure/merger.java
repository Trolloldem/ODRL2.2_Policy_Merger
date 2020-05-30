package mergingProcedure;

import Assets.Asset;
import Assets.AssetCollection;
import Assets.AssetTree;
import Policy.Policy;
import org.apache.jena.atlas.lib.Pair;

import java.util.*;
import java.util.stream.Collectors;

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
    public static Pair<AssetTree, Map<String, AssetCollection>> intersection(Map<String,Asset> assets, Map<String,Asset> assetsSecond, AssetTree tree, AssetTree treeSecond){
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

            Set<String> parentFirst = assets.get(uri).getParentURIs();
            Set<String> parentSecond = assetsSecond.get(uri).getParentURIs();

            if(!parentFirst.equals(parentSecond) && (parentFirst.contains("EveryAsset") || parentSecond.contains("EveryAsset"))) {
                ArrayList<AssetCollection> toCheck = parentFirst.contains("EveryAsset") ? assetsSecond.get(uri).getParents() : assets.get(uri).getParents();
                for(AssetCollection actParent : toCheck){

                    if (finalAssets.containsKey(actParent.getURI())) {
                        finalAssets.get(actParent.getURI()).addChild(finalAssets.get(uri));
                    } else {


                        actParent.addChild(finalAssets.get(uri));
                        finalAssets.put(actParent.getURI(), actParent);
                    }
                }
                finalAssets.get(uri).addParents(toCheck);
            }
            if(parentFirst.equals(parentSecond)) {
                for (String parentURI : parentFirst){
                    if (finalAssets.containsKey(parentURI)) {
                        finalAssets.get(parentURI).addChild(finalAssets.get(uri));
                    } else {
                        AssetCollection actParent = new Asset(parentURI);
                        actParent.addChild(finalAssets.get(uri));
                        finalAssets.put(actParent.getURI(), actParent);
                    }
                }
            }
            if(!parentFirst.equals(parentSecond) && !parentFirst.contains("EveryAsset") && !parentSecond.contains("EveryAsset")){
                Set<String> totalParents = new HashSet<>(parentFirst);
                totalParents.addAll(parentSecond);
                for (String parentURI : totalParents){
                    if (finalAssets.containsKey(parentURI)) {
                        finalAssets.get(parentURI).addChild(finalAssets.get(uri));
                    } else {
                        AssetCollection actParent = new Asset(parentURI);
                        actParent.addChild(finalAssets.get(uri));
                        finalAssets.put(actParent.getURI(), actParent);
                    }
                }
            }

        }



        for(String uri: assets.keySet()){
            if(!commonURI.contains(uri)) {
                if (!finalAssets.containsKey(uri))
                    finalAssets.put(uri, new Asset(uri));
                for(String parentURI : assets.get(uri).getParentURIs()){
                    if (finalAssets.containsKey(parentURI)) {
                        finalAssets.get(parentURI).addChild(finalAssets.get(uri));
                    } else {
                        AssetCollection actParent = new Asset(parentURI);
                        actParent.addChild(finalAssets.get(uri));
                        finalAssets.put(actParent.getURI(), actParent);
                    }
                }
            }
        }

        for(String uri : uriSetSecond){
            if(!commonURI.contains(uri)) {
                if (!finalAssets.containsKey(uri))
                    finalAssets.put(uri, new Asset(uri));
                for(String parentURI : assetsSecond.get(uri).getParentURIs()){
                    if (finalAssets.containsKey(parentURI)) {
                        finalAssets.get(parentURI).addChild(finalAssets.get(uri));
                    } else {
                        AssetCollection actParent = new Asset(parentURI);
                        actParent.addChild(finalAssets.get(uri));
                        finalAssets.put(actParent.getURI(), actParent);
                    }
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


        return new Pair<AssetTree,Map<String, AssetCollection>> (finalTree,finalAssets);
    }

    public static Pair<AssetTree, Map<String, AssetCollection>> union(Map<String,Asset> assets, Map<String,Asset> assetsSecond, AssetTree tree, AssetTree treeSecond){
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

            Set<String> parentFirst = assets.get(uri).getParentURIs();
            Set<String> parentSecond = assetsSecond.get(uri).getParentURIs();

            if(!parentFirst.equals(parentSecond) && (parentFirst.contains("EveryAsset") || parentSecond.contains("EveryAsset"))) {
                ArrayList<AssetCollection> toCheck = parentFirst.contains("EveryAsset") ? assetsSecond.get(uri).getParents() : assets.get(uri).getParents();

                for(AssetCollection actParent : toCheck){
                    if (finalAssets.containsKey(actParent.getURI())) {
                        finalAssets.get(actParent.getURI()).addChild(finalAssets.get(uri));
                    } else {
                        actParent.addChild(finalAssets.get(uri));
                        finalAssets.put(actParent.getURI(), actParent);
                    }
                }
                finalAssets.get(uri).addParents(toCheck);
            }
            if(parentFirst.equals(parentSecond)) {
                for (String parentURI : parentFirst){
                    if (finalAssets.containsKey(parentURI)) {
                        finalAssets.get(parentURI).addChild(finalAssets.get(uri));
                    } else {
                        AssetCollection actParent = new Asset(parentURI);
                        actParent.addChild(finalAssets.get(uri));
                        finalAssets.put(actParent.getURI(), actParent);
                    }
                }
            }
            if(!parentFirst.equals(parentSecond) && !parentFirst.contains("EveryAsset") && !parentSecond.contains("EveryAsset")){
                Set<String> totalParents = new HashSet<>(parentFirst);
                totalParents.addAll(parentSecond);
                for (String parentURI : totalParents){
                    if (finalAssets.containsKey(parentURI)) {
                        finalAssets.get(parentURI).addChild(finalAssets.get(uri));
                    } else {
                        AssetCollection actParent = new Asset(parentURI);
                        actParent.addChild(finalAssets.get(uri));
                        finalAssets.put(actParent.getURI(), actParent);
                    }
                }
            }
        }

        for(String uri: assets.keySet()){
            if(!commonURI.contains(uri)) {
                if (!finalAssets.containsKey(uri))
                    finalAssets.put(uri, new Asset(uri));
                for(String parentURI : assets.get(uri).getParentURIs()){
                    if (finalAssets.containsKey(parentURI)) {
                        finalAssets.get(parentURI).addChild(finalAssets.get(uri));
                    } else {
                        AssetCollection actParent = new Asset(parentURI);
                        actParent.addChild(finalAssets.get(uri));
                        finalAssets.put(actParent.getURI(), actParent);
                    }
                }
            }
        }

        for(String uri : uriSetSecond){
            if(!commonURI.contains(uri)) {
                if (!finalAssets.containsKey(uri))
                    finalAssets.put(uri, new Asset(uri));
                for(String parentURI : assetsSecond.get(uri).getParentURIs()){
                    if (finalAssets.containsKey(parentURI)) {
                        finalAssets.get(parentURI).addChild(finalAssets.get(uri));
                    } else {
                        AssetCollection actParent = new Asset(parentURI);
                        actParent.addChild(finalAssets.get(uri));
                        finalAssets.put(actParent.getURI(), actParent);
                    }
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
        return new Pair<AssetTree,Map<String, AssetCollection>> (finalTree,finalAssets);
    }


    public static Pair<AssetTree, Map<String, AssetCollection>> intersectionOnCommon(Map<String,Asset> assets, Map<String,Asset> assetsSecond, AssetTree tree, AssetTree treeSecond){
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

            Set<String> parentFirst = assets.get(uri).getParentURIs();
            Set<String> parentSecond = assetsSecond.get(uri).getParentURIs();

            if(!parentFirst.equals(parentSecond) && (parentFirst.contains("EveryAsset") || parentSecond.contains("EveryAsset"))) {
                ArrayList<AssetCollection> toCheck = parentFirst.contains("EveryAsset") ? assetsSecond.get(uri).getParents() : assets.get(uri).getParents();


                for(AssetCollection actParent : toCheck){
                    if (finalAssets.containsKey(actParent.getURI())) {
                        finalAssets.get(actParent.getURI()).addChild(finalAssets.get(uri));
                    } else {
                        actParent.addChild(finalAssets.get(uri));
                        finalAssets.put(actParent.getURI(), actParent);
                    }
                }
                finalAssets.get(uri).addParents(toCheck);
            }
            if(parentFirst.equals(parentSecond)) {
                for (String parentURI : parentFirst){
                    if (finalAssets.containsKey(parentURI)) {
                        finalAssets.get(parentURI).addChild(finalAssets.get(uri));
                    } else {
                        AssetCollection actParent = new Asset(parentURI);
                        actParent.addChild(finalAssets.get(uri));
                        finalAssets.put(actParent.getURI(), actParent);
                    }
                }
            }
            if(!parentFirst.equals(parentSecond) && !parentFirst.contains("EveryAsset") && !parentSecond.contains("EveryAsset")){
                Set<String> totalParents = new HashSet<>(parentFirst);
                totalParents.addAll(parentSecond);
                for (String parentURI : totalParents){
                    if (finalAssets.containsKey(parentURI)) {
                        finalAssets.get(parentURI).addChild(finalAssets.get(uri));
                    } else {
                        AssetCollection actParent = new Asset(parentURI);
                        actParent.addChild(finalAssets.get(uri));
                        finalAssets.put(actParent.getURI(), actParent);
                    }
                }
            }
        }

        for(String uri: assets.keySet()){
            if(!commonURI.contains(uri)) {
                if (!finalAssets.containsKey(uri))
                    finalAssets.put(uri, new Asset(uri));
                for(String parentURI : assets.get(uri).getParentURIs()){
                    if (finalAssets.containsKey(parentURI)) {
                        finalAssets.get(parentURI).addChild(finalAssets.get(uri));
                    } else {
                        AssetCollection actParent = new Asset(parentURI);
                        actParent.addChild(finalAssets.get(uri));
                        finalAssets.put(actParent.getURI(), actParent);
                    }
                }
            }
        }

        for(String uri : uriSetSecond){
            if(!commonURI.contains(uri)) {
                if (!finalAssets.containsKey(uri))
                    finalAssets.put(uri, new Asset(uri));
                for(String parentURI : assetsSecond.get(uri).getParentURIs()){
                    if (finalAssets.containsKey(parentURI)) {
                        finalAssets.get(parentURI).addChild(finalAssets.get(uri));
                    } else {
                        AssetCollection actParent = new Asset(parentURI);
                        actParent.addChild(finalAssets.get(uri));
                        finalAssets.put(actParent.getURI(), actParent);
                    }
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

        Set<String> modifiedUri = getModifiedURIs(commonURI,policyFirstTree,policySecondTree,finalAssets);
        for(String res : finalAssets.keySet()){
            Policy toSet = null;
            if(! modifiedUri.contains(res) && !res.equals("EveryAsset")) {
                toSet = policyFirstTree.get(res) == null ? policySecondTree.get(res) : policyFirstTree.get(res);
                if (toSet != null) {
                    finalTree.setPolicyWithoutPropagation(toSet);

                }
            }
        }


        return new Pair<AssetTree,Map<String, AssetCollection>> (finalTree,finalAssets);
    }

    public static Pair<AssetTree, Map<String, AssetCollection>> unionOnCommon(Map<String,Asset> assets, Map<String,Asset> assetsSecond, AssetTree tree, AssetTree treeSecond){
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

            Set<String> parentFirst = assets.get(uri).getParentURIs();
            Set<String> parentSecond = assetsSecond.get(uri).getParentURIs();

            if(!parentFirst.equals(parentSecond) && (parentFirst.contains("EveryAsset") || parentSecond.contains("EveryAsset"))) {
                ArrayList<AssetCollection> toCheck = parentFirst.contains("EveryAsset") ? assetsSecond.get(uri).getParents() : assets.get(uri).getParents();

                for(AssetCollection actParent : toCheck){
                    if (finalAssets.containsKey(actParent.getURI())) {
                        finalAssets.get(actParent.getURI()).addChild(finalAssets.get(uri));
                    } else {
                        actParent.addChild(finalAssets.get(uri));
                        finalAssets.put(actParent.getURI(), actParent);
                    }
                }
                finalAssets.get(uri).addParents(toCheck);
            }
            if(parentFirst.equals(parentSecond)) {
                for (String parentURI : parentFirst){
                    if (finalAssets.containsKey(parentURI)) {
                        finalAssets.get(parentURI).addChild(finalAssets.get(uri));
                    } else {
                        AssetCollection actParent = new Asset(parentURI);
                        actParent.addChild(finalAssets.get(uri));
                        finalAssets.put(actParent.getURI(), actParent);
                    }
                }
            }
            if(!parentFirst.equals(parentSecond) && !parentFirst.contains("EveryAsset") && !parentSecond.contains("EveryAsset")){
                Set<String> totalParents = new HashSet<>(parentFirst);
                totalParents.addAll(parentSecond);
                for (String parentURI : totalParents){
                    if (finalAssets.containsKey(parentURI)) {
                        finalAssets.get(parentURI).addChild(finalAssets.get(uri));
                    } else {
                        AssetCollection actParent = new Asset(parentURI);
                        actParent.addChild(finalAssets.get(uri));
                        finalAssets.put(actParent.getURI(), actParent);
                    }
                }
            }
        }

        for(String uri: assets.keySet()){
            if(!commonURI.contains(uri)) {
                if (!finalAssets.containsKey(uri))
                    finalAssets.put(uri, new Asset(uri));
                for(String parentURI : assets.get(uri).getParentURIs()){
                    if (finalAssets.containsKey(parentURI)) {
                        finalAssets.get(parentURI).addChild(finalAssets.get(uri));
                    } else {
                        AssetCollection actParent = new Asset(parentURI);
                        actParent.addChild(finalAssets.get(uri));
                        finalAssets.put(actParent.getURI(), actParent);
                    }
                }
            }
        }

        for(String uri : uriSetSecond){
            if(!commonURI.contains(uri)) {
                if (!finalAssets.containsKey(uri))
                    finalAssets.put(uri, new Asset(uri));
                for(String parentURI : assetsSecond.get(uri).getParentURIs()){
                    if (finalAssets.containsKey(parentURI)) {
                        finalAssets.get(parentURI).addChild(finalAssets.get(uri));
                    } else {
                        AssetCollection actParent = new Asset(parentURI);
                        actParent.addChild(finalAssets.get(uri));
                        finalAssets.put(actParent.getURI(), actParent);
                    }
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

        Set<String> modifiedUri = getModifiedURIs(commonURI,policyFirstTree,policySecondTree,finalAssets);
        for(String res : finalAssets.keySet()){
            Policy toSet = null;
            if(! modifiedUri.contains(res) && !res.equals("EveryAsset")) {
                toSet = policyFirstTree.get(res) == null ? policySecondTree.get(res) : policyFirstTree.get(res);
                if (toSet != null) {
                    finalTree.setPolicyWithoutPropagation(toSet);

                }
            }
        }

        return new Pair<AssetTree,Map<String, AssetCollection>> (finalTree,finalAssets);
    }


    private static Set<String> getModifiedURIs(Set<String> commonURI, Map<String, Policy> policyFirstTree, Map<String, Policy> policySecondTree, Map<String, AssetCollection> finalAssets){

        Set<String> modified = new HashSet<String >();
        Queue<String> children = new LinkedList<>();
        for(String uri : commonURI) {
            if (!modified.contains(uri)){
                boolean mod = policyFirstTree.get(uri) != null && policySecondTree.get(uri) != null ? true : false;
                if (mod) {
                    modified.add(uri);
                    if(finalAssets.get(uri).getChildren() != null)
                        children.addAll(finalAssets.get(uri).getChildren().stream().map(asset -> asset.getURI()).collect(Collectors.toList()));
                    while (!children.isEmpty()) {
                        String internal = children.poll();
                        modified.add(internal);
                        if(finalAssets.get(internal).getChildren() != null)
                            children.addAll(finalAssets.get(internal).getChildren().stream().map(asset -> asset.getURI()).collect(Collectors.toList()));


                    }
                }
            }
        }

        return modified;
    }
}
