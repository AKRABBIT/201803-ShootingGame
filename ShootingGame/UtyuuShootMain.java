import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

public class UtyuuShootMain extends GameBase {
	public static final int GAMEN_W = 640;	//画面の幅
	public static final int GAMEN_H = 480;	//画面の高さ
	public static final int TEKI_RATE = 20;
	public static final int JIKAN_SEIGEN = 33000;	//1ステージは約30秒
	public static final String[] TEKIPATTERN = {"-4,0,60,0,0,30",
			"-2,-4,10,-2,-3,5,-2,-2,5,-2,0,5,-2,2,5,-2,3,5,-2,4,10,-2,3,5,-2,2,5,-2,0,5,-2,-2,5,-2,-3,5",
			"-2,0,10,-8,0,60,-2,0,10,4,3,60"};

	BufferedImage charaimage, backimage, startimage;
	Jiki jiki;
	boolean upkey, downkey, rightkey, leftkey, shiftkey;
	ArrayList jikitamas, tekis, tekitamas, bakuhatsus;
	int rensya   = 0;
	int tekikazu = 1;
	int tekitamarate = 150;
	long starttime;
	int score, highscore, stagenum = 1;

	public UtyuuShootMain() {
		super(GAMEN_W, GAMEN_H, "シューティングゲーム～宇宙～");

		try {
			// 画像ファイル取得
			// サンプルプログラムから拝借
			charaimage = ImageIO.read(getClass().getResource("shooting.png"));
			backimage = ImageIO.read(getClass().getResource("back.jpg"));
			startimage = ImageIO.read(getClass().getResource("start.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// タイマースタート
		goStartGamen();
	}

	// 画面切り替え
	public void initStageStart() {
		// キャラクターのインスタンス生成
		jiki = new Jiki(GAMEN_W / 2, GAMEN_H / 2, charaimage); // 自機を中央へ表示
		jikitamas  = new ArrayList();
		tekis      = new ArrayList();
		tekitamas  = new ArrayList();
		bakuhatsus = new ArrayList();

		// キー初期化
		upkey    = false;
		downkey  = false;
		rightkey = false;
		leftkey  = false;
		starttime = System.currentTimeMillis();

	}

	public void initStageClear() {
		stagenum = stagenum+1;
		tekikazu = tekikazu+1;
		if (tekikazu>15){
			tekikazu = 1;
			tekitamarate = tekitamarate-50;
		}
		if(tekitamarate<20) tekitamarate=20;
		score = score + stagenum * 100;
	}

	public void initGameOver() {
		if (score>highscore) highscore = score;
		score = 0;
	}

	// キーが押されたとき
	public void keyPressedGameMain(int key) {
		if (key == KeyEvent.VK_UP) 	 upkey   = true;
		if (key == KeyEvent.VK_DOWN) 	 downkey = true;
		if (key == KeyEvent.VK_LEFT) 	 leftkey = true;
		if (key == KeyEvent.VK_RIGHT) rightkey = true;
		if (key == KeyEvent.VK_SHIFT) shiftkey = true;
	}

	// キーが離されたとき
	public void keyReleasedGameMain(int key) {
		if (key == KeyEvent.VK_UP) 	upkey   = false;
		if (key == KeyEvent.VK_DOWN) 	downkey = false;
		if (key == KeyEvent.VK_LEFT)  leftkey  = false;
		if (key == KeyEvent.VK_RIGHT) rightkey = false;
		if (key == KeyEvent.VK_SHIFT) shiftkey = false;
	}

	public void runStartGamen(Graphics g) {
		g.drawImage(startimage,0,0,frame1);
	}

	public void runStageStart(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0,0,GAMEN_W,GAMEN_H);
		g.setColor(Color.CYAN);
		g.setFont(new Font("SansSerif",Font.BOLD,60));
		drawStringCenter("ステージ" + stagenum,200,g);
		drawStringCenter("スタート",280,g);
	}

	// ステージクリア時
	public void runStageClear(Graphics g) {
		g.setColor(Color.CYAN);
		g.setFont(new Font("SansSerif",Font.BOLD,60));
		drawStringCenter("ステージクリア",200,g);
	}

	// ゲームプレイ中の処理はここで行う
	public void runGameMain(Graphics g) {
		g.drawImage(backimage,0,0,frame1);

		g.clearRect(0, 0, GAMEN_W, GAMEN_H); //

		//自機移動
		jiki.move(upkey,downkey,leftkey,rightkey);

		//自弾発射
		if (shiftkey == true && rensya==0){
			jikitamas.add(new JikiTama(jiki.chara_x+36,
					jiki.chara_y+24,charaimage));
			rensya = 10;
		}
		if (rensya>0) rensya = rensya-1;

		//敵出現
		if (tekis.size() < tekikazu && Math.random()*TEKI_RATE<1){
			int ptnum = (int)(Math.random()*3);
			int ty = (int)(Math.random()*23)*20;
			tekis.add(new Teki(GAMEN_W, ty,
					charaimage, ptnum, TEKIPATTERN[ptnum]));
		}

		//敵弾発射
		Iterator it = tekis.iterator();
		while(it.hasNext()==true){
			Teki tk = (Teki)it.next();
			if (Math.random()*tekitamarate<1){
				tekitamas.add(new TekiTama(tk.chara_x-8, tk.chara_y+24,
						jiki.chara_x, jiki.chara_y, charaimage));
			}
		}

		// キャラクター画面表示
		jiki.draw(g, frame1);
		it = jikitamas.iterator();
		while(it.hasNext()==true){
			JikiTama jt = (JikiTama)it.next();
			jt.draw(g,frame1);
			if(jt.isSotoNiDeta()==true) it.remove();
		}
		it = tekis.iterator();
		while(it.hasNext()==true){
			Teki tk = (Teki)it.next();
			tk.draw(g,frame1);
			if(tk.isSotoNiDeta()==true) it.remove();
		}
		it = tekitamas.iterator();
		while(it.hasNext()==true){
			TekiTama tm = (TekiTama)it.next();
			tm.draw(g,frame1);
			if(tm.isSotoNiDeta()==true) it.remove();
		}
		it = bakuhatsus.iterator();
		while(it.hasNext()==true){
			Bakuhatsu bh = (Bakuhatsu)it.next();
			bh.draw(g,frame1);
			if(bh.isSotoNiDeta()==true) it.remove();
		}

		//当たり判定（敵と自機弾）
		it = tekis.iterator();
		while(it.hasNext()==true){
			Teki tk = (Teki)it.next();
			Iterator it2 = jikitamas.iterator();
			while(it2.hasNext()==true){
				JikiTama jt = (JikiTama)it2.next();
				if(tk.isAtari(jt)==true) {
					bakuhatsus.add(new Bakuhatsu(tk.chara_x, tk.chara_y, charaimage));
					it.remove();
					it2.remove();
					score = score+10;
					break;
				}
			}
		}
		//当たり判定（自機と敵、敵弾）
		it = tekis.iterator();
		while(it.hasNext()==true){
			Teki tk = (Teki)it.next();
			if(tk.isAtari(jiki)){
				goGameOver();
				break;
			}
		}
		it = tekitamas.iterator();
		while(it.hasNext()==true){
			TekiTama tt = (TekiTama)it.next();
			if(tt.isAtari(jiki)){
				goGameOver();
				break;
			}
		}

		//残り時間のチェック
		long jikansa = System.currentTimeMillis() - starttime;
		if(jikansa > JIKAN_SEIGEN) goStageClear();

		//スコア表示
		g.setColor(Color.YELLOW);
		g.fillRect(150,4,(int)((JIKAN_SEIGEN - jikansa ) / 60), 4);
		g.setColor(Color.WHITE);
		g.setFont(new Font("SansSerif", Font.PLAIN, 10));
		g.drawString("スコア:" + score + " ハイスコア:"+ highscore , 2, 10);

	}

	// ゲームオーバー時
	public void runGameOver(Graphics g) {
		g.setColor(Color.RED);
		g.setFont(new Font("SansSerif", Font.BOLD,80));
		drawStringCenter("GAMEOVER", 220, g);
		g.setFont(new Font("SansSerif", Font.PLAIN ,24));
		drawStringCenter("Rキーを押してください", 300, g);
	}

	// メインメソッド
	public static void main(String[] args) {
		UtyuuShootMain usm = new UtyuuShootMain();
	}
}
