import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(Parameterized.class)
public class ALUTest {

	/** 需要测试的类 */
	private ALU alu = new ALU();
	private String number1;
	private String number2;
	private ALU.Type type;
	private ALU.Operation operation;
	private int length;
	private String result;

	@Parameters
	public static List<Object[]> data() {
		ArrayList<Object[]> objArr = new ArrayList<Object[]>();
		Object[] temp;
		int num1;
		int num2;

		int range = 4000;
		int range2 = range << 1;

		for (int i = 0; i < 10000; i++) {
			temp = new Object[6];
			temp[2] = ALU.Type.INTEGER;

			num1 = (int) (range2 * Math.random() - range);
			num2 = (int) (range2 * Math.random() - range);

			switch ((int) (4 * Math.random())) {
			case 0:
				temp[3] = ALU.Operation.ADDITION;
				temp[5] = String.valueOf(num1 + num2);
				break;
			case 1:
				temp[3] = ALU.Operation.SUBTRACTION;
				temp[5] = String.valueOf(num1 - num2);
				break;
			case 2:
				temp[3] = ALU.Operation.MULTIPLICATION;
				temp[5] = String.valueOf(num1 * num2);
				break;
			case 3:
				temp[3] = ALU.Operation.DIVISION;
				// 除数和被除数
				while (num2 == 0 || (num1 % num2) == 0) {
					num1 = (int) (range2 * Math.random() - range);
					num2 = (int) (range2 * Math.random() - range);
				}
				temp[5] = String.valueOf(num1 / num2);
				break;
			}
			temp[0] = String.valueOf(num1);
			temp[1] = String.valueOf(num2);

			temp[4] = 32;
			objArr.add(temp);
		}

		Object[][] res = new Object[objArr.size()][];
		objArr.toArray(res);

		return Arrays.asList(res);
	}

	/**
	 * @param number1
	 *            操作数 1，用十进制表示，为被加数/被减数/被乘数/被除数
	 * @param number2
	 *            操作数 2，用十进制表示，为加数/减数/乘数/除数
	 * @param type
	 *            操作数类型
	 * @param operation
	 *            操作类型
	 * @return 计算结果，用十进制表示。其中，加法返回和，减法返回差，乘法返回积，除法返回商。如果是负数，最左边为“-”；如果是正数或 0，不
	 *         需要符号位
	 * @author cylong
	 * @version May 23, 2014 3:27:47 PM
	 */
	public ALUTest(String number1, String number2, ALU.Type type,
			ALU.Operation operation, int length, String result) {
		this.number1 = number1;
		this.number2 = number2;
		this.type = type;
		this.operation = operation;
		this.length = length;
		this.result = result;
	}

	/**
	 * Test method for
	 * {@link ALU#Calculation(java.lang.String, java.lang.String, ALU.Type, ALU.Operation, int)}
	 * .
	 */
	@Test
	public void testCalculation() {
		String res = alu.Calculation(number1, number2, type, operation, length);
		if (!res.equals(result)) {
			System.out.println(operation);
			System.out.println("operand1:" + number1);
			System.out.println("operand2:" + number2);
			System.out.println("expected:" + result);
			System.out.println("result  :" + res);
			System.out.println();
		}
		assertEquals(result, res);
	}

}
