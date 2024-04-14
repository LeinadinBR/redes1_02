/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
  Autor: Daniel Nogueira
  Matricula: 201911910
  Inicio...: 03 de Agosto de 2021
  Alteracao: 28 de Marco de 2022
  Nome.....: MeioFisico
  Funcao...: Classe que serve para simular o meio fisico
  =-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
public class MeioFisico {
  private MeioTransmicao meioTransmicao;  //referencia ao meio de comunicacao
  private MeioEnlaceDeDados meioEnlaceDeDados; //
  private int[][] bitsArmazenados;            //arranjo que representa uma memoria de curto prazo dos bits
  private Controlador controle;             //referencia ao controle de animacao
  private boolean isViolacaoTrue = false;   //boolean para definir se havera quebra da camada fisica

  /* *********************
  * Metodo: MeioFisico
  * Funcao: Construtor
  * Parametros: MeioAplicacao meioAplicacao, Controlador controle
  ********************* */
  public MeioFisico(MeioEnlaceDeDados meioEnlaceDeDados, Controlador controle){
    this.meioEnlaceDeDados = meioEnlaceDeDados;
    meioEnlaceDeDados.setMeioFisico(this);
    this.controle = controle;
  }

  /* *********************
  * Metodo: codificacaoEspecifica
  * Funcao: Funcao que passa chama as funcoes de transformacao em bits
  * Parametros: int[] bits, int tipoCodificacao
  * Retorno: void
  ********************* */
  public void codificacaoEspecifica(int[][] bits, int tipoCodificacao, int tipoDeEnlace){
    dormir(5);  //chama a funcao para que a animacao possa ser percebida

    //de acordo com o tipo de codificacao ele usa um algoritmo diferente
    switch (tipoCodificacao){
      case 0:
        bitsArmazenados = bits; //salva os bits na memoria, ja que nao precisa de nenhum algoritmo especifico
        controle.getEsq().setEnviandoDados(false);  //seta para false para que possa encerrar a animacao
        meioTransmicao.transmitir(tipoCodificacao, tipoDeEnlace);  //chama o proximo passo do envio
        break;
      case 1:
        bitsArmazenados = manchester(bits); //salva os bits na memoria depois de passar pelo algoritmo de manchester
        controle.getEsq().setEnviandoDados(false);  //seta para false para que possa encerrar a animacao
        meioTransmicao.transmitir(tipoCodificacao, tipoDeEnlace);  //chama o proximo passo do envio
        break;
      case 2:
        bitsArmazenados = manchesterDiferencial(bits);  //salva os bits na memoria depois de passar pelo algoritmo de manchester diferencial
        controle.getEsq().setEnviandoDados(false);  //seta para false para que possa encerrar a animacao
        meioTransmicao.transmitir(tipoCodificacao, tipoDeEnlace);  //chama o proximo passo do envio
        break;
    }
  }

  /* *********************
  * Metodo: manchester
  * Funcao: Funcao que transforma um fluxo de bits normal em um fluxo de bits manchester
  * Parametros: int[] input
  * Retorno: int[]
  ********************* */
  private int[] manchester(int[] input){
    //cria um arranjo 2 vezes maior
    int[] output = null; 
    
    output = new int[input.length*2];
    int count = 0;
    for (int i=0; i<input.length; i++){
      if (input[i] == 0){
        output[count] = 0;
        count++;
        output[count] = 1;
        count++;
      }
      else {
        output[count] = 1;
        count++;
        output[count] = 0;
        count++;
      }
    }
    
    return output;
  }

  /* *********************
  * Metodo: manchesterDiferencial
  * Funcao: Funcao que transforma um fluxo de bits normal em um fluxo de bits manchester diferencial
  * Parametros: int[] input
  * Retorno: int[]
  ********************* */
  private int[] manchesterDiferencial(int[] input){
    //cria um arranjo duas vezes maior
    int[] output = new int[input.length*2];

    //acomoda os valores iniciais antes do loop
    int count = 0;
    if (input[0] == 1){
      output[count] = 1;
      count++;
      output[count] = 0;
      count ++;
    }
    else {
      output[count] = 0;
      count++;
      output[count] = 1;
      count ++;
    }

    //parte principal onde calcula com base no bit anterior
    for (int i=1; i<input.length; i++){
      for (int j=1; j<2; j++){
        if (input[i] == 1){
          if (output[count-1] == 1){
            output[count] = 1;
            count++;
            output[count] = 0;
            count++;
          }
          else if (output[count-1] == 0){
            output[count] = 0;
            count++;
            output[count] = 1;
            count++;
          }
        }
        else if (input[i] == 0){
          if (output[count-1] == 1){
            output[count] = 0;
            count++;
            output[count] = 1;
            count++;
          }
          else if (output[count-1] == 0){
            output[count] = 1;
            count++;
            output[count] = 0;
            count++;
          }
        }
      }
    }
    return output;
  }

