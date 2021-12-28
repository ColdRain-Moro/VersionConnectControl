package me.asgard.rain.vcc;

/**
 * me.asgard.rain.vcc.Pair
 * VersionConnectControl
 *
 * @author 寒雨
 * @since 2021/12/28 23:52
 **/
public class Pair<K, V> {

    K first;
    V second;

    public Pair(K first, V second) {
        setFirst(first);
        setSecond(second);
    }

    public K getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    public void setFirst(K first) {
        this.first = first;
    }

    public void setSecond(V second) {
        this.second = second;
    }
}
