
public class ALU {
	// 测试
	/**public static void main(String args[]) {
		ALU alu = new ALU();
		int[] length = { 23, 8, 4 };
	//System.out.println(alu.NBCD("20"));//2708,-2706
	//System.out.println(alu.TrueValue("1101000000000000", Type.DECIMAL,length));
	//System.out.println(alu.SubtractionD("110000010000", "110000100000"));
		//System.out.println(alu.Calculation("10", "20", Type.DECIMAL, Operation.SUBTRACTION, length));
		System.out.println(alu.Addition("1001", "0001", '0', 8));
	}**/

	public enum Operation {
		ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION
	};

	public enum Type {
		INTEGER, FLOAT, DECIMAL
	};

	/**
	 * 十进制转二进制
	 * 
	 * @param number
	 * @param length
	 * @return
	 */
	public String Complement(String number, int length) {
		// 此处暂时不考虑溢出（integer类型装不下string）
		int num = Integer.parseInt(number);
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.insert(0, num & 1);
			num = num >> 1;
		}
		String s = sb.toString();

		return s;
	}

	/**
	 * 二进制转十进制
	 * 
	 * @param operand
	 * @param type
	 * @param length
	 * @return
	 */
	// ！！！！！在将浮点数转化为十进制小数时may结果是745.000这样，修正请在calculate方法中进行，切勿忘记！(K.O.)
	public String TrueValue(String operand, Type type, int length[]) {
		// 估计会溢出= =
		if (type == Type.INTEGER) {
			char[] c = operand.toCharArray();
			int num = -(c[0] - '0'); // 操作数的真值
			for (int i = 1; i < c.length; i++) {
				num = (num << 1) + c[i] - '0';
			}
			String s = Integer.toString(num);
			return s;
		} else if (type == Type.FLOAT) {
			String s = "";
			if (operand.charAt(0) == '1') {
				s = "-";
			}
			String exponent = operand.substring(1, length[1] + 1);
			String fraction = operand.substring(length[1] + 1);
			char[] fc = fraction.toCharArray();
			String exTemp = "";
			for (int i = 0; i < length[1]; i++) {
				exTemp = exTemp + "0";
			}
			String frTemp = "";

			for (int i = 0; i < length[0]; i++) {
				frTemp = frTemp + "0";
			}
			if (exponent.equals(exTemp)) {
				// 数为-0or+0
				if (fraction.equals(frTemp)) {
					s = s + "0";
					return s;
				} else {
					// 此处添加对应0.x的转化
					float f = 0.0f;
					String temp = "";
					char[] c = fraction.toCharArray();
					for (int i = 0; i < length[0]; i++) {
						f = (float) (f + Math.pow(2, -i - 1) * (c[i] - '0'));
					}
					temp = String.valueOf(f).substring(2);
					for (int i = 0; i < (Math.pow(2, length[1] - 1) - 2); i++) {
						temp = "0" + temp;
					}
					s = s + "0." + temp;
					return s;
				}
			}

			// 此处为指数全为1的情况
			exTemp = "1";
			for (int i = 0; i < length[1] - 1; i++) {
				exTemp = exTemp + "1";
			}
			if (exponent.equals(exTemp)) {
				if (fraction.equals(frTemp)) {
					// s = s + "Inf";
					if (operand.charAt(0) == '0') {
						return "+Inf";
					} else {
						return "-Inf";
					}
					// return s;
				} else {
					return "NaN";
				}
			}

			// 此处是规格化数的转化
			int ex = Integer.parseInt(TrueValue("0" + exponent, Type.INTEGER,
					length))
					- (int) Math.pow(2, length[1] - 1) + 1;
			double d = 1.0;
			for (int i = 0; i < length[0]; i++) {
				d = d + (fc[i] - '0') * Math.pow(2, -1 - i);
			}
			d = d * Math.pow(2, ex);

			return s + String.valueOf(d);

		} else if (type == Type.DECIMAL) {
			// System.out.print("here");
			String s = "";
			String s1 = "";//符号位
			if (operand.substring(0, 4).equals("1101")) {
				s1 = "-";
			}
			operand = operand.substring(4);
			for (int i = 0; i < operand.length() / 4; i++) {
				String temp = operand.substring(i * 4, (i + 1) * 4);
				// System.out.println(temp);
				s = s + Integer.valueOf(temp, 2);
			}
			//System.out.println("s:"+s);
			int tempInt = s.length();
			if (s.length() > 1) {
				for (int i = 0; i < tempInt-1; i++) {
					if (s.charAt(0) == '0') {
						s = s.substring(1);
					} else {
						break;
					}
				}
			}
			if(s.equals("0")){
				return s;
			}
			return s1+s;
		}
		return null;

	}

	/**
	 * 取反
	 * 
	 * @param operand
	 * @return
	 */
	public String Negation(String operand) {
		char[] c = operand.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '1') {
				c[i] = '0';
			} else {
				c[i] = '1';
			}
		}
		String s = String.valueOf(c);
		return s;
	}

	/**
	 * 左移
	 * 
	 * @param operand
	 * @param n
	 * @return
	 */
	public String LeftShift(String operand, int n) {
		String s = operand.substring(n);
		for (int i = 0; i < n; i++) {
			s = s + "0";
		}
		return s;

	}

	/**
	 * 符号右移
	 * 
	 * @param operand
	 * @param n
	 * @return
	 */
	public String RightShift(String operand, int n) {
		String temp = operand.substring(0, 1);
		String s = operand.substring(0, operand.length() - n);

		for (int i = 0; i < n; i++) {
			s = temp + s;
		}

		return s;
	}

	/**
	 * 全加器
	 * 
	 * @param x
	 * @param y
	 * @param c
	 * @return
	 */
	public String FullAdder(char x, char y, char c) {
		char[] c1 = new char[2];
		int x1 = x - '0';
		int y1 = y - '0';
		int cTemp = c - '0';
		int temp = x1 ^ y1 ^ cTemp;
		String s = Integer.toString(temp);
		// c1[0] = (char) (temp + '0');
		temp = (x1 & cTemp) | (x1 & y1) | (y1 & cTemp);
		// c1[1] = (char) (temp + '0');
		// String s = String.valueOf(c1);
		s = s + Integer.toString(temp);
		return s;
	}

	/**
	 * 符号扩展
	 * 
	 * @param s
	 * @param length
	 * @return
	 */
	public String expand(String s, int length) {
		String temp = s.substring(0, 1);
		int t = s.length();
		for (int i = 0; i < length - t; i++) {
			s = temp + s;
		}

		return s;
	}

	/**
	 * 进位先行加法器
	 * 
	 * @param operand1
	 * @param operand2
	 * @param c
	 * @param length
	 * @return
	 */
	public String CLAAdder(String operand1, String operand2, char c, int length) {
		operand1 = expand(operand1, length);
		operand2 = expand(operand2, length);
		StringBuffer sb1 = new StringBuffer(operand1);
		operand1 = sb1.reverse().toString();
		StringBuffer sb2 = new StringBuffer(operand2);
		operand2 = sb2.reverse().toString();
		// System.out.print(operand1);
		// System.out.println(operand1+","+operand2);
		char[] s1 = operand1.toCharArray();
		char[] s2 = operand2.toCharArray();
		int[] s01 = new int[length];
		int[] s02 = new int[length];
		for (int i = 0; i < length; i++) {
			s01[i] = s1[i] - '0';
			s02[i] = s2[i] - '0';
		}
		int[] and = new int[length];
		int[] or = new int[length];
		for (int i = 0; i < length; i++) {
			and[i] = (s01[i] & s02[i]);
			or[i] = (s01[i] | s02[i]);
			// System.out.println(and[i]+","+or[i]);
		}
		String s = null;
		int cTemp = c - '0';
		switch (length) {
		case 1: {
			s = FullAdder(s1[0], s2[0], c);
			break;
		}
		case 2: {
			char c1 = (char) (((and[0]) | ((or[0]) & cTemp)) + '0');
			char c2 = (char) (((and[1]) | (or[1] & and[0]) | (or[1] & or[0] & cTemp)) + '0');
			// System.out.print(c1+","+c2);
			s = FullAdder(s1[1], s2[1], c1).substring(0, 1);
			String temp = FullAdder(s1[0], s2[0], c).substring(0, 1);
			s = s + temp + c2;
			break;
		}
		case 3: {
			char c1 = (char) (((and[0]) | ((or[0]) & cTemp)) + '0');
			char c2 = (char) (((and[1]) | (or[1] & and[0]) | (or[1] & or[0] & cTemp)) + '0');
			char c3 = (char) (((and[2]) | (or[2] & and[1])
					| (or[2] & or[1] & and[0]) | (or[2] & or[1] & or[0] & cTemp)) + '0');
			s = FullAdder(s1[2], s2[2], c2).substring(0, 1)
					+ FullAdder(s1[1], s2[1], c1).substring(0, 1)
					+ FullAdder(s1[0], s2[0], c).substring(0, 1) + c3;
			// System.out.print(FullAdder(s1[0],s2[0],c).substring(1)+c3);
			break;
		}
		case 4: {
			char c1 = (char) (((and[0]) | ((or[0]) & cTemp)) + '0');
			char c2 = (char) (((and[1]) | (or[1] & and[0]) | (or[1] & or[0] & cTemp)) + '0');
			char c3 = (char) (((and[2]) | (or[2] & and[1])
					| (or[2] & or[1] & and[0]) | (or[2] & or[1] & or[0] & cTemp)) + '0');
			char c4 = (char) (((and[3]) | (or[3] & and[2])
					| (or[3] & or[2] & and[1])
					| (or[3] & or[2] & or[1] & and[0]) | (or[3] & or[2] & or[1]
					& or[0] & cTemp)) + '0');
			s = FullAdder(s1[3], s2[3], c3).substring(0, 1)
					+ FullAdder(s1[2], s2[2], c2).substring(0, 1)
					+ FullAdder(s1[1], s2[1], c1).substring(0, 1)
					+ FullAdder(s1[0], s2[0], c).substring(0, 1) + c4;
			break;
		}
		case 5: {
			char c1 = (char) (((and[0]) | ((or[0]) & cTemp)) + '0');
			char c2 = (char) (((and[1]) | (or[1] & and[0]) | (or[1] & or[0] & cTemp)) + '0');
			char c3 = (char) (((and[2]) | (or[2] & and[1])
					| (or[2] & or[1] & and[0]) | (or[2] & or[1] & or[0] & cTemp)) + '0');
			char c4 = (char) (((and[3]) | (or[3] & and[2])
					| (or[3] & or[2] & and[1])
					| (or[3] & or[2] & or[1] & and[0]) | (or[3] & or[2] & or[1]
					& or[0] & cTemp)) + '0');
			char c5 = (char) (((and[4]) | (or[4] & and[3])
					| (or[4] & or[3] & and[2])
					| (or[4] & or[3] & or[2] & and[1])
					| (or[4] & or[3] & or[2] & or[1] & and[0]) | (or[4] & or[3]
					& or[2] & or[1] & or[0] & cTemp)) + '0');
			s = FullAdder(s1[4], s2[4], c4).substring(0, 1)
					+ FullAdder(s1[3], s2[3], c3).substring(0, 1)
					+ FullAdder(s1[2], s2[2], c2).substring(0, 1)
					+ FullAdder(s1[1], s2[1], c1).substring(0, 1)
					+ FullAdder(s1[0], s2[0], c).substring(0, 1) + c5;
			break;
		}
		case 6: {
			char c1 = (char) (((and[0]) | ((or[0]) & cTemp)) + '0');
			char c2 = (char) (((and[1]) | (or[1] & and[0]) | (or[1] & or[0] & cTemp)) + '0');
			char c3 = (char) (((and[2]) | (or[2] & and[1])
					| (or[2] & or[1] & and[0]) | (or[2] & or[1] & or[0] & cTemp)) + '0');
			char c4 = (char) (((and[3]) | (or[3] & and[2])
					| (or[3] & or[2] & and[1])
					| (or[3] & or[2] & or[1] & and[0]) | (or[3] & or[2] & or[1]
					& or[0] & cTemp)) + '0');
			char c5 = (char) (((and[4]) | (or[4] & and[3])
					| (or[4] & or[3] & and[2])
					| (or[4] & or[3] & or[2] & and[1])
					| (or[4] & or[3] & or[2] & or[1] & and[0]) | (or[4] & or[3]
					& or[2] & or[1] & or[0] & cTemp)) + '0');
			char c6 = (char) (((and[5]) | (or[5] & and[4])
					| (or[5] & or[4] & and[3])
					| (or[5] & or[4] & or[3] & and[2])
					| (or[5] & or[4] & or[3] & or[2] & and[1])
					| (or[5] & or[4] & or[3] & or[2] & or[1] & and[0]) | (or[5]
					& or[4] & or[3] & or[2] & or[1] & or[0] & cTemp)) + '0');
			s = FullAdder(s1[5], s2[5], c5).substring(0, 1)
					+ FullAdder(s1[4], s2[4], c4).substring(0, 1)
					+ FullAdder(s1[3], s2[3], c3).substring(0, 1)
					+ FullAdder(s1[2], s2[2], c2).substring(0, 1)
					+ FullAdder(s1[1], s2[1], c1).substring(0, 1)
					+ FullAdder(s1[0], s2[0], c).substring(0, 1) + c6;
			break;
		}
		case 7: {
			char c1 = (char) (((and[0]) | ((or[0]) & cTemp)) + '0');
			char c2 = (char) (((and[1]) | (or[1] & and[0]) | (or[1] & or[0] & cTemp)) + '0');
			char c3 = (char) (((and[2]) | (or[2] & and[1])
					| (or[2] & or[1] & and[0]) | (or[2] & or[1] & or[0] & cTemp)) + '0');
			char c4 = (char) (((and[3]) | (or[3] & and[2])
					| (or[3] & or[2] & and[1])
					| (or[3] & or[2] & or[1] & and[0]) | (or[3] & or[2] & or[1]
					& or[0] & cTemp)) + '0');
			char c5 = (char) (((and[4]) | (or[4] & and[3])
					| (or[4] & or[3] & and[2])
					| (or[4] & or[3] & or[2] & and[1])
					| (or[4] & or[3] & or[2] & or[1] & and[0]) | (or[4] & or[3]
					& or[2] & or[1] & or[0] & cTemp)) + '0');
			char c6 = (char) (((and[5]) | (or[5] & and[4])
					| (or[5] & or[4] & and[3])
					| (or[5] & or[4] & or[3] & and[2])
					| (or[5] & or[4] & or[3] & or[2] & and[1])
					| (or[5] & or[4] & or[3] & or[2] & or[1] & and[0]) | (or[5]
					& or[4] & or[3] & or[2] & or[1] & or[0] & cTemp)) + '0');
			char c7 = (char) (((and[6]) | (or[6] & and[5])
					| (or[6] & or[5] & and[4])
					| (or[6] & or[5] & or[4] & and[3])
					| (or[6] & or[5] & or[4] & or[3] & and[2])
					| (or[6] & or[5] & or[4] & or[3] & or[2] & and[1])
					| (or[6] & or[5] & or[4] & or[3] & or[2] & or[1] & and[0]) | (or[6]
					& or[5] & or[4] & or[3] & or[2] & or[1] & or[0] & cTemp)) + '0');
			s = FullAdder(s1[6], s2[6], c6).substring(0, 1)
					+ FullAdder(s1[5], s2[5], c5).substring(0, 1)
					+ FullAdder(s1[4], s2[4], c4).substring(0, 1)
					+ FullAdder(s1[3], s2[3], c3).substring(0, 1)
					+ FullAdder(s1[2], s2[2], c2).substring(0, 1)
					+ FullAdder(s1[1], s2[1], c1).substring(0, 1)
					+ FullAdder(s1[0], s2[0], c).substring(0, 1) + c7;
			break;
		}
		case 8: {
			char c1 = (char) (((and[0]) | ((or[0]) & cTemp)) + '0');
			char c2 = (char) (((and[1]) | (or[1] & and[0]) | (or[1] & or[0] & cTemp)) + '0');
			char c3 = (char) (((and[2]) | (or[2] & and[1])
					| (or[2] & or[1] & and[0]) | (or[2] & or[1] & or[0] & cTemp)) + '0');
			char c4 = (char) (((and[3]) | (or[3] & and[2])
					| (or[3] & or[2] & and[1])
					| (or[3] & or[2] & or[1] & and[0]) | (or[3] & or[2] & or[1]
					& or[0] & cTemp)) + '0');
			char c5 = (char) (((and[4]) | (or[4] & and[3])
					| (or[4] & or[3] & and[2])
					| (or[4] & or[3] & or[2] & and[1])
					| (or[4] & or[3] & or[2] & or[1] & and[0]) | (or[4] & or[3]
					& or[2] & or[1] & or[0] & cTemp)) + '0');
			char c6 = (char) (((and[5]) | (or[5] & and[4])
					| (or[5] & or[4] & and[3])
					| (or[5] & or[4] & or[3] & and[2])
					| (or[5] & or[4] & or[3] & or[2] & and[1])
					| (or[5] & or[4] & or[3] & or[2] & or[1] & and[0]) | (or[5]
					& or[4] & or[3] & or[2] & or[1] & or[0] & cTemp)) + '0');
			char c7 = (char) (((and[6]) | (or[6] & and[5])
					| (or[6] & or[5] & and[4])
					| (or[6] & or[5] & or[4] & and[3])
					| (or[6] & or[5] & or[4] & or[3] & and[2])
					| (or[6] & or[5] & or[4] & or[3] & or[2] & and[1])
					| (or[6] & or[5] & or[4] & or[3] & or[2] & or[1] & and[0]) | (or[6]
					& or[5] & or[4] & or[3] & or[2] & or[1] & or[0] & cTemp)) + '0');
			char c8 = (char) (((and[7])
					| (or[7] & and[6])
					| (or[7] & or[6] & and[5])
					| (or[7] & or[6] & or[5] & and[4])
					| (or[7] & or[6] & or[5] & or[4] & and[3])
					| (or[7] & or[6] & or[5] & or[4] & or[3] & and[2])
					| (or[7] & or[6] & or[5] & or[4] & or[3] & or[2] & and[1])
					| (or[7] & or[6] & or[5] & or[4] & or[3] & or[2] & or[1] & and[0]) | (or[7]
					& or[6] & or[5] & or[4] & or[3] & or[2] & or[1] & or[0] & cTemp)) + '0');
			s = FullAdder(s1[7], s2[7], c7).substring(0, 1)
					+ FullAdder(s1[6], s2[6], c6).substring(0, 1)
					+ FullAdder(s1[5], s2[5], c5).substring(0, 1)
					+ FullAdder(s1[4], s2[4], c4).substring(0, 1)
					+ FullAdder(s1[3], s2[3], c3).substring(0, 1)
					+ FullAdder(s1[2], s2[2], c2).substring(0, 1)
					+ FullAdder(s1[1], s2[1], c1).substring(0, 1)
					+ FullAdder(s1[0], s2[0], c).substring(0, 1) + c8;
			break;
		}
		}
		return s;
	}

	/**
	 * 进位先行加法器
	 * 
	 * @param operand1
	 * @param operand2
	 * @param c
	 * @param length
	 * @return
	 */
	public String Addition(String operand1, String operand2, char c, int length) {
		operand1 = expand(operand1, length);
		operand2 = expand(operand2, length);
		String s = "";
		int n = length / 8;
		String result = "";
		while (n != 0) {
			String temp1 = operand1.substring((n - 1) * 8, n * 8);
			String temp2 = operand2.substring((n - 1) * 8, n * 8);
			result = CLAAdder(temp1, temp2, c, 8);
			c = result.charAt(8);
			s = result.substring(0, 8) + s;
			n--;
		}

		// 判断是否溢出

		if ((operand1.charAt(0) == '1' && operand2.charAt(0) == '1')
				&& s.charAt(0) == '0') {
			s = s + "1";
		} else if ((operand1.charAt(0) == '0' && operand2.charAt(0) == '0')
				&& s.charAt(0) == '1') {
			s = s + "1";
		} else {
			s = s + "0";
		}

		return s;
	}

	/**
	 * 减法
	 * 
	 * @param operand1
	 * @param operand2
	 * @param length
	 * @return
	 */
	public String Subtraction(String operand1, String operand2, int length) {
		operand2 = Negation(operand2);
		return Addition(operand1, operand2, '1', length);
	}

	/**
	 * Booth乘法
	 * 
	 * @param operand1
	 * @param operand2
	 * @param length
	 * @return
	 */
	public String Multiplication(String operand1, String operand2, int length) {
		operand1 = expand(operand1, length);
		operand2 = expand(operand2, length);
		// System.out.print(operand1+","+operand1);
		String s0 = "";
		for (int i = 0; i < length; i++) {
			s0 = s0 + "0";
		}
		// System.out.print(s0);
		String s = s0 + operand2;
		int temp = 0;
		char c = '0';
		for (int i = 0; i < length; i++) {
			temp = (s.charAt(s.length() - 1) - '0') - (c - '0');
			// System.out.println(temp);
			if (temp == 0) {
				c = s.charAt(s.length() - 1);
				s = RightShift(s, 1);
				// System.out.println(s);
			} else if (temp == 1) {
				String s1 = s.substring(0, length);
				// System.out.println(s1);
				// 修正当length = 4或者其他小于8的数出现的错误
				s1 = Subtraction(s1, operand1, length * 2);
				s = s1.substring(length, length * 2) + s.substring(length);
				c = s.charAt(s.length() - 1);
				s = RightShift(s, 1);
			} else {
				String s1 = s.substring(0, length);
				s1 = Addition(s1, operand1, '0', length * 2);
				s = s1.substring(length, length * 2) + s.substring(length);
				c = s.charAt(s.length() - 1);
				s = RightShift(s, 1);
			}

		}
		// System.out.println(s);
		return s;
	}

	/**
	 * 不恢复余数除法
	 * 
	 * @param operand1
	 * @param operand2
	 * @param length
	 * @return
	 */
	public String Division(String operand1, String operand2, int length) {
		operand1 = expand(operand1, length);
		operand2 = expand(operand2, length);
		String s = expand(operand1, length * 2);// 被除数
		char c = ' ';
		if (operand1.charAt(0) == operand2.charAt(0)) {
			String temp = Subtraction(s.substring(0, length), operand2,
					length * 2).substring(length, length * 2);
			s = temp + s.substring(length);
		} else {
			String temp = Addition(s.substring(0, length), operand2, '0',
					length * 2).substring(length, length * 2);
			s = temp + s.substring(length);
		}
		if (s.charAt(0) == operand2.charAt(0)) {
			c = '1';
		} else {
			c = '0';
		}
		for (int i = 0; i < length; i++) {
			s = LeftShift(s, 1);
			s = s.substring(0, s.length() - 1) + c;
			if (c == '1') {
				String temp = Subtraction(s.substring(0, length), operand2,
						length * 2).substring(length, length * 2);
				s = temp + s.substring(length);
			} else {
				String temp = Addition(s.substring(0, length), operand2, '0',
						length * 2).substring(length, length * 2);
				s = temp + s.substring(length);
			}
			if (s.charAt(0) == operand2.charAt(0)) {
				c = '1';
			} else {
				c = '0';
			}
		}
		String remainder = s.substring(0, length);
		String quotient = s.substring(length);
		if (operand1.charAt(0) != remainder.charAt(0)) {
			if (operand1.charAt(0) == operand2.charAt(0)) {
				String temp = Addition(remainder, operand2, '0', length * 2);
				remainder = temp.substring(length, length * 2);
			} else {
				String temp = Subtraction(remainder, operand2, length * 2);
				remainder = temp.substring(length, length * 2);
			}
		}
		quotient = LeftShift(quotient, 1);
		quotient = quotient.substring(0, length - 1) + c;
		if (operand1.charAt(0) != operand2.charAt(0)) {
			quotient = Addition(quotient, "01", '0', length * 2);
			quotient = quotient.substring(length, length * 2);
		}
		return quotient + remainder;
	}

	/**
	 * 生成十进制浮点数的二进制表示
	 * 
	 * @param number
	 * @param sLength
	 * @param eLength
	 * @return
	 */
	public String FloatRepresentation(String number, int sLength, int eLength) {
		String s = "";
		String num1 = "";// 装整数部分
		String num2 = "";// 装小数部分
		// 数字等于0的特殊情况
		int index = number.indexOf(".");
		// 符号位的确定
		if (number.charAt(0) == '-') {
			s = "1";
			num1 = number.substring(1, index);

		} else {
			s = "0";
			num1 = number.substring(0, index);

		}
		num2 = number.substring(index + 1);
		num1 = Integer.toBinaryString(Integer.parseInt(num1));// num1现装着整数部分的二进制表示
		// System.out.println(num1);测试整数部分表示正确
		String num2B = "";// num2B表示小数部分的二进制表示
		num2 = "0." + num2;
		// 进行小数转化而二进制表示的运算
		float f = Float.parseFloat(num2);
		if (f == 0.0f) {
			num2B = "0";
		} else {
			while (f != 0) {
				f = f * 2.0f;
				if (f >= 1) {
					f = f - 1.0f;
					num2B = num2B + "1";
				} else {
					num2B = num2B + "0";
				}
			}
			// System.out.println(temp);测试小数部分表示正确
		}
		// 对形为0.x形式的小数进行转化
		if (num1.equals("0")) {
			if (num2B.equals("0")) {
				for (int i = 0; i < sLength + eLength; i++) {
					s = s + "0";
				}
				return s;
			} else {
				int e = num2B.indexOf("1") + 1;
				int ex = (int) (Math.pow(2, eLength - 1) - 1 - e);
				num2B = num2B.substring(e);
				int tempLength = num2B.length();
				for (int i = 0; i < sLength - tempLength; i++) {
					num2B = num2B + "0";
				}
				// System.out.println(temp);
				// System.out.println(String.valueOf(ex));结果是输入出了错，应该先输入尾数长再输入指数长，否则不对应
				num1 = Complement(String.valueOf(ex), eLength);
				s = s + num1 + num2B;
			}
			return s;
		}
		// 对形为1.x形式的小数进行转化
		if (num1.equals("1")) {
			int ex = (int) (Math.pow(2, eLength - 1) - 1);
			num1 = Complement(String.valueOf(ex), eLength);
			int tempLength = num2B.length();
			for (int i = 0; i < sLength - tempLength; i++) {
				num2B = num2B + "0";
			}
			s = s + num1 + num2B;
			return s;
		}
		// 对其他形式小数的转化
		int ind = num1.indexOf("1") + 1;
		int ex = num1.length() - ind;
		ex = (int) (Math.pow(2, eLength - 1) - 1 + ex);
		// System.out.println(ex);
		num2B = num1.substring(ind) + num2B;
		// System.out.println(num2B);
		num1 = Complement(String.valueOf(ex), eLength);
		// System.out.println(num2B);
		int tempLength = num2B.length();
		for (int i = 0; i < sLength - tempLength; i++) {
			num2B = num2B + "0";
		}
		s = s + num1 + num2B;
		return s;
	}

	/**
	 * 十进制浮点数的IEEE754表示
	 * 
	 * @param number
	 * @param length
	 * @return
	 */
	public String IEEE754(String number, int length) {
		if (length == 32) {
			return FloatRepresentation(number, 23, 8);
		} else {
			return FloatRepresentation(number, 52, 11);
		}

	}

	/**
	 * 加法写的我好累。。不想写了。。 加油QAQ坚持QAQ
	 * 
	 * @param operand1
	 * @param operand2
	 * @param sLength
	 * @param eLength
	 * @param gLength
	 * @return
	 */
	public String AdditionF(String operand1, String operand2, int sLength,
			int eLength, int gLength) {
		// ！！！全用substring简直要命！！重写！！！！
		int[] temp0 = { 23, 8 };// 只是为了调用trueValue方便
		char[] f1 = new char[sLength + gLength];// 尾数+保护位
		char[] f2 = new char[sLength + gLength];
		char[] op1 = operand1.toCharArray();// 为了下面对f1 f2进行初始化
		char[] op2 = operand2.toCharArray();
		char c0;// 装最后的符号位
		for (int i = 0; i < sLength; i++) {
			f1[i] = op1[i + eLength + 1];
			f2[i] = op2[i + eLength + 1];
		}
		// 初始化保护位
		for (int i = 0; i < gLength; i++) {
			f1[i + sLength] = '0';
			f2[i + sLength] = '0';
		}
		// 一个操作数为0的情况
		String detection = "";// 用来检测全为0的情况
		for (int i = 0; i < sLength + eLength; i++) {
			detection = detection + "0";
		}
		if (operand1.equals("1" + detection)
				|| operand1.equals("0" + detection)) {
			return operand2 + "0";
		} else if (operand2.equals("1" + detection)
				|| operand2.equals("0" + detection)) {
			return operand1 + "0";
		}
		int ex1 = Integer.parseInt(TrueValue("0"
				+ operand1.substring(1, eLength + 1), Type.INTEGER, temp0))
				- (int) Math.pow(2, eLength - 1) + 1;
		int ex2 = Integer.parseInt(TrueValue("0"
				+ operand2.substring(1, eLength + 1), Type.INTEGER, temp0))
				- (int) Math.pow(2, eLength - 1) + 1;
		// 加上隐藏位,现在的长为sLength+1
		if (ex1 == 2 - (int) Math.pow(2, eLength - 1)) {
			f1 = ("0" + toString_1(f1)).toCharArray();
		} else {
			f1 = ("1" + toString_1(f1)).toCharArray();
		}
		if (ex2 == 2 - (int) Math.pow(2, eLength - 1)) {
			f2 = ("0" + toString_1(f2)).toCharArray();
		} else {
			f2 = ("1" + toString_1(f2)).toCharArray();
		}
		// 对阶
		while (ex1 != ex2) {
			if (ex1 > ex2) {
				f2 = ("0" + toString_1(f2)).substring(0, sLength + gLength + 1)
						.toCharArray();
				ex2 = ex2 + 1;
				// 在右移的过程中有一个操作数为0
				if (TrueValue(toString_1(f2), Type.INTEGER, temp0).equals("0")) {
					return operand1 + "0";
				}
			} else {
				f1 = ("0" + toString_1(f1)).substring(0, sLength + gLength + 1)
						.toCharArray();
				ex1 = ex1 + 1;
				if (TrueValue(toString_1(f1), Type.INTEGER, temp0).equals("0")) {
					return operand2 + "0";
				}
			}
		}
		int exponent = ex1;// exponent中放的是那个对完阶的指数值
		char[] c = new char[sLength + gLength + 1];
		// 进行计算！先判断符号位
		if (operand1.charAt(0) != operand2.charAt(0)) {
			// 异号做减法，也就是加上加数的补码
			String s = addtion(toString_1(f1), Negation(toString_1(f2)), '1',
					sLength + gLength + 1);
			// 异号时不溢出，要修正，取反加一
			if (s.charAt(sLength + gLength + 1) != '1') {
				if (operand1.charAt(0) == '0') {
					c0 = '1';
				} else {
					c0 = '0';
				}
				s = addtion(Negation(s.substring(0, sLength + gLength + 1)),
						"0", '1', sLength + gLength + 1).substring(0,
						sLength + gLength + 1);
			} else {
				c0 = operand1.charAt(0);
				s = s.substring(0, sLength + gLength + 1);
			}
			c = s.toCharArray();
		} else {
			c0 = operand1.charAt(0);
			String s = addtion(toString_1(f1), toString_1(f2), '0', sLength
					+ gLength + 1);
			// 溢出，右移，指数加1判断
			if (s.charAt(sLength + gLength + 1) == '1') {
				s = ("1" + s.substring(0, sLength + gLength + 1)).substring(0,
						sLength + gLength + 1);
				exponent = exponent + 1;
				if (exponent > Math.pow(2, eLength) - 1) {
					String s0 = "";
					for (int i = 0; i < sLength + eLength + 1; i++) {
						s0 = s0 + "0";
					}
					return s0 + "1";
				}
			} else {
				s = s.substring(0, sLength + gLength + 1);
			}
			c = s.toCharArray();
		}
		// 做完计算后有效值判断是否为0
		if (TrueValue(toString_1(f1), Type.INTEGER, temp0).equals("0")) {
			String s0 = "";
			for (int i = 0; i < sLength + eLength + 1; i++) {
				s0 = s0 + "0";
			}
			return s0 + "0";
		}
		// 规格化 从这开始感觉有问题
		while (c[0] != '1') {
			c = LeftShift(toString_1(c), 1).toCharArray();
			exponent = exponent - 1;
			if (exponent < (2 - (int) (Math.pow(2, eLength - 1)))) {
				String s0 = "";
				for (int i = 0; i < sLength + eLength + 1; i++) {
					s0 = s0 + "0";
				}
				return s0 + "1";
			}
		}
		// 下面看保护位
		char[] result = new char[sLength + 1];
		if (c[sLength + 1] == '1') {
			result = addtion(toString_1(c).substring(0, sLength), "0", '1',
					sLength + 1).toCharArray();
			// System.out.println(result);现在result是sLength+2位,看最后一位有没有溢出
			if (result[sLength + 1] == '1') {
				result = ("1" + toString_1(result)).substring(0, sLength + 1)
						.toCharArray();
				exponent = exponent + 1;
				if (exponent > (-1) + (int) (Math.pow(2, eLength - 1))) {
					String s0 = "";
					for (int i = 0; i < sLength + eLength + 1; i++) {
						s0 = s0 + "0";
					}
					return s0 + "1";
				}
			} else {
				result = toString_1(result).substring(0, sLength + 1)
						.toCharArray();
			}
		} else {
			result = toString_1(c).substring(0, sLength + 1).toCharArray();
		}
		// System.out.println(result);24位
		// System.out.println("here");
		return c0
				+ Complement(
						String.valueOf(exponent
								+ (int) Math.pow(2, eLength - 1) - 1),
						eLength + 1).substring(1)
				+ toString_1(result).substring(1) + "0";

	}

	/**
	 * 浮点数减法
	 * 
	 * @param operand1
	 * @param operand2
	 * @param sLength
	 * @param eLength
	 * @param gLength
	 * @return
	 */
	public String SubtractionF(String operand1, String operand2, int sLength,
			int eLength, int gLength) {
		if (operand2.charAt(0) == '0') {
			operand2 = operand2.substring(1);
			operand2 = "1" + operand2;
		} else {
			operand2 = operand2.substring(1);
			operand2 = "0" + operand2;
		}
		return AdditionF(operand1, operand2, sLength, eLength, gLength);
	}

	/**
	 * 获得正无穷的表示方法
	 * 
	 * @param sLength
	 * @param eLength
	 * @return
	 */
	public String getInf(int sLength, int eLength) {
		String s = "0";
		for (int i = 0; i < eLength; i++) {
			s = s + "1";
		}
		for (int i = 0; i < sLength; i++) {
			s = s + "0";
		}
		return s;
	}

	/**
	 * 获得0的表示方法
	 * 
	 * @param sLength
	 * @param eLength
	 * @return
	 */
	public String getZero(int sLength, int eLength) {
		String s = "0";
		for (int i = 0; i < eLength; i++) {
			s = s + "0";
		}
		for (int i = 0; i < sLength; i++) {
			s = s + "0";
		}
		return s;
	}

	/**
	 * 浮点数乘法
	 * 
	 * @param operand1
	 * @param operand2
	 * @param sLength
	 * @param eLength
	 * @return
	 */
	public String MultiplicationF(String operand1, String operand2,
			int sLength, int eLength) {
		String temp = "";
		char fuhao;
		if (operand1.charAt(0) == operand2.charAt(0)) {
			fuhao = '0';
		} else {
			fuhao = '1';
		}
		int[] temp0 = { 23, 8 };// 只是为了调用trueValue方便
		// 小数部分
		String op1 = operand1.substring(eLength + 1);
		String op2 = operand2.substring(eLength + 1);
		char[] p = new char[sLength + 1];// 乘积
		char[] result = new char[2 * (sLength + 1)];
		for (int i = 0; i < sLength + eLength; i++) {
			temp = temp + "0";
		}
		if (operand1.substring(1).equals(temp)
				|| operand2.substring(1).equals(temp)) {
			return temp + "0";
		}
		// 下面是对于指数的计算：
		int ex1 = Integer.parseInt(TrueValue("0"
				+ operand1.substring(1, eLength + 1), Type.INTEGER, temp0))
				- (int) Math.pow(2, eLength - 1) + 1;
		int ex2 = Integer.parseInt(TrueValue("0"
				+ operand2.substring(1, eLength + 1), Type.INTEGER, temp0))
				- (int) Math.pow(2, eLength - 1) + 1;
		int exStandard = (int) (Math.pow(2, eLength - 1) - 1);
		int ex = ex1 + ex2;
		if (ex > exStandard) {
			// 怎么报告上溢？(inf)
			return getInf(sLength, eLength);
		}
		if (ex < (2 - (int) Math.pow(2, eLength - 1))) {
			return getZero(sLength, eLength);
		}
		if (ex1 == 2 - (int) Math.pow(2, eLength - 1)) {
			op1 = "0" + op1;
		} else {
			op1 = "1" + op1;
		}
		if (ex2 == 2 - (int) Math.pow(2, eLength - 1)) {
			op2 = "0" + op2;
		} else {
			op2 = "1" + op2;
		}
		for (int i = 0; i < sLength + 1; i++) {
			p[i] = '0';
			result[i] = '0';
			result[i + sLength + 1] = op2.charAt(i);
		}
		// System.out.println(result);
		char c0 = '0';// 最后一个溢出位
		for (int i = 0; i < sLength + 1; i++) {
			if (result[2 * (sLength + 1) - 1] == '1') {
				String t = addtion(toString_1(p), op1, '0', sLength + 1);
				c0 = t.charAt(t.length() - 1);
				p = t.substring(0, sLength + 1).toCharArray();
			}
			for (int j = 0; j < (sLength + 1); j++) {
				result[j] = p[j];
			}
			// 右移结果
			result = (c0 + toString_1(result)).substring(0, 2 * (sLength + 1))
					.toCharArray();
			for (int j = 0; j < (sLength + 1); j++) {
				p[j] = result[j];
			}
		}
		// 现在的情况是乘完之后前两位都是小数点前的数字
		if (result[0] == '1') {
			result = ("0" + toString_1(result)).substring(0, 2 * (sLength + 1))
					.toCharArray();
			ex = ex + 1;
		}
		while (result[1] != '1') {
			result = LeftShift(toString_1(result), 1).toCharArray();
			ex = ex - 1;
			// 阶码下溢
			if (ex < (2 - (int) (Math.pow(2, eLength - 1)))) {
				return getZero(sLength, eLength);
			}
		}
		char[] c = new char[sLength + 1];
		for (int i = 0; i < sLength + 1; i++) {
			c[i] = result[i + 1];
		}
		// 结果最后移出来一位，要进1
		if (result[sLength + 2] == '1') {
			char[] tempc = addtion(toString_1(c), "0", '1', sLength + 1)
					.toCharArray();
			// System.out.println(tempc.length);
			// 判断加上1后是否溢出
			if (tempc[tempc.length - 1] == '1') {
				c = ("1" + toString_1(tempc).substring(0, sLength + 1))
						.substring(0, sLength + 1).toCharArray();
				ex = ex + 1;
				if (ex > (-1) + (int) (Math.pow(2, eLength - 1))) {
					return getInf(sLength, eLength);
				}
			} else {
				// 去掉最后的溢出位
				c = toString_1(tempc).substring(0, sLength + 1).toCharArray();
			}
		}
		return fuhao
				+ Complement(
						String.valueOf(ex - 1 + (int) Math.pow(2, eLength - 1)),
						eLength + 1).substring(1) + toString_1(c).substring(1);

	}

	/**
	 * 浮点数除法
	 * 
	 * @param operand1
	 * @param operand2
	 * @param sLength
	 * @param eLength
	 * @return
	 */
	public String DivisionF(String operand1, String operand2, int sLength,
			int eLength) {
		char fuhao;
		if (operand1.charAt(0) == operand2.charAt(0)) {
			fuhao = '0';
		} else {
			fuhao = '1';
		}
		char[] r = new char[sLength + 1];// 余数
		char[] q = new char[sLength + 1];// 商
		char[] result = new char[2 * sLength + 3];// 2个数（算上隐藏位）再加上最后的欲商
		String op1 = operand1.substring(eLength + 1);
		String op2 = operand2.substring(eLength + 1);
		int[] temp0 = { 23, 8 };// 只是为了调用trueValue方便
		String temp = "";
		for (int i = 0; i < sLength + eLength; i++) {
			temp = temp + "0";
		}
		// 被除数为0
		if (operand1.substring(1).equals(temp)) {
			return temp + "0";
		}
		// 除数为0
		if (operand2.substring(1).equals(temp)) {
			if (operand2.charAt(0) == '0') {
				temp = "0";
			} else {
				temp = "1";
			}
			for (int i = 0; i < eLength; i++) {
				temp = temp + "1";
			}
			for (int i = 0; i < sLength; i++) {
				temp = temp + "0";
			}
			return temp;
		}
		int ex1 = Integer.parseInt(TrueValue("0"
				+ operand1.substring(1, eLength + 1), Type.INTEGER, temp0))
				- (int) Math.pow(2, eLength - 1) + 1;
		int ex2 = Integer.parseInt(TrueValue("0"
				+ operand2.substring(1, eLength + 1), Type.INTEGER, temp0))
				- (int) Math.pow(2, eLength - 1) + 1;
		int exStandard = (int) (Math.pow(2, eLength - 1) - 1);
		int ex = ex1 - ex2;
		if (ex > exStandard) {
			return getInf(sLength, eLength);
		}
		if (ex < (2 - (int) Math.pow(2, eLength - 1))) {
			return getZero(sLength, eLength);
		}
		if (ex1 == 2 - (int) Math.pow(2, eLength - 1)) {
			op1 = "0" + op1;
		} else {
			op1 = "1" + op1;
		}
		if (ex2 == 2 - (int) Math.pow(2, eLength - 1)) {
			op2 = "0" + op2;
		} else {
			op2 = "1" + op2;
		}
		// 初始化r和q数组
		for (int i = 0; i < sLength + 1; i++) {
			result[i] = op1.charAt(i);
			r[i] = op1.charAt(i);
			q[i] = '0';
			result[i + sLength + 1] = '0';
		}
		// 不知道这里采用的是什么计算方法，我用的是够就减，不够就加
		for (int i = 0; i < sLength + 1; i++) {
			if (Integer.parseInt(TrueValue("0" + toString_1(r), Type.INTEGER,
					temp0)) >= Integer.parseInt(TrueValue("0" + op2,
					Type.INTEGER, temp0))) {
				r = Complement(
						String.valueOf(Integer.parseInt(TrueValue("0"
								+ toString_1(r), Type.INTEGER, temp0))
								- Integer.parseInt(TrueValue("0" + operand2,
										Type.INTEGER, temp0))), sLength + 2)
						.substring(1).toCharArray();
				result[2 * sLength + 2] = '1';
			} else {
				result[2 * sLength + 2] = '0';
			}
			// 把r中的内容更新到result中
			for (int j = 0; j < (sLength + 1); j++) {
				result[j] = r[j];
			}
			// 然后再左移
			result = LeftShift(toString_1(result), 1).toCharArray();
			// 左移之后再把结果更新到商和余数中
			for (int j = 0; j < sLength + 1; j++) {
				r[j] = result[j];
				q[j] = result[j + sLength + 1];
			}
		}
		// 对商进行规格化
		// 因为是1.几除以1.几，因此商为1.几或者0.几，不会出现上溢，只要看是不是要左移即可。
		while (q[0] != '1') {
			result = LeftShift(toString_1(q), 1).toCharArray();
			ex = ex - 1;
			for (int j = 0; j < sLength + 1; j++) {
				r[j] = result[j];
				q[j] = result[j + sLength + 1];
			}
			// 下溢
			if (ex < (2 - (int) (Math.pow(2, eLength - 1)))) {
				return getZero(sLength, eLength);
			}
		}

		return fuhao
				+ Complement(
						String.valueOf(ex - 1 + (int) Math.pow(2, eLength - 1)),
						eLength + 1).substring(1) + toString_1(q).substring(1);

	}

	/**
	 * 十进制整数的NBCD表示
	 * 
	 * @param number
	 * @return
	 */
	public String NBCD(String number) {
		String s = "";
		char[] c;
		if (number.charAt(0) == '-') {
			s = "1101";
			c = number.substring(1).toCharArray();
		} else {
			s = "1100";
			c = number.toCharArray();
		}
		for (int i = 0; i < c.length; i++) {
			// System.out.println(c[i]);
			switch (c[i]) {
			case '0':
				s = s + "0000";
				break;
			case '1':
				s = s + "0001";
				break;
			case '2':
				s = s + "0010";
				break;
			case '3':
				s = s + "0011";
				break;
			case '4':
				s = s + "0100";
				break;
			case '5':
				s = s + "0101";
				break;
			case '6':
				s = s + "0110";
				break;
			case '7':
				s = s + "0111";
				break;
			case '8':
				s = s + "1000";
				break;
			case '9':
				s = s + "1001";
				break;
			}
		}
		return s;
	}

	public String AdditionD(String operand1, String operand2) {
		if (!operand1.substring(0, 4).equals(operand2.substring(0, 4))) {
			if (operand2.substring(0, 4).equals("1100")) {
				return SubtractionD(operand1, "1101" + operand2.substring(4));
			} else {
				return SubtractionD(operand1, "1100" + operand2.substring(4));
			}
		}
		String s = operand1.substring(0, 4);
		String op1 = operand1.substring(4);
		String op2 = operand2.substring(4);
		int length = 0;
		if (op1.length() > op2.length()) {
			length = op1.length();
			op2 = expand("0" + op2, length);
		} else if (op1.length() < op2.length()) {
			length = op2.length();
			op1 = expand("0" + op1, length);
		} else {
			length = op1.length();
		}

		String[] o1 = new String[length / 4];
		String[] o2 = new String[length / 4];
		String[] result = new String[length / 4];
		String adjustion = "0000";
		for (int i = 0; i < length / 4; i++) {
			o1[i] = op1.substring(i * 4, (i + 1) * 4);
			// System.out.println(o1[i]);
			o2[i] = op2.substring(i * 4, (i + 1) * 4);
			// System.out.println(o1[i]);
		}
		for (int i = o1.length - 1; i >= 0; i--) {
			String temp = Addition("0" + o1[i], "0" + o2[i], '0', 8);
			// System.out.println(temp);
			temp = Addition(temp, adjustion, '0', 8);
			if (Integer.valueOf(temp.substring(0, 8), 2) <= 9) {
				result[i] = temp.substring(4, 8);
				adjustion = "0000";
			} else if ((Integer.valueOf(temp.substring(0, 8), 2) > 9)
					&& (Integer.valueOf(temp.substring(0, 8), 2) < 16)) {
				String temp1 = Addition("0" + temp.substring(4, 8), "0110",
						'0', 8);
				result[i] = temp1.substring(4, 8);
				adjustion = "0001";
			} else {
				adjustion = "0001";
				String temp1 = Addition(temp.substring(4, 8), "0110", '0', 8);
				result[i] = temp1.substring(4, 8);
				if (temp1.charAt(3) == '1') {
					adjustion = "0010";
				}
			}
		}
		if (adjustion.equals("0001")) {
			s = s + "0001";
		}
		if (Integer.valueOf(result[0], 2) > 9) {
			String temp = Addition(result[0], "0110", '0', 8);
			result[0] = temp.substring(4, 8);
			s = s + temp.substring(0, 4);
		}
		for (int i = 0; i < result.length; i++) {
			s = s + result[i];
		}
		return s;
	}
