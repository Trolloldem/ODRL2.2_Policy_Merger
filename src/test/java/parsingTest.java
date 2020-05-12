package test.java;

import Assets.Asset;
import Assets.AssetCollection;
import Parser.policyReader;
import Rule.Rule;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import Rule.Permission;
import Rule.Prohibition;

public class parsingTest {


    @Test
    public void ruleExtraction(){
        String examplePath = "./src/test/java/data.jsonld";
        Map<AssetCollection, List<Rule>> mappa = policyReader.readPolicyRules(examplePath);
        for(Map.Entry<AssetCollection,List<Rule>> entry : mappa.entrySet()){

            if(!(entry.getKey().equals(new Asset("EveryAsset"))||entry.getKey().equals(new Asset("http://example.com/music/1999.mp3")) || entry.getKey().equals(new Asset("http://example.com/music/PurpleRain.mp3"))  || entry.getKey().equals(new Asset("http://example.com/music/aLotOfMp3"))  || entry.getKey().equals(new Asset("http://example.com/music/10secPurple.mp3")) ))
                fail("L'asset "+entry.getKey()+" non è presente nel documento");

            if(entry.getKey().equals(new Asset("http://example.com/music/1999.mp3"))){
                Boolean[] flags = {false,false,false};
                for(Rule r : entry.getValue()){
                    switch (r.getAction()){
                        case PLAY:  assertEquals(true, r instanceof Permission, "L'azione "+r.getAction()+" deve essere Permitted");
                            flags[0]=true;
                            break;
                        case STREAM: assertEquals(true, r instanceof Permission, "L'azione "+r.getAction()+" deve essere Permitted");
                            flags[1]=true;
                        break;
                        case DELETE:assertEquals(true, r instanceof Prohibition, "L'azione "+r.getAction()+" deve essere Prohibited");
                            flags[2]=true;
                            break;
                        default:
                            fail("L'asset '"+entry.getKey()+"' non ha regole relative all'azione "+r.getAction()+" nel documento");
                    }
                }
                assertEquals(true,flags[0] && flags[1] &&flags[2], "Non sono presenti tutte le azioni dichiarate per: "+entry.getKey());
            }
            if(entry.getKey().equals(new Asset("http://example.com/music/PurpleRain.mp3"))){
                Boolean[] flags = {false,false,false};
                for(Rule r : entry.getValue()){
                    switch (r.getAction()){
                        case PLAY:  assertEquals(true, r instanceof Permission, "L'azione "+r.getAction()+" deve essere Permitted");
                            flags[0]=true;
                            break;
                        case STREAM: assertEquals(true, r instanceof Permission, "L'azione "+r.getAction()+" deve essere Permitted");
                            flags[1]=true;
                            break;
                        case INSTALL:assertEquals(true, r instanceof Prohibition, "L'azione "+r.getAction()+" deve essere Prohibited");
                            flags[2]=true;
                            break;
                        default:
                            fail("L'asset '"+entry.getKey()+"' non ha regole relative all'azione "+r.getAction()+" nel documento");
                    }
                }
                assertEquals(true,flags[0] && flags[1] &&flags[2], "Non sono presenti tutte le azioni dichiarate per: "+entry.getKey());
            }
            if(entry.getKey().equals(new Asset("http://example.com/music/aLotOfMp3"))){
                Boolean[] flags = {false};
                for(Rule r : entry.getValue()){
                    switch (r.getAction()){
                        case ANNOTATE:  assertEquals(true, r instanceof Permission, "L'azione "+r.getAction()+" deve essere Permitted");
                            flags[0]=true;
                            break;

                        default:
                            fail("L'asset '"+entry.getKey()+"' non ha regole relative all'azione "+r.getAction()+" nel documento");
                    }
                }
                assertEquals(true,flags[0], "Non sono presenti tutte le azioni dichiarate per: "+entry.getKey());
            }
            if(entry.getKey().equals(new Asset("http://example.com/music/10secPurple.mp3"))){
                Boolean[] flags = {false};
                for(Rule r : entry.getValue()){
                    switch (r.getAction()){
                        case ANNOTATE:  assertEquals(true, r instanceof Prohibition, "L'azione "+r.getAction()+" deve essere Permitted");
                            flags[0]=true;
                            break;

                        default:
                            fail("L'asset '"+entry.getKey()+"' non ha regole relative all'azione "+r.getAction()+" nel documento");
                    }
                }
                assertEquals(true,flags[0], "Non sono presenti tutte le azioni dichiarate per: "+entry.getKey());
            }
        }
    }
    @Test
    public void hierarchyCheck(){
        String examplePath = "./src/test/java/data.jsonld";
        Map<String,Asset> assets = policyReader.readAssets(examplePath);
        for(Map.Entry<String,Asset> entry : assets.entrySet()){
            if(!(entry.getKey().equals("http://example.com/music/1999.mp3") || entry.getKey().equals("http://example.com/music/PurpleRain.mp3")  || entry.getKey().equals("http://example.com/music/aLotOfMp3")  || entry.getKey().equals("http://example.com/music/10secPurple.mp3")) )
                fail("L'asset "+entry.getKey()+" non è presente nel documento");

            Asset actAsset = entry.getValue();
            if(actAsset.equals(new Asset("http://example.com/music/1999.mp3"))){
                assertEquals(new Asset("http://example.com/music/aLotOfMp3"), actAsset.getParent(), "Parent errato per l'asset: "+actAsset.getURI());
                assertEquals(null, actAsset.getChildren(), "Figli errati per l'asset: "+actAsset.getURI());
            }
            if(actAsset.equals(new Asset("http://example.com/music/aLotOfMp3"))){
                assertEquals(null, actAsset.getParent(), "Parent errato per l'asset: "+actAsset.getURI());
                assertEquals(true, actAsset.getChildren().contains(new Asset(("http://example.com/music/1999.mp3"))), "Figli errati per l'asset: "+actAsset.getURI());
                assertEquals(true, actAsset.getChildren().contains(new Asset(("http://example.com/music/PurpleRain.mp3"))), "Figli errati per l'asset: "+actAsset.getURI());
                assertEquals(2, actAsset.getChildren().size(),"Figli errati per l'asset: "+actAsset.getURI());
            }
            if(actAsset.equals(new Asset("http://example.com/music/PurpleRain.mp3"))){
                assertEquals(new Asset("http://example.com/music/aLotOfMp3"), actAsset.getParent(), "Parent errato per l'asset: "+actAsset.getURI());
                assertEquals(true, actAsset.getChildren().contains(new Asset("http://example.com/music/10secPurple.mp3")), "Figli errati per l'asset: "+actAsset.getURI());
                assertEquals(1, actAsset.getChildren().size(),"Figli errati per l'asset: "+actAsset.getURI());
            }
            if(actAsset.equals(new Asset("http://example.com/music/10secPurple.mp3"))){
                assertEquals(new Asset("http://example.com/music/PurpleRain.mp3"), actAsset.getParent(), "Parent errato per l'asset: "+actAsset.getURI());
                assertEquals(null, actAsset.getChildren(), "Figli errati per l'asset: "+actAsset.getURI());
            }


        }
    }
}
