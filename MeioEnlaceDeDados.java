/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
  Autor: Daniel Nogueira
  Matricula: 201911910
  Inicio...: 18 de Marco de 2022
  Alteracao: 28 de Marco de 2022
  Nome.....: MeioEnlaceDeDados
  Funcao...: Classe que serve para simular o meio de enlace de dados
  =-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
public class MeioEnlaceDeDados {

  private int[] bitsArmazenados;       //bits armazenados nessa camada
  private MeioAplicacao meioAplicacao; //referencia ao meio de aplicacao
  private MeioFisico meioFisico;       //referencia ao meio fisico
  private int tamanhoDoQuadro;         //atributo do tamanho do quadro

  /* *********************
  * Metodo: MeioEnlaceDeDados
  * Funcao: Construtor
  * Parametros: MeioAplicacao meioAplicacao, Controlador controle
  ********************* */
  public MeioEnlaceDeDados(MeioAplicacao meioAplicacao, Controlador controle){
    this.meioAplicacao = meioAplicacao;
    meioAplicacao.setMeioEnlaceDeDados(this);
  }

  /* *********************
  * Metodo: camadaEnlaceDeDadosTransmissora
  * Funcao: funcao principal da camada que chama sub-funcoes
  * Parametros: int[] bits, int tipoCodificacao, int tipoDeEnlace
  * Retorno: void
  ********************* */
  public void camadaEnlaceDeDadosTransmissora(int[] bits, int tipoCodificacao, int tipoDeEnlace){
    this.bitsArmazenados = camadaEnlaceDeDadosTransmissoraEnquadramento(bits, tipoDeEnlace);

    int count = 0;
    for (int i=0; i<bitsArmazenados.length; i++){
      
    }

    meioFisico.codificacaoEspecifica(bitsArmazenados, tipoCodificacao, tipoDeEnlace);
  }

  /* *********************
  * Metodo: camadaEnlaceDeDadosTransmissoraEnquadramento
  * Funcao: funcao que chama as funcoes de enquadramento
  * Parametros: int[] bits, int tipoDeEnlace
  * Retorno: int[]
  ********************* */
  public int[] camadaEnlaceDeDadosTransmissoraEnquadramento(int[] bits, int tipoDeEnlace){
    int[] quadroEnquadrado = null;
    
    switch(tipoDeEnlace){
      case 0: //contagem de caractereres
        quadroEnquadrado = camadaEnlaceDeDadosTransmissoraEnquadramentoContagemDeCaracteres(bits);
        break;
      case 1: //insercao de bytes
        quadroEnquadrado = camadaEnlaceDeDadosTransmissoraEnquadramentoInsercaoDeBytes(bits);
        break;
      case 2: //insercao de bits
        quadroEnquadrado = camadaEnlaceDeDadosTransmissoraEnquadramentoInsercaoDeBits(bits);
        break;
      case 3: //violacao da camada fisica
        quadroEnquadrado = camadaEnlaceDeDadosTransmissoraEnquadramentoViolacaoDaCamadaFisica(bits);
        break;
    }

    return quadroEnquadrado;
  }

  /* *********************
  * Metodo: camadaEnlaceDeDadosTransmissoraEnquadramentoContagemDeCaracteres
  * Funcao: funcao que enquadra por contagem de caracteres
  * Parametros: int[] bits
  * Retorno: int[]
  ********************* */
  private int[] camadaEnlaceDeDadosTransmissoraEnquadramentoContagemDeCaracteres(int[] bits){
    int tamanhoDoQuadro = 16;
    int r = 0;
    if (bits.length % tamanhoDoQuadro == 0)
      r = bits.length / tamanhoDoQuadro;
    else 
      r = bits.length / tamanhoDoQuadro + 1;

    this.tamanhoDoQuadro = tamanhoDoQuadro+8; //define o tamanho do quadro geral
    int[] bitsFinal = new int[(r*8) + bits.length];

    int count = 0;
    int count2 = 0;
    int count3 = 16;
    int[] tres = {0,0,1,1,0,0,1,1}; //numero 3 em binario em ascii, pq ele define o tamanho do quadro em bytes
    for (int i=0; i<bitsFinal.length; i++){
      if (count == 8)
        count3 = 0;
      if (count < 8 && count3 == 16){
        bitsFinal[i] = tres[count];
        count++;
        count2++;
      }
      else {
        count = 0;   
        count3++;
        bitsFinal[i] = bits[i-count2];     
      }
      
    }

    return bitsFinal;
  }

