/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
  Autor: Daniel Nogueira
  Matricula: 201911910
  Inicio...: 03 de Agosto de 2021
  Alteracao: 28 de Marco de 2022
  Nome.....: MeioComunicacao
  Funcao...: Classe que serve para simular o meio da comunicacao
  =-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
public class MeioTransmicao {
  private MeioFisico meioFisico1, meioFisico2;  //referencias aos meios fisicos que essa classe conecta
  private Controlador controle;                 //referencia ao controlador de animacao

  /* *********************
  * Metodo: MeioComunicacao
  * Funcao: Construtor
  * Parametros: MeioFisico meioFisico1, MeioFisico meioFisico2, Controlador controle
  ********************* */
  public MeioTransmicao(MeioFisico meioFisico1, MeioFisico meioFisico2, Controlador controle){
    this.meioFisico1 = meioFisico1;
    this.meioFisico1.setMeioTransmicao(this);
    this.meioFisico2 = meioFisico2;
    this.meioFisico2.setMeioTransmicao(this);
    this.controle = controle;
  }

  /* *********************
  * Metodo: transmitir
  * Funcao: Funcao que passa os bits de um MeioFisico para o outro
  * Parametros: int tipoCOdificacao
  * Retorno: void
  ********************* */
  public void transmitir(int tipoDeCodificacao, int tipoDeEnlace){
    meioFisico2.setBitsArmazenados(new int[meioFisico1.getBitsArmazenados().length]);
    controle.getTextField().setText("");
    for (int i=0; i<meioFisico1.getBitsArmazenados().length; i++){
      if (i==0)
        visualizarTransmissao(0, meioFisico1.getBitsArmazenados()[i]);
      else 
        visualizarTransmissao(meioFisico1.getBitsArmazenados()[i-1], meioFisico1.getBitsArmazenados()[i]);
      meioFisico2.getBitsArmazenados()[i] = meioFisico1.getBitsArmazenados()[i];
    }

    if (meioFisico1.isViolacaoTrue()){
      meioFisico2.setViolacaoTrue(true);
      meioFisico1.setViolacaoTrue(false);
    }
      
    //passa para a proxima etapa do envio da mensagem
    meioFisico2.decodificaoEspecifica(tipoDeCodificacao, tipoDeEnlace);
  }

  /* *********************
  * Metodo: visualizarTransmissao
  * Funcao: Funcao que atualiza a caixa de texto
  * Parametros: int i
  * Retorno: void
  ********************* */
  public void visualizarTransmissao(int i,int j) {
    try {
      Thread.sleep(50);
    } catch (InterruptedException e){}


    if (i==0){
      if (j==0){
        preencherTextArea("_");
      }
      else {
        preencherTextArea("|");
        preencherTextArea("¯");
      }
    }
    else {
      if (j==0){
        preencherTextArea("|");
        preencherTextArea("_");
      }
      else {
        preencherTextArea("¯");
      }
    }
  }

  /* *********************
  * Metodo: preencherTextArea
  * Funcao: Concatena uma string no textField
  * Parametros: String s
  * Retorno: void
  ********************* */
  public void preencherTextArea(String s){
    controle.getTextField().setText(controle.getTextField().getText().concat(s));
  }
}
