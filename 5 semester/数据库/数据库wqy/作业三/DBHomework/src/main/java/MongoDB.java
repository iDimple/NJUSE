import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

public class MongoDB {
    public static void main(String args[]) {

        // 连接到 mongodb 服务
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        // 连接到数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase("ticket");
        System.out.println("Connect to database successfully");
        //初始化数据
        // initDB(mongoDatabase);

        //选择集合
        MongoCollection<Document> collection = mongoDatabase.getCollection("train");


        getRemain(collection);


    }


    public static void getRemain(MongoCollection<Document> collection) {
        long start1 = 0;
        long end1 = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String time = "";
        String start = "";
        String end = "";
        String route = "";
        System.out.println("欢迎来到购票系统!");
        System.out.println("请输入购票日期");
        try {
            time = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("请输入您想在哪一站开始乘车");
        try {
            start = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("请输入您想在哪一站下车");
        try {
            end = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("开始查询余票...");
        start1 = System.currentTimeMillis();
        Document doc = new Document();
        Pattern startP = Pattern.compile(".*" + start + ".*", CASE_INSENSITIVE);
        Pattern endP = Pattern.compile(".*" + end + ".*", CASE_INSENSITIVE);
        doc.put("start", startP);
        doc.put("end", endP);
        doc.put("time", time);
        int i = 0;
        FindIterable<Document> findIterable = collection.find(doc);
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        while (mongoCursor.hasNext()) {
            i++;
            Document temp = mongoCursor.next();
            System.out.println(temp.get("trainNumber") + " " + temp.get("time") + " 出发站:" + temp.get("start") +
                    " 下车站:" + temp.get("end") +
                    " 商务座剩余:" +
                    temp.get("businessRemain") + " 一等座剩余:" +
                    temp.get("firstRemain") + " 二等座剩余:" +
                    temp.get("secondRemain") + " 无座剩余:" +
                    temp.get("noseat"));
        }
        if (i == 0) {
            System.out.println("没有符合类型的车票");
        }


        end1 = System.currentTimeMillis();
        System.out.println("结束查询余票!时间:" + (end1 - start1) + "ms");
        System.out.println("请输入您想乘坐的车次");
        String carno = "";
        String seattype = "";
        int num = 0;
        try {
            carno = br.readLine();
            System.out.println("请输入您想要的座位类型:商务座,一等座,二等座,无座");
            seattype = br.readLine();
            System.out.println("请输入购票数量");
            num = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("开始为您购票安排座位...");
        start1 = System.currentTimeMillis();
        Document doc1 = new Document();
        doc1.put("start", startP);
        doc1.put("end", endP);
        doc1.put("time", time);
        doc1.put("trainNumber", carno);
        FindIterable<Document> findIterable1 = collection.find(doc1);
        MongoCursor<Document> mongoCursor1 = findIterable1.iterator();
        int remain = 0;
        int carriageNo = 0;
        int seatno = 0;
        i = 0;
        if (mongoCursor1.hasNext()) {
            Document temp = mongoCursor1.next();
            if (seattype.equals("商务座")) {//商务座为第一节车厢,200个位置
                remain = temp.getInteger("businessRemain");
            } else if (seattype.equals("一等座")) {//一等座为第2-3个车厢,400个位置
                remain = temp.getInteger("firstRemain");
            } else if (seattype.equals("二等座")) {//二等座为4-8车箱,1000个位置
                remain = temp.getInteger("secondRemain");
            } else {//无座
                remain = temp.getInteger("noseat");
            }
            route = temp.getString("route");
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
                    System.out.println(temp.get("trainNumber") + "  "
                            + temp.get("time") + " 出发站:" + temp.get("start") +
                            " 下车站:" + temp.get("end") +
                            " 车厢号:" +
                            carriageNo + " 座位号:" +
                            seatno + " 座位类型:" +
                            seattype);

                } else {
                    System.out.println(temp.get("trainNumber") + "  "
                            + temp.get("time") + " 出发站:" + temp.get("start") +
                            " 下车站:" + temp.get("end") +
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
        end1 = System.currentTimeMillis();
        System.out.println("结束购票!时间:" + (end1 - start1) + "ms");
        //更新余票


        Document doc3 = new Document();
        doc3.put("time", time);
        doc3.put("trainNumber", carno);
        FindIterable<Document> findIterable3 = collection.find(doc3);
        MongoCursor<Document> mongoCursor3 = findIterable3.iterator();


        while (mongoCursor3.hasNext()) {
            Document temp = mongoCursor3.next();
            String tempRoute = temp.getString("route");
            if (isMinus(route, tempRoute)) {//余票减1
                BasicDBObject newDocument = new BasicDBObject();
                if (seattype.equals("商务座")) {
                    newDocument.append("$set", new BasicDBObject().append("businessRemain", remain));
                } else if (seattype.equals("一等座")) {//一等座为第2-3个车厢,400个位置
                    newDocument.append("$set", new BasicDBObject().append("firstRemain", remain));
                } else if (seattype.equals("二等座")) {//二等座为4-8车箱,1000个位置
                    newDocument.append("$set", new BasicDBObject().append("secondRemain", remain));
                } else {//无座
                    newDocument.append("$set", new BasicDBObject().append("noseat", remain));
                }

                collection.updateOne(doc3, newDocument);
            }
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


    public static void initDB(MongoDatabase mongoDatabase) {
        //创建集合
        // mongoDatabase.createCollection("train");
        //插入文档
        /**
         * 1. 创建文档 org.bson.Document 参数为key-value的格式
         * 2. 创建文档集合List<Document>
         * 3. 将文档集合插入数据库集合中 mongoCollection.insertMany(List<Document>) 插入单个文档可以用 mongoCollection.insertOne(Document)
         * */
        Document document = new Document("trainNumber", "G5").
                append("time", "2016-12-1").append("start", "北京南").append("end", "上海虹桥")
                .append("businessRemain", 200).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "北京南-济南西-南京南-上海虹桥");
        Document document1 = new Document("trainNumber", "G5").
                append("time", "2016-12-1").append("start", "北京南").append("end", "济南西")
                .append("businessRemain", 200).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "北京南-济南西");
        Document document2 = new Document("trainNumber", "G5").
                append("time", "2016-12-1").append("start", "北京南").append("end", "南京南")
                .append("businessRemain", 200).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "北京南-济南西-南京南");
        Document document3 = new Document("trainNumber", "G5").
                append("time", "2016-12-1").append("start", "济南西").append("end", "上海虹桥")
                .append("businessRemain", 200).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "济南西-南京南-上海虹桥");
        Document document4 = new Document("trainNumber", "G5").
                append("time", "2016-12-1").append("start", "南京南").append("end", "上海虹桥")
                .append("businessRemain", 200).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "南京南-上海虹桥");
        Document document5 = new Document("trainNumber", "G5").
                append("time", "2016-12-1").append("start", "济南西").append("end", "南京南")
                .append("businessRemain", 200).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "济南西-南京南");

        Document document6 = new Document("trainNumber", "G6").
                append("time", "2016-12-2").append("start", "上海虹桥").append("end", "北京南")
                .append("businessRemain", 200).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "上海虹桥-南京南-济南西-北京南");
        Document document7 = new Document("trainNumber", "G6").
                append("time", "2016-12-2").append("start", "上海虹桥").append("end", "济南西")
                .append("businessRemain", 200).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "上海虹桥-南京南-济南西");
        Document document8 = new Document("trainNumber", "G6").
                append("time", "2016-12-2").append("start", "上海虹桥").append("end", "南京南")
                .append("businessRemain", 200).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "上海虹桥-南京南");
        Document document9 = new Document("trainNumber", "G6").
                append("time", "2016-12-2").append("start", "南京南").append("end", "北京南")
                .append("businessRemain", 200).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "南京南-济南西-北京南");
        Document document10 = new Document("trainNumber", "G6").
                append("time", "2016-12-2").append("start", "南京南").append("end", "济南西")
                .append("businessRemain", 200).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "南京南-济南西");
        Document document11 = new Document("trainNumber", "G6").
                append("time", "2016-12-2").append("start", "济南西").append("end", "北京南")
                .append("businessRemain", 200).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "济南西-北京南");

        Document document12 = new Document("trainNumber", "G7").
                append("time", "2016-12-2").append("start", "北京南").append("end", "上海虹桥")
                .append("businessRemain", 200).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "北京南-济南西-南京南-上海虹桥");
        Document document13 = new Document("trainNumber", "G7").
                append("time", "2016-12-2").append("start", "北京南").append("end", "济南西")
                .append("businessRemain", 200).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "北京南-济南西");
        Document document14 = new Document("trainNumber", "G7").
                append("time", "2016-12-2").append("start", "北京南").append("end", "南京南")
                .append("businessRemain", 200).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "北京南-济南西-南京南");
        Document document15 = new Document("trainNumber", "G7").
                append("time", "2016-12-2").append("start", "济南西").append("end", "上海虹桥")
                .append("businessRemain", 200).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "济南西-南京南-上海虹桥");
        Document document16 = new Document("trainNumber", "G7").
                append("time", "2016-12-2").append("start", "南京南").append("end", "上海虹桥")
                .append("businessRemain", 200).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "南京南-上海虹桥");
        Document document17 = new Document("trainNumber", "G7").
                append("time", "2016-12-2").append("start", "济南西").append("end", "南京南")
                .append("businessRemain", 200).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "济南西-南京南");


        Document document18 = new Document("trainNumber", "G13").
                append("time", "2016-12-2").append("start", "北京南").append("end", "上海虹桥")
                .append("businessRemain", 0).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "北京南-济南西-南京南-上海虹桥");
        Document document19 = new Document("trainNumber", "G13").
                append("time", "2016-12-2").append("start", "北京南").append("end", "济南西")
                .append("businessRemain", 0).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "北京南-济南西");
        Document document20 = new Document("trainNumber", "G13").
                append("time", "2016-12-2").append("start", "北京南").append("end", "南京南")
                .append("businessRemain", 0).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "北京南-济南西-南京南");
        Document document21 = new Document("trainNumber", "G13").
                append("time", "2016-12-2").append("start", "济南西").append("end", "上海虹桥")
                .append("businessRemain", 0).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "济南西-南京南-上海虹桥");
        Document document22 = new Document("trainNumber", "G13").
                append("time", "2016-12-2").append("start", "南京南").append("end", "上海虹桥")
                .append("businessRemain", 1).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "南京南-上海虹桥");
        Document document23 = new Document("trainNumber", "G13").
                append("time", "2016-12-2").append("start", "济南西").append("end", "南京南")
                .append("businessRemain", 0).append("firstRemain", 400).append("secondRemain", 1000)
                .append("noseat", 200).append("route", "济南西-南京南");

        List<Document> documents = new ArrayList<Document>();
        documents.add(document);
        documents.add(document1);
        documents.add(document2);
        documents.add(document3);
        documents.add(document4);
        documents.add(document5);
        documents.add(document6);
        documents.add(document7);
        documents.add(document8);
        documents.add(document9);
        documents.add(document10);
        documents.add(document11);
        documents.add(document12);
        documents.add(document13);
        documents.add(document14);
        documents.add(document15);
        documents.add(document16);
        documents.add(document17);
        documents.add(document18);
        documents.add(document19);
        documents.add(document20);
        documents.add(document21);
        documents.add(document22);
        documents.add(document23);
        MongoCollection<Document> collection = mongoDatabase.getCollection("train");
        collection.insertMany(documents);
    }

    public static void removeCollection(MongoCollection<Document> collection) {
        collection.drop();
    }
}