  /* *********************
  * Metodo: camadaEnlaceDeDadosTransmissoraEnquadramentoInsercaoDeBytes
  * Funcao: funcao que enquadra por insercao de bytes
  * Parametros: int[] bits
  * Retorno: int[]
  ********************* */
  private int[] camadaEnlaceDeDadosTransmissoraEnquadramentoInsercaoDeBytes(int[] bits){
    int charCount = 0;

    int[] esc = {1,1,1,0,0,1,1,1};  //231 - Þ
    int[] flag = {1,1,1,0,1,0,0,0};  //232 - Þ

    for (int i=0; i<bits.length; i++){
      if (i % 16 == 0){
        charCount += 2;
      }
      if (isArrayEqual((getSubArrray(bits, i, i+8)), esc)){
        charCount++;
      }
      else if (isArrayEqual(getSubArrray(bits, i, i+8), flag)){
        charCount++;
      }
      i+=7;
    }

    if (bits.length % 16 != 0){
      charCount--;
    }

    int[] bitsFinal = new int[bits.length + (charCount*8)];

    int count = 0; //contador do byte de flag
    int count2 = 0; //contador dos bits
    int count3 = 0; //contador para inicar o check
    boolean check = true;

    for (int i=0; i<bitsFinal.length; i++){
      if (check == false && i % 32 == 0)
        check = true;
      if (check){
        bitsFinal[i] = flag[count];
        count++;
        if (count == 8){
          count = 0;
          check = false;
        }
      }
      else {
        if (isArrayEqual(getSubArrray(bits, count2, count2+8), flag) || isArrayEqual(getSubArrray(bits, count2, count2+8), esc)){
          bitsFinal[i] = esc[0];
          bitsFinal[i+1] = esc[1];
          bitsFinal[i+2] = esc[2];
          bitsFinal[i+3] = esc[3];
          bitsFinal[i+4] = esc[4];
          bitsFinal[i+5] = esc[5];
          bitsFinal[i+6] = esc[6];
          bitsFinal[i+7] = esc[7];
          i += 7;
          count2 += 8;
          count3 += 8;
        }
        else {
          bitsFinal[i] = bits[count2];
          count2++;
          count3++;

          if (count3 == 16){
            count3 = 0;
            check = true;
          }
        }
      }
    }

    return bitsFinal;
  }

  /* *********************
  * Metodo: camadaEnlaceDeDadosTransmissoraEnquadramentoInsercaoDeBits
  * Funcao: funcao que enquadra por insercao de bits
  * Parametros: int[] bits
  * Retorno: int[]
  ********************* */
  private int[] camadaEnlaceDeDadosTransmissoraEnquadramentoInsercaoDeBits(int[] bits){
    int[] flag = {0,1,1,1,1,1,1,0}; //6 bits 1 seguidos

    int frameCount = 0;
    int bitCount = 0;
    for (int i=0; i<bits.length; i++){
      if (isFive1(getSubArrray(bits, i, i+5))){
        bitCount++;
      }
      
    }        
    
    int[] bitsMeio = new int[bits.length + bitCount];

    int count = 0;  
    for (int i=0; i<bitsMeio.length; i++){
      if (isFive1(getSubArrray(bits, count, count+5))){
        bitsMeio[i] = bits[count];
        bitsMeio[i+1] = bits[count+1];
        bitsMeio[i+2] = bits[count+2];
        bitsMeio[i+3] = bits[count+3];
        bitsMeio[i+4] = bits[count+4];
        bitsMeio[i+5] = 0;
        i += 5;
        count += 5;
      }
      else {
        bitsMeio[i] = bits[count];
        count++;
      }
    }

    for (int i=0; i<bitsMeio.length; i++){
      if (i % 16 == 0){
        frameCount += 2;
      }
    }

    int[] bitsFinal = new int[bitsMeio.length + (8*frameCount)];

    count = 0;
    int count2 = 0;
    int count3 = 0;
    boolean check = true;

    for (int i=0; i<bitsFinal.length; i++){
      if (bitsFinal.length - i == 8)
        check = true;
      if (check == false && i % 32 == 0)
        check = true;

      if (check){
        bitsFinal[i] = flag[count];
        count++;
        if (count == 8){
          count = 0;
          check = false;
        }
      }
      else {
        bitsFinal[i] = bitsMeio[count2]; 
        count2++;
        count3++;

        if ((count3 == 16) || (bitsFinal.length - i < 8 && count2 == bitsMeio.length)){
          count3 = 0;
          check = true;
        }
          
      }
    }

    return bitsFinal;
  }

