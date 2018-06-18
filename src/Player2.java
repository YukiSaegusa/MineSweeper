import jp.ne.kuramae.torix.lecture.ms.core.MineSweeper;
import jp.ne.kuramae.torix.lecture.ms.core.Player;

/**
 * サンプルプレイヤー<br>
 * Playerクラスを継承したクラスを作成すればよい．
 *
 * @author tori
 *
 */

// 成功数 = 34経過時間 = 3分33秒
// 成功数 = 38経過時間 = 4分49秒
// 成功数 = 13 経過時間 = 125分52秒 revel:2 試行回数：100 成功率:0.13
// 成功数 = 24 経過時間 = 4分6秒 revel:1 試行回数：50 成功率:0.48
// 成功数 = 9 経過時間 = 36分58秒 revel:2 試行回数：50 成功率:0.18
// 成功数 = 814 経過時間 = 0分59秒 revel:0 seed:0 試行回数：1000 成功率:0.814
// 成功数 = 282 経過時間 = 5分58秒 revel:1 seed:0 試行回数：500 成功率:0.564
// 成功数 = 350 経過時間 = 9分34秒 revel:1 seed:0 試行回数：500 成功率:0.7
// 成功数 = 865 経過時間 = 1分42秒 revel:0 seed:0 試行回数：1000 成功率:0.865
// 成功数 = 4 経過時間 = 0分38秒 revel:2 seed:0 試行回数：50 成功率:0.08
// 成功数 = 14 経過時間 = 1分1秒 revel:2 seed:1 試行回数：100 成功率:0.14
// 成功数 = 866 経過時間 = 0分14秒 revel:0 seed:0 試行回数：1000 成功率:0.866

// 成功数 = 353 経過時間 = 2分17秒 revel:1 seed:0 試行回数：500 成功率:0.706

public class Player2 extends Player {

	static public void main(String[] args) {
		// ここで自分のプレイヤーを作成する
		int revel = 1;
		int trynumber = 500;
		int randomseed = 0;

		Player player = new Player2();

		MineSweeper mineSweeper = new MineSweeper(revel);
		// MineSweeper mineSweeper = new MineSweeper(lv); //lv=0,1,2でレベルを指定して作成
		// MineSweeper mineSweeper = new MineSweeper(width, height, bombNum);
		mineSweeper.setRandomSeed(randomseed);

		int countSuccess = 0;
		long start = System.currentTimeMillis();
		for (int i = 0; i < trynumber; i++) {
			long end1 = System.currentTimeMillis();
			System.out.println("現在" + (i + 1) + "ゲーム目" + countSuccess + "経過時間" + ((end1 - start) / 60000) + "分");
			System.out.println("現在" + (i + 1) + "ゲーム目" + countSuccess + "経過時間" + ((end1 - start) / 60000) + "分");
			System.out.println("現在" + (i + 1) + "ゲーム目" + countSuccess + "経過時間" + ((end1 - start) / 60000) + "分");
			System.out.println("現在" + (i + 1) + "ゲーム目" + countSuccess + "経過時間" + ((end1 - start) / 60000) + "分");
			System.out.println("試行数:" + trynumber + " 成功数:" + countSuccess + "  成功率:"
					+ (long) countSuccess * 100 / trynumber + "% ");
			mineSweeper.start(player);
			if (player.isClear()) {
				countSuccess += 1;
			}
		}
		long end2 = System.currentTimeMillis();

		System.out.println("成功数 = " + countSuccess + " 経過時間 = " + ((end2 - start) / 60000) + "分"
				+ ((int) ((end2 - start) / 1000) % 60) + "秒  revel:" + revel + "  seed:" + randomseed + " 試行回数："
				+ trynumber + " 成功率:" + (double) countSuccess / trynumber);
	}

	/**
	 *
	 * マインスイーパーを実行するメソッド<br>
	 * getWidth()で幅を取得<br>
	 * getHeight()で高さを取得<br>
	 * getCell(x, y)でx, y座標の周りにある爆弾の数を獲得可能．まだ開けていない場合は-1<br>
	 *
	 * <br>
	 *
	 */

	int opennum = 0;
	int chanceopennum = 0;
	int prechanceopen[] = new int[2];

