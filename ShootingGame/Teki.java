import java.awt.Point;
import java.awt.image.BufferedImage;

public class Teki extends GameChara {
	// 複数の敵を作るためPatternReaderクラスを利用
	// 全キャラクターの画像を一つにまとめ、切り取って表示することで効率化
	// 画像は参考にしたサンプルゲームより抜粋
	PatternReader preader;
	int hanten;

	// コンストラクタ
	public Teki(int x, int y, BufferedImage img, int ptnum, String patstr) {
		super(x, y, 40, 40, img, 48+ptnum*48, 0, 48, 48);

		preader = new PatternReader(patstr);
		if (chara_y > UtyuuShootMain.GAMEN_H / 2) {
			hanten = -1;
		} else {
			hanten = 1;
		}

	}

	// 切り取り位置の移動
	// 次の画像を読み込む
	public void move() {
		Point movexy = preader.next();
		chara_x = chara_x + movexy.x;
		chara_y = chara_y + movexy.y*hanten;
	}
}
