package Rule;
import Actions.Action;

public interface Rule {
    /**
     * Getter dell'azione permessa o vietata dalla regola
     * @return Azione permessa o vietata
     */
    public Action getAction();
    /**
     * Getter del tipo di regola
     * @return Stringa rappresentante il tipo di regola
     */
    public String getType();
}