  /* *********************
  * Metodo: camadaEnlaceDeDadosTransmissoraEnquadramentoViolacaoDaCamadaFisica
  * Funcao: funcao que enquadra por violacao da camada fisica
  * Parametros: int[] bits
  * Retorno: int[]
  ********************* */
  public int[] camadaEnlaceDeDadosTransmissoraEnquadramentoViolacaoDaCamadaFisica(int[] bits){
    //desisti de fazer por conta do tempo
    return bits;
  }

  /* *********************
  * Metodo: camadaEnlaceDeDadosReceptora
  * Funcao: funcao geral do receptor para a camada de enlace
  * Parametros: int[] bits, int tipoDeEnlace
  * Retorno: void
  ********************* */
  public void camadaEnlaceDeDadosReceptora(int[] bits, int tipoDeEnlace){
    camadaEnlaceDeDadosReceptoraEquadramento(bits, tipoDeEnlace);
    
    meioAplicacao.decodificarParaString(bitsArmazenados);
  }

  /* *********************
  * Metodo: camadaEnlaceDeDadosReceptoraEnquadramento
  * Funcao: funcao que chama as funcoes de desenquadramento
  * Parametros: int[] bits, int tipoDeEnlace
  * Retorno: void
  ********************* */
  public void camadaEnlaceDeDadosReceptoraEquadramento(int[] bits, int tipoDeEnlace){
    switch (tipoDeEnlace){
      case 0:
        bitsArmazenados = camadaEnlaceDeDadosReceptoraEnquadramentoContagemDeCaracteres(bits);
        break;
      case 1:
        bitsArmazenados = camadaEnlaceDeDadosReceptoraEnquadramentoInsercaoDeBytes(bits);
        break;
      case 2:
        bitsArmazenados = camadaEnlaceDeDadosReceptoraEnquadramentoInsercaoDeBits(bits);
        break;
      case 3:
        bitsArmazenados = camadaEnlaceDeDadosReceptoraEnquadramentoViolacaoDaCamadaFisica(bits);
        break;
    }
  }

  /* *********************
  * Metodo: camadaEnlaceDeDadosReceptoraEnquadramentoContagemDeCaracteres
  * Funcao: funcao que desenquadra baseado em contagem de caracteres
  * Parametros: int[] bits
  * Retorno: int[]
  ********************* */
  private int[] camadaEnlaceDeDadosReceptoraEnquadramentoContagemDeCaracteres(int[] bits) {
    int r = 0;
    int[] num = new int[8];
    for (int i=0; i<8; i++){
      num[i] = bits[i];
    }

    int asciiValue = 0;
    int count = 0;
    for (int i=7; i>=0; i--){
      if (num[i] == 1){
        asciiValue += Math.pow(2, count);
      }
      count++;
    }

    asciiValue = asciiValue -48;
    
    for (int i=0; i<bits.length; i+=(asciiValue*8)){
      if (isArrayEqual((getSubArrray(bits, i, i+8)), num)){
        r++;
      }
    }    

    int[] bitsFinal = new int[bits.length - (r*8)];

    count = 0;
    for (int i=0; i<bits.length; i++){
      if (i % (asciiValue*8) == 0){
        i+=7;
        continue;
      }
      bitsFinal[count] = bits[i];
      count++;
    }

    return bitsFinal;
  }

  /* *********************
  * Metodo: camadaEnlaceDeDadosReceptoraEnquadramentoInsercaoDeBytes
  * Funcao: funcao que desenquadra baseado em insercao de bytes
  * Parametros: int[] bits
  * Retorno: int[]
  ********************* */
  private int[] camadaEnlaceDeDadosReceptoraEnquadramentoInsercaoDeBytes(int[] bits) {
    int[] esc = {1,1,1,0,0,1,1,1};  //231 - Þ
    int[] flag = {1,1,1,0,1,0,0,0};  //232 - Þ

    int charCount = 0;

    for (int i=0; i<bits.length; i+=8){
      if ((isArrayEqual((getSubArrray(bits, i, i+8)), esc)) || (isArrayEqual(getSubArrray(bits, i, i+8), flag)))
        charCount++;
    }

    int[] bitsFinal = new int[bits.length - (charCount*8)];

    int count = 0;
    for (int i=0; i<bits.length; i++){
      if (isArrayEqual((getSubArrray(bits, i, i+8)), esc) || isArrayEqual(getSubArrray(bits, i, i+8), flag)){
        i+=7;
      }
      else {
        bitsFinal[count] = bits[i];
        count++;
      }
    }

    return bitsFinal;
  }

