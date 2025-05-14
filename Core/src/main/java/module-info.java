import dietz.common.services.IGamePlugin;

module Core{
    uses dietz.common.services.IGamePlugin;
    uses dietz.common.services.IEntityProcessing;
    uses dietz.common.services.IPostEntityProcessing;


    requires javafx.graphics;
    requires javafx.controls;
    requires Common;

    opens dietz.core to javafx.graphics;
}