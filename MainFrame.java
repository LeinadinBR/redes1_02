import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;

/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
  Autor: Daniel Nogueira
  Matricula: 201911910
  Inicio...: 30 de Julho de 2021
  Alteracao: 28 de Marco de 2022
  Nome.....: MainFrame
  Funcao...: Classe que serve de frame principal do programa
  =-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
public class MainFrame extends JFrame {
  private Display display;                //objeto de display
  private PanelSimulacao panelSimulacao;  //painel onde a animacao de simulacao acontece
  private PanelTexto panelTexto;          //painel onde os textos e botaos estao
  private MeioTransmicao meioTransmicao;  //simulacao do meio de comunicacao
  private MeioFisico meioFisicoTransmissor, meioFisicoReceptor;  //simulacao do meio fisico
  private MeioEnlaceDeDados meioEnlaceDeDadosTransmissor, meioEnlaceDeDadosReceptor; //
  private MeioAplicacao meioAplicacaoTransmissor, meioAplicacaoReceptor;  //simulacao do meio de aplicacao
  private Controlador controle;                                           //objeto de controle
  private JTextField textField;                                           //textfield onde aparece os sinais
  private JMenuBar menuBar;                                               //barra de menu
  private JMenu camadaFisicaMenu, camadaEnlaceMenu;                       //itens do menu
  private JRadioButtonMenuItem mBinario, mManchester, mDifManchester;     //itens de um menu especifico
  private JRadioButtonMenuItem m1ContagemCaractere, m1InsercaoBytes, m1InsercaoBits, m1ViolacaoFisica;  //itens de um menu especifico

  /* *********************
  * Metodo: MainFrame
  * Funcao: construtor
  * Parametros: nenhum
  ********************* */
  public MainFrame(){
    inicializar();

    menuBar.add(camadaFisicaMenu);
    menuBar.add(camadaEnlaceMenu);

    this.setJMenuBar(menuBar);

    this.setLayout(null);
    this.add(panelTexto);
    this.add(textField);
    this.add(panelSimulacao);

    this.setSize(new Dimension(900, 650));
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.setFocusable(false);
    this.setVisible(true);
    this.setTitle("Whatsapp 3 - Potencia Maxima");
    this.setLocationRelativeTo(null);

    acaoDoTextField();
    renderizar();
  }

  /* *********************
  * Metodo: inicializar
  * Funcao: inicializa os objetos da classe
  * Parametros: nenhum
  * Retorno: void
  ********************* */
  private void inicializar(){
    textField = new JTextField();
    textField.setBounds(100, 500, 700, 60);
    textField.setFont(new Font("Arial", Font.BOLD, 20));
    textField.setVisible(false);

    controle = new Controlador(textField);

    display = new Display();
    panelTexto = new PanelTexto();
    panelTexto.setBounds(0, 0, 900, 50);
    panelSimulacao = new PanelSimulacao(display, panelTexto, controle);
    panelSimulacao.setBounds(0, 5, 900, 600);

    controle.setDir(panelSimulacao.getBarrinhaDir());
    controle.setEsq(panelSimulacao.getBarrinhaEsq());

    meioAplicacaoTransmissor = new MeioAplicacao(panelTexto.getTexto1(), controle);
    meioAplicacaoReceptor = new MeioAplicacao(panelTexto.getTexto2(), controle);
    meioEnlaceDeDadosTransmissor = new MeioEnlaceDeDados(meioAplicacaoTransmissor, controle);
    meioEnlaceDeDadosReceptor = new MeioEnlaceDeDados(meioAplicacaoReceptor, controle);
    meioFisicoTransmissor = new MeioFisico(meioEnlaceDeDadosTransmissor, controle);
    meioFisicoReceptor = new MeioFisico(meioEnlaceDeDadosReceptor, controle);
    meioTransmicao = new MeioTransmicao(meioFisicoTransmissor, meioFisicoReceptor, controle);

    menuBar = new JMenuBar();
  
    camadaFisicaMenu = new JMenu("C. Fisica");
    camadaEnlaceMenu = new JMenu("C. Enlace");

    mBinario = new JRadioButtonMenuItem("Binario");
    mBinario.setSelected(true);
    mManchester = new JRadioButtonMenuItem("Manchester");
    mDifManchester = new JRadioButtonMenuItem("Dif. Manchester");
    m1ContagemCaractere = new JRadioButtonMenuItem("C. Caracteres");
    m1ContagemCaractere.setSelected(true);
    m1InsercaoBytes = new JRadioButtonMenuItem("Insercao Bytes");
    m1InsercaoBits = new JRadioButtonMenuItem("Insercao Bits");
    m1ViolacaoFisica = new JRadioButtonMenuItem("Violacao C. Fisica");

    ButtonGroup bg = new ButtonGroup();
    bg.add(mBinario);
    bg.add(mManchester);
    bg.add(mDifManchester);

    ButtonGroup bg1 = new ButtonGroup();
    bg1.add(m1ContagemCaractere);
    bg1.add(m1InsercaoBytes);
    bg1.add(m1InsercaoBits);
    bg1.add(m1ViolacaoFisica);

    camadaFisicaMenu.add(mBinario);
    camadaFisicaMenu.add(mManchester);
    camadaFisicaMenu.add(mDifManchester);

    camadaEnlaceMenu.add(m1ContagemCaractere);
    camadaEnlaceMenu.add(m1InsercaoBytes);
    camadaEnlaceMenu.add(m1InsercaoBits);
    camadaEnlaceMenu.add(m1ViolacaoFisica);
  }

  /* *********************
  * Metodo: renderizar
  * Funcao: chama a funcao de renderizar de panelSimulacao
  * Parametros: nenhum
  * Retorno: void
  ********************* */
  private void renderizar(){
    panelSimulacao.renderizar();
  }

  /* *********************
  * Metodo: acaoDoTextField
  * Funcao: da a acao para o botao que envia as mensagens
  * Parametros: nenhum
  * Retorno: void
  ********************* */
  private void acaoDoTextField(){
    panelTexto.getOkBtn().addActionListener(l -> {
      if (panelTexto.getTexto1().getText()!= null){
        controle.setIsProcessing(true);
        meioAplicacaoTransmissor.codificarParaBits(meioAplicacaoTransmissor.getCaixaTexto().getText(), selectedIndex(0), selectedIndex(1));
        panelTexto.getTexto1().setText("");
      }
    });
  }

  /* *********************
  * Metodo: selectedIndex
  * Funcao: Funcao que verifica qual o index do menu esta selecionado baseado em um int que define qual item do menu eh
  * Parametros: int i
  * Retorno: int
  ********************* */
  private int selectedIndex(int i){
    switch (i){
      case 0:
        if (mBinario.isSelected())
          return 0;
        else if (mManchester.isSelected())
          return 1;
        else 
          return 2;
      case 1:
        if (m1ContagemCaractere.isSelected())
          return 0;
        else if (m1InsercaoBytes.isSelected())
          return 1;
        else if (m1InsercaoBits.isSelected())
          return 2;
        else 
          return 3;
    }
    return 0;
  }
}
