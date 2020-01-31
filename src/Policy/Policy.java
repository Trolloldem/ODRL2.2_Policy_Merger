package Policy;

import java.util.ArrayList;
import Rule.Rule;

public interface Policy {
    /**
     * Getter della lista di regole presenti nella policy
     * @return ArrayList di Rule
     */
    public ArrayList<Rule> getRules();
    /**
     * Getter del tipo di policy
     * @return Stringa rappresentante il tipo di policy
     */
    public String getType();

    /**
     * Unisce i permessi della policy di cui si chiama il metodo con quelli della policy p
     * @param p: policy con la quale si vuole unire la policy attuale
     * @return Policy creata tramite unione
     */
    public Policy UniteWith(Policy p);
    /**
     * Interseca i permessi della policy di cui si chiama il metodo con quelli della policy p
     * @param p: policy con la quale si vuole intersecare la policy attuale
     * @return Policy creata tramite intersezione
     */
    public Policy IntersectWith(Policy p);
}