/**
 * BDC减法
 * @param operand1
 * @param operand2
 * @return
 */
	public String SubtractionD(String operand1, String operand2) {
		// 符号不同，变号做加法
		if (!operand1.substring(0, 4).equals(operand2.substring(0, 4))) {
			if (operand2.substring(0, 4).equals("1100")) {
				return AdditionD(operand1, "1101" + operand2.substring(4));
			} else {
				return AdditionD(operand1, "1100" + operand2.substring(4));
			}

		}
		String s = operand1.substring(0, 4);
		String op1 = operand1.substring(4);
		String op2 = operand2.substring(4);
		int length = 0;
		if (op1.length() > op2.length()) {
			length = op1.length();
			op2 = expand("0" + op2, length);
		} else if (op1.length() < op2.length()) {
			length = op2.length();
			op1 = expand("0" + op1, length);
		} else {
			length = op1.length();
		}

		String[] o1 = new String[length / 4];
		String[] o2 = new String[length / 4];
		String[] result = new String[length / 4];
		String adjustion = "0000";
		for (int i = 0; i < length / 4; i++) {
			o1[i] = op1.substring(i * 4, (i + 1) * 4);
			o2[i] = op2.substring(i * 4, (i + 1) * 4);
			if (o2[i].equals("0000")) {
				o2[i] = "1001";
			} else if (o2[i].equals("0001")) {
				o2[i] = "1000";
			} else if (o2[i].equals("0010")) {
				o2[i] = "0111";
			} else if (o2[i].equals("0011")) {
				o2[i] = "0110";
			} else if (o2[i].equals("0100")) {
				o2[i] = "0101";
			} else if (o2[i].equals("0101")) {
				o2[i] = "0100";
			} else if (o2[i].equals("0110")) {
				o2[i] = "0011";
			} else if (o2[i].equals("0111")) {
				o2[i] = "0010";
			} else if (o2[i].equals("1000")) {
				o2[i] = "0001";
			} else if (o2[i].equals("1001")) {
				o2[i] = "0000";
			}
		}
		o2[length / 4 - 1] = Addition("0" + o2[length / 4 - 1], "0001", '0', 8)
				.substring(4, 8);
		for (int i = o1.length - 1; i >= 0; i--) {
			String temp = Addition("0" + o1[i], "0" + o2[i], '0', 8);
			temp = Addition(temp, adjustion, '0', 8);
			if (Integer.valueOf(temp.substring(0, 8), 2) <= 9) {
				result[i] = temp.substring(4, 8);
				adjustion = "0000";
			} else if ((Integer.valueOf(temp.substring(0, 8), 2) > 9)
					&& (Integer.valueOf(temp.substring(0, 8), 2) < 16)) {
				String temp1 = Addition("0" + temp.substring(4, 8), "0110",
						'0', 8);
				result[i] = temp1.substring(4, 8);
				adjustion = "0001";
			} else {
				adjustion = "0001";
				String temp1 = Addition(temp.substring(4, 8), "0110", '0', 8);
				result[i] = temp1.substring(4, 8);
				if (temp1.charAt(3) == '1') {
					adjustion = "0010";
				}
			}
		}

		if (Integer.valueOf(result[0], 2) > 9) {
			String temp = Addition(result[0], "0110", '0', 8);
			result[0] = temp.substring(4, 8);
			for (int i = 0; i < result.length; i++) {
				s = s + result[i];
			}
			return s;
		}
		if (adjustion.equals("0001")) {
			for (int i = 0; i < result.length; i++) {
				s = s + result[i];
			}
			return s;
		}
		//有个bug，若最后取反加一时所得数最后一位大于9时还得修正
		if (s.equals("1100")) {
			s = "1101";
		} else {
			s = "1100";
		}
		for (int i = 0; i < result.length; i++) {
			if (result[i].equals("0000")) {
				result[i] = "1001";
			} else if (result[i].equals("0001")) {
				result[i] = "1000";
			} else if (result[i].equals("0010")) {
				result[i] = "0111";
			} else if (result[i].equals("0011")) {
				result[i] = "0110";
			} else if (result[i].equals("0100")) {
				result[i] = "0101";
			} else if (result[i].equals("0101")) {
				result[i] = "0100";
			} else if (result[i].equals("0110")) {
				result[i] = "0011";
			} else if (result[i].equals("0111")) {
				result[i] = "0010";
			} else if (result[i].equals("1000")) {
				result[i] = "0001";
			} else if (result[i].equals("1001")) {
				result[i] = "0000";
			}
		}
		//最后一位加一
		result[result.length - 1] = Addition("0" + result[result.length - 1],
				"0001", '0', 8).substring(4, 8);
		//修正上面所说的bug
		String adjustion1 = "0000";
		for(int i = result.length-1;i>=0;i--){
			result[i] = Addition("0"+result[i],adjustion1,'0',8).substring(4,8);
			//System.out.println("result"+result[i]);
			if(Integer.valueOf(result[i],2)>9){
				String temp1 = Addition("0" + result[i], "0110",
						'0', 8);
				result[i] = temp1.substring(4, 8);
				adjustion1 = "0001";
				//System.out.println("ad"+adjustion1);
			}else{
				adjustion1 = "0000";
			}
		}
		if(adjustion1.equals("0001")){
			s = s+"0001";
		}
		for (int i = 0; i < result.length; i++) {
			s = s + result[i];
		}
		return s;
	}

	public String Calculation(String number1, String number2, Type type,
			Operation operation, int[] length) {
		String result = "";
		if (type == Type.INTEGER) {
			String s1 = Complement(number1, length[0]);
			String s2 = Complement(number2, length[0]);
			switch (operation) {
			case ADDITION: {
				result = Addition(s1, s2, '0', length[0]).substring(0,
						length[0]);
				// System.out.println(result);
				break;
			}
			case SUBTRACTION: {
				result = Subtraction(s1, s2, length[0]).substring(0, length[0]);
				break;
			}
			case MULTIPLICATION: {
				result = Multiplication(s1, s2, length[0]);
				// System.out.println(result);
				break;
			}
			case DIVISION: {
				result = Division(s1, s2, length[0]).substring(0, length[0]);
			}
			}
			result = TrueValue(result, Type.INTEGER, length);
		} else if (type == Type.FLOAT) {
			int temp1 = number1.indexOf('.');
			if (temp1 < 0) {
				number1 = number1 + ".0";
			}
			int temp2 = number2.indexOf('.');
			if (temp2 < 0) {
				number2 = number2 + ".0";
			}
			String s1 = FloatRepresentation(number1, length[0], length[1]);
			String s2 = FloatRepresentation(number2, length[0], length[1]);
			switch (operation) {
			case ADDITION: {
				result = AdditionF(s1, s2, length[0], length[1], length[2])
						.substring(0, length[0] + length[1] + 1);
				// System.out.println(result);
				break;
			}
			case SUBTRACTION: {
				result = SubtractionF(s1, s2, length[0], length[1], length[2])
						.substring(0, length[0] + length[1] + 1);
				break;
			}
			case MULTIPLICATION: {
				result = MultiplicationF(s1, s2, length[0], length[1]);
				// System.out.println(result);
				break;
			}
			case DIVISION: {
				result = DivisionF(s1, s2, length[0], length[1]);
			}
			}
			result = TrueValue(result, Type.FLOAT, length);
			int temp = result.indexOf('.');
			if (temp > 0 && result.substring(temp + 1).equals("0")) {
				result = result.substring(0, temp);
			}
		}else if(type==Type.DECIMAL){
			String s1 = NBCD(number1);
			String s2 = NBCD(number2);
			switch (operation){
			case ADDITION:result = AdditionD(s1,s2);
			break;
			case SUBTRACTION:result = SubtractionD(s1,s2);
			break;
			}
			result =  TrueValue(result, Type.DECIMAL, length);
		}
		return result;
	}

	/**
	 * 另一个加法
	 * 
	 * @param operand1
	 * @param operand2
	 * @param c
	 * @param length
	 * @return
	 */
	public String addtion(String operand1, String operand2, char c, int length) {
		char[] op1 = operand1.toCharArray();
		char[] op2 = operand2.toCharArray();
		while (op1.length < length) {
			operand1 = op1[0] + operand1;
			op1 = operand1.toCharArray();
		}
		while (op2.length < length) {
			operand2 = op2[0] + operand2;
			op2 = operand2.toCharArray();
		}
		int[] p = new int[length];
		int[] g = new int[length];
		char[] allC = new char[length];
		char[] out = new char[length + 1];
		int intOfC = c - '0';
		for (int i = 0; i < length; i++) {
			p[i] = (op1[i] - '0') | (op2[i] - '0');
		}
		for (int i = 0; i < length; i++) {
			g[i] = (op1[i] - '0') & (op2[i] - '0');
		}
		for (int i = 0; i < length; i++) {
			int total = 0;
			for (int k = 0; k < length - i + 1; k++) {
				int temp = 1;
				if (i + k < length) {
					temp = temp & g[i + k];
				}
				if (k == length - i) {
					temp = temp & intOfC;
				}
				if (k != 0) {
					for (int j = i; j < k + i; j++) {
						temp = temp & p[j];
					}
				}
				total = total | temp;
			}
			allC[i] = (char) (total + '0');
		}
		String str = FullAdder(op1[length - 1], op2[length - 1], c);
		char[] sep = str.toCharArray();
		out[length - 1] = sep[0];
		for (int i = 0; i < length - 1; i++) {
			String string = FullAdder(op1[i], op2[i], allC[i + 1]);
			char[] separate = string.toCharArray();
			out[i] = separate[0];
		}
		out[length] = allC[0];
		return toString_1(out);
	}

	/**
	 * 把数组转化为字符串
	 * 
	 * @param c
	 * @return
	 */
	public String toString_1(char[] c) {
		String s = "";
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '0') {
				s = s + '0';
			} else {
				s = s + '1';
			}
		}
		return s;
	}
}