	@Override
	protected void start() {
		boolean[][] flag = new boolean[getWidth()][getHeight()];
		double chance[][] = new double[getWidth()][getHeight()];
		double test[][] = new double[getWidth()][getHeight()];
		open(0, 0);

		if (getCell(0, 0) > 0) {
			open(0, getHeight() - 1);
		}
		if (getCell(0, getHeight() - 1) > 0) {
			open(getWidth() - 1, 0);
		}
		if (getCell(getWidth() - 1, 0) > 0) {
			open(getWidth() - 1, getHeight() - 1);
		}

		LOOP: while (!isClear() && !isGameOver()) {
			while (true) {
				/*
				 * for (int i = 0; i < getWidth(); i++) { for (int j = 0; j < getHeight(); j++)
				 * { makeFlag(i, j, flag); //System.out.println(i + "," + j +"," + flag[i][j]);
				 * } }
				 */

				for (int i = 0; i < getWidth(); i++) {
					for (int j = 0; j < getHeight(); j++) {
						makeFlagSafely(i, j, flag);
						// System.out.println(i + "," + j +"," + flag[i][j]);
					}
				}
				for (int s = 0; s < getWidth(); s++) {
					for (int t = 0; t < getHeight(); t++) {
						openSafely(s, t, flag);
					}
				}

				if (opennum == 0) {
					break;
				} else {
					opennum = 0;
				}
			}
			// System.out.println(getWidth()+","+getHeight());
			if (getCell(0, getHeight() - 1) == -1 && flag[0][getHeight() - 1] == false) {
				open(0, getHeight() - 1);
				continue LOOP;
			} else if (getCell(getWidth() - 1, getHeight() - 1) == -1
					&& flag[getWidth() - 1][getHeight() - 1] == false) {
				open(getWidth() - 1, getHeight() - 1);
				continue LOOP;
			} else if (getCell(getWidth() - 1, 0) == -1 && flag[getWidth() - 1][0] == false) {
				open(getWidth() - 1, 0);
				continue LOOP;
			} else {
				System.out.println("chance");
				System.out.println("chance");
				System.out.println("chance");
				System.out.println("chance");
				System.out.println("chance");
				System.out.println("chance");
				System.out.println("chance");

				for (int x = 0; x < getWidth(); x++) {
					for (int y = 0; y < getHeight(); y++) {
						makeChance(x, y, flag, chance, test);
						System.out.println(x + "," + y + " " + chance[x][y]);
					}
				}
				chanceMinOpen(chance, flag);
				continue LOOP;
			}
		}

	}

	//////////////////////////////////////////////////////////////////
	protected int myGetCell(int x, int y) {// 壁を−２とする
		if (x == -1 || y == -1) {
			return -2;
		} else if (x == getWidth() || y == getHeight()) {
			return -2;
		} else {

			return getCell(x, y);
		}
	}

	public void myFlag(int x, int y, boolean[][] flag) {
		if (myGetCell(x, y) == -1) {
			flag[x][y] = true;
		}
	}

