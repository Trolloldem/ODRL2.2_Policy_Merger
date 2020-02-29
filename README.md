# ODRL2.2_Policy_Merger
Stato attuale:
- Rappresentazione delle azioni mediante albero di permessi
- Le policy hanno un singolo asset Target
- Le regole e le azioni attualmente rappresentate non presentano "constraint" o "refinement"
- Le policy definite per un asset si propagano sui target figli(sia in modo intersect che union)
- Le azioni di UNION e INTERSECT delle policy lavorano concordi con quanto presentato sopra
