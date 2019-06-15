package nl.java_etl;

import java.util.function.Function;

import nl.java_etl.binding.PojoBindingBuilder;

public class BindTest {
    public static void main(String... args) {
        Function<LatLon, XY> mapping = new PojoBindingBuilder<LatLon, XY>()
                .bind(LatLon::getLon, XY::setX)
                .bind(LatLon::getLat, XY::setY, 25.0)
                .asFunction(XY::new);
        LatLon ll = new LatLon();
        ll.setLon(5.0);
        XY xy = mapping.apply(ll);
        xy.toString();
    }

    public static class LatLon {
        Double lat;
        Double lon;

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLon() {
            return lon;
        }

        public void setLon(Double lon) {
            this.lon = lon;
        }
    }

    public static class XY {
        Double x;
        Double y;

        public Double getX() {
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }

        public Double getY() {
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }
    }
}
