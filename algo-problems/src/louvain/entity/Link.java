package louvain.entity;

/**
 * 边
 */
public class Link implements Cloneable {

    public int src;

    public int dst;

    public double weight;

    public Link(int src, int dst, double weight) {
        this.src = src;
        this.dst = dst;
        this.weight = weight;
    }

    public Link clone() {
        try {
            return (Link) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("fail to clone link!", e);
        }
    }

    @Override
    public String toString() {
        return "Link{" +
                "src=" + src +
                ", dst=" + dst +
                ", weight=" + weight +
                '}';
    }

}