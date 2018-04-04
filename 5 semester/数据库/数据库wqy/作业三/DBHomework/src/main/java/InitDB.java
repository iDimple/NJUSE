import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Alisa on 16/11/9.
 */
public class InitDB {
    public static Connection conn;

    public InitDB(Connection conn) {
        this.conn = conn;
    }

    public void createTable() {
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String trainDrop = "drop table if exists train;";
        String trainCreate = "create table train" + "( "
                + "carNumber varchar(255) not null,"
                + "time datetime not null,"
                + "start varchar(255) not null,"
                + "end varchar(255) not null,"
                + "businessRemain int(3) not null,"
                + "firstRemain int(3) not null,"
                + "secondRemain int(3) not null,"
                + "noSeatRemain int(3) not null,"
                + "route varchar(255) not null,"
                + "primary key(carNumber,time,start,end)"
                + " ) default charset=utf8;";

        String seatDrop = "drop table if exists seat;";
        String seatCreate = "create table seat (" +
                "carNumber varchar(255) not null,"
                + "time datetime not null,"
                + "start varchar(255) not null,"
                + "end varchar(255) not null," +
                "carriageNo int(2) not null," +
                "seatNo int(2) not null," +
                "type enum('商务座','一等座','二等座','无座') not null, " +
                "primary key(carNumber,time,start,end,seatNo,carriageNo)" +
                ")default charset=utf8;";

        String routeDrop = "drop table if exists route";
        String routeCreate="create table route(" +
                "carNumber varchar(255) not null," +
                "route varchar(255) not null," +
                "primary key(carNumber)" +
                ")default charset=utf8";

        try {
            Statement statement = conn.createStatement();
            // System.out.println("a");
            statement.addBatch(trainDrop);
            //  System.out.println("b");
            statement.addBatch(trainCreate);
            // System.out.println("c");
            statement.addBatch(seatDrop);
            statement.addBatch(seatCreate);
            statement.addBatch(routeDrop);
            statement.addBatch(routeCreate);
            statement.executeBatch();
            //  System.out.println("d");
            conn.commit();
            //  System.out.println("e");
            System.out.println("初始化数据库成功" );
        } catch (SQLException e) {
            System.out.println("初始化数据库失败" );
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public void insertData(){
        String insertRoute ="load data local infile '/Users/Alisa/Desktop/routes.txt'" +
                "into table route(carNumber,route);";
        String inserttrain ="load data local infile '/Users/Alisa/Desktop/data.txt'" +
                "into table train(carNumber,time,start,end,businessRemain,firstRemain,secondRemain,noSeatRemain,route);";
        try {
            Statement statement=conn.createStatement();
            statement.addBatch(insertRoute);
            statement.addBatch(inserttrain);
            statement.executeBatch();
            conn.commit();
            System.out.println("导入数据成功");
        } catch (SQLException e) {
            System.out.println("导入数据失败");
            e.printStackTrace();
        }



    }
}