	public void myOpen(int x, int y, boolean[][] flag) {
		// 定義域外でopenしようとしてもエラー吐かない(&フラッグあったら開かない（ミス防止）作る予定)
		if (myGetCell(x, y) == -1) {
			if (!flag[x][y] == true) {
				open(x, y);
				opennum += 1;
			}
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////
	protected int[] soutaizahyou(int x, int y) {
		// 点(s,t)からの相対座標(b[2i], b[2i+1])の配列
		int[] b = new int[16];

		// for (int i = 0; i < 3; i++) {
		// b[2 * i] = s - 1 + i;
		// b[2 * i + 1] = t - 1;
		// b[2 * i + 10] = s + 1 + i;
		// b[2 * i + 11] = t + 1;
		// }
		b[0] = x - 1;
		b[1] = y - 1;
		b[2] = x;
		b[3] = y - 1;
		b[4] = x + 1;
		b[5] = y - 1;
		b[6] = x - 1;
		b[7] = y;
		b[8] = x + 1;
		b[9] = y;
		b[10] = x - 1;
		b[11] = y + 1;
		b[12] = x;
		b[13] = y + 1;
		b[14] = x + 1;
		b[15] = y + 1;

		return b;
	}

	protected int[] check(int s, int t) { // (x,y)周りを配列にする
		int[] a = new int[8];
		int[] b = soutaizahyou(s, t);

		for (int i = 0; i < 8; i++) {
			if (b[2 * i] < 0 || b[2 * i + 1] < 0)
				a[i] = -2;
			else if (b[2 * i] == getWidth() || b[2 * i + 1] == getHeight())
				a[i] = -2;
			else
				a[i] = myGetCell(b[2 * i], b[2 * i + 1]);
		}

		return a;
	}

	protected int danNum(int x, int y) { // (x,y)周りの、空いていないマスの数を数える
		int a[] = check(x, y);
		int dan = 0;
		for (int i = 0; i < a.length; i++)
			if (a[i] == -1)
				dan += 1;
		return dan;
	}

	protected void makeFlag(int x, int y, boolean[][] flag, int a[], int b[]) { // (x,y)周りの、爆弾があるところをtrueとする
		if (getCell(x, y) != -1) {
			if (danNum(x, y) == getCell(x, y)) {
				for (int i = 0; i < 8; i++)
					if (a[i] == -1) {
						if (b[2 * i] < 0 || b[2 * i + 1] < 0)
							;
						else if (b[2 * i] == getWidth() || b[2 * i + 1] == getHeight())
							;
						else
							flag[b[2 * i]][b[2 * i + 1]] = true;
					}
			}
		}
	}

	protected void makeFlag2(int x, int y, boolean[][] flag, int a[]) {
		if (getCell(x, y) == 2) {
			int b = 1;
			b *= (a[0] + 1) * (a[1] + 1) * (a[2] + 1) * (a[4] + 1);
			if (b != 0 && a[3] == 1) {
				myFlag(x + 1, y + 1, flag);
			}
			int c = 1;
			c *= (a[6] + 1) * (a[7] + 1) * (a[2] + 1) * (a[4] + 1);
			if (c != 0 && a[1] == 1) {
				myFlag(x - 1, y + 1, flag);
			}
			int d = 1;
			d *= (a[6] + 1) * (a[7] + 1) * (a[5] + 1) * (a[3] + 1);
			if (d != 0 && a[4] == 1) {
				myFlag(x - 1, y - 1, flag);
			}
			int e = 1;
			e *= (a[0] + 1) * (a[1] + 1) * (a[5] + 1) * (a[3] + 1);
			if (e != 0 && a[6] == 1) {
				myFlag(x + 1, y - 1, flag);
			}
		}
	}

	protected void makeFlagSafely(int x, int y, boolean[][] flag) {
		int[] a = check(x, y);
		int[] b = soutaizahyou(x, y);
		makeFlag(x, y, flag, a, b);
		makeFlag2(x, y, flag, a);
	}

	protected int flagNum(int x, int y, boolean[][] flag) { // （x,y）周りの、旗の数を数える
		int sum = 0;
		int[] b = soutaizahyou(x, y);
		for (int i = 0; i < 8; i++) {

			if (b[2 * i] < 0 || b[2 * i + 1] < 0)
				;
			else if (b[2 * i] == getWidth() || b[2 * i + 1] == getHeight())
				;
			else if (flag[b[2 * i]][b[2 * i + 1]])
				sum += 1;
		}
		return sum;
	}

	protected void makeChance(int x, int y, boolean[][] flag, double chance[][], double test[][]) {// 確率を計算し、一番高いものをchance配列に入れる

		int a[] = check(x, y);
		int b[] = soutaizahyou(x, y);
		if (getCell(x, y) > 0) {
			for (int i = 0; i < 8; i++)
				if (a[i] == -1) {
					if (!flag[b[2 * i]][b[2 * i + 1]]) {
						test[b[2 * i]][b[2 * i + 1]] = ((double) getCell(x, y) - (double) flagNum(x, y, flag))
								/ (danNum(x, y) - flagNum(x, y, flag));
						// System.out.println(x+","+y+ " test "+test[b[2 * i]][b[2 * i + 1]]);
						if (test[b[2 * i]][b[2 * i + 1]] >= chance[b[2 * i]][b[2 * i + 1]]) {
							chance[b[2 * i]][b[2 * i + 1]] = test[b[2 * i]][b[2 * i + 1]];
							// System.out.println(x+","+y+ " chance "+chance[b[2 * i]][b[2 * i + 1]]);
						}
					}
				}
		}
	}

	protected void chanceMinOpen(double chance[][], boolean flag[][]) {// 確率が一番低いものを開ける
		chanceopennum = 0;
		double minchance = 1;
		int minzahyou[] = new int[2];
		for (int i = 0; i < getWidth(); i++) {
			for (int j = 0; j < getHeight(); j++) {
				if (chance[i][j] > 0 && getCell(i, j) == -1) {
					if (minchance > chance[i][j] && !flag[i][j]) {
						if (i != prechanceopen[0] - 1 && j != prechanceopen[1] - 1) {
							if (i != prechanceopen[0]+1 && j != prechanceopen[1] - 1) {
								if (i != prechanceopen[0] - 1 && j != prechanceopen[1] + 1) {
									if (i != prechanceopen[0] + 1 && j != prechanceopen[1] + 1) {

										minchance = chance[i][j];
										minzahyou[0] = i;
										minzahyou[1] = j;
										chanceopennum += 1;
									}
								}
							}
						}
					}
				}
			}
		}
		System.out.println(minzahyou[0] + " " + minzahyou[1]);
		open(minzahyou[0], minzahyou[1]);
		prechanceopen[0] = minzahyou[0];
		prechanceopen[1] = minzahyou[1];

		if (chanceopennum == 0) {
			for (int y = 0; y < getHeight(); y++) {
				for (int x = 0; x < getWidth(); x++) {
					int bombNum = getCell(x, y);
					// System.out.println(x+","+y+"="+bombNum);
					if (bombNum == -1 && !flag[x][y]) {
						open(x, y);
					}
				}
			}
		}

	}

	protected void chanceOpen(double chance[][], boolean flag[][]) {// 確率が低いと判定されたものを左上から順に開けていく
		for (int i = 1; i < 17; i++) {
			for (int x = 0; x < getWidth(); x++) {
				for (int y = 0; y < getHeight(); y++) {
					if (chance[x][y] < i / 16) {
						open(x, y);

					}
				}
			}
		}
	}

	protected int kateiOpen(int x, int y) {
		return 99;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////
	public void openSafely(int x, int y, boolean[][] flag) {
		if (myGetCell(x, y) != -1) {
			safeOpen(x, y, flag);
			openPoint1(x, y, flag);
			openPoint2(x, y, flag);
			openPoint3(x, y, flag);
			openPoint4(x, y, flag);
			openPoint5(x, y, flag);
			openPoint95(x, y, flag);
			openPoint96(x, y, flag);
			openPoint97(x, y, flag);
			openPoint98(x, y, flag);
			openPoint99(x, y, flag);
			openPoint100(x, y, flag);
		}
	}

	protected void safeOpen(int x, int y, boolean[][] flag) { // (x,y)周りの、開けても良いマスを開ける
		int a[] = check(x, y);
		int[] b = soutaizahyou(x, y);
		if (flagNum(x, y, flag) == getCell(x, y)) {
			for (int i = 0; i < 8; i++) {
				if (b[2 * i] < 0 || b[2 * i + 1] < 0)
					;
				else if (b[2 * i] == getWidth() || b[2 * i + 1] == getHeight())
					;
				else if (a[i] == -1) {
					if (!flag[b[2 * i]][b[2 * i + 1]]) {
						open(b[2 * i], b[2 * i + 1]);
						opennum += 1;
					}
				}
			}
		}
	}

	protected void openPoint1(int x, int y, boolean[][] flag) {// 1,2,1でセーフゾーン開封
		if (myGetCell(x, y) == 2) {
			int[] a = check(x, y);
			if (a[3] == 1 && a[4] == 1) {
				myOpen(x, y - 1, flag);
				myOpen(x, y + 1, flag);
			}
			if (a[1] == 1 && a[6] == 1) {
				myOpen(x - 1, y, flag);
				myOpen(x + 1, y, flag);
			}
		}
	}

	protected void openPoint2(int x, int y, boolean[][] flag) {// 1,2,2,1でセーフゾーン開封
		if (getCell(x, y) == 2) {
			int[] a = check(x, y);
			if (a[3] == 1 && a[4] == 2) {
				if (myGetCell(x + 2, y) == 1) {
					myOpen(x - 1, y - 1, flag);
					myOpen(x - 1, y + 1, flag);
					myOpen(x + 2, y - 1, flag);
					myOpen(x + 2, y + 1, flag);
					for (int i = 0; i < 3; i++) {
						myOpen(x - 2, y - 1 + i, flag);
						myOpen(x + 3, y - 1 + i, flag);
					}
				}
			}
			if (a[1] == 1 && a[6] == 2) {
				if (myGetCell(x, y + 2) == 1) {
					myOpen(x - 1, y - 1, flag);
					myOpen(x + 1, y - 1, flag);
					myOpen(x - 1, y + 2, flag);
					myOpen(x + 1, y + 2, flag);
					for (int i = 0; i < 3; i++) {
						myOpen(x - 1 + i, y - 2, flag);
						myOpen(x - 1 + i, y + 3, flag);
					}
				}
			}
		}
	}

	protected void openPoint3(int x, int y, boolean[][] flag) {// 2,3,2かつ、3の横にフラッグでセーフゾーン開封（openPoint1の類似）
		if (myGetCell(x, y) == 3) {
			int[] a = check(x, y);
			if (a[3] == 2 && a[4] == 2) {
				if (a[1] == -1 && flag[x][y - 1]) {
					myOpen(x, y + 1, flag);
				}
				if (a[6] == -1 && flag[x][y + 1]) {
					myOpen(x, y - 1, flag);
				}
			}

			if (a[1] == 2 && a[6] == 2) {
				if (a[3] == -1 && flag[x - 1][y]) {
					myOpen(x + 1, y, flag);
				}
				if (a[4] == -1 && flag[x + 1][y]) {
					myOpen(x - 1, y, flag);
				}
			}
		}
	}

	protected void openPoint4(int x, int y, boolean[][] flag) {// 2,1型
		if (myGetCell(x, y) == 2 && myGetCell(x + 1, y) == 1) {
			int[] a = check(x, y);
			int[] b = check(x + 1, y);
			int c = 1;
			for (int i = 0; i < 4; i++) {
				c *= (a[i] + 1);
			}
			c *= (b[2] + 1) * (b[4] + 1);
			if (c != 0) {
				myFlag(x - 1, y + 1, flag);
				myOpen(x + 2, y + 1, flag);
			}
		}
		if (myGetCell(x, y) == 2 && myGetCell(x + 1, y) == 1) {
			int[] a = check(x, y);
			int[] b = check(x + 1, y);
			int c = 1;
			for (int i = 0; i < 3; i++) {
				c *= (a[i + 5] + 1);
			}
			c *= (a[3] + 1) * (b[7] + 1) * (b[4] + 1);
			if (c != 0) {
				myFlag(x - 1, y - 1, flag);
				myOpen(x + 2, y - 1, flag);
			}
		}
		if (myGetCell(x, y) == 2 && myGetCell(x - 1, y) == 1) {
			int[] a = check(x, y);
			int[] b = check(x - 1, y);
			int c = 1;
			for (int i = 0; i < 3; i++) {
				c *= (a[i] + 1);
			}
			c *= (a[4] + 1) * (b[0] + 1) * (b[3] + 1);
			if (c != 0) {
				myOpen(x - 2, y + 1, flag);
				myFlag(x + 1, y + 1, flag);
			}
		}
		if (myGetCell(x, y) == 2 && myGetCell(x - 1, y) == 1) {
			int[] a = check(x, y);
			int[] b = check(x - 1, y);
			int c = 1;
			for (int i = 0; i < 3; i++) {
				c *= (a[i + 5] + 1);
			}
			c *= (a[4] + 1) * (b[3] + 1) * (b[5] + 1);
			if (c != 0) {
				myOpen(x - 2, y - 1, flag);
				myFlag(x + 1, y - 1, flag);
			}
		}
		if (myGetCell(x, y) == 2 && myGetCell(x, y - 1) == 1) {
			int[] a = check(x, y);
			int[] b = check(x, y - 1);
			int c = 1;
			c *= (a[0] + 1) * (a[3] + 1) * (a[5] + 1) * (a[6] + 1) * (b[0] + 1) * (b[1] + 1);
			if (c != 0) {
				myOpen(x + 1, y - 2, flag);
				myFlag(x + 1, y + 1, flag);
			}
		}
		if (myGetCell(x, y) == 2 && myGetCell(x, y - 1) == 1) {
			int[] a = check(x, y);
			int[] b = check(x, y - 1);
			int c = 1;
			c *= (a[2] + 1) * (a[4] + 1) * (a[7] + 1) * (a[6] + 1) * (b[2] + 1) * (b[1] + 1);
			if (c != 0) {
				myOpen(x - 1, y - 2, flag);
				myFlag(x - 1, y + 1, flag);
			}
		}
		if (myGetCell(x, y) == 2 && myGetCell(x, y + 1) == 1) {
			int[] a = check(x, y);
			int[] b = check(x, y + 1);
			int c = 1;
			c *= (a[0] + 1) * (a[1] + 1) * (a[3] + 1) * (a[5] + 1) * (b[5] + 1) * (b[6] + 1);
			if (c != 0) {
				myOpen(x + 1, y + 2, flag);
				myFlag(x + 1, y - 1, flag);
			}
		}
		if (myGetCell(x, y) == 2 && myGetCell(x, y + 1) == 1) {
			int[] a = check(x, y);
			int[] b = check(x, y + 1);
			int c = 1;
			c *= (a[1] + 1) * (a[2] + 1) * (a[4] + 1) * (a[7] + 1) * (b[6] + 1) * (b[7] + 1);
			if (c != 0) {
				myOpen(x - 1, y + 2, flag);
				myFlag(x - 1, y - 1, flag);
			}
		}
	}

	protected void openPoint5(int x, int y, boolean[][] flag) {// 1,1型
		if (getCell(x, y) == 1) {
			int[] a = check(x, y);

			if ((a[0] > -1 || a[0] == -2) && (a[3] > -1 || a[3] == -2)) {
				if ((a[5] > -1 || a[5] == -2) && a[4] == 1) {
					myOpen(x + 2, y - 1, flag);
					myOpen(x + 2, y, flag);
					myOpen(x + 2, y + 1, flag);
				}
			}
			if ((a[0] > -1 || a[0] == -2) && (a[1] > -1 || a[1] == -2)) {
				if ((a[2] > -1 || a[2] == -2) && a[6] == 1) {
					myOpen(x - 1, y + 2, flag);
					myOpen(x, y + 2, flag);
					myOpen(x + 1, y + 2, flag);
				}
			}
			if ((a[2] > -1 || a[2] == -2) && (a[4] > -1 || a[4] == -2)) {
				if ((a[7] > -1 || a[7] == -2) && a[3] == 1) {
					myOpen(x - 2, y - 1, flag);
					myOpen(x - 2, y, flag);
					myOpen(x - 2, y + 1, flag);
				}
			}
			if ((a[5] > -1 || a[5] == -2) && (a[6] > -1 || a[6] == -2)) {
				if ((a[7] > -1 || a[7] == -2) && a[1] == 1) {
					myOpen(x - 1, y - 2, flag);
					myOpen(x, y - 2, flag);
					myOpen(x + 1, y - 2, flag);
				}
			}
		}
	}

	protected void openPoint95(int x, int y, boolean[][] flag) {
		int[] a = check(x, y);
		int[] b = soutaizahyou(x, y);
		if (getCell(x, y) == 2) {
			if (a[1] == 1 && a[3] == 1) {
				if (a[0] != -1) {
					int[] a1 = check(x - 1, y);
					int[] b1 = check(x, y - 1);
					if (a1[0] != -1 && a1[3] != -1 && a1[5] != -1) {
						if (b1[0] != -1 && b1[1] != -1 && b1[2] != -1) {
							myOpen(x + 1, y + 1, flag);
						}
					}
				}
			} else if (a[3] == 1 && a[6] == 1) {
				if (a[5] != -1) {
					int[] a2 = check(x - 1, y);
					int[] b2 = check(x, y + 1);
					if (a2[0] != -1 && a2[3] != -1 && a2[5] != -1) {
						if (b2[5] != -1 && b2[6] != -1 && b2[7] != -1) {
							myOpen(x + 1, y - 1, flag);
						}
					}
				}
			} else if (a[1] == 1 && a[4] == 1) {
				if (a[2] != -1) {
					int[] a3 = check(x + 1, y);
					int[] b3 = check(x, y - 1);
					if (a3[2] != -1 && a3[4] != -1 && a3[7] != -1) {
						if (b3[0] != -1 && b3[1] != -1 && b3[2] != -1) {
							myOpen(x - 1, y + 1, flag);
						}
					}
				}
			} else if (a[4] == 1 && a[6] == 1) {
				if (a[7] != -1) {
					int[] a4 = check(x + 1, y);
					int[] b4 = check(x, y + 1);
					if (a4[2] != -1 && a4[4] != -1 && a4[7] != -1) {
						if (b4[5] != -1 && b4[6] != -1 && b4[7] != -1) {
							myOpen(x - 1, y - 1, flag);
						}
					}
				}
			}
		}
	}

	protected void openPoint96(int x, int y, boolean[][] flag) {// 1,3,1のL字型
		if (getCell(x, y) == 3) {
			int[] a = check(x, y);
			if (a[1] == 1 && a[4] == 1) {
				int[] b = check(x, y - 1);
				int[] c = check(x + 1, y);
				int d = 1;
				d *= (b[1] + 1) * (b[2] + 1) * (c[1] + 1) * (c[2] + 1) * (c[4] + 1);
				if (d != 0) {
					myOpen(x - 1, y - 2, flag);
					myOpen(x + 2, y + 1, flag);
					myFlag(x - 1, y + 1, flag);
				}
			}
			if (a[4] == 1 && a[6] == 1) {
				int[] b = check(x + 1, y);
				int[] c = check(x, y + 1);
				int d = 1;
				d *= (b[4] + 1) * (b[7] + 1) * (c[4] + 1) * (c[6] + 1) * (c[7] + 1);
				if (d != 0) {
					myOpen(x + 2, y - 1, flag);
					myOpen(x - 1, y + 2, flag);
					myFlag(x - 1, y - 1, flag);
				}
			}
			if (a[3] == 1 && a[6] == 1) {
				int[] b = check(x - 1, y);
				int[] c = check(x, y + 1);
				int d = 1;
				d *= (b[3] + 1) * (b[5] + 1) * (c[3] + 1) * (c[6] + 1) * (c[5] + 1);
				if (d != 0) {
					myOpen(x - 2, y - 1, flag);
					myOpen(x + 1, y + 2, flag);
					myFlag(x + 1, y - 1, flag);
				}
			}
			if (a[3] == 1 && a[1] == 1) {
				int[] b = check(x - 1, y);
				int[] c = check(x, y - 1);
				int d = 1;
				d *= (b[0] + 1) * (b[3] + 1) * (c[0] + 1) * (c[1] + 1) * (c[3] + 1);
				if (d != 0) {
					myOpen(x - 2, y + 1, flag);
					myOpen(x + 1, y - 2, flag);
					myFlag(x + 1, y + 1, flag);
				}
			}
		}
	}

	protected void openPoint97(int x, int y, boolean[][] flag) {// 2,2,2のL字型
		if (getCell(x, y) == 2) {
			int[] a = check(x, y);
			if (a[1] == 2 && a[4] == 2) {
				int[] b = check(x, y - 1);
				int[] c = check(x + 1, y);
				int d = 1;
				d *= (b[1] + 1) * (b[2] + 1) * (c[1] + 1) * (c[2] + 1) * (c[4] + 1);
				if (d != 0) {
					myFlag(x - 1, y - 2, flag);
					myFlag(x + 2, y + 1, flag);
					myOpen(x - 1, y + 1, flag);
				}
			}
			if (a[4] == 2 && a[6] == 2) {
				int[] b = check(x + 1, y);
				int[] c = check(x, y + 1);
				int d = 1;
				d *= (b[4] + 1) * (b[7] + 1) * (c[4] + 1) * (c[6] + 1) * (c[7] + 1);
				if (d != 0) {
					myFlag(x + 2, y - 1, flag);
					myFlag(x - 1, y + 2, flag);
					myOpen(x - 1, y - 1, flag);
				}
			}
			if (a[3] == 2 && a[6] == 2) {
				int[] b = check(x - 1, y);
				int[] c = check(x, y + 1);
				int d = 1;
				d *= (b[3] + 1) * (b[5] + 1) * (c[3] + 1) * (c[6] + 1) * (c[5] + 1);
				if (d != 0) {
					myFlag(x - 2, y - 1, flag);
					myFlag(x + 1, y + 2, flag);
					myOpen(x + 1, y - 1, flag);
				}
			}
			if (a[3] == 2 && a[1] == 2) {
				int[] b = check(x - 1, y);
				int[] c = check(x, y - 1);
				int d = 1;
				d *= (b[0] + 1) * (b[3] + 1) * (c[0] + 1) * (c[1] + 1) * (c[3] + 1);
				if (d != 0) {
					myFlag(x - 2, y + 1, flag);
					myFlag(x + 1, y - 2, flag);
					myOpen(x + 1, y + 1, flag);
				}
			}
		}
	}

	protected void openPoint98(int x, int y, boolean[][] flag) {// b=a+3型
		if (myGetCell(x, y) > 0 && myGetCell(x + 1, y) > 0) {
			if (myGetCell(x, y) - myGetCell(x + 1, y) == 3) {
				myFlag(x - 1, y - 1, flag);
				myFlag(x - 1, y, flag);
				myFlag(x - 1, y + 1, flag);
				myOpen(x + 2, y - 1, flag);
				myOpen(x + 2, y, flag);
				myOpen(x + 2, y + 1, flag);
			}
			if (myGetCell(x, y) - myGetCell(x + 1, y) == -3) {
				myOpen(x - 1, y - 1, flag);
				myOpen(x - 1, y, flag);
				myOpen(x - 1, y + 1, flag);
				myFlag(x + 2, y - 1, flag);
				myFlag(x + 2, y, flag);
				myFlag(x + 2, y + 1, flag);
			}
		}
		if (myGetCell(x, y) > 0 && myGetCell(x, y + 1) > 0) {
			if (myGetCell(x, y) - myGetCell(x, y + 1) == 3) {
				myOpen(x - 1, y + 2, flag);
				myOpen(x, y + 2, flag);
				myOpen(x + 1, y + 2, flag);
				myFlag(x - 1, y - 1, flag);
				myFlag(x, y - 1, flag);
				myFlag(x + 1, y - 1, flag);
			}
			if (myGetCell(x, y) - myGetCell(x, y + 1) == -3) {
				myFlag(x - 1, y + 2, flag);
				myFlag(x, y + 2, flag);
				myFlag(x + 1, y + 2, flag);
				myOpen(x - 1, y - 1, flag);
				myOpen(x, y - 1, flag);
				myOpen(x + 1, y - 1, flag);
			}
		}
	}

	protected void openPoint99(int x, int y, boolean[][] flag) {// b=a+2型
		if (myGetCell(x, y) > 0 && myGetCell(x + 1, y) > 0) {
			if (myGetCell(x, y) - myGetCell(x + 1, y) == 2) {
				int[] a = check(x, y);
				int c = 1;
				c *= (a[0] + 1) * (a[0] + 1) + (a[3] + 1) * (a[3] + 1) + (a[5] + 1) * (a[5] + 1);
				if (c != 0) {
					myFlag(x - 1, y - 1, flag);
					myFlag(x - 1, y, flag);
					myFlag(x - 1, y + 1, flag);
					myOpen(x + 2, y - 1, flag);
					myOpen(x + 2, y, flag);
					myOpen(x + 2, y + 1, flag);
				}
			}
			if (myGetCell(x, y) - myGetCell(x + 1, y) == -2) {
				int[] a = check(x + 1, y);
				int c = 1;
				c *= (a[2] + 1) * (a[2] + 1) + (a[4] + 1) * (a[4] + 1) + (a[7] + 1) * (a[7] + 1);
				if (c != 0) {
					myOpen(x - 1, y - 1, flag);
					myOpen(x - 1, y, flag);
					myOpen(x - 1, y + 1, flag);
					myFlag(x + 2, y - 1, flag);
					myFlag(x + 2, y, flag);
					myFlag(x + 2, y + 1, flag);
				}
			}
		}
		if (myGetCell(x, y) > 0 && myGetCell(x, y + 1) > 0) {
			if (myGetCell(x, y) - myGetCell(x, y + 1) == 2) {
				int[] a = check(x, y);
				int c = 1;
				c *= (a[0] + 1) * (a[0] + 1) + (a[1] + 1) * (a[1] + 1) + (a[2] + 1) * (a[2] + 1);
				if (c != 0) {
					myOpen(x - 1, y + 2, flag);
					myOpen(x, y + 2, flag);
					myOpen(x + 1, y + 2, flag);
					myFlag(x - 1, y - 1, flag);
					myFlag(x, y - 1, flag);
					myFlag(x + 1, y - 1, flag);
				}
			}
			if (myGetCell(x, y) - myGetCell(x, y + 1) == -2) {
				int[] a = check(x, y + 1);
				int c = 1;
				c *= (a[5] + 1) * (a[5] + 1) + (a[6] + 1) * (a[6] + 1) + (a[7] + 1) * (a[7] + 1);
				if (c != 0) {
					myFlag(x - 1, y + 2, flag);
					myFlag(x, y + 2, flag);
					myFlag(x + 1, y + 2, flag);
					myOpen(x - 1, y - 1, flag);
					myOpen(x, y - 1, flag);
					myOpen(x + 1, y - 1, flag);
				}
			}
		}
	}

	protected void openPoint100(int x, int y, boolean[][] flag) {// b=a+1型
		if (myGetCell(x, y) > 0 && myGetCell(x + 1, y) > 0) {
			if (myGetCell(x, y) - myGetCell(x + 1, y) == 1) {
				int[] a = check(x, y);
				int c = 1;
				int d = (a[0] + 1) * (a[0] + 1);
				int e = (a[3] + 1) * (a[3] + 1);
				int f = (a[5] + 1) * (a[5] + 1);
				c *= d * e + e * f + f * d;
				if (c != 0) {
					myFlag(x - 1, y - 1, flag);
					myFlag(x - 1, y, flag);
					myFlag(x - 1, y + 1, flag);
					myOpen(x + 2, y - 1, flag);
					myOpen(x + 2, y, flag);
					myOpen(x + 2, y + 1, flag);
				}
			}
			if (myGetCell(x, y) - myGetCell(x + 1, y) == -1) {
				int[] a = check(x + 1, y);
				int c = 1;
				int d = (a[2] + 1) * (a[2] + 1);
				int e = (a[4] + 1) * (a[4] + 1);
				int f = (a[7] + 1) * (a[7] + 1);
				c *= d * e + e * f + f * d;
				if (c != 0) {
					myOpen(x - 1, y - 1, flag);
					myOpen(x - 1, y, flag);
					myOpen(x - 1, y + 1, flag);
					myFlag(x + 2, y - 1, flag);
					myFlag(x + 2, y, flag);
					myFlag(x + 2, y + 1, flag);
				}
			}
		}
		if (myGetCell(x, y) > 0 && myGetCell(x, y + 1) > 0) {
			if (myGetCell(x, y) - myGetCell(x, y + 1) == 1) {
				int[] a = check(x, y);
				int c = 1;
				int d = (a[0] + 1) * (a[0] + 1);
				int e = (a[1] + 1) * (a[1] + 1);
				int f = (a[2] + 1) * (a[2] + 1);
				c *= d * e + e * f + f * d;
				if (c != 0) {
					myOpen(x - 1, y + 2, flag);
					myOpen(x, y + 2, flag);
					myOpen(x + 1, y + 2, flag);
					myFlag(x - 1, y - 1, flag);
					myFlag(x, y - 1, flag);
					myFlag(x + 1, y - 1, flag);
				}
			}
			if (myGetCell(x, y) - myGetCell(x, y + 1) == -1) {
				int[] a = check(x, y + 1);
				int c = 1;
				int d = (a[7] + 1) * (a[7] + 1);
				int e = (a[6] + 1) * (a[6] + 1);
				int f = (a[5] + 1) * (a[5] + 1);
				c *= d * e + e * f + f * d;
				if (c != 0) {
					myFlag(x - 1, y + 2, flag);
					myFlag(x, y + 2, flag);
					myFlag(x + 1, y + 2, flag);
					myOpen(x - 1, y - 1, flag);
					myOpen(x, y - 1, flag);
					myOpen(x + 1, y - 1, flag);
				}
			}
		}
	}

}