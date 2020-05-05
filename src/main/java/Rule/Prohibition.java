package Rule;

import Actions.Action;

public class Prohibition implements Rule{
    private String type = "Prohibition";
    private Action azione;
    public Prohibition(Action azione){
        this.azione=azione;
    }
    /**
     * Getter dell'azione permessa o vietata dalla regola
     * @return Azione permessa o vietata
     */
    @Override
    public Action getAction() {
        return azione;
    }
    /**
     * Getter del tipo di regola
     * @return Stringa "Prohibition"
     */
    @Override
    public String getType() {
        return type;
    }
}

