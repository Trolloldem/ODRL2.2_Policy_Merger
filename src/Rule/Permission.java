package Rule;

import Actions.Action;

public class Permission implements Rule{
    private String type = "Permission";
    private Action azione;

    public Permission(Action azione){
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
     * @return Stringa "Permission"
     */
    @Override
    public String getType() {
        return type;
    }
}
