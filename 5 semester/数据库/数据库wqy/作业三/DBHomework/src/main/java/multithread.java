import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static com.sun.tools.doclint.Entity.ge;
import static javafx.scene.input.KeyCode.T;


/**
 * Created by Alisa on 16/11/8.
 */
public class multithread extends Thread {
    public static Connection conn;



    public static void main(String[] args) {

        try {
            conn = SqlConfig.getConnection();
            conn.setAutoCommit(false);
            System.out.println("G13\t2016-12-2\t南京南\t上海虹桥   商务座剩余: 1");


        } catch (SQLException e) {
            System.out.println("error");
            e.printStackTrace();
        }

        multithread a = new multithread();
        a.setName("顾客A");
        multithread b = new multithread();
        b.setName("顾客B");
        a.start();
        b.start();


//        try {
//            conn.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    public void run() {

        getRemain(Thread.currentThread().getName());
    }

    public static synchronized void getRemain(String name) {
        String time = "2016-12-2";
        String start = "南京南";
        String end = "上海虹桥";
        String route = "";
        String carno = "G13";
        String seattype = "商务座";
        int num = 1;
        System.out.print(name);
        try {
            PreparedStatement ps1 = conn.prepareStatement
                    ("select * from train where start =? and end = ? and time=? and carNumber=?;");
            ps1.setString(1, "南京南");
            ps1.setString(2, "上海虹桥");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            java.sql.Date sDate = java.sql.Date.valueOf("2016-12-2");
            ps1.setDate(3, sDate);
            ps1.setString(4, "G13");
            ResultSet rs = ps1.executeQuery();
            conn.commit();
            int remain = 0;
            int i = 0;
            int carriageNo = 0;
            int seatno = 0;
            if (rs.next()) {
                if (seattype.equals("商务座")) {//商务座为第一节车厢,200个位置
                    remain = rs.getInt(5);
                } else if (seattype.equals("一等座")) {//一等座为第2-3个车厢,400个位置
                    remain = rs.getInt(6);
                } else if (seattype.equals("二等座")) {//二等座为4-8车箱,1000个位置
                    remain = rs.getInt(7);
                } else {//无座
                    remain = rs.getInt(8);
                }
                route = rs.getString(9);
                while (num > 0) {
                    i++;
                    if (seattype.equals("商务座")) {//商务座为第一节车厢,200个位置
                        carriageNo = 1;
                        seatno = 200 - (remain - 1) % 200;
                    } else if (seattype.equals("一等座")) {//一等座为第2-3个车厢,400个位置
                        carriageNo = 2;
                        seatno = 400 - (remain - 1) % 400;
                        if (seatno > 200) {
                            carriageNo = 3;
                            seatno = seatno - 200;
                        }
                    } else if (seattype.equals("二等座")) {//二等座为4-8车箱,1000个位置
                        carriageNo = 4;
                        seatno = 1000 - (remain - 1) % 1000;
                        if (seatno > 200 && seatno <= 400) {
                            carriageNo = 5;
                            seatno = seatno - 200;
                        } else if (seatno > 400 && seatno <= 600) {
                            carriageNo = 6;
                            seatno = seatno - 400;
                        } else if (seatno > 600 && seatno <= 800) {
                            carriageNo = 7;
                            seatno = seatno - 600;
                        } else if (seatno > 800 && seatno <= 1000) {
                            carriageNo = 8;
                            seatno = seatno - 800;
                        }
                    } else {//无座
                        carriageNo = 5;
                    }
                    if (remain < num) {
                        System.out.println("没有符合类型的车票");
                        return;
                    }
                    if (seatno != 0) {
                        System.out.println("正在为您打印车票!");
                        System.out.println(rs.getString(1) + "  "
                                + rs.getDate(2) + " 出发站:" + rs.getString(3) +
                                " 下车站:" + rs.getString(4) +
                                " 车厢号:" +
                                carriageNo + " 座位号:" +
                                seatno + " 座位类型:" +
                                seattype);

                    } else {
                        System.out.println(rs.getString(1) + "  "
                                + rs.getDate(2) + " 出发站:" + rs.getString(3) +
                                " 下车站:" + rs.getString(4) +
                                " 车厢号:" +
                                carriageNo + " 座位类型:" +
                                seattype);
                    }
                    num--;
                    remain--;

                }

            }
            if (i == 0) {
                System.out.println("没有符合类型的车票");
            }

            //更新余票
            PreparedStatement ps2 = null;

            if (seattype.equals("商务座")) {
                ps2 = conn.prepareStatement
                        ("update train set businessRemain=? where start like ? and end like ? and time=? and carNumber=?");

            } else if (seattype.equals("一等座")) {//一等座为第2-3个车厢,400个位置
                ps2 = conn.prepareStatement
                        ("update train set firstRemain=? where start like ? and end like ? and time=? and carNumber=?");
            } else if (seattype.equals("二等座")) {//二等座为4-8车箱,1000个位置
                ps2 = conn.prepareStatement
                        ("update train set secondRemain=? where start like ? and end like ? and time=? and carNumber=?");
            } else {//无座
                ps2 = conn.prepareStatement
                        ("update train set noSeatRemain=? where start like ? and end like ? and time=? and carNumber=?");
            }
            PreparedStatement ps3 = conn.prepareStatement("select * from train where time=? and carNumber=?");

            ps3.setDate(1, sDate);
            ps3.setString(2, carno);
            ps3.executeQuery();
            conn.commit();
            ResultSet rs3 = ps3.getResultSet();
            while (rs3.next()) {
                String tempRoute = rs3.getString(9);
                if (isMinus(route, tempRoute)) {//余票减1

                    ps2.setInt(1, remain);
                    ps2.setString(2, rs3.getString(3));
                    ps2.setString(3, rs3.getString(4));
                    ps2.setDate(4, sDate);
                    ps2.setString(5, carno);

                    ps2.executeUpdate();
                    conn.commit();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isMinus(String route, String temproute) {
        //只要route中有一段在temproute中,那么temproute的票就要减一
        String[] splitroute = route.split("-");
        int i = 0;
        for (int j = 0; j < splitroute.length; j++) {
            if (temproute.contains(splitroute[j])) {
                i++;
            }
        }
        if (i >= 2) {
            return true;
        }
        return false;
    }

}
