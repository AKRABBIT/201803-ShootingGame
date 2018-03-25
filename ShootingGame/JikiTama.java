import java.awt.image.BufferedImage;

public class JikiTama extends GameChara {
	// コンストラクタ
	public JikiTama(int x, int y, BufferedImage img) {
		// 弾画像の範囲をあらかじめ当たり領域として引数に渡す
		super(x, y, 12, 12, img, 240, 0, 12, 12);
	}

	// 前へ移動
	public void move() {
		chara_x = chara_x + 12;
	}

}
