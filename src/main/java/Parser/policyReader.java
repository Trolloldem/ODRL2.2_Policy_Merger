package Parser;


import Actions.Action;
import Assets.Asset;
import Assets.AssetCollection;
import Rule.Rule;
import Rule.Permission;
import Rule.Prohibition;
import javafx.util.Pair;
import org.apache.jena.base.Sys;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDFS;

import java.util.*;

public class policyReader {


    /**
     * Meteodo per il recupero delle regole interne alla singola policy, attualmente i file ODRL contengono una policy alla volta
     * @param policyRes risultato relativo alla query che ha estratto la policy di interesse
     * @param model modello del documento ODRL necessario per query SPARQL
     * @param mappaPolicyRules mappa che associa ad ogni Policy le regole che questa policy contiene
     * @param mappaRulesParams mappa che associa ad ogni regola i propri parametri necessari per creare un oggetto Rule
     */
    private static  void manageMaps(Resource policyRes, Model model, Map<Resource,Set<Pair<Resource,String>>> mappaPolicyRules, Map<Resource,List<Pair<String,String>>> mappaRulesParams) {

        ParameterizedSparqlString queryRulesString = new ParameterizedSparqlString();
        queryRulesString.setCommandText("SELECT ?rule ?pred ?ogg ?type WHERE {" +
                "?policy ?type ?rule ." +
                " ?rule ?pred ?ogg " +
                "FILTER (?type IN (<"+ODRL_vocab.permission+">, <"+ODRL_vocab.prohibition+"> ))}");
        queryRulesString.setIri("policy",policyRes.getURI());
        Query queryRules = QueryFactory.create(queryRulesString.toString());
        QueryExecution queryExe2 = QueryExecutionFactory.create(queryRules,model);
        ResultSet rules = queryExe2.execSelect();

        while(rules.hasNext()){
            QuerySolution rule = rules.next();
            Resource ruleRes = rule.getResource("rule");
            Resource type = rule.getResource("type");

            if(mappaPolicyRules.containsKey(policyRes)){

                Set<Pair<Resource,String>> listaResPolicty = mappaPolicyRules.get(policyRes);
                listaResPolicty.add(new Pair<>(ruleRes, type.toString()));

            }else{

                Set<Pair<Resource,String>>  listaResPolicy = new HashSet<>();
                listaResPolicy.add(new Pair<>(ruleRes, type.toString()));
                mappaPolicyRules.put(policyRes,listaResPolicy);

            }

            if(mappaRulesParams.containsKey(ruleRes)){

                List<Pair<String,String>> listaProp = mappaRulesParams.get(ruleRes);
                listaProp.add(new Pair<>(rule.getResource("pred").toString(), rule.getResource("ogg").toString()));

            }else{

                List<Pair<String,String>> listaProp = new LinkedList<>();
                listaProp.add(new Pair<>(rule.getResource("rule").toString(),type.toString()));
                listaProp.add(new Pair<>(rule.getResource("pred").toString(), rule.getResource("ogg").toString()));
                mappaRulesParams.put(ruleRes, listaProp);
            }


        }

    }

