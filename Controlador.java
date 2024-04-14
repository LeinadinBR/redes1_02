import javax.swing.JTextField;

/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
  Autor: Daniel Nogueira
  Matricula: 201911910
  Inicio...: 04 de Agosto de 2021
  Alteracao: 04 de Agosto de 2021
  Nome.....: Controlador
  Funcao...: Classe que serve de intermediario no controle da animacao das barrinhas
  =-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
public class Controlador {
  private Animacao esq, dir; //instancias de animacao
  private JTextField textField;
  private Boolean isProcessing = false;

  public Controlador(JTextField textField){
    this.textField = textField;
  }

  //getters e setters
  public Animacao getEsq() {
    return esq;
  }

  public void setEsq(Animacao esq) {
    this.esq = esq;
  }

  public Animacao getDir() {
    return dir;
  }

  public void setDir(Animacao dir) {
    this.dir = dir;
  }

  public JTextField getTextField() {
    return textField;
  }

  public void setTextField(JTextField textField) {
    this.textField = textField;
  }

  public Boolean getIsProcessing() {
    return isProcessing;
  }

  public void setIsProcessing(Boolean isProcessing) {
    this.isProcessing = isProcessing;
  }
}
