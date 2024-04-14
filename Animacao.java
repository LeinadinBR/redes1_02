/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
  Autor: Daniel Nogueira
  Matricula: 201911910
  Inicio...: 31 de Julho de 2021
  Alteracao: 04 de Agosto de 2021
  Nome.....: Animacao
  Funcao...: Classe que serve para fazer a animacao dos sprites das barrinhas
  =-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
public class Animacao {
  private int imagemAtual=0;      //int que indica qual imagem do array eh a atual
  private Sprite[] imagens;    //array de 'Sprite' que tem as imagens para a animacao
  private TickAnimacao tickAnimacao;      //objeto da classe 'TickAnimacao' que serve de temporizador
  private boolean isRunning = false;    //boolean para definir se a animacao comecou ou nao
  private int numImagens;           //indica a quantidade de imagens na animacao
  private Boolean enviandoDados = false;  //boolean para indicar se deve animar ou mostrar imagem estatica

  /* *********************
  * Metodo: Animacao
  * Funcao: Construtor de animacao
  * Parametros: String[] paths, int numImagens
  ********************* */
  public Animacao(String[] paths, int numImagens){
    this.numImagens = numImagens;

    imagens = new Sprite[numImagens];
    for (int i=0; i<numImagens; i++){
      imagens[i] = new Sprite(paths[i]);
    }
    tickAnimacao = new TickAnimacao();
  }

  /* *********************
  * Metodo: ligarTickAnimacao
  * Funcao: inicia a thread tickAnimacao que altera os sprites
  * Parametros: nenhum
  * Retorno: void
  ********************* */
  public void ligarTickAnimacao(){
    tickAnimacao.start();
    isRunning = true;
  }

  //Getters e setters--------------------------//
  public Sprite getImagem(){
    return imagens[imagemAtual];
  }

  public boolean isRunning() {
    return isRunning;
  }

  public void setRunning(boolean isRunning) {
    this.isRunning = isRunning;
  } 

  public void setEnviandoDados(Boolean enviandoDados){
    this.enviandoDados = enviandoDados;
  }

  public Boolean getEnviandoDados(){
    return this.enviandoDados;
  }
  //-------------------------------------------//
  
  /*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
  Autor: Daniel Nogueira
  Matricula: 201911910
  Inicio...: 31 de Julho de 2021
  Alteracao: 04 de Agosto de 2021
  Nome.....: TickAnimacao
  Funcao...: Classe criada para ajudar a classe 'Animacao' na questao do tempo de troca de sprites
  =-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
  class TickAnimacao extends Thread {
    /* *********************
    * Metodo: run
    * Funcao: metodo que sera responsavel pelo algoritmo de 'TickAnimacao'
    * Parametros: nenhum
    * Retorno: void
    ********************* */
    @Override
    public void run(){
      while (true){
        try {
          //esse if verifica qual tipo de padrao da animacao (troca de sprites) seguir
          if (numImagens>3){
            sleep(350);
            if (!enviandoDados)
              imagemAtual = 0;
            else{
              if (imagemAtual < numImagens-1){
                imagemAtual++;
              }
              else 
                imagemAtual = 1;
            }
          }
          else {
            sleep(130);
            if (!enviandoDados)
              imagemAtual = 0;
            else {
              if (imagemAtual<numImagens-1){
                imagemAtual++;
              }
              else {
                imagemAtual = 1;
              }
            }
          }
        }
        catch(InterruptedException e){
        }
      }
    }
  }


}