    /**
     * Recupera dal documento ODRL una mappa che collega i vari asset alle regole che li interessano
     * @param path, stringa contenente il path del file da parsare
     * @return Mappa che collega gli asset alla lista di regole che li interessano
     */
    public static Map<AssetCollection,List<Rule>>  readPolicyRules(String path){

        Map<Resource,Set<Pair<Resource,String>>> mappaPolicyRules = new HashMap<Resource,Set<Pair<Resource,String>>>();
        Map<Resource,List<Pair<String,String>>> mappaRulesParams = new HashMap<Resource,List<Pair<String,String>>>();

        Model model = ModelFactory.createDefaultModel() ;
        model.read(path) ;

        String queryPolicyString = "SELECT *  WHERE {" +
                                   "?policy <"+ODRL_vocab.type+"> <"+ODRL_vocab.Policy+"> .}";
        Query queryPolicy = QueryFactory.create(queryPolicyString);
        QueryExecution queryExe = QueryExecutionFactory.create(queryPolicy,model);

        try {

            ResultSet policies = queryExe.execSelect();

            while(policies.hasNext()){

                QuerySolution policy = policies.next();
                Resource policyRes = policy.getResource("policy");



                manageMaps(policyRes, model, mappaPolicyRules, mappaRulesParams);


            }
        }catch (Exception e){
            System.err.println(e.getMessage());
            return null;
        }

        Map<AssetCollection,List<Rule>> mapAssetRuleList = new HashMap<>();
        mapAssetRuleList.put(new Asset("EveryAsset"),new ArrayList<Rule>());

        // Ho mappa che collega il documento di policy a tutte le regole che contiene
        // Ho mappa che collega la sigola rule ai suoi parametri
        Iterator<Map.Entry<Resource, List<Pair<String, String>>>> itRule = mappaRulesParams.entrySet().iterator();
        while(itRule.hasNext() ){

            Map.Entry<Resource, List<Pair<String, String>>> singleRule = itRule.next();
            List<Pair<String,String>> propertyList = singleRule.getValue();

            //Check se la regola è una PERMISSION o UNA PROHIBITION
            AssetCollection collection = new Asset("EveryAsset");
            String assignee = "EveryOne";
            Action action = null;
            String type = "";

            for(Pair<String,String> actProperty : propertyList){

                if(actProperty.getValue().equals(ODRL_vocab.permission.toString())){
                    type = type + ODRL_vocab.permission;


                }
                if(actProperty.getValue().equals(ODRL_vocab.prohibition.toString())){
                    type = type + ODRL_vocab.prohibition;

                }
                if(actProperty.getKey().equals(ODRL_vocab.target.toString())){
                    collection = new Asset(actProperty.getValue());
                }
                if(actProperty.getKey().equals(ODRL_vocab.action.toString())){

                    String[] URITokens = actProperty.getValue().split("/");
                    String enumName = URITokens[URITokens.length-1].toUpperCase();
                    action = Action.valueOf(enumName);
                }
                if(actProperty.getKey().equals(ODRL_vocab.assignee)){
                    assignee = actProperty.getValue();
                }
            }

            if(type.equals("") || action==null){
                throw new RuntimeException("Rules must have a TYPE and an ACTION specified");
            }
            Rule rule;
            if(type.equals(ODRL_vocab.permission.toString())){
                rule = new Permission(action);
            }else {
                rule = new Prohibition(action);
            }
            if(mapAssetRuleList.containsKey(collection)){
                mapAssetRuleList.get(collection).add(rule);
            }else{
                List<Rule> assetRuleList = new ArrayList<Rule>();
                assetRuleList.add(rule);
                mapAssetRuleList.put(collection,assetRuleList);
            }

            collection = new Asset("EveryAsset");
            assignee = "EveryOne";
            action = null;
            type = "";
        }


        return mapAssetRuleList;


    }

    /**
     * Recupera del documento ODRL una mappa che collega l'URI ad un Asset, l'asset contiene già gli il Parent ed i Children
     * @param @param path, stringa contenente il path del file da parsare
     * @return Mappa che collega l'URI ad un Asset
     */
    public static Map<String, Asset> readAssets(String path){
        Map<String,Asset> assets = new HashMap<String,Asset>();
        Model model = ModelFactory.createDefaultModel() ;
        model.read(path) ;

        String queryAssetsString = "SELECT DISTINCT ?obj  WHERE {" +
                "?sub ?pred ?obj " +
                "FILTER (?pred IN (<"+ODRL_vocab.target+">, <"+ODRL_vocab.partOf+"> )) }";
        Query queryAssets = QueryFactory.create(queryAssetsString);
        QueryExecution queryExe = QueryExecutionFactory.create(queryAssets,model);

        try {

            ResultSet assetSet = queryExe.execSelect();

            while(assetSet.hasNext()){

                QuerySolution asset = assetSet.next();
                Resource assetRes = asset.getResource("obj");
                assets.put(assetRes.getURI(),new Asset(assetRes.getURI()));
            }
        }catch (Exception e){
            System.err.println(e.getMessage());

        }
        for(String uri : assets.keySet()){
            String teamplateQueryString = "SELECT DISTINCT ?sub  WHERE {"+
                    "?sub <"+ODRL_vocab.partOf+"> ?obj}";
            ParameterizedSparqlString queryHierarchyString = new ParameterizedSparqlString(teamplateQueryString);
            queryHierarchyString.setIri("obj",uri);
            Query queryHierarchy = QueryFactory.create(queryHierarchyString.toString());
            QueryExecution queryExeHierarchy = QueryExecutionFactory.create(queryHierarchy,model);
            try {
                AssetCollection actAsset = new Asset(uri);
                ResultSet assetSet = queryExeHierarchy.execSelect();

                while(assetSet.hasNext()){

                    QuerySolution asset = assetSet.next();
                    Resource assetRes = asset.getResource("sub");
                    assets.get(assetRes.getURI()).addParent(assets.get(uri));

                }
            }catch (Exception e){

                System.err.println(e.getMessage());

            }

        }


        return assets;


    }

    public static void retrieveODRLActionHierarchy(){
        String queryString = "SELECT * FROM <https://www.w3.org/ns/odrl/2/ODRL22.ttl> WHERE {" +
                "?sub <"+ODRL_vocab.includedIn+"> ?obj  }";
        Query query = QueryFactory.create(queryString);
        QueryExecution executionEnv = QueryExecutionFactory.create(query);
        ResultSet resultSet = executionEnv.execSelect();

        while(resultSet.hasNext()){

            QuerySolution triplet = resultSet.next();
            System.out.println("Action :"+triplet.getResource("sub")+ " is Included In: "+ triplet.getResource("obj"));
        }
    }
}
