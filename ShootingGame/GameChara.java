import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public abstract class GameChara {
	// 全てのゲームキャラの基本となるクラス
	// 判定領域に関するフィールド
	public static final int AREA_X1 = -48; // 左上のX座標
	public static final int AREA_Y1 = -48; // 左上のY座標
	public static final int AREA_X2 = UtyuuShootMain.GAMEN_W + 48; // 右下のX座標 画面サイズに一定値を加算
	public static final int AREA_Y2 = UtyuuShootMain.GAMEN_H + 48; // 右下のY座標 画面サイズに一定値を加算

	public int chara_x, chara_y;
	int image_x, image_y, image_w, image_h;
	int atari_w, atari_h;// 当たり藩邸領域の大きさ
	BufferedImage image1;

	GameChara(int x,int y, int aw,int ah, BufferedImage img,
			int ix,int iy, int iw,int ih){
		chara_x = x;
		chara_y = y;
		atari_w = aw;
		atari_h = ah;
		image1  = img;
		image_x = ix;
		image_y = iy;
		image_w = iw;
		image_h = ih;
	}

	// 描画
	public void draw(Graphics g,ImageObserver io){
		g.drawImage(image1, chara_x, chara_y, chara_x + image_w, chara_y + image_h,
				image_x, image_y, image_x+image_w, image_y+image_h, io);
		move();
	}

	// 移動クラスを抽象クラスとして定義しておく
	public abstract void move();

	// 当たり判定領域の計算を求める
	public int getx1(){
		return chara_x + ( image_w-atari_w ) / 2;
	}
	public int gety1(){
		return chara_y + ( image_h-atari_h ) / 2;
	}
	public int getx2(){
		return chara_x + ( image_w+atari_w ) / 2;
	}
	public int gety2(){
		return chara_y + ( image_h+atari_h ) / 2;
	}

	// 座標位置による当たり判定を行う
	public boolean isAtari(GameChara aite){
		if( aite.getx2() > getx1() && getx2() > aite.getx1() &&
				aite.gety2() > gety1() && gety2() > aite.gety1() ){
			return true;
		} else {
			return false;
		}
	}

	// 外に出たか判定するメソッド
	// プレイヤーからの見え方を考慮し、判定領域は画面サイズよりも大きい
	public boolean isSotoNiDeta(){
		if ( chara_x < AREA_X1 || chara_y < AREA_Y1 ||
				chara_x + image_w > AREA_X2 || 	chara_y + image_w > AREA_Y2 ){
			return true;
		} else {
			return false;
		}
	}
}