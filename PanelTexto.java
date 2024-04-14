import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
  Autor: Daniel Nogueira
  Matricula: 201911910
  Inicio...: 01 de Agosto de 2021
  Alteracao: 04 de Agosto de 2021
  Nome.....: PanelTexto
  Funcao...: Classe que serve para suporte de caixas de texto, botoes e caixas de escolha
  =-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
public class PanelTexto extends JPanel{
  private JTextField texto1, texto2;  //caixas de texto
  private JButton okBtn;              //botao de envio

  /* *********************
  * Metodo: PanelTexto
  * Funcao: Construtor
  * Parametros: Nenhum
  ********************* */
  public PanelTexto(){
    inicializar();

    this.setBackground(new Color(95, 140, 190));
    this.setLayout(null);
    this.setSize(new Dimension(900, 50));
    this.add(texto1);
    this.add(texto2);
    this.add(okBtn);
    this.setVisible(false);
  }


  /* *********************
  * Metodo: inicializar
  * Funcao: Funcao que inicializa os objetos da classe
  * Parametros: Nenhum
  * Retorno: void
  ********************* */
  private void inicializar(){
    texto1 = new JTextField();
    texto1.setBounds(110, 15, 100, 20);
    texto2 = new JTextField();
    texto2.setBounds(700, 15, 100, 20);
    texto2.setEditable(false);

    okBtn = new JButton("Enviar");
    okBtn.setBounds(215, 15, 80, 20);
    okBtn.setFocusable(false);

  }

  //metodos getters e setters
  public JTextField getTexto1() {
    return texto1;
  }

  public void setTexto1(JTextField texto1) {
    this.texto1 = texto1;
  }

  public JTextField getTexto2() {
    return texto2;
  }

  public void setTexto2(JTextField texto2) {
    this.texto2 = texto2;
  }

  public JButton getOkBtn() {
    return okBtn;
  }

  public void setOkBtn(JButton okBtn) {
    this.okBtn = okBtn;
  }

}
