public interface GpsListener {

    /**
     * 
     *
     * @param   name 
     * @param   latitude 
     * @param   longitude 
     * @param   altitude 
     */
    public void update(String name, double latitude, double longitude, double altitude);
        /**
     * 
     *
     * @param iotRoutePoint1
     * @param iotRoutePoint2
     * @return
     */
    public List<String[]> getRectanglePoint(IotRoutePoint iotRoutePoint1, IotRoutePoint iotRoutePoint2) {
        List<String[]> list = new ArrayList<>();

        Double mistakeDistance = Double.parseDouble(mistakeDistanceConfig);

        double bearing1 = GpsUtil.bearing(Double.parseDouble(iotRoutePoint1.getLatitude()),
                Double.parseDouble(iotRoutePoint1.getLongitude()),
                Double.parseDouble(iotRoutePoint2.getLatitude()),
                Double.parseDouble(iotRoutePoint2.getLongitude()));

        double bearing2 = bearing1 + 180 > 360 ? (bearing1 + 180 - 360) : bearing1 + 180; //起始点方位角的反向角
        
        String[] gpsLocationTemporary = GpsUtil.getGPSLocation(Double.parseDouble(iotRoutePoint1.getLatitude()), Double.parseDouble(iotRoutePoint1.getLongitude()),
                mistakeDistance, bearing2);

        double bearing3 = bearing1 + 90 > 360 ? (bearing1 + 90 - 360) : (bearing1 + 90);
        
        double bearing4 = bearing1 - 90 < 0 ? (360 - (90 - bearing1)) : (bearing1 - 90);

        String[] gpsLocation1 = GpsUtil.getGPSLocation(Double.parseDouble(gpsLocationTemporary[0]), Double.parseDouble(gpsLocationTemporary[1]), mistakeDistance, bearing3);
        
        String[] gpsLocation2 = GpsUtil.getGPSLocation(Double.parseDouble(gpsLocationTemporary[0]), Double.parseDouble(gpsLocationTemporary[1]), mistakeDistance, bearing4);

        String[] gpsLocationTemporary1 = GpsUtil.getGPSLocation(Double.parseDouble(iotRoutePoint2.getLatitude()), Double.parseDouble(iotRoutePoint2.getLongitude()),
                mistakeDistance, bearing1);

        String[] gpsLocation3 = GpsUtil.getGPSLocation(Double.parseDouble(gpsLocationTemporary1[0]), Double.parseDouble(gpsLocationTemporary1[1]), mistakeDistance, bearing4);

        String[] gpsLocation4 = GpsUtil.getGPSLocation(Double.parseDouble(gpsLocationTemporary1[0]), Double.parseDouble(gpsLocationTemporary1[1]), mistakeDistance, bearing3);

        list.add(gpsLocation1);
        list.add(gpsLocation2);
        list.add(gpsLocation3);
        list.add(gpsLocation4);
        
        list.add(new String[]{iotRoutePoint1.getLatitude(), iotRoutePoint1.getLongitude()});
        list.add(new String[]{iotRoutePoint2.getLatitude(), iotRoutePoint2.getLongitude()});
        return list;
    }
 
 
/**
     * 
     *
     * @param lat1 
     * @param lon1 
     * @param lat2 
     * @param lon2 
     * @return
     */
    public static double bearing(double lat1, double lon1, double lat2, double lon2) {
        double longitude1 = lon1;
        double longitude2 = lon2;
        double latitude1 = Math.toRadians(lat1);
        double latitude2 = Math.toRadians(lat2);
        double longDiff = Math.toRadians(longitude2 - longitude1);
        double y = Math.sin(longDiff) * Math.cos(latitude2);
        double x = Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(longDiff);
        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
    }
 
 
 
/**
     * 
     *
     * @param latitude  
     * @param longitude 
     * @param distance  
     * @param angle     
     * @return
     */
    public static String[] getGPSLocation(double latitude, double longitude, double distance, double angle) {
        String[] result = {"0.0", "0.0"};
        double m_Latitude;
        double m_RadLo, m_RadLa;
        double Ec;
        double Ed;
 
        m_Latitude = latitude;
        m_RadLo = longitude * PI / 180.0;
        m_RadLa = latitude * PI / 180.0;
        Ec = Rj + (Rc - Rj) * (90.0 - m_Latitude) / 90.0;
        Ed = Ec * Math.cos(m_RadLa);
 
        double dx = distance * 1000 * Math.sin(angle * PI / 180.0);
        double dy = distance * 1000 * Math.cos(angle * PI / 180.0);
 
        double BJD = (dx / Ed + m_RadLo) * 180.0 / PI;
        double BWD = (dy / Ec + m_RadLa) * 180.0 / PI;
 
        result[0] = BWD + "";
        result[1] = BJD + "";
        return result;
    }
} 