  /* *********************
  * Metodo: camadaEnlaceDeDadosReceptoraEnquadramentoInsercaoDeBits
  * Funcao: funcao que desenquadra baseado em insercao de bits
  * Parametros: int[] bits
  * Retorno: int[]
  ********************* */
  private int[] camadaEnlaceDeDadosReceptoraEnquadramentoInsercaoDeBits(int[] bits) {
    int[] flag = {0,1,1,1,1,1,1,0}; //6 bits 1 seguidos

    int numBits = 0;
    int numFrames = 0;

    int count = 0;
    for (int i=0; i<bits.length; i++){
      if (count >= 0 && count < 8){
        i+=7;
        continue;
      }
      if (count >= 24 && count < 32){
        i+=7;
        count = 0;
        continue;
      }
      if (isFive1andOne0(getSubArrray(bits, i, i+6))){
        numBits++;
      }
      count++;
    }

    for (int i=0; i<bits.length; i+=8){
      if (isArrayEqual(getSubArrray(bits, i, i+8), flag)){
        numFrames++;
      }
    }

    int[] bitsMeio = new int[bits.length - (numBits)];

    count = 0;
    for (int i=0; i<bitsMeio.length; i++){
      if (isFive1andOne0(getSubArrray(bits, count, count+6))){
        bitsMeio[i] = bits[count];
        bitsMeio[i+1] = bits[count+1];
        bitsMeio[i+2] = bits[count+2];
        bitsMeio[i+3] = bits[count+3];
        bitsMeio[i+4] = bits[count+4];
        count += 5;
        i += 4;
      }
      else {
        bitsMeio[i] = bits[count];
        count++;
      }
    }

    int[] bitsFinal = new int[bitsMeio.length - (numFrames*8)];

    count = 0;
    for (int i=0; i<bitsFinal.length; i++){
      if (isArrayEqual(getSubArrray(bitsMeio, count, count+8), flag)){
        count += 8;
        i--;
      }
      else {
        bitsFinal[i] = bitsMeio[count];
        count++;
      }
    }

    return bitsFinal;
  }

  /* *********************
  * Metodo: camadaEnlaceDeDadosReceptoraEnquadramentoViolacaoDaCamadaFisica
  * Funcao: funcao que desenquadra baseado em violacao da camada fisica
  * Parametros: int[] bits
  * Retorno: int[]
  ********************* */
  private int[] camadaEnlaceDeDadosReceptoraEnquadramentoViolacaoDaCamadaFisica(int[] bits){
    //desisti por falta de tempo
    return bits;
  }

  //metodos ajudantes

  /* *********************
  * Metodo: isArrayEqual
  * Funcao: funcao que compara se dois arrays sao iguais
  * Parametros: int[] a, int[] b
  * Retorno: boolean
  ********************* */
  private static boolean isArrayEqual(int[] a, int[] b){
    if (a.length == b.length){
      for (int i=0; i<a.length; i++){
        if (a[i] != b[i]) 
          return false;
      }
    }
    else 
      return false;

    return true;
  }

  /* *********************
  * Metodo: getSubArray
  * Funcao: funcao que cria um novo array com base em posicoes de um array existente
  * Parametros: int[] array, int beggining, int ending
  * Retorno: int[]
  ********************* */
  private static int[] getSubArrray(int[] array, int beggining, int ending){
    int[] arrayFinal = new int[ending-beggining];

    int count = 0;
    for (int i=0; i<array.length; i++){
      if (i>= beggining && i<ending){
        arrayFinal[count] = array[i];
        count++;
      }
    }

    return arrayFinal;
  }

  /* *********************
  * Metodo: isFive1
  * Funcao: funcao que conta se um determinado array tem 5 zeros seguidos
  * Parametros: int[] bits
  * Retorno: boolean
  ********************* */
  private static boolean isFive1(int bits[]){
    if (bits[0] == 1 && bits[1] == 1 && bits[2] == 1 && bits[3] == 1 && bits[4] == 1)
      return true;
    return false;
  }

  /* *********************
  * Metodo: isFive1andOne0
  * Funcao: funcao que verifica se em um array existe um '0' depois de cinco '1' seguidos
  * Parametros: int[] bits
  * Retorno: boolean
  ********************* */
  private static boolean isFive1andOne0(int bits[]){
    if (bits[0] == 1 && bits[1] == 1 && bits[2] == 1 && bits[3] == 1 && bits[4] == 1 && bits[5] == 0)
      return true;
    return false;
  }

  //metodos getters e setters
  public MeioFisico getMeioFisico() {
    return meioFisico;
  }

  public void setMeioFisico(MeioFisico meioFisico) {
    this.meioFisico = meioFisico;
  }
}
