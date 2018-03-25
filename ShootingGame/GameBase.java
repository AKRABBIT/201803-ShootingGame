import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.Sequencer;
import javax.swing.JFrame;

public abstract class GameBase {
	// ゲーム状態を記録する定数
	public static final int GS_STARTGAMEN = 0;
	public static final int GS_STAGESTART = 1;
	public static final int GS_STAGECLEAR = 2;
	public static final int GS_GAMEOVER = 3;
	public static final int GS_GAMEMAIN = 4;

	private int gamestate;
	public JFrame frame1;
	public BufferStrategy bstrategy;
	public Sequencer midiseq = null;
	private int waittimer; // 経過時間

	// コンストラクタ
	public GameBase(int w,int h, String title){
		frame1 = new JFrame(title);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setBackground(Color.WHITE);
		frame1.setResizable(false);

		frame1.setVisible(true);
		Insets insets = frame1.getInsets();
		frame1.setSize(w + insets.left + insets.right,
				h + insets.top + insets.bottom);
		frame1.setLocationRelativeTo(null);

		// 仮想画面の実現
		frame1.createBufferStrategy(2);
		bstrategy = frame1.getBufferStrategy();
		frame1.setIgnoreRepaint(true);

		frame1.addKeyListener(new MyKeyAdapter());
	}

	// 画面切り替えとタイマー呼び出し
	// ゲーム状態を代入することで画面切り替えを可能にする
	public void goStartGamen(){
		gamestate = GS_STARTGAMEN;
		Timer t = new Timer();
		t.schedule(new MyTimerTask(), 10, 30);
	}
	public void goStageStart(){
		initStageStart();
		waittimer = 100;
		gamestate = GS_STAGESTART;
	}
	public void goStageClear(){
		initStageClear();
		waittimer = 100;
		gamestate = GS_STAGECLEAR;
	}
	public void goGameMain(){
		gamestate = GS_GAMEMAIN;
	}
	public void goGameOver(){
		initGameOver();
		gamestate = GS_GAMEOVER;
	}

	// オーバーライド用のメソッド

	// 画面準備系メソッド。goメソッドの中で呼び出し
	public abstract void initStageStart();
	public abstract void initStageClear();
	public abstract void initGameOver();
	// キー押下判定系のメソッド
	public abstract void keyPressedGameMain(int keycode);
	public abstract void keyReleasedGameMain(int keycode);

	public void drawStringCenter(String str, int y, Graphics g) {
		int fw = frame1.getWidth() / 2;
		FontMetrics fm = g.getFontMetrics();
		int strw = fm.stringWidth(str) /2 ;
		g.drawString(str,fw-strw,y);
	}

	// オーバーライド用
	public abstract void runStartGamen(Graphics g);
	public abstract void runStageStart(Graphics g);
	public abstract void runStageClear(Graphics g);
	public abstract void runGameMain(Graphics g);
	public abstract void runGameOver(Graphics g);

	private class MyKeyAdapter extends KeyAdapter{
		public void keyPressed(KeyEvent ev) {
			if (gamestate == GS_GAMEMAIN){
				keyPressedGameMain(ev.getKeyCode());
			}
		}
		public void keyReleased(KeyEvent ev) {
			int keycode = ev.getKeyCode();
			switch(gamestate){
				case GS_GAMEMAIN:
					keyReleasedGameMain(keycode);
					break;
				case GS_STARTGAMEN:
					if (keycode == KeyEvent.VK_P) goStageStart();
					break;
				case GS_GAMEOVER:
					if (keycode == KeyEvent.VK_R) goStageStart();
			}
		}
	}
	private class MyTimerTask extends TimerTask {
		public void run() {
			Graphics g = bstrategy.getDrawGraphics();
			if (bstrategy.contentsLost() == false){
				Insets insets = frame1.getInsets();
				g.translate(insets.left, insets.top);

				// ゲームの状態により処理を切り替え
				switch(gamestate){
					case GS_STARTGAMEN:
						runStartGamen(g);
						break;
					case GS_STAGESTART:
						runStageStart(g);
						waittimer = waittimer-1; // 経過時間の判定
						if (waittimer < 0)	goGameMain(); // 0未満で画面切り替え
						break;
					case GS_STAGECLEAR:
						runStageClear(g);
						waittimer = waittimer-1; // 経過時間の判定
						if (waittimer < 0)	goStageStart(); // 0未満で画面切り替え
						break;
					case GS_GAMEMAIN:
						runGameMain(g);
						break;
					case GS_GAMEOVER:
						runGameOver(g);
						break;
				}

				bstrategy.show();
				g.dispose();
			}
		}
	}
}
