package Parser;


import Actions.Action;
import Assets.Asset;
import Assets.AssetCollection;
import Rule.Rule;
import Rule.Permission;
import Rule.Prohibition;
import javafx.util.Pair;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import java.util.*;

public class policyReader {

    private static void print(Set<Resource> risorse, Map<Resource,List<Pair<Property, RDFNode>>> mappaSoggettoValori, Map<Resource,Map<Property,Integer>> mappaSoggettoProp){

        for(Resource r : risorse){
            System.out.println("==========");

            System.out.println("Il soggetto identificato come: "+r+"\nHa le seguenti campi");
            for(Pair<Property,RDFNode> coppia : mappaSoggettoValori.get(r)){
                System.out.println(""+coppia.getKey()+" : "+coppia.getValue());
            }

            System.out.println("Visualizzazione del numero di volte in cui appare la propietà");

            Iterator<Map.Entry<Property, Integer>> it = mappaSoggettoProp.get(r).entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Property, Integer> pair = it.next();
                System.out.println("La proprietà "+pair.getKey() +" è presente: "+pair.getValue());
            }

            System.out.println("==========");
        }

    }



    public static void readFile(){


        Set<Resource> soggetti = new HashSet<Resource>();
        Map<Resource,List<Pair<Property, RDFNode>>> mappaSoggettoValori= new HashMap<Resource,List<Pair<Property, RDFNode>>> ();
        Map<Resource,Map<Property,Integer>> mappaSoggettoProp = new HashMap<Resource,Map<Property,Integer>>();

        Model model = ModelFactory.createDefaultModel() ;
        model.read("/home/oldem/IdeaProjects/actionTree/src/Parser/data.jsonld") ;

        for (StmtIterator it = model.listStatements(); it.hasNext(); ) {
            Statement s = it.next();

            Resource soggetto = s.getSubject();
            Property predicato = s.getPredicate();
            RDFNode oggetto = s.getObject();

            soggetti.add(s.getSubject());

            if(mappaSoggettoProp.containsKey(soggetto)){

                List<Pair<Property, RDFNode>> listaProp = mappaSoggettoValori.get(soggetto);
                Pair<Property,RDFNode> coppiaPredOgg = new Pair<Property,RDFNode>(predicato,oggetto);
                listaProp.add(coppiaPredOgg);

            }else{

                List<Pair<Property, RDFNode>> listaProp = new LinkedList<Pair<Property, RDFNode>>();
                Pair<Property,RDFNode> coppiaPredOgg = new Pair<Property,RDFNode>(predicato,oggetto);
                listaProp.add(coppiaPredOgg);
                mappaSoggettoValori.put(soggetto, listaProp);

            }

            if(mappaSoggettoProp.containsKey(soggetto)){

                Map<Property,Integer> mappaProp = mappaSoggettoProp.get(soggetto);
                if(mappaProp.containsKey(predicato)){

                    mappaProp.replace(predicato, mappaProp.get(predicato)+1);

                }else{

                    mappaProp.put(predicato, 1);

                }


            }else{

                Map<Property,Integer> mappaProp = new HashMap<Property,Integer>();
                mappaProp.put(predicato, 1);
                mappaSoggettoProp.put(soggetto, mappaProp);

            }



        }
        print(soggetti,mappaSoggettoValori, mappaSoggettoProp);
    }


    public static  void manageMaps(Resource policyRes, Property type, Model model, Map<Resource,Set<Pair<Resource,String>>> mappaPolicyRules, Map<Resource,List<Pair<String,String>>> mappaRulesParams) {

        ParameterizedSparqlString queryRulesString = new ParameterizedSparqlString();
        queryRulesString.setCommandText("SELECT ?rule ?pred ?ogg WHERE {" +
                "?policy <" + type + "> ?rule ." +
                " ?rule ?pred ?ogg}");
        queryRulesString.setIri("policy",policyRes.getURI());
        Query queryRules = QueryFactory.create(queryRulesString.toString());
        QueryExecution queryExe2 = QueryExecutionFactory.create(queryRules,model);
        ResultSet rules = queryExe2.execSelect();

        while(rules.hasNext()){
            QuerySolution rule = rules.next();
            Resource ruleRes = rule.getResource("rule");

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
     * Parsing del file JSON-LD con path passato come parametro
     * @param path, String con path del file che si vuole parsare
     */
    public static Map<AssetCollection,List<Rule>>  readFileQuery(String path){

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



                manageMaps(policyRes, ODRL_vocab.permission, model, mappaPolicyRules, mappaRulesParams);
                manageMaps(policyRes, ODRL_vocab.prohibition, model, mappaPolicyRules, mappaRulesParams);


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
}