  /* *********************
  * Metodo: decodificaoEspecificada
  * Funcao: Funcao que chama as funcoes de decodificacao
  * Parametros: int tipoCOdificacao
  * Retorno: void
  ********************* */
  public void decodificaoEspecifica(int tipoCodificacao, int tipoDeEnlace) {
    controle.getDir().setEnviandoDados(true); //seta para true para que possa comecar a animacao
    dormir(4);  //faz a thread parar para que a animacao possa ser percebida

    switch(tipoCodificacao){
      case 0:
        //chama o proximo passo do envio 
        meioEnlaceDeDados.camadaEnlaceDeDadosReceptora(bitsArmazenados, tipoDeEnlace);
        break;
      case 1:
        //chama o proximo passo do envio depois de decodificar para manchester
        meioEnlaceDeDados.camadaEnlaceDeDadosReceptora(decodificarManchester(), tipoDeEnlace);
        break;
      case 2:
        //chama  o proximo passo do envio depois de decodificar para manchester diferencial
        meioEnlaceDeDados.camadaEnlaceDeDadosReceptora(decodificarManchesterDiferencial(), tipoDeEnlace);
        break;
    }
  }

  /* *********************
  * Metodo: decodificarManchester
  * Funcao: Funcao que decodifica um fluxo em manchester para um fluxo normal de bits
  * Parametros: Nenhum
  * Retorno: int[]
  ********************* */
  public int[] decodificarManchester(){
    //cria um arranjo com metado do tamanho
    int[] output = null; 
  
    output = new int[bitsArmazenados.length/2];
    int count = 0;
    for (int i=0; i<bitsArmazenados.length; i+=2){
      if (bitsArmazenados[i] == 1 && bitsArmazenados[i+1] == 0)
        output[count] = 1;
      else if (bitsArmazenados[i] == 0 && bitsArmazenados[i+1] == 1)
        output[count] = 0;
      count++;
    } 
    
    return output;
  }

  /* *********************
  * Metodo: decodificarManchesterDiferencial
  * Funcao: Funcao que decodifica um fluxo em manchester diferencial para um fluxo normal de bits
  * Parametros: Nenhum
  * Retorno: int[]
  ********************* */
  public int[] decodificarManchesterDiferencial(){
    //cria um arranjo com metade do tamanho
    int[] output = new int[bitsArmazenados.length/2];

    //decodifica os dois primeiros bits que nao seguem o padrao
    int count = 0;
    if (bitsArmazenados[count] == 1 && bitsArmazenados[count+1] == 0){
      output[count] = 1;
      count++;
    }
    else if (bitsArmazenados[count] == 0 && bitsArmazenados[count+1] == 1){
      output[count] = 0;
      count++;
    }
    //parte principal
    for (int i=2; i<bitsArmazenados.length; i+=2){
      if (bitsArmazenados[i-1] == 1){
        if (bitsArmazenados[i] == 1)
          output[count] = 1;
        else if (bitsArmazenados[i]==0)
          output[count] = 0;
      }
      else if (bitsArmazenados[i-1] == 0){
        if (bitsArmazenados[i] == 0)
          output[count] = 1;
        else if (bitsArmazenados[i] == 1)
          output[count] = 0;
      }
      count++;
    }
    return output;
  }

  /* *********************
  * Metodo: dormir
  * Funcao: Funcao para parar a Thread
  * Parametros: int i
  * Retorno: void
  ********************* */
  private void dormir(int i){
    try {
      Thread.sleep(100 * i);
    }
    catch (InterruptedException e){
    }
  }

  //metodos getters e setters
  public MeioTransmicao getMeioTransmicao() {
    return meioTransmicao;
  }

  public void setMeioTransmicao(MeioTransmicao meioTransmicao) {
    this.meioTransmicao = meioTransmicao;
  }

  public int[] getBitsArmazenados() {
    return bitsArmazenados;
  }

  public void setBitsArmazenados(int[] bitsArmazenados) {
    this.bitsArmazenados = bitsArmazenados;
  }

  public boolean isViolacaoTrue() {
    return isViolacaoTrue;
  }

  public void setViolacaoTrue(boolean isViolacaoTrue) {
    this.isViolacaoTrue = isViolacaoTrue;
  }  
}
