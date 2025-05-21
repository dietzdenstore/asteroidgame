package dietz.common.ship;

public enum ShipDesign {

    CLASSIC(new double[]{10, 0, -10, -7, -10, 7}),
    ARROWHEAD(new double[]{15, 0, -10, -10, -5, 0, -10, 10}),
    STEALTH(new double[]{12, 0, -8, -8, -4, 0, -8, 8}),
    TRIDENT(new double[]{14,0,0,-3,-12,-6,-4,0,-12,6,0,3}),
    PHANTOM(new double[]{13,0,6,-1,0,-4,-6,-1,-11,0,-6,1,0,4,6,1});



    private final double[] shape;

    ShipDesign(double[] shape) {
        this.shape = shape;
    }

    public double[] getShape() {
        return shape;
    }

    public static ShipDesign random() {
        ShipDesign[] all = values();
        return all[(int) (Math.random() * all.length)];
    }
}
