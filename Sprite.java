import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
Autor: Daniel Nogueira
Matricula: 201911910
Inicio...: 30 de Julho de 2021
Alteracao: 04 de Agosto de 2021
Nome.....: Sprite
Funcao...: Tem a funcao de criar um objeto que carregue uma imagem
=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
public class Sprite {

  private BufferedImage spriteSheet; //objeto de BufferedImage que tem a imagem

  /* *********************
  * Metodo: Sprite
  * Funcao: Construtor de Sprite
  * Parametros: String path
  ********************* */
  public Sprite(String path){
    try {
			spriteSheet = ImageIO.read(getClass().getResource(path));  //le o caminho da imagem e retorna a imagem para BufferedImage
		}
    catch(IOException e){
    }  //Ocorre caso o arquivo nao seja encontrado
  }

  //getter para pegar uma subimagem
  public BufferedImage getSprite(int x , int y , int largura , int altura) {
		return spriteSheet.getSubimage(x, y, largura, altura);
	}

  //getter para pegar a imagem inteira
  public BufferedImage getSprite(){
    return spriteSheet;
  }
}
