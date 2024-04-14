import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
  Autor: Daniel Nogueira
  Matricula: 201911910
  Inicio...: 31 de Julho de 2021
  Alteracao: 20 de Fevereiro de 2022
  Nome.....: PanelSimulacao
  Funcao...: Classe que serve para animar a simulacao
  =-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
public class PanelSimulacao extends JPanel{
  private BufferedImage imagem = new BufferedImage(900, 600, BufferedImage.TYPE_INT_BGR);  //imagem na qual um objeto 'Graphic' pode 'desenhar'
  private Display display;        //referencia a display
  private PanelTexto panelTexto;   //referencia a panelTexto
  private JTextField textField;      //Area onde os bits sao mostrados
  private Sprite mesa, computador, pessoa1, pessoa2, background, background2;  //sprite imoveis
  private Animacao barrinhaEsq, barrinhaDir;        //sprites com animacoes
  private Controlador controle;

  /* *********************
  * Metodo: PanelSimulacao
  * Funcao: Construtor
  * Parametros: Display display, PanelTexto panelTexto
  ********************* */
  public PanelSimulacao(Display display, PanelTexto panelTexto, Controlador controle){
    this.display = display;
    this.panelTexto = panelTexto;
    this.controle = controle;
    this.textField = controle.getTextField();

    inicializar();

    this.add(display.getTela());
    this.setSize(new Dimension(900, 600));
  }

  /* *********************
  * Metodo: inicializar
  * Funcao: Funcao que inicializa os objetos da classe
  * Parametros: Nenhum
  * Retorno: void
  ********************* */
  private void inicializar(){
    mesa = new Sprite("res/mesa.png");
    computador = new Sprite("res/pc.png");
    pessoa1 = new Sprite("res/pessoa1.png");
    pessoa2 = new Sprite("res/pessoa2.png");
    background = new Sprite("res/background.png");
    background2 = new Sprite("res/background2.png");

    barrinhaEsq = new Animacao(new String[]{"res/base.png", "res/esteira1.png", "res/esteira2.png", "res/esteira3.png"},
       4);
    barrinhaDir = new Animacao(new String[]{"res/base.png", "res/esteira1-2.png", "res/esteira2-2.png", "res/esteira3-2.png"},
       4);

  }

  /* *********************
  * Metodo: renderizar
  * Funcao: Funcao que 'desenha' a simulacao
  * Parametros: Nenhum
  * Retorno: void
  ********************* */
  public void renderizar(){
    long tempo = 0;
    
    //inicia as threads das animacoes
    barrinhaDir.ligarTickAnimacao();
    barrinhaEsq.ligarTickAnimacao();
    
    while (true){
      //criacao do objeto de desenho "Graphics 'g'"------------
      display.criarBufferStrategy();
      BufferStrategy bs = display.getTela().getBufferStrategy();
      if (bs == null) {
        display.criarBufferStrategy();
        bs = display.getTela().getBufferStrategy();
      }
      Graphics g = imagem.getGraphics();
      g = bs.getDrawGraphics();
      
      //desenho em si no canvas---------------------------------------------------
      //imagem inicial
      if (tempo < 800){
        g.drawImage(background.getSprite(), 0, 0, 900, 600, null);
        tempo++;
      }
      //parte principal
      else {
        textField.setVisible(true);
        if (tempo == 800){
          tempo++;
          panelTexto.setVisible(true);
          this.setBounds(0, 50, 900, 600);
        }
        g.drawImage(background2.getSprite(), 0, 0, null);
        g.drawImage(barrinhaEsq.getImagem().getSprite(), 125, 120, 72, 330, null);
        g.drawImage(barrinhaDir.getImagem().getSprite(), 715, 120, 72, 330, null);
        g.drawImage(mesa.getSprite(), 124, 40, 64, 64, null);
        g.drawImage(mesa.getSprite(), 715, 40, 64, 64, null);
        g.drawImage(computador.getSprite(), 128, 20, 55, 55, null);
        g.drawImage(computador.getSprite(), 719, 20, 55, 55, null);
        g.drawImage(pessoa1.getSprite(), 110, 42, 100, 100, null);
        g.drawImage(pessoa2.getSprite(), 700, 42, 100, 100, null);

        if (controle.getIsProcessing())
          textField.update(textField.getGraphics());

      }
      //---------------------------------------------------------------------------------------

      //finaliza as variaveis
      g.dispose();
      bs.show();
    }
  }

  //metodos getters e setters
  public Animacao getBarrinhaEsq() {
    return barrinhaEsq;
  }

  public void setBarrinhaEsq(Animacao barrinhaEsq) {
    this.barrinhaEsq = barrinhaEsq;
  }

  public Animacao getBarrinhaDir() {
    return barrinhaDir;
  }

  public void setBarrinhaDir(Animacao barrinhaDir) {
    this.barrinhaDir = barrinhaDir;
  }
}