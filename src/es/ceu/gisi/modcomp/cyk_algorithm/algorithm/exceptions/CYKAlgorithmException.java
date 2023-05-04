package es.ceu.gisi.modcomp.cyk_algorithm.algorithm.exceptions;
import java.util.HashSet;

/**
 *
 * @author Sergio Saugar <sergio.saugargarcia@ceu.s>
 */
public class CYKAlgorithmException extends Exception {

    public CYKAlgorithmException(String el_elemento_debe_ser_una_letra_mayúscula) {

    /**
     *
     */

    /**
     *
     */
    /**
     *
     */

    /**
     *
     */
    public class Grammar implements addNonTerminal {

  private HashSet<Character> nonterminals;

  public Grammar() {
    nonterminals = new HashSet<Character>();
  }

  @Override
  public void addNonterminal(char nonterminal) throws CYKAlgorithmException {
    if (!Character.isUpperCase(nonterminal)) {
      throw new CYKAlgorithmException("El elemento debe ser una letra mayúscula");
    }
    if (nonterminals.contains(nonterminal)) {
      throw new CYKAlgorithmException("El elemento ya está en el conjunto");
    }
    nonterminals.add(nonterminal);
  }
}
        
    }

}
