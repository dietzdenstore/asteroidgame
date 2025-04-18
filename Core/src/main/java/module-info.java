module Core{
    uses dietz.common.GamePlugin;
    requires javafx.graphics;
    requires javafx.controls;
    requires Common;

    opens dietz.core to javafx.graphics;
}