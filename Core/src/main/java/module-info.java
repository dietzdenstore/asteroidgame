import dietz.common.services.IEntityProcessingService;
import dietz.common.services.IGamePluginService;
import dietz.common.services.IPostEntityProcessingService;

module Core{
    uses IGamePluginService;
    uses IEntityProcessingService;
    uses IPostEntityProcessingService;

    requires javafx.graphics;
    requires javafx.controls;

    requires spring.context;
    requires spring.core;
    requires spring.beans;

    requires Common;

    opens dietz.core to javafx.graphics, spring.context, spring.core, spring.beans;
}