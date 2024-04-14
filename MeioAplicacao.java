import javax.swing.JTextField;

/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
  Autor: Daniel Nogueira
  Matricula: 201911910
  Inicio...: 03 de Agosto de 2021
  Alteracao: 04 de Agosto de 2021
  Nome.....: MeioAplicacao
  Funcao...: Classe que serve para simular o meio da aplicacao
  =-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
public class MeioAplicacao {
  private MeioEnlaceDeDados meioEnlaceDeDados; //
  private JTextField caixaTexto;  //referencia a caixa de texto dessa unidade
  private Controlador controle;   //referencia ao controle da animacao

  /* *********************
  * Metodo: MeioAplicacao
  * Funcao: Construtor
  * Parametros: JTextField caixaTexto, Controlador controle
  ********************* */
  public MeioAplicacao(JTextField caixaTexto, Controlador controle){
    this.caixaTexto = caixaTexto;
    this.controle = controle;
  }

  /* *********************
  * Metodo: codificarParaBits
  * Funcao: Funcao que comeca o processo de transformacao para bits
  * Parametros: String mensagem, int tipoCodificacao
  * Retorno: void
  ********************* */
  public void codificarParaBits(String mensagem, int tipoCodificacao, int tipoDeEnlace){
    controle.getEsq().setEnviandoDados(true);       //seta true para que a animacao comece
    int[][] arrayDeBits = new int[mensagem.length()][8]; //arranjo que ira se armazenar os bits a principio
    int[] arrayDeAscII = new int[mensagem.length()];    //arranjo que ira se armazenar os valores em ASCII
    int[] arrayAux = {128, 64, 32, 16, 8, 4, 2, 1};     //arranjo auxiliar para calcular os bits

    //adquire os valores em ASCII
    for (int i=0; i<arrayDeAscII.length; i++){
      arrayDeAscII[i] = (int) mensagem.charAt(i);
    }

    //calcula-se os bits para cada caractere
    for (int i=0; i<arrayDeAscII.length; i++){
      for (int j=0; j<8; j++){
        if (arrayDeAscII[i] >= arrayAux[j]){
          arrayDeAscII[i] -= arrayAux[j];
          arrayDeBits[i][j] = 1;
        }
      }
    }

    //passa os bits para um arranjo uni-dimensional
    int[] arrayBitsFinal = new int[mensagem.length()*8];
    int count = 0;
    for (int i=0; i<mensagem.length(); i++){
      for (int j=0; j<8; j++){
        arrayBitsFinal[count] = arrayDeBits[i][j];
        count++;
      }
    }

    //chama a proxima parte da simulacao
    meioEnlaceDeDados.camadaEnlaceDeDadosTransmissora(arrayBitsFinal, tipoCodificacao, tipoDeEnlace);
  }

  /* *********************
  * Metodo: decodificarParaString
  * Funcao: Funcao que transforma os bits em uma String
  * Parametros: int[] bits
  * Retorno: void
  ********************* */
  public void decodificarParaString(int[] bits){
    String mensagem = "";     //String que sera construida
    int[] ascii = new int[bits.length/8];   //arranjo para os valores em ASCII
    int[] arrayAux = {128, 64, 32, 16, 8, 4, 2, 1};  //arranjo auxiliar para decifrar os bits

    int count = 0;  //acumula o valor em ASCII
    int count2 = 0; //conta os bits de 8 em 8
    int count3 = 0; //conta os indices do arranjo em ASCII
    for (int i=0; i<bits.length; i++){
      if (bits[i] == 1)
        count += arrayAux[count2];
      count2++;
      if (count2 == 8){
        ascii[count3] = count;
        count = 0;
        count2 = 0;
        count3++;
      }
    }

    //pega os valores em ASCII e transforma em char para depois ser concatenado na string
    char c;
    for (int i=0; i<ascii.length; i++){
      c = (char) ascii[i];
      mensagem += String.valueOf(c);
    }

    controle.getDir().setEnviandoDados(false);  //seta false para que a animacao termine
    caixaTexto.setText(mensagem);   //envia a mensagem para a caixa de texto
    controle.setIsProcessing(false);
  }


  //metodos getters e setters
  public JTextField getCaixaTexto() {
    return caixaTexto;
  }

  public void setCaixaTexto(JTextField caixaTexto) {
    this.caixaTexto = caixaTexto;
  }

  public MeioEnlaceDeDados getMeioEnlaceDeDados() {
    return meioEnlaceDeDados;
  }

  public void setMeioEnlaceDeDados(MeioEnlaceDeDados meioEnlaceDeDados) {
    this.meioEnlaceDeDados = meioEnlaceDeDados;
  }
}
