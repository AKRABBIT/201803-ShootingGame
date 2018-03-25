import java.awt.image.BufferedImage;

public class Jiki extends GameChara {

	// コンストラクタ
	// 自機画像の範囲をコンストラクタの引数に設定する
	public Jiki(int x, int y, BufferedImage img) {
		super(x, y, 12, 12, img, 0, 0, 48, 48);
	}

	// 抽象クラスで定義されたメソッド
	public void move() {
	}

	// 移動メソッド
	// 上、下、右、左の４方向
	public void move(boolean up,boolean down,boolean right,boolean left){
		// それぞれのフラグによる移動
		if (up    ==true)	chara_y = chara_y-8;
		if (down  ==true)	chara_y = chara_y+8;
		if (right ==true)	chara_x = chara_x+8;
		if (left  ==true)	chara_x = chara_x-8;

		// 移動位置の制御を行う
		if (chara_y<0)	chara_y = 0;
		if (chara_x<0)	chara_x = 0;
		if (chara_y>UtyuuShootMain.GAMEN_H-48)	chara_y = UtyuuShootMain.GAMEN_H-48;
		if (chara_x>UtyuuShootMain.GAMEN_W-48)	chara_x = UtyuuShootMain.GAMEN_W-48;
	}

}
