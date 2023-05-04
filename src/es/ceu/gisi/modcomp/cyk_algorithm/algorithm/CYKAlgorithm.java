package es.ceu.gisi.modcomp.cyk_algorithm.algorithm;

import es.ceu.gisi.modcomp.cyk_algorithm.algorithm.exceptions.CYKAlgorithmException;
import es.ceu.gisi.modcomp.cyk_algorithm.algorithm.interfaces.CYKAlgorithmInterface;
import java.util.HashSet;
import java.util.HashMap;


/**
 * Esta clase contiene la implementación de la interfaz CYKAlgorithmInterface
 * que establece los métodos necesarios para el correcto funcionamiento del
 * proyecto de programación de la asignatura Modelos de Computación.
 *
 * @author Sergio Saugar García <sergio.saugargarcia@ceu.es>
 */
public abstract class CYKAlgorithm implements CYKAlgorithmInterface {
    
    private final HashSet<Character> nonterminals;
    private char axiom;
    private HashMap<Character, HashSet<String>> productions;


    private HashSet<Character> terminals;
    private Object productions;

  public CYKAlgorithm() {
    nonterminals = new HashSet<>();
     terminals = new HashSet<>();
    productions = new HashMap<Character, HashSet<String>>();

  }

    @Override
    /**
     * Método que añade los elementos no terminales de la gramática.
     *
     * @param nonterminal Por ejemplo, 'S'
     * @throws CYKAlgorithmException Si el elemento no es una letra mayúscula.
     */
    public void addNonTerminal(char nonterminal) throws CYKAlgorithmException {
         if (!Character.isUpperCase(nonterminal)) {
      throw new CYKAlgorithmException("El elemento debe ser una letra mayúscula");
    }
    if (nonterminals.contains(nonterminal)) {
      throw new CYKAlgorithmException("El elemento ya está en el conjunto");
    }
    nonterminals.add(nonterminal);
  }

    

    @Override
    /**
     * Método que añade los elementos terminales de la gramática.
     *
     * @param terminal Por ejemplo, 'a'
     * @throws CYKAlgorithmException Si el elemento no es una letra minúscula.
     */
    
    public void addTerminal(char terminal) throws CYKAlgorithmException {
        if (!Character.isLowerCase(terminal)) {
      throw new CYKAlgorithmException("El elemento debe ser una letra minúscula");
    }
    if (terminals.contains(terminal)) {
      throw new CYKAlgorithmException("El elemento ya está en el conjunto");
    }
    terminals.add(terminal);
  }

    

    @Override
    /**
     * Método que indica, de los elementos no terminales, cuál es el axioma de
     * la gramática.
     *
     * @param nonterminal Por ejemplo, 'S'
     * @throws CYKAlgorithmException Si el elemento insertado no forma parte del
     * conjunto de elementos no terminales.
     */
    public void setStartSymbol(char nonterminal) throws CYKAlgorithmException {
      if (!nonterminals.contains(nonterminal)) {
      throw new CYKAlgorithmException("El elemento no forma parte del conjunto de elementos no terminales");
    }
    axiom = nonterminal;
  }
    

    @Override
    /**
     * Método utilizado para construir la gramática. Admite producciones en FNC,
     * es decir de tipo A::=BC o A::=a
     *
     * @param nonterminal A
     * @param production "BC" o "a"
     * @throws CYKAlgorithmException Si la producción no se ajusta a FNC o está
     * compuesta por elementos (terminales o no terminales) no definidos
     * previamente.
     */
    public void addProduction(char nonterminal, String production) throws CYKAlgorithmException {
        if (!nonterminals.contains(nonterminal)) {
      throw new CYKAlgorithmException("El elemento no forma parte del conjunto de elementos no terminales");
    }
      if (!productions.containsKey(nonterminal)) {
      productions.put(nonterminal, new HashSet<String>());
    }
    HashSet<String> set = productions.get(nonterminal);

        // Verificar que la producción esté en FNC
        switch (production.length()) {
            case 1 -> {
                // Producción de tipo A::=a
                if (!Character.isLowerCase(production.charAt(0))) {
                    throw new CYKAlgorithmException("La producción debe ser un elemento terminal (letra minúscula)");
                }         set.add(production);
            }
            case 2 -> {
                // Producción de tipo A::=BC
                if (!Character.isUpperCase(production.charAt(0)) || !Character.isUpperCase(production.charAt(1))) {
                    throw new CYKAlgorithmException("La producción debe ser una pareja de elementos no terminales (letras mayúsculas)");
                }         if (!productions.containsKey(production.charAt(0)) || !productions.containsKey(production.charAt(1))) {
                    throw new CYKAlgorithmException("Los elementos de la producción no están definidos previamente en la gramática");
                }         set.add(production);
            }
            default -> throw new CYKAlgorithmException("La producción no se ajusta a la FNC (Forma Normal de Chomsky)");
        }
    }

