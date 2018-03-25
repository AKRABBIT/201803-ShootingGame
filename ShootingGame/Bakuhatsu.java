import java.awt.image.BufferedImage;

public class Bakuhatsu extends GameChara {
	int waittime;

	// コンストラクタ
	public Bakuhatsu(int x, int y, BufferedImage img) {
		super(x, y, 0, 0, img, 192, 0, 48, 48);
		waittime = 10;
	}

	// 外に出たか判定するメソッド
	public boolean isSotoNiDeta(){
		if (waittime <0){
			return true;
		} else {
			return false;
		}
	}
	// 移動
	public void move() {
		waittime = waittime-1;
	}
}
