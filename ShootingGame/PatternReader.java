import java.awt.Point;
import java.util.StringTokenizer;

public class PatternReader {
	String patstr;
	StringTokenizer tokenizer;
	Point movexy = new Point();
	int kaisuu = 0;

	// コンストラクタ
	PatternReader(String str){
		patstr = str;
		tokenizer = new StringTokenizer(patstr,",");
	}

	Point next(){
		if (kaisuu == 0){
			if (tokenizer.hasMoreTokens() == false){
				tokenizer = new StringTokenizer(patstr,",");
			}
			movexy.x = Integer.parseInt(tokenizer.nextToken());	// X移動量取り出し
			movexy.y = Integer.parseInt(tokenizer.nextToken());	// Y移動量取り出し
			kaisuu = Integer.parseInt(tokenizer.nextToken());	// 繰り返し回数
		} else {
			kaisuu = kaisuu-1;
		}
		return movexy;
	}
}