    @Override
    /**
     * Método que indica si una palabra pertenece al lenguaje generado por la
     * gramática que se ha introducido.
     *
     * @param word La palabra a verificar, tiene que estar formada sólo por
     * elementos no terminales.
     * @return TRUE si la palabra pertenece, FALSE en caso contrario
     * @throws CYKAlgorithmException Si la palabra proporcionada no está formada
     * sólo por terminales, si está formada por terminales que no pertenecen al
     * conjunto de terminales definido para la gramática introducida, si la
     * gramática es vacía o si el autómata carece de axioma.
     */
    public String isDerived(String word) throws CYKAlgorithmException {
   // Verificar que la palabra esté formada sólo por elementos no terminales
    for (char c : word.toCharArray()) {
        if (Character.isLowerCase(c)) {
            throw new CYKAlgorithmException("La palabra contiene elementos terminales");
        }
    }
    
    // Verificar que la gramática tenga al menos un axioma
    if (this.axiom != null) 
    {
    } else {
        throw new CYKAlgorithmException("La gramática no tiene un axioma");
        }
    
    // Verificar que la palabra esté formada por elementos definidos en la gramática
    for (char c : word.toCharArray()) {
        if (!this.nonterminals.contains(c)) {
            throw new CYKAlgorithmException("La palabra contiene elementos no definidos en la gramática");
        }
    }
    
    // Crear la matriz para almacenar los resultados del algoritmo CYK
    int n = word.length();
    String[][] matrix = new String[n][n];
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            matrix[i][j] = "";
        }
    }
    
    // Llenar la diagonal de la matriz con los elementos de la palabra
    for (int i = 0; i < n; i++) {
        char c = word.charAt(i);
        String productions = this.productions.get(c);
        if (productions != null) {
            matrix[i][i] = productions;
        }
    }
    
    // Llenar las celdas restantes de la matriz
    for (int l = 2; l <= n; l++) {
        for (int i = 0; i <= n - l; i++) {
            int j = i + l - 1;
            for (int k = i; k < j; k++) {
                String left = matrix[i][k];
                String right = matrix[k+1][j];
                if (left.isEmpty() || right.isEmpty()) {
                    continue;
                }
                for (String production : this.fncProductions) {
                    char nt = production.charAt(0);
                    String body = production.substring(3);
                    if (left.contains(String.valueOf(nt)) && right.contains(body)) {
                        matrix[i][j] += nt;
                    }
                }
            }
        }
    }
    
    // Construir el String que se devolverá como resultado
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            sb.append(matrix[i][j]);
            if (j < n - 1) {
                sb.append("\t");
            }
        }
        sb.append("\n");
    }
    return sb.toString();
}

    @Override
    /**
     * Método que, para una palabra, devuelve un String que contiene todas las
     * celdas calculadas por el algoritmo (la visualización debe ser similar al
     * ejemplo proporcionado en el PDF que contiene el paso a paso del
     * algoritmo).
     *
     * @param word La palabra a verificar, tiene que estar formada sólo por
     * elementos no terminales.
     * @return Un String donde se vea la tabla calculada de manera completa,
     * todas las celdas que ha calculado el algoritmo.
     * @throws CYKAlgorithmException Si la palabra proporcionada no está formada
     * sólo por terminales, si está formada por terminales que no pertenecen al
     * conjunto de terminales definido para la gramática introducida, si la
     * gramática es vacía o si el autómata carece de axioma.
     */
    public String algorithmStateToString(String word) throws CYKAlgorithmException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    /**
     * Elimina todos los elementos que se han introducido hasta el momento en la
     * gramática (elementos terminales, no terminales, axioma y producciones),
     * dejando el algoritmo listo para volver a insertar una gramática nueva.
     */
    public void removeGrammar(Object axioma) {
       // Eliminar todos los elementos de la gramática
        terminals.clear();
        nonterminals.clear();
        producciones.clear();
        axioma = null;
    }

    @Override
    /**
     * Devuelve un String que representa todas las producciones que han sido
     * agregadas a un elemento no terminal.
     *
     * @param nonterminal
     * @return Devuelve un String donde se indica, el elemento no terminal, el
     * símbolo de producción "::=" y las producciones agregadas separadas, única
     * y exclusivamente por una barra '|' (no incluya ningún espacio). Por
     * ejemplo, si se piden las producciones del elemento 'S', el String de
     * salida podría ser: "S::=AB|BC".
     */
    public String getProductions(char nonterminal) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    /**
     * Devuelve un String con la gramática completa.
     *
     * @return Devuelve el agregado de hacer getProductions sobre todos los
     * elementos no terminales.
     */
    public String getGrammar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

   
