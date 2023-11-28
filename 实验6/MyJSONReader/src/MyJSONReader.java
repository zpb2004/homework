import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import java.awt.geom.Point2D;
import java.io.FileReader;
/**
 * @author HP
 */
public class MyJSONReader {

    private static final double EARTH_RADIUS = 6731;

    public static void main(String[] args) {
        try {
            // 读取 world.json 文件
            JSONReader reader = new JSONReader(new FileReader("world.json"));
            // 从文件中读取 JSON 对象
            JSONObject jsonObject = (JSONObject) reader.readObject();
            // 获取 features 数组
            JSONArray features = jsonObject.getJSONArray("features");
            // 遍历每个 Feature 对象
            for (int i = 0; i < features.size(); i++) {
                JSONObject feature = features.getJSONObject(i);
                // 获取国家名称和地理坐标
                String name = feature.getJSONObject("properties").getString("name");
                JSONArray coordinates = feature.getJSONObject("geometry").getJSONArray("coordinates");
                // 计算国土面积
                double area = calculateArea(coordinates);
                // 输出国家名称和国土面积
                System.out.println("国家：" + name);
                System.out.println("国土面积：" + area + " 平方公里");
                System.out.println();
            }
            // 关闭 JSONReader
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 计算国土面积的方法（示例，需要根据实际情况进行实现）
    private static double calculateArea(JSONArray coordinates) {
        double area = 0.0;

        // 循环遍历每个多边形（或多边形的环）
        for (int i = 0; i < coordinates.size(); i++) {
            JSONArray polygon = coordinates.getJSONArray(i);

            // 遍历多边形的每个顶点
            for (int j = 0; j < polygon.size() - 1; j++) {
                JSONArray point1 = polygon.getJSONArray(j);
                JSONArray point2 = polygon.getJSONArray(j + 1);

                // 分别获取第一个点和第二个点的经纬度
                double lon1 = Math.toRadians(point1.getDoubleValue(0));
                double lat1 = Math.toRadians(point1.getDoubleValue(1));
                double lon2 = Math.toRadians(point2.getDoubleValue(0));
                double lat2 = Math.toRadians(point2.getDoubleValue(1));

                area += haversine(lat1, lon1, lat2, lon2);
            }
        }

        return Math.abs(area);
    }


    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * EARTH_RADIUS * c;
    }

}


