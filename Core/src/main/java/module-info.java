import dietz.common.services.IGamePlugin;

module Core{
    uses IGamePlugin;
    requires javafx.graphics;
    requires javafx.controls;
    requires Common;

    opens dietz.core to javafx.graphics;
}