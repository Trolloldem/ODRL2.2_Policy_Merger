package Actions;

/**
 * Enumerativo che racchiude tutte le azioni presenti nell'ODRL Common Vocabulary
 */
public enum Action {
    ATTRIBUTION("Attribution",new Action[]{}),
    COMMERICALUSE("CommercialUse",new Action[]{}),
    DERIVATIVEWORKS("DerivativeWorks",new Action[]{}),
    DISTRIBUTION("Distribution",new Action[]{}),
    NOTICE("Notice",new Action[]{}),
    REPRODUCTION("Reproduction",new Action[]{}),
    SHAREALIKE("ShareAlike",new Action[]{}),
    SHARING("Sharing",new Action[]{}),
    SOURCECODE("SourceCode",new Action[]{}),
    ACCEPTTRACKING("acceptTracking",new Action[]{}),
    AGGREGATE("aggregate",new Action[]{}),
    ANNOTATE("annotate",new Action[]{}),
    ANONYMIZE("anonymize",new Action[]{}),
    ARCHIVE("archive",new Action[]{}),
    ATTRIBUTE("attribute",new Action[]{}),
    COMPENSATE("compensate",new Action[]{}),
    CONCURRENTUSE("concurrentUse",new Action[]{}),
    DELETE("delete",new Action[]{}),
    DERIVE("derive",new Action[]{}),
    DIGITIZE("digitalize",new Action[]{}),
    DISTRIBUTE("distribute",new Action[]{}),
    ENSUREEXCLUSIVITY("ensureExclusivity",new Action[]{}),
    EXTRACT("extract" ,new Action[]{}),
    EXECUTE("execute",new Action[]{}),
    GIVE("give",new Action[]{}),
    GRANTUSE("grantUse",new Action[]{}),
    INCLUDE("include",new Action[]{}),
    INDEX("index",new Action[]{}),
    INFORM("inform",new Action[]{}),
    INSTALL("install",new Action[]{}),
    MODIFY("modify",new Action[]{}),
    MOVE("move",new Action[]{}),
    NEXTPOLICY("nextPolicy",new Action[]{}),
    OBTAINCONSENT("obtainConsent",new Action[]{}),
    DISPLAY("display",new Action[]{}),
    PRESENT("present",new Action[]{}),
    PRINT("print",new Action[]{}),
    READ("read",new Action[]{}),
    REVIEWPOLICY("reviewPolicy",new Action[]{}),
    SELL("sell",new Action[]{}),
    STREAM("stream",new Action[]{}),
    SYNCHRONIZE("synchronize",new Action[]{}),
    TEXTTOSPEECH("textToSpeech",new Action[]{}),
    TRANSFORM("transform",new Action[]{}),
    TRANSLATE("translate",new Action[]{}),
    UNINSTALL("uninstall",new Action[]{}),
    WATERMARK("watermark",new Action[]{}),
    REPRODUCE("reproduce",new Action[]{EXTRACT}),
    PLAY("play",new Action[]{DISPLAY}),
    TRANSFER("transfer",new Action[]{GIVE,SELL}),
    USE("use", new Action[]{ATTRIBUTION, COMMERICALUSE, DERIVATIVEWORKS, DISTRIBUTION, NOTICE, REPRODUCTION, SHAREALIKE, SHARING, SOURCECODE, ACCEPTTRACKING, AGGREGATE, ANNOTATE, ANONYMIZE, ARCHIVE, ATTRIBUTE, COMPENSATE, CONCURRENTUSE, DELETE, DERIVE, DIGITIZE, DISTRIBUTE, ENSUREEXCLUSIVITY, EXECUTE, GRANTUSE, INCLUDE, INDEX, INFORM, INSTALL, MODIFY, MOVE, NEXTPOLICY, OBTAINCONSENT, PLAY, PRESENT, PRINT, READ, REPRODUCE, REVIEWPOLICY, STREAM, SYNCHRONIZE, TEXTTOSPEECH, TRANSFORM, TRANSLATE, UNINSTALL, WATERMARK});

    private String name;
    private Action[] includedBy;
    private Action includedIn=null;
    Action(String name,Action[] includedBy){
        this.name=name;
        this.includedBy=includedBy;
        for(Action child:this.getIncludedBy()){
            child.setIncludedIn(this);
        }
    }

    private void setIncludedIn(Action father){
        this.includedIn=father;
    }

    /**
     * Getter della stringa rappresentante il nome dell'azione
     * @return Stringa che rappresenta il nome dell'azione
     */
    public String getName(){
        return name;
    }

    /**
     * Getter di un array contenente tutte le azioni incluse
     * nell'azione della quale si chiama il metodo. Dipendenze
     * relative a quanto definito nell'ODRL Common Vocabulary
     * @return Array contenente tutte le azioni incluse
     *      * nell'azione della quale si chiama il metodo
     */
    public Action[] getIncludedBy(){
        return includedBy;
    }

    /**
     * Getter dell'azione che include l'azione della quale si chiama il metodo. Dipendenze
     * relative a quanto definito nell'ODRL Common Vocabulary
     * @return Azione che include l'azione della quale si chiama il metodo
     */
    public Action getIncludedIn() {
        return includedIn;
    }
}
